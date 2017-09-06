package com.zhiren.jt.zdt.monthreport.rucmjgb;

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


public class Rucmjgreport  extends BasePage implements PageValidateListener{
	
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
	
	private String RT_HET="Yuedmjgmxreport";//月度煤价格明细
	private String mstrReportName="Yuedmjgmxreport";
	
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
		
		JDBCcon cn = new JDBCcon();
		
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
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
		//	if(getBaoblxValue().getValue().equals("分厂汇总")){
				
		 
				 strSQL=
				 "select   case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;'||max(j.mingc) else\n"
				+ "         case when grouping(dc.mingc)=0 then dc.mingc else\n"
				+ "         case when grouping(fgs.mingc)=0 then fgs.mingc else '总计' end end  end aa,\n"
				+ "         fx.fenx as fenx,\n"
				+ "      Round(decode(sum(bq.jingz),0,0,sum(bq.daoczhj*bq.jingz)/sum(bq.jingz)),2) as bq_daoczhj ,\n"
				+ "      Round(decode(sum(tq.jingz),0,0,sum(tq.daoczhj*tq.jingz)/sum(tq.jingz)),2) as tq_daoczhj  ,\n"
				+ "      Round(decode(sum(bq.jingz),0,0,sum(bq.chebj*bq.jingz)/sum(bq.jingz)),2) as bq_chebj,\n"
				+ "      Round(decode(sum(tq.jingz),0,0,sum(tq.chebj*tq.jingz)/sum(tq.jingz)),2) as tq_chebj,\n"
				+ "      Round(decode(sum(bq.jingz),0,0,sum(bq.yunj*bq.jingz)/sum(bq.jingz)),2) as bq_yunj,\n"
				+ "      Round(decode(sum(tq.jingz),0,0,sum(tq.yunj*tq.jingz)/sum(tq.jingz)),2) as tq_yunj,\n"
				+ "      Round(decode(sum(bq.jingz),0,0,sum(bq.zaf*bq.jingz)/sum(bq.jingz)),2) as bq_zaf,\n"
				+ "      Round(decode(sum(tq.jingz),0,0,sum(tq.zaf*tq.jingz)/sum(tq.jingz)),2) as tq_zaf,\n"
				+ "      Round(decode(sum(bq.jingz),0,0,sum(bq.rucbmdj*bq.jingz)/sum(bq.jingz)),2) as bq_rucbmdj,\n"
				+ "      Round(decode(sum(tq.jingz),0,0,sum(tq.rucbmdj*tq.jingz)/sum(tq.jingz)),2) as tq_rucbmdj,\n"
				+ "      '' as bq_tianrmdj,'' as tq_tianrmdj\n"
				+ "\n"
				+ " from\n"
				+ "     (select distinct diancxxb_id,kouj,fx.fenx,fx.xuh from\n"
				+ "             (select distinct diancxxb_id,kj.jihkjb_id as kouj\n"
				+ "                     from yuercbmdj y,yuetjkjb kj\n"
				+ "                     where y.yuetjkjb_id=kj.id\n"
				+ "                     and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n"
				+ "                     or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))) ) dcid,vwfenxYue fx ) fx,\n"
				+ "     (select y.fenx,kj.diancxxb_id,kj.jihkjb_id as kouj,sum(sl.jingz) as jingz,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.jingz)/sum(sl.jingz),2)) as daoczhj,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum(y.meij*sl.jingz)/sum(sl.jingz),2)) as chebj,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum(y.yunj*sl.jingz)/sum(sl.jingz),2)) as yunj,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.jingz)/sum(sl.jingz),2)) as zaf ,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum(y.biaomdj*sl.jingz)/sum(sl.jingz),2)) as rucbmdj\n"
				+ "            from yuercbmdj y,yuetjkjb kj,yueslb sl,yuezlb zl\n"
				+ "           where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n"
				+ "           and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id\n"
				+ "           and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+)\n"
				+ "           group by (kj.diancxxb_id,kj.jihkjb_id,y.fenx)) bq,\n"
				+ "       (select y.fenx,kj.diancxxb_id,kj.jihkjb_id as kouj,sum(sl.jingz) as jingz,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.jingz)/sum(sl.jingz),2)) as daoczhj,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum(y.meij*sl.jingz)/sum(sl.jingz),2)) as chebj,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum(y.yunj*sl.jingz)/sum(sl.jingz),2)) as yunj,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.jingz)/sum(sl.jingz),2)) as zaf ,\n"
				+ "              decode(sum(sl.jingz),0,0,Round(sum(y.biaomdj*sl.jingz)/sum(sl.jingz),2)) as rucbmdj\n"
				+ "         from yuercbmdj y,yuetjkjb kj,yueslb sl,yuezlb zl\n"
				+ "              where kj.riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))\n"
				+ "              and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id\n"
				+ "              and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+)\n"
				+ "              group by (kj.diancxxb_id,kj.jihkjb_id,y.fenx)) tq,diancxxb dc,vwfengs fgs,jihkjb j\n"
				+ " where fx.diancxxb_id=tq.diancxxb_id(+)\n"
				+ "   and fx.diancxxb_id=bq.diancxxb_id(+)\n"
				+ "   and fx.diancxxb_id=dc.id\n"
				+ "   and fx.fenx=bq.fenx(+)\n"
				+ "   and fx.fenx=tq.fenx(+)\n"
				+ "   and fx.kouj=bq.kouj(+)\n"
				+ "   and fx.kouj=tq.kouj(+)\n"
				+ "   and fx.kouj=j.id\n"
				+ "   and j.id!=61\n"
				+ "   and dc.fuid=fgs.id  "+strGongsID+"\n"
				+ "   group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(fgs.mingc,fx.fenx),(fgs.mingc,fx.kouj,fx.fenx),(dc.mingc,fx.fenx),(dc.mingc,fx.kouj,fx.fenx))\n"
				+ "order by\n"
				+ "    decode(grouping(dc.mingc)+grouping(fgs.mingc)+grouping(fx.kouj),3,3,0) desc,\n"
				+ "    decode(grouping(dc.mingc)+grouping(fgs.mingc),2,2,0) desc,\n"
				+ "    min(fgs.xuh), fgs.mingc,\n"
				+ "    grouping(dc.mingc) desc,max(dc.xuh),dc.mingc,\n"
				+ "    grouping(fx.kouj) desc,fx.kouj desc,\n" + "    fx.fenx";

// 直属分厂汇总
				 ArrHeader=new String[3][14];
				 ArrHeader[0]=new String[] {"单位名称","当月或累计","到厂总合价","到厂总合价","车板(平仓)价","车板(平仓)价","运费","运费","杂费","杂费","入厂标煤单价","入厂标煤单价","天然油平均单价","天然油平均单价"};
				 ArrHeader[1]=new String[] {"单位名称","当月或累计","本期","同期","本期","同期","本期","同期","本期","同期","本期","同期","本期","同期"};
				 ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
				 
				 ArrWidth=new int[] {150,53,53,53,53,53,53,53,53,53,53,53,53,53};
				 String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
				 iFixedRows=1;
				
			//} 
			
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
			
			Table tb = new Table(rs,3, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"年"+intMonth+"月入厂煤价格情况表", ArrWidth);
			rt.setDefaultTitle(1, 3, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 2, "单位:元/吨", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(11, 3, "cpi燃料管理10表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			rt.body.setColFormat(arrFormat);
			
			tb.setColAlign(2, Table.ALIGN_CENTER);
			
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(11,2,"制表:",Table.ALIGN_LEFT);
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
		
		/*
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setWidth(120);
		//cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		*/
		
		
		
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