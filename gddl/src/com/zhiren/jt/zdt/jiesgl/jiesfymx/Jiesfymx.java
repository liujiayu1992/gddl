package com.zhiren.jt.zdt.jiesgl.jiesfymx;
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


public class Jiesfymx extends BasePage {
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
//	判断回退
	private boolean isReturn = false;
	private void setReturnValue(){
		String strDiancid=getTreeid();
		Visit visit=((Visit) getPage().getVisit());
		long diancxxb_id=visit.getDiancxxb_id();
		String strleix=getLeixDropDownValue().getValue();
		int jib=this.getDiancTreeJib();
		String strOldId="";
		if (String.valueOf(diancxxb_id).equals(strDiancid)){
			if (strleix.equals("分矿")){
				if (!"-1".equals(visit.getString3())){
					if(getJihkjDropDownValue().getValue().equals("市场采购")){
						if(isGonghjb==false){
							isReturn=true;
						}else{
							visit.setString3("-1");
							isReturn=false;
						}
						
					}else{
						visit.setString3("-1");
					}
//					visit.setString3("-1");
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
		isBegin = false;
		return getRucmcbtj();
	}

//	
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
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"年"+StrMonth+"月";

		((Visit)getPage().getVisit()).setDate1(DateUtil.getDate(strdate));

		
		Report rt=new Report();
		String strleix=getLeixDropDownValue().getValue();
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		
		String strleibztbId = ""; 
		
		if(getZhuangtValue().getId()==1){
			strleibztbId = " and js.liucztb_id=1 ";
		}else if(getZhuangtValue().getId()==2){
			strleibztbId = " and js.liucztb_id=0 ";
		}else{
			strleibztbId = " ";
		}
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and js.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and js.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
//		if (!"-1".equals(getGongysId())){
//			strCondition=strCondition+" and y.dqid=" +getGongysId();
//		}
//		供货单位超链到分公司
		if (!"-1".equals(getGongysId())){
//			当计划口径为市场采购时，加一层供货单位。
			if(getJihkjDropDownValue().getValue().equals("市场采购")){

				getGongysbjb();
				if(isReturn==true){
					isGonghjb=true;
				}
				
				if(isGonghjb==true){
//						供货地区超链到供货单位
						strCondition=strCondition+" and (y.dqid=" +getGongysId()+" or y.id="+getGongysId()+")";
				}else{
					//供货单位超链到电厂
					strCondition=strCondition+" and y.id=" +getGongysId();
				}
			}else{
				//供货单位超链到分公司
				strCondition=strCondition+" and y.dqid=" +getGongysId();
			}
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
				strCondition =strCondition+ " and js.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
					"        jiesb js ,vwgongys y,vwdianc dc\n" + 
					"        where js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+
					"         and js.jiesrq>=to_date('"+strdate+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)\n"  ;
				strGroupID = " y.dqid ";
				strQLeix="矿";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else if(strleix.equals("分矿")){
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
			//取得某日期段的供货地区	
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
				"        jiesb js,vwgongys y,vwdianc dc\n" + 
				"        where js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+
				"         and js.jiesrq>=to_date('"+strdate+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)\n" ;
				strGroupID = "y.dqid";
				strQLeix="矿";
			}
			//取某日期段的供货单位	
			if(!"-1".equals(getGongysId())&&isGonghjb==true&&getJihkjDropDownValue().getValue().equals("市场采购")){
				strgongs="select distinct y.mingc as mingc,y.id as id from\n" +
				"        jiesb js,vwgongys y,vwdianc dc\n" + 
				"        where js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+
				"         and js.jiesrq>=to_date('"+strdate+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)\n" ;
				strGroupID = "y.id";
				strQLeix="矿";
			}
		}
		JDBCcon cn = new JDBCcon();
		String danwmc=getTreeDiancmc(this.getTreeid());
		
