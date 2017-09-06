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
 * @discription 基于Yuejsdj.java 1.1.1.10修改而成
 */

public class Yuejsdjsc extends BasePage implements PageValidateListener {
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

		String strSql = "delete from YUEJSDJ where riq="+ CurrODate + " and diancxxb_id=" + diancxxb_id;
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"	+ strSql);
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
		String sql;
		JDBCcon con = new JDBCcon();

		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = "";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]	+ "-01");
		String Riq1 = DateUtil.FormatOracleDate(this.getRiq1());
		String Riq2 = DateUtil.FormatOracleDate(this.getRiq2());
		CurrZnDate = strDate[0] + "年" + strDate[1] + "月";
		
		String condition="";
		if(!strDate[1].equals("01")){
			condition="          UNION\n" + 
			"          SELECT DISTINCT J.DIANCXXB_ID,\n" + 
			"                          J.MEIKXXB_ID,\n" + 
			"                          J.JIHKJB_ID,\n" + 
			"                          J.PINZB_ID,\n" + 
			"                          J.YUNSFSB_ID\n" + 
			"            FROM YUEJSDJ J\n" + 
			"           WHERE RIQ = ADD_MONTHS("+CurrODate+", -1)\n" + 
			"             AND J.DIANCXXB_ID = "+diancxxb_id+"\n";
		}
		sql =   "INSERT INTO YUEJSDJ\n" +
				"  (ID, RIQ, DIANCXXB_ID, FENX, MEIKXXB_ID, JIHKJB_ID, PINZB_ID, YUNSFSB_ID)\n" + 
				"  SELECT GETNEWID(SR.DIANCXXB_ID) ID,\n" + 
				"         "+CurrODate+" RIQ,\n" + 
				"         SR.DIANCXXB_ID,\n" + 
				"         FX.FENX,\n" + 
				"         SR.MEIKXXB_ID,\n" + 
				"         SR.JIHKJB_ID,\n" + 
				"         SR.PINZB_ID,\n" + 
				"         SR.YUNSFSB_ID\n" + 
				"    FROM (SELECT DISTINCT J.DIANCXXB_ID,\n" + 
				"                          J.MEIKXXB_ID,\n" + 
				"                          J.JIHKJB_ID,\n" + 
				"                          GETTABLEID('pinzb', 'mingc', J.MEIZ) AS PINZB_ID,\n" + 
				"                          J.YUNSFSB_ID\n" + 
				"            FROM JIESB J\n" + 
				"           WHERE J.DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"             AND J.RUZRQ BETWEEN "+Riq1+" AND "+Riq2+"\n" + 
				condition+
				"			) SR,\n" + 
				"         VWFENXYUE FX";
