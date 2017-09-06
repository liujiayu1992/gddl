package com.zhiren.gangkjy.zhilgl.caiyxx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 *作者:童忠付
 *时间:2009-3-25
 *内容:质量管理 入场采样查询 
 */
public class Ruccycx extends BasePage implements PageValidateListener {



	private static final String BAOBPZB_GUANJZ = "ZL_CY_RC_CX";// baobpzb中对应的关键字  质量  采样  入厂 采样查询
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

	private String TAIZ = "Zhiltz";

	private String YUEB = "Zhilyb";

	

	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;

		
			return getZhiltz();
		}
	


	
	private StringBuffer getBaseSql(){

		StringBuffer buffer = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();

		buffer.append(" select rownum xuh,bianm,fahr,caiylb,caiyrq,yangplb,yangpzl,jieyr,beiz from(\n");
		buffer.append(" select distinct c.bianm,v.mingc fahr,cl.mingc caiylb,to_char(c.caiyrq,'yyyy-MM-dd') caiyrq,c.yangplb,c.yangpzl,c.jieyr,c.beiz from \n");
		buffer.append(" caiyb c,zhillsb z,vwfahr v,fahb f ,caiylbb cl \n");
		buffer.append(" where c.id=z.caiyb_id and z.zhilb_id=f.zhilb_id and f.gongysb_id=v.id and c.caiylbb_id=cl.id and cl.bianm='JC' \n");
		
		if(this.getFahrValue().getId()!=-1L){
			buffer.append(" and v.id=").append(this.getFahrValue().getId()).append("\n");
		}
		
		if(this.getRiqi()!=null && !this.getRiqi().equals("")){
			buffer.append(" and c.caiyrq>=to_date('").append(this.getRiqi()).append("','yyyy-MM-dd')").append("\n");
		}
		
		if(this.getRiq2()!=null && !this.getRiq2().equals("")){
			buffer.append(" and c.caiyrq<to_date('").append(this.getRiq2()).append("','yyyy-MM-dd')+1").append("\n");
		}
		
		if(this.getLeibValue().getId()!=-1L){
			buffer.append(" and c.yangplb='").append(this.getLeibValue().getValue()).append("'\n");
		}
		
		buffer.append(" )\n");
		return buffer;
	}
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		buffer=this.getBaseSql();

		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
//		System.out.println(buffer);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { { Local.xuh,
					Local.bianm, Local.fahr,
					Local.caiylb,Local.caiyrq, Local.yangplb,
					Local.yangpzl, Local.jieyy, Local.beiz
					} };

			ArrWidth = new int[] { 40, 100,100, 80, 80, 80, 80, 80, 80};

			rt.setTitle(Local.RptTitle_ruccycx, ArrWidth);
		}
		
		ArrWidth = new int[] { 40, 100,100, 80, 80, 80, 80, 80, 80};
		rt.setTitle(Local.RptTitle_ruccycx, ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 4, "制表单位:"
				+ v.getDiancqc(),
				Table.ALIGN_LEFT);
		
		rt.setDefaultTitle(5, 5, "入场采样日期:"
				+ getRiqi() + "至"
				+getRiq2(),
				Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
//
//		strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
//				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
//				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
	//	rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 9; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		con.Close();
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages> 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSql().toString(), rt,
				Local.RptTitle_ruchytjcx, "" + BAOBPZB_GUANJZ);
		return rt.getAllPagesHtml();

	}

	// 类别下拉框
	public IDropDownBean getLeibValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getLeibModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setLeibValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean6()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public IPropertySelectionModel getLeibModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {

			getLeibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setLeibModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getLeibModels() {

		
			List list=new ArrayList();
			list.add(new IDropDownBean(-1,"全部"));
			list.add(new IDropDownBean(2,"人工"));
			list.add(new IDropDownBean(3,"机械"));
			list.add(new IDropDownBean(4,"混合"));
			
			((Visit) getPage().getVisit())
			.setProSelectionModel6(new IDropDownModel(list));
	return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	
	
	//----------------------------------------------------
//	 发货人下拉框
	public IDropDownBean getFahrValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFahrModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFahrValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFahrModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFahrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFahrModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFahrModels() {

		
			String sql ="select v.id,v.mingc from vwfahr v";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "全部"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
	
	



	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				visit.setProSelectionModel6(null);
				visit.setDropDownBean6(null);
				setRiqi(null);

			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}

		}
		blnIsBegin = true;
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

//		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
	//	}

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

	//	if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
	//	}

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


	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("采样日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
	//	df.setValue(null);
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
	//	df1.setValue(null);
		df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("发货人:"));
		ComboBox fahr = new ComboBox();
		fahr.setEditable(true);
		fahr.setTransform("FahrDropDown");
		
		fahr.setWidth(100);
	//	meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(fahr);
		

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("样品类别:"));
		ComboBox leib = new ComboBox();
		leib.setTransform("LeibDropDown");
		leib.setEditable(true);
		leib.setWidth(100);
	//	meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(leib);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
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

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}

}
