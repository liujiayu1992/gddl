package com.zhiren.jt.jiesgl.report.changfhs;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 作者：张立东
 * 时间：2009-10-30
 * 描述：调整煤矿地区的取值。
 */
public class Guscx extends BasePage {
	// 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}

	// 页面判定方法
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

	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	// 绑定日期
	public String getRiq() {

		if (((Visit) this.getPage().getVisit()).getString1() == null
				|| ((Visit) this.getPage().getVisit()).getString1().equals("")) {

			((Visit) this.getPage().getVisit()).setString1(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString1() != null
				&& !((Visit) this.getPage().getVisit()).getString1().equals(
						riq1)) {

			((Visit) this.getPage().getVisit()).setString1(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}

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

	// 得到登陆人员所属电厂或分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;

	}

	private boolean isBegin = false;

	public String getPrintTable() {

		if (!isBegin) {
			return "";
		}

		return getCaiggsb();
	}

	public String getCaiggsb() {
		// 采购估收表
		_CurrentPage = 1;
		_AllPages = 1;

		Report rt = new Report();
		JDBCcon cn = new JDBCcon();

		String strC_Diancxxb_id = ""; // 电厂条件
		if (!this.getTreeid().equals("0")) {

			strC_Diancxxb_id = " and f.diancxxb_id=" + this.getTreeid() + "\n";
		}
		int i = 0;
		String sql = "";
		String ArrHeader[][] = null;
		int ArrWidth[] = null;
		if (getGongysValue().getValue().equals("各矿小计")) {
			// 选择了供应商
			sql = "SELECT DECODE(M.MINGC, NULL, '总计', M.MINGC) AS MEIKDW,\n"
					+ "       SUM(ROUND_NEW(F.BIAOZ, 0)) AS BIAOZ,\n"
					+ "       SUM(ROUND_NEW(F.JINGZ, 0) + ROUND_NEW(F.YUNS, 0) -\n"
					+ "           ROUND_NEW(F.BIAOZ, 0)) AS YINGK,\n"
					+ "       SUM(ROUND_NEW(F.YUNS, 0)) AS YUNS,\n"
					+ "       SUM(ROUND_NEW(F.JINGZ, 0)) AS GSL,\n"
					+ "       ROUND_NEW(SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            0,\n"
					+ "                            ROUND_NEW(F.JINGZ, 0) * GETGUSXX(F.ID, 'REZ'))) /\n"
					+ "                 SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            1,\n"
					+ "                            ROUND_NEW(JINGZ, 0))),\n"
					+ "                 0) AS REZ,\n"
					+ "       ROUND_NEW(SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            0,\n"
					+ "                            ROUND_NEW(F.JINGZ, 0) *\n"
					+ "                            (GETGUSXX(F.ID, 'MEIJ') + GETGUSXX(F.ID, 'YUNF')))) /\n"
					+ "                 SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            1,\n"
					+ "                            ROUND_NEW(JINGZ, 0))),\n"
					+ "                 2) AS GUJ,\n"
					+ "       ROUND_NEW(SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            0,\n"
					+ "                            ROUND_NEW(F.JINGZ, 0) * (GETGUSXX(F.ID, 'MEIJ')))) /\n"
					+ "                 SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            1,\n"
					+ "                            ROUND_NEW(JINGZ, 0))),\n"
					+ "                 2) AS MEIJ,\n"
					+ "       ROUND_NEW(SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            0,\n"
					+ "                            ROUND_NEW(F.JINGZ, 0) *\n"
					+ "                            (GETGUSXX(F.ID, 'YUNF') + GETGUSXX(F.ID, 'ZAF') +\n"
					+ "                             GETGUSXX(F.ID, 'FAZZF') +\n"
					+ "                             GETGUSXX(F.ID, 'DITF')))) /\n"
					+ "                 SUM(DECODE(ROUND_NEW(F.JINGZ, 0),\n"
					+ "                            0,\n"
					+ "                            1,\n"
					+ "                            ROUND_NEW(JINGZ, 0))),\n"
					+ "                 2) AS YUNJ\n"
					+ "  FROM FAHB F, MEIKXXB M, GONGYSMKGLB GLB\n"
					+ " WHERE F.MEIKXXB_ID = M.ID\n"
					+ "   AND M.ID = GLB.MEIKXXB_ID\n" + "   AND F.ID IN\n"
					+ "       (SELECT DISTINCT F.ID\n"
					+ "          FROM FAHB F, JIESB J\n"
					+ "         WHERE F.JIESB_ID = J.ID(+)\n"
					+ "           AND (J.RUZRQ IS NULL OR\n"
					+ "               J.RUZRQ > DATE '" + getRiq() + "')\n"
					+ "           AND F.DIANCXXB_ID = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "\n         AND F.DAOHRQ <= DATE '" + getRiq() + "')\n"
					+ " GROUP BY ROLLUP(M.MINGC)\n"
					+ " ORDER BY GROUPING(M.MINGC) DESC, M.MINGC";
			ArrHeader = new String[1][9];
			ArrHeader[0] = new String[] { "煤种", "票重<br>(吨)", "盈亏<br>(吨)",
					"运损<br>(吨)", "估收量<br>(吨)", "发热量<br>(kcal/kg)",
					"估价<br>(元/吨)", "煤价<br>(元/吨)", "运价<br>(元/吨)" };
			ArrWidth = new int[] { 120, 90, 60, 60, 90, 80, 75, 75, 75 };
		} else if (this.getGongysValue().getId() == -1) {
			// 没有选择供应商
			sql = "select\n"
					+ "      decode(g.mingc,null,'总计',g.mingc) as gongys,\n"
					+ "      biaoz,yingk,yuns,gsl,rez,guj,meij,yunj\n"
					+ "      from\n"
					+ "(select\n"
					+ "       g.meikdq_id as id,\n"
					+ "       sum(round_new(f.biaoz,0)) as  biaoz,\n"
					+ "       sum(round_new(f.jingz,0)+round_new(f.yuns,0)-round_new(f.biaoz,0)) as yingk,\n"
					+ "       sum(round_new(f.yuns,0)) as yuns,\n"
					+ "       sum(round_new(f.jingz,0)) as gsl,\n"
					+ "       round_new(sum(decode(round_new(f.jingz,0),0,0,f.jingz*getGusxx(f.id,'rez')))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),0) as rez,\n"
					+ "       round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*(getGusxx(f.id,'meij')+getGusxx(f.id,'yunf'))))\n"
					+ "                           /sum(decode(f.jingz,0,1,jingz)),2) as guj,\n"
					+ "       round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*(getGusxx(f.id,'meij'))))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),2) as meij,\n"
					+ "       round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*(getGusxx(f.id,'yunf')+getGusxx(f.id,'zaf')+getGusxx(f.id,'fazzf')+getGusxx(f.id,'ditf'))))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),2) as yunj\n"
					+ "\n"
					+ "       from fahb f,(select * from meikxxb) g\n"
					+ "       where f.meikxxb_id=g.id\n"
					+ "             and f.id in (\n"
					+ "                 select distinct f.id from fahb f,jiesb j\n"
					+ "                          where f.jiesb_id=j.id(+)\n"
					+ "                                and (j.ruzrq is null or j.ruzrq>to_date('"
					+ getRiq() + "','yyyy-MM-dd'))\n" + strC_Diancxxb_id
					+ "                                and f.daohrq<=to_date('"
					+ getRiq() + "','yyyy-MM-dd')\n" + "\n"
					+ "             )\n"
					+ "             group by rollup(g.meikdq_id)\n"
					+ " ) gs,gongysb g\n" + " where gs.id=g.id(+)\n"
					+ " order by g.mingc desc,g.mingc";

			ArrHeader = new String[1][9];
			ArrHeader[0] = new String[] { "煤种", "票重<br>(吨)", "盈亏<br>(吨)",
					"运损<br>(吨)", "估收量<br>(吨)", "发热量<br>(kcal/kg)",
					"估价<br>(元/吨)", "煤价<br>(元/吨)", "运价<br>(元/吨)" };
			ArrWidth = new int[] { 120, 90, 60, 60, 90, 80, 75, 75, 75 };
			i = 3;
		} else if (this.getGongysValue().getId() > -1) {
			sql = "select\n"
					+ "        decode(m.mingc,null,'总计',m.mingc) as meikdw,\n"
					+ "        decode(f.fahrq,null,decode(m.mingc,null,'','小计'),\n"
					+ " 		 to_char(f.fahrq,'yyyy-MM-dd')) as fahrq,\n"
					+ "        to_char(f.daohrq,'yyyy-MM-dd') as daohrq,\n"
					+ "        f.chec,sum(round_new(f.biaoz,0)) as  biaoz,\n"
					+ "        sum(round_new(f.jingz,0)+round_new(f.yuns,0)-round_new(f.biaoz,0)) as yingk,\n"
					+ "        sum(round_new(f.yuns,0)) as yuns,\n"
					+ "        sum(round_new(f.jingz,0)) as gsl,\n"
					+ "        round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*getGusxx(f.id,'rez')))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),0) as rez,\n"
					+ "        round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*(getGusxx(f.id,'meij')+getGusxx(f.id,'yunf'))))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),2) as guj,\n"
					+ "        round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*(getGusxx(f.id,'meij'))))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),2) as meij,\n"
					+ "        round_new(sum(decode(round_new(f.jingz,0),0,0,round_new(f.jingz,0)*(getGusxx(f.id,'yunf')+getGusxx(f.id,'zaf')+getGusxx(f.id,'fazzf')+getGusxx(f.id,'ditf'))))\n"
					+ "                           /sum(decode(round_new(f.jingz,0),0,1,round_new(jingz,0))),2) as yunj\n"
					+ "\n"
					+ "        from fahb f,meikxxb m,gongysmkglb glb\n"
					+ "        where f.meikxxb_id=m.id\n"
					+ "              and m.id=glb.meikxxb_id\n"
					+ "              and glb.gongysb_id="
					+ getGongysValue().getId()
					+ "\n"
					+ "              and f.id in(\n"
					+ "\n"
					+ "                  select distinct f.id from fahb f,jiesb j\n"
					+ "                          where f.jiesb_id=j.id(+)\n"
					+ "                                and (j.ruzrq is null or j.ruzrq>to_date('"
					+ this.getRiq()
					+ "','yyyy-MM-dd'))\n"
					+ "                                and f.diancxxb_id="
					+ this.getTreeid()
					+ "\n"
					+ "                                and f.daohrq<=to_date('"
					+ this.getRiq()
					+ "','yyyy-MM-dd')\n"
					+ "\n"
					+ "        )"
					+ "	group by rollup(m.mingc,f.fahrq,f.daohrq,f.chec) \n"
					+ "   having not (grouping(f.fahrq)<>1 and grouping(f.chec)=1) \n"
					+ "	order by grouping(m.mingc) desc,m.mingc,grouping(f.fahrq) desc,f.fahrq";

			ArrHeader = new String[1][12];
			ArrHeader[0] = new String[] { "发货单位", "发货日期", "到货日期", "车次",
					"票重<br>(吨)", "盈亏<br>(吨)", "运损<br>(吨)", "估收数量<br>(吨)",
					"热量<br>(kcal/kg)", "估价<br>(元/吨）", "煤价<br>(元/吨）",
					"运价<br>(元/吨）" };

			ArrWidth = new int[] { 120, 80, 80, 70, 70, 54, 54, 70, 54, 54, 54,
					54 };
			i = 4;
		}

		ResultSet rs = cn.getResultSet(sql);

		rt.setTitle("估收统计表", ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:" + getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, i, "估收日期:" + this.getRiq(), Table.ALIGN_CENTER);

		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {

			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean1(null); // 供应商
			((Visit) getPage().getVisit()).setProSelectionModel1(null);

			((Visit) getPage().getVisit()).setDropDownBean2(null); // 电厂树用
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setString1(""); // riq
			((Visit) getPage().getVisit()).setString2(""); // Treeid
			((Visit) getPage().getVisit()).setboolean1(false); // 日期改变
		}
		getToolBars();
		if (((Visit) getPage().getVisit()).getboolean1()) {

			this.getIGongysModels();
		}
		isBegin = true;
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(getRiq());
		df.Binding("Riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", ""
						+ ((Visit) getPage().getVisit()).getDiancxxb_id(),
				"forms[0]", null, getTreeid());
		((Visit) getPage().getVisit()).setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
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

		tb1.addText(new ToolbarText("供应商:"));
		ComboBox comb = new ComboBox();
		comb.setTransform("GongsmcDropDown");
		comb.setId("Gongys");
		comb.setEditable(true);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(280);
		comb.setReadOnly(false);
		comb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(comb);

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");

		tb1.addItem(tb);
		setToolbar(tb1);
	}

	// 供货单位
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setGongysValue(IDropDownBean Value) {

		if (((Visit) getPage().getVisit()).getDropDownBean1() != Value) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setIGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getIGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIGongysModels() {

		String sql = "select 0 id,'各矿小计' mingc from diancxxb where id = 312\n"
				+ " union\n"
				+ "select * from"
				+ "(select distinct g.id,g.mingc from fahb f,gongysb g\n"
				+ "       where f.gongysb_id=g.id\n"
				+ "				and g.leix=1\n"
				+ "             and f.id in (\n"
				+ "             select distinct f.id from fahb f,jiesb j\n"
				+ "                    where f.jiesb_id=j.id(+)\n"
				+ "                          and (j.ruzrq is null or j.ruzrq>to_date('"
				+ this.getRiq() + "','yyyy-MM-dd'))\n"
				+ "                          and f.diancxxb_id="
				+ this.getTreeid() + "\n"
				+ "                          and f.daohrq<=to_date('"
				+ this.getRiq() + "','yyyy-MM-dd')\n" + "       )\n"
				+ "       order by g.mingc)";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql, "全部");
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getTreeid() {
		if (((Visit) getPage().getVisit()).getString2() == null
				|| ((Visit) getPage().getVisit()).getString2().equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		if (((Visit) getPage().getVisit()).getString2() != null) {
			if (!treeid.equals(((Visit) getPage().getVisit()).getString2())) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getIDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
}
