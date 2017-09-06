 /*
 * 创建日期 2008-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.dtrlgs.shoumgl.shoumjs;
/**
 * @author zsj
 *运费处理原则：
 *1、当运费单价在煤款合同中时
 * 		两票结算：
 * 			煤款是到厂价：
 * 			
 * 				1、在getMeiPrise中计算出运费总金额
 * 				2、在computData、computData_Dpc算出煤款的含税总金额。
 * 				3、在reCount中用含税煤款-含税运费，赋值给含税煤款，再进行含税单价等指标计算。
 * 					（截止2009-8-3日，系统会认为这时的运价为含税运价）
 * 
 * 			煤款不是到厂价：
 * 				
 * 				1、在getMeiPrise中计算出运费总金额
 * 				2、在computData算出煤款的含税总金额。
 * 				
 * 
 * 		煤款结算：
 * 			煤款是到厂价：
 * 
 * 				1、在getMeiPrise中只得到煤款合同中的运费单价
 * 				2、在computData、computData_Dpc用煤单价-运费单价=煤单价
 * 					（截止2009-8-3日，系统会认为这时的运价为和煤款单价的含税和不含税属性相同）
 * 				3、由于不调用getYunFei方法，所以运费栏为空。
 * 			
 * 			煤款不是到厂价：
 * 			
 * 				1、在getMeiPrise中只得到煤款合同中的运费单价
 * 				2、在computData、computData_Dpc中不用煤单价-运费单价 					
 * 				3、由于不调用getYunFei方法，所以运费栏为空。				
 * 
 * 		运费结算：
 * 				
 * 				1、直接计算出运费getYunFei中实现。 
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
//			两票结算、煤款结算
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieszbsftz,Yansbh,Jieskdl);
			if (bsv.getErroInfo().equals("")){
//				得到供应商、运输方式等基本信息、要求数量和质量一定要审核
				if(Gongysb_id==0){
//					如果在结算选择页面供应商没做选择
					Gongysb_id=bsv.getGongysb_Id();
				}
				
			}else{
				bsv.getErroInfo();
				return bsv;
			}
			
//			得到公式。
//			得到全部公式，（有可能在单结算煤款时，出现到厂价情况，还要计算运费）
			if(!getGongsInfo(Diancxxb_id,"ALL")){
				
				return bsv;
			}
			
//			算煤价,合同中的相关值
			if(getMeiPrice(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,Jieszbsftz,SelIds,
						Gongysb_id,Jieskdl,Yunsdwb_id,Shangcjsl)){
				
			}else{
				
				bsv.getErroInfo();
				return bsv;
			}
			
			
		}else{	//运费结算、包括地铁运费
			getBaseInfo(SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieszbsftz,Yansbh,Jieskdl);
			
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
			
			getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,Yunsdwb_id,Jieslx,Shangcjsl,"");
			
			
//			得到运费公式
			if (getGongsInfo(Diancxxb_id,"YF")) {
				
			}else{
				//return ErroInfo;
				return bsv;
			}
			
//			算运费
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
	
	//得到系统信息，运费税率，煤款税率，公式。
	private boolean getGongsInfo(long _Diancxxb_id,String _Type) throws Exception{
//		参数说明：_diancxx_id，电厂公式
//				 _Type,		  如果为"MK"那就是煤款公式，如果为"YF"那就是运费公式,如果为"ALL"那就是两票结算
//		JDBCcon con =new JDBCcon();
//	    try {
//            
//            //煤款结算公式
//	    	ResultSet rs= con.getResultSet("select id from gongsb where mingc='结算煤价' and leix='结算' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
//            if (rs.next()) {
//            	
//            	DataBassUtil clob=new DataBassUtil();
//            	
//            	bsv.setGongs_Mk(clob.getClob("gongsb", "gongs", rs.getLong(1)));
//            	
//            }else{
//            	bsv.setErroInfo("没有得到煤价公式的系统设置值");
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
			
			str_Gongs_Mk=Shoumjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk);
			
			if(str_Gongs_Mk.equals("")){
				
				bsv.setErroInfo("没有得到煤价公式的系统设置值");
	        	return false;
			}else{
				
				bsv.setGongs_Mk(str_Gongs_Mk);
			}
		}else if(_Type.equals("YF")){
			
			str_Gongs_Yf=Shoumjsdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Yf);
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
	
	public void getYunshtInfo(long Yunshtb_id){
//		得到运输合同的收款单位，开户银行，帐号
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
			// TODO 自动生成 catch 块
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
            //发货日期、到货日期、车数、标重、盈亏、运损、发热量、硫 from fahb
	        //过衡量按列取证，结算量火车煤是按列取整，汽车煤是求和取整
	    	
//	      读取结算设置表中结算设置参数
//      	1、结算数量
//        	2、结算加权数量
//        	3、结算显示指标
//        	4、结算数量保留小数位
//        	5、结算数量取整方式
//        	6、Mt保留小数位
//        	7、Mad保留小数位
//        	8、Aar保留小数位
//        	9、Aad保留小数位
//        	10、Adb保留小数位
//        	11、Vad保留小数位
//        	12、Vdaf保留小数位
//        	13、Stad保留小数位
//        	14、Std保留小数位
//        	15、Had保留小数位
//        	16、Qnetar保留小数位
//        	17、Qbad保留小数位
//        	18、Qgrad保留小数位
//	    	19、T2保留小数位
//        	19、结算指标调整
//	    	20、是否以矿方质量结算
        
//	        String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//结算数量
//	        String jies_Jqsl="jingz";								//结算加权数量
//	        String jies_Jsslblxs="0";								//结算数量保留小数位
//	        String jies_Jieslqzfs="sum(round())";					//结算数量取整方式
//	        String jies_Kdkskzqzfs="round(sum())";					//扣吨、扣水、扣杂取整方式
//	        String jies_yunfjssl="jingz";							//运费结算数量
//	        boolean mbl_yunfjssl=false;								//用来判断用户是否单独设定了运费的结算数量，false没有，就取煤款结算数量
//	        
//	        
//	        jies_Jqsl="f."+MainGlobal.getXitxx_item("结算", Locale.jiaqsl_xitxx, 
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
////        	false表示用户没有单独设置运费结算数量，就取煤款结算数量
//        	jies_yunfjssl=jies_Jssl;
//        }
	    	
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
            								
            		bsv.setFahrq(Shoumjsdcz.FormatDate(bsv.getFahksrq()) +"至"+ Shoumjsdcz.FormatDate(bsv.getFahjzrq()));
            	}
            	
            	bsv.setYansksrq(rs.getDate("mindaohrq"));
            	bsv.setYansjsrq(rs.getDate("maxdaohrq"));

            	if (bsv.getYansksrq().equals(bsv.getYansjsrq())){
            		
            		bsv.setDaohrq(Shoumjsdcz.FormatDate(bsv.getYansksrq()));
            	}else{
            		
            		bsv.setDaohrq(Shoumjsdcz.FormatDate(bsv.getYansksrq())+"至"+Shoumjsdcz.FormatDate(bsv.getYansjsrq()));
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
	          	bsv.setJiesrq(Shoumjsdcz.FormatDate(new Date()));	//结算日期
	          	bsv.setJiesbh(Shoumjsdcz.getJiesbh(String.valueOf(Diancxxb_id),""));
	        	//由lie_id得出车次
	               ResultSetList rsl=con.getResultSetList("select chec from fahb where lie_id in("+SelIds+")");
	               if(rsl.next())
	               bsv.setDaibcc(rsl.getString("chec"));
	               rsl.close();
//            	bsv.setYunju_cf(rs.getDouble("yunju"));		//厂方
//            	bsv.setJihkjb_id(rs.getLong("jihkjb_id"));
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
	
	public Balances_variable getJiesszl(String Jieszbsftz,String SelIds,long Diancxxb_id,long Gongysb_id,
				long Hetb_id,double Jieskdl,long Yunsdwb_id,long Jieslx,double Shangcjsl,String Tsclzb_where){
		
//		结算数、质量
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
			
			rsl=con.getResultSetList(Shoumjsdcz.getJiesszl_Sql(bsv, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, 
					Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, Tsclzb_where).toString());
			
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
            	
            	bsv.setJiessl(rsl.getDouble("jiessl")-Jieskdl);		//结算重量
            	
            	if(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib){
            		
            		bsv.setJiessl(rsl.getDouble("yunfjssl")-Jieskdl);
            	}
				
            	bsv.setYanssl(rsl.getDouble("yanssl"));				//厂方验收数量
            	bsv.setJingz(rsl.getDouble("jingz"));				//净重
            	bsv.setKoud_js(Jieskdl);							//结算扣吨
            	
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
//	            bsv.setStar_cf(rsl.getDouble("Star_cf"));
	            bsv.setHad_cf(rsl.getDouble("Had_cf"));
	            bsv.setQbad_cf(rsl.getDouble("Qbad_cf"));
	            bsv.setQgrad_cf(rsl.getDouble("Qgrad_cf"));
	            bsv.setT2_cf(rsl.getDouble("T2_cf"));
	            
//              矿方指标
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
//                    bsv.setStar_js(rsl.getDouble("Star_js"));
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
//                    bsv.setStar_js(rsl.getDouble("Star"+strcforkf));
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
                if(bsv.getKoud()+bsv.getKous()+bsv.getKouz()+bsv.getKoud_js()>0){

                	bsv.setBeiz(bsv.getBeiz()+" 结算扣吨:"+(bsv.getKoud()+bsv.getKous()+bsv.getKouz()+bsv.getKoud_js()));
                }
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return bsv;
	}
	
	//计算煤价,热量折价,硫折价,灰熔点折价
	private boolean getMeiPrice(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String Jieszbsftz,String SelIds,long Gongysb_id,
									double Jieskdl,long yunsdwb_id,double Shangcjsl){
		//得到合同信息中的运价
			JDBCcon con =new JDBCcon();
			String sql="";
			Interpreter bsh=new Interpreter();
			Shoumjsdcz Jscz=new Shoumjsdcz();
			try{
				
//				数量(合同中以月为单位，每月存一条，暂定先取一条数)
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
				
//				质量(合同中一个合同号对应多个质量记录)
				sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
					+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
					+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
					+ " and htzl.danwb_id=dwb.id	\n"
					+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
					+ " and htb.id="+Hetb_id+" ";
				
//				得到增扣款中需要特殊处理(单批次)的指标
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
//					SelIds为全部结算lie_id
//					特殊指标数组只能赋值一次
					bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items()+Shoumjsdcz.getJieszbtscl(rsl.getString("zhib"), SelIds));
				}
				
				if(!bsv.getJieszbtscl_Items().equals("")
						&&bsv.getTsclzbs()==null){
//					说明有要特殊处理的指标
					String ArrayTsclzbs[]=null;
					ArrayTsclzbs=bsv.getJieszbtscl_Items().split(";");
					bsv.setTsclzbs(ArrayTsclzbs);
//					0,运距,meikxxb_id,100,10,0;
//					1,运距,meikxxb_id,101,15,0;
//					2,运距,meikxxb_id,102,20,0;
//					3,Std,meikxxb_id,100,1.0,0;
				}
				
//				价格（合同中一个合同对应多个基础价格）	
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
					
//					通过合同设置结算值
					setJieshtinfo(rsl,bsh);
					
//					合同基价指标,取出符合条件的合同基价
					if(rsl.getRows()==1){
						
//						就一条合同
//						目录价
						
//						计算运费
						if(Jieslx==Locale.liangpjs_feiylbb_id){
							
							getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
						}
						
						if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								目录价结算
							
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//								单批次结算
								
								String[] test=new String[1];
								
								if(SelIds.indexOf(",")==-1){
									
									test[0]=SelIds;
								}else{
									
									test=SelIds.split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									获得结算数量、质量
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl,"");
									
//									为目录价赋值
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
//									获得增扣款
									getZengkk(Hetb_id,bsh,true,null);
									
//									煤款含税单价保留小数位
									bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
									
//									含税单价取整方式
									bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
									
//									用户自定义公式
									bsh.set(Locale.user_custom_mlj_jiesgs,bsv.getUser_custom_mlj_jiesgs());			
									
//									增扣款保留小数位
									bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
									
//									执行合同价格公式
									ExecuteHetmdjgs(bsh);
									
//									执行公式
									bsh.eval(bsv.getGongs_Mk());
									
//									得到计算后的指标
									setJieszb(bsh,0,Shangcjsl);
									
//									计算煤款金额
									computData_Dpc(SelIds,Hetb_id,Shangcjsl);
								}
								
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						加权平均
								
//								获得结算数量、质量
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
										Jieskdl,yunsdwb_id,Jieslx,Shangcjsl,"");
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh,true,null);
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行合同价格公式
								ExecuteHetmdjgs(bsh);
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0,Shangcjsl);
								
								if(bsv.getTsclzbs()!=null){
//									如果存在需要单独处理的特殊指标
									Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
								}
								
//								计算煤款金额
								computData(SelIds,Hetb_id,Shangcjsl);
							}
							
						}else{		
							
//							一条合同
//							非目录价
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){//							单批次结算
								
								String[] test=new String[1];
								
								if(SelIds.indexOf(",")==-1){
									
									test[0]=SelIds;
								}else{
									
									test=SelIds.split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									获得结算数量、质量
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
											yunsdwb_id,Jieslx,Shangcjsl,"");
									
									double Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
									
//									Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
									
									bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
									bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));								//指标单位
									
									bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
									
									bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
									bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
									
									bsh.set(rsl.getString("zhib")+"增付单价", 	0);
									bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
									bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
									bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
									bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
									bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
									bsh.set(rsl.getString("zhib")+"增付单价", 	0);
									bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
									bsh.set(rsl.getString("zhib")+"小数处理", 	"");
									
//									获得增扣款
									getZengkk(Hetb_id,bsh,true,null);
									
//									用户自定义公式
									bsh.set(Locale.user_custom_fmlj_jiesgs,bsv.getUser_custom_fmlj_jiesgs());
									
//									煤款含税单价保留小数位
									bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
									
//									含税单价取整方式
									bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
									
//									增扣款保留小数位
									bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
									
//									执行合同价格公式
									ExecuteHetmdjgs(bsh);
//									执行公式
									bsh.eval(bsv.getGongs_Mk());
									
//									得到计算后的指标
									setJieszb(bsh,0,Shangcjsl);
									
//									计算煤款金额
									computData_Dpc(SelIds,Hetb_id,Shangcjsl);
								}
								
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
								
//								获得结算数量、质量
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,
										Hetb_id,Jieskdl,yunsdwb_id,Jieslx,Shangcjsl,"");
								
								double Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
								
								bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
								bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
								
								bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
								
								bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
								bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
								
								bsh.set(rsl.getString("zhib")+"增付单价", 	0);
								bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
								bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
								bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
								bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
								bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
								bsh.set(rsl.getString("zhib")+"增付单价", 	0);
								bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
								bsh.set(rsl.getString("zhib")+"小数处理", 	"");
								
//								获得增扣款
								getZengkk(Hetb_id,bsh,true,null);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行合同价格公式
								ExecuteHetmdjgs(bsh);
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0,Shangcjsl);
								
								if(bsv.getTsclzbs()!=null){
//									如果存在需要单独处理的特殊指标
									Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
								}
								
//								计算煤款金额
								computData(SelIds,Hetb_id,Shangcjsl);
							}
						}
						
						bsv.setHetjgpp_Flag(true);
						bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
					}else{
//						有多个合同
//						目录价
						
						if(bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//目录价结算
							
//							计算运费
							if(Jieslx==Locale.liangpjs_feiylbb_id){
								
								getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
							}
						
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){		//单批次结算
							
								String[] test=new String[1];
								
								if(SelIds.indexOf(",")==-1){
									
									test[0]=SelIds;
								}else{
									
									test=SelIds.split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
	//								获得结算数量、质量
									getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
											yunsdwb_id,Jieslx,Shangcjsl,"");
									
	//								为目录价赋值
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
	//								获得增扣款
									getZengkk(Hetb_id,bsh,true,null);
									
//									用户自定义公式
									bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
									
//									煤款含税单价保留小数位
									bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
									
//									含税单价取整方式
									bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
									
//									增扣款保留小数位
									bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
									
//									执行合同价格公式
									ExecuteHetmdjgs(bsh);
									
	//								执行公式
									bsh.eval(bsv.getGongs_Mk());
									
	//								得到计算后的指标
									setJieszb(bsh,0,Shangcjsl);
									
	//								计算煤款金额
									computData_Dpc(SelIds,Hetb_id,Shangcjsl);
								}
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均

								
//								获得结算数量、质量
								getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
										yunsdwb_id,Jieslx,Shangcjsl,"");
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh,true,null);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());
								
//								煤款含税单价保留小数位
								bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
								
//								含税单价取整方式
								bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
								
//								增扣款保留小数位
								bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
								
//								执行合同价格公式
								ExecuteHetmdjgs(bsh);
								
//								执行公式
								bsh.eval(bsv.getGongs_Mk());
								
//								得到计算后的指标
								setJieszb(bsh,0,Shangcjsl);
								
								if(bsv.getTsclzbs()!=null){
//									如果存在需要单独处理的特殊指标
									Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
											Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
								}
								
//								计算煤款金额
								computData(SelIds,Hetb_id,Shangcjsl);
							}
						}else{
							
//							多个合同
//							非目录价
							double	Dbljijzb=0;
							
							if(bsv.getJiesxs().equals(Locale.danpc_jiesxs)){	//单批次结算
								
									String[] test=new String[1];
									
									if(SelIds.indexOf(",")==-1){
										
										test[0]=SelIds;
									}else{
										
										test=SelIds.split(",");
									}
							
									for(int i=0;i<test.length;i++){
										
										
//										获得结算数量、质量
										getJiesszl(Jieszbsftz,test[i],Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
												yunsdwb_id,Jieslx,Shangcjsl,"");
										rsl.beforefirst();
										
										while(rsl.next()){
											
											Dbljijzb=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
											Dbljijzb=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
										
											if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
													&&Shoumjsdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
											){
												
//												设置结算基础数据
												setJieshtinfo(rsl, bsh);
												
												bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
												bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
												
												bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
												
												bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
												bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
												
												bsh.set(rsl.getString("zhib")+"增付单价", 	0);
												bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
												bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
												bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
												bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
												bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
												bsh.set(rsl.getString("zhib")+"增付单价", 	0);
												bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
												bsh.set(rsl.getString("zhib")+"小数处理", 	"");
												
	//											获得增扣款
												getZengkk(Hetb_id,bsh,true,null);
												
	//											用户自定义公式
												bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
												
//												煤款含税单价保留小数位
												bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
												
//												含税单价取整方式
												bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
												
//												增扣款保留小数位
												bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
												
//												执行合同价格公式
												ExecuteHetmdjgs(bsh);
												
	//											执行公式
												bsh.eval(bsv.getGongs_Mk());
												
	//											得到计算后的指标
												setJieszb(bsh,0,Shangcjsl);
												
//												计算运费（注意：只要算一次，要加一个变量判断）
												if(Jieslx==Locale.liangpjs_feiylbb_id&&!bsv.getDanpcysyf()){
//													判断条件：1、是两票结算；2、单批次结算还没有算过运费
													getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
													bsv.setDanpcysyf(true);
												}
												
	//											计算煤款金额
												computData_Dpc(SelIds,Hetb_id,Shangcjsl);
												
												bsv.setHetjgpp_Flag(true);
												bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
											}	
										}
									}		
									
										
							}else if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
								
//									获得结算数量、质量
									getJiesszl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,Jieskdl,
											yunsdwb_id,Jieslx,Shangcjsl,"");
									
									do{
										
										Dbljijzb=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");

										Dbljijzb=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
										
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
												&&Shoumjsdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
											){
											
//											设置结算的合同值
											this.setJieshtinfo(rsl, bsh);
											
											bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//合同价格表id
											
											bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
											bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
											
											bsv.setYifzzb(rsl.getString("zhib"));			//默认的已赋值指标
											
											bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
											bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
											
											bsh.set(rsl.getString("zhib")+"增付单价", 	0);
											bsh.set(rsl.getString("zhib")+"扣付单价", 	0);
											bsh.set(rsl.getString("zhib")+"增付价单位", 	"");
											bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
											bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
											bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
											bsh.set(rsl.getString("zhib")+"增付单价", 	0);
											bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
											bsh.set(rsl.getString("zhib")+"小数处理", 	"");
											
//												获得增扣款
											getZengkk(Hetb_id,bsh,true,null);
											
//												用户自定义公式
											bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());
											
//											煤款含税单价保留小数位
											bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
											
//											含税单价取整方式
											bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
											
//											增扣款保留小数位
											bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
											
//											执行合同价格公式
											ExecuteHetmdjgs(bsh);
											
//											执行公式
											bsh.eval(bsv.getGongs_Mk());
											
//												得到计算后的指标
											setJieszb(bsh,0,Shangcjsl);
											
//											计算运费（注意：只要算一次）
											if(Jieslx==Locale.liangpjs_feiylbb_id){
//												判断条件：是两票结算
												getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
											}
											
											if(bsv.getTsclzbs()!=null){
//												如果存在需要单独处理的特殊指标
												Jiestszbcl(Jieszbsftz,SelIds,Diancxxb_id,Gongysb_id,Hetb_id,
														Jieskdl,yunsdwb_id,Jieslx,Shangcjsl);
											}
											
//												计算煤款金额
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
	
	private void setJieshtinfo(ResultSetList rsl, Interpreter bsh) {
//		函数功能：
		
//				当结算时，要将合同信息给bsh赋值，以便后面结算用
//		函数逻辑：
		
//				将rsl的值取出赋值给bsh
//		函数形参：rsl 合同的数据集，bsh 结算自动计算的容器
		
		bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
		bsv.setJijlx(rsl.getInt("jijlx"));				//基价类型（0、含税；1、不含税）
		bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
		bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
		bsv.setHetmdjgs(Shoumjsdcz.getTransform_Hetjgs(
				rsl.getString("jijgs").trim(),
				rsl.getString("danw"),
				rsl.getDouble("jij"),
				bsv.getDiancxxb_id()));		//合同单价公式
		bsv.setJiesfs(rsl.getString("jiesfs"));			//结算方式（到厂价格、出矿价格）
		bsv.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
		bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
		bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
		bsv.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
		bsv.setFengsjj(rsl.getDouble("fengsjj")); 		//合同价格中的分公司加价（统一结算用）
//															分公司加价处理逻辑：
//																1、根据合同价格类型（含税、不含税）算出原始结算价（可能是含税也可能是不含税），
//																	用变量保存,并将分公司加价进行保存。
//																2、如果是含税价，结算单价=结算单价+分公司加价；
//																		如果是不含税价，结算单价=最后计算出的含税价+分公司加价
//		
		bsv.setJiagpzId(rsl.getString("pinzb_id"));			//价格里的品种，为了区分一个合同不同品种不同价格的情况
		try {
			bsh.set("结算形式", bsv.getJiesxs());
			bsh.set("计价方式", bsv.getHetjjfs());
			bsh.set("价格单位", bsv.getHetmdjdw());	
			bsh.set("合同价格", bsv.getHetmdj());
			bsh.set("最高煤价", bsv.getZuigmj());
			bsh.set("合同价格公式", bsv.getHetmdjgs());
			
		} catch (EvalError e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void ExecuteHetmdjgs(Interpreter bsh){
//		执行合同价格公式
//		函数功能：
		
//			在执行结算公式前，如果合同价格中存在价格公式，要先执行价格公式，将该值赋值给“合同价格”。
//		函数逻辑：

//			执行合同价格的公式。
//		函数形参：bsh 结算自动计算的容器
		
		if(!bsv.getHetmdjgs().equals("")){
			
			try {
//				bsh.set("厂方验收热量",10);
				bsh.eval(bsv.getHetmdjgs());
				bsv.setHetmdj(Double.parseDouble(bsh.get("合同价格").toString()));		//合同价格
				bsh.set("合同价格", bsv.getHetmdj());
			} catch (EvalError e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}

	private boolean getZengkk(long Hetb_id,Interpreter bsh,boolean Falg,String[] Tsclzb_item){
//		增扣款
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
			double Dbltmp=0; 		//记录指标结算值
			double Dblczxm=0;		//记录参照项目的值
			String Strtmp="";		//记录设定的指标
			String Strimplementedzb="";	//记录已经执行过的指标（即已经参与过执行的指标）。
			double Dblimplementedzbsx=0;	//记录已执行过的指标的上限
			StringBuffer sb = new StringBuffer();	//记录合同增扣款中的适用范围为1的记录
			
			if(Falg){
//				Falg=true 说明是正常增扣款计算，此时如果指标在特殊处理数组中 应不计算
				while(rsl.next()){
					
					Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
//					得到参照项目的结算标准
					Dblczxm=Shoumjsdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");
					
//					指标的结算指标
					Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
					Dblczxm=Shoumjsdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
					
					if(Dbltmp>=Shoumjsdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
							&&Shoumjsdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
							&&Shoumjsdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"))	//如果该指标需要特殊（单批次）处理，将不参加统一的增扣款的计算
					){
						
						//指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
						Strimplementedzb = rsl.getString("zhib");	//记录已使用的指标
						Dblimplementedzbsx = rsl.getDouble("shangx");
						
						bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
						bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));			//指标单位（上下限单位）
						bsh.set(rsl.getString("zhib")+"增扣款条件", 	rsl.getString("tiaoj"));		//大于等于、大于、小于、小于等于、	区间、等于
						bsh.set(rsl.getString("zhib")+"增付单价",	rsl.getDouble("zengfj"));		//增付价
						bsh.set(rsl.getString("zhib")+"扣付单价",	rsl.getDouble("kouj"));			//扣价
						bsh.set(rsl.getString("zhib")+"增付价单位", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//增价单位
						bsh.set(rsl.getString("zhib")+"扣付价单位", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//扣价单位
						bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
						bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
						bsh.set(rsl.getString("zhib")+"增扣款基数",	rsl.getDouble("jis"));			//基数（每升高xx或降低xx）
						bsh.set(rsl.getString("zhib")+"增扣款基数单位",	rsl.getString("jisdw"));	//基数单位
						bsh.set(rsl.getString("zhib")+"基准增扣价",	rsl.getDouble("jizzkj"));		//基准增扣价（用于多段增扣价累计时使用）
						bsh.set(rsl.getString("zhib")+"小数处理",	Shoumjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//小数处理（每升高xx或降低xx）
						
						Strtmp+="'"+rsl.getString("zhib")+"',";					//记录用户设置的影响结算单价的指标
//						处理曾扣款适用范围方法：
//							原理：先将增扣款的适用信息记录到一个StringBuffer变量中，形式为:Qnetar,1;结算数量,1;
//							(注：如果shiyfw为1认为是超出部分适用，才记录，如果是0则是适用于全部数据，不用记录) 
//							使用时解析这个StringBufffer
						if(rsl.getInt("shiyfw")>0){
							
//							适用范围为1 说明只对超出部分进行操作
							sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
						}
					}
				}
			}else{
//				Falg=false 说明是处理特殊增扣款计算，此时如果指标不在特殊处理数组中 应不计算
				while(rsl.next()){

					if(Tsclzb_item[1].equals(rsl.getString("zhib"))){

//						得到增扣款指标值
						Dbltmp=Double.parseDouble(Tsclzb_item[Tsclzb_item.length-2]);
					
//						给重新计算增扣款的特殊处理的指标数组打标志
						Shoumjsdcz.Mark_Tsclzbs_bz(bsv.getTsclzbs(),Tsclzb_item[0]+","+rsl.getString("zhib"));
					
//						得到参照项目的结算标准(只适用于加权平均的参照项目)
						Dblczxm=Shoumjsdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");
					
//						指标的结算指标
						Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
						Dblczxm=Shoumjsdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
					
						if(Dbltmp>=Shoumjsdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
								&&Shoumjsdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
								&&!Shoumjsdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"))	//如果该指标需要特殊（单批次）处理，将不参加统一的增扣款的计算
						){
							
							//指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
							Strimplementedzb = rsl.getString("zhib");	//记录已使用的指标
							Dblimplementedzbsx = rsl.getDouble("shangx");
							
							bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
							bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));			//指标单位（上下限单位）
							bsh.set(rsl.getString("zhib")+"增扣款条件", 	rsl.getString("tiaoj"));		//大于等于、大于、小于、小于等于、	区间、等于
							bsh.set(rsl.getString("zhib")+"增付单价",	rsl.getDouble("zengfj"));		//增付价
							bsh.set(rsl.getString("zhib")+"扣付单价",	rsl.getDouble("kouj"));			//扣价
							bsh.set(rsl.getString("zhib")+"增付价单位", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//增价单位
							bsh.set(rsl.getString("zhib")+"扣付价单位", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//扣价单位
							bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
							bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
							bsh.set(rsl.getString("zhib")+"增扣款基数",	rsl.getDouble("jis"));			//基数（每升高xx或降低xx）
							bsh.set(rsl.getString("zhib")+"增扣款基数单位",	rsl.getString("jisdw"));	//基数单位
							bsh.set(rsl.getString("zhib")+"基准增扣价",	rsl.getDouble("jizzkj"));		//基准增扣价（用于多段增扣价累计时使用）
							bsh.set(rsl.getString("zhib")+"小数处理",	Shoumjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//小数处理（每升高xx或降低xx）
							
							Strtmp+="'"+rsl.getString("zhib")+"',";					//记录用户设置的影响结算单价的指标
							
//							认为这个指标值应进行增扣款计算
							bsv.setTsclzbzkksfxyjs(true);
							
//							处理曾扣款适用范围方法：
//								原理：先将增扣款的适用信息记录到一个StringBuffer变量中，形式为:Qnetar,1;结算数量,1;
//								(注：如果shiyfw为1认为是超出部分适用，才记录，如果是0则是适用于全部数据，不用记录) 
//								使用时解析这个StringBufffer
							
							if(rsl.getInt("shiyfw")>0){
								
//								适用范围为1 说明只对超出部分进行操作
								sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
							}
						}
					}
				}
			}
			
			bsv.setMeikzkksyfw(sb);	//记录煤款增扣款有适用范围为超出部分的数据
			
//			if(Strtmp.equals("")){
//				
//				if(!bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){
//					
//					bsv.setErroInfo("系统中没有符合要求的增扣款项。");
//					return false;
//				}
//				
//			}
				
//			取出zhibb中没有在hetzkkb中体现的项目，目的是也要放到公式中去计算
			
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
//					又多个运费合同
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
//		计算运费
//		思路：
//			情况1：煤款合同中有运价（bsv.getHetyj()>0），运价单位有两种（元/吨，元/吨*公里）,
//					没有曾扣款情况.因为没法得到运费合同，所以不能算增扣款
//			情况2：是Hetyj、Hetyjdw无值,要从hetysjgb中取值，有增扣款
		try{
			
			Interpreter bsh=new Interpreter();
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
					
					getZengkk_yf(Hetb_id,bsh);	//不起任何作用只是公式上部报错
//					算运费
					bsh.eval(bsv.getGongs_Yf());
//						bsv.setYunfjsdj(Double.parseDouble(bsh.get("运费结算单价").toString()));
					setJieszb(bsh,1,Shangcjsl);
						
				}else{
					
					bsv.setErroInfo("请设置运费计算公式");
					return;
				}
			}else{
				
//				取hetys中的运价
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
//					检查是否有结算价格方案，如果有说明是特殊方式结算，不走原来的合同增扣款形式
					if(rsl.getLong("yunsjgfab_id")>0){
//						处理运费结算方案的特殊算法
						bsh.set("合同运价", 0);
						bsh.set("合同运价单位", Locale.yuanmd_danw);
						bsh.set("运价里程", 0);
						
//						解析运费价格明细中的数据
						bsh=Shoumjsdcz.CountYfjsfa(bsv.getMeikxxb_Id(),bsv.getDiancxxb_id(),bsv.getFaz_Id(),
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
						setJieszb(bsh,1,Shangcjsl);
						
					}else{
//						没有结算价格方案，按原来的价格条款和增扣款条款进行结算
						if(rsl.getRows()==1){
//							就一条合同价格
							bsh.set("合同运价", rsl.getDouble("yunja"));
							bsh.set("合同运价单位", rsl.getString("yunjdw"));
							bsh.set("运价里程", bsv.getYunju_js());
							
							if(rsl.getDouble("yunja")==0){
								
								bsv.setErroInfo("合同运价为0，请检查合同！");
								return;
							}
//							获得增扣款
							getZengkk_yf(Hetb_id,bsh);
							
//							运费含税单价保留小数位
							bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
							
							bsh.eval(bsv.getGongs_Yf());
							setJieszb(bsh,1,Shangcjsl);
							
						}else{
//							多个合同价格
							double shangx=0;
							double xiax=0;
							double yunju=bsv.getYunju_cf();	//运距
							double Dbltmp=0;
							
							do{
								shangx=rsl.getDouble("shangx");
								xiax=rsl.getDouble("xiax");
								
								Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
								
								Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
								
								if(yunju>=xiax&&yunju<=(shangx==0?1e308:shangx)){
									
									bsh.set("合同运价", rsl.getDouble("yunja"));
									bsh.set("合同运价单位", rsl.getString("yunjdw"));
									bsh.set("运价里程", bsv.getYunju_js());
									
									if(rsl.getDouble("yunja")==0){
										
										bsv.setErroInfo("合同运价为0，请检查合同！");
										return;
									}
									
									getZengkk_yf(Hetb_id,bsh);
									
//									运费含税单价保留小数位
									bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
									
									bsh.eval(bsv.getGongs_Yf());
									setJieszb(bsh,1,Shangcjsl);
								}
								
							}while(rsl.next());
						}
					}
				}else{
					
					bsv.setErroInfo("没有得到运费合同价格！");
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
		
//		运费的增扣款原则：如果运费价格里用到了该指标，还可以在增扣款中累计计算
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
			double Dbltmp=0; 		//记录指标结算值
			String  Strtmp="";		//记录设定的指标
			double Dblczxm=0;		//记录参照项目的值
			String Strimplementedzb="";	//记录已经执行过的指标（即已经参与过执行的指标）。
			double Dblimplementedzbsx=0;	//记录已执行过的指标的上限
			
			while(rsl.next()){
				
				Dbltmp=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
				Dblczxm=Shoumjsdcz.getZhib_info(bsv,rsl.getString("canzxm"),"js");
//				指标的结算指标
				Dbltmp=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp,bsv.getMj_to_kcal_xsclfs());
				Dblczxm=Shoumjsdcz.getUnit_transform(rsl.getString("canzxm"),rsl.getString("canzxmdw"),Dblczxm,bsv.getMj_to_kcal_xsclfs());
				
				if(Dbltmp>=Shoumjsdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))&&Dbltmp<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
						&&Shoumjsdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
				
				){
					
					Strimplementedzb = rsl.getString("zhib");
					Dblimplementedzbsx = rsl.getDouble("shangx");
					
					bsh.set(rsl.getString("zhib")+Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
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
					bsh.set(rsl.getString("zhib")+"小数处理",	Shoumjsdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//小数处理（每升高xx或降低xx）
					
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
				
					
					Strtmpdw=Shoumjsdcz.getZhibbdw(rsl.getString("zhib"),"");
					
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
	
	//计算运费，矿区杂费，计税扣除，地铁运费
	private boolean getYunFei(String SelIds,long Jieslx,long Hetb_id,double Shangcjsl){
	    JDBCcon con=new JDBCcon();
		try{
		    	
		    	String sql="";
		    	ResultSet rs=null;
		    	String sql_colum="";	//附加列（矿区运费用）
		    	String sql_talbe="";	//附加表（矿区运费用）
		    	long lngJieslx=0;
		    	long lngYunshtb_id=0;
		    	lngJieslx=Jieslx;
		    	
		    	if(lngJieslx==Locale.liangpjs_feiylbb_id||lngJieslx==Locale.guotyf_feiylbb_id){
		    		
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
//				        两票结算（铁路，从yunfdjb,danjcpb中取值，前提是要先进行货票核对）
//				    	yunf铁路大票上所有的费用
//				    	yunfzf铁路大票上所有不可抵税的费用
//				    	两票结算或国铁运费时用
				    	bsv.setTielyf(rs.getDouble("tielyf"));
				    	bsv.setTielzf(rs.getDouble("tielzf"));
				    	
				    	if(lngJieslx==Locale.liangpjs_feiylbb_id
				    			||lngJieslx==Locale.guotyf_feiylbb_id){
				    		
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
				    	
//				    	bsv.setYunfjsl(bsv.getGongfsl());
				    	if(Jieslx==Locale.liangpjs_feiylbb_id){
//				    		两票结算，情况1
				    		
				    		if(bsv.getHetyj()>0&&!bsv.getHetyjdw().equals("")){
//			    				如果煤款合同中有运费则计算
				    			
			    				CountYf(0,Shangcjsl);
			    			}else{
//			    				如果煤款合同中没有运费，从煤款运费合同关联表中取出运费合同id
			    				lngYunshtb_id=getYunshtb_id(Hetb_id);
			    				
			    				if(lngYunshtb_id>0){
//			    					说明有已找到相应的运费合同了
			    					CountYf(lngYunshtb_id,Shangcjsl);
			    				}else if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//			    					到厂价结算,如果是两票结算运费，而又是到厂价合同的话，不计算运费
			    					
			    				}else{
//			    					出矿价结算
			    					bsv.setErroInfo("煤款合同<"+Shoumjsdcz.getHetbh(bsv.getHetb_Id())+">没有对应的运费合同，请设置！");
			    					return false;
			    				}
			    			}
				    	}else if(Jieslx!=Locale.meikjs_feiylbb_id){
//				    		国铁运费结算(单独结算运费)
				    		CountYf(bsv.getHetb_Id(),Shangcjsl);
				    	}
				    }else{
//				    	从yunfdjb,danjcpb中取到数据，说明是通过核对货票生成的运费，运费结算量为biaoz
//				    	如果是单结算的运费，数据来源也是yunfdjb,danjcpb,那么认为结算数量也为biaoz
				    	bsv.setYunfjsl(bsv.getGongfsl());
				    	if(lngJieslx==Locale.guotyf_feiylbb_id){
				    		
				    		bsv.setJiessl(bsv.getGongfsl());
				    	}
				    }
				    
////		    		一口价运费源于煤款
//		    		if(bsv.getYikj_yunfyymk().equals("是")){
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
////		    				已经有合同价格表
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
//		此方法在Jiesdcz.java中还有一个类似的方法，如果修改此方法，要连同那个方法一起修改
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
			//结算单价
			if(Type==0){
				
				bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
			}else if(Type==1){
				
				if(bsv.getJieslx()==Locale.guotyf_feiylbb_id){
					
				    bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
				    bsv.setJiessl(bsv.getYunfjsl());
				}
				
				bsv.setYunfjsdj(Double.parseDouble(bsh.get("结算价格").toString()));
//				如果是非核对货票方式进行运费结算，在为运费单价赋值后直接计算出运费合计
				bsv.setTielyf((double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2));
				
			}
			if(!bsv.getShangcjslct_Flag()){
				
				bsv.setJiessl(CustomMaths.sub(bsv.getJiessl(), Shangcjsl)); //算完数量折价后将上次结算量删除，即为本次结算量
				bsv.setShangcjslct_Flag(true);
			}
			
		} catch (EvalError e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void computMlj(Interpreter bsh,ResultSetList rsl,Shoumjsdcz Jscz,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
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
				
				Dbljijzb=Shoumjsdcz.getZhib_info(bsv,rsl.getString("zhib"),"js");
				Dbljijzb=Shoumjsdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb,bsv.getMj_to_kcal_xsclfs());
				
				if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))){
					
					if(Shoumjsdcz.CheckMljRz(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", Shoumjsdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))));
						
						bsh.set(rsl.getString("zhib")+"_下限", Shoumjsdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));
						
						bsh.set("热值基价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}	
					if(Shoumjsdcz.CheckMljHff(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_下限", rsl.getDouble("xiax"));
						
						bsh.set("挥发份比价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Shoumjsdcz.CheckMljLiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_下限", rsl.getDouble("xiax"));
						
						bsh.set("硫分比价_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Shoumjsdcz.CheckMljHiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_上限", (rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx")));
						
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
			bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Shoumjsdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id,Locale.zhengcxjj_jies, "0")));
					
			//	加价
			bsh.set(Locale.jiaj_jies, Double.parseDouble(Shoumjsdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id,Locale.jiaj_jies, "0")));
			
		}catch(EvalError e){
			
			e.printStackTrace();
		}	
	}
	
	//计算费用加权
	private void computData(String selIds, long hetb_id, double shangcjsl){
		//计算煤价,热量折价,硫折价,灰熔点折价
		//煤款
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();	//煤款结算数量
		double _Meiksl=bsv.getMeiksl();
		
		//运费
//		double _Tielyf=bsv.getTielyf();
//		double _Tielzf=bsv.getTielzf();
//		double _Yunfsl=bsv.getYunfsl();
//		double _Kuangqyf=bsv.getKuangqyf();
//		double _Kuangqzf=bsv.getKuangqzf();
//		double _Kuangqsk=bsv.getKuangqsk();
//		double _Kuangqjk=bsv.getKuangqjk();
		
		//指标盈亏
		double _Shulyk=bsv.getShul_yk();		//执行合同中的超吨奖励用
		
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
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		double _Yunjuzje=0;
		double _Starzje=0;
		
		double _Meikzkktzsj[]=null;
		boolean _Iszksjtz=false; //判断是否已经进行了增扣款的数据调整了
		
		Danjsmk_dcjcl(1,selIds,hetb_id,shangcjsl);
		
		if(!bsv.getMeikzkksyfw().equals("")
				&&bsv.getMeikzkksyfw()!=null){
//			说明有要部分增扣款的项目,目前只处理数量部分享受加价的业务
//			1、超出部分对应的折单价
//			2、超出部分折金额
			
//			处理逻辑：
//			总金额：	(hansmj-超出部分的折价)*结算数量+超出部分的折价×超出部分
			_Meikzkktzsj=Shoumjsdcz.Zengkktz(bsv);
		}
		//价格金额计算
//		2008-12-9zsj加：
//		逻辑：	如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//					则我们认为是一下两种情况的一种：
//						情况一：
		
//							含税总煤款=到厂价×煤款结算数量-运费含税单价×运费结算数量
//							含税总运费=运费含税单价×运费结算数量
//							处理方法：在结算设置里增加“一口价(运费源于煤款)”的设置，默认值“否”，如果值为“是”
//									则按照此情况处理。
//						情况二：
		
//							煤款含税单价=计算出的煤款含税单价-合同价格中的含税运费单价
//							含税运费单价=合同价格中的含税运费单价
//							同时要更新Hansmj
//							处理方法：系统默认，即如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//									且结算设置中“一口价(运费源于煤款)”值为“否”，则按照此情况处理。
		
//						
		//			如果合同价格的结算方式是“到厂价格”，且合同价格中运费含税单价大于0，且运费价格单位不等于“”
					
//					if(bsv.getHetyjdw().equals(Locale.yuanmd_danw)){
//		//				如果运费单价单位=“元/吨”
//		//					保留小数的处理方法：根据结算设置"煤款含税单价保留小数位"
////						原则：
//		//					1、关于煤款是到厂价运费还有数的问题，如果煤价是不含税的，操作起来有问题，故先不予考虑，视为运费和煤款属于同种单价类型
////							2、关于分公司加价问题，认为加价都为含税价。
////								如果煤款为含税价，含税单价=含税单价+分公司加价
////								如果煤款为不含税价，含税单价=不含税单价×（1+税率）+分公司加价
//						
////						增扣款调整，放到下午处理
////						if(_Meikzkktzsj!=null){
////							
//////							1、超出部分对应的折单价
//////							2、超出部分折金额
//////							(hansmj-超出部分的折价)*结算数量+超出部分折金额
////							_Jiashj=(_Hansmj-_Meikzkktzsj[0])*_Jiessl+_Meikzkktzsj[1];
////							if(bsv.getJijlx()==0){
//////								含税单价
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,bsv.getMeikhsdjblxsw());
////							}else if(bsv.getJijlx()==1){
//////								不含税
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,7);
////							}
////						}
//						
//						_Hansmj=(double)CustomMaths.Round_new(_Hansmj-bsv.getHetyj(), bsv.getMeikhsdjblxsw());
//					}
		//			else if(bsv.getHetyjdw().equals(Locale.dun_danw)){
		////				如果运费单价单位=“吨”
		////				保留小数的处理方法：根据结算设置"结算数量保留小数位"
		//				_Jiessl=(double)CustomMaths.Round_new(_Jiessl-bsv.getHetyj(), Integer.parseInt(bsv.getJiesslblxs()));
		//				bsv.setJiessl(_Jiessl);
		//			}
		
//		计算指标特殊处理实现逻辑
		
		double jieszjecj=0;	//结算总金额差价（特殊指标增扣款折金额计算完成后要在jieszje上要增减的钱，
//								jieszje有可能是含税，也有可能是不含税的，根据Jijlx决定）
		
		if(bsv.getTsclzbs()!=null){
//			这时的Tsclzbs数组里存的是指标的折价信息
//			数组中元素排列：指标编码,指标折单价,折价数量,折金额
			String tmp[]=null;
			String zhibbm="";
			double zhibzje=0;
			double zhibzdj=0;		
			double zhibjsbz=0;		//特殊指标的结算标准(单批次结算指标的值)
			double zhibjsbzdysl=0;	//特殊指标结算标准对应的数量
			double zhibjsbzjqz=0;	//特殊指标结算标准的加权值（特殊指标的结算标准×特殊指标结算标准对应的数量）
			
			for(int i=0;i<bsv.getTsclzbs().length;i++){
				
				tmp = bsv.getTsclzbs()[i].split(",");
				
				if(zhibbm.equals(tmp[0])){
//						同一个指标
//						重新计算折金额
					zhibzje = CustomMaths.add(zhibzje,Double.parseDouble(tmp[tmp.length-1]));
//						重新计算折单价
					zhibzdj = CustomMaths.Round_new(zhibzje/bsv.getJiessl(),bsv.getMeikzkkblxsw());
//					
//						单批次结算指标的值
					zhibjsbz = Double.parseDouble(tmp[1]);
//						结算标准对应的数量
					zhibjsbzdysl = CustomMaths.add(zhibjsbzdysl,Double.parseDouble(tmp[3]));
//						累加结算标准的加权值
					zhibjsbzjqz = CustomMaths.add(zhibjsbzjqz, CustomMaths.mul(zhibjsbz, Double.parseDouble(tmp[3])));
//						给指标赋值
					Shoumjsdcz.setJieszbzdj_Tszb(zhibbm,bsv,zhibzdj,CustomMaths.Round_new(CustomMaths.div(zhibjsbzjqz, zhibjsbzdysl),bsv.getMeikzkkblxsw()),zhibzje);
					
				}else{
//						跟上一次不是同一个指标
					zhibbm = tmp[0];
//						算出折金额
					zhibzje = Double.parseDouble(tmp[tmp.length-1]);
//						算出折单价
					zhibzdj = Double.parseDouble(tmp[2]);
//						单批次结算指标的值
					zhibjsbz = Double.parseDouble(tmp[1]);
//						结算标准对应的数量
					zhibjsbzdysl = Double.parseDouble(tmp[3]);
//						结算标准的加权值
					zhibjsbzjqz = CustomMaths.mul(zhibjsbz, zhibjsbzdysl);
//						给指标赋值
					Shoumjsdcz.setJieszbzdj_Tszb(zhibbm,bsv,zhibzdj,zhibjsbz,zhibzje);
				}
//				将特殊增扣款的折价金额记录下来
				jieszjecj = zhibzje;
			}
		}
	
//			处理分公司加价、和不含税单价计算
		if(bsv.getJijlx()==0){
//					含税单价
//					if(Meikzkktzsj!=null){
////						说明有部分享受加价的情况
////						1、超出部分对应的折单价
////						2、超出部分折金额
////						(hansmj-超出部分的折价)*结算数量+超出部分折金额
//						
//					}
			
			if(jieszjecj==0){
//				没有特殊指标单独处理
				
				bsv.setJiajqdj(_Hansmj);										//保存加价前单价
				_Hansmj=_Hansmj+bsv.getFengsjj();								//加上分公司加价
				bsv.setHansmj(_Hansmj);											//更新含税单价
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//价税合计
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//价款合计
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//价款税款
				_Jine=_Jiakhj;													//金额
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
			}else{
//				有特殊指标单独处理
				
				_Jiashj = (double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//价税合计
				_Jiashj = (double)CustomMaths.Round_new(CustomMaths.add(_Jiashj,jieszjecj),2);			//计算不含分公司加价的价税合计
				_Hansmj = (double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());	//反推加钱前含税单价
				bsv.setJiajqdj(_Hansmj);
				_Jiashj = (double)CustomMaths.Round_new(_Jiashj
							+(double)CustomMaths.Round_new(CustomMaths.mul(bsv.getFengsjj(), _Jiessl),2),2);									//j结算特殊指标增扣款后价税合计
				_Hansmj= (double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());	//反推含税单价
				bsv.setHansmj(_Hansmj);											//更新含税单价
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//价款合计
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//价款税款
				_Jine=_Jiakhj;													//金额
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
			}
			
			
		}else if(bsv.getJijlx()==1){
//						基价类型（0、含税；1、不含税）
//						不含税
//					bsv.setJiajqdj(_Hansmj);			
			
			if(jieszjecj==0){
//				没有特殊指标单独处理
				
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
			}else{
//				有特殊指标单独处理
				
				_Buhsmj=_Hansmj;
				_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
				_Jiakhj=CustomMaths.add(_Jiakhj,jieszjecj);										//计算特殊处理的指标折金额
				_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setJiajqdj(_Hansmj);	//计算加价前含税单价_end
				
				_Jiashj=(double)CustomMaths.Round_new((double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2)
						+(double)CustomMaths.Round_new(bsv.getFengsjj()*bsv.getJiessl(),2),2);	//处理分公司加价
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);
				_Jine=_Jiakhj;
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);	
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setHansmj(_Hansmj);
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
			}
		}
				
		_Shulzjbz=_Hansmj;
		//合计
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
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
		double _Mad=bsv.getMad_zdj();
		double _Aar=bsv.getAar_zdj();
		double _Aad=bsv.getAad_zdj();
		double _Vad=bsv.getVad_zdj();
		double _T2=bsv.getT2_zdj();
		double _Shul=bsv.getShul_zdj();
		double _Yunju=bsv.getYunju_zdj();		//运距折单价
		double _Star=bsv.getStar_zdj();			//Star折单价
		
		//计算盈亏，折价金额
			
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
			
		
			_Yunjuzje=CustomMaths.add((double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2),bsv.getYunju_zje_tscl());			//运距折金额
			bsv.setYunju_zje(_Yunjuzje);
			bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl),bsv.getMeikzkkblxsw()));
			
		
			_Shulzje=CustomMaths.add((double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2),bsv.getShul_zje_tscl());	//超过狂发量的盈亏
			bsv.setShul_zje(_Shulzje);
			bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		
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
	
//	计算费用单批次
	private void computData_Dpc(String selIds, long hetb_id, double shangcjsl){
		//计算煤价,热量折价,硫折价,灰熔点折价
		//煤款
		JDBCcon con=new JDBCcon();
		StringBuffer sql=new StringBuffer("begin 	\n");
		
		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();
		double _Meiksl=bsv.getMeiksl();
		
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
		
		Danjsmk_dcjcl(1,selIds,hetb_id,shangcjsl);
		
		//价格金额计算
//		2008-12-9zsj加：
//		逻辑：	如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//		则我们认为是一下两种情况的一种：
//			情况一：

//				含税总煤款=到厂价×煤款结算数量-运费含税单价×运费结算数量
//				含税总运费=运费含税单价×运费结算数量
//				处理方法：在结算设置里增加“一口价(运费源于煤款)”的设置，默认值“否”，如果值为“是”
//						则按照此情况处理。
//			情况二：

//				煤款含税单价=计算出的煤款含税单价-合同价格中的含税运费单价
//				含税运费单价=合同价格中的含税运费单价
//				同时要更新Hansmj
//				处理方法：系统默认，即如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//						且结算设置中“一口价(运费源于煤款)”值为“否”，则按照此情况处理。
		
						
		
			if(bsv.getJijlx()==0){
//								含税单价
				
//							if(Meikzkktzsj!=null){
////								说明有部分享受加价的情况
////								1、超出部分对应的折单价
////								2、超出部分折金额
////								(hansmj-超出部分的折价)*结算数量+超出部分折金额
//								
//							}
				
				bsv.setJiajqdj(_Hansmj);										//保存加价前单价
				_Hansmj=_Hansmj+bsv.getFengsjj();								//加上分公司加价
				bsv.setHansmj(_Hansmj);											//更新含税单价
				_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);		//价税合计
				_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);	//价款合计
				_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);		//价款税款
				_Jine=_Jiakhj;													//金额
				_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
			
			}else if(bsv.getJijlx()==1){
//								基价类型（0、含税；1、不含税）
//								不含税
				_Buhsmj=_Hansmj;
				_Jiakhj=(double)CustomMaths.Round_new(_Buhsmj*_Jiessl,2);
//				计算加价前含税单价
				_Jiashj=(double)CustomMaths.Round_new(_Jiakhj*(1+_Meiksl),2);
				_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl, bsv.getMeikhsdjblxsw());
				bsv.setJiajqdj(_Hansmj);
//				计算加价前含税单价_end
				
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
		//合计
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//计算盈亏，折价金额
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
		_Yunjuzje=(double)CustomMaths.Round_new(_Yunju*_Jiessl,2);	//运距折金额
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//超过狂发量的盈亏
		
		//结算单显示时指标折金额项
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
		//计算运费结算后的折单价

		double _Hansmj=bsv.getHansmj();
		double _Jiessl=bsv.getJiessl();	//煤款结算数量
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
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		double _Yunjuzje=0;
		double _Starzje=0;
		double _Shulzjbz=0;
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
		double _Mad=bsv.getMad_zdj();
		double _Aar=bsv.getAar_zdj();
		double _Aad=bsv.getAad_zdj();
		double _Vad=bsv.getVad_zdj();
		double _T2=bsv.getT2_zdj();
		double _Shul=bsv.getShul_zdj();
		double _Yunju=bsv.getYunju_zdj();		//运距折单价
		double _Star=bsv.getStar_zdj();			//Star折单价
		
		//计算盈亏，折价金额
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
		
	
		_Yunjuzje=CustomMaths.add((double)CustomMaths.Round_new(_Yunju*bsv.getJiessl(),2),bsv.getYunju_zje_tscl());			//运距折金额
		bsv.setYunju_zje(_Yunjuzje);
		bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
	
		_Shulzje=CustomMaths.add((double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2),bsv.getShul_zje_tscl());	//超过狂发量的盈亏
		bsv.setShul_zje(_Shulzje);
		bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl),bsv.getMeikzkkblxsw()));
		
		//结算单显示时指标折金额项
		bsv.setShulzjbz(_Shulzjbz);
	}
	
	private void reCount(){
//		根据danpcjsmxb 给最后的单批次总结算单赋值。注：加权
		JDBCcon con =new JDBCcon();
		try{
			
//			结算指标信息
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
			
//			结算数量、价格、金额信息
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
					
//					结算价格
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
			
//			结算到厂价中煤款运费的情况
			if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//				如果合同中的结算方式为到厂价,统一算法：
//				如果有运费，那么用总煤款减去总运费剩下的就是总煤款
				computYunfAndHej();
				if(bsv.getYunzfhj()>0){
					
					bsv.setJiashj((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getYunzfhj(),2));	//原始价税合计
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
			
			
			
//	    	计算拒付亏吨运费
	    	if(bsv.getKuidjfyf().equals("是")){
	    		
	    		double Tielyfdj=(double)CustomMaths.Round_new(bsv.getTielyf()/bsv.getGongfsl(),2);
	    		double Tielzfdj=(double)CustomMaths.Round_new(bsv.getTielzf()/bsv.getGongfsl(),2);
	    		
	    		double Kuangqyfdj=(double)CustomMaths.Round_new(bsv.getKuangqyf()/bsv.getGongfsl(),2);
	    		double Kuangqzfdj=(double)CustomMaths.Round_new(bsv.getKuangqzf()/bsv.getGongfsl(),2);
	    		
	    		bsv.setTielyf((double)CustomMaths.Round_new(bsv.getTielyf()-(double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2),2));
	    		bsv.setTielzf((double)CustomMaths.Round_new(bsv.getTielzf()-(double)CustomMaths.Round_new(Tielzfdj*bsv.getKuid(),2),2));
	    		
	    		bsv.setKuangqyf((double)CustomMaths.Round_new(bsv.getKuangqyf()-(double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2),2));
	    		bsv.setKuangqzf((double)CustomMaths.Round_new(bsv.getKuangqzf()-(double)CustomMaths.Round_new(Kuangqzfdj*bsv.getKuid(),2),2));
	    		
	    		if((double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2)>0){
	    			
	    			bsv.setBeiz(bsv.getBeiz()+" "+"亏吨拒付运费："+(double)CustomMaths.Round_new(Tielyfdj*bsv.getKuid(),2)+"元，亏吨拒付杂费："+(double)CustomMaths.Round_new(Tielzfdj*bsv.getKuid(),2)+"元");
	    		}
	    		
	    		if((double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2)>0){
	    			
	    			bsv.setBeiz(bsv.getBeiz()+" "+"亏吨拒付矿运费："+(double)CustomMaths.Round_new(Kuangqyfdj*bsv.getKuid(),2)+"元，亏吨拒付矿杂费："+(double)CustomMaths.Round_new(Kuangqzfdj*bsv.getKuid(),2)+"元");
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
		String _Hejdx="";
		
		//计算运费项
		_Yunzfhj=(double)CustomMaths.Round_new(_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf,2);									//运杂费合计
		_Yunfsk=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new(_Tielyf*_Yunfsl,2)+_Kuangqsk),2);		//运费税款		
		_Buhsyf=(double)CustomMaths.Round_new(((double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2)+_Kuangqjk),2);		//不含税运费
		
		if(_Yunzfhj==0){
			
			_Yunzfhj=(double)CustomMaths.Round_new(bsv.getYunfjsdj()*bsv.getYunfjsl(),2);						//运费税款
			_Yunfsk=(double)CustomMaths.Round_new(_Yunzfhj*_Yunfsl,2);											//运费税款
			_Buhsyf=(double)CustomMaths.Round_new((_Yunzfhj-_Yunfsk),2);											//不含税运费
			_Tielyf=_Yunzfhj;
			bsv.setTielyf(_Tielyf);
		}
		
		//合计
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
//		结算特殊指标处理
//		目前先只处理加权平均结算时，某些指标需要单批次计算，进行特殊处理的情况
//		该函数只计算增扣款
		Interpreter bsh=null;
		String tmp[]=null;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String strtmp="";
		try {
			for(int i=0;i<bsv.getTsclzbs().length;i++){
				
				tmp = bsv.getTsclzbs()[i].split(",");
				if(tmp[tmp.length-1].equals("0")){
//					说明该指标还未经过特殊处理
					
					bsh=new Interpreter();
					bsh.set("结算形式", Locale.jiaqpj_jiesxs);

//					煤款含税单价保留小数位
					bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());	
					
//					含税单价取整方式
					bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());
					
					bsh.set("计价方式", Locale.rezakkf_hetjjfs);
					bsh.set("价格单位", Locale.yuanmqk_danw);	
					bsh.set("合同价格", 0);
					bsh.set("最高煤价", bsv.getZuigmj());
					
//					增扣款保留小数位
					bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());
					
//					用户自定义公式
					bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

					this.getZengkk(bsv.getHetb_Id(), bsh, false ,tmp);
					if(bsv.getTsclzbzkksfxyjs()){
						
//						将下一次指标值不进行增扣款计算
						bsv.setTsclzbzkksfxyjs(false);
//						该指标需要增扣款特殊处理
						bsh.eval(bsv.getGongs_Mk());
//						得到增扣款价格信息
						Shoumjsdcz.setJieszb_Tszbcl(bsh, bsv, tmp[1]);
//						要找到该增扣款对应的数量
						rsl=con.getResultSetList(Shoumjsdcz.getJiesszl_Sql(bsv, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, 
								Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, " and "+tmp[2]+"="+tmp[3]).toString());
						
						if(rsl.next()){
							
//							得到该指标的折价标准
//							字符串规则：指标编码,指标的值,指标折单价,折价数量,折金额
							strtmp+=tmp[1]+","+tmp[4]+","+bsh.get("折单价_"+tmp[1]).toString()+","+rsl.getString("jiessl")+","+CustomMaths.Round_new(CustomMaths.mul(Double.parseDouble(bsh.get("折单价_"+tmp[1]).toString()),rsl.getDouble("jiessl")),2)+";";
						}
					}
				}
			}
			
			if(!strtmp.equals("")){
//				如果有特殊处理的指标，就将Tsclzbs赋值为特殊指标的增扣款记录

				bsv.setTsclzbs(strtmp.split(";"));
//				运距,25,10,100,1000;
//				运距,23,12,300,3600;
			}else{
//				将Tsclzbs至空
				bsv.setTsclzbs(null);
			}
			
			if(rsl!=null){
				
				rsl.close();
			}
			
			con.Close();
			
		} catch (EvalError ev) {
			// TODO 自动生成 catch 块
			ev.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void Danjsmk_dcjcl(int Place, String selIds, long hetb_id, double shangcjsl){
//		函数名称：
		
//			单结算煤款（到厂价处理）
//		函数功能：
		
//			处理单结算煤款时，当煤款合同为到厂价时，减运费的问题			
//		函数逻辑：
//			逻辑1：
//				如果是单结算煤款且又是到厂价时，计算运费。
				
//			逻辑2：
//				在recount函数中已经计算过了到厂价的情况，要将运费里面的值清空
//		函数形参：
//			Place要应用的逻辑结构，1为逻辑1；2为逻辑2
//			selIds要结算的列id
//			idhetb_id煤款合同表
		
		if(Place==1){
//			逻辑1
			if(bsv.getJieslx()==Locale.meikjs_feiylbb_id
					&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
			){
//				特殊业务情形，如果是单结算煤款、合同价格是到厂价时，计算运费，
//					在后面的recount中从煤款中减掉运费，并将运费数据清空。
				this.getYunFei(selIds, Locale.liangpjs_feiylbb_id, hetb_id, shangcjsl);
			}
		}else if(Place==2){
//			逻辑2
			if(bsv.getJieslx()==Locale.meikjs_feiylbb_id
					&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
			){
//				特殊业务情形，如果是单结算煤款、合同价格是到厂价时，计算运费，
//					在后面的recount中从煤款中减掉运费，并将运费数据清空。
				bsv.setTielyf(0);
				bsv.setTielzf(0);
				bsv.setYunfsl(0);		//运费税率
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