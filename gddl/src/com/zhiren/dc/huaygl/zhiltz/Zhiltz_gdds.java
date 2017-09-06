package com.zhiren.dc.huaygl.zhiltz;
/*
 * 作者：赵胜男
 * 时间：2013-08-13
 * 描述：报表中表头新增英文简写标志，将收到基全硫改用空干基全硫（st,ad）并新增一列，干燥基全硫(St,d)
 */
/*
 * 作者：夏峥
 * 时间：2013-08-22
 * 描述：处理系统BUG。
 * 		调整数据求和方式。
 */
/*
 * 作者：王耀霆
 * 时间：2014-01-03
 * 描述：增加热值、硫分下拉框选项，根据下拉框的不同，自动判断上下限类型
 */

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
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

public class Zhiltz_gdds extends BasePage {
	
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


	private String Shangx;

	public String getShangx() {
		return Shangx;
	}

	public void setShangx(String shangx) {
		Shangx = shangx;
	}
	
	private String Xiax;

	public String getXiax() {
		return Xiax;
	}

	public void setXiax(String xiax) {
		Xiax = xiax;
	}
	
	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	private boolean blnIsBegin = false;


	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;

		return getZhiltz();
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

	// 全部编码
	/*
	 * 将检质数量由jingz改为发货表laimsl字段 并对弹筒热值、发热量（MJ）、发热量（Kcal）进行修约 修改时间：2008-12-04
	 * 修改人：王磊
	 */
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String meik = "";
		String pinz = "";
//		String yunsfs = "";
		String s = "";
		String rezqj = "";
		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// 增加
																		// 厂别处理条件;
		}

		if(getMeikxxValue().getValue().equals("全部")){
			meik = "";
		}else{
			meik = " and f.meikxxb_id = "+ getMeikxxValue().getId();
		}
		if(getPinzValue().getValue().equals("全部")){
			pinz = "";
		}else{
			pinz = " and f.pinzb_id = "+ getPinzValue().getId();
		}
