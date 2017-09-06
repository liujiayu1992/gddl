package com.zhiren.dc.monthReport;

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
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 2009-04-20
 * 王磊
 * 01表 供热天然煤耗 计算不正确 应使用gongrl 原使用 fadl
 */
/*
 * 作者：王磊
 * 时间：2009-06-04 15:25
 * 描述：修改01 表中计算公式
 * 		发电标准煤量(24栏) = 指标表.发电煤折标煤量 + 指标表.发电油折标煤量 + 指标表.发电气折标煤量
 * 	  	供热标准煤量(25栏) = 指标表.供热煤折标煤量 + 指标表.供热油折标煤量 + 指标表.供热气折标煤量
 * 		综合燃料发热量(26栏) 燃煤发热量(27栏) 公式中对应上面两栏关系进行修改
 * 		 
 */
/*
 * 作者：张少君
 * 时间：2009-07-20 14：30
 * 描述：修改08 表累计值问题
 * 		 累计值的误差是由加权产生的，现改成在计算“总计”标煤单价时从报表总计项计算得到。
 */
/*
 * 作者：王磊
 * 时间：2009-09-16 9：47
 * 描述：改动上报月报时删除数据不区分厂别的问题
 */
/*
 * 作者：王磊
 * 时间：2009-09-17 
 * 描述：修改月报申请修改为实时上传至分公司
 */
/*
 * 作者：王磊
 * 时间：2009-11-01
 * 描述：修改月报04表进厂量检斤量皆取票重默认检质率100%
 */
/*
 * 作者：王磊
 * 时间：2009-11-03
 * 描述：修改调燃01表机组容量取自jizb sum(jizurl)
 */
/*
 * 作者：张立东
 * 时间：2009-11-04
 * 描述：修改上传大唐国际月指标完成情况的表名：getRenwmc(),zhibwcqkb改成zhibwcqkyb
 */
/*
 * 作者：王磊
 * 时间：2009-11-09
 * 描述：03表的过衡率如果标重为零则检尺率过衡率都为0
 */
/*
 * 作者：王磊
 * 时间：2010-01-15
 * 描述：修改01表油数据修约小数位为保留3位
 */
/*
 * 作者：张少君
 * 时间：2010-05-05
 * 描述：修改08-1表累计到厂综合价、标煤单价、不含税标煤单价算法。
 */
/*
 * 作者：夏峥
 * 时间：2013-05-30
 * 描述：修改getDiaor16()，期初库存累计信息显示为当年1月期初累计信息
 */
public class MonthReport extends BasePage implements PageValidateListener {

	private static final String RT_SL = "yueslb";// 数量

	private static final String RT_HC = "yuehcb";// 耗存

	private static final String RT_ZL = "yuezlb";// 质量

	private static final String RT_DR16 = "diaor16b";

	private static final String RT_DR01 = "diaor01b";

	private static final String RT_DR03 = "diaor03b";

	private static final String RT_DR04 = "diaor04b";

	private static final String RT_DR08 = "diaor08b";

	private static final String RT_DR08ZR = "diaor08bzr";

	private static final String RT_DR08_1 = "diaor08b-1";

	private static final String RT_DR08_1ZR = "diaor08b-1zr";
	
	private static final String RT_DR08_1CW = "diaor08cw";

	private static final String Yueqfmk = "Yueqfmk";

	private static final String SB_Yes = "y";	//上报
	
	private static final String SB_No = "n";	//不上报

	private static final String RT_YUEZBWCQK = "yuezbwcqk";// 月指标完成情况
	
	private static final String ITEM_ONE = "*"; //计划口径合计前添加的符号，最后转化为大写数字序号
	
	private static final String ITEM_TWO = "#"; //统配、地方小计前添加的符号，最后转化为小写数字序号

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	public boolean getRaw() {
		return true;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// ***************设置消息框******************//
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

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(String dcid) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn
				.getResultSetList(" select quanc from diancxxb where id="
						+ dcid);
		if (rs.next()) {
			_TianzdwQuanc = rs.getString("quanc");
		}
		rs.close();
		return _TianzdwQuanc;
	}
	
	private void convertItem(Table tb) {
		String tbCell = "";
		String compareCell = "default"; 
		int t = -1;
		int k = 0;
		int j = 0;
		
		for (int i = 1; i< tb.getRows()-1; i++) {
			tbCell = tb.getCellValue(i, 1);
			t = tbCell.indexOf(ITEM_ONE);
			if (t > -1) {
				//防止连续合并的相同数据累加序号k
				if (!compareCell.equals(tbCell)) k++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, getDxValue(k) + "、" + tbCell.substring(t + 1));
				if (k > 1) j = 0;  //当碰见下一个计划口径时，j从零开始
			}
			t = tbCell.indexOf(ITEM_TWO);
			if (t > -1) {
				if (!compareCell.equals(tbCell)) j++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, j + "、" + tbCell.substring(t + 1));
			}
		}
	}
	
	public String getDxValue(int xuh) {
		String reXuh = "";
		String[] dx = { "", "一", "二", "二", "三", "四", 
				"五", "六", "七", "八", "九", "十" };
		String strXuh = String.valueOf(xuh);
		for (int i = 0; i < strXuh.length(); i++)
			reXuh = reXuh + dx[Integer.parseInt(strXuh.substring(i, i + 1))];

		return reXuh;
	}
	
	public String getPrintTable() {
		if (getReportType().equals(RT_SL)) {
			return getYueslb();
		} else if (getReportType().equals(RT_HC)) {
			return getYuehcb();
		} else if (getReportType().equals(RT_ZL)) {
			return getYuezlb();
		} else if (getReportType().equals(RT_DR01)) {
			return getDiaor01();
		} else if (getReportType().equals(RT_DR04)) {
			return getDiaor04();
		} else if (getReportType().equals(RT_DR16)) {
			return getDiaor16();
		} else if (getReportType().equals(RT_DR03)) {
			return getDiaor03();
		} else if (getReportType().equals(RT_DR08)) {
			return getDiaor08();
		} else if (getReportType().equals(RT_DR08ZR)) {
			return getDiaor08zr();
		} else if (getReportType().equals(RT_DR08_1)) { // 结算表
			return getDiaor08_1();
		}else if (getReportType().equals(RT_DR08_1CW)){  //财务要看的月报
			return getDiaor08_1CW(); 
		}else if (getReportType().equals(RT_DR08_1ZR)) { // 结算表
			return getDiaor08_1zr();
		} else if (getReportType().equals(Yueqfmk)) { // 结算表
			return getyueqfmk();
		} else if (getReportType().equals(RT_YUEZBWCQK)) { // 月指标完成情况
			return getYuezbwcqk();
		} else {
			return "无此报表";
		}
	}

	private String getZhibr() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "否";
		String sql = "select zhi from xitxxb where mingc='月报管理制表人是否默认当前用户' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				zhi = rs.getString("zhi");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		if (zhi.equals("是")) {
			zhibr = visit.getRenymc();
		}
		return zhibr;
	}

	/*
	 * 修改月数量基础表表头为Locale中变量 修改时间：2008-12-05 修改人：王磊
	 */
	private String getYueslb() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		sbsql
				.append("select * from (select gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,yunsfsb.mingc as yunsfsb_id,fenx,\n");
		sbsql.append("jingz,biaoz,yingd,kuid,yuns,koud,kous,\n");
		sbsql
				.append(" kouz,koum,zongkd,sanfsl,jianjl,ructzl,yingdzje,kuidzje,suopsl,suopje from yuetjkjb tj,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n");
		sbsql
				.append("  where tj.id=sl.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id \n");
		sbsql
				.append("  and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and diancxxb_id="
						+ lngDiancId
						+ " and riq=to_date('"
						+ strDate
						+ "','yyyy-mm-dd') order by sl.id )");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[1][22];
		ArrHeader[0] = new String[] { Locale.gongysb_id_fahb,
				Locale.jihkjb_id_fahb, Locale.pinzb_id_fahb,
				Locale.yunsfsb_id_fahb, Locale.MRtp_fenx, Locale.laimsl_fahb,
				Locale.biaoz_fahb, Locale.yingd_fahb, Locale.kuid_fahb,
				Locale.yuns_fahb, Locale.koud_fahb, Locale.kous_fahb,
				Locale.kouz_fahb, Locale.koum_fahb, Locale.zongkd_fahb,
				Locale.sanfsl_fahb, Locale.MRtp_jianjl, Locale.MRtp_ructzl,
				Locale.MRtp_yingdzje, Locale.MRtp_kuidzje, Locale.MRtp_suopsl,
				Locale.MRtp_suopje };
		// 列宽
		int ArrWidth[] = new int[] { 80, 60, 40, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50, 65, 65, 65, 50, 50 };
		// if(this.isA4parper()){
		// ArrWidth=new int[]
		// {70,30,20,50,50,50,50,30,30,30,50,50,50,50,50,50,50,50,50};
		// }
		//		
		// int e=0;
		// for(int i=0;i<ArrWidth.length;i++){
		// e+=ArrWidth[i];
		// }
		// System.out.println(e);
		// 设置页标题
		rt.setTitle("月数量统计表", ArrWidth);
		rt.setDefaultTitle(9, 2, strMonth, Table.ALIGN_CENTER);

		// 数据
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);

		// 页脚
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * 修改月耗存基础表表头为Locale中变量 修改时间：2008-12-05 修改人：王磊
	 */
	private String getYuehcb() { // 耗存表
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		sbsql
				.append("select gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,yunsfsb.mingc as yunsfsb_id,fenx,\n");
		sbsql.append("fady,gongry,qith,sunh,diaocl,panyk,kuc　\n");
		sbsql
				.append("  from yuetjkjb tj,yuehcb hc,gongysb,jihkjb,pinzb,yunsfsb\n");
		sbsql
				.append("  where tj.id=hc.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id \n");
		sbsql
				.append("  and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and diancxxb_id="
						+ lngDiancId
						+ " and riq=to_date('"
						+ strDate
						+ "','yyyy-mm-dd')");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { Locale.gongysb_id_fahb,
				Locale.jihkjb_id_fahb, Locale.pinzb_id_fahb,
				Locale.yunsfsb_id_fahb, Locale.MRtp_fenx, Locale.fady,
				Locale.gongry, Locale.qity, Locale.cuns, Locale.diaocl,
				Locale.panyk, Locale.kuc };
		// 列宽
		int ArrWidth[] = new int[] { 80, 60, 50, 60, 60, 60, 60, 60, 60, 60,
				60, 60 };

		// 设置页标题
		rt.setTitle("月耗存统计表", ArrWidth);
		rt.setDefaultTitle(6, 2, strMonth, Table.ALIGN_CENTER);

		// 数据
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);

		// 页脚
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getYuezlb() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		sbsql
				.append("select yz.gongysb_id,yz.jihkjb_id,yz.pinzb_id,yz.yunsfsb_id,yz.fenx,yz.qnet_ar,yz.aar,yz.ad,yz.vdaf,yz.mt,yz.stad,yz.aad,yz.mad,yz.qbad,yz.had,yz.vad,yz.fcad,yz.std,yz.qbrad,");
		sbsql
				.append(" yz.hdaf,yz.qgrad_daf,yz.sdaf,yz.var,yz.qnet_ar_kf,yz.aar_kf,yz.ad_kf,yz.vdaf_kf,yz.mt_kf,yz.stad_kf,yz.aad_kf,");
		sbsql
				.append(" yz.mad_kf,yz.qbad_kf,yz.had_kf,yz.vad_kf,yz.fcad_kf,yz.std_kf,");
		sbsql
				.append(" yz.qbrad_kf,yz.hdaf_kf,yz.qgrad_daf_kf,yz.sdaf_kf,yz.var_kf,yz.zhijbfml,yz.zhijbfje,yz.suopje,yz.lsuopsl,yz.lsuopje from (select yz.id, gy.mingc as gongysb_id,jh.mingc as jihkjb_id,pz.mingc as pinzb_id,ys.mingc as yunsfsb_id,");
		sbsql
				.append(" yz.fenx,yz.qnet_ar,yz.aar,yz.ad,yz.vdaf,yz.mt,yz.stad,yz.aad,yz.mad,yz.qbad,yz.had,yz.vad,yz.fcad,yz.std,yz.qbrad,");
		sbsql
				.append(" yz.hdaf,yz.qgrad_daf,yz.sdaf,yz.var,yz.qnet_ar_kf,yz.aar_kf,yz.ad_kf,yz.vdaf_kf,yz.mt_kf,yz.stad_kf,yz.aad_kf,");
		sbsql
				.append(" yz.mad_kf,yz.qbad_kf,yz.had_kf,yz.vad_kf,yz.fcad_kf,yz.std_kf,");
		sbsql
				.append(" yz.qbrad_kf,yz.hdaf_kf,yz.qgrad_daf_kf,yz.sdaf_kf,yz.var_kf,yz.zhijbfml,yz.zhijbfje,yz.suopje,yz.lsuopsl,yz.lsuopje\n");
		sbsql
				.append(" from yuezlb yz,yuetjkjb yj,gongysb gy,jihkjb jh,pinzb pz,yunsfsb ys");
		sbsql
				.append(" where yz.yuetjkjb_id=yj.id and yj.gongysb_id=gy.id and yj.jihkjb_id=jh.id and yj.pinzb_id=pz.id");
		sbsql.append(" and yj.yunsfsb_id=ys.id and yj.diancxxb_id="
				+ lngDiancId + " and yj.riq=to_date('" + strDate
				+ "','yyyy-mm-dd') order by yz.id)yz");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[2][46];
		ArrHeader[0] = new String[] { "供货单位", "计划<br>口径", "品种", "运输<br>方式",
				"分项", "厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验",
				"厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验", "厂内化验",
				"厂内化验", "厂内化验", "厂内化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验",
				"矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验",
				"矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验",
				"质价<br>不符<br>煤量<br>(吨)", "质价<br>不符<br>金额<br>(元)",
				"索赔<br>金额<br>(元)", "高硫<br>索赔<br>数量<br>(吨)",
				"高硫<br>索赔<br>金额<br>(元)" };
		ArrHeader[1] = new String[] { "供货单位", "计划<br>口径", "品种", "运输<br>方式",
				"分项", "收到基<br>低位<br>热值<br>Qnet,ar(Mj/kg)", "收到基<br>灰分<br>Aar",
				"干燥基<br>灰分<br>Ad", "干燥<br>无灰基<br>挥发分<br>Vdaf", "全水<br>Mt",
				"空气<br>干燥基<br>硫<br>St,ad", "空气<br>干燥基<br>灰分<br>Aad",
				"空气<br>干燥基<br>水分<br>Mad", "弹筒<br>热值", "空气<br>干燥基<br>氢<br>Had",
				"空气<br>干燥基<br>挥发分<br>Vad", "固定碳", "干燥基<br>硫<br>St,d",
				"空气<br>干燥基<br>高位热<br>Qbr,ad", "干燥<br>无灰基<br>氢<br>Hdaf",
				"干燥<br>无灰基<br>高位热<br>Qbr,daf", "干燥<br>无灰基<br>硫<br>St,daf",
				"收到基<br>挥发份<br>Var", "收到基<br>低位<br>热值<br>Qnet,ar(Mj/kg)",
				"收到基<br>灰分<br>Aar", "干燥基<br>灰分<br>Ad",
				"干燥<br>无灰基<br>挥发分<br>Vdaf", "全水<br>Mt",
				"空气<br>干燥基<br>硫<br>St,ad", "空气<br>干燥基<br>灰分<br>Aad",
				"空气<br>干燥基<br>水分<br>Mad", "弹筒<br>热值", "空气<br>干燥基<br>氢<br>Had",
				"空气<br>干燥基<br>挥发分<br>Vad", "固定碳", "干燥基<br>硫<br>St,d",
				"空气<br>干燥基<br>高位热<br>Qbr,ad", "干燥<br>无灰基<br>氢<br>Hdaf",
				"干燥<br>无灰基<br>高位热<br>Qbr,daf", "干燥<br>无灰基<br>硫<br>St,daf",
				"收到基<br>挥发份<br>Var", "质价<br>不符<br>煤量<br>(吨)",
				"质价<br>不符<br>金额<br>(元)", "索赔<br>金额<br>(元)",
				"高硫<br>索赔<br>数量<br>(吨)", "高硫<br>索赔<br>金额<br>(元)" };
		// 列宽
		int ArrWidth[] = new int[] { 100, 50, 40, 40, 40, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50 };

		// 设置页标题
		rt.setTitle("月质量统计表", ArrWidth);
		rt.setDefaultTitle(20, 2, strMonth, Table.ALIGN_CENTER);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		// 页脚
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * 将原修约函数round改为round_new 热值小数位取系统设置 修改时间：2008-12-05 修改人：王磊
	 */
	/*
	 * 作者：赵胜男
	 * 日期：2013-05-10
	 * 描述：增加水分差调整一列
	 */
	 
	private String getDiaor16() {
//		Visit v = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql = "select --decode(gongysb.mingc, null, '总计', gongysb.mingc) as kuangb,\n" + 
			
			"DECODE(GROUPING(S.TJKJ),\n" +
			"              1,\n" + 
			"              '总计',\n" + 
			"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
			"                     1,\n" + 
			"                     '*' || S.TJKJ,\n" + 
			"                     DECODE(GROUPING(S.QUANC),\n" + 
			"                            1,\n" + 
			"                            '#' || DECODE(JIHKJB.MINGC,\n" + 
			"                                          '市场采购',\n" + 
			"                                          '地方矿',\n" + 
			"                                          JIHKJB.MINGC) || '小计',\n" + 
			"                            DECODE(GROUPING(S.DQMC),\n" + 
			"                                   1,\n" + 
			"                                   '<I>' || S.QUANC || '</I>',\n" + 
			"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
			"                                          1,\n" + 
			"                                          S.DQMC || '小计',\n" + 
			"                                          GONGYSB.MINGC))))) AS KUANGB," 
			
				+ "       decode(pinzb.mingc,null,'_',pinzb.mingc) as pinz,\n"
				+ "       nvl(s.fenx,'') fenx,\n"
				+ "       nvl(sum(s.qickc), 0) as shangykc,\n"
				+ "     SUM(JINCML) AS JINML,\n" 
				+ "       sum(yuns) as yuns,\n"
				+ "       sum(kuid) kuid,\n" +
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * QNET_AR) / SUM(JINCML), 2)) AS QNET_AR,\n" + 
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * AAD) / SUM(JINCML), 2)) AS AAD,\n" + 
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * MT) / SUM(JINCML), 2)) AS Mar,\n" + 
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * Vdaf) / SUM(JINCML), 2)) AS Vdaf,\n"
				+ "       sum(s.hej) as hej,\n"
				+ "       sum(s.fady) as fady,\n"
				+ "       sum(s.gongry) as gongry,\n"
				+ "       sum(s.qith) as qith,\n"
				+ "       sum(s.sunh) as sunh,\n"
				+ "       sum(s.diaocl) as diaocl,\n"
				+ "       sum(s.panyk) as panyk,\n"
				+ " SUM(decode(diancxxb_id,324,s.shuifctz*0.4,s.shuifctz)) AS shuifctz,\n"
				+ "       sum(s.kuc) as kuc\n"
				+ "  from ("
				+ getBaseSql(false)
				+ ") s,\n"
				+ "       gongysb, JIHKJB,\n"
				+ "       pinzb\n"
				+ " where s.gongysb_id = gongysb.id and s.pinzb_id = pinzb.id and gongysb.leix = 0 \n"
				+ "   AND S.JIHKJB_ID = JIHKJB.ID\n" 
				+ "group by rollup(s.fenx, S.TJKJ,JIHKJB.MINGC,S.QUANC,S.DQMC, gongysb.mingc, pinzb.mingc)\n"
				+ "having not(GROUPING(GONGYSB.MINGC) + GROUPING(PINZB.MINGC) = 1 OR GROUPING(FENX) = 1)\n"