//		写入表头信息
		int Flag=con.getInsert(sql);
		if(Flag<0){
			setMsg(CurrZnDate + "的表头数据生成失败！");
			con.Close();
			return;
		}
		
		String yunj = "decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.guotyf,0))/sum(nvl(jy.jiessl,0)),2)) as yunj,\n";
		if (MainGlobal.getXitxx_item("数量", "是否重新关联煤款结算单对应的运费信息", 
			String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//			由于国电青铝电厂的煤款和运费是分开结算的，并且可能一张煤款结算单对应多个运输单位，那么还会有多个运费结算单，
//			这种情况比较特殊，所以需要在此重新关联出运费信息
			yunj = "decode(sum(getYunfxx4Jiesbid(j.id, 'guotyf')), 0, 0, round(sum(getYunfxx4Jiesbid(j.id, 'guotyf')) / sum(nvl(jy.jiessl, 0)), 2)) as yunj,\n";
		}

		sql = 
			"select meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id,jiesl,meij, ROUND(meij-meij/1.17,2) meijs," +
			"yunj, ROUND(yunj*0.07,2) yunjs,zaf,qnet_ar from(select j.meikxxb_id, j.jihkjb_id,\n" +
			"getTableId('pinzb','mingc',j.meiz) pinzb_id, j.yunsfsb_id,\n" + 
			"round(sum(j.jiessl),2) as jiesl,\n" + 
			"decode(sum(j.jiessl),0,0,round(sum(j.hansmk)/sum(j.jiessl),2)) as meij,\n" + 
			"decode(sum(j.jiessl),0,0,round(sum(j.shuik)/sum(j.jiessl),2)) as meijs,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.kuangqyf,0))/sum(nvl(jy.jiessl,0)),2)) as kuangqyf,\n" + 
			yunj + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.shuik,0))/sum(nvl(jy.jiessl,0)),2)) as yunjs,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round(sum(nvl(jy.guotzf,0)+nvl(jy.kuangqzf,0))/ sum(nvl(j.jiessl,0)),2)) as zaf,\n" + 
			"decode(sum(j.jiessl),0,0,round(sum(j.jiessl*j.jiesrl*0.0041816)/sum(j.jiessl),3)) as qnet_ar\n" + 
			"from jiesb j, jiesyfb jy\n" + 
			"where j.id = jy.diancjsmkb_id(+)\n" + 
			"and j.ruzrq BETWEEN "+Riq1+" AND "+Riq2+"\n" +
			" and j.diancxxb_id="+ diancxxb_id+
			"group by (j.meikxxb_id, j.jihkjb_id, j.meiz, j.yunsfsb_id))";
		
		ResultSetList rs = con.getResultSetList(sql);
		StringBuffer sb=new StringBuffer();
		sb.append("begin \n");
		while(rs.next()){
//			逐条更新本月信息
			sb.append(  "UPDATE YUEJSDJ J\n" +
						"   SET J.JIESL       = "+rs.getDouble("jiesl")+",\n" + 
						"       J.JIESRL      = "+rs.getDouble("qnet_ar")+",\n" + 
						"       J.JIESMJ      = "+rs.getDouble("meij")+",\n" + 
						"       J.JIESMJS     = "+rs.getDouble("meijs")+",\n" + 
						"       J.JIESYJ      = "+rs.getDouble("yunj")+",\n" + 
						"       J.JIESYJS     = "+rs.getDouble("yunjs")+",\n" + 
						"       J.JIESZF      = "+rs.getDouble("zaf")+",\n" + 
						"       J.KUANGQYF    = "+rs.getDouble("kuangqyf")+"\n" + 
						" WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
						"   AND RIQ = "+CurrODate+" \n" + 
						"   AND FENX = '"+SysConstant.Fenx_Beny+"'\n" + 
						"   AND J.MEIKXXB_ID = "+rs.getString("MEIKXXB_ID")+"\n" + 
						"   AND J.JIHKJB_ID = "+rs.getString("JIHKJB_ID")+"\n" + 
						"   AND J.PINZB_ID = "+rs.getString("PINZB_ID")+"\n" + 
						"   AND J.YUNSFSB_ID = "+rs.getString("YUNSFSB_ID")+";\n");
		}
//		更新本月标煤单价
		sb.append("UPDATE YUEJSDJ J\n" +
				"   SET J.JIESBMDJ = ROUND(DECODE(J.JIESRL,0,0,(J.JIESMJ + J.JIESYJ + J.JIESZF + J.KUANGQYF) *29.271 / J.JIESRL),2),\n" + 
				"       J.JIESBHSBMDJ = ROUND(DECODE(J.JIESRL,0,0,(J.JIESMJ + J.JIESYJ + J.JIESZF + J.KUANGQYF -J.JIESMJS - J.JIESYJS - J.JIESZFS) * 29.271 /J.JIESRL),2)\n" + 
				" WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"   AND RIQ = "+CurrODate+"\n" + 
				"   AND FENX = '"+SysConstant.Fenx_Beny+"';\n");
		sb.append("end;");
		
		if(sb.length()>20){
			Flag=con.getUpdate(sb.toString());
		}
		if(Flag<0){
			setMsg(CurrZnDate + "的数据生成失败！");
			con.Close();
			return;
		}
		rs.close();
		
