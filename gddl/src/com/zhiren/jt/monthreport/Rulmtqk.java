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

	// ҳ��仯��¼
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
		// ���·���1��ʱ����ʾ01,
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
		InterFac_dt Shangb=new InterFac_dt();	//ʵ�����ӿڱ���
		resul=Shangb.sqlExe(diancxxb_id, sqls, false);
		
		if(resul[0].equals("true")){
			//1�õ�ǰ����״̬Ϊ2

			String sql1="delete rulmtqkb \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//û����־
		}else{
			System.out.print("ͬ���޸�ʧ�ܣ�"+resul[0]);
			return;
		}
	}
	
	//�����޸�
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
		// ���·���1��ʱ����ʾ01,
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
		InterFac_dt Shangb=new InterFac_dt();	//ʵ�����ӿڱ���
		resul=Shangb.sqlExe(diancxxb_id, sqls, false);
		
		if(resul[0].equals("true")){
			//1�õ�ǰ����״̬Ϊ2
			String sql1="update ruLmtqkb set zhuangt=2 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//û����־
		}else{
			System.out.print("�����޸�ʧ�ܣ�"+resul[0]);
			return;
		}
	}
	
	//�ϱ�
	public void Shangb(){
		JDBCcon con = new JDBCcon();
//		if(getDiancTreeJib()!=3){
//			setMsg("��ѡ�����糧��");
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
		// ���·���1��ʱ����ʾ01,
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
//				"order by decode(r.fenx,'����',1,'����',2,'ͬ��',3,'��ֵ',4,'�ƻ�',5,'�ƻ���ֵ',6,7)";
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
		InterFac_dt Shangb=new InterFac_dt();	//ʵ�����ӿڱ���
		resul=Shangb.sqlExe(diancxxb_id, sqls, false);
		
		if(resul[0].equals("true")){
			//1�õ�ǰ����״̬Ϊ1

			String sql1="update rulmtqkb set zhuangt=1 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//û����־
		}else{
			System.out.print(riq1+"��¯ú̿�����"+resul[0]);
			return;
		}
	}
	
	// ����
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		int flag = -1;
		// ����������ݺ��·�������
		if(getDiancTreeJib()!=3){
			setMsg("��ѡ�����糧��");
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
		// ���·���1��ʱ����ʾ01,
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
			"       (select '����' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '����' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select 'ͬ��' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '��ֵ' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '�ƻ�' as fenx\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '�ƻ���ֵ' as fenx from dual) fx\n" + 
			" where d.id = " + getTreeid() + "\n" + 
			") bt,\n" + 
			"--diaor01bȡ��\n" + 
			"(--����\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '����' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '����'\n" + 
			"  and dr.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--����\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '����' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '�ۼ�'\n" + 
			"  and dr.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--ͬ��\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       'ͬ��' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '�ۼ�'\n" + 
			"  and dr.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12))\n" + 
			"union\n" + 
			"--��ֵ\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '��ֵ' as fenx,\n" + 
			"       nvl(bq.haoml,0)-nvl(tq.haoml,0) as haoml,\n" + 
			"       nvl(bq.farl,0)-nvl(tq.farl,0) as farl,\n" + 
			"       nvl(bq.haoyl,0)-nvl(tq.haoyl,0) as haoyl,\n" + 
			"       nvl(bq.fadl,0)-nvl(tq.fadl,0) as fadl,\n" + 
			"       nvl(bq.fadbzmh,0)-nvl(tq.fadbzmh,0) as fadbzmh,\n" + 
			"       nvl(bq.gongrl,0)-nvl(tq.gongrl,0) as gongrl,\n" + 
			"       nvl(bq.gongrbzmh,0)-nvl(tq.gongrbzmh,0) as gongrbzmh\n" + 
			"from\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       '����' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '�ۼ�'\n" + 
			"  and dr.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')) bq,\n" + 
			"(select dr.diancxxb_id,\n" + 
			"       'ͬ��' as fenx,\n" + 
			"       dr.meithyhj as haoml,\n" + 
			"       dr.zonghm as farl,\n" + 
			"       dr.shiyhyhj as haoyl,\n" + 
			"       dr.fadl,\n" + 
			"       dr.biaozmhfd as fadbzmh,\n" + 
			"       dr.gongrl,\n" + 
			"       dr.biaozmhgr as gongrbzmh\n" + 
			"from diaor01bb dr\n" + 
			"where dr.diancxxb_id = " + getTreeid() + "\n" + 
			"  and dr.fenx = '�ۼ�'\n" + 
			"  and dr.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12)) tq)\n" + 
			"union\n" + 
			"--�ƻ�\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"        '�ƻ�' as fenx,\n" + 
			"        0 as haoml,\n" + 
			"        0 as farl,\n" + 
			"        0 as haoyl,\n" + 
			"        0 as fadl,\n" + 
			"        0 as fadbzmh,\n" + 
			"        0 as gongrl,\n" + 
			"        0 as gongrbzmh\n" + 
			"from dual)\n" + 
			"union\n" + 
			"--�ƻ���ֵ\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"        '�ƻ���ֵ' as fenx,\n" + 
			"        0 as haoml,\n" + 
			"        0 as farl,\n" + 
			"        0 as haoyl,\n" + 
			"        0 as fadl,\n" + 
			"        0 as fadbzmh,\n" + 
			"        0 as gongrl,\n" + 
			"        0 as gongrbzmh\n" + 
			"from dual)) dr,\n" + 
			"--diaor01bȡ������\n" + 
			"--yuezbbȡ��\n" + 
			"(--����\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '����' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '����'\n" + 
			"  and y.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--����\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '����' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '�ۼ�'\n" + 
			"  and y.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd'))\n" + 
			"union\n" + 
			"--ͬ��\n" + 
			"(select y.diancxxb_id,\n" + 
			"       'ͬ��' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '�ۼ�'\n" + 
			"  and y.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12))\n" + 
			"union\n" + 
			"--��ֵ\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '��ֵ' as fenx,\n" + 
			"       nvl(bq.rultrmj,0)-nvl(tq.rultrmj,0) as rultrmj,\n" + 
			"       nvl(bq.meiyhjbml,0)-nvl(tq.meiyhjbml,0) as meiyhjbml,\n" + 
			"       nvl(bq.gongdl,0)-nvl(tq.gongdl,0) as gongdl,\n" + 
			"       nvl(bq.gongdbzmh,0)-nvl(tq.gongdbzmh,0) as gongdbzmh,\n" + 
			"       nvl(bq.faddwrlcb,0)-nvl(tq.faddwrlcb,0) as faddwrlcb\n" + 
			"from\n" + 
			"(select y.diancxxb_id,\n" + 
			"       '����' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '�ۼ�'\n" + 
			"  and y.riq = to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')) bq,\n" + 
			"(select y.diancxxb_id,\n" + 
			"       'ͬ��' as fenx,\n" + 
			"       y.rultrmpjdj as rultrmj,\n" + 
			"       y.rulmzbzml+y.rulyzbzml as meiyhjbml,\n" + 
			"       y.gongdl,\n" + 
			"       y.gongdbzmh,\n" + 
			"       y.faddwrlcb\n" + 
			"from yuezbb y\n" + 
			"where y.diancxxb_id = " + getTreeid() + "\n" + 
			"  and y.fenx = '�ۼ�'\n" + 
			"  and y.riq = add_months(trunc(to_date('"+intyear+"-"+StrMonth+"-01','yyyy-MM-dd')),-12)) tq)\n" + 
			"union\n" + 
			"--�ƻ�\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '�ƻ�' as fenx,\n" + 
			"       0 as rultrmj,\n" + 
			"       0 as meiyhjbml,\n" + 
			"       0 as gongdl,\n" + 
			"       0 as gongdbzmh,\n" + 
			"       0 as faddwrlcb\n" + 
			"from yuezbb y)\n" + 
			"union\n" + 
			"--�ƻ���ֵ\n" + 
			"(select " + getTreeid() + " as diancxxb_id,\n" + 
			"       '�ƻ���ֵ' as fenx,\n" + 
			"       0 as rultrmj,\n" + 
			"       0 as meiyhjbml,\n" + 
			"       0 as gongdl,\n" + 
			"       0 as gongdbzmh,\n" + 
			"       0 as faddwrlcb\n" + 
			"from yuezbb y)\n" + 
			") y\n" + 
			"--yuezbbȡ������\n" + 
			"where bt.diancxxb_id = d.id\n" + 
			"  and bt.diancxxb_id = dr.diancxxb_id(+)\n" + 
			"  and bt.diancxxb_id = y.diancxxb_id(+)\n" + 
			"  and bt.fenx = dr.fenx(+)\n" + 
			"  and bt.fenx = y.fenx(+)\n" + 
			"order by decode(bt.fenx,'����',1,'����',2,'ͬ��',3,'��ֵ',4,'�ƻ�',5,'�ƻ���ֵ',6,7)";

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
			setMsg("���ɳɹ���");
		}
		
		getSelectData(null);
		con.Close();
	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������

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
		// ���·���1��ʱ����ʾ01,
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
			"order by decode(r.fenx,'����',1,'����',2,'ͬ��',3,'��ֵ',4,'�ƻ�',5,'�ƻ���ֵ',6,7)";

		rsl = con.getResultSetList(sql);	
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rulmtqkb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("haoml").setHeader("��ú��");
		egu.getColumn("rultrmj").setHeader("��¯��Ȼú��");
		egu.getColumn("farl").setHeader("������");
		egu.getColumn("meizbmdj").setHeader("ú�۱�ú����");
		egu.getColumn("haoyl").setHeader("������");
		egu.getColumn("rulyj").setHeader("��¯�ͼ�");
		egu.getColumn("youzbmdj").setHeader("���۱�ú����");
		egu.getColumn("meiyhjbml").setHeader("ú�ͺϼƱ�ú��");
		egu.getColumn("meiyzhbmdj").setHeader("ú���ۺϱ�ú����");
		egu.getColumn("fadl").setHeader("������");
		egu.getColumn("fadbzmh").setHeader("�����׼ú��");
		egu.getColumn("gongdl").setHeader("������");
		egu.getColumn("gongdbzmh").setHeader("�����׼ú��");
		egu.getColumn("faddwrlcb").setHeader("���絥λȼ�ϳɱ�");
		egu.getColumn("gongrl").setHeader("������");
		egu.getColumn("gongrbzmh").setHeader("���ȱ�׼ú��");
		egu.getColumn("gongrdwrlcb").setHeader("���ȵ�λȼ�ϳɱ�");
		egu.setDefaultsortable(false);
		// �趨�г�ʼ���
