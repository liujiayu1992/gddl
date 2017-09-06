package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class DayShouhcyReport extends BasePage {
	public final static String LX_FC="fc";

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
			"and y.diancxxb_id in (select id from(\n" + 
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

		
		sbsql.append("(select dc.id,dc.mingc,dc.xuh,dc.fuid,gs.mingc as gsmc,gs.xuh as gsxh,p.id as pzid,p.mingc as pzmc,vwfenx.fenx \n");
		sbsql.append("from vwfenx,diancxxb dc,pinzb p,vwfengs gs \n");
		sbsql.append(diancCondition1);
        sbsql.append("and gs.id=dc.fuid and p.leib='油' ) fx,\n");
//		------------------------------------------------------------------
		sbsql.append("(select y.diancxxb_id,y.pinzb_id,decode(1,1,'当日') as fenx, \n");
		sbsql.append("Zuorykc(diancxxb_id,to_date('"+riq1+"','yyyy-mm-dd')) as zuorkc ,sum(nvl(shourl,0)) as shourl, \n");
		sbsql.append("sum(fady+gongry+qity) as haoyhj,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("sum(cuns) as cuns,sum(panyk) as panyk,sum(kuc) as kuc from shouhcrbyb y \n");
		sbsql.append("where y.riq=to_date('"+riq1+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("group by diancxxb_id,y.pinzb_id \n");
		sbsql.append("union \n");
		sbsql.append("select y.diancxxb_id,y.pinzb_id,decode(1,1,'累计') as fenx, \n");
		sbsql.append("0 as zuorkc ,sum(shourl) as shourl,sum(fady+gongry+qity) as haoyhj, \n");
		sbsql.append("sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("sum(cuns) as cuns,sum(panyk) as panyk,0 as kuc from shouhcrbyb y \n");
		sbsql.append("where y.riq<=to_date('"+riq1+"','yyyy-mm-dd') \n");
		sbsql.append("and y.riq>=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("group by diancxxb_id,y.pinzb_id) shul \n");
		String sbsqlz=sbsql.toString();
        sbsql.setLength(0);
		if(jib==3){
			sbsql.append("select fx.mingc as danw , \n");
			sbsql.append("decode(grouping(fx.pzmc),1,decode(fx.mingc,null,fx.pzmc,'小计'),fx.pzmc) as pinz, \n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc,\n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from  \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx (+) \n");
			sbsql.append("and fx.pzid =shul.pinzb_id(+)\n");
			sbsql.append("group by grouping sets (fx.fenx,(fx.pzmc,fx.fenx),(fx.mingc,fx.fenx),(fx.mingc,fx.pzmc,fx.fenx))\n");
			sbsql.append("having not( grouping(fx.mingc)=1)  \n");
			sbsql.append("order by grouping(fx.mingc) desc,min(fx.xuh) ,fx.mingc,grouping(fx.pzmc)desc,fx.pzmc,fx.fenx  \n");
		}else if(jib==2){
			sbsql.append("select decode(grouping(fx.gsmc)+grouping(fx.mingc),1,fx.gsmc,'&nbsp&nbsp'||fx.mingc) as danw , \n");
			sbsql.append("decode(grouping(fx.gsmc)+grouping(fx.mingc)+grouping(fx.pzmc),3,'小计',2,decode(fx.gsmc,null,fx.pzmc,'小计'),1,decode(fx.mingc,null,fx.pzmc,'小计'),fx.pzmc) as pinz,\n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from  \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx(+) and  fx.pzid=shul.pinzb_id(+)  \n");
			sbsql.append("group by grouping sets (fx.fenx,(fx.gsmc,fx.fenx),(fx.pzmc,fx.fenx),(fx.gsmc,fx.pzmc,fx.fenx),(fx.gsmc,fx.mingc,fx.fenx),(fx.gsmc,fx.mingc,fx.pzmc,fx.fenx))\n");
			sbsql.append("having not(grouping(fx.gsmc)=1 )\n");
			sbsql.append("order by grouping(fx.gsmc)desc,max(fx.gsxh),fx.gsmc,grouping(fx.mingc) desc,max(fx.xuh) ,fx.mingc,grouping(fx.pzmc)desc,fx.pzmc,fx.fenx ");
		}else{
			sbsql.append("select  decode(grouping(fx.gsmc)+grouping(fx.mingc),2,'总计',1,fx.gsmc,'&nbsp&nbsp'||fx.mingc) as danw,\n");
			sbsql.append("decode(grouping(fx.gsmc)+grouping(fx.mingc)+grouping(fx.pzmc),3,'小计',2,decode(fx.gsmc,null,fx.pzmc,'小计'),1,decode(fx.mingc,null,fx.pzmc,'小计'),fx.pzmc) as pinz,\n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc,\n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx (+) \n");
			sbsql.append("and shul.pinzb_id(+)=fx.pzid \n");
			sbsql.append("group by grouping sets (fx.fenx,(fx.gsmc,fx.fenx),(fx.pzmc,fx.fenx),(fx.gsmc,fx.pzmc,fx.fenx),(fx.gsmc,fx.mingc,fx.fenx),(fx.gsmc,fx.mingc,fx.pzmc,fx.fenx))\n");
			sbsql.append(" having not (grouping(fx.fenx)=1 or (grouping(fx.gsmc)=1 and grouping(fx.mingc)<>1)) \n");
			sbsql.append("order by grouping(fx.gsmc) desc,max(fx.gsxh),fx.gsmc,grouping(fx.mingc) desc,max(fx.xuh),fx.mingc desc,fx.pzmc desc ,fx.fenx\n");

		}
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		
    	
		 String ArrHeader[][]=new String[2][12];
		 ArrHeader[0]=new String[] {"单位","油种","分项","昨日库存","入厂油量","耗用","耗用","耗用","耗用","损耗","盘盈亏","库存"};
		 ArrHeader[1]=new String[] {"单位","油种","分项","昨日库存","入厂油量","合计","发电","供热","其它用","损耗","盘盈亏","库存"};

		 int ArrWidth[]=new int[] {150,60,60,60,60,60,60,60,60,60,60,60};
		

		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		rt.body.setPageRows(22);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("油收耗存日报", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(5, 3,riq+"至"+riq1,Table.ALIGN_LEFT);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "审核人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "填报人：", Table.ALIGN_CENTER);
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