package com.zhiren.jt.zdt.chengbgl.rucmcbtj;

/* 
* 时间：2009-08-29
* 作者： ll
* 修改内容：1、修改页面显示sql，增加yueslb、yuezlb、yuercbmdj 三个表的审核状态，
	以审核状态为条件查询。
			
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
/* 
* 时间：2009-07-20
* 作者： ll
* 修改内容：修改曲线图sql,按照每年年初到所选时间为查询条件。		
*/ 
/* 
* 时间：2009-07-28
* 作者： ll
* 修改内容：1、修改查询总金额，用万元为单位。
* 			2、把查询的起始月份默认为1月份。
*/ 

/* 
* 时间：2009-09-4
* 作者： ll
* 修改内容：解决煤矿地区再展示到下一层后，点击“返回上级”按钮页面不是返回到上一层菜单问题
* 		   
*/
/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Rucmcbtj extends BasePage {
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
	
//	private String REPORT_ONEGRAPH_JIESBMDJYS="jiesbmdjys";
//	private String mstrReportName="";
	 
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
		cn.Close();
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		return getRucmcbtj();
	}
	
	public String getRucmcbtj(){
		Visit visit = (Visit) getPage().getVisit();
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
//		开始时间
		long intnian;
		if (getNianF1Value() == null) {
			intnian = DateUtil.getYear(new Date());
		} else {
			intnian = getNianF1Value().getId();
		}
		long intyue;
		if (getYueF1Value() == null) {
			intyue = DateUtil.getMonth(new Date());
		} else {
			intyue = getYueF1Value().getId();
		}
		//当月份是1的时候显示01,
		String Stryue="";
		if(intyue<10){
			
			Stryue="0"+intyue;
		}else{
			Stryue=""+intyue;
		}
		String strdate = intyear+"-"+StrMonth+"-01";
		String strdate1 = intnian+"-"+Stryue+"-01";
		
		String titdate = intyear+"年"+StrMonth+"月";
		
		Report rt=new Report();
		String strleix=getLeixDropDownValue().getValue();
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and tj.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and tj.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("分厂")){
			if(jib==1){//选集团时刷新出所有的电厂
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,dc.mingc,dc.xuh as xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and tj.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
					"        yuercbmdj dj,yuetjkjb tj ,vwgongys y,vwdianc dc\n" + 
					"        where tj.gongysb_id=y.id and dc.id=tj.diancxxb_id \n" + strCondition+
					"        and dj.yuetjkjb_id=tj.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n"  ;
				strGroupID = " y.dqid ";
				strQLeix="矿";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//选集团时刷新出所有的电厂
				diancid = "";
//				*********************加入******************
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc,dc.xuh as xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct  id, mingc,xuh from diancxxb where id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct  id, mingc,xuh from diancxxb where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//取得某日期段的供货单位		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
				"        yuercbmdj dj,yuetjkjb tj,vwgongys y,vwdianc dc\n" + 
				"        where tj.gongysb_id=y.id and dc.id=tj.diancxxb_id \n" + strCondition+
				"        and dj.yuetjkjb_id=tj.id and tj.riq>=to_date('"+strdate+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "y.dqid";
				strQLeix="矿";
			}
		}
		String zhuangt="";
		String shulzt="";
		String zhilzt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
				shulzt="";
				zhilzt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (dj.zhuangt=1 or dj.zhuangt=2)";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and dj.zhuangt=2";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
			}
		String leijrq="";
		if(Stryue.equals("01")){
			leijrq="and dj.fenx='累计' and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') ";
		}else{
			leijrq="and dj.fenx='本月' and tj.riq>=to_date('"+strdate1+"','yyyy-mm-dd') and tj.riq<=to_date('"+strdate+"','yyyy-mm-dd') ";
		}
		JDBCcon cn = new JDBCcon();
		String danwmc=getTreeDiancmc(this.getTreeid());
		 
		 StringBuffer sbsql = new StringBuffer();
