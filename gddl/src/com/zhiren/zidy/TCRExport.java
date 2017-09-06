package com.zhiren.zidy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.transform.TransformerException;

import com.zhiren.common.DB2Xml;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

public class TCRExport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		
			try {
				while(true){
					BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
					System.out.println("请输入自定义方案的ID："); 
					String id = br.readLine();
					if(id == null || "".equals(id)){
						break;
					}
					ExportByID(id);
					System.out.println("导出过程结束。");
				}
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				System.exit(0);
				System.out.print("已退出");
			}
	}
	
	public static void ExportByID(String id){
		DB2Xml dx = new DB2Xml();
		dx.setElementForSQL("zidyfa", "select * from zidyfa where id =" + id + " order by id");
		dx.setElementForSQL("zidyfah", "select * from zidyfah where zidyfa_id =" + id + " order by id");
		dx.setElementForSQL("zidyfal", "select * from zidyfal where zidyfa_id ="+id + " order by id");
		dx.setElementForSQL("zidyfanr", "select * from zidyfanr where zidyfa_id ="+id + " order by id");
		dx.setElementForSQL("zidyfasjy", "select * from zidyfasjy where zidyfa_id ="+id + " order by id");
		dx.setElementForSQL("zidycszd", "select * from zidycszd where zidyfa_id ="+id + " order by id");
		dx.setElementForSQL("zidyfacsb", "select * from zidyfacsb where zidyfa_id = "+id + " order by id");
		dx.setElementForSQL("zidyfaysb", "select * from zidyfaysb where zidyfa_id = "+id + " order by id");
		dx.setElementForSQL("zidyfatb", "select * from zidyfatb where zidyfa_id = "+id);
		dx.setElementForSQL("zidysjy", "select * from zidysjy "
				+ "where id in (select zidysjy_id from zidyfasjy where zidyfa_id = " + id
				+ ") or id in (select zidysjy_id from zidyfacsb where zidyfa_id = "+id + ") order by id");
		dx.setElementForSQL("zidysjysql", "select * from zidysjysql "
				+ "where zidysjy_id in (select zidysjy_id from zidyfasjy where zidyfa_id = " + id
				+ ") or zidysjy_id in (select zidysjy_id from zidyfacsb where zidyfa_id = "+id + ") order by id");
		//dx.setElementForSQL("gongsb", "select * from gongsb where id = (select gongsb_id from jjfxdxfxszb where zidyfa_id =" + id + ")");
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList("select * from zidyfa where id="+id);
		String fName = id;
		if(rs.next()){
			fName = rs.getString("z_name");
		}
		File file = new File("D:/"+ fName+".xml");
		if(!file.exists()){
			try {
				file.createNewFile();
				
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		try {
			dx.WriteFile(file);
		} catch (TransformerException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

}
