package com.zhiren.dc.monthReport.gd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：赵胜男
 * 时间：2012-09-07
 * 描述：修改调燃02表显示总厂合计 
 */
public class Meitdhqkb extends BasePage {
	
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public boolean getRaw() {
		return true;
	}
	
	// 获得选择的树节点的对应的电厂名称
	private String getDcMingc(String id) {
		if (id == null || "".equals(id)) {
			return "";
		}
		JDBCcon con = new JDBCcon();
		String mingc = "";
		String sql = "select mingc from diancxxb where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
	
	// 刷新衡单列表
	public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		// 电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(80);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null, "function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
	}
	
	private String getSelectData(){
		JDBCcon con = new JDBCcon();
		
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
		
//		String StrMonth_next = "";
//		if (intMonth < 9) {
//			StrMonth_next = "0" + (intMonth + 1);
//		} else {
//			StrMonth_next = "" + (intMonth + 1);
//		}
		
		String riq = intyear + "-" + StrMonth + "-01";
		
		String sql = 
//			"SELECT DECODE(GROUPING(GYSMC) + GROUPING(KJMC), 2, '总计', 1, '' || KJMC, GYSMC) GMC,\n" +
//			"		DECODE(GROUPING(GYSMC) + GROUPING(KJMC), 2, '总计', 1, '' || KJMC, CZMC) CZ,\n" + 
//			"       SUM(DYJH) DYJH,\n" + 
//			"       SUM(LJJH) LJJH,\n" + 
//			"       SUM(DYLM) DYLM,\n" + 
//			"       SUM(LJLM) LJLM,\n" + 
//			"       SUM(DYLM) - SUM(DYJH) DYCQ,\n" + 
//			"       SUM(LJLM) - SUM(LJJH) LJCQ,\n" + 
//			"       ROUND_NEW(DECODE(SUM(DYJH), 0, 0, SUM(DYLM) / SUM(DYJH)), 2) DYDHL,\n" + 
//			"       ROUND_NEW(DECODE(SUM(LJJH), 0, 0, SUM(LJLM) / SUM(LJJH)), 2) LJDHL\n" + 
//			"  FROM (SELECT JHL.GYSMC,\n" + 
//			"               JHL.KJMC,\n" + 
//			"				JHL.CZMC,\n" + 
//			"               JHL.DY DYJH,\n" + 
//			"               JHL.LJ LJJH,\n" + 
//			"               NVL(LML.DY, 0) DYLM,\n" + 
//			"               NVL(LML.LJ, 0) LJLM\n" + 
//			"          FROM (SELECT JIHLJ.GYSMC,\n" + 
//			"                       JIHLJ.KJMC,\n" + 
//			"						JIHLJ.CZMC,\n" + 
//			"                       NVL(JIHDY.YUEJHCGL, 0) DY,\n" + 
//			"                       JIHLJ.YUEJHCGL LJ\n" + 
//			"                  FROM (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       JH.YUEJHCGL\n" + 
//			"                                  FROM YUECGJHB JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE JH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND JH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND JH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND JH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND JH.RIQ = TO_DATE('" + intyear + "-" + StrMonth + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) JIHDY,\n" + 
//			"                       (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       JH.YUEJHCGL\n" + 
//			"                                  FROM YUECGJHB JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE JH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND JH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND JH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND JH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND JH.RIQ BETWEEN\n" + 
//			"                                       TO_DATE('" + intyear + "-01-01', 'yyyy-mm-dd') AND\n" + 
//			"                                       TO_DATE('" + intyear + "-" + StrMonth + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) JIHLJ\n" + 
//			"                 WHERE JIHDY.GYSMC(+) = JIHLJ.GYSMC\n" + 
//			"                   AND JIHDY.KJMC(+) = JIHLJ.KJMC\n" + 
//			"					AND JIHDY.CZMC(+) = JIHLJ.CZMC) JHL,\n" + 
//			"               (SELECT SHIGLJ.GYSMC,\n" + 
//			"                       SHIGLJ.KJMC,\n" + 
//			"						SHIGLJ.CZMC,\n" + 
//			"                       NVL(SHIGDY.LAIML, 0) DY,\n" + 
//			"                       SHIGLJ.LAIML LJ\n" + 
//			"                  FROM (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
//			"                                  FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE FH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND FH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND FH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND FH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND FH.DAOHRQ >= TO_DATE('" + intyear + "-" + StrMonth + "-01', 'yyyy-mm-dd')\n" + 
//			"                                   AND FH.DAOHRQ < TO_DATE('" + intyear + "-" + StrMonth_next + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) SHIGDY,\n" + 
//			"                       (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购',\n" + 
//			"                                              DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
//			"                                  FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE FH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND FH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND FH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND FH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND FH.DAOHRQ >= TO_DATE('" + intyear + "-01-01', 'yyyy-mm-dd')\n" + 
//			"                                   AND FH.DAOHRQ < TO_DATE('" + intyear + "-" + StrMonth_next + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) SHIGLJ\n" + 
//			"                 WHERE SHIGDY.GYSMC(+) = SHIGLJ.GYSMC\n" + 
//			"                   AND SHIGDY.KJMC(+) = SHIGLJ.KJMC\n" + 
//			"					AND SHIGDY.CZMC(+) = SHIGLJ.CZMC) LML\n" + 
//			"         WHERE JHL.GYSMC = LML.GYSMC(+)\n" + 
//			"           AND JHL.KJMC = LML.KJMC(+)\n" + 
//			"			AND JHL.CZMC = LML.CZMC(+)\n" + 
//			"        UNION\n" + 
//			"        SELECT LML.GYSMC,\n" + 
//			"               LML.KJMC,\n" + 
//			"				LML.CZMC,\n" + 
//			"               NVL(JHL.DY, 0) DYJH,\n" + 
//			"               NVL(JHL.LJ, 0) LJJH,\n" + 
//			"               LML.DY DYLM,\n" + 
//			"               LML.LJ LJLM\n" + 
//			"          FROM (SELECT JIHLJ.GYSMC,\n" + 
//			"                       JIHLJ.KJMC,\n" + 
//			"						JIHLJ.CZMC,\n" + 
//			"                       NVL(JIHDY.YUEJHCGL, 0) DY,\n" + 
//			"                       JIHLJ.YUEJHCGL LJ\n" + 
//			"                  FROM (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       JH.YUEJHCGL\n" + 
//			"                                  FROM YUECGJHB JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE JH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND JH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND JH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND JH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND JH.RIQ = TO_DATE('" + intyear + "-" + StrMonth + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) JIHDY,\n" + 
//			"                       (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       JH.YUEJHCGL\n" + 
//			"                                  FROM YUECGJHB JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE JH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND JH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND JH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND JH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND JH.RIQ BETWEEN\n" + 
//			"                                       TO_DATE('" + intyear + "-01-01', 'yyyy-mm-dd') AND\n" + 
//			"                                       TO_DATE('" + intyear + "-" + StrMonth + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) JIHLJ\n" + 
//			"                 WHERE JIHDY.GYSMC(+) = JIHLJ.GYSMC\n" + 
//			"                   AND JIHDY.KJMC(+) = JIHLJ.KJMC\n" + 
//			"					AND JIHDY.CZMC(+) = JIHLJ.CZMC) JHL,\n" + 
//			"               (SELECT SHIGLJ.GYSMC,\n" + 
//			"                       SHIGLJ.KJMC,\n" + 
//			"						SHIGLJ.CZMC,\n" + 
//			"                       NVL(SHIGDY.LAIML, 0) DY,\n" + 
//			"                       SHIGLJ.LAIML LJ\n" + 
//			"                  FROM (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
//			"                                  FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE FH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND FH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND FH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND FH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND FH.DAOHRQ >= TO_DATE('" + intyear + "-" + StrMonth + "-01', 'yyyy-mm-dd')\n" + 
//			"                                   AND FH.DAOHRQ < TO_DATE('" + intyear + "-" + StrMonth_next + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) SHIGDY,\n" + 
//			"                       (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
//			"                          FROM (SELECT G.MINGC GYSMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', '企业自购', '合同供煤') KJMC,\n" + 
//			"										CZ.MINGC CZMC,\n" + 
//			"                                       DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
//			"                                  FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
//			"                                 WHERE FH.GONGYSB_ID = G.ID\n" + 
//			"                                   AND FH.JIHKJB_ID = KJ.ID\n" + 
//			"									AND FH.FAZ_ID = CZ.ID\n" + 
//			"                                   AND FH.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" + 
//			"                                   AND FH.DAOHRQ >= TO_DATE('" + intyear + "-01-01', 'yyyy-mm-dd')\n" + 
//			"                                   AND FH.DAOHRQ < TO_DATE('" + intyear + "-" + StrMonth_next + "-01', 'yyyy-mm-dd'))\n" + 
//			"                         GROUP BY GYSMC, KJMC, CZMC) SHIGLJ\n" + 
//			"                 WHERE SHIGDY.GYSMC(+) = SHIGLJ.GYSMC\n" + 
//			"                   AND SHIGDY.KJMC(+) = SHIGLJ.KJMC\n" +
//			"					AND SHIGDY.CZMC(+) = SHIGLJ.CZMC) LML\n" + 
//			"         WHERE JHL.GYSMC(+) = LML.GYSMC\n" + 
//			"           AND JHL.KJMC(+) = LML.KJMC\n" +
//			"			AND JHL.CZMC(+) = LML.CZMC)\n" + 
//			" GROUP BY ROLLUP(KJMC, GYSMC, CZMC)\n" + 
//			"HAVING NOT(GROUPING(GYSMC) + GROUPING(CZMC) = 1)\n" + 
//			" ORDER BY GROUPING(KJMC) DESC, KJMC, GROUPING(GYSMC) DESC, GYSMC, CZMC";
			
			"SELECT DECODE(GROUPING(G.MINGC) + GROUPING(KJ.MINGC), 2, '总计', 1, '' || KJ.MINGC, G.MINGC) GONGYSB_ID,\n" +
			"       DECODE(GROUPING(G.MINGC) + GROUPING(KJ.MINGC), 2, '总计', 1, '' || KJ.MINGC, CZ.MINGC) FAZ_ID,\n" + 
			"       SUM(DR.DANGYJH) DANGYJH,\n" + 
			"       SUM(DR.LEIJJH) LEIJJH,\n" + 
			"       SUM(DR.DANGYSG) DANGYSG,\n" + 
			"       SUM(DR.LEIJSG) LEIJSG,\n" + 
			"       SUM(DR.DANGYSG) - SUM(DR.DANGYJH) DANGYCQ,\n" + 
			"       SUM(DR.LEIJSG) - SUM(DR.LEIJJH) LEIJCQ,\n" + 
			"       ROUND_NEW(DECODE(SUM(DR.DANGYJH), 0, 0, SUM(DR.DANGYSG) / SUM(DR.DANGYJH)), 4) * 100 DANGYDHL,\n" + 
			"       ROUND_NEW(DECODE(SUM(DR.LEIJJH), 0, 0, SUM(DR.LEIJSG) / SUM(DR.LEIJJH)), 4) * 100 LEIJDHL\n" + 
			"  FROM DIAOR02BB DR,\n" + 
			"       GONGYSB G,\n" + 
			"       CHEZXXB CZ,\n" + 
			"       (SELECT ID, DECODE(MINGC, '市场采购', '企业自购', '合同供煤') MINGC\n" + 
			"          FROM JIHKJB) KJ\n" + 
			" WHERE DR.GONGYSB_ID = G.ID\n" + 
			"   AND DR.FAZ_ID = CZ.ID\n" + 
			"   AND DR.JIHKJB_ID = KJ.ID\n" + 
			"  and (diancxxb_id = " + getTreeid_dc() + " or diancxxb_id in (select id from diancxxb where fuid= " + getTreeid_dc() + " ))  \n" + 
			"   AND DR.RIQ = TO_DATE('" + riq + "', 'yyyy-mm-dd')\n" + 
			" GROUP BY ROLLUP(KJ.MINGC, G.MINGC, CZ.MINGC)\n" + 
			"HAVING NOT(GROUPING(G.MINGC) + GROUPING(CZ.MINGC) = 1)\n" + 
			" ORDER BY GROUPING(KJ.MINGC) DESC, KJ.MINGC, GROUPING(G.MINGC) DESC, G.MINGC, CZ.MINGC";
		
		Report rt = new Report();
		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		int aw = 0;
		
        ArrHeader = new String[3][10]; 
        ArrHeader[0] = new String[] {"供煤单位", "发站", "计划", "计划", "实供", "实供", "超（+）欠（-）", "超（+）欠（-）", "到货率%", "到货率%"};
        ArrHeader[1] = new String[] {"供煤单位", "发站", "当月", "累计", "当月", "累计", "当月", "累计", "当月", "累计"};
        ArrHeader[2] = new String[] {"甲", "乙", "1", "1", "2", "2", "3", "3", "4", "4"};
        ArrWidth = new int[] {120, 100, 100, 100, 100, 100, 100, 100, 100, 100};
    	aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
    	rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
    	rt.setTitle("煤炭到货情况表", ArrWidth);
    	
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 3, 0, 2));
		
		rt.setDefaultTitle(1, 3, "填报单位：" + ((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, intyear + "年" + intMonth + "月", Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 2, "调燃02表 单位：t", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(30);
		
		rt.body.mergeFixedRowCol();
		for (int i = 4; i < rt.body.getRows(); i++) {
			if ("总计".equals(rt.body.getCellValue(i, 1)) || "合同供煤".equals(rt.body.getCellValue(i, 1)) 
					|| "企业自购".equals(rt.body.getCellValue(i, 1))) {
				rt.body.merge(i, 1, i, 2);
				rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
			}
		}
		
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 2, "主管：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();
	}
	
	public String getPrintTable() {
		return getSelectData();
	}
	
	//	-------------------------电厂Tree-----------------------------------------------------------------
	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	DefaultTree dc;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
	//	-------------------------电厂Tree END-------------------------------------------------------------
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
			// begin方法里进行初始化设置
			visit.setString1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {
				visit.setString1(pagewith);
			}
			// visit.setString1(null);保存传递的非默认纸张的样式
			setTreeid_dc(visit.getDiancxxb_id() + "");
			// id=getTreeid();
			initToolbar();
		}
		
		if (nianfchanged) {
			nianfchanged = false;
			getSelectData();
		}
		if (yuefchanged) {
			yuefchanged = false;
			getSelectData();
		}
	}
	
	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		initToolbar();
	}
	
	// 页面登陆验证
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged = false;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	/**
	 * 月份
	 */
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
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public boolean yuefchanged = false;

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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
}