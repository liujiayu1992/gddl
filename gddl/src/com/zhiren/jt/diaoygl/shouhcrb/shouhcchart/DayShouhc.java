package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;
/* 
* 时间：2009-08-16
* 作者： ll
* 修改内容：1、“收煤”增加超级链接功能（计量管理---分厂分矿表）
* 		   
*/ 
/* 
* 时间：2009-08-18
* 作者： ll
* 修改内容：1、“收耗存日报首页中的可用天数不显示。
	点击进入某个分公司时页面显示可用天数
* 		   
*/ 
import org.apache.tapestry.html.BasePage;

import java.awt.Color;
import java.awt.GradientPaint;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

public class DayShouhc extends BasePage {
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	 //	页面判定方法
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private void setReturnValue(){
		String strDiancid=getTreeid();
		String strOldId="";
		int jib=this.getDiancTreeJib();
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select fuid from diancxxb d where d.id="+ strDiancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				strOldId = rs.getString("fuid");
				if (strOldId.equals("0")){
					return;
				}
			}
			setTreeid(strOldId);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
	}
	
//	开始日期v
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_BeginriqChange=true;
		}
	}
//	截止日期v
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_EndriqChange=true;
		}
	}
	
//	开始日期v
	private boolean _QisrqChange=false;
	public String getQisrq() {
		if (((Visit)getPage().getVisit()).getString12()==null){
			((Visit)getPage().getVisit()).setString12(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit)getPage().getVisit()).getString12();
	}
	
	public void setQisrq(String _value) {
		if (((Visit)getPage().getVisit()).getString12().equals(_value)) {
			_QisrqChange=false;
		} else {
			((Visit)getPage().getVisit()).setString12(_value);
			_QisrqChange=true;
		}
	}
