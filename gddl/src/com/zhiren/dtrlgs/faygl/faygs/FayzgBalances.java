 /*
 * �������� 2008-4-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.dtrlgs.faygl.faygs;
/**
 * @author Admini_ator
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.zhiren.common.*;
import com.zhiren.dtrlgs.faygl.fayjs.Balances_variable;
import com.zhiren.dtrlgs.faygl.fayjs.Fayjsdcz;
//import com.zhiren.dtrlgs.faygl.fayjs.Fayjsdcz;
import com.zhiren.dtrlgs.shoumgl.shoumjs.Shoumjsdcz;

import bsh.*;

public class FayzgBalances {
	
	Balances_variable bsv=null;
	
	public Balances_variable getBalanceData(Balances_variable bsv,String SelIds,long Diancxxb_id,
			long Hetb_id,long Jieslx,long Gongysb_id,String Jieszbsftz,String Yansbh,double Jieskdl,double Shangcjsl) throws Exception{
		
		this.bsv=bsv;
		this.bsv.setIsError(true);
		this.bsv.setErroInfo("");
		
		if(Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.meikjs_feiylbb_id){
//			��Ʊ���㡢ú�����
			
			if (getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id)){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
				
			}else{
				bsv.getErroInfo();
			}
			
//			�õ���ʽ��
			if (getGongsInfo(Diancxxb_id,"ALL")) {
				
			}else{
				//return ErroInfo;
			}
			
//			��ú��,��ͬ�е����ֵ
			if  (getMeiPrice(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,Jieszbsftz,SelIds,
						Gongysb_id,Jieskdl,Shangcjsl)){
				
			}else{
				
				bsv.getErroInfo();
			}
			
//			���˷�
//			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
//				
//			}else{
//				
//				bsv.getErroInfo();
//			}
		}else{	//�˷ѽ���
			
			if (getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id)){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
				
			}else{
				bsv.getErroInfo();
			}
			
			getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
			
//			���˷�
//			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
//				
//			}else{
//				bsv.getErroInfo();
//			}
		}
		
		computYunfAndHej();

		bsv.setIsError(false);
		
		return bsv;
	}
	
//	�����ݹ�_���ƹ���
	public Balances_variable getBalanceData(Balances_variable bsv,String SelIds,long Diancxxb_id,long Hetb_id,long Jieslx,long Gongysb_id,double Jiesrl) throws Exception{
		
		this.bsv=bsv;
		this.bsv.setIsError(true);
		this.bsv.setErroInfo("");
		
		if(Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.meikjs_feiylbb_id){
//			��Ʊ���㡢ú�����
			
			if (getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id)){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
				
			}else{
				bsv.getErroInfo();
			}
			
//			�õ���ʽ��
			if (getGongsInfo(Diancxxb_id,"ALL")) {
				
			}else{
				//return ErroInfo;
			}
			
			bsv.setSelIds(SelIds);
			bsv.setDiancxxb_id(Diancxxb_id);
//			��ú��,��ͬ�е����ֵ
			if  (getMeiPrice_PerFah(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,SelIds,Gongysb_id,Jiesrl)){
				
			}else{
				
				bsv.getErroInfo();
			}
			
//			���˷�
//			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
//				
//			}else{
//				
//				bsv.getErroInfo();
//			}
		}else{	//�˷ѽ���
			
			if (getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id)){
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
				
			}else{
				bsv.getErroInfo();
			}
			
			getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
			
//			���˷�
//			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
//				
//			}else{
//				bsv.getErroInfo();
//			}
		}
		
		computYunfAndHej();

		bsv.setIsError(false);
		
		return bsv;
	}
	
	//�õ�ϵͳ��Ϣ���˷�˰�ʣ�ú��˰�ʣ���ʽ��
	private boolean getGongsInfo(long _Diancxxb_id,String _Type) throws Exception{
//		JDBCcon con =new JDBCcon();
//	    try {
//            
//            //ú����㹫ʽ
//	    	con.setAutoCommit(false);
//	    	ResultSet rs= con.getResultSet("select id from gongsb where mingc='����ú��' and leix='����' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
//            if (rs.next()) {
//            	
//            	DataBassUtil clob=new DataBassUtil();
//            	
//            	bsv.setGongs(clob.getClob("gongsb", "gongs", rs.getLong(1)));
//            	
//            }else{
//            	bsv.setErroInfo("û�еõ�'ú�۹�ʽ'��ϵͳ����ֵ");
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
			
			str_Gongs_Mk=Fayjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			str_Gongs_Yf=Fayjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
			
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
			
			str_Gongs_Mk=Fayjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			
			if(str_Gongs_Mk.equals("")){
				
				bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
	        	return false;
			}else{
				
				bsv.setGongs_Mk(str_Gongs_Mk);
			}
		}else if(_Type.equals("YF")){
			
			str_Gongs_Yf=Fayjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
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
    
	private boolean getBaseInfo(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id) throws Exception{

        JDBCcon con =new JDBCcon();
	    try {
            //�������ڡ��������ڡ����������ء�ӯ�������𡢷��������� from fahb
	        //����������ȡ֤����������ú�ǰ���ȡ��������ú�����ȡ��
	    	
	    	String sql="";
	    	
//	    	sql=" select hetb_id,yunj,meikxxb_id,gongysb_id,pinzb_id,yunsfsb_id,minfahrq,maxfahrq,mindaohrq,maxdaohrq,gongysqc,meikdwqc,faz,faz_id,daoz_id,yuanshdw,pinz,yunsfs,nvl(getMeiksx(meikxxb_id,diancxxb_id,'�˾�'),0) as yunju		\n "+
//	    		" from 														\n" +
//	    		" (select max(hetb_id) as hetb_id,max(m.yunj) as yunj,max(f.meikxxb_id) as meikxxb_id,max(f.gongysb_id) as gongysb_id,max(pinzb_id) as pinzb_id,max(yunsfsb_id) as yunsfsb_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq,to_char(max(fahrq),'yyyy-mm-dd') as maxfahrq,max(pz.mingc) as pinz,max(ysfs.mingc) as yunsfs," +
//				" to_char(min(daohrq),'yyyy-mm-dd') as mindaohrq,to_char(max(daohrq),'yyyy-mm-dd') as maxdaohrq,max(g.quanc) as gongysqc,max(m.quanc) as meikdwqc,max(cz.mingc) as faz,max(cz.id) as faz_id,max(dz.id) as daoz_id,max(vwydw.mingc) as yuanshdw,max(f.diancxxb_id) as diancxxb_id " +
//				" from fahb f,zhilb z,kuangfzlb kz,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz" +
//				" where f.zhilb_id=z.id(+) and kz.id(+)=f.kuangfzlb_id and f.faz_id=cz.id(+) and f.pinzb_id=pz.id(+) " +
//				" and f.gongysb_id=g.id(+) and f.meikxxb_id=m.id(+) and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id(+) and f.daoz_id=dz.id(+)" +
//				" and f.id in("+SelIds+"))";
           
	    	sql=" select hetb_id,\n"+
	        " yunj,\n"+
	        " meikxxb_id,\n"+
	       "  gongysb_id,\n"+
	       "  pinzb_id,\n"+
	       "  yunsfsb_id,\n"+
	       "  minfahrq,\n"+
	        " maxfahrq,\n"+
	        " nvl(mindaohrq,to_char(sysdate,'yyyy-mm-dd')) as mindaohrq,\n"+
	        " nvl(maxdaohrq,to_char(sysdate,'yyyy-mm-dd')) as maxdaohrq,\n"+
	        " gongysqc,\n"+
	        " meikdwqc,\n"+
	        " faz,\n"+
	        " faz_id,\n"+
	        " daoz_id,\n"+
	        " yuanshdw,\n"+
	        " pinz,\n"+
	         "yunsfs,\n"+
	         "nvl(getMeiksx(meikxxb_id, diancxxb_id, '�˾�'), 0) as yunju\n"+
	         ",jihkjb_id\n"+
	         ",chec\n"+
	   " from (select max(hetb_id) as hetb_id,\n"+
	    "             max(m.yunj) as yunj,\n"+
	     "            max(f.meikxxb_id) as meikxxb_id,\n"+
	      "           max(f.gongysb_id) as gongysb_id,\n"+
	       "          max(pinzb_id) as pinzb_id,\n"+
	        "         max(yunsfsb_id) as yunsfsb_id,\n"+
	         "        to_char(min(fahrq), 'yyyy-mm-dd') as minfahrq,\n"+
	          "       to_char(max(fahrq), 'yyyy-mm-dd') as maxfahrq,\n"+
	           "      max(pz.mingc) as pinz,\n"+
	            "     max(ysfs.mingc) as yunsfs,\n"+
	             "    to_char(min(daohrq), 'yyyy-mm-dd') as mindaohrq,\n"+
	              "   to_char(max(daohrq), 'yyyy-mm-dd') as maxdaohrq,\n"+
	               "  max(g.quanc) as gongysqc,\n"+
	                " max(m.quanc) as meikdwqc,\n"+
	               "  max(cz.mingc) as faz,\n"+
	          "       max(cz.id) as faz_id,\n"+
	           "      max(dz.id) as daoz_id,\n"+
	            "     max(di.mingc)  yuanshdw,\n"+
	             "    max(f.diancxxb_id) as diancxxb_id\n"+
	          "      ,4 as jihkjb_id   --û��\n"+
	          "       ,max(f.chec) chec\n "+
	         "   from fayslb       f,\n"+
	          "       zhilb      z,\n"+
	          "       kuangfzlb  kz,\n"+
	           "      gongysb    g,\n"+
	            "     meikxxb    m,\n"+
	           "      chezxxb    cz,\n"+
	           "      pinzb      pz,\n"+
	          "       yunsfsb    ysfs,\n"+
	          "       chezxxb    dz, \n"+
	         "        diancxxb di\n"+
	         "  where f.zhilb_id = z.id(+) \n"+
	         "    and kz.id(+) = f.kuangfzlb_id\n"+
	         "    and f.faz_id = cz.id\n"+
	         "    and f.pinzb_id = pz.id\n"+
	         "    and f.gongysb_id = g.id\n"+
	         "    and f.meikxxb_id = m.id\n"+
	          "   and f.shr_diancxxb_id=di.id\n"+
	          "   and f.yunsfsb_id = ysfs.id\n"+
	          "   and f.daoz_id = dz.id\n"+
//	         "    and z.liucztb_id = 1\n"+
	         "    and f.id in ("+SelIds+"))\n";
	    	
            ResultSet rs = con.getResultSet(sql);
            
            if (rs.next()) {
            	
            	bsv.setFahksrq(rs.getDate("minfahrq"));
            	bsv.setFahjzrq(rs.getDate("maxfahrq"));
            	if (bsv.getFahksrq().equals(bsv.getFahjzrq())){
            		
            		bsv.setFahrq(Fayjsdcz.FormatDate(bsv.getFahksrq()));
            	}else{
            								
            		bsv.setFahrq(Fayjsdcz.FormatDate(bsv.getFahksrq()) +"��"+ Fayjsdcz.FormatDate(bsv.getFahjzrq()));
            	}
            	
            	bsv.setYansksrq(rs.getDate("mindaohrq"));
            	bsv.setYansjsrq(rs.getDate("maxdaohrq"));

            	if (bsv.getYansksrq().equals(bsv.getYansjsrq())){
            		
            		bsv.setDaohrq(Fayjsdcz.FormatDate(bsv.getYansksrq()));
            	}else{
            		
            		bsv.setDaohrq(Fayjsdcz.FormatDate(bsv.getYansksrq())+"��"+Fayjsdcz.FormatDate(bsv.getYansjsrq()));
            	}
            	
            	bsv.setHetb_Id(rs.getLong("hetb_id"));
            	bsv.setMeikxxb_Id(rs.getLong("meikxxb_id"));
                bsv.setGongysb_Id(rs.getLong("gongysb_id"));
            	bsv.setRanlpzb_Id(rs.getLong("pinzb_id"));
                bsv.setShoukdw(rs.getString("gongysqc"));
                bsv.setFahdw(rs.getString("meikdwqc"));
                bsv.setFaz(rs.getString("faz"));
                bsv.setFaz_Id(rs.getLong("faz_id"));
                bsv.setDaoz_Id(rs.getLong("daoz_id"));
                bsv.setYuanshr(rs.getString("yuanshdw"));
                bsv.setXianshr(rs.getString("yuanshdw"));
	          	bsv.setRanlpz(rs.getString("pinz"));
	          	bsv.setTianzdw(MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(Diancxxb_id)));
	          	bsv.setYunsfs(rs.getString("yunsfs"));
	          	bsv.setYunsfsb_id(rs.getLong("yunsfsb_id"));
	          	bsv.setJiesrq(Fayjsdcz.FormatDate(new Date()));	//��������
               
	          	bsv.setJiesbh(Fayjsdcz.getJiesbh(String.valueOf(Diancxxb_id),""));
	          	bsv.setDaibcc(MainGlobal.getShouwch(SelIds));
	          	bsv.setYunju_cf(rs.getDouble("yunju"));			//����
	          	bsv.setHetbh(MainGlobal.getTableCol("hetb", "hetbh", "id", String.valueOf(bsv.getHetb_Id())));
            	
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
	
	private Balances_variable getJiesszl(String Jieszbsftz,String SelIds,long Diancxxb_id,long Gongysb_id,
			long Hetb_id,double Jieskdl,double Shangcjsl){
		
//		������������
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
			
			String jies_Jqsl="jingz";								//�����Ȩ����
			String jies_Qnetarblxs="2";
			String jies_Stdblxs="2";
			String jies_Mtblxs="1";
			String jies_Madblxs="2";
			String jies_Aarblxs="2";
			String jies_Aadblxs="2";
			String jies_Adblxs="2";
			String jies_Vadblxs="2";
			String jies_Vdafblxs="2";
			String jies_Stadblxs="2";
			String jies_Starblxs="2";
			String jies_Hadblxs="2";
			String jies_Qbadblxs="2";
			String jies_Qgradblxs="2";
			String jies_T2blxs="2";
			String jies_shifykfzljs="��";
			String jies_Jieslqzfs="sum(round_new())";				//��������ȡ����ʽ
			String jies_Jsslblxs="0";								//������������С��λ
			String jies_Kdkskzqzfs="round_new(sum())";				//�۶֡���ˮ������ȡ����ʽ
			String jies_Jssl="jingz";			//��������
			String jies_yunfjssl="jingz";							//�˷ѽ�������
			boolean blnDandszyfjssl=false;	//�Ƿ񵥶��������˷ѽ����������������yunfjssl=���������������������Ʊ����yunfjssl=gongfsl
			
			String jies_Guohlqzfs=Locale.anlsswrhxj_jiesghlqzfs_xitxx;	//ϵͳ��Ϣ�й�����ȡ����ʽ��Ĭ��Ϊ������������������
			String jies_Guohlblxsw="0";									//ϵͳ��Ϣ�й���������С��λ��Ĭ��Ϊ��2
			String yunsdw="";	//���䵥λ������
//			if(Yunsdwb_id>-1){
////				ѡ�������䵥λ
//				yunsdw=" and cp.yunsdwb_id="+Yunsdwb_id;
//			}
			
			
			
//			jies_Jqsl=MainGlobal.getXitxx_item("����", Locale.jiaqsl_xitxx, 
//        			String.valueOf(Diancxxb_id),jies_Jqsl);
			
//			��ϵͳ��Ϣ����ȡ�������õ���Ϣ
			String XitxxArrar[][]=null;	
			XitxxArrar=MainGlobal.getXitxx_items("����", 
					"'"+Locale.jiaqsl_xitxx+"','"+Locale.jiesghlqzfs_xitxx+"','"+Locale.jiesghlblxsw_xitxx
					+"','"+Locale.meiksl_xitxx+"','"+Locale.yunfsl_xitxx+"'"
					,String.valueOf(Diancxxb_id));
			
//			����ȡ�õ�ֵ��Ȼ��Ա������и�ֵ
			if(XitxxArrar!=null){
				
				for(int i=0;i<XitxxArrar.length;i++){
					
					if(XitxxArrar[i][0]!=null){
						
						if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//							��Ȩ����
							jies_Jqsl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlqzfs_xitxx)){
//							������ȡ����ʽ
							jies_Guohlqzfs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlblxsw_xitxx)){
//							�������������С��λ
							jies_Guohlblxsw=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.meiksl_xitxx)){
//							ú��˰��
							bsv.setMeiksl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}else if(XitxxArrar[i][0].trim().equals(Locale.yunfsl_xitxx)){
//							�˷�˰��
							bsv.setYunfsl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}
					}
				}
			}
			
			if(jies_Guohlqzfs.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
//				������ȡ����ʽ:����������������
				jies_Guohlqzfs="sum(round_new())";
			}else if(jies_Guohlqzfs.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
//				������ȡ����ʽ:��Ӻ�ͳһ��������
				jies_Guohlqzfs="round_new(sum())";
			}else if(jies_Guohlqzfs.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
//				������ȡ����ʽ:������ȡ������
				jies_Guohlqzfs="sum()";
			}else{
//				������ȡ����ʽ:�����û�õ�Ĭ��Ϊ�����������������ӡ�
				jies_Guohlqzfs="sum(round_new())";
			}
			
			if(Fayjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){

	        	String JiesszArray[][]=null;
			
	        	JiesszArray=Fayjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
		
				for(int i=0;i<JiesszArray.length;i++){
					
					if(JiesszArray[i][0]!=null){
						
						if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
							
							jies_Jqsl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.mtblxsw_jies)){
							
							jies_Mtblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.madblxsw_jies)){
							
							jies_Madblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.aarblxsw_jies)){
							
							jies_Aarblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.aadblxsw_jies)){
							
							jies_Aadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.adblxsw_jies)){
							
							jies_Adblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.vadblxsw_jies)){
							
							jies_Vadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.vdafblxsw_jies)){
							
							jies_Vdafblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.stadblxsw_jies)){
							
							jies_Stadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.starblxsw_jies)){
						
							jies_Starblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.stdblxsw_jies)){
							
							jies_Stdblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.hadblxsw_jies)){
							
							jies_Hadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qnetarblxsw_jies)){
							
							jies_Qnetarblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qbadblxsw_jies)){
							
							jies_Qbadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qgradblxsw_jies)){
							
							jies_Qgradblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.shifykfzljs_jies)){
							
							jies_shifykfzljs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.meiksl_jies)){
							
							bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.yunfsl_jies)){
							
							bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
							
							jies_Jieslqzfs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.jiesslblxsw_jies)){
							
							jies_Jsslblxs=JiesszArray[i][1];
							bsv.setJiesslblxs(jies_Jsslblxs);
						}else if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
							
							jies_Jssl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.yunfjsslzcfs_jies)){
							
							jies_yunfjssl=JiesszArray[i][1];
							blnDandszyfjssl=true;
						}else if(JiesszArray[i][0].equals(Locale.user_custom_mlj_jiesgs)){
							
							bsv.setUser_custom_mlj_jiesgs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.user_custom_fmlj_jiesgs)){
							
							bsv.setUser_custom_fmlj_jiesgs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.yikj_yunfyymk_jies)){
							
							bsv.setYikj_yunfyymk(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.yikj_meikyyyf_jies)){
							
							bsv.setYikj_meikyyyf(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.meikhsdjblxsw_jies)){
							
//							ú�˰���۱���С��λ
							bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
							
//							�˷Ѻ�˰���۱���С��λ
							bsv.setYunfhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.kuidjfyf_jies)){
							
//							���־ܸ��˷�
							bsv.setKuidjfyf(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.Mj_to_kcal_xsclfs_jies)){
							
//							�׽�ת��
							bsv.setMj_to_kcal_xsclfs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.meikhsdjqzfs_jies)){
							
//							��˰����ȡ����ʽ
							bsv.setMeikhsdj_qzfs(JiesszArray[i][1]);
						}
					}
				}
	        }
//			��������������������������ú�û�¡�������������ǹ�����������Ʊ��������� ��danjcpb,yunfdjb���з�����������yunfjsb_id=0 or null
			String contion_table="";//
			String contion_where="";
			
//			contion_table=",danjcpb dj,(select * from yunfdjb yd where yd.feiylbb_id="+Locale.guotyf_feiylbb_id+") yd";
//			contion_where=" and cp.id=dj.chepb_id(+) and dj.yunfdjb_id=yd.id(+) and (dj.yunfjsb_id=0 or dj.yunfjsb_id is null) ";
			
			StringBuffer sql=new StringBuffer("");
			
			if(Jieszbsftz.equals("��")){
				
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Aar_cf,0) as Aar_cf,nvl(Aar_kf,0) as Aar_kf,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Ad_cf,0) as Ad_cf,				\n");
				sql.append(" 		nvl(Ad_kf,0) as Ad_kf,nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Stad_cf,0) as Stad_cf,nvl(Stad_kf,0) as Stad_kf,/*nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,*/nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,	\n");
				sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Shoumjsdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,	\n");
