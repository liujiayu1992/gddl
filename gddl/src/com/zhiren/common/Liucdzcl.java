package com.zhiren.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;

public class Liucdzcl {

	//��Ʊ����ʱ����Ҫ�����е�id������Ʊҳ������жϣ����Ƿ��ǵ��ŷ�Ʊ
	public static String idStrSource;
	// �ѱ����ͱ�id��list�������ַ���
	// ���磺jiesb,3001;jiesyfb,3002;&jiesb,3003;jiesyfb,3004;&	
	public static String TableNameIdStr(List TableName, List TableID) {

		String s = "";

		if (TableName != null && TableID != null
				&& TableName.size() == TableID.size()) {

			for (int i = 0; i < TableName.size(); i++) {

				Object objName = TableName.get(i);
				Object objID = TableID.get(i);

				// ������Ʊ����
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

	// �ѱ����ͱ�id���ַ������list,��list����TableName,TableID��ֵ����
	public static void TableNameIdList(List TableName, List TableID, String s) {

		if (s != null && s.length() > 0) {
			if (TableName == null) {
				TableName = new ArrayList();
			}
			if (TableID == null) {
				TableID = new ArrayList();
			}
			// ��һ�ΰ�&��������������飬����Ʊ���˷ѵ�
			String[] leibArr = s.split("&");

			for (int i = 0; i < leibArr.length; i++) {

				String str = leibArr[i];

				// ����������������
				String[] zhonglArr = str.split(";");

				if (zhonglArr.length == 1) { // ������Ʊ����

					String[] douh = zhonglArr[0].split(","); // �ָ����ţ���ȡname
					// id

					TableName.add(douh[0]);
					TableID.add(douh[1]);

				} else { // ����Ʊ����

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

		// ��������
		// ����������Ҫ����ı�������id�����̶�����id
		// �߼�����ͨ�������̶�����id���ҵ����̶�����Ҫ�����Ķ���
		// ����ʽ����ѭ��list�ķ�ʽȡ�������ͱ�id���ٽ����Բ�������ʽ������һ��ҳ��
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		visit.setList1(null);
		// visit.setLiucclsb(new StringBuffer(paramters));
//		�ѱ����ͱ�id��list�������ַ���.���磺jiesb,3001;jiesyfb,3002;&jiesb,3003;jiesyfb,3004;&	
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {

			con.Close();
		}
		
		if(tmp==null || tmp.equals("")){
			tmp="Ext.Msg.alert('��ʾ��Ϣ','�����Դ����ϵͳ����������!');";
		}
		return tmp;
	}
	public static String groupIdStr(String sId){
		
		String idStr="";
		String[] arr=sId.split(";");  //���ɵ�����¼
		
		if(arr != null){
			
			
			for(int i=0;i<arr.length;i++){
				
				String[] idArr=arr[i].split(",");//�õ�ÿ����¼��id
				
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
			String xiaox, long liucb_id) {// �ύ
		JDBCcon con = new JDBCcon();
		String sql = "";
		// long dangqdz=-1; //����ǰ״̬
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		try {
			// sql="select liucztb_id\n" +//��õ�ǰ״̬
			// "from "+TableName+"\n" +
			// "where id="+TableID;
			// ResultSet rs=con.getResultSet(sql);
			// if(rs.next()){
			// dangqdz=rs.getLong(1);
			// }
			// Ϊ��ʼ�ύ����ʱҪҪ�����ĺ����Ҫ�����̣���leibztb.id=0����Ϊ�������Ҷ�����
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
		// ��־
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
		if (leibztb_id == 0 || leibztb_id == 1) {// ���������״̬Ϊ0�򣱼�Ϊ����
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + leibztb_id + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		} else {// ������Ϊ
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + liuczthjid + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		}
		con.getUpdate(sql);
		con.Close();
	}

	public static void huit(String TableName, long TableID, long renyxxb_id,
			String xiaox) {// ����
		JDBCcon con = new JDBCcon();
		String sql = "";
		String dangqdz = ""; // ����ǰ״̬
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		try {
			sql = "select liucztb_id\n" + // ��õ�ǰ״̬
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
		// ��־
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
		if (leibztb_id == 0 || leibztb_id == 1) {// ���������״̬Ϊ0�򣱼�Ϊ����
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + leibztb_id + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		} else {// ������Ϊ
			sql = "update " + TableName + "\n" + "set " + TableName
					+ ".liucztb_id=" + liuczthjid + "," + TableName
					+ ".liucgzid=" + TableID + "where " + TableName + ".id="
					+ TableID;
		}
		con.getUpdate(sql);
		con.Close();
	}

	/**
	 * �ҵ����񷵻����ݱ�ģ�䣬����������������ҵ����ݱ�����˷���ͽ���״̬������״̬
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
	 * ��������������в����ҵ����ݱ���
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

	public static int getShendId(long LiucID, long liucztb_id) {// ��ô���״̬���
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
		if (liucztb_id == ShendId) {// ��״̬
			return 1;
		} else {
			return 0;
		}
	}

	public static List getRiz(long TableID) {// ��־bean
		// �������
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
		// ��������
		// ����������Ҫ����ı�������id�����̶�����id
		// �߼�����ͨ�������̶�����id���ҵ����̶�����Ҫ�����Ķ���
		String tmp = "";
					String url = "  	var url = 'http://'+document.location.host+document.location.pathname;"
							+ "	var end = url.indexOf(';');"
							+ "	url = url.substring(0,end);"
							+ "	url = url + '?service=page/' + 'Tableshyj&menuId="
							+ Liucdzb_ID + "';";

					tmp = url + "var a=window.open(url,'newWin');";
	
		if(tmp==null || tmp.equals("")){
			tmp="Ext.Msg.alert('��ʾ��Ϣ','�����Դ����ϵͳ����������!');";
		}
		return tmp;
	
	}
}
