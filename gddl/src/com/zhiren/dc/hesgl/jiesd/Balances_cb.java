 /*
 * 创建日期 2008-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.dc.hesgl.jiesd;
/**
 * @author Admini_ator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

/**
 * 2009-7-30
 * 张少君
 * 将成本模块参照结算模块进行了调整,但因功能不同两个模块的逻辑有一些差异
 * 差异
	1、结算是对多个发货的lie_id进行操作的，可能是多条记录；
		成本只是针对一条记录的发货的id进行操作
	2、结算时考虑了上批的结算量，主要是针对数量折价用；
		成本不考虑上批的结算量，只对本发货记录进行折价，所以没有此逻辑。
	3、结算时在多个发货指标加权平均的情况下，有一些特殊指标需要单批次处理；
		成本只对一条发货进行计算故没有此逻辑。
	4、结算时所有的计算结果都存在danpcjsmxb中，最后进行数据的整合；
		成本中的计算结果是存在变量中的，不操作danpcjsmxb。
	5、结算时要求结算的发货数量和质量必须经过审核，且达到终审状态；
		在成本估算过程中，数量和质量	有可能缺失。
	6、结算时如果没有质量信息是不能结算出正确结果的；
		在成本估算过程中，如果没有质量，系统会用合同价格
 */

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.zhiren.common.*;

import bsh.*;

public class Balances_cb {
	
	Balances_variable bsv=null;
	
	public Balances_variable getBalanceData(Balances_variable bsv,String SelIds,long Diancxxb_id,
			long Hetb_id,long Jieslx,long Gongysb_id,String Jieszbsftz,String Yansbh,double Jieskdl,double Shangcjsl) throws Exception{
		
		this.bsv=bsv;
		this.bsv.setIsError(true);
		this.bsv.setErroInfo("");
		this.bsv.setJieslx(Jieslx);
		this.bsv.setDiancxxb_id(Diancxxb_id);
		
		if(Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.meikjs_feiylbb_id){
//			两票结算、煤款结算
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id);
			if (bsv.getErroInfo().equals("")){
//				得到供应商、运输方式等基本信息
				
				if(Gongysb_id==0){
//					如果在结算选择页面供应商没做选择
					Gongysb_id=bsv.getGongysb_Id();
				}
				
			}else{
				bsv.getErroInfo();
			}
			
//			得到公式。
			if (getGongsInfo(Diancxxb_id,"ALL")) {
				
			}else{
				//return ErroInfo;
			}
			
//			算煤价,合同中的相关值
			if  (getMeiPrice(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,Jieszbsftz,SelIds,
						Gongysb_id,Jieskdl,Shangcjsl)){
				
			}else{
				
				bsv.getErroInfo();
			}
			
//			算运费
			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
				
			}else{
				
				bsv.getErroInfo();
			}
		}else{	//运费结算
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id);
			
			if(Hetb_id>0){
//				如果单结算运费，有两种情况：
//				1、通过核对货票得到运费
//				2、通过运输合同计算出运费
//				当Hetb_id>0时说明是第二种情况
				getYunshtInfo(Hetb_id);
			}
			
			if (bsv.getErroInfo().equals("")){
//				得到供应商、运输方式等基本信息
				if(Gongysb_id==0){
//					如果在结算选择页面供应商没做选择
					Gongysb_id=bsv.getGongysb_Id();
				}
			}else{
				bsv.getErroInfo();
			}
			
			getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
			
//			得到运费公式
			if (getGongsInfo(Diancxxb_id,"YF")) {
				
			}else{
				//return ErroInfo;
				return bsv;
			}
			
//			算运费
			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
				
			}else{
				bsv.getErroInfo();
			}
		}
		
		computYunfAndHej();

		bsv.setIsError(false);
		
		return bsv;
	}
	
//	结算暂估_大唐国际
	public Balances_variable getBalanceData(Balances_variable bsv,String SelIds,long Diancxxb_id,long Hetb_id,long Jieslx,long Gongysb_id,double Jiesrl) throws Exception{
		
		this.bsv=bsv;
		this.bsv.setIsError(true);
		this.bsv.setErroInfo("");
		this.bsv.setJieslx(Jieslx);
		this.bsv.setDiancxxb_id(Diancxxb_id);
		
		if(Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.meikjs_feiylbb_id){
//			两票结算、煤款结算
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id);
			
			if (bsv.getErroInfo().equals("")){
//				得到供应商、运输方式等基本信息
				if(Gongysb_id==0){
//					如果在结算选择页面供应商没做选择
					Gongysb_id=bsv.getGongysb_Id();
				}
			}else{
				bsv.getErroInfo();
			}
			
//			得到公式。
			if (getGongsInfo(Diancxxb_id,"ALL")) {
				
			}else{
				//return ErroInfo;
			}
			
			bsv.setSelIds(SelIds);
			bsv.setDiancxxb_id(Diancxxb_id);

//			算煤价,合同中的相关值
			if  (getMeiPrice_PerFah(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,SelIds,Gongysb_id,Jiesrl)){
				
			}else{
				
				bsv.getErroInfo();
			}
			
		}else{	//运费结算
			
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id);
			
			if(Hetb_id>0){
//				如果单结算运费，有两种情况：
//				1、通过核对货票得到运费
//				2、通过运输合同计算出运费
//				当Hetb_id>0时说明是第二种情况
				getYunshtInfo(Hetb_id);
			}
			
			if (bsv.getErroInfo().equals("")){
//				得到供应商、运输方式等基本信息
				if(Gongysb_id==0){
//					如果在结算选择页面供应商没做选择
					Gongysb_id=bsv.getGongysb_Id();
				}
			}else{
				bsv.getErroInfo();
				return bsv;
			}
			
			getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
			
//			得到运费公式
			if (getGongsInfo(Diancxxb_id,"YF")) {
				
			}else{
				//return ErroInfo;
				return bsv;
			}
			
//			算运费
			if  (getYunFei(SelIds,Jieslx,bsv.getHetb_Id())){
				
			}else{
				bsv.getErroInfo();
			}
		}
		
		computYunfAndHej();

		bsv.setIsError(false);
		
		return bsv;
	}
	
	//得到系统信息，运费税率，煤款税率，公式。
	private boolean getGongsInfo(long _Diancxxb_id,String _Type) throws Exception{
//		JDBCcon con =new JDBCcon();
//	    try {
//            
//            //煤款结算公式
//	    	con.setAutoCommit(false);
//	    	ResultSet rs= con.getResultSet("select id from gongsb where mingc='结算煤价' and leix='结算' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
//            if (rs.next()) {
//            	
//            	DataBassUtil clob=new DataBassUtil();
//            	
//            	bsv.setGongs(clob.getClob("gongsb", "gongs", rs.getLong(1)));
//            	
//            }else{
//            	bsv.setErroInfo("没有得到'煤价公式'的系统设置值");
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
			
			str_Gongs_Mk=Jiesdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			str_Gongs_Yf=Jiesdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
			
			if(str_Gongs_Mk.equals("")){
				
				bsv.setErroInfo("没有得到煤价公式的系统设置值");
	        	return false;
			}else{
				
				bsv.setGongs_Mk(str_Gongs_Mk);
			}
			
			if(str_Gongs_Yf.equals("")){
				
//				bsv.setErroInfo("没有得到运费公式的系统设置值");
//	        	return false;
			}else{
				
				bsv.setGongs_Yf(str_Gongs_Yf);
			}
		}else if(_Type.equals("MK")){
			
			str_Gongs_Mk=Jiesdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			
			if(str_Gongs_Mk.equals("")){
				
				bsv.setErroInfo("没有得到煤价公式的系统设置值");
	        	return false;
			}else{
				
				bsv.setGongs_Mk(str_Gongs_Mk);
			}
		}else if(_Type.equals("YF")){
			
			str_Gongs_Yf=Jiesdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
			if(str_Gongs_Yf.equals("")){
				
				bsv.setErroInfo("没有得到运费公式的系统设置值");
	        	return false;
			}else{
				
				bsv.setGongs_Yf(str_Gongs_Yf);
			}
		}
		
		return true;
	}
    
	
