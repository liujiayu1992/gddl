package com.zhiren.shanxdted.meigzxbb;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yunsfxbb extends BasePage {

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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
		return getKuangcrzc();
	}

	private String getKuangcrzc(){
		JDBCcon con = new JDBCcon();
		StringBuffer SQL = new StringBuffer();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
        SQL.append("SELECT DECODE(GROUPING(SL.YUNSL) + GROUPING(MEIKDW), 2, '合计', TO_CHAR(SL.YUNSL,'0.00')) AS YUNSL,\n");
        SQL.append("       SL.MEIKDW,\n");
        SQL.append("       SL.YUNSDW,\n"); 
        SQL.append("       SUM(SL.BIAOZ) AS BIAOZ,\n"); 
        SQL.append("       SUM(SL.JINGZ) AS JINGZ,\n" ); 
        SQL.append("       SUM(SL.HELYS) AS HELYS,\n");
        SQL.append("       SUM(SL.SHIJYS) AS SHIJYS,\n"); 
        SQL.append("       ROUND_NEW(DECODE(SUM(SL.BIAOZ),\n"); 
        SQL.append("              0,\n" ); 
        SQL.append("              0,\n" );
        SQL.append("              (SUM(SL.BIAOZ) - SUM(SL.JINGZ)) / SUM(SL.BIAOZ) * 100),2) AS BANGC,\n");
        SQL.append("       ROUND_NEW(SUM(SL.HELYS - SL.SHIJYS),2) AS SUNC\n");
        SQL.append("  FROM (SELECT FH.ID,\n" );
        SQL.append("               M.YUNSL,\n" );
        SQL.append("               M.MINGC MEIKDW,\n"); 
        SQL.append("               NVL(Y.MINGC, '无') AS YUNSDW,\n" );
        SQL.append("               SUM(C.BIAOZ) AS BIAOZ,\n");
        SQL.append("               SUM((C.MAOZ - C.PIZ - C.KOUD)) AS JINGZ,\n"); 
        SQL.append("               SUM((C.BIAOZ * M.YUNSL)) AS HELYS,\n");
        SQL.append("               SUM(C.BIAOZ - (C.MAOZ - C.PIZ - C.KOUD)) AS SHIJYS\n");
        SQL.append("          FROM FAHB FH, CHEPB C, MEIKXXB M, YUNSDWB Y\n");
        SQL.append("         WHERE FH.ID = C.FAHB_ID\n"); 
        SQL.append("           AND C.YUNSDWB_ID = Y.ID(+)\n"); 
        SQL.append("           AND FH.MEIKXXB_ID = M.ID\n"); 
        SQL.append("           AND FH.DAOHRQ >= TO_DATE('" + beginRiq + "', 'yyyy-mm-dd')\n"); 
        SQL.append("           AND FH.DAOHRQ <= TO_DATE('" + endRiq + "', 'yyyy-mm-dd')\n"); 
        SQL.append("           AND FH.DIANCXXB_ID IN (" + getTreeid() + ")\n"); 
        SQL.append("         GROUP BY M.YUNSL, FH.ID, M.MINGC, Y.MINGC) SL\n" );
        SQL.append(" GROUP BY GROUPING SETS ((),(SL.YUNSL, SL.MEIKDW, SL.YUNSDW))    \n");
        SQL.append(" ORDER BY SL.YUNSL,SL.MEIKDW");      
		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();
		String[][] ArrHeader = null;
		ArrHeader=new String[][] {{"运损","煤矿名称","车队","矿发煤量","入厂煤量","合理运损","实际运损","磅差(%)","损差(吨)"}};
		int ArrWidth[]=new int[] {60,220,220,80,80,60,60,60,60};
		rt.setTitle(beginRiq+"至"+endRiq+"直购承运车队运损分析表", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

//		rt.body.ShowZero=true;
		rt.body.mergeFixedRow();	
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
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

	private boolean beginChange = false;

	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		if (value != null) {
			if (!value.equals(((Visit) getPage().getVisit()).getString4())) {
				beginChange = true;
			} else {
				beginChange = false;
			}
		}
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		if(beginChange){
			((Visit) getPage().getVisit()).setString5(getBeginriqDate());
		}else{
			((Visit) getPage().getVisit()).setString5(value);
		}
		
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		 df.Binding("daohrq1","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		if(beginChange){
			df1.setValue(this.getBeginriqDate());
		}else{
			df1.setValue(this.getEndriqDate());
		}
		df1.Binding("daohrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(), null, true);

		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str = getTreeid().split(",");
		if (str.length > 1) {
			tf.setValue("组合电厂");
		} else {
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
					.parseLong(str[0])));
		}

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setMsg(null);
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setExtTree1(null);
			visit.setString4(null);
			visit.setString5(null);
			setTreeid(null);
		}
		getToolbars();
		blnIsBegin = true;

	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
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

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	// 得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancId;
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

	public String getDiancmc() {
		String[] str = getTreeid().split(",");
		if (str.length > 1) {
			return "组合电厂";
		} else {
			return getDiancmc(str[0]);
		}
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript() + getOtherScript("diancTree");
	}

	// 增加电厂多选树的级联
	public String getOtherScript(String treeid) {
		String str = " var "
				+ treeid
				+ "_history=\"\";\n"
				+ treeid
				+ "_treePanel.on(\"checkchange\",function(node,checked){\n"
				+ "    if(checked){\n"
				+ "      addNode(node);\n"
				+ "    }else{\n"
				+ "      subNode(node);\n"
				+ "    }\n"
				+ "    node.expand();\n"
				+ "    node.attributes.checked = checked;\n"
				+ "    node.eachChild(function(child) {\n"
				+ "      if(child.attributes.checked != checked){\n"
				+ "        if(checked){\n"
				+ "          addNode(child);\n"
				+ "        }else{\n"
				+ "          subNode(child);\n"
				+ "        }\n"
				+ "        child.ui.toggleCheck(checked);\n"
				+ "              child.attributes.checked = checked;\n"
				+ "              child.fireEvent('checkchange', child, checked);\n"
				+ "      }\n" + "    });\n" + "  }," + treeid
				+ "_treePanel);\n" + "  function addNode(node){\n"
				+ "    var history = '+,'+node.id+\";\";\n"
				+ "    writesrcipt(node,history);\n"
				+ "  }\n"
				+ "\n"
				+ "  function subNode(node){\n"
				+ "    var history = '-,'+node.id+\";\";\n"
				+ "    writesrcipt(node,history);\n"
				+ "  }\n"
				+ "function writesrcipt(node,history){\n"
				+ "\t\tif("
				+ treeid
				+ "_history==\"\"){\n"
				+ "\t\t\t"
				+ treeid
				+ "_history = history;\n"
				+
				"\t\t}else{\n" + "\t\t\tvar his = " + treeid
				+ "_history.split(\";\");\n" + "\t\t\tvar reset = false;\n"
				+ "\t\t\tfor(i=0;i<his.length;i++){\n"
				+ "\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n"
				+ "\t\t\t\t\this[i] = \"\";\n" + "\t\t\t\t\treset = true;\n"
				+ "\t\t\t\t\tbreak;\n" + "\t\t\t\t}\n" + "\t\t\t}\n"
				+ "\t\tif(reset){\n" + "\t\t\t  " + treeid
				+ "_history = his.join(\";\");\n"
				+
				"    }else{\n" + "      	 " + treeid + "_history += history;\n"
				+
				"    }\n" + "  }\n" + "\n" + "}";
		return str;
	}
}