//		sbsql.append("select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) , \n");
//		sbsql.append("       decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,  \n");
//		sbsql.append("       sum(sj.jingz) as 入厂数量,\n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as 发热量, \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as 综合价, \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as 煤价, \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as 运费, \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as 杂费, \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
//		sbsql.append("       	round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as 含税标煤单价, \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
//		sbsql.append("       	round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as 不含税标煤单价, \n");
//		sbsql.append("		 decode(sum(sj.jingz),0,0,round(sum(sj.yunsjl*sj.jingz)/sum(jingz),0))  as 运距 \n");
//		sbsql.append("   from   yuetjkjb tj,jihkjb kj, \n");
//		sbsql.append("(select "+strGroupID+" as id,dj.yuetjkjb_id,dj.fenx,nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl,\n \n");
//		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj,\n ");
//		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj,\n");
//		sbsql.append("        nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf,nvl(dj.yunsjl,0) as yunsjl \n");
//		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y \n \n");
//		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   \n");
//		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月'and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id ").append(strCondition).append(" \n");
//		sbsql.append("     ) sj, \n");
//		sbsql.append("     ( "+strgongs+" ) dc \n"); 
//		sbsql.append(" where sj.yuetjkjb_id=tj.id and sj.id=dc.id(+)  and tj.jihkjb_id=kj.id \n");//.append(strCondition).append(strCondition).append(" ");
//		sbsql.append("    and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')   \n");
//		sbsql.append("group by rollup(dc.mingc) order by grouping(dc.mingc),dc.mingc \n");
//		********************************修改后sql*****************************************
		if ((strleix.equals("分厂") && jib==3)){
			sbsql.append("select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,  \n");
		}else if ((strleix.equals("分矿") && jib==3  &&  !"-1".equals(getGongysId()))){
			sbsql.append("select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,  \n");
		}else{
			sbsql.append("select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,  \n");
			 sbsql.append("       decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,  \n");
		}
				 

		sbsql.append("       fx.fenx, \n");
		sbsql.append("       sum(sj.laimsl) as 入厂数量, \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as 发热量,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as 综合价, \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(sj.laimsl),2)) as 煤价,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.yunj*sj.laimsl)/sum(sj.laimsl),2)) as 运费,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.daozzf+sj.zaf+sj.jiaohqzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as 杂费,  \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2) as 含税标煤单价, \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit-sj.meijs-sj.yunjs)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2)  as 不含税标煤单价,            \n");
		sbsql.append("       round(sum(sj.laimsl)*decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2))/10000,2) as 总金额 --煤量*综合价 \n");
		sbsql.append("   from  \n");
		sbsql.append("       (select distinct dc.id,fx.fenx,fx.xuh from \n");
		sbsql.append("             (select distinct "+strGroupID+" as id \n");
		sbsql.append("                     from yuercbmdj ycb,yuetjkjb kj,vwdianc dc,vwgongys y \n");
		sbsql.append("                     where ycb.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id and kj.gongysb_id=y.id \n");
		sbsql.append("                     and (riq=to_date('"+strdate+"','yyyy-mm-dd') \n");
		sbsql.append("                     or riq=last_year_today(to_date('"+strdate+"','yyyy-mm-dd'))) ) dcid,vwfenxYue fx, \n");
		sbsql.append("             ( "+strgongs+" ) dc  \n");
		sbsql.append("         where dc.id=dcid.id  \n");
		sbsql.append("         ) fx,  \n");
		sbsql.append("(select decode(1,1,'本月','') as fenx,"+strGroupID+" as id, \n");
		sbsql.append("        sum(nvl(sl.laimsl,0)) as laimsl \n");
//		sbsql.append("        ,decode(sum(sl.laimsl),0,0,round(sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl),2)) as farl \n");
		sbsql.append("        ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id  \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id  ").append(strCondition).append(" \n");
		sbsql.append("         and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') "+zhuangt+shulzt+zhilzt+"  \n");
		sbsql.append("   group by ("+strGroupID+")  \n");
		sbsql.append("   union \n");
		sbsql.append("   select decode(1,1,'累计','') as fenx,"+strGroupID+" as id, \n");
		sbsql.append("          sum(nvl(sl.laimsl,0)) as laimsl \n");
//		sbsql.append("          ,decode(sum(sl.laimsl),0,0,round(sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl),2)) as farl \n");
		sbsql.append("          ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("    from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id  \n");
		sbsql.append("         and dj.fenx=sl.fenx(+) and dj.fenx=zl.fenx(+) and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id     ").append(strCondition).append(" \n");
		sbsql.append("          "+leijrq+zhuangt+shulzt+zhilzt+"  \n");
		sbsql.append("    group by ("+strGroupID+")  \n");
		sbsql.append("     ) sj,  \n");
		sbsql.append("     ( "+strgongs+" ) dc \n");
		sbsql.append(" where fx.id=dc.id(+)   \n");
		sbsql.append("       and fx.id=sj.id(+) \n");
		sbsql.append("       and fx.fenx=sj.fenx(+) \n");
		sbsql.append("group by rollup(fx.fenx,dc.mingc)  \n");
		sbsql.append("having not grouping(fx.fenx)=1 \n");
		sbsql.append("order by grouping(dc.mingc),max(dc.xuh),dc.mingc,grouping(fx.fenx) desc,max(fx.fenx) \n");
//		**********************************************************************************
		
		String ArrHeader[][]=new String[2][11];
		 ArrHeader[0]=new String[] {"单位","分项","入厂煤量<br>(吨)","入厂热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","运费<br>(元/吨)","杂费<br>(元/吨)","标煤单价(元/吨)","标煤单价(元/吨)","总金额(万元)"};
		 ArrHeader[1]=new String[] {"单位","分项","入厂煤量<br>(吨)","入厂热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","运费<br>(元/吨)","杂费<br>(元/吨)","含税","不含税","总金额(万元)"};
		 
		 int ArrWidth[]=new int[] {100,60,70,70,70,70,55,55,70,70,100};
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString10());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setCenter(false);
		rt.setBody(new Table(rs,2,0,1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setFontSize(12);
		
		rt.body.setUseCss(true);
		rt.body.setColHeaderClass("tab_colheader");
		rt.body.setRowHeaderClass("tab_rowheader");
		rt.body.setFirstDataRowClass("tab_data_line_one");
		rt.body.setSecondDataRowClass("tab_data_line_two");
		rt.body.setCellsClass("tab_cells");
		rt.body.setTableClass("tab_body");
		
		rt.body.setBorder(2);
		rt.body.setPageRows(rt.PAPER_ROWS);
		rt.body.setWidth(ArrWidth);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero = true;

		cn.Close();
		return rt.getHtml();
	}
	
	//比例图
	public String getChartPie3D(){
		Visit visit = (Visit) getPage().getVisit();
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"年"+StrMonth+"月";
		
		String strleix=getLeixDropDownValue().getValue();
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and tj.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and tj.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("分厂")){
			if(jib==1){//选集团时刷新出所有的电厂
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and tj.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
					"        yuercbmdj dj,yuetjkjb tj,vwgongys y,vwdianc dc\n" + 
					"        where tj.gongysb_id=y.id and dc.id=tj.diancxxb_id \n" + strCondition+
					"        and dj.yuetjkjb_id=tj.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n" ;
				strGroupID = " y.dqid ";
				strQLeix="矿";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//选集团时刷新出所有的电厂
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct  id, mingc from diancxxb where id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct  id, mingc from diancxxb where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//取得某日期段的供货单位		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
				"        yuercbmdj dj,yuetjkjb tj,vwgongys y,vwdianc dc\n" + 
				"        where tj.gongysb_id=y.id and dc.id=tj.diancxxb_id \n" + strCondition+
				"        and dj.yuetjkjb_id=tj.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "y.dqid";
				strQLeix="矿";
			}
		}
		String zhuangt="";
		String shulzt="";
		String zhilzt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
				shulzt="";
				zhilzt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (dj.zhuangt=1 or dj.zhuangt=2)";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and dj.zhuangt=2";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
			}
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		/*sbsql.append("select dc.mingc as danwmc,nvl(sj.jingz,0) as jingz   \n");
		sbsql.append("from   yuetjkjb tj,jihkjb kj,  \n");
		sbsql.append("    (select "+strGroupID+" as id,dj.yuetjkjb_id,dj.fenx,nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl, \n");
		sbsql.append("         nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj, \n");
		sbsql.append("         nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj, \n");
		sbsql.append("         nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf  \n");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id    \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月'and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id \n");
		sbsql.append(strCondition).append(" \n");
		sbsql.append("         )  sj,  \n");
		sbsql.append("     ( "+strgongs+" ) dc  \n");
		sbsql.append("where sj.yuetjkjb_id=tj.id and sj.id=dc.id(+)  and tj.jihkjb_id=kj.id  \n");
		sbsql.append("      and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')   \n");*/
