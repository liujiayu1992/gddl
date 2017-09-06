package com.zhiren.dc.huaygl.meizrbb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*王总兵
 *2010-4-28 15:47:28
 *1.增加二级审核完成以后才能出报表。
 *2.增加化验员审核人员二审人员姓名.
 */

public class Meizrbcx extends BasePage implements PageValidateListener {

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

	// 开始日期
	private Date _BeginriqValue = new Date();

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
			// _BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			// _BeginriqChange=true;
		}
	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			 this.getSelectData();
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		isBegin = true;
//		this.getSelectData();
	}

	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setBRiq(null);
			// setYuefValue(null);
			getBRiq();
			// getYuefModels();
			this.setTreeid(null);
			this.getTree();
			isBegin = true;

		}

		getToolBars();
		this.Refurbish();
	}

	private String RT_HET = "Yuedmjgmxreport";// 月度煤价格明细

	private String mstrReportName = "Yuedmjgmxreport";

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		if (mstrReportName.equals(RT_HET)) {
			return getSelectData();
		} else {
			return "无此报表";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt = 0;

	public void setZhuangt(int _value) {
		intZhuangt = 1;
	}

	private boolean isBegin = false;

	private String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

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

		String zhuangt_sl = "";
		String zhuangt_zl = "";
		String zhuangt_dj = "";

		String strGongsID = "";
		String guoltj = "";
		String notHuiz = "";
		String biaot = "";
		String biaoti = "";
		String group = "";
		String order = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strGongsID = " ";
			guoltj = " and dc.id not in(" + Guoldcid() + ")\n";
			biaot = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
			group = "  group by rollup(f.mingc,g.mingc)\n";

			order = " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " + this.getTreeid()
					+ " or dc.shangjgsid=" + this.getTreeid() + ")";
			guoltj = " and dc.id not in(" + Guoldcid() + ")\n";
			notHuiz = " having not grouping(f.mingc)=1 ";// 当电厂树是分公司时,去掉集团汇总

			biaot = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
			group = "  group by rollup(f.mingc,g.mingc)\n";

			order = " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strGongsID = " and dc.id= " + this.getTreeid();

			if (getBaoblxValue().getValue().equals("分厂汇总")) {
				notHuiz = " having not  grouping(dc.mingc)=1";
			} else if (getBaoblxValue().getValue().equals("分矿汇总")) {
				biaot = "select decode(grouping(g.mingc)+grouping(dc.mingc),2,'总计',1,dc.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
				group = "  group by rollup(dc.mingc,g.mingc)\n";

				order = " order by grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

				notHuiz = " having not  grouping(dc.mingc)=1";// 当电厂树是电厂时,去掉分公司和集团汇总
			} else {
				notHuiz = " having not  grouping(f.mingc)=1";// 当电厂树是电厂时,去掉分公司和集团汇总
			}
		} else if (jib == -1) {
			strGongsID = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			guoltj = "";
		}

		long yuefen = getYuefValue().getId();
		long yuefen1 = getYuefValue().getId();
		long yuefen2 = getYuefValue().getId();
		long yuefen3 = getYuefValue().getId();
		long yuefen4 = getYuefValue().getId();
		long nianfen = getNianfValue().getId();
		String nianyue = "";
		if (String.valueOf(yuefen).length() == 1) {
			nianyue = String.valueOf(nianfen) + "0" + String.valueOf(yuefen)
					+ "01";
		} else {
			nianyue = String.valueOf(nianfen) + String.valueOf(yuefen) + "01";
		}

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] = null;
		int iFixedRows = 0;// 固定行号
		int iCol = 0;// 列数
		// 报表内容

		biaoti = "燃料需用计划表";
	
	
		String rulrq="";
		

		String ssql = "select  distinct mingc , rulrq from (\n"
				+ "\n"
				+ " select distinct jfz.mingc as mingc ,jz.jizbh as jizbh,rzl1.rulrq as rulrq \n"
				+ "   from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
				+ "  where rzl1.rulrq = (to_date('" + getBRiq()
				+ "', 'yyyy-mm-dd')-1)\n" + "    and d.id = " + getTreeid()
				+ " and rzl1.jizfzb_id=jfz.id\n"
				+ "    and jgl.jizfzb_id = jfz.id\n"
				+ "    and jgl.jizb_id = jz.id\n" + ")  order by mingc  \n" + "\n" + "    ";
