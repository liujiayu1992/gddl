package com.zhiren.jt.zdt.shengcdy.shouhctb;


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
import com.zhiren.common.ResultSetList;
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

public class Shouhctb extends BasePage {
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
	
	private String REPORT_KUCGJ="shouhctb";
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
		return diancmc;
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(isBegin){
			return getShouhctb();
		
		}else{
			return "�޴˱���";
		}
	}

	public String getShouhctb(){
		
		Visit visit = (Visit) this.getPage().getVisit();
		_CurrentPage=1;
		_AllPages=1;
		Date dat=getBeginriqDate();//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		String strDate2=DateUtil.FormatDate(DateUtil.AddDate(dat, -14, 86400000));
		String strDate3=DateUtil.FormatDate(DateUtil.AddDate(dat, +1, 86400000));
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();
		long lngjiesbID = Long.parseLong(visit.getString1().substring(0,3));

		String sql = "select to_char(b.riq,'yyyy-mm-dd') as riq,a.shour,a.haoy,a.tiaozl,a.kuc,a.jingjcml,a.diyjjkcbl,a.keyts from "
				+ "(select rb.riq,rb.dangrgm as shour,rb.haoyqkdr as haoy,rb.tiaozl as tiaozl,rb.kuc as kuc,dc.jingjcml,\n"
				+ "case when (dc.jingjcml-rb.kuc)>0 then "
				+ "decode(dc.jingjcml,0,0,round((nvl(dc.jingjcml,0)-nvl(rb.kuc,0))/dc.jingjcml*100,2)) else 0 end as diyjjkcbl,"
				+ "decode(dc.rijhm,0,0,round(nvl(rb.kuc,0)/dc.rijhm,1)) as keyts\n"
				+ "from shouhcrbb rb,diancxxb dc where rb.diancxxb_id = dc.id and rb.diancxxb_id = "
				+ lngjiesbID+ " order by riq ) a,\n"
				+ "(select to_date('"+ strDate3	+ "','yyyy-mm-dd')-rownum+1 as riq from shouhcrbb where rownum<=16) b\n"
				+ "where b.riq<to_date('"+ strDate3 +"','yyyy-mm-dd') and a.riq(+)=b.riq order by b.riq";

 
		String ArrHeader[][]=new String[1][8];
		ArrHeader[0]=new String[] {"����","����ú��(��)","����ú��(��)","����ֵ(��)","���(��)","�����ú��(��)","���ھ�����<br>����(%)","��������"};

		int ArrWidth[]=new int[] {80,80,80,80,80,80,80,80};
		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("�պĴ汨��",ArrWidth);
		rt.setDefaultTitle(3, 4,DateUtil.Formatdate("yyyy��MM��dd��",DateUtil.getDate(strDate2))+"��"+DateUtil.Formatdate("yyyy��MM��dd��",DateUtil.getDate(strDate)), Table.ALIGN_CENTER);

		
		rt.setBody(new Table(rs,1,0,1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setColFormat(7,"0.00");
		rt.body.setColFormat(8,"0.0");
		rt.body.ShowZero = true;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
//		ҳ�� 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"����:"+strDate2+"��:"+strDate,Table.ALIGN_LEFT);
		 rt.setDefautlFooter(7,2,"��λ:�֡���",Table.ALIGN_RIGHT);
		
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
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
		}
		
		if (cycle.getRequestContext().getParameter("shouhctb") != null&&!cycle.getRequestContext().getParameter("shouhctb").equals("-1")) {
			visit.setString1(cycle.getRequestContext().getParameter("shouhctb"));
		}
		getToolBars();
		isBegin=true;
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
