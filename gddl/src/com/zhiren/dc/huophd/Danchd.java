package com.zhiren.dc.huophd;
 
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Radio;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.SysConstant;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.jilgl.Jilcz;

public class Danchd extends BasePage implements PageValidateListener {
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

	// ҳ��仯��¼

	public String getChange() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setChange(String change) {
		((Visit) this.getPage().getVisit()).setString1(change);
	}
	
	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString8();
	}

	public void setYansbh(String change) {
		((Visit) this.getPage().getVisit()).setString8(change);
	}
	
	public String getDporlp() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setDporlp(String value) {
		((Visit) this.getPage().getVisit()).setString2(value);
	}
	
	public String getBiaozhp() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setBiaozhp(String value) {
		((Visit) this.getPage().getVisit()).setString3(value);
	}
	
	public String getBzhpArray() {
		
		return ((Visit) this.getPage().getVisit()).getString9();
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		
		_DeleteChick = true;
	}
	
	private boolean _UpdataChick = false;

	public void UpdataButton(IRequestCycle cycle) {
		
		_UpdataChick = true;
	}
	
	private boolean _QuerChick = false;

	public void QuerButton(IRequestCycle cycle) {
		
		_QuerChick = true;
	}
	
	private boolean _ReturChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		
		_ReturChick = true;
	}

	private boolean _FeiyxgChick = false;

	public void FeiyxgButton(IRequestCycle cycle) {
		
		_FeiyxgChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		
		if (_DeleteChick) {
			_DeleteChick = false;
			DeleteYhdcp();		//ɾ���Ѻ˶�Ϊ����ĳ�Ƥ
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		if (_ReturChick) {
			_ReturChick = false;
//			���ػ�Ʊ�˶�ѡ�񷢻�����
			cycle.activate("Huophd");
		}
		if (_QuerChick) {
			_QuerChick = false;
//			���÷��õ���ֱ�ӱ���
			Querhd(getChange());
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		
		if (_UpdataChick) {
			_UpdataChick = false;
//			������޸ĵĵ�����¼
			UpdateChep();
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		
		if (_FeiyxgChick) {
			_FeiyxgChick = false;
			//Ϊ��ת��Huopdҳ����׼����Ϊ������ֵ
			gotoHuopd();
			cycle.activate("Huopd");
		}
		
		if(_ReturChick){
		   _ReturChick=false;
		   cycle.activate("Huophd");
		}
	}
	
	private void Querhd(String strchange) {
		// TODO �Զ����ɷ������
		
		try{
			
			if(!this.getBiaozhp().equals("")){
				
				Visit visit = (Visit) this.getPage().getVisit();
				ExtGridUtil egu=getExtGrid_Weihd();
				String mstr_FeiybId="";
				long Yansbhb_id=MainGlobal.getTableId("yansbhb", "bianm", visit.getString8());
				double mdb_Tmp[]=null;
				
				ResultSetList mdrsl = egu.getModifyResultSet(strchange);
				while(mdrsl.next()){
					for(int i=1;i<mdrsl.getColumnCount();i++) {
						if(mdrsl.getColumnNames()[i].equals("FYZID")){
							
							mstr_FeiybId=mdrsl.getString(i);
							break;
						}
						
						
					}
				}
				
//				���浽���ñ�
				mdb_Tmp=SaveFeiyb(visit.getLong1(),mstr_FeiybId);
				
				if(mdb_Tmp[0]>=0){
					
					this.gotoHuopd();
					String ChepId[]=null;
					
					if(visit.getString4()!=null&&!visit.getString4().equals("")){
						
						ChepId=visit.getString4().split(",");
					}
					
					
					String Lpbiaoz[]=null;
					
					if(visit.getString11()!=null&&!visit.getString11().equals("")){
//						������¼
						Lpbiaoz=visit.getString11().split(",");
					}
					
//					���浽yunfdjb��danjcpb��
					mdb_Tmp=SaveYunfdjb(ChepId,visit.getLong1(),mdb_Tmp[2],visit.getString5(),
							mdb_Tmp[1],visit.getLong2(),Yansbhb_id,visit.getString2(),Lpbiaoz);
				}
				
				if(mdb_Tmp[0]>=0){
					
					
					this.setMsg("�����ɹ���");
				}else{
					
					this.setMsg("����ʧ�ܣ�");
				}
					
			}else{
				
				this.setMsg("ϵͳ�в��������Ƴ�Ƥ�ĺ˶���Ϣ����ѡ��\"�����޸�\"��ť��");
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	private double[] SaveYunfdjb(String[] ChepId, long Diancxxb_id, double FeiyfzId, String Biaoz, 
			double Zongje, long Feiylbb_id, long Yansbhb_id,String Type,String[] LpBiaoz) {
		// TODO �Զ����ɷ������
		
		int flag=0;
		long Yunfdjb_id=0;
		double Yunfdjb[]=new double[1];
		JDBCcon con=new JDBCcon();
		StringBuffer SBsql = new StringBuffer("begin \n");
		
		if(Type.equals("dp")){
			
			for(int i=0;i<ChepId.length;i++){
				
				Yunfdjb_id=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
				SBsql.append("insert into yunfdjb (id, danjbh, feiyb_id, ches, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id)	\n"
							+ " values ("+Yunfdjb_id+",'',"+FeiyfzId+",1, "+Biaoz+", "+Zongje+", " +
								"'"+((Visit) getPage().getVisit()).getRenymc()+"', sysdate, " +
										"'', "+Feiylbb_id+"); \n");
				
				SBsql.append("insert into danjcpb (id, yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl)	\n"
							+ " values (getnewid("+Diancxxb_id+"), "+Yunfdjb_id+", "+ChepId[i]+", 0, "+Yansbhb_id+", "+Biaoz+"); \n");
				
				SBsql.append("update chepb set hedbz=4 where id="+ChepId[i]+"; \n");
			}
		}else if(Type.equals("lp")){
			
			Yunfdjb_id=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
			SBsql.append("insert into yunfdjb (id, danjbh, feiyb_id, ches, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id)	\n"
						+ " values ("+Yunfdjb_id+",'',"+FeiyfzId+","+ChepId.length+","+Biaoz+", "+Zongje+", " +
							"'"+((Visit) getPage().getVisit()).getRenymc()+"', sysdate, " +
									"'', "+Feiylbb_id+"); \n");
			
			for(int i=0;i<ChepId.length;i++){
				
				SBsql.append("insert into danjcpb (id, yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl)	\n"
						+ " values (getnewid("+Diancxxb_id+"), "+Yunfdjb_id+", "+ChepId[i]+", 0, "+Yansbhb_id+","+LpBiaoz[i]+"); \n");
				
				SBsql.append("update chepb set hedbz=4 where id="+ChepId[i]+"; \n");
			}
		}
		
		SBsql.append(" end;");
		flag=con.getInsert(SBsql.toString());
		con.Close();
		
		Yunfdjb[0]=flag;
		return Yunfdjb;
	}
	

	private double[] SaveFeiyb(long Diancxxb_id,String FeiybId) {
		// TODO �Զ����ɷ������
		JDBCcon con=new JDBCcon();
		double Feiyb[]=new double[3];
		
		try{
			double mdb_Jine=0;
			int flag=0;
			String sql="select * from biaozfyb where biaozhpb_id="+FeiybId;
			ResultSet rs=con.getResultSet(sql);
			((Visit) this.getPage().getVisit()).setInt3(JDBCcon.getRow(rs));
			if(((Visit) this.getPage().getVisit()).getInt3()>0){
				long FeiyfzId=Long.parseLong(MainGlobal.getNewID(Diancxxb_id)),Id=0,i=0;
				StringBuffer SBsql = new StringBuffer("begin \n");
				while(rs.next()){
					
					if(i==0){
						
						SBsql.append("insert into feiyb (id, feiyb_id, feiyxmb_id, shuib, zhi) \n"
								+ " \tvalues ("+FeiyfzId+", "+FeiyfzId+", "+rs.getLong("feiyxmb_id")+", "+rs.getInt("shuib")+", "+rs.getDouble("zhi")+"); \n");
					}else{
						Id=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
						SBsql.append("insert into feiyb (id, feiyb_id, feiyxmb_id, shuib, zhi) \n"
								+ " \tvalues ("+Id+", "+FeiyfzId+", "+rs.getLong("feiyxmb_id")+", "+rs.getInt("shuib")+", "+rs.getDouble("zhi")+"); \n");
					}
					i++;
//					yunfdjb���ۼƽ��
					mdb_Jine+=rs.getDouble("zhi");
				}
				SBsql.append(" end;");
				flag=con.getInsert(SBsql.toString());
				
				Feiyb[0]=flag;
				Feiyb[1]=mdb_Jine;
				Feiyb[2]=FeiyfzId;
			}
			rs.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Feiyb;
	}

	private void DeleteYhdcp() {
		// TODO �Զ����ɷ������
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=getExtGrid_Yihd();
		StringBuffer sql = new StringBuffer();	//��¼chepb_id
		StringBuffer sql2 = new StringBuffer();	//��¼yunfdjb_id
		String strsql="";
		try{
			
			ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
			while(mdrsl.next()) {
				
//				ȡ����Ƥ��id,��ͨ��danjcpb�ҵ�yunfdjb��feiylbb�ǵ�ǰ���õ�
//				ɾ����yunfdjb��Ӧ��danjcpb�еļ�¼
//				ɾ��yunfdjb��¼(�ֵ�Ʊ����Ʊ���ִ�����)
				
				sql.append(mdrsl.getString("ID")).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			
			sql2.append("select distinct dj.yunfdjb_id 	\n"
						+ " from danjcpb dj,yunfdjb yd 	\n"
						+ " where dj.yunfdjb_id=yd.id and dj.chepb_id in ("+sql.toString()+")	\n"
	                    + " and yd.feiylbb_id="+visit.getLong2());
			
//			ȡ����Ƥ��id,��ͨ��danjcpb�ҵ�yunfdjb��feiylbb�ǵ�ǰ���õ�yunfdjb_id
			ResultSet rs=con.getResultSet(sql2.toString());
			sql2.delete(0, sql2.length());
			while(rs.next()){
				
				sql2.append(rs.getString("yunfdjb_id")).append(",");
			}
			rs.close();
			sql2.deleteCharAt(sql2.length()-1);
			
			strsql="delete from danjcpb where yunfdjb_id in	\n" 
				+ " ("+sql2.toString()+") \n"
				+ " and chepb_id in ("+sql.toString()+")";
			
//			ɾ����yunfdjb��Ӧ��danjcpb�еļ�¼
			if(con.getDelete(strsql)>=0){
				
//				ɾ��yunfdjb��¼(�ֵ�Ʊ����Ʊ���ִ�����)
				if(ReCountYunfdjb(sql2.toString())>=0){
					
					if(UpdateChepbHedbz(sql.toString())>=0){
						
						this.setMsg("��Ƥɾ���ɹ���");
					}else{
						
						this.setMsg("����ʧ�ܣ�");
					}
					
				}else{
					
					this.setMsg("����ʧ�ܣ�");
				}
			}else{
				
				this.setMsg("����ʧ�ܣ�");
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}

	private int UpdateChepbHedbz(String ChepbIDs) {
		// TODO �Զ����ɷ������
//		���³�Ƥ��hedbz=3
		JDBCcon con=new JDBCcon();
		int flag=-1;
		
			String sql="update chepb set hedbz=3 where id in ("+ChepbIDs+")";
			flag=con.getUpdate(sql);
			
		con.Close();	
		return flag;
	}

	private int ReCountYunfdjb(String sql) {
		// TODO �Զ����ɷ������
		
		JDBCcon con=new JDBCcon();
		StringBuffer sql2 = new StringBuffer("begin		\n");
		String strsql="";
		String YunfdjbId[]=sql.split(",");
		int flag=-1;
		try{
			
			ResultSet rs=null;
			for(int i=0;i<YunfdjbId.length;i++){
				
				strsql=" select sum(c.biaoz) as biaoz from danjcpb dj,chepb c,yunfdjb yd "
					+ " where dj.yunfdjb_id=yd.id and dj.chepb_id=c.id and yd.id="+YunfdjbId[i];
				
				rs=con.getResultSet(strsql);
				
				if(rs.next()){
					
//					��Ʊ����ɾ��danjcpb�еĲ�����Ϣ����ɾ���˷ѵ��ݱ�����Ϣ
					sql2.append(" delete from danjcpb where yunfdjb_id="+YunfdjbId[i]+";	\n");
				}
					
//					��Ʊ��danjcpb���޲�����Ƥ��ֱ��ɾ��yunfdjb����Ϣ
					sql2.append(" delete from yunfdjb where id="+YunfdjbId[i]+";	\n");
			}
			rs.close();
			sql2.append(" end;");
			flag=con.getDelete(sql2.toString());
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return flag;
	}

	private void gotoHuopd() {
		// TODO �Զ����ɷ������
		
		ExtGridUtil egu=getExtGrid_Weihd();
		ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
		String mstr_chepbid="";
		String mstr_chebb_id="";
		String mstr_biaoz="";
		String mstr_lp_biaoz="";
		double mdb_biaoz=0;
		
		if(this.getDporlp().equals("dp")){
			
			while(mdrsl.next()){
			
				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					if(i==1){
	    				
						mstr_chepbid+=mdrsl.getString("ID")+",";
	    			}
					
	    			if(mdrsl.getColumnNames()[i].equals("BIAOZ")){
						
	    				mstr_biaoz=mdrsl.getString(i);
					}else if(mdrsl.getColumnNames()[i].equals("CHEBB_ID")){
						
						mstr_chebb_id=mdrsl.getString(i);
					}
				}
			}	
		}else if(this.getDporlp().equals("lp")){
			
			
			while(mdrsl.next()){
			
				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					if(i==1){
						
						mstr_chepbid+=mdrsl.getString("ID")+",";
					}	
					
	    			if(mdrsl.getColumnNames()[i].equals("BIAOZ")){
						
	    				mstr_lp_biaoz+=mdrsl.getString(i)+",";
	    				mdb_biaoz+=Double.parseDouble(mdrsl.getString(i));
					}else if(mdrsl.getColumnNames()[i].equals("CHEBB_ID")){
						
						mstr_chebb_id=mdrsl.getString(i);
					}
				}
			}
			mstr_biaoz=String.valueOf((double)Math.round(mdb_biaoz*100)/100);
			
			if(!mstr_lp_biaoz.equals("")){
				
				mstr_lp_biaoz=mstr_lp_biaoz.substring(0,mstr_lp_biaoz.lastIndexOf(","));
			}
		}
		
		
		//����
		((Visit) this.getPage().getVisit()).setInt2(Integer.parseInt(mstr_chebb_id));
		
		//��Ʊor��ƱString2
		getDporlp();
		
//		��׼��Ʊ����String3
		this.getBiaozhp();
		
//		ѡ�г��ŵ�id��String4
		((Visit) this.getPage().getVisit()).setString4(mstr_chepbid.substring(0,mstr_chepbid.lastIndexOf(",")));
		
//		Ʊ��(��ƱΪ����Ʊ�ء���ƱΪ��ѡ��Ƥ����Ʊ��)
		((Visit) this.getPage().getVisit()).setString5(mstr_biaoz);
		
//		��Ʊ��Ʊ�أ�
		((Visit) this.getPage().getVisit()).setString11(mstr_lp_biaoz);
	}

	private void UpdateChep() {
		// TODO �Զ����ɷ������
		Visit visit = (Visit) this.getPage().getVisit();
		this.Save(getChange(), visit);
	}
	
	public void Save(String strchange,Visit visit) {
		
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=getExtGrid_Weihd();
		String tableName="chepb";
		String SavebzSfgxyssj="0";	//�Ƿ���޸�ԭʼ���ݣ����ı���ʱ�Ƿ����chepb��ԭʼ���ݣ�
		String strChepb_Id="0";		//chepb_id
		String strCheph="";			//��Ƥ��
		String strChebb_Id="0";		//����
		String strBiaoz="0";		//
		
		try {
		
			SavebzSfgxyssj=MainGlobal.getTableCol("feiylbb", "shifggyssj", "id", String.valueOf(((Visit) this.getPage().getVisit()).getLong2()));
		
			StringBuffer sql = new StringBuffer("begin \n");
		
			ResultSetList delrsl = egu.getDeleteResultSet(strchange);
			while(delrsl.next()) {
				sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
			}
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		
			if(SavebzSfgxyssj.equals("1")){		
	//			��������ʱ�޸�ԭʼ���ݣ����޸ĺ�����ݱ��浽yunfjszlb��
				
				while(mdrsl.next()) {
					
					sql.append("update ").append(tableName).append(" set ");
					for(int i=1;i<mdrsl.getColumnCount();i++) {
						
						if(mdrsl.getColumnNames()[i].equals("CHEPH")||mdrsl.getColumnNames()[i].equals("BIAOZ")){
							
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");
							sql.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
						}else if(mdrsl.getColumnNames()[i].equals("CHEB")){
							
							sql.append("CHEBB_ID").append(" = ");
							sql.append(MainGlobal.getProperId(getIChebModel(),egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)).replace('\'', ' ').trim())).append(",");
						}
					}
					sql.deleteCharAt(sql.length()-1);
					sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
					
					sql.append("end;");
					
	//				���޸�ǰ��Ƥ����Ϣ����"Chepsjtzb"����
					Jiesdcz.UpdateChepsjtzb(visit.getDiancxxb_id(),Long.parseLong(mdrsl.getString("ID")),visit.getRenymc());
					
					strChepb_Id=mdrsl.getString("ID");
					strCheph=mdrsl.getString("CHEPH");
					strChebb_Id=mdrsl.getString("CHEBB_ID");
					strBiaoz=mdrsl.getString("BIAOZ");
					con.getUpdate(sql.toString());
					
	//				����chepbӯ��������
					Jilcz.CountChepbYuns(con, mdrsl.getString("ID"), SysConstant.HEDBZ_YDP);
					
	//					����fahbӯ��������
					Jilcz.updateFahb(con, MainGlobal.getTableCol("chepb", "fahb_id", "id", mdrsl.getString("ID")));
				}
			}else if(SavebzSfgxyssj.equals("0")){
	//			��������ʱ,���޸�ԭʼ���ݣ����浽yunfjszlb��
				
				while(mdrsl.next()) {
					
					strChepb_Id=mdrsl.getString("ID");
					strCheph=mdrsl.getString("CHEPH");
					strChebb_Id=mdrsl.getString("CHEBB_ID");
					strBiaoz=mdrsl.getString("BIAOZ");
				}
			}
		
//		���浽�˷ѽ�����������(DIANCXXB_ID,FEIYLBB_ID,CHEPB_ID,CHEPH,CHEBB_ID,BIAOZ)
			if(Jiesdcz.InsertYunfjszlb(((Visit) this.getPage().getVisit()).getLong1(),((Visit)this.getPage().getVisit()).getLong2(),strChepb_Id,strCheph,strChebb_Id,strBiaoz)){
				
				this.setMsg("����ɹ���");
			}else{
				
				this.setMsg("����ʧ�ܣ�");
			}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	//δ�˶�
	public void getWeihdSelectData() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		
		String sql_condition="";	//2009-6-9�ոģ�Ϊ�˽��ͬһ�Է���վ֮�䲻ͬú���в�ͬ��̵�����
		
		sql_condition=Jiesdcz.getLic_id(((Visit) getPage().getVisit()).getString6());
		
		if(!sql_condition.equals("")){
			
			sql_condition=" and lc.id="+sql_condition;
		}
		
		sql=" select nvl(cp.id,0) as id,nvl(fyzid,0) as fyzid,gys.mingc as fahdw,m.mingc as meikdwmc,cz.mingc as faz,daoz.mingc as daoz,to_char(f.fahrq,'yyyy-MM-dd') as fahrq,"
			 + " to_char(f.daohrq,'yyyy-MM-dd') as daohrq,cp.cheph as cheph,decode(gys.mingc,null,0,sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl))) as biaoz,nvl(decode(cp.chebb_id,1,'·��',2,'�Ա���',3,'����',4,'��'),'') as cheb,cp.chebb_id,"
	         + " nvl(jine.jine,0) as jine,nvl(jine.lic,0) as lic,'' as  yansbh,'' as feiymx"                 
	         + " from fahb f,gongysb gys,meikxxb m,chezxxb cz,zhilb z,caiyb cy," 
	                 
	         + " (select distinct chepb.*,fahb.meikxxb_id from chepb,fahb where chepb.fahb_id=fahb.id and fahb.lie_id in ("+((Visit) getPage().getVisit()).getString6()+") and chepb.id not in (select chepb_id from danjcpb dj,yunfdjb yd" 
	         + " where dj.yunfdjb_id=yd.id and yd.feiylbb_id="+((Visit) getPage().getVisit()).getLong2()+" and dj.chepb_id in" 
	         + " (select chepb.id from chepb,fahb where chepb.fahb_id=fahb.id and fahb.lie_id in ("+((Visit) getPage().getVisit()).getString6()+"))) order by chepb.biaoz) cp,"         
	         + " chezxxb daoz,"
	         + " (select yz.* from yunfjszlb yz,fahb fh,chepb cp where yz.chepb_id=cp.id and cp.fahb_id=fh.id and yz.diancxxb_id=fh.diancxxb_id and feiylbb_id="+((Visit) getPage().getVisit()).getLong2()+" and fh.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")) yfzl,"
	         + " (select bz.meikxxb_id,bz.biaoz,bz.cheb,bzfy.biaozhpb_id as fyzid,"
	         + "  nvl(lc.zhi,0) as lic,sum(bzfy.zhi) as jine"
	         + " 	from fahb f, biaozhpb bz,BIAOZFYB bzfy, licb lc,liclxb lclx "
	         + " 	where f.faz_id = bz.faz_id"
	         + "  	and f.daoz_id = bz.daoz_id "
	         + " 	and f.meikxxb_id=bz.meikxxb_id  "
	         + " 	and f.diancxxb_id=bz.diancxxb_id "
	         + "  	and f.faz_id = lc.faz_id(+) "
	         + "  	and f.daoz_id = lc.daoz_id(+) "
	         + "  	and f.diancxxb_id=lc.diancxxb_id(+) "
	         + "  	and bz.id = bzfy.biaozhpb_id "
	         + "	and lc.liclxb_id=lclx.id \n"
	         + "	and bz.meikxxb_id=f.meikxxb_id \n"
	         + "	and lclx.mingc='"+Locale.zonglc_jies+"' \n"	//�����
	         + "  	and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+") and bz.feiylbb_id="+((Visit) getPage().getVisit()).getLong2()+"\n"
	         + sql_condition
	         + " group by bz.meikxxb_id, bz.biaoz, bz.cheb, bzfy.biaozhpb_id,lc.zhi) jine"
	                 
	         + " where f.id=cp.fahb_id and f.gongysb_id=gys.id and f.meikxxb_id=m.id and cp.id=yfzl.chepb_id(+) 	\n"
	         + "      and f.faz_id=cz.id and daoz.id=f.daoz_id and f.zhilb_id=z.id(+) and cy.zhilb_id(+)=z.id"
	         + "      and jine.cheb(+)=cp.chebb_id and jine.biaoz(+)=cp.biaoz and cp.meikxxb_id=jine.meikxxb_id(+)" 
	         + "      and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")"
	               
	         + " group by cp.id,fyzid,gys.mingc,m.mingc,cz.mingc,daoz.mingc,f.fahrq,f.daohrq,cp.cheph,"
	         + "       cp.chebb_id,jine.jine,jine.lic "  
	         + " order by gys.mingc, m.mingc,cz.mingc,f.daohrq,sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) ";
		
		ResultSetList rsl=con.getResultSetList(sql);//δ�˶�
		ExtGridUtil egu = new ExtGridUtil("gridDiv_weihd", rsl);//�趨��¼����Ӧ�ı�
		egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("daoz").setHeader("��վ");
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("cheph").setHeader("��Ƥ��");
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("cheb").setHeader("����");
		egu.getColumn("jine").setHeader("���");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("fyzid").setHidden(true);
		egu.getColumn("lic").setHidden(true);
		egu.getColumn("yansbh").setHidden(true);
		egu.getColumn("feiymx").setHidden(true);
		egu.getColumn("chebb_id").setHidden(true);
		
		egu.getColumn("faz").setWidth(75);
		egu.getColumn("daoz").setWidth(75);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("cheb").setWidth(70);
		egu.getColumn("jine").setWidth(80);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		
//		���ձ�ų�ʼ��_Begin
		
		if(MainGlobal.getXitxx_item("����", Locale.shiyysbh_jies, String.valueOf(((Visit) getPage().getVisit()).getLong1()), "��").equals("��")){
			
			if(((Visit) getPage().getVisit()).getString8().equals("")){
				
				this.setYansbh(MainGlobal.getYansbh());
				((Visit) getPage().getVisit()).setString8(this.getYansbh());
			}else{
				
				this.setYansbh(((Visit) getPage().getVisit()).getString8());
			}
		}
//		���ձ�ų�ʼ��_End
		
		//Toolbar ��ʾ
		
		//radio_begin
		Radio rddp= new Radio("RG_HEDFS");
		rddp.setId("RG_HEDFS_DP");
		rddp.setBoxLabel("��Ʊ ");
		Radio rdlp= new Radio("RG_HEDFS");
		rdlp.setId("RG_HEDFS_LP");
		rdlp.setBoxLabel("��Ʊ");
		
		if(this.getDporlp().equals("")||this.getDporlp().equals("dp")){
			
			rddp.setChecked(true);
			this.setDporlp("dp");
		}else{
			
			rdlp.setChecked(true);
			this.setDporlp("lp");
		}
		egu.addToolbarItem(rddp.getScript());
		egu.addToolbarItem(rdlp.getScript());
		//radio_end
		
//		checkbox_begin
		egu.addTbarText("-");
		Checkbox cbsavalike=new Checkbox();
		cbsavalike.setChecked(true);
		cbsavalike.setId("SaveLike");
		egu.addToolbarItem(cbsavalike.getScript());
		egu.addTbarText("������ͬ��Ʊ");
//		checkbox_end
		
		egu.addTbarText("-");
		egu.addTbarText("��Ƥ��");
		TextField tfcph=new TextField();
		tfcph.setId("T_CHEPH");
		tfcph.setWidth(80);
		egu.addToolbarItem(tfcph.getScript());
		
		egu.addTbarText("Ʊ��");
		TextField tfbz=new TextField();
		tfbz.setId("T_BIAOZ");
		tfbz.setWidth(80);
		egu.addToolbarItem(tfbz.getScript());
		
		egu.addTbarText("����");
		ComboBox ChebSelect = new ComboBox();
		ChebSelect.setId("Cheb");
		ChebSelect.setWidth(80);
		ChebSelect.setLazyRender(true);
		ChebSelect.setTransform("ChebSelect");
		egu.addToolbarItem(ChebSelect.getScript());
		
		egu.addTbarText("-");
		
		String condition="";
		
		condition="var Mrcd = gridDiv_weihd_ds.getModifiedRecords();	\n"
			+ " if(Mrcd!=''){	\n"
			+ " 	Ext.MessageBox.alert('��ʾ��Ϣ','�иĶ�����Ϣ��δ����');	\n"
			+ " 	return;	\n"	
			+ " }	\n";
		
		egu.addToolbarButton("ȷ���޸�",GridButton.ButtonType_Save,"UpdataButton");
		
		egu.addTbarText("-");
		egu.addToolbarButton("ȷ�Ϻ˶�",GridButton.ButtonType_SubmitSel_condition,"QuerButton",condition);
		
		egu.addTbarText("-");
		egu.addToolbarButton("�����޸�",GridButton.ButtonType_SubmitSel_condition,"FeiyxgButton",condition);
		
//		egu.addToolbarItem("{"+new GridButton("�����޸�","function(){ " +
//				" \tvar rec=gridDiv_weihd_grid.getSelectionModel().getSelected();\n"+
//				" \tif(rec==null){ \n" +
//				" \t	alert('��ѡ��Ҫ�˶Եĳ�Ƥ��Ϣ!');return;	\n" +
//				" \t}else{	\n" +
//				" \t	document.getElementById('FeiyxgButton').click();	\n" +
//				" }}").getScript()+"}");
		
		egu.addTbarText("-");
		egu.addToolbarItem("{"+new GridButton("����","function(){ document.getElementById('ReturnButton').click();" +
				"}").getScript()+"}");
		
		//��������
		egu.addTbarText("-");
		egu.addTbarText("��������");
		TextField tfsearchcph=new TextField();
		tfsearchcph.setId("T_SEARCH_CHEPH");
		tfsearchcph.setWidth(90);
		tfsearchcph.setListeners("specialkey:Searchcheph");
		egu.addToolbarItem(tfsearchcph.getScript());
		
//		ʵ�ֳ����޸ġ�Ʊ���޸ġ������޸�
		egu.addOtherScript("T_CHEPH.on('change',function(){	\n" 
				+ "	\tvar i=-1;\n"
				+ "	\ti=document.getElementById('EditTableRow').value;	\n"
				+ "	\tif(i>-1&&T_CHEPH.getValue()!=''){	\n"
				+ " \t	gridDiv_weihd_ds.getAt(i).set('CHEPH',T_CHEPH.getValue());}})	\n"
				
				+ "	\tT_BIAOZ.on('change',function(){	\n"
				+ "	\tvar i=-1;	\n"
				+ "	\ti=document.getElementById('EditTableRow').value;	\n"
				+ "	\tif(i>-1&&T_BIAOZ.getValue()!=''){	\n"
				+ "		gridDiv_weihd_ds.getAt(i).set('BIAOZ',T_BIAOZ.getValue());}})	\n"
				
				+ "	\tCheb.on('select',function(){	\n"
				+ "	\t	var i=-1;	\n"
				+ "	\t	i=document.getElementById('EditTableRow').value;	\n"
				+ "	\t	if(i>-1){	\n"
				+ " \t		gridDiv_weihd_ds.getAt(i).set('CHEB',Cheb.getRawValue());}})	\n"
		);
		
//		����ϼ���ϼ������·����Զ�ѡ����ͬ��Ʊ
		egu.addOtherScript("gridDiv_weihd_grid.on('rowclick',function(own,row,e){\n "
								+ "	if(gridDiv_weihd_sm.isSelected(row)){\n "
								+ "		var rec = gridDiv_weihd_ds.getAt(row); \n"
								+ " 	var biaoz=0,length=0,jine=0,lic=0,dcbiaoz=0; \n"
								+ " 	var fahdw='',meikdw='',faz='',daoz='',cheb='',cheph='',strtmp=''; \n"
								+ " 	fahdw=rec.get('FAHDW');\n"
								+ " 	meikdw=rec.get('MEIKDWMC');\n"
								+ " 	faz=rec.get('FAZ');\n"
								+ "		daoz=rec.get('DAOZ');\n"
								+ "		cheb=rec.get('CHEB');\n"
								+ "		cheph=rec.get('CHEPH');\n"
								+ "		dcbiaoz=rec.get('BIAOZ');\n"
								+ " 	lic=rec.get('LIC');	\n"
								+ "		T_SEARCH_CHEPH.setValue(cheph);	\n"
								+ "		document.getElementById('EditTableRow').value=row;	\n"
								+ "		T_CHEPH.setValue(cheph);T_BIAOZ.setValue(dcbiaoz);Cheb.setRawValue(cheb); \n"
								+ " 	setBiaozhpmx(faz,daoz,cheb,dcbiaoz,lic);	\n"
								+ " 	if(SaveLike.checked){ \n"
								+ "			for(var i=0	;i<gridDiv_weihd_ds.getCount();i++){\n"
								+ "				if(gridDiv_weihd_ds.getAt(i).get('FAHDW')==fahdw && gridDiv_weihd_ds.getAt(i).get('MEIKDWMC')==meikdw \n"
								+ "					&& gridDiv_weihd_ds.getAt(i).get('FAZ')==faz && gridDiv_weihd_ds.getAt(i).get('DAOZ')==daoz \n"
								+ "					&& gridDiv_weihd_ds.getAt(i).get('CHEB')==cheb && gridDiv_weihd_ds.getAt(i).get('BIAOZ')==dcbiaoz){ \n"
								+ "					if(strtmp==' '){ \n"			
								+ " 					strtmp=i;\n"
								+ "					}else{\n"
								+ "						strtmp=strtmp+','+i;}}}\n"
								+ "		gridDiv_weihd_sm.selectRows(strtmp.split(','));	\n"
								+ " 	}else{	\n"
								+ " 		gridDiv_weihd_sm.selectRow(row,true);		\n" 
								+ "		}	\n"
								+ " 	rec = gridDiv_weihd_grid.getSelectionModel().getSelections();\n"
								+ "		length=rec.length;	\n"
								+ "		for(var i=0;i<rec.length;i++){ \n"
								+ "			if(0!=rec[i].get('ID')){	\n"
								+ "			biaoz+=eval(rec[i].get('BIAOZ')); \n"
								+ "			jine+=eval(rec[i].get('JINE'));	\n"
								+ "		}else{\n"
								+ "			length--;	}}	\n"
								+ "		Hej_ches.setValue(length);	\n"
								+ "		Hej_biaoz.setValue(Math.round(biaoz*100)/100);	\n"
								+ "		Hej_jine.setValue(Math.round(jine*100)/100);	\n"
//								+ "	\t	gridDiv_weihd_ds.getAt(gridDiv_weihd_ds.getCount()-1).set('CHEPH',length);	\n"
//								+ "	\t	gridDiv_weihd_ds.getAt(gridDiv_weihd_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
//								+ "	\t	gridDiv_weihd_ds.getAt(gridDiv_weihd_ds.getCount()-1).set('JINE',jine);		\n" 
								+ "		}\n"
								+ "	});");
		
		//�������½Ǳ�׼��Ʊ����ϸ��Ϣ��
		egu.addOtherScript("RG_HEDFS_DP.on('focus',function(){	\n" 
				+ "	\tdocument.getElementById('DPORLP').value='dp';});	\n" 
				+ "	RG_HEDFS_LP.on('focus',function(){	\n"
				+ "	\tdocument.getElementById('DPORLP').value='lp';});	\n" 
				+ "	function setBiaozhpmx(faz,daoz,cheb,dcbiaoz,lic){	\n" 
				+ "	\tfor(var i=0;i<Biaozhpfymx.length;i++){  \n"	
				+ " \t\tif(faz==Biaozhpfymx[i][2]	\n"
	 			+ " \t\t&&daoz==Biaozhpfymx[i][3]	\n"
	 			+ " \t\t&&cheb==Biaozhpfymx[i][1]	\n" 
	 			+ " \t\t&&dcbiaoz==Biaozhpfymx[i][0]	\n" 		
	 			+ " \t\t&&lic==Biaozhpfymx[i][4]){	\n"	 		
 				+ " \t\t\tdocument.getElementById('BIAOZHP').value=Biaozhpfymx[i][6];	\n"	
 				+ "	\t\t\tdocument.getElementById('BIAOZHP_TEXT').value=Biaozhpfymx[i][6];	\n"
 				+ "		return;	\n"
 				+ " }else{	\n" 
 				+ "	\t\t\tdocument.getElementById('BIAOZHP').value='';	\n"
 				+ "	\t\t\tdocument.getElementById('BIAOZHP_TEXT').value='';	\n"
 				+ "}}}"	
 				
 				+ "	gridDiv_weihd_grid.on('click',function(){	\n"
 				+ "		reCountToolbarNum(this);		\n"
 				+ "});"
 				
// 				Tab�л�������Toolbar�ϼ�
 				+ " function reCountToolbarNum(obj){					\n "
 				+ " \tvar rec;											\n"
 				+ " \tvar length=0,biaoz=0,jine=0;						\n"
 				+ " \trec = obj.getSelectionModel().getSelections();	\n"		
 				+ " \tlength=rec.length;								\n"
 				+ " \tfor(var i=0;i<rec.length;i++){					\n" 
 				+ " 	\tif(0!=rec[i].get('ID')){						\n"	
				+ " 		\tbiaoz+=eval(rec[i].get('BIAOZ'));	\n" 
				+ " 		\tjine+=eval(rec[i].get('JINE'));	\n"	
				+ " 	\t}else{								\n"
				+ " 		\tlength--;							\n"	
				+ " 	\t}										\n"
				+ " \t}											\n"	
				+ " \tHej_ches.setValue(length);				\n"	
				+ " \tHej_biaoz.setValue(Math.round(biaoz*100)/100);	\n"
				+ " \tHej_jine.setValue(Math.round(jine*100)/100);		\n"
				+ " \t} \n"
				
//				T_SEARCH_CHEPH ������Ƥ

				+"function Searchcheph(){\n" +
				"\n" + 
				"  if(T_SEARCH_CHEPH.getValue().trim()!=''){\n" + 
				"\n" + 
				"    	var cheph='';\n" + 
				"	 	var cheb='';\n" +
				"	 	var biaoz='';\n" +	
				"     	var i=-1;\n" + 
				"\n" + 
				"     i=gridDiv_weihd_ds.findBy(function(rec){\n" + 
				"\n" + 
				"       if(rec.get('CHEPH')==T_SEARCH_CHEPH.getValue()){\n" + 
				" 			cheph=rec.get('CHEPH');	\n" +
				"			cheb=rec.get('CHEB');	\n" +
				"			biaoz=rec.get('BIAOZ');	\n" +
				"         	return true;\n" + 
				"      	}\n" + 
				"    });\n" + 
				"\n" + 
				"    if(document.getElementById('DPORLP').value=='dp'){\n" + 
				"      var rec;\n" + 
				"      rec = gridDiv_weihd_sm.getSelected();\n" + 
				"\n" + 
				"      if(rec!=null){\n" + 
				"\n" + 
				"        if(rec.get('FAHDW')!=gridDiv_weihd_ds.getAt(i).get('FAHDW')\n" + 
				"          ||rec.get('MEIKDWMC')!=gridDiv_weihd_ds.getAt(i).get('MEIKDWMC')\n" + 
				"          ||rec.get('FAZ')!=gridDiv_weihd_ds.getAt(i).get('FAZ')\n" + 
				"          ||rec.get('DAOZ')!=gridDiv_weihd_ds.getAt(i).get('DAOZ')\n" + 
				"          ||rec.get('CHEB')!=gridDiv_weihd_ds.getAt(i).get('CHEB')\n" + 
				"          ||rec.get('BIAOZ')!=gridDiv_weihd_ds.getAt(i).get('BIAOZ')\n" + 
				"\n" + 
				"        ){\n" + 
				"          Ext.MessageBox.alert('��ʾ��Ϣ','��Ʊ�˶�ʱ������ѡ��ͬ��Ϣ�ļ�¼��');\n" + 
				"          return;\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"\n" + 
				"     if(i>=0){\n" + 
				"       gridDiv_weihd_sm.selectRow(i,true);\n" + 
				"       gridDiv_weihd_grid.getView().focusRow(i);//ָ���� \n" + 
				"		T_BIAOZ.setValue(biaoz);	\n" +
				"		T_CHEPH.setValue(cheph);	\n" +
				"		Cheb.setRawValue(cheb);		\n" +
				"     }else{\n" + 
				"       Ext.MessageBox.alert('��ʾ��Ϣ','û�ж�Ӧ�ĳ��ţ�');\n" + 
				"     }\n" + 
				"     T_SEARCH_CHEPH.focus(true,true);\n" + 
				"  }\n" + 
				" };"
		);		
		
//		������ͬ��Ʊcheckbox���� 
		egu.addOtherScript("SaveLike.on('check',function(){ \n"
						+ "	\tvar fahdw='',meikdw='',faz='',daoz='',cheb='',strtmp='';\n"
						+ "	\tvar biaoz=0,length=0,jine=0,dcbiaoz=0;\n"
						+ "	\tvar rec=gridDiv_weihd_grid.getSelectionModel().getSelected();\n"
						+ "	\tif(rec==null){\n"
						+ " \t}else{\n"
						+ "	\t	fahdw=rec.get('FAHDW');\n"
						+ "	\t	meikdw=rec.get('MEIKDWMC');\n"
						+ "	\t	faz=rec.get('FAZ');\n"
						+ "	\t	daoz=rec.get('DAOZ');\n"
						+ "	\t	cheb=rec.get('CHEB');\n"
						+ "	\t	dcbiaoz=rec.get('BIAOZ');\n"
						+ "	\tfor(var i=0	;i<gridDiv_weihd_ds.getCount();i++){\n"
						+ "	\t	if(gridDiv_weihd_ds.getAt(i).get('FAHDW')==fahdw && gridDiv_weihd_ds.getAt(i).get('MEIKDWMC')==meikdw \n"
						+ "	\t		&& gridDiv_weihd_ds.getAt(i).get('FAZ')==faz && gridDiv_weihd_ds.getAt(i).get('DAOZ')==daoz \n"
						+ "	\t		&& gridDiv_weihd_ds.getAt(i).get('CHEB')==cheb && gridDiv_weihd_ds.getAt(i).get('BIAOZ')==dcbiaoz){ \n"
						+ "	\t		if(strtmp==' '){\n"
						+ "	\t			strtmp=i;\n"
						+ "	\t		}else{	\n"
						+ " \t			strtmp=strtmp+','+i;}}}	\n"
						+ " \t	gridDiv_weihd_sm.selectRows(strtmp.split(','));	\n"
						+ "	\t	rec = gridDiv_weihd_grid.getSelectionModel().getSelections(); \n"
						+ "	\t	length=rec.length;	\n"
						+ "	\t	for(var i=0;i<rec.length;i++){ \n"
						+ "	\t		if(0!=rec[i].get('ID')){	\n"
						+ "	\t			biaoz+=eval(rec[i].get('BIAOZ'));	\n"
						+ " \t			jine+=eval(rec[i].get('JINE'));	\n"		
						+ " \t		}else{\n"
						+ "	\t			length--;	\n"
						+ "	\t		}}	\n"
						+ " \t	Hej_ches.setValue(length);	\n"
						+ " \t	Hej_biaoz.setValue(Math.round(biaoz*100)/100);	\n"
						+ "	\t	Hej_jine.setValue(Math.round(jine*100)/100);	\n"
//						+ "	\t	gridDiv_weihd_ds.getAt(gridDiv_weihd_ds.getCount()-1).set('CHEPH',length);	\n"
//						+ "	\t	gridDiv_weihd_ds.getAt(gridDiv_weihd_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
//						+ "	\t	gridDiv_weihd_ds.getAt(gridDiv_weihd_ds.getCount()-1).set('JINE',jine);	\n"
						+ "	\t}});"
		);
		
//		egu.addOtherScript("function gridDiv_weihd_save(rec){" +
//				"	if(rec.get('FAHDW')=='�ϼ�'){" +
//				"	return 'continue';" +
//				"}}");
		
		//��combobox���
		setExtGrid_Weihd(egu);
		con.Close();
	}
	
	public void getYihdSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
		JDBCcon con = new JDBCcon();
		
		String sql_condition="";	//2009-6-9�ոģ�Ϊ�˽��ͬһ�Է���վ֮�䲻ͬú���в�ͬ��̵�����
		
		sql_condition=Jiesdcz.getLic_id(((Visit) getPage().getVisit()).getString6());
		
		if(!sql_condition.equals("")){
			
			sql_condition=" and lc.id="+sql_condition;
		}
		
		sql=" select nvl(cp.id,0) as id,nvl(fyzid,0) as fyzid,gys.mingc as fahdw,m.mingc as meikdwmc,cz.mingc as faz,daoz.mingc as daoz,to_char(f.fahrq,'yyyy-MM-dd') as fahrq,"
			 + " to_char(f.daohrq,'yyyy-MM-dd') as daohrq,cp.cheph as cheph,decode(gys.mingc,null,0,sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl))) as biaoz,nvl(decode(cp.chebb_id,1,'·��',2,'�Ա���',3,'����',4,'��'),'') as cheb,cp.chebb_id,"
			 + " nvl(jine.jine,0) as jine,'' as  yansbh,"
			 + " '��վ:'||cz.mingc||'   ��վ:'||daoz.mingc||'   Ʊ��:'||decode(gys.mingc,null,0,sum(decode(yfzl.jifzl, null, cp.biaoz, yfzl.jifzl)))||'   ����:'||nvl(decode(cp.chebb_id, 1, '·��', 2, '�Ա���', 3, '����', 4, '��'),'')||'   \\n\\n\\n'||getFeiymx(fyzid) as feiymx\n"                 
	         + " from fahb f,gongysb gys,meikxxb m,chezxxb cz,zhilb z,caiyb cy," 
	         + " (select distinct chepb.*,fahb.meikxxb_id from chepb,fahb where chepb.fahb_id=fahb.id and fahb.lie_id in ("+((Visit) getPage().getVisit()).getString6()+") and  chepb.id in (select chepb_id from danjcpb dj,yunfdjb yd"
	         + "  where dj.yunfdjb_id=yd.id and yd.feiylbb_id="+((Visit) getPage().getVisit()).getLong2()+" and dj.chepb_id in" 
	         + " (select chepb.id from chepb,fahb where chepb.fahb_id=fahb.id and fahb.lie_id in ("+((Visit) getPage().getVisit()).getString6()+"))) order by chepb.biaoz) cp, "         
	         + " chezxxb daoz,"
	         + " (select yz.* from yunfjszlb yz,fahb fh,chepb cp where yz.chepb_id=cp.id and cp.fahb_id=fh.id and yz.diancxxb_id=fh.diancxxb_id and feiylbb_id="+((Visit) getPage().getVisit()).getLong2()+" and fh.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")) yfzl,"        
	         +"(select cp.id,f.meikxxb_id,\n"
	         +"               yfdj.biaoz,\n" 
	         +"               cp.chebb_id as cheb,\n" 
	         +"               fy.id as fyzid,\n" 
	         +"               sum(yfdj.zongje) as jine\n" 
	         +"          from fahb f, chepb cp, danjcpb dj, yunfdjb yfdj, feiyb fy\n"
	         +"         where cp.fahb_id = f.id\n"
	         +"           and dj.chepb_id = cp.id\n"
	         +"           and dj.yunfdjb_id = yfdj.id\n" 
	         +"              --and cp.chebb_id = cb.id\n"
	         +"           and yfdj.feiyb_id = fy.id\n"
	         +"           and f.lie_id in\n"
	         +"               ("+((Visit) getPage().getVisit()).getString6()+")\n"
	         +"           and yfdj.feiylbb_id = "+((Visit) getPage().getVisit()).getLong2()+"\n"
	         +"         group by cp.id,f.meikxxb_id, yfdj.biaoz, cp.chebb_id, fy.id) jine\n"
     
	         + " where f.id=cp.fahb_id and f.gongysb_id=gys.id and f.meikxxb_id=m.id and cp.id=yfzl.chepb_id(+) 	\n"
	         + "      and f.faz_id=cz.id and daoz.id=f.daoz_id and f.zhilb_id=z.id(+) and cy.zhilb_id(+)=z.id"
	         + "      and jine.id(+) = cp.id and jine.cheb(+)=cp.chebb_id  and cp.meikxxb_id=jine.meikxxb_id(+)";
	         if(visit.getLong1()!=323){
	        	 sql+="and jine.biaoz(+)=cp.biaoz\n";
	         }
	         
	         sql+= "      and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")"
	               
	         + " group by cp.id,fyzid,gys.mingc,m.mingc,cz.mingc,daoz.mingc,f.fahrq,f.daohrq,cp.cheph,"
	         + "       cp.chebb_id,jine.jine "  
	         + " order by gys.mingc, m.mingc,cz.mingc,f.daohrq,sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) ";
		
		ResultSetList rsl=con.getResultSetList(sql);//�Ѻ˶�
		ExtGridUtil egu = new ExtGridUtil("gridDiv_yihd", rsl);//�趨��¼����Ӧ�ı�
		egu.getColumn("fahdw").setHeader("������λ");
		egu.getColumn("meikdwmc").setHeader("ú�λ");
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("daoz").setHeader("��վ");
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("cheph").setHeader("��Ƥ��");
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("cheb").setHeader("����");
		egu.getColumn("jine").setHeader("���");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("fyzid").setHidden(true);
//		egu.getColumn("lic").setHidden(true);
		egu.getColumn("yansbh").setHidden(true);
		egu.getColumn("feiymx").setHidden(true);
		egu.getColumn("chebb_id").setHidden(true);
		
		egu.getColumn("faz").setWidth(75);
		egu.getColumn("daoz").setWidth(75);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("cheb").setWidth(70);
		egu.getColumn("jine").setWidth(80);
		egu.addPaging(0);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));
		
		
		//Toolbar ��ʾ
		
