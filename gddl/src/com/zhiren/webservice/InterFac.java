package com.zhiren.webservice;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysInfo;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 厂级服务  
 * 厂级：	InterFac		名称	类型
	方法名1：	响应请求	request	
	参数名1：	用户名	usr	String
	参数名2：	密码	password	String
	参数名3：	任务名	task	String
	返回值：	成功/失败1,失败2		String

	方法名2：	响应请求	requestall	
	参数名1：	用户名	usr	String
	参数名2：	密码	password	String
	返回值：	成功/失败1,失败2		String
 *
 */
public class InterFac {
	private static final String error006="-1,006,远程webservice服务出现未知问题，可能是webservice部署失败。！";
	private static final String error101="-1,101,网络连接失败，或url不能定位到服务！";
	private static final String error102="-1,102,资源定位符url不符合其规则！";
	private static final String error103="-1,103,任务发送配置sql语句出错！";//
	private static final String error104="-1,104,本地web服务部署失败！";
	private static final String error105="1,105,本地未知异常！";
	//tem1="-1,"+"106,"+parmeter[4].toString();//客户端错误
	private static final String rizPath="C:/fassj_zr";// 发送日志路径
	private String user;
	private String password;
	private String endpointAddress;
//	
	public InterFac() {
		super();
		// TODO 自动生成构造函数存根
		SysInfo xitxx=new SysInfo();
		user=MainGlobal.getXitxx_item("接口用","接口上传用户名", "197", " ");
		password=MainGlobal.getXitxx_item("接口用","接口上传密码", "197", " ");
		endpointAddress=MainGlobal.getXitxx_item("接口用","入口地址", "197", " ");
	}
	//输入一个sql语句能够返回他的结果字符串，字符串格式 1123 ，20
	//按天验证数据
	public String getSqlString(String sql){
		String reslutStr="";
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		try{
			int Colucount=rs.getMetaData().getColumnCount();
			if(rs.next()){//规定只能有一个返回值
				for(int i=1;i<=Colucount;i++){
					String tem=rs.getString(i);
					if(i==1){
						reslutStr+=(tem==null?"":tem);
					}else{
						reslutStr+=","+(tem==null?"":tem);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return reslutStr;
	}
	public void shangc(String diancxxb_id,String riq1,String riq2,String leix){
		//1,写任务，避免与触发器重复
		//数量、质量、合同、数量分表、质量分表、价格分表、增扣款分表、文字分表、煤款、运费、成本月报、入炉煤质量表、入炉煤数量表、收耗存日报
		//电量日报、结算指标数据表、质量临时表
		JDBCcon con=new JDBCcon();
		String changbb_id="0";
		String changbWhere=" and  changbb_id<>-1 ";
		String sql="";
		if(!diancxxb_id.equals("-1")){
			sql=
				"select id\n" +
				"from changbb\n" + 
				"where id_jit="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			try {
				if(rs.next()){
					changbb_id=rs.getString(1);
				}else{
					return;
				}
				changbWhere=" and changbb_id="+changbb_id;
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}

		//fahb
		if(leix.equals("fahb")||leix.equals("全部")){
			sql=
			"insert into jiekrwb\n" +
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (select xl_jiekrwb_id.nextval id,\n" + 
			"          'fahb' renwmc,\n" + 
			"          id renwbs,\n" + 
			"          0 renllx,\n" + 
			"          0 zhixzt,\n" + 
			"          changbb_id,\n" + 
			"          'xml' mingllx,to_date(guohrq,'yyyy-mm-dd')\n" + 
			"     from(\n" + 
			"     select distinct fahb.id,'fahb'renwmc,0 renllx,to_char(chepb.guohsj,'yyyy-mm-dd')guohrq,changbb_id\n" + 
			"     from fahb, chepb\n" + 
			"    where fahb.id = chepb.fahb_id　\n" + //and fahb.hedbz=1
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') >= '"+riq1+"'\n" + 
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') <= '"+riq2+"'\n" + 
			"     "+changbWhere+" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb))";
	
			con.getInsert(sql);
		}
		//zhilb
		if(leix.equals("zhilb")||leix.equals("全部")){
	    sql=
	    	"insert into jiekrwb\n" +
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (select xl_jiekrwb_id.nextval id,\n" + 
			"          'zhilb' renwmc,\n" + 
			"          zhilb_id renwbs,\n" + 
			"          0 renllx,\n" + 
			"          0 zhixzt,\n" + 
			"          changbb_id,\n" + 
			"          'xml' mingllx,to_date(guohrq,'yyyy-mm-dd')\n" + 
			"     from(\n" + 
			"     select distinct fahb.zhilb_id,'zhilb'renwmc,0 renllx,to_char(chepb.guohsj,'yyyy-mm-dd')guohrq,changbb_id\n" + 
			"     from fahb, chepb,zhilb\n" + 
			"    where fahb.zhilb_id=zhilb.id and  zhilb.shangjshzt=1 and fahb.id = chepb.fahb_id　\n" + // and fahb.hedbz=1
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') >= '"+riq1+"'\n" + 
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') <= '"+riq2+"'\n" + 
			"     "+changbWhere+"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb ))";

			con.getInsert(sql);
		}
			//zhilbtmp
		if(leix.equals("zhillsb")||leix.equals("全部")){
		    sql=
		    	"insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (select xl_jiekrwb_id.nextval id,\n" + 
				"          'zhillsb' renwmc,\n" + 
				"          id renwbs,\n" + 
				"          0 renllx,\n" + 
				"          0 zhixzt,\n" + 
				"         changbb_id,\n" + 
				"          'xml' mingllx,to_date(guohrq,'yyyy-mm-dd')\n" + 
				"     from(\n" + 
				"     select distinct  zhilbtmp.id,'zhillsb'renwmc,0 renllx,to_char(chepb.guohsj,'yyyy-mm-dd')guohrq,changbb_id\n" + 
				"     from fahb, chepb,zhilbtmp\n" + 
				"    where  fahb.zhilb_id=zhilbtmp.zhilb_id  and zhilbtmp.shangjshzt=1 and fahb.id = chepb.fahb_id　\n" + 
				"      and to_char(chepb.guohsj, 'YYYY-MM-DD') >= '"+riq1+"'\n" + 
				"      and to_char(chepb.guohsj, 'YYYY-MM-DD') <= '"+riq2+"'\n" + 
				"    "+changbWhere+"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb ))";

				con.getInsert(sql);
		}
//				hetb
		if(leix.equals("hetb")||leix.equals("全部")){
			    sql="insert into jiekrwb\n" +
			    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			    "  (\n" + 
			    "  select xl_jiekrwb_id.nextval id,'hetb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,to_date(qiandrq,'yyyy-mm-dd')\n" + 
			    "  from(\n" + 
			    "  select id,'hetb'renwmc,0 renllx,to_char(qiandrq,'yyyy-mm-dd')qiandrq,changbb_id\n" + 
			    "  from hetxxb where " + 
			    "    to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"'\n" + 
			    "  and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"'\n" + 
			    changbWhere+
			    "  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='hetb')\n" + 
			    "  )";
				con.getInsert(sql);
//				hetslb
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				" (\n" + 
				"select xl_jiekrwb_id.nextval id, 'hetslb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				"from(\n" + 
				"select nianjhhtb.id,'hetslb'renwmc,0 renllx,changbb_id\n" + 
				"from nianjhhtb,hetxxb\n" + 
				"where nianjhhtb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetslb')\n" + 
				" )";
				con.getInsert(sql);	
//				hetzlxyb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetzlb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				" select hetzlxyb.id,'hetzlb'renwmc,0 renllx,changbb_id\n" + 
				" from hetzlxyb,hetxxb\n" + 
				" where hetzlxyb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetzlb' ))";
				con.getInsert(sql);	
//				hetjgb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetjgb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				"  select hetjsxyb.id,'hetjgb'renwmc,0 renllx,changbb_id\n" + 
				" from hetjsxyb,hetxxb\n" + 
				" where hetjsxyb.biaoz=0  and hetjsxyb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetjgb'))";
				con.getInsert(sql);	
//				hetzkkb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetzkkb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				"   select hetjsxyb.id,'hetzkkb'renwmc,0 renllx,changbb_id\n" + 
				" from hetjsxyb,hetxxb\n" + 
				" where hetjsxyb.biaoz=1  and hetjsxyb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetzkkb' ))";
				con.getInsert(sql);	
//				hetwzb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetwzb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				"  select id,'hetwzb'renwmc,0 renllx,changbb_id\n" + 
				" from hetxxb\n" + 
				"  where to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetwzb'  ))";
				con.getInsert(sql);	
		}
//				jiesb
		if(leix.equals("jiesb")||leix.equals("全部")){
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'jiesb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,to_date(jiesrq,'yyyy-mm-dd')\n" + 
				"  from(\n" + 
				"    select id,'jiesb'renwmc,0 renllx,to_char(jiesrq,'yyyy-mm-dd')jiesrq,changbb_id\n" + 
				"  from jiesb\n" + 
				"  where kuangfjs<>1 and  to_char(jiesb.jiesrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(jiesb.jiesrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='jiesb'  ))";

				con.getInsert(sql);
//				jiesyfb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'jiesyfb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(jiesrq,'yyyy-mm-dd')\n" + 
				"  from(\n" + 
				"   select id,'jiesyfb'renwmc,0 renllx,to_char(jiesrq,'yyyy-mm-dd')jiesrq,changbb_id\n" + 
				"  from jiesb\n" + 
				"  where kuangfjs<>1 and   to_char(jiesb.jiesrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(jiesb.jiesrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+" \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='jiesyfb' ))";

				con.getInsert(sql);
//				jieszbsjb
				sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'jieszbsjb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,to_date(jiesrq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				"select id,'jieszbsjb'renwmc,0 renllx,to_char(jiesrq,'yyyy-mm-dd')jiesrq,changbb_id\n" + 
				"from jiesb\n" + 
				"where kuangfjs<>1 and   to_char(jiesb.jiesrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(jiesb.jiesrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='jieszbsjb'))";
				con.getInsert(sql);
		}
//				yuercbmdj
		if(leix.equals("yuercbmdj")||leix.equals("全部")){
				sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'yuercbmdj'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(riq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				" select id,'yuercbmdj'renwmc,0 renllx,c.nianf||'-'||to_char(yuef,'00')||'-01' riq,changbb_id\n" + 
				"from chengbmxybb c\n" + 
				"where  to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"' and (dangyjlj='本年'or dangyjlj='本月')\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='yuercbmdj'))";
				con.getInsert(sql);
////				yuetjkjb
//				sql="insert into jiekrwb\n" +
//				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
//				" (\n" + 
//				" select xl_jiekrwb_id.nextval id, 'yuetjkjb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
//				" from(\n" + 
//				" select id,'yuetjkjb'renwmc,0 renllx,changbb_id\n" + 
//				" from chengbmxybb c\n" + 
//				" where  to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"' and (dangyjlj='本年'or dangyjlj='本月')\n" + changbWhere+ 
//				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='yuetjkjb'))";
//				con.getInsert(sql);
		}
//				rulmzlb
		if(leix.equals("rulmzlb")||leix.equals("全部")){
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'rulmzlb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,rulrq\n" + 
				" from(\n" + 
				"  select id,'rulmzlb'renwmc,0 renllx,rulrq,changbb_id\n" + 
				" from rulmzlb r\n" + 
				" where  r.shenhzt=3 and to_char(rulrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(rulrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='rulmzlb'))";
				con.getInsert(sql);
		}
//				haoyqkmb
		if(leix.equals("meihyb")||leix.equals("全部")){
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'meihyb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				" from(\n" + 
				" select id,'meihyb'renwmc,0 renllx,riq,changbb_id\n" + 
				" from haoyqkmb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='meihyb'))";
				con.getInsert(sql);
		}
//				shouhcrbb
		if(leix.equals("shouhcrbb")||leix.equals("全部")){
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				"\n" + 
				" select xl_jiekrwb_id.nextval id, 'shouhcrbb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				" from (\n" + 
				" select id,riq,changbb_id\n" + 
				" from shouhcrbmb where id in(\n" + 
				" select id\n" + 
				" from(\n" + 
				" select id,'shouhcrbb'renwmc,0 renllx\n" + 
				" from shouhcrbmb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+" \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx from jiekrwb where renwmc='shouhcrbb')\n" + 
				")\n" + 
				" order by riq\n" + 
				")\n" + 
				")";
				con.getInsert(sql);
		}
//				riscsjb
		if(leix.equals("riscsjb")||leix.equals("全部")){
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'riscsjb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				"  from(\n" + 
				"    select id,'riscsjb'renwmc,0 renllx,riq,changbb_id\n" + 
				"  from dianlrb r\n" + 
				"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' and r.jizh<>'合计'  "+changbWhere+" \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='riscsjb'))";
				con.getInsert(sql);
		}
		if(leix.equals("yuecgjhb")||leix.equals("全部")){
			sql=
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'yuecgjhb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(riq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				" select id,'yuecgjhb'renwmc,0 renllx,c.niand||'-'||trim(to_char(yued,'00'))||'-01' riq,changbb_id\n" + 
				"from yuedjhfb c\n" + 
				"where  to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='yuecgjhb'))";
				con.getInsert(sql);
//			yuexqjhh
			sql=
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'yuexqjhh'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(riq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				" select id,'yuexqjhh'renwmc,0 renllx,c.niand||'-'||trim(to_char(yued,'00'))||'-01' riq,changbb_id\n" + 
				"from yuedjhzb c\n" + 
				"where  to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='yuexqjhh'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("quzpkb")||leix.equals("全部")){
			sql=
				"insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'quzpkb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				" from(\n" + 
				" select id,'quzpkb'renwmc,0 renllx,riq,changbb_id\n" + 
				" from quzpkqkb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='quzpkb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("zhuangcyb")||leix.equals("全部")){
			sql=
				"insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'zhuangcyb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				" from(\n" + 
				" select id,'zhuangcyb'renwmc,0 renllx,riq,changbb_id\n" + 
				" from kuangfybb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='zhuangcyb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("tielyb")||leix.equals("全部")){
			sql=
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'tielyb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'tielyb'renwmc,0 renllx,riq,changbb_id\n" + 
				"from tielybb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='tielyb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("pandb")||leix.equals("全部")){
		   //盘点的总表
			sql=//pandb任务
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'pandb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'pandb'renwmc,0 renllx,riq,changbb_id\n" + 
				"from panmyqkbgzb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandb'))";
			con.getInsert(sql);
			//pandzmy,pandzmm任务
			sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'pandzmy'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'pandzmy'renwmc,0 renllx,riq,changbb_id\n" + 
				"from panmyqkbgzb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandzmy'))";
			con.getInsert(sql);
			sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'pandzmm'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'pandzmm'renwmc,0 renllx,riq,changbb_id\n" + 
				"from panmyqkbgzb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandzmm'))"; 
		    con.getInsert(sql);
		    //总表下的分表和基础数据，没有总表的不上传
		    sql=
		    	"insert into jiekrwb\n" +
		    	"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    	"  (\n" + 
		    	"  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    	"  from(\n" + 
		    	"          select id,'pandyb'renwmc,0 renllx,riq,changbb_id\n" + 
		    	"          from pandcyb\n" + 
		    	"          where to_char(pandcyb.riq,'yyyymmdd')||pandcyb.changbb_id in\n" + 
		    	"          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    	"          from panmyqkbgzb\n" + 
		    	"          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+" )\n" + 
		    	"          minus\n" + 
		    	"          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandyb'\n" + 
		    	"  )\n" + 
		    	" ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" + 
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandmdb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from meicmd\n" + 
		    "          where to_char(meicmd.riq,'yyyymmdd')||meicmd.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'" + changbWhere+"  )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandmdb'\n" + 
		    "  ) ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" +
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandtjb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from meicpd\n" + 
		    "          where to_char(meicpd.riq,'yyyymmdd')||meicpd.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"  )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandtjb'\n" + 
		    "  ) ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" +
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandwzcmb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from weizcm\n" + 
		    "          where to_char(weizcm.riq,'yyyymmdd')||weizcm.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+" )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandwzcmb'\n" + 
		    "  ) ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" +
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandbmryzzb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from pandbp\n" + 
		    "          where to_char(pandbp.riq,'yyyymmdd')||pandbp.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+" )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandbmryzzb'\n" + 
		    "  )) ";
			con.getInsert(sql);
			//pandtb xml

			 sql="insert into jiekrwb\n" +
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'pandtb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
			"  from(\n" + 
			"    select id,'pandtb'renwmc,0 renllx,riq,changbb_id,'xml'mingllx\n" + 
			"  from pandtb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id,mingllx from jiekrwb where renwmc='pandtb'))\n" ;
			 con.getInsert(sql);
			 sql="   insert into jiekrwb\n" + 
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj,minglcs)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'pandtb'renwmc,pandt.id renwbs,0 renllx,0 zhixzt, pandt.changbb_id,'file'mingllx1,pandt.riq,\n" + 
			"  (select zhi from xitxxb where duixm='盘点图表目录')||'\\'||pan.fuj minglcs\n" + 
			"  from(\n" + 
			"  select id,'pandtb'renwmc,0 renllx,riq,'file'mingllx,changbb_id\n" + 
			"  from pandtb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,mingllx,changbb_id from jiekrwb where renwmc='pandtb')pandt,pandtb pan\n" + 
			"  where pandt.id=pan.id)\n" ;
			 con.getInsert(sql);
			 sql="    insert into jiekrwb\n" + 
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'yuemcmyzb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
			"  from(\n" + 
			"    select id,'yuemcmyzb'renwmc,0 renllx,riq,changbb_id,'xml'mingllx\n" + 
			"  from yuemcmyzb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id,mingllx from jiekrwb where renwmc='yuemcmyzb'))\n" ;
			 con.getInsert(sql);
			 sql="    insert into jiekrwb\n" + 
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'yuemcmyb'renwmc,id renwbs,0 renllx,0 zhixzt,1 changbb_id,'xml'mingllx,riq\n" + 
			"  from(\n" + 
			"    select id,'yuemcmyb'renwmc,0 renllx,riq,changbb_id,'xml'mingllx\n" + 
			"  from yuemcmyzb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id,mingllx from jiekrwb where renwmc='yuemcmyb'))";
			 con.getInsert(sql);
	   }
	   if(leix.equals("shouhcrbyb")||leix.equals("全部")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'shouhcrbyb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'shouhcrbyb'renwmc,0 renllx,riq,changbb_id\n" + 
				   "  from ranlkcqk r\n" + 
				   "  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"   and r.pinz='油'\n" + 
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='shouhcrbyb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("yuezbb")||leix.equals("全部")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'yuezbb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'yuezbb' renwmc,0 renllx, to_date(r.niand||'-'||r.yued||'-'||'01','YYYY-MM-DD')  riq,changbb_id\n" + 
				   "  from rulmydtjb r\n" + 
				   "  where  to_char(to_date(r.niand||'-'||r.yued||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'\n" + 
				   "   and to_char(to_date(r.niand||'-'||r.yued||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"\n" + 
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='yuezbb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("jizyxqkb")||leix.equals("全部")){
			sql=
				"insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'jizyxqkb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				"  from(\n" + 
				"    select id,'jizyxqkb'renwmc,0 renllx,riq,changbb_id\n" + 
				"  from dianlrb r\n" + 
				"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+" and jizh <> '合计' and jizzk is not null\n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='jizyxqkb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("shebyxqkb")||leix.equals("全部")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'shebyxqkb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'shebyxqkb' renwmc,0 renllx,   riq,changbb_id\n" + 
				   "  from jiexsbb\n" + 
				   "  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"'\n" + 
				   "   and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='shebyxqkb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("rigjb")||leix.equals("全部")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'rigjb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'rigjb' renwmc,0 renllx, riq,changbb_id\n" + 
				   "  from rigjb\n" + 
				   "  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"'\n" + 
				   "   and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='rigjb'))";
			con.getInsert(sql);
	   }
		con.Close();
		//2,调用接口
		if(leix.equals("全部")){
			requestall(diancxxb_id,riq1,riq2);
			requestFile(diancxxb_id,"",riq1,riq2);
		}else{
			request(leix,diancxxb_id,riq1,riq2);
			requestFile(diancxxb_id,leix,riq1,riq2);
		}
	}
	public void request(String task,String diancxxb_id,String riq1,String riq2){////远程调用、本地定时调用 and j.renwmc='"+task+"'order by id0";
		String renwmc="",renwsj="",id_jit="",renllx="",id="",shujjl="",tiaoj="",xml="",id0="",tem1="",tem2="";
		String message="";
		String diancidWhere ="";
		String diancidWhere1 ="";
		String riqWhere=" where  renwsj>='"+riq1+"' and renwsj<='"+riq2+"' ";
		String riqWhere1=" and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'即时',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')>='"+riq1+
		"' and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'即时',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')<='"+riq2+"' ";
	
		//删除查看文件夹的所有文件
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		JDBCcon con=new JDBCcon();
		JDBCcon con1=null;
		
		if(!diancxxb_id.equals("-1")){
			diancidWhere=" and id_jit="+diancxxb_id;
			diancidWhere1=" and changbb_id =(select id from changbb where id_jit="+diancxxb_id+")";
	    }
		if(riq1==null||riq2==null||riq1.equals("")||riq2.equals("")){
			riqWhere="";
			riqWhere1="";
		}
		String sql=
//不区分时间
//			"select changbb.id_jit,j.id id0,to_char(j.renwsj,'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
//			"from jiekrwb j,jiekfspzb p ,changbb\n" + 
//			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.changbb_id=changbb.id and j.mingllx=p.mingllx "+diancidWhere+" and j.renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"') order by id0";

			"select *\n" +
			"from(\n" + 
			"  select changbb.id_jit,j.id id0,to_char(decode(renwmcb.pinl,'即时',j.shengcsj,j.renwsj),'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" + 
			"from jiekrwb j,jiekfspzb p ,changbb,renwmcb\n" + 
			"where j.renwmc=renwmcb.renw and j.mingllx='xml' and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.changbb_id=changbb.id and j.mingllx=p.mingllx  "+diancidWhere+" and j.renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"') order by id0\n" + 
			") "+riqWhere;

	    ResultSet rs=con.getResultSet(sql);
		 sql=//要执行的进行加锁（0与-1），执行成功和执行失败的会自动解锁，但未执行的要人工进行解锁
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt=2\n" + 
		"where mingllx='xml' and(jiekrwb.zhixzt=0 or jiekrwb.zhixzt=-1) and renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"') "
		+riqWhere1+diancidWhere1;
		con.getUpdate(sql);
		Service service = new Service();
		try{
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renwsj=rs.getString("renwsj");
				renllx=rs.getString("renllx");
				id=rs.getString("id");//集团唯一任务标识id
				id0=rs.getString("id0");//任务id
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				id_jit=rs.getString("id_jit");
				shujjl=shujjl.replaceAll("%%", tiaoj);
			    con1=new JDBCcon();
				    if(renllx.equals("2")){//因为插入前都要删除、所以修改与增加是一样的都是先删除后新增
				    	renllx="0";
				    }
					xml=CreateXml(renwmc,renwsj,id_jit,renllx,id,	con1.getResultSet(shujjl));
					Call call = (Call) service.createCall();//远程调用者
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("incept");
					call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {user,password,xml.getBytes()}));//写日志
					//关联操作
//					String[]dat=data.split(",");//dat[0] 
					Xierz(tem1,id0);
//				}
					con1.Close();
			}
			StringBuffer id0s=new StringBuffer();
			List sqls=CreateSql(id0s);
			for (int ii=0;ii<sqls.size();ii++){
				Call call = (Call) service.createCall();//远程调用者
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("execute");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				
				String data=String.valueOf(call.invoke(new Object[] {user,password,sqls.get(ii).toString()}));//写日志
//				String in=sqls.get(ii).toString().substring(sqls.get(ii).toString().lastIndexOf("in"));
				Xierzexe(data,id0s.toString());
			}
		}catch(SQLException e){//造成中断的错误
			jies();
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//构造url时出错
			jies();
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//远程未知错误或网络错误No route to host: connect
			jies();
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// 网络错误
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			jies();
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			jies();
			message=error105;
			e.printStackTrace();
		}finally{
			if(con1!=null){
				con1.Close();
			}
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	public  void requestall(String diancxxb_id,String riq1,String riq2) {////远程调用、本地定时调用
		String renwmc="",renwsj="",id_jit="",renllx="",id="",shujjl="",tiaoj="",xml="",id0="",tem1="",tem2="";
		String message="";
		String diancidWhere ="";
		String diancidWhere1 ="";
		String riqWhere=" where  renwsj>='"+riq1+"' and renwsj<='"+riq2+"' ";
		String riqWhere1=" and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'即时',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')>='"+riq1+
		"' and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'即时',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')<='"+riq2+"' ";
		//删除查看文件夹的所有文件  
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		JDBCcon con=new JDBCcon();
		JDBCcon con1=null;
		if(!diancxxb_id.equals("-1")){
			diancidWhere=" and id_jit="+diancxxb_id;
			diancidWhere1=" and changbb_id =(select id from changbb where id_jit="+diancxxb_id+")";
	    }
		if(riq1==null||riq2==null||riq1.equals("")||riq2.equals("")){
			riqWhere="";
			riqWhere1="";
		}
		String sql=
//			不区分时间
//			"select changbb.id_jit,j.id id0,to_char(j.renwsj,'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
//			"from jiekrwb j,jiekfspzb p ,changbb\n" + 
//			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.changbb_id=changbb.id and j.mingllx=p.mingllx "+diancidWhere+" order by id0";
		"select *\n" +
		"from(\n" + 
		"  select changbb.id_jit,j.id id0,to_char(decode(renwmcb.pinl,'即时',j.shengcsj,j.renwsj),'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" + 
		"from jiekrwb j,jiekfspzb p ,changbb,renwmcb\n" + 
		"where j.renwmc=renwmcb.renw (+)" +
				"and j.mingllx='xml' " +
				"and (zhixzt=0 or zhixzt=-1) " +
				"and j.renwmc=p.renwmc " +
				"and j.changbb_id=changbb.id(+) " +
				"and j.mingllx=p.mingllx (+) "+diancidWhere+"  order by id0\n" +
		")  "+riqWhere;
		try{
	    ResultSet rs=con.getResultSet(sql);
//	    sql=//要执行的进行加锁（0与-1），执行成功和执行失败的会自动解锁，但未执行的要人工进行解锁
//			"update jiekrwb\n" +
//			"set jiekrwb.zhixzt=2\n" +
//			"where  mingllx='xml' and(jiekrwb.zhixzt=0 or jiekrwb.zhixzt=-1) "
//			+riqWhere1+diancidWhere1;
//			con.getUpdate(sql);
		
		Service service = new Service();
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id=rs.getString("id");//集团唯一任务标识id
				id0=rs.getString("id0");//任务id
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				renwsj=rs.getString("renwsj");
				id_jit=rs.getString("id_jit");
				shujjl=shujjl.replaceAll("%%", tiaoj);
			    con1=new JDBCcon();
			    if(renllx.equals("2")){//因为插入前都要删除、所以修改与增加是一样的都是先删除后新增
			    	renllx="0";
			    }
				xml=CreateXml(renwmc,renwsj,id_jit,renllx,id,	con1.getResultSet(shujjl));
				Call call = (Call) service.createCall();//远程调用者
				java.net.URL url=new java.net.URL(endpointAddress);
				call.setTargetEndpointAddress(url);
				call.setOperationName("incept");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				tem1=String.valueOf(call.invoke(new Object[] {user,password,xml.getBytes()}));//写日志
				//关联操作
//					String[]dat=data.split(",");//dat[0] 
				Xierz(tem1,id0);
				con1.Close();
			}
			StringBuffer id0s=new StringBuffer();
			List sqls=CreateSql(id0s);
			for (int ii=0;ii<sqls.size();ii++){
				Call call = (Call) service.createCall();//远程调用者
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("execute");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				
				String data=String.valueOf(call.invoke(new Object[] {user,password,sqls.get(ii).toString()}));//写日志
//				String in=sqls.get(ii).toString().substring(sqls.get(ii).toString().lastIndexOf("in"));
				Xierzexe(data,id0s.toString());
			}
		}catch(SQLException e){//造成中断的错误
			jies();
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//构造url时出错
			jies();
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//远程未知错误或网络错误No route to host: connect
			jies();
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// 网络错误
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			jies();
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			jies();
			message=error105;
			e.printStackTrace();
		}finally{
			if(con1!=null){
				con1.Close();
			}
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	private void jies(){//对 0与-1的解锁
		JDBCcon con=new JDBCcon();
		String sql="update jiekrwb\n" +
		"set jiekrwb.zhixzt=0\n" + 
		"where jiekrwb.zhixzt=2 and jiekrwb.zhixbz is null";
		con.getResultSet(sql);
		sql=
			"update jiekrwb\n" +
			"set jiekrwb.zhixzt=-1\n" + 
			"where jiekrwb.zhixzt=2 and jiekrwb.zhixbz  is not null";
		con.getResultSet(sql);
		con.Close();
	}
	private  String CreateXml(String shujxy,String renwsj,String id_jit,String caoz,String guanlId,ResultSet rs) throws SQLException, IOException{
		String xmlAray="";//没有记录客户端异常。。。。。
		// TODO 自动生成方法存根
		Element root = new Element("命令帧");
		Document document = new Document(root);
		
//		root.setAttribute(new Attribute("vin", "123fhg5869705iop90"));
		root.addContent(new Element("数据协议").addContent(shujxy));
		root.addContent(new Element("操作").addContent(caoz));
		root.addContent(new Element("主键").addContent(guanlId));
		root.addContent(new Element("任务日期").addContent(renwsj));
		root.addContent(new Element("电厂id").addContent(id_jit));
		
			while(rs.next()){
				Element elShujjl = new Element("数据记录");
				root.addContent(elShujjl);
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
					elShujjl.addContent(new Element(rs.getMetaData().getColumnName(i)).addContent(rs.getString(i)));
					
				}
			}
			File file=new File(rizPath) ;
			FileWriter writer = new FileWriter(file.getAbsolutePath()+"/"+shujxy+guanlId+caoz+".xml");
			XMLOutputter outputter = new XMLOutputter();   
			Format format=Format.getPrettyFormat(); 
			format.setEncoding("gb2312"); 
//			format.setOmitDeclaration(true);
			outputter.setFormat(format);
			outputter.output(document, writer);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			outputter.output(document, bo);
			xmlAray=bo.toString();
			//xmlAray=bo.toByteArray();
		return xmlAray;
	}
	private   List CreateSql(StringBuffer id0s) throws SQLException{
		List resultSql=new ArrayList();
		JDBCcon con=new JDBCcon();
		String ids="";
		String sql=
			"select j.id,p.renwsql biaom,p.gengxy,changbb.id_jit||j.minglcs gengxyz,p.renwbs,changbb.id_jit||j.renwbs renwbsz\n" +
			"from jiekrwb j,jiekfspzb p,changbb\n" + 
			"where j.renwmc=p.renwmc and j.mingllx=p.mingllx and j.mingllx<>'xml'and j.mingllx<>'file' and zhixzt=0 and j.changbb_id=changbb.id" +
			" order by biaom,gengxy,gengxyz,renwbs";
		ResultSet rs=con.getResultSet(sql);
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
						in+=renwbsz;
					}else{
						in+=","+renwbsz;
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
					in+=renwbsz;
				}
			}
			//最后一个分组
			if(kk!=0){
				in+=")";
				resultSql.add("update "+biaom+" set "+gengxy+"'"+gengxyz+"' where "+renwbs+in);
			}
		}finally{
			con.Close();
		}
		if(!ids.equals("")){
			id0s.append(ids.substring(0, ids.lastIndexOf(",")));
		}
		return resultSql;
	}
	public  void requestFile(String diancxxb_id,String task,String riq1,String riq2) {////远程调用、本地定时调用
		String renwmc="",filePath="",id_jit="",renllx="",id0="",tem1="";
		Object[] parmeter=null;
		String message="";
		String diancidWhere ="";
		String taskWhere=" and j.renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"')";
		String riqWhere=" where  renwsj>='"+riq1+"' and renwsj<='"+riq2+"' ";
		JDBCcon con=new JDBCcon();
		if(!diancxxb_id.equals("-1")){
			diancidWhere=" and id_jit="+diancxxb_id;
	    }
		if(riq1==null||riq2==null||riq1.equals("")||riq2.equals("")){
			riqWhere="";
		}
		if(task==null ||task.endsWith("")){
			taskWhere="";
		}
		String sql=
		"select *\n" +
		"from(\n" + 
		"  select changbb.id_jit,j.id id0,to_char(decode(renwmcb.pinl,'即时',j.shengcsj,j.renwsj),'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,j.minglcs\n" + 
		"from jiekrwb j,changbb,renwmcb\n" + 
		"where j.renwmc=renwmcb.renw and j.mingllx='file' and (zhixzt=0 or zhixzt=-1)  and j.changbb_id=changbb.id   "+diancidWhere+taskWhere+" order by id0\n" + 
		") "+riqWhere;
	    ResultSet rs=con.getResultSet(sql);
	   
		
		Service service = new Service();
		try{
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id0=rs.getString("id0");//任务id
				filePath=rs.getString("minglcs");
				id_jit=rs.getString("id_jit");
				//根据filePath，renwmc，id_jit,renllx计算出集团文件存储的四个参数
				parmeter=parmeterFilestroe(filePath,renwmc,id_jit,renllx);//返回数组0:path1:name2:caoz3，错误与true;
				if(parmeter[3].equals("true")){//如果为真说明具备了上传条件
					Call call = (Call) service.createCall();//远程调用者
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("fileStroe");
					call.addParameter("path", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("name", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("file", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.addParameter("caoz", XMLType.SOAP_STRING,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {parmeter[0],parmeter[1],fileByte,parmeter[2]}));//写日志
				}else{
					tem1="-1,"+"106,"+parmeter[3].toString();//客户端错误
				}
				Xierz(tem1,id0);////"1,'000','接收成功'";
			}
			
		}catch(SQLException e){//造成中断的错误
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//构造url时出错
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//远程未知错误或网络错误No route to host: connect
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// 网络错误
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			message=error105;
			e.printStackTrace();
		}finally{
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	private byte[] fileByte=null;
	private Object[] parmeterFilestroe(String filePath,String renwmc,String id_jit,String renllx){
		//filePath:D:\Tomcat 5.0\webapps\ftp\20081210.dmp
		//0:任务+电厂（pandtb，108）1:name2:caoz3,err
		Object[] temArry=new String[4];
		InputStream is = null;
		ByteArrayOutputStream byteOut=null;
		//把文件转换成二进制数组byte
		if (filePath != null&& !filePath.equals("")) {
			try {
				File file=new File(filePath);
				//上传的文件名称与表数据对应
				temArry[0]=renwmc+","+id_jit;
				//上传的文件名称与表数据对应
				temArry[1]=filePath.substring(filePath.lastIndexOf("\\")+1);
				is = new FileInputStream(file);
				byteOut=new ByteArrayOutputStream();
				byte[] buff = new byte[1024];
				while (is.read(buff) != -1) {
					byteOut.write(buff);
				}
				fileByte=byteOut.toByteArray();//返回二进制数组byte
				temArry[2]=renllx;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.print("上传出错！");
				temArry[3]=e.getMessage();
				return temArry;
			} finally {
				try {
					byteOut.close();
					is.close();
				} catch (Exception e) {
					
				}
			}
		}else{
			temArry[3]="路径为空！";
			return temArry;
		}
		temArry[3]="true";
		return temArry;
	}
	private void Xierz(String data,String id0){//写日志
		//"1,'000','接收成功'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		
		"where id='"+id0+"'";
		con.getUpdate(sql);
		con.Close();
	}
	private void Xierzexe(String data,String in){//执行日志
		//"1,'000','接收成功'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		"where id in("+in+")";
		con.getUpdate(sql);
		con.Close();
	}
	public String[]  sqlExe(String[] sqls,boolean isTransaction) throws Exception{
		//1连接数据库
		String ConnStr="",UserName="",UserPassword="";
		String[] arryRes=new String[sqls.length];
		if (ConnStr.equals("")) {
			ConnStr = MainGlobal.getDb_jdbcDriverURL();
		}
		if (UserName.equals("")) {
			UserName = MainGlobal.getDb_username();
		}
		if (UserPassword.equals("")) {
			UserPassword = MainGlobal.getDb_password();
		}

		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection dBConnection = DriverManager.getConnection(ConnStr,UserName, UserPassword);
		Statement st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		//2循环执行sqls，并记录执行结果。
		try{
			if(isTransaction){//如果是事务
				dBConnection.setAutoCommit(false);
				for (int i=0;i<sqls.length;i++){
					try {
						st.executeUpdate(sqls[i]);
						arryRes[0]="true";
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						arryRes[0]=e.getLocalizedMessage();
						e.printStackTrace();
						dBConnection.rollback();
						return arryRes;
					}
				}
				dBConnection.commit();
			}else{// 不是事务
				for (int i=0;i<sqls.length;i++){
					try {
						st.executeUpdate(sqls[i]);
						arryRes[i]="true";
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						arryRes[i]=e.getLocalizedMessage();
						e.printStackTrace();
					}
				}
			}
		}
		finally{
			st.close();
			dBConnection.close();
		}
		return arryRes;
	}

}
