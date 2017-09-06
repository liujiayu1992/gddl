/* chh 2008-09-24 设计热值、校核热值与
 * 修改视图加入fuid,shangjgsid
 * create or replace view vwdianc as
select dc.id as id,dc.mingc , dc.xuh ,dc.fuid,dc.shangjgsid,
        fgs.id as fgsid,fgs.mingc as fgsmc,fgs.xuh as fgsxh,
        rlgs.id as rlgsid ,rlgs.mingc as rlgsmc,rlgs.xuh as rlgsxh
  from diancxxb dc,diancxxb fgs,diancxxb rlgs
  where dc.jib=3
  and dc.fuid=fgs.id(+)
  and dc.shangjgsid=rlgs.id(+);
 */
/* 
* 时间：2009-07-21
* 作者： ll
* 修改内容：修改查询sql,需求煤量改为从yuecgjhb中取值，热值转换为大卡显示。
*			
*/ 
 
package com.zhiren.jt.zdt.monthreport.shejsjrzdb;
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
public class Shejsjrzdb  extends BasePage implements PageValidateListener{
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
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
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		return getSelectData();
	}
	
	private boolean isBegin=false;
	private String getSelectData(){
		String strGongsID = "";
		String strDate="";
		
		JDBCcon cn = new JDBCcon();
		strDate=getNianfValue().getId()+"-"+getYuefValue().getId()+"-01";
		
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

		//报表内容			 
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n");
		sbsql.append("       sum(cgjh.xuqml) as xuqml,sum(lm.laim) as shijgy, \n");
		sbsql.append("       sum(lm.zhongdlm) as zhongdlm, \n");
		sbsql.append("       decode(sum(cgjh.zhongdht),0,0,round(sum(lm.zhongdlm)/sum(cgjh.zhongdht)*100,2)) as zhongddxl, \n");
		sbsql.append("       sum(lm.shiclm) as shiclm, \n");
		sbsql.append("       decode(sum(cgjh.shicht),0,0,round(sum(lm.shiclm)/sum(cgjh.shicht)*100,2)) as shicdxl, \n");
		sbsql.append("       decode(grouping(dc.mingc),1,'',max(shejmz)) as shejmz, \n");
		sbsql.append("       round(decode(sum(decode(nvl(sj.shejrz,0),0,0,sj.jizrl)),0,0,round(sum(sj.jizrl*shejrz)/sum(decode(nvl(sj.shejrz,0),0,0,sj.jizrl)),3))*1000/4.1816,0) as shejrz,  \n");
		sbsql.append("       round(decode(sum(decode(nvl(sj.jiaohrz,0),0,0,sj.jizrl)),0,0,round(sum(sj.jizrl*sj.jiaohrz)/sum(decode(nvl(sj.jiaohrz,0),0,0,sj.jizrl)),3))*1000/4.1816,0) as jiaohrz,  \n");
		sbsql.append("       round(decode(sum(decode(nvl(lm.shijrz,0),0,0,lm.laim)),0,0,round(sum(lm.laim*shijrz)/sum(decode(nvl(lm.shijrz,0),0,0,lm.laim)),2))*1000/4.1816,0) as shij  \n");
		sbsql.append(" from vwdianc dc,\n");
		sbsql.append("(select diancxxb_id,sum(jizurl) as jizrl,max(shejmz) as shejmz, \n");
		sbsql.append("        sum(jizurl*qnet_ar)/sum(jizurl) as shejrz, \n");
		sbsql.append("        sum(jizurl*qnet_ar_xh)/sum(jizurl) as jiaohrz \n");
		sbsql.append(" from jizb \n");
		sbsql.append(" group by diancxxb_id) sj, \n");
		sbsql.append("(select dc.id as diancxxb_id, sum(cg.yuejhcgl) as xuqml,sum(decode(jihkjb_id,1,cg.yuejhcgl,3,cg.yuejhcgl,0)) as zhongdht,  \n");
		sbsql.append("        sum(decode(jihkjb_id,2,cg.yuejhcgl,0)) as shicht \n");
		sbsql.append("  from yuecgjhb cg,diancxxb dc \n");
		sbsql.append("  where dc.id=cg.diancxxb_id \n");
		sbsql.append("        and riq=to_date('").append(strDate).append("','yyyy-mm-dd') \n").append(strGongsID);
		sbsql.append("  group by dc.id) cgjh, \n");
		sbsql.append("(select dc.id as diancxxb_id,sum(laimsl) as laim,sum(decode(jihkjb_id,1,laimsl,3,laimsl,0)) as zhongdlm, \n");
		sbsql.append("        sum(decode(jihkjb_id,1,0,3,0,laimsl)) as shiclm, \n");
//		sbsql.append("        decode(sum(decode(nvl(qnet_ar,0),0,laimsl)),0,0,round( sum(laimsl*nvl(qnet_ar,0))/sum(decode(nvl(qnet_ar,0),0,laimsl)),2)) as shijrz \n");
		sbsql.append("        decode(sum(laimsl),0,0, sum(laimsl*nvl(qnet_ar,0))/sum(laimsl)) as shijrz \n");
		sbsql.append(" from yueslb sl,yuetjkjb kj,yuezlb zl,diancxxb dc \n");
		sbsql.append("        where sl.yuetjkjb_id=kj.id \n");
		sbsql.append("             and kj.diancxxb_id=dc.id \n");
		sbsql.append("             and kj.riq=to_date('").append(strDate).append("','yyyy-mm-dd') \n").append(strGongsID);
		sbsql.append("             and zl.yuetjkjb_id(+)=kj.id \n");
		sbsql.append("             and sl.fenx='本月' and zl.fenx='本月' \n");
		sbsql.append("        group by dc.id) lm \n");
		sbsql.append("where dc.id=sj.diancxxb_id(+) \n");
		sbsql.append("      and dc.id=cgjh.diancxxb_id(+) \n").append(strGongsID);
		sbsql.append("      and dc.id=lm.diancxxb_id(+) \n");
		sbsql.append("group by rollup(dc.fgsmc,dc.mingc) \n");
		sbsql.append("order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc, \n");
		sbsql.append("      grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
		
//		报表表头定义
		Report rt = new Report();
		String titlename="设计、校核与实际热值对比表";
		
		String ArrHeader[][]=new String[2][11];
		ArrHeader[0]=new String[] {"单位","需求煤量","实际供应<br>合计","重点合同","重点合同","市场采购","市场采购","设计煤种","设计热值","校核热值","实际热值"};
		ArrHeader[1]=new String[] {"单位","需求煤量","实际供应<br>合计","到货量","到货率","到货量","到货率","设计煤种","设计热值","校核热值","实际热值"};
		
		int ArrWidth[]=new int[] {120,60,60,60,50,60,50,100,60,60,60};
		ResultSet rs = cn.getResultSet(sbsql.toString());

		// 数据
		rt.setBody(new Table(rs,2, 0, 1));
		rt.setTitle(titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 2, ""+getNianfValue().getId()+"年"+getYuefValue().getId()+"月", Table.ALIGN_LEFT);
		rt.setDefaultTitle(10,2, "单位:吨、Kcal/Kg", Table.ALIGN_RIGHT);
		rt.body.setUseDefaultCss(true);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		
		rt.body.ShowZero =false;
		if(rt.body.getRows()>2){
			rt.body.mergeFixedCols();
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}

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
		setDiancxxModel(new IDropDownModel(sql,""));
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


