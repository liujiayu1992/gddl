package com.zhiren.dc.jilgl.gongl.rijh;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author huochaoyuan
 *汽车计划检斤用的编码对照单
 *在填写日计划后，查看各种编码的对应关系，"caiy"1、2级编号对应，"zhiy"2、3级编号对应,"all"3级编码对应
 *按时间、电厂查询
 */
public class Caiyzdd_rjh extends BasePage {
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

	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		
		blnIsBegin = false;
		if (mstrReportName.equals(Caiy)){
			return getBianm(1,2);
		}else if(mstrReportName.equals(Zhiy)){

			return getBianm(2,3);
		}else if(mstrReportName.equals(All)){

			return getBianm2();
		}else{	
			return "无此报表";
		}		
	}


	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
	
	private String getBianm(int a,int b) {
		Visit visit=(Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
		String cheh="";
		if(!getCheh1().equals("")){
			cheh="and l.cheph='"+getCheh1()+"'\n";
		}
		StringBuffer sb = new StringBuffer("");
		sb.setLength(0);
		sb.append("select a.bianm as bianm1,a.mingc as mingc1,b.bianm as bianm2,b.mingc as mingc2 from(\n");
		sb.append("select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id from qicrjhb where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate()+"') j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib="+a+") a\n");
		sb.append(",\n");
		sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id from qicrjhb where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate()+"') j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib="+b+") b\n");
		sb.append("where a.zhillsb_id=b.zhillsb_id\n");
        sb.append("order by bianm1");

		ResultSet rs = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String[][] dataList = null;
		int rowNum = 0;
		try {
			int i = 0;
			rs.last();
			rowNum = rs.getRow() * 3;
			if (rowNum <= 0) {
				return null;
			}
			dataList = new String[rowNum][6];
			rs.beforeFirst();
			Date dysj=new Date();
			while (rs.next()) {
				dataList[i++] = new String[] { rs.getString("mingc1"), rs.getString("bianm1"),
						"|", rs.getString("mingc2"),rs.getString("bianm2") };
				dataList[i++] = new String[] { "打印时间",DateUtil.FormatDateTime(dysj),
						"|","打印时间",DateUtil.FormatDateTime(dysj)};
				dataList[i++] = new String[] { "----", "----", "|", "----",
						"----"};
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int[] ArrWidth = new int[] { 140, 140, 50,140,140};

		rt.setBody(new Table(rowNum, 5));
		rt.body.setWidth(ArrWidth);

		for (int i = 0; i < dataList.length; i++) {
			for (int j = 0; j < 5; j++) {
				rt.body.setCellValue(i + 1, j + 1, dataList[i][j]);
			}
		}
		rt.body.setPageRows(21);

		rt.body.setBorder(0, 0, 2, 2);

		for (int i = 1; i <= 5; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.body.setColCells(1, Table.PER_BORDER_LEFT, 2);
//		rt.body.setColCells(6, Table.PER_BORDER_RIGHT, 2);

		for (int i = 1; i <= dataList.length; i++) {
			if (i % 3 == 0) {
				rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 2);
				rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
				rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(i, Table.PER_BORDER_TOP, 1);
			}
		}

		con.Close();
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();

		rt.body.setRowHeight(45);

		return rt.getAllPagesHtml();


	}
	
	private String getBianm2() {
		Visit visit=(Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
		String cheh="";
		if(!getCheh1().equals("")){
			cheh="and l.cheph='"+getCheh1()+"'\n";
		}
		StringBuffer sb = new StringBuffer("");
		sb.setLength(0);
		sb.append("select a.bianm as bianm1,a.mingc as mingc1,b.bianm as bianm2,b.mingc as mingc2 ,c.bianm as bianm3,c.mingc as mingc3 from(\n");
		sb.append("select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id from qicrjhb where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate()+"') j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib=1) a\n");
		sb.append(",\n");
		sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id from qicrjhb where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate()+"') j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib=2) b\n");
		sb.append(",\n");
		sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id from qicrjhb where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate()+"') j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib=3) c\n");
		sb.append("where a.zhillsb_id=b.zhillsb_id\n");
		sb.append("and a.zhillsb_id=c.zhillsb_id\n");
        sb.append("order by bianm1");
		ResultSet rs = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String[][] dataList = null;
		int rowNum = 0;
		try {
			int i = 0;
			rs.last();
			rowNum = rs.getRow() * 3;
			if (rowNum <= 0) {
				return null;
			}
			dataList = new String[rowNum][8];
			rs.beforeFirst();
			Date dysj=new Date();
			while (rs.next()) {
				dataList[i++] = new String[] { rs.getString("mingc1"), rs.getString("bianm1"),
						"|", rs.getString("mingc2"),rs.getString("bianm2"),"|", rs.getString("mingc3"),rs.getString("bianm3") };
				dataList[i++] = new String[] { "打印时间",DateUtil.FormatDateTime(dysj),
						"|","打印时间",DateUtil.FormatDateTime(dysj),"|","打印时间",DateUtil.FormatDateTime(dysj)};
				dataList[i++] = new String[] { "----", "----", "|", "----","----","|", "----","----"};
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int[] ArrWidth = new int[] { 80, 100, 40,80,100,40,80,100};

		rt.setBody(new Table(rowNum, 8));
		rt.body.setWidth(ArrWidth);

		for (int i = 0; i < dataList.length; i++) {
			for (int j = 0; j < 8; j++) {
				rt.body.setCellValue(i + 1, j + 1, dataList[i][j]);
			}
		}
		rt.body.setPageRows(21);

		rt.body.setBorder(0, 0, 2, 2);

		for (int i = 1; i <= 8; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.body.setColCells(1, Table.PER_BORDER_LEFT, 2);
//		rt.body.setColCells(6, Table.PER_BORDER_RIGHT, 2);

		for (int i = 1; i <= dataList.length; i++) {
			if (i % 3 == 0) {
				rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 2);
				rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
				rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(i, Table.PER_BORDER_TOP, 1);
			}
		}

		con.Close();
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();

		rt.body.setRowHeight(45);

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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		//日期
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
//		DateField df1 = new DateField();
//		df1.setValue(this.getEndriqDate());
//		df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
//		df1.setWidth(80);
//		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(80);
//		tb1.addField(cb1);
//		tb1.addText(new ToolbarText("-"));

		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"form0",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
//		tb1.addText(new ToolbarText("供应商:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("车号:"));
//		TextField ch = new TextField();
//		ch.setValue(this.getCheh1());
//		ch.listeners="change:function(own,newvalue,oldvalue) {document.getElementById('CHEH1').value =newvalue;}";
//		ch.setWidth(80);
//		tb1.addField(ch);
//		
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
private String Caiy="caiy";//采样班用1、2级编码对应
private String Zhiy="zhiy";//制样班用2、3级编码对应
private String All="all"; //全部编码对应关系  3级编码对应
	
	private String mstrReportName="";
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString4(null);
			visit.setString1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDefaultTree(null);
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString1();
        }
		getToolbars();
		blnIsBegin = true;

	}

    // 车号
	public String _cheh="";
	
	public String getCheh1(){
		return _cheh;
	}
	
	public void setCheh1(String cheh){
		_cheh=cheh;
	}
	// 日期
    public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,0);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));

		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
   
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
    
//	电厂名称
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
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
