package com.zhiren.jt.rezcfxbreport;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Rezcfxbreport  extends BasePage implements PageValidateListener{
	
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
	
	//开始日期
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
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
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getLeijrz();
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
			this.getLeijrz();
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}

		if(_fengschange){
			
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
	}
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (getBaoblxValue().getValue().equals("累计热值")){
			return getLeijrz();
		}else if(getBaoblxValue().getValue().equals("累计入厂热值升幅排序(区域公司)")){
			return getGongsrcrz();
		}else if(getBaoblxValue().getValue().equals("累计入厂热值升幅排序(厂级排序)")){
			return getChangjrcrz();
		}else if(getBaoblxValue().getValue().equals("环比热值")){
			return getHuanbrz();
		}else if(getBaoblxValue().getValue().equals("环比入厂热值排序(区域公司)")){
			return getGongshbrcrz();
		}else if(getBaoblxValue().getValue().equals("环比入厂热值排序(厂级排序)")){
			return getChangjhbrcrz();
		}
		else{
			return "无此报表";
		}
	}
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	private String getLeijrz(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="热值分析(累计热值差大于0.5)";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and dc.fuid=  " +this.getTreeid();

		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());


		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
				"select  dcb.mingc,(tqdr.rucrl-tqdr1.rulrl) as tqchaz,(dr.rucrl-dr1.rulrl) as bqchaz,(dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl) as chaz,\n" +
				"                  round(((dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl))/(tqdr.rucrl-tqdr1.rulrl)*100,2) as q\n" + 
				"      from\n" + 
				"            (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='累计'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )dr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='累计' and riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and diaor01bb.diancxxb_id=dc.id "+strGongsID+"\n" + 
				"              group by (dc.id))dr1,\n" + 
				"              (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='累计'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )tqdr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='累计' and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and diaor01bb.diancxxb_id=dc.id"+strGongsID+"\n" + 
				"               group by (dc.id))tqdr1,diancxxb dcb\n" + 
				"           where (dr.rucrl-dr1.rulrl)>0.5 and dcb.id=dr.id(+) and dcb.id=dr1.id(+) and dcb.id=tqdr.id(+) and dcb.id=tqdr1.id(+)";
		}else{
			strSQL=
				"select  dcb.mingc,(tqdr.rucrl-tqdr1.rulrl) as tqchaz,(dr.rucrl-dr1.rulrl) as bqchaz,(dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl) as chaz,\n" +
				"                  round(((dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl))/(tqdr.rucrl-tqdr1.rulrl)*100,2) as q\n" + 
				"      from\n" + 
				"            (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='累计'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )dr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='累计' and riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and diaor01bb.diancxxb_id=dc.id "+strGongsID+"\n" + 
				"              group by (dc.id))dr1,\n" + 
				"              (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='累计'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )tqdr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='累计' and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and diaor01bb.diancxxb_id=dc.id"+strGongsID+"\n" + 
				"               group by (dc.id))tqdr1,diancxxb dcb\n" + 
				"           where (dr.rucrl-dr1.rulrl)>0.5 and dcb.id=dr.id(+) and dcb.id=dr1.id(+) and dcb.id=tqdr.id(+) and dcb.id=tqdr1.id(+)";
		}
			long year=intyear-1;

		//System.out.println(strSQL);
		 ArrHeader=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂名称",""+year+"-1~"+year+"-"+intMonth+"",""+intyear+"-1~"+intyear+"-"+intMonth+"","差值","%"};

		 ArrWidth=new int[] {150,100,100,100,100};

		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 1, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,"填表日期"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,1,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//设置页数
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	//累计入厂热值升幅排序(区域公司)				
	private String getGongsrcrz(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="热值分析(累计入厂热值升幅排序(区域公司))";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and dc.fuid=  " +this.getTreeid();

		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());


		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=

				"select dcb.mingc,tqdr.rucrl,dr.rucrl,(dr.rucrl - tqdr.rucrl) as chaz,\n" +
				"                  round((dr.rucrl-tqdr.rucrl)/tqdr.rucrl*100,2) as q\n" + 
				"             from\n" + 
				"                    (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='累计'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id   "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )dr, (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='累计'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )tqdr,(\n " +
				"                    select distinct dq.id as id,dq.mingc from diancxxb dc,diaor04bb dr ,diancxxb dq\n" + 
				"                          where dr.diancxxb_id=dc.id and dq.id=dc.fuid\n" + 
				"                        		   and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)) "+strGongsID+") dcb \n"+
				"            where	\n" + 
				"					dcb.id=dr.id(+) and dcb.id=tqdr.id(+)\n" +
				"    		 order by chaz desc,q desc";
		}else{
			strSQL=
				"select dcb.mingc,tqdr.rucrl,dr.rucrl,(dr.rucrl - tqdr.rucrl) as chaz,\n" +
				"                  round((dr.rucrl-tqdr.rucrl)/tqdr.rucrl*100,2) as q\n" + 
				"             from\n" + 
				"                    (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='累计'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id   "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )dr, (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='累计'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )tqdr,(\n " +
				"                    select distinct dq.id as id,dq.mingc from diancxxb dc,diaor04bb dr ,diancxxb dq\n" + 
				"                          where dr.diancxxb_id=dc.id and dq.id=dc.fuid\n" + 
				"                        		   and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)) "+strGongsID+") dcb \n"+
				"            where	\n" + 
				"					dcb.id=dr.id(+) and dcb.id=tqdr.id(+)\n" +
				"    		 order by chaz desc,q desc";
}
		 long year=intyear-1;
		//System.out.println(strSQL);
		 ArrHeader=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂名称",""+year+"-1~"+year+"-"+intMonth+"",""+intyear+"-1~"+intyear+"-"+intMonth+"","差值","%"};

		 ArrWidth=new int[] {150,100,100,100,100};

		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 1, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,"填表日期"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,1,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//设置页数
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	//累计入厂热值升幅排序(区域公司)				
	private String getChangjrcrz(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="热值分析(累计入厂热值升幅排序(厂级排序))";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and dc.fuid=  " +this.getTreeid();

		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());


		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
				"select dc.mingc,nvl(bq.rucrl,0) as rucrl_bq,nvl(tq.rucrl,0) as rucrl_tq,(nvl(bq.rucrl,0) - nvl(tq.rucrl,0)) as chaz,\n" +
				"                 decode(nvl(tq.rucrl,0),0,'',round((nvl(bq.rucrl,0)-nvl(tq.rucrl,0))/nvl(tq.rucrl,0)*100,2)) as q\n" + 
				"          from\n" + 
				"                --dianc\n" + 
				"               ( select distinct dc.id,dc.mingc from diancxxb dc,diaor04bb dr\n" + 
				"                 where dr.diancxxb_id=dc.id\n" + 
				"                       and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))  "+strGongsID+") dc,\n" + 
				"               --本期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='累计'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) bq,\n" + 
				"               --同期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='累计'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) tq\n" + 
				"          where dc.id=bq.id(+) and dc.id=tq.id(+)\n" + 
				"          order by chaz desc,q desc";
		}else{
			strSQL=
				"select dc.mingc,nvl(bq.rucrl,0) as rucrl_bq,nvl(tq.rucrl,0) as rucrl_tq,(nvl(bq.rucrl,0) - nvl(tq.rucrl,0)) as chaz,\n" +
				"                 decode(nvl(tq.rucrl,0),0,'',round((nvl(bq.rucrl,0)-nvl(tq.rucrl,0))/nvl(tq.rucrl,0)*100,2)) as q\n" + 
				"          from\n" + 
				"                --dianc\n" + 
				"               ( select distinct dc.id,dc.mingc from diancxxb dc,diaor04bb dr\n" + 
				"                 where dr.diancxxb_id=dc.id\n" + 
				"                       and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))  "+strGongsID+") dc,\n" + 
				"               --本期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='累计'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) bq,\n" + 
				"               --同期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='累计'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) tq\n" + 
				"          where dc.id=bq.id(+) and dc.id=tq.id(+)\n" + 
				"          order by chaz desc,q desc";
		}
		long year=intyear-1;
		//System.out.println(strSQL);
		 ArrHeader=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂名称",""+year+"-1~"+year+"-"+intMonth+"",""+intyear+"-1~"+intyear+"-"+intMonth+"","差值","%"};

		 ArrWidth=new int[] {150,100,100,100,100};

		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 1, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,"填表日期"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,1,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//设置页数
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}	
	//****************************************************
	//环比热值差大于0.5				
	private String getHuanbrz(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="热值分析(环比热值差大于0.5)";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and dc.fuid=  " +this.getTreeid();

		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());


		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
				"select  dcb.mingc,(tqdr.rucrl-tqdr1.rulrl) as tqchaz,(dr.rucrl-dr1.rulrl) as bqchaz,(dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl) as chaz,\n" +
				"                  round(((dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl))/(tqdr.rucrl-tqdr1.rulrl)*100,2) as q\n" + 
				"      from\n" + 
				"            (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='本月'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )dr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='本月' and riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and diaor01bb.diancxxb_id=dc.id "+strGongsID+"\n" + 
				"              group by (dc.id))dr1,\n" + 
				"              (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='本月'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )tqdr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='本月' and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and diaor01bb.diancxxb_id=dc.id"+strGongsID+"\n" + 
				"               group by (dc.id))tqdr1,diancxxb dcb\n" + 
				"           where (dr.rucrl-dr1.rulrl)>0.5 and dcb.id=dr.id(+) and dcb.id=dr1.id(+) and dcb.id=tqdr.id(+) and dcb.id=tqdr1.id(+)";
		}else{
			strSQL=
				"select  dcb.mingc,(tqdr.rucrl-tqdr1.rulrl) as tqchaz,(dr.rucrl-dr1.rulrl) as bqchaz,(dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl) as chaz,\n" +
				"                  round(((dr.rucrl-dr1.rulrl)- (tqdr.rucrl-tqdr1.rulrl))/(tqdr.rucrl-tqdr1.rulrl)*100,2) as q\n" + 
				"      from\n" + 
				"            (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='本月'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )dr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='本月' and riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and diaor01bb.diancxxb_id=dc.id "+strGongsID+"\n" + 
				"              group by (dc.id))dr1,\n" + 
				"              (\n" + 
				"            select dc.id,decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                   from diaor04bb dr4,diancxxb dc\n" + 
				"                   where  dr4.fenx='本月'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dc.id=dr4.diancxxb_id "+strGongsID+"\n" + 
				"                   group by (dc.id)\n" + 
				"            )tqdr,\n" + 
				"            (\n" + 
				"            select dc.id as id,fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as rulrl,max(riq) as riq\n" + 
				"                    from diaor01bb ,diancxxb dc\n" + 
				"                    where  diaor01bb.fenx='本月' and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and diaor01bb.diancxxb_id=dc.id"+strGongsID+"\n" + 
				"               group by (dc.id))tqdr1,diancxxb dcb\n" + 
				"           where (dr.rucrl-dr1.rulrl)>0.5 and dcb.id=dr.id(+) and dcb.id=dr1.id(+) and dcb.id=tqdr.id(+) and dcb.id=tqdr1.id(+)";
		}
