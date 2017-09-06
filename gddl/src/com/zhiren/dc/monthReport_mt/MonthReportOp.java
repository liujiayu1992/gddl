package com.zhiren.dc.monthReport_mt;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
/**
 * 
 * @author Rock
 * @since 2009-12-25
 * @version 0.1
 * @discription 月报的基本操作 如统计口径的确认 等
 */
public class MonthReportOp {
	private static String insertYuetjkj(JDBCcon con, String oraDate,
			String gongysb_id, String meikxxb_id, String jihkjb_id, 
			String pinzb_id, String yunsfsb_id, String diancxxb_id){
		String strid = MainGlobal.getNewID(con,diancxxb_id);
		/* 取得序号 */
		String sql = "select nvl(max(xuh),1) xuh from yuetjkjb_mt where riq = " + oraDate + " and diancxxb_id =" + diancxxb_id ;
		ResultSetList rs = con.getResultSetList(sql);
		long xuh = 0;
		if(rs.next()){
			xuh = rs.getLong("xuh");
		}
		rs.close();
		
		sql = "insert into yuetjkjb_mt(id,riq,diancxxb_id,xuh,gongysb_id,meikxxb_id,pinzb_id,jihkjb_id,yunsfsb_id)\n" + 
			"values(" + strid + "," + oraDate + "," + diancxxb_id + "," + xuh + "," + gongysb_id + "," + meikxxb_id +
			"," + pinzb_id + "," + jihkjb_id + "," + yunsfsb_id + ")";
		if(con.getUpdate(sql) == -1){
			return null;
		}else{
			return strid;
		}
	}
	
	public static String getYuetjkj(JDBCcon con, String oraDate,
			String gongysb_id, String meikxxb_id, String jihkjb_id, 
			String pinzb_id, String yunsfsb_id, String diancxxb_id){
		String strid;
		String sql = "select * from yuetjkjb_mt where gongysb_id = " + gongysb_id +" and meikxxb_id = " + meikxxb_id + "\n" + 
					" and jihkjb_id = " + jihkjb_id + " and pinzb_id = " + pinzb_id + "\n" +
					" and yunsfsb_id = " + yunsfsb_id + " and diancxxb_id = " + diancxxb_id + "\n" +
					" and riq = " + oraDate;
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			strid = rs.getString("id");
		}else{
			strid = insertYuetjkj(con,oraDate,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id,diancxxb_id);
		}
		return strid;
	}
}