//		----------------------------------------------
	/*	sbsql.append("select dc.mingc as danwmc,nvl(jiessl,0) as jiessl  \n");
		sbsql.append("   from   \n");
		sbsql.append("    ( select "+strGroupID+" as id,sum(js.jiessl) as jiessl, decode(sum(nvl(js.jiessl,0)),0,0,round(sum(nvl(js.meikje,0)+nvl(yf.hansyf,0))/sum(js.jiessl),2)) as zonghj, \n");
		sbsql.append("             decode(sum(nvl(js.jiessl,0)),0,0,round(sum(meikje)/sum(js.jiessl),2)) as hanskj,  \n");
		sbsql.append("             decode(sum(nvl(js.jiessl,0)),0,0,round(sum(nvl(yf.guotyf,0)+nvl(yf.bukyf,0)+nvl(yf.dityf,0))/sum(js.jiessl),2)) as yunf,  \n");
		sbsql.append("             decode(sum(nvl(js.jiessl,0)),0,0,round(sum(nvl(yf.guotzf,0)+nvl(yf.ditzf,0))/sum(js.jiessl),2)) as zaf, " );
		sbsql.append("             decode(sum(nvl(js.jiessl,0)),0,0,round(sum(js.shuik)/sum(js.jiessl),2)) as meis,  \n");
		sbsql.append("             decode(sum(nvl(js.jiessl,0)),0,0,round(sum(yf.shuik)/sum(js.jiessl),2)) as yunfs \n");
		sbsql.append("        from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y, hetb ht,\n");
		sbsql.append("              (select js.id,nvl(zl.jies,0) as farl from jiesb js, (select zb.jiesdid, jies from jieszbsjb zb where zhibb_id=getFarlZhib_id()) zl,vwdianc dc,vwgongys y \n");
		sbsql.append("                where js.id=zl.jiesdid(+)  and js.jiessl<>0 \n");
		sbsql.append("                  and js.diancxxb_id=dc.id and js.gongysb_id=y.id ").append(strCondition).append(" \n");
		sbsql.append("                  and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<to_date('"+strDate2+"','yyyy-mm-dd') \n");
		sbsql.append("               ) rl  \n");
		sbsql.append("       where js.id=yf.diancjsmkb_id and js.hetb_id=ht.id(+) and js.id=rl.id  \n");
		sbsql.append("         and js.diancxxb_id=dc.id and js.gongysb_id=y.id ").append(strCondition).append(" \n");
		sbsql.append("         and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<to_date('"+strDate2+"','yyyy-mm-dd')  \n");
		sbsql.append("       group by "+strGroupID+")  sj, \n");
		sbsql.append("     ( "+strgongs+" ) dc \n");
		sbsql.append("where sj.id(+)=dc.id  \n");*/
