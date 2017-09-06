package com.zhiren.dc.yunsl;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunsl extends BasePage implements PageValidateListener {

	// 消息框
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

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Yunslbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Insert(IRequestCycle cycle) {
		boolean zhuangt = false;
		((Visit) getPage().getVisit()).setboolean1(zhuangt);
		((Visit) getPage().getVisit()).setLong2(-1);
		((Visit) getPage().getVisit()).setLong3(-1);
		((Visit) getPage().getVisit()).setLong4(-1);
		cycle.activate("YunslEdition");
	}

	// private void Delete() {
	// // 为 "删除" 按钮添加处理程序
	// List _list = ((Visit) getPage().getVisit()).getList1();
	// // ((Yunslbean) _list.get(i)).getXXX();
	// int introw = getEditTableRow();
	// JDBCcon con = new JDBCcon();
	// List _value = getEditValues();
	// int t;
	// if (introw != -1) {
	// if (_value != null) {
	// con.getDelete(" Delete From xitxxb Where id="
	// + ((Yunslbean) _list.get(introw)).getId());
	// _value.remove(introw);
	//
	// int c = _value.size(); // 计算剩余Value表的行数
	// for (int i = introw; i < c; i++) { // 注意：记录当前行的变量introw是从零开始计数的
	// t = ((Yunslbean) _value.get(i)).getXuh();
	// ((Yunslbean) _value.get(i)).setXuh(t - 1);
	// }
	// }
	// }
	// for (int j = 0; j < _value.size(); j++) {
	// if (((Yunslbean) _value.get(j)).getId() != -1) {
	// String strSql = "update jihkjb set xuh="
	// + ((Yunslbean) _value.get(j)).getXuh() + " where id="
	// + ((Yunslbean) _value.get(j)).getId();
	// con.getUpdate(strSql);
	// }
	// }
	// }

	// private void Save() {
	// List _list = ((Visit) getPage().getVisit()).getList1();
	// JDBCcon con = new JDBCcon();
	// try {
	// for (int i = 0; i < _list.size(); i++) {
	// long _id = ((Yunslbean) _list.get(i)).getId();
	// long diancxxb_id = ((Visit) getPage().getVisit())
	// .getDiancxxb_id();
	// String Sql = "";
	// int flag = -1;
	// String mmingc = ((Yunslbean) _list.get(i)).getMingc();
	// long mzhuangt = getProperId(_IZhuangtModel, ((Yunslbean) _list
	// .get(i)).getZhuangt());
	// ResultSet rs;
	// if (_id == 0) {
	// _id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
	// if (isNull(mmingc)) {
	// setMsg("名称不能为空！");
	// con.rollBack();
	// con.Close();
	// return;
	// } else {
	// // 判断名称是否存在有重复数据
	// Sql = "select mingc\n" + " from chezxxb c\n"
	// + " where EXISTS (select *\n"
	// + " from chezxxb\n"
	// + " where c.mingc = mingc\n"
	// + " and mingc = '" + mmingc + "')";
	// rs = con.getResultSet(Sql);
	// if (rs.next()) {
	// setMsg("名称存在重复！");
	// rs.close();
	// con.rollBack();
	// con.Close();
	// return;
	// } else {
	// rs.close();
	// }
	// }
	// Sql = " insert into xitxxb
	// (ID,XUH,DIANCXXB_ID,MINGC,ZHI,DANW,LEIB,ZHUANGT,BEIZ) values("
	// + _id
	// + ","
	// + ((Yunslbean) _list.get(i)).getXuh()
	// + ","
	// + ((Yunslbean) _list.get(i)).getDiancxxb_id()
	// + ",'"
	// + mmingc
	// + "','"
	// + ((Yunslbean) _list.get(i)).getZhi()
	// + "','"
	// + ((Yunslbean) _list.get(i)).getDanw()
	// + "','"
	// + ((Yunslbean) _list.get(i)).getLeib()
	// + "',"
	// + mzhuangt
	// + ",'"
	// + ((Yunslbean) _list.get(i)).getBeiz() + "')";
	// flag = con.getInsert(Sql);
	// } else {
	// Sql = " update xitxxb set XUH="
	// + ((Yunslbean) _list.get(i)).getXuh()
	// + ",DIANCXXB_ID="
	// + ((Yunslbean) _list.get(i)).getDiancxxb_id()
	// + ",MINGC='" + mmingc + "',ZHI='"
	// + ((Yunslbean) _list.get(i)).getZhi() + "',DANW='"
	// + ((Yunslbean) _list.get(i)).getDanw() + "',LEIB='"
	// + ((Yunslbean) _list.get(i)).getLeib()
	// + "',ZHUANGT=" + mzhuangt + ",BEIZ='"
	// + ((Yunslbean) _list.get(i)).getBeiz()
	// + "' where id=" + _id;
	// flag = con.getInsert(Sql);
	// }
	// if (flag == -1) {
	// setMsg("数据存储失败！");
	// con.rollBack();
	// con.Close();
	// return;
	// } else {
	// con.commit();
	// getSelectData();
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// con.Close();
	// }
	// }

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	// private boolean _DeleteChick = false;
	//
	// public void DeleteButton(IRequestCycle cycle) {
	// _DeleteChick = true;
	// }
	//
	// private boolean _SaveChick = false;
	//
	// public void SaveButton(IRequestCycle cycle) {
	// _SaveChick = true;
	// }

	private boolean _EditionChick = false;

	public void EditionButton(IRequestCycle cycle) {
		_EditionChick = true;
	}

	private boolean _firstpagebutton = false;

	private boolean _uppagebutton = false;

	private boolean _downpagebutton = false;

	private boolean _lastpagebutton = false;

	private boolean _gopagebutton = false;

	public void FirstPageButton(IRequestCycle cycle) {
		_firstpagebutton = true;
	}

	public void UpPageButton(IRequestCycle cycle) {
		_uppagebutton = true;
	}

	public void DownPageButton(IRequestCycle cycle) {
		_downpagebutton = true;
	}

	public void LastPageButton(IRequestCycle cycle) {
		_lastpagebutton = true;
	}

	public void GoPageButton(IRequestCycle cycle) {
		_gopagebutton = true;
	}

	public String getArrayScript() {
		StringBuffer array = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		boolean xianszt;
		if (visit.isJTUser()) {
			xianszt = true;
		} else {
			xianszt = false;
		}
		array.append("var xianszt = " + xianszt + ";\n");
		return array.toString();
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
			ToFirstPage();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert(cycle);
		}
		if (_EditionChick) {
			_EditionChick = false;
			Edition(cycle);
		}
		if (_firstpagebutton) {
			ToFirstPage();
			_firstpagebutton = false;
		}
		if (_uppagebutton) {
			ToUpPage();
			_uppagebutton = false;
		}
		if (_downpagebutton) {
			ToDownPage();
			_downpagebutton = false;
		}
		if (_lastpagebutton) {
			ToLastPage();
			_lastpagebutton = false;
		}

		if (_gopagebutton) {
			GoPage();
			_gopagebutton = false;
		}
		if (ischanged) {
			setMeikxxb_idValue(this.getMeikxxb_idValue());
			this.getMeikxxb_idModels();
			getSelectData();
			ischanged = false;
		}
	}

	private Yunslbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Yunslbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Yunslbean EditValue) {
		_EditValue = EditValue;
	}

	private void Edition(IRequestCycle cycle) {
		// 为 "编辑" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Chezxxbean) _list.get(i)).getXXX();
		int introw = getEditTableRow();
		boolean zhuangt = true;
		if (introw != -1) {
			if (((Yunslbean) _list.get(introw)).getSel() == true) {
				zhuangt = true;
				long shangjdw = -1;
				shangjdw = getGongysDropDownValue().getId();
				long _id = ((Yunslbean) _list.get(introw)).getId();
				long mmeikxxb_id = getProperId(getMeikxxb_idModel(),
						((Yunslbean) _list.get(introw)).getMeikxxb_id());
				long mpinzb_id = getProperId(getPinzb_idModel(), ((Yunslbean) _list
						.get(introw)).getPingzb_id());
				long myunsfsb_id = getProperId(getYunsfsb_idModel(), ((Yunslbean) _list
						.get(introw)).getYunsfsb_id());
				((Visit) getPage().getVisit()).setLong1(_id);
				((Visit) getPage().getVisit()).setLong2(shangjdw);
				((Visit) getPage().getVisit()).setLong3(mmeikxxb_id);
				((Visit) getPage().getVisit()).setLong4(mpinzb_id);
				((Visit) getPage().getVisit()).setLong5(myunsfsb_id);
				((Visit) getPage().getVisit()).setboolean1(zhuangt);
				cycle.activate("YunslEdition");
			}
		} else {
			setMsg("请选择要编辑的数据！");
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		StringBuffer SQL = new StringBuffer("");
		ResultSet rs;
		boolean hasData = false;
		try {
			getPinzb_idModels();
			getYunsfsb_idModels();
			String conditional = "";
			StringBuffer sql = new StringBuffer("");
			int rowOfPage = this.getRowsOfPage();
			if (visit.isJTUser()) {
				if (getGongysDropDownValue().getId() == -1) {
					conditional = "";
					sql
							.append("select ceil(rownum/"
									+ rowOfPage
									+ ") as page,x.* from yunslb x order by x.xuh,x.mingc");
				} else {
					conditional = "where diancxxb_id = "
							+ getGongysDropDownValue().getId() + "";
					sql.append("select ceil(rownum/" + rowOfPage
							+ ") as page,x.*\n");
					sql.append("from xitxxb x\n");
					sql.append("where EXISTS (select *\n");
					sql.append("from (select *\n");
					sql.append("from diancxxb\n");
					sql.append("where fuid = ");
					sql.append(getGongysDropDownValue().getId());
					sql.append("\n");
					sql.append("union\n");
					sql.append("select * from diancxxb where id = ");
					sql.append(getGongysDropDownValue().getId() + ") d\n");
					sql.append("where d.id = x.diancxxb_id)");
				}
			} else {
				if (getGongysDropDownValue().getId() == -1) {
					conditional = "";
				} else {
					conditional = "and " + getGongysDropDownValue().getId()
							+ "";
				}
				sql.append("select ceil(rownum/" + rowOfPage + ") as page,x.* "
						+ " from xitxxb x" + " where zhuangt = 1 ");
				sql
						.append(" and diancxxb_id = " + visit.getDiancxxb_id()
								+ " ");
				sql.append(conditional + "" + " order by xuh, mingc, leib ");
			}
			setSQL(sql);
			rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				int mpage = rs.getInt("PAGE");
				setPages(mpage);
				hasData = true;
			}
			this.setCurrentPage(0);
			this.setTotalPages(0);
			this.setGoPage(0);
			if (hasData) {
				this.setCurrentPage(1);
				this.setGoPage(1);
				SQL.delete(0, SQL.length());
				SQL.append("select count(*) as pages from (");
				SQL.append(sql);
				SQL.append(") t");
				CountPages(SQL);
				this.setEditValues(_editvalues);
				ToFirstPage();
			} else {
				this.setEditValues(_editvalues);
				this.setCurrentPage(1);
				this.setGoPage(1);
				this.setTotalPages(1);
				ToFirstPage();
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		setEditTableRow(-1);
	}

	private List getListValues(ResultSet rs) {
		List _editvalues = new ArrayList();
		try {
			while (rs.next()) {
				long mid = rs.getLong("ID");
				int mxuh = rs.getInt("XUH");
				String mdiancxxb_id = getProperValue(getMeikxxb_idModel(), rs
						.getInt("DIANCXXB_ID"));
				String mpinzb_id = "";
				String myunsfsb_id = "";
				String mmeikxxb_id = "";
				double myunsl = 0.000D;
				int mpage = rs.getInt("APAGE");
				Yunslbean xitxx = new Yunslbean(false, mid, ++mxuh, mdiancxxb_id,
						mpinzb_id, myunsfsb_id, mmeikxxb_id, myunsl);
				setPages(mpage);
				_editvalues.add(xitxx);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _editvalues;
	}

	private boolean ischanged;

	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {
		if (Value != null && getGongysDropDownValue() != null) {
			if (Value.getId() != getGongysDropDownValue().getId()) {
				ischanged = true;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getGongysDropDownModels() {
		Visit visit = ((Visit) getPage().getVisit());
		String sql = "";
		if (visit.isJTUser()) {
			sql = "select id,mingc\n"
					+ "  from diancxxb d\n"
					+ " where EXISTS (select distinct fuid from diancxxb where fuid = d.id)\n"
					+ " order by xuh, mingc";
		} else {
			if (visit.isGSUser()) {
				sql = "select dc.id, dc.mingc\n"
						+ "  from (select id, mingc, fuid\n"
						+ "          from diancxxb d\n"
						+ "         where EXISTS (select distinct fuid from diancxxb where fuid = d.id)\n"
						+ "         order by xuh, mingc) qu,\n"
						+ "       diancxxb dc\n" + " where dc.fuid = qu.id\n"
						+ "   and dc.fuid = " + visit.getDiancxxb_id() + "\n"
						+ " order by dc.xuh, dc.mingc";

			} else {
				sql = "select id, mingc from diancxxb where id = "
						+ visit.getDiancxxb_id();

			}
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, "全部"));
	}

	public IDropDownBean getMeikxxb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getMeikxxb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setMeikxxb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getMeikxxb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getMeikxxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getMeikxxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setMeikxxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getMeikxxb_idModels() {
		// Visit visit = ((Visit) getPage().getVisit());
		String sql = "select id,mingc from diancxxb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, null));
	}

	public IDropDownBean getPinzb_idValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getPinzb_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getPinzb_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong4()) {
					((Visit) getPage().getVisit()).setDropDownBean2(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setPinzb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getPinzb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getPinzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getPinzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setPinzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getPinzb_idModels() {
		String sql = "select z.*\n"
				+ "  from (select 0 as id, decode(1,1,'不可编辑') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1,1,'可以编辑') as mingc from dual) z\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, null));
	}
	
	
	public IDropDownBean getYunsfsb_idValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getYunsfsb_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getYunsfsb_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong4()) {
					((Visit) getPage().getVisit()).setDropDownBean2(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setYunsfsb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getYunsfsb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getYunsfsb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getYunsfsb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setYunsfsb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getYunsfsb_idModels() {
		String sql = "select z.*\n"
				+ "  from (select 0 as id, decode(1,1,'不可编辑') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1,1,'可以编辑') as mingc from dual) z\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, null));
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
			visit.setDropDownBean1(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel3(null);
			getSelectData();

		}
	}

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

	private ResultSet getQueryResult(StringBuffer SQL) {
		ResultSet rs = null;
		JDBCcon con = new JDBCcon();
		rs = con.getResultSet(SQL);
		return rs;
	}

	private StringBuffer m_sql = new StringBuffer("");

	public StringBuffer getSQL() {
		return m_sql;
	}

	public void setSQL(StringBuffer sql) {
		m_sql = sql;
	}

	private int m_pages = 0;

	public int getPages() {
		return m_pages;
	}

	public void setPages(int pages) {
		m_pages = pages;
	}

	private int getRowsOfPage() {
		int rowsOfpage = 19;
		return rowsOfpage;
	}

	private int m_totalpages = 0;

	public int getTotalPages() {
		return m_totalpages;
	}

	public void setTotalPages(int _value) {
		m_totalpages = _value;
	}

	private int m_currentpage = 0;

	public int getCurrentPage() {
		return m_currentpage;
	}

	public void setCurrentPage(int _value) {
		m_currentpage = _value;
	}

	private int m_page = 1;

	public int getGoPage() {
		return m_page;
	}

	public void setGoPage(int page) {
		m_page = page;
	}

	private void ToFirstPage() {
		List _editvalues = new ArrayList();
		this.setEditValues(null);
		ResultSet rs = null;
		StringBuffer SQL = new StringBuffer("");
		int rowsOfpage = this.getRowsOfPage();
		SQL.append("select * from (select ceil(rownum / " + rowsOfpage
				+ ") as apage,t.* from (");
		SQL.append(this.getSQL());
		SQL.append(")t order by t.xuh,t.mingc) where apage = 1");
		rs = this.getQueryResult(SQL);
		_editvalues = getListValues(rs);
		this.setEditValues(_editvalues);
		this.setCurrentPage(1);
		rs = null;
	}

	private void ToUpPage() {
		int currentPage = this.getCurrentPage();
		if (1 < currentPage) {
			// currentPage = currentPage - 1;
			int startPage = currentPage - 1;
			int endPage = currentPage;
			// 显示上一页的内容
			List _editvalues = new ArrayList();
			this.setEditValues(null);
			ResultSet rs = null;
			StringBuffer SQL = new StringBuffer("");
			int rowsOfpage = this.getRowsOfPage();
			SQL.append("select * from (select ceil(rownum / " + rowsOfpage
					+ ") as apage,t.* from (");
			SQL.append(this.getSQL());
			SQL.append(")t order by t.xuh,t.mingc) where apage >= ");
			SQL.append(startPage);
			SQL.append(" and apage < ");
			SQL.append(endPage);
			rs = this.getQueryResult(SQL);
			_editvalues = getListValues(rs);
			this.setEditValues(_editvalues);
			this.setCurrentPage(startPage);
			rs = null;
		} else {
			setMsg("已到达首页！");
		}
	}

	private void ToDownPage() {
		int currentPage = this.getCurrentPage();
		if (currentPage < this.getTotalPages()) {
			int startPage = currentPage;
			int endPage = currentPage + 1;
			// 显示下一页的内容
			List _editvalues = new ArrayList();
			this.setEditValues(null);
			ResultSet rs = null;
			StringBuffer SQL = new StringBuffer("");
			int rowsOfpage = this.getRowsOfPage();
			SQL.append("select * from (select ceil(rownum / " + rowsOfpage
					+ ") as apage,t.* from (");
			SQL.append(this.getSQL());
			SQL.append(")t order by t.xuh,t.mingc) where apage > ");
			SQL.append(startPage);
			SQL.append(" and apage <= ");
			SQL.append(endPage);
			rs = this.getQueryResult(SQL);
			_editvalues = getListValues(rs);
			this.setEditValues(_editvalues);
			this.setCurrentPage(endPage);
		} else {
			setMsg("已到达末尾页！");
		}
	}

	private void ToLastPage() {
		// if (this.getCurrentPage() < this.getTotalPages()) {
		// 显示最后一页的内容
		int startPage = this.getTotalPages() - 1;
		int endPage = this.getTotalPages();
		this.setEditValues(null);
		List _editvalues = new ArrayList();
		this.setEditValues(null);
		ResultSet rs = null;
		StringBuffer SQL = new StringBuffer("");
		int rowsOfpage = this.getRowsOfPage();
		SQL.append("select * from (select ceil(rownum / " + rowsOfpage
				+ ") as apage,t.* from (");
		SQL.append(this.getSQL());
		SQL.append(")t order by t.xuh,t.mingc) where apage > ");
		SQL.append(startPage);
		SQL.append(" and apage <= ");
		SQL.append(endPage);
		rs = this.getQueryResult(SQL);
		_editvalues = getListValues(rs);
		this.setEditValues(_editvalues);
		this.setCurrentPage(this.getTotalPages());
		// }

	}

	private void GoPage() {
		int goPage = this.getGoPage();
		if (1 <= goPage && goPage <= this.getTotalPages()) {
			int startPage = goPage;
			int endPage = goPage + 1;
			this.setEditValues(null);
			List _editvalues = new ArrayList();
			ResultSet rs = null;
			StringBuffer SQL = new StringBuffer("");
			int rowsOfpage = this.getRowsOfPage();
			SQL.append("select * from (select ceil(rownum / " + rowsOfpage
					+ ") as apage,t.* from (");
			SQL.append(this.getSQL());
			SQL.append(")t order by t.xuh,t.mingc) where apage >= ");
			SQL.append(startPage);
			SQL.append(" and apage < ");
			SQL.append(endPage);
			rs = this.getQueryResult(SQL);
			_editvalues = getListValues(rs);
			this.setEditValues(_editvalues);
			this.setCurrentPage(this.getGoPage());
		}

	}

	private void CountPages(StringBuffer sql) {
		JDBCcon conn = new JDBCcon();
		ResultSet rs = null;
		rs = conn.getResultSet(sql);
		try {
			if (rs.next()) {
				this.setTotalPages(rs.getInt("pages") / this.getRowsOfPage()
						+ 1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
