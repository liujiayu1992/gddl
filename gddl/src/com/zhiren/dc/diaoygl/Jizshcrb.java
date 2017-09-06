package com.zhiren.dc.diaoygl;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：机组收耗存日报
 */

public class Jizshcrb extends BasePage implements PageValidateListener {
	
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
	
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String _date) {
		this.date = _date;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
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
		
//		取得各机组的日耗用量和累计用量
		String haoyl_sql = 
			"select dangr.mingc, dangr.meih, leij.meih leij\n" +
			"  from (select jzfz.mingc, sum(mh.fadhy + mh.gongrhy + mh.qity + mh.feiscy) meih\n" + 
			"          from meihyb mh, jizfzb jzfz\n" + 
			"         where mh.jizfzb_id = jzfz.id\n" + 
			"           and mh.rulrq = to_date('"+ getDate() +"', 'yyyy-MM-dd')\n" + 
			"         group by jzfz.mingc\n" + 
			"         order by jzfz.mingc) dangr,\n" + 
			"       (select jzfz.mingc, sum(mh.fadhy + mh.gongrhy + mh.qity + mh.feiscy) meih\n" + 
			"          from meihyb mh, jizfzb jzfz\n" + 
			"         where mh.jizfzb_id = jzfz.id\n" + 
			"           and mh.rulrq >= first_day(to_date('"+ getDate() +"', 'yyyy-MM-dd'))\n" + 
			"           and mh.rulrq <= to_date('"+ getDate() +"', 'yyyy-MM-dd')\n" + 
			"         group by jzfz.mingc\n" + 
			"         order by jzfz.mingc) leij\n" + 
			" where dangr.mingc = leij.mingc";
		ResultSetList haoyl = con.getResultSetList(haoyl_sql);
		
//		取得当日库存油和累计库存油
		String kucy_sql = 
			"select dangrkc.dangrkcy, leijkc.leijkcy\n" +
			"  from (select sum(shcrb.kuc) dangrkcy, shcrb.riq\n" + 
			"          from shouhcrbyb shcrb\n" + 
			"         where shcrb.riq = to_date('"+ getDate() +"', 'yyyy-MM-dd')\n" + 
			"         group by shcrb.riq) dangrkc,\n" + 
			"       (select sum(leij.leijkcy) leijkcy, leij.temp\n" + 
			"          from (select 1 as temp, shcrb.kuc leijkcy\n" + 
			"                  from shouhcrbyb shcrb\n" + 
			"                where shcrb.riq >=first_day(to_date('"+ getDate() +"', 'yyyy-MM-dd'))\n" + 
			"                  and shcrb.riq <= to_date('"+ getDate() +"', 'yyyy-MM-dd')) leij\n" + 
			"         group by leij.temp) leijkc";

		
		ResultSetList kucy = con.getResultSetList(kucy_sql);
		
		String[][] ArrHeader = new String[1][2 + haoyl.getRows() + kucy.getRows()];
		String[][] ArrBody = new String[2][2 + haoyl.getRows() + kucy.getRows()];
		int[] ArrWidth = new int[2 + haoyl.getRows() + kucy.getRows()];
		
		String[][] hejsl = getHejsl(con);
		
		ArrHeader[0][0] = "项目";
		ArrHeader[0][haoyl.getRows() + 1] = "合计";
		ArrBody[0][0] = "当日";
		ArrBody[0][haoyl.getRows() + 1] = hejsl[0][0];
		ArrBody[1][0] = "累计";
		ArrBody[1][haoyl.getRows() + 1] = hejsl[0][1];
		ArrWidth[0] = 100; 
		ArrWidth[haoyl.getRows() + 1] = 100;
		
		int j = 1;
		while(haoyl.next()) {
			ArrHeader[0][j] = haoyl.getString("mingc");
			ArrBody[0][j] = haoyl.getString("meih");
			ArrBody[1][j] = haoyl.getString("leij");
			ArrWidth[j] = 80;
			j ++;
		}
		
		while(kucy.next()) {
			ArrHeader[0][haoyl.getRows() + 2] = "油";
			ArrBody[0][haoyl.getRows() + 2] = kucy.getString("dangrkcy");
			ArrBody[1][haoyl.getRows() + 2] = kucy.getString("leijkcy");
			ArrWidth[haoyl.getRows() + 2] = 100;
		}
		
		rt.setTitle("机组收耗存日报", ArrWidth);
		rt.setBody(new Table(ArrBody, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.ShowZero = true;
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(haoyl.getRows() + 2, Table.ALIGN_RIGHT);
		for (int i = 2; i < haoyl.getRows() + 2; i ++) {
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(haoyl.getRows() + 3, Table.ALIGN_RIGHT);
		rt.setDefaultTitle(1, haoyl.getRows() + kucy.getRows() + 2, "单位：吨", Table.ALIGN_RIGHT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		haoyl.close();
		kucy.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
//	取得当日合计数量和累计合计数量
	public String[][] getHejsl(JDBCcon con) {
		String[][] hej = new String[1][2];
		hej[0][0] = "0"; 
		hej[0][1] = "0";
		String sql = 
			"select dangr.dangrmh, leij.leijmh\n" +
			"  from (select sum(mh.fadhy + mh.gongrhy + mh.qity + mh.feiscy) dangrmh\n" + 
			"          from meihyb mh\n" + 
			"         where mh.rulrq = to_date('"+ getDate() +"', 'yyyy-MM-dd')\n" + 
			"         group by mh.rulrq) dangr,\n" + 
			"       (select sum(leijmh) leijmh\n" + 
			"          from (select (mh.fadhy + mh.gongrhy + mh.qity + mh.feiscy) leijmh, 1 as temp\n" + 
			"                  from meihyb mh\n" + 
			"                 where mh.rulrq >= first_day(to_date('"+ getDate() +"', 'yyyy-MM-dd'))\n" + 
			"                   and mh.rulrq <= to_date('"+ getDate() +"', 'yyyy-MM-dd'))\n" + 
			"         group by temp) leij";

		ResultSetList rsl = con.getResultSetList(sql);
		while(rsl.next()) {
			hej[0][0] = rsl.getString("dangrmh");
			hej[0][1] = rsl.getString("leijmh");
		}
		return hej;
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("选择日期："));
		DateField df = new DateField();
		df.setValue(getDate());
		df.Binding("Date", "");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
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
			setDate(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}
