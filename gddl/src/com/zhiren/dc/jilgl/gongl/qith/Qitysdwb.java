package com.zhiren.dc.jilgl.gongl.qith;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-06-15 16：47
 * 描述：其它货运输单位保存不上的BUG
 */
public class Qitysdwb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	 private void Save() {
	 Visit visit = (Visit) this.getPage().getVisit();
	 //visit.getExtGrid1().Save(getChange(), visit);
	 JDBCcon con = new JDBCcon();
	 int flag = 0;
	 ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
	 StringBuffer sql = new StringBuffer("begin \n");
	 while(rsl.next()){
		 
		 StringBuffer sql2 = new StringBuffer();
		 sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
		 if("0".equals(rsl.getString("ID"))){
			 
			 sql.append("insert into qitysdwb(id,diancxxb_id,mingc,beiz)\n ");
			 sql.append("values(").append(sql2).append(",");
			 sql.append(visit.getDiancxxb_id()).append(",'");
			 sql.append(rsl.getString("MINGC")).append("','");
			 sql.append(rsl.getString("BEIZ")).append("');\n");
		 }else{
			 sql.append("update ");
			    sql.append("qitysdwb set id = ").append(rsl.getString("ID"));
			    sql.append(",diancxxb_id=").append(visit.getDiancxxb_id());
			    sql.append(",mingc = '").append(rsl.getString("MINGC"));
			    sql.append("',beiz ='").append(rsl.getString("BEIZ"));
			    sql.append("' where id = ").append(rsl.getString("ID")).append(";\n");
			   				 
			 }
		 }
	 sql.append("end;\n");
	 flag = con.getUpdate(sql.toString());
	 if(flag == -1){
		 con.rollBack();
		 rsl.close();
		 con.Close();
		 WriteLog.writeErrorLog("数据保存出错");
		 setMsg("数据保存出错");
		 return;
	 	}
		 
	 }
	 
	 

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id, diancxxb_id, mingc, beiz from qitysdwb  ");
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qitysdwb");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("mingc").setHeader("运输单位名称");
//		egu.getColumn("mingc").setHidden(true);

		egu.getColumn("beiz").setHeader("备注");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
	
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);   //删除按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//打印
		/*String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Chezreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}").getScript()
				+ "}");*/  
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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
			getSelectData();
		}
	}
}
