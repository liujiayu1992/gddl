package com.zhiren.jt.zdt.niancgjhqpb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 * @2009-08-19
 * 年采购计划棋盘表
 *
 */

/*
 *  ly
 * 2009-08-26
 * 
 *修改报表类型和需求量取值表条件判断
 */
public class NiancgjhqpReport  extends BasePage implements PageValidateListener{

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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

	public String getPrintTable() {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		int[] ArrWidth;
		
//		年份条件
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
//		月份条件
		String intmonth;
		String strmonth;
		intmonth = getRbvalue();
		if(intmonth.equals("1")){
			strmonth = "1";
		} else {
			strmonth = "1-"+intmonth;
		}
//		机组状态
		String jizzt="";
		String jiztt="";
		if (getChartDropDownValue().getValue().equals("全部") || getChartDropDownValue().getValue()=="全部"){
			jiztt="(全国订货含新机分二级单位明细)";
			jizzt="";
		}else if (getChartDropDownValue().getValue().equals("现役机组") || getChartDropDownValue().getValue()=="现役机组"){
			jiztt="(全国订货现役分二级单位明细)";
			jizzt=" and nc.jizzt ="+getChartDropDownValue().getId();
		}else if (getChartDropDownValue().getValue().equals("新增机组") || getChartDropDownValue().getValue()=="新增现役机组"){
			jiztt="全国订货新增分二级单位明细)";
			jizzt=" and nc.jizzt ="+getChartDropDownValue().getId();
		}
		String jihkj="";
//		计划口径
		if (getJihkjDropDownValue().getValue().equals("全部") || getJihkjDropDownValue().getValue()=="全部"){
			jihkj=" ";
		}else {
			jihkj=" and nc.jihkjb_id ="+getJihkjDropDownValue().getId()+"\n";
		}
			
		
//		需求量取值表条件
		String tableName = "";
		if(getBaoblxDropDownValue().getValue()=="电厂申请"||getBaoblxDropDownValue().getValue().equals("电厂申请")){
			tableName = "niancgjh";
		} else if(getBaoblxDropDownValue().getValue()=="二级公司上报"||getBaoblxDropDownValue().getValue().equals("二级公司上报")){
			tableName = "niancgjh_fgs";
		} else if(getBaoblxDropDownValue().getValue()=="集团核定"||getBaoblxDropDownValue().getValue().equals("集团核定")){
			tableName = "niancgjh_hd";
		}
//		电厂条件
		String dcid = this.getTreeid();
		String title = "";
		String diancid = "";
		String idlx = "";
		String zongc = "";
		if(getDiancTreeJib()==1){
			title = "集团公司"+jiztt+(intyear-1)+"年重点合同量、"+strmonth+"月重点合同计划兑现及"+(intyear)+"年需求";
			diancid = "";
			idlx = "fuid";
			zongc = "集团公司";
		} else if(getDiancTreeJib()==2){
			title = getTreeDiancmc(this.getTreeid())+jiztt+(intyear-1)+"年重点合同量、"+strmonth+"月重点合同计划兑现及"+(intyear)+"年需求";
			diancid = "	and (dc.id = "+dcid+" or dc.fuid = "+dcid+" or dc.shangjgsid = "+dcid+")\n";
			idlx = "id";
			zongc = getTreeDiancmc(this.getTreeid());
		} else if(getDiancTreeJib()==3){
			title = getTreeDiancmc(this.getTreeid())+jiztt+(intyear-1)+"年重点合同量、"+strmonth+"月重点合同计划兑现及"+(intyear)+"年需求";
			diancid = "	and dc.id = "+dcid+"\n";
			idlx = "id";
			zongc = getTreeDiancmc(this.getTreeid());
		}
		
		
//		得到行标题-供应商
		StringBuffer strRow = new StringBuffer();
	      strRow.append(
	    		  "select decode(grouping(g.smc) + grouping(g.dqmc),2,'合计',1,g.smc || '小计',g.dqmc) as 供应商\n" +
	    		  "  from ((select distinct n.gongysb_id\n" + 
	    		  "           from niandhtqkb n,diancxxb dc\n" + 
	    		  "          where n.jihkjb_id = 1\n" + 
	    		  "            and n.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "            and n.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "			   and n.diancxxb_id = dc.id\n" +
	    		  diancid + 
	    		  "         ) union (select distinct yt.gongysb_id\n" + 
	    		  "                    from yueslb y, yuetjkjb yt,diancxxb dc\n" + 
	    		  "                   where y.yuetjkjb_id = yt.id\n" + 
	    		  "                     and yt.jihkjb_id = 1\n" + 
	    		  "                     and yt.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "                     and yt.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "						and yt.diancxxb_id = dc.id\n" +	
	    		  diancid + 
	    		  "                  ) union\n" + 
	    		  "        (select distinct nc.gongysb_id\n" + 
	    		  "           from "+tableName+" nc,diancxxb dc\n" + 
	    		  "          where 1 = 1\n" + 
	    		  jihkj + 
	    		  "            and nc.riq >= to_date('"+(intyear)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "            and nc.riq <= to_date('"+(intyear)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "            "+jizzt+"\n" + 
	    		  "			   and nc.diancxxb_id = dc.id\n" +	
	    		  diancid +  
	    		  "         )) b,\n" + 
	    		  "       vwgongys g\n" + //,shengfb s
	    		  " where b.gongysb_id = g.id\n" + 
	    		  //"   and g.shengfb_id = s.id\n" + 
	    		  " group by rollup(g.smc, g.dqmc)\n" + 
	    		  " order by grouping(g.smc) desc, g.smc, grouping(g.dqmc) desc, g.dqmc\n");
	      
	      //得到列标题-电厂
	      StringBuffer strCol = new StringBuffer();
	      strCol.append(
	    		  "select distinct decode(dc.mingc,'中电投','集团公司',dc.mingc) as 电厂\n" +
	    		  "  from ((select distinct d."+idlx+"\n" + 
	    		  "           from niandhtqkb n, diancxxb d\n" + 
	    		  "          where n.jihkjb_id = 1\n" + 
	    		  "            and n.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "            and n.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "            and n.diancxxb_id = d.id) union\n" + 
	    		  "        (select distinct d."+idlx+"\n" + 
	    		  "           from yueslb y, yuetjkjb yt, diancxxb d\n" + 
	    		  "          where y.yuetjkjb_id = yt.id\n" + 
	    		  "            and yt.jihkjb_id = 1\n" + 
	    		  "            and yt.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "            and yt.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "            and yt.diancxxb_id = d.id) union\n" + 
	    		  "        (select distinct d."+idlx+"\n" + 
	    		  "           from "+tableName+" nc, diancxxb d\n" + 
	    		  "          where 1 = 1\n" + 
	    		  jihkj + 
	    		  "            and nc.riq >= to_date('"+(intyear)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "            and nc.riq <= to_date('"+(intyear)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "            "+jizzt+"\n" +
	    		  "            and nc.diancxxb_id = d.id) union (select "+dcid+" as "+idlx+" from dual)) b,\n" + 
	    		  "       diancxxb dc\n" + 
	    		  " where b."+idlx+" = dc.id\n" + 
	    		  diancid +
	    		  " order by decode(电厂,'"+zongc+"',0,1),电厂\n");
	      
	      //总数据
	      StringBuffer sbsql = new StringBuffer();
	      sbsql.append(

	    		  //"--数据\n" +
	    		  "select decode(grouping(d.mingc), 1, '"+zongc+"', d.mingc) as 电厂,\n" + 
	    		  "       decode(grouping(g.smc)+grouping(g.dqmc),2,'合计',1,decode(grouping(g.dqmc),1,g.smc||'小计',g.dqmc),g.dqmc) as 供应商,\n" + 
	    		  "       nvl(sum(yi.hej),0) as 重点订货量,\n" + 
	    		  "       round_new(nvl(sum(er.laimsl),0)/10000,2) as 重点订货兑现量,\n" + 
	    		  "       round_new(nvl(sum(san.laimsl),0)/10000,2) 市场交易合同量,\n" + 
	    		  "       round_new(nvl(sum(si.nianjhcgl),0)/10000,2) as 需求量\n" + 
	    		  "  from \n" + //--总表
	    		  "        ((select distinct dc."+idlx+" as diancxxb_id, n.gongysb_id\n" + 
	    		  "            from niandhtqkb n, diancxxb dc\n" + 
	    		  "           where n.jihkjb_id = 1\n" + 
	    		  "             and n.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "             and n.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "             and n.diancxxb_id = dc.id "+diancid+") union\n" + 
	    		  "         (select distinct dc."+idlx+" as diancxxb_id, yt.gongysb_id\n" + 
	    		  "            from yueslb y, yuetjkjb yt, diancxxb dc\n" + 
	    		  "           where y.yuetjkjb_id = yt.id\n" + 
	    		  "             and yt.jihkjb_id = 1\n" + 
	    		  "             and yt.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "             and yt.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "             and yt.diancxxb_id = dc.id "+diancid+") union\n" + 
	    		  "         (select distinct dc."+idlx+" as diancxxb_id, nc.gongysb_id\n" + 
	    		  "            from "+tableName+" nc, diancxxb dc\n" + 
	    		  "           where 1 = 1\n" + 
	    		  jihkj + 
	    		  "             and nc.riq >= to_date('"+(intyear)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "             and nc.riq <= to_date('"+(intyear)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "             "+jizzt+"\n" + 
	    		  "             and nc.diancxxb_id = dc.id "+diancid+")) zb,\n" + 
	    		  //"       --重点订货量\n" + 
	    		  "       (select dc."+idlx+" as diancxxb_id, n.gongysb_id, sum(n.hej) as hej\n" + 
	    		  "          from niandhtqkb n, diancxxb dc\n" + 
	    		  "         where n.jihkjb_id = 1\n" + 
	    		  "           and n.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "           and n.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "           and n.diancxxb_id = dc.id "+diancid+" group by dc."+idlx+",n.gongysb_id) yi,\n" + 
	    		  //"       --重点订货对限量\n" + 
	    		  "       (select dc."+idlx+" as diancxxb_id, yt.gongysb_id, sum(y.laimsl) as laimsl\n" + 
	    		  "          from yueslb y, yuetjkjb yt, diancxxb dc\n" + 
	    		  "         where y.yuetjkjb_id = yt.id\n" + 
	    		  "           and yt.jihkjb_id = 1 and y.fenx='本月' \n" + 
	    		  "           and yt.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "           and yt.riq <= last_day(to_date('"+(intyear-1)+"-"+intmonth+"-01', 'yyyy-MM-dd'))\n" + 
	    		  "           and yt.diancxxb_id = dc.id "+diancid+" group by dc."+idlx+",yt.gongysb_id) er,\n" + 
	    		  //"       --市场交易合同量\n" + 
	    		  "       (select dc."+idlx+" as diancxxb_id, yt.gongysb_id, sum(y.laimsl) as laimsl\n" + 
	    		  "          from yueslb y, yuetjkjb yt, diancxxb dc\n" + 
	    		  "         where y.yuetjkjb_id = yt.id\n" + 
	    		  "           and yt.jihkjb_id = 2 and y.fenx='本月' \n" + 
	    		  "           and yt.riq >= to_date('"+(intyear-1)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "           and yt.riq <= to_date('"+(intyear-1)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "           and yt.diancxxb_id = dc.id "+diancid+" group by dc."+idlx+",yt.gongysb_id) san,\n" + 
	    		  //"       --需求量\n" + 
	    		  "       (select dc."+idlx+" as diancxxb_id, nc.gongysb_id, sum(nc.nianjhcgl) as nianjhcgl\n" + 
	    		  "          from "+tableName+" nc, diancxxb dc\n" + 
	    		  "         where 1 = 1\n" + 
	    		  jihkj + 
	    		  "           and nc.riq >= to_date('"+(intyear)+"-01-01', 'yyyy-MM-dd')\n" + 
	    		  "           and nc.riq <= to_date('"+(intyear)+"-12-31', 'yyyy-MM-dd')\n" + 
	    		  "           "+jizzt+"\n" +
	    		  "           and nc.diancxxb_id = dc.id "+diancid+" group by dc."+idlx+",nc.gongysb_id) si,\n" + 
	    		  "       diancxxb d,\n" + 
	    		  "       vwgongys g\n" + 
	    		  //"       ,shengfb s\n" + 
	    		  " where zb.diancxxb_id = yi.diancxxb_id(+)\n" + 
	    		  "   and zb.diancxxb_id = er.diancxxb_id(+)\n" + 
	    		  "   and zb.diancxxb_id = san.diancxxb_id(+)\n" + 
	    		  "   and zb.diancxxb_id = si.diancxxb_id(+)\n" + 
	    		  "   and zb.gongysb_id = yi.gongysb_id(+)\n" + 
	    		  "   and zb.gongysb_id = er.gongysb_id(+)\n" + 
	    		  "   and zb.gongysb_id = san.gongysb_id(+)\n" + 
	    		  "   and zb.gongysb_id = si.gongysb_id(+)\n" + 
	    		  "   and zb.diancxxb_id = d.id\n" + 
	    		  "   and zb.gongysb_id = g.id\n" + 
	    		  //"   and g.shengfb_id = s.id\n" + 
	    		  " group by cube(d.mingc, g.smc, g.dqmc)\n");
	      
	      try{
				ResultSetList rl = cn.getResultSetList(sbsql.toString());
				
				if(!rl.next()){
					return "无数据！";
				}
				
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}  

	    cd.setRowNames("供应商");
	    cd.setColNames("电厂");
	    cd.setDataNames("重点订货量,重点订货兑现量,市场交易合同量,需求量");
	    cd.setDataOnRow(false);
	    cd.setRowToCol(false);
	    //生成棋盘表数组
	    cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		
	    ArrWidth = new int[cd.DataTable.getCols()];
		int cols = ArrWidth.length;
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 65;
		}
		ArrWidth[0] = 80;
//		ArrWidth[1] = 80;
		rt.setBody(cd.DataTable);
		
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero = false;
		
