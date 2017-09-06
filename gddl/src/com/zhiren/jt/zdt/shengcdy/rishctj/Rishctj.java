package com.zhiren.jt.zdt.shengcdy.rishctj;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 2009-09-07
 * 作者：lll
 * 描述：系统信息表中增加 "収耗存日报是否显示运损"
 *       如果zhi='否'则隐藏运损且合计的值不含运损
 *       否则则显示运损且合计的值包含运损
 */
/*
 * 作者：王磊
 * 时间：2009-12-03
 * 描述：日收耗存统计将与分公司关联进行左连接
 */

/*
 * 作者：chh
 * 时间：2009-12-04
 * 描述：一厂多制下，主页收耗存连接显示不能显示数据
 * 		修改为：如果是择总厂显示分厂和分厂合计数据，如果子厂显示子厂数据
 */

public class Rishctj extends BasePage {
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

//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGrisT FROM JICXXB WHERE MINGC = '报表中的厂别是否显示'";
//		ResultSet rs = con.getResultSet(sql);
//		try {                                      
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		JDBCcon con = new JDBCcon();
		String Sql="select zhi from xitxxb where mingc='収耗存日报是否显示运损'";
		ResultSet rsl= con.getResultSet(Sql);
		String zhi="";
		try{
			if(rsl.next()){
				 zhi=rsl.getString("zhi");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    finally{
		con.Close();
	}
		if(zhi.equals("否")){
			return getNoxsData();
			}
		else{
			return getPrintData();
		}
//		return getQibb();
//		if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		
//		} else {
//			return "无此报表";
//		}
	}
	
	private String getNoxsData(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=DateUtil.FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib=this.getDiancTreeJib();
		boolean blnIsZongc=false;
		String diancCondition=
			"and diancxxb_id in (select id from(\n" + 
			" 	select id from diancxxb\n" + 
			" 	start with id="+getTreeid()+"\n" + 
			" 	connect by (fuid=prior id or shangjgsid=prior id))\n" + 
			" union\n" + 
			" select id  from diancxxb\n" + 
			" 	where id="+getTreeid()+") " ; 
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
		sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,zuorkc(diancxxb_id,to_date('"+riq+"','yyyy-mm-dd')) as zuorkc,sum(kuc) as kuc,sum(dangrgm) as dangrgm, \n");
//		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
//		-----------------合计中不含盘盈亏和水分差调整----------------------------
		sbsql.append("        sum(fady+gongry+qity) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb  where riq=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id \n");
		sbsql.append("union   select diancxxb_id,decode(1,1,'累计') as fenx,0 as zuorkc ,0 as kuc,sum(dangrgm) as dangrgm, \n");
//		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
//		-----------------合计中不含盘盈亏和水分差调整----------------------------
		sbsql.append("        sum(fady+gongry+qity) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb s where riq<=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append("        and  riq>=first_day(to_date('"+riq+"','yyyy-mm-dd')) \n");
		sbsql.append(diancCondition);
		
		if (jib==3){
			//是电厂的时候，连接替换为电厂
			sbsql.append("        group by diancxxb_id) dr,vwdianc gs \n");
		}else{
			sbsql.append("        group by diancxxb_id) dr,vwfengs gs \n");
		}
		String tj=sbsql.toString();
		String strMingc="";
		sbsql.setLength(0);
		String strZongcName ="";
		if(jib==3){
			blnIsZongc=IsZongc(con,getTreeid());//是否是一厂多制中的总厂
			if (!blnIsZongc){
				strMingc="gs.mingc";
			}else{//用总厂的名称替代总计
				strZongcName=getTreeDiancmc(getTreeid());
				strMingc="decode(grouping(gs.mingc),1,'"+strZongcName+"',gs.mingc)";
			}
			
			sbsql.append("select "+strMingc+" as danw, \n");
			sbsql.append("decode(fx.fenx,'累计','月累',fx.fenx) as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.id \n");
			sbsql.append("and fx.fenx=dr.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc) \n");
			if (blnIsZongc){
				//总厂的过滤，总厂的数据，用合计显示
				sbsql.append("having not(grouping(fx.fenx)=1 or (gs.mingc='"+strZongcName+"' and grouping(gs.mingc)=0)) \n");
			}else{//不是总厂过滤总计
				sbsql.append("having not(grouping(fx.fenx)=1 or  grouping(gs.mingc)=1) \n");
			}
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append(" min(fx.xuh),fx.fenx \n");
			
		}else if(jib==2){
			sbsql.append(
					"select decode(grouping(gs.mingc) + grouping(fx.mingc),\n" +
					"              1,\n" + 
					"              '&nbsp;&nbsp;' || (select mingc from diancxxb where jib = 2),\n" + 
					"              '&nbsp;&nbsp;' || fx.mingc) as danw,\n" + 
					"       decode(fx.fenx, '累计', '月累', fx.fenx) as fenx,\n" + 
					"       sum(nvl(zuorkc, 0)) as zuorkc,\n" + 
					"       sum(dr.dangrgm) as dangrgm,\n" + 
					"       sum(dr.hej) as hej,\n" + 
					"       sum(fady) as fady,\n" + 
					"       sum(gongry) as gongry,\n" + 
					"       sum(qity) as qity,\n" + 
					"       sum(cuns) as cuns,\n" + 
					"       sum(shuifctz) as shuifctz,\n" + 
					"       sum(panyk) as panyk,\n" + 
					"       sum(tiaozl) as tiaozl,\n" + 
					"       sum(nvl(dr.kuc, 0)) as kuc\n" + 
					"  from (select dc.id, dc.mingc, dc.xuh, dc.fuid, vwfenx.fenx\n" + 
					"          from vwfenx, diancxxb dc\n" + 
					"         where dc.id in (select id from diancxxb where jib = 3)) fx,\n" + 
					"       (select diancxxb_id,\n" + 
					"               decode(1, 1, '当日') as fenx,\n" + 
					"               zuorkc(diancxxb_id, to_date('"+riq+"', 'yyyy-mm-dd')) as zuorkc,\n" + 
					"               sum(kuc) as kuc,\n" + 
					"               sum(dangrgm) as dangrgm,\n" + 
					"               sum(fady + gongry + qity + yuns) as hej,\n" + 
					"               sum(fady) as fady,\n" + 
					"               sum(gongry) as gongry,\n" + 
					"               sum(qity) as qity,\n" + 
					"               sum(cuns) as cuns,\n" + 
					"               sum(shuifctz) as shuifctz,\n" + 
					"               sum(tiaozl) as tiaozl,\n" + 
					"               sum(panyk) as panyk\n" + 
					"          from shouhcrbb\n" + 
					"         where riq = to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
					"           and diancxxb_id in (select id from diancxxb where jib = 3)\n" + 
					"         group by diancxxb_id\n" + 
					"        union\n" + 
					"        select diancxxb_id,\n" + 
					"               decode(1, 1, '累计') as fenx,\n" + 
					"               0 as zuorkc,\n" + 
					"               0 as kuc,\n" + 
					"               sum(dangrgm) as dangrgm,\n" + 
					"               sum(fady + gongry + qity + yuns) as hej,\n" + 
					"               sum(fady) as fady,\n" + 
					"               sum(gongry) as gongry,\n" + 
					"               sum(qity) as qity,\n" + 
					"               sum(cuns) as cuns,\n" + 
					"               sum(shuifctz) as shuifctz,\n" + 
					"               sum(tiaozl) as tiaozl,\n" + 
					"               sum(panyk) as panyk\n" + 
					"          from shouhcrbb s\n" + 
					"         where riq <= to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
					"           and riq >= first_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
					"           and diancxxb_id in (select id from diancxxb where jib = 3)\n" + 
					"         group by diancxxb_id) dr,\n" + 
					"       vwfengs gs\n" + 
					" where fx.id = dr.diancxxb_id(+)\n" + 
					"   and gs.id(+) = fx.fuid\n" + 
					"   and fx.fenx = dr.fenx(+)\n" + 
					" group by rollup(fx.fenx, gs.mingc, fx.mingc)\n" + 
					"having not(grouping(gs.mingc) + grouping(fx.mingc) = 2)\n" + 
					" order by grouping(gs.mingc) desc,\n" + 
					"          min(gs.xuh),\n" + 
					"          gs.mingc,\n" + 
					"          grouping(fx.mingc) desc,\n" + 
					"          min(fx.xuh),\n" + 
					"          fx.mingc,\n" + 
					"          fx.fenx");
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("decode(fx.fenx,'累计','月累',fx.fenx) as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][13];
    	
//		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","耗用","耗用","耗用","调整量","库存"};
	    //		-----------------耗用中不含盘盈亏和水分差调整----------------------------
		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","水分差调整","盘盈亏","调整量","库存"};
    
		ArrHeader[1]=new String[] {"单位","单位","昨日库存","入厂煤量","合计","发电","供热","其它用","储损","水分差调整","盘盈亏","调整量","库存"};
	
		int ArrWidth[]=new int[] {120,40,60,60,60,60,60,60,50,50,60,60,60};

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
		rt.setTitle("收耗存日报", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 4, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 3,DateUtil.Formatdate("yyyy年MM月dd日",DateUtil.getDate(riq)),Table.ALIGN_CENTER );
		rt.setDefaultTitle(12, 2, "单位:吨" ,Table.ALIGN_RIGHT);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "审核人:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(12, 2, "填报人:", Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	
	private String getPrintData(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=DateUtil.FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib=this.getDiancTreeJib();
		
		String diancCondition=
			"and diancxxb_id in (select id from(\n" + 
			" 	select id from diancxxb\n" + 
			" 	start with id="+getTreeid()+"\n" + 
			" 	connect by (fuid=prior id or shangjgsid=prior id))\n" + 
			" union\n" + 
			" select id  from diancxxb\n" + 
			" 	where id="+getTreeid()+") " ; 
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
		sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,zuorkc(diancxxb_id,to_date('"+riq+"','yyyy-mm-dd')) as zuorkc,sum(kuc) as kuc,sum(dangrgm) as dangrgm, \n");
//		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
//		-----------------合计中不含盘盈亏和水分差调整----------------------------
		sbsql.append("        sum(fady+gongry+qity+yuns) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb  where riq=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id \n");
		sbsql.append("union   select diancxxb_id,decode(1,1,'累计') as fenx,0 as zuorkc ,0 as kuc,sum(dangrgm) as dangrgm, \n");
//		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
//		-----------------合计中不含盘盈亏和水分差调整----------------------------
		sbsql.append("        sum(fady+gongry+qity+yuns) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb s where riq<=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append("        and  riq>=first_day(to_date('"+riq+"','yyyy-mm-dd')) \n");
		sbsql.append(diancCondition);
		
		if (jib==3){
			//是电厂的时候，连接替换为电厂
			sbsql.append("        group by diancxxb_id) dr,vwdianc gs \n");
		}else{
			sbsql.append("        group by diancxxb_id) dr,vwfengs gs \n");
		}
		
		String tj=sbsql.toString();
		sbsql.setLength(0);
		boolean blnIsZongc=false;
		String strMingc="";
		String strZongcName="";
		
		if(jib==3){
			blnIsZongc=IsZongc(con,getTreeid());//是否是一厂多制中的总厂
			if (!blnIsZongc){
				strMingc="gs.mingc";
			}else{//用总厂的名称替代总计
				strZongcName=getTreeDiancmc(getTreeid());
				strMingc="decode(grouping(gs.mingc),1,'"+strZongcName+"',gs.mingc)";
			}
			
			sbsql.append("select "+strMingc+" as danw, \n");
			sbsql.append("decode(fx.fenx,'累计','月累',fx.fenx) as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id(+)=fx.id \n");
			sbsql.append("and fx.fenx=dr.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc) \n");
			if (blnIsZongc){
				//总厂的过滤，总厂的数据，用合计显示
				sbsql.append("having not(grouping(fx.fenx)=1 or (gs.mingc='"+strZongcName+"' and grouping(gs.mingc)=0)) \n");
			}else{//不是总厂过滤总计
				sbsql.append("having not(grouping(fx.fenx)=1 or  grouping(gs.mingc)=1) \n");
			}
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append(" min(fx.xuh),fx.fenx \n");
			
		}else if(jib==2){
			sbsql.append(
					"select decode(grouping(gs.mingc) + grouping(fx.mingc),\n" +
					"              1,\n" + 
					"              '&nbsp;&nbsp;' || (select mingc from diancxxb where jib = 2),\n" + 
					"              '&nbsp;&nbsp;' || fx.mingc) as danw,\n" + 
					"       decode(fx.fenx, '累计', '月累', fx.fenx) as fenx,\n" + 
					"       sum(nvl(zuorkc, 0)) as zuorkc,\n" + 
					"       sum(dr.dangrgm) as dangrgm,\n" + 
					"       sum(dr.hej) as hej,\n" + 
					"       sum(fady) as fady,\n" + 
					"       sum(gongry) as gongry,\n" + 
					"       sum(qity) as qity,\n" + 
					"       sum(yuns) as yuns,\n" + 
					"       sum(cuns) as cuns,\n" + 
					"       sum(shuifctz) as shuifctz,\n" + 
					"       sum(panyk) as panyk,\n" + 
					"       sum(tiaozl) as tiaozl,\n" + 
					"       sum(nvl(dr.kuc, 0)) as kuc\n" + 
					"  from (select dc.id, dc.mingc, dc.xuh, dc.fuid, vwfenx.fenx\n" + 
					"          from vwfenx, diancxxb dc\n" + 
					"         where dc.id in (select id from diancxxb where jib = 3)) fx,\n" + 
					"       (select diancxxb_id,\n" + 
					"               decode(1, 1, '当日') as fenx,\n" + 
					"               zuorkc(diancxxb_id, to_date('"+riq+"', 'yyyy-mm-dd')) as zuorkc,\n" + 
					"               sum(kuc) as kuc,\n" + 
					"               sum(dangrgm) as dangrgm,\n" + 
					"               sum(fady + gongry + qity + yuns) as hej,\n" + 
					"               sum(fady) as fady,\n" + 
					"               sum(gongry) as gongry,\n" + 
					"               sum(qity) as qity,\n" + 
					"               sum(yuns) as yuns,\n" + 
					"               sum(cuns) as cuns,\n" + 
					"               sum(shuifctz) as shuifctz,\n" + 
					"               sum(tiaozl) as tiaozl,\n" + 
					"               sum(panyk) as panyk\n" + 
					"          from shouhcrbb\n" + 
					"         where riq = to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
					"           and diancxxb_id in (select id from diancxxb where jib = 3)\n" + 
					"         group by diancxxb_id\n" + 
					"        union\n" + 
					"        select diancxxb_id,\n" + 
					"               decode(1, 1, '累计') as fenx,\n" + 
					"               0 as zuorkc,\n" + 
					"               0 as kuc,\n" + 
					"               sum(dangrgm) as dangrgm,\n" + 
					"               sum(fady + gongry + qity + yuns) as hej,\n" + 
					"               sum(fady) as fady,\n" + 
					"               sum(gongry) as gongry,\n" + 
					"               sum(qity) as qity,\n" + 
					"               sum(yuns) as yuns,\n" + 
					"               sum(cuns) as cuns,\n" + 
					"               sum(shuifctz) as shuifctz,\n" + 
					"               sum(tiaozl) as tiaozl,\n" + 
					"               sum(panyk) as panyk\n" + 
					"          from shouhcrbb s\n" + 
					"         where riq <= to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
					"           and riq >= first_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
					"           and diancxxb_id in (select id from diancxxb where jib = 3)\n" + 
					"         group by diancxxb_id) dr,\n" + 
					"       vwfengs gs\n" + 
					" where fx.id = dr.diancxxb_id(+)\n" + 
					"   and gs.id(+) = fx.fuid\n" + 
					"   and fx.fenx = dr.fenx(+)\n" + 
					" group by rollup(fx.fenx, gs.mingc, fx.mingc)\n" + 
					"having not(grouping(gs.mingc) + grouping(fx.mingc) = 2)\n" + 
					" order by grouping(gs.mingc) desc,\n" + 
					"          min(gs.xuh),\n" + 
					"          gs.mingc,\n" + 
					"          grouping(fx.mingc) desc,\n" + 
					"          min(fx.xuh),\n" + 
					"          fx.mingc,\n" + 
					"          fx.fenx");
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("decode(fx.fenx,'累计','月累',fx.fenx) as fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id(+)=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][14];
    	
//		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","耗用","耗用","耗用","调整量","库存"};
	    //		-----------------耗用中不含盘盈亏和水分差调整----------------------------
		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","耗用","水分差调整","盘盈亏","调整量","库存"};
    
		ArrHeader[1]=new String[] {"单位","单位","昨日库存","入厂煤量","合计","发电","供热","其它用","运损","储损","水分差调整","盘盈亏","调整量","库存"};
	
		int ArrWidth[]=new int[] {120,40,60,60,60,60,60,60,60,50,50,60,60,60};

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
		

		
		//rt.body.setCells(1, 9, rt.body.getRows(), 9, Table.PER_USED, false);
		
		rt.setTitle("收耗存日报", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(6, 3,DateUtil.Formatdate("yyyy年MM月dd日",DateUtil.getDate(riq)),Table.ALIGN_CENTER );
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "填报人：", Table.ALIGN_LEFT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}	
	
	//是一厂多制的总厂，级别是3，并且有孩子
	private boolean IsZongc(JDBCcon cn,String strDiancID ){
		String sqlq="select id from diancxxb where fuid in(select id from diancxxb where id="+strDiancID+" and jib=3)";
		ResultSetList rs=new ResultSetList();
		rs=cn.getResultSetList(sqlq);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}

	
	// 合同量分厂分矿分矿分厂统计报表
	private String getZhiltj() {
//		2008-09-02 以前使用 
		/*JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id \n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+" or shangjgsid="+getTreeid()+")" ; 
		String diancCondition1=
			" where dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+" or shangjgsid="+getTreeid()+") " ; 
		 
		sbsql.append("from  \n");
		sbsql.append("(select dc.id,dc.mingc,dc.xuh,dc.fuid,vwfenx.fenx \n");
		sbsql.append("from vwfenx,diancxxb dc").append(diancCondition1).append(" ) fx, \n");
		sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,zuorkc(diancxxb_id,to_date('"+getBeginriqDate()+"','yyyy-mm-dd')) as zuorkc,sum(kuc) as kuc,sum(dangrgm) as dangrgm, \n");
		sbsql.append("        sum(fady+gongry) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb  where riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id \n");
		sbsql.append("union   select diancxxb_id,decode(1,1,'累计') as fenx,0 as zuorkc ,0 as kuc,sum(dangrgm) as dangrgm, \n");
		sbsql.append("        sum(fady+gongry) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb s where riq<=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
		sbsql.append("        and  riq>=first_day(to_date('"+getBeginriqDate()+"','yyyy-mm-dd')) \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id) dr,vwfengs gs \n");
		String tj=sbsql.toString();
		sbsql.setLength(0);
		
		if(jib==3){
			sbsql.append("select fx.mingc as danw, \n");
			sbsql.append("fx.fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(tiaozl) as tiaozl,sum(panyk) as panyk,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else if(jib==2){
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(tiaozl) as tiaozl,sum(panyk) as panyk,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(gs.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(tiaozl) as tiaozl,sum(panyk) as panyk,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id(+) and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			
//		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][13];
		 ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","其它用","损耗","水分差<br>调整","调整量","盘盈亏","库存"};
		 ArrHeader[1]=new String[] {"单位","单位","昨日库存","入厂煤量","合计","发电","供热","其它用","损耗","水分差<br>调整","调整量","盘盈亏","库存"};

		 int ArrWidth[]=new int[] {150,50,80,80,80,80,80,60,60,60,60,60,80};*/

//		lzq 2008-09-02 修改与收耗存日报中的报表打印中的收耗存日报查询一致
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String strRiq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq=DateUtil.FormatDate(DateUtil.getDate(getBeginriqDate()));
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and diancxxb_id in (select id from(\n" + 
			" 	select id from diancxxb\n" + 
			" 	start with id="+getTreeid()+"\n" + 
			" 	connect by fuid=prior id )\n" + 
			" union\n" + 
			" select id  from diancxxb\n" + 
			" 	where id="+getTreeid()+") " ; 
		String diancCondition1=
			" where dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		 
		sbsql.append("from  \n");
		sbsql.append("(select dc.id,dc.mingc,dc.xuh,dc.fuid,vwfenx.fenx \n");
		sbsql.append("from vwfenx,diancxxb dc").append(diancCondition1).append(" ) fx, \n");
		sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,zuorkc(diancxxb_id,to_date('"+riq+"','yyyy-mm-dd')) as zuorkc,sum(kuc) as kuc,sum(dangrgm) as dangrgm, \n");
		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb  where riq=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id \n");
		sbsql.append("union   select diancxxb_id,decode(1,1,'累计') as fenx,0 as zuorkc ,0 as kuc,sum(dangrgm) as dangrgm, \n");
		sbsql.append("        sum(fady+gongry+qity+yuns+cuns+shuifctz+panyk) as hej,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("        sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz,sum(tiaozl) as tiaozl, sum(panyk) as panyk  \n");
		sbsql.append("        from shouhcrbb s where riq<=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append("        and  riq>=first_day(to_date('"+riq+"','yyyy-mm-dd')) \n");
		sbsql.append(diancCondition);
		sbsql.append("        group by diancxxb_id) dr,vwfengs gs \n");
		String tj=sbsql.toString();
		sbsql.setLength(0);
		
		if(jib==3){
			//如果是一厂多制，显示总厂和所有子厂的
			sbsql.append("select fx.mingc as danw, \n");
			sbsql.append("fx.fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			
			
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else if(jib==2){
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(gs.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx,sum(nvl(zuorkc,0)) as zuorkc,sum(dr.dangrgm) as dangrgm,sum(dr.hej) as hej,sum(fady) as fady, \n");
			sbsql.append("sum(gongry) as gongry,sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as cuns,sum(shuifctz) as shuifctz, \n");
			sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(tj);
			sbsql.append("where fx.id=dr.diancxxb_id and gs.id=fx.fuid \n");
			sbsql.append("and fx.fenx=dr.fenx  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][14];
		ArrHeader[0]=new String[] {"单位","单位","昨日库存","入厂煤量","耗用","耗用","耗用","耗用","耗用","耗用","耗用","耗用","调整量","库存"};
		ArrHeader[1]=new String[] {"单位","单位","昨日库存","入厂煤量","合计","发电","供热","其它用","运损","储损","水分差调整","盘盈亏","调整量","库存"};
	
		int ArrWidth[]=new int[] {150,50,80,80,80,80,80,60,60,60,60,60,60,80};

			Table bt=new Table(rs,2,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			//
			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("收耗存日报", ArrWidth);
			rt.title.setRowHeight(2, 50);
			
			
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(6, 2,strRiq,Table.ALIGN_CENTER);
			rt.body.setPageRows(22);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(10, 2, "审核人：", Table.ALIGN_LEFT);
			rt.setDefautlFooter(12, 2, "填报人：", Table.ALIGN_LEFT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
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
			visit.setString4(null);
			this.setTreeid(null);
		}
		getToolbars();

		blnIsBegin = true;

	}
 

    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return IDropDownDiancmc;
		}
//		 分公司下拉框
		private boolean _fengschange = false;

		public IDropDownBean getFengsValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getFengsModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
		}

		public void setFengsValue(IDropDownBean Value) {
			if (getFengsValue().getId() != Value.getId()) {
				_fengschange = true;
			}
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}

		public IPropertySelectionModel getFengsModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
				getFengsModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel4();
		}

		public void setDiancxxModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel4(value);
		}

		public void getFengsModels() {
			String sql;
			sql = "select id ,mingc from diancxxb where jib=2 order by id";
			setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
		}
	
//		得到系统信息表中配置的报表标题的单位名称
		public String getBiaotmc(){
			String biaotmc="";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
			ResultSet rs=cn.getResultSet(sql_biaotmc);
			try {
				while(rs.next()){
					 biaotmc=rs.getString("zhi");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				cn.Close();
			}
				
			return biaotmc;
			
		}
		
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}