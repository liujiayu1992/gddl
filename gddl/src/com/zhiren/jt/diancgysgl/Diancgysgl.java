package com.zhiren.jt.diancgysgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Diancgysgl extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private boolean _LastChick = false;

	public void LastButton(IRequestCycle cycle) {
		_LastChick = true;
	}
	
	private void Last(IRequestCycle cycle){
		cycle.activate("Zidyfapz_sjy");
	}

	private boolean _NextChick = false;

	public void NextButton(IRequestCycle cycle) {
		_NextChick = true;
	}
	
	private void Next(IRequestCycle cycle){
		Visit v = (Visit) getPage().getVisit();
		String sql = "select * from zidyfapz where zidyfa_id=" + v.getString1();
		String pz_id = "-1";
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			pz_id = rs.getString("id");
		}
		List l = getMultipleSelected();
		sql = "begin \n";
		for(int i =0;i <l.size() ; i++){
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) ( select " 
				+ pz_id + ",z_column,'showNum'," + (i+1)+" from zidyjcsjyms where id = "
				+ ((IDropDownBean)l.get(i)).getId() + ");\n";
		}
		sql += "end;";
		con.getDelete("delete from zidypzms where z_code = 'showNum' and zidyfapz_id =" +pz_id);
		con.getUpdate(sql);
		cycle.activate("Zidyfapz_qt");
	}

	public void submit(IRequestCycle cycle) {
		if (_LastChick) {
			_LastChick = false;
			Last(cycle);
		}
		if (_NextChick) {
			_NextChick = false;
			Next(cycle);
		}
	}
//	MultipleModel
	public SortMode getSort() {
		return SortMode.USER;
	}

	public IPropertySelectionModel getMultipleModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			CreateMultipleModel();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setMultipleModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public List getMultipleSelected() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setMultipleSelected(List ChepSelect) {
		((Visit) getPage().getVisit()).setList1(ChepSelect);
	}

	private void CreateMultipleModel() {
		setMultipleSelected(null);
		Visit v = (Visit) getPage().getVisit();
		List _MultipleList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = " select g.id,g.mingc from gongysb g ";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.getRows()==0){
//			此处添加 自定义基础数据源描述未配置的错误代码
		}
		while(rs.next()){
			_MultipleList.add(new IDropDownBean(rs.getLong("id"), rs
					.getString("mingc")));
		}
		rs.close();
		con.Close();
		setMultipleModel(new IDropDownModel(_MultipleList));
	}
	
	public void initMultipleSelected(){
		Visit v = (Visit) getPage().getVisit();
		List _MultipleSelectedList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = " select gongysb_id from gongysdcglb ";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			for(int i = 0;i<getMultipleModel().getOptionCount();i++){
				IDropDownBean ib = (IDropDownBean)getMultipleModel().getOption(i);
				if(rs.getLong("gongysb_id") == ib.getId()){
					_MultipleSelectedList.add(ib);
					break;
				}
			}
		}
		rs.close();
		setMultipleSelected(_MultipleSelectedList);
	}
	
//	 电厂名称
	// private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		// if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
		// _DiancmcChange = false;
		// }else{
		// _DiancmcChange = true;
		// }
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

//	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
//	Toolbar
	public void getToolBars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		
		ToolbarButton lbtn = new ToolbarButton(null,"上一步","function(){document.getElementById('LastButton').click();}");
		tb1.addItem(lbtn);
		ToolbarButton nbtn = new ToolbarButton(null,"下一步","function(){document.getElementById('NextButton').click();}");
		nbtn.setIcon(SysConstant.Btn_Icon_Save);
		tb1.addItem(nbtn);
		setToolbar(tb1);
	}
	
	

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
			if("".equals(visit.getString1())){
//				方案ID未得到 的错误代码
			}
			visit.setDefaultTree(null);
			getToolBars();
			initMultipleSelected();
		}
		getToolBars();
	}
}
