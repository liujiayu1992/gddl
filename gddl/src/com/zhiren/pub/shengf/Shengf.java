package com.zhiren.pub.shengf;

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

public class Shengf extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

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

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Shengfbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Shengfbean) _list.get(i)).getXXX();
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mid = -1;
		int mxuh = _value.size() + 1;
		String mmingc = "";
		String mquanc = "";
		String mpiny = "";
		_value.add(new Shengfbean(mid, mxuh, mmingc, mquanc, mpiny));
		setEditValues(_value);
		// setMsg();
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Shengfbean) _list.get(i)).getXXX();
		int introw = getEditTableRow();
		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {
				JDBCcon con = new JDBCcon();
				con.getDelete(" Delete  From shengfb Where id="
						+ ((Shengfbean) _list.get(introw)).getId());
				_value.remove(introw);
			}
		}
		// setMsg();
	}

	private void Save() {
		List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			for (int i = 0; i < _list.size(); i++) {
				long _id = ((Shengfbean) _list.get(i)).getId();
				String Sql = "";
				if (_id == -1) {
					_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
					Sql = " insert  into shengfb (ID,XUH,MINGC,QUANC,PINY) values("
							+ _id
							+ ","
							+ ((Shengfbean) _list.get(i)).getXuh()
							+ ",'"
							+ ((Shengfbean) _list.get(i)).getMingc()
							+ "','"
							+ ((Shengfbean) _list.get(i)).getQuanc()
							+ "','"
							+ ((Shengfbean) _list.get(i)).getPiny()
							+ "')";
					con.getInsert(Sql);
				} else {
					Sql = " update   shengfb  set  XUH="
							+ ((Shengfbean) _list.get(i)).getXuh() + ",MINGC='"
							+ ((Shengfbean) _list.get(i)).getMingc()
							+ "',QUANC='"
							+ ((Shengfbean) _list.get(i)).getQuanc()
							+ "',PINY='"
							+ ((Shengfbean) _list.get(i)).getPiny()
							+ "', where id=" + _id;
					con.getInsert(Sql);
				}
			}
		} finally {
			con.Close();
		}
		// setMsg();
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

	private Shengfbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Shengfbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Shengfbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			String sql = "select * from shengfb order by xuh,mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getLong("ID");
				int mxuh = rs.getInt("XUH");
				String mmingc = rs.getString("MINGC");
				String mquanc = rs.getString("QUANC");
				String mpiny = rs.getString("PINY");
				_editvalues
						.add(new Shengfbean(mid, mxuh, mmingc, mquanc, mpiny));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Shengfbean());
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();

		}
	}

	// private String getProperValue(IPropertySelectionModel _selectModel,
	// int value) {
	// int OprionCount;
	// OprionCount = _selectModel.getOptionCount();
	// for (int i = 0; i < OprionCount; i++) {
	// if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
	// return ((IDropDownBean) _selectModel.getOption(i)).getValue();
	// }
	// }
	// return null;
	// }
	//
	// private long getProperId(IPropertySelectionModel _selectModel, String
	// value) {
	// int OprionCount;
	// OprionCount = _selectModel.getOptionCount();
	//
	// for (int i = 0; i < OprionCount; i++) {
	// if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
	// value)) {
	// return ((IDropDownBean) _selectModel.getOption(i)).getId();
	// }
	// }
	// return -1;
	// }
}