//	得到要结算的数量，质量等基本信息
	/**
	 * @param selIds
	 * @param _Diancxxb_id
	 * @return
	 * @throws Exception
	 */
    
	private Balances_variable getBaseInfo(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id) throws Exception{

        JDBCcon con =new JDBCcon();
	    try {
            //发货日期、到货日期、车数、标重、盈亏、运损、发热量、硫 from fahb
	        //过衡量按列取证，结算量火车煤是按列取整，汽车煤是求和取整
	    	
	    	String sql="";
	    	
	    	sql=" select hetb_id,yunj,meikxxb_id,gongysb_id,pinzb_id,yunsfsb_id,minfahrq,maxfahrq,mindaohrq,maxdaohrq,gongysqc,meikdwqc,faz,faz_id,daoz_id,yuanshdw," +
	    		" pinz,yunsfs,nvl(getMeiksx(meikxxb_id,diancxxb_id,'运距'),0) as yunju,jihkjb_id		\n "+
	    		" from 														\n" +
	    		" (select max(hetb_id) as hetb_id,max(m.yunj) as yunj,max(f.meikxxb_id) as meikxxb_id,max(f.gongysb_id) as gongysb_id, \n "+
	    		" max(pinzb_id) as pinzb_id,max(yunsfsb_id) as yunsfsb_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq,to_char(max(fahrq),'yyyy-mm-dd') as maxfahrq, \n " +
	    		" max(pz.mingc) as pinz,max(ysfs.mingc) as yunsfs," +
				" to_char(min(daohrq),'yyyy-mm-dd') as mindaohrq,to_char(max(daohrq),'yyyy-mm-dd') as maxdaohrq,max(g.quanc) as gongysqc,max(m.quanc) as meikdwqc, \n " +
				" max(cz.mingc) as faz,max(cz.id) as faz_id,max(dz.id) as daoz_id,max(decode(vwydw.mingc,null,(select mingc from diancxxb where id = f.diancxxb_id),vwydw.mingc)) as yuanshdw, \n " +
				" max(f.diancxxb_id) as diancxxb_id,max(f.jihkjb_id) as jihkjb_id " +
				" from fahb f,zhilb z,kuangfzlb kz,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz" +
				" where f.zhilb_id=z.id(+) and kz.id(+)=f.kuangfzlb_id and f.faz_id=cz.id and f.pinzb_id=pz.id " +
				" and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id" +
				" and f.id in("+SelIds+"))";
           
            ResultSet rs = con.getResultSet(sql);
            
            if (rs.next()) {
            	
            	bsv.setFahksrq(rs.getDate("minfahrq"));
            	bsv.setFahjzrq(rs.getDate("maxfahrq"));
            	if (bsv.getFahksrq().equals(bsv.getFahjzrq())){
            		
            		bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()));
            	}else{
            								
            		bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()) +"至"+ Jiesdcz.FormatDate(bsv.getFahjzrq()));
            	}
            	
            	bsv.setYansksrq(rs.getDate("mindaohrq"));
            	bsv.setYansjsrq(rs.getDate("maxdaohrq"));

            	if (bsv.getYansksrq().equals(bsv.getYansjsrq())){
            		
            		bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq()));
            	}else{
            		
            		bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq())+"至"+Jiesdcz.FormatDate(bsv.getYansjsrq()));
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
	          	bsv.setJiesrq(Jiesdcz.FormatDate(new Date()));	//结算日期
	          	bsv.setJiesbh(Jiesdcz.getJiesbh(String.valueOf(Diancxxb_id),""));
	          	bsv.setDaibcc(MainGlobal.getShouwch(SelIds));
            	bsv.setYunju_cf(rs.getDouble("yunju"));		//厂方
            	bsv.setJihkjb_id(rs.getLong("jihkjb_id"));
            	bsv.setHetbh(MainGlobal.getTableCol("hetb", "hetbh", "id", String.valueOf(bsv.getHetb_Id())));
	          	
            }else{
            	bsv.setErroInfo("要结算的车皮信息不存在可能已被其他用户删除!");
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
	
	private Balances_variable getJiesszl(String Jieszbsftz,String SelIds,long Diancxxb_id,long Gongysb_id,
			long Hetb_id,double Jieskdl,double Shangcjsl){
		
//		结算数、质量
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
			
			
			String jies_Jqsl="jingz";								//结算加权数量
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
			String jies_shifykfzljs="否";
			String jies_Jieslqzfs="sum(round_new())";				//结算数量取整方式
			String jies_Jsslblxs="0";								//结算数量保留小数位
			String jies_Kdkskzqzfs="round_new(sum())";				//扣吨、扣水、扣杂取整方式
			String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//结算数量
			String jies_yunfjssl="jingz";							//运费结算数量
			boolean blnDandszyfjssl=false;	//是否单独设置了运费结算数量，如果是则yunfjssl=设置数量，如果不是且两票结算yunfjssl=gongfsl
			String jiscdkd="否";										//是否计算超吨、扣吨
			String ChaodOrKuid="";									//计算超吨还是亏吨
			
			String jies_Guohlqzfs=Locale.anlsswrhxj_jiesghlqzfs_xitxx;	//系统信息中过衡量取整方式，默认为：按列四舍五入后相加
			String jies_Guohlblxsw="0";									//系统信息中过衡量保留小数位，默认为：2
			long Jieslx=Locale.liangpjs_feiylbb_id;						//两票结算
			
//			jies_Jqsl=MainGlobal.getXitxx_item("结算", Locale.jiaqsl_xitxx, 
//	    			String.valueOf(Diancxxb_id),jies_Jqsl);
			
//			从系统信息表中取结算设置的信息
			String XitxxArrar[][]=null;	
			XitxxArrar=MainGlobal.getXitxx_items("结算",	"select mingc from xitxxb where leib='结算'"
					,String.valueOf(Diancxxb_id));
			
//			分析取得的值，然后对变量进行赋值
			if(XitxxArrar!=null){
				
				for(int i=0;i<XitxxArrar.length;i++){
					
					if(XitxxArrar[i][0]!=null){
						
						if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//							加权数量
							jies_Jqsl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlqzfs_xitxx)){
//							过衡量取整方式
							jies_Guohlqzfs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlblxsw_xitxx)){
//							结算过衡量保留小数位
							jies_Guohlblxsw=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.meiksl_xitxx)){
//							煤款税率
							bsv.setMeiksl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}else if(XitxxArrar[i][0].trim().equals(Locale.yunfsl_xitxx)){
//							运费税率
							bsv.setYunfsl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiscdkd_xitxx)){
//							计算超吨扣吨
							jiscdkd=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.Meikzkkblxsw_xitxx)){
//							煤款增扣款保留小数位
							bsv.setMeikzkkblxsw(Integer.parseInt(XitxxArrar[i][1].trim()));
						}
					}
				}
			}
			
			if(jiscdkd.equals("是")){
//				已在系统信息表中设定了超吨或亏吨的计算
				if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//					到厂价计算“超吨”
					ChaodOrKuid="CD";
				}else if(bsv.getJiesfs().equals(Locale.chukjg_ht_jsfs)){
//					出矿价计算“亏吨”
					ChaodOrKuid="KD";
				}
			}
//			记录超吨Or亏吨
			bsv.setChaodOrKuid(ChaodOrKuid);
			
			if(jies_Guohlqzfs.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
//				过衡量取整方式:按列四舍五入后相加
				jies_Guohlqzfs="sum(round_new())";
			}else if(jies_Guohlqzfs.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
//				过衡量取整方式:相加后统一四舍五入
				jies_Guohlqzfs="round_new(sum())";
			}else if(jies_Guohlqzfs.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
//				过衡量取整方式:不进行取整操作
				jies_Guohlqzfs="sum()";
			}else{
//				过衡量取整方式:如果都没得到默认为“按列四舍五入后相加”
				jies_Guohlqzfs="sum(round_new())";
			}
			
			if(Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){

	        	String JiesszArray[][]=null;
			
	        	JiesszArray=Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
		
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
							
//							jies_shifykfzljs=JiesszArray[i][1];
							bsv.setShifykfzljs(JiesszArray[i][1]);
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
							
//							煤款含税单价保留小数位
							bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
							
//							运费含税单价保留小数位
							bsv.setYunfhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.kuidjfyf_jies)){
							
//							亏吨拒付运费
							bsv.setKuidjfyf(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.Mj_to_kcal_xsclfs_jies)){
							
//							兆焦转大卡
							bsv.setMj_to_kcal_xsclfs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.meikhsdjqzfs_jies)){
							
//							含税单价取整方式
							bsv.setMeikhsdj_qzfs(JiesszArray[i][1]);
						}
					}
				}
	        }
			
			try {
				
				bsv.setMeikxxb_Id(Long.parseLong(MainGlobal.getTableCol("fahb", "Meikxxb_id", "id in ("+SelIds+")")));
				bsv.setFaz_Id(Long.parseLong(MainGlobal.getTableCol("fahb", "Faz_id", "id in ("+SelIds+")")));
			} catch (NumberFormatException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			
//			增加两个条件项，如果结算类型是煤款，没事。如果结算类型是国铁、或是两票或地铁结算 加danjcpb,yunfdjb其中费用类别国铁，yunfjsb_id=0 or null
			String contion_table="";//
			String contion_where="";
			String yunsdw="";
			long Yunf_Jieslx=Jieslx;	//为了处理两票结算为了得到运费单据表中的费用类型是“国铁”的数据，在此要进行类型转换
			if(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib){
//				运费结算和两票结算
				
				if(Jieslx==Locale.liangpjs_feiylbb_id){
					
					Yunf_Jieslx=Locale.guotyf_feiylbb_id;
				}
				
				contion_table=",(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl \n"+	
	                "       		where c.id=yfzl.chepb_id \n" +														
	                "       			and yfzl.feiylbb_id="+Yunf_Jieslx+") yfzl,\n" +
	                "		(select distinct cp.id,dj.yunfjsb_id\n" +
	                "                  from chepb cp,yunfdjb yd,danjcpb dj,fahb f,zhilb z,yansbhb ys,\n" +
	                "						gongysb g,meikxxb m\n" + 
	                "                  where yd.id=dj.yunfdjb_id\n" + 
	                "                        and dj.chepb_id=cp.id\n" + 
	                "                        and f.id=cp.fahb_id\n" + 
	                "						 and f.gongysb_id=g.id\n" +
	                "						 and f.meikxxb_id=m.id\n" +
	                "                        and yd.feiylbb_id="+Yunf_Jieslx+"\n" +
	                "						 and f.id in ("+SelIds+") \n" + 
	                "     ) djcp";
				
				contion_where=
					"       and cp.id=djcp.id(+)\n" + 
					"       and (djcp.yunfjsb_id is null or djcp.yunfjsb_id=0) \n" + 
					"		and cp.id=yfzl.chepb_id(+) ";
			}
			
			StringBuffer sql=new StringBuffer("");
			
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Aar_cf,0) as Aar_cf,nvl(Aar_kf,0) as Aar_kf,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Ad_cf,0) as Ad_cf,				\n");
				sql.append(" 		nvl(Ad_kf,0) as Ad_kf,nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Stad_cf,0) as Stad_cf,nvl(Stad_kf,0) as Stad_kf,nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,	\n");
				sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,						\n");
				sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
				sql.append("	 --厂方验收   																															\n");
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
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf,				 		\n");
				sql.append("	 --矿方验收   \n");
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
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_kf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_kf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_kf					 	\n");
				sql.append("	 	from (select distinct f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,									\n");
				sql.append("				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,									\n");
				sql.append("       			sum(cp.maoz) as maoz,sum(cp.piz) as piz,sum(cp.maoz-cp.piz-cp.zongkd) as jingz,												\n");
				
				if(contion_table.equals("")){
//					如果不是结算煤款或两票结算则取原票重
					sql.append("			sum(cp.biaoz) as biaoz,	\n");
				}else{
//					如果是则取调整后的票重
					sql.append("			sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) as biaoz,	\n");
				}
				
				sql.append("       			sum(cp.yingd) as yingd,sum(cp.yingk) as yingk,sum(cp.yuns) as yuns,sum(cp.koud) as koud,sum(cp.kous) as kous,				\n");
				sql.append("       			sum(cp.kouz) as kouz,sum(cp.koum) as koum,sum(cp.zongkd) as zongkd,sum(cp.sanfsl) as sanfsl,count(cp.id) as ches,			\n");
				sql.append("       			sum(nvl(getChaodkd(cp.id,'biaoz','"+jies_Jssl+"','"+Locale.sheq_ht_xscz+"','"+ChaodOrKuid+"'),0)) as chaokdl, 						\n");
				sql.append("				f.tiaozbz,f.yansbhb_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc																\n");
				sql.append(" 			from fahb f,chepb cp"+contion_table+" where f.id=cp.fahb_id "+contion_where+" and f.id in("+SelIds+")"+yunsdw+" 													\n"); 
				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
				sql.append("       				f.tiaozbz,f.yansbhb_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc															\n");
				sql.append("			) f,zhilb z,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz,  									\n");
				sql.append("	 	(select distinct f.id as fahb_id,k.* from fahb f,chepb cp,kuangfzlzb k where f.id=cp.fahb_id and cp.kuangfzlzb_id=k.id "+yunsdw+" and f.id in ("+SelIds+")) kz			\n");
				sql.append("			where f.zhilb_id=z.id(+) and f.id=kz.fahb_id(+) and f.faz_id=cz.id and f.pinzb_id=pz.id   								\n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.id in("+SelIds+")) 	\n");
			
			rsl=con.getResultSetList(sql.toString());
			
			if(rsl.next()){

				
//				数量
				bsv.setKoud(rsl.getDouble("koud"));					//扣吨
            	bsv.setKous(rsl.getDouble("kous"));					//扣水
            	bsv.setKouz(rsl.getDouble("kouz"));					//扣杂
            	bsv.setChes(rsl.getLong("ches"));					    //车数
            	
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//标重
            	bsv.setYingksl(rsl.getDouble("yingk"));				//盈亏  
            	bsv.setYingd(rsl.getDouble("yingd"));				//盈吨
            	bsv.setKuid(rsl.getDouble("kuid"));					
            	
            	bsv.setJiessl(rsl.getDouble("jiessl")-Jieskdl);			//结算重量
            	
            	bsv.setYanssl(rsl.getDouble("jingz"));				//厂方验收数量
            	bsv.setJingz(rsl.getDouble("jingz"));				//净重
            	bsv.setKoud_js(Jieskdl);											//结算扣吨
            	
            	bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl()-bsv.getJingz()),2));	//结算数量差异(结算量和过衡量的差值)
