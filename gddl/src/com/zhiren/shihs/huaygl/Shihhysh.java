package com.zhiren.shihs.huaygl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihhysh extends BasePage implements PageValidateListener {
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _HuitChick = false;
	
	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_HuitChick) {
			Huit();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}
	
	public void Huit() {
		JDBCcon con = new JDBCcon();
		String strSQL = "";
		int flag = 0;
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			strSQL = "UPDATE shihcyb SET shihzlb_id=0  WHERE shihzlb_id= " + rsl.getString("id");
			flag = con.getUpdate(strSQL);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + strSQL);
				setMsg("回退失败!");
				con.rollBack();
				con.Close();
				return;
			}
			strSQL = "DELETE FROM shihzlb WHERE ID = " + rsl.getString("id");
			flag = con.getDelete(strSQL);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + strSQL);
				setMsg("回退失败!");
				con.rollBack();
				con.Close();
				return;
			}
		}
		rsl.close();
		con.commit();
		con.Close();
	}
	
	private void Save() {
		JDBCcon con = new JDBCcon();
		String strSQL = "";
		int flag = 0;
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			strSQL = "UPDATE shihzlb SET shenhzt=1 WHERE ID=" + rsl.getString("id");

			flag = con.getUpdate(strSQL);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + strSQL);
				setMsg("审核失败!");
				con.rollBack();
				con.Close();
				return;
			}
		}
		rsl.close();
		con.commit();
		con.Close();
	}
	
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();	
		String sql = 
			"SELECT ID,\n" +
			"       BIANM,\n" + 
			"       GETSHIHHYCPH((SELECT ID FROM SHIHCYB WHERE SHIHZLB_ID = SHIHZLB.ID)) AS CHEH,\n" + 
			"       HUAYSJ,\n" + 
			"       CAO,\n" + 
			"       CACO3,\n" + 
			"       XID,\n" + 
			"       MGO,\n" + 
			"       CAOH2,\n" + 
			"       MGCO3,\n" + 
			"       SIO2,\n" + 
			"       HUAYY,\n" + 
			"       HEGBZ,\n" +
			"       BEIZ,\n" + 
			"       LURRY\n" + 
			"  FROM SHIHZLB\n" + 
			" WHERE SHENHZT = 0\n" +
			" ORDER BY huaysj";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		// //设置表名称用于保存
		egu.setTableName("shihzlb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth("bodyWidth");
		// /设置显示列名称
		egu.getColumn("id").setHeader("化验编号");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("bianm").setHeader("化验编号");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("cheh").setWidth(80);
		egu.getColumn("cheh").setEditor(null);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("CaO").setHeader("CaO(%)");
		egu.getColumn("CaO").setWidth(70);
		egu.getColumn("CaO").setEditor(null);
		egu.getColumn("CaCO3").setHeader("CaCO3(%)");
		egu.getColumn("CaCO3").setWidth(70);
		egu.getColumn("CaCO3").setEditor(null);
		egu.getColumn("XID").setHeader("细度(%)");
		egu.getColumn("XID").setWidth(70);
		egu.getColumn("XID").setEditor(null);
		egu.getColumn("CaOH2").setHeader("CaOH2(%)");
		egu.getColumn("CaOH2").setWidth(80);
		egu.getColumn("CaOH2").setEditor(null);
		egu.getColumn("MgO").setHeader("MgO(%)");
		egu.getColumn("MgO").setWidth(70);
		egu.getColumn("MgO").setEditor(null);
		egu.getColumn("MgCO3").setHeader("MgCO3(%)");
		egu.getColumn("MgCO3").setWidth(70);
		egu.getColumn("MgCO3").setEditor(null);
		egu.getColumn("SIO2").setHeader("SiO2(%)");
		egu.getColumn("SIO2").setWidth(70);
		egu.getColumn("SIO2").setEditor(null);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("hegbz").setWidth(60);
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lurry").setHeader("录入员");
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setHidden(true);
		egu.getColumn("hegbz").setHeader("是否合格");
		egu.getColumn("hegbz").setEditor(null);
		egu.getColumn("hegbz").setWidth(60);
		egu.getColumn("beiz").setHeader("备注");
		
//		刷新按钮
		GridButton gbf = new GridButton("刷新", 
				"function(){document.getElementById('RefreshButton').click();}");		
		gbf.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbf);
		
		egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,
				"HuitButton", null, null);
		
		egu.addToolbarButton("审核", GridButton.ButtonType_SubmitSel,
				"SaveButton", null, null);
			
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
	    egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		setExtGrid(egu);
		con.Close();
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
			getSelectData();
		}
		getSelectData();
	}
	
}