		rt.setTitle(title, ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		cd.DataTable.setCellValue(1, 1, "煤矿名称");
		cd.DataTable.setCellValue(2, 1, "煤矿名称");
		
		String nianf = ""+(getNianfValue().getId()-1);
		String xx = nianf.substring(2);
		for (int i=2;i<=cd.DataTable.getCols();i=i+4){
			cd.DataTable.setCellValue(2, i, xx+"年重点订货量");
			cd.DataTable.setCellValue(2, i+1, strmonth+"月重点订货兑现量");
			cd.DataTable.setCellValue(2, i+2,xx+"年市场交易合同量");
			cd.DataTable.setCellValue(2, i+3, (getNianfValue().getId())+"需求量");
		}
		rt.body.mergeFixedRowCol();
		rt.setDefaultTitle(1, 4, "制表单位:"+this.getDiancmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(cols-1, 2, "单位: 万吨",Table.ALIGN_RIGHT);
		
		int rows= rt.body.getRows();
		for (int i=3;i<rows;i++){
			String mingc=rt.body.getCellValue(i, 1);
			String mingc2=mingc.substring(mingc.length()-2, mingc.length());
			if (mingc2.equals("小计")){
				for (int j=1;j<cols+1;j++){
//					rt.body.getCell(i, j).setBg_Color("#B6C6D8");
					rt.body.getCell(i, j).backColor=rt.body.getCell(i, j).getBg_Color();
				}
			}else{
				continue;
			}
		} 
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}

	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	
	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}


//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefenValue(null);
			this.getYuefenModels();
			this.setTreeid(null);
			
