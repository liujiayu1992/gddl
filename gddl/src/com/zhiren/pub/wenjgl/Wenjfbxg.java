package com.zhiren.pub.wenjgl;


import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Wenjfbxg extends BasePage  {
	private boolean wenjfb = false;
    public void wenjfb(IRequestCycle cycle) {
    	wenjfb = true;
    }
	public void submit(IRequestCycle cycle) {
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
		if (shuaxButton) {
			shuaxButton = false;
			getSelectData();
		}
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//显示
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			getSelectData();
		}
	}
	private void getSelectData() {
//		 取数据
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		StringBuffer data=new StringBuffer();
		int i=0;
		try{
			String sql="";
			sql=
				"select w.id,r.mingc reny,to_char(min(f.shij),'YYYY-MM-DD HH24:MI:SS')shij,to_char(min(f.youxq),'YYYY-MM-DD')youxsj,w.biaot ,getJiesdws(w.id)jiesdws\n" +
				"from fabwjb f,renyxxb r,wenjb w\n" + 
				"where f.renyxxb_id=r.id and f.wenjb_id=w.id and to_char(f.shij,'YYYY-MM-DD')>='" + getRiq()+"'and  to_char(f.shij,'YYYY-MM-DD')<='"+ getRiq1()+
				"' group by w.id,r.mingc ,w.biaot";
			ResultSet rs=con.getResultSet(sql);
			data.append(" var fabData = [\n");
			while(rs.next()){
				if(i==0){
					data.append("['"+rs.getString("id")+"','"+rs.getString("reny")+"','"+rs.getString("shij")+"','"+rs.getString("biaot")+"','"+rs.getString("jiesdws")+"','"+rs.getString("youxsj")+"']\n");
				}else{
					data.append(",['"+rs.getString("id")+"','"+rs.getString("reny")+"','"+rs.getString("shij")+"','"+rs.getString("biaot")+"','"+rs.getString("jiesdws")+"','"+rs.getString("youxsj")+"']\n");
				}
				i++;
			}
			
			data.append("]\n");
			setGridData(data.toString());
			//
//			//树数据
//			TreeNodes tns=new TreeNodes("diancxxb",visit.getDiancxxb_id(),"mingc",0,true);
//			setTreeData(tns.getScript());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	private void  DelData(){
		JDBCcon con=new JDBCcon();
		String sql=
		"delete\n" +
		"from fabwjb\n" + 
		"where fabwjb.wenjb_id="+getChange();
		con.getDelete(sql);
		getSelectData();
		
	}

	
	private String GridData;
	public String getGridData(){
		return GridData;
	}
	public void setGridData(String value){
		this.GridData=value;
	}
//	private String TreeData;
//	public String getTreeData(){
//		return TreeData;
//	}
//	public void setTreeData(String value){
//		this.TreeData=value;
//	}
	private String riq;
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	private String riq1;
	public String getRiq1() {
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	private boolean shuaxButton = false;
	
	public void shuaxButton(IRequestCycle cycle) {
		shuaxButton = true;
	}
	
	
}