//		 System.out.println(ssql);
		StringBuffer ssb1 = new StringBuffer();// 记录第一个表和入场煤表
		StringBuffer ssb = new StringBuffer();// 记录第一个表和入场煤表
		StringBuffer ssbz = new StringBuffer();// 记录总表
		StringBuffer sbiao = new StringBuffer();// 记录第二列标题
		StringBuffer sbiao1 = new StringBuffer();// 记录第一行的标题
		StringBuffer hangs = new StringBuffer();// 记录行的数量
		int[] hang = new int[1];
		ResultSetList rs1 = cn.getResultSetList(ssql);

		ssb1
				.append("("
						+ "select '收到基水分Mar(%)'as zhibiao ,1 as xuh   from dual union\n"
						+ "select  '空干基水分Mad(%)' as zhibiao,2 as xuh   from dual union\n"
						+ "select ' 空干基灰分Aad(%)'as zhibiao,3  as xuh   from dual union\n"
						+ "select '干燥基灰分Ad(%)'as zhibiao,4  as xuh   from dual union\n"
						+ "select '空干基挥发分Vad(%)'as zhibiao,5  as xuh   from dual union\n"
						+ "select '干燥无灰基挥发分Vdaf(%)'as zhibiao,6  as xuh   from dual union\n"
						+ "select '固定碳Fc,ad(%)'as zhibiao,7  as xuh   from dual union\n"
						+ "select '空干基全硫St,ad(%)'as zhibiao,8  as xuh   from dual union\n"
						+ "select '干燥基全硫St,d(%)'as zhibiao,9  as xuh   from dual union\n"
						+ "select '空干基氢值Had(%)'as zhibiao,10  as xuh   from dual union\n"
						+ "select '弹筒发热量Qb,ad(J/g)'as zhibiao,11  as xuh   from dual union\n"
						+ "select '干燥基高位热值Qgr.d(J/g)'as zhibiao,12  as xuh   from dual union\n"
						+ "select '收到基低位热值<br>Qnet,ar(J/g)'as zhibiao,13  as xuh   from dual \n"
						+ "order by xuh" + ") zhib");
		sbiao1.append("");
		sbiao.append("");
		hangs.append(100);
		hang[0] = 100;
		int i = 0;
		while (rs1.next()) {
//			rulrq=rs1.getString("rulrq");
			i++;
			String jizfz = rs1.getString("mingc");// 目的取到所有系统
//			System.out.println(rs1.getString("rulrq")+"@@@@@@");
//			System.out.println(jizfz+rulrq);
			String ssql1 = "select jizbh as  bianh from (\n"
					+ "\n"
					+ " select distinct jz.jizbh as jizbh\n"
					+ "   from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
					+ "  where rzl1.rulrq = to_date('" + getBRiq()
					+ "', 'yyyy-mm-dd')-1 \n" + "    and d.id = " + getTreeid()
					+ " and rzl1.jizfzb_id=jfz.id\n"
					+ "    and jgl.jizfzb_id = jfz.id\n"
					+ "    and jgl.jizb_id = jz.id\n and jfz.mingc='" + jizfz
					+ "'" + " )  order by jizbh  \n" +

					"      \n" + "\n" + "   ";
			sbiao.append("," + jizfz + "<br>(");
			// System.out.println(jizfz);
			// System.out.println(ssql1);
			// System.out.println(getBRiq());
			int abc = 0;
			ResultSetList rs11 = cn.getResultSetList(ssql1);
			while (rs11.next()) {
				abc++;
				if (rs11.getRows() == 1) {

					sbiao.append("" + rs11.getString("bianh"));
				}
				if (rs11.getRows() >= 1 && abc == 1) {
					sbiao.append("" + rs11.getString("bianh"));
				}
				if (rs11.getRows() >= 1 && abc > 1) {
					sbiao.append("、" + rs11.getString("bianh"));
				}

			}
			sbiao.append(")机");
			ssb
					.append(",( \n"
							+ "                              (     select to_char(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * mt) / sum(af), 2)\n"
							+ "                                         )) as jieg,1 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.mt,0) as mt\n"
							+ "                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id"
							+ " and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id "
							+ "  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ ")\n" + "\n"
							+ "                                          union"

					);// 拼接查询表1
			ssb
					.append("\n"
							+ "                               (    select "
							+
							// "decode(\n" +
							// " sum(af),\n" +
							// " 0,\n" +
							// " 0,\n" +
							// " round_new(sum(af * mt) / sum(af), 2)\n" +
							// " ) as mt," +
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*mad) / sum(af), 2) ),2) as jieg,2 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.mad,0) as mad\n"
							+
							// "rzl1.mt as mt,rzl1.mad as mad,rzl1.aad as
							// aad,rzl1.ad as ad ,rzl1.vad as vad,rzl1.vdaf as
							// vdaf" +
							// "rzl1.fcad as fcad,rzl1.stad as stad,rzl1.std as
							// std,rzl1.had as had,rzl1.qbad as qbad,rzl1.qgrd
							// as qgrd," +
							// "rzl1.qnet_ar as qnet_ar\n" +
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id   and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ ")\n" + "\n"
							+ "                                          union"

					);// 拼接查询表2
			ssb
					.append("\n"
							+ "                         (          select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*aad) / sum(af), 2)),2) as jieg,3 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.aad,0) as aad\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ ")\n" + "\n"
							+ "                                          union"

					);// 拼接查询表3
			ssb
					.append("\n"
							+ "                      (             select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*ad) / sum(af), 2) ),2) as jieg,4 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.ad,0) as ad\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ ")\n" + "\n"
							+ "                                          union"

					);// 拼接查询表4
			ssb
					.append("\n"
							+ "                          (         select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*vad) / sum(af), 2) ),2) as jieg,5 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.vad,0) as vad\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ ")\n" + "\n"
							+ "                                          union"

					);// 拼接查询表5
			ssb
					.append("\n"
							+ "                      (             select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*vdaf) / sum(af), 2) ),2) as jieg,6 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.vdaf,0) as vdaf\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "                     )                     union"

					);// 拼接查询表6
			ssb
					.append("\n"
							+ "                           (        select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*fcad) / sum(af), 2) ),2) as jieg,7 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.fcad,0) as fcad\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "                            )              union"

					);// 拼接查询表7
			ssb
					.append("\n"
							+ "                         (          select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*stad) / sum(af), 2) ),2) as jieg,8 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.stad,0) as stad\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "                                    )      union"

					);// 拼接查询表8
			ssb
					.append("\n"
							+ "                           (        select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*std) / sum(af), 2) ),2) as jieg,9 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.std,0) as std\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "                                     )     union"

					);// 拼接查询表9
			ssb
					.append("\n"
							+ "                              (     select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*had) / sum(af), 2) ),2) as jieg,10 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.had,0) as had\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "                                  )        union"

					);// 拼接查询表10
			ssb
					.append("\n"
							+ "                            (       select "
							+
							"Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*qbad) / sum(af), 2) ),2) as jieg,11 as xuh"
							+
							"                                        from (\n"
							+ "\n"
							+ "                                        select nvl(rzl1.qbad,0) as qbad\n"
							+
							"                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n" + "\n"
							+ "                              )           union"

					);// 拼接查询表11
			ssb
					.append("\n"
							+ "                         (          select "
							+ "Formatxiaosws(decode(sum(af),0,0,round_new(sum(af*qgrd) / sum(af), 2) ),2) as jieg,12 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "                                        select "
							+ "nvl(rzl1.qgrd,0) as qgrd\n"
							+ "                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "                                     )     union"

					);// 拼接查询表12
			ssb
					.append("\n"
							+ "                              (     select "
							+

							"to_char(decode(sum(af),0,0,round_new(sum(af*qnet_ar) / sum(af)*1000, 0))) ||'(约合'|| to_char(decode(sum(af),0,0,round_new(sum(af*qnet_ar) / sum(af) * 7000 / 29.271,0)))||'卡/克)'  as jieg,"
 +
							" 13 as xuh\n"
							+ "                                        from (\n"
							+ "\n"
							+ "                                        select "
							+
							"nvl(rzl1.qnet_ar,0) as qnet_ar\n"
							+ "                                        from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ // 添加机组信息
							"                                          \n"
							+ "\n"
							+ "\n"
							+ "                                         ) fdl,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(rzl1.meil,0)) af\n"
							+ "                                          from rulmzlb rzl1, diancxxb d, jizfzb jfz, jizfzglb jgl, jizb jz\n"
							+ "                                        where rzl1.rulrq = (to_date('"
							+ getBRiq()
							+ "', 'yyyy-mm-dd')-1) \n"
							+ "                                          and d.id = "
							+ getTreeid()
							+ " and rzl1.jizfzb_id=jfz.id\n"
							+ "                                          and jgl.jizfzb_id = jfz.id\n"
							+ "                                          and jgl.jizb_id = jz.id  and   jfz.mingc='"
							+ jizfz
							+ "'"
							+ "\n"
							+ "                                          \n"
							+ "\n"
							+ "\n"
							+ "                                          ) mh\n"
							+ "\n"
							+ "\n"
							+ "         ) order by xuh                                ) a"
							+ i// 增加构建表名

					);// 拼接查询表13

		}
		String ssql2 = "select gethuaybh4zl(zl1.zhilb_id) as bianh,g.mingc as mingc,fh1.daohrq as daohrq from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
				+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id and fh1.diancxxb_id="
				+ getTreeid()
				+ " and fh1.daohrq= (to_date('"
				+ getBRiq()
				+ "','yyyy-mm-dd')-2) \n"
				+" order by bianh";
		

		StringBuffer ssb2 = new StringBuffer();
		ResultSetList rs2 = cn.getResultSetList(ssql2);// 取到所有不同供应商和验收编号信息
		int cun = 0;
		String daohrq="";
		while (rs2.next()) {
//			daohrq=rs2.getString("daohrq");
			String bianh = rs2.getString("bianh");
			String mingc = rs2.getString("mingc");
			sbiao.append("," + mingc + "<br>" + bianh);
			cun++;

			ssb2
					.append(",(\n"
							+ "                                   select to_char(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * mt) / sum(af), 2)\n"
							+ "                                         )) as jieg,1 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.mt,0) as mt\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                         union");// 表1
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * mad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,2 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.mad,0) as mad"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7  and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7  and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                         union");// 表2
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * aad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,3 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.aad,0) as aad\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7  and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7  and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                         union");// 表3
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * ad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,4 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.ad,0) as ad\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7  and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+)  and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表4
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * vad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,5 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.vad,0) as vad"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表5
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * vdaf) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,6 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.vdaf,0) as vdaf\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+)  and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表6
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * fcad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,7 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.fcad,0) as fcad\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+)  and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表7
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * stad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,8 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.stad,0) as stad\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表8
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * std) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,9 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.std,0) as std\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表9
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * had) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,10 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.had,0) as had\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表10
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * qbad) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,11 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.qbad,0) as qbad \n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表11
			ssb2
					.append("\n"
							+ "                                   select Formatxiaosws(decode(\n"
							+ "                                        sum(af),\n"
							+ "                                         0,\n"
							+ "                                         0,\n"
							+ "                                         round_new(sum(af * qgrd) / sum(af), 2)\n"
							+ "                                         ),2) as jieg,12 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.qgrd,0) as qgrd\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                          union");// 表12
			ssb2
					.append("\n"
							+ "                                   select " +
							"to_char(decode(sum(af),0,0,round_new(sum(af*qnet_ar) / sum(af)*1000, 0))) ||'(约合'|| " +
							"to_char(decode(sum(af),0,0,round_new(sum(af*qnet_ar) / sum(af) * 7000 / 29.271,0)))||'卡/克)'  as jieg,"+
//									"decode(\n"
//							+ "                                        sum(af),\n"
//							+ "                                         0,\n"
//							+ "                                         0,\n"
//							+ "                                         round_new(sum(af * qnet_ar) / sum(af), 2)\n"
//							+ "                                         ) as jieg," +
							
									"13 as xuh"
							+ "                                        from (\n"
							+ "\n"
							+ "select nvl(zl1.qnet_ar,0) as qnet_ar\n"
							+
							" from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "                                         ) fdll,\n"
							+ "                                         (\n"
							+ "\n"
							+ "                                         select sum(nvl(fh1.laimsl,0)) af\n"
							+ " from dual,zhillsb zl1,fahb fh1 ,meikxxb g\n"
							+ "   where fh1.zhilb_id = zl1.zhilb_id(+) and zl1.shenhzt=7 and fh1.meikxxb_id=g.id  and fh1.diancxxb_id="
							+ getTreeid()
							+ "\n"
							+ "   and fh1.daohrq=(to_date('"
							+ getBRiq()
							+ "','yyyy-mm-dd')-2) \n"
							+ "   and gethuaybh4zl(zl1.zhilb_id)='"
							+ bianh
							+ "' and g.mingc='"
							+ mingc
							+ "'"
							+ "\n"
							+ "\n"
							+ "                                          ) mhh\n"
							+ "\n" + "\n"
							+ "                                         ) b"
							+ cun// 增加构建表名
					);// 表13

		}
		StringBuffer wheretj = new StringBuffer();
		StringBuffer fromtj = new StringBuffer();
		ssbz.append("select zhib.zhibiao ");
		if (i != 0 || cun != 0) {

			wheretj.append(" where ");// 构造where条件
		}
		fromtj.append(" from  " + ssb1);// 构造from条件
		String jiajianrq=" select  (to_date('"+ getBRiq()+ "','yyyy-mm-dd')-2) as jian2," + "(to_date('"+ getBRiq()+ "','yyyy-mm-dd')-1) as jian1"+ " from dual ";
		ResultSetList rsjj = cn.getResultSetList(jiajianrq);
		while(rsjj.next()){
			rulrq=rsjj.getDateString("jian1");
			daohrq=rsjj.getDateString("jian2");
		}
		for (int a = 0; a < i; a++) {
			// hang[1+a]=50;
			// hangs.append(",");
			sbiao1.append(",入炉煤(取样时间" + rulrq.substring(5).replaceAll("-", ".") + ")");// 入炉煤(取样时间03.10)增加
			ssbz.append(",a" + (a + 1) + ".jieg");
			// hangs.append(50);

			// wheretj.append(" ="+"a"+(a+1)+".xuh ");
			// fromtj.append(", a"+(a+1));
			if (a == 0) {
				wheretj.append(" zhib.xuh  =" + "a" + (a + 1) + ".xuh  ");
			}
			if (a >= 1) {
				wheretj.append(" and zhib.xuh =" + "a" + (a + 1) + ".xuh ");
			}
		}

		for (int b = 0; b < cun; b++) {
			// hang[1+i+b]=50;
			sbiao1.append(",入厂煤(取样时间" + daohrq.substring(5).replaceAll("-", ".") + ")");// 入厂煤(取样时间03.09)
//			System.out.println(sbiao1);
			ssbz.append(",b" + (b + 1) + ".jieg");
			hangs.append(",");
			hangs.append(50);
			// wheretj.append(" zhib.xuh ="+"b"+(b+1)+".xuh ");
			// fromtj.append(", b"+(b+1));
			if (b == 0 && i == 0) {

				wheretj.append(" zhib.xuh  =" + "b" + (b + 1) + ".xuh  ");
			}
			if (b == 0 && i != 0) {

				wheretj.append(" and zhib.xuh  =" + "b" + (b + 1) + ".xuh  ");
			}
			if (b >= 1) {
				wheretj.append(" and zhib.xuh =" + "b" + (b + 1) + ".xuh ");
			}
		}
		// ssbz.append("");
		ssbz.append(fromtj);// 汇总成为总表查询中的from
		// ssbz.append(ssb1);//各个子表
		// if(ssb.length()>1){
       if((i+cun)==0){
    	   return "无此报表";
        }
		ssbz.append(ssb);// 各个子表
		// }
		if (ssb2.length() > 1) {

			ssbz.append(ssb2);// 各个子表
		}
		ssbz.append(wheretj);// 汇总成为总表查询中的where
		ssbz.append(" order by zhib.xuh");
		// File file = new File("\b", ssbz.toString());
		// StringBuffer ss=new StringBuffer();
		// readToBuffer(ss,ssbz);

