package com.zhiren.pub.caizhfs;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Caizhfsreport extends BasePage {
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
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

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}

	// ***************������Ϣ��******************//
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

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		String sql = "select f.mingc,b.mingc bum_id,l.mingc leibb_id,\n"
				+ "decode(shifzm,1,'��','��') shifzm\n"
				+ "from caizhdyb c, leibb l, bumb b, caizhfsb f\n"
				+ "where c.leibb_id = l.id and c.bum_id = b.id\n"
				+ "and caizhfsb_id = f.id and f.diancxxb_id = "
				+ ((Visit) this.getPage().getVisit()).getDiancxxb_id();

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][] = new String[1][4];
		ArrHeader[0] = new String[] { "������ʽ", "����", "���", "�Ƿ�ת��" };
		// �п�
		int ArrWidth[] = new int[] { 120, 100, 100, 100 };

		// ����ҳ����
		rt.setTitle("���ƻ���ʽ��Ϣ��", ArrWidth);
		// rt.setDefaultTitle(1,4,"���λ:"+danwqc,Table.ALIGN_LEFT);
		// rt.setDefaultTitle(6,4,_NianfValue.getId()+"��"+
		// _YuefValue.getId()+"��",Table.ALIGN_CENTER);
		// rt.setDefaultTitle(11,2,"��Ȼ16-1��",Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 1, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_RIGHT);

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
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

}