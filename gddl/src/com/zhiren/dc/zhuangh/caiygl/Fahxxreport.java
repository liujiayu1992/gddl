package com.zhiren.dc.zhuangh.caiygl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;



public class Fahxxreport extends BasePage {
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
	
//	��ʼ����v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
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
	
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndriqChange=true;
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
	
	private String REPORT_JIZYXQK="Report";
	private String REPORT_QUEMTJBG="quemtjbg";
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
		}
		cn.Close();
		if(diancmc.equals("���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���")){
	          return "���ƹ���ȼ�Ϲ���";
		}else{
			return diancmc;
		}
		
	}
	
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_JIZYXQK)){
			return getJizyxqkreport();
		}else{
			return getJizyxqkreport();
		}
	}

	public String getJizyxqkreport(){
		Visit visit = (Visit) getPage().getVisit();
		_CurrentPage=1;
		_AllPages=1;
		Date dat=new Date();//getBeginriqDate();//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = " and dc.jib=3 ";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";
		}
		
		//ȡ��ҳ���ѯ��ʽ
		String zhuangt=this.getBaoblxValue().getStrId();
		
		//��Ź���Ĳ���sql���
		String biaoz="=";
		
		//�������Ĺ���
		if(zhuangt.equals("0")){
			biaoz=" and "+
			"rb.shangmkssj >=\n" +
			"to_date(to_char(sysdate-1,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
			"and rb.shangmjssj< to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss') \n"+
			" and rb.shangmjssj is not null";


		}else if(zhuangt.equals("1")){
			biaoz="and "+
				"rb.shangmkssj >=\n" +
				"to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
				"and rb.shangmjssj< sysdate  \n"+
			" and rb.shangmjssj is not null"
;
		}else if(zhuangt.equals("2")){
			biaoz=
		" and rb.shangmjssj is  null";
		}else if(zhuangt.equals("3")){
			biaoz=" and rb.shangmkssj > to_date(to_char(sysdate-1,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" +
			"and rb.shangmkssj< to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss') \n"+		
			" and rb.shangmjssj is  null";
		}else if(zhuangt.equals("4")){
			biaoz="";
		}
		
		biaoz="and "+
		"rb.shangmkssj >=\n" +
		"to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
		"and rb.shangmjssj< sysdate  \n"+
	" and rb.shangmjssj is not null";
		String c_sql=
			"select zm.bianm,lc.mingc,g.mingc,mk.mingc,mz.mingc,f.biaoz,f.sanfsl,to_char(f.daohrq,'yyyy-mm-dd') \n" +
			" " +
//			",round_new(r.mt,1),r.mad,r.aad,\n" + 
//			"r.vad,r.stad,r.had,r.fcad,r.aar,r.vdaf, r.ad,round_new((r.vad/(100-r.mad))*100,2) as vd ," +
//			"round_new(r.qbad,2),round_new(r.qgrd,2),round_new(r.qgrad,2)," +
//			"round_new(r.qnet_ar,2), round_new(round_new(r.qnet_ar,2) * 1000 / 4.1816, 0),\n" + 
//			"r.huayy,to_char(r.huaysj,'yyyy-mm-dd')\n" + 
			"from zhillsb r,pinzb mz,fahb f,zhuanmb zm,zhuanmlb l,gongysb g,luncxxb lc,meikxxb mk\n" + 
			" where r.id=" +visit.getString9()+
			" and r.zhilb_id=f.zhilb_id and  zm.zhuanmlb_id=l.id and l.jib=3 and r.id=zm.zhillsb_id"+
			" and\n" + 
			" f.pinzb_id=mz.id  and f.gongysb_id=g.id and f.meikxxb_id=mk.id and f.luncxxb_id=lc.id ";
