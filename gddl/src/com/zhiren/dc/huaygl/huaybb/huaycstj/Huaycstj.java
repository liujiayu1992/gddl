package com.zhiren.dc.huaygl.huaybb.huaycstj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import java.math.*;


/*����:���ܱ�
 *����:2010-4-13 11:37:07
 *����:1.����ÿһ�еĿ��,���ó����ô�ӡһ��ֽ
 *     2.�趨�ϼ���float��ʾ2λС��
 */
/**
 * @author yinjm
 * ú̿����Ʊ�ݱ���
 */

public class Huaycstj extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
	}
	
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private String riq; // ��������
	
	public String getRiq() {
		return riq;
	}
	
	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	
private String riq2; // ��������
	
	public String getRiq2() {
		return riq2;
	}
	
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
	
	
	private String Markmk = "true"; // ���ú��λ�������Ƿ�ѡ��
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
	}
	
	

	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
//	ú��λ������_��ʼ
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = 
			"select rownum as xuh ,a.mingc from (\n" +
			"select distinct m.mingc\n" + 
			"from zhillsb ls,zhilb zl,(select distinct zhilb_id,meikxxb_id from fahb) f,meikxxb m\n" + 
			" where ls.huaysj>=to_date('"+getRiq()+"','yyyy-mm-dd')\n" + 
			" and ls.huaysj<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
			" and ls.zhilb_id=zl.id\n" + 
			" and zl.id=f.zhilb_id\n" + 
			" and f.meikxxb_id=m.id\n" + 
			" and ls.qnet_ar is not null) a";

		setMeikdwModel(new IDropDownModel(sql, "ȫ��"));
	}
