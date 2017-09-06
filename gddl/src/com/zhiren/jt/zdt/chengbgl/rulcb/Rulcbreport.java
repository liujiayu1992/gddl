package com.zhiren.jt.zdt.chengbgl.rulcb;

import org.apache.tapestry.html.BasePage;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
/* 
* 时间：2009-05-4
* 作者： ll
* 修改内容：1、修改查询sql中yuezbb表中字段的名称,按照yuezbb中新的公式，取新的字段名。
* 		   
*/ 
/* 
* 时间：2009-06-1
* 作者： ll
* 修改内容：1、修改报表中的单位名称，千焦/千克改为兆焦/千克，元/千瓦时改为元/千千瓦时，元/千焦改为元/吉焦
* 		   2、修改查询sql中公式，按照财务生产数据中计算公式修改。	   
*/ 
/* 
* 时间：2009-07-20
* 作者： ll
* 修改内容：修改入厂、入炉对比中发电量和供电量按照万千瓦时的单位进行计算。  
*/ 
/* 
* 时间：2009-08-5
* 作者： ll
* 修改内容：1、按二级公司登陆时统计口径默认“按公司统计”；
*          2、二级公司登陆时去“总计”行。
* 		   
*/ 
/* 
* 时间：2009-08-29
* 作者： ll
* 修改内容：入炉煤标煤单价和入炉油标煤单价查询页面，中的热值增加大卡显示。
* 		   
*/ 
/* 
* 时间：2009-09-4
* 作者： ll
* 修改内容：1、二级公司登陆时去“总计”行。
* 		   
*/
/* 
* 时间：2009-09-8
* 作者： ll
* 修改内容：1、入炉热值单位mj要求保留两位小数.
* 			2、修改电厂排序错误。
* 		   
*/
/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Rulcbreport extends BasePage {
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
	
//	 年份
private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
		
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean3()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(_value);
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
	
	private String REPORT_RULCBREPORT="rulcbreport";
	private String REPORT_RUCRLBMDJBJ="rucrlbmdjbj";//入厂入炉标煤单价比较
	private String mstrReportName="";
	
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
	private boolean isBegin=false;
	public String getPrintTable(){
		
		if(! isBegin){
			return "";
		}
		
		if(mstrReportName.equals(REPORT_RULCBREPORT)){
			if(getBaoblxValue().getValue().equals("入炉煤标煤单价")){
				
				return getRulmbmdj();
				
			}else if(getBaoblxValue().getValue().equals("入炉油标煤单价")){
				
				return getRulybmdj();
				
			}else if(getBaoblxValue().getValue().equals("入炉成本")){
				
				return getRulmcb();
				
			}else{
				return "无此报表";
			}
		}else if(mstrReportName.equals(REPORT_RUCRLBMDJBJ)){
				
			return getRucrlbmdjbj();
				
		}else{
			return "无此报表";
		}
	}

	public String getRulmbmdj(){//入炉煤标煤单价
		
		_CurrentPage=1;
		_AllPages=1;
		
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
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		String guolzj="";
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//集团用户
			if (treejib == 1) {// 选集团
				str = "";
				
			} else if (treejib == 2) {// 选分公司
				str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ getTreeid() + ") ";
			} else if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
			}
		}else if(isGongsUser()){//分公司用户
			str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			guolzj=" and grouping(gs.mingc)=0\n";//分公司查看报表时过滤总计。
			if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				guolzj=" and grouping(dc.mingc)=0\n";
			}
		}else if(isDiancUser()){//电厂用户
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
			guolzj=" and grouping(dc.mingc)=0\n";
		}else{
			str = "";
		}
		
		String sql ="select a.danwmc,a.fenx,a.rulmlhj,a.fadhml,a.gongrhml,a.rulmrz,a.rulmrz_dk,a.fadtrmdj,a.gongrtrmdj,a.fadbmdj,a.gongrbmdj,a.fadryzbmdj,a.gongryzbmdj,       \n"
			    /*+"      Round(decode((a.fadhml+a.fadhy+a.gongrhml+a.gongrhy),0,0,(a.fadbmdj*a.fadhml+a.fadryzbmdj*a.fadhy+a.gongrbmdj*a.gongrhml+a.gongryzbmdj*a.gongrhy)/(a.fadhml+a.fadhy+a.gongrhml+a.gongrhy)),2) as zonghbmdjhj,     \n"
			    +"       Round(decode((a.fadhml+a.fadhy),0,0,(a.fadbmdj*a.fadhml+a.fadryzbmdj*a.fadhy)/(a.fadhml+a.fadhy)),2) as zonghbmdjfd,     \n"
				+"       Round(decode((a.gongrhml+a.gongrhy),0,0,(a.gongrbmdj*a.gongrhml+a.gongryzbmdj*a.gongrhy)/(a.gongrhml+a.gongrhy)),2) as zonghbmdjgr      \n"*/
				+"		 a.zonghbmdj,a.fadzhbmdj,a.gongrzhbmdj\n"
				+" from (   \n"
				+"select decode(grouping(dc.mingc)+grouping(gs.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc) as danwmc,fx.fenx,\n"
				+ "       sum(nvl(jc.rulmlhj,0)) as rulmlhj,sum(nvl(fadhml,0)) as fadhml,sum(nvl(gongrhml,0)) as gongrhml,\n"
				+ "       decode(sum(nvl(jc.rulmlhj,0)),0,0,round(sum(nvl(jc.rulmlhj,0)*nvl(jc.rulmrz,0))/sum(nvl(jc.rulmlhj,0)),2)) as rulmrz,\n"
				+ "       round(decode(sum(nvl(jc.rulmlhj,0)),0,0,round(sum(nvl(jc.rulmlhj,0)*nvl(jc.rulmrz,0))/sum(nvl(jc.rulmlhj,0)),2))*1000/4.1816,0) as rulmrz_dk,\n"
				+ "       decode(sum(nvl(jc.fadhml,0)),0,0,round(sum(nvl(jc.fadhml,0)*nvl(jc.fadtrmdj,0))/sum(nvl(jc.fadhml,0)),2)) as fadtrmdj,\n"
				+ "       decode(sum(nvl(jc.gongrhml,0)),0,0,round(sum(nvl(jc.gongrhml,0)*nvl(jc.gongrtrmdj,0))/sum(nvl(jc.gongrhml,0)),2)) as gongrtrmdj,\n"
				+ "\n"
				/*+ "       decode(sum(nvl(jc.rulmlhj,0)),0,0,decode(round(sum(nvl(jc.rulmlhj,0)*nvl(jc.rulmrz,0))/sum(nvl(jc.rulmlhj,0)),3),0,0,\n"
				+ "             decode(sum(nvl(jc.fadhml,0)),0,0,\n"
				+ "             round(round(sum(nvl(jc.fadhml,0)*nvl(jc.fadtrmdj,0))/sum(nvl(jc.fadhml,0)),2)*29.271\n"
				+ "             /round(sum(nvl(jc.rulmlhj,0)*nvl(jc.rulmrz,0))/sum(nvl(jc.rulmlhj,0)),3),2)))) as fadbmdj,\n"
				+ "\n"
				+ "       decode(sum(nvl(jc.rulmlhj,0)),0,0,decode(round(sum(nvl(jc.rulmlhj,0)*nvl(jc.rulmrz,0))/sum(nvl(jc.rulmlhj,0)),3),0,0,\n"
				+ "             decode(sum(nvl(jc.gongrhml,0)),0,0,\n"
				+ "             round(round(sum(nvl(jc.gongrhml,0)*nvl(jc.gongrtrmdj,0))/sum(nvl(jc.gongrhml,0)),2)*29.271\n"
				+ "             /round(sum(nvl(jc.rulmlhj,0)*nvl(jc.rulmrz,0))/sum(nvl(jc.rulmlhj,0)),3),2)))) as gongrbmdj,\n"
				+ "\n"
				+ "       decode(sum(nvl(jc.fadhy,0)+nvl(jc.gongrhy,0)),0,0,decode(round(sum((nvl(jc.fadhy,0)+nvl(jc.gongrhy,0))*nvl(jc.rulyrz,0))/sum(nvl(jc.fadhy,0)+nvl(jc.gongrhy,0)),3),0,0,\n"
				+ "             decode(sum(nvl(jc.fadhy,0)),0,0,\n"
				+ "             round(round(sum(nvl(jc.fadhy,0)*nvl(jc.fadtrydj,0))/sum(nvl(jc.fadhy,0)),2)*29.271\n"
				+ "             /round(sum((nvl(jc.fadhy,0)+nvl(jc.gongrhy,0))*nvl(jc.rulyrz,0))/sum(nvl(jc.fadhy,0)+nvl(jc.gongrhy,0)),3),2)))) as fadryzbmdj,\n"
				+ "\n"
				+ "       decode(sum(nvl(jc.fadhy,0)+nvl(jc.gongrhy,0)),0,0,decode(round(sum((nvl(jc.fadhy,0)+nvl(jc.gongrhy,0))*nvl(jc.rulyrz,0))/sum(nvl(jc.fadhy,0)+nvl(jc.gongrhy,0)),3),0,0,\n"
				+ "             decode(sum(nvl(jc.gongrhy,0)),0,0,\n"
				+ "             round(round(sum(nvl(jc.gongrhy,0)*nvl(jc.gongrtrydj,0))/sum(nvl(jc.gongrhy,0)),2)*29.271\n"
				+ "             /round(sum((nvl(jc.fadhy,0)+nvl(jc.gongrhy,0))*nvl(jc.rulyrz,0))/sum(nvl(jc.fadhy,0)+nvl(jc.gongrhy,0)),3),2)))) as gongryzbmdj,\n"
				*/
				+ "     sum(nvl(jc.fadhy,0)) as fadhy,sum(nvl(jc.gongrhy,0)) as gongrhy,\n"

				// 煤折标煤单价:（发电燃煤成本＋供热厂用电分摊燃料费中的燃煤费用）/发电煤折标准煤量
				+ "       decode(sum(jc.fadmzbml),0,0,round((sum(jc.fadmcb)+sum(jc.ranmf))*10000/sum(jc.fadmzbml),2)) as fadbmdj,\n"  
				// 煤折标煤单价:（供热燃煤成本－供热厂用电分摊燃料费中的燃煤费用）/供热煤折标准煤量 
				+ "       decode(sum(jc.gongrmzbmzl),0,0,round((sum(jc.gongrmcb)-sum(jc.ranmf))*10000/sum(jc.gongrmzbmzl),2)) as gongrbmdj,\n"  
				// 油折标煤单价:（发电燃油成本＋供热厂用电分摊燃油费用）/发电油折标准煤量 
				+ "       decode(sum(jc.fadyzbzml),0,0,round((sum(jc.fadycb)+sum(jc.ranyf))*10000/sum(jc.fadyzbzml),2)) as fadryzbmdj,\n" 
				// 油折标煤单价:（供热燃油成本－供热厂用电分摊燃油费用）/供热油折标准煤量 
				+ "       decode(sum(jc.gongryzbzml),0,0,round((sum(jc.gongrycb)-sum(jc.ranyf))*10000/sum(jc.gongryzbzml),2)) as gongryzbmdj,\n" 
				// 综合：（发电燃煤成本＋发电燃油成本＋发电燃气成本＋供热燃煤成本＋供热燃油成本＋供热燃气成本）/（发电煤折标准煤量＋发电油折标准煤量＋发电气折标准煤量+供热煤折标准煤量＋供热油折标准煤量＋供热气折标准煤量） 
				+ "       decode((sum(jc.fadmzbml)+sum(jc.fadyzbzml)+sum(jc.fadqzbzml)+sum(jc.gongrmzbmzl)+sum(jc.gongryzbzml)+sum(jc.gongrqzbzml)),0,0,\n" 
				+ "             round((sum(jc.fadmcb)+sum(jc.fadycb)+sum(jc.fadrqcb)+sum(jc.gongrmcb)+sum(jc.gongrycb)+sum(jc.gongrrqcb))*10000/(sum(jc.fadmzbml)+sum(jc.fadyzbzml)+sum(jc.fadqzbzml)+sum(jc.gongrmzbmzl)+sum(jc.gongryzbzml)+sum(jc.gongrqzbzml)),2)) as zonghbmdj,\n"  
				// 综合：（发电燃煤成本＋发电燃油成本＋发电燃气成本＋供热厂用电分摊燃料费）/（发电煤折标准煤量＋发电油折标准煤量＋发电气折标准煤量）
				+ "       decode((sum(jc.fadmzbml)+sum(jc.fadyzbzml)+sum(jc.fadqzbzml)),0,0,\n" 
				+ "             round((sum(jc.fadmcb)+sum(jc.fadycb)+sum(jc.fadrqcb)+sum(jc.gongrcydftrlf))*10000/(sum(jc.fadmzbml)+sum(jc.fadyzbzml)+sum(jc.fadqzbzml)),2)) as fadzhbmdj,\n" 
				// 综合：（供热燃煤成本＋供热燃油成本＋供热燃气成本－供热厂用电分摊燃料费）/（供热煤折标准煤量＋供热油折标准煤量＋供热气折标准煤量）
				+ "       decode((sum(jc.gongrmzbmzl)+sum(jc.gongryzbzml)+sum(jc.gongrqzbzml)),0,0,\n"  
				+ "             round((sum(jc.gongrmcb)+sum(jc.gongrycb)+sum(jc.gongrrqcb)-sum(jc.gongrcydftrlf))*10000/(sum(jc.gongrmzbmzl)+sum(jc.gongryzbzml)+sum(jc.gongrqzbzml)),2)) as gongrzhbmdj\n" 
				+ "  from (\n"