//				+ "order by grouping(gongysb.mingc) desc,gongysb.mingc,grouping(pinzb.mingc) desc,pinzb.mingc,s.fenx"
				+ "ORDER BY S.TJKJ DESC,JIHKJB.MINGC DESC,S.QUANC DESC, GROUPING(S.DQMC) DESC,S.DQMC,GONGYSB.MINGC DESC,s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		// String ArrHeader[][];
		// int ArrWidth[];
		String ArrHeader[][] = new String[4][19];
		ArrHeader[0] = new String[] { "矿别", "品种", "本月<br>及<br>累计", "月初结存量",
				"实际供应情况", "实际供应情况", "实际供应情况", "实际供应情况", "实际供应情况", "实际供应情况",
				"实际供应情况", "实际用量", "实际用量", "实际用量", "实际用量", "实际用量", "调出量", "盘盈亏","水分差调整",
				"月末结存量" };
		ArrHeader[1] = new String[] { "矿别", "品种", "本月<br>及<br>累计", "月初结存量",
				"矿方供应量", "矿方供应量", "矿方供应量", "发热量", "灰分％", "水分％", "挥发分％", "合计",
				"发电", "供热", "其它", "储存损耗", "调出量", "盘盈亏","水分差调整", "月末结存量" };
		ArrHeader[2] = new String[] { "矿别", "品种", "本月<br>及<br>累计", "月初结存量",
				"合计", "运输损耗", "亏吨", "发热量", "灰分％", "水分％", "挥发分％", "合计", "发电",
				"供热", "其它", "储存损耗", "调出量", "盘盈亏","水分差调整", "月末结存量" };
		ArrHeader[3] = new String[] { "甲", "乙", "丙", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16","17" };

		int ArrWidth[] = new int[] {120, 40, 40, 59, 59, 50, 50, 50, 50, 50,
				50, 55, 55, 55, 55, 55, 55, 55, 55,55 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("生产用煤炭供应、耗用与结存月报", ArrWidth);
		Visit visit = ((Visit) getPage().getVisit());
		String _Danwqc = getTianzdwQuanc(getTreeid());
		if (_Danwqc.equals("北京大唐燃料有限公司") && visit.getRenyjb() == 2) {
			_Danwqc = "大唐国际发电股份有限公司燃料管理部";
		}
		rt.setDefaultTitle(1, 5, "填报单位:" + _Danwqc, Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "电生16-1表",
				Table.ALIGN_RIGHT);

		// 设置页面
		rt.setMarginBottom(rt.getMarginBottom() + 25);
		// 数据
		rt.setBody(new Table(rs, 4, 0, 3));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");

		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.getPages();

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		
		rt.body.ShowZero = reportShowZero();
		
		// 添加数子序号
		convertItem(rt.body);
		
		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(6, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(11, 2, "制表:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(15, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.getPages();

		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
		// return rt.getAllPapersHtml(0);
	}

	private String getDiaor01() {
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql = "select d.mingc, s.jjizrl, s.fenx, s.biaoz, s.mhxj, s.mfady, s.mgongry,"
				+ "s.mqith, s.msunh, s.mkuc, s.shouyl, s.yhxj, s.fadyy, s.gongryy, s.qithy, s.sunhy, s.kucy,"
				+ "s.FADL, s.GONGRL, s.FADBZMH, s.GONGRBZMH, s.fadtrmh, s.gongrtrmh, s.FADBZML, s.GONGRBZML,"
				+ "s.zonghrlfrl, s.ranmrlfrl"
				+ " from ("
				+ getBaseSql(false)
				+ ") s, diancxxb d where s.diancxxb_id = d.id order by s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[4][27];
		ArrHeader[0] = new String[] { "单位<br>名称", "发电<br>设备<br>容量",
				"本月<br>或<br>累计", "煤炭(吨)", "煤炭(吨)", "煤炭(吨)", "煤炭(吨)", "煤炭(吨)",
				"煤炭(吨)", "煤炭(吨)", "石油(吨)", "石油(吨)", "石油(吨)", "石油(吨)", "石油(吨)",
				"石油(吨)", "石油(吨)", "实际完成", "实际完成", "煤耗率", "煤耗率", "煤耗率", "煤耗率",
				"标准煤量", "标准煤量", "发热量", "发热量" };
		ArrHeader[1] = new String[] { "单位<br>名称", "发电<br>设备<br>容量",
				"本月<br>或<br>累计", "实供", "耗用", "耗用", "耗用", "耗用", "耗用", "库存",
				"实供", "耗用", "耗用", "耗用", "耗用", "耗用", "库存", "发电量<br>(万千瓦时)",
				"供热量<br>(吉焦)", "标准煤耗率", "标准煤耗率", "天然煤耗率", "天然煤耗率",
				"发电用<br>(吨)", "供热用<br>(吨)", " 综合燃料<br>(兆焦/千克)",
				"纯然煤<br>(兆焦/千克)" };
		ArrHeader[2] = new String[] { "单位<br>名称", "发电<br>设备<br>容量",
				"本月<br>或<br>累计", "实供", "小计", "发电", "供热", "其它", "损耗", "库存",
				"实供", "小计", "发电", "供热", "其它", "损耗", "库存", "发电量<br>(万千瓦时)",
				"供热量<br>(吉焦)", "发电<br>(克/千瓦时)", "供热<br>(千克/吉焦)",
				"发电<br>(克/千瓦时)", "供热<br>(千克/吉焦)", "发电用<br>(吨)", "供热用<br>(吨)",
				" 综合燃料<br>(兆焦/千克)", "纯然煤<br>(兆焦/千克)" };
		ArrHeader[3] = new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
				"19", "20", "21", "22", "23", "24", "25", "26", "27" };

		ArrWidth = new int[] { 65, 33, 33, 43, 43, 40, 33, 33, 33, 33, 33, 33,
				33, 33, 33, 33, 33, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("生产用煤炭供应、耗用与结存汇总表", ArrWidth);
		rt.setDefaultTitle(1, 7, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 3, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃01表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 4, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		// rt.body.mergeFixedRow();
		rt.body.ShowZero = reportShowZero();
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setRowCells(1, Table.PER_FONTSIZE, 8);
		rt.body.setRowCells(2, Table.PER_FONTSIZE, 8);
		rt.body.setRowCells(3, Table.PER_FONTSIZE, 8);
		for (int i = 1; i <= 27; i++) {
			rt.body.setColCells(i, Table.PER_FONTSIZE, 8);
		}
		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(14, 2, "分管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(19, 2, "制表:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(23, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * 将原修约函数round改为round_new 修改时间：2008-12-05 修改人：王磊
	 */
	private String getDiaor03() {
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql =

		" select decode(grouping(g.mingc), 1, '总计', g.mingc) as 煤矿名称,\n"
				+ "       s.fenx as fenx,\n"
				+ "       sum(s.jincl),\n"
				+ "       sum(s.jianjl) as guohl,\n"
				+ "       decode(sum(s.jincl),0,0,round_new(sum(s.jianjl) / sum(s.jincl) * 100, 2)) as 百分比,\n"
				+ "       decode(sum(s.jincl),0,0,round_new(sum(s.jianjl) / sum(s.jincl) * 100, 2)) as guoh,\n"
				+ "       decode(sum(s.jincl),0,0,100 -  decode(sum(s.jincl),0,0,round_new(sum(s.jianjl) / sum(s.jincl) * 100, 2))) as jianc,\n"
				+ "       sum(nvl(s.yingd, 0)) as yind,\n"
				+ "       sum(nvl(s.yingdzje, 0)) as yingdje,\n"
				+ "       sum(nvl(s.kuid,0)) as kuid,\n"
				+ "       sum(nvl(s.kuidzje, 0)) as kuidje,\n"
				+ "       sum(nvl(suopsl, 0)) as suopsl,\n"
				+ "       sum(nvl(suopje, 0)) as suopje,\n"
				+ "       '' as shuom\n"
				+ "from ("
				+ getBaseSql(false)
				+ ")s, gongysb g\n"
				+ "   where s.gongysb_id = g.id\n"
				+ "   group by rollup(s.fenx, g.mingc) having(grouping(s.fenx) = 0)\n"
				+ "order by grouping(g.mingc) desc, g.mingc, max(g.xuh), grouping(s.fenx) desc, s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[4][14];
		ArrHeader[0] = new String[] { "单位", "本月<br>或<br>累计", "进厂数量<br>(吨)",
				"         进厂抽查数量", "         进厂抽查数量", "         进厂抽查数量",
				"         进厂抽查数量", "盈亏情况", "盈亏情况", "盈亏情况", "盈亏情况",
				"索赔数量<br>(吨)", "索赔金额<br>(元)", "说明" };
		ArrHeader[1] = new String[] { "单位", "本月<br>或<br>累计", "进厂数量<br>(吨)",
				"      数量", "      数量", "      其中", "      其中", "盈煤<br>(吨)",
				"盈煤金额<br>(元)", "亏吨<br>(吨)", "亏吨金额<br>(元)", "索赔数量<br>(吨)",
				"索赔金额<br>(元)", "说明" };
		ArrHeader[2] = new String[] { "单位", "本月<br>或<br>累计", "进厂数量<br>(吨)",
				"(吨)", "%", "过衡%", "检尺%", "盈煤<br>(吨)", "盈煤金额<br>(元)",
				"亏吨<br>(吨)", "亏吨金额<br>(元)", "索赔数量<br>(吨)", "索赔金额<br>(元)", "说明" };
		ArrHeader[3] = new String[] { "甲", "乙", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12" };
		ArrWidth = new int[] { 100, 40, 60, 60, 60, 60, 60, 60, 80, 60, 80, 80,
				80, 120 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// int e=0;
		// for(int i=0;i<ArrWidth.length;i++){
		// e+=ArrWidth[i];
		// }
		// System.out.println(e);
		// 设置页标题
		rt.setTitle("进厂煤计量盈亏月报表", ArrWidth);
		rt.setDefaultTitle(1, 5, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃03表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 4, 0, 1));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.getPages();
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.mergeFixedCol(1);

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(5, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(9, 2, "制表:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols(), 1, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * 将原修约函数round改为round_new 全水及热值小数位取系统设置 修改时间：2008-12-05 修改人：王磊
	 */
	private String getDiaor04() {
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";
		String sql = "select decode(grouping(g.mingc),1,'总计',g.mingc) as 供货单位,\n"
				+ " decode(grouping(p.mingc ),1,'- ',p.mingc )as 品种,\n"
				+ " s.fenx as 本月或累计,\n"
				+ " nvl(sum(s.jincl),0) as 进厂煤量,nvl(sum(s.jincl),0) as 验收煤量,\n"
				+ " nvl(round_new(decode(sum(s.jincl),0,0,(sum(s.jincl) / sum(s.jincl)) * 100), 2),0) as 检质率,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2)),0) as QNET_AR,\n"
				+ " nvl(dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2))),0) as 等级,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.mt_kf*s.jincl)/sum(s.jincl),1)),0) as MT_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.aar_kf*s.jincl)/sum(s.jincl),2)),0) as AAR_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.vdaf_kf*s.jincl)/sum(s.jincl),2)),0) as VDAF_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.std_kf*s.jincl)/sum(s.jincl),2)),0) as STD_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2)),0) as QNET_AR,\n"
				+ " nvl(dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2))),0) as 等级,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.mt*s.jincl)/sum(s.jincl),1)),0) as MT,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.aar*s.jincl)/sum(s.jincl),2)),0) as AAR,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.vdaf*s.jincl)/sum(s.jincl),2)),0) as VDAF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.std*s.jincl)/sum(s.jincl),2)),0) as STD,\n"
				+ " nvl(\n"
				+ " dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2)))-\n"
				+ " dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2))),0) as 等级差,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2))\n"
				+ "   -decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2)),0) as 热值差,\n"
				+ "\n"
				+ " decode(sum(s.jincl),0,0,round_new(sum(s.jincl*s.zhijbflv)/sum(s.jincl),2)) as 百分比,\n"
				+ " decode(sum(s.jincl*s.zhijbflv/100),0,0,round_new(sum(s.zhijbfje)/sum(s.jincl*s.zhijbflv/100),2)) as 单价差,\n"
				+ " sum(s.zhijbfje) as 总金额,sum(s.suopje) as 索赔金额,\n"
				+ " sum(s.lsuopsl) as 硫索赔数量,sum(s.lsuopje) as 硫索赔金额\n"
				+ " from\n"
				+ "( "
				+ getBaseSql(false)
				+ ") s,gongysb g, pinzb p\n"
				+ "where s.gongysb_id = g.id and s.pinzb_id = p.id\n"
				+ " group by rollup(s.fenx,g.mingc,p.mingc)\n"
				+ " having(grouping(s.fenx) = 0) and not(grouping(g.mingc)=0 and grouping(p.mingc)=1)\n"
				+ " order by grouping(g.mingc) desc, g.mingc,p.mingc,s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[3][26];
		ArrHeader[0] = new String[] { "矿别", "品种", "本月<br>或<br>累计", "验收数量",
				"验收数量", "验收数量", "矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验", "矿方化验",
				"厂方化验", "厂方化验", "厂方化验", "厂方化验", "厂方化验", "厂方化验", "等级差", "热值差",
				"质价不符情况", "质价不符情况", "质价不符情况", "索赔金额<br>(元)", "高硫索赔", "高硫索赔" };

		ArrHeader[1] = new String[] { "矿别", "品种", "本月<br>或<br>累计",
				"进厂<br>煤量<br>(吨)", "验收<br>煤量<br>(吨)", "检质<br>率%",
				"Qnet,<br>ar(MJ<br>/Kg)", "等级", "Mt%", "Aar%", "Vdaf%",
				"St,d%", "Qnet,<br>ar(MJ<br>/Kg)", "等级", "Mt%", "Aar%",
				"Vdaf%", "St,d%", "等级", "Qnet,<br>ar(MJ<br>/Kg)", "%",
				"单价差<br>(元/吨)", "总金额<br>(元)", "索赔金额<br>(元)", "数量<br>(吨)",
				"金额<br>(元)" };
		ArrHeader[2] = new String[] { "甲", "乙", "丙", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
				"17", "18", "19", "20", "21", "22", "23" };

		ArrWidth = new int[] { 85, 40, 38, 45, 45, 30, 35, 30, 30, 30, 30, 30,
				30, 30, 30, 30, 30, 30, 40, 40, 30, 60, 75, 75, 45, 50 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("进厂煤发热量计价煤质验收情况月报", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃04表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 3, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.ShowZero = reportShowZero();
		if (rt.body.getRows() > rt.body.getFixedRows()) {
			rt.body.mergeFixedCol(2);
		}
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.0");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.0");
		rt.body.setColFormat(20, "0.00");
		rt.body.setColFormat(21, "0.00");
		rt.body.setColFormat(23, "0.00");
		rt.body.setColFormat(24, "0.00");
		rt.body.setColFormat(25, "");
		rt.body.setColFormat(26, "0.00");
		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(11, 2, "分管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 2, "制表:" + getZhibr(), Table.ALIGN_CENTER);
		rt.setDefautlFooter(22, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	Report rt_zhibb = null;

	/*
	 * 08表 开发人：张少君 日期：2009-04-13
	 */
	private String getDiaor08() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql =

		" select  mingc,fenx,laiml,decode(mingc,'总计',kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy,daoczhj) as daoczhj,\n"
				+ "		 kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,\n"
				+ "        syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
				+ "		 decode(mingc,'总计',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy)*29.271/qnet_ar,2),bmdj) as bmdj,\n"
				+ "		 decode(mingc,'总计',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy-kjs-tlyfs-syfs-qyfs)*29.271/qnet_ar,2),bhsbmj) as bhsbmj\n"
				+ "        from (\n"
				+ "	select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
				+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n"
				+ "       sl.fenx,\n"
				+ "       t.gongysb_id,\n"
				+ "       sum(sl.biaoz) as laiml,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum((j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz)/sum(sl.biaoz),2)) as daoczhj,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as daoczhj,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.meij*sl.biaoz)/sum(sl.biaoz),2)) as kj,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meij*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as  kj,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.meijs*sl.biaoz)/sum(sl.biaoz),2)) as kjs,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meijs*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as kjs,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.jiaohqzf*sl.biaoz)/sum(sl.biaoz),2)) as jhqyzf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.jiaohqzf*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as jhqyzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0))/sum(sl.biaoz),2)) as tlyf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0))/sum(sl.biaoz),2)) as tlyfs, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyfs,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0))/sum(sl.biaoz),2)) as tlzf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0))/sum(sl.biaoz),2)) as syf, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0)))	\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0))/sum(sl.biaoz),2)) as syfs, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syfs,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0))/sum(sl.biaoz),2)) as szf, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as szf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0))/sum(sl.biaoz),2)) as qyf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0))/sum(sl.biaoz),2)) as qyfs,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.daozzf*sl.biaoz)/sum(sl.biaoz),2)) as dzzf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.daozzf*sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as dzzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.qit*sl.biaoz)/sum(sl.biaoz),2)) as qtfy,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qit*sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qtfy,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.qnet_ar*sl.biaoz)/sum(sl.biaoz),2)) as qnet_ar,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qnet_ar * sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qnet_ar,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.biaomdj*sl.biaoz)/sum(sl.biaoz),2)) as bmdj,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.biaomdj * sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bmdj,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.buhsbmdj*sl.biaoz)/sum(sl.biaoz),2)) as bhsbmj	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.buhsbmdj * sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bhsbmj\n"
				+ "\n"
				+ "  from yuercbmdj j,yueslb sl,yuetjkjb t,vwgongys gys\n"
				+ " where j.yuetjkjb_id = t.id\n"
				+ "   and sl.yuetjkjb_id=t.id\n"
				+ "   and j.yuetjkjb_id=sl.yuetjkjb_id\n"
				+ "   and t.gongysb_id = gys.id\n"
				+ "   and j.fenx=sl.fenx\n"
				+ "   and t.riq = to_date('"
				+ strDate
				+ "', 'yyyy-mm-dd')\n"
				+ "   and (t.diancxxb_id = "
				+ getTreeid()
				+ " or t.diancxxb_id in(select id from diancxxb where fuid="
				+ getTreeid()
				+ "))\n"
				+ " group by rollup(sl.fenx,gys.mingc,t.gongysb_id)\n"
				+ "\n"
				+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
				+ "            or grouping(sl.fenx)=1)\n"
				+ " order by xuh,sl.fenx)";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][21];
		ArrHeader[0] = new String[] { "供应商", "本月<br>或<br>累计", "煤量(吨)",
				"到厂<br>综合价<br>(元/吨)", "矿价<br>(元/吨)", "增值<br>税额<br>(元/吨)",
				"交货前<br>运杂费<br>(元/吨)", "铁路<br>运费<br>(元/吨)", "铁路<br>税额<br>(元/吨)",
				"铁路<br>杂费<br>(元/吨)", "水运费<br>(元/吨)", "水运<br>税额<br>(元/吨)",
				"水运<br>杂费<br>(元/吨)", "汽运费<br>(元/吨)", "汽运<br>税额<br>(元/吨)",
				"港杂费<br>(元/吨)", "到站<br>杂费<br>(元/吨)", "其他<br>费用<br>(元/吨)",
				"热值<br>Mj/kg", "标煤单价<br>含税<br>(元/吨)", "标煤单价<br>不含税<br>(元/吨)" };
		ArrHeader[1] = new String[] { "甲", "乙", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19" };

		ArrWidth = new int[] { 95, 40, 55, 47, 47, 38, 47, 47, 47, 47, 47, 47,
				47, 47, 47, 47, 47, 47, 47, 47, 47 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("电煤价格情况表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃08表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		rt.body.setColFormat(20, "0.000");
		rt.body.setColFormat(21, "0.00");

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "分管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "制表:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiaor08zr() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql =

		" select  mingc,fenx,laiml,decode(mingc,'总计',kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy,daoczhj) as daoczhj,\n"
				+ "		 kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
				+ "		 decode(mingc,'总计',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy)*29.271/qnet_ar,2),bmdj) as bmdj,\n"
				+ "		 decode(mingc,'总计',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy-kjs-tlyfs-syfs-qyfs)*29.271/qnet_ar,2),bhsbmj) as bhsbmj\n"
				+ "        from (\n"
				+ "	select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
				+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n"
				+ "       sl.fenx,\n"
				+ "       t.gongysb_id,\n"
				+ "       sum(sl.biaoz) as laiml,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as daoczhj,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meij*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as  kj,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meijs*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as kjs,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.jiaohqzf*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as jhqyzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyfs,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syfs,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as szf,\n"
				+ "\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.daozzf*sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as dzzf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qit*sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qtfy,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qnet_ar * sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qnet_ar,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.biaomdj * sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bmdj,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.buhsbmdj * sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bhsbmj\n"
				+ "\n"
				+ "  from yuercbmdj j,yueslb sl,yuetjkjb t,vwgongys gys\n"
				+ " where j.yuetjkjb_id = t.id\n"
				+ "   and sl.yuetjkjb_id=t.id\n"
				+ "   and j.yuetjkjb_id=sl.yuetjkjb_id\n"
				+ "   and t.gongysb_id = gys.id\n"
				+ "   and j.fenx=sl.fenx\n"
				+ "   and t.riq = to_date('"
				+ strDate
				+ "', 'yyyy-mm-dd')\n"
				+ "   and (t.diancxxb_id = "
				+ getTreeid()
				+ " or t.diancxxb_id in(select id from diancxxb where fuid="
				+ getTreeid()
				+ "))\n"
				+ " group by rollup(sl.fenx,gys.mingc,t.gongysb_id)\n"
				+ "\n"
				+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
				+ "            or grouping(sl.fenx)=1)\n"
				+ " order by xuh,sl.fenx)";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][21];
		ArrHeader[0] = new String[] { "供应商", "本月<br>或<br>累计", "煤量(吨)",
				"到厂<br>综合价<br>(元/吨)", "矿价<br>(元/吨)", "增值税额<br>(元/吨)",
				"交货前<br>运杂费<br>(元/吨)", "铁路运费<br>(元/吨)", "铁路税额<br>(元/吨)",
				"铁路杂费<br>(元/吨)", "汽运费<br>(元/吨)", "汽运税额<br>(元/吨)",
				"港杂费<br>(元/吨)", "到站杂费<br>(元/吨)", "其他费用<br>(元/吨)",
				"热值<br>Mj/kg", "标煤单价<br>含税<br>(元/吨)", "标煤单价<br>不含税<br>(元/吨)" };
		ArrHeader[1] = new String[] { "甲", "乙", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19" };

		ArrWidth = new int[] { 100, 40, 60, 50, 50, 40, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("电煤价格情况表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃08表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.000");
		rt.body.setColFormat(18, "0.00");

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "分管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "制表:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

//	private void ArrWidth(int i) {
//		// TODO 自动生成方法存根
//	}

	private String getYuezbwcqk() {
		JDBCcon con = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		String yuejsbmdj = "yuercbmdj";
		ResultSet rs = con
				.getResultSet("SELECT ZHI FROM XITXXB WHERE MINGC = '月指标完成情况'");
		try {
			if (rs.next()) {
				yuejsbmdj = rs.getString("ZHI");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String Yearm = this.getNianfValue().getId() + YUEF;
		String Yearm_tq = (this.getNianfValue().getId() - 1) + YUEF;
		// 本月本期
		String Wdanw = "10000";
		String sql1 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')='"
				+ Yearm
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')='"
				+ Yearm
				+ "'and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2) jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--进厂天然煤量\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--进厂煤发热量\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--进厂天然煤单价\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--进厂标煤单价\n"
				+ "sum(s.yingd-s.kuid)yingk,--进厂煤盈亏吨\n"
				+ "sum(s.suopje)suopje,--亏吨索赔金额\n"
				+ "sum(z.suopje)relspje,--热量索赔金额\n"
				+ "sum(z.lsuopje)lsuopje--高硫索赔金额\n"
				+ "from (select * from yueslb where fenx='本月') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='本月') z,\n"
				+ "(select * from "
				+ yuejsbmdj
				+ " where fenx='本月') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--入炉煤发热量\n"
				+ "yuezbb.faddwrlcb,--单位燃料成本\n" + "yuezbb.fadl,--发电量\n"
				+ "yuezbb.gongrl,--供热量\n" + "yuezbb.shoudl,--上网电量\n"
				+ "yuezbb.gongdbzmh,--供电煤耗\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--耗用天然煤量\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--耗用标煤量\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm + "' and yuezbb.fenx='本月'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_bb = con.getResultSetList(sql1);
		// 本月同期
		String sql2 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')='"
				+ Yearm_tq
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')='"
				+ Yearm_tq
				+ "'and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2)jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--进厂天然煤量\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--进厂煤发热量\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--进厂天然煤单价\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--进厂标煤单价\n"
				+ "sum(s.yingd-s.kuid)yingk,--进厂煤盈亏吨\n"
				+ "sum(s.suopje)suopje,--亏吨索赔金额\n"
				+ "sum(z.suopje)relspje,--热量索赔金额\n"
				+ "sum(z.lsuopje)lsuopje--高硫索赔金额\n"
				+ "from (select * from yueslb where fenx='本月') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='本月') z,\n"
				+ "(select * from yuercbmdj where fenx='本月') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm_tq
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--入炉煤发热量\n"
				+ "yuezbb.faddwrlcb,--单位燃料成本\n" + "yuezbb.fadl,--发电量\n"
				+ "yuezbb.gongrl,--供热量\n" + "yuezbb.shoudl,--上网电量\n"
				+ "yuezbb.gongdbzmh,--供电煤耗\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--耗用天然煤量\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--耗用标煤量\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm_tq + "' and yuezbb.fenx='本月'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_bt = con.getResultSetList(sql2);
		// 累计本期
		String sql3 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')<='"
				+ Yearm
				+ "'and  to_char(jiesb.ruzrq,'yyyy')='"
				+ this.getNianfValue().getId()
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')<='"
				+ Yearm
				+ "'and  to_char(jiesyfb.ruzrq,'yyyy')='"
				+ this.getNianfValue().getId()
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2)jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--进厂天然煤量\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--进厂煤发热量\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--进厂天然煤单价\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--进厂标煤单价\n"
				+ "sum(s.yingd-s.kuid)yingk,--进厂煤盈亏吨\n"
				+ "sum(s.suopje)suopje,--亏吨索赔金额\n"
				+ "sum(z.suopje)relspje,--热量索赔金额\n"
				+ "sum(z.lsuopje)lsuopje--高硫索赔金额\n"
				+ "from (select * from yueslb where fenx='累计') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='累计') z,\n"
				+ "(select * from "
				+ yuejsbmdj
				+ " where fenx='累计') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--入炉煤发热量\n"
				+ "yuezbb.faddwrlcb,--单位燃料成本\n" + "yuezbb.fadl,--发电量\n"
				+ "yuezbb.gongrl,--供热量\n" + "yuezbb.shoudl,--上网电量\n"
				+ "yuezbb.gongdbzmh,--供电煤耗\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--耗用天然煤量\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--耗用标煤量\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm + "' and yuezbb.fenx='累计'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_lb = con.getResultSetList(sql3);
		// 累计同期
		String sql4 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')<='"
				+ Yearm_tq
				+ "'and  to_char(jiesb.ruzrq,'yyyy')='"
				+ (this.getNianfValue().getId() - 1)
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')<='"
				+ Yearm_tq
				+ "'and  to_char(jiesyfb.ruzrq,'yyyy')='"
				+ (this.getNianfValue().getId() - 1)
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2)jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--进厂天然煤量\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--进厂煤发热量\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--进厂天然煤单价\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--进厂标煤单价\n"
				+ "sum(s.yingd-s.kuid)yingk,--进厂煤盈亏吨\n"
				+ "sum(s.suopje)suopje,--亏吨索赔金额\n"
				+ "sum(z.suopje)relspje,--热量索赔金额\n"
				+ "sum(z.lsuopje)lsuopje--高硫索赔金额\n"
				+ "from (select * from yueslb where fenx='累计') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='累计') z,\n"
				+ "(select * from yuercbmdj where fenx='累计') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm_tq
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--入炉煤发热量\n"
				+ "yuezbb.faddwrlcb,--单位燃料成本\n" + "yuezbb.fadl,--发电量\n"
				+ "yuezbb.gongrl,--供热量\n" + "yuezbb.shoudl,--上网电量\n"
				+ "yuezbb.gongdbzmh,--供电煤耗\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--耗用天然煤量\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--耗用标煤量\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm_tq + "' and yuezbb.fenx='累计'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_lt = con.getResultSetList(sql4);
		Report rt = new Report();
		rt_zhibb = rt;
		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[1][7];
		ArrHeader[0] = new String[] { "指标名称", "单位", "当日或累计", "本期", "同期",
				"变化情况", "备注" };
		ArrWidth = new int[] { 100, 100, 80, 80, 80, 80, 100 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		// 设置页标题
		rt.setTitle("发电厂燃料管理指标完成情况统计", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, "日期:" + this.getNianfValue().getId() + "年"
				+ YUEF + "月", Table.ALIGN_LEFT);
		rt.title.setRowHeight(40);
		// 数据
		rt.setBody(new Table(35, 7));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(40, this.paperStyle));
		//
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = reportShowZero();
		// 填写数据
		// 填写横标题
		rt.body.setCellValue(1, 1, "指标名称");
		rt.body.setCellValue(1, 2, "单位");
		rt.body.setCellValue(1, 3, "当日或累计");
		rt.body.setCellValue(1, 4, "本期");
		rt.body.setCellValue(1, 5, "同期");
		rt.body.setCellValue(1, 6, "变化情况");
		rt.body.setCellValue(1, 7, "备注");
		// 填写竖标题
		rt.body.setCellValue(2, 1, "进厂天然煤量");
		rt.body.setCellValue(3, 1, "进厂天然煤量");
		rt.body.setCellValue(4, 1, "进厂煤发热量");
		rt.body.setCellValue(5, 1, "进厂煤发热量");
		rt.body.setCellValue(6, 1, "入炉煤发热量");
		rt.body.setCellValue(7, 1, "入炉煤发热量");
		rt.body.setCellValue(8, 1, "进厂天然煤单价");
		rt.body.setCellValue(9, 1, "进厂天然煤单价");
		rt.body.setCellValue(10, 1, "进厂标煤单价");
		rt.body.setCellValue(11, 1, "进厂标煤单价");
		rt.body.setCellValue(12, 1, "单位燃料成本");
		rt.body.setCellValue(13, 1, "单位燃料成本");
		rt.body.setCellValue(14, 1, "发电量");
		rt.body.setCellValue(15, 1, "发电量");
		rt.body.setCellValue(16, 1, "供热量");
		rt.body.setCellValue(17, 1, "供热量");
		rt.body.setCellValue(18, 1, "上网电量");
		rt.body.setCellValue(19, 1, "上网电量");
		rt.body.setCellValue(20, 1, "供电煤耗");
		rt.body.setCellValue(21, 1, "供电煤耗");
		rt.body.setCellValue(22, 1, "耗用天然煤量");
		rt.body.setCellValue(23, 1, "耗用天然煤量");
		rt.body.setCellValue(24, 1, "耗用标煤量");
		rt.body.setCellValue(25, 1, "耗用标煤量");
		rt.body.setCellValue(26, 1, "进厂煤盈亏吨");
		rt.body.setCellValue(27, 1, "进厂煤盈亏吨");
		rt.body.setCellValue(28, 1, "亏吨索赔金额");
		rt.body.setCellValue(29, 1, "亏吨索赔金额");
		rt.body.setCellValue(30, 1, "亏卡索赔金额");
		rt.body.setCellValue(31, 1, "亏卡索赔金额");
		rt.body.setCellValue(32, 1, "超硫索赔金额");
		rt.body.setCellValue(33, 1, "超硫索赔金额");
		rt.body.setCellValue(34, 1, "燃料结算金额");
		rt.body.setCellValue(35, 1, "燃料结算金额");
		//
		rt.body.setCellValue(2, 2, "万吨");
		rt.body.setCellValue(3, 2, "万吨");
		rt.body.setCellValue(4, 2, "Mj/kg");
		rt.body.setCellValue(5, 2, "Mj/kg");
		rt.body.setCellValue(6, 2, "Mj/kg");
		rt.body.setCellValue(7, 2, "Mj/kg");
		rt.body.setCellValue(8, 2, "元/吨");
		rt.body.setCellValue(9, 2, "元/吨");
		rt.body.setCellValue(10, 2, "元/吨");
		rt.body.setCellValue(11, 2, "元/吨");
		rt.body.setCellValue(12, 2, "元/千千瓦时");
		rt.body.setCellValue(13, 2, "元/千千瓦时");
		rt.body.setCellValue(14, 2, "万千瓦时");
		rt.body.setCellValue(15, 2, "万千瓦时");
		rt.body.setCellValue(16, 2, "十亿焦耳");
		rt.body.setCellValue(17, 2, "十亿焦耳");
		rt.body.setCellValue(18, 2, "万千瓦时");
		rt.body.setCellValue(19, 2, "万千瓦时");
		rt.body.setCellValue(20, 2, "g/kwh");
		rt.body.setCellValue(21, 2, "g/kwh");
		rt.body.setCellValue(22, 2, "万吨");
		rt.body.setCellValue(23, 2, "万吨");
		rt.body.setCellValue(24, 2, "万吨");
		rt.body.setCellValue(25, 2, "万吨");
		rt.body.setCellValue(26, 2, "吨");
		rt.body.setCellValue(27, 2, "吨");
		rt.body.setCellValue(28, 2, "万元");
		rt.body.setCellValue(29, 2, "万元");
		rt.body.setCellValue(30, 2, "万元");
		rt.body.setCellValue(31, 2, "万元");
		rt.body.setCellValue(32, 2, "万元");
		rt.body.setCellValue(33, 2, "万元");
		rt.body.setCellValue(34, 2, "万元");
		rt.body.setCellValue(35, 2, "万元");
		// 填写值
		int k1 = 0, k2 = 0;
		// if(rs_bb.next()){
		// k++;
		// }
		rs_bb.next();
		rs_bt.next();
		rs_lb.next();
		rs_lt.next();
		for (int i = 2; i < 36; i++) {
			// 当月或累计
			if (i % 2 == 0) {// 偶数
				rt.body.setCellValue(i, 3, "本月");
				// 本期第4列
				rt.body.setCellValue(i, 4, rs_bb.getString(k1));
				// 同期第5列
				rt.body.setCellValue(i, 5, rs_bt.getString(k1));
				// 变化情况6列
				rt.body.setCellValue(i, 6, String
						.valueOf((rs_bb.getString(k1) == null) ? 0 : rs_bb
								.getDouble(k1)
								- ((rs_bt.getString(k1) == null) ? 0 : rs_bt
										.getDouble(k1))));

				k1++;
			} else {
				rt.body.setCellValue(i, 3, "累计");
				// 本期第4列
				rt.body.setCellValue(i, 4, rs_lb.getString(k2));
				// 同期第5列
				rt.body.setCellValue(i, 5, rs_lt.getString(k2));
				// 变化情况6列
				rt.body.setCellValue(i, 6, String
						.valueOf((rs_lb.getString(k2) == null) ? 0 : rs_lb
								.getDouble(k2)
								- ((rs_lt.getString(k2) == null) ? 0 : rs_lt
										.getDouble(k2))));
				k2++;
			}
		}
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		// rt.body.setRowCells(1, Table.ALIGN_RIGHT, 1);
		// 页脚
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(3, 2, "制表:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols(), 1, Table.PAGENUMBER_CHINA,
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

	/*
	 * 08-1表 开发人：张少君 日期：2009-04-10
	 */
	private String getDiaor08_1() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql = "select mingc,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj,\n"
				+ "        		kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar, \n"
				+ "				decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj,	\n"
				+ "				decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk	\n"
				+ "         from (\n"
				+ "select decode(gongysb.mingc,null,0,avg(gongysb.xuh)) as xuh,\n" + 
//				+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n" 
				
				"DECODE(GROUPING(J.TJKJ),\n" +
				"              1,\n" + 
				"              '总计',\n" + 
				"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
				"                     1,\n" + 
				"                     '*' || J.TJKJ,\n" + 
				"                     DECODE(GROUPING(J.QUANC),\n" + 
				"                            1,\n" + 
				"                            '#' || decode(JIHKJB.MINGC,'市场采购','地方矿',JIHKJB.MINGC) || '小计',\n" + 
				"                            DECODE(GROUPING(J.DQMC),\n" + 
				"                                   1,\n" + 
				"                                   '<I>'||J.QUANC||'</I>',\n" + 
				"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
				"                                          1,\n" + 
				"                                          J.DQMC || '小计',\n" + 
				"                                          GONGYSB.MINGC))))) AS MINGC," 
				
				+ "       j.fenx,\n"
				+ "       --t.gongysb_id,\n"
				+ "       sum(j.jiesl) as jsl,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum((j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl)/sum(j.jiesl),2)) as daoczhj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.meij*j.jiesl)/sum(j.jiesl),2)) as kj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.meijs*j.jiesl)/sum(j.jiesl),2)) as kjs,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.kuangqyf*j.jiesl)/sum(j.jiesl),2)) as jhqyzf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,1,j.yunj*j.jiesl,0))/sum(j.jiesl),2)) as tlyf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,1,j.yunjs*j.jiesl,0))/sum(j.jiesl),2)) as tlyfs, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,1,j.zaf*j.jiesl,0))/sum(j.jiesl),2)) as tlzf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,3,j.yunj*j.jiesl,0))/sum(j.jiesl),2)) as syf, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,3,j.yunjs*jiesl,0))/sum(j.jiesl),2)) as syfs, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,3,j.zaf*j.jiesl,0))/sum(j.jiesl),2)) as szf, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,2,j.yunj*j.jiesl,0))/sum(j.jiesl),2)) as qyf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,2,j.yunj*j.jiesl,0))/sum(j.jiesl),2)* (select nvl(max(zhi),0.07) from xitxxb where xitxxb.mingc='运费税率')) as qyfs,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2)* (select nvl(max(zhi),0.07) from xitxxb where xitxxb.mingc='运费税率') as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.daozzf*j.jiesl)/sum(j.jiesl),2)) as dzzf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.qit*j.jiesl)/sum(j.jiesl),2)) as qtfy,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.qnet_ar*j.jiesl)/sum(j.jiesl),2)) as qnet_ar,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.biaomdj*j.jiesl)/sum(j.jiesl),2)) as bmdj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.buhsbmdj*j.jiesl)/sum(j.jiesl),2)) as bhsbmj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
				+ "       round(sum(j.meij * j.jiesl)/10000,2) mk\n" + 

				"from (SELECT j.*,\n" +
				"               to_date('" + strDate + "','yyyy-mm-dd') riq, tj.diancxxb_id, tj.gongysb_id,\n" + 
				"               tj.DQID,\n" + 
				"               tj.DQMC,\n" + 
				"               SF.QUANC QUANC,\n" + 
				"               DECODE(tj.JIHKJB_ID, 1, '重点订货', 3, '重点订货', kj.MINGC) TJKJ,\n" + 
				"               tj.JIHKJB_ID,\n" + 
				"               yunsfsb_id\n" + 
				"          FROM YUEJSBMDJ_DK j, (select yuetjkjb.id,yunsfsb_id, gongysb_id, diancxxb_id, '本月' as fenx, JIHKJB_ID, DQID, DQMC\n" + 
				"                        from yuetjkjb , VWGONGYSDQ DQ\n" + 
				"                       where riq = to_date('" + strDate + "','yyyy-mm-dd')\n" + 
				"                         AND YUETJKJB.GONGYSB_ID = DQ.ID\n" + 
				"                         and diancxxb_id = " + getTreeid() + "\n" + 
				"                      union\n" + 
				"                      select yuetjkjb.id,yunsfsb_id, gongysb_id, diancxxb_id, '累计' as fenx, JIHKJB_ID, DQID, DQMC\n" + 
				"                        from yuetjkjb, VWGONGYSDQ DQ\n" + 
				"                       where riq = to_date('" + strDate + "','yyyy-mm-dd')\n" + 
				"                         AND YUETJKJB.GONGYSB_ID = DQ.ID\n" + 
				"                         and diancxxb_id = " + getTreeid() + ") tj,\n" + 
				"       SHENGFB SF, MEIKDQB DQ, jihkjb kj\n" + 
				" where j.yuetjkjb_id(+) = tj.id\n" + 
				"   and tj.fenx = j.fenx(+)\n" + 
				"   AND tj.JIHKJB_ID = kj.ID\n" + 
				"   AND tj.DQID = DQ.ID\n" + 
				"   AND DQ.SHENGFB_ID = SF.ID) j, gongysb, jihkjb\n" + 
				" WHERE j.gongysb_id = gongysb.id and gongysb.leix = 0\n" + 
				"   AND j.JIHKJB_ID = JIHKJB.ID\n" + 
				" group by rollup(j.fenx, j.TJKJ,jihkjb.MINGC,j.QUANC, j.DQMC, gongysb.mingc)\n" + 
				" having not (grouping(j.fenx)=1)\n" + 
				" order by j.TJKJ DESC,jihkjb.MINGC DESC,j.QUANC DESC, GROUPING(j.DQMC) DESC,j.DQMC,gongysb.MINGC DESC,j.fenx,xuh)";


		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][22];
		ArrHeader[0] = new String[] { "供应商", "本月<br>或<br>累计", "煤量(吨)",
				"到厂<br>综合价<br>(元/吨)", "矿价<br>(元/吨)", "增值<br>税额<br>(元/吨)",
				"交货前<br>运杂费<br>(元/吨)", "铁路<br>运费<br>(元/吨)", "铁路<br>税额<br>(元/吨)",
				"铁路<br>杂费<br>(元/吨)", "水运费<br>(元/吨)", "水运<br>税额<br>(元/吨)",
				"水运<br>杂费<br>(元/吨)", "汽运费<br>(元/吨)", "汽运<br>税额<br>(元/吨)",
				"港杂费<br>(元/吨)", "到站<br>杂费<br>(元/吨)", "其他<br>费用<br>(元/吨)",
				"热值<br>Mj/kg", "标煤单价<br>含税<br>(元/吨)", "标煤单价<br>不含税<br>(元/吨)",
				"煤款<br>(万元)" };
		ArrHeader[1] = new String[] { "甲", "乙", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20" };

		ArrWidth = new int[] {120, 40, 55, 47, 47, 40, 45, 45, 45, 45, 45, 45,
				47, 47, 47, 47, 47, 47, 47, 47, 47, 57 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		
		// 设置页标题
		rt.setTitle("电煤价格情况表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃08-1表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		
		rt.body.ShowZero = reportShowZero();
		
		convertItem(rt.body);

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		rt.body.setColFormat(20, "0.000");
		rt.body.setColFormat(21, "0.00");
		rt.body.setColFormat(22, "0.00");
		// rt.body.setColFormat(23,"0.00");

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiaor08_1zr() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql = "select mingc,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj, \n"
				+ " 		kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar, \n"
				+ "         decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj, \n" 
				+ " 		decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk\n"
				+ "         from (\n"
				+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
				+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n"
				+ "       j.fenx,\n"
				+ "       t.gongysb_id,\n"
				+ "       sum(j.jiesl) as jsl,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
				+ "\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
				+ "       round(sum(j.meij * j.jiesl)/10000,2) mk\n"
				+ "  from yuejsbmdj j, yuetjkjb t, vwgongys gys\n"
				+ " where j.yuetjkjb_id = t.id\n"
				+ "   and t.gongysb_id = gys.id\n"
				+ "   and t.riq = to_date('"
				+ strDate
				+ "', 'yyyy-mm-dd')\n"
				+ "   and t.diancxxb_id = "
				+ getTreeid()
				+ "\n"
				+ " group by rollup(fenx,gys.mingc,t.gongysb_id)\n"
				+ "\n"
				+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
				+ "            or grouping(fenx)=1)\n"
				+ " order by xuh,fenx  )\n";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][19];
		ArrHeader[0] = new String[] { "供应商", "本月<br>或<br>累计", "煤量(吨)",
				"到厂<br>综合价<br>(元/吨)", "矿价<br>(元/吨)", "增值税额<br>(元/吨)",
				"交货前<br>运杂费<br>(元/吨)", "铁路运费<br>(元/吨)", "铁路税额<br>(元/吨)",
				"铁路杂费<br>(元/吨)", "汽运费<br>(元/吨)", "汽运税额<br>(元/吨)",
				"港杂费<br>(元/吨)", "到站杂费<br>(元/吨)", "其他费用<br>(元/吨)",
				"热值<br>Mj/kg", "标煤单价<br>含税<br>(元/吨)", "标煤单价<br>不含税<br>(元/吨)",
				"煤款<br>(万元)" };
		ArrHeader[1] = new String[] { "甲", "乙", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17" };

		ArrWidth = new int[] { 100, 40, 60, 50, 50, 40, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 60 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("电煤价格情况表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃08-1表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		// rt.body.setColFormat(23,"0.00");

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiaor08_1CW() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql = "select  mingc,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj, \n" 
			+ "			kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar, \n"
			+ "         decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj, \n" 
			+ " 		decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk,sun,round(sl,2) \n"
			+ "         from (\n"
			+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
			+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n"
			+ "       j.fenx,\n"
			+ "       t.gongysb_id,\n"
			+ "       sum(j.jiesl) as jsl,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
			+ "\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
			+ "\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
			+ "\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyfs,\n"
			+ "       0 gangzf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
			+ "       round(sum(j.meij * j.jiesl),2) mk,\n"
			+ "		sum(j.sunhzje) sun, \n"
			+ " 	(sum(j.sunhzje)/(sum(j.meij * j.jiesl)))* 100  sl \n"
			+ "  from yuejsbmdjcw j, yuetjkjb t, vwgongys gys\n"
			+ " where j.yuetjkjb_id = t.id\n"
			+ "   and t.gongysb_id = gys.id\n"
			+ "   and t.riq = to_date('"
			+ strDate
			+ "', 'yyyy-mm-dd')\n"
			+ "   and t.diancxxb_id = "
			+ getTreeid()
			+ "\n"
			+ " group by rollup(fenx,gys.mingc,t.gongysb_id)\n"
			+ "\n"
			+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
			+ "            or grouping(fenx)=1)\n"
			+ " order by xuh,fenx  )\n";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][19];
		ArrHeader[0] = new String[] { "供应商", "本月<br>或<br>累计", "煤量(吨)",
				"到厂<br>综合价<br>(元/吨)", "矿价<br>(元/吨)", "增值税额<br>(元/吨)",
				"交货前<br>运杂费<br>(元/吨)", "铁路运费<br>(元/吨)", "铁路税额<br>(元/吨)",
				"铁路杂费<br>(元/吨)", "汽运费<br>(元/吨)", "汽运税额<br>(元/吨)",
				"港杂费<br>(元/吨)", "到站杂费<br>(元/吨)", "其他费用<br>(元/吨)",
				"热值<br>Mj/kg", "标煤单价<br>含税<br>(元/吨)", "标煤单价<br>不含税<br>(元/吨)",
				"煤款<br>(万元)","损耗折金额<br>(元)","损耗率<br>(%)" };
		ArrHeader[1] = new String[] { "甲", "乙", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17","18","19" };

		ArrWidth = new int[] { 100, 40, 60, 50, 50, 40, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 80 ,50,50};
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// 设置页标题
		rt.setTitle("电煤价格情况表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "调燃08-1表",
				Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		rt.body.setColFormat(20, "0.00");
		rt.body.setColFormat(21, "0.00");
		// rt.body.setColFormat(23,"0.00");

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	private String getyueqfmk() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		JDBCcon con = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		Report rt = new Report();
		String rowTitleSql = "select decode(grouping(g.dqmc),1,'合计',g.dqmc) gys,q.fenx\n"
				+ "from vwgongys g,yueqfmkb q\n"
				+ "where q.gongysb_id = g.dqid\n"
				+ "group by rollup(q.fenx,g.dqmc) having not grouping(q.fenx) = 1\n"
				+ "order by grouping(g.dqmc) desc,max(g.xuh),q.fenx";

		String colTitleSql = "";

		String bodySql = "select decode(grouping(d.mingc),1,'合计',d.mingc) dianc,\n"
				+ "decode(grouping(g.dqmc),1,'合计',g.dqmc) gys,\n"
				+ "decode(grouping(q.fenx),1,'合计',q.fenx) fenx,\n"
				+ "sum(q.meil) \"煤量(吨)\",sum(q.qiank) \"欠款(万元)\"\n"
				+ " from yueqfmkb q,(select id, mingc, fuid, level as jib\n"
				+ "from diancxxb start with id= "
				+ getTreeid()
				+ "\n"
				+ "connect by fuid = prior id\n"
				+ "order SIBLINGS by xuh ) d, vwgongys g\n"
				+ "where q.gongysb_id = g.dqid and q.diancxxb_id = d.id\n"
				+ "and q.riq="
				+ strOraDate
				+ "\n"
				+ "group by cube(d.mingc,g.dqmc,q.fenx)";

		int jib = getJibbyDCID(con, getTreeid());
		if (jib == 3) {
			colTitleSql = "select d.mingc dianc from vwdianc d where d.id ="
					+ getTreeid();
		} else if (jib == 2) {
			colTitleSql = "select decode(grouping(d.mingc),1,'合计',d.mingc) dianc\n"
					+ "from vwdianc d\n"
					+ "where d.fuid = "
					+ getTreeid()
					+ "\n"
					+ "group by rollup(d.mingc) order by grouping(d.mingc) desc,max(d.xuh)";
		} else if (jib == 1) {
			colTitleSql = "select decode(grouping(d.mingc),1,'合计',d.mingc) dianc "
					+ "from diancxxb d where d.fuid ="
					+ getTreeid()
					+ "\n"
					+ "group by rollup(d.mingc) order by grouping(d.mingc) desc,max(d.xuh)";

			bodySql = "select decode(grouping(d.mingc),1,'合计',d.mingc) dianc,\n"
					+ "decode(grouping(g.dqmc),1,'合计',g.dqmc) gys,\n"
					+ "decode(grouping(q.fenx),1,'合计',q.fenx) fenx,\n"
					+ "sum(q.meil) \"煤量(吨)\",sum(q.qiank) \"欠款(万元)\"\n"
					+ " from yueqfmkb q,(select id, mingc, fuid, level as jib\n"
					+ "from diancxxb start with id= "
					+ getTreeid()
					+ "\n"
					+ "connect by fuid = prior id\n"
					+ "order SIBLINGS by xuh ) d, vwgongys g, vwdianc vd\n"
					+ "where q.gongysb_id = g.dqid and q.diancxxb_id = vd.id\n"
					+ "and vd.fuid = d.id\n"
					+ "and q.riq="
					+ strOraDate
					+ "\n"
					+ "group by cube(d.mingc,g.dqmc,q.fenx)";

		}
		ResultSetList rsl = con.getResultSetList(colTitleSql);
		int ArrWidth[] = new int[rsl.getRows() * 2 + 2];
		ArrWidth[0] = 100;
		ArrWidth[1] = 50;
		for (int i = 2; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		cd.setRowNames("gys,fenx");
		cd.setColNames("dianc");
		cd.setDataNames("煤量(吨),欠款(万元)");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(rowTitleSql, colTitleSql, bodySql);

		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;

		rt.setTitle("月欠付煤款信息", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		// rt.setDefaultTitle(1, 2, "单位:万吨",Table.ALIGN_LEFT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _SbClick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbClick = true;
	}

	private boolean _SqxgClick = false;

	public void SqxgButton(IRequestCycle cycle) {
		_SqxgClick = true;
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SbClick) {
			_SbClick = false;
			Shangb();
			getSelectData();
		}
		if (_SqxgClick) {
			_SqxgClick = false;
			Shenqxg();
			getSelectData();
		}
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
			getSelectData();
		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (cycle.getRequestContext().getParameter("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameter("lx"));
			init();
		}
		if (cycle.getRequestContext().getParameter("sb") != null) {
			visit.setString2(cycle.getRequestContext().getParameter("sb"));
		}
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			if (visit.getString1() == null || "".equals(visit.getString1())) {
				visit.setString1(RT_DR16);
			}
			if (visit.getString2() == null || "".equals(visit.getString2())) {
				visit.setString2(SB_No);
			}
			init();
		}
	}

	private void init() {
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel10(null);
		visit.setDropDownBean10(null);
		visit.setProSelectionModel3(null);
		visit.setDropDownBean3(null);
		visit.setDefaultTree(null);
		setDiancmcModel(null);
		setTreeid(visit.getDiancxxb_id() + "");
		paperStyle();
		getSelectData();
	}

	private String getMokmc() {
		if (getReportType().equals(RT_SL)) {
			return "";// SysConstant;
		} else if (getReportType().equals(RT_HC)) {
			return "";// getYuehcb();
		} else if (getReportType().equals(RT_ZL)) {
			return "";// getYuezlb();
		} else if (getReportType().equals(RT_DR01)) {
			return SysConstant.Diaor01b;
		} else if (getReportType().equals(RT_DR04)) {
			return SysConstant.Diaor04b;
		} else if (getReportType().equals(RT_DR16)) {
			return SysConstant.Diaor16b;
		} else if (getReportType().equals(RT_DR03)) {
			return SysConstant.Diaor03b;
		} else if (getReportType().equals(RT_DR08_1)) {
			return SysConstant.Diaor08b;
		} else if (getReportType().equals(RT_DR08)) {
			return "调燃08_1表";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			return "月指标表";
		} else {
			return "";
		}
	}

	private String getBaseSql(boolean id) {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		String newid = "";
		if (id) {
			newid = "getnewid(" + getTreeid() + ") id,";
		}
		if (getReportType().equals(RT_DR16)) {
			return "select "
					+ newid
					+ strOraDate
					+ " riq,z.diancxxb_id,z.gongysb_id,\n"
					+ "       Z.DQID,\n" 
					+ "       Z.DQMC,\n" 
					+ "       SHENGFB.QUANC QUANC,\n" 
					+ "       DECODE(Z.JIHKJB_ID, 1, '重点订货', 3, '重点订货', J.MINGC) TJKJ,\n" 
					+ "       Z.JIHKJB_ID,\n" 
					+ "       SL.JINGZ+sl.yuns AS JINCML,\n" 
					+ "       z.pinzb_id,yunsfsb_id,\n"
					+ "       decode(zl.FENX,'本月',HC.QICKC,GETQICKCLJ(z.ID))QICKC,\n"
					+ "       nvl(sl.biaoz, 0) as biaoz,\n"
					+ "       sl.yuns as yuns,\n"
					+ "       sl.kuid - sl.yingd as kuid,\n"
					+ "       nvl(zl.qnet_ar, 0) as farl,\n"
					+ "       nvl(zl.aad, 0) as huif,\n"
					+ "       nvl(zl.mt, 0) as shuif,\n"
					+ "       nvl(zl.vdaf, 0) as huiff,\n"
					+ "       hc.fady + hc.gongry + hc.qith + hc.sunh as hej,\n"
					+ "       hc.fady,\n"
					+ "       hc.gongry,\n"
					+ "       hc.qith,\n"
					+ "       hc.sunh,\n"
					+ "       hc.diaocl,\n"
					+ "       hc.panyk,\n"
					+ "       hc.shuifctz,\n"
					+ "		  ZL.*," 
					+ "       kuc\n"
					+ "  from (select * from yueslb) sl,\n"
					+ "       (select * from yuehcb) hc,\n"
					+ "       (select * from yuezlb) zl,\n"
					+ "       (select yuetjkjb.id, gongysb_id, pinzb_id, yunsfsb_id, diancxxb_id, '本月' as fenx, JIHKJB_ID, DQID, DQMC\n"
					+ "          from yuetjkjb, VWGONGYSDQ DQ\n"
					+ "         where riq = "
					+ strOraDate
					+ "\n"
					+ " AND YUETJKJB.GONGYSB_ID = DQ.ID"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ "or diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid()
					+ "))\n"
					+ "        union\n"
					+ "        select yuetjkjb.id, gongysb_id, pinzb_id, yunsfsb_id, diancxxb_id, '累计' as fenx, JIHKJB_ID, DQID, DQMC\n"
					+ "          from yuetjkjb, VWGONGYSDQ DQ\n" + "         where riq = "
					+ strOraDate + "\n" 
					+ " AND YUETJKJB.GONGYSB_ID = DQ.ID"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ "or diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid() + "))) z,\n" 
					+"       JIHKJB J,\n" 
					+ "       MEIKDQB,\n" 
					+ "       SHENGFB\n" 
					+ " where z.id = sl.yuetjkjb_id(+)\n"
					+ "   and z.fenx = sl.fenx(+)\n"
					+ "   and z.id = hc.yuetjkjb_id(+)\n"
					+ "   and z.fenx = hc.fenx(+)\n"
					+ "   and z.id = zl.yuetjkjb_id(+)\n" 
					+ "   AND Z.JIHKJB_ID = J.ID\n" 
					+ "   AND Z.DQID = MEIKDQB.ID\n" 
					+ "   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID"
					+ "   and z.fenx = zl.fenx(+)";
		} else if (getReportType().equals(RT_DR01)) {

			return "select "
					+ newid
					+ strOraDate
					+ " riq,"
					+ getTreeid()
					+ " as diancxxb_id,\n"
					+ "       jz.jizurl jjizrl,\n"
					+ "       nvl(fx.fenx, '') fenx,\n"
					+ "       sl.biaoz,\n"
					+ "       (hc.fady + hc.gongry + hc.qith + hc.sunh) mhxj,\n"
					+ "       hc.fady mfady,\n"
					+ "       hc.gongry mgongry,\n"
					+ "       hc.qith mqith,\n"
					+ "       hc.sunh msunh,\n"
					+ "       hc.kuc mkuc,\n"
					+ "       yo.shouyl,\n"
					+ "       (yo.fadyy + yo.gongry + yo.qithy + yo.sunh) yhxj,\n"
					+ "       yo.fadyy,\n"
					+ "       yo.gongry gongryy,\n"
					+ "       yo.qithy,\n"
					+ "       yo.sunh sunhy,\n"
					+ "       yo.kuc kucy,\n"
					+ "       zb.FADL,\n"
					+ "       zb.GONGRL,\n"
					+ "       zb.FADBZMH,\n"
					+ "       zb.GONGRBZMH,\n"
					+ "       decode(nvl(zb.fadl,0), 0, 0, round_new((nvl(yo.fadyy,0) * 2 + nvl(hc.fady,0))*100 / zb.fadl, 0)) fadtrmh,\n"
					+ "       decode(nvl(zb.GONGRL,0),0,0,round_new((nvl(yo.gongry,0) * 2 + nvl(hc.gongry,0)) *1000/ zb.GONGRL, 0)) gongrtrmh,\n"
					+ "       (nvl(zb.FADMZBML,0)+ nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0)) FADBZML ,\n"
					+ "       (nvl(zb.GONGRMZBML,0)+ nvl(zb.GONGRYZBZML,0)+ nvl(zb.GONGRQZBZML,0)) GONGRBZML,\n"
					+ "       decode((nvl(yo.fadyy,0) * 2 + nvl(hc.fady,0)),0,0,\n"
					+ "              round_new(nvl(nvl(zb.FADMZBML,0)+ nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0),0) * 7000 * 0.0041816 /(nvl(yo.fadyy,0) * 2 + nvl(hc.fady,0)),2)) zonghrlfrl,\n"
					+ "       decode(nvl(hc.fady,0),0,0,round_new((nvl(nvl(zb.FADMZBML,0)+ nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0),0) * 7000 - nvl(yo.fadyy,0) * 10000) * 0.0041816 / nvl(hc.fady,0), 2)) ranmrlfrl\n"
					+ "  from (select sum(biaoz) biaoz, fenx\n"
					+ "          from yueslb s, yuetjkjb t\n"
					+ "         where s.yuetjkjb_id = t.id\n"
					+ "           and t.riq = "
					+ strOraDate
					+ "\n"
					+ "           and (t.diancxxb_id = "
					+ getTreeid()
					+ " or t.diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid()
					+ "))\n"
					+ "         group by fenx) sl,\n"
					+ "       (select riq,fenx,sum(qickc) qickc,sum(shouml) shouml,sum(fady) fady,sum(gongry) gongry,sum(qith) qith,sum(sunh) sunh,\n"
					+ "         sum(diaocl) diaocl,sum(panyk) panyk,sum(kuc) kuc,sum(shuifctz) shuifctz from yueshchjb\n"
					+ "         where riq = "
					+ strOraDate
					+ "\n"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid()
					+ ")) group by riq,fenx) hc,\n"
					+ "       (select riq,fenx,round_new(sum(qickc),3) qickc,round_new(sum(shouyl),3) shouyl,round_new(sum(fadyy),3) fadyy,round_new(sum(gongry),3) gongry,round_new(sum(qithy),3) qithy,round_new(sum(sunh),3) sunh, \n"
					+ "         round_new(sum(diaocl),3) diaocl,round_new(sum(panyk),3) panyk,round_new(sum(kuc),3) kuc from yueshcyb\n"
					+ "         where riq = "
					+ strOraDate
					+ "\n"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid()
					+ ")) group by riq,fenx) yo,\n"
					+ "       (select riq,fenx, sum(fadytrml) fadytrml,sum(gongrytrml) gongrytrml,sum(fadl) fadl,sum(gongrl) gongrl,\n"
					+ "         sum(FADMZBML) FADMZBML,sum(FADYZBZML) FADYZBZML,sum(FADQZBZML) FADQZBZML,sum(GONGRMZBML) GONGRMZBML,\n"
					+ "         sum(GONGRYZBZML) GONGRYZBZML,sum(GONGRQZBZML) GONGRQZBZML,\n"
					+ "         Round_New( DECODE((sum(FADL)) ,0,0,sum((FADMZBML+ FADYZBZML+ FADQZBZML))*100/sum((FADL))) ,0) FADBZMH,\n"
					+ "         Round_New(DECODE(sum(GONGRL),0,0, sum((GONGRMZBML+ GONGRYZBZML+ GONGRQZBZML))*1000/ sum(GONGRL)),0) GONGRBZMH \n"
					+ "         from yuezbb\n" + "         where riq = "
					+ strOraDate + "\n" + "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid() + ")) group by riq,fenx) zb,\n"
					+ "       (select sum(jizurl) jizurl\n"
					+ "          from jizb\n" + "         where toucrq <= "
					+ strOraDate + "\n" + "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid() + "))) jz,\n" + "       (select '本月' fenx\n"
					+ "          from dual\n" + "        union\n"
					+ "        select '累计' fenx from dual) fx\n"
					+ " where fx.fenx = hc.fenx(+)\n"
					+ "   and fx.fenx = yo.fenx(+)\n"
					+ "   and fx.fenx = zb.fenx(+)\n"
					+ "   and fx.fenx = sl.fenx(+)";
		} else if (getReportType().equals(RT_DR03)) {
			return "select "
					+ newid
					+ strOraDate
					+ " riq,t.diancxxb_id,t.gongysb_id,t.yunsfsb_id,\n"
					+ "       y.fenx,\n"
					+ "       y.biaoz jincl,\n"
					+ "       y.biaoz jianjl,\n"
					+ "       round_new(decode(y.biaoz, 0, 0, y.biaoz * 100 / y.biaoz), 2) chouclv,\n"
					+ "       round_new(decode(y.biaoz, 0, 0, y.biaoz * 100 / y.biaoz), 2) guohlv,\n"
					+ "       decode(y.biaoz,0,0,null,0,"
					+ "		(100 - round_new(decode(y.biaoz, 0, 0, y.biaoz * 100 / y.biaoz), 2))) jianclv,\n"
					+ "       y.yingd,\n"
					+ "       y.yingdzje,\n"
					+ "       y.kuid,\n"
					+ "       y.kuidzje,\n"
					+ "       y.suopsl,\n"
					+ "       y.suopje,\n"
					+ "       '' shuom\n"
					+ "  from yueslb y, yuetjkjb t\n"
					+ " where y.yuetjkjb_id = t.id\n"
					+ "   and t.riq = "
					+ strOraDate
					+ "\n"
					+ "   and (t.diancxxb_id = "
					+ getTreeid()
					+ " or t.diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid() + "))";

		} else if (getReportType().equals(RT_DR04)) {
			return "select "
					+ newid
					+ strOraDate
					+ " riq,t.diancxxb_id,t.gongysb_id, t.pinzb_id, t.yunsfsb_id,y.fenx, l.biaoz jincl, l.biaoz as jianjl,\n"
					+ "decode(l.jingz,0,0,round_new(l.jingz*100/l.jingz,2)) jianjlv,\n"
					+ "y.qnet_ar_kf, dengji(y.qnet_ar_kf) dengj_kf, y.mt_kf, y.aar_kf,\n"
					+ "y.vdaf_kf, y.std_kf, y.qnet_ar, dengji(y.qnet_ar) dengj, y.mt,\n"
					+ "y.aar, y.vdaf, y.std, dengji(y.qnet_ar) - dengji(y.qnet_ar_kf) dengjc,\n"
					+ "y.qnet_ar - y.qnet_ar_kf rezc,\n"
					+ "decode(l.biaoz,0,0,round_new(y.zhijbfml*100/l.biaoz,2)) zhijbflv,\n"
					+ "decode(y.zhijbfml,0,0,round_new(y.zhijbfje/y.zhijbfml,3)) danjc,\n"
					+ "y.zhijbfje,\n"
					+ "y.suopje,\n"
					+ "y.lsuopsl,\n"
					+ "y.lsuopje\n"
					+ "from yuezlb y, yueslb l, yuetjkjb t\n"
					+ "where y.yuetjkjb_id = t.id\n"
					+ "and l.yuetjkjb_id = t.id\n"
					+ "and l.fenx = y.fenx\n"
					+ "and t.riq = "
					+ strOraDate
					+ "\n"
					+ "and (t.diancxxb_id = "
					+ getTreeid()
					+ " or t.diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid() + "))";
		} else if (getReportType().equals(RT_DR08_1)) {
			return "select  "
					+ newid
					+ " riq,diancxxb_id,gongysb_id,1 yunsfsbid,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj, \n" 
					+ "			kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,\n"
					+ "         syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
					+ "			decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj, \n"
					+ " 		decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk\n"
					+ "         from (\n"
					+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
					+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n"
					+ "       j.fenx,\n"
					+ "       t.gongysb_id,\n"
					+ strOraDate
					+ " riq,max(t.diancxxb_id)diancxxb_id,"
					+ "       sum(j.jiesl) as jsl,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
					+ "\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
					
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2)* (select nvl(max(zhi),0.07) from xitxxb where xitxxb.mingc='运费税率') as qyfs,\n"
			
					+ "       0 gangzf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
					+ "       sum(j.meij * j.jiesl) mk\n"
					+ "  from yuejsbmdj j, yuetjkjb t, vwgongys gys\n"
					+ " where j.yuetjkjb_id = t.id\n"
					+ "   and t.gongysb_id = gys.id\n"
					+ "   and t.riq = to_date('"
					+ strDate
					+ "', 'yyyy-mm-dd')\n"
					+ "   and t.diancxxb_id = "
					+ getTreeid()
					+ "\n"
					+ " group by rollup(fenx,gys.mingc,t.gongysb_id)\n"
					+ "\n"
					+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
					+ "            or grouping(fenx)=1)and grouping(gys.mingc)=0\n"
					+ " order by xuh,fenx  )\n";
		} else if (getReportType().equals(RT_DR08)) {
			return "select  "
					+ newid
					+ " riq,diancxxb_id,gongysb_id,fenx,laiml,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj,\n"
					+ "        kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
					+ "		   decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj,\n"
					+ " 	   decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj \n"
					+ "        from (\n"
					+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
					+ "       decode(gys.mingc,null,'总计',gys.mingc) as mingc,\n"
					+ "       sl.fenx,\n"
					+ "       t.gongysb_id,\n"
					+ strOraDate
					+ " riq,max(t.diancxxb_id)diancxxb_id,"
					+ "       sum(sl.biaoz) as laiml,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as daoczhj,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.meij*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as  kj,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.meijs*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as kjs,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.jiaohqzf*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as jhqyzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyfs,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syfs,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as szf,\n"
					+ "\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyfs,\n"
					+ "       0 gangzf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.daozzf*sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as dzzf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.qit*sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qtfy,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.qnet_ar * sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qnet_ar,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.biaomdj * sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bmdj,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.buhsbmdj * sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bhsbmj\n"
					+ "\n"
					+ "  from yuercbmdj j,yueslb sl,yuetjkjb t,vwgongys gys\n"
					+ " where j.yuetjkjb_id = t.id\n"
					+ "   and sl.yuetjkjb_id=t.id\n"
					+ "   and j.yuetjkjb_id=sl.yuetjkjb_id\n"
					+ "   and t.gongysb_id = gys.id\n"
					+ "   and j.fenx=sl.fenx\n"
					+ "   and t.riq = to_date('"
					+ strDate
					+ "', 'yyyy-mm-dd')\n"
					+ "   and t.diancxxb_id = "
					+ getTreeid()
					+ "\n"
					+ " group by rollup(sl.fenx,gys.mingc,t.gongysb_id)\n"
					+ "\n"
					+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
					+ "            or grouping(sl.fenx)=1)and grouping(gys.mingc)=0\n"
					+ " order by xuh,sl.fenx)";
		} else {
			return "";
		}
	}

	private String getInsRptTableSql() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		String sql = "";
		if (getReportType().equals(RT_DR16)) {
			sql = "insert into diaor16bb (id,riq,diancxxb_id,gongysb_id,pinz,yunsfsb_id,"
					+ "fenx,shangyjc,kuangfgyl,yuns,kuid,farl,huif,shuif,huiff,shijhylhj,"
					+ "shijhylfdy,shijhylgry,shijhylqty,shijhylzcsh,diaocl,panypk,yuemjc)("
					+ getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR01)) {
			sql = "insert into diaor01bb (id,riq,diancxxb_id,fadsbrl,fenx,meitsg,meithyhj,"
					+ "meithyfd,meithygr,meithyqt,meithysh,meitkc,shiysg,shiyhyhj,shiyhyfd,"
					+ "shiyhygr,shiyhyqt,shiyhysh,shiykc,fadl,gongrl,biaozmhfd,biaozmhgr,"
					+ "tianrmhfd,tianrmhgr,biaozmlfd,biaozmlgr,zonghrl,zonghm) ("
					+ getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR03)) {
			sql = "insert into diaor03bb (id,riq,diancxxb_id,gongysb_id,yunsfsb_id,fenx,"
					+ "jincsl,choucsl,zhanjcm,guoh,jianc,yingdsl,yingdzje,kuid,kuidzje,"
					+ "suopsl,suopje,shuom) (" + getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR04)) {
			sql = "insert into diaor04bb (id,riq,diancxxb_id,gongysb_id,pinz,yunsfsb_id,fenx,"
					+ "jincsl,yanssl,jianzl,kuangffrl,kuangfdj,kuangfsf,kuangfhf,kuangfhff,kuangflf,"
					+ "changffrl,changfdj,changfsf,changfhf,changfhff,changflf,dengjc,"
					+ "rejc,bufl,danjc,zongje,suopje,liuspsl,liusp) ("
					+ getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR08_1)) {
			sql = "insert into diaor08bb (id,riq,diancxxb_id,gongysb_id,yunsfsb_id,fenx,"
					+ "meil,daoczhj,kuangj,zengzse,jiaohqyzf,tielyf,tieyse,tielzf,shuiyf,"
					+ "shuiyse,shuiyzf,qiyf,qiyse,gangzf,daozzf,qitfy,rez,biaomdj,buhsbmdj,"
					+ "meik) (" + getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			sql = "begin \n";
			for (int i = 2; i <= 35; i++) {
				sql += "insert into zhibwcqkb(id,xuh,riq,fenx,diancxxb_id,zhibmc,danwmc,benq,tongq,bianhqk,beiz)values\n"
						+ "(getnewid("
						+ getTreeid()
						+ "),"
						+ (i - 1)
						+ ","
						+ strOraDate
						+ ",'"
						+ rt_zhibb.body.getCellValue(i, 3)
						+ "',"
						+ getTreeid()
						+ ",'"
						+ rt_zhibb.body.getCellValue(i, 1)
						+ "','"
						+ rt_zhibb.body.getCellValue(i, 2)
						+ "',"
						+ ((rt_zhibb.body.getCellValue(i, 4).equals("")) ? "0"
								: rt_zhibb.body.getCellValue(i, 4))
						+ ","
						+ ((rt_zhibb.body.getCellValue(i, 5).equals("")) ? "0"
								: rt_zhibb.body.getCellValue(i, 5))
						+ ","
						+ ((rt_zhibb.body.getCellValue(i, 6).equals("")) ? "0"
								: rt_zhibb.body.getCellValue(i, 6))
						+ ",'"
						+ rt_zhibb.body.getCellValue(i, 7) + "');\n";
			}
			sql += "end ;";

		} else if (getReportType().equals(RT_DR08)) {
			sql = "insert into DIAOR08BB_NEW (id,riq,diancxxb_id,gongysb_id,fenx,"
					+ "meil,daoczhj,kuangj,zengzse,jiaohqyzf,tielyf,tieyse,tielzf,shuiyf,"
					+ "shuiyse,shuiyzf,qiyf,qiyse,gangzf,daozzf,qitfy,rez,biaomdj,buhsbmdj"
					+ ") (" + getBaseSql(true) + ")";
		}
		return sql;
	}

	private String getDelRptTableSql(String strOraDate) {
		String talbeName = "";
		if (getReportType().equals(RT_DR16)) {
			talbeName = "diaor16bb";
		} else if (getReportType().equals(RT_DR01)) {
			talbeName = "diaor01bb";
		} else if (getReportType().equals(RT_DR03)) {
			talbeName = "diaor03bb";
		} else if (getReportType().equals(RT_DR04)) {
			talbeName = "diaor04bb";
		} else if (getReportType().equals(RT_DR08_1)) {
			talbeName = "diaor08bb";
		} else if (getReportType().equals(RT_DR08)) {
			talbeName = "diaor08bb_new";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			talbeName = "zhibwcqkb";
		}
		String sql = "delete from " + talbeName + " where riq = " + strOraDate
				+ " and diancxxb_id=" + getTreeid();
		return sql;
	}

	private String getRenwmc() {
		String talbeName = "";
		if (getReportType().equals(RT_DR16)) {
			talbeName = "diaor16bb";
		} else if (getReportType().equals(RT_DR01)) {
			talbeName = "diaor01bb";
		} else if (getReportType().equals(RT_DR03)) {
			talbeName = "diaor03bb";
		} else if (getReportType().equals(RT_DR04)) {
			talbeName = "diaor04bb";
		} else if (getReportType().equals(RT_DR08_1)) {
			talbeName = "diaor08bb";
		} else if (getReportType().equals(RT_DR08)) {
			talbeName = "diaor08bb_new";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			talbeName = "zhibwcqkyb";
		}
		return talbeName;
	}

	private void Shangb() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.getDelete(getDelRptTableSql(strOraDate));
		con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con, getTreeid(), strOraDate, "0", getMokmc(), v
				.getRenymc(), "");
		con.commit();
		con.Close();
		InterFac_dt senter = new InterFac_dt();
		senter.request(getRenwmc());
		setMsg("上报数据成功！");
	}

	private void Shenqxg() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con, getTreeid(), strOraDate, "0", getMokmc(), v
				.getRenymc(), getMiaos());
		con.Close();
		InterFac_dt senter = new InterFac_dt();
		senter.request(getRenwmc());
	}

	private boolean getDiancg() {
		JDBCcon con = new JDBCcon();
		String sqlq = "select id from diancxxb where fuid in(select id from diancxxb where id="
				+ getTreeid() + " and jib=3)";
		ResultSetList rs = con.getResultSetList(sqlq);
		if (rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con = new JDBCcon();
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
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

		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);

		if (SB_Yes.equals(visit.getString2())) {
			if (getDiancg()) {
				ToolbarButton tbsb = new ToolbarButton(null, "上报",
						"function(){document.getElementById('SbButton').click();}");
				tbsb.setIcon(SysConstant.Btn_Icon_SelSubmit);
				tb1.addItem(tbsb);
				ToolbarButton tbsq = new ToolbarButton(null, "申请修改",
						"function(){Rpt_window.show();}");
				tbsq.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(tbsq);

				String YUEF = getYuefValue().getValue();
				if (Integer.parseInt(YUEF) < 10) {
					YUEF = "0" + YUEF;
				} else {
					YUEF = getYuefValue().getValue();
				}
				String strDate = getNianfValue().getValue() + "-" + YUEF
						+ "-01";
				String strOraDate = DateUtil.FormatOracleDate(strDate);
				String sql = "select zhuangt from shujshb where diancxxb_id = "
						+ getTreeid() + "\n" + "and mokmc = '" + getMokmc()
						+ "' and riq = " + strOraDate;
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.next()) {
					if (rsl.getInt("zhuangt") == 1) {
						tbsb.setDisabled(true);
						tbsq.setDisabled(false);
					} else if (rsl.getInt("zhuangt") == 2) {
						tbsb.setDisabled(true);
						tbsq.setDisabled(true);
					} else if (rsl.getInt("zhuangt") == 0) {
						tbsb.setDisabled(false);
						tbsq.setDisabled(true);
					}
				} else {
					tbsb.setDisabled(false);
					tbsq.setDisabled(true);
				}
			}
		}
		setToolbar(tb1);
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

	// 年份下拉框
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
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

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	private int paperStyle;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public int getJibbyDCID(JDBCcon con, String dcid) {
		int jib = 3;
		ResultSetList rsl = con
				.getResultSetList("select jib from diancxxb where id =" + dcid);
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		rsl.close();
		return jib;
	}
}