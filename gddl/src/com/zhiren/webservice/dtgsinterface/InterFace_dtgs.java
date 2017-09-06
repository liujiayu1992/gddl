package com.zhiren.webservice.dtgsinterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;


public class InterFace_dtgs { 
	
	private static final String error000="1,000,接收成功";
//	private static final String error001="-1,001,XMLData数据转换为gb312编码时出错";
//	private static final String error002="-1,002,文档不符合dom规范,可能是发送配置表中sql的字段为函数时没有写别名";
	private static final String error003="-1,003,插入异常，请检查发送接收的配置，例如发送数据的日期时间类型必须用格式字符串，接收端编码类型字段的配置等或远程数据库编码重复或数据库连接失败";//
//	private static final String error004="-1,004,远程执行sql时数据库连接失败";
	
	private static final String error005="-1,005,远程执行sql时出错，可能是客户端生成的sql语句不符合规范，请检查发送配置表";
	
	private static final String error007="1,007,删除远程数据时没有找到主键字段名称，可能是远程接收配置没有设置主键名称!";//删除不成功时，下次不再执行
	private static final String error008="1,008,更新0条数据，也就是远程数据与本地数据没有同步";
	
//	private static final String error009="-1,009,用户名称不在，你的电厂还没有在集团注册用户";
//	private static final String error010="-1,010,用户账户的密码错误请检查系统密码设置";
	private static final String error011="-1,011,接收数据库的接收配置有不识别的类型";
	private static final String error013="-1,013,发送端与接收端配置的字段个数不一致。";
	private static final String error014="-1,014,插入删除时的未知异常。";
	
	
//	private static final String rizPath="d:/发送数据查看";// 发送日志路径
//	private static final String error006="-1,006,远程webservice服务出现未知问题，可能是webservice部署失败。！";
//	private static final String error101="-1,101,网络连接失败，或url不能定位到服务！";
//	private static final String error102="-1,102,电厂数据库连接配置有问题！";
	private static final String error103="-1,103,任务发送配置sql语句出错！";//
//	private static final String error104="-1,104,本地web服务部署失败！";
	

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
				
//				使用集团统一的电厂ID
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
					renwbs=rwrs.getString("renwbs");//集团唯一任务标识id
					id0=rwrs.getString("id0");//任务id
					shujjl=rwrs.getString("shujjl");
					tiaoj=rwrs.getString("tiaoj");
					shujjl = shujjl.replaceAll("%%", tiaoj);

					if(renwlx.equals("2")){//因为插入前都要删除、所以修改与增加是一样的都是先删除后新增
				    	renwlx="0";
				    }
					dcdatarsl = dbcn.getResultSetList(shujjl);//取得任务数据
					if(dcdatarsl.next()){
						dcdatarsl.beforefirst();
						message = insertData(mdiancxxb_id,dcdatarsl,renwmc,renwbs,renwlx,renwrq);
					}else{
						message = error103;
					}
					Xierz(dbcn,message,id0);
				}
				rwrs.close();
				
