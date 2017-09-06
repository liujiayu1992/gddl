package com.zhiren.jt.jiesgl.report.kuangfhs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.MainGlobal;
import com.zhiren.jt.jiesgl.report.pub.Fengsjsd;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：Caigd_gs(采购单_甘肃)
 */

public class Caigd_gs extends BasePage implements PageValidateListener {
	
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
	
	public String getPrintTable() {
		return getSelectData();
	}
	
	public String getSelectData() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		
		String type = visit.getString1();
		String bianm = visit.getString2();
		
		Fengsjsd jsd = new Fengsjsd();
		StringBuffer sb = new StringBuffer();
		sb.append(jsd.getFengsjsd(type, bianm));
		
		_CurrentPage = 1;
		setAllPages(jsd.getAllPages());
		
		return sb.toString();
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
			visit.setString1("");
			visit.setString2("");
		}
		String args = cycle.getRequestContext().getParameter("lx");
		visit.setString1(args.substring(0, args.indexOf(",")));
		visit.setString2(args.substring(args.indexOf(",")+1));
		getSelectData();
	}

}
