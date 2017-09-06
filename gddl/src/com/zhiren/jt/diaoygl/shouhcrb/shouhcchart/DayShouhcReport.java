package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;

/* 
* 时间：2009-08-13
* 作者： ll
* 修改内容：1、页面显示增加可用天数。
* 		   
*/ 
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

/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

public class DayShouhcReport extends BasePage {
	public final static String LX_FC="fc";
	public final static String LX_FK="fk";
	public final static String LX_FKFC="fkfc";
	public final static String LX_FCFK="fcfk";
	public final static String LX_QP="qp";
	
	private String leix="fc";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getPrintData();
	}

	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	
	private String getPrintData(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=DateUtil.FormatDate(getBeginriqDate());
		String riq1=DateUtil.FormatDate(getEndriqDate());
		int jib=this.getDiancTreeJib(getTreeid());
		String diancCondition=
			"and diancxxb_id in (select id from(\n" + 
			" 	select id from diancxxb\n" + 
			" 	start with (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")\n" + 
			" 	connect by fuid=prior id )\n" + 
			" union\n" + 
			" select id  from diancxxb\n" + 
			" 	where (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")) " ; 
		String diancCondition1=
			" where dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		 
		sbsql.append("from  \n");
		sbsql.append("(select dc.id,dc.mingc,dc.xuh,dc.fuid,vwfenx.fenx \n");
		sbsql.append("from vwfenx,diancxxb dc").append(diancCondition1).append(" ) fx, \n");
		sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,zuorkc(diancxxb_id,to_date('"+riq1+"','yyyy-mm-dd')) as zuorkc,sum(kuc) as kuc,sum(dangrgm) as dangrgm, \n");
//		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
//		-----------------合计中不含盘盈亏和水分差调整----------------------------
		sbsql.append("        sum(fady+gongry+qity+yuns+cuns) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb  where riq=to_date('"+riq1+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id \n");
		sbsql.append("union   select diancxxb_id,decode(1,1,'累计') as fenx,0 as zuorkc ,0 as kuc,sum(dangrgm) as dangrgm, \n");
//		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
//		-----------------合计中不含盘盈亏和水分差调整----------------------------
		sbsql.append("        sum(fady+gongry+qity+yuns+cuns) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb s where riq<=to_date('"+riq1+"','yyyy-mm-dd') \n");
		sbsql.append("        and  riq>=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id) dr,vwfengs gs \n");
		String tj=sbsql.toString();
		sbsql.setLength(0);
		
		if(jib==3){
			sbsql.append("select fx.mingc as danw, \n");
			sbsql.append("fx.fenx as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(",round_new(sum(nvl(kuc,0))/sum( keyts_rijhm_new(fx.id,hej,to_date('"+riq1+"','yyyy-mm-dd'),1)),1)  as keyts \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else if(jib==2){
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(",round_new(sum(nvl(kuc,0))/sum( keyts_rijhm_new(fx.id,hej,to_date('"+riq1+"','yyyy-mm-dd'),1)),1)  as keyts \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(",round_new(sum(nvl(kuc,0))/sum( keyts_rijhm_new(fx.id,hej,to_date('"+riq1+"','yyyy-mm-dd'),1)),1)  as keyts \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][15];
    	
//		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","耗用","耗用","耗用","调整量","库存"};
	    //		-----------------耗用中不含盘盈亏和水分差调整----------------------------
		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","耗用","水分差调整","盘盈亏","调整量","库存","可用天数"};
    
	    ArrHeader[1]=new String[] {"单位","单位","昨日库存","入厂煤量","合计","发电","供热","其它用","运损","储损","水分差调整","盘盈亏","调整量","库存","可用天数"};
	
		int ArrWidth[]=new int[] {150,50,80,60,60,60,60,60,60,60,60,60,60,60,60};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("收耗存日报", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 3, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(6, 3,riq+"至"+riq1,Table.ALIGN_LEFT);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "填报人：", Table.ALIGN_CENTER);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		
		//begin方法里进行初始化设置
		visit.setString4(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString4(pagewith);
			}
		//	visit.setString4(null);保存传递的非默认纸张的样式
			

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString9();
            }
        }
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

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		
		String sqlJib = "select d.jib from diancxxb d where d.id="			+ DiancTreeJib;
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