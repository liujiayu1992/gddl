package com.zhiren.jt.het.hethqd;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author 
 * ��ɽ��������ѯ
 */
public class Hethqd  extends BasePage implements PageValidateListener{

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
	
//	private static final String BAOBPZB_GUANJZ = "CAIYLBCX";// baobpzb�ж�Ӧ�Ĺؼ���
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	private boolean rqchange = false;//�ж������Ƿ�ı�
	private boolean gyschange = false;//�жϹ�Ӧ���Ƿ�ı�
//	��ʼ����
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (this.briq!=null){
			if(!this.briq.equals(briq)){
				rqchange = true;
			}
		}
		
		this.briq = briq;
	}
	
//	��ֹ����
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq!=null){
			if(!this.riq.equals(riq)){
				rqchange = true;
			}
		}
		
		this.riq = riq;
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
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel11(null);
			visit.setDropDownBean11(null);
			
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
			
			getGongysModels();
			setGongysValue(null);
			getGongysModel();
			this.getSelectData();
		}else if(rqchange){
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
			
			getGongysModels();
			setGongysValue(null);
			getGongysModel();
		}else if(gyschange){
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
		}
		gyschange = false;
		rqchange = false;
		isBegin = true;
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

	
	public String getHuisbm(){//xitxxb������ Ĭ��Ϊ7��
		Visit visit=(Visit)this.getPage().getVisit();
		String hangs = "7";
		hangs = MainGlobal.getXitxx_item("��ͬ", "��ͬ��ǩ���л����Ŵ�ӡ����", String.valueOf(visit.getDiancxxb_id()), "7");
		return hangs;
	}

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();		
		String sql = 
			"select d.mingc as danw,\n" +
			"       mb.mingc as hetmc,\n" + 
			"       decode(count(h.id),1,sum(jg.jij),min(jg.jij)||'��'||max(jg.jij)) as hetjk,\n" + 
			"       h.gongfdwmc as jiaf,\n" + 
			"       h.xufdwmc as yif\n" + 
			"from hetb h,hetb_mb mb,hetjgb jg,diancxxb d\n" + 
			"where h.hetb_mb_id = mb.id\n" + 
			"      and h.diancxxb_id = d.id\n" + 
			"      and jg.hetb_id = h.id\n" + 
			"      and d.id = "+getTreeid_dc()+"\n" + 
			"      and h.qisrq>="+ DateUtil.FormatOracleDate(getBRiq()) + " and h.qisrq<="+ DateUtil.FormatOracleDate(getRiq()) + "\n" +  
//			"      and h.gongysb_id =\n" + 
			"      and h.hetbh = '"+getBianhValue().getValue()+"'\n" + 
			"group by d.mingc,mb.mingc,h.gongfdwmc,h.xufdwmc";


		ResultSetList rs = con.getResultSetList(sql);
		String danw = "";
		String hetmc = "";
		String hetjk = "";
		String jiaf = "";
		String yif = "";

		while(rs.next()){
			danw = rs.getString("danw");
			hetmc = rs.getString("hetmc");
			hetjk = rs.getString("hetjk");
			jiaf = rs.getString("jiaf");
			yif = rs.getString("yif");
		}

		int huisbm = Integer.parseInt(getHuisbm());
		
		 String ArrHeader[][]=new String[7][5];
		 ArrHeader[0]=new String[] {"","","","",""};
		 ArrHeader[1]=new String[] {"","","","",""};
		 ArrHeader[2]=new String[] {"�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾"};
		 ArrHeader[3]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
		 ArrHeader[4]=new String[] {"��   ͬ   ��   ǩ   ��","��   ͬ   ��   ǩ   ��","��   ͬ   ��   ǩ   ��","��   ͬ   ��   ǩ   ��","��   ͬ   ��   ǩ   ��"};
		 ArrHeader[5]=new String[] {"","","","","","","","","","","","","","","","","","",""};
		 ArrHeader[6]=new String[] {"��λ��"+danw,"��λ��"+danw,"��λ��"+danw,"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp��&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp��&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp��","&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp��&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp��&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp��"};

		
		 String ArrBody[][]=new String[huisbm+6][5];
		 ArrBody[0]=new String[] {"��ͬ����",hetmc,hetmc,"��ͬ�ۿ�",hetjk};
		 ArrBody[1]=new String[] {"��   ��",jiaf,jiaf,"��  ��",yif};
		 ArrBody[2]=new String[] {"���첿��","","","������",""};
		 for(int i=3;i<(huisbm+3);i++){
			 ArrBody[i]=new String[] {"��<br><br>��<br><br>��<br><br>��","","","",""};
		 }
		 ArrBody[huisbm+3]=new String[] {"�ֹܾ��첿���쵼","�ֹܾ��첿���쵼","","",""};
		 ArrBody[huisbm+4]=new String[] {"���ܸ��ܾ���","���ܸ��ܾ���","","",""};
		 ArrBody[huisbm+5]=new String[] {"�ܾ���(����)","�ܾ���(����)","","",""};

		int i=0;
		int ArrWidth[]=new int[] {75,75,150,75,225};
		
//		 ����ҳTitle
		 rt.setTitle(new Table(ArrHeader,0,0,0));
		 rt.setBody(new Table(ArrBody,0,0,0));
		 rt.body.setWidth(ArrWidth);
		 rt.title.setWidth(ArrWidth);
		 rt.title.setRowHeight(5,50);
		 