//            	bsv.setJiesslcy(rsl.getDouble("jieslcy"));			//结算数量差异
            	bsv.setYuns(rsl.getDouble("yuns"));					//实际运损
            	bsv.setYunfjsl(rsl.getDouble("yunfjssl")-Jieskdl);	//运费结算数量
            	bsv.setChaokdl(rsl.getDouble("chaokdl")); 			//超亏吨量（要放到danpcmxb中）
            	
//            	if(!blnDandszyfjssl&&Jieslx==Locale.liangpjs_feiylbb_id){
////            		没有单独设置运费结算量且结算方式为两票，则运费结算数据量为gongfsl
//            		bsv.setYunfjsl(rsl.getDouble("biaoz"));
//            	}
            	
//	        	厂方指标
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
	            
//              矿方指标
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
                
                
//              结算指标
                String strcforkf="_cf";
//                if(jies_shifykfzljs.equals("是")){
                if(bsv.getShifykfzljs().equals("是")){
//                	是否用矿方质量结算
                	strcforkf="_kf";
                }
                
                if(Jieszbsftz.equals("是")){
                	
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
                    
                }else if(Jieszbsftz.equals("否")){
                	
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
                    	
                    	bsv.setErroInfo("没有矿方化验数据，请录入！");
                    }
                }
                bsv.setYunju_js(bsv.getYunju_cf());		//运距赋值
                bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), Shangcjsl));	//实际结算数量+上次结算数量（为了算数量折价用）
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return bsv;
	}
	
//	大唐国际暂估算法，得到数量质量
	
	private void getJiesszl_PerFah(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id,double Jiesrl){
		
//		结算数、质量
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
			
			String jies_Jqsl="jingz";								//结算加权数量
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
			String jies_shifykfzljs="否";
			String jies_Jieslqzfs="sum(round_new())";				//结算数量取整方式
			String jies_Jsslblxs="0";								//结算数量保留小数位
			String jies_Kdkskzqzfs="round_new(sum())";				//扣吨、扣水、扣杂取整方式
			String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//结算数量
			String jies_yunfjssl="jingz";							//运费结算数量
			boolean blnDandszyfjssl=false;	//是否单独设置了运费结算数量，如果是则yunfjssl=设置数量，如果不是且两票结算yunfjssl=gongfsl
			String jiscdkd="否";										//是否计算超吨、扣吨
			String ChaodOrKuid="";									//计算超吨还是亏吨
			String jies_Guohlqzfs=Locale.anlsswrhxj_jiesghlqzfs_xitxx;	//系统信息中过衡量取整方式，默认为：按列四舍五入后相加
			String jies_Guohlblxsw="0";									//系统信息中过衡量保留小数位，默认为：2
			
			
//			从系统信息表中取结算设置的信息
			String XitxxArrar[][]=null;	
			XitxxArrar=MainGlobal.getXitxx_items("结算",	"select mingc from xitxxb where leib='结算'"
					,String.valueOf(Diancxxb_id));
			
//			分析取得的值，然后对变量进行赋值
			if(XitxxArrar!=null){
				
				for(int i=0;i<XitxxArrar.length;i++){
					
					if(XitxxArrar[i][0]!=null){
						
						if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//							加权数量
							jies_Jqsl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlqzfs_xitxx)){
//							过衡量取整方式
							jies_Guohlqzfs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlblxsw_xitxx)){
//							结算过衡量保留小数位
							jies_Guohlblxsw=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.meiksl_xitxx)){
//							煤款税率
							bsv.setMeiksl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}else if(XitxxArrar[i][0].trim().equals(Locale.yunfsl_xitxx)){
//							运费税率
							bsv.setYunfsl(Double.parseDouble(XitxxArrar[i][1].trim()));
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiscdkd_xitxx)){
//							计算超吨扣吨
							jiscdkd=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.Meikzkkblxsw_xitxx)){
//							煤款增扣款保留小数位
							bsv.setMeikzkkblxsw(Integer.parseInt(XitxxArrar[i][1].trim()));
						}
					}
				}
			}
			
			if(jiscdkd.equals("是")){
//				已在系统信息表中设定了超吨或亏吨的计算
				if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//					到厂价计算“超吨”
					ChaodOrKuid="CD";
				}else if(bsv.getJiesfs().equals(Locale.chukjg_ht_jsfs)){
//					出矿价计算“亏吨”
					ChaodOrKuid="KD";
				}
			}
//			记录超吨Or亏吨
			bsv.setChaodOrKuid(ChaodOrKuid);
			
			if(jies_Guohlqzfs.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
//				过衡量取整方式:按列四舍五入后相加
				jies_Guohlqzfs="sum(round_new())";
			}else if(jies_Guohlqzfs.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
//				过衡量取整方式:相加后统一四舍五入
				jies_Guohlqzfs="round_new(sum())";
			}else if(jies_Guohlqzfs.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
//				过衡量取整方式:不进行取整操作
				jies_Guohlqzfs="sum()";
			}else{
//				过衡量取整方式:如果都没得到默认为“按列四舍五入后相加”
				jies_Guohlqzfs="sum(round_new())";
			}
			
			if(Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){

	        	String JiesszArray[][]=null;
			
	        	JiesszArray=Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
		
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
							
//							jies_shifykfzljs=JiesszArray[i][1];
							bsv.setShifykfzljs(JiesszArray[i][1]);
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
							
//							煤款含税单价保留小数位
							bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
							
//							运费含税单价保留小数位
							bsv.setYunfhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
						}else if(JiesszArray[i][0].equals(Locale.kuidjfyf_jies)){
							
//							亏吨拒付运费
							bsv.setKuidjfyf(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.Mj_to_kcal_xsclfs_jies)){
							
//							兆焦转大卡
							bsv.setMj_to_kcal_xsclfs(JiesszArray[i][1]);
						}else if(JiesszArray[i][0].equals(Locale.meikhsdjqzfs_jies)){
							
//							含税单价取整方式
							bsv.setMeikhsdj_qzfs(JiesszArray[i][1]);
						}
					}
				}
	        }
			
			StringBuffer sql=new StringBuffer("");
			
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Std_cf,0) as Std_cf,nvl(Mt_cf,0) as Mt_cf,nvl(Mad_cf,0) as Mad_cf,nvl(Aar_cf,0) as Aar_cf,nvl(Aad_cf,0) as Aad_cf,nvl(Ad_cf,0) as Ad_cf,				\n");
				sql.append(" 		nvl(Vad_cf,0) as Vad_cf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Stad_cf,0) as Stad_cf,nvl(Star_cf,0) as Star_cf,nvl(Had_cf,0) as Had_cf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(T2_cf,0) as T2_cf,	\n");
				sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,yingd,yingd-yingk as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz,						\n");
				sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,   \n");
				sql.append("	 --厂方验收   																															\n");
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
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf				 		\n");
				
				sql.append("	 	from fahb f,zhilb z,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz  								\n");
				sql.append("			where f.zhilb_id=z.id(+) and f.faz_id=cz.id and f.pinzb_id=pz.id   								\n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.id in("+SelIds+")) 	\n");
			
			rsl=con.getResultSetList(sql.toString());
			if(rsl.next()){
				
//				数量
				bsv.setKoud(rsl.getDouble("koud"));					//扣吨
            	bsv.setKous(rsl.getDouble("kous"));					//扣水
            	bsv.setKouz(rsl.getDouble("kouz"));					//扣杂
            	bsv.setChes(rsl.getLong("ches"));					//车数
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//标重
            	bsv.setYingksl(rsl.getDouble("yingk"));				//盈亏  
            	bsv.setYingd(rsl.getDouble("yingd"));				//盈吨
            	bsv.setKuid(rsl.getDouble("kuid"));					//亏吨
            	bsv.setJiessl(rsl.getDouble("jiessl"));		//结算重量
            	bsv.setYanssl(rsl.getDouble("jingz"));				//厂方验收数量
            	bsv.setJingz(rsl.getDouble("jingz"));				//净重
            	bsv.setJiesslcy(bsv.getJiessl()-bsv.getJingz());	//结算数量差异(结算量和过衡量的差值)
            	bsv.setYuns(rsl.getDouble("yuns"));					//实际运损
            	bsv.setYunfjsl(rsl.getDouble("yunfjssl"));			//运费结算数量
				
//	        	厂方指标
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
                
//              结算指标
                String strcforkf="_cf";
                
                bsv.setQnetar_js(rsl.getDouble("Qnetar"+strcforkf));
                if(Jiesrl>0){
//                	取上一次的结算热量或是合同质量标准中的热量
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
                bsv.setStar_js(rsl.getDouble("Star"+strcforkf));
                bsv.setHad_js(rsl.getDouble("Had"+strcforkf));
                bsv.setQbad_js(rsl.getDouble("Qbad"+strcforkf));
                bsv.setQgrad_js(rsl.getDouble("Qgrad"+strcforkf));
                bsv.setT2_js(rsl.getDouble("T2"+strcforkf));
                
                bsv.setYunju_js(bsv.getYunju_cf());		//运距赋值
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
//	大唐国际暂估算法，得到数量质量_End
	
	//计算煤价,热量折价,硫折价,灰熔点折价
	private boolean getMeiPrice(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String Jieszbsftz,String SelIds,long Gongysb_id,
									double Jieskdl,double Shangcjsl){
		//得到合同信息中的运价
		JDBCcon con =new JDBCcon();
		String sql="";
		Interpreter bsh=new Interpreter();
		Jiesdcz Jscz=new Jiesdcz();
		try{
			
//			数量(合同中以月为单位，每月存一条，暂定先取一条数)
			sql="select nvl(htsl.hetl,0) as hetl,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh \n"
				+ " from hetb htb, hetslb htsl		\n"
				+ " where htb.id=htsl.hetb_id(+)	\n"      
				+ " and (htsl.pinzb_id="+Ranlpzb_id+" or htsl.pinzb_id is null) and (yunsfsb_id="+Yunsfsb_id
				+ " or yunsfsb_id is null) and (faz_id="+Faz_id+" or faz_id is null) and (daoz_id="+Daoz_id+" or daoz_id is null)	\n"
				+ " and (htsl.diancxxb_id="+Diancxxb_id+" or htsl.diancxxb_id is null) and (htsl.riq<=to_date('"+Jiesdcz.FormatDate(Minfahrq)
				+ "','yyyy-MM-dd') or htsl.riq is null)	\n"              
				+ " and htb.id="+Hetb_id+"";
			
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()){
				
				bsv.setHetml(rsl.getString("hetl"));
				bsv.setShoukdw(rsl.getString("gongfdwmc"));
				bsv.setKaihyh(rsl.getString("gongfkhyh"));
				bsv.setZhangH(rsl.getString("gongfzh"));
			}
			
//			质量(合同中一个合同号对应多个质量记录)
			sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
				+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
				+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
				+ " and htzl.danwb_id=dwb.id	\n"
				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
				+ " and htb.id="+Hetb_id+" ";
			
//			价格（合同中一个合同对应多个基础价格）	
			sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,jijlx,	\n"
				+ " jijdw.bianm as jijdw,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,htjg.pinzb_id,		\n"
				+ " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs,fengsjj	\n"
				+ " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
				+ " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
				+ " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n" 
				+ " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
				+ " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n" 
				+ " and htjg.yunsfsb_id=ysfs.id																\n"
				+ " and htjg.hetjjfsb_id=htjfsb.id															\n"
				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"          
				+ " and htb.id="+Hetb_id+" order by shangx desc";
			
			rsl=con.getResultSetList(sql);
			
			if(rsl.next()){
				
				bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
				bsv.setJijlx(rsl.getInt("jijlx"));				//基价类型（0、含税；1、不含税）
				bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
				bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
				bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
				bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
				bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
				bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
				bsv.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
				bsv.setFengsjj(rsl.getDouble("fengsjj")); 		//合同价格中的分公司加价（统一结算用）
				bsv.setChengbzb(rsl.getDouble("xiax"));			//计算成本用
//				分公司加价处理逻辑：
//					1、根据合同价格类型（含税、不含税）算出原始结算价（可能是含税也可能是不含税），
//						用变量保存,并将分公司加价进行保存。
//					2、如果是含税价，结算单价=结算单价+分公司加价；
//							如果是不含税价，结算单价=最后计算出的含税价+分公司加价
//
				bsv.setJiagpzId(rsl.getString("pinzb_id"));			//价格里的品种，为了区分一个合同不同品种不同价格的情况
			
				bsh.set("结算形式", bsv.getJiesxs());
				bsh.set("计价方式", bsv.getHetjjfs());
				bsh.set("价格单位", bsv.getHetmdjdw());	
				bsh.set("合同价格", bsv.getHetmdj());
				bsh.set("最高煤价", bsv.getZuigmj());
				
//				合同基价指标,取出符合条件的合同基价
				if(rsl.getRows()==1){
					
//					计算运费
					if(Jieslx==Locale.liangpjs_feiylbb_id){
						
						getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
					}
					
//					就一条合同
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								目录价结算
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//								单批次结算
							
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
								
//								获得结算数量、质量
								getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs,bsv.getUser_custom_mlj_jiesgs());			
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0);
								
//								符合含税单价
								reCountMj();
								
//								计算煤款金额
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						加权平均
							
//							获得结算数量、质量
							getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
							
//							为目录价赋值
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							获得增扣款
							getZengkk(Hetb_id,bsh);
							
//							煤款含税单价保留小数位
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							用户自定义公式
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							含税单价取整方式
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							增扣款保留小数位
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							执行公式
							bsh.eval(bsv.getGongs_Mk());
							
//							得到计算后的指标
							setJieszb(bsh,0);
							
//							符合含税单价
							reCountMj();
							
//							计算煤款金额
							computData();
						}
						
					}else{		
						
//						一条合同
//						非目录价
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//							单批次结算
							
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
								
//								获得结算数量、质量
								getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
								double Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								
								Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
								
								bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
								bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));								//指标单位
								
								bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
								
								bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
								bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
								
								bsh.set(rsl.getString("zhib")+"增付单价", 	0);
								bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
								bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
								bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
								bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
								bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
								bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
								bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
								bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
								bsh.set(rsl.getString("zhib")+"小数处理", 	"");
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_fmlj_jiesgs,bsv.getUser_custom_fmlj_jiesgs());
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0);
								