//				sql.append(			"0 as koud,0 as kous,0 as kouz,	\n");
//				sql.append(" 		max(f.chec) as ches, 0 as biaoz,0 as yingk,0 as yingd, 	\n");
//				sql.append(        " sum(meil) as jiessl,   \n");
//				sql.append(        " 0 as yunfjssl,0 as chaokdl,   \n");
				sql.append(			Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches, "+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Fayjsdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
				sql.append(         Fayjsdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
				sql.append("	 --��������   																															\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qnet_ar)/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_cf,   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_cf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_cf,  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_cf, 			 	\n");
//				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf,				 		\n");
//				sql.append("	 --������   \n");
				sql.append("     --������׼ \n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qnet_ar)/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_kf,  		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_kf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_kf,  			\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_kf, 			 	\n");
//				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_kf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_kf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_kf					 	\n");
				sql.append("	 	from ( select distinct f.id, f.diancxxb_id fahdw,f.meil, f.diancxxb_id, f.gongysb_id, f.meikxxb_id, f.pinzb_id, f.faz_id, f.daoz_id, f.fahrq, f.daohrq,									\n");
				sql.append("				f.hetb_id, f.zhilb_id, f.yunsfsb_id,f.chec, 0 as chaokdl, f.kuangfzlb_id									\n");
				sql.append("       			from fayslb f where f.id in ("+SelIds+") 												\n");
				
//				if(!(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib)){
////					������ǽ���ú�����Ʊ������ȡԭƱ��
//					sql.append("			f.biaoz as biaoz,	\n");
//				}else{
////					�������ȡ�������Ʊ��
//					sql.append("			decode(yfzl.jifzl,null,f.biaoz,yfzl.jifzl) as biaoz,	\n");
//				}
				
//				sql.append("       			f.yingd as yingd,f.yingk as yingk,f.yuns as yuns,f.koud as koud,f.kous as kous,				\n");
//				sql.append("       			f.kouz as kouz,f.koum as koum,f.zongkd as zongkd,f.sanfsl as sanfsl,f.ches as ches,			\n");
//				sql.append("       			0 as chaokdl, 						\n");
//				sql.append("				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
//				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc																\n");
//				sql.append(" 			from fahb f"+contion_table+" where  "+contion_where+"  f.lie_id in("+SelIds+")"+yunsdw+" 													\n"); 
//				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
//				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
//				sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
//				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc															\n");
				sql.append("			) f,fahbtmp ftmp,zhilb z,gongysb g,meikxxb m,chezxxb cz,pinzb pz,yunsfsb ysfs,chezxxb dz,  									\n");
				sql.append("	 	(select distinct f.id as fahb_id,k.* from fayslb f,zhilb k where f.zhilb_id=k.id "+yunsdw+" and f.id in ("+SelIds+")) kz			\n");
				sql.append("			where f.zhilb_id=z.id and ftmp.fhfy_id=f.id and f.id=kz.fahb_id(+) and f.faz_id=cz.id and f.pinzb_id=pz.id   								\n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id  and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				 and z.liucztb_id=1 and f.id in("+SelIds+")) 	\n");
			
			}else if(Jieszbsftz.equals("��")){
				
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Qnetar_js,0) as Qnetar_js,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Std_js,0) as Std_js,\n ");
				sql.append("		nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mt_js,0) as Mt_js,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Mad_js,0) as Mad_js,nvl(Aar_cf,0) as Aar_cf,\n ");
				sql.append("		nvl(Aar_kf,0) as Aar_kf,nvl(Aar_js,0) as Aar_js,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Aad_js,0) as Aad_js,nvl(Ad_cf,0) as Ad_cf,nvl(Ad_kf,0) as Ad_kf,nvl(Ad_js,0) as Ad_js,	\n");
				sql.append(" 		nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vad_js,0) as Vad_js,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Vdaf_js,0) as Vdaf_js,nvl(Stad_cf,0) as Stad_cf, \n ");
				sql.append("		nvl(Stad_kf,0) as Stad_kf,nvl(Stad_js,0) as Stad_js,/*nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(star_js,0) as star_js,*/nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf, \n ");
				sql.append("		nvl(Had_js,0) as Had_js,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qbad_js,0) as Qbad_js,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(Qgrad_js,0) as Qgrad_js, \n ");
				sql.append("		nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,nvl(T2_js,0) as T2_js,	\n");
				sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(jingz+yuns-Jiessl_kf) as yingk,Jiessl_js as jiessl,(Jiessl_js-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-(jingz+yuns-Jiessl_kf) as kuid from (select "+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Fayjsdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,						\n");
				sql.append(			Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Fayjsdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl   \n");
				sql.append("	 --��������   																															\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qnetar_zhibb+"','changf') as Qnetar_cf,   		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Std_zhibb+"','changf') as Std_cf,   		   		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mt_zhibb+"','changf') as Mt_cf,		  			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mad_zhibb+"','changf') as Mad_cf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aar_zhibb+"','changf') as Aar_cf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aad_zhibb+"','changf') as Aad_cf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Ad_zhibb+"','changf') as Ad_cf,		  			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vad_zhibb+"','changf') as Vad_cf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vdaf_zhibb+"','changf') as Vdaf_cf,  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Stad_zhibb+"','changf') as Stad_cf, 			 	\n");
