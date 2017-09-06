package com.zhiren.dc.hesgl.report;

import java.sql.ResultSet;
import java.util.Date;

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
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Dancyfjscx extends BasePage {
	// 界面用户提示
	private String msg = "";
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
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
		return getSelectData();
	}
	
	// 运输单位模糊查询
	private String YSDW;

	public String getYsdw() {
		return YSDW;
	}

	public void setYsdw(String ysdw) {
		this.YSDW = ysdw;
	}
	
	private String YSDWID;

	public String getYsdwid() {
		return YSDWID;
	}

	public void setYsdwid(String ysdwid) {
		this.YSDWID = ysdwid;
	}
	
	// 页面变化记录
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
	
	// 设置制表人默认当前用户
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "否";
		String sql = "select zhi from xitxxb where mingc = '月报管理制表人是否默认当前用户' " +
								"and diancxxb_id = " + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
		  while(rs.next()) {
			  zhi = rs.getString("zhi");
		  }
		} catch (Exception e) {
			System.out.println(e);
		}
		if (zhi.equals("是")) {
			zhibr = visit.getRenymc();
		}	
		return zhibr;
	}
	
	// 获得选择的树节点的对应的电厂名称   
    private String getDcMingc(String id) { 
    	if(id == null || "".equals(id)) {
    		return "";
    	}
		JDBCcon con = new JDBCcon();
		String mingc = "";
		String sql = "select mingc from diancxxb where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
    
    // 获得选择的树节点的对应的供应商名称   
    private String[] getGys(String id) { 
    	String[] gys = {"全部", "-1"};
    	if (id == null || "".equals(id)) {
    		return gys;
    	}
		JDBCcon con = new JDBCcon();
		String sql = "select mingc, lx from vwgongysmk where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			gys[0] = rsl.getString("mingc");
			gys[1] = rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
    // 刷新衡单列表
	public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		/*
		// 电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "Form0", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
		*/
		// 结算方案
		tb1.addText(new ToolbarText("结算方案:"));
		LovComboBox jiesfa = new LovComboBox();
		jiesfa.setId("jiesfa");
		jiesfa.setTransform("JiesfaDropDown");
		jiesfa.setForceSelection(false);
//		leix.setId("JiesfaDropDown");
		jiesfa.setLazyRender(true);// 动态绑定
		jiesfa.setWidth(160);
		jiesfa.setListeners("select:function(e){document.getElementById('CHANGE').value = e.getValue();}");
		tb1.addField(jiesfa);
		tb1.addText(new ToolbarText("-"));
		
		// 运输单位
		tb1.addText(new ToolbarText("运输单位:"));
		LovComboBox yunsdw = new LovComboBox();
		yunsdw.setId("yunsdw");
		yunsdw.setTransform("YunsdwDropDown");
		yunsdw.setForceSelection(false);
//		leix.setId("JiesfaDropDown");
		yunsdw.setLazyRender(true);// 动态绑定
		yunsdw.setWidth(160);
		yunsdw.setListeners("select:function(e){	\n" +
								"document.getElementById('YUNSDW').value = e.getRawValue();\n" +
								"document.getElementById('YUNSDWID').value = e.getValue();\n" +
								"}");
		tb1.addField(yunsdw);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){\n" +
														"		if(jiesfa.getRawValue()!=''){\n" +
														"			document.getElementById('RefurbishButton').click();\n" +
														"		}else{\n" +
														"			Ext.MessageBox.alert('提示信息','请选择一个结算方案!');\n" +
														"			return;\n" +
														"		}}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	private String getSelectData(){
		
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		String time = "";
		String kaisrq = "";
		String jiezrq = "";
		ResultSetList rstmp = null;
		String sql_condition = "";	//运输单位附加条件
		
		if(null!=this.getYsdw()&&!"".equals(this.getYsdw())&&!"全部".equals(this.getYsdw())){
			
			String tmp[]=null;
			
			tmp = getYsdw().split(",");
			
			for(int i=0;i<tmp.length;i++){
				
				if(i==0){
					
					sql_condition = "and (JY.SHOUKDW IN ('"+tmp[i].trim()+"')	\n";
				}else{
					
					sql_condition+= "or JY.SHOUKDW IN ('"+tmp[i].trim()+"')	\n";
				}
			}
			
			sql_condition+=")";
		}
		
		sql = 
				"SELECT TO_CHAR(min(DAOHJZSJ), 'yyyy-MM') || '月' AS TITLE,\n" +
				"\t\t\t TO_CHAR(min(DAOHQSSJ), 'yyyy-MM-dd') AS KAISRQ,\n" + 
				"\t\t\t TO_CHAR(max(DAOHJZSJ), 'yyyy-MM-dd') AS JIEZRQ\n" + 
				"\tFROM JIESFAB\n" + 
				" WHERE ID IN ("+this.getChange()+")";

		rstmp = con.getResultSetList(sql);
		if(rstmp.next()){
			
			time = rstmp.getString("TITLE");
			kaisrq = rstmp.getString("KAISRQ");
			jiezrq = rstmp.getString("JIEZRQ");
		}
		
		
		sql = "SELECT ROWNUM AS XUH, A.*\n" +
			"	FROM (SELECT DECODE(JY.SHOUKDW,\n" +
			"\t\t\t\t\t\t\tNULL,\n" + 
			"\t\t\t\t\t\t\t'车队合计',\n" + 
			"\t\t\t\t\t\t\tDECODE(C.CHEPH, NULL, JY.SHOUKDW || '合计', JY.SHOUKDW)) AS YUNSDW,\n" + 
			"\t\t\t DECODE(C.CHEPH,\n" + 
			"\t\t\t\t\t\t\tNULL,\n" + 
			"\t\t\t\t\t\t\t'',\n" + 
			"\t\t\t\t\t\t\tDECODE(JY.MEIKDWMC, NULL, C.CHEPH || '合计', C.CHEPH)) AS CHEPH,\n" + 
			"\t\t\t JY.MEIKDWMC,\n" + 
			"\t\t\t COUNT(C.ID) AS CHES,\n" + 
			"\t\t\t SUM(ROUND_NEW(C.MAOZ, 2) - ROUND_NEW(C.PIZ, 2) -\n" + 
			"\t\t\t\t\t ROUND_NEW(C.ZONGKD, 2)) AS LAYL,\n" + 
			"\t\t\t SUM(JY.HANSDJ * (ROUND_NEW(C.MAOZ, 2) - ROUND_NEW(C.PIZ, 2) -\n" + 
			"\t\t\t\t\t ROUND_NEW(C.ZONGKD, 2))) AS YUNF\n" + 
			"\tFROM DANJCPB DJ, CHEPB C, JIESYFB JY\n" + 
			" WHERE DJ.CHEPB_ID = C.ID\n" + 
			"\t AND DJ.YUNFJSB_ID = JY.ID\n" + 
			"\t AND JY.JIESFAB_ID IN ("+this.getChange()+")\n" + 
			sql_condition +
//			"\t AND JY.DIANCXXB_ID = "+this.getTreeid_dc()+"\n" + 
			" GROUP BY ROLLUP(JY.SHOUKDW, C.CHEPH, JY.MEIKDWMC)) A";

		
		Report rt = new Report();
		rstmp = con.getResultSetList(sql);
		String[][] ArrHeader = new String[1][7];
        ArrHeader[0] = new String[] {"序号", "运输单位", "车牌号", 
        							"煤矿名称", "车数(车)",  "拉运量(吨)", "运费(元)"};
    
    	int ArrWidth[] = new int[] {50, 150, 80, 120, 70, 70, 70};
		rt.setTitle(time + "单车运费结算表", ArrWidth);
//		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		ResultSetList r = con.getResultSetList("SELECT quanc\n" +
				"\tFROM DIANCXXB\n" + 
				" WHERE ID IN (SELECT JIESDW_ID FROM JIESFAB WHERE ID IN ("+this.getChange()+"))");
		StringBuffer diancmc = new StringBuffer();
		if (r.next()) {
			diancmc.append(r.getString("quanc")).append(",");
		}
		r.close();
		
		if(diancmc.length()>0){
	    	
			diancmc.deleteCharAt(diancmc.length()-1);
	    }
		
		rt.setDefaultTitle(1, 2, "起始日期：" + kaisrq , Table.ALIGN_LEFT);
//		rt.setDefaultTitle(5, 3, jiezrq,
//				Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 2, "终止日期："+jiezrq, Table.ALIGN_RIGHT);

		rt.setBody(new Table(rstmp, 1, 0, 1));
		
		for (int i = 1; i <= 4; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		for (int i = 5; i <= 7; i++) {
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "制表："+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, time + "单车运费结算表", "dancyfjsb");
     	return rt.getAllPagesHtml();
	}
	
    // 工具栏使用的方法
	private String treeid;
	
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = "0";
		}
		return treeid;
	}
	
	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	
