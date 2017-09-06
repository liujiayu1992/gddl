package com.zhiren.shanxdted.huayxgrzcx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Huayxgrzcx extends BasePage implements PageValidateListener {

	protected void initialize() {
		super.initialize();
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

	// 绑定日期
	public String getEndRiq() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setEndRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq1);
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

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	private Connection getSQLServerCon () {
		Connection cnn = null;
		String SQLServer_IP = "192.168.17.11";
		String SQLSever_DB = "nyh";
		String DBDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String ConnStr = "jdbc:microsoft:sqlserver://" + SQLServer_IP + ":1433;DatabaseName=" + SQLSever_DB;
		String UserName = "sa";
		String UserPassword = "";
		try {
			Class.forName(DBDriver);
			cnn = DriverManager.getConnection(ConnStr,
					UserName, UserPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnn;
	}
	
	public synchronized ResultSet getResultSet(String sql) {
		Connection con = getSQLServerCon();
		ResultSet rs = null;
		try {
			con.setAutoCommit(true);
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(sql);
			if (rs == null) {
				con.rollback();
			}
			con.close();
		} catch (Exception exp) {
			System.out.println("SQL:\n");
			System.out.println(sql);
			exp.printStackTrace();
			return null;
		} 		
		return rs;
	}
	
	public String getPrintTable() {

		JDBCcon con = new JDBCcon();
		Report rt=new Report();
		String sql = 
			"SELECT decode(f.diancxxb_id,301,'一期',302,'二期') diancxxbId,to_char(f.daohrq,'yyyy-mm-dd') 日期 ,\n" +
			"(SELECT mingc FROM meikxxb WHERE id=f.meikxxb_id) AS 煤矿,\n" +
			"nvl((SELECT mingc FROM yunsdwb WHERE ID\n" + 
			" =(SELECT MAX(chepb.yunsdwb_id) FROM chepb WHERE fahb_id=f.id)),'火车') 运输单位 ,\n" + 
			"z.qnet_ar AS 修改后热值,z.mt 修改后全水 ,z.mad 修改后水,z.stad 修改后硫,z.aad 修改后灰,z.vad 修改后挥发份,\n" + 
			"zl.qnet_ar AS 修改前热值 ,zl.mt 修改前全水,zl.mad 修改前水,zl.stad 修改前硫,z.aad 修改后灰,z.vad 修改前挥发份\n" + 
			"FROM zhillsb   z,\n" + 
			"zhillsb_log zl,fahb  f\n" + 
			"WHERE z.id=zl.zhillsb_id\n" + 
			"AND f.daohrq BETWEEN to_date('" + this.getRiq() + "','yyyy-mm-dd') \n" + 
			"AND  to_date('" + this.getEndRiq() + "','yyyy-mm-dd')\n" + 
			"AND f.zhilb_id=z.zhilb_id\n" + 
			"--AND z.qnet_ar<zl.qnet_ar\n" + 
			"ORDER BY diancxxbId desc, 日期";	
			
		ResultSet rs=con.getResultSet(sql);
		String ArrHeader[][]=new String[2][16];
		ArrHeader[0] = new String[] {"厂别","到货日期","煤矿单位","运输单位","修改后","修改后",
								"修改后","修改后","修改后","修改后",
								"修改前","修改前","修改前","修改前","修改前","修改前"};
		ArrHeader[1] = new String[] {"厂别","到货日期","煤矿单位","运输单位","热值Qnetar","全水Mt",
				"水Mad","硫Stad","灰Aad","挥发份Vad",
				"热值Qnetar","全水Mt","水Mad","硫Stad","灰Aad","挥发份Vad"};
		int ArrWidth[]=new int[] {80,80,120,120,60,60,60,60,60,60,60,60,60,60,60,60};
		rt.setTitle("化验修改查询", ArrWidth);
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);

//		for (int i=2;i<=6;i++) rt.body.setColFormat(i, "0.00");
//		
		rt.body.setPageRows(10000);
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1, 3, "打印日期："
//				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
//				Table.ALIGN_RIGHT);
//		rt.setDefautlFooter(8, 2, "审核：", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(10, 2, "制表：", Table.ALIGN_CENTER);
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
//			getSelectData();
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
			visit.setString6(null);
		}
		getSelectData();
	}
	
	public void getSelectData() {
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField dfend = new DateField();
		dfend.setValue(getEndRiq());
		dfend.Binding("EndRIQ", "Forms[0]");// 与html页中的id绑定,并自动刷新
		dfend.setId("guohrq");
		tb1.addField(dfend);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}	
}