//		if(getYunsfsValue().getValue().equals("全部")){
//			yunsfs = "";
//		}else{
//			yunsfs = " and f.yunsfsb_id = "+ getYunsfsValue().getId();
//		}
		if(getRezValue().getId()==1){
			if(!Shangx.equals("")){
				if(!Xiax.equals("")){
					rezqj = " AND round_new(ZL.QNET_AR/0.0041816,0)>="+Xiax +" AND round_new(ZL.QNET_AR/0.0041816,0)<="+Shangx;
				}else{
					rezqj = " AND round_new(ZL.QNET_AR/0.0041816,0)<="+Shangx;
				}
			}else{
				if(!Xiax.equals("")){
					rezqj = " AND round_new(ZL.QNET_AR/0.0041816,0)>="+Xiax;
				}else{
					rezqj = "";
				}
			}
		}else{
			if(!Shangx.equals("")){
				if(!Xiax.equals("")){
					rezqj = " AND round_new(ZL.star,2)>="+Xiax +" AND round_new(ZL.star,2)<="+Shangx;
				}else{
					rezqj = " AND round_new(ZL.star,2)<="+Shangx;
				}
			}else{
				if(!Xiax.equals("")){
					rezqj = " AND round_new(ZL.star,2)>="+Xiax;
				}else{
					rezqj = "";
				}
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(
				"SELECT decode(M.MINGC, NULL, '合计', m.mingc) AS meikdw,\n" +
				"       decode(F.DAOHRQ,\n" + 
				"              NULL,\n" + 
				"              decode(m.mingc, NULL, '', '小计'),\n" + 
				"              to_char(F.DAOHRQ, 'yyyy.mm.dd')) AS daohrq,\n" + 
				"       P.MINGC,\n" + 
				"       ZM.BIANM,\n" + 
				"       SUM(ROUND_NEW(DECODE(YP.LEIB, '抽查样', 0, F.LAIMSL)," + v.getShuldec() + ")) JINGZ,\n" + 
				"       ROUND_NEW(DECODE(SUM(ROUND_NEW(F.LAIMSL," + v.getShuldec() + ")),\n" + 
				"                        0,\n" + 
				"                        0,\n" + 
				"                        ROUND_NEW(SUM(ROUND_NEW(ZL.QNET_AR, " + v.getFarldec() + ") *\n" + 
				"                                      ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")) /\n" + 
				"                                  SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"                                  3)) * 1000 / 4.1816,\n" + 
				"                 0) AS QNET_AR,\n" + 
				"       DECODE(SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              ROUND_NEW(SUM(ROUND_NEW(ZL.MT, 1) * ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")) /\n" + 
				"                        SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"                        1)) AS MT,\n" + 
				"       DECODE(SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              ROUND_NEW(SUM(ZL.AAR * ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")) /\n" + 
				"                        SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"                        2)) AS AAR,\n" + 
				"       DECODE(SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              ROUND_NEW(SUM(ZL.VAR * ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")) /\n" + 
				"                        SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"                        2)) AS VAR,\n" + 
				"       DECODE(SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              ROUND_NEW(SUM(ZL.star * ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")) /\n" + 
				"                        SUM(ROUND_NEW(F.LAIMSL, " + v.getShuldec() + ")),\n" + 
				"                        2)) AS STAR \n" + 
				"\n" + 
				"  FROM FAHB F,\n" + 
				"       (select id,\n" + 
				"               zhilb_id,\n" + 
				"               QNET_AR,\n" + 
				"               MT,\n" + 
				"               AAR,\n" + 
				"               vdaf,\n" + 
				"               VAR,\n" + 
				"               ROUND(stad * (100 - mt) / (100 - mad), 2) star \n" + 
				"          from ZHILLSB) ZL,\n" + 
				"       ZHUANMB ZM,\n" + 
				"       MEIKXXB M,\n" + 
				"       PINZB P,\n" + 
				"       YANGPDHB YP\n" + 
				" WHERE F.DAOHRQ >= TO_DATE('" + getRiqi() + "', 'yyyy-mm-dd')\n" + 
				"   AND F.DAOHRQ <= TO_DATE('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"   AND F.ZHILB_ID = ZL.ZHILB_ID\n" + 
				"   AND F.PINZB_ID = P.ID\n" + 
				"   AND F.MEIKXXB_ID = M.ID\n" +  meik + rezqj + pinz +
				"   AND ZL.ID = ZM.ZHILLSB_ID\n" + 
				"   AND ZM.ZHUANMLB_ID = 100663\n" + 
				"   AND YP.ZHILBLSB_ID = ZL.ID\n" + s +
				" GROUP BY ROLLUP(M.MINGC, F.DAOHRQ, F.ZHILB_ID, (P.MINGC, ZM.BIANM))\n" + 
				"HAVING GROUPING(M.MINGC) + GROUPING(ZM.BIANM) = 0 OR GROUPING(F.DAOHRQ) + GROUPING(ZM.BIANM) = 2\n" + 
				" ORDER BY M.MINGC, F.DAOHRQ"
			);

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][11];

		ArrHeader[0] = new String[] { "矿别", "入厂煤日期", "品种", "来样编号","入厂煤量(吨)", "收到基低位热量(Kcal/g)<br>Qnet,ar<br>" ,
				"收到基水分<br>(%)<br>Mt", "收到基灰<br>(%)<br>Aar", "收到基挥发分<br>(%)<br>Var",
				"收到基硫<br>(%)<br>Star","干燥基全硫<br>(%)<br>Stad"};
		int ArrWidth[] = new int[] { 100, 80, 80,80, 80, 70, 70, 70, 70, 70,70 };

		rt.setTitle("数量、化验统计台帐", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(4, 3, "卸煤日期:" + getRiqi() + "至" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		for (int i=6;i <= rt.body.getCols(); i++)
			rt.body.setColFormat(i, "0.00");
		for (int i = 1; i <= rt.body.getCols(); i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
//		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}


	private IDropDownBean MeikxxValue;

	public IDropDownBean getMeikxxValue() {
		if (MeikxxValue == null) {
			MeikxxValue = (IDropDownBean) MeikxxModel.getOption(0);
		}
		return MeikxxValue;
	}

	public void setMeikxxValue(IDropDownBean Value) {
		if (!(MeikxxValue == Value)) {
			MeikxxValue = Value;
//			falg = true;
		}
	}

	private IPropertySelectionModel MeikxxModel;

	public void setMeikxxModel(IPropertySelectionModel value) {
		MeikxxModel = value;
	}

	public IPropertySelectionModel getMeikxxModel() {
		if (MeikxxModel == null) {
			getMeikxxModels();
		}
		return MeikxxModel;
	}

	public IPropertySelectionModel getMeikxxModels() {
		String sql = "select id,mingc from meikxxb where zhuangt=1";
		MeikxxModel = new IDropDownModel(sql, "全部");
		return MeikxxModel;
	}
	//热值下拉框
	    
	    public IDropDownBean getRezValue() {
			if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
				if (getRezModel().getOptionCount() > 0) {
					setRezValue((IDropDownBean) getRezModel().getOption(0));
				}
			}
			return ((Visit) this.getPage().getVisit()).getDropDownBean3();
		}

		public void setRezValue(IDropDownBean value) {
			((Visit) this.getPage().getVisit()).setDropDownBean3(value);
		}
		
		public IPropertySelectionModel getRezModel() {
			if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
				setRezModels();
			}
			return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
		}

		public void setRezModel(IPropertySelectionModel value) {
			((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
		}

		public void setRezModels() {
			List list = new ArrayList();
			list.add(new IDropDownBean(1, "热值"));
			list.add(new IDropDownBean(2, "硫分"));
			setRezModel(new IDropDownModel(list));
		}
	//品种下拉框
	private IDropDownBean PinzValue;

	public IDropDownBean getPinzValue() {
		if (PinzValue == null) {
			PinzValue = (IDropDownBean) PinzModel.getOption(0);
		}
		return PinzValue;
	}

	public void setPinzValue(IDropDownBean Value) {
		if (!(PinzValue == Value)) {
			PinzValue = Value;
//			falg = true;
		}
	}

	private IPropertySelectionModel PinzModel;

	public void setPinzModel(IPropertySelectionModel value) {
		PinzModel = value;
	}

	public IPropertySelectionModel getPinzModel() {
		if (PinzModel == null) {
			getPinzModels();
		}
		return PinzModel;
	}

	public IPropertySelectionModel getPinzModels() {
		String sql = "select id,mingc from pinzb where zhuangt=1";
		PinzModel = new IDropDownModel(sql, "全部");
		return PinzModel;
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			this.setShangx("");
			this.setXiax("");
			this.setRezModel(null);
			this.setRezValue(null);
			setTreeid_dc(null);
		}	
		
		blnIsBegin = true;
		getSelectData();

	}
	
//	private String getDataSource(){
//		Visit visit = (Visit) getPage().getVisit();
//		return visit.getString14();
//	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			riqi = DateUtil.FormatDate(stra.getTime());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
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

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
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
		getZhiltz();
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
						|| "".equals(getTreeid_dc()) ? "0" : getTreeid_dc())));

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
		df.setWidth(70);
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		df1.setWidth(70);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("煤矿:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("MEIKXXSelect");
		meik.setEditable(true);
		meik.setWidth(90);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("品种:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setEditable(true);
		pinz.setWidth(70);
		pinz.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));
		
//		leix.setListeners("select:function(own,rec,index){Ext.getDom('LeixSelect').selectedIndex=index}");

		ComboBox rez = new ComboBox();
		rez.setTransform("RezSelect");
		rez.setEditable(true);
		rez.setWidth(70);
		rez.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(rez);
		
		tb1.addText(new ToolbarText("下限："));
		TextField xiax = new TextField();
		xiax.setId("XIAX");
		xiax.setWidth(40);
		xiax.setValue(getXiax());
		xiax.setListeners("change:function(own,newValue,oldValue){"
				+ "document.all.item('XIAX').value=newValue;}");
		tb1.addField(xiax);
		
		tb1.addText(new ToolbarText("上限："));
		TextField shangx = new TextField();
		shangx.setId("SHANGX");
		shangx.setWidth(40);
		shangx.setValue(getShangx());
		shangx.setListeners("change:function(own,newValue,oldValue){"+
				"document.all.item('SHANGX').value=newValue;}");
		tb1.addField(shangx);
		
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
}