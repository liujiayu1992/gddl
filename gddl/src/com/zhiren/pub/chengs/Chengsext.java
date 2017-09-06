package com.zhiren.pub.chengs;

import java.sql.SQLException;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 2010-5-21
 * 
 * @author 李琛基
 * 城市维护页面
 * 
 */
public class Chengsext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

	private void Save() throws SQLException {
		String bianm = "";
		int xuh = 0;
		int shengfbID = 0;
		boolean isExist = false;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		// 对于 传递过来的 数据 里面 有&的要排除 ，否则出错
		System.out.println(getChange());
		if (this.getChange() != null) {
			this.Change = this.getChange().replaceAll("(&lt;)+(.)*(&gt;)+", "");
		}
		System.out.println(Change);

		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1()
				.getDeleteResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while (drsl.next()) {
			sql.append("delete from ").append("chengsb").append(" where id =")
					.append(drsl.getString(0)).append(";\n");
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());

		while (rsl.next()) {
			shengfbID = Integer.parseInt(visit.getExtGrid1().getValueSql(
					visit.getExtGrid1().getColumn("shengfb_id"),
					rsl.getString("shengfb_id")));
			ResultSetList rs = con
					.getResultSetList("select quanc from chengsb where shengfb_id="
							+ shengfbID);
			while (rs.next()) {
				if (("'" + rs.getString("quanc") + "'").equals(visit
						.getExtGrid1().getValueSql(
								visit.getExtGrid1().getColumn("quanc"),
								rsl.getString("quanc")))) {
					isExist = true;
				}
			}
			ResultSetList rs2 = con
					.getResultSetList("select count(id) as num from chengsb where shengfb_id="
							+ shengfbID);
			while (rs2.next()) {
				bianm = rs2.getInt("num") < 9 ? "0" + (rs2.getInt("num") + 1)
						: String.valueOf((rs2.getInt("num") + 1));
			}
			ResultSetList rs3 = con
					.getResultSetList("select max(xuh) as xuh from chengsb");

			while (rs3.next()) {
				xuh = rs3.getInt("xuh") + 1;
			}
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(rsl.getString("ID"))) {
				sql.append("insert into ").append("chengsb").append("(id");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					// if (!rsl.getColumnNames()[i].equals("MEICT")){
					sql.append(",").append(rsl.getColumnNames()[i]);
					if (rsl.getColumnNames()[i].equals("XUH")) {
						sql2.append(",").append(xuh);
					} else if (rsl.getColumnNames()[i].equals("BIANM")) {
						sql2.append(",").append("'" + bianm + "'");
					} else if (rsl.getColumnNames()[i].equals("MINGC")) {
						sql2.append(",").append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i + 1]),
										rsl.getString(i + 1)));
					} else {
						sql2.append(",").append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i)));
					}

					// }
				}
				sql.append(") values(").append(sql2).append(");\n");
				if (isExist) {
					setMsg("城市已经存在!");
					con.Close();
				}
			} else {
				sql.append("update ").append("chengsb").append(" set ");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					sql.append(rsl.getColumnNames()[i]).append(" = ");
					if (rsl.getColumnNames()[i].equals("MINGC")) {
						sql.append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i + 1]),
										rsl.getString(i + 1))).append(",");
					} else {
						sql.append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i))).append(",");
					}
					// sql.append(
					// visit.getExtGrid1().getValueSql(
					// visit.getExtGrid1().getColumn(
					// rsl.getColumnNames()[i]),
					// rsl.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(rsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			setMsg("保存失败！");
		} else {
			setMsg("保存成功！");
			con.commit();
		}
		con.Close();
		// Visit visit = (Visit) this.getPage().getVisit();
		// visit.getExtGrid1().Save(getChange(), visit);
	}

	// 省份下拉框
	public IDropDownBean getShengfDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getShengfDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setShengfDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setShengfDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getShengfDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getShengfDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getShengfDropDownModels() {
		String sql = "select id,quanc\n" + "from shengfb \n";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "全部"));
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			try {
				Save();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			getSelectData();
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql
				.append("select cs.id,sf.quanc as shengfb_id,cs.xuh as xuh,cs.bianm as bianm,cs.mingc as mingc,cs.quanc as quanc,cs.piny as piny,cs.beiz as beiz from chengsb cs,shengfb sf where cs.shengfb_id=sf.id(+)");
		if (getShengfDropDownValue().getId() != -1) {
			sql.append(" and sf.id=" + getShengfDropDownValue().getId());
		}
		sql.append(" order by sf.quanc,cs.xuh");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chengsb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("xuh").setHidden(true);
		egu.getColumn("mingc").setHeader("简称");
		egu.getColumn("mingc").setHidden(true);
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("shengfb_id").setHeader("省份地区");
		egu.getColumn("shengfb_id").setEditor(new ComboBox());
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		// egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("省份 ");
		ComboBox cbo = new ComboBox();
		cbo.setLazyRender(true);
		cbo.setTransform("ShengfDropDown");
		cbo.setId("ShengfDropDown");
		cbo.setWidth(120);
		egu.addToolbarItem(cbo.getScript());
		egu
				.addOtherScript("ShengfDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		/*
		 * String str = " var url =
		 * 'http://'+document.location.host+document.location.pathname;" + "var
		 * end = url.indexOf(';');" + "url = url.substring(0,end);" + "url = url +
		 * '?service=page/' + 'Shengfreport&lx=rezc';" + "
		 * window.open(url,'newWin');"; egu.addToolbarItem("{" + new
		 * GridButton("打印", "function (){" + str + "}",
		 * SysConstant.Btn_Icon_Print).getScript() + "}");
		 */
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
			this.setShengfDropDownModel(null);
			this.setShengfDropDownValue(null);
		}
		getSelectData();
	}
}
