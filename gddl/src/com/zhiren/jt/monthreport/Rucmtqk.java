package com.zhiren.jt.monthreport;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterFac_dt;

public class Rucmtqk extends BasePage implements PageValidateListener {
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if (flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	
	private boolean _OtherButton = false;

	public void OtherButton(IRequestCycle cycle) {
		_OtherButton = true;
	}
	
	private boolean _ShangbClick = false;

	public void ShangbButton(IRequestCycle cycle) {
		_ShangbClick = true;
	}
	
	private boolean _ShenqxgClick = false;

	public void ShenqxgButton(IRequestCycle cycle) {
		_ShenqxgClick = true;
	}
	
	private boolean _TongyClick = false;

	public void TongyButton(IRequestCycle cycle) {
		_TongyClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
		if(_OtherButton){
			_OtherButton=false;
			Other(cycle);
		}
		if (_ShangbClick) {
			_ShangbClick = false;
			Shangb();
		}
		if (_ShenqxgClick) {
			_ShenqxgClick = false;
			isXiug();
		}
		if (_TongyClick) {
			_TongyClick = false;
			Tongy();
		}
	}
	
	public void Tongy(){
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
		String riq1=intyear+"-"+StrMonth+"-01";
		String CurrODate = DateUtil.FormatOracleDate(riq1);
		String diancID = "	and diancxxb_id = " + this.getTreeid() +"\n";
		String sql = " SELECT ID\n" +
					 " FROM RUCMTQKB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=1\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "update RUCMTQKB set zhuangt=0 where id = "+rsl.getString("id")+"\n";
			i++;
		}
		
		String diancxxb_id = this.getTreeid();
//		String dc_sql = "SELECT ID FROM DIANCXXB WHERE JIB=2 \n";
//		rsl=con.getResultSetList(dc_sql);
//		if(rsl.next()){
//			diancxxb_id = rsl.getString("ID");
//		}
		
		String[] resul=null; 
		InterFac_dt Shangb=new InterFac_dt();	//实例化接口变量
		resul=Shangb.sqlExe(diancxxb_id, sqls, false);
		
		if(resul[0].equals("true")){
			//1置当前数据状态为2

			String sql1="delete rucmtqkb \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//没有日志
		}else{
			System.out.print("同意修改失败："+resul[0]);
			return;
		}
	}
	
	//申请修改
	public void isXiug(){
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
		String riq1=intyear+"-"+StrMonth+"-01";
		String CurrODate = DateUtil.FormatOracleDate(riq1);
		
		String diancID = "	and diancxxb_id = " + this.getTreeid() +"\n";
		String sql = "SELECT ID\n" +
					 " FROM RUCMTQKB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=1\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "update RUCMTQKB set zhuangt=1 where id = "+rsl.getString("id")+"\n";
			i++;
		}
		
		String diancxxb_id = "";
		String dc_sql = "SELECT ID FROM DIANCXXB WHERE JIB=2 \n";
		rsl=con.getResultSetList(dc_sql);
		if(rsl.next()){
			diancxxb_id = rsl.getString("ID");
		}
		
		String[] resul=null; 
		InterFac_dt Shangb=new InterFac_dt();	//实例化接口变量
		resul=Shangb.sqlExe(diancxxb_id, sqls, false);
		