//				+ "        select zb.diancxxb_id,zb.fenx,zb.fadhml+zb.gongrhml as rulmlhj,zb.fadhml,zb.gongrhml,\n"
//				+ "               zb.rulmrz,zb.fadtrmdj,zb.gongrtrmdj,zb.gongrhy,zb.fadhy, zb.fadtrydj,zb.gongrtrydj,zb.rulyrz\n"
//-------------------------------修改调整公式后yuezbb中的字段------------------------------------				
				+ "        select zb.diancxxb_id,zb.fenx,zb.fadytrml+zb.gongrytrml as rulmlhj,zb.fadytrml as  fadhml,zb.gongrytrml as gongrhml,\n"
		        + "				  (zb.rultrmpjfrl/1000) as rulmrz,zb.qiz_fadtrmdj as  fadtrmdj,zb.qiz_gongrtrmdj as gongrtrmdj,zb.gongrytryl as  gongrhy,\n"
		        + "				  zb.fadytryl as  fadhy, zb.qiz_fadtrydj as fadtrydj,zb.qiz_gongrtrydj as gongrtrydj,(zb.rultrypjfrl/1000) as rulyrz\n"
		        + "				  ,zb.fadmcb as fadmcb,zb.fadycb as fadycb,zb.fadrqcb as fadrqcb,zb.gongrmcb as gongrmcb,zb.gongrycb as gongrycb,zb.gongrrqcb as gongrrqcb,zb.qiz_ranm as ranmf,zb.qiz_rany as ranyf\n"
                + "				  ,zb.Fadmzbml as fadmzbml,zb.fadyzbzml as fadyzbzml,zb.fadqzbzml as fadqzbzml,zb.gongrmzbml as gongrmzbmzl,zb.gongryzbzml as gongryzbzml\n"
                + "				  ,zb.gongrqzbzml as gongrqzbzml,zb.gongrcydftrlf as gongrcydftrlf\n"
