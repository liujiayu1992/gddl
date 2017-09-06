package com.zhiren.dc.pandgd;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zhiren.common.CustomDate;
import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.FileUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * 修改： lichenji
 * 时间：2010年8月2日17:16:41
 * 描述：修改盘点报表的样式，使之符合国电要求。
 *
 */

/*
 * 修改人：ww
 * 修改时间：2010-09-17  
 * 修改内容： 1、修改测试记录数据翻倍问题
 * 			2、盘点日期取盘点开始日期
 * 			3、修改月度实际库存计算公式
 * 			4、煤场累计库存及盈亏表改从pandzmb中取数
 */
/*
 * 修改人：licj
 * 修改时间：2010-10-18  
 * 修改内容： 
 * 			增加年、月份选择下拉框，通过选择年月份查看对应编码的盘点报告
 * 			修改平均密度计算方式为加数平均
 */
/*
 * 修改人：licj
 * 修改时间：2010年10月26日  
 * 修改内容： 
 * 			对盘点数据统计汇总里月度实际库存添加了盘前后的判断。
 */
/*
 * 修改人：songy
 * 修改时间：2011年5月26日  
 * 修改内容： 
 * 			修改本期盈亏=月度实际库存-计提损耗后库存。
 */
/*
 * 作者：夏峥
 * 日期：2011-06-23
 * 描述：修正盘点查询图片不能正常生产的错误
 * 		 优化图片查询所用的SQL语句
 */
/*
 * 作者：夏峥
 * 日期：2011-06-24
 * 描述：修正meidjmdcs方法中平均密度的取值方式,变更为加权平均
 *       去除无用注释信息
 */
