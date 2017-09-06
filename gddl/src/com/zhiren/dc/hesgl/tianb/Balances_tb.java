package com.zhiren.dc.hesgl.tianb;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.dc.hesgl.jiesd.Balances;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import com.zhiren.dc.hesgl.jiesd.Dcbalance;
import com.zhiren.dc.hesgl.jiesd.Dcbalancebean;
import com.zhiren.dc.hesgl.jiesd.Dcbalancezbbean;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Balances_tb extends BasePage {

	private static int _editTableRow = -1;//�༭����ѡ�е���

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private String _msg;

	protected void initialize() {
		super.initialize();
		_msg = "";
		_liucmc="";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	public boolean isHetbhDp() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setHetbhDp(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}
	
	public boolean isLiucmcDp() {
		return ((Visit) getPage().getVisit()).getboolean6();
	}

	public void setLiucmcDp(boolean editable) {
		((Visit) getPage().getVisit()).setboolean6(editable);
	}
	
//	int1 ״̬ �ж����޸ĺ�ͬ�Ż����ύ��������	1���޸ĺ�ͬ��2���ύ��������
	public void setZhuangt(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}	
	
	private static Date _JiesrqsmallValue = new Date();
	private boolean _Jiesrqsmallchange = false;
	public Date getJiesrqsmall() {
		return _JiesrqsmallValue;
	}

	public void setJiesrqsmall(Date _value) {
		if (FormatDate(_JiesrqsmallValue).equals(FormatDate(_value))) {
			_Jiesrqsmallchange = false;
		} else {

			_JiesrqsmallValue = _value;
			_Jiesrqsmallchange = true;
		}

	}
	
	private static Date _JiesrqbigValue = new Date();
	private boolean _Jiesrqbigchange = false;
	public Date getJiesrqbig() {
		return _JiesrqbigValue;
	}

	public void setJiesrqbig(Date _value) {
		if (FormatDate(_JiesrqbigValue).equals(FormatDate(_value))) {
			_Jiesrqbigchange = false;
			
		} else {

			_JiesrqbigValue = _value;
			_Jiesrqbigchange = true;
		}

	}
	// ָ������Model

	public IDropDownBean getZhibbmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIZhibbmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setZhibbmValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean2(value);
	}

	public void setIZhibbmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIZhibbmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIZhibbmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIZhibbmModels() {
		String sql = "select id,bianm from zhibb order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	private String SaveJszbsjb(long Mkid, String mingc, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// ������㵥��ָ������
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));

			sql = " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id) "
					+ " values ("
					+ Id
					+ ", "
					+ Mkid
					+ ", "
					+ getProperId(getIZhibbmModel(), mingc)
					+ ", '"
					+ hetbz
					+ "', "
					+ gongf
					+ ", "
					+ changf
					+ ", "
					+ jies
					+ ", "
					+ yingk
					+ ", "
					+ zhejbz
					+ ", "
					+ zhejje
					+ ","
					+ zhuangt
					+ ","
					+ MainGlobal.getTableId("yansbhb", "bianm",
							((Visit) getPage().getVisit()).getString4())
					+ ");	\n";

		} catch (Exception e) {

			e.printStackTrace();
		} 
		return sql;
	}
	private boolean UpdateFahb_Jiesbid(long JiesbId,JDBCcon con) {
		// ���·�����jiesb_id
		boolean Flag = false;
		String sql = "update fahb set jiesb_id=" + JiesbId
				+ " where id in ("
				+ ((Visit) getPage().getVisit()).getString13() + ")";
		if (con.getUpdate(sql) >= 0) {

			Flag = true;
		} 
		return Flag;
	}
	private  boolean UpdateJiesyfb_Diancjsmkbid(long meikjsb_id,
			JDBCcon con,String TableName_yf){
//		����Ƚ������˷ѣ��ٽ���ú����Ҫ��jiesyfb���й���
		boolean Flag=false;
		Jiesdcz Jscz = new Jiesdcz();
		String where = " diancjsmkb_id";
		
		if(TableName_yf.equals("kuangfjsyfb")){
//			�����˷ѱ�
			where = " kuangfjsmkb_id";
		}
		
		String sql="update "+TableName_yf+" set "+where+"="+meikjsb_id+" where id in("+
				"select distinct dj.yunfjsb_id\n" +
				"       from fahb f,chepb c,danjcpb dj,yunfdjb yd\n" + 
				"       where f.id=c.fahb_id\n" + 
				"             and c.id=dj.chepb_id\n" + 
				"             and dj.yunfdjb_id=yd.id\n" + 
				"             and yd.feiylbb_id="+Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
						.getLong2())+"\n" + 
				"             and f.jiesb_id in ("+meikjsb_id+"))";
		
		if(con.getUpdate(sql)>=0){
			
			Flag=true;
		}
		return Flag;
	}
	private long GetMeikjsb_id(long meikjsb_id) throws SQLException{
//		�����˷�ʱҪ�ͽ������а󶨣�������jiesyfb.diancjsmkb_idΪjiesb.id
		long Jiesb_id=meikjsb_id;
		if(meikjsb_id>0){
//			�������Ʊ���㣬meikjsb_id>0
			
		}else{
			
			JDBCcon con=new JDBCcon();
			String sql="select jiesb_id from fahb where id in ("+((Visit) getPage().getVisit()).getString13()+")";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Jiesb_id=rs.getLong("jiesb_id");
			}
			rs.close();
			con.Close();
		}
		
		return Jiesb_id;
	}
	private boolean checkXitszDjyf() {
		// ���ϵͳ�����е�"�ɵ��������˷�"����
		JDBCcon con = new JDBCcon();
		try {
			String zhi = "";

			String sql = "select zhi from xitxxb where mingc='�ɵ��������˷�'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				zhi = rs.getString("zhi");
			}

			if (zhi.trim().equals("��")) {

				return true;
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return false;
	}

	private long SaveDiancjsmkb(JDBCcon con, String TableName_mk,String TableName_yf) {
		// �洢ú���
		String sql = "";
		long Id = 0;

		try {

			if (((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id() > 0) {
//				���danpcjsmxb��jiesbid����0˵��danpcjsmxb��������ϢҪ��jiesb�������
//					�ʽ�����idӦȡdanpcjsmxb��jiesbid��
				Id = ((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id();
			} else {

				Id = Long
						.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));
			}

			sql = "insert into "+TableName_mk+" (id, diancxxb_id, bianm, gongysb_id, gongysmc, yingd, kuid, \n" +
					"faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, \n" +
					"zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, \n" +
					"meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, RANLBMJBR, RANLBMJBRQ ,\n" +
					"jihkjb_id) "
					+ " values ("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
					+ "',"
					+ MainGlobal
							.getTableId("gongysb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getFahdw())
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFahdw()
					+ "',"
					//+ ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
					//+ ","
					//+ ((Dcbalancebean) getEditValues().get(0)).getYunju()
					//+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYingd()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuid()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getFaz()
					+ "', to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getXianshr()
					+ "', to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
					+ "',"
					+ ((Dcbalancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiessl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJingz()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYuns()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getShulzjbz()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqjk()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiasje()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiakhj()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiakje()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiaksk()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiaksl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBuhsdj()
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong2()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'),null,"
					+ ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
					+ ",0,0,'"
					+ ((Dcbalancebean) getEditValues().get(0)).getBeiz()
					+ "','"
					+ ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd'),"
					+ ((Dcbalancebean) getEditValues().get(0)).getJihkjb_id()
					+ ")";

			if (con.getInsert(sql) >= 0) {

				if (UpdateJiesb(Id,con,TableName_mk)) {

					((Dcbalancebean) getEditValues().get(0)).setId(Id);

					if (UpdateFahb_Jiesbid(Id,con)) {
						// ���·�������jiesb_id
						if(UpdateJiesyfb_Diancjsmkbid(Id,con,TableName_yf)){
//							����Ƚ������˷ѣ��ٽ���ú����Ҫ��jiesyfb���й���
							return Id;
						}
					}
				}
			} 

		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}
	private boolean UpdateDanjcpb_Jiesyfbid(long JiesyfbId, JDBCcon con) {
		// ���ݳ�Ƥ��yunfjsb_id
		// ����ڽ���ѡ��ҳ����ѡ����Ҫ�˶Ի�Ʊ��ʱ˵����yunfdjb��danjcpb���Ѿ��м�¼
		// ����ڽ���ѡ��ҳ����ѡ�񡰲���Ҫ�˶Ի�Ʊ��ʱ˵����yunfdjb��danjcpb�ж�û�м�¼��Ҫ�����¼�¼
		boolean Flag = false;
		boolean Hedbz = false; // true �Ѻ˶ԡ�false δ�˶�
		Jiesdcz Jscz = new Jiesdcz();
		String yunsdw_contion="";
		String strChepb="chepb";
		
		if(((Visit) getPage().getVisit()).getLong9()>-1){
			
			if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//				��װ�˷� 
				yunsdw_contion=" and c.yunsdw='"+((Visit) getPage().getVisit()).getString10()+"'";
			}else{
				
				yunsdw_contion=" and c.yunsdwb_id="+((Visit) getPage().getVisit()).getLong9();
			}
		}
		
		if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//			��װ�˷� 
			
			strChepb="daozcpb";
		}
		
		try {
			StringBuffer sbcpid = new StringBuffer();
			String sql = "select c.id from fahb f,"+strChepb+" c		\n"
					+ " where f.id=c.fahb_id and f.id in ("
					+ ((Visit) getPage().getVisit()).getString13() + ")	\n"
					+ yunsdw_contion;

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				sbcpid.append(rs.getLong("id")).append(",");
			}
			sbcpid.deleteCharAt(sbcpid.length() - 1);

			// �ж�yunfdjb��danjcpb���Ƿ���ڼ�¼���������Hedbz=trueֱ�Ӹ��£����������Hedbz=false;�Ƚ����ݲ�������ٸ���״̬
			sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
					+ " and danjcpb.chepb_id in ("
					+ sbcpid.toString()
					+ ") and yunfdjb.feiylbb_id="
					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
							.getLong2()) + "";

			rs = con.getResultSet(sql);
			if (rs.next()) {

				Hedbz = true;
			}

			if (!Hedbz) {

				String Chep[] = sbcpid.toString().split(",");
				StringBuffer sb = new StringBuffer("begin		\n");
				String Yunfdjb_id = "0";

				for (int i = 0; i < Chep.length; i++) {

					Yunfdjb_id = MainGlobal.getNewID(((Visit) getPage()
							.getVisit()).getLong1());
					sb
							.append(
									"insert into yunfdjb(id, danjbh, feiyb_id, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id, ches)	\n")
							.append(" values		\n")
							.append(
									" ("
											+ Yunfdjb_id
											+ ", '', 0, 0, 0, '"
											+ ((Visit) getPage().getVisit())
													.getRenymc()
											+ "', sysdate, ")
							.append(
									"'����˶Ի�Ʊ�����˷ѽ���', "
											+ Jscz
													.Feiylbb_transition(((Visit) getPage()
															.getVisit())
															.getLong2())
											+ ", 1);	\n");

					sb
							.append(
									"insert into danjcpb(yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl, id)		\n")
							.append(" values		\n").append(
									"("
											+ Yunfdjb_id
											+ ", "
											+ Chep[i]
											+ ", 0, 0, 0, getnewid("
											+ ((Visit) getPage().getVisit())
													.getLong1() + "));	\n");
				}
				sb.append("end;");
				con.getInsert(sb.toString());
			}
			rs.close();

			sql = "update danjcpb set yunfjsb_id="
					+ JiesyfbId
					+ " where chepb_id in ("
					+ sbcpid.toString()
					+ ") 	\n"
					+ " and danjcpb.yunfdjb_id in	\n"
					+ " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
					+ "	and danjcpb.chepb_id in ("
					+ sbcpid.toString()
					+ ") and yunfdjb.feiylbb_id="
					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
							.getLong2()) + ") ";
			if (con.getUpdate(sql) > 0) {

				if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//					��װ�˷� 
					sql = "update daozcpb set jiesb_id="+JiesyfbId+" where id in ("+sbcpid.toString()+")";
					if(con.getUpdate(sql)>0){
						
						Flag = true;
					}
				}else{
					
					Flag = true;
				}
				
			} 

		} catch (Exception e) {

			e.printStackTrace();
		}

		return Flag;
	}
	private long SaveDiancjsyfb(long Meikid,JDBCcon con,String TableName) {
		// �洢�˷ѱ�
		String sql = "";
		long Id = 0;
		try {
			
			String guanlb_id = "diancjsmkb_id";	//����������
			
			if(TableName.equals("kuangfjsyfb")){
				
				guanlb_id = "kuangfjsmkb_id";
			}

			if (((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id() > 0) {

				Id = ((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id();
			} else {

				Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
						.getVisit()).getLong1()));
			}

			sql = " insert into "+TableName+" (id,diancxxb_id, bianm, gongysb_id, gongysmc, meikxxb_id, "
					+ " meikdwmc, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, "
					+ " yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, "
					+ " fukfs, duifdd, ches, gongfsl, yanssl, jiessl, yingk, guohl, yuns, koud, jiesslcy, "
					+ " guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, "
					+ " shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, "
					+ " "+guanlb_id+", RANLBMJBR, RANLBMJBRQ, kuidjfyf,kuidjfzf)"
					+ " values("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
					+ "', "
					+ MainGlobal
							.getTableId("gongysb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getFahdw())
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFahdw()
					+ "',"
					+ MainGlobal
							.getTableId("meikxxb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getMeikdw())
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
					+ "',"
					//+ ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
					//+ ","
					//+ ((Dcbalancebean) getEditValues().get(0)).getYunju()
					//+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYingd()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuid()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getFaz()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getXianshr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'), to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'), '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
					+ "', "
					+ ((Dcbalancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getGongfsl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYanssl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfjsl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYingksl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJingz()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYuns()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getTielyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getTielzf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuangqyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuangqzf()
					+ ","
					+ (double) Math
							.round((((Dcbalancebean) getEditValues().get(0))
									.getTielzf() + ((Dcbalancebean) getEditValues()
									.get(0)).getKuangqzf()) * 100)
					/ 100
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfhsdj()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqyzf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunzfhj()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getBuhsyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfsk()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfsl()
					+ ", 0, "
					+ ((Visit) getPage().getVisit()).getLong2()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'), null,"
					+ ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
					+ ", 0, 0, '"
					+ ((Dcbalancebean) getEditValues().get(0)).getBeiz()
					+ "',"
					+ GetMeikjsb_id(Meikid)
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd')" 
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuidjfzf()
					+ ")";

			if (con.getInsert(sql) >= 0) {

				((Dcbalancebean) getEditValues().get(0)).setYid(Id);
				if (UpdateDanjcpb_Jiesyfbid(Id, con)) {
					
					return Id;
				}
			} 

		} catch (Exception e) {

			e.printStackTrace();
		} 
		return 0;
	}
	private boolean SaveZhib(long Mkid,JDBCcon con) {
		// ������㵥ָ��
		boolean Flag = false;

		StringBuffer sbsql = new StringBuffer(" begin \n");

		sbsql.append(this.SaveJszbsjb(Mkid, Locale.jiessl_zhibb,
				((Dcbalancebean) getEditValues().get(0)).getHetsl(),
				((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
				((Dcbalancebean) getEditValues().get(0)).getYanssl(),
				((Dcbalancebean) getEditValues().get(0)).getJiessl(),
				((Dcbalancebean) getEditValues().get(0)).getYingksl(),
				((Dcbalancebean) getEditValues().get(0)).getShulzjbz(),
				((Dcbalancebean) getEditValues().get(0)).getShulzjje(), 1));
		
		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Shul_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_ht(),
					((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
					((Dcbalancebean) getEditValues().get(0)).getYanssl(),
					((Dcbalancebean) getEditValues().get(0)).getJiessl(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_ht()
				.equals("")) {

			sbsql
					.append(this
							.SaveJszbsjb(
									Mkid,
									Locale.Qnetar_zhibb,
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_ht(),
													"-", 0),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_kf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_cf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_js(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_yk(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_zdj(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Std_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAd_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Ad_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vdaf_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMt_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Mt_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQgrad_ht().equals(
				"")) {

			sbsql
					.append(this
							.SaveJszbsjb(
									Mkid,
									Locale.Qgrad_zhibb,
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_ht(),
													"-", 0),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_kf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_cf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_js(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_yk(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_zdj(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Qbad_zhibb, MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_ht(), "-", 0), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_kf(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_cf(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_js(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Had_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Stad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Mad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aar_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getT2_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.T2_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getYunju_ht().equals(
				"")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Yunju_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_zje(), 1));
		}
		
		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStar_ht().equals(
		"")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Star_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_js(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_zje(), 1));
		}

		sbsql.append(" end; ");

		if (con.getInsert(sbsql.toString()) >= 0) {

			Flag = true;
		} 
		return Flag;
	}

	private boolean UpdateJiesb(long Jiesb_Id,JDBCcon con,String TableName) {

		String sql;
		try {
			sql = "update "+TableName+" set jiesrl="
					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_js()
					+ "," + " jieslf="
					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js()
					+ "," + " jiesrcrl="
					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_cf()
					+ "," + " meikxxb_id="
					+ MainGlobal.getTableId("meikxxb", "quanc", ((Dcbalancebean) getEditValues().get(0)).getMeikdw())
					+ ","+ " meikdwmc='"
					+ ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
					+ "',hetj="
					+ ((Dcbalancebean) getEditValues().get(0)).getHetjg()
					+ ",fengsjj="
					+((Dcbalancebean) getEditValues().get(0)).getFengsjj()
					+",jiajqdj="
					+((Dcbalancebean) getEditValues().get(0)).getJiajqdj()
					+",jijlx="
					+((Dcbalancebean) getEditValues().get(0)).getJijlx()
					+",Yunfhsdj="
					+((Dcbalancebean) getEditValues().get(0)).getYunfjsdj_mk()
					+",hansyf="
					+(((Dcbalancebean) getEditValues().get(0)).getYunzfhj()==0?
							((Dcbalancebean) getEditValues().get(0)).getYunzfhj_mk():
							((Dcbalancebean) getEditValues().get(0)).getYunzfhj())	
					+",Buhsyf="
					+(((Dcbalancebean) getEditValues().get(0)).getBuhsyf()==0?
							((Dcbalancebean) getEditValues().get(0)).getBuhsyf_mk():
							((Dcbalancebean) getEditValues().get(0)).getBuhsyf())	
					+",yunfjsl="
					+((Dcbalancebean) getEditValues().get(0)).getYunfjsl_mk();
			
			if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){
				
				sql+= ",kuidjfyf="
					+((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
					+ ",kuidjfzf="
					+((Dcbalancebean) getEditValues().get(0)).getKuidjfzf();	
			}
			
			if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("CD")){
//				����"����"
				sql+=",chaokdl="+((Dcbalancebean) getEditValues().get(0)).getChaokdl()
					+", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
			}else if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("KD")){
//				����"����"
				sql+=",chaokdl="+(-((Dcbalancebean) getEditValues().get(0)).getChaokdl())
					+", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
			}
			
			sql+=" where id=" + Jiesb_Id;
			if (con.getUpdate(sql) >= 0) {

				return true;
			}
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return false;
	}
	private boolean Save() {
		// ��Ҫ˵�������н����δ��д ��ͬid������״̬id�����̸���id���󷽽���id
		String msg = "";
		long Mkid = 0;// ú��id
		long Yfid = 0;// �˷�id
		boolean Flag = false;
		String table_mk = "jiesb";
		String table_yf = "jiesyfb";
		JDBCcon con =new JDBCcon();
		con.setAutoCommit(false);
		try {
			if (getEditValues() != null
					&& !getEditValues().isEmpty()
					&& !((Dcbalancebean) getEditValues().get(0))
							.getJiesbh().equals("")) {
				
				if (Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())){
					
//					�Ƿֹ�˾�ɹ�����
					table_mk="kuangfjsmkb";
					table_yf="kuangfjsyfb";
				}
				
				if (Jiesdcz.checkbh(table_mk,table_yf,
						((Dcbalancebean) getEditValues().get(0))
								.getJiesbh(), 0, 0)) {

					if (((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id) {// ��Ʊ����
//							 ����ú��˷�
						if (((Dcbalancebean) getEditValues().get(0))
								.getId() == 0) {
							// �糧ú���
							Mkid = SaveDiancjsmkb(con,table_mk,table_yf);

							if (Mkid > 0) {
								// �����糧�����˷ѱ�
								Yfid = SaveDiancjsyfb(Mkid,con,table_yf);

								if (Yfid > 0) {
									// Ҫ�ͻ�����Ϣָ��ģ�鶨һ��zhibb����

									if (SaveZhib(Mkid,con)) {

										Flag = true;
									}
								}
							}
						}

					} else if (((Dcbalancebean) getEditValues().get(0)).getJiasje()!=0) {
						// ������ú��
						if (((Dcbalancebean) getEditValues().get(0))
								.getId() == 0) {
							// ����ú���
							Mkid = SaveDiancjsmkb(con,table_mk,table_yf);

							if (Mkid > 0) {

								if (this.SaveZhib(Mkid,con)) {

									Flag = true;
								}
							}
						}
					} else if(((Dcbalancebean) getEditValues().get(0)).getYunzfhj()!=0){
						// �������˷�
						if (checkXitszDjyf()) {
							// ��ϵͳ��Ϣ������ã�����˾ϵͳ�ܲ��ܵ������˷�
							Yfid = SaveDiancjsyfb(0,con,table_yf);

							if (Yfid > 0) {

								Flag = true;
							}

						} else {

							msg = "��ѡ������˷Ѷ�Ӧ��ú����㵥";
						}
					}
				} else {

					msg = "���㵥����ظ�";
				}
				} else {

					msg = "���ܱ���ս��㵥";
				}

			if (Flag) {

				setMsg("��������ɹ���");
				con.commit();
				Chengbcl.CountCb_js(((Visit) getPage().getVisit()).getString15(),Mkid, Yfid);

			} else {

				setMsg(msg + " �������ʧ�ܣ�");
				con.rollBack();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Flag;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
//			getSelectData();
		}
		if(_QuedChick){
			_QuedChick=false;
			Submit();
//			getSelectData();
		}
		if(_ReturnChick){
			_ReturnChick=false;
			Return(cycle);
		}
	}
	
	private void Return(IRequestCycle cycle) {
//		����
		cycle.activate("Jiesxz");
	}
	
	private void Submit(){
		long Liuc_id=MainGlobal.getProperId(getILiucmcModel(), getLiucmc()); 
		if(Liuc_id>-1){
//			
			if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.liangpjs_feiylbb_id){//��Ʊ����
				
				Liuc.tij("jiesb", ((Dcbalancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				
				Jiesdcz.Zijsdlccl("jiesb",((Dcbalancebean) getEditValues().get(0)).getId(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
				
				Liuc.tij("jiesyfb", ((Dcbalancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				
				Jiesdcz.Zijsdlccl("jiesyfb",((Dcbalancebean) getEditValues().get(0)).getYid(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
//				Jiesdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues().get(0)).getId());
//				Jiesdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues().get(0)).getYid());
			
			}else if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){//ú�����
				
				Liuc.tij("jiesb", ((Dcbalancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				Jiesdcz.Zijsdlccl("jiesb",((Dcbalancebean) getEditValues().get(0)).getId(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
//				Jiesdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues().get(0)).getId());
			}else {//�˷ѽ���
				
				Liuc.tij("jiesyfb", ((Dcbalancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				Jiesdcz.Zijsdlccl("jiesyfb",((Dcbalancebean) getEditValues().get(0)).getYid(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
//				Jiesdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues().get(0)).getYid());
			}
		}
	}
	
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private Dcbalancebean _EditValue;
	public Dcbalancebean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dcbalancebean EditValue) {
		_EditValue = EditValue;
	}
	
	private Dcbalancezbbean _JieszbValue;
	public Dcbalancezbbean getJieszbValue() {
		return _JieszbValue;
	}

	public void setJieszbValue(Dcbalancezbbean JieszbValue) {
		_JieszbValue = JieszbValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getTitle(){
		
		return Locale.jiesd_title;
	}
	
	public String getJiesslcyText(){
		
		return Locale.jiesslcy_title;
	}
	private String getDiancjsbs(long diancxxb_id) {

		JDBCcon con = new JDBCcon();
		String sql = "", diancjsbs = "";

		try {

			sql = "select JIESBDCBS from diancxxb where id=" + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				diancjsbs = rs.getString("JIESBDCBS") + "-";
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return diancjsbs;
	}
	public void getSelectData() {
		Visit visit = new Visit();
		List _editvalues = new ArrayList();
		List _Jieszbvalues = new ArrayList();

		if (getEditValues() != null) {

			getEditValues().clear();
		}
		if (getJieszbValues() != null) {

			getJieszbValues().clear();
		}

		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			String mtianzdw = MainGlobal.getTableCol("diancxxb", "quanc", "id",
					String.valueOf(((Visit) getPage().getVisit()).getLong1()));
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw="";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq = "";
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = mtianzdw;
			String mzhangh = "";
			String mhetsl = "";// ��ͬ����
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = myuanshr;
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			String mMjtokcalxsclfs=""; 	//�׽�ת��С������ʽ
			double myunfhsdj = 0;		//�˷Ѻ�˰����

			String mShulzb_ht="";	//����ָ���ͬ
			double mShulzb_yk=0;	//����ָ��ӯ��
			double mShulzb_zdj=0;	//����ָ���۵���
			double mShulzb_zje=0;	//����ָ���۽��
			
			String mQnetar_ht = ""; // ��ͬ����
			double mQnetar_kf = 0; // ��������
			double mQnetar_cf = 0; // ��������
			double mQnetar_js = 0; // ��������
			double mQnetar_yk = 0; // ����ӯ��
			double mQnetar_zdj = 0; // �۵���
			double mQnetar_zje = 0; // ����

			String mStd_ht = ""; // ��ͬ���
			double mStd_kf = 0; // ��������
			double mStd_cf = 0; // ��������
			double mStd_js = 0; // ��������
			double mStd_yk = 0; // ����ӯ��
			double mStd_zdj = 0; // �۵���
			double mStd_zje = 0; // ����

			String mAd_ht = ""; // ��ͬ���
			double mAd_kf = 0; // ��������
			double mAd_cf = 0; // ��������
			double mAd_js = 0; // ��������
			double mAd_yk = 0; // ����ӯ��
			double mAd_zdj = 0; // �۵���
			double mAd_zje = 0; // ����

			String mVdaf_ht = ""; // ��ͬ���
			double mVdaf_kf = 0; // ��������
			double mVdaf_cf = 0; // ��������
			double mVdaf_js = 0; // ��������
			double mVdaf_yk = 0; // ����ӯ��
			double mVdaf_zdj = 0; // �۵���
			double mVdaf_zje = 0; // ����

			String mMt_ht = ""; // ��ͬ���
			double mMt_kf = 0; // ��������
			double mMt_cf = 0; // ��������
			double mMt_js = 0; // ��������
			double mMt_yk = 0; // ����ӯ��
			double mMt_zdj = 0; // �۵���
			double mMt_zje = 0; // ����

			String mQgrad_ht = ""; // ��ͬ���
			double mQgrad_kf = 0; // ��������
			double mQgrad_cf = 0; // ��������
			double mQgrad_js = 0; // ��������
			double mQgrad_yk = 0; // ����ӯ��
			double mQgrad_zdj = 0; // �۵���
			double mQgrad_zje = 0; // ����

			String mQbad_ht = ""; // ��ͬ���
			double mQbad_kf = 0; // ��������
			double mQbad_cf = 0; // ��������
			double mQbad_js = 0; // ��������
			double mQbad_yk = 0; // ����ӯ��
			double mQbad_zdj = 0; // �۵���
			double mQbad_zje = 0; // ����

			String mHad_ht = ""; // ��ͬ���
			double mHad_kf = 0; // ��������
			double mHad_cf = 0; // ��������
			double mHad_js = 0; // ��������
			double mHad_yk = 0; // ����ӯ��
			double mHad_zdj = 0; // �۵���
			double mHad_zje = 0; // ����

			String mStad_ht = ""; // ��ͬ���
			double mStad_kf = 0; // ��������
			double mStad_cf = 0; // ��������
			double mStad_js = 0; // ��������
			double mStad_yk = 0; // ����ӯ��
			double mStad_zdj = 0; // �۵���
			double mStad_zje = 0; // ����
			
			String mStar_ht = ""; // ��ͬ���
			double mStar_kf = 0; // ��������
			double mStar_cf = 0; // ��������
			double mStar_js = 0; // ��������
			double mStar_yk = 0; // ����ӯ��
			double mStar_zdj = 0; // �۵���
			double mStar_zje = 0; // ����

			String mMad_ht = ""; // ��ͬ���
			double mMad_kf = 0; // ��������
			double mMad_cf = 0; // ��������
			double mMad_js = 0; // ��������
			double mMad_yk = 0; // ����ӯ��
			double mMad_zdj = 0; // �۵���
			double mMad_zje = 0; // ����

			String mAar_ht = ""; // ��ͬ���
			double mAar_kf = 0; // ��������
			double mAar_cf = 0; // ��������
			double mAar_js = 0; // ��������
			double mAar_yk = 0; // ����ӯ��
			double mAar_zdj = 0; // �۵���
			double mAar_zje = 0; // ����

			String mAad_ht = ""; // ��ͬ���
			double mAad_kf = 0; // ��������
			double mAad_cf = 0; // ��������
			double mAad_js = 0; // ��������
			double mAad_yk = 0; // ����ӯ��
			double mAad_zdj = 0; // �۵���
			double mAad_zje = 0; // ����

			String mVad_ht = ""; // ��ͬ���
			double mVad_kf = 0; // ��������
			double mVad_cf = 0; // ��������
			double mVad_js = 0; // ��������
			double mVad_yk = 0; // ����ӯ��
			double mVad_zdj = 0; // �۵���
			double mVad_zje = 0; // ����

			String mT2_ht = ""; // ��ͬ���
			double mT2_kf = 0; // ��������
			double mT2_cf = 0; // ��������
			double mT2_js = 0; // ��������
			double mT2_yk = 0; // ����ӯ��
			double mT2_zdj = 0; // �۵���
			double mT2_zje = 0; // ����

			String mYunju_ht = ""; // ��ͬ�˾�
			double mYunju_kf = 0; // �����˾�
			double mYunju_cf = 0; // �����˾�
			double mYunju_js = 0; // �����˾�
			double mYunju_yk = 0; // ����ӯ��
			double mYunju_zdj = 0; // �۵���
			double mYunju_zje = 0; // ����
			
			double mYunfjsdj_mk = 0;	//�˷ѽ��㵥��(jiesb)
			double mYunzfhj_mk = 0;		//���ӷѺϼƣ�jiesb��
			double mBuhsyf_mk = 0;		//����˰�˷ѣ�jiesb��
			double mYunfjsl_mk = 0;		//�˷ѽ�������(jiesb)	
			
			double mKouk_js=0;
			long mMeikxxb_id=0;
			long mGongysb_id=0;
			long mChongdjsb_id=0;
			double mWeicdje=0;
			int mJieslx_dt=0;

			long mhetb_id = 0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mkoud_js = 0; // ����ʱ�Ŀ۶�
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.17;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf = 0;
			double mkuangqzf = 0;
			double mkuangqsk = 0;
			double mkuangqjk = 0;
			double mjiesslcy = 0;
			double mbukyqyzf = 0;
			double mjiskc = 0;
			double mbuhsyf = 0;
			double myunfsl = 0.07;
			double myunfsk = 0;
			double myunzfhj = 0;
			double mhej = 0;
			String mdaxhj = "";
			String mmeikhjdx = "";
			String myunzfhjdx = "";
			String mbeiz = "";
			String mranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = ((Visit) getPage().getVisit()).getLong2();
			double myuns = 0;
			String myunsfs = "";
			long myunsfsb_id = 0;
			String mdiancjsbs = "";
			mdiancjsbs = getDiancjsbs(((Visit) getPage().getVisit()).getLong1());
			String mstrJieszb = "";
			String mstrHejdxh= "";
			String mErroMessage = "";
			double myingd = 0;
			double mkuid = 0;
			String myunju = ""; // ��������˾�varchar2��

			// ���е����ν���ʱ��Ҫ��ÿһ�����εĽ��������������������danpcjsmxb�У���ʱ�Ͳ�����id
			// ����ʱҪ�ж��������id������о�һ��Ҫ�����id
			long mMeikjsb_id = 0;
			long mYunfjsb_id = 0;
			long mJihkjb_id = 0;
			
			double mfengsjj=0;	//�ֹ�˾�Ӽ�
			double mjiajqdj=0;	//�Ӽ�ǰ����
			int mjijlx=0;		//�������ͣ�0����˰��1������˰��
			
			double mkuidjfyf_je=0;	//���־ܸ��˷ѽ��
			double mkuidjfzf_je=0;	//���־ܸ��ӷѽ��
			double mchaokdl=0;		//��/������
			String mChaodorKuid="";	//��/���ֱ�ʶ��CD,KD,""��

			// mkuangqyf=((Visit) getPage().getVisit()).getDouble2(); //�����˷�
			// mkuangqzf=((Visit) getPage().getVisit()).getDouble3(); //�����ӷ�
			// mkuangqsk=((Visit) getPage().getVisit()).getDouble4(); //����˰��
			// mkuangqjk=((Visit) getPage().getVisit()).getDouble5(); //�����ۿ�

			// ((Visit) getPage().getVisit()).getLong1() //�糧��Ϣ��id
			// ((Visit) getPage().getVisit()).getString1() //��Id
			// ((Visit) getPage().getVisit()).getLong2() //��������
			// ((Visit) getPage().getVisit()).getString2() //�Ƿ���Ҫ����ָ��������ǡ���
			// ((Visit) getPage().getVisit()).getString4() //���ձ��
			// ((Visit) getPage().getVisit()).getLong3() //������λ��id
			// ((Visit) getPage().getVisit()).getLong8() //��ͬ��id
			// ((Visit) getPage().getVisit()).getLong9() //���䵥λid			

			Balances bls = new Balances();
			Balances_variable bsv = new Balances_variable(); // Balances����
			bls.setBsv(bsv);
			
			bls.getBalanceData(((Visit) getPage().getVisit()).getString1(),
					((Visit) getPage().getVisit()).getLong1(),
					((Visit) getPage().getVisit()).getLong2(),
					((Visit) getPage().getVisit()).getLong3(),
					((Visit) getPage().getVisit()).getLong8(),
					((Visit) getPage().getVisit()).getString2(),
					((Visit) getPage().getVisit()).getString4(), 
					Double.parseDouble(((Visit) getPage().getVisit()).getString7()),
					Double.parseDouble(((Visit) getPage().getVisit()).getString18()),
					((Visit) getPage().getVisit()).getLong9(),
					Double.parseDouble(((Visit) getPage().getVisit()).getString12()),
					((Visit) getPage().getVisit()).getString10(),visit,0.00
			);
			
			bsv = bls.getBsv();
//			mErroMessage = bsv.getErroInfo();
			mjiesbh = bsv.getJiesbh();
			
			if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())){
//				����Ƿֹ�˾�ɹ����㣬���¼���ɹ������
				mjiesbh = Jiesdcz.getJiesbh(((Visit) getPage().getVisit()).getString15()+","
						+String.valueOf(((Visit) getPage().getVisit()).getLong1()), "");
			}

//			mErroMessage = bsv.getErroInfo();
			mjiesbh = bsv.getJiesbh();
			mfahdw = bsv.getFahdw();
			mmeikdw = bsv.getMeikdw();
			mfaz = bsv.getFaz();
			mshoukdw = bsv.getShoukdw();
			mfahksrq = bsv.getFahksrq();
			mfahjzrq = bsv.getFahjzrq();
			mfahrq = bsv.getFahrq();
			myansksrq = bsv.getYansksrq();
			myansjzrq = bsv.getYansjsrq();
			mkaihyh = bsv.getKaihyh();
			mpinz = bsv.getRanlpz();
			mzhangh = bsv.getZhangH();
			mhetsl = String.valueOf(bsv.getHetml());
			mgongfsl = bsv.getGongfsl();
			mches = bsv.getChes();
			myansbh = bsv.getYansbh();
			mshulzjbz = bsv.getHansmj();
			myanssl = bsv.getYanssl();
			myingksl = bsv.getYingksl();
			mshulzjje = bsv.getShul_zje();
			mhetb_id = bsv.getHetb_Id();
			mdaibcc = bsv.getDaibcc();
			mkoud_js = bsv.getKoud_js();
			myunsfsb_id = bsv.getYunsfsb_id();
			myunju = bsv.getYunju_jsbz();
//			�ӼۺͲ���˰���۴���
			mfengsjj=bsv.getFengsjj();
			mjiajqdj=bsv.getJiajqdj();
			mjijlx=bsv.getJijlx();
			mMjtokcalxsclfs=bsv.getMj_to_kcal_xsclfs();//�׽�ת��С������ʽ
			myunfhsdj=bsv.getYunfjsdj();	//�˷Ѻ�������

//			ָ��_Begin
//			��������ָ��_Begin
			mShulzb_ht=bsv.getShul_ht();
			mShulzb_yk=bsv.getShul_yk();
			mShulzb_zdj=bsv.getShulzb_zdj();
			mShulzb_zje=bsv.getShulzb_zje();
//			��������ָ��_End
			
			mQnetar_ht = bsv.getQnetar_ht();
			mQnetar_kf = bsv.getQnetar_kf();
			mQnetar_cf = bsv.getQnetar_cf();
			mQnetar_js = bsv.getQnetar_js();
			mQnetar_yk = bsv.getQnetar_yk();
			mQnetar_zdj = bsv.getQnetar_zdj();
			mQnetar_zje = bsv.getQnetar_zje();

			//ww 2010-09-27 ��������MJת���ɿ�
			mQnetar_ht =  MainGlobal.Mjkg_to_kcalkg(mQnetar_ht, "", 0);
			mQnetar_kf = MainGlobal.Mjkg_to_kcalkg(mQnetar_kf, 0);
			mQnetar_cf =  MainGlobal.Mjkg_to_kcalkg(mQnetar_cf, 0);
			mQnetar_js =  MainGlobal.Mjkg_to_kcalkg(mQnetar_js, 0);
			mQnetar_yk = MainGlobal.Mjkg_to_kcalkg(mQnetar_yk, 0);
			mQnetar_zdj =  MainGlobal.Mjkg_to_kcalkg(mQnetar_zdj, 0);
			mQnetar_zje = MainGlobal.Mjkg_to_kcalkg(mQnetar_zje, 0);

			mAd_ht = bsv.getAd_ht();
			mAd_kf = bsv.getAd_kf();
			mAd_cf = bsv.getAd_cf();
			mAd_js = bsv.getAd_js();
			mAd_yk = bsv.getAd_yk();
			mAd_zdj = bsv.getAd_zdj();
			mAd_zje = bsv.getAd_zje();

			mStd_ht = bsv.getStd_ht();
			mStd_kf = bsv.getStd_kf();
			mStd_cf = bsv.getStd_cf();
			mStd_js = bsv.getStd_js();
			mStd_yk = bsv.getStd_yk();
			mStd_zdj = bsv.getStd_zdj();
			mStd_zje = bsv.getStd_zje();

			mVdaf_ht = bsv.getVdaf_ht();
			mVdaf_kf = bsv.getVdaf_kf();
			mVdaf_cf = bsv.getVdaf_cf();
			mVdaf_js = bsv.getVdaf_js();
			mVdaf_yk = bsv.getVdaf_yk();
			mVdaf_zdj = bsv.getVdaf_zdj();
			mVdaf_zje = bsv.getVdaf_zje();

			mMt_ht = bsv.getMt_ht();
			mMt_kf = bsv.getMt_kf();
			mMt_cf = bsv.getMt_cf();
			mMt_js = bsv.getMt_js();
			mMt_yk = bsv.getMt_yk();
			mMt_zdj = bsv.getMt_zdj();
			mMt_zje = bsv.getMt_zje();

			mQgrad_ht = bsv.getQgrad_ht();
			mQgrad_kf = bsv.getQgrad_kf();
			mQgrad_cf = bsv.getQgrad_cf();
			mQgrad_js = bsv.getQgrad_js();
			mQgrad_yk = bsv.getQgrad_yk();
			mQgrad_zdj = bsv.getQgrad_zdj();
			mQgrad_zje = bsv.getQgrad_zje();

			mQbad_ht = bsv.getQbad_ht();
			mQbad_kf = bsv.getQbad_kf();
			mQbad_cf = bsv.getQbad_cf();
			mQbad_js = bsv.getQbad_js();
			mQbad_yk = bsv.getQbad_yk();
			mQbad_zdj = bsv.getQbad_zdj();
			mQbad_zje = bsv.getQbad_zje();

			mHad_ht = bsv.getHad_ht();
			mHad_kf = bsv.getHad_kf();
			mHad_cf = bsv.getHad_cf();
			mHad_js = bsv.getHad_js();
			mHad_yk = bsv.getHad_yk();
			mHad_zdj = bsv.getHad_zdj();
			mHad_zje = bsv.getHad_zje();

			mStad_ht = bsv.getStad_ht();
			mStad_kf = bsv.getStad_kf();
			mStad_cf = bsv.getStad_cf();
			mStad_js = bsv.getStad_js();
			mStad_yk = bsv.getStad_yk();
			mStad_zdj = bsv.getStad_zdj();
			mStad_zje = bsv.getStad_zje();
			
			mStar_ht = bsv.getStar_ht();
			mStar_kf = bsv.getStar_kf();
			mStar_cf = bsv.getStar_cf();
			mStar_js = bsv.getStar_js();
			mStar_yk = bsv.getStar_yk();
			mStar_zdj = bsv.getStar_zdj();
			mStar_zje = bsv.getStar_zje();

			mMad_ht = bsv.getMad_ht();
			mMad_kf = bsv.getMad_kf();
			mMad_cf = bsv.getMad_cf();
			mMad_js = bsv.getMad_js();
			mMad_yk = bsv.getMad_yk();
			mMad_zdj = bsv.getMad_zdj();
			mMad_zje = bsv.getMad_zje();

			mAar_ht = bsv.getAar_ht();
			mAar_kf = bsv.getAar_kf();
			mAar_cf = bsv.getAar_cf();
			mAar_js = bsv.getAar_js();
			mAar_yk = bsv.getAar_yk();
			mAar_zdj = bsv.getAar_zdj();
			mAar_zje = bsv.getAar_zje();

			mAad_ht = bsv.getAad_ht();
			mAad_kf = bsv.getAad_kf();
			mAad_cf = bsv.getAad_cf();
			mAad_js = bsv.getAad_js();
			mAad_yk = bsv.getAad_yk();
			mAad_zdj = bsv.getAad_zdj();
			mAad_zje = bsv.getAad_zje();

			mVad_ht = bsv.getVad_ht();
			mVad_kf = bsv.getVad_kf();
			mVad_cf = bsv.getVad_cf();
			mVad_js = bsv.getVad_js();
			mVad_yk = bsv.getVad_yk();
			mVad_zdj = bsv.getVad_zdj();
			mVad_zje = bsv.getVad_zje();

			mT2_ht = bsv.getT2_ht();
			mT2_kf = bsv.getT2_kf();
			mT2_cf = bsv.getT2_cf();
			mT2_js = bsv.getT2_js();
			mT2_yk = bsv.getT2_yk();
			mT2_zdj = bsv.getT2_zdj();
			mT2_zje = bsv.getT2_zje();

			mYunju_ht = bsv.getYunju_ht(); // ��ͬ�˾�
			mYunju_kf = bsv.getYunju_kf(); // �����˾�
			mYunju_cf = bsv.getYunju_cf(); // �����˾�
			mYunju_js = bsv.getYunju_js(); // �����˾�
			mYunju_yk = bsv.getYunju_yk(); // �˾�ӯ��
			mYunju_zdj = bsv.getYunju_zdj(); // �˾��۵���
			mYunju_zje = bsv.getYunju_zje(); // �۽��

			// ָ��_End
			
//			��ú����е��˷Ѳ���
			
			mYunfjsdj_mk = bsv.getYunfjsdj_mk();
			mYunzfhj_mk = bsv.getYunzfhj_mk();
			mBuhsyf_mk = bsv.getBuhsyf_mk();
			mYunfjsl_mk = bsv.getYunfjsl_mk();
//			��ú����е��˷Ѳ���_end

			mjiessl = bsv.getJiessl();
			myunfjsl = bsv.getYunfjsl();
			mbuhsdj = bsv.getBuhsmj();
			mjiakje = bsv.getJine();
			mjiakhj = bsv.getJiakhj();
			mjiaksl = bsv.getMeiksl();
			mjiaksk = bsv.getJiaksk();
			mjiasje = bsv.getJiashj();
			mtielyf = bsv.getTielyf();
			mtielzf = bsv.getTielzf();
//			�����˷ѡ��ӷ�
			mkuangqyf=bsv.getKuangqyf();
			mkuangqzf=bsv.getKuangqzf();
			
			mjiesslcy = bsv.getJiesslcy();
			mbuhsyf = bsv.getBuhsyf();
			myunfsl = bsv.getYunfsl();
			myunfsk = bsv.getYunfsk();
			myunzfhj = bsv.getYunzfhj();
			mhej = bsv.getHej();
			Money mn = new Money();
			mdaxhj = mn.NumToRMBStr(mhej);
			mmeikhjdx = mn.NumToRMBStr(bsv.getJiashj());
			myunzfhjdx = mn.NumToRMBStr(bsv.getYunzfhj());
//			mbeiz = bsv.getBeiz();
			mbeiz = "";
			mjingz = bsv.getJingz();
			mhetjg = bsv.getHetmdj();
			myuns = bsv.getYuns();
			myunsfs = bsv.getYunsfs();
			myingd = bsv.getYingd();
			mkuid = bsv.getKuid();
			mJihkjb_id = bsv.getJihkjb_id();
			mMeikjsb_id = bsv.getMeikjsb_id();
			mYunfjsb_id = bsv.getYunfjsb_id();
			
			mkuidjfyf_je = bsv.getKuidjfyf_je();
			mkuidjfzf_je = bsv.getKuidjfzf_je();
			mchaokdl = Math.abs(bsv.getChaokdl());	//��/����������������ʾ
			mChaodorKuid = bsv.getChaodOrKuid();	//��/���ֱ�ʶ
			
			
			 mKouk_js=bsv.getKouk_js();
		     mMeikxxb_id=bsv.getMeikxxb_Id();
			 mGongysb_id=bsv.getGongysb_Id();
			 mChongdjsb_id=0;
			 mWeicdje=0;
			 mJieslx_dt=0;
			
			((Visit) getPage().getVisit()).setLong4(bsv.getMeikxxb_Id()); // ú����Ϣ��id
			((Visit) getPage().getVisit()).setLong5(bsv.getFaz_Id()); // ��վid
			((Visit) getPage().getVisit()).setLong6(bsv.getDaoz_Id()); // ��վid
			((Visit) getPage().getVisit()).setDouble1(bsv.getGongfsl()); // ��������

			Jiesdcz Jsdcz = new Jiesdcz();
			
			if (!mShulzb_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb(
						"����(��)", "Shulzb_ht",
						"Shulzb_kf", "Shulzb_cf", "Shulzb_js", "Shulzb_yk",
						"Shulzb_zdj", "Shulzb_zje", mShulzb_ht, mgongfsl, myanssl, 
						mjiessl, mShulzb_yk, mShulzb_zdj, mShulzb_zje);
			}

			if (!mQnetar_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Qnetar"+"(kcal/kg)", "Qnetar_ht",
						"Qnetar_kf", "Qnetar_cf", "Qnetar_js", "Qnetar_yk",
						"Qnetar_zdj", "Qnetar_zje", MainGlobal.Mjkg_to_kcalkg(
								mQnetar_ht, "-", 0), MainGlobal.Mjkg_to_kcalkg(
								mQnetar_kf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQnetar_cf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQnetar_js, 0,mMjtokcalxsclfs), mQnetar_yk, mQnetar_zdj,
						mQnetar_zje);
			}

			if (!mAd_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Ad(%)", "Ad_ht", "Ad_kf",
						"Ad_cf", "Ad_js", "Ad_yk", "Ad_zdj", "Ad_zje", mAd_ht,
						mAd_kf, mAd_cf, mAd_js, mAd_yk, mAd_zdj, mAd_zje);
			}

			if (!mStd_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Std(%)", "Std_ht", "Std_kf",
						"Std_cf", "Std_js", "Std_yk", "Std_zdj", "Std_zje",
						mStd_ht, mStd_kf, mStd_cf, mStd_js, mStd_yk, mStd_zdj,
						mStd_zje);
			}

			if (!mVdaf_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Vdaf(%)", "Vdaf_ht", "Vdaf_kf",
						"Vdaf_cf", "Vdaf_js", "Vdaf_yk", "Vdaf_zdj",
						"Vdaf_zje", mVdaf_ht, mVdaf_kf, mVdaf_cf, mVdaf_js,
						mVdaf_yk, mVdaf_zdj, mVdaf_zje);
			}

			if (!mMt_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Mt(%)", "Mt_ht", "Mt_kf",
						"Mt_cf", "Mt_js", "Mt_yk", "Mt_zdj", "Mt_zje", mMt_ht,
						mMt_kf, mMt_cf, mMt_js, mMt_yk, mMt_zdj, mMt_zje);
			}

			if (!mQgrad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Qgrad(kcal/kg)", "Qgrad_ht",
						"Qgrad_kf", "Qgrad_cf", "Qgrad_js", "Qgrad_yk",
						"Qgrad_zdj", "Qgrad_zje", MainGlobal.Mjkg_to_kcalkg(
								mQgrad_ht, "-", 0), MainGlobal.Mjkg_to_kcalkg(
								mQgrad_kf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQgrad_cf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQgrad_js, 0,mMjtokcalxsclfs), mQgrad_yk, mQgrad_zdj,
						mQgrad_zje);
			}

			if (!mQbad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Qbad(kcal/kg)", "Qbad_ht",
						"Qbad_kf", "Qbad_cf", "Qbad_js", "Qbad_yk", "Qbad_zdj",
						"Qbad_zje",
						MainGlobal.Mjkg_to_kcalkg(mQbad_ht, "-", 0), MainGlobal
								.Mjkg_to_kcalkg(mQbad_kf, 0,mMjtokcalxsclfs), MainGlobal
								.Mjkg_to_kcalkg(mQbad_cf, 0,mMjtokcalxsclfs), MainGlobal
								.Mjkg_to_kcalkg(mQbad_js, 0,mMjtokcalxsclfs), mQbad_yk,
						mQbad_zdj, mQbad_zje);
			}

			if (!mHad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Had(%)", "Had_ht", "Had_kf",
						"Had_cf", "Had_js", "Had_yk", "Had_zdj", "Had_zje",
						mHad_ht, mHad_kf, mHad_cf, mHad_js, mHad_yk, mHad_zdj,
						mHad_zje);
			}

			if (!mStad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Stad(%)", "Stad_ht", "Stad_kf",
						"Stad_cf", "Stad_js", "Stad_yk", "Stad_zdj",
						"Stad_zje", mStad_ht, mStad_kf, mStad_cf, mStad_js,
						mStad_yk, mStad_zdj, mStad_zje);
			}
			
			if (!mStar_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Star(%)", "Star_ht", "Star_kf",
						"Star_cf", "Star_js", "Star_yk", "Star_zdj",
						"Star_zje", mStar_ht, mStar_kf, mStar_cf, mStar_js,
						mStar_yk, mStar_zdj, mStar_zje);
			}

			if (!mMad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Mad(%)", "Mad_ht", "Mad_kf",
						"Mad_cf", "Mad_js", "Mad_yk", "Mad_zdj", "Mad_zje",
						mMad_ht, mMad_kf, mMad_cf, mMad_js, mMad_yk, mMad_zdj,
						mMad_zje);
			}

			if (!mAar_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Aar(%)", "Aar_ht", "Aar_kf",
						"Aar_cf", "Aar_js", "Aar_yk", "Aar_zdj", "Aar_zje",
						mAar_ht, mAar_kf, mAar_cf, mAar_js, mAar_yk, mAar_zdj,
						mAar_zje);
			}

			if (!mAad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Aad(%)", "Aad_ht", "Aad_kf",
						"Aad_cf", "Aad_js", "Aad_yk", "Aad_zdj", "Aad_zje",
						mAad_ht, mAad_kf, mAad_cf, mAad_js, mAad_yk, mAad_zdj,
						mAad_zje);
			}

			if (!mVad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Vad(%)", "Vad_ht", "Vad_kf",
						"Vad_cf", "Vad_js", "Vad_yk", "Vad_zdj", "Vad_zje",
						mVad_ht, mVad_kf, mVad_cf, mVad_js, mVad_yk, mVad_zdj,
						mVad_zje);
			}

			if (!mT2_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("T2(%)", "T2_ht", "T2_kf",
						"T2_cf", "T2_js", "T2_yk", "T2_zdj", "T2_zje", mT2_ht,
						mT2_kf, mT2_cf, mT2_js, mT2_yk, mT2_zdj, mT2_zje);
			}

			if (!mYunju_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("�˾�(Km)", "Yunju_ht", "Yunju_kf",
						"Yunju_cf", "Yunju_js", "Yunju_yk", "Yunju_zdj",
						"Yunju_zje", mYunju_ht, mYunju_kf, mYunju_cf,
						mYunju_js, mYunju_yk, mYunju_zdj, mYunju_zje);
			}

			setJieszb(mstrJieszb);
			
//			���ó�/���ֵ���ʾ	
			mstrHejdxh=Jsdcz.SetHejdxh(bsv.getChaodOrKuid(),mchaokdl,bsv.getHej(),bsv.getHejdx());
			
			this.setHejdxh(mstrHejdxh);
			

			if (!mErroMessage.equals("")) {

				this.setMsg(mErroMessage);
			} 

			_editvalues.add(new Dcbalancebean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw, mfaz, myunsfsb_id, mshoukdw, mfahksrq, mfahjzrq,
					myansksrq, myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh,
					mhetsl, mgongfsl, mches, mxianshr, mfapbh, mdaibcc,
					myansbh, mduifdd, mfukfs, mshulzjbz, myanssl, myingksl,
					myingd, mkuid, mshulzjje, mjiessl, mjiesslcy, myunfjsl,
					mbuhsdj, mjiakje, mbukyqjk, mjiakhj, mjiaksl, mjiaksk,
					mjiasje, mtielyf, mtielzf, mkuangqyf, mkuangqzf, mkuangqsk,
					mkuangqjk, mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq, mfahrq,
					mchangcwjbr, mchangcwjbrq, mruzrq, mjieszxjbr, mjieszxjbrq,
					mgongsrlbjbr, mgongsrlbjbrq, mhetjg, mjieslx, myuns,
					mkoud_js, myunsfs, mdiancjsbs, mhetb_id, myunju,
					mMeikjsb_id, mYunfjsb_id, mJihkjb_id, mfengsjj, mjiajqdj, 
					mjijlx,mMjtokcalxsclfs,mkuidjfyf_je,mkuidjfzf_je,mchaokdl,
					mChaodorKuid,myunfhsdj,mYunfjsdj_mk,mYunzfhj_mk,mBuhsyf_mk,
					mYunfjsl_mk,mKouk_js,mMeikxxb_id,mGongysb_id,mChongdjsb_id,mWeicdje,mJieslx_dt));
			_Jieszbvalues.add(new Dcbalancezbbean(mShulzb_ht,mQnetar_ht, mStd_ht, mAd_ht,
					mVdaf_ht, mMt_ht, mQgrad_ht, mQbad_ht, mHad_ht, mStad_ht,
					mMad_ht, mAar_ht, mAad_ht, mVad_ht, mT2_ht, mYunju_ht,mStar_ht,
					mQnetar_kf, mStd_kf, mAd_kf, mVdaf_kf, mMt_kf, mQgrad_kf,
					mQbad_kf, mHad_kf, mStad_kf, mMad_kf, mAar_kf, mAad_kf,
					mVad_kf, mT2_kf, mYunju_kf, mStar_kf,mQnetar_cf, mStd_cf, mAd_cf,
					mVdaf_cf, mMt_cf, mQgrad_cf, mQbad_cf, mHad_cf, mStad_cf,
					mMad_cf, mAar_cf, mAad_cf, mVad_cf, mT2_cf, mYunju_cf,mStar_cf,
					mQnetar_js, mStd_js, mAd_js, mVdaf_js, mMt_js, mQgrad_js,
					mQbad_js, mHad_js, mStad_js, mMad_js, mAar_js, mAad_js,
					mVad_js, mT2_js, mYunju_js, mStar_js, mShulzb_yk, mQnetar_yk, mStd_yk, mAd_yk,
					mVdaf_yk, mMt_yk, mQgrad_yk, mQbad_yk, mHad_yk, mStad_yk,
					mMad_yk, mAar_yk, mAad_yk, mVad_yk, mT2_yk, mYunju_yk,mStar_yk,
					mShulzb_zdj,mQnetar_zdj, mStd_zdj, mAd_zdj, mVdaf_zdj, mMt_zdj,
					mQgrad_zdj, mQbad_zdj, mHad_zdj, mStad_zdj, mMad_zdj,
					mAar_zdj, mAad_zdj, mVad_zdj, mT2_zdj, mYunju_zdj,mStar_zdj,
					mShulzb_zje, mQnetar_zje, mStd_zje, mAd_zje, mVdaf_zje, mMt_zje,
					mQgrad_zje, mQbad_zje, mHad_zje, mStad_zje, mMad_zje,
					mAar_zje, mAad_zje, mVad_zje, mT2_zje, mYunju_zje,
					mStar_zje));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Dcbalancebean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		this.setJieszbValues(_Jieszbvalues);
	}
	public List getJieszbValues() {
		return ((Visit) getPage().getVisit()).getList2();
	}

	public void setJieszbValues(List editList) {
		((Visit) getPage().getVisit()).setList2(editList);
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
//		����
		ToolbarButton returnbt=new ToolbarButton(null,"����","function(){ document.Form0.ReturnButton.click(); }");
		returnbt.setId("returnbt");
		tb1.addItem(returnbt);
		tb1.addText(new ToolbarText("-"));
//		����
		ToolbarButton savebt=new ToolbarButton(null,"����","function(){ document.Form0.SaveButton.click(); }");
		savebt.setId("savebt");
		tb1.addItem(savebt);
		tb1.addText(new ToolbarText("-"));
//		�ύ��������
		ToolbarButton submitbt=new ToolbarButton(null,"�ύ��������","function(){  \n"
				+ " if(!win){	\n" 
				+ "	\tvar form = new Ext.form.FormPanel({	\n" 
				+ " \tbaseCls: 'x-plain',	\n" 		
				+ " \tlabelAlign:'right',	\n" 
				+ " \tdefaultType: 'textfield',	\n"
				+ " \titems: [{		\n"
				+ " \txtype:'fieldset',	\n"
				+ " \ttitle:'��ѡ����������',	\n"
				+ " \tautoHeight:false,	\n"
				+ " \theight:220,	\n"
				+ " \titems:[	\n"
        		+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
				+ " \twidth:150,	\n"
				+ " \tid:'lcmccb',	\n"
				+ " \tselectOnFocus:true,	\n"
				+ "	\ttransform:'LiucmcDropDown',	\n"						
				+ " \tlazyRender:true,	\n"	
				+ " \tfieldLabel:'��������',		\n" 
				+ " \ttriggerAction:'all',	\n"
				+ " \ttypeAhead:true,	\n"	
				+ " \tforceSelection:true,	\n"
				+ " \teditable:false	\n"					
				+ " \t})	\n"
				+ " \t]		\n"
				+ " \t}]	\n"		
				+ " \t});	\n"
				+ " \twin = new Ext.Window({	\n"
				+ " \tel:'hello-win',	\n"
				+ " \tlayout:'fit',	\n"
				+ " \twidth:500,	\n"	
				+ " \theight:300,	\n"
				+ " \tcloseAction:'hide',	\n"
				+ " \tplain: true,	\n"
				+ " \ttitle:'����',	\n"
				+ " \titems: [form],	\n"
				+ " \tbuttons: [{	\n"
				+ " \ttext:'ȷ��',	\n"
				+ " \thandler:function(){	\n"  
				+ " \twin.hide();	\n"
				+ " \tif(lcmccb.getRawValue()=='��ѡ��'){		\n" 
				+ "	\t	alert('��ѡ���������ƣ�');		\n"
				+ " \t}else{" 
				+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
				+ " \t\t document.all.item('QuedButton').click();	\n"
				+ " \t}	\n"
				+ " \t}	\n"   
				+ " \t},{	\n"
				+ " \ttext: 'ȡ��',	\n"
				+ " \thandler: function(){	\n"
				+ " \twin.hide();	\n"
				+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"	
				+ " \t}		\n"
				+ " \t}]	\n"
				+ " \t});}	\n" 
				+ " \twin.show(this);	\n"

				+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
				+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
				+ " \t}	\n"	
				+ " \t}");
		submitbt.setId("submitbt");
		tb1.addItem(submitbt);
		
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//�ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setList2(null);
			visit.setInt1(0);
			_JiesrqsmallValue = new Date();
			_JiesrqbigValue = new Date();
			visit.setboolean2(false);//�ֹ�˾
			visit.setboolean3(false);//�糧
//			visit.setString2("");	 //����ָ��ҳ����ʾ
			
//			 ((Visit) getPage().getVisit()).getLong1() //�糧��Ϣ��id
			// ((Visit) getPage().getVisit()).getString1() //fahb_Id
			// ((Visit) getPage().getVisit()).getLong2() //��������
			// ((Visit) getPage().getVisit()).getString2() //�Ƿ���Ҫ����ָ��������ǡ���
			// ((Visit) getPage().getVisit()).getString4() //���ձ��
			// ((Visit) getPage().getVisit()).getLong3() //������λ��id
			// visit.setLong4(0); //ú����Ϣ��id
			// visit.setLong5(0); //��վ
			// visit.setLong6(0); //��վ
			// visit.setLong7(0); //�����˷ѱ�id
			// visit.setLong8(0); //��ͬ��id
			// visit.setLong9(0); //���䵥λid
			// visit.setDouble1(0); //��Ʊ��
//			visit.setString12("");	//�ϴν�����
//			visit.setString13(string); //����id
//			visit.setString14("");	//gongysbmlt_id(��Ӧ�̱�)
//			visit.setString10("");	//���䵥λ
			
//			visit.setString15("");	//���㵥λ����������ҳ�����õĽ��㵥λid����
//			�ò���Ҫ������һ��ҳ�棬����ʱ���ж���
			
			
//			((Visit) getPage().getVisit()).setString11("");	//����Kuangqzf����ʱ����ת����ȷ�Ľ���(DCBalance,Jiesdxg)
			
			((Visit) getPage().getVisit()).setString8(""); // ����ָ��ҳ����ʾֵ
			((Visit) getPage().getVisit()).setString9("");	//�ϼƴ�д����ʾ���ݣ�Ϊʵ�ֶ�̬���á����۶֡���ʾ�ã�
//			((Visit) getPage().getVisit()).setString13("229206629");//fahb_id
//			((Visit) getPage().getVisit()).setLong9(229198316);//yunsdwb_id
//			((Visit) getPage().getVisit()).setLong1(visit.getDiancxxb_id());
			if(visit.getRenyjb()<3){
				
				visit.setboolean3(true);
			}	

			setLiucmcValue(null);
			setILiucmcModel(null);	//8
			setTianzdwValue(null);	//14
			setTianzdwModel(null);	
			setDiancmcModel(null);	//1
			setDiancmcValue(null);
			setZhibbmValue(null);	//2
			setIZhibbmModel(null);
			setShoukdwValue(null);	//9
			setIShoukdwModel(null);
			setFahdwModel(null);	//10
			setFahdwValue(null);	
			
			
			getILiucmcModels();
			getTianzdwModels();
			getDiancmcModels();
			getIZhibbmModels();
			
			getIShoukdwModels();	//9
			getFahdwModels();		//10
			visit.setboolean1(false);//����
			visit.setboolean5(false);//��ͬ�����ʾ
			visit.setboolean6(false);//����������ʾ
			
			getSelectData();
		}
		getToolbars();
		
		if(visit.getboolean1()){
			visit.setboolean1(false);
		}
	}
	public String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}

	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString8(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString8();
	}
	
	public void setHejdxh(String value) {

		((Visit) getPage().getVisit()).setString9(value);
	}

	public String getHejdxh() {

		return ((Visit) getPage().getVisit()).getString9();
	}
	
	//������_begin
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	//������_end
	
	public boolean isGongsdp() {
		return ((Visit) getPage().getVisit()).getboolean2();
	}

	public void setGongsdp(boolean gongsdp) {
		((Visit) getPage().getVisit()).setboolean2(gongsdp);
	}
	
	public boolean isDiancdp() {
		return ((Visit) getPage().getVisit()).getboolean3();
	}

	public void setDiancdp(boolean diancdp) {
		((Visit) getPage().getVisit()).setboolean3(diancdp);
	}
	
//�տλ	
    public IDropDownBean getShoukdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean9()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean9((IDropDownBean)getIShoukdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean9();
    }
    
    public void setShoukdwValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean9()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean9(value);
        }
    }
    
    public void setIShoukdwModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel9(value);
	}
    
    public IPropertySelectionModel getIShoukdwModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel9()==null){
        
    		getIShoukdwModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel9();
    }
    
    public IPropertySelectionModel getIShoukdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "��ѡ��"));
			String sql = "select distinct quanc as shoukdw from shoukdw order by quanc";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("shoukdw")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
//  ������λ	
    public IDropDownBean getFahdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getFahdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean10();
    }
    
    public void setFahdwValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean10()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean10(value);
        }
    }
    
    public void setFahdwModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel10(value);
	}
    
    public IPropertySelectionModel getFahdwModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel10()==null){
        
    		getFahdwModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel10();
    }
    
    public IPropertySelectionModel getFahdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "��ѡ��"));
			String sql = "select distinct quanc as fahdw from gongysb where leix=1 order by quanc";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("fahdw")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
//  ��վ	
    public IDropDownBean getFazValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getFazModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean11();
    }
    
    public void setFazValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean11()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean11(value);
        }
    }
    
    public void setFazModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel11(value);
	}
    
    public IPropertySelectionModel getFazModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel11()==null){
        
    		getFazModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel11();
    }
    
    public IPropertySelectionModel getFazModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "��ѡ��"));
			String sql = "select distinct quanc as faz from chezxxb where leib='��վ' order by quanc";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("faz")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}
