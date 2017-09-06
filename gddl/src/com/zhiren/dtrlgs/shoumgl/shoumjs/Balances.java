 /*
 * �������� 2008-4-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.dtrlgs.shoumgl.shoumjs;
/**
 * @author zsj
 *�˷Ѵ���ԭ��
 *1�����˷ѵ�����ú���ͬ��ʱ
 * 		��Ʊ���㣺
 * 			ú���ǵ����ۣ�
 * 			
 * 				1����getMeiPrise�м�����˷��ܽ��
 * 				2����computData��computData_Dpc���ú��ĺ�˰�ܽ�
 * 				3����reCount���ú�˰ú��-��˰�˷ѣ���ֵ����˰ú��ٽ��к�˰���۵�ָ����㡣
 * 					����ֹ2009-8-3�գ�ϵͳ����Ϊ��ʱ���˼�Ϊ��˰�˼ۣ�
 * 
 * 			ú��ǵ����ۣ�
 * 				
 * 				1����getMeiPrise�м�����˷��ܽ��
 * 				2����computData���ú��ĺ�˰�ܽ�
 * 				
 * 
 * 		ú����㣺
 * 			ú���ǵ����ۣ�
 * 
 * 				1����getMeiPrise��ֻ�õ�ú���ͬ�е��˷ѵ���
 * 				2����computData��computData_Dpc��ú����-�˷ѵ���=ú����
 * 					����ֹ2009-8-3�գ�ϵͳ����Ϊ��ʱ���˼�Ϊ��ú��۵ĺ�˰�Ͳ���˰������ͬ��
 * 				3�����ڲ�����getYunFei�����������˷���Ϊ�ա�
 * 			
 * 			ú��ǵ����ۣ�
 * 			
 * 				1����getMeiPrise��ֻ�õ�ú���ͬ�е��˷ѵ���
 * 				2����computData��computData_Dpc�в���ú����-�˷ѵ��� 					
 * 				3�����ڲ�����getYunFei�����������˷���Ϊ�ա�				
 * 
 * 		�˷ѽ��㣺
 * 				
 * 				1��ֱ�Ӽ�����˷�getYunFei��ʵ�֡� 
 * 		
 */
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.*;
import com.zhiren.main.Visit;

import bsh.*;

public class Balances {
	
	
	Balances_variable bsv=new Balances_variable();
	private IPropertySelectionModel _ZhibModel;
	
	public Balances_variable getBsv(){
		
		return bsv;
	}
	
	public void setBsv(Balances_variable value){
		
		bsv=value;
	}
	
	public Balances_variable getBalanceData(String SelIds,long Diancxxb_id,long Jieslx,long Gongysb_id,
				long Hetb_id,String Jieszbsftz,String Yansbh,double Jieskdl,long Yunsdwb_id,double Shangcjsl) throws Exception{
		
		
		bsv.setIsError(true);
		bsv.setErroInfo("");
		bsv.setJieslx(Jieslx);
		bsv.setDiancxxb_id(Diancxxb_id);
		bsv.setSelIds(SelIds);
		
		if(Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.meikjs_feiylbb_id){
//			��Ʊ���㡢ú�����
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieszbsftz,Yansbh,Jieskdl);
			if (bsv.getErroInfo().equals("")){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ��Ҫ������������һ��Ҫ���
				if(Gongysb_id==0){
//					����ڽ���ѡ��ҳ�湩Ӧ��û��ѡ��
					Gongysb_id=bsv.getGongysb_Id();
				}
				
			}else{
				bsv.getErroInfo();
				return bsv;
			}
			
//			�õ���ʽ��
//			�õ�ȫ����ʽ�����п����ڵ�����ú��ʱ�����ֵ������������Ҫ�����˷ѣ�
			if(!getGongsInfo(Diancxxb_id,"ALL")){
				
				return bsv;
			}
			
//			��ú��,��ͬ�е����ֵ
			if(getMeiPrice(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,Jieszbsftz,SelIds,
						Gongysb_id,Jieskdl,Yunsdwb_id,Shangcjsl)){
				
			}else{
				
				bsv.getErroInfo();
				return bsv;
			}
			
			
		}else{	//�˷ѽ��㡢���������˷�
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieszbsftz,Yansbh,Jieskdl);
			
			if(Hetb_id>0){
//				����������˷ѣ������������
//				1��ͨ���˶Ի�Ʊ�õ��˷�
//				2��ͨ�������ͬ������˷�
//				��Hetb_id>0ʱ˵���ǵڶ������
				getYunshtInfo(Hetb_id);
			}
			
			if (bsv.getErroInfo().equals("")){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
				if(Gongysb_id==0){
//					����ڽ���ѡ��ҳ�湩Ӧ��û��ѡ��
					Gongysb_id=bsv.getGongysb_Id();
				}
			}else{
				bsv.getErroInfo();
				return bsv;
			}
			
			getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Yunsdwb_id,Jieslx,Shangcjsl,"");
			
			
//			�õ��˷ѹ�ʽ
			if (getGongsInfo(Diancxxb_id,"YF")) {
				
			}else{
				//return ErroInfo;
				return bsv;
			}
			
//			���˷�
			if(getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl)){
				
			}else{
				bsv.getErroInfo();
				return bsv;
			}
			
			computData_Yf();
		}
		
		reCount();
		computYunfAndHej();

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
//            	bsv.setGongs_Mk(clob.getClob("gongsb", "gongs", rs.getLong(1)));
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
		
		String str_Gongs_Mk="";
		String str_Gongs_Yf="";
		
		if(_Type.equals("ALL")){
			
			str_Gongs_Mk=Shoumjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			str_Gongs_Yf=Shoumjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
			
			if(str_Gongs_Mk.equals("")){
				
				bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
	        	return false;
			}else{
				
				bsv.setGongs_Mk(str_Gongs_Mk);
			}
			
			if(str_Gongs_Yf.equals("")){
				
//				bsv.setErroInfo("û�еõ��˷ѹ�ʽ��ϵͳ����ֵ");
//	        	return false;
			}else{
				
				bsv.setGongs_Yf(str_Gongs_Yf);
			}
		}else if(_Type.equals("MK")){
			
			str_Gongs_Mk=Shoumjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			
			if(str_Gongs_Mk.equals("")){
				
				bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
	        	return false;
			}else{
				
				bsv.setGongs_Mk(str_Gongs_Mk);
			}
		}else if(_Type.equals("YF")){
			
			str_Gongs_Yf=Shoumjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
			if(str_Gongs_Yf.equals("")){
				
				bsv.setErroInfo("û�еõ��˷ѹ�ʽ��ϵͳ����ֵ");
	        	return false;
			}else{
				
				bsv.setGongs_Yf(str_Gongs_Yf);
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
	
	public void getYunshtInfo(long Yunshtb_id){
//		�õ������ͬ���տλ���������У��ʺ�
		JDBCcon con = new JDBCcon();
		try {	
			
			String sql = 
				"select nvl(yd.quanc,'') as quanc,\n" +
				"         nvl(yd.kaihyh,'') as kaihyh,\n" + 
				"         nvl(yd.zhangh,'') as zhangh\n" + 
				"       from hetys hys,yunsdwb yd\n" + 
				"       where hys.yunsdwb_id = yd.id\n" + 
				"             and hys.id="+Yunshtb_id;

			
			ResultSet rs = con.getResultSet(sql);
		
			while(rs.next()){
				
				bsv.setShoukdw(rs.getString("quanc"));
				bsv.setKaihyh(rs.getString("kaihyh"));
				bsv.setZhangH(rs.getString("zhangh"));
			}
			rs.close();
		} catch (SQLException s) {
			// TODO �Զ����� catch ��
			s.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			
			con.Close();
		}	
	}
    
	public Balances_variable getBaseInfo(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id,String Jieszbsftz,String Yansbh,double Jieskdl) throws Exception{

        JDBCcon con =new JDBCcon();
	    try {
            //�������ڡ��������ڡ����������ء�ӯ�������𡢷��������� from fahb
	        //����������ȡ֤����������ú�ǰ���ȡ��������ú�����ȡ��
	    	
//	      ��ȡ�������ñ��н������ò���
//      	1����������
//        	2�������Ȩ����
//        	3��������ʾָ��
//        	4��������������С��λ
//        	5����������ȡ����ʽ
//        	6��Mt����С��λ
//        	7��Mad����С��λ
//        	8��Aar����С��λ
//        	9��Aad����С��λ
//        	10��Adb����С��λ
//        	11��Vad����С��λ
//        	12��Vdaf����С��λ
//        	13��Stad����С��λ
//        	14��Std����С��λ
//        	15��Had����С��λ
//        	16��Qnetar����С��λ
//        	17��Qbad����С��λ
//        	18��Qgrad����С��λ
//	    	19��T2����С��λ
//        	19������ָ�����
//	    	20���Ƿ��Կ���������
        
//	        String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//��������
//	        String jies_Jqsl="jingz";								//�����Ȩ����
//	        String jies_Jsslblxs="0";								//������������С��λ
//	        String jies_Jieslqzfs="sum(round())";					//��������ȡ����ʽ
//	        String jies_Kdkskzqzfs="round(sum())";					//�۶֡���ˮ������ȡ����ʽ
//	        String jies_yunfjssl="jingz";							//�˷ѽ�������
//	        boolean mbl_yunfjssl=false;								//�����ж��û��Ƿ񵥶��趨���˷ѵĽ���������falseû�У���ȡú���������
//	        
//	        
//	        jies_Jqsl="f."+MainGlobal.getXitxx_item("����", Locale.jiaqsl_xitxx, 
//        			String.valueOf(Diancxxb_id),jies_Jqsl);
//        	
//        if(Shoumjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){
//
//        	String JiesszArray[][]=null;
//		
//        	JiesszArray=Shoumjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
//	
//			for(int i=0;i<JiesszArray.length;i++){
//				
//				if(JiesszArray[i][0]!=null){
//					
//					if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
//						
//						jies_Jssl=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
//						
//						jies_Jqsl=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.jiesslblxsw_jies)){
//						
//						jies_Jsslblxs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.kdkskzqzfs_jies)){
//						
//						jies_Kdkskzqzfs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.meiksl_jies)){
//						
//						bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1]));
//					}else if(JiesszArray[i][0].equals(Locale.yunfsl_jies)){
//						
//						bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1]));
//					}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
//						
//						jies_Jieslqzfs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.yunfjsslzcfs_jies)){
//						
//						jies_yunfjssl=JiesszArray[i][1];
//						mbl_yunfjssl=true;
//					}
//				}
//			}
//        }
//        
//        if(!mbl_yunfjssl){
//        	
////        	false��ʾ�û�û�е��������˷ѽ�����������ȡú���������
//        	jies_yunfjssl=jies_Jssl;
//        }
	    	
	    	String sql="";
	    	
	    	sql=" select hetb_id,yunj,meikxxb_id,gongysb_id,pinzb_id,yunsfsb_id,minfahrq,maxfahrq,mindaohrq,maxdaohrq,gongysqc,meikdwqc,faz,faz_id,daoz_id,yuanshdw," +
	    		" pinz,yunsfs,nvl(getMeiksx(meikxxb_id,diancxxb_id,'�˾�'),0) as yunju,jihkjb_id		\n "+
	    		" from 														\n" +
	    		" (select max(hetb_id) as hetb_id,max(m.yunj) as yunj,max(f.meikxxb_id) as meikxxb_id,max(f.gongysb_id) as gongysb_id, \n "+
	    		" max(pinzb_id) as pinzb_id,max(yunsfsb_id) as yunsfsb_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq,to_char(max(fahrq),'yyyy-mm-dd') as maxfahrq, \n " +
	    		" max(pz.mingc) as pinz,max(ysfs.mingc) as yunsfs," +
				" to_char(min(daohrq),'yyyy-mm-dd') as mindaohrq,to_char(max(daohrq),'yyyy-mm-dd') as maxdaohrq,max(g.quanc) as gongysqc,max(m.quanc) as meikdwqc, \n " +
				" max(cz.mingc) as faz,max(cz.id) as faz_id,max(dz.id) as daoz_id,max(decode(vwydw.mingc,null,(select mingc from diancxxb where id = f.diancxxb_id),vwydw.mingc)) as yuanshdw, \n " +
				" max(f.diancxxb_id) as diancxxb_id,max(f.jihkjb_id) as jihkjb_id " +
				" from fahb f,zhilb z,kuangfzlb kz,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz" +
				" where f.zhilb_id=z.id and kz.id(+)=f.kuangfzlb_id and f.faz_id=cz.id and f.pinzb_id=pz.id " +
				" and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id" +
				" and z.liucztb_id=1 and f.liucztb_id=0 " +
				" and f.lie_id in("+SelIds+"))";
           
            ResultSet rs = con.getResultSet(sql);
            
            if (rs.next()) {
            	
            	bsv.setFahksrq(rs.getDate("minfahrq"));
            	bsv.setFahjzrq(rs.getDate("maxfahrq"));
            	if (bsv.getFahksrq().equals(bsv.getFahjzrq())){
            		
            		bsv.setFahrq(Shoumjsdcz.FormatDate(bsv.getFahksrq()));
            	}else{
            								
            		bsv.setFahrq(Shoumjsdcz.FormatDate(bsv.getFahksrq()) +"��"+ Shoumjsdcz.FormatDate(bsv.getFahjzrq()));
            	}
            	
            	bsv.setYansksrq(rs.getDate("mindaohrq"));
            	bsv.setYansjsrq(rs.getDate("maxdaohrq"));

            	if (bsv.getYansksrq().equals(bsv.getYansjsrq())){
            		
            		bsv.setDaohrq(Shoumjsdcz.FormatDate(bsv.getYansksrq()));
            	}else{
            		
            		bsv.setDaohrq(Shoumjsdcz.FormatDate(bsv.getYansksrq())+"��"+Shoumjsdcz.FormatDate(bsv.getYansjsrq()));
            	}
            	
            	bsv.setHetb_Id(rs.getLong("hetb_id"));
            	bsv.setMeikxxb_Id(rs.getLong("meikxxb_id"));
                bsv.setGongysb_Id(rs.getLong("gongysb_id"));
            	bsv.setRanlpzb_Id(rs.getLong("pinzb_id"));
                bsv.setShoukdw(rs.getString("gongysqc"));
                bsv.setFahdw(rs.getString("gongysqc"));
                bsv.setMeikdw(rs.getString("meikdwqc"));
                bsv.setFaz(rs.getString("faz"));
                bsv.setFaz_Id(rs.getLong("faz_id"));
                bsv.setDaoz_Id(rs.getLong("daoz_id"));
                bsv.setYuanshr(rs.getString("yuanshdw"));
                bsv.setXianshr(rs.getString("yuanshdw"));
	          	bsv.setRanlpz(rs.getString("pinz"));
	          	bsv.setTianzdw(MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(Diancxxb_id)));
	          	bsv.setYunsfs(rs.getString("yunsfs"));
	          	bsv.setYunsfsb_id(rs.getLong("yunsfsb_id"));
	          	bsv.setJiesrq(Shoumjsdcz.FormatDate(new Date()));	//��������
	          	bsv.setJiesbh(Shoumjsdcz.getJiesbh(String.valueOf(Diancxxb_id),""));
	        	//��lie_id�ó�����
	               ResultSetList rsl=con.getResultSetList("select chec from fahb where lie_id in("+SelIds+")");
	               if(rsl.next())
	               bsv.setDaibcc(rsl.getString("chec"));
	               rsl.close();
//            	bsv.setYunju_cf(rs.getDouble("yunju"));		//����
//            	bsv.setJihkjb_id(rs.getLong("jihkjb_id"));
            	bsv.setHetbh(MainGlobal.getTableCol("hetb", "hetbh", "id", String.valueOf(bsv.getHetb_Id())));
	          	
            }else{
            	bsv.setErroInfo("Ҫ����ĳ�Ƥ��Ϣ�����ڿ����ѱ������û�ɾ��!");
            	return bsv;
            }
            rs.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	con.Close();
        }

		return bsv;
	}
	
	public Balances_variable getJiesszl(String Jieszbsftz,String SelIds,long Diancxxb_id,long Gongysb_id,
				long Hetb_id,double Jieskdl,long Yunsdwb_id,long Jieslx,double Shangcjsl,String Tsclzb_where){
		
//		������������
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
			
			rsl=con.getResultSetList(Shoumjsdcz.getJiesszl_Sql(bsv, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, 
					Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, Tsclzb_where).toString());
			
			if(rsl.next()){
				
//				����
				bsv.setKoud(rsl.getDouble("koud"));					//�۶�
            	bsv.setKous(rsl.getDouble("kous"));					//��ˮ
            	bsv.setKouz(rsl.getDouble("kouz"));					//����
            	bsv.setChes(rsl.getLong("ches"));					    //����
            	
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//����
            	bsv.setYingksl(rsl.getDouble("yingk"));				//ӯ��  
            	bsv.setYingd(rsl.getDouble("yingd"));				//ӯ��
            	bsv.setKuid(rsl.getDouble("kuid"));					
            	
            	bsv.setJiessl(rsl.getDouble("jiessl")-Jieskdl);		//��������
            	
            	if(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib){
            		
            		bsv.setJiessl(rsl.getDouble("yunfjssl")-Jieskdl);
            	}
				
            	bsv.setYanssl(rsl.getDouble("yanssl"));				//������������
            	bsv.setJingz(rsl.getDouble("jingz"));				//����
            	bsv.setKoud_js(Jieskdl);							//����۶�
            	
            	bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl()-bsv.getJingz()),2));	//������������(�������͹������Ĳ�ֵ)