public class Pandcx extends BasePage {

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
	public String getId() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}
	/**
	 * 获得盘点是盘前(盘点日期处于盘煤月的下一个月)还是盘后(盘点日期处于盘煤月的当月)
	 * @param pandId
	 * @return
	 * ture:盘后
	 * false:盘前
	 */
	private boolean getPandqhzt(long pandId){
		JDBCcon con = new JDBCcon();
		boolean bl=false;
		String sql="select to_char(kaisrq,'yyyy-MM') kaisrq,to_char(jiesrq,'yyyy-MM') jiesrq from pand_gd where id="+pandId;
		ResultSetList list=con.getResultSetList(sql);
		if (list.next()) {
			if (list.getString("kaisrq").equals(list.getString("jiesrq"))) {
				bl=true;
			}else{
				bl=false;
			}
		}
		return bl;
	}

	// 合同页面报表
	public String pandgbReport() {

		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		Report rt = new Report();
		long pandID = -1;
		if (getPandsjValue() == null) {// 把请选择去掉,有时候有空的时候,反回
			setPrintTable("");
			return "";
		} else {
			pandID = getPandsjValue().getId();
		}
		String time = "";
		String pandrq = "";
		String strPandrq1="";
		String strPandrq = "";
		String piz="";
		String shenh="";
		String bianz="";
		String bianzbm="";
		String danw = "";
		String sjsql = "select riq,kaisrq,piz,shenh,bianz,bianzbm  from  pand_gd p where p.id="
				+ pandID;
		ResultSetList sjrs = con.getResultSetList(sjsql);

		if (sjrs.next()) {
			time = DateUtil.Formatdate("yyyy年MM月dd日", sjrs.getDate("riq"));

			strPandrq = DateUtil.Formatdate("yyyy年MM月", sjrs.getDate("kaisrq"));

			strPandrq1= DateUtil.Formatdate("yyyy-MM", sjrs.getDate("riq"));

			pandrq=sjrs.getString("riq");
			piz=sjrs.getString("piz");
			shenh=sjrs.getString("shenh");
			bianz=sjrs.getString("bianz");
			bianzbm=sjrs.getString("bianzbm");
		}
		// 查询单位名称
		String sql1 = "SELECT QUANC FROM DIANCXXB WHERE ID=(select diancxxb_id from pand_gd where id ="
				+ getPandbID() + ")";
		ResultSetList mingc = con.getResultSetList(sql1);
		if (mingc.next()) {
			danw = mingc.getString("QUANC");
		}
		String ArrHeader[][] = new String[15][7];
		ArrHeader[0] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[1] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[2] = new String[] { "", "燃料库存盘点报告", "燃料库存盘点报告", "燃料库存盘点报告",
				"燃料库存盘点报告", "燃料库存盘点报告", "" };
		ArrHeader[3] = new String[] { "", "", "" + strPandrq + "",
				"" + strPandrq + "", "" + strPandrq + "", "", "" };
		ArrHeader[4] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[5] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[6] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[7] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[8] = new String[] { "", "", "", "", "", "", "" };
		ArrHeader[9] = new String[] { "", "　　　单　　位：" + danw, "　　　单　　 位：" + danw,
				"　　　单　　位：" + danw, "　　　单　　位：" + danw ,
				"　　　单　　位：" + danw, "　　　单    位：" + danw };
		ArrHeader[10] = new String[] { "", "　　　批　　准："+piz, "　　　批　　准："+piz,
				"　　　批　　准："+ piz, "　　　批　　准：" + piz, "", "" };
		ArrHeader[11] = new String[] { "", "　　　审　　核："+ shenh, "　　　审　　核："+ shenh,
				"　　　审　　核："+ shenh, "　　　审　　核：" + shenh , "", "" };
		ArrHeader[12] = new String[] { "", "　　　编　　制："+ bianz, "　　　编　　制："+ bianz,
				"　　　编　　制：" + bianz, "　　　编　　制："+ bianz, "", "" };
		ArrHeader[13] = new String[] { "", "　　　编制部门："+ bianzbm, "　　　编制部门："+ bianzbm,
				"　　　编制部门："+ bianzbm, "　　　编制部门："+ bianzbm, "", "" };
		ArrHeader[14] = new String[] { "", "　　　编制时间：" + time, "　　　编制时间：" + time,
				"　　　编制时间：" + time , "　　　编制时间：" + time, "", "" };

		int ArrWidth[] = new int[] { 100, 100, 100, 100, 100, 100, 130 };

		rt.setTitle("", ArrWidth);

//		设置页面
		rt.setBody(new Table(ArrHeader, 0, 0, 0));
		rt.body.setRowHeight(30);
		rt.body.setWidth(ArrWidth);
		rt.getPages();

		// 将边框去掉
		rt.body.setBorder(0, 0, 0, 0);
		rt.body.setCells(1, 1, ArrHeader.length, 7, Table.PER_BORDER_BOTTOM, 0);
		for (int i = 1; i <= 15; i++) {
			for (int j = 1; j <= 7; j++) {
				rt.body.setCellBorder(i, j, 0, 0, 0, 0);
			}
		}
		// 设置合并的单元格
		rt.body.merge(1, 1, 1, 2);
		rt.body.mergeCell(3, 2, 3, 6);
		rt.body.merge(4, 3, 4, 5);
		rt.body.mergeCell(10, 2, 10, 7);
		rt.body.mergeCell(11, 2, 11, 5);
		rt.body.mergeCell(12, 2, 12, 5);
		rt.body.mergeCell(13, 2, 13, 5);
		rt.body.mergeCell(14, 2, 14, 5);
		rt.body.mergeCell(15, 2, 15, 5);
		rt.body.setRowCells(3, Table.PER_FONTNAME, "黑体");
		rt.body.setRowCells(3, Table.PER_FONTSIZE, 38);
		rt.body.setCellAlign(3, 2, Table.ALIGN_CENTER);
		rt.body.setRowCells(4, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(4, Table.PER_FONTSIZE, 22);
		rt.body.setCellAlign(4, 3, Table.ALIGN_CENTER);
		rt.body.setCellFontBold(4, 3, true);
		rt.body.setRowCells(10, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(10, Table.PER_FONTSIZE, 18);
		rt.body.setCellAlign(10, 3, Table.ALIGN_LEFT);
		rt.body.setRowCells(11, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(11, Table.PER_FONTSIZE, 18);
		rt.body.setCellAlign(11, 3, Table.ALIGN_LEFT);
		rt.body.setRowCells(12, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(12, Table.PER_FONTSIZE, 18);
		rt.body.setCellAlign(12, 3, Table.ALIGN_LEFT);
		rt.body.setRowCells(13, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(13, Table.PER_FONTSIZE, 18);
		rt.body.setCellAlign(13, 3, Table.ALIGN_LEFT);
		rt.body.setRowCells(14, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(14, Table.PER_FONTSIZE, 18);
		rt.body.setCellAlign(14, 3, Table.ALIGN_LEFT);
		rt.body.setRowCells(15, Table.PER_FONTNAME, "宋体");
		rt.body.setRowCells(15, Table.PER_FONTSIZE, 18);
		rt.body.setCellAlign(15, 3, Table.ALIGN_LEFT);
		rt.body.setCellImage(1, 1, 285, 48, "imgs/report/GDTLogo_new.gif");
		rt.body.setCellspacing(10);
		rt.body.setRowHeight(30);
		rt.body.setRowHeight(3, 60);
		rt.body.setRowHeight(4, 60);
		rt.body.setRowHeight(5, 60);
		rt.body.setRowHeight(6, 60);
		// rt.body.merge(1, 1, 1, 2);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		// System.out.println(rt.getAllPagesHtml_GD());
		sjrs.close();
		con.Close();
		return rt.getAllPagesHtml_GD(0);

	}

	// 盘点人员页报表
	public String Pandrybb() {
		JDBCcon con = new JDBCcon();
		String sql = "select bum,canjry,zhiz from pandryb b ,pand_gd pg where  b.pand_gd_id=pg.id and b.pand_gd_id="+this.getPandsjValue().getId()+" order by b.id";
		ResultSet rs = con.getResultSet(sql);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][3];
		ArrWidth = new int[] { 130, 150, 400 };
		ArrHeader[0] = new String[] { "部门", "参加人员", "职责" };
		rt.setBody(new Table(rs, 1, 0, 2));

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setCellFontBold(1, 1, true);
		rt.body.setCellFontBold(1, 2, true);
		rt.body.setCellFontBold(1, 3, true);
		rt.body.setRowHeight(100);
		rt.body.setRowHeight(1, 40);
		for (int i=1;i<=rt.body.getRows();i++)
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 14);

		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(), Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		//设置标题
		rt.createTitle(5, new int[]{600});
		rt.title.setCellValue(2, 1, "一、本次盘点参加部门及其职责");
		rt.title.setCellValue(4, 1, "盘点工作小组成员及其职责");
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.title.setCellAlign(4, 1, Table.ALIGN_CENTER);
		rt.title.setCellFont(2, 1, "宋体", 16, true);
		rt.title.setCellFont(4, 1, "宋体", 16, true);

//		设置页面

		_CurrentPage = 1;

		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml_GD(1);
	}

	// 盘点事项说明表
	public String pandsxbb() throws SQLException{
		JDBCcon cn=new JDBCcon();
		Report rt=new Report();
		String sql=

				"select pg.riq,to_char(pg.kaisrq,'yyyy-mm-dd hh24:mi') as kaisrq,\n" +
						"    \t\t      to_char(pg.jiesrq,'yyyy-mm-dd hh24:mi') as jiesrq,\n" +
						"    \t\t        p.pandff,\n" +
						"    \t\t        p.shiyyq,\n" +
						"    \t\t        p.meitcfqk,\n" +
						"    \t\t        p.midcd,\n" +
						"    \t\t        p.rulmjljsfctz,\n" +
						"    \t\t        p.yingkqkfx\n" +
						"    \t\t   from pand_gd pg, pandsxb p\n" +
						"    \t\t  where p.pand_gd_id = pg.id and pg.id="+this.getPandsjValue().getId()+"";
		String riq="";
		String kais="";
		String jies="";
		String pandff="";
		String shiyyq="";
		String meitcfqk="";
		String midcd="";
		String rulmjljsfctz="";
		String yingkqkfx="";
		String a1="";
		String a2="";
		String b1="";
		ResultSetList rs = cn.getResultSetList(sql);
		if(rs.getRows()==0){
			kais=null;
			jies=null;
			pandff=null;
			shiyyq=null;
			meitcfqk=null;
			midcd=null;
			rulmjljsfctz=null;
			yingkqkfx=null;
			a1=null;
			a2=null;
			b1=null;
		}else{
			while(rs.next()){
				riq=DateUtil.Formatdate("yyyy年MM月dd日", rs.getDate("riq"));
				kais=rs.getString("kaisrq");
				jies=rs.getString("jiesrq");
				pandff=rs.getString("pandff");
				shiyyq=rs.getString("shiyyq");
				meitcfqk=rs.getString("meitcfqk");
				midcd=rs.getString("midcd");
				rulmjljsfctz=rs.getString("rulmjljsfctz");
				yingkqkfx=rs.getString("yingkqkfx");
			}

			String a[]=kais.split(" ");
			String b[]=jies.split(" ");
			a1=a[0];
			a2=a[1];
			b1=b[1];
		}
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[7][1];
		ArrHeader[0] = new String[] { "　　1.盘点时间：<br>&nbsp;<br>　　"+a1+" "+a2};
		ArrHeader[1] = new String[] { "　　2.盘点方法：<br>&nbsp;<br>　　"+pandff};
		ArrHeader[2] = new String[] { "　　3.使用仪器：<br>&nbsp;<br>　　"+shiyyq};
		ArrHeader[3] = new String[] { "　　4.煤炭存放情况：<br>&nbsp;<br>　　"+meitcfqk};
		ArrHeader[4] = new String[] { "　　5.密度测定：<br>&nbsp;<br>　　"+midcd};
		ArrHeader[5] = new String[] { "　　6.入炉煤计量及水份差调整：<br>&nbsp;<br>　　"+rulmjljsfctz };
		ArrHeader[6] = new String[] { "　　7.盈亏情况分析：<br>&nbsp;<br>　　"+yingkqkfx};
		ArrWidth = new int[] { 600 };
		//设置标题
		rt.createTitle(3, new int[]{600});
		rt.title.setCellValue(2, 1, "二、主要事项说明");
		rt.title.setCellFont(2, 1, "宋体", 16, true);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);

// 		设置页面

		rt.setBody(new Table(ArrHeader, 0, 0, 0));
		rt.body.setRowHeight(40);
		rt.body.setWidth(ArrWidth);
		rt.body.setBorderNone();
		rt.body.setCellspacing(20);

		for (int i=1;i<=rt.body.getRows();i++)
			rt.body.setCellFont(i, 1, "宋体", 14, false);
		for (int i = 1; i <= rt.body.getRows(); i++)
			for (int j = 1; j <= rt.body.getCols(); j++)
				rt.body.setCellBorderNone(i, j);

		cn.Close();

		return rt.getAllPagesHtml_GD(2);
	}
	// 盘点汇总表
	public String pandhzb() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		CustomDate custom = new CustomDate();
		String time = "";
		String pandrq = "";
		String whererq = "";
		String strPandrq1="";
		String strPandrq = "";
		long pandID = -1;
		if (getPandsjValue() == null) {// 把请选择去掉,有时候有空的时候,反回
			setPrintTable("");
			return "";
		} else {
			pandID = getPandsjValue().getId();
		}
		String sjsql = "select p.riq as riq, p.kaisrq from  pand_gd p where p.id="
				+ pandID;
		ResultSetList sjrs = cn.getResultSetList(sjsql);

		if (sjrs.next()) {
			time = DateUtil.Formatdate("yyyy年MM月dd日", sjrs.getDate("riq"));

			strPandrq = DateUtil.Formatdate("yyyy年MM月", sjrs.getDate("kaisrq"));

			strPandrq1= DateUtil.Formatdate("yyyy-MM", sjrs.getDate("kaisrq"));

			pandrq=DateUtil.Formatdate("yyyy年MM月dd日", sjrs.getDate("kaisrq"));
			whererq=DateUtil.Formatdate("yyyy-MM-dd", sjrs.getDate("kaisrq"));
		}
		int ges = 0;

		String danw = "";
		// 煤场
		String sql = "select decode(b.mingc,null,'小计',b.mingc) as mingc, sum(m.shul) as shul\n"
				+ "  from Meicdxcsb m, meicb b\n"
				+ " where m.meicb_id = b.id\n"
				+ " and m.pand_gd_id ="
				+ this.getPandsjValue().getId()
				+ "\n"
				+ " group by rollup(b.mingc)\n" + " order by b.mingc";
		;
		ResultSetList rs = cn.getResultSetList(sql);

		String sql1 = "select decode(jz.jizbh,null,'小计',jz.jizbh) as jizbh,sum(pd.shul) as shul from jizb jz,item i,meicclb gl,pandgdcmb pd\n"
				+ "       where  pd.meicclb_id = gl.id\n"
				+ "              and jz.id = gl.jizb_id\n"
				+ "              and gl.item_id = i.id\n"
				+ "              and pd.pand_gd_id = "
				+ this.getPandsjValue().getId()
				+ "\n"
				+ "              group by rollup(jz.jizbh)"
				+ "				order by jizbh";

		ResultSetList rs1 = cn.getResultSetList(sql1);

		// 煤仓
		String sql2 = "select decode(wz.mingc,null,'小计',wz.mingc) as xiemg,sum(cunml) as cunml from pandgdwzcmb cm,pandgdcmwz wz\n"
				+ "       where cm.pandgdcmb_id = wz.id\n"
				+ "             and cm.pand_gd_id = "
				+ this.getPandsjValue().getId()
				+ "\n"
				+ "			  group by rollup(wz.mingc) \n"
				+ "             order by mingc";

		ResultSetList rs2 = cn.getResultSetList(sql2);

		// 卸煤沟
		sql = "select max(a) as a from\n" + "(select " + rs.getRows()
				+ " as a from dual\n" + "union\n" + "select " + rs1.getRows()
				+ " as a from dual\n" + "union\n" + "select " + rs2.getRows()
				+ " as a from dual)";

		ResultSetList rec = cn.getResultSetList(sql);

		if (rec.next()) {

			ges = rec.getInt(0);
		}
		rec.close();

		double dangrscll = 0;
		double dangrschl = 0;
		double panqhll = 0;
		double panqhhl = 0;
		double rucpjsf =0;
		double rulpjsf = 0;
		double shuifctz = 0;

		double str_meichangxj = 0;
		double str_meicangxj = 0;
		double str_xiemgxj = 0;

		String sql4 = "select  nvl(b.dangrscll,0) as dangrscll,nvl(b.dangrschl,0) as dangrschl,nvl(b.panqhll,0) as panqhll,nvl(b.panqhhl,0) as panqhhl,nvl(b.rucpjsf,0) as rucpjsf,nvl(b.rulpjsf,0) as rulpjsf,nvl(b.shuifctz,0) as shuifctz from pandqhbhb b,pand_gd p where b.pand_gd_id=p.id and p.id="+this.getPandsjValue().getId()+"";
		ResultSetList rs3 = cn.getResultSetList(sql4);
		while (rs3.next()) {
			dangrscll = CustomMaths.Round_New(rs3.getDouble("dangrscll"),2);
			dangrschl =  CustomMaths.Round_New(rs3.getDouble("dangrschl"),2);
			panqhll =  CustomMaths.Round_New(rs3.getDouble("panqhll"),2);
			panqhhl =  CustomMaths.Round_New(rs3.getDouble("panqhhl"),2);
			rucpjsf =  CustomMaths.Round_New(rs3.getDouble("rucpjsf"),2);
			rulpjsf =  CustomMaths.Round_New(rs3.getDouble("rulpjsf"),2);
			shuifctz =  CustomMaths.Round_New(rs3.getDouble("shuifctz"),2);
		}
		String sql3 = "SELECT QUANC FROM DIANCXXB WHERE ID=(select diancxxb_id from pand_gd where id ="
				+ getPandbID() + ")";
		ResultSetList mingc = cn.getResultSetList(sql3);
		if (mingc.next()) {
			danw = mingc.getString("QUANC");
		}
		// 盘点人员
		String sql5 =

				"select  p.bum as bum,p.canjry as canjry\n"
						+ "  from pandryb p, pand_gd pg\n"
						+ " where p.pand_gd_id = pg.id\n" + " and p.pand_gd_id="
						+ this.getPandsjValue().getId() + "";

		ResultSetList bm = cn.getResultSetList(sql5);
		int bums = 0;
		String canjry = "";
		bums = bm.getRows();
		int i = bums / 3;
		int hangs = 0;
		if (bums!=0){
			hangs=1;
		}
		if (bums % 3 != 0) {
			hangs = i + 1;
		}
		//账面数据
		double runxcs=0;
		String sql6="select Round_new(avg(kuc)*0.005,2) as runxcs from shouhcrbb b where to_char(b.riq,'yyyy-mm-dd') like '"+strPandrq1+"%' and b.riq=to_date('"+whererq+"','yyyy-mm-dd')";
		ResultSetList zm=cn.getResultSetList(sql6);
		while(zm.next()){
			runxcs= CustomMaths.Round_New(zm.getDouble("runxcs"),2);
		}

		double yunxcs = 0;
		double jitcs = 0;
		double yuns = 0;
		double dangyljrcsf = 0;
		double dangyljrlsf = 0;
		double shuiftz = 0;
		double qickc = 0;
		double benylm = 0;
		double benyhm = 0;
		double qithm = 0;
		double diaocl = 0;
		double benqkc = 0;
		double jitshhkc= 0;
		double benqyk = 0;
		String sql7=
				"select p.yunxcs,\n" +
						"       p.jitcs,\n" +
						"       p.yuns,\n" +
						"       p.dangyljrcsf,\n" +
						"       p.dangyljrlsf,\n" +
						"       p.shuifctz,\n" +
						"       p.qickc,\n" +
						"       p.benylm,\n" +
						"       p.benyhm,\n" +
						"       p.qithm,\n" +
						"       p.diaocl,\n" +
						"        p.benqkc,\n" +
						"       p.jitshhkc "+
						"       from pandzmb p where p.pand_gd_id="+this.getPandsjValue().getId();
		ResultSetList zm1=cn.getResultSetList(sql7);
		while(zm1.next()){
			yunxcs = zm1.getDouble("yunxcs");
			jitcs = zm1.getDouble("jitcs");
			yuns = zm1.getDouble("yuns");
			dangyljrcsf = zm1.getDouble("dangyljrcsf");
			dangyljrlsf = zm1.getDouble("dangyljrlsf");
			shuiftz = zm1.getDouble("shuifctz");
			qickc = zm1.getDouble("qickc");
			benylm = zm1.getDouble("benylm");
			benyhm = zm1.getDouble("benyhm");
			qithm = zm1.getDouble("qithm");
			diaocl = zm1.getDouble("diaocl");
			benqkc = zm1.getDouble("benqkc");
			jitshhkc = zm1.getDouble("jitshhkc");

		}
		double shuif=0;
		double huif=0;
		String sql8=
				"select sum(b.shuif) as shuif, sum(b.huif) as huif\n" +
						"           from diaor16bb b\n" +
						"          where b.riq = to_date('"+strPandrq1+"-1', 'yyyy-mm-dd')\n" +
						"            and b.fenx = '累计'";
		ResultSetList zm2=cn.getResultSetList(sql8);
		while(zm2.next()){
			shuif=zm2.getDouble("shuif");
			huif=zm2.getDouble("huif");
		}
		String ArrHeader[][] = new String[11 + ges + 1 + hangs][7];
		ArrHeader[0] = new String[] { "盘<br>点<br>实<br>际<br>数<br>据<br>统<br>计", "实际库存", "实际库存", "实际库存",
				"实际库存", "实际库存", "实际库存" };
		ArrHeader[1] = new String[] { "盘<br>点<br>实<br>际<br>数<br>据<br>统<br>计", "煤场", "数量", "煤仓", "数量",
				"卸煤沟", "数量" };

		for (int n = 2; n < ges + 2; n++) {

			ArrHeader[n] = new String[] { "盘<br>点<br>实<br>际<br>数<br>据<br>统<br>计", "", "", "", "", "", "" };

			while (rs.next()) {

				if (rs.getString(0).equals("小计")) {

					str_meichangxj = rs.getDouble(1);

				} else {

					ArrHeader[n][1] = rs.getString(0);
					ArrHeader[n][2] = rs.getString(1);
					break;
				}

			}

			while (rs1.next()) {

				if (rs1.getString(0).equals("小计")) {

					str_meicangxj = rs1.getDouble(1);


				} else {

					ArrHeader[n][3] = rs1.getString(0);
					ArrHeader[n][4] = rs1.getString(1);
					break;
				}
			}

			while (rs2.next()) {

				if (rs2.getString(0).equals("小计")) {

					str_xiemgxj = rs2.getDouble(1);

				} else {

					ArrHeader[n][5] = rs2.getString(0);
					ArrHeader[n][6] = rs2.getString(1);
					break;
				}
			}
		}

		double shijpc =  CustomMaths.Round_New(str_meichangxj
				+ str_meicangxj
				+ str_xiemgxj-dangrscll
				+ dangrschl,2);

		double yuedsjkc= getPandqhzt(getPandbID())?CustomMaths.Round_New(shijpc+panqhll-panqhhl+shuifctz,2):CustomMaths.Round_New(shijpc-panqhll+panqhhl+shuifctz,2);
		//benqyk=CustomMaths.Round_New(yuedsjkc-jitshhkc, 2); //本期盈亏
		benqyk=0;
		ArrHeader[++ges] = new String[] { "盘<br>点<br>实<br>际<br>数<br>据<br>统<br>计", "小计", str_meichangxj+"",
				"小计", str_meicangxj+"", "小计", str_xiemgxj+"" };
		ArrHeader[++ges] = new String[] { "盘<br>点<br>实<br>际<br>数<br>据<br>统<br>计", "当日时差来量："+dangrscll,
				"当日时差来量："+dangrscll, "当日时差耗量:"+dangrschl, "当日时差耗量:"+dangrschl, "", "" };
		ArrHeader[++ges] = new String[] { "盘<br>点<br>实<br>际<br>数<br>据<br>统<br>计", "实际盘存:"+ shijpc, "实际盘存:"+ shijpc, "实际盘存:"+ shijpc, "实际盘存:"+ shijpc, "实际盘存:"+ shijpc, "实际盘存:"+ shijpc };
		ArrHeader[++ges] = new String[] { "月<br>末<br>折<br>算", "盘前(后)来量："+ panqhll,
				"盘前(后)来量："+ panqhll, "盘前(后)来量："+ panqhll,"盘前(后)耗量："+ panqhhl,"盘前(后)耗量："+ panqhhl,"盘前(后)耗量："+ panqhhl };
		ArrHeader[++ges] = new String[] { "月<br>末<br>折<br>算", "入厂平均水分:"+ rucpjsf, "入厂平均水分:"+ rucpjsf,
				"入炉平均水分:"+ rulpjsf,"入炉平均水分:"+ rulpjsf, "水分差调整:"+shuifctz, "水分差调整:"+shuifctz };
		ArrHeader[++ges] = new String[] { "月<br>末<br>折<br>算", "月度实际库存："+yuedsjkc,"月度实际库存："+yuedsjkc,"月度实际库存："+yuedsjkc,"月度实际库存："+yuedsjkc,"月度实际库存："+yuedsjkc,"月度实际库存："+yuedsjkc};
		ArrHeader[++ges] = new String[] { "账<br>面", "允许储损:"+yunxcs, "允许储损:"+yunxcs, "计提储损:"+jitcs,"计提储损:"+jitcs,
				"运输损耗:"+yuns,"运输损耗:"+yuns };
		ArrHeader[++ges] = new String[] { "账<br>面", "当月累计入厂水分:"+dangyljrcsf, "当月累计入厂水分:"+dangyljrcsf, "当月累计入炉水分："+dangyljrlsf, "当月累计入炉水分："+dangyljrlsf, "水分差调整:"+shuiftz, "水分差调整:"+shuiftz };
		ArrHeader[++ges] = new String[] { "账<br>面", "期初库存："+qickc, "期初库存："+qickc, "本月来煤："+benylm, "本月来煤："+benylm, "本月耗煤:"+benyhm, "本月耗煤:"+benyhm };
		ArrHeader[++ges] = new String[] { "账<br>面", "其它耗煤："+qithm,  "其它耗煤："+qithm, "调 出 量："+diaocl, "调 出 量："+diaocl, "",
				"" };
		ArrHeader[++ges] = new String[] { "账<br>面", "本期库存："+benqkc, "本期库存："+benqkc, "计提损耗后库存："+jitshhkc, "计提损耗后库存："+jitshhkc,
				"本期盈亏(±):"+benqyk, "本期盈亏(±):"+benqyk};

		int x = 0;
		for (int j = ges + 1; j < ges + 1 + hangs; j++) {
			ArrHeader[j] = new String[] { "参<br>加<br>盘<br>点<br>人<br>员", "", "", "", "", "", "" };
			while (bm.next()) {
				x++;
				ArrHeader[j][x] = bm.getString("bum")+"："+bm.getString("canjry");
				ArrHeader[j][++x] = bm.getString("bum")+"："+bm.getString("canjry");
				if (x == 6) {
					x = 0;
					break;
				}

			}
		}
		int ArrWidth[] = new int[] { 40, 90, 100, 100, 100, 100, 90 };

//		设置页面
		rt.setBody(new Table(ArrHeader, 0, 0, 0));
		rt.body.setRowHeight(40);
		rt.body.setWidth(ArrWidth);
		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(1, Table.PER_FONTSIZE, 12);
		rt.body.setColCells(1, Table.PER_FONTNAME, "仿宋");
		rt.body.setRowCells(1, Table.PER_FONTBOLD, true);
		rt.body.setRowCells(2, Table.PER_FONTBOLD, true);
		rt.body.setColCells(1, Table.PER_FONTBOLD, true);

		rt.body.merge(1, 2, 1, 7);
		rt.body.merge(1, 1, 2 + ges + 3 - 12, 1);
		rt.body.merge((2 + ges + 3 - 11), 1, (2 + ges + 2 + 3 - 11), 1);
		rt.body.merge((ges - 3), 1, ges + 1, 1);
		if(hangs!=0){
			rt.body.merge(ges + 2, 1, ges + 1 + hangs, 1);
		}
		rt.body.merge((ges -8), 2, (ges -8), 3);
		rt.body.merge((ges -8), 4, (ges -8), 5);
		rt.body.merge((ges -8), 6, (ges -8), 7);
		rt.body.merge((ges -7), 2, (ges -7), 7);
		rt.body.merge((ges -6), 2, (ges -6), 4);//月末折算第一行
		rt.body.merge((ges -6), 5, (ges -6), 7);
		rt.body.merge((ges -5), 2, (ges -5), 3);
		rt.body.merge((ges -5), 4, (ges -5), 5);
		rt.body.merge((ges -5), 6, (ges -5), 7);
		rt.body.merge((ges -4), 2, (ges -4), 7);

		for(int m=ges + 2;m<=ges + 1 + hangs;m++){
			rt.body.merge(m, 2, m, 3);
			rt.body.merge(m, 4, m, 5);
			rt.body.merge(m, 6, m, 7);
		}
		for (int m=ges-3;m<=ges+1;m++){  //账面
			rt.body.merge(m, 2, m, 3);
			rt.body.merge(m, 4, m, 5);
			rt.body.merge(m, 6, m, 7);
		}
		for (int k=1;k<=rt.body.getRows()-hangs-10;k++){
			for(int j=2;j<=rt.body.getCols();j++){
				rt.body.setCellAlign(k, j, Table.ALIGN_CENTER);
				rt.body.setCellFontName(k, j, "仿宋");
				rt.body.setCellFontSize(k, j, 12);
			}
		}
		for (int k=rt.body.getRows()-hangs-9;k<=rt.body.getRows();k++){
			for(int j=2;j<=rt.body.getCols();j++){
				rt.body.setCellAlign(k, j, Table.ALIGN_LEFT);
				rt.body.setCellFontName(k, j, "仿宋");
				rt.body.setCellFontSize(k, j, 12);
			}
		}

//		设置标题
		rt.createTitle(6, ArrWidth);

		rt.title.setCellFont(2, 1, "宋体", 16, true);
		rt.title.setCellValue(2, 1, "三、盘点数据统计汇总表",ArrWidth.length);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);

		rt.title.setCellFont(4, 1, "宋体", 14, true);
		rt.title.setCellValue(4, 1, danw +"盘点数据统计汇总表",ArrWidth.length);
		rt.title.setCellAlign(4, 1, Table.ALIGN_CENTER);

		rt.title.setCellFont(6, 1, "仿宋", 12, false);
		rt.title.setCellValue(6, 1, "盘点时间:"+pandrq,rt.title.getCols()-2);
		rt.title.setCellAlign(6, 1, Table.ALIGN_LEFT);
		rt.title.setCellFont(6, rt.title.getCols()-1, "仿宋", 12, false);
		rt.title.setCellValue(6,rt.title.getCols()-1, "单位：吨",2);
		rt.title.setCellAlign(6,rt.title.getCols()-1, Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		// System.out.println(rt.getAllPagesHtml_GD_GD());
		cn.Close();

		return rt.getAllPagesHtml_GD(3);
	}

	public String isNull(String a) {
		if(a.equals("")||a==null){
			return "0";
		}
		return a;
	}

	// 煤厂累计库存及盈亏报表
	public String MeicLjkcyk() {
		CustomDate custom=new CustomDate();
		JDBCcon cn = new JDBCcon();
		ResultSetList dcrs=cn.getResultSetList("select diancxxb_id from pand_gd where id="+getPandbID());
		String strDiancxxbId= this.getTreeid();
		if(dcrs.next()){
			strDiancxxbId=dcrs.getString("diancxxb_id");
		}
		String yuns = "";
		String shijhylzcsh = "";
		String yuemjc = "";
		String Nianf ="";
		//2010-09-18 ww 盘点时间取盘点开始时间
		String sqls = "select to_char(p.kaisrq,'yyyy') as nian  from  pand_gd p where p.id="
				+ getPandsjValue().getId() + "";

		ResultSetList rss = cn.getResultSetList(sqls);
		if(rss.next()){
			Nianf = custom.FormatDate(custom.getDate(rss.getString("nian"),
					"yyyy"), "yyyy");
		}
		String sql =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-01' AND diancxxb_id='" + strDiancxxbId + "'";


		ResultSetList rs = cn.getResultSetList(sql);
		while (rs.next()) {
			yuns = rs.getString("yuns");
			shijhylzcsh = rs.getString("shijhylzcsh");
			yuemjc = rs.getString("yuemjc");
		}
		String yuns1 = "";
		String shijhylzcsh1 = "";
		String yuemjc1 = "";
		String sql1 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-02' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs1 = cn.getResultSetList(sql1);
		while (rs1.next()) {
			yuns1 = rs1.getString("yuns");
			shijhylzcsh1 = rs1.getString("shijhylzcsh");
			yuemjc1 = rs1.getString("yuemjc");
		}
		String yuns2 = "";
		String shijhylzcsh2 = "";
		String yuemjc2 = "";
		String sql2 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-03' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs2 = cn.getResultSetList(sql2);
		while (rs2.next()) {
			yuns2 = rs2.getString("yuns");
			shijhylzcsh2 = rs2.getString("shijhylzcsh");
			yuemjc2 = rs2.getString("yuemjc");
		}
		String yuns3 = "";
		String shijhylzcsh3 = "";
		String yuemjc3 = "";
		String sql3 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-04' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs3 = cn.getResultSetList(sql3);
		while (rs3.next()) {
			yuns3 = rs3.getString("yuns");
			shijhylzcsh3 = rs3.getString("shijhylzcsh");
			yuemjc3 = rs3.getString("yuemjc");
		}
		String yuns4 = "";
		String shijhylzcsh4 = "";
		String yuemjc4 = "";
		String sql4 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-05' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs4 = cn.getResultSetList(sql4);
		while (rs4.next()) {
			yuns4 = rs4.getString("yuns");
			shijhylzcsh4 = rs4.getString("shijhylzcsh");
			yuemjc4 = rs4.getString("yuemjc");
		}
		String yuns5 = "";
		String shijhylzcsh5 = "";
		String yuemjc5 = "";
		String sql5 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-06' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs5 = cn.getResultSetList(sql5);
		while (rs5.next()) {
			yuns5 = rs5.getString("yuns");
			shijhylzcsh5 = rs5.getString("shijhylzcsh");
			yuemjc5 = rs5.getString("yuemjc");
		}
		String yuns6 = "";
		String shijhylzcsh6 = "";
		String yuemjc6 = "";
		String sql6 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-07' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs6 = cn.getResultSetList(sql6);
		while (rs6.next()) {
			yuns6 = rs6.getString("yuns");
			shijhylzcsh6 = rs6.getString("shijhylzcsh");
			yuemjc6 = rs6.getString("yuemjc");
		}
		String yuns7 = "";
		String shijhylzcsh7 = "";
		String yuemjc7 = "";
		String sql7 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-08' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs7 = cn.getResultSetList(sql7);
		while (rs1.next()) {
			yuns7 = rs7.getString("yuns");
			shijhylzcsh7 = rs7.getString("shijhylzcsh");
			yuemjc7 = rs7.getString("yuemjc");
		}
		String yuns8 = "";
		String shijhylzcsh8 = "";
		String yuemjc8 = "";
		String sql8 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-09' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs8 = cn.getResultSetList(sql8);
		while (rs8.next()) {
			yuns8 = rs8.getString("yuns");
			shijhylzcsh8 = rs8.getString("shijhylzcsh");
			yuemjc8 = rs8.getString("yuemjc");
		}
		String yuns9 = "";
		String shijhylzcsh9 = "";
		String yuemjc9 = "";
		String sql9 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-10' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs9 = cn.getResultSetList(sql9);
		while (rs9.next()) {
			yuns9 = rs9.getString("yuns");
			shijhylzcsh9 = rs9.getString("shijhylzcsh");
			yuemjc9 = rs9.getString("yuemjc");
		}
		String yuns10 = "";
		String shijhylzcsh10 = "";
		String yuemjc10 = "";
		String sql10 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-11' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs10 = cn.getResultSetList(sql10);
		while (rs10.next()) {
			yuns10 = rs10.getString("yuns");
			shijhylzcsh10 = rs10.getString("shijhylzcsh");
			yuemjc10 = rs10.getString("yuemjc");
		}
		String yuns11 = "";
		String shijhylzcsh11 = "";
		String yuemjc11 = "";
		String sql11 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf)-1)+ "-12' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs11 = cn.getResultSetList(sql11);
		while (rs11.next()) {
			yuns11 = rs11.getString("yuns");
			shijhylzcsh11 = rs11.getString("shijhylzcsh");
			yuemjc11 = rs11.getString("yuemjc");
		}
		String yuns12 = "";
		String shijhylzcsh12 = "";
		String yuemjc12 = "";
		String sql12 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')>='" + (Integer.parseInt(Nianf)-1)+ "-01'\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')<='" + (Integer.parseInt(Nianf)-1)+ "-12' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs12 = cn.getResultSetList(sql12);
		while (rs12.next()) {
			yuns12 = rs12.getString("yuns");
			shijhylzcsh12 = rs12.getString("shijhylzcsh");
			yuemjc12 = rs12.getString("yuemjc");
		}
		// 下面的是10年
		String yuns14 = "";
		String shijhylzcsh14 = "";
		String yuemjc14 = "";
		String sql14 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-01' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs14 = cn.getResultSetList(sql14);
		while (rs14.next()) {
			yuns14 = rs14.getString("yuns");
			shijhylzcsh14 = rs14.getString("shijhylzcsh");
			yuemjc14 = rs14.getString("yuemjc");
		}
		String yuns15 = "";
		String shijhylzcsh15 = "";
		String yuemjc15 = "";
		String sql15 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-02' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs15 = cn.getResultSetList(sql15);
		while (rs15.next()) {
			yuns15 = rs15.getString("yuns");
			shijhylzcsh15 = rs15.getString("shijhylzcsh");
			yuemjc15 = rs15.getString("yuemjc");
		}
		String yuns16 = "";
		String shijhylzcsh16 = "";
		String yuemjc16 = "";
		String sql16 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-03' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs16 = cn.getResultSetList(sql16);
		while (rs16.next()) {
			yuns16 = rs16.getString("yuns");
			shijhylzcsh16 = rs16.getString("shijhylzcsh");
			yuemjc16 = rs16.getString("yuemjc");
		}
		String yuns17 = "";
		String shijhylzcsh17 = "";
		String yuemjc17 = "";
		String sql17 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-04' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs17 = cn.getResultSetList(sql17);
		while (rs17.next()) {
			yuns17 = rs17.getString("yuns");
			shijhylzcsh17 = rs17.getString("shijhylzcsh");
			yuemjc17 = rs17.getString("yuemjc");
		}
		String yuns18 = "";
		String shijhylzcsh18 = "";
		String yuemjc18 = "";
		String sql18 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-05' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs18 = cn.getResultSetList(sql18);
		while (rs18.next()) {
			yuns18 = rs18.getString("yuns");
			shijhylzcsh18 = rs18.getString("shijhylzcsh");
			yuemjc18 = rs18.getString("yuemjc");
		}
		String yuns19 = "";
		String shijhylzcsh19 = "";
		String yuemjc19 = "";
		String sql19 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-06' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs19 = cn.getResultSetList(sql19);
		while (rs19.next()) {
			yuns19 = rs19.getString("yuns");
			shijhylzcsh19 = rs19.getString("shijhylzcsh");
			yuemjc19 = rs19.getString("yuemjc");
		}
		String yuns20 = "";
		String shijhylzcsh20 = "";
		String yuemjc20 = "";
		String sql20 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-07' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs20 = cn.getResultSetList(sql20);
		while (rs20.next()) {
			yuns20 = rs20.getString("yuns");
			shijhylzcsh20 = rs20.getString("shijhylzcsh");
			yuemjc20 = rs20.getString("yuemjc");
		}
		String yuns21 = "";
		String shijhylzcsh21 = "";
		String yuemjc21 = "";
		String sql21 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-08' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs21 = cn.getResultSetList(sql21);
		while (rs21.next()) {
			yuns21 = rs21.getString("yuns");
			shijhylzcsh21 = rs21.getString("shijhylzcsh");
			yuemjc21 = rs21.getString("yuemjc");
		}
		String yuns22 = "";
		String shijhylzcsh22 = "";
		String yuemjc22 = "";
		String sql22 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-09' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs22 = cn.getResultSetList(sql22);
		while (rs22.next()) {
			yuns22 = rs22.getString("yuns");
			shijhylzcsh22 = rs22.getString("shijhylzcsh");
			yuemjc22 = rs22.getString("yuemjc");
		}
		String yuns23 = "";
		String shijhylzcsh23 = "";
		String yuemjc23 = "";
		String sql23 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-10' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs23 = cn.getResultSetList(sql23);
		while (rs23.next()) {
			yuns23 = rs23.getString("yuns");
			shijhylzcsh23 = rs23.getString("shijhylzcsh");
			yuemjc23 = rs23.getString("yuemjc");
		}
		String yuns24 = "";
		String shijhylzcsh24 = "";
		String yuemjc24 = "";
		String sql24 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-11' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs24 = cn.getResultSetList(sql24);
		while (rs24.next()) {
			yuns24 = rs24.getString("yuns");
			shijhylzcsh24 = rs24.getString("shijhylzcsh");
			yuemjc24 = rs24.getString("yuemjc");
		}
		String yuns25 = "";
		String shijhylzcsh25 = "";
		String yuemjc25 = "";
		String sql25 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')='" + (Integer.parseInt(Nianf))+ "-12' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs25 = cn.getResultSetList(sql25);
		while (rs25.next()) {
			yuns25 = rs25.getString("yuns");
			shijhylzcsh25 = rs25.getString("shijhylzcsh");
			yuemjc25 = rs25.getString("yuemjc");
		}
		String yuns26 = "";
		String shijhylzcsh26 = "";
		String yuemjc26 = "";

		String sql26 =
				"SELECT SUM(yuns) AS yuns,SUM(jitcs) AS shijhylzcsh,sum(jitshhkc) as yuemjc\n" +
						"FROM pand_gd pd,pandzmb zm WHERE zm.pand_gd_id=pd.id\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')>='" + (Integer.parseInt(Nianf))+ "-01'\n" +
						"AND to_char(pd.kaisrq,'yyyy-mm')<='" + (Integer.parseInt(Nianf))+ "-12' AND diancxxb_id='" + strDiancxxbId + "'";
		ResultSetList rs26 = cn.getResultSetList(sql26);
		while (rs26.next()) {
			yuns26 = rs26.getString("yuns");
			shijhylzcsh26 = rs26.getString("shijhylzcsh");
			yuemjc26 = rs26.getString("yuemjc");
		}
		Report rt = new Report();
		String ArrHeader[][] = new String[28][4];
		ArrHeader[0] = new String[] { ""+""+(Integer.parseInt(Nianf)-1)+""+"年度", "途损量（吨）", "场损量（吨）", "月末库存量（吨）" };
		ArrHeader[1] = new String[] { "1月份", "" + yuns + "",
				"" + shijhylzcsh + "", "" + yuemjc + "" };
		ArrHeader[2] = new String[] { "2月份", "" + yuns1 + "",
				"" + shijhylzcsh1 + "", "" + yuemjc1 + "" };
		ArrHeader[3] = new String[] { "3月份", "" + yuns2 + "",
				"" + shijhylzcsh2 + "", "" + yuemjc2 + "" };
		ArrHeader[4] = new String[] { "4月份", "" + yuns3 + "",
				"" + shijhylzcsh3 + "", "" + yuemjc3 + "" };
		ArrHeader[5] = new String[] { "5月份", "" + yuns4 + "",
				"" + shijhylzcsh4 + "", "" + yuemjc4 + "" };
		ArrHeader[6] = new String[] { "6月份", "" + yuns5 + "",
				"" + shijhylzcsh5 + "", "" + yuemjc5 + "" };
		ArrHeader[7] = new String[] { "7月份", "" + yuns6 + "",
				"" + shijhylzcsh6 + "", "" + yuemjc6 + "" };
		ArrHeader[8] = new String[] { "8月份", "" + yuns7 + "",
				"" + shijhylzcsh7 + "", "" + yuemjc7 + "" };
		ArrHeader[9] = new String[] { "9月份", "" + yuns8 + "",
				"" + shijhylzcsh8 + "", "" + yuemjc8 + "" };
		ArrHeader[10] = new String[] { "10月份", "" + yuns9 + "",
				"" + shijhylzcsh9 + "", "" + yuemjc9 + "" };
		ArrHeader[11] = new String[] { "11月份", "" + yuns10 + "",
				"" + shijhylzcsh10 + "", "" + yuemjc10 + "" };
		ArrHeader[12] = new String[] { "12月份", "" + yuns11 + "",
				"" + shijhylzcsh11 + "", "" + yuemjc11 + "" };
		ArrHeader[13] = new String[] { "年度累计", "" + yuns12 + "",
				"" + shijhylzcsh12 + "", "" + yuemjc12 + "" };
		ArrHeader[14] = new String[] { ""+Nianf+" 年度", "途损量（吨）", "场损量（吨）",
				"月末库存量（吨）" };
		ArrHeader[15] = new String[] { "1月份", "" + yuns14 + "",
				"" + shijhylzcsh14 + "", "" + yuemjc14 + "" };
		ArrHeader[16] = new String[] { "2月份", "" + yuns15 + "",
				"" + shijhylzcsh15 + "", "" + yuemjc15 + "" };
		ArrHeader[17] = new String[] { "3月份", "" + yuns16 + "",
				"" + shijhylzcsh16 + "", "" + yuemjc16 + "" };
		ArrHeader[18] = new String[] { "4月份", "" + yuns17 + "",
				"" + shijhylzcsh17 + "", "" + yuemjc17 + "" };
		ArrHeader[19] = new String[] { "5月份", "" + yuns18 + "",
				"" + shijhylzcsh18 + "", "" + yuemjc18 + "" };
		ArrHeader[20] = new String[] { "6月份", "" + yuns19 + "",
				"" + shijhylzcsh19 + "", "" + yuemjc19 + "" };
		ArrHeader[21] = new String[] { "7月份", "" + yuns20 + "",
				"" + shijhylzcsh20 + "", "" + yuemjc20 + "" };
		ArrHeader[22] = new String[] { "8月份", "" + yuns21 + "",
				"" + shijhylzcsh21 + "", "" + yuemjc21 + "" };
		ArrHeader[23] = new String[] { "9月份", "" + yuns22 + "",
				"" + shijhylzcsh22 + "", "" + yuemjc22 + "" };
		ArrHeader[24] = new String[] { "10月份", "" + yuns23 + "",
				"" + shijhylzcsh23 + "", "" + yuemjc23 + "" };
		ArrHeader[25] = new String[] { "11月份", "" + yuns24 + "",
				"" + shijhylzcsh24 + "", "" + yuemjc24 + "" };
		ArrHeader[26] = new String[] { "12月份", "" + yuns25 + "",
				"" + shijhylzcsh25 + "", "" + yuemjc25 + "" };
		ArrHeader[27] = new String[] { "年度累计", "" + yuns26 + "",
				"" + shijhylzcsh26 + "", "" + yuemjc26 + "" };
		int ArrWidth[] = new int[] { 150, 150, 150, 150 };
		rt.setTitle("煤场累计库存及盈亏表", ArrWidth);

