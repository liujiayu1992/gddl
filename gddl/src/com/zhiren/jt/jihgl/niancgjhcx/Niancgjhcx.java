package com.zhiren.jt.jihgl.niancgjhcx;

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
import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
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



public class Niancgjhcx  extends BasePage implements PageValidateListener {
	

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
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}


	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		getSelectData();
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			_BeginriqValue = new Date();
			visit.setList1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setDiancmcValue(null);
			this.getFengsModels();
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setTreeid(null);
			this.setNianfValue(null);
			this.getNianfModels();
			
			isBegin=true;
			//this.getSelectData();
		}
		
		/*if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(_fengschange){
			_fengschange=false;
			Refurbish();
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			this.Refurbish();
		}*/
		getToolBar();
		Refurbish();
	}
	
	private String RT_HET="dinghjhcx";
	private String mstrReportName="dinghjhcx";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "无此报表";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	
	/**
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		
		int jib=this.getDiancTreeJib();
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename=intyear+"年采购计划查询";
		int iFixedRows=0;//固定行号
		
		StringBuffer strSQL = new StringBuffer();
		
		StringBuffer strgrouping = new StringBuffer();
		StringBuffer strwhere = new StringBuffer();
		StringBuffer strgroupby = new StringBuffer();
		StringBuffer strhaving = new StringBuffer();
		StringBuffer strorderby = new StringBuffer();
		
		strSQL.append("select \n");
		if (getBaoblxValue().getValue().equals("分厂")){
			titlename=titlename+"(分厂)";
			iFixedRows=1;
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
				strwhere.append("");
				strgroupby.append("group by rollup(dc.fgsmc,dc.mingc)\n");
				strhaving.append("");
				strorderby.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//燃料公司
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',2,dc.rlgsmc,1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append("group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strhaving.append("having not grouping(dc.rlgsmc)=1\n");
						strorderby.append("order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}else{//分公司
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append("group by rollup(dc.fgsmc,dc.mingc)\n");
						strhaving.append("");
						strorderby.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//选电厂只刷新出该电厂
				strgrouping.append("decode(grouping(dc.mingc),1,'总计',dc.mingc) as diancmc,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append("group by rollup(dc.mingc)\n");
				strhaving.append("having not grouping(dc.mingc)=1\n");
				strorderby.append("order by grouping(dc.mingc) desc,dc.mingc desc\n");
			}
			
			ArrHeader=new String[3][18];
			 ArrHeader[0]=new String[] {"收货电厂","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)"};
			 ArrHeader[1]=new String[] {"收货电厂","全年合计","一季度","一季度","一季度","一季度","二季度","二季度","二季度","二季度","三季度","三季度","三季度","三季度","四季度","四季度","四季度","四季度"};
			 ArrHeader[2]=new String[] {"收货电厂","全年合计","一季度","一月","二月","三月","二季度","四月","五月","六月","三季度","七月","八月","九月","四季度","十月","十一月","十二月"};
			 //说明:如果报表不用设置页面距,横向打印正好答应完,ArrWidth的总宽度要小于971
			 ArrWidth=new int[] {120,55,55,48,48,48,55,48,48,48,55,48,48,48,55,48,48,48};
			
		}else if (getBaoblxValue().getValue().equals("分矿")){
			titlename=titlename+"(分矿)";
			iFixedRows=1;
			strgrouping.append("decode(grouping(g.id),1,'全网合计',max(g.mingc)) as meikdqmc,\n");
			strgroupby.append("group by rollup(g.id)\n");
			strorderby.append("order by grouping(g.id) desc,g.id desc\n");
			 
			if(jib==1){//选集团时刷新出所有的电厂
				strwhere.append("");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
			}else if (jib==3){//选电厂只刷新出该电厂
				strwhere.append(" and dc.id=").append(this.getTreeid());
			}
			
			ArrHeader=new String[3][18];
			 ArrHeader[0]=new String[] {"收货电厂","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)"};
			 ArrHeader[1]=new String[] {"收货电厂","全年合计","一季度","一季度","一季度","一季度","二季度","二季度","二季度","二季度","三季度","三季度","三季度","三季度","四季度","四季度","四季度","四季度"};
			 ArrHeader[2]=new String[] {"收货电厂","全年合计","一季度","一月","二月","三月","二季度","四月","五月","六月","三季度","七月","八月","九月","四季度","十月","十一月","十二月"};
			 //说明:如果报表不用设置页面距,横向打印正好答应完,ArrWidth的总宽度要小于971
			 ArrWidth=new int[] {120,55,55,48,48,48,55,48,48,48,55,48,48,48,55,48,48,48};
			
		}else if (getBaoblxValue().getValue().equals("分厂分矿")) {
			titlename=titlename+"(分厂分矿)";
			iFixedRows=2;
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
				strwhere.append("");
				strgroupby.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc)\n");
				strhaving.append("");
				strorderby.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//燃料公司
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',2,dc.rlgsmc,1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append("group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc,g.mingc)\n");
						strhaving.append("having not grouping(dc.rlgsmc)=1\n");
						strorderby.append("order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
					}else{//分公司
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc)\n");
						strhaving.append("");
						strorderby.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//选电厂只刷新出该电厂
				strgrouping.append("decode(grouping(dc.mingc),1,'总计',dc.mingc) as diancmc,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append("group by rollup(dc.mingc,g.mingc)\n");
				strhaving.append("having not grouping(dc.mingc)=1\n");
				strorderby.append("order by grouping(dc.mingc) desc,dc.mingc desc,grouping(g.mingc) desc,g.mingc\n");
			}
			
			 ArrHeader=new String[3][19];
			 ArrHeader[0]=new String[] {"电厂名称","煤矿","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)"};
			 ArrHeader[1]=new String[] {"电厂名称","煤矿","全年合计","一季度","一季度","一季度","一季度","二季度","二季度","二季度","二季度","三季度","三季度","三季度","三季度","四季度","四季度","四季度","四季度"};
			 ArrHeader[2]=new String[] {"电厂名称","煤矿","全年合计","一季度","一月","二月","三月","二季度","四月","五月","六月","三季度","七月","八月","九月","四季度","十月","十一月","十二月"};

			 ArrWidth=new int[] {120,100,55,55,48,48,48,55,48,48,48,55,48,48,48,55,48,48,48};
			
		}else if (getBaoblxValue().getValue().equals("分矿百分比表")){
			 titlename=titlename+"(分矿百分比表)";
			 iFixedRows=1;
			 String strtiaoj="";
			 String strGongsID="";
			 if(jib==1){//选集团时刷新出所有的电厂
				strtiaoj=" and dh.diancxxb_id=dc.id  ";
				strGongsID = " and n.diancxxb_id=d.id  ";
				
			 }else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strtiaoj="and dh.diancxxb_id=dc.id and dc.fuid=" +this.getTreeid();
				strGongsID = "and n.diancxxb_id=d.id and d.fuid= " + this.getTreeid();
			 }else if (jib==3){//选电厂只刷新出该电厂
				strtiaoj="and dh.diancxxb_id=dc.id and dc.id="+ this.getTreeid();
				strGongsID = "and n.diancxxb_id=d.id and d.id= "+  this.getTreeid();
			 }
			 
			 strSQL.append(" b.meikdqmc,round(b.hej/a.hej*100,2) as hej,\n" +
			 "round(b.JID1/a.hej*100,2) as jid1,round(b.yue1/a.hej*100,2) as yue1,round(b.yue2/a.hej*100,2) as yue2,round(b.yue3/a.hej*100,2) as yue3,\n" + 
			 "round(b.JID2/a.hej*100,2) as jid2,round(b.yue4/a.hej*100,2) as yue4,round(b.yue5/a.hej*100,2) as yue5,round(b.yue6/a.hej*100,2) as yue6,\n" + 
			 "round(b.JID3/a.hej*100,2) as jid3,round(b.yue7/a.hej*100,2) as yue7,round(b.yue8/a.hej*100,2) as yue8,round(b.yue9/a.hej*100,2) as yue9,\n" + 
			 "round(b.JID4/a.hej*100,2) as jid4,round(b.yue10/a.hej*100,2) as yue10,round(b.yue11/a.hej*100,2) as yue11,round(b.yue12/a.hej*100,2) as yue12\n" + 
			 " from (select\n" + 
			 "decode(grouping(mk.mingc),\n" + 
			 "1,'全网合计',max(mk.mingc)) as meikdqmc,\n" + 
			 "to_char(round(sum(dh.hej),1),'fm99999999.00') as hej,\n" + 
			 "to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')<=3 THEN dh.hej ELSE 0 END),1),'fm99999999.00') AS JID1,\n" + 
			 "to_char(round(sum(decode(to_char(dh.riq,'mm'),1,dh.hej,0)),1),'fm99999999.00') as yue1,to_char(round(sum(decode(to_char(dh.riq,'mm'),2,dh.hej,0)),1),'fm99999999.00') as yue2, to_char(round(sum(decode(to_char(dh.riq,'mm'),3,dh.hej,0)),1),'fm99999999.00') as yue3,\n" + 
			 "to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')>3 and to_char(dh.riq,'mm')<=6 THEN dh.hej ELSE 0 END),1),'fm99999999.00') AS JID2,\n" + 
			 "to_char(round(sum(decode(to_char(dh.riq,'mm'),4,dh.hej,0)),1),'fm99999999.00') as yue4,to_char(round(sum(decode(to_char(dh.riq,'mm'),5,dh.hej,0)),1),'fm99999999.00') as yue5,to_char(round(sum(decode(to_char(dh.riq,'mm'),6,dh.hej,0)),1),'fm99999999.00') as yue6,\n" + 
			 "to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')>6 and to_char(dh.riq,'mm')<=9 THEN dh.hej ELSE 0 END),1),'fm99999999.00') AS JID3,\n" + 
			 "to_char(round(sum(decode(to_char(dh.riq,'mm'),7,dh.hej,0)),1),'fm99999999.00') as yue7,to_char(round(sum(decode(to_char(dh.riq,'mm'),8,dh.hej,0)),1),'fm99999999.00') as yue8,to_char(round(sum(decode(to_char(dh.riq,'mm'),9,dh.hej,0)),1),'fm99999999.00') as yue9,\n" + 
			 "to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')>9 and to_char(dh.riq,'mm')<=12 THEN dh.hej ELSE 0 END),1),'fm99999999.00') AS JID4,\n" + 
			 "to_char(round(sum(decode(to_char(dh.riq,'mm'),10,dh.hej,0)),1),'fm99999999.00') as yue10,to_char(round(sum(decode(to_char(dh.riq,'mm'),11,dh.hej,0)),1),'fm99999999.00') as yue11,to_char(round(sum(decode(to_char(dh.riq,'mm'),12,dh.hej,0)),1),'fm99999999.00') as yue12\n" + 
			 "from niancgjhb dh,\n" + 
			 "(select md.id,md.mingc from gongysb md) mk,diancxxb dc\n" + 
			 "where dh.gongysb_id=mk.id and to_char(dh.riq,'yyyy')='"+ intyear+"' "+strtiaoj+"\n" + 
			 "group by rollup(mk.mingc)\n" + 
			 "order by grouping(mk.mingc) desc,mk.mingc desc) b,\n" + 
			 "(select sum(n.hej) as hej from niancgjhb n ,diancxxb d where to_char(n.riq,'yyyy')='"+ intyear+"' "+strGongsID+") a");
			 
			 ArrHeader=new String[3][18];
			 ArrHeader[0]=new String[] {"煤矿","年度煤炭订货数量(万吨)","年度煤炭订货数量(万吨)","年度煤炭订货数量(万吨)","年度煤炭订货数量(万吨)","年度煤炭订货数量(万吨)","年度煤炭订货数量(万吨)","年度煤炭订货数量(万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)"};
			 ArrHeader[1]=new String[] {"煤矿","全年合计","一季度","一季度","一季度","一季度","二季度","二季度","二季度","二季度","三季度","三季度","三季度","三季度","四季度","四季度","四季度","四季度"};
			 ArrHeader[2]=new String[] {"煤矿","全年合计","一季度","一月","二月","三月","二季度","四月","五月","六月","三季度","七月","八月","九月","四季度","十月","十一月","十二月"};

			 ArrWidth=new int[] {120,55,55,48,48,48,55,48,48,48,55,48,48,48,55,48,48,48};
		}else if(getBaoblxValue().getValue().equals("分矿分厂")){
			titlename=titlename+"(分矿分厂)";
			iFixedRows=2;
			if(jib==1){//选集团时刷新出所有的电厂
//				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
//				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
				
				strgrouping.append("decode(grouping(g.mingc),1,'总计',g.mingc) as meikmc,\n");
				strgrouping.append("decode(grouping(g.mingc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.fgsmc||'小计',dc.mingc) as diancmc,\n");
				
				
				strwhere.append("");
				strgroupby.append("group by rollup(g.mingc,dc.fgsmc,dc.mingc)\n");
				strhaving.append("");
				strorderby.append("order by grouping(g.mingc) desc,g.mingc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//燃料公司
//						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',2,dc.rlgsmc,1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
//						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
						
						strgrouping.append("decode(grouping(g.mingc),1,'总计',g.mingc) as meikmc,\n");
						strgrouping.append("decode(grouping(g.mingc)+grouping(dc.rlgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.rlgsmc||'小计',dc.mingc) as diancmc,\n");
						
						
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append("group by rollup(g.mingc,dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strhaving.append("having not grouping(dc.rlgsmc)=1\n");
						strorderby.append("order by grouping(g.mingc) desc,g.mingc,grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}else{//分公司
//						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n");
//						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',g.mingc) as meikmc,\n");
						
						strgrouping.append("decode(grouping(g.mingc),1,'总计',g.mingc) as meikmc,\n");
						strgrouping.append("decode(grouping(g.mingc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.fgsmc||'小计',dc.mingc) as diancmc,\n");
						
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append("group by rollup(g.mingc,dc.fgsmc,dc.mingc)\n");
						strhaving.append("");
						strorderby.append("order by grouping(g.mingc) desc,g.mingc,grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,dc.mingc desc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//选电厂只刷新出该电厂
				strgrouping.append("decode(grouping(g.mingc),1,'总计',g.mingc) as meikmc,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'小计',dc.mingc) as diancmc,\n");
				
				
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append("group by rollup(g.mingc,dc.mingc)\n");
				strhaving.append("having not grouping(g.mingc)=1\n");
				strorderby.append("order by grouping(g.mingc) desc,g.mingc,grouping(dc.mingc) desc,dc.mingc desc\n");
			}
			
			 ArrHeader=new String[3][19];
			 ArrHeader[0]=new String[] {"煤矿","电厂名称","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)","年度煤炭订货数量(单位:万吨)"};
			 ArrHeader[1]=new String[] {"煤矿","电厂名称","全年合计","一季度","一季度","一季度","一季度","二季度","二季度","二季度","二季度","三季度","三季度","三季度","三季度","四季度","四季度","四季度","四季度"};
			 ArrHeader[2]=new String[] {"煤矿","电厂名称","全年合计","一季度","一月","二月","三月","二季度","四月","五月","六月","三季度","七月","八月","九月","四季度","十月","十一月","十二月"};

			 ArrWidth=new int[] {120,100,55,55,48,48,48,55,48,48,48,55,48,48,48,55,48,48,48};
			
		}
		
		if (!getBaoblxValue().getValue().equals("分矿百分比表")){
			strSQL.append(strgrouping);
			strSQL.append("to_char(round(sum(dh.hej),1),'fm99999999.0') as hej,\n");
			strSQL.append("to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')<=3 THEN dh.hej ELSE 0 END),1),'fm99999999.0') AS JID1,\n");
			strSQL.append("to_char(round(sum(decode(to_char(dh.riq,'mm'),1,dh.hej,0)),1),'fm99999999.0') as yue1,to_char(round(sum(decode(to_char(dh.riq,'mm'),2,dh.hej,0)),1),'fm99999999.0') as yue2, to_char(round(sum(decode(to_char(dh.riq,'mm'),3,dh.hej,0)),1),'fm99999999.0') as yue3,\n");
			strSQL.append("to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')>3 and to_char(dh.riq,'mm')<=6 THEN dh.hej ELSE 0 END),1),'fm99999999.0') AS JID2,\n");
			strSQL.append("to_char(round(sum(decode(to_char(dh.riq,'mm'),4,dh.hej,0)),1),'fm99999999.0') as yue4,to_char(round(sum(decode(to_char(dh.riq,'mm'),5,dh.hej,0)),1),'fm99999999.0') as yue5,to_char(round(sum(decode(to_char(dh.riq,'mm'),6,dh.hej,0)),1),'fm99999999.0') as yue6,\n");
			strSQL.append("to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')>6 and to_char(dh.riq,'mm')<=9 THEN dh.hej ELSE 0 END),1),'fm99999999.0') AS JID3,\n");
			strSQL.append("to_char(round(sum(decode(to_char(dh.riq,'mm'),7,dh.hej,0)),1),'fm99999999.0') as yue7,to_char(round(sum(decode(to_char(dh.riq,'mm'),8,dh.hej,0)),1),'fm99999999.0') as yue8,to_char(round(sum(decode(to_char(dh.riq,'mm'),9,dh.hej,0)),1),'fm99999999.0') as yue9,\n");
			strSQL.append("to_char(round(sum(CASE WHEN to_char(dh.riq,'mm')>9 and to_char(dh.riq,'mm')<=12 THEN dh.hej ELSE 0 END),1),'fm99999999.0') AS JID4,\n");
			strSQL.append("to_char(round(sum(decode(to_char(dh.riq,'mm'),10,dh.hej,0)),1),'fm99999999.0') as yue10,to_char(round(sum(decode(to_char(dh.riq,'mm'),11,dh.hej,0)),1),'fm99999999.0') as yue11,to_char(round(sum(decode(to_char(dh.riq,'mm'),12,dh.hej,0)),1),'fm99999999.0') as yue12\n");
			strSQL.append("from niancgjhb dh,vwdianc dc,gongysb g\n");
			strSQL.append("where dh.diancxxb_id=dc.id and dh.gongysb_id=g.id and to_char(dh.riq,'yyyy')='"+ intyear+"' \n");
			strSQL.append(strwhere);
			strSQL.append(strgroupby);
			strSQL.append(strhaving);
			strSQL.append(strorderby);
		}
		
		
		ResultSet rs = cn.getResultSet(strSQL.toString());
		 
		rt.setTitle(titlename, ArrWidth);
		Visit visit=(Visit)this.getPage().getVisit();
		String zhibdw=this.getDiancmc();
		if(this.getDiancmc().equals("北京大唐燃料有限公司")&&visit.getRenyjb()==2){
			zhibdw="大唐国际发电股份有限公司燃料管理部";
		}
		
		rt.setDefaultTitle(1, 5, "制表单位:"+zhibdw, Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 7, this.getNianfValue()+"年", Table.ALIGN_CENTER);
		rt.setDefaultTitle(17, 2, "单位:万吨", Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 3, 0, iFixedRows));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(36);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRowCol();
		
		if(rt.body.getRows()>3){
			rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
		}
		
		rt.body.ShowZero =false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 8, "审核:",Table.ALIGN_CENTER);
//		Visit visit=(Visit) getPage().getVisit();
		rt.setDefautlFooter(17,2,"制表:",Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		

		return rt.getAllPagesHtml();
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
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
		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
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
		for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
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
//	矿报表类型
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"分厂"));
		fahdwList.add(new IDropDownBean(1,"分矿"));
		fahdwList.add(new IDropDownBean(2,"分厂分矿"));
		fahdwList.add(new IDropDownBean(3,"分矿分厂"));
		fahdwList.add(new IDropDownBean(4,"分矿百分比表"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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

public void getToolBar() {
	Toolbar tb1 = new Toolbar("tbdiv");
	

	tb1.addText(new ToolbarText("统计口径:"));
	ComboBox cb = new ComboBox();
	cb.setTransform("BaoblxDropDown");
	cb.setId("Tongjkj");
	cb.setWidth(120);
	tb1.addField(cb);
	tb1.addText(new ToolbarText("-"));
	
	
	
	tb1.addText(new ToolbarText("年份:"));
	ComboBox nianf = new ComboBox();
	nianf.setTransform("NIANF");
	nianf.setId("nianfShuax");
	nianf.setWidth(60);
	tb1.addField(nianf);
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
