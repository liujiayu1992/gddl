package com.zhiren.gangkjy.taiz;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;

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


public class Jiextz  extends BasePage implements PageValidateListener{

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
	
	
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			this.getSelectData();
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

	/**
	 * ���缯�ŵ�ú��Ϣ�ձ���
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		StringBuffer strSQL = new StringBuffer();
		String sql = 
			"select   decode(grouping(to_char(f.daohrq,'yyyy-mm-dd'))+grouping(g.mingc),2,'�ϼ�',1,to_char(f.daohrq,'yyyy-mm-dd')||'��С��',to_char(f.daohrq,'yyyy-MM-dd'))as shoumrq,\n" +
			"         g.mingc as fahr,\n" + 
			"         to_char(f.fahrq,'yyyy-MM-dd') as fahrq,\n" + 
			"         c.mingc as faz,f.chec,p.mingc as pinz,\n" + 
			"         sum(f.ches) as ches,\n" + 
			"         sum(f.biaoz) as biaoz,\n" + 
			"         sum(f.maoz) as maoz,\n" + 
			"         sum(f.piz) as piz,\n" + 
			"         sum(f.jingz) as jingz,\n" + 
			"         sum(f.yingk) as yingk,\n" + 
			"         sum(f.yuns) as yuns,\n" + 
			"         to_char(f.guohsj,'yyyy-MM-dd hh:mi:ss') as guohsj,\n" + 
			"         to_char(f.daohrq,'yyyy-MM-dd hh:mi:ss') as xiemsj,\n" + 
			"         getrudsj(f.id) as rudsj,\n" + 
			"         getduow(f.id) as duow,\n" + 
			"         f.beiz\n" + 
			"from fahb f ,vwchez c,vwpinz p,vwgongys g\n" + 
			"where f.faz_id = c.id\n" + 
			"      and f.pinzb_id = p.id\n" + 
			"      and f.gongysb_id = g.id\n" + 
			" 	   and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   and f.daohrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"	   and f.diancxxb_id = " + visit.getDiancxxb_id() + "\n" +
			"group by rollup (to_char(f.daohrq,'yyyy-mm-dd'),g.mingc,f.daohrq,f.fahrq,c.mingc,f.chec,p.mingc,f.guohsj,getrudsj(f.id),getduow(f.id),f.beiz)\n" + 
			"having not (grouping(g.mingc) || grouping(f.beiz)) =1\n" + 
			"order by grouping(to_char(f.daohrq,'yyyy-mm-dd')) desc,to_char(f.daohrq,'yyyy-mm-dd'),grouping(g.mingc) desc,f.fahrq,c.mingc,f.chec,p.mingc,f.guohsj,getrudsj(f.id),getduow(f.id),f.beiz  \n";


		strSQL.append(sql);
		
		 String ArrHeader[][]=new String[1][18];
		 ArrHeader[0]=new String[] {Local.fahrq,Local.fahr,Local.fahrq,Local.faz,Local.chec,Local.pinz,Local.ches,Local.biaoz,Local.maoz,Local.piz,Local.jingz,Local.yingk,Local.yuns,Local.guohrq,Local.xiemsj,Local.rudsj,Local.duow,Local.beiz};

		 int ArrWidth[]=new int[] {120,100,100,50,50,50,40,40,40,40,40,40,40,100,100,100,80,70};
		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("�� ж �� �� ̨ ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 18, getBRiq() + " �� " + getERiq(), Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		
//		rt.body.mergeFixedCols();52909042
//		rt.body.mergeFixedRow();
		rt.body.setFixedCols(1);
		rt.body.setFixedCols(2);
		rt.body.setFixedRows(2);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_CENTER);
		rt.body.setColAlign(15, Table.ALIGN_CENTER);
		rt.body.setColAlign(16, Table.ALIGN_CENTER);
		rt.body.setColAlign(17, Table.ALIGN_LEFT);
		rt.body.setColAlign(18, Table.ALIGN_CENTER);
		
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
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
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("жú����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
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


}