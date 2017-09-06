package com.zhiren.dc.monthReport;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 
 * @author 夏峥
 * @since 2012-04-09
 * @version V0.1
 * @discription 基于Yuejsdj.java 1.1.1.10和Yuercbmdjb.java 1.1.1.9修改而成的月入厂标煤单价生成界面
 */

public class Yuercdjsc extends BasePage implements PageValidateListener {
	public static final String strParam = "strtime";
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		msg = "";
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
	}

	private void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = "";
		String CurrODate = "";

		CurrZnDate = strDate[0] + "年" + strDate[1] + "月";
		CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]+ "-01");

		StringBuffer strSql = new StringBuffer();
		strSql.append("begin \n");
		strSql.append("delete from yuercbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="+ CurrODate + " and diancxxb_id=" + diancxxb_id+");\n");
		strSql.append("end;");
		int flag = con.getDelete(strSql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"	+ strSql.toString());
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}

	public void CreateData() {
		/* 首先删除当月数据 */
		DelData();
		
		String diancxxb_id = getTreeid(); // 电厂ID
		StringBuffer sql=new StringBuffer();
		JDBCcon con = new JDBCcon();

		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = "";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]	+ "-01");
		String year=DateUtil.FormatOracleDate(strDate[0] + "-01-01");
		String Riq1 = DateUtil.FormatOracleDate(this.getRiq1());
		String Riq2 = DateUtil.FormatOracleDate(this.getRiq2());
		CurrZnDate = strDate[0] + "年" + strDate[1] + "月";

		sql.append("INSERT INTO yuercbmdj (ID,fenx,yuetjkjb_id,rucsl,zhuangt)\n" + 
					"SELECT getnewid("+diancxxb_id+"),sl.fenx,t.id,sl.jingz+sl.yuns,0\n" + 
					"FROM yuetjkjb t, yueslb sl\n" + 
					"WHERE t.id=sl.yuetjkjb_id AND t.diancxxb_id="+diancxxb_id+"\n" + 
					"AND t.riq="+CurrODate+"\n" + 
					"ORDER BY t.id");

//		写入表头信息
		int Flag=con.getInsert(sql.toString());
		if(Flag<0){
			setMsg(CurrZnDate + "的表头数据生成失败！");
			con.Close();
			return;
		}
//		计算本月来煤且结算入账的信息
		Flag=CountJS(con, Riq1,Riq2,diancxxb_id,CurrODate, SysConstant.Fenx_Beny);
		if(Flag<0){
			setMsg(CurrZnDate + "的来煤且结算入账的信息生成失败！");
			con.Close();
			return;
		}
////		计算本年至当月来煤且结算入账的信息
//		Flag=CountJS(con, year,Riq2,diancxxb_id,CurrODate, SysConstant.Fenx_Leij);
//		if(Flag<0){
//			setMsg("本年至"+CurrZnDate+"来煤且结算入账的信息生成失败！");
//			con.Close();
//			return;
//		}
		
//		计算本月来煤且未结算入账的信息
		Flag=CountGS(con, Riq1,Riq2,diancxxb_id,CurrODate, SysConstant.Fenx_Beny);
		if(Flag<0){
			setMsg(CurrZnDate + "的来煤且未结算入账的信息生成失败！");
			con.Close();
			return;
		}
////		计算本年至当月来煤且未结算入账的信息
//		Flag=CountGS(con, year,Riq2,diancxxb_id,CurrODate, SysConstant.Fenx_Leij);
//		if(Flag<0){
//			setMsg("本年至"+CurrZnDate+"来煤且未结算入账的信息生成失败！");
//			con.Close();
//			return;
//		}
		
//		更新本月入厂标煤单价信息
		sql.setLength(0);
		sql.append("SELECT yuetjkjb_id,fenx,rcrl,rcmj,round(rcmj-rcmj/1.17,2)rcmjs,rcyj,round(rcyj*0.07,2)rcyjs,rczf,rczfs,\n" +
					"DECODE(rc.rcrl,0,0,ROUND((rc.rcmj+rc.rcyj+rc.rczf)*29.271/rc.rcrl,2))rcbmdj,\n" + 
					"DECODE(rc.rcrl,0,0,ROUND((rc.rcmj+rc.rcyj+rc.rczf-round(rcmj-rcmj/1.17,2)-round(rcyj*0.07,2)-rc.rczfs)*29.271/rc.rcrl,2))rcbhsbmdj\n" + 
					"FROM (SELECT js.yuetjkjb_id,js.fenx,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusrl+jiesl*jiesrl)/(gusl+jiesl))rcrl,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusmj+jiesl*jiesmj)/(gusl+jiesl))rcmj,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusmjs+jiesl*jiesmjs)/(gusl+jiesl))rcmjs,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusyj+jiesl*jiesyj)/(gusl+jiesl))rcyj,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusyjs+jiesl*jiesyjs)/(gusl+jiesl))rcyjs,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*guszf+jiesl*jieszf)/(gusl+jiesl))rczf,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*guszfs+jiesl*jieszfs)/(gusl+jiesl))rczfs\n" + 
					"  FROM YUERCBMDJ JS, YUETJKJB T\n" + 
					" WHERE DIANCXXB_ID = "+ diancxxb_id+" AND JS.FENX='"+SysConstant.Fenx_Beny+"'\n" + 
					"   AND T.ID = JS.YUETJKJB_ID\n" + 
					"   AND T.RIQ = "+CurrODate+")rc");
		ResultSetList rs = con.getResultSetList(sql.toString());
		StringBuffer sb=new StringBuffer();
		sb.append("begin\n");
		while(rs.next()){
//			逐条更新信息
			sb.append("UPDATE YUERCBMDJ\n" +
					"   SET RUCRL      = "+rs.getDouble("RCRL")+",\n" + 
					"       RUCMJ      = "+rs.getDouble("RCMJ")+",\n" + 
					"       RUCMJS     = "+rs.getDouble("RCMJS")+",\n" + 
					"       RUCYJ      = "+rs.getDouble("RCYJ")+",\n" + 
					"       RUCYJS     = "+rs.getDouble("RCYJS")+",\n" + 
					"       RUCZF      = "+rs.getDouble("RCZF")+",\n" + 
					"       RUCZFS     = "+rs.getDouble("RCZFS")+",\n" + 
					"       RUCBMDJ    = "+rs.getDouble("RCBMDJ")+",\n" + 
					"       RUCBHSBMDJ = "+rs.getDouble("RCBHSBMDJ")+"\n" + 
					" WHERE FENX = '"+rs.getString("FENX")+"'\n" + 
					"   AND YUETJKJB_ID = "+rs.getString("YUETJKJB_ID")+";");
		}
		sb.append("end;");
		if(sb.length()>20){
			Flag=con.getUpdate(sb.toString());
		}
		rs.close();
		if(Flag<0){
			setMsg(CurrZnDate+"入厂标煤单价信息生成失败！");
			con.Close();
			return;
		}
		
