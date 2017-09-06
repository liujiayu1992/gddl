package com.zhiren.dc.jilgl.gongl.rijh;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

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
 *汽车计划检斤用的采样任务单
 *在填写日计划、维护日计划采样维护后，查看该页面，指导采样人员如何采样
 *按时间、电厂、车号查询
 */
public class Caiyzdd_rjh2 extends BasePage {
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
		if (mstrReportName.equals(A)){
			return getBianm(1);
		}else if(mstrReportName.equals(B)){
			return getBianm(2);
		}else if(mstrReportName.equals(C)){
			return getBianm(3);
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
	
	private String getBianm(int a) {
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
		sb.append("select  z.zhillsb_id as id,z.bianm as bianm,zz.mingc as mingc,decode((select mingc from zhuanmlb where jib="+a+"-1),null,zz.mingc,(select mingc from zhuanmlb where jib="+a+"-1))as mingc1\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id from qicrjhb where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate()+"') j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib="+a);

		ResultSet rs = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String[][] dataList = null;
		int rowNum = 0;
		try {
			int i = 0;
			rs.last();
			rowNum = rs.getRow() * 9;
			if (rowNum <= 0) {
				return null;
			}
			dataList = new String[rowNum][14];
			rs.beforeFirst();
			while (rs.next()) {
				dataList[i++] = new String[] {getImgServletPath("dt_hlj_sys"
						+ ".gif"),getImgServletPath("dt_hlj_sys"
								+ ".gif"),"|",getImgServletPath("dt_hlj_sys"
										+ ".gif"),getImgServletPath("dt_hlj_sys"
												+ ".gif"),"|",getImgServletPath("dt_hlj_sys"
														+ ".gif"),getImgServletPath("dt_hlj_sys"
																+ ".gif"),"|",getImgServletPath("dt_hlj_sys"
																		+ ".gif"),getImgServletPath("dt_hlj_sys"
																				+ ".gif"),"|",getImgServletPath("dt_hlj_sys"
																						+ ".gif"),getImgServletPath("dt_hlj_sys"
																								+ ".gif")};					
				dataList[i++] = new String[] {"燃料管理部制样班","燃料管理部制样班","|","燃料管理部制样班","燃料管理部制样班","|","燃料管理部制样班","燃料管理部制样班","|","燃料管理部制样班","燃料管理部制样班","|","燃料管理部制样班","燃料管理部制样班"};	
				if(!getLeixValue().getValue().equals("空")){
				dataList[i++] = new String[] {"煤样种类","分析样","|","煤样种类","分析样","|","煤样种类","存查样","|","煤样种类","存查样","|","煤样种类","全水样"};	
				dataList[i++] = new String[] {"煤样编码",rs.getString("bianm"),"|","煤样编码",rs.getString("bianm"),"|","煤样编码",rs.getString("bianm"),"|","煤样编码",rs.getString("bianm"),"|","煤样编码",rs.getString("bianm")};	
				}else{
					dataList[i++] = new String[] {"煤样种类","","|","煤样种类","","|","煤样种类","","|","煤样种类","","|","煤样种类",""};	
					dataList[i++] = new String[] {"煤样编码","","|","煤样编码","","|","煤样编码","","|","煤样编码","","|","煤样编码",""};	
					
				}
				dataList[i++] = new String[] {"制样人员","","|","制样人员","","|","制样人员","","|","制样人员","","|","制样人员",""};	
				dataList[i++] = new String[] {"制样人员","","|","制样人员","","|","制样人员","","|","制样人员","","|","制样人员",""};	
				dataList[i++] = new String[] {"制样日期","","|","制样日期","","|","制样日期","","|","制样日期","","|","制样日期",""};	
				dataList[i++] = new String[] {"样品重量","","|","样品重量","","|","样品重量","","|","样品重量","","|","样品重量",""};	
				dataList[i++] = new String[]{ "----", "----", "|","----", "----", "|", "----", "----", "|", "----", "----", "|", "----", "----"};
				                                                      
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int[] ArrWidth = new int[] { 60,100,40,60,100,40,60,100,40,60,100,40,60,100};

		rt.setBody(new Table(rowNum,14));
		rt.body.setWidth(ArrWidth);
		for (int i = 0; i < dataList.length; i++) {
			for (int j = 0; j < 14; j++) {
				rt.body.setCellValue(i + 1, j + 1, dataList[i][j]);
			}
		}
		rt.body.setPageRows(27);

		rt.body.setBorder(0,0,0,2);

		for (int i = 1; i <= 14; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
//        rt.body.setCells(1, 1, iEndRow, iEndCol, intProperty, blnValue)
//		for (int i = 1; i <= dataList.length; i++) {
//			if (i % 3 == 0) {
//				rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 2);
//				rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
//				rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
//				rt.body.setRowCells(i, Table.PER_BORDER_TOP, 1);
//			}
//		}
		for (int i = 1; i <= 14; i++) {
			if (i % 3 == 0) {
		rt.body.setColCells(i, Table.PER_BORDER_RIGHT,2);
		rt.body.setColCells(i, Table.PER_BORDER_LEFT,1);
		rt.body.setColCells(i, Table.PER_BORDER_BOTTOM,0);
		rt.body.setColCells(i, Table.PER_BORDER_TOP,0);
			}
		}
		
		rt.body.setColCells(1, Table.PER_BORDER_LEFT, 2);
		rt.body.setColCells(14, Table.PER_BORDER_RIGHT, 2);
		for(int i = 1; i <= dataList.length; i++){
			   rt.body.setRowCells(i, Table.PER_BORDER_TOP, 0);
			   rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 0);
			   rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
			   rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
			   rt.body.setRowCells(i+1, Table.PER_BORDER_TOP, 0);
			   rt.body.setRowCells(i+1, Table.PER_BORDER_BOTTOM, 2);
			   rt.body.setRowCells(i+1, Table.PER_BORDER_LEFT, 0);
			   rt.body.setRowCells(i+1, Table.PER_BORDER_RIGHT, 0);
			   i=i+8;
		}
		for(int i = 9; i <= dataList.length; i++){
			rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_TOP, 1);
			rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 0);
			   i=i+8;
			   
			}
		for (int i = 1; i <= 14; i++) {
			if (i % 3 == 0) {
		rt.body.setColCells(i, Table.PER_BORDER_BOTTOM,0);
		rt.body.setColCells(i, Table.PER_BORDER_TOP,0);
			}
		}

		for(int i = 1; i <= dataList.length; i++){
		   rt.body.mergeRow(i);
		   rt.body.mergeRow(i+1);
		   i=i+8;
		   
		}
		for(int i = 5; i <= dataList.length; i++){
			for (int j=1;j<14;j++){		
			   rt.body.mergeCell(i, j, i+1, j);			   
			   j=j+2;
			}
			i=i+8;
			} 
		con.Close();
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
        rt.body.setFontSize(9);
		rt.body.setRowHeight(23);
		for(int i = 9; i <= dataList.length; i++){
			rt.body.setRowHeight(i, 34);
			i=i+8;
		}
//		for (int i = 1; i <= dataList.length; i++) {
//			rt.body.setRowHeight(i, 20);
//			i=i+2;
//		}

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
		
		tb1.addText(new ToolbarText("煤样类型:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("LEIX");
		cb2.setEditable(true);
		cb2.setWidth(80);
		tb1.addField(cb2);
		
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
private String A="a";//zhuanmb中级别为1
private String B="b";//zhuanmb中级别为2
private String C="c";//zhuanmb中级别为3
	
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
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			getLeixModel();
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
	
	  public IDropDownBean getLeixValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getLeixModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean2();
	    }
	    public void setLeixValue(IDropDownBean Value) {
//	    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//		    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//		    		((Visit) getPage().getVisit()).setBooleanData1(true);
//		    	}else{
//		    		((Visit) getPage().getVisit()).setBooleanData1(false);
//		    	}
		    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//	    	}
	    }
	    public void setLeixModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
	    }

	    public IPropertySelectionModel getLeixModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
	            getLeixModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel2();
	    }
	    public void getLeixModels() {
	    	 List list=new ArrayList();
	         list.add(new IDropDownBean(0,"正常"));
	         list.add(new IDropDownBean(1,"空"));
	         ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
	         return ;
	    } 
	    
	    public String CreateChartFile(JFreeChart chart, String filebassname) {
			try {
				Visit visit = (Visit) getPage().getVisit();
				String FilePath = MainGlobal.getServletPath(getPage())
						+ "/img/CaiyPicture" + visit.getRenyID();
				File dir = new File(FilePath.replaceAll("\\\\", "/"));
				if (dir.exists()) {
					deleteFile(dir);// 册除文件及文件夹下所在的文件
					dir.mkdir();
				} else {
					dir.mkdirs();
				}
				File chartFile = new File(dir, filebassname + ".jpg");
				ChartUtilities.saveChartAsJPEG(chartFile, chart, 990, 400);
				return getImgServletPath(chartFile.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
	    
		private static void deleteFile(File dir) throws IOException {// 册除文件夹及文件夹下所有内容
			if ((dir == null) || !dir.isDirectory()) {
				throw new IllegalArgumentException("Argument " + dir
						+ " is not a directory. ");
			}
			File[] entries = dir.listFiles();
			int sz = entries.length;
			for (int i = 0; i < sz; i++) {
				if (entries[i].isDirectory()) {
					deleteFile(entries[i]);
				} else {
					entries[i].delete();
				}
			}
			dir.delete();
		}
		public String getImgServletPath(String name) {
			Visit visit = (Visit) getPage().getVisit();
			String a="<img width=160 height=20 src='"
					+ MainGlobal.getHomeContext(getPage()) + "/imgs/report"+
					"/" + name + "' />";
			return a;
		}
}
