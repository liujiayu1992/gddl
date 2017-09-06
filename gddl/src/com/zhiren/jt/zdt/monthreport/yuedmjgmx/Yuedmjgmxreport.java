package com.zhiren.jt.zdt.monthreport.yuedmjgmx;

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
/* 
* 时间：2009-05-23
* 作者： sy
* 修改内容：修改天然煤综合车板价\热值\到厂价判断除数到货量是否为零,若零则值为零
*			
*/ 

public class Yuedmjgmxreport  extends BasePage implements PageValidateListener{
	
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
			//this.getSelectData();
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
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			setBaoblxValue(null);
			getIBaoblxModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
		
		}
		
		getToolBars() ;
		this.Refurbish();
	}
	
	private String RT_HET="Yuedmjgmxreport";//月度煤价格明细
	private String mstrReportName="Yuedmjgmxreport";
	
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
	
	private String getSelectData(){
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
		
	
		
		

		String strGongsID = "";
		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			notHuiz=" having not grouping(f.mingc)=1 ";//当电厂树是分公司时,去掉集团汇总
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			
				notHuiz=" having not  grouping(dc.mingc)=1";//当电厂树是电厂时,去掉分公司和集团汇总
			
		
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		
		
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
		
		//此报表是张广宏修改上报的假数据.注意此页面的sql中用到计划口径表的id做条件,如果计划口径表id改变,此表将不能正确查询出数据.
		//重点订货 :1,     区域订货:3,      市场采购:2
		if(getBaoblxValue().getValue().equals("分厂分矿汇总")){
				
		  strSQL = "select decode(grouping(dc.mingc)+grouping(f.mingc)+grouping(g.mingc),3,'总计',2,f.mingc,1,'&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as diancmc,\n"
					+ "    sum(decode(y.jihkjb_id,1,y.daohl,0)) as daohl_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.chebj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as chebj_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.farl,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),3) as farl_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.daocj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as daocj_zd,\n"
					+ "    sum(decode(y.jihkjb_id,3,y.daohl,0)) as daohl_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.chebj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),2) as chebj_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.farl,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as farl_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.daocj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as daocj_qy,\n"
					+ "    sum(decode(y.jihkjb_id,2,y.daohl,0)) as daohl_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.chebj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as chebj_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.farl,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as farl_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.daocj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as daocj_sc,\n"
					+ "    sum(y.daohl) as daohl_zh,\n"
					+ "    Round(decode(sum(y.daohl),0,0,sum(y.daohl*y.chebj)/sum(y.daohl)),2) as chebj_zh,\n"
					+ "    Round(decode(sum(y.daohl),0,0,sum(y.daohl*y.farl)/sum(y.daohl)),2) as farl_zh,\n"
					+ "    Round(decode(sum(y.daohl),0,0,sum(y.daohl*y.daocj)/sum(y.daohl)),2) as daocj_zh\n"
					+ "  from yuedmjgmxb y, diancxxb dc, gongysb g, jihkjb j,vwfengs f\n"
					+ " where y.diancxxb_id = dc.id\n"
					+ " and  y.gongysb_id=g.id\n"
					+ " and y.jihkjb_id=j.id\n"
					+ " and dc.fuid=f.id\n"
					+ " and y.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  "+strGongsID+"\n"
					+ " group by rollup(f.mingc,dc.mingc,g.mingc)\n"
					+"  "+notHuiz+"\n"
					+ " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

			  


// 直属分厂汇总
				 ArrHeader=new String[4][17];
				 ArrHeader[0]=new String[] {"电厂名称","跨区交易","跨区交易","跨区交易","跨区交易","区域交易","区域交易","区域交易","区域交易","市场交易","市场交易","市场交易","市场交易","天然煤综合","天然煤综合","天然煤综合","天然煤综合"};
				 ArrHeader[1]=new String[] {"电厂名称","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价"};
				 ArrHeader[2]=new String[] {"电厂名称","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨"};
				 ArrHeader[3]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17"};
				 
				 ArrWidth=new int[] {150,60,50,50,50,60,50,50,50,60,50,50,50,60,50,50,50};
				 arrFormat= new String []{"","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00"};
					
				 iFixedRows=1;
				 iCol=10;
				 
			}else if(getBaoblxValue().getValue().equals("分厂汇总")){
				
				strSQL ="select decode(grouping(dc.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||dc.mingc) as diancmc,\n"
					+ "    sum(decode(y.jihkjb_id,1,y.daohl,0)) as daohl_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.chebj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as chebj_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.farl,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),3) as farl_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.daocj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as daocj_zd,\n"
					+ "    sum(decode(y.jihkjb_id,3,y.daohl,0)) as daohl_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.chebj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),2) as chebj_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.farl,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as farl_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.daocj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as daocj_qy,\n"
					+ "    sum(decode(y.jihkjb_id,2,y.daohl,0)) as daohl_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.chebj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as chebj_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.farl,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as farl_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.daocj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as daocj_sc,\n"
					+ "    sum(y.daohl) as daohl_zh,\n"
					+ "    Round(sum(y.daohl*y.chebj)/sum(y.daohl),2) as chebj_zh,\n"
					+ "    Round(sum(y.daohl*y.farl)/sum(y.daohl),2) as farl_zh,\n"
					+ "    Round(sum(y.daohl*y.daocj)/sum(y.daohl),2) as daocj_zh\n"
					+ "  from yuedmjgmxb y, diancxxb dc, gongysb g, jihkjb j,vwfengs f\n"
					+ " where y.diancxxb_id = dc.id\n"
					+ " and  y.gongysb_id=g.id\n"
					+ " and y.jihkjb_id=j.id\n"
					+ " and dc.fuid=f.id\n"
					+ " and y.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  "+strGongsID+"\n"
					+ " group by rollup(f.mingc,dc.mingc)\n"
					+"  "+notHuiz+"\n"
					+ " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc";



				ArrHeader=new String[4][17];
				 ArrHeader[0]=new String[] {"电厂名称","跨区交易","跨区交易","跨区交易","跨区交易","区域交易","区域交易","区域交易","区域交易","市场交易","市场交易","市场交易","市场交易","天然煤综合","天然煤综合","天然煤综合","天然煤综合"};
				 ArrHeader[1]=new String[] {"电厂名称","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价"};
				 ArrHeader[2]=new String[] {"电厂名称","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨"};
				 ArrHeader[3]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17"};
				 
				 ArrWidth=new int[] {150,60,50,50,50,60,50,50,50,60,50,50,50,60,50,50,50};
				 arrFormat= new String []{"","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00"};
					
				 iFixedRows=1;
				 iCol=10;
			}else if(getBaoblxValue().getValue().equals("分矿汇总")){
			
					if(jib==3){
						notHuiz=" having not grouping(f.mingc)=1";
					}
				strSQL = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n"
					+ "    sum(decode(y.jihkjb_id,1,y.daohl,0)) as daohl_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.chebj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as chebj_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.farl,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),3) as farl_zd,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.daocj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as daocj_zd,\n"
					+ "    sum(decode(y.jihkjb_id,3,y.daohl,0)) as daohl_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.chebj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),2) as chebj_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.farl,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as farl_qy,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.daocj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as daocj_qy,\n"
					+ "    sum(decode(y.jihkjb_id,2,y.daohl,0)) as daohl_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.chebj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as chebj_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.farl,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as farl_sc,\n"
					+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.daocj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as daocj_sc,\n"
					+ "    sum(y.daohl) as daohl_zh,\n"
					+ "    Round(sum(y.daohl*y.chebj)/sum(y.daohl),2) as chebj_zh,\n"
					+ "    Round(sum(y.daohl*y.farl)/sum(y.daohl),2) as farl_zh,\n"
					+ "    Round(sum(y.daohl*y.daocj)/sum(y.daohl),2) as daocj_zh\n"
					+ "  from yuedmjgmxb y, diancxxb dc, gongysb g, jihkjb j,vwfengs f\n"
					+ " where y.diancxxb_id = dc.id\n"
					+ " and  y.gongysb_id=g.id\n"
					+ " and y.jihkjb_id=j.id\n"
					+ " and dc.fuid=f.id\n"
					+ " and y.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  "+strGongsID+"\n"
					+ " group by rollup(f.mingc,g.mingc)\n"
					+"  "+notHuiz+"\n"
					+ " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";


				
				 ArrHeader=new String[4][17];
				 ArrHeader[0]=new String[] {"购煤地区","跨区交易","跨区交易","跨区交易","跨区交易","区域交易","区域交易","区域交易","区域交易","市场交易","市场交易","市场交易","市场交易","天然煤综合","天然煤综合","天然煤综合","天然煤综合"};
				 ArrHeader[1]=new String[] {"购煤地区","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价","到货量","车板<br>(平仓)价","热值","到厂价"};
				 ArrHeader[2]=new String[] {"购煤地区","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨","吨","元/吨","兆焦/千克","元/吨"};
				 ArrHeader[3]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17"};
				 
				 ArrWidth=new int[] {150,60,50,50,50,60,50,50,50,60,50,50,50,60,50,50,50};
				 arrFormat=new String []{"","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00"};
				 iFixedRows=1;
				 iCol=10;
			}
			
			// System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
			
			Table tb = new Table(rs, 4, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle( getBiaotmc()+intyear+"年"+intMonth+"月电煤价格明细表(分购煤地区)", ArrWidth);
			rt.setDefaultTitle(1, 3, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			
			rt.setDefaultTitle(7, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
			rt.setDefaultTitle(13, 5, "cpi燃料管理01表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			if(rt.body.getRows()>4){
				rt.body.setCellAlign(5, 1, Table.ALIGN_CENTER);
			}
		
			rt.body.setColFormat(arrFormat);
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(13,3,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
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
		fahdwList.add(new IDropDownBean(0,"分厂分矿汇总"));
		fahdwList.add(new IDropDownBean(1,"分厂汇总"));
		fahdwList.add(new IDropDownBean(2,"分矿汇总"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
			int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
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
			int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
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