//		long year=intyear-1;
		long month=0;
		if(intMonth==1){
			month=1;
		}else{
			month=intMonth-1;
		}
		//System.out.println(strSQL);
		 ArrHeader=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂名称",""+intyear+"年"+month+"月",""+intyear+"年"+intMonth+"月","差值","%"};

		 ArrWidth=new int[] {150,100,100,100,100};

		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 1, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,"填表日期"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,1,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//设置页数
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	//环比入厂热值排序(区域公司)				
	private String getGongshbrcrz(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="热值分析(环比入厂热值排序(区域公司))";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and dc.fuid=  " +this.getTreeid();

		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());


		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=

				"select dcb.mingc,tqdr.rucrl,dr.rucrl,(dr.rucrl - tqdr.rucrl) as chaz,\n" +
				"                  round((dr.rucrl-tqdr.rucrl)/tqdr.rucrl*100,2) as q\n" + 
				"             from\n" + 
				"                    (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='本月'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id   "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )dr, (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='本月'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )tqdr,(\n " +
				"                    select distinct dq.id as id,dq.mingc from diancxxb dc,diaor04bb dr ,diancxxb dq\n" + 
				"                          where dr.diancxxb_id=dc.id and dq.id=dc.fuid\n" + 
				"                        		   and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)) "+strGongsID+") dcb \n"+
				"            where	\n" + 
				"					dcb.id=dr.id(+) and dcb.id=tqdr.id(+)\n" +
				"    		 order by chaz desc,q desc";
		}else{
			strSQL=
				"select dcb.mingc,tqdr.rucrl,dr.rucrl,(dr.rucrl - tqdr.rucrl) as chaz,\n" +
				"                  round((dr.rucrl-tqdr.rucrl)/tqdr.rucrl*100,2) as q\n" + 
				"             from\n" + 
				"                    (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='本月'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id   "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )dr, (\n" + 
				"                    select dq.id ,max(dq.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                           from diaor04bb dr4,diancxxb dc,diancxxb dq\n" + 
				"                           where  dr4.fenx='本月'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                                  and dq.id=dc.fuid\n" + 
				"                           group by (dq.id)\n" + 
				"                    )tqdr,(\n " +
				"                    select distinct dq.id as id,dq.mingc from diancxxb dc,diaor04bb dr ,diancxxb dq\n" + 
				"                          where dr.diancxxb_id=dc.id and dq.id=dc.fuid\n" + 
				"                        		   and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)) "+strGongsID+") dcb \n"+
				"            where	\n" + 
				"					dcb.id=dr.id(+) and dcb.id=tqdr.id(+)\n" +
				"    		 order by chaz desc,q desc";
}
//		long year=intyear-1;
		long month=0;
		if(intMonth==1){
			month=1;
		}else{
			month=intMonth-1;
		}
		//System.out.println(strSQL);
		 ArrHeader=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂名称",""+intyear+"年"+month+"月",""+intyear+"年"+intMonth+"月","差值","%"};

		 ArrWidth=new int[] {150,100,100,100,100};

		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 1, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,"填表日期"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,1,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//设置页数
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	//环比入厂热值排序(厂级排序)			
	private String getChangjhbrcrz(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="热值分析(环比入厂热值排序(厂级排序))";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and dc.fuid=  " +this.getTreeid();

		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());


		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
				"select dc.mingc,nvl(bq.rucrl,0) as rucrl_bq,nvl(tq.rucrl,0) as rucrl_tq,(nvl(bq.rucrl,0) - nvl(tq.rucrl,0)) as chaz,\n" +
				"                 decode(nvl(tq.rucrl,0),0,'',round((nvl(bq.rucrl,0)-nvl(tq.rucrl,0))/nvl(tq.rucrl,0)*100,2)) as q\n" + 
				"          from\n" + 
				"                --dianc\n" + 
				"               ( select distinct dc.id,dc.mingc from diancxxb dc,diaor04bb dr\n" + 
				"                 where dr.diancxxb_id=dc.id\n" + 
				"                       and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1))  "+strGongsID+") dc,\n" + 
				"               --本期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='本月'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) bq,\n" + 
				"               --同期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='本月'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) tq\n" + 
				"          where dc.id=bq.id(+) and dc.id=tq.id(+)\n" + 
				"          order by chaz desc,q desc";
		}else{
			strSQL=
				"select dc.mingc,nvl(bq.rucrl,0) as rucrl_bq,nvl(tq.rucrl,0) as rucrl_tq,(nvl(bq.rucrl,0) - nvl(tq.rucrl,0)) as chaz,\n" +
				"                 decode(nvl(tq.rucrl,0),0,'',round((nvl(bq.rucrl,0)-nvl(tq.rucrl,0))/nvl(tq.rucrl,0)*100,2)) as q\n" + 
				"          from\n" + 
				"                --dianc\n" + 
				"               ( select distinct dc.id,dc.mingc from diancxxb dc,diaor04bb dr\n" + 
				"                 where dr.diancxxb_id=dc.id\n" + 
				"                       and (dr.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or  dr.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1))  "+strGongsID+") dc,\n" + 
				"               --本期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='本月'and dr4.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) bq,\n" + 
				"               --同期\n" + 
				"               (select dc.id ,max(dc.mingc),decode(sum(dr4.jincsl),0,0,round(sum(dr4.jincsl*dr4.changffrl)/sum(dr4.jincsl),3)) as rucrl,max(riq) as riq\n" + 
				"                from diaor04bb dr4,diancxxb dc\n" + 
				"                where  dr4.fenx='本月'and dr4.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dc.id=dr4.diancxxb_id  "+strGongsID+"\n" + 
				"                       group by (dc.id)) tq\n" + 
				"          where dc.id=bq.id(+) and dc.id=tq.id(+)\n" + 
				"          order by chaz desc,q desc";
		}
