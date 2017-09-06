package com.zhiren.jt.jiesgl.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：赵胜男
 * 时间：2013-03-27
 * 适用范围：国电电力
 * 描述：调整页面格式
 */
/*
 * 作者：夏峥
 * 时间：2013-03-27
 * 适用范围：国电电力
 * 描述：调整界面查询结果
 */
public class Jiestz_GD extends BasePage {
	
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		setMsg(null);
		return getShouhc();
	}
	
	private String getShouhc() {
		JDBCcon con = new JDBCcon();
//		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
//		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();

		String sql="SELECT  \n" +
		"        SR.DANW,\n" + 
		"        SR.YUNSFS,\n" + 
		"        SR.GONGYSMC,\n" + 
		"        SR.SHOUKDW,\n" + 
		"        SR.GONGMRQ,\n" + 
		"        SR.JIESRQ,\n" + 
		"        SR.JIESSL,\n" + 
		"        SR.JIESRL,\n" + 
		"        SR.MEIJ+SR.YUNJ DAOCJ,\n" + 
		"        SR.MEIJ,\n" + 
		"        SR.YUNJ,\n" + 
		"        SR.HETBZ,\n" + 
		"        SR.CHANGF,\n" + 
		"        ROUND(DECODE(SR.CHANGF,0,0,(SR.MEIJ+SR.YUNJ)*7000/SR.CHANGF),2) HANSBMDJ,\n" + 
		"        ROUND(DECODE(SR.CHANGF,0,0,(SR.MEIJ/1.17+SR.YUNJ*0.93)*7000/SR.CHANGF),2) BUHSBMDJ\n" + 
		"FROM (" +
		"SELECT * FROM\n" +
		"(SELECT DECODE(GROUPING(DC.MINGC), 1, '总计', DC.MINGC) DANW,\n" + 
		"       DECODE(GROUPING(DC.MINGC) + GROUPING(JS.YUNSFS),1,JS.YUNSFS||'小计',JS.YUNSFS) YUNSFS,\n" + 
		"       JS.GONGYSMC,\n" + 
		"       JS.SHOUKDW,\n" + 
		"       JS.GONGMRQ,\n" + 
		"       JS.JIESRQ,\n" + 
		"       SUM(JS.JIESSL) JIESSL,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * JS.JIESRL) / SUM(JS.JIESSL)),0) JIESRL,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * JS.MEIJ) / SUM(JS.JIESSL)),2) MEIJ,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * JS.YUNJ) / SUM(JS.JIESSL)),2) YUNJ,\n" + 
		"       PC.HETBZ,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * NVL(PC.CHANGF,0)) / SUM(JS.JIESSL)),0) CHANGF\n" + 
		"  FROM (SELECT JS.ID,\n" + 
		"               JS.DIANCXXB_ID,\n" + 
		"               YS.MINGC YUNSFS,\n" + 
		"               JS.GONGYSMC,\n" + 
		"               JS.SHOUKDW,\n" + 
		"               TO_CHAR(JS.FAHKSRQ, 'yyyy-mm-dd') || '至' ||\n" + 
		"               TO_CHAR(JS.FAHJZRQ, 'yyyy-mm-dd') GONGMRQ,\n" + 
		"               TO_CHAR(JS.JIESRQ, 'yyyy-mm-dd')JIESRQ,\n" + 
		"               JS.JIESSL,\n" + 
		"               JS.JIESRL,\n" + 
		"               JS.HANSDJ MEIJ,\n" + 
		"               NVL(YF.HANSDJ, 0) YUNJ\n" + 
		"          FROM JIESB JS, JIESYFB YF, YUNSFSB YS\n" + 
		"         WHERE JS.BIANM = YF.BIANM(+)\n" + 
		"           AND JS.YUNSFSB_ID = YS.ID\n" + 
		"           AND JS.JIESRQ BETWEEN DATE '"+beginRiq+"' AND DATE'"+endRiq+"') JS,\n" + 
		"       DANPCJSMXB PC,\n" + 
		"       DIANCXXB DC\n" + 
		" WHERE JS.ID = PC.JIESDID(+)\n" + 
		"   AND JS.DIANCXXB_ID = DC.ID\n" + 
		"   AND PC.ZHIBB_ID = 2\n" + 
		"   AND DC.ID IN ("+getTreeid()+")\n" + 
		" GROUP BY ROLLUP(JS.YUNSFS,(DC.MINGC,DC.XUH,JS.GONGYSMC,JS.SHOUKDW, JS.GONGMRQ, JS.JIESRQ,  PC.HETBZ))\n" + 
		" HAVING (GROUPING(DC.MINGC)=1)\n" + 
		" ORDER BY GROUPING(JS.YUNSFS)DESC,JS.YUNSFS)HZ\n" + 
		" UNION ALL\n" + 
		" SELECT * FROM\n" + 
		"(SELECT DC.MINGC DANW,\n" + 
		"       DECODE(GROUPING(JS.YUNSFS)+GROUPING(JS.GONGYSMC),2,'单位小计',1,JS.YUNSFS||'小计',JS.YUNSFS) YUNSFS,\n" + 
		"       JS.GONGYSMC,\n" + 
		"       JS.SHOUKDW,\n" + 
		"       JS.GONGMRQ,\n" + 
		"       JS.JIESRQ,\n" + 
		"       SUM(JS.JIESSL) JIESSL,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * JS.JIESRL) / SUM(JS.JIESSL)),0) JIESRL,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * JS.MEIJ) / SUM(JS.JIESSL)),2) MEIJ,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * JS.YUNJ) / SUM(JS.JIESSL)),2) YUNJ,\n" + 
		"       PC.HETBZ,\n" + 
		"       ROUND(DECODE(SUM(JS.JIESSL),0,0,SUM(JS.JIESSL * NVL(PC.CHANGF,0)) / SUM(JS.JIESSL)),0) CHANGF\n" + 
		"  FROM (SELECT JS.ID,\n" + 
		"               JS.DIANCXXB_ID,\n" + 
		"               YS.MINGC YUNSFS,\n" + 
		"               JS.GONGYSMC,\n" + 
		"               JS.SHOUKDW,\n" + 
		"               TO_CHAR(JS.FAHKSRQ, 'yyyy-mm-dd') || '至' ||\n" + 
		"               TO_CHAR(JS.FAHJZRQ, 'yyyy-mm-dd') GONGMRQ,\n" + 
		"               TO_CHAR(JS.JIESRQ, 'yyyy-mm-dd')JIESRQ,\n" + 
		"               JS.JIESSL,\n" + 
		"               JS.JIESRL,\n" + 
		"               JS.HANSDJ MEIJ,\n" + 
		"               NVL(YF.HANSDJ, 0) YUNJ\n" + 
		"          FROM JIESB JS, JIESYFB YF, YUNSFSB YS\n" + 
		"         WHERE JS.BIANM = YF.BIANM(+)\n" + 
		"           AND JS.YUNSFSB_ID = YS.ID\n" + 
		"           AND JS.JIESRQ BETWEEN DATE '"+beginRiq+"' AND DATE'"+endRiq+"') JS,\n" + 
		"       DANPCJSMXB PC,\n" + 
		"       DIANCXXB DC\n" + 
		" WHERE JS.ID = PC.JIESDID(+)\n" + 
		"   AND JS.DIANCXXB_ID = DC.ID\n" + 
		"   AND PC.ZHIBB_ID = 2\n" + 
		"   AND DC.ID IN ("+getTreeid()+")\n" + 
		" GROUP BY ROLLUP((DC.MINGC,DC.XUH), JS.YUNSFS,(JS.GONGYSMC,JS.SHOUKDW, JS.GONGMRQ, JS.JIESRQ,  PC.HETBZ))\n" + 
		" HAVING(GROUPING(DC.XUH)=0)\n" + 
		" ORDER BY GROUPING(DC.XUH) DESC,DC.XUH,GROUPING(JS.YUNSFS) DESC,JS.YUNSFS,grouping(JS.GONGYSMC)desc,JS.GONGYSMC,JS.SHOUKDW,JS.GONGMRQ,JS.JIESRQ)MX\n" + 
		")SR";
		
		//System.out.println(sbsql);
		ResultSetList rs = con.getResultSetList(sql);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][15];
		ArrHeader[0]=new String[] {"收货单位","运输方式","供煤单位","结算单位","供煤时间","结算时间","结算量","结算热值","到厂价<br>(含税)","其中：(含税)","其中：(含税)","热值","热值","入厂标煤单价","入厂标煤单价"};
		ArrHeader[1]=new String[] {"收货单位","运输方式","供煤单位","结算单位","供煤时间","结算时间","结算量","结算热值","到厂价<br>(含税)","煤价","运价","合同","到厂","含税","不含税"};
		int ArrWidth[]=new int[] {100,60,200,200,150,80,80,60,60,60,60,60,60,65,65};

		rt.setBody(new Table(rs, 2, 0, 0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle("国电电力燃料结算台帐",ArrWidth);
		rt.setDefaultTitle(1, 2, "填报单位:", Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, "日期："+beginRiq+"至"+endRiq, Table.ALIGN_CENTER);
		rt.setDefaultTitle(13, 3, "单位:吨、元/吨、Kcal/Kg", Table.ALIGN_RIGHT);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
//		合并单元格
		rt.body.mergeCol(1);
		rt.body.mergeCol(2);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		
		//rt.body.setPageRows(21);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
//		if(value!=null){
//			if(!value.equals(((Visit) getPage().getVisit()).getString4())){
//				beginChange=true;
//			}else{
//				beginChange=false;
//			}
//		}
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
//		if(beginChange){
//			((Visit) getPage().getVisit()).setString5(getBeginriqDate());
//		}else{
//			((Visit) getPage().getVisit()).setString5(value);
//		}
		((Visit) getPage().getVisit()).setString5(value);
		
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
//		if(beginChange){
//			df1.setValue(this.getBeginriqDate());
//		}else{
//			df1.setValue(this.getEndriqDate());
//		}
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("组合电厂");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setExtTree1(null);
			visit.setString4(null);
			visit.setString5(null);
			setTreeid(null);
			initDiancTree();
		}
		getToolbars();
		blnIsBegin = true;

	}

	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		/*if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}*/
		this.treeid = treeid;
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
//	电厂名称
//	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

//	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
//		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
//			_DiancmcChange = false;
//		}else{
//			_DiancmcChange = true;
//		}
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
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
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

//	 得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="+ diancId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;

	}
	public String getDiancmc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "组合电厂";
		}else{
			return getDiancmc(str[0]);
		}
	}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}

//	增加电厂多选树的级联
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
//	初始化多选电厂树中的默认值
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
}