//            	bsv.setJiesslcy(rsl.getDouble("jieslcy"));			//������������
            	bsv.setYuns(rsl.getDouble("yuns"));					//ʵ������
            	bsv.setYunfjsl(rsl.getDouble("yunfjssl")-Jieskdl);	//�˷ѽ�������
            	bsv.setChaokdl(rsl.getDouble("chaokdl")); 			//����������Ҫ�ŵ�danpcmxb�У�
            	
//            	if(!blnDandszyfjssl&&Jieslx==Locale.liangpjs_feiylbb_id){
////            		û�е��������˷ѽ������ҽ��㷽ʽΪ��Ʊ�����˷ѽ���������Ϊgongfsl
//            		bsv.setYunfjsl(rsl.getDouble("biaoz"));
//            	}
            	
//	        	����ָ��
	        	bsv.setQnetar_cf(rsl.getDouble("Qnetar_cf"));
	            bsv.setStd_cf(rsl.getDouble("Std_cf")); 
	            bsv.setMt_cf(rsl.getDouble("Mt_cf"));
	            bsv.setMad_cf(rsl.getDouble("Mad_cf"));
	            bsv.setAar_cf(rsl.getDouble("Aar_cf"));
	            bsv.setAad_cf(rsl.getDouble("Aad_cf"));
	            bsv.setAd_cf(rsl.getDouble("Ad_cf"));
	            bsv.setVad_cf(rsl.getDouble("Vad_cf"));
	            bsv.setVdaf_cf(rsl.getDouble("Vdaf_cf"));
	            bsv.setStad_cf(rsl.getDouble("Stad_cf"));
//	            bsv.setStar_cf(rsl.getDouble("Star_cf"));
	            bsv.setHad_cf(rsl.getDouble("Had_cf"));
	            bsv.setQbad_cf(rsl.getDouble("Qbad_cf"));
	            bsv.setQgrad_cf(rsl.getDouble("Qgrad_cf"));
	            bsv.setT2_cf(rsl.getDouble("T2_cf"));
	            
//              ��ָ��
                bsv.setQnetar_kf(rsl.getDouble("Qnetar_kf"));
                bsv.setStd_kf(rsl.getDouble("Std_kf")); 
//              bsv.setStar_kf(rsl.getDouble("Star_kf"));
                bsv.setMt_kf(rsl.getDouble("Mt_kf"));
                bsv.setMad_kf(rsl.getDouble("Mad_kf"));
                bsv.setAar_kf(rsl.getDouble("Aar_kf"));
                bsv.setAad_kf(rsl.getDouble("Aad_kf"));
                bsv.setAd_kf(rsl.getDouble("Ad_kf"));
                bsv.setVad_kf(rsl.getDouble("Vad_kf"));
                bsv.setVdaf_kf(rsl.getDouble("Vdaf_kf"));
                bsv.setStad_kf(rsl.getDouble("Stad_kf"));
                bsv.setHad_kf(rsl.getDouble("Had_kf"));
                bsv.setQbad_kf(rsl.getDouble("Qbad_kf"));
                bsv.setQgrad_kf(rsl.getDouble("Qgrad_kf"));
                bsv.setT2_kf(rsl.getDouble("T2_kf"));
                
                
//              ����ָ��
                String strcforkf="_cf";
//                if(jies_shifykfzljs.equals("��")){
                if(bsv.getShifykfzljs().equals("��")){
//                	�Ƿ��ÿ���������
                	strcforkf="_kf";
                }
                
                if(Jieszbsftz.equals("��")){
                	
                	bsv.setQnetar_js(rsl.getDouble("Qnetar_js"));
                    bsv.setStd_js(rsl.getDouble("Std_js")); 
                    bsv.setMt_js(rsl.getDouble("Mt_js"));
                    bsv.setMad_js(rsl.getDouble("Mad_js"));
                    bsv.setAar_js(rsl.getDouble("Aar_js"));
                    bsv.setAad_js(rsl.getDouble("Aad_js"));
                    bsv.setAd_js(rsl.getDouble("Ad_js"));
                    bsv.setVad_js(rsl.getDouble("Vad_js"));
                    bsv.setVdaf_js(rsl.getDouble("Vdaf_js"));
                    bsv.setStad_js(rsl.getDouble("Stad_js"));
//                    bsv.setStar_js(rsl.getDouble("Star_js"));
                    bsv.setHad_js(rsl.getDouble("Had_js"));
                    bsv.setQbad_js(rsl.getDouble("Qbad_js"));
                    bsv.setQgrad_js(rsl.getDouble("Qgrad_js"));
                    bsv.setT2_js(rsl.getDouble("T2_js"));
                    
                }else if(Jieszbsftz.equals("��")){
                	
                	bsv.setQnetar_js(rsl.getDouble("Qnetar"+strcforkf));
                    bsv.setStd_js(rsl.getDouble("Std"+strcforkf)); 
                    bsv.setMt_js(rsl.getDouble("Mt"+strcforkf));
                    bsv.setMad_js(rsl.getDouble("Mad"+strcforkf));
                    bsv.setAar_js(rsl.getDouble("Aar"+strcforkf));
                    bsv.setAad_js(rsl.getDouble("Aad"+strcforkf));
                    bsv.setAd_js(rsl.getDouble("Ad"+strcforkf));
                    bsv.setVad_js(rsl.getDouble("Vad"+strcforkf));
                    bsv.setVdaf_js(rsl.getDouble("Vdaf"+strcforkf));
                    bsv.setStad_js(rsl.getDouble("Stad"+strcforkf));
//                    bsv.setStar_js(rsl.getDouble("Star"+strcforkf));
                    bsv.setHad_js(rsl.getDouble("Had"+strcforkf));
                    bsv.setQbad_js(rsl.getDouble("Qbad"+strcforkf));
                    bsv.setQgrad_js(rsl.getDouble("Qgrad"+strcforkf));
                    bsv.setT2_js(rsl.getDouble("T2"+strcforkf));
                    
                    
                    if(bsv.getQnetar_js()==0){
                    	
                    	bsv.setErroInfo("û�п󷽻������ݣ���¼�룡");
                    }
                }
                bsv.setYunju_js(bsv.getYunju_cf());		//�˾ำֵ
                bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), Shangcjsl));	//ʵ�ʽ�������+�ϴν���������Ϊ���������ۼ��ã�
                if(bsv.getKoud()+bsv.getKous()+bsv.getKouz()+bsv.getKoud_js()>0){

                	bsv.setBeiz(bsv.getBeiz()+" ����۶�:"+(bsv.getKoud()+bsv.getKous()+bsv.getKouz()+bsv.getKoud_js()));
                }
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return bsv;
	}
	
	//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
	private boolean getMeiPrice(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String Jieszbsftz,String SelIds,long Gongysb_id,
									double Jieskdl,long yunsdwb_id,double Shangcjsl){
		//�õ���ͬ��Ϣ�е��˼�
			JDBCcon con =new JDBCcon();
			String sql="";
			Interpreter bsh=new Interpreter();
			Shoumjsdcz Jscz=new Shoumjsdcz();
			try{
				
//				����(��ͬ������Ϊ��λ��ÿ�´�һ�����ݶ���ȡһ����)
				sql="select nvl(htsl.hetl,0) as hetl,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh \n"
					+ " from hetb htb, hetslb htsl		\n"
					+ " where htb.id=htsl.hetb_id(+)	\n"      
					+ " and (htsl.pinzb_id="+Ranlpzb_id+" or htsl.pinzb_id is null) and (yunsfsb_id="+Yunsfsb_id
					+ " or yunsfsb_id is null) and (faz_id="+Faz_id+" or faz_id is null) and (daoz_id="+Daoz_id+" or daoz_id is null)	\n"
					+ " and (htsl.diancxxb_id="+Diancxxb_id+" or htsl.diancxxb_id is null) and (htsl.riq<=to_date('"+Shoumjsdcz.FormatDate(Minfahrq)
					+ "','yyyy-MM-dd') or htsl.riq is null)	\n"              
					+ " and htb.id="+Hetb_id+"";
				
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.next()){
					
					bsv.setHetml(rsl.getString("hetl"));
					bsv.setShoukdw(rsl.getString("gongfdwmc"));
					bsv.setKaihyh(rsl.getString("gongfkhyh"));
					bsv.setZhangH(rsl.getString("gongfzh"));
				}
				
//				����(��ͬ��һ����ͬ�Ŷ�Ӧ���������¼)
				sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
					+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
					+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
					+ " and htzl.danwb_id=dwb.id	\n"
					+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
					+ " and htb.id="+Hetb_id+" ";
				
//				�õ����ۿ�����Ҫ���⴦��(������)��ָ��
				sql="select distinct zbb.bianm as zhib\n" +
					"        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" + 
					"        where htb.id=htzkk.hetb_id\n" + 
					"              and htzkk.zhibb_id=zbb.id\n" + 
					"              and htzkk.hetjsxsb_id=xs.id\n" + 
					"              and xs.bianm='"+Locale.danpc_jiesxs+"'\n" + 
					"              and zbb.leib=1\n" + 
					"              and htb.id="+Hetb_id+"";
				
				rsl=con.getResultSetList(sql);
				while(rsl.next()){
//					SelIdsΪȫ������lie_id
//					����ָ������ֻ�ܸ�ֵһ��
					bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items()+Shoumjsdcz.getJieszbtscl(rsl.getString("zhib"), SelIds));
				}
				
				if(!bsv.getJieszbtscl_Items().equals("")
						&&bsv.getTsclzbs()==null){
//					˵����Ҫ���⴦���ָ��
					String ArrayTsclzbs[]=null;
					ArrayTsclzbs=bsv.getJieszbtscl_Items().split(";");
					bsv.setTsclzbs(ArrayTsclzbs);
//					0,�˾�,meikxxb_id,100,10,0;
//					1,�˾�,meikxxb_id,101,15,0;
//					2,�˾�,meikxxb_id,102,20,0;
//					3,Std,meikxxb_id,100,1.0,0;
				}
				
//				�۸񣨺�ͬ��һ����ͬ��Ӧ��������۸�	
				sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,jijlx,	\n"
					+ " jijdw.bianm as jijdw,nvl(jijgs,'') as jijgs,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,htjg.pinzb_id,		\n"
					+ " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs,fengsjj	\n"
					+ " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
					+ " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
					+ " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n" 
					+ " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
					+ " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n" 
					+ " and htjg.yunsfsb_id=ysfs.id																\n"
					+ " and htjg.hetjjfsb_id=htjfsb.id															\n"
					+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"          
					+ " and htb.id="+Hetb_id+"";
				
				rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
//					ͨ����ͬ���ý���ֵ
					setJieshtinfo(rsl,bsh);
					
//					��ͬ����ָ��,ȡ�����������ĺ�ͬ����
					if(rsl.getRows()==1){
						
//						��һ����ͬ
//						Ŀ¼��
						
//						�����˷�
						if(Jieslx==Locale.liangpjs_feiylbb_id){
							
							getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
						}
						
						if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								Ŀ¼�۽���
							
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//								�����ν���
								
								String[] test=new String[1];
								
								if(SelIds.indexOf(",")==-1){
									
									test[0]=SelIds;
								}else{
									
									test=SelIds.split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									��ý�������������
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl,"");
									
//									ΪĿ¼�۸�ֵ
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
//									������ۿ�
									getZengkk(Hetb_id,bsh,true,null);
									
//									ú�˰���۱���С��λ
									bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
									
//									��˰����ȡ����ʽ
									bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
									
//									�û��Զ��幫ʽ
									bsh.set(Locale.user_custom_mlj_jiesgs,bsv.getUser_custom_mlj_jiesgs());			
									
//									���ۿ��С��λ
									bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
									
//									ִ�к�ͬ�۸�ʽ
									ExecuteHetmdjgs(bsh);
									
//									ִ�й�ʽ
									bsh.eval(bsv.getGongs_Mk());
									
//									�õ�������ָ��
									setJieszb(bsh,0,Shangcjsl);
									
//									����ú����
									computData_Dpc(SelIds,Hetb_id,Shangcjsl);
								}
								
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						��Ȩƽ��
								
//								��ý�������������
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
										Jieskdl,yunsdwb_id,Jieslx,Shangcjsl,"");
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh,true,null);
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								��˰����ȡ����ʽ
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								���ۿ��С��λ
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0,Shangcjsl);
								
								if(bsv.getTsclzbs()!=null){
//									���������Ҫ�������������ָ��
									Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
								}
								
//								����ú����
								computData(SelIds,Hetb_id,Shangcjsl);
							}
							
						}else{		
							
//							һ����ͬ
//							��Ŀ¼��
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//							�����ν���
								
								String[] test=new String[1];
								
								if(SelIds.indexOf(",")==-1){
									
									test[0]=SelIds;
								}else{
									
									test=SelIds.split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									��ý�������������
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
											yunsdwb_id,Jieslx,Shangcjsl,"");
									
									double Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
									
//									Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
									
									bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
									bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));								//ָ�굥λ
									
									bsv.setYifzzb(rsl.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
									
									bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
									bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
									
									bsh.set(rsl.getString("zhib")+"��������", 	0);
									bsh.set(rsl.getString("zhib")+"�۸�����", 	0);
									bsh.set(rsl.getString("zhib")+"�����۵�λ", 	"");
									bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"");
									bsh.set(rsl.getString("zhib")+"���ۿ����", 	0);
									bsh.set(rsl.getString("zhib")+"���ۿ������λ", 	"");
									bsh.set(rsl.getString("zhib")+"��������", 	0);
									bsh.set(rsl.getString("zhib")+"��׼���ۼ�", 	0);
									bsh.set(rsl.getString("zhib")+"С������", 	"");
									
//									������ۿ�
									getZengkk(Hetb_id,bsh,true,null);
									
//									�û��Զ��幫ʽ
									bsh.set(Locale.user_custom_fmlj_jiesgs,bsv.getUser_custom_fmlj_jiesgs());
									
//									ú�˰���۱���С��λ
									bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
									
//									��˰����ȡ����ʽ
									bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
									
//									���ۿ��С��λ
									bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
									
//									ִ�к�ͬ�۸�ʽ
									ExecuteHetmdjgs(bsh);
//									ִ�й�ʽ
									bsh.eval(bsv.getGongs_Mk());
									
//									�õ�������ָ��
									setJieszb(bsh,0,Shangcjsl);
									
//									����ú����
									computData_Dpc(SelIds,Hetb_id,Shangcjsl);
								}
								
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
								
//								��ý�������������
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,
										Hetb_id,Jieskdl,yunsdwb_id,Jieslx,Shangcjsl,"");
								
								double Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
								
								bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
								bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));
								
								bsv.setYifzzb(rsl.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
								
								bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
								bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
								
								bsh.set(rsl.getString("zhib")+"��������", 	0);
								bsh.set(rsl.getString("zhib")+"�۸�����", 	0);
								bsh.set(rsl.getString("zhib")+"�����۵�λ", 	"");
								bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"");
								bsh.set(rsl.getString("zhib")+"���ۿ����", 	0);
								bsh.set(rsl.getString("zhib")+"���ۿ������λ", 	"");
								bsh.set(rsl.getString("zhib")+"��������", 	0);
								bsh.set(rsl.getString("zhib")+"��׼���ۼ�", 	0);
								bsh.set(rsl.getString("zhib")+"С������", 	"");
								
//								������ۿ�
								getZengkk(Hetb_id,bsh,true,null);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								��˰����ȡ����ʽ
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								���ۿ��С��λ
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0,Shangcjsl);
								
								if(bsv.getTsclzbs()!=null){
//									���������Ҫ�������������ָ��
									Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
								}
								
