package com.zhiren.jt.gsdt.rulmjgfx;

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


public class Rulmjgfxreport  extends BasePage implements PageValidateListener{
	
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
			
		}
		
		
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Zhidlcx";
	private String mstrReportName="Zhidlcx";
	
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		String zhuangt="";
	
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and sl.zhuangt=2";
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
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
//			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="入炉煤价格分析表";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
				strSQL  = 
					"select   decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" + 
					"  decode(1,1,fx.fenx,'') as fenx,\n" + 
					"                   sum(bq.chunrml+bq.youzml) as tianrml_bq,sum(bq.chunrml) as chunrml_bq,sum(bq.youzml) as youzml_bq,\n" + 
					"                   decode(sum(bq.zonghtrml),0,0,Round_New(sum(bq.zonghrlrz*bq.laimsl)/sum(bq.zonghtrml),2)) as zonghrlrz_bq ,\n" + 
					"                   sum(bq.ranlzcb*10000) as ranlzcb_bq,\n" + 
					"                   decode(sum(bq.chunrml+bq.youzml),0,0,Round_New(sum(bq.fadmcb+bq.fadycb+bq.gongrmcb+bq.gongrycb)*10000/sum(bq.chunrml+bq.youzml),2)) as tianrmdj_bq,\n" + 
					"                   decode(sum(bq.chunrml) ,0,0,Round_New(sum(bq.fadmcb+ bq.gongrmcb)*10000/sum(bq.chunrml),2)) as chunmdj_bq,\n" + 
					"                   decode(sum(bq.youzml),0,0, Round_New(sum(bq.fadycb+ bq.gongrycb)*10000/sum(bq.youzml),2)) as youzdj_bq,\n" + 
					"                   decode(sum(bq.fadmzbml+bq.fadyzbzml+bq.fadqzbzml+bq.gongrmzbml+bq.gongryzbzml+bq.gongrqzbzml) ,0,0,\n" + 
					"                            Round_New(sum(bq.fadmcb+bq.fadycb+bq.fadrqcb+bq.gongrmcb+bq.gongrycb+bq.gongrrqcb)*10000/sum(bq.fadmzbml+bq.fadyzbzml+bq.fadqzbzml+bq.gongrmzbml+bq.gongryzbzml+bq.gongrqzbzml),2)) as biaomdj_bq,\n" + 
					"                   decode(sum(bq.fadmzbml+bq.gongrmzbml),0,0, Round_New(sum(bq.fadmcb+bq.gongrmcb)*10000/ sum(bq.fadmzbml+bq.gongrmzbml),2)) as meizbmdj_bq,\n" + 
					"                   decode(sum(bq.fadyzbzml+bq.gongryzbzml),0,0, Round_New(sum(bq.fadycb+bq.gongrycb)*10000/ sum(bq.fadyzbzml+bq.gongryzbzml),2)) as meizbmdj_bq,\n" + 
					"                   sum(bq.fadl) as fadl_bq,\n" + 
					"                   decode(sum(bq.fadl- bq.fadcgdl) ,0,0,Round_New(sum(bq.fadmzbml+bq.fadyzbzml+bq.fadqzbzml)*100/sum(bq.fadl- bq.fadcgdl),2)) as fadbzmhl_bq,\n" + 
					"                   sum(bq.gongrl) as gongrl_bq,\n" + 
					"                   decode(sum(bq.gongrl),0,0, Round_New(sum(bq.gongrmzbml+bq.gongryzbzml+bq.gongrqzbzml)*1000/ sum(bq.gongrl),2)) as gongrbzmhl_bq,\n" + 
					"                   (decode(sum(bq.laimsl),0,0,Round_New(sum(bq.rc_farl*1000*bq.laimsl)/sum(bq.laimsl),2))-\n" + 
					"                          decode(sum(bq.zonghtrml),0,0,Round_New(sum(bq.zonghrlrz*1000*bq.laimsl)/sum(bq.zonghtrml),2))) as rucrlrzc_bq,\n" + 
					"                   sum(bq.rijkc) as rijkc_bq,\n" + 
					"                    sum(tq.chunrml+tq.youzml) as tianrml_tq,sum(tq.chunrml) as chunrml_tq,sum(tq.youzml) as youzml_tq,\n" + 
					"                   decode(sum(tq.zonghtrml),0,0,Round_New(sum(tq.zonghrlrz*tq.laimsl)/sum(tq.zonghtrml),2)) as zonghrlrz_tq ,\n" + 
					"                   sum(tq.ranlzcb*10000) as ranlzcb_tq,\n" + 
					"                   decode(sum(tq.chunrml+tq.youzml),0,0,Round_New(sum(tq.fadmcb+tq.fadycb+tq.gongrmcb+tq.gongrycb)*10000/sum(tq.chunrml+tq.youzml),2)) as tianrmdj_tq,\n" + 
					"                   decode(sum(tq.chunrml) ,0,0,Round_New(sum(tq.fadmcb+ tq.gongrmcb)*10000/sum(tq.chunrml),2)) as chunmdj_tq,\n" + 
					"                   decode(sum(tq.youzml),0,0, Round_New(sum(tq.fadycb+ tq.gongrycb)*10000/sum(tq.youzml),2)) as youzdj_tq,\n" + 
					"                   decode(sum(tq.fadmzbml+tq.fadyzbzml+tq.fadqzbzml+tq.gongrmzbml+tq.gongryzbzml+tq.gongrqzbzml) ,0,0,\n" + 
					"                            Round_New(sum(tq.fadmcb+tq.fadycb+tq.fadrqcb+tq.gongrmcb+tq.gongrycb+tq.gongrrqcb)*10000/sum(tq.fadmzbml+tq.fadyzbzml+tq.fadqzbzml+tq.gongrmzbml+tq.gongryzbzml+tq.gongrqzbzml),2)) as biaomdj_tq,\n" + 
					"                   decode(sum(tq.fadmzbml+tq.gongrmzbml),0,0, Round_New(sum(tq.fadmcb+tq.gongrmcb)*10000/ sum(tq.fadmzbml+tq.gongrmzbml),2)) as meizbmdj_tq,\n" + 
					"                   decode(sum(tq.fadyzbzml+tq.gongryzbzml),0,0, Round_New(sum(tq.fadycb+tq.gongrycb)*10000/ sum(tq.fadyzbzml+tq.gongryzbzml),2)) as meizbmdj_tq,\n" + 
					"                   sum(tq.fadl) as fadl_bq,\n" + 
					"                   decode(sum(tq.fadl- tq.fadcgdl) ,0,0,Round_New(sum(tq.fadmzbml+tq.fadyzbzml+tq.fadqzbzml)*100/sum(tq.fadl- tq.fadcgdl),2)) as fadbzmhl_tq,\n" + 
					"                   sum(tq.gongrl) as gongrl_bq,\n" + 
					"                   decode(sum(tq.gongrl),0,0, Round_New(sum(tq.gongrmzbml+tq.gongryzbzml+tq.gongrqzbzml)*1000/ sum(tq.gongrl),2)) as gongrbzmhl_tq,\n" + 
					"                   (decode(sum(tq.laimsl),0,0,Round_New(sum(tq.rc_farl*1000*tq.laimsl)/sum(tq.laimsl),2))-\n" + 
					"                          decode(sum(tq.zonghtrml),0,0,Round_New(sum(tq.zonghrlrz*1000*tq.laimsl)/sum(tq.zonghtrml),2))) as rucrlrzc_tq,\n" + 
					"                   sum(tq.rijkc) as rijkc_tq,\n" + 
					"                   (sum(bq.chunrml+bq.zonghtrml)-sum(tq.chunrml+tq.zonghtrml)) as tianrml_bj,\n" + 
					"                   (decode(sum(bq.zonghtrml),0,0,Round_New(sum(bq.zonghrlrz*bq.laimsl)/sum(bq.zonghtrml),2))-\n" + 
					"                          decode(sum(tq.zonghtrml),0,0,Round_New(sum(tq.zonghrlrz*tq.laimsl)/sum(tq.zonghtrml),2))) as rez_bj,\n" + 
					"                   (decode(sum(bq.chunrml+bq.youzml),0,0,Round_New(sum(bq.fadmcb+bq.fadycb+bq.gongrmcb+bq.gongrycb)*10000/sum(bq.chunrml+bq.youzml),2))-\n" + 
					"                          decode(sum(tq.chunrml+tq.youzml),0,0,Round_New(sum(tq.fadmcb+tq.fadycb+tq.gongrmcb+tq.gongrycb)*10000/sum(tq.chunrml+tq.youzml),2))) as tianrmdj_bj,\n" + 
					"                   (decode(sum(bq.fadmzbml+bq.fadyzbzml+bq.fadqzbzml+bq.gongrmzbml+bq.gongryzbzml+bq.gongrqzbzml) ,0,0,\n" + 
					"                            Round_New(sum(bq.fadmcb+bq.fadycb+bq.fadrqcb+bq.gongrmcb+bq.gongrycb+bq.gongrrqcb)*10000/sum(bq.fadmzbml+bq.fadyzbzml+bq.fadqzbzml+bq.gongrmzbml+bq.gongryzbzml+bq.gongrqzbzml),2))-\n" + 
					"                       decode(sum(tq.fadmzbml+tq.fadyzbzml+tq.fadqzbzml+tq.gongrmzbml+tq.gongryzbzml+tq.gongrqzbzml) ,0,0,\n" + 
					"                                  Round_New(sum(tq.fadmcb+tq.fadycb+tq.fadrqcb+tq.gongrmcb+tq.gongrycb+tq.gongrrqcb)*10000/sum(tq.fadmzbml+tq.fadyzbzml+tq.fadqzbzml+tq.gongrmzbml+tq.gongryzbzml+tq.gongrqzbzml),2))) as biaomdj_bj,\n" + 
					"                   (sum(bq.fadl)-sum(tq.fadl)) as fadl_bj,\n" + 
					"                   (sum(bq.gongrl)- sum(tq.gongrl)) as gongrl_bj,\n" + 
					"                   ( (decode(sum(bq.laimsl),0,0,Round_New(sum(bq.rc_farl*1000*bq.laimsl)/sum(bq.laimsl),2))-\n" + 
					"                          decode(sum(bq.zonghtrml),0,0,Round_New(sum(bq.zonghrlrz*1000*bq.laimsl)/sum(bq.zonghtrml),2)))-\n" + 
					"                     (decode(sum(tq.laimsl),0,0,Round_New(sum(tq.rc_farl*1000*tq.laimsl)/sum(tq.laimsl),2))-\n" + 
					"                          decode(sum(tq.zonghtrml),0,0,Round_New(sum(tq.zonghrlrz*1000*tq.laimsl)/sum(tq.zonghtrml),2)))) as rucrlrzc_bj\n" + 
					"      from ( select diancxxb_id,fx.fenx,fx.xuh from\n" + 
					"                   (select distinct diancxxb_id from yueslb sl,yuezlb zl,yuetjkjb tj\n" + 
					"                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
					"                    union\n" + 
					"                     select distinct diancxxb_id from yuezbb\n" + 
					"                        where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
					"                    ) yid,\n" + 
					"               (select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计') as fenx,2 as xhu from dual ) fx,diancxxb dc\n" + 
					"               where dc.id=yid.diancxxb_id  "+strGongsID+"  ) fx,\n" + 
					"\n" + 
					"      (select  decode(1,1,'本月') as fenx,yzb.diancxxb_id as diancxxb_id,\n" + 
					"                           yzb.chunrml as chunrml,yzb.youzml as youzml,yzb.zonghtrml as zonghtrml, yzb.zonghrlrz as zonghrlrz,yzb.ranlzcb as ranlzcb,\n" + 
					"                           yzb.zonghbml as zonghbml,yzb.fadmzbml as fadmzbml,yzb.fadyzbzml as fadyzbzml,yzb.fadqzbzml as fadqzbzml,\n" + 
					"                           yzb.gongrmzbml as gongrmzbml,yzb.gongryzbzml as gongryzbzml,yzb.gongrqzbzml as gongrqzbzml,\n" + 
					"                           yzb.fadmcb as fadmcb,yzb.gongrmcb as gongrmcb,yzb.fadrqcb as fadrqcb,yzb.gongrrqcb as gongrrqcb,yzb.fadycb as fadycb,\n" + 
					"                           yzb.gongrycb as gongrycb,yzb.gongrcydftrlf as gongrcydftrlf,yzb.qiz_ranm as qiz_ranm,\n" + 
					"                           yzb.qiz_rany as qiz_rany,yzb.rulmzbzml as rulmzbzml,yzb.rulyzbzml as rulyzbzml,yzb.fadl as fadl,yzb.fadcgdl as fadcgdl,\n" + 
					"                           (yzb.fadmzbml+ yzb.fadyzbzml+ yzb.fadqzbzml) as zonghfdmh,yzb.meihcb as meihcb,yzb.gongrl as gongrl,\n" + 
					"                           (yzb.gongrmzbml+ yzb.gongryzbzml+ yzb.gongrqzbzml) as zonghfdmh,\n" + 
					"                           yzl.laimsl,yzl.rcfal as rc_farl,decode(tians,0,0,Round_New(rikc.kuc/tians,0)) as rijkc\n" + 
					"              from (select zb.diancxxb_id,\n" + 
					"							  sum(nvl(zb.FADGRYTRML,0)) as chunrml,sum(nvl(zb.fadgrytryl,0)*2) as youzml,\n" +
					"                             sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)) as zonghtrml,\n" + 
					"                             decode(sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)),0,0,\n" + 
					"                                   (sum(nvl(zb.fadgrytrml,0)*nvl(zb.rultrmpjfrl,0)/1000)+sum(nvl(zb.fadgrytryl,0)*nvl(zb.rultrypjfrl,0)/1000)+sum(nvl(zb.fadgrytrql,0)*nvl(zb.rultrqpjfrl,0)/1000))/sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0))) as zonghrlrz,--综合燃料热值\n" + 
					"                             sum(nvl(zb.ranlcb_bhs,0)) as ranlzcb,---燃料总成本\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)+nvl(zb.rulyzbzml,0)+nvl(zb.rulqzbzml,0)) as zonghbml,--综合标煤量\n" + 
					"                             sum(nvl(zb.fadmzbml,0)) as fadmzbml,\n" + 
					"                             sum(nvl(zb.fadyzbzml,0)) as fadyzbzml,\n" + 
					"                             sum(nvl(zb.fadqzbzml,0)) as fadqzbzml,\n" + 
					"                             sum(nvl(zb.gongrmzbml,0)) as gongrmzbml,\n" + 
					"                             sum(nvl(zb.gongryzbzml,0)) as gongryzbzml,\n" + 
					"                             sum(nvl(zb.gongrqzbzml,0)) as gongrqzbzml,\n" + 
					"                             sum(nvl(zb.fadmcb,0)) as fadmcb,--发电煤成本\n" + 
					"                             sum(nvl(zb.gongrmcb,0)) as gongrmcb,--供热煤成本\n" + 
					"                             sum(nvl(zb.fadrqcb,0)) as fadrqcb,\n" + 
					"                             sum(nvl(zb.gongrrqcb,0)) as gongrrqcb,\n" + 
					"                             sum(nvl(zb.fadycb,0)) as fadycb,--发电油成本\n" + 
					"                             sum(nvl(zb.gongrycb,0)) as gongrycb,--供热油成本\n" + 
					"                             sum(nvl(zb.gongrcydftrlf,0)) as gongrcydftrlf,\n" + 
					"                             sum(nvl(zb.qiz_ranm,0)) as qiz_ranm,\n" + 
					"                             sum(nvl(zb.qiz_rany,0)) as qiz_rany,\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)) as rulmzbzml,--入炉煤折标煤量\n" + 
					"                             sum(nvl(zb.rulyzbzml,0)) as rulyzbzml,--入炉油折标煤量\n" + 
					"                             sum(nvl(zb.fadl,0)) as fadl,--发电量\n" + 
					"                             sum(nvl(zb.fadcgdl,0)) as fadcgdl,--发电厂供电量\n" + 
					"                             sum(nvl(zb.FADMZBML,0)+nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0)) as zonghfdmh,--综合发电煤耗量(吨)\n" + 
					"                             sum(nvl(zb.FADL,0)- nvl(zb.FADCGDL,0)) as meihcb,--煤耗发电量（万千瓦时）\n" + 
					"                             sum(nvl(zb.GONGRL,0)) as gongrl,--供热量\n" + 
					"                             sum(nvl(zb.GONGRMZBML,0)+ nvl(zb.GONGRYZBZML,0)+ nvl(zb.GONGRQZBZML,0)) as zonghfdmh--综合供热煤耗量(吨)\n"+
					"                          from yuezbb zb,diancxxb dc\n" + 
					"                          where zb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and zb.fenx='本月'and zb.diancxxb_id=dc.id  "+strGongsID+"\n" + 
					"                        group by ( zb.diancxxb_id) )yzb,\n" + 
					"                (select tj.diancxxb_id as diancxxb_id,decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rcfal, sum(sl.laimsl) as laimsl\n" + 
					"                         from yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
					"                         where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id\n" + 
					"                               and sl.fenx = '本月' and zl.fenx(+)=sl.fenx "+strGongsID+"\n" + 
					"                         group by (tj.diancxxb_id)  )yzl,\n" + 
					"             \t( select shc.diancxxb_id as diancxxb_id,sum(shc.kuc) as kuc,daycount(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) as tians\n" + 
					"                                     from shouhcrbb shc ,diancxxb dc where shc.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
					"                                     and shc.diancxxb_id=dc.id   "+strGongsID+"\n" + 
					"                         group by (shc.diancxxb_id) )rikc\n" + 
					"               where yzb.diancxxb_id=yzl.diancxxb_id(+) and yzb.diancxxb_id=rikc.diancxxb_id(+)\n" + 
					"          union\n" + 
					"           select  decode(1,1,'累计') as fenx,yzb.diancxxb_id as diancxxb_id,\n" + 
					"                           yzb.chunrml as chunrml,yzb.youzml as youzml,yzb.zonghtrml as zonghtrml, yzb.zonghrlrz as zonghrlrz,yzb.ranlzcb as ranlzcb,\n" + 
					"                           yzb.zonghbml as zonghbml,yzb.fadmzbml as fadmzbml,yzb.fadyzbzml as fadyzbzml,yzb.fadqzbzml as fadqzbzml,\n" + 
					"                           yzb.gongrmzbml as gongrmzbml,yzb.gongryzbzml as gongryzbzml,yzb.gongrqzbzml as gongrqzbzml,\n" + 
					"                           yzb.fadmcb as fadmcb,yzb.gongrmcb as gongrmcb,yzb.fadrqcb as fadrqcb,yzb.gongrrqcb as gongrrqcb,yzb.fadycb as fadycb,\n" + 
					"                           yzb.gongrycb as gongrycb,yzb.gongrcydftrlf as gongrcydftrlf,yzb.qiz_ranm as qiz_ranm,\n" + 
					"                           yzb.qiz_rany as qiz_rany,yzb.rulmzbzml as rulmzbzml,yzb.rulyzbzml as rulyzbzml,yzb.fadl as fadl,yzb.fadcgdl as fadcgdl,\n" + 
					"                           (yzb.fadmzbml+ yzb.fadyzbzml+ yzb.fadqzbzml) as zonghfdmh,yzb.meihcb as meihcb,yzb.gongrl as gongrl,\n" + 
					"                           (yzb.gongrmzbml+ yzb.gongryzbzml+ yzb.gongrqzbzml) as zonghfdmh,\n" + 
					"                           yzl.laimsl,yzl.rcfal as rc_farl,decode(tians,0,0,Round_New(rikc.kuc/tians,0)) as rijkc\n" + 
					"              from (select zb.diancxxb_id,\n" + 
					"							  sum(nvl(zb.FADGRYTRML,0)) as chunrml,sum(nvl(zb.fadgrytryl,0)*2) as youzml,\n" +
					"                             sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)) as zonghtrml,\n" + 
					"                             decode(sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)),0,0,\n" + 
					"                                   (sum(nvl(zb.fadgrytrml,0)*nvl(zb.rultrmpjfrl,0)/1000)+sum(nvl(zb.fadgrytryl,0)*nvl(zb.rultrypjfrl,0)/1000)+sum(nvl(zb.fadgrytrql,0)*nvl(zb.rultrqpjfrl,0)/1000))/sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0))) as zonghrlrz,--综合燃料热值\n" + 
					"                             sum(nvl(zb.ranlcb_bhs,0)) as ranlzcb,---燃料总成本\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)+nvl(zb.rulyzbzml,0)+nvl(zb.rulqzbzml,0)) as zonghbml,--综合标煤量\n" + 
					"                             sum(nvl(zb.fadmzbml,0)) as fadmzbml,\n" + 
					"                             sum(nvl(zb.fadyzbzml,0)) as fadyzbzml,\n" + 
					"                             sum(nvl(zb.fadqzbzml,0)) as fadqzbzml,\n" + 
					"                             sum(nvl(zb.gongrmzbml,0)) as gongrmzbml,\n" + 
					"                             sum(nvl(zb.gongryzbzml,0)) as gongryzbzml,\n" + 
					"                             sum(nvl(zb.gongrqzbzml,0)) as gongrqzbzml,\n" + 
					"                             sum(nvl(zb.fadmcb,0)) as fadmcb,--发电煤成本\n" + 
					"                             sum(nvl(zb.gongrmcb,0)) as gongrmcb,--供热煤成本\n" + 
					"                             sum(nvl(zb.fadrqcb,0)) as fadrqcb,\n" + 
					"                             sum(nvl(zb.gongrrqcb,0)) as gongrrqcb,\n" + 
					"                             sum(nvl(zb.fadycb,0)) as fadycb,--发电油成本\n" + 
					"                             sum(nvl(zb.gongrycb,0)) as gongrycb,--供热油成本\n" + 
					"                             sum(nvl(zb.gongrcydftrlf,0)) as gongrcydftrlf,\n" + 
					"                             sum(nvl(zb.qiz_ranm,0)) as qiz_ranm,\n" + 
					"                             sum(nvl(zb.qiz_rany,0)) as qiz_rany,\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)) as rulmzbzml,--入炉煤折标煤量\n" + 
					"                             sum(nvl(zb.rulyzbzml,0)) as rulyzbzml,--入炉油折标煤量\n" + 
					"                             sum(nvl(zb.fadl,0)) as fadl,--发电量\n" + 
					"                             sum(nvl(zb.fadcgdl,0)) as fadcgdl,--发电厂供电量\n" + 
					"                             sum(nvl(zb.FADMZBML,0)+nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0)) as zonghfdmh,--综合发电煤耗量(吨)\n" + 
					"                             sum(nvl(zb.FADL,0)- nvl(zb.FADCGDL,0)) as meihcb,--煤耗发电量（万千瓦时）\n" + 
					"                             sum(nvl(zb.GONGRL,0)) as gongrl,--供热量\n" + 
					"                             sum(nvl(zb.GONGRMZBML,0)+ nvl(zb.GONGRYZBZML,0)+ nvl(zb.GONGRQZBZML,0)) as zonghfdmh--综合供热煤耗量(吨)\n"+
					"                          from yuezbb zb,diancxxb dc\n" + 
					"                          where zb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and zb.fenx='累计'and zb.diancxxb_id=dc.id  "+strGongsID+"\n" + 
					"                        group by ( zb.diancxxb_id) )yzb,\n" + 
					"                (select tj.diancxxb_id as diancxxb_id,decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rcfal, sum(sl.laimsl) as laimsl\n" + 
					"                         from yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
					"                         where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id\n" + 
					"                               and sl.fenx = '累计' and zl.fenx(+)=sl.fenx "+strGongsID+"\n" + 
					"                         group by (tj.diancxxb_id)  )yzl,\n" + 
					"             \t( select shc.diancxxb_id as diancxxb_id,sum(shc.kuc) as kuc,(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')-to_date('"+intyear+"-1-01','yyyy-mm-dd')) as tians\n" + 
					"                                     from shouhcrbb shc ,diancxxb dc where shc.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and shc.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
					"                                     and shc.diancxxb_id=dc.id   "+strGongsID+"\n" + 
					"                         group by (shc.diancxxb_id) )rikc\n" + 
					"               where yzb.diancxxb_id=yzl.diancxxb_id(+) and yzb.diancxxb_id=rikc.diancxxb_id(+)\n" + 
					"           )bq,\n" + 
					"            (select  decode(1,1,'本月') as fenx,yzb.diancxxb_id as diancxxb_id,\n" + 
					"                           yzb.chunrml as chunrml,yzb.youzml as youzml,yzb.zonghtrml as zonghtrml, yzb.zonghrlrz as zonghrlrz,yzb.ranlzcb as ranlzcb,\n" + 
					"                           yzb.zonghbml as zonghbml,yzb.fadmzbml as fadmzbml,yzb.fadyzbzml as fadyzbzml,yzb.fadqzbzml as fadqzbzml,\n" + 
					"                           yzb.gongrmzbml as gongrmzbml,yzb.gongryzbzml as gongryzbzml,yzb.gongrqzbzml as gongrqzbzml,\n" + 
					"                           yzb.fadmcb as fadmcb,yzb.gongrmcb as gongrmcb,yzb.fadrqcb as fadrqcb,yzb.gongrrqcb as gongrrqcb,yzb.fadycb as fadycb,\n" + 
					"                           yzb.gongrycb as gongrycb,yzb.gongrcydftrlf as gongrcydftrlf,yzb.qiz_ranm as qiz_ranm,\n" + 
					"                           yzb.qiz_rany as qiz_rany,yzb.rulmzbzml as rulmzbzml,yzb.rulyzbzml as rulyzbzml,yzb.fadl as fadl,yzb.fadcgdl as fadcgdl,\n" + 
					"                           (yzb.fadmzbml+ yzb.fadyzbzml+ yzb.fadqzbzml) as zonghfdmh,yzb.meihcb as meihcb,yzb.gongrl as gongrl,\n" + 
					"                           (yzb.gongrmzbml+ yzb.gongryzbzml+ yzb.gongrqzbzml) as zonghfdmh,\n" + 
					"                           yzl.laimsl,yzl.rcfal as rc_farl,decode(tians,0,0,Round_New(rikc.kuc/tians,0)) as rijkc\n" + 
					"              from (select zb.diancxxb_id,\n" + 
					"							  sum(nvl(zb.FADGRYTRML,0)) as chunrml,sum(nvl(zb.fadgrytryl,0)*2) as youzml,\n" +
					"                             sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)) as zonghtrml,\n" + 
					"                             decode(sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)),0,0,\n" + 
					"                                   (sum(nvl(zb.fadgrytrml,0)*nvl(zb.rultrmpjfrl,0)/1000)+sum(nvl(zb.fadgrytryl,0)*nvl(zb.rultrypjfrl,0)/1000)+sum(nvl(zb.fadgrytrql,0)*nvl(zb.rultrqpjfrl,0)/1000))/sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0))) as zonghrlrz,--综合燃料热值\n" + 
					"                             sum(nvl(zb.ranlcb_bhs,0)) as ranlzcb,---燃料总成本\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)+nvl(zb.rulyzbzml,0)+nvl(zb.rulqzbzml,0)) as zonghbml,--综合标煤量\n" + 
					"                             sum(nvl(zb.fadmzbml,0)) as fadmzbml,\n" + 
					"                             sum(nvl(zb.fadyzbzml,0)) as fadyzbzml,\n" + 
					"                             sum(nvl(zb.fadqzbzml,0)) as fadqzbzml,\n" + 
					"                             sum(nvl(zb.gongrmzbml,0)) as gongrmzbml,\n" + 
					"                             sum(nvl(zb.gongryzbzml,0)) as gongryzbzml,\n" + 
					"                             sum(nvl(zb.gongrqzbzml,0)) as gongrqzbzml,\n" + 
					"                             sum(nvl(zb.fadmcb,0)) as fadmcb,--发电煤成本\n" + 
					"                             sum(nvl(zb.gongrmcb,0)) as gongrmcb,--供热煤成本\n" + 
					"                             sum(nvl(zb.fadrqcb,0)) as fadrqcb,\n" + 
					"                             sum(nvl(zb.gongrrqcb,0)) as gongrrqcb,\n" + 
					"                             sum(nvl(zb.fadycb,0)) as fadycb,--发电油成本\n" + 
					"                             sum(nvl(zb.gongrycb,0)) as gongrycb,--供热油成本\n" + 
					"                             sum(nvl(zb.gongrcydftrlf,0)) as gongrcydftrlf,\n" + 
					"                             sum(nvl(zb.qiz_ranm,0)) as qiz_ranm,\n" + 
					"                             sum(nvl(zb.qiz_rany,0)) as qiz_rany,\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)) as rulmzbzml,--入炉煤折标煤量\n" + 
					"                             sum(nvl(zb.rulyzbzml,0)) as rulyzbzml,--入炉油折标煤量\n" + 
					"                             sum(nvl(zb.fadl,0)) as fadl,--发电量\n" + 
					"                             sum(nvl(zb.fadcgdl,0)) as fadcgdl,--发电厂供电量\n" + 
					"                             sum(nvl(zb.FADMZBML,0)+nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0)) as zonghfdmh,--综合发电煤耗量(吨)\n" + 
					"                             sum(nvl(zb.FADL,0)- nvl(zb.FADCGDL,0)) as meihcb,--煤耗发电量（万千瓦时）\n" + 
					"                             sum(nvl(zb.GONGRL,0)) as gongrl,--供热量\n" + 
					"                             sum(nvl(zb.GONGRMZBML,0)+ nvl(zb.GONGRYZBZML,0)+ nvl(zb.GONGRQZBZML,0)) as zonghfdmh--综合供热煤耗量(吨)\n"+
					"                          from yuezbb zb,diancxxb dc\n" + 
					"                          where zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and zb.fenx='本月'and zb.diancxxb_id=dc.id "+strGongsID+"\n" + 
					"                        group by ( zb.diancxxb_id) )yzb,\n" + 
					"                (select tj.diancxxb_id as diancxxb_id,decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rcfal, sum(sl.laimsl) as laimsl\n" + 
					"                         from yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
					"                         where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id\n" + 
					"                               and sl.fenx = '本月' and zl.fenx(+)=sl.fenx "+strGongsID+"\n" + 
					"                         group by (tj.diancxxb_id)  )yzl,\n" + 
					"             \t( select shc.diancxxb_id as diancxxb_id,sum(shc.kuc) as kuc,daycount(add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)) as tians\n" + 
					"                                     from shouhcrbb shc ,diancxxb dc where shc.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
					"                                     and shc.diancxxb_id=dc.id   "+strGongsID+"\n" + 
					"                         group by (shc.diancxxb_id) )rikc\n" + 
					"               where yzb.diancxxb_id=yzl.diancxxb_id(+) and yzb.diancxxb_id=rikc.diancxxb_id(+)\n" + 
					"          union\n" + 
					"           select  decode(1,1,'累计') as fenx,yzb.diancxxb_id as diancxxb_id,\n" + 
					"                           yzb.chunrml as chunrml,yzb.youzml as youzml,yzb.zonghtrml as zonghtrml, yzb.zonghrlrz as zonghrlrz,yzb.ranlzcb as ranlzcb,\n" + 
					"                           yzb.zonghbml as zonghbml,yzb.fadmzbml as fadmzbml,yzb.fadyzbzml as fadyzbzml,yzb.fadqzbzml as fadqzbzml,\n" + 
					"                           yzb.gongrmzbml as gongrmzbml,yzb.gongryzbzml as gongryzbzml,yzb.gongrqzbzml as gongrqzbzml,\n" + 
					"                           yzb.fadmcb as fadmcb,yzb.gongrmcb as gongrmcb,yzb.fadrqcb as fadrqcb,yzb.gongrrqcb as gongrrqcb,yzb.fadycb as fadycb,\n" + 
					"                           yzb.gongrycb as gongrycb,yzb.gongrcydftrlf as gongrcydftrlf,yzb.qiz_ranm as qiz_ranm,\n" + 
					"                           yzb.qiz_rany as qiz_rany,yzb.rulmzbzml as rulmzbzml,yzb.rulyzbzml as rulyzbzml,yzb.fadl as fadl,yzb.fadcgdl as fadcgdl,\n" + 
					"                           (yzb.fadmzbml+ yzb.fadyzbzml+ yzb.fadqzbzml) as zonghfdmh,yzb.meihcb as meihcb,yzb.gongrl as gongrl,\n" + 
					"                           (yzb.gongrmzbml+ yzb.gongryzbzml+ yzb.gongrqzbzml) as zonghfdmh,\n" + 
					"                           yzl.laimsl,yzl.rcfal as rc_farl,decode(tians,0,0,Round_New(rikc.kuc/tians,0)) as rijkc\n" + 
					"              from (select zb.diancxxb_id,\n" + 
					"							  sum(nvl(zb.FADGRYTRML,0)) as chunrml,sum(nvl(zb.fadgrytryl,0)*2) as youzml,\n" +
					"                             sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)) as zonghtrml,\n" + 
					"                             decode(sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0)),0,0,\n" + 
					"                                   (sum(nvl(zb.fadgrytrml,0)*nvl(zb.rultrmpjfrl,0)/1000)+sum(nvl(zb.fadgrytryl,0)*nvl(zb.rultrypjfrl,0)/1000)+sum(nvl(zb.fadgrytrql,0)*nvl(zb.rultrqpjfrl,0)/1000))/sum(nvl(zb.fadgrytryl,0)+nvl(zb.fadgrytrml,0)+nvl(zb.fadgrytrql,0))) as zonghrlrz,--综合燃料热值\n" + 
					"                             sum(nvl(zb.ranlcb_bhs,0)) as ranlzcb,---燃料总成本\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)+nvl(zb.rulyzbzml,0)+nvl(zb.rulqzbzml,0)) as zonghbml,--综合标煤量\n" + 
					"                             sum(nvl(zb.fadmzbml,0)) as fadmzbml,\n" + 
					"                             sum(nvl(zb.fadyzbzml,0)) as fadyzbzml,\n" + 
					"                             sum(nvl(zb.fadqzbzml,0)) as fadqzbzml,\n" + 
					"                             sum(nvl(zb.gongrmzbml,0)) as gongrmzbml,\n" + 
					"                             sum(nvl(zb.gongryzbzml,0)) as gongryzbzml,\n" + 
					"                             sum(nvl(zb.gongrqzbzml,0)) as gongrqzbzml,\n" + 
					"                             sum(nvl(zb.fadmcb,0)) as fadmcb,--发电煤成本\n" + 
					"                             sum(nvl(zb.gongrmcb,0)) as gongrmcb,--供热煤成本\n" + 
					"                             sum(nvl(zb.fadrqcb,0)) as fadrqcb,\n" + 
					"                             sum(nvl(zb.gongrrqcb,0)) as gongrrqcb,\n" + 
					"                             sum(nvl(zb.fadycb,0)) as fadycb,--发电油成本\n" + 
					"                             sum(nvl(zb.gongrycb,0)) as gongrycb,--供热油成本\n" + 
					"                             sum(nvl(zb.gongrcydftrlf,0)) as gongrcydftrlf,\n" + 
					"                             sum(nvl(zb.qiz_ranm,0)) as qiz_ranm,\n" + 
					"                             sum(nvl(zb.qiz_rany,0)) as qiz_rany,\n" + 
					"                             sum(nvl(zb.RULMZBZML,0)) as rulmzbzml,--入炉煤折标煤量\n" + 
					"                             sum(nvl(zb.rulyzbzml,0)) as rulyzbzml,--入炉油折标煤量\n" + 
					"                             sum(nvl(zb.fadl,0)) as fadl,--发电量\n" + 
					"                             sum(nvl(zb.fadcgdl,0)) as fadcgdl,--发电厂供电量\n" + 
					"                             sum(nvl(zb.FADMZBML,0)+nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0)) as zonghfdmh,--综合发电煤耗量(吨)\n" + 
					"                             sum(nvl(zb.FADL,0)- nvl(zb.FADCGDL,0)) as meihcb,--煤耗发电量（万千瓦时）\n" + 
					"                             sum(nvl(zb.GONGRL,0)) as gongrl,--供热量\n" + 
					"                             sum(nvl(zb.GONGRMZBML,0)+ nvl(zb.GONGRYZBZML,0)+ nvl(zb.GONGRQZBZML,0)) as zonghfdmh--综合供热煤耗量(吨)\n"+
					"                          from yuezbb zb,diancxxb dc\n" + 
					"                          where zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and zb.fenx='累计'and zb.diancxxb_id=dc.id  "+strGongsID+"\n" + 
					"                        group by ( zb.diancxxb_id) )yzb,\n" + 
					"                (select tj.diancxxb_id as diancxxb_id,decode(sum(sl.laimsl), 0, 0, sum(sl.laimsl * zl.qnet_ar) / sum(sl.laimsl)) as rcfal, sum(sl.laimsl) as laimsl\n" + 
					"                         from yueslb sl, yuetjkjb tj, yuezlb zl,diancxxb dc\n" + 
					"                         where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id\n" + 
					"                               and sl.fenx = '累计' and zl.fenx(+)=sl.fenx "+strGongsID+"\n" + 
					"                         group by (tj.diancxxb_id)  )yzl,\n" + 
					"             \t( select shc.diancxxb_id as diancxxb_id,sum(shc.kuc) as kuc,(add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)-add_months(to_date('"+intyear+"-1-01','yyyy-mm-dd'),-12)) as tians\n" + 
					"                                     from shouhcrbb shc ,diancxxb dc where shc.riq>=add_months(to_date('"+intyear+"-1-01','yyyy-mm-dd'),-12) and shc.riq<=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
					"                                     and shc.diancxxb_id=dc.id   "+strGongsID+"\n" + 
					"                         group by (shc.diancxxb_id) )rikc\n" + 
					"               where yzb.diancxxb_id=yzl.diancxxb_id(+) and yzb.diancxxb_id=rikc.diancxxb_id(+)\n" + 
					"           )tq, vwdianc dc\n" + 
					"            where dc.id=fx.diancxxb_id\n" + 
					"                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
					"                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
					"                 and fx.fenx=tq.fenx(+)\n" + 
					"                 and fx.fenx=bq.fenx(+)\n" + 
					" group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" + 
					"having not grouping(fx.fenx)=1\n" + 
					"order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
					"grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)\n" ;
			
