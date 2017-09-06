package com.zhiren.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;

public class Liucdzcl {

	//发票操作时，需要把所有的id传到发票页面进行判断，看是否是单张发票
	public static String idStrSource;
	// 把表名和表id的list解析成字符串
	// 例如：jiesb,3001;jiesyfb,3002;&jiesb,3003;jiesyfb,3004;&	
	public static String TableNameIdStr(List TableName, List TableID) {

		String s = "";

		if (TableName != null && TableID != null
				&& TableName.size() == TableID.size()) {

			for (int i = 0; i < TableName.size(); i++) {

				Object objName = TableName.get(i);
				Object objID = TableID.get(i);

				// 属于两票结算
				if (objName instanceof List) {

					List listName = (List) objName;
					List listID = (List) objID;

					for (int j = 0; j < listName.size(); j++) {

						s += listName.get(j) + "," + listID.get(j) + ";";
					}
					s += "&";

				} else {
					s += (String) objName + "," + (String) objID + ";";
					s += "&";
				}
			}
		}
		return s;
	}	

	// 把表名和表id的字符串组成list,给list参数TableName,TableID赋值操作
	public static void TableNameIdList(List TableName, List TableID, String s) {

		if (s != null && s.length() > 0) {
			if (TableName == null) {
				TableName = new ArrayList();
			}
			if (TableID == null) {
				TableID = new ArrayList();
			}
			// 第一次按&解析后是类别数组，如两票，运费等
			String[] leibArr = s.split("&");

			for (int i = 0; i < leibArr.length; i++) {

				String str = leibArr[i];

				// 解析后是种类数组
				String[] zhonglArr = str.split(";");

				if (zhonglArr.length == 1) { // 不是两票结算

					String[] douh = zhonglArr[0].split(","); // 分隔逗号，提取name
					// id

					TableName.add(douh[0]);
					TableID.add(douh[1]);

				} else { // 是两票结算

					List listName = new ArrayList();
					List listID = new ArrayList();

					for (int j = 0; j < zhonglArr.length; j++) {

						String[] douh = zhonglArr[j].split(",");
						listName.add(douh[0]);
						listID.add(douh[1]);

					}

					TableName.add(listName);
					TableID.add(listID);
				}

			}

		}

	}

	public static String Dongzcl(String Liucdzb_ID,List TableName,List Table_ID) {

		// 动作处理
		// 参数：动作要处理的表名，表id，流程动作表id
		// 逻辑：先通过“流程动作表id”找到流程动作中要触发的动作
		// 处理方式：用循环list的方式取出表名和表id，再将其以参数的形式传到下一个页面
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		visit.setList1(null);
		// visit.setLiucclsb(new StringBuffer(paramters));
//		把表名和表id的list解析成字符串.例如：jiesb,3001;jiesyfb,3002;&jiesb,3003;jiesyfb,3004;&	
		String Tb_and_tId = TableNameIdStr(TableName,Table_ID);
		if(Tb_and_tId!= null && Tb_and_tId.length() > 0){
			Tb_and_tId = Tb_and_tId.substring(0,Tb_and_tId.length()-2);
		}
		String tmp = "";
		try {
			String dongz = "";
			String sql = "select dongz from liucdzb where id=" + Liucdzb_ID;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				dongz = rs.getString("dongz");
			}

			if (dongz!=null && !dongz.equals("")) {

				if (dongz.equals(SysConstant.Liucdz_ShenhOrHuitBtxyj)) {

					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
							+ "	var end = url.indexOf(';');"
							+ "	url = url.substring(0,end);"
							+ "	url = url + '?service=page/' + 'Jieslc_Yijtx&menuId="
							+ Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
				} else if (dongz.equals("Jieslc_Yijtx&lx=Cwrz")) {

					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
							+ "	var end = url.indexOf(';');"
							+ "	url = url.substring(0,end);"
							+ "	url = url + '?service=page/"
							+ dongz
							+ "&menuId=" + Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
				}else if (dongz.equals("Hetlc_Yijtx&lx=Ghth")) {

					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
							+ "	var end = url.indexOf(';');"
							+ "	url = url.substring(0,end);"
							+ "	url = url + '?service=page/' + 'Hetlc_Yijtx&menuId="
							+ Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
				}else if (dongz.equals("Hetlc_Yijtx")) {

						String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
								+ "	var end = url.indexOf(';');"
								+ "	url = url.substring(0,end);"
								+ "	url = url + '?service=page/' + 'Hetlc_Yij&menuId="
								+ Liucdzb_ID + "';";

						tmp = url + "var a=window.open(url,'newWin');";
				} else if (dongz.equals("Jieslc_Faptx")) {
					
					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
							+ "	var end = url.indexOf(';');"
							+ "	url = url.substring(0,end);"
							+ "	url = url + '?service=page/' + 'Jieslc_Faptx&menuId="
							+ Liucdzb_ID + "&fP_id="+groupIdStr(idStrSource)+"&Tb_name_id="+Tb_and_tId+"';";

					tmp = url + "var a=window.open(url,'newWin');";
              
				} else if(dongz.equals("Jieslc_Yijtx&lx=Update")){
					
					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
						+ "	var end = url.indexOf(';');"
						+ "	url = url.substring(0,end);"
						+ "	url = url + '?service=page/"
						+ dongz
						+ "&menuId=" + Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
					
				} else if(dongz.equals("Huayejsh")){
					
					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
						+ "	var end = url.indexOf(';');"
						+ "	url = url.substring(0,end);"
						+ "	url = url + '?service=page/"
						+ dongz
						+ "&menuId=" + Liucdzb_ID + "&Tb_name_id=" + Tb_and_tId + "';";

					tmp = url + "var a=window.open(url,'newWin');";
					
				} else if(dongz.equals("Jieslc_Yijtx&lx=Insert")){
					
					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
						+ "	var end = url.indexOf(';');"
						+ "	url = url.substring(0,end);"
						+ "	url = url + '?service=page/"
						+ dongz
						+ "&menuId=" + Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
				}
			}

			rs.close();
			
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {

			con.Close();
		}
		
		if(tmp==null || tmp.equals("")){
			tmp="Ext.Msg.alert('提示信息','相关资源须在系统中事先配置!');";
		}
		return tmp;
	}
	public static String groupIdStr(String sId){
		
		String idStr="";
		String[] arr=sId.split(";");  //拆解成单个记录
		
		if(arr != null){
			
			
			for(int i=0;i<arr.length;i++){
				
				String[] idArr=arr[i].split(",");//得到每个记录的id
				
				idStr+=idArr[i];
				
				if(i != arr.length-1){
					idStr+=",";
				}
			}
			
		}
		
		return idStr;
		
	}
	
