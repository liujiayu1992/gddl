package com.zhiren.jt.zdt.monthreport.yuedzhbb02report;
/* 
* 时间：2009-07-21
* 作者： ll
* 修改内容：1、修改查询sql中含税标煤单价和不含税标煤单价算法。
* 		   
*/
/* 
* 时间：2009-07-21
* 作者： ll
* 修改内容：1、增加审核状态。
* 		   
*/
/* 
* 时间：2009-07-31
* 作者： ll
* 修改内容：1、Cpi燃料月度综合02表—热值取整
* 		   2、判断电厂id为141(芜湖电厂)，入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段
* 		   3、在查询sql中，增加审核状态条件
*/ 
/* 
* 时间：2009-08-11
* 作者： sy
* 修改内容：rucbmdj_tqlj中热值用的是tq，应该为tqlj
*/ 
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


public class Yuedzhbb02report  extends BasePage implements PageValidateListener{
	
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
			setNianfenValue(null);
			setYuefenValue(null);
			getNianfenModels();
			getYuefenModels();
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
		if(nianfenchanged){
			nianfenchanged=false;
			Refurbish();
		}
		if(yuefenchanged){
			yuefenchanged=false;
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
		String shulzt="";
		String zhilzt="";
		String rucbmdjzt="";
			if(visit.getRenyjb()==3){
				shulzt="";
				zhilzt="";
				rucbmdjzt="";
			}else if(visit.getRenyjb()==2){
				shulzt="";
				zhilzt="";
				rucbmdjzt="";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
				rucbmdjzt=" and (y.zhuangt=1 or y.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				shulzt="";
				zhilzt="";
				rucbmdjzt="";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
				rucbmdjzt=" and  y.zhuangt=2";
			}
//----------------截止日期-----------------			
		long intyear=0;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth=0;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
//-----------------开始日期----------
		long intyear1=0;
		if (getNianfenValue() == null){
			intyear1=DateUtil.getYear(new Date());
		}else{
			intyear1=getNianfenValue().getId();
		}
		long intMonth1=0;
		if(getYuefenValue() == null){
			intMonth1 = DateUtil.getMonth(new Date());
		}else{
			intMonth1 = getYuefenValue().getId();
		}
		String strGongsID = "";
		String strGongsID2 = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			strGongsID2 = "";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			strGongsID2 = "where (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			strGongsID2 = "where dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			strGongsID2 = "where dc.id= " +((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());
	
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="入厂煤价、热值完成情况比较表（分计划口径）";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
		if(intyear1==intyear && intMonth1==intMonth){
			strSQL=
				"select mingc,kouj,laimsl_by,farl_by,farl_tq,farl_by-farl_sy as huanb_rz,farl_leij-farl_tqlj as tongb_rz,\n" +
				"        daoczhj_by,daoczhj_tq,daoczhj_by-daoczhj_sy as huanb_zhj,daoczhj_leij-daoczhj_tqlj as tongb_zhj,\n" + 
				"        rucbmdj_by,rucbmdj_tq,rucbmdj_by-rucbmdj_sy as huanb_bmdj,rucbmdj_leij-rucbmdj_tqlj as tongb_bmdj,\n" + 
				"        decode(rucbmdj_by,0,0,round((rucbmdj_by-rucbmdj_sy)/rucbmdj_by,2)*100) as huanbzf_bmdj,\n" + 
				"        decode(rucbmdj_leij,0,0,round((rucbmdj_leij-rucbmdj_tqlj)/rucbmdj_leij,2)*100) as tongtqzf_bmdj\n" + 
				" from(\n" + 
				" select  decode(grouping(a.fgsmc)+grouping(a.diancmc),2,'集团公司',1,a.fgsmc,a.diancmc) as mingc,\n" + 
				"        decode(a.mingc,null,'合计',a.mingc) as kouj,\n" + 
				"        sum(beny.laimsl) as laimsl_by,\n" + 
				"        round(decode(sum(beny.laimsl),0,0,round(sum(beny.qnet_ar*beny.laimsl)/sum(beny.laimsl),2))*1000/4.1816,0) as farl_by,\n" + 
				"        decode(sum(beny.laimsl),0,0,round(sum((beny.meij+beny.zaf+beny.jiaohqzf+beny.yunj+beny.daozzf+beny.qit)*beny.laimsl)/sum(beny.laimsl),2)) as daoczhj_by,\n" + 
				"		 round(decode(decode(sum(beny.laimsl),0,0,sum(beny.laimsl*beny.qnet_ar)/sum(beny.laimsl)),0,0,\n" +
				"      			round(decode(sum(beny.laimsl),0,0,sum(beny.laimsl*(beny.meij+beny.zaf+beny.jiaohqzf+beny.yunj+beny.daozzf+beny.qit))/sum(beny.laimsl)),2)*29.271/decode(sum(beny.laimsl),0,0,sum(beny.laimsl*beny.qnet_ar)/sum(beny.laimsl))),2) as rucbmdj_by,\n" + 

				"        sum(leij.laimsl) as laimsl_leij,\n" + 
				"        round(decode(sum(leij.laimsl),0,0,round(sum(leij.qnet_ar*leij.laimsl)/sum(leij.laimsl),2))*1000/4.1816,0) as farl_leij,\n" + 
				"        decode(sum(leij.laimsl),0,0,round(sum((leij.meij+leij.zaf+leij.jiaohqzf+leij.yunj+leij.daozzf+leij.qit)*leij.laimsl)/sum(leij.laimsl),2)) as daoczhj_leij,\n" + 
				"		 round(decode(decode(sum(leij.laimsl),0,0,sum(leij.laimsl*leij.qnet_ar)/sum(leij.laimsl)),0,0,\n" +
				"    		   round(decode(sum(leij.laimsl),0,0,sum(leij.laimsl*(leij.meij+leij.zaf+leij.jiaohqzf+leij.yunj+leij.daozzf+leij.qit))/sum(leij.laimsl)),2)*29.271/decode(sum(leij.laimsl),0,0,sum(leij.laimsl*leij.qnet_ar)/sum(leij.laimsl))),2) as rucbmdj_leij,\n" + 
 

				"        sum(sy.laimsl) as laimsl_sy,\n" + 
				"        round(decode(sum(sy.laimsl),0,0,round(sum(sy.qnet_ar*sy.laimsl)/sum(sy.laimsl),2))*1000/4.1816,0) as farl_sy,\n" + 
				"        decode(sum(sy.laimsl),0,0,round(sum((sy.meij+sy.zaf+sy.jiaohqzf+sy.yunj+sy.daozzf+sy.qit)*sy.laimsl)/sum(sy.laimsl),2)) as daoczhj_sy,\n" + 
				"		 round(decode(decode(sum(sy.laimsl),0,0,sum(sy.laimsl*sy.qnet_ar)/sum(sy.laimsl)),0,0,\n" +
				"      		   round(decode(sum(sy.laimsl),0,0,sum(sy.laimsl*(sy.meij+sy.zaf+sy.jiaohqzf+sy.yunj+sy.daozzf+sy.qit))/sum(sy.laimsl)),2)*29.271/decode(sum(sy.laimsl),0,0,sum(sy.laimsl*sy.qnet_ar)/sum(sy.laimsl))),2) as rucbmdj_sy,\n" + 
				"        sum(tq.laimsl) as laimsl_tq,\n" + 
				"        round(decode(sum(tq.laimsl),0,0,round(sum(tq.qnet_ar*tq.laimsl)/sum(tq.laimsl),2))*1000/4.1816,0) as farl_tq,\n" + 
				"        decode(sum(tq.laimsl),0,0,round(sum((tq.meij+tq.zaf+tq.jiaohqzf+tq.yunj+tq.daozzf+tq.qit)*tq.laimsl)/sum(tq.laimsl),2)) as daoczhj_tq,\n" + 
				" 		 round(decode(decode(sum(tq.laimsl),0,0,sum(tq.laimsl*tq.qnet_ar)/sum(tq.laimsl)),0,0,\n" +
				"      		   round(decode(sum(tq.laimsl),0,0,sum(tq.laimsl*(tq.meij+tq.zaf+tq.jiaohqzf+tq.yunj+tq.daozzf+tq.qit))/sum(tq.laimsl)),2)*29.271/decode(sum(tq.laimsl),0,0,sum(tq.laimsl*tq.qnet_ar)/sum(tq.laimsl))),2) as rucbmdj_tq,\n" + 
				"        sum(tqlj.laimsl) as laimsl_tqlj,\n" + 
				"        round(decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.qnet_ar*tqlj.laimsl)/sum(tqlj.laimsl),2))*1000/4.1816,0) as farl_tqlj,\n" + 
				"        decode(sum(tqlj.laimsl),0,0,round(sum((tqlj.meij+tqlj.zaf+tqlj.jiaohqzf+tqlj.yunj+tqlj.daozzf+tqlj.qit)*tqlj.laimsl)/sum(tqlj.laimsl),2)) as daoczhj_tqlj,\n" + 
				"		 round(decode(decode(sum(tqlj.laimsl),0,0,sum(tqlj.laimsl*tqlj.qnet_ar)/sum(tqlj.laimsl)),0,0,\n" +
				"              round(decode(sum(tqlj.laimsl),0,0,sum(tqlj.laimsl*(tqlj.meij+tqlj.zaf+tqlj.jiaohqzf+tqlj.yunj+tqlj.daozzf+tqlj.qit))/sum(tqlj.laimsl)),2)*29.271/decode(sum(tqlj.laimsl),0,0,sum(tqlj.laimsl*tqlj.qnet_ar)/sum(tqlj.laimsl))),2) as rucbmdj_tqlj\n" + 

				"\n" + 
				" from\n" + 
				" (select dc.fgsmc,dc.fgsxh ,dc.id as id,dc.mingc as diancmc,dc.xuh, kj.id as jihkjb_id, decode(kj.id,2,'市场采购','重点订货') as mingc from vwdianc dc,jihkjb kj "+strGongsID2+")a,\n" + 
				" (select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"   from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"   where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"       and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"       and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"    group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")beny,(\n" + 
				"select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" +
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")tq,(\n" + 
				"select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")sy,\n" + 
				"(\n" + 
				"select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='累计' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")leij,\n" + 
				"(select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='累计' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")tqlj\n" + 
				"where a.id=beny.diancxxb_id(+) and a.id=leij.diancxxb_id(+) and a.id = tq.diancxxb_id(+) and a.id=tqlj.diancxxb_id(+) and a.id=sy.diancxxb_id(+)\n" + 
				"and a.jihkjb_id=beny.jihkjb_id(+) and a.jihkjb_id=leij.jihkjb_id(+) and a.jihkjb_id = tq.jihkjb_id(+) and a.jihkjb_id=sy.jihkjb_id(+)\n" + 
				"and a.jihkjb_id=tqlj.jihkjb_id(+)\n" + 
				"\n" + 
				"group by grouping sets ('1',(a.mingc),(a.fgsmc),(a.fgsmc,a.mingc),(a.fgsmc,a.diancmc,a.mingc),(a.fgsmc,a.diancmc))\n" + 
				"order by  grouping('1'),grouping(a.fgsmc) desc,min(a.fgsxh),\n" + 
				"      grouping(a.diancmc) desc,\n" + 
				"      min(a.xuh),grouping(a.mingc) desc\n" + 
				")";

//				直属分厂汇总	
				 ArrHeader =new String[2][17];
				 ArrHeader[0]=new String[] {"单位名称","项目","来煤量（吨）","入厂热值（大卡/公斤）","入厂热值（大卡/公斤）","入厂热值（大卡/公斤）","入厂热值（大卡/公斤）","到厂综合价（元/吨）","到厂综合价（元/吨）","到厂综合价（元/吨）","到厂综合价（元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）"};
				 ArrHeader[1]=new String[] {"单位名称","项目","来煤量（吨）","本期","去年同期","环比升降","同比升降","本期","去年同期","环比升降","同比升降","本期","去年同期","环比升降","环比涨幅","同比升降","同比涨幅"};

