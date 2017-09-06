//2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   最后一层查询显示明细数据
//         去掉小图比例图的比例数据显示
/*
 * 作者:tzf
 * 时间:2010-01-27
 * 修改内容:根据龙江公司要求，盈亏和运损相互计算，否则报表账面不平，用参数控制。
 */
package com.zhiren.jt.zdt.yansgl.rucmslys;

import org.apache.tapestry.html.BasePage;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Cell;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartBean;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

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
/*
 * 作者：songy
 * 时间：2011-7-20
 * 描述：增加从煤矿级别开始按口径分组
 */
/*
 * 作者：夏峥
 * 时间：2011-08-26
 * 描述：修正getLaimlField()方法中对来煤数量的取值方式。
 */
public class Rucmslys extends BasePage {
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
	
	private String getFilerCondtion(int jib){
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and fh.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and fh.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}

		if(jib==1){//选集团时刷新出所有的电厂
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strCondition =strCondition+ " ";
		}else if (jib==3){//选电厂只刷新出该电厂
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}
		return strCondition;
	}
	
	private void setReturnValue(){
		String strDiancid=getTreeid();
		Visit visit=((Visit) getPage().getVisit());
		long diancxxb_id=visit.getDiancxxb_id();
		String strleix=getLeixDropDownValue().getValue();
		
		String strOldId="";
		if (String.valueOf(diancxxb_id).equals(strDiancid)){
			if (strleix.equals("分矿")){
				if (!"-1".equals(visit.getString3())){
					visit.setString3("-1");
				}
			}
			return;
		}
		
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
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)), 0, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_BeginriqChange=true;
		}
	}
	
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
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
	
	public void setGongysId(String _value){
		 ((Visit) getPage().getVisit()).setString3(_value);
	}
	
	public String getGongysId(){
		Visit visit = (Visit) getPage().getVisit();
		
		return visit.getString3();
	}
	
	private String REPORT_ONEGRAPH_RUCMZLYS="rucmslys";
	private String mstrReportName="";
	 
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
		}finally{
			cn.Close();
		}
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		return getRucmslys();
	} 
	
	public String getRucmslys(){
		Date dat1=getBeginriqDate();//日期
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//日期字符
		Date dat2=getEndriqDate();//日期
		String strDate2=DateUtil.FormatDate(dat2);//日期字符
		
		Report rt=new Report();
		
		int jib=this.getDiancTreeJib();
		
		String strgongs = "";
		String strGroupID = "";
		String Strxuh= "";
		String where = "";
		String strQLeix="厂";
		String strCondition=getFilerCondtion(jib);;
		
		String Strkj="";
		String Strkjb="";
		String grouping="";
		String Strand="";
		String Strkjmc="";
		String Strgp="";
		String Strgp1="";
		
//		if(jib==1){//选集团时刷新出所有的电厂
//			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//取得集团下的所有分公司
//			strGroupID = "dc.fgsid";
//		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
//			strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//取得分公司下的所有电厂
//			strGroupID = "dc.id";
//		}else if (jib==3){
//			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
//			strGroupID = "dc.id";
//		}else if (jib==-1){
//			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
//			strGroupID = "dc.id";
//		}
//		
//		if ((strleix.equals("分厂") && jib==3) || (strleix.equals("分矿") && "-1".equals(getGongysId()))){
//			strgongs="select distinct y.dqmc as mingc,y.dqid as id,dc.xuh from\n" +
//			"        fahb fh,vwgongys y,vwdianc dc\n" + 
//			"        where fh.gongysb_id=y.id and dc.id=fh.diancxxb_id \n" + strCondition+
//			"        and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
//			"        and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
//			strGroupID = " y.dqid ";
//			strQLeix="矿";
//		}
		

		if(jib==1){//选集团时刷新出所有的电厂
//			strgongs = "select distinct dc.id,dc.fgsid,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//取得集团下的所有分公司
//			strGroupID = "sj.diancxxb_id";
			strgongs="dc.fgsmc";
			strGroupID="dc.fgsid";
			Strxuh="dc.fgsxh";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
//			strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//取得分公司下的所有电厂
//			strGroupID = "sj.diancxxb_id";
			strgongs="dc.mingc";
			strGroupID="dc.id";
			Strxuh="dc.xuh";
			
		}else if (jib==3){
//			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
//			strGroupID = "sj.diancxxb_id";
			strgongs="dc.mingc";
			strGroupID="dc.id";
			Strxuh="dc.xuh";
			if (strleix.equals("分厂")){
				Strkj=",fh.jihkjb_id";
				Strkjb=" ,jihkjb kj";
				grouping=" ,grouping(kj.mingc),max(kj.xuh) ";
				Strand="  AND  sj.jihkjb_id=kj.id";
	            Strkjmc="  kj.mingc kjmc,";
	            Strgp = "kj.mingc";
	            Strgp1 = ",kj.mingc";	
			}
			
		}else if (jib==-1){
//			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
//			strGroupID = "sj.diancxxb_id";
			strgongs="dc.mingc";
			strGroupID="dc.id";
			Strxuh="dc.xuh";
		}
		
		if ((strleix.equals("分厂") && jib==3) || (strleix.equals("分矿") && "-1".equals(getGongysId()))){
//			strgongs="select distinct y.dqmc as mingc,y.id,y.dqid,y.xuh from\n" +
//			"        fahb fh,vwgongys y,vwdianc dc\n" + 
//			"        where fh.gongysb_id=y.id and dc.id=fh.diancxxb_id \n" + strCondition+
//			"        and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
//			"        and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
			strgongs="y.dqmc";
			strGroupID="y.dqid";
			Strxuh="y.xuh";
			strQLeix="矿";
			//where=" and dc.id="+this.getTreeid()+"";
		}
		
		JDBCcon cn = new JDBCcon();
		
		String xhjs_str=" select * from xitxxb where mingc='数量报表盈亏运损相互计算' and zhi='是' and leib='数量' and zhuangt=1 ";//盈亏运损是否通过相互计算得到
		ResultSetList rsl_ys=cn.getResultSetList(xhjs_str);
		
		String yuns_js=" sum(round_new(fh.yuns,"+"0"+")) as yuns,\n";//火车的 或者 所有运输方式的 运损 计算表达式
		
	
		if(rsl_ys.next()){
			
			 yuns_js=" sum(round_new(fh.yingk,"+"0"+")) + sum(round_new(fh.biaoz,"+"0"+")) " +
			"- sum(round_new(fh.jingz,"+"0"+")) as yuns,\n ";//火车的 或者 所有运输方式的 运损 计算表达式
		}
		
		rsl_ys.close();
		StringBuffer sbsql = new StringBuffer();

//		if ((strleix.equals("分厂") && jib==3)){
//			sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
//		}else if ((strleix.equals("分矿") && jib==2 &&  !"-1".equals(getGongysId()))){
//			sbsql.append(" select getLinkMingxTaiz(decode(grouping(dc.mingc),1,-1,max(dc.id)) ,'"+getGongysId()+"',decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
//		}else{
//			sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
//		}
		
		if ((strleix.equals("分厂") && jib==3)){
			sbsql.append(" select  decode(grouping("+strgongs+"),1,'总计',"+strgongs+") as danwmc,  \n" );
			sbsql.append("   getLinkMingxTaiz('"+this.getTreeid()+",'|| max(kj.id) ,decode(grouping("+strgongs+"),1,-1,max("+strGroupID+")) ,"+Strgp+") as kjmc, \n" );
		}else if ((strleix.equals("分矿") && jib==2 &&  !"-1".equals(getGongysId()))){
			sbsql.append(" select getLinkMingxTaiz(decode(grouping("+strgongs+"),1,-1,max("+strGroupID+")) ,'"+getGongysId()+"',decode(grouping("+strgongs+"),1,'总计',"+strgongs+")) as danwmc,\n");
		}else{
			sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping("+strgongs+"),1,-1,max("+strGroupID+")),decode(grouping("+strgongs+"),1,'总计',"+strgongs+")) as danwmc,\n");
		}
		
		//*************************自定义的地方********************************//
		//sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,  \n");
		//sbsql.append("       decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,--sum(round_new(sj.jingz)) as jingz,  \n");
		sbsql.append(" sum(round_new(sj.ches,0)) as ches," );
		sbsql.append("sum(round_new(sj.biaoz,0)) as biaoz," );
		sbsql.append("sum(round_new(sj.laimsl,0)) as laimsl,sum(round_new(sj.yuns,0)) as yuns," );
		sbsql.append("sum(round_new(sj.jingz,0)) as jingz,\n");
		sbsql.append(" sum(round_new(sj.yingd,0)) as yingd,\n");
		sbsql.append(" sum(round_new(sj.kuid,0)) as kuid, sum(round_new(sj.koud,0)) as koud \n");
		sbsql.append(" from \n");
		sbsql.append("       (select fh.diancxxb_id,fh.gongysb_id "+Strkj+" ,  sum(fh.ches) as ches,"+getLaimlField()+" as laimsl,round_new(sum(fh.biaoz),0) as biaoz,round_new(sum(fh.jingz),0) as jingz,  \n");
		sbsql.append(yuns_js+"              round_new(sum(fh.yingd),0) as yingd, \n");
		sbsql.append("              round_new(sum(fh.yingd-fh.yingk),0) as kuid, round_new(sum(fh.koud),0) as koud \n");
		sbsql.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		sbsql.append("       where fh.gongysb_id=y.id  \n");
		sbsql.append("         and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')  \n");
		sbsql.append("         and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n").append(strCondition);
		sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		sbsql.append("         and fh.gongysb_id=y.id \n");
		sbsql.append("         group by fh.diancxxb_id,fh.gongysb_id,fh.lieid "+Strkj+" ) sj,vwdianc dc,vwgongys y "+Strkjb+" \n");
		sbsql.append(" where  sj.diancxxb_id=dc.id and sj.gongysb_id=y.id  \n"+Strand );
		sbsql.append(" group by rollup("+strgongs+Strgp1+")  \n");
		sbsql.append(" order by grouping("+strgongs+"),max("+Strxuh+"),"+strgongs+"   \n" +grouping);
		
		if ((strleix.equals("分厂") && jib==3)){
			String ArrHeader[][] = new String[3][10];
			String jihkjMingc=getJihkjDropDownValue().getValue();
			String yunsfsMingc=getYunsfsDropDownValue().getValue();
			ArrHeader[0] = new String[] { "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>","<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>","<font size=3>入厂煤数量情况</font>" };
			ArrHeader[1] = new String[] { "日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc};
			ArrHeader[2] = new String[] { "供货单位","计划口径","车数", "票重<br>(吨)", "实收量<br>(吨)", "运损<br>(吨)","净重<br>(吨)", "盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
			int ArrWidth[] = new int[] { 150,75, 75, 80, 80, 80, 70, 70, 70, 70 };
			ResultSet rs = cn.getResultSet(sbsql.toString());

			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setCenter(false);
			rt.setBody(new Table(rs,3,0,1));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setFontSize(12);

			rt.body.setUseCss(true);
//			rt.body.setColHeaderClass("tab_colheader");
//			rt.body.setRowHeaderClass("tab_rowheader");
//			rt.body.setFirstDataRowClass("tab_data_line_one");
//			rt.body.setSecondDataRowClass("tab_data_line_two");
//			rt.body.setCellsClass("tab_cells");
//			rt.body.setTableClass("tab_body");

			rt.body.setBorder(2);

			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.ShowZero = true;

			cn.Close();
			return rt.getHtml();

		}

		else {
			String ArrHeader[][] = new String[3][9];
			String jihkjMingc=getJihkjDropDownValue().getValue();
			String yunsfsMingc=getYunsfsDropDownValue().getValue();
			ArrHeader[0] = new String[] { "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>","<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>", "<font size=3>入厂煤数量情况</font>" };
			ArrHeader[1] = new String[] { "日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc,
					"日期:"+strDate1+"至"+strDate2+"&nbsp;类型:"+strleix+ "&nbsp;计划口径:"+jihkjMingc+"&nbsp;运输方式:"+yunsfsMingc};
			ArrHeader[2] = new String[] { "供货单位","车数", "票重<br>(吨)", "实收量<br>(吨)", "运损<br>(吨)","净重<br>(吨)", "盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
			int ArrWidth[] = new int[] { 150, 75, 80, 80, 80, 70, 70, 70, 70 };


			ResultSet rs = cn.getResultSet(sbsql.toString());

			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setCenter(false);
			rt.setBody(new Table(rs,3,0,1));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setFontSize(12);

			rt.body.setUseCss(true);
//			rt.body.setColHeaderClass("tab_colheader");
//			rt.body.setRowHeaderClass("tab_rowheader");
//			rt.body.setFirstDataRowClass("tab_data_line_one");
//			rt.body.setSecondDataRowClass("tab_data_line_two");
//			rt.body.setCellsClass("tab_cells");
//			rt.body.setTableClass("tab_body");

			rt.body.setBorder(2);

			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.ShowZero = true;

			cn.Close();
			return rt.getHtml();

		}
	}
	private String getLaimlField(){
		JDBCcon con = new JDBCcon();
		String laiml = SysConstant.LaimField;
		laiml="round_new(sum(fh.laimsl),0)";
		ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = '使用集团' and zhuangt = 1 and zhi = '中国大唐'");
		if(rs.next()){
			laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
		}
		rs.close();
		con.Close();
		return laiml;
	}
	
	//比例图
	public String getChart3D(){
		Date dat1=getBeginriqDate();//日期
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//日期字符
		
		Date dat2=getEndriqDate();//日期
		String strDate2=DateUtil.FormatDate(dat2);//日期字符
		int jib=this.getDiancTreeJib();
		
		String strgongs = "";
		String strGroupID = "";
		String strCondition=getFilerCondtion(jib);
		
		if(jib==1){//选集团时刷新出所有的电厂
			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){
			strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}else if (jib==-1){
			strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}
		
		if ((strleix.equals("分厂") && jib==3) || (strleix.equals("分矿") && "-1".equals(getGongysId()))){
			strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
			"        fahb fh,vwgongys y,vwdianc dc\n" + 
			"        where fh.gongysb_id=y.id and dc.id=fh.diancxxb_id \n" + strCondition+
			"        and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
			"        and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
			strGroupID = " y.dqid ";
		}
		
		String strDataField=getDataField();
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		 sbsql.append(" select dc.mingc as danwmc,  \n");
		 sbsql.append(" sum(round_new(sj.shul,0)) as shul \n");
		 sbsql.append(" from ( "+strgongs+" ) dc, \n");
		 sbsql.append("       (select "+strGroupID+" as id, "+strDataField+" \n");
		 sbsql.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		 sbsql.append("       where fh.gongysb_id=y.id  \n");
		 sbsql.append("         and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')  \n");
		 sbsql.append("         and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n").append(strCondition);
		 sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		 sbsql.append("         and fh.gongysb_id=y.id \n");
		 sbsql.append("         group by "+strGroupID+",fh.lieid) sj  \n");
		 sbsql.append(" where sj.id(+)=dc.id  \n");
		 sbsql.append(" group by dc.mingc  \n");
		 sbsql.append(" order by dc.mingc  \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", "shul", true);
		ct.pieLabGenerator=ct.piedatformat2;//显示百分比
		ct.showLegend=false;
		ct.pieLabFormat=false;
		ct.chartBackgroundPaint=gp;
		
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChart3D','show')\"");
		String charImg=ct.ChartPie3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChart3D");
		ct.showLegend=true;
		ct.pieLabFormat=true;//不显示数据内容
		ct.pieLabGenerator=ct.piedatformat2;//显示百分比
		ct.setImgEvents("");
		ct.setDisplayPicture(false);
		((Visit) getPage().getVisit()).setString7(ct.ChartPie3D(getPage(),dataset, "", 600, 300));
		cn.Close();
		return charImg;
	}
	
	public String getChart3DBig(){
		return ((Visit) getPage().getVisit()).getString7();
	}
	
//	柱型图
	public String getChartBar(){
		String strDataField=getDataField();
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(getBeginriqDate());//日期字符
		String strDate2=DateUtil.FormatDate(getEndriqDate());//日期字符
		String strDate3=getQisrq();
		String strDate4=getJiezrq();
		int jib=this.getDiancTreeJib();
		String date1="";
		String date2="";
		String strgongs = "";
		String strgongsq="";
		String strGroupID = "";
		String strCondition=getFilerCondtion(jib);
		
		if(jib==1){//选集团时刷新出所有的电厂
			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){
			strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}else if (jib==-1){
			strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}

		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){//本同期对比
			
			date1=" and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and fh.daohrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12) and fh.daohrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12)  \n";
		
		}else if(getRbvalue().equals("tiaoj2")){//环比
			
			date1=" and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and fh.daohrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and fh.daohrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1)  \n";
		
		}else{//时间段对比
			
			date1=" and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and fh.daohrq>=to_date('"+strDate3+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
		
		}
		
		strgongsq=strgongs;
		if ((strleix.equals("分厂") && jib==3) || (strleix.equals("分矿") && "-1".equals(getGongysId()))){
			strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
			"        fahb fh,vwgongys y,vwdianc dc\n" + 
			"        where fh.gongysb_id=y.id and dc.id=fh.diancxxb_id \n" + strCondition+date1;
			
			strgongsq="select distinct y.dqmc as mingc,y.dqid as id from\n" +
			"        fahb fh,vwgongys y,vwdianc dc\n" + 
			"        where fh.gongysb_id=y.id and dc.id=fh.diancxxb_id \n" + strCondition+date2;
			
			strGroupID = " y.dqid ";
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" select danwmc,fenx,sum(shul) as shul from (\n");
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'本期','本期') as fenx,nvl(shul,0) as shul  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,");
		sbsql.append(strDataField);
		sbsql.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		sbsql.append("       where fh.gongysb_id=y.id  \n");
		sbsql.append(date1).append(strCondition);
		sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		sbsql.append("         and fh.gongysb_id=y.id \n");
		sbsql.append("         group by "+strGroupID+",fh.lieid) sj  \n");
		sbsql.append(" where sj.id(+)=dc.id  \n");
		sbsql.append(" union all  \n");
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'同期','同期') as fenx,nvl(shul,0) shul  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,");
		sbsql.append(strDataField);
		sbsql.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		sbsql.append("       where fh.gongysb_id=y.id  \n");
		sbsql.append(date2).append(strCondition);
		sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		sbsql.append("         and fh.gongysb_id=y.id \n");
		sbsql.append("         group by "+strGroupID+",fh.lieid) sj  \n");
		sbsql.append(" where sj.id(+)=dc.id ) \n");
		sbsql.append("group by danwmc,fenx order by danwmc\n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		CategoryDataset dataset = cd.getRsDataChart(rs, "danwmc", "fenx", "shul");
		ct.intDigits=getDigts();				//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = false;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.chartBackgroundPaint=gp;
		//return ct.ChartBar3D(getPage(), dataset, "", 200, 120);
		
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartBar','show')\" ");
		String charImg=ct.ChartBar3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChartBar");
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.showLegend = true;
		ct.setImgEvents("");
		ct.setDisplayPicture(false);
		((Visit) getPage().getVisit()).setString5(ct.ChartBar3D(getPage(),dataset, "", 600, 300));
		cn.Close();
		return charImg;
	}
	
	public String getChartBarBig(){
		return ((Visit) getPage().getVisit()).getString5();
	}
	
	
