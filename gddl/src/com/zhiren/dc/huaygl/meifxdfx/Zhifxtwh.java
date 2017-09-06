package com.zhiren.dc.huaygl.meifxdfx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*作者:王总兵
 *日期:2010-4-25 14:46:38
 *描述:保存方法的update更新语句,最后少".append(";\n")"导致多行更新不成功.进行修改
 * 
 */
/**
 * @author yinjm
 * 类名：制粉系统维护
 */

public class Zhifxtwh extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl =  visit.getExtGrid1().getDeleteResultSet(getChange());
		while(delrsl.next()) {
			sbsql.append("delete from zhifxtb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into zhifxtb(id, xuh, mingc) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append(mdrsl.getString("xuh")).append(", '")
				.append(mdrsl.getString("mingc")).append("');\n");
			} else {
				sbsql.append("update zhifxtb set ")
				.append("xuh = ").append(mdrsl.getString("xuh"))
				.append(", mingc = '").append(mdrsl.getString("mingc"))
				.append("' where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql = "select id, xuh, mingc from zhifxtb order by xuh";
		
		ResultSetList rsl =  con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("mingc").setHeader("制粉系统名称");
		
		GridButton btn = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		btn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(btn);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		String condition = 
			"var xuhMrcd = gridDiv_ds.getModifiedRecords();\n" +
			"for(var i = 0; i< xuhMrcd.length; i++){\n" + 
			"    if(xuhMrcd[i].get('XUH') == '' || xuhMrcd[i].get('XUH') == null){\n" + 
			"        Ext.MessageBox.alert('提示信息','字段 序号 不能为空');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"}";
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", condition);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
		}
		getSelectData();
	}
}