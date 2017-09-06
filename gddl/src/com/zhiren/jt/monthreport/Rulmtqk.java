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

public class Rulmtqk extends BasePage implements PageValidateListener {
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
					 " FROM RUlMTQKB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=1\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "update RULMTQKB set zhuangt=0 where id = "+rsl.getString("id")+"\n";
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

			String sql1="delete rulmtqkb \n" +
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
					 " FROM RULMTQKB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=1\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "update RULMTQKB set zhuangt=1 where id = "+rsl.getString("id")+"\n";
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
			String sql1="update ruLmtqkb set zhuangt=2 \n" +
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
				"       r.diancxxb_id,\n" + 
				"       r.fenx,\n" + 
				"       r.haoml,\n" + 
				"       r.rultrmj,\n" + 
				"       r.farl,\n" + 
				"       r.meizbmdj,\n" + 
				"       r.haoyl,\n" + 
				"       r.rulyj,\n" + 
				"       r.youzbmdj,\n" + 
				"       r.meiyhjbml,\n" + 
				"       r.meiyzhbmdj,\n" + 
				"       r.fadl,\n" + 
				"       r.fadbzmh,\n" + 
				"       r.gongdl,\n" + 
				"       r.gongdbzmh,\n" + 
				"       r.faddwrlcb,\n" + 
				"       r.gongrl,\n" + 
				"       r.gongrbzmh,\n" + 
				"       r.gongrdwrlcb\n" + 
				"from rulmtqkb r\n" + 
				"where r.diancxxb_id = "+getTreeid()+"\n" + 
				"  and r.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')\n";
//				"order by decode(r.fenx,'当月',1,'本期',2,'同期',3,'差值',4,'计划',5,'计划差值',6,7)";
		ResultSetList rsl=con.getResultSetList(sql);
		
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			sqls[i] = "insert into rulmtqkb(id,diancxxb_id,riq,fenx,haoml,rultrmj,farl,meizbmdj,haoyl,rulyj,youzbmdj,meiyhjbml,meiyzhbmdj,fadl,fadbzmh,gongdl,gongdbzmh,faddwrlcb,gongrl,gongrbzmh,gongrdwrlcb,zhuangt) values("
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
					+ rsl.getString("haoml")+","+rsl.getString("rultrmj")+","+rsl.getString("farl")+","+rsl.getString("meizbmdj")+","+rsl.getString("haoyl")+","+rsl.getString("rulyj")+","+rsl.getString("youzbmdj")+","+rsl.getString("meiyhjbml")+","+rsl.getString("meiyzhbmdj")+","+rsl.getString("fadl")+","+rsl.getString("fadbzmh")+","+rsl.getString("gongdl")+","+rsl.getString("gongdbzmh")+","+rsl.getString("faddwrlcb")+","+rsl.getString("gongrl")+","+rsl.getString("gongrbzmh")+","+rsl.getString("gongrdwrlcb")
					+ ",0)";
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

