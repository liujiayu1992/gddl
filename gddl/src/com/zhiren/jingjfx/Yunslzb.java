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
 * 时间：2009-11-22
 * 描述：修改保存及刷新添加年份下拉框
 */
public class Yunslzb extends BasePage implements PageValidateListener {
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
	
	private void Save() {

		Visit visit = (Visit) this.getPage().getVisit();		
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		JDBCcon con=new JDBCcon();
//		String Mname="";
		while (delrsl.next()){//删除 
			sql.append("delete from yunslzbb where\n");
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
						"insert into yunslzbb(id,diancxxb_id,yunsfsb_id,riq,zhib) values(\n" +
						"getnewid(" + visit.getDiancxxb_id() + "),\n" + 
						(getExtGrid().getColumn("dmingc").combo).getBeanId(mdfrsl.getString("dmingc")) +",\n" + 
						(getExtGrid().getColumn("ymingc").combo).getBeanId(mdfrsl.getString("ymingc")) +",\n" + 
						"to_date('"+mdfrsl.getString("riq") +"','yyyy-mm-dd'),\n" + 
						"'" + mdfrsl.getString("zhib") +"'\n" + 
						");");
			}else{	//修改
				sql.append(
						"update yunslzbb set\n" +
						"diancxxb_id=" + (getExtGrid().getColumn("dmingc").combo).getBeanId(mdfrsl.getString("dmingc")) + "," +
						"yunsfsb_id=" + (getExtGrid().getColumn("ymingc").combo).getBeanId(mdfrsl.getString("ymingc")) + "," +
						"riq=to_date('" + mdfrsl.getString("riq") + "','yyyy-mm-dd'),\n" + 
						"zhib='" + mdfrsl.getString("zhib") + "'\n" + 
						"where id=" + mdfrsl.getString("id") + ";\n");
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
		sql.append(
				"select y.id,d.mingc dmingc,ysf.mingc ymingc,y.riq,y.zhib\n" +
				"from yunslzbb y,diancxxb d,yunsfsb ysf\n" + 
				"where y.diancxxb_id=d.id and to_char(y.riq,'yyyy') = "+ 
				getNianValue().getValue() + "\n" +
				"  and y.yunsfsb_id=ysf.id(+)" + str);
		
		ResultSetList rsl = con.getResultSetList(sql.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yunslzbb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("dmingc").setHeader("单位名称");
		egu.getColumn("dmingc").setWidth(100);
		egu.getColumn("dmingc").setDefaultValue(getTreeName());
		
		egu.getColumn("ymingc").setHeader("运输方式");		
		egu.getColumn("ymingc").setEditor(new ComboBox());
		String Yunsfsb_sql="select id,mingc from yunsfsb ";
		egu.getColumn("ymingc").setComboEditor(egu.gridId, new IDropDownModel(Yunsfsb_sql));
		egu.getColumn("ymingc").editor.allowBlank = true;
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(70);
		egu.getColumn("riq").setDefaultValue(getNianValue()+"-01-01");
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("zhib").setHeader("指标");
		egu.getColumn("zhib").setWidth(100);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
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
			setNianModel(null);
			setNianValue(null);
			setYunsfsbModel(null);
			setYunsfsbValue(null);
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
	
//	 运输方式
	public IDropDownBean getYunsfsbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getYunsfsbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setYunsfsbValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean9(value);
	}

	public void setYunsfsbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getYunsfsbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getYunsfsbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getYunsfsbModels() {
		String sql = "select id,mingc from yunsfsb";
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
	
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
