package com.zhiren.guodxw;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 尹佳明
 * 2009-08-24
 * 类名：Caiybq(采样标签)
 */

public class Caiybq extends BasePage {

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
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
	
	private boolean _SearchChephChick = false;

	public void SearchChephButton(IRequestCycle cycle) {
		_SearchChephChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SearchChephChick) {
			_SearchChephChick = false;
			searchCheph();
		}
	}
	
	private void searchCheph() {
		System.out.println(this.getChange());
	}
	
	public String getPrintTable() {
		
		Report rt = new Report();
		
		String[][] ArrBody = new String[2][2];
		ArrBody[0] = new String[] {"车皮号", ""};
		ArrBody[1] = new String[] {"采样编码", ""};
		
		int[] ArrWidth = new int[] {100, 180};
		
		rt.setTitle("采 样 标 签", ArrWidth);
		rt.setBody(new Table(ArrBody, 0, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(30);
		rt.body.setCells(1, 1, 2, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		return rt.getAllPagesHtml();
	}
	
	private void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("车皮号："));
		TextField tfCheph = new TextField();
		tfCheph.setId("tf_cheph");
		tfCheph.setWidth(90);
		tbr.addField(tfCheph);
		tbr.addText(new ToolbarText("-"));
		
		String handler = "function(){															\n" +
		"	if(tf_cheph.getValue() != ''){										\n" +
		"		document.getElementById('CHANGE').value = tf_cheph.getValue();	\n" +
		" 		document.getElementById('SearchChephButton').click();			\n" +
		"	}else{																\n" +
		"		Ext.MessageBox.alert('提示信息','请输入车皮号！');				\n" +
		"	}																	\n" +
		"}																		\n";
		
		ToolbarButton tbrtn = new ToolbarButton(null, "确定", handler);
		tbrtn.setIcon(SysConstant.Btn_Icon_SelSubmit);
		tbr.addItem(tbrtn);
		tbr.addFill();
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
		}
		getSelectData();
	}
	
}
