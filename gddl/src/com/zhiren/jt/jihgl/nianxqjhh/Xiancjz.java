package com.zhiren.jt.jihgl.nianxqjhh;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiancjz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		JDBCcon con = new JDBCcon();
		
//		 ����������ݺ��·�������
		
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		long diancxxb_id=Long.parseLong(this.getTreeid());
		//�ж��Ƿ������ݣ����û�о����
		
		String sbsql="select * from nianxqjhh where nianf="+nextyear+" and jizzt=0 and diancxxb_id="+diancxxb_id;
		
		if (!con.getHasIt(sbsql)){//���û���������һ���µ����ݡ�
			Insert();
		}
		
		con.setAutoCommit(false);
		int flag =0;

		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange().replaceAll("&nbsp;", ""));//��getChange�е�&nbsp;�滻��"",����ext����ʶ��
		
		String zhi_sn1="";
		String zhi_jn1="";
		String zhi_nn1="";
		
		//����SQL
		StringBuffer snsb1 = new StringBuffer();
		StringBuffer jnsb1 = new StringBuffer();
		StringBuffer nnsb1 = new StringBuffer();
	
		//��������
		StringBuffer snsj1 = new StringBuffer();
		StringBuffer jnsj1 = new StringBuffer();
		StringBuffer nnsj1 = new StringBuffer();
		
		snsb1.append("update nianxqjhh set ");
		jnsb1.append("update nianxqjhh set ");
		nnsb1.append("update nianxqjhh set ");
		
		while (rsl.next()) {
				
			switch(rsl.getInt("LANC")){
				case 1:
					zhi_sn1 = "FADL="+rsl.getString("lastyear");
					zhi_jn1 = "FADL="+rsl.getString("intyear");
					zhi_nn1 = "FADL="+rsl.getString("nextyear");
					break;
				case 2:
					zhi_sn1 = "GONGDBZMH="+rsl.getString("lastyear");
					zhi_jn1 = "GONGDBZMH="+rsl.getString("intyear");
					zhi_nn1 = "GONGDBZMH="+rsl.getString("nextyear");
					break;
				case 3:
					zhi_sn1 = "ZONGHCYDL="+rsl.getString("lastyear");
					zhi_jn1 = "ZONGHCYDL="+rsl.getString("intyear");
					zhi_nn1 = "ZONGHCYDL="+rsl.getString("nextyear");
					break;
				case 4:
					zhi_sn1 = "FADBHM="+rsl.getString("lastyear");
					zhi_jn1 = "FADBHM="+rsl.getString("intyear");
					zhi_nn1 = "FADBHM="+rsl.getString("nextyear");
					break;
				case 5:
					zhi_sn1 = "GONGRL="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRL="+rsl.getString("intyear");
					zhi_nn1 = "GONGRL="+rsl.getString("nextyear");
					break;
				case 6:
					zhi_sn1 = "GONGRBZMH="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRBZMH="+rsl.getString("intyear");
					zhi_nn1 = "GONGRBZMH="+rsl.getString("nextyear");
					break;
				case 7:
					zhi_sn1 = "FADXYBML="+rsl.getString("lastyear");
					zhi_jn1 = "FADXYBML="+rsl.getString("intyear");
					zhi_nn1 = "FADXYBML="+rsl.getString("nextyear");
					break;
				case 8:
					zhi_sn1 = "GONGRXYBML="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRXYBML="+rsl.getString("intyear");
					zhi_nn1 = "GONGRXYBML="+rsl.getString("nextyear");
					break;
				case 9:
					zhi_sn1 = "XUYBML="+rsl.getString("lastyear");
					zhi_jn1 = "XUYBML="+rsl.getString("intyear");
					zhi_nn1 = "XUYBML="+rsl.getString("nextyear");
					break;
				case 10:
					zhi_sn1 = "DIANHZRYL="+rsl.getString("lastyear");
					zhi_jn1 = "DIANHZRYL="+rsl.getString("intyear");
					zhi_nn1 = "DIANHZRYL="+rsl.getString("nextyear");
					break;
				case 11:
					zhi_sn1 = "YOUFRL="+rsl.getString("lastyear");
					zhi_jn1 = "YOUFRL="+rsl.getString("intyear");
					zhi_nn1 = "YOUFRL="+rsl.getString("nextyear");
					break;
				case 12: 
					zhi_sn1 = "";
					zhi_jn1 = "";
					zhi_nn1 = "";
					continue;
				case 13:
					zhi_sn1 = "RULRZ="+rsl.getString("lastyear");
					zhi_jn1 = "RULRZ="+rsl.getString("intyear");
					zhi_nn1 = "RULRZ="+rsl.getString("nextyear");
					break;	
				case 14:
					zhi_sn1 = "FADXYML="+rsl.getString("lastyear");
					zhi_jn1 = "FADXYML="+rsl.getString("intyear");
					zhi_nn1 = "FADXYML="+rsl.getString("nextyear");
					break;	
				case 15:
					zhi_sn1 = "GONGRXYML="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRXYML="+rsl.getString("intyear");
					zhi_nn1 = "GONGRXYML="+rsl.getString("nextyear");
					break;	
				case 16:
					zhi_sn1 = "XUYYML="+rsl.getString("lastyear");
					zhi_jn1 = "XUYYML="+rsl.getString("intyear");
					zhi_nn1 = "XUYYML="+rsl.getString("nextyear");
					break;	
				case 17:
					zhi_sn1 = "QITY="+rsl.getString("lastyear");
					zhi_jn1 = "QITY="+rsl.getString("intyear");
					zhi_nn1 = "QITY="+rsl.getString("nextyear");
					break;
				case 18:
					zhi_sn1 = "YUNS="+rsl.getString("lastyear");
					zhi_jn1 = "YUNS="+rsl.getString("intyear");
					zhi_nn1 = "YUNS="+rsl.getString("nextyear");
					break;
				case 19:
					zhi_sn1 = "QICKC="+rsl.getString("lastyear");
					zhi_jn1 = "QICKC="+rsl.getString("intyear");
					zhi_nn1 = "QICKC="+rsl.getString("nextyear");
					break;
				case 20:
					zhi_sn1 = "QIMKC="+rsl.getString("lastyear");
					zhi_jn1 = "QIMKC="+rsl.getString("intyear");
					zhi_nn1 = "QIMKC="+rsl.getString("nextyear");
					break;
				case 21:
					zhi_sn1 = "ZONGXQL="+rsl.getString("lastyear");
					zhi_jn1 = "ZONGXQL="+rsl.getString("intyear");
					zhi_nn1 = "ZONGXQL="+rsl.getString("nextyear");
					break;	
				case 22:
					zhi_sn1 = "ZHUANGJRL="+rsl.getString("lastyear");
					zhi_jn1 = "ZHUANGJRL="+rsl.getString("intyear");
					zhi_nn1 = "ZHUANGJRL="+rsl.getString("nextyear");
					break;
				case 23:
					zhi_sn1 = "MIAOS='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "MIAOS='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "MIAOS='"+rsl.getString("nextyear")+"'";
					break;
				case 24:
					zhi_sn1 = "TOUCRQ='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "TOUCRQ='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "TOUCRQ='"+rsl.getString("nextyear")+"'";
					break;
				case 25:
					zhi_sn1 = "SHEJMZ='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "SHEJMZ='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "SHEJMZ='"+rsl.getString("nextyear")+"'";
					break;
				case 26:
					zhi_sn1 = "BEIZ='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "BEIZ='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "BEIZ='"+rsl.getString("nextyear")+"'";
					break;
				default: break;
			}
			if(!zhi_sn1.equals("")){
				snsj1.append(zhi_sn1).append(",");
			}
			if(!zhi_jn1.equals("")){
				jnsj1.append(zhi_jn1).append(",");
			}
			if(!zhi_nn1.equals("")){
				nnsj1.append(zhi_nn1).append(",");
			}
				
		}
		
		if(snsj1.length()!=0){
			snsj1.deleteCharAt(snsj1.length()-1);
			snsb1.append(snsj1);
			snsb1.append(" where ").append("nianf="+nextyear+"").append(" and ");
			snsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(lastyear);
			System.out.println(snsb1.toString());
			flag=con.getUpdate(snsb1.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ snsb1.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(jnsj1.length()!=0){
			jnsj1.deleteCharAt(jnsj1.length()-1);
			jnsb1.append(jnsj1);
			jnsb1.append(" where ").append("nianf="+nextyear+"").append(" and ");
			jnsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(intyear);
			System.out.println(jnsb1.toString());
			flag=con.getUpdate(jnsb1.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ jnsb1.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(nnsj1.length()!=0){
			nnsj1.deleteCharAt(nnsj1.length()-1);
			nnsb1.append(nnsj1);
			nnsb1.append(" where ").append("nianf="+nextyear+"").append(" and ");
			nnsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(nextyear);
			System.out.println(nnsb1.toString());
			flag=con.getUpdate(nnsb1.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ nnsb1.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		
		//---------------------
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.commit();
		//---------------------
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
	private boolean _InsertButton = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertButton = true;
	}
	private boolean _DeleteButton = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteButton = true;
	}
	
	private boolean _ShenhClick = false;
	
	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	private boolean _ReturnClick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
		if(_InsertButton){
			_InsertButton=false;
			Insert();
			getSelectData();
		}
		if(_DeleteButton){
			_DeleteButton = false;
			Delete();
			getSelectData();
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			getSelectData();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			Return();
			getSelectData();
		}

	}
	public void Shenh(){//	��˰�ť
		//����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefenValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefenValue().getId();
		}
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		String chaxzt="";
		String zhuangt="";
		Visit visit = (Visit) getPage().getVisit();

		// ----------�糧��--------------
		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.shangjgsid = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
		//��ѯ����ʱ��״̬
		if(visit.getRenyjb()==3){
			chaxzt="";
		}else if(visit.getRenyjb()==2){
			chaxzt=" and y.zhuangt=1";
		}else if(visit.getRenyjb()==1){
			chaxzt=" and y.zhuangt=2";
		}
		//�����˸ı��״̬
		if(visit.getRenyjb()==3){
			zhuangt="zhuangt=1"; 
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=2";
		}
		
		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from nianxqjhh y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id \n" + 
		"       and y.nianf="+intyear+chaxzt+str+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");		
		while (rsl.next()) {
	
			sql.append("update nianxqjhh set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
			

//			String tongjkj_id = "select distinct y.id as id from yuezbb y,diancxxb dc" +
//								" where  y.fenx='�ۼ�' and y.diancxxb_id=dc.id "+" and y.riq="+CurrODate;
//			ResultSet rs1 = con.getResultSet(tongjkj_id);
//		
//			StringBuffer sqllj =new StringBuffer();
//			try {
//				while (rs1.next()) {
//					 sqllj.append("begin \n");
//					 sqllj.append("update yuezbb set "+zhuangt+ " where id=" + rs1.getLong("id")+";\n");
//						 sqllj.append("end;");
//				 }
//			} catch (SQLException e) {
//				// TODO �Զ����� catch ��
//				e.printStackTrace();
//			}
//			 con.getUpdate(sqllj.toString());
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
		setMsg("�������ϱ��ϼ���λ�������ٶ����ݽ����޸�!");
	}

	public void Return(){
//		 ����������ݺ��·�������
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
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		String chaxzt="";
		String zhuangt="";
		Visit visit = (Visit) getPage().getVisit();
		// ----------�糧��--------------
		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.shangjgsid = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
		//��ѯ����ʱ��״̬
		if(visit.getRenyjb()==3){
			chaxzt="";
		}else if(visit.getRenyjb()==2){
			chaxzt=" and y.zhuangt=1";
		}else if(visit.getRenyjb()==1){
			chaxzt=" and y.zhuangt=2";
		}
		//�����˸ı��״̬
		if(visit.getRenyjb()==1){
			zhuangt="zhuangt=1";
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=0";
		}

		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from nianxqjhh y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id \n" + 
		"      and y.riq="+CurrODate+chaxzt+str+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("begin \n");		
		while (rsl.next()) {
			result++;
			sql.append("update nianxqjhh set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
			
		}	
		sql.append("end;");
		if(result>0){
			con.getUpdate(sql.toString());
		}
//		con.getUpdate(sql.toString());
		
		con.Close();
	}
	
	public void Delete(){
		JDBCcon con = new JDBCcon();
		//����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		//ɾ�����º��ۼƵ�����
		//long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String DeleteStr="delete nianxqjhh y where y.nianf=" + intyear + " and y.diancxxb_id="+diancxxb_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	
//	������ά����-�洢ȥ��ͽ��������
	private String shuj[][]= new String[2][26];
	
	/**
	 * �õ������ȥ�������
	 * @param (������JDBCcon,��ݣ��糧ID,��)
	 */
	public void getQus(JDBCcon con2,long year,long shujlx,String yuefen,long diancxxb_id,int row){//�õ������ȥ�������(��������ݣ��糧ID)
		String sql_sn="select to_char(n.gongdbzmh) as gongdbzmh,to_char(n.zonghcydl) as zonghcydl," +
		  " to_char(n.fadbhm) as fadbhm,to_char(n.gongrl) as gongrl,to_char(n.gongrbzmh) as gongrbzmh, " +
		  " to_char(n.fadxybml) as fadxybml,to_char(n.gongrxybml) as gongrxybml,to_char(n.xuybml) as xuybml," +
		  " to_char(n.dianhzryl) as dianhzryl,to_char(n.youfrl) as youfrl, " +
		  " to_char(n.zongxql-n.youfrl) xuymzbml,to_char(n.rulrz) as rulrz,to_char(n.fadxyml) as fadxyml," +
		  " to_char(n.gongrxyml) as gongrxyml, " +
		  " to_char(n.xuyyml) as xuyyml,to_char(n.qity) as qity,to_char(n.yuns) as yuns," +
		  " to_char(n.qickc) as qickc,to_char(n.qimkc) as qimkc,to_char(n.zongxql) as zongxql " +
		  " from nianxqjhh n where n.nianf="+(year-1)+" and n.jizzt=0 and n.shujzt='"+(shujlx-1)+"' " +
		" and n.diancxxb_id="+diancxxb_id;

		if(!con2.getHasIt(sql_sn)){//�ж�ȥ���Ƿ������ݣ����û�д��±���ȡ
		sql_sn = 
			"select \n" +// yz.diancxxb_id,yz.fenx,
			"to_char(round_new(yz.fadl,2)) as fadl,\n" + //��ǧ��ʱ
			"to_char(round_new(yz.gongdbzmh,2)) as gongdbzmh,\n" + //��/ǧ��ʱ
			"to_char(round_new(yz.FADZHCGDL,2)) as zonghcydl,\n" + //��/ǧ��ʱ
			"to_char(round_new(yz.FADBZMH,2)) as fadbhm,\n" + //��/ǧ��ʱ
			"to_char(round_new(yz.gongrl,2)) as gongrl,\n" + //����
			"to_char(round_new(yz.gongrbzmh,2)) as gongrbzmh,\n" + //ǧ��/����
			"to_char(round_new((yz.fadl*10000*yz.gongdbzmh)/1000000,2)) as fadxybml,\n" + //���(��ǧ��ʱ*10000*��/ǧ��ʱ)/1000000
			"to_char(round_new((yz.gongrl*yz.gongrbzmh)/1000,2)) as gongrxybml,\n" + //��� (����*ǧ��/����)/1000
			"to_char(round_new(((yz.fadl*10000*yz.gongdbzmh)/1000000+(yz.gongrl*yz.gongrbzmh)/1000),2)) as xuybml,\n" + //���
			"'0' as dianhzryl,\n" + //��
			"to_char(round_new(yz.RULTRYPJFRL,2)) as youfrl,\n" + //MJ/KG
			"to_char(round_new(((decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000) +\n" + 
			"            decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000)+yz.qitfy)-yz.RULTRYPJFRL),2)) as xuymzbml,\n" + //MJ/KG
			"to_char(round_new(yz.RULTRMPJFRL,2)) as rulrz,\n" + //MJ/KG
			"to_char(round_new(decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000),2)) as fadxyml,\n" + //���
			"to_char(round_new(decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000),2)) as gongrxyml,\n" + //���	
			"to_char(round_new(decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000) +\n" + 
			"          decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000),2)) as xuyyml,\n" + //���
			"to_char(round_new(yz.qitfy,2)) as qity,'0' as yuns,'0' as qickc,'0' as qimkc,\n" + //���
			"to_char(round_new((decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000) +\n" + 
			"    decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000)+yz.qitfy),2)) as zongxql\n" + //���
			"from yuezbb yz\n" + 
			" where yz.riq=to_date('" + (year-1) + "-"+yuefen+"-01','yyyy-mm-dd') and yz.fenx='�ۼ�' \n" +
			"   and yz.diancxxb_id="+diancxxb_id+"";
		}
		
		ResultSetList rs_sn=con2.getResultSetList(sql_sn);
		
		if(rs_sn.next()){
		for (int j=0;j<20;j++){
			shuj[row][j]=""+rs_sn.getString(j);
		}
		}
		con2.Close();
	}
	
	public void Insert(){
		JDBCcon con1 = new JDBCcon();
		//����������ݺ��·�������
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		/**
		 * �����ֶΣ�ȥ��(��ȥ���nianxqjhh����ȡ�����û�д�ȥ����±���ȡ)�����꣨�ӽ�����±���ȡ����
		 * 0 fadl_xj ��������¼��
		 * 1 gongdbzmh_xj �����׼ú�ģ�¼��
		 * 2 zonghcydl_xj �ۺϳ��õ��ʣ�¼��
		 * 3 fadbhm_xj �����׼ú�ģ�¼��
		 * 4 gongrl_xj ������¼��
		 * 5 gongrbzmh_xj ���ȱ�׼ú�ģ�¼��
		 * 6 fadxybml_xj �������ñ�ú��--���㣨������*�����׼ú�ģ�
		 * 7 gongrxybml_xj �������ñ�ú��--���㣨������*���ȱ�׼ú�ģ�
		 * 8 xuybml_xj ���ñ�ú��--���㣨�������ñ�ú��+�������ñ�ú����
		 * 9 dianhzryl_xj �����ȼ������¼��
		 * 10 youfrl_xj �ͷ�������¼��
		 * 11 xuymzbml_xj �������۱�ú��--���㣨��������-�ͷ�������
		 * 12 rulrz_xj ��¯��ֵ��¼��
		 * 13 fadxyml_xj ������ԭú��--���㣨�������ñ�ú��*29.271/��¯��ֵ��
		 * 14 gongrxyml_xj ������ԭú��--���㣨�������ñ�ú��*29.271/��¯��ֵ��
		 * 15 xuyyml_xj ����ԭú��--���㣨������ԭú��+������ԭú����
		 * 16 qity_xj �����ã�¼��
		 * 17 yuns_xj ����¼��
		 * 18 qickc_xj �ڳ���棭¼��
		 * 19 qimkc_xj ��δ��棭¼��
		 * 20 zongxql_xj ��������--���㣨����ԭú��+������+����+��δ���-�ڳ���棩
		 * 21 ZHUANGJRL װ������
		 * 22 MIAOS װ����������
		 * 23 TOUCRQ Ͷ������
		 * 24 SHEJMZ ���ú��
		 * 25 BEIZ ��ע
		 */
		
		for (int i=0;i<shuj.length;i++){
			for (int j=0;j<shuj[0].length;j++){
				shuj[i][j]="0";
			}
		}
		
		
		//�ж�״̬
		long zhuangt =0;//����״̬
		String gongsshr="";//��˾�����
		String gongsshsj=DateUtil.FormatDate(new Date());//��˾���ʱ��
		String jitshr="";//���������
		String jitshsj=DateUtil.FormatDate(new Date());//�������ʱ��
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));//�õ�һ���µ�ID
		long diancxxb_id=Long.parseLong(this.getTreeid());//�õ��糧��Ϣ��ID
		
		//ȡ��ȥ�������(��ȥ���nianxqjhh����ȡ�����û�д�ȥ����±���ȡ)
		getQus(con1,intyear,lastyear,"12",diancxxb_id,0);
		//ȡ�ý��������(�ӽ����nianxqjhh����ȡ�����û�дӽ�����±���ȡ)
		String yuefen="12";
		if(getRbvalue()!=null && !getRbvalue().equals("")){
			if (Long.parseLong(getRbvalue())<10){
				yuefen="0"+Long.parseLong(getRbvalue());
			}else{
				yuefen=getRbvalue();
			}
		}
		getQus(con1,nextyear,intyear,yuefen,diancxxb_id,1);
		
		shuj[1][10]="41.816";//�ͷ�����
	
		//***********������������***************//		
		//������������״̬Ϊ1
		String insert_sql1="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL," +
				"GONGRBML,BIAOMH,ZHUANGJRL,MIAOS,TOUCRQ,SHEJMZ,BEIZ,QITY,YUNS,QICKC,QIMKC,ZONGXQL,GONGDBZMH,YOUFRL) values (" +
					id+","+diancxxb_id+","+nextyear+","+shuj[0][0]+","+shuj[0][3]+","+shuj[0][2]+","+
					shuj[0][4]+","+shuj[0][5]+","+shuj[0][12]+","+shuj[0][6]+","+shuj[0][7]+","+
					shuj[0][8]+","+shuj[0][13]+","+shuj[0][14]+","+shuj[0][15]+","+
					shuj[0][9]+","+zhuangt+",0,"+lastyear+",'"+gongsshr+"',to_date('"+gongsshsj+"','yyyy-mm-dd'),'"+
					jitshr+"',to_date('"+jitshsj+"','yyyy-mm-dd'),0,0,0,"+shuj[0][21]+",'"+shuj[0][22]+"','"+
					shuj[0][23]+"','"+shuj[0][24]+"','"+shuj[0][25]+"',"+shuj[0][16]+","+shuj[0][17]+","+
					shuj[0][18]+","+shuj[0][19]+","+shuj[0][20]+","+shuj[0][1]+","+shuj[0][10]+")";
		con1.getInsert(insert_sql1);
		
		//***********�����������***************//		
		//������������״̬Ϊ1
		String insert_sql2="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL," +
				"GONGRBML,BIAOMH,ZHUANGJRL,MIAOS,TOUCRQ,SHEJMZ,BEIZ,QITY,YUNS,QICKC,QIMKC,ZONGXQL,GONGDBZMH,YOUFRL) values (" +
					id+","+diancxxb_id+","+nextyear+","+shuj[1][0]+","+shuj[1][3]+","+shuj[1][2]+","+
					shuj[1][4]+","+shuj[1][5]+","+shuj[1][12]+","+shuj[1][6]+","+shuj[1][7]+","+
					shuj[1][8]+","+shuj[1][13]+","+shuj[1][14]+","+shuj[1][15]+","+
					shuj[1][9]+","+zhuangt+",0,"+intyear+",'"+gongsshr+"',to_date('"+gongsshsj+"','yyyy-mm-dd'),'"+
					jitshr+"',to_date('"+jitshsj+"','yyyy-mm-dd'),0,0,0,"+shuj[1][21]+",'"+shuj[1][22]+"','"+
					shuj[1][23]+"','"+shuj[1][24]+"','"+shuj[1][25]+"',"+shuj[1][16]+","+shuj[1][17]+","+
					shuj[1][18]+","+shuj[1][19]+","+shuj[1][20]+","+shuj[1][1]+","+shuj[1][10]+")";
		con1.getInsert(insert_sql2);
		
		//***********������������***************//	
		//������������״̬Ϊ1
		String insert_sql3="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
		"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
		"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL," +
		"GONGRBML,BIAOMH,ZHUANGJRL,MIAOS,TOUCRQ,SHEJMZ,BEIZ,QITY,YUNS,QICKC,QIMKC,ZONGXQL  ) values (" +
			id+","+diancxxb_id+","+nextyear+",0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,0,"+nextyear+",'"+gongsshr+"',to_date('"+gongsshsj+"','yyyy-mm-dd'),'"+
					jitshr+"',to_date('"+jitshsj+"','yyyy-mm-dd'),0,0,0,0,'0','0','0','0',0,0,0,0,0)";
		con1.getInsert(insert_sql3);
		
		con1.Close();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhuangt="";
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (yx.zhuangt=1 or yx.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and yx.zhuangt=2";
			}
		}		
		//����������ݺ��·�������
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		int treejib = this.getDiancTreeJib();

//		---------------------------------------------------------------------	
		boolean isReture = false ;
		if(visit.getRenyjb()==1){
			if(treejib==1){
				isReture=true;
			}
		}else if(visit.getRenyjb()==2){
			if(treejib==2){
				isReture=true;
			}
		}
		
		String jizzt= "and yx.jizzt=0";

//		if(isReture) {
//			setMsg("�������Ҫ������Ϣʱ����ѡ����Ҫ���˵Ĺ�˾��糧���ƣ�");
//		}
//	----------------------------------------------------------------------	
		
		String sbsql1="select * from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and  nianf="+nextyear+" and jizzt=0 "+zhuangt+" and diancxxb_id="+getTreeid();
		
		if (!con.getHasIt(sbsql1)){//���û���������һ���µ����ݡ�
			Insert();
		}

		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select lanc, \n");//decode(jizlx,null,'�ִ����',jizlx) as jizlx,
		sbsql.append("decode(mingc,null,decode(lanc,1,'������',2,'�����׼ú��',3,'�ۺϳ��õ���',4,'�����׼ú��',5,'������',6,'���ȱ�׼ú��', \n");
		sbsql.append("7,'�������ñ�ú��',8,'�������ñ�ú��',9,'���ñ�ú��',10,'�����ȼ����',11,'�ͷ�����',  \n");
		sbsql.append("12,'�������۱�ú��',13,'��¯��ֵ',14,'������ԭú��',15,'�������ñ�ú��',16,'����ԭú��',17,'������',18,'����',19,'�ڳ����',\n");
		sbsql.append("20,'��δ���',21,'��������',22,'װ������',23,'װ����������',24,'Ͷ������',25,'���ú��',26,'��ע',mingc),mingc) as mingc,zz.danw,\n");
		//sbsql.append("to_char(sum(nvl(lastyear,0))) as lastyear,to_char(sum(nvl(intyear,0))) as intyear,to_char(sum(nvl(nextyear,0))) as nextyear, \n");
		sbsql.append("to_char(max(nvl(lastyear,0))) as lastyear,to_char(max(nvl(intyear,0))) as intyear,to_char(max(nvl(nextyear,0))) as nextyear, \n");
		sbsql.append("decode("+visit.getRenyjb()+",3,decode(zz.zhuangt,0,'δ�ύ','���ύ'),2,decode(zz.zhuangt,1,'δ���','�����'),3,decode(zz.zhuangt,2,'δ���','�����')) as zhuangt \n");
		sbsql.append("from (  \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'��ǧ��ʱ' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 1 as lanc,'������' as mingc,'��ǧ��ʱ' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"  ) z1, \n");
		sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'��/ǧ��ʱ' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 2 as lanc,'�����׼ú��' as mingc,'��/ǧ��ʱ' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'��/ǧ��ʱ' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 3 as lanc,'�ۺϳ��õ���' as mingc,'��/ǧ��ʱ' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'��/ǧ��ʱ' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 4 as lanc,'�����׼ú��' as mingc,'��/ǧ��ʱ' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'����' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 5 as lanc,'������' as mingc,'����' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'ǧ��/����' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 6 as lanc,'���ȱ�׼ú��' as mingc,'ǧ��/����' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 7 as lanc,'�������ñ�ú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 8 as lanc,'�������ñ�ú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 9 as lanc,'���ñ�ú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 10 as lanc,'�����ȼ����' as mingc,'��' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 11 as lanc,'�ͷ�����' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 12 as lanc,'�������۱�ú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 13 as lanc,'��¯��ֵ' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 14 as lanc,'������ԭú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 15 as lanc,'������ԭú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 16 as lanc,'����ԭú��' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
//		�����������ã������ڳ���棬��δ���
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 17 as lanc,'������' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 18 as lanc,'����' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 19 as lanc,'�ڳ����' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 20 as lanc,'��δ���' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 21 as lanc,'��������' as mingc,'���' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 22 as lanc,'װ������' as mingc,'��ǧ��' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		/*sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 22 as lanc,'װ������' as mingc,'��ǧ��' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 22 as lanc,to_char(yx.ZHUANGJRL,'FM99999990.00') as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 22 as lanc,to_char(yx.ZHUANGJRL,'FM99999990.00') as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 22 as lanc,to_char(yx.ZHUANGJRL,'FM99999990.00') as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 23 as lanc,'װ����������' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 23 as lanc,to_char(yx.MIAOS) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 23 as lanc,to_char(yx.MIAOS) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 23 as lanc,to_char(yx.MIAOS) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 24 as lanc,'Ͷ������' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 24 as lanc,to_char(yx.TOUCRQ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 24 as lanc,to_char(yx.TOUCRQ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 24 as lanc,to_char(yx.TOUCRQ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 25 as lanc,'���ú��' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 25 as lanc,to_char(yx.SHEJMZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 25 as lanc,to_char(yx.SHEJMZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 25 as lanc,to_char(yx.SHEJMZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 26 as lanc,'��ע' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 26 as lanc,to_char(yx.BEIZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 26 as lanc,to_char(yx.BEIZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 26 as lanc,to_char(yx.BEIZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");*/
		
		sbsql.append(") zz \n");
		sbsql.append("group by (lanc,mingc,zz.danw,zz.zhuangt) \n");
		sbsql.append("order by lanc asc \n");

		  
	    //System.out.println(sbsql.toString());
	    
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		boolean yincan = false;
		boolean falge = false;
		while (rsl.next()) {
			falge = true;
			if (visit.getRenyjb() == 3) {// �糧�û�
				if (rsl.getString("zhuangt").equals("���ύ")) {
					yincan = true;
				} else {
					yincan = false;
				}
			} else if (visit.getRenyjb() == 2) {// �����û�
				if (rsl.getString("zhuangt").equals("�����")) {
					yincan = true;
				} else {
					yincan = false;
				}
			}
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("nianxqjhh");

		// egu.getColumn("jizlx").setCenterHeader("��������");
		// egu.getColumn("jizlx").setEditor(null);
		egu.getColumn("lanc").setCenterHeader("����");
		egu.getColumn("lanc").setEditor(null);
		egu.getColumn("mingc").setCenterHeader("��Ŀ");
		egu.getColumn("mingc").setEditor(null);
		if (getRbvalue() == null || getRbvalue().equals("")) {
			egu.getColumn("intyear").setCenterHeader(intyear + "�������");
		} else {
			egu.getColumn("intyear").setCenterHeader(
					intyear + "��" + getRbvalue() + "�������");
		}
		egu.getColumn("lastyear").setCenterHeader(lastyear + "���");
		egu.getColumn("nextyear").setCenterHeader(nextyear + "��Ԥ��");
		egu.getColumn("danw").setCenterHeader("��λ");
		egu.getColumn("danw").setEditor(null);
		egu.getColumn("zhuangt").setCenterHeader("״̬");
		//egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("zhuangt").setDefaultValue("δ�ύ");

		// �趨���ɱ༭�е���ɫ
		egu.getColumn("lanc").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		egu.getColumn("mingc").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		egu.getColumn("danw").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		egu.getColumn("zhuangt").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		// //�趨��Ԫ��ĵ�ɫ
		egu.getColumn("lastyear").setRenderer("function(value,metadata,rec,rowIndex){if((rowIndex>5 && rowIndex<9) || rowIndex==11 || (rowIndex>12 && rowIndex<16) || rowIndex==20){metadata.css='tdTextext4';} return value;}");
		egu.getColumn("intyear").setRenderer("function(value,metadata,rec,rowIndex){if((rowIndex>5 && rowIndex<9) || rowIndex==11 || (rowIndex>12 && rowIndex<16) || rowIndex==20){metadata.css='tdTextext4';} return value;}");
		egu.getColumn("nextyear").setRenderer("function(value,metadata,rec,rowIndex){if((rowIndex>5 && rowIndex<9) || rowIndex==11 || (rowIndex>12 && rowIndex<16) || rowIndex==20){metadata.css='tdTextext4';} return value;}");

		egu.setDefaultsortable(false);// �趨ҳ�治�Զ�����

		// �趨�г�ʼ���
		// egu.getColumn("jizlx").setWidth(70);
		// egu.getColumn("jizlx").setAlign("right");
		egu.getColumn("lanc").setWidth(70);
		egu.getColumn("lanc").setAlign("right");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("lastyear").setWidth(80);
		egu.getColumn("intyear").setWidth(120);
		egu.getColumn("nextyear").setWidth(80);
		egu.getColumn("danw").setWidth(70);
		egu.getColumn("zhuangt").setWidth(70);

		// �趨�е�С��λ
		// ((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ
		egu.setWidth(1000);// ����ҳ��Ŀ��,������������ʱ��ʾ������

		// ��˱�־
		
		/*ComboBox cb_ys = new ComboBox();
		egu.getColumn("zhuangt").setEditor(cb_ys);
		cb_ys.setEditable(true);
		// String ysSql = "select id,mingc from yunsfsb order by mingc";

		List list = new ArrayList();

		if (visit.getRenyjb() == 3) {// �糧
			list.add(new IDropDownBean(0, "δ�ύ"));
			list.add(new IDropDownBean(1, "���ύ"));
		} else if (visit.getRenyjb() == 2) {// �ֹ�˾
			list.add(new IDropDownBean(1, "δ���"));
			list.add(new IDropDownBean(2, "�����"));
		} else {// ����
			list.add(new IDropDownBean(2, "δ���"));
			list.add(new IDropDownBean(3, "�����"));
		}

		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(list));
		egu.getColumn("zhuangt").setReturnId(true);*/
		
		// *************************������*****************************************88
		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		

		// �趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���

		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				MainGlobal.getExtMessageBox(
						"'����ˢ��'+Ext.getDom('NIANF').value+'�������,���Ժ�'", true))
				.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		String Strtmpfunction = "var form = new Ext.FormPanel({ \n"
				+ "		labelAlign : 'right', \n"
				+ "		frame : true, \n"
				+ "		labelAlign : 'messageform', \n"
				+ "		labelWidth : 160, \n"
				+ "		items : [{ \n"
				+ "		layout : 'column',\n"
				+ "		items : [{ \n"
				+ "		layout:'form',\n"
				+ "		columnWidth:3,\n"
				+ "			items : [new Ext.form.ComboBox({ \n"
				+ "			fieldLabel:'��ѡ�������ɽ�ֹ���·�', \n"
				+ "          id: 'yuefenname',\n"
				+ "			width:100, \n"
				+ "			selectOnFocus:true, \n"
				+ "			transform:'YUEFEN', \n"
				+ "			lazyRender:true, \n"
				+ "			triggerAction:'all', \n"
				+ "			typeAhead:true, \n"
				+ "			forceSelection:true\n"
				+ "			//editable:false \n"
				+ "			})] \n"
				+ "		}]\n"
				+ "	}] \n"
				+ "}); \n"
				+ "	win = new Ext.Window({ \n"
				+ "		layout : 'fit', \n"
				+ "		width : 300, \n"
				+ "		//height : 110, \n"
				+ "		closeAction : 'hide', \n"
				+ "		plain : true, \n"
				+ "		title : '����', \n"
				+ "		items : [form],  \n"
				+ "		buttons : [{ \n"
				+ "			text : 'ȷ��', \n"
				+ "			handler : function() { \n"
				+ "				win.hide(); \n"
				+ "				document.getElementById('TEXT_RADIO_SELECT_VALUE').value = \n"
				+ "						Ext.getCmp(\"yuefenname\").value; \n"
				+ "				//document.getElementById('RefurbishButton').click(); \n"
				+ "			} \n" + "		}, { \n" + "			text : 'ȡ��', \n"
				+ "			handler : function() { \n" + "				win.hide(); \n"
				+ "			} \n" + "		}] \n" + "	});\n";

		if (visit.getRenyjb() == 3) {
//			 ���ð�ť
			StringBuffer insz = new StringBuffer();
			insz.append("function(){ if(!win){");
			insz.append(Strtmpfunction);
			insz.append("} win.show(this);	\n");
			insz.append("}");
			GridButton gbSz = new GridButton("��������", insz.toString());
			gbSz.setIcon(SysConstant.Btn_Icon_Search);
			gbSz.setId("SEARCH");
//			if (falge) {
//				String str1 = "";
//				str1 = "Ext.getCmp(\"SEARCH\").setDisabled(true);";
//				egu.addOtherScript(str1);
//			} else {
//				String str1 = "";
//				str1 = "Ext.getCmp(\"SEARCH\").setDisabled(false);";
//				egu.addOtherScript(str1);
//			}

			egu.addTbarBtn(gbSz);
			
			if (yincan == false) {
				// ��Ӱ�ť
//				StringBuffer ins = new StringBuffer();
//				ins.append("function (){").append(
//						"document.getElementById('InsertButton').click();}");
//				GridButton gbIns = new GridButton("���", ins.toString());
//				gbIns.setIcon(SysConstant.Btn_Icon_Insert);
//				gbIns.setId("INSERT");
//				if (falge) {
//					String str1 = "";
//					str1 = "Ext.getCmp(\"INSERT\").setDisabled(true);";
//					egu.addOtherScript(str1);
//				} else {
//					String str1 = "";
//					str1 = "Ext.getCmp(\"INSERT\").setDisabled(false);";
//					egu.addOtherScript(str1);
//				}
//
//				egu.addTbarBtn(gbIns);
				
				// ɾ����ť
				if (falge == true) {
					String ss = "Ext.MessageBox.confirm('����', '��ȷ��Ҫɾ���������е�������', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
					StringBuffer del = new StringBuffer();
					del.append("function (){").append("" + ss + "}");
					GridButton gbDel = new GridButton("ɾ��", del.toString());
					gbDel.setIcon(SysConstant.Btn_Icon_Delete);
					gbDel.setId("DEL");
					// if(falge){
					// String str1="";
					// str1="Ext.getCmp(\"DEL\").setDisabled(true);";
					// egu.addOtherScript(str1);
					// }
					egu.addTbarBtn(gbDel);
				}
			}
		}
		// ���水ť
		if (falge == true) {
			if (yincan == false) {
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton",
						MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...",
								200));
				// ��˰�ť
				if (visit.isShifsh() == true) {
					if (visit.getRenyjb() == 2 || visit.getRenyjb() == 3) {
						// GridButton gsh = new GridButton("���","function
						// (){document.getElementById('ShenhButton').click();}");

						String ss = "Ext.MessageBox.confirm('����', '��ȷ��Ҫ�������ύ�ϼ���λ��', function(btn) { if(btn=='yes'){document.getElementById('ShenhButton').click();}})";
						StringBuffer shenh = new StringBuffer();
						shenh.append("function (){").append("" + ss + "}");

						GridButton gbShen = new GridButton("���", shenh
								.toString());
						gbShen.setIcon(SysConstant.Btn_Icon_Search);
						gbShen.setId("SHENH");
						// if(yincan==true){
						// String str1="";
						// str1="Ext.getCmp(\"SHENH\").setDisabled(true);";
						// egu.addOtherScript(str1);
						// }
						egu.addTbarBtn(gbShen);
					}
				}
			}

			// ���˰�ť
			if (visit.isShifsh() == true) {
				// egu.getColumn("zhuangt").setEditor(null);
				if (visit.getRenyjb() == 2 || visit.getRenyjb() == 1) {
					GridButton ght = new GridButton("����",
							"function (){document.getElementById('ReturnButton').click();}");
					ght.setDisabled(isReture);
					ght.setIcon(SysConstant.Btn_Icon_Return);
					ght.setId("RETURN");
					if (falge) {
						String str1 = "";
						str1 = "Ext.getCmp(\"RETURN\").setDisabled(false);";
						egu.addOtherScript(str1);
					}
					egu.addTbarBtn(ght);
				}
			}
		}

		egu.addTbarText("->");

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");

		// �趨ĳһ�в��ܱ༭
		// sb.append("if(e.record.get('JIZLX')=='�ܼ�'){e.cancel=true;}");//
		sb
				.append("if(e.record.get('LANC')=='7' || e.record.get('LANC')=='8' || e.record.get('LANC')=='9' || e.record.get('LANC')=='12' ||e.record.get('LANC')=='14' || e.record.get('LANC')=='15' || e.record.get('LANC')=='16' || e.record.get('LANC')=='21'){e.cancel=true;}");

		sb.append("});");

		sb.append("gridDiv_grid.on('afteredit',function(e){\n");

		// ************************��������**************************//
		sb.append("fadl_xj = gridDiv_ds.getAt(0);\n");// ��������¼��
		sb.append("gongdbzmh_xj = gridDiv_ds.getAt(1);\n");// �����׼ú�ģ�¼��

		sb.append("zonghcydl_xj = gridDiv_ds.getAt(2);\n");// �ۺϳ��õ��ʣ�¼��
		sb.append("fadbhm_xj = gridDiv_ds.getAt(3);\n");// �����׼ú�ģ�¼��

		sb.append("gongrl_xj = gridDiv_ds.getAt(4);\n");// ��������¼��
		sb.append("gongrbzmh_xj = gridDiv_ds.getAt(5);\n");// ���ȱ�׼ú�ģ�¼��

		sb.append("fadxybml_xj = gridDiv_ds.getAt(6);\n");// �������ñ�ú��--���㣨������*�����׼ú�ģ�
		sb.append("gongrxybml_xj = gridDiv_ds.getAt(7);\n");// �������ñ�ú��--���㣨������*���ȱ�׼ú�ģ�
		
		sb.append("xuybml_xj = gridDiv_ds.getAt(8);\n");// ���ñ�ú��--���㣨�������ñ�ú��+�������ñ�ú����
		sb.append("dianhzryl_xj = gridDiv_ds.getAt(9);\n");// �����ȼ������¼��
		sb.append("youfrl_xj = gridDiv_ds.getAt(10);\n");// �ͷ�������¼��

		sb.append("xuymzbml_xj = gridDiv_ds.getAt(11);\n");// �������۱�ú�������㣨��������-�ͷ�������
		sb.append("rulrz_xj = gridDiv_ds.getAt(12);\n");// ��¯��ֵ��¼��
		sb.append("fadxyml_xj = gridDiv_ds.getAt(13);\n");// ������ԭú��--���㣨�������ñ�ú��*29.271/��¯��ֵ��
		sb.append("gongrxyml_xj = gridDiv_ds.getAt(14);\n");// ������ԭú��--���㣨�������ñ�ú��*29.271/��¯��ֵ��
		sb.append("xuyyml_xj = gridDiv_ds.getAt(15);\n");// ����ԭú��--���㣨������ԭú��+������ԭú����
		sb.append("qity_xj = gridDiv_ds.getAt(16);\n");// �����ã�¼��
		sb.append("yuns_xj = gridDiv_ds.getAt(17);\n");// ����¼��
		sb.append("qickc_xj = gridDiv_ds.getAt(18);\n");// �ڳ���棭¼��
		sb.append("qimkc_xj = gridDiv_ds.getAt(19);\n");// ��δ��棭¼��
		sb.append("zongxql_xj = gridDiv_ds.getAt(20);\n");// ��������--���㣨����ԭú��+������+����+��δ���-�ڳ���棩

		//���㷢�����ñ�ú��-�������� ��������*�����׼ú�ģ�/10000
		sb.append("fadxybml_xj.set('LASTYEAR',Round((eval(fadl_xj.get('LASTYEAR')||0))*eval(fadbhm_xj.get('LASTYEAR')||0)/1000000,2));\n");
		sb.append("fadxybml_xj.set('INTYEAR',Round((eval(fadl_xj.get('INTYEAR')||0))*eval(fadbhm_xj.get('INTYEAR')||0)/1000000,2));\n");
		sb.append("fadxybml_xj.set('NEXTYEAR',Round((eval(fadl_xj.get('NEXTYEAR')||0))*eval(fadbhm_xj.get('NEXTYEAR')||0)/1000000,2));\n");

		// ���㹩�����ñ�ú��-�������� ��������*���ȱ�׼ú�ģ�/10000
		sb.append("gongrxybml_xj.set('LASTYEAR',Round(eval(gongrl_xj.get('LASTYEAR')||0)*eval(gongrbzmh_xj.get('LASTYEAR')||0)/10000000,2));\n");
		sb.append("gongrxybml_xj.set('INTYEAR',Round(eval(gongrl_xj.get('INTYEAR')||0)*eval(gongrbzmh_xj.get('INTYEAR')||0)/10000000,2));\n");
		sb.append("gongrxybml_xj.set('NEXTYEAR',Round(eval(gongrl_xj.get('NEXTYEAR')||0)*eval(gongrbzmh_xj.get('NEXTYEAR')||0)/10000000,2));\n");
		// �������ñ�ú��-�������� ���������ñ�ú��+�������ñ�ú����
		sb.append("xuybml_xj.set('LASTYEAR',Round(eval(fadxybml_xj.get('LASTYEAR')||0)+eval(gongrxybml_xj.get('LASTYEAR')||0),2));\n");
		sb.append("xuybml_xj.set('INTYEAR',Round(eval(fadxybml_xj.get('INTYEAR')||0)+eval(gongrxybml_xj.get('INTYEAR')||0),2));\n");
		sb.append("xuybml_xj.set('NEXTYEAR',Round(eval(fadxybml_xj.get('NEXTYEAR')||0)+eval(gongrxybml_xj.get('NEXTYEAR')||0),2));\n");
		// �����������۱�ú�� �����ȼ����*41.816/29.271  �ͷ�����=41.816 --����������-�ͷ�������
		sb.append("xuymzbml_xj.set('LASTYEAR',Round(eval(dianhzryl_xj.get('LASTYEAR')||0)*eval(youfrl_xj.get('LASTYEAR')||0)/29.271,2));\n");
		sb.append("xuymzbml_xj.set('INTYEAR',Round(eval(dianhzryl_xj.get('INTYEAR')||0)*eval(youfrl_xj.get('INTYEAR')||0)/29.271,2));\n");
		sb.append("xuymzbml_xj.set('NEXTYEAR',Round(eval(dianhzryl_xj.get('NEXTYEAR')||0)*eval(youfrl_xj.get('NEXTYEAR')||0)/29.271,2));\n");
		// ���㷢����ԭú��--�������� ���������ñ�ú��*29.271/��¯��ֵ��
		sb.append("if(eval(rulrz_xj.get('LASTYEAR')||0)==0){ \n");
		sb.append("		fadxyml_xj.set('LASTYEAR',0);\n");
		sb.append("}else{ \n");
		sb.append("		fadxyml_xj.set('LASTYEAR',Round((eval(fadxybml_xj.get('LASTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('LASTYEAR')||0)/1000,2));\n");
		sb.append("}\n");
		sb.append("if(eval(rulrz_xj.get('INTYEAR')||0)==0){ \n");
		sb.append("		fadxyml_xj.set('INTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("		fadxyml_xj.set('INTYEAR',Round((eval(fadxybml_xj.get('INTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('INTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		sb.append("if(eval(rulrz_xj.get('NEXTYEAR')||0)==0){ \n");
		sb.append("		fadxyml_xj.set('NEXTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("		fadxyml_xj.set('NEXTYEAR',Round((eval(fadxybml_xj.get('NEXTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('NEXTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		// ���㹩����ԭú��--�������� ���������ñ�ú��*29.271/��¯��ֵ��
		sb.append("if(eval(rulrz_xj.get('LASTYEAR')||0)==0){ \n");
		sb.append("gongrxyml_xj.set('LASTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("gongrxyml_xj.set('LASTYEAR',Round((eval(gongrxybml_xj.get('LASTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('LASTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		sb.append("if(eval(rulrz_xj.get('INTYEAR')||0)==0){ \n");
		sb.append("gongrxyml_xj.set('INTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("gongrxyml_xj.set('INTYEAR',Round((eval(gongrxybml_xj.get('INTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('INTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		sb.append("if(eval(rulrz_xj.get('NEXTYEAR')||0)==0){ \n");
		sb.append("gongrxyml_xj.set('NEXTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("gongrxyml_xj.set('NEXTYEAR',Round((eval(gongrxybml_xj.get('NEXTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('NEXTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		// ��������ԭú��--�������� ��������ԭú��+������ԭú����
		sb.append("var xuyyml1=Round(eval(fadxyml_xj.get('LASTYEAR')||0)+eval(gongrxyml_xj.get('LASTYEAR')||0),2);\n");
		sb.append("if (xuyyml1=='0.00'){\n");
		sb.append(" 	xuyyml1='0';\n");
		sb.append("}\n");
		sb.append("var xuyyml2=Round(eval(fadxyml_xj.get('INTYEAR')||0)+eval(gongrxyml_xj.get('INTYEAR')||0),2);\n");
		sb.append("if (xuyyml2=='0.00'){\n");
		sb.append(" 	xuyyml2='0';\n");
		sb.append("}\n");
		sb.append("var xuyyml3=Round(eval(fadxyml_xj.get('NEXTYEAR')||0)+eval(gongrxyml_xj.get('NEXTYEAR')||0),2);\n");
		sb.append("if (xuyyml3=='0.00'){\n");
		sb.append(" 	xuyyml3='0';\n");
		sb.append("}\n");
		sb.append("xuyyml_xj.set('LASTYEAR',xuyyml1);\n");
		sb.append("xuyyml_xj.set('INTYEAR',xuyyml2);\n");
		sb.append("xuyyml_xj.set('NEXTYEAR',xuyyml3);\n");
		// ������������--�������飨����ԭú��+������+����+��δ���-�ڳ���棩
		sb.append("var zongxql1=Round(eval(xuyyml_xj.get('LASTYEAR')||0)+eval(qity_xj.get('LASTYEAR')||0)+eval(yuns_xj.get('LASTYEAR')||0)+eval(qimkc_xj.get('LASTYEAR')||0)-eval(qickc_xj.get('LASTYEAR')||0),2);\n");
		sb.append("if (zongxql1=='0.00'){\n");
		sb.append(" 	zongxql1='0';\n");
		sb.append("}\n");
		sb.append("var zongxql2=Round(eval(xuyyml_xj.get('INTYEAR')||0)+eval(qity_xj.get('INTYEAR')||0)+eval(yuns_xj.get('INTYEAR')||0)+eval(qimkc_xj.get('INTYEAR')||0)-eval(qickc_xj.get('INTYEAR')||0),2);\n");
		sb.append("if (zongxql2=='0.00'){\n");
		sb.append(" 	zongxql2='0';\n");
		sb.append("}\n");
		sb.append("var zongxql3=Round(eval(xuyyml_xj.get('NEXTYEAR')||0)+eval(qity_xj.get('NEXTYEAR')||0)+eval(yuns_xj.get('NEXTYEAR')||0)+eval(qimkc_xj.get('NEXTYEAR')||0)-eval(qickc_xj.get('NEXTYEAR')||0),2);\n");
		sb.append("if (zongxql3=='0.00'){\n");
		sb.append(" 	zongxql3='0';\n");
		sb.append("}\n");
		sb.append("zongxql_xj.set('LASTYEAR',zongxql1);\n");
		sb.append("zongxql_xj.set('INTYEAR',zongxql2);\n");
		sb.append("zongxql_xj.set('NEXTYEAR',zongxql3);\n");
		
		sb.append("});\n");

		//�趨�ϼ��в�����
		//sb.append("function gridDiv_save(record){if(record.get('JIZLX')=='�ܼ�') return 'continue';}");

		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
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
			this.setYuefenValue(null);
			this.getYuefenModels();
			visit.setShifsh(true);
			setTbmsg(null);
			setRbvalue(null);
			getSelectData();
		}
			
			getSelectData();
		
		
	}
	
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
	public String getNianfen() {
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setNianfen(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}
	
	public String getYuefen() {
		int intYuefen = Integer.parseInt(((Visit) getPage().getVisit()).getString5());
		if (intYuefen<10){
			return "0"+intYuefen;
		}else{
			return ((Visit) getPage().getVisit()).getString5();
		}
	}
	public void setYuefen(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	public void setRiq1() {
		setNianfen(getNianfenValue().getValue());
		setYuefen(getYuefenValue().getValue());
	}	
	private static IPropertySelectionModel _NianfenModel;
    public IPropertySelectionModel getNianfenModel() {
        if (_NianfenModel == null) {
            getNianfenModels();
        }
        return _NianfenModel;
    }
    
	private IDropDownBean _NianfenValue;
	
    public IDropDownBean getNianfenValue() {
        if (_NianfenValue == null) {
            int _nianfen = DateUtil.getYear(new Date());
            int _yuefen = DateUtil.getMonth(new Date());
            if (_yuefen == 1) {
                _nianfen = _nianfen - 1;
            }
            for (int i = 0; i < getNianfenModel().getOptionCount(); i++) {
                Object obj = getNianfenModel().getOption(i);
                if (_nianfen == ((IDropDownBean) obj).getId()) {
                    _NianfenValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfenValue;
    }
	
    public void setNianfenValue(IDropDownBean Value) {
    	if  (_NianfenValue!=Value){
    		_NianfenValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfenModels() {
        List listNianfen = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianfen.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfenModel = new IDropDownModel(listNianfen);
        return _NianfenModel;
    }

    public void setNianfenModel(IPropertySelectionModel _value) {
        _NianfenModel = _value;
    }

	// �·�������
	private static IPropertySelectionModel _YuefenModel;
	
	public IPropertySelectionModel getYuefenModel() {
	    if (_YuefenModel == null) {
	        getYuefenModels();
	    }
	    return _YuefenModel;
	}
	
	private IDropDownBean _YuefenValue;
	
	public IDropDownBean getYuefenValue() {
	    if (_YuefenValue == null) {
	        int _yuefen = DateUtil.getMonth(new Date());
	        if (_yuefen == 1) {
	            _yuefen = 12;
	        } else {
	            _yuefen = _yuefen - 1;
	        }
	        for (int i = 0; i < getYuefenModel().getOptionCount(); i++) {
	            Object obj = getYuefenModel().getOption(i);
	            if (_yuefen == ((IDropDownBean) obj).getId()) {
	                _YuefenValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefenValue;
	}
	
	public void setYuefenValue(IDropDownBean Value) {
    	if  (_YuefenValue!=Value){
    		_YuefenValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefenModels() {
        List listYuefen = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuefen.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefenModel = new IDropDownModel(listYuefen);
        return _YuefenModel;
    }

    public void setYuefenModel(IPropertySelectionModel _value) {
        _YuefenModel = _value;
    }	
//	 ���
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
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
		if (_YuefValue!= null) {
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
	


//	 �õ���½�û����ڵ糧���߷ֹ�˾������
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
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
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
		return getTree().getWindowTreeScript();
	}
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
}