//				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Star_zhibb+"','changf') as Star_cf, 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Had_zhibb+"','changf') as Had_cf,	 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qbad_zhibb+"','changf') as Qbad_cf, 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qgrad_zhibb+"','changf') as Qgrad_cf,		 		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.T2_zhibb+"','changf') as T2_cf,				 	\n");
				sql.append("	 --������   \n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.jiessl_zhibb+"','gongf') as Jiessl_kf,   			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qnetar_zhibb+"','gongf') as Qnetar_kf,   			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Std_zhibb+"','gongf') as Std_kf,   		   		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mt_zhibb+"','gongf') as Mt_kf,		  			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mad_zhibb+"','gongf') as Mad_kf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aar_zhibb+"','gongf') as Aar_kf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aad_zhibb+"','gongf') as Aad_kf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Ad_zhibb+"','gongf') as Ad_kf,		  			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vad_zhibb+"','gongf') as Vad_kf,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vdaf_zhibb+"','gongf') as Vdaf_kf,  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Stad_zhibb+"','gongf') as Stad_kf, 			 	\n");
//				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Star_zhibb+"','gongf') as Star_kf, 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Had_zhibb+"','gongf') as Had_kf,	 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qbad_zhibb+"','gongf') as Qbad_kf, 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qgrad_zhibb+"','gongf') as Qgrad_kf,		 		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.T2_zhibb+"','gongf') as T2_kf,				 	\n");
				sql.append("	 --����ָ��   \n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.jiessl_zhibb+"','jies') as Jiessl_js,   			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qnetar_zhibb+"','jies') as Qnetar_js,   			\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Std_zhibb+"','jies') as Std_js,   		   		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mt_zhibb+"','jies') as Mt_js,		  				\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mad_zhibb+"','jies') as Mad_js,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aar_zhibb+"','jies') as Aar_js,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aad_zhibb+"','jies') as Aad_js,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Ad_zhibb+"','jies') as Ad_js,		  				\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vad_zhibb+"','jies') as Vad_js,	  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vdaf_zhibb+"','jies') as Vdaf_js,  			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Stad_zhibb+"','jies') as Stad_js, 			 	\n");
//				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Star_zhibb+"','jies') as Star_js, 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Had_zhibb+"','jies') as Had_js,	 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qbad_zhibb+"','jies') as Qbad_js, 			 	\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qgrad_zhibb+"','jies') as Qgrad_js,		 		\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.T2_zhibb+"','jies') as T2_js				 		\n");
				
				sql.append("	 	from (select distinct f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,									\n");
				sql.append("				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,									\n");
				sql.append("       			f.maoz as maoz,f.piz as piz,sum(f.maoz-f.piz-f.zongkd) as jingz,												\n");
				
//				if(!(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib)){
////					������ǽ���ú�����Ʊ������ȡԭƱ��
					sql.append("			f.biaoz as biaoz,	\n");
//				}else{
////					�������ȡ�������Ʊ��
//					sql.append("			decode(yfzl.jifzl,null,f.biaoz,yfzl.jifzl) as biaoz,	\n");
//				}
				
				sql.append("       			f.yingd as yingd,f.yingk as yingk,f.yuns as yuns,f.koud as koud,f.kous as kous,				\n");
				sql.append("       			f.kouz as kouz,f.koum as koum,f.zongkd as zongkd,f.sanfsl as sanfsl,f.ches as ches,			\n");
				sql.append("       			0 as chaokdl, 						\n");
				sql.append("       			f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc																\n");
				sql.append(" 			from fayslb f"+contion_table+" where  f.lie_id in("+SelIds+")"+yunsdw+" 													\n"); 
//				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
//				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
//				sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
//				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc															\n");
				sql.append("			) f,yansbhb y,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz  									\n");
				sql.append("			where f.faz_id=cz.id and f.pinzb_id=pz.id and f.yansbhb_id=y.id \n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.liucztb_id=0 and f.lie_id in("+SelIds+")) 	\n");
			}
			
			rsl=con.getResultSetList(sql.toString());
			if(rsl.next()){
				
//				����
				bsv.setKoud(rsl.getDouble("koud"));					//�۶�
            	bsv.setKous(rsl.getDouble("kous"));					//��ˮ
            	bsv.setKouz(rsl.getDouble("kouz"));					//����
//            	bsv.setChes(rsl.getLong("ches"));					    //����
            	
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//����
            	bsv.setYingksl(rsl.getDouble("yingk"));				//ӯ��  
            	bsv.setYingd(rsl.getDouble("yingd"));				//ӯ��
            	bsv.setKuid(rsl.getDouble("kuid"));					
            	
            	bsv.setJiessl(rsl.getDouble("jiessl")-Jieskdl);			//��������
            	
//            	if(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib){
//            		
//            		bsv.setJiessl(rsl.getDouble("yunfjssl")-Jieskdl);
//            	}
				
            	bsv.setYanssl(rsl.getDouble("jingz"));				//������������
            	bsv.setJingz(rsl.getDouble("jingz"));				//����
            	bsv.setKoud_js(Jieskdl);											//����۶�
            	
            	bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl()-bsv.getJingz()),2));	//������������(�������͹������Ĳ�ֵ)
//            	bsv.setJiesslcy(rsl.getDouble("jieslcy"));			//������������
            	bsv.setYuns(rsl.getDouble("yuns"));					//ʵ������
            	bsv.setYunfjsl(rsl.getDouble("yunfjssl")-Jieskdl);	//�˷ѽ�������
            	
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
	            bsv.setStar_cf(rsl.getDouble("Star_cf"));
	            bsv.setHad_cf(rsl.getDouble("Had_cf"));
	            bsv.setQbad_cf(rsl.getDouble("Qbad_cf"));
	            bsv.setQgrad_cf(rsl.getDouble("Qgrad_cf"));
	            bsv.setT2_cf(rsl.getDouble("T2_cf"));
	            
//              ��ָ��
                bsv.setQnetar_kf(rsl.getDouble("Qnetar_kf"));
                bsv.setStd_kf(rsl.getDouble("Std_kf")); 
                bsv.setStar_kf(rsl.getDouble("Star_kf"));
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
                if(jies_shifykfzljs.equals("��")){
                	
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
                    bsv.setStar_js(rsl.getDouble("Star_js"));
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
                    bsv.setStar_js(rsl.getDouble("Star"+strcforkf));
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
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return bsv;
	}
	