//		egu.addToolbarItem("{"+new GridButton("ɾ���Ѻ˶Գ�Ƥ","function(){ " +
////				"if(gridDiv_yihd_sm.hasSelection()){ " +
////				" }else{ "+
////				" 	alert('��ѡ��һ�Ž��㵥!');} "+
//				"}").getScript()+"}");
		egu.addToolbarButton("ɾ���Ѻ˶Գ�Ƥ",GridButton.ButtonType_SubmitSel, "DeleteButton");
		egu.addTbarText("-");
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setChecked(true);
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("�Զ�ѡ����ͬ��Ƥ");

		egu.addOtherScript("gridDiv_yihd_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
//		����TabPanl�л�ʱ�ٻ�Grid���������Ȳ���ʾ���Grid��
		egu.setDefaultRender(false);
		
		egu.addOtherScript("gridDiv_yihd_grid.on('rowclick',function(own,row,e){	\n"
						+ " if(gridDiv_yihd_sm.isSelected(row)){	\n"
						+ " 	var rec = gridDiv_yihd_ds.getAt(row);	\n"
						+ " 	var biaoz=0,length=0,jine=0,dcbiaoz=0;	\n" 
						+ " 	var fahdw='',meikdw='',faz='',daoz='',cheb='',cheph='',strtmp='';	\n" 
						+ " 	fahdw=rec.get('FAHDW');	\n"
						+ " 	meikdw=rec.get('MEIKDWMC');	\n"
						+ " 	faz=rec.get('FAZ');	\n"
						+ " 	daoz=rec.get('DAOZ');	\n"
						+ " 	cheb=rec.get('CHEB');	\n"
						+ " 	cheph=rec.get('CHEPH');	\n"
						+ " 	dcbiaoz=rec.get('BIAOZ');	\n"
//						+ " 	lic=rec.get('LIC');	\n"	
						+ " 	document.getElementById('EditTableRow').value=row;	\n"	

						+ " 	setyihdBiaozhpmx(faz,daoz,cheb,dcbiaoz);	\n"	 			
						+ " 	if(SelectLike.checked){	\n" 
						+ " 		for(var i=0	;i<gridDiv_yihd_ds.getCount();i++){	\n"
						+ " 			if(gridDiv_yihd_ds.getAt(i).get('FAHDW')==fahdw && gridDiv_yihd_ds.getAt(i).get('MEIKDWMC')==meikdw 	\n" 
						+ " 				&& gridDiv_yihd_ds.getAt(i).get('FAZ')==faz && gridDiv_yihd_ds.getAt(i).get('DAOZ')==daoz	\n" 
						+ " 				&& gridDiv_yihd_ds.getAt(i).get('CHEB')==cheb && gridDiv_yihd_ds.getAt(i).get('BIAOZ')==dcbiaoz){	\n" 
						+ " 				if(strtmp==' '){	\n" 
 						+ "						strtmp=i;	\n"
 						+ " 				}else{	\n"
						+ " 					strtmp=strtmp+','+i;	\n"
						+ " 				}	\n"
						+ " 			}	\n"
						+ " 		}	\n"
						+ " 		gridDiv_yihd_sm.selectRows(strtmp.split(','));	\n"
						+ " 	}	\n"
						+ " 	rec = gridDiv_yihd_grid.getSelectionModel().getSelections();	\n"
						+ " 	length=rec.length;	\n"
						+ " 	for(var i=0;i<rec.length;i++){	\n" 
						+ " 		if(0!=rec[i].get('ID')){	\n"	
						+ " 			biaoz+=eval(rec[i].get('BIAOZ'));	\n" 
						+ " 			jine+=eval(rec[i].get('JINE'));	\n"	
						+ " 		}else{	\n"
						+ " 			length--;	\n"	
						+ " 		}	\n"
						+ " 	}	\n"	
						+ " Hej_ches.setValue(length);	\n"
						+ " Hej_biaoz.setValue(Math.round(biaoz*100)/100);	\n"
						+ " Hej_jine.setValue(Math.round(jine*100)/100);	\n"					
//						+ " gridDiv_yihd_ds.getAt(gridDiv_yihd_ds.getCount()-1).set('CHEPH',length);	\n"	
//						+ " gridDiv_yihd_ds.getAt(gridDiv_yihd_ds.getCount()-1).set('BIAOZ',biaoz);	\n"	
//						+ " gridDiv_yihd_ds.getAt(gridDiv_yihd_ds.getCount()-1).set('JINE',jine);	\n"		
						+ " }});");
						
//						�������½Ǳ�׼��Ʊ����ϸ��Ϣ��
						egu.addOtherScript("RG_HEDFS_DP.on('focus',function(){	\n" 
								+ "	\tdocument.getElementById('DPORLP').value='dp';});	\n" 
								+ "	RG_HEDFS_LP.on('focus',function(){	\n"
								+ "	\tdocument.getElementById('DPORLP').value='lp';});	\n" 
								+ "	function setyihdBiaozhpmx(faz,daoz,cheb,dcbiaoz){	\n" 
								+ "	\tfor(var i=0;i<Biaozhpfymx.length;i++){  \n"	
								+ " \t\tif(faz==Biaozhpfymx[i][2]	\n"
					 			+ " \t\t&&daoz==Biaozhpfymx[i][3]	\n"
					 			+ " \t\t&&cheb==Biaozhpfymx[i][1]	\n" 
					 			+ " \t\t&&dcbiaoz==Biaozhpfymx[i][0]){	\n"	 		
				 				+ " \t\t\tdocument.getElementById('BIAOZHP').value=gridDiv_yihd_ds.getAt(i).get('FEIYMX');	\n"	
				 				+ "	\t\t\tdocument.getElementById('BIAOZHP_TEXT').value=gridDiv_yihd_ds.getAt(i).get('FEIYMX');	\n"
				 				+ "		return;	\n"
				 				+ " }else{	\n" 
				 				+ "	\t\t\tdocument.getElementById('BIAOZHP').value='';	\n"
				 				+ "	\t\t\tdocument.getElementById('BIAOZHP_TEXT').value='';	\n"
				 				+ "}}}"	
				 				
				 				+ "	gridDiv_yihd_grid.on('click',function(){	\n"
								+ " 	reCountToolbarNum(this);		\n"
								+ "	});");
				
//		egu.addOtherScript("function gridDiv_yihd_save(rec){" +
//				"	if(rec.get('FAHDW')=='�ϼ�'){" +
//				"	return 'continue';" +
//				"}}");
		
		setExtGrid_Yihd(egu);
		con.Close();
	}
	
	public void getFenzSelectData(){
		
		String sql = "";
		JDBCcon con = new JDBCcon();
		
		sql=" select decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl) as biaoz,	\n"
			+ " decode(cp.chebb_id,1,'·��',2,'�Ա���',3,'����',4,'��') as cheb,count(cp.id) as ches		\n"
			+ " from chepb cp,fahb f,(select yz.* from yunfjszlb yz,fahb fh,chepb cp where yz.chepb_id=cp.id 	\n" 
			+ "  	and cp.fahb_id=fh.id and yz.diancxxb_id=fh.diancxxb_id and feiylbb_id="+((Visit) getPage().getVisit()).getLong2()
			+ "		and fh.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")) yfzl	\n"
			+ " where cp.fahb_id=f.id and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+") and cp.id=yfzl.chepb_id(+) "
			+ " group by decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl),cp.chebb_id order by cp.chebb_id ";
		
		ResultSetList rsl=con.getResultSetList(sql);//δ�˶�
		ExtGridUtil egu = new ExtGridUtil("gridDiv_fenz", rsl);//�趨��¼����Ӧ�ı�
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("cheb").setHeader("����");
		egu.getColumn("ches").setHeader("����");

		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("cheb").setWidth(80);
		egu.addPaging(10);
		setExtGrid_Fenz(egu);
		con.Close();
	}

	public void getBiaozhpArray(String meikxxb_id,long faz_id,long feiylbb_id,long diancxxb_id) {
		// TODO �Զ����ɷ������
//		���ر�׼��Ʊ�е�Ʊ�ء����𡢷�վ����վ����̡����
		JDBCcon con=new JDBCcon();
		String Strtmp="";
		StringBuffer BiaozhpfymxArrayScript = new StringBuffer();
		try{
			int i=0,j=0;
			String sql="";
//			��վ����վ������Ʊ�ء���̡���� ��Ʊ��������ֵ����Ʊ����ֵ
			
//			2008-08-06 zsj�޸�ȥ��licb�ж�������͵��ж�
//			2009-03-03 zsj�����������͵��жϣ�Ҫ���ں˶Եķ�վ��վ֮��Ҫ���롰����̡����������
			sql="select bz.id,bz.biaoz,decode(bz.cheb,1,'·��',2,'�Ա���',3,'����',4,'��') as cheb,c.mingc as faz,dz.mingc as daoz,lc.zhi as lic,sum(bzfy.zhi) as jine	\n"
	                + " from biaozhpb bz,biaozfyb bzfy,chezxxb c,licb lc,chezxxb dz,liclxb lclx						\n"
	                + "	where bz.id=bzfy.biaozhpb_id 											\n" 
	                + "		 and bz.faz_id=c.id													\n" 
	                + "      and bz.daoz_id=dz.id and dz.id=lc.daoz_id							\n" 
	                + "      and c.id=lc.faz_id	and bz.faz_id="+faz_id+" 						\n"
	                + "		 and lc.liclxb_id=lclx.id and lclx.mingc='"+Locale.zonglc_jies+"'	\n"
	                + " and bz.meikxxb_id in ("+meikxxb_id+") and bz.feiylbb_id="+feiylbb_id+" 		\n"
	                + " and bz.diancxxb_id="+diancxxb_id+"	   									\n"
	                + " group by bz.id,bz.biaoz,bz.cheb,c.mingc,dz.mingc,lc.zhi ";
			
			ResultSetList rs=con.getResultSetList(sql);
			j=rs.getRows();
			for(i=0;i<j;i++){
				
				if(i==0){
					
					Strtmp="new Array()";                   
				}else{
            	   
					Strtmp+=",new Array()";
				}
			}
			i=0;
			
			sql="";
			
			BiaozhpfymxArrayScript.append("var Biaozhpfymx=new Array("+Strtmp+");");
	     	  
			String mbiaozhpb_id="";
	        String mbiaoz="";
	        String mcheb="";
	        String mfaz="";
	        String mdaoz="";
	        String mlic="";
	        String mjine="";
				
           while(rs.next()){
        	   
        	   mbiaozhpb_id=rs.getString("id");
        	   mbiaoz=rs.getString("biaoz");
        	   mcheb=rs.getString("cheb");
        	   mfaz=rs.getString("faz");
        	   mdaoz=rs.getString("daoz");
        	   mlic=rs.getString("lic");
        	   mjine=rs.getString("jine");
               
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][0] ='"+mbiaoz+"';");
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][1] ='"+mcheb+"';");
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][2] ='"+mfaz+"';");
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][3] ='"+mdaoz+"';");
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][4] ='"+mlic+"';");
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][5] ='"+mjine+"';");
               BiaozhpfymxArrayScript.append("Biaozhpfymx["+i+"][6] ='��վ��"+mfaz+"   ��վ��"+mdaoz+"   ��̣�"+mlic+"   Ʊ�أ�"+mbiaoz+"   ����"+mcheb+"	\\n\\n\\n"+getBiaozhpFymx(mbiaozhpb_id)+"';");
               i++;
           }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		((Visit) this.getPage().getVisit()).setString9(BiaozhpfymxArrayScript.toString());
	}

	private String getBiaozhpFymx(String mbiaozhpb_id) {
		// TODO �Զ����ɷ������
		JDBCcon con=new JDBCcon();
		String sql="",feiymx="";
		int i=0;
		try{
			
			sql="select fm.mingc,bzfy.zhi from biaozfyb bzfy,feiyxmb fx,feiymcb fm \n"
		       + " where bzfy.feiyxmb_id=fx.id and fx.feiymcb_id=fm.id	\n" 
		       + " and bzfy.biaozhpb_id="+mbiaozhpb_id+" \n"
		       + " order by fm.mingc ";
			
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				if(feiymx.equals("")){
					
					feiymx=rs.getString("mingc")+":"+rs.getString("zhi");
				}else if(i%4==0&&i!=0){
					
					feiymx+="\\n\\n";
					feiymx+=rs.getString("mingc")+":"+rs.getString("zhi");
				}else{
					
					feiymx+="   "+rs.getString("mingc")+":"+rs.getString("zhi");
				}
				i++;
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return feiymx;
	}

	//δ�˶�
	public ExtGridUtil getExtGrid_Weihd() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid_Weihd(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript_Weihd() {
		return getExtGrid_Weihd().getGridScript();
	}

	public String getGridHtml_Weihd() {
		return getExtGrid_Weihd().getHtml();
	}
	//
	
	//�Ѻ˶�
	public ExtGridUtil getExtGrid_Yihd() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid_Yihd(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript_Yihd() {
		return getExtGrid_Yihd().getGridScript();
	}

	public String getGridHtml_Yihd() {
		return getExtGrid_Yihd().getHtml();
	}
	//
	
	//����
	public ExtGridUtil getExtGrid_Fenz() {
		return ((Visit) this.getPage().getVisit()).getExtGrid3();
	}

	public void setExtGrid_Fenz(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid3(extgrid);
	}

	public String getGridScript_Fenz() {
		return getExtGrid_Fenz().getGridScript();
	}

	public String getGridHtml_Fenz() {
		return getExtGrid_Fenz().getHtml();
	}
	//
	
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
	
	//����
	public IDropDownBean getChebValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIChebModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setChebValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public IPropertySelectionModel getIChebModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getIChebModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setIChebModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}
	
	public void setEditTableRow(int value){
		
		((Visit) this.getPage().getVisit()).setInt1(value);
	}

	public void getIChebModels() {

		List list = new ArrayList();
		list.add(new IDropDownBean(1, "·��"));
		list.add(new IDropDownBean(2, "�Ա���"));
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(list));
	}
	//

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(-1);		//EditTableRow
			visit.setInt2(0);		//����
			visit.setInt3(0);		//��ʼ����׼���ñ��¼��
			visit.setExtGrid1(null);//δ�˶�
			visit.setExtGrid2(null);//�Ѻ˶�
			visit.setExtGrid3(null);//����
			visit.setString1("");	//change
			if(visit.getLong1()==323){//��Ȫ�糧Ĭ��Ϊ��Ʊ
				visit.setString2("lp");	//��Ʊor��Ʊ
			}else{
			  visit.setString2("");	//��Ʊor��Ʊ
			}
			visit.setString3("");	//��׼��Ʊ������ҳ�����½ǣ�
			visit.setString9("");	//���ҳ���׼��Ʊ�����飨�����ڱ�ҳ��
			visit.setString11("");	//Danjcpb�е�JIFZL�ֶε�ֵ������Ʊ����ʱ������¼������Ʊ����Ϣ����Ʊʱ����
			
			setIChebModel(null);
			setChebValue(null);
			getIChebModels();
			
		}
//		��Ҫ��ǰҳ��ȡֵ	diancxxb_id��lie_id��feiylbb_id��meikxxb_id��faz_id.daoz_id,fahrq,yansbh
		getBiaozhpArray(visit.getString12(),visit.getLong4(),visit.getLong2(),visit.getLong1());	//meikxxb_id,faz_id,feiylbb_id,diancxxb_id
		getWeihdSelectData();
		getYihdSelectData();
		getFenzSelectData();
		setEditTableRow(-1);
	}
	
}