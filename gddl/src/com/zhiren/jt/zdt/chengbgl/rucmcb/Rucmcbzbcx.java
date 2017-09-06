package com.zhiren.jt.zdt.chengbgl.rucmcb;
/* 
* 时间：2009-08-29
* 作者： ll
* 修改内容：1、增加分厂、分矿、分厂分矿、分矿分厂表查询
* 			2、增加计划口径下拉框，并以计划口径为条件进行查询
* 			3、月份下拉框默认为当前月份的前一个月
*/ 
/* 
* 时间：2009-09-4
* 作者： ll
* 修改内容：1、二级公司登陆时去“总计”行。
* 		   
*/
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
import com.zhiren.common.MainGlobal;
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

public class Rucmcbzbcx extends BasePage {
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
    private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
			int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj)
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
	
	private String REPORT_RUCMCBZBCX="rucmcbzbcx";
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
		isBegin = false;
		if(mstrReportName.equals(REPORT_RUCMCBZBCX)){
			return getRucmcbzb();
		}else{
			return "无此报表";
		}
	}

	public String getRucmcbzb(){
		
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
		String sql = "";
		
		String str = "";
		String sql_bt="";
		String sql_group = "";
		String having = "";
		String strTitle="";
		String jihkjbid="";//计划口径查询条件
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//集团用户
			if (treejib == 1) {// 选集团
				str = "";
				having = " ";
				
			} else if (treejib == 2) {// 选分公司
				str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ getTreeid() + ") ";
				having = " and grouping(dc.fgsmc)=0\n";//分公司查看报表时过滤总计。
			} else if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				having =" and grouping(dc.mingc)=0\n";
			}
		}else if(isGongsUser()){//分公司用户
			str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			having = " and grouping(dc.fgsmc)=0\n";//分公司查看报表时过滤总计。
			if (treejib == 3) {// 选电厂
				str = " and dc.id = " + getTreeid() + " ";
				having =" and grouping(dc.mingc)=0\n";
			}
		}else if(isDiancUser()){//电厂用户
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
			having =" and grouping(dc.mingc)=0\n";
		}else{
			having = "";
			str = "";
		}
		
		if(getJihkjDropDownValue().getId()!=-1){
			jihkjbid= "and j.id="+getJihkjDropDownValue().getId();
		}
//		------------------------------------------------------------------------
		if (getBaoblxValue().getValue().equals("分厂")){
			sql_bt = "select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n";
			
			sql_group = " group by rollup(fx.fenx,dc.fgsmc,dc.mingc)   \n"+
				     " having not grouping(fx.fenx)=1"+having+"\n"+
				     " order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n"+
				     "          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n"+
				     "          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx";
			strTitle="煤炭入厂成本统计(分厂)";
		}else if(getBaoblxValue().getValue().equals("分矿")){
			
			sql_bt = "select decode(grouping(smc)+grouping(dqmc),2,'总计',1,smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,";
			
			sql_group = " group by rollup(fx.fenx,smc,dqmc)   \n"+
					 " having not grouping(fx.fenx)=1"+
					 " order by grouping(smc) desc,max(sxh) ,smc, \n"+
					 "          grouping(dqmc) desc,max(dqxh) ,dqmc, \n"+
					 "          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx";
			strTitle="煤炭入厂成本统计(分矿)";
		}else if(getBaoblxValue().getValue().equals("分厂分矿")){
			sql_bt="select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,";
			
			sql_group = "group by rollup(fx.fenx,fgsmc,dc.mingc,dqmc)   \n"+
					" having not grouping(fx.fenx)=1"+having+"\n"+
					" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n"+
					"          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n"+
					"          grouping(dqmc) desc,max(dqxh) ,dqmc, \n"+
					"          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx";
			strTitle="煤炭入厂成本统计(分厂分矿)";
		}else if(getBaoblxValue().getValue().equals("分矿分厂")){
			if(isJitUserShow()){
				sql_bt = "select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'总计',2,dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,";
				
				sql_group = " group by rollup(fx.fenx,dqmc,fgsmc,dc.mingc)   \n"+
						 " having not grouping(fx.fenx)=1\n"+
						 " order by  grouping(dqmc) desc,max(dqxh) ,dqmc, \n"+
						 "          grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n"+
						 "          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n"+
						 "          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx";
			}else{
				
				sql_bt = "select decode(grouping(dc.mingc)+grouping(dqmc),2,'总计',1,dqmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,";
				
				sql_group = " group by rollup(fx.fenx,dqmc,dc.mingc)   \n"+
						 " having not grouping(fx.fenx)=1\n"+
						 " order by  grouping(dqmc) desc,max(dqxh) ,dqmc, \n"+
						 "          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n"+
						 "          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx";
			}
			
			strTitle="煤炭入厂成本统计(分矿分厂)";
		}
