package com.zhiren.pub.lujxx;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Lujxx extends BasePage implements PageValidateListener {

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

	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		} else if (value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isRepeat(List list, String bean, String field,
			String value, int index) {
		try {
			if (list == null || bean == null || field == null || value == null) {
				return false;
			}
			Class cls = Class.forName(bean);
			Method m = cls.getMethod("get" + field, null);
			String beanValue;
			int result = 0;
			for (int i = index + 1; i < list.size(); i++) {
				beanValue = (String) m.invoke(list.get(i), null);
				if (beanValue.equals(value)) {
					result++;
				}
			}
			if (result >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return false;
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Lujxxbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Lujxxbean) _list.get(i)).getXXX();
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mid = -1;
		int mxuh = _value.size() + 1;
		String mmingc = "";
		String mbianm = "";
		String mpiny = "";
		String mbeiz = "";
		_value.add(new Lujxxbean(mid, mxuh, mmingc, mbianm, mpiny, mbeiz));
		// setMsg();
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Lujxxbean) _list.get(i)).getXXX();
		JDBCcon con = new JDBCcon();
		int introw = getEditTableRow();
		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {
				String sql = "Delete  From lujxxb Where id="
						+ ((Lujxxbean) _list.get(introw)).getId();
				con.getDelete(sql);
				_value.remove(introw);
			}
			int t;
			int c = _value.size();
			for (int i = introw; i < c; i++) {
				t = ((Lujxxbean) _value.get(i)).getXuh();
				((Lujxxbean) _value.get(i)).setXuh(t - 1);
			}
		}
		for (int j = 0; j < _list.size(); j++) {
			String sql = "update lujxxb set xuh="
					+ ((Lujxxbean) _list.get(j)).getXuh() + " where id="
					+ ((Lujxxbean) _list.get(j)).getId();
			con.getUpdate(sql);
		}
		con.Close();
		setMsg("删除数据成功!");
	}

	private void Save() {
		List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String Sql = "";
		try {
			for (int i = 0; i < _list.size(); i++) {
				long _id = ((Lujxxbean) _list.get(i)).getId();
				int mxuh = ((Lujxxbean) _list.get(i)).getXuh();
				String mmingc = ((Lujxxbean) _list.get(i)).getMingc();
				String mbianm = ((Lujxxbean) _list.get(i)).getBianm();
				String mpiny = ((Lujxxbean) _list.get(i)).getPiny();
				String mbeiz = ((Lujxxbean) _list.get(i)).getBeiz();
				int flag = -1;
				if (_id == -1) {
					// 判断是否空数据或重复数据
					if (isNull(((Lujxxbean) _list.get(i)).getMingc())) {
						setMsg("第" + (i + 1) + "行名称不能为空！");
						con.rollBack();
						con.Close();
						return;
					} else {
						Sql = "select mingc\n" + "  from lujxxb l\n"
								+ " where EXISTS (select *\n"
								+ "          from lujxxb\n"
								+ "         where l.mingc = mingc\n"
								+ "           and mingc = '" + mmingc + "')";
						ResultSet rs = con.getResultSet(Sql);
						if (rs.next()) {
							setMsg("第" + (i + 1) + "行编码存在重复！");
							rs.close();
							con.rollBack();
							con.Close();
							return;
						} else {
							rs.close();
						}
					}
					// 判断是否空数据或重复数据
					if (isNull(((Lujxxbean) _list.get(i)).getBianm())) {
						setMsg("第" + (i + 1) + "行编码不能为空！");
						con.rollBack();
						con.Close();
						return;
					} else {
						Sql = "select bianm\n" + "  from lujxxb l\n"
								+ " where EXISTS (select *\n"
								+ "          from lujxxb\n"
								+ "         where l.bianm = bianm\n"
								+ "           and bianm = '" + mbianm + "')";
						ResultSet rs = con.getResultSet(Sql);
						if (rs.next()) {
							setMsg("第" + (i + 1) + "行编码存在重复！");
							rs.close();
							con.rollBack();
							con.Close();
							return;
						} else {
							rs.close();
						}
					}
					// 判断拼音是否存在重复
					Sql = "select piny\n" + "  from lujxxb l\n"
							+ " where EXISTS (select *\n"
							+ "          from lujxxb\n"
							+ "         where l.piny = piny\n"
							+ "           and piny = '" + mpiny + "')";
					ResultSet rs = con.getResultSet(Sql);
					if (rs.next()) {
						setMsg("第" + (i + 1) + "行拼音存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						rs.close();
					}
					_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
					Sql = " insert  into lujxxb (ID,XUH,MINGC,BIANM,PINY,BEIZ) values("
							+ _id
							+ ","
							+ mxuh
							+ ",'"
							+ mmingc
							+ "','"
							+ mbianm
							+ "','" + mpiny + "','" + mbeiz + "')";
					flag = con.getInsert(Sql);
				} else {
					Sql = "select mingc\n" + "  from lujxxb l\n"
							+ " where EXISTS (select *\n"
							+ "          from lujxxb\n"
							+ "         where l.mingc = mingc\n"
							+ "           and mingc = '" + mmingc
							+ "' and id!= " + _id + ")";
					ResultSet rs = con.getResultSet(Sql);
					if (rs.next()) {
						setMsg("第" + (i + 1) + "行名称存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						rs.close();
					}
					Sql = " update   lujxxb  set  XUH=" + mxuh + ",MINGC='"
							+ mmingc + "',BIANM='" + mbianm + "',PINY='"
							+ mpiny + "',BEIZ='" + mbeiz + "' where id=" + _id;
					flag = con.getInsert(Sql);
				}
				if (flag == -1) {
					System.out.println("数据存储错误!");
					setMsg("保存失败！");
					con.rollBack();
					con.Close();
					return;
				} else {
					con.commit();
					setMsg("数据存储成功！");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private Lujxxbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Lujxxbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Lujxxbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			String sql = "select * from lujxxb order by xuh,mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getLong("ID");
				int mxuh = rs.getInt("XUH");
				String mmingc = rs.getString("MINGC");
				String mbianm = rs.getString("BIANM");
				String mpiny = rs.getString("PINY");
				String mbeiz = rs.getString("BEIZ");
				_editvalues.add(new Lujxxbean(mid, mxuh, mmingc, mbianm, mpiny,
						mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Lujxxbean());
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
