package com.zhiren.shihs;

import java.sql.ResultSet;
import java.util.Date;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shihsltz extends BasePage implements PageValidateListener {
	
	protected void initialize() {
		super.initialize();
	}
	
	public boolean getRaw() {
		return true;
	}
	
	// 报表初始设置
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
	
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getRiqs() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiqs(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}
	
	public String getRiqe() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiqe(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq1);
		}
	}
	
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			setPinzValue(null);
			setPinzModel(null);
			getPinzModels();
			setLiulfsValue(null);
			setLiulfsModel(null);
			getLiulfsModels();
		}
		getSelectData();
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
	
	public void getSelectData() {
		String riqTiaoj = this.getRiqs();
		String riqTiaoj2 = this.getRiqe();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		if (riqTiaoj2 == null || riqTiaoj2.equals("")) {
			riqTiaoj2 = DateUtil.FormatDate(new Date());
		}
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRiqs());
		df.Binding("RIQS", "");// 与html页中的id绑定,并自动刷新
		df.setId("startrq");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("至:"));
		DateField df2 = new DateField();
		df2.setValue(getRiqe());
		df2.Binding("RIQE", "forms[0]");// 与html页中的id绑定,并自动刷新
		df2.setId("endrq");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));
			
		tb1.addText(new ToolbarText("供货单位:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("GongysDropDown");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("品种:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("PinzDropDown");
		cb2.setWidth(80);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("浏览方式:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("LiulfsDropDown");
		cb3.setWidth(80);
		tb1.addField(cb3);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addFill();
		
		setToolbar(tb1);
	}
	
	private String getWhere(){
		
		String where="";
		
		//供应商单位
		if(MainGlobal.getProperId(getGongysModel(), getGongysValue().getValue())>-1){			
			where+=" and cp.gongysb_id="+MainGlobal.getProperId(getGongysModel(), getGongysValue().getValue()) +"\n";
		}
    	//品种
    	if(MainGlobal.getProperId(getPinzModel(), getPinzValue().getValue())>-1){
    		where+=" and cp.SHIHPZB_ID = " + MainGlobal.getProperId(getPinzModel(), getPinzValue().getValue()) +"\n";
    	}
    	return where;
	}
	
	public String getPrintTable() {
		Visit visit = (Visit) this.getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqs();
		String riqTiaoj2 = this.getRiqe();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		if (riqTiaoj2 == null || riqTiaoj2.equals("")) {
			riqTiaoj2 = DateUtil.FormatDate(new Date());
		}
		String sql="";
    	//浏览方式
    	if(getLiulfsValue().getId()==-1){
    		sql = 

    			"select DECODE(GYS.MINGC, NULL, '合计', GYS.MINGC) AS GONGYSMC,\n" +
    			"       DECODE(GROUPING(GYS.MINGC)+GROUPING(PZ.MINGC), 1, '供应商合计', PZ.MINGC) AS PINZMC,\n" + 
    			"       DECODE(GROUPING(CP.CHEPH)+GROUPING(PZ.MINGC), 1, '品种小计', cp.cheph) AS CHEPH,\n" + 
    			"       to_char(cp.fahrq,'yyyy-mm-dd') fahrq,\n" + 
    			"       to_char(cp.daohrq,'yyyy-mm-dd') daohrq,\n" + 
    			"       count(0),\n" + 
    			"       sum(cp.maoz),\n" + 
    			"       sum(cp.piz),\n" + 
    			"       sum(cp.maoz)-sum(cp.piz) jingz\n" + 
    			"  from SHIHCPB cp, SHIHGYSB GYS, SHIHPZB PZ\n" + 
    			" where CP.GONGYSB_ID = GYS.ID\n" + 
    			"   AND CP.SHIHPZB_ID = PZ.ID\n" + 
    			"   AND CP.DIANCXXB_ID = " + diancxxb_id + "\n" +
    				getWhere() + "\n" +
    			"   AND CP.DAOHRQ BETWEEN TO_DATE('" + riqTiaoj + "', 'yyyy-mm-dd') AND TO_DATE('" + riqTiaoj2 + "', 'yyyy-mm-dd')\n" + 
    			"group by rollup(GYS.MINGC, PZ.MINGC, (CHEPH, CP.FAHRQ, CP.DAOHRQ))" + 
    			"order by gys.mingc,pz.mingc,cp.fahrq,cp.cheph";
    	}else{
    		sql = 

    			"select GYS.MINGC AS GONGYSMC,\n" +
    			"       PZ.MINGC AS PINZMC,\n" + 
    			"       '' AS CHEPH,\n" + 
    			"       to_char(cp.fahrq,'yyyy-mm-dd') fahrq,\n" + 
    			"       to_char(cp.daohrq,'yyyy-mm-dd') daohrq,\n" + 
    			"       count(0),\n" + 
    			"       sum(cp.maoz),\n" + 
    			"       sum(cp.piz),\n" + 
    			"       sum(cp.maoz)-sum(cp.piz) jingz\n" + 
    			"  from SHIHCPB cp, SHIHGYSB GYS, SHIHPZB PZ\n" + 
    			" where CP.GONGYSB_ID = GYS.ID\n" + 
    			"   AND CP.SHIHPZB_ID = PZ.ID\n" + 
    			"   AND CP.DIANCXXB_ID = " + diancxxb_id + "\n" +
    				getWhere() + "\n" +
    			"   AND CP.DAOHRQ BETWEEN TO_DATE('" + riqTiaoj + "', 'yyyy-mm-dd') AND TO_DATE('" + riqTiaoj2 + "', 'yyyy-mm-dd')\n" + 
    			"group by GYS.MINGC, PZ.MINGC, CP.FAHRQ, CP.DAOHRQ\n" + 
    			"order by gys.mingc,pz.mingc,cp.fahrq";
    	}
    	
		

		ResultSet rs=con.getResultSet(sql);
		Report rt=new Report();
		String ArrHeader[][]=new String[1][9];
		ArrHeader[0] = new String[] {"供应商","品种","车牌号","发货日期","到货日期","车数",
					"毛重","皮重","净重"};
		int ArrWidth[]=new int[] {100,80,70,80,80,50,60,70,50};
		rt.setTitle("石灰石数量台账", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		for (int i=1;i<=rt.body.getCols();i++) 
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		for (int i=8;i<=rt.body.getCols();i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.setPageRows(40);
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_RIGHT);
		rt.setDefautlFooter(6, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "制表：", Table.ALIGN_LEFT);
		return rt.getAllPagesHtml();
	}
	
	
//	供应商下拉框
	public String getGongys() {
		return ((Visit) this.getPage().getVisit()).getString10();
	}
	public void setGongys(String changb) {
		((Visit) this.getPage().getVisit()).setString10(changb);
	}
	
//	品种下拉框
	public String getPinz() {
		return ((Visit) this.getPage().getVisit()).getString11();
	}
	public void setPinz(String pinz) {
		((Visit) this.getPage().getVisit()).setString11(pinz);
	}
	
//	浏览下拉框
	public String getLiulfs() {
		return ((Visit) this.getPage().getVisit()).getString12();
	}
	public void setLiulfs(String liulfs) {
		((Visit) this.getPage().getVisit()).setString12(liulfs);
	}
	
//	品种_begin
	
	public IDropDownBean getPinzValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getPinzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setPinzValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean7()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getPinzModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getPinzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void setPinzModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getPinzModels() {
			String sql = "select id,mingc from shihpzb order by mingc";
			((Visit) getPage().getVisit())
			.setProSelectionModel7(new IDropDownModel(sql,"全部"));
			return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
//	品种_end
	
//	供货单位
	public IDropDownBean getGongysValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setGongysValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean8()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getGongysModels() {
		String sql = "SELECT ID,mingc FROM shihgysb";

		((Visit) getPage().getVisit())
		.setProSelectionModel8(new IDropDownModel(sql,"全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
	
//	浏览方式
	public IDropDownBean getLiulfsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getLiulfsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setLiulfsValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean6()){
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public IPropertySelectionModel getLiulfsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getLiulfsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setLiulfsModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}
	public IPropertySelectionModel getLiulfsModels() {
			String sql = "select 1 id,'合计表' from dual ";
			((Visit) getPage().getVisit())
			.setProSelectionModel6(new IDropDownModel(sql,"明细表"));
			return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}
}