//		 �ϲ���Ԫ��
//		 ��ͷ_Begin
		 rt.title.merge(2, 1, 2, 5);
		 rt.title.merge(3, 1, 3, 5);
		 rt.title.merge(4, 1, 4, 5);
		 rt.title.merge(5, 1, 5, 5);
		 rt.title.merge(6, 1, 6, 5);
		 
		 rt.title.merge(7, 1, 7, 3);
		 rt.title.merge(7, 4, 7, 5);
		 
		 rt.title.setBorder(0,0,0,0);
		 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(7,Table.PER_BORDER_BOTTOM,0);
		 
		 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(7,Table.PER_BORDER_RIGHT,0);
		 
		 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_LEFT);
		 
//		 ����
		 rt.title.setCells(3, 1, 3, 5, Table.PER_FONTNAME, "����");
		 rt.title.setCells(3, 1, 3, 5, Table.PER_FONTSIZE, 12);
		 rt.title.setCells(4, 1, 4, 5, Table.PER_FONTNAME, "Arial Unicode MS");
		 rt.title.setCells(4, 1, 4, 5, Table.PER_FONTSIZE, 12);
		 rt.title.setCells(5, 1, 5, 5, Table.PER_FONTNAME, "����");
		 rt.title.setCells(5, 1, 5, 5, Table.PER_FONTSIZE, 20);
//		 ����				 
		 
//		 ͼƬ
		 rt.title.setCellImage(2, 1, 120, 60, "imgs/report/GDBZ.gif");	//����ı�־�����ֳ�Ҫһ�����Ͼ����ˣ�
		 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		 rt.title.setCellImage(6, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
//		 ͼƬ_End		 
//		 ��ͷ_End
		 

//		Table bt=new Table(huisbm+6,5);
//		rt.setBody(bt);
//		
//		String[][] ArrHeader1 = new String[1][5];
//		ArrHeader1[0] = ArrHeader[0];
//		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
//		
//		rt.setTitle("��&nbspͬ&nbsp��&nbspǩ&nbsp��", ArrWidth);
//		
//		rt.setDefaultTitle(1,3,"��λ:"+danw,Table.ALIGN_LEFT);
//		
//		rt.setDefaultTitle(4,2,"&nbsp&nbsp&nbsp&nbsp��&nbsp&nbsp&nbsp&nbsp��&nbsp&nbsp&nbsp&nbsp��",Table.ALIGN_CENTER);

		//�ϲ�
		rt.body.merge(1, 2, 1, 3);
		rt.body.merge(2, 2, 2, 3);
//		rt.body.merge(3, 2, 3, 3);
		rt.body.merge(4, 1, huisbm+3, 1);
		
		rt.body.merge(huisbm+4, 1, huisbm+4, 2);
		rt.body.merge(huisbm+5, 1, huisbm+5, 2);
		rt.body.merge(huisbm+6, 1, huisbm+6, 2);
		for(int a=4;a<=(huisbm+6);a++){
			rt.body.merge(a, 3, a, 5);
		}
			
//		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setRowHeight(50);
//		rt.body.setHeaderData(ArrHeader);// ��ͷ����

		rt.body.setCellBorderRight(3, 2, 0);//����3��2�����Ҳ����
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
	
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 5, "ע�����ݱ���ҵ��ͬ�����ƶȹ涨Ȩ�����л����������򣬻����ż�������Ӧд�������ǩ��������", Table.ALIGN_LEFT);
		
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();

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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��ͬ����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "caiF");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("hetrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("��:"));
		DateField dfb1 = new DateField();
		dfb1.setValue(getRiq());
		dfb1.Binding("RIQ", "caiF");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb1.setId("hetrqb1");
		tb1.addField(dfb1);
		
		tb1.addText(new ToolbarText("-")); 
		
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
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��Ӧ��:"));
		ComboBox gys = new ComboBox();
		gys.setTransform("GongysDropDown");
		gys.setEditable(true);
		gys.setWidth(100);
		gys.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(gys);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ͬ���:"));
		ComboBox bh = new ComboBox();
		bh.setTransform("BianhDropDown");
		bh.setEditable(true);
		bh.setWidth(100);
		bh.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(bh);
		
		tb1.addText(new ToolbarText("-"));
		
		
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){if(document.all.BianhDropDown.value==-1){Ext.Msg.alert('��ʾ��Ϣ','��ѡ��һ����ͬ���!'); return;} document.Form0.submit();}");
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

//	���

	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getBianhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setBianhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setBianhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getBianhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getBianhModels() {
		
		String gys = "";
		if(getGongysValue().getValue().equals("��ѡ��")||getGongysValue().getId()==-1){
			gys = "";
		}else{
			gys = "      and h.gongysb_id ="+getGongysValue().getId()+"\n";
		}

		String sql =
			"select h.id,h.hetbh\n" +
			"from hetb h\n" + 
			"where h.qisrq>="+ DateUtil.FormatOracleDate(getBRiq()) + " and h.qisrq<="+ DateUtil.FormatOracleDate(getRiq()) + "\n" + 
			"      and h.diancxxb_id ="+getTreeid_dc()+"\n" + 
			gys;

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
	
//	��Ӧ��

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean11((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean11() != value) {
			gyschange = true;
			((Visit) getPage().getVisit()).setDropDownBean11(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getGongysModels() {

		String sql =
			"select distinct g.id,g.quanc\n" +
			"from gongysb g,hetb h\n" + 
			"where h.gongysb_id = g.id\n" + 
			"      and h.qisrq>="+ DateUtil.FormatOracleDate(getBRiq()) + "\n" + 
			"      and h.qisrq<="+ DateUtil.FormatOracleDate(getRiq()) + "\n" + 
			"      and h.diancxxb_id="+getTreeid_dc()+"\n";

		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
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
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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
	
//	-------------------------�糧Tree END----------

}