//		计算并更新结算单价和月入厂标煤单价的累积信息
		Flag=CountLJ(con,diancxxb_id,year,CurrODate);
		if(Flag<0){
			setMsg(CurrZnDate+"累积信息更新失败！");
			con.Close();
			return;
		}else{
			setMsg(CurrZnDate+"入厂标煤单价信息生成成功！");
			con.Close();
		}
	}
	
//	计算并更新结算信息
	private int CountJS(JDBCcon con, String Riq1,String Riq2,String diancxxb_id,String CurrODate, String Fenx){
		StringBuffer sql=new StringBuffer();
		String yunj = "decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.guotyf,0)+nvl(jy.kuangqyf,0))/sum(nvl(jy.jiessl,0)),2)) as yunj,\n";
		if (MainGlobal.getXitxx_item("数量", "是否重新关联煤款结算单对应的运费信息", 
			String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//			由于国电青铝电厂的煤款和运费是分开结算的，并且可能一张煤款结算单对应多个运输单位，那么还会有多个运费结算单，
//			这种情况比较特殊，所以需要在此重新关联出运费信息
			yunj = "decode(sum(getYunfxx4Jiesbid(j.id, 'guotyf')), 0, 0, round(sum(getYunfxx4Jiesbid(j.id, 'guotyf')+nvl(jy.kuangqyf,0)) / sum(nvl(jy.jiessl, 0)), 2)) as yunj,\n";
		}

		sql.append( 
			"select gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,jiesl,meij, ROUND(meij-meij/1.17,2) meijs,\n" +
			"yunj, ROUND(yunj*0.07,2) yunjs,zaf,qnet_ar from(select gys.id gongysb_id, f.jihkjb_id,f.pinzb_id, f.yunsfsb_id,\n" +
			"round(sum(f.jingz),2) as jiesl,\n" + 
			"decode(sum(j.jiessl),0,0,round(sum(j.hansmk)/sum(j.jiessl),2)) as meij,\n" + 
			"decode(sum(j.jiessl),0,0,round(sum(j.shuik)/sum(j.jiessl),2)) as meijs,\n" + 
			yunj + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.shuik,0))/sum(nvl(jy.jiessl,0)),2)) as yunjs,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.guotzf,0)+nvl(jy.kuangqzf,0))/ sum(nvl(j.jiessl,0)),2)) as zaf,\n" + 
			"decode(sum(j.jiessl),0,0,round(sum(j.jiessl*j.jiesrl*0.0041816)/sum(j.jiessl),3)) as qnet_ar\n" + 
			"from jiesb j, jiesyfb jy,fahb f,meikxxb mk, gongysb gys\n" + 
			"where j.id = f.jiesb_id and j.id = jy.diancjsmkb_id(+) AND f.meikxxb_id=mk.id AND mk.meikdq_id=gys.id\n" + 
			" and f.diancxxb_id="+ diancxxb_id+"\n"+
			"and j.ruzrq BETWEEN "+Riq1+" AND "+Riq2+"\n" +
			"and f.daohrq BETWEEN "+Riq1+" AND "+Riq2+"\n" +
			"group by (gys.id, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id))");
		
		ResultSetList rs = con.getResultSetList(sql.toString());
		StringBuffer sb=new StringBuffer();
		sb.append("begin\n");
		while(rs.next()){
//			逐条更新信息
			sb.append(
					"UPDATE (SELECT RC.*\n" + 
					"                  FROM YUETJKJB T, YUERCBMDJ RC\n" + 
					"                 WHERE T.DIANCXXB_ID = "+diancxxb_id+"\n" + 
					"                   AND T.ID = RC.YUETJKJB_ID\n" + 
					"                   AND RC.RUCSL > 0\n" + 
					"                   AND T.RIQ = "+CurrODate+"\n" + 
					"                   AND RC.FENX = '"+Fenx+"'\n" + 
					"                   AND T.GONGYSB_ID = "+rs.getString("gongysb_id")+"\n" + 
					"                   AND T.JIHKJB_ID = "+rs.getString("JIHKJB_ID")+"\n" + 
					"                   AND T.PINZB_ID = "+rs.getString("PINZB_ID")+"\n" + 
					"                   AND T.YUNSFSB_ID = "+rs.getString("YUNSFSB_ID")+")\n" + 
					"   SET JIESL   = "+rs.getDouble("jiesl")+",\n" + 
					"       JIESRL  = "+rs.getDouble("qnet_ar")+",\n" + 
					"       JIESMJ  = "+rs.getDouble("meij")+",\n" + 
					"       JIESMJS = "+rs.getDouble("meijs")+",\n" + 
					"       JIESYJ  = "+rs.getDouble("yunj")+",\n" + 
					"       JIESYJS = "+rs.getDouble("yunjs")+",\n" + 
					"       JIESZF  = "+rs.getDouble("zaf")+";\n");
		}
		sb.append("end;");
		
		int Flag=0;
		if(sb.length()>20){
			Flag=con.getUpdate(sb.toString());
		}
		rs.close();
		return Flag;
	}
	
	
