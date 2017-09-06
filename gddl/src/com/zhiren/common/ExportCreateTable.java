package com.zhiren.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ExportCreateTable {

	private static File FilePath = new File("D:\\");
	private static String FileName = "CreateTalbeSQL.sql";
	private static File FileObject = new File(FilePath,FileName);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("导出开始");
		JDBCcon con = new JDBCcon();
		String sql = "select ut.table_name,uc.comments \n"
			+"       from user_tab_comments uc,user_tables ut \n"
			+"where uc.table_type = 'TABLE' and uc.table_name = ut.table_name \n"
			+"order by ut.table_name";
		try {
			if(!FilePath.exists()){
				FilePath.mkdirs();
			}
			if(!FileObject.exists()){
				FileObject.createNewFile();
			}
			FileWriter fwn = new FileWriter(FileObject,true);
			PrintWriter pwn = new PrintWriter(fwn);
			
			
			ResultSetList rs = con.getResultSetList(sql);
			while(rs.next()){
				System.out.println("--" + rs.getString("TABLE_NAME"));
				String createTable = getCreateTableScript(con,rs.getString("TABLE_NAME"),rs.getString("COMMENTS"));
				pwn.println(createTable);
			}
			rs.close();
			pwn.close();
			fwn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		con.Close();
		System.out.println("导出结束");
		System.exit(0);
	}

	public static String getCreateTableScript(JDBCcon con,String TableName,String Comments){
		String script = "Create Table " + TableName + "\n(";
		String sql = "select t.*, c.COMMENTS \n" +
		"from user_tab_columns  t,user_col_comments c \n" +
		"where t.table_name = c.table_name \n" +
		"and t.column_name = c.column_name \n"+
		"and t.table_name = '"+TableName.toUpperCase()+"'";
		ResultSetList rs = con.getResultSetList(sql);
		String colType ;
		String comment = "";
		while(rs.next()){
			colType = rs.getString("DATA_TYPE");
			script += rs.getString("COLUMN_NAME") + " " + colType;
			if("VARCHAR2".equals(colType)){
				script += "(" + rs.getString("DATA_LENGTH") + ")";
			}else if("NUMBER".equals(colType)){
				if(rs.getString("DATA_PRECISION")!=null && !"".equals(rs.getString("DATA_PRECISION")))
					script += "(" + rs.getString("DATA_PRECISION") + "," + rs.getInt("DATA_SCALE")+")";
			}else if("DATE".equals(colType)){
				
			}else if("VARCHAR".equals(colType)){
				script += "(" + rs.getString("DATA_LENGTH") + ")";
			}
			if(rs.getString("DATA_DEFAULT") != null  && !"".equals(rs.getString("DATA_DEFAULT") )){
				script += " default " + rs.getString("DATA_DEFAULT") ;
			}
			script += " " + ("N".equals(rs.getString("NULLABLE"))?" NOT NULL":" NULL") + ",\n";
			comment += "comment on column " + TableName + "." + rs.getString("COLUMN_NAME") + " is '" + rs.getString("COMMENTS") + "';\n";
		}
		rs.close();
		rs = con.getResultSetList("select * from user_cons_columns where table_name = '" + TableName + "' and position = 1");
		String primarykey = "";
		while(rs.next()){
			primarykey += "alter table " + TableName + " add constraint " + rs.getString("constraint_name")  +" primary key (" + rs.getString("column_name") +");\n";
		} 
		rs.close();
		script = script.subSequence(0, script.length()-2) + ");\n";
		script += "comment on Table " + TableName + " is '" +Comments+"';\n";
		script += comment;
		script += primarykey;
		return script;
	}
}