		if(resul[0].equals("true")){
			//1置当前数据状态为2
			String sql1="update rucmtqkb set zhuangt=2 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//没有日志
		}else{
			System.out.print("申请修改失败："+resul[0]);
			return;
		}
	}
	
	//上报
	public void Shangb(){
		JDBCcon con = new JDBCcon();
//		if(getDiancTreeJib()!=3){
//			setMsg("请选择具体电厂！");
//			return;
//		}
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
		String riq1=intyear+"-"+StrMonth+"-01";
		String CurrODate = DateUtil.FormatOracleDate(riq1);
		String diancID = "	and diancxxb_id = " + this.getTreeid() +"\n";
		String sql = "select r.id,\n" +
				"		r.diancxxb_id,\n" +
				"		r.gongysb_id,\n" +
				"       r.fenx,\n" + 
				"       r.laim,\n" + 
				"       r.haom,\n" + 
				"       r.kuc,\n" + 
				"       r.meij,\n" + 
				"       r.zengzs,\n" + 
				"       r.yunzf,\n" + 
				"       r.daocj,\n" + 
				"       r.yisj,\n" + 
				"       r.farl,\n" + 
				"       r.biaomdj,\n" + 
				"       r.yisbmdj\n" + 
				"from rucmtqkb r\n" + 
				"where r.diancxxb_id = "+getTreeid()+"\n" + 
				"      and r.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')\n";
//				"order by g.mingc,decode(r.fenx, '当月', 1, '本期', 2, '同期', 3, '差值', 4, 5)";
		ResultSetList rsl=con.getResultSetList(sql);
		
		String zsql = "select r.id,\n" +
				"       r.diancxxb_id,\n" + 
				"       r.riq,\n" + 
				"       r.fenx,\n" + 
				"       r.rez,\n" + 
				"       r.laiy,\n" + 
				"       r.haoy,\n" + 
				"       r.youkc\n" + 
				"from rucmtqkzb r\n" + 
				"where r.diancxxb_id = "+getTreeid()+"\n" + 
				"      and r.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')\n";
//				"order by decode(r.fenx,'当月',1,'本期',2,'同期',3,'差值',4,5)";
		ResultSetList zrsl=con.getResultSetList(zsql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()+zrsl.getRows()];
		while(rsl.next()){
			sqls[i] = "insert into rucmtqkb(id,diancxxb_id,riq,fenx,gongysb_id,laim,haom,kuc,meij,zengzs,yunzf,daocj,yisj,farl,biaomdj,yisbmdj,zhuangt) values("
					+
					rsl.getString("id")
					+ ","
					+ getTreeid()
					+ ","
					+ "to_date('"
					+ riq1
					+ "','yyyy-mm-dd')"
					+ ",'"
					+ rsl.getString("fenx")
					+ "',"
					+ rsl.getString("gongysb_id")
					+ ","
					+ rsl.getString("laim") +","+ rsl.getString("haom") +","+ rsl.getString("kuc") +","+ rsl.getString("meij") +","+ rsl.getString("zengzs") +","+ rsl.getString("yunzf") +","+ rsl.getString("daocj") +","+ rsl.getString("yisj") +","+ rsl.getString("farl") +","+ rsl.getString("biaomdj") +","+ rsl.getString("yisbmdj")
					+ ",0)";
			i++;
		}
		while(zrsl.next()){
			sqls[i] = "insert into rucmtqkzb(id,diancxxb_id,riq,fenx,rez,laiy,haoy,youkc) values("
					+
					rsl.getString("id")
					+ ","
					+ getTreeid()
					+ ","
					+ "to_date('"
					+ riq1
					+ "','yyyy-mm-dd')"
					+ ",'"
					+ rsl.getString("fenx")
					+ "',"
					+ rsl.getString("rez") + "," + rsl.getString("laiy") +","+ rsl.getString("haoy") +","+ rsl.getString("youkc")
					+ ")";
			i++;
		}
		String diancxxb_id = "";
		String dc_sql = "SELECT ID FROM DIANCXXB WHERE JIB=2 \n";
		rsl=con.getResultSetList(dc_sql);
		if(rsl.next()){
			diancxxb_id = rsl.getString("ID");
		}
		
		String[] resul=null; 
		InterFac_dt Shangb=new InterFac_dt();	//实例化接口变量
		resul=Shangb.sqlExe(diancxxb_id, sqls, false);
		
		if(resul[0].equals("true")){
			//1置当前数据状态为1

			String sql1="update rucmtqkb set zhuangt=1 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//没有日志
		}else{
			System.out.print(riq1+"入厂煤炭情况表："+resul[0]);
			return;
		}
	}
	
	public void Other(IRequestCycle cycle) {
		if(getDiancTreeJib()!=3){
			setMsg("请选择具体电厂！");
			return;
		}
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
		String riq1=intyear+"-"+StrMonth+"-01";
		((Visit) getPage().getVisit()).setString3(this.getTreeid());
		((Visit) getPage().getVisit()).setString4(riq1);
		cycle.activate("Rulmyqk");
	}

	// 生成
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		int flag = -1;
		int flag1 = -1;
		// 工具栏的年份和月份下拉框
		if(getDiancTreeJib()!=3){
			setMsg("请选择具体电厂！");
			return;
		}
