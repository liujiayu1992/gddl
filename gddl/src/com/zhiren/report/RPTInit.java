package com.zhiren.report;


import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

public class RPTInit {

	public static void getInsertSql(long diancxxb_id, String buffer, Report rt,
			String title, String REPORT_NAME) {
		JDBCcon con = new JDBCcon();
		String sql = "";

		// ResultSetList rsl=con.getResultSetList(buffer);
		// String[] zidm=rsl.getColumnNames();
		String SQL = "select * from baobpzb where guanjz='" + REPORT_NAME + "'";
		ResultSetList rsl = con.getResultSetList(SQL);
		try {
			if (rsl.next()) {
				rsl.close();
				con.Close();
				return;
			}
			rsl.close();
			rsl = con.getResultSetList(buffer);
			String[] zidm = rsl.getColumnNames();
			String baobpzbid = MainGlobal.getNewID(diancxxb_id);
			String sb = "insert into baobpzb(id,guanjz,biaot) values("
					+ baobpzbid + ",'" + REPORT_NAME + "','" + title + "')";
			con.getInsert(sb);
			Report rt1 = rt;
			Column[] col = rt1.body.cols;
			int row = rt1.body.getFixedRows();
			for (int j = 1; j < col.length; j++) {
				String baobpzzbid = MainGlobal.getNewID(diancxxb_id);
				int kuand = col[j].width;
				String format=col[j].format;
				String head = "";
				for (int i = 1; i < row + 1; i++) {
					head += "!@" + rt1.body.getCellValue(i, j);
				}
				head = head.substring(2);
				sql = "insert into baobpzzb(id,baobpzb_id,zidm,biaot,xuh,kuand,shifxs,format) values("
						+ baobpzzbid
						+ ","
						+ baobpzbid
						+ ",'"
						+ zidm[j - 1]
						+ "','" + head + "'," + j + "," + kuand + ",1,'"+format+"')";
				con.getInsert(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		con.Close();

	}

}