// 直属分厂汇总 
				ArrHeader=new String[5][43];
				ArrHeader[0]=new String[] {"单位名称","本月或累计","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","当    年    实    际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","上年同期实际","本期实际较上年同期实际","本期实际较上年同期实际","本期实际较上年同期实际","本期实际较上年同期实际","本期实际较上年同期实际","本期实际较上年同期实际","本期实际较上年同期实际"};
				ArrHeader[1]=new String[] {"单位名称","本月或累计","天然煤量","其中：","其中：","综合燃料热值","燃料总费用","天然煤单价","其中：","其中：","标煤单价","其中：","其中：","发电量","发电标准煤耗率","供热量","供热标准煤耗率","入厂入炉煤热值差","日均库存","天然煤量","其中：","其中：","综合燃料热值","燃料总费用","天然煤单价","其中：","其中：","标煤单价","其中：","其中：","发电量","发电标准煤耗率","供热量","供热标准煤耗率","入厂入炉煤热值差","日均库存","天然煤量","热值","天然煤单价","标煤单价","发电量","供热量","入厂入炉煤热值差"};
				ArrHeader[2]=new String[] {"单位名称","本月或累计","天然煤量","纯然煤量","油折煤量","综合燃料热值","燃料总费用","天然煤单价","纯煤单价","油折单价","标煤单价","煤折","油折","发电量","发电标准煤耗率","供热量","供热标准煤耗率","入厂入炉煤热值差","日均库存","天然煤量","纯然煤量","油折煤量","综合燃料热值","燃料总费用","天然煤单价","纯煤单价","油折单价","标煤单价","煤折","油折","发电量","发电标准煤耗率","供热量","供热标准煤耗率","入厂入炉煤热值差","日均库存","天然煤量","热值","天然煤单价","标煤单价","发电量","供热量","入厂入炉煤热值差"};
				ArrHeader[3]=new String[] {"单位名称","本月或累计","吨","吨","吨","MJ/Kg","元","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","万千瓦时","克/千瓦时","吉焦","千克/吉焦","KJ/Kg","吨","吨","吨","吨","MJ/Kg","元","元/吨","元/吨","元/吨","元/吨","元/吨","元/吨","万千瓦时","克/千瓦时","吉焦","千克/吉焦","KJ/Kg","吨","吨","MJ/Kg","元/吨","元/吨","万千瓦时","吉焦","KJ/Kg"};
				ArrHeader[4]=new String[] {"单位名称","本月或累计","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41"};

				ArrWidth=new int[] {150,40,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};


				 iFixedRows=1;
				 iCol=10;
				 
			
			
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
			Table tb=new Table(rs,5, 0, 1);
			rt.setBody(tb);
			
			
			rt.setTitle(getBiaotmc()+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2, "报送单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			
			rt.setDefaultTitle(20, 2, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_RIGHT);

			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			
			tb.setColAlign(2, Table.ALIGN_LEFT);
//			tb.setColAlign(3, Table.ALIGN_LEFT);
//			tb.setColAlign(4, Table.ALIGN_LEFT);
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(4,3,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(17,3,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(24,4,"发送日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
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
				if (_yuef == ((IDropDownBean) obj)
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