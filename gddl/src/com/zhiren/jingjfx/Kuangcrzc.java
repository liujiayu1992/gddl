package com.zhiren.jingjfx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-11-21
 * 描述：修改矿厂热值差添加时自动加载默认日期，不自动计算热值差累计项，供应商从当年月报中选取
 */
public class Kuangcrzc extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//
//	public int getDataColumnCount() {
//		int count = 0;
//		for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
//			if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
//				count++;
//			}
//		}
//		return count;
//	}
	
	private void Save() {

		Visit visit = (Visit) this.getPage().getVisit();		
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		JDBCcon con=new JDBCcon();
//		String Mname="";
		while (delrsl.next()){//删除 
			sql.append("delete from kuangcrzcb where\n");
			sql.append("id=" + delrsl.getString("id"));
			sql.append(";\n");
		}
		ResultSetList mdfrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (mdfrsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "ShujblH.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (mdfrsl.next()) {
			if("0".equals(mdfrsl.getString("id"))){//增加
				sql.append(
						"insert into kuangcrzcb(id,diancxxb_id,riq,gongysb_id,yiy,ery,sany,siy,wuy,liuy,\n" +
						"qiy,bay,jiuy,shiy,shiyy,shiey,leij,zhib) values(\n" + 
						"getnewid("+ visit.getDiancxxb_id() + ")," +
						(getExtGrid().getColumn("dmingc").combo).getBeanId(mdfrsl.getString("dmingc"))+"," +
						"trunc(to_date('"+mdfrsl.getString("riq")+"','yyyy-mm-dd'),'yyyy')," +
						(getExtGrid().getColumn("gmingc").combo).getBeanId(mdfrsl.getString("gmingc"))+"," +
						mdfrsl.getInt("yiy") + "," +
						mdfrsl.getInt("ery") + "," +
						mdfrsl.getInt("sany") + "," +
						mdfrsl.getInt("siy") + "," +
						mdfrsl.getInt("wuy") + "," +
						mdfrsl.getInt("liuy") + "," +
						mdfrsl.getInt("qiy") + "," +
						mdfrsl.getInt("bay") + "," +
						mdfrsl.getInt("jiuy") + "," +
						mdfrsl.getInt("shiy") + "," +
						mdfrsl.getInt("shiyy") + "," +
						mdfrsl.getInt("shiey") + "," +
						mdfrsl.getInt("leij") + "," +
						"'"+mdfrsl.getString("zhib") + "'" +
						"\n" + 
						");");
			}else{	//修改
				sql.append(
						"update kuangcrzcb set\n" +
						"diancxxb_id=" + (getExtGrid().getColumn("dmingc").combo).getBeanId(mdfrsl.getString("dmingc")) +" ,\n" + 
						"riq=" + "trunc(to_date('"+mdfrsl.getString("riq")+"','yyyy-mm-dd'),'yyyy')" +" ,\n" + 
						"gongysb_id=" + (getExtGrid().getColumn("gmingc").combo).getBeanId(mdfrsl.getString("gmingc")) +" ,\n" + 
						"yiy=" + mdfrsl.getInt("yiy") +" ,\n" + 
						"ery=" + mdfrsl.getInt("ery") +" ,\n" + 
						"sany=" + mdfrsl.getInt("sany") +" ,\n" + 
						"siy=" + mdfrsl.getInt("siy") +" ,\n" + 
						"wuy=" + mdfrsl.getInt("wuy") +" ,\n" + 
						"liuy=" + mdfrsl.getInt("liuy") +" ,\n" + 
						"qiy=" + mdfrsl.getInt("qiy") +" ,\n" + 
						"bay=" + mdfrsl.getInt("bay") +" ,\n" + 
						"jiuy=" + mdfrsl.getInt("jiuy") +" ,\n" + 
						"shiy=" + mdfrsl.getInt("shiy") +" ,\n" + 
						"shiyy=" + mdfrsl.getInt("shiyy") +" ,\n" + 
						"shiey=" + mdfrsl.getInt("shiey") +" ,\n" + 
						"leij=" + mdfrsl.getInt("leij") +" ,\n" + 
						"zhib='" + mdfrsl.getString("zhib") +"' \n" + 
						"where id="+mdfrsl.getString("id")+";\n");
			}
		}
		sql.append("end;");
		if(!(con.getUpdate(sql.toString())>-1)){
			this.setMsg("保存成功！");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshButton = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshButton) {
			_RefreshButton = false;
			getSelectData();
		}
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = " and d.jib=3 ";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			//str = " and dc.fuid = "+ getTreeid() + "";
			str = " and  (d.fuid = "+ getTreeid() + " or d.shangjgsid= "+this.getTreeid()+")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and d.id = " + getTreeid() + "";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select\n" +
			"k.id,d.mingc dmingc,TRUNC(k.riq,'yyyy') riq,g.mingc gmingc,yiy,ery,sany,siy,wuy,liuy,qiy,bay,jiuy,shiy,shiyy,shiey,k.leij,k.zhib\n" + 
			"from kuangcrzcb k,diancxxb d,gongysb g\n" + 
			"where k.diancxxb_id=d.id\n" + 
			"  and k.gongysb_id=g.id\n"+ str);
		if(getNianValue().getId()!=-1){
			sql.append("  and to_char(riq,'yyyy')=" + getNianValue());	
		}
		ResultSetList rsl = con.getResultSetList(sql.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("kuangcrzcb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("dmingc").setHeader("单位名称");
//		egu.getColumn("dmingc").setEditor(null);
		egu.getColumn("dmingc").setWidth(90);
		egu.getColumn("dmingc").setDefaultValue(getTreeName());
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(70);
		egu.getColumn("riq").setDefaultValue(getNianValue()+"-01-01");
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("gmingc").setHeader("供应商");
		egu.getColumn("gmingc").setWidth(90);
		
		egu.getColumn("gmingc").setEditor(new ComboBox());
		String gongys_sql= 
			"select distinct g.id,g.mingc\n" +
			"from yuetjkjb y,gongysb g\n" + 
			"where y.gongysb_id = g.id\n" + 
			"and y.diancxxb_id = "+(getTreeid()==null?"100":getTreeid())+"\n" + 
			"and to_char(y.riq,'yyyy') = " + (getNianValue()==null?String.valueOf(DateUtil.getYear(new Date())):getNianValue().getValue());
		egu.getColumn("gmingc").setComboEditor(egu.gridId,	new IDropDownModel(gongys_sql));
		egu.getColumn("gmingc").setReturnId(true);
		
		egu.getColumn("yiy").setHeader("一月");
		egu.getColumn("yiy").setWidth(45);
		
		egu.getColumn("ery").setHeader("二月");
		egu.getColumn("ery").setWidth(45);
		
		egu.getColumn("sany").setHeader("三月");
		egu.getColumn("sany").setWidth(45);
		
		egu.getColumn("siy").setHeader("四月");
		egu.getColumn("siy").setWidth(45);
		
		egu.getColumn("wuy").setHeader("五月");
		egu.getColumn("wuy").setWidth(45);
		
		egu.getColumn("liuy").setHeader("六月");
		egu.getColumn("liuy").setWidth(45);
		
		egu.getColumn("qiy").setHeader("七月");
		egu.getColumn("qiy").setWidth(45);
		
		egu.getColumn("bay").setHeader("八月");
		egu.getColumn("bay").setWidth(45);
		
		egu.getColumn("jiuy").setHeader("九月");
		egu.getColumn("jiuy").setWidth(45);
		
		egu.getColumn("shiy").setHeader("十月");
		egu.getColumn("shiy").setWidth(50);
		
		egu.getColumn("shiyy").setHeader("十一月");
		egu.getColumn("shiyy").setWidth(45);
		
		egu.getColumn("shiey").setHeader("十二月");
		egu.getColumn("shiey").setWidth(45);
		
		egu.getColumn("leij").setHeader("累计");
		egu.getColumn("leij").setWidth(60);
		
		egu.getColumn("zhib").setHeader("年度目标");
		egu.getColumn("zhib").setWidth(60);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		//单位名称下拉框
		//电厂下拉框
		int treejib2 = this.getDiancTreeJib();
		if (treejib2 == 1) {// 选集团时刷新出所有的电厂
			egu.getColumn("dmingc").setEditor(new ComboBox());
			egu.getColumn("dmingc").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("dmingc").setReturnId(true);
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("dmingc").setEditor(new ComboBox());
			egu.getColumn("dmingc").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
			egu.getColumn("dmingc").setReturnId(true);
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			egu.getColumn("dmingc").setEditor(new ComboBox());
			egu.getColumn("dmingc").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
//			egu.getColumn("dmingc").setHidden(true);
			egu.getColumn("dmingc").setDefaultValue(mingc);
		}	
//		 工具栏
		// 电厂树
		egu.addTbarText("年份：");

		ComboBox comb = new ComboBox();
		comb.setTransform("NianDropDown");
		comb.setId("Nian");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(70);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		/*egu.addOtherScript(
				"gridDiv_grid.on('afteredit',function(e){" +
				"var rec=e.record;\n" +
				"if( e.field=='YIY' || e.field=='ERY' || e.field=='SANY' || e.field=='SIY' || e.field=='WUY' || e.field=='LIUY'" +
				" || e.field=='QIY' || e.field=='BAY' || e.field=='JIUY' || e.field=='SHIY' || e.field=='SHIYY' || e.field=='SHIEY'){\n" +
				" rec.set('LEIJ',parseFloat(rec.get('YIY')||'0')+parseFloat(rec.get('ERY')||'0')+parseFloat(rec.get('SANY')||'0')+parseFloat(rec.get('SIY')||'0')+parseFloat(rec.get('WUY')||'0')+parseFloat(rec.get('LIUY')||'0')+parseFloat(rec.get('QIY')||'0')+parseFloat(rec.get('BAY')||'0')+parseFloat(rec.get('JIUY')||'0')+parseFloat(rec.get('SHIY')||'0')+parseFloat(rec.get('SHIYY')||'0')+parseFloat(rec.get('SHIEY')||'0'));"+
				"} \n" +
				"});\n" +
				"if(gridDiv_ds!=null){\n" +
				" var res=gridDiv_ds.getRange();\n" +
				
				" var i=0;"+
				"while(res!=null && res.length>0 && i<res.length ){" +
				
				"if(res[i].get('YIY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',5).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('ERY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',6).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('SANY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',7).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('SIY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',8).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('WUY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',9).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('LIUY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',10).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('QIY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',11).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('BAY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',12).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('JIUY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',13).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('SHIY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',14).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('SHIYY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',15).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('SHIEY')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',16).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				
				"if(res[i].get('LEIJ')<='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getCell('+i+',17).style.backgroundColor=\"red\";'; " +
				" eval(colorStr);\n" +
				"}\n" +
				"i++;" +
				"}" +
				"}");*/
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setTreeid(null);
		}
		getSelectData();
	}

	

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeName(){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=con.getResultSetList("select mingc from diancxxb where id=" + getTreeid());
		if(rsl.next()){
			return rsl.getString("mingc");
		}else{
			return "";
		}
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
	
//	 供货单位
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {
			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		String sql = 
			"select distinct g.id,g.mingc\n" +
			"from yuetjkjb y,gongysb g\n" + 
			"where y.gongysb_id = g.id\n" + 
			"and y.diancxxb_id = "+(getTreeid()==null?"100":getTreeid())+"\n" + 
			"and to_char(y.riq,'yyyy') = " + (getNianValue()==null?String.valueOf(DateUtil.getYear(new Date())):getNianValue().getValue());
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
//	 年份
	public IDropDownBean getNianValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			if(getNianModel().getOptionCount()>0){
				for(int i =0; i < getNianModel().getOptionCount();i++){
					if(((IDropDownBean)getNianModel().getOption(i)).getId() == DateUtil.getYear(new Date())){
						setNianValue((IDropDownBean)getNianModel().getOption(i));
					}
				}
			}
		}
		if(((Visit) getPage().getVisit()).getDropDownBean10() == null){
			setNianValue(new IDropDownBean((long)DateUtil.getYear(new Date()),
					String.valueOf(DateUtil.getYear(new Date()))));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setNianValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}

	public void setNianModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getNianModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getNianModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getNianModels() {
		String sql = "select yvalue,ylabel from nianfb";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
}
