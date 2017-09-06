package com.zhiren.webservice.dtgsinterface;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.zhiren.common.En_Decrypt_ZR;
import com.zhiren.common.ResultSetList;

public class DBconn {
	private String DBDriver = "oracle.jdbc.driver.OracleDriver";
	private boolean AutoCommit = true;
	private String ConnStr = "";
	private Connection cnn = null;
	private ResultSet rs = null;
	private Statement stmt=null;
	private PreparedStatement ps = null;
	private String UserName="";
	private String Password="";

	public void setUserName(String strUserName){
		UserName=strUserName;
	}

	public void setPassword(String strPassword){
		Password=strPassword;
	}

	public DBconn(String strHostName,String strPort,String strSID){
		ConnStr="jdbc:oracle:thin:@"+strHostName +":" +strPort +":" +strSID;
		if(strHostName.equals("10.119.56.1")){
            ConnStr = "jdbc:oracle:oci8:@ps";
		}
	}

	public void setAutoCommit(boolean Auto) {
		AutoCommit = Auto;
	}

	public void commit() {   
		if (cnn != null) {
			try {
				cnn.commit();  
			} catch (Exception e) {   
				e.printStackTrace();
			}
		} 
	}
	
	private void openCnn(){
		try {
			if (cnn != null) {
				cnn.close();
			}
			Class.forName(DBDriver);
			cnn = DriverManager.getConnection(ConnStr,UserName, Password);
		} catch (Exception e) {
			System.out.println(ConnStr);
			e.printStackTrace();
		}
	}

	public  ResultSet getResultSet(String sql) {
		openCnn();
		try {
			stmt = cnn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException ex) {
			System.err.println("aq.executeQuery:" + ex.getMessage());
		}
		return rs;
	}

	public synchronized ResultSetList getResultSetList(String sql) {
		ResultSetList rsl = new ResultSetList();
		try {
			openCnn();
			ps = cnn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columns = rsmd.getColumnCount();
				//rs.getMetaData().
				String[]  columnNames = new String[columns];
				String[]  columnTypes = new String[columns];
				boolean[] nullables = new boolean[columns];
				long[]	colPrecisions = new long[columns];
				long[]	colScales = new long[columns];
				for(int i=0;i < columns;i++) {
					columnNames[i] = rsmd.getColumnName(i+1);
					columnTypes[i] = rsmd.getColumnTypeName(i+1);
					nullables[i] = rsmd.isNullable(i+1)== ResultSetMetaData.columnNullable;
					if("CLOB".equals(columnTypes[i])||"BLOB".equals(columnTypes[i])){
						colPrecisions[i] = 4294967295L;
					}else{
						colPrecisions[i] = rsmd.getPrecision(i+1);
					}
					colScales[i] = rsmd.getScale(i+1);
				}
				rsl.setColumnNames(columnNames);
				rsl.setColumnTypes(columnTypes);
				rsl.setNullables(nullables);
				rsl.setColPrecisions(colPrecisions);
				rsl.setColScales(colScales);
				while (rs.next()) {
					String[] recorder = new String[columns];
					for (int i = 0; i < columns; i++) {
						if("DATE".equals(columnTypes[i])) {
							if (rs.getTimestamp(i+1)==null){
								recorder[i]="";
							}else{
								recorder[i] = String.valueOf(rs.getTimestamp(i+1).getTime());
							}
						}else if("BLOB".equals(columnTypes[i])){
							ByteArrayOutputStream bos = null;
							oracle.sql.BLOB blob = (oracle.sql.BLOB)rs.getBlob(i + 1); 
							if(blob == null) {
								recorder[i] = "";
							}else{
								bos = new ByteArrayOutputStream();
								InputStream ins = blob.getBinaryStream();
								int c; 
								while ((c=ins.read())!=-1) { 
									bos.write(c); 
								}
								ins.close();
								recorder[i] = En_Decrypt_ZR.Byt2Str(bos.toByteArray());
								bos.close();
							}
						}else if("CLOB".equals(columnTypes[i])){
							oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(i + 1);
							if(clob == null) {
								recorder[i] = "";
							}else{
								StringBuffer strClob = new StringBuffer();
								BufferedReader br = new BufferedReader(clob
										.getCharacterStream());
								String out = br.readLine();
								while (out != null) {
									strClob.append(out);
									strClob.append("\n");
									out = br.readLine();
								}
								br.close();
								recorder[i] = strClob.toString();
							}
						}else{
							recorder[i] = rs.getString(i + 1)==null?"":rs.getString(i + 1);
						}
					}
					rsl.getResultSetlist().add(recorder);
				}
			} else {
				cnn.rollback();
			}
			rs.close();
		} catch (Exception err) {
			System.out.println(sql);
			err.printStackTrace();
			return null;
		} 
		return rsl;
	}
	
	public synchronized int getUpdate(String sql) {
		int flag = -1;
		try {
			openCnn();
			cnn.setAutoCommit(AutoCommit);

			stmt = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			flag = stmt.executeUpdate(sql);

			if (flag == -1) {
				cnn.rollback();
			}
			stmt.close();
		} catch (Exception err) {
			System.out.println("********************数据更新失败！******************");
			err.printStackTrace();
		} finally {

		}
		return (flag);
	}

	public void rollBack() {
        try {
            if (cnn != null) {
            	cnn.rollback();
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
	
	public  void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (cnn != null) {
				cnn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}