//	���ƹ����ݹ��㷨���õ���������
	
	private void getJiesszl_PerFah(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id,double Jiesrl){
		
//		������������
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
			
			String jies_Jqsl="meil";								//�����Ȩ����
			String jies_Qnetarblxs="2";
			String jies_Stdblxs="2";
			String jies_Mtblxs="1";
			String jies_Madblxs="2";
			String jies_Aarblxs="2";
			String jies_Aadblxs="2";
			String jies_Adblxs="2";
			String jies_Vadblxs="2";
			String jies_Vdafblxs="2";
			String jies_Stadblxs="2";
			String jies_Hadblxs="2";
			String jies_Qbadblxs="2";
			String jies_Qgradblxs="2";
			String jies_T2blxs="2";
			String jies_shifykfzljs="��";
			String jies_Jieslqzfs="sum(round_new())";				//��������ȡ����ʽ
			String jies_Jsslblxs="0";								//������������С��λ
			String jies_Kdkskzqzfs="round_new(sum())";				//�۶֡���ˮ������ȡ����ʽ
			String jies_Jssl="meil";			//��������
			String jies_yunfjssl="meil";							//�˷ѽ�������
			
			String jies_Guohlqzfs=Locale.anlsswrhxj_jiesghlqzfs_xitxx;	//ϵͳ��Ϣ�й�����ȡ����ʽ��Ĭ��Ϊ������������������
			String jies_Guohlblxsw="0";									//ϵͳ��Ϣ�й���������С��λ��Ĭ��Ϊ��2

//			��ϵͳ��Ϣ����ȡ�������õ���Ϣ
			String XitxxArrar[][]=null;	
			XitxxArrar=MainGlobal.getXitxx_items("����", 
					"'"+Locale.jiaqsl_xitxx+"','"+Locale.jiesghlqzfs_xitxx+"','"+Locale.jiesghlblxsw_xitxx
					+"','"+Locale.meiksl_xitxx+"','"+Locale.yunfsl_xitxx+"'"
					,String.valueOf(Diancxxb_id));
			
//			����ȡ�õ�ֵ��Ȼ��Ա������и�ֵ
			if(XitxxArrar!=null){
				
				for(int i=0;i<XitxxArrar.length;i++){
					
					if(XitxxArrar[i][0]!=null){
						
						if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//							��Ȩ����
							jies_Jqsl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlqzfs_xitxx)){
//							������ȡ����ʽ
							jies_Guohlqzfs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlblxsw_xitxx)){
//							�������������С��λ
							jies_Guohlblxsw=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.meiksl_xitxx)){
//							ú��˰��
							bsv.setMeiksl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}else if(XitxxArrar[i][0].trim().equals(Locale.yunfsl_xitxx)){
//							�˷�˰��
							bsv.setYunfsl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}
					}
				}
			}
			
			if(jies_Guohlqzfs.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
//				������ȡ����ʽ:����������������
				jies_Guohlqzfs="sum(round_new())";
			}else if(jies_Guohlqzfs.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
//				������ȡ����ʽ:��Ӻ�ͳһ��������
				jies_Guohlqzfs="round_new(sum())";
			}else if(jies_Guohlqzfs.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
//				������ȡ����ʽ:������ȡ������
				jies_Guohlqzfs="sum()";
			}else{
//				������ȡ����ʽ:�����û�õ�Ĭ��Ϊ�����������������ӡ�
				jies_Guohlqzfs="sum(round_new())";
			}
			
			if(Fayjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){

	        	String JiesszArray[][]=null;
			
	        	JiesszArray=Fayjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
		
				for(int i=0;i<JiesszArray.length;i++){
					
					if(JiesszArray[i][0]!=null){
						
						if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
							
							jies_Jqsl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.mtblxsw_jies)){
							
							jies_Mtblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.madblxsw_jies)){
							
							jies_Madblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.aarblxsw_jies)){
							
							jies_Aarblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.aadblxsw_jies)){
							
							jies_Aadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.adblxsw_jies)){
							
							jies_Adblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.vadblxsw_jies)){
							
							jies_Vadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.vdafblxsw_jies)){
							
							jies_Vdafblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.stadblxsw_jies)){
							
							jies_Stadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.stdblxsw_jies)){
							
							jies_Stdblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.hadblxsw_jies)){
							
							jies_Hadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qnetarblxsw_jies)){
							
							jies_Qnetarblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qbadblxsw_jies)){
							
							jies_Qbadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qgradblxsw_jies)){
							
							jies_Qgradblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.shifykfzljs_jies)){
							
							jies_shifykfzljs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.meiksl_jies)){
							
							bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.yunfsl_jies)){
							
							bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
							
							jies_Jieslqzfs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.jiesslblxsw_jies)){
							
							jies_Jsslblxs=JiesszArray[i][1];
							bsv.setJiesslblxs(jies_Jsslblxs);
						}else if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
							
							jies_Jssl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.yunfjsslzcfs_jies)){
							
							jies_yunfjssl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.user_custom_mlj_jiesgs)){
							
							bsv.setUser_custom_mlj_jiesgs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.user_custom_fmlj_jiesgs)){
							
							bsv.setUser_custom_fmlj_jiesgs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.yikj_yunfyymk_jies)){
							
							bsv.setYikj_yunfyymk(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.yikj_meikyyyf_jies)){
							
							bsv.setYikj_meikyyyf(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.meikhsdjblxsw_jies)){
							
//							ú�˰���۱���С��λ
							bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
							
//							�˷Ѻ�˰���۱���С��λ
							bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}
					}
				}
	        }
			
			StringBuffer sql=new StringBuffer("");
			
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Std_cf,0) as Std_cf,nvl(Mt_cf,0) as Mt_cf,nvl(Mad_cf,0) as Mad_cf,nvl(Aar_cf,0) as Aar_cf,nvl(Aad_cf,0) as Aad_cf,nvl(Ad_cf,0) as Ad_cf,				\n");
				sql.append(" 		nvl(Vad_cf,0) as Vad_cf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Stad_cf,0) as Stad_cf,nvl(Had_cf,0) as Had_cf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(T2_cf,0) as T2_cf,	\n");
				sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,yingd,yingd-yingk as kuid from (select "+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,sum(meil) as jingz,						\n");
//				sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,yingd,yingd-yingk as kuid from (select "+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz,						\n");
				sql.append(			Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Fayjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches, sum(meil) as biaoz,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "meil+yuns-meil", jies_Jsslblxs)+" as yingk,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
//				sql.append(" 		sum(ches) as ches, "+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Fayjsdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
//				sql.append(         Fayjsdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
				sql.append(         "sum(meil) as jiessl,   \n");
				sql.append(         Fayjsdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,   \n");
				sql.append("	 --��������   																															\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qnet_ar)/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_cf,   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_cf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_cf,  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf				 		\n");
				
				sql.append("	 	from fayslb fy,fahbtmp f,zhilb z,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz  								\n");
				sql.append("			where fy.zhilb_id=z.id(+) and fy.faz_id=cz.id and fy.pinzb_id=pz.id   and fy.id=f.fayslb_id(+)							\n");
				sql.append("				and fy.gongysb_id=g.id and fy.meikxxb_id=m.id and fy.shr_diancxxb_id=vwydw.id(+) and fy.yunsfsb_id=ysfs.id and fy.daoz_id=dz.id   	\n");
				sql.append("				and fy.id in("+SelIds+")) 	\n");
			
			rsl=con.getResultSetList(sql.toString());
			if(rsl.next()){
				
//				����
				bsv.setKoud(rsl.getDouble("koud"));					//�۶�
            	bsv.setKous(rsl.getDouble("kous"));					//��ˮ
            	bsv.setKouz(rsl.getDouble("kouz"));					//����
            	bsv.setChes(rsl.getLong("ches"));					//����
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//����
            	bsv.setYingksl(rsl.getDouble("yingk"));				//ӯ��  
            	bsv.setYingd(rsl.getDouble("yingd"));				//ӯ��
            	bsv.setKuid(rsl.getDouble("kuid"));					//����
            	bsv.setJiessl(rsl.getDouble("jiessl"));		//��������
            	bsv.setYanssl(rsl.getDouble("jingz"));				//������������
            	bsv.setJingz(rsl.getDouble("jingz"));				//����
            	bsv.setJiesslcy(bsv.getJiessl()-bsv.getJingz());	//������������(�������͹������Ĳ�ֵ)
            	bsv.setYuns(rsl.getDouble("yuns"));					//ʵ������
            	bsv.setYunfjsl(rsl.getDouble("yunfjssl"));			//�˷ѽ�������
				
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
	            bsv.setHad_cf(rsl.getDouble("Had_cf"));
	            bsv.setQbad_cf(rsl.getDouble("Qbad_cf"));
	            bsv.setQgrad_cf(rsl.getDouble("Qgrad_cf"));
	            bsv.setT2_cf(rsl.getDouble("T2_cf"));
                
//              ����ָ��
                String strcforkf="_cf";
                
                bsv.setQnetar_js(rsl.getDouble("Qnetar"+strcforkf));
                if(Jiesrl>0){
//                	ȡ��һ�εĽ����������Ǻ�ͬ������׼�е�����
                	bsv.setQnetar_js(Jiesrl);
                }	
                bsv.setStd_js(rsl.getDouble("Std"+strcforkf)); 
                bsv.setMt_js(rsl.getDouble("Mt"+strcforkf));
                bsv.setMad_js(rsl.getDouble("Mad"+strcforkf));
                bsv.setAar_js(rsl.getDouble("Aar"+strcforkf));
                bsv.setAad_js(rsl.getDouble("Aad"+strcforkf));
                bsv.setAd_js(rsl.getDouble("Ad"+strcforkf));
                bsv.setVad_js(rsl.getDouble("Vad"+strcforkf));
                bsv.setVdaf_js(rsl.getDouble("Vdaf"+strcforkf));
                bsv.setStad_js(rsl.getDouble("Stad"+strcforkf));
                bsv.setHad_js(rsl.getDouble("Had"+strcforkf));
                bsv.setQbad_js(rsl.getDouble("Qbad"+strcforkf));
                bsv.setQgrad_js(rsl.getDouble("Qgrad"+strcforkf));
                bsv.setT2_js(rsl.getDouble("T2"+strcforkf));
                
                bsv.setYunju_js(bsv.getYunju_cf());		//�˾ำֵ
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
//	���ƹ����ݹ��㷨���õ���������_End
	
	//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
	private boolean getMeiPrice(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String Jieszbsftz,String SelIds,long Gongysb_id,
									double Jieskdl,double Shangcjsl){
		//�õ���ͬ��Ϣ�е��˼�
		JDBCcon con =new JDBCcon();
		String sql="";
		Interpreter bsh=new Interpreter();
		Fayjsdcz Jscz=new Fayjsdcz();
		try{
			
//			����(��ͬ������Ϊ��λ��ÿ�´�һ�����ݶ���ȡһ����)
			sql="select htsl.hetl,htb.gongfkhyh,htb.gongfzh \n"
				+ " from hetb htb, hetslb htsl	\n"
				+ " where htb.id=htsl.hetb_id	\n"      
				+ " and htsl.pinzb_id="+Ranlpzb_id+" and yunsfsb_id="+Yunsfsb_id+" and faz_id="+Faz_id+" and daoz_id="+Daoz_id+"	\n"
				+ " and htsl.diancxxb_id="+Diancxxb_id+" and htsl.riq<=to_date('"+Fayjsdcz.FormatDate(Minfahrq)+"','yyyy-MM-dd')	\n"              
				+ " and htb.id="+Hetb_id+" order by riq desc ";
			
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()){
				
				bsv.setHetml(rsl.getString("hetl"));
				bsv.setKaihyh(rsl.getString("gongfkhyh"));
				bsv.setZhangH(rsl.getString("gongfzh"));
			}
			
//			����(��ͬ��һ����ͬ�Ŷ�Ӧ���������¼)
			sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
				+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
				+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
				+ " and htzl.danwb_id=dwb.id	\n"
				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
				+ " and htb.id="+Hetb_id+" ";
			
//			�۸񣨺�ͬ��һ����ͬ��Ӧ��������۸�	
//			sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,				\n"
//				+ " jijdw.bianm as jijdw,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,					\n"
//				+ " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs			\n"
//				+ " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
//				+ " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
//				+ " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n" 
//				+ " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
//				+ " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n" 
//				+ " and htjg.yunsfsb_id=ysfs.id																\n"
//				+ " and htjg.hetjjfsb_id=htjfsb.id															\n"
//				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"          
//				+ " and htb.id="+Hetb_id+"";
			
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
				
				bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
				bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
				bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
				bsv.setHetmdjgs(Fayzgcl.getTransform_Hetjgs(
						rsl.getString("jijgs").trim(),
						rsl.getString("danw"),
						rsl.getDouble("jij"),
						bsv.getDiancxxb_id()));		//��ͬ���۹�ʽ
				bsv.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
				bsv.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
				bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
				bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
				bsv.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
			
				bsh.set("������ʽ", bsv.getJiesxs());
				bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
				bsh.set("�۸�λ", bsv.getHetmdjdw());	
				bsh.set("��ͬ�۸�", bsv.getHetmdj());
				bsh.set("���ú��", bsv.getZuigmj());
				bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());
				
//				��ͬ����ָ��,ȡ�����������ĺ�ͬ����
				if(rsl.getRows()==1){
					
//					��һ����ͬ
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								Ŀ¼�۽���
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//								�����ν���
							
							String[] test=null;
							
							test=SelIds.split(",");
							
//							if(SelIds.indexOf(",")==-1){
//								
//								test[0]=SelIds;
//							}else{
//								
//								test=SelIds.split(",");
//							}
							
							for(int i=0;i<test.length;i++){
								
//								��ý�������������
								getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs,bsv.getUser_custom_mlj_jiesgs());			
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0);
								
//								����ú����
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						��Ȩƽ��
							
//							��ý�������������
							getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
							
//							ΪĿ¼�۸�ֵ
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							������ۿ�
							getZengkk(Hetb_id,bsh);
							
//							ú�˰���۱���С��λ
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							�û��Զ��幫ʽ
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							ִ�к�ͬ�۸�ʽ
							ExecuteHetmdjgs(bsh);
							
//							ִ�й�ʽ
							bsh.eval(bsv.getGongs_Mk());
							
//							�õ�������ָ��
							setJieszb(bsh,0);
							
//							����ú����
							computData();
						}
						
					}else{		
						
//						һ����ͬ
//						��Ŀ¼��
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//							�����ν���
							
							String[] test=null;
							test=SelIds.split(",");
							
//							if(SelIds.indexOf(",")==-1){
//								
//								test[0]=SelIds;
//							}else{
//								
//								test=SelIds.split(",");
//							}
							
							for(int i=0;i<test.length;i++){
								
//								��ý�������������
								getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
								double Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
								
								bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
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
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_fmlj_jiesgs,bsv.getUser_custom_fmlj_jiesgs());
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0);
								