		 StringBuffer sbsql = new StringBuffer();
//		sbsql.append("select getLinkMingxTaiz('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) , \n");
		 if ((strleix.equals("分厂") && jib==3)){
				sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,\n");
			}else if ((strleix.equals("分矿") && jib==2 &&  !"-1".equals(getGongysId()))){
				if(getJihkjDropDownValue().getValue().equals("市场采购")){
						if(isGonghjb==true){
							sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,\n");
						}else{
//							sbsql.append(" select getAlink(decode(grouping(dc.mingc),1,-1,max(dc.id)) ,'"+getGongysId()+"',decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,\n");
							sbsql.append(" select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,\n");
						}
				}else{
//					sbsql.append(" select getAlink(decode(grouping(dc.mingc),1,-1,max(dc.id)) ,'"+getGongysId()+"',decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,\n");
					sbsql.append(" select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,\n");
				}
			}else{
					sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,\n");
			}
//		------------------------------------------------------------------------------------------------
		sbsql.append("       sum(sj.jiessl) as jiessl,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),3)) as farl,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy,\n" );
		sbsql.append("       decode( sum(nvl(sj.jiessl,0)),0,0,decode(round(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),3),0,0,\n" );
		sbsql.append("               round(round(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.271/round(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),3),2))\n" );
		sbsql.append("       ) as hansbmdj,\n" );
		sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),3),0,0,\n" );
		sbsql.append("              round(\n" );
		sbsql.append("                   (round(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)\n" );
		sbsql.append("                   -round(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)\n" );
		sbsql.append("                   -round(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.271/round(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),3)\n" );
		sbsql.append("        ,2))) as buhsbmdj\n" );
		sbsql.append(" from jihkjb jh,(\n" );
		sbsql.append("       select js.id,"+strGroupID+" as diancxxb_id,dc.mingc as mingc,ht.jihkjb_id as jihkjb_id,js.gongysb_id,js.jiesrq as riq,\n" );
		sbsql.append("             nvl(js.jiessl,0) as jiessl,round(nvl(rl.farl,0)*4.1816/1000,3) as farl,decode(nvl(js.jiessl,0),0,0,round((nvl(js.meikje,0)/js.jiessl),2)) as hansdj,\n" );
		sbsql.append("             decode(nvl(js.jiessl,0),0,0,round((nvl(js.meikje,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.dityf,0)/yf.jiessl,2)+round(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" );
		sbsql.append("                   +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" );
		sbsql.append("                   +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" );
		sbsql.append("                   +0 as zonghj,\n" );
		sbsql.append("             decode(nvl(js.jiessl,0),0,0,round((nvl(js.shuik,0)/js.jiessl),2)) as zengzs,\n" );
		sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf,\n" );
		sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf,\n" );
		sbsql.append("             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf,\n" );
		sbsql.append("             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs,\n" );
		sbsql.append("             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf,\n" );
		sbsql.append("             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf,\n" );
		sbsql.append("             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys,\n" );
		sbsql.append("             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf,\n" );
		sbsql.append("             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf,\n" );
		sbsql.append("             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys,\n" );
		sbsql.append("             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf,\n" );
		sbsql.append("             0 as qitfy\n" );
		sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,vwdianc dc,vwgongys y,\n" );
		sbsql.append("              (select js.id,nvl(zl.jies,0) as farl from jiesb js,jieszbsjb zl,zhibb zb\n" );
		sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='收到基低位热值Qnetar(MJ/Kg)'\n" );
		sbsql.append("                  and js.jiesrq>=to_date('"+strdate+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)\n" );
		sbsql.append("               ) rl\n" );
		sbsql.append("       where js.id=yf.diancjsmkb_id and js.hetb_id=ht.id and js.id=rl.id and js.yunsfsb_id=ys.id and y.id=js.gongysb_id "+strleibztbId+"\n" );
		sbsql.append("         and js.diancxxb_id=dc.id " ).append(strCondition).append(" \n");
		sbsql.append("         and js.jiesrq>=to_date('"+strdate+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)\n" );
		sbsql.append("    ) sj,\n" );
		sbsql.append("     ( "+strgongs+" ) dc \n"); 
		sbsql.append("   where jh.id=sj.jihkjb_id and sj.diancxxb_id=dc.id(+) and sj.riq>=to_date('"+strdate+"','yyyy-mm-dd') and sj.riq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)\n" );
		sbsql.append("     group by rollup(dc.mingc)");
		sbsql.append("     order by grouping(dc.mingc)");
