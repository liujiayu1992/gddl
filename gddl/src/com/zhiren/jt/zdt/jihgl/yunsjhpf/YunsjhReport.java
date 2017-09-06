package com.zhiren.jt.zdt.jihgl.yunsjhpf;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
//import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
//import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
//import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author xzy
 */
public class YunsjhReport extends BasePage {

	// *********************报表设置************************//
	public boolean getRaw() {
		return true;
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

	private String RT_DR1 = "yunsjhtb";// 运输计划提报

	private String RT_DR2 = "yunsjhbctb";// 运输计划补充提报

	private String RT_DR3 = "yunsjhpf";// 运输计划批复

	private String RT_DR4 = "yunsjhbcpf";// 运输计划补充批复

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		long leib = 1;// 默认类别为1：计划数据；0：补充数据；
		String tablename = "";// 运输计划提报：yunsjhtbb；运输计划批复：yunsjhb
		String title = "";
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if(getBaoblxValue()!=null){
			if (getBaoblxValue().getValue().equals("运输计划提报查询")){
				leib = 1;
				tablename = "yunsjhtbb";
				title = "运输计划提报查询";
				return getSelectData(title, tablename, leib);
			}else if (getBaoblxValue().getValue().equals("运输计划补充提报查询")){
				leib = 0;
				tablename = "yunsjhtbb";
				title = "运输计划补充提报查询";
				return getSelectData(title, tablename, leib);
			}else if (getBaoblxValue().getValue().equals("运输计划批复查询")){
				leib = 1;
				tablename = "yunsjhb";
				title = "运输计划批复查询";
				return getSelectData(title, tablename, leib);
			}else if (getBaoblxValue().getValue().equals("运输计划补充批复查询")){
				leib = 0;
				tablename = "yunsjhb";
				title = "运输计划补充批复查询";
				return getSelectData(title, tablename, leib);
			} else {
				return "无此报表";
			}
		} else {
			return "无此报表";
		}

	}

	// *******************综合表设置******************//
	// *****************生成SQL语句*******************//
	/**
	 * 电厂信息表
	 */
	private String getDiancSQL() {
		StringBuffer str_SQL = new StringBuffer();
//		str_SQL.append(",(select diancid, diancmc, fengsid, fengsmc from \n");
//		str_SQL.append(" (select id as fengsid, mingc as fengsmc, fuid from diancxxb where fuid = 100) gs,\n");
//		str_SQL.append(" (select dc.id as diancid, dc.mingc as diancmc, fuid,shangjgsid from diancxxb dc where dc.fuid != 100) dc  \n");
//		str_SQL.append(" where dc.fuid = gs.fengsid or dc.shangjgsid=gs.fengsid) dianc \n");
		
		str_SQL.append(",(select d.id as diancid,d.xuh as xuh1,d.mingc as diancmc,d.jingjcml,d.rijhm,dc.id as fengsid,dc.mingc as fengsmc,sf.id as quygsid,\n");
		str_SQL.append("sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid \n");
		str_SQL.append("from diancxxb d, diancxxb dc, diancxxb sf \n");
		str_SQL.append("where d.jib = 3 and d.fuid = dc.id(+) and d.shangjgsid = sf.id(+)) dianc \n");
		
		
		
		
		return str_SQL.toString();
	}

	/**
	 * 生成报表查询语句 类型：分厂，分矿来汇总数据 参数：表名: tablename,
	 * 汇总名称：groupingmc，单位名称：danwmc，日期：riq；where:条件
	 * 
	 * @return 返回SQL查询语句
	 */
	private String getStrSQL(String tablename, String sum, String groupingmc,
			String where, String group, String order, String danwmc,
			String danwtj) {
		StringBuffer Str_sql = new StringBuffer();
		Str_sql.append("select \n");
		Str_sql.append(groupingmc);
		Str_sql
				.append("(select cz.mingc from chezxxb cz where y.daoz_id=cz.id) as daoz_id,pz.mingc,\n");
		Str_sql.append(sum);
		Str_sql.append("from \n");
		Str_sql.append(tablename).append(" y,gongysb g,pinzb pz,chezxxb cz \n");
		Str_sql.append(getDiancSQL());// dianc
		Str_sql.append(" where 1=1 ").append(where).append(danwtj);
		Str_sql.append(group);
		Str_sql
				.append(" having not (grouping("+danwmc+") || grouping(y.beiz)) =1 \n");
		Str_sql.append(order).append("\n");
		return Str_sql.toString();
	}

