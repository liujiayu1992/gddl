package com.zhiren.jt.jihgl.benyfkddh_lj;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
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



public class Benyfkddh_lj  extends BasePage implements PageValidateListener {
	

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
//	
	
	// 日期是否变化
	private boolean riqchange = false;
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (this.briq != null) {
			if (!this.briq.equals(briq))
				riqchange = true;
		}
//		this.riq = riq;
		
		this.briq = briq;
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}


	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		try {
			getSelectData();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = new Date();
			visit.setList1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setDiancmcValue(null);
			this.getFengsModels();
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setTreeid(null);
			
			setBRiq(DateUtil.FormatDate(new Date()));
			
			isBegin=true;
			//this.getSelectData();
		}
		
		getToolBar();
		Refurbish();
	}
	
	private String RT_HET="dinghjhcx";
	private String mstrReportName="dinghjhcx";
	
	public String getPrintTable() throws SQLException{
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
	 * @author xzy
	 */
	private String getSelectData() throws SQLException{
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		
		
		int jib=this.getDiancTreeJib();
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="本月分矿点到货情况表";
		int iFixedRows=0;//固定行号
		
		StringBuffer strSQL = new StringBuffer();
		
		StringBuffer strgrouping = new StringBuffer();
		StringBuffer strwhere = new StringBuffer();
		StringBuffer strgroupby = new StringBuffer();
		StringBuffer strhaving = new StringBuffer();
		StringBuffer strorderby = new StringBuffer();
		String sqll="select id from diancxxb d where   d.fuid="+visit.getDiancxxb_id()+" order by id ";
		ResultSetList rsl = cn.getResultSetList(sqll);
		String daq= getBRiq();
		String yuep= "";
		Calendar   cal   =   new   GregorianCalendar();   
	    //或者用Calendar   cal   =   Calendar.getInstance();   
	  
	    /**设置date**/  
	    SimpleDateFormat oSdf = new SimpleDateFormat ("",Locale.ENGLISH);   
	    oSdf.applyPattern("yyyyMM");   
	    try {   
//	        System.out.println(oSdf.parse(daq));   
	        cal.setTime(oSdf.parse(daq));   
	    } catch (ParseException e) {   
	        e.printStackTrace();   
	    }   
	    int num2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);   
	    
//	    System.out.println(num2);   

		
//		System.out.println(daq.substring(5,6)+daq.substring(6,7)+"@"+daq.substring(6,7));
		if("0".equals( daq.substring(5,6))){
			
//			System.out.println(daq.substring(6,7));
			yuep=daq.substring(6,7);
		}else{
			yuep=daq.substring(5,7);
//			System.out.println(daq.substring(5,7));
		}
		int a=110;
		if (rsl.getRows() > 0) {
			strSQL.append("select mingc,nianj,tianj,yuej,shijt,shijy,daht,dahy,riby,chae from (");
			strSQL
					.append(
							"(select dc.mingc,d.value nianj,round_new(d.y"+yuep+"/"+num2+",2) tianj,d.y"+yuep+" yuej,d shijt,y shijy,round_new(d*100/round_new(d.y"+yuep+"/"+num2+",2),2) daht,round_new(y*100/d.y"+yuep+",2) dahy,'' as riby,'' as chae,'1' as xuh\n" +
							"from\n" + 
							"(select sum(value) value,sum(y"+yuep+") y"+yuep+" from ranlxyjhb ran,ranlxyjhzbb rz,diancxxb dc, item it where dc.fuid="+visit.getDiancxxb_id()+"\n" + 
							" and to_char(rz.nianf,'yyyy') = '"+daq.substring(0,4)+"' \n" + 
							"and it.mingc='入厂煤炭采购计划'\n" + 
							" and ran.zhibmc_item_id=it.id\n" + 
							" and ran.ranlxyjhzbb_id=rz.id\n" + 
							"  ) d ,\n" + 
							"  (\n" + 
							"  select  round_new(sum(f.laimsl)/10000,2) d from fahb f ,diancxxb dc,jihkjb j\n" + 
							"  where\n" + 
							" f.daohrq=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
							"   and f.diancxxb_id=dc.id\n" + 
							"   and dc.fuid="+visit.getDiancxxb_id()+"\n" + 
							"   and f.jihkjb_id=j.id\n" + 
							"   and j.mingc='重点订货'\n" + 
							"  ) shij,\n" + 
							"  (\n" + 
							"  select round_new(sum(f.laimsl)/10000,2) y from fahb f ,diancxxb dc,jihkjb j\n" + 
							"  where\n" + 
							" f.daohrq<=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
							" and f.daohrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
							"   and f.diancxxb_id=dc.id\n" + 
							"   and dc.fuid="+visit.getDiancxxb_id()+"\n" + 
							"   and f.jihkjb_id=j.id\n" + 
							"  ) shiy,diancxxb dc\n" + 
							"  where dc.id="+visit.getDiancxxb_id()+")\n" + 
							""
);
		}
		
//		System.out.println(daq.substring(0,4));
		while(rsl.next()){
			a++;
				long did=rsl.getLong("id");
				strSQL.append("union"+
						"(select dc.mingc,0 nianj,0 tianj,0 yuej,d shijt,y shijy,0 daht,0 dahy,'' as riby,'' as chae,'"+a+"' as xuh\n" +
						"from\n" + 
						"(select sum(value) value,sum(y"+yuep+") y"+yuep+" from ranlxyjhb ran,ranlxyjhzbb rz,diancxxb dc, item it where dc.id="+did+"\n" + 
						" and to_char(rz.nianf,'yyyy') = '"+daq.substring(0,4)+"' \n" + 
						"and it.mingc='入厂煤炭采购计划'\n" + 
						" and ran.zhibmc_item_id=it.id\n" + 
						" and ran.ranlxyjhzbb_id=rz.id\n" + 
						"  ) d ,\n" + 
						"  (\n" + 
						"  select  round_new(sum(f.laimsl)/10000,2) d from fahb f ,diancxxb dc,jihkjb j\n" + 
						"  where\n" + 
						" f.daohrq=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						"   and f.diancxxb_id=dc.id\n" + 
						"   and dc.id="+did+"\n" + 
						"   and f.jihkjb_id=j.id\n" + 
						"   and j.mingc='重点订货'\n" + 
						"  ) shij,\n" + 
						"  (\n" + 
						"  select round_new(sum(f.laimsl)/10000,2) y from fahb f ,diancxxb dc,jihkjb j\n" + 
						"  where\n" + 
						" f.daohrq<=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						" and f.daohrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						"   and f.diancxxb_id=dc.id\n" + 
						"   and dc.id="+did+"\n" + 
						"   and f.jihkjb_id=j.id\n" + 
						"  ) shiy,diancxxb dc\n" + 
						"  where dc.id="+did+")\n"
						
);
				a++;
				strSQL.append("union"+
						"(select '黑龙江龙煤矿业集团有限责任公司' as mingc,d.value nianj,round_new(d.y"+yuep+"/"+num2+",2) tianj,d.y"+yuep+" yuej,d shijt,y shijy,round_new(d*100/round_new(d.y"+yuep+"/"+num2+",2),2) daht,round_new(y*100/d.y"+yuep+",2) dahy,'' as riby,'' as chae,'"+(a)+"' as xuh\n" +
						"from\n" + 
						"(select sum(value) value,sum(y"+yuep+") y"+yuep+" from ranlxyjhb ran,ranlxyjhzbb rz,diancxxb dc, item it where dc.id="+did+"\n" + 
						" and to_char(rz.nianf,'yyyy') = '"+daq.substring(0,4)+"' \n" + 
						"and it.mingc='入厂煤炭采购计划'\n" + 
						" and ran.zhibmc_item_id=it.id\n" + 
						" and ran.ranlxyjhzbb_id=rz.id\n" + 
						"  ) d ,\n" + 
						"  (\n" + 
						"  select  round_new(sum(f.laimsl)/10000,2) d from fahb f ,diancxxb dc,jihkjb j\n" + 
						"  where\n" + 
						" f.daohrq=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						"   and f.diancxxb_id=dc.id\n" + 
						"   and dc.id="+did+"\n" + 
						"   and f.jihkjb_id=j.id\n" + 
						"   and j.mingc='重点订货'\n" + 
						"  ) shij,\n" + 
						"  (\n" + 
						"  select round_new(sum(f.laimsl)/10000,2) y from fahb f ,diancxxb dc,jihkjb j\n" + 
						"  where\n" + 
						" f.daohrq<=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						" and f.daohrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						"   and f.diancxxb_id=dc.id\n" + 
						"   and dc.id="+did+"\n" + 
						"   and f.jihkjb_id=j.id\n" + 
						"  ) shiy,diancxxb dc\n" + 
						"  where dc.id="+did+")\n"
)	;
				a++;
				strSQL.append("union"+
						"(select '地方自购煤炭' as mingc,0 nianj,0 tianj,0 yuej,d shijt,y shijy,0 daht,0 dahy,'' as riby,'' as chae,'"+(a)+"' as xuh\n" +
						"from\n" + 
						"(select sum(value) value,sum(y"+yuep+") y"+yuep+""+" from ranlxyjhb ran,ranlxyjhzbb rz,diancxxb dc, item it where dc.id="+did+"\n" + 
						" and to_char(rz.nianf,'yyyy') = '"+daq.substring(0,4)+"' \n" +  
						"and it.mingc='入厂煤炭采购计划'\n" + 
						" and ran.zhibmc_item_id=it.id\n" + 
						" and ran.ranlxyjhzbb_id=rz.id\n" + 
						"  ) d ,\n" + 
						"  (\n" + 
						"  select  round_new(sum(f.laimsl)/10000,2) d from fahb f ,diancxxb dc,jihkjb j\n" + 
						"  where\n" + 
						" f.daohrq=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						"   and f.diancxxb_id=dc.id\n" + 
						"   and dc.id="+did+"\n" + 
						"   and f.jihkjb_id=j.id\n" + 
						"   and j.mingc<>'重点订货'\n" + 
						"  ) shij,\n" + 
						"  (\n" + 
						"  select round_new(sum(f.laimsl)/10000,2) y from fahb f ,diancxxb dc,jihkjb j\n" + 
						"  where\n" + 
						" f.daohrq<=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
						" and f.daohrq>=to_date('"+daq.substring(0,8)+"01"+"','yyyy-mm-dd')\n" + 
						"   and f.diancxxb_id=dc.id\n" + 
						"   and dc.id="+did+"\n" + 
						"   and f.jihkjb_id=j.id\n" + 
						"  ) shiy,diancxxb dc\n" + 
						"  where dc.id="+did+")\n"
)	;
			}
		strSQL.append(" )order by xuh \n");
		 ArrHeader=new String[2][10];
		 ArrHeader[0]=new String[] {"名      称","计划","计划","计划","实供","实供","到货率","到货率",""," "};
		 ArrHeader[1]=new String[] {"名      称","年累计<br>(万吨)","当日<br>(万吨)","月累计<br>(万吨)","当日<br>(万吨)","月累计<br>(万吨)","当日<br>(%)","月累计<br>(%)","日报<br>月累计","差额"};
//		 rt.title.setRowHeight(2,50);
		ResultSet rs = cn.getResultSet(strSQL.toString());
//		System.out.println(strSQL.toString());
		ArrWidth=new int []{210,70,70,70,70,70,70,70,70,70};
		rt.setTitle(titlename, ArrWidth);
		String zhibdw=this.getDiancmc();
		
		rt.setDefaultTitle(1, 4, "填报单位:"+zhibdw, Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2,"时间：" +daq.replaceFirst("-", "年").replaceFirst("-", "月")+"日", Table.ALIGN_CENTER);

		rt.setBody(new Table(rs, 2, 0, 0));
//		rt.body.useCss = true;
		
//		rt.body.setUseCss(true);
//		rt.body.setUseDefaultCss(true);
//		rt.body.setRowClassName(3, "tab_data_line_one_lj_1");
//		rt.body.setRowClassName(5, "tab_data_line_one_lj");
//		rt.body.setRowClassName(8, "tab_data_line_one_lj");
//		rt.body.setRowClassName(11, "tab_data_line_one_lj");
//		rt.body.setRowClassName(14, "tab_data_line_one_lj");
//		rt.body.setCellClassName(3, 3, "tab_data_line_one_lj");//.getCellStyle(1, 1). .setRowClassName(4, "tab_data_line_one_lj");
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(35);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero =false;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		
//System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
	}
	private String userName=""; 
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
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
		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
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
		fahdwList.add(new IDropDownBean(0,"分厂"));
		fahdwList.add(new IDropDownBean(1,"分矿"));
		fahdwList.add(new IDropDownBean(2,"分厂分矿"));
		fahdwList.add(new IDropDownBean(3,"分矿分厂"));
		fahdwList.add(new IDropDownBean(4,"分矿百分比表"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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

public void getToolBar() {
	Toolbar tb1 = new Toolbar("tbdiv");
	
	

	tb1.addText(new ToolbarText("选择日期:"));
	DateField dfb = new DateField();
	dfb.setValue(getBRiq());
	dfb.Binding("BRiq", "Form0");// 与html页中的id绑定,并自动刷新
	dfb.setId("riq");
	tb1.addField(dfb);
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
