package com.zhiren.jt.pinz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pinz extends BasePage implements PageValidateListener {

	private int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

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

	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		} else if (value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private void Save() {
		List _value = getEditValues();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		long mid = -1;
		int mxuh = 0;
		String mbianm = "";
		String mmingc = "";
		String mpiny = "";
		String mpinzms = "";
		long mzhuangt = 0;
		String strSql = "";
		ResultSet rs;
		int flag = -1;
		try {
			for (int i = 0; i < _value.size(); i++) {
				mid = ((Pinzbean) _value.get(i)).getId();
				mxuh = ((Pinzbean) _value.get(i)).getXuh();
				mbianm = ((Pinzbean) _value.get(i)).getBianm();
				mmingc = ((Pinzbean) _value.get(i)).getMingc();
				mpiny = ((Pinzbean) _value.get(i)).getPiny();
				mpinzms = ((Pinzbean) _value.get(i)).getPinzms();
				mzhuangt = getProperId(getZhuangtModels(), ((Pinzbean) _value
						.get(i)).getZhuangt());
				long diancxxb_id = visit.getDiancxxb_id();
				if (mid == -1) {
					if (isNull(((Pinzbean) _value.get(i)).getMingc())) {
						setMsg("第" + (i + 1) + "行名称不能为空！");
						con.rollBack();
						con.Close();
						return;
					} else {
						// 判断名称是否存在有重复数据
						strSql = "select mingc\n" + "  from pinzb c\n"
								+ " where EXISTS (select *\n"
								+ "          from pinzb\n"
								+ "         where c.mingc = mingc\n"
								+ "           and mingc = '" + mmingc + "')";
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
					}
					mid = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
					strSql = "insert into pinzb\n"
							+ "  (id, xuh, bianm, mingc, piny, zhuangt, pinzms)\n"
							+ "values\n" + "  (" + mid + ", " + mxuh + ", '"
							+ mbianm + "', '" + mmingc + "','" + mpiny + "', "
							+ mzhuangt + ", '" + mpinzms + "')";
					flag = con.getInsert(strSql);
				} else {
					strSql = "update pinzb set xuh = " + mxuh + ",bianm = '"
							+ mbianm + "',mingc = '" + mmingc + "',piny = '"
							+ mpiny + "',zhuangt = " + mzhuangt + ",pinzms = '"
							+ mpinzms + "' where id = " + mid;
					flag = con.getUpdate(strSql);
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

	private void Delete() {
		JDBCcon con = new JDBCcon();
		int intRow = getEditTableRow();
		if (intRow != -1) {
			List _value = getEditValues();
			if (_value != null) {
				if (((Pinzbean) _value.get(intRow)).getId() != -1) {
					String strSql = "delete pinzb where id = "
							+ ((Pinzbean) _value.get(intRow)).getId();
					con.getDelete(strSql);
					con.Close();
				}
				_value.remove(intRow);
				int c = _value.size();
				for (int i = intRow; i < c; i++) {
					int xuh = ((Pinzbean) _value.get(i)).getXuh();
					((Pinzbean) _value.get(i)).setXuh(xuh - 1);
				}
			}
			for (int j = 0; j < _value.size(); j++) {
				String sql = "update pinzb set xuh="
						+ ((Pinzbean) _value.get(j)).getXuh() + " where id="
						+ ((Pinzbean) _value.get(j)).getId();
				con.getUpdate(sql);

			}
			con.Close();
		} else
			return;
	}

	private void Insert() {
		// // 为 "添加" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mid = -1;
		int mxuh = _value.size() + 1;
		String mbianm = "";
		String mmingc = "";
		String mpiny = "";
		String mzhuangt = "使用";
		String mpinzms = "";
		_value.add(new Pinzbean(mid, mxuh, mbianm, mmingc, mpiny, mzhuangt,
				mpinzms));
		setEditValues(_value);
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Pinzbean) _list.get(i)).getXXX();
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

	private Pinzbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Pinzbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Pinzbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {
			String sql = "select * from pinzb order by xuh,mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getInt("ID");
				int mxuh = rs.getInt("XUH");
				String mbianm = rs.getString("BIANM");
				String mmingc = rs.getString("MINGC");
				String mpiny = rs.getString("PINY");
				String mzhuangt = getProperValue(getZhuangtModel(), rs
						.getInt("ZHUANGT"));
				String mpinzms = rs.getString("PINZMS");
				Pinzbean bean = new Pinzbean(mid, mxuh, mbianm, mmingc, mpiny,
						mzhuangt, mpinzms);
				_editvalues.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Pinzbean());
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

	private IDropDownBean _ZhuangtValue;

	public IDropDownBean getZhuangtValue() {
		if (_ZhuangtValue == null) {
			_ZhuangtValue = (IDropDownBean) _ZhuangtModel.getOption(1);
		}
		return _ZhuangtValue;
	}

	public void setZhuangtValue(IDropDownBean Value) {
		_ZhuangtValue = Value;
	}

	private IPropertySelectionModel _ZhuangtModel;

	public void setZhuangtModel(IPropertySelectionModel value) {
		_ZhuangtModel = value;
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (_ZhuangtModel == null) {
			getZhuangtModels();
		}
		return _ZhuangtModel;
	}

	public IPropertySelectionModel getZhuangtModels() {
		List listZhuangt = new ArrayList();
		listZhuangt.add(new IDropDownBean(0, "停用"));
		listZhuangt.add(new IDropDownBean(1, "使用"));
		_ZhuangtModel = new IDropDownModel(listZhuangt);
		return _ZhuangtModel;
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
}
