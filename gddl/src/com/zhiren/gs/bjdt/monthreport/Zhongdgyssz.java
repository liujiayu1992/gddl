package com.zhiren.gs.bjdt.monthreport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.zhiren.common.ext.ExtGridUtil;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhongdgyssz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if(!(this.msg=msg).equals(""))
		this.msg =MainGlobal.getExtMessageBox("'"+msg+"'",true);
	}
	
	
	private void Save(IRequestCycle cycle){
		Visit v = (Visit) getPage().getVisit();
		List _MultipleSelectedList = new ArrayList();
		_MultipleSelectedList=this.getMultipleSelected();
		JDBCcon con = new JDBCcon();
		
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		sb.append("delete from fenxzdgysb; \n ");
		int result = 0;
		 for(int i=0;i<_MultipleSelectedList.size();i++) {
			 sb.append("insert into fenxzdgysb values(getnewid("+v.getDiancxxb_id()+"),"
				 +((IDropDownBean)_MultipleSelectedList.get(i)).getId()+","+i+","+1+",'"
				 +((IDropDownBean)_MultipleSelectedList.get(i)).getValue()+"','');\n");
			 result++;
		 }
		 sb.append("end;");
		 if(result>0){
			 result = con.getInsert(sb.toString());
		 }
		 
		 con.Close();
		 if(result>=0){
			 this.setMsg("重点供应商设置成功!");
		 }else{
			 this.setMsg("重点供应商设置失败!");
		 }
	     
	}

	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	private boolean _SaveChick = false;
	
     public void SaveButton(IRequestCycle cycle){
    	 _SaveChick=true;
	}
     
	public void submit(IRequestCycle cycle) {
		
		if(_SaveChick){
			_SaveChick=false;
			Save(cycle);
		}
		if(_ReturnChick){
			_ReturnChick = false;
			cycle.activate("Jingjhdfx_bjdt");
		}
//		setMultipleModel(null);
//		setMultipleSelected(null);
		initMultipleSelected();
//		CreateMultipleModel();
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
		if(((Visit) getPage().getVisit()).getList1()==null){
			initMultipleSelected();
		}
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
		String sql = " select g.id,g.quanc mingc from gongysb g where fuid='0' order by g.quanc ";
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
		String sql = " select  gongysb_id from fenxzdgysb ";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			for(int i = 0;i<getMultipleModel().getOptionCount();i++){
//				System.out.println(i);
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
		
		ToolbarButton lbtn = new ToolbarButton(null,"返回","function(){document.getElementById('ReturnButton').click();}");
	tb1.addItem(lbtn);
		
		//测试时放开
//		tb1.addItem(lbtn2);
		
		ToolbarButton nbtn = new ToolbarButton(null,"保存","function(){document.getElementById('SaveButton').click();}");
		nbtn.setIcon(SysConstant.Btn_Icon_Save);
		tb1.addItem(nbtn);
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(100);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
//		tb1.addField(WeizSelect);
//		StringBuffer sb=new StringBuffer();
//		sb.append("alert(GuanjzTf.getValue());\n");
		
		setToolbar(tb1);
	}
	
	
//  查找方式下拉菜单
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		
	
		list.add(new IDropDownBean(1, "按全称查找"));
		
		list.add(new IDropDownBean(2, "按简称查找"));
//		list.add(new IDropDownBean(3, "已审核的任务"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
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
			//传电厂id的值
//			this.setTreeid(visit.getString3());
			setMultipleModel(null);
	     	setMultipleSelected(null);
	     	setWeizSelectValue(null);
			setWeizSelectModel(null);
//			visit.setDefaultTree(null);
//			getToolBars();
			setMsg("");
		}
		
		getToolBars();
//		initMultipleSelected(); 
	}
}