//		计算累计数据
		Flag=CountLeij();
		if(Flag<0){
			setMsg(CurrZnDate + "的数据生成累计失败！");
			con.Close();
			return;
		}else {
			setMsg(CurrZnDate + "的数据成功生成！");
		} 
		con.Close();
	}
	
	private int CountLeij(){
		String sql;
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		String strDate[] = this.getRiq2().split("-");
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]	+ "-01");
		String year=DateUtil.FormatOracleDate(strDate[0] + "-01-01");
		sql="SELECT MEIKXXB_ID,JIHKJB_ID,PINZB_ID,YUNSFSB_ID,JIESL,JIESRL,JIESMJ,ROUND(JIESMJ-JIESMJ/1.17,2) JIESMJS,KUANGQYF,JIESYJ, ROUND(JIESYJ*0.07,2) JIESYJS,\n" +
			"  JIESZF, JIESZFS, JIESBMDJ,JIESBHSBMDJ \n" +
			" FROM (SELECT DJ.MEIKXXB_ID,\n" +
			"       DJ.JIHKJB_ID,\n" + 
			"       DJ.PINZB_ID,\n" + 
			"       DJ.YUNSFSB_ID,\n" + 
			"       SUM(DJ.JIESL) JIESL,\n" + 
			"       round(DECODE(SUM(DECODE(DJ.JIESRL, 0, 0, DJ.JIESL)),0,0,SUM(DJ.JIESRL * DECODE(DJ.JIESRL, 0, 0, DJ.JIESL)) /SUM(DECODE(DJ.JIESRL, 0, 0, DJ.JIESL))),3) JIESRL,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESMJ) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESMJ,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESMJS) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESMJS,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESMJ, 0, 0, JIESL) * KUANGQYF) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) KUANGQYF,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESMJ, 0, 0, JIESL)),0,0,SUM(DECODE(JIESMJ, 0, 0, JIESL) * JIESYJ) /SUM(DECODE(JIESMJ, 0, 0, JIESL))),2) JIESYJ,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESMJ, 0, 0, JIESL)),0,0,SUM(DECODE(JIESMJ, 0, 0, JIESL) * JIESYJS) /SUM(DECODE(JIESMJ, 0, 0, JIESL))),2) JIESYJS,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESZF) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESZF,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESZFS) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESZFS,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESBMDJ) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESBMDJ,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESBHSBMDJ) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESBHSBMDJ\n" + 
			"  FROM YUEJSDJ DJ,\n" + 
			"       (SELECT DIANCXXB_ID,\n" + 
			"               FENX,\n" + 
			"               MEIKXXB_ID,\n" + 
			"               JIHKJB_ID,\n" + 
			"               PINZB_ID,\n" + 
			"               YUNSFSB_ID\n" + 
			"          FROM YUEJSDJ\n" + 
			"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND RIQ = "+CurrODate+"\n" + 
			"           AND FENX = '"+SysConstant.Fenx_Beny+"') FX\n" + 
			" WHERE DJ.DIANCXXB_ID = FX.DIANCXXB_ID\n" + 
			"   AND DJ.FENX = FX.FENX\n" + 
			"   AND DJ.MEIKXXB_ID = FX.MEIKXXB_ID\n" + 
			"   AND DJ.JIHKJB_ID = FX.JIHKJB_ID\n" + 
			"   AND DJ.PINZB_ID = FX.PINZB_ID\n" + 
			"   AND DJ.YUNSFSB_ID = FX.YUNSFSB_ID\n" +
			"	AND DJ.RIQ BETWEEN "+year+" and " + CurrODate+"\n"+
			" GROUP BY (DJ.MEIKXXB_ID, DJ.JIHKJB_ID, DJ.PINZB_ID, DJ.YUNSFSB_ID))";
//		取得本年1月至所报月份的所有结算信息
		ResultSetList rs = con.getResultSetList(sql);
		StringBuffer sb=new StringBuffer();
		sb.append("begin \n");
		while (rs.next()){
//			逐条更新当月累计信息
			sb.append(  "UPDATE YUEJSDJ J\n" +
					"   SET J.JIESL       = "+rs.getDouble("JIESL")+",\n" + 
					"       J.JIESRL      = "+rs.getDouble("JIESRL")+",\n" + 
					"       J.JIESMJ      = "+rs.getDouble("JIESMJ")+",\n" + 
					"       J.JIESMJS     = "+rs.getDouble("JIESMJS")+",\n" + 
					"       J.JIESYJ      = "+rs.getDouble("JIESYJ")+",\n" + 
					"       J.JIESYJS     = "+rs.getDouble("JIESYJS")+",\n" + 
					"       J.JIESZF      = "+rs.getDouble("JIESZF")+",\n" +
					"       J.JIESZFS     = "+rs.getDouble("JIESZFS")+",\n" + 
					"       J.JIESBMDJ    = "+rs.getDouble("JIESBMDJ")+",\n" + 
					"       J.JIESBHSBMDJ = "+rs.getDouble("JIESBHSBMDJ")+",\n" + 
					"       J.KUANGQYF    = "+rs.getDouble("kuangqyf")+"\n" + 
					" WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
					"   AND RIQ = "+CurrODate+" \n" + 
					"   AND FENX = '"+SysConstant.Fenx_Leij+"'\n" + 
					"   AND J.MEIKXXB_ID = "+rs.getString("MEIKXXB_ID")+"\n" + 
					"   AND J.JIHKJB_ID = "+rs.getString("JIHKJB_ID")+"\n" + 
					"   AND J.PINZB_ID = "+rs.getString("PINZB_ID")+"\n" + 
					"   AND J.YUNSFSB_ID = "+rs.getString("YUNSFSB_ID")+";\n");
		}
		sb.append("end;");
		int flag=0;
		if(sb.length()>20){
			flag=con.getUpdate(sb.toString());
		}
		return flag;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb=new StringBuffer();
