package com.zhiren.dc.jilgl.jicxx.chedxx;

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

public class Chelxxb_cx extends BasePage {
	
	/**
	 * ����:���ܱ�
	 * ʱ��:2010-3-28 14:09:43
	 * ����:������ú��ѯ����
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb�ж�Ӧ�Ĺؼ���
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

    
//  ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    


	
	
//	��ȡ������
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"ȼ�����յ�";
		
		return sb;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("���䵥λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(120);
		CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
	
//		��������
		tb1.addText(new ToolbarText("����ʽ:"));
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(90);
		leix.setListeners("select:function(){document.Form0.submit();}");
		//leix.setEditable(true);
		tb1.addField(leix);
		

		
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
		if(hasDianc(String.valueOf(visit.getDiancxxb_id()))){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
	
			return getMeikbbhz();
	
	}
	
	
	
	
	public String getMeikbbhz(){


		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer talbe=new StringBuffer();	//�������
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		
		
		long meik=this.getGongysValue().getId();
		String meiktiaoj="";
		if(meik==-1){
			meiktiaoj="";
		}else{
			meiktiaoj="  and c.yunsdwb_id= "+meik;
		}
		String paix="";
		if(getJieslbValue().getValue().equals("�����䵥λ")){
			paix="order by y.mingc,cd.chedjc,c.cheph";
			
		}else if (getJieslbValue().getValue().equals("������")){
			paix="order by c.kah";
		}else if (getJieslbValue().getValue().equals("������")){
			paix="order by c.chezxm";
		}else if (getJieslbValue().getValue().equals("������")){
			paix="order by c.chex";
		}else{
			paix="order by c.cheph";
		}
	
		
		
		Report rt = new Report(); //������
		
			
		
				sbsql.setLength(0);
					sbsql.append(

							"select rownum as xuh,a.* from (\n"+
							"select y.mingc,cd.chedjc,c.cheph,c.chezxm,c.kah ,\n" +
							"c.chang,c.kuan,c.gao,c.dig,c.gangssl,ii.mingc as chex,\n" +
							"decode(c.islocked,0,'ʹ��','ͣ��') as zhuangt\n" +
							" from chelxxb c,chedxxb cd,yunsdwb y,item ii\n" + 
							"where c.ched_id=cd.id(+)\n" + 
							"and c.yunsdwb_id=y.id\n" + 
							" and c.chex=ii.id(+)\n"+
							""+meiktiaoj+"\n"+
							""+paix+" ) a"


					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][13];
		
					ArrHeader[0]=new String[] {"���","���䵥λ","����","����","��������","����","���᳤<br>(����)","�����<br>(����)",
							"�����<br>(����)","��ظ�<br>(����)","��������","����","״̬"};
					int ArrWidth[]=new int[] {55,120,75,75,65,60,75,60,60,60,60,90,60};
					  strFormat = new String[] { "","", "", "","" , "","", "", "","" , "","",""};
					// ����
					rt.setTitle(v.getDiancmc()+"������Ϣ",ArrWidth);
					
					rt.setDefaultTitle(4, 2, "", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 0));
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(3000);
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			
			
		
		
		
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
		
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			setChangbValue(null);
			setChangbModel(null);
//			visit.setDefaultTree(null);
//			setDiancmcModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
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
	
	

	
	
	
//	 �������(���յ�,�ܸ���,��Ʊ��)
	public IDropDownBean getJieslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJieslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJieslbValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJieslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJieslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJieslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJieslbModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�����䵥λ"));
		list.add(new IDropDownBean(2, "������"));
		list.add(new IDropDownBean(3, "������"));
		list.add(new IDropDownBean(4, "������"));
		list.add(new IDropDownBean(5, "������"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
	
	
	
//	 ��Ӧ��������
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		
		String sql_gongys = 
			"select id,mingc from yunsdwb order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	
}