//		———————————修改sql—————————————————————————————
		 
		sbsql.append("select dc.mingc as danwmc,nvl(sj.laimsl,0) as laimsl    \n");
		sbsql.append("from  ( select  "+strGroupID+" as id,  \n");
		sbsql.append("        sum(nvl(sl.laimsl,0)) as laimsl  \n");
		sbsql.append("        ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs  \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id   \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id    \n");
		sbsql.append("         and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')  "+zhuangt+shulzt+zhilzt+"  \n");
		sbsql.append(strCondition).append(" \n");
		sbsql.append("   group by ( "+strGroupID+")   \n");
		sbsql.append("         )  sj,   \n");
		sbsql.append("     ( "+strgongs+" ) dc   \n");
		sbsql.append("where  sj.id=dc.id(+)   \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", "laimsl", true);
		
		ct.showLegend=false;
		ct.chartBackgroundPaint=gp;
		ct.pieLabFormat=false;//不显示数据内容
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartPie3D','show')\"");
		String charImg=ct.ChartPie3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChartPie3D");
		ct.showLegend=true;
		ct.pieLabFormat=true;//不显示数据内容
		ct.pieLabGenerator=ct.piedatformat2;//显示百分比
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString7(ct.ChartPie3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChartPie3DBig(){
		return ((Visit) getPage().getVisit()).getString7();
	}
	
//	柱型图
	public String getChartBar(){
		Visit visit = (Visit) getPage().getVisit();
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"年"+StrMonth+"月";
//		***********************条件设置中的年份、月份**************************
		long intnianfen;
		if (getNianfenValue() == null) {
			intnianfen = DateUtil.getYear(new Date());
		} else {
			intnianfen = getNianfenValue().getId();
		}
		long intyuefen;
		if (getYuefenValue() == null) {
			intyuefen = DateUtil.getMonth(new Date());
		} else {
			intyuefen = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String Stryuefen="";
		if(intyuefen<10){
			
			Stryuefen="0"+intyuefen;
		}else{
			Stryuefen=""+intyuefen;
		}
		
//		String yue="";
//		if(getYue()!=null || getYue().equals("")){
//			if (Long.parseLong(getYue())<10){
//				yue="0"+Long.parseLong(getYue());
//			}else{
//				yue=getYue();
//			}
//		}
		String strdate1 = getNian()+"-"+getYue()+"-01";
//		*********************************************************************
		
		String date1="";
		String date2="";
		
		String strleix=getLeixDropDownValue().getValue();
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and tj.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and tj.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("分厂")){
			if(jib==1){//选集团时刷新出所有的电厂
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and tj.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
					"        yuercbmdj dj,yuetjkjb tj,vwgongys y,vwdianc dc\n" + 
					"        where tj.gongysb_id=y.id and dc.id=tj.diancxxb_id \n" + strCondition+
					"        and dj.yuetjkjb_id=tj.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n" ;
				strGroupID = " y.dqid ";
				strQLeix="矿";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//选集团时刷新出所有的电厂
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct  id, mingc from diancxxb where id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct  id, mingc from diancxxb where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//取得某日期段的供货单位		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
				"        yuercbmdj dj,yuetjkjb tj,vwgongys y,vwdianc dc\n" + 
				"        where tj.gongysb_id=y.id and dc.id=tj.diancxxb_id \n" + strCondition+
				"        and dj.yuetjkjb_id=tj.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "y.dqid";
				strQLeix="矿";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			date1=" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n";
			date2=" and tj.riq=add_months(to_date('"+strdate+"','yyyy-mm-dd'),-12)  \n";
		}else if(getRbvalue().equals("tiaoj2")){
			date1=" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') \n";
			date2=" and tj.riq=add_months(to_date('"+strdate+"','yyyy-mm-dd'),-1) \n";
		}else{
			date1=" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')  \n";
			date2=" and tj.riq=to_date('"+strdate1+"','yyyy-mm-dd')  \n";
		}
		String zhuangt="";
		String shulzt="";
		String zhilzt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
				shulzt="";
				zhilzt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (dj.zhuangt=1 or dj.zhuangt=2)";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and dj.zhuangt=2";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
			}
/*		sbsql.append("select dc.mingc as danwmc, decode(1,1,'本期') as fenx, \n");
		sbsql.append("  sum(sj.jingz) as jingz, \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as farl,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as zonghj,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as hansmj,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as yunf,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as zaf,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,  \n");
		sbsql.append("round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as biaomdj,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,  \n");
		sbsql.append("round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as buhsbmdj \n");
		sbsql.append("from  yuetjkjb tj,jihkjb kj,  \n");
		sbsql.append("  ( select "+strGroupID+" as id,dj.yuetjkjb_id,dj.fenx,nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl, \n");
		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj, \n");
		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj, \n");
		sbsql.append("        nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf  \n");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id    \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月'and tj.diancxxb_id=dc.id  \n");
		sbsql.append("         and tj.gongysb_id=y.id  \n");
//		sbsql.append("         and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') \n");
		sbsql.append( date1);
		sbsql.append("         "+strCondition+"  \n");
		sbsql.append("         )  sj,  \n");
		sbsql.append("     (  "+strgongs+"  ) dc \n");
		sbsql.append("where sj.yuetjkjb_id=tj.id and sj.id=dc.id(+)  and tj.jihkjb_id=kj.id  \n");
//		sbsql.append("    and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')   \n");
		sbsql.append( date1);
		sbsql.append("group by rollup(dc.mingc) having (dc.mingc is not null) \n");
		sbsql.append("       union   \n");
		sbsql.append("select dc.mingc as danwmc, decode(1,1,'同期') as fenx,  \n");
		sbsql.append("  sum(sj.jingz) as jingz, \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as farl,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as zonghj,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as hansmj,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as yunf,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as zaf,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,  \n");
		sbsql.append("round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as biaomdj,  \n");
		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,  \n");
		sbsql.append("round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as buhsbmdj \n");
		sbsql.append("from  yuetjkjb tj,jihkjb kj,  \n");
		sbsql.append("  ( select "+strGroupID+" as id,dj.yuetjkjb_id,dj.fenx,nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl, \n");
		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj, \n");
		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj, \n");
		sbsql.append("        nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf  \n");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id    \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月'and tj.diancxxb_id=dc.id  \n");
		sbsql.append("         and tj.gongysb_id=y.id  \n");
//		sbsql.append("         and tj.riq=add_months(to_date('"+strdate+"','yyyy-mm-dd'),-12)");
		sbsql.append( date2);
		sbsql.append("         "+strCondition+" \n");
		sbsql.append("         )  sj,  \n");
		sbsql.append("     (  "+strgongs+"  ) dc \n");
		sbsql.append("where sj.yuetjkjb_id=tj.id and sj.id=dc.id(+)  and tj.jihkjb_id=kj.id  \n");
		sbsql.append("    and tj.riq=add_months(to_date('"+strdate+"','yyyy-mm-dd'),-12)  \n");
		sbsql.append( date2);
		sbsql.append("group by rollup(dc.mingc) having (dc.mingc is not null) \n");*/
//*******************************修改后sql**********************************************
	
		sbsql.append("select dc.mingc as danwmc, decode(1,1,'本期') as fenx,  \n");
		sbsql.append("     sum(sj.laimsl) as laimsl,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as farl,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zonghj,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(sj.laimsl),2)) as hansmj,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.yunj*sj.laimsl)/sum(sj.laimsl),2)) as yunf,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.daozzf+sj.zaf+sj.jiaohqzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zaf,   \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2) as biaomdj,  \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit-sj.meijs-sj.yunjs)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2)  as buhsbmdj,             \n");
		sbsql.append("       sum(sj.laimsl)*decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zongje  \n");
		sbsql.append(" from   \n");
		sbsql.append("  (     select "+strGroupID+" as id,  \n");
		sbsql.append("        sum(nvl(sl.laimsl,0)) as laimsl  \n");
		sbsql.append("        ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs  \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id   \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id  "+zhuangt+shulzt+zhilzt+"  \n");
		sbsql.append( date1);
		sbsql.append("         "+strCondition+" \n");
		sbsql.append("   group by ("+strGroupID+")              \n");
		sbsql.append("         )  sj,   \n");
		sbsql.append("     (   "+strgongs+"    ) dc  \n");
		sbsql.append("where  sj.id=dc.id(+)   \n");
		sbsql.append("group by rollup(dc.mingc) having (dc.mingc is not null)  \n");
		sbsql.append("       union    \n");
		sbsql.append("  select dc.mingc as danwmc, decode(1,1,'同期') as fenx,  \n");
		sbsql.append("     sum(sj.laimsl) as laimsl,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as farl,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zonghj,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(sj.laimsl),2)) as hansmj,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.yunj*sj.laimsl)/sum(sj.laimsl),2)) as yunf,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.daozzf+sj.zaf+sj.jiaohqzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zaf,   \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2) as biaomdj,  \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit-sj.meijs-sj.yunjs)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2)  as buhsbmdj,             \n");
		sbsql.append("       sum(sj.laimsl)*decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zongje  \n");
		sbsql.append(" from   \n");
		sbsql.append("  (     select "+strGroupID+" as id,  \n");
		sbsql.append("        sum(nvl(sl.laimsl,0)) as laimsl  \n");
		sbsql.append("        ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs  \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id   \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id  "+zhuangt+shulzt+zhilzt+"  \n");
		sbsql.append( date2);
		sbsql.append("         "+strCondition+" \n");
		sbsql.append("   group by ("+strGroupID+")              \n");
		sbsql.append("         )  sj,   \n");
		sbsql.append("     (  "+strgongs+"    ) dc  \n");
		sbsql.append("where  sj.id=dc.id(+)   \n");
		sbsql.append("group by rollup(dc.mingc) having (dc.mingc is not null)       \n");
		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		CategoryDataset dataset = cd.getRsDataChart(rs, "danwmc", "fenx",  getDataField());
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
		
		((Visit) getPage().getVisit()).setString5(ct.ChartBar3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChartBarBig(){
		return ((Visit) getPage().getVisit()).getString5();
	}
	
	
//	曲线图
	public String getChartLine(){
//		String strDateBegin=DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getBeginriqDate()));//日期字符
//		String strDateEnd=DateUtil.FormatDate(DateUtil.getLastDayOfYear(getBeginriqDate()));//日期字符
		Visit visit = (Visit) getPage().getVisit();
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"年"+StrMonth+"月";
		int jib=this.getDiancTreeJib();
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and tj.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and tj.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if(jib==1){//选集团时刷新出所有的电厂
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}
		String zhuangt="";
		String shulzt="";
		String zhilzt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
				shulzt="";
				zhilzt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (dj.zhuangt=1 or dj.zhuangt=2)";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and dj.zhuangt=2";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
			}
		JDBCcon cn = new JDBCcon();
		
		StringBuffer sbsql = new StringBuffer();
//		 
//		sbsql.append("select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx,  \n");
//		sbsql.append("      sum(sj.jingz) as jingz,  \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as farl,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as zonghj,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as hansmj,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as yunf,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as zaf,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,   \n");
//		sbsql.append("round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as biaomdj,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,   \n");
//		sbsql.append("round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as buhsbmdj    \n");
//		sbsql.append("from ( select to_char(tj.riq,'MM') as yuef ,  \n");
//		sbsql.append("        nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl,  \n");
//		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj,  \n");
//		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj,  \n");
//		sbsql.append("        nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf   \n");
//		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y   \n");
//		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id     \n");
//		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月'and tj.diancxxb_id=dc.id   \n");
//		sbsql.append("         and tj.gongysb_id=y.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')   \n");
//		sbsql.append("        "+strCondition+")  sj,  \n");
//		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'本期') as fenx  \n");
//		sbsql.append("    from xitxxb  where rownum<=12) bq   \n");
//		sbsql.append("        where bq.yuef=sj.yuef(+)  \n");
//		sbsql.append("group  by (to_char(bq.yuef),bq.fenx) \n");
//		sbsql.append("       union   \n");
//		sbsql.append("select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx,  \n");
//		sbsql.append("        sum(sj.jingz) as jingz,  \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as farl,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as zonghj,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as hansmj,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as yunf,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as zaf,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,   \n");
//		sbsql.append("round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as biaomdj,   \n");
//		sbsql.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,   \n");
//		sbsql.append("round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as buhsbmdj    \n");
//		sbsql.append("from ( select to_char(tj.riq,'MM') as yuef , \n");
//		sbsql.append("                     nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl,  \n");
//		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj,  \n");
//		sbsql.append("        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj,  \n");
//		sbsql.append("        nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf   \n");
//		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y   \n");
//		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id     \n");
//		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月'and tj.diancxxb_id=dc.id   \n");
//		sbsql.append("         and tj.gongysb_id=y.id and tj.riq=add_months(to_date('"+strdate+"','yyyy-mm-dd'),-12)   \n");
//		sbsql.append("        "+strCondition+")  sj,  \n");
//		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'同期') as fenx  \n");
//		sbsql.append("    from xitxxb  where rownum<=12) bq   \n");
//		sbsql.append("        where bq.yuef=sj.yuef(+)  \n");
//		sbsql.append("group  by (to_char(bq.yuef),bq.fenx)  \n");
//		--------------------------------修改后sql----------------------------------------
		sbsql.append("select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx,   \n");
		sbsql.append("       sum(sj.laimsl) as laimsl,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as farl,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zonghj,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(sj.laimsl),2)) as hansmj,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.yunj*sj.laimsl)/sum(sj.laimsl),2)) as yunf,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.daozzf+sj.zaf+sj.jiaohqzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zaf,   \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2) as biaomdj,  \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit-sj.meijs-sj.yunjs)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2)  as buhsbmdj,             \n");
		sbsql.append("       sum(sj.laimsl)*decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as 总金额 --煤量*综合价  \n");
		sbsql.append("from ( select to_char(tj.riq,'MM') as yuef ,   \n");
		sbsql.append("        sum(nvl(sl.laimsl,0)) as laimsl  \n");
		sbsql.append("        ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs  \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id   \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id "+zhuangt+shulzt+zhilzt+"    \n");
		sbsql.append("         and tj.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and tj.riq<=to_date('"+strdate+"','yyyy-mm-dd')  "+strCondition+"  \n");
		sbsql.append("   group by (to_char(tj.riq,'MM'))     \n");
		sbsql.append("        )  sj,   \n");
		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'本期') as fenx   \n");
		sbsql.append("    from xitxxb  where rownum<=12) bq    \n");
		sbsql.append("        where bq.yuef=sj.yuef(+)   \n");
		sbsql.append("group  by (to_char(bq.yuef),bq.fenx)  \n");
		sbsql.append("       union    \n");
		sbsql.append("select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx,   \n");
		sbsql.append("       sum(sj.laimsl) as laimsl,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as farl,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zonghj,  \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(sj.laimsl),2)) as hansmj,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.yunj*sj.laimsl)/sum(sj.laimsl),2)) as yunf,   \n");
		sbsql.append("       decode(sum(sj.laimsl),0,0,round(sum((sj.daozzf+sj.zaf+sj.jiaohqzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as zaf,   \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2) as biaomdj,  \n");
		sbsql.append("       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit-sj.meijs-sj.yunjs)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2)  as buhsbmdj,             \n");
		sbsql.append("       sum(sj.laimsl)*decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as 总金额 --煤量*综合价  \n");
		sbsql.append("from ( select to_char(tj.riq,'MM') as yuef ,   \n");
		sbsql.append("        sum(nvl(sl.laimsl,0)) as laimsl  \n");
		sbsql.append("        ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs  \n");
		sbsql.append("         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs  \n");
		sbsql.append("		   ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf");
		sbsql.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j  \n");
		sbsql.append("   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id   \n");
		sbsql.append("         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id  "+zhuangt+shulzt+zhilzt+"   \n");
//		sbsql.append("         and tj.riq>=add_months(to_date('"+intyear+"-01-01','yyyy-mm-dd'),-12) and tj.riq<=add_months(to_date('"+strdate+"','yyyy-mm-dd'),-12)  "+strCondition+" \n");
		sbsql.append("         and tj.riq>=add_months(to_date('"+intyear+"-01-01','yyyy-mm-dd'),-12) and tj.riq<=add_months(to_date('"+intyear+"-12-01','yyyy-mm-dd'),-12)  "+strCondition+" \n");
		sbsql.append("   group by (to_char(tj.riq,'MM'))     \n");
		sbsql.append("        )  sj,   \n");
		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'同期') as fenx   \n");
		sbsql.append("    from xitxxb  where rownum<=12) bq    \n");
		sbsql.append("        where bq.yuef=sj.yuef(+)   \n");
		sbsql.append("group  by (to_char(bq.yuef),bq.fenx)  \n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","yuef", getDataField());//rs记录集构造生成图片需要的数据
		ct.intDigits=getDigts();
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
			
			visit.setDate1(DateUtil.getFirstDayOfMonth(new Date()));
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			visit.setDropDownBean6(null);
			visit.setDropDownBean7(null);
			
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setProSelectionModel6(null);
			visit.setProSelectionModel7(null);
			this.setTreeid(null);
			setNian(null);
			setYue(null);
			
			setNianF1Value(null);
			setYueF1Value(null);
			getNianF1Models();
			getYueF1Models();
			
			setNianfValue(null);
			setYuefValue(null);
			
			//begin方法里进行初始化设置
			visit.setString10(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString10(pagewith);
				}
			//	visit.setString10(null);保存传递的非默认纸张的样式
				
			getNianfModels();
			getYuefModels();
		}
		if(ZhuangtChange == false){
			ZhuangtChange = false;
			getRucmcbtj();
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
		
			
			+ "new Ext.form.ComboBox({ \n"
			+ "	fieldLabel:'年份', \n"
			+ "width:60, \n"
			+ "selectOnFocus:true, \n"
			+ "transform:'NIANFEN', \n"
			+ "lazyRender:true, \n"
			+ "triggerAction:'all', \n"
			+ "typeAhead:true, \n"
			+ "forceSelection:true, \n"
//			+ "listeners:{select:function(){document.Form0.submit();}}, \n"
			+ "editable:false \n"
			+ "}), \n"
			+ "new Ext.form.ComboBox({ \n"
			+ "	fieldLabel:'月份', \n"
			+ "width:60, \n"
			+ "selectOnFocus:true, \n"
			+ "transform:'YUEFEN', \n"
			+ "lazyRender:true, \n"
			+ "triggerAction:'all', \n"
			+ "typeAhead:true, \n"
			+ "forceSelection:true, \n"
//			+ "listeners:{select:function(){document.Form0.submit();}}, \n"
			+ "editable:false \n"
			+ "})] \n"
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
			+ "		document.getElementById('NIAN').value=document.getElementById('NIANFEN').value;	\n"
			+ "		document.getElementById('YUE').value=document.getElementById('YUEFEN').value;	\n"
			+ " 	//document.getElementById('RefurbishButton').click(); \n"
			+ " 	 document.Form0.submit();\n"
			+ "  	}   \n"
			+ "},{\n"
			+ "   text: '取消',\n"
			+ "   handler: function(){\n"
			+ "     win.hide();\n"
			+ "   }\n"
			+ "}]\n"
           
			+ " });";
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText(""));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianF1");
		nianf.setWidth(50);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("年"));
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText(""));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YueF1");
		yuef.setWidth(40);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("月"));
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("至"));
		ComboBox nianfe = new ComboBox();
		nianfe.setTransform("NIANF");
		nianfe.setWidth(50);
		nianfe.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianfe);
		tb1.addText(new ToolbarText("年"));
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText(""));
		ComboBox yuefe = new ComboBox();
		yuefe.setTransform("YUEF");
		yuefe.setWidth(40);
		yuefe.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuefe);
		tb1.addText(new ToolbarText("月"));
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
		tblx.setWidth(50);
		tb1.addField(tblx);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(70);
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
		
