 /*
 * �������� 2008-4-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.shihs.hesgl;
/**
 * @author Admini_ator
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.*;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;

import bsh.*;

public class Balances_Shih {
	
	
	Balances_variable_Shih bsv=null;
	private IPropertySelectionModel _ZhibModel;
	
	public Balances_variable_Shih getBsv(){
		
		return bsv;
	}
	
	public void setBsv(Balances_variable_Shih value){
		
		bsv=value;
	}
	
	public Balances_variable_Shih getBalanceData(String SelIds,long Diancxxb_id,long Jieslx,double Jieskdl) throws Exception{
//		������
//			SelIds			ѡ���shihcpb��id
//			Diancxxb_id		�糧��Ϣ��id
//			Jieslx			��������id
//			Jieskdl			����۶���
		
		bsv.setIsError(true);
		bsv.setErroInfo("");
		bsv.setJieslx(Jieslx);
		bsv.setDiancxxb_id(Diancxxb_id);
		
		if(Jieslx==Locale.shihsjs_feiylbb_id){
//			��Ʊ���㡢ú�����
			
			if (getBaseInfo(SelIds,Diancxxb_id,Jieskdl)){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ��Ҫ������������һ��Ҫ���
				
			}else{
				
				bsv.getErroInfo();
				return bsv;
			}
			
//			�õ���ʽ��
			if(Jieslx==Locale.shihsjs_feiylbb_id){
//				ʯ��ʯ����
				if (getGongsInfo(Diancxxb_id,"SHIH")) {
					
				}else{
					//return ErroInfo;
					return bsv;
				}
			}
			
//			��ú��,��ͬ�е����ֵ
			if(getMeiPrice(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,SelIds,
						Jieskdl)){
				
			}else{
				
				bsv.getErroInfo();
				return bsv;
			}
		}
//		else{	//�˷ѽ��㡢���������˷�
//			
//			if (getBaseInfo(SelIds,Diancxxb_id,Jieskdl)){
////				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
//				
//			}else{
//				bsv.getErroInfo();
//				return bsv;
//			}
//			
//			getJiesszl(SelIds,Diancxxb_id,bsv.getHetb_Id(),Jieskdl,Jieslx);
//			
//			
////			�õ��˷ѹ�ʽ
//			if (getGongsInfo(Diancxxb_id,"YF")) {
//				
//			}else{
//				//return ErroInfo;
//				return bsv;
//			}
//			
////			���˷�
//			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
//				
//			}else{
//				bsv.getErroInfo();
//				return bsv;
//			}
//		}
		
//		computYunfAndHej();

		bsv.setIsError(false);
		
		return bsv;
	}
	
	//�õ�ϵͳ��Ϣ���˷�˰�ʣ�ú��˰�ʣ���ʽ��
	private boolean getGongsInfo(long _Diancxxb_id,String _Type) throws Exception{
//		����˵����_diancxx_id���糧��ʽ
//				 _Type,		  ���Ϊ"MK"�Ǿ���ú�ʽ�����Ϊ"YF"�Ǿ����˷ѹ�ʽ,���Ϊ"ALL"�Ǿ�����Ʊ����
//		JDBCcon con =new JDBCcon();
//	    try {
//            
//            //ú����㹫ʽ
//	    	ResultSet rs= con.getResultSet("select id from gongsb where mingc='����ú��' and leix='����' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
//            if (rs.next()) {
//            	
//            	DataBassUtil clob=new DataBassUtil();
//            	
//            	bsv.setGongs_Yf(clob.getClob("gongsb", "gongs", rs.getLong(1)));
//            	
//            }else{
//            	bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
//            	return false;
//            }
//            rs.close();
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        	con.Close();
//        }
//
//		return true;
		
		String str_Gongs_Shih="";
		
		if(_Type.equals("SHIH")){
			
			str_Gongs_Shih=Jiesdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Shih);
			
			if(str_Gongs_Shih.equals("")){
				
				bsv.setErroInfo("û�еõ�ʯ��ʯ��ʽ!");
	        	return false;
			}else{
				
				bsv.setGongs_Shih(str_Gongs_Shih);
			}
		}
		
		return true;
	}
    
	
//	�õ�Ҫ����������������Ȼ�����Ϣ
	/**
	 * @param selIds
	 * @param _Diancxxb_id
	 * @return
	 * @throws Exception
	 */
    
	private boolean getBaseInfo(String SelIds,long Diancxxb_id,double Jieskdl) throws Exception{

        JDBCcon con =new JDBCcon();
	    try {
            //�������ڡ��������ڡ����������ء�ӯ�������𡢷��������� from fahb
	        //����������ȡ֤����������ú�ǰ���ȡ��������ú�����ȡ��
	    	
	    	String sql="";
	    		
	    	sql="select max(shcp.shihhtb_id) as hetb_id,\n" +
				"       max(gys.quanc) as gongysqc,max(gys.id) as gongysb_id,\n" + 
				"       max(shcp.shihpzb_id) as pinzb_id,max(pzb.mingc) as pinz,\n" + 
				"       to_char(min(shcp.fahrq),'yyyy-MM-dd') as minfahrq,\n" + 
				"       to_char(max(shcp.fahrq),'yyyy-MM-dd') as maxfahrq,\n" + 
				"       to_char(min(shcp.daohrq),'yyyy-MM-dd') as mindaohrq,\n" + 
				"       to_char(max(shcp.daohrq),'yyyy-MM-dd') as maxdaohrq\n" + 
				"       from shihcpb shcp,shihgysb gys,shihpzb pzb \n" + 
				"            where shcp.gongysb_id=gys.id and shcp.shihpzb_id=pzb.id\n" + 
				"                  and shcp.id in ("+SelIds+")";
           
            ResultSet rs = con.getResultSet(sql);
            
            if (rs.next()) {
            	
            	bsv.setFahksrq(rs.getDate("minfahrq"));
            	bsv.setFahjzrq(rs.getDate("maxfahrq"));
            	if (bsv.getFahksrq().equals(bsv.getFahjzrq())){
            		
            		bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()));
            	}else{
            								
            		bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()) +"��"+ Jiesdcz.FormatDate(bsv.getFahjzrq()));
            	}
            	
            	bsv.setYansksrq(rs.getDate("mindaohrq"));
            	bsv.setYansjsrq(rs.getDate("maxdaohrq"));

            	if (bsv.getYansksrq().equals(bsv.getYansjsrq())){
            		
            		bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq()));
            	}else{
            		
            		bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq())+"��"+Jiesdcz.FormatDate(bsv.getYansjsrq()));
            	}
            	
            	bsv.setHetb_Id(rs.getLong("hetb_id"));
            	bsv.setFahdw(rs.getString("gongysqc"));
                bsv.setGongysb_Id(rs.getLong("gongysb_id"));
            	bsv.setRanlpzb_Id(rs.getLong("pinzb_id"));
            	bsv.setRanlpz(rs.getString("pinz"));
            	
