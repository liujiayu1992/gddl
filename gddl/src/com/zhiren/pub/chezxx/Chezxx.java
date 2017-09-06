package com.zhiren.pub.chezxx;

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

/**
 * @author 王刚
 * 
 */
public class Chezxx extends BasePage implements PageValidateListener {

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

	private int m_pages = 0;

	public int getPages() {
		return m_pages;
	}

	public void setPages(int pages) {
		m_pages = pages;
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getEditValues();
		// ((Chezxxbean) _list.get(i)).getXXX();
		getSelectData(1);
		// setMsg();
	}

	private void Insert(IRequestCycle cycle) {
		boolean zhuangt = false;
		((Visit) getPage().getVisit()).setboolean1(zhuangt);
		((Visit) getPage().getVisit()).setLong2(-1);
		((Visit) getPage().getVisit()).setLong3(-1);
		cycle.activate("Edition");
	}

	private void Edition(IRequestCycle cycle) {
		// 为 "编辑" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Chezxxbean) _list.get(i)).getXXX();
		int introw = getEditTableRow();
		// List _value = getEditValues();
		// JDBCcon con = new JDBCcon();
		boolean zhuangt = true;
		if (introw != -1) {
			if (((Chezxxbean) _list.get(introw)).getSel() == true) {
				zhuangt = true;
				long _id = ((Chezxxbean) _list.get(introw)).getId();
				long lujxxb_id = getLujxxb_idValue().getId();
				long leib = _LeibValue.getId();
				((Visit) getPage().getVisit()).setLong1(_id);
				((Visit) getPage().getVisit()).setLong2(leib);
				((Visit) getPage().getVisit()).setLong3(lujxxb_id);
				((Visit) getPage().getVisit()).setboolean1(zhuangt);
				cycle.activate("Edition");
			}
		} else {
			setMsg("请选择要编辑的数据！");
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

	// Edition
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
			setLujxxb_idValue(this.getLujxxb_idValue());
			getSelectData(1);
			ischanged = false;
		}
	}

	private Chezxxbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Chezxxbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Chezxxbean EditValue) {
		_EditValue = EditValue;
	}

	private StringBuffer m_sql = new StringBuffer("");

	public StringBuffer getSQL() {
		return m_sql;
	}

	public void setSQL(StringBuffer sql) {
		m_sql = sql;
	}

	public void getSelectData(int leix) {
		// Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		StringBuffer SQL = new StringBuffer("");
		boolean hasData = false;
		String conditional = "";
		getLeibModels();
		getLujxxb_idModels();
		if (leix == 0) {// 第一次加载
			conditional = "";
		} else {
			if (getLujxxb_idValue().getId() == -1) {
				conditional = "";
			} else {
				conditional = "and l.id =" + getLujxxb_idValue().getId();
			}
		}
		try {
			StringBuffer sql = new StringBuffer("");
			int rowOfPage = this.getRowsOfPage();
			sql.append("select ceil(rownum / " + rowOfPage
					+ ") as page, c.*,l.mingc as luj ");
			sql.append("from chezxxb c,lujxxb l,diancdzb d ");
			sql.append("where c.lujxxb_id = l.id ");
			sql.append("and d.chezxxb_id = c.id ");
			sql.append("and d.diancxxb_id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " ");
			sql.append(conditional);
			sql.append("order by c.xuh,c.mingc");
			setSQL(sql);
			ResultSet rs = JDBCcon.getResultSet(sql);
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
				String mbianm = rs.getString("BIANM");
				String mmingc = rs.getString("MINGC");
				String mquanc = rs.getString("QUANC");
				String mpiny = rs.getString("PINY");
				String mlujxxb_id = rs.getString("LUJ");
				String mleib = rs.getString("LEIB");
				String mbeiz = rs.getString("BEIZ");
				int mpage = rs.getInt("APAGE");
				Chezxxbean chez = new Chezxxbean(false, mid, mxuh, mbianm,
						mmingc, mquanc, mpiny, mlujxxb_id, mleib, mbeiz);
				setPages(mpage);
				_editvalues.add(chez);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _editvalues;
	}

	private boolean ischanged;

	public IDropDownBean getLujxxb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getLujxxb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setLujxxb_idValue(IDropDownBean Value) {
		if (Value != null && getLujxxb_idValue() != null) {
			if (Value.getId() != getLujxxb_idValue().getId()) {
				ischanged = true;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getLujxxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getLujxxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setLujxxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getLujxxb_idModels() {
		String sql = "select id, mingc from lujxxb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));
	}

	private IDropDownBean _LeibValue;

	public IDropDownBean getLeibValue() {
		if (_LeibValue == null) {
			_LeibValue = (IDropDownBean) _LeibModel.getOption(1);
		}
		return _LeibValue;
	}

	public void setLeibValue(IDropDownBean Value) {
		if(Value==null){
			getLeibModels();
			Value = (IDropDownBean) getLeibModels();
		}else{
			_LeibValue = Value;
		}
		_LeibValue = Value;
	}

	private IPropertySelectionModel _LeibModel;

	public void setLeibModel(IPropertySelectionModel value) {
		_LeibModel = value;
	}

	public IPropertySelectionModel getLeibModel() {
		if (_LeibModel == null) {
			getLeibModels();
		}
		return _LeibModel;
	}

	public IPropertySelectionModel getLeibModels() {
		List listLeib = new ArrayList();
		listLeib.add(new IDropDownBean(1, "车站"));
		listLeib.add(new IDropDownBean(2, "港口"));
		_LeibModel = new IDropDownModel(listLeib);
		return _LeibModel;
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
			visit.setDropDownBean1(null);
			visit.setList1(null);
			getLujxxb_idModels();
			getLeibModels();
			getSelectData(0);

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

	private ResultSet getQueryResult(StringBuffer SQL) {
		ResultSet rs = null;
		JDBCcon con = new JDBCcon();
		rs = con.getResultSet(SQL);
		return rs;
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