//		保存界面中修改的全部信息
		sb.append("begin \n");
		while (rs.next()){
			sb.append(  "UPDATE YUEJSDJ J\n" +
					"   SET J.JIESL       = "+rs.getDouble("JIESL")+",\n" + 
					"       J.JIESRL      = "+rs.getDouble("JIESRL")+",\n" + 
					"       J.JIESMJ      = "+rs.getDouble("JIESMJ")+",\n" + 
					"       J.JIESMJS     = "+rs.getDouble("JIESMJS")+",\n" + 
					"       J.JIESYJ      = "+rs.getDouble("JIESYJ")+",\n" + 
					"       J.JIESYJS     = "+rs.getDouble("JIESYJS")+",\n" + 
					"       J.JIESZF      = "+rs.getDouble("JIESZF")+",\n" +
					"       J.JIESZFS     = "+rs.getDouble("JIESZFS")+",\n" + 
					"       J.JIESBMDJ    = "+rs.getDouble("JIESBMDJ")+",\n" + 
					"       J.JIESBHSBMDJ = "+rs.getDouble("JIESBHSBMDJ")+",\n" + 
					"       J.KUANGQYF    = "+rs.getDouble("kuangqyf")+"\n" + 
					" WHERE ID = "+rs.getString("ID")+" ;\n");
		}
		sb.append("end;");
		int flag=0;
		if(sb.length()>20){
			flag=con.getUpdate(sb.toString());
		}
		String strDate [] = this.getRiq2().split("-");
		String CurrZnDate = strDate[0] + "年" + strDate[1] + "月";
		if(flag<0){
			setMsg(CurrZnDate + "的数据保存失败！");
			con.Close();
			return;
		}
