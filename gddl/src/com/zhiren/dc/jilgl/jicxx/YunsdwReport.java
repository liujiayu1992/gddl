package com.zhiren.dc.jilgl.jicxx;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：运输单位报表
 */

public class YunsdwReport extends BasePage implements PageValidateListener {
	
	String[] params = new String[1];
	
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
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql = 
			"select mingc, quanc, danwdz, youzbm, shuih, faddbr,\n" +
			"       weitdlr, kaihyh, zhangh, dianh, chuanz, beiz\n" + 
			"  from yunsdwb\n" + 
			" where diancxxb_id = "+ params[0] +"\n" + 
			" order by mingc";
		
		String[][] ArrHeader = new String[1][12];
		ArrHeader[0] = new String[]{"名称","全称","单位地址","邮政编码","税号","法定代表人","委托代理人","开户银行","帐号","电话","传真","备注"};
		int[] ArrWidth = new int[] {90, 150, 150, 65, 100, 70, 70, 150, 120, 90, 90, 120};
		
		ResultSetList rslData =  con.getResultSetList(sql);
		rt.setTitle("运输单位", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_LEFT);
		rt.body.setColAlign(9, Table.ALIGN_LEFT);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.setColAlign(11, Table.ALIGN_LEFT);
		rt.body.setColAlign(12, Table.ALIGN_LEFT);
		
		rt.setDefaultTitle(1, 5, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(10, 2, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_RIGHT);
		
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
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			params = cycle.getRequestContext().getParameters("lx");
		}
	}
}
