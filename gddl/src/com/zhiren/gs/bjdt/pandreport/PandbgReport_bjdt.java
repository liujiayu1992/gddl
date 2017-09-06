package com.zhiren.gs.bjdt.pandreport;

import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.CustomDate;
import com.zhiren.common.DateUtil;
import com.zhiren.common.FileUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class PandbgReport_bjdt extends BasePage {

	public boolean getRaw() {
		return true;
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// 盘点信息：序号、煤场名称（包括汇总信息：盘点实存）、存煤量、盘点体积、盘点密度
	private String[][] getPandMxx(JDBCcon con) {
		String[][] meic = null;
		String sql = "select nvl(xuh,0) as xuh,decode(grouping(mingc),1,'盘点实存',mingc) as mingc,Round_new(sum(nvl(cunml,0)),0) as cunml,nvl(tij,0) as tij,nvl(mid,0) as mid from ( \n"
				+ " select ('0'||rownum) as xuh,meicb.mingc,Round_new(nvl(cunml,0),0) as cunml,nvl(pandtjb.tij,0) as tij,nvl(mid,0) as mid \n"
				+ " from pandtjb,meicb\n"
				+ " where pandtjb.meicb_id=meicb.id and pandb_id="
				+ getPandbID()
				+ "\n"
				+ " order by mingc ) group by rollup((xuh,mingc,tij,mid))";
		ResultSetList rsl = con.getResultSetList(sql);
		int rows = 0;
		if (rsl.getRows() != 0) {
			rows = rsl.getRows();
			meic = new String[rows][4];
		}
		int i = 0;
		while (rsl.next()) {
			if (rsl.getString("xuh").startsWith("0")) {
			}
			meic[i][0] = rsl.getString("mingc");
			meic[i][1] = rsl.getString("cunml");
			meic[i][2] = rsl.getString("tij");
			meic[i][3] = rsl.getString("mid");
			i++;
		}
		rsl.close();
//		con.Close();
		return meic;
	}

	// 其他存煤信息：序号、煤场名称（包括汇总信息：盘点实存）、存煤量、盘点体积、盘点密度
	private String[][] getQitcm(JDBCcon con) {
		String[][] meic = null;
		String sql = "select nvl(xuh,0) as xuh,decode(grouping(mingc),1,'盘点实存',mingc) as mingc,Round_new(sum(nvl(cunml,0)),0) as cunml,nvl(tij,0) as tij,nvl(mid,0) as mid from ( \n"
				+ " select ('1'||rownum) as xuh,pandcmwz.mingc,Round_new(nvl(cunml,0),0) as cunml,0 as tij,0 as mid \n"
				+ " from pandwzcmb,pandcmwz \n"
				+ " where pandwzcmb.pandcmwz_id=pandcmwz.id and pandb_id="
				+ getPandbID()
				+ "\n"
				+ ") group by rollup((xuh,mingc,tij,mid)) order by xuh desc, mingc ";
		ResultSetList rsl = con.getResultSetList(sql);
		int rows = 0;
		if (rsl.getRows() != 0) {
			rows = rsl.getRows();
			meic = new String[rows][4];
		}
		int i = 0;
		while (rsl.next()) {
			if (rsl.getString("xuh").startsWith("0")) {
			}
			meic[i][0] = rsl.getString("mingc");
			meic[i][1] = rsl.getString("cunml");
			meic[i][2] = rsl.getString("tij");
			meic[i][3] = rsl.getString("mid");
			i++;
		}
		rsl.close();
//		con.Close();
		return meic;
	}

	// 帐面情况:上月库存、本月进煤、耗煤合计、发电耗、供热耗、其它用、存损、帐面库存、盘点后收煤、盘点后耗煤、盘盈亏
	private String[] getZhangmqk(JDBCcon con) {

		String sql = null;
		sql = "select Round_new(nvl(zhangmkc, 0) - (nvl(benyjm, 0) - nvl(fadh, 0) - nvl(gongrh, 0) - nvl(feiscy, 0) - nvl(qity, 0) - nvl(diaocl, 0) - nvl(cuns, 0) - nvl(yuns, 0) + nvl(shuifc, 0)),0) as shangykc,\n"
				+ "Round_new(nvl(benyjm, 0), 0) as benyjm,\n"
				+ "Round_new(nvl(fadh, 0) + nvl(gongrh, 0) + nvl(qity, 0) + nvl(cuns, 0), 0) as haomhj,\n"
				+ "Round_new(nvl(fadh, 0), 0) as fadh,\n"
				+ "Round_new(nvl(gongrh, 0), 0) as gongrh,\n"
				+ "Round_new(nvl(qity, 0), 0) as qity,\n"
				+ "Round_new(nvl(cuns, 0), 0) as cuns,\n"
				+ "Round_new(nvl(zhangmkc, 0), 0) as zhangmkc,\n"
				+ "Round_new(nvl(pandhsm, 0), 0) as pandhsm,\n"
				+ "Round_new(nvl(pandhhm, 0), 0) as pandhhm,\n"
				+ "Round_new(nvl(panyk, 0), 0) as panyk\n"
				+ "from pandzmm\n"
				+ "where pandb_id = " + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		String[] strZhangm = new String[11];
		strZhangm = new String[] { "", "", "", "", "", "", "", "", "", "", "" };
		if (rsl.next()) {
			// int columnCount = rsl.getColumnCount();
			for (int i = 0; i < 11; i++) {
				strZhangm[i] = rsl.getString(i);
			}
		}
		rsl.close();
		return strZhangm;
	}

	// 获取盘点部门、人员、职责信息
	private Map getBumry(JDBCcon con) {
		String bum = null;
		String reny = null;
		Map bummap = new HashMap();
		try {
			String sql = "select z.id,z.bum,z.reny,z.zhiz from pandbmryzzb z,pandb p where z.pandb_id = p.id and p.id="
					+ getPandbID() + " order by bum,reny";
			ResultSetList rsl = con.getResultSetList(sql);
			while (rsl.next()) {
				bum = rsl.getString("bum");
				reny = rsl.getString("reny");
				if (bummap.get(bum) == null) {
					bummap.put(bum, reny);
				} else {
					bummap.put(bum, bummap.get(bum).toString() + "," + reny);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bummap;

	}

	// 获取盘点备注和意见
	private String[] getQit(JDBCcon con) {
		String[] pandyj = new String[2];
		pandyj = new String[] { "", "" };
		String sql = "select beiz,pandyj from pandb where id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			for (int i = 0; i < 2; i++) {
				pandyj[i] = rsl.getString(i);
			}
		}
		rsl.close();
		return pandyj;
	}

	public String pandgbReport() {
	
//		((Visit)this.getPage().getVisit()).setDiancxxb_id(202);
			String strPandsj = "";
			String strPandrq = "";
			long pandID = -1;
			int pageRows = 30;// 设置一页有多少行
			if (getPandsjValue() == null) {// 把请选择去掉,有时候有空的时候,反回
				setPrintTable("");
				return "";
			} else {
				pandID = getPandsjValue().getId();
			}
			JDBCcon con = new JDBCcon();
			CustomDate custom = new CustomDate();
			StringBuffer buf = new StringBuffer();
			StringBuffer strBuf = new StringBuffer();
			String diancm = "";// 电厂名称
			String time = "";
			String sjsql = "select to_char(p.riq,'yyyy-mm-dd HH24:mi:ss') as riq  from  pandb p where p.id="
					+ pandID;
			ResultSetList sjrs = con.getResultSetList(sjsql);

			if (sjrs.next()) {
				time = custom.FormatDate(custom.getDate(sjrs.getString("riq"),
						"yyyy-MM-dd HH:mm:ss"), "yyyy年MM月dd日 HH时mm分ss秒");
				strPandrq = custom.FormatDate(custom.getDate(sjrs.getString("riq"),
						"yyyy-MM-dd"), "yyyy年MM月dd日");
				strPandsj = "to_date('" + sjrs.getString("riq")
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
			Report rt = new Report();
			List list = new ArrayList();
			String[] strlist = { "", "", "", "", "", "", "", "", "", "" };
			addList(1, list, strlist);
			addList(1, list, new String[] {
					"<b>盘点编码：" + getPandsjValue().getValue() + "</b>", "", "", "",
					"", "", "", "", "", "" });
			addList(7, list, strlist);
			int[] ArrWidth = { 120, 70, 120, 70, 70, 70, 120, 70, 120, 70 };
			buf
					.append("SELECT QUANC FROM DIANCXXB WHERE ID=(select diancxxb_id from pandb where id ="
							+ getPandbID() + ")");
			ResultSetList rs = con.getResultSetList(buf.toString());

			if (rs.next()) {
				list.add(new String[10]);//到此十行
				diancm = rs.getString("QUANC");
				((String[]) list.get(9))[0] = "<b>" + diancm
						+ "<br><br>盘&nbsp;&nbsp;点&nbsp;&nbsp;报&nbsp;&nbsp;告</b>";//修改第十行
				strBuf.append("10").append(",");
			}
			rs.close();
			addList(8, list, strlist);// 加入1个空行
			addOne(strBuf, list, 0, "");
			addOne(strBuf, list, 0, "<b>盘点日期：" + strPandrq + "</b>");//20行
			addList(1, list, strlist);

			if (list.size() > pageRows) {// 如果list的size>43就会自动截取
				list = list.subList(0, pageRows - 1);
			} else {
				addList(pageRows - list.size(), list, strlist);
			}
			// 上面是设置第一页的报表，--------------------从下面开始要设置第二页的报表------------------------

			int zhuNum = 0;
			int PanmxxRowCount = 13;
			String strZhangm[] = getZhangmqk(con);
			String strMeicxx[][] = getPandMxx(con);
			String strQitcm[][] = getQitcm(con);
			// String strRenyxx[][] = getRenyInfo(con);
			Map bumryMap = getBumry(con);

			int BumryxxRowCount = 5;

			String strQit[] = getQit(con);
			String pandbz = "";
			String pandyj = "";
			pandbz = strQit[0];
			pandyj = strQit[1];

			addList(1, list, strlist);
			if (time != null && !time.equals("")) {
				addOne(strBuf, list, 0, "<b>" + time.substring(0, 8)
						+ "燃料库存盘点报告</b>");
			} else {
				addOne(strBuf, list, 0, "<b>"
						+ getPandsjValue().getValue().substring(0, 4) + "年"
						+ getPandsjValue().getValue().substring(4, 6)
						+ "月燃料库存盘点报告</b>");
			}
			addList(1, list, new String[] { "电厂名称:" + diancm, "", "", "", "", "",
					"", "", "", "" });
			addList(1, list, new String[] { "<b>帐面情况(全月)</b>", "", "<b>盘点情况</b>",
					"", "", "", "<b>盘煤简要分析与说明", "", "", "" });
			addList(1, list, new String[] { "项目", "数量<br>（T）", "存煤位置", "数量<br>（T）",
					"体积<br>（m<sup>3</sup>）", "比重<br>（T/m<sup>3</sup>）",
					"&nbsp;&nbsp;&nbsp;&nbsp;" + pandbz, "", "", "" });
			buf.setLength(0);// 清空buf

			int RowCount = 0;
			if (strMeicxx != null && strQitcm != null) {
				RowCount = strMeicxx.length + strQitcm.length;
			} else if (strMeicxx != null && strQitcm == null) {
				RowCount = strMeicxx.length;
			} else if (strMeicxx == null && strQitcm != null) {
				RowCount = strQitcm.length;
			}

			if (RowCount >= 13) {
				PanmxxRowCount = RowCount + 1;
			} else {
				PanmxxRowCount = 13;
			}

			String strPanmxx[][] = new String[PanmxxRowCount][10];
			for (int i = 0; i < strPanmxx.length; i++) {
				for (int j = 0; j < strPanmxx[i].length; j++) {
					strPanmxx[i][j] = "";
				}
			}

			strPanmxx[0][0] = "月初库存";
			strPanmxx[1][0] = "实收煤量";
			strPanmxx[2][0] = "合计耗煤";
			strPanmxx[3][0] = "&nbsp;&nbsp;发电耗煤";
			strPanmxx[4][0] = "&nbsp;&nbsp;供热耗煤";
			strPanmxx[5][0] = "&nbsp;&nbsp;其它用";
			strPanmxx[6][0] = "&nbsp;&nbsp;储存损耗";

			strPanmxx[PanmxxRowCount - 5][0] = "帐面存煤";
			strPanmxx[PanmxxRowCount - 4][0] = "盘点后收煤";
			strPanmxx[PanmxxRowCount - 3][0] = "盘点后耗煤";
			// strPanmxx[PanmxxRowCount - 2][0] = "盘点实存";
			strPanmxx[PanmxxRowCount - 1][0] = "月末实存";

			strPanmxx[PanmxxRowCount - 1][2] = "盘点盈亏<br>（±）";

			strPanmxx[PanmxxRowCount - 1][4] = "盘点误差<br>（%）";

			for (int i = 0; i < strZhangm.length - 4; i++) {
				strPanmxx[i][1] = strZhangm[i];
			}
			// 帐面存煤
			strPanmxx[PanmxxRowCount - 5][1] = strZhangm[strZhangm.length - 4];
			// 盘点后收煤
			strPanmxx[PanmxxRowCount - 4][1] = strZhangm[strZhangm.length - 3];
			// 盘点后耗煤
			strPanmxx[PanmxxRowCount - 3][1] = strZhangm[strZhangm.length - 2];
			// // 盘点盈亏
			// strPanmxx[PanmxxRowCount - 1][3] = strZhangm[strZhangm.length - 1];

			if (strMeicxx != null) {
				// 所有煤场信息
				for (int i = 0; i < strMeicxx.length - 1; i++) {
					strPanmxx[i][2] = strMeicxx[i][0];
					strPanmxx[i][3] = strMeicxx[i][1];
					strPanmxx[i][4] = strMeicxx[i][2];
					strPanmxx[i][5] = strMeicxx[i][3];
				}
			}

			if (strQitcm != null) {
				// 其他存煤信息
				for (int i = 0; i < strQitcm.length - 1; i++) {
					strPanmxx[PanmxxRowCount - 2 - i][2] = strQitcm[i][0];
					strPanmxx[PanmxxRowCount - 2 - i][3] = strQitcm[i][1];
					strPanmxx[PanmxxRowCount - 2 - i][4] = strQitcm[i][2];
					strPanmxx[PanmxxRowCount - 2 - i][5] = strQitcm[i][3];
				}
			}

			if (strMeicxx != null) {
				// 盘点实存
				strPanmxx[PanmxxRowCount - 2][0] = strMeicxx[strMeicxx.length - 1][0];
				double dbQitcm = 0;
				if(strQitcm != null){
					dbQitcm = Double.parseDouble(strQitcm[strQitcm.length - 1][1]);
				}
				strPanmxx[PanmxxRowCount - 2][1] = String.valueOf(new java.text.DecimalFormat("#")
													.format(Double.parseDouble(strMeicxx[strMeicxx.length - 1][1])+ dbQitcm));

				// 月末实存 = 盘点实存+盘点后收煤-盘点后耗煤
				double pandsc = Double	.parseDouble(strPanmxx[PanmxxRowCount - 2][1]);//修正盘点实存取值
				double pandhsm = Double
						.parseDouble(strZhangm[strZhangm.length - 3]);
				double pandhhm = Double
						.parseDouble(strZhangm[strZhangm.length - 2]);
				strPanmxx[PanmxxRowCount - 1][1] = String
						.valueOf(new java.text.DecimalFormat("#").format(pandsc	+ pandhsm - pandhhm));

				// 盘点盈亏 = 月末实存 - 帐面存煤
				strPanmxx[PanmxxRowCount - 1][3] = String
						.valueOf(new java.text.DecimalFormat("#")
								.format((pandsc + pandhsm - pandhhm)
										- Double
												.parseDouble(strZhangm[strZhangm.length - 4])));
			}

			// 盘点误差 = 盘点盈亏/帐面存煤*100(保留两位小数)
			if (strZhangm[strZhangm.length - 4].equals("0")
					|| strZhangm[strZhangm.length - 4].equals("")
					|| strZhangm[strZhangm.length - 4].equals(null)) {
				strPanmxx[PanmxxRowCount - 1][5] = "";
			} else {
				strPanmxx[PanmxxRowCount - 1][5] = String
						.valueOf(new java.text.DecimalFormat("#.00")
								.format(Double
										.parseDouble(NnvlStr(strPanmxx[PanmxxRowCount - 1][3]))
										/ Double
												.parseDouble(strZhangm[strZhangm.length - 4])
										* 100));
			}

			for (int i = 0; i < strPanmxx.length; i++) {
				addList(1, list, strPanmxx[i]);
			}

			// 盈亏分析内容
			zhuNum = list.size();
			addList(1, list, new String[] { "<b>参加盘点部门和人员", "", "", "", "",
					"<b>参加盘煤人员意见：", "", "", "", "" });

			String strBumryxx[][] = new String[BumryxxRowCount][10];
			for (int i = 0; i < strBumryxx.length; i++) {
				for (int j = 0; j < strBumryxx[i].length; j++) {
					strBumryxx[i][j] = "";
				}
			}

			if (bumryMap != null) {
				Iterator it = bumryMap.entrySet().iterator();
				int i = 0;
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object bums = entry.getKey();
					Object renys = entry.getValue();
					String bum = (String) bums;
					String reny = (String) renys;

					if (i == 0) {

						addList(1, list,
								new String[] { bum, "", reny, "", "",
										"&nbsp;&nbsp;&nbsp;&nbsp;" + pandyj, "",
										"", "", "" });
					} else {

						addList(1, list, new String[] { bum, "", reny, "", "", "",
								"", "", "", "" });
					}
					i++;
				}
				if (bumryMap.size() >= 5) {
					BumryxxRowCount = bumryMap.size() + 1;
					addList(1, list, strlist);
				} else {
					BumryxxRowCount = 5;
					int h = BumryxxRowCount - bumryMap.size();

					addList(1, list, new String[] { "", "", "", "", "",
							"&nbsp;&nbsp;&nbsp;&nbsp;" + pandyj, "", "", "", "" });
					addList(h - 1, list, strlist);
				}
			}

			addList(1, list, new String[] { "主管：", "", "", "审核：", "", "", "",
					"制表：", "", "" });

			if (list.size() > 2 * pageRows) {// 如果list的size>2*43就会自动截取
				list = list.subList(0, 2 * pageRows - 1);
			} else {
				addList(2 * pageRows - list.size(), list, strlist);
			}

			// 上面是设置第二页的报表，--------------------从下面开始要设置第三页的报表------------------------
			addList(1, list, strlist);

			addOne(strBuf, list, 0, "<b>附表一 密度测量报告</b>");
			addList(1, list, new String[] { "电厂名称:" + diancm, "", "", "", "", "",
					"测定时间：" + time, "", "", "" });
//			addList(1, list, new String[] { midTable(diancm, strPandsj), "", "",
//					"", "", "", "", "", "", "" });
			  int zhanyyshu=0,yespage=0;
			  zhanyyshu =yespage=  midTable(diancm,strPandsj,list,pageRows);//构造并返回构造页数
			  
//			if (list.size() > 3 * pageRows) {// 如果list的size>3*43就会自动截取
//				list = list.subList(0, 3 * pageRows - 1);
//			} else {
//				addList(3 * pageRows - list.size(), list, strlist);
//			}
			// 上面是设置第三页的报表，--------------------从下面开始要设置第四页以后的报表------------------------
			buf.setLength(0);
			addList(1, list, strlist);

			addOne(strBuf, list, 0, "<b>附表二 盘煤图形</b>");
			addList(1, list, new String[] { "电厂名称:" + diancm, "", "", "", "", "",
					"", "", "", "" });
			addList(1, list, strlist);
			buf
					.append("select m.mingc as meic,t.id,t.diancxxb_id,t.riq,t.xuh,t.bianm,t.mingc,t.mokmc,t.leix,t.beiz \n"
							+ "from pandtjb pt, meicb m, tupccb t\n"
							+ " where bianm in \n"
							+ "		(select pt.id from pandtjb pt,pandb p where pt.pandb_id = p.id and p.id = "
							+ getPandbID()
							+ ")\n"
							+ "and t.bianm = pt.id\n"
							+ "and pt.meicb_id = m.id\n"
							+ "order by meic,xuh,mingc");

			// buf.append(changbId(true));
			ResultSetList mapRs = con.getResultSetList(buf.toString());
			String ddd = "";
			if (mapRs.getRows() == 0) {// 没有数据就添加一个空行
				addList(1, list, strlist);
			}
			int  tupbegin=zhanyyshu;
			for (int i = 0; mapRs.next(); i++) {
				String strPanmt = mapRs.getString("bianm") + "_" + mapRs.getInt("xuh")
						+ FileUtil.getSuffix(mapRs.getString("mingc"));
				String diancId = mapRs.getString("diancxxb_id");
				String mok = mapRs.getString("mokmc");
				String meic = mapRs.getString("meic");

				this.setdiancId(diancId);
				this.setMk(mok);
				this.setMeic(meic);
				addList(1, list, strlist);
				if (strPanmt != null && !strPanmt.equals("")) {// 图片不为空
					addList(1, list, new String[] {
							getMeic() + "_" + mapRs.getInt("xuh"), "", "", "", "",
							"", "", "", "", "" });
					ddd = ddd + list.size() + ",";

					addList(1, list, new String[] {
							"",
							"",
							"",
							"",
							"",
							"<p><img name='Picture" + i + "' " + "src='"
									+ getImagePath(strPanmt) + "' /></p>", "", "",
							"", "" });
				}
//				图片开始前页数
				
				if (list.size() > ++zhanyyshu * pageRows) {// 如果list的size>4*43就会自动截取
					list = list.subList(0, zhanyyshu * pageRows - 1);
				} else {
					addList(zhanyyshu * pageRows - list.size(), list, strlist);
				}
			}
		
			mapRs.close();

			// ----------------------------------------上面是所有报表----------------------------------------

			String[][] strList = new String[list.size()][10];
			for (int i = 0; i < strList.length; i++) {
				strList[i] = (String[]) list.get(i);
				// System.out.println(i);
			}

			rt.setBody(new Table(strList, 0, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(pageRows);

			// 从下面开始设置表上的边框

			rt.body.setBorderNone();
			for (int i = 1; i <= 10; i++) {
				rt.body.setColCells(i, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setColCells(i, Table.PER_BORDER_RIGHT, 0);
			}

			// 设置对齐方式
			rt.body.setColAlign(1, Table.ALIGN_LEFT);
			rt.body.setColAlign(3, Table.ALIGN_LEFT);
			rt.body.setColAlign(7, Table.ALIGN_LEFT);
			rt.body.setColAlign(9, Table.ALIGN_LEFT);
			rt.body.setRowCells(pageRows + 3, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.body.setCellAlign(pageRows + 3, 9, Table.ALIGN_RIGHT);

			

			rt.body.setRowCells(pageRows + 4, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setRowCells(pageRows + 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCellAlign(pageRows + 5, 7, Table.ALIGN_LEFT);
			rt.body.setCells(pageRows + 5, 7, zhuNum, 10, Table.PER_VALIGN,
					Table.VALIGN_TOP);
			rt.body.setCellAlign(zhuNum, 3, Table.ALIGN_CENTER);
			rt.body.setCellAlign(zhuNum, 5, Table.ALIGN_CENTER);
             
			for (int i=2;i<yespage;i++){
				if(i>2)
				rt.body.setCellAlign(i * pageRows + 1, 1, Table.ALIGN_CENTER);
				if(i==2){
					   rt.body.setCellAlign(i * pageRows + 3, 1, Table.ALIGN_CENTER);
					   rt.body.setCellAlign(i * pageRows + 4, 1, Table.ALIGN_CENTER);
				}
			}
           
			rt.body.setCellAlign(zhuNum + 1, 1, Table.ALIGN_CENTER);

			// 设置合并
			rt.body.mergeCell(pageRows + 3, 1, pageRows + 3, 4);
			rt.body.mergeCell(pageRows + 3, 5, pageRows + 3, 8);
			rt.body.mergeCell(pageRows + 3, 9, pageRows + 3, 10);
			rt.body.mergeCell(pageRows + 4, 1, pageRows + 4, 2);
			rt.body.mergeCell(pageRows + 4, 3, pageRows + 4, 6);
			rt.body.mergeCell(pageRows + 4, 7, pageRows + 4, 10);
			for (int i=2;i<yespage;i++){
				if(i==2){
					rt.body.mergeCell(i * pageRows + 3, 1, i * pageRows + 3, 4);
					rt.body.mergeCell(i * pageRows + 3, 7, i * pageRows + 3, 10);
				}
				if(i>2){
					rt.body.mergeCell(i * pageRows + 1, 1, i * pageRows + 5, 10);
				}
			}
//				rt.body.mergeCell(tupbegin * pageRows + 7, 1, tupbegin * pageRows + 20, 10);
//			System.out.println(rt.body.rows.length);
			rt.body.mergeCell(yespage * pageRows + 3, 1, yespage * pageRows + 3, 10);
                    
			// 备注内容
			rt.body.mergeCell(pageRows + 5, 7, zhuNum, 10);

			rt.body.mergeCell(zhuNum + 1, 1, zhuNum + 1, 5);
			for (int i = zhuNum + 2; i <= zhuNum + 1 + BumryxxRowCount; i++) {
				rt.body.mergeCell(i, 1, i, 2);
				rt.body.mergeCell(i, 3, i, 5);
			}

			// 盘点意见
			rt.body.mergeCell(zhuNum + 1, 6, zhuNum + 1, 10);
			rt.body.mergeCell(zhuNum + 2, 6, zhuNum + 1 + BumryxxRowCount, 10);
			rt.body.setCellBorder(zhuNum + 1, 6, 0, 2, 0, 0);
			rt.body.setCellBorder(zhuNum + 2, 6, 0, 2, 0, 2);
			rt.body.setCells(zhuNum + 2, 6, zhuNum + 1 + BumryxxRowCount, 10,
					Table.PER_VALIGN, Table.VALIGN_TOP);

			rt.body.mergeCell(2 * pageRows + 4, 1, 3 * pageRows, 10);

			// 设置边框
			rt.body.setCellBorder(pageRows + 4, 1, 2, 2, 2, 1);
			rt.body.setCellBorder(pageRows + 4, 3, 0, 2, 2, 1);
			rt.body.setCellBorder(pageRows + 4, 7, 0, 2, 2, 1);

			for (int i = pageRows + 5; i <= zhuNum; i++) {
				rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 1);
				rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 1);

				rt.body.setCellBorderLeft(i, 1, 2);
				rt.body.setCellBorderRight(i, 2, 2);
				rt.body.setCellBorderRight(i, 6, 2);

			}

			// 备注内容
			rt.body.setCellBorder(pageRows + 5, 7, 0, 2, 0, 2);

			rt.body.setRowCells(zhuNum - 1, Table.PER_BORDER_BOTTOM, 2);
			rt.body.setRowCells(zhuNum, Table.PER_BORDER_BOTTOM, 2);
			rt.body.setCellBorder(zhuNum + 1, 1, 2, 2, 0, 1);
			for (int i = zhuNum + 2; i <= zhuNum + 1 + BumryxxRowCount; i++) {
				rt.body.setCellBorder(i, 1, 2, 1, 0, 1);
				rt.body.setCellBorder(i, 3, 0, 2, 0, 1);
			}
			rt.body.setCellBorder(zhuNum + 1 + BumryxxRowCount, 1, 2, 1, 0, 2);
			rt.body.setCellBorder(zhuNum + 1 + BumryxxRowCount, 3, 0, 2, 0, 2);

			rt.body.mergeCell(2, 1, 2, 10);
			// rt.body.mergeCell(32, 1, 32, 10);
			rt.body.setRowCells(2, Table.PER_FONTSIZE, 12);

			String[] listSplit = strBuf.toString().split(",");
			
			for (int i = 0; i < listSplit.length; i++) {
				int num = Integer.parseInt(listSplit[i]);
				// 从下面开始设置字体
				rt.body.setRowCells(num, Table.PER_FONTSIZE, 22);
				rt.body.setRowCells(20, Table.PER_FONTSIZE, 12);//盘点日期哪一行
				// 从下面设置对齐方式

				// 从下面开始设置合并
				if (num < pageRows && num > 31 && num < 2 * pageRows) {
					rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt.body.setRowCells(num, Table.PER_FONTSIZE, 12);
					rt.body.mergeCell(num, 1, num, 10);
				} else if (num > 2 && num < 31 && num < pageRows
						|| num > 2 * pageRows) {
					rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt.body.mergeCell(num, 1, num, 10);
				} else if (num > pageRows && (num - pageRows) == 2) {
					rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt.body.mergeCell(num, 1, num, 10);
				}
			}
			if (!(ddd == "")) {
				String[] dddd = ddd.split(",");
				for (int i = 0; i < dddd.length; i++) {
					int num = Integer.parseInt(dddd[i]);
					rt.body.mergeCell(num, 1, num, 10);
					rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt.body.setRowCells(num, Table.PER_FONTSIZE, 22);
				}
			}

			con.Close();
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			rt.body.setRowHeight(21);
//			第一页的后9行设为行高为0
			rt.body.rows[1].height=0;
			rt.body.rows[31].height=0;
			for(int i=22;i<=30;i++){
			rt.body.rows[i].height=0;
			}
			for (int i=2;i<yespage;i++){
				if(i>2){
					for(int j=2;j<pageRows;j++){
					rt.body.rows[i*pageRows+j].height=0;
					}
				}
			}
			for (int i=tupbegin;i<zhanyyshu;i++){
				if(i==tupbegin){
					for(int j=8;j<pageRows;j++){
						rt.body.rows[i*pageRows+j].height=0;
						rt.body.rows[i*pageRows+j].hidden=true;
						             
					}
				}else{
					for(int j=6;j<pageRows;j++){
						rt.body.rows[i*pageRows+j].height=0;
						rt.body.rows[i*pageRows+j].hidden=true;
						             
					}
				}	
			}
//     System.out.println(rt.getAllPagesHtml());
			return rt.getAllPagesHtml();// ph;
		
	}

	public long getPandbID() {
		int id = -1;
		if (getPandsjValue() == null) {
			return id;
		}
		return getPandsjValue().getId();
	}

	public String getRiq(JDBCcon con) {
		String sDate = "";
		String sql = "select riq from pandb where id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			sDate = DateUtil.Formatdate("yyyy 年 MM 月 dd 日", rsl.getDate("riq"));
		}
		if (sDate.equals("")) {
			sDate = DateUtil.Formatdate("yyyy 年 MM 月 dd 日", new Date());
		}
		con.Close();
		return sDate;

	}

	private int midTable(String str, String strPandsj,List list,int pageRows) {
		StringBuffer buf = new StringBuffer();
		int i=2;// 当前页数
		JDBCcon con = new JDBCcon();
		StringBuffer bufSql = new StringBuffer();
		bufSql
				.append("SELECT m.ID,m.mingc as MEICMC FROM MEICB m,pandmdb pm where m.id = pm.meicb_id and pm.pandb_id ="
						+ getPandbID()
						+ " group by m.id,m.mingc order by meicmc");
		ResultSetList rs = con.getResultSetList(bufSql.toString());
		while (rs.next()) {
			bufSql.setLength(0);
			bufSql.append("SELECT ROWNUM AS ID,NVL(CED,'0') \n");
			bufSql.append("CED,chentmz,chentpz,YANGPZL,CEDJJ,\n" +
							" TO_NUMBER(NVL(rj.rongqtj,0)) as zhi,nvl(MID,0) MID \n" +
							" FROM pandmdb pm,pandb p ,pandtjb pt,\n");
			bufSql.append("(select p.zhi as rongqtj, p.diancxxb_id from xitxxb p where mingc = '盘点器皿容积' "+changbId(true)+") rj");
			bufSql.append(" WHERE pm.pandb_id=p.id and pt.PANDB_ID=p.id and rj.diancxxb_id=p.diancxxb_id and p.id=" + getPandbID()
					+ " and pm.meicb_id=" + rs.getLong("id")+" and pm.meicb_id=pt.meicb_id");
			bufSql.append(changbId(true));
			bufSql.append("UNION\n");
			bufSql.append("SELECT 66 AS ID,NVL('','加权平均') CED,\n");
			bufSql.append("Round_new(NVL(AVG(chentmz), 0),4) chentmz,\n");
			bufSql.append("Round_new(NVL(AVG(chentpz), 0),4) chentpz,\n");
			bufSql.append("Round_new(NVL(AVG(YANGPZL),0),4) YANGPZL, \n"
						+ "ROUND_NEW(NVL(AVG(CEDJJ),0),4) CEDJJ,\n" +
						   "ROUND_NEW(NVL(AVG(rj.rongqtj),0),4) as zhi,\n"+
						  "ROUND_NEW(NVL(AVG(MID),0),4) MID \n");
			bufSql.append("FROM pandmdb pm,pandb p ,pandtjb pt, \n" +
						"(select p.zhi as rongqtj, p.diancxxb_id from xitxxb p where mingc = '盘点器皿容积' "+changbId(true)+") rj \n" +
						"WHERE pm.pandb_id=p.id and pt.PANDB_ID=p.id and rj.diancxxb_id=p.diancxxb_id and p.id="
					+ getPandbID() + " and pm.meicb_id=" + rs.getLong("id") +" and pm.meicb_id=pt.meicb_id");
			bufSql.append(changbId(true));// 去掉后面加的group by
			ResultSetList bRs = con.getResultSetList(bufSql.toString());
			boolean flag = false;
			bufSql.setLength(0);
			String firstStr = firstTd("测点", 140, "center")
					+ middleTd(140, "测点间距(m)", "center")
					+ middleTd(140, "容器毛重(T)", "center")
					+ middleTd(140, "容器皮重(T)", "center")
					+ middleTd(140, "煤样重量(T)", "center")
					+ middleTd(140, "容器体积(m<sup>3</sup>)", "center")
					+ middleTd(140, "密度(T/m<sup>3</sup>)", "center")+ "</tr>";
			while (bRs.next()) {
				bufSql.append(firstTd(bRs.getString(1), 140, "center"));
				bufSql.append(middleTd(110, String.valueOf(bRs.getDouble(5)),
						"right"));
				bufSql.append(middleTd(110, String.valueOf(bRs.getDouble(2)),
						"right"));
				bufSql.append(middleTd(110, String.valueOf(bRs.getDouble(3)),
						"right"));
				bufSql.append(middleTd(110, String.valueOf(bRs.getDouble(4)),
						"right"));
				bufSql.append(middleTd(110, String.valueOf(bRs.getDouble(6)),
						"right")); 
				bufSql.append(middleTd(110, String.valueOf(bRs.getDouble(7)),
						"right"));

				bufSql.append("</tr>");
				flag = true;
			}
			bRs.close();
			if (flag) {
//				String sql = "SELECT nvl(AVG(MID),0) MID FROM pandtjb,pandb p"
//						+ " WHERE PANDB_ID=p.id and p.id=" + getPandbID()
//						+ " and meicb_id=" + rs.getLong("id") + changbId(true);
//				String tSql = "select zhi as rongqtj from xitxxb where mingc = '盘点器皿容积'";
//
//				ResultSetList midRs = con.getResultSetList(sql);
//				ResultSetList tjRsl = con.getResultSetList(tSql);
//
//				if (midRs.next() && tjRsl.next()) {
//					bufSql.append(lastTr(tjRsl.getString("rongqtj"), midRs
//							.getString("MID")));
//				}
//				midRs.close();
//				tjRsl.close();

				buf.append("<br>");
				buf.append(tableMid(rs.getString("MEICMC")));
				buf.append(firstStr);
				buf.append(bufSql);
				buf.append("</table>");
			}
			
			if((rs.getRow()+1)%3==0||rs.getRows()==(rs.getRow()+1)){
						this.addList(1, list, new String[] {buf.toString() , "", "","", "", "", "", "", "", "" });//开始构造
						 i++;
						if (list.size() > i * pageRows) {// 如果list的size>3*43就会自动截取
							list = list.subList(0, i * pageRows - 1);
						} else {
							String[] strlist = { "", "", "", "", "", "", "", "", "", "" };
							addList(i * pageRows - list.size(), list, strlist);
						}
//						System.out.print("--------------------------------------------------");
//						System.out.println(buf.toString());
//						System.out.print("--------------------------------------------------");
			     buf.delete(0, buf.length());
			}
		}
		rs.close();
		con.Close();
//   System.out.println(buf.toString());
//		return buf.toString();
		return i;
	}

	private String tableMid(String str) {
		StringBuffer buf = new StringBuffer();
		buf.append("<table width='700' cellpadding='0' cellspacing='0'"
				+ " style='font-family:宋体;font-size:9pt;border-left:2px "
				+ "solid rgb(0,0,0);border-top:2px solid rgb(0,0,0);"
				+ "border-right:1px solid rgb(0,0,0);border-bottom:1px "
				+ "solid rgb(0,0,0);'>");
		buf.append("<tr height='21'>\n");
		buf.append("<td width='700' colspan='7'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");
		buf.append("</tr>");
		return buf.toString();
	}

	private String firstTd(String str, int num, String align) {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr height='21'>\n");
		buf.append("<td width='" + num + "' align='" + align + "' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");

		return buf.toString();
	}

	private String middleTd(int width, String str, String align) {
		StringBuffer buf = new StringBuffer();
		buf.append("<td width='" + width + "'  align='" + align + "' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");

		return buf.toString();
	}

	private String lastTr(String tij, String mid) {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr height='21'>\n");
		buf.append("<td width='140'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>容器体积(m<sup>3</sup>)</td>\n");
		buf.append("<td width='140' align='right' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + tij + "</td>\n");
		buf.append("<td width='140'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>密度(T/m<sup>3</sup>)</td>\n");
		buf.append("<td width='350' colspan = 2 align='right' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + mid + "</td></tr>\n");

		return buf.toString();
	}

	private void addOne(StringBuffer strBuf, List list, int add, String str) {
		String[] strList = { "", "", "", "", "", "", "", "", "", "" };
		strList[add] = str;
		list.add(strList);
		strBuf.append(list.size()).append(",");
	}

	private void addList(int size, List list, String[] strlist) {
		for (int i = 0; i < size; i++) {// 用的是改变对象的引用是会改变原来的对象的方法
			list.add(strlist);
		}
	}

	private boolean blnIsBegin = false;

	private String printTable = "";

	private void setPrintTable(String str) {
		printTable = str;
	}

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		setPrintTable("");
		blnIsBegin = false;
//		try {
		if(this.getPandsjValue().getId()==-1){
			return "";
		}else{
			return pandgbReport();
		}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return "";
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			blnIsBegin = true;
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			// visit.setEditValues(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			setPandsjValue(null);
			setPandsjModel(null);
			this.setTreeid(null);
		
		}
		setMk("pandReport");
		
		if (treeChange) {
			setPandsjValue(null);
			setPandsjModel(null);
			treeChange = false;
		}
		init();
		getToolbars();
		blnIsBegin = true;
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "Form0", null,
				getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("盘点编号:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("PandsjDropDown");
		cb2.setEditable(true);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	private boolean treeChange = false;

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
				treeChange = true;
			} else {
				treeChange = false;
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称

	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getDiancmcModel() {
		if (_IDiancmcModel == null) {
			getDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	// 盘点编号下拉框
	public IDropDownBean getPandsjValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getPandsjModel().getOptionCount() > 0) {
				((Visit) getPage().getVisit())
						.setDropDownBean2((IDropDownBean) getPandsjModel()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setPandsjValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setPandsjModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getPandsjModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIPandsjModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getIPandsjModels() {
		String strGongsID = "";
//		if(getDiancTreeJib()==3){
			strGongsID = " and dc.id= " + this.getTreeid();
//		}else{
//			strGongsID = " and (dc.id= " + this.getTreeid() +" or dc.fuid= " + this.getTreeid() +" )";
//		}
		String sql = "select pd.id,bianm as bianm from pandb pd,vwdianc dc"
				+ " where pd.diancxxb_id=dc.id" + strGongsID
				+ " order by pd.bianm desc";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql,"请选择"));
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String changbId(boolean flag) {// 厂别表
		String changb = "";
		long changbb_id = -1;
		changbb_id = getDiancmcValue() == null ? ((Visit) getPage().getVisit())
				.getDiancxxb_id() : Long.parseLong(this.getTreeid());
		if (flag) {
			changb = " AND P.DIANCXXB_ID = " + changbb_id + "\n";
		} else {
			changb = "" + changbb_id;
		}
		return changb;
	}

	private String changbId(String str) {// 厂别表
		String changb = "";
		long changbb_id = getDiancmcValue() == null ? ((Visit) getPage()
				.getVisit()).getDiancxxb_id() : Long
				.parseLong(this.getTreeid());
		changb = " AND " + str.toUpperCase() + ".DIANCXXB_ID = " + changbb_id
				+ "\n";

		return changb;
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

	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getEndriqDateSelect() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setEndriqDateSelect(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	String mok = null;

	public String getMk() {
		return mok;
	}

	public void setMk(String leix) {
		mok = leix;
	}

	String meic = null;

	public String getMeic() {
		return meic;
	}

	public void setMeic(String meicmc) {
		meic = meicmc;
	}

	String diancxxbId = null;

	public String getdiancxxbId() {
//		diancxxbId=((Visit)this.getPage().getVisit()).getDiancxxb_id()+"";
		diancxxbId=this.getTreeid();
		return diancxxbId;
	}

	public void setdiancId(String diancId) {
		diancxxbId = diancId;
	}

	public IPropertySelectionModel getFileModel() {
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setFileModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public String getImagextlj() {
		String Imagelj = "D:/zhiren";
		String iSql = "";
		iSql = "select zhi from xitxxb where mingc='盘煤图路径' and zhuangt=1";
		JDBCcon con = new JDBCcon();
		ResultSetList Irsl = con.getResultSetList(iSql);
		if (Irsl.next()) {
			Imagelj = Irsl.getString("zhi");
		}
		con.Close();
		return Imagelj;
	}

	public File getImageFilePath() {
		return new File(MainGlobal.getXitsz(null, ""
				+ this.getTreeid(),
				getImagextlj() + "/" + getMk() + "/" + getdiancxxbId()));
	}

	public File getImageTmpPath() {
		return new File(MainGlobal.getWebAbsolutePath().getParentFile()
				+ "/img/tmp/" + getMk() + "/" + getdiancxxbId() + "/");
	}

	public String getImagePath(String fileName) {
		String imgPath = "#";
		if (fileName != null && !"".equals(fileName)) {
			imgPath = "img/tmp/" + getMk() + "/" + getdiancxxbId() + "/"
					+ fileName;
		}
		return imgPath;// "imgs/login/spacer.gif";
	}

	public void init() {
		JDBCcon con = new JDBCcon();
		String sql = "select t.xuh,t.bianm,p.id,t.mingc from tupccb t,pandtjb p where to_char(p.id)=t.bianm and p.pandb_id = "
				+ getPandsjValue().getId() + "";
		ResultSetList rsl = con.getResultSetList(sql);
		List fileModel = new ArrayList();
		while (rsl.next()) {
			String srcFileName = rsl.getLong("id") + "_" + rsl.getInt("xuh")
					+ FileUtil.getSuffix(rsl.getString("mingc"));
			File srcFile = new File(getImageFilePath(), srcFileName);
			try {
				fileModel.add(new IDropDownBean(rsl.getString("xuh"), rsl
						.getString("mingc")));
				FileUtil.copy(srcFile, getImageTmpPath().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setFileModel(new IDropDownModel(fileModel));
		rsl.close();
		con.Close();
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	public static String NnvlStr(String strValue){
		if (strValue==null) {
			return "0";
		}else if(strValue.equals("null")){
			return "0";
		}else if(strValue.trim().equals("")){
			return "0";
		}
		
		return strValue;
	}
	public class ITable extends Table{

		public ITable(int iRows, int iCols) {
			super(iRows, iCols);
			// TODO 自动生成构造函数存根
		}
		 public String getIHtml(String tableId) {
				
				StringBuffer sb = new StringBuffer();
				sb.append(getTableH(tableId));
				for (int i = 1; i <= getRows(); i++) {
					sb.append(getRowHtml(i));
				}
				sb.append("</table>\n");
				
				return  sb.toString();
			}
	}
}