//		------------------------------------------------------------------------------------------------


		String ArrHeader[][] = new String[2][21];
		
		ArrHeader[0] = new String[] { "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细","燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细",
									  "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细","燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细",
									  "燃料结算费用明细", "燃料结算费用明细", "燃料结算费用明细",};
		ArrHeader[1]=new String[] {"单位","结算煤量<br>(吨)","结算热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","增值税<br>(元/吨)","矿运费<br>(元/吨)","交货<br>前运杂费<br>(元/吨)",
					 					"铁路运费<br>(元/吨)","铁路<br>运费税额<br>(元/吨)","到站<br>铁路杂费<br>(元/吨)","汽车运费<br>(元/吨)","汽运税额<br>(元/吨)","汽运杂费<br>(元/吨)",
					 					"海(水)<br>运费<br>(元/吨)","海(水)<br>运税额<br>(元/吨)","港杂费<br>(元/吨)","其它费用<br>(元/吨)","含税<br>标煤单价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)"};
		
		int ArrWidth[]=new int[] {100,80,65,55,55,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
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
		
		rt.body.setWidth(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero = true;

		cn.Close();
		return rt.getHtml();
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
			
			visit.setDate1(null);
//			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
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
		}
		getToolBars();
		isBegin=true;
	}	
//	判断供应是否有父id
//	----------------------------------
	private boolean isGonghjb=false;
	public void getGongysbjb(){
		JDBCcon con=new JDBCcon();
		String gongysfuid="";
		JDBCcon cn = new JDBCcon();
	
		String sql_gongysfuid="select g.fuid from gongysb g where g.id="+getGongysId();
		ResultSet rs=cn.getResultSet(sql_gongysfuid);
		try {
			while(rs.next()){
				gongysfuid=rs.getString("fuid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.Close();
		if(gongysfuid.equals("0")||gongysfuid.equals("-1")){
			isGonghjb=true;
		}else{
			isGonghjb=false;
		}
		
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
		

		ToolbarButton tbok = new ToolbarButton(null,"确定","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
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

//	结算单状态
	public boolean _Zhuangtchange = false;

	public IDropDownBean getZhuangtValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
			((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIZhuangtModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean11();
	}

	public void setZhuangtValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean11() != null) {
			id = ((Visit)getPage().getVisit()).getDropDownBean11().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Zhuangtchange = true;
			} else {
				_Zhuangtchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean11(Value);
	}

	public void setIZhuangtModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getIZhuangtModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel11() == null) {
			getIZhuangtModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getIZhuangtModels() {

		List List = new ArrayList();
		List.add(new IDropDownBean(0,"全部"));
		List.add(new IDropDownBean(1,"已审核"));
		List.add(new IDropDownBean(2,"未审核"));
		
		((Visit)getPage().getVisit()).setProSelectionModel11(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel11();
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
		list.add(new IDropDownBean(1, "入厂热值"));
		list.add(new IDropDownBean(2, "综合价"));
		list.add(new IDropDownBean(3, "煤价"));
		list.add(new IDropDownBean(4, "运费"));
		list.add(new IDropDownBean(5, "不含税标煤单价"));
		list.add(new IDropDownBean(6, "含税标煤价"));
		list.add(new IDropDownBean(7, "入厂煤量"));
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	private String getDataField(){
		String strDataField=getChartDropDownValue().getValue();

		if (strDataField.equals("入厂热值")){
			return "farl";
		}else if(strDataField.equals("综合价")){
			return "zonghj";
		}else if(strDataField.equals("煤价")){
			return "hansmj";
		}else if(strDataField.equals("运费")){
			return "yunf";
		}else if(strDataField.equals("不含税标煤单价")){
			return "buhsbmdj";
		}else if(strDataField.equals("含税标煤单价")){
			return "biaomdj";
		}else if(strDataField.equals("入厂煤量")){
			return "jingz";
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
	
	 // 年份下拉框
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

	// 月份下拉框
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
//	        if (_yuef == 1) {
//	            _yuef = 12;
//	        } else {
//	            _yuef = _yuef - 1;
//	        }
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