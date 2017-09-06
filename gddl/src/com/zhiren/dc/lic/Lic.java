package com.zhiren.dc.lic;

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

public class Lic extends BasePage implements PageValidateListener {

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
		// ((Licbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Licbean) _list.get(i)).getXXX();
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		int xuh = _value.size() + 1;
		long mid = -1;
		long mdiancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String mfaz_id = "";
		String mdaoz_id = "";
		String mliclxb_id = "";
		double mzhi = 0.0D;
		String mbeiz = "";
		_value.add(new Licbean(xuh, mid, mdiancxxb_id, mfaz_id, mdaoz_id,
				mliclxb_id, mzhi, mbeiz));
		setEditValues(_value);
		// setMsg();
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Licbean) _list.get(i)).getXXX();
		int introw = getEditTableRow();
		int t;
		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {
				JDBCcon con = new JDBCcon();
				con.getDelete(" Delete  From licb Where id="
						+ ((Licbean) _list.get(introw)).getId());
				_value.remove(introw);
			}
			int c = _value.size(); // 计算剩余Value表的行数
			for (int i = introw; i < c; i++) { // 注意：记录当前行的变量introw是从零开始计数的
				t = ((Licbean) _value.get(i)).getXuh();
				((Licbean) _value.get(i)).setXuh(t - 1);
			}
		}
		// setMsg();
	}

	private void Save() {
		List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		int flag = -1;
		ResultSet rs;
		try {
			for (int i = 0; i < _list.size(); i++) {// getLujxxb_idValue()
				long _id = ((Licbean) _list.get(i)).getId();
				long mfaz_id = getProperId(getFaz_idModel(), ((Licbean) _list
						.get(i)).getFaz_id());
				long mdaoz_id = getProperId(getDaoz_idModel(), ((Licbean) _list
						.get(i)).getDaoz_id());
				long mliclxb_id = getProperId(getLiclxb_idModel(),
						((Licbean) _list.get(i)).getLiclxb_id());
				String Sql = "";
				if (mfaz_id == -1) {
					setMsg("请选择发站！");
					con.rollBack();
					con.Close();
					return;
				}
				if (mdaoz_id == -1) {
					setMsg("请选择到站！");
					con.rollBack();
					con.Close();
					return;
				}
				if (mliclxb_id == -1) {
					setMsg("请选择类型！");
					con.rollBack();
					con.Close();
					return;
				}
				if (_id == -1) {
					_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
					Sql = "select faz_id, daoz_id, liclxb_id\n"
							+ "  from licb c\n" + " where EXISTS (select *\n"
							+ "          from licb\n"
							+ "         where c.faz_id = faz_id\n"
							+ "           and c.daoz_id = daoz_id\n"
							+ "           and c.liclxb_id = liclxb_id\n"
							+ "           and faz_id = " + mfaz_id + "\n"
							+ "           and daoz_id = " + mdaoz_id + "\n"
							+ "           and liclxb_id = " + mliclxb_id + "\n"
							+ "           and diancxxb_id = " + diancxxb_id
							+ ")";
					rs = con.getResultSet(Sql);
					if (rs.next()) {
						setMsg("数据存在重复！");
						rs.close();
						con.rollBack();
						con.Close();
						return;
					} else {
						rs.close();
						Sql = " insert  into licb (ID,DIANCXXB_ID,FAZ_ID,DAOZ_ID,LICLXB_ID,ZHI,BEIZ) values("
								+ _id
								+ ","
								+ diancxxb_id
								+ ","
								+ mfaz_id
								+ ","
								+ mdaoz_id
								+ ","
								+ mliclxb_id
								+ ","
								+ ((Licbean) _list.get(i)).getZhi()
								+ ",'"
								+ ((Licbean) _list.get(i)).getBeiz() + "')";
						flag = con.getInsert(Sql);
					}
				} else {
					Sql = " update   licb  set  DIANCXXB_ID=" + diancxxb_id
							+ ",FAZ_ID=" + mfaz_id + ",DAOZ_ID=" + mdaoz_id
							+ ",LICLXB_ID=" + mliclxb_id + ",ZHI="
							+ ((Licbean) _list.get(i)).getZhi() + ",BEIZ='"
							+ ((Licbean) _list.get(i)).getBeiz()
							+ "' where id=" + _id;
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
					getSelectData();
				}
			}
		} catch (Exception e) {
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

	private Licbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Licbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Licbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {
			getFaz_idModels();
			getDaoz_idModels();
			getLiclxb_idModels();
			int xuh = 0;
			String sql = "select l.*,z.mingc as faz,dz.mingc as daoz "
					+ " from licb l, chezxxb z, chezxxb dz, liclxb x "
					+ " Where l.faz_id = z.Id" + " And l.daoz_id = dz.Id "
					+ " and x.id = l.liclxb_id" + " and diancxxb_id = "
					+ visit.getDiancxxb_id() + ""
					+ " Order By z.xuh, z.mingc, x.xuh, x.mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getLong("ID");
				long mdiancxxb_id = rs.getLong("DIANCXXB_ID");
				String mfaz_id = rs.getString("FAZ");
				String mdaoz_id = rs.getString("DAOZ");
				String mliclxb_id = getProperValue(getLiclxb_idModel(), rs
						.getInt("LICLXB_ID"));
				double mzhi = rs.getDouble("ZHI");
				String mbeiz = rs.getString("BEIZ");
				_editvalues.add(new Licbean(++xuh, mid, mdiancxxb_id, mfaz_id,
						mdaoz_id, mliclxb_id, mzhi, mbeiz));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Licbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		return ((Visit) getPage().getVisit()).getList1();
	}

	public IDropDownBean getFaz_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getFaz_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setFaz_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getFaz_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getFaz_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setFaz_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getFaz_idModels() {
		String sql = "select distinct c.id, c.mingc as chezmc "
				+ " from chezxxb c, diancdzb g where c.id = g.chezxxb_id "
				+ " and c.leib = '车站' and g.diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " "
				+ " order by c.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, null));
	}

	public IDropDownBean getDaoz_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getDaoz_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setDaoz_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getDaoz_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDaoz_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDaoz_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getDaoz_idModels() {
		String sql = "select distinct c.id, c.mingc as chezmc "
				+ " from chezxxb c, diancdzb g where c.id = g.chezxxb_id "
				+ " and c.leib = '车站' and g.diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " "
				+ " order by c.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, null));
	}

	public IDropDownBean getLiclxb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLiclxb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLiclxb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getLiclxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLiclxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setLiclxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getLiclxb_idModels() {
		String sql = "select id, mingc from liclxb order by xuh, mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, null));
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
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setList1(null);
			getSelectData();
		}
	}

	//
	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
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
