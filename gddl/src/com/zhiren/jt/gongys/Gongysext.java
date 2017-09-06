package com.zhiren.jt.gongys;

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

public class Gongysext extends BasePage implements PageValidateListener {

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
	
	private String _mingc;
	
	public void setMingc(String mingc){
		_mingc = mingc;
	}
	
	public String getMingc(){
		return _mingc;
	}
	
	private String _bianm;
	
	public void setBianm(String bianm){
		_bianm = bianm;
	}
	
	public String getBianm(){
		return _bianm;
	}
	
	
	private void Insert(IRequestCycle cycle) {
		Visit visit = ((Visit) getPage().getVisit());
		String lianpID = "-1";
		visit.setboolean1(false);
		visit.setboolean2(false);
		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
		visit.setString1(lianpID);
		cycle.activate("GongysChakext");
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
//		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Gongysbean) _list.get(i)).getXXX();
		getSelectData(1);
		// setMsg();
	}
	
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
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

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
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
		if (_InsertChick) {
			_InsertChick = false;
			Insert(cycle);
		}
	}

	private Gongysbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Gongysbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Gongysbean EditValue) {
		_EditValue = EditValue;
	}
	
	public void chakSelectAction(IRequestCycle cycle) {
		Visit visit = ((Visit) getPage().getVisit());
//		List _list = ((Visit) getPage().getVisit()).getList1();
		Object obj[] = cycle.getServiceParameters();
		String lianpID = "-1";
		if (obj.length > 0) {
			lianpID = (String)obj[0];
		}
//		int introw = getEditTableRow();
		boolean chak = true;
		visit.setboolean1(chak);
		visit.setboolean2(true);
//		long fuid = ((Gongysbean) _list.get(introw)).getFuid();
		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
		String id = (String) cycle.getRequestContext().getRequest().getAttribute("id");
//		visit.setLong2(fuid);
		visit.setString1(id);
		cycle.activate("GongysChakext");
	}
	
	
	public void xiugSelectAction(IRequestCycle cycle) {
		Visit visit = ((Visit) getPage().getVisit());
		Object obj[] = cycle.getServiceParameters();
		String lianpID = "-1";
		if (obj.length > 0) {
			lianpID = (String) obj[0];
		}
		boolean xiug = false;
		visit.setboolean2(true);
		visit.setboolean1(xiug);
		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
		String id = (String) cycle.getRequestContext().getRequest().getAttribute("id");
		visit.setString1(id);
		cycle.activate("GongysChakext");
	}

	public void getSelectData(int leix) {
//		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		StringBuffer sql = new StringBuffer("");
		int rowOfPage = this.getRowsOfPage();
		StringBuffer SQL = new StringBuffer("");
		boolean hasData = false;
		try {
			String Strshengf = "";
			String Strgongslb = "";
			String Strmingc = "";
			String Strbianm = "";
			if (leix == 0) {// 第一次加载
				Strshengf = "";
				Strgongslb = "";
			} else {
				if (getShengfValue().getId() == -1) {
					Strshengf = "";
				} else {
					Strshengf = "and shengfb.id =" + getShengfValue().getId();
				}
				if (getGongyslbValue().getId() == -1) {
					Strgongslb = "";
				} else {
					if (getGongyslbValue().getId() == 0) {
						Strgongslb = " and fuid = 0";
					} else {
						Strgongslb = " and fuid != 0";
					}
				}
				if(getMingc().equals("")||getMingc().equals(null)){
					Strmingc = "";
				}else{
					Strmingc = " and mingc like '%"+getMingc()+"%'";
				}
				if(getBianm().equals("")||getBianm().equals(null)){
					Strbianm = "";
				}else{
					Strbianm = " and bianm like '%"+getBianm()+"%'";
				}
			}
			sql.append("select ceil(rownum/" + rowOfPage
					+ ") as page,gongysb.id as id,gongysb.xuh as xuh,");
			sql.append(" gongysb.bianm as bianm," + " gongysb.quanc as quanc,");
			sql
					.append(" gongysb.mingc as mingc,"
							+ " shengfb.quanc as shengf,");
			sql.append("fuid as fuid,");
			sql.append(" decode(fuid, 0, '大供应商', '小供应商') as leib");
			sql.append(" from gongysb, shengfb");
			sql.append(" where shengfb.id = gongysb.shengfb_id " + Strshengf
					+ " " + Strgongslb + " "+Strmingc+" "+Strbianm+"");
			sql.append(" order by gongysb.xuh, gongysb.mingc");
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
				String mbianm = rs.getString("BIANM");
				String mquanc = rs.getString("QUANC");
				String mmingc = rs.getString("MINGC");
				String mshengf = rs.getString("SHENGF");
				String mleib = rs.getString("LEIB");
				int mpage = rs.getInt("APAGE");
				long fuid = rs.getLong("FUID");
				Gongysbean gongys = new Gongysbean(mid, mbianm, mquanc, mmingc,
						mshengf, mleib,""+mid,fuid);
				setPages(mpage);
				_editvalues.add(gongys);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _editvalues;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setList1(null);
			setMingc(null);
			setBianm(null);
			getShengfModels();
			getGongyslbModels();
			getSelectData(0);

		}
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

//	private String getProperValue(IPropertySelectionModel _selectModel,
//			int value) {
//		int OprionCount;
//		OprionCount = _selectModel.getOptionCount();
//		for (int i = 0; i < OprionCount; i++) {
//			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
//				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
//			}
//		}
//		return null;
//	}
//
//	private long getProperId(IPropertySelectionModel _selectModel, String value) {
//		int OprionCount;
//		OprionCount = _selectModel.getOptionCount();
//
//		for (int i = 0; i < OprionCount; i++) {
//			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
//					value)) {
//				return ((IDropDownBean) _selectModel.getOption(i)).getId();
//			}
//		}
//		return -1;
//	}

	public IDropDownBean getShengfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getShengfModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setShengfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getShengfModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getShengfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getShengfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getShengfModels() {
		String sql = "select id, quanc from shengfb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));
	}

	public IDropDownBean getGongyslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongyslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongyslbValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongyslbModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getGongyslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongyslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongyslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getGongyslbModels() {
		String sql = "select lb.*\n"
				+ "  from (select 0 as id, decode(1, 1, '大供应商') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1, 1, '小供应商') as mingc from dual) lb";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
	}

	private int m_pages = 0;

	public int getPages() {
		return m_pages;
	}

	public void setPages(int pages) {
		m_pages = pages;
	}

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

	private StringBuffer m_sql = new StringBuffer("");

	public StringBuffer getSQL() {
		return m_sql;
	}

	public void setSQL(StringBuffer sql) {
		m_sql = sql;
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
