package com.zhiren.dc.huaygl.ruchy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.dc.huaygl.Update;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruchy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save(int flag) {
		Visit visit = (Visit) this.getPage().getVisit();
		List listqsf = ((Visit) getPage().getVisit()).getList2();
		List listlf = ((Visit) getPage().getVisit()).getList3();
		List listdt = ((Visit) getPage().getVisit()).getList4();
		List listsf = ((Visit) getPage().getVisit()).getList5();
		List listhf = ((Visit) getPage().getVisit()).getList6();
		List listhff = ((Visit) getPage().getVisit()).getList7();
		JDBCcon con = new JDBCcon();
		String sql = "";
		long diancxxb_id = visit.getDiancxxb_id();
		try {
			for (int i = 0; i < listqsf.size(); i++) {
				long id = ((Ruchyqsfbean) listqsf.get(i)).getId();
				Date fenxrq = ((Ruchyqsfbean) listqsf.get(i)).getFenxrq();
				long fenxxmb_id = ((Ruchyqsfbean) listqsf.get(i))
						.getFenxxmb_id();
				double honghzl1 = ((Ruchyqsfbean) listqsf.get(i)).getHonghzl1();
				double honghzl2 = ((Ruchyqsfbean) listqsf.get(i)).getHonghzl2();
				double honghzl3 = ((Ruchyqsfbean) listqsf.get(i)).getHonghzl3();
				double honghzl4 = ((Ruchyqsfbean) listqsf.get(i)).getHonghzl4();
				String huayyqbh = ((Ruchyqsfbean) listqsf.get(i)).getHuayyqbh();
				String lury = ((Ruchyqsfbean) listqsf.get(i)).getLury();
				double meiyzl = ((Ruchyqsfbean) listqsf.get(i)).getMeiyzl();
				String qimbh = ((Ruchyqsfbean) listqsf.get(i)).getQimbh();
				double qimmyzl = ((Ruchyqsfbean) listqsf.get(i)).getQimmyzl();
				double qimzl = ((Ruchyqsfbean) listqsf.get(i)).getQimzl();
				double shengyzl = ((Ruchyqsfbean) listqsf.get(i)).getShengyzl();
				String shenhry = ((Ruchyqsfbean) listqsf.get(i)).getShenhry();
				int shenhzt = ((Ruchyqsfbean) listqsf.get(i)).getShenhzt();
				double shiqzl = ((Ruchyqsfbean) listqsf.get(i)).getShiqzl();
				int xuh = ((Ruchyqsfbean) listqsf.get(i)).getXuh();
				double zhi = ((Ruchyqsfbean) listqsf.get(i)).getZhi();
				long zhillsb_id = ((Ruchyqsfbean) listqsf.get(i))
						.getZhillsb_id();
				double zuizhhzl = ((Ruchyqsfbean) listqsf.get(i)).getZuizhhzl();
				if (id == 0) {
					if (qimzl != 0 || meiyzl != 0 || zhi != 0) {
						sql = "insert into quansfhyb\n" + "  (ID,\n"
								+ "   XUH,\n" + "   ZHILLSB_ID,\n"
								+ "   FENXRQ,\n" + "   FENXXMB_ID,\n"
								+ "   HUAYYQBH,\n" + "   QIMBH,\n"
								+ "   QIMMYZL,\n" + "   QIMZL,\n"
								+ "   MEIYZL,\n" + "   HONGHZL1,\n"
								+ "   HONGHZL2,\n" + "   HONGHZL3,\n"
								+ "   HONGHZL4,\n" + "   ZUIZHHZL,\n"
								+ "   SHIQZL,\n" + "   SHENGYZL,\n"
								+ "   ZHI,\n" + "   SHENHZT,\n"
								+ "   SHENHRY,\n" + "   LURY)\n" + "values\n"
								+ "  (getnewid(" + diancxxb_id + ")," + xuh
								+ "," + zhillsb_id + ",to_date('"
								+ DateUtil.FormatDate(fenxrq)
								+ "','yyyy-mm-dd')," + fenxxmb_id + ",'"
								+ huayyqbh + "','" + qimbh + "'," + qimmyzl
								+ "," + qimzl + "," + meiyzl + "," + honghzl1
								+ "," + honghzl2 + "," + honghzl3 + ","
								+ honghzl4 + "," + zuizhhzl + "," + shiqzl
								+ "," + shengyzl + "," + zhi + "," + shenhzt
								+ ",'" + shenhry + "','" + visit.getRenymc()
								+ "')";
						con.getInsert(sql);
					}
				} else {
					sql = "update quansfhyb\n" + "   set FENXRQ   = to_date('"
							+ DateUtil.FormatDate(fenxrq)
							+ "', 'yyyy-mm-dd'),\n" + "       HUAYYQBH = '"
							+ huayyqbh + "',\n" + "       QIMBH    = '" + qimbh
							+ "',\n" + "       QIMMYZL  = " + qimmyzl + ",\n"
							+ "       QIMZL    = " + qimzl + ",\n"
							+ "       MEIYZL   = " + meiyzl + ",\n"
							+ "       HONGHZL1 = " + honghzl1 + ",\n"
							+ "       HONGHZL2 = " + honghzl2 + ",\n"
							+ "       HONGHZL3 = " + honghzl3 + ",\n"
							+ "       HONGHZL4 = " + honghzl4 + ",\n"
							+ "       ZUIZHHZL = " + zuizhhzl + ",\n"
							+ "       SHIQZL   = " + shiqzl + ",\n"
							+ "       SHENGYZL = " + shengyzl + ",\n"
							+ "       ZHI      = " + zhi + ",\n"
							+ "       SHENHZT  = " + shenhzt + ",\n"
							+ "       SHENHRY  = '" + shenhry + "',\n"
							+ "       LURY     = '" + visit.getRenymc() + "'\n"
							+ " where id = " + id + "\n"
							+ "   and zhillsb_id = " + zhillsb_id + "\n"
							+ "   and xuh = " + xuh + "\n"
							+ "   and fenxxmb_id = " + fenxxmb_id;
					con.getUpdate(sql);
				}
			}
			for (int i = 0; i < listlf.size(); i++) {
				long id = ((Ruchylfbean) listlf.get(i)).getId();
				Date fenxrq = ((Ruchylfbean) listlf.get(i)).getFenxrq();
				long fenxxmb_id = ((Ruchylfbean) listlf.get(i)).getFenxxmb_id();
				String huayyqbh = ((Ruchylfbean) listlf.get(i)).getHuayyqbh();
				double liuf = ((Ruchylfbean) listlf.get(i)).getLiuf();
				String lury = ((Ruchylfbean) listlf.get(i)).getLury();
				double meiyzl = ((Ruchylfbean) listlf.get(i)).getMeiyzl();
				double qimmyzl = ((Ruchylfbean) listlf.get(i)).getQimmyzl();
				double qimzl = ((Ruchylfbean) listlf.get(i)).getQimzl();
				String shenhry = ((Ruchylfbean) listlf.get(i)).getShenhry();
				int shenhzt = ((Ruchylfbean) listlf.get(i)).getShenhzt();
				int xuh = ((Ruchylfbean) listlf.get(i)).getXuh();
				double zhi = ((Ruchylfbean) listlf.get(i)).getZhi();
				long zhillsb_id = ((Ruchylfbean) listlf.get(i)).getZhillsb_id();
				if (id == 0) {
					if (qimzl != 0 || meiyzl != 0 || zhi != 0 || liuf != 0) {
						sql = "insert into liufhyb\n" + "  (ID,\n"
								+ "   XUH,\n" + "   ZHILLSB_ID,\n"
								+ "   FENXRQ,\n" + "   FENXXMB_ID,\n"
								+ "   HUAYYQBH,\n" + "   MEIYZL,\n"
								+ "   ZHI,\n" + "   SHENHZT,\n"
								+ "   SHENHRY,\n" + "   LURY,\n"
								+ "   QIMMYZL,\n" + "   QIMZL,\n"
								+ "   LIUF)\n" + "values\n" + "  (getnewid("
								+ diancxxb_id + ")," + xuh + "," + zhillsb_id
								+ ",to_date('" + DateUtil.FormatDate(fenxrq)
								+ "','yyyy-mm-dd')," + fenxxmb_id + ",'"
								+ huayyqbh + "'," + meiyzl + "," + zhi + ","
								+ shenhzt + ",'" + shenhry + "','"
								+ visit.getRenymc() + "'," + qimmyzl + ","
								+ qimzl + "," + liuf + ")";
						con.getInsert(sql);
					}
				} else {
					sql = "update liufhyb\n" + "  set FENXRQ=to_date('"
							+ DateUtil.FormatDate(fenxrq)
							+ "','yyyy-mm-dd'),\n" + "   HUAYYQBH = '"
							+ huayyqbh + "',\n" + "   MEIYZL=" + meiyzl + ",\n"
							+ "   ZHI=" + zhi + ",\n" + "   SHENHZT=" + shenhzt
							+ ",\n" + "   SHENHRY='" + shenhry + "',\n"
							+ "   LURY='" + visit.getRenymc() + "',\n"
							+ "   QIMMYZL=" + qimmyzl + ",\n" + "   QIMZL="
							+ qimzl + ",\n" + "   LIUF=" + liuf + " where \n"
							+ "  id = " + id + "and xuh = " + xuh
							+ " and zhillsb_id = " + zhillsb_id
							+ "and fenxxmb_id = " + fenxxmb_id;
					con.getUpdate(sql);
				}
			}
			for (int i = 0; i < listdt.size(); i++) {
				long id = ((Ruchydtrbean) listdt.get(i)).getId();
				Date fenxrq = ((Ruchydtrbean) listdt.get(i)).getFenxrq();
				long fenxxmb_id = ((Ruchydtrbean) listdt.get(i))
						.getFenxxmb_id();
				String huayyqbh = ((Ruchydtrbean) listdt.get(i)).getHuayyqbh();
				String lury = ((Ruchydtrbean) listdt.get(i)).getLury();
				double meiyzl = ((Ruchydtrbean) listdt.get(i)).getMeiyzl();
				double qimmyzl = ((Ruchydtrbean) listdt.get(i)).getQimmyzl();
				double qimzl = ((Ruchydtrbean) listdt.get(i)).getQimzl();
				String shenhry = ((Ruchydtrbean) listdt.get(i)).getShenhry();
				int shenhzt = ((Ruchydtrbean) listdt.get(i)).getShenhzt();
				double tianjwrz = ((Ruchydtrbean) listdt.get(i)).getTianjwrz();
				double tianjwzl = ((Ruchydtrbean) listdt.get(i)).getTianjwzl();
				int xuh = ((Ruchydtrbean) listdt.get(i)).getXuh();
				double zhi = ((Ruchydtrbean) listdt.get(i)).getZhi();
				long zhillsb_id = ((Ruchydtrbean) listdt.get(i))
						.getZhillsb_id();
				if (id == 0) {
					if (qimzl != 0 || meiyzl != 0 || zhi != 0) {
						sql = "insert into danthyb\n" + "  (ID,\n"
								+ "   XUH,\n" + "   ZHILLSB_ID,\n"
								+ "   FENXRQ,\n" + "   FENXXMB_ID,\n"
								+ "   HUAYYQBH,\n" + "   QIMZL,\n"
								+ "   MEIYZL,\n" + "   SHENHZT,\n"
								+ "   SHENHRY,\n" + "   LURY,\n"
								+ "   TIANJWRZ,\n" + "   TIANJWZL,\n"
								+ "   ZHI,\n" + "   QIMMYZL)\n" + "values\n"
								+ "  (getnewid(" + diancxxb_id + ")," + xuh
								+ "," + zhillsb_id + ",to_date('"
								+ DateUtil.FormatDate(fenxrq)
								+ "','yyyy-mm-dd')," + fenxxmb_id + ",'"
								+ huayyqbh + "'," + qimzl + "," + meiyzl + ","
								+ shenhzt + ",'" + shenhry + "','"
								+ visit.getRenymc() + "'," + tianjwrz + ","
								+ tianjwzl + "," + zhi + "," + qimmyzl + ")";
						con.getInsert(sql);
					}
				} else {
					sql = "update danthyb\n" + "set FENXRQ = to_date('"
							+ DateUtil.FormatDate(fenxrq)
							+ "','yyyy-mm-dd'),\n" + "HUAYYQBH = '" + huayyqbh
							+ "',\n" + "QIMZL = " + qimzl + ",\n"
							+ "   MEIYZL=" + meiyzl + ",\n" + "   SHENHZT="
							+ shenhzt + ",\n" + "   SHENHRY='" + shenhry
							+ "',\n" + "   LURY='" + visit.getRenymc() + "',\n"
							+ "   TIANJWRZ=" + tianjwrz + ",\n"
							+ "   TIANJWZL=" + tianjwzl + ",\n" + "   ZHI="
							+ zhi + ",\n" + "   QIMMYZL=" + qimmyzl + "\n"
							+ "where id =" + id + " and xuh = " + xuh
							+ " and zhillsb_id = " + zhillsb_id
							+ " and fenxxmb_id = " + fenxxmb_id;
					con.getUpdate(sql);
				}
			}
			for (int i = 0; i < listsf.size(); i++) {
				long id = ((Ruchygyfxbean) listsf.get(i)).getId();
				Date fenxrq = ((Ruchygyfxbean) listsf.get(i)).getFenxrq();
				long fenxxmb_id = ((Ruchygyfxbean) listsf.get(i))
						.getFenxxmb_id();
				double honghzl1 = ((Ruchygyfxbean) listsf.get(i)).getHonghzl1();
				double honghzl2 = ((Ruchygyfxbean) listsf.get(i)).getHonghzl2();
				double honghzl3 = ((Ruchygyfxbean) listsf.get(i)).getHonghzl3();
				double honghzl4 = ((Ruchygyfxbean) listsf.get(i)).getHonghzl4();
				String huayyqbh = ((Ruchygyfxbean) listsf.get(i)).getHuayyqbh();
				String lury = ((Ruchygyfxbean) listsf.get(i)).getLury();
				double meiyzl = ((Ruchygyfxbean) listsf.get(i)).getMeiyzl();
				String qimbh = ((Ruchygyfxbean) listsf.get(i)).getQimbh();
				double qimmyzl = ((Ruchygyfxbean) listsf.get(i)).getQimmyzl();
				double qimzl = ((Ruchygyfxbean) listsf.get(i)).getQimzl();
				double shengyzl = ((Ruchygyfxbean) listsf.get(i)).getShengyzl();
				String shenhry = ((Ruchygyfxbean) listsf.get(i)).getShenhry();
				int shenhzt = ((Ruchygyfxbean) listsf.get(i)).getShenhzt();
				double shiqzl = ((Ruchygyfxbean) listsf.get(i)).getShiqzl();
				int xuh = ((Ruchygyfxbean) listsf.get(i)).getXuh();
				double zhi = ((Ruchygyfxbean) listsf.get(i)).getZhi();
				long zhillsb_id = ((Ruchygyfxbean) listsf.get(i))
						.getZhillsb_id();
				double zuizhhzl = ((Ruchygyfxbean) listsf.get(i)).getZuizhhzl();
				if (id == 0) {
					if (qimzl != 0 || meiyzl != 0 || zhi != 0) {
						sql = "insert into gongyfxb\n" + "  (ID,\n"
								+ "   XUH,\n" + "   ZHILLSB_ID,\n"
								+ "   FENXRQ,\n" + "   FENXXMB_ID,\n"
								+ "   HUAYYQBH,\n" + "   QIMBH,\n"
								+ "   QIMMYZL,\n" + "   QIMZL,\n"
								+ "   MEIYZL,\n" + "   HONGHZL1,\n"
								+ "   HONGHZL2,\n" + "   HONGHZL3,\n"
								+ "   HONGHZL4,\n" + "   ZUIZHHZL,\n"
								+ "   SHIQZL,\n" + "   SHENGYZL,\n"
								+ "   ZHI,\n" + "   SHENHZT,\n"
								+ "   SHENHRY,\n" + "   LURY)\n" + "values\n"
								+ "  (getnewid(" + diancxxb_id + ")," + xuh
								+ "," + zhillsb_id + ",to_date('"
								+ DateUtil.FormatDate(fenxrq)
								+ "','yyyy-mm-dd')," + fenxxmb_id + ",'"
								+ huayyqbh + "','" + qimbh + "'," + qimmyzl
								+ "," + qimzl + "," + meiyzl + "," + honghzl1
								+ "," + honghzl2 + "," + honghzl3 + ","
								+ honghzl4 + "," + zuizhhzl + "," + shiqzl
								+ "," + shengyzl + "," + zhi + "," + shenhzt
								+ ",'" + shenhry + "','" + visit.getRenymc()
								+ "')";
						con.getInsert(sql);
					}
				} else {
					sql = "update gongyfxb\n" + "   set FENXRQ   = to_date('"
							+ DateUtil.FormatDate(fenxrq)
							+ "', 'yyyy-mm-dd'),\n" + "       HUAYYQBH = '"
							+ huayyqbh + "',\n" + "       QIMBH    = '" + qimbh
							+ "',\n" + "       QIMMYZL  = " + qimmyzl + ",\n"
							+ "       QIMZL    = " + qimzl + ",\n"
							+ "       MEIYZL   = " + meiyzl + ",\n"
							+ "       HONGHZL1 = " + honghzl1 + ",\n"
							+ "       HONGHZL2 = " + honghzl2 + ",\n"
							+ "       HONGHZL3 = " + honghzl3 + ",\n"
							+ "       HONGHZL4 = " + honghzl4 + ",\n"
							+ "       ZUIZHHZL = " + zuizhhzl + ",\n"
							+ "       SHIQZL   = " + shiqzl + ",\n"
							+ "       SHENGYZL = " + shengyzl + ",\n"
							+ "       ZHI      = " + zhi + ",\n"
							+ "       SHENHZT  = " + shenhzt + ",\n"
							+ "       SHENHRY  = '" + shenhry + "',\n"
							+ "       LURY     = '" + visit.getRenymc() + "'\n"
							+ " where id = " + id + "\n"
							+ "   and zhillsb_id = " + zhillsb_id + "\n"
							+ "   and xuh = " + xuh + "\n"
							+ "   and fenxxmb_id = " + fenxxmb_id;
					con.getUpdate(sql);
				}
			}
			for (int i = 0; i < listhf.size(); i++) {
				long id = ((Ruchygyfxbean) listhf.get(i)).getId();
				Date fenxrq = ((Ruchygyfxbean) listhf.get(i)).getFenxrq();
				long fenxxmb_id = ((Ruchygyfxbean) listhf.get(i))
						.getFenxxmb_id();
				double honghzl1 = ((Ruchygyfxbean) listhf.get(i)).getHonghzl1();
				double honghzl2 = ((Ruchygyfxbean) listhf.get(i)).getHonghzl2();
				double honghzl3 = ((Ruchygyfxbean) listhf.get(i)).getHonghzl3();
				double honghzl4 = ((Ruchygyfxbean) listhf.get(i)).getHonghzl4();
				String huayyqbh = ((Ruchygyfxbean) listhf.get(i)).getHuayyqbh();
				String lury = ((Ruchygyfxbean) listhf.get(i)).getLury();
				double meiyzl = ((Ruchygyfxbean) listhf.get(i)).getMeiyzl();
				String qimbh = ((Ruchygyfxbean) listhf.get(i)).getQimbh();
				double qimmyzl = ((Ruchygyfxbean) listhf.get(i)).getQimmyzl();
				double qimzl = ((Ruchygyfxbean) listhf.get(i)).getQimzl();
				double shengyzl = ((Ruchygyfxbean) listhf.get(i)).getShengyzl();
				String shenhry = ((Ruchygyfxbean) listhf.get(i)).getShenhry();
				int shenhzt = ((Ruchygyfxbean) listhf.get(i)).getShenhzt();
				double shiqzl = ((Ruchygyfxbean) listhf.get(i)).getShiqzl();
				int xuh = ((Ruchygyfxbean) listhf.get(i)).getXuh();
				double zhi = ((Ruchygyfxbean) listhf.get(i)).getZhi();
				long zhillsb_id = ((Ruchygyfxbean) listhf.get(i))
						.getZhillsb_id();
				double zuizhhzl = ((Ruchygyfxbean) listhf.get(i)).getZuizhhzl();
				if (id == 0) {
					if (qimzl != 0 || meiyzl != 0 || zhi != 0) {
						sql = "insert into gongyfxb\n" + "  (ID,\n"
								+ "   XUH,\n" + "   ZHILLSB_ID,\n"
								+ "   FENXRQ,\n" + "   FENXXMB_ID,\n"
								+ "   HUAYYQBH,\n" + "   QIMBH,\n"
								+ "   QIMMYZL,\n" + "   QIMZL,\n"
								+ "   MEIYZL,\n" + "   HONGHZL1,\n"
								+ "   HONGHZL2,\n" + "   HONGHZL3,\n"
								+ "   HONGHZL4,\n" + "   ZUIZHHZL,\n"
								+ "   SHIQZL,\n" + "   SHENGYZL,\n"
								+ "   ZHI,\n" + "   SHENHZT,\n"
								+ "   SHENHRY,\n" + "   LURY)\n" + "values\n"
								+ "  (getnewid(" + diancxxb_id + ")," + xuh
								+ "," + zhillsb_id + ",to_date('"
								+ DateUtil.FormatDate(fenxrq)
								+ "','yyyy-mm-dd')," + fenxxmb_id + ",'"
								+ huayyqbh + "','" + qimbh + "'," + qimmyzl
								+ "," + qimzl + "," + meiyzl + "," + honghzl1
								+ "," + honghzl2 + "," + honghzl3 + ","
								+ honghzl4 + "," + zuizhhzl + "," + shiqzl
								+ "," + shengyzl + "," + zhi + "," + shenhzt
								+ ",'" + shenhry + "','" + visit.getRenymc()
								+ "')";
						con.getInsert(sql);
					}
				} else {
					sql = "update gongyfxb\n" + "   set FENXRQ   = to_date('"
							+ DateUtil.FormatDate(fenxrq)
							+ "', 'yyyy-mm-dd'),\n" + "       HUAYYQBH = '"
							+ huayyqbh + "',\n" + "       QIMBH    = '" + qimbh
							+ "',\n" + "       QIMMYZL  = " + qimmyzl + ",\n"
							+ "       QIMZL    = " + qimzl + ",\n"
							+ "       MEIYZL   = " + meiyzl + ",\n"
							+ "       HONGHZL1 = " + honghzl1 + ",\n"
							+ "       HONGHZL2 = " + honghzl2 + ",\n"
							+ "       HONGHZL3 = " + honghzl3 + ",\n"
							+ "       HONGHZL4 = " + honghzl4 + ",\n"
							+ "       ZUIZHHZL = " + zuizhhzl + ",\n"
							+ "       SHIQZL   = " + shiqzl + ",\n"
							+ "       SHENGYZL = " + shengyzl + ",\n"
							+ "       ZHI      = " + zhi + ",\n"
							+ "       SHENHZT  = " + shenhzt + ",\n"
							+ "       SHENHRY  = '" + shenhry + "',\n"
							+ "       LURY     = '" + visit.getRenymc() + "'\n"
							+ " where id = " + id + "\n"
							+ "   and zhillsb_id = " + zhillsb_id + "\n"
							+ "   and xuh = " + xuh + "\n"
							+ "   and fenxxmb_id = " + fenxxmb_id;
					con.getUpdate(sql);
				}
			}
			for (int i = 0; i < listhff.size(); i++) {
				long id = ((Ruchygyfxbean) listhff.get(i)).getId();
				Date fenxrq = ((Ruchygyfxbean) listhff.get(i)).getFenxrq();
				long fenxxmb_id = ((Ruchygyfxbean) listhff.get(i))
						.getFenxxmb_id();
				double honghzl1 = ((Ruchygyfxbean) listhff.get(i))
						.getHonghzl1();
				double honghzl2 = ((Ruchygyfxbean) listhff.get(i))
						.getHonghzl2();
				double honghzl3 = ((Ruchygyfxbean) listhff.get(i))
						.getHonghzl3();
				double honghzl4 = ((Ruchygyfxbean) listhff.get(i))
						.getHonghzl4();
				String huayyqbh = ((Ruchygyfxbean) listhff.get(i))
						.getHuayyqbh();
				String lury = ((Ruchygyfxbean) listhff.get(i)).getLury();
				double meiyzl = ((Ruchygyfxbean) listhff.get(i)).getMeiyzl();
				String qimbh = ((Ruchygyfxbean) listhff.get(i)).getQimbh();
				double qimmyzl = ((Ruchygyfxbean) listhff.get(i)).getQimmyzl();
				double qimzl = ((Ruchygyfxbean) listhff.get(i)).getQimzl();
				double shengyzl = ((Ruchygyfxbean) listhff.get(i))
						.getShengyzl();
				String shenhry = ((Ruchygyfxbean) listhff.get(i)).getShenhry();
				int shenhzt = ((Ruchygyfxbean) listhff.get(i)).getShenhzt();
				double shiqzl = ((Ruchygyfxbean) listhff.get(i)).getShiqzl();
				int xuh = ((Ruchygyfxbean) listhff.get(i)).getXuh();
				double zhi = ((Ruchygyfxbean) listhff.get(i)).getZhi();
				long zhillsb_id = ((Ruchygyfxbean) listhff.get(i))
						.getZhillsb_id();
				double zuizhhzl = ((Ruchygyfxbean) listhff.get(i))
						.getZuizhhzl();
				if (id == 0) {
					if (qimzl != 0 || meiyzl != 0 || zhi != 0) {
						sql = "insert into gongyfxb\n" + "  (ID,\n"
								+ "   XUH,\n" + "   ZHILLSB_ID,\n"
								+ "   FENXRQ,\n" + "   FENXXMB_ID,\n"
								+ "   HUAYYQBH,\n" + "   QIMBH,\n"
								+ "   QIMMYZL,\n" + "   QIMZL,\n"
								+ "   MEIYZL,\n" + "   HONGHZL1,\n"
								+ "   HONGHZL2,\n" + "   HONGHZL3,\n"
								+ "   HONGHZL4,\n" + "   ZUIZHHZL,\n"
								+ "   SHIQZL,\n" + "   SHENGYZL,\n"
								+ "   ZHI,\n" + "   SHENHZT,\n"
								+ "   SHENHRY,\n" + "   LURY)\n" + "values\n"
								+ "  (getnewid(" + diancxxb_id + ")," + xuh
								+ "," + zhillsb_id + ",to_date('"
								+ DateUtil.FormatDate(fenxrq)
								+ "','yyyy-mm-dd')," + fenxxmb_id + ",'"
								+ huayyqbh + "','" + qimbh + "'," + qimmyzl
								+ "," + qimzl + "," + meiyzl + "," + honghzl1
								+ "," + honghzl2 + "," + honghzl3 + ","
								+ honghzl4 + "," + zuizhhzl + "," + shiqzl
								+ "," + shengyzl + "," + zhi + "," + shenhzt
								+ ",'" + shenhry + "','" + visit.getRenymc()
								+ "')";
						con.getInsert(sql);
					}
				} else {
					sql = "update gongyfxb\n" + "   set FENXRQ   = to_date('"
							+ DateUtil.FormatDate(fenxrq)
							+ "', 'yyyy-mm-dd'),\n" + "       HUAYYQBH = '"
							+ huayyqbh + "',\n" + "       QIMBH    = '" + qimbh
							+ "',\n" + "       QIMMYZL  = " + qimmyzl + ",\n"
							+ "       QIMZL    = " + qimzl + ",\n"
							+ "       MEIYZL   = " + meiyzl + ",\n"
							+ "       HONGHZL1 = " + honghzl1 + ",\n"
							+ "       HONGHZL2 = " + honghzl2 + ",\n"
							+ "       HONGHZL3 = " + honghzl3 + ",\n"
							+ "       HONGHZL4 = " + honghzl4 + ",\n"
							+ "       ZUIZHHZL = " + zuizhhzl + ",\n"
							+ "       SHIQZL   = " + shiqzl + ",\n"
							+ "       SHENGYZL = " + shengyzl + ",\n"
							+ "       ZHI      = " + zhi + ",\n"
							+ "       SHENHZT  = " + shenhzt + ",\n"
							+ "       SHENHRY  = '" + shenhry + "',\n"
							+ "       LURY     = '" + visit.getRenymc() + "'\n"
							+ " where id = " + id + "\n"
							+ "   and zhillsb_id = " + zhillsb_id + "\n"
							+ "   and xuh = " + xuh + "\n"
							+ "   and fenxxmb_id = " + fenxxmb_id;
					con.getUpdate(sql);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
			getSelectData();
		}
		if (flag == 0) {
			visit.setList2(null);
			visit.setList3(null);
			visit.setList4(null);
			visit.setList5(null);
			visit.setList6(null);
			visit.setList7(null);
		}
	}

	// 全水分平均值
	private double avgqsf;

	public double getAvgqsf() {
		return avgqsf;
	}

	public void setAvgqsf(double value) {
		avgqsf = value;
	}

	// 硫分平均值
	private double avglf;

	public double getAvglf() {
		return avglf;
	}

	public void setAvglf(double value) {
		avglf = value;
	}

	// 灰分平均值
	private double avghf;

	public double getAvghf() {
		return avghf;
	}

	public void setAvghf(double value) {
		avghf = value;
	}

	// 挥发分平均值
	private double avghff;

	public double getAvghff() {
		return avghff;
	}

	public void setAvghff(double value) {
		avghff = value;
	}

	// 水分平均值
	private double avgsf;

	public double getAvgsf() {
		return avgsf;
	}

	public void setAvgsf(double value) {
		avgsf = value;
	}

	// 弹筒平均值
	private double avgdt;

	public double getAvgdt() {
		return avgdt;
	}

	public void setAvgdt(double value) {
		avgdt = value;
	}

	// 全水分
	public List getList() {
		return ((Visit) getPage().getVisit()).getList2();
	}

	public void setList(List editList) {
		((Visit) getPage().getVisit()).setList2(editList);
	}

	// 硫分
	public List getList1() {
		return ((Visit) getPage().getVisit()).getList3();
	}

	public void setList1(List editList) {
		((Visit) getPage().getVisit()).setList3(editList);
	}

	// 弹筒
	public List getList2() {
		return ((Visit) getPage().getVisit()).getList4();
	}

	public void setList2(List editList) {
		((Visit) getPage().getVisit()).setList4(editList);
	}

	// 水分
	public List getList3() {
		return ((Visit) getPage().getVisit()).getList5();
	}

	public void setList3(List editList) {
		((Visit) getPage().getVisit()).setList5(editList);
	}

	// 灰分
	public List getList4() {
		return ((Visit) getPage().getVisit()).getList6();
	}

	public void setList4(List editList) {
		((Visit) getPage().getVisit()).setList6(editList);
	}

	// 挥发分
	public List getList5() {
		return ((Visit) getPage().getVisit()).getList7();
	}

	public void setList5(List editList) {
		((Visit) getPage().getVisit()).setList7(editList);
	}

	// /////全水分///////
	private Ruchyqsfbean _HyqsfValue;

	public Ruchyqsfbean getHyqsfValue() {
		return _HyqsfValue;
	}

	public void setHyqsfValue(Ruchyqsfbean value) {
		_HyqsfValue = value;
	}

	// /////弹筒///////
	private Ruchydtrbean _HydtValue;

	public Ruchydtrbean getHydtValue() {
		return _HydtValue;
	}

	public void setHydtValue(Ruchydtrbean value) {
		_HydtValue = value;
	}

	// /////硫分///////
	private Ruchylfbean _HylfValue;

	public Ruchylfbean getHylfValue() {
		return _HylfValue;
	}

	public void setHylfValue(Ruchylfbean value) {
		_HylfValue = value;
	}

	// /////水分///////
	private Ruchygyfxbean _HysfValue;

	public Ruchygyfxbean getHysfValue() {
		return _HysfValue;
	}

	public void setHysfValue(Ruchygyfxbean value) {
		_HysfValue = value;
	}

	// /////灰分///////
	private Ruchygyfxbean _HyhfValue;

	public Ruchygyfxbean getHyhfValue() {
		return _HyhfValue;
	}

	public void setHyhfValue(Ruchygyfxbean value) {
		_HyhfValue = value;
	}

	// /////挥发分///////
	private Ruchygyfxbean _HyhffValue;

	public Ruchygyfxbean getHyhffValue() {
		return _HyhffValue;
	}

	public void setHyhffValue(Ruchygyfxbean value) {
		_HyhffValue = value;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _RbishChick = false;

	public void RbishButton(IRequestCycle cycle) {
		_RbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SubmitChick = false;

	public void SubmitButton(IRequestCycle cycle) {
		_SubmitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_RbishChick) {
			_RbishChick = false;
			getAnalysis();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save(0);
			// getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_SubmitChick) {
			_SubmitChick = false;
			Submit();
		}
	}

	private void Refurbish() {
		getSelectData();
	}

	public void Delete() {
		Visit visit = (Visit) getPage().getVisit();
		visit.setList1(null);
		visit.setList2(null);
		visit.setList3(null);
		visit.setList4(null);
		visit.setList5(null);
		visit.setList6(null);
		visit.setList7(null);
	}

	public void Submit() {
		Visit visit = (Visit) getPage().getVisit();
		Save(1);
		JDBCcon con = new JDBCcon();
		String sql = "";
		long diancxxb_id = visit.getDiancxxb_id();
		try {
			double shuif = 0;
			double huif = 0;
			double huiff = 0;
			double quansf = 0;
			double liuf = 0;
			double dantr = 0;
			int count = 0;
			long[] fenxxmb_id = new long[6];
			double[] zhi = new double[6];
			sql = "select b.zhillsb_id as zhillsb_id, b.fenxxmb_id as fenxxmb_id,f.mingc as fenxxm, b.zhi as zhi\n"
					+ "  from ((select zhillsb_id, fenxxmb_id, avg(zhi) as zhi\n"
					+ "           from quansfhyb\n"
					+ "          where zhi <> 0\n"
					+ "          group by zhillsb_id, fenxxmb_id) union\n"
					+ "        (select zhillsb_id, fenxxmb_id, avg(zhi) as zhi\n"
					+ "           from gongyfxb\n"
					+ "          where zhi <> 0\n"
					+ "          group by zhillsb_id, fenxxmb_id) union\n"
					+ "        (select zhillsb_id, fenxxmb_id, avg(zhi) as zhi\n"
					+ "           from danthyb\n"
					+ "          where zhi <> 0\n"
					+ "          group by zhillsb_id, fenxxmb_id) union\n"
					+ "        (select zhillsb_id, fenxxmb_id, avg(zhi) as zhi\n"
					+ "           from liufhyb\n"
					+ "          where zhi <> 0\n"
					+ "          group by zhillsb_id, fenxxmb_id)) b,\n"
					+ "       fenxxmb f\n"
					+ " where f.id = b.fenxxmb_id\n"
					+ "   and b.zhillsb_id = "
					+ zhillsbid
					+ "\n"
					+ " order by f.xuh";
			ResultSetList rs = con.getResultSetList(sql);
			while (rs.next()) {
				if (rs.getString("fenxxm").equals("全水分")) {
					zhi[0] = rs.getDouble("zhi");
					fenxxmb_id[0] = rs.getLong("fenxxmb_id");
				}
				if (rs.getString("fenxxm").equals("水分")) {
					zhi[1] = rs.getDouble("zhi");
					fenxxmb_id[1] = rs.getLong("fenxxmb_id");
				}
				if (rs.getString("fenxxm").equals("灰分")) {
					zhi[2] = rs.getDouble("zhi");
					fenxxmb_id[2] = rs.getLong("fenxxmb_id");
				}
				if (rs.getString("fenxxm").equals("挥发分")) {
					zhi[3] = rs.getDouble("zhi");
					fenxxmb_id[3] = rs.getLong("fenxxmb_id");
				}
				if (rs.getString("fenxxm").equals("硫分")) {
					zhi[4] = rs.getDouble("zhi");
					fenxxmb_id[4] = rs.getLong("fenxxmb_id");
				}
				if (rs.getString("fenxxm").equals("弹筒热值")) {
					zhi[5] = rs.getDouble("zhi");
					fenxxmb_id[5] = rs.getLong("fenxxmb_id");
				}
				count++;
			}
			if (count == 6 && quansf != 0 && shuif != 0 && huif != 0
					&& huiff != 0 && liuf != 0 && dantr != 0) {
				Update.UpdateZt(con, "quansfhyb", zhillsbid, fenxxmb_id[0]);
				Update.UpdateZt(con, "gongyfxb", zhillsbid, fenxxmb_id[1]);
				Update.UpdateZt(con, "gongyfxb", zhillsbid, fenxxmb_id[2]);
				Update.UpdateZt(con, "gongyfxb", zhillsbid, fenxxmb_id[3]);
				Update.UpdateZt(con, "danthyb", zhillsbid, fenxxmb_id[5]);
				Update.UpdateZt(con, "liufhyb", zhillsbid, fenxxmb_id[4]);
				Compute.ComputeValue(con, zhillsbid, diancxxb_id, zhi);
			} else {
				if (quansf != 0) {
					Update.UpdateZt(con, "quansfhyb", zhillsbid, fenxxmb_id[0]);
				}
				if (shuif != 0) {
					Update.UpdateZt(con, "gongyfxb", zhillsbid, fenxxmb_id[1]);
				}
				if (huif != 0) {
					Update.UpdateZt(con, "gongyfxb", zhillsbid, fenxxmb_id[2]);
				}
				if (huiff != 0) {
					Update.UpdateZt(con, "gongyfxb", zhillsbid, fenxxmb_id[3]);
				}
				if (liuf != 0) {
					Update.UpdateZt(con, "liufhyb", zhillsbid, fenxxmb_id[4]);
				}
				if (dantr != 0) {
					Update.UpdateZt(con, "danthyb", zhillsbid, fenxxmb_id[5]);
				}
				Compute.ComputeValue(con, zhillsbid, diancxxb_id, zhi);
			}
			// Update.UpdateZLLSB(con, zhillsbid, zhi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
			getSelectData();
		}
		visit.setList1(null);
		visit.setList2(null);
		visit.setList3(null);
		visit.setList4(null);
		visit.setList5(null);
		visit.setList6(null);
		visit.setList7(null);
	}

	private long zhillsbid = 0;

	public void getAnalysis() {
		List listqsf = new ArrayList();
		List listlf = new ArrayList();
		List listhf = new ArrayList();
		List listhff = new ArrayList();
		List listdt = new ArrayList();
		List listsf = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			zhillsbid = Long.parseLong(getTreeid());
			// 全水分
			double avgzhi = 0.0D;
			String Sql = "select * from quansfhyb where zhillsb_id = "
					+ zhillsbid + " order by id";
			ResultSetList rs = con.getResultSetList(Sql);
			int count = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				int xuh = rs.getInt("xuh");
				long zhillsb_id = rs.getLong("zhillsb_id");
				Date fenxrq = rs.getDate("fenxrq");
				long fenxxmb_id = rs.getLong("fenxxmb_id");
				String huayyqbh = rs.getString("huayyqbh");
				String qimbh = rs.getString("qimbh");
				double qimmyzl = rs.getDouble("qimmyzl");
				double qimzl = rs.getDouble("qimzl");
				double meiyzl = rs.getDouble("meiyzl");
				double honghzl1 = rs.getDouble("honghzl1");
				double honghzl2 = rs.getDouble("honghzl2");
				double honghzl3 = rs.getDouble("honghzl3");
				double honghzl4 = rs.getDouble("honghzl4");
				double zuizhhzl = rs.getDouble("zuizhhzl");
				double shiqzl = rs.getDouble("shiqzl");
				double shengyzl = rs.getDouble("shengyzl");
				double zhi = rs.getDouble("zhi");
				int shenhzt = rs.getInt("shenhzt");
				String shenhry = rs.getString("shenhry");
				String lury = rs.getString("lury");
				if (zhi != 0) {
					avgzhi += zhi;
				} else {
					count++;
				}
				listqsf.add(new Ruchyqsfbean(id, xuh, zhillsb_id, fenxrq,
						fenxxmb_id, huayyqbh, qimbh, qimmyzl, qimzl, meiyzl,
						honghzl1, honghzl2, honghzl3, honghzl4, zuizhhzl,
						shiqzl, shengyzl, zhi, shenhzt, shenhry, lury));
				setList(listqsf);
			}
			avgzhi = Math.round(avgzhi / (rs.getRow() - count) * 10) / 10D;
			setAvgqsf(avgzhi);
			int i = 0;
			int h = 1;
			for (i = listqsf.size(); i < 4; i++) {
				listqsf.add(new Ruchyqsfbean(0, h, zhillsbid, new Date(), 1,
						"", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", ""));
				setList(listqsf);
				h++;
			}
			// 硫分
			avgzhi = 0.0D;
			Sql = "select * from liufhyb where zhillsb_id = " + zhillsbid
					+ " order by id";
			rs = con.getResultSetList(Sql);
			count = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				int xuh = rs.getInt("xuh");
				long zhillsb_id = rs.getLong("zhillsb_id");
				Date fenxrq = rs.getDate("fenxrq");
				long fenxxmb_id = rs.getLong("fenxxmb_id");
				String huayyqbh = rs.getString("huayyqbh");
				double meiyzl = rs.getDouble("meiyzl");
				double zhi = rs.getDouble("zhi");
				double qimzl = rs.getDouble("qimzl");
				double qimmyzl = rs.getDouble("qimmyzl");
				double liuf = rs.getDouble("liuf");
				int shenhzt = rs.getInt("shenhzt");
				String shenhry = rs.getString("shenhry");
				String lury = rs.getString("lury");
				if (zhi != 0) {
					avgzhi += zhi;
				} else {
					count++;
				}
				listlf.add(new Ruchylfbean(id, xuh, zhillsb_id, fenxrq,
						fenxxmb_id, huayyqbh, qimzl, qimmyzl, meiyzl, zhi,
						liuf, shenhzt, shenhry, lury));
				setList1(listlf);
			}
			avgzhi = Math.round(avgzhi / (rs.getRow() - count) * 10) / 10D;
			setAvglf(avgzhi);
			int j = 0;
			h = 1;
			for (j = listlf.size(); j < 4; j++) {
				listlf.add(new Ruchylfbean(0, h, zhillsbid, new Date(), 6, "",
						0, 0, 0, 0, 0, 0, "", ""));
				setList1(listlf);
				h++;
			}
			// 弹筒热
			Sql = "select * from danthyb where zhillsb_id = " + zhillsbid
					+ " order by id ";
			rs = con.getResultSetList(Sql);
			while (rs.next()) {
				long id = rs.getLong("id");
				int xuh = rs.getInt("xuh");
				long zhillsb_id = rs.getLong("zhillsb_id");
				Date fenxrq = rs.getDate("fenxrq");
				long fenxxmb_id = rs.getLong("fenxxmb_id");
				String huayyqbh = rs.getString("huayyqbh");
				double qimzl = rs.getDouble("qimzl");
				double qimmyzl = rs.getDouble("qimmyzl");
				double meiyzl = rs.getDouble("meiyzl");
				double tianjwrz = rs.getDouble("tianjwrz");
				double tianjwzl = rs.getDouble("tianjwzl");
				double zhi = rs.getDouble("zhi");
				int shenhzt = rs.getInt("shenhzt");
				String shenhry = rs.getString("shenhry");
				String lury = rs.getString("lury");
				if (zhi != 0) {
					avgzhi += zhi;
				} else {
					count++;
				}
				listdt.add(new Ruchydtrbean(id, xuh, zhillsb_id, fenxrq,
						fenxxmb_id, huayyqbh, qimzl, qimmyzl, meiyzl, zhi,
						shenhzt, shenhry, lury, tianjwrz, tianjwzl));
				setList2(listdt);
			}
			avgzhi = Math.round(avgzhi / (rs.getRow() - count) * 10) / 10D;
			setAvgdt(avgzhi);
			int k = 0;
			h = 1;
			for (k = listdt.size(); k < 4; k++) {
				listdt.add(new Ruchydtrbean(0, h, zhillsbid, new Date(), 5, "",
						0, 0, 0, 0, 0, "", "", 0, 0));
				setList2(listdt);
				h++;
			}
			// 工业分析水分
			avgzhi = 0.0D;
			count = 0;
			Sql = "select * from gongyfxb where zhillsb_id = " + zhillsbid
					+ " and fenxxmb_id = 2 order by id";
			rs = con.getResultSetList(Sql);
			while (rs.next()) {
				long id = rs.getLong("id");
				int xuh = rs.getInt("xuh");
				long zhillsb_id = rs.getLong("zhillsb_id");
				Date fenxrq = rs.getDate("fenxrq");
				long fenxxmb_id = rs.getLong("fenxxmb_id");
				String huayyqbh = rs.getString("huayyqbh");
				String qimbh = rs.getString("qimbh");
				double qimmyzl = rs.getDouble("qimmyzl");
				double qimzl = rs.getDouble("qimzl");
				double meiyzl = rs.getDouble("meiyzl");
				double honghzl1 = rs.getDouble("honghzl1");
				double honghzl2 = rs.getDouble("honghzl2");
				double honghzl3 = rs.getDouble("honghzl3");
				double honghzl4 = rs.getDouble("honghzl4");
				double zuizhhzl = rs.getDouble("zuizhhzl");
				double shiqzl = rs.getDouble("shiqzl");
				double shengyzl = rs.getDouble("shengyzl");
				double zhi = rs.getDouble("zhi");
				int shenhzt = rs.getInt("shenhzt");
				String shenhry = rs.getString("shenhry");
				String lury = rs.getString("lury");
				if (zhi != 0) {
					avgzhi += zhi;
				} else {
					count++;
				}
				listsf.add(new Ruchygyfxbean(id, xuh, zhillsb_id, fenxrq,
						fenxxmb_id, huayyqbh, qimbh, qimmyzl, qimzl, meiyzl,
						honghzl1, honghzl2, honghzl3, honghzl4, zuizhhzl,
						shiqzl, shengyzl, zhi, shenhzt, shenhry, lury));
				setList3(listsf);
			}
			avgzhi = Math.round(avgzhi / (rs.getRow() - count) * 10) / 10D;
			setAvgsf(avgzhi);
			int l = 0;
			h = 1;
			for (l = listsf.size(); l < 4; l++) {
				listsf.add(new Ruchygyfxbean(0, h, zhillsbid, new Date(), 2,
						"", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", ""));
				setList3(listsf);
				h++;
			}
			// 工业分析灰分
			avgzhi = 0.0D;
			count = 0;
			Sql = "select * from gongyfxb where zhillsb_id = " + zhillsbid
					+ " and fenxxmb_id = 4 order by id";
			rs = con.getResultSetList(Sql);
			while (rs.next()) {
				long id = rs.getLong("id");
				int xuh = rs.getInt("xuh");
				long zhillsb_id = rs.getLong("zhillsb_id");
				Date fenxrq = rs.getDate("fenxrq");
				long fenxxmb_id = rs.getLong("fenxxmb_id");
				String huayyqbh = rs.getString("huayyqbh");
				String qimbh = rs.getString("qimbh");
				double qimmyzl = rs.getDouble("qimmyzl");
				double qimzl = rs.getDouble("qimzl");
				double meiyzl = rs.getDouble("meiyzl");
				double honghzl1 = rs.getDouble("honghzl1");
				double honghzl2 = rs.getDouble("honghzl2");
				double honghzl3 = rs.getDouble("honghzl3");
				double honghzl4 = rs.getDouble("honghzl4");
				double zuizhhzl = rs.getDouble("zuizhhzl");
				double shiqzl = rs.getDouble("shiqzl");
				double shengyzl = rs.getDouble("shengyzl");
				double zhi = rs.getDouble("zhi");
				int shenhzt = rs.getInt("shenhzt");
				String shenhry = rs.getString("shenhry");
				String lury = rs.getString("lury");
				if (zhi != 0) {
					avgzhi += zhi;
				} else {
					count++;
				}
				listhf.add(new Ruchygyfxbean(id, xuh, zhillsb_id, fenxrq,
						fenxxmb_id, huayyqbh, qimbh, qimmyzl, qimzl, meiyzl,
						honghzl1, honghzl2, honghzl3, honghzl4, zuizhhzl,
						shiqzl, shengyzl, zhi, shenhzt, shenhry, lury));
				setList4(listhf);
			}
			avgzhi = Math.round(avgzhi / (rs.getRow() - count) * 10) / 10D;
			setAvghf(avgzhi);
			l = 0;
			h = 1;
			for (l = listhf.size(); l < 4; l++) {
				listhf.add(new Ruchygyfxbean(0, h, zhillsbid, new Date(), 4,
						"", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", ""));
				setList4(listhf);
				h++;
			}
			// 工业分析挥发分
			avgzhi = 0.0D;
			count = 0;
			Sql = "select * from gongyfxb where zhillsb_id = " + zhillsbid
					+ " and fenxxmb_id = 3 order by id";
			rs = con.getResultSetList(Sql);
			while (rs.next()) {
				long id = rs.getLong("id");
				int xuh = rs.getInt("xuh");
				long zhillsb_id = rs.getLong("zhillsb_id");
				Date fenxrq = rs.getDate("fenxrq");
				long fenxxmb_id = rs.getLong("fenxxmb_id");
				String huayyqbh = rs.getString("huayyqbh");
				String qimbh = rs.getString("qimbh");
				double qimmyzl = rs.getDouble("qimmyzl");
				double qimzl = rs.getDouble("qimzl");
				double meiyzl = rs.getDouble("meiyzl");
				double honghzl1 = rs.getDouble("honghzl1");
				double honghzl2 = rs.getDouble("honghzl2");
				double honghzl3 = rs.getDouble("honghzl3");
				double honghzl4 = rs.getDouble("honghzl4");
				double zuizhhzl = rs.getDouble("zuizhhzl");
				double shiqzl = rs.getDouble("shiqzl");
				double shengyzl = rs.getDouble("shengyzl");
				double zhi = rs.getDouble("zhi");
				int shenhzt = rs.getInt("shenhzt");
				String shenhry = rs.getString("shenhry");
				String lury = rs.getString("lury");
				if (zhi != 0) {
					avgzhi += zhi;
				} else {
					count++;
				}
				listhff.add(new Ruchygyfxbean(id, xuh, zhillsb_id, fenxrq,
						fenxxmb_id, huayyqbh, qimbh, qimmyzl, qimzl, meiyzl,
						honghzl1, honghzl2, honghzl3, honghzl4, zuizhhzl,
						shiqzl, shengyzl, zhi, shenhzt, shenhry, lury));
				setList5(listhff);
			}
			avgzhi = Math.round(avgzhi / (rs.getRow() - count) * 10) / 10D;
			setAvghff(avgzhi);
			l = 0;
			h = 1;
			for (l = listhff.size(); l < 4; l++) {
				listhff.add(new Ruchygyfxbean(0, h, zhillsbid, new Date(), 3,
						"", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", ""));
				setList5(listhff);
				h++;
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			con.Close();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb = new Toolbar("tbarDiv");
		tb.addText(new ToolbarText("化验编号："));
		// addToolbarItem(bianh.getScript());
		DefaultTree dt = new DefaultTree(DefaultTree.tree_hybh_win, "HuayTree",
				"" + visit.getDiancxxb_id(), "forms[0]", null, getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("HuayTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getHuaybhModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){HuayTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb.addField(tf);
		tb.addItem(tb2);
		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		tb.addItem(rbtn);
		ToolbarButton dbtn = new ToolbarButton(null, "删除",
				"function(){document.getElementById('DeleteButton').click();}");
		ToolbarButton oktn = new ToolbarButton(null, "保存",
				"function(){document.getElementById('SaveButton').click();}");
		ToolbarButton tbtn = new ToolbarButton(null, "提交",
				"function(){document.getElementById('SubmitButton').click();}");
		tb.addItem(dbtn);
		tb.addItem(oktn);
		tb.addItem(tbtn);
		setToolbar(tb);
		getAnalysis();
	}

	// private String _var;

	public String getVar() {
		String str = "";
		String items = getShowPanel(leftItem);
		if ("".equals(items)) {
			str = "hidden:true";
		} else {
			str = "hidden:false,\n items:[" + items + "]";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		return sb.toString();
	}

	// private String _var1;

	public String getVar1() {
		String str = "";
		String items = getShowPanel(rightItem);
		if ("".equals(items)) {
			str = "hidden:true";
		} else {
			str = "hidden:false,\n items:[" + items + "]";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		return sb.toString();
	}

	private int _value;

	public int getValue() {
		return _value;
	}

	public void setValue(int value) {
		_value = value;
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb) {
		((Visit) this.getPage().getVisit()).setToolbar(tb);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getRound_new() {
		String str = "function Round_new(value,_bit){\n"
				+ "  var value1;\n"
				+ "  var dbla;\n"
				+ "  value1=Math.floor(value*Math.pow(10,_bit))-Math.floor(value*Math.pow(10,_bit-1))*10;\n"
				+ "  dbla=Math.round(value * Math.pow(10, _bit)*10000000)/10000000;\n"
				+ "  if ((dbla-Math.floor(value*Math.pow(10,_bit)))>=0.5 && (dbla-Math.floor(value*Math.pow(10,_bit)))<0.6){\n"
				+ "    if ((dbla-Math.floor(value*Math.pow(10,_bit)))==0.5){\n"
				+ "      if(value1==0 || value1==2 || value1==4 || value1==6 || value1==8 ){\n"
				+ "        return Math.floor(value*Math.pow(10,_bit))/Math.pow(10,_bit);\n"
				+ "      }else{\n"
				+ "        return (Math.floor(value*Math.pow(10,_bit))+1)/Math.pow(10,_bit);\n"
				+ "      }\n"
				+ "    }else{\n"
				+ "      return Math.round(value * Math.pow (10,_bit))/ Math.pow(10,_bit);\n"
				+ "    }\n"
				+ "  }else{\n"
				+ "    return Math.round(value * Math.pow (10,_bit))/ Math.pow(10,_bit);\n"
				+ "  }\n" + "}";
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		return sb.toString();
	}

	public String getScript() {
		String Strtmpfunction = "var form = new Ext.form.FormPanel({ "
				+ "baseCls: 'x-plain', \n" + "abelWidth: 55,\n"
				+ "defaultType: 'textfield',\n" + "items: [ \n" + "{ \n"
				+ "    	fieldLabel: '化验编号：',\n" + "     name: 'bhvalue'\n"
				+ "    }]	\n" + "});\n" + " win = new Ext.Window({\n"
				+ "layout:'fit',\n" + "width:500,\n" + "height:300,\n"
				+ "closeAction:'hide',\n" + " plain: true,\n" + "title:'条件',\n"
				+ "items: [form],\n" + "buttons: [{\n" + "    text:'确定',\n"
				+ "   handler:function(){  \n"
				+ "  	document.getElementById('Value').value=1;	win.hide();\n"
				+ "document.getElementById('RbishButton').click();  \n"
				+ "  	}   \n" + "},{\n" + "   text: '取消',\n"
				+ "   handler: function(){\n" + "       win.hide();\n"
				+ "   }\n" + "}]\n" + " });";
		StringBuffer sb = new StringBuffer();
		sb.append(Strtmpfunction);
		return sb.toString();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	private static final int leftItem = 0;

	private static final int rightItem = 1;

	public String getShowPanel(int itemtype) {
		Visit visit = (Visit) getPage().getVisit();
		String Stritems = "";
		StringTokenizer st = new StringTokenizer(visit.getString1(), "-");
		for (int i = 0; st.hasMoreTokens(); i++) {
			String temp = st.nextToken();
			if ((i % 2) == itemtype) {
				Stritems = Stritems + temp + ",";
			}
		}
		return Stritems.substring(0, Stritems.length() - 1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String lx = cycle.getRequestContext().getParameter("lx");
		if (lx != null) {
			visit.setString1(lx);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			if (lx == null) {
				visit.setString1("Mt-Mad-Aad-Vad-Sd-Qar");
			}
			visit.setList1(null);
			visit.setList2(null);
			visit.setList3(null);
			visit.setList4(null);
			visit.setList5(null);
			visit.setList6(null);
			visit.setList7(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			setHuaybhValue(null);
			setHuaybhModel(null);
			setValue(0);
		}
		getSelectData();

	}

	private String m_script = "";

	public String getContext() {
		return m_script;
	}

	public void setContext(String script) {
		m_script = script;
	}

	private String bhvalue;

	public String getBhvalue() {
		return bhvalue;
	}

	public void setBhvalue(String value) {
		bhvalue = value;
	}

	private String ztvalue;

	public String getZtvalue() {
		return ztvalue;
	}

	public void setZtvalue(String value) {
		ztvalue = value;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("HuayTree_text"))
						.setValue(((IDropDownModel) getHuaybhModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc from vwgongysmk where diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		setGongysDropDownModel(new IDropDownModel(sql, "全部"));
		return;
	}

	private boolean ischange;

	private static IPropertySelectionModel _ShujztModel;

	public IPropertySelectionModel getShujztModel() {
		if (_ShujztModel == null) {
			getShujztModels();
		}
		return _ShujztModel;
	}

	private IDropDownBean _ShujztValue;

	public IDropDownBean getShujztValue() {
		if (_ShujztValue == null) {
			_ShujztValue = (IDropDownBean) getShujztModels().getOption(0);
		}
		return _ShujztValue;
	}

	public void setShujztValue(IDropDownBean Value) {
		if (Value != null && getShujztValue() != null) {
			if (Value.getId() != getShujztValue().getId()) {
				ischange = true;
			}
		}
		_ShujztValue = Value;
	}

	public IPropertySelectionModel getShujztModels() {
		List listShujzt = new ArrayList();
		listShujzt.add(new IDropDownBean(0, "未录入"));
		listShujzt.add(new IDropDownBean(1, "未提交"));
		_ShujztModel = new IDropDownModel(listShujzt);
		return _ShujztModel;
	}

	public void setShujztModel(IPropertySelectionModel _value) {
		_ShujztModel = _value;
	}

	private IDropDownBean HuaybhValue;

	private String strhuaybh;

	public IDropDownBean getHuaybhValue() {
		int i = 0;
		if (HuaybhValue == null) {// 查找的典型例子
			while (i < getHuaybhModel().getOptionCount()
					&& !((IDropDownBean) getHuaybhModel().getOption(i))
							.getValue().equals(strhuaybh)) {
				i++;
			}
			if (i >= getHuaybhModel().getOptionCount()) {// 没找到
				HuaybhValue = (IDropDownBean) getHuaybhModel().getOption(0);
			} else {// 找到定位第i项
				HuaybhValue = (IDropDownBean) getHuaybhModel().getOption(i);
			}
		}
		return HuaybhValue;
	}

	public void setHuaybhValue(IDropDownBean Value) {
		HuaybhValue = Value;
	}

	public void setHuaybhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getHuaybhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getHuaybhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getHuaybhModels() {
		// String str = "";
		// if (MainGlobal.getProperId(getShujztModels(), getZtvalue()) == 0) {
		// str = " and z.shenhzt = 0";
		// } else {
		// str = " and z.shenhzt = 1";
		// }
		String sql = "select zm.zhillsb_id as id, zm.bianm as bianm\n"
				+ "  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z\n"
				+ " where zm.zhuanmlb_id = lb.id\n" + "   and lb.jib = 3\n"
				+ "   and y.zhilblsb_id = zm.zhillsb_id\n"
				+ "   and z.id = zm.zhillsb_id\n";
		List dropdownlist = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int id = rs.getInt(1);
					String mc = rs.getString(2);
					dropdownlist.add(new IDropDownBean(id, mc));
				}
			} else {
				dropdownlist.add(new IDropDownBean(1, "请选择"));
			}
			// rss.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		setHuaybhModel(new IDropDownModel(dropdownlist));
	}

}
