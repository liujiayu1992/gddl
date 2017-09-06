package com.zhiren.jt.zdt.shengcdy.hetwcqkb;

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

/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Hetwcqkb extends BasePage {
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

	private String getFilerCondtion(int jib){
		String strCondition="";
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jihkjb.id=" +getJihkjDropDownValue().getId();
		}
		return strCondition;
	}
	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
//		return getQibb();
		if(getLeixSelectValue().getValue().equals("分厂")){
			return getZhiltj();
		}else if(getLeixSelectValue().getValue().equals("分矿")){
			return getZhiltj();
		}else if(getLeixSelectValue().getValue().equals("棋盘表")){
			return getQibb();
		} else {
			return "无此报表";
		}
	}
	
	private String getQibb(){
		JDBCcon conn= new JDBCcon();
		ChessboardTable cd=new  ChessboardTable();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		//电厂条件
		int jib=this.getDiancTreeJib();
		String jihkjCondition=getFilerCondtion(jib);
		String diancCondition="";
		if(jib==1){
			diancCondition=",diancxxb gs where gs.fuid=dc.id and gs.id=fh.diancxxb_id and fh.gongysb_id=gys.id \n ";
		}else{
			diancCondition=
				" where dc.id=fh.diancxxb_id and fh.diancxxb_id in (select id\n" + 
				" from(\n" + 
				" select id from diancxxb\n" + 
				" start with id="+getTreeid()+"\n" + 
				" connect by fuid=prior id\n" + 
				" )\n" + 
				" union\n" + 
				" select id\n" + 
				" from diancxxb\n" + 
				" where id="+getTreeid()+")" ; 
		}
		String gongysfuid="(select fuid from gongysb where id="+getGongysDropDownValue().getId()+"\n)";
		long fuid=0;
		ResultSet rs1 = conn.getResultSet(gongysfuid);
		try {
			if(rs1.next()){
				fuid=rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrWidth =new int[] {120,60,80,80,80,80,80,80};
		
		String gongystj="";
		if(getGongysDropDownValue().getId()==-1){		
			gongystj="(select xgys.id as id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc\n" +
						"  from gongysb xgys,gongysb dgys\n" + 
						"  where xgys.fuid=dgys.id(+))gys";

		}else{
			gongystj="(select gys.id,gys.xuh,gys.mingc from gongysb gys  where (gys.id="+getGongysDropDownValue().getId()+" or  gys.fuid="+getGongysDropDownValue().getId()+"))gys ";
			
		}
		StringBuffer strRow = new StringBuffer();
		
		strRow.append("select decode( grouping(gys.mingc),1,'小计',gys.mingc) as 供应商 from \n");
			strRow.append("(select distinct  ht.gongysb_id \n");
			strRow.append("       from hetb ht,hetslb hs,jihkjb  \n");
			strRow.append("         where ht.id=hs.hetb_id and ht.jihkjb_id=jihkjb.id\n");
			strRow.append("         and hs.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			strRow.append("         and hs.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')").append(jihkjCondition).append("  \n");
			strRow.append("  union select  distinct fh.gongysb_id \n");
			strRow.append("        from fahb  fh,jihkjb \n");
			strRow.append("        where fh.jihkjb_id=jihkjb.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			strRow.append("       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')").append(jihkjCondition).append(" ) bt,fahb fh,diancxxb dc, \n");
			strRow.append(gongystj);
			strRow.append(diancCondition);
			strRow.append("and bt.gongysb_id=gys.id \n");
			strRow.append("group by rollup(gys.mingc) \n");
			strRow.append("order by grouping(gys.mingc) desc,max(gys.xuh),gys.mingc \n");
			 
		StringBuffer strCol = new StringBuffer();
			strCol.append("select decode( grouping(dc.mingc),1,'小计',dc.mingc) as 电厂 from \n");
			strCol.append("(select distinct  ht.diancxxb_id \n");
			strCol.append("       from hetb ht,hetslb hs  ,jihkjb\n");
			strCol.append("         where ht.id=hs.hetb_id and ht.jihkjb_id=jihkjb.id\n");
			strCol.append("         and hs.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			strCol.append("         and hs.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')").append(jihkjCondition).append("  \n");
			strCol.append("  union select  distinct fh.diancxxb_id \n");
			strCol.append("        from fahb  fh,jihkjb \n");
			strCol.append("        where fh.jihkjb_id=jihkjb.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			strCol.append("       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')").append(jihkjCondition).append(" ) bt, \n");
			strCol.append("       diancxxb dc,fahb fh, \n");
			strCol.append(gongystj);
			strCol.append(diancCondition);
			strCol.append("and bt.diancxxb_id(+)=dc.id \n");
			strCol.append("group by rollup(dc.mingc) \n");
			strCol.append("order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" select decode(grouping( fgs.mingc),1,'小计',fgs.mingc) as 电厂, \n");
			sbsql.append("       decode(grouping( gys.mingc),1,'小计',gys.mingc) as 供应商, \n");
			sbsql.append("       round(decode(sum(nvl(htlj,0)),0,0, sum(nvl(lmlj,0))/sum(nvl(htlj,0)))*100,2) as 累计, \n");
			sbsql.append("       round(decode(sum(nvl(htdr,0)),0,0, sum(nvl(lmdr,0)) /sum(nvl(htdr,0)))*100,2) as 当日 \n");
			sbsql.append(" from \n");
			sbsql.append("  (select distinct ht.diancxxb_id, ht.gongysb_id \n");
			sbsql.append("       from hetb ht,hetslb hs ,jihkjb \n");
			sbsql.append("         where ht.id=hs.hetb_id and ht.jihkjb_id=jihkjb.id \n");
			sbsql.append("         and hs.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("         and hs.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') ").append(jihkjCondition).append(" \n");
			sbsql.append("  union select  distinct fh.diancxxb_id, fh.gongysb_id \n");
			sbsql.append("        from fahb  fh,jihkjb \n");
			sbsql.append("        where fh.jihkjb_id=jihkjb.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') ").append(jihkjCondition).append(" ) bt, \n");
			sbsql.append(" (select ht.diancxxb_id, ht.gongysb_id,round(sum(getHetl_Sjd(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),  \n");
			sbsql.append("       to_date('"+getEndriqDate()+"','yyyy-mm-dd'),hs.riq,hs.hetl)),2) as htlj, \n");
			sbsql.append("       round(sum(getHetl_Sjd(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),  \n");
			sbsql.append("       to_date('"+getEndriqDate()+"','yyyy-mm-dd'),hs.riq,hs.hetl)),2) as htdr \n");
			sbsql.append("       from hetb ht,hetslb hs ,jihkjb \n");
			sbsql.append("            where ht.id=hs.hetb_id and ht.jihkjb_id=jihkjb.id \n");
			sbsql.append("         and hs.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("         and hs.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') ").append(jihkjCondition).append(" \n");
			sbsql.append("         group by ht.diancxxb_id, ht.gongysb_id) ht, \n");
			sbsql.append(" (select fh.diancxxb_id, fh.gongysb_id,sum(round(laimsl)) as lmlj, \n");
			sbsql.append("       sum(decode(daohrq,to_date('"+getEndriqDate()+"','yyyy-mm-dd') ,round(laimsl),0)) as lmdr \n");
			sbsql.append("        from fahb  fh,jihkjb \n");
			sbsql.append("        where fh.jihkjb_id=jihkjb.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("       group by fh.diancxxb_id, fh.gongysb_id) lm, \n");
			sbsql.append("       (select xgys.id as id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc \n");
			sbsql.append("               from gongysb xgys,gongysb dgys \n");
			sbsql.append("               where xgys.fuid=dgys.id(+))gys,diancxxb dc,vwfengs fgs \n");
			sbsql.append("  where bt.diancxxb_id=ht.diancxxb_id(+) and bt.gongysb_id=ht.gongysb_id(+) \n");
			sbsql.append("        and bt.diancxxb_id=lm.diancxxb_id(+) and bt.gongysb_id=lm.gongysb_id(+) \n");
			sbsql.append("        and bt.diancxxb_id=dc.id  and bt.gongysb_id=gys.id and dc.fuid=fgs.id \n");
			sbsql.append("  group by   cube(fgs.mingc,gys.mingc) \n");
			
//			System.out.println(sbsql);
//			System.out.println(strRow);
//			System.out.println(strCol);
			cd.setRowNames("供应商");
			cd.setColNames("电厂");
			cd.setDataNames("当日,累计");
			cd.setDataOnRow(true);
			cd.setRowToCol(false);
			cd.setData(strRow.toString(),strCol.toString(),sbsql.toString());
			cd.DataTable.setColAlign(2, Table.ALIGN_CENTER);
			
			ArrWidth =new int[cd.DataTable.getCols()];
			for (int i=1;i<ArrWidth.length;i++){
				ArrWidth[i]=80;
			}
			ArrWidth[0]=120;
			ArrWidth[1]=60;
			//
			rt.setBody(cd.DataTable);
			for(int i=3;i<rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_RIGHT);
			}
			if(rt.body.getRows()>0 && rt.body.getCols()>0){
				rt.body.setCellValue(1, 1, "单位");
				rt.body.setCellValue(1, 2, "单位");
			}
			rt.body.setWidth(ArrWidth);
			rt.body.mergeFixedRowCol();
			rt.body.ShowZero=false;
			rt.setTitle("合同完成情况"+"(棋盘表)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 3,riq+"-"+riq1,Table.ALIGN_RIGHT);
			rt.body.setPageRows(rt.PAPER_COLROWS);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(7,1,"单位:车、吨",Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}
	
	
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		int jib=this.getDiancTreeJib();
		String jihkjCondition=getFilerCondtion(jib);
		String diancCondition=
			"and hs.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ; 
		String diancCondition1=
			"and diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ; 
		String diancCondition2=
			"and d.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by(fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ;
		
//		供应商条件
		String gongysid="and f.gongysb_id ="+getGongysDropDownValue().getId();
		String gongysid1="and gongysb_id ="+getGongysDropDownValue().getId();
		String gongysfuid="(select fuid from gongysb where id="+getGongysDropDownValue().getId()+")";
		long fuid=0;
		ResultSet rs1 = con.getResultSet(gongysfuid);
		try {
			if(rs1.next()){
				fuid=rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		String gongysCondition="and f.gongysb_id in(select id from gongysb where fuid="+getGongysDropDownValue().getId() +")";
		String gongysCondition1="and gongysb_id in(select id from gongysb where fuid="+getGongysDropDownValue().getId() +")";
		String gongysConditionk="";
		
		 //System.out.println(diancCondition);
		//分厂
		if(getLeixSelectValue().getId()==1){
			sbsql.append("from  \n");
			sbsql.append("(select d.xuh,d.mingc,fgs.xuh as fgsxh,fgs.mingc as fgsmc, d.id as diancxxb_id,  vwfenx.xhu,vwfenx.fenx,vdc.rlgsmc from \n");
			sbsql.append("         diancxxb d,vwFenx,vwfengs fgs,vwdianc vdc \n");
			sbsql.append("         where d.jib=3 \n");
			sbsql.append("         and d.fuid=fgs.id and d.id=vdc.id ").append(diancCondition2).append(") fx, \n");
			
			sbsql.append("(select hs.diancxxb_id,decode(1,1,'当日') as fenx,round(sum(hs.yuejhcgl/daycount(hs.riq)),2)  as dr \n");
			sbsql.append("        from yuecgjhb hs,jihkjb where hs.jihkjb_id=jihkjb.id ").append(jihkjCondition).append("\n");
			sbsql.append("        and hs.riq=to_date('"+getEndriqDate()+"','yyyy-mm-dd')");
			sbsql.append(diancCondition).append(" group by hs.diancxxb_id \n");
			sbsql.append("union  select hs.diancxxb_id,decode(1,1,'累计') as fenx,round(sum(getHetl_Sjd(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'), \n");
			sbsql.append("       to_date('"+getEndriqDate()+"','yyyy-mm-dd'),hs.riq,hs.yuejhcgl)),2) as dr \n");
			sbsql.append("       from yuecgjhb hs,jihkjb where hs.jihkjb_id=jihkjb.id ").append(jihkjCondition).append("\n");
			sbsql.append("       and hs.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') and hs.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append(diancCondition);
			sbsql.append("       group by hs.diancxxb_id) bqhetl, \n");
			
			sbsql.append("(select hs.diancxxb_id,decode(1,1,'当日') as fenx,round(sum(yuejhcgl/daycount(hs.riq)),2)  as dr \n");
			sbsql.append("        from yuecgjhb hs,jihkjb where hs.jihkjb_id=jihkjb.id ").append(jihkjCondition).append(" \n");
			sbsql.append("        and hs.riq=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12) ");
			sbsql.append(diancCondition).append(" group by hs.diancxxb_id \n");
			sbsql.append("union  select hs.diancxxb_id,decode(1,1,'累计') as fenx,round(sum(getHetl_Sjd(add_months(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12), \n");
			sbsql.append("       add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12),hs.riq,hs.yuejhcgl)),2) as dr \n");
			sbsql.append("       from yuecgjhb hs,jihkjb where jihkjb.id=hs.jihkjb_id ").append(jihkjCondition).append("\n");
			sbsql.append("       and hs.riq<=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12) and hs.riq>=add_months(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) \n");
			sbsql.append(diancCondition);
			sbsql.append("       group by hs.diancxxb_id) tqhetl, \n");
			
			sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,sum(round(laimsl)) as dr \n");
			sbsql.append("        from fahb ,jihkjb where fahb.jihkjb_id=jihkjb.id ").append(jihkjCondition).append(" and daohrq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') ").append(diancCondition1).append(" group by diancxxb_id \n");
			sbsql.append("        union \n");
			sbsql.append("        select diancxxb_id,decode(1,1,'累计') as fenx,sum(round(laimsl)) as lj \n");
			sbsql.append("        from fahb,jihkjb where fahb.jihkjb_id=jihkjb.id ").append(jihkjCondition).append(" and daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("        and daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')").append(diancCondition1).append(" group by diancxxb_id) bqrucl, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,sum(round(laimsl)) as dr \n");
			sbsql.append("        from fahb,jihkjb where fahb.jihkjb_id=jihkjb.id ").append(jihkjCondition).append(" and daohrq=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12) ").append(diancCondition1).append(" group by diancxxb_id \n");
			sbsql.append("        union \n");
			sbsql.append("        select diancxxb_id,decode(1,1,'累计') as fenx,sum(round(laimsl)) as lj \n");
			sbsql.append("        from fahb,jihkjb where fahb.jihkjb_id=jihkjb.id").append(jihkjCondition).append("  and daohrq<=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12)  \n");
			sbsql.append("        and daohrq>=add_months(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) ").append(diancCondition1).append(" group by diancxxb_id) tqrucl \n");

         String sbsqlz=sbsql.toString();
         sbsql.setLength(0);
			if(jib==3){
				sbsql.append("select fx.mingc as dianc, fx.fenx, \n");
				sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq, \n");
				sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq, \n");
				sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch, \n");
				sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch, \n");
				sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl, \n");
				sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl \n");
				sbsql.append(sbsqlz);
				sbsql.append("where fx.diancxxb_id=bqhetl.diancxxb_id(+)     and fx.fenx=bqhetl.fenx(+) \n");
				sbsql.append("     and fx.diancxxb_id=tqhetl.diancxxb_id(+) and fx.fenx=tqhetl.fenx(+) \n");
				sbsql.append("     and fx.diancxxb_id=bqrucl.diancxxb_id(+) and fx.fenx=bqrucl.fenx(+) \n");
				sbsql.append("     and fx.diancxxb_id=tqrucl.diancxxb_id(+) and fx.fenx=tqrucl.fenx(+) \n");
				sbsql.append("group by rollup(fx.fenx,fx.fgsmc,fx.mingc) \n");
				sbsql.append("having not(grouping(fx.mingc)=1) \n");
				sbsql.append("order by grouping(fx.fgsmc) desc,min(fx.fgsxh),fx.fgsmc, \n");
				sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,min(fx.xhu),fx.fenx \n");
			}else if(jib==2){
				String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
				JDBCcon cn = new JDBCcon();
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl!=null){
						sbsql.append("select decode(grouping(fx.rlgsmc)+grouping(fx.fgsmc)+grouping(fx.mingc),2,fx.rlgsmc,1,fx.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||fx.mingc) as dianc, fx.fenx, \n");
						sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq, \n");
						sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq, \n");
						sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch, \n");
						sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch, \n");
						sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl, \n");
						sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl \n");
						sbsql.append(sbsqlz);
						sbsql.append("where fx.diancxxb_id=bqhetl.diancxxb_id(+)     and fx.fenx=bqhetl.fenx(+) \n");
						sbsql.append("     and fx.diancxxb_id=tqhetl.diancxxb_id(+) and fx.fenx=tqhetl.fenx(+) \n");
						sbsql.append("     and fx.diancxxb_id=bqrucl.diancxxb_id(+) and fx.fenx=bqrucl.fenx(+) \n");
						sbsql.append("     and fx.diancxxb_id=tqrucl.diancxxb_id(+) and fx.fenx=tqrucl.fenx(+) \n");
						sbsql.append("group by rollup(fx.fenx,fx.rlgsmc,fx.fgsmc,fx.mingc) \n");
						sbsql.append("having not(grouping(fx.rlgsmc)=1) \n");
						sbsql.append("order by grouping(fx.rlgsmc) desc,fx.rlgsmc,grouping(fx.fgsmc) desc,min(fx.fgsxh),fx.fgsmc, \n");
						sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,min(fx.xhu),fx.fenx \n");
					}else{
						sbsql.append("select decode(grouping(fx.fgsmc)+grouping(fx.mingc),1,fx.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||fx.mingc) as dianc, fx.fenx, \n");
						sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq, \n");
						sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq, \n");
						sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch, \n");
						sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch, \n");
						sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl, \n");
						sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl \n");
						sbsql.append(sbsqlz);
						sbsql.append("where fx.diancxxb_id=bqhetl.diancxxb_id(+)     and fx.fenx=bqhetl.fenx(+) \n");
						sbsql.append("     and fx.diancxxb_id=tqhetl.diancxxb_id(+) and fx.fenx=tqhetl.fenx(+) \n");
						sbsql.append("     and fx.diancxxb_id=bqrucl.diancxxb_id(+) and fx.fenx=bqrucl.fenx(+) \n");
						sbsql.append("     and fx.diancxxb_id=tqrucl.diancxxb_id(+) and fx.fenx=tqrucl.fenx(+) \n");
						sbsql.append("group by rollup(fx.fenx,fx.fgsmc,fx.mingc) \n");
						sbsql.append("having not(grouping(fx.fgsmc)=1) \n");
						sbsql.append("order by grouping(fx.fgsmc) desc,min(fx.fgsxh),fx.fgsmc, \n");
						sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,min(fx.xhu),fx.fenx \n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else{
				sbsql.append("select decode(grouping(fx.fgsmc)+grouping(fx.mingc),2,'总计',1,fx.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||fx.mingc) as dianc, fx.fenx, \n");
				sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq, \n");
				sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq, \n");
				sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch, \n");
				sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch, \n");
				sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl, \n");
				sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl \n");
				sbsql.append(sbsqlz);
				sbsql.append("where fx.diancxxb_id=bqhetl.diancxxb_id(+)     and fx.fenx=bqhetl.fenx(+) \n");
				sbsql.append("     and fx.diancxxb_id=tqhetl.diancxxb_id(+) and fx.fenx=tqhetl.fenx(+) \n");
				sbsql.append("     and fx.diancxxb_id=bqrucl.diancxxb_id(+) and fx.fenx=bqrucl.fenx(+) \n");
				sbsql.append("     and fx.diancxxb_id=tqrucl.diancxxb_id(+) and fx.fenx=tqrucl.fenx(+) \n");
				sbsql.append("group by rollup(fx.fenx,fx.fgsmc,fx.mingc) \n");
				sbsql.append("having not(grouping(fx.fenx)=1) \n");
				sbsql.append("order by grouping(fx.fgsmc) desc,min(fx.fgsxh),fx.fgsmc, \n");
				sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,min(fx.xhu),fx.fenx \n");
			}
		}else if(getLeixSelectValue().getId()==2){//分矿
			sbsql.append("from  \n");
			sbsql.append("(select distinct d.xuh,d.id as diancxxb_id,f.gongysb_id,g.mingc,vwfenx.xhu,vwfenx.fenx from \n");
			sbsql.append("diancxxb d,vwFenx,fahb f,gongysb g \n");
			sbsql.append("where f.gongysb_id=g.id and f.diancxxb_id=d.id \n").append(diancCondition2);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition);
				}
			}else{
				sbsql.append(gongysid);
			}
			sbsql.append(") fx, \n");
			//本期合同
			sbsql.append("(select hs.diancxxb_id,hs.gongysb_id,g.mingc,decode(1,1,'当日') as fenx,round(yuejhcgl/daycount(hs.riq),2)  as dr \n");
			sbsql.append(" from yuecgjhb hs,gongysb g,shengfb sf,jihkjb where hs.jihkjb_id=jihkjb.id \n");
			sbsql.append(" and hs.gongysb_id=g.id and g.shengfb_id=sf.id  \n");
			sbsql.append("        and hs.riq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') ").append(jihkjCondition);
			sbsql.append(diancCondition);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition);
				}
			}else{
				sbsql.append(gongysid);
			}
			sbsql.append(" group by (hs.diancxxb_id,hs.gongysb_id,g.mingc,yuejhcgl,hs.riq) \n");
			
			sbsql.append("union  select hs.diancxxb_id,hs.gongysb_id,g.mingc,decode(1,1,'累计') as fenx,round(getHetl_Sjd(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'), \n");
			sbsql.append("       to_date('"+getEndriqDate()+"','yyyy-mm-dd'),hs.riq,hs.yuejhcgl),2) as dr \n");
			sbsql.append("       from yuecgjhb hs,gongysb g,shengfb sf,jihkjb where hs.jihkjb_id=jihkjb.id \n");
			sbsql.append("and hs.gongysb_id=g.id and g.shengfb_id=sf.id ").append(jihkjCondition).append("\n");
			sbsql.append("       and hs.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') and hs.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append(diancCondition);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition);
				}
			}else{
				sbsql.append(gongysid);
			}
			sbsql.append("       group by (hs.diancxxb_id,hs.gongysb_id,g.mingc,hs.yuejhcgl,hs.riq)) bqhetl, \n");
			//同期合同
			sbsql.append("(select hs.diancxxb_id,hs.gongysb_id,g.mingc,decode(1,1,'当日') as fenx,round(yuejhcgl/daycount(hs.riq),2)  as dr \n");
			sbsql.append("        from yuecgjhb hs,gongysb g,shengfb sf,jihkjb where hs.jihkjb_id=jihkjb.id \n");
			sbsql.append(" and hs.gongysb_id=g.id and g.shengfb_id=sf.id").append(jihkjCondition);
			sbsql.append("        and hs.riq=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12) ");
			sbsql.append(diancCondition);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition);
				}
			}else{
				sbsql.append(gongysid);
			}
			sbsql.append(" group by (hs.diancxxb_id,hs.gongysb_id,g.mingc,yuejhcgl,hs.riq) \n");
			sbsql.append("union  select hs.diancxxb_id,hs.gongysb_id,g.mingc,decode(1,1,'累计') as fenx,round(getHetl_Sjd(add_months(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12), \n");
			sbsql.append("       add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12),hs.riq,hs.yuejhcgl),2) as dr \n");
			sbsql.append("       from yuecgjhb hs,gongysb g,shengfb sf ,jihkjb where hs.jihkjb_id=jihkjb.id \n");
			sbsql.append(" and hs.gongysb_id=g.id and g.shengfb_id=sf.id").append(jihkjCondition);
			sbsql.append("       and hs.riq<=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12) and hs.riq>=add_months(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) \n");
			sbsql.append(diancCondition);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition);
				}
			}else{
				sbsql.append(gongysid);
			}
			sbsql.append("       group by (hs.diancxxb_id,hs.gongysb_id,g.mingc,yuejhcgl,hs.riq))tqhetl, \n");
			//*********************************************************************************************//
			sbsql.append("(select gongysb_id,decode(1,1,'当日') as fenx,sum(round(laimsl)) as dr \n");
			sbsql.append("        from fahb,jihkjb where fahb.jihkjb_id=jihkjb.id ").append(jihkjCondition).append(" and daohrq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') ").append(diancCondition1);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition1);
				}
			}else{
				sbsql.append(gongysid1);
			}
			sbsql.append(" group by gongysb_id \n");
			sbsql.append("        union \n");
			sbsql.append("        select gongysb_id,decode(1,1,'累计') as fenx,sum(round(laimsl)) as lj \n");
			sbsql.append("        from fahb,jihkjb where fahb.jihkjb_id=jihkjb.id  ").append(jihkjCondition).append(" and  daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("        and daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')").append(diancCondition1);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition1); 
				}
			}else{
				sbsql.append(gongysid1);
			}
			sbsql.append(" group by gongysb_id) bqrucl, \n");
			sbsql.append("(select gongysb_id,decode(1,1,'当日') as fenx,sum(round(laimsl)) as dr \n");
			sbsql.append("        from fahb,jihkjb where fahb.jihkjb_id=jihkjb.id  ").append(jihkjCondition).append(" and daohrq=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12) ").append(diancCondition1);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition1);
				}
			}else{
				sbsql.append(gongysid1);
			}
			sbsql.append(" group by gongysb_id \n");
			sbsql.append("        union \n");
			sbsql.append("        select gongysb_id,decode(1,1,'累计') as fenx,sum(round(laimsl)) as lj \n");
			sbsql.append("        from fahb ,jihkjb where fahb.jihkjb_id=jihkjb.id  ").append(jihkjCondition).append(" and daohrq<=add_months(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12)  \n");
			sbsql.append("        and daohrq>=add_months(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) ").append(diancCondition1);
			if(fuid==0){
				if(getGongysDropDownValue().getId()==-1){
					sbsql.append(gongysConditionk);
				}else{
				sbsql.append(gongysCondition1);
				}
			}else{
				sbsql.append(gongysid1);
			}
			sbsql.append(" group by gongysb_id) tqrucl,shengfb sf,gongysb g,diancxxb d \n");

	         String sbsqlz=sbsql.toString();
	         sbsql.setLength(0);
				if(jib==3){
					sbsql.append("select fx.mingc as dianc, fx.fenx,  \n");
					sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq,  \n");
					sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq,  \n");
					sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch,  \n");
					sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch,  \n");
					sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl,  \n");
					sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl  \n");
					sbsql.append(sbsqlz);
					sbsql.append("where fx.gongysb_id=g.id and g.shengfb_id=sf.id and fx.diancxxb_id=d.id \n");
					sbsql.append("     and fx.gongysb_id=bqhetl.gongysb_id(+) and fx.fenx=bqhetl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=tqhetl.gongysb_id(+) and fx.fenx=tqhetl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=bqrucl.gongysb_id(+) and fx.fenx=bqrucl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=tqrucl.gongysb_id(+) and fx.fenx=tqrucl.fenx(+)  \n");
					sbsql.append("group by rollup(fx.fenx,sf.quanc,fx.mingc)  \n");
					sbsql.append("having not(grouping(fx.mingc)=1)  \n");
					sbsql.append("order by grouping(sf.quanc) desc,min(sf.quanc),sf.quanc, \n");
					sbsql.append("grouping(fx.mingc) desc,min(fx.mingc),fx.fenx  \n");
				}else if(jib==2){
					sbsql.append("select decode(grouping(sf.quanc)+grouping(fx.mingc),1,sf.quanc,fx.mingc) as dianc, fx.fenx,  \n");
					sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq,  \n");
					sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq,  \n");
					sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch,  \n");
					sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch,  \n");
					sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl,  \n");
					sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl  \n");
					sbsql.append(sbsqlz);
					sbsql.append("where fx.gongysb_id=g.id and g.shengfb_id=sf.id and fx.diancxxb_id=d.id \n");
					sbsql.append("     and fx.gongysb_id=bqhetl.gongysb_id(+) and fx.fenx=bqhetl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=tqhetl.gongysb_id(+) and fx.fenx=tqhetl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=bqrucl.gongysb_id(+) and fx.fenx=bqrucl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=tqrucl.gongysb_id(+) and fx.fenx=tqrucl.fenx(+)  \n");
					sbsql.append("group by rollup(fx.fenx,sf.quanc,fx.mingc)  \n");
					sbsql.append("having not(grouping(sf.quanc)=1)  \n");
					sbsql.append("order by grouping(sf.quanc) desc,min(sf.quanc),sf.quanc, \n");
					sbsql.append("grouping(fx.mingc) desc,min(fx.mingc),fx.fenx  \n");
				}else{
					sbsql.append("select decode(grouping(sf.quanc)+grouping(fx.mingc),2,'总计',1,sf.quanc,'&nbsp;&nbsp;'||fx.mingc) as dianc, fx.fenx,  \n");
					sbsql.append("      sum(nvl(bqhetl.dr,0)) as htbq,sum(nvl(tqhetl.dr,0)) as httq,  \n");
					sbsql.append("      sum(nvl(bqrucl.dr,0)) as rcbq,sum(nvl(tqrucl.dr,0)) as rctq,  \n");
					sbsql.append("      sum(nvl(bqhetl.dr,0))-sum(nvl(bqrucl.dr,0)) as bqch,  \n");
					sbsql.append("      sum(nvl(tqhetl.dr,0))-sum(nvl(tqrucl.dr,0)) as btch,  \n");
					sbsql.append("      decode(sum(nvl(bqhetl.dr,0)),0,0,round(sum(nvl(bqrucl.dr,0))/sum(nvl(bqhetl.dr,0))*100,2)) as bqdhl,  \n");
					sbsql.append("      decode(sum(nvl(tqhetl.dr,0)),0,0,round(sum(nvl(tqrucl.dr,0))/sum(nvl(tqhetl.dr,0))*100,2)) as tqdhl  \n");
					sbsql.append(sbsqlz);
					sbsql.append("where fx.gongysb_id=g.id and g.shengfb_id=sf.id and fx.diancxxb_id=d.id \n");
					sbsql.append("     and fx.gongysb_id=bqhetl.gongysb_id(+) and fx.fenx=bqhetl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=tqhetl.gongysb_id(+) and fx.fenx=tqhetl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=bqrucl.gongysb_id(+) and fx.fenx=bqrucl.fenx(+)  \n");
					sbsql.append("     and fx.gongysb_id=tqrucl.gongysb_id(+) and fx.fenx=tqrucl.fenx(+)  \n");
					sbsql.append("group by rollup(fx.fenx,sf.quanc,fx.mingc)  \n");
					sbsql.append("having not(grouping(fx.fenx)=1)  \n");
					sbsql.append("order by grouping(sf.quanc) desc,min(sf.xuh),sf.quanc, \n");
					sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx  \n");
				}
		}
		           
		//System.out.println(sbsql);
