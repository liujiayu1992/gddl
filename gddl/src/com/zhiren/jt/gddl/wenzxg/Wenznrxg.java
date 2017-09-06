package com.zhiren.jt.gddl.wenzxg;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;

public class Wenznrxg extends BasePage  {
	private String neir;

	public String getNeir() {
		return neir;
	}
	public void setNeir(String neir) {
		this.neir = neir;
	}
	
	protected void initialize() {
		super.initialize();
		setNeir("");
	}
	
	private String id;   //��¼ѡ��(��Ҫ�޸�)������id
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	
	private String closeScript;  //�رոô��ڵ�javascript
	public void setCloseScript(String closeScript){
		this.closeScript = closeScript;
	}
	public String getCloseScript(){
		return closeScript;
	}
	
	private boolean baoc = false;//����
	public void baoc(IRequestCycle cycle) {
		baoc = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (baoc) {
			baoc = false;
			save();
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		if (!visit.getActivePageName().toString().equals(
//				this.getPageName().toString())) {//��ʾ
			visit.setActivePageName(getPageName().toString());
			setId(cycle.getRequestContext().getParameter("id"));
			JDBCcon con = new JDBCcon();
			StringBuffer sql = new StringBuffer();
			sql.append("select wenz from wenzxxb wb where wb.id = "+getId()+"\n");
			ResultSetList rsl = con.getResultSetList(sql.toString());
			while(rsl.next()){
				setNeir(rsl.getString("wenz"));
			}
	}
//	 Page����
	private void save(){
		JDBCcon con=new JDBCcon();
		StringBuffer sql= new StringBuffer();
		sql.append("update wenzxxb wb set wenz = '"+getNeir()+"'\n" +
				"where wb.id = "+getId());
		con.getUpdate(sql.toString());
		con.Close();
		closeScript="window.opener.document.getElementById('RefurbishButton').click();window.opener=null;window.close();";
	}

}
