package com.zhiren.webservice;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

/*
 * 作者：王伟
 * 时间：2010-08-24
 * 描述：添加参数配置解决脱离qichjcsjppb和qichysdwppb表的配置也可以自动导入数据
 * 		"SELECT zhi FROM xitxxb WHERE mingc='导入数据信息匹配' AND beiz='使用'"
 */
/*
 * 作者：夏峥
 * 时间：2011-12-01
 * 适用范围：国电电力汽车衡过衡单位
 * 描述：匹配时供应商只使用类型为0的供应商
 */
public class QichsjppInterface extends BasePage {
	
	public static String[][] Qicsjpp(String daohrq) throws SQLException{
//			用chepbtmp中的数据和qichjcsjppb、qichysdwppb的数据进行匹配，将匹配的数据用insert
//				语句的形式调用Qicsjcl.jws
		JDBCcon con=new JDBCcon();
		String strArray[][]=null;
		String where="";
		if(!daohrq.equals("")){
			
			where=" and daohrq=to_date('"+daohrq+"','yyyy-MM-dd')";
		}
		ResultSet rsPP = con.getResultSet("SELECT zhi FROM xitxxb WHERE mingc='导入数据信息匹配' AND beiz='使用'");
		String zhi = "否";
		boolean isPP = false;
		if (rsPP.next()) {
			zhi = rsPP.getString("zhi");
		}
		if ("是".equals(zhi)) {
			isPP = true;
		}
		rsPP.close();
		try{
			StringBuffer Sb_Id=new StringBuffer();
			String sql ="";
			
			int i=-1;
			
			sql="select distinct diancxxb_id from chepbtmp c where c.fahb_id<>1"+where+" order by diancxxb_id";
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.getRows()>0){
				
				strArray=new String[rsl.getRows()][3];
			}
			while(rsl.next()){
				i++;
				sql ="select c.id,c.diancxxb_id,q.gongysb_id,q.meikxxb_id,q.pinzb_id,1 as faz_id,1 as daoz_id,q.jihkjb_id,\n" +
					"       to_char(c.fahrq,'yyyy-MM-dd') as fahrq,\n" + 
					"       to_char(c.daohrq,'yyyy-MM-dd') as daohrq,\n" + 
					"       0 as hetb_id,0 as zhilb_id,'' as caiybh,0 as jiesb_id,2 as yunsfsb_id,\n" + 
					"       c.chec,c.cheph,c.maoz,c.piz,c.biaoz,c.yingd,c.yingk,c.yuns,c.yunsl,c.koud,\n" + 
					"       c.kous,c.kouz,c.sanfsl,c.ches,0 as guohb_id,0 as fahb_id,c.jianjfs,c.chebb_id,\n" + 
					"       1 as yuandz_id,c.diancxxb_id as yuanshdwb_id,0 as kuangfzlb_id,c.meikdwmc as yuanmkdw,\n" + 
					"       nvl(ys.yunsdwb_id,0) as yunsdwb_id,to_char(c.qingcsj,'yyyy-MM-dd hh24:mi:ss') as qingcsj,nvl(c.qingchh,' ') as qingchh,\n" + 
					"       nvl(c.qingcjjy,' ') as qingcjjy,to_char(c.zhongcsj,'yyyy-MM-dd hh24:mi:ss') as zhongcsj,nvl(c.zhongchh,' ') as zhongchh,\n" + 
					"       nvl(c.zhongcjjy,' ') as zhongcjjy,0 as meicb_id,nvl(c.daozch,' ') as daozch,\n" + 
					"       nvl(c.lury,' ') as lury,c.beiz,to_char(c.zhongcsj,'yyyy-MM-dd') as caiyrq,sysdate as lursj,xcfs.id as xiecfsb_id \n" + 
					"       from chepbtmp c,qichjcsjppb q,qichysdwppb ys,xiecfsb xcfs\n" + 
					"       where c.gongysmc=q.gongysmc\n" +
					" 			  and c.diancxxb_id=q.diancxxb_id\n" +
					" 			  and c.diancxxb_id=ys.diancxxb_id(+)\n" +
					"             and c.meikdwmc=q.meikdwmc\n" + 
					"             and c.pinz=q.pinz\n" + 
					"             and c.yunsdw=ys.yunsdw(+)\n" +
					"			  and c.xiecfs=xcfs.mingc(+)\n" +
					"             and q.zhuangt=1 \n"+			
					"             and c.fahb_id<>1"+where+" and c.diancxxb_id="+rsl.getString("diancxxb_id");
				if (isPP) {
					sql = 
						"select c.id,c.diancxxb_id,g.id AS gongysb_id,mk.id AS meikxxb_id,\n" +
						"       pz.id AS pinzb_id,1 as faz_id,1 as daoz_id,kj.id AS  jihkjb_id,\n" + 
						"       to_char(c.fahrq,'yyyy-MM-dd') as fahrq,\n" + 
						"       to_char(c.daohrq,'yyyy-MM-dd') as daohrq,\n" + 
						"       0 as hetb_id,0 as zhilb_id,'' as caiybh,0 as jiesb_id,2 as yunsfsb_id,\n" + 
						"       c.chec,c.cheph,c.maoz,c.piz,c.biaoz,c.yingd,c.yingk,c.yuns,c.yunsl,c.koud,\n" + 
						"       c.kous,c.kouz,c.sanfsl,c.ches,0 as guohb_id,0 as fahb_id,c.jianjfs,c.chebb_id,\n" + 
						"       1 as yuandz_id,c.diancxxb_id as yuanshdwb_id,0 as kuangfzlb_id,c.meikdwmc as yuanmkdw,\n" + 
						"       nvl(ys.id,0) as yunsdwb_id,to_char(c.qingcsj,'yyyy-MM-dd hh24:mi:ss') as qingcsj,nvl(c.qingchh,' ') as qingchh,\n" + 
						"       nvl(c.qingcjjy,' ') as qingcjjy,to_char(c.zhongcsj,'yyyy-MM-dd hh24:mi:ss') as zhongcsj,nvl(c.zhongchh,' ') as zhongchh,\n" + 
						"       nvl(c.zhongcjjy,' ') as zhongcjjy,0 as meicb_id,nvl(c.daozch,' ') as daozch,\n" + 
						"       nvl(c.lury,' ') as lury,c.beiz,to_char(c.zhongcsj,'yyyy-MM-dd') as caiyrq,sysdate as lursj,xcfs.id as xiecfsb_id,c.piaojh\n" + 
						"       from chepbtmp c, (select * from gongysb where leix=1) g,meikxxb mk,pinzb pz,yunsdwb ys,jihkjb kj,xiecfsb xcfs\n" + 
						"       where c.gongysmc=g.mingc\n" + 
						"             and c.meikdwmc=mk.mingc\n" + 
						"             and c.pinz=pz.mingc\n" + 
						"             and c.yunsdw=ys.mingc(+)\n" + 
						"             and c.xiecfs=xcfs.mingc(+)\n" + 
						"             AND c.jihkj=kj.mingc\n" + 
						"             and c.fahb_id<>1\n" + 
						where + 
						"             and c.diancxxb_id=" + rsl.getString("diancxxb_id");
				}
				ResultSetList rs=con.getResultSetList(sql);
				StringBuffer sb=new StringBuffer();
				sb.append("begin	\n");
				while(rs.next()){
					
					sb.append("	insert into cheplsb (id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id,")
						.append("daoz_id, jihkjb_id, fahrq, daohrq, hetb_id, zhilb_id, caiybh, jiesb_id, yunsfsb_id,")
						.append("chec, cheph, maoz, piz, biaoz, yingd, yingk, yuns, yunsl, koud, kous, kouz, sanfsl, ches,")
						.append("guohb_id, fahb_id, jianjfs, chebb_id, yuandz_id, yuanshdwb_id, kuangfzlb_id, yuanmkdw, yunsdwb_id,")
						.append("qingcsj, qingchh, qingcjjy, zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch, lury, beiz, caiyrq, lursj, xiecfsb_id,piaojh)\n");
				
					sb.append("		values	\n");
					sb.append("		(getnewid("+rs.getLong("diancxxb_id")+"), "+rs.getLong("diancxxb_id")+", "+rs.getLong("gongysb_id")+", ")
						.append(rs.getLong("meikxxb_id")+", "+rs.getLong("pinzb_id")+", "+rs.getLong("faz_id")+", "+rs.getLong("daoz_id")+", ")
						.append(rs.getLong("jihkjb_id")+", to_date('"+rs.getString("fahrq")+"','yyyy-MM-dd'), to_date('"+rs.getString("daohrq")+"','yyyy-MM-dd'),")
						.append(rs.getLong("hetb_id")+", "+rs.getLong("zhilb_id")+", '"+rs.getString("caiybh")+"', "+rs.getLong("jiesb_id")+", ")
						.append(rs.getLong("yunsfsb_id")+", '"+rs.getString("chec")+"', '"+rs.getString("cheph")+"', "+rs.getDouble("maoz")+", ")
						.append(rs.getDouble("piz")+", "+rs.getDouble("biaoz")+", "+rs.getDouble("yingd")+", "+rs.getDouble("yingk")+", "+rs.getDouble("yuns")+", ")
						.append(rs.getDouble("yunsl")+", "+rs.getDouble("koud")+", "+rs.getDouble("kous")+", "+rs.getDouble("kouz")+", "+rs.getDouble("sanfsl")+", ")
						.append(rs.getDouble("ches")+", "+rs.getLong("guohb_id")+", "+rs.getLong("fahb_id")+", '"+rs.getString("jianjfs")+"', "+rs.getLong("chebb_id")+", ")
						.append(rs.getLong("yuandz_id")+", "+rs.getLong("yuanshdwb_id")+", "+rs.getLong("kuangfzlb_id")+", '"+rs.getString("yuanmkdw")+"', ")
						.append(rs.getLong("yunsdwb_id")+", to_date('"+rs.getString("qingcsj")+"','yyyy-MM-dd hh24:mi:ss'), '"+rs.getString("qingchh")+"', '")
						.append(rs.getString("qingcjjy")+"', to_date('"+rs.getString("zhongcsj")+"','yyyy-MM-dd hh24:mi:ss'), '"+rs.getString("zhongchh")+"','")
						.append(rs.getString("zhongcjjy")+"', "+rs.getLong("meicb_id")+", '"+rs.getString("daozch")+"', '"+rs.getString("lury")+"', '"+rs.getString("beiz")+"', to_date('")
						.append(rs.getString("caiyrq")+"','yyyy-MM-dd'), sysdate, "+rs.getLong("xiecfsb_id")+"," + rs.getString("piaojh") + ");	\n");
				
					Sb_Id.append(rs.getString("id")).append(",");
				}
				sb.append("end; ");
				
				if(rs.getRows()>0){
//					匹配成功
//					记录匹配信息以便调用webservice用
					strArray[i][0]=rsl.getString("diancxxb_id");
					strArray[i][1]=sb.toString();
					strArray[i][2]=Sb_Id.deleteCharAt(Sb_Id.length()-1).toString();
				}
				
				if(rs!=null){
					
					rs.close();
				}
			}
			if(rsl!=null){
				
				rsl.close();
			}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return strArray;
	}
	
