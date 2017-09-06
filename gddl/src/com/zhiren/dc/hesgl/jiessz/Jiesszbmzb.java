package com.zhiren.dc.hesgl.jiessz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jiesszbmzb extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			cycle.activate("Jiesszbmb");
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		 visit.getExtGrid1().Save(getChange(), visit);
	}

	
	
	public void getSelectData(String Param) {
	    Visit visit = (Visit) getPage().getVisit();
	    String str_lb="";
	    
		JDBCcon con = new JDBCcon();
		int ID=Integer.parseInt(Param.substring(0,Param.lastIndexOf(',')));
		str_lb=Param.substring(Param.lastIndexOf(',')+1);
		try{
		String str="select gl.id as id,bm.id as jiesszbmb_id,bm.bianm as jiesszbmbmc ,zb.bianm as jiesszbmzb_id from jiesszbmb bm,jiesszbmzb zb,jiesszbmglb gl"+
		            " where gl.jiesszbmb_id=bm.id"+
	                " and gl.jiesszbmzb_id=zb.id" +
	                " and bm.id="+ID;
		ResultSetList rsl = con.getResultSetList(str);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("JIESSZBMGLB");
		egu.getColumn("jiesszbmb_id").setHeader("id");	
		egu.getColumn("jiesszbmb_id").setHidden(true);
		egu.getColumn("jiesszbmb_id").setDefaultValue(""+ID);
		egu.getColumn("jiesszbmbmc").setHeader("名称");	
		egu.getColumn("jiesszbmbmc").setWidth(150);
		egu.getColumn("jiesszbmbmc").setUpdate(false);
		egu.getColumn("jiesszbmzb_id").setHeader("编码");
		egu.getColumn("jiesszbmzb_id").setWidth(230);
	    
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		egu.getColumn("jiesszbmbmc").setDefaultValue(str_lb);
		egu.getColumn("jiesszbmzb_id").setEditor(new ComboBox());
		egu.getColumn("jiesszbmzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,bianm from jiesszbmzb order by bianm"));
		egu.getColumn("jiesszbmzb_id").setReturnId(true);
	
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    egu.addToolbarItem("{"+new GridButton("返回","function(){ document.getElementById('ReturnButton').click();" +
		"}").getScript()+"}");
		setExtGrid(egu);
		
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
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
			visit.setList1(null);
			getSelectData(visit.getString10());
		}
	}
	
	
}