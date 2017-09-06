package com.zhiren.jt.dtsx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * 
 * @author huochaoyuan
 * 陕西分公司日报程序用
 * (公交办报表<外报数据>)计划量取自月运输计划
 *
 */
/**
 * chenzt 2010-04-06 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Shouhcrb_fc extends BasePage {
	public boolean getRaw() {
		return true;
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;

		return GongjbReport();

	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
//		Visit visit = (Visit) getPage().getVisit();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 陕西公交办报表，取外报数据fabh biaoz(数量),chec(车数)，where
	// beiz="人工上报"，计划量取当月运输计划，累计为实际当月来没累计数量，库存为外包库存(shouhcrbb.shangbkc)
	private String GongjbReport() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		// String strgys="";
		// if(!getGongysDropDownValue().getValue().equals("全部")){
		// strgys=" and g.id="+getGongysDropDownValue().getId()+"\n";
		// }
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}

		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
					+ ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
		StringBuffer buffer = new StringBuffer("");

		buffer
				.append("select decode(a.id,0.1,'总计',d.mingc),decode(a.id,0.1,'总计',decode(a.gysid,0.2,'小计',g.mingc)),\n");
		buffer
				.append("(nvl(a.hcdr,0)+nvl(a.qcdr,0)) as xj,a.hcdr,a.qcdr,a.hcjh,a.hclj,(nvl(a.hcjh,0)-nvl(a.hclj,0)) as hccc,\n");
		buffer
				.append("a.qcjh,a.qclj,(nvl(a.qcjh,0)-nvl(a.qclj,0)) as qccc,s.haoyqkdr,s.hmlj,s.kuc\n");
		buffer.append("from\n");
		buffer
				.append("(select decode(z.id,null,0.1,z.id) as id,decode(z.gysid,null,0.2,z.gysid)as gysid,sum(hcdr.lail) as hcdr,sum(qcdr.lail) as qcdr,\n");
		buffer
				.append("sum(hcjh.duns) as hcjh,sum(hclj.lmlj) as hclj,sum(qcjh.duns) as qcjh,sum(qclj.lmlj)as qclj\n");
		buffer.append("from\n");
		buffer
				.append("(select distinct diancxxb_id as id,gongysb_id as gysid from fahb where to_char(daohrq, 'yyyy-mm-dd') ='"
						+ getBeginriqDate()
						+ "' and biaoz!=0 )z,\n");
		buffer
				.append("(select diancxxb_id as id,gongysb_id as gysid,round(sum(biaoz),0) as lail\n");
		buffer.append("from fahb\n");
		buffer.append("where to_char(daohrq, 'yyyy-mm-dd') ='"
				+ getBeginriqDate() + "'\n");
		buffer
				.append("and yunsfsb_id=(select id from yunsfsb where mingc='铁路') \n");
		buffer.append("group by diancxxb_id,gongysb_id\n");
		buffer.append(")hcdr,\n");
		buffer
				.append("(select diancxxb_id as id,gongysb_id as gysid,round(sum(biaoz),0) as lail\n");
		buffer.append("from fahb\n");
		buffer.append("where to_char(daohrq, 'yyyy-mm-dd') ='"
				+ getBeginriqDate() + "'\n");
		buffer
				.append("and (yunsfsb_id=(select id from yunsfsb where mingc='公路') or yunsfsb_id=(select id from yunsfsb where mingc='皮带') ) \n");
		buffer.append("group by diancxxb_id,gongysb_id\n");
		buffer.append(")qcdr,\n");
		buffer.append(" (select diancxxb_id as id,gongysb_id as gysid,\n");
		buffer.append("sum(biaoz) as lmlj\n");
		buffer.append("from fahb\n");
		buffer.append(" where to_char(daohrq, 'yyyy-mm-dd') >='"
				+ getBeginriqDate().substring(0, 7) + "-01'\n");
		buffer.append("and to_char(daohrq, 'yyyy-mm-dd') <='"
				+ getBeginriqDate() + "'\n");
		buffer
				.append("and yunsfsb_id = (select id from yunsfsb where mingc='铁路') \n");
		buffer.append(" group by diancxxb_id, gongysb_id) hclj,\n");
		buffer.append("(select diancxxb_id as id,gongysb_id as gysid,\n");
		buffer.append("sum(biaoz) as lmlj\n");
		buffer.append("from fahb\n");
		buffer.append(" where to_char(daohrq, 'yyyy-mm-dd') >='"
				+ getBeginriqDate().substring(0, 7) + "-01'\n");
		buffer.append("and to_char(daohrq, 'yyyy-mm-dd') <='"
				+ getBeginriqDate() + "'\n");
		buffer
				.append("and (yunsfsb_id=(select id from yunsfsb where mingc='公路') or yunsfsb_id=(select id from yunsfsb where mingc='皮带') ) \n");
		buffer.append(" group by diancxxb_id, gongysb_id) qclj,\n");
		buffer
				.append("(select y.diancxxb_id as id,y.gongysb_id as gysid,sum(y.duns) as duns from\n");
		buffer
				.append(" (select diancxxb_id as id,gongysb_id as gysid,yunsfsb_id,pinzb_id,max(riq) as riq from yueysjh\n");
		buffer.append(" where to_char(riq, 'yyyy-mm') ='"
				+ getBeginriqDate().substring(0, 7) + "'\n");
		buffer
				.append(" and yunsfsb_id=(select id from yunsfsb where mingc='铁路') and to_char(riq, 'yyyy-mm-dd') <='"
						+ getBeginriqDate() + "'\n");
		buffer
				.append(" group by diancxxb_id,gongysb_id,yunsfsb_id,pinzb_id)m,yueysjh y\n");
		buffer.append(" where y.diancxxb_id=m.id\n");
		buffer.append(" and y.gongysb_id=m.gysid\n");
		buffer.append(" and y.yunsfsb_id=m.yunsfsb_id\n");
		buffer
				.append(" and y.riq=m.riq and y.pinzb_id=m.pinzb_id group by y.diancxxb_id,y.gongysb_id");
		buffer.append(")hcjh,\n");
		buffer
				.append("(select y.diancxxb_id as id,y.gongysb_id as gysid,sum(y.duns)as duns from\n");
		buffer
				.append(" (select diancxxb_id as id,gongysb_id as gysid,yunsfsb_id,pinzb_id,max(riq) as riq from yueysjh\n");
		buffer.append(" where to_char(riq, 'yyyy-mm') ='"
				+ getBeginriqDate().substring(0, 7) + "'\n");
		buffer
				.append(" and (yunsfsb_id=(select id from yunsfsb where mingc='公路') or yunsfsb_id=(select id from yunsfsb where mingc='皮带') ) and to_char(riq, 'yyyy-mm-dd') <='"
						+ getBeginriqDate() + "'\n");
		buffer
				.append(" group by diancxxb_id,gongysb_id,yunsfsb_id,pinzb_id)m,yueysjh y\n");
		buffer.append(" where y.diancxxb_id=m.id\n");
		buffer.append(" and y.gongysb_id=m.gysid\n");
		buffer.append(" and y.yunsfsb_id=m.yunsfsb_id\n");
		buffer
				.append(" and y.riq=m.riq and y.pinzb_id=m.pinzb_id group by y.diancxxb_id,y.gongysb_id");
		buffer.append(")qcjh,diancxxb d\n");
		buffer.append("\n");
		buffer.append(" where z.id=hcdr.id(+)\n");
		buffer.append(" and z.id=qcdr.id(+)\n");
		buffer.append(" and z.id=hclj.id(+)\n");
		buffer.append(" and z.id=qclj.id(+)\n");
		buffer.append(" and z.id=hcjh.id(+)\n");
		buffer.append(" and z.id=qcjh.id(+)\n");
		buffer.append(" and z.gysid=hcdr.gysid(+)\n");
		buffer.append(" and z.gysid=qcdr.gysid(+)\n");
		buffer.append(" and z.gysid=hclj.gysid(+)\n");
		buffer.append(" and z.gysid=qclj.gysid(+)\n");
		buffer.append(" and z.gysid=hcjh.gysid(+)\n");
		buffer.append(" and z.gysid=qcjh.gysid(+)\n");
		buffer.append(" and z.id=d.id\n");
		buffer.append(str + "\n");
		buffer.append(" group by rollup (z.id,z.gysid)\n");
		buffer.append(" )a,\n");
		buffer
				.append(" (select decode(f.diancxxb_id, null, 0.1, max(f.diancxxb_id)) as id,\n");
		buffer.append("               f.diancxxb_id,\n");
		buffer.append("               0.2 as gysid,\n");
		buffer.append("               sum(s.haoyqkdr) as haoyqkdr,\n");
		buffer.append("               sum(s.shangbkc) as kuc,\n");
		buffer.append("               sum(lj.lj) as hmlj\n");
		buffer
				.append("          from (select diancxxb_id, haoyqkdr,shangbkc\n");
		buffer.append("                  from shouhcrbb\n");
		buffer.append("                 where to_char(riq, 'yyyy-mm-dd') ='"
				+ getBeginriqDate() + "') s,\n");
		buffer.append("               (select distinct diancxxb_id\n");
		buffer.append("                  from fahb\n");
		buffer.append("                 where to_char(daohrq, 'yyyy-mm-dd') ='"
				+ getBeginriqDate() + "') f,\n");
		buffer
				.append("               (select diancxxb_id, sum(haoyqkdr) as lj\n");
		buffer.append("                  from shouhcrbb\n");
		buffer.append("                 where to_char(riq, 'yyyy-mm-dd') >= '"
				+ getBeginriqDate().substring(0, 7) + "-01'\n");
		buffer.append("                   and to_char(riq, 'yyyy-mm-dd') <='"
				+ getBeginriqDate() + "'\n");
		buffer.append("                 group by diancxxb_id) lj\n");
		// buffer.append(" diancxxb d\n");
		buffer.append("         where f.diancxxb_id = s.diancxxb_id(+)\n");
		// buffer.append(" and f.diancxxb_id = d.id(+)\n");
		buffer.append("           and f.diancxxb_id = lj.diancxxb_id\n");
		buffer.append("\n");
		buffer
				.append("         group by rollup(f.diancxxb_id)) s,gongysb g,diancxxb d\n");
		buffer.append("\n");
		buffer.append(" where a.id=s.id(+)\n");
		buffer.append(" and a.gysid=s.gysid(+)\n");
		buffer.append(" and a.id=d.id(+)\n");
		buffer.append(" and a.gysid=g.id(+)\n");
		buffer.append(" order by a.id desc,a.gysid desc\n");
		buffer.append("\n");
		buffer.append("\n");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		// 分厂
		ArrHeader = new String[4][14];
		ArrHeader[0] = new String[] { "电厂", "供应商", "供(进)煤量", "供(进)煤量",
				"供(进)煤量", "供(进)煤量", "供(进)煤量", "供(进)煤量", "供(进)煤量", "供(进)煤量",
				"供(进)煤量", "耗煤", "耗煤", "库存" };
		ArrHeader[1] = new String[] { "电厂", "供应商", "当日", "当日", "当日", "累计",
				"累计", "累计", "累计", "累计", "累计", "当日", "累计", "库存" };
		ArrHeader[2] = new String[] { "电厂", "供应商", "小计", "火车", "汽车<p>(皮带)",
				"火车", "火车", "火车", "汽车(皮带)", "汽车(皮带)", "汽车(皮带)", "当日", "累计",
				"库存" };
		ArrHeader[3] = new String[] { "电厂", "供应商", "小计", "火车", "汽车<p>(皮带)",
				"计划", "实际", "超欠", "计划", "实际", "超欠", "当日", "累计", "库存" };

		ArrWidth = new int[] { 120, 120, 50, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50 };

		rt.setBody(new Table(rs, 4, 0, 0));
		rt.setTitle("全省直供进煤、耗煤、库存日报表<p>(" + getBeginriqDate() + ")", ArrWidth);
		rt
				.setDefaultTitle(1, 2, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4,
				FormatDate(DateUtil.getDate(getBeginriqDate())),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(13, 2, "单位:吨", Table.ALIGN_RIGHT);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.createDefautlFooter(ArrWidth);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(12);
		rt.body.mergeCol(1);
		rt.body.setCells(5, 1, rt.body.getRows(), 2, Table.PER_ALIGN,
				Table.ALIGN_LEFT);
		rt.body.mergeFixedRowCol();
		rt.setDefautlFooter(1, 2, "制表时间：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 4, "审核:", Table.ALIGN_CENTER);

		if (((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")) {

			rt.setDefautlFooter(13, 2, "制表:", Table.ALIGN_RIGHT);
		} else {

			rt.setDefautlFooter(13, 2, "制表:"
					+ ((Visit) getPage().getVisit()).getDiancmc(),
					Table.ALIGN_RIGHT);
		}
		// rt.setDefautlFooter(13,2, "制表：",Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	private String dianc_id = "";

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
		if (!dianc_id.equals(getTreeid())) {
			getGongysDropDownModels();
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		// 日期
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime", "");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "Form0", null,
				getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			setGongysDropDownModel(null);
			getGongysDropDownModels();
		}
		getToolbars();
		blnIsBegin = true;

	}

	// 供应商

	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getGongysDropDownModels() {
		String sql = "select id, mingc\n"
				+ "  from gongysb\n"
				+ " where id in (select gongysb_id from gongysdcglb where diancxxb_id = "
				+ getTreeid() + ")";
		dianc_id = getTreeid();
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}

	// 日期
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			// stra.set(DateUtil.getYear(new Date()), 0, 1);
			stra.setTime(new Date());
			stra.add(Calendar.DATE, -1);
			stra.set(Calendar.DATE, stra.getActualMinimum(Calendar.DATE));
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate() {
		if (((Visit) getPage().getVisit()).getString5() == null
				|| ((Visit) getPage().getVisit()).getString5() == "") {
			((Visit) getPage().getVisit()).setString5(DateUtil
					.FormatDate(new Date()));

		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setEndriqDate(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}

	// 类型
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setLeixSelectValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);

	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分厂"));
		list.add(new IDropDownBean(2, "分矿"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
