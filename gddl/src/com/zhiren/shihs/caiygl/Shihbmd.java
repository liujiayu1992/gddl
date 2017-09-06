package com.zhiren.shihs.caiygl;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shihbmd extends BasePage {

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
	
	public boolean getRaw() {
		return true;
	}
	
	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
		}
		
		getSelectData();

	}
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
			
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}
	
	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		String sql =
			"SELECT GONGHDW,PINZ,GETSHIHHYCPH(ID),BIANM\n" +
			"FROM (\n" + 
			"SELECT DISTINCT SG.MINGC AS GONGHDW, SPZ.MINGC AS PINZ, CY.BIANM,cy.id\n" + 
			"  FROM SHIHCPB CP, SHIHCYB CY, SHIHGYSB SG, SHIHPZB SPZ\n" + 
			" WHERE CY.CAIYSJ = TO_DATE('" +  getRiqi() + "', 'yyyy-mm-dd')\n" + 
			"   AND CP.SHIHCYB_ID = CY.ID\n" + 
			"   AND CP.GONGYSB_ID = SG.ID\n" + 
			"   AND CP.SHIHPZB_ID = SPZ.ID\n" + 
			" ) a";
		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		String ArrHeader[][] = new String[1][4];
		ArrHeader[0] = new String[] { "供货单位", "品种", "车号", "采样编码" };

		int ArrWidth[] = new int[] { 150, 60, 100, 100 };

		rt.setTitle("石灰石采样编码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		alignCheph(rt.body);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private void alignCheph(Table table) {
		String args[] = null;
		
		for (int i=1; i<=table.getRows(); i++) {
			args = table.getCell(i, 3).value.split(",");
			if (args.length>0) {
				String cheph ="";
				for (int j=0;j<args.length;j++) cheph += args[j] + "<br>";
				cheph = cheph.substring(0, cheph.lastIndexOf("<br>"));
				table.getCell(i, 3).value = cheph;
			}
		}
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

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
		}

	}
}
