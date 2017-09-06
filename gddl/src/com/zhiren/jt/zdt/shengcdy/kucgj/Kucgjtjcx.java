package com.zhiren.jt.zdt.shengcdy.kucgj;


import org.apache.tapestry.html.BasePage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Kucgjtjcx extends BasePage {
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
	
//	��ʼ����v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		Date beginDate = new Date();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			beginDate = sdf.parse(DateUtil.getYear(new Date())+"-01-01");
		}catch(Exception e){
			e.printStackTrace();
		}
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(beginDate);
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_BeginriqChange=true;
		}
	}
	
	//	��ʼ����v
	private boolean _EndChange=false;
	public Date getEndDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_EndChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndChange=true;
		}
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
	
	private String REPORT_KUCGJTJCX="kucgjtjcx";
	private String mstrReportName="";
	
	//�õ���½��Ա�����糧��ֹ�˾������
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
		}finally{
			cn.Close();
		}
			
		if(diancmc.equals("���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���")){
	          return "���ƹ���ȼ�Ϲ���";
		}else{
			return diancmc;
		}
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_KUCGJTJCX)){
			return getKucgjtjcx();
		}else{
			return "�޴˱���";
		}
	}

	public String getKucgjtjcx(){
		
		_CurrentPage=1;
		_AllPages=1;
		Date bgdat=getBeginriqDate();//����
		Date eddat=getEndDate();//����
		String strbgDate=DateUtil.FormatDate(bgdat);//�����ַ�
		String stredDate=DateUtil.FormatDate(eddat);//�����ַ�
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
			
		//lxΪ���������޸��ɴ�ú���������������ھ����ú�����������������������
		String Str="&'||'diancxxb_id='||bt.id||'&'||'beginriq="+strbgDate+"'||'&'||'endriq="+stredDate+"'||'&'||'tians='||jj.tians||'&'||'lx=";
	//	System.out.println(Str);
		String sql = 
					//"select bt.mingc as �糧����,xfh.tians as �޸�������,ts.zongts as ��������,\n"
					"select bt.mingc as �糧����,getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','DiaoymxReport','"+Str+"xfh',xfh.tians) as �޸�������,\n"
					+ "getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','DiaoymxReport','"+Str+"jj',ts.zongts) as ��������,\n"
					+ "       --decode(xfh.tians,0,0,decode(xfh.xianfhcs,0,1,xfh.xianfhcs)) as xianfhcs,--�޸��ɴ���\n"
					+ "       --decode(ts.zongts,0,0,decode(ts.jingjcs,0,1,ts.jingjcs)) as jingjcs,--�������\n"
					+ "       decode(ts.jingjcs,0,ts.zongts,round(ts.zongts/ts.jingjcs)) as ƽ����������, \n"//jj.tians as ���������,
					+ "getHtmlAllAlert('"+MainGlobal.getHomeContext(this)+"','DiaoymxReport','"+Str+"zc',jj.tians) as ���������,\n"
					+ "       bt.xianfhkc as �޸��ɿ��,bt.jingjcml as ������ \n"
					+ "  from \n"
					+ "(select mingc,max(tians) as tians,sum(jingjcs) as jingjcs from \n"
					+ "(select sj.mingc,sj.jingjrq as riq,sum(1) as tians,sum(jingjcs) as jingjcs from \n"
					+ "(select dc.mingc,rb.riq,getJingjkcgj(dc.id,rb.riq) as jingjcs,getJingjkcgjrq(dc.id,rb.riq) as jingjrq \n"
					+ "  from shouhcrbb rb,diancxxb dc \n"
					+ " where rb.diancxxb_id=dc.id and rb.kuc<dc.jingjcml "+str+" \n"
					+ "   and rb.riq>=to_date('"+strbgDate+"','yyyy-mm-dd') and rb.riq<=to_date('"+stredDate+"','yyyy-mm-dd') ) sj \n"
					+ " group by (sj.mingc,sj.jingjrq) )  group by mingc ) jj, \n"
					+ "(select dc.mingc,sum(getXianfhkcgj(dc.id,rb.riq)) as xianfhcs,sum(1) as tians \n"
					+ "  from shouhcrbb rb,diancxxb dc \n"
					+ " where rb.diancxxb_id=dc.id and rb.kuc<dc.xianfhkc "+str+" \n"
					+ "   and rb.riq>=to_date('"+strbgDate+"','yyyy-mm-dd') and rb.riq<=to_date('"+stredDate+"','yyyy-mm-dd')  group by (dc.mingc) ) xfh, \n"
					+ " \n"
//					+ "(select mingc,sum(tians) as zongts,sum(jingjcs) as jingjcs from \n"
//					+ "(select sj.mingc,sj.jingjrq as riq,sum(1) as tians,sum(jingjcs) as jingjcs from \n"
//					+ "(select dc.mingc,rb.riq,getJingjkcgj(dc.id,rb.riq) as jingjcs,getJingjkcgjrq(dc.id,rb.riq) as jingjrq \n"
//					+ "  from shouhcrbb rb,diancxxb dc\n"
//					+ " where rb.diancxxb_id=dc.id and rb.kuc<dc.jingjcml "+str+" \n"
//					+ "   and rb.riq>=to_date('"+strbgDate+"','yyyy-mm-dd') and rb.riq<=to_date('"+stredDate+"','yyyy-mm-dd') ) sj \n"
//					+ " group by (sj.mingc,sj.jingjrq) ) zs group by mingc) ts, \n"
					+"(select mingc,sum(case when rb.kuc<dc.jingjcml then 1 else 0 end ) as zongts,\n" 
					+"    getJingjcs(dc.id,to_date('"+strbgDate+"','yyyy-mm-dd'),to_date('"+stredDate+"','yyyy-mm-dd')) as jingjcs\n"  
					+"    from shouhcrbb rb,diancxxb dc\n"  
					+"    where rb.diancxxb_id=dc.id\n" 
					+"    and (dc.id = 158 or dc.fuid = 158 or dc.shangjgsid= 158)\n"  
					+"    and rb.riq>=to_date('"+strbgDate+"','yyyy-mm-dd')\n"  
					+"    and rb.riq<=to_date('"+stredDate+"','yyyy-mm-dd')\n"  
					+"  group by dc.id,mingc) TS,"
					+ " \n"
					+ " (select distinct dc.id,dc.mingc,dc.xuh,dc.xianfhkc,dc.jingjcml from shouhcrbb rb,diancxxb dc where rb.diancxxb_id=dc.id and rb.kuc<dc.jingjcml \n"
					+ " and rb.riq>=to_date('"+strbgDate+"','yyyy-mm-dd') and rb.riq<=to_date('"+stredDate+"','yyyy-mm-dd') "+str+" \n"
					+ " union\n"
					+ " select distinct dc.id,dc.mingc,dc.xuh,dc.xianfhkc,dc.jingjcml from shouhcrbb rb,diancxxb dc where rb.diancxxb_id=dc.id and rb.kuc<dc.xianfhkc \n"
					+ " and rb.riq>=to_date('"+strbgDate+"','yyyy-mm-dd') and rb.riq<=to_date('"+stredDate+"','yyyy-mm-dd') "+str+") bt \n"
					+ " \n"
					+ "where bt.mingc=jj.mingc(+) and bt.mingc=ts.mingc(+) and bt.mingc=xfh.mingc(+) order by bt.xuh ";

		 String ArrHeader[][]=new String[2][7];
		 ArrHeader[0]=new String[] {"�糧","����","����","ƽ����������","���������","�޸��ɿ��","������"};
		 ArrHeader[1]=new String[] {"�糧","�����޸��ɴ�ú��","���ھ����ú��","ƽ����������","���������","�޸��ɿ��","������"};


		int ArrWidth[]=new int[] {120,120,120,80,80,80,80};
		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("���澯ͳ��",ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3,  FormatDate(DateUtil.getDate(strbgDate))+"-"+FormatDate(DateUtil.getDate(stredDate)),Table.ALIGN_CENTER);
		rt.setDefaultTitle(6, 2, "��λ:��",Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(rs,2,0,1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(36);
		rt.body.ShowZero = true;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
//		ҳ�� 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(3,2,"���:",Table.ALIGN_CENTER);
		 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
				
			 rt.setDefautlFooter(6,2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					 rt.setDefautlFooter(6,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}
//		 rt.setDefautlFooter(6,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		 
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		Date beginDate = new Date();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			beginDate = sdf.parse(DateUtil.getYear(new Date())+"-01-01");
		}catch(Exception e){
			e.printStackTrace();
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(beginDate);
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setTreeid(null);
			this.setBeginriqDate(visit.getMorkssj());
			this.setEndDate(visit.getMorjssj());
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(beginDate);
					visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					this.setTreeid(null);
					this.setBeginriqDate(visit.getMorkssj());
					this.setEndDate(visit.getMorjssj());
				}
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }
		
		getToolBars();
		isBegin=true;
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
		setDiancxxModel(new IDropDownModel(sql,"�е�Ͷ"));
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
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��ʼ����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��������:"));
		DateField enddf = new DateField();
		enddf.setValue(DateUtil.FormatDate(this.getEndDate()));
		enddf.Binding("endRiqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		enddf.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(enddf);
		
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
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
//		ToolbarButton tbLink = new ToolbarButton(null,"��ӡ","function(){}");
		
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
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
	
//  Day������
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
//        listDay.add(new IDropDownBean(-1, "��ѡ��"));
    	for (int i = 1; i < 32; i++) {
            listDay.add(new IDropDownBean(i, String.valueOf(i)+"��"));
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
