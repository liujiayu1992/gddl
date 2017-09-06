package com.zhiren.shihs.huaygl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihhylr extends BasePage implements PageValidateListener {
	
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
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _ZuofChick = false;
	
	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_ZuofChick) {
			_ZuofChick = false;
			Zuof();
		}
	}
	
	private void Zuof() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		String strSQL = "";
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			strSQL = "UPDATE SHIHCYB SET SHIHZLB_ID = -1 WHERE ID=" + rsl.getString("id");
			int flag = con.getUpdate(strSQL);
			if (flag == -1) {
				setMsg("作废失败!");
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
		Visit visit = (Visit)this.getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		String strSQL = "";
		int flag = 0;
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			String newID = MainGlobal.getNewID(diancxxb_id);
			strSQL = 
				"INSERT INTO shihzlb (ID,BIANM,HUAYY,HUAYSJ,CAO,CACO3,XID,CAOH2,MGO,MGCO3,SIO2,LURRY,HEGBZ,LURSJ,BEIZ) \n" +
				"VALUES (\n" +
				"" + newID + ",\n" + 
				"'" + rsl.getString("bianm") + "',\n" + 
				"'" + rsl.getString("huayy") + "',\n" + 
				"to_date('" + rsl.getString("huaysj") + "','yyyy-mm-dd'),\n" +
				"" + rsl.getString("CaO") + ",\n" +
				"" + rsl.getString("CaCO3") + ",\n" +
				"" + rsl.getString("xid") + ",\n" +
				"" + rsl.getString("CaOH2") + ",\n" +
				"" + rsl.getString("MgO") + ",\n" +
				"" + rsl.getString("MgCO3") + ",\n" +
				"" + rsl.getString("SIO2") + ",\n" +
				"'" + visit.getRenymc() + "',\n" + 
				"'" + rsl.getString("hegbz")+ "',\n" + 
				"SYSDATE,\n" +
				"'" + rsl.getString("beiz") + "'\n" + 
				")";

			flag = con.getInsert(strSQL);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.InsertDatabaseFail + "\nSQL:" + strSQL);
				setMsg("保存失败!");
				con.rollBack();
				con.Close();
				return;
			}
			//更新shihcyb.shihzlb_id
			strSQL = "UPDATE SHIHCYB SET SHIHCYB.SHIHZLB_ID=" + newID +
				" WHERE ID = " + rsl.getString("id");
			flag = con.getUpdate(strSQL);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + strSQL);
				setMsg("保存失败!");
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
			"       GETSHIHHYCPH(ID) AS CHEH,\n" +
			"       TO_DATE(TO_CHAR(SYSDATE, 'yyyy-mm-dd'), 'yyyy-mm-dd') AS HUAYSJ,\n" + 
			"       0 AS CAO,\n" + 
			"       0 AS CACO3,\n" + 
			"       0 AS XID,\n" + 
			"       0 AS MGO,\n" + 
			"       0 AS CAOH2,\n" + 
			"       0 AS MGCO3,\n" + 
			"       0 AS SIO2,\n" + 
			"       NVL('" + visit.getRenymc() + "','') AS HUAYY,\n" + 
			"       '' AS HUAYLRY,\n" + 
			"       nvl('合格','') as HEGBZ,\n" +
			"       '' AS BEIZ\n" + 
			"  FROM SHIHCYB\n" + 
			" WHERE SHIHZLB_ID = 0\n" +
			"   AND GETSHIHHYCPH(ID) IS NOT NULL";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		// //设置表名称用于保存
		egu.setTableName("shihzlb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth("bodyWidth");
		// /设置显示列名称
		egu.getColumn("bianm").setHeader("化验编号");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("cheh").setWidth(80);
		egu.getColumn("cheh").setEditor(null);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("CaO").setHeader("CaO(%)");
		egu.getColumn("CaO").setWidth(70);
		egu.getColumn("CaCO3").setHeader("CaCO3(%)");
		egu.getColumn("CaCO3").setWidth(70);
		egu.getColumn("XID").setHeader("细度(%)");
		egu.getColumn("XID").setWidth(70);
		egu.getColumn("CaOH2").setHeader("CaOH2(%)");
		egu.getColumn("CaOH2").setWidth(70);
		egu.getColumn("MgO").setHeader("MgO(%)");
		egu.getColumn("MgO").setWidth(70);
		egu.getColumn("MgCO3").setHeader("MgCO3(%)");
		egu.getColumn("MgCO3").setWidth(70);
		egu.getColumn("SIO2").setHeader("SiO2(%)");
		egu.getColumn("SIO2").setWidth(70);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setWidth(60);
		egu.getColumn("huaylry").setHeader("录入员");
		egu.getColumn("huaylry").setEditor(null);
		egu.getColumn("huaylry").setHidden(true);
		egu.getColumn("hegbz").setHeader("是否合格");
		egu.getColumn("hegbz").setWidth(60);
		egu.getColumn("beiz").setHeader("备注");
		
		((NumberField)egu.getColumn("CaO").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("CaCO3").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("XID").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("CaOH2").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("MgO").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("MgCO3").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("SIO2").editor).setDecimalPrecision(2);
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("hegbz").setEditor(c1);
		c1.setEditable(true);
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "合格"));
		list.add(new IDropDownBean(2, "不合格"));
		egu.getColumn("hegbz").setComboEditor(egu.gridId,
				new IDropDownModel(list));
		
		GridButton gbf = new GridButton("刷新", 
				"function(){document.getElementById('RefreshButton').click();}");		
		gbf.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbf);
		
		egu.addToolbarButton("作废", GridButton.ButtonType_SubmitSel,
				"ZuofButton", null, null);
		
		egu.addToolbarButton("提交", GridButton.ButtonType_SubmitSel,
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
