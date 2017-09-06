package com.zhiren.dc.monthReport;

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


public class ZhigkjReport extends BasePage {
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

	private String REPORT_NAME_GANGK = "zhigkj";

	private String mstrReportName = "";

	public String getDiancQuanc() {
		return getDiancQuanc(getDiancId());
	}

	public long getDiancId() {
		return getDiancId();
	}

	// �õ���λȫ��
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
			return "�޴˱���";
		}
	}

	private String getRezc() {
		JDBCcon cn = new JDBCcon();
		String sbsql ="select distinct y.xuh,y.diancxxb_id,y.fuid fuid,\n"
			+ "y.mingc,y.quanc,decode(y.zhuangt,0,'ͣ��','ʹ��') zhuangt\n"
			+ "from yuebdwb y order by xuh";

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][] = new String[1][6];
		ArrHeader[0] = new String[] { "���","�糧id", "������λ", "���", "ȫ��", "ʹ��״̬" };
		// �п�
		int ArrWidth[] = new int[] { 60, 80, 120, 120,120, 80 };

		// ����ҳ����
		rt.setTitle("ֱ�ܿھ���", ArrWidth);

		// ����
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.ShowZero = false;

		// ҳ��
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,6,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);

		// ����ҳ��
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