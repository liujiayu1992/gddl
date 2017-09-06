package com.zhiren.dc.diaoygl.Kucmjg;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

 
public class Kucmjgb extends BasePage {
	
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
		return  getKucmjgb();
	}
	
	private String getKucmjgb(){
		JDBCcon con = new JDBCcon();
		StringBuffer SQL = new StringBuffer();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		
		SQL.append("SELECT DECODE(GROUPING(DIANCXXB_ID) + GROUPING(PINZB_ID), 2, '公司合计', 1, DIANCXXB_ID \n");
		SQL.append("  || '小计', DIANCXXB_ID) DANW, \n");
		SQL.append("  PINZB_ID, \n");
		SQL.append("  SUM(LAIMSL)/ 10000 LAIMSL, \n");
		SQL.append("  ROUND_NEW(ROUND_NEW(DECODE(SUM(DECODE(LAIMRL, 0, 0, LAIMSL)), 0, 0, SUM(LAIMRL * LAIMSL) / SUM(DECODE(LAIMRL, 0, 0, LAIMSL))), 0), 0) LAIMRL, \n");
		SQL.append("  ROUND_NEW(DECODE(SUM(LAIMSL_BM), 0, 0, SUM(LAIMSL_BM * RUCBMDJ) / SUM(LAIMSL_BM)), 2) RUCBMDJ, \n");
		SQL.append("  ROUND_NEW(SUM(LAIMSL_BM* RUCBMDJ) / 10000, 6) RUCJE, \n");
		SQL.append("  SUM(HAOMSL) / 10000 HAOMSL, \n");
		SQL.append("  ROUND_NEW(ROUND_NEW(DECODE(SUM(DECODE(HAOMRL, 0, 0, HAOMSL)), 0, 0, SUM(HAOMRL * HAOMSL) / SUM(DECODE(HAOMRL, 0, 0, HAOMSL))), 0), 0) HAOMRL, \n");
		SQL.append("  ROUND_NEW(DECODE(SUM(HAOMSL_BM), 0, 0, SUM(HAOMSL_BM * RULBMDJ) / SUM(HAOMSL_BM)), 2) RULBMDJ, \n");
		SQL.append("  SUM(HAOMZF) HAOMZF, \n");
		SQL.append("  ROUND_NEW(SUM(HAOMSL_BM * RULBMDJ) / 10000 + SUM(HAOMZF), 6) RULJE, \n");
		SQL.append("  SUM(KUCSL) KUCSL, \n");
		SQL.append("  ROUND_NEW(ROUND_NEW(DECODE(SUM(DECODE(KUCRL, 0, 0, KUCSL)), 0, 0, SUM(KUCRL * KUCSL) / SUM(DECODE(KUCRL, 0, 0, KUCSL))), 0), 0) KUCRL, \n");
		SQL.append("  ROUND_NEW(DECODE(SUM(KUCSL_BM), 0, 0, SUM(KUCSL_BM * KUCBMDJ) / SUM(KUCSL_BM)), 2) KUCBMDJ, \n");
		SQL.append("  ROUND_NEW(SUM(KUCSL_BM * KUCBMDJ), 6) KUCJE, \n");
		SQL.append("  SUM(XIAZLMYC) XIAZLMYC \n");
		SQL.append("FROM \n");
		SQL.append("  (SELECT DC.XUH, \n");
		SQL.append("    DC.MINGC DIANCXXB_ID, \n");
		SQL.append("    P.MINGC PINZB_ID, \n");
		SQL.append("    DECODE(K.LAIMRL, 0, 0, K.LAIMSL * 7000 / K.LAIMRL) LAIMSL_BM, \n");
		SQL.append("    K.LAIMSL, \n");
		SQL.append("    K.LAIMRL, \n");
		SQL.append("    K.RUCBMDJ, \n");
		SQL.append("    DECODE(K.HAOMRL, 0, 0, K.HAOMSL * 7000 / K.HAOMRL) HAOMSL_BM, \n");
		SQL.append("    K.HAOMSL, \n");
		SQL.append("    K.HAOMRL, \n");
		SQL.append("    K.HAOMZF, \n");
		SQL.append("    K.RULBMDJ, \n");
		SQL.append("    DECODE(K.KUCRL, 0, 0, K.KUCSL * 7000 / K.KUCRL) KUCSL_BM, \n");
		SQL.append("    K.KUCSL, \n");
		SQL.append("    K.KUCRL, \n");
		SQL.append("    K. KUCBMDJ, \n");
		SQL.append("    K. XIAZLMYC, \n");
		SQL.append("    K.BEIZ \n");
		SQL.append("  FROM KUCMJGB K, \n");
		SQL.append("    (SELECT ID, XUH, MINGC FROM DIANCXXB WHERE JIB = 3 \n");
		SQL.append("    ) DC, \n");
		SQL.append("    PINZB P \n");
		SQL.append("  WHERE K.PINZB_ID = P.ID \n");
		SQL.append("  AND DC.ID        = K.DIANCXXB_ID(+) \n");
		SQL.append("    --AND k.zhuangt=1 \n");
		SQL.append("  AND K.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+" \n");
		SQL.append("  AND K.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+" \n");
		SQL.append("  ) K \n");
		SQL.append("GROUP BY ROLLUP(DIANCXXB_ID, PINZB_ID, XUH) \n");
		SQL.append("HAVING NOT GROUPING(XUH) + GROUPING(PINZB_ID) = 1 \n");
		SQL.append("ORDER BY GROUPING(DIANCXXB_ID) DESC, \n");
		SQL.append("  MAX(XUH), \n");
		SQL.append("  GROUPING(PINZB_ID) DESC, \n");
		SQL.append("  PINZB_ID");
		
		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();
		
		String ArrHeader[][]=new String[3][16];
		ArrHeader[0]=new String[] {"单位","煤种","入厂管理","入厂管理","入厂管理","入厂管理","耗煤管理","耗煤管理","耗煤管理","耗煤管理","耗煤管理","库存管理","库存管理","库存管理","库存管理","下周来煤预测"};
		ArrHeader[1]=new String[] {"单位","煤种","数量","热值","入厂标煤单价","入厂金额","数量","热值","入炉标煤单价","杂费","入炉金额","数量","热值","标煤单价","库存金额","下周来煤预测"};
		ArrHeader[2]=new String[] {"单位","煤种","万吨","Kcal/Kg","元/吨","万元","万吨","Kcal/Kg","万元","元/吨","万元","万吨","Kcal/Kg","元/吨","万元","万吨"};
		int ArrWidth[]=new int[] {150,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80};
		//rs.beforefirst();
		rt.setBody(new Table(rs, 3, 0, 1));
		
		rt.setTitle("国电电力所属各厂库存煤结构统计表", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 16, "报表日期：" + FormatDate(new Date()),Table.ALIGN_CENTER);
		
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

//		合并固定行
		rt.body.mergeFixedRow();
//		显示0
		rt.body.ShowZero = true;
//		第一二列居中
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);

		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 2, "主管：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 3, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "制表：", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
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
//	设置开始日期
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setBeginriqDate(String value){
			((Visit) getPage().getVisit()).setString4(value);
	}
//	设置终止日期
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	
	public void setEndriqDate(String value){
			((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df1 = new DateField();
		df1.setValue(this.getBeginriqDate());
		df1.Binding("Beginrq1","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("至"));
		DateField df2 = new DateField();
		df2.setValue(this.getEndriqDate());
		df2.Binding("Endrq2","");// 与html页中的id绑定,并自动刷新
		df2.setWidth(80);
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
//			清空日期变量
			visit.setString4(null);
			visit.setString5(null);
		}
		getToolbars();
		blnIsBegin = true;

	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}