//	曲线图
	public String getChartLine(){
		String strDateBegin=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(getEndriqDate()), -1, DateUtil.AddType_intYear));//日期字符
		String strDateEnd=DateUtil.FormatDate(getEndriqDate());//日期字符
		
		int jib=this.getDiancTreeJib();
		String strCondition=getFilerCondtion(jib);
		String strDataField=getDataField();
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select to_date(bq.yuef||'-01','yyyy-mm-dd') as yuef,bq.fenx,nvl(shul,0) as shul  \n");
		sbsql.append("  from (select to_char(daohrq,'yyyy-mm') as yuef,\n");
		sbsql.append(strDataField);
		sbsql.append("               from fahb fh,vwdianc dc,vwgongys y  \n");
		sbsql.append("        where fh.gongysb_id=y.id   \n");
		sbsql.append("              and fh.daohrq>=to_date('"+strDateBegin+"','yyyy-mm-dd')   \n");
		sbsql.append("              and fh.daohrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("              and fh.diancxxb_id=dc.id     \n").append(strCondition);
		sbsql.append("              and fh.gongysb_id=y.id  \n");
		sbsql.append("         group by to_char(daohrq,'yyyy-mm')) bqsj, \n");
		sbsql.append("         (select yuef,rownum as yf,nvl('本期','') as fenx from vwnianyue  \n");
		sbsql.append("          where to_date(yuef,'yyyy-mm') <= to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
		sbsql.append(" and rownum<=12 ) bq ");
		sbsql.append(" where bq.yuef=bqsj.yuef(+) \n");
		sbsql.append(" union all \n");
		sbsql.append(" select  add_months(to_date(to_char(bq.yuef)||'-01','yyyy-mm-dd'),12) as yuef,bq.fenx,nvl(shul,0) as shul  \n");
		sbsql.append("  from (select to_char(daohrq,'yyyy-mm') as yuef, \n");
		sbsql.append(strDataField);
		sbsql.append("               from fahb fh,vwdianc dc,vwgongys y  \n");
		sbsql.append("        where fh.gongysb_id=y.id   \n");
		sbsql.append("              and fh.daohrq>=add_months(to_date('"+strDateBegin+"','yyyy-mm-dd'),-12) \n");
		sbsql.append("              and fh.daohrq<=add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12) \n");
		sbsql.append("              and fh.diancxxb_id=dc.id     \n").append(strCondition);
		sbsql.append("              and fh.gongysb_id=y.id  \n");
		sbsql.append("         group by to_char(daohrq,'yyyy-mm')) bqsj  , \n");
		sbsql.append("         (select yuef,rownum as yf,nvl('同期','') as fenx  from vwnianyue \n");
		sbsql.append("           where to_date(yuef,'yyyy-mm') <= add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12) and rownum<=12) bq \n");
		sbsql.append("  where bq.yuef=bqsj.yuef(+) \n");
		sbsql.append(" order by yuef ,fenx \n");
		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","yuef", "shul");//rs记录集构造生成图片需要的数据
		ct.intDigits=0;
		ct.lineDateFormatOverride="MM";
		ct.setDateUnit(Chart.DATEUNIT_MONTH, 1);
		ct.chartBackgroundPaint=gp;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartLine','show')\"");
		String charImg=ct.ChartTimeGraph(getPage(),data2, "", 200, 120);
		ct.setID("imgChartLine");
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.showLegend = true;
		ct.setImgEvents("");
		ct.setDisplayPicture(false);
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
			visit.setString13("");
			
			visit.setDate1(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)), 0, DateUtil.AddType_intDay));
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			visit.setDropDownBean6(null);
			
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
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
//			getDayValue();
		}
		
		getToolBars();
		isBegin=true;
	}	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		

		Visit visit = (Visit) getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		String danx1="";
        String danx2="";
        String danx3="";
        if(getRbvalue().equals("tiaoj1")||getRbvalue().equals("")){
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
		tb1.addText(new ToolbarText("至:"));
		DateField edf = new DateField();
		edf.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		edf.Binding("EndriqDateSelect","");// 与html页中的id绑定,并自动刷新
		edf.setWidth(60);
		tb1.addField(edf);
		tb1.addText(new ToolbarText("-"));
		
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("类型:"));
		ComboBox tblx = new ComboBox();
		tblx.setTransform("LeixDropDown");
		tblx.setWidth(60);
		tb1.addField(tblx);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox tbysfs = new ComboBox();
		tbysfs.setTransform("YunsfsDropDown");
		tbysfs.setWidth(60);
		tb1.addField(tbysfs);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("图表数据:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(80);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("-"));
	

		ToolbarButton tbok = new ToolbarButton(null,"确定","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tbtj=new ToolbarButton(null,"条件设置","function(){ if(!win){ "+Strtmpfunction+"}"
				+ " win.show(this);	\n" 
				+ "}"); 
		tbtj.setIcon(SysConstant.Btn_Icon_Search);
		
		tb1.addItem(tbok);
		tb1.addItem(tbtj);
		
		String strDiancid=getTreeid();
		long diancxxb_id=visit.getDiancxxb_id();
		String diancxxbid=""+diancxxb_id;
//		System.out.println("树="+strDiancid+"，电厂="+diancxxbid);
		if(getLeixDropDownValue().getValue().equals("分厂")){
			if(!strDiancid.equals(diancxxbid)){
				ToolbarButton tbfh = new ToolbarButton(null,"返回上级","function(){document.getElementById('ReturnButton').click();}");
				tbfh.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(tbfh);
			}
		}else{
			if(!"-1".equals(getGongysId())){
				ToolbarButton tbfh = new ToolbarButton(null,"返回上级","function(){document.getElementById('ReturnButton').click();}");
				tbfh.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(tbfh);
			}
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
		sql = "select d.id,d.mingc from diancxxb d order by d.xuh  ";
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
	

//	类型
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getLeixDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分厂"));
		list.add(new IDropDownBean(2, "分矿"));
		//list.add(new IDropDownBean(3, "棋盘表"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
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
		String sql = "select id,mingc\n" + "from jihkjb  order by mingc  \n";
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql, "全部"));
		return;
	}	
	
