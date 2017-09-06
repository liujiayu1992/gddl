package com.zhiren.jt.zdt.monthreport.yuedzhbb01report;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;

/* 
* 时间：2009-05-4
* 作者： ll
* 修改内容：1、修改查询sql中yuezbb表中字段的名称,按照yuezbb中新的公式，取新的字段名。
* 		   
*/ 
/* 
* 时间：2009-05-25
* 作者： ll
* 修改内容：1、修改查询sql中入厂热值和入炉热值的单位换算。
* 		   
*/ 
/* 
* 时间：2009-06-20
* 作者： ll
* 修改内容：1、修改查询sql中含税标煤单价和不含税标煤单价公式。
* 		   
*/ 
/* 
* 时间：2009-07-21
* 作者： ll
* 修改内容：1、修改查询sql中含税标煤单价和不含税标煤单价算法。
* 		   
*/ 
/* 
* 时间：2009-07-31
* 作者： ll
* 修改内容：1、Cpi燃料月度综合01表—热值取整
* 		   2、判断电厂id为141(芜湖电厂)，入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段
* 		   
*/ 
/* 
* 时间：2009-08-12
* 作者： ll
* 修改内容：1、判断电厂id为141(芜湖电厂)，显示入厂热值时取yuezlb中的qnet_ar，显示入厂入炉热值差时取yuezlb中的diancrz字段。
* 		   
*/ 
public class Yuedzhbb01report  extends BasePage implements PageValidateListener{
	
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
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
			this.getSelectData();
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
	//--------------------------
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	
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
		Visit visit = (Visit) getPage().getVisit();
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		String zhuangt="";
		String shulzt="";
		String zhilzt="";
		String shczt="";
		String rucbmdjzt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
				shulzt="";
				zhilzt="";
				shczt="";
				rucbmdjzt="";
			}else if(visit.getRenyjb()==2){
				zhuangt="";
				shulzt="";
				zhilzt="";
				shczt="";
				rucbmdjzt="";
				zhuangt=" and (zb.zhuangt=1 or zb.zhuangt=2)";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
				rucbmdjzt=" and (y.zhuangt=1 or y.zhuangt=2)";
				shczt=" and (yshc.zhuangt=1 or yshc.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt="";
				shulzt="";
				zhilzt="";
				shczt="";
				rucbmdjzt="";
				zhuangt=" and zb.zhuangt=2";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
				rucbmdjzt=" and  y.zhuangt=2";
				shczt=" and  yshc.zhuangt=2";
			}
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
		String strGongsID2 = "";
		String guolzj="";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			strGongsID2 = "";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			strGongsID2 = "where (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			guolzj=" and grouping(dc.fgsmc)=0\n";//分公司查看报表时过滤总计。
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			strGongsID2 = "where dc.id= " +this.getTreeid();
			guolzj=" and grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			strGongsID2 = "where dc.id= " +((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());
	
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="煤炭量质价完成情况一览表";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
				strSQL=
	
				"\n" +
				"select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc ||'合计','&nbsp;&nbsp;'||dc.mingc) as danwmc,\n" + 
				"  fx.fenx,\n" + 
				" decode(fx.fenx,'本月',sum(beny.laimsl),'累计',sum(leij.laimsl),'环比差',sum(beny.laimsl)-sum(sy.laimsl),'同比差',sum(leij.laimsl)-sum(tqlj.laimsl)) as laimsl\n" + 
				" ,decode(fx.fenx,'本月',sum(beny.haoml),'累计',sum(leij.haoml),'环比差',sum(beny.haoml)-sum(sy.haoml),'同比差',sum(leij.haoml)-sum(tqlj.haoml)) as haoml\n" + 
				" ,decode(fx.fenx,'本月',sum(beny.shijkc),'累计',sum(beny.shijkc),'环比差',sum(beny.shijkc)-sum(sy.shijkc),'同比差',sum(beny.shijkc)-sum(tqby.shijkc)) as shijkc\n" + 
				" ,decode(fx.fenx,'本月',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.rucmrz1)/sum(beny.laimsl),2))*1000/4.1816,0),\n" + 
				"        '累计',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.rucmrz1)/sum(leij.laimsl),2))*1000/4.1816,0),\n" + 
				"        '环比差',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.rucmrz1)/sum(beny.laimsl),2))*1000/4.1816,0)-round(decode(sum(sy.laimsl),0,0,round(sum(sy.laimsl*sy.rucmrz1)/sum(sy.laimsl),2))*1000/4.1816,0),\n" + 
				"        '同比差',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.rucmrz1)/sum(leij.laimsl),2))*1000/4.1816,0)-round(decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.laimsl*tqlj.rucmrz1)/sum(tqlj.laimsl),2))*1000/4.1816,0)) as rucmrz\n" + 
				"  ,decode(fx.fenx,'本月',round(decode(sum(beny.haoytrm),0,0,round(sum(beny.haoytrm*beny.rulmrz1)/sum(beny.haoytrm),2))*1000/4.1816,0),\n" + 
				"        '累计',round(decode(sum(leij.haoytrm),0,0,round(sum(leij.haoytrm*leij.rulmrz1)/sum(leij.haoytrm),2))*1000/4.1816,0),\n" + 
				"        '环比差',round(decode(sum(beny.haoytrm),0,0,round(sum(beny.haoytrm*beny.rulmrz1)/sum(beny.haoytrm),2))*1000/4.1816,0)-round(decode(sum(sy.haoytrm),0,0,round(sum(sy.haoytrm*sy.rulmrz1)/sum(sy.haoytrm),2))*1000/4.1816,0),\n" + 
				"        '同比差',round(decode(sum(leij.haoytrm),0,0,round(sum(leij.haoytrm*leij.rulmrz)/sum(leij.haoytrm),2))*1000/4.1816,0)-round(decode(sum(tqlj.haoytrm),0,0,round(sum(tqlj.haoytrm*tqlj.rulmrz)/sum(tqlj.haoytrm),2))*1000/4.1816,0)) as rulmrz\n" + 
				
				"   ,decode(fx.fenx,'本月',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.rucmrz0)/sum(beny.laimsl),2))*1000/4.1816,0)-round(decode(sum(beny.haoytrm),0,0,round(sum(beny.haoytrm*beny.rulmrz1)/sum(beny.haoytrm),2))*1000/4.1816,0),\n" + 
				"        '累计',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.rucmrz0)/sum(leij.laimsl),2))*1000/4.1816,0)-round(decode(sum(leij.haoytrm),0,0,round(sum(leij.haoytrm*leij.rulmrz1)/sum(leij.haoytrm),2))*1000/4.1816,0),\n" + 
				"        '环比差',(round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.rucmrz0)/sum(beny.laimsl),2))*1000/4.1816,0)-round(decode(sum(sy.laimsl),0,0,round(sum(sy.laimsl*sy.rucmrz0)/sum(sy.laimsl),2))*1000/4.1816,0))\n" + 
				"                 -(round(decode(sum(beny.haoytrm),0,0,round(sum(beny.haoytrm*beny.rulmrz1)/sum(beny.haoytrm),2))*1000/4.1816,0)-round(decode(sum(sy.haoytrm),0,0,round(sum(sy.haoytrm*sy.rulmrz1)/sum(sy.haoytrm),2))*1000/4.1816,0)),\n" + 
				"        '同比差',(round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.rucmrz0)/sum(leij.laimsl),2))*1000/4.1816,0)-round(decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.laimsl*tqlj.rucmrz0)/sum(tqlj.laimsl),2))*1000/4.1816,0))\n" + 
				"                 -(round(decode(sum(leij.haoytrm),0,0,round(sum(leij.haoytrm*leij.rulmrz1)/sum(leij.haoytrm),2))*1000/4.1816,0)-round(decode(sum(tqlj.haoytrm),0,0,round(sum(tqlj.haoytrm*tqlj.rulmrz1)/sum(tqlj.haoytrm),2))*1000/4.1816,0))) as rezc\n" + 
				
				"  ,decode(fx.fenx,'本月',decode(sum(beny.laimsl),0,0,round(sum(beny.laimsl*beny.chebj)/sum(beny.laimsl),2)),\n" + 
				"        '累计',decode(sum(leij.laimsl),0,0,round(sum(leij.laimsl*leij.chebj)/sum(leij.laimsl),2)),\n" + 
				"        '环比差',decode(sum(beny.laimsl),0,0,round(sum(beny.laimsl*beny.chebj)/sum(beny.laimsl),2))-decode(sum(sy.laimsl),0,0,round(sum(sy.laimsl*sy.chebj)/sum(sy.laimsl),2)),\n" + 
				"        '同比差',decode(sum(leij.laimsl),0,0,round(sum(leij.laimsl*leij.chebj)/sum(leij.laimsl),2))-decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.laimsl*tqlj.chebj)/sum(tqlj.laimsl),2))) as chebj\n" + 
				"  ,decode(fx.fenx,'本月',decode(sum(beny.laimsl),0,0,round(sum(beny.laimsl*beny.daoczhj)/sum(beny.laimsl),2)),\n" + 
				"        '累计',decode(sum(leij.laimsl),0,0,round(sum(leij.laimsl*leij.daoczhj)/sum(leij.laimsl),2)),\n" + 
				"        '环比差',decode(sum(beny.laimsl),0,0,round(sum(beny.laimsl*beny.daoczhj)/sum(beny.laimsl),2))-decode(sum(sy.laimsl),0,0,round(sum(sy.laimsl*sy.daoczhj)/sum(sy.laimsl),2)),\n" + 
				"        '同比差',decode(sum(leij.laimsl),0,0,round(sum(leij.laimsl*leij.daoczhj)/sum(leij.laimsl),2))-decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.laimsl*tqlj.daoczhj)/sum(tqlj.laimsl),2))) as daoczhj\n" + 
				" ,decode(fx.fenx,'本月',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.daoczhj)/sum(beny.laimsl),2)*29.271/round(sum(beny.laimsl*beny.rucmrz1)/sum(beny.laimsl),2)),2),\n" + 
				"        '累计',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.daoczhj)/sum(leij.laimsl),2)*29.271/round(sum(leij.laimsl*leij.rucmrz1)/sum(leij.laimsl),2)),2),\n" + 
				"        '环比差',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.daoczhj)/sum(beny.laimsl),2)*29.271/round(sum(beny.laimsl*beny.rucmrz1)/sum(beny.laimsl),2)),2)\n" + 
				"                 -round(decode(sum(sy.laimsl),0,0,round(sum(sy.laimsl*sy.daoczhj)/sum(sy.laimsl),2)*29.271/round(sum(sy.laimsl*sy.rucmrz1)/sum(sy.laimsl),2)),2),\n" + 
				"        '同比差',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.daoczhj)/sum(leij.laimsl),2)*29.271/round(sum(leij.laimsl*leij.rucmrz1)/sum(leij.laimsl),2)),2)\n" + 
				"                 -round(decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.laimsl*tqlj.daoczhj)/sum(tqlj.laimsl),2)*29.271/round(sum(tqlj.laimsl*tqlj.rucmrz1)/sum(tqlj.laimsl),2)),2)) as hans_rcbmdj\n" + 
				"  ,decode(fx.fenx,'本月',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.buhs_daoczhj)/sum(beny.laimsl),2)*29.271/round(sum(beny.laimsl*beny.rucmrz1)/sum(beny.laimsl),2)),2),\n" + 
				"        '累计',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.buhs_daoczhj)/sum(leij.laimsl),2)*29.271/round(sum(leij.laimsl*leij.rucmrz1)/sum(leij.laimsl),2)),2),\n" + 
				"        '环比差',round(decode(sum(beny.laimsl*leij.rucmrz1),0,0,round(sum(beny.laimsl*beny.buhs_daoczhj)/sum(beny.laimsl),2)*29.271/round(sum(beny.laimsl*beny.rucmrz1)/sum(beny.laimsl),2)),2)\n" + 
				"                 -round(decode(sum(sy.laimsl),0,0,round(sum(sy.laimsl*sy.buhs_daoczhj)/sum(sy.laimsl),2)*29.271/round(sum(sy.laimsl*sy.rucmrz1)/sum(sy.laimsl),2)),2),\n" + 
				"        '同比差',round(decode(sum(leij.laimsl*leij.rucmrz1),0,0,round(sum(leij.laimsl*leij.buhs_daoczhj)/sum(leij.laimsl),2)*29.271/round(sum(leij.laimsl*leij.rucmrz1)/sum(leij.laimsl),2)),2)\n" + 
				"                 -round(decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.laimsl*tqlj.buhs_daoczhj)/sum(tqlj.laimsl),2)*29.271/round(sum(tqlj.laimsl*tqlj.rucmrz1)/sum(tqlj.laimsl),2)),2)) as buhs_rcbmdj\n" + 
				"  ,decode(fx.fenx,'本月',round(sum(beny.rulbml),0),'累计',round(sum(leij.rulbml),0),'环比差',round(sum(beny.rulbml),0)-round(sum(sy.rulbml),0),'同比差',round(sum(leij.rulbml),0)-round(sum(tqlj.rulbml),0)) as rulbml\n" + 
				"  ,decode(fx.fenx,'本月',round(decode(sum(beny.rulbml),0,0,sum(beny.ranlcb)/sum(beny.rulbml)),2),\n" + 
				"        '累计',round(decode(sum(leij.rulbml),0,0,sum(leij.ranlcb)/sum(leij.rulbml)),2),\n" + 
				"        '环比差',round(decode(sum(beny.rulbml),0,0,sum(beny.ranlcb)/sum(beny.rulbml)),2)-round(decode(sum(sy.rulbml),0,0,sum(sy.ranlcb)/sum(sy.rulbml)),2),\n" + 
				"        '同比差',round(decode(sum(leij.rulbml),0,0,sum(leij.ranlcb)/sum(leij.rulbml)),2)-round(decode(sum(tqlj.rulbml),0,0,sum(tqlj.ranlcb)/sum(tqlj.rulbml)),2)) as rulmzhbmdj\n" + 
				" from\n" + 
				"      ( select dc.id as diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"              (select decode(1,1,'本月') as fenx,1 as xuh  from dual\n" + 
				"               union\n" + 
				"               select decode(1,1,'累计') as fenx,2 as xhu from dual\n" + 
				"               union\n" + 
				"               select decode(1,1,'环比差') as fenx,3 as xuh from dual\n" + 
				"               union\n" + 
				"               select decode(1,1,'同比差') as fenx,4 as xuh from dual\n" + 
				"               ) fx,vwdianc dc\n" + 
				"               "+strGongsID2+
				"                ) fx,\n" + 
				"           (select dc.id as diancxxb_id,\n" + 
				"                   r.laimsl,shc.haoml,shc.shijkc,r.rucmrz0,r.rucmrz1,r.rucmrz,y.rulmrz1,y.rulmrz,y.haoytrm,\n" + 
				"                   round(r.chebj,2) as chebj,round((r.meij+r.zaf+r.yunj+r.daozzf+r.qit+r.jiaohqzf),2) as daoczhj,\n" + 
				"                   round((r.meij+r.zaf+r.jiaohqzf+r.yunj+r.daozzf+r.qit-r.meijs-r.yunjs),4) as buhs_daoczhj,\n" + 
				"                   y.rulbml,y.ranlcb\n" + 
				"             from (select tj.diancxxb_id as diancxxb_id,sum(sl.laimsl) as laimsl,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as rucmrz0,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rucmrz1,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar*1000/4.1816) / sum(sl.laimsl)) as rucmrz\n" + 
				"                        ,decode(sum(sl.laimsl),0,0, sum(y.meij*sl.laimsl)/sum(sl.laimsl)) as chebj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl)) as meij\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl)) as jiaohqzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl)) as yunj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl)) as daozzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl)) as qit\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meijs,0)*sl.laimsl)/sum(sl.laimsl)) as meijs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunjs,0)*sl.laimsl)/sum(sl.laimsl)) as yunjs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl)) as zaf\n" + 
				"                     from yuercbmdj y,yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
				"                     where y.yuetjkjb_id=sl.yuetjkjb_id and  zl.yuetjkjb_id=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id\n" + 
				"                              and sl.fenx = '本月' and zl.fenx=sl.fenx and y.fenx(+)=sl.fenx "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                     group by (tj.diancxxb_id)) r,\n" + 
				"           (select diancxxb_id,sum(zb.rulmzbzml+zb.rulyzbzml+zb.rulqzbzml) as rulbml,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/1000*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz1,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/4.1816*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz,\n" + 
				"                          sum(zb.fadgrytrml) as haoytrm,\n" + 
				"                          sum(zb.ranlcb_bhs*10000) as ranlcb\n" + 
				"                    from yuezbb zb,diancxxb dc\n" + 
				"                    where zb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and zb.fenx='本月'and zb.diancxxb_id=dc.id   "+strGongsID+zhuangt+"\n" + 
				"                    group by (diancxxb_id) )  y,\n" + 
				"                  (select diancxxb_id,sum(yshc.fady+yshc.gongry+yshc.qith) as haoml,sum(yshc.kuc) as shijkc\n" + 
				"                   from yueshchjb yshc,diancxxb dc\n" + 
				"                   where yshc.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and yshc.fenx='本月'and yshc.diancxxb_id=dc.id  "+strGongsID+shczt+"\n" + 
				"                   group by (diancxxb_id) ) shc,\n" + 
				"                   (select decode(d.diancxxb_id,null,shc.diancxxb_id,d.diancxxb_id) as id from\n" + 
				"                        ( select decode(lm.diancxxb_id,null,rl.diancxxb_id,lm.diancxxb_id) as diancxxb_id from\n" + 
				"                              ( select distinct diancxxb_id from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,vwdianc dc\n" + 
				"                                 where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id and sl.yuetjkjb_id=y.yuetjkjb_id  and tj.diancxxb_id=dc.id\n" + 
				"                                  and sl.fenx = '本月' and zl.fenx(+)=sl.fenx and y.fenx(+)=sl.fenx and riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"                                  "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                                group by(diancxxb_id))lm\n" + 
				"                                 FULL JOIN\n" + 
				"                               (select distinct diancxxb_id from yuezbb zb,vwdianc dc\n" + 
				"                                      where  riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and zb.fenx='本月' and zb.diancxxb_id=dc.id "+strGongsID+zhuangt+"\n" + 
				"                                     group by (diancxxb_id))rl\n" + 
				"                               ON lm.diancxxb_id=rl.diancxxb_id )d\n" + 
				"                        FULL JOIN\n" + 
				"                        (select distinct diancxxb_id from yueshchjb yshc,vwdianc dc\n" + 
				"                             where  riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and yshc.fenx='本月' and yshc.diancxxb_id=dc.id "+strGongsID+shczt+"\n" + 
				"                             group by(diancxxb_id))shc\n" + 
				"                        ON d.diancxxb_id =shc.diancxxb_id)dc\n" + 
				"               where dc.id =y.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dc.id=r.diancxxb_id(+)\n" + 
				"          )beny,\n" + 
				"          (select  dc.id as diancxxb_id,\n" + 
				"                   r.laimsl,shc.haoml,shc.shijkc,r.rucmrz0,r.rucmrz1,r.rucmrz,y.rulmrz1,y.rulmrz,y.haoytrm,\n" + 
				"                   round(r.chebj,2) as chebj,round((r.meij+r.zaf+r.yunj+r.daozzf+r.qit+r.jiaohqzf),2) as daoczhj,\n" + 
				"                   round((r.meij+r.zaf+r.jiaohqzf+r.yunj+r.daozzf+r.qit-r.meijs-r.yunjs),4) as buhs_daoczhj,\n" + 
				"                   y.rulbml,y.ranlcb\n" + 
				"             from (select tj.diancxxb_id as diancxxb_id,sum(sl.laimsl) as laimsl,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as rucmrz0,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rucmrz1,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar*1000/4.1816) / sum(sl.laimsl)) as rucmrz\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(y.meij*sl.laimsl)/sum(sl.laimsl)) as chebj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl)) as meij\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl)) as jiaohqzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl)) as yunj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl)) as daozzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl)) as qit\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meijs,0)*sl.laimsl)/sum(sl.laimsl)) as meijs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunjs,0)*sl.laimsl)/sum(sl.laimsl)) as yunjs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl)) as zaf\n" + 
				"                     from yuercbmdj y,yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
				"                     where y.yuetjkjb_id=sl.yuetjkjb_id and  zl.yuetjkjb_id=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id\n" + 
				"                              and sl.fenx = '累计' and zl.fenx=sl.fenx and y.fenx(+)=sl.fenx  "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                     group by (tj.diancxxb_id) ) r,\n" + 
				"           (select diancxxb_id,sum(zb.fadmzbml+zb.gongrmzbml+zb.fadyzbzml+zb.gongryzbzml) as rulbml,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/1000*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz1,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/4.1816*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz,\n" + 
				"                          sum(zb.fadgrytrml) as haoytrm,\n" + 
				"                          sum(zb.ranlcb_bhs*10000) as ranlcb\n" + 
				"                    from yuezbb zb,diancxxb dc\n" + 
				"                    where zb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and zb.fenx='累计'and zb.diancxxb_id=dc.id   "+strGongsID+zhuangt+"\n" + 
				"                    group by (diancxxb_id) )  y,\n" + 
				"                  (select diancxxb_id,sum(yshc.fady+yshc.gongry+yshc.qith) as haoml,sum(yshc.kuc) as shijkc\n" + 
				"                   from yueshchjb yshc,diancxxb dc\n" + 
				"                   where yshc.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and yshc.fenx='累计'and yshc.diancxxb_id=dc.id   "+strGongsID+shczt+"\n" + 
				"                   group by (diancxxb_id) ) shc,\n" + 
				"                   (select decode(d.diancxxb_id,null,shc.diancxxb_id,d.diancxxb_id) as id from\n" + 
				"                        ( select decode(lm.diancxxb_id,null,rl.diancxxb_id,lm.diancxxb_id) as diancxxb_id from\n" + 
				"                              ( select distinct diancxxb_id from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,vwdianc dc\n" + 
				"                                 where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id and sl.yuetjkjb_id=y.yuetjkjb_id  and tj.diancxxb_id=dc.id\n" + 
				"                                  and sl.fenx = '累计' and zl.fenx(+)=sl.fenx and y.fenx(+)=sl.fenx and riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"                               "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                                group by(diancxxb_id))lm\n" + 
				"                                 FULL JOIN\n" + 
				"                               (select distinct diancxxb_id from yuezbb zb,vwdianc dc\n" + 
				"                                      where  riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and zb.fenx='累计' and zb.diancxxb_id=dc.id  "+strGongsID+zhuangt+"\n" + 
				"                                     group by (diancxxb_id))rl\n" + 
				"                               ON lm.diancxxb_id=rl.diancxxb_id )d\n" + 
				"                        FULL JOIN\n" + 
				"                        (select distinct diancxxb_id from yueshchjb yshc,vwdianc dc\n" + 
				"                             where  riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and yshc.fenx='累计' and yshc.diancxxb_id=dc.id "+strGongsID+shczt+"\n" + 
				"                             group by(diancxxb_id))shc\n" + 
				"                        ON d.diancxxb_id =shc.diancxxb_id)dc\n" + 
				"               where dc.id =y.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dc.id=r.diancxxb_id(+)\n" + 
				"         )leij,\n" + 
				"         (select   dc.id as diancxxb_id,\n" + 
				"                   r.laimsl,shc.haoml,shc.shijkc,r.rucmrz0,r.rucmrz1,r.rucmrz,y.rulmrz1,y.rulmrz,y.haoytrm,\n" + 
				"                   round(r.chebj,2) as chebj,round((r.meij+r.zaf+r.yunj+r.daozzf+r.qit+r.jiaohqzf),2) as daoczhj,\n" +
				"                   round((r.meij+r.zaf+r.jiaohqzf+r.yunj+r.daozzf+r.qit-r.meijs-r.yunjs),4) as buhs_daoczhj,\n" + 
				"                   y.rulbml,y.ranlcb\n" + 
				"             from (select tj.diancxxb_id as diancxxb_id,sum(sl.laimsl) as laimsl,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as rucmrz0,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rucmrz1,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar*1000/4.1816) / sum(sl.laimsl)) as rucmrz\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(y.meij*sl.laimsl)/sum(sl.laimsl)) as chebj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl)) as meij\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl)) as jiaohqzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl)) as yunj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl)) as daozzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl)) as qit\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meijs,0)*sl.laimsl)/sum(sl.laimsl)) as meijs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunjs,0)*sl.laimsl)/sum(sl.laimsl)) as yunjs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl)) as zaf\n" + 
				"                     from yuercbmdj y,yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
				"                     where y.yuetjkjb_id=sl.yuetjkjb_id and  zl.yuetjkjb_id=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id\n" + 
				"                           and tj.riq = add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and tj.diancxxb_id = dc.id\n" + 
				"                           and sl.fenx = '本月' and zl.fenx=sl.fenx and y.fenx(+)=sl.fenx  "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                     group by (tj.diancxxb_id) ) r,\n" + 
				"           (select diancxxb_id,sum(zb.fadmzbml+zb.gongrmzbml+zb.fadyzbzml+zb.gongryzbzml) as rulbml,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/1000*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz1,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/4.1816*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz,\n" + 
				"                          sum(zb.fadgrytrml) as haoytrm,\n" + 
				"                          sum(zb.ranlcb_bhs*10000) as ranlcb\n" + 
				"                    from yuezbb zb,diancxxb dc\n" + 
				"                    where zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and zb.fenx='本月'and zb.diancxxb_id=dc.id  "+strGongsID+zhuangt+"\n" + 
				"                    group by (diancxxb_id) )y,\n" + 
				"                    (select diancxxb_id,sum(yshc.fady+yshc.gongry+yshc.qith) as haoml,sum(yshc.kuc) as shijkc\n" + 
				"                   from yueshchjb yshc,diancxxb dc\n" + 
				"                   where yshc.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and yshc.fenx='本月'and yshc.diancxxb_id=dc.id  "+strGongsID+shczt+"\n" + 
				"                   group by (diancxxb_id) ) shc,\n" + 
				"                   (select decode(d.diancxxb_id,null,shc.diancxxb_id,d.diancxxb_id) as id from\n" + 
				"                        ( select decode(lm.diancxxb_id,null,rl.diancxxb_id,lm.diancxxb_id) as diancxxb_id from\n" + 
				"                              ( select distinct diancxxb_id from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,vwdianc dc\n" + 
				"                                 where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id and sl.yuetjkjb_id=y.yuetjkjb_id  and tj.diancxxb_id=dc.id\n" + 
				"                                  and sl.fenx = '本月' and zl.fenx(+)=sl.fenx and y.fenx(+)=sl.fenx and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)\n" + 
				"                                 "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                                group by(diancxxb_id))lm\n" + 
				"                                 FULL JOIN\n" + 
				"                               (select distinct diancxxb_id from yuezbb zb,vwdianc dc\n" + 
				"                                      where  riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)  and zb.fenx='本月' and zb.diancxxb_id=dc.id  "+strGongsID+zhuangt+"\n" + 
				"                                     group by (diancxxb_id))rl\n" + 
				"                               ON lm.diancxxb_id=rl.diancxxb_id )d\n" + 
				"                        FULL JOIN\n" + 
				"                        (select distinct diancxxb_id from yueshchjb yshc,vwdianc dc\n" + 
				"                             where  riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)  and yshc.fenx='本月'  and yshc.diancxxb_id=dc.id "+strGongsID+shczt+"\n" + 
				"                             group by(diancxxb_id))shc\n" + 
				"                        ON d.diancxxb_id =shc.diancxxb_id)dc\n" + 
				"               where dc.id =y.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dc.id=r.diancxxb_id(+)\n" + 
				"         )sy,\n" + 
				"          (select  dc.id as diancxxb_id,\n" + 
				"                   r.laimsl,shc.haoml,shc.shijkc,r.rucmrz0,r.rucmrz1,r.rucmrz,y.rulmrz1,y.rulmrz,y.haoytrm,\n" + 
				"                   round(r.chebj,2) as chebj,round((r.meij+r.zaf+r.yunj+r.daozzf+r.qit+r.jiaohqzf),2) as daoczhj,\n" +
				"                   round((r.meij+r.zaf+r.jiaohqzf+r.yunj+r.daozzf+r.qit-r.meijs-r.yunjs),4) as buhs_daoczhj,\n" + 
				"                   y.rulbml,y.ranlcb\n" + 
				"             from (select tj.diancxxb_id as diancxxb_id,sum(sl.laimsl) as laimsl,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as rucmrz0,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rucmrz1,\n" + 
				"                        decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar*1000/4.1816) / sum(sl.laimsl)) as rucmrz\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(y.meij*sl.laimsl)/sum(sl.laimsl)) as chebj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl)) as meij\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl)) as jiaohqzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl)) as yunj\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl)) as daozzf\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl)) as qit\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.meijs,0)*sl.laimsl)/sum(sl.laimsl)) as meijs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.yunjs,0)*sl.laimsl)/sum(sl.laimsl)) as yunjs\n" + 
				"                        ,decode(sum(sl.laimsl),0,0,sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl)) as zaf\n" + 
				"                     from yuercbmdj y,yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
				"                     where y.yuetjkjb_id=sl.yuetjkjb_id and  zl.yuetjkjb_id=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id\n" + 
				"                              and sl.fenx = '累计' and zl.fenx=sl.fenx and y.fenx(+)=sl.fenx "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                     group by (tj.diancxxb_id)  ) r,\n" + 
				"    (select diancxxb_id,sum(zb.fadmzbml+zb.gongrmzbml+zb.fadyzbzml+zb.gongryzbzml) as rulbml,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/1000*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz1,\n" + 
				"                          decode(sum(zb.fadgrytrml),0,0,sum(zb.rultrmpjfrl/4.1816*zb.fadgrytrml)/sum(zb.fadgrytrml)) as rulmrz,\n" + 
				"                          sum(zb.fadgrytrml) as haoytrm,\n" + 
				"                          sum(zb.ranlcb_bhs*10000) as ranlcb\n" + 
				"                    from yuezbb zb,diancxxb dc\n" + 
				"                    where zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and zb.fenx='累计'and zb.diancxxb_id=dc.id  "+strGongsID+zhuangt+"\n" + 
				"                    group by (diancxxb_id) )  y,\n" + 
				"                  (select diancxxb_id,sum(yshc.fady+yshc.gongry+yshc.qith) as haoml,sum(yshc.kuc) as shijkc\n" + 
				"                   from yueshchjb yshc,diancxxb dc\n" + 
				"                   where yshc.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and yshc.fenx='累计'and yshc.diancxxb_id=dc.id   "+strGongsID+shczt+"\n" + 
				"                   group by (diancxxb_id) ) shc,\n" + 
				"                   (select decode(d.diancxxb_id,null,shc.diancxxb_id,d.diancxxb_id) as id from\n" + 
				"                        ( select decode(lm.diancxxb_id,null,rl.diancxxb_id,lm.diancxxb_id) as diancxxb_id from\n" + 
				"                              ( select distinct diancxxb_id from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,vwdianc dc\n" + 
				"                                 where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id and sl.yuetjkjb_id=y.yuetjkjb_id  and tj.diancxxb_id=dc.id\n" + 
				"                                  and sl.fenx = '累计' and zl.fenx(+)=sl.fenx and y.fenx(+)=sl.fenx and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"                                 "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                                group by(diancxxb_id))lm\n" + 
				"                                 FULL JOIN\n" + 
				"                               (select distinct diancxxb_id from yuezbb zb,vwdianc dc\n" + 
				"                                      where  riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)  and zb.fenx='累计' and zb.diancxxb_id=dc.id  "+strGongsID+zhuangt+"\n" + 
				"                                     group by (diancxxb_id))rl\n" + 
				"                               ON lm.diancxxb_id=rl.diancxxb_id )d\n" + 
				"                        FULL JOIN\n" + 
				"                        (select distinct diancxxb_id from yueshchjb yshc,vwdianc dc\n" + 
				"                             where  riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)  and yshc.fenx='累计'  and yshc.diancxxb_id=dc.id "+strGongsID+shczt+"\n" + 
				"                             group by(diancxxb_id))shc\n" + 
				"                        ON d.diancxxb_id =shc.diancxxb_id)dc\n" + 
				"               where dc.id =y.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dc.id=r.diancxxb_id(+)\n" + 
				"         )tqlj,\n" + 
				"             (select  dc.id as diancxxb_id, shc.shijkc\n" + 
				"             from (select tj.diancxxb_id as diancxxb_id\n" + 
				"                     from yuercbmdj y,yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
				"                     where y.yuetjkjb_id=sl.yuetjkjb_id and  zl.yuetjkjb_id=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id\n" + 
				"                              and sl.fenx = '本月' and zl.fenx=sl.fenx and y.fenx(+)=sl.fenx "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                     group by (tj.diancxxb_id)  ) r,\n" + 
				"                   (select diancxxb_id from yuezbb zb,diancxxb dc\n" + 
				"                    where zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and zb.fenx='本月'and zb.diancxxb_id=dc.id    "+strGongsID+zhuangt+"\n" + 
				"                    group by (diancxxb_id) )  y,\n" + 
				"                  (select diancxxb_id,sum(yshc.kuc) as shijkc\n" + 
				"                   from yueshchjb yshc,diancxxb dc\n" + 
				"                   where yshc.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and yshc.fenx='本月'and yshc.diancxxb_id=dc.id   "+strGongsID+shczt+"\n" + 
				"                   group by (diancxxb_id) ) shc,\n" + 
				"                   (select decode(d.diancxxb_id,null,shc.diancxxb_id,d.diancxxb_id) as id from\n" + 
				"                        ( select decode(lm.diancxxb_id,null,rl.diancxxb_id,lm.diancxxb_id) as diancxxb_id from\n" + 
				"                              ( select distinct diancxxb_id from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,vwdianc dc\n" + 
				"                                 where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id and sl.yuetjkjb_id=y.yuetjkjb_id  and tj.diancxxb_id=dc.id\n" + 
				"                                  and sl.fenx = '本月' and zl.fenx(+)=sl.fenx and y.fenx(+)=sl.fenx and riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"                                 "+strGongsID+shulzt+zhilzt+rucbmdjzt+" \n" + 
				"                                group by(diancxxb_id))lm\n" + 
				"                                 FULL JOIN\n" + 
				"                               (select distinct diancxxb_id from yuezbb zb,vwdianc dc\n" + 
				"                                      where  riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)  and zb.fenx='本月' and zb.diancxxb_id=dc.id  "+strGongsID+zhuangt+"\n" + 
				"                                     group by (diancxxb_id))rl\n" + 
				"                               ON lm.diancxxb_id=rl.diancxxb_id )d\n" + 
				"                        FULL JOIN\n" + 
				"                        (select distinct diancxxb_id from yueshchjb yshc,vwdianc dc\n" + 
				"                             where  riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)  and yshc.fenx='本月' and yshc.diancxxb_id=dc.id "+strGongsID+shczt+"\n" + 
				"                             group by(diancxxb_id))shc\n" + 
				"                        ON d.diancxxb_id =shc.diancxxb_id)dc\n" + 
				"               where dc.id =y.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dc.id=r.diancxxb_id(+)\n" + 
				"         )tqby,  vwdianc dc\n" + 
				" where fx.diancxxb_id =beny.diancxxb_id(+) and fx.diancxxb_id=leij.diancxxb_id(+) and fx.diancxxb_id=sy.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id=tqlj.diancxxb_id(+) and fx.diancxxb_id=tqby.diancxxb_id(+)\n" + 
				"       and dc.id=fx.diancxxb_id\n" + 
				" group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" + 
				" having not grouping(fx.fenx)=1\n" + guolzj+
				" order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
				" grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";

				