			visit.setString13(getYuefenValue().getValue());	//rbvalue
			visit.setDropDownBean4(null);		//计划口径
			visit.setProSelectionModel4(null); //计划口径
			visit.setDropDownBean5(null);		//报表类型
			visit.setProSelectionModel5(null); //报表类型
			visit.setDropDownBean6(null);		//机组状态
			visit.setProSelectionModel6(null); //机组状态
		}
		getToolBars();
		
	}

//	******************************************************************************
	//年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
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
	
//	月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel5() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean5((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean5()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean5(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel5(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel5(_value);
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

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

	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		
		tb1.addText(new ToolbarText("-"));

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
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("机组状态:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(80);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("报表类型:"));
		ComboBox tblx = new ComboBox();
		tblx.setTransform("BaoblxDropDown");
		tblx.setWidth(110);
		tb1.addField(tblx);
		tb1.addText(new ToolbarText("-"));
		
		String Strtmpfunction=
			 "var form = new Ext.FormPanel({ \n"+
			 "		labelAlign : 'right', \n"+
			 "		frame : true, \n"+
			 "		labelAlign : 'messageform', \n"+
			 "		labelWidth : 160, \n"+
			 "		items : [{ \n"+
			 "		layout : 'column',\n"+
			 "		items : [{ \n"+
			 "		layout:'form',\n"+
			 "		columnWidth:3,\n"+
			 "			items : [new Ext.form.ComboBox({ \n"+
			 "			fieldLabel:'请选择重点订货兑现量截止的月份', \n"+
			 "          id: 'yuefenname',\n"+
			 "			width:100, \n"+
			 "			selectOnFocus:true, \n"+
			 "			transform:'YUEFEN', \n"+
			 "			lazyRender:true, \n"+
			 "			triggerAction:'all', \n"+
			 "			typeAhead:true, \n"+
			 "			forceSelection:true\n"+
			 "			//editable:false \n"+
			 "			})] \n"+
			 "		}]\n"+
			 "	}] \n"+
			 "}); \n"+
			 "	win = new Ext.Window({ \n"+
			 "		layout : 'fit', \n"+
			 "		width : 300, \n"+
			 "		//height : 110, \n"+
			 "		closeAction : 'hide', \n"+
			 "		plain : true, \n"+
			 "		title : '条件', \n"+
			 "		items : [form],  \n"+
			 "		buttons : [{ \n"+
			 "			text : '确定', \n"+
			 "			handler : function() { \n"+
			 "				win.hide(); \n"+
			 "				document.getElementById('TEXT_RADIO_SELECT_VALUE').value = \n"+
			 "						Ext.getCmp(\"yuefenname\").value; \n"+
			 "				//document.getElementById('RefurbishButton').click(); \n"+
			 "			} \n"+
			 "		}, { \n"+
			 "			text : '取消', \n"+
			 "			handler : function() { \n"+
			 "				win.hide(); \n"+
			 "			} \n"+
			 "		}] \n"+
			 "	});\n";
		StringBuffer insz = new StringBuffer();
		insz.append("function(){ if(!win){");
		insz.append(Strtmpfunction);
		insz.append("} win.show(this);	\n");
		insz.append("}");
		ToolbarButton rbtn = new ToolbarButton(null,"条件设置",insz.toString());
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
//		tb1.addFill();
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.getElementById('RefurbishButton').click();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString13(rbvalue);
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
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
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
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}
	