//		System.out.println(ssbz);
		//		
		// StringBuffer cs = new StringBuffer();
		// cs.append("序号,指标名称,单位,合计,一月份,二月份,三月份,四月份,五月份,六月份,七月份,八月份,九月份,十月份,十一月份,十二月份");
		// StringBuffer cs2 = new StringBuffer();
		// cs2.append("");
		ArrHeader = new String[2][(i + cun + 1)];// 拼接
		ArrHeader[0] = sbiao1.toString().split(",");
		ArrHeader[1] = sbiao.toString().split(",");// 组合拼接
		ArrWidth = new int[(1+i+cun)] ;//770
		for(int c=1;c<=(1+i+cun);c++){
			if(c==1){
				
				ArrWidth[0]=150;
				
			}else{
				ArrWidth[c-1]=70;
			}
		                	   
		}
//		            ArrWidth=new int[]       { 100, 90, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//				70, 70, 50, 50 };
		
		// ArrWidth = hang ;
		
		 iFixedRows = 1;
		 iCol = 10;
		// }
		ResultSetList rs = cn.getResultSetList(ssbz.toString());
		//System.out.println(ssbz.toString());
		String biao = "";
		String sqlq = "select mingc from diancxxb d where id=" + getTreeid();
		ResultSetList rsr = cn.getResultSetList(sqlq);
		try {
			while (rsr.next()) {
				biao = rsr.getString("mingc");
			}
		} finally {
			
		}
		// 数据
		Table tb = new Table(rs, 2, 0, 0);
		rt.setBody(tb);

		rt.setTitle(visit.getDiancmc()+ "煤质日报表", ArrWidth);
		// rt
		// .setDefaultTitle(1, 3, "填报单位:"
		// +biao ,
		// Table.ALIGN_LEFT);

		rt.setDefaultTitle((i+cun), 2, "" + getBRiq().substring(0, 4) + "年"
				+ getBRiq().substring(5, 7) + "月" + getBRiq().substring(8)
				+ "日", Table.ALIGN_LEFT);
		// rt.setDefaultTitle(13, 5, "", Table.ALIGN_RIGHT);//

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = true;
		//设定页面全部居中对齐
			
			int Colshu=rt.body.getCols();
			for(int a=1;a<=Colshu;a++){
				rt.body.setColAlign(a, Table.ALIGN_CENTER);
			}
			
			
		
		//rt.setCenter(true);
