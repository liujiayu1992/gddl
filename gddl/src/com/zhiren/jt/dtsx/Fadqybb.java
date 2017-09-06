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
 * @author huochaoyuan
 * 陕西分公司日报程序用
 *（发电企业报表<外报数量>）计划量取自月运输计划
 *
 */
/**
 * chenzt 2010-04-06 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Fadqybb extends BasePage {
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

		return getFadqy();

	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	// 发电企业报表，取外报数据fahb biaoz(数量),chec(车数),where beiz='人工上报'，单厂显示；
	private String getFadqy() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer buffer = new StringBuffer("");
		String str = "";
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
		// String str1 = "and d.id = " + getTreeid();
		buffer
				.append("select decode(d.mingc,null,'总计',d.mingc) as dc,decode(g.mingc,null,'总计',g.mingc) as gys,decode(p.mingc,null,decode(g.mingc,null,'总计','小计'),p.mingc),a.qcrj,a.qcdr,a.hcrjcs,a.hcrj,a.hccs,a.hcdr,nvl(a.hcdr,0)+nvl(a.qcdr,0),' 'as beiz\n");
		buffer.append("from\n");
		buffer.append("(select z.id as id,z.gysid as gysid,z.pzid as pzid,\n");
		buffer.append("sum(hcdr.lail) as hcdr,sum(qcdr.lail) as qcdr,\n");
		buffer
				.append("sum(hcjh.duns) as hcjh,sum(qcjh.duns) as qcjh,sum(hcjh.rj) as hcrj,sum(hcjh.rjcs)as hcrjcs,sum(qcjh.rj) as qcrj,sum(hcdr.ches) as hccs\n");
		buffer.append("from\n");
		buffer
				.append("(select distinct f.diancxxb_id as id,f.gongysb_id as gysid ,f.pinzb_id as pzid from fahb f,diancxxb d\n");
		buffer.append("where to_char(f.daohrq, 'yyyy-mm-dd') = '"
				+ getBeginriqDate() + "'\n");
		buffer.append("and f.diancxxb_id=d.id(+)\n");
		buffer.append("and f.biaoz!=0 ");
		buffer.append(str + "\n");
		buffer.append(")z,\n");
		buffer
				.append("(select diancxxb_id as id,gongysb_id as gysid,pinzb_id as pzid ,round(sum(biaoz),0) as lail,sum(chec)as ches\n");
		buffer.append("from fahb\n");
		buffer.append("where to_char(daohrq, 'yyyy-mm-dd') = '"
				+ getBeginriqDate() + "'\n");
		buffer
				.append("and yunsfsb_id=(select id from yunsfsb where mingc='铁路') \n");
		buffer.append("group by diancxxb_id,gongysb_id,pinzb_id\n");
		buffer.append(")hcdr,\n");
		buffer
				.append("(select diancxxb_id as id,gongysb_id as gysid,pinzb_id as pzid,round(sum(biaoz),0) as lail\n");
		buffer.append("from fahb\n");
		buffer.append("where to_char(daohrq, 'yyyy-mm-dd') = '"
				+ getBeginriqDate() + "'\n");
		buffer
				.append("and (yunsfsb_id=(select id from yunsfsb where mingc='公路') or yunsfsb_id=(select id from yunsfsb where mingc='皮带') ) \n");
		buffer.append("group by diancxxb_id,gongysb_id,pinzb_id\n");
		buffer.append(")qcdr,\n");
		buffer
				.append("(select y.diancxxb_id as id,y.gongysb_id as gysid,y.pinzb_id as pzid,y.duns,round(y.duns/daycount(to_date('"
						+ getBeginriqDate()
						+ "','yyyy-mm-dd')),0) as rj,round(y.duns/(daycount(to_date('"
						+ getBeginriqDate()
						+ "','yyyy-mm-dd'))*63.5),0)as rjcs from\n");
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
		buffer
				.append(" and y.yunsfsb_id=m.yunsfsb_id and y.pinzb_id=m.pinzb_id\n");
		buffer.append(" and y.riq=m.riq");
		buffer.append(")hcjh,\n");
		buffer
				.append("(select y.diancxxb_id as id,y.gongysb_id as gysid,y.pinzb_id as pzid,y.duns,round(y.duns/daycount(to_date('"
						+ getBeginriqDate()
						+ "','yyyy-mm-dd')),0) as rj from\n");
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
		buffer
				.append(" and y.yunsfsb_id=m.yunsfsb_id and y.pinzb_id=m.pinzb_id\n");
		buffer.append(" and y.riq=m.riq");
		buffer.append(")qcjh\n");

		buffer.append(" where z.id=hcdr.id(+)\n");
		buffer.append(" and z.id=qcdr.id(+)\n");
		buffer.append(" and z.id=hcjh.id(+)\n");
		buffer.append(" and z.id=qcjh.id(+)\n");
		buffer.append(" and z.gysid=hcdr.gysid(+)\n");
		buffer.append(" and z.gysid=qcdr.gysid(+)\n");
		buffer.append(" and z.gysid=hcjh.gysid(+)\n");
		buffer.append(" and z.gysid=qcjh.gysid(+)\n");
		buffer.append(" and z.pzid=hcdr.pzid(+)\n");
		buffer.append(" and z.pzid=qcdr.pzid(+)\n");
		buffer.append(" and z.pzid=hcjh.pzid(+)\n");
		buffer.append(" and z.pzid=qcjh.pzid(+)\n");
		buffer.append(" group by z.id,rollup(z.gysid,z.pzid)\n");
		buffer.append(" )a,diancxxb d,gongysb g,pinzb p\n");
		buffer.append(" where a.id=d.id(+)\n");
		buffer.append(" and a.gysid=g.id(+)\n");
		buffer.append(" and a.pzid=p.id(+)");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		// 分厂
		ArrHeader = new String[4][11];
		ArrHeader[0] = new String[] { "单位", "供应商", "煤种", "日到煤", "日到煤", "日到煤",
				"日到煤", "日到煤", "日到煤", "日到煤", "存在问题" };
		ArrHeader[1] = new String[] { "单位", "供应商", "煤种", "汽车(皮带)", "汽车(皮带)",
				"火车", "火车", "火车", "火车", "合计<p>吨", "存在问题" };
		ArrHeader[2] = new String[] { "单位", "供应商", "煤种", "计划<p>吨", "完成<p>吨",
				"计划", "计划", "完成", "完成", "合计<p>吨", "存在问题" };
		ArrHeader[3] = new String[] { "单位", "供应商", "煤种", "计划<p>吨", "完成<p>吨",
				"车", "吨", "车", "吨", "合计<p>吨", "存在问题" };

		ArrWidth = new int[] { 110, 110, 50, 50, 50, 50, 50, 50, 50, 40, 140 };

		rt.setBody(new Table(rs, 4, 2, 3));
		rt.setTitle("煤炭发运情况日报<p>(" + getBeginriqDate() + ")", ArrWidth);
		rt
				.setDefaultTitle(1, 3, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4,
				FormatDate(DateUtil.getDate(getBeginriqDate())),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 3, "单位:吨、万千瓦", Table.ALIGN_RIGHT);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.createDefautlFooter(ArrWidth);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		// System.out.println(getTreeid());
		rt.body.setPageRows(36);
		// rt.body.mergeCell(4, 11, rt.body.getRows()-2, 11);
		// rt.body.mergeFixedRowCol();

		int zrkc = 0;
		int kc = 0;
		int zjrl = 0;
		int yxrl = 0;
		int fadl = 0;
		int rhm = 0;

		String shcsql =

		"select sum(nvl(vw.zongrl, 0) / 10) as zongrl,\n"
				+ "               sum(s.haoyqkdr) as haoyqkdr,\n"
				+ "              sum(s.kuc) as kuc,\n"
				+ "              sum(zr.kuc) as kuczr,\n"
				+ "              sum(s.fadl) as fdl,\n"
				+ "              (sum(nvl(vw.zongrl, 0) / 10)-sum(quemtjrl)) as yxrl\n"
				+ "          from (select diancxxb_id, haoyqkdr,kuc as kuc,fadl,quemtjrl\n"
				+ "                  from shouhcrbb\n"
				+ "                 where to_char(riq, 'yyyy-mm-dd') = '"
				+ getBeginriqDate() + "') s,\n"
				+ "                 (select diancxxb_id,kuc as kuc\n"
				+ "                  from shouhcrbb\n"
				+ "                 where riq = to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')-1) zr,\n"
				+ "               (select distinct diancxxb_id\n"
				+ "                  from fahb\n"
				+ "                 where to_char(daohrq, 'yyyy-mm-dd') = '"
				+ getBeginriqDate() + "') f,vwjizxx vw,diancxxb d\n"
				+ "         where f.diancxxb_id = s.diancxxb_id(+)\n"
				+ "           and f.diancxxb_id=vw.diancxxb_id(+)\n"
				+ "           and f.diancxxb_id=zr.diancxxb_id(+)\n"
				+ "           and f.diancxxb_id=d.id\n" + "           " + str
				+ "\n";

		try {

			ResultSet shc = con.getResultSet(shcsql);
			if (shc.next()) {
				zrkc = shc.getInt("kuczr");
				kc = shc.getInt("kuc");
				zjrl = shc.getInt("zongrl");
				yxrl = shc.getInt("yxrl");
				fadl = shc.getInt("fdl");
				rhm = shc.getInt("haoyqkdr");
			}
			shc.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		rt.body.merge(rt.body.getRows() - 1, 3, rt.body.getRows() - 1, 5);
		rt.body.merge(rt.body.getRows() - 1, 6, rt.body.getRows() - 1, 8);
		rt.body.merge(rt.body.getRows() - 1, 9, rt.body.getRows() - 1, 10);
		rt.body.merge(rt.body.getRows(), 3, rt.body.getRows(), 5);
		rt.body.merge(rt.body.getRows(), 6, rt.body.getRows(), 8);
		rt.body.merge(rt.body.getRows(), 9, rt.body.getRows(), 10);

		rt.body.setCellValue(rt.body.getRows() - 1, 1, "前日库存(吨)");
		rt.body.setCellValue(rt.body.getRows() - 1, 2, String.valueOf(zrkc)
				+ "吨");
		rt.body.setCellValue(rt.body.getRows() - 1, 3, "当期库存");

		rt.body
				.setCellValue(rt.body.getRows() - 1, 6, String.valueOf(kc)
						+ "吨");
		rt.body.setCellValue(rt.body.getRows() - 1, 9, "日耗煤");
		rt.body.setCellValue(rt.body.getRows() - 1, 11, String.valueOf(rhm)
				+ "吨");

		rt.body.setCellValue(rt.body.getRows(), 1, "装机(万千瓦)");
		rt.body.setCellValue(rt.body.getRows(), 2, String.valueOf(zjrl)
				+ "(万千瓦)");
		rt.body.setCellValue(rt.body.getRows(), 3, "运行(万千瓦)");

		rt.body.setCellValue(rt.body.getRows(), 6, String.valueOf(yxrl)
				+ "(万千瓦)");
		rt.body.setCellValue(rt.body.getRows(), 9, "发电量(万千瓦时)");
		rt.body.setCellValue(rt.body.getRows(), 11, String.valueOf(fadl)
				+ "(万千瓦时)");

		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();

		rt.setDefautlFooter(1, 2, "制表时间:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_CENTER);
		if (((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")) {

			rt.setDefautlFooter(9, 3, "制表:", Table.ALIGN_RIGHT);
		} else {

			rt.setDefautlFooter(9, 3, "制表:"
					+ ((Visit) getPage().getVisit()).getDiancmc(),
					Table.ALIGN_RIGHT);
		}

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		// con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
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

		// DateField df1 = new DateField();
		// df1.setValue(this.getEndriqDate());
		// df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
		// df1.setWidth(80);
		// tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		// tb1.addText(new ToolbarText("统计口径:"));
		// ComboBox cb1 = new ComboBox();
		// cb1.setTransform("LeixSelect");
		// cb1.setWidth(80);
		// tb1.addField(cb1);
		// tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "form0", null,
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

		// tb1.addText(new ToolbarText("供应商:"));
		// ComboBox cb2 = new ComboBox();
		// cb2.setTransform("GongysDropDown");
		// cb2.setEditable(true);
		// tb1.addField(cb2);

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
		// if(Value!=null&&((Visit)
		// getPage().getVisit()).getDropDownBean2()!=null){
		// if(Value.getId()!=((Visit)
		// getPage().getVisit()).getDropDownBean2().getId()){
		// ((Visit) getPage().getVisit()).setBooleanData1(true);
		// }else{
		// ((Visit) getPage().getVisit()).setBooleanData1(false);
		// }
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
		// }
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
		String sql = "select id,mingc\n" + "from gongysb\n"
				+ "where gongysb.fuid=0";
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

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
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
