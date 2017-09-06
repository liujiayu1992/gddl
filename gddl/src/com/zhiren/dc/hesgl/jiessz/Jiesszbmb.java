package com.zhiren.dc.hesgl.jiessz;

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
import org.apache.tapestry.html.BasePage;

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

public class Jiesszbmb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
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
	 visit.getExtGrid1().Save(getChange(), visit);
	 }

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
    
	private boolean _ShezChick = false;

	public void ShezButton(IRequestCycle cycle) {
		_ShezChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShezChick) {
			_ShezChick = false;
			GotoShez(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("SELECT   j.id, j.bianm,j.mingc,decode(j.shifwy,0,'否','是') as shifwy \n"
						+ "from jiesszbmb j");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jiesszbmb");
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(150);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("shifwy").setHeader("是否唯一");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
//		String sql = "select id from diancxxb where id="
//				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
//		ResultSet rs = con.getResultSet(sql);
//		String diancid = "";
//		try {
//			while (rs.next()) {
//				diancid = rs.getString("id");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		egu.getColumn("diancxxb_id").setDefaultValue(diancid);
	
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "否"));
		l.add(new IDropDownBean(1, "是"));
		egu.getColumn("shifwy").setEditor(new ComboBox());
		egu.getColumn("shifwy").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("shifwy").setReturnId(true);
		egu.getColumn("shifwy").setDefaultValue(String.valueOf(((IDropDownBean)l.get(0)).getValue()));
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str2=
			" if(gridDiv_sm.getSelections().length <= 0 " 
		    + "|| gridDiv_sm.getSelections().length > 1){ \n"
		    + " 	Ext.MessageBox.alert('提示信息','请选中一个项目!'); \n"
		    + " 	return;"
	        +"}"
	        + " var rec = gridDiv_sm.getSelected(); \n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('提示信息','在设置之前请先保存!'); \n"
		    + "  	return;"
	        +"}"
	        +" gridDiv_history = rec.get('ID')+','+rec.get('MINGC');	\n"
	        +" document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +" document.getElementById('ShezButton').click(); \n";

        egu.addToolbarItem("{"+new GridButton("设置","function(){"+str2+"}").getScript()+"}");
		setExtGrid(egu);
		con.Close();
	}
	
	
	private void GotoShez(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		 ((Visit) getPage().getVisit()).setString10(this.getParameters());
    	cycle.activate("Jiesszbmzb");
	}

	private String Parameters;//记录项目ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
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
			visit.setString10("");
			getSelectData();
		}
	}
}