//---------------------------------------------------------------------------------------------
		        + "          from yuezbb zb,diancxxb dc\n"
				+ "         where zb.diancxxb_id=dc.id "+str+" and zb.riq=to_date('"+strdate+"','yyyy-mm-dd')\n"
				+ "        ) jc\n"
				+ "      ,diancxxb dc,diancxxb gs,\n"
				+ "      (select dc.id as diancxxb_id,fx.fenx from\n"
				+ "         (select distinct dc.id from yuezbb zb,diancxxb dc where zb.diancxxb_id=dc.id "+str+" and zb.riq=to_date('"+strdate+"','yyyy-mm-dd') )dc,\n"
				+ "         (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx )fx\n"
				+ " where jc.diancxxb_id(+)=fx.diancxxb_id and fx.diancxxb_id=dc.id and dc.fuid=gs.id and jc.fenx(+)=fx.fenx\n"
				+ " group by rollup(fx.fenx,gs.mingc,dc.mingc)\n"
				+ " having not grouping(fx.fenx)=1 "+guolzj+"\n"
				+ " order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc,fx.fenx) a";
			
		 String ArrHeader[][]=new String[2][16];
		 ArrHeader[0]=new String[] {"电厂","电厂","入炉天燃煤量","入炉天燃煤量","入炉天燃煤量","入炉热值","入炉热值","天然煤单价","天然煤单价","煤折标煤单价","煤折标煤单价","油折标煤单价","油折标煤单价","综合标煤单价","综合标煤单价","综合标煤单价"};
		 ArrHeader[1]=new String[] {"电厂","电厂","合计","发电","供热","Mj/Kg","Kcal/Kg","发电","供热","发电","供热","发电","供热","合计","发电","供热"};

		 int ArrWidth[]=new int[] {120,40,75,75,75,55,65,55,55,55,55,55,55,55,55,55};

		 String format[] = new String[] {"","","0","0","0","0.00","0","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};

		ResultSet rs = cn.getResultSet(sql);
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("入炉煤标煤单价",ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 4,  titdate, Table.ALIGN_CENTER);
		rt.setDefaultTitle(14,3,"单位:吨、元/吨、兆焦/千克",Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setColFormat(format);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(rt.body.getRows()>2){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(3, 1).equals("总计")){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			
		}
//		页脚 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(6,4,"审核:",Table.ALIGN_CENTER);

		 if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
		 			
			 rt.setDefautlFooter(15,2, "制表:",Table.ALIGN_RIGHT);
		 		}else{
		 			
		 			 rt.setDefautlFooter(15,2, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
		 		}
//		 rt.setDefautlFooter(15,2,"制表:",Table.ALIGN_RIGHT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	
public String getRulybmdj(){//入炉油标煤单价
		
		_CurrentPage=1;
		_AllPages=1;
		
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
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		String guolzj="";
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//集团用户
			if (treejib == 1) {// 选集团
				str = "";
				
			} else if (treejib == 2) {// 选分公司
				str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ getTreeid() + ") ";
			} else if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
			}
		}else if(isGongsUser()){//分公司用户
			str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			guolzj=" and grouping(gs.mingc)=0\n";//分公司查看报表时过滤总计。
			if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
			}
		}else if(isDiancUser()){//电厂用户
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
			guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
		}else{
			str = "";
		}
		
		String sql = " select a.danwmc,a.fenx,a.rulyhj,a.fadhy,a.gongrhy,a.rulyrz,a.rulyrz_dk,a.fadtrydj,a.gongrtrydj, \n"
				+"            decode((a.fadhy+a.gongrhy),0,0,Round((a.fadyzbmdj*a.fadhy+a.gongrtrydj1*a.gongrhy)/(a.fadhy+a.gongrhy),2)) as zhebmdjhj,\n"
				+"            a.fadyzbmdj,a.gongrtrydj1 \n"
				+"   from ("
				+"        select decode(grouping(dc.mingc)+grouping(gs.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc) as danwmc,fx.fenx,\n"
				+ "       sum(nvl(jc.rulyhj,0)) as rulyhj,sum(nvl(fadhy,0)) as fadhy,sum(nvl(gongrhy,0)) as gongrhy,\n"
				+ "       decode(sum(nvl(jc.rulyhj,0)),0,0,round(sum(nvl(jc.rulyhj,0)*nvl(jc.rulyrz,0))/sum(nvl(jc.rulyhj,0)),2)) as rulyrz,\n"
				+ "       round(decode(sum(nvl(jc.rulyhj,0)),0,0,round(sum(nvl(jc.rulyhj,0)*nvl(jc.rulyrz,0))/sum(nvl(jc.rulyhj,0)),2))*1000/4.1816,0) as rulyrz_dk,\n"
				+ "       decode(sum(nvl(jc.fadhy,0)),0,0,round(sum(nvl(jc.fadhy,0)*nvl(jc.fadtrydj,0))/sum(nvl(jc.fadhy,0)),2)) as fadtrydj,\n"
				+ "       decode(sum(nvl(jc.gongrhy,0)),0,0,round(sum(nvl(jc.gongrhy,0)*nvl(jc.gongrtrydj,0))/sum(nvl(jc.gongrhy,0)),2)) as gongrtrydj,\n"
				+ "\n"
				//发电油折标煤单价＝（发电燃油成本＋供热厂用电分摊燃油费用）/发电油折标准煤量
				+ "		  decode(sum(fadyzbzml),0,0,round((sum(fadycb)+sum(rany))*10000/sum(fadyzbzml),2)) as fadyzbmdj,\n"
