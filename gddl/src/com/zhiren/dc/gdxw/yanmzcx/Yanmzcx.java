package com.zhiren.dc.gdxw.yanmzcx;

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
/*
 * ����:tzf
 * ʱ��:2009-12-30
 * �޸�����:����ú��ѡ����ӿ۶�״̬ѡ���ú�� ȫ�������ʵ����Ҫ�鿴��
 */
public class Yanmzcx extends BasePage {

	/**
	 * ����:���ܱ�
	 * ʱ��:2009-10-25 15:00:13
	 * sunday,today my birthday,overtime!
	 * ����:������ú�²�ѯ
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

		tb1.addText(new ToolbarText("��ú����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("BRIQ");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true';}");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("��ú��:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(100);
		
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		
//	    ú��������
		tb1.addText(new ToolbarText("ú��:"));
		ComboBox Meik = new ComboBox();
		Meik.setTransform("MEIK");
		Meik.setWidth(100);
		//Meik.setListeners("select:function(){document.forms[0].submit();}");
		Meik.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false'; document.forms[0].submit();}");
		Meik.setEditable(true);
		tb1.addField(Meik);
		tb1.addText(new ToolbarText("-"));
		
	
//	    �۶�������
		tb1.addText(new ToolbarText("�۶�״̬:"));
		ComboBox Koud = new ComboBox();
		Koud.setTransform("KOUD");
		Koud.setWidth(100);
		Koud.setListeners("select:function(){document.forms[0].submit();}");
		Koud.setEditable(true);
		tb1.addField(Koud);
		tb1.addText(new ToolbarText("-"));
		
		
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
		//.addItem(rbtn);
		

		
		
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
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		String tiaoj="";
		long meik=this.getGongysValue().getId();
		if(meik==-1){
			tiaoj=" ";
		}else{
			tiaoj=" and c.meigy='"+getGongysValue().getValue().substring(getGongysValue().getValue().indexOf("---")+3, getGongysValue().getValue().length())+"' ";
			
			
		}
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and c.meikdwmc='"+this.getMeikValue().getValue()+"' ";
		}
		
		
		String koud_str=" ";
		long koud_id=this.getKoudValue().getId();
		if(koud_id==1){
			koud_str=" ";
		}else if(koud_id==2){
			koud_str=" and c.koud>0 ";
		}else{
			koud_str=" and c.koud=0 ";
		}
		

			sbsql.append(
					"select decode(c.qingcsj,null,decode(c.meikdwmc,null,'�ܼ�: '||count(c.id)||'��',c.meikdwmc||'--С��:'||count(c.id)||'��'),c.meikdwmc) as meikdwmc,\n" +
					"max(c.cheph) as cheph,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
					"sum(c.koud) as koud,\n" + 
					"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,max(c.meigy) as meigy,max(r.quanc) as quanc ,\n" + 
					"max(c.lury) as caiyy,max(c.zhongcjjy) as qingcy,max(c.qingcjjy) as zhongcy\n" + 
					"from chepbtmp c ,renyxxb r\n" + 
					"where c.meigy=r.zhiw\n" + 
					"and c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
					 tiaoj+"  \n" + 
					 mk_str+" \n"+
					 koud_str+" \n"+
					"group by rollup (c.meikdwmc,c.qingcsj)\n" + 
					"order by c.meikdwmc,c.qingcsj");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][12];
//				1120
				ArrHeader[0]=new String[] {"ú��","����","ë��","Ƥ��","����","�۶�","����ʱ��","��ú��","��úԱ","����Ա","�غ���Ա","�պ���Ա"};
				int ArrWidth[]=new int[] {130,60,60,60,60,50,90,55,55,55,65,65};
				  strFormat = new String[] { "", "","0.00","0.00", "0.00","0.00","" , "","","" , "","" };
				// ����
				rt.setTitle(visit.getDiancmc()+"��ú�²�ѯ",ArrWidth);
				rt.setDefaultTitle(1, 3, "����:"+kais+" �� "+jies, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(25);
				
				int iLastRow=rt.body.getRows();
				for (int i=1;i<=iLastRow;i++){
					if(rt.body.getCellValue(i,7).equals("")){
						rt.body.setCellValue(i, 2, "");
						rt.body.setCellValue(i, 8, "");
						rt.body.setCellValue(i, 9, "");
						rt.body.setCellValue(i, 10, "");
						rt.body.setCellValue(i, 11, "");
						rt.body.setCellValue(i, 12, "");
						
					}
					
				}
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 7);
					if((xiaoj.equals(""))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
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
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
		if (getMarkbh().equals("true")) { // �ж����getMarkbh()����"true"����ô���³�ʼ�����������
			getMeikModels();
		}
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
		
//		}else{
//			this.setMeikValue(null);
//			this.setMeikModel(null);
//		}
		getSelectData();
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
		
		String sql_gongys = "select r.id,r.quanc||'---'||r.zhiw from renyxxb r where r.bum='��úԱ' order by r.zhiw";
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	
	
	
//	 ú��������
	public IDropDownBean getMeikValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getMeikModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setMeikValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getMeikModels() {
		
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String sql=" select rownum rn, a.meikdwmc from ( select distinct c.meikdwmc from chepbtmp c where  "+
		" c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
		"and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1 ) a\n" ;
		
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql,"ȫ��"));
		return;
	}
	
	
//	 �۶�״̬������
	public IDropDownBean getKoudValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getKoudModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setKoudValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setKoudModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getKoudModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getKoudModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getKoudModels() {
		
		List list=new ArrayList();
		list.add(new IDropDownBean("1","ȫ��"));
		list.add(new IDropDownBean("2","����0"));
		list.add(new IDropDownBean("3","����0"));
		
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	
}