				 ArrWidth=new int[] {100,60,80,80,60,60,60,60,60,60,60,60,60,60,60,60,60};

		}else{
			strSQL=
				" select mingc,kouj,laimsl_by,farl_by,farl_tq,farl_leij-farl_tqlj as tongb_rz,\n" +
				"        daoczhj_by,daoczhj_tq,daoczhj_leij-daoczhj_tqlj as tongb_zhj,\n" + 
				"        rucbmdj_by,rucbmdj_tq,rucbmdj_leij-rucbmdj_tqlj as tongb_bmdj,\n" + 
				"        decode(rucbmdj_leij,0,0,round((rucbmdj_leij-rucbmdj_tqlj)/rucbmdj_leij,2)*100) as tongtqzf_bmdj\n" + 
				" from(\n" + 
				" select  decode(grouping(a.fgsmc)+grouping(a.diancmc),2,'集团公司',1,a.fgsmc,a.diancmc) as mingc,\n" + 
				"        decode(a.mingc,null,'合计',a.mingc) as kouj,\n" + 
				"        sum(beny.laimsl) as laimsl_by,\n" + 
				"        round(decode(sum(beny.laimsl),0,0,round(sum(beny.qnet_ar*beny.laimsl)/sum(beny.laimsl),2))*1000/4.1816,0) as farl_by,\n" + 
				"        decode(sum(beny.laimsl),0,0,round(sum((beny.meij+beny.zaf+beny.jiaohqzf+beny.yunj+beny.daozzf+beny.qit)*beny.laimsl)/sum(beny.laimsl),2)) as daoczhj_by,\n" + 

				"		round(decode(decode(sum(beny.laimsl),0,0,sum(beny.laimsl*beny.qnet_ar)/sum(beny.laimsl)),0,0,\n" +
				"      		   round(decode(sum(beny.laimsl),0,0,sum(beny.laimsl*(beny.meij+beny.zaf+beny.jiaohqzf+beny.yunj+beny.daozzf+beny.qit))/sum(beny.laimsl)),2)*29.271/decode(sum(beny.laimsl),0,0,sum(beny.laimsl*beny.qnet_ar)/sum(beny.laimsl))),2) as rucbmdj_by,\n" + 


				
				"        sum(leij.laimsl) as laimsl_leij,\n" + 
				"        round(decode(sum(leij.laimsl),0,0,round(sum(leij.qnet_ar*leij.laimsl)/sum(leij.laimsl),2))*1000/4.1816,0) as farl_leij,\n" + 
				"        decode(sum(leij.laimsl),0,0,round(sum((leij.meij+leij.zaf+leij.jiaohqzf+leij.yunj+leij.daozzf+leij.qit)*leij.laimsl)/sum(leij.laimsl),2)) as daoczhj_leij,\n" + 
				

				"		round(decode(decode(sum(leij.laimsl),0,0,sum(leij.laimsl*leij.qnet_ar)/sum(leij.laimsl)),0,0,\n" +
				"              round(decode(sum(leij.laimsl),0,0,sum(leij.laimsl*(leij.meij+leij.zaf+leij.jiaohqzf+leij.yunj+leij.daozzf+leij.qit))/sum(leij.laimsl)),2)*29.271/decode(sum(leij.laimsl),0,0,sum(leij.laimsl*leij.qnet_ar)/sum(leij.laimsl))),2) as rucbmdj_leij,\n"+

				
				"        sum(tq.laimsl) as laimsl_tq,\n" + 
				"        round(decode(sum(tq.laimsl),0,0,round(sum(tq.qnet_ar*tq.laimsl)/sum(tq.laimsl),2))*1000/4.1816,0) as farl_tq,\n" + 
				"        decode(sum(tq.laimsl),0,0,round(sum((tq.meij+tq.zaf+tq.jiaohqzf+tq.yunj+tq.daozzf+tq.qit)*tq.laimsl)/sum(tq.laimsl),2)) as daoczhj_tq,\n" + 

				"		round(decode(decode(sum(tq.laimsl),0,0,sum(tq.laimsl*tq.qnet_ar)/sum(tq.laimsl)),0,0,\n" +
				"              round(decode(sum(tq.laimsl),0,0,sum(tq.laimsl*(tq.meij+tq.zaf+tq.jiaohqzf+tq.yunj+tq.daozzf+tq.qit))/sum(tq.laimsl)),2)*29.271/decode(sum(tq.laimsl),0,0,sum(tq.laimsl*tq.qnet_ar)/sum(tq.laimsl))),2) as rucbmdj_tq,\n" + 
				
				"        sum(tqlj.laimsl) as laimsl_tqlj,\n" + 
				"        round(decode(sum(tqlj.laimsl),0,0,round(sum(tqlj.qnet_ar*tqlj.laimsl)/sum(tqlj.laimsl),2))*1000/4.1816,0) as farl_tqlj,\n" + 
				"        decode(sum(tqlj.laimsl),0,0,round(sum((tqlj.meij+tqlj.zaf+tqlj.jiaohqzf+tqlj.yunj+tqlj.daozzf+tqlj.qit)*tqlj.laimsl)/sum(tqlj.laimsl),2)) as daoczhj_tqlj,\n" + 

				"		round(decode(decode(sum(tqlj.laimsl),0,0,sum(tqlj.laimsl*tqlj.qnet_ar)/sum(tqlj.laimsl)),0,0,\n" +
				"             round(decode(sum(tqlj.laimsl),0,0,sum(tqlj.laimsl*(tqlj.meij+tqlj.zaf+tqlj.jiaohqzf+tqlj.yunj+tqlj.daozzf+tqlj.qit))/sum(tqlj.laimsl)),2)*29.271/decode(sum(tqlj.laimsl),0,0,sum(tqlj.laimsl*tqlj.qnet_ar)/sum(tqlj.laimsl))),2) as rucbmdj_tqlj \n"+

				
				"\n" + 
				" from\n" + 
				" (select dc.fgsmc,dc.fgsxh ,dc.id as id,dc.mingc as diancmc,dc.xuh, kj.id as jihkjb_id, decode(kj.id,2,'市场采购','重点订货') as mingc\n" + 
				"  from vwdianc dc,jihkjb kj  "+strGongsID2+")a,\n" + 
				" (select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"   from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"   where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"       and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"       and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"    group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")beny,(\n" + 
				"select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")tq,\n" + 
				"(\n" + 
				"select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq>=to_date('"+intyear1+"-"+intMonth1+"-01','yyyy-mm-dd')\n" + 
				"      and tj.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")leij,\n" + 
				"(select tj.diancxxb_id,\n" + 
				"       decode(tj.jihkjb_id,2,2,1) as jihkjb_id,\n" + 
				"       sum(sl.laimsl) as laimsl,\n" + 
				"       decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * decode(dc.id,141,zl.diancrz,zl.qnet_ar)) / sum(sl.laimsl)) as qnet_ar0,\n" +
				"       decode(sum(sl.laimsl),0,0,sum(zl.qnet_ar*sl.laimsl)/sum(sl.laimsl)) as qnet_ar,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit,\n" + 
				"       decode(sum(sl.laimsl),0,0,round(sum(nvl(y.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
				"from yuercbmdj y,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc\n" + 
				"where y.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id\n" + 
				"      and sl.fenx='本月' and sl.fenx=zl.fenx(+) and sl.fenx=y.fenx(+)\n" + 
				"      and tj.riq>=add_months(to_date('"+intyear1+"-"+intMonth1+"-01','yyyy-mm-dd'),-12)\n" + 
				"      and tj.riq<=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) "+strGongsID+shulzt+zhilzt+rucbmdjzt+"\n" + 
				"group by (tj.diancxxb_id,tj.jihkjb_id)\n" + 
				")tqlj\n" + 
				"where a.id=beny.diancxxb_id(+) and a.id=leij.diancxxb_id(+) and a.id = tq.diancxxb_id(+) and a.id=tqlj.diancxxb_id(+)\n" + 
				"and a.jihkjb_id=beny.jihkjb_id(+) and a.jihkjb_id=leij.jihkjb_id(+) and a.jihkjb_id = tq.jihkjb_id(+)\n" + 
				"and a.jihkjb_id=tqlj.jihkjb_id(+)\n" + 
				"\n" + 
				"group by grouping sets ('1',(a.mingc),(a.fgsmc),(a.fgsmc,a.mingc),(a.fgsmc,a.diancmc,a.mingc),(a.fgsmc,a.diancmc))\n" + 
				"order by  grouping('1'),grouping(a.fgsmc) desc,min(a.fgsxh),\n" + 
				"      grouping(a.diancmc) desc,\n" + 
				"      min(a.xuh),a.diancmc,grouping(a.mingc) desc,a.mingc\n" + 
				")";
