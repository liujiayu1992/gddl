package com.zhiren.dtrlgs.jihgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiaosyjhwh extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}

//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

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
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
	
	// 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;
	
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}
	
	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
					}
				
			}
		}
		return _NianfValue;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2008; i <= DateUtil.getYear(new Date()); i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)+"年"));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}


	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
				if(i<10){
					listYuef.add(new IDropDownBean(i, "0"+String.valueOf(i)));
				}else{
					listYuef.add(new IDropDownBean(i, String.valueOf(i)));
				}
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} 
		else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}
	public void save() {
		StringBuffer sSql = new StringBuffer("begin \n");
		long id = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList dlrsl = getExtGrid().getDeleteResultSet(getChange());
		if (dlrsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fayxxwh.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (dlrsl.next()) {
			id = dlrsl.getInt("id");
			sSql.append("delete from XIAOSYJHB where id=" + id+";\n");
		}
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		if(mdrsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fayxxwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String dianc=getTreeid();
		while (mdrsl.next()) {
			id = mdrsl.getInt("id");
			String pinz=getExtGrid().getValueSql(getExtGrid().getColumn("PINZ"),mdrsl.getString("PINZ"));
			String gongys=getExtGrid().getValueSql(getExtGrid().getColumn("GONGYS"),mdrsl.getString("GONGYS"));
			String meikdw=getExtGrid().getValueSql(getExtGrid().getColumn("MEIKDW"),mdrsl.getString("MEIKDW"));
			String meil=mdrsl.getString("MEIL");
			String frl=mdrsl.getString("FARL");
			String beiz=mdrsl.getString("BEIZ");			
			String riq="to_date('"+mdrsl.getString("RIQ")+"','yyyy-mm-dd')";
			if (id == 0) {
				sSql.append("insert into XIAOSYJHB (ID,DIANCXXB_ID,RIQ,PINZB_ID,")
					.append("GONGYSB_ID,MEIKXXB_ID,MEIL,FARL,BEIZ,LURRY,LURSJ) values(");
				sSql.append("getNewId(" + visit.getDiancxxb_id() + "),\n")
					.append(dianc+",\n")
					.append(riq+",\n")
					.append(pinz+",\n")
					.append(gongys+",\n")
					.append(meikdw+",\n")
					.append(meil+",\n")
					.append(frl+",\n '")
					.append(beiz+"',\n")
					.append(visit.getRenyID()+",sysdate); \n");
					
			} else {
				sSql.append("update xiaosyjhb set PINZB_ID=")
					.append(pinz)
					.append(",gongysb_id="+gongys)
					.append(",meikxxb_id="+meikdw)
					.append(",meil="+meil)
					.append(",farl="+frl)
					.append(",beiz='"+beiz+"' ")
					.append(" where id="+id)
					.append(";\n");
					
			}
		}
		dlrsl.close();
		mdrsl.close();
		sSql.append("end;");
		if(con.getUpdate(sSql.toString())>=0){
			
			setMsg("保存成功！");
		}else {
			
			setMsg("保存失败！");
		}
		con.Close();
	}

	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
		
		String riq="\n and x.riq=to_date('"+getNianfValue().getStrId()+"-"+getYuefValue().getValue()+"-01','yyyy-mm-dd')";

		String xiaoSql=	"select x.id as id, d.mingc as xuqdw, x.riq as riq, " +
			"pz.mingc as pinz,g.mingc as gongys,\n" +
			"m.mingc as meikdw,x.meil as meil,x.farl as farl,x.beiz as beiz," +
			"x.lurry as lurry,x.lursj as lursj\n" + 
			"from XIAOSYJHB x, diancxxb d,meikxxb m,\n" + 
			"vwpinz pz,(select id,mingc from gongysb) g\n" + 
			"where x.diancxxb_id=d.id\n" + 
			"and x.pinzb_id=pz.id\n" + 
			"and x.gongysb_id=g.id\n" + 
			"and x.meikxxb_id=m.id(+) \n" +str+riq;

		ResultSetList rsl = con.getResultSetList(xiaoSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		
		egu.setTableName("XIAOSYJHB");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("xuqdw").setHeader("需求单位");
		egu.getColumn("xuqdw").setWidth(120);
		ComboBox xuqdw = new ComboBox();
		egu.getColumn("xuqdw").setEditor(xuqdw);
//		xuqdw.setEditable(false);
		xuqdw.setReadOnly(true);
		String xqdw = "select id, mingc from diancxxb order by mingc";
		egu.getColumn("xuqdw").setComboEditor(egu.gridId,
				new IDropDownModel(xqdw));
		egu.getColumn("xuqdw").setReturnId(true);
		ResultSetList rsl2=con.getResultSetList("select mingc from diancxxb where id="+this.getTreeid());
		if(rsl2.next()){
		egu.getColumn("xuqdw").setDefaultValue(rsl2.getString("mingc"));
		}
		rsl2.close();
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(getNianfValue().getStrId()+"-"+getYuefValue().getValue()+"-01");
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		ComboBox pinz = new ComboBox();
		egu.getColumn("pinz").setEditor(pinz);
		pinz.setEditable(true);
		String pz = "select id,mingc from vwpinz order by mingc";
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pz));
		egu.getColumn("pinz").setReturnId(true);
		
//		供应商名称
		egu.getColumn("gongys").setHeader("供应商名称");
		egu.getColumn("gongys").setWidth(120);
		ComboBox gongys = new ComboBox();
		egu.getColumn("gongys").setEditor(gongys);
		gongys.setEditable(true);
		String gongysSql = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongys").setComboEditor(egu.gridId,
				new IDropDownModel(gongysSql));
		egu.getColumn("gongys").setReturnId(true);
		