	private String getSelectData(String title, String tablename, long leix) {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		int iFixedRows = 1;// 固定行号

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		String riq = "" + intyear + "-" + StrMonth;// 得到选择日期

		String groupingmc = "";// 汇总名称
		String danwmc = "";// GROUP BY 的名称
		String where = "";// 汇总条件
		String group = "";// 按什么汇总
		String order = "";// 按什么排序
		String sum = "";// SUM数据内容
		// 标题日期
		String date = "" + intyear + "年" + StrMonth + "月";

		// 获得查询条件
		StringBuffer Strdanwtj = new StringBuffer();
		String danwtj = "";
		String strdiancid = "";// 电厂条件
		String titlename = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strdiancid = " ";
		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			strdiancid = "  and (fengsid=  " +this.getTreeid()+" or shangjgsid="+this.getTreeid()+")";
		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strdiancid = " and diancid= " + this.getTreeid();
		} else if (jib == -1) {
			strdiancid = " and diancid = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		Strdanwtj.append(strdiancid);
		String gongysmc = "";
		if (getGongysDropDownValue() != null
				&& getGongysDropDownValue().getId() != -1) {
			gongysmc = " and gongysb_id=" + getGongysDropDownValue().getId();
		}
		Strdanwtj.append(gongysmc);
		danwtj = Strdanwtj.toString();
		// 设置查询语句
		// leix 1:为正式计划；0:补充计划
		if (tablename.equals("yunsjhtbb")) {
			sum = " sum(y.yuanc) as yuanc,sum(y.yuand) as yuanc,y.beiz \n";
			if(getLeixSelectValue().getValue().equals("分厂")){
				ArrHeader = new String[1][7];
				ArrHeader[0] = new String[] { "单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数",  "备注" };
				ArrWidth = new int[] { 150, 80, 80, 80, 80, 80, 80 };
			}else if(getLeixSelectValue().getValue().equals("分矿")){
				ArrHeader = new String[1][7];
				ArrHeader[0] = new String[] { "单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数",  "备注" };
				ArrWidth = new int[] { 150, 80, 80, 80, 80, 80, 80 };
			}else if(getLeixSelectValue().getValue().equals("分厂分矿")){
				ArrHeader = new String[1][8];
				ArrHeader[0] = new String[] { "单位","供货单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数",  "备注" };
				ArrWidth = new int[] { 150,150,80, 80, 80, 80, 80, 80 };
			}else if(getLeixSelectValue().getValue().equals("分矿分厂")){
				ArrHeader = new String[1][8];
				ArrHeader[0] = new String[] { "供货单位","单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数",  "备注" };
				ArrWidth = new int[] { 150,150,80, 80, 80, 80, 80, 80 };
			}

		} else if (tablename.equals("yunsjhb")) {
			sum = " sum(y.yuanc) as yuanc,sum(y.yuand) as yuanc,\n"
					+ "sum(y.pic) as pic,sum(y.pid) as pid,y.beiz \n";//y.pizjhh,
			
			if(getLeixSelectValue().getValue().equals("分厂")){
				ArrHeader = new String[1][9];
				ArrHeader[0] = new String[] { "单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数", "批复车数", "批复吨数", "备注" };//"批准号",

				ArrWidth = new int[] { 150, 80, 80, 80, 80, 80, 80,  80, 80 };
			}else if(getLeixSelectValue().getValue().equals("分矿")){
				ArrHeader = new String[1][9];
				ArrHeader[0] = new String[] { "单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数", "批复车数", "批复吨数", "备注" };//"批准号",

				ArrWidth = new int[] { 150, 80, 80, 80, 80, 80, 80,  80, 80 };
			}else if(getLeixSelectValue().getValue().equals("分厂分矿")){
				ArrHeader = new String[1][10];
				ArrHeader[0] = new String[] { "单位","供货单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数", "批复车数", "批复吨数", "备注" };//"批准号",

				ArrWidth = new int[] { 150,150, 80, 80, 80, 80, 80, 80,  80, 80 };
			}else if(getLeixSelectValue().getValue().equals("分矿分厂")){
				ArrHeader = new String[1][10];
				ArrHeader[0] = new String[] { "供货单位","单位", "发站(港)", "到站(港)", "品种", "车数",
						"吨数", "批复车数", "批复吨数", "备注" };//"批准号",

				ArrWidth = new int[] { 150,150, 80, 80, 80, 80, 80, 80,  80, 80 };
			}
		}
		if (getLeixSelectValue() != null) {
			where = " and y.diancxxb_id=diancid and y.gongysb_id=g.id and y.pinm=pz.id(+) "
					+ "and y.faz_id=cz.id(+) and leix="
					+ leix
					+ " and to_char(y.riq,'yyyy-mm')='" + riq + "' \n";
			//**********************分厂*************************//
			if (getLeixSelectValue().getValue().equals("分厂")) {
				titlename = title + "(分厂)";
				if (jib == 3 || jib == -1) {// 按电厂分
					groupingmc = " decode(grouping(diancmc),1,'总计',diancmc) as dianwmc,\n"
							+ " decode(grouping(diancmc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
					danwmc = "cz.mingc";
					group = " group by rollup (diancmc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
					order = " order by grouping(diancmc) desc,diancmc,\n"
							+ " grouping(cz.mingc) desc,cz.mingc\n";
				} else if(jib ==2) {//分公司及燃料公司
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//燃料公司
							groupingmc = " decode(grouping(quygs) + grouping(diancmc),2,'总计',1,quygs,'&nbsp;&nbsp;'||diancmc) as dianwmc,\n"
								+ " decode(grouping(diancmc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
							danwmc = "cz.mingc";
							group = " group by rollup (quygs,diancmc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
							order = " order by grouping(quygs) desc,quygs,\n"
									+ " grouping(diancmc) desc,diancmc desc,grouping(cz.mingc) desc,cz.mingc\n";
						}else{//分公司
							groupingmc = " decode(grouping(fengsmc) + grouping(diancmc),2,'总计',1,fengsmc,'&nbsp;&nbsp;'||diancmc) as dianwmc,\n"
								+ " decode(grouping(diancmc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
							danwmc = "cz.mingc";
							group = " group by rollup (fengsmc,diancmc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
							order = " order by grouping(fengsmc) desc,fengsmc,\n"
									+ " grouping(diancmc) desc,diancmc desc,grouping(cz.mingc) desc,cz.mingc\n";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else{//集团
					groupingmc = " decode(grouping(fengsmc) + grouping(diancmc),2,'总计',1,fengsmc,'&nbsp;&nbsp;'||diancmc) as dianwmc,\n"
						+ " decode(grouping(diancmc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
					danwmc = "cz.mingc";
					group = " group by rollup (fengsmc,diancmc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
					order = " order by grouping(fengsmc) desc,fengsmc,\n"
							+ " grouping(diancmc) desc,diancmc desc,grouping(cz.mingc) desc,cz.mingc\n";
				}
			//**********************分矿*************************//				
			} else if (getLeixSelectValue().getValue().equals("分矿")) {
				titlename = title + "(分矿)";
				groupingmc = " decode(grouping(g.mingc),1,'总计',g.mingc) as dianwmc,\n"// '&nbsp;&nbsp;&nbsp;&nbsp;'||
						+ " decode(grouping(g.mingc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
				danwmc = "cz.mingc";
				group = " group by rollup (g.mingc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
				order = " order by grouping(g.mingc) desc,g.mingc,"
						+ " grouping(cz.mingc) desc,cz.mingc\n";
		    //**********************分厂分矿*************************//				
			} else if (getLeixSelectValue().getValue().equals("分厂分矿")) {
				titlename = title + "(分厂分矿)";
				if (jib == 3 || jib == -1) {// 按电厂分
					groupingmc = " decode(grouping(diancmc),1,'总计',diancmc) as dianwmc,\n"
							+ " decode(grouping(diancmc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as gongysmc,\n"
							+ " decode(grouping(g.mingc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
					danwmc = "g.mingc";
					group = " group by rollup (diancmc,g.mingc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
					order = " order by grouping(diancmc) desc,diancmc,grouping(g.mingc) desc,g.mingc,\n"
							+ " grouping(cz.mingc) desc,cz.mingc\n";
				} else if (jib ==2) {//分公司及燃料公司
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//燃料公司
							groupingmc = " decode(grouping(quygs) + grouping(diancmc),2,'总计',1,quygs,'&nbsp;&nbsp;'||diancmc) as dianwmc,\n"
								+ " decode(grouping(diancmc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as gongysmc,\n"
								+ " decode(grouping(g.mingc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
							danwmc = "g.mingc";
							group = " group by rollup (quygs,diancmc,g.mingc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
							order = " order by grouping(quygs) desc,quygs,\n"
									+ " grouping(diancmc) desc,diancmc desc,grouping(g.mingc) desc,g.mingc,grouping(cz.mingc) desc,cz.mingc\n";
						}else{//分公司
							groupingmc = " decode(grouping(fengsmc) + grouping(diancmc),2,'总计',1,fengsmc,'&nbsp;&nbsp;'||diancmc) as dianwmc,\n"
								+ " decode(grouping(diancmc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as gongysmc,\n"
								+ " decode(grouping(g.mingc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
							danwmc = "g.mingc";
							group = " group by rollup (fengsmc,diancmc,g.mingc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
							order = " order by grouping(fengsmc) desc,fengsmc,\n"
									+ " grouping(diancmc) desc,diancmc desc,grouping(g.mingc) desc,g.mingc,grouping(cz.mingc) desc,cz.mingc\n";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				} else {//集团
					groupingmc = " decode(grouping(fengsmc) + grouping(diancmc),2,'总计',1,fengsmc,'&nbsp;&nbsp;'||diancmc) as dianwmc,\n"
						+ " decode(grouping(diancmc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as gongysmc,\n"
						+ " decode(grouping(g.mingc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
					danwmc = "g.mingc";
					group = " group by rollup (fengsmc,diancmc,g.mingc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
					order = " order by grouping(fengsmc) desc,fengsmc,\n"
							+ " grouping(diancmc) desc,diancmc desc,grouping(g.mingc) desc,g.mingc,grouping(cz.mingc) desc,cz.mingc\n";
				}
		    //**********************分矿分厂*************************//					
			} else if (getLeixSelectValue().getValue().equals("分矿分厂")){
				titlename = title + "(分矿分厂)";
				groupingmc = " decode(grouping(g.mingc),1,'总计',g.mingc) as gongysmc,\n"
						+ " decode(grouping(g.mingc)+grouping(diancmc),2,'',1,'小计',diancmc) as dianwmc,\n"
						+ " decode(grouping(diancmc)+grouping(cz.mingc),2,'',1,'小计',cz.mingc) as faz_id, \n";
				danwmc = "diancmc";
				group = " group by rollup (g.mingc,diancmc,cz.mingc,daoz_id,pz.mingc,y.beiz)\n";
				order = " order by grouping(g.mingc) desc,g.mingc,grouping(diancmc) desc,diancmc,\n"
						+ " grouping(cz.mingc) desc,cz.mingc\n";
			}
			// 汇总名称：groupingmc，单位名称：danwmc，当天日期：riq1，日期段：riq2；where:条件
			strSQL = getStrSQL(tablename, sum, groupingmc, where, group, order,
					danwmc, danwtj);
		}
		//System.out.print(strSQL);

		iFixedRows = 1;

		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs, 1, 0, iFixedRows));
		if (cn.getHasIt(strSQL)) {// 值
			rt.body.setCellAlign(2, 1, 1);// 总计居中
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
		}
		rt.setTitle(titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, date, Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		// rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
	}

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

