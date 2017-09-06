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

	// private static String TableName="";//���ݱ���
	// private static String TableID="0";//���ݱ����
	// private static String LiucID="0";
	// // private static String wodrwIds="";
	// // private static String liuczIds="";
	// private static String renyxxb_id="";
	// private static String leibb_id="";
	// private static String xiaox="";//�ύ����ǰ�Ļ�д����־
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
	/** ��δ���ҳ���ô˷����ύ
	 *  isTongguo �����Ƿ�ֱ��ͨ�� true����ֱ��ͨ��  false ��ʾ �ύ
 	 * */
	public static void tij(String TableName, long TableID, long renyxxb_id,boolean isTongguo,
			String xiaox) throws Exception {// �ύ
		JDBCcon con = new JDBCcon();
		String sql = "";
		long dangqdz = -1; // ����ǰ״̬
		long liuczthjid = 0;
		long liucdzb_id = 0;
		String qianqmc = "";
		String houjmc = "";
		String caoz = "";
		long leibztb_id = -1;
		long diancID=-1;
		String Jiesdh="";
		try {
			sql = "select liucztb_id--,bianm,diancxxb_id\n" + // ��õ�ǰ״̬
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
//		������״̬��ĺ��״̬Ϊ��˽�����ͬ���糧
//		if(leibztb_id==1){
//			if(!DiancUpdate(diancID,Jiesdh,4,1)){
////			   	throw new Exception("ͬ���糧ʱ�����ݿ���ִ�������ϵ����Ա");  	
//			}
//		}
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
		// if(leibztb_id==0||leibztb_id==1){//���������״̬Ϊ0�򣱼�Ϊ����
		// sql=
		// "update "+TableName+"\n" +
		// "set "+TableName+".liucztb_id=" + leibztb_id
		// +","+TableName+".liucgzid="+TableID
		// +"where "+TableName+".id="+TableID;
		// }else{//������Ϊ
		sql = "update " + TableName + "\n" + "set " + TableName
				+ ".liucztb_id=" + liuczthjid + "," + TableName + ".liucgzid="
				+ TableID + "where " + TableName + ".id=" + TableID;
		// }

		con.getUpdate(sql);
		con.Close();
	}

	/** ��δѡ������ҳ��ѡ���ô˷����ύ,�˷������Ըı�����
	 *   isTongguo �����Ƿ�ֱ��ͨ�� true����ֱ��ͨ��  false ��ʾ �ύ
	 * */
	public static void tij(String TableName, long TableID, long renyxxb_id, long liucb_id,String xiaox,boolean isTongguo)throws Exception {// �ύ

		JDBCcon con = new JDBCcon();
		String sql = "";
		long dangqdz = -1; // ����ǰ״̬
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
//	  ���л������޸�״̬
//		if (rs1.next()) {
				// ��ΪĬ�������ǡ���˾��ˡ�������Ҫ�ı�Ϊ�û�ѡ�������
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
//				д����־
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
	//���±�־
					sql = "update " + TableName + "\n" + "set " + TableName
							+ ".liucztb_id=" + liuczthjid + "," + TableName
							+ ".liucgzid=" + TableID + "where " + TableName
							+ ".id=" + TableID;
					con.getUpdate(sql);
				
				rsl.close();
//		}
//		rs1.close();
		con.Close();
		//�ύ�ϼ�

	}

	/** һ��������*/
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
		long houjlbb_id=0;
		try {
			sql = "select liucztb_id\n" + // ��õ�ǰ״̬
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
					+ "  and liucztb1.xuh>liucztb2.xuh";// �����ǻ���

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
		// if(leibztb_id==0||leibztb_id==1){//���������״̬Ϊ0�򣱼�Ϊ����
		// sql=
		// "update "+TableName+"\n" +
		// "set "+TableName+".liucztb_id=" + leibztb_id
		// +","+TableName+".liucgzid="+TableID
		// +"where "+TableName+".id="+TableID;
		// }else{//������Ϊ
		
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

	/** �õ��ոմӵ糧�ϴ��ģ���δѡ�����̵���Ŀ,���renyxxb_idΪ-1��Ϊȫ��*/
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
				+ "and lb1.mingc='����'\n"
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

	/** �õ�δ��˵�����,���renyxxb_idΪ-1��Ϊȫ��*/
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
				// "and lb1.mingc='����'\n"+
				// "and lb2.mingc=''\n"+
				"and ldz.liucdzb_id=dz.id\n"
				+ "and ldz.liucjsb_id=rj.liucjsb_id\n"+ "and zt1.xuh<zt2.xuh\n"  ;// ��˷���
//				+"and zt1.xuh>2\n";// �ӵ��ܲ����ĵڶ������̣���һ�����������ϴ��糧��δѡ�����̵�����
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
	//���� ������ �� ��Աid �жϵ�ǰ��Ա��Ҫ����������,����liucztb_id,������  ���ͨ��, ���,�ͻ���ҳ��
	//( ԭ����һ�������в�����һ���˲��ܵ���������ɫ)
	public static long getcaozsjzt(long renyxxb_id, String liucb_mingc) throws Exception{
		JDBCcon con = new JDBCcon();
	     long liucztb_id = -1;
		String sql = "select zt1.id from liucztb zt1,liucztb zt2,liucdzjsb dzjs ,liucdzb dz,renyjsb js,liucb lc where \n"+
                   " dzjs.liucdzb_id=dz.id\n"+
                  " and dzjs.liucjsb_id=js.liucjsb_id\n"+
                 "  and dz.liucztqqid=zt1.id\n"+
                "  and dz.liuczthjid=zt2.id\n"+
                "   and zt1.xuh<zt2.xuh\n"+// ��˷���
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
	
	/** �õ�����˵�����,���renyxxb_idΪ-1��Ϊȫ��*/
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
				+"and lb2.mingc='����'\n" + "and ldz.liucdzb_id=dz.id\n"
				+ "and ldz.liucjsb_id=rj.liucjsb_id\n" ;
				if(renyxxb_id!=-1){
					sql+= "and rj.renyxxb_id="+ renyxxb_id + "\n";
				}
		// "and zt1.xuh<zt2.xuh";//��˷���
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
	/**ͬ���糧״̬
     ������״̬ �����	���ͨ��  Ϊ 4,1 �����   ɾ����  Ϊ1,-1
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
//		���ӣ���Ӫ     ��ɽ
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
//				���ʱ��Ҫ����������ע��ȡ��,ͬʱȡ��643��,118�е�ע��
				 String dcsql1 = "update "+jiesb[t]+" set shenhjb="+shenhjb+",shenhzt="+zhuangt+" where "+mjiesbhzd+"='"+bianh+"'";
//				 result = dbcn.getUpdate(dcsql1);
				 System.out.println(dcsql1);
				 
//				������糧���ݿ������
//				 String testsql = "select shenhjb,shenhzt from "+jiesb[t]+" where "+mjiesbhzd+"='"+bianh+"'";
//				 rs = dbcn.getResultSet(testsql);
//				 if(rs.next()){
//					 System.out.println("��˼���="+rs.getInt("shenhjb")+";���״̬="+rs.getInt("shenhzt"));
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
			 throw new Exception("ͬ���糧ʱ�����ݿ���ִ�������ϵ����Ա");
		 }finally{
			 con.Close();
		 }
		 return true;
		 }
	 
/**���˵糧�Ķ���,��ɾ�����أ�ͬʱ�޸ĵ糧��Ŀ��״̬(ֻ������diancjsmkb)
 * @return
 * */
		public static void getDeleteDcBalance(long diancjsmkb_id)throws Exception {

			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			
			
			//ͬ���糧
			 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+diancjsmkb_id+"\n";
			ResultSetList sr =con.getResultSetList(sqli);
			 if(sr.next()){
			      if(!DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
//				     throw new Exception("ͬ���糧ʱ�����ݿ���ִ�������ϵ����Ա");
			      }
			 }
			 
			con.getDelete(" delete from jieszbsjb ji where  ji.jiesdid="+diancjsmkb_id+"\n");
//                     ��ɾ�糧��,����ɹ���ɾ���ص�
		    int  result1 =-1;
			String sql = "call deleteDiancJsd(" + diancjsmkb_id + ")";
			result1 = con.getUpdate(sql);
		   
			 
			
			if (result1>=0) {
				con.commit();
				System.out.println("���㵥ID=" + diancjsmkb_id + "  �����ɹ���"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("���˵糧�����ɹ���");
			} else {
				con.rollBack();
				System.out.println("���㵥ID=" + diancjsmkb_id + "  ����ʧ�ܣ�"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("���˵糧����ʧ�ܣ�");
			}
	
		}
	//ɾ���󷽽���ú����ʵ�ֺ���
		public static void TuiHuiDianChang_kuangfjsmkb(long kuangfjsmkb_id) throws Exception{

			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			
			
//			//ͬ���糧
//			 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+kuangfjsmkb_id+"\n";
//			ResultSetList sr =con.getResultSetList(sqli);
//			 if(sr.next()){
//			      if(!DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
////				     throw new Exception("ͬ���糧ʱ�����ݿ���ִ�������ϵ����Ա");
//			      }
//			 }
			 
			con.getDelete(" delete from jieszbsjb ji where  ji.jiesdid="+kuangfjsmkb_id+"\n");
//                     ��ɾ�糧��,����ɹ���ɾ���ص�
		    int  result1 =-1;
			String sql = "call deletekuangfjsd(" + kuangfjsmkb_id + ")";
			
			result1 = con.getUpdate(sql);
		int result2=	con.getUpdate("update kuangfjsyfb set KUANGFJSMKB_ID=0 where KUANGFJSMKB_ID="+kuangfjsmkb_id+"\n");
		    
			 
			
			if (result2>=0&&result1>=0) {
				con.commit();
				System.out.println("���㵥ID=" + kuangfjsmkb_id + "  �����ɹ���"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("���˵糧�����ɹ���");
			} else {
				con.rollBack();
				System.out.println("���㵥ID=" + kuangfjsmkb_id + "  ����ʧ�ܣ�"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("���˵糧����ʧ�ܣ�");
			}
	
		}
//		ɾ�����������
		public static void TuiHuiDianChang_jsmkb(long diancjsmkb_id) throws Exception{

			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			
			
//			//ͬ���糧
//			 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+kuangfjsmkb_id+"\n";
//			ResultSetList sr =con.getResultSetList(sqli);
//			 if(sr.next()){
//			      if(!DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
////				     throw new Exception("ͬ���糧ʱ�����ݿ���ִ�������ϵ����Ա");
//			      }
//			 }
			 
			con.getDelete(" delete from jieszbsjb ji where  ji.jiesdid="+diancjsmkb_id+"\n");
//                     ��ɾ�糧��,����ɹ���ɾ���ص�
		    int  result1 =-1;
			String sql = "call deletediancjsd(" + diancjsmkb_id + ")";
			
			result1 = con.getUpdate(sql);
		int result2=	con.getUpdate("update diancjsyfb set diancJSMKB_ID=0 where diancJSMKB_ID="+diancjsmkb_id+"\n");
		    
			 
			
			if (result2>=0&&result1>=0) {
				con.commit();
				System.out.println("���㵥ID=" + diancjsmkb_id + "  �����ɹ���"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("���˵糧�����ɹ���");
			} else {
				con.rollBack();
				System.out.println("���㵥ID=" + diancjsmkb_id + "  ����ʧ�ܣ�"
						+ DateUtil.FormatDateTime(new Date()));
				con.Close();
				throw new Exception("���˵糧����ʧ�ܣ�");
			}
	
		}
		/**
		 * �γ�ѡ�����̵������˵���list
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
		 * �ж�һ����ɫ�ǲ������һ������������
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
		 * �ж�һ����ɫ�ǲ��ǵ�һ������������
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
	//�������̶������жϴ˴β�����ӵ�е�Ȩ��
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
//  �����������ĳ��������һ����liucztb_id
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
