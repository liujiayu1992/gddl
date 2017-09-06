package com.zhiren.pub.jihkj;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jihkj extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// �༭����ѡ�е���

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť���Ӵ�������
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Liclxbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Insert() {
		// Ϊ "����" ��ť���Ӵ�������
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Liclxbean) _list.get(i)).getXXX();
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}

		long mID = -1;
		int mXuh = _value.size() + 1;
		String mMingc = "";
		String mKoujbm = "";
		String mBeiz = "";
		_value.add(new Jihkjbean(mID, mXuh, mMingc, mKoujbm, mBeiz));
		setEditValues(_value);
	}

	private void Delete() {
		// Ϊ "ɾ��" ��ť���Ӵ�������
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Liclxbean) _list.get(i)).getXXX();
		JDBCcon con = new JDBCcon();
		int t;
		int intRow = getEditTableRow();
		List _value = getEditValues();
		if (intRow != -1) {

			if (_value != null) {
				// xuh=((Liclxbean)_value.get(intRow)).getXuh(); //��ǰ�е����

				if (((Jihkjbean) _value.get(intRow)).getId() != -1) {
					String strSql = "delete from jihkjb where id="
							+ ((Jihkjbean) _value.get(intRow)).getId();
					con.getDelete(strSql);
					con.Close();
				}
				_value.remove(intRow); // �����ǰ��
				int c = _value.size(); // ����ʣ��Value��������
				for (int i = intRow; i < c; i++) { // ע�⣺��¼��ǰ�еı���introw�Ǵ��㿪ʼ������
					t = ((Jihkjbean) _value.get(i)).getXuh();
					((Jihkjbean) _value.get(i)).setXuh(t - 1);
				}
			}
		}
		for (int j = 0; j < _value.size(); j++) {
			if (((Jihkjbean) _value.get(j)).getId() != -1) {
				String strSql = "update jihkjb set xuh="
						+ ((Jihkjbean) _value.get(j)).getXuh() + " where id="
						+ ((Jihkjbean) _value.get(j)).getId();
				con.getUpdate(strSql);
			}
		}
		con.Close();
	}

	private void Save() {
		// Ϊ "����" ��ť���Ӵ�������
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Liclxbean) _list.get(i)).getXXX();
		String strSql;
		List _value = getEditValues();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		long mID = -1;
		int mXuh = 0;
		long mDiancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String mMingc = "";
		String mBianm = "";
		String mBeiz = "";
		for (int i = 0; i < _value.size(); i++) {
			mID = ((Jihkjbean) _value.get(i)).getId();
			mXuh = ((Jihkjbean) _value.get(i)).getXuh();
			mMingc = ((Jihkjbean) _value.get(i)).getMingc();
			mBianm = ((Jihkjbean) _value.get(i)).getBianm();
			mBeiz = ((Jihkjbean) _value.get(i)).getBeiz();

			if (mID == -1) {
				mID = Long.parseLong(MainGlobal.getNewID(mDiancxxb_id));
				strSql = "insert into jihkjb (id,xuh,mingc,bianm,beiz) values ("
						+ mID
						+ ","
						+ mXuh
						+ ",'"
						+ mMingc
						+ "','"
						+ mBianm
						+ "','" + mBeiz + "')";
				con.getInsert(strSql);
			} else {
				strSql = "update jihkjb set xuh=" + mXuh + ",mingc='" + mMingc
						+ "',bianm='" + mBianm + "',beiz='" + mBeiz
						+ "' where id=" + mID;
				con.getUpdate(strSql);
			}
		}
		setMsg("����ɹ���");
		con.commit();
		con.Close();
		getSelectData();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
	}

	private Jihkjbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Jihkjbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Jihkjbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			String sql = "select * from jihkjb order by xuh,mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getInt("ID");
				int mxuh = rs.getInt("XUH");
				String mmingc = rs.getString("MINGC");
				String mBianm = rs.getString("BIANM");
				String mbeiz = rs.getString("BEIZ");
				_editvalues
						.add(new Jihkjbean(mid, mxuh, mmingc, mBianm, mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Jihkjbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		return ((Visit) getPage().getVisit()).getList1();
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();

		}
	}
}