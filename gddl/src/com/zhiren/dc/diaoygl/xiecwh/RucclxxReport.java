package com.zhiren.dc.diaoygl.xiecwh;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�yangzl
 * ʱ�䣺2010-01-18
 */
public class RucclxxReport extends BasePage implements PageValidateListener {

	// ��Ϣ��
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, true);
	}

	protected void initialize() {
		super.initialize();
		_pageLink = "";
		setMsg(null);
	}

	// ��ť�¼�����ˢ�¡�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = true;
			Refurbish();
		}
	}

	// ҳ���ʼ��ˢ���¼�
	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		getSelectData();
	}

	/**
	 * ҳ�濪ʼʱ��ʼ������
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			this.setRCJZRiq(DateUtil.FormatDate(new Date()));
			this.setRCKSRiq(DateUtil.FormatDate(new Date()));
			this.getSelectData();
			Refurbish();
		}
		getToolBars();	
		//Refurbish();
	}

	public String getPrintTable() {
		return getSelectData();
	}

	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();

		// �����ͷ����
		Report rt = new Report();
		// String arrFormat[] = null;
		// ���ñ���ı�ͷ��
		String ArrHeader[][] = new String[2][12];

		ArrHeader[0]=new String[] {"�����볧��Ϣ","�����볧��Ϣ","�����볧��Ϣ","�����볧��Ϣ","�����볧��Ϣ","�����볧��Ϣ","�����볧��Ϣ"
						,"ж��ʱ�䷶Χ<br/>����/ʱ/������/ʱ/�֣�","����Ա"};
		ArrHeader[1]=new String[] {"���","������λ","��������","��վ","Ʊ��","�ɵ���","�볧ʱ��<br/>����/ʱ/�֣�"
						,"ж��ʱ�䷶Χ<br/>����/ʱ/������/ʱ/�֣�","����Ա"};


		int ArrWidth[] = new int[] {40,120, 80, 50, 50, 50, 80, 150, 90 };

		String[] arrFormat = new String[] {"", "", "", "", "0.000", "", "",
				"", "" };

		// ��������
		//meizb m,     m.mingc meiz,
		strSQL = "select rownum as xuh,g.mingc fahdw,c.cheph chelbh,cz.mingc fz,c.biaoz piaoz,c.zhongchh gudh,to_char(c.zhongcsj,'dd-hh24-mi') rucsj,\n"
				+ "decode(to_char(rx.xieckssj,'dd/hh24/mi')||'��'||to_char(rx.xiecjzsj,'dd/hh24/mi'),'��','',to_char(rx.xieckssj,'dd/hh24/mi')||'��'||to_char(rx.xiecjzsj,'dd/hh24/mi')) xiecsjfw,\n"
				+ "rx.diaody diaody\n"
				+ "from fahb f,chepb c,chezxxb cz,gongysb g,rucxcb rx,yunsfsb y\n"
				+ "where f.faz_id=cz.id\n"
				+ "      and f.gongysb_id=g.id\n"
				+ "      and c.id=rx.chepb_id(+)\n"
				+ "      and c.fahb_id=f.id\n"
				+ "      and f.yunsfsb_id=y.id\n"
				+ "      and y.id=1"
				+ "      and c.zhongcsj >="
				+ DateUtil.FormatOracleDate(this.getRCKSRiq())
				+ "\n"
				+ "      and c.zhongcsj <="
				+ DateUtil.FormatOracleDate(this.getRCJZRiq())
				+ "+1"
				+ "\n"
				+ "order by xuh\n";

		ResultSetList rs = cn.getResultSetList(strSQL.toString());
		int cols=rs.getRows();
		// ����
		rt.setBody(new Table(rs, 2, 0, 1));
		String riqK = this.getRCKSRiq();
		String riqJ = this.getRCJZRiq();
		String riq="";
		System.out.println(DateUtil.getDate(riqK));
		System.out.println(DateUtil.getDate(riqJ));
		System.out.println("riq bijiao:"+DateUtil.getDate(riqK).compareTo(DateUtil.getDate(riqJ)));		
		if(DateUtil.getDate(riqK).compareTo(DateUtil.getDate(riqJ))>0) {
			this.setMsg("'��ʼ����ҪС�ڵ��ڽ�ֹ���ڣ�'");			
		}else if (Integer.parseInt(riqK.substring(0, riqK.indexOf("-")))==Integer.parseInt(
				riqJ.substring(0, riqJ.indexOf("-")))) {
				riq=riqK.substring(0, riqK.indexOf("-"));
		}else if(Integer.parseInt(riqK.substring(0, riqK.indexOf("-")))<Integer.parseInt(
				riqJ.substring(0, riqJ.indexOf("-")))) {
				riq=riqK.substring(0, riqK.indexOf("-"))+"/"+riqJ.substring(0, riqJ.indexOf("-"));
		}
		rt.setTitle(riq+"��"+visit.getDiancmc() + "�볧ú̿������Ϣ", ArrWidth);
		rt
				.setDefaultTitle(1, 2, "���λ:"
						+ ((Visit) getPage().getVisit()).getDiancmc(),
						Table.ALIGN_LEFT);
		// rt.setDefaultTitle(4, 3, "�����:" + intyear + "��" + intMonth + "��",
		// Table.ALIGN_CENTER);
		rt.setDefaultTitle(7, 3, "��λ:��", Table.ALIGN_RIGHT);
		
		//���ݾ���
//		rt.body.setColAlign(iCol, intAlign)
		rt.body.setCells(1, 1, rt.body.getRows(), ArrHeader[0].length, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = true;
//		rt.body.mergeFixedRow();// �ϲ���
//		rt.body.mergeFixedCols();// �Ͳ���

		if(rt.body.getPages()>900) {
			this.setMsg("'����ѯ������̫���޷���ʾ��'");
			return "";
		}
		 if (rt.body.getRows() > 1) {
			 rt.body.merge(1, 1, 1, 8);
			 rt.body.merge(1, 8, 2, 8);
			 rt.body.merge(1, 9, 2, 9);
		 }
		// rt.body.mergeFixedCols();
		rt.body.setColFormat(arrFormat);
		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 3, "�Ʊ�:", Table.ALIGN_RIGHT);
		rt.setPaper(Report.PAPER_A4);
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rs.close();
		cn.Close();
		return rt.getAllPagesHtml();
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

	

	/*
	 * private String FormatDate(Date _date) { if (_date == null) { return ""; }
	  return DateUtil.Formatdate("yyyy��MM��dd��", _date); }
	 */

	// ***************************�����ʼ����***************************//
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
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

	// ҳ���ж�����
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	// ��������
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ʼ����:"));
		// �볧��ʼʱ��
		DateField rucksDF = new DateField();
		rucksDF.setValue(this.getRCKSRiq());
		rucksDF
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('RCKSRiq').value = newValue.dateFormat('Y-m-d');}");
		tb1.addField(rucksDF);

		tb1.addText(new ToolbarText("-"));
		// �볧��ֹʱ��ѡ��
		DateField rucjzDF = new DateField();
		rucjzDF.setValue(this.getRCJZRiq());
		rucjzDF
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('RCJZRiq').value = newValue.dateFormat('Y-m-d');document.forms[0].submit();}");
		tb1.addField(rucjzDF);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}

	// ��ѯ�������볧��ʼ���� yyyy-mm-dd
	private String rcksRiq = "";

	public String getRCKSRiq() {
		return rcksRiq;
	}

	public void setRCKSRiq(String riq) {
		rcksRiq = riq;
	}

	// ��ѯ�������볧�������� yyyy-mm-dd
	private String rcjzRiq = "";

	public String getRCJZRiq() {
		return rcjzRiq;
	}

	public void setRCJZRiq(String riq) {
		rcjzRiq = riq;
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