//		根据参数判断是否保存累计值
		if(MainGlobal.getXitxx_item("月报", "月报结算单价自动计算累计", getTreeid(), "是").equals("是")){
//			计算累计数据
			flag=CountLeij();
			if(flag<0){
				setMsg(CurrZnDate + "的数据累计保存失败！");
				con.Close();
				return;
			}
		}
		setMsg(CurrZnDate + "的数据保存生成！");
		rs.close();
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
				+ "  from YUEJSDJ s\n"
				+ " where diancxxb_id = "+ getTreeid() + "\n" 
				+ "   and riq = " + CurrODate;
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
		String strDate1[] = this.getRiq2().split("-");
		String strDate = strDate1[0] + "-" + strDate1[1] + "-01";
		String strSql = "";
		String diancxxb_id = this.getTreeid();
		strSql ="select * from (SELECT --grouping(FENX)a, grouping(GYS.MINGC)b, grouping(J.MINGC)c, grouping(P.MINGC)d, grouping(YS.MINGC)e, grouping(DJ.ID)f,\n" +
			"       DECODE(grouping(DJ.ID),1,-1,DJ.ID) ID,\n" + 
			"       '' AS MEIKXXB_ID,\n" + 
			"       '' AS JIHKJB_ID,\n" + 
			"       '' AS PINZB_ID,\n" + 
			"       '' AS YUNSFSB_ID,\n" + 
			"       DECODE(grouping(DJ.ID),1,DJ.FENX||'合计',DJ.FENX)FENX,\n" + 
			"       SUM(JIESL) JIESL,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESRL) /SUM(DECODE(JIESRL, 0, 0, JIESL))),3) JIESRL,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESMJ) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESMJ,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESMJS) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESMJS,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESMJ, 0, 0, JIESL) * KUANGQYF) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) KUANGQYF,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESMJ, 0, 0, JIESL)),0,0,SUM(DECODE(JIESMJ, 0, 0, JIESL) * JIESYJ) /SUM(DECODE(JIESMJ, 0, 0, JIESL))),2) JIESYJ,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESMJ, 0, 0, JIESL)),0,0,SUM(DECODE(JIESMJ, 0, 0, JIESL) * JIESYJS) /SUM(DECODE(JIESMJ, 0, 0, JIESL))),2) JIESYJS,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESZF) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESZF,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESZFS) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESZFS,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESBMDJ) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESBMDJ,\n" + 
			"       ROUND (DECODE(SUM(DECODE(JIESRL, 0, 0, JIESL)),0,0,SUM(DECODE(JIESRL, 0, 0, JIESL) * JIESBHSBMDJ) /SUM(DECODE(JIESRL, 0, 0, JIESL))),2) JIESBHSBMDJ\n" + 
			"  FROM YUEJSDJ DJ, MEIKXXB MK, JIHKJB J, PINZB P, YUNSFSB YS\n" + 
			" WHERE DJ.MEIKXXB_ID = MK.ID\n" + 
			"   AND DJ.JIHKJB_ID = J.ID\n" + 
			"   AND DJ.PINZB_ID = P.ID\n" + 
			"   AND DJ.YUNSFSB_ID = YS.ID\n" + 
			"   AND DJ.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND DJ.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n" + 
			" GROUP BY ROLLUP(FENX, (MK.MINGC, J.MINGC, P.MINGC, YS.MINGC, DJ.ID))\n" + 
			" HAVING GROUPING(FENX)+GROUPING(MK.MINGC)=1\n" + 
			" ORDER BY FENX)\n union all \n"+
			"select * from (SELECT DJ.ID ID,\n" +
			"       MK.MINGC AS MEIKXXB_ID,\n" + 
			"       J.MINGC AS JIHKJB_ID,\n" + 
			"       P.MINGC AS PINZB_ID,\n" + 
			"       YS.MINGC AS YUNSFSB_ID,\n" + 
			"       DJ.FENX FENX,\n" + 
			"       JIESL JIESL,\n" + 
			"       JIESRL JIESRL,\n" + 
			"       JIESMJ JIESMJ,\n" + 
			"       JIESMJS JIESMJS,\n" + 
			"       KUANGQYF KUANGQYF,\n" + 
			"       JIESYJ JIESYJ,\n" + 
			"       JIESYJS JIESYJS,\n" + 
			"       JIESZF JIESZF,\n" + 
			"       JIESZFS JIESZFS,\n" + 
			"       JIESBMDJ JIESBMDJ,\n" + 
			"       JIESBHSBMDJ JIESBHSBMDJ\n" + 
			"  FROM YUEJSDJ DJ, MEIKXXB MK, JIHKJB J, PINZB P, YUNSFSB YS\n" + 
			" WHERE DJ.MEIKXXB_ID = MK.ID\n" + 
			"   AND DJ.JIHKJB_ID = J.ID\n" + 
			"   AND DJ.PINZB_ID = P.ID\n" + 
			"   AND DJ.YUNSFSB_ID = YS.ID\n" + 
			"   AND DJ.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND DJ.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n" + 
			" ORDER BY JIHKJB_ID DESC, GONGYSB_ID, PINZB_ID, ID, FENX)";
 
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置表名称用于保存
		egu.setTableName("yuejsdj");
		//设置显示列名称
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置不分页显示
		egu.addPaging(0);
		
		egu.getColumn("ID").setHeader("id");
		egu.getColumn("ID").setHidden(true);

		egu.getColumn("MEIKXXB_ID").setCenterHeader("煤矿单位");
		egu.getColumn("MEIKXXB_ID").setWidth(120);
		egu.getColumn("MEIKXXB_ID").setEditor(null);
		egu.getColumn("MEIKXXB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

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

		egu.getColumn("JIESL").setCenterHeader("结算量<br>(吨)");
		egu.getColumn("JIESL").setWidth(60);
		((NumberField)egu.getColumn("JIESL").editor).setDecimalPrecision(2);

		egu.getColumn("JIESRL").setCenterHeader("结算热量<br>(MJ/kg)");
		egu.getColumn("JIESRL").setWidth(70);
		((NumberField)egu.getColumn("JIESRL").editor).setDecimalPrecision(3);
		
		egu.getColumn("JIESMJ").setCenterHeader("含税煤价<br>(元/吨)");
		egu.getColumn("JIESMJ").setWidth(60);
		((NumberField)egu.getColumn("JIESMJ").editor).setDecimalPrecision(2);

		egu.getColumn("JIESMJS").setCenterHeader("煤价税<br>(元/吨)");
		egu.getColumn("JIESMJS").setWidth(60);
		((NumberField)egu.getColumn("JIESMJS").editor).setDecimalPrecision(2);
		
		egu.getColumn("kuangqyf").setCenterHeader("矿区运费<br>(元/吨)");
		egu.getColumn("kuangqyf").setWidth(70);
		((NumberField)egu.getColumn("kuangqyf").editor).setDecimalPrecision(2);

		egu.getColumn("JIESYJ").setCenterHeader("含税运价<br>(元/吨)");
		egu.getColumn("JIESYJ").setWidth(70);
		((NumberField)egu.getColumn("JIESYJ").editor).setDecimalPrecision(2);

		egu.getColumn("JIESYJS").setCenterHeader("运价税<br>(元/吨)");
		egu.getColumn("JIESYJS").setWidth(60);
		((NumberField)egu.getColumn("JIESYJS").editor).setDecimalPrecision(2);
		
		egu.getColumn("JIESZF").setCenterHeader("杂费<br>(元/吨)");
		egu.getColumn("JIESZF").setWidth(60);
		((NumberField)egu.getColumn("JIESZF").editor).setDecimalPrecision(2);

		egu.getColumn("JIESZFS").setCenterHeader("杂费税<br>(元/吨)");
		egu.getColumn("JIESZFS").setWidth(60);
		((NumberField)egu.getColumn("JIESZFS").editor).setDecimalPrecision(2);

		egu.getColumn("JIESBMDJ").setCenterHeader("标煤单价<br>(元/吨)");
		egu.getColumn("JIESBMDJ").setWidth(60);
		egu.getColumn("JIESBMDJ").setEditor(null);
		egu.getColumn("JIESBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.getColumn("JIESBHSBMDJ").setCenterHeader("不含税标煤单价<br>(元/吨)");
		egu.getColumn("JIESBHSBMDJ").setWidth(100);
		egu.getColumn("JIESBHSBMDJ").setEditor(null);
		egu.getColumn("JIESBHSBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

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
			egu.addTbarBtn(gbc);
			// 删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);

//			保存按钮
			GridButton gbs;
			gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton","if(validateCK(gridDiv_ds)){return;};\n");	
			egu.addTbarBtn(gbs);
		}

		String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", diancxxb_id, "0.17");
		String yunjs = MainGlobal.getXitxx_item("结算", "运费税率", diancxxb_id, "0.07");
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='JIESMJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('JIESMJ')||0)-(eval(gridDiv_ds.getAt(i).get('JIESMJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('JIESMJS',meijs);\n" + 
				"  }\n" + 
				"  if(e.field=='JIESYJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('JIESYJ')||0)*" + yunjs + ",2);\n" + 
				"    gridDiv_ds.getAt(i).set('JIESYJS',yunjs);\n" + 
				"  }\n" + 
				"  if(e.field=='JIESMJ'||e.field=='JIESMJS'||e.field=='KUANGQYF'||e.field=='JIESYJ'||e.field=='JIESYJS'||e.field=='JIESZF'||e.field=='JIESZFS'||e.field=='JIESRL'){\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('JIESMJ')||0)+eval(gridDiv_ds.getAt(i).get('KUANGQYF')||0)+eval(gridDiv_ds.getAt(i).get('JIESYJ')||0)+eval(gridDiv_ds.getAt(i).get('JIESZF')||0);\n" + 
				" 	if(eval(gridDiv_ds.getAt(i).get('JIESRL'))!=0){\n" +
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('JIESRL')||0),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('JIESMJS')||0)-eval(gridDiv_ds.getAt(i).get('JIESYJS')||0)-eval(gridDiv_ds.getAt(i).get('JIESZFS')||0))*29.271/eval(gridDiv_ds.getAt(i).get('JIESRL')||0),2);\n" + 
				"}\n" + 
				"    gridDiv_ds.getAt(i).set('JIESBMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('JIESBHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				"});\n");
		
		String condition="";
		if(MainGlobal.getXitxx_item("月报", "月报结算单价自动计算累计", getTreeid(), "是").equals("是")){
			condition="if(e.record.get('FENX')=='累计'){ e.cancel=true;}\n";
		}
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){\n");
		sb.append("if(e.record.get('ID')=='-1'){e.cancel=true;}\n");//合计行不允许编辑
		sb.append(condition);
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