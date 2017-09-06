package com.zhiren.shanxdted.meigzxbb;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public abstract class Meizhcbb extends BasePage implements PageValidateListener{

	//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	//	 年
	public IDropDownBean getYearValue() {
		int _nianf = DateUtil.getYear(new Date());
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
			.setDropDownBean4((IDropDownBean) getYearModel()
					.getOption(_nianf-2007));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYearValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setYearModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYearModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYearModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getYearModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select yvalue id,ylabel name from nianfb");
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql.toString()));
	}

	// 月
	public IDropDownBean getMonthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			int _yuef = DateUtil.getMonth(new Date());
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getMonthModel()
							.getOption(_yuef-1));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setMonthValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}
	}

	public void setMonthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getMonthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getMonthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getMonthModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select mvalue id,mlabel name from yuefb");
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql.toString()));
	}
	
	
//	页面变化记录
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
    
    //获取表表标题
	public String getRptTitle(String diancid) {
		String sb="";
		String dcmc = "";
		try{
			dcmc=MainGlobal.getTableCol("diancxxb", "mingc", "id in ("+diancid+")");
		}catch(Exception e){
			e.printStackTrace();
		}
		sb = dcmc + getMonthValue().getId()+"月褐煤收耗存报表";
		return sb;
	}
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox year = new ComboBox();
		year.setTransform("YearSelect");
		year.setEditable(true);
		year.setWidth(80);
		tb1.addField(year);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("月份:"));
		ComboBox month = new ComboBox();
		month.setTransform("MonthSelect");
		month.setEditable(true);
		month.setWidth(60);
		tb1.addField(month);
		tb1.addText(new ToolbarText("-"));

		//电厂Tree
//		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
//		" and z.mingc='shulzhcxqx' and r.id="+visit.getRenyID();//zuxxb中组的名称
//		ResultSetList rsl=con.getResultSetList(toaijsql);
//		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
//		if(rsl.next()){
//			diancxxb_id = 300;
//		}
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//		ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid(),null,true);
//
//		setDCTree(etu);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		String[] str=getTreeid().split(",");
//		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
//		ToolbarButton toolb2 = new ToolbarButton(null, null,
//				"function(){diancTree_window.show();}");
//		toolb2.setIcon("ext/resources/images/list-items.gif");
//		toolb2.setCls("x-btn-icon");
//		toolb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(toolb2);
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));	
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	private String getSql(){
		StringBuffer sql=new StringBuffer();
		sql.append(
				"select to_char(r.riq,'yyyy-mm-dd'),jinml,panml,haoml,qimkc from hemshc h,\n" +
				"              (select last_day(to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"','yyyy-mm'))-rownum+1 as riq from all_objects where rownum<=to_char(last_day(to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"','yyyy-mm')),'dd')) r\n" + 
				" where h.diancxxb_id(+)="+getTreeid()+"\n" + 
				"   and r.riq=h.riq(+)\n" + 
				"order by r.riq");
		return sql.toString();
	}
	
	public String getPrintTable(){
		int PageRows=9999;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		ResultSetList rstmp = con.getResultSetList(getSql());
		int[] ArrWidth=null;
		String[][] ArrHeader=null;
		//表头部分
		ArrHeader = new String[1][5];
		ArrHeader[0] = new String[] { "日期","进煤量","盘煤量","耗煤量","库存"};
		
		//设定报表每一列的宽度
		ArrWidth = new int[5];
		ArrWidth = new int[] {  80, 80, 80, 80, 80};

		rt.createTitle(3, ArrWidth);
		rt.title.setCellValue(1, 1,getRptTitle(getTreeid()) , ArrWidth.length);
		
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 16);
		rt.title.setCellAlign(1, 1, Table.ALIGN_CENTER);
		rt.title.setCells(1, 1, 4, 7, Table.PER_FONTNAME, "宋体");
		rt.title.setCells(1, 1, 2, 1, Table.PER_FONTBOLD, true);
		rt.title.setCells(2, 1, 3, 1, Table.PER_FONTBOLD, true);
		rt.title.setCellValue(2, 1, getYearValue().getValue() + " 年 " + getMonthValue().getValue() + " 月 ",ArrWidth.length);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 10);
		
		rt.setBody(new Table(rstmp, 1, 0, 3));  //填充数据
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.createDefautlFooter(ArrWidth);
//		rt.body.merge(1, 1, 1, 22);
//		rt.body.mergeFixedCols();
		rt.body.setPageRows(PageRows);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		rt.body.ShowZero = ((Visit) getPage().getVisit()).isReportShowZero();  //是否显示值为0的数据
		 
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
		return rt.getAllPagesHtml();
	}
	
	//---------------------------
	
	//工具栏使用的方法
	
//	public String getTreeScript() {
//		return getDCTree().getWindowTreeScript();
//	}
////	-------------------------电厂Tree-----------------------------------------------------------------
//	public IPropertySelectionModel getDiancmcModel() {
//		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
//			getDiancmcModels();
//		}
//		return ((Visit) getPage().getVisit()).getProSelectionModel2();
//	}
//
//	public void setDiancmcModel(IPropertySelectionModel _value) {
//		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
//	}
//
//	public void getDiancmcModels() {
//		String sql = "select id,mingc from diancxxb";
//		setDiancmcModel(new IDropDownModel(sql));
//	}
//
//	public String getTreeid() {
//		String treeid = ((Visit) getPage().getVisit()).getString3();
//		if (treeid == null || treeid.equals("")) {
//			((Visit) getPage().getVisit()).setString3(String
//					.valueOf(((Visit) this.getPage().getVisit())
//							.getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString3();
//	}
//	
//	public void setTreeid(String treeid) {
//		((Visit) getPage().getVisit()).setString3(treeid);
//	}
//	
//	public ExtTreeUtil getDCTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setDCTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getDCTree().getWindowTreeHtml(this);
//	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			this.treeid="";
//			getDiancmcModels();
			setYearValue(null);
			setYearModel(null);
			getYearModels();
			setMonthValue(null);
			setMonthModel(null);
			getMonthModels();
			setTreeid(visit.getDiancxxb_id() + "");
		}
		getSelectData();
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	
//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
		}
		getSelectData();
	}
//	页面登陆验证
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
