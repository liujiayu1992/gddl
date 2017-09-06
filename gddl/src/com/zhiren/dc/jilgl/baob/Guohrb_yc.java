package com.zhiren.dc.jilgl.baob;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Guohrb_yc extends BasePage {
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

	// 日期控件
	boolean riqichange = false;

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
			riqichange = true;
		}
	}
	
	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String ArrHeader[][] = new String[1][11];
		ArrHeader[0] = new String[] {"供货单位", "煤矿单位", "运输方式", "毛重", "皮重", "净重", "票重", 
									 "盈亏", "运损", "总扣杂", "车数"};
		
		int ArrWidth[] = new int[] {100, 100, 60, 60, 60, 60, 60, 60, 60, 60, 60};
		
		String strGYS = "";
		if (getGongmdwValue().getId() != -1) {
			strGYS = "and fh.gongysb_id =" + getGongmdwValue().getStrId();
		}
		
		String strHQ = "";
		if (getHengqValue().getId() != -1) {
			strHQ = "and cp.zhongchh = '" + getHengqValue().getValue() + "'";
		}
		
		StringBuffer buffer = new StringBuffer();		
	    buffer.append(
	    		"select gmingc, mmingc, ymingc, round_new(maoz, 2), round_new(piz, 2), round_new(jingz, 2), round_new(biaoz, 2), \n" +
	    		"       round_new(yingk, 2), round_new(yuns, 2), round_new(koud, 2), ches\n" +
	    		"  from (select decode(grouping(gys.mingc), 1, '合计', gys.mingc) as gmingc,\n" + 
	    		"               decode(grouping(gys.mingc) + grouping(mk.mingc), 2, '', 1, '小计', mk.mingc) as mmingc,\n" + 
	    		"               decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(ys.mingc), 3, '', 2, '', 1, '小计', ys.mingc) as ymingc,\n" + 
	    		"               sum(fh.maoz) as maoz,\n" + 
	    		"               sum(fh.piz) as piz,\n" + 
	    		"               sum(fh.jingz) as jingz,\n" + 
	    		"               sum(fh.biaoz) as biaoz,\n" + 
	    		"               sum(fh.yingk) as yingk,\n" + 
	    		"               sum(fh.yuns) as yuns,\n" + 
	    		"               sum(fh.koud) as koud,\n" + 
	    		"               sum(fh.ches) as ches\n" + 
	    		"          from fahb    fh,\n" + 
	    		"               chepb   cp,\n" + 
	    		"               gongysb gys,\n" + 
	    		"               meikxxb mk,\n" + 
	    		"               yunsfsb ys\n" + 
	    		"         where fh.id = cp.fahb_id\n" + 
	    		"           and fh.gongysb_id = gys.id\n" + 
	    		"           and fh.meikxxb_id = mk.id\n" + 
	    		"           and fh.yunsfsb_id = ys.id\n" + strGYS +
	    		"           and to_char(cp.zhongcsj, 'yyyy-mm-dd') = '"+getRiqi()+"'\n" + strHQ +
	    		"         group by rollup(gys.mingc, mk.mingc, ys.mingc)\n" + 
	    		"        union\n" + 
	    		"        select decode(grouping(gys.mingc), 1, '累计', gys.mingc) as gmingc,\n" + 
	    		"               decode(grouping(gys.mingc) + grouping(mk.mingc), 2, '', 1, '累计', mk.mingc) as mmingc,\n" + 
	    		"               decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(ys.mingc), 3, '', 2, '', 1, '累计', ys.mingc) as ymingc,\n" + 
	    		"               sum(fh.maoz) as maoz,\n" + 
	    		"               sum(fh.piz) as piz,\n" + 
	    		"               sum(fh.jingz) as jingz,\n" + 
	    		"               sum(fh.biaoz) as biaoz,\n" + 
	    		"               sum(fh.yingk) as yingk,\n" + 
	    		"               sum(fh.yuns) as yuns,\n" + 
	    		"               sum(fh.koud) as koud,\n" + 
	    		"               sum(fh.ches) as ches\n" + 
	    		"          from fahb    fh,\n" + 
	    		"               chepb   cp,\n" +  
	    		"               gongysb gys,\n" + 
	    		"               meikxxb mk,\n" + 
	    		"               yunsfsb ys\n" + 
	    		"         where fh.id = cp.fahb_id\n" + 
	    		"           and fh.gongysb_id = gys.id\n" + 
	    		"           and fh.meikxxb_id = mk.id\n" + 
	    		"           and fh.yunsfsb_id = ys.id\n" + strGYS + 
	    		"           and to_char(cp.zhongcsj, 'yyyy-mm') = '"+getRiqi().substring(0, 7)+"'\n" + 
	    		"           and to_char(cp.zhongcsj, 'dd') between '01' and '"+getRiqi().substring(8, 10)+"'\n" + strHQ +
	    		"         group by rollup(gys.mingc, mk.mingc, ys.mingc)\n" + 
	    		"        having not(grouping(ys.mingc) = 0))\n" + 
	    		" order by decode(gmingc, '累计', 0, '合计', 1, 2), gmingc,\n" + 
	    		"          decode(mmingc, '累计', 0, '小计', 1, 2), mmingc,\n" + 
	    		"          decode(ymingc, '累计', 0, '小计', 1, 2), ymingc");
	    ResultSet rs = con.getResultSet(buffer, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    
	    rsl = con.getResultSetList(buffer.toString());
	    if (!rsl.next()) {
	    	return null;
	    }
	    
		Report rt = new Report();
 
		rt.setBody(new Table(rs, 1, 0, 3));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.setTitle("过 衡 日 报", ArrWidth);
		
		rt.body.setPageRows(20);
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for (int i = 1; i <= 11; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		rt.body.setRowHeight(1, 40);
		
		double yingklj = 0.0;
		double yingkhj = 0.0;
		double yunslj = 0.0;
		double yunshj = 0.0;
		for (int i = 2; i <= rsl.getRows() + 1; i++) {
			double jingz = Double.parseDouble(rt.body.getCellValue(i, 6));
			double piaoz = Double.parseDouble(rt.body.getCellValue(i, 7));
			double yingk = Double.parseDouble(rt.body.getCellValue(i, 8));
			double yuns = Double.parseDouble(rt.body.getCellValue(i, 9));
			if (jingz > piaoz) {
				yingk = jingz - piaoz;
				rt.body.setCellValue(i, 8, CustomMaths.Round_new(yingk, 2) + "");
				rt.body.setCellValue(i, 9, "0");
			} else if (jingz < piaoz) {
				if (Math.abs(jingz - piaoz) < piaoz * 0.01) {
					rt.body.setCellValue(i, 8, "0");
					yuns = piaoz - jingz;
					rt.body.setCellValue(i, 9, CustomMaths.Round_new(yuns, 2) + "");
				} else if (Math.abs(jingz - piaoz) > piaoz * 0.01) {
					yuns = piaoz * 0.01;
					rt.body.setCellValue(i, 9, CustomMaths.Round_new(yuns, 2) + "");
					yingk = piaoz - jingz - yuns;
					rt.body.setCellValue(i, 8, CustomMaths.Round_new(yingk, 2) + "");
				}
			}
			if (rt.body.getCellValue(i, 2).equals("累计")) {
				yingklj = yingklj + yingk;
				yunslj = yunslj + yuns;
				rt.body.setCellValue(2, 8, CustomMaths.Round_new(yingklj, 2) + "");
				rt.body.setCellValue(2, 9, CustomMaths.Round_new(yunslj, 2) + "");
			}
			if (rt.body.getCellValue(i, 2).equals("小计")) {
				yingkhj = yingkhj + yingk;
				yunshj = yunshj + yuns;
				rt.body.setCellValue(3, 8, CustomMaths.Round_new(yingkhj, 2) + "");
				rt.body.setCellValue(3, 9, CustomMaths.Round_new(yunshj, 2) + "");
			}
		}
		
		rt.setDefaultTitle(1, 3, "制表单位："
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, "报表日期：" + getRiqi(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4, "单位：吨、车", Table.ALIGN_RIGHT);
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		
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
		
		tb1.addText(new ToolbarText("过衡日期:"));
		DateField df = new DateField();
		df.setValue(getRiqi());
		df.Binding("RIQI", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("RIQI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商："));
		ComboBox gys = new ComboBox();
		gys.setTransform("GONGMDW");
		gys.setWidth(120);
		gys.setListeners("select:function(){Form0.submit();}");
		tb1.addField(gys);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("衡器："));
		ComboBox hq = new ComboBox();
		hq.setTransform("HENGQ");
		hq.setWidth(60);
		hq.setListeners("select:function(){Form0.submit();}");
		tb1.addField(hq);
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

			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
		}
		getToolbars();
		blnIsBegin = true;
	}

    // 供应商下拉框
	public IDropDownBean getGongmdwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getGongmdwModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getGongmdwModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setGongmdwValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setGongmdwModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGongmdwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIGongmdwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIGongmdwModels() {		
		String sql = "select id, mingc from gongysb order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql, "全部"));
	}
	
    // 衡器下拉框
	public IDropDownBean getHengqValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			if (getHengqModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getHengqModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setHengqValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setHengqModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getHengqModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIHengqModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void getIHengqModels() {		
		String sql = 
			"select id, mingc\n" +
			"  from (select 1 as id, 'A系统' as mingc\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select 2 as id, 'B系统' as mingc from dual)\n" + 
			" order by id";
		
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "全厂"));
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
