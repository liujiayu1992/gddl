package com.zhiren.webservice.dtgsinterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;


public class InterFace_dtgs { 
	
	private static final String error000="1,000,���ճɹ�";
//	private static final String error001="-1,001,XMLData����ת��Ϊgb312����ʱ����";
//	private static final String error002="-1,002,�ĵ�������dom�淶,�����Ƿ������ñ���sql���ֶ�Ϊ����ʱû��д����";
	private static final String error003="-1,003,�����쳣�����鷢�ͽ��յ����ã����緢�����ݵ�����ʱ�����ͱ����ø�ʽ�ַ��������ն˱��������ֶε����õȻ�Զ�����ݿ�����ظ������ݿ�����ʧ��";//
//	private static final String error004="-1,004,Զ��ִ��sqlʱ���ݿ�����ʧ��";
	
	private static final String error005="-1,005,Զ��ִ��sqlʱ���������ǿͻ������ɵ�sql��䲻���Ϲ淶�����鷢�����ñ�";
	
	private static final String error007="1,007,ɾ��Զ������ʱû���ҵ������ֶ����ƣ�������Զ�̽�������û��������������!";//ɾ�����ɹ�ʱ���´β���ִ��
	private static final String error008="1,008,����0�����ݣ�Ҳ����Զ�������뱾������û��ͬ��";
	
//	private static final String error009="-1,009,�û����Ʋ��ڣ���ĵ糧��û���ڼ���ע���û�";
//	private static final String error010="-1,010,�û��˻��������������ϵͳ��������";
	private static final String error011="-1,011,�������ݿ�Ľ��������в�ʶ�������";
	private static final String error013="-1,013,���Ͷ�����ն����õ��ֶθ�����һ�¡�";
	private static final String error014="-1,014,����ɾ��ʱ��δ֪�쳣��";
	
	
//	private static final String rizPath="d:/�������ݲ鿴";// ������־·��
//	private static final String error006="-1,006,Զ��webservice�������δ֪���⣬������webservice����ʧ�ܡ���";
//	private static final String error101="-1,101,��������ʧ�ܣ���url���ܶ�λ������";
//	private static final String error102="-1,102,�糧���ݿ��������������⣡";
	private static final String error103="-1,103,����������sql������";//
//	private static final String error104="-1,104,����web������ʧ�ܣ�";
	

