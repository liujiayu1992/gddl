package com.zhiren.dc.yunsl;

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

public class Yunslreport extends BasePage {


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
		if (mstrReportName.equals(REPORT_NAME_REZC)) {
			return getRezc();
		} else {
			return "�޴˱���";
		}
	}

	private String getRezc() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		/*
		 * String strNianf=getNianfValue().getValue(); String
		 * strYuef=getYuefValue().getValue();
		 */

		// sbsql.append("select
		// laidrq,shiydwmc,laidr,jiedr,chulr,went,chulqk,chulsm from
		// dianhwhjlb");
		
		sbsql.append(" select pz.mingc as pinzb_id, \n");
		sbsql.append(" sf.mingc as yunsfsb_id, \n");
		sbsql.append(" xx.mingc as meikxxb_id, \n");
		sbsql.append(" yunsl*100 \n");
		sbsql.append("  from yunslb ysl, pinzb pz, yunsfsb sf, meikxxb xx, diancxxb dc \n");
		sbsql.append(" where ysl.diancxxb_id = dc.id and ysl.pinzb_id = pz.id  and ysl.yunsfsb_id = sf.id  and ysl.meikxxb_id = xx.id\n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// �����ͷ����
		 String ArrHeader[][]=new String[1][4];
		 ArrHeader[0]=new String[] {"Ʒ��","���䷽ʽ","ú��","������(%)"};
		// �п�
		 int ArrWidth[]=new int[] {80,80,80,80};

		// ����ҳ����
		rt.setTitle("��������Ϣ��", ArrWidth);
		// rt.setDefaultTitle(1,4,"���λ:"+_Danwqc,Table.ALIGN_LEFT);
		// rt.setValue("tianbrq",strDate +"��"+strEndDate+"��");

		// rt.setDefaultTitle(3,14,strNianf+"��"+strYuef+"��",Table.ALIGN_CENTER);
		// rt.setDefaultTitle(11,2,"��Ȼ16-1��",Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		// rt.body.mergeFixedCol(1);
		rt.body.ShowZero = false;

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,4, "��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(19,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );

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