//				+ "       decode(sum(nvl(jc.rulyhj,0)),0,0,decode(sum(nvl(jc.rulyhj,0)*nvl(jc.rulyrz,0)),0,0,\n"
//				+ "             round(decode(sum(nvl(jc.fadhy,0)),0,0,round(sum(nvl(jc.fadhy,0)*nvl(jc.fadtrydj,0))/sum(nvl(jc.fadhy,0)),2))\n"
//				+ "             *29.271/round(sum(nvl(jc.rulyhj,0)*nvl(jc.rulyrz,0))/sum(nvl(jc.rulyhj,0)),3),2))) as fadyzbmdj,\n"
				//供热油折标煤单价＝（供热燃油成本－供热厂用电分摊燃油费用）/供热油折标准煤量
				+ "		  decode(sum(gongryzbzml),0,0,round((sum(gongrycb)+sum(rany))*10000/sum(gongryzbzml),2)) as gongrtrydj1"
//				+ "       decode(sum(nvl(jc.rulyhj,0)),0,0,decode(sum(nvl(jc.rulyhj,0)*nvl(jc.rulyrz,0)),0,0,\n"
//				+ "             round(decode(sum(nvl(jc.gongrhy,0)),0,0,round(sum(nvl(jc.gongrhy,0)*nvl(jc.gongrtrydj,0))/sum(nvl(jc.gongrhy,0)),2))\n"
//				+ "             *29.271/round(sum(nvl(jc.rulyhj,0)*nvl(jc.rulyrz,0))/sum(nvl(jc.rulyhj,0)),3),2))) as gongrtrydj1\n"

