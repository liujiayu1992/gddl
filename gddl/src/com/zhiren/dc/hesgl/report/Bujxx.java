package com.zhiren.dc.hesgl.report;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Bujxx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	//日期
	private String riq;
	
	public String getRiq(){
		return riq;
	}
	public void setRiq(String riq){
		this.riq=riq;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	protected void initialize() {
		msg = "";
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		
		String sql =

			"select bj.id,\n" +
			"       jiesfab_id,\n" + 
			"       bujlx,\n" + 
			"       jiesdw,\n" + 
			"       ys.mingc as yunsdwb_id,\n" + 
			"       meikxxb_id,\n" + 
			"       danj,\n" +
			"       meil,\n" +
			"       meik,\n" + 
			"       yunf,\n" + 
			"       shuij,\n" + 
			"       miaos\n" + 
			"       from bujb bj,yunsdwb ys\n" + 
			"       where bj.yunsdwb_id = ys.id(+)\n" + 
			"       and jiesfab_id = "+ getJiesfaValue().getId()+"\n" + 
			"       and bujlx = '"+ getJieslxValue().getValue()+"'";		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("bujb");
		egu.setWidth("bodyWidth");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("jiesfab_id").setHidden(true);
		egu.getColumn("jiesfab_id").setDefaultValue(getJiesfaValue().getId() + "");
		egu.getColumn("bujlx").setHidden(true);
		egu.getColumn("bujlx").setDefaultValue(getJieslxValue().getValue());
		
		egu.getColumn("jiesdw").setHeader("结算单位");
		egu.getColumn("jiesdw").setEditor(new ComboBox());
		egu.getColumn("jiesdw").setComboEditor(egu.gridId,
				new IDropDownModel("select id, shoukdw from jiesb order by shoukdw"));
		egu.getColumn("jiesdw").returnId = false;
		egu.getColumn("jiesdw").editor.allowBlank = true;
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setEditor(new ComboBox());
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsdwb order by mingc"));
		egu.getColumn("yunsdwb_id").returnId = true;
		egu.getColumn("yunsdwb_id").editor.allowBlank = true;
		
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("danj").setHeader("结算煤价(元/吨)");
		egu.getColumn("meil").setHeader("煤量(吨)");
		egu.getColumn("meik").setHeader("煤款(元)");
		egu.getColumn("yunf").setHeader("运费(元)");
		egu.getColumn("shuij").setHeader("税金(元)");
		egu.getColumn("miaos").setHeader("描述");
		if ("煤款补价".equals(getJieslxValue().getValue())) {
			egu.getColumn("yunsdwb_id").setHidden(true);
			egu.getColumn("yunsdwb_id").setDefaultValue("");
			egu.getColumn("yunf").setHidden(true);
			egu.getColumn("yunf").setDefaultValue("0");
			egu.getColumn("meil").setHidden(true);
			egu.getColumn("meil").setDefaultValue("0");
		} else {
			if("煤场短盘补价".equals(getJieslxValue().getValue())){
				egu.getColumn("meil").setHidden(true);
				egu.getColumn("meil").setDefaultValue("0");
			}
			egu.getColumn("jiesdw").setHidden(true);
			egu.getColumn("jiesdw").setDefaultValue("");
			egu.getColumn("meik").setHidden(true);
			egu.getColumn("meik").setDefaultValue("0");
			if("运费补价".equals(getJieslxValue().getValue())){
			}else{
			egu.getColumn("danj").setHidden(true);
			egu.getColumn("danj").setDefaultValue("0");
			}
		}
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
        //日期
		egu.addTbarText("结算日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("补价类型:");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("JieslxDropDown");
		comb4.setId("Jieslx");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(100);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");		
		egu.addTbarText("-");
		
		egu.addTbarText("结算方案");
		ComboBox jiesfa = new ComboBox();
		jiesfa.setTransform("JiesfaDropDown");
		jiesfa.setId("Jiesfa");
		jiesfa.setEditable(false);
		jiesfa.setLazyRender(true);
		jiesfa.setWidth(100);
		egu.addToolbarItem(jiesfa.getScript());
		egu.addOtherScript("Jiesfa.on('select',function(){document.forms[0].submit();});");		
		egu.addTbarText("-");
		
		// 刷新按钮
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		
		egu.addTbarBtn(refurbish);
		refurbish.setMinWidth(40);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");					
		
		setExtGrid(egu);
		rsl.close();
		con.Close();
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setRiq(DateUtil.FormatDate(new Date()));
			setJiesfaValue(null);
			setIJiesfaModel(null);
			getIJiesfaModels();
			
			setJieslxValue(null);
			setJieslxModel(null);
			getJieslxModels();
			
			getSelectData();
		}
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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

	// 结算方案
	public IDropDownBean getJiesfaValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean2(new IDropDownBean(-1, ""));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesfaValue(IDropDownBean Value) {
		
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean2()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean2().getId();
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIJiesfaModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIJiesfaModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIJiesfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesfaModels() {

			String sql = 
				"SELECT ID, BIANM\n" +
				"\tFROM JIESFAB\n" + 
				" WHERE SHIFJS = 1\n" + 
				"\t AND JIESDW_ID = " + this.getTreeid() + "\n" + 
				"\t and daohjzsj = to_date('"+this.getRiq()+"','yyyy-MM-dd')\n"+
				" ORDER BY BIANM desc";
			
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
    // 补价类型
	public boolean _Jieslxchange = false;
	
	public IDropDownBean getJieslxValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getJieslxModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getJieslxModels() {
		
		String sql = 
			"select id, mingc\n" +
			"  from (select 1 as id, '煤场短盘补价' as mingc\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select 2 as id, '煤款补价' as mingc\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select 3 as id, '运费补价' as mingc from dual)";
			
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
}