			String sql1="update rulmtqkb set zhuangt=1 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//没有日志
		}else{
			System.out.print(riq1+"入炉煤炭情况表："+resul[0]);
			return;
		}
	}
	
	// 生成
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		int flag = -1;
		// 工具栏的年份和月份下拉框
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
		String riq1=intyear+"-"+intMonth+"-01";
		
		String sql = 
			"select d.mingc as diancxxb_id,\n" +
			"       bt.fenx,\n" + 
			"       nvl(dr.haoml,0) as haoml,\n" + 
			"       nvl(y.rultrmj,0) as rultrmj,\n" + 
			"       nvl(dr.farl,0) as farl,\n" + 
			"       decode(nvl(dr.farl,0),0,0,round_new(y.rultrmj*29.271/dr.farl,2)) as meizbmdj,\n" + 
			"       nvl(dr.haoyl,0) as haoyl,\n" + 
			"       0 as rulyj,\n" + 
			"       0 as youzbmdj,\n" + 
			"       nvl(y.meiyhjbml,0) as meiyhjbml,\n" + 
			"       0 as meiyzhbmdj,\n" + 
			"       nvl(dr.fadl,0) as fadl,\n" + 
			"       nvl(dr.fadbzmh,0) as fadbzmh,\n" + 
			"       nvl(y.gongdl,0) as gongdl,\n" + 
			"       nvl(y.gongdbzmh,0) as gongdbzmh,\n" + 
			"       nvl(y.faddwrlcb,0) as faddwrlcb,\n" + 
			"       nvl(dr.gongrl,0) as gongrl,\n" + 
			"       nvl(dr.gongrbzmh,0) as gongrbzmh,\n" + 
			"       0 as gongrdwrlcb\n" + 
			"from diancxxb d,\n" + 
			"(select d.id as diancxxb_id, fx.fenx\n" + 
			"  from diancxxb d,\n" + 
			"       (select '当月' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '本期' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '同期' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '差值' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '计划' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '计划差值' as fenx from dual) fx\n" + 
			" where d.id = " + getTreeid() + "\n" + 
			") bt,\n" + 
			"--diaor01b取数\n" + 
			"(--当月\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '当月' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '本月'\n" + 
			"  and dr.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--本期\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '本期' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '累计'\n" + 
			"  and dr.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--同期\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '同期' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '累计'\n" + 
			"  and dr.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12))\n" + 
			"union\n" + 
			"--差值\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '差值' as fenx,\n" + 
			"       nvl(bq.haoml,0)-nvl(tq.haoml,0) as haoml,\n" + 
			"       nvl(bq.farl,0)-nvl(tq.farl,0) as farl,\n" + 
			"       nvl(bq.haoyl,0)-nvl(tq.haoyl,0) as haoyl,\n" + 
			"       nvl(bq.fadl,0)-nvl(tq.fadl,0) as fadl,\n" + 
			"       nvl(bq.fadbzmh,0)-nvl(tq.fadbzmh,0) as fadbzmh,\n" + 
			"       nvl(bq.gongrl,0)-nvl(tq.gongrl,0) as gongrl,\n" + 
			"       nvl(bq.gongrbzmh,0)-nvl(tq.gongrbzmh,0) as gongrbzmh\n" + 
			"from\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '本期' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '累计'\n" + 
			"  and dr.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')) bq,\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '同期' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '累计'\n" + 
			"  and dr.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12)) tq)\n" + 
			"union\n" + 
			"--计划\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"        '计划' as fenx,\n" + 
			"        0 as haoml,\n" + 
			"        0 as farl,\n" + 
			"        0 as haoyl,\n" + 
			"        0 as fadl,\n" + 
			"        0 as fadbzmh,\n" + 
			"        0 as gongrl,\n" + 
			"        0 as gongrbzmh\n" + 
			"from dual)\n" + 
			"union\n" + 
			"--计划差值\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"        '计划差值' as fenx,\n" + 
			"        0 as haoml,\n" + 
			"        0 as farl,\n" + 
			"        0 as haoyl,\n" + 
			"        0 as fadl,\n" + 
			"        0 as fadbzmh,\n" + 
			"        0 as gongrl,\n" + 
			"        0 as gongrbzmh\n" + 
			"from dual)) dr,\n" + 
			"--diaor01b取数结束\n" + 
			"--yuezbb取数\n" + 
			"(--当月\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '当月' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '本月'\n" + 
			"  and y.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--本期\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '本期' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '累计'\n" + 
			"  and y.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--同期\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '同期' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '累计'\n" + 
			"  and y.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12))\n" + 
			"union\n" + 
			"--差值\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '差值' as fenx,\n" + 
			"       nvl(bq.rultrmj,0)-nvl(tq.rultrmj,0) as rultrmj,\n" + 
			"       nvl(bq.meiyhjbml,0)-nvl(tq.meiyhjbml,0) as meiyhjbml,\n" + 
			"       nvl(bq.gongdl,0)-nvl(tq.gongdl,0) as gongdl,\n" + 
			"       nvl(bq.gongdbzmh,0)-nvl(tq.gongdbzmh,0) as gongdbzmh,\n" + 
			"       nvl(bq.faddwrlcb,0)-nvl(tq.faddwrlcb,0) as faddwrlcb\n" + 
			"from\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '本期' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '累计'\n" + 
			"  and y.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')) bq,\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '同期' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '累计'\n" + 
			"  and y.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12)) tq)\n" + 
			"union\n" + 
			"--计划\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '计划' as fenx,\n" + 
			"       0 as rultrmj,\n" + 
			"       0 as meiyhjbml,\n" + 
			"       0 as gongdl,\n" + 
			"       0 as gongdbzmh,\n" + 
			"       0 as faddwrlcb\n" + 
			"from yuezbb y)\n" + 
			"union\n" + 
			"--计划差值\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '计划差值' as fenx,\n" + 
			"       0 as rultrmj,\n" + 
			"       0 as meiyhjbml,\n" + 
			"       0 as gongdl,\n" + 
			"       0 as gongdbzmh,\n" + 
			"       0 as faddwrlcb\n" + 
			"from yuezbb y)\n" + 
			") y\n" + 
			"--yuezbb取数结束\n" + 
			"where bt.diancxxb_id = d.id\n" + 
			"  and bt.diancxxb_id = dr.diancxxb_id(+)\n" + 
			"  and bt.diancxxb_id = y.diancxxb_id(+)\n" + 
			"  and bt.fenx = dr.fenx(+)\n" + 
			"  and bt.fenx = y.fenx(+)\n" + 
			"order by decode(bt.fenx,'当月',1,'本期',2,'同期',3,'差值',4,'计划',5,'计划差值',6,7)";

		ResultSetList rslcopy = con.getResultSetList(sql);
		con.getDelete("delete rulmtqkb where diancxxb_id = "+ getTreeid()+" and riq = to_date('"+riq1+"','yyyy-MM-dd')\n");
		while (rslcopy.next()) {
//			String diancxxb_id = rslcopy.getString("diancxxb_id");
//			Date riq = rslcopy.getDate("riq");
			String fenx = rslcopy.getString("fenx");
			double haoml = rslcopy.getDouble("haoml");
			double rultrmj = rslcopy.getDouble("rultrmj");
			double farl = rslcopy.getDouble("farl");
			double meizbmdj = rslcopy.getDouble("meizbmdj");
			double haoyl = rslcopy.getDouble("haoyl");
			double rulyj = rslcopy.getDouble("rulyj");
			double youzbmdj = rslcopy.getDouble("youzbmdj");
			double meiyhjbml = rslcopy.getDouble("meiyhjbml");
			double meiyzhbmdj = rslcopy.getDouble("meiyzhbmdj");
			double fadl = rslcopy.getDouble("fadl");
			double fadbzmh = rslcopy.getDouble("fadbzmh");
			double gongdl = rslcopy.getDouble("gongdl");
			double gongdbzmh = rslcopy.getDouble("gongdbzmh");
			double faddwrlcb = rslcopy.getDouble("faddwrlcb");
			double gongrl = rslcopy.getDouble("gongrl");
			double gongrbzmh = rslcopy.getDouble("gongrbzmh");
			double gongrdwrlcb = rslcopy.getDouble("gongrdwrlcb");
			
			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			
			flag = con.getInsert("insert into rulmtqkb(id,diancxxb_id,riq,fenx,haoml,rultrmj,farl,meizbmdj,haoyl,rulyj,youzbmdj,meiyhjbml,meiyzhbmdj,fadl,fadbzmh,gongdl,gongdbzmh,faddwrlcb,gongrl,gongrbzmh,gongrdwrlcb) values("
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
							+haoml+","+rultrmj+","+farl+","+meizbmdj+","+haoyl+","+rulyj+","+youzbmdj+","+meiyhjbml+","+meiyzhbmdj+","+fadl+","+fadbzmh+","+gongdl+","+gongdbzmh+","+faddwrlcb+","+gongrl+","+gongrbzmh+","+gongrdwrlcb
							+ ")");

		}
		
		if(flag!=-1){
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
			"       r.diancxxb_id,\n" + 
			"       r.fenx,\n" + 
			"       r.riq,\n" + 
			"       r.haoml,\n" + 
			"       r.rultrmj,\n" + 
			"       r.farl,\n" + 
			"       r.meizbmdj,\n" + 
			"       r.haoyl,\n" + 
			"       r.rulyj,\n" + 
			"       r.youzbmdj,\n" + 
			"       r.meiyhjbml,\n" + 
			"       r.meiyzhbmdj,\n" + 
			"       r.fadl,\n" + 
			"       r.fadbzmh,\n" + 
			"       r.gongdl,\n" + 
			"       r.gongdbzmh,\n" + 
			"       r.faddwrlcb,\n" + 
			"       r.gongrl,\n" + 
			"       r.gongrbzmh,\n" + 
			"       r.gongrdwrlcb\n" + 
			"from rulmtqkb r\n" + 
			"where r.diancxxb_id = "+getTreeid()+"\n" + 
			"  and r.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')\n" + 
			"order by decode(r.fenx,'当月',1,'本期',2,'同期',3,'差值',4,'计划',5,'计划差值',6,7)";

		rsl = con.getResultSetList(sql);	
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rulmtqkb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("haoml").setHeader("耗煤量");
		egu.getColumn("rultrmj").setHeader("入炉天然煤价");
		egu.getColumn("farl").setHeader("发热量");
		egu.getColumn("meizbmdj").setHeader("煤折标煤单价");
		egu.getColumn("haoyl").setHeader("耗油量");
		egu.getColumn("rulyj").setHeader("入炉油价");
		egu.getColumn("youzbmdj").setHeader("油折标煤单价");
		egu.getColumn("meiyhjbml").setHeader("煤油合计标煤量");
		egu.getColumn("meiyzhbmdj").setHeader("煤油综合标煤单价");
		egu.getColumn("fadl").setHeader("发电量");
		egu.getColumn("fadbzmh").setHeader("发电标准煤耗");
		egu.getColumn("gongdl").setHeader("供电量");
		egu.getColumn("gongdbzmh").setHeader("供电标准煤耗");
		egu.getColumn("faddwrlcb").setHeader("发电单位燃料成本");
		egu.getColumn("gongrl").setHeader("供热量");
		egu.getColumn("gongrbzmh").setHeader("供热标准煤耗");
		egu.getColumn("gongrdwrlcb").setHeader("供热单位燃料成本");
		egu.setDefaultsortable(false);
		// 设定列初始宽度
//		egu.getColumn("riq").setWidth(80);
//		egu.getColumn("rez").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(30);// 设置分页

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
			" 	if(e.row==3||e.row==5){ e.cancel=true;}" +//第4行差值行不允许编辑
			"});" +
		
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"   if(e.row==1||e.row==2){\n" +
			"		var rec1=gridDiv_ds.getAt(1);\n" +
			"		var rec2=gridDiv_ds.getAt(2);\n" +
			"		var rec3=gridDiv_ds.getAt(3);\n" +
			"		rec3.set(e.field,rec1.get(e.field)-rec2.get(e.field));\n" +
			"	}\n" +
			"   if(e.row==1||e.row==4){\n" +
			"		var rec1=gridDiv_ds.getAt(1);\n" +
			"		var rec4=gridDiv_ds.getAt(4);\n" +
			"		var rec5=gridDiv_ds.getAt(5);\n" +
			"		if(e.field=='MEIYZHBMDJ'){\n" +
			"			rec5.set('MEIYZHBMDJ',rec1.get('MEIYZHBMDJ')-rec4.get('MEIYZHBMDJ'));\n" +
			"		}\n" +
			"	}\n" +
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
		
		String sql = "select zhuangt from rulmtqkb r\n" + 
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
		}
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
}
