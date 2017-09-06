package com.zhiren.pub.yunsfs;

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

public class Yunsfs extends BasePage implements PageValidateListener {

	private Yunsfsbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Yunsfsbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Yunsfsbean EditValue) {
		_EditValue = EditValue;
	}

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

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getSelectData();
	}

	/**
	 * 点击添加按钮产生的事件.
	 * 
	 */
	private boolean _InsertClick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertClick = true;
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mid = -1;
		String mmingc = "";
		String mpiny = "";
		String mbeiz = "";
		_value.add(new Yunsfsbean(mid, mmingc, mpiny, mbeiz));
		setEditValues(_value);
	}

	/**
	 * 点击删除按钮产生的事件.
	 * 
	 */
	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		JDBCcon con = new JDBCcon();
		int intRow = getEditTableRow();
		if (intRow != -1) {
			List _value = getEditValues();
			if (_value != null) {
				if (((Yunsfsbean) _value.get(intRow)).getId() != -1) {
					String strSql = "delete from yunsfsb where id = "
							+ ((Yunsfsbean) _value.get(intRow)).getId();
					con.getDelete(strSql);
					con.Close();
				}
				_value.remove(intRow);
			}
		}
		getSelectData();
	}

	/**
	 * 点击保存按钮产生的事件.
	 * 
	 */
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		List _list = ((Visit) getPage().getVisit()).getList1();
		try {
			String Sql = "";
			int flag = -1;
			for (int i = 0; i < _list.size(); i++) {
				long _id = ((Yunsfsbean) _list.get(i)).getId();
				String mmingc = ((Yunsfsbean) _list.get(i)).getMingc();
				String mpiny = ((Yunsfsbean) _list.get(i)).getPiny();
				String mbeiz = ((Yunsfsbean) _list.get(i)).getBeiz();
				if (_id == -1) {
					// 判断名称是否空数据
					if (isNull(mmingc)) {
						setMsg("名称不能为空！");
						con.rollBack();
						con.Close();
						return;
					} else {
						// 判断名称是否存在有重复数据
						Sql = "select mingc\n" + "  from yunsfsb c\n"
								+ " where EXISTS (select *\n"
								+ "          from yunsfsb\n"
								+ "         where c.mingc = mingc\n"
								+ "           and mingc = '" + mmingc + "')";
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
					// 判断拼音是否存在有重复数据
					Sql = "select piny\n" + "  from yunsfsb c\n"
							+ " where EXISTS (select *\n"
							+ "          from yunsfsb\n"
							+ "         where c.piny = piny\n"
							+ "           and piny = '" + mpiny + "')";
					rs = con.getResultSet(Sql);
					if (rs.next()) {
						setMsg("拼音存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						rs.close();
					}
					_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
					Sql = " insert  into yunsfsb (ID,MINGC,PINY,BEIZ) values("
							+ _id + ",'" + mmingc + "','" + mpiny + "','"
							+ mbeiz + "')";
					flag = con.getInsert(Sql);
				} else {
					Sql = "select mingc\n" + "  from yunsfsb c\n"
							+ " where EXISTS (select *\n"
							+ "          from yunsfsb\n"
							+ "         where c.mingc = mingc\n"
							+ "           and mingc = '" + mmingc
							+ "' and id!=" + _id + ")";
					rs = con.getResultSet(Sql);
					if (rs.next()) {
						setMsg("名称存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						Sql = " update   yunsfsb  set  MINGC='" + mmingc
								+ "',PINY='" + mpiny + "',,BEIZ='" + mbeiz
								+ "' where id=" + _id;
						flag = con.getUpdate(Sql);
					}
					rs.close();
				}
				if (flag == -1) {
					System.out.println("数据存储错误!");
					setMsg("保存失败！");
					con.rollBack();
					con.Close();
					return;
				} else {
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

	/**
	 * 处理每种按钮点击事件的最后提交.
	 * 
	 */
	public void submit(IRequestCycle cycle) {
		setMsg("");
		if (_RefurbishClick) {
			_RefurbishClick = false;
			Refurbish();
		}
		if (_InsertClick) {
			_InsertClick = false;
			Insert();
		}
		if (_DeleteClick) {
			_DeleteClick = false;
			Delete();
		}
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select * from yunsfsb order by mingc";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getLong("ID");
				String mmingc = rs.getString("MINGC");
				String mpiny = rs.getString("PINY");
				String mbeiz = rs.getString("BEIZ");
				_editvalues.add(new Yunsfsbean(mid, mmingc, mpiny, mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Yunsfsbean());
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
}