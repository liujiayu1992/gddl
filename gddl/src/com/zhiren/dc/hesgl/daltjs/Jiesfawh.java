package com.zhiren.dc.hesgl.daltjs;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 2010-08-12
 * 类名：结算方案维护
 */

public class Jiesfawh extends BasePage implements PageValidateListener {
	
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
	
	private String rowNumIndex; // 保存已选择的记录的id，并将其传到下个页面

	public String getRowNumIndex() {
		return rowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		this.rowNumIndex = rowNumIndex;
	}
	
//	结算类型下拉框_开始
	public IDropDownBean getJieslxValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getJieslxModel().getOptionCount() > 0) {
				setJieslxValue((IDropDownBean) getJieslxModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getJieslxModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getJieslxModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setJieslxModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getJieslxModels() {
		String sql = "select lb.id, lb.mingc from feiylbb lb where lb.id in (2,3) order by lb.id";
		setJieslxModel(new IDropDownModel(sql, "全部"));
	}
//	结算类型下拉框_结束
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"设置"按钮
	private boolean _ShezClick = false;
	
	public void ShezButton(IRequestCycle cycle) {
		_ShezClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_ShezClick) {
			_ShezClick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString10(getRowNumIndex()); // 将已选择的记录id(jiefab_id)传到下个页面
			cycle.activate("Jiesfagl");
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from jiesfab where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into jiesfab(id, jinmdw_id, jiesdw_id, bianm, fahqssj, fahjzsj, jieslx, shifjs) values(getnewid(")
				.append((getExtGrid().getColumn("jiesdw_id").combo).getBeanId(mdrsl.getString("jiesdw_id"))).append("), '")
				.append(mdrsl.getString("jinmdw_id")).append("', ")
				.append((getExtGrid().getColumn("jiesdw_id").combo).getBeanId(mdrsl.getString("jiesdw_id"))).append(", '")
				.append(mdrsl.getString("bianm")).append("', ")
				.append("to_date('").append(mdrsl.getString("fahqssj")).append("', 'yyyy-mm-dd')").append(", ")
				.append("to_date('").append(mdrsl.getString("fahjzsj")).append("', 'yyyy-mm-dd')").append(", ")
				.append((getExtGrid().getColumn("feiylbb_id").combo).getBeanId(mdrsl.getString("feiylbb_id"))).append(", ")
				.append("0);\n");
			} else {
				sbsql.append("update jiesfab fa set fa.jinmdw_id = '")
				.append(mdrsl.getString("jinmdw_id")).append("', ")
				.append("fa.jiesdw_id = ").append((getExtGrid().getColumn("jiesdw_id").combo).getBeanId(mdrsl.getString("jiesdw_id"))).append(", ")
				.append("fa.bianm = '").append(mdrsl.getString("bianm")).append("', ")
				.append("fa.fahqssj = to_date('").append(mdrsl.getString("fahqssj")).append("', 'yyyy-mm-dd')").append(", ")
				.append("fa.fahjzsj = to_date('").append(mdrsl.getString("fahjzsj")).append("', 'yyyy-mm-dd')").append(", ")
				.append("fa.jieslx = ").append((getExtGrid().getColumn("feiylbb_id").combo).getBeanId(mdrsl.getString("feiylbb_id")))
				.append(" where fa.id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		
		String feiylbb_id = "";
		if (!getJieslxValue().getStrId().equals("-1")) {
			feiylbb_id = "\n    and fa.jieslx = " + getJieslxValue().getStrId();
		}
		
		String sql = 
			"select fa.id,\n" +
			"       fa.bianm,\n" + 
			"       fa.fahqssj,\n" + 
			"       fa.fahjzsj,\n" + 
			"       lb.mingc as feiylbb_id,\n" + 
			"       fa.jinmdw_id,\n" + 
			"       jsdw.mingc as jiesdw_id\n" + 
			"  from jiesfab fa, feiylbb lb, diancxxb jsdw\n" + 
			" where fa.jieslx = lb.id\n" + 
			"   and fa.jiesdw_id = jsdw.id\n" + 
			"   and fa.shifjs = 0\n" + feiylbb_id +
			" order by fa.fahqssj, fa.fahjzsj";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("bianm").setHeader("结算方案编码");
		egu.getColumn("fahqssj").setHeader("发货启始时间");
		egu.getColumn("fahjzsj").setHeader("发货截止时间");
		egu.getColumn("feiylbb_id").setHeader("结算类型");
		egu.getColumn("jinmdw_id").setHeader("进煤单位");
		egu.getColumn("jiesdw_id").setHeader("结算单位");
		
		egu.getColumn("feiylbb_id").setEditor(new ComboBox());
		egu.getColumn("feiylbb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select lb.id, lb.mingc from feiylbb lb where (lb.leib < 2 or lb.leib = 3) order by lb.id"));
		
//		SelectCombo jmdw = new SelectCombo();
//		egu.getColumn("jinmdw_id").setEditor(jmdw);
//		egu.getColumn("jinmdw_id").setComboEditor(egu.gridId, 
//				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc order by dc.id"));
//		egu.getColumn("jinmdw_id").setWidth(160);
		
//		ComboBox jinmdw = new ComboBox();
//		egu.getColumn("jinmdw_id").setEditor(jinmdw);
//		egu.getColumn("jinmdw_id").setComboEditor(egu.gridId, 
//				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc order by dc.id"));
//		egu.getColumn("jinmdw_id").setWidth(160);
		
		egu.getColumn("jinmdw_id").setEditor(new LovComboBox());
		egu.getColumn("jinmdw_id").setLovComboEditor(egu.gridId, 
				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc order by dc.id"));
		egu.getColumn("jinmdw_id").setWidth(190);

		ComboBox jiesdw = new ComboBox();
		egu.getColumn("jiesdw_id").setEditor(jiesdw);
		egu.getColumn("jiesdw_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc order by dc.id"));
		egu.getColumn("jiesdw_id").setWidth(160);
		
		egu.addTbarText("结算类型：");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setTransform("Jieslx");
		comb.setId("jieslx");
		comb.setLazyRender(true);
		comb.setEditable(false);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("jieslx.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		String handler = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelected()==null){\n" + 
			"      Ext.MessageBox.alert('提示信息','请选中一行记录！');\n" + 
			"      return;\n" + 
			"    }\n" + 
			"    var rec=gridDiv_sm.getSelected();\n" +
			"    if (rec.get('ID') == 0) {\n" +
			"        Ext.MessageBox.alert('提示信息','请先保存数据！');\n" + 
			"        return;\n" + 
			"    }" +
			"    document.all.RowNumIndex.value=rec.get('ID');\n" +
			"    document.getElementById('ShezButton').click();\n" + 
			"}";
		GridButton shez = new GridButton("设置", handler, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(shez);
		
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
			visit.setProSelectionModel3(null); // 结算类型下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}

}