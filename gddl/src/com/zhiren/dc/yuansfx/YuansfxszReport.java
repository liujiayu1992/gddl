package com.zhiren.dc.yuansfx;

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
 * 类名：元素分析设置报表
 */

public class YuansfxszReport extends BasePage implements PageValidateListener {
	
	String[] params = new String[6];
	
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
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql = 
			"select mk.mingc as meikxxb_id,\n" +
			"       decode(ysfx.pinzb_id, '', (select mingc from pinzb where id = "+ params[1] +"), ysfx.pinzb_id) pinzb_id,\n" + 
			"       decode(ysfx.yunsfsb_id, '', (select mingc from yunsfsb where id = "+ params[2] +"), ysfx.yunsfsb_id) yunsfsb_id,\n" + 
			"       decode(ysfx.riq, '', to_char(sysdate, 'yyyy-mm-dd'), ysfx.riq) riq,\n" + 
			"       decode(ysfx.yuansxmb_id, '', (select mingc from yuansxmb where id = "+ params[0] +"), ysfx.yuansxmb_id) yuansxmb_id,\n" + 
			"       ysfx.zhi\n" + 
			"  from (select fx.id,\n" + 
			"               fx.diancxxb_id,\n" + 
			"               fx.meikxxb_id,\n" + 
			"               pz.mingc as pinzb_id,\n" + 
			"               ys.mingc as yunsfsb_id,\n" + 
			"               to_char(fx.riq, 'yyyy-mm-dd') riq,\n" + 
			"               xm.mingc as yuansxmb_id,\n" + 
			"               fx.zhi,\n" + 
			"               fx.zhuangt\n" + 
			"          from yuansfxb fx, diancxxb dc, pinzb pz, yunsfsb ys, yuansxmb xm\n" + 
			"         where fx.diancxxb_id = dc.id\n" + 
			"           and fx.meizb_id = pz.id\n" + 
			"           and fx.yunsfsb_id = ys.id\n" + 
			"           and fx.yuansxmb_id = xm.id\n" + 
			"           and fx.diancxxb_id = "+ visit.getDiancxxb_id() +"\n" + 
			"           and fx.yuansxmb_id = "+ params[0] +"\n" + 
			"           and fx.meizb_id = "+ params[1] +"\n" + 
			"           and fx.yunsfsb_id = "+ params[2] +"\n" + 
			"         order by fx.meikxxb_id) ysfx,\n" + 
			"       (select distinct mk.id, mk.mingc\n" + 
			"          from meikxxb mk, gongysmkglb gm\n" + 
			"         where gm.meikxxb_id = mk.id\n" + 
			"         order by mk.mingc) mk\n" + 
			" where ysfx.meikxxb_id(+) = mk.id\n" + 
			" order by mk.mingc";

		String[][] ArrHeader = new String[1][6];
		ArrHeader[0] = new String[]{"煤矿名称", "品种", "运输方式", "启用日期", "项目名称", "值"};
		int[] ArrWidth = new int[] {180, 90, 90, 90, 120, 90};
		
		ResultSetList rslData =  con.getResultSetList(sql);
		rt.setTitle("元素分析表", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 6, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName().toString());
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			params = cycle.getRequestContext().getParameters("lx");
		}
	}
}