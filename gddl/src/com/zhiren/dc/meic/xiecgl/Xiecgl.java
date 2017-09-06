package com.zhiren.dc.meic.xiecgl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiecgl extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	public boolean isChaxShow() {
		return ((Visit) getPage().getVisit()).getInt1() == 1;
	}

	// 显示或隐藏班组和班次
	public int getSelectedStep() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setSelectedStep(int v) {
		((Visit) getPage().getVisit()).setInt1(v);
	}

	public int getEditTableRow() {
		return ((Visit) getPage().getVisit()).getInt2();
	}

	public void setEditTableRow(int _value) {
		((Visit) getPage().getVisit()).setInt2(_value);
	}

	public int getEditTableRow1() {
		return ((Visit) getPage().getVisit()).getInt3();
	}

	public void setEditTableRow1(int _value) {
		((Visit) getPage().getVisit()).setInt3(_value);
	}

	// 区分各煤场记录个数，以改变字体颜色用
	private int option1;

	public int getOption1() {
		return option1;
	}

	public void setOption1(int _value) {
		option1 = _value;
	}

	private int option2;

	public int getOption2() {
		return option2;
	}

	public void setOption2(int _value) {
		option2 = _value;
	}

	private int option3;

	public int getOption3() {
		return option3;
	}

	private String _caiybh;

	public String getCaiybh() {
		if (_caiybh == null) {
			_caiybh = new String();
		}
		return _caiybh;
	}

	public void setCaiybh(String value) {
		_caiybh = value;
	}




	private String _qingcsj;

	public String getQingcsj() {
		if (_qingcsj == null) {
			_qingcsj = DateUtil.FormatDate(new Date());
		}
		return _qingcsj;
	}

	public void setQingcsj(String value) {
		_qingcsj = value;
	}

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList3();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList3(editList);
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_DuihChick) {
			_DuihChick = false;
			Duih(cycle);
		}
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DuihChick = false;

	public void DuihButton(IRequestCycle cycle) {
		_DuihChick = true;
	}

	private void Duih(IRequestCycle cycle) {
		cycle.activate("Duih");
	}


	// 未检斤文件的方法
	public SortMode getSort() {
		return SortMode.USER;
	}

	public IPropertySelectionModel getChepModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			setChepModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setChepModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public List getChepSelected() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setChepSelected(List ChepSelect) {
		((Visit) getPage().getVisit()).setList1(ChepSelect);
	}

	private void setChepModels() {
		setChepSelected(null);
		Visit v = (Visit) getPage().getVisit();
		List _ChepList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = "select id,mingc from renyxxb";
		ResultSetList rs = con.getResultSetList(sql);
		boolean isHidden = true;
		if (rs.next()) {
			isHidden = ("否".equals(rs.getString("zhi")));
		}
		if (isHidden) {
			sql = "select id,mingc from renyxxb";
		} else {
			sql = "select id,mingc from renyxxb";
		}
		rs = con.getResultSetList(sql);
		while (rs.next()) {
			_ChepList.add(new IDropDownBean(rs.getLong("id"), rs
					.getString("cheph")));
		}
		con.Close();
		setChepModel(new IDropDownModel(_ChepList));
	}

	// 表格使用的方法
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	public void getSelectData() {

		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("检斤时间:"));
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setValue(this.getRiqi1());
		df1.Binding("RIQI1", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("采样编号"));
		TextField Cheh = new TextField();
		Cheh.setAllowBlank(true);
		Cheh.setId("_caiybh");
		Cheh.setListeners(
				"change:function(own,newValue,oldValue){"+
                "document.all.item('hetbh').value=newValue;}"        		 	
		);
		tb1.addField(Cheh);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("班组:"));
		ComboBox Banzcb = new ComboBox();
		Banzcb.setTransform("Banz");
		Banzcb.setWidth(130);
		Banzcb.setListeners("select:function(own,rec,index){Ext.getDom('Banz').selectedIndex=index}");
		tb1.addField(Banzcb);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("发站"));
		ComboBox Faz = new ComboBox();
		Faz.setTransform("Faz");
		Faz.setWidth(130);
		Faz.setListeners("select:function(own,rec,index){Ext.getDom('Faz').selectedIndex=index}");
		tb1.addField(Faz);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("上煤日期:"));
		DateField df2 = new DateField();
		df2.setValue(this.getQingcsj());
		df2.Binding("Qingcsj", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("班次"));
		ComboBox Banc = new ComboBox();
		Banc.setTransform("Banc");
		Banc.setWidth(130);
		Banc.setListeners("select:function(own,rec,index){Ext.getDom('Banc').selectedIndex=index}");
		tb1.addField(Banc);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("存煤位置"));
		ComboBox Cunmwz = new ComboBox();
		Cunmwz.setTransform("Cunmwz");
		Cunmwz.setWidth(130);
		Cunmwz.setListeners("select:function(own,rec,index){Ext.getDom('Cunmwz').selectedIndex=index}");
		tb1.addField(Cunmwz);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		ToolbarButton sbtn = new ToolbarButton(null, "保存",
				"function(){document.getElementById('SaveButton').click();}");
		sbtn.setIcon(SysConstant.Btn_Icon_Save);
		ToolbarButton okbtn = new ToolbarButton(null, "调整车位",
				"function(){document.getElementById('DuihButton').click();}");
		okbtn.setIcon(SysConstant.Btn_Icon_SelSubmit);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addItem(sbtn);
		tb1.addItem(okbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);

	}

	// 卸车List记录
	public List getXiecList() {
		if (((Visit) this.getPage().getVisit()).getList2() == null) {
			setXiecList(new ArrayList());
		}
		return ((Visit) this.getPage().getVisit()).getList2();
	}

	public void setXiecList(List list) {
		((Visit) this.getPage().getVisit()).setList2(list);
	}

	// 班组下拉框
	public IDropDownBean getBanzValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setBanzValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getBanzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getBanzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setBanzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getBanzModels() {
		String sql = "select id,mingc from renyxxb";
		setBanzModel(new IDropDownModel(sql));
	}

	// 班次下拉框
	public IDropDownBean getBancValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setBancValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getBancModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getBancModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setBancModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getBancModels() {
		String sql = "select id,mingc from renyxxb";
		setBancModel(new IDropDownModel(sql));
	}

	// 班次下拉框
	public IDropDownBean getChehValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setChehValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getChehModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getChehModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setChehModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getChehModels() {
		String sql = "select id,mingc from renyxxb";
		setChehModel(new IDropDownModel(sql));
	}

	// 存煤位置下拉框
	public IDropDownBean getCunmwzValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setCunmwzValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getCunmwzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getCunmwzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setCunmwzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getCunmwzModels() {
		if (getPage().getPageName().equals("Xiecgl_qh1")) {
			List lst = new ArrayList();
			// lst.add(new IDropDownBean(-1, "请选择"));
			lst.add(new IDropDownBean(10000, "直接耗用"));

			setCunmwzModel(new IDropDownModel(lst));
			return;
		}
		String sql = "select id,mingc from renyxxb";
		if (getPage().getPageName().equals("Xiecgl_qh")) {
			sql = "select id,mingc from renyxxb";
		}

		setCunmwzModel(new IDropDownModel(sql));
	}

	boolean _isXians = false;

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
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setChepSelected(null);
			setChepModels();
			setBanzValue(null);
			setBanzModel(null);
			setBancValue(null);
			setBancModel(null);
			setChehValue(null);
			setChehModel(null);
			setFazValue(null);
			setFazModel(null);
			setCunmwzValue(null);
			setCunmwzModel(null);

			visit.setList2(null);

			visit.setProSelectionModel6(null);
			visit.setDropDownBean6(null);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
//			try {
//				this.setQingcsj((DateUtil.FormatDate(sdf.parse(sdf.format(new Date())))));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
		}
		getSelectData();
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	private String _msg;

	public void detach() {
		super.detach();
	}





	// 发站下拉框
	public IDropDownBean getFazValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setFazValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean6(value);
	}

	public IPropertySelectionModel getFazModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getIFazModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setFazModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public void getIFazModels() {
		String sql = "select id,mingc from renyxxb";

		setFazModel(new IDropDownModel(sql, "全部"));
	}

	private String Cheph = "";

	public String getCheph() {
		return Cheph;
	}

	public void setCheph(String mCheph) {
		Cheph = mCheph;
	}



	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
		} else {
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// 日期控件
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

	boolean riqichange1 = false;

	private String riqi1;

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi)) {
			this.riqi1 = riqi;
			riqichange1 = true;
		}

	}
}