//			直属分厂汇总	
			 ArrHeader =new String[2][13];
			 ArrHeader[0]=new String[] {"单位名称","项目","来煤量（吨）","入厂热值（大卡/公斤）","入厂热值（大卡/公斤）","入厂热值（大卡/公斤）","到厂综合价（元/吨）","到厂综合价（元/吨）","到厂综合价（元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）","入厂标煤单价（含税，元/吨）"};
			 ArrHeader[1]=new String[] {"单位名称","项目","来煤量（吨）","本期","去年同期","同比升降","本期","去年同期","同比升降","本期","去年同期","同比升降","同比涨幅"};

			 ArrWidth =new int[] {100,60,80,60,60,60,60,60,60,60,60,60,60};

		}
				 iFixedRows=1;
				 iCol=10;
			
//			System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
			Table tb=new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"年"+intMonth+"月"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 3, "填报单位:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(7, 3, "单位:吨、大卡/公斤、元/吨", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(12, 2, "cpi燃料月度综合02表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			//页脚 
			
			  rt.createDefautlFooter(ArrWidth);
			/*  rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(4,1,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,1,"审核:",Table.ALIGN_LEFT);*/
			  rt.setDefautlFooter(1, 2, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(3,3,"审核:",Table.ALIGN_CENTER);
			  rt.setDefautlFooter(7,2,"制表:",Table.ALIGN_LEFT);
			  tb.setColAlign(2, Table.ALIGN_CENTER);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT	);
