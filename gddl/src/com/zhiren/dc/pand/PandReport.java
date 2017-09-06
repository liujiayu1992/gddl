//2008-10-12 chh 
//修改内容 :加入电厂树，公司的可以选择电厂查看报告

package com.zhiren.dc.pand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class PandReport extends BasePage implements PageValidateListener {
	private static final int TABLE_HEADER = 2;

	private static final int LOCKED_COUNT = 13;

	private static final int YOU_SHC = 9;

	private boolean isBegin;

	private List listMeicMC = null;

	private List listMeicMiD = null;

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 报表初始设置
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

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	盘点编号下拉框
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getPandModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getPandModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setPandValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setPandModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getPandModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIPandModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIPandModels() {
		String strGongsID = "";

		strGongsID=" and dc.id= " +this.getTreeid();
		
		String sql = "select pd.id,bianm as bianm from pandb pd,vwdianc dc"+
			 " where pd.diancxxb_id=dc.id" +strGongsID
				+ " order by pd.id desc";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}


	public long getPandbID() {
		int id = -1;
		if (getPandValue() == null) {
//			JDBCcon con = new JDBCcon();
//			String sql = "select id,bianm from pandb where diancxxb_id="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//					+ " and zhuangt=0 order by id desc";
//			ResultSetList rsl = con.getResultSetList(sql);
//			if (rsl.next()) {
//				id = rsl.getInt("id");
//			}
			return id;
		}
		return getPandValue().getId();
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
		return sDate;

	}

	// submit
	public void submit(IRequestCycle cycle) {

	}

	// 报表主体
	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		JDBCcon con = new JDBCcon();
		Document doc = new Document();
		// 盘煤油情况报告
		doc.addParagraph(getPanMY(con));
		// 密度测量情况报告
		if (listMeicMC != null) {
			doc.addParagraph(getMidCL(con));
		}
		con.Close();
		return doc.getHtml();
	}

	public Paragraph getMidCL(JDBCcon con) {
		Paragraph ph = new Paragraph();
		String tableData[][] = null;
		String sql = "";
		ph.addText("<div style='height: 100px'></div>");
		if (listMeicMC != null) {
			for (int i = 0; i < listMeicMC.size(); i++) {
				String meicMC = (String) listMeicMC.get(i);
				sql = "select decode(grouping(ced),1,'加权平均',ced)as ced,round_new(avg(yangpzl),5)as yangpzl,cedjj \n"
						+ "from pandmdb,meicb where pandmdb.meicb_id=meicb.id\n"
						+ " and pandb_id="
						+ getPandbID()
						+ " and meicb.mingc='"
						+ meicMC
						+ "' \n"
						+ "group by rollup(ced,cedjj)\n"
						+ "having not (grouping(cedjj) + grouping(ced)=1)";
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.getRows() != 0) {
					tableData = new String[rsl.getRows() + 3][3];
				}
				int j = 0;
				while (rsl.next()) {
					if (rsl.getString("ced").equals("加权平均")) {
						j = j + 2; // 如果是平均数在他之前空两行
					}
					tableData[j][0] = rsl.getString("ced");
					tableData[j][1] = rsl.getString("yangpzl");
					tableData[j][2] = rsl.getString("cedjj");
					j++;
				}
				// 密度
				tableData[tableData.length - 1][0] = "密度(吨/m<sup>3</sup>)";
				tableData[tableData.length - 1][1] = (String) listMeicMiD.get(i);
				tableData[tableData.length - 1][2] = (String) listMeicMiD.get(i);
				String ArrHeader[][] = new String[2][3];
				ArrHeader[0] = new String[] { meicMC, meicMC, meicMC };
				ArrHeader[1] = new String[] { "测点", "煤样重量(t)", "测点间距(m)" };
				int ArrWidth[] = new int[] { 150, 150, 150 };
				int TitleWidth[] = new int[] { 200, 25, 25, 200 };
				Table tb = new Table(tableData, 2, 0, 1);
				Report rt = new Report();

				int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
				rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
				tb.setWidth(ArrWidth);
				tb.setHeaderData(ArrHeader);
				tb.ShowZero = false;
				tb.merge(1, 1, 1, 3);
				tb.merge(tb.rows.length - 1, 2, tb.rows.length - 1, 3);
				for (int n = 0; n < tb.cols.length; n++) {
					tb.setColAlign(n + 1, Table.ALIGN_CENTER);
				}
				ph.addText("<br>");
				rt.setBody(tb);
				if (i == 0) {
					rt.setTitle(getRiq(con).substring(0, 11) + "密度测量情况报告", TitleWidth);
					rt.setDefaultTitle(1, 2, ((Visit) getPage().getVisit()).getDiancqc(),
							Table.ALIGN_LEFT);
					rt.setDefaultTitle(3, 2, "测定日期： " + getRiq(con), Table.ALIGN_RIGHT);
				}
				ph.addText(rt.getHtml());
				ph.addText("<br>");
			}
		}

		return ph;
	}

	public Paragraph getPanMY(JDBCcon con) {
		int rows = 0;
		int zhangmkc_position = LOCKED_COUNT + 1; // 盘点时账面库存的行位置
		Paragraph ph = new Paragraph();
		// 取得存煤场所数量及信息
		String[][] meic = null;
		meic = getPandMxx(con);
		int meicCount = 0;
		if (meic != null) {
			meicCount = meic.length;
		}
		// meicCount = 15;
		// 盈亏情况分析
		int yingkFXCount = 5;
		// 参加盘点人员信息
		String[] beiz = null;
		int beizCount = 5;
		beiz = getBeizInfo(con);
		if (beiz != null) {
			beizCount = beiz.length;
		}
		// 存油信息
		String[][] youg = null;
		youg = getCunYxx(con);
		int yougCount = 0;
		if (youg != null) {
			yougCount = youg.length;
			// yougCount = 5;
		}
		String ArrHeader[][] = new String[2][8];
		ArrHeader[0] = new String[] { "收耗存情况", "收耗存情况", "盘点情况", "盘点情况", "盘点情况",
				"盘点情况", "盘油情况", "盘油情况" };
		ArrHeader[1] = new String[] { "项目", "数量", "煤场", "体积", "比重", "存煤量", "油罐",
				"存油量" };
		int ArrWidth[] = new int[] { 150, 80, 100, 50, 50, 70, 100, 80 };
		int TitleWidth[] = new int[] { 150, 120, 130, 50, 50, 70, 30, 80 };
		// 判断表格有多少行
		if ((meicCount > LOCKED_COUNT - TABLE_HEADER)
				&& (meicCount > YOU_SHC + yougCount)) {
			rows = meicCount + TABLE_HEADER + 3 + yingkFXCount + 1 + beizCount;
			zhangmkc_position = meicCount + TABLE_HEADER + 1;
		} else if ((YOU_SHC + yougCount > LOCKED_COUNT)
				&& (YOU_SHC + yougCount > meicCount + TABLE_HEADER)) {
			rows = YOU_SHC + yougCount + 3 + yingkFXCount + 1 + beizCount;
			zhangmkc_position = YOU_SHC + yougCount + 1;
		} else {
			rows = LOCKED_COUNT + 3 + yingkFXCount + 1 + beizCount;
		}
		Table tb = new Table(rows, ArrHeader[0].length);
		Report rt = new Report();
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		tb.setWidth(ArrWidth);
		tb.setHeaderData(ArrHeader);
		tb.ShowZero = false;
		tb.setRowHeight(30);
		
		tb.merge(1, 1, 1, 2);
		tb.merge(1, 3, 1, 6);
		tb.merge(1, 7, 1, 8);
		// 项目信息
		tb.setCellValue(3, 1, "月初账面库存");
		tb.setCellValue(4, 1, "本月进煤量");
		tb.setCellValue(5, 1, "耗煤合计");
		tb.setCellValue(6, 1, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电耗煤");
		tb.setCellValue(7, 1, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热耗煤");
		tb.setCellValue(8, 1, "非生产用煤");
		tb.setCellValue(9, 1, "其他用");
		tb.setCellValue(10, 1, "调出量");
		tb.setCellValue(11, 1, "储存损耗");
		tb.setCellValue(12, 1, "运输损耗");
		tb.setCellValue(13, 1, "水分差调整");
		for (int i = 0; i < (zhangmkc_position - LOCKED_COUNT - 1); i++) {
			tb.setCellValue(LOCKED_COUNT + i + 1, 1, "");
		}
		tb.setCellValue(zhangmkc_position, 1, "盘点时账面库存");
		tb.setCellValue(zhangmkc_position + 1, 1, "盘盈(+)或盘亏(-)");
		// 盈亏分析内容
		int fenxRow = zhangmkc_position + 2;
		tb.setCellValue(fenxRow, 1, "盈亏情况分析：");
		tb.mergeRow(fenxRow);
		tb.merge(fenxRow + 1, 1, fenxRow + yingkFXCount, 1);
		tb.merge(fenxRow + 1, 2, fenxRow + yingkFXCount, 8);
		tb.setRowHeight(fenxRow + 1, 120);
		tb.setCellVAlign(fenxRow + 1, 1, Table.VALIGN_TOP);
		// tb.merge(zhangmkc_position + 3, 1, zhangmkc_position + 2 +
		// yingkFXCount , 1);
		// tb.merge(zhangmkc_position + 3, 2, zhangmkc_position + 2 +
		// yingkFXCount , 8);
		// tb.mergeRow(iRow)
		// 备注说明内容
		int beizRow = zhangmkc_position + 2 + yingkFXCount + 1;
		tb.merge(beizRow + 1, 1, beizRow + beizCount, 8);
		tb.setCellValue(beizRow, 1, "备注说明：");
		tb.mergeRow(beizRow);
		String s = "";
		if (beiz != null) {
			for (int i = 0; i < beizCount; i++) {
				s += beiz[i] + "<br>";
			}
			tb.setCellValue(beizRow + 1, 1, s);
		}
		tb.setRowHeight(beizRow + 1, 120);
		tb.setCellVAlign(beizRow + 1, 1, Table.VALIGN_TOP);
		tb.merge(zhangmkc_position, 2, zhangmkc_position, 8);
		tb.merge(zhangmkc_position + 1, 2, zhangmkc_position + 1, 8);
		// 数量信息
		double[] shul = null;
		shul = getShul(con);
		if (shul != null) {
			if ((zhangmkc_position + 1 - TABLE_HEADER) == shul.length) {
				for (int i = 0; i < shul.length; i++) {
					tb.setCellValue(i + 3, 2, String.valueOf(Math.round(shul[i])));
				}
			} else {
				int j = 0;
				for (int i = 0; i < meicCount + 2; i++) {
					if (i < shul.length - 2) {
						tb.setCellValue(i + 3, 2, String.valueOf(Math.round(shul[j++])));
					} else if (i < meicCount) {
						continue;
					} else {
						tb.setCellValue(i + 3, 2, String.valueOf(Math.round(shul[j++])));
					}
				}
			}
		}
		// 煤场信息
		if (meicCount != 0) {
			int meicRow = 3;
			for (int i = 0; i < meic.length; i++) {
				tb.setCellValue(meicRow + i, 3, meic[i][0]);
				tb.setCellValue(meicRow + i, 4, meic[i][1]);
				tb.setCellValue(meicRow + i, 5, meic[i][2]);
				tb.setCellValue(meicRow + i, 6, meic[i][3]);
			}
		}
		// 盘点油信息
		double[] shouhcy = null;
		shouhcy = getShouHCY(con);
		tb.setCellValue(3, 7, "月初结存量");
		tb.setCellValue(4, 7, "本月进油量");
		tb.setCellValue(5, 7, "发电耗");
		tb.setCellValue(6, 7, "供热耗");
		tb.setCellValue(7, 7, "其他耗");
		tb.setCellValue(8, 7, "盘点时账面存油");
		tb.setCellValue(9, 7, "盘点存油合计");
		if (shouhcy != null) {
			for (int i = 0; i < shouhcy.length; i++) {
				tb.setCellValue(3 + i, 8, String.valueOf(Math.round(shouhcy[i])));
			}
		}
		for (int i = 0; i < yougCount; i++) {
			tb.setCellValue(10 + i, 7, youg[i][0]);
			tb.setCellValue(10 + i, 8, youg[i][1]);
		}
		rt.setBody(tb);
		rt.setTitle(getRiq(con).substring(0, 11) + "盘煤（油）情况报告", TitleWidth);
		rt.setDefaultTitle(1, 2, "电厂名称："
				+ ((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, "报送日期： " + getRiq(con), Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 2, "单位：吨", Table.ALIGN_RIGHT);
		rt.createDefautlFooter(TitleWidth);
		rt.setDefautlFooter(7, 2, "制表：", Table.ALIGN_LEFT);
		tb.setColAlign(2, Table.ALIGN_RIGHT);
		tb.setColAlign(4, Table.ALIGN_RIGHT);
		tb.setColAlign(5, Table.ALIGN_RIGHT);
		tb.setColAlign(6, Table.ALIGN_RIGHT);
		tb.setColAlign(8, Table.ALIGN_RIGHT);
		tb.setCellAlign(zhangmkc_position, 2, Table.ALIGN_CENTER);
		tb.setCellAlign(zhangmkc_position + 1, 2, Table.ALIGN_CENTER);
		ph.addText(rt.getAllPagesHtml());
		return ph;
	}

	private double[] getShouHCY(JDBCcon con) {
		double[] shouhcy = null;
		String sql = "select (zhangmkc-(benyjy-fadh-gongrh-qity))qickc,benyjy,fadh,gongrh,qity,"
				+ "zhangmkc,py.cunyl as cunyhj from pandzmy,\n"
				+ "(select sum(cunyl)as cunyl from pandyb where pandb_id="
				+ getPandbID() + ")py \n" + " where pandb_id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			int columnCount = rsl.getColumnCount();
			shouhcy = new double[columnCount];
			for (int i = 0; i < columnCount; i++) {
				shouhcy[i] = rsl.getDouble(i);
			}
		}
		return shouhcy;
	}

	private String[][] getCunYxx(JDBCcon con) {
		String[][] youg = null;
		String sql = "select yougb.mingc,cunyl from pandyb,yougb where pandyb.yougb_id=yougb.id \n"
				+ "and pandb_id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() != 0) {
			youg = new String[rsl.getRows()][2];
		}
		int i = 0;
		while (rsl.next()) {
			youg[i][0] = rsl.getString("mingc");
			youg[i][1] = String.valueOf(Math.round(rsl.getDouble("cunyl")));
			i++;
		}
		return youg;
	}

	private String[] getBeizInfo(JDBCcon con) {
		String[] beiz = null;
		String sql = "select bum,reny,zhiz from pandbmryzzb where pandb_id="
				+ getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() != 0) {
			beiz = new String[rsl.getRows()];
		}
		int i = 0;
		while (rsl.next()) {
			beiz[i++] = "部门：" + rsl.getString("bum") + "," + "人员："
					+ rsl.getString("reny") + "," + "职责：" + rsl.getString("zhiz");
		}
		return beiz;
	}

	private String[][] getPandMxx(JDBCcon con) {
		String[][] meic = null;
		String sql = "select ('0'||rownum) as xhu,meicb.mingc,pandtjb.tij,mid,cunml\n"
				+ "from pandtjb,meicb\n "
				+ "where pandtjb.meicb_id=meicb.id and pandb_id="
				+ getPandbID()
				+ "\n"
				+ "union\n"
				+ "select ('1'||rownum) as xuh,pandcmwz.mingc,0 as tij,0 as mid,cunml\n"
				+ "from pandwzcmb,pandcmwz \n"
				+ "where pandwzcmb.pandcmwz_id=pandcmwz.id and pandb_id="
				+ getPandbID() + "\n";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() != 0) {
			meic = new String[rsl.getRows()][4];
			listMeicMC = new ArrayList();
			listMeicMiD = new ArrayList();
		}
		int i = 0;
		while (rsl.next()) {
			if (rsl.getString("xhu").startsWith("0")) {
				listMeicMC.add(rsl.getString("mingc"));
				listMeicMiD.add(rsl.getString("mid"));
			}
			meic[i][0] = rsl.getString("mingc");
			meic[i][1] = String.valueOf(Math.round(rsl.getDouble("tij")));
			meic[i][2] = String.valueOf(rsl.getDouble("mid"));
			meic[i][3] = String.valueOf(Math.round(rsl.getDouble("cunml")));
			i++;
		}
		return meic;
	}

	private double[] getShul(JDBCcon con) {
		double[] shul = null;
		String sql = "select (zhangmkc-(benyjm-fadh-gongrh-feiscy-qity-diaocl-cuns-yuns+shuifc))as shangykc,\n"
				+ " benyjm,(fadh+gongrh)as haomhj,fadh,gongrh,feiscy,qity,\n"
				+ " diaocl,cuns,yuns,shuifc,zhangmkc,panyk from pandzmm"
				+ " where pandb_id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			int columnCount = rsl.getColumnCount();
			shul = new double[columnCount];
			for (int i = 0; i < columnCount; i++) {
				shul[i] = rsl.getDouble(i);
			}
		}
		return shul;
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("盘点编码："));
		ComboBox combPandPM = new ComboBox();
		combPandPM.setWidth(150);
		combPandPM.setTransform("PandDropDown");
		combPandPM.setLazyRender(true);
		tb1.addField(combPandPM);
		tb1.addText(new ToolbarText("-"));
		
		
		ToolbarButton button = new ToolbarButton(null, "刷新",
				"function() {document.forms[0].submit();}");
		button.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(button);
		setToolbar(tb1);
	}

	// 页面初始设置
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
			this.setTreeid(null);
			this.getTree();
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
		}
		
		if (blnDiancChange){
			blnDiancChange=false;
			setPandModel(null);
			setPandValue(null);
		}
		isBegin = true;
		getToolBars();
	}

	protected void initialize() {
		super.initialize();
		listMeicMC = null;
		listMeicMiD = null;
		setMsg("");
	}

	// 页面判定方法
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
	
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
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

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
	}
	
	private boolean blnDiancChange=false;
	private String treeid;

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		String strDiancID=((Visit) getPage().getVisit()).getString2();
		if (treeid==null){
			blnDiancChange=true;
		} else if(!treeid.equals(strDiancID)){
			blnDiancChange=true;
		}
		
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
}