//		------------------------------------------------------------------------
	/*	if(getBaoblxValue().getValue().equals("分厂")){
			sql = "select --case when grouping(dc.mingc)=0 then dc.mingc else \n"
					+ "          --case when grouping(gs.mingc)=0 then gs.mingc else '总计' end end as 电厂, \n"
					+ "       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc else \n"
					+ "       case when grouping(kj.mingc)=0 then '&nbsp;&nbsp;'||kj.mingc else \n"
					+ "       case when grouping(dc.mingc)=0 then dc.mingc else \n"
					+ "       case when grouping(gs.mingc)=0 then gs.mingc else '总计' end end  end end 供煤单位, \n"
					+ "       sj.fenx as 分项,sum(sj.jingz) as 入厂数量, \n"
					+ "		  decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as 发热量,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as 综合价,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as 煤价,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as 运费,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as 杂费,\n"
					
					+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
					+ "			  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as 含税标煤单价,  "
					+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
					+ "			  round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as 不含税标煤单价  "
					
					+ "  from  yuetjkjb tj,diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj,\n"
					+ "(select dj.yuetjkjb_id,dj.fenx,nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl,\n"
					+ "        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj,\n"
					+ "        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj,\n"
					+ "        nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf \n"
					+ "   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys \n"
					+ "  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id \n"
					+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx  ) sj \n"
					+ "  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id and dc.fuid=gs.id \n"
					+ "	   	   "+str+" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')  \n"
					+ "  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gs.mingc,sj.fenx),(gs.mingc,kj.mingc,sj.fenx), \n"
					+ "        (dc.mingc,sj.fenx),(dc.mingc,kj.mingc,sj.fenx),(dc.mingc,kj.mingc,gy.mingc,sj.fenx)) \n"
					+ "	"+having+" \n"	
					+ "order by	\n"
					+ "    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj.mingc),1,4,3),0) desc, \n"
					+ "    min(gs.xuh), gs.mingc,min(dc.xuh),grouping(kj.mingc) desc,min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),sj.fenx";
	
	
			 
		}else if(getBaoblxValue().getValue().equals("分矿")){
			
			sql = "select case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc else\n"
				+ "       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;'||gy.mingc else\n"
				+ "       case when grouping(kj.mingc)=0 then kj.mingc else '总计' end end  end 供煤单位,\n"
				+ "       sj.fenx as 分项,sum(sj.jingz) as 入厂数量,\n"
				+ "		  decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as 发热量,\n"
				+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as 综合价,\n"
				+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as 煤价,\n"
				+ "       decode(sum(sj.jingz),0,0,round(sum(sj.yunj*sj.jingz)/sum(jingz),2)) as 运费,\n"
				+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zaf*sj.jingz)/sum(jingz),2)) as 杂费,\n"
				
				+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
				+ "			  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as 含税标煤单价,  "
				+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
				+ "			  round(round(sum(sj.buhszhj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as 不含税标煤单价  "
				
				+ "  from yuetjkjb tj,diancxxb dc,gongysb gy,jihkjb kj,\n"
				+ "    (select dj.yuetjkjb_id,dj.fenx,nvl(sl.jingz,0) as jingz,nvl(zl.qnet_ar,0) as farl,\n"
				+ "            nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0) as zonghj,\n"
				+ "        nvl(dj.meij,0)+nvl(dj.jiaohqzf,0)+nvl(dj.yunj,0)+nvl(dj.daozzf,0)+nvl(dj.qit,0)-nvl(dj.meijs,0)-nvl(dj.yunjs,0) as buhszhj,\n"
				+ "            nvl(dj.meij,0) as meij,nvl(dj.yunj,0) as yunj,nvl(dj.daozzf,0)+nvl(dj.jiaohqzf,0)+nvl(dj.qit,0) as zaf \n"
				+ "   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys\n"
				+ "  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id\n"
				+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx \n"
				+ "    ) sj\n"
				+ "  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id\n"
				+ "    "+str+" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n"
				+ "  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx))\n"
				+ "order by\n"
				+ "    decode(grouping(kj.mingc)+grouping(gy.mingc)+grouping(dc.mingc),3,3,0) desc,\n"
				+ "    min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),grouping(dc.mingc) desc,min(dc.xuh),sj.fenx";
			
		}else{
			return "无此报表";
		}*/
		sql=sql_bt+
		"       fx.fenx,\n" + 
		"       sum(sj.laimsl) as 入厂数量,\n" + 
		"       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as 发热量,\n" + 
		"       decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as 综合价,\n" + 
		"       decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(sj.laimsl),2)) as 煤价,\n" + 
		"       decode(sum(sj.laimsl),0,0,round(sum(sj.yunj*sj.laimsl)/sum(sj.laimsl),2)) as 运费,\n" + 
		"       decode(sum(sj.laimsl),0,0,round(sum((sj.daozzf+sj.zaf+sj.jiaohqzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)) as 杂费,\n" + 
		"       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2) as 含税标煤单价,\n" + 
		"       round(decode(sum(sj.laimsl),0,0,round(sum((sj.meij+sj.zaf+sj.jiaohqzf+sj.yunj+sj.daozzf+sj.qit-sj.meijs-sj.yunjs)*sj.laimsl)/sum(sj.laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(sj.laimsl),2)),2)  as 不含税标煤单价\n" + 
		"   from\n" + 
		"       (select distinct dcid.diancxxb_id,dcid.gongysb_id,fx.fenx,fx.xuh from\n" + 
		"             (select distinct dc.id as diancxxb_id ,y.id as gongysb_id\n" + 
		"                     from yuercbmdj ycb,yuetjkjb kj,vwdianc dc,vwgongys y\n" + 
		"                     where ycb.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id and kj.gongysb_id=y.id  "+str+" \n" + 
		"                     and (riq=to_date('"+strdate+"','yyyy-mm-dd') or riq=last_year_today(to_date('"+strdate+"','yyyy-mm-dd'))) ) dcid,\n" + 
		"                     vwfenxYue fx\n" + 
		"         ) fx,\n" + 
		"(select decode(1,1,'本月','') as fenx,dc.id as id,y.id as gongysb_id,\n" + 
		"        sum(nvl(sl.laimsl,0)) as laimsl\n" + 
		"         ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs\n" + 
		"        ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf " +
		"  from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j\n" + 
		"   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id "+jihkjbid+"\n" + 
		"         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id   "+str+" \n" + 
		"         and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n" + 
		"   group by (dc.id,y.id )\n" + 
		"   union\n" + 
		"   select decode(1,1,'累计','') as fenx,dc.id as id,y.id as gongysb_id,\n" + 
		"          sum(nvl(sl.laimsl,0)) as laimsl\n" + 
		"          ,decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meij,0)*sl.laimsl)/sum(sl.laimsl),2)) as meij\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.jiaohqzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as jiaohqzf\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunj,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunj\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.daozzf,0)*sl.laimsl)/sum(sl.laimsl),2)) as daozzf\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.qit,0)*sl.laimsl)/sum(sl.laimsl),2)) as qit\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.meijs,0)*sl.laimsl)/sum(sl.laimsl),2)) as meijs\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.yunjs,0)*sl.laimsl)/sum(sl.laimsl),2)) as yunjs\n" + 
		"         ,decode(sum(sl.laimsl),0,0,round(sum(nvl(dj.zaf,0)*sl.laimsl)/sum(sl.laimsl),2)) as zaf\n" + 
		"   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,vwdianc dc, vwgongys y ,jihkjb j\n" + 
		"   where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   and tj.jihkjb_id=j.id "+jihkjbid+"\n" +  
		"         and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='本月' and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id  "+str+"     \n" + 
		"         and tj.riq>=getyearfirstdate(to_date('"+strdate+"','yyyy-mm-dd')) and tj.riq<=to_date('"+strdate+"','yyyy-mm-dd')\n" + 
		"   group by (dc.id,y.id )\n" + 
		"     ) sj,vwdianc dc,vwgongys gy\n" + 
		" where fx.diancxxb_id=dc.id(+)  and fx.gongysb_id=gy.id(+)\n" + 
		"       and fx.diancxxb_id=sj.id(+)  and fx.gongysb_id=sj.gongysb_id(+)\n" + 
		"       and fx.fenx=sj.fenx(+)\n" + sql_group;
	

		String ArrHeader[][]=new String[2][10];
		 ArrHeader[0]=new String[] {"单位","本月<br>累计","入厂煤量<br>(吨)","入厂热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","运费<br>(元/吨)","杂费<br>(元/吨)","标煤单价(元/吨)","标煤单价(元/吨)"};
		 ArrHeader[1]=new String[] {"单位","本月<br>累计","入厂煤量<br>(吨)","入厂热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","运费<br>(元/吨)","杂费<br>(元/吨)","含税","不含税"};
		 int ArrWidth[]=new int[] {150,40,65,55,55,55,55,55,55,55};

		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle(strTitle,ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, titdate, Table.ALIGN_CENTER);
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
//		rt.body.setRowHeight(1, 50);
		rt.body.setPageRows(24);
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
		 rt.setDefautlFooter(1,4,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		
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
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			this.setTreeid(null);
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
					visit.setDropDownBean5(null);
					visit.setProSelectionModel5(null);
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
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BAOBLX");
		cb.setWidth(80);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
//		ToolbarButton tbLink = new ToolbarButton(null,"打印","function(){}");
		
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
//	 统计口径
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

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

	private IPropertySelectionModel _IBaoblxModel;

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
		List.add(new IDropDownBean(0,"分厂"));
		List.add(new IDropDownBean(1,"分矿"));
		List.add(new IDropDownBean(2,"分厂分矿"));
		List.add(new IDropDownBean(3,"分矿分厂"));
		
		((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
//	计划口径
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb \n";
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql, "全部"));
		return;
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

