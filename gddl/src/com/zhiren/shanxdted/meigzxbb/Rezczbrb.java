package com.zhiren.shanxdted.meigzxbb;

import java.sql.ResultSet;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rezczbrb extends BasePage implements PageValidateListener {
	
	public boolean getRaw() {
		return true;
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

	// ***************设置消息框******************//
	private String _msg;
	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	public String getPrintTable(){
		return getZhiltz();
	}

	private String getZhiltz() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		buffer.append(

				"select y.mingc,r.ruc,r.rul,r.cz,y.cz,r.rucl,ruch from (\n" +
				"select d.mingc,decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/4.1816*1000,0)) ruc,\n" + 
				"       decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil)/4.1816*1000,0)) rul,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/4.1816*1000,0))-\n" + 
				"       decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil)/4.1816*1000,0)) cz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz),2)) rucl,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz),2)) ruch\n" + 
				"  from fahb f,zhilb z,diancxxb d,rulmzlb r\n" + 
				" where f.zhilb_id=z.id\n" + 
				"   and f.diancxxb_id=d.id\n" + 
				"   and f.daohrq=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
				"   and f.daohrq=r.rulrq(+)\n" + 
				"   and f.diancxxb_id=r.diancxxb_id(+)\n" + 
				"group by d.mingc,r.rulrq\n" + 
				") r,\n" + 
				"(\n" + 
				"select d.mingc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/4.1816*1000,0))-\n" + 
				"       decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil)/4.1816*1000,0)) cz\n" + 
				"  from fahb f,zhilb z,diancxxb d,rulmzlb r\n" + 
				" where f.zhilb_id=z.id\n" + 
				"   and f.diancxxb_id=d.id\n" + 
				"   and f.daohrq>=trunc(to_date('"+getRiqi()+"','yyyy-mm-dd'),'mm')\n" + 
				"   and f.daohrq<=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
				"   and f.daohrq=r.rulrq(+)\n" + 
				"   and f.diancxxb_id=r.diancxxb_id(+)\n" + 
				"group by d.mingc\n" + 
				") y\n" + 
				" where r.mingc(+)=y.mingc");
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][7];

		ArrHeader[0] = new String[] {"电厂|指标","入厂热值","入炉热值","日热值差","月热值差","硫%","灰%"};
		int[] ArrWidth = new int[7];

		ArrWidth = new int[] { 80, 80, 80, 80, 80, 80, 80};

		rt.setTitle("热值差指标日报表(" + getRiqi() + ")", ArrWidth);
//		rt.title.setRowHeight(2, 40);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
//		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiqi(null);
		}
		getSelectData();
	}
	
	// 绑定日期
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
		}

	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			getZhiltz();
			_RefurbishChick = false;
		}
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);

		setToolbar(tb1);

	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
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