//	计算并更新估收信息
	private int CountGS(JDBCcon con, String Riq1,String Riq2,String diancxxb_id,String CurrODate, String Fenx){
//		取得已到货且未结算的信息，其中数量信息为来煤数量-结算数量
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT RC.ID,\n" +
		"       RC.RUCSL - RC.JIESL RUCSL,\n" + 
		"       GUS.RUCRL,\n" + 
		"       GUS.RUCMJ,\n" + 
		"       ROUND((GUS.RUCMJ-GUS.RUCMJ/1.17),2) RUCMJS,\n" + 
		"       GUS.RUCYJ,\n" + 
		"       ROUND(GUS.RUCYJ*0.07,2) RUCYJS,\n" + 
		"       GUS.RUCZF\n" + 
		"  FROM (SELECT GYS.ID GONGYSB_ID,\n" + 
		"               FH.JIHKJB_ID,\n" + 
		"               FH.PINZB_ID,\n" + 
		"               FH.YUNSFSB_ID,\n" + 
		"               ROUND(SUM(FH.JINGZ), 2) JINGZ,\n" + 
		"               ROUND(DECODE(SUM(FH.JINGZ),0,0,SUM(FH.JINGZ * ZL.QNET_AR) / SUM(FH.JINGZ)),3) RUCRL,\n" + 
		"               ROUND(DECODE(SUM(FH.JINGZ),0,0,SUM(FH.JINGZ * FH.MEIJ) / SUM(FH.JINGZ)),2) RUCMJ,\n" + 
		"               ROUND(DECODE(SUM(FH.JINGZ),0,0,SUM(FH.JINGZ * FH.MEIJS) / SUM(FH.JINGZ)),2) RUCMJS,\n" + 
		"               ROUND(DECODE(SUM(FH.JINGZ),0,0,SUM(FH.JINGZ * FH.YUNJ) / SUM(FH.JINGZ)),2) RUCYJ,\n" + 
		"               ROUND(DECODE(SUM(FH.JINGZ),0,0,SUM(FH.JINGZ * FH.YUNJS) / SUM(FH.JINGZ)),2) RUCYJS,\n" + 
		"               ROUND(DECODE(SUM(FH.JINGZ),0,0,SUM(FH.JINGZ * FH.ZAF) / SUM(FH.JINGZ)),2) RUCZF\n" + 
		"          FROM (SELECT F.ID,\n" + 
		"                       F.ZHILB_ID,\n" + 
		"                       F.MEIKXXB_ID,\n" + 
		"                       F.JIHKJB_ID,\n" + 
		"                       F.PINZB_ID,\n" + 
		"                       F.YUNSFSB_ID,\n" + 
		"                       F.LAIMSL JINGZ,\n" + 
		"                       NVL(GS.MEIJ, 0) + NVL(GS.MEIS, 0) MEIJ,\n" + 
		"                       NVL(GS.MEIS, 0) MEIJS,\n" + 
		"                       NVL(GS.YUNF, 0) + NVL(GS.YUNFS, 0) YUNJ,\n" + 
		"                       NVL(GS.YUNFS, 0) YUNJS,\n" + 
		"                       NVL(GS.ZAF, 0) ZAF\n" + 
		"                  FROM FAHB F,\n" + 
		"                       (SELECT GS.FAHB_ID,\n" + 
		"                               GS.MEIJ,\n" + 
		"                               GS.MEIS,\n" + 
		"                               GS.FAZZF,\n" + 
		"                               GS.YUNF,\n" + 
		"                               GS.YUNFS,\n" + 
		"                               GS.ZAF,\n" + 
		"                               GS.DITF\n" + 
		"                          FROM (SELECT MAX(LEIX) LEIX, MAX(ID) ID, FAHB_ID\n" + 
		"                                  FROM GUSLSB\n" + 
		"                                 GROUP BY FAHB_ID) G,\n" + 
		"                               GUSLSB GS\n" + 
		"                         WHERE G.ID = GS.ID) GS,\n" + 
		"                       JIESB J\n" + 
		"                 WHERE GS.FAHB_ID(+) = F.ID\n" + 
		"                   AND F.JIESB_ID = J.ID(+)\n" + 
		"                   AND J.RUZRQ IS NULL\n" + 
		"                   AND F.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                   AND F.DAOHRQ BETWEEN "+Riq1+" AND "+Riq2+") FH,\n" + 
		"               GONGYSB GYS,\n" + 
		"               MEIKXXB MK,\n" + 
		"               ZHILB ZL\n" + 
		"         WHERE FH.MEIKXXB_ID = MK.ID\n" + 
		"           AND MK.MEIKDQ_ID = GYS.ID\n" + 
		"           AND FH.ZHILB_ID = ZL.ID\n" + 
		"         GROUP BY GYS.ID, FH.JIHKJB_ID, FH.PINZB_ID, FH.YUNSFSB_ID) GUS,\n" + 
		"       YUETJKJB T,\n" + 
		"       YUERCBMDJ RC\n" + 
		" WHERE T.ID = RC.YUETJKJB_ID\n" + 
		"   AND RC.FENX = '"+Fenx+"'\n" + 
		"   AND T.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"   AND T.RIQ = "+CurrODate+"\n" + 
		"   AND RC.RUCSL > 0\n" + 
		"   AND T.GONGYSB_ID = GUS.GONGYSB_ID\n" + 
		"   AND T.JIHKJB_ID = GUS.JIHKJB_ID\n" + 
		"   AND T.PINZB_ID = GUS.PINZB_ID\n" + 
		"   AND T.YUNSFSB_ID = GUS.YUNSFSB_ID");

		ResultSetList rs = con.getResultSetList(sql.toString());
		StringBuffer sb=new StringBuffer();
		sb.append("begin\n");
		while(rs.next()){
//			逐条更新信息
			sb.append(
					"UPDATE YUERCBMDJ \n" + 
					"   SET GUSL   = "+rs.getDouble("RUCSL")+",\n" + 
					"       GUSRL  = "+rs.getDouble("RUCRL")+",\n" + 
					"       GUSMJ  = "+rs.getDouble("RUCMJ")+",\n" + 
					"       GUSMJS = "+rs.getDouble("RUCMJS")+",\n" + 
					"       GUSYJ  = "+rs.getDouble("RUCYJ")+",\n" + 
					"       GUSYJS = "+rs.getDouble("RUCYJS")+",\n" + 
					"       GUSZF  = "+rs.getDouble("RUCZF")+" WHERE ID="+rs.getString("ID")+";\n");
		}
		sb.append("end;");
		int Flag=0;
		if(sb.length()>20){
			Flag=con.getUpdate(sb.toString());
		}
		rs.close();
		return Flag;
	}
	
	