//	ú��λ������_����

	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String meik=this.getMeikdwValue().getValue();
		
		
		
		String sql = "";

			
		if(meik=="ȫ��"){
			sql=
				"select rownum as xuh,a.mingc,formatxiaosws(a.stad,2) as stad,a.dak,a.riq\n" +
				"from (\n" + 
				"select m.mingc,ls.stad,round(ls.qnet_ar*1000/4.1816,0) as dak,to_char(ls.huaysj,'yyyy-mm-dd') as riq\n" + 
				"from zhillsb ls,zhilb zl,(select distinct zhilb_id,meikxxb_id from fahb) f,meikxxb m\n" + 
				" where ls.huaysj>=to_date('"+getRiq()+"','yyyy-mm-dd')\n" + 
				" and ls.huaysj<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
				" and ls.zhilb_id=zl.id\n" + 
				" and zl.id=f.zhilb_id\n" + 
				" and f.meikxxb_id=m.id\n" + 
				"  and ls.shenhzt=7\n"+
				" and ls.qnet_ar is not null\n" + 
				" order by ls.huaysj) a";
		}else{

			sql="select rownum as xuh,a.mingc,a.stad,a.dak,a.riq\n" +
			"from (\n" + 
			"select decode(m.mingc,null,'��Ȩƽ��',m.mingc) as mingc,\n" + 
			"formatxiaosws(decode(sum(f.shul),0,0,round_new(sum(ls.stad*f.shul)/sum(f.shul),2)),2)  as stad,\n" + 
			"decode(sum(f.shul),0,0,Round_new(sum(ls.qnet_ar*f.shul)/sum(f.shul)*1000/4.1816,0))  as dak,\n" + 
			"to_char(ls.huaysj,'yyyy-mm-dd') as riq\n" + 
			"from zhillsb ls,zhilb zl,(select  zhilb_id,f.meikxxb_id,sum(maoz-piz) as shul from fahb f group by (f.zhilb_id,f.meikxxb_id)) f,meikxxb m\n" + 
			" where ls.huaysj>=to_date('"+getRiq()+"','yyyy-mm-dd')\n" + 
			" and ls.huaysj<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
			" and ls.zhilb_id=zl.id\n" + 
			" and zl.id=f.zhilb_id\n" + 
			" and f.meikxxb_id=m.id\n" + 
			" and m.mingc='"+meik+"'\n" + 
			"  and ls.shenhzt=7\n"+
			" and ls.qnet_ar is not null\n" + 
			" group by rollup (m.mingc,ls.huaysj)\n" + 
			" having not (grouping(m.mingc)+grouping(ls.huaysj)=1)\n" + 
			" order by ls.huaysj) a\n" + 
			"";

		}

			




		ResultSetList rs =  con.getResultSetList(sql);
		String[][] ArrHeader = new String[1][5];
		ArrHeader[0] = new String[]{"���", "ú��", "���", "��ֵ", "��������"};
		int[] ArrWidth = new int[] {50, 120, 80, 80, 120};
		

		rt.setTitle("�볧ú�������ͳ��", ArrWidth);
		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 3, "����:"+getRiq()+"��"+getRiq2(),Table.ALIGN_LEFT);
		
		
	

		rt.setBody(new Table(rs, 1, 0, 2));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		for(int i=1;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.setPageRows(25);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rs.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("�������ڣ�"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
//		df.Binding("Riq", "forms[0]");
		df.setListeners("change:function(own,newValue,oldValue){document.getElementById('Riq').value = newValue.dateFormat('Y-m-d'); " +
			"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		tbr.addText(new ToolbarText("��ֹ��"));
		DateField df2 = new DateField();
		df2.setValue(getRiq2());
		df2.setId("riq2");

		df2.setListeners("change:function(own,newValue,oldValue){document.getElementById('Riq2').value = newValue.dateFormat('Y-m-d'); " +
			"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		tbr.addField(df2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("ú��λ��"));
		ComboBox mkdw = new ComboBox();
		mkdw.setTransform("Meikdw");
		mkdw.setWidth(120);
		mkdw.setListeners("select:function(){document.getElementById('Mark_mk').value = 'false'; " +
			" document.forms[0].submit();}");
		mkdw.setLazyRender(true);
		mkdw.setEditable(false);
		tbr.addField(mkdw);
		tbr.addText(new ToolbarText("-"));
		
		
		
		ToolbarButton tbrtn = new ToolbarButton(null, "��ѯ", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 *  ȡ��ҳ��Ҫ��ʾ��ҳ��
	 * @param rslrows ���������
	 * @return
	 */
	public int getPages(int rslrows) {
		int pages = rslrows / 50;
		if (rslrows % 50 > 0) {
			pages ++;
		}
		return pages;
	}
	
	/**
	 * �������String������ת��ΪData������
	 * @param date
	 * @return
	 */
	public static Date getDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date tempdate = new Date();
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			tempdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tempdate;
	}
	
	/**
	 * ��ȡ�����ڵ����
	 * @param date
	 * @return
	 */
	public int getYear(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int year = DateUtil.getYear(new Date());
		try {
			Date tempdate = sdf.parse(date);
			year = DateUtil.getYear(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
	}
	
	/**
	 * ��ȡ�����ڵ��·�
	 * @param date
	 * @return
	 */
	public int getMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int month = DateUtil.getMonth(new Date());
		try {
			Date tempdate = sdf.parse(date);
			month = DateUtil.getMonth(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}
	
	/**
	 * ��ȡú��λ����
	 * @param mkmc
	 * @return
	 */
	public String getMeikdwmc(String mkmc) {
		if (!mkmc.equals("��ѡ��")){
			return mkmc;
		} else {
			return "";
		}
	}
	
	/**
	 * ��ȡ���
	 * @param mkmc
	 * @return
	 */
	public String getBianh(String bh) {
		if (!bh.equals("��ѡ��")){
			return bh;
		} else {
			return "";
		}
	}
	
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // ú��λ������
			visit.setDropDownBean3(null);
			visit.setProSelectionModel2(null); // ���������
			visit.setDropDownBean2(null);
		}
		if (getMarkmk().equals("true")) { // �ж����getMarkmk()����"true"����ô���³�ʼ��ú��λ������ͱ��������
			getMeikdwModels();
			
		}
		
		getSelectData();
	}
}