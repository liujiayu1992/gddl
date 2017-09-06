package com.zhiren.dc.gdxw.meikpq;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Meikwglcx extends BasePage {

	/**
	 * ����:���ܱ�
	 * ʱ��:2009-9-26 17:15:06
	 * ����:����������ѯ
	 */
	
//	�����û���ʾ
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
	
	private String Markbh = "true"; // ��Ǳ���������Ƿ�ѡ��
	
	public String getMarkbh() {
		return Markbh;
	}

	public void setMarkbh(String markbh) {
		Markbh = markbh;
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

    

	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
	
		

		
		
/*		//����������
		tb1.addText(new ToolbarText("����:"));
		ComboBox cheh = new ComboBox();
		cheh.setTransform("CHEH");
		cheh.setWidth(100);
		cheh.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false';document.forms[0].submit();}");
		cheh.setEditable(true);
		tb1.addField(cheh);
		tb1.addText(new ToolbarText("-"));
		
		
		//ͣ���˺�������
		tb1.addText(new ToolbarText("ͣ����:"));
		ComboBox caiyh = new ComboBox();
		caiyh.setTransform("CAIYH");
		caiyh.setWidth(100);
		caiyh.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false';document.forms[0].submit();}");
		caiyh.setEditable(true);
		tb1.addField(caiyh);
		tb1.addText(new ToolbarText("-"));
*/		
		
//		�糧Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		
		
		

		
		//�����һ�����ƾ���ʾ�糧��,������ǾͲ���ʾ�糧
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		//ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		//rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		//tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
			return getYansd();
		
	}
	
	
	
	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//�������
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //������
//		String kais=this.getBRiq();
//		if(kais.equals("")||kais=="null"){
//			kais=DateUtil.FormatDate(new Date());
//		}
//		
//		String jies=this.getERiq();
//		if(jies.equals("")||jies=="null"){
//    	jies=DateUtil.FormatDate(new Date());
//		}
//		String tiaoj="";
//		long meik=this.getCaiyhValue().getId();
//		if(meik==-1){
//			tiaoj="";
//		}else{
//			tiaoj=" and c.suocr='"+this.getCaiyhValue().getValue()+"'";
//		}
//		
//		String chehtj="";
//		long cheh=this.getChehValue().getId();
//		if(cheh==-1){
//			chehtj="";
//		}else{
//			chehtj=" and c.cheph='"+this.getChehValue().getValue()+"'\n";
//		}
//		
		

			sbsql.append("select rownum as xuh, mingc from meikxxb where id not in (select meikxxb_id from gongysmkglb )");

			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][2];
//				1120
				ArrHeader[0]=new String[] {"���","ú������"};
				int ArrWidth[]=new int[] {100,200};
				  strFormat = new String[] { "", "" };
				// ����
				rt.setTitle(visit.getDiancmc()+"ú���޹�����ѯ",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500);
				rt.body.setFontSize(12);
				
				
				
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 2, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(30);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	
	

	
	
	

	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
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
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
		    visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
//		if (getMarkbh().equals("true")) { // �ж����getMarkbh()����"true"����ô���³�ʼ�����������
//		getChehModels();
//		this.getCaiyhModels();
//		}
		getSelectData();
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
			
			
		}
		
		//getSelectData();
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
	

/*
	
//	 ����������
	public IDropDownBean getChehValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getChehModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setChehValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setChehModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getChehModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getChehModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getChehModels() {

		
		String sql_gongys =

			"select rownum as xuh ,cheph from (\n" +
			" select distinct c.cheph from chelxxlogb c)";


		
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
//	
//
//	
//	 ͣ����������
	public IDropDownBean getCaiyhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getChehModel()
							.getOption(0));
	}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setCaiyhValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setCaiyhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getCaiyhModel() {
	if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getCaiyhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getCaiyhModels() {
		
		
		String sql_gongys =

			"select rownum as xuh ,suocr from (\n" +
			"select distinct c.suocr from chelxxlogb c)";


		
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	*/
	
}