//		 arrFormat=new String
//		 []{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
//		 rt.body.setColFormat(arrFormat);
		// 页脚
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		String huayy="";
		String yishen="";
		String ershen="";
		sqlq=
			"select max(huayy) as huayy,max(shenhry) as yishen,max(shenhryej) as erjshen from zhillsb z\n" +
			"where z.huaysj=to_date('"+this.getBRiq()+"','yyyy-mm-dd')\n" + 
			"and z.shenhzt=7";

		rsr = cn.getResultSetList(sqlq);
		if(rsr.next()){
			huayy=rsr.getString("huayy");
			yishen=rsr.getString("yishen");
			ershen=rsr.getString("erjshen");
		}
		
		
		rt.setDefautlFooter(1, (i+cun+1)/3, "批准:"+ershen, Table.ALIGN_LEFT);
		rt.setDefautlFooter((i+cun+1)/3+1, (i+cun+1)/3, "审核:"+yishen, Table.ALIGN_LEFT);
		rt.setDefautlFooter(2*(i+cun+1)/3+1, (i+cun+1)/3, "分析:"+huayy, Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsr.close();
		cn.Close();

		return rt.getAllPagesHtml();
	}

	// public void readToBuffer(StringBuffer buffer, InputStream is)
	// throws IOException {
	// String line; // 用来保存每行读取的内容
	// // InputStream is=null;
	// BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	// line = reader.readLine(); // 读取第一行
	// while (line != null) { // 如果 line 为空说明读完了
	// buffer.append(line); // 将读到的内容添加到 buffer 中
	// buffer.append("\n"); // 添加换行符
	// line = reader.readLine(); // 读取下一行
	// }
	// }
	// 得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  mingc from itemsort where bianm='RANLXYJHZB'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("mingc");
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

	// 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
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
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}

	// 矿报表类型
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "分矿汇总"));
			fahdwList.add(new IDropDownBean(1, "分厂汇总"));
			fahdwList.add(new IDropDownBean(2, "分厂分矿汇总"));

			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	// ***************************报表初始设置***************************//
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	boolean briqchange = false;

	private String briq;

	public String getBRiq() {
		if (briq == null || briq.equals("")) {
			briq = DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {

		if (this.briq != null && !this.briq.equals(briq)) {
			this.briq = briq;
			briqchange = true;
		}

	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		// tb1.addText(new ToolbarText("年份:"));
		// ComboBox nianf = new ComboBox();
		// nianf.setTransform("NIANF");
		// nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		// tb1.addField(nianf);
		// tb1.addText(new ToolbarText("-"));

		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("BRIQ");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

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
				"function(){document.getElementById('RefurbishButton').click();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	// 查询是否设置过滤电厂id
	private String Guoldcid() {
		JDBCcon con = new JDBCcon();
		String dcid = "";
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb where mingc='过滤上海杨树浦发电、上海吴泾热电、上海闵行数据'\n");

		while (rsl.next()) {
			dcid = rsl.getString("zhi");
		}
		con.Close();

		return dcid;
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

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
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
		if (_NianfValue == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
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
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
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

}