//				+ "      from (select zb.diancxxb_id,zb.fenx,zb.fadhy+zb.gongrhy as rulyhj,zb.fadhy,zb.gongrhy,zb.rulyrz,zb.fadtrydj,zb.gongrtrydj\n"
//				+ "          from yuezbb1 zb,diancxxb dc where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd')  ) jc,\n"
//				-------------------------------修改调整公式后yuezbb中的字段------------------------------------	
				+ "		 from (select zb.diancxxb_id,zb.fenx,zb.fadytryl+zb.gongrytryl as rulyhj,zb.fadytryl as  fadhy,zb.gongrytryl as gongrhy, \n"
				+ "			          (zb.rultrypjfrl/1000) as rulyrz,zb.qiz_fadtrydj as  fadtrydj,zb.qiz_gongrtrydj as gongrtrydj \n"
				+ "                   ,zb.FADYCB as fadycb,zb.QIZ_RANY as rany,zb.FADYZBZML as fadyzbzml,zb.gongrycb as gongrycb,zb.gongryzbzml as gongryzbzml\n"
				+ "				from yuezbb zb,diancxxb dc \n"
				+ "				where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd')) jc,\n"
//				---------------------------------------------------------------------------------------------
				+ "      diancxxb dc,diancxxb gs,\n"
				+ "      (select dc.id as diancxxb_id,fx.fenx from\n"
				+ "         (select distinct dc.id from yuezbb zb,diancxxb dc where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd') )dc,\n"
				+ "         (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx )fx\n"
				+ " where jc.diancxxb_id(+)=fx.diancxxb_id and fx.diancxxb_id=dc.id and dc.fuid=gs.id and jc.fenx(+)=fx.fenx\n"
				+ " group by rollup(fx.fenx,gs.mingc,dc.mingc)\n"
				+ " having not grouping(fx.fenx)=1 "+guolzj+"\n"
				+ " order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc,fx.fenx )a";

		 String ArrHeader[][]=new String[2][12];
		 ArrHeader[0]=new String[] {"电厂","电厂","入炉油","入炉油","入炉油","入炉热值","入炉热值","天然油价","天然油价","折标煤单价","折标煤单价","折标煤单价"};
		 ArrHeader[1]=new String[] {"电厂","电厂","合计","发电","供热","Mj/Kg","Kcal/Kg","发电","供热","合计","发电","供热"};

		 int ArrWidth[]=new int[] {120,40,70,65,65,55,65,55,55,55,55,55};
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		 String format[] = new String[] {"","","0","0","0","0.00","0","0.00","0.00","0.00","0.00","0.00"};

		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("入炉油标煤单价",ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 3,  titdate, Table.ALIGN_CENTER);
		rt.setDefaultTitle(10,3,"单位:吨、元/吨、兆焦/千克",Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setColFormat(format);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(rt.body.getRows()>2){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(3, 1).equals("总计")){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
		}
//		页脚 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5,3,"审核:",Table.ALIGN_CENTER);

		 if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
		 			
		 		rt.setDefautlFooter(11, 2, "制表:",Table.ALIGN_RIGHT);
		 		}else{
		 			
		 		rt.setDefautlFooter(11, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
		 		}
//		 rt.setDefautlFooter(11,2,"制表:",Table.ALIGN_RIGHT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public String getRulmcb(){//入炉煤成本
		
		_CurrentPage=1;
		_AllPages=1;
		
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
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();
	
		String str = "";
		String guolzj="";
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//集团用户
			if (treejib == 1) {// 选集团
				str = "";
			} else if (treejib == 2) {// 选分公司
				str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ getTreeid() + ") ";
			} else if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
			}
		}else if(isGongsUser()){//分公司用户
			str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			guolzj=" and grouping(gs.mingc)=0\n";//分公司查看报表时过滤总计。
			if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
			}
		}else if(isDiancUser()){//电厂用户
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
			guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
		}else{
			str = "";
		}
		String sql = "select decode(grouping(dc.mingc)+grouping(gs.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc) as danwmc,fx.fenx,\n"
				+ "       sum(nvl(rulcbhj,0)) as rulcbhj,sum(nvl(fadcbhj,0)) as fadcbhj,sum(nvl(gongrcbhj,0)) as gongrcbhj,\n"
				+ "       sum(nvl(fadmcb,0)) as fadmcb,sum(nvl(gongrmcb,0)) as gongrmcb,sum(nvl(fadycb,0)) as fadycb,sum(nvl(gongrycb,0)) as gongrycb,\n"
				+ "       round(decode(sum(nvl(fadl,0)),0,0,sum(nvl(fadcbhj,0)*10000)/sum(fadl*10)),2) as faddwrlcb,\n"
				+ "       round(decode(sum(nvl(gongrl,0)),0,0,sum(nvl(gongrcbhj,0)*10000)/sum(gongrl)),2) as gongrdwrlcb\n"