//		煤矿单位
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setWidth(120);
		ComboBox meikdw = new ComboBox();
		egu.getColumn("meikdw").setEditor(meikdw);
		meikdw.setEditable(true);
		String meikdwSql = "select id,mingc from meikxxb order by xuh";
		egu.getColumn("meikdw").setComboEditor(egu.gridId,
				new IDropDownModel(meikdwSql));
		egu.getColumn("meikdw").setReturnId(true);
		egu.getColumn("meikdw").editor.setAllowBlank(true);
		
		egu.getColumn("meil").setHeader("煤量");
		egu.getColumn("meil").setWidth(70);
		egu.getColumn("meil").setDefaultValue("0.00");
		((NumberField)egu.getColumn("meil").editor).setDecimalPrecision(2);

		
		egu.getColumn("farl").setHeader("发热量");
		egu.getColumn("farl").setWidth(80);
		((NumberField)egu.getColumn("farl").editor).setDecimalPrecision(3);
		egu.getColumn("farl").setDefaultValue("0.000");
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		
		egu.getColumn("lurry").setHeader("录入人员");
		egu.getColumn("lurry").setWidth(60);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setDefaultValue(visit.getRenyID()+"");
		egu.getColumn("lurry").setHidden(true);
		
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setWidth(60);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("lursj").setHidden(true);
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("年份：");
		ComboBox nianf=new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setListeners("select:function(){document.Form0.submit();}");
		nianf.setWidth(60);
		egu.addToolbarItem(nianf.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("月份：");
		ComboBox yuef=new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setListeners("select:function(){document.Form0.submit();}");
		yuef.setWidth(60);
		egu.addToolbarItem(yuef.getScript());
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
//		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","var bobMrcd = gridDiv_ds.getModifiedRecords();for(var i=0;i<bobMrcd.lenght;i++){if(Mrcd[i].get('MEIL') == ''){Ext.MessageBox.alert('提示信息','字段 煤量 不能为空');return;}}");
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		
		StringBuffer sbOtherScript =new StringBuffer();
		sbOtherScript.append("gridDiv_grid.on('afteredit',function(e){ ");
		sbOtherScript.append("if(e.field == 'MEIL' && e.record.get('MEIL')=='')" +
							"{e.record.set('MEIL','0');}")
							.append("if(e.field == 'FARL' && e.record.get('FARL')=='')" +
							"{e.record.set('FARL','0');}");
		sbOtherScript.append("});\n");
		sbOtherScript.append("gridDiv_grid.on('beforeedit',function(e){ ");
		sbOtherScript.append("if(e.field == 'XUQDW')" +
							"{e.cancel=true;}else{e.cancel=false;}");
		sbOtherScript.append("});\n");
		egu.addOtherScript(sbOtherScript.toString());
		
		setExtGrid(egu);
		con.Close();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
			setNianfModel(null);
			setNianfValue(null);
			setYuefModel(null);
			setYuefValue(null);
		}
		getSelectData();
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
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

}
