package com.zhiren.gangkjy.duicgl.duicfgl;
/*
 * ����:����
 * ʱ��:2009-3-27
 * ����:�Ѵ�ѵĲ�ѯͳ�Ʊ���
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;


import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Duicfcxtj extends BasePage {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
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

	public boolean getRaw() {
		return true;
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

	private String REPORT_NAME_RUCMZJYYB = "Rihcdyxxreport";// �볧ú�ʼ��鱨��

//	private String REPORT_NAME_RUCMZJYYB_A = "Rucmzjyyb_A";// �볧ú�ʼ��鱨��
//
//	private String REPORT_NAME_RUCMZJYYB_B = "Rucmzjyyb_B";// �볧ú�ʼ��鱨��

	private String REPORT_NAME_REZC = "rihcdyxx";

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		// if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB)) {
		return getRihcdyxxreport();
		// } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_A)) {
		// return getRucmzjyyb_A();
		// } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_B)) {
		// return getRucmzjyyb_B();
		// } else {
		// return "�޴˱���";
		// }
	}

	private String getRihcdyxxreport() {
		Visit visit = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();

		String strDianc = "";
		if(this.getDiancTreeJib()==1){//����
			strDianc = "";
		}else if(this.getDiancTreeJib()==2){
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
		
		String sql=
			"select decode(grouping(m.mingc), 1, '�� ��', m.mingc) as meicb_id,\n" +
			"       decode(grouping(m.mingc) + grouping(d.riq),\n" + 
			"              1,\n" + 
			"              'С��',\n" + 
			"              to_char(d.riq, 'yyyy-mm-dd')) as riq,\n" + 
			"\n" + 
			"       to_char(d.jifksrq,'yyyy-mm-dd') as jifksrq,\n" + 
			"       to_char(d.jifjsrq,'yyyy-mm-dd') as jifjsrq,\n" + 
			"       sum(d.duicsl) as duicsl,\n" + 
			"       sum(d.jine) as jine,\n" + 
			"       d.beiz as beiz\n" + 
			"\n" + 
			"  from duicf d,  meicb m,diancxxb dc \n" + 
			"\n" + 
		
			"   where d.meicb_id = m.id\n" + 
			"   and d.diancxxb_id =dc.id "+ strDianc+"\n" + 
			"      and  d.riq>= to_date('"+ getRiq() +"','yyyy-mm-dd')\n" + 
			"      and  d.riq<= to_date('"+ getAfter() +"','yyyy-mm-dd')\n"+
			"\n" + 
			" group by rollup(m.mingc, d.riq, d.jifksrq, d.jifjsrq, d.beiz)\n" + 
			"having grouping(d.riq) = 1 or grouping(d.beiz) = 0\n" + 
			" order by (m.mingc) desc, (d.riq) asc";
		
		ResultSet rs = con.getResultSet(sql);
//		System.out.println(sql);

		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][7];
		ArrHeader[0]=new String[] {Local.duow,Local.fufrq,Local.jifksrq,Local.jifjsrq,Local.duicsl,Local.jine,Local.beiz};
		
		int ArrWidth[]=new int[] {100,100,100,100,100,100,120};

		rt.setTitle("�Ѵ�Ѳ�ѯͳ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, "��ѯ����:" + getRiq() + "��" + getAfter(),Table.ALIGN_CENTER);
		
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

//		String[] strFormat = new String[] {  "", "", "", "", "", "",
//				"0.00", "", "", "0.00", "", "", "0.00", "", "",
//				"0.00", "", "" , "0.00","", "", "0.00", ""};

		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeFixedCols();
//		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 7; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "��ӡ����:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(7, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}

	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public void getSelectData() {
		
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ѯ����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
			
// ��
		tb1.addText(new ToolbarText("��λ����:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
		((Visit)getPage().getVisit()).setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//		egu.addTbarText("-");// ���÷ָ���

//		tb1.addText(new ToolbarText("���䷽ʽ:"));
//		ComboBox meik = new ComboBox();
//		meik.setTransform("YUNSFSSelect");
//		meik.setEditable(true);
//		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(meik);

		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb.setIcon(SysConstant.Btn_Icon_Print);
		setToolbar(tb1);

	}

	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
	
//	��ӵ糧��
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
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

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}

	// ���䷽ʽ������
//	private boolean falg1 = false;
//
//	private IDropDownBean YunsfsValue;
//
//	public IDropDownBean getYunsfsValue() {
//		if (YunsfsValue == null) {
//			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
//		}
//		return YunsfsValue;
//	}
//
//	public void setYunsfsValue(IDropDownBean Value) {
//		if (!(YunsfsValue == Value)) {
//			YunsfsValue = Value;
//			falg1 = true;
//		}
//	}
//
//	private IPropertySelectionModel YunsfsModel;
//
//	public void setYunsfsModel(IPropertySelectionModel value) {
//		YunsfsModel = value;
//	}
//
//	public IPropertySelectionModel getYunsfsModel() {
//		if (YunsfsModel == null) {
//			getYunsfsModels();
//		}
//		return YunsfsModel;
//	}
//
//	public IPropertySelectionModel getYunsfsModels() {
//		String sql = "select id,mingc from yunsfsb";
//		YunsfsModel = new IDropDownModel(sql);
//		return YunsfsModel;
//	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
			setTreeid(null);
			getSelectData();
		}
		blnIsBegin = true;
		getSelectData();

	}

}