	/*
	 * private String FormatDate(Date _date) { if (_date == null) { return ""; }
	 * return DateUtil.Formatdate("yyyy年MM月dd日", _date); }
	 */

	/*
	 * public String getBeginriqDate(){ if(((Visit)
	 * getPage().getVisit()).getString4()==null||((Visit)
	 * getPage().getVisit()).getString4()==""){ Calendar
	 * stra=Calendar.getInstance(); // stra.set(DateUtil.getYear(new Date()), 0,
	 * 1); stra.setTime(new Date()); stra.add(Calendar.DATE,-1); ((Visit)
	 * getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime())); }
	 * return ((Visit) getPage().getVisit()).getString4(); } public void
	 * setBeginriqDate(String value){ ((Visit)
	 * getPage().getVisit()).setString4(value); } public String getEndriqDate(){
	 * if(((Visit) getPage().getVisit()).getString5()==null||((Visit)
	 * getPage().getVisit()).getString5()==""){ ((Visit)
	 * getPage().getVisit()).setString5(DateUtil.FormatDate(new Date())); }
	 * return ((Visit) getPage().getVisit()).getString5(); } public void
	 * setEndriqDate(String value){ ((Visit)
	 * getPage().getVisit()).setString5(value); }
	 */

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("YUEF");
		cb2.setWidth(60);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("报表方式:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(150);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("LeixSelect");
		cb3.setWidth(80);
		tb1.addField(cb3);
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

		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb4 = new ComboBox();
		cb4.setTransform("GongysDropDown");
		cb4.setEditable(true);
		// meik.setWidth(60);
		// meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb4);

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
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setTreeid(null);
			visit.setDefaultTree(null);
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
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
		list.add(new IDropDownBean(3, "分厂分矿"));
		list.add(new IDropDownBean(4, "分矿分厂"));
		
		// list.add(new IDropDownBean(3,"棋盘表"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}
	
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"运输计划提报查询"));
		fahdwList.add(new IDropDownBean(1,"运输计划补充提报查询"));
		fahdwList.add(new IDropDownBean(2,"运输计划批复查询"));
		fahdwList.add(new IDropDownBean(3,"运输计划补充批复查询"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
	// private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		// if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
		// _DiancmcChange = false;
		// }else{
		// _DiancmcChange = true;
		// }
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

	// 年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
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