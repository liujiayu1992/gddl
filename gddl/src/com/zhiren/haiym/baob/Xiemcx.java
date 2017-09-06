package com.zhiren.haiym.baob;

import java.util.ArrayList;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 尹佳明 2009-09-11 类名：Xiemcx(卸煤查询)
 */

public class Xiemcx extends BasePage {

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	protected void initialize() {
		this._msg = "";
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public boolean getRaw() {
		return true;
	}

	// 起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// 结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	// 类别下拉框_开始
	public IDropDownBean getLeibValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getLeibModel().getOptionCount() > 0) {
				setLeibValue((IDropDownBean) getLeibModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setLeibValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(LeibValue);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setLeibModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setLeibModels() {
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(1, "班组"));
		list.add(new IDropDownBean(2, "日期"));
		setLeibModel(new IDropDownModel(list));
	}
	// 类别下拉框_结束

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	public String getPrintTable() {

		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader = new String[1][4];
		int[] ArrWidth = new int[4];
		String sql = "";

		if (getLeibValue().getValue().equals("班组")) {
			sql = "select decode(grouping(item.mingc)+grouping(lcxm.riq)+grouping(lcxx.mingc)+grouping(lcxm.xieml), 4, '合计', item.mingc)  banz,\n"
				+ "    decode(grouping(item.mingc)+grouping(lcxm.riq)+grouping(lcxx.mingc)+grouping(lcxm.xieml), 3, '小计', to_char(lcxm.riq, 'yyyy-MM-dd')) riq,\n"
				+ "    lcxx.mingc huocmc, sum(lcxm.xieml) xieml\n"
				+ "from luncxmb lcxm, fahb fh, luncxxb lcxx, item\n"
				+ "where lcxm.fahb_id = fh.id\n"
				+ "    and fh.luncxxb_id = lcxx.id\n"
				+ "    and lcxm.bianz_id = item.id\n"
				+ "    and lcxm.riq >= to_date('"
				+ getBRiq()
				+ "', 'yyyy-MM-dd')\n"
				+ "    and lcxm.riq < to_date('"
				+ getERiq()
				+ "', 'yyyy-MM-dd') + 1\n"
				+ "group by rollup(item.id, item.mingc, lcxm.riq, lcxx.mingc, lcxm.xieml)\n"
				+ "having not (grouping(item.mingc)+grouping(lcxm.riq)+grouping(lcxx.mingc)+grouping(lcxm.xieml) = 1\n"
				+ "    or grouping(item.mingc)+grouping(lcxm.riq)+grouping(lcxx.mingc)+grouping(lcxm.xieml) = 2\n"
				+ "    or grouping(item.id)+grouping(item.mingc)+grouping(lcxm.riq)+grouping(lcxx.mingc)+grouping(lcxm.xieml) = 4)\n"
				+ "order by item.id, lcxm.riq";

			ArrWidth = new int[] {130, 110, 180, 100};

			ArrHeader = new String[1][4];
			ArrHeader[0][0] = "班组";
			ArrHeader[0][1] = "日期";
			ArrHeader[0][2] = "货船名称";
			ArrHeader[0][3] = "卸煤量";

		} else {
			sql = "select decode(grouping(lcxm.riq)+grouping(item.id)+grouping(item.mingc)+grouping(lcxx.mingc)+grouping(lcxm.xieml), 5, '合计', to_char(lcxm.riq, 'yyyy-MM-dd')) riq,\n"
				+ "    decode(grouping(lcxm.riq)+grouping(item.id)+grouping(item.mingc)+grouping(lcxx.mingc)+grouping(lcxm.xieml), 4, '当日小计', item.mingc) banz,\n"
				+ "    lcxx.mingc huocmc, sum(lcxm.xieml)\n"
				+ "from luncxmb lcxm, fahb fh, luncxxb lcxx, item\n"
				+ "where lcxm.fahb_id = fh.id\n"
				+ "    and fh.luncxxb_id = lcxx.id\n"
				+ "    and lcxm.bianz_id = item.id\n"
				+ "    and lcxm.riq >= to_date('"
				+ getBRiq()
				+ "', 'yyyy-MM-dd')\n"
				+ "    and lcxm.riq < to_date('"
				+ getERiq()
				+ "', 'yyyy-MM-dd') + 1\n"
				+ "group by rollup(lcxm.riq, item.id, item.mingc, lcxx.mingc, lcxm.xieml)\n"
				+ "having not (grouping(item.id)+grouping(item.mingc)+grouping(lcxx.mingc)+grouping(lcxm.xieml) = 1\n"
				+ "    or grouping(item.id)+grouping(item.mingc)+grouping(lcxx.mingc)+grouping(lcxm.xieml) = 2\n"
				+ "    or grouping(item.id)+grouping(item.mingc)+grouping(lcxx.mingc)+grouping(lcxm.xieml) = 3)\n"
					+ "order by lcxm.riq, item.id";

			ArrWidth = new int[] {110, 130, 180, 100};

			ArrHeader = new String[1][4];
			ArrHeader[0][0] = "日期";
			ArrHeader[0][1] = "班组";
			ArrHeader[0][2] = "货船名称";
			ArrHeader[0][3] = "卸煤量";
		}

		ResultSetList rslData = con.getResultSetList(sql);

		rt.setTitle(((Visit) this.getPage().getVisit()).getDiancqc() + "<br>卸煤数量", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 1));
		rt.body.setPageRows(20);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.mergeFixedCols();
		if (rslData.getRows() > 0) {
			rt.body.mergeCell(rslData.getRows() + 1, 1, rslData.getRows() + 1, 2);
		}
		rt.setDefaultTitle(1, 2, "制表单位："
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, "查询日期：" + getBRiq() + " 至 " + getERiq(),
				Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 1, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 1, "制表：", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();

		return rt.getAllPagesHtml();
	}

	private void getSelectData() {

		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("查询日期："));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");
		tbr.addField(dfb);
		tbr.addText(new ToolbarText(" "));
		tbr.addText(new ToolbarText(" 至 "));
		tbr.addText(new ToolbarText(" "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");
		tbr.addField(dfe);

		tbr.addText(new ToolbarText("-"));
		tbr.addText(new ToolbarText("类别："));
		ComboBox Leibcomb = new ComboBox();
		Leibcomb.setWidth(100);
		Leibcomb.setTransform("Leib");
		Leibcomb.setId("Leib");
		Leibcomb.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
		Leibcomb.setLazyRender(true);
		Leibcomb.setEditable(true);
		tbr.addField(Leibcomb);

		tbr.addText(new ToolbarText("-"));
		ToolbarButton tbrtn = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tbr.addItem(tbrtn);
		tbr.addFill();
		setToolbar(tbr);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}