//		egu.getColumn("riq").setWidth(80);
//		egu.getColumn("rez").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(30);// ���÷�ҳ

		// *****************************************����Ĭ��ֵ****************************
		
//		// �������ڵ�Ĭ��ֵ,
//		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// �趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);
		
		if(visit.getRenyjb()==3&&getZhuangt()<=0){
//			StringBuffer cpb = new StringBuffer();
//			cpb.append("function(){").append("document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("����",getBtnHandlerScript("CopyButton"));
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		
		if(visit.getRenyjb()==3&&getZhuangt()==0){
//			�ϱ���ť
			GridButton gbs = new GridButton("�ϱ�",
					getBtnHandlerScript("ShangbButton"));
			gbs.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbs);
		}
		
		if(visit.getRenyjb()==3&&getZhuangt()==1){
//			�����޸İ�ť
			GridButton gbs = new GridButton("�����޸�",
					getBtnHandlerScript("ShenqxgButton"));
			gbs.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbs);
		}
		
		if(visit.getRenyjb()==2&&getZhuangt()==1){
//			�����޸İ�ť
			GridButton gbs = new GridButton("ͬ��",
					getBtnHandlerScript("TongyButton"));
			gbs.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbs);
		}

//		 ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		
		String sb_str=
			"gridDiv_grid.on('beforeedit',function(e){" +
			" 	if(e.row==3||e.row==5){ e.cancel=true;}" +//��4�в�ֵ�в�����༭
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
		// ---------------ҳ��js�������--------------------------
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
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
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		StringBuffer btnsb = new StringBuffer();
		String cnDate = intyear + "��" + StrMonth + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CopyButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} else if(btnName.endsWith("ShangbButton")){
			btnsb.append("�����ϱ��������޸ģ��Ƿ������");
		} else if(btnName.endsWith("ShenqxgButton")){
			btnsb.append("�����").append(cnDate).append("���ݽ����޸ģ��Ƿ������");
		} else if(btnName.endsWith("TongyButton")){
			btnsb.append("ͬ���").append(cnDate).append("���ݽ����޸ģ��Ƿ������");
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
		// ���·���1��ʱ����ʾ01,
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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

	// ���
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

	// �·�
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
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
			// TODO �Զ����� catch ��
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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
