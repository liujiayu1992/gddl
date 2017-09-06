package com.zhiren.jt.jiesgl.changfhs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.FileNameFilter;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Jiesdyz{
//	根据电厂上传的结算数据进行厂级介绍单的验证
//	2008年08月25日 版本V1.0
//		实现功能：根据电厂传上来的数据，重新计算煤款单价、价税合计
//		思路:电厂将结算的数据通过接口上传到jiesyzsjy表中，
//			如果是加权平均就往jiesyzsjy表中写一条数据，如果是单批次就要写多条数据
//			要想进行结算验证有几个前提条件：
//			1、首先结算单要找到合同
//			2、要可以找到对应的fahb记录
//			3、要得到对应的xml结算公式
//			4、要得到jiesyzsjy的信息
	
	Jiesdyzbean yzb=new Jiesdyzbean(); 
	
//	1、根据厂方得到数量质量信息
	private String getYanz(long Jiesb_id,long Diancxxb_id){
		
		String sql="";
		
		JDBCcon con=new JDBCcon();
		try{
//			先得到合同信息，得到hetb_id和jiessl
			sql="select jiessl,jiesb.hetb_id,jiesb.shuil,round_new(yingd-kuid,2) as yingk from jiesb,hetb 	\n" +
					" where jiesb.hetb_id=hetb.id  		   						\n" +
					" and jiesb.id="+Jiesb_id+" and jiesb.diancxxb_id="+Diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				yzb.setHetb_id(rsl.getLong("hetb_id"));
				yzb.setJiessl(rsl.getDouble("jiessl"));
				yzb.setMeiksl(rsl.getDouble("shuil"));
			}
			
			if(yzb.getHetb_id()==0){//未找到合同
				
				yzb.setErro_Msg("没有找到对应的合同");
				return "";
			}else{//已找到合同
				
				if(setOtherJcxx(Jiesb_id,Diancxxb_id)){//从发货表中取出基础信息
					
					if(getGongsInfo(Diancxxb_id)){
						
						yzb.setSelIds(getJiesyzsjy_SelId(Jiesb_id,Diancxxb_id));
						
						if(!yzb.getSelIds().equals("")){//得到基础数据源Id的串
							
							if(Count(yzb.getRanlpzb_id(),yzb.getYunsfsb_id(),yzb.getFaz_id(),yzb.getDaoz_id(),
									Diancxxb_id,yzb.getHetb_id(),yzb.getMinfahrq(),yzb.getGongysb_id())){
								
								yzb.setErro_Msg("结算价格时出错");
								return "";
							}
						}else{
							
							yzb.setErro_Msg("没有得到验证数据源信息");
							return "";
						}
						
					}else{
						
						yzb.setErro_Msg("没有得到公式信息");
						return "";
					}
				}else{
					
					yzb.setErro_Msg("没有得到基础数据");
					return "";
				}
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return yzb.getErro_Msg();
	}

	private boolean setOtherJcxx(long lngJiesb_id,long diancxxb_id){
		// TODO 自动生成方法存根
//		这些信息在jiesb中无法得到，所以写次方法
		JDBCcon con=new JDBCcon();
		String sql="";
		boolean Flag=false;
		try{
			
			sql="select max(gongysb_id) as gongysb_id,max(pinzb_id) as ranlpzb_id,max(yunsfsb_id) as yunsfsb_id,max(faz_id) as faz_id, \n" +
					" max(daoz_id) as daoz_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq	\n"+
					" from fahb where jiesb_id="+lngJiesb_id+" and diancxxb_id="+diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.getRows()>0){
				
				if(rsl.next()){
					
					yzb.setGongysb_id(rsl.getLong("gongysb_id"));
					yzb.setRanlpzb_id(rsl.getLong("ranlpzb_id"));
					yzb.setYunsfsb_id(rsl.getLong("yunsfsb_id"));
					yzb.setFaz_id(rsl.getLong("faz_id"));
					yzb.setDaoz_id(rsl.getLong("daoz_id"));
					yzb.setMinfahrq(rsl.getDate("minfahrq"));
					Flag=true;
				}
			}
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Flag;
	}

	private boolean Count(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
			long Hetb_id,Date Minfahrq,long Gongysb_id) {
		// TODO 自动生成方法存根
		

		//得到合同信息中的运价
			JDBCcon con =new JDBCcon();
			String sql="";
			Interpreter bsh=new Interpreter();
			Jiesdcz Jscz=new Jiesdcz();
			try{
				
//				数量(合同中以月为单位，每月存一条，暂定先取一条数)
				sql="select htsl.hetl,htb.gongfkhyh,htb.gongfzh \n"
					+ " from hetb htb, hetslb htsl	\n"
					+ " where htb.id=htsl.hetb_id	\n"      
					+ " and htsl.pinzb_id="+Ranlpzb_id+" and yunsfsb_id="+Yunsfsb_id+" and faz_id="+Faz_id+" and daoz_id="+Daoz_id+"	\n"
					+ " and htsl.diancxxb_id="+Diancxxb_id+" and htsl.riq<=to_date('"+Jiesdcz.FormatDate(Minfahrq)+"','yyyy-MM-dd')	\n"              
					+ " and htb.id="+Hetb_id+" order by riq desc ";
				
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.next()){
					
					yzb.setHetml(rsl.getDouble("hetl"));
					yzb.setKaihyh(rsl.getString("gongfkhyh"));
					yzb.setZhangh(rsl.getString("gongfzh"));
				}
				
//				质量(合同中一个合同号对应多个质量记录)
				sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
					+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
					+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
					+ " and htzl.danwb_id=dwb.id	\n"
					+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
					+ " and htb.id="+Hetb_id+" ";
				
//				价格（合同中一个合同对应多个基础价格）	
				sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,				\n"
					+ " jijdw.bianm as jijdw,jsfs.mingc as  jiesfs,jsxs.bianm as jiesxs,yunj,					\n"
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
					
					yzb.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
					yzb.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
					yzb.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
					yzb.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
					yzb.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
					yzb.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
					yzb.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
					yzb.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
				
					bsh.set("结算形式", yzb.getJiesxs());
					bsh.set("计价方式", yzb.getHetjjfs());
					bsh.set("价格单位", yzb.getHetmdjdw());	
					bsh.set("合同价格", yzb.getHetmdj());
					bsh.set("最高煤价", yzb.getZuigmj());
					
//					合同基价指标,取出符合条件的合同基价
					if(rsl.getRows()==1){
						
//						就一条合同
						if(yzb.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								目录价结算
							
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){//								单批次结算
								
								String[] test=null;
								
								if(yzb.getSelIds().indexOf(",")>-1){
									
									test[0]=yzb.getSelIds();
								}else{
									
									test=yzb.getSelIds().split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									获得结算数量、质量
									getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
									
//									为目录价赋值
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
//									获得增扣款
									getZengkk(Hetb_id,bsh);
									
//									用户自定义公式
									bsh.set(Locale.user_custom_mlj_jiesgs,yzb.getUser_custom_mlj_jiesgs());			
									
//									执行公式
									bsh.eval(yzb.getGongs());
									
//									得到计算后的指标
									setJieszb(bsh);
									
//									计算煤款金额
									computData_Dpc();
								}
								
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						加权平均
								
//								获得结算数量、质量
								getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs, yzb.getUser_custom_mlj_jiesgs());
								
//								执行公式
								bsh.eval(yzb.getGongs());
								
//								得到计算后的指标
								setJieszb(bsh);
								
//								计算煤款金额
								computData();
							}
							
						}else{		
							
//							非目录价
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){//							单批次结算
								
								String[] test=null;
								
								if(yzb.getSelIds().indexOf(",")>-1){
									
									test[0]=yzb.getSelIds();
								}else{
									
									test=yzb.getSelIds().split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									获得结算数量、质量
									getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
									
									double Dbltmp=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
									
									Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
									
									bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
									bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));								//指标单位
									
									yzb.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
									
									bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
									bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
									
									
//									获得增扣款
									getZengkk(Hetb_id,bsh);
									
//									用户自定义公式
									bsh.set(Locale.user_custom_fmlj_jiesgs,yzb.getUser_custom_fmlj_jiesgs());
									
//									执行公式
									bsh.eval(yzb.getGongs());
									
//									得到计算后的指标
									setJieszb(bsh);
									
//									计算煤款金额
									computData_Dpc();
								}
								
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
								
//								获得结算数量、质量
								getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
								
								double Dbltmp=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
								Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
								
								bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//结算值
								bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
								
								yzb.setYifzzb(rsl.getString("zhib"));	//默认的已赋值指标
								
								bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
								bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_fmlj_jiesgs, yzb.getUser_custom_fmlj_jiesgs());
								