//		if(this.getChangbValue().getId()==-1){
//			setMsg("请选择矿别！");
//			return;
//		}
		
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
		String riq1=intyear+"-"+intMonth+"-01";
		
		String sql = 
			"select bt.diancxxb_id,\n" +
			"       bt.gongysb_id,\n" + 
			"       bt.fenx,\n" + 
			"       nvl(nr.laim, 0) as laim,\n" + 
			"       nvl(nr.haom, 0) as haom,\n" + 
			"       nvl(nr.kuc, 0) as kuc,\n" + 
			"       nvl(nr.meij, 0) as meij,\n" + 
			"       nvl(nr.zengzs, 0) as zengzs,\n" + 
			"       nvl(nr.yunzf, 0) as yunzf,\n" + 
			"       nvl(nr.daocj, 0) as daocj,\n" + 
			"       nvl(nr.yisj, 0) as yisj,\n" + 
			"       nvl(nr.farl, 0) as farl,\n" + 
			"       nvl(nr.biaomdj, 0) as biaomdj,\n" + 
			"       nvl(nr.yisbmdj, 0) as yisbmdj\n" + 
			"  from --表头\n" + 
			"        (select dc.diancxxb_id, dc.gongysb_id, fx.fenx\n" + 
			"           from (select distinct diancxxb_id, diancxxb.mingc, y.gongysb_id\n" + 
			"                   from yuetjkjb y, diancxxb\n" + 
			"                  where y.riq = to_date('"+intyear+"-"+StrMonth+"-01', 'yyyy-MM-dd')\n" + 
			"                    and y.diancxxb_id = diancxxb.id) dc,\n" + 
			"                (select '当月' as fenx\n" + 
			"                   from dual\n" + 
			"                 union\n" + 
			"                 select '本期' as fenx\n" + 
			"                   from dual\n" + 
			"                 union\n" + 
			"                 select '同期' as fenx\n" + 
			"                   from dual\n" + 
			"                 union\n" + 
			"                 select '差值' as fenx from dual) fx) bt,\n" + 
			"       --本月，累计\n" + 
			"       ((select tj.diancxxb_id,\n" + 
			"                tj.gongysb_id,\n" + 
			"                decode(hc.fenx, '本月', '当月', '累计', '本期', hc.fenx) as fenx,\n" + 
			"                sum(hc.shouml) as laim,\n" + 
			"                sum(hc.fady + hc.gongry + hc.qith + hc.sunh + hc.diaocl +\n" + 
			"                    hc.panyk) as haom,\n" + 
			"                sum(hc.kuc) as kuc,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.meij) / sum(hc.shouml), 2)) as meij,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.meijs) / sum(hc.shouml),\n" + 
			"                                 2)) as zengzs,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml *\n" + 
			"                                     (dj.yunj + dj.daozzf + dj.zaf + dj.qit)) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as yunzf,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * (dj.meij + dj.yunj)) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as daocj,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml *\n" + 
			"                                     (dj.meij - dj.meijs + dj.yunj +\n" + 
			"                                     dj.daozzf + dj.zaf + dj.qit - dj.yunjs)) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as yisj,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.qnet_ar) / sum(hc.shouml),\n" + 
			"                                 2)) as farl,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.biaomdj) / sum(hc.shouml),\n" + 
			"                                 2)) as biaomdj,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.buhsbmdj) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as yisbmdj\n" + 
			"           from yuehcb hc, yuercbmdj dj, yuetjkjb tj, gongysb g\n" + 
			"          where hc.yuetjkjb_id = tj.id\n" + 
			"            and dj.yuetjkjb_id = tj.id\n" + 
			"            and hc.fenx = dj.fenx\n" + 
			"            and tj.gongysb_id = g.id\n" + 
			"            and tj.riq = to_date('"+intyear+"-"+StrMonth+"-01', 'yyyy-MM-dd')\n" + 
			"          group by (tj.diancxxb_id, hc.fenx, tj.gongysb_id)) union\n" + 
			"       --同期\n" + 
			"        (select tj.diancxxb_id,\n" + 
			"                tj.gongysb_id,\n" + 
			"                '同期' as fenx,\n" + 
			"                sum(hc.shouml) as laim,\n" + 
			"                sum(hc.fady + hc.gongry + hc.qith + hc.sunh + hc.diaocl +\n" + 
			"                    hc.panyk) as haom,\n" + 
			"                sum(hc.kuc) as kuc,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.meij) / sum(hc.shouml), 2)) as meij,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.meijs) / sum(hc.shouml), 2)) as zengzs,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml *\n" + 
			"                                     (dj.yunj + dj.daozzf + dj.zaf + dj.qit)) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as yunzf,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * (dj.meij + dj.yunj)) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as daocj,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml *\n" + 
			"                                     (dj.meij - dj.meijs + dj.yunj + dj.daozzf +\n" + 
			"                                     dj.zaf + dj.qit - dj.yunjs)) /\n" + 
			"                                 sum(hc.shouml),\n" + 
			"                                 2)) as yisj,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.qnet_ar) / sum(hc.shouml),\n" + 
			"                                 2)) as farl,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.biaomdj) / sum(hc.shouml),\n" + 
			"                                 2)) as biaomdj,\n" + 
			"                decode(sum(hc.shouml),\n" + 
			"                       0,\n" + 
			"                       0,\n" + 
			"                       round_new(sum(hc.shouml * dj.buhsbmdj) / sum(hc.shouml),\n" + 
			"                                 2)) as yisbmdj\n" + 
			"           from yuehcb hc, yuercbmdj dj, yuetjkjb tj, gongysb g\n" + 
			"          where hc.yuetjkjb_id = tj.id\n" + 
			"            and dj.yuetjkjb_id = tj.id\n" + 
			"            and hc.fenx = '累计'\n" + 
			"            and dj.fenx = '累计'\n" + 
			"            and tj.gongysb_id = g.id\n" + 
			"            and tj.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12)\n" + 
			"          group by (tj.diancxxb_id, hc.fenx, tj.gongysb_id)) union\n" + 
			"        (select z.diancxxb_id,\n" + 
			"                z.gongysb_id,\n" + 
			"                '差值' as fenx,\n" + 
			"                nvl(bq.laim, 0) - nvl(tq.laim, 0) as laim,\n" + 
			"                nvl(bq.haom, 0) - nvl(tq.haom, 0) as haom,\n" + 
			"                nvl(bq.kuc, 0) - nvl(tq.kuc, 0) as kuc,\n" + 
			"                nvl(bq.meij, 0) - nvl(tq.meij, 0) as meij,\n" + 
			"                nvl(bq.zengzs, 0) - nvl(tq.zengzs, 0) as zengzs,\n" + 
			"                nvl(bq.yunzf, 0) - nvl(tq.yunzf, 0) as yunzf,\n" + 
			"                nvl(bq.daocj, 0) - nvl(tq.daocj, 0) as daocj,\n" + 
			"                nvl(bq.yisj, 0) - nvl(tq.yisj, 0) as yisj,\n" + 
			"                nvl(bq.farl, 0) - nvl(tq.farl, 0) as farl,\n" + 
			"                nvl(bq.biaomdj, 0) - nvl(tq.biaomdj, 0) as biaomdj,\n" + 
			"                nvl(bq.yisbmdj, 0) - nvl(tq.yisbmdj, 0) as yisbmdj\n" + 
			"           from (select distinct diancxxb_id, diancxxb.mingc, y.gongysb_id\n" + 
			"                   from yuetjkjb y, diancxxb\n" + 
			"                  where y.riq = to_date('"+intyear+"-"+StrMonth+"-01', 'yyyy-MM-dd')\n" + 
			"                    and y.diancxxb_id = diancxxb.id) z,\n" + 
			"                (select tj.diancxxb_id,\n" + 
			"                        tj.gongysb_id,\n" + 
			"                        decode(hc.fenx,\n" + 
			"                               '本月',\n" + 
			"                               '当月',\n" + 
			"                               '累计',\n" + 
			"                               '本期',\n" + 
			"                               hc.fenx) as fenx,\n" + 
			"                        sum(hc.shouml) as laim,\n" + 
			"                        sum(hc.fady + hc.gongry + hc.qith + hc.sunh +\n" + 
			"                            hc.diaocl + hc.panyk) as haom,\n" + 
			"                        sum(hc.kuc) as kuc,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.meij) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as meij,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.meijs) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as zengzs,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * (dj.yunj + dj.daozzf +\n" + 
			"                                             dj.zaf + dj.qit)) / sum(hc.shouml),\n" + 
			"                                         2)) as yunzf,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * (dj.meij + dj.yunj)) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as daocj,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml *\n" + 
			"                                             (dj.meij - dj.meijs + dj.yunj +\n" + 
			"                                             dj.daozzf + dj.zaf + dj.qit -\n" + 
			"                                             dj.yunjs)) / sum(hc.shouml),\n" + 
			"                                         2)) as yisj,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.qnet_ar) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as farl,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.biaomdj) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as biaomdj,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.buhsbmdj) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as yisbmdj\n" + 
			"                   from yuehcb hc, yuercbmdj dj, yuetjkjb tj, gongysb g\n" + 
			"                  where hc.yuetjkjb_id = tj.id\n" + 
			"                    and dj.yuetjkjb_id = tj.id\n" + 
			"                    and hc.fenx = '累计'\n" + 
			"                    and dj.fenx = '累计'\n" + 
			"                    and tj.gongysb_id = g.id\n" + 
			"                    and tj.riq = to_date('"+intyear+"-"+StrMonth+"-01', 'yyyy-MM-dd')\n" + 
			"                  group by (tj.diancxxb_id, hc.fenx, tj.gongysb_id)) bq,\n" + 
			"                --同期\n" + 
			"                (select tj.diancxxb_id,\n" + 
			"                        tj.gongysb_id,\n" + 
			"                        '同期' as fenx,\n" + 
			"                        sum(hc.shouml) as laim,\n" + 
			"                        sum(hc.fady + hc.gongry + hc.qith + hc.sunh +\n" + 
			"                            hc.diaocl + hc.panyk) as haom,\n" + 
			"                        sum(hc.kuc) as kuc,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.meij) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as meij,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.meijs) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as zengzs,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * (dj.yunj + dj.daozzf +\n" + 
			"                                             dj.zaf + dj.qit)) / sum(hc.shouml),\n" + 
			"                                         2)) as yunzf,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * (dj.meij + dj.yunj)) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as daocj,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml *\n" + 
			"                                             (dj.meij - dj.meijs + dj.yunj +\n" + 
			"                                             dj.daozzf + dj.zaf + dj.qit -\n" + 
			"                                             dj.yunjs)) / sum(hc.shouml),\n" + 
			"                                         2)) as yisj,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.qnet_ar) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as farl,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.biaomdj) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as biaomdj,\n" + 
			"                        decode(sum(hc.shouml),\n" + 
			"                               0,\n" + 
			"                               0,\n" + 
			"                               round_new(sum(hc.shouml * dj.buhsbmdj) /\n" + 
			"                                         sum(hc.shouml),\n" + 
			"                                         2)) as yisbmdj\n" + 
			"                   from yuehcb hc, yuercbmdj dj, yuetjkjb tj, gongysb g\n" + 
			"                  where hc.yuetjkjb_id = tj.id\n" + 
			"                    and dj.yuetjkjb_id = tj.id\n" + 
			"                    and hc.fenx = '累计'\n" + 
			"                    and dj.fenx = '累计'\n" + 
			"                    and tj.gongysb_id = g.id\n" + 
			"                    and tj.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12)\n" + 
			"                  group by (tj.diancxxb_id, hc.fenx, tj.gongysb_id)) tq\n" + 
			"          where z.diancxxb_id = bq.diancxxb_id(+)\n" + 
			"            and z.diancxxb_id = tq.diancxxb_id(+)\n" + 
			"            and z.gongysb_id = bq.gongysb_id(+)\n" + 
			"            and z.gongysb_id = tq.gongysb_id(+))) nr\n" + 
