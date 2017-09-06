package com.zhiren.pub.liclx;

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

public class Liclx extends BasePage implements PageValidateListener {

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
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Liclxbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
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
		_value.add(new Liclxbean(mID, mXuh, mMingc, mKoujbm, mBeiz));
		setEditValues(_value);
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Liclxbean) _list.get(i)).getXXX();
		JDBCcon con = new JDBCcon();
		int t;
		int intRow = getEditTableRow();
		List _value = getEditValues();
		if (intRow != -1) {

			if (_value != null) {
				// xuh=((Liclxbean)_value.get(intRow)).getXuh(); //当前行的序号

				if (((Liclxbean) _value.get(intRow)).getId() != -1) {
					String strSql = "delete from jihkjb where id="
							+ ((Liclxbean) _value.get(intRow)).getId();
					con.getDelete(strSql);
					con.Close();
				}
				_value.remove(intRow); // 清除当前行
				int c = _value.size(); // 计算剩余Value表的行数
				for (int i = intRow; i < c; i++) { // 注意：记录当前行的变量introw是从零开始计数的
					t = ((Liclxbean) _value.get(i)).getXuh();
					((Liclxbean) _value.get(i)).setXuh(t - 1);
				}
			}
		}
		for (int j = 0; j < _value.size(); j++) {
			if (((Liclxbean) _value.get(j)).getId() != -1) {
				String strSql = "update jihkjb set xuh="
						+ ((Liclxbean) _value.get(j)).getXuh() + " where id="
						+ ((Liclxbean) _value.get(j)).getId();
				con.getUpdate(strSql);
			}
		}
		con.Close();
	}

	private void Save() {
		// 为 "保存" 按钮添加处理程序
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
		String mPiny = "";
		String mBeiz = "";
		ResultSet rs;
		int flag = -1;
		try {
			for (int i = 0; i < _value.size(); i++) {
				mID = ((Liclxbean) _value.get(i)).getId();
				mXuh = ((Liclxbean) _value.get(i)).getXuh();
				mMingc = ((Liclxbean) _value.get(i)).getMingc();
				mPiny = ((Liclxbean) _value.get(i)).getPiny();
				mBeiz = ((Liclxbean) _value.get(i)).getBeiz();
				if (isNull(mMingc)) {
					setMsg("");
					con.rollBack();
					con.Close();
					return;
				}
				if (mID == -1) {
					mID = Long.parseLong(MainGlobal.getNewID(mDiancxxb_id));
					strSql = "select mingc\n" + "  from chezxxb c\n"
							+ " where EXISTS (select *\n"
							+ "          from chezxxb\n"
							+ "         where c.mingc = mingc\n"
							+ "           and mingc = '" + mMingc + "')";
					rs = con.getResultSet(strSql);
					if (rs.next()) {
						setMsg("名称存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						rs.close();
					}
					strSql = "insert into liclxb (id,xuh,mingc,piny,beiz) values ("
							+ mID
							+ ","
							+ mXuh
							+ ",'"
							+ mMingc
							+ "','"
							+ mPiny
							+ "','" + mBeiz + "')";
					flag = con.getInsert(strSql);
				} else {
					strSql = "select mingc\n" + "  from chezxxb c\n"
							+ " where EXISTS (select *\n"
							+ "          from chezxxb\n"
							+ "         where c.mingc = mingc\n"
							+ "           and mingc = '" + mMingc + "')";
					rs = con.getResultSet(strSql);
					if (rs.next()) {
						setMsg("名称存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						rs.close();
					}
					strSql = "update liclxb set xuh=" + mXuh + ",mingc='"
							+ mMingc + "',piny='" + mPiny + "',beiz='" + mBeiz
							+ "' where id=" + mID;
					flag = con.getUpdate(strSql);
				}
				if (flag == -1) {
					System.out.println("数据存储错误!");
					setMsg("保存失败！");
					con.rollBack();
					con.Close();
					return;
				} else {
					setMsg("保存成功！");
					con.commit();
					con.Close();
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

	private Liclxbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Liclxbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Liclxbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			String sql = "select * from liclxb order by xuh,mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getInt("ID");
				int mxuh = rs.getInt("XUH");
				String mmingc = rs.getString("MINGC");
				String mpiny = rs.getString("PINY");
				String mbeiz = rs.getString("BEIZ");
				_editvalues.add(new Liclxbean(mid, mxuh, mmingc, mpiny, mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Liclxbean());
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