//			设置页数
//			 rt.createDefautlFooter(ArrWidth);
//			 rt.setDefautlFooter(1,2,"审核人:",Table.ALIGN_LEFT);
//			 rt.setDefautlFooter(4,3,"填报人:",Table.ALIGN_LEFT);
//			 rt.setDefautlFooter(8,2,"联系电话:",Table.ALIGN_RIGHT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
//			System.out.println(rt.getAllPagesHtml());
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
	
//	----------截止时间---------------------------
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
//----------开始时间---------------------------
//	年份
	private static IPropertySelectionModel _NianfenModel;

	public IPropertySelectionModel getNianfenModel() {
		if (_NianfenModel == null) {
			getNianfenModels();
		}
		return _NianfenModel;
	}
 
	private IDropDownBean _NianfenValue;

	public IDropDownBean getNianfenValue() {
		if (_NianfenValue == null) {
			int _nianfen = DateUtil.getYear(new Date());
            int _yuefen = DateUtil.getMonth(new Date());
            if (_yuefen == 1) {
                _nianfen = _nianfen - 1;
            }
			for (int i = 0; i < _NianfenModel.getOptionCount(); i++) {
				Object obj = _NianfenModel.getOption(i);
				if (_nianfen == ((IDropDownBean) obj).getId()) {
					_NianfenValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfenValue;
	}
	public boolean nianfenchanged = false;
	public void setNianfenValue(IDropDownBean Value) {
		if (_NianfenValue != Value) {
			nianfenchanged = true;
		}
		_NianfenValue = Value;
	}

	public IPropertySelectionModel getNianfenModels() {
		List listNianfen = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianfen.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfenModel = new IDropDownModel(listNianfen);
		return _NianfenModel;
	}

	public void setNianfenModel(IPropertySelectionModel _value) {
		_NianfenModel = _value;
	}
	
	/**
	 * 月份
	 */
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
	            _yuefen = _yuefen-_yuefen + 1;
	        }
			for (int i = 0; i < _YuefenModel.getOptionCount(); i++) {
				Object obj = _YuefenModel.getOption(i);
				if (_yuefen == ((IDropDownBean) obj).getId()) {
					_YuefenValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefenValue;
	}
	public boolean yuefenchanged = false;
	public void setYuefenValue(IDropDownBean Value) {
		if (_YuefenValue != Value) {
			yuefenchanged = true;
		}
		_YuefenValue = Value;
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
//--------------------------------------------
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
		
//	    ******开始日期
		
		ComboBox nianfen = new ComboBox();
		nianfen.setTransform("NIANFEN");
		nianfen.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianfen);
		tb1.addText(new ToolbarText("年:"));
		tb1.addText(new ToolbarText("-"));
		
		
		ComboBox yuefen = new ComboBox();
		yuefen.setTransform("YUEFEN");
		yuefen.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuefen);
		tb1.addText(new ToolbarText("月"));
		tb1.addText(new ToolbarText("-"));
//		******截止日期
		tb1.addText(new ToolbarText("至:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("年"));
		tb1.addText(new ToolbarText("-"));
		
		
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("月"));
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