//								符合含税单价
								reCountMj();
								
//								计算煤款金额
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
							
//							获得结算数量、质量
							getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
							
							double Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
							Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
							
							bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
							bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
							
							bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
							
							bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
							bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
							
							bsh.set(rsl.getString("zhib")+"增付单价", 	0);
							bsh.set(rsl.getString("zhib")+"增付单价公式", "");
							bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
							bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
							bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
							bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
							bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
							bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
							bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
							bsh.set(rsl.getString("zhib")+"小数处理", 	"");
							
//							获得增扣款
							getZengkk(Hetb_id,bsh);
							
//							用户自定义公式
							bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
							
//							煤款含税单价保留小数位
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							含税单价取整方式
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							增扣款保留小数位
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							执行公式
							bsh.eval(bsv.getGongs_Mk());
							
//							得到计算后的指标
							setJieszb(bsh,0);
							
//							符合含税单价
							reCountMj();
							
//							计算煤款金额
							computData();
						}
					}
					
					bsv.setHetjgpp_Flag(true);
					bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
				}else{
//					有多个合同
//					目录价
					
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//					目录价结算
					
//						计算运费
						if(Jieslx==Locale.liangpjs_feiylbb_id){
							
							getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
						}
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){		//					单批次结算
						
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
								
//								获得结算数量、质量
								getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0);
								
//								符合含税单价
								reCountMj();
								
//								计算煤款金额
								computData_Dpc();
							}
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均

							
//							获得结算数量、质量
							getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
							
//							为目录价赋值
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							获得增扣款
							getZengkk(Hetb_id,bsh);
							
//							用户自定义公式
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							煤款含税单价保留小数位
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							含税单价取整方式
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							增扣款保留小数位
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							执行公式
							bsh.eval(bsv.getGongs_Mk());
							
//							得到计算后的指标
							setJieszb(bsh,0);
							
//							符合含税单价
							reCountMj();
							
//							计算煤款金额
							computData();
						}
					}else{
						
//						多个合同
//						非目录价
						double	Dbljijzb=0;
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){	//单批次结算
							
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
									
//									获得结算数量、质量
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
									rsl.beforefirst();
									
									while(rsl.next()){
										bsv.setJiagpzId(rsl.getString("pinzb_id"));	
										Dbljijzb=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
										Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
									
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
												&&Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
										){
										
											bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
											bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
											bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
											bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
											bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
											bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
											bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
											bsv.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
										
											bsh.set("结算形式", bsv.getJiesxs());
											bsh.set("计价方式", bsv.getHetjjfs());
											bsh.set("价格单位", bsv.getHetmdjdw());	
											bsh.set("合同价格", bsv.getHetmdj());
											bsh.set("最高煤价", bsv.getZuigmj());
											
											bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
											bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
											
											bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标

											bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
											bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
											
											bsh.set(rsl.getString("zhib")+"增付单价", 	0);
											bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
											bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
											bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
											bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
											bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
											bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
											bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
											bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
											bsh.set(rsl.getString("zhib")+"小数处理", 	"");
											
//											获得增扣款
											getZengkk(Hetb_id,bsh);
											
//											用户自定义公式
											bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
											
//											煤款含税单价保留小数位
											bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
											
//											含税单价取整方式
											bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
											
//											增扣款保留小数位
											bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
											
//											执行公式
											bsh.eval(bsv.getGongs_Mk());
											
//											得到计算后的指标
											setJieszb(bsh,0);
											
//											符合含税单价
											reCountMj();
											
//											计算煤款金额
											computData_Dpc();
											
											bsv.setHetjgpp_Flag(true);
											bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
										}	
									}
								}		
								
									
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
							
//									获得结算数量、质量
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Shangcjsl);
								
								do{
									bsv.setJiagpzId(rsl.getString("pinzb_id"));	
									Dbljijzb=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
									
									Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
									
									if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
											&&Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
											){
										
										bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
										bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
										bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
										bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
										bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
										bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
										bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
										bsv.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
									
										bsh.set("结算形式", bsv.getJiesxs());
										bsh.set("计价方式", bsv.getHetjjfs());
										bsh.set("价格单位", bsv.getHetmdjdw());	
										bsh.set("合同价格", bsv.getHetmdj());
										bsh.set("最高煤价", bsv.getZuigmj());
										
										bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//合同价格表id
										
										bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
										bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
										
										bsv.setYifzzb(rsl.getString("zhib"));			//默认的已赋值指标
										
										bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
										bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
										
										bsh.set(rsl.getString("zhib")+"增付单价", 	0);
										bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
										bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
										bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
										bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
										bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
										bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
										bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
										bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
										bsh.set(rsl.getString("zhib")+"小数处理", 	"");
										
//											获得增扣款
										getZengkk(Hetb_id,bsh);
										
//										用户自定义公式
										bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
										
//										煤款含税单价保留小数位
										bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
										
//										含税单价取整方式
										bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
										
//										增扣款保留小数位
										bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
										
//										执行公式
										bsh.eval(bsv.getGongs_Mk());
										
//										得到计算后的指标
										setJieszb(bsh,0);
										
//										计算运费（注意：只要算一次）
										if(Jieslx==Locale.liangpjs_feiylbb_id){
//											判断条件：是两票结算
											getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
										}
										
//										符合含税单价
										reCountMj();
										
//										计算煤款金额
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
				
				bsv.setErroInfo("没有合同价格与结算数据匹配！");
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
	
//	大唐国际暂估用，结算煤价算法
	
//	计算煤价,热量折价,硫折价,灰熔点折价
	private boolean getMeiPrice_PerFah(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String SelIds,long Gongysb_id,double Jiesrl){
		//得到合同信息中的运价
		JDBCcon con =new JDBCcon();
		String sql="";
		Interpreter bsh=new Interpreter();
		Jiesdcz Jscz=new Jiesdcz();
		try{
			
//			数量(合同中以月为单位，每月存一条，暂定先取一条数)
			sql="select nvl(htsl.hetl,0) as hetl,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh \n"
				+ " from hetb htb, hetslb htsl		\n"
				+ " where htb.id=htsl.hetb_id(+)	\n"      
				+ " and (htsl.pinzb_id="+Ranlpzb_id+" or htsl.pinzb_id is null) and (yunsfsb_id="+Yunsfsb_id
				+ " or yunsfsb_id is null) and (faz_id="+Faz_id+" or faz_id is null) and (daoz_id="+Daoz_id+" or daoz_id is null)	\n"
				+ " and (htsl.diancxxb_id="+Diancxxb_id+" or htsl.diancxxb_id is null) and (htsl.riq<=to_date('"+Jiesdcz.FormatDate(Minfahrq)
				+ "','yyyy-MM-dd') or htsl.riq is null)	\n"              
				+ " and htb.id="+Hetb_id+"";
			
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()){
				
				bsv.setHetml(rsl.getString("hetl"));
				bsv.setShoukdw(rsl.getString("gongfdwmc"));
				bsv.setKaihyh(rsl.getString("gongfkhyh"));
				bsv.setZhangH(rsl.getString("gongfzh"));
			}
			
//			质量(合同中一个合同号对应多个质量记录)
			sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
				+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
				+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
				+ " and htzl.danwb_id=dwb.id	\n"
				+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
				+ " and htb.id="+Hetb_id+" ";
			
//			价格（合同中一个合同对应多个基础价格）	
			sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,				\n"
				+ " jijdw.bianm as jijdw,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,					\n"
				+ " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs			\n"
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
				
				bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
				bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
				bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
				bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
				bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
				bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
				bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
				bsv.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
				bsv.setFengsjj(rsl.getDouble("fengsjj")); 		//合同价格中的分公司加价（统一结算用）
//				分公司加价处理逻辑：
//					1、根据合同价格类型（含税、不含税）算出原始结算价（可能是含税也可能是不含税），
//						用变量保存,并将分公司加价进行保存。
//					2、如果是含税价，结算单价=结算单价+分公司加价；
//							如果是不含税价，结算单价=最后计算出的含税价+分公司加价
//
				bsv.setJiagpzId(rsl.getString("pinzb_id"));			//价格里的品种，为了区分一个合同不同品种不同价格的情况
				
				bsh.set("结算形式", bsv.getJiesxs());
				bsh.set("计价方式", bsv.getHetjjfs());
				bsh.set("价格单位", bsv.getHetmdjdw());	
				bsh.set("合同价格", bsv.getHetmdj());
				bsh.set("最高煤价", bsv.getZuigmj());
				
//				合同基价指标,取出符合条件的合同基价
				if(rsl.getRows()==1){
					
//					就一条合同
					
//					计算运费
					if(Jieslx==Locale.liangpjs_feiylbb_id){
						
						getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
					}
					
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								目录价结算
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//								单批次结算
							
							String[] test=null;
							
							test=SelIds.split(",");
							
							for(int i=0;i<test.length;i++){
								
//								获得结算数量、质量
								getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	

//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs,bsv.getUser_custom_mlj_jiesgs());			
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0);
								
//								计算煤款金额
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						加权平均
							
//							获得结算数量、质量
							getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
							
//							为目录价赋值
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							获得增扣款
							getZengkk(Hetb_id,bsh);
							
//							煤款含税单价保留小数位
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							用户自定义公式
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							含税单价取整方式
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							增扣款保留小数位
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							执行公式
							bsh.eval(bsv.getGongs_Mk());
							
//							得到计算后的指标
							setJieszb(bsh,0);
							
//							计算煤款金额
							computData();
						}
						
					}else{		
						
//						一条合同
//						非目录价
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//							单批次结算
							
							String[] test=null;
							test=SelIds.split(",");
							
							for(int i=0;i<test.length;i++){
								
//								获得结算数量、质量
								getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
								double Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
								
								bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
								bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));								//指标单位
								
								bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
								
								bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
								bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
								
								bsh.set(rsl.getString("zhib")+"增付单价", 	0);
								bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
								bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
								bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
								bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
								bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
								bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
								bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
								bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
								bsh.set(rsl.getString("zhib")+"小数处理", 	"");
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_fmlj_jiesgs,bsv.getUser_custom_fmlj_jiesgs());
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0);
								
