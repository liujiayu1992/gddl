/*
* ʱ�䣺2009-04-11
* ���ߣ�chh
* �޸����ݣ�_friqValue ֻ�ڵ�һ�ν���ҳ���Ǹ�ֵ,���Ĳ�ѯ���ں�_friqValue���仯���ݲ�ѯ����ȷ��
* ��Ϊ_friqValue��ÿ���ύҳ��ʱ��ֵ��������ֻ��һ�θ�ֵ��
*/

package com.zhiren.jt.zdt.gonggxx.shicdt;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class GangkjgReport  extends BasePage implements PageValidateListener{

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
	
//	��������v
	private Date _friqValue = DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
	private boolean _friqChange=false;
	public Date getfriqDate() {
		if (_friqValue==null){
			_friqValue =DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
		}
		return _friqValue;
	}
	
	public void setfriqDate(Date _value) {
		if (FormatDate(_friqValue).equals(FormatDate(_value))) {
			_friqChange=false;
		} else {
			_friqValue = _value;
			_friqChange=true;
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
		}
	}



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			
			visit.setList1(null);
			this.getSelectData();
		}
		_friqValue = DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
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
		JDBCcon cn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		String riq = OraDate(_BeginriqValue);//��ǰ����
		String friq = OraDate(_friqValue);
		
// **ú��**
		//�����ͷ����
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrWidth = new int[] {100,50,50,50,50,50,50,50,50,50,50};
		
//		�õ������ݣ�ʱ��
		StringBuffer strCol= new StringBuffer();
		strCol.append("select to_char("+riq+"-rownum+1,'MM')||'��'|| to_char("+riq+"-rownum+1,'dd')||'��' as ʱ��  \n");
		strCol.append(" from all_objects");
		strCol.append(" where rownum<=10 \n");
		strCol.append(" order by ʱ��");
		
//		�õ������ݣ�Ʒ��
		StringBuffer strRow = new StringBuffer();
		
		//--ʵ��װ��
		strRow.append("select distinct pz.mingc as Ʒ�� \n");
		strRow.append("from gangkscjgb g,pinzb pz where g.pinzb_id=pz.id  \n");
		strRow.append("and riq>="+friq+" and riq<="+riq+"\n");
		strRow.append("order by pz.mingc desc \n");
		
//		 �õ�ȫ������ ���̱�
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append(" select  riq as ʱ��, gk.mingc as Ʒ��, gk.jiag as �۸������� \n");
		sbsql.append(" from (select pz.mingc ,pinzb_id,to_char(riq, 'MM')||'��'||to_char(riq, 'dd')||'��' as riq,(jiagxx||'~'||jiagsx) as jiag \n");
		sbsql.append("       from gangkscjgb gk ,pinzb pz \n");
		sbsql.append("       where gk.riq >="+friq+" and gk.riq <="+riq+"  \n");
		sbsql.append("       and gk.pinzb_id = pz.id  \n");
		sbsql.append("       )  gk order by riq \n");
		
		cd.setRowNames("Ʒ��");
		cd.setColNames("ʱ��");
		cd.setDataNames("�۸�������");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 60;
		}
		ArrWidth[0] = 100;
		ArrWidth[1] = 60;
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle("�ػʵ���ú̿�г���Ҫú���г�����۸��ܱ�", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.body.setCellValue(1, 0, "ú̿Ʒ��");
		rt.body.setCellValue(1, 1, "ú̿Ʒ��");
		rt.setDefaultTitle(1, 2, "��λ��Ԫ/��",Table.ALIGN_LEFT);
						
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		
		
// **�۴�**
		Report rt_gc = new Report();
		String[][] ArrHeader_gc;
		int[] ArrWidth_gc;
		ArrWidth_gc = new int[] {54,50,50,50,50,50,50,50,50,50,50};
		
//		�õ������ݣ�ʱ��
		StringBuffer strCol_gc= new StringBuffer();
		strCol_gc.append("select to_char("+riq+"-rownum+1,'MM')||'��'|| to_char("+riq+"-rownum+1,'dd')||'��' as ʱ��  \n");
		strCol_gc.append(" from all_objects");
		strCol_gc.append(" where rownum<=10 \n");
		strCol_gc.append(" order by ʱ��");

//		�õ������ݣ��ۿ�,����
		StringBuffer strRow_gc = new StringBuffer();
		
		strRow_gc.append("select distinct c.mingc as �ۿ�,g.fenl as ���� \n");
		strRow_gc.append("from gangkgc g,chezxxb c where g.chezxxb_id=c.id  \n");
		strRow_gc.append("and riq>="+friq+" and riq<="+riq+"\n");
		strRow_gc.append("order by c.mingc desc,g.fenl desc \n");
		
//		 �õ�ȫ������ ���̱�
		StringBuffer sbsql_gc = new StringBuffer();
		
		sbsql_gc.append(" select  riq as ʱ��, gk.mingc as �ۿ�, gk.fenl as ����, gk.gangc as �۴�  \n");
		sbsql_gc.append(" from (select c.mingc ,chezxxb_id,to_char(riq, 'MM')||'��'||to_char(riq, 'dd')||'��' as riq,fenl,gangc \n");
		sbsql_gc.append("       from gangkgc gk ,chezxxb c \n");
		sbsql_gc.append("       where gk.riq >="+friq+" and gk.riq <="+riq+"  \n");
		sbsql_gc.append("       and gk.chezxxb_id = c.id  \n");
		sbsql_gc.append("       )  gk order by riq \n");
		
		cd.setRowNames("�ۿ�,����");
		cd.setColNames("ʱ��");
		cd.setDataNames("�۴�");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow_gc.toString(), strCol_gc.toString(), sbsql_gc.toString());
		ArrWidth_gc = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth_gc.length; i++) {
			ArrWidth_gc[i] = 60;
		}
		ArrWidth_gc[0] = 60;
		ArrWidth_gc[1] = 40;
		rt_gc.setBody(cd.DataTable);
		rt_gc.body.setWidth(ArrWidth_gc);
		rt_gc.body.mergeFixedRowCol();
		rt_gc.body.ShowZero = true;
		rt_gc.setTitle("��Ҫ�ۿڸ۴���Ϣһ�ܱ仯���", ArrWidth_gc);
		rt_gc.title.setRowHeight(2, 50);
		rt_gc.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt_gc.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt_gc.body.setCellValue(1, 0, "�ۿ�����");
		rt_gc.body.setCellValue(1, 1, "�ۿ�����");
		rt_gc.setDefaultTitle(1, 2, "��λ�����",Table.ALIGN_LEFT);
						
		rt_gc.body.setPageRows(21);
		rt_gc.createDefautlFooter(ArrWidth_gc);
//		rt_gc.setDefautlFooter(1, 3,
//				"��ӡ����:"
//						+ FormatDate(DateUtil.getDate(DateUtil
//								.FormatDate(new Date()))), Table.ALIGN_LEFT);
//		rt_gc.setDefautlFooter(7, 1, "��λ:������", Table.ALIGN_RIGHT);
			
			_CurrentPage=1;
			_AllPages=1;
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml()+rt_gc.getAllPagesHtml();
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
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
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


}