//		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
//		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		ToolbarButton tbtj=new ToolbarButton(null,"条件设置","function(){ if(!win){ "+Strtmpfunction+"}"
				+ " win.show(this);	\n" 
				+ "}"); 
		tbtj.setIcon(SysConstant.Btn_Icon_Search);	
		
		
//		------------最高层时，返回上级按钮隐藏--------
		String strDiancid=getTreeid();
		long diancxxb_id=visit.getDiancxxb_id();
		String diancxxbid=""+diancxxb_id;
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
		tb1.addItem(tbok);
//		tb1.addItem(tb);
		tb1.addItem(tbtj);

		
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
	public String getYue() {
		return ((Visit) this.getPage().getVisit()).getString8();
	}

	public void setYue(String yue) {
		((Visit) this.getPage().getVisit()).setString8(yue);
	}	
	public String getNian() {
		return ((Visit) this.getPage().getVisit()).getString9();
	}

	public void setNian(String nian) {
		((Visit) this.getPage().getVisit()).setString9(nian);
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
		String sql = "select id,mingc\n" + "from jihkjb \n";
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
		String sql = "select id,mingc\n" + "from yunsfsb \n";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return;
	}
	
//	 供应商
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getGongysDropDownModel().getOption(0));
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
		String sql = "select id,mingc\n" + "from gongysb\n";
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
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
		list.add(new IDropDownBean(1, "不含税标煤单价"));
		list.add(new IDropDownBean(2, "综合价"));
		list.add(new IDropDownBean(3, "煤价"));
		list.add(new IDropDownBean(4, "运费"));
		list.add(new IDropDownBean(5, "入厂热值"));
		list.add(new IDropDownBean(6, "含税标煤单价"));
		list.add(new IDropDownBean(7, "入厂煤量"));
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	private String getDataField(){
		String strDataField=getChartDropDownValue().getValue();

		if (strDataField.equals("不含税标煤单价")){
			return "buhsbmdj";
		}else if(strDataField.equals("综合价")){
			return "zonghj";
		}else if(strDataField.equals("煤价")){
			return "hansmj";
		}else if(strDataField.equals("运费")){
			return "yunf";
		}else if(strDataField.equals("入厂热值")){
			return "farl";
		}else if(strDataField.equals("含税标煤单价")){
			return "biaomdj";
		}else if(strDataField.equals("入厂煤量")){
			return "laimsl";
		}
		return "buhsbmdj";
	}
	
	private int getDigts(){
		String strDataField=getChartDropDownValue().getValue();
		if (strDataField.equals("入厂热值")){
			return 2;
		}else if(strDataField.equals("入厂煤量")){
			return 0;
		}else if(strDataField.equals("煤价")){
			return 2;
		}else if(strDataField.equals("运杂费")){
			return 2;
		}else if(strDataField.equals("灰份ad(%)")){
			return 2;
		}
		return 2;
	}	
	
    //状态
	private boolean ZhuangtChange = false;