//			"       gongysb g\n" + 
			" where bt.diancxxb_id = nr.diancxxb_id(+)\n" + 
			"   and bt.gongysb_id = nr.gongysb_id(+)\n" + 
			"   and bt.fenx = nr.fenx(+)\n" + 
//			"   and bt.gongysb_id = g.id\n" + 
			"   and bt.diancxxb_id = " + getTreeid() + "\n" + 
//			"   and bt.gongysb_id = "+this.getChangbValue().getId()+"\n" + 
			" order by bt.diancxxb_id,\n" + 
			"          bt.gongysb_id,\n" + 
			"          decode(bt.fenx, '当月', 1, '本期', 2, '同期', 3, '差值', 4, 5)";
		

		ResultSetList rslcopy = con.getResultSetList(sql);
		con.getDelete("delete rucmtqkb where diancxxb_id = "+ getTreeid()+" and riq = to_date('"+riq1+"','yyyy-MM-dd') " +
//							" and gongysb_id = "+this.getChangbValue().getId()+
							"\n");
		while (rslcopy.next()) {
//			String diancxxb_id = rslcopy.getString("diancxxb_id");
//			Date riq = rslcopy.getDate("riq");
			String fenx = rslcopy.getString("fenx");
			String gongysb_id = rslcopy.getString("gongysb_id");
			double laim = rslcopy.getDouble("laim");
			double haom = rslcopy.getDouble("haom");
			double kuc = rslcopy.getDouble("kuc");
			double meij = rslcopy.getDouble("meij");
			double zengzs = rslcopy.getDouble("zengzs");
			double yunzf = rslcopy.getDouble("yunzf");
			double daocj = rslcopy.getDouble("daocj");
			double yisj = rslcopy.getDouble("yisj");
			double farl = rslcopy.getDouble("farl");
			double biaomdj = rslcopy.getDouble("biaomdj");
			double yisbmdj = rslcopy.getDouble("yisbmdj");

			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			
			flag = con.getInsert("insert into rucmtqkb(id,diancxxb_id,riq,fenx,gongysb_id,laim,haom,kuc,meij,zengzs,yunzf,daocj,yisj,farl,biaomdj,yisbmdj) values("
							+
							_id
							+ ","
							+ getTreeid()
							+ ","
							+ "to_date('"
							+ riq1
							+ "','yyyy-mm-dd')"
							+ ",'"
							+ fenx
							+ "',"
							+ gongysb_id
//							+ this.getChangbValue().getId()
							+ ","
							+ laim +","+ haom +","+ kuc +","+ meij +","+ zengzs +","+ yunzf +","+ daocj +","+ yisj +","+ farl +","+ biaomdj +","+ yisbmdj
							+ ")");

		}
		
		String sql1 = 
			"select zb.diancxxb_id, zb.fenx, nr.rez, nr.laiy, nr.haoy, nr.kuc\n" +
			"  from (select dc.diancxxb_id, fx.fenx\n" + 
			"          from (select " + getTreeid() + " as diancxxb_id from dual) dc,\n" + 
			"               (select '当月' as fenx\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '本期' as fenx\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '同期' as fenx\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '差值' as fenx from dual) fx) zb,\n" + 
			"       ((select d.diancxxb_id,\n" + 
			"                '当月' as fenx,\n" + 
			"                d.zonghm as rez,\n" + 
			"                d.shiysg as laiy,\n" + 
			"                d.shiyhyhj as haoy,\n" + 
			"                d.shiykc as kuc\n" + 
			"           from diaor01bb d\n" + 
			"          where d.diancxxb_id = 214\n" + 
			"            and d.fenx = '本月'\n" + 
			"            and riq = to_date('2010-04-01', 'yyyy-MM-dd')) union\n" + 
			"        (select d.diancxxb_id,\n" + 
			"                '本期' as fenx,\n" + 
			"                d.zonghm as rez,\n" + 
			"                d.shiysg as laiy,\n" + 
			"                d.shiyhyhj as haoy,\n" + 
			"                d.shiykc as kuc\n" + 
			"           from diaor01bb d\n" + 
			"          where d.diancxxb_id = 214\n" + 
			"            and d.fenx = '累计'\n" + 
			"            and riq = to_date('2010-04-01', 'yyyy-MM-dd')) union\n" + 
			"        (select d.diancxxb_id,\n" + 
			"                '同期' as fenx,\n" + 
			"                d.zonghm as rez,\n" + 
			"                d.shiysg as laiy,\n" + 
			"                d.shiyhyhj as haoy,\n" + 
			"                d.shiykc as kuc\n" + 
			"           from diaor01bb d\n" + 
			"          where d.diancxxb_id = 214\n" + 
			"            and d.fenx = '累计'\n" + 
			"            and riq =\n" + 
			"                add_months(trunc(to_date('2010-04-01', 'yyyy-MM-dd')), -12))\n" + 
			"        union (select bq.diancxxb_id,\n" + 
			"                      '差值' as fenx,\n" + 
			"                      nvl(bq.rez, 0) - nvl(tq.rez, 0) as rez,\n" + 
			"                      nvl(bq.laiy, 0) - nvl(tq.laiy, 0) as laiy,\n" + 
			"                      nvl(bq.haoy, 0) - nvl(tq.haoy, 0) as haoy,\n" + 
			"                      nvl(bq.kuc, 0) - nvl(tq.kuc, 0) as kuc\n" + 
			"                 from (select d.diancxxb_id,\n" + 
			"                              '本期' as fenx,\n" + 
			"                              d.zonghm as rez,\n" + 
			"                              d.shiysg as laiy,\n" + 
			"                              d.shiyhyhj as haoy,\n" + 
			"                              d.shiykc as kuc\n" + 
			"                         from diaor01bb d\n" + 
			"                        where d.diancxxb_id = 214\n" + 
			"                          and d.fenx = '累计'\n" + 
			"                          and riq = to_date('2010-04-01', 'yyyy-MM-dd')) bq,\n" + 
			"                      (select d.diancxxb_id,\n" + 
			"                              '同期' as fenx,\n" + 
			"                              d.zonghm as rez,\n" + 
			"                              d.shiysg as laiy,\n" + 
			"                              d.shiyhyhj as haoy,\n" + 
			"                              d.shiykc as kuc\n" + 
			"                         from diaor01bb d\n" + 
			"                        where d.diancxxb_id = 214\n" + 
			"                          and d.fenx = '累计'\n" + 
			"                          and riq = add_months(trunc(to_date('2010-04-01','yyyy-MM-dd')),-12)) tq)) nr\n" + 
			" where zb.diancxxb_id = nr.diancxxb_id(+)\n" + 
			"   and zb.fenx = nr.fenx(+)";
			
		rslcopy = con.getResultSetList(sql1);
		con.getDelete("delete rucmtqkzb where diancxxb_id = "+ getTreeid()+" and riq = to_date('"+riq1+"','yyyy-MM-dd') \n");
		while (rslcopy.next()) {
			String fenx = rslcopy.getString("fenx");
			double rez = rslcopy.getDouble("rez");
			double laiy = rslcopy.getDouble("laiy");
			double haoy = rslcopy.getDouble("haoy");
			double youkc = rslcopy.getDouble("kuc");

			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			
			flag1 = con.getInsert("insert into rucmtqkzb(id,diancxxb_id,riq,fenx,rez,laiy,haoy,youkc) values("
							+
							_id
							+ ","
							+ getTreeid()
							+ ","
							+ "to_date('"
							+ riq1
							+ "','yyyy-mm-dd')"
							+ ",'"
							+ fenx
							+ "',"+ rez +","+ laiy +","+ haoy +","+ youkc
							+ ")");

		}
		if(flag!=-1&&flag1!=-1){
			setMsg("生成成功！");
		}
		
		getSelectData(null);
		con.Close();
	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框

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
		// -----------------------------------
		
		String sql = 
			"select r.id,\n" +
			"		r.diancxxb_id,\n" +
			"		g.mingc as gongysb_id,\n" +
			"       r.fenx,\n" + 
			"       r.laim,\n" + 
			"       r.haom,\n" + 
			"       r.kuc,\n" + 
			"       r.meij,\n" + 
			"       r.zengzs,\n" + 
			"       r.yunzf,\n" + 
			"       r.daocj,\n" + 
			"       r.yisj,\n" + 
			"       r.farl,\n" + 
			"       r.biaomdj,\n" + 
			"       r.yisbmdj\n" + 
			"from rucmtqkb r,gongysb g\n" + 
			"where r.diancxxb_id = "+getTreeid()+"\n" + 
			"	   and r.gongysb_id = g.id\n" +
