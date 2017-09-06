package com.zhiren.zidy;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zidyfapz_px extends BasePage implements PageValidateListener {
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
		String sql = "select * from zidyjcsjyms where zidyjcsjy_id in (select zidyjcsjy_id from zidyfapz where zidyfa_id ="+v.getString1()+")";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.getRows()==0){
//			此处添加 自定义基础数据源描述未配置的错误代码
		}
		while(rs.next()){
			_MultipleList.add(new IDropDownBean(rs.getLong("id"), rs
					.getString("z_column_cn")));
		}
		rs.close();
		con.Close();
		setMultipleModel(new IDropDownModel(_MultipleList));
	}
	
	public void initMultipleSelected(){
		Visit v = (Visit) getPage().getVisit();
		List _MultipleSelectedList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = "select y.id\n"
					+ "  from (select * \n"
					+ "          from zidyjcsjyms \n"
					+ "         where zidyjcsjy_id in \n"
					+ "               (select zidyjcsjy_id from zidyfapz where zidyfa_id = "+v.getString1()+")) y,"
					+ "       (select * \n"
					+ "          from zidypzms \n"
					+ "         where z_code = 'showNum' \n"
					+ "           and zidyfapz_id in \n"
					+ "               (select id from zidyfapz where zidyfa_id = "+v.getString1()+")) m \n"
					+ " where y.z_column = m.z_column order by m.z_value";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			for(int i = 0;i<getMultipleModel().getOptionCount();i++){
				IDropDownBean ib = (IDropDownBean)getMultipleModel().getOption(i);
				if(rs.getLong("id") == ib.getId()){
					_MultipleSelectedList.add(ib);
					break;
				}
			}
		}
		rs.close();
		setMultipleSelected(_MultipleSelectedList);
	}
	
//	Toolbar
	public void createToolbar(){
		Toolbar tb1 = new Toolbar("tbdiv");
		ToolbarButton lbtn = new ToolbarButton(null,"上一步","function(){document.getElementById('LastButton').click();}");
		tb1.addItem(lbtn);
		ToolbarButton nbtn = new ToolbarButton(null,"下一步","function(){document.getElementById('NextButton').click();}");
		nbtn.setIcon(SysConstant.Btn_Icon_Save);
		tb1.addItem(nbtn);
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getToolbar() == null){
			return "";
		}
		return getToolbar().getRenderScript();
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
			createToolbar();
			CreateMultipleModel();
			initMultipleSelected();
		}
	}
}