//	private IDropDownBean _ZhuangtValue;
	
    public IDropDownBean getZhuangtValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean7()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean)getZhuangtModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean7();
    }
    public void setZhuangtValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean7(Value);

    }
    public void setZhuangtModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getZhuangtModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
            getZhuangtModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }
    public void getZhuangtModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(0,"含税"));
        list.add(new IDropDownBean(1,"不含税"));
        ((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(list)) ;
        return ;
    }

//    ------------------------------------------------------------------------
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString6();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString6(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString2());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString2();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
//	******************************************************************
	 // 开始年份下拉框
   private static IPropertySelectionModel _NianF1Model;
   public IPropertySelectionModel getNianF1Model() {
       if (_NianF1Model == null) {
           getNianF1Models();
       }
       return _NianF1Model;
   }
   
	private IDropDownBean _NianF1Value;
	
   public IDropDownBean getNianF1Value() {
       if (_NianF1Value == null) {
           int _nianfe = DateUtil.getYear(new Date());
           int _yuefe = DateUtil.getMonth(new Date());
           if (_yuefe == 1) {
               _nianfe = _nianfe - 1;
           }
           for (int i = 0; i < getNianF1Model().getOptionCount(); i++) {
               Object obj = getNianF1Model().getOption(i);
               if (_nianfe == ((IDropDownBean) obj).getId()) {
                   _NianF1Value = (IDropDownBean) obj;
                   break;
               }
           }
       }
       return _NianF1Value;
   }

   public void setNianF1Value(IDropDownBean Value) {
   	if  (_NianF1Value!=Value){
   		_NianF1Value = Value;
   	}
   }

   public IPropertySelectionModel getNianF1Models() {
       List listNianF1 = new ArrayList();
       int i;
       for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
           listNianF1.add(new IDropDownBean(i, String.valueOf(i)));
       }
       _NianF1Model = new IDropDownModel(listNianF1);
       return _NianF1Model;
   }

   public void setNianF1Model(IPropertySelectionModel _value) {
       _NianF1Model = _value;
   }

	// 开始月份下拉框
	private static IPropertySelectionModel _YueF1Model;
	
	public IPropertySelectionModel getYueF1Model() {
	    if (_YueF1Model == null) {
	        getYueF1Models();
	    }
	    return _YueF1Model;
	}
	
	private IDropDownBean _YueF1Value;
	
	public IDropDownBean getYueF1Value() {
	    if (_YueF1Value == null) {
	        int _yuefe = DateUtil.getMonth(new Date());
	        if (_yuefe == 1) {
	            _yuefe = 12;
	        } else {
//	            _yuefe = _yuefe - 1;
	        	_yuefe = 1;
	        }
	        
	        for (int i = 0; i < getYueF1Model().getOptionCount(); i++) {
	            Object obj = getYueF1Model().getOption(i);
	            if (_yuefe == ((IDropDownBean) obj).getId()) {
	                _YueF1Value = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YueF1Value;
	}
	
	public void setYueF1Value(IDropDownBean Value) {
   	if  (_YueF1Value!=Value){
   		_YueF1Value = Value;
   	}
	}

   public IPropertySelectionModel getYueF1Models() {
       List listYueF1 = new ArrayList();
       for (int i = 1; i < 13; i++) {
           listYueF1.add(new IDropDownBean(i, String.valueOf(i)));
       }
       _YueF1Model = new IDropDownModel(listYueF1);
       return _YueF1Model;
   }

   public void setYueF1Model(IPropertySelectionModel _value) {
       _YueF1Model = _value;
   }
//*****************************************************************************
	 //截止年份下拉框
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
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
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

	// 截止月份下拉框
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
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
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

//******************************************************************
    public String getNianfen() {
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setNianfen(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}
	
	public String getYuefen() {
		int intYuefen = Integer.parseInt(((Visit) getPage().getVisit()).getString5());
		if (intYuefen<10){
			return "0"+intYuefen;
		}else{
			return ((Visit) getPage().getVisit()).getString5();
		}
	}
	public void setYuefen(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	public void setRiq1() {
		setNianfen(getNianfenValue().getValue());
		setYuefen(getYuefenValue().getValue());
	}
	
	 // 年份下拉框
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
            for (int i = 0; i < getNianfenModel().getOptionCount(); i++) {
                Object obj = getNianfenModel().getOption(i);
                if (_nianfen == ((IDropDownBean) obj).getId()) {
                    _NianfenValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfenValue;
    }
	
    public void setNianfenValue(IDropDownBean Value) {
    	if  (_NianfenValue!=Value){
    		_NianfenValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfenModels() {
        List listNianfen = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianfen.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfenModel = new IDropDownModel(listNianfen);
        return _NianfenModel;
    }

    public void setNianfenModel(IPropertySelectionModel _value) {
        _NianfenModel = _value;
    }

	// 月份下拉框
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
    
    
}