//		设置标题
		rt.createTitle(5, new int[]{600});
		rt.title.setCellFont(2, 1, "宋体", 16, true);
		rt.title.setCellValue(2, 1,"4.库存盘点有关图表");
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.title.setCellFont(4, 1, "宋体", 14, true);
		rt.title.setCellValue(4, 1, "(1)煤场累计库存及盈亏表");
		rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);
		//rt.title.setCellspacing(10);
//		设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);

		rt.setBody(new Table(ArrHeader, 0, 0, 0));
		rt.body.setRowHeight(30);
		rt.body.setWidth(ArrWidth);
		for(int j=1;j<=rt.body.getRows();j++){
			for (int i = 0; i <= rt.body.getCols(); i++) {
				rt.body.setCellAlign(j, i, Table.ALIGN_CENTER);
				rt.body.setCellFontSize(j, i, 12);
			}
		}
		rt.body.setRowCells(1, Table.PER_FONTBOLD, true);
		rt.body.setRowCells(15, Table.PER_FONTBOLD, true);
		_CurrentPage = 1;
		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml_GD(7);
	}

	// 煤厂堆形测试记录表
	public String panddxcsbb() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		Report rt = new Report();
		int[] ArrWidth;
		CustomDate custom = new CustomDate();
		long pandID =-1;
		if (getPandsjValue() == null) {// 把请选择去掉,有时候有空的时候,反回
			setPrintTable("");
			return "";
		} else {
			pandID = getPandsjValue().getId();
		}
		String strPandrq="";
		String danw ="";
		String sjsql = "select to_char(p.riq,'yyyy-mm-dd') as riq, to_char(p.kaisrq,'yyyy-mm-dd') as pmrq from  pand_gd p where p.id="
				+ pandID;
		ResultSetList sjrs = con.getResultSetList(sjsql);

		if (sjrs.next()) {

			strPandrq = custom.FormatDate(custom.getDate(sjrs.getString("pmrq"),
					"yyyy-MM-dd"), "yyyy年MM月dd日");
		}
		String sql1="SELECT QUANC FROM DIANCXXB WHERE ID=(select diancxxb_id from pand_gd where id ="
				+ getPandbID() + ")";
		ResultSetList mingc =con.getResultSetList(sql1);
		if(mingc.next()){
			danw=mingc.getString("QUANC");
		}

		//判断是人工还是激光采样
		String pandff="";
		String sqlff="select pandff from pandsxb where pand_gd_id="+this.getPandsjValue().getId();
		ResultSetList rss=con.getResultSetList(sqlff);
		while(rss.next()){
			pandff = rss.getString("pandff");
		}
		if(pandff.equals("人工")){
			//行标题_指标名称
			StringBuffer col_title = new StringBuffer();
			col_title.append(

					"select  mingc as 线性尺寸 from (select mingc from meicb,meicdxcsb where meicdxcsb.meicb_id=meicb.id and meicdxcsb.pand_gd_id=" + pandID + " ) order by mingc"

			);

			//列标题_电厂名称
			StringBuffer row_title = new StringBuffer();
			row_title.append(

					"select mingc as 名称 from\n" +
							"(select 1 as id ,'上宽b1' as mingc from meicdxcsb union\n" +
							"       select 2 as id ,'下宽b' as mingc from meicdxcsb union\n" +
							"       select 3 as id ,'高度h' as mingc from meicdxcsb union\n" +
							"       select 4 as id ,'顶长a1' as mingc from meicdxcsb union\n" +
							"       select 5 as id ,'基长a' as mingc from meicdxcsb union\n" +
							"       select 6 as id ,'体积(m3)' as mingc from meicdxcsb union\n" +
							"       select 7 as id ,'密度(t/m3)' as mingc from meicdxcsb  union\n" +
							"       select 8 as id ,'数量(t)' as mingc from meicdxcsb)\n" +
							"   order by id"

			);

			//数据
			StringBuffer data = new StringBuffer();
			data.append(

					"select a.线性尺寸,a.mingc as 名称 ,a.shuj from\n" +
							" (select 1 as id,mc.mingc as 线性尺寸, '上宽b1' as mingc, m.shangk as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"    select 2 as id, mc.mingc as 线性尺寸, '下宽b' as mingc, m.xiak as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"   select 3 as id, mc.mingc as 线性尺寸, '高度h' as mingc, m.gao as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"    select 4 as id, mc.mingc as 线性尺寸, '顶长a1' as mingc, m.dingc as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"   select 5 as id,mc.mingc as 线性尺寸, '基长a' as mingc, m.jic as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"   select  6 as id,mc.mingc as 线性尺寸, '体积(m3)' as mingc, m.tij as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"   select 7 as id, mc.mingc as 线性尺寸, '密度(t/m3)' as mingc, m.mid as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + "\n" +
							"   union\n" +
							"   select 8 as id, mc.mingc as 线性尺寸, '数量(t)' as mingc, m.shul as shuj\n" +
							"   from meicdxcsb m,meicb mc\n" +
							"   where m.meicb_id = mc.id and m.pand_gd_id=" + pandID + ") a\n" +
							"   order by a.id"

			);



			cd.setRowNames("名称");
			cd.setColNames("线性尺寸");
			cd.setDataNames("shuj");
			cd.setDataOnRow(false);
			cd.setRowToCol(false);
			//生成棋盘表数组
			cd.setData(row_title.toString(), col_title.toString(), data.toString());

			ArrWidth = new int[cd.DataTable.getCols()];
			for (int i = 1; i < ArrWidth.length; i++) {
				ArrWidth[i] = 100;
			}
			ArrWidth[0] = 140;
			rt.setBody(cd.DataTable);
			rt.body.setWidth(ArrWidth);
			rt.body.setRowCells(1, Table.PER_FONTBOLD, true);
			rt.body.setRowCells(1, Table.PER_FONTSIZE, 11);
			rt.body.setRowHeight(30);
			cd.DataTable.setCellValue(1, 1, "线性尺寸（m）");
			for(int i=2;i<=rt.body.getRows();i++){
				for(int j=1;j<=rt.body.getCols();j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
					rt.body.setCellFontSize(i, j, 11);
				}
			}

			if (ArrWidth.length==1) {
				return "";
			}

			//设置标题
			rt.createTitle(6, new int[]{300,300});
			rt.title.setCellFont(2, 1, "宋体", 16, true);
			rt.title.setCellValue(2, 1, "四、附表",2);
			rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
			rt.title.setCellFont(4, 1, "宋体", 14, true);
			rt.title.setCellValue(4, 1, "1."+danw +"煤场堆形测量记录",2);
			rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);
			rt.title.setCellFont(6, 1, "仿宋", 12, false);
			rt.title.setCellValue(6, 1, "盘点时间:"+strPandrq,2);
			rt.title.setCellAlign(6, 1, Table.ALIGN_LEFT);

			_CurrentPage = 1;
			_AllPages = rt.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			rt.createFooter(1, ArrWidth);
			rt.footer.setCellValue(1, 1, "　　审核:");
			rt.footer.setCellAlign(1, 1, Table.ALIGN_LEFT);
			rt.footer.setCellFont(1, 1, "宋体", 10, false);
			rt.footer.setCellValue(1, 2, "　　制表:");
			rt.footer.setCellAlign(1, 2, Table.ALIGN_LEFT);
			rt.footer.setCellFont(1, 2, "宋体", 10, false);

			con.Close();
			return rt.getAllPagesHtml_GD(4);
		}else{

			rt.createTitle(6, new int[]{300,300});
			rt.title.setCellFont(2, 1, "宋体", 16, true);
			rt.title.setCellValue(2, 1, "四、附表",2);
			rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
			rt.title.setCellFont(4, 1, "宋体", 16, true);
			rt.title.setCellValue(4, 1, "1."+danw +"煤场堆形测量记录",2);
			rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);
			rt.title.setCellFont(6, 1, "仿宋", 12, false);
			rt.title.setCellValue(6, 1, "盘点时间:"+strPandrq,2);
			rt.title.setCellAlign(6, 1, Table.ALIGN_LEFT);
			// String Sql="select id from MEICDXCSB where pand_gd_id=19725413758" ;
			String sql = "select * from tupccb where bianm ='0'" ;
			ResultSetList rs = con.getResultSetList(sql);
			// ResultSetList rs= con.getResultSetList(Sql);
			String ArrHeader [][]=new String [rs.getRows()][5];
			for(int i=0;i<rs.getRows();i++){
				ArrHeader [0] = new String[]{"","","","",""};
			}
			if(ArrHeader.length==0){
				ArrHeader = new String [1][5];
				ArrHeader [0] = new String[]{"","","","",""};
			}
			int[] ArrWidth1 = {80,80,80,80,80};
			rt.setBody(new Table(ArrHeader, 0, 0, 0));
			rt.body.setRowHeight(30);
			rt.body.setWidth(ArrWidth1);
			rt.getPages();
			//去掉边框
			rt.body.setBorder(0, 0, 0, 0);
			rt.body.setCells(1, 1, ArrHeader.length, 5, Table.PER_BORDER_BOTTOM, 0);
			for (int i = 1; i <= rs.getRows(); i++) {
				for (int j = 1; j <= 5; j++) {
					rt.body.setCellBorder(i, j, 0, 0, 0, 0);
				}
			}
			//设置图片
			List fileModel = new ArrayList();

			while (rs.next()) {
				String srcFileName = getId() + "_" + rs.getInt("xuh")
						+ FileUtil.getSuffix(rs.getString("mingc"));
				File srcFile = new File(getImageFilePath(), srcFileName);
				int j=1;
				for(int i=j;i<=rs.getRows();i++){
					rt.body.setCellImage(i, 3, 600, 500, MainGlobal.getHomeContext(this)+"/img/tmp/pandReport/0_1.JPG");
				}
				j++;
				try {
					fileModel.add(new IDropDownBean(rs.getString("xuh"), rs
							.getString("mingc")));
					FileUtil.copy(srcFile, getImageTmpPath().getPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        		/*int j=1;
        		while(rs.next()){

        			for(int i=j;i<=rs.getRows();i++){
						rt.body.setCellImage(i, 3, 600, 500, "D:/zhiren/pandReport/" + srcFileName);
        			// rt.body.setCellImage(i, 3, 600, 500, "D:/zhiren/pandReport/" + this.getTreeid() + "/"+rs.getString("id")+"_1.jpg");
        			}
        			j++;
        		}*/
//        	 设置标题

			_CurrentPage = 1;
			_AllPages = rt.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			return rt.getAllPagesHtml_GD(4);
		}

	}
	//煤仓存量情况统计表
	public String Meicclqktjb() {
		JDBCcon cn = new JDBCcon();
		String time = "";
		String pandrq = "";
		String strPandrq = "";
		String danw = "";
		String sql1="SELECT QUANC FROM DIANCXXB WHERE ID=(select diancxxb_id from pand_gd where id ="
				+ getPandbID() + ")";
		ResultSetList mingc =cn.getResultSetList(sql1);
		if(mingc.next()){
			danw=mingc.getString("QUANC");
		}
		CustomDate custom = new CustomDate();
		long pandID = -1;
		if (getPandsjValue() == null) {// 把请选择去掉,有时候有空的时候,反回
			setPrintTable("");
			return "";
		} else {
			pandID = getPandsjValue().getId();
		}
		String sql = "select to_char(p.riq,'yyyy-mm-dd') as riq,to_char(p.kaisrq,'yyyy-mm-dd') as pmrq  from  pand_gd p where p.id="
				+ pandID;
		ResultSetList rs = cn.getResultSetList(sql);

		if (rs.next()) {

			strPandrq = custom.FormatDate(custom.getDate(rs.getString("pmrq"),
					"yyyy-MM-dd"), "yyyy年MM月dd日");
		}

		int row_count = 0;
		int col_count = 0;
//		得到机组个数和最大煤仓数
		sql=
				"select jz.jizbh,count(gl.item_id) as mcgs\n" +
						"       from pandgdcmb cm,meicclb gl,jizb jz,item mc\n" +
						"       where cm.meicclb_id = gl.id\n" +
						"             and gl.jizb_id = jz.id\n" +
						"             and gl.item_id = mc.id\n" +
						"             and cm.pand_gd_id = "+pandID+"\n" +
						"             group by jz.jizbh\n" +
						"             order by count(gl.item_id) desc";

		rs = cn.getResultSetList(sql);
		if(rs.getRows()==0){
			row_count = 3;
			col_count = 3;
		}else{
			if(rs.next()){

				col_count = rs.getInt("mcgs")+1;
			}

			row_count=rs.getRows()*3;
		}

//		初始化
		Report rt = new Report();
		String ArrHeader[][] = new String[row_count][col_count];

		for (int i = 0; i < row_count; i++) {


			ArrHeader[i] = new String[col_count];
		}


//		得到明细信息
		String jizmc="";
		String jizmc1="";
		//机组名称
		sql=
				"select jz.jizbh,mc.mingc,cm.shul,round_new((cm.shul/mc.beiz)*100,1) as biaoc\n" +
						"       from pandgdcmb cm,meicclb gl,jizb jz,item mc\n" +
						"  where cm.meicclb_id = gl.id\n" +
						"        and gl.jizb_id = jz.id\n" +
						"        and gl.item_id = mc.id\n" +
						"        and cm.pand_gd_id = "+pandID+"\n" +
						"        order by jizbh,mc.mingc";

		rs = cn.getResultSetList(sql);


		int i=0,j=0;
		while(rs.next()){

			if("".equals(jizmc)){

				jizmc = rs.getString(0);
				ArrHeader[i][j++] = rs.getString(0);
				ArrHeader[i+1][j-1] = "煤位(％)";
				ArrHeader[i+2][j-1] = "数量(t)";

				ArrHeader[i][j++] = rs.getString(1);
				ArrHeader[i+1][j-1] = rs.getString(3);
				ArrHeader[i+2][j-1] = rs.getString(2);
			}else if(jizmc.equals(rs.getString(0))){
//						同一机组

				ArrHeader[i][j++] = rs.getString(1);
				ArrHeader[i+1][j-1] = rs.getString(3);
				ArrHeader[i+2][j-1] = rs.getString(2);
				//i=i+2;

			} else {
//						不同的机组
				i+=3;
				j=0;
				jizmc = rs.getString(0);
				ArrHeader[i][j++] = rs.getString(0);
				ArrHeader[i+1][j-1] = "煤位(％)";
				ArrHeader[i+2][j-1] = "数量(t)";

				ArrHeader[i][j++] = rs.getString(1);
				ArrHeader[i+1][j-1] = rs.getString(3);
				ArrHeader[i+2][j-1] = rs.getString(2);
			}


		}


		int ArrWidth[] = new int[col_count] ;
		for(int m=0;m<ArrWidth.length;m++)
			ArrWidth[m]=150;
		//		设置标题
		rt.createTitle(4, new int[]{600});
		rt.title.setCellFont(2, 1, "宋体", 14, true);
		rt.title.setCellValue(2, 1, "2.煤仓存量情况统计表");
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.title.setCellFont(4, 1, "仿宋", 12, false);
		rt.title.setCellValue(4, 1, "盘点时间:"+strPandrq);
		rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);

		rt.setBody(new Table(ArrHeader, 0, 0, 0));
		rt.body.setRowHeight(30);
		rt.body.setWidth(ArrWidth);

		for(int m=1;m<=rt.body.getRows();m++){
			for(int p=1;p<=rt.body.getCols();p++){
				rt.body.setCellAlign(m, p, Table.ALIGN_CENTER);
				rt.body.setCellFontSize(m, p, 11);
			}
		}
		_CurrentPage = 1;
		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rt.createFooter(1, ArrWidth);
		rt.footer.setCellValue(1, 1, "　　审核:");
		rt.footer.setCellAlign(1, 1, Table.ALIGN_LEFT);
		rt.footer.setCellFont(1, 1, "宋体", 12, false);
		rt.footer.setCellValue(1, 2, "　　制表:");
		rt.footer.setCellAlign(1, 2, Table.ALIGN_LEFT);
		rt.footer.setCellFont(1, 2, "宋体", 12, false);
		cn.Close();
		return rt.getAllPagesHtml_GD(5);

	}
	//煤堆积密度测试记录  
	public String meidjmdcs(){
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		long pandID =-1;
		if (getPandsjValue() == null) {// 把请选择去掉,有时候有空的时候,反回
			setPrintTable("");
			return "";
		} else {
			pandID = getPandsjValue().getId();
		}
		String strPandrq="";
		String danw ="";
		String sjsql = "select to_char(p.riq,'yyyy-mm-dd') as riq,to_char(p.kaisrq,'yyyy-mm-dd') as pmrq  from  pand_gd p where p.id="
				+ pandID;
		ResultSetList sjrs = con.getResultSetList(sjsql);

		if (sjrs.next()) {

			strPandrq = custom.FormatDate(custom.getDate(sjrs.getString("pmrq"),
					"yyyy-MM-dd"), "yyyy年MM月dd日");
		}
		String sql1="SELECT QUANC FROM DIANCXXB WHERE ID=(select diancxxb_id from pand_gd where id ="
				+ getPandbID() + ")";
		ResultSetList mingc =con.getResultSetList(sql1);
		if(mingc.next()){
			danw=mingc.getString("QUANC");
		}
		String sql =
				"   SELECT\n" +
						"   mc.mingc as mingc,\n" +
						" md.tongb,\n" +
						" md.zongz,\n" +
						" md.piz,\n" +
						" md.jingz,\n" +
						" md.rongj,\n" +
						" md.mid,\n" +
						" --md.mid\n" +
						"(SELECT decode(SUM(jingz),0,0,round(SUM(mid*jingz)/SUM(jingz),3))\n" +
						"FROM meicdjmdcdb md WHERE md.meicb_id=mc.id AND md.pand_gd_id=p.id) AS pingjmd\n" +
						"\n" +
						"   FROM pand_gd p,meicdjmdcdb md,meicb mc\n" +
						"   WHERE p.id=md.pand_gd_id AND md.meicb_id= mc.id\n" +
						"   AND  p.id= "+this.getPandsjValue().getId()+"\n" +
						"   ORDER BY mingc,tongb";

		ResultSetList rs=con.getResultSetList(sql);
		Report rt = new Report();
		String ArrHeader[][]=new String[1][8];
		int ArrWidth[] = new int[] {100,80,80,80,80,80,80,70};

		ArrHeader[0]=new String[]{"煤场（堆）","桶别","总重<br>(kg)","皮重<br>(kg)","净重<br>(kg)","容积<br>(m3)","密度<br>(t/m3)","平均密度"};
		rt.setBody(new Table(rs, 1, 0, 1));

		rt.body.setWidth(ArrWidth);

		rt.body.setRowCells(1, Table.PER_FONTBOLD, true);
		for(int m=1;m<=rt.body.getRows();m++){
			for(int p=1;p<=rt.body.getCols();p++){
				rt.body.setCellAlign(m, p, Table.ALIGN_CENTER);
				rt.body.setCellFontSize(m, p, 12);
			}
		}
		rt.body.setHeaderData(ArrHeader);
		// rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.merge(1, 8, rt.body.getRows(), 8);

		rt.body.setRowHeight(50);
		rt.body.setRowHeight(1, 60);


		//	设置标题
		rt.createTitle(4, new int[]{600});
		rt.title.setCellFont(2, 1, "宋体", 16, true);
		rt.title.setCellValue(2, 1,"3."+danw+"煤堆积密度测试记录");
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.title.setCellFont(4, 1, "仿宋", 12, false);
		rt.title.setCellValue(4, 1, "盘点时间:"+strPandrq);
		rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);

		rt.createFooter(1, ArrWidth);
		rt.footer.setCellValue(1, 1, "　　审核:",2);
		rt.footer.setCellAlign(1, 1, Table.ALIGN_LEFT);
		rt.footer.setCellFont(1, 1, "宋体", 12, false);
		rt.footer.setCellValue(1, 7, "　　制表:",2);
		rt.footer.setCellAlign(1, 7, Table.ALIGN_LEFT);
		rt.footer.setCellFont(1, 7, "宋体", 12, false);
		con.Close();
		//_AllPages=7;
		return rt.getAllPagesHtml_GD(6);
	}