//								执行公式
								bsh.eval(yzb.getGongs());
								
//								得到计算后的指标
								setJieszb(bsh);
								
//								计算煤款金额
								computData();
							}
						}
						
						yzb.setHetjgpp_Flag(true);
						yzb.setHetjgb_id(rsl.getLong("hetjgb_id"));
					}else{
//						有多个合同
						
						if(yzb.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//					目录价结算
						
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){		//					单批次结算
							
								String[] test=null;
								
								if(yzb.getSelIds().indexOf(",")>-1){
									
									test[0]=yzb.getSelIds();
								}else{
									
									test=yzb.getSelIds().split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
	//								获得结算数量、质量
									getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
									
	//								为目录价赋值
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
	//								获得增扣款
									getZengkk(Hetb_id,bsh);
									
//									用户自定义公式
									bsh.set(Locale.user_custom_mlj_jiesgs, yzb.getUser_custom_mlj_jiesgs());
									
	//								执行公式
									bsh.eval(yzb.getGongs());
									
	//								得到计算后的指标
									setJieszb(bsh);
									
	//								计算煤款金额
									computData_Dpc();
								}
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均

								
//								获得结算数量、质量
								getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
								
//								为目录价赋值
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								获得增扣款
								getZengkk(Hetb_id,bsh);
								
//								用户自定义公式
								bsh.set(Locale.user_custom_mlj_jiesgs, yzb.getUser_custom_mlj_jiesgs());
								
//								执行公式
								bsh.eval(yzb.getGongs());
								
//								得到计算后的指标
								setJieszb(bsh);
								
//								计算煤款金额
								computData();
							}
						}else{
							
//							多个合同
//							非目录价
							double	Dbljijzb=0;
							
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){	//单批次结算
								
									String[] test=null;
									
									if(yzb.getSelIds().indexOf(",")>-1){
										
										test[0]=yzb.getSelIds();
									}else{
										
										test=yzb.getSelIds().split(",");
									}
							
									for(int i=0;i<test.length;i++){
										
										ResultSetList rsltmp=rsl;
										
//										获得结算数量、质量
										getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
										
										do{
											
											Dbljijzb=Jiesdcz.getYanZZb_info(yzb, rsltmp.getString("zhib"));
											Dbljijzb=Jiesdcz.getUnit_transform(rsltmp.getString("zhib"),rsltmp.getString("danw"),Dbljijzb);
										
											if(Dbljijzb>=rsltmp.getDouble("xiax")&&Dbljijzb<=(rsltmp.getDouble("shangx")==0?9999:rsltmp.getDouble("shangx"))){
											
												yzb.setHetmdj(rsltmp.getDouble("jij"));			//结算煤单价
												yzb.setZuigmj(rsltmp.getDouble("zuigmj"));			//最高煤价
												yzb.setHetmdjdw(rsltmp.getString("jijdw"));		//合同煤基价单位
												yzb.setJiesfs(rsltmp.getString("jiesfs"));			//结算方式（出厂价、出矿价）
												yzb.setJiesxs(rsltmp.getString("jiesxs"));			//结算形式（单批次、加权平均）
												yzb.setHetyj(rsltmp.getDouble("yunj"));			//合同运价单价
												yzb.setHetyjdw(rsltmp.getString("yunjdw"));		//合同运价单位
												yzb.setHetjjfs(rsltmp.getString("hejfs"));		//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
											
												bsh.set("结算形式", yzb.getJiesxs());
												bsh.set("计价方式", yzb.getHetjjfs());
												bsh.set("价格单位", yzb.getHetmdjdw());	
												bsh.set("合同价格", yzb.getHetmdj());
												bsh.set("最高煤价", yzb.getZuigmj());
												
												bsh.set(rsltmp.getString("zhib")+Jiesdcz.getZhibbdw(rsltmp.getString("zhib"),rsltmp.getString("danw")),Dbljijzb);	//结算值
												bsh.set(rsltmp.getString("zhib")+"计量单位", 	rsltmp.getString("danw"));
												
												yzb.setYifzzb(rsltmp.getString("zhib"));	//默认的已赋值指标
												
												bsh.set(rsltmp.getString("zhib")+"上限", 		rsltmp.getDouble("shangx"));		//指标上限
												bsh.set(rsltmp.getString("zhib")+"下限", 		rsltmp.getDouble("xiax"));			//指标下限
												
	//											获得增扣款
												getZengkk(Hetb_id,bsh);
												
	//											用户自定义公式
												bsh.set(Locale.user_custom_fmlj_jiesgs, yzb.getUser_custom_fmlj_jiesgs());
												
	//											执行公式
												bsh.eval(yzb.getGongs());
												
	//											得到计算后的指标
												setJieszb(bsh);
												
	//											计算煤款金额
												computData_Dpc();
												
												yzb.setHetjgpp_Flag(true);
											}	
										}while(rsltmp.next());
									}		
									
										
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					加权平均
								
//									获得结算数量、质量
									getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
									
									do{
										
										Dbljijzb=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
										Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
										
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
											
											yzb.setHetmdj(rsl.getDouble("jij"));			//结算煤单价
											yzb.setZuigmj(rsl.getDouble("zuigmj"));			//最高煤价
											yzb.setHetmdjdw(rsl.getString("jijdw"));		//合同煤基价单位
											yzb.setJiesfs(rsl.getString("jiesfs"));			//结算方式（出厂价、出矿价）
											yzb.setJiesxs(rsl.getString("jiesxs"));			//结算形式（单批次、加权平均）
											yzb.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
											yzb.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
											yzb.setHetjjfs(rsl.getString("hejfs"));			//合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
										
											bsh.set("结算形式", yzb.getJiesxs());
											bsh.set("计价方式", yzb.getHetjjfs());
											bsh.set("价格单位", yzb.getHetmdjdw());	
											bsh.set("合同价格", yzb.getHetmdj());
											bsh.set("最高煤价", yzb.getZuigmj());
											
											yzb.setHetjgb_id(rsl.getLong("hetjgb_id"));		//合同价格表id
											
											bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//结算值
											bsh.set(rsl.getString("zhib")+"计量单位", 	rsl.getString("danw"));
											
											yzb.setYifzzb(rsl.getString("zhib"));			//默认的已赋值指标
											
											bsh.set(rsl.getString("zhib")+"上限", 		rsl.getDouble("shangx"));		//指标上限
											bsh.set(rsl.getString("zhib")+"下限", 		rsl.getDouble("xiax"));			//指标下限
											
//												获得增扣款
											getZengkk(Hetb_id,bsh);
											
//												用户自定义公式
											bsh.set(Locale.user_custom_fmlj_jiesgs, yzb.getUser_custom_fmlj_jiesgs());
											
//												执行公式
											bsh.eval(yzb.getGongs());
											
//												得到计算后的指标
											setJieszb(bsh);
											
//												计算煤款金额
											computData();
											
											yzb.setHetjgpp_Flag(true);
										}
										
									}while(rsl.next());
							}
						}
					}
				}
				
				if(!yzb.getHetjgpp_Flag()){
					
					yzb.setErro_Msg("没有合同价格与结算数据匹配！");
					return false;
				}
					
		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    } finally {
		    	con.Close();
		    }
		    return true;
	}
	
	private boolean getGongsInfo(long Diancxxb_id){
	    
		yzb.setGongs(Jiesdcz.GetJiesgs(Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk));
		
		if(!yzb.getGongs().equals("")){
			
			return true;
		}
		return false;
	}

	private void SetJicxx(ResultSetList rsl) {
		// TODO 自动生成方法存根
		
		yzb.setJiessl(rsl.getDouble("jiessl"));
		yzb.setQnetar(rsl.getDouble("Qnetar"));
		yzb.setStd(rsl.getDouble("std"));
		yzb.setAd(rsl.getDouble("ad"));
		yzb.setVdaf(rsl.getDouble("vdaf"));
		yzb.setMt(rsl.getDouble("mt"));
		yzb.setQgrad(rsl.getDouble("Qgrad"));
		yzb.setQbad(rsl.getDouble("Qbad"));
		yzb.setHad(rsl.getDouble("Had"));
		yzb.setStad(rsl.getDouble("stad"));
		yzb.setMad(rsl.getDouble("mad"));
		yzb.setAar(rsl.getDouble("aar"));
		yzb.setAad(rsl.getDouble("aad"));
		yzb.setVad(rsl.getDouble("Vad"));
		yzb.setT2(rsl.getDouble("T2"));
	}
	
	private String getJiesyzsjy_SelId(long Jiesb_id,long Diancxxb_id){
		String sql="";
		StringBuffer SelId=new StringBuffer("");
		JDBCcon con=new JDBCcon();
		try{
			
			sql="select id from jiesyzsjy where jiesb_id="+Jiesb_id+" and diancxxb_id="+Diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				SelId.append(rsl.getString("id")).append(",");
			}
			rsl.close();
			SelId.deleteCharAt(SelId.length()-1);
			
		}catch(Exception e){
		
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return SelId.toString();
	}
	
	private void getJiesszl(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
//		结算数、质量
		JDBCcon con=new JDBCcon();
		String sql="";
		try{
			
			sql="select * from jiesyzsjy where id in ("+SelIds+") and diancxxb_id="+Diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				SetJicxx(rsl);
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private void computMlj(Interpreter bsh,ResultSetList rsl,Jiesdcz Jscz,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
//		为计算目录价价格赋值
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
				
				Dbljijzb=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
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
					
					yzb.setHetyj(rsl.getDouble("yunj"));			//合同运价单价
					yzb.setHetyjdw(rsl.getString("yunjdw"));		//合同运价单位
					yzb.setHetjgpp_Flag(true);						//合同价格匹配（判断是否取到可用的合同价格）
				}
				
			}while(rsl.next());
			
			bsh.set("品种比价", Jscz.getMljPzbj(yzb.getRanlpzb_id()));
			
			//	政策性加价
			bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id,Locale.zhengcxjj_jies, "0")));
					
			//	加价
			bsh.set(Locale.jiaj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id,Locale.jiaj_jies, "0")));
			
		}catch(EvalError e){
			
			e.printStackTrace();
		}	
	}
	
	private boolean getZengkk(long Hetb_id,Interpreter bsh){
//		增扣款
		JDBCcon con=new JDBCcon();
		try{
			ResultSetList rsl=null;
			
			String sql="select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jis,	\n"
				+ " jisdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,	\n"
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
			String  Strtmp="";		//记录设定的指标
			
			while(rsl.next()){
				
				Dbltmp=Jiesdcz.getYanZZb_info(yzb, rsl.getString("zhib"));
//				指标的结算指标
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					//指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
					
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
					bsh.set(rsl.getString("zhib")+"小数处理",	Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//小数处理（每升高xx或降低xx）
					
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
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ("+Strtmp.substring(0,Strtmp.lastIndexOf(","))+") and leib=1 ";
			}else if(!yzb.getYifzzb().equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ('"+yzb.getYifzzb()+"') and leib=1 ";
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
				bsh.set(rsl.getString("zhib")+"增付单价",		0);					//增付价
				bsh.set(rsl.getString("zhib")+"扣付单价",		0);					//扣价
				bsh.set(rsl.getString("zhib")+"增付价单位", 	"");					//增价单位
				bsh.set(rsl.getString("zhib")+"扣付价单位", 	"");					//扣价单位
				bsh.set(rsl.getString("zhib")+"上限", 		0);						//指标上限
				bsh.set(rsl.getString("zhib")+"下限", 		0);						//指标下限
				bsh.set(rsl.getString("zhib")+"增扣款基数",	0);						//基数（每升高xx或降低xx）
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
	
	private void setJieszb(Interpreter bsh){
		
		try {
			
//			数量增扣款取值
			yzb.setShul_ht(bsh.get("合同标准_结算数量").toString());
			yzb.setShul_yk(Double.parseDouble(bsh.get("盈亏_结算数量").toString()));
			yzb.setShul_zdj(Double.parseDouble(bsh.get("折单价_结算数量").toString()));
			
//			Qnetar
			yzb.setQnetar_ht(bsh.get("合同标准_Qnetar").toString());
			yzb.setQnetar_yk(Double.parseDouble(bsh.get("盈亏_Qnetar").toString()));
			yzb.setQnetar_zdj(Double.parseDouble(bsh.get("折单价_Qnetar").toString()));
			
//			Std
			yzb.setStd_ht(bsh.get("合同标准_Std").toString());
			yzb.setStd_yk(Double.parseDouble(bsh.get("盈亏_Std").toString()));
			yzb.setStd_zdj(Double.parseDouble(bsh.get("折单价_Std").toString()));
			
//			Ad
			yzb.setAd_ht(bsh.get("合同标准_Ad").toString());
			yzb.setAd_yk(Double.parseDouble(bsh.get("盈亏_Ad").toString()));
			yzb.setAd_zdj(Double.parseDouble(bsh.get("折单价_Ad").toString()));
			
//			Vdaf
			yzb.setVdaf_ht(bsh.get("合同标准_Vdaf").toString());
			yzb.setVdaf_yk(Double.parseDouble(bsh.get("盈亏_Vdaf").toString()));
			yzb.setVdaf_zdj(Double.parseDouble(bsh.get("折单价_Vdaf").toString()));
			
//			Mt
			yzb.setMt_ht(bsh.get("合同标准_Mt").toString());
			yzb.setMt_yk(Double.parseDouble(bsh.get("盈亏_Mt").toString()));
			yzb.setMt_zdj(Double.parseDouble(bsh.get("折单价_Mt").toString()));
			
//			Qgrad
			yzb.setQgrad_ht(bsh.get("合同标准_Qgrad").toString());
			yzb.setQgrad_yk(Double.parseDouble(bsh.get("盈亏_Qgrad").toString()));
			yzb.setQgrad_zdj(Double.parseDouble(bsh.get("折单价_Qgrad").toString()));
			
//			Qbad
			yzb.setQbad_ht(bsh.get("合同标准_Qbad").toString());
			yzb.setQbad_yk(Double.parseDouble(bsh.get("盈亏_Qbad").toString()));
			yzb.setQbad_zdj(Double.parseDouble(bsh.get("折单价_Qbad").toString()));
			
//			Had
			yzb.setHad_ht(bsh.get("合同标准_Had").toString());
			yzb.setHad_yk(Double.parseDouble(bsh.get("盈亏_Had").toString()));
			yzb.setHad_zdj(Double.parseDouble(bsh.get("折单价_Had").toString()));
			
//			Stad
			yzb.setStad_ht(bsh.get("合同标准_Stad").toString());
			yzb.setStad_yk(Double.parseDouble(bsh.get("盈亏_Stad").toString()));
			yzb.setStad_zdj(Double.parseDouble(bsh.get("折单价_Stad").toString()));
			
//			Mad
			yzb.setMad_ht(bsh.get("合同标准_Mad").toString());
			yzb.setMad_yk(Double.parseDouble(bsh.get("盈亏_Mad").toString()));
			yzb.setMad_zdj(Double.parseDouble(bsh.get("折单价_Mad").toString()));
			
//			Aar
			yzb.setAar_ht(bsh.get("合同标准_Aar").toString());
			yzb.setAar_yk(Double.parseDouble(bsh.get("盈亏_Aar").toString()));
			yzb.setAar_zdj(Double.parseDouble(bsh.get("折单价_Aar").toString()));
			
//			Aad
			yzb.setAad_ht(bsh.get("合同标准_Aad").toString());
			yzb.setAad_yk(Double.parseDouble(bsh.get("盈亏_Aad").toString()));
			yzb.setAad_zdj(Double.parseDouble(bsh.get("折单价_Aad").toString()));
			
//			Vad
			yzb.setVad_ht(bsh.get("合同标准_Vad").toString());
			yzb.setVad_yk(Double.parseDouble(bsh.get("盈亏_Vad").toString()));
			yzb.setVad_zdj(Double.parseDouble(bsh.get("折单价_Vad").toString()));
			
//			St
			yzb.setT2_ht(bsh.get("合同标准_T2").toString());
			yzb.setT2_yk(Double.parseDouble(bsh.get("盈亏_T2").toString()));
			yzb.setT2_zdj(Double.parseDouble(bsh.get("折单价_T2").toString()));
			
			//结算单价
			yzb.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
			
		} catch (EvalError e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
//	计算费用单批次
	private void computData_Dpc(){
		//计算煤价,热量折价,硫折价,灰熔点折价
		//煤款
		double _Hansmj=yzb.getHansmj();
		double _Jiessl=yzb.getJiessl();
		double _Meiksl=yzb.getMeiksl();
		yzb.setJiessl_sum(yzb.getJiessl_sum()+_Jiessl);
		
		//指标折单价
		double _Qnetar=yzb.getQnetar_zdj();
		double _Std=yzb.getStd_zdj();
		double _Ad=yzb.getAd_zdj();
		double _Vdaf=yzb.getVdaf_zdj();
		double _Mt=yzb.getMt_zdj();
		double _Qgrad=yzb.getQgrad_zdj();
		double _Qbad=yzb.getQbad_zdj();
		double _Had=yzb.getHad_zdj();
		double _Stad=yzb.getStad_zdj();
		double _Mad=yzb.getMad_zdj();
		double _Aar=yzb.getAar_zdj();
		double _Aad=yzb.getAad_zdj();
		double _Vad=yzb.getVad_zdj();
		double _T2=yzb.getT2_zdj();
		double _Shul=yzb.getShul_zdj();
		
		//指标盈亏
		double _Shulyk=yzb.getShul_yk();								//执行合同中的超吨奖励用
		
		double _Jiashj=0;
		double _Jiakhj=0;
		double _Jiaksk=0;
		double _Jine=0;
		double _Buhsmj=0;
		double _Shulzjbz=0;
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
		
		//价格金额计算
		_Jiashj=(double)Math.round(_Hansmj*_Jiessl*100)/100;												//价税合计
		_Jiakhj=(double) Math.round ((_Jiashj)/(1+_Meiksl)*100)/100;										//价款合计
		_Jiaksk=(double)Math.round((_Jiashj-_Jiakhj)*100)/100;												//价款税款
		_Jine=_Jiakhj;																						//金额
		_Buhsmj=(double)Math.round(_Jiakhj/_Jiessl*10000000)/10000000;										//不含税单价
		_Shulzjbz=_Hansmj;
		
		//合计
		_Hej=(double)Math.round((_Jiashj)*100)/100;
		
		//计算盈亏，折价金额
		_Qnetarzje=(double)Math.round(_Qnetar*_Jiessl*100)/100;
		_Stdzje=(double)Math.round(_Std*_Jiessl*100)/100;
		_Adzje=(double)Math.round(_Ad*_Jiessl*100)/100;
		_Vdafzje=(double)Math.round(_Vdaf*_Jiessl*100)/100;
		_Mtzje=(double)Math.round(_Mt*_Jiessl*100)/100;
		_Qgradzje=(double)Math.round(_Qgrad*_Jiessl*100)/100;
		_Qbadzje=(double)Math.round(_Qbad*_Jiessl*100)/100;
		_Hadzje=(double)Math.round(_Had*_Jiessl*100)/100;
		_Stadzje=(double)Math.round(_Stad*_Jiessl*100)/100;
		_Madzje=(double)Math.round(_Mad*_Jiessl*100)/100;
		_Aarzje=(double)Math.round(_Aar*_Jiessl*100)/100;
		_Aadzje=(double)Math.round(_Aad*_Jiessl*100)/100;
		_Vadzje=(double)Math.round(_Vad*_Jiessl*100)/100;
		_T2zje=(double)Math.round(_T2*_Jiessl*100)/100;
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		_Shulzje=(double)Math.round(_Shulzjbz*yzb.getYingksl()*100)/100;	//超过矿发量的盈亏
		
		//结算单显示时指标折金额项
		yzb.setJiashj(yzb.getJiashj()+_Jiashj);
		yzb.setJiakhj(yzb.getJiakhj()+_Jiakhj);
		yzb.setJiaksk(yzb.getJiaksk()+_Jiaksk);
		yzb.setJine(yzb.getJine()+_Jine);
		yzb.setHej(yzb.getHej()+_Hej);
		
		yzb.setQnetar_zje(yzb.getQnetar_zje()+_Qnetarzje);
		yzb.setStd_zje(yzb.getStd_zje()+_Stdzje);
		yzb.setAd_zje(yzb.getAd_zje()+_Adzje);
		yzb.setVdaf_zje(yzb.getVdaf_zje()+_Vdafzje);
		yzb.setMt_zje(yzb.getMt_zje()+_Mtzje);
		yzb.setQgrad_zje(yzb.getQgrad_zje()+_Qgradzje);
		yzb.setQbad_zje(yzb.getQbad_zje()+_Qbadzje);
		yzb.setHad_zje(yzb.getHad_zje()+_Hadzje);
		yzb.setStad_zje(yzb.getStad_zje()+_Stadzje);
		yzb.setMad_zje(yzb.getMad_zje()+_Madzje);
		yzb.setAar_zje(yzb.getAar_zje()+_Aarzje);
		yzb.setAad_zje(yzb.getAad_zje()+_Aadzje);
		yzb.setVad_zje(yzb.getVad_zje()+_Vadzje);
		yzb.setT2_zje(yzb.getT2_zje()+_T2);
		yzb.setShul_zje(yzb.getShul_zje()+_Shulzje);
		
		
		_Qnetar=(double)Math.round(yzb.getQnetar_zje()/yzb.getJiessl_sum()*100)/100;
		_Std=(double)Math.round(yzb.getStd_zje()/yzb.getJiessl_sum()*100)/100;
		_Ad=(double)Math.round(yzb.getAd_zje()/yzb.getJiessl_sum()*100)/100;
		_Vdaf=(double)Math.round(yzb.getVdaf_zje()/yzb.getJiessl_sum()*100)/100;
		_Mt=(double)Math.round(yzb.getMt_zje()/yzb.getJiessl_sum()*100)/100;
		_Qgrad=(double)Math.round(yzb.getQgrad_zje()/yzb.getJiessl_sum()*100)/100;
		_Qbad=(double)Math.round(yzb.getQbad_zje()/yzb.getJiessl_sum()*100)/100;
		_Had=(double)Math.round(yzb.getHad_zje()/yzb.getJiessl_sum()*100)/100;
		_Stad=(double)Math.round(yzb.getStad_zje()/yzb.getJiessl_sum()*100)/100;
		_Mad=(double)Math.round(yzb.getMad_zje()/yzb.getJiessl_sum()*100)/100;
		_Aar=(double)Math.round(yzb.getAar_zje()/yzb.getJiessl_sum()*100)/100;
		_Aad=(double)Math.round(yzb.getAad_zje()/yzb.getJiessl_sum()*100)/100;
		_Vad=(double)Math.round(yzb.getVad_zje()/yzb.getJiessl_sum()*100)/100;
		_T2=(double)Math.round(yzb.getT2_zje()/yzb.getJiessl_sum()*100)/100;
		_Shulzjbz=(double)Math.round(yzb.getShul_zje()/yzb.getJiessl_sum()*100)/100;
		_Buhsmj=(double)Math.round(yzb.getJiakhj()/yzb.getJiessl_sum()*100)/100;
		
		yzb.setQnetar_zdj(_Qnetar);
		yzb.setStd_zdj(_Std);
		yzb.setAd_zdj(_Ad);
		yzb.setVdaf_zdj(_Vdaf);
		yzb.setMt_zdj(_Mt);
		yzb.setQgrad_zdj(_Qgrad);
		yzb.setQbad_zdj(_Qbad);
		yzb.setHad_zdj(_Had);
		yzb.setStad_zdj(_Stad);
		yzb.setMad_zdj(_Mad);
		yzb.setAar_zdj(_Aar);
		yzb.setAad_zdj(_Aad);
		yzb.setVad_zdj(_Vad);
		yzb.setT2_zdj(_T2);
		yzb.setShulzjbz(_Shulzjbz);
		yzb.setBuhsmj(_Buhsmj);
	}
	
//	计算费用加权
	private void computData(){
		//计算煤价,热量折价,硫折价,灰熔点折价
		//煤款
		double _Hansmj=yzb.getHansmj();
		double _Jiessl=yzb.getJiessl();
		double _Meiksl=yzb.getMeiksl();
		
		//指标折单价
		double _Qnetar=yzb.getQnetar_zdj();
		double _Std=yzb.getStd_zdj();
		double _Ad=yzb.getAd_zdj();
		double _Vdaf=yzb.getVdaf_zdj();
		double _Mt=yzb.getMt_zdj();
		double _Qgrad=yzb.getQgrad_zdj();
		double _Qbad=yzb.getQbad_zdj();
		double _Had=yzb.getHad_zdj();
		double _Stad=yzb.getStad_zdj();
		double _Mad=yzb.getMad_zdj();
		double _Aar=yzb.getAar_zdj();
		double _Aad=yzb.getAad_zdj();
		double _Vad=yzb.getVad_zdj();
		double _T2=yzb.getT2_zdj();
		double _Shul=yzb.getShul_zdj();
		
		//指标盈亏
		double _Shulyk=yzb.getShul_yk();		//执行合同中的超吨奖励用
		
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
		
		//价格金额计算
		_Jiashj=(double)Math.round(_Hansmj*_Jiessl*100)/100;												//价税合计
		_Jiakhj=(double) Math.round ((_Jiashj)/(1+_Meiksl)*100)/100;										//价款合计
		_Jiaksk=(double)Math.round((_Jiashj-_Jiakhj)*100)/100;												//价款税款
		_Jine=_Jiakhj;																						//金额
		_Buhsmj=(double)Math.round(_Jiakhj/_Jiessl*10000000)/10000000;										//不含税单价
		_Shulzjbz=_Hansmj;
		
		//计算运费项
//		_Yunzfhj=Math.round((_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf)*100)/100;									//运杂费合计
//		_Yunfsk=(double)Math.round(((double)Math.round(_Tielyf*_Yunfsl*100)/100+_Kuangqsk)*100)/100;		//运费税款		
//		_Buhsyf=(double)Math.round(((double)Math.round((_Yunzfhj-_Yunfsk)*100)/100+_Kuangqjk)*100)/100;	//不含税运费
		
		//合计
		_Hej=(double)Math.round((_Jiashj)*100)/100;
		
		//计算盈亏，折价金额
		_Qnetarzje=(double)Math.round(_Qnetar*_Jiessl*100)/100;
		_Stdzje=(double)Math.round(_Std*_Jiessl*100)/100;
		_Adzje=(double)Math.round(_Ad*_Jiessl*100)/100;
		_Vdafzje=(double)Math.round(_Vdaf*_Jiessl*100)/100;
		_Mtzje=(double)Math.round(_Mt*_Jiessl*100)/100;
		_Qgradzje=(double)Math.round(_Qgrad*_Jiessl*100)/100;
		_Qbadzje=(double)Math.round(_Qbad*_Jiessl*100)/100;
		_Hadzje=(double)Math.round(_Had*_Jiessl*100)/100;
		_Stadzje=(double)Math.round(_Stad*_Jiessl*100)/100;
		_Madzje=(double)Math.round(_Mad*_Jiessl*100)/100;
		_Aarzje=(double)Math.round(_Aar*_Jiessl*100)/100;
		_Aadzje=(double)Math.round(_Aad*_Jiessl*100)/100;
		_Vadzje=(double)Math.round(_Vad*_Jiessl*100)/100;
		_T2zje=(double)Math.round(_T2*_Jiessl*100)/100;
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法
		_Shulzje=(double)Math.round(_Shulzjbz*yzb.getYingksl()*100)/100;	//超过狂发量的盈亏
		
		//结算单显示时指标折金额项
		yzb.setJiashj(_Jiashj);
		yzb.setJiakhj(_Jiakhj);
		yzb.setJiaksk(_Jiaksk);
		yzb.setJine(_Jine);
		yzb.setBuhsmj(_Buhsmj);
		yzb.setShulzjbz(_Shulzjbz);
//		yzb.setYunfsk(_Yunfsk);
//		yzb.setBuhsyf(_Buhsyf);
//		yzb.setYunzfhj(_Yunzfhj);
//		yzb.setYunfsk(_Yunfsk);
//		yzb.setBuhsyf(_Buhsyf);
		yzb.setHej(_Hej);
		yzb.setQnetar_zje(_Qnetarzje);
		yzb.setStd_zje(_Stdzje);
		yzb.setAd_zje(_Adzje);
		yzb.setVdaf_zje(_Vdafzje);
		yzb.setMt_zje(_Mtzje);
		yzb.setQgrad_zje(_Qgradzje);
		yzb.setQbad_zje(_Qbadzje);
		yzb.setHad_zje(_Hadzje);
		yzb.setStad_zje(_Stadzje);
		yzb.setMad_zje(_Madzje);
		yzb.setAar_zje(_Aarzje);
		yzb.setAad_zje(_Aadzje);
		yzb.setVad_zje(_Vadzje);
		yzb.setT2_zje(_T2);
		yzb.setShul_zje(_Shulzje);
	}
}