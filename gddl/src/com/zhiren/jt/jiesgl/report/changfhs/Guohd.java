package com.zhiren.jt.jiesgl.report.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：陈泽天
 * 时间：2010-01-27 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：王耀霆
 * 时间：2014-01-2 
 * 描述：为大开电厂特殊定制过衡单查询界面
 */
/*
 * 作者：王耀霆
 * 时间：2014-01-20 
 * 描述：为大开的过衡单添加数量经办人和数量负责人，每页打印的行数增加到24行
 */
/*
 * 作者：夏峥
 * 时间：2014-03-07 
 * 描述：修改记录集未关闭的BUG
 */
public class Guohd extends BasePage {
	
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

	public String getDiancQuanc() {
		return getDiancQuanc(getDiancId());
	}

	public long getDiancId() {
		/*
		 * if (isGongsUser()){ return getDiancmcValue().getId(); }else{ return
		 * ((Visit)getPage().getVisit()).getDiancxxbId(); }
		 */
		return getDiancId();
	}

	// 得到单位全称
	public String getDiancQuanc(long diancxxbID) {
		String _DiancQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ diancxxbID);
			while (rs.next()) {
				_DiancQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.Close();
		return _DiancQuanc;
	}

	public String getPrintTable() throws SQLException {
		if(MainGlobal.getXitxx_item("结算过衡单", "大开电厂特殊过衡单", "0", "否").equals("是")){
			return getDKGuohd();
		}else{
			return getGuohd();
		}
		
	}
	
	private String getJiesbh(String jesdid) {
		String Jiesbh = "";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String sql = "select bianm from jiesb where id = " + jesdid;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			Jiesbh = rsl.getString("bianm");
		}
		