//月度途损、场损量走势图


	//	第一：建立DELETEfile
	private static void deleteFile(File dir) throws IOException {// 册除文件夹及文件夹下所有内容
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir
					+ " is not a directory. ");
		}
		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				deleteFile(entries[i]);
			} else {
				entries[i].delete();
			}
		}
		dir.delete();
	}

	//	第二：写getImgServletPath方法
	public String getImgServletPath(String name) {
		Visit visit = (Visit) getPage().getVisit();
		return ("<img width=550 height=350 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/img/BizdbPicture"
				+ visit.getRenyID() + "/" + name + "' />");
	}
	public String getImgServletPath1(String name) {
		Visit visit = (Visit) getPage().getVisit();
		return ("<img width=550 height=350 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/img/BizdbPicture1"
				+ visit.getRenyID() + "/" + name + "' />");
	}

//	第三：	先在指定路径下创建一个图片文件

	//
	int num = 0;// 设置一个值自动增长，防止在一次访问中不刷新图形设置
	String meikmc;
	//	2010年度
	private String getYudCszst() {

		CustomDate custom = new CustomDate();
		String Nianf ="";
		CreateChartFile(createChart1(), "YudCszsts" + num);
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String sql = "select to_char(p.riq,'yyyy') as nian  from  pand_gd p where p.id="
				+ getPandsjValue().getId() + "";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			Nianf = custom.FormatDate(custom.getDate(rs.getString("nian"),
					"yyyy"), "yyyy");
		}
		int[] colWidth = { 600 };

