package com.zhiren.jt.zdt.zonghcx.gongyscx;

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


public class Gongyscx_mx  extends BasePage implements PageValidateListener{
	
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
	
//	 开始日期
	private Date _BeginriqValue = new Date();

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (DateUtil.Formatdate("yyyy-MM-dd", _BeginriqValue).equals(
				DateUtil.Formatdate("yyyy-MM-dd", _value))) {
			_BeginriqChange = false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange = true;
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
			setBaoblxValue(null);
			getIBaoblxModels();
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
		if(_Baoblxchange){
			_Baoblxchange=false;
			Refurbish();
		}
		
		visit.setLong1(0);
		if (cycle.getRequestContext().getParameter("gongysb_id") != null&&!cycle.getRequestContext().getParameter("gongysb_id").equals("-1")) {
				visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("gongysb_id")));
			}
		Refurbish();
	}
	
	private String RT_HET="Ranlsckb";
	private String mstrReportName="Ranlsckb";//燃料生产快报
	
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
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		
		String quanc="";
		String mingc="";
		String bianm="";
		String gongyslb="";
		String shengf="";
		String chengs="";
		String lisgx="";
		String meiz="";
		String xiny="";
		String youzbm="";
		String danwdz="";
		String dianh="";
		String faddbr="";
		String weitdlr="";
		String gongsxz="";
		String shifss="";
		String shangsdz="";
		String kaihyh="";
		String zhangh="";
		String shuih="";
		String meitly="";
		String chubnl="";
		String kegywfmz="";
		String shengcnl="";
		String gongynl="";
		String rez="";
		String yunsnl="";
			
			
		
		
		
		long gongysb_id=((Visit) getPage().getVisit()).getLong1();
		
			
				strSQL =
					"select g.quanc,g.mingc,g.bianm,'' as gongyslb,sf.quanc as shengf,cs.quanc as chengs,\n" +
					"'' as lisgx,g.meiz,decode(g.xiny,1,'优',2,'良',3,'可','良') as xiny,\n" + 
					"g.youzbm,g.danwdz,g.dianh,g.faddbr,g.weitdlr,g.gongsxz,decode(g.shifss,1,'是','否') as shifss,\n" + 
					"g.shangsdz,g.kaihyh,g.zhangh,g.shuih,g.meitly,g.chubnl,g.kegywfmz,g.shengcnl,g.gongynl,'' as rez,g.yunsnl\n" + 
					"from gongysb g,shengfb sf,chengsb cs\n" + 
					" where g.shengfb_id=sf.id(+)\n" + 
					" and g.chengsb_id=cs.id(+)\n" + 
					" and g.id="+gongysb_id+"";

				


				ResultSet rs = cn.getResultSet(strSQL);
				try {
					if(rs.next()){
						 quanc=rs.getString("quanc");
						 mingc=rs.getString("mingc");
						 bianm=rs.getString("bianm");
						 gongyslb=rs.getString("gongyslb");
						 shengf=rs.getString("shengf");
						 chengs=rs.getString("chengs");
						 lisgx=rs.getString("lisgx");
						 meiz=rs.getString("meiz");
						 xiny=rs.getString("xiny");
						 youzbm=rs.getString("youzbm");
						 danwdz=rs.getString("danwdz");
						 dianh=rs.getString("dianh");
						 faddbr=rs.getString("faddbr");
						 weitdlr=rs.getString("weitdlr");
						 gongsxz=rs.getString("gongsxz");
						 shifss=rs.getString("shifss");
						 shangsdz=rs.getString("shangsdz");
						 kaihyh=rs.getString("kaihyh");
						 zhangh=rs.getString("zhangh");
						 shuih=rs.getString("shuih");
						 meitly=rs.getString("meitly");
						 chubnl=rs.getString("chubnl");
						 kegywfmz=rs.getString("kegywfmz");
						 shengcnl=rs.getString("shengcnl");
						 gongynl=rs.getString("gongynl");
						 rez=rs.getString("rez");
						 yunsnl=rs.getString("yunsnl");
						
						
					}
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				
				
			

				 ArrHeader=new String[14][9];
				 ArrHeader[0]=new String[] {"供应商信息","供应商信息","供应商信息","供应商信息","供应商信息","供应商信息","供应商信息","供应商信息","供应商信息"};
				 ArrHeader[1]=new String[] {"名称:",quanc,"","","","","简称:",mingc,""};
				 ArrHeader[2]=new String[] {"编码:",bianm,"","","供应商类别","供应商类别",gongyslb,"",""};
				 ArrHeader[3]=new String[] {"所属省份:",shengf,"","","所属城市","所属城市",chengs,"",""};
				 ArrHeader[4]=new String[] {"隶属关系:",lisgx,"","","煤种","煤种",meiz,"",""};
				 ArrHeader[5]=new String[] {"公司整体状况","公司整体状况","公司整体状况","公司整体状况","公司整体状况","公司整体状况","公司整体状况","公司整体状况","公司整体状况"};
				 ArrHeader[6]=new String[] {"信用等级:",xiny,"","","邮政编码","邮政编码",youzbm,"",""};
				 ArrHeader[7]=new String[] {"详细地址",danwdz,"","","","","联系电话",dianh,""};
				 ArrHeader[8]=new String[] {"法定代表人:",faddbr,"委托代理人","委托代理人",weitdlr,"","公司性质",gongsxz,""};
				 ArrHeader[9]=new String[] {"是否上市公司:",shifss,"上市地址","上市地址",shangsdz,"","开户银行",kaihyh,""};
				 ArrHeader[10]=new String[] {"账号",zhangh,"","","税号","税号",shuih,"","",};
				 ArrHeader[11]=new String[] {"资源基本状况:","资源基本状况","资源基本状况","资源基本状况","资源基本状况","资源基本状况","资源基本状况","资源基本状况","资源基本状况",};
				 ArrHeader[12]=new String[] {"煤炭来源:",meitly,"","煤炭储备",chubnl,"","主要煤种",kegywfmz,""};
				 ArrHeader[13]=new String[] {"生产能力:",shengcnl,"供应能力",gongynl,"热值",rez,"运输能力",yunsnl,""};
							
				 
				 
				 ArrWidth=new int[] {95,85,65,65,85,85,65,85,100};
				 iFixedRows=1;
				
				 
			
			
			
			
			 
			// 数据
			
			rt.setBody(new Table(ArrHeader,0,0,0));
			rt.setTitle("供应商信息表", ArrWidth);
			//rt.setDefaultTitle(1, 2, "单位:"+this.getDiancmc(), Table.ALIGN_LEFT);
			
	
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(100);
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			
			
		    rt.body.mergeCell(1,1,1,9);
		    rt.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		    rt.body.mergeCell(2,2,2,6);
		    rt.body.mergeCell(2,8,2,9);
		    rt.body.mergeCell(3,2,3,4);
		    rt.body.mergeCell(3,5,3,6);
		    rt.body.mergeCell(3,7,3,9);
		    rt.body.mergeCell(4,2,4,4);
		    rt.body.mergeCell(4,5,4,6);
		    rt.body.mergeCell(4,7,4,9);
		    
		    rt.body.mergeCell(5,2,5,4);
		    rt.body.mergeCell(5,5,5,6);
		    rt.body.mergeCell(5,7,5,9);
		    
		    rt.body.mergeCell(6,1,6,9);
		    
		    rt.body.mergeCell(7,2,7,4);
		    rt.body.mergeCell(7,5,7,6);
		    rt.body.mergeCell(7,7,7,9);
		    
		    rt.body.mergeCell(8,2,8,6);
		    rt.body.mergeCell(8,8,8,9);
		    
		    rt.body.mergeCell(9,3,9,4);
		    rt.body.mergeCell(9,5,9,6);
		    rt.body.mergeCell(9,8,9,9);
		    
		    rt.body.mergeCell(10,3,10,4);
		    rt.body.mergeCell(10,5,10,6);
		    rt.body.mergeCell(10,8,10,9);
		    
		    rt.body.mergeCell(11,2,11,4);
		    rt.body.mergeCell(11,5,11,6);
		    rt.body.mergeCell(11,7,11,9);
		    
		    rt.body.mergeCell(12,1,12,9);
		    
		    rt.body.mergeCell(13,2,13,3);
		    rt.body.mergeCell(13,5,13,6);
		    rt.body.mergeCell(13,8,13,9);
		    
		  // rt.body.mergeCell(14,2,14,3);
		 //  rt.body.mergeCell(14,5,14,6);
		    rt.body.mergeCell(14,8,14,9);
		  
		   for (int i=1;i<=9;i++){
		    	rt.body.setColAlign(i, Table.ALIGN_CENTER);
			   
		    } 
		  
		    
		    
			
			//页脚 
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,5,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  //rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	//得到连接过来的电厂名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getLong1();
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
	
//	矿报表类型
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
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
		try{
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"分厂汇总"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
	
	
	
	
	

}