package com.zhiren.jt.het.shenhrz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.jt.het.renyjs.Renyjsbean;
import com.zhiren.main.Visit;

public class Yunshtshrz extends BasePage {

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
	

	private boolean tijsh;//是否添加  提交进入流程审核功能
	
	public boolean isTijsh(){
		
		return tijsh;
	}
	
	public void setTijsh(){
		
		tijsh=false;
		
		String sql=" select * from xitxxb  where mingc='运输合同模板提交审核' and leib='合同模板' and zhi='是' and zhuangt=1 ";
	
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			tijsh=true;
		}
		
	}
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		if (visit.getRenyID() == -1) {
//			visit.setboolean1(true);
//			return;
//		}
		
		visit.setActivePageName(this.getPageName().toString());//激活页
		
		JDBCcon con=new JDBCcon();
		this.setTijsh();
		
		
		if(this.isTijsh()){
			
			//运输合同
			if(cycle.getRequestContext().getParameter("leix")!=null && cycle.getRequestContext().getParameter("leix").equals("1")){
				if (cycle.getRequestContext().getParameter("hetys_id") != null&&!cycle.getRequestContext().getParameter("hetys_id").equals("-1")) {
					visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetys_id")));
					//加载意见
					String sql=
						"select rzb.qianqztmc,to_char(rzb.shij,'YYYY-MM-DD HH24:mi:ss')shij,rzb.liucdzbmc,rzb.houjztmc,rzb.miaos,rzb.caozy\n" +
						"from hetys,liucgzb rzb\n" + 
						"where hetys.liucgzid=rzb.liucgzid and hetys.id="+visit.getLong1();
					ResultSet rs=con.getResultSet(sql);
					getyijs().clear();
					try{
						while(rs.next()){
							String qianqztmc=rs.getString("qianqztmc");
							String caozy=rs.getString("caozy");
							String shij=rs.getString("shij");
							String liucdzbmc=rs.getString("liucdzbmc");
							String houjztmc=rs.getString("houjztmc");
							String miaos=rs.getString("miaos");
							getyijs().add(new Yijbean(caozy+shij+liucdzbmc+houjztmc+":",miaos));
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//加载合同
					StringBuffer buffer=new StringBuffer();
					buffer.append(" var hetys_id="+visit.getLong1()+";");
					setscript(buffer.toString());
					
					this.setLeixScript(" var leix="+cycle.getRequestContext().getParameter("leix")+";");
				}else{
					//不可能执行的路径
				}
				
				
			}else{//模板
				if (cycle.getRequestContext().getParameter("hetys_id") != null&&!cycle.getRequestContext().getParameter("hetys_id").equals("-1")) {
					visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetys_id")));
					//加载意见
					String sql=
						"select rzb.qianqztmc,to_char(rzb.shij,'YYYY-MM-DD HH24:mi:ss')shij,rzb.liucdzbmc,rzb.houjztmc,rzb.miaos,rzb.caozy\n" +
						"from hetys_mb,liucgzb rzb\n" + 
						"where hetys_mb.liucgzid=rzb.liucgzid and hetys_mb.id="+visit.getLong1();
					ResultSet rs=con.getResultSet(sql);
					getyijs().clear();
					try{
						while(rs.next()){
							String qianqztmc=rs.getString("qianqztmc");
							String caozy=rs.getString("caozy");
							String shij=rs.getString("shij");
							String liucdzbmc=rs.getString("liucdzbmc");
							String houjztmc=rs.getString("houjztmc");
							String miaos=rs.getString("miaos");
							getyijs().add(new Yijbean(caozy+shij+liucdzbmc+houjztmc+":",miaos));
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//加载合同
					StringBuffer buffer=new StringBuffer();
					buffer.append(" var hetys_id="+visit.getLong1()+";");
					setscript(buffer.toString());
					
					this.setLeixScript(" var leix="+cycle.getRequestContext().getParameter("leix")+";");
				}else{
					//不可能执行的路径
				}
			}
		}else{
			if (cycle.getRequestContext().getParameter("hetys_id") != null&&!cycle.getRequestContext().getParameter("hetys_id").equals("-1")) {
				visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetys_id")));
				//加载意见
				String sql=
					"select rzb.qianqztmc,to_char(rzb.shij,'YYYY-MM-DD HH24:mi:ss')shij,rzb.liucdzbmc,rzb.houjztmc,rzb.miaos,rzb.caozy\n" +
					"from hetys,liucgzb rzb\n" + 
					"where hetys.liucgzid=rzb.liucgzid and hetys.id="+visit.getLong1();
				ResultSet rs=con.getResultSet(sql);
				getyijs().clear();
				try{
					while(rs.next()){
						String qianqztmc=rs.getString("qianqztmc");
						String caozy=rs.getString("caozy");
						String shij=rs.getString("shij");
						String liucdzbmc=rs.getString("liucdzbmc");
						String houjztmc=rs.getString("houjztmc");
						String miaos=rs.getString("miaos");
						getyijs().add(new Yijbean(caozy+shij+liucdzbmc+houjztmc+":",miaos));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				//加载合同
				StringBuffer buffer=new StringBuffer();
				buffer.append(" var hetys_id="+visit.getLong1()+";");
				setscript(buffer.toString());
				
				this.setLeixScript(" var leix="+cycle.getRequestContext().getParameter("leix")+";");
			}else{
				//不可能执行的路径
			}
		}
		
	}
	
	public String getLeixScript(){
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setLeixScript(String value){
		((Visit) getPage().getVisit()).setString2(value);
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

}
