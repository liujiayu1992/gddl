package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;
/*
 * 时间：2008-09-10
 * 作者：chh
 * 关键字：库存对比
 * 描述：加入空白说明列
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
import com.zhiren.common.ResultSetList;
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

public class KucdbReport extends BasePage {
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
			((Visit)getPage().getVisit()).setDate2(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
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
		String riq1=DateUtil.FormatDate(getBeginriqDate());
		String riq3=DateUtil.FormatDate(DateUtil.AddDate(getBeginriqDate(), -1, DateUtil.AddType_intDay));
		String riq2=DateUtil.FormatDate(getEndriqDate());
		int jib=this.getDiancTreeJib(getTreeid());
		String diancCondition=
			"and diancxxb_id in (select id from(\n" + 
			" 	select id from diancxxb\n" + 
			" 	start with id="+getTreeid()+"\n" + 
			" 	connect by fuid=prior id )\n" + 
			" union\n" + 
			" select id  from diancxxb\n" + 
			" 	where id="+getTreeid()+") " ; 
		
		if(jib==3){
			sbsql.append("select dc.mingc as danw, \n");
			sbsql.append("round(sum(dr.dangrgm),0) as dangrgm,round(sum(dr.haoyqkdr),0) as dangrhy,round(sum(qr.kuc),0) as qianrkc,round(sum(dangr.kuc),0) as dangrkc, \n");
			sbsql.append("round(sum(nvl(dangr.kuc,0)-nvl(qr.kuc,0)),0) as kucc,case when round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2)>=1000 then 1000 else round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2) end as zengjbl \n");
			sbsql.append("from diancxxb dc,vwfengs gs, \n");
			sbsql.append("(select diancxxb_id,sum(dangrgm) as dangrgm,sum(haoyqkdr) as haoyqkdr \n");
			sbsql.append("from shouhcrbb where riq>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
			sbsql.append("and riq<=to_date('"+riq2+"','yyyy-mm-dd') ").append(diancCondition).append(" group by diancxxb_id) dr, \n");
			sbsql.append("(select diancxxb_id,kuc from shouhcrbb where riq=to_date('"+riq2+"','yyyy-mm-dd')").append(diancCondition).append(" ) dangr, \n");
			sbsql.append("(select diancxxb_id,dangrgm,haoyqkdr,kuc from shouhcrbb  \n");
			sbsql.append("where riq=to_date('"+riq1+"','yyyy-mm-dd')-1 ").append(diancCondition).append(") qr \n");
			sbsql.append("where dc.fuid=gs.id and dr.diancxxb_id=dc.id and dr.diancxxb_id=qr.diancxxb_id(+)  and dr.diancxxb_id=dangr.diancxxb_id(+)  \n");
			sbsql.append("group by rollup(gs.mingc,dc.mingc) \n");
			sbsql.append("having not(grouping(dc.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");
		}else if(jib==2){
			diancCondition=
				"and diancxxb_id in (select id from(\n" + 
				" 	select id from diancxxb\n" + 
				" 	start with (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")\n" + 
				" 	connect by fuid=prior id )\n" + 
				" union\n" + 
				" select id  from diancxxb\n" + 
				" 	where (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")) " ; 
			

			String sql = "select decode( grouping(dc.mingc),1,'总计',dc.mingc) as danw,\n" +
			"       round(sum(dr.dangrgm), 0) as dangrgm,\n" + 
			"       round(sum(dr.haoyqkdr), 0) as dangrhy,\n" + 
			"       round(sum(qr.kuc), 0) as qianrkc,\n" + 
			"       round(sum(dangr.kuc), 0) as dangrkc,\n" + 
			"       round(sum(nvl(dangr.kuc, 0) - nvl(qr.kuc, 0)), 0) as kucc,\n" + 
			"       case\n" + 
			"         when round(decode(sum(qr.kuc), 0,1000,\n" + 
			"                           sum(dangr.kuc - qr.kuc) / sum(abs(qr.kuc))) * 100,\n" + 
			"                    2) >= 1000 then\n" + 
			"          1000\n" + 
			"         else\n" + 
			"          round(decode(sum(qr.kuc),\n" + 
			"                       0,\n" + 
			"                       1000,\n" + 
			"                       sum(dangr.kuc - qr.kuc) / sum(abs(qr.kuc))) * 100,\n" + 
			"                2)\n" + 
			"       end as zengjbl\n" + 
			"\n" + 
			"  from diancxxb dc,\n" + 
			"\n" + 
			"       (select diancxxb_id,\n" + 
			"               sum(dangrgm) as dangrgm,\n" + 
			"               sum(haoyqkdr) as haoyqkdr\n" + 
			"          from shouhcrbb\n" + 
			"         where riq >= to_date('" + riq1 + "', 'yyyy-mm-dd')\n" + 
			"           and riq <= to_date('" + riq2 + "', 'yyyy-mm-dd')\n" + 
			diancCondition+ 
			"         group by diancxxb_id) dr,\n" + 
			"\n" + 
			"       (select diancxxb_id, kuc\n" + 
			"          from shouhcrbb\n" + 
			"         where riq = to_date('" + riq2 + "', 'yyyy-mm-dd')\n" + 
			diancCondition +
			"		) dangr,\n" + 
			"\n" + 
			"       (select diancxxb_id, dangrgm, haoyqkdr, kuc\n" + 
			"          from shouhcrbb\n" + 
			"         where riq = to_date('" + riq1 + "', 'yyyy-mm-dd') - 1\n" + 
			diancCondition +
			") qr\n" + 
			"\n" + 
			" where dr.diancxxb_id = dc.id\n" + 
			"     and dr.diancxxb_id = qr.diancxxb_id(+)\n" + 
			"     and dr.diancxxb_id = dangr.diancxxb_id(+)\n" + 
			"\n" + 
			" group by rollup(dc.mingc)\n" + 
			" order by  grouping(dc.mingc) desc,\n" + 
			"          min(dc.xuh),\n" + 
			"          dc.mingc";
			
			sbsql.append(sql);

			
//			sbsql.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,dc.mingc) as danw, \n");
//			sbsql.append("round(sum(dr.dangrgm),0) as dangrgm,round(sum(dr.haoyqkdr),0) as dangrhy,round(sum(qr.kuc),0) as qianrkc,round(sum(dangr.kuc),0) as dangrkc, \n");
//			sbsql.append("round(sum(nvl(dangr.kuc,0)-nvl(qr.kuc,0)),0) as kucc,case when round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2)>=1000 then 1000 else round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2) end as zengjbl \n");
//			sbsql.append("from diancxxb dc,vwfengs gs, \n");
//			sbsql.append("(select diancxxb_id,sum(dangrgm) as dangrgm,sum(haoyqkdr) as haoyqkdr \n");
//			sbsql.append("from shouhcrbb where riq>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
//			sbsql.append("and riq<=to_date('"+riq2+"','yyyy-mm-dd') ").append(diancCondition).append(" group by diancxxb_id) dr, \n");
//			sbsql.append("(select diancxxb_id,kuc from shouhcrbb where riq=to_date('"+riq2+"','yyyy-mm-dd')").append(diancCondition).append(" ) dangr, \n");
//			sbsql.append("(select diancxxb_id,dangrgm,haoyqkdr,kuc from shouhcrbb  \n");
//			sbsql.append("where riq=to_date('"+riq1+"','yyyy-mm-dd')-1 ").append(diancCondition).append(") qr \n");
//			sbsql.append("where (dc.shangjgsid=gs.id or dc.id=gs.id) and dr.diancxxb_id=dc.id and dr.diancxxb_id=qr.diancxxb_id(+)  and dr.diancxxb_id=dangr.diancxxb_id(+)  \n");
//			sbsql.append("group by rollup(gs.mingc,dc.mingc) \n");
//			sbsql.append("having not(grouping(gs.mingc)=1) \n");
//			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
//			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'总计',1,'&nbsp&nbsp'||gs.mingc,'&nbsp&nbsp&nbsp&nbsp'||dc.mingc) as danw, \n");
			sbsql.append("round(sum(dr.dangrgm),0) as dangrgm,round(sum(dr.haoyqkdr),0) as dangrhy,round(sum(qr.kuc),0) as qianrkc,round(sum(dangr.kuc),0) as dangrkc, \n");
			sbsql.append("round(sum(nvl(dangr.kuc,0)-nvl(qr.kuc,0)),0) as kucc,case when round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2)>=1000 then 1000 else round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2) end as zengjbl \n");
			sbsql.append("from diancxxb dc,vwfengs gs, \n");
			sbsql.append("(select diancxxb_id,sum(dangrgm) as dangrgm,sum(haoyqkdr) as haoyqkdr \n");
			sbsql.append("from shouhcrbb where riq>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
			sbsql.append("and riq<=to_date('"+riq2+"','yyyy-mm-dd') ").append(diancCondition).append(" group by diancxxb_id) dr, \n");
			sbsql.append("(select diancxxb_id,kuc from shouhcrbb where riq=to_date('"+riq2+"','yyyy-mm-dd')").append(diancCondition).append(" ) dangr, \n");
			sbsql.append("(select diancxxb_id,dangrgm,haoyqkdr,kuc from shouhcrbb  \n");
			sbsql.append("where riq=to_date('"+riq1+"','yyyy-mm-dd')-1 ").append(diancCondition).append(") qr \n");
			sbsql.append("where dc.fuid=gs.id and dr.diancxxb_id=dc.id and dr.diancxxb_id=qr.diancxxb_id(+)  and dr.diancxxb_id=dangr.diancxxb_id(+)  \n");
			sbsql.append("group by rollup(gs.mingc,dc.mingc) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");
		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][7];
		 ArrHeader[0]=new String[] {"单位","来煤量","耗用量","库存","库存","库存差","增减比例(%)"};
		 ArrHeader[1]=new String[] {"单位","来煤量","耗用量",riq3,riq2,"库存差","增减比例(%)"};

		 int ArrWidth[]=new int[] {150,80,80,80,80,80,80};


		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setPageRows(22);
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
//		rt.body.setCellValue(2, 4, riq1);
//		rt.body.setCellValue(2, 5, riq2);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("库存对比情况表", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(3, 2,riq1+"至"+riq2,Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审核人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "填报人：", Table.ALIGN_LEFT);
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
			visit.setString10((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString10();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString10();
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