//		设置标题
		rt.createTitle(2, colWidth);
		rt.title.setCellFont(1, 1, "宋体", 14, true);
		rt.title.setCellValue(1, 1, "(2)月度途损、场损量走势图",colWidth.length);
		rt.title.setCellAlign(1, 1, Table.ALIGN_LEFT);
		rt.title.setCellFont(2, 1, "宋体", 12, true);
		rt.title.setCellValue(2, 1, (Integer.parseInt(Nianf)-1) +"年度月度途损、场损量走势图",colWidth.length);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setCellspacing(10);

		rt.setBody(new Table(3, 1));// 表身
		rt.body.setWidth(colWidth);
		rt.body.setCellValue(1, 1, getImgServletPath("YudCszsts" + num
				+ ".jpg"));// 表中的图

		num=0;
		num++;

		custom = new CustomDate();
		Nianf ="";
		CreateChartFile1(createChart(), "YudCszst" + num);

		sql = "select to_char(p.riq,'yyyy') as nian  from  pand_gd p where p.id="
				+ getPandsjValue().getId() + "";
		ResultSetList rs1 = con.getResultSetList(sql);
		if(rs1.next()){
			Nianf = custom.FormatDate(custom.getDate(rs.getString("nian"),
					"yyyy"), "yyyy");
		}

		rt.body.setCellValue(2, 1, (Integer.parseInt(Nianf)) +"年度月度途损、场损量走势图");
		rt.body.setCellFont(2, 1,"宋体", 12, true);
		rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.body.setRowHeight(2, 50);
		rt.body.setBorder(0, 0, 0, 0);
		rt.body.setCellBorder(1, 1, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 1, 0, 0, 0, 0);
		rt.body.setCellBorder(3, 1, 0, 0, 0, 0);
		rt.body.setCellValue(3, 1, getImgServletPath1("YudCszst" + num
				+ ".jpg"));// 表中的图
		_AllPages=9;
		return rt.getAllPagesHtml_GD(8);
	}

	//	2009年度
	int num1=0;
	private String getYudCszst1() {
		num1++;

		CustomDate custom = new CustomDate();
		String Nianf ="";
		CreateChartFile(createChart1(), "YudCszsts" + num1);
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String sql = "select to_char(p.riq,'yyyy') as nian  from  pand_gd p where p.id="
				+ getPandsjValue().getId() + "";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			Nianf = custom.FormatDate(custom.getDate(rs.getString("nian"),
					"yyyy"), "yyyy");
		}
		int[] colWidth = { 600 };

