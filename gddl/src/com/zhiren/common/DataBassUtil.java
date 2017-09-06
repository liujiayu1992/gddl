package com.zhiren.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBassUtil {
	
	public DataBassUtil() {
		
	}
	public void UpdateBlob(
			String TableName, String colName, long id, String strblob,boolean isEncrypt) throws SQLException, IOException {
		byte[] blob = null;
		try {
			if(isEncrypt) {
				blob = En_Decrypt_ZR.encryptByDES(En_Decrypt_ZR.Str2Byt(strblob), En_Decrypt_ZR.keybyte, En_Decrypt_ZR.ivbyte);
				UpdateBlob(TableName, colName, id, blob);
			}else {
				UpdateBlob(TableName,colName,id,strblob);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void UpdateBlob(
		String TableName, String colName, long id, String str) throws SQLException, IOException {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "update "+TableName+" set "+colName
			+" = empty_blob() where id ="+id;
		con.getUpdate(sql);
		sql = "select "+colName+" from "+TableName
		+" where id="+id+" for update";
		ResultSet rs  = con.getResultSet(sql);
		if(rs.next()) {
			oracle.sql.BLOB ob = (oracle.sql.BLOB)rs.getBlob(colName);
			BufferedOutputStream out = new BufferedOutputStream(ob.getBinaryOutputStream()); 
			StringReader sr = new StringReader(str);
			int c;
			while ((c = sr.read()) != -1) {
				out.write(c);
			}
			out.close(); 
		}
		rs.close();
		con.commit();
		con.Close();
	}
	public void UpdateBlob(
			String TableName, String colName, long id, byte[] blob) throws SQLException, IOException {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "update "+TableName+" set "+colName
			+" = empty_blob() where id ="+id;
		con.getUpdate(sql);
		sql = "select "+colName+" from "+TableName
			+" where id="+id+" for update";
		ResultSet rs  = con.getResultSet(sql);
		if(rs.next()) {
			oracle.sql.BLOB ob = (oracle.sql.BLOB)rs.getBlob(colName);
			BufferedOutputStream out = new BufferedOutputStream(ob.getBinaryOutputStream()); 
			BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(blob)); 
			int c; 
			while ((c=in.read())!=-1) { 
				out.write(c); 
			} 
			in.close(); 
			out.close(); 
		}
		rs.close();
		con.commit();
		con.Close();
	}
	public ByteArrayOutputStream GetBlob
			(String TableName, String colName, long id) throws SQLException, IOException {
		ByteArrayOutputStream bos = null;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "select "+colName+" from "+TableName
		+" where id="+id;
		ResultSet rs  = con.getResultSet(sql);
		if(rs.next()) {
			oracle.sql.BLOB blob = (oracle.sql.BLOB)rs.getBlob(colName); 
			if(blob == null) {
				return null;
			}
			bos = new ByteArrayOutputStream();
			InputStream ins = blob.getBinaryStream();
			int c; 
			while ((c=ins.read())!=-1) { 
				bos.write(c); 
			}
			ins.close();
		}
		rs.close();
		con.Close();
		return bos;
	}
	public String GetStrBlob
		(String TableName, String colName, long id,boolean isEncrypt) throws SQLException, IOException {
		String strblob = "";
		ByteArrayOutputStream out = GetBlob(TableName, colName, id);
		byte[] bytValue = out.toByteArray();
		try {
			if(isEncrypt) {
				byte[] bytv = En_Decrypt_ZR.decryptByDES(bytValue, En_Decrypt_ZR.keybyte, En_Decrypt_ZR.ivbyte);
				strblob = En_Decrypt_ZR.Byt2Str(bytv);
			}else {
				strblob = En_Decrypt_ZR.Byt2Str(bytValue);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return strblob;
	}
	
	public void UpdateClob
		(String TableName, String colName, long id, String strClob) throws SQLException, IOException {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "update "+TableName+" set "+colName
			+" = empty_clob() where id ="+id;
		con.getUpdate(sql);
		sql = "select "+colName+" from "+TableName
		+" where id="+id+" for update";
		ResultSet rs  = con.getResultSet(sql);
		if(rs.next()) {
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs
			.getClob(colName);
			BufferedWriter out = new BufferedWriter(clob
					.getCharacterOutputStream());
			StringReader sr = new StringReader(strClob);
			int c;
			while ((c = sr.read()) != -1) {
				out.write(c);
			}
			out.close();
		}
		rs.close();
		con.commit();
		con.Close();
	}
	
	public String getClob
		(String TableName, String colName, long id) throws SQLException, IOException {
		StringBuffer strClob = new StringBuffer();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "select "+colName+" from "+TableName
		+" where id="+id;
		ResultSet rs  = con.getResultSet(sql);
		if(rs.next()) {
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(colName);
			if(clob == null) {
				return "";
			}
			BufferedReader br = new BufferedReader(clob
					.getCharacterStream());
			String out = br.readLine();
			while (out != null) {
				strClob.append(out);
				strClob.append("\n");
				out = br.readLine();
			}
			br.close();
		}
		rs.close();
		con.commit();
		con.Close();
		return strClob.toString();
	}
}
