package com.zhiren.shanxdted.hetgl.mkydhtgl;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Mkydht extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	
//	public String getBriq() {
//		return ((Visit)getPage().getVisit()).getString5();
//	}
//	
//	public void setBriq(String value) {
//		((Visit)getPage().getVisit()).setString5(value);
//	}
//	
//	public String getEriq() {
//		return ((Visit)getPage().getVisit()).getString6();
//	}
//	
//	public void setEriq(String value) {
//		((Visit)getPage().getVisit()).setString6(value);
//	}
	
	private void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String SQL = 
			"SELECT GL.ID,\n" +
			"       MK.MINGC AS MKDW,\n" + 
			"       YD.MINGC AS YSDW,\n" + 
			"       HT.HETBH AS HTBH,\n" + 
			"       HT.QISRQ,\n" + 
			"       HT.GUOQRQ\n" + 
			"  FROM MKYDHTGLB GL, MEIKXXB MK, YUNSDWB YD, HETB HT\n" + 
			"\n" + 
			" WHERE GL.MEIKXXB_ID = MK.ID\n" + 
			"   AND GL.YUNSDWB_ID = YD.ID\n" + 
			"   AND GL.HETB_ID = HT.ID\n" + 
			"   AND HT.ID = " + visit.getString1();
		
		ResultSetList rsl = con.getResultSetList(SQL);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("MKYDHTGLB");
//		egu.setWidth("bodyWidth - 230");
		egu.setHeight("bodyHeight - 10");
		egu.addPaging(0);
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mkdw").setHeader("煤矿单位");
		egu.getColumn("mkdw").setWidth(150);
		egu.getColumn("ysdw").setHeader("运输单位");
		egu.getColumn("ysdw").setWidth(150);
		egu.getColumn("htbh").setHeader("合同编号");
		egu.getColumn("htbh").setWidth(150);
		egu.getColumn("qisrq").setHeader("启用日期");
		egu.getColumn("qisrq").setEditor(null);
		egu.getColumn("qisrq").setWidth(120);
		egu.getColumn("guoqrq").setHeader("终止日期");
		egu.getColumn("guoqrq").setEditor(null);
		egu.getColumn("guoqrq").setWidth(120);
		
		String strDc ="";
		if ("".equals(visit.getString3()) || visit.getString3()==null) {
			if (visit.isFencb()) {
				strDc = " AND DIANCXXB_ID IN (SELECT ID FROM DIANCXXB WHERE FUID = " + visit.getDiancxxb_id() + ")";
			} else {
				strDc = " AND DIANCXXB_ID = " + visit.getDiancxxb_id();
			}

		} else {
			strDc = " AND DIANCXXB_ID = " + visit.getString3();
		}
		
		//煤矿下拉框
		egu.getColumn("mkdw").setEditor(new ComboBox());
		String strMk = 
			"SELECT MEIKXXB.ID, MEIKXXB.MINGC\n" +
			"  FROM MEIKXXB,\n" + 
			"       GONGYSB,\n" + 
			"       GONGYSMKGLB,\n" + 
			"       (SELECT DISTINCT GONGYSB_ID\n" + 
			"          FROM HETB\n" + 
			"         WHERE ID= " + visit.getString1()+ " \n" + 
			strDc + ") A\n" + 
			" WHERE MEIKXXB.ID = GONGYSMKGLB.MEIKXXB_ID\n" + 
			"   AND GONGYSB.ID = GONGYSMKGLB.GONGYSB_ID\n" + 
			"   AND GONGYSB.ID = A.GONGYSB_ID ORDER BY MEIKXXB_ID";

		egu.getColumn("mkdw").setComboEditor(egu.gridId, new IDropDownModel(strMk));
		
		//运输单位下拉框
		egu.getColumn("ysdw").setEditor(new ComboBox());
		String strYs = "SELECT ID,MINGC FROM YUNSDWB ORDER BY MINGC";
		egu.getColumn("ysdw").setComboEditor(egu.gridId, new IDropDownModel(strYs));
		
		//合同编号下拉框
		egu.getColumn("htbh").setEditor(new ComboBox());
