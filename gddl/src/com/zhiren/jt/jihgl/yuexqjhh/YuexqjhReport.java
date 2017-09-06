package com.zhiren.jt.jihgl.yuexqjhh;

/**
 * 作者： ww
 * 时间： 2010-01-19
 * 描述： 修改一厂多制月需用量计划查询选择总厂显示分厂及汇总
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-20 15：25
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class YuexqjhReport extends BasePage {
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

		return getZhiltj();

	}

	// 合同量分厂,分矿,分矿分厂统计报表
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq = "";// FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib = this.getDiancTreeJib();
		Visit visit = (Visit) getPage().getVisit();

		// 年，月
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		riq = "" + intyear + "年" + StrMonth + "月";

		/*
		 * // 审核状态 String shenhzt = ""; if (getShenhDropDownValue() != null) {
		 * shenhzt = " and yx.zhuangt=" + getShenhDropDownValue().getId(); }
		 */

		if (jib == 1) {// 

			// sbsql.append("select decode(grouping(dc.mingc),1,'总计',dc.mingc)
			// as danw,\n");
			sbsql
					.append(" select decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
			sbsql
					.append("sum(yx.fadl) as fadl,round(decode(sum(yx.fadl),0,0,sum(yx.fadmh*yx.fadl)/sum(yx.fadl)),0) as fadmh,sum(yx.fadml) as fadml,\n");
			sbsql
					.append("sum(yx.gongrl) as gongrl,round(decode(sum(yx.gongrl),0,0,sum(yx.gongrmh*yx.gongrl)/sum(yx.gongrl)),0)  as gongrmh,sum(yx.gongrml),\n");
			sbsql
					.append("sum(yx.sunh) as sunh,sum(yx.qity) as qity,sum(yx.yuekc) as yuekc,\n");
			sbsql.append("sum(yx.yuemkc) as yuemkc,sum(yx.xuqsl) as xuqsl\n");
			sbsql.append("from yuexqjhh yx,vwdianc dc\n");
			sbsql.append("where yx.diancxxb_id=dc.id \n");
			sbsql.append("and yx.riq=to_date('" + intyear + "-" + StrMonth
					+ "-01','yyyy-mm-dd')\n");
			// sbsql.append(shenhzt);
			sbsql.append("group by rollup (dc.fgsmc,dc.mingc)\n");
			sbsql
					.append("order by grouping(dc.fgsmc) desc ,dc.fgsmc,grouping(dc.mingc) desc ,max(dc.xuh)\n");

		} else if (jib == 2) {// 分公司及燃料公司
			String ranlgs = "select id from diancxxb where shangjgsid= "
					+ this.getTreeid();
			try {
				ResultSet rl = con.getResultSet(ranlgs);
				if (rl.next()) {// 燃料公司
					sbsql
							.append(" select decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',\n");
					sbsql
							.append("2,dc.rlgsmc,1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
					sbsql
							.append("sum(yx.fadl) as fadl,round(decode(sum(yx.fadl),0,0,sum(yx.fadmh*yx.fadl)/sum(yx.fadl)),0) as fadmh,sum(yx.fadml) as fadml,\n");
					sbsql
							.append("sum(yx.gongrl) as gongrl,round(decode(sum(yx.gongrl),0,0,sum(yx.gongrmh*yx.gongrl)/sum(yx.gongrl)),0)  as gongrmh,sum(yx.gongrml),\n");
					sbsql
							.append("sum(yx.sunh) as sunh,sum(yx.qity) as qity,sum(yx.yuekc) as yuekc,\n");
					sbsql
							.append("sum(yx.yuemkc) as yuemkc,sum(yx.xuqsl) as xuqsl\n");
					sbsql.append("from yuexqjhh yx,vwdianc dc\n");
					sbsql.append("where yx.diancxxb_id=dc.id \n");
					sbsql.append("and yx.riq=to_date('" + intyear + "-"
							+ StrMonth + "-01','yyyy-mm-dd')\n");
					// sbsql.append(shenhzt);
					sbsql.append(
							" and (dc.fuid=  " + this.getTreeid()
									+ " or dc.shangjgsid= " + this.getTreeid()
									+ ") ").append("\n");
					sbsql
							.append("group by rollup (dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
					sbsql
							.append("order by grouping(dc.rlgsmc) desc,dc.rlgsmc,grouping(dc.fgsmc) desc ,dc.fgsmc,grouping(dc.mingc) desc,dc.mingc,max(dc.xuh),grouping(g.mingc) desc,max(g.xuh)\n");
				} else {// 分公司
					sbsql
							.append(" select decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
					sbsql
							.append("sum(yx.fadl) as fadl,round(decode(sum(yx.fadl),0,0,sum(yx.fadmh*yx.fadl)/sum(yx.fadl)),0) as fadmh,sum(yx.fadml) as fadml,\n");
					sbsql
							.append("sum(yx.gongrl) as gongrl,round(decode(sum(yx.gongrl),0,0,sum(yx.gongrmh*yx.gongrl)/sum(yx.gongrl)),0)  as gongrmh,sum(yx.gongrml),\n");
					sbsql
							.append("sum(yx.sunh) as sunh,sum(yx.qity) as qity,sum(yx.yuekc) as yuekc,\n");
					sbsql
							.append("sum(yx.yuemkc) as yuemkc,sum(yx.xuqsl) as xuqsl\n");
					sbsql.append("from yuexqjhh yx,vwdianc dc\n");
					sbsql.append("where yx.diancxxb_id=dc.id \n");
					sbsql.append("and yx.riq=to_date('" + intyear + "-"
							+ StrMonth + "-01','yyyy-mm-dd')\n");
					// sbsql.append(shenhzt);
					sbsql.append(
							" and (dc.fuid=  " + this.getTreeid()
									+ " or dc.shangjgsid= " + this.getTreeid()
									+ ") ").append("\n");
					sbsql.append("group by rollup (dc.fgsmc,dc.mingc)\n");
					sbsql
							.append("order by grouping(dc.fgsmc) desc ,dc.fgsmc,grouping(dc.mingc) desc ,max(dc.xuh)\n");
				}
				rl.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}

		} else {// 集团，分公司

			sbsql
					.append("select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danw,\n");
			sbsql
					.append("sum(yx.fadl) as fadl,round(decode(sum(yx.fadl),0,0,sum(yx.fadmh*yx.fadl)/sum(yx.fadl)),0) as fadmh,sum(yx.fadml) as fadml,\n");
			sbsql
					.append("sum(yx.gongrl) as gongrl,round(decode(sum(yx.gongrl),0,0,sum(yx.gongrmh*yx.gongrl)/sum(yx.gongrl)),0)  as gongrmh,sum(yx.gongrml),\n");
			sbsql
					.append("sum(yx.sunh) as sunh,sum(yx.qity) as qity,sum(yx.yuekc) as yuekc,\n");
			sbsql.append("sum(yx.yuemkc) as yuemkc,sum(yx.xuqsl) as xuqsl\n");
			sbsql
					.append("from yuexqjhh yx,(select d.id, d.mingc,d.jingjcml ,d.rijhm,dc.xuh,dc.mingc as fengs, dc.id as fuid,dc.shangjgsid from diancxxb d, diancxxb dc  where d.jib = 3  and d.fuid = dc.id(+)) dc\n");
			sbsql.append("where yx.diancxxb_id=dc.id \n");
			sbsql.append("and yx.riq=to_date('" + intyear + "-" + StrMonth
					+ "-01','yyyy-mm-dd')\n");
			// sbsql.append(shenhzt);
			// 判断是否为总厂
			if (isZongC(con, visit)) {
				sbsql.append(" and dc.fuid=").append(this.getTreeid());
			} else {
				sbsql.append(" and dc.id=").append(this.getTreeid());
			}
			sbsql.append("group by rollup (dc.mingc)\n");
			sbsql.append("order by grouping(dc.mingc) desc,dc.mingc\n");
		}

		// System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { "单位", "发电量<br>(万千瓦时)",
				"发电原煤耗<br>(克/千瓦时)", "发电原煤量<br>(吨)", "供热量<br>(万吉焦)",
				"供热原煤耗<br>(千克/吉焦)", "供热用原煤量<br>(吨)", "运输损耗<br>(吨)",
				"其它用<br>(吨)", "月初库<br>(吨)", "月末库存<br>(吨)", "需求数量<br>(吨)" };

		int ArrWidth[] = new int[] { 120, 80, 40, 80, 40, 40, 65, 55, 55, 60,
				60, 60 };//780
//int ArrWidth[] = new int[] { 150, 80, 80, 80, 80, 80, 80, 80, 60, 60,
//		60, 60 };
		Table bt = new Table(rs, 1, 0, 1);
		rt.setBody(bt);
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		bt.setColAlign(2, Table.ALIGN_CENTER);

		if (rt.body.getRows() > 1) {
			rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
		}
		//
		rt.body.ShowZero = false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw)); 
		
		rt.body.mergeFixedRow();// 合并行
		rt.body.mergeFixedCols();// 和并列
		rt.setTitle("月需用量计划", ArrWidth);// getBiaotmc()+
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt
				.setDefaultTitle(1, 2, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, riq, Table.ALIGN_CENTER);
		rt.setDefaultTitle(11, 2, "单位:吨、吉焦/千克", Table.ALIGN_RIGHT);
		rt.body.setPageRows(rt.PAPER_ROWS);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"制表时间:"
						+ FormatDate(DateUtil.getDate(DateUtil
								.FormatDate(new Date()))), Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 4, "审批：", Table.ALIGN_CENTER);
		rt
				.setDefautlFooter(11, 2, "制表："
						+ ((Visit) getPage().getVisit()).getRenymc(),
						Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean isZongC(JDBCcon con, Visit visit) {
		if (visit.isFencb()) {
			String sSQL = "SELECT * FROM diancxxb WHERE fuid="
					+ this.getTreeid();
			return con.getHasIt(sSQL);
		}
		return false;
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	// /////
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

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("YUEF");
		cb2.setWidth(60);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
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

		/*
		 * tb1.addText(new ToolbarText("审核状态:")); ComboBox tblx = new
		 * ComboBox(); tblx.setTransform("ShenhDropDown"); tblx.setWidth(80);
		 * tb1.addField(tblx); tb1.addText(new ToolbarText("-"));
		 */

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
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);

			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();

			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
			
            //	begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
				//	visit.setString1(null);保存传递的非默认纸张的样式
		}
		getToolbars();
		blnIsBegin = true;

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
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	// 分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql, "中国大唐集团"));
	}

	// 得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

	}

	// 年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null || _NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
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

	// 类型
	public IDropDownBean getShenhDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getShenhDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setShenhDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setShenhDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getShenhDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getShenhDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getShenhDropDownModels() {
		List list = new ArrayList();
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyjb() == 3) {// 电厂
			list.add(new IDropDownBean(0, "未提交"));
			list.add(new IDropDownBean(1, "已提交"));
		} else if (visit.getRenyjb() == 2) {// 分公司
			list.add(new IDropDownBean(1, "未审核"));
			list.add(new IDropDownBean(2, "已审核"));
		} else {// 集团
			list.add(new IDropDownBean(2, "未审核"));
			list.add(new IDropDownBean(3, "已审核"));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(list));
		return;
	}
}
