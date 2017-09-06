package com.zhiren.jt.zdt.monthreport.yuecgjhreport;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;
/* 
* 时间：2009-06-15
* 作者： ll
* 修改内容：标煤单价应为不含税,增加不含税标煤单价。
* 		   
*/
/* 
* 时间：2009-06-26
* 作者： sy
* 修改内容：区域口径的内容也要求统计进来
* 		   
*/


/*
 *  sy
 * 2009-08-26
 * 
 * 修改电厂排序，按xuh排序
 *
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-28 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：赵胜男
 * 时间：2012-04-24
 * 描述：删除累计到货率列。
 *       计划采购数量分本期、累计，增加累计列。
 */
public class Yuecgjhreport  extends BasePage implements PageValidateListener{
	
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
//	 判断是否是公司用户
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
//	 判断是否是电厂用户
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	//开始日期
	private Date _BeginriqValue = new Date();
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
		} else {
			_BeginriqValue = _value;
		}
	}
	//消息框
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
	
	//按钮事件－“刷新”
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
	
	//页面初始化刷新事件
	private void Refurbish() {
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

	/**
	 * 页面开始时初始化方法
	 */
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
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
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
	
	/*
	 * 自定义报表
	 * @RT_HET=yunsjhcx
	 */
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
	
	/**
	 * 查询数据
	 * @author xzy
	 * @return
	 */
	private String getSelectData(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		//判断用户进入系统后数据的初始状态
		String zhuangt="";
		if(visit.getRenyjb()==3){
			zhuangt="";
		}else if(visit.getRenyjb()==2){
			zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
		}else if(visit.getRenyjb()==1){
			zhuangt=" and sl.zhuangt=2";
		}
		//得到时间条件的值
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
//		 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		visit.setString3(""+intyear+"-"+StrMonth);//年份

		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] =null;
		String titlename="煤炭采购计划表";
		//报表内容
		
		//获得cg
		StringBuffer str_cg = new StringBuffer();
		str_cg.append("(select y.diancxxb_id,y.gongysb_id,y.jihkjb_id ,\n");
		str_cg.append("max(y.faz_id) as faz_id,max(y.daoz_id) as daoz_id,sum(y.yuejhcgl) as cgl,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as chebj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n");//挥发分
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n");//硫分
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.yunf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as yunf,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.zaf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as zaf,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl),2)) as rez,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.biaomdj*y.yuejhcgl)/sum(y.yuejhcgl),2)) as biaomdj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl),2)) as daocj,\n");
		str_cg.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as kouj,\n");
		str_cg.append("max(y.jiakk) as jiakk,max(y.jihddsjysl) as sls \n");
		str_cg.append("from yuecgjhb y where y.riq=to_date('"+ intyear+ "-"+ intMonth+ "-01','yyyy-mm-dd') \n");
		str_cg.append("group by (y.diancxxb_id,y.jihkjb_id,y.gongysb_id)) cg,\n");
		str_cg.append(
				"(select y.diancxxb_id,y.gongysb_id,y.jihkjb_id ,\n" +
				"max(y.faz_id) as faz_id,max(y.daoz_id) as daoz_id,sum(y.yuejhcgl) as cgl,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as chebj,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.yunf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as yunf,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.zaf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as zaf,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl),2)) as rez,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.biaomdj*y.yuejhcgl)/sum(y.yuejhcgl),2)) as biaomdj,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl),2)) as daocj,\n" + 
				"decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as kouj,\n" + 
				"max(y.jiakk) as jiakk,max(y.jihddsjysl) as sls\n" + 
				"from yuecgjhb y where y.riq BETWEEN to_date('"+intyear+"-01-01','yyyy-mm-dd') AND to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"group by (y.diancxxb_id,y.jihkjb_id,y.gongysb_id)) ljcg,");
		//获得zj
		StringBuffer str_zj = new StringBuffer();
		str_zj.append("(select n.diancxxb_id,n.gongysb_id,n.jihkjb_id ,sum(n.hej) as nianhj from niandhtqkb n\n");
		str_zj.append("where to_char(n.riq,'yyyy')='"+intyear+"' group by (n.diancxxb_id,n.jihkjb_id,n.gongysb_id)) zj,\n");
		//获得shid
		StringBuffer str_shid = new StringBuffer();
		str_shid.append("( select kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id,sum(sl.laimsl) as shijlm\n");
		str_shid.append(" from yueslb sl,yuetjkjb kj where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and\n");
		str_shid.append(" sl.fenx='累计' and sl.yuetjkjb_id=kj.id "+zhuangt+"\n");
		str_shid.append("group by (kj.diancxxb_id,kj.jihkjb_id,kj.gongysb_id)) shid,\n");
		//获得jh
		StringBuffer str_jh = new StringBuffer();
		str_jh.append("(select n.diancxxb_id,n.gongysb_id,n.jihkjb_id,sum(n.hej) as leijjh\n");
		str_jh.append(" from niandhtqkb n where n.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and\n");
		str_jh.append(" n.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n");
		str_jh.append(" group by (n.diancxxb_id,n.jihkjb_id,n.gongysb_id)) jh,\n");
		
		/**
		 * 设置报表类型下拉框的查询条件
		 * 类型：分厂分矿，分矿分厂，分厂，分矿
		 * 自定义的字符串名：dianwmc (根据不同的报表类型来设置分组字段)
		 * *********************************************************
		 * 根据用户的级别来查询数据的SQL
		 * jib: "1" 集团级，"2" 分公司或燃料公司级 "3" 电厂级
		 */
		String dianwmc="";
		String groupby="";
		String orderby="";
		String havingnot="";
		if (getBaoblxValue()!=null){
			if(getBaoblxValue().getValue().equals("分厂分矿")){
				titlename=titlename+"(分厂分矿)";
				if(jib==1){//集团用户
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '总计' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  +"decode(grouping(dc.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
						  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else\n"
						  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  end 供煤单位,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
						  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";

				}else if (jib==2){//分公司及燃料公司
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//燃料公司
							dianwmc=
								  /*"case when grouping(vdc.rlgsmc)=0 then vdc.rlgsmc when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc \n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								 " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
								  +"decode(grouping(dc.mingc)+grouping(vdc.rlgsmc),2,'总计',1,vdc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
								  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else\n"
								  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  end 供煤单位,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  grouping sets (j.mingc,vdc.rlgsmc,(vdc.rlgsmc,j.mingc),\n"
								  + "(vdc.rlgsmc,dc.mingc),(vdc.rlgsmc,dc.mingc,j.mingc),(vdc.rlgsmc,dc.mingc,j.mingc,g.mingc))\n";
							orderby="order by grouping(vdc.rlgsmc) desc,min(vdc.rlgsxh),vdc.rlgsmc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc ";
						}else{//分公司
							dianwmc=
								  /*"case when grouping(f.mingc)=1 then '总计' when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								  +" decode(grouping(f.mingc)+grouping(dc.mingc),2,'总计',"
								  +" 1,f.mingc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n"
								  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else\n"
								  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  end 供煤单位,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
								  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
							havingnot="having not grouping(f.mingc)=1\n";
							orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//电厂
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '总计' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  + "decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danw,\n"
						  + "case when grouping(g.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc else\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else\n"
						  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  end 供煤单位,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n"
						  + "(f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc),(f.mingc,dc.mingc,j.mingc,g.mingc))\n";
					havingnot="having not grouping(dc.mingc)=1\n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc,grouping(g.mingc) desc ,g.mingc";
				}
			}else if (getBaoblxValue().getValue().equals("分矿分厂")){
				titlename=titlename+"(分矿分厂)";
				if(jib==1){//集团用户
					dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
						  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'总计',2,'合计',1,'小计','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
				}else if (jib==2){//分公司及燃料公司
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//燃料公司
							dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
								  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'总计',2,'合计',1,'小计','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc";
						}else{//分公司
							dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
								  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'总计',2,'合计',1,'小计','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//电厂
					dianwmc="case when grouping(g.mingc)=1 then j.mingc else '&nbsp;&nbsp;'||g.mingc end as danw,\n"
						  + "decode(grouping(j.mingc)+grouping(g.mingc)+grouping(dc.mingc),3,'总计',2,'合计',1,'小计','&nbsp;&nbsp;'||dc.mingc) as mingc,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc)) as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc,dc.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc,grouping(dc.mingc) desc ,dc.mingc desc,max(dc.xuh)";
				}
			}else if (getBaoblxValue().getValue().equals("分厂")){
				titlename=titlename+"(分厂)";
				if(jib==1){//集团用户
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '总计' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  " getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  +"decode(grouping(dc.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
						  + "case when grouping(j.mingc)=0 then j.mingc else \n"
						  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  供煤单位,\n"
						  + "decode(grouping(f.mingc)+grouping(j.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(f.mingc)+grouping(j.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
				}else if (jib==2){//分公司及燃料公司
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//燃料公司
							dianwmc=
								  /*"case when grouping(vdc.rlgsmc)=1 then '总计' when grouping(j.mingc)=0 and \n"
								  + "grouping(vdc.rlgsmc)=1  then j.mingc when grouping(vdc.rlgsmc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||vdc.rlgsmc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								" getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
								  +"decode(grouping(dc.mingc)+grouping(vdc.rlgsmc),2,'总计',1,vdc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,"
								  + "case when grouping(j.mingc)=0 then j.mingc else \n"
								  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  供煤单位,\n"
								  + "decode(grouping(vdc.rlgsmc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(vdc.rlgsmc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  grouping sets (j.mingc,vdc.rlgsmc,(vdc.rlgsmc,j.mingc),\n (vdc.rlgsmc,dc.mingc),(vdc.rlgsmc,dc.mingc,j.mingc))\n";
							orderby="order by grouping(vdc.rlgsmc) desc,min(vdc.rlgsxh),vdc.rlgsmc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
						}else{//分公司
							dianwmc=
								  /*"case when grouping(f.mingc)=1 then '总计' when grouping(j.mingc)=0 and \n"
								  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
								  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
								  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								  +" decode(grouping(f.mingc)+grouping(dc.mingc),2,'总计',"
								  +" 1,f.mingc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n"
								  + "case when grouping(j.mingc)=0 then j.mingc else \n"
								  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  供煤单位,\n"
								  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
							havingnot="having not grouping(f.mingc)=1\n";
							orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//电厂
					dianwmc=
						  /*"case when grouping(f.mingc)=1 then '总计' when grouping(j.mingc)=0 and \n"
						  + "grouping(f.mingc)=1  then j.mingc when grouping(f.mingc)=0  and grouping(dc.mingc)=1 then '&nbsp;&nbsp;'||f.mingc\n"
						  + "else '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc end as danw,\n"*/
						  "  getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id))," 
						  + "decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danw,\n"
						  + "case when grouping(j.mingc)=0 then j.mingc else \n"
						  + "case when grouping(dc.mingc)=0 then '小计' else '合计' end end  供煤单位,\n"
						  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(f.mingc)+grouping(dc.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  grouping sets (j.mingc,f.mingc,(f.mingc,j.mingc),\n (f.mingc,dc.mingc),(f.mingc,dc.mingc,j.mingc))\n";
					havingnot="having not grouping(dc.mingc)=1 \n";
					orderby="order by grouping(f.mingc) desc,min(f.xuh),f.mingc,grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,grouping(j.mingc) desc,min(j.xuh),j.mingc";
				}
			}else if (getBaoblxValue().getValue().equals("分矿")){
				titlename=titlename+"(分矿)";
				if(jib==1){//集团用户
					dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'总计',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
				}else if (jib==2){//分公司及燃料公司
					String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
					try{
						ResultSet rl = cn.getResultSet(ranlgs);
						if(rl.next()){//燃料公司
							dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'总计',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
						}else{//分公司
							dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'总计',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
								  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
								  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
							groupby="group by  rollup (j.mingc,g.mingc)\n";
							orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
						}
						rl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						cn.Close();
					}
				}else {//电厂
					dianwmc="decode(grouping(g.mingc)+grouping(j.mingc),2,'总计',1,j.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.mingc) as danw,\n"
						  + "decode(grouping(g.mingc),1,'',max(c1.mingc)) as faz,\n"
						  + "decode(grouping(g.mingc),1,'',max(c2.mingc))  as daoz,\n";
					groupby="group by  rollup (j.mingc,g.mingc)\n";
					orderby="order by grouping(j.mingc) desc ,j.mingc desc,grouping(g.mingc) desc ,g.mingc desc";
				}
			}
		}else{
			return "";
		}
		
		//*********************************************主SQL*********************************************//
		strSQL.append(" select \n");
		//自定义表报数据
		strSQL.append(dianwmc);
		//数据默认数据
		strSQL.append( " sum(zj.nianhj)*10000 as zj,\n");
//		strSQL.append( " decode(sum(jh.leijjh),0,0,Round(sum(shid.shijlm)*100/(sum(jh.leijjh)*10000),2)) as daohl,\n");
		strSQL.append( " sum(cg.cgl) as cgl,\n");
		strSQL.append( " sum(ljcg.cgl) as ljcgl,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.rez*cg.cgl)/sum(cg.cgl),2)) as rez,\n");//热值
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.huiff*cg.cgl)/sum(cg.cgl),2)) as huiff,\n");//挥发分
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.liuf*cg.cgl)/sum(cg.cgl),2)) as liuf,\n");//硫分
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.chebj*cg.cgl)/sum(cg.cgl),2)) as chebj,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.yunf*cg.cgl)/sum(cg.cgl),2)) as yunf,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum(cg.zaf*cg.cgl)/sum(cg.cgl),2)) as zaf,\n");
		strSQL.append( " decode(sum(cg.cgl),0,0,Round(sum((cg.chebj+cg.yunf+cg.zaf)*cg.cgl)/sum(cg.cgl),2)) as daocj,\n");
		//strSQL.append( "decode(sum(cg.cgl),0,0,Round(sum(cg.biaomdj*cg.cgl)/sum(cg.cgl),2)) as biaomdj\n);"
		strSQL.append( " Round(decode(sum(cg.rez*cg.cgl),0,0,Round((sum((cg.chebj+cg.yunf+cg.zaf)*cg.cgl)/sum(cg.cgl)),2)*29.271/");
		strSQL.append( " Round((sum(cg.rez*cg.cgl)/sum(cg.cgl)),2)),2) as biaomdj \n");
        //不含税标煤单价
		strSQL.append( " ,Round(decode(sum(cg.rez*cg.cgl),0,0,Round((sum((cg.chebj/(1+0.17)+cg.yunf*(1-0.07)+cg.zaf)*cg.cgl)/sum(cg.cgl)),2)*29.271/");
		strSQL.append( " Round((sum(cg.rez*cg.cgl)/sum(cg.cgl)),2)),2) as buhs_biaomdj \n");
		//获得所需的表
		strSQL.append( " from \n ");
		strSQL.append(str_cg.toString()).append(str_zj.toString()).append(str_shid.toString()).append(str_jh.toString());
		//设置：where,group by ,order by 
		strSQL.append( " diancxxb dc,gongysb g,jihkjb j,chezxxb c1,chezxxb c2,vwfengs f,vwdianc vdc\n");
		strSQL.append( " where cg.diancxxb_id=zj.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=shid.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=jh.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=ljcg.diancxxb_id(+)\n");
		strSQL.append( " and   cg.diancxxb_id=dc.id\n");
		strSQL.append( " and   cg.gongysb_id=zj.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=shid.diancxxb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=jh.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=ljcg.gongysb_id(+)\n");
		strSQL.append( " and   cg.gongysb_id=g.id\n");
		strSQL.append( " and   cg.jihkjb_id=zj.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=shid.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=jh.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=ljcg.jihkjb_id(+)\n");
		strSQL.append( " and   cg.jihkjb_id=j.id\n");
		strSQL.append( " and   cg.faz_id=c1.id\n");
		strSQL.append( " and   cg.daoz_id=c2.id\n");
		strSQL.append( " and   dc.id=vdc.id \n");
		strSQL.append( "  "+strGongsID+"\n");
		strSQL.append( " and   dc.fuid=f.id(+)\n");
	    /*strSQL.append( " and   j.id!=3\n");*/	
		strSQL.append(groupby);
		strSQL.append(havingnot);
		strSQL.append(orderby);
		
		//设置报表的表头列
		if(getBaoblxValue().getValue().equals("分厂分矿")){
			ArrHeader=new String[2][12];
			ArrHeader[0]=new String[]{"单位","供应单位","发站(港)","到站(港)","年度订<br>货量","计划采<br>购数量","计划采<br>购数量","质量","质量","质量","价格","价格","价格","价格","价格","价格"};
		    ArrHeader[1]=new String[] {"单位","供应单位","发站(港)","到站(港)","年度订<br>货量","本月","累计","热值","挥发分","硫分","煤价","运费","杂费","到厂价","标煤单价","不含税标煤单价"};
		    ArrWidth =new int[] {150,80,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0","0", "0","0.00", "0.00", "0.00","0.00", "0.00", "0.00","0.00", "0.00","0.00"};
		}else if(getBaoblxValue().getValue().equals("分矿分厂")){
			ArrHeader=new String[2][12];
			ArrHeader[0]=new String[]{"供应单位","单位","发站(港)","到站(港)","年度订<br>货量","计划采<br>购数量","计划采<br>购数量","质量","质量","质量","价格","价格","价格","价格","价格","价格"};
		    ArrHeader[1]=new String[] {"供应单位","单位","发站(港)","到站(港)","年度订<br>货量","本月","累计","热值","挥发分","硫分","煤价","运费","杂费","到厂价","标煤单价","不含税标煤单价"};
		    ArrWidth =new int[] {150,80,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0","0", "0","0.00","0.00", "0.00","0.00","0.00", "0.00","0.00", "0.00","0.00"};
		}else if(getBaoblxValue().getValue().equals("分矿")){
			ArrHeader=new String[2][11];
			ArrHeader[0]=new String[]{"供应单位","发站(港)","到站(港)","年度订<br>货量","计划采<br>购数量","计划采<br>购数量","质量","质量","质量","价格","价格","价格","价格","价格","价格"};
		    ArrHeader[1]=new String[] {"供应单位","发站(港)","到站(港)","年度订<br>货量","本月","累计","热值","挥发分","硫分","煤价","运费","杂费","到厂价","标煤单价","不含税标煤单价"};
		    ArrWidth =new int[] {150,80,60,60,60,60,60,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0","0", "0","0.00", "0.00","0.00","0.00", "0.00","0.00","0.00", "0.00"};
		}else if(getBaoblxValue().getValue().equals("分厂")){
			ArrHeader=new String[2][12];
			ArrHeader[0]=new String[]{"单位","计划口径","发站(港)","到站(港)","年度订<br>货量","计划采<br>购数量","计划采<br>购数量","质量","质量","质量","价格","价格","价格","价格","价格","价格"};
		    ArrHeader[1]=new String[] {"单位","计划口径","发站(港)","到站(港)","年度订<br>货量","本月","累计","热值","挥发分","硫分","煤价","运费","杂费","到厂价","标煤单价","不含税标煤单价"};
		    ArrWidth =new int[] {150,80,60,60,60,60,60,50,50,50,50,50,50,50,50,50};
		    arrFormat = new String[] { "", "","","", "0", "0","0","0.00", "0.00", "0.00","0.00", "0.00", "0.00","0.00", "0.00","0.00"};
		}
//		System.out.println(strSQL.toString());
		ResultSet rs = cn.getResultSet(strSQL.toString());
		// 数据
		rt.setBody(new Table(rs,2, 0, 4));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_CENTER);
		if(getBaoblxValue().getValue().equals("分矿")){
			rt.setDefaultTitle(9,3, "单位:吨、元/吨、MJ/Kg", Table.ALIGN_RIGHT);
		}else{
			rt.setDefaultTitle(10,3, "单位:吨、元/吨、MJ/Kg", Table.ALIGN_RIGHT);
		}
			
		
		
		rt.body.setWidth(ArrWidth);
//		rt.body.setPageRows(18);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		if(jib==3){
			 rt.body.setColAlign(2, Table.ALIGN_LEFT);
		     rt.body.setColAlign(3, Table.ALIGN_LEFT);
		}
		if(jib==2){
			if(getBaoblxValue().getValue().equals("分厂分矿")){
				try {
					if (rs.getRow()!=0){
						rt.body.setCellValue(3, 2, "总计");
					}
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
			}
			rt.body.setColAlign(2, Table.ALIGN_LEFT);
		    rt.body.setColAlign(3, Table.ALIGN_LEFT);
		    rt.body.setColAlign(4, Table.ALIGN_LEFT);
		}
		rt.body.setColFormat(arrFormat);
		//页脚 
		
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(7,2,"审核:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(11,2,"制表:",Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
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

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}*/
	
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
	//条件工具
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

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
		
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
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
	//电厂树
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
//	 报表类型
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
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
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "分厂"));
			fahdwList.add(new IDropDownBean(1, "分矿"));
			fahdwList.add(new IDropDownBean(2, "分厂分矿"));
			fahdwList.add(new IDropDownBean(3, "分矿分厂"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
	}

	
}