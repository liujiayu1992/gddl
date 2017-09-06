package com.zhiren.dc.rulgl.rulqtzb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Feihcx extends BasePage {
	
	private final static String fhbzitem_bianm="FHBZ";//飞灰班值项目对应的编码
	
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	//  
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		
		String sql = 
			"select decode(caiyrq,\n" +
			"              null,\n" + 
			"              decode(jizb_id, null, '全厂', '机组'),\n" + 
			"              to_char(caiyrq, 'yyyy-mm-dd')) as caiyrq,\n" + 
			"       decode(jizb_id, null, decode(caiyrq, null, '', '小计'), jizb_id) as jizb_id,\n" + 
			"       decode(banz,\n" + 
			"              null,\n" + 
			"              decode(jizb_id, null, '', decode(caiyrq, null, '', '小计')),\n" + 
			"              b.mingc) as banz,\n" + 
			"       zhi\n" + 
			"  from (select caiyrq, jizb_id, banz, round_new(avg(zhi) * 100, 1) as zhi\n" + 
			"          from (select r.caiyrq,\n" + 
			"                       j.jizbh jizb_id,\n" + 
			"                       (select it.id from item it where it.id = r.banz) banz,\n" + 
			"                       r.zhi\n" + 
			"                  from item it, itemsort im, rulqtzbb r, jizb j\n" + 
			"                 where r.item_id = it.id\n" + 
			"                   and it.itemsortid = im.itemsortid\n" + 
			"                   and it.bianm = 'FH'\n" + 
			"                   and im.bianm = 'RLHYQTZB'\n" + 
			"                   and r.jizb_id = j.id(+)\n" + 
			"                   and to_char(r.riq,'yyyy-mm-dd')>='"+getRiq1()+"'" + 
			"                   and to_char(r.riq,'yyyy-mm-dd')<='"+getRiq2()+"'\n" + 
			"                   and j.jizbh is not null)\n" + 
			"         group by rollup(caiyrq, jizb_id, banz)\n" + 
			"        union\n" + 
			"        select caiyrq, jizb_id, banz, round_new(avg(zhi) * 100, 1) as zhi\n" + 
			"          from (select r.caiyrq,\n" + 
			"                       j.jizbh jizb_id,\n" + 
			"                       (select it.id from item it where it.id = r.banz) banz,\n" + 
			"                       r.zhi\n" + 
			"                  from item it, itemsort im, rulqtzbb r, jizb j\n" + 
			"                 where r.item_id = it.id\n" + 
			"                   and it.itemsortid = im.itemsortid\n" + 
			"                   and it.bianm = 'FH'\n" + 
			"                   and im.bianm = 'RLHYQTZB'\n" + 
			"                   and r.jizb_id = j.id(+)\n" + 
			"                   and to_char(r.riq,'yyyy-mm-dd')>='"+getRiq1()+"'" + 
			"                   and to_char(r.riq,'yyyy-mm-dd')<='"+getRiq2()+"'\n" + 
			"                   and j.jizbh is not null)\n" + 
			"         group by grouping sets(jizb_id,(caiyrq, jizb_id, banz))\n" + 
			"        having not grouping(caiyrq) = 0) x,\n" + 
			"        (select it.id, it.mingc\n" + 
			"         from item it\n" + 
			"          where it.itemsortid =(select im.id from itemsort im where im.bianm = '"+fhbzitem_bianm+"')\n" + 
			"        ) b\n" + 
			"        where b.id(+) = x.banz\n" + 
			"order by decode(caiyrq,'全厂',3,'机组',2,1),caiyrq,decode(jizb_id,'小计',2,1),jizb_id,decode(banz,'小计',2,1),b.id\n" + 
			"";
		buffer.append(sql);
		ResultSet rs = con.getResultSet(buffer.toString());
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][4];
		ArrWidth = new int[] { 120,120,120,80};
		ArrHeader[0] = new String[] { "日期", "机组", "班值","值(%)"};
		rt.setBody(new Table(rs, 1, 0, 0));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("飞 灰 查 询", ArrWidth);
		rt.title.setRowHeight(2, 50);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		
//		rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
//		rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
//		rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
		rt.body.setPageRows(21);
//		rt.body.mergeFixedCols();
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1, 15, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	boolean riqichange = false;
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String riq) {
		if (this.riq1 != null && !this.riq1.equals(riq)) {
			this.riq1 = riq;
			riqichange = true;
		}
	}
	
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq) {
		if (this.riq2!= null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ1");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		ToolbarButton tb = new ToolbarButton(null, "刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
		}
		getToolbars();
		blnIsBegin = true;

	}

	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