	public void getRenw(String strRenwmc){
		
		JDBCcon con = new JDBCcon();
		DBconn dbcn = null;
		ResultSetList rwrs=null;
		ResultSetList dcdatarsl = null;
		
		String mdiancxxb_id = "", mdiancbm="", mjitdcid = "", mhostname = "", msid = "", mduank = "", myonghm = "", mmim = "";

		String renwmc="",renwlx="",renwbs="",shujjl="",tiaoj="",id0="",renwrq="";
		
		String message="";
		
		List updatesql = new ArrayList();
		
		String dccnsql = "select dc.bianm, pz.diancxxb_id,pz.ip,pz.sid,pz.duank,pz.yonghm,pz.mim,pz.jitdcid from diancxtpz pz,diancxxb dc where dc.id=pz.jitdcid and pz.jiekcszt=1 ";
		
		ResultSetList dcconrs = con.getResultSetList(dccnsql);
		try{
			while(dcconrs.next()){
				
				mdiancxxb_id = dcconrs.getString("diancxxb_id");
				mdiancbm = dcconrs.getString("bianm");
				mjitdcid = dcconrs.getString("jitdcid");
				mhostname = dcconrs.getString("ip");
				msid = dcconrs.getString("sid");
				mduank = dcconrs.getString("duank");
				myonghm = dcconrs.getString("yonghm");
				mmim = dcconrs.getString("mim");
				
//				ʹ�ü���ͳһ�ĵ糧ID
				mdiancxxb_id = mjitdcid;
				
				dbcn=new DBconn(mhostname,mduank,msid);
			    dbcn.setUserName(myonghm);
			    dbcn.setPassword(mmim);
			    
			    String sql = "select j.id id0,j.renwmc,to_char(renwsj,'yyyy-mm-dd hh24:mi:ss') as renwrq,j.renllx, j.renwbs,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj \n"
						+"  from jiekrwb j,jiekfspzb p \n"
						+ "where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.renwmc='"+strRenwmc+"' and j.mingllx=p.mingllx order by id0";
				
			    rwrs=dbcn.getResultSetList(sql);
				
				while(rwrs.next()){//a
					renwmc=rwrs.getString("renwmc");
					renwrq = rwrs.getString("renwrq");
					renwlx=rwrs.getString("renllx");
					renwbs=rwrs.getString("renwbs");//����Ψһ�����ʶid
					id0=rwrs.getString("id0");//����id
					shujjl=rwrs.getString("shujjl");
					tiaoj=rwrs.getString("tiaoj");
					shujjl = shujjl.replaceAll("%%", tiaoj);

					if(renwlx.equals("2")){//��Ϊ����ǰ��Ҫɾ���������޸���������һ���Ķ�����ɾ��������
				    	renwlx="0";
				    }
					dcdatarsl = dbcn.getResultSetList(shujjl);//ȡ����������
					if(dcdatarsl.next()){
						dcdatarsl.beforefirst();
						message = insertData(mdiancxxb_id,dcdatarsl,renwmc,renwbs,renwlx,renwrq);
					}else{
						message = error103;
					}
					Xierz(dbcn,message,id0);
				}
				rwrs.close();
				
//			���ݸ�������
				StringBuffer sb=new StringBuffer();
				updatesql=CreateSql(dbcn,sb,renwmc,mdiancxxb_id);
				for (int ii=0;ii<updatesql.size();ii++){
					int flag = con.getUpdate(updatesql.get(ii).toString());//д��־
					if(flag==-1){
						 message=error005;
					 }if(flag==0){
						 message=error008;
					 }else{
						 message=error000;
					 }
					 Xierzexe(dbcn,message,sb.toString());
				}
			}
			dcconrs.close();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			message=error103;
			e.printStackTrace();
		}finally{
			
			con.Close();
			if(!message.equals("")){
				Xierz(dbcn,message,id0);
			}
			dbcn.close();
		}
	}
	