//			"      and r.gongysb_id = "+this.getChangbValue().getId()+"\n" + 
			"      and r.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')\n" + 
			"order by g.mingc,decode(r.fenx, '当月', 1, '本期', 2, '同期', 3, '差值', 4, 5)";

		rsl = con.getResultSetList(sql);	
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rucmtqkb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("矿别");
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(50);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("laim").setHeader("来煤");
		egu.getColumn("haom").setHeader("耗煤");
		egu.getColumn("kuc").setHeader("库存");
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("zengzs").setHeader("增值税");
		egu.getColumn("yunzf").setHeader("运杂费");
		egu.getColumn("daocj").setHeader("到厂价");
		egu.getColumn("yisj").setHeader("剔税价");
		egu.getColumn("farl").setHeader("发热量");
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("yisbmdj").setHeader("剔税标煤单价");
		egu.setDefaultsortable(false);
		// 设定列初始宽度
//		egu.getColumn("riq").setWidth(80);
//		egu.getColumn("rez").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		String str3 = "select id,mingc from gongysb where fuid=-1\n";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(str3));
		egu.getColumn("gongysb_id").setReturnId(true);
		egu.getColumn("gongysb_id").setEditor(null);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(0);// 设置分页

		// *****************************************设置默认值****************************
		
//		// 设置日期的默认值,
//		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
//		egu.addTbarText("煤矿:");
//		ComboBox changbcb = new ComboBox();
//		changbcb.setTransform("ChangbSelect");
//		changbcb.setId("cb");// 和自动刷新绑定
//		changbcb.setLazyRender(true);// 动态绑定
//		changbcb.setWidth(150);
////		changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//		egu.addToolbarItem(changbcb.getScript());
//		egu.addOtherScript("cb.on('select',function(){document.forms[0].submit();});");
//		egu.addTbarText("-");
		
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);
		
		if(visit.getRenyjb()==3&&getZhuangt()<=0){
//			StringBuffer cpb = new StringBuffer();
//			cpb.append("function(){").append("document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("生成",getBtnHandlerScript("CopyButton"));
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		
		StringBuffer qit = new StringBuffer();
		qit.append("function(){").append("document.getElementById('OtherButton').click();}");
		GridButton ot = new GridButton("其它维护", qit.toString());
//		ot.setIcon(SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(ot);
		
		if(visit.getRenyjb()==3&&getZhuangt()==0){
//			上报按钮
			GridButton gbs = new GridButton("上报",
					getBtnHandlerScript("ShangbButton"));
			gbs.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbs);
		}
		
		if(visit.getRenyjb()==3&&getZhuangt()==1){
//			申请修改按钮
			GridButton gbs = new GridButton("申请修改",
					getBtnHandlerScript("ShenqxgButton"));
			gbs.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbs);
		}
		
		if(visit.getRenyjb()==2&&getZhuangt()==1){
//			申请修改按钮
			GridButton gbs = new GridButton("同意",
					getBtnHandlerScript("TongyButton"));
			gbs.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbs);
		}