//		long year=intyear-1;
		long month=0;
		if(intMonth==1){
			month=1;
		}else{
			month=intMonth-1;
		}
		//System.out.println(strSQL);
		 ArrHeader=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂名称",""+intyear+"年"+month+"月",""+intyear+"年"+intMonth+"月","差值","%"};

		 ArrWidth=new int[] {150,100,100,100,100};

		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 1, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,"填表日期"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,1,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//设置页数
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}	
	//****************************************************
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
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
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
	
	
//	矿别名称
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{
//		List fahdwList = new ArrayList();
//		fahdwList.add(new IDropDownBean(-1,"请选择"));
//		
//		String sql="";
//		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
////		System.out.println(sql);
//		ResultSet rs = con.getResultSet(sql);
//		for(int i=0;rs.next();i++){
//			fahdwList.add(new IDropDownBean(i,rs.getString("meikdqmc")));
//		}
		
		String sql="";
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	

//	年份
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
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * 月份
	 */
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
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
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
//	 分公司下拉框
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
	
//*************************************************************
//	矿报表类型
	public boolean _Baoblxchange = false;
//	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getIBaoblxModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean1() != null) {
			id = (((Visit)getPage().getVisit()).getDropDownBean1()).getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

//	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIBaoblxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIBaoblxModels() {
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"累计热值"));
		fahdwList.add(new IDropDownBean(1,"累计入厂热值升幅排序(区域公司)"));
		fahdwList.add(new IDropDownBean(2,"累计入厂热值升幅排序(厂级排序)"));
		fahdwList.add(new IDropDownBean(3,"环比热值"));
		fahdwList.add(new IDropDownBean(4,"环比入厂热值排序(区域公司)"));
		fahdwList.add(new IDropDownBean(5,"环比入厂热值排序(厂级排序)"));

		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(fahdwList));

		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
	
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
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
		

		tb1.addText(new ToolbarText("报表查询:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setId("Tongjkj");
		cb.setWidth(200);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));

		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
		
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

	
}