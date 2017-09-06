package com.zhiren.jingjfx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import org.mozilla.javascript.tools.shell.Global;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.zidy.ZidyOperation;

/**
 * 为经济分析模块的jjfxdxmk表增加的增删改查维护界面
 * @author Elwin
 *
 */
/*
 * 作者：夏峥
 * 时间：2011-06-14
 * 描述：保存方法中，得到ID的方式由于Int型变量长度不够，因此转换为long型
 */

public class Jingjfxmk extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		String sql = "begin \n";
		while(rs.next()){
			sql += "delete from jjfxdxmk where id = "+ rs.getString("id") + ";\n";
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
//			由于Int型变量长度不够，因此转换为long型
			if(rs.getLong("id") == 0){
				String id = MainGlobal.getNewID(v.getDiancxxb_id());
				sql += "insert into jjfxdxmk(id,bianm,mingc,beiz) values("
					+ id + "," + id +",'" + rs.getString("mingc") + "','"
					+ rs.getString("beiz") + "');\n";
			}else{
				sql += "update jjfxdxmk set mingc = '"+ rs.getString("mingc") 
					+"',beiz = '" + rs.getString("beiz") 
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
		Visit visit=(Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select id,bianm,mingc,beiz from jjfxdxmk");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jjfxdxmk");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("bianm").setHidden(true);
		egu.getColumn("bianm").setWidth(120);
		
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(150);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(0);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
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
		}
		getSelectData();
	}

}