//  ��������	
    public IDropDownBean getPinzValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean12()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean12((IDropDownBean)getPinzModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean12();
    }
    
    public void setPinzValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean12()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean12(value);
        }
    }
    
    public void setPinzModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel12(value);
	}
    
    public IPropertySelectionModel getPinzModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel12()==null){
        
    		getPinzModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel12();
    }
    
    public IPropertySelectionModel getPinzModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "��ѡ��"));
			String sql = "select distinct mingc as pinz from pinzb where zhuangt=1 order by mingc";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("pinz")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel12(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}
    
//  ԭ�ջ���
    public IDropDownBean getYuanshrValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean13()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean13((IDropDownBean)getYuanshrModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean13();
    }
    
    public void setYuanshrValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean13()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean13(value);
        }
    }
    
    public void setYuanshrModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel13(value);
	}
    
    public IPropertySelectionModel getYuanshrModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel13()==null){
        
    		getYuanshrModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel13();
    }
    
    public IPropertySelectionModel getYuanshrModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "��ѡ��"));
			String sql = "select distinct quanc as yuanshr from diancxxb order by quanc";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("yuanshr")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel13(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel13();
	}
    
//  ���Ƶ�λ
    public IDropDownBean getTianzdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean14()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean14((IDropDownBean)getTianzdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean14();
    }
    
    public void setTianzdwValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean14()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean14(value);
        }
    }
    
    public void setTianzdwModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel14(value);
	}
    
    public IPropertySelectionModel getTianzdwModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel14()==null){
        
    		getTianzdwModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel14();
    }
    
    public IPropertySelectionModel getTianzdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "��ѡ��"));
			String sql = "select distinct quanc as tianzdw from diancxxb order by quanc";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("tianzdw")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel14(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel14();
	}
    