//                bsv.setFaz(rs.getString("faz"));
//                bsv.setFaz_Id(rs.getLong("faz_id"));
//                bsv.setDaoz_Id(rs.getLong("daoz_id"));
                
                bsv.setYuanshr(MainGlobal.getTableCol("diancxxb", "mingc", "id", String.valueOf(Diancxxb_id)));
                bsv.setXianshr(bsv.getYuanshr());
	          	bsv.setTianzdw(MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(Diancxxb_id)));
	          	
	          	bsv.setJiesrq(Jiesdcz.FormatDate(new Date()));	//��������
	          	bsv.setJiesbh(Jiesdcz.getJiesbh_Shih(String.valueOf(Diancxxb_id)));	//������
	          	bsv.setDaibcc(MainGlobal.getShouwch_Cp(SelIds));	
	          	
            }else{
            	bsv.setErroInfo("Ҫ����ĳ�Ƥ��Ϣ�����ڿ����ѱ������û�ɾ��!");
            	return false;
            }
            rs.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	con.Close();
        }

		return true;
	}
	
	private void getJiesszl(String SelIds,long Diancxxb_id,long Hetb_id,double Jieskdl,long Jieslx){
		
//		������������
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
//			����ϵͳ��Ϣ�������õ���Ŀ��
//			1��ʯ��ʯ����������ɷ�ʽ
//			2��ʯ��ʯ��������ȡ����ʽ
//			3��ʯ��ʯ������������С��λ
//			4��ʯ��ʯ��Ȩ����
//			5��ʯ��ʯ��˰���۱���С��λ
			
//			�ر�˵������ͬ���趨�ġ��������㡱�����ȼ�Ҫ����ϵͳ��Ϣ�����趨�ġ�����������ɷ�ʽ��
//				�����ͬ�С��������㡱����Ϊ������������������xitxxb��ȡ����Ĭ��Ϊmaoz-piz-koud
//				�����ͬ�С��������㡱����Ϊ��������������ͽ���������Ϊbiaoz������ȥxitxxb��ȡ��
//				��ΪĿǰû�п�����������ʲ�����
			
			String jies_Jqsl="maoz-piz-koud";				//�����Ȩ����
			String jies_CaOblxs="2";						//����ָ�걣��С��λ
			String jies_MgOblxs="2";
			String jies_Xidblxs="2";
			
			String jies_Jieslqzfs="sum()";					//��������ȡ����ʽ
			String jies_Jsslblxs="2";						//������������С��λ
			String jies_Kdkskzqzfs="sum()";					//�۶֡���ˮ������ȡ����ʽ
			String jies_Jssl="maoz-piz-koud";				//��������
			String jies_yunfjssl="maoz-piz-koud";			//�˷ѽ�������
			
			String jies_Guohlqzfs=Locale.bujxqzcz_jiesghlqzfs_xitxx;	//ϵͳ��Ϣ�й�����ȡ����ʽ��Ĭ��Ϊ��������ȡ������(sum())
			String jies_Guohlblxsw="2";						//ϵͳ��Ϣ�й���������С��λ��Ĭ��Ϊ��2
			String yunsdw="";	//���䵥λ������
			
//			��ϵͳ��Ϣ����ȡ�������õ���Ϣ
			String XitxxArrar[][]=null;	
			XitxxArrar=MainGlobal.getXitxx_items("ʯ��ʯ����",
					"'"+Locale.jiesslzcfs_jies+"'," +		//����������ɷ�ʽ
					"'"+Locale.jiesslqzfs_jies+"'," +		//��������ȡ����ʽ
					"'"+Locale.jiesslblxsw_jies+"'," +		//������������С��λ
					"'"+Locale.jiaqsl_xitxx+"'," +			//��Ȩ����
					"'"+Locale.hansdjblxsw_xitxx+"'"		//��˰���۱���С��λ
					,String.valueOf(Diancxxb_id));
			
//			����ȡ�õ�ֵ��Ȼ��Ա������и�ֵ
			if(XitxxArrar!=null){
				
				for(int i=0;i<XitxxArrar.length;i++){
					
					if(XitxxArrar[i][0]!=null){
						
						if(XitxxArrar[i][0].trim().equals(Locale.jiesslzcfs_jies)){
//							����������ɷ�ʽ
							jies_Jssl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslqzfs_jies)){
//							��������ȡ����ʽ
							jies_Jieslqzfs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslblxsw_jies)){
//							������������С��λ
							jies_Jsslblxs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//							��Ȩ����
							jies_Jqsl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.hansdjblxsw_xitxx)){
//							��˰���۱���С��λ
							bsv.setHansdjblxsw(Integer.parseInt(XitxxArrar[i][1].trim()));
						}
					}
				}
			}
			
			StringBuffer sql=new StringBuffer("");
			
				
			sql.append(" select 				\n");
			sql.append(" 		nvl(koud,0) as koud,nvl(ches,0) as ches,nvl(biaoz,0) as biaoz,nvl(yingk,0) as yingk,	\n");
			sql.append("		nvl(jiessl,0) as jiessl,nvl(jingz,0) as jingz,nvl(yuns,0) as yuns,nvl(cao,0) as cao,		\n");
			sql.append("		nvl(mgo,0) as mgo,nvl(xid,0) as xid,nvl(jiessl-jingz,0) as jiesslcy from 					\n");
			sql.append("		(select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"(maoz-piz-koud)",jies_Guohlblxsw)+" as jingz,						\n");
			sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,	\n");
			sql.append(" 		count(cp.id) as ches, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "(maoz-piz-koud)+yuns-biaoz", jies_Jsslblxs)+" as yingk, 	\n");
			sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
			sql.append("	 --��������   																															\n");
			sql.append("	 	decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*zl.caco3)/sum("+jies_Jqsl+"),2)) as cao,   								\n");
			sql.append("	 	decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*zl.mgco3)/sum("+jies_Jqsl+"),2)) as mgo,   								\n");
			sql.append("	 	decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*zl.xid)/sum("+jies_Jqsl+"),2)) as xid   								\n");
			
			sql.append("	 	from shihcpb cp,shihcyb cy,shihzlb zl										\n");
			sql.append("			where cp.shihcyb_id=cy.id and cy.shihzlb_id=zl.id   					\n");
			sql.append("				and cp.id in ("+SelIds+")) 	\n");
		
			
			rsl=con.getResultSetList(sql.toString());
			if(rsl.next()){
				
//				����
				bsv.setKoud(rsl.getDouble("koud"));					//�۶�
            	bsv.setChes(rsl.getLong("ches"));					//����
            	
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//����
            	bsv.setYingksl(rsl.getDouble("yingk"));				//ӯ��  
            	
            	bsv.setYanssl(rsl.getDouble("jingz"));				//������������
            	bsv.setJingz(rsl.getDouble("jingz"));				//����
            	bsv.setKoud_js(Jieskdl);											//����۶�
            	
            	if(bsv.getShuljs().equals("��������")){
                	
            		bsv.setJiessl(rsl.getDouble("jiessl")-Jieskdl);		//��������
                	
                }else if(bsv.getShuljs().equals("������")){
                	
                	bsv.setJiessl(rsl.getDouble("biaoz")-Jieskdl);		//��������
                }
            	
            	bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl()-bsv.getJingz()),2));	//������������(�������͹������Ĳ�ֵ)
            	
            	bsv.setYuns(rsl.getDouble("yuns"));					//ʵ������
