package com.zhiren.dtrlgs.pubclass;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
import com.zhiren.webservice.dtgsinterface.DBconn;

public class BalanceLiuc {

	// private static String TableName="";//数据表单表
	// private static String TableID="0";//数据表单ｉｄ
	// private static String LiucID="0";
	// // private static String wodrwIds="";
	// // private static String liuczIds="";
	// private static String renyxxb_id="";
	// private static String leibb_id="";
	// private static String xiaox="";//提交回退前的话写入日志
	//		
	//		
	// public static String getLiucID() {
	// return LiucID;
	// }
	// public static void setLiucID(String liucID) {
	// LiucID = liucID;
	// }
	// public static String getTableID() {
	// return TableID;
	// }
	// public static void setTableID(String tableID) {
	// TableID = tableID;
	// }
	// public String getTableName() {
	// return TableName;
	// }
	// public static void setTableName(String tableName) {
	// TableName = tableName;
	// }
	/** 在未审核页面用此方法提交
	 *  isTongguo 代表是否直接通过 true代表直接通过  false 表示 提交
 	 * */
	public static void tij(String TableName, long TableID, long renyxxb_id,boolean isTongguo,
			String xiaox) throws Exception {// 提交
		JDBCcon con = new JDBCcon();
		String sql = "";
		long dangqdz = -1; // 表单当前状态
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		long diancID=-1;
		String Jiesdh="";
		try {
			sql = "select liucztb_id--,bianm,diancxxb_id\n" + // 获得当前状态
					"from " + TableName + "\n" + "where id=" + TableID;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				dangqdz = rs.getLong("liucztb_id");
//				 diancID=rs.getLong("diancxxb_id");
//				 Jiesdh=rs.getString("bianm");
			}else{
				return;
			}
			sql = "select distinct liucdzb.id, leibztb2.id leibztb2_id,liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n"
					+ "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2,liucdzjsb dzjs,liucjsb ljs,renyjsb rjs\n"
					+ "where liucdzb.liucztqqid=liucztb1.id\n"
					+ " and liucdzb.liuczthjid=liucztb2.id\n"
					+ " and liucztb1.leibztb_id=leibztb1.id\n"
					+ " and liucztb2.leibztb_id=leibztb2.id\n"
					+ " and liucdzb.liucztqqid="
					+ dangqdz
					+" and liucztb1.xuh<liucztb2.xuh\n"
				
					+" and dzjs.liucdzb_id=liucdzb.id" 
					+" and dzjs.liucjsb_id=rjs.liucjsb_id\n"
                    +" and rjs.renyxxb_id="+renyxxb_id+"\n";
			ResultSetList rs1 = con.getResultSetList(sql);
		    if(isTongguo){
				while(rs1.next()){
					 if(rs1.getLong("leibztb2_id")==1){
							liuczthjid = rs1.getLong("liuczthjid");
							liucdzb_id = rs1.getLong("id");
							qianqmc = rs1.getString("qianqmc");
							houjmc = rs1.getString("houjmc");
							caoz = rs1.getString("caoz");
							leibztb_id = rs1.getLong("leibztb_id");
							break;
						}
				}
		    }else{
		    	while(rs1.next()){
		    	if(rs1.getLong("leibztb2_id")!=1){
					liuczthjid = rs1.getLong("liuczthjid");
					liucdzb_id = rs1.getLong("id");
					qianqmc = rs1.getString("qianqmc");
					houjmc = rs1.getString("houjmc");
					caoz = rs1.getString("caoz");
					leibztb_id = rs1.getLong("leibztb_id");
					break;
				}
		    	}
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		如果类别状态表的后继状态为审核结束，同步电厂
//		if(leibztb_id==1){
//			if(!DiancUpdate(diancID,Jiesdh,4,1)){
////			   	throw new Exception("同步电厂时，数据库出现错误，请联系管理员");  	
//			}
//		}
		// 日志
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij,"
				+ "miaos)values(xl_xul_id.nextval,"
				+ TableID
				+ ","
				+ liucdzb_id
				+ ",'"
				+ qianqmc
				+ "','"
				+ houjmc
				+ "','"
				+ caoz
				+ "',(select quanc from renyxxb where id="
				+ renyxxb_id
				+ "),to_date('"
				+ format.format(new Date())
				+ "','YYYY-MM-DD HH24:mi:ss')" + ",'" + xiaox + "')";
		con.getInsert(sql);
		// if(leibztb_id==0||leibztb_id==1){//如果后继类别状态为0或１即为回退
		// sql=
		// "update "+TableName+"\n" +
		// "set "+TableName+".liucztb_id=" + leibztb_id
		// +","+TableName+".liucgzid="+TableID
		// +"where "+TableName+".id="+TableID;
		// }else{//如果后继为
		sql = "update " + TableName + "\n" + "set " + TableName
				+ ".liucztb_id=" + liuczthjid + "," + TableName + ".liucgzid="
				+ TableID + "where " + TableName + ".id=" + TableID;
		// }

		con.getUpdate(sql);
		con.Close();
	}

	/** 在未选择流程页面选择用此方法提交,此方法可以改变流程
	 *   isTongguo 代表是否直接通过 true代表直接通过  false 表示 提交
	 * */
	public static void tij(String TableName, long TableID, long renyxxb_id, long liucb_id,String xiaox,boolean isTongguo)throws Exception {// 提交

		JDBCcon con = new JDBCcon();
		String sql = "";
		long dangqdz = -1; // 表单当前状态
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		long diancID=-1;
		String Jiesdh="";

//		String sql_1 = "select distinct zt.liucb_id from "+TableName+"  js,liucb,liucztb zt where liucb.id=zt.liucb_id and zt.id=js.liucztb_id  and js.id="+TableID+"\n";
//		ResultSetList rs1 = con
//				.getResultSetList(sql_1);
//	  是切换流程修改状态
//		if (rs1.next()) {
				// 因为默认流程是“公司审核”，所以要改变为用户选择的流程
				 sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n"
						+ "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n"
						+ "where liucdzb.liucztqqid=liucztb1.id\n"
						+ " and liucdzb.liuczthjid=liucztb2.id\n"
						+ " and liucztb1.leibztb_id=leibztb1.id\n"
						+ " and liucztb2.leibztb_id=leibztb2.id\n"
						+ " and leibztb1.id=0\n"
						+ " and liucztb1.liucb_id="
						+ "(select id from liucb where liucb.id="+liucb_id+")\n"
						+ "  and liucztb1.xuh<liucztb2.xuh\n";
	     
				ResultSetList rsl = con.getResultSetList(sql);
				while(rsl.next()){
						liuczthjid = rsl.getLong("liuczthjid");
						liucdzb_id = rsl.getLong("id");
						qianqmc = rsl.getString("qianqmc");
						houjmc = rsl.getString("houjmc");
						caoz = rsl.getString("caoz");
						leibztb_id = rsl.getLong("leibztb_id");
					}
//				写入日志
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij,"
						+ "miaos)values(xl_xul_id.nextval,"
						+ TableID
						+ ","
						+ liucdzb_id
						+ ",'"
						+ qianqmc
						+ "','"
						+ houjmc
						+ "','"
						+ caoz
						+ "',(select quanc from renyxxb where id="
						+ renyxxb_id
						+ "),to_date('"
						+ format.format(new Date())
						+ "','YYYY-MM-DD HH24:mi:ss')" + ",'" + xiaox + "')";
				con.getInsert(sql);
	//更新标志
					sql = "update " + TableName + "\n" + "set " + TableName
							+ ".liucztb_id=" + liuczthjid + "," + TableName
							+ ".liucgzid=" + TableID + "where " + TableName
							+ ".id=" + TableID;
					con.getUpdate(sql);
				
				rsl.close();
//		}
//		rs1.close();
		con.Close();
		//提交上级

	}

	/** 一步步回退*/
	public static void huit(String TableName, long TableID, long renyxxb_id,
			String xiaox) {// 回退
		JDBCcon con = new JDBCcon();
		String sql = "";
		String dangqdz = ""; // 表单当前状态
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		long houjlbb_id=0;
		try {
			sql = "select liucztb_id\n" + // 获得当前状态
					"from " + TableName + "\n" + "where id=" + TableID;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				dangqdz = rs.getString(1);
			}
			sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.id houjlbb_id,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n"
					+ "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n"
					+ "where liucdzb.liucztqqid=liucztb1.id\n"
					+ " and liucdzb.liuczthjid=liucztb2.id\n"
					+ " and liucztb1.leibztb_id=leibztb1.id\n"
					+ " and liucztb2.leibztb_id=leibztb2.id\n"
					+ " and liucdzb.liucztqqid="
					+ dangqdz
					+ "  and liucztb1.xuh>liucztb2.xuh";// 方向是回退

			ResultSet rs1 = con.getResultSet(sql);
			if (rs1.next()) {
				houjlbb_id=rs1.getLong("houjlbb_id");
				liuczthjid = rs1.getLong("liuczthjid");
				liucdzb_id = rs1.getLong("id");
				qianqmc = rs1.getString("qianqmc");
				houjmc = rs1.getString("houjmc");
				caoz = rs1.getString("caoz");
				leibztb_id = rs1.getLong("leibztb_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 日志
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij,"
				+ "miaos)values(xl_xul_id.nextval,"
				+ TableID
				+ ","
				+ liucdzb_id
				+ ",'"
				+ qianqmc
				+ "','"
				+ houjmc
				+ "','"
				+ caoz
				+ "',(select quanc from renyxxb where id="
				+ renyxxb_id
				+ "),to_date('"
				+ format.format(new Date())
				+ "','YYYY-MM-DD HH24:mi:ss')" + ",'" + xiaox + "')";
		con.getInsert(sql);
		// if(leibztb_id==0||leibztb_id==1){//如果后继类别状态为0或１即为回退
		// sql=
		// "update "+TableName+"\n" +
		// "set "+TableName+".liucztb_id=" + leibztb_id
		// +","+TableName+".liucgzid="+TableID
		// +"where "+TableName+".id="+TableID;
		// }else{//如果后继为
		
        if(houjlbb_id==0){
        	liuczthjid=0;
		  }
		sql = "update " + TableName + "\n" + "set " + TableName
				+ ".liucztb_id=" + liuczthjid + "," + TableName + ".liucgzid="
				+ TableID + "where " + TableName + ".id=" + TableID;
		// }

		con.getUpdate(sql);
		con.Close();
	}

	/**
	 * 我的任务返回数据表的ｉｄ，类别中所有流程中我的数据表ｉｄ除了发起和结束状态的所有状态
	 * 
	 * @return
	 */
	public static String getWodrws(String tableName, long renyxxb_id,
			String leib) {
		String ids = "";
		JDBCcon con = new JDBCcon();
		//
		String sql =
		// "select "+tableName+".id\n" +
		// "from "+tableName+"\n" +
		// "where "+tableName+".liucztb_id in(\n" +
		// "select liucdzb.liucztqqid\n" +
		// "from liucdzjsb,liucdzb\n" +
		// "where liucdzjsb.liucdzb_id=liucdzb.id\n" +
		// "and liucdzjsb.liucjsb_id in\n" +
		// "(select liucjsb_id from renyjsb where renyxxb_id="+renyxxb_id+")\n"
		// +
		// ")";
		"select "
				+ tableName
				+ ".id\n"
				+ "from  "
				+ tableName
				+ "\n"
				+ "where  "
				+ tableName
				+ ".liucztb_id in(\n"
				+ "select liucdzb.liucztqqid\n"
				+ "from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n"
				+ "where liucdzjsb.liucdzb_id=liucdzb.id and liucdzb.liucztqqid=liucztb.id\n"
				+ "and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='"
				+ leib + "'\n" + "and liucdzjsb.liucjsb_id in\n"
				+ "(select liucjsb_id from renyjsb where renyxxb_id="
				+ renyxxb_id + ")\n" + ")";

		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				if (!ids.equals("")) {
					ids += ",";
				}
				ids += rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (ids.equals("")) {
			ids = "-1";
		}
		return ids;
	}

	/**
	 * 类别中所有流程中不是我的数据表ｉｄ
	 * 
	 * @return
	 */
	public static String getLiuczs(String tableName, long renyxxb_id,
			String leib) {
		String ids = "";
		JDBCcon con = new JDBCcon();
		String sql = "select "
				+ tableName
				+ ".id\n"
				+ "from  "
				+ tableName
				+ "\n"
				+ "where   "
				+ tableName
				+ ".liucztb_id in\n"
				+ "(\n"
				+ "select liucztb.id\n"
				+ "from liucztb,leibztb,liuclbb\n"
				+ "where liucztb.leibztb_id=leibztb.id and liuclbb.id=leibztb.liuclbb_id and liuclbb.mingc='"
				+ leib
				+ "'\n"
				+ "minus\n"
				+ "select distinct liucdzb.liucztqqid\n"
				+ "from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n"
				+ "where liucdzjsb.liucdzb_id=liucdzb.id and liucdzb.liucztqqid=liucztb.id\n"
				+ "and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='"
				+ leib + "'\n" + "and liucdzjsb.liucjsb_id in\n"
				+ "(select liucjsb_id from renyjsb where renyxxb_id="
				+ renyxxb_id + ")\n" + ")";
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				if (!ids.equals("")) {
					ids += ",";
				}
				ids += rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (ids.equals("")) {
			ids = "-1";
		}
		return ids;
	}

	public static int getShendId(long LiucID, long liucztb_id) {// 获得待审定状态ｉｄ
		String sql = "select id,rownum as nmber\n" + "from\n" + "(\n"
				+ "select id,rownum as nmber\n" + "from(\n" + "select id\n"
				+ "from liucztb\n" + "where liucb_id=" + LiucID
				+ " order by liucztb.xuh desc\n" + ")a\n" + ")\n"
				+ "where nmber=2";
		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sql);
		long ShendId = 0;
		try {
			if (rs.next()) {
				ShendId = rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		con.Close();
		if (liucztb_id == ShendId) {// 审定状态
			return 1;
		} else {
			return 0;
		}
	}

	public static List getRiz(long TableID) {// 日志bean
	// 加载意见
		JDBCcon con = new JDBCcon();
		List list = new ArrayList();
		String sql = "select rzb.qianqztmc,to_char(rzb.shij,'YYYY-MM-DD HH24:mi:ss')shij,rzb.liucdzbmc,rzb.houjztmc,rzb.miaos,rzb.caozy\n"
				+ "from liucgzb rzb\n"
				+ "where rzb.liucgzid="
				+ TableID
				+ " order by shij";
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				// String qianqztmc=rs.getString("qianqztmc");
				String caozy = rs.getString("caozy");
				String shij = rs.getString("shij");
				String liucdzbmc = rs.getString("liucdzbmc");
				String houjztmc = rs.getString("houjztmc");
				String miaos = rs.getString("miaos");
				list.add(new Yijbean(caozy + shij + liucdzbmc + houjztmc + ":",
						miaos));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		con.Close();
		return list;
	}

	/** 得到刚刚从电厂上传的，还未选择流程的条目,如果renyxxb_id为-1则为全部*/
	public static String getWeixz(String tableName, long renyxxb_id, String leib) {
		JDBCcon con = new JDBCcon();
		String ids = "";

		String sql = " select distinct djs.id \n"
				+ "from liucdzjsb ldz,liucdzb dz,liucztb zt1,leibztb lb1,liucztb zt2,leibztb lb2,renyjsb rj,"+tableName+" djs\n"
				+ "where\n" + " dz.liucztqqid=zt1.id\n"
				+ "and dz.liuczthjid=zt2.id\n"
				+ "and zt1.leibztb_id=lb1.id\n"
				+
				// "and zt2.leibztb_id=lb2.id\n"+
				"and zt2.id=djs.liucztb_id\n"
				+ "and lb1.mingc='发起'\n"
				+
				// "and lb2.mingc=''\n"+
				"and ldz.liucdzb_id=dz.id\n"
				+ "and ldz.liucjsb_id=rj.liucjsb_id\n" ;
				if(renyxxb_id!=-1){
				sql+= "and rj.renyxxb_id="+ renyxxb_id + "\n";
				}
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				if (!ids.equals("")) {
					ids += ",";
				}
				ids += rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (ids.equals("")) {
			ids = "-1";
		}
		return ids;
	}

	/** 得到未审核的任务,如果renyxxb_id为-1则为全部*/
	public static String getWeish(String tableName, long renyxxb_id, String leib) {
		JDBCcon con = new JDBCcon();
		String ids = "";
		String sql = " select distinct djs.id \n"
				+ "from liucdzjsb ldz,liucdzb dz,liucztb zt1,leibztb lb1,liucztb zt2,leibztb lb2,renyjsb rj,"+tableName+" djs\n"
				+ "where\n"
				+ " dz.liucztqqid=zt1.id\n"
				+ "and dz.liuczthjid=zt2.id\n"
				+ "and zt1.id=djs.liucztb_id\n"
				+
				// "and zt2.leibztb_id=\n"+
				// "and lb1.mingc='发起'\n"+
				// "and lb2.mingc=''\n"+
				"and ldz.liucdzb_id=dz.id\n"
				+ "and ldz.liucjsb_id=rj.liucjsb_id\n"+ "and zt1.xuh<zt2.xuh\n"  ;// 审核方向
//				+"and zt1.xuh>2\n";// 从的能操作的第二个流程，第一个流程属于上传电厂而未选择流程的任务
		if(renyxxb_id!=-1){
			sql+= "and rj.renyxxb_id="+ renyxxb_id + "\n";
			}
		
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				if (!ids.equals("")) {
					ids += ",";
				}
				ids += rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (ids.equals("")) {
			ids = "-1";
		}
		return ids;
	}
	//根据 流程名 和 人员id 判断当前人员所要操作的数据,返回liucztb_id,试用于  审核通过, 审核,和回退页面
	//( 原则是一个流程中不能有一个人不能担当两个角色)
	public static long getcaozsjzt(long renyxxb_id, String liucb_mingc) throws Exception{
		JDBCcon con = new JDBCcon();
	     long liucztb_id = -1;
		String sql = "select zt1.id from liucztb zt1,liucztb zt2,liucdzjsb dzjs ,liucdzb dz,renyjsb js,liucb lc where \n"+
                   " dzjs.liucdzb_id=dz.id\n"+
                  " and dzjs.liucjsb_id=js.liucjsb_id\n"+
                 "  and dz.liucztqqid=zt1.id\n"+
                "  and dz.liuczthjid=zt2.id\n"+
                "   and zt1.xuh<zt2.xuh\n"+// 审核方向
                "  and  zt1.liucb_id=lc.id\n"+
                "  and  lc.mingc='"+liucb_mingc+"'  \n";
	        sql+="\n";//
			sql+= "and js.renyxxb_id="+ renyxxb_id + "\n";
		ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				liucztb_id= rs.getLong("id");
			}
		return liucztb_id;
	}
	
	/** 得到已审核的任务,如果renyxxb_id为-1则为全部*/
	public static String getYish(String tableName, long renyxxb_id, String leib) {
		JDBCcon con = new JDBCcon();
		String ids = "";
		String sql = " select distinct djs.liucztb_id\n"
				+ "from liucdzjsb ldz,liucdzb dz,liucztb zt1,leibztb lb1,liucztb zt2,leibztb lb2,renyjsb rj,"+tableName+" djs\n"
				+ "where\n"
				+ " dz.liucztqqid=zt1.id\n"
				+ "and dz.liuczthjid=zt2.id\n"
				+
				// "and zt1.leibztb_id=djs.liucztb_id\n"+
				"and zt2.leibztb_id=lb2.id\n"
				+ "and zt2.id=djs.liucztb_id\n"
				+"and lb2.mingc='结束'\n" + "and ldz.liucdzb_id=dz.id\n"
				+ "and ldz.liucjsb_id=rj.liucjsb_id\n" ;
				if(renyxxb_id!=-1){
					sql+= "and rj.renyxxb_id="+ renyxxb_id + "\n";
				}
		// "and zt1.xuh<zt2.xuh";//审核方向
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				if (!ids.equals("")) {
					ids += ",";
				}
				ids += rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (ids.equals("")) {
			ids = "-1";
		}
		return ids;
	}
	/**同步电厂状态
     后两个状态 如果是	审核通过  为 4,1 如果是   删除了  为1,-1
     */
	 public static  boolean DiancUpdate(long diancID,String Jiesdh,int shenhjb,int zhuangt)throws Exception{
			
		 JDBCcon con = new JDBCcon();
		 ResultSet rs;
		 String sql = "";
		 String mhostname = "";
		 String msid = "";
		 String mduank = "";
		 String myonghm = "";
		 String mmim = "";
		 String mjiesb = "";
		 String mjiesbhzd = "";
		 String mpinysy = "";
		 String jiesb[]={};
//		滨河，联营     唐山
		 if(diancID==82 || diancID==102){
			 diancID = 211;
		 }
		 sql = "select pz.*,dc.piny from diancxtpz pz,diancxxb dc where pz.diancxxb_id=dc.id and pz.diancxxb_id="+diancID;
				
		 try{
			 rs = con.getResultSet(sql);
			 if(rs.next()){
			 mhostname = rs.getString("ip");
			 msid = rs.getString("sid");
			 mduank = rs.getString("duank");
			 myonghm = rs.getString("yonghm");
			 mmim = rs.getString("mim");
			 mjiesb = rs.getString("jiesb");
			 jiesb = mjiesb.split(",");
			 mjiesbhzd = rs.getString("jiesbhzd");
			 mpinysy = rs.getString("piny");
			 }

			 String bianh = Jiesdh.replaceAll(mpinysy, "");
			 if(diancID==211){
			 bianh = bianh.replaceAll("BH", "");
			 bianh = bianh.replaceAll("LY", "");
			 }
			 DBconn dbcn=new DBconn(mhostname,mduank,msid);
			 dbcn.setAutoCommit(false);
			 dbcn.setUserName(myonghm);
			 dbcn.setPassword(mmim);
			 for(int t=0;t<jiesb.length;t++){
				 int result=-1;
//				打包时需要将下面两个注释取消,同时取消643行,118行的注释
				 String dcsql1 = "update "+jiesb[t]+" set shenhjb="+shenhjb+",shenhzt="+zhuangt+" where "+mjiesbhzd+"='"+bianh+"'";
//				 result = dbcn.getUpdate(dcsql1);
				 System.out.println(dcsql1);
				 
//				测试与电厂数据库的连接
//				 String testsql = "select shenhjb,shenhzt from "+jiesb[t]+" where "+mjiesbhzd+"='"+bianh+"'";
//				 rs = dbcn.getResultSet(testsql);
//				 if(rs.next()){
//					 System.out.println("审核级别="+rs.getInt("shenhjb")+";审核状态="+rs.getInt("shenhzt"));
//					 return true;
//				 }
				 if(result<0){
					 dbcn.rollBack();
					 dbcn.close();
					 return false;
				 }
			 }
			 dbcn.commit();
			 dbcn.close();
		 }catch(Exception e){
			 e.printStackTrace();
			 throw new Exception("同步电厂时，数据库出现错误，请联系管理员");
		 }finally{
			 con.Close();
		 }
		 return true;
		 }
	 
/**回退电厂的动作,及删除本地，同时修改电厂条目的状态(只是用于diancjsmkb)
 * @return
 * */
		public static void getDeleteDcBalance(long diancjsmkb_id)throws Exception {

			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			
			
			//同步电厂
			 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+diancjsmkb_id+"\n";
			ResultSetList sr =con.getResultSetList(sqli);
			 if(sr.next()){
			      if(!DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
//				     throw new Exception("同步电厂时，数据库出现错误，请联系管理员");
			      }
			 }
			 
			con.getDelete(" delete from jieszbsjb ji where  ji.jiesdid="+diancjsmkb_id+"\n");
//                     先删电厂的,如果成功再删本地的
		    int  result1 =-1;
			String sql = "call deleteDiancJsd(" + diancjsmkb_id + ")";
			result1 = con.getUpdate(sql);
		   
			 
			
			if (result1>=0) {
				con.commit();
				System.out.println("结算单ID=" + diancjsmkb_id + "  操作成功！"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("回退电厂操作成功！");
			} else {
				con.rollBack();
				System.out.println("结算单ID=" + diancjsmkb_id + "  操作失败！"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("回退电厂操作失败！");
			}
	
		}
	//删除矿方结算煤款表的实现函数
		public static void TuiHuiDianChang_kuangfjsmkb(long kuangfjsmkb_id) throws Exception{

			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			
			
//			//同步电厂
//			 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+kuangfjsmkb_id+"\n";
//			ResultSetList sr =con.getResultSetList(sqli);
//			 if(sr.next()){
//			      if(!DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
////				     throw new Exception("同步电厂时，数据库出现错误，请联系管理员");
//			      }
//			 }
			 
			con.getDelete(" delete from jieszbsjb ji where  ji.jiesdid="+kuangfjsmkb_id+"\n");
//                     先删电厂的,如果成功再删本地的
		    int  result1 =-1;
			String sql = "call deletekuangfjsd(" + kuangfjsmkb_id + ")";
			
			result1 = con.getUpdate(sql);
		int result2=	con.getUpdate("update kuangfjsyfb set KUANGFJSMKB_ID=0 where KUANGFJSMKB_ID="+kuangfjsmkb_id+"\n");
		    
			 
			
			if (result2>=0&&result1>=0) {
				con.commit();
				System.out.println("结算单ID=" + kuangfjsmkb_id + "  操作成功！"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("回退电厂操作成功！");
			} else {
				con.rollBack();
				System.out.println("结算单ID=" + kuangfjsmkb_id + "  操作失败！"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("回退电厂操作失败！");
			}
	
		}
//		删除厂方结算表
		public static void TuiHuiDianChang_jsmkb(long diancjsmkb_id) throws Exception{

			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			
			
//			//同步电厂
//			 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+kuangfjsmkb_id+"\n";
//			ResultSetList sr =con.getResultSetList(sqli);
//			 if(sr.next()){
//			      if(!DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
////				     throw new Exception("同步电厂时，数据库出现错误，请联系管理员");
//			      }
//			 }
			 
			con.getDelete(" delete from jieszbsjb ji where  ji.jiesdid="+diancjsmkb_id+"\n");
//                     先删电厂的,如果成功再删本地的
		    int  result1 =-1;
			String sql = "call deletediancjsd(" + diancjsmkb_id + ")";
			
			result1 = con.getUpdate(sql);
		int result2=	con.getUpdate("update diancjsyfb set diancJSMKB_ID=0 where diancJSMKB_ID="+diancjsmkb_id+"\n");
		    
			 
			
			if (result2>=0&&result1>=0) {
				con.commit();
				System.out.println("结算单ID=" + diancjsmkb_id + "  操作成功！"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("回退电厂操作成功！");
			} else {
				con.rollBack();
				System.out.println("结算单ID=" + diancjsmkb_id + "  操作失败！"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("回退电厂操作失败！");
			}
	
		}
		/**
		 * 形成选择流程的下拉菜单的list
		 * */
		public static ArrayList getIcanliuc(long renyxxb_id,String Liuclbb_mingc){
			JDBCcon con = new JDBCcon();
			String sql=
			"select  lc.id,lc.mingc from liucb lc,liuclbb lb,liucdzb dz,\n" +
			"liucztb zt1,liucztb zt2,liucdzjsb dzjs,liucjsb ljs,renyjsb rjs where lc.liuclbb_id=lb.id\n"+
            "and lb.mingc='"+Liuclbb_mingc+"' and dz.liucztqqid=zt1.id and zt1.leibztb_id=0\n"+ 
            "and dz.liuczthjid=zt2.id and zt2.liucb_id=lc.id and dzjs.liucdzb_id=dz.id\n"+ 
            "and dzjs.liucjsb_id=ljs.id and rjs.liucjsb_id=ljs.id and rjs.renyxxb_id="+renyxxb_id+"\n";
         ResultSetList rsl=con.getResultSetList(sql);
			ArrayList l=new ArrayList();
			while (rsl.next()) {
						l.add(new IDropDownBean(rsl.getLong("id"), rsl
								.getString("mingc")));

			}
			con.Close();
			return l;
		}
		/**
		 * 判断一个角色是不是最后一步审批的流程
		 * */
		public static boolean isLastLeader(long renyxxb_id,String liucb_mingc){
			  JDBCcon jcon=new JDBCcon();
			  
			  String sql="select zt2.leibztb_id from liucdzb dz,liucztb zt1 ,liucztb zt2,liucdzjsb dzjs,liucjsb ljs,renyjsb rjs ,liucb lcb\n" +
			  		"     where dzjs.liucdzb_id=dz.id and  dzjs.liucjsb_id=ljs.id\n"+
                      "   and rjs.liucjsb_id=ljs.id\n " +
                      "and dz.liucztqqid=zt1.id\n" +
                      " and dz.liuczthjid=zt2.id\n" +
                      "and zt1.liucb_id=lcb.id\n"+
                      "and zt2.liucb_id=lcb.id\n"+
                      "and lcb.mingc='"+liucb_mingc+"'\n"+
                      " and zt1.xuh<zt2.xuh \n" +
                      "and rjs.renyxxb_id="+renyxxb_id+"\n";
			  ResultSetList rsl=jcon.getResultSetList(sql);
			  while(rsl.next()){
				  if(rsl.getRows()==1&&rsl.getLong("leibztb_id")==1){
					  return true;
				  }
			  }
			    return false;
		}
	
		/**
		 * 判断一个角色是不是第一步审批的流程
		 * *//*
		public static boolean isFirstRenY(long renyxxb_id,String liuclbb_mingc){
			 JDBCcon jcon=new JDBCcon();
			 String sql="select zt2.leibztb_id from liucdzb dz,liucztb zt1 ,liucztb zt2,liucdzjsb dzjs,liucjsb ljs,renyjsb rjs,liucb lcb\n" +
		  		"     where dzjs.liucdzb_id=dz.id and  dzjs.liucjsb_id=ljs.id\n"+
               "   and rjs.liucjsb_id=ljs.id\n " +
               "and dz.liucztqqid=zt1.id\n" +
               " and dz.liuczthjid=zt2.id\n" +
               "and zt1.liucb_id=lcb.id\n"+
               "and zt2.liucb_id=lcb.id\n"+
               "and lcb.mingc='"+liuclbb_mingc+"'\n"+
               " and zt1.xuh<zt2.xuh \n" +
               "and zt1.leibztb_id=0\n"+
               "and rjs.renyxxb_id="+renyxxb_id+"\n";
			return jcon.getHasIt(sql);
		}*/
	//根据流程动作表判断此次操作所拥有的权限
		public static boolean  isHadThis(long renyxxb_id,String liuclbb_mingc,String DongZuoMingCheng){
			JDBCcon jcon=new JDBCcon();

			String sql="select dz.dongz,zt2.leibztb_id from liucdzb dz,liucztb zt1 ,liucztb zt2,liucdzjsb dzjs,liucjsb ljs,renyjsb rjs,liucb lcb\n"+
					  	"	where dzjs.liucdzb_id=dz.id\n"+
			             "    and  dzjs.liucjsb_id=ljs.id\n"+
			              "   and rjs.liucjsb_id=ljs.id\n"+
			               "  and dz.liucztqqid=zt1.id\n"+
			                " and dz.liuczthjid=zt2.id\n"+
			                 "and zt1.liucb_id=lcb.id\n"+
			                 "and zt2.liucb_id=lcb.id\n"+
			                 "and  lcb.mingc='"+liuclbb_mingc+"'\n"+
			                 "and zt1.xuh<zt2.xuh\n"+
			                 "and rjs.renyxxb_id="+renyxxb_id+"\n";
			ResultSetList rsl=	jcon.getResultSetList(sql);
			while(rsl.next()){
				if(rsl.getString("dongz").indexOf(DongZuoMingCheng)!=-1){
					return true;
				}
			}
			return false;
		}
//  根据流程名的出流程最后一步的liucztb_id
	public static long getLastStatus(String liucm){
		JDBCcon jcon=new JDBCcon();
		String sql=" select lzt_.id from liucztb lzt_ ,liucb liu_\n"+
                         " where liu_.id=lzt_.liucb_id\n"+
                          "     and liu_.mingc='"+liucm+"'\n"+
                           "    and lzt_.xuh =(\n"+
                            "                        select max(zt1.xuh) from liucdzb dz,liucb liuc ,liucztb zt1,liucztb zt2\n"+
                             "                           where dz.liucztqqid=zt1.id\n"+
                              "                           and dz.liuczthjid=zt2.id\n"+
                             "                            and liuc.id=zt1.liucb_id\n"+
                             "                            and liuc.id=zt2.liucb_id\n"+
                             "                            and liuc.mingc='"+liucm+"'\n"+
                             "                            and zt1.xuh<zt2.xuh\n"+
                             "                            and zt2.leibztb_id=1\n"+
                             "                 )";
		ResultSetList rsl=jcon.getResultSetList(sql);
		if(rsl.next()){
			return rsl.getLong("id");
		}
		return 0;

	}
}
