package com.zhiren.jt.zdt.rulgl.gusmhyb;

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


public class Gusmhyb  extends BasePage implements PageValidateListener{
	
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
		int jib=this.getDiancTreeJib();
		String titlename="耗煤估算月报";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon con = new JDBCcon();
		
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

		String danwmc="";//汇总名称
		
		danwmc=getTreeDiancmc(this.getTreeid());
	
		String diancCondition1=
			"where d.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ;

			StringBuffer sbsql = new StringBuffer();
			sbsql.append("from  \n");
			sbsql.append("(select d.id,d.xuh,d.mingc,d.fuid,vwfenxyue.fenx from diancxxb d,vwfenxyue) fx, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'本月') as fenx,sum(fadl) as fadl,sum(gongdl) as gongdl, \n");
			sbsql.append("sum(gongrl) as gongrl from riscsjb where riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
			sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id \n");
			sbsql.append("union select diancxxb_id,decode(1,1,'累计') as fenx,sum(fadl) as fadl,sum(gongdl) as gongdl, \n");
			sbsql.append("sum(gongrl) as gongrl from riscsjb where riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) \n");
			sbsql.append("and riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') group by diancxxb_id) rsj, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'本月') as fenx,sum(fady) as fadym,sum(gongry) as gongrym \n");
			sbsql.append("from shouhcrbb where riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
			sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id \n");
			sbsql.append("union select diancxxb_id,decode(1,1,'累计') as fenx,sum(fady) as fadym,sum(gongry) as gongrym \n");
			sbsql.append("from shouhcrbb where riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) \n");
			sbsql.append("and riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') group by diancxxb_id) msj, \n");
			sbsql.append("(select rl.diancxxb_id,decode(1,1,'本月') as fenx,decode(sum(mh.fadhy+mh.gongrhy), \n");
			sbsql.append("0,0,round((sum(mh.fadhy*rl.qnet_ar)+sum(mh.gongrhy*rl.qnet_ar))/sum(mh.fadhy+mh.gongrhy),2)) as farl  \n");
			sbsql.append("from rulmzlb rl,meihyb mh where mh.rulmzlb_id=rl.id and mh.rulrq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
			sbsql.append("and mh.rulrq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by rl.diancxxb_id \n");
			sbsql.append("union select rl.diancxxb_id,decode(1,1,'累计') as fenx,decode(sum(mh.fadhy+mh.gongrhy), \n");
			sbsql.append("0,0,round((sum(mh.fadhy*rl.qnet_ar)+sum(mh.gongrhy*rl.qnet_ar))/sum(mh.fadhy+mh.gongrhy),2)) as farl  \n");
			sbsql.append("from rulmzlb rl,meihyb mh where mh.rulmzlb_id=rl.id and mh.rulrq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) \n");
			sbsql.append("and mh.rulrq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') group by rl.diancxxb_id) mrez, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'本月') as fenx,sum(fady) as fady,'40.98' as yrl,sum(gongry) as gongry \n");
			sbsql.append("from shouhcrbyb where riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
			sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id \n");
			sbsql.append("union select diancxxb_id,decode(1,1,'累计') as fenx,sum(fady) as fady,'40.98' as yrl,sum(gongry) as gongry \n");
			sbsql.append("from shouhcrbyb where riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) \n");
			sbsql.append("and riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') group by diancxxb_id) ysj,vwfengs gs,vwdianc vdc \n");
			String  tjsql=sbsql.toString();
			sbsql.setLength(0);
		
		if(jib==3){
			sbsql.append("select fx.mingc as danw, \n");
			sbsql.append("fx.fenx,round(sum(rsj.fadl),0)  as fadl,round(sum(rsj.gongdl),0) as gongdl,round(sum(rsj.gongrl),0) as gongrl, \n");
			sbsql.append("round(sum(msj.fadym),0) as fadym,round(sum(msj.gongrym),0) as gongrym, \n");
			sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
			sbsql.append("round(sum(ysj.fady),0) as fadyy,round(sum(ysj.gongry),0) as gongryy, \n");
			sbsql.append("decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2)) as youhrz, \n");
			sbsql.append("round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as fadmzbml, \n");
			sbsql.append("round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as gongrmzbml, \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as fadyzbml, \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as gongryzbml, \n");
			sbsql.append("(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as fadzhbml, \n");
			sbsql.append("(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as gongrzhbml, \n");
			sbsql.append("decode(sum(rsj.fadl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.fadl),2)) as fadbzmh, \n");
			sbsql.append("decode(sum(rsj.gongrl),0,0,round((round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongrl),2)) as gongrbzmh, \n");
			sbsql.append("decode(sum(rsj.gongdl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongdl),2)) as gongdbzmh \n");
			sbsql.append(tjsql);
			sbsql.append("where fx.fuid=gs.id and fx.id=ysj.diancxxb_id(+) and fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
			sbsql.append("and fx.id=msj.diancxxb_id(+) and fx.fenx=ysj.fenx(+) and fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
			sbsql.append("and fx.fenx=mrez.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else if(jib==2){
			JDBCcon cn = new JDBCcon();
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			String danw = "";
			String groupby = "";
			String having ="";
			String orderby = "";
			try{
				ResultSet rl = cn.getResultSet(ranlgs);
				if(rl.next()){
					danw="decode(grouping(vdc.rlgsmc)+grouping(gs.mingc)+grouping(fx.mingc),2,vdc.rlgsmc,1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw,\n";
					groupby="group by rollup(fx.fenx,vdc.rlgsmc,gs.mingc,fx.mingc) \n";
					having="having not(grouping(vdc.rlgsmc)=1) \n";
					orderby="order by grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n";
				}else{
					danw="decode(grouping(gs.mingc)+grouping(fx.mingc),1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw,\n";
					groupby="group by rollup(fx.fenx,gs.mingc,fx.mingc) \n";
					having="having not(grouping(gs.mingc)=1) \n";
					orderby="order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n";
				}
				rl.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			sbsql.append("select "+danw+"");
			sbsql.append("fx.fenx,round(sum(rsj.fadl),0)  as fadl,round(sum(rsj.gongdl),0) as gongdl,round(sum(rsj.gongrl),0) as gongrl, \n");
			sbsql.append("round(sum(msj.fadym),0) as fadym,round(sum(msj.gongrym),0) as gongrym, \n");
			sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
			sbsql.append("round(sum(ysj.fady),0) as fadyy,round(sum(ysj.gongry),0) as gongryy, \n");
			sbsql.append("decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2)) as youhrz, \n");
			sbsql.append("round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as fadmzbml, \n");
			sbsql.append("round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as gongrmzbml, \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as fadyzbml, \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as gongryzbml, \n");
			sbsql.append("(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as fadzhbml, \n");
			sbsql.append("(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as gongrzhbml, \n");
			sbsql.append("decode(sum(rsj.fadl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.fadl),2)) as fadbzmh, \n");
			sbsql.append("decode(sum(rsj.gongrl),0,0,round((round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongrl),2)) as gongrbzmh, \n");
			sbsql.append("decode(sum(rsj.gongdl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongdl),2)) as gongdbzmh \n");
			sbsql.append(tjsql);
			sbsql.append("where fx.fuid=gs.id and fx.id=ysj.diancxxb_id(+) and fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
			sbsql.append("and fx.id=msj.diancxxb_id(+) and fx.fenx=ysj.fenx(+) and fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
			sbsql.append("and fx.fenx=mrez.fenx(+) and fx.id=vdc.id  \n");
			sbsql.append(groupby);
			sbsql.append(having);
			sbsql.append(orderby);
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx,round(sum(rsj.fadl),0)  as fadl,round(sum(rsj.gongdl),0) as gongdl,round(sum(rsj.gongrl),0) as gongrl, \n");
			sbsql.append("round(sum(msj.fadym),0) as fadym,round(sum(msj.gongrym),0) as gongrym, \n");
			sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
			sbsql.append("round(sum(ysj.fady),0) as fadyy,round(sum(ysj.gongry),0) as gongryy, \n");
			sbsql.append("decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2)) as youhrz, \n");
			sbsql.append("round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271) as fadmzbml, \n");
			sbsql.append("round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271) as gongrmzbml, \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271) as fadyzbml, \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271) as gongryzbml, \n");
			sbsql.append("(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271)) as fadzhbml, \n");
			sbsql.append("(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271)) as gongrzhbml, \n");
			sbsql.append("decode(sum(rsj.fadl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.fadl),0)) as fadbzmh, \n");
			sbsql.append("decode(sum(rsj.gongrl),0,0,round((round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271))/sum(rsj.gongrl),0)) as gongrbzmh, \n");
			sbsql.append("decode(sum(rsj.gongdl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongdl),0)) as gongdbzmh \n");
			sbsql.append(tjsql);
			sbsql.append("where fx.fuid=gs.id and fx.id=ysj.diancxxb_id(+) and fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
			sbsql.append("and fx.id=msj.diancxxb_id(+) and fx.fenx=ysj.fenx(+) and fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
			sbsql.append("and fx.fenx=mrez.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			
		 //System.out.println(sbsql);
		 ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		 Report rt = new Report();
		 String ArrHeader[][]=new String[2][20];
		 ArrHeader[0]=new String[] {"单位","单位","发电量<br>(千瓦时)","供电量<br>(千瓦时)","供热量<br>(吉焦)","耗天然煤量(吨)","耗天然煤量(吨)","耗煤热值<br>(千卡/千克)","耗天然油量(吨)","耗天然油量(吨)","耗油热值<br>(千卡/千克)","耗用煤折标煤量(吨)","耗用煤折标煤量(吨)","耗用油折标煤量(吨)","耗用油折标煤量(吨)","综合标煤量(吨)","综合标煤量(吨)","标准煤耗","标准煤耗","标准煤耗"};
		 ArrHeader[1]=new String[] {"单位","单位","发电量<br>(千瓦时)","供电量<br>(千瓦时)","供热量<br>(吉焦)","发电","供热","耗煤热值<br>(千卡/千克)","发电","供热","耗油热值<br>(千卡/千克)","发电","供热","发电","供热","发电","供热","发电<br>(克/度)","供电<br>(克/度)","供热<br>(千克/吉焦)"};
		 int ArrWidth[]=new int[] {150,45,70,70,70,70,70,70,50,50,70,70,70,50,50,70,70,45,45,45};
		 iFixedRows=1;
		 iCol=10;
	
	 
		// 数据
	//	rt.setBody(new Table(rs,2, 0, 2));
		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		//第二列居中
		bt.setColAlign(2,Table.ALIGN_CENTER);
		rt.setTitle(getBiaotmc()+titlename, ArrWidth);
		rt.setDefaultTitle(1, 3, "填报单位:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4, intyear+"年"+intMonth+"月", Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		//第三行、第一列居中
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		//页脚 
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
	//	设置页数
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(15,3,"审核人:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(18,3,"填报人:",Table.ALIGN_LEFT);
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			con.Close();
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