//		ResultSet rs = con.getResultSet(sbsql.toString());
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][10];
		 if(getLeixSelectValue().getId()==1){//分厂
		 ArrHeader[0]=new String[] {"单位","单位","计划量","计划量","实收量","实收量","差额","差额","到货率(%)","到货率(%)"};
		 ArrHeader[1]=new String[] {"单位","单位","本期","同期","本期","同期","本期","同期","本期","同期"};

		 	int ArrWidth[]=new int[] {150,60,80,80,80,80,80,80,80,80};
			Table bt = new Table(rs,2,0,2);
		 	int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			//
			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("煤炭到货情况对比表(分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(4, 4,riq+"-"+riq1,Table.ALIGN_CENTER);
			rt.body.setPageRows(rt.PAPER_COLROWS);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 2, "审核人：", Table.ALIGN_LEFT);
			rt.setDefautlFooter(9, 2, "填报人：", Table.ALIGN_LEFT);
		 }else if(getLeixSelectValue().getId()==2){
			 ArrHeader[0]=new String[] {"单位","单位","合同量","合同量","实收量","实收量","差额","差额","到货率(%)","到货率(%)"};
			 ArrHeader[1]=new String[] {"单位","单位","本期","同期","本期","同期","本期","同期","本期","同期"};
			 	int ArrWidth[]=new int[] {150,60,80,80,80,80,80,80,80,80};
			 	int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
				rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
				
			 	Table bt = new Table(rs,2,0,2);
				rt.setBody(bt);
				bt.setColAlign(2, Table.ALIGN_CENTER);
				if(rt.body.getRows()>2){
					rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
				}
				//
				rt.body.ShowZero=false;
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);
//				增加长度的拉伸
				rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
				rt.body.mergeFixedRow();//合并行
				rt.body.mergeFixedCols();//和并列
				rt.setTitle("煤炭到货情况对比表(分矿)", ArrWidth);
				rt.title.setRowHeight(2, 50);
				rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
				rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
				rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//				rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
				rt.setDefaultTitle(4, 4,riq+"-"+riq1,Table.ALIGN_CENTER);
				rt.body.setPageRows(rt.PAPER_COLROWS);
				rt.createDefautlFooter(ArrWidth);
				rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
				rt.setDefautlFooter(7, 2, "审核人：", Table.ALIGN_LEFT);
				rt.setDefautlFooter(9, 2, "填报人：", Table.ALIGN_LEFT);
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
				stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
				stra.add(Calendar.MONTH,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tb1.addField(tbjhkj);
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
		
		tb1.addText(new ToolbarText("供货单位:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		//meik.setWidth(60);
		//meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
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
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
		}
		getToolbars();

		blnIsBegin = true;
	}
	
//	得到系统信息表中配置的报表标题的单位名称
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
	
//	 供应商
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
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
    	String sql="select id,mingc from gongysb  order by mingc ";
//    	+"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
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
		String sql = "select id,mingc from diancxxb order by xuh  ";
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
		
		 //类型
	    public IDropDownBean getLeixSelectValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean4();
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
	        List list=new ArrayList();
	        list.add(new IDropDownBean(1,"分厂"));
	        list.add(new IDropDownBean(2,"分矿"));
	        list.add(new IDropDownBean(3,"棋盘表"));
	        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
	        return ;
	    }
	    

//		计划口径
		public IDropDownBean getJihkjDropDownValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getJihkjDropDownModel().getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setJihkjDropDownValue(IDropDownBean Value) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}

		public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getJihkjDropDownModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getJihkjDropDownModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public void getJihkjDropDownModels() {
			String sql = "select id,mingc\n" + "from jihkjb order by mingc  \n";
			((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql, "全部"));
			return;
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
		
		//
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