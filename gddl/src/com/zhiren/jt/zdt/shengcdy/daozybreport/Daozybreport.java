package com.zhiren.jt.zdt.shengcdy.daozybreport;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;

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

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.tools.FtpUpload;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.tools.FtpCreatTxt;


import org.apache.tapestry.contrib.palette.SortMode;
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-30
 * ���������ӶԷֹ�˾�������� �Ա�û�зֹ�˾ʱ�ɲ�ѯ���ݡ�
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ�����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ����ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾�������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Daozybreport  extends BasePage implements PageValidateListener{
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

	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	//��ʼ����v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
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
	
	private boolean _ShangcChick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_ShangcChick){
			_ShangcChick=false;
		}
	}
	

	private void Refurbish() {
        //Ϊ"ˢ��" ��ť���Ӵ�������
		isBegin=true;
		getSelectData();
	}

	
//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());

			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			isBegin=true;
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setTreeid(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
				
//			this.getShouhcrb();
//			this.getSelectData();
			
		}
		if (_BeginriqChange){
			_BeginriqChange=false;
			Refurbish();
		}
//		if(_Baoblxchange){
//			_Baoblxchange=false;
//			Refurbish();
//		}
		if(_fengschange){
			_fengschange=false;
			Refurbish();
		}
		this.getSelectData();
		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}
	private String RT_HET="zhuangcyb";
	private String mstrReportName="zhuangcyb";
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

//	���缯�ŵ�ú��Ϣ�ձ���
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//������ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="��վԤ��";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
				titlename=titlename+"";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL=
					"select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc),lj.mingc,cz.mingc,tb.banjh,sum(tb.ches)\n" +
					"from tielyb tb,diancxxb dc,chezxxb cz,lujxxb lj,diancxxb gs\n" + 
					"where tb.diancxxb_id=dc.id and tb.chezxxb_id=cz.id  and gs.id=dc.fuid\n" + 
					"      and lj.id=cz.lujxxb_id and riq="+riq+strGongsID+"\n" + 
					"group by rollup(gs.mingc,(dc.mingc,lj.mingc,cz.mingc,tb.banjh))\n" + 
					"having  grouping(gs.mingc)=0\n" + 
					"order by grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc,dc.mingc";


		}else{
			strSQL=
				"select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc),lj.mingc,cz.mingc,tb.banjh,sum(tb.ches)\n" +
				"from tielyb tb,diancxxb dc,chezxxb cz,lujxxb lj,diancxxb gs\n" + 
				"where tb.diancxxb_id=dc.id and tb.chezxxb_id=cz.id  and gs.id(+)=dc.fuid\n" + 
				"      and lj.id=cz.lujxxb_id and riq="+riq+strGongsID+"\n" + 
				"group by rollup(gs.mingc,(dc.mingc,lj.mingc,cz.mingc,tb.banjh))\n" + 
				"having  grouping(gs.mingc)=0\n" + 
				"order by grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc,dc.mingc";
		}

		 ArrHeader =new String[1][5];
		 ArrHeader[0]=new String[] {"��λ","·��","��վ","��ƻ�","����"};

		 ArrWidth =new int[] {150,120,120,100,100};


			ResultSet rs = cn.getResultSet(strSQL);
			 

			
			// ����
			rt.setBody(new Table(rs,1, 0, 1));

			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//���ӱ�����A4����
			rt.setTitle(titlename, ArrWidth);
	    //  rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2,"�Ʊ���λ:"+getDiancName(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 2, riq1, Table.ALIGN_RIGHT);
		//	rt.setDefaultTitle(5, 2,"��λ:��", Table.ALIGN_RIGHT);

			rt.body.setWidth(ArrWidth);
//			rt.body.setPageRows(12);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			���ӳ��ȵ�����
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();

			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			rt.body.setColAlign(2, Table.ALIGN_LEFT);
			rt.body.setColAlign(3, Table.ALIGN_LEFT);
			//ҳ�� 
			
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,2,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(3,2,"���:",Table.ALIGN_LEFT);
			  if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				  rt.setDefautlFooter(5,0, "�Ʊ�:",Table.ALIGN_RIGHT);
					}else{
						
						rt.setDefautlFooter(5,0, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

			(),Table.ALIGN_RIGHT);
					}
//			  rt.setDefautlFooter(5,0,"�Ʊ�:",Table.ALIGN_RIGHT);
//			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			
			
			//����ҳ��
//		    rt.createDefautlFooter(ArrWidth);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
		
	}
//	******************************************************************************

	
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
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
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
	
//	�󱨱�����
//	public boolean _Baoblxchange = false;
//	public IDropDownBean getBaoblxValue() {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
//			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getIBaoblxModels().getOption(0));
//		}
//		return ((Visit)getPage().getVisit()).getDropDownBean1();
//	}
//
//	public void setBaoblxValue(IDropDownBean Value) {
//		long id = -2;
//		if (((Visit)getPage().getVisit()).getDropDownBean1() != null) {
//			id = (((Visit)getPage().getVisit()).getDropDownBean1()).getId();
//		}
//		if (Value != null) {
//			if (Value.getId() != id) {
//				_Baoblxchange = true;
//			} else {
//				_Baoblxchange = false;
//			}
//		}
//		((Visit)getPage().getVisit()).setDropDownBean1(Value);
//	}
//
//	public void setIBaoblxModel(IPropertySelectionModel value) {
//		((Visit)getPage().getVisit()).setProSelectionModel1(value);
//	}
//
//	public IPropertySelectionModel getIBaoblxModel() {
//		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
//			getIBaoblxModels();
//		}
//		return ((Visit)getPage().getVisit()).getProSelectionModel1();
//	}
//
//	public IPropertySelectionModel getIBaoblxModels() {
//
//		List fahdwList = new ArrayList();
//		fahdwList.add(new IDropDownBean(0,"�����ú��Ϣ�ձ�"));
//		
//		fahdwList.add(new IDropDownBean(1,"�����ú��Ϣ�ձ�(ʡ)"));
//		fahdwList.add(new IDropDownBean(2,"��ú�����ձ���"));
//		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(fahdwList));
//		
//		return ((Visit)getPage().getVisit()).getProSelectionModel1();
//	}
	
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
	
//	***************************������ʼ����***************************//
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
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
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
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("������ѯ:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setId("Tongjkj");
//		cb.setWidth(240);
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
//		
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