//								计算煤款金额
								computData_Dpc();
							}
							
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
							
//							获得结算数量、质量
							getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
							
							double Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
							Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
							
							bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
							bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
							
							bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
							
							bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
							bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
							
							bsh.set(rsl.getString("zhib")+"增付单价", 	0);
							bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
							bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
							bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
							bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
							bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
							bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
							bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
							bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
							bsh.set(rsl.getString("zhib")+"小数处理", 	"");
							
//							获得增扣款
							getZengkk(Hetb_id,bsh);
							
//							用户自定义公式
							bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
							
//							煤款含税单价保留小数位
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							含税单价取整方式
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							增扣款保留小数位
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							执行公式
							bsh.eval(bsv.getGongs_Mk());
							
//							得到计算后的指标
							setJieszb(bsh,0);
							
//							计算煤款金额
							computData();
						}
					}
					
					bsv.setHetjgpp_Flag(true);
					bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
					
				}else{
//					有多个合同
//					目录价
					
					if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//					目录价结算
						
//						计算运费
						if(Jieslx==Locale.liangpjs_feiylbb_id){
							
							getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
						}
					
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){		//					单批次结算
						
							String[] test=null;
							test=SelIds.split(",");
							
							for(int i=0;i<test.length;i++){
								
//								获得结算数量、质量
								getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0);
								
//								计算煤款金额
								computData_Dpc();
							}
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均

							
//							获得结算数量、质量
							getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
							
//							为目录价赋值
							computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id,Hetb_id);
							
//							获得增扣款
							getZengkk(Hetb_id,bsh);
							
//							用户自定义公式
							bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
							
//							煤款含税单价保留小数位
							bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
							
//							含税单价取整方式
							bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
							
//							增扣款保留小数位
							bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
							
//							执行公式
							bsh.eval(bsv.getGongs_Mk());
							
//							得到计算后的指标
							setJieszb(bsh,0);
							
//							计算煤款金额
							computData();
						}
					}else{
						
//						多个合同
//						非目录价
						double	Dbljijzb=0;
						
						if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){	//单批次结算
							
								String[] test=null;
								
								test=SelIds.split(",");
						
								for(int i=0;i<test.length;i++){
									
//									获得结算数量、质量
									getJiesszl_PerFah(test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
									rsl.beforefirst();
									
									while(rsl.next()){
										
										Dbljijzb=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
										Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
									
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
												&&Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
												){
										
											bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
											bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
											bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
											bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
											bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
											bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
											bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
											bsv.setHetjjfs(rsl.getString("hejfs"));		//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
										
											bsh.set("结算形式", bsv.getJiesxs());
											bsh.set("计价方式", bsv.getHetjjfs());
											bsh.set("价格单位", bsv.getHetmdjdw());	
											bsh.set("合同价格", bsv.getHetmdj());
											bsh.set("最高煤价", bsv.getZuigmj());
											
											bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
											bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
											
											bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
											
											bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
											bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
											
											bsh.set(rsl.getString("zhib")+"增付单价", 	0);
											bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
											bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
											bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
											bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
											bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
											bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
											bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
											bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
											bsh.set(rsl.getString("zhib")+"小数处理", 	"");
											
//											获得增扣款
											getZengkk(Hetb_id,bsh);
											
//											用户自定义公式
											bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
											
//											煤款含税单价保留小数位
											bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
											
//											含税单价取整方式
											bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
											
//											增扣款保留小数位
											bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
											
//											执行公式
											bsh.eval(bsv.getGongs_Mk());
											
//											得到计算后的指标
											setJieszb(bsh,0);
											
//											计算煤款金额
											computData_Dpc();
											
											bsv.setHetjgpp_Flag(true);
											bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
										}	
									}
								}		
								
									
						}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
							
//									获得结算数量、质量
								getJiesszl_PerFah(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jiesrl);
								
								do{
									
									Dbljijzb=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
									Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
									
									if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
											&&Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
											){
										
										bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
										bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
										bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
										bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
										bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
										bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
										bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
										bsv.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
									
										bsh.set("结算形式", bsv.getJiesxs());
										bsh.set("计价方式", bsv.getHetjjfs());
										bsh.set("价格单位", bsv.getHetmdjdw());	
										bsh.set("合同价格", bsv.getHetmdj());
										bsh.set("最高煤价", bsv.getZuigmj());
										
										bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//合同价格表id
										
										bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
										bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
										
										bsv.setYifzzb(rsl.getString("zhib"));			//默认的已赋值指标
										
										bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
										bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
										
										bsh.set(rsl.getString("zhib")+"增付单价", 	0);
										bsh.set(rsl.getString("zhib")+"增付单价公式", 	"");
										bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
										bsh.set(rsl.getString("zhib")+"扣付单价公式", 	"");
										bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
										bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
										bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
										bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
										bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
										bsh.set(rsl.getString("zhib")+"小数处理", 	"");
										
//											获得增扣款
										getZengkk(Hetb_id,bsh);
										
//											用户自定义公式
										bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
										
//										煤款含税单价保留小数位
										bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());
										
//										含税单价取整方式
										bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
										
//										增扣款保留小数位
										bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
										
//										执行公式
										bsh.eval(bsv.getGongs_Mk());
										
//										得到计算后的指标
										setJieszb(bsh,0);
										
//										计算运费（注意：只要算一次）
										if(Jieslx==Locale.liangpjs_feiylbb_id){
//											判断条件：是两票结算
											getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
										}
										
