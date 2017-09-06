package com.zhiren.dc.monthReport_mt;

import java.util.ArrayList;
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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * @author 刘雨
 * @since 2010-07-06
 * @version V0.1
 * @discription 马头月报结算标煤单价
 */

public class Yuejsdj_mt extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString9();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString9(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString10());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString10();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString10(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
	private void updateYuejsdj(JDBCcon con, ResultSetList rsmx, String tjkjid, String fenx){
		double jiessl = 0.0;
		double meij = 0.0;
		double meijs = 0.0;
		double kuangqyf = 0.0;
		double tielyf = 0.0;
		double tielyfs = 0.0;
		double daozzf = 0.0;
		double tielzf = 0.0;
		double qit = 0.0;
		double farl = 0.0;
		double daoczhj = 0.0;
		double biaomdj = 0.0;
		double buhsbmdj = 0.0;
		jiessl = rsmx.getDouble("jiesl");
		meij = rsmx.getDouble("meij");
		meijs = rsmx.getDouble("meijs");
		kuangqyf = rsmx.getDouble("kuangqyf");
		tielyf = rsmx.getDouble("yunj");
		tielyfs = rsmx.getDouble("yunjs");
		daozzf = rsmx.getDouble("daozzf");
		tielzf = rsmx.getDouble("zaf");
		qit = rsmx.getDouble("qit");
		farl = rsmx.getDouble("qnet_ar");
//		到场综合价 = 煤价(含税) + 矿区运费 + 铁路运费(含税) + 到站杂费 + 铁路杂费 + 其它
		daoczhj=meij+kuangqyf+tielyf+daozzf+tielzf + qit;
		if(farl >0 ){
			biaomdj=CustomMaths.Round_new(daoczhj*29.271/farl,2);
			buhsbmdj=CustomMaths.Round_new((daoczhj-meijs-tielyfs)*29.271/farl,2);
		}
		String sql = "update yuejsbmdj set jiesl = " + jiessl +
		",meij = " + meij + 
		",meijs = " + meijs +
		",yunj = " + tielyf +
		",yunjs = " + tielyfs +
		",daozzf = " + daozzf +
		",zaf = " + tielzf +
		",qit = " + qit +
		",qnet_ar = " + farl +
		",biaomdj = " + biaomdj + 
		",buhsbmdj = " + buhsbmdj +
		",kuangqyf = " + kuangqyf +
		" where yuetjkjb_id =" + tjkjid + " and fenx = '" + fenx + "'";
		con.getUpdate(sql);
	}
	private void Save() {
		if(getChange() == null || "".equals(getChange())){
			setMsg("没有进行改动无需保存！");
			return ;
		}
		String sql;
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		while(rs.next()){
			String tjkjid = rs.getString("yuetjkjb_id");
			updateYuejsdj(con, rs, tjkjid, SysConstant.Fenx_Beny);
			int intYuef = Integer.parseInt(getYuef());
			if(intYuef == 1) {
				updateYuejsdj(con, rs, tjkjid, SysConstant.Fenx_Leij);
			}else{
				sql = "select * from yuetjkjb_mt where id = " + tjkjid;
				ResultSetList rsl = con.getResultSetList(sql);
				if(rsl.next()){
					String CODate = DateUtil.FormatOracleDate(rsl.getDate("riq"));
					sql = getYuejsdjSql_lj(CODate, null, rsl.getString("gongysb_id"),
							rsl.getString("jihkjb_id"), rsl.getString("pinzb_id"),
							rsl.getString("yunsfsb_id"), rsl.getString("diancxxb_id"));
					rsl = con.getResultSetList(sql);
					if(rsl.next()){
						updateYuejsdj(con, rsl, tjkjid, SysConstant.Fenx_Leij);
					}
					rsl.close();
				}
			}
			
		}
		rs.close();
		con.Close();
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
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
	}
	
	private String getYuejsdjSql_by(String CODate, String data_c, String gongysb_id, 
			String jihkjb_id, String pinzb_id, String yunsfsb_id, String diancxxb_id){
		String sql = 
			"select j.diancxxb_id, m.meikdq_id, j.jihkjb_id,\n" +
			"getTableId('pinzb','mingc',j.meiz) pinzb_id, j.yunsfsb_id,\n" + 
			"round_new(sum(j.jiessl),0) as jiesl,\n" + 
			"sum(round_new(getjiesdzb('jiesb',j.id,'结算数量' ,'gongf'),0)) as gongfsl,\n" + 
			"decode(sum(j.jiessl),0,0,round_new(sum(j.jiessl*round_new(j.hansmk/j.jiessl,2))/sum(j.jiessl),2)) as meij,\n" + 
			"decode(sum(j.jiessl),0,0,round_new(sum(j.jiessl*round_new(j.shuik/j.jiessl,2))/sum(j.jiessl),2)) as meijs,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum(nvl(jy.kuangqzf,0)+nvl(jy.kuangqyf,0))/sum(nvl(jy.jiessl,0)),2)) as kuangqyf,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum(nvl(jy.guotyf,0))/sum(nvl(j.jiessl,0)),2)) as yunj,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum((nvl(jy.guotyf,0)+nvl(jy.kuangqyf,0))*nvl(jy.shuil,0))/sum(nvl(j.jiessl,0)),2)) as yunjs,\n" + 
			"0 as daozzf,0 as qit,\n" + 
			"decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum(nvl(jy.guotzf,0))/ sum(nvl(j.jiessl,0)),2)) as zaf,\n" + 
			"decode(sum(j.jiessl),0,0,round_new(sum(j.jiessl*sj.jies*0.0041816)/sum(j.jiessl),2)) as qnet_ar\n" + 
			"from jiesb j, meikxxb m, jiesyfb jy,(select * from jieszbsjb where zhibb_id=2) sj\n" + 
			"where j.meikxxb_id = m.id and j.id = jy.diancjsmkb_id(+) and j.id=sj.jiesdid(+)\n" + 
			"and j.diancxxb_id = "+diancxxb_id+"\n" + 
			"and m.meikdq_id = "+gongysb_id+"\n" + 
			"and j.jihkjb_id = "+jihkjb_id+"\n" + 
			"and getTableId('pinzb','mingc',j.meiz) = "+pinzb_id+"\n" + 
			"and j.yunsfsb_id = "+yunsfsb_id+"\n" + 
			"and j.ruzrq >= "+CODate+"- "+data_c+"\n" + 
			"and j.ruzrq < add_months("+CODate+",1) -"+data_c+"\n" + 
			"group by (j.diancxxb_id, m.meikdq_id, j.jihkjb_id, j.meiz, j.yunsfsb_id)";
		return sql;
	}
	private String getYuejsdjSql_lj(String CODate, String data_c, String gongysb_id, 
			String jihkjb_id, String pinzb_id, String yunsfsb_id, String diancxxb_id){
		String sql = 

			"select\n" +
			"round_new(sum(jiesl),0) as jiesl,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(meij*jiesl)/sum(jiesl)),2) meij,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(meijs*jiesl)/sum(jiesl)),2) meijs,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(yunj*jiesl)/sum(jiesl)),2) yunj,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(yunjs*jiesl)/sum(jiesl)),2) yunjs,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(daozzf*jiesl)/sum(jiesl)),2) daozzf,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(zaf*jiesl)/sum(jiesl)),2) zaf,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(qit*jiesl)/sum(jiesl)),2) qit,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(qnet_ar*jiesl)/sum(jiesl)),2) qnet_ar,\n" + 
			"round_new(decode(sum(jiesl),0,0,sum(kuangqyf*jiesl)/sum(jiesl)),2) kuangqyf\n" + 
			"from\n" + 
			"(select t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n" + 
			"y.jiesl, y.meij, y.meijs, y.yunj , y.yunjs, y.daozzf, y.zaf, y.qit, y.qnet_ar,\n" + 
			"y.kuangqyf\n" + 
			"from yuejsbmdj y, yuetjkjb_mt t\n" + 
			"where y.yuetjkjb_id = t.id\n" + 
			"and t.riq = "+CODate+"\n" + 
			"and t.gongysb_id = "+gongysb_id+"\n" + 
			"and t.jihkjb_id = "+jihkjb_id+"\n" + 
			"and t.pinzb_id = "+pinzb_id+"\n" + 
			"and t.yunsfsb_id = "+yunsfsb_id+"\n" + 
			"and t.diancxxb_id = "+diancxxb_id+"\n" + 
			"and y.fenx = '" + SysConstant.Fenx_Beny + "'\n" +
			"union\n" + 
			"select t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n" + 
			"y.jiesl, y.meij, y.meijs, y.yunj , y.yunjs, y.daozzf, y.zaf, y.qit, y.qnet_ar,\n" + 
			"y.kuangqyf\n" + 
			"from yuejsbmdj y, yuetjkjb_mt t\n" + 
			"where y.yuetjkjb_id = t.id\n" + 
			"and t.riq = add_months("+CODate+",-1)\n" + 
			"and t.gongysb_id = "+gongysb_id+"\n" + 
			"and t.jihkjb_id = "+jihkjb_id+"\n" + 
			"and t.pinzb_id = "+pinzb_id+"\n" + 
			"and t.yunsfsb_id = "+yunsfsb_id+"\n" + 
			"and t.diancxxb_id = "+diancxxb_id+"\n" + 
			"and y.fenx = '" + SysConstant.Fenx_Leij + "')\n" +
			"group by diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n" + 
			"";
		return sql;
	}
	private String InsertYuejsdj(JDBCcon con, String sql, String tjkjid, String fenx, String diancxxb_id){
		String strid = MainGlobal.getNewID(con, diancxxb_id);
		double jiessl = 0.0;
		double meij = 0.0;
		double meijs = 0.0;
		double kuangqyf = 0.0;
		double tielyf = 0.0;
		double tielyfs = 0.0;
		double daozzf = 0.0;
		double tielzf = 0.0;
		double qit = 0.0;
		double farl = 0.0;
		double daoczhj = 0.0;
		double biaomdj = 0.0;
		double buhsbmdj = 0.0;
		ResultSetList rsmx = con.getResultSetList(sql);
		if(rsmx.next()){
			jiessl = rsmx.getDouble("jiesl");
			meij = rsmx.getDouble("meij");
			meijs = rsmx.getDouble("meijs");
			kuangqyf = rsmx.getDouble("kuangqyf");
			tielyf = rsmx.getDouble("yunj");
			tielyfs = rsmx.getDouble("yunjs");
			daozzf = rsmx.getDouble("daozzf");
			tielzf = rsmx.getDouble("zaf");
			qit = rsmx.getDouble("qit");
			farl = rsmx.getDouble("qnet_ar");
//			到场综合价 = 煤价(含税) + 矿区运费 + 铁路运费(含税) + 到站杂费 + 铁路杂费 + 其它
			daoczhj=meij+kuangqyf+tielyf+daozzf+tielzf + qit;
			if(farl >0 ){
				biaomdj=CustomMaths.Round_new(daoczhj*29.271/farl,2);
				buhsbmdj=CustomMaths.Round_new((daoczhj-meijs-tielyfs)*29.271/farl,2);
			}
		}
		rsmx.close();
		sql = "insert into yuejsbmdj (id, fenx, yuetjkjb_id, jiesl, meij, meijs, kuangqyf, " +
				"yunj, yunjs, daozzf, zaf, qit, qnet_ar, biaomdj, buhsbmdj)\n values (" +
				 strid + ",'" + fenx + "'," + tjkjid + "," + jiessl + "," + meij + "," +
				 meijs + "," + kuangqyf + "," + tielyf + "," + tielyfs + "," + daozzf +
				 "," + tielzf + "," + qit + "," + farl + "," + biaomdj + "," + buhsbmdj + ")";
		if(con.getInsert(sql)==-1){
			return null;
		}
		return strid;
	}
	
	
	private void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from yuejsbmdj where yuetjkjb_id in (select id from yuetjkjb_mt where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ ")";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}
	
	public void CreateData() {
		/* 首先删除当月数据 */
		DelData();
		String diancxxb_id = getTreeid();	//电厂ID
		String sql;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		int intYuef = Integer.parseInt(getYuef());
		/* 取得月报的取数日期差 */
		String date_c = MainGlobal.getXitxx_item("月报", "月报取数日期差", diancxxb_id, "0");
		/* 循环查找统计口径 */
		sql = "select distinct j.diancxxb_id,m.meikdq_id,m.id as meikxxb_id,j.jihkjb_id,getTableId('pinzb','mingc',j.meiz) as pinzb_id,j.yunsfsb_id\n" +
			"from jiesb j, meikxxb m\n" + 
			"where j.meikxxb_id = m.id\n" + 
			"and j.diancxxb_id =" + diancxxb_id + "\n" + 
			"and j.ruzrq >= "+CurrODate+" - "+date_c+"\n" + 
			"and j.ruzrq < add_months("+CurrODate+",1) - "+date_c+"\nunion\n" +
			"select distinct t.diancxxb_id, t.gongysb_id, t.meikxxb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id from yuejsbmdj y, yuetjkjb_mt t\n" + 
			"where y.yuetjkjb_id = t.id\n" +
			"and t.diancxxb_id = " + diancxxb_id + "\n" + 
			"and t.riq =add_months(" + CurrODate +",-1)"; 
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			String meikxxb_id = rs.getString("meikxxb_id");
			String gongysb_id = rs.getString("meikdq_id");
			String jihkjb_id = rs.getString("jihkjb_id");
			String pinzb_id = rs.getString("pinzb_id");
			String yunsfsb_id = rs.getString("yunsfsb_id");
			/* 得到统计口径ID */
			String tjkjid = MonthReportOp.getYuetjkj(con,CurrODate,gongysb_id,meikxxb_id,jihkjb_id,
					pinzb_id,yunsfsb_id,diancxxb_id);
			/* 取得当月数据的SQL */
			sql = getYuejsdjSql_by(CurrODate, date_c, gongysb_id, jihkjb_id, pinzb_id,
					yunsfsb_id, diancxxb_id);
			/* 插入当月数据 */
			InsertYuejsdj(con, sql, tjkjid, SysConstant.Fenx_Beny, diancxxb_id);
			/* 取得累计数据的SQL */
			if(intYuef == 1){	//如果是一月份取当月数据
				sql = getYuejsdjSql_by(CurrODate, date_c, gongysb_id, jihkjb_id, pinzb_id,
						yunsfsb_id, diancxxb_id);
			}else{
				sql = getYuejsdjSql_lj(CurrODate, date_c, gongysb_id, jihkjb_id, pinzb_id,
						yunsfsb_id, diancxxb_id);
			}
			/* 插入累计数据 */
			InsertYuejsdj(con, sql, tjkjid, SysConstant.Fenx_Leij, diancxxb_id);
		}
		rs.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
	}

	public void getSelectData() {
		String strDate="";
		strDate=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String strSql="";	
		String diancxxb_id=this.getTreeid();
		strSql=
			"select dj.id,dj.yuetjkjb_id,gongysb.mingc as gongysb_id,meikxxb.mingc as meikxxb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" +
			"yunsfsb.mingc as yunsfsb_id,fenx,jiesl,meij,meijs,kuangqyf,dj.yunj,yunjs,daozzf,zaf,qit,qnet_ar,biaomdj,buhsbmdj\n" + 
			"from yuetjkjb_mt tj,yuejsbmdj dj,gongysb,jihkjb,pinzb,yunsfsb,meikxxb\n" + 
			"where tj.id=dj.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.meikxxb_id=meikxxb.id and tj.jihkjb_id=jihkjb.id\n" + 
			"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and dj.fenx='"+SysConstant.Fenx_Beny+"'\n" + 
			"and diancxxb_id=" + diancxxb_id +" and riq=to_date('"+strDate+"','yyyy-mm-dd')\n" +
			"order by dj.id ";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置表名称用于保存
		egu.setTableName("yuejsbmdj");
		// /设置显示列名称
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight-30");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setHeader("yuetjkjb_id");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setEditor(null);
		
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);	
		egu.getColumn("jihkjb_id").setEditor(null);
		
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("pinzb_id").setEditor(null);
		
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setEditor(null);
		
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		
		egu.getColumn("jiesl").setHeader("结算量");
		egu.getColumn("jiesl").setWidth(60);
		egu.getColumn("jiesl").setEditor(null);
		
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("meij").setWidth(60);	
		egu.getColumn("meij").setEditor(null);
		
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("meijs").setEditor(null);
		
		egu.getColumn("kuangqyf").setHeader("交货前杂费");
		egu.getColumn("kuangqyf").setWidth(60);
		egu.getColumn("kuangqyf").setEditor(null);
		
		egu.getColumn("yunj").setHeader("运价");
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunj").setEditor(null);
		
		egu.getColumn("yunjs").setHeader("运价税");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		
		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("zaf").setEditor(null);
		
		egu.getColumn("qit").setHeader("其他");
		egu.getColumn("qit").setWidth(60);
		
		egu.getColumn("qnet_ar").setHeader("结算热量");
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("qnet_ar").setEditor(null);
		
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("biaomdj").setEditor(null);
		
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setWidth(100);
		egu.getColumn("buhsbmdj").setEditor(null);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.setDefaultsortable(false);    
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("月份");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addToolbarItem(new ToolbarText("-").getScript());
		egu.addToolbarItem("{"+new GridButton("刷新","function (){document.getElementById('RefreshButton').click()}").getScript()+"}");
