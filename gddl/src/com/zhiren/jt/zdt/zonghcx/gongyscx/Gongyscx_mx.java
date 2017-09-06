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
	
//	 ��ʼ����
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
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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
	private String mstrReportName="Ranlsckb";//ȼ�������챨
	
	public String getPrintTable(){
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
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		
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
					"'' as lisgx,g.meiz,decode(g.xiny,1,'��',2,'��',3,'��','��') as xiny,\n" + 
					"g.youzbm,g.danwdz,g.dianh,g.faddbr,g.weitdlr,g.gongsxz,decode(g.shifss,1,'��','��') as shifss,\n" + 
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
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
				
				
			

				 ArrHeader=new String[14][9];
				 ArrHeader[0]=new String[] {"��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ","��Ӧ����Ϣ"};
				 ArrHeader[1]=new String[] {"����:",quanc,"","","","","���:",mingc,""};
				 ArrHeader[2]=new String[] {"����:",bianm,"","","��Ӧ�����","��Ӧ�����",gongyslb,"",""};
				 ArrHeader[3]=new String[] {"����ʡ��:",shengf,"","","��������","��������",chengs,"",""};
				 ArrHeader[4]=new String[] {"������ϵ:",lisgx,"","","ú��","ú��",meiz,"",""};
				 ArrHeader[5]=new String[] {"��˾����״��","��˾����״��","��˾����״��","��˾����״��","��˾����״��","��˾����״��","��˾����״��","��˾����״��","��˾����״��"};
				 ArrHeader[6]=new String[] {"���õȼ�:",xiny,"","","��������","��������",youzbm,"",""};
				 ArrHeader[7]=new String[] {"��ϸ��ַ",danwdz,"","","","","��ϵ�绰",dianh,""};
				 ArrHeader[8]=new String[] {"����������:",faddbr,"ί�д�����","ί�д�����",weitdlr,"","��˾����",gongsxz,""};
				 ArrHeader[9]=new String[] {"�Ƿ����й�˾:",shifss,"���е�ַ","���е�ַ",shangsdz,"","��������",kaihyh,""};
				 ArrHeader[10]=new String[] {"�˺�",zhangh,"","","˰��","˰��",shuih,"","",};
				 ArrHeader[11]=new String[] {"��Դ����״��:","��Դ����״��","��Դ����״��","��Դ����״��","��Դ����״��","��Դ����״��","��Դ����״��","��Դ����״��","��Դ����״��",};
				 ArrHeader[12]=new String[] {"ú̿��Դ:",meitly,"","ú̿����",chubnl,"","��Ҫú��",kegywfmz,""};
				 ArrHeader[13]=new String[] {"��������:",shengcnl,"��Ӧ����",gongynl,"��ֵ",rez,"��������",yunsnl,""};
							
				 
				 
				 ArrWidth=new int[] {95,85,65,65,85,85,65,85,100};
				 iFixedRows=1;
				
				 
			
			
			
			
			 
			// ����
			
			rt.setBody(new Table(ArrHeader,0,0,0));
			rt.setTitle("��Ӧ����Ϣ��", ArrWidth);
			//rt.setDefaultTitle(1, 2, "��λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
			
	
			
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
		  
		    
		    
			
			//ҳ�� 
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,5,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  //rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	//�õ����ӹ����ĵ糧����
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
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
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
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
		fahdwList.add(new IDropDownBean(0,"�ֳ�����"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	���
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
	 * �·�
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

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

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
	 //	ҳ���ж�����
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
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	
	
	
	

}