//								����ú����
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
							
//							��ý�������������
							getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
							
							double Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
							Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
							
							bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
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
							
//							������ۿ�
							getZengkk(Hetb_id,bsh);
							
//							�û��Զ��幫ʽ
							bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
							
//							ú�˰���۱���С��λ
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							ִ�к�ͬ�۸�ʽ
							ExecuteHetmdjgs(bsh);
							
//							ִ�й�ʽ
							bsh.eval(bsv.getGongs_Mk());
							
//							�õ�������ָ��
							setJieszb(bsh,0);
							
//							����ú����
							computData();
						}
					}
					
					bsv.setHetjgpp_Flag(true);
					bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
				}else{
//					�ж����ͬ
//					Ŀ¼��
					
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//					Ŀ¼�۽���
					
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){		//					�����ν���
						
							String[] test=null;
							test=SelIds.split(",");
//							if(SelIds.indexOf(",")==-1){
//								
//								test[0]=SelIds;
//							}else{
//								
//								test=SelIds.split(",");
//							}
							
							for(int i=0;i<test.length;i++){
								
//								��ý�������������
								getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0);
								
//								����ú����
								computData_Dpc();
							}
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��

							
//							��ý�������������
							getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
							
//							ΪĿ¼�۸�ֵ
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							������ۿ�
							getZengkk(Hetb_id,bsh);
							
//							�û��Զ��幫ʽ
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							ú�˰���۱���С��λ
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							ִ�к�ͬ�۸�ʽ
							ExecuteHetmdjgs(bsh);
							
//							ִ�й�ʽ
							bsh.eval(bsv.getGongs_Mk());
							
//							�õ�������ָ��
							setJieszb(bsh,0);
							
//							����ú����
							computData();
						}
					}else{
						
//						�����ͬ
//						��Ŀ¼��
						double	Dbljijzb=0;
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){	//�����ν���
							
								String[] test=null;
								
								test=SelIds.split(",");
//								if(SelIds.indexOf(",")==-1){
//									
//									test[0]=SelIds;
//								}else{
//									
//									test=SelIds.split(",");
//								}
						
								for(int i=0;i<test.length;i++){
									
									ResultSetList rsltmp=rsl;
									
//									��ý�������������
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
									
									do{
										
										Dbljijzb=Fayjsdcz.getZhib_info(bsv,rsltmp.getString("zhib"),"js");
										Dbljijzb=Fayjsdcz.getUnit_transform(rsltmp.getString("zhib"),rsltmp.getString("danw"),Dbljijzb);
									
										if(Dbljijzb>=rsltmp.getDouble("xiax")&&Dbljijzb<=(rsltmp.getDouble("shangx")==0?9999:rsltmp.getDouble("shangx"))){
										
											bsv.setHetmdj(rsltmp.getDouble("jij"));			//����ú����
											bsv.setZuigmj(rsltmp.getDouble("zuigmj"));			//���ú��
											bsv.setHetmdjdw(rsltmp.getString("jijdw"));		//��ͬú���۵�λ
											bsv.setHetmdjgs(Fayzgcl.getTransform_Hetjgs(
													rsl.getString("jijgs").trim(),
													rsl.getString("danw"),
													rsl.getDouble("jij"),
													bsv.getDiancxxb_id()));		//��ͬ���۹�ʽ
											bsv.setJiesfs(rsltmp.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
											bsv.setJiesxs(rsltmp.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
											bsv.setHetyj(rsltmp.getDouble("yunj"));			//��ͬ�˼۵���
											bsv.setHetyjdw(rsltmp.getString("yunjdw"));		//��ͬ�˼۵�λ
											bsv.setHetjjfs(rsltmp.getString("hejfs"));		//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
										
											bsh.set("������ʽ", bsv.getJiesxs());
											bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
											bsh.set("�۸�λ", bsv.getHetmdjdw());	
											bsh.set("��ͬ�۸�", bsv.getHetmdj());
											bsh.set("���ú��", bsv.getZuigmj());
											bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());
											
											bsh.set(rsltmp.getString("zhib")+Fayjsdcz.getZhibbdw(rsltmp.getString("zhib"),rsltmp.getString("danw")),Dbljijzb);	//����ֵ
											bsh.set(rsltmp.getString("zhib")+"������λ", 	rsltmp.getString("danw"));
											
											bsv.setYifzzb(rsltmp.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
											
											bsh.set(rsltmp.getString("zhib")+"����", 		rsltmp.getDouble("shangx"));		//ָ������
											bsh.set(rsltmp.getString("zhib")+"����", 		rsltmp.getDouble("xiax"));			//ָ������
											
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
											getZengkk(Hetb_id,bsh);
											
//											�û��Զ��幫ʽ
											bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
											
//											ú�˰���۱���С��λ
											bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
											
//											ִ�к�ͬ�۸�ʽ
											ExecuteHetmdjgs(bsh);
											
//											ִ�й�ʽ
											bsh.eval(bsv.getGongs_Mk());
											
//											�õ�������ָ��
											setJieszb(bsh,0);
											
//											����ú����
											computData_Dpc();
											
											bsv.setHetjgpp_Flag(true);
										}	
									}while(rsltmp.next());
								}		
								
									
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
							
//									��ý�������������
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
								do{
									
									Dbljijzb=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
									Dbljijzb=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
									
									if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
										
										bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
										bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
										bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
										bsv.setHetmdjgs(Fayzgcl.getTransform_Hetjgs(
												rsl.getString("jijgs").trim(),
												rsl.getString("danw"),
												rsl.getDouble("jij"),
												bsv.getDiancxxb_id()));		//��ͬ���۹�ʽ
										
										bsv.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
										bsv.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
										bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
										bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
										bsv.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
									
										bsh.set("������ʽ", bsv.getJiesxs());
										bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
										bsh.set("�۸�λ", bsv.getHetmdjdw());	
										bsh.set("��ͬ�۸�", bsv.getHetmdj());
										bsh.set("���ú��", bsv.getZuigmj());
										bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());
										
										bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//��ͬ�۸��id
										
										bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
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
										
//											������ۿ�
										getZengkk(Hetb_id,bsh);
										
//											�û��Զ��幫ʽ
										bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
										
//										ú�˰���۱���С��λ
										bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
										
//										ִ�к�ͬ�۸�ʽ
										ExecuteHetmdjgs(bsh);
										
//											ִ�й�ʽ
										bsh.eval(bsv.getGongs_Mk());
										
//											�õ�������ָ��
										setJieszb(bsh,0);
										
//											����ú����
										computData();
										
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
	
//	���ƹ����ݹ��ã�����ú���㷨
	
//	����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
	private boolean getMeiPrice_PerFah(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String SelIds,long Gongysb_id,double Jiesrl){
		//�õ���ͬ��Ϣ�е��˼�
		JDBCcon con =new JDBCcon();
		String sql="";
		Interpreter bsh=new Interpreter();
		Fayjsdcz Jscz=new Fayjsdcz();
		try{
			
//			����(��ͬ������Ϊ��λ��ÿ�´�һ�����ݶ���ȡһ����)
			sql="select htsl.hetl,htb.gongfkhyh,htb.gongfzh \n"
				+ " from hetb htb, hetslb htsl	\n"
				+ " where htb.id=htsl.hetb_id	\n"      
				+ " and htsl.pinzb_id="+Ranlpzb_id+" and yunsfsb_id="+Yunsfsb_id+" and faz_id="+Faz_id+" and daoz_id="+Daoz_id+"	\n"
				+ " and htsl.diancxxb_id="+Diancxxb_id+" and htsl.riq<=to_date('"+Fayjsdcz.FormatDate(Minfahrq)+"','yyyy-MM-dd')	\n"              
				+ " and htb.id="+Hetb_id+" order by riq desc ";
			
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()){
				
				bsv.setHetml(rsl.getString("hetl"));
				bsv.setKaihyh(rsl.getString("gongfkhyh"));
				bsv.setZhangH(rsl.getString("gongfzh"));
			}
			
//			����(��ͬ��һ����ͬ�Ŷ�Ӧ���������¼)
			sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
				+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
				+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
				+ " and htzl.danwb_id=dwb.id	\n"
				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
				+ " and htb.id="+Hetb_id+" ";
			
//			�۸񣨺�ͬ��һ����ͬ��Ӧ��������۸�	
//			sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,				\n"
//				+ " jijdw.bianm as jijdw,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,					\n"
//				+ " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs			\n"
//				+ " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
//				+ " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
//				+ " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n" 
//				+ " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
//				+ " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n" 
//				+ " and htjg.yunsfsb_id=ysfs.id																\n"
//				+ " and htjg.hetjjfsb_id=htjfsb.id															\n"
//				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"          
//				+ " and htb.id="+Hetb_id+"";
			
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
				
				bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
				bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
				bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
				bsv.setHetmdjgs(Fayzgcl.getTransform_Hetjgs(
						rsl.getString("jijgs").trim(),
						rsl.getString("danw"),
						rsl.getDouble("jij"),
						bsv.getDiancxxb_id()));		//��ͬ���۹�ʽ
				bsv.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
				bsv.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
				bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
				bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
				bsv.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
			
				bsh.set("������ʽ", bsv.getJiesxs());
				bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
				bsh.set("�۸�λ", bsv.getHetmdjdw());	
				bsh.set("��ͬ�۸�", bsv.getHetmdj());
				bsh.set("���ú��", bsv.getZuigmj());
				bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());
				
//				��ͬ����ָ��,ȡ�����������ĺ�ͬ����
				if(rsl.getRows()==1){
					
//					��һ����ͬ
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								Ŀ¼�۽���
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//								�����ν���
							
							String[] test=null;
							
							test=SelIds.split(",");
							
							for(int i=0;i<test.length;i++){
								
//								��ý�������������
								getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								ú�˰���۱���С��λ
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs,bsv.getUser_custom_mlj_jiesgs());		
								
//								��˰����ȡ����ʽ
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								���ۿ��С��λ
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								ִ�к�ͬ�۸�ʽ
								ExecuteHetmdjgs(bsh);
								
//								ִ�й�ʽ
								bsh.eval(bsv.getGongs_Mk());
								
//								�õ�������ָ��
								setJieszb(bsh,0);
								
//								����ú����
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						��Ȩƽ��
							
//							��ý�������������
							getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
							
//							ΪĿ¼�۸�ֵ
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							������ۿ�
							getZengkk(Hetb_id,bsh);
							
//							ú�˰���۱���С��λ
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							�û��Զ��幫ʽ
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							��˰����ȡ����ʽ
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							���ۿ��С��λ
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							ִ�к�ͬ�۸�ʽ
							ExecuteHetmdjgs(bsh);
							
//							ִ�й�ʽ
							bsh.eval(bsv.getGongs_Mk());
							
//							�õ�������ָ��
							setJieszb(bsh,0);
							
//							����ú����
							computData();
						}
						
					}else{		
						
//						һ����ͬ
//						��Ŀ¼��
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//							�����ν���
							
							String[] test=null;
							test=SelIds.split(",");
							
							for(int i=0;i<test.length;i++){
								
//								��ý�������������
								getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
								double Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
								
								bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
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
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_fmlj_jiesgs,bsv.getUser_custom_fmlj_jiesgs());
								
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
								setJieszb(bsh,0);
								
//								����ú����
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
							
//							��ý�������������
							getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
							
							double Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
							Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
							
							bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
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
							
//							������ۿ�
							getZengkk(Hetb_id,bsh);
							
//							�û��Զ��幫ʽ
							bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
							
//							ú�˰���۱���С��λ
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							��˰����ȡ����ʽ
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							���ۿ��С��λ
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							ִ�к�ͬ�۸�ʽ
							ExecuteHetmdjgs(bsh);
							
//							ִ�й�ʽ
							bsh.eval(bsv.getGongs_Mk());
							
//							�õ�������ָ��
							setJieszb(bsh,0);
							
//							����ú����
							computData();
						}
					}
					
					bsv.setHetjgpp_Flag(true);
					bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
				}else{
//					�ж����ͬ
//					Ŀ¼��
					
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//					Ŀ¼�۽���
					
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){		//					�����ν���
						
							String[] test=null;
							test=SelIds.split(",");
							
							for(int i=0;i<test.length;i++){
								
//								��ý�������������
								getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
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
								setJieszb(bsh,0);
								
//								����ú����
								computData_Dpc();
							}
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��

							
//							��ý�������������
							getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
							
//							ΪĿ¼�۸�ֵ
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							������ۿ�
							getZengkk(Hetb_id,bsh);
							
//							�û��Զ��幫ʽ
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							ú�˰���۱���С��λ
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							��˰����ȡ����ʽ
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							���ۿ��С��λ
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							ִ�к�ͬ�۸�ʽ
							ExecuteHetmdjgs(bsh);
							
//							ִ�й�ʽ
							bsh.eval(bsv.getGongs_Mk());
							
//							�õ�������ָ��
							setJieszb(bsh,0);
							
//							����ú����
							computData();
						}
					}else{
						
//						�����ͬ
//						��Ŀ¼��
						double	Dbljijzb=0;
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){	//�����ν���
							
								String[] test=null;
								
								test=SelIds.split(",");
						
								for(int i=0;i<test.length;i++){
									
//									��ý�������������
									getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
									
									do{
										
										Dbljijzb=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
										Dbljijzb=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
									
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
										
											bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
											bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
											bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
											bsv.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
											bsv.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
											bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
											bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
											bsv.setHetjjfs(rsl.getString("hejfs"));		//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
										
											bsh.set("������ʽ", bsv.getJiesxs());
											bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
											bsh.set("�۸�λ", bsv.getHetmdjdw());	
											bsh.set("��ͬ�۸�", bsv.getHetmdj());
											bsh.set("���ú��", bsv.getZuigmj());
											
											bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
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
											getZengkk(Hetb_id,bsh);
											
//											�û��Զ��幫ʽ
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
											
//											�õ�������ָ��
											setJieszb(bsh,0);
											
//											����ú����
											computData_Dpc();
											
											bsv.setHetjgpp_Flag(true);
										}	
									}while(rsl.next());
								}		
								
									
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
							