//	        	����ָ��
	        	bsv.setCaO_cf(rsl.getDouble("CaO"));
	            bsv.setMgO_cf(rsl.getDouble("MgO")); 
	            bsv.setXid_cf(rsl.getDouble("xid"));
	            
//              ����ָ��
                
            	bsv.setCaO_js(rsl.getDouble("cao"));
                bsv.setMgO_js(rsl.getDouble("mgo")); 
                bsv.setXid_js(rsl.getDouble("xid"));
                
                if(bsv.getCaO_js()==0){
                	
                	bsv.setErroInfo("û�л������ݣ����飡");
                }
			}
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
	private boolean getMeiPrice(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String SelIds,double Jieskdl){
		//�õ���ͬ��Ϣ�е��˼�
			JDBCcon con =new JDBCcon();
			String sql="";
			Interpreter bsh=new Interpreter();
			Jiesdcz Jscz=new Jiesdcz();
			try{
				
//				��ͬ��Ϣ��������ͬ�������������С������ʺ�
				sql="select ht.hetsl,gys.kaihyh as gongfkhyh,gys.zhangh as zhangh,\n" +
					"       decode(ht.shuljs,0,'��������','��������') as shuljs,\n" + 
					"       decode(ht.zhiljs,0,'��������','��������') as zhiljs\n" + 
					"       from shihhtb ht,shihgysb gys\n" + 
					"       where ht.shihgysb_id=gys.id\n" + 
					"             and ht.id="+Hetb_id+"\n" + 
					"             and ht.diancxxb_id="+Diancxxb_id;
				
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.next()){
					
					bsv.setHetml(rsl.getDouble("hetsl"));
					bsv.setKaihyh(rsl.getString("gongfkhyh"));
					bsv.setZhangh(rsl.getString("gongfzh"));
					bsv.setShuljs(rsl.getString("shuljs"));
					bsv.setZhiljs(rsl.getString("zhiljs"));
				}
				
//				�۸񣨺�ͬ��һ����ͬ��Ӧ��������۸�	
				sql="select zb.bianm as zhib,jg.jij,jg.zuigmj,jg.zuidmj,jijdw.bianm as jijdw,jg.yunj,\n" +
					"       yunjdw.bianm as yunjdw,dwb.bianm as danw,jg.shangx,jg.xiax \n" + 
					"       from shihhtjg jg,danwb jijdw,danwb dwb,danwb yunjdw,zhibb zb\n" + 
					"       where jg.jijdwid=jijdw.id\n" + 
					"             and jg.yunjdw_id=yunjdw.id(+)\n" +
					"			  and jg.zhibb_id=zb.id		\n"+
					"             and jg.danwb_id=dwb.id 	\n"+				
					"             and jg.shihhtb_id="+Hetb_id+"";

				rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
					bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
					bsv.setZuidmj(rsl.getDouble("zuidmj")); 		//���ú��
					bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
					bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
					bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
				
					bsh.set("�۸�λ", bsv.getHetmdjdw());	
					bsh.set("��ͬ�۸�", bsv.getHetmdj());
					bsh.set("��߼۸�", bsv.getZuigmj());
					bsh.set("��ͼ۸�", bsv.getZuidmj());
					
//					��ͬ����ָ��,ȡ�����������ĺ�ͬ����
					if(rsl.getRows()==1){
						
//						��һ����ͬ
//						��ý�������������
						getJiesszl(SelIds,Diancxxb_id,Hetb_id,Jieskdl,Jieslx);
						
						double Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
						Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
						
						bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ+��λ�������λ��ָ���ã�
						
						bsv.setYifzzb(rsl.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
						
						bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
						bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
						
						bsh.set(rsl.getString("zhib")+"������λ", 	"");
						bsh.set(rsl.getString("zhib")+"���۵���", 	0);
						bsh.set(rsl.getString("zhib")+"���ۼ۵�λ", 	"");
						bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"");
						bsh.set(rsl.getString("zhib")+"���ۿ����", 	0);
						bsh.set(rsl.getString("zhib")+"���ۿ������λ", 	"");
						bsh.set(rsl.getString("zhib")+"��׼���ۼ�", 	0);
						
//						������ۿ�
						getZengkk(Hetb_id,bsh);
						
//						��˰���۱���С��λ
						bsh.set(Locale.hansdjblxsw_xitxx, bsv.getHansdjblxsw());	
						
//								ִ�й�ʽ
						bsh.eval(bsv.getGongs_Shih());
						
//								�õ�������ָ��
						setJieszb(bsh,0);
						
//								����ú����
						computData();
						
						bsv.setHetjgpp_Flag(true);
						bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
					
					}else{
//						�ж����ͬ
						
							double	Dbljijzb=0;
	//						��ý�������������
							getJiesszl(SelIds,Diancxxb_id,Hetb_id,Jieskdl,Jieslx);
							
							do{
								
								Dbljijzb=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
								Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
								
								if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
									
									bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
									bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
									bsv.setZuidmj(rsl.getDouble("zuidmj")); 		//���ú��
									bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
									bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
									bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
								
									bsh.set("�۸�λ", bsv.getHetmdjdw());	
									bsh.set("��ͬ�۸�", bsv.getHetmdj());
									bsh.set("��߼۸�", bsv.getZuigmj());
									bsh.set("��ͼ۸�", bsv.getZuidmj());
									
									bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//��ͬ�۸��id
									
									bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
									
									bsv.setYifzzb(rsl.getString("zhib"));			//Ĭ�ϵ��Ѹ�ֵָ��
									
									bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
									bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
									
									bsh.set(rsl.getString("zhib")+"������λ", 	"");
									bsh.set(rsl.getString("zhib")+"���۵���", 	0);
									bsh.set(rsl.getString("zhib")+"���ۼ۵�λ", 	"");
									bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"");
									bsh.set(rsl.getString("zhib")+"���ۿ����", 	0);
									bsh.set(rsl.getString("zhib")+"���ۿ������λ", 	"");
									bsh.set(rsl.getString("zhib")+"��׼���ۼ�", 	0);
									
	//												������ۿ�
									getZengkk(Hetb_id,bsh);
									
	//								��˰���۱���С��λ
									bsh.set(Locale.hansdjblxsw_xitxx, bsv.getHansdjblxsw());	
									
	//												ִ�й�ʽ
									bsh.eval(bsv.getGongs_Shih());
									
	//												�õ�������ָ��
									setJieszb(bsh,0);
									
	//												����ú����
									computData();
									
									bsv.setHetjgpp_Flag(true);
									bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
								}
								
							}while(rsl.next());
					}
				}
				
				if(!bsv.getHetjgpp_Flag()){
					
					bsv.setErroInfo("û�к�ͬ�۸����������ƥ�䣡");
				}
				rsl.close();	
		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    } finally {
		    	con.Close();
		    }
		    
		    return true;
	}
	
	private boolean getZengkk(long Hetb_id,Interpreter bsh){
//		���ۿ�
		JDBCcon con=new JDBCcon();
		try{
			ResultSetList rsl=null;
			
			String sql="select zb.bianm as zhib,tj.bianm as tiaoj,shangx,xiax,dw.bianm as danw,jis,jsdw.bianm as jisdw,\n" +
				"       kouj as zengkj,kjdw.bianm as zengkjdw,jicj as jizzkj\n" + 
				"       from shihhtb ht,shihhtzkk zkk,zhibb zb,tiaojb tj,danwb dw,danwb jsdw,danwb kjdw\n" + 
				"       where ht.id=zkk.shihhtb_id and zkk.zhibb_id=zb.id and zkk.tiaojb_id=tj.id\n" + 
				"             and zkk.danwb_id=dw.id(+) and zkk.jisdwid=jsdw.id(+)\n" + 
				"             and zkk.koujdw=kjdw.id\n" + 
				"             and ht.id="+Hetb_id;

			
			rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//��¼ָ�����ֵ
			String  Strtmp="";		//��¼�趨��ָ��
			while(rsl.next()){
				
				Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
//				ָ��Ľ���ָ��
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					//ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
					
					bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
					bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));			//�����޵�λ
					bsh.set(rsl.getString("zhib")+"���ۿ�����", 	rsl.getString("tiaoj"));		//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
					bsh.set(rsl.getString("zhib")+"���۵���",	rsl.getDouble("zengkj"));		//���ۼ�
					bsh.set(rsl.getString("zhib")+"���ۼ۵�λ", 	rsl.getString("zengkjdw")==null?"":rsl.getString("zengkjdw"));	//���۵�λ
					bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
					bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
					bsh.set(rsl.getString("zhib")+"���ۿ����",	rsl.getDouble("jis"));			//������ÿ����xx�򽵵�xx��
					bsh.set(rsl.getString("zhib")+"���ۿ������λ",	rsl.getString("jisdw"));	//������λ
					bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	rsl.getDouble("jizzkj"));		//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
					
					Strtmp+="'"+rsl.getString("zhib")+"',";					//��¼�û����õ�Ӱ����㵥�۵�ָ��
				}
			}
