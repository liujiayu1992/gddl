package com.zhiren.dc.jilgl.gongl.suoc;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
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
/*
 * 2009-02-19
 * ����
 * ������������ʱ��ĸ�ʽ��,������ʱ������,����ģ����ѯ
 */

public class Suocztb extends BasePage {
//	�����û���ʾ
	public static final DateFormat DATE_FORMAT 
	= new SimpleDateFormat("yyyy-MM-dd");
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
	
	
	private String hetbh;

	public String getHetbh() {
		return hetbh;
	}

	public void setHetbh(String hetbh) {
		this.hetbh = hetbh;
	}
//	������
	private String qsriq;

	public String getRiq() {
		return qsriq;
	}

	public void setRiq(String qsriq) {
		this.qsriq = qsriq;
	}
	//-------
	private String jzriq;

	public String getRiq1() {
		return jzriq;
	}

	public void setRiq1(String jzriq) {
		this.jzriq = jzriq;
	}
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
//	��ȡ��ص�SQL
	public StringBuffer getBaseSql() {
		Visit v = (Visit) this.getPage().getVisit();
		String CurrentDate = DateUtil.FormatOracleDate(getRiq());
		String CurrentDate1 = DateUtil.FormatOracleDate(getFDOfMonth(getRiq1()));
		String cheph;
		if(getHetbh()==null||getHetbh().equals("")){
		  cheph="";
		}else{
			cheph= "and c.cheph like '%"+getHetbh()+"%'";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum,c.cheph,to_char(s.suocsj,'yyyy-mm-dd hh24:mi:ss') suocsj,suocry,s.suocyy,to_char(s.jiessj,'yyyy-mm-dd hh24:mi:ss') jiessj,s.jiesry from suocztb s,chelxxb c");
		sb.append(" where s.chelxxb_id=c.id ");
		sb.append("and c.diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+"\n");
		sb.append( cheph);
		sb.append(" and s.suocsj >= ");sb.append(CurrentDate);
		sb.append(" and s.suocsj <");sb.append(CurrentDate1);
		sb.append(" order by s.suocsj");
		
		return sb;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if(visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("����ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("qsriq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("qsriq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setValue(getRiq1());
		df1.Binding("jzriq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("jzriq");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
        tb1.addText(new ToolbarText("��Ƥ��:"));
		
		//cb.setListeners("select:function(){document.Form0.submit();}");
		TextField hetbh=new TextField();
		hetbh.setAllowBlank(true);
		hetbh.setId("hetbh");
		hetbh.setListeners(
				"change:function(own,newValue,oldValue){"+
                "document.all.item('hetbh').value=newValue;}"        		 	
		);
		tb1.addField(hetbh);
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		
		ResultSet rs = con.getResultSet(getBaseSql(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		Report rt = new Report();

		 String ArrHeader[][]=new String[1][7];
		 ArrHeader[0]=new String[] {"���","���ƺ�","����ʱ��","����ԭ��","����������","����ʱ��","����������"};

		 int ArrWidth[]=new int[] {50,80,120,80,80,120,80};

	
		rt.setTitle("����״̬", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 5, "�������ڣ�" + getRiq(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 4, "��λ���֡���", Table.ALIGN_RIGHT);

		String[] arrFormat = new String[] { "", "", "", "", "", "", ""};

		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		//rt.body.setCellVAlign(i, j, intAlign);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 5, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 4, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}
//	������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setRiq(DateUtil.FormatDate(new Date()));
			setRiq1(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setRiq1(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			getSelectData();
		}
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
//	public static Date getFirstDayOfMonth(Date date) {
//		return getFDOfMonth(FormatDate(date));
//	}
	
	public static Date getFDOfMonth(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])+1, 1);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}
	public static String FormatDate(Date date) {
		String StrDate;
		try {
			StrDate = DATE_FORMAT.format(date);
		} catch (NullPointerException NPE) {
			StrDate = "1900-01-01";
			System.out.println("��ʽ������Ϊ�գ�");
		} catch (Exception E) {
			StrDate = "1900-01-01";
			System.out.println("δ֪�쳣��");
		}
		return StrDate;
	}
//	ҳ���½��֤
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
}