//									��ý�������������
								getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
								do{
									
									Dbljijzb=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
									Dbljijzb=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
									
									if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
										
										bsv.setHetmdj(rsl.getDouble("jij"));			//����ú����
										bsv.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
										bsv.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
										bsv.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
										bsv.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
										bsv.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
										bsv.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
										bsv.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
									
										bsh.set("������ʽ", bsv.getJiesxs());
										bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
										bsh.set("�۸�λ", bsv.getHetmdjdw());	
										bsh.set("��ͬ�۸�", bsv.getHetmdj());
										bsh.set("���ú��", bsv.getZuigmj());
										
										bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//��ͬ�۸��id
										
										bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
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
										
//											������ۿ�
										getZengkk(Hetb_id,bsh);
										
//											�û��Զ��幫ʽ
										bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
										
//										ú�˰���۱���С��λ
										bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
										
//										��˰����ȡ����ʽ
										bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
										
//										���ۿ��С��λ
										bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
										
//										ִ�к�ͬ�۸�ʽ
										ExecuteHetmdjgs(bsh);
										
//											ִ�й�ʽ
										bsh.eval(bsv.getGongs_Mk());
										
//											�õ�������ָ��
										setJieszb(bsh,0);
										
//											����ú����
										computData();
										
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
	
	private boolean getZengkk(long Hetb_id,Interpreter bsh){
//		���ۿ�
		JDBCcon con=new JDBCcon();
		try{
			ResultSetList rsl=null;
			
			String sql="select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,nvl(shangx,0) as shangx,nvl(xiax,0) as xiax,dwb.bianm as danw,nvl(jis,0) as jis,	\n"
				+ " jisdw.bianm as jisdw,nvl(kouj,0) as kouj,kjdw.bianm as koujdw,nvl(zengfj,0) as zengfj,zfjdw.bianm as zengfjdw,	\n"
				+ " xiaoscl,jizzkj,jizzb,czxm.mingc as canzxm,czxmdw.bianm as canzxmdw,canzsx,canzxx	\n"  
				+ " from hetb htb, hetzkkb htzkk,zhibb zbb,tiaojb tjb,danwb dwb,danwb jisdw,danwb kjdw,	\n"
				+ " danwb zfjdw,zhibb czxm,danwb czxmdw	\n"
				+ " where htb.id=htzkk.hetb_id and htzkk.zhibb_id=zbb.id and htzkk.tiaojb_id=tjb.id	\n" 
				+ " and htzkk.danwb_id=dwb.id(+) and htzkk.jisdwid=jisdw.id(+)	\n" 
				+ " and htzkk.koujdw=kjdw.id(+)		\n" 
				+ " and htzkk.zengfjdw=zfjdw.id(+)	\n" 
				+ " and htzkk.canzxm=czxm.id(+)		\n" 
				+ " and htzkk.canzxmdw=czxmdw.id(+)	\n"
				+ " and tjb.leib=1 and zbb.leib=1	\n"          
				+ " and htb.id="+Hetb_id;
			
			rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//��¼ָ�����ֵ
			String  Strtmp="";		//��¼�趨��ָ��
			while(rsl.next()){
				
				Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
//				ָ��Ľ���ָ��
				Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					//ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
					
					bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
					bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));			//ָ�굥λ
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
					bsh.set(rsl.getString("zhib")+"С������",	Fayjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
					
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
				
				Strtmpdw=Fayjsdcz.getZhibbdw(rsl.getString("zhib"),"");
				
				bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//����ֵ
				bsh.set(rsl.getString("zhib")+"������λ", 	Strtmpdw);				//ָ�굥λ
				bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"����");					//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
				bsh.set(rsl.getString("zhib")+"��������",		0);					//������
				bsh.set(rsl.getString("zhib")+"�۸�����",		0);					//�ۼ�
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
	
	
	private void ExecuteHetmdjgs(Interpreter bsh){
//		ִ�к�ͬ�۸�ʽ
//		�������ܣ�
		
//			��ִ�н��㹫ʽǰ�������ͬ�۸��д��ڼ۸�ʽ��Ҫ��ִ�м۸�ʽ������ֵ��ֵ������ͬ�۸񡱡�
//		�����߼���

//			ִ�к�ͬ�۸�Ĺ�ʽ��
//		�����βΣ�bsh �����Զ����������
		
		if(!bsv.getHetmdjgs().equals("")){
			
			try {
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
	
	private void CountYf(long Hetb_id){
//		�����˷�
//		˼·��
//			���1��ú���ͬ�����˼ۣ�bsv.getHetyj()>0�����˼۵�λ�����֣�Ԫ/�֣�Ԫ/��*���,
//					û�����ۿ����.��Ϊû���õ��˷Ѻ�ͬ�����Բ��������ۿ�
//			���2����Hetyj��Hetyjdw��ֵ,Ҫ��hetysjgb��ȡֵ�������ۿ�
		try{
			
			Interpreter bsh=new Interpreter();
			String sql="";
//			�˷Ѻ�˰���۱���С��λ
			if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//				���1
//				ȡhetb�е��˼�
				bsh.set("��ͬ�˼�", bsv.getHetyj());
				bsh.set("��ͬ�˼۵�λ", bsv.getHetyjdw());
				bsh.set("�˼����", bsv.getYunju_js());
				
				if(!bsv.getGongs_Yf().equals("")){
					
					if(bsv.getYunfjsl()>0){
						
//						�˷Ѻ�˰���۱���С��λ
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						getZengkk_yf(Hetb_id,bsh);
						
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
				sql="select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
							+ " yunja,yjdw.bianm as yunjdw	\n" 
							+ " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
							+ " danwb yjdw										\n"
							+ " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id	\n" 
							+ " and jg.tiaojb_id=tj.id(+)						\n"  
							+ " and jg.danwb_id=dw.id(+)						\n" 
							+ " and jg.yunjdw_id=yjdw.id(+)						\n"
							+ " and ht.id="+Hetb_id+" and jg.meikxxb_id="+bsv.getMeikxxb_Id()+"	";
				
				ResultSetList rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					if(rsl.getRows()==1){
//						��һ����ͬ�۸�
						bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
						bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
						bsh.set("�˼����", bsv.getYunju_js());
						
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
						double yunju=bsv.getYunju_cf();	//�˾�
						double Dbltmp=0;
						
						do{
							shangx=rsl.getDouble("shangx");
							xiax=rsl.getDouble("xiax");
							
							Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
							
							Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
							
							if(yunju>=xiax&&yunju<=(shangx==0?9999:shangx)){
								
								bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
								bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
								bsh.set("�˼����", bsv.getYunju_js());
								
								getZengkk_yf(Hetb_id,bsh);
								
//								�˷Ѻ�˰���۱���С��λ
								bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
								
								bsh.eval(bsv.getGongs_Yf());
								setJieszb(bsh,1);
							}
							
						}while(rsl.next());
						rsl.close();
						con.Close();
					}
				}else{
					
					bsv.setErroInfo("û�еõ��˷Ѻ�ͬ�۸�");
					return;
				}
				
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
				
				Dbltmp=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
//				ָ��Ľ���ָ��
				Dbltmp=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
				
					bsh.set(rsl.getString("zhib")+Fayjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
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
					bsh.set(rsl.getString("zhib")+"С������",	Fayjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
					
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
				
					
					Strtmpdw=Fayjsdcz.getZhibbdw(rsl.getString("zhib"),"");
					
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
	
	
//	�����˷ѣ������ӷѣ���˰�۳��������˷�
/*	private boolean getYunFei(String SelIds,long Jieslx,long Hetb_id){
	    JDBCcon con=new JDBCcon();
		try{
		    	
		    	String sql="";
		    	ResultSet rs=null;
		    	String sql_colum="";	//�����У������˷��ã�
		    	String sql_talbe="";	//���ӱ������˷��ã�
		    	long lngJieslx=0;
		    	long lngYunshtb_id=0;
		    	lngJieslx=Jieslx;
		    	
		    	if(lngJieslx==1||lngJieslx==3){
		    		
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
				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.id in ("+SelIds+"))))		\n"
				        	+ " ,																							\n"            
				        	+ "(select sum(zhi) as kuangqzf from 	\n"
				        	+ " (select distinct feiyb.*,yunfdjb.id  																	\n"
				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"  
				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"  
				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id														\n"  
				        	+ " and feiylbb.id="+Locale.kuangqyf_feiylbb_id+" and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"  
				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.id in ("+SelIds+"))))		\n";
	    		
		    			lngJieslx=3;
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
			        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.id in ("+SelIds+"))))		\n"
			        	+ " ,																							\n"            
			        	+ "(select sum(zhi) as tielzf from			\n"
			        	+ " (select distinct feiyb.*,yunfdjb.id  																\n"
			        	+ " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"  
			        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"  
			        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"  
			        	+ " and feiylbb.id="+lngJieslx+" and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"  
			        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.id in ("+SelIds+"))))		\n"
			        	+ sql_talbe;
			        
				    rs=con.getResultSet(sql);
				    
				    if (rs.next()){
//				        ��Ʊ���㣨��·����yunfdjb,danjcpb��ȡֵ��ǰ����Ҫ�Ƚ��л�Ʊ�˶ԣ�
//				    	yunf��·��Ʊ�����еķ���
//				    	yunfzf��·��Ʊ�����в��ɵ�˰�ķ���
//				    	��Ʊ���������˷�ʱ��
				    	bsv.setTielyf(rs.getDouble("tielyf"));
				    	bsv.setTielzf(rs.getDouble("tielzf"));
				    	
				    	if(lngJieslx==1||lngJieslx==3){
				    		
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
				    	if(Jieslx==1){
//				    		��Ʊ���㣬���1
				    		
				    		if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//			    				���ú���ͬ�����˷������
				    			
			    				CountYf(0);
			    			}else{
//			    				���ú���ͬ��û���˷ѣ���ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid
			    				lngYunshtb_id=getYunshtb_id(Hetb_id);
			    				
			    				if(lngYunshtb_id>0){
//			    					˵�������ҵ���Ӧ���˷Ѻ�ͬ��
			    					CountYf(lngYunshtb_id);
			    				}else if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//			    					�����۽���,�������Ʊ�����˷ѣ������ǵ����ۺ�ͬ�Ļ����������˷�
			    					
			    				}else{
//			    					����۽���,Ӧ�����˷�,����Ӻ�ͬ�в��ܵõ��˼���Ϣ��Ҫ��yunfwhb��ȡά��ֵ
			    					CountYffromwh();
//			    					bsv.setErroInfo("ú���ͬ<"+Fayjsdcz.getHetbh(bsv.getHetb_Id())+">û�ж�Ӧ���˷Ѻ�ͬ�������ã�");
			    					return false;
			    				}
			    			}
				    	}else if(Jieslx!=2){
//				    		�����˷ѽ���
				    		CountYf(bsv.getHetb_Id());
				    	}
				    }
				    
		    	rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return true;
	}
	*/
	private void CountYffromwh(){
		
		String sql="";
		JDBCcon con=new JDBCcon();
		sql="select * from jiesyfb jy\n" + 
			"       where jy.gongysb_id="+bsv.getGongysb_Id()+"\n" + 
			"             and jy.ruzrq is not null\n" + 
			"             and rownum=1 and diancxxb_id="+bsv.getDiancxxb_id()+" \n" + 
			"             order by ruzrq desc";
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			
			bsv.setTielyf(rsl.getDouble("guotyf"));
			bsv.setTielzf(rsl.getDouble("guotzf"));
			bsv.setKuangqyf(rsl.getDouble("kuangqyf"));
			bsv.setKuangqzf(rsl.getDouble("kuangqzf"));
		}else{
	//		��û�о�Ҫ��yunfwhb��ȡ��
			
			sql="select * from yunfwhb where diancxxb_id="+bsv.getDiancxxb_id()+" and meikxxb_id="
				+bsv.getMeikxxb_Id()+" order by id desc";
			rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				bsv.setTielyf((double)CustomMaths.Round_new(bsv.getYunfjsl()*rsl.getDouble("yunf"),2));
				bsv.setTielzf((double)CustomMaths.Round_new(bsv.getYunfjsl()*rsl.getDouble("zaf"),2));
				bsv.setKuangqyf((double)CustomMaths.Round_new(bsv.getYunfjsl()*rsl.getDouble("fazzf"),2));
			}
		}
		rsl.close();
		con.Close();
	}
	
	private void setJieszb(Interpreter bsh,int Type){
		
		try {
			
//			Type	0:ú�����
//			1:�˷ѽ���
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
//			���㵥��
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
	
	private void computMlj(Interpreter bsh,ResultSetList rsl,Fayjsdcz Jscz,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
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
				
				Dbljijzb=Fayjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
				Dbljijzb=Fayjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
				
				if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					if(Fayjsdcz.CheckMljRz(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", Fayjsdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))));
						
						bsh.set(rsl.getString("zhib")+"_����", Fayjsdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));
						
						bsh.set("��ֵ����_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}	
					if(Fayjsdcz.CheckMljHff(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("�ӷ��ݱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Fayjsdcz.CheckMljLiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("��ֱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Fayjsdcz.CheckMljHiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
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
			bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Fayjsdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id,Locale.zhengcxjj_jies, "0")));
					
			//	�Ӽ�
			bsh.set(Locale.jiaj_jies, Double.parseDouble(Fayjsdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id, Locale.jiaj_jies, "0")));
			
		}catch(EvalError e){
			
			e.printStackTrace();
		}	
	}
	
//	������ü�Ȩ
	private void computData(){
		//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
		//ú��
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();
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
		double _Yunju=bsv.getYunju_zdj();			//�˾��۵���
		
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
		
//		�۸������
//		2008-12-9zsj�ӣ�
//		�߼���	�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//					��
//						���һ������˷ѵ��۵�λ=��Ԫ/�֡�
		
//							ú�˰����=�������ú�˰����-��ͬ�۸��еĺ�˰�˷ѵ���
//							ͬʱҪ����Hansmj
		
//						�����������˷ѵ��۵�λ=���֡�
		
//							ú���������=��ǰ��������-��˰�˷ѵ��ۣ��֣�
//							ͬʱҪ����Jiessl
		
		if(bsv.getYikj_yunfyymk().equals("��")){
			if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)&&bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
	//			�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸����˷Ѻ�˰���۴���0�����˷Ѽ۸�λ�����ڡ���
				
				if(bsv.getHetyjdw().equals(Locale.yuanmd_danw)){
	//				����˷ѵ��۵�λ=��Ԫ/�֡�
	//					����С���Ĵ����������ݽ�������"ú�˰���۱���С��λ"
					_Hansmj=(double)CustomMaths.Round_new(_Hansmj-bsv.getHetyj(), bsv.getMeikhsdjblxsw());
					bsv.setHansmj(_Hansmj);
					
					_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);												//��˰�ϼ�
					_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);										//�ۿ�ϼ�
					_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);												//�ۿ�˰��
					_Jine=_Jiakhj;																						//���
					_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);										//����˰����
				}
	//			else if(bsv.getHetyjdw().equals(Locale.dun_danw)){
	////				����˷ѵ��۵�λ=���֡�
	////				����С���Ĵ����������ݽ�������"������������С��λ"
	//				_Jiessl=(double)CustomMaths.Round_new(_Jiessl-bsv.getHetyj(), Integer.parseInt(bsv.getJiesslblxs()));
	//				bsv.setJiessl(_Jiessl);
	//			}
			}else{
				
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);												//��˰�ϼ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);										//�ۿ�ϼ�
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);												//�ۿ�˰��
				_Jine=_Jiakhj;																						//���
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);	
			}
		}else if(bsv.getYikj_yunfyymk().equals("��")){
				//		һ�ڼۣ��˷�Դ��ú� ���壬��������ܺ�˰ú���ϼ�ȥ�˷ѵ��ܼ�=�����ܺ�˰ú��
			_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Hansmj*_Jiessl,2)
					-(double)CustomMaths.Round_new(bsv.getHetyj()*bsv.getYunfjsl(),2),2);	//ú��ļ�˰�ϼ�-�˷ѵļ�˰�ϼ�=ú��ļ�˰�ϼ�
			_Jiakhj=(double)CustomMaths.Round_new(_Jiashj/(1+_Meiksl),2);
			_Jiaksk=(double)CustomMaths.Round_new(_Jiashj-_Jiakhj,2);	
			_Jine=_Jiakhj;
			bsv.setHansmj((double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw()));
			_Hansmj=bsv.getHansmj();
			_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);
		}										//����˰����
		_Shulzjbz=_Hansmj;
		
		//�����˷���
