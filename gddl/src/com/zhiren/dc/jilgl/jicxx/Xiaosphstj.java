package com.zhiren.dc.jilgl.jicxx;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Xiaosphstj extends BasePage {
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
		return getSelectData();
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

	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	public String getYuef() {
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}
	
	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String ArrHeader[][] = new String[2][13];
		ArrHeader[0] = new String[] {"序号", "单位", "矿名", getYuef() + "月", getYuef() + "月", getYuef() + "月", getYuef() + "月", 
									 getYuef() + "月", "01-" + getYuef() + "累计", "01-" + getYuef() + "累计", "01-" + getYuef() + "累计", 
									 "01-" + getYuef() + "累计", "01-" + getYuef() + "累计"};
		
		ArrHeader[1] = new String[] {"序号", "单位", "矿名", "进煤量(吨)", "回收支数", "回收量(吨)", "回收率%", "超欠量(吨)", 
				 					 "进煤量(吨)", "回收支数", "回收量(吨)", "回收率%", "超欠量(吨)"};
		
		int ArrWidth[] = new int[] {40, 100, 100, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
		
		StringBuffer buffer = new StringBuffer();		
	    buffer.append(
	    		"select 0," +
	    		"       decode(a.mid, 0, '合计', 1, '小计', gys.mingc),\n" +
	    		"       decode(a.mid, 0, '合计', 1, '小计', mk.mingc),\n" + 
	    		"       b.jinml,\n" + 
	    		"       c.huiszs,\n" + 
	    		"       d.huisl,\n" + 
	    		"       round_new(decode(b.jinml, 0, 0, d.huisl / b.jinml * 100), 2),\n" + 
	    		"       d.huisl - b.jinml,\n" + 
	    		"       e.jinml,\n" + 
	    		"       f.huiszs,\n" + 
	    		"       g.huisl,\n" + 
	    		"       round_new(decode(e.jinml, 0, 0, g.huisl / e.jinml * 100), 2),\n" + 
	    		"       g.huisl - e.jinml\n" + 
	    		"  from (select decode(grouping(gid), 1, 0, gid) as gid,\n" + 
	    		"               decode(grouping(gid) + grouping(mid), 2, 0, 1, 1, mid) as mid\n" + 
	    		"          from (select gys.id as gid, mk.id as mid\n" + 
	    		"                  from fahb fh, gongysb gys, meikxxb mk\n" + 
	    		"                 where fh.gongysb_id = gys.id\n" + 
	    		"                   and fh.meikxxb_id = mk.id\n" + 
	    		"                   and to_char(fh.daohrq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"                   and to_char(fh.daohrq, 'mm') between '01' and '"+getYuef()+"'\n" + 
	    		"                union\n" + 
	    		"                select gys.id as gid, mk.id as mid\n" + 
	    		"                  from meitxspjb pj, gongysb gys, meikxxb mk\n" + 
	    		"                 where pj.gongysb_id = gys.id\n" + 
	    		"                   and pj.meikxxb_id = mk.id\n" + 
	    		"                   and to_char(pj.riq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"                   and to_char(pj.riq, 'mm') between '01' and '"+getYuef()+"')\n" + 
	    		"         group by rollup(gid, mid)) a,\n" + 
	    		"       (select decode(grouping(gys.id), 1, 0, gys.id) as gid,\n" + 
	    		"               decode(grouping(gys.id) + grouping(mk.id), 2, 0, 1, 1, mk.id) as mid,\n" + 
	    		"               sum(fh.maoz - fh.piz) as jinml\n" + 
	    		"          from fahb fh, gongysb gys, meikxxb mk\n" + 
	    		"         where fh.gongysb_id = gys.id\n" + 
	    		"           and fh.meikxxb_id = mk.id\n" + 
	    		"           and to_char(fh.daohrq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"           and to_char(fh.daohrq, 'mm') = '"+getYuef()+"'\n" + 
	    		"         group by rollup(gys.id, mk.id)) b,\n" + 
	    		"       (select decode(grouping(gid), 1, 0, gid) as gid,\n" + 
	    		"               decode(grouping(gid) + grouping(mid), 2, 0, 1, 1, mid) as mid,\n" + 
	    		"               sum(num) as huiszs\n" + 
	    		"          from (select pj.gongysb_id as gid, pj.meikxxb_id as mid, 1 as num\n" + 
	    		"                  from meitxspjb pj, gongysb gys, meikxxb mk\n" + 
	    		"                 where pj.gongysb_id = gys.id\n" + 
	    		"                   and pj.meikxxb_id = mk.id\n" + 
	    		"                   and to_char(pj.riq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"                   and to_char(pj.riq, 'mm') = '"+getYuef()+"')\n" + 
	    		"         group by rollup(gid, mid)) c,\n" + 
	    		"       (select decode(grouping(gys.id), 1, 0, gys.id) as gid,\n" + 
	    		"               decode(grouping(gys.id) + grouping(mk.id), 2, 0, 1, 1, mk.id) as mid,\n" + 
	    		"               sum(pj.shul) as huisl\n" + 
	    		"          from meitxspjb pj, gongysb gys, meikxxb mk\n" + 
	    		"         where pj.gongysb_id = gys.id\n" + 
	    		"           and pj.meikxxb_id = mk.id\n" + 
	    		"           and to_char(pj.riq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"           and to_char(pj.riq, 'mm') = '"+getYuef()+"'\n" + 
	    		"         group by rollup(gys.id, mk.id)) d,\n" + 
	    		"       (select decode(grouping(gys.id), 1, 0, gys.id) as gid,\n" + 
	    		"               decode(grouping(gys.id) + grouping(mk.id), 2, 0, 1, 1, mk.id) as mid,\n" + 
	    		"               sum(fh.maoz - fh.piz) as jinml\n" + 
	    		"          from fahb fh, gongysb gys, meikxxb mk\n" + 
	    		"         where fh.gongysb_id = gys.id\n" + 
	    		"           and fh.meikxxb_id = mk.id\n" + 
	    		"           and to_char(fh.daohrq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"           and to_char(fh.daohrq, 'mm') between '01' and '"+getYuef()+"'\n" + 
	    		"         group by rollup(gys.id, mk.id)) e,\n" + 
	    		"       (select decode(grouping(gid), 1, 0, gid) as gid,\n" + 
	    		"               decode(grouping(gid) + grouping(mid), 2, 0, 1, 1, mid) as mid,\n" + 
	    		"               sum(num) as huiszs\n" + 
	    		"          from (select pj.gongysb_id as gid, pj.meikxxb_id as mid, 1 as num\n" + 
	    		"                  from meitxspjb pj, gongysb gys, meikxxb mk\n" + 
	    		"                 where pj.gongysb_id = gys.id\n" + 
	    		"                   and pj.meikxxb_id = mk.id\n" + 
	    		"                   and to_char(pj.riq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"                   and to_char(pj.riq, 'mm') between '01' and '"+getYuef()+"')\n" + 
	    		"         group by rollup(gid, mid)) f,\n" + 
	    		"       (select decode(grouping(gys.id), 1, 0, gys.id) as gid,\n" + 
	    		"               decode(grouping(gys.id) + grouping(mk.id), 2, 0, 1, 1, mk.id) as mid,\n" + 
	    		"               sum(pj.shul) as huisl\n" + 
	    		"          from meitxspjb pj, gongysb gys, meikxxb mk\n" + 
	    		"         where pj.gongysb_id = gys.id\n" + 
	    		"           and pj.meikxxb_id = mk.id\n" + 
	    		"           and to_char(pj.riq, 'yyyy') = '"+getNianf()+"'\n" + 
	    		"           and to_char(pj.riq, 'mm') between '01' and '"+getYuef()+"'\n" + 
	    		"         group by rollup(gys.id, mk.id)) g,\n" + 
	    		"       gongysb gys,\n" + 
	    		"       meikxxb mk\n" + 
	    		" where a.gid = b.gid(+)\n" + 
	    		"   and a.gid = c.gid(+)\n" + 
	    		"   and a.gid = d.gid(+)\n" + 
	    		"   and a.gid = e.gid(+)\n" + 
	    		"   and a.gid = f.gid(+)\n" + 
	    		"   and a.gid = g.gid(+)\n" + 
	    		"   and a.gid = gys.id(+)\n" + 
	    		"   and a.mid = b.mid(+)\n" + 
	    		"   and a.mid = c.mid(+)\n" + 
	    		"   and a.mid = d.mid(+)\n" + 
	    		"   and a.mid = e.mid(+)\n" + 
	    		"   and a.mid = f.mid(+)\n" + 
	    		"   and a.mid = g.mid(+)\n" + 
	    		"   and a.mid = mk.id(+)\n" + 
	    		" order by decode(a.gid, 0, 1, 0), a.gid, decode(a.mid, 1, 1, 0), a.mid");
	    ResultSet rs = con.getResultSet(buffer, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    
	    rsl = con.getResultSetList(buffer.toString());
	    if (!rsl.next()) {
	    	return null;
	    }
	    
		Report rt = new Report();
 
		rt.setBody(new Table(rs, 2, 0, 3));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.setTitle(getNianf() + "年01月-" + getYuef() + "月份销售票回收情况统计表", ArrWidth);
		
		rt.body.setPageRows(20);
		
		int a = 1;
		for (int i = 3; i < 100000; i++) {
			if (rt.body.getCellValue(i, 2).equals("合计")) {
				rt.body.merge(i, 2, i, 3);
				break;
			}
			rt.body.setCellValue(i, 1, a + "");
			
			if (rt.body.getCellValue(i, 2).equals("小计")) {
				rt.body.merge(i, 2, i, 3);
				a++;
			} 
		}
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for (int i = 1; i <= 13; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		for (int i = 0; i <= 2; i++) {
			rt.body.setRowHeight(i, 40);
		}
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
				
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");

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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);

			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
		}
		getToolbars();
		blnIsBegin = true;
	}

    // 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean5((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean5() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean6((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean6() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(_value);
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
}
