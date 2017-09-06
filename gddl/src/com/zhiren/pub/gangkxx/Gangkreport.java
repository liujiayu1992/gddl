package com.zhiren.pub.gangkxx;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;

import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Gangkreport extends BasePage {
	/*
	 * private String FormatDate(Date _date) { if (_date == null) { return
	 * MainGlobal.Formatdate("yyyy-MM-dd", new Date()); } return
	 * MainGlobal.Formatdate("yyyy-MM-dd", _date); }
	 * 
	 * private static Date _riqValue = new Date();
	 * 
	 * public Date getRiq() { return _riqValue; }
	 * 
	 * public void setRiq(Date _date) { _riqValue = _date; }
	 * 
	 * private static Date _riqEndValue = new Date();
	 * 
	 * public Date getEndRiq() { return _riqEndValue; }
	 * 
	 * public void setEndRiq(Date _date) { _riqEndValue = _date; }
	 */

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

	private String REPORT_NAME_GANGK = "gangk";

	private String mstrReportName = "";

	public String getDiancQuanc() {
		return getDiancQuanc(getDiancId());
	}

	public long getDiancId() {
		/*
		 * if (isGongsUser()){ return getDiancmcValue().getId(); }else{ return
		 * ((Visit)getPage().getVisit()).getDiancxxbId(); }
		 */
		return getDiancId();
	}

	// 得到单位全称
	public String getDiancQuanc(long diancxxbID) {
		String _DiancQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ diancxxbID);
			while (rs.next()) {
				_DiancQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.Close();
		return _DiancQuanc;
	}

	public String getPrintTable() {
		if (mstrReportName.equals(REPORT_NAME_GANGK)) {
			return getRezc();
		} else {
			return "无此报表";
		}
	}

	private String getRezc() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select  c.xuh,   \n");
		sbsql.append(" c.bianm, \n");
		sbsql.append(" c.mingc, \n");
		sbsql.append(" c.quanc, \n");
		sbsql.append(" c.piny \n");
//		sbsql.append(" l.mingc, \n");
//		sbsql.append(" c.leib \n");
		sbsql.append("from chezxxb c ,lujxxb l \n");
		sbsql.append(" where c.lujxxb_id = l.id and c.leib = '港口' \n");
		sbsql.append(" order by xuh \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[1][5];
		ArrHeader[0] = new String[] { "序号", "编码", "简称", "全称", "拼音" };
		// 列宽
		int ArrWidth[] = new int[] { 60, 80, 120, 120, 120 };

		// 设置页标题
		rt.setTitle("港口信息表", ArrWidth);
		// rt.setDefaultTitle(1,4,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		// rt.setValue("tianbrq",strDate +"年"+strEndDate+"月");

		// rt.setDefaultTitle(3,14,strNianf+"年"+strYuef+"月",Table.ALIGN_CENTER);
		// rt.setDefaultTitle(11,2,"调然16-1表",Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		// rt.body.mergeFixedCol(1);
		rt.body.ShowZero = false;

		// 页脚
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,7,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
//		 rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(19,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);

		}
		String[] param = null;
		if (cycle.getRequestContext().getParameters("lx") != null) {
			param = cycle.getRequestContext().getParameters("lx");
			if (param != null) {
				mstrReportName = param[0];
			}
		}

	}
}