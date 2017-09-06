package com.zhiren.dc.huaygl.zhiltz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class Zhiljb extends BasePage {

	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	// ***************设置消息框******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// private String leix = "";

	public String getPrintTable() {
		setMsg(null);

		return getZhiljb();

	}

	// 设置制表人默认当前用户
	private String getZhibr() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "否";
		String sql = "select zhi from xitxxb where mingc='月报管理制表人是否默认当前用户' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
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

	// 判断电厂Tree中所选电厂时候还有子电厂
	private boolean hasDianc(String id) {
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}

	private String getZhiljb() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();

		buffer
				.append(
						"select  decode(grouping(zhuanmb.bianm), 1, '总计', zhuanmb.bianm) bianm,\n" +
						"       meikxxb.mingc meikdq,\n" + 
						"\n" + 
						"       pinzb.mingc as pinz,\n" + 
						"       sum(fahb.ches) as ches,\n" + 
						"       sum(round_new(fahb.laimsl, 2)) as jingz,\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new((sum(round_new(zhilb.qnet_ar, 2) *\n" + 
						"                             round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2))) / 4.1816 * 1000,\n" + 
						"                        0)) as farl1,\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(round_new(sum(round_new(zhilb.qnet_ar, 2) *\n" + 
						"                                      round_new(fahb.laimsl, 2)) /\n" + 
						"                                  sum(round_new(fahb.laimsl, 2)),\n" + 
						"                                  2) ,\n" + 
						"                        2)) as farl2,\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(sum(round_new(zhilb.mt, 1) *\n" + 
						"                            round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2)),\n" + 
						"                        1)) as mt,\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(sum(zhilb.mad * round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2)),\n" + 
						"                        2)) as mad,\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(sum(zhilb.aar * round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2)),\n" + 
						"                        2)) as aar,\n" + 
						"\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(sum(round_new(zhilb.qbad, 2) *\n" + 
						"                            round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2)) ,\n" + 
						"                        0)) as qbad,\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(sum(round_new(zhilb.qgrad, 2) *\n" + 
						"                            round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2)) ,\n" + 
						"                        2)) as qgrad,\n" + 
						"\n" + 
						"\n" + 
						"       decode(sum(round_new(fahb.laimsl, 2)),\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              round_new(sum(zhilb.stad * round_new(fahb.laimsl, 2)) /\n" + 
						"                        sum(round_new(fahb.laimsl, 2)),\n" + 
						"                        2)) as stad,\n" + 
						"        to_char(zhilb.huaysj,'yyyy-mm-dd') huaysj   "+
						"  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb,zhillsb,zhuanmb\n" + 
						" where fahb.zhilb_id = zhilb.id\n" + 
						"   and fahb.gongysb_id = gongysb.id\n" + 
						"   and fahb.meikxxb_id = meikxxb.id\n" + 
						"   and fahb.faz_id = chezxxb.id\n" + 
						"   and fahb.pinzb_id = pinzb.id\n" + 
						"   and fahb.diancxxb_id = "+v.getDiancxxb_id()+"\n" + 
						"   and zhillsb.zhilb_id=zhilb.id\n" + 
						"   and zhuanmb.zhillsb_id=zhillsb.id\n" + 
						"   and zhuanmb.zhuanmlb_id=100663\n" + 
						"   and fahb.daohrq>=to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
						"   and fahb.daohrq<=to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" + 
						" group by rollup(zhuanmb.bianm, meikxxb.mingc,pinzb.mingc,fahb.ches,zhilb.huaysj)\n" + 
						"having grouping(zhilb.huaysj) = 0 or grouping(zhuanmb.bianm) = 1\n" + 
						" order by\n" + 
						"          decode(grouping(zhuanmb.bianm), 1, -999, 1),zhuanmb.bianm,"+
						"          decode(grouping(meikxxb.mingc), 1, -999, 1),\n" + 
						"          max(meikxxb.xuh),\n" + 
						"          decode(grouping(pinzb.mingc), 1, -999, 1),\n" + 
						"          max(pinzb.xuh)");

		// System.out.println(buffer.toString());
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


		String ArrHeader[][] = new String[2][14];
		ArrHeader[0] = new String[] { "填表单位", "填表单位","燃料部", "燃料部", "燃料部","燃料部","燃料部","进煤日期","进煤日期", getRiqi()+"---"+getRiq2(), getRiqi()+"---"+getRiq2(),getRiqi()+"---"+getRiq2(),getRiqi()+"---"+getRiq2(),getRiqi()+"---"+getRiq2() };
		ArrHeader[1] = new String[] { "编码","煤矿单位", "品种", "车数", "煤量", "低位CaL/kg",
				"低位 MJ/kg", "全水% Mt", "固水% Mad", "灰分% Aar", "弹筒 MJ/kg",
				"高位 MJ/kg", "硫%  St,ad","化验日期" };

		int[] ArrWidth = new int[14];

		ArrWidth = new int[] { 50,70, 45, 45, 45, 45, 45,45, 45, 45, 45, 45, 45,65 };

		rt.setTitle("入 厂 质 检 简 表", ArrWidth);
		rt.title.setRowHeight(3, 40);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(3, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "到货日期:" + getRiqi() ,
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		//
		// String[] strFormat = new String[] { "", "", "", "", "", "", "",
		// "0.0",
		// "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
		// "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		
		
		// rt.body.mergeFixedRow();
		// rt.body.setColFormat(strFormat);
		// 合并单元格
		rt.body.mergeCell(1, 1, 1, 2);
		rt.body.mergeCell(1, 3, 1, 7);
		rt.body.mergeCell(1, 8, 1, 9);
		rt.body.mergeCell(1, 10, 1, 14);
		for (int i = 1; i <= 14; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
			//rt.body.setColCells(i, Table.PER_FONTSIZE, 11);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(1, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 4, "制表:" + getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}


	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
            setRiqi(null);
            setRiq2(null);
			getSelectData();
		}

		getSelectData();

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
	
	private String riq;
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riq;
	}
	public void setRiq(String riq) {
		
		if(this.riq !=null &&!this.riq.equals(riq)){
			this.riq = riq;
			riqichange=true;
		}
		 
	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}



	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {

			Refurbish();
			_RefurbishClick = false;
		}

	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getZhiljb();
	}

	// -------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="
				+ visit.getDiancxxb_id() + " \n";
		sql += " union \n";
		sql += "  select d.id,d.mingc from diancxxb d where d.fuid="
				+ visit.getDiancxxb_id() + " \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

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

	// -------------------------电厂Tree END----------

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();

		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
				getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid_dc() == null
						|| "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		 tb1.addText(new ToolbarText("至:"));
		 DateField df1 = new DateField();
		 df1.setReadOnly(true);
		 df1.setValue(this.getRiq2());
		 df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
		 df1.setId("riq2");
		 tb1.addField(df1);
		 tb1.addText(new ToolbarText("-"));

		// tb1.addText(new ToolbarText("煤矿:"));
		// ComboBox meik = new ComboBox();
		// meik.setTransform("MeikSelect");
		// meik.setWidth(80);
		// meik.setListeners("select:function(own,rec,index){Ext.getDom('MeikSelect').selectedIndex=index}");
		// tb1.addField(meik);
		// tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

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
	
	public String Caiyrq(){
		JDBCcon con = new JDBCcon();
		String caiyrq="";
		String sql=
			"select to_char(z.huaysj,'yyyy-mm-dd') huaysj from zhilb z ,fahb f where\n" +
			"z.id=f.zhilb_id and f.daohrq=to_date("+getRiqi()+",'yyyy-mm-dd')";

		ResultSet rs=con.getResultSet(sql);
		try {
			caiyrq=rs.getString("caiyrq");
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} 
		
		
		return caiyrq;
	} 

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
}