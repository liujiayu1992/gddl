package com.zhiren.gdjh.report;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Niandjhshcx extends BasePage implements PageValidateListener {

	public void submit(IRequestCycle cycle) {

	}
	
	// 报表展示
	public String getPrintTable() {
		return getCgjh()+getZfjh()+getZhib();
	}

	private String getCgjh() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		Visit visit = (Visit) getPage().getVisit();
		String intyear= visit.getString3();
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String dcid=visit.getString4();

		String strSQL =
			"SELECT GHDW,JIHKJ,HET_SL,HET_REZ,HET_MEIJ,HET_YUNJ,JIH_SL,JIH_REZ,JIH_REZC,JIH_MEIJ,JIH_MEIJBHS,JIH_YUNJ,\n" +
			"JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,JIH_QIT,JIH_QITBHS,(JIH_MEIJ+JIH_YUNJ+JIH_ZAF+JIH_QIT)JIH_DAOCJ," +
			"DECODE(JIH_REZ,0,0,ROUND((JIH_MEIJ+JIH_YUNJ+JIH_ZAF+JIH_QIT)*29.271/JIH_REZ,2))JIH_DAOCBMDJ\n" +
			"FROM ("+
			"SELECT DECODE(GROUPING(G.MINGC), 1, '<b>总计</b>', G.MINGC) GHDW, J.MINGC JIHKJ,\n" +
			"      SUM(CG.HET_SL) HET_SL,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_REZ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_REZ,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_MEIJ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_MEIJ,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_YUNJ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_YUNJ,\n" + 
			"      SUM(CG.JIH_SL) JIH_SL,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_REZ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_REZ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM((CG.JIH_REZ - NVL(ZB.RUCRLRZC, 0)) * CG.JIH_SL) /SUM(CG.JIH_SL)),2) JIH_REZC,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_MEIJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_MEIJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_MEIJBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_MEIJBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_YUNJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_YUNJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_YUNJBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_YUNJBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_ZAF * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_ZAF,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_ZAFBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_ZAFBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_QIT * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_QIT,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_QITBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_QITBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_DAOCJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_DAOCJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_DAOCBMDJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_DAOCBMDJ\n" + 
			"  FROM NIANDJH_CAIG CG, GONGYSB G, NIANDJH_ZHIB ZB, JIHKJB J\n" + 
			" WHERE CG.GONGYSB_ID = G.ID AND CG.JIHKJB_ID=J.ID\n" + 
			"   AND CG.DIANCXXB_ID = ZB.DIANCXXB_ID\n" + 
			"   AND CG.RIQ = ZB.RIQ\n" + 
			"   AND CG.RIQ = "+curdate+"\n" + 
			"   AND CG.DIANCXXB_ID="+dcid+"\n" + 
			" GROUP BY ROLLUP((G.MINGC,J.MINGC)))";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 4, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[4][19];
		ArrHeader[0] = (new String[] { "供货单位","计划口径", intyear+"年合同量", intyear+"年合同量", intyear+"年合同量", intyear+"年合同量",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测",intyear+"年采购情况预测"});
		ArrHeader[1] = (new String[] { "供货单位","计划口径", "数量", "热值", "车板价<br>(含税)", "运费<br>(含税)","到货量","热值","折入炉热值","车板价","车板价<br>(不含税)","运费","运费<br>(不含税)","杂费","杂费<br>(不含税)","其他","其他<br>(不含税)","到厂价","到厂标<br>煤单价" });
		ArrHeader[2] = (new String[] { "供货单位","计划口径", "吨","兆焦/千克", "元/吨", "元/吨","吨","兆焦/千克","兆焦/千克","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨"});
		ArrHeader[3] = (new String[] { "甲","乙", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16","17"});
		
		ArrWidth = (new int[] { 130,60,70,60,60,60,70,60,60,60,60,60,60,60,60,60,60,60,60 });
		String rptitle="表一："+intyear+"年煤炭采购计划(表中“杂费”是指煤炭买卖合同中约定的与煤款或运费一同结算的费用：包括填报装车、站台、港杂等费用，一票结算的除外)";
		rt.setTitle("", ArrWidth);

		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		合并表头
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 3, 1);
		rt.body.merge(1, 2, 3, 2);
//		设置对齐方式
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getZfjh() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		Visit visit = (Visit) getPage().getVisit();
		long intyear= Long.parseLong(visit.getString3());
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String dcid=visit.getString4();
		String strSQL =
			"SELECT DECODE(GROUPING(ZAFMC), 1, '<b>总计</b>', ZAFMC) ZAFMC,\n" +
			"       SUM(YUCJE),\n" + 
			"       YUCSM,\n" + 
			"       SUM(SHIJWCJE),\n" + 
			"       SUM(YUJWCJE),\n" + 
			"       YUJWCSM\n" + 
			"  FROM NIANDJH_ZAF\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+dcid+"\n" + 
			" GROUP BY ROLLUP((ZAFMC, YUCSM, YUJWCSM))";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 2, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[2][6];
		ArrHeader[0] = (new String[] { "费用名称", intyear+"年预测", intyear+"年预测", (intyear-1)+"年预计完成", (intyear-1)+"年预计完成",(intyear-1)+"年预计完成"});
		ArrHeader[1] = (new String[] { "费用名称", "预测(元)", "说明", "实际完成(元)", "预计(元)","说明"});
		
		ArrWidth = (new int[] { 170,120,440,120,120,260});
		String rptitle="表二:列入燃料成本的其它费用（燃料杂费）";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		合并表头
		rt.body.mergeFixedRowCol();
//		设置对齐方式
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getZhib() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		Visit visit = (Visit) getPage().getVisit();
		String intyear= visit.getString3();
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String dcid=visit.getString4();
		
		String strSQL ="SELECT DECODE(0, 0, '合计') QIB,\n" +
			"       ZB.FADL,\n" + 
			"       ZB.GONGDMH,\n" + 
			"       ZB.FADCYDL,\n" + 
			"       ZB.FADBML,\n" + 
			"       ZB.GONGRL,\n" + 
			"       ZB.GONGRMH,\n" + 
			"       ZB.GONGRBML,\n" + 
			"       ZB.BIAOMLHJ,\n" + 
			"       ZB.MEIZBML,\n" + 
			"       ZB.MEIZBMDJ,\n" + 
			"       ZB.RANYL, \n" +
			"		ZB.YOUZBML,\n" + 
			"       ZB.RANYDJ,\n" + 
			"       ZB.YOUZBMDJ,\n" + 
			"       ZB.QITFY,\n" + 
			"       ZB.RLZHBMDJ\n" + 
			"  FROM NIANDJH_ZHIB ZB\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+dcid+"";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[3][17];
		ArrHeader[0] = (new String[] { "期别", "发电量", "供电煤耗", "发电厂<br>用电率", "发电标煤量","供热量","供热煤耗","供热标煤量","标煤量合计","煤折标煤量","煤折<br>标煤单价","燃油量","油折<br>标煤量","燃油单价<br>(不含税)","油折<br>标煤单价","其他费用","入炉综合标煤单价"});
		ArrHeader[1] = (new String[] { "期别", "万千瓦时","克/千瓦时", "%", "吨","万吉焦","千克/吉焦","吨","吨","吨","元/吨","吨","吨","元/吨","元/吨","元","元/吨"});
		ArrHeader[2] = (new String[] { "甲", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16"});
		
		ArrWidth = (new int[] { 60,80,70,60,80,80,70,80,80,80,60,80,70,70,70,80,60});
		String rptitle="表三:"+intyear+"年相关指标预测";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		合并表头
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 2, 1);
//		设置对齐方式
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

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

	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
//			visit.setActivePageName(getPageName().toString());
			visit.setString3("");
			visit.setString4("");
//			通过参数配置日期和电厂标识
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString3(cycle.getRequestContext().getParameter("lx"));
			}
			if(cycle.getRequestContext().getParameter("dc") != null){
				visit.setString4(cycle.getRequestContext().getParameter("dc"));
			}
			if(visit.getString3()==null || visit.getString3().equals("")){
				visit.setString3(DateUtil.getYear(new Date()) + "");
			}
			if(visit.getString4()==null || visit.getString4().equals("")){
				visit.setString4(visit.getDiancxxb_id()+"");
			}
			
			visit.setList1(null);
		}
		
	}
	
}