		rsl.close();
		con.Close();
		return Jiesbh;
	}
	
	private String getHetbh(String jesdid) {
		String Hetbh = "";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String sql = "select ht.hetbh from jiesb js, hetb ht, fahb fh where js.id = fh.jiesb_id and ht.id = fh.hetb_id and js.id = " + jesdid;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			Hetbh = rsl.getString("hetbh");
		}
		
		rsl.close();
		con.Close();
		return Hetbh;
	}
	
	private String getDKGuohd() throws SQLException {
		JDBCcon con = new JDBCcon();
		StringBuffer talbe=new StringBuffer();	//报表输出
		long lngJieslx=0;	//结算类型
		String strJiesd_id="0";	//结算单id
		String strcans=((Visit) getPage().getVisit()).getString1();		//参数
		lngJieslx=Long.parseLong(strcans.substring(0,strcans.indexOf(",")));
		strJiesd_id=strcans.substring(strcans.indexOf(",")+1);

		
			StringBuffer sbsql = new StringBuffer();
			if(lngJieslx==Locale.liangpjs_feiylbb_id||lngJieslx==Locale.meikjs_feiylbb_id){
				sbsql.append(

						"select gongysb_id,meikxxb_id,faz_id,pinzb_id,RIQ,bianm,cheph,maoz,PIZ,JINGZ,BIAOZ, YINGD,YUNS,kuid,KOUD\n" +
						"from(SELECT decode(grouping(g.mingc), 1, g.mingc, g.mingc) gongysb_id,\n" + 
						"                       decode(grouping(m.mingc), 1, '合    计', m.mingc) meikxxb_id,\n" + 
						"                       decode(grouping(c.mingc), 1, null, c.mingc) faz_id,\n" + 
						"                       decode(grouping(p.mingc), 1, null, p.mingc) pinzb_id,\n" + 
						"                       decode(grouping(to_char(FAHB.DAOHRQ, 'yyyy-mm-dd')),\n" + 
						"                              1,\n" + 
						"                              null,\n" + 
						"                              to_char(FAHB.DAOHRQ, 'yyyy-mm-dd')) RIQ,\n" + 
						"                       cy.bianm,\n" + 
						"                       decode(grouping(CHEPB.CHEPH), 1, null, chepb.cheph) cheph,\n" + 
						"                       SUM(CHEPB.Maoz) maoz,\n" + 
						"                       SUM(CHEPB.PIZ) PIZ,\n" + 
						"                       SUM(CHEPB.MAOZ - CHEPB.PIZ) JINGZ,\n" + 
						"                       SUM(CHEPB.BIAOZ) BIAOZ,\n" + 
						"                       SUM(CHEPB.YINGD) YINGD,\n" + 
						"                       SUM(CHEPB.YUNS) YUNS,\n" + 
						"                       SUM(CHEPB.Yingd-chepb.yingk) kuid,\n" + 
						"                       SUM(CHEPB.KOUD) KOUD\n" + 
						"                  FROM FAHB, CHEPB, gongysb g, meikxxb m, pinzb p, chezxxb c,caiyb cy\n" + 
						"                 WHERE FAHB.ID = CHEPB.FAHB_ID\n" + 
						"                   and fahb.zhilb_id=cy.zhilb_id\n" + 
						"                   and gongysb_id = g.id\n" + 
						"                   and meikxxb_id = m.id\n" + 
						"                   and faz_id = c.id\n" + 
						"                   and pinzb_id = p.id\n" + 
						"                   AND FAHB.JIESB_ID = "+strJiesd_id+"\n" + 
						"                   GROUP BY ROLLUP(g.mingc,(m.mingc,p.mingc,c.mingc,to_char(FAHB.DAOHRQ, 'yyyy-mm-dd'),cy.bianm,CHEPB.CHEPH))\n" + 
						"                   having not grouping(g.mingc)=1\n" + 
						"                   )\n" + 
						"                   order by riq ,gongysb_id");



			}else{
				sbsql.append(
						"select gongysb_id,meikxxb_id,faz_id,pinzb_id,RIQ,bianm,cheph,maoz,PIZ,JINGZ,BIAOZ, YINGD,YUNS,kuid,KOUD\n" +
						"from(SELECT decode(grouping(g.mingc), 1, g.mingc, g.mingc) gongysb_id,\n" + 
						"                       decode(grouping(m.mingc), 1, '合    计', m.mingc) meikxxb_id,\n" + 
						"                       decode(grouping(c.mingc), 1, null, c.mingc) faz_id,\n" + 
						"                       decode(grouping(p.mingc), 1, null, p.mingc) pinzb_id,\n" + 
						"                       decode(grouping(to_char(FAHB.DAOHRQ, 'yyyy-mm-dd')),\n" + 
						"                              1,\n" + 
						"                              null,\n" + 
						"                              to_char(FAHB.DAOHRQ, 'yyyy-mm-dd')) RIQ,\n" + 
						"                       cy.bianm,\n" + 
						"                       decode(grouping(CHEPB.CHEPH), 1, null, chepb.cheph) cheph,\n" + 
						"                       to_char(SUM(CHEPB.Maoz)) maoz,\n" + 
						"                       to_char(SUM(CHEPB.PIZ)) PIZ,\n" + 
						"                       to_char(SUM(CHEPB.MAOZ - CHEPB.PIZ)) JINGZ,\n" + 
						"                       to_char(SUM(CHEPB.BIAOZ)) BIAOZ,\n" + 
						"                       (SUM(CHEPB.YINGD)) YINGD,\n" + 
						"                       to_char(SUM(CHEPB.YUNS)) YUNS,\n" + 
						"                       to_char(SUM(CHEPB.Yingd-chepb.yingk)) kuid,\n" + 
						"                       to_char(SUM(CHEPB.KOUD)) KOUD\n" + 
						"                  FROM FAHB, CHEPB, gongysb g, meikxxb m, pinzb p, chezxxb c,caiyb cy\n" + 
						"                 WHERE FAHB.ID = CHEPB.FAHB_ID\n" + 
						"                   and fahb.zhilb_id=cy.zhilb_id\n" + 
						"                   and gongysb_id = g.id\n" + 
						"                   and meikxxb_id = m.id\n" + 
						"                   and faz_id = c.id\n" + 
						"                   and pinzb_id = p.id\n" + 
						"   				and fahb.jiesb_id =\n" + 
						"       			(select DIANCJSMKB_ID from jiesyfb where id ="+strJiesd_id+")\n "+
						"                   GROUP BY ROLLUP(g.mingc,(m.mingc,p.mingc,c.mingc,to_char(FAHB.DAOHRQ, 'yyyy-mm-dd'),cy.bianm,CHEPB.CHEPH))\n" + 
						"                   having not grouping(g.mingc)=1\n" + 
						"                   )\n" + 
						"                   order by riq ,gongysb_id");

			} 
			ResultSet rs=con.getResultSet(sbsql.toString());
			rs=con.getResultSet(sbsql.toString());
			
			String sqlstr =
			"                   select nvl('数量经办人:','') gongysb_id,\n" + 
			"                          '' meikxxb_id,\n" + 
			"                          '' faz_id,\n" + 
			"                          '' pinzb_id,\n" + 
			"                          '' riq,\n" + 
			"                          '' bianm,\n" + 
			"                          '' cheph,\n" + 
			"                          '' maoz,\n" + 
			"                          '' piz,\n" + 
			"                          '' jingz,\n" + 
			"                          '' biaoz,\n" + 
			"                          nvl('数量负责人:','') yingd,\n" + 
			"                          '' yuns,\n" + 
			"                          '' kuid,\n" + 
			"                          '' koud from dual";
			ResultSet rs1 = con.getResultSet(sqlstr.toString());
			Report rt = new Report(); //报表定义
			String ArrHeader[][]=new String[1][15];
//			1120
			ArrHeader[0]=new String[] {"供应商","煤矿","发站","品种","到货日期","编码","车号","毛重","皮重","净重","票重","盈吨","运损","亏吨","扣吨"};
			int ArrWidth[]=new int[] {91,91,70,50,80,60,60,50,50,50,50,40,40,50,50};
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			// 数据
			rt.setTitle("来煤过衡单",ArrWidth);
			Table tab = new Table(rs, 1, 0, 0);
			tab.AddTableData(rs1);
			rs.close();
			rs1.close();
			rt.setBody(tab);
			
			rt.setDefaultTitle(10, 6, "结算单号：" + getJiesbh(strJiesd_id) + "<br>" + "合同编号：" + getHetbh(strJiesd_id) +"",Table.ALIGN_LEFT);
			rt.body.setWidth(ArrWidth);

			rt.body.setPageRows(24);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.ShowZero = true;
			
			
			rt.body.setCells(rt.body.getRows() - 2, 1, rt.body.getRows() - 2, 2, Table.PER_ALIGN, Table.ALIGN_RIGHT);
			//rt.body.mergeCell(rt.body.getRows() - 2, 1, rt.body.getRows() - 2, 2);
			
//			rt.body.setRowHeight(rt.body.getRows() - 1, 5);
//			
			rt.body.setRowCells(rt.body.getRows() , Table.PER_BORDER_BOTTOM, 0);
			rt.body.setRowCells(rt.body.getRows() , Table.PER_BORDER_RIGHT, 0);
			rt.body.setBorder(0, 0,2, 0);
			rt.body.setCells(1, 1, rt.body.getRows() - 1, 1, Table.PER_BORDER_LEFT, 2);
			rt.body.setCells(1, 15, rt.body.getRows() - 2, 15, Table.PER_BORDER_RIGHT,2);
			rt.body.setRowCells(rt.body.getRows() - 1, Table.PER_BORDER_BOTTOM, 2);
			
			rt.body.setCellAlign(rt.body.getRows(), 1, Table.ALIGN_LEFT);
			rt.body.mergeCell(rt.body.getRows(), 12, rt.body.getRows(), 15);
			rt.body.setCellAlign(rt.body.getRows(), 12, Table.ALIGN_LEFT);

			
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "打印日期:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA, Table.ALIGN_RIGHT);
			
//			构造表
			talbe.append(rt.getAllPagesHtml());
//			 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
		return talbe.toString();
	}
	
	private String getGuohd() {
		JDBCcon con = new JDBCcon();
		StringBuffer talbe=new StringBuffer();	//报表输出
		long lngJieslx=0;	//结算类型
		String strJiesd_id="0";	//结算单id
		String strcans=((Visit) getPage().getVisit()).getString1();		//参数
		lngJieslx=Long.parseLong(strcans.substring(0,strcans.indexOf(",")));
		strJiesd_id=strcans.substring(strcans.indexOf(",")+1);
		//锦州public static final long liangpjs_feiylbb_id=1;	//两票结算
		//public static final long meikjs_feiylbb_id=2;	//煤款结算
		//public static final long guotyf_feiylbb_id=3;	//国铁运费
		
		boolean isMeikGuohd=false;
		if(!MainGlobal.getXitxx_item("结算过衡单", "到货日期过衡单", "0", "是").equals("否")){
			isMeikGuohd=true;
			StringBuffer sbsql = new StringBuffer();
			if(lngJieslx==Locale.liangpjs_feiylbb_id||lngJieslx==Locale.meikjs_feiylbb_id){
				sbsql.append(
						"SELECT RIQ, CHEPH, MAOZ, PIZ, JINGZ, BIAOZ, KOUD\n" +
						"  FROM (SELECT RIQ, CHEPH, MAOZ, PIZ, JINGZ, BIAOZ, KOUD, 1 ID\n" + 
						"          FROM (SELECT DECODE(TO_CHAR(FAHB.DAOHRQ, 'yyyy-mm-dd'),\n" + 
						"                              NULL,\n" + 
						"                              '合    计',\n" + 
						"                              TO_CHAR(FAHB.DAOHRQ, 'yyyy-mm-dd')) RIQ,\n" + 
						"                       DECODE(CHEPB.CHEPH, NULL, '合    计', CHEPB.CHEPH) CHEPH,\n" + 
						"                       TO_CHAR(SUM(CHEPB.MAOZ)) MAOZ,\n" + 
						"                       TO_CHAR(SUM(CHEPB.PIZ)) PIZ,\n" + 
						"                       TO_CHAR(SUM(CHEPB.MAOZ - CHEPB.PIZ)) JINGZ,\n" + 
						"                       TO_CHAR(SUM(CHEPB.BIAOZ)) BIAOZ,\n" + 
						"                       TO_CHAR(SUM(CHEPB.KOUD + CHEPB.KOUS + CHEPB.KOUZ)) KOUD\n" + 
						"                  FROM FAHB, CHEPB\n" + 
						"                 WHERE FAHB.ID = CHEPB.FAHB_ID\n" + 
						"                   AND FAHB.JIESB_ID = " + strJiesd_id + "\n" + 
						"                 GROUP BY ROLLUP(FAHB.DAOHRQ, CHEPB.CHEPH)\n" + 
						"                HAVING NOT(GROUPING(FAHB.DAOHRQ) + GROUPING(CHEPB.CHEPH) = 1)\n" + 
						"                 ORDER BY FAHB.DAOHRQ, CHEPB.CHEPH)\n" + 
						"        UNION\n" + 
						"        SELECT '' RIQ,\n" + 
						"               '' CHEPH,\n" + 
						"               '' MAOZ,\n" + 
						"               '' PIZ,\n" + 
						"               '' JINGZ,\n" + 
						"               '' BIAOZ,\n" + 
						"               '' KOUD,\n" + 
						"               2 ID\n" + 
						"          FROM DUAL" +
						"	     UNION" + 
						"        SELECT '数量经办人：' RIQ,\n" + 
						"               '' CHEPH,\n" + 
						"               '' MAOZ,\n" + 
						"               '' PIZ,\n" + 
						"               '' JINGZ,\n" + 
						"               '数量负责人：' BIAOZ,\n" + 
						"               '' KOUD,\n" + 
						"               3 ID\n" + 
						"          FROM DUAL)\n" + 
						" ORDER BY ID, RIQ");
			}else{
				isMeikGuohd=false;
				sbsql.append("select to_char(fahb.daohrq,'yyyy-mm-dd')riq,chepb.cheph,chepb.maoz,chepb.piz,chepb.maoz-chepb.piz jingz,chepb.biaoz,chepb.koud+chepb.kous+chepb.kouz koud\n" +
				"from fahb,chepb\n" + 
				"where fahb.id=chepb.fahb_id\n" + 
				"and fahb.jiesb_id=(select DIANCJSMKB_ID from jiesyfb where id="+strJiesd_id+")\n" + 
				"order by riq ,chepb.id");
			}
			
			ResultSet rs=con.getResultSet(sbsql.toString());
			rs=con.getResultSet(sbsql.toString());
			Report rt = new Report(); //报表定义
			String ArrHeader[][]=new String[1][7];
//			1120
			ArrHeader[0]=new String[] {"来煤时间","车号","毛重(吨)","皮重(吨)","净重(吨)","票重(吨)","扣吨扣水(吨)"};
			int ArrWidth[]=new int[] {150,91,91,91,91,91,91};
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			// 数据
			rt.setTitle("来煤过衡单",ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.setDefaultTitle(6, 3, "结算单号：" + getJiesbh(strJiesd_id) + "<br>" + "合同编号：" + getHetbh(strJiesd_id) + "",Table.ALIGN_LEFT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(Report.PAPER_ROWS);
			
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// 表头数据
//			rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(),3);
//			rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
//			rt.body.mergeFixedRow();
			rt.body.ShowZero = true;
			
			if (isMeikGuohd){
				rt.body.setCells(rt.body.getRows() - 2, 1, rt.body.getRows() - 2, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
				rt.body.mergeCell(rt.body.getRows() - 2, 1, rt.body.getRows() - 2, 2);
				
				rt.body.setRowHeight(rt.body.getRows() - 1, 5);
				
				rt.body.setRowCells(rt.body.getRows() - 1, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setRowCells(rt.body.getRows() - 1, Table.PER_BORDER_RIGHT, 0);
				rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM, 0);
				rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_RIGHT, 0);
				rt.body.setBorder(0, 0, 2, 0);
				rt.body.setCells(1, 1, rt.body.getRows() - 2, 1, Table.PER_BORDER_LEFT, 2);
				rt.body.setCells(1, 7, rt.body.getRows() - 2, 7, Table.PER_BORDER_RIGHT, 2);
				rt.body.setRowCells(rt.body.getRows() - 2, Table.PER_BORDER_BOTTOM, 2);
			}else{
				rt.createDefautlFooter(ArrWidth);
				rt.footer.setCellValue(2, 1, "经办人:");
				rt.footer.setCellValue(2, 5, "负责人:");
			}
//			rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
//			构造表
			talbe.append(rt.getAllPagesHtml());
//			 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
		}else{
			StringBuffer sbsql = new StringBuffer();
			ResultSet rs=null;
			String faz="";
			String gongmdw="";
			String hetbh="";
			String famsj="";
			int p=0;
			String Jiesslzcfs="biaoz+yingk-koud-kous-kouz"; //结算数量组成方式
//			得到结算数量的设置组成方式
				if(lngJieslx==Locale.liangpjs_feiylbb_id||lngJieslx==Locale.meikjs_feiylbb_id){
		//			两票和煤款结算
					
						sbsql.append(
								"select jiesszb.zhi from jiesszb,jiesszbmb bm\n" +
								"                         where jiesszfab_id in (\n" + 
								"                           select id from jiesszfab where id in (\n" + 
								"                           select jiesszfab_id from jiesszfahtglb\n" + 
								"                                  where hetb_id in (select hetb_id from jiesb where id in ("
									+strJiesd_id+") and rownum=1))\n" + 
								"                         )\n" + 
								"                         and jiesszb.jiesszbmb_id=bm.id\n" + 
								"                         and bm.bianm='"+Locale.jiesslzcfs_jies+"'");
						
						ResultSetList rsl=con.getResultSetList(sbsql.toString());
						if(rsl.next()){
							
							Jiesslzcfs=rsl.getString("zhi");
						}
						
						
						sbsql.setLength(0);
						
						sbsql.append(
			
								"select distinct fh.lie_id from fahb fh,\n" +
								"(select cp.fahb_id\n" + 
								"       from chepb cp,guohb gh\n" + 
								"            where cp.guohb_id = gh.id \n" + 
								"                  and cp.fahb_id in\n" + 
								"                  (select id from fahb where jiesb_id in ("+strJiesd_id+") \n" +
								"					)\n" + 
								"  ) cp\n" + 
								"\n" + 
								"  where fh.id=cp.fahb_id");
						
						rsl=con.getResultSetList(sbsql.toString());
						
						while(rsl.next()){
							
							sbsql.setLength(0);
							
							sbsql.append(
									"select min(cz.mingc) as faz,min(g.mingc) as gongysmc,\n" +
									"       min(nvl(ht.hetbh,'')) as hetbh,to_char(min(f.fahrq),'yyyy-MM-dd') as famsj\n" + 
									"       from fahb f,chezxxb cz,gongysb g,hetb ht\n" + 
									"       where f.faz_id = cz.id\n" + 
									"             and f.gongysb_id = g.id\n" + 
									"             and f.hetb_id = ht.id(+)\n" + 
									"             and f.lie_id ="+rsl.getString("lie_id")
							);
							rs=con.getResultSet(sbsql.toString());
							try {
								if(rs.next()){
									
									faz=rs.getString("faz");
									gongmdw=rs.getString("gongysmc");
									hetbh=Jiesdcz.nvlStr(rs.getString("hetbh"));
									famsj=rs.getString("famsj");
								}
							} catch (SQLException e) {
								// TODO 自动生成 catch 块
								e.printStackTrace();
							}
							
							sbsql.setLength(0);
							sbsql.append(
									"select decode(guohsj,null,'合计',to_char(guohsj,'yyyy-MM-dd hh24:mi:ss')) as guohsj,\n" +
									"       cheph,sum(cp.maoz) as maoz,sum(cp.piz) as piz,sum(cp.jingz) as jingz,\n" + 
									"       sum(cp.biaoz) as biaoz,sum(cp.koud) as koud,sum(cp.jiessl) as jiessl\n" + 
									"        from fahb fh,\n" + 
									"(select cp.fahb_id,gh.guohsj,cp.cheph,cp.maoz,cp.piz,(cp.maoz-cp.piz) as jingz,\n" + 
									"       cp.biaoz as biaoz,cp.zongkd as koud,("+Jiesslzcfs+") as jiessl\n" + 
									"       from chepb cp,guohb gh\n" + 
									"            where cp.guohb_id = gh.id \n" + 
									"                  and cp.fahb_id in\n" + 
									"                  (select id from fahb where lie_id="+rsl.getString("lie_id")+")\n" + 
									"  ) cp\n" + 
									"\n" + 
									"  where fh.id=cp.fahb_id\n" + 
									"        and fh.lie_id="+rsl.getString("lie_id")+"\n" + 
									"        group by rollup(guohsj,cheph)\n" + 
									"        having not (grouping(cheph)=1 and grouping(guohsj)=0)"
							);
						}
				}else{
		//			统一为运费类结算(注：运费结算的合同信息不显示,只显示煤款的信息)
//					结算设置
					Jiesslzcfs="maoz-piz-zongkd";	//结算数量组成方式
					
					sbsql.append(
							"select jiesszb.zhi from jiesszb,jiesszbmb bm\n" +
							"                         where jiesszfab_id in (\n" + 
							"                           select id from jiesszfab where id in (\n" + 
							"                           select jiesszfab_id from jiesszfahtglb\n" + 
							"                                  where hetb_id in (select hetb_id from jiesyfb where id in ("
								+strJiesd_id+") and rownum=1))\n" + 
							"                         )\n" + 
							"                         and jiesszb.jiesszbmb_id=bm.id\n" + 
							"                         and bm.bianm='"+Locale.yunfjsslzcfs_jies+"'");
					
					ResultSetList rsl=con.getResultSetList(sbsql.toString());
					if(rsl.next()){
						
						Jiesslzcfs=rsl.getString("zhi");
					}
					
					if(lngJieslx==Locale.daozyf_feiylbb_id){
//						倒装运费
						sbsql.setLength(0);
						
						sbsql.append(
								

								"select\n" +
								"       distinct f.lie_id\n" + 
								"       from fahb f,daozcpb dz\n" + 
								"       where f.id = dz.fahb_id\n" + 
								"             and dz.jiesb_id="+strJiesd_id);
						
						rsl=con.getResultSetList(sbsql.toString());
						
						while(rsl.next()){
							
							sbsql.setLength(0);
							
							sbsql.append(
									"select min(cz.mingc) as faz,min(g.mingc) as gongysmc,\n" +
									"       min(nvl(ht.hetbh,'')) as hetbh,to_char(min(f.fahrq),'yyyy-MM-dd') as famsj\n" + 
									"       from fahb f,chezxxb cz,gongysb g,hetb ht\n" + 
									"       where f.faz_id = cz.id\n" + 
									"             and f.gongysb_id = g.id\n" + 
									"             and f.hetb_id = ht.id(+)\n" + 
									"             and f.lie_id ="+rsl.getString("lie_id")
							);
							rs=con.getResultSet(sbsql.toString());
							try {
								if(rs.next()){
									
									faz=rs.getString("faz");
									gongmdw=rs.getString("gongysmc");
									hetbh=Jiesdcz.nvlStr(rs.getString("hetbh"));
									famsj=rs.getString("famsj");
								}
							} catch (SQLException e) {
								// TODO 自动生成 catch 块
								e.printStackTrace();
							}
							
							sbsql.setLength(0);
							sbsql.append(

									"select\n" +
									"    decode(dc.qingcsj,null,'合计',to_char(dc.qingcsj,'yyyy-MM-dd hh24:mi:ss')) as laimsj,\n" + 
									"    nvl(dc.cheph,'') as cheh, sum(nvl(dc.maoz,0)) as maoz,\n" + 
									"    sum(nvl(dc.piz,0)) as piz,\n" + 
									"    sum(nvl(dc.maoz,0)-nvl(dc.piz,0)-nvl(dc.zongkd,0)) as jingz,\n" + 
									"    sum(nvl(dc.biaoz,0)) as biaoz,sum(nvl(dc.zongkd,0)) as koud,\n" + 
									"    sum(dc.maoz-dc.piz-dc.zongkd) as jiessl\n" + 
									"    from daozcpb dc,fahb fh\n" + 
									"    where dc.jiesb_id="+strJiesd_id+"\n" + 
									"          and dc.fahb_id = fh.id\n" + 
									"          and fh.lie_id ="+rsl.getString("lie_id")+"\n" + 
									"    group by rollup(qingcsj,cheph)\n" + 
									"    having not (grouping(cheph)=1 and grouping(qingcsj)=0)"

							);
						}
					}else{
						
//						其它运费
						sbsql.setLength(0);
						
						sbsql.append(
								

								"select distinct f.lie_id\n" +
								"        from chepb cp,yunfdjb yd,danjcpb dj,fahb f\n" + 
								"        where yd.id=dj.yunfdjb_id\n" + 
								"              and dj.chepb_id=cp.id\n" + 
								"              and f.id=cp.fahb_id\n" + 
								"              and yd.feiylbb_id="+lngJieslx+"\n" + 
								"              and dj.yunfjsb_id="+strJiesd_id);
						
						rsl=con.getResultSetList(sbsql.toString());
						
						while(rsl.next()){
							
							sbsql.setLength(0);
							
							sbsql.append(
									"select min(cz.mingc) as faz,min(g.mingc) as gongysmc,\n" +
									"       min(nvl(ht.hetbh,'')) as hetbh,to_char(min(f.fahrq),'yyyy-MM-dd') as famsj\n" + 
									"       from fahb f,chezxxb cz,gongysb g,hetb ht\n" + 
									"       where f.faz_id = cz.id\n" + 
									"             and f.gongysb_id = g.id\n" + 
									"             and f.hetb_id = ht.id(+)\n" + 
									"             and f.lie_id ="+rsl.getString("lie_id")
							);
							rs=con.getResultSet(sbsql.toString());
							try {
								if(rs.next()){
									
									faz=rs.getString("faz");
									gongmdw=rs.getString("gongysmc");
									hetbh=Jiesdcz.nvlStr(rs.getString("hetbh"));
									famsj=rs.getString("famsj");
								}
							} catch (SQLException e) {
								// TODO 自动生成 catch 块
								e.printStackTrace();
							}
							
							sbsql.setLength(0);
							sbsql.append(

									"select\n" +
									"       decode(guohsj,null,'合计',to_char(guohsj,'yyyy-MM-dd hh24:mi:ss')) as guohsj,\n" + 
									"       cheph,sum(maoz) as maoz,sum(piz) as piz,sum(jingz) as jingz,\n" + 
									"		sum(biaoz) as biaoz,sum(koud) as koud,sum(jiessl) as jiessl\n" + 
									"       from\n" + 
									"       (select g.guohsj,c.cheph,c.maoz,c.piz,(c.maoz-c.piz) as jingz,\n" + 
									"              c.biaoz,c.zongkd as koud,(biaoz) as jiessl\n" + 
									"              from chepb c,guohb g,danjcpb d\n" + 
									"              where c.guohb_id = g.id\n" + 
									"                    and c.id = d.chepb_id\n" + 
									"                    and d.yunfjsb_id = "+strJiesd_id+"\n" + 
									"                    and c.fahb_id in\n" + 
									"                    (select id from fahb where lie_id="+rsl.getString("lie_id")+"))\n" + 
									"       group by rollup(guohsj,cheph)\n" + 
									"       having not (grouping(cheph)=1 and grouping(guohsj)=0)"

							);
						}
						
					}
					
				}
			
				rs=con.getResultSet(sbsql.toString());
				
				Report rt = new Report(); //报表定义
				String ArrHeader[][]=new String[1][8];
//				1120
				ArrHeader[0]=new String[] {"来煤时间","车号","毛重(吨)","皮重(吨)","净重(吨)","票重(吨)","扣吨扣水(吨)","结算煤量(吨)"};
				int ArrWidth[]=new int[] {150,91,91,91,91,91,91,91};
				int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
				rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
				// 数据
				rt.setTitle("来煤过衡单",ArrWidth);
				rt.setDefaultTitleLeft("发站:"+faz+"<br>供煤单位:"+gongmdw+"",2);
				rt.setDefaultTitleRight("合同编号:"+hetbh+"<br>发煤时间:"+famsj, 2);
				rt.setBody(new Table(rs, 1, 0, 0));
				
//				rt.setDefaultTitle(2, 3, "结算单号：" + getJiesbh(strJiesd_id), Table.ALIGN_LEFT);
//				rt.setDefaultTitle(5, 3, "合同号：" + getHetbh(strJiesd_id), Table.ALIGN_RIGHT);
				rt.setDefaultTitle(6, 3, "结算单号：" + getJiesbh(strJiesd_id) + "<br>" + "合同编号：" + getHetbh(strJiesd_id) + "",Table.ALIGN_LEFT);
				
				rt.body.setWidth(ArrWidth);
//				rt.body.setPageRows(20);
				rt.body.setPageRows(Report.PAPER_COLROWS);
//				增加长度的拉伸
				rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
				rt.body.setHeaderData(ArrHeader);// 表头数据
				
//				rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(),3);
//				rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
//				rt.body.mergeFixedRow();
				rt.body.ShowZero = true;
								
//				rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
//				构造表
				talbe.append(rt.getAllPagesHtml(p));
				p++;
			
			// 设置页数
			_AllPages=p;
			_CurrentPage = 1;
			
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			
		}
		return talbe.toString();
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
	
	// 窗口弹出js_begin
	public String getWindowScript() {
		return ((Visit) getPage().getVisit()).getString6();
	}

	public void setWindowScript(String value) {
		((Visit) getPage().getVisit()).setString6(value);
	}
	// 窗口弹出js_end
	
	public boolean isLx(String a,String b){
		boolean lx = false;
		int i=0;
		while(i<b.length()){
			if(a.equals(b.substring(i,i+1))){
				return lx=true;
			}
			i++;
		}
		return lx;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			
			visit.setString6("");	//要弹出的页面
			
			if (cycle.getRequestContext().getParameters("lx") != null) {
				// jiesb_id
				visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			}

			// begin方法里进行初始化设置
			visit.setString4(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {
				visit.setString4(pagewith);
			}
			// visit.setString4(null);保存传递的非默认纸张的样式
		}
	}
}