//		 生成按钮
		GridButton gbc = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//		删除按钮
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
//		egu.addToolbarItem("{"+new GridButton("生成","function (){document.getElementById('CreateButton').click()}").getScript()+"}");
//		保存
//		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
//		 保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		
//		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
//		"var end = url.indexOf(';');"+
//         "url = url.substring(0,end);"+
//         "url = url + '?service=page/' + 'MonthReport&lx=yuejsdjb';" +
//         " window.open(url,'newWin');";
//		egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");
		
		StringBuffer sb = new StringBuffer();
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"\n" + 
				"  if(e.field=='DAOZZF'||e.field=='QIT'){\n" + 
				"\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ'))+eval(gridDiv_ds.getAt(i).get('KUANGQYF'))+eval(gridDiv_ds.getAt(i).get('YUNJ'))+eval(gridDiv_ds.getAt(i).get('DAOZZF'))+eval(gridDiv_ds.getAt(i).get('ZAF'))+eval(gridDiv_ds.getAt(i).get('QIT'));\n" + 
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS'))-eval(gridDiv_ds.getAt(i).get('YUNJS')))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),2);\n" + 
				"\n" + 
				"    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				"});"
		);
		egu.addOtherScript(sb.toString());

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianfValue() + "年" + getYuefValue() + "月";
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
			setRiq();
			getSelectData();
		}
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
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
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
    	if  (_YuefValue!=Value){
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
}
