package com.zhiren.dc.monthReport.gd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2013-01-02
 * 描述：修正生成时日期出现的BUG。使用数据库自带的日期转换方式
 * 		修正日期下拉框初始化功能。
 */
/*
 * 作者：夏峥
 * 时间：2014-01-25
 * 描述：修正生成时1月累计数据的BUG。
 */
public class Meitdhqkb_tb extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private void Save() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String tablename = "diaor02bb";
		
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tablename).append(" set dangyjh = ").append(mdrsl.getDouble("dangyjh")).append(", ")
			   .append("leijjh = ").append(mdrsl.getDouble("leijjh")).append(", ")
			   .append("dangysg = ").append(mdrsl.getDouble("dangysg")).append(", ")
			   .append("leijsg = ").append(mdrsl.getDouble("leijsg"))
			   .append(" where id = ").append(mdrsl.getString("ID")).append(";\n");
		}
		mdrsl.close();
		sql.append("end;");
		
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			this.setMsg("保存失败！");
		} else {
			this.setMsg("保存成功！");
		}
		
		con.Close();   	
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	private boolean hasData(String riq, String diancxxb_id) {
		boolean hasIt = false;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String sql = "SELECT * FROM DIAOR02BB WHERE RIQ = TO_DATE('" + riq + "', 'yyyy-mm-dd') AND DIANCXXB_ID = " + diancxxb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			hasIt = true;
		}
		
		return hasIt;
	}
	
	private void CreateData() {
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
		
//		String riq_yiy = intyear + "-01-01";
		String riq_dangy = intyear + "-" + StrMonth + "-01";
//		String riq_xiay = intyear + "-" + StrMonth_next + "-01";
		
//		String riq_shangy = "";
//		if (intMonth == 1) {
//			intyear = intyear - 1;
//			intMonth = 12;
//			riq_shangy = intyear + "-" + intMonth + "-01";
//		} else {
//			riq_shangy = intyear + "-" + (intMonth - 1) + "-01";
//		}
		
		String sql = 
			"SELECT G.ID GYSMC, C.ID CZMC, J.ID KJMC, DYJH, LJJH, ROUND_NEW(DYLM, 0) DYLM, ROUND_NEW(LJLM, 0) LJLM\n" + 
			"FROM\n" + 
			"(SELECT GYSMC, CZMC, KJMC, SUM(DYJH) DYJH, SUM(LJJH) LJJH, SUM(DYLM) DYLM, SUM(LJLM) LJLM\n" + 
			"FROM\n" + 
			"(SELECT JHL.GYSMC, JHL.CZMC, JHL.KJMC, JHL.DY DYJH, JHL.LJ LJJH, NVL(LML.DY, 0) DYLM, NVL(LML.LJ, 0) LJLM\n" +
			"  FROM (SELECT JIHLJ.GYSMC, JIHLJ.KJMC, JIHLJ.CZMC, NVL(JIHDY.YUEJHCGL, 0) DY, JIHLJ.YUEJHCGL LJ\n" + 
			"          FROM (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
			"                  FROM (SELECT G.MINGC GYSMC, KJ.MINGC KJMC, CZ.MINGC CZMC, JH.JIH_SL YUEJHCGL\n" + 
			"                          FROM YUEDJH_CAIG JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE JH.GONGYSB_ID = G.ID\n" + 
			"                           AND JH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND JH.FAZ_ID = CZ.ID\n" + 
			"                           AND JH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND JH.RIQ = TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) JIHDY,\n" + 
			"               (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
			"                  FROM (SELECT G.MINGC GYSMC, KJ.MINGC KJMC, CZ.MINGC CZMC, JH.JIH_SL YUEJHCGL\n" + 
			"                          FROM YUEDJH_CAIG JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE JH.GONGYSB_ID = G.ID\n" + 
			"                           AND JH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND JH.FAZ_ID = CZ.ID\n" + 
			"                           AND JH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND JH.RIQ BETWEEN\n" + 
			"                               trunc(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),'yyyy') AND\n" + 
			"                               TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) JIHLJ\n" + 
			"         WHERE JIHDY.GYSMC(+) = JIHLJ.GYSMC\n" + 
			"           AND JIHDY.KJMC(+) = JIHLJ.KJMC\n" + 
			"           AND JIHDY.CZMC(+) = JIHLJ.CZMC) JHL,\n" + 
			"       (SELECT SHIGLJ.GYSMC, SHIGLJ.KJMC, SHIGLJ.CZMC, NVL(SHIGDY.LAIML, 0) DY, SHIGLJ.LAIML LJ\n" + 
			"          FROM (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
			"                  FROM (SELECT G.MINGC GYSMC,\n" + 
			"                               KJ.MINGC KJMC,\n" + 
			"                               CZ.MINGC CZMC,\n" + 
			"                               DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
			"                          FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE FH.GONGYSB_ID = G.ID\n" + 
			"                           AND FH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND FH.FAZ_ID = CZ.ID\n" + 
			"                           AND FH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND FH.DAOHRQ >= TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd')\n" + 
			"                           AND FH.DAOHRQ < ADD_MONTHS(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),1))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) SHIGDY,\n" + 
			"               (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
			"                  FROM (SELECT G.MINGC GYSMC,\n" + 
			"                               KJ.MINGC KJMC,\n" + 
			"                               CZ.MINGC CZMC,\n" + 
			"                               DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
			"                          FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE FH.GONGYSB_ID = G.ID\n" + 
			"                           AND FH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND FH.FAZ_ID = CZ.ID\n" + 
			"                           AND FH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND FH.DAOHRQ >= trunc(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),'yyyy')\n" + 
			"                           AND FH.DAOHRQ < ADD_MONTHS(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),1))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) SHIGLJ\n" + 
			"         WHERE SHIGDY.GYSMC(+) = SHIGLJ.GYSMC\n" + 
			"           AND SHIGDY.KJMC(+) = SHIGLJ.KJMC\n" + 
			"           AND SHIGDY.CZMC(+) = SHIGLJ.CZMC) LML\n" + 
			" WHERE JHL.GYSMC = LML.GYSMC(+)\n" + 
			"   AND JHL.KJMC = LML.KJMC(+)\n" + 
			"   AND JHL.CZMC = LML.CZMC(+)\n" + 
			"UNION\n" + 
			"SELECT LML.GYSMC, LML.CZMC, LML.KJMC, NVL(JHL.DY, 0) DYJH, NVL(JHL.LJ, 0) LJJH, LML.DY DYLM, LML.LJ LJLM\n" + 
			"  FROM (SELECT JIHLJ.GYSMC, JIHLJ.KJMC, JIHLJ.CZMC, NVL(JIHDY.YUEJHCGL, 0) DY, JIHLJ.YUEJHCGL LJ\n" + 
			"          FROM (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
			"                  FROM (SELECT G.MINGC GYSMC, KJ.MINGC KJMC, CZ.MINGC CZMC, JH.JIH_SL YUEJHCGL\n" + 
			"                          FROM YUEDJH_CAIG JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE JH.GONGYSB_ID = G.ID\n" + 
			"                           AND JH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND JH.FAZ_ID = CZ.ID\n" + 
			"                           AND JH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND JH.RIQ = TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) JIHDY,\n" + 
			"               (SELECT GYSMC, KJMC, CZMC, SUM(YUEJHCGL) YUEJHCGL\n" + 
			"                  FROM (SELECT G.MINGC GYSMC, KJ.MINGC KJMC, CZ.MINGC CZMC, JH.JIH_SL YUEJHCGL\n" + 
			"                          FROM YUEDJH_CAIG JH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE JH.GONGYSB_ID = G.ID\n" + 
			"                           AND JH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND JH.FAZ_ID = CZ.ID\n" + 
			"                           AND JH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND JH.RIQ BETWEEN\n" + 
			"                               trunc(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),'yyyy') AND\n" + 
			"                               TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) JIHLJ\n" + 
			"         WHERE JIHDY.GYSMC(+) = JIHLJ.GYSMC\n" + 
			"           AND JIHDY.KJMC(+) = JIHLJ.KJMC\n" + 
			"           AND JIHDY.CZMC(+) = JIHLJ.CZMC) JHL,\n" + 
			"       (SELECT SHIGLJ.GYSMC, SHIGLJ.KJMC, SHIGLJ.CZMC, NVL(SHIGDY.LAIML, 0) DY, SHIGLJ.LAIML LJ\n" + 
			"          FROM (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
			"                  FROM (SELECT G.MINGC GYSMC,\n" + 
			"                               KJ.MINGC KJMC,\n" + 
			"                               CZ.MINGC CZMC,\n" + 
			"                               DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
			"                          FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE FH.GONGYSB_ID = G.ID\n" + 
			"                           AND FH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND FH.FAZ_ID = CZ.ID\n" + 
			"                           AND FH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND FH.DAOHRQ >= TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd')\n" + 
			"                           AND FH.DAOHRQ < ADD_MONTHS(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),1))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) SHIGDY,\n" + 
			"               (SELECT GYSMC, KJMC, CZMC, SUM(LAIML) LAIML\n" + 
			"                  FROM (SELECT G.MINGC GYSMC,\n" + 
			"                               KJ.MINGC KJMC,\n" + 
			"                               CZ.MINGC CZMC,\n" + 
			"                               DECODE(KJ.MINGC, '市场采购', DECODE(G.MINGC, '龙煤集团', FH.BIAOZ, FH.JINGZ), FH.BIAOZ) LAIML\n" + 
			"                          FROM FAHB FH, GONGYSB G, JIHKJB KJ, CHEZXXB CZ\n" + 
			"                         WHERE FH.GONGYSB_ID = G.ID\n" + 
			"                           AND FH.JIHKJB_ID = KJ.ID\n" + 
			"                           AND FH.FAZ_ID = CZ.ID\n" + 
			"                           AND FH.DIANCXXB_ID = " + getTreeid() + "\n" + 
			"                           AND FH.DAOHRQ >= trunc(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),'yyyy')\n" + 
			"                           AND FH.DAOHRQ < ADD_MONTHS(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),1))\n" + 
			"                 GROUP BY GYSMC, KJMC, CZMC) SHIGLJ\n" + 
			"         WHERE SHIGDY.GYSMC(+) = SHIGLJ.GYSMC\n" + 
			"           AND SHIGDY.KJMC(+) = SHIGLJ.KJMC\n" + 
			"           AND SHIGDY.CZMC(+) = SHIGLJ.CZMC) LML\n" + 
			" WHERE JHL.GYSMC(+) = LML.GYSMC\n" + 
			"   AND JHL.KJMC(+) = LML.KJMC\n" + 
			"   AND JHL.CZMC(+) = LML.CZMC)\n" + 
			" GROUP BY GYSMC, CZMC, KJMC) SJ, GONGYSB G, CHEZXXB C, JIHKJB J\n" + 
			" WHERE SJ.GYSMC = G.MINGC\n" +
			"   AND SJ.CZMC = C.MINGC\n" +
			"   AND SJ.KJMC = J.MINGC\n" +
			"   AND G.LEIX = 1";
		
		ResultSetList rsl = con.getResultSetList(sql);
		StringBuffer buff = new StringBuffer("begin\n");
		
		if (hasData(riq_dangy, getTreeid())) {
			buff.append("DELETE FROM DIAOR02BB WHERE RIQ = TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd') AND DIANCXXB_ID = " + getTreeid() + ";\n");
		}
		
		while (rsl.next()) {
			buff.append("INSERT INTO DIAOR02BB(ID, DIANCXXB_ID, RIQ, GONGYSB_ID, FAZ_ID, JIHKJB_ID, DANGYJH, LEIJJH, DANGYSG, LEIJSG)\n")
			    .append("VALUES(GETNEWID(" + getTreeid() + "),\n")
			    .append(getTreeid()).append(",\n")
			    .append("TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),\n")
			    .append(rsl.getLong("gysmc")).append(",\n")
			    .append(rsl.getLong("czmc")).append(",\n")
			    .append(rsl.getLong("kjmc")).append(",\n")
			    .append(rsl.getDouble("dyjh")).append(",\n")
			    .append(rsl.getDouble("ljjh")).append(",\n")
			    .append(rsl.getDouble("dylm")).append(",\n")
			    .append(rsl.getDouble("ljlm")).append(");\n");
		}
		if (intMonth==1){
			sql = 
				"SELECT BENY.GONGYSB_ID,\n" +
				"       BENY.FAZ_ID,\n" + 
				"       BENY.JIHKJB_ID,\n" + 
				"       BENY.DANGYJH LEIJJH,\n" + 
				"       BENY.DANGYSG LEIJSG\n" + 
				"  FROM (SELECT *\n" + 
				"          FROM DIAOR02BB\n" + 
				"         WHERE RIQ = TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd')\n" + 
				"           AND DIANCXXB_ID = " + getTreeid() + ") BENY\n";
		}else{
			sql = 
				"SELECT BENY.GONGYSB_ID,\n" +
				"       BENY.FAZ_ID,\n" + 
				"       BENY.JIHKJB_ID,\n" + 
				"       NVL(SHANGY.LEIJJH, 0)+ BENY.DANGYJH LEIJJH,\n" + 
				"       NVL(SHANGY.LEIJSG, 0)+BENY.DANGYSG LEIJSG\n" + 
				"  FROM (SELECT *\n" + 
				"          FROM DIAOR02BB\n" + 
				"         WHERE RIQ = ADD_MONTHS(TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd'),-1)\n" + 
				"           AND DIANCXXB_ID = " + getTreeid() + ") SHANGY,\n" + 
				"       (SELECT *\n" + 
				"          FROM DIAOR02BB\n" + 
				"         WHERE RIQ = TO_DATE('" + riq_dangy + "', 'yyyy-mm-dd')\n" + 
				"           AND DIANCXXB_ID = " + getTreeid() + ") BENY\n" + 
				" WHERE BENY.GONGYSB_ID = SHANGY.GONGYSB_ID(+)\n" + 
				"   AND BENY.FAZ_ID = SHANGY.FAZ_ID(+)\n" + 
				"   AND BENY.JIHKJB_ID = SHANGY.JIHKJB_ID(+)";
		}
		
		
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			buff.append("UPDATE DIAOR02BB SET ")
				.append("LEIJJH = ").append(rsl.getDouble("LEIJJH")).append(", ")
				.append("LEIJSG = ").append(rsl.getDouble("LEIJSG")).append("\n ")
				.append(" WHERE GONGYSB_ID = ").append(rsl.getLong("gongysb_id"))
				.append(" AND FAZ_ID = ").append(rsl.getLong("faz_id"))
				.append(" AND JIHKJB_ID = ").append(rsl.getLong("jihkjb_id"))
				.append(" AND RIQ = TO_DATE('").append(riq_dangy).append("', 'yyyy-mm-dd')")
				.append(" AND DIANCXXB_ID = ").append(getTreeid()).append(";\n");
		}
		
		buff.append("end;");
		
		int flag = con.getUpdate(buff.toString());
		if (flag == -1) {
			this.setMsg("生成失败！");
		} else {
			this.setMsg("生成成功！");
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long year = getNianfValue().getId();
		long month = getYuefValue().getId();
		String riq_shangy = "";
		if (month == 1) {
			year = year - 1;
			month = 12;
			riq_shangy = year + "-" + month + "-01";
		} else {
			riq_shangy = year + "-" + (month - 1) + "-01";
		}
		
		String riq = getNianfValue().getValue() + "-" + getYuefValue().getValue() + "-01";
		String sql="";
		if(getYuefValue().getId()==1){
			 sql = 
				"SELECT DR.ID,\n" +
				"       DR.DIANCXXB_ID,\n" + 
				"       DR.RIQ,\n" + 
				"       G.MINGC GONGYSB_ID,\n" + 
				"       CZ.MINGC FAZ_ID,\n" + 
				"       KJ.MINGC JIHKJB_ID,\n" + 
				"       DR.DANGYJH,\n" + 
				"       0 SHANGYLJJH,\n" + 
				"       DR.LEIJJH,\n" + 
				"       DR.DANGYSG,\n" + 
				"       0 SHANGYLJSG,\n" + 
				"       DR.LEIJSG\n" + 
				"  FROM DIAOR02BB DR,\n" + 
				"       GONGYSB G,\n" + 
				"       CHEZXXB CZ,\n" + 
				"       JIHKJB KJ\n" + 
				" WHERE DR.GONGYSB_ID = G.ID\n" + 
				"   AND DR.FAZ_ID = CZ.ID\n" + 
				"   AND DR.JIHKJB_ID = KJ.ID\n" + 
				"   AND DR.DIANCXXB_ID = " + getTreeid() + "\n" + 
				"   AND DR.RIQ = TO_DATE('" + riq + "', 'yyyy-mm-dd')\n" + 
				" ORDER BY G.MINGC, CZ.MINGC, KJ.MINGC";
		}else{
			 sql = 
				"SELECT DR.ID,\n" +
				"       DR.DIANCXXB_ID,\n" + 
				"       DR.RIQ,\n" + 
				"       G.MINGC GONGYSB_ID,\n" + 
				"       CZ.MINGC FAZ_ID,\n" + 
				"       KJ.MINGC JIHKJB_ID,\n" + 
				"       DR.DANGYJH,\n" + 
				"       NVL(SHANGYSJ.LEIJJH, 0) SHANGYLJJH,\n" + 
				"       DR.LEIJJH,\n" + 
				"       DR.DANGYSG,\n" + 
				"       NVL(SHANGYSJ.LEIJSG, 0) SHANGYLJSG,\n" + 
				"       DR.LEIJSG\n" + 
				"  FROM DIAOR02BB DR,\n" + 
				"       (SELECT GONGYSB_ID, FAZ_ID, JIHKJB_ID, LEIJJH, LEIJSG\n" + 
				"          FROM DIAOR02BB\n" + 
				"         WHERE RIQ = TO_DATE('" + riq_shangy + "', 'yyyy-mm-dd')\n" + 
				"           AND DIANCXXB_ID = " + getTreeid() + ") SHANGYSJ,\n" + 
				"       GONGYSB G,\n" + 
				"       CHEZXXB CZ,\n" + 
				"       JIHKJB KJ\n" + 
				" WHERE DR.GONGYSB_ID = G.ID\n" + 
				"   AND DR.FAZ_ID = CZ.ID\n" + 
				"   AND DR.JIHKJB_ID = KJ.ID\n" + 
				"   AND DR.GONGYSB_ID = SHANGYSJ.GONGYSB_ID(+)\n" + 
				"   AND DR.FAZ_ID = SHANGYSJ.FAZ_ID(+)\n" + 
				"   AND DR.JIHKJB_ID = SHANGYSJ.JIHKJB_ID(+)\n" + 
				"   AND DR.DIANCXXB_ID = " + getTreeid() + "\n" + 
				"   AND DR.RIQ = TO_DATE('" + riq + "', 'yyyy-mm-dd')\n" + 
				" ORDER BY G.MINGC, CZ.MINGC, KJ.MINGC";
		}
		
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth("bodyWidth");
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(30);
		egu.setTableName("diaor02bb");
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("gongysb_id").setCenterHeader("供煤单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").editor = null;
		egu.getColumn("faz_id").setCenterHeader("发站");
		egu.getColumn("faz_id").editor = null;
		egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
		egu.getColumn("jihkjb_id").editor = null;
		egu.getColumn("dangyjh").setCenterHeader("当月计划");
		egu.getColumn("shangyljjh").setHidden(true);
		egu.getColumn("leijjh").setCenterHeader("累计计划");
		egu.getColumn("leijjh").editor = null;
		egu.getColumn("dangysg").setCenterHeader("当月实供");
		egu.getColumn("shangyljsg").setHidden(true);
		egu.getColumn("leijsg").setCenterHeader("累计实供");
		egu.getColumn("leijsg").editor = null;
		
	    ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, visit.getDiancxxb_id(), getTreeid());		
		setTree(etu);
		egu.addTbarText("电厂：");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		// 设置年份下拉框
		egu.addTbarText("年份:");
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		egu.addToolbarItem(nianf.getScript());
		egu.addTbarText("-");
		
		// 设置月份下拉框
		egu.addTbarText("月份:");
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		yuef.setValue(this.getRiq2());
		egu.addToolbarItem(yuef.getScript());
		egu.addTbarText("-");
		
		GridButton refurbish = new GridButton("刷新", "function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addToolbarItem("{" + refurbish.getScript() + "}");
		egu.addTbarText("-");
		
		GridButton create = new GridButton("生成", getBtnHandlerScript("CreateButton"));
		create.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(create);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.on('afteredit', AutoCount);\n" + 
						   "function AutoCount(e) {\n" + 
						   "	e.record.set('LEIJJH', Round(eval(e.record.get('DANGYJH') || 0) + eval(e.record.get('SHANGYLJJH') || 0), 2));\n" + 
						   "	e.record.set('LEIJSG', Round(eval(e.record.get('DANGYSG') || 0) + eval(e.record.get('SHANGYLJSG') || 0), 2));\n" + 
						   "}"		
		);
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = this.getNianfValue().getValue() + "年"
				+ this.getYuefValue().getValue() + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append(cnDate).append("数据将被覆盖").append("，是否继续？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setMsg(null);
			setNianfValue(null);
			getNianfModels();
			setYuefValue(null);
			getYuefModels();
		}
		
		getSelectData();
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
		for (i = 2011; i <= DateUtil.getYear(new Date()) + 1; i++) {
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

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
//			设定月份下拉框为当前月份的上月
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

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getRiq1() {
		String year = "" + (Calendar.YEAR + 1900);
		return year;
	}

	public String getRiq2() {
		String month = "" + (Calendar.MONTH + 1);
		return month;
	}

	public void setRiq1(String riq1) {
		if (((Visit) this.getPage().getVisit()).getString5() != null
				&& !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	
// =========================电厂树==============
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	private String treeid = "";

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {
			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
}