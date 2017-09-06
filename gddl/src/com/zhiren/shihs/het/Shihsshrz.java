package com.zhiren.shihs.het;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;

public class Shihsshrz extends BasePage implements PageValidateListener {



	public boolean getRaw() {
		return true;
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	public void submit(IRequestCycle cycle) {

	}
	public List getyijs(){
		if(((Visit) getPage().getVisit()).getList6()==null){
			((Visit) getPage().getVisit()).setList6(new ArrayList());
		}
		return((Visit) getPage().getVisit()).getList6();
	}
	public void setyijs(List value){
		((Visit) getPage().getVisit()).setList6(value);
	}
	 private Yijbean yijbean;
	public Yijbean getyijbean(){
		return yijbean;
	}
	public void setyijbean(Yijbean value){
		yijbean= value;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		if (visit.getRenyID() == -1) {
//			visit.setboolean1(true);
//			return;
//		}
		JDBCcon con=new JDBCcon();
		
	
			
			if(cycle.getRequestContext().getParameter("hetb_id") != null&&!cycle.getRequestContext().getParameter("hetb_id").equals("-1")){
				
				visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetb_id")));
				
				StringBuffer buffer=new StringBuffer();
				buffer.append(" var hetb_id="+visit.getLong1()+";");
				setscript(buffer.toString());
				
			}else{
				
				
				
			}
			
	
		
		
	}
	public String getscript(){
		return((Visit) getPage().getVisit()).getString1();
	}
	public void setscript(String value){
		((Visit) getPage().getVisit()).setString1(value);
	}

//	// 厂别表下拉框
//	private boolean falg = false;
//
//	private IDropDownBean ChangbValue;
//
//	public IDropDownBean getChangbValue() {
//		if (ChangbValue == null) {
//			ChangbValue = (IDropDownBean) ChangbModel.getOption(0);
//		}
//		return ChangbValue;
//	}
//
//	public void setChangbValue(IDropDownBean Value) {
//		if (!(ChangbValue == Value)) {
//			ChangbValue = Value;
//			falg = true;
//		}
//
//	}
//
//	private IPropertySelectionModel ChangbModel;
//
//	public void setChangbModel(IPropertySelectionModel value) {
//		ChangbModel = value;
//	}
//
//	public IPropertySelectionModel getChangbModel() {
//		if (ChangbModel == null) {
//			getChangbModels();
//		}
//		return ChangbModel;
//	}
//
//	public IPropertySelectionModel getChangbModels() {
//		StringBuffer sql = new StringBuffer("SELECT ID,MINGC FROM CHANGBB");
//		ChangbModel = new IDropDownSelectionModel(sql);
//		return ChangbModel;
//	}

	// 类型下拉框
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}
	public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getHetbhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1() ;
	}

	public void setHetbhValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setHetbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getHetbhModels() {
		String sql =
			"select id,hetb.hetbh\n" +
			"from hetb\n" + 
			"where hetb.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		setHetbhModel( new IDropDownModel(sql,"请选择"));
		return ;
	}

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}


}
