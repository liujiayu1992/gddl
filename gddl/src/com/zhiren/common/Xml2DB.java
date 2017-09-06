package com.zhiren.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Xml2DB {
	public static final int transFormType_SQL = 0;
	public static final int transFormType_RS = 1;
	private SAXBuilder builder = null;
	private FileInputStream fis = null;
	private Document doc = null;
	private Element root = null;
	private JDBCcon con = null;
	
	public Xml2DB(File xmlFile){
		init(xmlFile);
	}
	
	private void init(File xmlFile){
		builder = new SAXBuilder();
		con = new JDBCcon();
		try {
			fis = new FileInputStream(xmlFile);
			doc = builder.build(fis);
			root = doc.getRootElement();
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	private Element getRoot(){
		return root;
	}
	
	public void Transform(int type){
		switch(type){
		case transFormType_SQL: Transform();
		case transFormType_RS:
		}
	}
	public void TransformRS(){
		List eTables = getRoot().getChildren();
		for(int i = 0; i<eTables.size(); i++){
			Element eTable = (Element)eTables.get(i);
			String id = eTable.getChildText("ID");
			if(id == null){
				//con.getDelete(getDelSql(eTable));
				//Insert(eTable);
			}else{
				ResultSetList rs = con.getResultSetList(
						"select id from "+eTable.getName() 
						+ " where id=" + id);
				if(rs.getRows()>0){
					Update(eTable);
				}else{
					Insert(eTable);
				}
				rs.close();
			}
		}
	}
	public void Transform(){
		List eTables = getRoot().getChildren();
		String SQL = "begin \n";
		for(int i = 0; i<eTables.size(); i++){
			Element eTable = (Element)eTables.get(i);
			String id = eTable.getChildText("ID");
			if(id == null){
				if(isClobTable(eTable)){
					con.getDelete(getDelSql(eTable));
					Insert(eTable);
				}else{
					SQL += getDelSql(eTable);
					SQL += getInsSql(eTable);
				}
			}else{
				ResultSetList rs = con.getResultSetList(
						"select id from "+eTable.getName() 
						+ " where id=" + id);
				if(rs.getRows()>0){
					if(isClobTable(eTable))
						Update(eTable);
					else
						SQL += getUpdSql(eTable);
					
				}else{
					if(isClobTable(eTable))
						Insert(eTable);
					else
						SQL += getInsSql(eTable);
				}
				rs.close();
			}
		}
		SQL += "\n end;";
		if(SQL.length() > 9)
			con.getUpdate(SQL);
	}
	
	private void Insert(Element eTable){
		String tName = eTable.getName();
		List eColumns = eTable.getChildren();
		List clob = new ArrayList();
		String icol = "";
		String vcol = "";
		if(eColumns.size() > 0)
			for(int i = 0; i < eColumns.size(); i++){
				Element eCol = (Element)eColumns.get(i);
				icol += "," + eCol.getName();
				String type = eCol.getAttributeValue("dataType");
				if("NUMBER".equals(type))
					vcol += "," + ("".equals(eCol.getValue())?"0":eCol.getValue());
				else if("VARCHAR2".equals(type))
						vcol += ",'" + eCol.getValue().replaceAll("'", "''") + "'";
				else if("DATE".equals(type))
					vcol += ",to_date('" + eCol.getValue() + "','yyyy-mm-dd hh24:mi:ss')";
				else if("CLOB".equals(type)){
					vcol += ",empty_clob()";
					clob.add(new ClobBean("C",tName,eCol.getName(),eTable.getChildText("ID"),eCol.getValue()));
				}else if("BLOB".equals(type)){
					vcol += ",empty_blob()";
					clob.add(new ClobBean("B",tName,eCol.getName(),eTable.getChildText("ID"),eCol.getValue()));
				}
			}
		icol = icol.substring(1);
		vcol = vcol.substring(1);
		String sql = "insert into " + tName 
					+ "(" + icol + ") values("
					+ vcol + ")\n";
		con.getInsert(sql);
		DataBassUtil dbu = new DataBassUtil();
		for(int i = 0; i<clob.size();i++){
			ClobBean cb = (ClobBean)clob.get(i);
			try {
				if("C".equals(cb.Type))
					dbu.UpdateClob(cb.tableName, cb.colName, Long.parseLong(cb.Id), cb.Clob);
				else
					dbu.UpdateBlob(cb.tableName, cb.colName, Long.parseLong(cb.Id), cb.Clob);
			} catch (NumberFormatException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
	}
	
	private String getInsSql(Element eTable){
		String tName = eTable.getName();
		List eColumns = eTable.getChildren();
		String icol = "";
		String vcol = "";
		if(eColumns.size() > 0)
			for(int i = 0; i < eColumns.size(); i++){
				Element eCol = (Element)eColumns.get(i);
				icol += "," + eCol.getName();
				String type = eCol.getAttributeValue("dataType");
				if("NUMBER".equals(type))
					vcol += "," + ("".equals(eCol.getValue())?"0":eCol.getValue());
				else if("VARCHAR2".equals(type))
						vcol += ",'" + eCol.getValue().replaceAll("'", "''") + "'";
				else if("DATE".equals(type))
					vcol += ",to_date('" + eCol.getValue() + "','yyyy-mm-dd hh24:mi:ss')";
			}
		else
			return "";
		icol = icol.substring(1);
		vcol = vcol.substring(1);
		String sql = "insert into " + tName 
					+ "(" + icol + ") values("
					+ vcol + ");\n";
		return sql;
	}
	
	private void Update(Element eTable){
		String tName = eTable.getName();
		List eColumns = eTable.getChildren();
		String id = "";
		String ucol = "";
		List clob = new ArrayList();
		if(eColumns.size() > 0)
			for(int i = 0; i < eColumns.size(); i++){
				Element eCol = (Element)eColumns.get(i);
				if("ID".equals(eCol.getName())){
					id = eCol.getValue();
				}
				String type = eCol.getAttributeValue("dataType");
				if("NUMBER".equals(type))
					ucol +=  "," + eCol.getName() + "="+("".equals(eCol.getValue())?"0":eCol.getValue());
				else if("VARCHAR2".equals(type))
						ucol += "," + eCol.getName() + "="+"'" + eCol.getValue().replaceAll("'", "''") + "'";
				else if("DATE".equals(type))
					ucol += "," + eCol.getName() + "="+"to_date('" + eCol.getValue() + "','yyyy-mm-dd hh24:mi:ss')";
				else if("CLOB".equals(type))
					clob.add(new ClobBean("C",tName,eCol.getName(),eTable.getChildText("ID"),eCol.getValue()));
				else if("BLOB".equals(type))
					clob.add(new ClobBean("B",tName,eCol.getName(),eTable.getChildText("ID"),eCol.getValue()));
			}
		ucol = ucol.substring(1);
		String sql = "update " + tName 
					+ " set " + ucol + " where id = "+ id +"\n";
		con.getUpdate(sql);
		DataBassUtil dbu = new DataBassUtil();
		for(int i = 0; i<clob.size();i++){
			ClobBean cb = (ClobBean)clob.get(i);
			try {
				if("C".equals(cb.Type))
					dbu.UpdateClob(cb.tableName, cb.colName, Long.parseLong(cb.Id), cb.Clob);
				else
					dbu.UpdateBlob(cb.tableName, cb.colName, Long.parseLong(cb.Id), cb.Clob);
			} catch (NumberFormatException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
	}
	private String getUpdSql(Element eTable){
		String tName = eTable.getName();
		List eColumns = eTable.getChildren();
		String id = "";
		String ucol = "";
		if(eColumns.size() > 0)
			for(int i = 0; i < eColumns.size(); i++){
				Element eCol = (Element)eColumns.get(i);
				if("ID".equals(eCol.getName())){
					id = eCol.getValue();
				}
				ucol += "," + eCol.getName() + "=";
				String type = eCol.getAttributeValue("dataType");
				if("NUMBER".equals(type))
					ucol +=  ("".equals(eCol.getValue())?"0":eCol.getValue());
				else if("VARCHAR2".equals(type))
						ucol += "'" + eCol.getValue().replaceAll("'", "''") + "'";
				else if("DATE".equals(type))
					ucol += "to_date('" + eCol.getValue() + "','yyyy-mm-dd hh24:mi:ss')";
			}
		else
			return "";
		ucol = ucol.substring(1);
		String sql = "update " + tName 
					+ " set " + ucol + " where id = "+ id +";\n";
		return sql;
	}
	
	private String getDelSql(Element eTable){
		String tName = eTable.getName();
		List eColumns = eTable.getChildren();
		String id = "";
		String ucol = "";
		if(eColumns.size() > 0)
			for(int i = 0; i < eColumns.size(); i++){
				Element eCol = (Element)eColumns.get(i);
				if("ID".equals(eCol.getName())){
					id = eCol.getValue();
				}
				ucol += " and " + eCol.getName() + "=";
				String type = eCol.getAttributeValue("dataType");
				if("NUMBER".equals(type))
					ucol +=  ("".equals(eCol.getValue())?"0":eCol.getValue());
				else if("VARCHAR2".equals(type))
						ucol += "'" + eCol.getValue().replaceAll("'", "''") + "'";
				else if("DATE".equals(type))
					ucol += "to_date('" + eCol.getValue() + "','yyyy-mm-dd hh24:mi:ss')";
			}
		else
			return "";
		ucol = ucol.substring(5);
		String sql = "";
		if("".equals(id))
			sql = "delete from " + tName 
					+ " where " + ucol + " ;\n";
		else 
			sql = "delete from " + tName
					+ " where id = " + id + " ;\n";
		return sql;
	}
	
	public class ClobBean{
		public String Type;
		public String tableName;
		public String colName;
		public String Id;
		public String Clob;
		public ClobBean(String type,String tn, String cn, String id, String cb){
			Type = type;
			tableName = tn;
			colName = cn;
			Id = id;
			Clob = cb;
		}
	}
	
	private boolean isClobTable(Element eTable){
		List eColumns = eTable.getChildren();
		boolean isClobTable = false;
		if(eColumns.size() > 0)
			for(int i = 0; i < eColumns.size(); i++){
				Element eCol = (Element)eColumns.get(i);
				String type = eCol.getAttributeValue("dataType");
				if("CLOB".equals(type)||"BLOB".equals(type)){
					isClobTable = true;
					break;
				}
			}
		return isClobTable;
	}
}