//	截至日期v
	private boolean _JiezrqChange=false;
	public String getJiezrq() {
		if (((Visit)getPage().getVisit()).getString11()==null){
			((Visit)getPage().getVisit()).setString11(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getString11();
	}
	
	public void setJiezrq(String _value) {
		if (((Visit)getPage().getVisit()).getString11().equals(_value)) {
			_JiezrqChange=false;
		} else {
			((Visit)getPage().getVisit()).setString11(_value);
			_JiezrqChange=true;
		}
	}
	
	public boolean getRaw() {
		return true;
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}
	
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	//得到登陆人员所属电厂或分公司的名称
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
			e.printStackTrace();
		}
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		return getShouhc();
	}
	
	public String getShouhc(){
		Date dat=getBeginriqDate();//日期
		Date dat2=getEndriqDate();
		String strDate=DateUtil.FormatDate(dat);//日期字符
		String strDate2=DateUtil.FormatDate(dat2);
		String strGongs = "";
		String strGroupID = "";
		String strLink = "";
		
		int jib=this.getDiancTreeJib();
		
		if(jib==1){//选集团时刷新出所有的电厂
			//strGongs = "select distinct dc.fgsid as id,dc.xuh,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			//修改sql,否则用集团进的时候分公司的库存计算不正确 ---wzb
			strGongs = " select dc.id,dc.xuh,dc.mingc from diancxxb dc where dc.jib=2 and dc.ranlgs=-1";//取得集团下的所有分公司
			
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongs = "select distinct dc.id,dc.xuh,dc.mingc from vwdianc dc where (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+") ";//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongs = "select distinct dc.id,dc.xuh,dc.mingc from vwdianc dc where dc.id="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==-1){
			strGongs = "select distinct dc.id,dc.xuh,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}
		 
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		String sql_kc = "sum(nvl(dr.kuc,0)) as kuc";//库存
		String sql_kyts ="";//	收耗存日报首页中的可用天数不显示。点击进入某个分公司时页面显示可用天数
		
		if(jib!=1){
			 sql_kyts = ",decode(grouping(dc.mingc),1,'',sum(keyts_rb(dc.id,to_date('"+(strDate2)+"','yyyy-mm-dd')))) as keyts ";//可用天数
		}
		String sql_ts ="select zhi from xitxxb where mingc = '是否有汇总可用天数' and diancxxb_id=0";//判断系统信息表中“是否有汇总可用天数”。
		
		ResultSet rs1=cn.getResultSet(sql_ts);
		try {
			if(rs1.next()){
				long zhi=rs1.getLong("zhi");
				if (zhi==0){//不用汇总可用天数
					if(jib!=1){				
						sql_kyts = " ,round_new(sum(nvl(dr.kuc,0))/sum( keyts_rijhm_new(dc.id,dr.hj,to_date('"+(strDate2)+"','yyyy-mm-dd'),"+jib+")),1)  as keyts ";
					}else{
						sql_kyts ="";//	收耗存日报首页中的可用天数不显示。点击进入某个分公司时页面显示可用天数
					}
				}else{
					sql_kyts = ",sum(keyts_rb(dc.id,to_date('"+(strDate2)+"','yyyy-mm-dd'))) as keyts ";
					
					if (jib==1){
//						sql_kyts = "decode(grouping(dc.mingc),1,keyts_rb_new(dc.id,to_date('"+(strDate2)+"','yyyy-mm-dd'),1),sum(keyts_rb_new(dc.id,to_date('"+(strDate2)+"','yyyy-mm-dd'),2))) as keyts ";
						sql_kyts ="";//	收耗存日报首页中的可用天数不显示。点击进入某个分公司时页面显示可用天数
					}else if (jib==2){
						sql_kyts = ",decode(grouping(dc.mingc),1,keyts_rb_new("+this.getTreeid()+",to_date('"+(strDate2)+"','yyyy-mm-dd'),2),sum(keyts_rb_new(dc.id,to_date('"+(strDate2)+"','yyyy-mm-dd'),3))) as keyts ";
					}else if (jib==3){
						sql_kyts = ",decode(grouping(dc.mingc),1,keyts_rb_new("+this.getTreeid()+",to_date('"+(strDate2)+"','yyyy-mm-dd'),2),sum(keyts_rb_new(dc.id,to_date('"+(strDate2)+"','yyyy-mm-dd'),3))) as keyts ";
					}
				}
			}
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (jib==3){
			sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
		}else if (jib==2){
			sbsql.append(" select getLinkMingxTaiz(decode(grouping(dc.mingc),1,-1,max(dc.id)),'"+this.getTreeid()+"',decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
		}else{
			sbsql.append(" select getAlink('厂',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
			
			sql_kc=" getLinkMingxKc(decode(grouping(dc.mingc),1,-1,max(dc.id)),'"+this.getTreeid()+"',sum(nvl(dr.kuc,0))) as kuc";
		}
		try{

			sbsql.append("getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','Shoumfcfkbreport','&'||'diancxxb_id='||decode(grouping(dc.mingc),1,getDiancId_Fuid(max(dc.id)),max(dc.id))||'&'||'beginriq="+strDate2+"'||'&'||'endriq="+strDate2+"',sum(dr.gm)) as gm,");
			sbsql.append("getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','Shoumfcfkbreport','&'||'diancxxb_id='||decode(grouping(dc.mingc),1,getDiancId_Fuid(max(dc.id)),max(dc.id))||'&'||'beginriq="+strDate+"'||'&'||'endriq="+strDate2+"',sum(nvl(lj.gm,0))) as gmlj,");
			//			sbsql.append("    sum(nvl(dr.gm,0)) as gm,sum(nvl(lj.gm,0)) as gmlj, \n");
			sbsql.append("    sum(nvl(dr.hj,0)) as hy,sum(nvl(lj.hj,0)) as hylj, \n");
			sbsql.append("    sum(nvl(dr.tiaozl,0)) as tiaozl,"+sql_kc+""+sql_kyts+" \n");
			sbsql.append(" from (").append(strGongs).append(") dc,  \n");
			sbsql.append("      (select ").append(strGroupID).append(" as id,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl, \n");
			sbsql.append("            sum(fady) as fady,sum(gongry) as gongry,sum(kuc) as kuc,sum(fady+gongry+qity+yuns+cuns) as hj     \n");
			sbsql.append("         from shouhcrbb hc,vwdianc dc \n");
			sbsql.append("         where hc.diancxxb_id=dc.id and riq=to_date('").append(strDate2).append("','yyyy-mm-dd') group by ").append(strGroupID).append(") dr , \n");
			sbsql.append("      (select ").append(strGroupID).append(" as id,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl, \n");
			sbsql.append("            sum(fady) as fady,sum(gongry) as gongry,sum(fady+gongry+qity+yuns+cuns) as hj    \n");
			sbsql.append("         from shouhcrbb hc,vwdianc dc \n");
			sbsql.append("         where hc.diancxxb_id=dc.id and riq>=to_date('").append(strDate).append("','yyyy-mm-dd') \n");
			sbsql.append("               and riq<=to_date('").append(strDate2).append("','yyyy-mm-dd') \n");
			sbsql.append("         group by ").append(strGroupID).append(") lj \n");
			sbsql.append(" where dc.id=dr.id(+) \n");
			sbsql.append("       and dc.id=lj.id(+) \n");
			sbsql.append("    group by rollup(dc.mingc) order by grouping(dc.mingc), max(dc.xuh)\n");


			/*if(jib==1){
				String ArrHeader[][]=new String[3][7]; 
				ArrHeader[0]=new String[] {"日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况"};
				ArrHeader[1]=new String[] {"单位","收煤","收煤","总耗用","总耗用","调整","库存"};
				ArrHeader[2]=new String[] {"单位","当日","累计","当日","累计","调整","库存"};

				int ArrWidth[]=new int[] {180,80,80,80,80,80,100};
				ResultSet rs = cn.getResultSet(sbsql.toString());

				Report rt=new Report();
				rt.setCenter(false);
				rt.setBody(new Table(rs,3,0,1));
				rt.body.setHeaderData(ArrHeader);
				rt.body.setFontSize(12);
				rt.body.setBorder(2);
				rt.body.setUseCss(true);
				rt.body.setColHeaderClass("tab_colheader");
				rt.body.setRowHeaderClass("tab_rowheader");
				rt.body.setFirstDataRowClass("tab_data_line_one");
				rt.body.setSecondDataRowClass("tab_data_line_two");
				rt.body.setCellsClass("tab_cells");
				rt.body.setTableClass("tab_body");
				rt.body.setWidth(ArrWidth);
				rt.body.setColAlign(1, Table.ALIGN_CENTER);
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				return rt.getHtml();
			}else{*/
				String ArrHeader[][]=new String[3][8]; 
				ArrHeader[0]=new String[] {"日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况","日收耗存情况"};
				ArrHeader[1]=new String[] {"单位","收煤","收煤","总耗用","总耗用","调整","库存","可用天数"};
				ArrHeader[2]=new String[] {"单位","当日","累计","当日","累计","调整","库存","可用天数"};

				int ArrWidth[]=new int[] {180,80,80,80,80,80,100,70};
				ResultSet rs = cn.getResultSet(sbsql.toString());

				Report rt=new Report();
				rt.setCenter(false);
				int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
				rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
				rt.setBody(new Table(rs,3,0,1));
				rt.body.setHeaderData(ArrHeader);
				rt.body.setFontSize(12);
				rt.body.setBorder(2);
				rt.body.setUseCss(true);
				rt.body.setColHeaderClass("tab_colheader");
				rt.body.setRowHeaderClass("tab_rowheader");
				rt.body.setFirstDataRowClass("tab_data_line_one");
				rt.body.setSecondDataRowClass("tab_data_line_two");
				rt.body.setCellsClass("tab_cells");
				rt.body.setTableClass("tab_body");
				rt.body.setWidth(ArrWidth);
				rt.body.setColAlign(1, Table.ALIGN_CENTER);
				rt.body.setPageRows(rt.PAPER_ROWS);
//				增加长度的拉伸
				rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				return rt.getHtml();
//			}
		
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}finally{
			cn.Close();
		}
	}
	
	//比例图
	public String getChart3D(){
		Date dat=getBeginriqDate();//起始日期
		Date dat2=getEndriqDate();//截止日期
		String strDate=DateUtil.FormatDate(dat);//起始日期字符
		String strDate2=DateUtil.FormatDate(dat2);//截止日期字符
		int jib=this.getDiancTreeJib();
		
		String strGongs = "";
		String strGroupID = "";
		
		if(jib==1){//选集团时刷新出所有的电厂
			strGongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongs = "select distinct dc.id,dc.mingc from vwdianc dc where (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+") ";//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.id="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==-1){
			strGongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}
		
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" select max(dc.mingc) as danwmc,");
		sbsql.append("    sum(nvl(dr.gm,0)) as gm,sum(nvl(lj.gm,0)) as gmlj, \n");
		sbsql.append("    sum(nvl(dr.hy,0)) as hy,sum(nvl(lj.hy,0)) as hylj,sum(dr.gongry) as gongry,sum(dr.fady) as fady, \n");
		sbsql.append("    sum(nvl(dr.kuc,0)) as kuc,sum(keyts_rb(dc.id,to_date('").append(strDate2).append("','yyyy-mm-dd'))) as keyts \n");
		sbsql.append(" from (").append(strGongs).append(") dc,  \n");
		sbsql.append("      (select ").append(strGroupID).append(" as id,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl, \n");
		sbsql.append("            sum(fady) as fady,sum(gongry) as gongry,sum(kuc) as kuc    \n");
		sbsql.append("         from shouhcrbb hc,vwdianc dc\n");
		sbsql.append("         where  hc.diancxxb_id=dc.id\n" );
		sbsql.append(" 				and riq=to_date('").append(strDate2).append("','yyyy-mm-dd') ");
		sbsql.append(" 		  group by ").append(strGroupID).append(" ) dr , \n");
		sbsql.append("      (select ").append(strGroupID).append(" as id,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl, \n");
		sbsql.append("            sum(fady) as fady,sum(gongry) as gongry,sum(kuc) as kuc    \n");
		sbsql.append("         from shouhcrbb hc,vwdianc dc\n");
		sbsql.append("         where  hc.diancxxb_id=dc.id\n" );
		sbsql.append("         and riq>=to_date('").append(strDate).append("','yyyy-mm-dd') \n");
		sbsql.append(" 				and riq<=to_date('").append(strDate2).append("','yyyy-mm-dd') ");
		sbsql.append(" 			group by ").append(strGroupID).append(" ) lj  \n");
		sbsql.append(" where dc.id=dr.id(+) \n");
		sbsql.append("      and dc.id=lj.id(+) \n");
		sbsql.append(" group by dc.mingc \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", getDataField(), true);
		ct.pieLabGenerator=ct.piedatformat2;//显示百分比
		ct.showLegend=false;
		ct.chartBackgroundPaint=gp;
		ct.pieLabFormat=false;//不显示数据内容
		
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChart3D','show')\"");
		String charImg=ct.ChartPie3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChart3D");
		ct.showLegend=true;
		ct.pieLabFormat=true;//不显示数据内容
		ct.pieLabGenerator=ct.piedatformat2;//显示百分比
		
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString7(ct.ChartPie3D(getPage(),dataset, "", 600, 300));
		return charImg;
	}
	
	public String getChart3DBig(){
		return ((Visit) getPage().getVisit()).getString7();
	}
	
//	柱型图
	public String getChartBar(){
		Date dat=getBeginriqDate();//起始日期
		Date dat2=getEndriqDate();//截止日期
		String strDate1=DateUtil.FormatDate(dat);//起始日期字符
		String strDate2=DateUtil.FormatDate(dat2);//截止日期字符
		String strDate3=getQisrq();
		String strDate4=getJiezrq();
		int jib=this.getDiancTreeJib();
		String date1="";
		String date2="";
		String rq1="";
		String rq2="";
		String strGongs = "";
		String strGroupID = "";
		
		if(jib==1){//选集团时刷新出所有的电厂
			strGongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongs = "select distinct dc.id,dc.mingc from vwdianc dc where  (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.id="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==-1){
			strGongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		} 
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();

		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			date1=" and riq>=to_date('"+strDate1+"','yyyy-mm-dd') and riq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and riq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd') ,-12) and riq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-12)  \n";
			rq1="to_date('"+strDate2+"','yyyy-mm-dd')";
			rq2="add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12)";
		}else if(getRbvalue().equals("tiaoj2")){
			date1=" and riq>=to_date('"+strDate1+"','yyyy-mm-dd') and riq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and riq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and riq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1)  \n";
			rq1="to_date('"+strDate2+"','yyyy-mm-dd')";
			rq2="add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1)";
		}else{
			date1=" and riq>=to_date('"+strDate1+"','yyyy-mm-dd') and riq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and riq>=to_date('"+strDate3+"','yyyy-mm-dd') and riq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
			rq1="to_date('"+strDate2+"','yyyy-mm-dd')";
			rq2="to_date('"+strDate4+"','yyyy-mm-dd')";
		}
		
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'本期','本期') as fenx,");
		sbsql.append("    sum(nvl(dr.gm,0)) as gm, \n");
		sbsql.append("    sum(nvl(dr.hy,0)) as hy, sum(dr.gongry) as gongry,sum(dr.fady) as fady,\n");
		sbsql.append("    sum(nvl(decode(dr.riq,").append(rq1).append(",dr.kuc,0),0)) as kuc,sum(keyts_rb(dc.id,to_date('").append(strDate2).append("','yyyy-mm-dd'))) as keyts \n");
		sbsql.append(" from (").append(strGongs).append(") dc,  \n");
		sbsql.append("      (select riq,").append(strGroupID).append(" as id,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl, \n");
		sbsql.append("            sum(fady) as fady,sum(gongry) as gongry ,sum(kuc) as kuc  \n");
		sbsql.append("         from shouhcrbb hc,vwdianc dc\n");
		sbsql.append("         where  hc.diancxxb_id=dc.id\n" );
		sbsql.append(date1);
		sbsql.append(" 		  group by riq,").append(strGroupID).append(" ) dr  \n");
		sbsql.append(" where dc.id=dr.id(+) \n");
		sbsql.append(" group by dc.mingc \n");
		sbsql.append(" union all \n");
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'同期','同期') as fenx,");
		sbsql.append("    sum(nvl(dr.gm,0)) as gm, \n");
		sbsql.append("    sum(nvl(dr.hy,0)) as haoy,sum(dr.gongry) as gongry,sum(dr.fady) as fady,\n");
		sbsql.append("    sum(nvl(decode(dr.riq,").append(rq2).append(",dr.kuc,0),0)) as kuc,sum(keyts_rb(dc.id,to_date('").append(strDate2).append("','yyyy-mm-dd'))) as keyts \n");
		sbsql.append(" from (").append(strGongs).append(") dc,  \n");
		sbsql.append("      (select riq,").append(strGroupID).append(" as id,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl, \n");
		sbsql.append("            sum(fady) as fady,sum(gongry) as gongry,sum(kuc) as kuc   \n");
		sbsql.append("         from shouhcrbb hc,vwdianc dc\n");
		sbsql.append("         where  hc.diancxxb_id=dc.id\n" );
		sbsql.append(date2);
		sbsql.append(" 		  group by riq, ").append(strGroupID).append(" ) dr  \n");
		sbsql.append(" where dc.id=dr.id(+) \n");
		sbsql.append(" group by dc.mingc \n");
		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		CategoryDataset dataset = cd.getRsDataChart(rs, "danwmc", "fenx", getDataField());
		ct.intDigits=getDigts();				//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.chartBackgroundPaint=gp;
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = false;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartBar','show')\" ");
		String charImg=ct.ChartBar3D(getPage(),dataset, "", 200, 120);
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.showLegend = true;
		
		ct.setID("imgChartBar");
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString5(ct.ChartBar3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChartBarBig(){
		return ((Visit) getPage().getVisit()).getString5();
	}
	
//	曲线图
	public String getChartLine(){
		Date dat=getBeginriqDate();//起始日期
		Date dat2=getEndriqDate();//截止日期
		String strDate1=DateUtil.FormatDate(dat);//起始日期字符
		String strDate2=DateUtil.FormatDate(dat2);//截止日期字符
		String strDate3=getQisrq();
		String strDate4=getJiezrq();
		String strCondition="";
		String date1="";
		String date2="";
		String riq1="";
		String riq2="";
		String riqc="";
		String shijc="";
		int jib=this.getDiancTreeJib();
		
		if(jib==1){//选集团时刷新出所有的电厂
		
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}
		
		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			date1=" where hc.diancxxb_id=dc.id and riq<=to_date('"+strDate2+"','yyyy-mm-dd') and  riq>=to_date('"+strDate2+"','yyyy-mm-dd')-15 \n";
			date2=" where hc.diancxxb_id=dc.id and riq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-12) and riq>=add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-12)-15 \n";
			riq1="  (select to_date('"+strDate2+"','yyyy-mm-dd')-rownum+1 as riq  \n";
			riq2="  (select add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-12)-rownum+1 as riq  \n";
			shijc=" add_months(rq.riq,12) ";
			riqc="";
		}else if(getRbvalue().equals("tiaoj2")){
			date1=" where hc.diancxxb_id=dc.id and riq<=to_date('"+strDate2+"','yyyy-mm-dd') and  riq>=to_date('"+strDate2+"','yyyy-mm-dd')-15 \n";
			date2=" where hc.diancxxb_id=dc.id and riq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-1) and riq>=add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-1)-15 \n";
			riq1="  (select to_date('"+strDate2+"','yyyy-mm-dd')-rownum+1 as riq  \n";
			riq2="  (select add_months(to_date('"+strDate2+"','yyyy-mm-dd') ,-1)-rownum+1 as riq  \n";
			shijc=" rq.riq+c.riqc ";
			riqc=",(select distinct to_date('"+strDate2+"','yyyy-mm-dd')-add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1) as riqc from diancxxb )  c";
		}else{
			date1=" where hc.diancxxb_id=dc.id and riq<=to_date('"+strDate2+"','yyyy-mm-dd') and  riq>=to_date('"+strDate2+"','yyyy-mm-dd')-15 \n";
			date2=" where hc.diancxxb_id=dc.id and riq<=to_date('"+strDate4+"','yyyy-mm-dd')  and riq>=to_date('"+strDate3+"','yyyy-mm-dd')-15 \n";
			riq1="  (select to_date('"+strDate2+"','yyyy-mm-dd')-rownum+1 as riq  \n";
			riq2="  (select to_date('"+strDate4+"','yyyy-mm-dd')-rownum+1 as riq  \n";
			shijc=" rq.riq+c.riqc ";
			riqc=",(select distinct to_date('"+strDate2+"','yyyy-mm-dd')-to_date('"+strDate4+"','yyyy-mm-dd') as riqc from diancxxb )  c";
		}
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select  rq.riq,decode(1,1,'本期','本期') as fenx, gm,hy,tiaozl,fady,gongry,kuc \n");
		sbsql.append("       from (select riq,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl,  \n");
		sbsql.append("                    sum(fady) as fady,sum(gongry) as gongry ,sum(kuc) as kuc   \n");
		sbsql.append("                    from shouhcrbb hc,vwdianc dc \n");
		sbsql.append(date1);
		sbsql.append(strCondition).append(" group by riq) dr  ,  \n");
		sbsql.append(riq1);
		sbsql.append("             from diancxxb where rownum<=15) rq   \n");
		sbsql.append("       where rq.riq=dr.riq(+) \n");
		sbsql.append("     union all \n");
		sbsql.append("     select ").append(shijc).append(" as riq,decode(1,1,'同期','同期') as fenx, gm,hy,tiaozl,fady,gongry,kuc \n");
		sbsql.append("       from (select riq,sum(dangrgm) gm,sum(haoyqkdr) hy,sum(tiaozl) as tiaozl,  \n");
		sbsql.append("                    sum(fady) as fady,sum(gongry) as gongry ,sum(kuc) as kuc   \n");
		sbsql.append("                    from shouhcrbb hc,vwdianc dc \n");
		sbsql.append(date2);
		sbsql.append(strCondition).append("   group by riq) dr  ,  \n");
		sbsql.append(riq2);
		sbsql.append("             from diancxxb where rownum<=15) rq   \n");
		sbsql.append(riqc); 
		
		sbsql.append("       where rq.riq=dr.riq(+) \n");
		
		JDBCcon cn = new JDBCcon();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","riq",getDataField());//rs记录集构造生成图片需要的数据
		ct.intDigits=0;
		ct.lineDateFormatOverride="MM-dd";
		ct.setDateUnit(Chart.DATEUNIT_DAY, 1);
		ct.chartBackgroundPaint=gp;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartLine','show')\"");
		String charImg=ct.ChartTimeGraph(getPage(),data2, "", 200, 120);
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.showLegend = true;
		ct.setID("imgChartLine");
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString6(ct.ChartTimeGraph(getPage(),data2, "", 600, 300));
		
		cn.Close();
		return charImg;
	}
	
	public String getChartLineBig(){
		return ((Visit) getPage().getVisit()).getString6();
	}
	
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ReturnClick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			return;
		}
		if (_ReturnClick){
			_ReturnClick=false;
			setReturnValue();
			return;
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString3("-1");
			
			visit.setString12(DateUtil.FormatDate(new Date()));
			visit.setString11(DateUtil.FormatDate(new Date()));
			
			visit.setDate1(null);
			visit.setDate2(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			this.setTreeid(null);
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);保存传递的非默认纸张的样式
				
			this.setBeginriqDate(visit.getMorkssj());
			this.setEndriqDate(visit.getMorjssj());
		}
		getToolBars();
		isBegin=true;
	}	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
	
	public void getToolBars() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String danx1="";
        String danx2="";
        String danx3="";
        if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			danx1="checked:true ,   \n";
		}else if(getRbvalue().equals("tiaoj2")){
			danx2="checked:true ,   \n";
		}else{
			danx3="checked:true ,   \n";
		}

        String Strtmpfunction="var form = new Ext.FormPanel({ "
	        + "labelAlign:'right', \n"
	        + "frame:true,\n"
	        + "items: [ \n"
		    + "{ \n"
		    + "  layout:'column',\n"
		    + "  items:[{ \n"
		    + "    layout:'form',\n"
		    + "    columnWidth:.5,"
		    + "       items:[{ \n"
		    + "    	    xtype:'textfield', \n"
		    + "    		fieldLabel:'条件选择',\n"
		    + "    		width:0 \n"
		    + "    		},	\n"
			+ " 	{ \n"
			+ "         xtype:'radio',\n"
			+ "			boxLabel:'本同期对比', \n"
		    + "     	Value:'tiaoj1', \n"
		    + "         labelSeparator:'',\n"
		    +	danx1
			+ "			name:'tiaoj',\n"
			+ "			listeners:{ \n"
			+ "				'check':function(r,c){ \n"
			+ "                if(r.checked){"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}\n"
			+ "         }"
			+ "		} \n"
			+ "		},\n"
			+ "		{  \n"   
			+ "         xtype:'radio',\n"
			+ "			boxLabel:'环比',\n"
			+ "			Value:'tiaoj2', \n"
			+ "         labelSeparator:'',\n"
			+	danx2
			+ "			name:'tiaoj',\n"
			+ "			listeners:{ \n"
			+ "				'check':function(r,c){ \n"
			+ "					if(r.checked){"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}\n"
			+ "           }"
			+ "			} \n"
			+ "		},		\n"	
			+ "		{  \n"
			+ "         xtype:'radio',\n"
			+ "			boxLabel:'时间对比------------>请在右侧输入时间',\n"
			+ "			Value:'tiaoj3', \n"
			+ "         labelSeparator:'',\n"
			+	danx3
			+ "			name:'tiaoj',\n"
			+ "			listeners:{ \n"
			+ "				'check':function(r,c){ \n"
			+ "					if(r.checked){"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}\n"
			+ "            }"
			+ "			} \n"
			+ "		}]},		\n"	
			+ "		{ \n"
			+ "   		layout:'form',\n"
			+ "   		columnWidth:.5,\n"
			+ "   		items:[{},{},{},{},{},\n"
			+ "   	{ \n"
			+ "			xtype:'datefield', \n"   
			+ "			fieldLabel:'起始日期', \n"   
			+ "			name:'qisrq', \n" 
			+ "    	 	listeners:{change:function(own,newValue,oldValue) {document.getElementById('QISRQ').value = newValue.dateFormat('Y-m-d');}},\n"
			+ "     	value:document.getElementById('QISRQ').value \n"
			+ "		}, \n"   
			+ "		{ \n"   
			+ "			xtype:'datefield', \n"   
			+ "			fieldLabel:'截止日期', \n"   
			+ "			name:'jiezrq', \n"
			+ "     	value:'', \n"
			+ "     	listeners:{change:function(own,newValue,oldValue) {document.getElementById('JIEZRQ').value = newValue.dateFormat('Y-m-d');}},\n"
			+ "     	value:document.getElementById('JIEZRQ').value \n"
			+ "			}] \n"
			+ "			}] \n"
			+ "		}] \n"
			+ " });\n"
			+ " win = new Ext.Window({\n"
			+ " layout:'fit',\n"
			+ " width:500,\n"
			+ " height:300,\n"
			+ " closeAction:'hide',\n"
			+ " plain: true,\n"
			+ " title:'条件',\n"
			+ " items: [form],\n"
			+ " buttons: [{\n"
			+ "   text:'确定',\n"
			+ "   handler:function(){  \n"
			+ "  	win.hide();\n"
			+ "		document.getElementById('TEXT_RADIO_SELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
			+ " 	document.getElementById('RefurbishButton').click(); \n"
			+ "  	}   \n"
			+ "},{\n"
			+ "   text: '取消',\n"
			+ "   handler: function(){\n"
			+ "     win.hide();\n"
			+ "   }\n"
			+ "}]\n"
           
			+ " });";
	    
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","");// 与html页中的id绑定,并自动刷新
		df.setWidth(60);
 		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		 
		tb1.addText(new ToolbarText("到:"));
		DateField df1 = new DateField();
		df1.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		df1.Binding("endriqDateSelect","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(60);
 		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
	
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setId("diancTree_text");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(120);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		tb1.addText(new ToolbarText("图表数据:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(80);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tbok = new ToolbarButton(null,"确定","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		ToolbarButton tbtj=new ToolbarButton(null,"条件设置","function(){ if(!win){ "+Strtmpfunction+"}"
				+ " win.show(this);	\n" 
				+ "}"); 
		tbtj.setIcon(SysConstant.Btn_Icon_Search);		
		tb1.addItem(tbok);
		tb1.addItem(tb);
		tb1.addItem(tbtj);
		String diancxxbid=this.getTreeid();
		long strDiancid=visit.getDiancxxb_id();
		String Diancid=""+strDiancid;
		if(!diancxxbid.equals(Diancid)){
		ToolbarButton tbfh = new ToolbarButton(null,"返回上级","function(){document.getElementById('ReturnButton').click();}");
		tbfh.setIcon(SysConstant.Btn_Icon_Return);
		tb1.addItem(tbfh);
		}
		setToolbar(tb1);
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
		String sql="";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
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

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		Visit visit =((Visit) getPage().getVisit());
		visit.setString2(treeid);
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
	
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
//	类型
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
		list.add(new IDropDownBean(1, "库存"));
		list.add(new IDropDownBean(2, "收入"));
		list.add(new IDropDownBean(3, "耗用"));
		list.add(new IDropDownBean(4, "发电用"));
		list.add(new IDropDownBean(5, "供热用"));
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	private String getDataField(){
		String strDataField=getChartDropDownValue().getValue();
		if (strDataField.equals("库存")){
			return "kuc";
		}else if(strDataField.equals("收入")){
			return "gm";
		}else if(strDataField.equals("耗用")){
			return "hy";
		}else if(strDataField.equals("发电用")){
			return "fady";
		}else if(strDataField.equals("供热用")){
			return "gongry";
		}
		return "kuc";
	}
	
	private int getDigts(){
		return 0;
	}
}