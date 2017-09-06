package com.zhiren.gangkjy.jiexdy.xiemgl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;

/**
 * 
 * @author ����
 *
 */
public class Guohddy  extends BasePage implements PageValidateListener{

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
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
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
		}
	}



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.getSelectData();
			visit.setboolean1(false);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHengdValue(null);
			setHengdModel(null);
		}
		
		if(riqchange){
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
		}
		isBegin=true;
		getToolBars();
		
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
		
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		StringBuffer sb = new StringBuffer();
		long hengdid = -2;
		if(getHengdValue()!=null) {
			hengdid = getHengdValue().getId();
		}
		//����ʱ��
		String time = (hengdid > 0) ? getRiq()+" "+getHengdValue().getValue()
				: "1990-01-01 23:00:00";

		// ������ϸ��SQL
		String sqltable = 
			"select c.xuh ,\n" +
			" 		v.mingc,\n" +
			"       f.chec,\n" + 
			"       to_char(f.fahrq,'yyyy-mm-dd') fahrq,\n" + 
			"       to_char(f.daohrq,'yyyy-mm-dd') daohrq,\n" + 
			"       m.mingc,\n" + 
			"       p.mingc,\n" + 
			"       c.cheph,\n" + 
			"       f.biaoz,\n" + 
			"       f.yingk,\n" + 
			"       f.jingz,\n" + 
			"       b.mingc,\n" + 
			"       f.piz,\n" + 
			"       f.maoz,\n" + 
			"       f.yuns,\n" + 
			"       f.yunsl,\n" + 
			"       f.ches,\n" + 
			"       c.jianjfs\n" + 
			"  from vwfahr v, fahb f, vwchez m, vwpinz p, chepb c, chebb b\n" + 
			" where  c.fahb_id = f.id\n" + 
			"   and p.id = f.pinzb_id\n" + 
			"   and b.id = c.chebb_id\n" + 
			"   and f.faz_id = m.id\n" + 
			"   and v.id=f.gongysb_id\n" + 
			"   and c.fahb_id = "+hengdid+" order by c.xuh \n";
		
		
		sb.setLength(0);
		
		sb.append(sqltable);
		
		try {
			ResultSet rs = con.getResultSet(sb,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			

			
				
			
			String[][] ArrHeader = new String[1][18];
			ArrHeader[0] = new String[] { Local.xuh, Local.fahr, Local.chec, Local.fahrq, Local.daohrq_id_fahb,
					Local.faz, Local.pinz,Local.cheph, Local.biaoz, Local.yingk, Local.jingz, Local.cheb,Local.piz, Local.maoz,
					Local.yuns, Local.yunsl, Local.ches,Local.jianjfs };

			int[] ArrWidth = new int[18];
			ArrWidth = new int[] { 32, 60, 60, 80, 80, 50,50,80, 40, 50, 40, 32, 32, 32, 50, 40,30,50 };
			


			// ����
			
			Table tb=new Table(rs, 1, 0, 1);
			rt.setBody(tb);
			
			tb.setColFormat(9,"0.000");
			tb.setColFormat(10,"0.000");
			tb.setColFormat(11,"0.000");
			tb.setColFormat(13,"0.000");
			tb.setColFormat(14,"0.000");
			tb.setColFormat(15,"0.000");
			tb.setColFormat(16,"0.000");
			rt.setTitle("���ⵥ��ӡ", ArrWidth);
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setColAlign(11, Table.ALIGN_CENTER);
			rt.body.setColAlign(17,Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_RIGHT);
			rt.body.setColAlign(5, Table.ALIGN_RIGHT);
			rt.body.setColAlign(4, Table.ALIGN_RIGHT);
			rt.body.setColAlign(8, Table.ALIGN_LEFT);
			rt.body.setColAlign(9, Table.ALIGN_RIGHT);
			rt.body.setColAlign(10, Table.ALIGN_RIGHT);
			rt.body.setColAlign(12, Table.ALIGN_RIGHT);
			rt.body.setColAlign(13, Table.ALIGN_RIGHT);
			rt.body.setColAlign(15, Table.ALIGN_RIGHT);
			rt.body.setColAlign(16, Table.ALIGN_RIGHT);
			rt.body.setColAlign(18, Table.ALIGN_LEFT);
			String sqla = 
				"select sum(f.biaoz) biaoz,\n" +
				"   sum(f.jingz) jingz,\n" + 
				"   sum(f.yuns) yuns,\n" + 
				"  sum(f.yingd) yingd,\n" + 
				"  sum(f.yingk) yingk,\n" + 
				"  sum(f.yingd)-sum(f.yingk) kuid\n" + 
				"   from fahb f where to_char(f.guohsj,'yyyy-mm-dd') = '"+getRiq()+"' \n";
			rs = con.getResultSet(sqla);
			String biaoz =" ";
			String jingz = " ";
			String yuns = " ";
			String yingk = " ";
			String yingd = " ";
			String kuid = " ";
			
			if(rs.next()){
				biaoz = rs.getString("biaoz");
				jingz = rs.getString("jingz");
				yuns = rs.getString("yuns");
				yingd = rs.getString("yingd");
				yingk = rs.getString("yingk");
				kuid = rs.getString("kuid");
				if(biaoz==null&&jingz==null&&yuns==null&&yingd==null&&yingk==null&&kuid==null){
					 biaoz =" ";
					 jingz = " ";
					 yuns = " ";
					 yingk = " ";
					 yingd = " ";
					 kuid = " ";
				}
				
			}
			
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "�ܼ�:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "Ʊ��:"+biaoz, Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 2, "����:"+jingz, Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 2, "����:"+yuns, Table.ALIGN_LEFT);
			rt.setDefautlFooter(9, 3, "ӯ��:"+yingk, Table.ALIGN_LEFT);
			rt.setDefautlFooter(12, 3, "ӯ��"+yingd, Table.ALIGN_LEFT);
			rt.setDefautlFooter(15, 3, "����"+kuid, Table.ALIGN_LEFT);
				_CurrentPage=1;
				_AllPages=rt.body.getPages();
				if (_AllPages==0){
					_CurrentPage=0;
				}
			
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
			con.Close();
			return rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
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

	
	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("���ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		tb1.addField(hengdcb);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
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
//	�����Ƿ�仯
	private boolean riqchange = false;
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq != null){
			if(!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	�ⵥ������
	public IDropDownBean getHengdValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getHengdModel().getOptionCount()>0) {
				setHengdValue((IDropDownBean)getHengdModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setHengdValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getHengdModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setHengdModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}

	
	public void setHengdModels() {

		StringBuffer sb = new StringBuffer();

		sb.append("select id,to_char(guohsj,'hh24:mi:ss') guohsj from fahb where to_char(guohsj,'yyyy-mm-dd') = '").append(getRiq()).append("'");
		setHengdModel(new IDropDownModel(sb.toString()));
	}


}