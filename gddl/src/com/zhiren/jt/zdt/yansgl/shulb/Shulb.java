//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.jt.zdt.yansgl.shulb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shulb extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		// return getQibb();
		if (getLeixSelectValue().getValue().equals("分厂")) {
			return getFenctj();
		} else if (getLeixSelectValue().getValue().equals("分矿")) {
			return getFenctj();
		} else if (getLeixSelectValue().getValue().equals("棋盘表")) {
			return getQibb();
		} else {
			return "无此报表";
		}
	}

	// 得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

	}

	private String getQibb() {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		String riq = FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1 = FormatDate(DateUtil.getDate(getEndriqDate()));
		// 电厂条件
		int jib = this.getDiancTreeJib();
		String diancCondition = "";
		if (jib == 1) {
			diancCondition = ",diancxxb gs where gs.fuid=dc.id and gs.id=fh.diancxxb_id \n ";
			// }else if(){

		} else {
			diancCondition = " where dc.id=fh.diancxxb_id and fh.diancxxb_id in (select id\n"
					+ " from(\n"
					+ " select id from diancxxb\n"
					+ " start with id="
					+ getTreeid()
					+ "\n"
					+ " connect by fuid=prior id\n"
					+ " )\n"
					+ " union\n"
					+ " select id\n"
					+ " from diancxxb\n"
					+ " where id="
					+ getTreeid() + ")";
		}
		// 供应商条件
		// String gongysCondition=" and fh.gongysb_id in(select id from gongysb
		// where fuid="+getGongysDropDownValue().getId() +"\n)";
		// String gongysid=" and fh.gongysb_id
		// ="+getGongysDropDownValue().getId()+"\n";
		String gongysfuid = "(select fuid from gongysb where id="
				+ getGongysDropDownValue().getId() + "\n)";
		long fuid = 0;
		ResultSet rs1 = conn.getResultSet(gongysfuid);
		try {
			if (rs1.next()) {
				fuid = rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrWidth = new int[] { 120, 60, 80, 80, 80, 80, 80, 80 };

		String gongystj = "";
		if (getGongysDropDownValue().getId() == -1) {
			gongystj = ",(select xgys.id as id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc\n"
					+ "  from gongysb xgys,gongysb dgys\n"
					+ "  where xgys.fuid=dgys.id(+))gys";

		} else {
			gongystj = ",(select gys.id,gys.xuh,gys.mingc from gongysb gys  where (gys.id="
					+ getGongysDropDownValue().getId()
					+ " or  gys.fuid="
					+ getGongysDropDownValue().getId() + "))gys ";

		}
		StringBuffer strRow = new StringBuffer();
		strRow
				.append("select decode(grouping(gys.mingc),1,'小计',gys.mingc) as 供应商 \n");
		strRow.append("  from fahb fh,diancxxb dc--,gongysb gys  \n");
		// strRow.append(" where dc.id=fh.diancxxb_id \n");
		strRow.append(gongystj);
		strRow.append(diancCondition);
		strRow.append("     and daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')  \n");
		strRow.append("     and daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd') \n");
		strRow.append("		and fh.gongysb_id=gys.id \n");

		strRow.append("  group by rollup(gys.mingc)  \n");
		strRow
				.append("  order by grouping(gys.mingc) desc,min(gys.xuh) ,gys.mingc \n");

		StringBuffer strCol = new StringBuffer();
		strCol
				.append("select decode(grouping(dc.mingc),1,'小计',0,dc.mingc,'小计') as 电厂  \n");
		strCol.append("from fahb fh,diancxxb dc--,gongysb gys  \n");
		strCol.append(gongystj);
		strCol.append(diancCondition);
		strCol.append("     and fh.gongysb_id=gys.id  \n");
		strCol.append("     and daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')  \n");
		strCol.append("     and daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd')  \n");
		strCol.append("group by rollup(dc.mingc)  \n");
		strCol
				.append("order by grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");

		StringBuffer sbsql = new StringBuffer();
		sbsql
				.append("select grouping(gys.mingc) as rowjb,grouping(dc.mingc) as coljb, \n");
		sbsql
				.append("    decode(grouping(gys.mingc),0,gys.mingc,'小计') as 供应商, \n");
		sbsql
				.append("    decode(grouping(dc.mingc),0,dc.mingc,2,'小计','小计') as 电厂, \n");
		sbsql
				.append("    sum(decode(daohrq,to_date('"
						+ getEndriqDate()
						+ "','yyyy-mm-dd'),round(jingz),0)) as 当日 ,sum(round(jingz)) as 累计 \n");
		sbsql.append("from fahb fh,diancxxb dc--,gongysb gys \n");
		sbsql.append(gongystj);
		sbsql.append(diancCondition);
		sbsql.append("     and daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')  \n");
		sbsql.append("     and daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd')  \n");
		sbsql.append("	   and fh.gongysb_id=gys.id \n");
		sbsql.append("     group by cube(gys.mingc ,dc.mingc)  \n");
		sbsql.append("  --  having  not (grouping(dc.mingc)=0 ) \n");
		sbsql
				.append("     order by grouping(gys.mingc) desc,min(gys.xuh) ,gys.mingc, \n");
		sbsql
				.append("           grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
		 System.out.println("QIP"+sbsql);
		cd.setRowNames("供应商");
		cd.setColNames("电厂");
		cd.setDataNames("当日,累计");
		cd.setDataOnRow(true);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		ArrWidth[0] = 120;
		ArrWidth[1] = 120;
		//
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		// rt.body.ShowZero=true;
		rt.setTitle("入厂煤计量统计" + "(棋盘表)", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt
				.setDefaultTitle(1, 2, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, riq + "-" + riq1, Table.ALIGN_RIGHT);
		rt.body.setPageRows(22);
		for (int i = 3; i <= rt.body.getCols(); i++) {
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		if (rt.body.getRows() > 0 && rt.body.getCols() > 0) {
			rt.body.setCellValue(1, 1, "单位");
			rt.body.setCellValue(1, 2, "单位");
		}
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2,
				"打印日期:"
						+ FormatDate(DateUtil.getDate(DateUtil
								.FormatDate(new Date()))), Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 1, "单位:车、吨", Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(3,1,"审核",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(5,2,"制表",Table.ALIGN_LEFT);
		cd.DataTable.setColAlign(2, Table.ALIGN_CENTER);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}

	// 入煤计量分厂统计报表
	private String getFenctj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		Visit visit = (Visit) getPage().getVisit();
		String riq = FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1 = FormatDate(DateUtil.getDate(getEndriqDate()));
		// 电厂条件
		int jib = this.getDiancTreeJib();
		String diancCondition = "and fh.diancxxb_id in (select id\n"
				+ " from(\n" + " select id from diancxxb\n" + " start with id="
				+ getTreeid() + "\n" + " connect by fuid=prior id\n" + " )\n"
				+ " union\n" + " select id\n" + " from diancxxb\n"
				+ " where id=" + getTreeid() + ")";
		// 供应商条件
		String gongysid = "and fh.gongysb_id ="
				+ getGongysDropDownValue().getId();
		String gongysfuid = "(select fuid from gongysb where id="
				+ getGongysDropDownValue().getId() + ")";
		long fuid = 0;
		ResultSet rs1 = con.getResultSet(gongysfuid);
		try {
			if (rs1.next()) {
				fuid = rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		String gongysCondition = "and fh.gongysb_id in(select id from gongysb where (fuid="
				+ getGongysDropDownValue().getId()
				+ " or id="
				+ getGongysDropDownValue().getId() + "))";
		String gongysConditionk = "";
		// 分厂
		if (getLeixSelectValue().getId() == 1) {
			sbsql.append("       from \n");
			sbsql.append("         (select diancxxb_id,fx.fenx,fx.xuh from \n");
			sbsql
					.append("             (select distinct diancxxb_id from fahb \n");
			sbsql.append("                     where daohrq>=to_date('"
					+ getBeginriqDate()
					+ "','yyyy-mm-dd') and daohrq<=to_date('" + getEndriqDate()
					+ "','yyyy-mm-dd') \n");
			sbsql
					.append("             ) fh,(select decode(1,1,'当日') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc \n");
			sbsql.append("             where dc.id=fh.diancxxb_id   \n");
			sbsql.append(diancCondition);
			// sbsql.append(gongysCondition);
			sbsql.append("				) fx,");
			sbsql.append("          ( \n");
			sbsql
					.append("          select decode(1,1,'当日') as fenx,dc.id,dc.mingc,fh.biaoz as biaoz,fh.jingz as jingz,fh.yuns as yuns,fh.yingd as yingd,(fh.biaoz+fh.yingd-fh.yuns-fh.jingz) as kuid,fh.koud as koud  \n");
			sbsql.append("                 from fahb fh,diancxxb dc \n");
			sbsql.append("                 where fh.diancxxb_id=dc.id  \n");
			sbsql.append("                       and fh.daohrq=to_date('"
					+ getEndriqDate() + "','yyyy-mm-dd') \n");
			sbsql.append(diancCondition);
			// sbsql.append(gongysCondition);
			sbsql.append("          union  \n");
			sbsql
					.append("          select decode( 1,1,'累计') as fenx,dc.id,dc.mingc,sum(round(fh.biaoz,0)) as biaoz,sum(round(fh.jingz,0)) as jingz,sum(round(fh.yuns,0)) as yuns,sum(round(fh.yingd,0)) as yingd, \n");
			sbsql
					.append("                      sum(round(fh.biaoz,0)+round(fh.yingd,0)-round(fh.yuns,0)-round(fh.jingz,0)) as kuid,sum(round(fh.koud,0)) as koud \n");
			sbsql.append("                 from fahb fh,diancxxb dc \n");
			sbsql.append("                 where fh.diancxxb_id=dc.id   \n");
			sbsql.append("                       and fh.daohrq>=to_date('"
					+ getBeginriqDate()
					+ "','yyyy-mm-dd') and fh.daohrq<=to_date('"
					+ getEndriqDate() + "','yyyy-mm-dd') \n");
			sbsql.append(diancCondition);
			// sbsql.append(gongysCondition);
			sbsql.append("                 group by (dc.id,dc.mingc) \n");
			sbsql.append("         )fah, \n");
			String sbsqlz = sbsql.toString();
			sbsql.setLength(0);
			if (jib == 3) {
				sbsql
						.append("select dc.mingc as diancmc,decode(1,1,fx.fenx,'') as fenx,sum(fah.biaoz), \n");
				sbsql
						.append("                 sum(fah.jingz),sum(fah.yuns),sum(fah.yingd) as yingd,sum(fah.kuid),sum(fah.koud)  \n");
				sbsql.append(sbsqlz);
				sbsql.append("	 diancxxb dc \n");
				sbsql
						.append("   where fx.diancxxb_id=dc.id and fx.fenx=fah.fenx(+) and fah.id(+)=fx.diancxxb_id   \n");
				sbsql.append("   group by rollup(dc.mingc,fx.fenx) \n");
				sbsql.append("   having not grouping(fx.fenx)=1 \n");
				sbsql.append("   order by  \n");
				sbsql
						.append("         grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) \n");
			} else {
				sbsql
						.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp'||dc.mingc) as diancmc,decode(1,1,fx.fenx,'') as fenx,sum(fah.biaoz), \n");
				sbsql
						.append("                 sum(fah.jingz),sum(fah.yuns),sum(fah.yingd) as yingd,sum(fah.kuid),sum(fah.koud)  \n");
				sbsql.append(sbsqlz);
				sbsql.append("	 diancxxb dc,diancxxb gs \n");
				sbsql
						.append("   where fx.diancxxb_id=dc.id and fx.fenx=fah.fenx(+) and fah.id(+)=fx.diancxxb_id and dc.fuid=gs.id  \n");
				sbsql
						.append("   group by rollup(fx.fenx,gs.mingc,dc.mingc) \n");
				sbsql.append("   having not grouping(fx.fenx)=1 \n");
				sbsql
						.append("   order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc, \n");
				sbsql
						.append("         grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) \n");
			}

		} else if (getLeixSelectValue().getId() == 2) {// 分矿
			sbsql.append("     from \n");
			sbsql
					.append("        (select diancxxb_id,gongysb_id,fx.fenx,fx.xuh from  \n");
			sbsql
					.append("             (select distinct diancxxb_id,gongysb_id from fahb  \n");
			sbsql.append("                     where daohrq>=to_date('"
					+ getBeginriqDate()
					+ "','yyyy-mm-dd') and daohrq<=to_date('" + getEndriqDate()
					+ "','yyyy-mm-dd')  \n");
			sbsql
					.append("             ) fh,(select decode(1,1,'当日') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc  \n");
			sbsql.append("             where dc.id=fh.diancxxb_id    \n");
			sbsql.append(diancCondition);
			if (fuid == 0) {
				if (getGongysDropDownValue().getId() == -1) {
					sbsql.append(gongysConditionk);
				} else {
					sbsql.append(gongysCondition);
				}
			} else {
				sbsql.append(gongysid);
			}
			sbsql.append(") fx,       \n");
			sbsql.append("           ( \n");
			sbsql
					.append("           select decode(1,1,'当日')as fenx,dc.id as id,gy.id as gy_id,gy.mingc,sum(fh.biaoz) as biaoz,sum(fh.jingz) as jingz,sum(fh.yuns) as yuns,sum(fh.yingd) as yingd, \n");
			sbsql
					.append("                      sum(fh.biaoz+fh.yingd-fh.yuns-fh.jingz) as kuid,sum(fh.koud) as koud \n");
			sbsql
					.append("           from fahb fh,gongysb gy,shengfb sf,diancxxb dc  \n");
			sbsql
					.append("           where fh.gongysb_id=gy.id and gy.shengfb_id=sf.id and fh.diancxxb_id=dc.id \n");
			sbsql.append("                 and fh.daohrq=to_date('"
					+ getEndriqDate() + "','yyyy-mm-dd')  \n");
			sbsql.append(diancCondition);
			if (fuid == 0) {
				if (getGongysDropDownValue().getId() == -1) {
					sbsql.append(gongysConditionk);
				} else {
					sbsql.append(gongysCondition);
				}
			} else {
				sbsql.append(gongysid);
			}
			sbsql.append("           group by(dc.id,gy.id,gy.mingc) \n");
			sbsql.append("           union \n");
			sbsql
					.append("           select decode(1,1,'累计')as fenx,dc.id as id,gy.id as gy_id,gy.mingc,sum(round(fh.biaoz,0)) as biaoz,sum(round(fh.jingz,0)) as jingz,sum(round(fh.yuns,0)) as yuns,sum(round(fh.yingd,0)) as yingd, \n");
			sbsql
					.append("                      sum(round(fh.biaoz,0)+round(fh.yingd,0)-round(fh.yuns,0)-round(fh.jingz,0)) as kuid,sum(round(fh.koud,0)) as koud \n");
			sbsql
					.append("           from fahb fh,gongysb gy,shengfb sf,diancxxb dc  \n");
			sbsql
					.append("           where fh.gongysb_id=gy.id and gy.shengfb_id=sf.id and fh.diancxxb_id=dc.id \n");
			sbsql.append("                 and fh.daohrq>=to_date('"
					+ getBeginriqDate()
					+ "','yyyy-mm-dd') and fh.daohrq<=to_date('"
					+ getEndriqDate() + "','yyyy-mm-dd')  \n");
			sbsql.append(diancCondition);
			if (fuid == 0) {
				if (getGongysDropDownValue().getId() == -1) {
					sbsql.append(gongysConditionk);
				} else {
					sbsql.append(gongysCondition);
				}
			} else {
				sbsql.append(gongysid);
			}
			sbsql
					.append("           group by(dc.id,gy.id,gy.mingc)           \n");
			sbsql.append("           )fah \n");
			sbsql.append("           ,diancxxb dc,gongysb gy,shengfb sf \n");
			String sbsqlk = sbsql.toString();
			sbsql.setLength(0);
			if (jib == 3) {
				sbsql
						.append("     select decode(grouping(sf.quanc)+grouping(gy.mingc),2,'总计',1,sf.quanc,'&nbsp;&nbsp'||gy.mingc) as gonghdw,decode(1,1,fx.fenx,'') as fenx,sum(fah.biaoz), \n");
				sbsql
						.append("                 sum(fah.jingz),sum(fah.yuns),sum(fah.yingd) as yingd,sum(fah.kuid),sum(fah.koud)  \n");
				sbsql.append(sbsqlk);
				sbsql
						.append("       where fx.diancxxb_id=dc.id and gy.shengfb_id=sf.id    and fx.gongysb_id=gy.id\n");
				sbsql
						.append("             and fx.fenx=fah.fenx(+) and fx.diancxxb_id=fah.id(+) and fx.gongysb_id=fah.gy_id(+) \n");
				sbsql
						.append("       group by rollup(fx.fenx,sf.quanc,gy.mingc) \n");
				sbsql.append("       having not grouping(fx.fenx)=1 \n");
				sbsql
						.append("       order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,  \n");
				sbsql
						.append("         grouping(gy.mingc) desc,max(gy.xuh) ,gy.mingc,max(fx.xuh)  \n");
			} else {
				sbsql
						.append("     select decode(grouping(sf.quanc)+grouping(gy.mingc),2,'总计',1,sf.quanc,'&nbsp;&nbsp'||gy.mingc) as gonghdw,decode(1,1,fx.fenx,'') as fenx,sum(fah.biaoz), \n");
				sbsql
						.append("                 sum(fah.jingz),sum(fah.yuns),sum(fah.yingd) as yingd,sum(fah.kuid),sum(fah.koud)  \n");
				sbsql.append(sbsqlk);
				sbsql
						.append("       where fx.diancxxb_id=dc.id and gy.shengfb_id=sf.id  and fx.gongysb_id=gy.id\n");
				sbsql
						.append("             and fx.fenx=fah.fenx(+) and fx.diancxxb_id=fah.id(+) and fx.gongysb_id=fah.gy_id(+) \n");
				sbsql
						.append("       group by rollup(fx.fenx,sf.quanc,gy.mingc) \n");
				sbsql.append("       having not grouping(fx.fenx)=1 \n");
				sbsql
						.append("       order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,  \n");
				sbsql
						.append("         grouping(gy.mingc) desc,max(gy.xuh) ,gy.mingc,max(fx.xuh)  \n");
			}
		}
		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		int iFixedRows = 0;// 固定行号
		String[][] ArrHeader;
		int[] ArrWidth;
		if (getLeixSelectValue().getId() == 1) {// 分厂
			ArrHeader = new String[1][8];
			ArrHeader[0] = new String[] { "单位", "单位", "票重<br>(吨)", "净重<br>(吨)",
					"运损<br>(吨)", "盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
			// ArrHeader[1]=new String[]
			// {"单位","单位","标重<br>(吨)","净重<br>(吨)","运损<br>(吨)","盈吨<br>(吨)","亏吨<br>(吨)","扣吨<br>(吨)"};

			ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80 };

			Table tb = new Table(rs, 1, 0, 1);
			rt.setBody(tb);
			//
			// rt.body.ShowZero=true;
			rt.body.ShowZero = false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("入厂煤计量统计" + "(分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:"
					+ ((Visit) getPage().getVisit()).getDiancmc(),
					Table.ALIGN_LEFT);
			// rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(3, 3, riq + "-" + riq1, Table.ALIGN_RIGHT);
			rt.body.setPageRows(22);
			// rt.body.mergeCell(1, 1, 2, 2);
			// rt.body.mergeCol(1);
			// rt.body.mergeCol(2);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			if (rt.body.getRows() > 1) {
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}

			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 2, "打印日期:"
					+ FormatDate(DateUtil.getDate(DateUtil
							.FormatDate(new Date()))), Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "审核", Table.ALIGN_CENTER);
			rt.setDefautlFooter(6, 2, "制表", Table.ALIGN_LEFT);
			tb.setColAlign(2, Table.ALIGN_CENTER);
		} else if (getLeixSelectValue().getId() == 2) {// 分矿
			ArrHeader = new String[1][8];

			ArrHeader[0] = new String[] { "供货单位", "供货单位", "票重<br>(吨)",
					"净重<br>(吨)", "运损<br>(吨)", "盈吨<br>(吨)", "亏吨<br>(吨)",
					"扣吨<br>(吨)" };
			// ArrHeader[1]=new String[]
			// {"供货单位","累计","(吨)","(吨)","(吨)","(吨)","(吨)","(吨)"};
			ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80 };

			Table tb = new Table(rs, 1, 0, 1);
			rt.setBody(tb);
			//
			// rt.body.ShowZero=true;
			rt.body.ShowZero = false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("入厂煤计量统计" + "(分矿)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:"
					+ ((Visit) getPage().getVisit()).getDiancqc(),
					Table.ALIGN_LEFT);
			// rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(3, 3, riq + "-" + riq1, Table.ALIGN_RIGHT);
			rt.body.setPageRows(22);
			// rt.body.mergeCell(1, 1, 2, 1);
			// rt.body.mergeCol(1);
			// rt.body.mergeCol(2);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			if (rt.body.getRows() > 1) {
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 2, "打印日期:"
					+ FormatDate(DateUtil.getDate(DateUtil
							.FormatDate(new Date()))), Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "审核", Table.ALIGN_CENTER);
			rt.setDefautlFooter(6, 2, "制表", Table.ALIGN_LEFT);
			tb.setColAlign(2, Table.ALIGN_CENTER);
		}
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	// ________________________________________________________

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	// /////
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			// stra.set(DateUtil.getYear(new Date()), 0, 1);
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			// stra.setTime(new Date());
			stra.add(Calendar.MONTH, -1);

			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate() {
		if (((Visit) getPage().getVisit()).getString5() == null
				|| ((Visit) getPage().getVisit()).getString5() == "") {
			((Visit) getPage().getVisit()).setString5(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setEndriqDate(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1", "");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2", "");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
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

		tb1.addText(new ToolbarText("供货单位:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		// meik.setWidth(60);
		// meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
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
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
		}
		getToolbars();

		blnIsBegin = true;

	}

	// 供应商
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc\n" + "from gongysb\n";
		// +"where gongysb.fuid=0";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}

	// 类型
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);

	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分厂"));
		list.add(new IDropDownBean(2, "分矿"));
		list.add(new IDropDownBean(3, "棋盘表"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
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
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
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
}