//				+ "  from (select zb.diancxxb_id,zb.fenx,zb.fadmcb+zb.gongrmcb+zb.fadycb+zb.gongrycb as rulcbhj,\n"
//				+ "               zb.fadmcb+zb.fadycb as fadcbhj,zb.gongrmcb+zb.gongrycb as gongrcbhj,\n"
//				+ "               zb.fadmcb,zb.gongrmcb,zb.fadycb,zb.gongrycb,zb.fadl,zb.gongrl\n"
//				+ "          from yuezbb1 zb,diancxxb dc where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd')  ) jc,\n"
//				-------------------------------修改调整公式后yuezbb中的字段------------------------------------	
				+ "	 from (select zb.diancxxb_id,zb.fenx,zb.fadmcb+zb.gongrmcb+zb.fadycb+zb.gongrycb+zb.fadrqcb+zb.gongrrqcb as rulcbhj, \n"
				+ "			      zb.fadmcb+zb.fadycb+zb.fadrqcb as fadcbhj,zb.gongrmcb+zb.gongrycb+zb.gongrrqcb as gongrcbhj, \n"
				+ "				  zb.fadmcb,zb.gongrmcb,zb.fadycb,zb.gongrycb,zb.fadl,zb.gongrl \n"
				+ "	       from yuezbb zb,diancxxb dc where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd') ) jc,\n"
