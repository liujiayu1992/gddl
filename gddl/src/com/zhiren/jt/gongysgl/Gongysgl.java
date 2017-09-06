package com.zhiren.jt.gongysgl;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*作者:wzb
 * 日期:2010-7-1 13:54:33
 * 描述:供货单位下拉框可编辑
 * 
 */


/*
 * 作者:tzf
 * 时间:2010-01-05
 * 修改内容:煤矿字段增加拼音索引
 */
/*
 * 作者:赵胜男
 * 时间:2013-03-22
 * 修改内容:增加模糊查询功能
 */
public class Gongysgl extends BasePage implements PageValidateListener {
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

		Save1(getChange(), visit);
		;

	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "gongysmkglb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			// StringBuffer sql3 = new StringBuffer();
			// StringBuffer sql4 = new StringBuffer();
			// StringBuffer sql5 = new StringBuffer();
			// StringBuffer sql6 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					if (mdrsl.getColumnNames()[i].equals("GONGYSB_ID")) {
						sql2.append(",").append(getGongysValue().getId());
					} else if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl
												.getString("meikxxb_id")));
					} else {
						sql2.append(",").append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i)));
					}

				}

				sql.append(") values(").append(sql2).append(");\n");

			} else {

				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {

					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					if (mdrsl.getColumnNames()[i].equals("GONGYSB_ID")) {

						sql.append(getGongysValue().getId()).append(",");
					} else if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")) {

						sql.append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl
												.getString("meikxxb_id")))
								.append(",");
					} else {
						sql.append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;

		}
		if (_DeleteChick) {
			_DeleteChick = false;

		}

	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		sql = "select gm.id, g.mingc gongysb_id,nvl(m.piny||'---'||m.mingc,'') meikxxb_id from gongysb g,meikxxb m,gongysmkglb gm where gm.gongysb_id="
				+ getGongysValue().getId()
				+ "\n"
				+ "and gm.gongysb_id=g.id and m.id=gm.meikxxb_id";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("gongysmkglb");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("gongysb_id").setWidth(200);
		egu.getColumn("meikxxb_id").setWidth(400);

		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id")
				.setDefaultValue(getGongysValue().getValue());
		egu.getColumn("gongysb_id").setReturnId(true);

		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu
				.getColumn("meikxxb_id")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(
								"select id,nvl(piny||'---'||mingc,'') mingc from meikxxb where id not in(select meikxxb_id from gongysmkglb  where gongysb_id="
										+ getGongysValue().getStrId() + ")"));
		egu.getColumn("meikxxb_id").setReturnId(true);
		((ComboBox)(egu.getColumn("meikxxb_id").editor)).setEditable(true);
		((ComboBox)(egu.getColumn("meikxxb_id").editor)).setIsMohcx(true);
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		//egu.setWidth(1000);
		egu.setWidth("bodyWidth");

		// 供货单位
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(true);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(300);
		comb4.setIsMohcx(true);
		//comb4.setReadOnly(true);
		
		egu.addToolbarItem(comb4.getScript());
		egu
				.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");

		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Insert_condition, "",
				"if(Gongys.getRawValue()=='请选择'){alert('请选择供应商'); return;}");

		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
		egu.addToolbarButton(GridButton.ButtonType_Save, "savebutton");
		setExtGrid(egu);

		con.Close();
	}

	// 供货单位

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {

		String sql = "select gys.id,gys.mingc from gongysdcglb glb,diancxxb dc,gongysb gys\n"
				+ "where glb.diancxxb_id=dc.id and glb.gongysb_id=gys.id and gys.leix = 1 and dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " order by gys.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	// 煤矿单位
	public IDropDownBean getMeikdwValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getMeikdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public IPropertySelectionModel getMeikdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getMeikdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getMeikdwModels() {

		String sql = "select id,mingc from meikxxb where id not in(select meikxxb_id from gongysmkglb  where gongysb_id="
				+ getGongysValue().getStrId() + ")";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
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

			getGongysValue();
			setGongysValue(null);
			getGongysModels();
			getMeikdwValue();
			setMeikdwValue(null);
			getSelectData();
		}
		getSelectData();
	}

}