package com.zhiren.pub.reny;

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


public class Renyxxreport extends BasePage {

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

	private String REPORT_NAME_REZC = "rezc";

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
		if (mstrReportName.equals(REPORT_NAME_REZC)) {
			return getRezc();
		} else {
			return "无此报表";
		}
	}

	private String getRezc() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select mingc,   \n");
		sbsql.append(" quanc, \n");
		sbsql.append(" xingb, \n");
		sbsql.append(" decode(zhuangt,1,'是','否') zhuangt ,\n");
		sbsql.append(" yiddh, \n");
		sbsql.append(" guddh, \n");
		sbsql.append(" chuanz, \n");
		sbsql.append(" youzbm, \n");
		sbsql.append(" email, \n");
		sbsql.append(" lianxdz \n");
		sbsql.append("  from  renyxxb \n");
		sbsql.append(" where diancxxb_id = \n");
		sbsql.append(visit.getDiancxxb_id());
		sbsql.append(" order by mingc \n");
		

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] { "用户名", "姓名", "姓别", "是否可登陆", "移动电话", "固定电话", "传真", "邮政编码", "Email", "联系地址" };
		// 列宽
		int ArrWidth[] = new int[] { 60, 60, 40, 40,100,80,80,80,85,140 };

		// 设置页标题
		rt.setTitle("人员信息表", ArrWidth);
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
		 rt.setDefautlFooter(1, 10, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
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
