package com.zhiren.dc.caiygl;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：采样化验次数报表
 */

public class CaiyhycsReport extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	开始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

//	结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
//	供应商下拉框_开始
	public IDropDownBean getGongysValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getGongysModel().getOptionCount() > 0) {
				setGongysValue((IDropDownBean) getGongysModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setGongysValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getGongysModels() {
		String sql = "select id, mingc from gongysb where leix=1 order by mingc";
		setGongysModel(new IDropDownModel(sql, "请选择"));
	}
//	供应商下拉框_结束
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String gongys_id = "";
		if (!getGongysValue().getStrId().equals("-1")) {
			gongys_id = "and cs.gongysb_id = " + getGongysValue().getStrId();
		}
		
		String sql = 
			"select decode(grouping(gys.mingc), 1, '总计', gys.mingc) gongysb_id,\n" +
			"       decode(grouping(gys.mingc)+grouping(mk.mingc), 1, '合计', mk.mingc) meikxxb_id,\n" + 
			"       decode(grouping(mk.mingc)+grouping(cs.riq), 1, '小计', to_char(cs.riq, 'yyyy-mm-dd')) riq,\n" + 
			"       sum(cs.laimsl) laimsl,\n" + 
			"       sum(cs.caiycs) caiycs,\n" + 
			"       max(cs.caiyzrr) caiyzrr,\n" + 
			"       max(cs.huaycs) huaycx,\n" + 
			"       max(cs.huayzrr) huayzrr\n" + 
			"  from caiyhycstjb cs, gongysb gys, meikxxb mk\n" + 
			" where cs.gongysb_id = gys.id\n" + 
			"   and cs.meikxxb_id = mk.id\n" + 
			"   and cs.riq >= to_date('"+ getBRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and cs.riq <= to_date('"+ getERiq() +"', 'yyyy-mm-dd')\n" + gongys_id +
			" group by rollup(gys.mingc, mk.mingc, cs.riq)";

		ResultSetList rslData = con.getResultSetList(sql);
		String[][] ArrHeader = new String[2][8];
		ArrHeader[0] = new String[]{"供货方", "含矿点", "日期", "来煤量(吨)", "采样环节", "采样环节", "化验环节", "化验环节"};
		ArrHeader[1] = new String[]{"供货方", "含矿点", "日期", "来煤量(吨)", "采样次数", "责任人", "化验次数", "责任人"};
		int[] ArrWidth = new int[] {160, 120, 80, 80, 60, 60, 60, 60};
		
		rt.setTitle(((Visit)this.getPage().getVisit()).getDiancmc()+"入厂煤采样化验次数统计表", ArrWidth);
		rt.setBody(new Table(rslData, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(36);
		rt.body.setHeaderData(ArrHeader);
		rt.body.ShowZero = true;
		for (int i = 1; i <= 8; i ++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		//rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4, "日期："+getBRiq()+"至"+getERiq(), Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 2, "制表：", Table.ALIGN_LEFT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("查询日期："));
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.Binding("BRIQ", "");
		tbr.addField(bdf);
		
		tbr.addText(new ToolbarText("至"));
		DateField edf = new DateField();
		edf.setValue(getERiq());
		edf.Binding("ERIQ", "");
		tbr.addField(edf);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("供应商单位："));
		ComboBox gysdw = new ComboBox();
		gysdw.setTransform("Gongys");
		gysdw.setWidth(120);
		gysdw.setListWidth(150);
		gysdw.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		gysdw.setLazyRender(true);
		tbr.addField(gysdw);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel3(null); //供应商下拉框
			visit.setDropDownBean3(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}