//	运输方式
	public IDropDownBean getYunsfsDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getYunsfsDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYunsfsDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setYunsfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getYunsfsDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYunsfsDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getYunsfsDropDownModels() {
		String sql = "select id,mingc\n" + "from yunsfsb   order by mingc  \n";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return;
	}
	
//	 供应商
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
		String sql = "select id,mingc\n" + "from gongysb   order by mingc  \n";
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}
	

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString13(rbvalue);
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
		list.add(new IDropDownBean(1, "实收量"));
		list.add(new IDropDownBean(2, "净重"));
		list.add(new IDropDownBean(3, "票重"));
		list.add(new IDropDownBean(4, "运损"));
		list.add(new IDropDownBean(5, "盈吨"));
		list.add(new IDropDownBean(6, "亏吨"));
		list.add(new IDropDownBean(7, "车数"));
		
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	private String getDataField(){
		String strDataField=getChartDropDownValue().getValue();
		
		if (strDataField.equals("实收量")){
			return getLaimlField()+" as shul \n";
		}else if (strDataField.equals("票重")){
			return " sum(round_new(fh.biaoz,0)) as shul \n";
		}else if(strDataField.equals("净重")){
			return " sum(round_new(fh.jingz,0)) as shul \n";
		}else if(strDataField.equals("运损")){
			return " sum(round_new(fh.yuns,0)) as shul \n";
		}else if(strDataField.equals("盈吨")){
			return " sum(round_new(fh.yingd,0)) as shul \n";
		}else if(strDataField.equals("亏吨")){ 
			return " sum(round_new((fh.yingd-fh.yingk),0)) as shul \n";
		}else if(strDataField.equals("车数")){ 
			return " sum(round_new(fh.ches,0)) as shul \n";
		}
		return " sum(round_new(fh.biaoz,0)) as shul \n";
	}
	
	private int getDigts(){
		String strDataField=getChartDropDownValue().getValue();
		if (strDataField.equals("票重")){
			return 0;
		}else if(strDataField.equals("净重")){
			return 1;
		}else if(strDataField.equals("运损")){
			return 2;
		}else if(strDataField.equals("盈吨")){
			return 2;
		}else if(strDataField.equals("亏吨")){
			return 3;
		}else if(strDataField.equals("车数")){
			return 4;
		}
		return 0;
	}
}