//	 月份下拉框
	private static IPropertySelectionModel _YuefenModel;
	
	public IPropertySelectionModel getYuefenModel() {
	    if (_YuefenModel == null) {
	        getYuefenModels();
	    }
	    return _YuefenModel;
	}
	
	private IDropDownBean _YuefenValue;
	
	public IDropDownBean getYuefenValue() {
	    if (_YuefenValue == null) {
	        int _yuefen = DateUtil.getMonth(new Date());
	        if (_yuefen == 1) {
	            _yuefen = 12;
	        } else {
	            _yuefen = _yuefen - 1;
	        }
	        for (int i = 0; i < getYuefenModel().getOptionCount(); i++) {
	            Object obj = getYuefenModel().getOption(i);
	            if (_yuefen == ((IDropDownBean) obj).getId()) {
	                _YuefenValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefenValue;
	}
	
	public void setYuefenValue(IDropDownBean Value) {
    	if  (_YuefenValue!=Value){
    		_YuefenValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefenModels() {
        List listYuefen = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuefen.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefenModel = new IDropDownModel(listYuefen);
        return _YuefenModel;
    }

    public void setYuefenModel(IPropertySelectionModel _value) {
        _YuefenModel = _value;
    }
    
//	计划口径
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb \n";
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql, "全部"));
		return;
	}
	
//	机组状态
	public IDropDownBean getChartDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean) getChartDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setChartDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setChartDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getChartDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getChartDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getChartDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "全部"));
		list.add(new IDropDownBean(1, "现役机组"));
		list.add(new IDropDownBean(2, "新增机组"));
		
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
//	报表类型
	public IDropDownBean getBaoblxDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getBaoblxDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setBaoblxDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setBaoblxDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getBaoblxDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getBaoblxDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getBaoblxDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "电厂申请"));
		list.add(new IDropDownBean(1, "二级公司上报"));
		list.add(new IDropDownBean(2, "集团核定"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}

}