//	更新结算单价表中的累积数据后，根据结算单价表中的累积数据更新相应的月入厂标煤单价表中的累积数据
	private int CountLJ(JDBCcon con,String diancxxb_id,String year,String CurrODate){
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT ID,JIESL,JIESRL,JIESMJ,ROUND(JIESMJ-JIESMJ/1.17,2)JIESMJS,JIESYJ,ROUND(JIESYJ*0.07)JIESYJS,JIESZF,JIESZFS,\n" +
		"GUSL,GUSRL,GUSMJ,ROUND(GUSMJ-GUSMJ/1.17,2)GUSMJS,GUSYJ,ROUND(GUSYJS*0.07)GUSYJS,GUSZF,GUSZFS\n" + 
		"FROM\n" + 
		"(SELECT T.ID,\n" + 
		"       ROUND(SUM(GS.JIESL), 2) JIESL,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESRL)/SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),2))JIESRL,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESMJ)/SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),2))JIESMJ,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESMJS)/SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),2))JIESMJS,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESMJ, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESYJ)/SUM(DECODE(GS.JIESMJ, 0, 0, GS.JIESL)),2))JIESYJ,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESMJ, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESYJS)/SUM(DECODE(GS.JIESMJ, 0, 0, GS.JIESL)),2))JIESYJS,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESZF)/SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),2))JIESZF,\n" + 
		"       DECODE(SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),0,0,ROUND(SUM(GS.JIESL * GS.JIESZFS)/SUM(DECODE(GS.JIESRL, 0, 0, GS.JIESL)),2))JIESZFS,\n" + 
		"       ROUND(SUM(GS.GUSL), 2) GUSL,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSRL)/SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),2)) GUSRL,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSMJ)/SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),2))GUSMJ,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSMJS)/SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),2))GUSMJS,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSMJ, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSYJ)/SUM(DECODE(GS.GUSMJ, 0, 0, GS.GUSL)),2))GUSYJ,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSMJ, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSYJS)/SUM(DECODE(GS.GUSMJ, 0, 0, GS.GUSL)),2))GUSYJS,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSZF)/SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),2))GUSZF,\n" + 
		"       DECODE(SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),0,0,ROUND(SUM(GS.GUSL * GS.GUSZFS)/SUM(DECODE(GS.GUSRL, 0, 0, GS.GUSL)),2))GUSZFS\n" + 
		"  FROM YUERCBMDJ GS,\n" + 
		"       YUETJKJB T1,\n" + 
		"       (SELECT *\n" + 
		"          FROM YUETJKJB\n" + 
		"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"           AND RIQ = "+CurrODate+") T\n" + 
		" WHERE GS.YUETJKJB_ID = T1.ID\n" + 
		"   AND GS.FENX = '本月'\n" + 
		"   AND T1.GONGYSB_ID = T.GONGYSB_ID\n" + 
		"   AND T1.JIHKJB_ID = T.JIHKJB_ID\n" + 
		"   AND T1.PINZB_ID = T.PINZB_ID\n" + 
		"   AND T1.YUNSFSB_ID = T.YUNSFSB_ID\n" + 
		"   AND T1.RIQ BETWEEN "+year+" AND "+CurrODate+"\n" + 
		"   GROUP BY T.ID)");

		ResultSetList rs = con.getResultSetList(sql.toString());
		StringBuffer sb=new StringBuffer();
		sb.append("begin\n");
		while(rs.next()){
//			逐条更新信息
			sb.append(
					"UPDATE (SELECT * FROM YUERCBMDJ GS WHERE GS.YUETJKJB_ID = "+rs.getString("ID")+" AND GS.FENX='"+SysConstant.Fenx_Leij+"')\n" + 
					"   SET GUSL   = "+rs.getDouble("GUSL")+",\n" + 
					"       GUSRL  = "+rs.getDouble("GUSRL")+",\n" + 
					"       GUSMJ  = "+rs.getDouble("GUSMJ")+",\n" + 
					"       GUSMJS = "+rs.getDouble("GUSMJS")+",\n" + 
					"       GUSYJ  = "+rs.getDouble("GUSYJ")+",\n" + 
					"       GUSYJS = "+rs.getDouble("GUSYJS")+",\n" + 
					"       GUSZF  = "+rs.getDouble("GUSZF")+",\n"+
					"       GUSZFS  = "+rs.getDouble("GUSZFS")+",\n"+
					"		JIESL   = "+rs.getDouble("JIESL")+",\n" +
					"		JIESRL  = "+rs.getDouble("JIESRL")+",\n" + 
					"		JIESMJ  = "+rs.getDouble("JIESMJ")+",\n" + 
					"		JIESMJS = "+rs.getDouble("JIESMJS")+",\n" + 
					"		JIESYJ  = "+rs.getDouble("JIESYJ")+",\n" + 
					"		JIESYJS = "+rs.getDouble("JIESYJS")+",\n" + 
					"		JIESZF  = "+rs.getDouble("JIESZF")+",\n" + 
					"		JIESZFS = "+rs.getDouble("JIESZFS")+";\n");
		}
		sb.append("end;");
		int Flag=0;
		if(sb.length()>20){
			Flag=con.getUpdate(sb.toString());
		}
		if(Flag<0){
			rs.close();
			return -1;
		}
		
//		更新累积入厂标煤单价信息
		sql.setLength(0);
		sb.setLength(0);
		sql.append("SELECT yuetjkjb_id,fenx,RCSL,rcrl,rcmj,round(rcmj-rcmj/1.17,2)rcmjs,rcyj,round(rcyj*0.07,2)rcyjs,rczf,rczfs,\n" +
					"DECODE(rc.rcrl,0,0,ROUND((rc.rcmj+rc.rcyj+rc.rczf)*29.271/rc.rcrl,2))rcbmdj,\n" + 
					"DECODE(rc.rcrl,0,0,ROUND((rc.rcmj+rc.rcyj+rc.rczf-round(rcmj-rcmj/1.17,2)-round(rcyj*0.07,2)-rc.rczfs)*29.271/rc.rcrl,2))rcbhsbmdj\n" + 
					"FROM (SELECT js.yuetjkjb_id,js.fenx,\n" + 
					"(gusl+jiesl) rcSl,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusrl+jiesl*jiesrl)/(gusl+jiesl))rcrl,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusmj+jiesl*jiesmj)/(gusl+jiesl))rcmj,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusmjs+jiesl*jiesmjs)/(gusl+jiesl))rcmjs,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusyj+jiesl*jiesyj)/(gusl+jiesl))rcyj,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*gusyjs+jiesl*jiesyjs)/(gusl+jiesl))rcyjs,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*guszf+jiesl*jieszf)/(gusl+jiesl))rczf,\n" + 
					"DECODE((gusl+jiesl),0,0, (gusl*guszfs+jiesl*jieszfs)/(gusl+jiesl))rczfs\n" + 
					"  FROM YUERCBMDJ JS, YUETJKJB T\n" + 
					" WHERE DIANCXXB_ID = "+ diancxxb_id+" AND JS.FENX='"+SysConstant.Fenx_Leij+"'\n" + 
					"   AND T.ID = JS.YUETJKJB_ID\n" + 
					"   AND T.RIQ = "+CurrODate+")rc");
		rs = con.getResultSetList(sql.toString());
		sb.append("begin\n");
		while(rs.next()){
//			逐条更新信息
			sb.append("UPDATE YUERCBMDJ\n" +
					"   SET RUCRL      = "+rs.getDouble("RCRL")+",\n" + 
					"       RUCSL      = "+rs.getDouble("RCSL")+",\n" + 
					"       RUCMJ      = "+rs.getDouble("RCMJ")+",\n" + 
					"       RUCMJS     = "+rs.getDouble("RCMJS")+",\n" + 
					"       RUCYJ      = "+rs.getDouble("RCYJ")+",\n" + 
					"       RUCYJS     = "+rs.getDouble("RCYJS")+",\n" + 
					"       RUCZF      = "+rs.getDouble("RCZF")+",\n" + 
					"       RUCZFS     = "+rs.getDouble("RCZFS")+",\n" + 
					"       RUCBMDJ    = "+rs.getDouble("RCBMDJ")+",\n" + 
					"       RUCBHSBMDJ = "+rs.getDouble("RCBHSBMDJ")+"\n" + 
					" WHERE FENX = '"+rs.getString("FENX")+"'\n" + 
					"   AND YUETJKJB_ID = "+rs.getString("YUETJKJB_ID")+";");
		}
		sb.append("end;");
		if(sb.length()>20){
			Flag=con.getUpdate(sb.toString());
		}
		if(Flag<0){
			rs.close();
			return -1;
		}
		rs.close();
		return Flag;
	}
	

	private void Save() {
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb=new StringBuffer();
		
		String diancxxb_id = getTreeid(); // 电厂ID
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = strDate[0] + "年" + strDate[1] + "月";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]	+ "-01");
		String year=DateUtil.FormatOracleDate(strDate[0] + "-01-01");
		