	public static boolean UpdateChepbtmp(JDBCcon con,String Ids){
		
		boolean Flag=false;
		String sql="update chepbtmp set fahb_id=1 where id in ("+Ids+")";
		if(con.getUpdate(sql)>=0){
			
			Flag=true;
		}
		return Flag;
	}
	
	public static String Qicsjpp_sd(long Diancxxb_id,String daohrq){
		
		Service ser=new Service();
		Call call = null;
		String ReValue="";
		try {
			call = (Call) ser.createCall();
		} catch (ServiceException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		// 设置入口
		try {
			call.setTargetEndpointAddress(new java.net.URL(getAddress(Diancxxb_id)));
		} catch (MalformedURLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		call.addParameter("strDaohrq", XMLType.SOAP_STRING,ParameterMode.IN);
		call.setOperationName("Qicsjpp_jws");
		// 设置入口参数
		
		call.setReturnType(XMLType.SOAP_STRING);
		try {
			ReValue=String.valueOf(call.invoke(new Object[]{daohrq}));
		} catch (RemoteException e) {
			// TODO 自动生成 catch 块
			ReValue="汽车数据导入文件位置有误";
			e.printStackTrace();
		}
		
		return ReValue;
	}
	
	private static String getAddress(long Diancxxb_id){
		
		String _HttpServerPath="http://localhost/Qicsjcl.jws";
		
		_HttpServerPath=MainGlobal.getXitxx_item("数量", "汽车数据处理jws位置", String.valueOf(Diancxxb_id), _HttpServerPath);
		
		return _HttpServerPath;
	}
}