	public static String[] parseIdStr(String sId){
		if(sId != null){
			String[] arr=sId.split(",");
			return arr;
		}else{
			return new String[]{""};
		}
		
	}

	public static void tij(String TableName, long TableID, long renyxxb_id,
			String xiaox, long liucb_id) {// 提交
		JDBCcon con = new JDBCcon();
		String sql = "";
		// long dangqdz=-1; //表单当前状态
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		try {
			// sql="select liucztb_id\n" +//获得当前状态
			// "from "+TableName+"\n" +
			// "where id="+TableID;
			// ResultSet rs=con.getResultSet(sql);
			// if(rs.next()){
			// dangqdz=rs.getLong(1);
			// }
			// 为初始提交，这时要要找他的后继则要有流程ｉｄ和leibztb.id=0来作为条件查找动作表
			sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n"
					+ "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n"
					+ "where liucdzb.liucztqqid=liucztb1.id\n"
					+ " and liucdzb.liuczthjid=liucztb2.id\n"
					+ " and liucztb1.leibztb_id=leibztb1.id\n"
					+ " and liucztb2.leibztb_id=leibztb2.id\n"
					+ " and leibztb1.id=0\n"
					+ " and liucztb1.liucb_id="
					+ liucb_id + "  and liucztb1.xuh<liucztb2.xuh";
			ResultSet rs1 = con.getResultSet(sql);
			if (rs1.next()) {
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
		if (leibztb_id == 0 || leibztb_id == 1) {// 如果后继类别状态为0或１即为回退
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + leibztb_id + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		} else {// 如果后继为
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + liuczthjid + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		}
		con.getUpdate(sql);
		con.Close();
	}

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
		try {
			sql = "select liucztb_id\n" + // 获得当前状态
					"from " + TableName + "\n" + "where id=" + TableID;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				dangqdz = rs.getString(1);
			}
			sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n"
					+ "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n"
					+ "where liucdzb.liucztqqid=liucztb1.id\n"
					+ " and liucdzb.liuczthjid=liucztb2.id\n"
					+ " and liucztb1.leibztb_id=leibztb1.id\n"
					+ " and liucztb2.leibztb_id=leibztb2.id\n"
					+ " and liucdzb.liucztqqid="
					+ dangqdz
					+ "  and liucztb1.xuh>liucztb2.xuh";
			ResultSet rs1 = con.getResultSet(sql);
			if (rs1.next()) {
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
		if (leibztb_id == 0 || leibztb_id == 1) {// 如果后继类别状态为0或１即为回退
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + leibztb_id + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		} else {// 如果后继为
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + liuczthjid + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		}
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
	
	public static String getOpenWinScr(String Liucdzb_ID,List TableName,List Table_ID){
		// 动作处理
		// 参数：动作要处理的表名，表id，流程动作表id
		// 逻辑：先通过“流程动作表id”找到流程动作中要触发的动作
		String tmp = "";
					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
							+ "	var end = url.indexOf(';');"
							+ "	url = url.substring(0,end);"
							+ "	url = url + '?service=page/' + 'Tableshyj&menuId="
							+ Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
	
		if(tmp==null || tmp.equals("")){
			tmp="Ext.Msg.alert('提示信息','相关资源须在系统中事先配置!');";
		}
		return tmp;
	
	}
}