//	�������� DropDownBean8
//  �������� ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='����' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   �������� end
	
//***************************************************************************//
	public boolean getRaw() {
		return true;
	}

//	��ʽ��
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//��ǧλ�ָ���
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
//	public IPropertySelectionModel getIFahdwModels(){
//    	
//    	String sql="";
//    		
//    		if(((Visit)getPage().getVisit()).isDCUser()){
//        		
//        		sql=" select id,gongysmc from "
//    				+ " (select distinct gongysb_id as id,gongysmc from jiesb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and fuid=0 and liucztb_id=0)"
//    				+ " union"
//    				+ " (select distinct gongysb_id as id,gongysmc from jiesyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and fuid=0 and liucztb_id=0) order by gongysmc";
//        	
//        	}else if(((Visit)getPage().getVisit()).isGSUser()){
//        		
//        		sql=" select id,gongysmc from "
//    				+ " (select distinct gongysb_id as id,gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and fuid=0 and liucztb_id=0)"
//    				+ " union"
//    				+ " (select distinct gongysb_id as id,gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and fuid=0 and liucztb_id=0) order by gongysmc";
//        		
//        	}else if(((Visit)getPage().getVisit()).isJTUser()){
//        		
//        		sql="select id,gongysmc from "
//    				+ " (select distinct gongysb_id as id,gongysmc from diancjsmkb where fuid=0 and liucztb_id=0)"
//    				+ " union"
//    				+ " (select distinct gongysb_id as id,gongysmc from diancjsyfb where fuid=0 and liucztb_id=0) order by gongysmc";
//        	}
// 
//    	((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));
//        return ((Visit)getPage().getVisit()).getProSelectionModel2();
//    }
	

    //********************************************************************************//
    
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
    
//		�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "(select id,mingc from diancxxb where id="
			+((Visit) getPage().getVisit()).getDiancxxb_id()
			+") union (select id,mingc from diancxxb where fuid="
			+((Visit) getPage().getVisit()).getDiancxxb_id()+") order by mingc";
		
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
}