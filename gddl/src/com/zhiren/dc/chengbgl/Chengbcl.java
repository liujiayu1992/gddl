 /*
 * 创建日期 2008-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.dc.chengbgl;
/**
 * @author Admini_ator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.sql.*;
import java.util.Date;
import java.util.List;

import com.zhiren.common.*;
import com.zhiren.dc.hesgl.jiesd.Balances_cb;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Chengbcl {
	
	public static void CountCb(List LstRuccbb_id,long Diancxxb_id,int Zhuangt){
//		计算成本数据审核
		JDBCcon con=new JDBCcon();
		String strHetb_id="0";
		String strFahb_id="0";
		long lngGongysb_id=0;
		long lngMeikxxb_id=0;
		long lngFaz_id=0;
		long lngHetb_id=0;
		String sql="";
		String strJieslx="";//结算类型
		ResultSetList rsl=null;
		
		if(LstRuccbb_id.size()>0){
			
			StringBuffer sb=new StringBuffer(" begin	\n");
			
			for(int i=0;i<LstRuccbb_id.size();i++){
				
				strFahb_id="0";
				
				if(!String.valueOf(LstRuccbb_id.get(i)).equals("0")&&!String.valueOf(LstRuccbb_id.get(i)).equals("null")){
					
					sql="select id,nvl(hetb_id,0) as hetb_id,nvl(gongysb_id,0) as gongysb_id,nvl(meikxxb_id,0) as meikxxb_id,nvl(faz_id,0) as faz_id from fahb where ruccbb_id in ("+String.valueOf(LstRuccbb_id.get(i))+")";
					rsl=con.getResultSetList(sql);
					while(rsl.next()){
						
						strHetb_id=rsl.getString("hetb_id");
						if(!strHetb_id.equals("0")){
							lngHetb_id=Long.parseLong(strHetb_id);
							strJieslx=getHetjsxs(rsl.getString("hetb_id"));
							if(strJieslx.equals(Locale.jiaqpj_jiesxs)){
//								加权平均,就往ruccbtmp中插入一条记录
								if(strFahb_id.equals("0")){
									
									strFahb_id=rsl.getString("id");
								}else{
									
									strFahb_id+=","+rsl.getString("id");
								}
								lngGongysb_id=rsl.getLong("gongysb_id");
								lngMeikxxb_id=rsl.getLong("meikxxb_id");
								lngFaz_id=rsl.getLong("faz_id");
								
							}else if(strJieslx.equals(Locale.danpc_jiesxs)){
//								单批次,就往ruccbtmp中插入多条记录
								strFahb_id=rsl.getString("id");
								lngGongysb_id=rsl.getLong("gongysb_id");
								lngMeikxxb_id=rsl.getLong("meikxxb_id");
								lngFaz_id=rsl.getLong("faz_id");
								sb.append(Count(strFahb_id, Diancxxb_id, lngGongysb_id, lngMeikxxb_id, lngFaz_id, lngHetb_id, 0, String.valueOf(LstRuccbb_id.get(i))));
							}
						}
					}
					
					if(strJieslx.equals(Locale.jiaqpj_jiesxs)){
//						加权平均
						sb.append(Count(strFahb_id, Diancxxb_id, lngGongysb_id, lngMeikxxb_id, lngFaz_id, lngHetb_id, 0, String.valueOf(LstRuccbb_id.get(i))));
					}
					
				}
			}
			
			sb.append(" end;");
			if(sb.length()>13){
				
				con.getUpdate(sb.toString());
				CountRuccbbtmp(LstRuccbb_id,Zhuangt);
			}
		}
		con.Close();
		if(rsl!=null){
			
			rsl.close();
		}
	}
	
	public static String CountCb_PerFah(List Fahb_id,boolean Flag){
//		此方法为中国大唐Fmis暂估的标准算法
//			传入参数:发货表_id,重算标志Flag 
//			预先定义：针对估收历史表类型（leix）的说明：
//				1、为没有质量时数量暂估，不管经过Flag=true时的几次强制重算，类型不变始终为1
//				2、当该发货有化验值时，如果是第一次估价那么类型为2，
//					如果类型2的记录已存在又要强制重算那么类型为3，如果类型为3还要强制重算那么类型始终为3
//				3、当入账操作时，类型为4，如果强制重算类型还是4
		
//			逻辑关系见流程图		
		String str_ReturnValue="";
		if(Fahb_id.size()>0){
			
			JDBCcon con=new JDBCcon();
			String sql="";
			String checkError="";
			long Hetb_id=0;
			long Diancxxb_id=0;
			long Gongysb_id=0;
			ResultSetList rsl=null;
			StringBuffer sb=new StringBuffer("begin \n");
			
			for(int i=0;i<Fahb_id.size();i++){
				
//				得到发货的基础信息
				sql="select hetb_id,diancxxb_id,gongysb_id from fahb where id="+Fahb_id.get(i);
				rsl=con.getResultSetList(sql);
				if(rsl.next()){
					
					Hetb_id=rsl.getLong("hetb_id");
					Diancxxb_id=rsl.getLong("diancxxb_id");
					Gongysb_id=rsl.getLong("gongysb_id");
				}
				
				if(Hetb_id>0){
//					有合同

//					先检查该发货记录有无质量信息
					sql=" select z.* from fahb f,zhilb z \n"
						+ " where f.zhilb_id=z.id and f.liucztb_id=1 and z.liucztb_id=1 \n"
						+ " 	and f.id="+Fahb_id.get(i);
					
					rsl=con.getResultSetList(sql);
					if(rsl.getRows()>0){
//						存在质量信息
						sql="select f.* from fahb f,guslsb gs where f.id=gs.fahb_id \n"
							+ " and gs.leix=2 and f.liucztb_id=1 and f.id="+Fahb_id.get(i); 
						
						rsl=con.getResultSetList(sql);
						if(rsl.next()){
//							有类型为2的记录
							sql="select * from fahb where jiesb_id>0 and id="+Fahb_id.get(i);
							rsl=con.getResultSetList(sql);
							if(rsl.next()){
//								有结算表id
								sql="select * from fahb f,guslsb gs where f.id=gs.fahb_id \n"
									+ " and gs.leix=4 and f.liucztb_id=1 and f.id="+Fahb_id.get(i);
								rsl=con.getResultSetList(sql);
								if(rsl.next()){
//									有leix=4的记录
									if(Flag){
//										重新计算估收单价，insert历史表一条leix=4的记录
										checkError=CountZg_Rz(Long.parseLong(Fahb_id.get(i).toString()),Diancxxb_id,4).toString();
										if(checkError.indexOf("Error")>-1){
											
											str_ReturnValue=checkError;
//											return str_ReturnValue;
										}else{
											
											sb.append(checkError);
										}
										
									}else{
										
										str_ReturnValue="已有历史记录!";
//										return str_ReturnValue;
									}
								}else{
//									以前没有做过入账暂估重算，重新计算估收单价，insert历史表一条leix=4的记录
//									前提，电厂在结算入账时要求煤款和运费同时入账，且煤款只能结给一家，运费也只能结给一家
									checkError=CountZg_Rz(Long.parseLong(Fahb_id.get(i).toString()),Diancxxb_id,4).toString();
									if(checkError.indexOf("Error")>-1){
										
										str_ReturnValue=checkError;
//										return str_ReturnValue;
									}else{
										
										sb.append(checkError);
									}
								}
								
							}else{
//								没有结算表id
								if(Flag){
//									重算
									checkError=CountZg_Yzl(Gongysb_id,Long.parseLong(Fahb_id.get(i).toString()),Diancxxb_id,Hetb_id,3).toString();
									if(checkError.indexOf("Error")>-1){
										
										str_ReturnValue=checkError;
//										return str_ReturnValue;
									}else{
										
										sb.append(checkError);
									}
								}else{
									
									str_ReturnValue="已有历史记录!";
//									return str_ReturnValue;
								}
							}
							
						}else{
//							没有类型为2的记录,重新计算估收单价，insert历史表一条leix=2的记录
							checkError=CountZg_Yzl(Gongysb_id,Long.parseLong(Fahb_id.get(i).toString()),Diancxxb_id,Hetb_id,2).toString();
							if(checkError.indexOf("Error")>-1){
								
								str_ReturnValue=checkError;
//								return str_ReturnValue;
							}else{
								
								sb.append(checkError);
							}
						}
						
					}else{
//						不存在质量信息
						sql="select gs.* from fahb f,guslsb gs\n" +
							"       where f.id=gs.fahb_id\n" + 
							"             and f.liucztb_id=1\n" + 
							"             and gs.leix=1 and f.id="+Fahb_id.get(i);
						rsl=con.getResultSetList(sql);
						if(rsl.getRows()>0){
//							有leix=1的记录
							if(Flag){
//								重算标志为true，重新计算估收单价
//								首次计算暂估单价（没有质量计算暂估）
								checkError=CountZg_Mzl(Gongysb_id,Long.parseLong(Fahb_id.get(i).toString()),Diancxxb_id,Hetb_id).toString();
								if(checkError.indexOf("Error")>-1){
									
									str_ReturnValue=checkError;
//									return str_ReturnValue;
								}else{
									
									sb.append(checkError);
								}
								
							}else{
								
								str_ReturnValue="已有历史记录!";
//								return str_ReturnValue;
							}
						}else{
							
//							首次计算暂估单价（没有质量计算暂估）
							checkError=CountZg_Mzl(Gongysb_id,Long.parseLong(Fahb_id.get(i).toString()),Diancxxb_id,Hetb_id).toString();
							if(checkError.indexOf("Error")>-1){
								
								str_ReturnValue=checkError;
//								return str_ReturnValue;
							}else{
								
								sb.append(checkError);
							}
						}
					}
				}
				
			}
			
			sb.append("end;");
			if(rsl!=null){
				
				rsl.close();
			}
			
			if(sb.length()>13){
				
				if(con.getInsert(sb.toString())>-1){
					
					str_ReturnValue="暂估数据生成成功!";
				}else{
					
					str_ReturnValue="操作暂估表时出错!";
				}
			}
			con.Close();
		}
		
		return str_ReturnValue;
	}
	
	private static StringBuffer CountZg_Mzl(long Gongysb_id,long Fahb_id,long Diancxxb_id,long Hetb_id){
//		首次计算暂估单价（没有质量计算暂估）
		double jiesrl=0;
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		String sql="";
		StringBuffer sb=new StringBuffer();
		Balances_cb bls=new Balances_cb();
		Balances_variable bsv=new Balances_variable();
		
		try{
			
			sql="select jiesrl from jiesb j\n" +
				"       where j.gongysb_id="+Gongysb_id+"	\n" + 
				"             and j.ruzrq is not null	\n" + 
				"			  and j.diancxxb_id="+Diancxxb_id+" \n" +
				"             and rownum=1	\n" + 
				"             order by ruzrq desc";
			rsl=con.getResultSetList(sql);
			if(rsl.next()){
	//			从最近一次结算中取结算热量
				jiesrl=MainGlobal.Mjkg_to_kcalkg(rsl.getDouble("jiesrl"), 0);
			}else{
//				从合同质量要求中取热量要求
				sql="select zl.xiax from hetb h,hetzlb zl,zhibb zb\n" +
					"                    where h.id=zl.hetb_id			\n" + 
					"                          and zl.zhibb_id=zb.id	\n" + 
					"						   and zb.bianm='"+Locale.Qnetar_zhibb+"'	\n" +
					"                          and h.id=\n" + 
					"             (select f.hetb_id from fahb f where id="+Fahb_id+")\n" + 
					"             order by xiax";
				rsl=con.getResultSetList(sql);
				if(rsl.next()){
					
					jiesrl=MainGlobal.Mjkg_to_kcalkg(rsl.getDouble("xiax"), 0);
				}else{
					
					sb.append("Error:没有从合同质量中找到对发热量的要求!");
				}
			}
			if(jiesrl>0){
				
	//			暂估计算
				bsv=bls.getBalanceData(bsv, String.valueOf(Fahb_id), Diancxxb_id, Hetb_id, 1, Gongysb_id, jiesrl);
	//			生成guslsb的insert语句
				if(bsv.getHansmj()>0){
					
					sb.append(getInsertGslsb(bsv,Diancxxb_id,1,DateUtil.FormatDate(bsv.getFahksrq())));
				}
			}
			
			if(rsl!=null){
				
				rsl.close();
			}
		}catch(SQLException s){
			
			s.printStackTrace();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
			bsv=null;
			bls=null;
		}
		
		return sb;
	}
	
	//计算有质量的价格
	private static StringBuffer CountZg_Yzl(long Gongysb_id,long Fahb_id,long Diancxxb_id,long Hetb_id,int leix){
//		首次计算暂估单价（没有质量计算暂估）
		double jiesrl=0;
		StringBuffer sb=new StringBuffer();
		Balances_cb bls=new Balances_cb();
		Balances_variable bsv=new Balances_variable();
		try{
				
//			暂估计算
			bsv=bls.getBalanceData(bsv, String.valueOf(Fahb_id), Diancxxb_id, Hetb_id, 1, Gongysb_id, jiesrl);
//			生成guslsb的insert语句
			sb.append(getInsertGslsb(bsv,Diancxxb_id,leix,DateUtil.FormatDate(new Date())));
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			bsv=null;
			bls=null;
		}
		
		return sb;
	}
	
//	计算入账后的价格
	private static StringBuffer CountZg_Rz(long Fahb_id,long Diancxxb_id,int leix){
//		在入账时确定该发货的最后成本（从jiesb和jiesyfb中取值）
		StringBuffer sb=new StringBuffer();
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		String sql="";
		try{
			long jiesb_id=0;
			long yunfjsb_id=0;
			
			double jiesrl=0;
			String hetbh="";
			double hetj=0;
			String hetbz="";
			double zhedj=0;
			double meij=0;
			double meis=0;
			String riq="";
			double yunf=0;
			double zaf=0;
			double fazzf=0;
			double yunfs=0;
			
			sql="select distinct jiesb_id from fahb f,chepb c where f.id=c.fahb_id and f.id="+Fahb_id;
			rsl=con.getResultSetList(sql);
			if(rsl.next()){
//				得到煤款结算id
				jiesb_id=rsl.getLong("jiesb_id");
			}
//			如果存在一个发货对应两个运费结算，可能会有差异
			sql="select distinct dj.yunfjsb_id from danjcpb dj,chepb c,yunfdjb yd\n" +
				"       where dj.chepb_id=c.id\n" + 
				"             and dj.yunfdjb_id=yd.id\n" + 
				"             and yd.feiylbb_id="+Locale.guotyf_feiylbb_id+"\n" + 
				"             and c.fahb_id="+Fahb_id;
			rsl=con.getResultSetList(sql);
			if(rsl.next()){
//				得到运费结算id
				yunfjsb_id=rsl.getLong("yunfjsb_id");
			}
			
			sql="select j.jiesrl,nvl(h.hetbh,'合同已丢失') as hetbh,j.hetj,\n" +
				"       nvl(zb.hetbz,'无热量合同标准') as hetbz,\n" + 
				"       nvl(zb.zhejbz,0) as zhedj,\n" + 
				"       round_new(j.buhsmk/j.jiessl,2) as meij,\n" + 
				"       round_new(j.shuik/j.jiessl,2) as meis,\n" + 
				"       to_char(j.ruzrq,'yyyy-MM-dd') as riq\n" + 
				"       from jiesb j,hetb h,jieszbsjb zb\n" + 
				"       where j.hetb_id=h.id(+)\n" + 
				"             and j.id=zb.jiesdid\n" + 
				"             and zb.zhibb_id=2\n" + 
				"             and j.id="+jiesb_id;
			rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				jiesrl=MainGlobal.Mjkg_to_kcalkg(rsl.getDouble("jiesrl"),0);
				hetbh=rsl.getString("hetbh");
				hetj=rsl.getDouble("hetj");
				hetbz=rsl.getString("hetbz");
				zhedj=rsl.getDouble("zhedj");
				meij=rsl.getDouble("meij");
				meis=rsl.getDouble("meis");
				riq=rsl.getString("riq");
			}
			
			if(jiesb_id>0||yunfjsb_id>0){
				
				sql="select round_new((jy.hansyf-jy.guotzf-jy.kuangqzf-jy.kuangqyf-jy.shuik)/jy.jiessl,2) as yunf,\n" +
					"       round_new(jy.guotzf/jy.jiessl,2) as zaf,\n" + 
					"       round_new((jy.kuangqyf+jy.kuangqzf)/jy.jiessl,2) as fazzf,round_new(jy.shuik/jy.jiessl,2) as yunfs,\n" + 
					"       to_char(jy.ruzrq,'yyyy-MM-dd') as riq\n" + 
					"       from jiesyfb jy\n" + 
					"       where id="+yunfjsb_id;
				
				rsl=con.getResultSetList(sql);
				if(rsl.next()){
					
					yunf=rsl.getDouble("yunf");
					zaf=rsl.getDouble("zaf");
					fazzf=rsl.getDouble("fazzf");
					yunfs=rsl.getDouble("yunfs");
					riq=rsl.getString("riq");
				}
				if(!riq.equals("")){
					
					sb.append("insert into guslsb\n" +
							"  (id, fahb_id, rez, leix, heth, hetjg, hetbz, hetzk, meij, meis, yunf, zaf, fazzf, ditf, yunfs, zhuangt, riq)\n" + 
							"values\n" + 
							"  (getnewid("+Diancxxb_id+"), "+Fahb_id+", "+jiesrl+", "+leix+", '"+hetbh+"', " +
							" "+hetj+", '"+hetbz+"', "+zhedj+", "+meij+", " +
							" "+meis+", "+yunf+", " +
							" "+zaf+", "+fazzf+", 0, "+yunfs+", 0, to_date('"+riq+"','yyyy-MM-dd')); \n");
				}
			}
			
			if(rsl!=null){
				
				rsl.close();
			}
		}catch(Exception e){
			sb.append("Error:SQL");
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return sb;
	}
	
	private static StringBuffer getInsertGslsb(Balances_variable bsv, long diancxxb_id, int leix, String riq) {
		// TODO 自动生成方法存根
		StringBuffer sb=new StringBuffer();
		
		try{
			sb.append("insert into guslsb\n" +
					"  (id, fahb_id, rez, leix, heth, hetjg, hetbz, hetzk, meij, meis, yunf, zaf, fazzf, ditf, yunfs, zhuangt, riq)\n" + 
					"values\n" + 
					"  (getnewid("+diancxxb_id+"), "+bsv.getSelIds()+", "+MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0)+", "+leix+", '"+bsv.getHetbh()+"', " +
					" "+bsv.getHetmdj()+", '"+bsv.getQnetar_ht()+"', "+bsv.getQnetar_zdj()+", "+CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),2)+", " +
					" "+CustomMaths.Round_new(bsv.getJiaksk()/bsv.getJiessl(),2)+", "+CustomMaths.Round_new((bsv.getYunzfhj()-bsv.getTielzf()-bsv.getKuangqzf()-bsv.getKuangqyf()-bsv.getYunfsk())/bsv.getYunfjsl(),2)+", " +
					" "+CustomMaths.Round_new(bsv.getTielzf()/bsv.getYunfjsl(), 2)+", "+CustomMaths.Round_new((bsv.getKuangqyf()+bsv.getKuangqzf())/bsv.getYunfjsl(),2)+", 0, "
					+CustomMaths.Round_new(bsv.getYunfsk()/bsv.getYunfjsl(),2)+", 0, to_date('"+riq+"','yyyy-MM-dd')); \n");
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		return sb;
	}

	public static void CountCb_js(String Jiesdwid,long Jiesb_id,long Yunfjsb_id){
		
		StringBuffer sb=new StringBuffer("");
		String sql="";
		JDBCcon con=new JDBCcon();
		ResultSetList rsl = new ResultSetList();
		try{
			
			double meij=0;
			double meijs=0;
			double qnet_ar=0;
			double yunj=0;
			double yunjs=0;
			double jiaohqzf=0;
			String strTmp="";
			String strFinalArray[]=null;
			String where_ruccbb_continue="";
			
			if(Jiesb_id>0||Yunfjsb_id>0){
				
				if(!Jiesdcz.isFengsj(Jiesdwid)){
//					不等于分公司结算才更新成本，否则不更新成本
//					两票结算
					sb.append(" update ruccb set ");
					if(Jiesb_id>0){
						
						sql=" select round_new(decode(jiessl,0,0,hansmk/jiessl),2) as hansdj,	\n"
							+ " round_new(decode(jiessl,0,0,shuik/jiessl),2) as shuik,		\n"
							+ " jiesrcrl \n" 
							+ " from jiesb where id="+Jiesb_id;
						rsl=con.getResultSetList(sql);
						if(rsl.next()){
							
							meij=rsl.getDouble("hansdj");
							meijs=rsl.getDouble("shuik");
							qnet_ar=rsl.getDouble("jiesrcrl");
							
							sb.append("meij=").append(meij).append(",meijs=").append(meijs).append(",qnet_ar=").append(qnet_ar);
						}
					}
					if(Yunfjsb_id>0){
						
						sql=" select round_new(hansyf/jiessl,2) as yunj," +
								" round_new(shuik/jiessl,2) as yunjs," +
								" round_new((kuangqyf+kuangqzf)/jiessl,2) as jiaohqzf " +
								" from jiesyfb where id="+Yunfjsb_id;
						
						rsl=con.getResultSetList(sql);
						if(rsl.next()){
							
							yunj=rsl.getDouble("yunj");
							yunjs=rsl.getDouble("yunjs");
							jiaohqzf=rsl.getDouble("jiaohqzf");
							
							if(Jiesb_id>0){
								
								sb.append(",").append("yunj=").append(yunj).append(",").append("yunjs=")
									.append(yunjs).append(",").append("jiaohqzf=").append(jiaohqzf);
							}else{
								
								sb.append("yunj=").append(yunj).append(",").append("yunjs=")
									.append(yunjs).append(",").append("jiaohqzf=").append(jiaohqzf);
							}
						}
					}
					
					strTmp = getRuccbb_id(Jiesb_id,Yunfjsb_id);
					strFinalArray = Jiesdcz.getFenzzfc(strTmp, ",", 1000);	//得到分组字符串
					
					if(strFinalArray!=null){
						
						where_ruccbb_continue="";
//						说明有分组
						for(int i=0;i<strFinalArray.length;i++){
							
							if(i==0){
								
								where_ruccbb_continue=" id in ("+strFinalArray[i]+")";
							}else{
								
								where_ruccbb_continue=" or id in ("+strFinalArray[i]+")";
							}
							
						}
					}
					
					if(!where_ruccbb_continue.equals("")){
						
						sb.append(" where (").append(where_ruccbb_continue).append(")");
					}else{
						
						sb.append(" where id in(").append(getRuccbb_id(Jiesb_id,Yunfjsb_id)).append(")");
					}
					con.getUpdate(sb.toString());
				}
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private static String getRuccbb_id(long jiesb_id, long yunfjsb_id) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		StringBuffer sb=new StringBuffer();
		String sql="";
		ResultSet rs=null;
		try{
			
			if(jiesb_id>0){
				
				sql="select nvl(ruccbb_id,0) as ruccbb_id from fahb where jiesb_id="+jiesb_id;
				rs=con.getResultSet(sql);
				while(rs.next()){
					
					sb.append(rs.getLong("ruccbb_id")).append(",");
				}
			}
			
			if(yunfjsb_id>0){
				
				sql="select ruccbb_id	\n" 
					+ "	from fahb f,chepb cp,danjcpb dj		\n"
					+ " where f.id=cp.fahb_id and cp.id=dj.chepb_id	\n"
					+ " and dj.yunfjsb_id="+yunfjsb_id; 
             
				rs=con.getResultSet(sql);
				while(rs.next()){
					
					sb.append(rs.getLong("ruccbb_id")).append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return sb.toString();
	}

	private static void CountRuccbbtmp(List lstRuccbb_id,int Zhuangt) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		String sql="";
		String Ruccbb_id="";
		ResultSetList rsl=null;
		
		try{
			StringBuffer sb=new StringBuffer("begin	\n");
			for(int i=0;i<lstRuccbb_id.size();i++){
				
				if(Ruccbb_id.equals("")){
					
					Ruccbb_id=String.valueOf(lstRuccbb_id.get(i));
				}else{
					
					Ruccbb_id=Ruccbb_id+","+String.valueOf(lstRuccbb_id.get(i));
				}
				
				sql=" select  	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*meij)/sum(meijssl),2)) as meij,	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*meijs)/sum(meijssl),2)) as meijs,	\n"
					+ " decode(sum(nvl(yunfjssl,0)),0,0,round_new(sum(yunfjssl*yunj)/sum(yunfjssl),2)) as yunj,	\n"
					+ " decode(sum(nvl(yunfjssl,0)),0,0,round_new(sum(yunfjssl*yunjs)/sum(yunfjssl),2)) as yunjs,	\n"
					+ " decode(sum(nvl(yunfjssl,0)),0,0,round_new(sum(yunfjssl*jiaohqzf)/sum(yunfjssl),2)) as jiaohqzf,	\n"
					+ " decode(sum(nvl(yunfjssl,0)),0,0,round_new(sum(yunfjssl*zaf)/sum(yunfjssl),2)) as zaf,	\n"
					+ " decode(sum(nvl(yunfjssl,0)),0,0,round_new(sum(yunfjssl*daozzf)/sum(yunfjssl),2)) as daozzf,	\n"
					+ " decode(sum(nvl(yunfjssl,0)),0,0,round_new(sum(yunfjssl*qitfy)/sum(yunfjssl),2)) as qitfy,	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*qnet_ar)/sum(meijssl),2)) as qnet_ar,	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*relzj)/sum(meijssl),2)) as relzj,	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*liuzj)/sum(meijssl),2)) as liuzj,	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*huifzj)/sum(meijssl),2)) as huifzj,	\n"
					+ " decode(sum(nvl(meijssl,0)),0,0,round_new(sum(meijssl*shuifzj)/sum(meijssl),2)) as shuifzj	\n"
					+ " from ruccbtmp	\n" 
					+ " where ruccbb_id="+String.valueOf(lstRuccbb_id.get(i));
				
				rsl=con.getResultSetList(sql);
				if(rsl.next()){
					
					sb.append("	 update ruccb set meij =").append(rsl.getDouble("meij")).append(",").append(" meijs =").append(rsl.getDouble("meijs")).append(",");
					sb.append(" 	yunj =").append(rsl.getDouble("yunj")).append(",").append("yunjs =").append(rsl.getDouble("yunjs")).append(",");
					sb.append("		jiaohqzf =").append(rsl.getDouble("jiaohqzf")).append(",").append(" zaf =").append(rsl.getDouble("zaf")).append(",");
					sb.append("		daozzf =").append(rsl.getDouble("daozzf")).append(",").append(" qitfy =").append(rsl.getDouble("qitfy")).append(",");
					sb.append("		qnet_ar =").append(rsl.getDouble("qnet_ar")).append(",").append("zhuangt =").append(Zhuangt).append(",");
					sb.append("		relzj =").append(rsl.getDouble("relzj")).append(",").append("liuzj =").append(rsl.getDouble("liuzj")).append(",");
					sb.append(" 	huifzj =").append(rsl.getDouble("huifzj")).append(",").append(" shuifzj =").append(rsl.getDouble("shuifzj"));
					sb.append("		where id =").append(lstRuccbb_id.get(i)).append(";	\n");
				}
			}
			
			sb.append("end;");
			con.getUpdate(sb.toString());
			con.getDelete("delete from ruccbtmp");
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}

	private static String getHetjsxs(String strHetb_id) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		String Hetjsxs="";
		try{
			
			String sql="select distinct jsxs.bianm as jsxs 	\n"
				+ " from hetjgb htjg,hetb ht,hetjsxsb jsxs	\n" 
				+ " where htjg.hetb_id=ht.id and htjg.hetjsxsb_id=jsxs.id	\n"
	            + "		and ht.id="+strHetb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				Hetjsxs=rsl.getString("jsxs");
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Hetjsxs;
	}
	
	private static double[] getYunf(Balances_variable bsv,String Fahb_id){
//		计算运费，如果运费大于0直接返回
//				 如果等于0从上次结算单中取出价格
		JDBCcon con=new JDBCcon();
		long lngJiesyfb_id=0;
		String sql="";
		double dblYunf[]={0,0,0};//0 运费，1 运费税,	2 交货前杂费
		
		if(bsv.getHetyj()==0){
			
			ResultSetList rsl=null;
				
			sql="select max(id) as id from jiesyfb where gongysmc='"+bsv.getFahdw()+"' and faz='"+bsv.getFaz()+"'";
			rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				if(rsl.getString("id")==null||rsl.getString("id").equals("")){
					
					
				}else{
					
					lngJiesyfb_id=rsl.getLong("id");
				}
			}
			
			if(lngJiesyfb_id>0){
				
				sql="select * from jiesyfb where id="+lngJiesyfb_id;
				rsl=con.getResultSetList(sql);
				if(rsl.next()){
					
					dblYunf[0]=bsv.getYunfjsl()==0?(double)Math.round((rsl.getDouble("guotyf")+rsl.getDouble("dityf"))/bsv.getGongfsl()*100)/100:(double)Math.round((rsl.getDouble("guotyf")+rsl.getDouble("dityf"))/bsv.getYunfjsl()*100)/100;
					dblYunf[1]=bsv.getYunfjsl()==0?(double)Math.round(rsl.getDouble("shuik")/bsv.getGongfsl()*100)/100:(double)Math.round(rsl.getDouble("shuik")/bsv.getYunfjsl()*100)/100;
					dblYunf[2]=bsv.getYunfjsl()==0?(double)Math.round(rsl.getDouble("ditzf")/bsv.getGongfsl()*100)/100:(double)Math.round(rsl.getDouble("ditzf")/bsv.getYunfjsl()*100)/100;
				}
			}
			rsl.close();
		}else{
			
			dblYunf[0]=bsv.getHetyj();
			dblYunf[1]=(double)Math.round(bsv.getHetyj()*bsv.getYunfsl()*100)/100;
			dblYunf[2]=getJiaohqzf(bsv.getFahdw(),bsv.getFaz(),Fahb_id);
		}
		
		con.Close();
		return dblYunf;
	}

	private static double getJiaohqzf(String Gongys, String Faz, String Fahb_id) {
		// TODO 自动生成方法存根
//		矿区地铁运费
		JDBCcon con=new JDBCcon();
		
		double dblDityf=0;
		double dblDitzf=0;
		double dblGongfsl=0;
		try{
			
			String sql="select * from jiesyfb where gongysmc='"+Gongys+"' and faz='"+Faz+"'";
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				dblDityf=rsl.getDouble("dityf");
				dblDitzf=rsl.getDouble("ditzf");
				dblGongfsl=rsl.getDouble("jiessl");
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		if(dblGongfsl==0){
			
			return dblGongfsl=0;
		}else{
			
			return (double)Math.round((dblDityf+dblDitzf)/dblGongfsl*100)/100;
		}
	}

	private static String Count(String strFahb_id, long Diancxxb_id, long Gongysb_id, long Meikxxb_id, long Faz_id, long Hetb_id, int Zhuangt, String Ruccbb_id) {
		// TODO 自动生成方法存根
//		结算成本
		Balances_cb bls=new Balances_cb();
		Balances_variable bsv=new Balances_variable();
		String sql="";
		double dblYunf[]=null;
		try{
			
			bsv=bls.getBalanceData(bsv, strFahb_id, Diancxxb_id, Hetb_id, Locale.liangpjs_feiylbb_id, Gongysb_id, "否", "", 0,0);
			
			dblYunf=getYunf(bsv,strFahb_id);
			
			if(bsv.getHansmj()>0){
				
				sql="insert into ruccbtmp(id, ruccbb_id, meijssl, meij, meijs, yunfjssl, yunj, yunjs, jiaohqzf, zaf, daozzf, qitfy, qnet_ar, relzj, liuzj, huifzj, shuifzj)	\n"
					+ " values	\n"
					+ " (getnewid("+Diancxxb_id+"), "+Ruccbb_id+","+bsv.getJiessl()+", "+bsv.getHansmj()+", "+CustomMaths.sub(bsv.getHansmj(),CustomMaths.Round_new((bsv.getHansmj()/(1+bsv.getMeiksl())),2))+", "
						+bsv.getYunfjsl()+", "+dblYunf[0]+", "+dblYunf[1]+", "+dblYunf[2]+", 0, 0, 0, "+bsv.getQnetar_cf()+", "+bsv.getQnetar_zdj()+", "+bsv.getStd_zdj()+", "+bsv.getAar_zdj()+", "+bsv.getMt_zdj()+");	\n";
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		return sql;
	}
}
