package com.zhiren.haiym.baob;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhongchy extends BasePage implements PageValidateListener {
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
	boolean afterchange = false;
	private String after;
	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}
	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}

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
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getRucmzjyyb();
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

	private String getRucmzjyyb() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append(
			"select\n" +
			"       g.mingc gmingc,\n" + 
			"       max(c.piaojh),\n" + 
			"       l.mingc lmingc,\n" + 
			"       f.chec,\n" + 
			"       to_char(f.daohrq,'yyyy-dd-mm') daohrq,\n" + 
			"       round_new(z.qnet_ar/4.1816*1000,0) rl,\n" + 
			"       round_new(z.mt,1) mt,\n" + 
			"       round_new(z.aar,2) aar,\n" + 
			"       round_new(z.var,2) var,\n" + 
			"       round_new(z.stad*(100-z.mt)/(100-z.mad),2) star,\n" + 
			"       z.t2,\n" + 
			"       f.biaoz\n" + 
			"  from fahb f,kuangfzlb z ,gongysb g,chepb c,luncxxb l\n" + 
			"where f.kuangfzlb_id = z.id\n" + 
			"  and f.gongysb_id=g.id\n" + 
			"  and f.id = c.fahb_id\n" + 
			"  and f.luncxxb_id=l.id\n" + 
			"  and f.daohrq >= to_date('" + getRiq() + "', 'yyyy-mm-dd')\n" + 
			"  and f.daohrq <= to_date('" + getAfter() + "', 'yyyy-mm-dd')\n" );
		if(getGongysValue().getId()!=0){
			sql.append("  and f.gongysb_id=" +getGongysValue().getId() + "\n");
		}		
		sql.append("group by g.mingc,l.mingc,f.chec,daohrq,qnet_ar,mt,aar,var,z.stad,z.mad,z.t2,f.biaoz\n" +
			"order by g.mingc, l.mingc ,f.chec,daohrq");

		ResultSetList rstmp = con.getResultSetList(sql.toString());
		int[] ArrWidth=null;
		String[][] ArrHeader=null;
		
		ArrHeader = new String[1][12];
		
		ArrHeader[0] = new String[] { "发货单位", "运单编号", "船名", "船次","到货日期",
				"发热量(Qnet)<br>(大卡/KG)","水份<br>(Mt)%","灰份<br>(Aar)%","挥发分<br>(Var)%",
				"全硫<br>(Star)%","灰熔点<br>(T2)℃","数量(吨)"};
		ArrWidth = new int[12];
		ArrWidth = new int[] { 90, 90, 70, 30, 70, 
								50, 50, 50, 50, 50, 50, 50};

		rt.setTitle(v.getDiancqc() +"<br>仲裁化验", ArrWidth);
//		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 24);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "到货日期:" + getRiq() + "至" + getAfter(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rstmp, 1, 0, 5));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.createDefautlFooter(ArrWidth);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		rt.setDefautlFooter(10, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(1, 2, "制表:", Table.ALIGN_LEFT);

		// 设置页数
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		con.Close();
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();
	}

	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("供应商:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("GONGYSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

		ToolbarButton tb = new ToolbarButton(null, "查询",
		"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);

		setToolbar(tb1);

	}
	
//  判断电厂Tree中所选电厂时候还有子电厂   
//    private boolean hasDianc(String id){ 
//		JDBCcon con=new JDBCcon();
//		boolean mingc= false;
//		String sql="select mingc from diancxxb where fuid = " + id;
//		ResultSetList rsl=con.getResultSetList(sql);
//		if(rsl.next()){
//			mingc=true;
//		}
//		rsl.close();
//		return mingc;
//	}
	
//	 统计方式下拉框
	private IDropDownBean GongysValue;
	public IDropDownBean getGongysValue() {
		if (GongysValue == null) {
			GongysValue = (IDropDownBean) GongysModel.getOption(0);
		}
		return GongysValue;
	}
	public void setGongysValue(IDropDownBean Value) {
		if (!(GongysValue == Value)) {
			GongysValue = Value;
		}
	}
	private IPropertySelectionModel GongysModel;
	public void setGongysModel(IPropertySelectionModel value) {
		GongysModel = value;
	}
	public IPropertySelectionModel getGongysModel() {
		if (GongysModel == null) {
			getGongysModels();
		}
		return GongysModel;
	}
	public IPropertySelectionModel getGongysModels() {
		String sql = "select 0 id,'全部' mingc from dual " +
					"union select g.id ,g.mingc from fahb f,gongysb g " +
					"	   where f.gongysb_id=g.id " ;
		GongysModel = new IDropDownModel(sql);
		return GongysModel;
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
//				setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
				setGongysValue(null);
				setGongysModel(null);
		}
		blnIsBegin = true;
		getSelectData();
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
}
