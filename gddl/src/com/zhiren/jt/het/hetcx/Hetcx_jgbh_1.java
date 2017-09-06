package com.zhiren.jt.het.hetcx;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.zhiren.jt.het.hetcx2.Hetcxbean2_1;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Hetcx_jgbh_1 extends BasePage {
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
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	private String mstrReportName = "";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getHetcx();
		//} else {
		//	return "无此报表";getTreeScript
		//}
	}
	private String getHetcx() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="select hetbh,gongys,to_char(qisrq,'yyyy-mm-dd')qisrq,to_char(guoqrq,'yyyy-mm-dd')guoqrq,xiax,shangx,jij,mingc danw\n" +
		"from (\n" + 
		"select hetb.hetbh,hetb.qisrq,hetb.guoqrq,hetjgb.xiax,hetjgb.shangx,hetjgb.jij,danwb.mingc,gongysb.mingc gongys\n" + 
		"from hetjgb,hetb,danwb,gongysb\n" + 
		"where hetb.gongysb_id=gongysb.id and  hetb_id="+visit.getString1()+" and hetb.id=hetjgb.hetb_id and hetjgb.jijdwid=danwb.id\n" + 
		"union\n" + 
		"select hetb.hetbh,hetb.qisrq,hetb.guoqrq,hetjgb.xiax,hetjgb.shangx,hetjgb.jij,danwb.mingc,gongysb.mingc gongys\n" + 
		"from hetjgb,hetb,danwb,gongysb\n" + 
		"where hetb.gongysb_id=gongysb.id and hetb.fuid="+visit.getString1()+" and hetb.id=hetjgb.hetb_id and hetjgb.jijdwid=danwb.id\n" + 
		")a\n" + 
		"order by a.qisrq";
		ResultSet rs=con.getResultSet(sql);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][8];
		ArrWidth = new int[] { 120,100 ,80, 80,80,80,80,80};
		ArrHeader[0] = new String[] { "合同号","供应商", "起始日期", "过期日期","下限","上限","价格","单位"};
		rt.setBody(new Table(rs,1,0,0));
		rt.setTitle("价   格   变   更", ArrWidth);
//		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.setDefaultTitle(1, 3, "检验日期:" +"",Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		//rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 7, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);

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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (cycle.getRequestContext().getParameter("hetb_id") != null) {//1个参数
			visit.setString1(cycle.getRequestContext().getParameter("hetb_id"));
			
		}else{
			blnIsBegin = false;
			return;
		}
		blnIsBegin = true;
	}
}