//				直属分厂汇总	
				 ArrHeader =new String[2][14];
				 ArrHeader[0]=new String[] {"单位名称","分项","来煤量","耗煤量","月末实际库存","入厂煤热值","入炉煤热值","热值差","车板（平仓）煤价（含税）","到厂综合价 （含税）","入厂标煤单价（含税）","入厂标煤单价（不含税）","入炉标煤量（含油、气）","入炉综合标煤单价"};
				 ArrHeader[1]=new String[] {"甲","乙","1","2","3","4","5","6","7","8","9","10","11","12"};

				 ArrWidth =new int[] {150,60,80,80,80,60,60,60,60,60,60,60,80,60};


				 iFixedRows=1;
				 iCol=10;

			
//			System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
			Table tb=new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"年"+intMonth+"月"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2, "填报单位:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 2, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(7, 3, "单位:吨、大卡/公斤、元/吨", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(13, 2, "cpi燃料月度综合01表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(20);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			//页脚 
			
			  rt.createDefautlFooter(ArrWidth);

			  rt.setDefautlFooter(1, 2, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(3,3,"审核:",Table.ALIGN_CENTER);
			  rt.setDefautlFooter(7,2,"制表:",Table.ALIGN_LEFT);
			  tb.setColAlign(2, Table.ALIGN_CENTER);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT	);

		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
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
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
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
			rs.close();
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
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

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