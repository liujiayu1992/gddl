 /*
 * 创建日期 2008-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.shihs.hesgl;
/**
 * @author Admini_ator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
//		参数：
//			SelIds			选择的shihcpb的id
//			Diancxxb_id		电厂信息表id
//			Jieslx			结算类型id
//			Jieskdl			结算扣吨量
		
		bsv.setIsError(true);
		bsv.setErroInfo("");
		bsv.setJieslx(Jieslx);
		bsv.setDiancxxb_id(Diancxxb_id);
		
		if(Jieslx==Locale.shihsjs_feiylbb_id){
//			两票结算、煤款结算
			
			if (getBaseInfo(SelIds,Diancxxb_id,Jieskdl)){
//				得到供应商、运输方式等基本信息、要求数量和质量一定要审核
				
			}else{
				
				bsv.getErroInfo();
				return bsv;
			}
			
//			得到公式。
			if(Jieslx==Locale.shihsjs_feiylbb_id){
//				石灰石结算
				if (getGongsInfo(Diancxxb_id,"SHIH")) {
					
				}else{
					//return ErroInfo;
					return bsv;
				}
			}
			
//			算煤价,合同中的相关值
			if(getMeiPrice(bsv.getRanlpzb_Id(),bsv.getYunsfsb_id(),bsv.getFaz_Id(),
						bsv.getDaoz_Id(),Diancxxb_id,bsv.getHetb_Id(),
						bsv.getFahksrq(),Jieslx,SelIds,
						Jieskdl)){
				
			}else{
				
				bsv.getErroInfo();
				return bsv;
			}
		}
//		else{	//运费结算、包括地铁运费
//			
//			if (getBaseInfo(SelIds,Diancxxb_id,Jieskdl)){
////				得到供应商、运输方式等基本信息
//				
//			}else{
//				bsv.getErroInfo();
//				return bsv;
//			}
//			
//			getJiesszl(SelIds,Diancxxb_id,bsv.getHetb_Id(),Jieskdl,Jieslx);
//			
//			
////			得到运费公式
//			if (getGongsInfo(Diancxxb_id,"YF")) {
//				
//			}else{
//				//return ErroInfo;
//				return bsv;
//			}
//			
////			算运费
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
//            	bsv.setGongs_Yf(clob.getClob("gongsb", "gongs", rs.getLong(1)));
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
		
		String str_Gongs_Shih="";
		
		if(_Type.equals("SHIH")){
			
			str_Gongs_Shih=Jiesdcz.GetJiesgs(_Diancxxb_id,SysConstant.Gs_JS_HeadName_Shih);
			
			if(str_Gongs_Shih.equals("")){
				
				bsv.setErroInfo("没有得到石灰石公式!");
	        	return false;
			}else{
				
				bsv.setGongs_Shih(str_Gongs_Shih);
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
    
	private boolean getBaseInfo(String SelIds,long Diancxxb_id,double Jieskdl) throws Exception{

        JDBCcon con =new JDBCcon();
	    try {
            //发货日期、到货日期、车数、标重、盈亏、运损、发热量、硫 from fahb
	        //过衡量按列取证，结算量火车煤是按列取整，汽车煤是求和取整
	    	
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
	          	
	          	bsv.setJiesrq(Jiesdcz.FormatDate(new Date()));	//结算日期
	          	bsv.setJiesbh(Jiesdcz.getJiesbh_Shih(String.valueOf(Diancxxb_id)));	//结算编号
	          	bsv.setDaibcc(MainGlobal.getShouwch_Cp(SelIds));	
	          	
            }else{
            	bsv.setErroInfo("要结算的车皮信息不存在可能已被其他用户删除!");
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
		
//		结算数、质量
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		try{
//			可在系统信息表中设置的项目：
//			1、石灰石结算数量组成方式
//			2、石灰石结算数量取整方式
//			3、石灰石结算数量保留小数位
//			4、石灰石加权数量
//			5、石灰石含税单价保留小数位
			
//			特别说明：合同中设定的“数量结算”的优先级要高于系统信息表中设定的“结算数量组成方式”
//				如果合同中“数量结算”设置为“厂方数量”程序会从xitxxb中取数，默认为maoz-piz-koud
//				如果合同中“数量结算”设置为“矿方数量”程序就将结算量定为biaoz，不再去xitxxb中取数
//				因为目前没有矿方质量的需求故不考虑
			
			String jies_Jqsl="maoz-piz-koud";				//结算加权数量
			String jies_CaOblxs="2";						//结算指标保留小数位
			String jies_MgOblxs="2";
			String jies_Xidblxs="2";
			
			String jies_Jieslqzfs="sum()";					//结算数量取整方式
			String jies_Jsslblxs="2";						//结算数量保留小数位
			String jies_Kdkskzqzfs="sum()";					//扣吨、扣水、扣杂取整方式
			String jies_Jssl="maoz-piz-koud";				//结算数量
			String jies_yunfjssl="maoz-piz-koud";			//运费结算数量
			
			String jies_Guohlqzfs=Locale.bujxqzcz_jiesghlqzfs_xitxx;	//系统信息中过衡量取整方式，默认为：不进行取整操作(sum())
			String jies_Guohlblxsw="2";						//系统信息中过衡量保留小数位，默认为：2
			String yunsdw="";	//运输单位的条件
			
//			从系统信息表中取结算设置的信息
			String XitxxArrar[][]=null;	
			XitxxArrar=MainGlobal.getXitxx_items("石灰石结算",
					"'"+Locale.jiesslzcfs_jies+"'," +		//结算数量组成方式
					"'"+Locale.jiesslqzfs_jies+"'," +		//结算数量取整方式
					"'"+Locale.jiesslblxsw_jies+"'," +		//结算数量保留小数位
					"'"+Locale.jiaqsl_xitxx+"'," +			//加权数量
					"'"+Locale.hansdjblxsw_xitxx+"'"		//含税单价保留小数位
					,String.valueOf(Diancxxb_id));
			
//			分析取得的值，然后对变量进行赋值
			if(XitxxArrar!=null){
				
				for(int i=0;i<XitxxArrar.length;i++){
					
					if(XitxxArrar[i][0]!=null){
						
						if(XitxxArrar[i][0].trim().equals(Locale.jiesslzcfs_jies)){
//							结算数量组成方式
							jies_Jssl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslqzfs_jies)){
//							结算数量取整方式
							jies_Jieslqzfs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslblxsw_jies)){
//							结算数量保留小数位
							jies_Jsslblxs=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//							加权数量
							jies_Jqsl=XitxxArrar[i][1].trim();
						}else if(XitxxArrar[i][0].trim().equals(Locale.hansdjblxsw_xitxx)){
//							含税单价保留小数位
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
			sql.append("	 --厂方验收   																															\n");
			sql.append("	 	decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*zl.caco3)/sum("+jies_Jqsl+"),2)) as cao,   								\n");
			sql.append("	 	decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*zl.mgco3)/sum("+jies_Jqsl+"),2)) as mgo,   								\n");
			sql.append("	 	decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*zl.xid)/sum("+jies_Jqsl+"),2)) as xid   								\n");
			
			sql.append("	 	from shihcpb cp,shihcyb cy,shihzlb zl										\n");
			sql.append("			where cp.shihcyb_id=cy.id and cy.shihzlb_id=zl.id   					\n");
			sql.append("				and cp.id in ("+SelIds+")) 	\n");
		
			
			rsl=con.getResultSetList(sql.toString());
			if(rsl.next()){
				
//				数量
				bsv.setKoud(rsl.getDouble("koud"));					//扣吨
            	bsv.setChes(rsl.getLong("ches"));					//车数
            	
            	bsv.setGongfsl(rsl.getDouble("biaoz"));				//标重
            	bsv.setYingksl(rsl.getDouble("yingk"));				//盈亏  
            	
            	bsv.setYanssl(rsl.getDouble("jingz"));				//厂方验收数量
            	bsv.setJingz(rsl.getDouble("jingz"));				//净重
            	bsv.setKoud_js(Jieskdl);											//结算扣吨
            	
            	if(bsv.getShuljs().equals("厂方数量")){
                	
            		bsv.setJiessl(rsl.getDouble("jiessl")-Jieskdl);		//结算重量
                	
                }else if(bsv.getShuljs().equals("矿方数量")){
                	
                	bsv.setJiessl(rsl.getDouble("biaoz")-Jieskdl);		//结算重量
                }
            	
            	bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl()-bsv.getJingz()),2));	//结算数量差异(结算量和过衡量的差值)
            	
            	bsv.setYuns(rsl.getDouble("yuns"));					//实际运损
//	        	厂方指标
	        	bsv.setCaO_cf(rsl.getDouble("CaO"));
	            bsv.setMgO_cf(rsl.getDouble("MgO")); 
	            bsv.setXid_cf(rsl.getDouble("xid"));
	            
//              结算指标
                
            	bsv.setCaO_js(rsl.getDouble("cao"));
                bsv.setMgO_js(rsl.getDouble("mgo")); 
                bsv.setXid_js(rsl.getDouble("xid"));
                
                if(bsv.getCaO_js()==0){
                	
                	bsv.setErroInfo("没有化验数据，请检查！");
                }
			}
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	//计算煤价,热量折价,硫折价,灰熔点折价
	private boolean getMeiPrice(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
									long Hetb_id,Date Minfahrq,long Jieslx,
									String SelIds,double Jieskdl){
		//得到合同信息中的运价
			JDBCcon con =new JDBCcon();
			String sql="";
			Interpreter bsh=new Interpreter();
			Jiesdcz Jscz=new Jiesdcz();
			try{
				
//				合同信息，包括合同量、供方开户行、供方帐号
				sql="select ht.hetsl,gys.kaihyh as gongfkhyh,gys.zhangh as zhangh,\n" +
					"       decode(ht.shuljs,0,'厂方数量','供方数量') as shuljs,\n" + 
					"       decode(ht.zhiljs,0,'厂方数量','供方数量') as zhiljs\n" + 
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
				
//				价格（合同中一个合同对应多个基础价格）	
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
					
					bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
					bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
					bsv.setZuidmj(rsl.getDouble("zuidmj")); 		//最低煤价
					bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
					bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
					bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
				
					bsh.set("价格单位", bsv.getHetmdjdw());	
					bsh.set("合同价格", bsv.getHetmdj());
					bsh.set("最高价格", bsv.getZuigmj());
					bsh.set("最低价格", bsv.getZuidmj());
					
//					合同基价指标,取出符合条件的合同基价
					if(rsl.getRows()==1){
						
//						就一条合同
//						获得结算数量、质量
						getJiesszl(SelIds,Diancxxb_id,Hetb_id,Jieskdl,Jieslx);
						
						double Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
						Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
						
						bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值+单位（多个单位的指标用）
						
						bsv.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
						
						bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
						bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
						
						bsh.set(rsl.getString("zhib")+"计量单位", 	"");
						bsh.set(rsl.getString("zhib")+"增扣单价", 	0);
						bsh.set(rsl.getString("zhib")+"增扣价单位", 	"");
						bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
						bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
						bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
						bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
						
//						获得增扣款
						getZengkk(Hetb_id,bsh);
						
//						含税单价保留小数位
						bsh.set(Locale.hansdjblxsw_xitxx, bsv.getHansdjblxsw());	
						
//								执行公式
						bsh.eval(bsv.getGongs_Shih());
						
//								得到计算后的指标
						setJieszb(bsh,0);
						
//								计算煤款金额
						computData();
						
						bsv.setHetjgpp_Flag(true);
						bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
					
					}else{
//						有多个合同
						
							double	Dbljijzb=0;
	//						获得结算数量、质量
							getJiesszl(SelIds,Diancxxb_id,Hetb_id,Jieskdl,Jieslx);
							
							do{
								
								Dbljijzb=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
								Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
								
								if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
									
									bsv.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
									bsv.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
									bsv.setZuidmj(rsl.getDouble("zuidmj")); 		//最低煤价
									bsv.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
									bsv.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
									bsv.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
								
									bsh.set("价格单位", bsv.getHetmdjdw());	
									bsh.set("合同价格", bsv.getHetmdj());
									bsh.set("最高价格", bsv.getZuigmj());
									bsh.set("最低价格", bsv.getZuidmj());
									
									bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));		//合同价格表id
									
									bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
									
									bsv.setYifzzb(rsl.getString("zhib"));			//默认的已赋值指标
									
									bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
									bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
									
									bsh.set(rsl.getString("zhib")+"计量单位", 	"");
									bsh.set(rsl.getString("zhib")+"增扣单价", 	0);
									bsh.set(rsl.getString("zhib")+"增扣价单位", 	"");
									bsh.set(rsl.getString("zhib")+"增扣款条件", 	"");
									bsh.set(rsl.getString("zhib")+"增扣款基数", 	0);
									bsh.set(rsl.getString("zhib")+"增扣款基数单位", 	"");
									bsh.set(rsl.getString("zhib")+"基准增扣价", 	0);
									
	//												获得增扣款
									getZengkk(Hetb_id,bsh);
									
	//								含税单价保留小数位
									bsh.set(Locale.hansdjblxsw_xitxx, bsv.getHansdjblxsw());	
									
	//												执行公式
									bsh.eval(bsv.getGongs_Shih());
									
	//												得到计算后的指标
									setJieszb(bsh,0);
									
	//												计算煤款金额
									computData();
									
									bsv.setHetjgpp_Flag(true);
									bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
								}
								
							}while(rsl.next());
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
			
			String sql="select zb.bianm as zhib,tj.bianm as tiaoj,shangx,xiax,dw.bianm as danw,jis,jsdw.bianm as jisdw,\n" +
				"       kouj as zengkj,kjdw.bianm as zengkjdw,jicj as jizzkj\n" + 
				"       from shihhtb ht,shihhtzkk zkk,zhibb zb,tiaojb tj,danwb dw,danwb jsdw,danwb kjdw\n" + 
				"       where ht.id=zkk.shihhtb_id and zkk.zhibb_id=zb.id and zkk.tiaojb_id=tj.id\n" + 
				"             and zkk.danwb_id=dw.id(+) and zkk.jisdwid=jsdw.id(+)\n" + 
				"             and zkk.koujdw=kjdw.id\n" + 
				"             and ht.id="+Hetb_id;

			
			rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//记录指标结算值
			String  Strtmp="";		//记录设定的指标
			while(rsl.next()){
				
				Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
//				指标的结算指标
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					//指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
					
					bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
					bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));			//上下限单位
					bsh.set(rsl.getString("zhib")+"增扣款条件", 	rsl.getString("tiaoj"));		//大于等于、大于、小于、小于等于、	区间、等于
					bsh.set(rsl.getString("zhib")+"增扣单价",	rsl.getDouble("zengkj"));		//增扣价
					bsh.set(rsl.getString("zhib")+"增扣价单位", 	rsl.getString("zengkjdw")==null?"":rsl.getString("zengkjdw"));	//增价单位
					bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
					bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
					bsh.set(rsl.getString("zhib")+"增扣款基数",	rsl.getDouble("jis"));			//基数（每升高xx或降低xx）
					bsh.set(rsl.getString("zhib")+"增扣款基数单位",	rsl.getString("jisdw"));	//基数单位
					bsh.set(rsl.getString("zhib")+"基准增扣价",	rsl.getDouble("jizzkj"));		//基准增扣价（用于多段增扣价累计时使用）
					
					Strtmp+="'"+rsl.getString("zhib")+"',";					//记录用户设置的影响结算单价的指标
				}
			}
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
					
					bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//结算值
					bsh.set(rsl.getString("zhib")+"计量单位", 	Strtmpdw);				//指标单位
					bsh.set(rsl.getString("zhib")+"增扣款条件", 	"区间");					//大于等于、大于、小于、小于等于、	区间、等于
					bsh.set(rsl.getString("zhib")+"增扣单价",	0);						//增付价
					bsh.set(rsl.getString("zhib")+"增扣价单位", 	"");					//增价单位
					bsh.set(rsl.getString("zhib")+"上限", 		0);						//指标上限
					bsh.set(rsl.getString("zhib")+"下限", 		0);						//指标下限
					bsh.set(rsl.getString("zhib")+"增扣款基数",	0);						//基数（每升高xx或降低xx）
					bsh.set(rsl.getString("zhib")+"增扣款基数单位",	"");				//基数单位
					bsh.set(rsl.getString("zhib")+"基准增扣价",	0);						//基准增扣价（用于多段增扣价累计时使用）
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
//				bsh.set("运价里程", bsv.getYunju_js());
				
				if(!bsv.getGongs_Yf().equals("")){
					
//					if(bsv.getYunfjsl()>0){
					if(true){
						
//						运费含税单价保留小数位
						bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
						
						getZengkk_yf(Hetb_id,bsh);	//不起任何作用只是公式上部报错
//						计算运费
						bsh.eval(bsv.getGongs_Yf());
//						bsv.setYunfjsdj(Double.parseDouble(bsh.get("运费结算单价").toString()));
						setJieszb(bsh,1);
						
					}else{
						
						bsv.setErroInfo("运费结算数量为零，请设置");
						return;
					}
				}else{
					
					bsv.setErroInfo("请设置运费计算公式");
					return;
				}
			}else{
				
//				取hetys中的运价
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
//						就一条合同价格
						bsh.set("合同运价", rsl.getDouble("yunja"));
						bsh.set("合同运价单位", rsl.getString("yunjdw"));
//						bsh.set("运价里程", bsv.getYunju_js());
						
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
//						double yunju=bsv.getYunju_cf();	//运距
						double yunju=0;
						double Dbltmp=0;
						
						do{
							shangx=rsl.getDouble("shangx");
							xiax=rsl.getDouble("xiax");
							
							Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
							
							Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
							
							if(yunju>=xiax&&yunju<=(shangx==0?9999:shangx)){
								
								bsh.set("合同运价", rsl.getDouble("yunja"));
								bsh.set("合同运价单位", rsl.getString("yunjdw"));
//								bsh.set("运价里程", bsv.getYunju_js());
								
								getZengkk_yf(Hetb_id,bsh);
								
//								运费含税单价保留小数位
								bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());	
								
								bsh.eval(bsv.getGongs_Yf());
								setJieszb(bsh,1);
							}
							
						}while(rsl.next());
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
				
				Dbltmp=Jiesdcz.getZhib_info_Shih(bsv,rsl.getString("zhib"),"js");
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
				
	//			CaO
				bsv.setCaO_ht(bsh.get("合同标准_CaO").toString());
				bsv.setCaO_yk(Double.parseDouble(bsh.get("盈亏_CaO").toString()));
				bsv.setCaO_zdj(Double.parseDouble(bsh.get("折单价_CaO").toString()));
				
	//			MgO
				bsv.setMgO_ht(bsh.get("合同标准_MgO").toString());
				bsv.setMgO_yk(Double.parseDouble(bsh.get("盈亏_MgO").toString()));
				bsv.setMgO_zdj(Double.parseDouble(bsh.get("折单价_MgO").toString()));
				
	//			细度
				bsv.setXid_ht(bsh.get("合同标准_细度").toString());
				bsv.setXid_yk(Double.parseDouble(bsh.get("盈亏_细度").toString()));
				bsv.setXid_zdj(Double.parseDouble(bsh.get("折单价_细度").toString()));
			}
			//结算单价
			if(Type==0){
				
				bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
			}else if(Type==1){
				
				if(bsv.getJieslx()==Locale.guotyf_feiylbb_id){
					
				    bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
				}
				bsv.setYunfjsdj(Double.parseDouble(bsh.get("结算价格").toString()));
			}
			
		} catch (EvalError e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	//计算费用加权
	private void computData(){
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
		
		//指标折单价
		double _CaO=bsv.getCaO_zdj();
		double _MgO=bsv.getMgO_zdj();
		double _Xid=bsv.getXid_zdj();
		
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
		double _CaOzje=0;
		double _MgOzje=0;
		double _Xidzje=0;
		double _Shulzje=0;
		//价格金额计算

		_Jiashj=(double)CustomMaths.Round_new(_Hansmj*_Jiessl,2);												//价税合计
		_Jiakhj=(double)CustomMaths.Round_new((_Jiashj)/(1+_Meiksl),2);										//价款合计
		_Jiaksk=(double)CustomMaths.Round_new((_Jiashj-_Jiakhj),2);												//价款税款
		_Jine=_Jiakhj;																						//金额
		_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);	
		_Shulzjbz=_Hansmj;
		//合计
		_Hej=(double)CustomMaths.Round_new((_Jiashj),2);
		
		//计算盈亏，折价金额
		_CaOzje=(double)CustomMaths.Round_new(_CaO*_Jiessl,2);
		_MgOzje=(double)CustomMaths.Round_new(_MgO*_Jiessl,2);
		_Xidzje=(double)CustomMaths.Round_new(_Xid*_Jiessl,2);
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		_Shulzje=(double)CustomMaths.Round_new(_Shulzjbz*bsv.getYingksl(),2);	//超过狂发量的盈亏
		
		//结算单显示时指标折金额项
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