//		_Yunzfhj=Math.round((_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf)*100)/100;									//���ӷѺϼ�
//		_Yunfsk=(double)Math.round(((double)Math.round(_Tielyf*_Yunfsl*100)/100+_Kuangqsk)*100)/100;		//�˷�˰��		
//		_Buhsyf=(double)Math.round(((double)Math.round((_Yunzfhj-_Yunfsk)*100)/100+_Kuangqjk)*100)/100;	//����˰�˷�
		
//		�ϼ�
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//����ӯ�����ۼ۽��
		_Qnetarzje=(double)CustomMaths.Round_new(_Qnetar*_Jiessl,2);
		_Stdzje=(double)CustomMaths.Round_new(_Std*_Jiessl,2);
		_Adzje=(double)CustomMaths.Round_new(_Ad*_Jiessl,2);
		_Vdafzje=(double)CustomMaths.Round_new(_Vdaf*_Jiessl,2);
		_Mtzje=(double)CustomMaths.Round_new(_Mt*_Jiessl,2);
		_Qgradzje=(double)CustomMaths.Round_new(_Qgrad*_Jiessl,2);
		_Qbadzje=(double)CustomMaths.Round_new(_Qbad*_Jiessl,2);
		_Hadzje=(double)CustomMaths.Round_new(_Had*_Jiessl,2);
		_Stadzje=(double)CustomMaths.Round_new(_Stad*_Jiessl,2);
		_Madzje=(double)CustomMaths.Round_new(_Mad*_Jiessl,2);
		_Aarzje=(double)CustomMaths.Round_new(_Aar*_Jiessl,2);
		_Aadzje=(double)CustomMaths.Round_new(_Aad*_Jiessl,2);
		_Vadzje=(double)CustomMaths.Round_new(_Vad*_Jiessl,2);
		_T2zje=(double)CustomMaths.Round_new(_T2*_Jiessl,2);
		_Yunjuzje=(double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2);			//�˾��۽��
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//����������ӯ��
		
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
		bsv.setShul_zje(_Shulzje);
		bsv.setYunju_zje(_Yunjuzje);
	}
	
