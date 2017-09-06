package com.zhiren.dc.monthReport;

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
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:ly
 * ʱ��:2010-04-28
 * ����:�볧��ú���۷���
 */
import com.zhiren.webservice.InterFac_dt;

public class Rucbmdjwh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// ����������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		String tableName="rucbmdjfxb";
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			sql.append("update ").append(tableName).append(" set ")
				.append("benqljl=").append(mdrsl.getString("benqljl")).append(",")
				.append("benqlj=").append(mdrsl.getString("benqlj")).append(",")
				.append("shangqljl=").append(mdrsl.getString("shangqljl")).append(",")
				.append("shangqlj=").append(mdrsl.getString("shangqlj")).append(",")
				.append("shangnpjl=").append(mdrsl.getString("shangnpjl")).append(",")
				.append("shangnpj=").append(mdrsl.getString("shangnpj")).append(",")
				.append("benywcl=").append(mdrsl.getString("benywcl")).append(",")
				.append("benywc=").append(mdrsl.getString("benywc")).append(",")
				.append("shangywcl=").append(mdrsl.getString("shangywcl")).append(",")
				.append("shangywc=").append(mdrsl.getString("shangywc"))
			    .append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			setMsg("����ɹ���");
//			Countlj(); //�����ۼ���
		}
		con.Close();
		sql=null;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
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
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
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
		getSelectData();
	}
	
	public void Tongy(){
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String diancID = "	and diancxxb_id = " + this.getTreeid() +"\n";
		String sql = "SELECT ID\n" +
					 " FROM RUCBMDJFXB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=1\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "update RUCBMDJFXB set zhuangt=0 where id = "+rsl.getString("id")+"\n";
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

			String sql1="delete rucbmdjfxb \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//û����־
		}else{
			System.out.print("ͬ���޸�ʧ�ܣ�"+resul[0]);
			return;
		}
	}
	
	public void isXiug(){
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String diancID = "	and diancxxb_id = " + this.getTreeid() +"\n";
		String sql = "SELECT ID\n" +
					 " FROM RUCBMDJFXB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=1\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "update RUCBMDJFXB set zhuangt=1 where id = "+rsl.getString("id")+"\n";
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

			String sql1="update rucbmdjfxb set zhuangt=2 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//û����־
		}else{
			System.out.print("�����޸�ʧ�ܣ�"+resul[0]);
			return;
		}
	}
	
	public void Shangb(){
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String diancID = "	and diancxxb_id = " + this.getTreeid() +"\n";
		String sql = "SELECT ID,DIANCXXB_ID,TO_CHAR(RIQ,'yyyy-MM-dd') AS RIQ,LEIX,BENQLJL,BENQLJ,SHANGQLJL,SHANGQLJ,SHANGNPJL,SHANGNPJ,BENYWCL,BENYWC,SHANGYWCL,SHANGYWC,ZHUANGT \n" +
					 " FROM RUCBMDJFXB\n" +
					 " WHERE RIQ = "+CurrODate+"\n" + 
						diancID + 
					 "	AND ZHUANGT=0\n";
		ResultSetList rsl=con.getResultSetList(sql);
		int i = 0;
		String[] sqls = new String[rsl.getRows()];
		while(rsl.next()){
			
			sqls[i] = "INSERT INTO RUCBMDJFXB VALUES("+rsl.getString("ID")+","
													  +rsl.getString("DIANCXXB_ID")+","
													  +DateUtil.FormatOracleDate(rsl.getString("RIQ"))+",'"
													  +rsl.getString("LEIX")+"',"
													  +rsl.getString("BENQLJL")+","
													  +rsl.getString("BENQLJ")+","
													  +rsl.getString("SHANGQLJL")+","
													  +rsl.getString("SHANGQLJ")+","
													  +rsl.getString("SHANGNPJL")+","
													  +rsl.getString("SHANGNPJ")+","
													  +rsl.getString("BENYWCL")+","
													  +rsl.getString("BENYWC")+","
													  +rsl.getString("SHANGYWCL")+","
													  +rsl.getString("SHANGYWC")+","
													  +"0" +
					 ")\n";
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

			String sql1="update rucbmdjfxb set zhuangt=1 \n" +
						" where riq = "+CurrODate+"\n" + diancID;
			con.getUpdate(sql1);
			return;//û����־
		}else{
			System.out.print(getNianf() + "-" + getYuef() + "-01"+"�볧��ú�����ϴ�ʧ�ܣ�"+resul[0]);
			return;
		}
	}

	private String getSql(String diancxxb_id,boolean n){
		int lastYear = Integer.parseInt(getNianf())-1;
		int lastMonth = Integer.parseInt(getYuef())-1;
		String benq = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String shangq = DateUtil.FormatOracleDate(lastYear+"-"+getYuef()+"-01");
		String shangn = DateUtil.FormatOracleDate(lastYear+"-12-01");
		String shangy = "";
		if(Integer.parseInt(getYuef())==1){
			shangy = DateUtil.FormatOracleDate(lastYear+"-12-01");
		} else {
			shangy = DateUtil.FormatOracleDate(getNianf()+"-"+lastMonth+"-01");
		}
		
		String sql = "begin \n";
//			"	insert into rucbmdjfxb\n" +
//			"  	(id, diancxxb_id, riq, leix, benqljl, benqlj, shangqljl, shangqlj, shangnpjl, shangnpj, benywcl, benywc, shangywcl, shangywc, zhuangt)\n" + 
//			"	values\n" + 
//			"  	(getnewid("+diancxxb_id+"), "+diancxxb_id+", "+benq+", '"+getIDropDownDiancmc(diancxxb_id)+"',0,0,0,0,0,0,0,0,0,0, 0);\n";
			//ȼú
			if(n){
				sql += "insert into rucbmdjfxb\n" +
				"     (id, diancxxb_id, riq, leix, benqljl, benqlj, shangqljl, shangqlj, shangnpjl, shangnpj, benywcl, benywc, shangywcl, shangywc, zhuangt)\n" + 
				"     values(getnewid("+diancxxb_id+"),\n" + 
				"             "+diancxxb_id+",\n" + 
				"             to_date('2009-04-01','yyyy-MM-dd'),\n" + 
				"             'ȼú',\n" + 
				"             --�����ۼ���\n" + 
				"             nvl((select sum(s.laimsl)\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                and s.yuetjkjb_id = tj.id\n" + 
				"                and riq = "+benq+"\n" + 
				"				 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                and dj.fenx = '�ۼ�'),0),\n" + 
				"             --�����ۼ�\n" + 
				"             nvl((select decode(sum(s.laimsl),0,0,round_new(sum(biaomdj*s.laimsl)/sum(s.laimsl),2))\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                and s.yuetjkjb_id = tj.id\n" + 
				"                and riq = "+benq+"\n" + 
				"				 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                and dj.fenx = '�ۼ�'),0),\n" + 
				"             --�����ۼ���\n" + 
				"             nvl((select sum(s.laimsl)\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                and s.yuetjkjb_id = tj.id\n" + 
				"                and riq = "+shangq+"\n" + 
				"				 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                and dj.fenx = '�ۼ�'),0),\n" + 
				"             --�����ۼ�\n" + 
				"             nvl((select decode(sum(s.laimsl),0,0,round_new(sum(biaomdj*s.laimsl)/sum(s.laimsl),2))\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                and s.yuetjkjb_id = tj.id\n" + 
				"                and riq = "+shangq+"\n" + 
				"				 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                and dj.fenx = '�ۼ�'),0),\n" + 
				"             --����ƽ����\n" + 
				"             nvl((select sum(s.laimsl)\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                    and s.yuetjkjb_id = tj.id\n" + 
				"                    and riq = "+shangn+"\n" + 
				"					 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                    and dj.fenx = '�ۼ�'),0),\n" + 
				"             --����ƽ��\n" + 
				"             nvl((select decode(sum(s.laimsl),0,0,round_new(sum(biaomdj*s.laimsl)/sum(s.laimsl),2))\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                    and s.yuetjkjb_id = tj.id\n" + 
				"                    and riq = "+shangn+"\n" + 
				"					 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                    and dj.fenx = '�ۼ�'),0),\n" + 
				"             --���������\n" + 
				"             nvl((select sum(s.laimsl)\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                    and s.yuetjkjb_id = tj.id\n" + 
				"                    and riq = "+benq+"\n" + 
				"				 	 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                    and dj.fenx = '����'),0),\n" + 
				"             --�������\n" + 
				"             nvl((select decode(sum(s.laimsl),0,0,round_new(sum(biaomdj*s.laimsl)/sum(s.laimsl),2))\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                    and s.yuetjkjb_id = tj.id\n" + 
				"                    and riq = "+benq+"\n" + 
				"				 	 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                    and dj.fenx = '����'),0),\n" + 
				"             --���������\n" + 
				"             nvl((select sum(s.laimsl)\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                    and s.yuetjkjb_id = tj.id\n" + 
				"                    and riq = "+shangy+"\n" + 
				"				 	 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                    and dj.fenx = '����'),0),\n" + 
				"             --�������\n" + 
				"             nvl((select decode(sum(s.laimsl),0,0,round_new(sum(biaomdj*s.laimsl)/sum(s.laimsl),2))\n" + 
				"              from yuercbmdj dj,yuetjkjb tj,yueslb s\n" + 
				"              where dj.yuetjkjb_id=tj.id\n" + 
				"                    and s.yuetjkjb_id = tj.id\n" + 
				"                    and riq = "+shangy+"\n" + 
				"				 	 and tj.diancxxb_id = "+diancxxb_id+"\n" +
				"                    and dj.fenx = '����'),0),\n" + 
				"              0);\n";
			} else {
				sql += 
					"	insert into rucbmdjfxb\n" +
					"  	(id, diancxxb_id, riq, leix, benqljl, benqlj, shangqljl, shangqlj, shangnpjl, shangnpj, benywcl, benywc, shangywcl, shangywc, zhuangt)\n" + 
					"	values\n" + 
					"  	(getnewid("+diancxxb_id+"), "+diancxxb_id+", "+benq+", 'ȼú',0,0,0,0,0,0,0,0,0,0, 0);\n";
			}
			
			//ȼ��
			sql += 
				"	insert into rucbmdjfxb\n" +
				"  	(id, diancxxb_id, riq, leix, benqljl, benqlj, shangqljl, shangqlj, shangnpjl, shangnpj, benywcl, benywc, shangywcl, shangywc, zhuangt)\n" + 
				"	values\n" + 
				"  	(getnewid("+diancxxb_id+"), "+diancxxb_id+", "+benq+", 'ȼ��',0,0,0,0,0,0,0,0,0,0, 0);\n" +
				
				"	end;\n";

			
		return sql;
	}

	public void CreateData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id=this.getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql = "";
		if(Cleaning_up_History(CurrODate,"")){
			int flag = -1;	
			strSql = "select id from yuetjkjb where diancxxb_id = " + diancxxb_id +
			" and riq = " + CurrODate;
			ResultSetList rs=con.getResultSetList(strSql);
			if(rs.next()){
				flag = con.getInsert(getSql(diancxxb_id,true));
			} else {
				flag = con.getInsert(getSql(diancxxb_id,false));
			}
			rs.close();
			if(flag>=0){
				setMsg("�������ɳɹ�!");
			} else {
				setMsg("��������ʧ��!");
			}
			con.Close();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String diancID = "";
		int nian = Integer.parseInt(getNianf())-1;
		if(getDiancTreeJib()==2){
			diancID = "	and r.diancxxb_id=0 \n";
		} else if(getDiancTreeJib()==3){
			diancID = "	and r.diancxxb_id = " + this.getTreeid() +"\n";
		}
		
		String strSql = "";
		strSql =
			"select r.id,\n" +
			"		r.leix,\n" +
			"       r.benqljl,\n" + 
			"       r.benqlj,\n" + 
			"       r.shangqljl,\n" + 
			"       r.shangqlj,\n" + 
			"       r.benqlj-r.shangqlj as tongb,\n" + 
			"       r.shangnpjl,\n" + 
			"       r.shangnpj,\n" + 
			"       r.benqlj-r.shangnpj as nianb,\n" + 
			"       r.benywcl,\n" + 
			"       r.benywc,\n" + 
			"       r.shangywcl,\n" + 
			"       r.shangywc,\n" + 
			"       r.benywc-r.shangywc as huanb\n" + 
			"from rucbmdjfxb r\n" + 
			"where r.riq = "+CurrODate+"\n" + 
			diancID + 
			"	and r.zhuangt=0\n" +
			"order by decode(r.leix,'ȼú',1,'ȼ��',2,0)";

		JDBCcon con = new JDBCcon();

		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
					+ strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("rucbmdjfxb");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("leix").setWidth(60);
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("leix").setEditor(null);
		egu.getColumn("benqljl").setHeader("�����ۼ���");
		egu.getColumn("benqljl").setWidth(80);
		egu.getColumn("benqlj").setHeader("�����ۼ�");
		egu.getColumn("benqlj").setWidth(80);
		egu.getColumn("shangqljl").setHeader("�����ۼ���");
		egu.getColumn("shangqljl").setWidth(80);
		egu.getColumn("shangqlj").setHeader("�����ۼ�");
		egu.getColumn("shangqlj").setWidth(80);
		egu.getColumn("tongb").setHeader("ͬ�ȷ���");
		egu.getColumn("tongb").setEditor(null);
		egu.getColumn("tongb").setWidth(80);
		egu.getColumn("shangnpjl").setHeader(nian+"��ƽ����");
		egu.getColumn("shangnpjl").setWidth(100);
		egu.getColumn("shangnpj").setHeader(nian+"��ƽ��");
		egu.getColumn("shangnpj").setWidth(100);
		egu.getColumn("nianb").setHeader(nian+"��ƽ���ȷ���");
		egu.getColumn("nianb").setEditor(null);
		egu.getColumn("nianb").setWidth(110);
		egu.getColumn("benywcl").setHeader("���������");
		egu.getColumn("benywcl").setWidth(80);
		egu.getColumn("benywc").setHeader("�������");
		egu.getColumn("benywc").setWidth(80);
		egu.getColumn("shangywcl").setHeader("���������");
		egu.getColumn("shangywcl").setWidth(80);
		egu.getColumn("shangywc").setHeader("�������");
		egu.getColumn("shangywc").setWidth(80);
		egu.getColumn("huanb").setHeader("���ȷ���");
		egu.getColumn("huanb").setEditor(null);
		egu.getColumn("huanb").setWidth(80);

		egu.setDefaultsortable(false);

		// �������и�Ϊ���Ա༭�ġ�
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		// /���ð�ť
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		// �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		if(visit.getRenyjb()==3&&getZhuangt()<=0){
//			 ���ɰ�ť
			GridButton gbc = new GridButton("����",
					getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
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
		
		if(visit.getRenyjb()==2||(visit.getRenyjb()==3&&getZhuangt()==0)){
//			 ���水ť
			GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
					egu.getGridColumns(), "SaveButton");
			egu.addTbarBtn(gbs);
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"    var tongb=0,nianb=0,huanb=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    tongb=Round_new(eval(gridDiv_ds.getAt(i).get('BENQLJ'))-eval(gridDiv_ds.getAt(i).get('SHANGQLJ')),2);\n" + 
				"    nianb=Round_new(eval(gridDiv_ds.getAt(i).get('BENQLJ'))-eval(gridDiv_ds.getAt(i).get('SHANGNPJ')),2);\n" + 
				"    huanb=Round_new(eval(gridDiv_ds.getAt(i).get('BENYWC'))-eval(gridDiv_ds.getAt(i).get('SHANGYWC')),2);\n" + 
//				"    nianb=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),2);\n" + 
//				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS'))-eval(gridDiv_ds.getAt(i).get('YUNJS')))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),2);\n" + 
				"\n" + 
				"    gridDiv_ds.getAt(i).set('TONGB',tongb);\n" + 
				"    gridDiv_ds.getAt(i).set('NIANB',nianb);\n" + 
				"    gridDiv_ds.getAt(i).set('HUANB',huanb);\n" +
				"});"
		);	
	
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		con.Close();
	}
	
	public int getZhuangt(){
		int zt = -1;
		JDBCcon cn = new JDBCcon();
		String diancID = "	and r.diancxxb_id = " + this.getTreeid() +"\n";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		
		String sql = "select zhuangt from rucbmdjfxb r\n" + 
					"where r.riq = "+CurrODate+"\n" + 
						diancID + "\n";
		ResultSetList rsl = cn.getResultSetList(sql);
		if(rsl.next()){
			zt = rsl.getInt("zhuangt");
		}
		return zt;
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "��" + getYuef() + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
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
//-----------------  ���������ͷż�¼��   ----------------------------------------------------
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			this.setTreeid(null);
			setRiq();
			getSelectData();
		}
		getSelectData();
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ getTreeid();
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
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
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	boolean treechange = false;

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
		return getTree().getWindowTreeScript();
	}
	
	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
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

	// ---------------------------------------------------------------------------------------
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	// ���������
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
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�������
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
	
	private boolean Cleaning_up_History(String Date,String Condition){
//		���û���������ɡ���ťʱ��Ҫ����ɾ��yuercbmdj���������¼���
		JDBCcon con = new JDBCcon();
		String sql="";
		boolean Falg=false;
		String diancxxb_id=this.getTreeid();
		
		sql=
				"delete from rucbmdjfxb r where r.diancxxb_id=" +diancxxb_id+"\n"+
				"             and r.riq="+Date+"\n" +Condition;
		
		if(con.getDelete(sql)>=0){
			
			Falg=true;
		}
		con.Close();
		return Falg;
	}
}