//		 ---------------页面js的计算开始------------------------------------------
		
		String sb_str=
			"gridDiv_grid.on('beforeedit',function(e){" +
			"	var rec = gridDiv_ds.getAt(e.row);\n" +
			" 	if(rec.get('FENX')=='差值'){ e.cancel=true;}" +//第4行差值行不允许编辑
			"});\n" +
		
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"	var rec1; var rec2; var rec3;\n" +
			"	var rec = gridDiv_ds.getRange();\n" +
			"	for(var i=1;i<rec.length;i=i+4){\n" +
			"   	if(e.row==i||e.row==i+1){\n" +
			"			rec1=gridDiv_ds.getAt(i);\n" +
			"			rec2=gridDiv_ds.getAt(i+1);\n" +
			"			rec3=gridDiv_ds.getAt(i+2);\n" +
			"			rec3.set(e.field,rec1.get(e.field)-rec2.get(e.field));\n" +
			"		}\n" +
			" 	}\n" +
			
			"});\n";

		StringBuffer sb = new StringBuffer(sb_str);
		
		
		
		egu.addOtherScript(sb.toString());
		// ---------------页面js计算结束--------------------------
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
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
		StringBuffer btnsb = new StringBuffer();
		String cnDate = intyear + "年" + StrMonth + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CopyButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else if(btnName.endsWith("ShangbButton")){
			btnsb.append("数据上报将不能修改，是否继续？");
		} else if(btnName.endsWith("ShenqxgButton")){
			btnsb.append("申请对").append(cnDate).append("数据进行修改，是否继续？");
		} else if(btnName.endsWith("TongyButton")){
			btnsb.append("同意对").append(cnDate).append("数据进行修改，是否继续？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	public int getZhuangt(){
		int zt = -1;
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
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		String diancID = "	and r.diancxxb_id = " + this.getTreeid() +"\n";
		String CurrODate = DateUtil.FormatOracleDate(intyear + "-" + StrMonth + "-01");
		
		String sql = "select zhuangt from rucmtqkb r\n" + 
					"where r.riq = "+CurrODate+"\n" + 
						diancID + "\n";
		ResultSetList rsl = cn.getResultSetList(sql);
		if(rsl.next()){
			zt = rsl.getInt("zhuangt");
		}
		return zt;
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
//			setChangbModels();
			visit.setString3("");
			visit.setString4("");
		}
//		if(nianfchanged){
//			nianfchanged=false;
//			setChangbModels();
//		}
//		if(Changeyuef){
//			Changeyuef=false;
//			setChangbModels();
//		}
//		if(dctree){
//			dctree=false;
//			setChangbModels();
//		}
		getSelectData(null);

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
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

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public boolean dctree = false;
	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			dctree = true;
		}
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
	
