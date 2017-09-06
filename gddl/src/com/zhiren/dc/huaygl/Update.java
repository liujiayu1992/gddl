package com.zhiren.dc.huaygl;

import com.zhiren.common.JDBCcon;

public class Update {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

	public static void UpdateZt(JDBCcon con, String tName, long zhillsb_id,
			long fenxxmb_id) {
		String SQL = "";
		try {
			SQL = "update " + tName + " set shenhzt = 1 where zhillsb_id = "
					+ zhillsb_id + " and fenxxmb_id = " + fenxxmb_id;
			con.getUpdate(SQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void UpdateZLLSB(JDBCcon con, long zhillsb_id, double[] zhi) {
		String SQL = "";
		try {
			SQL = "update zhillsb set shenhzt = 1 where zhillsb_id = "
					+ zhillsb_id;
			con.getUpdate(SQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void InsertZhilb(JDBCcon con, long zhillsb_id) {
		String SQL = "";
		try {
			SQL = "insert into zhilb () values ()";
			con.getInsert(SQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