	private String insertData(String diancxxb_id,ResultSetList dcrsl,String renwmc,String renwbs,String renwlx,String renwrq){

		String message=error000;
		JDBCcon con=new JDBCcon();
		
		StringBuffer bufferTable=new StringBuffer();
		StringBuffer bufferValus=new StringBuffer();
		
		try{
		    String sql = "select renwmc,jiekjspzb.zidmc,leix,jiekbmzhpzb.zidmc zhuanmsql,zhujmc,weik\n"
					+ "from jiekjspzb,jiekbmzhpzb\n"
					+ "where  jiekjspzb.bianm=jiekbmzhpzb.bianm(+) and renwmc='"+renwmc+"' order by jiekjspzb.id";
		    ResultSetList rs=con.getResultSetList(sql);
			if(renwlx.equals("0")){//0:����1:ɾ�� ���Ϊ���Ӳ���
				//���룺1,���ɱ�ͷ(id,mingc...)2,��ȡ���ݼ�¼��֯��valus3,�γɲ���sql
				//��ͷ�ڶ�������Ϣʱ˳������
				//��ɾ��������
				//����ʱʹ������ɾ��ʱ��ʹ������
				
				con.setAutoCommit(false);
				sql = "delete from "+renwmc+" where "+rs.getString(0,"zhujmc")+"='"+diancxxb_id+renwbs+"'";
				con.getDelete(sql);
				
				bufferTable.append("(");
				int kk=0;
				while(rs.next()){
					if(kk==0){
						bufferTable.append(rs.getString("zidmc"));
					}else{
						bufferTable.append(","+rs.getString("zidmc")); 
					}
					kk++;
				}
				bufferTable.append(")");
				if( dcrsl.getColumnCount()!=rs.getRows()){//���Ͷ�����ն��ֶ��������ò�һ��
					 message=error013;
					 return message;
				}
				
				for (int i = 0; dcrsl.next(); i++) {//���ݼ�¼(һ��ֻ��һ��)
					bufferValus.setLength(0);
					bufferValus.append("(");
					for(int k=0;k<dcrsl.getColumnCount();k++){//2����values,��֤���ݣ�������֤�Ϳ���֤��
						//�����ֶ����õ����ʹ�����Ӧ�ֶ�����
						String leix = rs.getString(k,"leix");
						if(k>0){
							bufferValus.append(",");
						}
						//���Խ��б�����֤�Ϳ���֤
						String str= jiaoy(con,rs.getString(k,"zidmc"),rs.getString(k,"zhuanmSql"),rs.getString(k,"weik"),dcrsl.getString(k),leix);// �ֶ����ơ��ֶα��롢�ֶ�Ϊ��
						if(!str.equals("")){
							message="-1,012,"+str+"";
							return message;
						}
						if("varchar".equals(leix)||"number".equals(leix)||"id".equals(leix)){
							bufferValus.append("'"+dcrsl.getString(k)+"'");
						}else if("bianm".equals(leix)){
							bufferValus.append("("+rs.getString(k,"zhuanmSql")+"'"+dcrsl.getString(k)+"')");
						}else if("date".equals(leix)){
							bufferValus.append("to_date('"+dcrsl.getString(k)+"','YYYY-MM-DD')");
						}else if("time".equals(leix)){
							bufferValus.append("to_date('"+dcrsl.getString(k)+"','YYYY-MM-DD HH24:MI:SS')");
						}else if("decode".equals(leix)){//varchar,nubmer
							bufferValus.append("("+rs.getString(k,"zhuanmSql").replaceAll("%%", "'"+dcrsl.getString(k)+"'")+")");//�滻����
						}else{//��ʶ�������
							message=error011+":"+leix;
							return message;
						}
					}
					bufferValus.append(")");
					
					sql="insert into "+renwmc+bufferTable.toString()+" values \n"+bufferValus.toString();
//						System.out.println(sql); 
					int flag=con.getInsert(sql);
					if(flag==-1){
						throw new SQLException();
					}
				}
				con.commit();
					
			}else {
				//ɾ���������γ�ɾ��sql
				 sql=
					"delete from "+renwmc+" where "+rs.getString(0,"zhujmc")+"="+diancxxb_id+renwbs;
				 int count=con.getDelete(sql);
				 if(count==-1){//"-1,003,"+str+""ɾ��"+count+"������
					 message=error007;
				 }if(count==0){
					 message=error008;
				 }else{
					 message=error000;
				 }
				 return message;
			}

		}catch(SQLException e2){
			con.rollBack();
			message=error003;//��
			return message;
		}catch(Exception e3){
			con.rollBack();
			message=error014;//��
		}
		finally{
			  
			  Xiejsrzb(con,"",renwmc,renwbs,message,renwlx,renwrq,diancxxb_id);
			  con.Close();
		}
		return message;

	}
	
	private String jiaoy(JDBCcon cn,String zidmc,String zhuanmsql,String zidwk,String data,String leix) throws SQLException{//�Ƚ��п���֤�ٽ��б�����֤
		String resultStr="";
		if((zidwk.equals("")||zidwk==null)&&(data.equals("")||data==null)){//���������Ϊ��,���ݻ�Ϊ���򷵻ش���
			resultStr=zidmc+"������Ϊ��";
			return resultStr;
		}
//		JDBCcon con=new JDBCcon();
		try{
			if(!zhuanmsql.equals("")&&!data.equals("")&&data!=null){//�������ת�룬����ת������
				if("bianm".equals(leix)){//
					ResultSetList rs=cn.getResultSetList(zhuanmsql+"'"+data+"'");
					if(!rs.next()){
						resultStr=zidmc+"�ֶε�"+data+"���벻��ʶ��";
						return resultStr;
					}
				}else{//decode
					if(zidwk.equals("")){//������Ǳ������У��
						ResultSetList rs=cn.getResultSetList(zhuanmsql.replaceAll("%%", "'"+data+"'"));
						if(rs.next()){
							if(rs.getString(0)==null||rs.getString(0).equals("")){
								resultStr=zidmc+"�ֶε�"+data+"�����ݿ����Ҳ�����Ӧ��ֵ��Ҳ����˵��Զ�����ݿ������տ��Ϊ��";
							}
							return resultStr;
						}
					}
				}
			}
		}
		finally{
//			 con.Close();
		}
		return resultStr;
	}