//			"select m.bianm haoybh ,to_char(r.huaysj,'yyyy-mm-dd')\n" +
//			",round_new(r.mt,1),r.mad,r.aad,\n" + 
//			"r.vad,r.stad,r.had,r.fcad,r.aar,r.vdaf, r.ad,round_new((r.vad/(100-r.mad))*100,2) as vd ,round_new(r.qbad,2),round_new(r.qgrd,2),round_new(r.qgrad,2)," +
//			"round_new(r.qnet_ar,2), round_new(round_new(r.qnet_ar,2) * 1000 / 4.1816, 0),\n" + 
//			"r.huayy\n" + 
//			"from zhillsb r,zhuanmb m,zhuanmlb l\n" +//caiyb z,pinzb mz	,fahb f 
//			" where r.huaysj>=to_date('"+getRiq()+"'||' '||'00:00:00','yyyy-mm-dd hh24:mi:ss')" +
//			" and r.huaysj<=to_date('"+getRiq()+"'||' '||'23:59:59','yyyy-mm-dd hh24:mi:ss')"+"\n"+
//			" \n" + 
//			" \n" + 
//			" "+
//			" and"+
//			" r.id=m.zhillsb_id and r.shenhzt>0 and m.zhuanmlb_id=l.id and l.jib=3 union\n" +//r.caiyb_id=z.id and
//			"\n" + 
//			" \n"+
//
//
//			"select z.bianm,to_char(zx.fenxrq,'yyyy-mm-dd')\n" +
//			",round_new(zx.mt,1),zx.mad,zx.aad,\n" + 
//			"zx.vad,zx.stad,zx.had,zx.fcad,zx.aar,zx.vdaf,zx.ad,round_new((zx.vad/(100-zx.mad))*100,2) as vd ,round_new(zx.qbad,2),round_new(zx.qgrd,2),round_new(zx.qgrad,2)," +
//			"round_new(zx.qnet_ar,2), round_new(round_new(zx.qnet_ar,2) * 1000 / 4.1816, 0),\n" + 
//			"zx.huayy\n" + 
//			"from rulmzlzmxb zx,zhuanmb z,zhuanmlb l\n" + 
//			" where zx.fenxrq>=to_date('"+getRiq()+"'||' '||'00:00:00','yyyy-mm-dd hh24:mi:ss')" +
//			" and zx.fenxrq<=to_date('"+getRiq()+"'||' '||'23:59:59','yyyy-mm-dd hh24:mi:ss')"+"\n"+
//
//			" and zx.zhuanmbzllsb_id=z.zhillsb_id and zx.shenhzt>0\n" + 
//			"  and z.zhuanmlb_id=l.id and l.jib=3\n" + ""
			;




		String ArrHeader[][]=new String[1][8];//18
		ArrHeader[0]=new String[] {"������","����","��Ӧ��","ú������","ú������","�˵���","ˮ����","��������"
				};

		int ArrWidth[]=new int[] {90,90,120,90,90,70,70,90};
		
		//������¼������
		ResultSet rs = cn.getResultSet(c_sql);
		int s=0;
		

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("������Ϣ",ArrWidth);
		
		//footer

		StringBuffer sb_zhu=new StringBuffer("");
		StringBuffer sb_z=new StringBuffer("");

		String shenhry="";
		String shenhryej="";
	
//		rt.setDefaultTitle(1, 7, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "", Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(13, 6, " ",Table.ALIGN_RIGHT);
		
		
		
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
	
		
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

//	
	//
	public static boolean isNetFileAvailable(String netFileUrl)
	{
	   InputStream   netFileInputStream =null;
	    try{
	     URL   url   =   new   URL(netFileUrl);   
	     URLConnection   urlConn   =   url.openConnection();   
	     netFileInputStream   =   urlConn.getInputStream(); 
	    }catch (IOException e)
	    {
	     return false;
	    }
	     if(null!=netFileInputStream)
	     {
	      return true;
	     }else
	     {
	     return false;
	     }
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private void Return(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();

			
			cycle.activate("Huaylrxg_zh");
		
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
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
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			setRiq(DateUtil.FormatDate(new Date()));
			this.setTreeid(null);
			
			//begin��������г�ʼ������
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
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

		if(_Baoblxchange){
			_Baoblxchange=false;
			getJizyxqkreport();
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
		
//		tb1.addText(new ToolbarText("����:"));
//		DateField df = new DateField();
//		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
//		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setWidth(100);
//		//df.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(df);
//		
//		tb1.addText(new ToolbarText("-"));
		
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
		
//		tb1.addText(new ToolbarText("����ѡ��:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		cb.setId("Baoblx");
//		cb.setWidth(120);
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("��������:"));
//		DateField df = new DateField();
//		df.setValue(getRiq());
//		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setId("rulrq");
//		tb1.addField(df);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
//		ToolbarButton tbLink = new ToolbarButton(null,"��ӡ","function(){}");
			ToolbarButton rbtn = new ToolbarButton(null, "����",
			"function(){document.getElementById('ReturnButton').click();}");
			rbtn.setIcon(SysConstant.Btn_Icon_Search);
	    //tb1.setWidth("bodyWidth");
	    tb1.addItem(rbtn);
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
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
			fahdwList.add(new IDropDownBean(0,"����(����14�㵽����14��)"));
			fahdwList.add(new IDropDownBean(1,"����(����14�㵽����)"));
			fahdwList.add(new IDropDownBean(2,"δ���ȡ��(����)"));
			fahdwList.add(new IDropDownBean(3,"δ���ȡ��(����)"));
			fahdwList.add(new IDropDownBean(4,"����"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
}