//		保存界面中修改的全部信息
		sb.append("begin \n");
		while (rs.next()){
			sb.append("UPDATE YUERCBMDJ \n" + 
					"   SET GUSL   = "+rs.getDouble("GUSL")+",\n" + 
					"       GUSRL  = "+rs.getDouble("GUSRL")+",\n" + 
					"       GUSMJ  = "+rs.getDouble("GUSMJ")+",\n" + 
					"       GUSMJS = "+rs.getDouble("GUSMJS")+",\n" + 
					"       GUSYJ  = "+rs.getDouble("GUSYJ")+",\n" + 
					"       GUSYJS = "+rs.getDouble("GUSYJS")+",\n" + 
					"       GUSZF  = "+rs.getDouble("GUSZF")+",\n"+
					"       GUSZFS  = "+rs.getDouble("GUSZFS")+",\n"+
					"		JIESL   = "+rs.getDouble("JIESL")+",\n" +
					"		JIESRL  = "+rs.getDouble("JIESRL")+",\n" + 
					"		JIESMJ  = "+rs.getDouble("JIESMJ")+",\n" + 
					"		JIESMJS = "+rs.getDouble("JIESMJS")+",\n" + 
					"		JIESYJ  = "+rs.getDouble("JIESYJ")+",\n" + 
					"		JIESYJS = "+rs.getDouble("JIESYJS")+",\n" + 
					"		JIESZF  = "+rs.getDouble("JIESZF")+",\n" + 
					"		JIESZFS = "+rs.getDouble("JIESZFS")+",\n"+
					"   	RUCRL      = "+rs.getDouble("RUCRL")+",\n" +
					"		RUCMJ      = "+rs.getDouble("RUCMJ")+",\n" + 
					"       RUCMJS     = "+rs.getDouble("RUCMJS")+",\n" + 
					"       RUCYJ      = "+rs.getDouble("RUCYJ")+",\n" + 
					"       RUCYJS     = "+rs.getDouble("RUCYJS")+",\n" + 
					"       RUCZF      = "+rs.getDouble("RUCZF")+",\n" + 
					"       RUCZFS     = "+rs.getDouble("RUCZFS")+",\n" + 
					"       RUCBMDJ    = "+rs.getDouble("RUCBMDJ")+",\n" + 
					"       RUCBHSBMDJ = "+rs.getDouble("RUCBHSBMDJ")+"\n" + 
					" 		WHERE ID = "+rs.getString("RCID")+" ;\n");
		}
		sb.append("end;");
		int flag=0;
		if(sb.length()>20){
			flag=con.getUpdate(sb.toString());
		}
		if(flag<0){
			setMsg("保存"+CurrZnDate+"时,本月信息更新失败！");
			con.Close();
			return;
		}
		