//								����ú����
								computData(SelIds,Hetb_id,Shangcjsl);
							}
						}
						
						bsv.setHetjgpp_Flag(true);
						bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
					}else{
//						�ж����ͬ
//						Ŀ¼��
						
						if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//Ŀ¼�۽���
							
//							�����˷�
							if(Jieslx==Locale.liangpjs_feiylbb_id){
								
								getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
							}
						
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){		//�����ν���
							
								String[] test=new String[1];
								
								if(SelIds.indexOf(",")==-1){
									
									test[0]=SelIds;
								}else{
									
									test=SelIds.split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
	//								��ý�������������
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
											yunsdwb_id,Jieslx,Shangcjsl,"");
									
	//								ΪĿ¼�۸�ֵ
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
	//								������ۿ�
									getZengkk(Hetb_id,bsh,true,null);
									
//									�û��Զ��幫ʽ
									bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
									
//									ú�˰���۱���С��λ
									bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
									
//									��˰����ȡ����ʽ
									bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
									
//									���ۿ��С��λ
									bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
									
//									ִ�к�ͬ�۸�ʽ
									ExecuteHetmdjgs(bsh);
									
	//								ִ�й�ʽ
									bsh.eval(bsv.getGongs_Mk());
									
	//								�õ�������ָ��
									setJieszb(bsh,0,Shangcjsl);
									
	//								����ú����
									computData_Dpc(SelIds,Hetb_id,Shangcjsl);
								}
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��

								
//								��ý�������������
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
										yunsdwb_id,Jieslx,Shangcjsl,"");
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh,true,null);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								��˰����ȡ����ʽ
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								���ۿ��С��λ
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0,Shangcjsl);
								
								if(bsv.getTsclzbs()!=null){
//									���������Ҫ�������������ָ��
									Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
								}
								
//								����ú����
								computData(SelIds,Hetb_id,Shangcjsl);
							}
						}else{
							
//							�����ͬ
//							��Ŀ¼��
							double	Dbljijzb=0;
							
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){	//�����ν���
								
									String[] test=new String[1];
									
									if(SelIds.indexOf(",")==-1){
										
										test[0]=SelIds;
									}else{
										
										test=SelIds.split(",");
									}
							
									for(int i=0;i<test.length;i++){
										
										
//										��ý�������������
										getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
												yunsdwb_id,Jieslx,Shangcjsl,"");
										rsl.beforefirst();
										
										while(rsl.next()){
											
											Dbljijzb=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
											Dbljijzb=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
										
											if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
													&&Shoumjsdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
											){
												
//												���ý����������
												setJieshtinfo(rsl, bsh);
												
												bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
												bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));
												
												bsv.setYifzzb(rsl.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
												
												bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
												bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
												
												bsh.set(rsl.getString("zhib")+"��������", 	0);
												bsh.set(rsl.getString("zhib")+"�۸�����", 	0);
												bsh.set(rsl.getString("zhib")+"�����۵�λ", 	"");
												bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"");
												bsh.set(rsl.getString("zhib")+"���ۿ����", 	0);
												bsh.set(rsl.getString("zhib")+"���ۿ������λ", 	"");
												bsh.set(rsl.getString("zhib")+"��������", 	0);
												bsh.set(rsl.getString("zhib")+"��׼���ۼ�", 	0);
												bsh.set(rsl.getString("zhib")+"С������", 	"");
												
	//											������ۿ�
												getZengkk(Hetb_id,bsh,true,null);
												
	//											�û��Զ��幫ʽ
												bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
												
//												ú�˰���۱���С��λ
												bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
												
//												��˰����ȡ����ʽ
												bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
												
//												���ۿ��С��λ
												bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
												
//												ִ�к�ͬ�۸�ʽ
												ExecuteHetmdjgs(bsh);
												
	//											ִ�й�ʽ
												bsh.eval(bsv.getGongs_Mk());
												
	//											�õ�������ָ��
												setJieszb(bsh,0,Shangcjsl);
												
//												�����˷ѣ�ע�⣺ֻҪ��һ�Σ�Ҫ��һ�������жϣ�
												if(Jieslx==Locale.liangpjs_feiylbb_id&&!bsv.getDanpcysyf()){
//													�ж�������1������Ʊ���㣻2�������ν��㻹û������˷�
													getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
													bsv.setDanpcysyf(true);
												}
												
	//											����ú����
												computData_Dpc(SelIds,Hetb_id,Shangcjsl);
												
												bsv.setHetjgpp_Flag(true);
												bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
											}	
										}
									}		
									
										
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
								
//									��ý�������������
									getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
											yunsdwb_id,Jieslx,Shangcjsl,"");
									
									do{
										
										Dbljijzb=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");

										Dbljijzb=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
										
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
												&&Shoumjsdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
											){
											
//											���ý���ĺ�ֵͬ
											this.setJieshtinfo(rsl, bsh);
											
											bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//��ͬ�۸��id
											
											bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
											bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));
											
											bsv.setYifzzb(rsl.getString("zhib"));			//Ĭ�ϵ��Ѹ�ֵָ��
											
											bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
											bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
											
											bsh.set(rsl.getString("zhib")+"��������", 	0);
											bsh.set(rsl.getString("zhib")+"�۸�����", 	0);
											bsh.set(rsl.getString("zhib")+"�����۵�λ", 	"");
											bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"");
											bsh.set(rsl.getString("zhib")+"���ۿ����", 	0);
											bsh.set(rsl.getString("zhib")+"���ۿ������λ", 	"");
											bsh.set(rsl.getString("zhib")+"��������", 	0);
											bsh.set(rsl.getString("zhib")+"��׼���ۼ�", 	0);
											bsh.set(rsl.getString("zhib")+"С������", 	"");
											
//												������ۿ�
											getZengkk(Hetb_id,bsh,true,null);
											
//												�û��Զ��幫ʽ
											bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
											
//											ú�˰���۱���С��λ
											bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
											
//											��˰����ȡ����ʽ
											bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
											
//											���ۿ��С��λ
											bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
											
//											ִ�к�ͬ�۸�ʽ
											ExecuteHetmdjgs(bsh);
											
//											ִ�й�ʽ
											bsh.eval(bsv.getGongs_Mk());
											
//												�õ�������ָ��
											setJieszb(bsh,0,Shangcjsl);
											
//											�����˷ѣ�ע�⣺ֻҪ��һ�Σ�
											if(Jieslx==Locale.liangpjs_feiylbb_id){
//												�ж�����������Ʊ����
												getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
											}
											
											if(bsv.getTsclzbs()!=null){
//												���������Ҫ�������������ָ��
												Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
														Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
											}
											