//			if(Strtmp.equals("")){
//				
//				if(!bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){
//					
//					bsv.setErroInfo("ϵͳ��û�з���Ҫ������ۿ��");
//					return false;
//				}
//				
//			}
				
//			ȡ��zhibb��û����hetzkkb�����ֵ���Ŀ��Ŀ����ҲҪ�ŵ���ʽ��ȥ����
			
			if(!Strtmp.equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ("+Strtmp.substring(0,Strtmp.lastIndexOf(","))+") and leib=2 ";
				
			}else if(!bsv.getYifzzb().equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ('"+bsv.getYifzzb()+"') and leib=2 ";
			}else{
				
				sql="select distinct bianm as zhib from zhibb where leib=2 ";
			}
			
			rsl=con.getResultSetList(sql);
			
			String Strtmpdw="";
			
			while(rsl.next()){
				
					Strtmpdw=Jiesdcz.getZhibbdw(rsl.getString("zhib"),"");
					
					bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//����ֵ
					bsh.set(rsl.getString("zhib")+"������λ", 	Strtmpdw);				//ָ�굥λ
					bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"����");					//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
					bsh.set(rsl.getString("zhib")+"���۵���",	0);						//������
					bsh.set(rsl.getString("zhib")+"���ۼ۵�λ", 	"");					//���۵�λ
					bsh.set(rsl.getString("zhib")+"����", 		0);						//ָ������
					bsh.set(rsl.getString("zhib")+"����", 		0);						//ָ������
					bsh.set(rsl.getString("zhib")+"���ۿ����",	0);						//������ÿ����xx�򽵵�xx��
					bsh.set(rsl.getString("zhib")+"���ۿ������λ",	"");				//������λ
					bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	0);						//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return true;
	}
	
	private long getYunshtb_id(long Hetb_id){
//		�õ�hetys.id
		JDBCcon con= new JDBCcon();
		long lngYunshtb_id=0;
		long lngFinYshtb_id=0;
		
//		���1���������Ʊ���㡢�ȴ�ú���ͬ����ȡ�˷ѡ�
//		���û��ȡ���˷ѣ��ٴ�ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid,
//		�ٸ����˷Ѻ�ͬ�е�ú��ȡ������۸�
		
		try{
//			��getMeiprise�����У��Ѿ���¼�º�ͬ���õ�hetjgb_id
			ResultSet rs=null;
			String sql="select yunj,danwb.bianm as yunjdw from hetjgb,danwb 	\n"
				+" where hetjgb.yunjdw_id=danwb.id and hetjgb.id="+bsv.getHetjgb_id();
			ResultSet rec=con.getResultSet(sql);
			while(rec.next()){
				
				bsv.setHetyj(rec.getDouble("yunj"));
				bsv.setHetyjdw(rec.getString("yunjdw"));
			}
			
			if(bsv.getHetyj()==0){
//				���û��ȡ���˷ѣ��ٴ�ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid
				
				sql="select hetys_id from meikyfhtglb where hetb_id="+Hetb_id;
				rec=con.getResultSet(sql);
				while(rec.next()){
//					�ֶ���˷Ѻ�ͬ
					lngYunshtb_id=rec.getLong("hetys_id");
					sql="select hetys.id from hetysjgb,hetys 	\n"
						+ " where hetys.id=hetysjgb.hetys_id	\n"
						+ " and hetys.id="+lngYunshtb_id+" 		\n"
						+ " and meikxxb_id="+bsv.getGongysb_Id();
					
					rs=con.getResultSet(sql);
					while(rs.next()){
						
						lngFinYshtb_id=lngYunshtb_id;
						return lngFinYshtb_id;
					}
				}
			}
			if(rs!=null){
				
				rs.close();
			}
			if(rec!=null){
				
				rec.close();
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return 0;
	}
	
	private void CountYf(long Hetb_id){
//		�����˷�
//		˼·��
//			���1��ú���ͬ�����˼ۣ�bsv.getHetyj()>0�����˼۵�λ�����֣�Ԫ/�֣�Ԫ/��*���,
//					û�����ۿ����.��Ϊû���õ��˷Ѻ�ͬ�����Բ��������ۿ�
//			���2����Hetyj��Hetyjdw��ֵ,Ҫ��hetysjgb��ȡֵ�������ۿ�
		try{
			
			Interpreter bsh=new Interpreter();
//			�˷Ѻ�˰���۱���С��λ
			if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//				���1
//				ȡhetb�е��˼�
				bsh.set("��ͬ�˼�", bsv.getHetyj());
				bsh.set("��ͬ�˼۵�λ", bsv.getHetyjdw());
//				bsh.set("�˼����", bsv.getYunju_js());
				
				if(!bsv.getGongs_Yf().equals("")){
					
//					if(bsv.getYunfjsl()>0){
					if(true){
						
//						�˷Ѻ�˰���۱���С��λ
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						getZengkk_yf(Hetb_id,bsh);	//�����κ�����ֻ�ǹ�ʽ�ϲ�����
//						�����˷�
						bsh.eval(bsv.getGongs_Yf());
//						bsv.setYunfjsdj(Double.parseDouble(bsh.get("�˷ѽ��㵥��").toString()));
						setJieszb(bsh,1);
						
					}else{
						
						bsv.setErroInfo("�˷ѽ�������Ϊ�㣬������");
						return;
					}
				}else{
					
					bsv.setErroInfo("�������˷Ѽ��㹫ʽ");
					return;
				}
			}else{
				
//				ȡhetys�е��˼�
				JDBCcon con=new JDBCcon();
				String sql=" select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
							+ " yunja,yjdw.bianm as yunjdw	\n" 
							+ " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
							+ " danwb yjdw										\n"
							+ " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id	\n" 
							+ " and jg.tiaojb_id=tj.id(+)						\n"  
							+ " and jg.danwb_id=dw.id(+)						\n" 
							+ " and jg.yunjdw_id=yjdw.id(+)						\n"
							+ " and ht.id="+Hetb_id+" and jg.meikxxb_id="+bsv.getGongysb_Id()+"	";
				
				ResultSetList rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					if(rsl.getRows()==1){
//						��һ����ͬ�۸�
						bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
						bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
//						bsh.set("�˼����", bsv.getYunju_js());
						
//						������ۿ�
						getZengkk_yf(Hetb_id,bsh);
						
//						�˷Ѻ�˰���۱���С��λ
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						bsh.eval(bsv.getGongs_Yf());
						setJieszb(bsh,1);
						
					}else{
//						�����ͬ�۸�
						double shangx=0;
						double xiax=0;
//						double yunju=bsv.getYunju_cf();	//�˾�
						double yunju=0;
						double Dbltmp=0;
						
						do{
							shangx=rsl.getDouble("shangx");
							xiax=rsl.getDouble("xiax");
							
							Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
							
							Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
							
							if(yunju>=xiax&&yunju<=(shangx==0?9999:shangx)){
								
								bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
								bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
//								bsh.set("�˼����", bsv.getYunju_js());
								
								getZengkk_yf(Hetb_id,bsh);
								
//								�˷Ѻ�˰���۱���С��λ
								bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
								
								bsh.eval(bsv.getGongs_Yf());
								setJieszb(bsh,1);
							}
							
						}while(rsl.next());
					}
				}else{
					
					bsv.setErroInfo("û�еõ��˷Ѻ�ͬ�۸�");
					return;
				}
				rsl.close();
				con.Close();
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	private void getZengkk_yf(long Hetb_id,Interpreter bsh){
		
//		�˷ѵ����ۿ�ԭ������˷Ѽ۸����õ��˸�ָ�꣬�����������ۿ����ۼƼ���
		JDBCcon con=new JDBCcon();
		try{
			
			String sql=" select distinct zb.bianm as zhib,tj.bianm as tiaoj,shangx,xiax,dw.bianm as danw,			\n"
				+ " 	jis,jsdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,		\n"
				+ " 	xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,canzsx,canzxx		\n"
				+ " 	from hetys ht,hetyszkkb zkk,zhibb zb,tiaojb tj,danwb dw,danwb jsdw,danwb kjdw,			\n"
				+ " 		danwb zfjdw,zhibb czxm,danwb czxmdw													\n"
				+ " 		where ht.id=zkk.hetys_id and zkk.zhibb_id=zb.id and zkk.tiaojb_id=tj.id				\n"
                + "  			and zkk.danwb_id=dw.id and zkk.jisdwid=jsdw.id(+) and zkk.koujdw=kjdw.id(+)		\n"
                + "  			and zkk.zengfjdw=zfjdw.id(+) and zkk.canzxm=czxm.id(+) and zkk.canzxmdw=czxmdw.id(+)	\n"
                + " 			and ht.id="+Hetb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//��¼ָ�����ֵ
			String  Strtmp="";		//��¼�趨��ָ��
			
			while(rsl.next()){
				
				Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
//				ָ��Ľ���ָ��
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
				
					bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
					bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));			//ָ�굥λ
					bsh.set(rsl.getString("zhib")+"���ۿ�����", 	rsl.getString("tiaoj"));		//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
					bsh.set(rsl.getString("zhib")+"��������",	rsl.getDouble("zengfj"));		//������
					bsh.set(rsl.getString("zhib")+"�۸�����",	rsl.getDouble("kouj"));			//�ۼ�
					bsh.set(rsl.getString("zhib")+"�����۵�λ", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//���۵�λ
					bsh.set(rsl.getString("zhib")+"�۸��۵�λ", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//�ۼ۵�λ
					bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
					bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
					bsh.set(rsl.getString("zhib")+"���ۿ����",	rsl.getDouble("jis"));			//������ÿ����xx�򽵵�xx��
					bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	rsl.getDouble("jizzkj"));		//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
					bsh.set(rsl.getString("zhib")+"С������",	Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
					
					Strtmp+="'"+rsl.getString("zhib")+"',";					//��¼�û����õ�Ӱ���˷ѽ��㵥�۵�ָ��
				}
			}

			if(!Strtmp.equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ("+Strtmp.substring(0,Strtmp.lastIndexOf(","))+") and leib=1 ";
				
			}else{
				
				sql="select distinct bianm as zhib from zhibb where leib=1 ";
			}
			
			rsl=con.getResultSetList(sql);
			
			String Strtmpdw="";
			
			while(rsl.next()){
				
					
					Strtmpdw=Jiesdcz.getZhibbdw(rsl.getString("zhib"),"");
					
					bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//����ֵ
					bsh.set(rsl.getString("zhib")+"������λ", 	Strtmpdw);				//ָ�굥λ
					bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"����");					//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
					bsh.set(rsl.getString("zhib")+"��������",	0);						//������
					bsh.set(rsl.getString("zhib")+"�۸�����",	0);						//�ۼ�
					bsh.set(rsl.getString("zhib")+"�����۵�λ", 	"");					//���۵�λ
					bsh.set(rsl.getString("zhib")+"�۸��۵�λ", 	"");					//�ۼ۵�λ
					bsh.set(rsl.getString("zhib")+"����", 		0);						//ָ������
					bsh.set(rsl.getString("zhib")+"����", 		0);						//ָ������
					bsh.set(rsl.getString("zhib")+"���ۿ����",	0);						//������ÿ����xx�򽵵�xx��
					bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	0);						//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
					bsh.set(rsl.getString("zhib")+"С������",	"");					//С������ÿ����xx�򽵵�xx��
				
			}
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private void setJieszb(Interpreter bsh,int Type){
		
		try {
			
//			Type	0:ú�����
//					1:�˷ѽ���
			if(Type==0||(Type==1&&bsv.getJieslx()==Locale.guotyf_feiylbb_id)){
//				�����ú��������Ʊ�������ú��ʱ���Խ��и�ֵ������ֻ���ڵ��������˷�ʱ�ſɸ�ֵ
				
	//			�������ۿ�ȡֵ
				bsv.setShul_ht(bsh.get("��ͬ��׼_��������").toString());
				bsv.setShul_yk(Double.parseDouble(bsh.get("ӯ��_��������").toString()));
				bsv.setShul_zdj(Double.parseDouble(bsh.get("�۵���_��������").toString()));
				
	//			CaO
				bsv.setCaO_ht(bsh.get("��ͬ��׼_CaO").toString());
				bsv.setCaO_yk(Double.parseDouble(bsh.get("ӯ��_CaO").toString()));
				bsv.setCaO_zdj(Double.parseDouble(bsh.get("�۵���_CaO").toString()));
				
	//			MgO
				bsv.setMgO_ht(bsh.get("��ͬ��׼_MgO").toString());
				bsv.setMgO_yk(Double.parseDouble(bsh.get("ӯ��_MgO").toString()));
				bsv.setMgO_zdj(Double.parseDouble(bsh.get("�۵���_MgO").toString()));
				
	//			ϸ��
				bsv.setXid_ht(bsh.get("��ͬ��׼_ϸ��").toString());
				bsv.setXid_yk(Double.parseDouble(bsh.get("ӯ��_ϸ��").toString()));
				bsv.setXid_zdj(Double.parseDouble(bsh.get("�۵���_ϸ��").toString()));
			}
			//���㵥��
			if(Type==0){
				
				bsv.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));
			}else if(Type==1){
				
				if(bsv.getJieslx()==Locale.guotyf_feiylbb_id){
					
				    bsv.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));
				}
				bsv.setYunfjsdj(Double.parseDouble(bsh.get("����۸�").toString()));
			}
			
		} catch (EvalError e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}
	
	//������ü�Ȩ
	private void computData(){
		//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
		//ú��
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();	//ú���������
		double _Meiksl=bsv.getMeiksl();
		
		//�˷�
//		double _Tielyf=bsv.getTielyf();
//		double _Tielzf=bsv.getTielzf();
//		double _Yunfsl=bsv.getYunfsl();
//		double _Kuangqyf=bsv.getKuangqyf();
//		double _Kuangqzf=bsv.getKuangqzf();
//		double _Kuangqsk=bsv.getKuangqsk();
//		double _Kuangqjk=bsv.getKuangqjk();
		
		//ָ���۵���
		double _CaO=bsv.getCaO_zdj();
		double _MgO=bsv.getMgO_zdj();
		double _Xid=bsv.getXid_zdj();
		
		//ָ��ӯ��
		double _Shulyk=bsv.getShul_yk();		//ִ�к�ͬ�еĳ��ֽ�����
		
		double _Jiashj=0;
		double _Jiakhj=0;
		double _Jiaksk=0;
		double _Jine=0;
		double _Buhsmj=0;
		double _Shulzjbz=0;
//		double _Yunfsk=0;
//		double _Yunzfhj=0;
//		double _Buhsyf=0;
		double _Hej=0;
		
		//ָ���۽��
		double _CaOzje=0;
		double _MgOzje=0;
		double _Xidzje=0;
		double _Shulzje=0;
		//�۸������

		_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);												//��˰�ϼ�
		_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);										//�ۿ�ϼ�
		_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);												//�ۿ�˰��
		_Jine=_Jiakhj;																						//���
		_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);	
		_Shulzjbz=_Hansmj;
		//�ϼ�
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//����ӯ�����ۼ۽��
		_CaOzje=(double)CustomMaths.Round_new(_CaO*_Jiessl,2);
		_MgOzje=(double)CustomMaths.Round_new(_MgO*_Jiessl,2);
		_Xidzje=(double)CustomMaths.Round_new(_Xid*_Jiessl,2);
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//����������ӯ��
		
		//���㵥��ʾʱָ���۽����
		bsv.setJiashj(_Jiashj);
		bsv.setJiakhj(_Jiakhj);
		bsv.setJiaksk(_Jiaksk);
		bsv.setJine(_Jine);
		bsv.setBuhsmj(_Buhsmj);
		bsv.setShulzjbz(_Shulzjbz);

		bsv.setHej(_Hej);
		bsv.setCaO_zje(_CaOzje);
		bsv.setMgO_zje(_MgOzje);
		bsv.setXid_zje(_Xidzje);
		bsv.setShul_zje(_Shulzje);
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public IPropertySelectionModel getZhibModel() {
		
		if (_ZhibModel == null) {
			getIZhibmModels();
		}
		return _ZhibModel;
	}
	
	public IPropertySelectionModel getIZhibmModels() {
		
		String sql = "select id,bianm from zhibb where leib=2 order by bianm";
		_ZhibModel=new IDropDownModel(sql);
		return _ZhibModel;
	}
}