//		String strHt = 
//			"SELECT ID, HETBH\n" +
//			"  FROM HETB\n" + 
//			" WHERE QISRQ BETWEEN TO_DATE('" + this.getBriq() + "', 'yyyy-mm-dd') AND\n" + 
//			"       TO_DATE('" + this.getEriq() + "', 'yyyy-mm-dd')\n" + 
//			strDc + "\n" +
//			" ORDER BY HETBH";
//		egu.getColumn("htbh").setComboEditor(egu.gridId, new IDropDownModel(strHt));
		
		String strHt = "SELECT ID, HETBH FROM HETB WHERE ID =" + visit.getString1();
		egu.getColumn("htbh").setComboEditor(egu.gridId, new IDropDownModel(strHt));
		
		strHt = "SELECT ID, HETBH, QISRQ, GUOQRQ FROM HETB WHERE ID =" + visit.getString1();
		ResultSetList rsHt = con.getResultSetList(strHt);
		if (rsHt.next()) {
			egu.getColumn("htbh").setDefaultValue(rsHt.getString("hetbh"));
			egu.getColumn("qisrq").setDefaultValue(DateUtil.FormatDate(rsHt.getDate("qisrq")));
			egu.getColumn("guoqrq").setDefaultValue(DateUtil.FormatDate(rsHt.getDate("guoqrq")));
		}
		
		
		
//		egu.addTbarText("合同日期:");
//		DateField dStart = new DateField();
//		dStart.Binding("Briq","");
//		dStart.setValue(getBriq());
//		egu.addToolbarItem(dStart.getScript());
//		egu.addTbarText("至");
//		DateField dEnd = new DateField();
//		dEnd.Binding("Eriq","");
//		dEnd.setValue(getEriq());
//		egu.addToolbarItem(dEnd.getScript());
//		egu.addTbarText("-");
//		
		//刷新按钮
		GridButton refurbish = new GridButton("刷新",
		"function (){document.getElementById('RefreshButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		//添加按钮
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		//删除按钮
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		//保存按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		setExtGrid(egu);		
		rsl.close();
		con.Close();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
	private void Save() {
		long id = 0;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		con.setAutoCommit(false);
		rsl = getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()) {
			id = rsl.getLong("id");
			
			//进行删除操作时添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,"合同关联",
					"mkydhtglb",id+"");
			int flag = con.getDelete("delete from mkydhtglb where id=" + id);
			if (flag == -1) {
				con.rollBack();
				con.Close();
			}
		}
		
		rsl = null;
		rsl = getExtGrid().getModifyResultSet(getChange());
		long lngMkID = 0;
		long lngYsID = 0;
		long lngHtID = 0;
		String sDiancxxbID = visit.getString3();
		String nianf = "";
		String yuef = "";
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				lngMkID = this.getExtGrid().getColumn("mkdw").combo.getBeanId(rsl.getString("mkdw"));
				lngYsID = this.getExtGrid().getColumn("ysdw").combo.getBeanId(rsl.getString("ysdw"));
				lngHtID = this.getExtGrid().getColumn("htbh").combo.getBeanId(rsl.getString("htbh"));
				ResultSetList rs = con.getResultSetList("SELECT QISRQ FROM HETB WHERE ID=" + lngHtID);
				if (rs.next()) {
					nianf = DateUtil.Formatdate("yyyy", rs.getDate("QISRQ"));
					yuef = DateUtil.Formatdate("MM", rs.getDate("QISRQ"));
				} 
				
				String insetId = MainGlobal.getNewID(Long.parseLong(sDiancxxbID));
				String sInsert = 
					"INSERT INTO MKYDHTGLB\n" +
					"  (ID, MEIKXXB_ID, YUNSDWB_ID, HETB_ID, DIANCXXB_ID)\n" + 
					"VALUES\n" + 
					"  (" + insetId +",\n" +
					lngMkID + ",\n" +
					lngYsID + ",\n" +
					lngHtID + ",\n" +
					sDiancxxbID + 
					")";

				int flag = con.getInsert(sInsert);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
//				visit.setString10("NavTree_root" + nianf + nianf + yuef + insetId);
//				visit.setString10(insetId);
			}
		}
		rsl.close();
		con.commit();
		con.Close();
	}
	
	//	页面登陆验证
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
		
		if(cycle.getRequestContext().getParameter("lx") != null){
			visit.setString1(cycle.getRequestContext().getParameter("lx"));
		}
		
//		if (cycle.getRequestContext().getParameter("riq") != null) {
//			try {
//				String sRiq = new String(cycle.getRequestContext()
//						.getParameter("riq").getBytes("iso8859-1"), "gb2312");
//				int pos = sRiq.indexOf("至");
//				this.setBriq(sRiq.substring(0, pos));
//				this.setEriq(sRiq.substring(pos+1));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		if (!visit.getActivePageName().toString().equals(getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
//			visit.setActivePageName(getPageName().toString());

			if (visit.getString1() == null || "".equals(visit.getString1())) {
				visit.setString1("-1");
			}
		}
		
		getSelectData();
	}

}