//				---------------------------------------------------------------------------------------------
				+ "      diancxxb dc,diancxxb gs,\n"
				+ "      (select dc.id as diancxxb_id,fx.fenx from\n"
				+ "         (select distinct dc.id from yuezbb zb,diancxxb dc where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd') )dc,\n"
				+ "         (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx )fx\n"
				+ " where jc.diancxxb_id(+)=fx.diancxxb_id and fx.diancxxb_id=dc.id and dc.fuid=gs.id and jc.fenx(+)=fx.fenx\n"
				+ " group by rollup(fx.fenx,gs.mingc,dc.mingc)\n"
				+ " having not grouping(fx.fenx)=1 "+guolzj+"\n"
				+ " order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc,fx.fenx";

		 String ArrHeader[][]=new String[2][11];
		 ArrHeader[0]=new String[] {"电厂","电厂","成本","成本","成本","煤成本","煤成本","油成本","油成本","单位燃料成本","单位燃料成本"};
		 ArrHeader[1]=new String[] {"电厂","电厂","合计","发电","供热","发电","供热","发电","供热","发电","供热"};

		 int ArrWidth[]=new int[] {120,40,65,65,60,60,60,60,60,50,50};
	
		 String format[] = new String[] {"","","0","0","0","0","0","0","0","0.00","0.00"};
	
		ResultSet rs = cn.getResultSet(sql);
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("入炉煤成本",ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3,  titdate, Table.ALIGN_CENTER);
		rt.setDefaultTitle(8, 4, "单位:元、元/千千瓦时、元/吉焦",Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setColFormat(format);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(rt.body.getRows()>2){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(3, 1).equals("总计")){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
		}
	//	页脚 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 3, "制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5, 3, "审核:",Table.ALIGN_CENTER);

		 if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
		 			
		 		rt.setDefautlFooter(10, 2, "制表:",Table.ALIGN_RIGHT);
		 		}else{
		 			
		 		rt.setDefautlFooter(10, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
		 		}
//		 rt.setDefautlFooter(10,2,"制表:",Table.ALIGN_RIGHT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public String getRucrlbmdjbj(){//入炉煤成本
		
		_CurrentPage=1;
		_AllPages=1;
		
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
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();
	
		String str = "";
		String guolzj="";
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//集团用户
			if (treejib == 1) {// 选集团
				str = "";
			} else if (treejib == 2) {// 选分公司
				str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ getTreeid() + ") ";
				guolzj=" and grouping(gs.mingc)=0\n";//分公司查看报表时过滤总计。
			} else if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
			}
		}else if(isGongsUser()){//分公司用户
			str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			guolzj=" and grouping(gs.mingc)=0\n";//分公司查看报表时过滤总计。
			if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
			}
		}else if(isDiancUser()){//电厂用户
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
			guolzj=" and grouping(dc.mingc)=0\n";//分公司查看报表时过滤总计。
		}else{
			str = "";
		}
		
		String sql = "select case when grouping(gs.mingc)=1 then '总计' else\n"
				+ "       case when grouping(dc.mingc)=1 then gs.mingc else '&nbsp;&nbsp;'||dc.mingc end end as danwmc,fx.fenx,\n"
				+ "       decode(sum(nvl(rc.jingz,0)),0,0,decode(round(sum(nvl(rc.farl,0)*nvl(rc.jingz,0))/sum(nvl(rc.jingz,0)),3),0,0,\n"
				+ "           round(round(sum(nvl(rc.zonghj,0)*nvl(rc.jingz,0))/sum(nvl(rc.jingz,0)),2)\n"
				+ "              *29.271/round(sum(nvl(rc.farl,0)*nvl(rc.jingz,0))/sum(nvl(rc.jingz,0)),3),2))) as rucbmdj,\n"
				+ "\n"

				+ "		  decode(sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),0,0,\n"
				+ "           decode(round(sum((nvl(rl.fadhml,0)+nvl(rl.gongrhml,0))*rl.rulmrz)/sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),3),0,0,\n"
				+ "           round((round(sum(nvl(rl.fadmcb,0)+nvl(rl.gongrmcb,0))/sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),2))*29.271/\n"
				+ "           round(sum((nvl(rl.fadhml,0)+nvl(rl.gongrhml,0))*rl.rulmrz)/sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),3),2))) as rulbmdj,\n"
				+ "\n"
				+ "       decode(sum(nvl(rc.jingz,0)),0,0,decode(round(sum(nvl(rc.farl,0)*nvl(rc.jingz,0))/sum(nvl(rc.jingz,0)),3),0,0,\n"
				+ "           round(round(sum(nvl(rc.zonghj,0)*nvl(rc.jingz,0))/sum(nvl(rc.jingz,0)),2)\n"
				+ "              *29.271/round(sum(nvl(rc.farl,0)*nvl(rc.jingz,0))/sum(nvl(rc.jingz,0)),3),2)))\n"
				+ "        -decode(sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),0,0,\n"
				+ "           decode(round(sum((nvl(rl.fadhml,0)+nvl(rl.gongrhml,0))*rl.rulmrz)/sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),3),0,0,\n"
				+ "           round((round(sum(nvl(rl.fadmcb,0)+nvl(rl.gongrmcb,0))/sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),2))*29.271/\n"
				+ "           round(sum((nvl(rl.fadhml,0)+nvl(rl.gongrhml,0))*rl.rulmrz)/sum(nvl(rl.fadhml,0)+nvl(rl.gongrhml,0)),3),2))) as chaz \n"

				+ "\n"
				+ "  from diancxxb dc,diancxxb gs,\n"
				+ "(select tj.diancxxb_id,dj.fenx,sum(nvl(sl.jingz,0)) as jingz,\n"
				+ "       decode(sum(nvl(sl.jingz,0)),0,0,round(sum((nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0))*nvl(sl.jingz,0))/sum(sl.jingz),2)) as zonghj,\n"
				+ "       decode(sum(nvl(sl.jingz,0)),0,0,round(sum(sl.jingz*zl.qnet_ar)/sum(sl.jingz),3)) as farl\n"
				+ "   from yuercbmdj dj,yuetjkjb tj,yueslb sl,yuezlb zl,diancxxb dc \n"
				+ "  where sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and dj.yuetjkjb_id=tj.id\n"
				+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx and tj.diancxxb_id=dc.id "+ str+ " and tj.riq=to_date('"+ strdate+ "','yyyy-mm-dd')\n"
				+ "  group by tj.diancxxb_id,tj.riq,dj.fenx) rc,\n"
				+ "\n"
