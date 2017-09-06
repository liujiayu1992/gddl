package com.zhiren.dc.jilgl.gongl.sanfsllr;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class SanfsllrReport extends BasePage {
	
//	private String msg = "";
//
//	public String getMsg() {
//		return msg;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = MainGlobal.getExtMessageBox(msg, false);
//	}

	protected void initialize() {
		super.initialize();
//		setMsg("");
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
	
	// 绑定日期
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
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
	
	public void SetOtherScript( String otherScript) {
		((Visit) getPage().getVisit()).setString3(otherScript);
	}
	
	public String getOtherScript() {
		if(((Visit) getPage().getVisit()).getString3()==null){
			
			((Visit) getPage().getVisit()).setString3("");
		}
		return ((Visit) getPage().getVisit()).getString3();
	}
	
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2()==null
				||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
		}
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {		
		return getTree().getWindowTreeScript();
	}
	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = visit.getDiancxxb_id() + "";
		String riqTiaoj = this.getRiq();
		String meikmcWhere = "";
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			meikmcWhere = "AND MEIKXXB_ID = " + getTreeid();
		}
		String sql = 
			"SELECT \n" +
			"       DECODE(GROUPING(CHEPH), 1, '合计', CHEPH) AS CHEPH,\n" + 
			"       SUM(MAOZ) AS MAOZ,\n" + 
			"       SUM(PIZ) AS PIZ,\n" + 
			"       SUM(BIAOZ) AS BIAOZ,\n" + 
			"       SUM(SANFSL) AS SANFSL,\n" + 
			"       SUM(CHAE) AS CHAE,\n" + 
			"       MEIKMC,\n" + 
			"       PINZ,\n" + 
			"       YUNSDW,\n" + 
			"DECODE(GROUPING(ID), 1, '', MAX(ZHONGCSJ)) AS ZHONGCSJ,\n" +
			"DECODE(GROUPING(ID), 1, '', MAX(QINGCSJ)) AS QINGCSJ\n" +
			"  FROM (SELECT ID,\n" + 
			"               CHEPH,\n" + 
			"               MAOZ,\n" + 
			"               PIZ,\n" + 
			"               BIAOZ,\n" + 
			"               SANFSL,\n" + 
			"               (BIAOZ - SANFSL) AS CHAE,\n" + 
			"               (SELECT NVL(MINGC, '')\n" + 
			"                  FROM MEIKXXB\n" + 
			"                 WHERE ID = (SELECT MEIKXXB_ID FROM FAHB WHERE ID = FAHB_ID)) AS MEIKMC,\n" + 
			"               (SELECT NVL(MINGC, '')\n" + 
			"                  FROM PINZB\n" + 
			"                 WHERE ID = (SELECT PINZB_ID FROM FAHB WHERE ID = FAHB_ID)) AS PINZ,\n" + 
			"               (SELECT NVL(MINGC, '') FROM YUNSDWB WHERE ID = YUNSDWB_ID) AS YUNSDW,\n" + 
			"               TO_CHAR(ZHONGCSJ, 'yyyy-mm-dd hh24:mi:ss') AS ZHONGCSJ,\n" + 
			"               TO_CHAR(QINGCSJ, 'yyyy-mm-dd hh24:mi:ss') AS QINGCSJ\n" + 
			"          FROM CHEPB\n" + 
			"         WHERE FAHB_ID IN\n" + 
			"               (SELECT ID\n" + 
			"                  FROM FAHB\n" + 
			"                 WHERE DAOHRQ = TO_DATE('" + riqTiaoj + " ', 'yyyy-mm-dd')\n" + 
			"                   AND DIANCXXB_ID = " + diancxxb_id + "\n" +
			"                   " + meikmcWhere + ")) A\n" + 
			" GROUP BY ROLLUP(ID, MEIKMC, PINZ, YUNSDW, CHEPH)\n" + 
			"HAVING GROUPING(ID) = 1 OR GROUPING(MEIKMC) + GROUPING(CHEPH) = 0\n" + 
			" ORDER BY GROUPING(A.CHEPH), A.CHEPH";
		
		ResultSet rs=con.getResultSet(sql);
		Report rt=new Report();
		String ArrHeader[][]=new String[1][11];
		ArrHeader[0] = new String[] {"车号","毛重","皮重","净重","三方量","差额",
								"供货单位","品种","运输单位","重车时间","轻车时间"};
		int ArrWidth[]=new int[] {59,50,50,50,50,50,100,50,80,120,120};
		rt.setTitle("三方数量查询", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		for (int i=2;i<=6;i++) rt.body.setColFormat(i, "0.00");
		
		rt.body.setPageRows(40);
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_RIGHT);
		rt.setDefautlFooter(8, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "制表：", Table.ALIGN_CENTER);
		return rt.getAllPagesHtml();
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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
			getSelectData();
		}
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString2(null);
			visit.setString3(null);
			visit.setString5(null);
		}
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		String condition=" and fh.daohrq = to_date('"+ riqTiaoj +"','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		this.SetOtherScript("gongysTree_text.setValue(" 
				+ "gongysTree_treePanel.getSelectionModel().getSelectedNode()==null?'':"
				+ "gongysTree_treePanel.getSelectionModel().getSelectedNode().text);");

		ToolbarButton tb2 = new ToolbarButton(null, null, "function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
			
		tb1.addText(new ToolbarText("供货单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}
}