//												����ú����
											computData(SelIds,Hetb_id,Shangcjsl);
											
											bsv.setHetjgpp_Flag(true);
											bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
										}
										
									}while(rsl.next());
							}
						}
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
	
	private void setJieshtinfo(ResultSetList rsl, Interpreter bsh) {
//		�������ܣ�
		
//				������ʱ��Ҫ����ͬ��Ϣ��bsh��ֵ���Ա���������
//		�����߼���
		
//				��rsl��ֵȡ����ֵ��bsh
//		�����βΣ�rsl ��ͬ�����ݼ���bsh �����Զ����������
		
		bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
		bsv.setJijlx(rsl.getInt("jijlx"));				//�������ͣ�0����˰��1������˰��
		bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
		bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
		bsv.setHetmdjgs(Shoumjsdcz.getTransform_Hetjgs(
				rsl.getString("jijgs").trim(),
				rsl.getString("danw"),
				rsl.getDouble("jij"),
				bsv.getDiancxxb_id()));		//��ͬ���۹�ʽ
		bsv.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������۸񡢳���۸�
		bsv.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
		bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
		bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
		bsv.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
		bsv.setFengsjj(rsl.getDouble("fengsjj")); 		//��ͬ�۸��еķֹ�˾�Ӽۣ�ͳһ�����ã�
//															�ֹ�˾�Ӽ۴����߼���
//																1�����ݺ�ͬ�۸����ͣ���˰������˰�����ԭʼ����ۣ������Ǻ�˰Ҳ�����ǲ���˰����
//																	�ñ�������,�����ֹ�˾�Ӽ۽��б��档
//																2������Ǻ�˰�ۣ����㵥��=���㵥��+�ֹ�˾�Ӽۣ�
//																		����ǲ���˰�ۣ����㵥��=��������ĺ�˰��+�ֹ�˾�Ӽ�
//		
		bsv.setJiagpzId(rsl.getString("pinzb_id"));			//�۸����Ʒ�֣�Ϊ������һ����ͬ��ͬƷ�ֲ�ͬ�۸�����
		try {
			bsh.set("������ʽ", bsv.getJiesxs());
			bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
			bsh.set("�۸�λ", bsv.getHetmdjdw());	
			bsh.set("��ͬ�۸�", bsv.getHetmdj());
			bsh.set("���ú��", bsv.getZuigmj());
			bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());
			
		} catch (EvalError e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void ExecuteHetmdjgs(Interpreter bsh){
//		ִ�к�ͬ�۸�ʽ
//		�������ܣ�
		
//			��ִ�н��㹫ʽǰ�������ͬ�۸��д��ڼ۸�ʽ��Ҫ��ִ�м۸�ʽ������ֵ��ֵ������ͬ�۸񡱡�
//		�����߼���

//			ִ�к�ͬ�۸�Ĺ�ʽ��
//		�����βΣ�bsh �����Զ����������
		
		if(!bsv.getHetmdjgs().equals("")){
			
			try {
//				bsh.set("������������",10);
				bsh.eval(bsv.getHetmdjgs());
				bsv.setHetmdj(Double.parseDouble(bsh.get("��ͬ�۸�").toString()));		//��ͬ�۸�
				bsh.set("��ͬ�۸�", bsv.getHetmdj());
			} catch (EvalError e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}

	private boolean getZengkk(long Hetb_id,Interpreter bsh,boolean Falg,String[] Tsclzb_item){
//		���ۿ�
		JDBCcon con=new JDBCcon();
		try{
			ResultSetList rsl=null;
			
			String sql="select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jis,	\n"
				+ " jisdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,	\n"
				+ " xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,nvl(canzsx,0) as canzsx, \n"
				+ " nvl(canzxx,0) as canzxx,shiyfw,nvl(xs.bianm,'') as jiesxs	\n"  
				+ " from hetb htb, hetzkkb htzkk,zhibb zbb,tiaojb tjb,danwb dwb,danwb jisdw,danwb kjdw,	\n"
				+ " danwb zfjdw,zhibb czxm,danwb czxmdw,hetjsxsb xs		\n"
				+ " where htb.id=htzkk.hetb_id and htzkk.zhibb_id=zbb.id and htzkk.tiaojb_id=tjb.id	\n" 
				+ " and htzkk.danwb_id=dwb.id(+) and htzkk.jisdwid=jisdw.id(+)	\n" 
				+ " and htzkk.koujdw=kjdw.id(+)		\n"
				+ " and htzkk.hetjsxsb_id=xs.id(+)	\n"
				+ " and htzkk.zengfjdw=zfjdw.id(+)	\n" 
				+ " and htzkk.canzxm=czxm.id(+)		\n" 
				+ " and htzkk.canzxmdw=czxmdw.id(+)	\n"
				+ " and tjb.leib=1 and zbb.leib=1	\n"          
				+ " and htb.id="+Hetb_id+" order by zbb.bianm,tjb.bianm,xiax,shangx ";
			
			rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//��¼ָ�����ֵ
			double Dblczxm=0;		//��¼������Ŀ��ֵ
			String Strtmp="";		//��¼�趨��ָ��
			String Strimplementedzb="";	//��¼�Ѿ�ִ�й���ָ�꣨���Ѿ������ִ�е�ָ�꣩��
			double Dblimplementedzbsx=0;	//��¼��ִ�й���ָ�������
			StringBuffer sb = new StringBuffer();	//��¼��ͬ���ۿ��е����÷�ΧΪ1�ļ�¼
			
			if(Falg){
//				Falg=true ˵�����������ۿ���㣬��ʱ���ָ�������⴦�������� Ӧ������
				while(rsl.next()){
					
					Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
//					�õ�������Ŀ�Ľ����׼
					Dblczxm=Shoumjsdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");
					
//					ָ��Ľ���ָ��
					Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
					Dblczxm=Shoumjsdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
					
					if(Dbltmp>=Shoumjsdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
							&&Shoumjsdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
							&&Shoumjsdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"))	//�����ָ����Ҫ���⣨�����Σ����������μ�ͳһ�����ۿ�ļ���
					){
						
						//ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
						Strimplementedzb = rsl.getString("zhib");	//��¼��ʹ�õ�ָ��
						Dblimplementedzbsx = rsl.getDouble("shangx");
						
						bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
						bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));			//ָ�굥λ�������޵�λ��
						bsh.set(rsl.getString("zhib")+"���ۿ�����", 	rsl.getString("tiaoj"));		//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
						bsh.set(rsl.getString("zhib")+"��������",	rsl.getDouble("zengfj"));		//������
						bsh.set(rsl.getString("zhib")+"�۸�����",	rsl.getDouble("kouj"));			//�ۼ�
						bsh.set(rsl.getString("zhib")+"�����۵�λ", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//���۵�λ
						bsh.set(rsl.getString("zhib")+"�۸��۵�λ", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//�ۼ۵�λ
						bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
						bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
						bsh.set(rsl.getString("zhib")+"���ۿ����",	rsl.getDouble("jis"));			//������ÿ����xx�򽵵�xx��
						bsh.set(rsl.getString("zhib")+"���ۿ������λ",	rsl.getString("jisdw"));	//������λ
						bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	rsl.getDouble("jizzkj"));		//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
						bsh.set(rsl.getString("zhib")+"С������",	Shoumjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
						
						Strtmp+="'"+rsl.getString("zhib")+"',";					//��¼�û����õ�Ӱ����㵥�۵�ָ��
//						�������ۿ����÷�Χ������
//							ԭ���Ƚ����ۿ��������Ϣ��¼��һ��StringBuffer�����У���ʽΪ:Qnetar,1;��������,1;
//							(ע�����shiyfwΪ1��Ϊ�ǳ����������ã��ż�¼�������0����������ȫ�����ݣ����ü�¼) 
//							ʹ��ʱ�������StringBufffer
						if(rsl.getInt("shiyfw")>0){
							
//							���÷�ΧΪ1 ˵��ֻ�Գ������ֽ��в���
							sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
						}
					}
				}
			}else{
//				Falg=false ˵���Ǵ����������ۿ���㣬��ʱ���ָ�겻�����⴦�������� Ӧ������
				while(rsl.next()){

					if(Tsclzb_item[1].equals(rsl.getString("zhib"))){

//						�õ����ۿ�ָ��ֵ
						Dbltmp=Double.parseDouble(Tsclzb_item[Tsclzb_item.length-2]);
					
//						�����¼������ۿ�����⴦���ָ��������־
						Shoumjsdcz.Mark_Tsclzbs_bz(bsv.getTsclzbs(),Tsclzb_item[0]+","+rsl.getString("zhib"));
					
//						�õ�������Ŀ�Ľ����׼(ֻ�����ڼ�Ȩƽ���Ĳ�����Ŀ)
						Dblczxm=Shoumjsdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");
					
//						ָ��Ľ���ָ��
						Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
						Dblczxm=Shoumjsdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
					
						if(Dbltmp>=Shoumjsdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
								&&Shoumjsdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
								&&!Shoumjsdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"))	//�����ָ����Ҫ���⣨�����Σ����������μ�ͳһ�����ۿ�ļ���
						){
							
							//ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
							Strimplementedzb = rsl.getString("zhib");	//��¼��ʹ�õ�ָ��
							Dblimplementedzbsx = rsl.getDouble("shangx");
							
							bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
							bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));			//ָ�굥λ�������޵�λ��
							bsh.set(rsl.getString("zhib")+"���ۿ�����", 	rsl.getString("tiaoj"));		//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
							bsh.set(rsl.getString("zhib")+"��������",	rsl.getDouble("zengfj"));		//������
							bsh.set(rsl.getString("zhib")+"�۸�����",	rsl.getDouble("kouj"));			//�ۼ�
							bsh.set(rsl.getString("zhib")+"�����۵�λ", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//���۵�λ
							bsh.set(rsl.getString("zhib")+"�۸��۵�λ", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//�ۼ۵�λ
							bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
							bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
							bsh.set(rsl.getString("zhib")+"���ۿ����",	rsl.getDouble("jis"));			//������ÿ����xx�򽵵�xx��
							bsh.set(rsl.getString("zhib")+"���ۿ������λ",	rsl.getString("jisdw"));	//������λ
							bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	rsl.getDouble("jizzkj"));		//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
							bsh.set(rsl.getString("zhib")+"С������",	Shoumjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
							
							Strtmp+="'"+rsl.getString("zhib")+"',";					//��¼�û����õ�Ӱ����㵥�۵�ָ��
							
//							��Ϊ���ָ��ֵӦ�������ۿ����
							bsv.setTsclzbzkksfxyjs(true);
							
//							�������ۿ����÷�Χ������
//								ԭ���Ƚ����ۿ��������Ϣ��¼��һ��StringBuffer�����У���ʽΪ:Qnetar,1;��������,1;
//								(ע�����shiyfwΪ1��Ϊ�ǳ����������ã��ż�¼�������0����������ȫ�����ݣ����ü�¼) 
//								ʹ��ʱ�������StringBufffer
							
							if(rsl.getInt("shiyfw")>0){
								
//								���÷�ΧΪ1 ˵��ֻ�Գ������ֽ��в���
								sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
							}
						}
					}
				}
			}
			
			bsv.setMeikzkksyfw(sb);	//��¼ú�����ۿ������÷�ΧΪ�������ֵ�����
			
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
			
			if(!Strtmp.equals("")&&bsv.getYifzzb().equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ("+Strtmp.substring(0,Strtmp.lastIndexOf(","))+") and leib=1 ";
			}else if(!Strtmp.equals("")&&!bsv.getYifzzb().equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ("+Strtmp+"'"+bsv.getYifzzb()+"') and leib=1 ";
			}else if(!bsv.getYifzzb().equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ('"+bsv.getYifzzb()+"') and leib=1 ";
			}else{
				
				sql="select distinct bianm as zhib from zhibb where leib=1 ";
			}
			
			rsl=con.getResultSetList(sql);
			
			String Strtmpdw="";
			
			while(rsl.next()){
				
					
					Strtmpdw=Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),"");
					
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
					bsh.set(rsl.getString("zhib")+"���ۿ������λ",	"");				//������λ
					bsh.set(rsl.getString("zhib")+"��׼���ۼ�",	0);						//��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
					bsh.set(rsl.getString("zhib")+"С������",	"");					//С������ÿ����xx�򽵵�xx��
				
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
						+ " and meikxxb_id="+bsv.getMeikxxb_Id();
					
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
	
	private void CountYf(long Hetb_id,double Shangcjsl){
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
				bsh.set("�˼����", bsv.getYunju_js());
				
				if(!bsv.getGongs_Yf().equals("")){
					
//					�˷Ѻ�˰���۱���С��λ
					bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
					
					getZengkk_yf(Hetb_id,bsh);	//�����κ�����ֻ�ǹ�ʽ�ϲ�����
//					���˷�
					bsh.eval(bsv.getGongs_Yf());
//						bsv.setYunfjsdj(Double.parseDouble(bsh.get("�˷ѽ��㵥��").toString()));
					setJieszb(bsh,1,Shangcjsl);
						
				}else{
					
					bsv.setErroInfo("�������˷Ѽ��㹫ʽ");
					return;
				}
			}else{
				
//				ȡhetys�е��˼�
				JDBCcon con=new JDBCcon();
				String sql=" select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
							+ " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n" 
							+ " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
							+ " danwb yjdw										\n"
							+ " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id	\n" 
							+ " and jg.tiaojb_id=tj.id(+)						\n"  
							+ " and jg.danwb_id=dw.id(+)						\n" 
							+ " and jg.yunjdw_id=yjdw.id(+)						\n"
							+ " and ht.id="+Hetb_id+" and jg.meikxxb_id="+bsv.getMeikxxb_Id()+"	";
				
				ResultSetList rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
//					����Ƿ��н���۸񷽰��������˵�������ⷽʽ���㣬����ԭ���ĺ�ͬ���ۿ���ʽ
					if(rsl.getLong("yunsjgfab_id")>0){
//						�����˷ѽ��㷽���������㷨
						bsh.set("��ͬ�˼�", 0);
						bsh.set("��ͬ�˼۵�λ", Locale.yuanmd_danw);
						bsh.set("�˼����", 0);
						
//						�����˷Ѽ۸���ϸ�е�����
						bsh=Shoumjsdcz.CountYfjsfa(bsv.getMeikxxb_Id(),bsv.getDiancxxb_id(),bsv.getFaz_Id(),
								bsv.getDaoz_Id(),rsl.getLong("yunsjgfab_id"),bsh);
						
						if(Double.parseDouble(bsh.get("��ͬ�˼�").toString())==0){
							
							bsv.setErroInfo("û�к�����۸񷽰�ƥ������ݣ�");
							return;
						}
						
//						������ۿ�
						getZengkk_yf(Hetb_id,bsh);
						
//						�˷Ѻ�˰���۱���С��λ
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						bsh.eval(bsv.getGongs_Yf());
						setJieszb(bsh,1,Shangcjsl);
						
					}else{
//						û�н���۸񷽰�����ԭ���ļ۸���������ۿ�������н���
						if(rsl.getRows()==1){
//							��һ����ͬ�۸�
							bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
							bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
							bsh.set("�˼����", bsv.getYunju_js());
							
							if(rsl.getDouble("yunja")==0){
								
								bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
								return;
							}
//							������ۿ�
							getZengkk_yf(Hetb_id,bsh);
							
//							�˷Ѻ�˰���۱���С��λ
							bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
							
							bsh.eval(bsv.getGongs_Yf());
							setJieszb(bsh,1,Shangcjsl);
							
						}else{
//							�����ͬ�۸�
							double shangx=0;
							double xiax=0;
							double yunju=bsv.getYunju_cf();	//�˾�
							double Dbltmp=0;
							
							do{
								shangx=rsl.getDouble("shangx");
								xiax=rsl.getDouble("xiax");
								
								Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								
								Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
								
								if(yunju>=xiax&&yunju<=(shangx==0?1e308:shangx)){
									
									bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
									bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
									bsh.set("�˼����", bsv.getYunju_js());
									
									if(rsl.getDouble("yunja")==0){
										
										bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
										return;
									}
									
									getZengkk_yf(Hetb_id,bsh);
									
//									�˷Ѻ�˰���۱���С��λ
									bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
									
									bsh.eval(bsv.getGongs_Yf());
									setJieszb(bsh,1,Shangcjsl);
								}
								
							}while(rsl.next());
						}
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
				+ " 	jis,jsdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,			\n"
				+ " 	xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,nvl(canzsx,0) as canzsx,	\n"
				+ "		nvl(canzxx,0) as canzxx			\n"
				+ " 	from hetys ht,hetyszkkb zkk,zhibb zb,tiaojb tj,danwb dw,danwb jsdw,danwb kjdw,				\n"
				+ " 		danwb zfjdw,zhibb czxm,danwb czxmdw														\n"
				+ " 		where ht.id=zkk.hetys_id and zkk.zhibb_id=zb.id and zkk.tiaojb_id=tj.id					\n"
                + "  			and zkk.danwb_id=dw.id and zkk.jisdwid=jsdw.id(+) and zkk.koujdw=kjdw.id(+)			\n"
                + "  			and zkk.zengfjdw=zfjdw.id(+) and zkk.canzxm=czxm.id(+) and zkk.canzxmdw=czxmdw.id(+)	\n"
                + " 			and ht.id="+Hetb_id+" order by zb.bianm,tj.bianm,xiax,shangx ";
			ResultSetList rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//��¼ָ�����ֵ
			String  Strtmp="";		//��¼�趨��ָ��
			double Dblczxm=0;		//��¼������Ŀ��ֵ
			String Strimplementedzb="";	//��¼�Ѿ�ִ�й���ָ�꣨���Ѿ������ִ�е�ָ�꣩��
			double Dblimplementedzbsx=0;	//��¼��ִ�й���ָ�������
			
			while(rsl.next()){
				
				Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
				Dblczxm=Shoumjsdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");
//				ָ��Ľ���ָ��
				Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
				Dblczxm=Shoumjsdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
				
				if(Dbltmp>=Shoumjsdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
						&&Shoumjsdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
				
				){
					
					Strimplementedzb = rsl.getString("zhib");
					Dblimplementedzbsx = rsl.getDouble("shangx");
					
					bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
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
					bsh.set(rsl.getString("zhib")+"С������",	Shoumjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
					
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
				
					
					Strtmpdw=Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),"");
					
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
	
	//�����˷ѣ������ӷѣ���˰�۳��������˷�
	private boolean getYunFei(String SelIds,long Jieslx,long Hetb_id,double Shangcjsl){
	    JDBCcon con=new JDBCcon();
		try{
		    	
		    	String sql="";
		    	ResultSet rs=null;
		    	String sql_colum="";	//�����У������˷��ã�
		    	String sql_talbe="";	//���ӱ������˷��ã�
		    	long lngJieslx=0;
		    	long lngYunshtb_id=0;
		    	lngJieslx=Jieslx;
		    	
		    	if(lngJieslx==Locale.liangpjs_feiylbb_id||lngJieslx==Locale.guotyf_feiylbb_id){
		    		
//		    		��Ʊ���㡢�����˷�
		    		sql_colum=",decode(kuangqyf,null,0,kuangqyf) as kuangqyf,	\n"
		    				+ "decode(kuangqzf,null,0,kuangqzf) as kuangqzf		\n";
		    		
		    		sql_talbe=" ,																							\n"            
				        	+ "(select sum(zhi) as kuangqyf from 	\n" 
		    				+ " (select distinct feiyb.*,yunfdjb.id  																	\n"
				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"  
				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"  
				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id 														\n"  
				        	+ " and feiylbb.id="+Locale.kuangqyf_feiylbb_id+" and feiyb.shuib=1 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"  
				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+"))))		\n"
				        	+ " ,																							\n"            
				        	+ "(select sum(zhi) as kuangqzf from 	\n"
				        	+ " (select distinct feiyb.*,yunfdjb.id  																	\n"
				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"  
				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"  
				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id														\n"  
				        	+ " and feiylbb.id="+Locale.kuangqyf_feiylbb_id+" and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"  
				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+"))))		\n";
		    		
		    		lngJieslx=Locale.guotyf_feiylbb_id;
		    	}
		    	
		    		sql=" select decode(tielyf,null,0,tielyf) as tielyf,												\n"
			        	+ " decode(tielzf,null,0,tielzf) as tielzf														\n"
			        	+ sql_colum
			        	+ " from	"
			        	+ "(select sum(zhi) as tielyf from																\n" 
			        	+ "(select distinct feiyb.*,yunfdjb.id																		\n" 
			        	+ " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n" 
			        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n" 
			        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id 														\n"  
			        	+ " and feiylbb.id="+lngJieslx+" and feiyb.shuib=1 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"  
			        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+"))))		\n"
			        	+ " ,																							\n"            
			        	+ "(select sum(zhi) as tielzf from			\n"
			        	+ " (select distinct feiyb.*,yunfdjb.id  																\n"
			        	+ " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"  
			        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"  
			        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"  
			        	+ " and feiylbb.id="+lngJieslx+" and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"  
			        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+"))))		\n"
			        	+ sql_talbe;
			        
				    rs=con.getResultSet(sql);
				    
				    if (rs.next()){
//				        ��Ʊ���㣨��·����yunfdjb,danjcpb��ȡֵ��ǰ����Ҫ�Ƚ��л�Ʊ�˶ԣ�
//				    	yunf��·��Ʊ�����еķ���
//				    	yunfzf��·��Ʊ�����в��ɵ�˰�ķ���
//				    	��Ʊ���������˷�ʱ��
				    	bsv.setTielyf(rs.getDouble("tielyf"));
				    	bsv.setTielzf(rs.getDouble("tielzf"));
				    	
				    	if(lngJieslx==Locale.liangpjs_feiylbb_id
				    			||lngJieslx==Locale.guotyf_feiylbb_id){
				    		
//				    		��Ʊ���㡢�����˷�
				    		bsv.setKuangqyf(rs.getDouble("kuangqyf"));
					    	bsv.setKuangqzf(rs.getDouble("kuangqzf"));
				    	}
				    }
				    
				    if(bsv.getTielyf()==0&&bsv.getTielzf()==0&&bsv.getKuangqyf()==0&&bsv.getKuangqzf()==0){
//				    	�����yunfdjb,danjcpb��ȡֵ������˵���Ǵ�ú����л����˷Ѻ�ͬ��ȡ��
				    	
//				    	���1���������Ʊ���㡢�ȴ�ú���ͬ����ȡ�˷ѡ�
//				    			���û��ȡ���˷ѣ��ٴ�ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid,
//				    			�ٸ����˷Ѻ�ͬ�е�ú��ȡ������۸�
				    	
//				    	���2������ǵ������˷ѣ���ôHetb_id�����˷Ѻ�ͬ��id,
//				    			�ٸ����˷Ѻ�ͬ�е�ú��ȡ������۸�
				    	
//				    	bsv.setYunfjsl(bsv.getGongfsl());
				    	if(Jieslx==Locale.liangpjs_feiylbb_id){
//				    		��Ʊ���㣬���1
				    		
				    		if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//			    				���ú���ͬ�����˷������
				    			
			    				CountYf(0,Shangcjsl);
			    			}else{
//			    				���ú���ͬ��û���˷ѣ���ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid
			    				lngYunshtb_id=getYunshtb_id(Hetb_id);
			    				
			    				if(lngYunshtb_id>0){
//			    					˵�������ҵ���Ӧ���˷Ѻ�ͬ��
			    					CountYf(lngYunshtb_id,Shangcjsl);
			    				}else if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//			    					�����۽���,�������Ʊ�����˷ѣ������ǵ����ۺ�ͬ�Ļ����������˷�
			    					
			    				}else{
//			    					����۽���
			    					bsv.setErroInfo("ú���ͬ<"+Shoumjsdcz.getHetbh(bsv.getHetb_Id())+">û�ж�Ӧ���˷Ѻ�ͬ�������ã�");
			    					return false;
			    				}
			    			}
				    	}else if(Jieslx!=Locale.meikjs_feiylbb_id){
//				    		�����˷ѽ���(���������˷�)
				    		CountYf(bsv.getHetb_Id(),Shangcjsl);
				    	}
				    }else{
//				    	��yunfdjb,danjcpb��ȡ�����ݣ�˵����ͨ���˶Ի�Ʊ���ɵ��˷ѣ��˷ѽ�����Ϊbiaoz
//				    	����ǵ�������˷ѣ�������ԴҲ��yunfdjb,danjcpb,��ô��Ϊ��������ҲΪbiaoz
				    	bsv.setYunfjsl(bsv.getGongfsl());
				    	if(lngJieslx==Locale.guotyf_feiylbb_id){
				    		
				    		bsv.setJiessl(bsv.getGongfsl());
				    	}
				    }
				    
////		    		һ�ڼ��˷�Դ��ú��
//		    		if(bsv.getYikj_yunfyymk().equals("��")){
//		    			
//		    			sql=" select decode(tielyf,null,0,tielyf) as tielyf,												\n"
//				        	+ " decode(tielzf,null,0,tielzf) as tielzf														\n"
//				        	+ " from																						\n" 
//				        	+ "(select sum(zhi) as tielyf																	\n" 
//				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb															\n" 
//				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n" 
//				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"  
//				        	+ " and feiylbb.mingc='"+Locale.guotyf_feiylbb+"' and feiyb.shuib=1 and danjcpb.chepb_id in		\n"  
//				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+")))		\n"
//				        	+ " ,																							\n"            
//				        	+ " (select sum(zhi) as tielzf  																\n"
//				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb															\n"  
//				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"  
//				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"  
//				        	+ " and feiylbb.mingc='"+Locale.guotyf_feiylbb+"' and feiyb.shuib=0 and danjcpb.chepb_id in		\n"  
//				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+")))";
//		    		}else{
//		    			
//		    			if(bsv.getHetjgb_id()>0){
////		    				�Ѿ��к�ͬ�۸��
//		    				
//		    				sql="select hj.yunj as qiyj	from hetjgb hj where hj.id="+bsv.getHetjgb_id();
//		    			}else{
//		    				
//		    				sql="select hj.yunj as qiyj	from hetjgb hj,hetb ht where hj.hetb_id=ht.id and ht.id="+Hetb_id;
//		    			}
//		    		}
//		    		
//		    		rs=con.getResultSet(sql);
//		    		
//		    		if(rs.next()){
//		    			
//		    			bsv.setTielyf((double)CustomMaths.Round_new(rs.getDouble("qiyj")*bsv.getYunfjsl(),2));
//		    		}
		    	rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return true;
	}
	
	private void setJieszb(Interpreter bsh,int Type,double Shangcjsl){
//		�˷�����Jiesdcz.java�л���һ�����Ƶķ���������޸Ĵ˷�����Ҫ��ͬ�Ǹ�����һ���޸�
		try {
			
//			Type	0:ú�����
//					1:�˷ѽ���
			if(Type==0||(Type==1&&bsv.getJieslx()==Locale.guotyf_feiylbb_id)){
//				�����ú��������Ʊ�������ú��ʱ���Խ��и�ֵ������ֻ���ڵ��������˷�ʱ�ſɸ�ֵ
				
	//			�������ۿ�ȡֵ
				bsv.setShul_ht(bsh.get("��ͬ��׼_��������").toString());
				bsv.setShul_yk(Double.parseDouble(bsh.get("ӯ��_��������").toString()));
				bsv.setShul_zdj(Double.parseDouble(bsh.get("�۵���_��������").toString()));
				
	//			Qnetar
				bsv.setQnetar_ht(bsh.get("��ͬ��׼_Qnetar").toString());
				bsv.setQnetar_yk(Double.parseDouble(bsh.get("ӯ��_Qnetar").toString()));
				bsv.setQnetar_zdj(Double.parseDouble(bsh.get("�۵���_Qnetar").toString()));
				
	//			Std
				bsv.setStd_ht(bsh.get("��ͬ��׼_Std").toString());
				bsv.setStd_yk(Double.parseDouble(bsh.get("ӯ��_Std").toString()));
				bsv.setStd_zdj(Double.parseDouble(bsh.get("�۵���_Std").toString()));
				
	//			Ad
				bsv.setAd_ht(bsh.get("��ͬ��׼_Ad").toString());
				bsv.setAd_yk(Double.parseDouble(bsh.get("ӯ��_Ad").toString()));
				bsv.setAd_zdj(Double.parseDouble(bsh.get("�۵���_Ad").toString()));
				
	//			Vdaf
				bsv.setVdaf_ht(bsh.get("��ͬ��׼_Vdaf").toString());
				bsv.setVdaf_yk(Double.parseDouble(bsh.get("ӯ��_Vdaf").toString()));
				bsv.setVdaf_zdj(Double.parseDouble(bsh.get("�۵���_Vdaf").toString()));
				
	//			Mt
				bsv.setMt_ht(bsh.get("��ͬ��׼_Mt").toString());
				bsv.setMt_yk(Double.parseDouble(bsh.get("ӯ��_Mt").toString()));
				bsv.setMt_zdj(Double.parseDouble(bsh.get("�۵���_Mt").toString()));
				
	//			Qgrad
				bsv.setQgrad_ht(bsh.get("��ͬ��׼_Qgrad").toString());
				bsv.setQgrad_yk(Double.parseDouble(bsh.get("ӯ��_Qgrad").toString()));
				bsv.setQgrad_zdj(Double.parseDouble(bsh.get("�۵���_Qgrad").toString()));
				
	//			Qbad
				bsv.setQbad_ht(bsh.get("��ͬ��׼_Qbad").toString());
				bsv.setQbad_yk(Double.parseDouble(bsh.get("ӯ��_Qbad").toString()));
				bsv.setQbad_zdj(Double.parseDouble(bsh.get("�۵���_Qbad").toString()));
				
	//			Had
				bsv.setHad_ht(bsh.get("��ͬ��׼_Had").toString());
				bsv.setHad_yk(Double.parseDouble(bsh.get("ӯ��_Had").toString()));
				bsv.setHad_zdj(Double.parseDouble(bsh.get("�۵���_Had").toString()));
				
	//			Stad
				bsv.setStad_ht(bsh.get("��ͬ��׼_Stad").toString());
				bsv.setStad_yk(Double.parseDouble(bsh.get("ӯ��_Stad").toString()));
				bsv.setStad_zdj(Double.parseDouble(bsh.get("�۵���_Stad").toString()));
				
	//			Star
				bsv.setStar_ht(bsh.get("��ͬ��׼_Star").toString());
				bsv.setStar_yk(Double.parseDouble(bsh.get("ӯ��_Star").toString()));
				bsv.setStar_zdj(Double.parseDouble(bsh.get("�۵���_Star").toString()));
				
	//			Mad
				bsv.setMad_ht(bsh.get("��ͬ��׼_Mad").toString());
				bsv.setMad_yk(Double.parseDouble(bsh.get("ӯ��_Mad").toString()));
				bsv.setMad_zdj(Double.parseDouble(bsh.get("�۵���_Mad").toString()));
				
	//			Aar
				bsv.setAar_ht(bsh.get("��ͬ��׼_Aar").toString());
				bsv.setAar_yk(Double.parseDouble(bsh.get("ӯ��_Aar").toString()));
				bsv.setAar_zdj(Double.parseDouble(bsh.get("�۵���_Aar").toString()));
				
	//			Aad
				bsv.setAad_ht(bsh.get("��ͬ��׼_Aad").toString());
				bsv.setAad_yk(Double.parseDouble(bsh.get("ӯ��_Aad").toString()));
				bsv.setAad_zdj(Double.parseDouble(bsh.get("�۵���_Aad").toString()));
				
	//			Vad
				bsv.setVad_ht(bsh.get("��ͬ��׼_Vad").toString());
				bsv.setVad_yk(Double.parseDouble(bsh.get("ӯ��_Vad").toString()));
				bsv.setVad_zdj(Double.parseDouble(bsh.get("�۵���_Vad").toString()));
				
	//			St
				bsv.setT2_ht(bsh.get("��ͬ��׼_T2").toString());
				bsv.setT2_yk(Double.parseDouble(bsh.get("ӯ��_T2").toString()));
				bsv.setT2_zdj(Double.parseDouble(bsh.get("�۵���_T2").toString()));
				
	//			�˾�
				bsv.setYunju_ht(bsh.get("��ͬ��׼_�˾�").toString());
				bsv.setYunju_yk(Double.parseDouble(bsh.get("ӯ��_�˾�").toString()));
				bsv.setYunju_zdj(Double.parseDouble(bsh.get("�۵���_�˾�").toString()));
			}
			//���㵥��
			if(Type==0){
				
				bsv.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));
			}else if(Type==1){
				
				if(bsv.getJieslx()==Locale.guotyf_feiylbb_id){
					
				    bsv.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));
				    bsv.setJiessl(bsv.getYunfjsl());
				}
				
				bsv.setYunfjsdj(Double.parseDouble(bsh.get("����۸�").toString()));
//				����ǷǺ˶Ի�Ʊ��ʽ�����˷ѽ��㣬��Ϊ�˷ѵ��۸�ֵ��ֱ�Ӽ�����˷Ѻϼ�
				bsv.setTielyf((double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2));
				
			}
			if(!bsv.getShangcjslct_Flag()){
				
				bsv.setJiessl(CustomMaths.sub(bsv.getJiessl(), Shangcjsl)); //���������ۼۺ��ϴν�����ɾ������Ϊ���ν�����
				bsv.setShangcjslct_Flag(true);
			}
			
		} catch (EvalError e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void computMlj(Interpreter bsh,ResultSetList rsl,Shoumjsdcz Jscz,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
//		Ϊ����Ŀ¼�۸�ֵ
		try {
			double	Dbljijzb=0;
			
			bsh.set("��ֵ����_"+Locale.Qnetar_zhibb, 0);
			bsh.set(Locale.Qnetar_zhibb+"_����", 0);
			bsh.set(Locale.Qnetar_zhibb+"_����", 0);
			bsh.set("�ӷ��ݱȼ�_"+Locale.Vdaf_zhibb, 0);
			bsh.set(Locale.Vdaf_zhibb+"_����", 0);
			bsh.set(Locale.Vdaf_zhibb+"_����", 0);
			bsh.set("��ֱȼ�_"+Locale.Std_zhibb, 0);
			bsh.set(Locale.Std_zhibb+"_����", 0);
			bsh.set(Locale.Std_zhibb+"_����", 0);
			bsh.set("��ֱȼ�_"+Locale.Stad_zhibb, 0);
			bsh.set(Locale.Stad_zhibb+"_����", 0);
			bsh.set(Locale.Stad_zhibb+"_����", 0);
			bsh.set("�ҷֱȼ�_"+Locale.Aar_zhibb, 0);
			bsh.set(Locale.Aar_zhibb+"_����", 0);
			bsh.set(Locale.Aar_zhibb+"_����", 0);
			bsh.set("�ҷֱȼ�_"+Locale.Aad_zhibb, 0);
			bsh.set(Locale.Aad_zhibb+"_����", 0);
			bsh.set(Locale.Aad_zhibb+"_����", 0);
			
			do {
				
				Dbljijzb=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
				Dbljijzb=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
				
				if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))){
					
					if(Shoumjsdcz.CheckMljRz(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", Shoumjsdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))));
						
						bsh.set(rsl.getString("zhib")+"_����", Shoumjsdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));
						
						bsh.set("��ֵ����_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}	
					if(Shoumjsdcz.CheckMljHff(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("�ӷ��ݱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Shoumjsdcz.CheckMljLiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("��ֱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Shoumjsdcz.CheckMljHiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("�ҷֱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					
					bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
					bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
					
					bsv.setHetjgpp_Flag(true);
				}
				
			}while(rsl.next());
			
			bsh.set("Ʒ�ֱȼ�", Jscz.getMljPzbj(bsv.getRanlpzb_Id()));
			
			//	�����ԼӼ�
			bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Shoumjsdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id,Locale.zhengcxjj_jies, "0")));
					
			//	�Ӽ�
			bsh.set(Locale.jiaj_jies, Double.parseDouble(Shoumjsdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id,Locale.jiaj_jies, "0")));
			
		}catch(EvalError e){
			
			e.printStackTrace();
		}	
	}
	
	//������ü�Ȩ
	private void computData(String selIds, long hetb_id, double shangcjsl){
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
		double _Qnetarzje=0;
		double _Stdzje=0;
		double _Adzje=0;
		double _Vdafzje=0;
		double _Mtzje=0;
		double _Qgradzje=0;
		double _Qbadzje=0;
		double _Hadzje=0;
		double _Stadzje=0;
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		double _Yunjuzje=0;
		double _Starzje=0;
		
		double _Meikzkktzsj[]=null;
		boolean _Iszksjtz=false; //�ж��Ƿ��Ѿ����������ۿ�����ݵ�����
		
		Danjsmk_dcjcl(1,selIds,hetb_id,shangcjsl);
		
		if(!bsv.getMeikzkksyfw().equals("")
				&&bsv.getMeikzkksyfw()!=null){
//			˵����Ҫ�������ۿ����Ŀ,Ŀǰֻ���������������ܼӼ۵�ҵ��
//			1���������ֶ�Ӧ���۵���
//			2�����������۽��
			
//			�����߼���
//			�ܽ�	(hansmj-�������ֵ��ۼ�)*��������+�������ֵ��ۼۡ���������
			_Meikzkktzsj=Shoumjsdcz.Zengkktz(bsv);
		}
		//�۸������
//		2008-12-9zsj�ӣ�
//		�߼���	�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//					��������Ϊ��һ�����������һ�֣�
//						���һ��
		
//							��˰��ú��=�����ۡ�ú���������-�˷Ѻ�˰���ۡ��˷ѽ�������
//							��˰���˷�=�˷Ѻ�˰���ۡ��˷ѽ�������
//							���������ڽ������������ӡ�һ�ڼ�(�˷�Դ��ú��)�������ã�Ĭ��ֵ���񡱣����ֵΪ���ǡ�
//									���մ��������
//						�������
		
//							ú�˰����=�������ú�˰����-��ͬ�۸��еĺ�˰�˷ѵ���
//							��˰�˷ѵ���=��ͬ�۸��еĺ�˰�˷ѵ���
//							ͬʱҪ����Hansmj
//							��������ϵͳĬ�ϣ��������ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//									�ҽ��������С�һ�ڼ�(�˷�Դ��ú��)��ֵΪ���񡱣����մ��������
		
//						
		//			�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸����˷Ѻ�˰���۴���0�����˷Ѽ۸�λ�����ڡ���
					
//					if(bsv.getHetyjdw().equals(Locale.yuanmd_danw)){
//		//				����˷ѵ��۵�λ=��Ԫ/�֡�
//		//					����С���Ĵ����������ݽ�������"ú�˰���۱���С��λ"
////						ԭ��
//		//					1������ú���ǵ������˷ѻ����������⣬���ú���ǲ���˰�ģ��������������⣬���Ȳ��迼�ǣ���Ϊ�˷Ѻ�ú������ͬ�ֵ�������
////							2�����ڷֹ�˾�Ӽ����⣬��Ϊ�Ӽ۶�Ϊ��˰�ۡ�
////								���ú��Ϊ��˰�ۣ���˰����=��˰����+�ֹ�˾�Ӽ�
////								���ú��Ϊ����˰�ۣ���˰����=����˰���ۡ���1+˰�ʣ�+�ֹ�˾�Ӽ�
//						
////						���ۿ�������ŵ����紦��
////						if(_Meikzkktzsj!=null){
////							
//////							1���������ֶ�Ӧ���۵���
//////							2�����������۽��
//////							(hansmj-�������ֵ��ۼ�)*��������+���������۽��
////							_Jiashj=(_Hansmj-_Meikzkktzsj[0])*_Jiessl+_Meikzkktzsj[1];
////							if(bsv.getJijlx()==0){
//////								��˰����
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,bsv.getMeikhsdjblxsw());
////							}else if(bsv.getJijlx()==1){
//////								����˰
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,7);
////							}
////						}
//						
//						_Hansmj=(double)CustomMaths.Round_new(_Hansmj-bsv.getHetyj(), bsv.getMeikhsdjblxsw());
//					}
		//			else if(bsv.getHetyjdw().equals(Locale.dun_danw)){
		////				����˷ѵ��۵�λ=���֡�
		////				����С���Ĵ����������ݽ�������"������������С��λ"
		//				_Jiessl=(double)CustomMaths.Round_new(_Jiessl-bsv.getHetyj(), Integer.parseInt(bsv.getJiesslblxs()));
		//				bsv.setJiessl(_Jiessl);
		//			}
		
//		����ָ�����⴦��ʵ���߼�
		
		double jieszjecj=0;	//�����ܽ���ۣ�����ָ�����ۿ��۽�������ɺ�Ҫ��jieszje��Ҫ������Ǯ��
//								jieszje�п����Ǻ�˰��Ҳ�п����ǲ���˰�ģ�����Jijlx������
		
		if(bsv.getTsclzbs()!=null){
//			��ʱ��Tsclzbs����������ָ����ۼ���Ϣ
//			������Ԫ�����У�ָ�����,ָ���۵���,�ۼ�����,�۽��
			String tmp[]=null;
			String zhibbm="";
			double zhibzje=0;
			double zhibzdj=0;		
			double zhibjsbz=0;		//����ָ��Ľ����׼(�����ν���ָ���ֵ)
			double zhibjsbzdysl=0;	//����ָ������׼��Ӧ������
			double zhibjsbzjqz=0;	//����ָ������׼�ļ�Ȩֵ������ָ��Ľ����׼������ָ������׼��Ӧ��������
			
			for(int i=0;i<bsv.getTsclzbs().length;i++){
				
				tmp = bsv.getTsclzbs()[i].split(",");
				
				if(zhibbm.equals(tmp[0])){
//						ͬһ��ָ��
//						���¼����۽��
					zhibzje = CustomMaths.add(zhibzje,Double.parseDouble(tmp[tmp.length-1]));
//						���¼����۵���
					zhibzdj = CustomMaths.Round_new(zhibzje/bsv.getJiessl(),bsv.getMeikzkkblxsw());
//					
//						�����ν���ָ���ֵ
					zhibjsbz = Double.parseDouble(tmp[1]);
//						�����׼��Ӧ������
					zhibjsbzdysl = CustomMaths.add(zhibjsbzdysl,Double.parseDouble(tmp[3]));
//						�ۼӽ����׼�ļ�Ȩֵ
					zhibjsbzjqz = CustomMaths.add(zhibjsbzjqz, CustomMaths.mul(zhibjsbz, Double.parseDouble(tmp[3])));
//						��ָ�긳ֵ
					Shoumjsdcz.setJieszbzdj_Tszb(zhibbm,bsv,zhibzdj,CustomMaths.Round_new(CustomMaths.div(zhibjsbzjqz, zhibjsbzdysl),bsv.getMeikzkkblxsw()),zhibzje);
					
				}else{
//						����һ�β���ͬһ��ָ��
					zhibbm = tmp[0];
//						����۽��
					zhibzje = Double.parseDouble(tmp[tmp.length-1]);
//						����۵���
					zhibzdj = Double.parseDouble(tmp[2]);
//						�����ν���ָ���ֵ
					zhibjsbz = Double.parseDouble(tmp[1]);
//						�����׼��Ӧ������
					zhibjsbzdysl = Double.parseDouble(tmp[3]);
//						�����׼�ļ�Ȩֵ
					zhibjsbzjqz = CustomMaths.mul(zhibjsbz, zhibjsbzdysl);
//						��ָ�긳ֵ
					Shoumjsdcz.setJieszbzdj_Tszb(zhibbm,bsv,zhibzdj,zhibjsbz,zhibzje);
				}
//				���������ۿ���ۼ۽���¼����
				jieszjecj = zhibzje;
			}
		}
	
//			����ֹ�˾�Ӽۡ��Ͳ���˰���ۼ���
		if(bsv.getJijlx()==0){
//					��˰����
//					if(Meikzkktzsj!=null){
////						˵���в������ܼӼ۵����
////						1���������ֶ�Ӧ���۵���
////						2�����������۽��
////						(hansmj-�������ֵ��ۼ�)*��������+���������۽��
//						
//					}
			
			if(jieszjecj==0){
//				û������ָ�굥������
				
				bsv.setJiajqdj(_Hansmj);										//����Ӽ�ǰ����
				_Hansmj=_Hansmj+bsv.getFengsjj();								//���Ϸֹ�˾�Ӽ�
				bsv.setHansmj(_Hansmj);											//���º�˰����
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//��˰�ϼ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//�ۿ�ϼ�
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//�ۿ�˰��
				_Jine=_Jiakhj;													//���
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
			}else{
//				������ָ�굥������
				
				_Jiashj = (double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//��˰�ϼ�
				_Jiashj = (double)CustomMaths.Round_new(CustomMaths.add(_Jiashj,jieszjecj),2);			//���㲻���ֹ�˾�Ӽ۵ļ�˰�ϼ�
				_Hansmj = (double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());	//���Ƽ�Ǯǰ��˰����
				bsv.setJiajqdj(_Hansmj);
				_Jiashj = (double)CustomMaths.Round_new(_Jiashj
							+(double)CustomMaths.Round_new(CustomMaths.mul(bsv.getFengsjj(), _Jiessl),2),2);									//j��������ָ�����ۿ���˰�ϼ�
				_Hansmj= (double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());	//���ƺ�˰����
				bsv.setHansmj(_Hansmj);											//���º�˰����
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//�ۿ�ϼ�
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//�ۿ�˰��
				_Jine=_Jiakhj;													//���
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
			}
			
			
		}else if(bsv.getJijlx()==1){
//						�������ͣ�0����˰��1������˰��
//						����˰
//					bsv.setJiajqdj(_Hansmj);			
			
			if(jieszjecj==0){
//				û������ָ�굥������
				
				_Buhsmj=_Hansmj;
				_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
//						����Ӽ�ǰ��˰����
				_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setJiajqdj(_Hansmj);
//						����Ӽ�ǰ��˰����_end
				
				_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2)
						+(double)CustomMaths.Round_new(bsv.getFengsjj()*bsv.getJiessl(),2),2);	//����ֹ�˾�Ӽ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);
				_Jine=_Jiakhj;
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);	
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setHansmj(_Hansmj);
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
			}else{
//				������ָ�굥������
				
				_Buhsmj=_Hansmj;
				_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
				_Jiakhj=CustomMaths.add(_Jiakhj,jieszjecj);										//�������⴦���ָ���۽��
				_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setJiajqdj(_Hansmj);	//����Ӽ�ǰ��˰����_end
				
				_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2)
						+(double)CustomMaths.Round_new(bsv.getFengsjj()*bsv.getJiessl(),2),2);	//����ֹ�˾�Ӽ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);
				_Jine=_Jiakhj;
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);	
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setHansmj(_Hansmj);
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
			}
		}
				
		_Shulzjbz=_Hansmj;
		//�ϼ�
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//ָ���۵���
		
		double _Qnetar=bsv.getQnetar_zdj();
		double _Std=bsv.getStd_zdj();
		double _Ad=bsv.getAd_zdj();
		double _Vdaf=bsv.getVdaf_zdj();
		double _Mt=bsv.getMt_zdj();
		double _Qgrad=bsv.getQgrad_zdj();
		double _Qbad=bsv.getQbad_zdj();
		double _Had=bsv.getHad_zdj();
		double _Stad=bsv.getStad_zdj();
		double _Mad=bsv.getMad_zdj();
		double _Aar=bsv.getAar_zdj();
		double _Aad=bsv.getAad_zdj();
		double _Vad=bsv.getVad_zdj();
		double _T2=bsv.getT2_zdj();
		double _Shul=bsv.getShul_zdj();
		double _Yunju=bsv.getYunju_zdj();		//�˾��۵���
		double _Star=bsv.getStar_zdj();			//Star�۵���
		
		//����ӯ�����ۼ۽��
			
			_Qnetarzje=CustomMaths.add((double)CustomMaths.Round_new(_Qnetar*_Jiessl,0),bsv.getQnetar_zje_tscl());
			bsv.setQnetar_zje(_Qnetarzje);
			bsv.setQnetar_zdj(CustomMaths.Round_new(CustomMaths.div(_Qnetarzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
			_Stdzje=CustomMaths.add((double)CustomMaths.Round_new(_Std*_Jiessl,2),bsv.getStd_zje_tscl());
			bsv.setStd_zje(_Stdzje);
			bsv.setStd_zdj(CustomMaths.Round_new(CustomMaths.div(_Stdzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
			_Adzje=CustomMaths.add((double)CustomMaths.Round_new(_Ad*_Jiessl,2),bsv.getAd_zje_tscl());
			bsv.setAd_zje(_Adzje);
			bsv.setAd_zdj(CustomMaths.Round_new(CustomMaths.div(_Adzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
			_Vdafzje=CustomMaths.add((double)CustomMaths.Round_new(_Vdaf*_Jiessl,2),bsv.getVdaf_zje_tscl());
			bsv.setVdaf_zje(_Vdafzje);
			bsv.setVdaf_zdj(CustomMaths.Round_new(CustomMaths.div(_Vdafzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
			_Mtzje=CustomMaths.add((double)CustomMaths.Round_new(_Mt*_Jiessl,2),bsv.getMt_zje_tscl());
			bsv.setMt_zje(_Mtzje);
			bsv.setMt_zdj(CustomMaths.Round_new(CustomMaths.div(_Mtzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Qgradzje=CustomMaths.add((double)CustomMaths.Round_new(_Qgrad*_Jiessl,2),bsv.getQgrad_zje_tscl());
			bsv.setQgrad_zje(_Qgradzje);
			bsv.setQgrad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qgradzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Qbadzje=CustomMaths.add((double)CustomMaths.Round_new(_Qbad*_Jiessl,2),bsv.getQbad_zje_tscl());
			bsv.setQbad_zje(_Qbadzje);
			bsv.setQbad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qbadzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Hadzje=CustomMaths.add((double)CustomMaths.Round_new(_Had*_Jiessl,2),bsv.getHad_zje_tscl());
			bsv.setHad_zje(_Hadzje);
			bsv.setHad_zdj(CustomMaths.Round_new(CustomMaths.div(_Hadzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Stadzje=CustomMaths.add((double)CustomMaths.Round_new(_Stad*_Jiessl,2),bsv.getStad_zje_tscl());
			bsv.setStad_zje(_Stadzje);
			bsv.setStad_zdj(CustomMaths.Round_new(CustomMaths.div(_Stadzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
			
			_Starzje=CustomMaths.add((double)CustomMaths.Round_new(_Star*_Jiessl,2),bsv.getStar_zje_tscl());
			bsv.setStar_zje(_Starzje);
			bsv.setStar_zdj(CustomMaths.Round_new(CustomMaths.div(_Starzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
			
			_Madzje=CustomMaths.add((double)CustomMaths.Round_new(_Mad*_Jiessl,2),bsv.getMad_zje_tscl());
			bsv.setMad_zje(_Madzje);
			bsv.setMad_zdj(CustomMaths.Round_new(CustomMaths.div(_Madzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Aarzje=CustomMaths.add((double)CustomMaths.Round_new(_Aar*_Jiessl,2),bsv.getAar_zje_tscl());
			bsv.setAar_zje(_Aarzje);
			bsv.setAar_zdj(CustomMaths.Round_new(CustomMaths.div(_Aarzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Aadzje=CustomMaths.add((double)CustomMaths.Round_new(_Aad*_Jiessl,2),bsv.getAad_zje_tscl());
			bsv.setAad_zje(_Aadzje);
			bsv.setAad_zdj(CustomMaths.Round_new(CustomMaths.div(_Aadzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Vadzje=CustomMaths.add((double)CustomMaths.Round_new(_Vad*_Jiessl,2),bsv.getVad_zje_tscl());
			bsv.setVad_zje(_Vadzje);
			bsv.setVad_zdj(CustomMaths.Round_new(CustomMaths.div(_Vadzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_T2zje=CustomMaths.add((double)CustomMaths.Round_new(_T2*_Jiessl,2),bsv.getT2_zje_tscl());
			bsv.setT2_zje(_T2zje);
			bsv.setT2_zdj(CustomMaths.Round_new(CustomMaths.div(_T2zje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Yunjuzje=CustomMaths.add((double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2),bsv.getYunju_zje_tscl());			//�˾��۽��
			bsv.setYunju_zje(_Yunjuzje);
			bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Shulzje=CustomMaths.add((double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2),bsv.getShul_zje_tscl());	//����������ӯ��
			bsv.setShul_zje(_Shulzje);
			bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		
		//���㵥��ʾʱָ���۽����
		bsv.setJiashj(_Jiashj);
		bsv.setJiakhj(_Jiakhj);
		bsv.setJiaksk(_Jiaksk);
		bsv.setJine(_Jine);
		bsv.setBuhsmj(_Buhsmj);
		bsv.setShulzjbz(_Shulzjbz);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
//		bsv.setYunzfhj(_Yunzfhj);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
		bsv.setHej(_Hej);
		
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin	\n"); 
		
		if(bsv.getMeikjsb_id()==0){
			
			bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
		}
		
		sql.append(Shoumjsdcz.InsertDanpcjsmkb(bsv,this.getZhibModel()));
		
		sql.append("end;");
		
		if(sql.length()>13){
			
			con.getInsert(sql.toString());
		}
		sql.setLength(0);
		
		con.Close();
	}
	
//	������õ�����
	private void computData_Dpc(String selIds, long hetb_id, double shangcjsl){
		//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
		//ú��
		JDBCcon con=new JDBCcon();
		StringBuffer sql=new StringBuffer("begin 	\n");
		
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();
		double _Meiksl=bsv.getMeiksl();
		
		//ָ���۵���
		double _Qnetar=bsv.getQnetar_zdj();
		double _Std=bsv.getStd_zdj();
		double _Ad=bsv.getAd_zdj();
		double _Vdaf=bsv.getVdaf_zdj();
		double _Mt=bsv.getMt_zdj();
		double _Qgrad=bsv.getQgrad_zdj();
		double _Qbad=bsv.getQbad_zdj();
		double _Had=bsv.getHad_zdj();
		double _Stad=bsv.getStad_zdj();
		double _Star=bsv.getStar_zdj();
		double _Mad=bsv.getMad_zdj();
		double _Aar=bsv.getAar_zdj();
		double _Aad=bsv.getAad_zdj();
		double _Vad=bsv.getVad_zdj();
		double _T2=bsv.getT2_zdj();
		double _Shul=bsv.getShul_zdj();
		double _Yunju=bsv.getYunju_zdj();
		//ָ��ӯ��
		double _Shulyk=bsv.getShul_yk();								//ִ�к�ͬ�еĳ��ֽ�����
		
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
		double _Qnetarzje=0;
		double _Stdzje=0;
		double _Adzje=0;
		double _Vdafzje=0;
		double _Mtzje=0;
		double _Qgradzje=0;
		double _Qbadzje=0;
		double _Hadzje=0;
		double _Stadzje=0;
		double _Starzje=0;
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		double _Yunjuzje=0;
		
		Danjsmk_dcjcl(1,selIds,hetb_id,shangcjsl);
		
		//�۸������
//		2008-12-9zsj�ӣ�
//		�߼���	�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//		��������Ϊ��һ�����������һ�֣�
//			���һ��

//				��˰��ú��=�����ۡ�ú���������-�˷Ѻ�˰���ۡ��˷ѽ�������
//				��˰���˷�=�˷Ѻ�˰���ۡ��˷ѽ�������
//				���������ڽ������������ӡ�һ�ڼ�(�˷�Դ��ú��)�������ã�Ĭ��ֵ���񡱣����ֵΪ���ǡ�
//						���մ��������
//			�������

//				ú�˰����=�������ú�˰����-��ͬ�۸��еĺ�˰�˷ѵ���
//				��˰�˷ѵ���=��ͬ�۸��еĺ�˰�˷ѵ���
//				ͬʱҪ����Hansmj
//				��������ϵͳĬ�ϣ��������ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//						�ҽ��������С�һ�ڼ�(�˷�Դ��ú��)��ֵΪ���񡱣����մ��������
		
						
		
			if(bsv.getJijlx()==0){
//								��˰����
				
//							if(Meikzkktzsj!=null){
////								˵���в������ܼӼ۵����
////								1���������ֶ�Ӧ���۵���
////								2�����������۽��
////								(hansmj-�������ֵ��ۼ�)*��������+���������۽��
//								
//							}
				
				bsv.setJiajqdj(_Hansmj);										//����Ӽ�ǰ����
				_Hansmj=_Hansmj+bsv.getFengsjj();								//���Ϸֹ�˾�Ӽ�
				bsv.setHansmj(_Hansmj);											//���º�˰����
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//��˰�ϼ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//�ۿ�ϼ�
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//�ۿ�˰��
				_Jine=_Jiakhj;													//���
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
			
			}else if(bsv.getJijlx()==1){
//								�������ͣ�0����˰��1������˰��
//								����˰
				_Buhsmj=_Hansmj;
				_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
//				����Ӽ�ǰ��˰����
				_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setJiajqdj(_Hansmj);
//				����Ӽ�ǰ��˰����_end
				
				_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2)
						+(double)CustomMaths.Round_new(bsv.getFengsjj()*bsv.getJiessl(),2),2);	//����ֹ�˾�Ӽ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);
				_Jine=_Jiakhj;
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);	
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setHansmj(_Hansmj);
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
			}
		
		_Shulzjbz=_Hansmj;
		//�ϼ�
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//����ӯ�����ۼ۽��
		_Qnetarzje=(double)CustomMaths.Round_new(_Qnetar*_Jiessl,0);
		_Stdzje=(double)CustomMaths.Round_new(_Std*_Jiessl,2);
		_Adzje=(double)CustomMaths.Round_new(_Ad*_Jiessl,2);
		_Vdafzje=(double)CustomMaths.Round_new(_Vdaf*_Jiessl,2);
		_Mtzje=(double)CustomMaths.Round_new(_Mt*_Jiessl,2);
		_Qgradzje=(double)CustomMaths.Round_new(_Qgrad*_Jiessl,2);
		_Qbadzje=(double)CustomMaths.Round_new(_Qbad*_Jiessl,2);
		_Hadzje=(double)CustomMaths.Round_new(_Had*_Jiessl,2);
		_Stadzje=(double)CustomMaths.Round_new(_Stad*_Jiessl,2);
		_Starzje=(double)CustomMaths.Round_new(_Star*_Jiessl,2);
		_Madzje=(double)CustomMaths.Round_new(_Mad*_Jiessl,2);
		_Aarzje=(double)CustomMaths.Round_new(_Aar*_Jiessl,2);
		_Aadzje=(double)CustomMaths.Round_new(_Aad*_Jiessl,2);
		_Vadzje=(double)CustomMaths.Round_new(_Vad*_Jiessl,2);
		_T2zje=(double)CustomMaths.Round_new(_T2*_Jiessl,2);
		_Yunjuzje=(double)CustomMaths.Round_new(_Yunju*_Jiessl,2);	//�˾��۽��
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//����������ӯ��
		
		//���㵥��ʾʱָ���۽����
		bsv.setShulzjbz(_Shulzjbz);
		bsv.setShulzjje(_Shulzje);
		bsv.setJiashj(_Jiashj);
		bsv.setJiakhj(_Jiakhj);
		bsv.setJiaksk(_Jiaksk);
		bsv.setJine(_Jine);
		bsv.setHej(_Hej);
		
		bsv.setQnetar_zje(_Qnetarzje);
		bsv.setStd_zje(_Stdzje);
		bsv.setAd_zje(_Adzje);
		bsv.setVdaf_zje(_Vdafzje);
		bsv.setMt_zje(_Mtzje);
		bsv.setQgrad_zje(_Qgradzje);
		bsv.setQbad_zje(_Qbadzje);
		bsv.setHad_zje(_Hadzje);
		bsv.setStad_zje(_Stadzje);
		bsv.setMad_zje(_Madzje);
		bsv.setAar_zje(_Aarzje);
		bsv.setAad_zje(_Aadzje);
		bsv.setVad_zje(_Vadzje);
		bsv.setT2_zje(_T2);
		bsv.setStar_zje(_Starzje);
		bsv.setYunju_zje(_Yunjuzje);
		
		bsv.setXuh(bsv.getXuh()+1);
		
		if(bsv.getMeikjsb_id()==0){
			
			bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
		}
		
		sql.append(Shoumjsdcz.InsertDanpcjsmkb(bsv,this.getZhibModel()));
		
		sql.append("end;");
		
		if(sql.length()>13){
			
			con.getInsert(sql.toString());
		}
		sql.setLength(0);
		
		con.Close();
	}
	
	private void computData_Yf(){
		//�����˷ѽ������۵���

		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();	//ú���������
		//ָ���۽��
		double _Qnetarzje=0;
		double _Stdzje=0;
		double _Adzje=0;
		double _Vdafzje=0;
		double _Mtzje=0;
		double _Qgradzje=0;
		double _Qbadzje=0;
		double _Hadzje=0;
		double _Stadzje=0;
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		double _Yunjuzje=0;
		double _Starzje=0;
		double _Shulzjbz=0;
		//ָ���۵���
		double _Qnetar=bsv.getQnetar_zdj();
		double _Std=bsv.getStd_zdj();
		double _Ad=bsv.getAd_zdj();
		double _Vdaf=bsv.getVdaf_zdj();
		double _Mt=bsv.getMt_zdj();
		double _Qgrad=bsv.getQgrad_zdj();
		double _Qbad=bsv.getQbad_zdj();
		double _Had=bsv.getHad_zdj();
		double _Stad=bsv.getStad_zdj();
		double _Mad=bsv.getMad_zdj();
		double _Aar=bsv.getAar_zdj();
		double _Aad=bsv.getAad_zdj();
		double _Vad=bsv.getVad_zdj();
		double _T2=bsv.getT2_zdj();
		double _Shul=bsv.getShul_zdj();
		double _Yunju=bsv.getYunju_zdj();		//�˾��۵���
		double _Star=bsv.getStar_zdj();			//Star�۵���
		
		//����ӯ�����ۼ۽��
		bsv.setQnetar_yk(Shoumjsdcz.reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht(),"-",0),
				MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQnetar_yk()));	
		_Qnetarzje=CustomMaths.add((double)CustomMaths.Round_new(_Qnetar*_Jiessl,0),bsv.getQnetar_zje_tscl());
		bsv.setQnetar_zje(_Qnetarzje);
		bsv.setQnetar_zdj(CustomMaths.Round_new(CustomMaths.div(_Qnetarzje, _Jiessl),bsv.getMeikzkkblxsw()));
	
		bsv.setStd_yk(Shoumjsdcz.reCoundYk(bsv.getStd_ht(),bsv.getStd_js(),bsv.getStd_yk()));
		_Stdzje=CustomMaths.add((double)CustomMaths.Round_new(_Std*_Jiessl,2),bsv.getStd_zje_tscl());
		bsv.setStd_zje(_Stdzje);
		bsv.setStd_zdj(CustomMaths.Round_new(CustomMaths.div(_Stdzje, _Jiessl),bsv.getMeikzkkblxsw()));
	
		bsv.setAd_yk(Shoumjsdcz.reCoundYk(bsv.getAd_ht(),bsv.getAd_js(),bsv.getAd_yk()));
		_Adzje=CustomMaths.add((double)CustomMaths.Round_new(_Ad*_Jiessl,2),bsv.getAd_zje_tscl());
		bsv.setAd_zje(_Adzje);
		bsv.setAd_zdj(CustomMaths.Round_new(CustomMaths.div(_Adzje, _Jiessl),bsv.getMeikzkkblxsw()));
	
		_Vdafzje=CustomMaths.add((double)CustomMaths.Round_new(_Vdaf*_Jiessl,2),bsv.getVdaf_zje_tscl());
		bsv.setVdaf_zje(_Vdafzje);
		bsv.setVdaf_zdj(CustomMaths.Round_new(CustomMaths.div(_Vdafzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
		_Mtzje=CustomMaths.add((double)CustomMaths.Round_new(_Mt*_Jiessl,2),bsv.getMt_zje_tscl());
		bsv.setMt_zje(_Mtzje);
		bsv.setMt_zdj(CustomMaths.Round_new(CustomMaths.div(_Mtzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Qgradzje=CustomMaths.add((double)CustomMaths.Round_new(_Qgrad*_Jiessl,2),bsv.getQgrad_zje_tscl());
		bsv.setQgrad_zje(_Qgradzje);
		bsv.setQgrad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qgradzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Qbadzje=CustomMaths.add((double)CustomMaths.Round_new(_Qbad*_Jiessl,2),bsv.getQbad_zje_tscl());
		bsv.setQbad_zje(_Qbadzje);
		bsv.setQbad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qbadzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Hadzje=CustomMaths.add((double)CustomMaths.Round_new(_Had*_Jiessl,2),bsv.getHad_zje_tscl());
		bsv.setHad_zje(_Hadzje);
		bsv.setHad_zdj(CustomMaths.Round_new(CustomMaths.div(_Hadzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Stadzje=CustomMaths.add((double)CustomMaths.Round_new(_Stad*_Jiessl,2),bsv.getStad_zje_tscl());
		bsv.setStad_zje(_Stadzje);
		bsv.setStad_zdj(CustomMaths.Round_new(CustomMaths.div(_Stadzje, _Jiessl),bsv.getMeikzkkblxsw()));
	
		
		_Starzje=CustomMaths.add((double)CustomMaths.Round_new(_Star*_Jiessl,2),bsv.getStar_zje_tscl());
		bsv.setStar_zje(_Starzje);
		bsv.setStar_zdj(CustomMaths.Round_new(CustomMaths.div(_Starzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
		
		_Madzje=CustomMaths.add((double)CustomMaths.Round_new(_Mad*_Jiessl,2),bsv.getMad_zje_tscl());
		bsv.setMad_zje(_Madzje);
		bsv.setMad_zdj(CustomMaths.Round_new(CustomMaths.div(_Madzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Aarzje=CustomMaths.add((double)CustomMaths.Round_new(_Aar*_Jiessl,2),bsv.getAar_zje_tscl());
		bsv.setAar_zje(_Aarzje);
		bsv.setAar_zdj(CustomMaths.Round_new(CustomMaths.div(_Aarzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Aadzje=CustomMaths.add((double)CustomMaths.Round_new(_Aad*_Jiessl,2),bsv.getAad_zje_tscl());
		bsv.setAad_zje(_Aadzje);
		bsv.setAad_zdj(CustomMaths.Round_new(CustomMaths.div(_Aadzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Vadzje=CustomMaths.add((double)CustomMaths.Round_new(_Vad*_Jiessl,2),bsv.getVad_zje_tscl());
		bsv.setVad_zje(_Vadzje);
		bsv.setVad_zdj(CustomMaths.Round_new(CustomMaths.div(_Vadzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_T2zje=CustomMaths.add((double)CustomMaths.Round_new(_T2*_Jiessl,2),bsv.getT2_zje_tscl());
		bsv.setT2_zje(_T2zje);
		bsv.setT2_zdj(CustomMaths.Round_new(CustomMaths.div(_T2zje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Yunjuzje=CustomMaths.add((double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2),bsv.getYunju_zje_tscl());			//�˾��۽��
		bsv.setYunju_zje(_Yunjuzje);
		bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Shulzje=CustomMaths.add((double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2),bsv.getShul_zje_tscl());	//����������ӯ��
		bsv.setShul_zje(_Shulzje);
		bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
		//���㵥��ʾʱָ���۽����
		bsv.setShulzjbz(_Shulzjbz);
	}
	
	private void reCount(){
//		����danpcjsmxb �����ĵ������ܽ��㵥��ֵ��ע����Ȩ
		JDBCcon con =new JDBCcon();
		try{
			
//			����ָ����Ϣ
			String strSql=" select zb.bianm,  	\n"  
						+ " 	max(mx.hetbz) as hetbz, case when zb.bianm='"+Locale.jiessl_zhibb+"' then sum(gongf)	\n"       
						+ " 		else round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),2) end as gongf,	\n"  
						+ " 	case when zb.bianm='"+Locale.jiessl_zhibb+"' then sum(changf)	\n"    
						+ " 		else round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),2) end as changf,	\n"  
						+ " 	case when zb.bianm='"+Locale.jiessl_zhibb+"' then sum(jies)		\n"      
						+ " 		else round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),2) end as jies,	\n"  
						+ " 	sum(mx.yingk) as yingk,		\n"    
						+ " 	round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.zhejbz))/sum(decode(jiessl,0,1,jiessl)),"+bsv.getMeikzkkblxsw()+") as zhejbz,	\n"  
						+ " 	sum(mx.zhejje) as zhejje	\n"  
						+ " from danpcjsmxb mx,zhibb zb	\n"   
						+ " where zb.id=mx.zhibb_id and mx.leib=1 and jiesdid="+bsv.getMeikjsb_id()+"	\n"   
						+ " group by zb.bianm";
			ResultSet rs=con.getResultSet(strSql);
			while(rs.next()){
				
				if(rs.getString("bianm").equals(Locale.jiessl_zhibb)){
					
					bsv.setShul_ht(rs.getString("hetbz"));
					bsv.setGongfsl(rs.getDouble("gongf"));
					bsv.setYanssl(rs.getDouble("changf"));
					bsv.setJiessl(rs.getDouble("jies"));
					bsv.setYingksl(rs.getDouble("yingk"));
					bsv.setShulzjbz(rs.getDouble("zhejbz"));
					bsv.setShulzjje(rs.getDouble("zhejje"));

					
				}else if(rs.getString("bianm").equals(Locale.Qnetar_zhibb)){
					
					bsv.setQnetar_ht(rs.getString("hetbz"));
					bsv.setQnetar_kf((double)CustomMaths.Round_new(rs.getDouble("gongf"),0));
					bsv.setQnetar_cf((double)CustomMaths.Round_new(rs.getDouble("changf"),0));
					bsv.setQnetar_js((double)CustomMaths.Round_new(rs.getDouble("jies"),0));
					bsv.setQnetar_yk((double)CustomMaths.Round_new(rs.getDouble("yingk"),0));
					bsv.setQnetar_zdj(rs.getDouble("zhejbz"));
					bsv.setQnetar_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Std_zhibb)){
					
					bsv.setStd_ht(rs.getString("hetbz"));
					bsv.setStd_kf(rs.getDouble("gongf"));
					bsv.setStd_cf(rs.getDouble("changf"));
					bsv.setStd_js(rs.getDouble("jies"));
					bsv.setStd_yk(rs.getDouble("yingk"));
					bsv.setStd_zdj(rs.getDouble("zhejbz"));
					bsv.setStd_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Ad_zhibb)){
					
					bsv.setAd_ht(rs.getString("hetbz"));
					bsv.setAd_kf(rs.getDouble("gongf"));
					bsv.setAd_cf(rs.getDouble("changf"));
					bsv.setAd_js(rs.getDouble("jies"));
					bsv.setAd_yk(rs.getDouble("yingk"));
					bsv.setAd_zdj(rs.getDouble("zhejbz"));
					bsv.setAd_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Vdaf_zhibb)){
					
					bsv.setVdaf_ht(rs.getString("hetbz"));
					bsv.setVdaf_kf(rs.getDouble("gongf"));
					bsv.setVdaf_cf(rs.getDouble("changf"));
					bsv.setVdaf_js(rs.getDouble("jies"));
					bsv.setVdaf_yk(rs.getDouble("yingk"));
					bsv.setVdaf_zdj(rs.getDouble("zhejbz"));
					bsv.setVdaf_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Mt_zhibb)){
					
					bsv.setMt_ht(rs.getString("hetbz"));
					bsv.setMt_kf(rs.getDouble("gongf"));
					bsv.setMt_cf(rs.getDouble("changf"));
					bsv.setMt_js(rs.getDouble("jies"));
					bsv.setMt_yk(rs.getDouble("yingk"));
					bsv.setMt_zdj(rs.getDouble("zhejbz"));
					bsv.setMt_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Qgrad_zhibb)){
					
					bsv.setQgrad_ht(rs.getString("hetbz"));
					bsv.setQgrad_kf(rs.getDouble("gongf"));
					bsv.setQgrad_cf(rs.getDouble("changf"));
					bsv.setQgrad_js(rs.getDouble("jies"));
					bsv.setQgrad_yk(rs.getDouble("yingk"));
					bsv.setQgrad_zdj(rs.getDouble("zhejbz"));
					bsv.setQgrad_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Qbad_zhibb)){
					
					bsv.setQbad_ht(rs.getString("hetbz"));
					bsv.setQbad_kf(rs.getDouble("gongf"));
					bsv.setQbad_cf(rs.getDouble("changf"));
					bsv.setQbad_js(rs.getDouble("jies"));
					bsv.setQbad_yk(rs.getDouble("yingk"));
					bsv.setQbad_zdj(rs.getDouble("zhejbz"));
					bsv.setQbad_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Had_zhibb)){
					
					bsv.setHad_ht(rs.getString("hetbz"));
					bsv.setHad_kf(rs.getDouble("gongf"));
					bsv.setHad_cf(rs.getDouble("changf"));
					bsv.setHad_js(rs.getDouble("jies"));
					bsv.setHad_yk(rs.getDouble("yingk"));
					bsv.setHad_zdj(rs.getDouble("zhejbz"));
					bsv.setHad_zje(rs.getDouble("zhejje"));
				
				}else if(rs.getString("bianm").equals(Locale.Stad_zhibb)){
					
					bsv.setStad_ht(rs.getString("hetbz"));
					bsv.setStad_kf(rs.getDouble("gongf"));
					bsv.setStad_cf(rs.getDouble("changf"));
					bsv.setStad_js(rs.getDouble("jies"));
					bsv.setStad_yk(rs.getDouble("yingk"));
					bsv.setStad_zdj(rs.getDouble("zhejbz"));
					bsv.setStad_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Star_zhibb)){
					
					bsv.setStar_ht(rs.getString("hetbz"));
					bsv.setStar_kf(rs.getDouble("gongf"));
					bsv.setStar_cf(rs.getDouble("changf"));
					bsv.setStar_js(rs.getDouble("jies"));
					bsv.setStar_yk(rs.getDouble("yingk"));
					bsv.setStar_zdj(rs.getDouble("zhejbz"));
					bsv.setStar_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Mad_zhibb)){
					
					bsv.setMad_ht(rs.getString("hetbz"));
					bsv.setMad_kf(rs.getDouble("gongf"));
					bsv.setMad_cf(rs.getDouble("changf"));
					bsv.setMad_js(rs.getDouble("jies"));
					bsv.setMad_yk(rs.getDouble("yingk"));
					bsv.setMad_zdj(rs.getDouble("zhejbz"));
					bsv.setMad_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Aar_zhibb)){
					
					bsv.setAar_ht(rs.getString("hetbz"));
					bsv.setAar_kf(rs.getDouble("gongf"));
					bsv.setAar_cf(rs.getDouble("changf"));
					bsv.setAar_js(rs.getDouble("jies"));
					bsv.setAar_yk(rs.getDouble("yingk"));
					bsv.setAar_zdj(rs.getDouble("zhejbz"));
					bsv.setAar_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Aad_zhibb)){
					
					bsv.setAad_ht(rs.getString("hetbz"));
					bsv.setAad_kf(rs.getDouble("gongf"));
					bsv.setAad_cf(rs.getDouble("changf"));
					bsv.setAad_js(rs.getDouble("jies"));
					bsv.setAad_yk(rs.getDouble("yingk"));
					bsv.setAad_zdj(rs.getDouble("zhejbz"));
					bsv.setAad_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.Vad_zhibb)){
					
					bsv.setVad_ht(rs.getString("hetbz"));
					bsv.setVad_kf(rs.getDouble("gongf"));
					bsv.setVad_cf(rs.getDouble("changf"));
					bsv.setVad_js(rs.getDouble("jies"));
					bsv.setVad_yk(rs.getDouble("yingk"));
					bsv.setVad_zdj(rs.getDouble("zhejbz"));
					bsv.setVad_zje(rs.getDouble("zhejje"));
					
				}else if(rs.getString("bianm").equals(Locale.T2_zhibb)){
					
					bsv.setT2_ht(rs.getString("hetbz"));
					bsv.setT2_kf(rs.getDouble("gongf"));
					bsv.setT2_cf(rs.getDouble("changf"));
					bsv.setT2_js(rs.getDouble("jies"));
					bsv.setT2_yk(rs.getDouble("yingk"));
					bsv.setT2_zdj(rs.getDouble("zhejbz"));
					bsv.setT2_zje(rs.getDouble("zhejje"));
				
				}else if(rs.getString("bianm").equals(Locale.Yunju_zhibb)){
					
					bsv.setYunju_ht(rs.getString("hetbz"));
					bsv.setYunju_kf(rs.getDouble("gongf"));
					bsv.setYunju_cf(rs.getDouble("changf"));
					bsv.setYunju_js(rs.getDouble("jies"));
					bsv.setYunju_yk(rs.getDouble("yingk"));
					bsv.setYunju_zdj(rs.getDouble("zhejbz"));
					bsv.setYunju_zje(rs.getDouble("zhejje"));
				}
			}
			
//			�����������۸񡢽����Ϣ
			strSql="select sum(gongfsl) as gongfsl,sum(yanssl) as yanssl,sum(jiessl) as jiessl,sum(koud) as koud,	\n"
				+ " 	sum(kous) as kous,sum(kouz) as kouz,sum(ches) as ches,sum(jingz) as jingz,	\n"
				+ " 	sum(koud_js) as koud_js,sum(jiesslcy) as jiesslcy,sum(yuns) as yuns,	\n"
				+ " 	round_new(sum(decode(jiessl,0,0,jiessl*jiajqdj))/sum(decode(jiessl,0,1,jiessl)),"+bsv.getMeikhsdjblxsw()+") as jiajqdj,	\n"
				+ " 	round_new(sum(decode(jiessl,0,0,jiessl*jiesdj))/sum(decode(jiessl,0,1,jiessl)),"+bsv.getMeikhsdjblxsw()+") as jiesdj,	\n"
				+ " 	sum(jiakhj) as jiakhj,sum(jiaksk) as jiaksk,sum(jiashj) as jiashj,sum(chaokdl) as chaokdl,		\n"
				+ " 	round_new(sum(decode(jiessl,0,0,jiessl*biaomdj))/sum(decode(jiessl,0,1,jiessl)),2) as biaomdj,	\n"
				+ " 	round_new(sum(decode(jiessl,0,0,jiessl*buhsbmdj))/sum(decode(jiessl,0,1,jiessl)),2) as buhsbmdj	\n"
				+ " from 	\n"
				+ " 	(select xuh,	\n"       
				+ " 		max(mx.gongfsl) as gongfsl,		\n"
				+ " 		max(mx.yanssl) as yanssl,		\n"
				+ " 		max(mx.jiessl) as jiessl,		\n" 
				+ " 		max(mx.koud)  as koud,			\n"
				+ " 		max(mx.kous)  as kous,			\n"
				+ " 		max(mx.kouz)  as kouz,			\n"
				+ " 		max(mx.ches)  as ches,			\n"
				+ " 		max(mx.jingz)  as jingz,		\n"
				+ " 		max(mx.koud_js)  as koud_js,	\n"
				+ " 		max(mx.jiesslcy) as jiesslcy,	\n"
				+ " 		max(mx.yuns) as yuns,			\n"
				+ " 		max(mx.jiajqdj) as jiajqdj,		\n"
				+ " 		max(mx.jiesdj) as jiesdj,		\n"  
				+ " 		max(mx.jiakhj) as jiakhj,		\n"  
				+ " 		max(mx.jiaksk) as jiaksk,		\n"  
				+ " 		max(mx.jiashj) as jiashj,		\n"
				+ "			max(mx.chaokdl) as chaokdl,		\n"
				+ " 		0 as biaomdj,		\n"         
				+ " 		0 as buhsbmdj		\n" 
				+ " 	from danpcjsmxb mx		\n"
				+ " 	where leib=1 and jiesdid="+bsv.getMeikjsb_id()+"	\n"
				+ " 	group by xuh)";
			rs=con.getResultSet(strSql);
			while(rs.next()){
				
				if(rs.getDouble("jiessl")>0){
					
//					����۸�
					bsv.setGongfsl(rs.getDouble("gongfsl"));
					bsv.setYanssl(rs.getDouble("yanssl"));
					bsv.setJiessl(rs.getDouble("jiessl"));
					bsv.setKoud(rs.getDouble("koud"));
					bsv.setKous(rs.getDouble("kous"));
					bsv.setKouz(rs.getDouble("kouz"));
					bsv.setChes(rs.getLong("ches"));
					bsv.setJingz(rs.getDouble("jingz"));
					bsv.setKoud_js(rs.getDouble("koud_js"));
					bsv.setJiesslcy(rs.getDouble("jiesslcy"));
					bsv.setYuns(rs.getDouble("yuns"));
					bsv.setJiajqdj(rs.getDouble("jiajqdj"));
					bsv.setShulzjbz(rs.getDouble("jiesdj"));
					bsv.setHansmj(rs.getDouble("jiesdj"));
					bsv.setJiakhj(rs.getDouble("jiakhj"));
					bsv.setJine(rs.getDouble("jiakhj"));
					bsv.setJiaksk(rs.getDouble("jiaksk"));
					bsv.setJiashj(rs.getDouble("jiashj"));
					bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
					bsv.setChaokdl(rs.getDouble("chaokdl"));
				}
			}
			rs.close();
			
//			���㵽������ú���˷ѵ����
			if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//				�����ͬ�еĽ��㷽ʽΪ������,ͳһ�㷨��
//				������˷ѣ���ô����ú���ȥ���˷�ʣ�µľ�����ú��
				computYunfAndHej();
				if(bsv.getYunzfhj()>0){
					
					bsv.setJiashj((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getYunzfhj(),2));	//ԭʼ��˰�ϼ�
					bsv.setJiakhj((double)CustomMaths.Round_new(bsv.getJiashj()/(1+bsv.getMeiksl()),2));
					bsv.setJiaksk((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getJiakhj(),2));	
					bsv.setJine(bsv.getJiakhj());
					bsv.setHansmj((double)CustomMaths.Round_new(bsv.getJiashj()/bsv.getJiessl(), bsv.getMeikhsdjblxsw()));
					bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
					
					Shoumjsdcz.UpdateDanpcjsmkb(bsv.getMeikjsb_id(), bsv.getHansmj(), bsv.getJiakhj(), 
							bsv.getJiaksk(), bsv.getJiashj(), CustomMaths.sub(bsv.getHansmj(), bsv.getFengsjj()));
					
					Danjsmk_dcjcl(2, "", 0, 0);
				}
			}
			
			
			
//	    	����ܸ������˷�
	    	if(bsv.getKuidjfyf().equals("��")){
	    		
	    		double Tielyfdj=(double)CustomMaths.Round_new(bsv.getTielyf()/bsv.getGongfsl(),2);
	    		double Tielzfdj=(double)CustomMaths.Round_new(bsv.getTielzf()/bsv.getGongfsl(),2);
	    		
	    		double Kuangqyfdj=(double)CustomMaths.Round_new(bsv.getKuangqyf()/bsv.getGongfsl(),2);
	    		double Kuangqzfdj=(double)CustomMaths.Round_new(bsv.getKuangqzf()/bsv.getGongfsl(),2);
	    		
	    		bsv.setTielyf((double)CustomMaths.Round_new(bsv.getTielyf()-(double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2),2));
	    		bsv.setTielzf((double)CustomMaths.Round_new(bsv.getTielzf()-(double)CustomMaths.Round_new(Tielzfdj*bsv.getKuid(),2),2));
	    		
	    		bsv.setKuangqyf((double)CustomMaths.Round_new(bsv.getKuangqyf()-(double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2),2));
	    		bsv.setKuangqzf((double)CustomMaths.Round_new(bsv.getKuangqzf()-(double)CustomMaths.Round_new(Kuangqzfdj*bsv.getKuid(),2),2));
	    		
	    		if((double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2)>0){
	    			
	    			bsv.setBeiz(bsv.getBeiz()+" "+"���־ܸ��˷ѣ�"+(double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2)+"Ԫ�����־ܸ��ӷѣ�"+(double)CustomMaths.Round_new(Tielzfdj*bsv.getKuid(),2)+"Ԫ");
	    		}
	    		
	    		if((double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2)>0){
	    			
	    			bsv.setBeiz(bsv.getBeiz()+" "+"���־ܸ����˷ѣ�"+(double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2)+"Ԫ�����־ܸ����ӷѣ�"+(double)CustomMaths.Round_new(Kuangqzfdj*bsv.getKuid(),2)+"Ԫ");
	    		}
	    		
	    		bsv.setKuidjfyf_je((double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2)+(double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2));
	    		bsv.setKuidjfzf_je((double)CustomMaths.Round_new(Tielzfdj*bsv.getKuid(),2)+(double)CustomMaths.Round_new(Kuangqzfdj*bsv.getKuid(),2));
	    	}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private void computYunfAndHej(){
		
		//�˷�
		double _Tielyf=bsv.getTielyf();
		double _Tielzf=bsv.getTielzf();
		double _Yunfsl=bsv.getYunfsl();		//�˷�˰��
		double _Kuangqyf=bsv.getKuangqyf();
		double _Kuangqzf=bsv.getKuangqzf();
		double _Kuangqsk=bsv.getKuangqsk();
		double _Kuangqjk=bsv.getKuangqjk();
		
		double _Yunfsk=0;
		double _Yunzfhj=0;
		double _Buhsyf=0;
		double _Hej=0;
		String _Hejdx="";
		
		//�����˷���
		_Yunzfhj=(double)CustomMaths.Round_new(_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf,2);									//���ӷѺϼ�
		_Yunfsk=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new(_Tielyf*_Yunfsl,2)+_Kuangqsk),2);		//�˷�˰��		
		_Buhsyf=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2)+_Kuangqjk),2);		//����˰�˷�
		
		if(_Yunzfhj==0){
			
			_Yunzfhj=(double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2);						//�˷�˰��
			_Yunfsk=(double)CustomMaths.Round_new(_Yunzfhj*_Yunfsl,2);											//�˷�˰��
			_Buhsyf=(double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2);											//����˰�˷�
			_Tielyf=_Yunzfhj;
			bsv.setTielyf(_Tielyf);
		}
		
		//�ϼ�
		_Hej=(double)CustomMaths.Round_new((_Yunzfhj+bsv.getJiashj()),2);
		_Hejdx=getDXMoney(_Hej);
		
		bsv.setYunfsk(_Yunfsk);
		bsv.setBuhsyf(_Buhsyf);
		bsv.setYunzfhj(_Yunzfhj);
		bsv.setYunfsk(_Yunfsk);
		bsv.setBuhsyf(_Buhsyf);
		bsv.setHej(_Hej);
		bsv.setHejdx(_Hejdx);
	}
	
	private void Jiestszbcl(String Jieszbsftz,String SelIds,long Diancxxb_id,long Gongysb_id,
						long Hetb_id,double Jieskdl,long Yunsdwb_id,long Jieslx,double Shangcjsl){
//		��������ָ�괦��
//		Ŀǰ��ֻ�����Ȩƽ������ʱ��ĳЩָ����Ҫ�����μ��㣬�������⴦������
//		�ú���ֻ�������ۿ�
		Interpreter bsh=null;
		String tmp[]=null;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String strtmp="";
		try {
			for(int i=0;i<bsv.getTsclzbs().length;i++){
				
				tmp = bsv.getTsclzbs()[i].split(",");
				if(tmp[tmp.length-1].equals("0")){
//					˵����ָ�껹δ�������⴦��
					
					bsh=new Interpreter();
					bsh.set("������ʽ", Locale.jiaqpj_jiesxs);

//					ú�˰���۱���С��λ
					bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
					
//					��˰����ȡ����ʽ
					bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
					
					bsh.set("�Ƽ۷�ʽ", Locale.rezakkf_hetjjfs);
					bsh.set("�۸�λ", Locale.yuanmqk_danw);	
					bsh.set("��ͬ�۸�", 0);
					bsh.set("���ú��", bsv.getZuigmj());
					
//					���ۿ��С��λ
					bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
					
//					�û��Զ��幫ʽ
					bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

					this.getZengkk(bsv.getHetb_Id(), bsh, false ,tmp);
					if(bsv.getTsclzbzkksfxyjs()){
						
//						����һ��ָ��ֵ���������ۿ����
						bsv.setTsclzbzkksfxyjs(false);
//						��ָ����Ҫ���ۿ����⴦��
						bsh.eval(bsv.getGongs_Mk());
//						�õ����ۿ�۸���Ϣ
						Shoumjsdcz.setJieszb_Tszbcl(bsh, bsv, tmp[1]);
//						Ҫ�ҵ������ۿ��Ӧ������
						rsl=con.getResultSetList(Shoumjsdcz.getJiesszl_Sql(bsv, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, 
								Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, " and "+tmp[2]+"="+tmp[3]).toString());
						
						if(rsl.next()){
							
//							�õ���ָ����ۼ۱�׼
//							�ַ�������ָ�����,ָ���ֵ,ָ���۵���,�ۼ�����,�۽��
							strtmp+=tmp[1]+","+tmp[4]+","+bsh.get("�۵���_"+tmp[1]).toString()+","+rsl.getString("jiessl")+","+CustomMaths.Round_new(CustomMaths.mul(Double.parseDouble(bsh.get("�۵���_"+tmp[1]).toString()),rsl.getDouble("jiessl")),2)+";";
						}
					}
				}
			}
			
			if(!strtmp.equals("")){
//				��������⴦���ָ�꣬�ͽ�Tsclzbs��ֵΪ����ָ������ۿ��¼

				bsv.setTsclzbs(strtmp.split(";"));
//				�˾�,25,10,100,1000;
//				�˾�,23,12,300,3600;
			}else{
//				��Tsclzbs����
				bsv.setTsclzbs(null);
			}
			
			if(rsl!=null){
				
				rsl.close();
			}
			
			con.Close();
			
		} catch (EvalError ev) {
			// TODO �Զ����� catch ��
			ev.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void Danjsmk_dcjcl(int Place, String selIds, long hetb_id, double shangcjsl){
//		�������ƣ�
		
//			������ú������۴���
//		�������ܣ�
		
//			��������ú��ʱ����ú���ͬΪ������ʱ�����˷ѵ�����			
//		�����߼���
//			�߼�1��
//				����ǵ�����ú�������ǵ�����ʱ�������˷ѡ�
				
//			�߼�2��
//				��recount�������Ѿ�������˵����۵������Ҫ���˷������ֵ���
//		�����βΣ�
//			PlaceҪӦ�õ��߼��ṹ��1Ϊ�߼�1��2Ϊ�߼�2
//			selIdsҪ�������id
//			idhetb_idú���ͬ��
		
		if(Place==1){
//			�߼�1
			if(bsv.getJieslx()==Locale.meikjs_feiylbb_id
					&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
			){
//				����ҵ�����Σ�����ǵ�����ú���ͬ�۸��ǵ�����ʱ�������˷ѣ�
//					�ں����recount�д�ú���м����˷ѣ������˷�������ա�
				this.getYunFei(selIds, Locale.liangpjs_feiylbb_id, hetb_id, shangcjsl);
			}
		}else if(Place==2){
//			�߼�2
			if(bsv.getJieslx()==Locale.meikjs_feiylbb_id
					&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
			){
//				����ҵ�����Σ�����ǵ�����ú���ͬ�۸��ǵ�����ʱ�������˷ѣ�
//					�ں����recount�д�ú���м����˷ѣ������˷�������ա�
				bsv.setTielyf(0);
				bsv.setTielzf(0);
				bsv.setYunfsl(0);		//�˷�˰��
				bsv.setKuangqyf(0);
				bsv.setKuangqzf(0);
				bsv.setKuangqsk(0);
				bsv.setKuangqjk(0);
				bsv.setYunfjsdj(0);
				bsv.setYunfjsl(0);
			}
		}
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
		
		String sql = "select id,bianm from zhibb where leib=1 order by bianm";
		_ZhibModel=new IDropDownModel(sql);
		return _ZhibModel;
	}
}