//										计算煤款金额
										computData();
										
										bsv.setHetjgpp_Flag(true);
										bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
									}
									
								}while(rsl.next());
						}
					}
					
					if(Jieslx==Locale.liangpjs_feiylbb_id){
						
						getYunFei(SelIds,Jieslx,bsv.getHetb_Id());
					}
				}
			}
			
			if(!bsv.getHetjgpp_Flag()){
				
				bsv.setErroInfo("没有合同价格与结算数据匹配！");
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
//		增扣款
		JDBCcon con=new JDBCcon();
		try{
			ResultSetList rsl=null;
			
			String sql="select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,nvl(shangx,0) as shangx,nvl(xiax,0) as xiax,dwb.bianm as danw,nvl(jis,0) as jis,	\n"
				+ " jisdw.bianm as jisdw,nvl(kouj,0) as kouj,koujgs,kjdw.bianm as koujdw,nvl(zengfj,0) as zengfj,zengfjgs,zfjdw.bianm as zengfjdw,	\n"
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
			double Dbltmp=0; 		//记录指标结算值
			double Dblczxm=0;		//记录参照项目的值
			String Strtmp="";		//记录设定的指标
			String Strimplementedzb="";	//记录已经执行过的指标（即已经参与过执行的指标）。
			double Dblimplementedzbsx=0;	//记录已执行过的指标的上限
			StringBuffer sb = new StringBuffer();	//记录合同增扣款中的适用范围为1的记录
			
			while(rsl.next()){
				
//				指标的结算指标
				Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
//				得到参照项目的结算标准
				Dblczxm=Jiesdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");

//				指标的结算指标
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
				Dblczxm=Jiesdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
				
				if(Dbltmp>=Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))
						&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
						&&Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
						)	
				{
					
					//指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
					Strimplementedzb = rsl.getString("zhib");	//记录已使用的指标
					Dblimplementedzbsx = rsl.getDouble("shangx");
					
					bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
					bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));			//指标单位
					bsh.set(rsl.getString("zhib")+"增扣款条件", 	rsl.getString("tiaoj"));		//大于等于、大于、小于、小于等于、	区间、等于
					bsh.set(rsl.getString("zhib")+"增付单价",		rsl.getDouble("zengfj"));		//增付价
					bsh.set(rsl.getString("zhib")+"增付单价公式",	rsl.getString("zengfjgs")==null?"":rsl.getString("zengfjgs"));		//增付价公式
					bsh.set(rsl.getString("zhib")+"扣付单价",		rsl.getDouble("kouj"));			//扣价
					bsh.set(rsl.getString("zhib")+"扣付单价公式",	rsl.getString("koujgs")==null?"":rsl.getString("koujgs"));			//扣价公式
					bsh.set(rsl.getString("zhib")+"增付价单位", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//增价单位
					bsh.set(rsl.getString("zhib")+"扣付价单位", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//扣价单位
					bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
					bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
					bsh.set(rsl.getString("zhib")+"增扣款基数",	rsl.getDouble("jis"));			//基数（每升高xx或降低xx）
					bsh.set(rsl.getString("zhib")+"增扣款基数单位",	rsl.getString("jisdw"));	//基数单位
					bsh.set(rsl.getString("zhib")+"基准增扣价",	rsl.getDouble("jizzkj"));		//基准增扣价（用于多段增扣价累计时使用）
					bsh.set(rsl.getString("zhib")+"小数处理",	Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//小数处理（每升高xx或降低xx）
					
					Strtmp+="'"+rsl.getString("zhib")+"',";					//记录用户设置的影响结算单价的指标
//						处理曾扣款适用范围方法：
//						原理：先将增扣款的适用信息记录到一个StringBuffer变量中，形式为:Qnetar,1;结算数量,1;
//						(注：如果shiyfw为1认为是超出部分适用，才记录，如果是0则是适用于全部数据，不用记录) 
//						使用时解析这个StringBufffer
					if(rsl.getInt("shiyfw")>0){
						
//						适用范围为1 说明只对超出部分进行操作
						sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
					}
				}
			}
			
			bsv.setMeikzkksyfw(sb);	//记录煤款增扣款有适用范围为超出部分的数据
			
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
				
				Strtmpdw=Jiesdcz.getZhibbdw(rsl.getString("zhib"),"");
				
				bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//结算值
				bsh.set(rsl.getString("zhib")+"计量单位", 	Strtmpdw);				//指标单位
				bsh.set(rsl.getString("zhib")+"增扣款条件", 	"区间");					//大于等于、大于、小于、小于等于、	区间、等于
				bsh.set(rsl.getString("zhib")+"增付单价",		0);						//增付价
				bsh.set(rsl.getString("zhib")+"增付单价公式",	"");					//增付价
				bsh.set(rsl.getString("zhib")+"扣付单价",		0);						//扣价
				bsh.set(rsl.getString("zhib")+"扣付单价公式",	"");					//扣价
				bsh.set(rsl.getString("zhib")+"增付价单位", 	"");					//增价单位
				bsh.set(rsl.getString("zhib")+"扣付价单位", 	"");					//扣价单位
				bsh.set(rsl.getString("zhib")+"上限", 		0);						//指标上限
				bsh.set(rsl.getString("zhib")+"下限", 		0);						//指标下限
				bsh.set(rsl.getString("zhib")+"增扣款基数",	0);						//基数（每升高xx或降低xx）
				bsh.set(rsl.getString("zhib")+"增扣款基数单位",	"");				//基数单位
				bsh.set(rsl.getString("zhib")+"基准增扣价",	0);						//基准增扣价（用于多段增扣价累计时使用）
				bsh.set(rsl.getString("zhib")+"小数处理",	"");					//小数处理（每升高xx或降低xx）
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
//		得到hetys.id
		JDBCcon con= new JDBCcon();
		long lngYunshtb_id=0;
		long lngFinYshtb_id=0;
		
//		情况1：如果是两票结算、先从煤款合同表中取运费、
//		如果没有取到运费，再从煤款运费合同关联表中取出运费合同id,
//		再根据运费合同中的煤矿取出结算价格
		
		try{
//			在getMeiprise方法中，已经记录下合同所用的hetjgb_id
			ResultSet rs=null;
			String sql="select yunj,danwb.bianm as yunjdw from hetjgb,danwb 	\n"
				+" where hetjgb.yunjdw_id=danwb.id and hetjgb.id="+bsv.getHetjgb_id();
			ResultSet rec=con.getResultSet(sql);
			while(rec.next()){
				
				bsv.setHetyj(rec.getDouble("yunj"));
				bsv.setHetyjdw(rec.getString("yunjdw"));
			}
			
			if(bsv.getHetyj()==0){
//				如果没有取到运费，再从煤款运费合同关联表中取出运费合同id
				
				sql="select hetys_id from meikyfhtglb where hetb_id="+Hetb_id;
				rec=con.getResultSet(sql);
				while(rec.next()){
//					有多个运费合同
					lngYunshtb_id=rec.getLong("hetys_id");
					sql="select hetys.id from hetysjgb,hetys 	\n"
						+ " where hetys.id=hetysjgb.hetys_id	\n"
						+ " and hetys.id="+lngYunshtb_id+" 		\n"
						+ " and (meikxxb_id="+bsv.getMeikxxb_Id()
						+" or meikxxb_id=0)";
					
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
//		计算运费
//		思路：
//			情况1：煤款合同中有运价（bsv.getHetyj()>0），运价单位有两种（元/吨，元/吨*公里）,
//					没有曾扣款情况.因为没法得到运费合同，所以不能算增扣款
//			情况2：是Hetyj、Hetyjdw无值,要从hetysjgb中取值，有增扣款
		try{
			
			Interpreter bsh=new Interpreter();
			String sql="";
//			运费含税单价保留小数位
			if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//				情况1
//				取hetb中的运价
				bsh.set("合同运价", bsv.getHetyj());
				bsh.set("合同运价单位", bsv.getHetyjdw());
				bsh.set("运价里程", bsv.getYunju_js());
				
				if(!bsv.getGongs_Yf().equals("")){
					
					
//					运费含税单价保留小数位
					bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
					
					getZengkk_yf(Hetb_id,bsh);
					
//					计算运费
					bsh.eval(bsv.getGongs_Yf());
//					bsv.setYunfjsdj(Double.parseDouble(bsh.get("运费结算单价").toString()));
					setJieszb(bsh,1);
						
				}else{
					
					bsv.setErroInfo("请设置运费计算公式");
					return;
				}
			}else{
				
//				取hetys中的运价
				JDBCcon con=new JDBCcon();
				sql=" select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
					+ " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n" 
					+ " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
					+ " danwb yjdw										\n"
					+ " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id	\n" 
					+ " and jg.tiaojb_id=tj.id(+)						\n"  
					+ " and jg.danwb_id=dw.id(+)						\n" 
					+ " and jg.yunjdw_id=yjdw.id(+)						\n"
					+ " and ht.id="+Hetb_id+" and (jg.meikxxb_id="
					+bsv.getMeikxxb_Id()+" or jg.meikxxb_id = 0)	";
				
				ResultSetList rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					if(rsl.getLong("yunsjgfab_id")>0){
//						处理运费结算方案的特殊算法
						bsh.set("合同运价", 0);
						bsh.set("合同运价单位", Locale.yuanmd_danw);
						bsh.set("运价里程", 0);
						
//						解析运费价格明细中的数据
						bsh=Jiesdcz.CountYfjsfa(bsv,bsv.getMeikxxb_Id(),bsv.getDiancxxb_id(),bsv.getFaz_Id(),
								bsv.getDaoz_Id(),rsl.getLong("yunsjgfab_id"),bsh);
						
						if(Double.parseDouble(bsh.get("合同运价").toString())==0){
							
							bsv.setErroInfo("没有和运输价格方案匹配的数据！");
							return;
						}
						
//						获得增扣款
						getZengkk_yf(Hetb_id,bsh);
						
//						运费含税单价保留小数位
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						bsh.eval(bsv.getGongs_Yf());
						setJieszb(bsh,1);
						
					}else{
						
						
					}
					
					if(rsl.getRows()==1){
//						就一条合同价格
						bsh.set("合同运价", rsl.getDouble("yunja"));
						bsh.set("合同运价单位", rsl.getString("yunjdw"));
						bsh.set("运价里程", bsv.getYunju_js());
						
//						获得增扣款
						getZengkk_yf(Hetb_id,bsh);
						
//						运费含税单价保留小数位
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						bsh.eval(bsv.getGongs_Yf());
						setJieszb(bsh,1);
						
					}else{
//						多个合同价格
						double shangx=0;
						double xiax=0;
						double yunju=bsv.getYunju_cf();	//运距
						double Dbltmp=0;
						
						do{
							shangx=rsl.getDouble("shangx");
							xiax=rsl.getDouble("xiax");
							
							Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
							
							Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
							
							if(yunju>=xiax&&yunju<=(shangx==0?9999:shangx)){
								
								bsh.set("合同运价", rsl.getDouble("yunja"));
								bsh.set("合同运价单位", rsl.getString("yunjdw"));
								bsh.set("运价里程", bsv.getYunju_js());
								
								if(rsl.getDouble("yunja")==0){
									
									bsv.setErroInfo("合同运价为0，请检查合同！");
									return;
								}
								
								getZengkk_yf(Hetb_id,bsh);
								
//								运费含税单价保留小数位
								bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
								
								bsh.eval(bsv.getGongs_Yf());
								setJieszb(bsh,1);
							}
							
						}while(rsl.next());
						rsl.close();
						con.Close();
					}
				}else{
					
					bsv.setErroInfo("没有得到运费合同价格！");
					return;
				}
				
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	private void getZengkk_yf(long Hetb_id,Interpreter bsh){
		
//		运费的增扣款原则：如果运费价格里用到了该指标，还可以在增扣款中累计计算
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
			double Dbltmp=0; 		//记录指标结算值
			String  Strtmp="";		//记录设定的指标
			
			while(rsl.next()){
				
				Dbltmp=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
//				指标的结算指标
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
				
					bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
					bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));			//指标单位
					bsh.set(rsl.getString("zhib")+"增扣款条件", 	rsl.getString("tiaoj"));		//大于等于、大于、小于、小于等于、	区间、等于
					bsh.set(rsl.getString("zhib")+"增付单价",	rsl.getDouble("zengfj"));		//增付价
					bsh.set(rsl.getString("zhib")+"扣付单价",	rsl.getDouble("kouj"));			//扣价
					bsh.set(rsl.getString("zhib")+"增付价单位", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//增价单位
					bsh.set(rsl.getString("zhib")+"扣付价单位", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//扣价单位
					bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
					bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
					bsh.set(rsl.getString("zhib")+"增扣款基数",	rsl.getDouble("jis"));			//基数（每升高xx或降低xx）
					bsh.set(rsl.getString("zhib")+"基准增扣价",	rsl.getDouble("jizzkj"));		//基准增扣价（用于多段增扣价累计时使用）
					bsh.set(rsl.getString("zhib")+"小数处理",	Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//小数处理（每升高xx或降低xx）
					
					Strtmp+="'"+rsl.getString("zhib")+"',";					//记录用户设置的影响运费结算单价的指标
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
					
					bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//结算值
					bsh.set(rsl.getString("zhib")+"计量单位", 	Strtmpdw);				//指标单位
					bsh.set(rsl.getString("zhib")+"增扣款条件", 	"区间");					//大于等于、大于、小于、小于等于、	区间、等于
					bsh.set(rsl.getString("zhib")+"增付单价",	0);						//增付价
					bsh.set(rsl.getString("zhib")+"扣付单价",	0);						//扣价
					bsh.set(rsl.getString("zhib")+"增付价单位", 	"");					//增价单位
					bsh.set(rsl.getString("zhib")+"扣付价单位", 	"");					//扣价单位
					bsh.set(rsl.getString("zhib")+"上限", 		0);						//指标上限
					bsh.set(rsl.getString("zhib")+"下限", 		0);						//指标下限
					bsh.set(rsl.getString("zhib")+"增扣款基数",	0);						//基数（每升高xx或降低xx）
					bsh.set(rsl.getString("zhib")+"基准增扣价",	0);						//基准增扣价（用于多段增扣价累计时使用）
					bsh.set(rsl.getString("zhib")+"小数处理",	"");					//小数处理（每升高xx或降低xx）
				
			}
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	
//	计算运费，矿区杂费，计税扣除，地铁运费
	private boolean getYunFei(String SelIds,long Jieslx,long Hetb_id){
	    JDBCcon con=new JDBCcon();
		try{
		    	
		    	String sql="";
		    	ResultSet rs=null;
		    	String sql_colum="";	//附加列（矿区运费用）
		    	String sql_talbe="";	//附加表（矿区运费用）
		    	long lngJieslx=0;
		    	long lngYunshtb_id=0;
		    	lngJieslx=Jieslx;
		    	
		    	if(lngJieslx==Locale.liangpjs_feiylbb_id||lngJieslx==Locale.guotyf_feiylbb_id
		    			){
		    		
//		    		两票结算、国铁运费
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
//				        两票结算（铁路，从yunfdjb,danjcpb中取值，前提是要先进行货票核对）
//				    	yunf铁路大票上所有的费用
//				    	yunfzf铁路大票上所有不可抵税的费用
//				    	两票结算或国铁运费时用
				    	bsv.setTielyf(rs.getDouble("tielyf"));
				    	bsv.setTielzf(rs.getDouble("tielzf"));
				    	
				    	if(lngJieslx==1||lngJieslx==3){
				    		
//				    		两票结算、国铁运费
				    		bsv.setKuangqyf(rs.getDouble("kuangqyf"));
					    	bsv.setKuangqzf(rs.getDouble("kuangqzf"));
				    	}
				    }
				    
				    if(bsv.getTielyf()==0&&bsv.getTielzf()==0&&bsv.getKuangqyf()==0&&bsv.getKuangqzf()==0){
//				    	如果从yunfdjb,danjcpb中取值不到，说明是从煤款单价中或是运费合同中取数
				    	
//				    	情况1：如果是两票结算、先从煤款合同表中取运费、
//				    			如果没有取到运费，再从煤款运费合同关联表中取出运费合同id,
//				    			再根据运费合同中的煤矿取出结算价格
				    	
//				    	情况2：如果是单结算运费，那么Hetb_id就是运费合同表id,
//				    			再根据运费合同中的煤矿取出结算价格
				    	if(Jieslx==1){
//				    		两票结算，情况1
				    		
				    		if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//			    				如果煤款合同中有运费则计算
				    			
			    				CountYf(0);
			    			}else{
//			    				如果煤款合同中没有运费，从煤款运费合同关联表中取出运费合同id
			    				lngYunshtb_id=getYunshtb_id(Hetb_id);
			    				
			    				if(lngYunshtb_id>0){
//			    					说明有已找到相应的运费合同了
			    					CountYf(lngYunshtb_id);
			    				}else if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//			    					到厂价结算,如果是两票结算运费，而又是到厂价合同的话，不计算运费
			    					
			    				}else{
//			    					出矿价结算,应该算运费,如果从合同中不能得到运价信息就要从yunfwhb中取维护值
			    					CountYffromwh();
//			    					bsv.setErroInfo("煤款合同<"+Jiesdcz.getHetbh(bsv.getHetb_Id())+">没有对应的运费合同，请设置！");
			    					return false;
			    				}
			    			}
				    	}else if(Jieslx!=2){
//				    		国铁运费结算
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
	
	private void CountYffromwh(){
//		先找到上一次的结算运费信息
		String sql="";
		JDBCcon con=new JDBCcon();
		sql="select decode(jy.jiessl,0,0,round_new(jy.guotyf/jy.jiessl,2)*"+bsv.getYunfjsl()+") as guotyf, \n" +
			"		decode(jy.jiessl,0,0,round_new(jy.guotzf/jy.jiessl,2)*"+bsv.getYunfjsl()+") as guotzf,	\n" +
			"		decode(jy.jiessl,0,0,round_new(jy.kuangqyf/jy.jiessl,2)*"+bsv.getYunfjsl()+") as kuangqyf,	\n" +
			"		decode(jy.jiessl,0,0,round_new(jy.kuangqzf/jy.jiessl,2)*"+bsv.getYunfjsl()+") as kuangqzf	\n" +
			" 		from jiesyfb jy\n" + 
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
	//		还没有就要从yunfwhb中取数
			
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
			
//			Type	0:煤款结算
//					1:运费结算
			if(Type==0||(Type==1&&bsv.getJieslx()==Locale.guotyf_feiylbb_id)){
//				如果是煤款结算或两票结算结算煤款时可以进行赋值，否则只有在单独结算运费时才可赋值
			
	//			数量增扣款取值
				bsv.setShul_ht(bsh.get("合同标准_结算数量").toString());
				bsv.setShul_yk(Double.parseDouble(bsh.get("盈亏_结算数量").toString()));
				bsv.setShul_zdj(Double.parseDouble(bsh.get("折单价_结算数量").toString()));
				
	//			Qnetar
				bsv.setQnetar_ht(bsh.get("合同标准_Qnetar").toString());
				bsv.setQnetar_yk(Double.parseDouble(bsh.get("盈亏_Qnetar").toString()));
				bsv.setQnetar_zdj(Double.parseDouble(bsh.get("折单价_Qnetar").toString()));
				
	//			Std
				bsv.setStd_ht(bsh.get("合同标准_Std").toString());
				bsv.setStd_yk(Double.parseDouble(bsh.get("盈亏_Std").toString()));
				bsv.setStd_zdj(Double.parseDouble(bsh.get("折单价_Std").toString()));
				
	//			Ad
				bsv.setAd_ht(bsh.get("合同标准_Ad").toString());
				bsv.setAd_yk(Double.parseDouble(bsh.get("盈亏_Ad").toString()));
				bsv.setAd_zdj(Double.parseDouble(bsh.get("折单价_Ad").toString()));
				
	//			Vdaf
				bsv.setVdaf_ht(bsh.get("合同标准_Vdaf").toString());
				bsv.setVdaf_yk(Double.parseDouble(bsh.get("盈亏_Vdaf").toString()));
				bsv.setVdaf_zdj(Double.parseDouble(bsh.get("折单价_Vdaf").toString()));
				
	//			Mt
				bsv.setMt_ht(bsh.get("合同标准_Mt").toString());
				bsv.setMt_yk(Double.parseDouble(bsh.get("盈亏_Mt").toString()));
				bsv.setMt_zdj(Double.parseDouble(bsh.get("折单价_Mt").toString()));
				
	//			Qgrad
				bsv.setQgrad_ht(bsh.get("合同标准_Qgrad").toString());
				bsv.setQgrad_yk(Double.parseDouble(bsh.get("盈亏_Qgrad").toString()));
				bsv.setQgrad_zdj(Double.parseDouble(bsh.get("折单价_Qgrad").toString()));
				
	//			Qbad
				bsv.setQbad_ht(bsh.get("合同标准_Qbad").toString());
				bsv.setQbad_yk(Double.parseDouble(bsh.get("盈亏_Qbad").toString()));
				bsv.setQbad_zdj(Double.parseDouble(bsh.get("折单价_Qbad").toString()));
				
	//			Had
				bsv.setHad_ht(bsh.get("合同标准_Had").toString());
				bsv.setHad_yk(Double.parseDouble(bsh.get("盈亏_Had").toString()));
				bsv.setHad_zdj(Double.parseDouble(bsh.get("折单价_Had").toString()));
				
	//			Stad
				bsv.setStad_ht(bsh.get("合同标准_Stad").toString());
				bsv.setStad_yk(Double.parseDouble(bsh.get("盈亏_Stad").toString()));
				bsv.setStad_zdj(Double.parseDouble(bsh.get("折单价_Stad").toString()));
				
	//			Star
				bsv.setStar_ht(bsh.get("合同标准_Star").toString());
				bsv.setStar_yk(Double.parseDouble(bsh.get("盈亏_Star").toString()));
				bsv.setStar_zdj(Double.parseDouble(bsh.get("折单价_Star").toString()));
				
	//			Mad
				bsv.setMad_ht(bsh.get("合同标准_Mad").toString());
				bsv.setMad_yk(Double.parseDouble(bsh.get("盈亏_Mad").toString()));
				bsv.setMad_zdj(Double.parseDouble(bsh.get("折单价_Mad").toString()));
				
	//			Aar
				bsv.setAar_ht(bsh.get("合同标准_Aar").toString());
				bsv.setAar_yk(Double.parseDouble(bsh.get("盈亏_Aar").toString()));
				bsv.setAar_zdj(Double.parseDouble(bsh.get("折单价_Aar").toString()));
				
	//			Aad
				bsv.setAad_ht(bsh.get("合同标准_Aad").toString());
				bsv.setAad_yk(Double.parseDouble(bsh.get("盈亏_Aad").toString()));
				bsv.setAad_zdj(Double.parseDouble(bsh.get("折单价_Aad").toString()));
				
	//			Vad
				bsv.setVad_ht(bsh.get("合同标准_Vad").toString());
				bsv.setVad_yk(Double.parseDouble(bsh.get("盈亏_Vad").toString()));
				bsv.setVad_zdj(Double.parseDouble(bsh.get("折单价_Vad").toString()));
				
	//			St
				bsv.setT2_ht(bsh.get("合同标准_T2").toString());
				bsv.setT2_yk(Double.parseDouble(bsh.get("盈亏_T2").toString()));
				bsv.setT2_zdj(Double.parseDouble(bsh.get("折单价_T2").toString()));
				
	//			运距
				bsv.setYunju_ht(bsh.get("合同标准_运距").toString());
				bsv.setYunju_yk(Double.parseDouble(bsh.get("盈亏_运距").toString()));
				bsv.setYunju_zdj(Double.parseDouble(bsh.get("折单价_运距").toString()));
			}
//			结算单价
			if(Type==0){
				
				bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
			}else if(Type==1){
				
				if(bsv.getJieslx()==Locale.guotyf_feiylbb_id){
					
				    bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
				}
				
				bsv.setYunfjsdj(Double.parseDouble(bsh.get("结算价格").toString()));
//				如果是非核对货票方式进行运费结算，在为运费单价赋值后直接计算出运费合计
				bsv.setTielyf((double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2));
				bsv.setJiessl(bsv.getYunfjsl());
			}
			
		} catch (EvalError e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	private void computMlj(Interpreter bsh,ResultSetList rsl,Jiesdcz Jscz,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
//		为计算目录价赋值
		try {
			double	Dbljijzb=0;
			
			bsh.set("热值基价_"+Locale.Qnetar_zhibb, 0);
			bsh.set(Locale.Qnetar_zhibb+"_上限", 0);
			bsh.set(Locale.Qnetar_zhibb+"_下限", 0);
			bsh.set("挥发份比价_"+Locale.Vdaf_zhibb, 0);
			bsh.set(Locale.Vdaf_zhibb+"_上限", 0);
			bsh.set(Locale.Vdaf_zhibb+"_下限", 0);
			bsh.set("硫分比价_"+Locale.Std_zhibb, 0);
			bsh.set(Locale.Std_zhibb+"_上限", 0);
			bsh.set(Locale.Std_zhibb+"_下限", 0);
			bsh.set("硫分比价_"+Locale.Stad_zhibb, 0);
			bsh.set(Locale.Stad_zhibb+"_上限", 0);
			bsh.set(Locale.Stad_zhibb+"_下限", 0);
			bsh.set("灰分比价_"+Locale.Aar_zhibb, 0);
			bsh.set(Locale.Aar_zhibb+"_上限", 0);
			bsh.set(Locale.Aar_zhibb+"_下限", 0);
			bsh.set("灰分比价_"+Locale.Aad_zhibb, 0);
			bsh.set(Locale.Aad_zhibb+"_上限", 0);
			bsh.set(Locale.Aad_zhibb+"_下限", 0);
			
			do {
				
				Dbljijzb=Jiesdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
				Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
				
				if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					if(Jiesdcz.CheckMljRz(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))));
						
						bsh.set(rsl.getString("zhib")+"_下限", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));
						
						bsh.set("热值基价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}	
					if(Jiesdcz.CheckMljHff(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_下限", rsl.getDouble("xiax"));
						
						bsh.set("挥发份比价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Jiesdcz.CheckMljLiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_下限", rsl.getDouble("xiax"));
						
						bsh.set("硫分比价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Jiesdcz.CheckMljHiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_下限", rsl.getDouble("xiax"));
						
						bsh.set("灰分比价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					
					bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
					bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
					
					bsv.setHetjgpp_Flag(true);
				}
				
			}while(rsl.next());
			
			bsh.set("品种比价", Jscz.getMljPzbj(bsv.getRanlpzb_Id()));
			
			//	政策性加价
			bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id,Locale.zhengcxjj_jies, "0")));
					
			//	加价
			bsh.set(Locale.jiaj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id, Locale.jiaj_jies, "0")));
			
		}catch(EvalError e){
			
			e.printStackTrace();
		}	
	}
	
//	计算费用加权
	private void computData(){
		//计算煤价,热量折价,硫折价,灰熔点折价
		//煤款
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();
		double _Meiksl=bsv.getMeiksl();
		
		double _Jiashj=0;
		double _Jiakhj=0;
		double _Jiaksk=0;
		double _Jine=0;
		double _Buhsmj=0;
		double _Shulzjbz=0;
		double _Hej=0;
		
		//指标盈亏
		double _Shulyk=bsv.getShul_yk();		//执行合同中的超吨奖励用
		
		//指标折单价
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
		if((bsv.getJieslx()==Locale.meikjs_feiylbb_id||bsv.getJieslx()==Locale.liangpjs_feiylbb_id)
				&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
				&&bsv.getHetyjdw().equals(Locale.yuanmd_danw)		
				){
//			特殊业务情形，如果是单结算煤款、合同价格是到厂价时，先用煤单价-运费单价=煤单价
			_Hansmj=CustomMaths.sub(_Hansmj,bsv.getHetyj());
		}
		
		if(!bsv.getMeikzkksyfw().equals("")
				&&bsv.getMeikzkksyfw()!=null){
//			说明有要部分增扣款的项目,目前只处理数量部分享受加价的业务
//			1、超出部分对应的折单价
//			2、超出部分折金额
			
//			处理逻辑：
//			总金额：	(hansmj-超出部分的折价)*结算数量+超出部分的折价×超出部分
			_Meikzkktzsj=Jiesdcz.Zengkktz(bsv);
		}
		
//		处理分公司加价、和不含税单价计算
		if(bsv.getJijlx()==0){
//					含税单价
//					if(Meikzkktzsj!=null){
////						说明有部分享受加价的情况
////						1、超出部分对应的折单价
////						2、超出部分折金额
////						(hansmj-超出部分的折价)*结算数量+超出部分折金额
//						
//					}
			
				bsv.setJiajqdj(_Hansmj);										//保存加价前单价
				_Hansmj=_Hansmj+bsv.getFengsjj();								//加上分公司加价
				bsv.setHansmj(_Hansmj);											//更新含税单价
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//价税合计
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//价款合计
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//价款税款
				_Jine=_Jiakhj;													//金额
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
			
		}else if(bsv.getJijlx()==1){
//						基价类型（0、含税；1、不含税）
//						不含税
//					bsv.setJiajqdj(_Hansmj);			
				_Buhsmj=_Hansmj;
				_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
//						计算加价前含税单价
				_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setJiajqdj(_Hansmj);
//						计算加价前含税单价_end
				
				_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2)
						+(double)CustomMaths.Round_new(bsv.getFengsjj()*bsv.getJiessl(),2),2);	//处理分公司加价
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);
				_Jine=_Jiakhj;
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);	
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setHansmj(_Hansmj);
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
		}
		
		//不含税单价
		_Shulzjbz=_Hansmj;
		
//		合计
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
//		指标折单价
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
		double _Yunju=bsv.getYunju_zdj();		//运距折单价
		double _Star=bsv.getStar_zdj();			//Star折单价
		
		//计算盈亏，折价金额
		_Qnetarzje=(double)CustomMaths.Round_new(_Qnetar*_Jiessl,2);
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
		_Yunjuzje=(double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2);			//运距折金额
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//超过狂发量的盈亏
		
		//结算单显示时指标折金额项
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
		bsv.setStar_zje(_Starzje);
		bsv.setMad_zje(_Madzje);
		bsv.setAar_zje(_Aarzje);
		bsv.setAad_zje(_Aadzje);
		bsv.setVad_zje(_Vadzje);
		bsv.setT2_zje(_T2);
		bsv.setShul_zje(_Shulzje);
		bsv.setYunju_zje(_Yunjuzje);
		
	}
	
