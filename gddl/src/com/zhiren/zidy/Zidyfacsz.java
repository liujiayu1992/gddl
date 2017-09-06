//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.zidy;


import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zidyfacsz extends BasePage {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean blnIsBegin = false;
	

	 private void Save() {
	 Visit visit = (Visit) this.getPage().getVisit();
	 visit.getExtGrid1().Save(getChange(), visit);
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
	}
	
	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String lngjiesbID = visit.getString1();
		ResultSetList rsl = con
				.getResultSetList("select z.id,d.z_alias_cn as zidycszd_id,z_operators,z_value,z_group from zidyfacsz z,zidycszd d where z.zidycszd_id = d.id and d.zidyfa_id =  "+lngjiesbID);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidyfacsz");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		
		
		
		egu.getColumn("zidycszd_id").setHeader("自定义名称");
		ComboBox luj = new ComboBox();
		luj.setEditable(true);
		egu.getColumn("zidycszd_id").setEditor(luj);
		egu.getColumn("zidycszd_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,z_alias_cn  from zidycszd where zidyfa_id =  "+lngjiesbID));
		//egu.getColumn("zidycszd_id").setDefaultValue(rsl.getString(colNum));
		egu.getColumn("z_operators").setHeader("运算符");
		ComboBox combSex= new ComboBox();
		egu.getColumn("z_operators").setEditor(combSex);
		combSex.setEditable(true);
		List Fuh = new ArrayList();
		Fuh.add(new IDropDownBean(0, ">"));
		Fuh.add(new IDropDownBean(1, "<"));
		Fuh.add(new IDropDownBean(2, "="));
		Fuh.add(new IDropDownBean(3, ">="));
		Fuh.add(new IDropDownBean(4, "<="));
		Fuh.add(new IDropDownBean(5, "in"));
		Fuh.add(new IDropDownBean(6, "like"));
		egu.getColumn("z_operators").setComboEditor(egu.gridId, new IDropDownModel(Fuh));
		egu.getColumn("z_operators").returnId = false;
		
		
		egu.getColumn("z_value").setHeader("值");
		egu.getColumn("z_group").setHeader("组");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// List l = new ArrayList();
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
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
//		if (cycle.getRequestContext().getParameter("zidid") != null&&!cycle.getRequestContext().getParameter("zidid").equals("-1")) {
//			visit.setString1(cycle.getRequestContext().getParameter("zidid"));
//		}else{
//			blnIsBegin = false;
//			return;
//		}
//		getToolbars();
//		blnIsBegin = true;
		getSelectData();
	}
   
}
