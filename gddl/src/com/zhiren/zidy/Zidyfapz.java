package com.zhiren.zidy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zidyfapz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	private String Parameters;

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		String sql = "begin \n";
		while(rs.next()){
			sql += "delete from zidyfa where id = "+ rs.getString("id") + ";\n";
			ZidyOperation.DeleteReportSet(con,rs.getString("id"));
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getDelete(sql);
		}
		con.commit();
		rs.close();
		
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while(rs.next()){
			if(rs.getInt("id") == 0){
				String id = MainGlobal.getNewID(v.getDiancxxb_id());
				sql += "insert into zidyfa(id,z_report_id,z_name,z_designer,z_time,z_remark) values("
					+ id + "," + id +",'" + rs.getString("z_name") + "','"
					+ rs.getString("z_designer") + "',sysdate,'"+ rs.getString("z_remark") + "');\n";
			}else{
				sql += "update zidyfa set z_name = '"+ rs.getString("z_name") 
					+"',z_designer = '" + rs.getString("z_designer") 
					+"',z_remark = '"+ rs.getString("z_remark")
					+"' where id = "+rs.getString("id") + " ;\n";
			}
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getInsert(sql);
		}
		con.commit();
		rs.close();
	}

	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}

	private void Create(IRequestCycle cycle) {
		Visit v = (Visit) this.getPage().getVisit();
		v.setString1(getChange());
		cycle.activate("Zidyfapz_sjy");
	}

	private boolean _XiugChick = false;

	public void XiugButton(IRequestCycle cycle) {
		_XiugChick = true;
	}

	private boolean _ZengjChick = false;

	public void ZengjButton(IRequestCycle cycle) {
		_ZengjChick = true;
	}
	
	public void SaveReportSet(){
		String zo = ZidyOperation.SaveReportSet(getChange());
		if(zo==null)
			setMsg("报表配置成功！");
		else
			setMsg("报表配置失败！");
			
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateChick) {
			_CreateChick = false;
			Create(cycle);
		}
		if (_XiugChick) {
			_XiugChick = false;
			getSelectData();
		}
		if (_ZengjChick) {
			_ZengjChick = false;
			SaveReportSet();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,z_report_id,z_name,\n" +
				"z_designer, z_time,z_remark from zidyfa\n" +
				"where z_remark is not null\n" +
				"order by z_report_id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidyfa");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("z_report_id").setHeader("编号");
		egu.getColumn("z_report_id").setEditor(null);
		egu.getColumn("z_report_id").setDefaultValue(
				"getnewid(" + visit.getDiancxxb_id() + ")");
		egu.getColumn("z_name").setHeader("方案名称");
		egu.getColumn("z_name").setWidth(300);
		egu.getColumn("z_designer").setHeader("设计人");
		egu.getColumn("z_time").setHeader("设计时间");
		egu.getColumn("z_time").setWidth(200);
		egu.getColumn("z_time").setDefaultValue("" + getRiqi());
		egu.getColumn("z_time").setEditor(null);
		egu.getColumn("z_remark").setHeader("备注");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		//egu.setWidth(1024);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		// String str = " var url =
		// 'http://'+document.location.host+document.location.pathname;"
		// + "var end = url.indexOf(';');"
		// + "url = url.substring(0,end);"
		// + "url = url + '?service=page/' + 'Chezreport&lx=rezc';"
		// + " window.open(url,'newWin');";
		// egu.addToolbarItem("{"
		// + new GridButton("打印", "function (){" + str + "}").getScript()
		// + "}");

		// ////////////////////////////增加其它按钮以及配置按钮路径///////////////////////////////////
//		String str1 = " if(gridDiv_sm.getSelections().length <= 0 "
//				+ "|| gridDiv_sm.getSelections().length > 1){ \n"
//				+ " 	Ext.MessageBox.alert('提示信息','请选择一个自定义方案!'); \n"
//				+ " 	return;" + "}" + " var rec = gridDiv_sm.getSelected(); \n"
//				+ " document.getElementById('CHANGE').value=rec.get('ID'); \n"
//				+ " document.getElementById('CreateButton').click(); \n";
//
//		egu.addToolbarItem("{"
//				+ new GridButton("创建报表", "function(){" + str1 + "}")
//						.getScript() + "}");

		/*ComboBox comb3 = new ComboBox();
		comb3.setTransform("TiaojDropDown");
		comb3.setId("tiaoj");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());
		String str2 =
		// " if(gridDiv_sm.getSelections().length <= 0 "
		// + "|| gridDiv_sm.getSelections().length > 1){ \n"
		// + " Ext.MessageBox.alert('提示信息','请选择一个自定义方案!'); \n"
		// + " return;"
		// + "}"
		// + " var rec = gridDiv_sm.getSelected(); \n"
		// + "}"
		// + " gridDiv_history = rec.get('ID')+','+tiaoj.getRawValue(); \n"
		// + " document.getElementById('PARAMETERS').value=gridDiv_history; \n"
		" document.getElementById('XiugButton').click(); \n";
		//
		egu.addToolbarItem("{"
				+ new GridButton("修改", "function(){" + str2 + "}").getScript()
				+ "}");*/

//		String str3 =
//			" if(gridDiv_sm.getSelections().length <= 0 "
//			+ "|| gridDiv_sm.getSelections().length > 1){ \n"
//			+ " 	Ext.MessageBox.alert('提示信息','请选择一个自定义方案!'); \n"
//			+ " 	return;" + "}" + " var rec = gridDiv_sm.getSelected(); \n"
//			+ " document.getElementById('CHANGE').value=rec.get('ID'); \n"
//			+ " document.getElementById('ZengjButton').click(); \n";
//
//		egu.addToolbarItem("{"
//				+ new GridButton("生成方案配置", "function(){" + str3 + "}")
//						.getScript() + "}");

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

	public IDropDownBean getTiaojValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getITiaojModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setTiaojValue(IDropDownBean Value) {

		if (((Visit) getPage().getVisit()).getDropDownBean1() != Value) {

			// ((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setITiaojModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getITiaojModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getITiaojModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getITiaojModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "调整显示位置"));
		list.add(new IDropDownBean(1, "设置查询条件"));
		list.add(new IDropDownBean(2, "设置其他参数"));
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(list));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}
		getSelectData();
	}
}