//	������õ�����
	private void computData_Dpc(){
		//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
		//ú��
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();
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
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		double _Yunjuzje=0;
		
//		�۸������
//		2008-12-9zsj�ӣ�
//		�߼���	�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//					��
//						���һ������˷ѵ��۵�λ=��Ԫ/�֡�
		
//							ú�˰����=�������ú�˰����-��ͬ�۸��еĺ�˰�˷ѵ���
//							ͬʱҪ����Hansmj
		
//						�����������˷ѵ��۵�λ=���֡�
		
//							ú���������=��ǰ��������-��˰�˷ѵ��ۣ��֣�
//							ͬʱҪ����Jiessl
		
		if(bsv.getYikj_yunfyymk().equals("��")){
			if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)&&bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
	//			�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸����˷Ѻ�˰���۴���0�����˷Ѽ۸�λ�����ڡ���
				
				if(bsv.getHetyjdw().equals(Locale.yuanmd_danw)){
	//				����˷ѵ��۵�λ=��Ԫ/�֡�
	//					����С���Ĵ����������ݽ�������"ú�˰���۱���С��λ"
					_Hansmj=(double)CustomMaths.Round_new(_Hansmj-bsv.getHetyj(), bsv.getMeikhsdjblxsw());
					bsv.setHansmj(_Hansmj);
					
					_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);												//��˰�ϼ�
					_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);										//�ۿ�ϼ�
					_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);												//�ۿ�˰��
					_Jine=_Jiakhj;																						//���
					_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);										//����˰����
				}
	//			else if(bsv.getHetyjdw().equals(Locale.dun_danw)){
	////				����˷ѵ��۵�λ=���֡�
	////				����С���Ĵ����������ݽ�������"������������С��λ"
	//				_Jiessl=(double)CustomMaths.Round_new(_Jiessl-bsv.getHetyj(), Integer.parseInt(bsv.getJiesslblxs()));
	//				bsv.setJiessl(_Jiessl);
	//			}
			}else{
				
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);												//��˰�ϼ�
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);										//�ۿ�ϼ�
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);												//�ۿ�˰��
				_Jine=_Jiakhj;																						//���
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);	
			}
			
		}else if(bsv.getYikj_yunfyymk().equals("��")){
				//		һ�ڼۣ��˷�Դ��ú� ���壬��������ܺ�˰ú���ϼ�ȥ�˷ѵ��ܼ�=�����ܺ�˰ú��
			_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Hansmj*_Jiessl,2)
					-(double)CustomMaths.Round_new(bsv.getHetyj()*bsv.getYunfjsl(),2),2);	//ú��ļ�˰�ϼ�-�˷ѵļ�˰�ϼ�=ú��ļ�˰�ϼ�
			_Jiakhj=(double)CustomMaths.Round_new(_Jiashj/(1+_Meiksl),2);
			_Jiaksk=(double)CustomMaths.Round_new(_Jiashj-_Jiakhj,2);	
			_Jine=_Jiakhj;
			bsv.setHansmj((double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw()));
			_Hansmj=bsv.getHansmj();
			_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);
		}										//����˰����
		_Shulzjbz=_Hansmj;
		
//		//�����˷���
//		_Yunzfhj=Math.round((_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf)*100)/100;									//���ӷѺϼ�
//		_Yunfsk=(double)Math.round(((double)Math.round(_Tielyf*_Yunfsl*100)/100+_Kuangqsk)*100)/100;		//�˷�˰��		
//		_Buhsyf=(double)Math.round(((double)Math.round((_Yunzfhj-_Yunfsk)*100)/100+_Kuangqjk)*100)/100;	//����˰�˷�
		
//		 �ϼ�
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//����ӯ�����ۼ۽��
		_Qnetarzje=(double)CustomMaths.Round_new(_Qnetar*_Jiessl,2);
		_Stdzje=(double)CustomMaths.Round_new(_Std*_Jiessl,2);
		_Adzje=(double)CustomMaths.Round_new(_Ad*_Jiessl,2);
		_Vdafzje=(double)CustomMaths.Round_new(_Vdaf*_Jiessl,2);
		_Mtzje=(double)CustomMaths.Round_new(_Mt*_Jiessl,2);
		_Qgradzje=(double)CustomMaths.Round_new(_Qgrad*_Jiessl,2);
		_Qbadzje=(double)CustomMaths.Round_new(_Qbad*_Jiessl,2);
		_Hadzje=(double)CustomMaths.Round_new(_Had*_Jiessl,2);
		_Stadzje=(double)CustomMaths.Round_new(_Stad*_Jiessl,2);
		_Madzje=(double)CustomMaths.Round_new(_Mad*_Jiessl,2);
		_Aarzje=(double)CustomMaths.Round_new(_Aar*_Jiessl,2);
		_Aadzje=(double)CustomMaths.Round_new(_Aad*_Jiessl,2);
		_Vadzje=(double)CustomMaths.Round_new(_Vad*_Jiessl,2);
		_T2zje=(double)CustomMaths.Round_new(_T2*_Jiessl,2);
		_Yunjuzje=(double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2);			//�˾��۽��
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//����������ӯ��
		
		//���㵥��ʾʱָ���۽����
		
		bsv.setShulzjbz(_Shulzjbz);
		bsv.setShulzjbz(_Shulzje);
		bsv.setJiashj(_Jiashj);
		bsv.setJiakhj(_Jiakhj);
		bsv.setJiaksk(_Jiaksk);
		bsv.setJine(_Jine);
		bsv.setHej(_Hej);
		
		bsv.setQnetar_zje(bsv.getQnetar_zje()+_Qnetarzje);
		bsv.setStd_zje(bsv.getStd_zje()+_Stdzje);
		bsv.setAd_zje(bsv.getAd_zje()+_Adzje);
		bsv.setVdaf_zje(bsv.getVdaf_zje()+_Vdafzje);
		bsv.setMt_zje(bsv.getMt_zje()+_Mtzje);
		bsv.setQgrad_zje(bsv.getQgrad_zje()+_Qgradzje);
		bsv.setQbad_zje(bsv.getQbad_zje()+_Qbadzje);
		bsv.setHad_zje(bsv.getHad_zje()+_Hadzje);
		bsv.setStad_zje(bsv.getStad_zje()+_Stadzje);
		bsv.setMad_zje(bsv.getMad_zje()+_Madzje);
		bsv.setAar_zje(bsv.getAar_zje()+_Aarzje);
		bsv.setAad_zje(bsv.getAad_zje()+_Aadzje);
		bsv.setVad_zje(bsv.getVad_zje()+_Vadzje);
		bsv.setT2_zje(bsv.getT2_zje()+_T2);
		bsv.setYunju_zje(bsv.getYunju_zje()+_Yunjuzje);
		bsv.setShul_zje(bsv.getShul_zje()+_Shulzje);
		
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
		bsv.setYunju_zje(_Yunjuzje);
		
		
		_Qnetar=(double)CustomMaths.Round_new(bsv.getQnetar_zje()/bsv.getJiessl(),2);
		_Std=(double)CustomMaths.Round_new(bsv.getStd_zje()/bsv.getJiessl(),2);
		_Ad=(double)CustomMaths.Round_new(bsv.getAd_zje()/bsv.getJiessl(),2);
		_Vdaf=(double)CustomMaths.Round_new(bsv.getVdaf_zje()/bsv.getJiessl(),2);
		_Mt=(double)CustomMaths.Round_new(bsv.getMt_zje()/bsv.getJiessl(),2);
		_Qgrad=(double)CustomMaths.Round_new(bsv.getQgrad_zje()/bsv.getJiessl(),2);
		_Qbad=(double)CustomMaths.Round_new(bsv.getQbad_zje()/bsv.getJiessl(),2);
		_Had=(double)CustomMaths.Round_new(bsv.getHad_zje()/bsv.getJiessl(),2);
		_Stad=(double)CustomMaths.Round_new(bsv.getStad_zje()/bsv.getJiessl(),2);
		_Mad=(double)CustomMaths.Round_new(bsv.getMad_zje()/bsv.getJiessl(),2);
		_Aar=(double)CustomMaths.Round_new(bsv.getAar_zje()/bsv.getJiessl(),2);
		_Aad=(double)CustomMaths.Round_new(bsv.getAad_zje()/bsv.getJiessl(),2);
		_Vad=(double)CustomMaths.Round_new(bsv.getVad_zje()/bsv.getJiessl(),2);
		_T2=(double)CustomMaths.Round_new(bsv.getT2_zje()/bsv.getJiessl(),2);
		_Shulzjbz=(double)CustomMaths.Round_new(bsv.getShul_zje()/bsv.getJiessl(),2);
		_Buhsmj=(double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),2);
		
		bsv.setQnetar_zdj(_Qnetar);
		bsv.setStd_zdj(_Std);
		bsv.setAd_zdj(_Ad);
		bsv.setVdaf_zdj(_Vdaf);
		bsv.setMt_zdj(_Mt);
		bsv.setQgrad_zdj(_Qgrad);
		bsv.setQbad_zdj(_Qbad);
		bsv.setHad_zdj(_Had);
		bsv.setStad_zdj(_Stad);
		bsv.setMad_zdj(_Mad);
		bsv.setAar_zdj(_Aar);
		bsv.setAad_zdj(_Aad);
		bsv.setVad_zdj(_Vad);
		bsv.setT2_zdj(_T2);
		bsv.setYunju_zdj(_Yunju);
		bsv.setShulzjbz(_Shulzjbz);
		bsv.setBuhsmj(_Buhsmj);
		
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
//		bsv.setYunzfhj(_Yunzfhj);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
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
		
		//�����˷���
		_Yunzfhj=(double)CustomMaths.Round_new(_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf,2);								//���ӷѺϼ�
		_Yunfsk=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new(_Tielyf*_Yunfsl,2)+_Kuangqsk),2);		//�˷�˰��		
		_Buhsyf=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2)+_Kuangqjk),2);	//����˰�˷�
		
		if(_Yunzfhj==0){
			
			_Yunzfhj=(double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2);						//�˷�˰��
			_Yunfsk=(double)CustomMaths.Round_new(_Yunzfhj*_Yunfsl,2);											//�˷�˰��
			_Buhsyf=(double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2);											//����˰�˷�
			_Tielyf=_Yunzfhj;
			bsv.setTielyf(_Tielyf);
		}
		
		//�ϼ�
		_Hej=(double)Math.round((_Yunzfhj+bsv.getJiashj())*100)/100;
		
		if(bsv.getYikj_yunfyymk().equals("��")){
//			һ�ڼۣ��˷�Դ��ú� ���壬��������ܺ�˰ú���ϼ�ȥ�˷ѵ��ܼ�=�����ܺ�˰ú��
			bsv.setJiashj((double)CustomMaths.Round_new(bsv.getJiashj()-_Yunzfhj,2));	//ú��ļ�˰�ϼ�-�˷ѵļ�˰�ϼ�=ú��ļ�˰�ϼ�
			bsv.setJiakhj((double)CustomMaths.Round_new((bsv.getJiashj())/(1+bsv.getMeiksl()),2));
			bsv.setJiaksk((double)CustomMaths.Round_new((bsv.getJiashj()-bsv.getJiakhj()),2));	
			bsv.setJine(bsv.getJiakhj());
			bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
		}
		
		bsv.setYunfsk(_Yunfsk);
		bsv.setBuhsyf(_Buhsyf);
		bsv.setYunzfhj(_Yunzfhj);
		bsv.setYunfsk(_Yunfsk);
		bsv.setBuhsyf(_Buhsyf);
		bsv.setHej(_Hej);
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
}
