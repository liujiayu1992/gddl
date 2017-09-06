package com.zhiren.common;

//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.sql.CallableStatement;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.LinkedList;
//
//import sun.jdbc.odbc.JdbcOdbcDriver;
//
//import javax.naming.Context;
//import javax.sql.DataSource;
//
///*
// * 时间：2013年4月26日
// * 修改人：Qiuzw
// * 描述：
// * 		创建SQL Server数据库连接	
// */
//public class JdbcConSqlServer {
//	public static final int ConnectionType_JDBC = 0;
//	public static final int ConnectionType_TomcatJNDI = 1;
//	public static final int ConnectionType_WebLogicJNDI = 2;
//	public static final int ConnectionType_SQLserver = 3;
//	public static final int ConnectionType_ODBC = 4;
//	private Connection dBConnection = null;
//	private CallableStatement Call = null;
//
//	private boolean AutoCommit = true;
//
//	private PreparedStatement ps = null;
//
//	private Statement st = null;
//
//	private ResultSet rs = null;
//
//	private DataSource ds = null;
//
//	private static String DBDriver = "oracle.jdbc.driver.OracleDriver";
//
//	private static String ConnStr = "";// jdbc:oracle:thin:@localhost:1521:rmis
//
//	private static String UserName = "";//"zgdtrmis";//zgdt"
//
//	private static String UserPassword = "";//"rgzx123";//zgdt"
//
//	private static String JNDIName = "";//jdbc/Oracle
//
//	public JdbcConSqlServer() {
//
//	}
//	
//	public JdbcConSqlServer(int ConnectionType, String JNDIName, String ConnStr, 
//			String UserName, String UserPassword){
//		try {
//			if (JNDIName.equals("")) {
//				JNDIName = MainGlobal.getDb_jndiname();
//			}
//			if (ConnStr.equals("")) {
//				ConnStr = MainGlobal.getDb_jdbcDriverURL();
//			}
//			if (UserName.equals("")) {
//				UserName = MainGlobal.getDb_username();
//			}
//			if (UserPassword.equals("")) {
//				UserPassword = MainGlobal.getDb_password();
//			}
//			switch (ConnectionType){
//				case ConnectionType_JDBC:
//					Class.forName(DBDriver);
//					dBConnection = DriverManager.getConnection(ConnStr,
//							UserName, UserPassword);break;
//				case ConnectionType_TomcatJNDI:
//					Context initCtx = new javax.naming.InitialContext();
//					Context envCtx = (Context) initCtx.lookup("java:comp/env");
//					ds = (DataSource) envCtx.lookup(JNDIName);
//					dBConnection = ds.getConnection();break;
//				case ConnectionType_WebLogicJNDI:
//					Context WLinitCtx = new javax.naming.InitialContext();
//					ds = (DataSource) WLinitCtx.lookup(JNDIName);
//					dBConnection = ds.getConnection();break;
//				case ConnectionType_SQLserver:
//					DBDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
//					//ConnStr = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=rmis";
//					//UserName = "sa";
//					//UserPassword = "";
//					Class.forName(DBDriver);
//					dBConnection = DriverManager.getConnection(ConnStr,
//							UserName, UserPassword);break;
//				case ConnectionType_ODBC:
//					DriverManager.registerDriver(new JdbcOdbcDriver());
//					dBConnection = DriverManager.getConnection(ConnStr,UserName,UserPassword);break;
//				default :break;
//			}
//		} catch (Exception exp) {
//			System.out.println("**********************数据库连接失败！*********");
//			exp.printStackTrace();
//		}
//	}
//	
//	public synchronized Connection getConnection() {
//		return dBConnection;
//	}
//	
//	public synchronized CallableStatement getCallableStatement() {
//		return Call;
//	}
//
//	public synchronized CallableStatement getCallableStatement(String callSql) {
//		if (dBConnection == null) {
//			JDBCBegin(ConnectionType_JDBC);
//		}
//		try {
//			Call = getConnection().prepareCall(callSql);
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		return Call;
//	}
//	
//	public synchronized boolean UpdateCall(String callSql) {
//		if (dBConnection == null) {
//			JDBCBegin(ConnectionType_JDBC);
//		}
//		try {
//			Call = getConnection().prepareCall(callSql);
//			return Call.execute();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//			return false;
//		}finally {
//			try {
//				Call.close();
//			} catch (SQLException e) {
//				// TODO 自动生成 catch 块
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public synchronized void setAutoCommit(boolean Auto) {
//		AutoCommit = Auto;
//	}
//
//	public synchronized void commit() {
//		if (dBConnection != null) {
//			try {
//				dBConnection.commit();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	// @param 决定使用数据库连接的方法 
//	public synchronized void JDBCBegin(int i) {
//		try {
//			i = Integer.parseInt(MainGlobal.getDb_type());
//			if (JNDIName.equals("")) {
//				JNDIName = MainGlobal.getDb_jndiname();
//			}
//			if (ConnStr.equals("")) {
//				ConnStr = MainGlobal.getDb_jdbcDriverURL();
//			}
//			if (UserName.equals("")) {
//				UserName = MainGlobal.getDb_username();
//			}
//			if (UserPassword.equals("")) {
//				UserPassword = MainGlobal.getDb_password();
//			}
//			switch (i){
//				case ConnectionType_JDBC:
//					Class.forName(DBDriver);
//					dBConnection = DriverManager.getConnection(ConnStr,
//							UserName, UserPassword);break;
//				case ConnectionType_TomcatJNDI:
//					Context initCtx = new javax.naming.InitialContext();
//					Context envCtx = (Context) initCtx.lookup("java:comp/env");
//					ds = (DataSource) envCtx.lookup(JNDIName);
//					dBConnection = ds.getConnection();break;
//				case ConnectionType_WebLogicJNDI:
//					Context WLinitCtx = new javax.naming.InitialContext();
//					ds = (DataSource) WLinitCtx.lookup(JNDIName);
//					dBConnection = ds.getConnection();break;
//				case ConnectionType_SQLserver:
//					DBDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
//					ConnStr = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=rmis";
//					UserName = "sa";
//					UserPassword = "";
//					Class.forName(DBDriver);
//					dBConnection = DriverManager.getConnection(ConnStr,
//							UserName, UserPassword);break;
//				case ConnectionType_ODBC:
//					DriverManager.registerDriver(new JdbcOdbcDriver());
//					dBConnection = DriverManager.getConnection(ConnStr,UserName,UserPassword);break;
//				default :break;
//			}
//		} catch (Exception exp) {
//			System.out.println("**********************数据库连接失败！*********");
//			exp.printStackTrace();
//		}
//	}
//
//	// ****************************设置数据库相关信息**********************************
//	// 全部加载
//	public synchronized void setJDBCParam(String Driver, String Conn,
//			String Name, String Password) {
//		DBDriver = Driver;
//		ConnStr = Conn;
//		UserName = Name;
//		UserPassword = Password;
//	}
//
//	// 驱动
//	public static void setDBDriver(String Driver) {
//		DBDriver = Driver;
//	}
//
//	// URL
//	public static void setCONNUrl(String Conn) {
//		ConnStr = Conn;
//	}
//
//	// 用户名
//	public static void setUserName(String Name) {
//		UserName = Name;
//	}
//
//	// 密码
//	public static void setUserPassword(String Password) {
//		UserPassword = Password;
//	}
//
//	// JNDI名
//	public static void setJNDIName(String JNDIname) {
//		JNDIName = JNDIname;
//	}
//
//	// *******************************取得记录集列数*********************************//
//	public synchronized static int getColumnCount(ResultSet rsy) {
//		int RsColumn = 0;
//		try {
//			ResultSetMetaData rsmd = rsy.getMetaData();
//			RsColumn = rsmd.getColumnCount();
//		} catch (Exception err) {
//			err.printStackTrace();
//			return -1;
//		}
//		return (RsColumn);
//	}
//
//	// ****************************返回记录集指定列类型*******************************//
//	public synchronized static String getColumnTypeName(ResultSet rsy,
//			int columnCount) {
//		String RsColumnType = null;
//		try {
//			ResultSetMetaData rsmd = rsy.getMetaData();
//			RsColumnType = rsmd.getColumnTypeName(columnCount);
//		} catch (Exception err) {
//			err.printStackTrace();
//			return "未知";
//		}
//		return (RsColumnType);
//	}
//
//	// ****************************返回记录集指定列的名称*****************************//
//	public synchronized static String getColumnName(ResultSet rsy,
//			int columnCount) {
//		String RsColumnName = null;
//		try {
//			ResultSetMetaData rsmd = rsy.getMetaData();
//			RsColumnName = rsmd.getColumnName(columnCount);
//		} catch (Exception err) {
//			err.printStackTrace();
//			return "未知";
//		}
//		return (RsColumnName);
//	}
//	
//	public synchronized static boolean isNullable(ResultSet rsy,
//			int columnCount) {
//		boolean nullable = false;
//		try {
//			ResultSetMetaData rsmd = rsy.getMetaData();
//			nullable = rsmd.isNullable(columnCount) == ResultSetMetaData.columnNullable;
//		} catch (Exception err) {
//			err.printStackTrace();
//			return false;
//		}
//		return nullable;
//	}
//
//	// *****************取得记录集行数(存在返回记录行数，不存在返回0)********************//
//	public synchronized static int getRow(ResultSet rsy) {
//		int RsRows = 0;
//		try {
//			rsy.last();
//			RsRows = rsy.getRow();
//			rsy.beforeFirst();
//		} catch (Exception err) {
//			System.out.println("**********************判断数据存在异常**********");
//			err.printStackTrace();
//			return -1;
//		}
//		return RsRows;
//	}
//
//	// ***********************取得数据(返回LinkedList型数据)**************************//
//	public synchronized ArrayList getArrData(String sql) {
//		ArrayList Arr = new ArrayList();
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			ps = dBConnection
//					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
//							ResultSet.CONCUR_UPDATABLE);
//			rs = ps.executeQuery();
//			if (rs != null) {
//				int columns = getColumnCount(rs);
//				while (rs.next()) {
//					String[] recorder = new String[columns];
//					for (int i = 0; i < columns; i++) {
//						if (getColumnTypeName(rs, i + 1).equals("string"))
//							recorder[i] = rs.getString(i + 1);
//						else if (getColumnTypeName(rs, i + 1).equals("long"))
//							recorder[i] = String.valueOf(rs.getLong(i + 1));
//						else if (getColumnTypeName(rs, i + 1).equals("double"))
//							recorder[i] = String.valueOf(rs.getDouble(i + 1));
//						else if (getColumnTypeName(rs, i + 1).equals("date"))
//							recorder[i] = String.valueOf(rs.getDate(i + 1));
//						else if (getColumnTypeName(rs, i + 1).equals(
//								"bigdecimal"))
//							recorder[i] = String.valueOf(rs
//									.getBigDecimal(i + 1));
//						else
//							recorder[i] = rs.getString(i + 1);
//					}
//					Arr.add(recorder);
//				}
//			} else {
//				dBConnection.rollback();
//			}
//			rs.close();
//			ps.close();
//		} catch (Exception err) {
//			err.printStackTrace();
//			System.out.println("********************获得数据时异常！*********");
//			return null;
//		} 
//		return Arr;
//	}
//	
//	/*
//	 * 2010-06-19 ww
//	 * 添加以下代码
//	 * dBConnection.setAutoCommit(AutoCommit);
//	 */
//	public synchronized ResultSetList getResultSetList(String sql) {
//		ResultSetList rsl = new ResultSetList();
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			
//			dBConnection.setAutoCommit(AutoCommit);
//			ps = dBConnection
//					.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//			rs = ps.executeQuery();
//			if (rs != null) {
//				ResultSetMetaData rsmd = rs.getMetaData();
//				int columns = rsmd.getColumnCount();
//				//rs.getMetaData().
//				String[]  columnNames = new String[columns];
//				String[]  columnTypes = new String[columns];
//				boolean[] nullables = new boolean[columns];
//				long[]	colPrecisions = new long[columns];
//				long[]	colScales = new long[columns];
//				for(int i=0;i < columns;i++) {
//					columnNames[i] = rsmd.getColumnName(i+1);
//					columnTypes[i] = rsmd.getColumnTypeName(i+1);
//					nullables[i] = rsmd.isNullable(i+1)== ResultSetMetaData.columnNullable;
//					if("CLOB".equals(columnTypes[i])||"BLOB".equals(columnTypes[i])){
//						colPrecisions[i] = 4294967295L;
//					}else{
//						colPrecisions[i] = rsmd.getPrecision(i+1);
//					}
//					colScales[i] = rsmd.getScale(i+1);
//				}
//				rsl.setColumnNames(columnNames);
//				rsl.setColumnTypes(columnTypes);
//				rsl.setNullables(nullables);
//				rsl.setColPrecisions(colPrecisions);
//				rsl.setColScales(colScales);
//				while (rs.next()) {
//					String[] recorder = new String[columns];
//					for (int i = 0; i < columns; i++) {
//						if("DATE".equals(columnTypes[i])) {
//							if (rs.getTimestamp(i+1)==null){
//								recorder[i]="";
//							}else{
//								recorder[i] = String.valueOf(rs.getTimestamp(i+1).getTime());
//							}
//						}else if("BLOB".equals(columnTypes[i])){
//							ByteArrayOutputStream bos = null;
//							oracle.sql.BLOB blob = (oracle.sql.BLOB)rs.getBlob(i + 1); 
//							if(blob == null) {
//								recorder[i] = "";
//							}else{
//								bos = new ByteArrayOutputStream();
//								InputStream ins = blob.getBinaryStream();
//								int c; 
//								while ((c=ins.read())!=-1) { 
//									bos.write(c); 
//								}
//								ins.close();
//								recorder[i] = En_Decrypt_ZR.Byt2Str(bos.toByteArray());
//								bos.close();
//							}
//						}else if("CLOB".equals(columnTypes[i])){
//							oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(i + 1);
//							if(clob == null) {
//								recorder[i] = "";
//							}else{
//								StringBuffer strClob = new StringBuffer();
//								BufferedReader br = new BufferedReader(clob
//										.getCharacterStream());
//								String out = br.readLine();
//								while (out != null) {
//									strClob.append(out);
//									strClob.append("\n");
//									out = br.readLine();
//								}
//								br.close();
//								recorder[i] = strClob.toString();
//							}
//						}else{
//							recorder[i] = rs.getString(i + 1)==null?"":rs.getString(i + 1);
//						}
//					}
//					rsl.getResultSetlist().add(recorder);
//				}
//			} else {
//				dBConnection.rollback();
//			}
//			closeRs();
//		} catch (Exception err) {
//			System.out.println(sql);
//			err.printStackTrace();
//			return null;
//		} 
//		return rsl;
//	}
//
//	public synchronized LinkedList getData(String sql) {
//		LinkedList ll = new LinkedList();
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			ps = dBConnection
//					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
//							ResultSet.CONCUR_UPDATABLE);
//			rs = ps.executeQuery();
//			if (rs != null) {
//				int columns = getColumnCount(rs);
//				while (rs.next()) {
//					String[] recorder = new String[columns];
//					for (int i = 0; i < columns; i++) {
//						if (getColumnTypeName(rs, i + 1).equals("long"))
//							recorder[i] = String.valueOf(rs.getLong(i + 1));
//						else
//							recorder[i] = rs.getString(i + 1);
//					}
//					ll.add(recorder);
//				}
//			} else {
//				dBConnection.rollback();
//			}
//			closeRs();
//		} catch (Exception err) {
//			err.printStackTrace();
//			System.out.println("********************获得数据时异常！*********");
//			return null;
//		} 
//		return ll;
//	}
//
//	// 判断该记录是否存在 
//	public synchronized boolean getHasIt(String sql) {
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			ps = dBConnection.prepareStatement(sql);
//			rs = ps.executeQuery();
//			if (rs != null) {
//				// System.out.println("***********************已获得结果集********");
//				if (rs.next()){
//					closeRs();
//					return true;
//				}else{
//					closeRs();
//					return false;
//				}
//			} else {
//				dBConnection.rollback();
//			}
//		} catch (Exception exp) {
//			System.out.println("**********************判断数据存在异常**********");
//			System.out.println(exp.getMessage());
//			return false;
//		} 
//		return false;
//	}
//
//	// ***************************插入数据(1:成功-1:失败)*****************************//
//	public synchronized int getInsert(String sql) {
//		int flag = -1;
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			dBConnection.setAutoCommit(AutoCommit);
//			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			flag = st.executeUpdate(sql);
//			if (flag == -1) {
//				dBConnection.rollback();
//			}
//		} catch (Exception err) {
//			System.out.println("**********************数据插入异常！***************");
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			err.printStackTrace();
//			return -1;
//		} finally {
//			closeRs();
//		}
//		return (flag);
//	}
//
//	// ***************************删除数据(1:成功-1:失败)*****************************//
//	public synchronized int getDelete(String sql) {
//		int flag = -1;
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			dBConnection.setAutoCommit(AutoCommit);
//			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			flag = st.executeUpdate(sql);
//			if (flag == -1) {
//				dBConnection.rollback();
//			}
//		} catch (Exception err) {
//			System.out.println("*********************删除数据异常！*****************");
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			err.printStackTrace();
//			return -1;
//		} finally{
//			closeRs();
//		}
//		return (flag);
//	}
//
//	// **************************更新数据(1:成功0:失败)******************************//
//	public synchronized int getUpdate(String sql) {
//		int flag = -1;
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			dBConnection.setAutoCommit(AutoCommit);
//			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			flag = st.executeUpdate(sql);
//			if (flag == -1) {
//				dBConnection.rollback();
//			}
//		} catch (Exception err) {
//			System.out.println("********************数据更新失败！******************");
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			err.printStackTrace();
//			return -1;
//		} finally{
//			closeRs();
//		}
//		return (flag);
//	}
//
//	// *********************************取得rs记录集*********************************//
//	public synchronized ResultSet getResultSet(String sql) {
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			dBConnection.setAutoCommit(AutoCommit);
//			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			rs = st.executeQuery(sql);
//			if (rs == null) {
//				dBConnection.rollback();
//			}
//		} catch (Exception exp) {
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			exp.printStackTrace();
//			closeRs();
//			return null;
//		} 
//		return rs;
//	}
//
//	// ***********************************取得ps记录集*****************************************//
//	public synchronized PreparedStatement getPresultSet(String sql) {
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			ps = dBConnection.prepareStatement(sql);
//			if (ps == null) {
//				dBConnection.rollback();
//			}
//		} catch (Exception exp) {
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			exp.printStackTrace();
//			closeRs();
//			return null;
//		}
//		return ps;
//	}
//
//	// **********************************关闭连接***********************************//
//	public synchronized void Close() {
//		try {
//			if (rs != null) {
//				rs.close();
//				rs = null;
//			}
//			if (ps != null) {
//				ps.close();
//				ps = null;
//			}
//			if (st != null) {
//				st.close();
//				st = null;
//			}
//			if(Call != null) {
//				Call.close();
//				Call = null;
//			}
//			if (dBConnection != null) {
//				dBConnection.close();
//				dBConnection = null;
//			}
//		} catch (Exception exp) {
//			exp.printStackTrace();
//		}
//	}
//
//	public synchronized void closeRs() {
//		try {
//			if (rs != null) {
//				rs.close();
//				rs = null;
//			}
//			if (ps != null) {
//				ps.close();
//				ps = null;
//			}
//			if (st != null) {
//				st.close();
//				st = null;
//			}
//			// System.out.println("********************数据连接关闭！******************");
//		} catch (Exception exp) {
//			exp.printStackTrace();
//		}
//	}
//
//	// ****************************************************************************//
//	/*
//	 * public String getColumnLabel(ResultSet rsy,int columnCount){ String
//	 * TableName = null; try{ ResultSetMetaData rsmd = rsy.getMetaData();
//	 * TableName = rsmd.getColumnLabel(columnCount); }catch(Exception
//	 * err){err.printStackTrace();} return(TableName); }
//	 */
//	public synchronized void rollBack() {
//		try {
//			if (dBConnection != null) {
//				dBConnection.rollback();
//			}
//		} catch (SQLException s) {
//			s.printStackTrace();
//		}
//	}
//
//	// *********************************取得rs记录集*********************************//
//	public synchronized ResultSet getResultSet(StringBuffer sql) {
//		Statement stat = null;
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			stat = dBConnection.createStatement();
//			rs = stat.executeQuery(sql.toString());
//			if (rs == null) {
//			}
//		} catch (Exception exp) {
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			exp.printStackTrace();
//			return null;
//		} finally {
//			stat = null;
//		}
//		return rs;
//	}
//
//	public synchronized ResultSet getResultSet(StringBuffer sql,
//			int resultSetType, int resultSetConcurrency) {
//		Statement stat = null;
//		try {
//			if (dBConnection == null) {
//				JDBCBegin(ConnectionType_JDBC);
//			}
//			stat = dBConnection.createStatement(resultSetType,
//					resultSetConcurrency);
//			rs = stat.executeQuery(sql.toString());
//
//			if (rs == null) {
//			}
//		} catch (Exception exp) {
//			System.out.println("SQL:\n");
//			System.out.println(sql);
//			exp.printStackTrace();
//			return null;
//		} finally {
//			stat = null;
//		}
//		return rs;
//	}
//
//	public synchronized boolean getAutoCommit() {
//		return AutoCommit;
//	}
//
//}
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

public class JdbcConSqlServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4467612734557151541L;

	private Connection dBConnection = null;

	private boolean AutoCommit = true;

	private PreparedStatement ps = null;

	private Statement st = null;

	private ResultSet rs = null;

	private DataSource ds = null;

	private int hasIt;
	
	private static String DBDriver = "";//

	private static String ConnStr = "";

	private static String UserName = "sa"; //

	private static String UserPassword = ""; //

	private static String JNDIName = "";

	public JdbcConSqlServer() {
	}

	public synchronized void setAutoCommit(boolean Auto) {
		AutoCommit = Auto;
	}

	public synchronized void commit() {
		if (dBConnection != null) {
			try {
				dBConnection.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// @param 决定使用数据库连接的方法 1：JDBC直连 2：TOMCAT 数据库连接池 3： webLogic 数据库连接池
	public synchronized void JDBCBegin(int i) {
		try {
			if (DBDriver.equals("")) {
				DBDriver = MainGlobal.getDb_DriverName_SQLSERVER();
			}
			if (ConnStr.equals("")) {
				ConnStr = MainGlobal.getDb_jdbcDriverURL_SQLSERVER();
			}
			if (UserName.equals("")) {
				UserName = MainGlobal.getDb_username_SQLSERVER();
			}
			if (UserPassword.equals("")) {
				UserPassword = MainGlobal.getDb_password_SQLSERVER();
			}
			if (dBConnection == null) {
				Class.forName(DBDriver);
				dBConnection = DriverManager.getConnection(ConnStr, UserName,
						UserPassword);
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getLocalizedMessage());
			System.out.println(exp.getStackTrace());
			System.out
					.println("方法JDBCBegin(i)**********************数据库连接失败！*********");
		}
	}

	public synchronized void JDBCBegin() {
		try {
			if (DBDriver.equals("")) {
				DBDriver = MainGlobal.getDb_DriverName_SQLSERVER();
			}
			if (ConnStr.equals("")) {
				ConnStr = MainGlobal.getDb_jdbcDriverURL_SQLSERVER();
			}
			if (UserName.equals("")) {
				UserName = MainGlobal.getDb_username_SQLSERVER();
			}
			if (UserPassword.equals("")) {
				UserPassword = MainGlobal.getDb_password_SQLSERVER();
			}
			if (dBConnection == null) {
				Class.forName(DBDriver);
				dBConnection = DriverManager.getConnection(ConnStr, UserName,
						UserPassword);
			}

		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getLocalizedMessage());
			System.out.println(exp.getStackTrace());
			System.out
					.println("JDBCBegin()**********************数据库连接失败！*********");
		}
	}

	// ****************************设置数据库相关信息**********************************
	// 全部加载
	public synchronized void setJDBCParam(String Driver, String Conn,
			String Name, String Password) {
		DBDriver = Driver;
		ConnStr = Conn;
		UserName = Name;
		UserPassword = Password;
	}

	// 驱动
	public static void setDBDriver(String Driver) {
		DBDriver = Driver;
	}

	// URL
	public static void setCONNUrl(String Conn) {
		ConnStr = Conn;
	}

	// 用户名
	public static void setUserName(String Name) {
		UserName = Name;
	}

	// 密码
	public static void setUserPassword(String Password) {
		UserPassword = Password;
	}

	// JNDI名
	public static void setJNDIName(String JNDIname) {
		JNDIName = JNDIname;
	}

	// *******************************取得记录集列数*********************************//
	public synchronized static int getColumnCount(ResultSet rsy) {
		int RsColumn = 0;
		try {
			ResultSetMetaData rsmd = rsy.getMetaData();
			RsColumn = rsmd.getColumnCount();
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return (RsColumn);
	}

	// ****************************返回记录集指定列类型*******************************//
	public synchronized static String getColumnTypeName(ResultSet rsy,
			int columnCount) {
		String RsColumnType = null;
		try {
			ResultSetMetaData rsmd = rsy.getMetaData();
			RsColumnType = rsmd.getColumnTypeName(columnCount);
		} catch (Exception err) {
			err.printStackTrace();
			return "未知";
		}
		return (RsColumnType);
	}

	// ****************************返回记录集指定列的名称*****************************//
	public synchronized static String getColumnName(ResultSet rsy,
			int columnCount) {
		String RsColumnName = null;
		try {
			ResultSetMetaData rsmd = rsy.getMetaData();
			RsColumnName = rsmd.getColumnName(columnCount);
		} catch (Exception err) {
			err.printStackTrace();
			return "未知";
		}
		return (RsColumnName);
	}

	// *****************取得记录集行数(存在返回记录行数，不存在返回0)********************//
	public synchronized static int getRow(ResultSet rsy) {
		int RsRows = 0;
		try {
			rsy.last();
			RsRows = rsy.getRow();
			rsy.beforeFirst();
		} catch (Exception err) {
			System.out.println("**********************判断数据存在异常**********");
			err.printStackTrace();
			return -1;
		}
		return RsRows;
	}

	// ***********************取得数据(返回LinkedList型数据)**************************//
//	public ArrayList getArrData(String sql) {
//		ArrayList Arr = new ArrayList();
//		try {
//			JDBCBegin(LIAN_JIE_FANG_SHI);
//
//			ps = dBConnection
//					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
//							ResultSet.CONCUR_UPDATABLE);
//			rs = ps.executeQuery();
//			if (rs != null) {
//				int columns = getColumnCount(rs);
//				while (rs.next()) {
//					String[] recorder = new String[columns];
//					for (int i = 0; i < columns; i++) {
//						if (getColumnTypeName(rs, i + 1).equals("string"))
//							recorder[i] = rs.getString(i + 1);
//						else if (getColumnTypeName(rs, i + 1).equals("long"))
//							recorder[i] = String.valueOf(rs.getLong(i + 1));
//						else if (getColumnTypeName(rs, i + 1).equals("double"))
//							recorder[i] = String.valueOf(rs.getDouble(i + 1));
//						else if (getColumnTypeName(rs, i + 1).equals("date"))
//							recorder[i] = String.valueOf(rs.getDate(i + 1));
//						else if (getColumnTypeName(rs, i + 1).equals(
//								"bigdecimal"))
//							recorder[i] = String.valueOf(rs
//									.getBigDecimal(i + 1));
//						else
//							recorder[i] = rs.getString(i + 1);
//					}
//					Arr.add(recorder);
//				}
//			} else {
//				dBConnection.rollback();
//			}
//			rs.close();
//			ps.close();
//		} catch (Exception err) {
//			err.printStackTrace();
//			System.out.println("********************获得数据时异常！*********");
//			closeRs();
//			return null;
//		} finally {
//		}
//		return Arr;
//	}
//
//	public synchronized LinkedList getData(String sql) {
//		LinkedList ll = new LinkedList();
//		try {
//			JDBCBegin(LIAN_JIE_FANG_SHI);
//			ps = dBConnection
//					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
//							ResultSet.CONCUR_UPDATABLE);
//			rs = ps.executeQuery();
//			if (rs != null) {
//				int columns = getColumnCount(rs);
//				while (rs.next()) {
//					String[] recorder = new String[columns];
//					for (int i = 0; i < columns; i++) {
//						if (getColumnTypeName(rs, i + 1).equals("long"))
//							recorder[i] = String.valueOf(rs.getLong(i + 1));
//						else
//							recorder[i] = rs.getString(i + 1);
//					}
//					ll.add(recorder);
//				}
//			} else {
//				dBConnection.rollback();
//			}
//			rs.close();
//			ps.close();
//		} catch (Exception err) {
//			err.printStackTrace();
//			System.out.println("********************获得数据时异常！*********");
//			closeRs();
//			return null;
//		} finally {
//		}
//		return ll;
//	}

	private void reGetConnection() {
		Date now = new Date();
		while (dBConnection == null || isClosed()) {
			JDBCBegin();
			Date cur = new Date();
			if (dBConnection != null
					|| ((cur.getTime() - now.getTime()) / 1000 > 5)) {// 90秒强制连接，不成功，表示失败
				break;
			}
		}
	}

	// /////////////////////////////判断该记录是否存在 1:已存在
	// 0:未发现//////////////////////////////////////////
	public synchronized int getHasIt(String sql) {

		try {
			reGetConnection();// 检查连接是否失效
			ps = dBConnection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs != null) {
				if (rs.next())
					hasIt = 1;
				else
					hasIt = 0;
			} else {
				dBConnection.rollback();
			}
			rs.close();
			ps.close();

		} catch (Exception exp) {
			System.out.println("**********************判断数据存在异常**********");
			System.out.println(exp.getMessage());
			closeRs();
			return -1;
		} finally {
		}
		return hasIt;
	}

	// ***************************插入数据(1:成功0:失败)*****************************//
	public synchronized int getInsert(String sql) {
		int flag = -1;
		try {
			reGetConnection();// 检查连接是否失效

			dBConnection.setAutoCommit(AutoCommit);
			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			flag = st.executeUpdate(sql);
			if (flag == -1) {
				dBConnection.rollback();
			}
			st.close();
		} catch (Exception err) {
			System.out.println("**********************数据插入异常！***************");
			System.out.println("SQL:\n");
			System.out.println(sql);
			err.printStackTrace();
			closeRs();
			return -1;
		} finally {
		}
		return (flag);
	}

	// ***************************删除数据(1:成功0:失败)*****************************//
	public synchronized int getDelete(String sql) {
		int flag = -1;
		try {
			reGetConnection();// 检查连接是否失效
			dBConnection.setAutoCommit(AutoCommit);
			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			flag = st.executeUpdate(sql);
			if (flag == -1) {
				dBConnection.rollback();
			}
			st.close();
		} catch (Exception err) {
			System.out.println("*********************删除数据异常！*****************");
			System.out.println("SQL:\n");
			System.out.println(sql);
			err.printStackTrace();
			closeRs();
			return -1;
		} finally {
		}
		return (flag);
	}

	// **************************更新数据(1:成功0:失败)******************************//
	public synchronized int getUpdate(String sql) {
		int flag = -1;
		try {
			reGetConnection();// 检查连接是否失效
			dBConnection.setAutoCommit(AutoCommit);
			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			flag = st.executeUpdate(sql);
			if (flag == -1) {
				dBConnection.rollback();
			}
			st.close();
		} catch (Exception err) {
			System.out.println("********************数据更新失败！******************");
			System.out.println("SQL:\n");
			System.out.println(sql);
			err.printStackTrace();
			closeRs();
			return -1;
		} finally {
		}
		return (flag);
	}

	// *********************************取得rs记录集*********************************//
	public synchronized ResultSet getResultSet(String sql) {

		try {
			reGetConnection();// 检查连接是否失效

			dBConnection.setAutoCommit(AutoCommit);
			st = dBConnection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = st.executeQuery(sql);
			if (rs == null) {
				dBConnection.rollback();
			}

		} catch (SQLException exp) {
			System.out.println("SQL:\n");
			System.out.println(sql);
			exp.printStackTrace();
			closeRs();
			return null;
		} finally {

		}
		return rs;
	}

	// ***********************************取得ps记录集*****************************************//
	public synchronized PreparedStatement getPresultSet(String sql) {
		try {
			reGetConnection();// 检查连接是否失效
			dBConnection.setAutoCommit(AutoCommit);
			ps = dBConnection.prepareStatement(sql);
			if (ps == null) {
				dBConnection.rollback();
			}
		} catch (SQLException exp) {
			System.out.println("SQL:\n");
			System.out.println(sql);
			exp.printStackTrace();
			closeRs();
			return null;
		}
		return ps;
	}

	// **********************************关闭连接***********************************//
	public synchronized void Close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (st != null) {
				st.close();
				st = null;
			}
			if (dBConnection != null) {
				dBConnection.close();
				dBConnection = null;
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public synchronized void closeRs() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (st != null) {
				st.close();
				st = null;
			}
		} catch (SQLException exp) {
			exp.printStackTrace();
		}
	}

	public synchronized void rollBack() {
		try {
			if (dBConnection != null) {
				dBConnection.rollback();
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	// *********************************取得rs记录集*********************************//
	public synchronized ResultSet getResultSet(StringBuffer sql) {
		Statement stat = null;
		try {
			reGetConnection();

			stat = dBConnection.createStatement();
			rs = stat.executeQuery(sql.toString());
			if (rs == null) {
			}
		} catch (SQLException exp) {
			System.out.println("SQL:\n");
			System.out.println(sql);
			exp.printStackTrace();
			return null;
		} finally {
			stat = null;
		}
		return rs;
	}

	public synchronized ResultSet getResultSet(StringBuffer sql,
			int resultSetType, int resultSetConcurrency) {
		Statement stat = null;
		try {
			reGetConnection();

			stat = dBConnection.createStatement(resultSetType,
					resultSetConcurrency);
			rs = stat.executeQuery(sql.toString());

			if (rs == null) {
			}
		} catch (SQLException exp) {
			System.out.println("SQL:\n");
			System.out.println(sql);
			exp.printStackTrace();
			return null;
		} finally {
			stat = null;
		}
		return rs;
	}

	public synchronized boolean getAutoCommit() {
		return AutoCommit;
	}

	public synchronized boolean isClosed() {

		try {
			if (dBConnection != null) {
				return dBConnection.isClosed();
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