	private void Xiejsrzb(JDBCcon cn,String zhangh_id,String renw,String renwbs,String message,String caoz,String renwrq,String diancxxb_id){//д������־��
		String[]dat=message.split(",");
//		if(dat[0].equals("-1")){//���Ϊ����
//			JDBCcon con=new JDBCcon();
//			System.out.print(renwrq);
			String sql = "insert into jiekjsrzb(id,diancxxb_id,renw,renwbs,shij,cuowdm,cuowxx,caoz,zhixzt,riq)values(\n"
				+ "xl_diancxxb_id.nextval,"+diancxxb_id+",'"+renw+"',"+renwbs+",sysdate,'"+dat[1]+"','"+dat[2]+"','"+caoz+"',"+dat[0]+",to_date('"+renwrq+"','yyyy-mm-dd hh24:mi:ss'))";
			cn.getInsert(sql);
//			cn.Close();
//		}
	}
	
	private void Xierz(DBconn dbcn,String data,String id0){//д��־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
//		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		
		"where id='"+id0+"'";
		dbcn.getUpdate(sql);
//		con.Close();
	}
	
	private void Xierzexe(DBconn dbcn,String data,String in){//ִ����־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
//		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		"where id in("+in+")";
		dbcn.getUpdate(sql);
//		con.Close();
	}
	
	private   List CreateSql(DBconn dbcn,StringBuffer id0s,String renwmc,String diancid){
		List resultSql=new ArrayList();
//		JDBCcon con=new JDBCcon();
		String ids="";
		String sql = "select j.id,p.renwsql biaom,p.gengxy,j.minglcs gengxyz,p.renwbs,j.renwbs renwbsz\n"
					+ "  from jiekrwb j,jiekfspzb p\n"
					+ " where j.renwmc=p.renwmc and j.mingllx=p.mingllx and j.renllx=3 and zhixzt=0 and j.renwmc='"+renwmc+"'"
					+ " order by biaom,gengxy,gengxyz,renwbs";
		ResultSet rs=dbcn.getResultSet(sql);
		String biaom="",gengxy="",gengxyz="",renwbs="",renwbsz="";
		String biaom_p="",gengxy_p="",gengxyz_p="",renwbs_p="";
		String in="(";
		int kk=0;
		try{
			while(rs.next()){
				ids=ids+rs.getString("id")+",";
				biaom=rs.getString("biaom");
				gengxy=rs.getString("gengxy");
				gengxyz=rs.getString("gengxyz");
				renwbs=rs.getString("renwbs");
				renwbsz=rs.getString("renwbsz");
				if(kk==0||(biaom.equals(biaom_p)&&gengxy.equals(gengxy_p)&&gengxyz.equals(gengxyz_p)&&renwbs.equals(renwbs_p))){//�����ǰֵ��һ����Ȼ��һ��ֵ��˵��Ϊͬһ������
					//׷��in��ģ���������
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					
					if(kk==0){
						in+=diancid+renwbsz;
					}else{
						in+=","+diancid+renwbsz;
					}
					kk++;
				}else{ //����������һ���·��顣
					in+=")";
					resultSql.add("update "+biaom_p+" set "+gengxy_p+"'"+gengxyz_p+"' where "+renwbs_p+in);
					//��ʼ����������
					kk=1;
					in="(";
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					in+=diancid+renwbsz;
				}
			}
			//���һ������
			if(kk!=0){
				in+=")";
				resultSql.add("update "+biaom+" set "+gengxy+"'"+gengxyz+"' where "+renwbs+in);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			con.Close();
		}
		if(!ids.equals("")){
			id0s.append(ids.substring(0, ids.lastIndexOf(",")));
		}
		return resultSql;
	}
	
	
}