////	 煤矿下拉框
//	public IDropDownBean getChangbValue() {
//		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
//			if (getChangbModel().getOptionCount() > 0) {
//				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
//			}else{
//				setChangbValue(new IDropDownBean());
//			}
//		}
//		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
//	}
//
//	public void setChangbValue(IDropDownBean value) {
//		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
//	}
//
//	public IPropertySelectionModel getChangbModel() {
//		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
//			setChangbModels();
//		}
//		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
//	}
//
//	public void setChangbModel(IPropertySelectionModel value) {
//		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
//	}
//
//	public void setChangbModels() {
//		long intyear;
//		if (getNianfValue() == null) {
//			intyear = DateUtil.getYear(new Date());
//		} else {
//			intyear = getNianfValue().getId();
//		}
//		long intMonth;
//		if (getYuefValue() == null) {
//			intMonth = DateUtil.getMonth(new Date());
//		} else {
//			intMonth = getYuefValue().getId();
//		}
//		// 当月份是1的时候显示01,
//		String StrMonth = "";
//		if (intMonth < 10) {
//
//			StrMonth = "0" + intMonth;
//		} else {
//			StrMonth = "" + intMonth;
//		}
//		String sql = 
//			"select distinct g.id, g.mingc\n" +
//			"                   from yuetjkjb y, diancxxb,gongysb g\n" + 
//			"                  where y.riq = to_date('"+intyear+"-"+StrMonth+"-01', 'yyyy-MM-dd')\n" + 
//			"                    and y.diancxxb_id = diancxxb.id\n" + 
//			"                    and diancxxb.id="+getTreeid()+"\n" + 
//			"                    and y.gongysb_id = g.id";
//
//		setChangbModel(new IDropDownModel(sql,"请选择"));
//	}
}
