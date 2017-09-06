package com.zhiren.dc.huaygl.huaybl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huaybl extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

//	public double getDouble(String colNum) {
//		String value = getString(colNum);
//		double doublev = 0.0;
//		if (value == null || value.equals("")) {
//			value = "0.0";
//		}
//		try {
//			doublev = Double.parseDouble(value);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return doublev;
//	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		while (rsl.next()) {
			sb
					.append("insert into zhilb\n")
					.append(
							"(id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,std,qgrad,hdaf,qgrad_daf,sdaf,var,huayy,lury,shenhzt)\n")
					.append("values (" + rsl.getLong("id")).append(",'")
					.append(rsl.getString("huaybh")).append("',").append(
							rsl.getLong("caiyb_id")).append(",").append(
							DateUtil.FormatOracleDate(rsl.getString("huaysj")))
					.append(",").append(rsl.getDouble("qnet_ar"))
					.append(",").append(rsl.getDouble("aar"))
					.append(",").append(rsl.getDouble("ad")).append(
							",").append(rsl.getDouble("vdaf"))
					.append(",").append(rsl.getDouble("mt")).append(
							",").append(rsl.getDouble("stad"))
					.append(",").append(rsl.getDouble("aad"))
					.append(",").append(rsl.getDouble("mad"))
					.append(",").append(rsl.getDouble("qbad"))
					.append(",").append(rsl.getDouble("had"))
					.append(",").append(rsl.getDouble("vad"))
					.append(",").append(rsl.getDouble("std"))
					.append(",").append(rsl.getDouble("qgrad"))
					.append(",").append(rsl.getDouble("hdaf"))
					.append(",").append(rsl.getDouble("qgrad_daf"))
					.append(",").append(rsl.getDouble("sdaf"))
					.append(",").append(rsl.getDouble("var"))
					.append(",'").append(rsl.getString("huayy")).append("','")
					.append(visit.getRenymc()).append("',").append("3").append(
							");\n");
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("SELECT   c.zhilb_id AS ID, c.bianm AS bianm, z.huaybh AS huaybh,z.huayy as huayy,\n"
						+ "         c.ID AS caiyb_id, decode(z.huaysj,null,sysdate,z.huaysj) AS huaysj, z.aar AS aar, z.ad AS ad,\n"
						+ "         z.aad AS aad, z.mt AS mt, z.mad AS mad, z.std AS std, z.stad AS stad,\n"
						+ "         z.sdaf AS sdaf, z.qbad AS qbad, z.had AS had, z.hdaf AS hdaf,\n"
						+ "         z.vad AS vad, z.vdaf AS vdaf, z.fcad AS fcad, z.qgrad AS qgrad,\n"
						+ "         z.qgrad_daf AS qgrad_daf, z.qnet_ar AS qnet_ar, z.var AS var, z.shenhzt as shenhzt\n"
						+ "    FROM zhilb z, caiyb c, fahb f\n"
						+ "   WHERE c.zhilb_id = z.ID(+)\n"
						+ "     AND f.zhilb_id = c.zhilb_id\n"
						+ "     AND z.shenhzt is null\n"
						+ "     AND c.caiyrq BETWEEN TO_DATE ('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')\n"
						+ "                      AND TO_DATE ('"
						+ getRiq2()
						+ "', 'yyyy-mm-dd')\n"
						+ "     AND f.diancxxb_id = "
						+ visit.getDiancxxb_id() + "\n" + "ORDER BY c.bianm");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zhilb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("bianm").setHeader("采样编号");
		egu.getColumn("bianm").setUpdate(true);
		egu.getColumn("bianm").setWidth(60);
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("caiyb_id").setEditor(null);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("huaysj").setWidth(70);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setWidth(90);
		// egu.getColumn("huaysj").setDataType(datatype)
		egu.getColumn("aar").setHeader("aar(%)");
		egu.getColumn("ad").setHeader("ad(%)");
		egu.getColumn("aad").setHeader("aad(%)");
		egu.getColumn("mt").setHeader("mt(%)");
		egu.getColumn("mad").setHeader("mad(%)");
		egu.getColumn("std").setHeader("std(%)");
		egu.getColumn("stad").setHeader("stad(%)");
		egu.getColumn("sdaf").setHeader("sdaf(%)");
		egu.getColumn("qbad").setHeader("qbad(MJ/kg)");
		egu.getColumn("had").setHeader("had(%)");
		egu.getColumn("hdaf").setHeader("hdaf(%)");
		egu.getColumn("vad").setHeader("vad(%)");
		egu.getColumn("vdaf").setHeader("vdaf(%)");
		egu.getColumn("fcad").setHeader("fcad(%)");
		egu.getColumn("qgrad").setHeader("qgrad(MJ/kg)");
		egu.getColumn("qgrad_daf").setHeader("qgrad_daf(MJ/kg)");
		egu.getColumn("qnet_ar").setHeader("qnet_ar(MJ/kg)");
		egu.getColumn("var").setHeader("var(%)");

		egu.getColumn("aar").setWidth(70);
		egu.getColumn("ad").setWidth(70);
		egu.getColumn("aad").setWidth(70);
		egu.getColumn("mt").setWidth(70);
		egu.getColumn("mad").setWidth(70);
		egu.getColumn("std").setWidth(70);
		egu.getColumn("stad").setWidth(70);
		egu.getColumn("sdaf").setWidth(70);
		egu.getColumn("qbad").setWidth(70);
		egu.getColumn("had").setWidth(70);
		egu.getColumn("hdaf").setWidth(70);
		egu.getColumn("vad").setWidth(70);
		egu.getColumn("vdaf").setWidth(70);
		egu.getColumn("fcad").setWidth(70);
		egu.getColumn("qgrad").setWidth(70);
		egu.getColumn("qgrad_daf").setWidth(70);
		egu.getColumn("qnet_ar").setWidth(70);
		egu.getColumn("var").setWidth(70);
		
		
		egu.getColumn("aar").setDefaultValue("0");
		egu.getColumn("ad").setDefaultValue("0");
		egu.getColumn("aad").setDefaultValue("0");
		egu.getColumn("mt").setDefaultValue("0");
		egu.getColumn("mad").setDefaultValue("0");
		egu.getColumn("std").setDefaultValue("0");
		egu.getColumn("stad").setDefaultValue("0");
		egu.getColumn("sdaf").setDefaultValue("0");
		egu.getColumn("qbad").setDefaultValue("0");
		egu.getColumn("had").setDefaultValue("0");
		egu.getColumn("hdaf").setDefaultValue("0");
		egu.getColumn("vad").setDefaultValue("0");
		egu.getColumn("vdaf").setDefaultValue("0");
		egu.getColumn("fcad").setDefaultValue("0");
		egu.getColumn("qgrad").setDefaultValue("0");
		egu.getColumn("qgrad_daf").setDefaultValue("0");
		egu.getColumn("qnet_ar").setDefaultValue("0");
		egu.getColumn("var").setDefaultValue("0");
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setEditor(null);
		egu.getColumn("shenhzt").setDefaultValue("3");
		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");// 设置分隔符
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		egu.setWidth(990);
		egu
				.addToolbarItem("{"
						+ new GridButton("刷新",
								"function(){document.getElementById('RefurbishButton').click();}")
								.getScript() + "}");
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
			visit.setList1(null);
			getSelectData();
		}
	}
}
