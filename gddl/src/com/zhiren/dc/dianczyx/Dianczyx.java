package com.zhiren.dc.dianczyx;

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

public class Dianczyx extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// 编辑框中选中的行

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

	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		} else if (value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Dianczyxbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getEditValues();
		// ((Dianczyxbean) _list.get(i)).getXXX();
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mid = -1;
		int xuh = _value.size() + 1;
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String mingc = "";
		String bianm = "";
		double lic = 0.0D;
		String beiz = "";
		_value.add(new Dianczyxbean(xuh, mid, diancxxb_id, bianm, mingc, lic,
				beiz));
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Dianczyxbean) _list.get(i)).getXXX();
		int introw = getEditTableRow();
		JDBCcon con = new JDBCcon();
		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {

				con.getDelete(" Delete  From dianczyxb Where id="
						+ ((Dianczyxbean) _list.get(introw)).getId());
				_value.remove(introw);
				int t;
				int c = _value.size();
				for (int i = introw; i < c; i++) {
					t = ((Dianczyxbean) _value.get(i)).getXuh();
					((Dianczyxbean) _value.get(i)).setXuh(t - 1);
				}
			}
		}
		con.Close();
	}

	private void Save() {
		List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		ResultSet rs;
		try {
			for (int i = 0; i < _list.size(); i++) {
				long _id = ((Dianczyxbean) _list.get(i)).getId();
				String bianm = ((Dianczyxbean) _list.get(i)).getBianm();
				String mingc = ((Dianczyxbean) _list.get(i)).getMingc();
				double lic = ((Dianczyxbean) _list.get(i)).getLic();
				String beiz = ((Dianczyxbean) _list.get(i)).getBeiz();
				String Sql = "";
				int flag = -1;
				if (_id == -1) {
					_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
					if (isNull(((Dianczyxbean) _list.get(i)).getMingc())) {
						setMsg("第" + (i + 1) + "行名称不能为空！");
						con.rollBack();
						con.Close();
						return;
					} else {
						// 判断名称是否存在有重复数据
						Sql = "select mingc\n" + "  from dianczyxb c\n"
								+ " where EXISTS (select *\n"
								+ "          from dianczyxb\n"
								+ "         where c.mingc = mingc\n"
								+ "           and mingc = '" + mingc + "')";
						rs = con.getResultSet(Sql);
						if (rs.next()) {
							setMsg("名称存在重复！");
							rs.close();
							con.rollBack();
							con.Close();
							return;
						} else {
							rs.close();
						}
					}
					Sql = " insert  into dianczyxb (ID,diancxxb_id,bianm,mingc,lic,beiz) values("
							+ _id
							+ ","
							+ diancxxb_id
							+ ",'"
							+ bianm
							+ "','"
							+ mingc + "'," + lic + ",'" + beiz + "')";
					flag = con.getInsert(Sql);
				} else {
					Sql = "select mingc\n" + "  from dianczyxb c\n"
							+ " where EXISTS (select *\n"
							+ "          from dianczyxb\n"
							+ "         where c.mingc = mingc\n"
							+ "           and mingc = '" + mingc + "' and id!="
							+ _id + ")";
					rs = con.getResultSet(Sql);
					if (rs.next()) {
						setMsg("名称存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						Sql = " update   dianczyxb  set bianm='" + bianm
								+ "',mingc='" + mingc + "',lic = " + lic
								+ ",beiz= '" + beiz + "' where id=" + _id;
						flag = con.getUpdate(Sql);
					}
				}
				if (flag == -1) {
					setMsg("存储失败！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					setMsg("保存成功！");
					rs.close();
					con.commit();
					getSelectData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
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

	private Dianczyxbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Dianczyxbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dianczyxbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			String sql = "select * from dianczyxb where diancxxb_id = "
					+ visit.getDiancxxb_id() + " order by mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			int xuh = 0;
			while (rs.next()) {
				long mid = rs.getLong("ID");
				long mdiancxxb_id = rs.getLong("DIANCXXB_ID");
				String mbianm = rs.getString("BIANM");
				String mmingc = rs.getString("MINGC");
				double mlic = rs.getDouble("LIC");
				String mbeiz = rs.getString("BEIZ");
				_editvalues.add(new Dianczyxbean(++xuh, mid, mdiancxxb_id,
						mbianm, mmingc, mlic, mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Dianczyxbean());
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
	//
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
