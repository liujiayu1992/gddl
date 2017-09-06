package com.zhiren.jt.zdt.xitgl.gongyspj.pingjmc;


import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;


import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pingjmc extends BasePage implements PageValidateListener {
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


	
	
	
	public void Save() 
	{
		//--------------------------------
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");

		while (drsl.next()) {
			//删除月标煤单价表
			sql_delete.append("delete from ").append("pingjmcb").append(" where id =").append(drsl.getLong("id")).append(";\n");
			}
		sql_delete.append("end;");
		if(sql_delete.length()>11){
			con.getUpdate(sql_delete.toString());
		}
		
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		StringBuffer sql = new StringBuffer("begin \n");
		while (rsl.next()) {
			
			long id = 0;
			
			
			String ID_1=rsl.getString("ID");

			if ("0".equals(ID_1)||"".equals(ID_1)) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
			
				//向月标煤单价表里插入数据
				sql.append("insert into pingjmcb(id,xuh,mingc,caozy,riq,beiz) values(\n"
						+id+","+rsl.getInt("xuh")+",'"+rsl.getString("mingc")+"','"+rsl.getString("caozy")+"',sysdate"+",'"+rsl.getString("beiz")+
						"');\n");
		
				
			} else {
				
				 sql.append("update pingjmcb set xuh="
				 + rsl.getInt("xuh")+",mingc ='"
				 + rsl.getString("mingc")+"',beiz='" + rsl.getString("beiz")+ 
				 "'  where id=" + rsl.getString("ID") +";\n");
				
			}
		
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());


		
		con.Close();
		setMsg("保存成功!");
	
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
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rsl = con.getResultSetList("select id,xuh,mingc,beiz,caozy,riq from pingjmcb where id not in (select id from pingjmcb where beiz='默认设置') order by xuh");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("pingjmcb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setUnique(false);
//		egu.getColumn("mingc").isUnique();
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caozy").setDefaultValue(""+visit.getRenymc());
		egu.getColumn("caozy").setHidden(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(""+new Date());
		egu.getColumn("riq").setHidden(true);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