//	public String getTreeScript() {
//		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
//	}
	
//	-------------------------电厂Tree-----------------------------------------------------------------
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
		
		if(!((Visit) getPage().getVisit()).getString3().equals(treeid)){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setboolean1(false);	//结算方案改变
			visit.setString3("");	//this.getTreeid_dc()
			setTreeid_dc(visit.getDiancxxb_id()+"");
			visit.setboolean1(false);
			
			this.setYunsdwValue(null);
			this.setIYunsdwModel(null);
			
			this.setJiesfaValue(null);	//DropDownBean2
			this.setIJiesfaModel(null);	//ProSelectionModel2
		}
		
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
			this.setYunsdwValue(null);
			this.setIYunsdwModel(null);
		}
		
		initToolbar();
		blnIsBegin = true;
	}
	
	// 按钮的监听事件
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
		}
		initToolbar();
	}
	
	// 页面登陆验证
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
	
//	运输单位
	public IDropDownBean getYunsdwValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean1(new IDropDownBean(-1, ""));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setYunsdwValue(IDropDownBean Value) {
		
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean1()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean1().getId();
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setIYunsdwModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIYunsdwModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			
			getIYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIYunsdwModels() {

			String sql = 
				"SELECT DISTINCT YD.ID, YD.QUANC\n" +
				"\tFROM JIESYFB JY, YUNSDWB YD\n" + 
				" WHERE JY.SHOUKDW = YD.QUANC\n" +
				" ORDER BY YD.QUANC";

			((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	运输单位_end
	
//	结算方案名称
	public IDropDownBean getJiesfaValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean2(new IDropDownBean(-1, ""));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesfaValue(IDropDownBean Value) {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2()!= Value) {
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIJiesfaModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIJiesfaModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIJiesfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesfaModels() {

			String sql = 
				"SELECT ID, BIANM\n" +
				"\tFROM JIESFAB\n" + 
				" WHERE SHIFJS = 1\n" + 
				"\t AND JIESLX = "+Locale.guotyf_feiylbb_id+"\n" + 
//				"\t AND JIESDW_ID = "+this.getTreeid_dc()+"\n" + 
				" ORDER BY BIANM desc";
			
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	结算方案名称_end
}