//	计算费用单批次
	private void computData_Dpc(){
		//计算煤价,热量折价,硫折价,灰熔点折价
		//煤款
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();
		double _Meiksl=bsv.getMeiksl();
		//运费
//		double _Tielyf=bsv.getTielyf();
//		double _Tielzf=bsv.getTielzf();
//		double _Yunfsl=bsv.getYunfsl();
//		double _Kuangqyf=bsv.getKuangqyf();
//		double _Kuangqzf=bsv.getKuangqzf();
//		double _Kuangqsk=bsv.getKuangqsk();
//		double _Kuangqjk=bsv.getKuangqjk();
		
		//指标折单价
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
		//指标盈亏
		double _Shulyk=bsv.getShul_yk();								//执行合同中的超吨奖励用
		
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
		
		//指标折金额
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
		
		
		if((bsv.getJieslx()==Locale.meikjs_feiylbb_id||bsv.getJieslx()==Locale.liangpjs_feiylbb_id)
				&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
				&&bsv.getHetyjdw().equals(Locale.yuanmd_danw)
		){
//			特殊业务情形，如果是单结算煤款、合同价格是到厂价时，先用煤单价-运费单价=煤单价
			_Hansmj=CustomMaths.sub(_Hansmj,bsv.getHetyj());
		}
		
//		价格金额计算
//		2008-12-9zsj加：
//		逻辑：	如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//					则
//						情况一：如果运费单价单位=“元/吨”
		
//							煤款含税单价=计算出的煤款含税单价-合同价格中的含税运费单价
//							同时要更新Hansmj
		
//						情况二：如果运费单价单位=“吨”
		
//							煤款结算数量=当前结算数量-含税运费单价（吨）
//							同时要更新Jiessl
		
		if(bsv.getJijlx()==0){
//			含税单价

//		if(Meikzkktzsj!=null){
////			说明有部分享受加价的情况
////			1、超出部分对应的折单价
////			2、超出部分折金额
////			(hansmj-超出部分的折价)*结算数量+超出部分折金额
//			
//		}

			bsv.setJiajqdj(_Hansmj);										//保存加价前单价
			_Hansmj=_Hansmj+bsv.getFengsjj();								//加上分公司加价
			bsv.setHansmj(_Hansmj);											//更新含税单价
			_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//价税合计
			_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//价款合计
			_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//价款税款
			_Jine=_Jiakhj;													//金额
			_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
		
		}else if(bsv.getJijlx()==1){
		//			基价类型（0、含税；1、不含税）
		//			不含税
			_Buhsmj=_Hansmj;
			_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
			//计算加价前含税单价
			_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
			_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
			bsv.setJiajqdj(_Hansmj);
			//计算加价前含税单价_end
			
			_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2)
				+(double)CustomMaths.Round_new(bsv.getFengsjj()*bsv.getJiessl(),2),2);	//处理分公司加价
			_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);
			_Jine=_Jiakhj;
			_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);	
			_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
			bsv.setHansmj(_Hansmj);
			_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
		}

		_Shulzjbz=_Hansmj;