//		设置标题
		rt.createTitle(2, colWidth);
		rt.title.setCellFont(1, 1, "宋体", 12, true);
		rt.title.setCellValue(1, 1, "(2)月度途损、场损量走势图",colWidth.length);
		rt.title.setCellAlign(1, 1, Table.ALIGN_LEFT);
		rt.title.setCellFont(2, 1, "宋体", 12, true);
		rt.title.setCellValue(2, 1, (Integer.parseInt(Nianf)-1) +"年度月度途损、场损量走势图",colWidth.length);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setCellspacing(10);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("YudCszsts" + num1
				+ ".jpg"));// 表中的图

		// 设置页数

		return rt.getAllPagesHtml_GD(8);
	}

	//	第四：Jfreechart生成图形的方法:2010年度
	private JFreeChart createChart() {
		// create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		String Nianf ="";
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		String sql1 = "select to_char(p.riq,'yyyy') as nian  from  pand_gd p where p.id="
				+ getPandsjValue().getId() + "";
		ResultSetList rs1 = con.getResultSetList(sql1);
		if(rs1.next()){
			Nianf = custom.FormatDate(custom.getDate(rs1.getString("nian"),
					"yyyy"), "yyyy");
		}

		ResultSetList dcrs=con.getResultSetList("select diancxxb_id from pand_gd where id="+getPandbID());
		String strDiancxxbId= this.getTreeid();
		if(dcrs.next()){
			strDiancxxbId=dcrs.getString("diancxxb_id");
		}
		StringBuffer sql = new StringBuffer();
//		优化SQL的消耗。优化后消耗由110降至9
		sql.append(
				"SELECT Y.YUEF || '月', S.YUNS, S.SHIJHYLZCSH\n" +
						"  FROM (SELECT TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01', 'MM'), ROWNUM - 1),\n" +
						"                                 'MM')) YUEF\n" +
						"          FROM DUAL\n" +
						"        CONNECT BY ROWNUM <=\n" +
						"                   MONTHS_BETWEEN(TO_DATE('12', 'mm'), TO_DATE('01', 'mm')) + 1) Y,\n" +
						"       (SELECT TO_CHAR(PD.KAISRQ, 'mm') AS RIQ,\n" +
						"               SUM(ZM.YUNS) AS YUNS,\n" +
						"               SUM(ZM.JITCS) AS SHIJHYLZCSH\n" +
						"          FROM PAND_GD PD, PANDZMB ZM\n" +
						"         WHERE ZM.PAND_GD_ID = PD.ID\n" +
						"           AND TO_CHAR(PD.KAISRQ, 'yyyy') = '"+Nianf+"'\n" +
						"           AND PD.DIANCXXB_ID = "+strDiancxxbId+"\n" +
						"         GROUP BY TO_CHAR(PD.KAISRQ, 'mm')) S\n" +
						" WHERE Y.YUEF = S.RIQ(+)\n" +
						" ORDER BY Y.YUEF");
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				if(rs.getDouble(3) == 0){
					dataset1.addValue(0, "场损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}else{
					dataset1.addValue(rs.getDouble(3), "场损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
				if(rs.getDouble(2) == 0){
					dataset2.addValue(0, "途损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}else{
					dataset2.addValue(rs.getDouble(2), "途损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见

		CategoryPlot plot = new CategoryPlot();

		CategoryItemRenderer renderer1 = new LineAndShapeRenderer();
		renderer1.setItemLabelGenerator(generator);
		renderer1.setItemLabelsVisible(true);
		plot.setDataset(dataset1);
		plot.setRenderer(renderer1);

		NumberAxis Number = new NumberAxis("");
		Number.setAutoTickUnitSelection(false);
		Number.setTickUnit(new NumberTickUnit(200));
		Number.setRangeWithMargins(0, 1200);

		plot.setDomainAxis(new CategoryAxis(""));// 设置x axis
		plot.setRangeAxis(Number);// 设置y axis

		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1,dataset2);
		plot.setRenderer(1,renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// 设置右边的纵轴为1的轴

		// create the third dataset and renderer...
		NumberAxis rangeAxis2 = new NumberAxis("");
		rangeAxis2.setAutoTickUnitSelection(false);
		rangeAxis2.setTickUnit(new NumberTickUnit(200));
		rangeAxis2.setRangeWithMargins(0, 1200);
		plot.setRangeAxis(1, rangeAxis2);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(false);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		plot.setNoDataMessage("在您设置的条件中没有相关数据!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}
	//	Jfreechart生成图形的方法:2009年度
	private JFreeChart createChart1() {
		// create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		String Nianf ="";
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		String sql1 = "select to_char(p.riq,'yyyy') as nian  from  pand_gd p where p.id="
				+ getPandsjValue().getId() + "";
		ResultSetList rs1 = con.getResultSetList(sql1);
		if(rs1.next()){
			Nianf = custom.FormatDate(custom.getDate(rs1.getString("nian"),
					"yyyy"), "yyyy");
		}
		ResultSetList dcrs=con.getResultSetList("select diancxxb_id from pand_gd where id="+getPandbID());
		String strDiancxxbId= this.getTreeid();
		if(dcrs.next()){
			strDiancxxbId=dcrs.getString("diancxxb_id");
		}
		StringBuffer sql = new StringBuffer();
//		优化SQL的消耗。优化后消耗由110降至9
		sql.append(
				"SELECT Y.YUEF || '月', S.YUNS, S.SHIJHYLZCSH\n" +
						"  FROM (SELECT TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01', 'MM'), ROWNUM - 1),\n" +
						"                                 'MM')) YUEF\n" +
						"          FROM DUAL\n" +
						"        CONNECT BY ROWNUM <=\n" +
						"                   MONTHS_BETWEEN(TO_DATE('12', 'mm'), TO_DATE('01', 'mm')) + 1) Y,\n" +
						"       (SELECT TO_CHAR(PD.KAISRQ, 'mm') AS RIQ,\n" +
						"               SUM(ZM.YUNS) AS YUNS,\n" +
						"               SUM(ZM.JITCS) AS SHIJHYLZCSH\n" +
						"          FROM PAND_GD PD, PANDZMB ZM\n" +
						"         WHERE ZM.PAND_GD_ID = PD.ID\n" +
						"           AND TO_CHAR(PD.KAISRQ, 'yyyy') = '"+(Integer.parseInt(Nianf)-1)+"'\n" +
						"           AND PD.DIANCXXB_ID = "+strDiancxxbId+"\n" +
						"         GROUP BY TO_CHAR(PD.KAISRQ, 'mm')) S\n" +
						" WHERE Y.YUEF = S.RIQ(+)\n" +
						" ORDER BY Y.YUEF");

		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				if(rs.getDouble(3) == 0){
					dataset1.addValue(0, "场损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}else{
					dataset1.addValue(rs.getDouble(3), "场损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
				if(rs.getDouble(2) == 0){
					dataset2.addValue(0, "途损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}else{
					dataset2.addValue(rs.getDouble(2), "途损",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见

		CategoryPlot plot = new CategoryPlot();

		CategoryItemRenderer renderer1 = new LineAndShapeRenderer();
		renderer1.setItemLabelGenerator(generator);
		renderer1.setItemLabelsVisible(true);
		plot.setDataset(dataset1);
		plot.setRenderer(renderer1);

		NumberAxis Number = new NumberAxis("");
		Number.setAutoTickUnitSelection(false);
		Number.setTickUnit(new NumberTickUnit(200));
		Number.setRangeWithMargins(0, 1200);

		plot.setDomainAxis(new CategoryAxis(""));// 设置x axis
		plot.setRangeAxis(Number);// 设置y axis

		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1,dataset2);
		plot.setRenderer(1,renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// 设置右边的纵轴为1的轴

		// create the third dataset and renderer...
		NumberAxis rangeAxis2 = new NumberAxis("");
		rangeAxis2.setAutoTickUnitSelection(false);
		rangeAxis2.setTickUnit(new NumberTickUnit(200));
		rangeAxis2.setRangeWithMargins(0, 1200);
		plot.setRangeAxis(1, rangeAxis2);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(false);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		plot.setNoDataMessage("在您设置的条件中没有相关数据!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}
	//	       第五：
	public String CreateChartFile(JFreeChart chart, String filebassname) {
		try {
			Visit visit = (Visit) getPage().getVisit();
			String FilePath = MainGlobal.getServletPath(getPage())
					+ "/img/BizdbPicture" + visit.getRenyID();
			File dir = new File(FilePath.replaceAll("\\\\", "/"));
			if (dir.exists()) {
				deleteFile(dir);// 册除文件及文件夹下所在的文件
				dir.mkdir();
			} else {
				dir.mkdirs();
			}
			File chartFile = new File(dir, filebassname + ".jpg");
			ChartUtilities.saveChartAsJPEG(chartFile, chart, 790, 400);
			return getImgServletPath(chartFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public String CreateChartFile1(JFreeChart chart, String filebassname) {
		try {
			Visit visit = (Visit) getPage().getVisit();
			String FilePath = MainGlobal.getServletPath(getPage())
					+ "/img/BizdbPicture1" + visit.getRenyID();
			File dir = new File(FilePath.replaceAll("\\\\", "/"));
			if (dir.exists()) {
				deleteFile(dir);// 册除文件及文件夹下所在的文件
				dir.mkdir();
			} else {
				dir.mkdirs();
			}
			File chartFile = new File(dir, filebassname + ".jpg");
			ChartUtilities.saveChartAsJPEG(chartFile, chart, 790, 400);
			return getImgServletPath(chartFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
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
		String sql = "select riq from pand_gd where id=" + getPandbID();
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

	private boolean blnIsBegin = false;

	private String printTable = "";

	private void setPrintTable(String str) {
		printTable = str;
	}

	public String getPrintTable() throws SQLException {
		if (!blnIsBegin) {
			return "";
		}
		setPrintTable("");
		blnIsBegin = false;
		if (this.getPandsjValue().getId() == -1) {
			return "";
		} else {
			StringBuffer str=new StringBuffer();
//			首页页面报表
			str.append(pandgbReport());
//			盘点人员报表
			str.append(Pandrybb());
//			盘点事项说明表
			str.append(pandsxbb());
//			盘点汇总表
			str.append(pandhzb());
//			 煤厂堆形测试记录表
			str.append(panddxcsbb());
//			煤仓存量情况统计表
			str.append(Meicclqktjb());
//			煤堆积密度测试记录  
			str.append(meidjmdcs());
//			煤厂累计库存及盈亏报表
			str.append(MeicLjkcyk());
//			月度途损、场损量走势图
			str.append(getYudCszst());
			return str.toString();
		}

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
			visit.setProSelectionModel12(null);
			visit.setProSelectionModel14(null);
			setPandsjValue(null);
			setNianfValue(null);
			setNianfModel(null);
			setYuefValue(null);
			setYuefModel(null);
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

		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));

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
//		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
		getIPandsjModels();
//		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getIPandsjModels() {
		String beginsj=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		Date lastday=DateUtil.getLastDayOfMonth(DateUtil.getDate(beginsj));
		String endsj=DateUtil.Formatdate("yyyy-MM-dd", lastday);
		String strGongsID = "";
		if(getDiancTreeJib()==3){
			strGongsID = " and (dc.id= " + this.getTreeid() +" or dc.fuid= " +this.getTreeid() +" )";
		}else if(getDiancTreeJib()==2){
			strGongsID = " and (dc.fuid= " + this.getTreeid() +" or dc.fuid in ( select id from vwdianc where fuid=" +this.getTreeid() +") )";
		}
		String sql = "select pd.id,bianm as bianm from pand_gd pd,vwdianc dc"
				+ " where pd.riq<=date'"+endsj+"' and pd.riq>=date'"+beginsj+"' and " +
				" pd.diancxxb_id=dc.id" + strGongsID
				+ " order by pd.bianm desc";
		JDBCcon cn=new JDBCcon();
		ResultSetList rs=cn.getResultSetList(sql);
		if(rs.getRows()==0){
			((Visit) getPage().getVisit())
					.setProSelectionModel2(new IDropDownModel(sql,"请添加盘点编码"));
		}else
		{
			((Visit) getPage().getVisit())
					.setProSelectionModel2(new IDropDownModel(sql));
		}
		rs.close();
		cn.Close();

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
		diancxxbId = this.getTreeid();
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
		return new File(MainGlobal.getXitsz(null, "" + this.getTreeid(),
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

	public static String NnvlStr(String strValue) {
		if (strValue == null) {
			return "0";
		} else if (strValue.equals("null")) {
			return "0";
		} else if (strValue.trim().equals("")) {
			return "0";
		}

		return strValue;
	}

	public class ITable extends Table {

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

			return sb.toString();
		}
	}
	//	 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
}