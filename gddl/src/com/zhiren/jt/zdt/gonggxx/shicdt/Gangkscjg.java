package com.zhiren.jt.zdt.gonggxx.shicdt;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Gangkscjg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	 private void Save() {
	 Visit visit = (Visit) this.getPage().getVisit();
	 visit.getExtGrid1().Save(getChange(), visit);
	 }

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}
	
//绑定开始日期
	private String startRiq;
	public String getStartRiq() {
		return startRiq;
	}
	public void setStartRiq(String riq) {
		this.startRiq = riq;
	}
//绑定结束日期
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql="select g.id,\n"
			+ "       g.riq,\n"
			+ "       c.mingc as chezxxb_id,\n"
			+ "       p.mingc as pinzb_id,\n"
			+ "       g.jiagxx,\n"
			+ "       g.jiagsx,\n"
			+ "       g.beiz\n"
			+ " from chezxxb c,pinzb p,gangkscjgb g\n"
			+ " where g.chezxxb_id = c.id"
			+ " and g.pinzb_id = p.id"
			+ " and c.id = "+ getPandbID()
			+ " and riq >= " + DateUtil.FormatOracleDate(getStartRiq())
			+ " and riq < " + DateUtil.FormatOracleDate(getEndRiq()) + "+1"
			+ " order by g.id";
//		System.out.print(sql);
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("gangkscjgb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(70);
		egu.getColumn("riq").setDefaultValue(getEndRiq());
		egu.getColumn("chezxxb_id").setHeader("港口");
		egu.getColumn("chezxxb_id").setWidth(70);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(70);
//		egu.getColumn("jiagsx").editor.setMaxValue("5000");
//		egu.getColumn("jiagsx").editor.setMinValue("100");
		egu.getColumn("jiagxx").setHeader("价格下限（元/吨）");
		egu.getColumn("jiagxx").setWidth(110);
		egu.getColumn("jiagxx").setDefaultValue("0");
		egu.getColumn("jiagxx").editor.allowBlank = false;//不能为空
		
		egu.getColumn("jiagsx").setHeader("价格上限（元/吨）");
		egu.getColumn("jiagsx").setWidth(110);
		egu.getColumn("jiagsx").setDefaultValue("0");
		egu.getColumn("jiagsx").editor.allowBlank = false;//不能为空
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(130);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addTbarText("日期:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ","");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("至");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ","");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("港口：");
		ComboBox gkcom = new ComboBox();
		gkcom.setWidth(100);
		gkcom.setTransform("GangkDropDown");
		gkcom.setId("GangkDropDown");
		gkcom.setLazyRender(true);
		egu.addToolbarItem(gkcom.getScript());
		egu.addTbarText("-");
		
		IDropDownModel dm=new IDropDownModel("select id, mingc from chezxxb where leib = '港口' order by xuh");
		ComboBox gk = new ComboBox();
		gk.setEditable(true);
		egu.getColumn("chezxxb_id").setEditor(gk);
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,dm);
		if (dm.getOptionCount()>0){
			egu.getColumn("chezxxb_id").setDefaultValue(dm.getLabel(0));
		}
		
		
		IDropDownModel dmpz=new IDropDownModel("select id, mingc from pinzb where leib='港口煤'");
		ComboBox pz = new ComboBox();
		pz.setEditable(true);
		egu.getColumn("pinzb_id").setEditor(pz);
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,dmpz);
//		if (dmpz.getOptionCount()>0){
//			egu.getColumn("pinzb_id").setDefaultValue(dmpz.getLabel(0));
//		}
		
		GridButton gRefresh = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		setExtGrid(egu);
		con.Close();
	}
	
	//港口下拉框
	private IPropertySelectionModel _gangkModel;
	public void setGangkModel(IDropDownModel value) {
		_gangkModel = value; 
	}
	public IPropertySelectionModel getGangkModel() {
		if (_gangkModel == null) {
			_gangkModel = new IDropDownModel("select id,mingc from chezxxb  where leib = '港口' order by xuh");
		}
	    return _gangkModel;
	}
	private IDropDownBean _gangkValue;
	public void setGangkValue(IDropDownBean value) {
		_gangkValue = value;
	}
	public IDropDownBean getGangkValue() {
		return _gangkValue;
	}
	public long getPandbID() {
		if (getGangkValue() == null) {
			return -1;
		}
		return getGangkValue().getId();
	}


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
			setGangkModel(null);
			setGangkValue(null);
			setStartRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
}