//			数据更新任务
				StringBuffer sb=new StringBuffer();
				updatesql=CreateSql(dbcn,sb,renwmc,mdiancxxb_id);
				for (int ii=0;ii<updatesql.size();ii++){
					int flag = con.getUpdate(updatesql.get(ii).toString());//写日志
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
			// TODO 自动生成 catch 块
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
			if(renwlx.equals("0")){//0:增加1:删除 如果为增加操作
				//插入：1,生成表头(id,mingc...)2,读取数据记录组织成valus3,形成插入sql
				//表头在读配置信息时顺便生成
				//先删除后增加
				//增加时使用事务删除时不使用事务
				
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
				if( dcrsl.getColumnCount()!=rs.getRows()){//发送端与接收端字段数量配置不一致
					 message=error013;
					 return message;
				}
				
				for (int i = 0; dcrsl.next(); i++) {//数据记录(一般只有一个)
					bufferValus.setLength(0);
					bufferValus.append("(");
					for(int k=0;k<dcrsl.getColumnCount();k++){//2构造values,验证数据（编码验证和空验证）
						//根据字段配置的类型处理响应字段数据
						String leix = rs.getString(k,"leix");
						if(k>0){
							bufferValus.append(",");
						}
						//可以进行编码验证和空验证
						String str= jiaoy(con,rs.getString(k,"zidmc"),rs.getString(k,"zhuanmSql"),rs.getString(k,"weik"),dcrsl.getString(k),leix);// 字段名称、字段编码、字段为空
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
							bufferValus.append("("+rs.getString(k,"zhuanmSql").replaceAll("%%", "'"+dcrsl.getString(k)+"'")+")");//替换参数
						}else{//不识别的类型
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
				//删除操作：形成删除sql
				 sql=
					"delete from "+renwmc+" where "+rs.getString(0,"zhujmc")+"="+diancxxb_id+renwbs;
				 int count=con.getDelete(sql);
				 if(count==-1){//"-1,003,"+str+""删除"+count+"行数据
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
			message=error003;//　
			return message;
		}catch(Exception e3){
			con.rollBack();
			message=error014;//　
		}
		finally{
			  
			  Xiejsrzb(con,"",renwmc,renwbs,message,renwlx,renwrq,diancxxb_id);
			  con.Close();
		}
		return message;

	}
	
	private String jiaoy(JDBCcon cn,String zidmc,String zhuanmsql,String zidwk,String data,String leix) throws SQLException{//先进行空验证再进行编码验证
		String resultStr="";
		if((zidwk.equals("")||zidwk==null)&&(data.equals("")||data==null)){//如果不允许为空,数据还为空则返回错误
			resultStr=zidmc+"不允许为空";
			return resultStr;
		}
//		JDBCcon con=new JDBCcon();
		try{
			if(!zhuanmsql.equals("")&&!data.equals("")&&data!=null){//如果是有转码，并有转码数据
				if("bianm".equals(leix)){//
					ResultSetList rs=cn.getResultSetList(zhuanmsql+"'"+data+"'");
					if(!rs.next()){
						resultStr=zidmc+"字段的"+data+"编码不能识别";
						return resultStr;
					}
				}else{//decode
					if(zidwk.equals("")){//不如果是必填项才校验
						ResultSetList rs=cn.getResultSetList(zhuanmsql.replaceAll("%%", "'"+data+"'"));
						if(rs.next()){
							if(rs.getString(0)==null||rs.getString(0).equals("")){
								resultStr=zidmc+"字段的"+data+"在数据库中找不到对应的值，也就是说在远程数据库中昨日库存为空";
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

	private void Xiejsrzb(JDBCcon cn,String zhangh_id,String renw,String renwbs,String message,String caoz,String renwrq,String diancxxb_id){//写接收日志表
		String[]dat=message.split(",");
//		if(dat[0].equals("-1")){//如果为错误
//			JDBCcon con=new JDBCcon();
//			System.out.print(renwrq);
			String sql = "insert into jiekjsrzb(id,diancxxb_id,renw,renwbs,shij,cuowdm,cuowxx,caoz,zhixzt,riq)values(\n"
				+ "xl_diancxxb_id.nextval,"+diancxxb_id+",'"+renw+"',"+renwbs+",sysdate,'"+dat[1]+"','"+dat[2]+"','"+caoz+"',"+dat[0]+",to_date('"+renwrq+"','yyyy-mm-dd hh24:mi:ss'))";
			cn.getInsert(sql);
//			cn.Close();
//		}
	}
	
	private void Xierz(DBconn dbcn,String data,String id0){//写日志
		//"1,'000','接收成功'";
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
	
	private void Xierzexe(DBconn dbcn,String data,String in){//执行日志
		//"1,'000','接收成功'";
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
				if(kk==0||(biaom.equals(biaom_p)&&gengxy.equals(gengxy_p)&&gengxyz.equals(gengxyz_p)&&renwbs.equals(renwbs_p))){//如果当前值上一个相等或第一个值则说明为同一个分组
					//追加in后的（。。。）
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
				}else{ //不相等则产生一个新分组。
					in+=")";
					resultSql.add("update "+biaom_p+" set "+gengxy_p+"'"+gengxyz_p+"' where "+renwbs_p+in);
					//初始化各个变量
					kk=1;
					in="(";
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					in+=diancid+renwbsz;
				}
			}
			//最后一个分组
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