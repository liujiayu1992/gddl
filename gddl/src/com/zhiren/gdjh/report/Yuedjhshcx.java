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
 
public class Yuedjhshcx extends BasePage implements PageValidateListener {

	public void submit(IRequestCycle cycle) {

	}

	// 报表展示
	public String getPrintTable() {
		return getYuedjh_caig()+getZhib();
	}

	private String getYuedjh_caig() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		Visit visit = (Visit) getPage().getVisit();
		String intyear= visit.getString5();
		String curdate = "to_date('" + intyear + "-01','yyyy-mm-dd')";
		String dcid=visit.getString6();

		String strSQL =		"SELECT\n" +
						"GONGYSB_ID,\n" + 
						"MEIKXXB_ID,\n" + 
						"JIHKJB_ID,\n" + 
						"PINZB_ID,\n" + 
						"FAZ_ID,\n" + 
						"JIH_SL,\n" + 
						"JIH_REZ,\n" + 
						"JIH_LIUF,\n" + 
						"JIH_HFF,\n" + 
						"JIH_MEIJ,\n" + 
						"JIH_MEIJBHS,\n" + 
						"JIH_YUNJ,\n" + 
						"JIH_YUNJBHS,\n" + 
						"JIH_ZAF,\n" + 
						"JIH_ZAFBHS,\n" + 
						"(JIH_MEIJ+JIH_YUNJ+JIH_ZAF) JIH_DAOCJ,\n" + 
						"(JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS) JIH_DAOCJBHS,\n" + 
						" DECODE(JIH_REZ, 0,0, ROUND((JIH_MEIJ+JIH_YUNJ+JIH_ZAF) * 29.271 / JIH_REZ, 2)) JIH_DAOCBMDJ,\n" + 
						" DECODE(JIH_REZ,0,0,ROUND((JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS) *29.271 / JIH_REZ,2)) JIH_DAOCBMDJBHS\n" + 
						"FROM(SELECT decode(grouping(gys.mingc),1,'<b>总计</b>',GYS.MINGC)        GONGYSB_ID,\n" +
		"       MK.MINGC        MEIKXXB_ID, J.MINGC JIHKJB_ID,\n" + 
		"       PZ.MINGC        PINZB_ID,\n" + 
		"       CZ.MINGC        FAZ_ID,\n" + 
		"       sum(JIH_SL) JIH_SL,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_REZ*JIH_SL) /sum(JIH_SL)),2) JIH_REZ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_LIUF*JIH_SL) /sum(JIH_SL)),2) JIH_LIUF,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_HFF*JIH_SL) /sum(JIH_SL)),2)JIH_HFF ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_MEIJ*JIH_SL) /sum(JIH_SL)),2) JIH_MEIJ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_MEIJBHS*JIH_SL) /sum(JIH_SL)),2) JIH_MEIJBHS,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_YUNJ*JIH_SL) /sum(JIH_SL)),2) JIH_YUNJ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_YUNJBHS*JIH_SL) /sum(JIH_SL)),2)JIH_YUNJBHS ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_ZAF*JIH_SL) /sum(JIH_SL)),2)JIH_ZAF ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_ZAFBHS*JIH_SL) /sum(JIH_SL)),2) JIH_ZAFBHS,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCJ*JIH_SL) /sum(JIH_SL)),2) JIH_DAOCJ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCJBHS*JIH_SL) /sum(JIH_SL)),2) JIH_DAOCJBHS,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCBMDJ*JIH_SL) /sum(JIH_SL)),2)JIH_DAOCBMDJ ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCBMDJBHS*JIH_SL) /sum(JIH_SL)),2) JIH_DAOCBMDJBHS\n" + 
		"  FROM YUEDJH_CAIG CG, GONGYSB GYS, MEIKXXB MK, PINZB PZ, CHEZXXB CZ,JIHKJB J\n" + 
		" WHERE CG.GONGYSB_ID = GYS.ID AND CG.JIHKJB_ID=J.ID\n" + 
		"   AND CG.MEIKXXB_ID = MK.ID\n" + 
		"   AND CG.PINZB_ID = PZ.ID\n" + 
		"   AND CG.FAZ_ID = CZ.ID\n" + 
		"   AND DIANCXXB_ID = "+dcid+"\n" + 
		"   AND RIQ = "+curdate+"\n" + 
		"   GROUP BY ROLLUP ((GYS.MINGC,MK.MINGC,J.MINGC,PZ.MINGC,CZ.MINGC)))";
		
		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 5);
		rt.setBody(tb);

		ArrHeader = new String[3][19];
		ArrHeader[0] = (new String[] { "供货单位","煤矿单位","计划口径", "品种","发站","采购量","热值","硫份","挥发份","车板价","车板价<br>(不含税)","运费","运费<br>(不含税)","杂费","杂费<br>(不含税)","到厂价","到厂价<br>(不含税)","到厂<br>标煤单价","到厂<br>标煤单价<br>(不含税)"});
		ArrHeader[1] = (new String[] { "供货单位", "煤矿单位","计划口径", "品种", "发站", "吨","兆焦/千克","%","%","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨"});
		ArrHeader[2] = (new String[] { "甲", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"});
		
		ArrWidth = (new int[] { 150,150,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60 });
		
		String rptitle="表一：煤炭采购计划(表中“杂费”是指煤炭买卖合同中约定的与煤款或运费一同结算的费用：包括填报装车、站台、港杂等费用，一票结算的除外)";
		String[] str=visit.getString5().split("-");
		if(str.length==2){
			rptitle="表一:"+str[0]+"年"+str[1]+"月煤炭采购计划(表中“杂费”是指煤炭买卖合同中约定的与煤款或运费一同结算的费用：包括填报装车、站台、港杂等费用，一票结算的除外)";
		}
		
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
		rt.body.merge(1, 1, 2, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.body.merge(1, 3, 2, 3);
		rt.body.merge(1, 4, 2, 4);
		rt.body.merge(1, 5, 2, 5);
		
//		设置对齐方式
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
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
		
		String curdate = "to_date('" + visit.getString5() + "-01','yyyy-mm-dd')";
		String dcid=visit.getString6();

		String strSQL ="SELECT DECODE(0, 0, '合计') QIB,\n" +
			"       FADL,\n" + 
			"       GONGDMH,\n" + 
			"       FADCYDL,\n" + 
			"       FADBML,\n" + 
			"       GONGRL,\n" + 
			"       GONGRMH,\n" + 
			"       GONGRBML,\n" + 
			"       BIAOMLHJ,\n" + 
			"       HAOYYML,\n" + 
			"       RLZHBMDJ,\n" + 
			"       SHANGYMKC,\n" + 
			"       SHANGYMKCDJ,\n" + 
			"       SHANGYMKCRZ,\n" + 
			"       YUEMKCJHZ,\n" + 
			"       YUEMKCRZ\n" + 
			"  FROM YUEDJH_ZHIB\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+dcid;
		
		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[3][16];
		ArrHeader[0] = (new String[] { "期别", "发电量", "供电煤耗", "发电厂用电率", "发电标煤量","供热量","供热煤耗","供热标煤量","标煤量合计","耗用原煤量","入炉综合标煤单价","上月末库存","上月末库存单价","上月末库存热值","月末库存计划值","月末库存热值"});
		ArrHeader[1] = (new String[] { "期别", "万千瓦时","克/千瓦时", "%", "吨","万吉焦","千克/吉焦","吨","吨","吨","元/吨","吨","元/吨","兆焦/千克","吨","兆焦/千克"});
		ArrHeader[2] = (new String[] { "甲", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15"});
		
		ArrWidth = (new int[] { 80,80,70,90,80,80,70,80,80,80,100,80,90,90,90,80});
		
		String rptitle="相关指标预测";
		String[] str=visit.getString5().split("-");
		if(str.length==2){
			rptitle="表二:"+str[0]+"年"+str[1]+"月相关指标预测";
		}
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
			visit.setActivePageName(getPageName().toString());
			visit.setString5("");
			visit.setString6("");
//			通过参数配置日期和电厂标识
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString5(cycle.getRequestContext().getParameter("lx"));
			}
			if(cycle.getRequestContext().getParameter("dc") != null){
				visit.setString6(cycle.getRequestContext().getParameter("dc"));
			}
			if(visit.getString5()==null || visit.getString5().equals("")){
				visit.setString5(DateUtil.getYear(new Date()) + "");
			}
			if(visit.getString6()==null || visit.getString6().equals("")){
				visit.setString6(visit.getDiancxxb_id()+"");
			}
			visit.setList1(null);
		}
	}
}