//		计算并更新结算单价和月入厂标煤单价的累积信息
		flag=CountLJ(con,diancxxb_id,year,CurrODate);
		if(flag<0){
			setMsg("保存"+CurrZnDate+"时,累积信息更新失败！");
			con.Close();
			return;
		}else{
			setMsg(CurrZnDate + "的数据保存生成！");
		}
		con.Close();
	}
	
	/**
	 * @param con
	 * @return true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	private boolean getZhangt(JDBCcon con) {
		String CurrODate = "";
		String sj[] = this.getRiq2().split("-");
		CurrODate = DateUtil.FormatOracleDate(sj[0] + "-" + sj[1] + "-01");

		String sql = "select max(s.zhuangt) zhuangt\n"
				+ "  from YUERCBMDJ s, yuetjkjb t\n"
				+ " where t.id=s.yuetjkjb_id AND t.diancxxb_id =  "+ getTreeid() + "\n" 
				+ "   and t.riq = " + CurrODate;
		ResultSetList rs = con.getResultSetList(sql);
		boolean zt = true;
		if (con.getHasIt(sql)) {
			while (rs.next()) {
				if (rs.getInt("zhuangt") == 0 || rs.getInt("zhuangt") == 2) {
					zt = false;
				}
			}
		} else {
			zt = false;
		}
		return zt;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strDate1[] = this.getRiq2().split("-");
		String strDate = strDate1[0] + "-" + strDate1[1] + "-01";
		String strSql = "";
		String diancxxb_id = this.getTreeid();
		strSql = "select id from yueshchjb where riq = TO_DATE('"+strDate+"', 'yyyy-mm-dd') and diancxxb_id ="+diancxxb_id;
		boolean isReady = !con.getHasIt(strSql);
		if(isReady) {
			setMsg("请在使用本模块之前，首先完成本月耗存合计数据的计算！");
		}
		strSql =
			"SELECT * FROM (\n" +
			"SELECT -1 RCID,\n" + 
			"       GYS.MINGC GONGYSB_ID,\n" + 
			"       JH.MINGC JIHKJB_ID,\n" + 
			"       PZ.MINGC PINZB_ID,\n" + 
			"       YS.MINGC YUNSFSB_ID,\n" + 
			"       RC.FENX||'合计' FENX,\n" + 
			"       ROUND(SUM(RC.JIESL), 2) JIESL,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESRL)/SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),2))JIESRL,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESMJ)/SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),2))JIESMJ,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESMJS)/SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),2))JIESMJS,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESMJ, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESYJ)/SUM(DECODE(RC.JIESMJ, 0, 0, RC.JIESL)),2))JIESYJ,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESMJ, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESYJS)/SUM(DECODE(RC.JIESMJ, 0, 0, RC.JIESL)),2))JIESYJS,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESZF)/SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),2))JIESZF,\n" + 
			"       DECODE(SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),0,0,ROUND(SUM(RC.JIESL * RC.JIESZFS)/SUM(DECODE(RC.JIESRL, 0, 0, RC.JIESL)),2))JIESZFS,\n" + 
			"       ROUND(SUM(RC.GUSL), 2) GUSL,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSRL)/SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),2)) GUSRL,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSMJ)/SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),2))GUSMJ,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSMJS)/SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),2))GUSMJS,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSMJ, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSYJ)/SUM(DECODE(RC.GUSMJ, 0, 0, RC.GUSL)),2))GUSYJ,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSMJ, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSYJS)/SUM(DECODE(RC.GUSMJ, 0, 0, RC.GUSL)),2))GUSYJS,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSZF)/SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),2))GUSZF,\n" + 
			"       DECODE(SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),0,0,ROUND(SUM(RC.GUSL * RC.GUSZFS)/SUM(DECODE(RC.GUSRL, 0, 0, RC.GUSL)),2))GUSZFS,\n" + 
			"       ROUND(SUM(RC.RUCSL), 2) RUCSL,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCRL)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCRL,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCMJ)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCMJ,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCMJS)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCMJS,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCMJ, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCYJ)/SUM(DECODE(RC.RUCMJ, 0, 0, RC.RUCSL)),2))RUCYJ,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCMJ, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCYJS)/SUM(DECODE(RC.RUCMJ, 0, 0, RC.RUCSL)),2))RUCYJS,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCZF)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCZF,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCZFS)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCZFS,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCBMDJ)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCBMDJ,\n" + 
			"       DECODE(SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),0,0,ROUND(SUM(RC.RUCSL * RC.RUCBHSBMDJ)/SUM(DECODE(RC.RUCRL, 0, 0, RC.RUCSL)),2))RUCBHSBMDJ\n" + 
			"  FROM YUERCBMDJ RC,\n" + 
			"       YUETJKJB  T,\n" + 
			"       GONGYSB   GYS,\n" + 
			"       JIHKJB    JH,\n" + 
			"       YUNSFSB   YS,\n" + 
			"       PINZB     PZ\n" + 
			" WHERE T.ID = RC.YUETJKJB_ID\n" + 
			"   AND T.GONGYSB_ID = GYS.ID\n" + 
			"   AND T.JIHKJB_ID = JH.ID\n" + 
			"   AND T.YUNSFSB_ID = YS.ID\n" + 
			"   AND T.PINZB_ID = PZ.ID\n" + 
			"   AND T.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND T.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n" + 
			"   GROUP BY  ROLLUP  (RC.FENX,(GYS.MINGC,JH.MINGC ,PZ.MINGC ,YS.MINGC))\n" + 
			"   HAVING GROUPING(RC.FENX)+GROUPING(GYS.MINGC)=1\n" + 
			"   ORDER BY FENX)\n" + 
			"   UNION ALL\n" + 
			"   SELECT * FROM (SELECT RC.ID         RCID,\n" + 
			"               GYS.MINGC     GONGYSB_ID,\n" + 
			"               JH.MINGC      JIHKJB_ID,\n" + 
			"               PZ.MINGC      PINZB_ID,\n" + 
			"               YS.MINGC      YUNSFSB_ID,\n" + 
			"               RC.FENX       FENX,\n" + 
			"               RC.JIESL      JIESL,\n" + 
			"               RC.JIESRL     JIESRL,\n" + 
			"               RC.JIESMJ     JIESMJ,\n" + 
			"               RC.JIESMJS    JIESMJS,\n" + 
			"               RC.JIESYJ     JIESYJ,\n" + 
			"               RC.JIESYJS    JIESYJS,\n" + 
			"               RC.JIESZF     JIESZF,\n" + 
			"               RC.JIESZFS    JIESZFS,\n" + 
			"               RC.GUSL       GUSL,\n" + 
			"               RC.GUSRL      GUSRL,\n" + 
			"               RC.GUSMJ      GUSMJ,\n" + 
			"               RC.GUSMJS     GUSMJS,\n" + 
			"               RC.GUSYJ      GUSYJ,\n" + 
			"               RC.GUSYJS     GUSYJS,\n" + 
			"               RC.GUSZF      GUSZF,\n" + 
			"               RC.GUSZFS     GUSZFS,\n" + 
			"               RC.RUCSL      RUCSL,\n" + 
			"               RC.RUCRL      RUCRL,\n" + 
			"               RC.RUCMJ      RUCMJ,\n" + 
			"               RC.RUCMJS     RUCMJS,\n" + 
			"               RC.RUCYJ      RUCYJ,\n" + 
			"               RC.RUCYJS     RUCYJS,\n" + 
			"               RC.RUCZF      RUCZF,\n" + 
			"               RC.RUCZFS     RUCZFS,\n" + 
			"               RC.RUCBMDJ    RUCBMDJ,\n" + 
			"               RC.RUCBHSBMDJ RUCBHSBMDJ\n" + 
			"          FROM YUERCBMDJ RC,\n" + 
			"               YUETJKJB  T,\n" + 
			"               GONGYSB   GYS,\n" + 
			"               JIHKJB    JH,\n" + 
			"               YUNSFSB   YS,\n" + 
			"               PINZB     PZ\n" + 
			"         WHERE T.ID = RC.YUETJKJB_ID\n" + 
			"           AND T.GONGYSB_ID = GYS.ID\n" + 
			"           AND T.JIHKJB_ID = JH.ID\n" + 
			"           AND T.YUNSFSB_ID = YS.ID\n" + 
			"           AND T.PINZB_ID = PZ.ID\n" + 
			"           AND T.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND T.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n" + 
			"         ORDER BY JIHKJB_ID DESC, GONGYSB_ID, PINZB_ID, FENX)";
 
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置表名称用于保存
//		egu.setTableName("yuercbmdj");
		//设置显示列名称
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置不分页显示
		egu.addPaging(0);
		
		egu.getColumn("RCID").setHeader("RCID");
		egu.getColumn("RCID").setHidden(true);
		egu.getColumn("RCID").setEditor(null);

		egu.getColumn("GONGYSB_ID").setCenterHeader("供应商");
		egu.getColumn("GONGYSB_ID").setWidth(120);
		egu.getColumn("GONGYSB_ID").setEditor(null);
		egu.getColumn("GONGYSB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("JIHKJB_ID").setCenterHeader("计划口径");
		egu.getColumn("JIHKJB_ID").setWidth(80);
		egu.getColumn("JIHKJB_ID").setEditor(null);
		egu.getColumn("JIHKJB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("PINZB_ID").setCenterHeader("品种");
		egu.getColumn("PINZB_ID").setWidth(80);
		egu.getColumn("PINZB_ID").setEditor(null);
		egu.getColumn("PINZB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("YUNSFSB_ID").setCenterHeader("运输方式");
		egu.getColumn("YUNSFSB_ID").setWidth(80);
		egu.getColumn("YUNSFSB_ID").setEditor(null);
		egu.getColumn("YUNSFSB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("FENX").setCenterHeader("分项");
		egu.getColumn("FENX").setWidth(60);
		egu.getColumn("FENX").setEditor(null);
		egu.getColumn("FENX").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");


		egu.getColumn("JIESL").setCenterHeader("<b>结算</b><br>数量<br>(吨)");
		egu.getColumn("JIESL").setWidth(80);
		((NumberField)egu.getColumn("JIESL").editor).setDecimalPrecision(2);

		egu.getColumn("JIESRL").setCenterHeader("<b>结算</b><br>热值<br>(MJ/kg)");
		egu.getColumn("JIESRL").setWidth(60);
		((NumberField)egu.getColumn("JIESRL").editor).setDecimalPrecision(3);
		
		egu.getColumn("JIESMJ").setCenterHeader("<b>结算</b><br>煤价(含税)<br>(元/吨)");
		egu.getColumn("JIESMJ").setWidth(70);
		((NumberField)egu.getColumn("JIESMJ").editor).setDecimalPrecision(2);

		egu.getColumn("JIESMJS").setCenterHeader("<b>结算</b><br>煤价税<br>(元/吨)");
		egu.getColumn("JIESMJS").setWidth(60);
		egu.getColumn("JIESMJS").setEditor(null);
		egu.getColumn("JIESMJS").setHidden(true);
		egu.getColumn("JIESMJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("JIESYJ").setCenterHeader("<b>结算</b><br>运价(含税)<br>(元/吨)");
		egu.getColumn("JIESYJ").setWidth(70);
		((NumberField)egu.getColumn("JIESYJ").editor).setDecimalPrecision(2);

		egu.getColumn("JIESYJS").setCenterHeader("<b>结算</b><br>运价税<br>(元/吨)");
		egu.getColumn("JIESYJS").setWidth(60);
		egu.getColumn("JIESYJS").setEditor(null);
		egu.getColumn("JIESYJS").setHidden(true);
		egu.getColumn("JIESYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("JIESZF").setCenterHeader("<b>结算</b><br>杂费(含税)<br>(元/吨)");
		egu.getColumn("JIESZF").setWidth(70);
		((NumberField)egu.getColumn("JIESZF").editor).setDecimalPrecision(2);

		egu.getColumn("JIESZFS").setCenterHeader("<b>结算</b><br>杂费税<br>(元/吨)");
		egu.getColumn("JIESZFS").setWidth(60);
		((NumberField)egu.getColumn("JIESZFS").editor).setDecimalPrecision(2);

		egu.getColumn("GUSL").setCenterHeader("<b>估收</b><br>数量<br>(吨)");
		egu.getColumn("GUSL").setWidth(80);
		((NumberField)egu.getColumn("GUSL").editor).setDecimalPrecision(2);
		
		egu.getColumn("GUSRL").setCenterHeader("<b>估收</b><br>热值<br>(MJ/kg)");
		egu.getColumn("GUSRL").setWidth(60);
		((NumberField)egu.getColumn("GUSRL").editor).setDecimalPrecision(3);
		
		egu.getColumn("GUSMJ").setCenterHeader("<b>估收</b><br>煤价(含税)<br>(元/吨)");
		egu.getColumn("GUSMJ").setWidth(70);
		((NumberField)egu.getColumn("GUSMJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("GUSMJS").setCenterHeader("<b>估收</b><br>煤价税<br>(元/吨)");
		egu.getColumn("GUSMJS").setWidth(60);
		egu.getColumn("GUSMJS").setEditor(null);
		egu.getColumn("GUSMJS").setHidden(true);
		egu.getColumn("GUSMJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("GUSYJ").setCenterHeader("<b>估收</b><br>运价(含税)<br>(元/吨)");
		egu.getColumn("GUSYJ").setWidth(70);
		((NumberField)egu.getColumn("GUSYJ").editor).setDecimalPrecision(2);

		egu.getColumn("GUSYJS").setCenterHeader("<b>估收</b><br>运价税<br>(元/吨)");
		egu.getColumn("GUSYJS").setWidth(60);
		egu.getColumn("GUSYJS").setEditor(null);
		egu.getColumn("GUSYJS").setHidden(true);
		egu.getColumn("GUSYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("GUSZF").setCenterHeader("<b>估收</b><br>杂费(含税)<br>(元/吨)");
		egu.getColumn("GUSZF").setWidth(70);
		((NumberField)egu.getColumn("GUSZF").editor).setDecimalPrecision(2);
		
		egu.getColumn("GUSZFS").setCenterHeader("<b>估收</b><br>杂费税<br>(元/吨)");
		egu.getColumn("GUSZFS").setWidth(60);
		((NumberField)egu.getColumn("GUSZFS").editor).setDecimalPrecision(2);
	
		egu.getColumn("RUCSL").setCenterHeader("<b>入厂</b><br>数量<br>(吨)");
		egu.getColumn("RUCSL").setWidth(80);
		egu.getColumn("RUCSL").setEditor(null);
		egu.getColumn("RUCSL").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("RUCRL").setCenterHeader("<b>入厂</b><br>热值<br>(MJ/kg)");
		egu.getColumn("RUCRL").setWidth(60);
		egu.getColumn("RUCRL").setEditor(null);
		egu.getColumn("RUCRL").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("RUCMJ").setCenterHeader("<b>入厂</b><br>煤价(含税)<br>(元/吨)");
		egu.getColumn("RUCMJ").setWidth(70);
		egu.getColumn("RUCMJ").setEditor(null);
		egu.getColumn("RUCMJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("RUCMJS").setCenterHeader("<b>入厂</b><br>煤价税<br>(元/吨)");
		egu.getColumn("RUCMJS").setWidth(60);
		egu.getColumn("RUCMJS").setEditor(null);
		egu.getColumn("RUCMJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("RUCYJ").setCenterHeader("<b>入厂</b><br>运价(含税)<br>(元/吨)");
		egu.getColumn("RUCYJ").setWidth(70);
		egu.getColumn("RUCYJ").setEditor(null);
		egu.getColumn("RUCYJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("RUCYJS").setCenterHeader("<b>入厂</b><br>运价税<br>(元/吨)");
		egu.getColumn("RUCYJS").setWidth(60);
		egu.getColumn("RUCYJS").setEditor(null);
		egu.getColumn("RUCYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("RUCZF").setCenterHeader("<b>入厂</b><br>杂费(含税)<br>(元/吨)");
		egu.getColumn("RUCZF").setWidth(70);
		egu.getColumn("RUCZF").setEditor(null);
		egu.getColumn("RUCZF").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("RUCZFS").setCenterHeader("<b>入厂</b><br>杂费税<br>(元/吨)");
		egu.getColumn("RUCZFS").setWidth(60);
		egu.getColumn("RUCZFS").setEditor(null);
		egu.getColumn("RUCZFS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("RUCBMDJ").setCenterHeader("<b>入厂</b><br>标煤单价<br>(元/吨)");
		egu.getColumn("RUCBMDJ").setWidth(80);
		egu.getColumn("RUCBMDJ").setEditor(null);
		egu.getColumn("RUCBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("RUCBHSBMDJ").setCenterHeader("<b>入厂</b><br>不含税标煤单价<br>(元/吨)");
		egu.getColumn("RUCBHSBMDJ").setWidth(100);
		egu.getColumn("RUCBHSBMDJ").setEditor(null);
		egu.getColumn("RUCBHSBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.setDefaultsortable(false);
		// /设置按钮
		egu.addTbarText("时间:");
		DateField df = new DateField();
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("-");
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc, visit.getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

//		刷新按钮
		GridButton gbr = new GridButton("刷新","function (){document.getElementById('RefreshButton').click();}");
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		// 判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if (getZhangt(con)) {
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		} else {
			// 生成按钮
			GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			gbc.setDisabled(isReady);
			egu.addTbarBtn(gbc);
			// 删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			gbd.setDisabled(isReady);
			egu.addTbarBtn(gbd);

//			保存按钮
			GridButton gbs;
			gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton","if(validateCK(gridDiv_ds)){return;};\n");	
			gbs.setDisabled(isReady);
			egu.addTbarBtn(gbs);
		}

		String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", diancxxb_id, "0.17");
		String yunjs = MainGlobal.getXitxx_item("结算", "运费税率", diancxxb_id, "0.07");
		StringBuffer sb = new StringBuffer();
		sb.append(
					"gridDiv_grid.on('afteredit',function(e){\n" +
					"  i=e.row;\n" +
					"  if(e.field=='JIESMJ'){\n" +
					"    var meijs=0;\n" +
					"   meijs=Round_new(eval(gridDiv_ds.getAt(i).get('JIESMJ')||0)-(eval(gridDiv_ds.getAt(i).get('JIESMJ')||0)/(1+" + meijs + ")),2);\n" +  
					"    gridDiv_ds.getAt(i).set('JIESMJS',meijs);\n" +
					"  }\n" +
					"  if(e.field=='JIESYJ'){\n" +
					"    var yunjs=0;\n" +
					"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('JIESYJ')||0)*" + yunjs + ",2);\n" +
					"    gridDiv_ds.getAt(i).set('JIESYJS',yunjs);\n" +
					"  }\n" +
					"  if(e.field=='GUSMJ'){\n" +
					"    var meijs=0;\n" +
					"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('GUSMJ')||0)-(eval(gridDiv_ds.getAt(i).get('GUSMJ')||0)/(1+" + meijs + ")),2);\n" +
					"    gridDiv_ds.getAt(i).set('GUSMJS',meijs);\n" +
					"  }\n" +
					"  if(e.field=='GUSYJ'){\n" +
					"    var yunjs=0;\n" +
					"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('GUSYJ')||0)*" + yunjs + ",2);\n" +
					"    gridDiv_ds.getAt(i).set('GUSYJS',yunjs);\n" +
					"  }\n" +
					"	if(eval(gridDiv_ds.getAt(i).get('RUCSL')||0)!=0){\n" +
					"		var jsl=eval(gridDiv_ds.getAt(i).get('JIESL')||0);\n" +
					"		var gsl=eval(gridDiv_ds.getAt(i).get('GUSL')||0);\n" +
					"		var rez=0;\n" +
					"		var meij=0;\n" +
					"   	 	var yunj=0;\n" +
					"   	 	var zaf=0;\n" +
					"   	 	var zafs=0;\n" +
					"			if(jsl+gsl!=0){\n" +
					"   	 	rez=Round_new((eval(gridDiv_ds.getAt(i).get('JIESRL')||0)*jsl+eval(gridDiv_ds.getAt(i).get('GUSRL')||0)*gsl)/(jsl+gsl),3);\n" +
					"   	 	meij=Round_new((eval(gridDiv_ds.getAt(i).get('JIESMJ')||0)*jsl+eval(gridDiv_ds.getAt(i).get('GUSMJ')||0)*gsl)/(jsl+gsl),2);\n" +
					"   	 	yunj=Round_new((eval(gridDiv_ds.getAt(i).get('JIESYJ')||0)*jsl+eval(gridDiv_ds.getAt(i).get('GUSYJ')||0)*gsl)/(jsl+gsl),2);\n" +
					"			zaf=Round_new((eval(gridDiv_ds.getAt(i).get('JIESZF')||0)*jsl+eval(gridDiv_ds.getAt(i).get('GUSZF')||0)*gsl)/(jsl+gsl),2);\n" +
					"			zafs=Round_new((eval(gridDiv_ds.getAt(i).get('JIESZFS')||0)*jsl+eval(gridDiv_ds.getAt(i).get('GUSZFS')||0)*gsl)/(jsl+gsl),2);\n" +
					"			}\n"+
					"    		var meijs=Round_new(meij-meij/(1+" + meijs + "),2);\n" +  
					"    		var yunjs=Round_new(yunj*" + yunjs + ",2);\n" +
					"			gridDiv_ds.getAt(i).set('RUCRL',rez);\n" +
					"   	 	gridDiv_ds.getAt(i).set('RUCMJ',meij);\n" +
					"    		gridDiv_ds.getAt(i).set('RUCMJS',meijs);\n" +	
					"   	 	gridDiv_ds.getAt(i).set('RUCYJ',yunj);\n" +
					"   		gridDiv_ds.getAt(i).set('RUCYJS',yunjs);\n" +
					"   	 	gridDiv_ds.getAt(i).set('RUCZF',zaf);\n" +
					"   	 	gridDiv_ds.getAt(i).set('RUCZFS',zafs);\n" +
					"	}\n" +
					" 	if(eval(gridDiv_ds.getAt(i).get('RUCRL'))!=0){\n" +
					"		var daoczhj=0,biaomdj=0,buhsbmdj=0;\n" +
					"    	daoczhj=eval(gridDiv_ds.getAt(i).get('RUCMJ')||0)+eval(gridDiv_ds.getAt(i).get('RUCYJ')||0)+eval(gridDiv_ds.getAt(i).get('RUCZF')||0);\n" +
					"   	biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('RUCRL')||0),2);\n" +
					"    	buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('RUCMJS')||0)-eval(gridDiv_ds.getAt(i).get('RUCYJS')||0)-eval(gridDiv_ds.getAt(i).get('RUCZFS')||0))*29.271/eval(gridDiv_ds.getAt(i).get('RUCRL')||0),2);\n" +
					"    	gridDiv_ds.getAt(i).set('RUCBMDJ',biaomdj);\n" +
					"    	gridDiv_ds.getAt(i).set('RUCBHSBMDJ',buhsbmdj);\n" +
					"    }\n" +
					"});\n");
		
	
		sb.append("gridDiv_grid.on('beforeedit',function(e){\n");
		sb.append("if(e.record.get('FENX')=='累计'||e.record.get('RCID')=='-1'||e.record.get('RUCSL')=='0'){e.cancel=true;}\n");//合计行不允许编辑
		sb.append("});\n");
		egu.addOtherScript(sb.toString());
		egu.addTbarText("-");
		egu.addTbarText("合计信息保存后刷新!");
		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String strDate[] = this.getRiq2().split("-");
		String cnDate = strDate[0] + "年" + strDate[1] + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
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
			visit.setList1(null);
			visit.setString11("");
			visit.setString13("");
		}
		getSelectData();
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}

	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString11() == null
				|| ((Visit) this.getPage().getVisit()).getString11().equals("")) {

			((Visit) this.getPage().getVisit()).setString11(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString11();
	}

	public void setRiq1(String riq1) {
		if (((Visit) this.getPage().getVisit()).getString11() != null
				&& !((Visit) this.getPage().getVisit()).getString11().equals(
						riq1)) {

			((Visit) this.getPage().getVisit()).setString11(riq1);
		}
	}

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString13() == null
				|| ((Visit) this.getPage().getVisit()).getString13().equals("")) {

			((Visit) this.getPage().getVisit()).setString13(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setRiq2(String riq2) {
		if (((Visit) this.getPage().getVisit()).getString13() != null
				&& !((Visit) this.getPage().getVisit()).getString13().equals(
						riq2)) {

			((Visit) this.getPage().getVisit()).setString13(riq2);
		}
	}
}