package com.zhiren.pub.shuzhlfw;

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

public class Shuzhlfw extends BasePage implements PageValidateListener {

	// �༭����ѡ�е���
	private static int _editTableRow = -1;

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private void Save() {
		String strSql;
		List _value = getEditValues();
		JDBCcon con = new JDBCcon();
		long mID = -1;
		long mDiancxxb_ID = -1;
		String mMingc = "";
		double mShangx = 0.000;
		double mXiax = 0.000;
		String mLeib = "";
		String mBeiz = "";
		for (int i = 0; i < _value.size(); i++) {
			mID = ((Shuzhlfwbean) _value.get(i)).getId();
			mDiancxxb_ID = ((Visit) getPage().getVisit()).getDiancxxb_id();
			mMingc = ((Shuzhlfwbean) _value.get(i)).getMingc();
			mShangx = ((Shuzhlfwbean) _value.get(i)).getShangx();
			mXiax = ((Shuzhlfwbean) _value.get(i)).getXiax();
			mLeib = ((Shuzhlfwbean) _value.get(i)).getLeib();
			mBeiz = ((Shuzhlfwbean) _value.get(i)).getBeiz();
			if (mID == -1) {
				mID = Long.parseLong(MainGlobal.getNewID(mDiancxxb_ID));
				strSql = "insert into shuzhlfwb (id,diancxxb_id,mingc,shangx,xiax,leib,beiz) values ("
						+ mID
						+ ","
						+ mDiancxxb_ID
						+ ",'"
						+ mMingc
						+ "',"
						+ mShangx
						+ ","
						+ mXiax
						+ ",'"
						+ mLeib
						+ "','"
						+ mBeiz
						+ "')";
				con.getInsert(strSql);
			} else {
				strSql = "update shuzhlfwb set mingc='" + mMingc + "',shangx="
						+ mShangx + ",xiax=" + mXiax + ",leib='" + mLeib
						+ "',beiz='" + mBeiz + "' where id=" + mID;
				con.getUpdate(strSql);
			}
		}
		con.Close();
		getSelectData();
	}

	private void Delete() {
		// Ϊ "ɾ��" ��ť��Ӵ������
		JDBCcon con = new JDBCcon();
		int intRow = getEditTableRow();
		if (intRow != -1) {
			List _value = getEditValues();
			if (_value != null) {
				if (((Shuzhlfwbean) _value.get(intRow)).getId() != -1) {
					String strSql = "delete from shuzhlfwb where id="
							+ ((Shuzhlfwbean) _value.get(intRow)).getId();
					con.getDelete(strSql);
					con.Close();
				}
				_value.remove(intRow);
				int t = 0;
				int c = _value.size(); // ����ʣ��Value�������
				for (int i = intRow; i < c; i++) { // ע�⣺��¼��ǰ�еı���introw�Ǵ��㿪ʼ������
					t = ((Shuzhlfwbean) _value.get(i)).getXuh();
					((Shuzhlfwbean) _value.get(i)).setXuh(t - 1);
				}
			}
		}
		getSelectData();

	}

	private void Insert() {
		// Ϊ "���" ��ť��Ӵ������
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		int mxuh = _value.size() + 1;
		long mid = -1;
		long mdiancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String mmingc = "";
		double mshangx = 0.000;
		double mxiax = 0.000;
		String mleib = "";
		String mbeiz = "";
		_value.add(new Shuzhlfwbean(mxuh, mid, mdiancxxb_id, mmingc, mshangx,
				mxiax, mleib, mbeiz));
		setEditValues(_value);
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}

	// /////////////////////////////////////////////////////////////////////

	private Shuzhlfwbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Shuzhlfwbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Shuzhlfwbean EditValue) {
		_EditValue = EditValue;
	}

	// /////////////////////////////////////////////////////
	public List getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {
			String sql = "select * from shuzhlfwb  Order By leib,mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			int mxuh = 0;
			while (rs.next()) {
				long mid = rs.getLong("ID");
				long mdiancxxb_id = rs.getLong("DIANCXXB_ID");
				String mmingc = rs.getString("MINGC");
				double mshangx = rs.getDouble("SHANGX");
				double mxiax = rs.getDouble("XIAX");
				String mleib = rs.getString("LEIB");
				String mbeiz = rs.getString("BEIZ");
				_editvalues.add(new Shuzhlfwbean(++mxuh, mid, mdiancxxb_id,
						mmingc, mshangx, mxiax, mleib, mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Shuzhlfwbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		return ((Visit) getPage().getVisit()).getList1();
	}

	// //////////////////////////////////////////////////////////////////////////////////
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();

		}
	}

	// ��Ϣ��
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

}