//		 合计
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//计算盈亏，折价金额
		_Qnetarzje=(double)CustomMaths.Round_new(_Qnetar*_Jiessl,2);
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
		_Yunjuzje=(double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2);			//运距折金额
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//超过狂发量的盈亏
		
		//结算单显示时指标折金额项
		
		bsv.setShulzjbz(_Shulzjbz);
		bsv.setShulzjbz(_Shulzje);
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
	}
	
	private void computYunfAndHej(){
		
		//运费
		double _Tielyf=bsv.getTielyf();
		double _Tielzf=bsv.getTielzf();
		double _Yunfsl=bsv.getYunfsl();		//运费税率
		double _Kuangqyf=bsv.getKuangqyf();
		double _Kuangqzf=bsv.getKuangqzf();
		double _Kuangqsk=bsv.getKuangqsk();
		double _Kuangqjk=bsv.getKuangqjk();
		
		double _Yunfsk=0;
		double _Yunzfhj=0;
		double _Buhsyf=0;
		double _Hej=0;
		
		//计算运费项
		_Yunzfhj=(double)CustomMaths.Round_new(_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf,2);								//运杂费合计
		_Yunfsk=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new(_Tielyf*_Yunfsl,2)+_Kuangqsk),2);		//运费税款		
		_Buhsyf=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2)+_Kuangqjk),2);	//不含税运费
		
		if(_Yunzfhj==0&&bsv.getJieslx()!=Locale.meikjs_feiylbb_id){
//			如果运杂费合计为0，且结算类型不等于煤款结算，才执行以下语句
			_Yunzfhj=(double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2);						//运费税款
			_Yunfsk=(double)CustomMaths.Round_new(_Yunzfhj*_Yunfsl,2);											//运费税款
			_Buhsyf=(double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2);											//不含税运费
			_Tielyf=_Yunzfhj;
			bsv.setTielyf(_Tielyf);
		}
		
		//合计
		_Hej=(double)Math.round((_Yunzfhj+bsv.getJiashj())*100)/100;
		
		if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//			如果是到厂价结算，且运杂费合计大于0，执行煤款减运费的操作
			if(bsv.getYunzfhj()>0){
				
				bsv.setJiashj((double)CustomMaths.Round_new(bsv.getJiashj()-_Yunzfhj,2));	//煤款的价税合计-运费的价税合计=煤款的价税合计
				bsv.setJiakhj((double)CustomMaths.Round_new((bsv.getJiashj())/(1+bsv.getMeiksl()),2));
				bsv.setJiaksk((double)CustomMaths.Round_new((bsv.getJiashj()-bsv.getJiakhj()),2));	
				bsv.setJine(bsv.getJiakhj());
				bsv.setHansmj((double)CustomMaths.Round_new(bsv.getJiashj()/bsv.getJiessl(), bsv.getMeikhsdjblxsw()));
				bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
			}
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
	
	public void reCountMj(){
//		重新计算一下煤价
		if(bsv.getHansmj()<100){
//			如果含税煤价比较离谱，我们认为可能是没有质量或质量不全，没法计算准确的价格，先按合同价格计算
			
			if(bsv.getHetmdjdw().equals(Locale.yuanmd_danw)){
				
				bsv.setHansmj(bsv.getHetmdj());
			}else if(bsv.getHetmdjdw().equals(Locale.qiankmqk_danw)
					||bsv.getHetmdjdw().equals(Locale.zhaojmqk_danw)){
				
				bsv.setHansmj(CustomMaths.Round_new(bsv.getHetmdj()*bsv.getChengbzb(),2));
			}
		}
	}
	
	public void getYunshtInfo(long Yunshtb_id){
//		得到运输合同的收款单位，开户银行，帐号
		JDBCcon con = new JDBCcon();
		try {	
			
			String sql = 
				"select yd.quanc,yd.kaihyh,yd.zhangh\n" +
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
			// TODO 自动生成 catch 块
			s.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			
			con.Close();
		}	
	}
}
