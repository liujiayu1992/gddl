package com.zhiren.dc.zhil;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhil extends BasePage implements PageValidateListener {
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
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl =
		con.getResultSetList(
						"select z.id,huaybh,c.bianm as caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qbrad,hdaf,qgrad_daf,sdaf,var,t1,t2,t3,t4,jijsf,jijfrl,huayy,lury,beiz,decode(shenhzt,0,'已审核','未审核') as shenhzt,banz from zhilb z,caiyb c"
						+" where z.caiyb_id=c.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("zhilb");
		egu.setWidth(1000);
		// /设置显示列名称
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("caiyb_id").setHeader("采样编号");
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("qnet_ar").setHeader("QNET_AR(Mj/kg)");
		egu.getColumn("aar").setHeader("AAR(%)");
		egu.getColumn("ad").setHeader("AD(%)");
		egu.getColumn("vdaf").setHeader("VDAF(%)");
		egu.getColumn("mt").setHeader("MT(%)");
		egu.getColumn("stad").setHeader("STAD(%)");
		egu.getColumn("aad").setHeader("AAD(%)");
		egu.getColumn("mad").setHeader("MAD(%)");
		egu.getColumn("qbad").setHeader("QBAD(Mj/kg)");
		egu.getColumn("had").setHeader("HAD(%)");
		egu.getColumn("vad").setHeader("VAD(%)");
		egu.getColumn("fcad").setHeader("FCAD(%)");
		egu.getColumn("std").setHeader("STD(%)");
		egu.getColumn("qbrad").setHeader("QBRAD(Mj/kg)");
		egu.getColumn("hdaf").setHeader("HDAF(%)");
		egu.getColumn("qgrad_daf").setHeader("QGRAD_AF(Mj/kg)");
		egu.getColumn("sdaf").setHeader("SDAF(%)");
		egu.getColumn("var").setHeader("VAR(%)");
		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("jijsf").setHeader("计划水分(%)");
		egu.getColumn("jijfrl").setHeader("计划热量(Mj/kg)");
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("shenhzt").setHeader("审核状态");
		egu.getColumn("banz").setHeader("班组");
		// //设置列宽度
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("caiyb_id").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qbrad").setWidth(80);
		egu.getColumn("var").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("jijsf").setWidth(80);
		egu.getColumn("jijfrl").setWidth(110);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		egu.getColumn("shenhzt").setWidth(80);
		egu.getColumn("banz").setWidth(80);
		// //设置当前列是否编辑
//		egu.getColumn("piny").setEditor(null);
		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		// /动态下拉框
		egu.getColumn("caiyb_id").setEditor(new ComboBox());
		egu.getColumn("caiyb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, bianm from caiyb"));
		// /静态下拉框
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "已审核"));
		l.add(new IDropDownBean(1, "未审核"));
		egu.getColumn("shenhzt").setEditor(new ComboBox());
		egu.getColumn("shenhzt").setComboEditor(egu.gridId, new IDropDownModel(l));
		// /是否返回下拉框的值或ID
		egu.getColumn("shenhzt").setReturnId(true);
		// /设置下拉框默认值
//		egu.getColumn("shenhzt").setDefaultValue(0);
		// /设置按钮
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
			visit.setList1(null);
			getSelectData();
		}
	}
}
