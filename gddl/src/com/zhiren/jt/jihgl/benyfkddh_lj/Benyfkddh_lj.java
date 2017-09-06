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
	

//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
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
	
	// �����Ƿ�仯
	private boolean riqchange = false;
//	������
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
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		try {
			getSelectData();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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
			return "�޴˱���";
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
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="���·ֿ�㵽�������";
		int iFixedRows=0;//�̶��к�
		
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
	    //������Calendar   cal   =   Calendar.getInstance();   
	  
	    /**����date**/  
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
							"and it.mingc='�볧ú̿�ɹ��ƻ�'\n" + 
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
							"   and j.mingc='�ص㶩��'\n" + 
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
						"and it.mingc='�볧ú̿�ɹ��ƻ�'\n" + 
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
						"   and j.mingc='�ص㶩��'\n" + 
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
						"(select '��������ú��ҵ�����������ι�˾' as mingc,d.value nianj,round_new(d.y"+yuep+"/"+num2+",2) tianj,d.y"+yuep+" yuej,d shijt,y shijy,round_new(d*100/round_new(d.y"+yuep+"/"+num2+",2),2) daht,round_new(y*100/d.y"+yuep+",2) dahy,'' as riby,'' as chae,'"+(a)+"' as xuh\n" +
						"from\n" + 
						"(select sum(value) value,sum(y"+yuep+") y"+yuep+" from ranlxyjhb ran,ranlxyjhzbb rz,diancxxb dc, item it where dc.id="+did+"\n" + 
						" and to_char(rz.nianf,'yyyy') = '"+daq.substring(0,4)+"' \n" + 
						"and it.mingc='�볧ú̿�ɹ��ƻ�'\n" + 
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
						"   and j.mingc='�ص㶩��'\n" + 
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
						"(select '�ط��Թ�ú̿' as mingc,0 nianj,0 tianj,0 yuej,d shijt,y shijy,0 daht,0 dahy,'' as riby,'' as chae,'"+(a)+"' as xuh\n" +
						"from\n" + 
						"(select sum(value) value,sum(y"+yuep+") y"+yuep+""+" from ranlxyjhb ran,ranlxyjhzbb rz,diancxxb dc, item it where dc.id="+did+"\n" + 
						" and to_char(rz.nianf,'yyyy') = '"+daq.substring(0,4)+"' \n" +  
						"and it.mingc='�볧ú̿�ɹ��ƻ�'\n" + 
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
						"   and j.mingc<>'�ص㶩��'\n" + 
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
		 ArrHeader[0]=new String[] {"��      ��","�ƻ�","�ƻ�","�ƻ�","ʵ��","ʵ��","������","������",""," "};
		 ArrHeader[1]=new String[] {"��      ��","���ۼ�<br>(���)","����<br>(���)","���ۼ�<br>(���)","����<br>(���)","���ۼ�<br>(���)","����<br>(%)","���ۼ�<br>(%)","�ձ�<br>���ۼ�","���"};
//		 rt.title.setRowHeight(2,50);
		ResultSet rs = cn.getResultSet(strSQL.toString());
//		System.out.println(strSQL.toString());
		ArrWidth=new int []{210,70,70,70,70,70,70,70,70,70};
		rt.setTitle(titlename, ArrWidth);
		String zhibdw=this.getDiancmc();
		
		rt.setDefaultTitle(1, 4, "���λ:"+zhibdw, Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2,"ʱ�䣺" +daq.replaceFirst("-", "��").replaceFirst("-", "��")+"��", Table.ALIGN_CENTER);

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
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	
//	�糧����
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
	
//	�������
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

	// Page����

	
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
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
//	 �ֹ�˾������
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
		setDiancxxModel(new IDropDownModel(sql,"�й����Ƽ���"));
	}
//	�󱨱�����
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
		fahdwList.add(new IDropDownBean(0,"�ֳ�"));
		fahdwList.add(new IDropDownBean(1,"�ֿ�"));
		fahdwList.add(new IDropDownBean(2,"�ֳ��ֿ�"));
		fahdwList.add(new IDropDownBean(3,"�ֿ�ֳ�"));
		fahdwList.add(new IDropDownBean(4,"�ֿ�ٷֱȱ�"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}

public void getToolBar() {
	Toolbar tb1 = new Toolbar("tbdiv");
	
	

	tb1.addText(new ToolbarText("ѡ������:"));
	DateField dfb = new DateField();
	dfb.setValue(getBRiq());
	dfb.Binding("BRiq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
	dfb.setId("riq");
	tb1.addField(dfb);
	tb1.addText(new ToolbarText("-"));

	
	
	

	
	
	ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
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
