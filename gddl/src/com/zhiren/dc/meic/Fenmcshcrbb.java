package com.zhiren.dc.meic;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Fenmcshcrbb extends BasePage {
	
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
	
	// 绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
	// 绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
	
	// 获得选择的树节点的对应的电厂名称
	private String getDcMingc(String id) {
		if (id == null || "".equals(id)) {
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
	
	// 刷新衡单列表
	public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		// 电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(80);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null, "function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
	}
	
	private String getSelectData(){
		JDBCcon con = new JDBCcon();
		String sql = 
			"SELECT DECODE(GROUPING(DC.MINGC) + GROUPING(MC.MINGC) + GROUPING(HC.RIQ), 3, '合计', DC.MINGC) DCMC,\n" +
			"       DECODE(GROUPING(MC.MINGC) + GROUPING(HC.RIQ), 2, '合计', MC.MINGC) MCMC,\n" + 
			"       DECODE(GROUPING(HC.RIQ), 1, '合计', TO_CHAR(HC.RIQ, 'yyyy-mm-dd')) RIQ,\n" + 
			"       SUM(HC.LAIMSL) LAIMSL,\n" + 
			"       SUM(HC.HAOMSL) HAOMSL,\n" + 
			"       SUM(HC.KUC) KUC\n" + 
			"  FROM FENMCSHCRBB HC, MEICBEXT MC, DIANCXXB DC\n" + 
			" WHERE HC.MEICBEXT_ID = MC.ID\n" + 
			"   AND MC.DIANCXXB_ID = DC.ID\n" + 
			"   AND HC.RIQ >= TO_DATE('" + this.getBRiq() + "', 'yyyy-mm-dd')\n" + 
			"   AND HC.RIQ <= TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" + 
			"	AND (DC.ID = " + this.getTreeid_dc() + " OR DC.FUID = " + this.getTreeid_dc() + ")" + 
			" GROUP BY ROLLUP(DC.MINGC, MC.MINGC, HC.RIQ)\n" + 
			" ORDER BY DC.MINGC, MC.MINGC, HC.RIQ";
		
		Report rt = new Report();
		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		int aw = 0;
		
        ArrHeader = new String[][] {{"厂别", "煤场", "日期", "来煤量", "耗煤量", "库存"}};
        ArrWidth = new int[] {100, 120, 100, 100, 100, 100};
    	aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
    	rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
    	rt.setTitle("各煤场收耗存", ArrWidth);
    	
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 1, 0, 3));
		for (int i = 1; i <= 6; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		if (rt.body.getRows() > 1) {
			int kuchj = 0;
			int kuchj1 = 0;
			for (int i = 1; i <= rt.body.getRows(); i++) {
				if (rt.body.getCellValue(i, 3).equals("合计") && !rt.body.getCellValue(i, 2).equals("合计")) {
					rt.body.setCellValue(i, 6, rt.body.getCellValue(i - 1, 6));
					kuchj += Double.parseDouble(rt.body.getCellValue(i - 1, 6));
				}
				
				if (rt.body.getCellValue(i, 3).equals("合计") && rt.body.getCellValue(i, 2).equals("合计") 
						&& !rt.body.getCellValue(i, 1).equals("合计")) {
					rt.body.setCellValue(i, 6, kuchj + "");
					kuchj1 += Double.parseDouble(rt.body.getCellValue(i, 6));
					kuchj = 0;
					rt.body.merge(i, 2, i, 3);
				}
			}
			rt.body.setCellValue(rt.body.getRows(), 6, kuchj1 + "");
		}
				
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(28);
		
		rt.body.mergeFixedRowCol();
		rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 3);
		
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(2, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();
	}
	
	public String getPrintTable() {
		return getSelectData();
	}
	
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
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	DefaultTree dc;

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
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			// begin方法里进行初始化设置
			visit.setString1(null);
			
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {
				visit.setString1(pagewith);
			}
			// visit.setString1(null);保存传递的非默认纸张的样式
			setTreeid_dc(visit.getDiancxxb_id() + "");
			// id=getTreeid();
			initToolbar();
		}
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
}