//				+ " (select distinct zb.diancxxb_id,zb.fenx,zb.fadmcb,zb.gongrmcb,zb.fadhml,zb.gongrhml,zb.rulmrz\n"
//				--------------------------------修改调整公式后yuezbb中的字段---------------------------------------------------
				+ " (select distinct zb.diancxxb_id,zb.fenx,zb.fadmcb*10000 as fadmcb,zb.gongrmcb*10000 as gongrmcb,zb.fadytrml as fadhml,zb.gongrytrml as gongrhml,zb.rultrmpjfrl/1000 as rulmrz\n"
//				---------------------------------------------------------------------------------------------
				+ "    from yuezbb zb,yuetjkjb tj,diancxxb dc\n"
				+ "   where tj.diancxxb_id=zb.diancxxb_id and tj.diancxxb_id=dc.id "+ str+ " \n"
				+ "     and tj.riq=zb.riq and tj.riq=to_date('"+ strdate+ "','yyyy-mm-dd') ) rl,\n"
				+ " (select * from\n"
				+ "  (select distinct tj.diancxxb_id from yuetjkjb tj,diancxxb dc where tj.diancxxb_id=dc.id "+ str+ " and tj.riq=to_date('"+ strdate+ "','yyyy-mm-dd') union\n"
				+ "   select distinct diancxxb_id from yuezbb zb,diancxxb dc where zb.diancxxb_id=dc.id "+ str+ " and zb.riq=to_date('"+ strdate+ "','yyyy-mm-dd') ) d,\n"
				+ "  (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) f ) fx\n"
				+ "\n"
				+ "where rc.diancxxb_id(+)=fx.diancxxb_id and rl.diancxxb_id(+)=fx.diancxxb_id and rc.fenx(+)=fx.fenx and rl.fenx(+)=fx.fenx\n"
				+ "  and fx.diancxxb_id=dc.id and dc.fuid=gs.id\n"
				+ "group by rollup(fx.fenx,gs.mingc,dc.mingc)\n"
				+ "having not grouping(fx.fenx)=1 "+guolzj+"\n"
				+ "order by grouping(gs.mingc)desc,min(gs.xuh),grouping(dc.mingc)desc,min(dc.xuh),fx.fenx";

		
		 String ArrHeader[][]=new String[1][5];
		 ArrHeader[0]=new String[] {"电厂","电厂","入厂","入炉","差值"};

		 int ArrWidth[]=new int[] {160,40,100,100,100};
	
		 String format[] = new String[] {"","","0.00","0.00","0.00"};
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("入厂入炉标煤单价比较",ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, titdate, Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(rs,1,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setColFormat(format);
		rt.body.setRowHeight(1, 30);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(rt.body.getRows()>1){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(2, 1).equals("总计")){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
		}
	//	页脚 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			this.setTreeid(null);
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);保存传递的非默认纸张的样式
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean2(null);
					visit.setProSelectionModel2(null);
					visit.setDropDownBean3(null);
					visit.setProSelectionModel3(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					visit.setDropDownBean10(null);
					visit.setProSelectionModel10(null);
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }
		getToolBars();
		isBegin=true;
	}

//	 统计口径
	public boolean _Baoblxchange = false;

	public IDropDownBean getBaoblxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getIBaoblxModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean10() != null) {
			id = ((Visit)getPage().getVisit()).getDropDownBean10().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public void setIBaoblxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getIBaoblxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getIBaoblxModels() {
		
		List List = new ArrayList();
		List.add(new IDropDownBean(0,"入炉煤标煤单价"));
		List.add(new IDropDownBean(1,"入炉油标煤单价"));
		List.add(new IDropDownBean(2,"入炉成本"));
		
		((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
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
		setDiancxxModel(new IDropDownModel(sql,"中电投"));
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
		if(mstrReportName.equals(this.REPORT_RULCBREPORT)){
			tb1.addText(new ToolbarText("统计口径:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BAOBLX");
			cb.setWidth(120);
			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		}
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
//		ToolbarButton tbLink = new ToolbarButton(null,"打印","function(){}");
		
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
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
	
//  Day下拉框
	private boolean _Day = false;
	private IDropDownBean _DayValue;
	private IPropertySelectionModel _DayModel;

	public IDropDownBean getDayValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean) getDayModel().getOption(14));
    	}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	public void setDayValue(IDropDownBean Value) {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()!=null){
			if(((Visit)getPage().getVisit()).getDropDownBean1().getId()!=Value.getId()){
				_Day=true;
			}
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDayModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
    }
    public IPropertySelectionModel getDayModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDayModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    public void getDayModels() {
        List listDay = new ArrayList();
//        listDay.add(new IDropDownBean(-1, "请选择"));
    	for (int i = 1; i < 32; i++) {
            listDay.add(new IDropDownBean(i, String.valueOf(i)+"天"));
        }
    	((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listDay));
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
	
	
}
