package com.zhiren.zidy;

import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;

public class Zidyjcsjy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
//	保存数据list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
    
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	private String Parameters;//记录ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		getExtGrid().Save(getChange(), visit);
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while(delrsl.next()){
			String sql = "delete from zidyjcsjysql where zidyjcsjy_id =" + delrsl.getString(0);
			con.getDelete(sql);
			sql = "delete from zidyjcsjyms where zidyjcsjy_id =" + delrsl.getString(0);
			con.getDelete(sql);
		}
		delrsl.close();
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}

	private boolean _ShedzdmsChick = false;

	public void ShedzdmsButton(IRequestCycle cycle) {
		_ShedzdmsChick = true;
	}
	
	
	private boolean _ShedfzChick = false;

	public void ShedfzButton(IRequestCycle cycle) {
		_ShedfzChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShedsqlChick){
			_ShedsqlChick = false;
			UpdateSQL(cycle);
		}
		if (_ShedzdmsChick){
			_ShedzdmsChick = false;
			UpdateDescription(cycle);
		}
		if (_ShedfzChick){
			_ShedfzChick = false;
			UpdateGroup(cycle);
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,z_title_cn,z_designer,z_time,z_remark from zidyjcsjy");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidyjcsjy");
		//egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("z_title_cn").setHeader("数据源名称");
		//egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(visit.getDiancxxb_id()));
		egu.getColumn("z_designer").setHeader("设计者");
		egu.getColumn("z_time").setHidden(true);
		
		egu.getColumn("z_remark").setHeader("备注");
		
		egu.getColumn("z_title_cn").setWidth(150);
		Date time = new Date();
		String date = DateUtil.FormatDate(time);
		egu.getColumn("z_time").setDefaultValue(date);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//只能单选中行

		egu.addPaging(25);

		egu.addToolbarButton(GridButton.ButtonType_Refresh, null);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gb = new GridButton("设计SQL","function(){ " +
				" var rec = gridDiv_sm.getSelected(); " +
				" if(rec != null){var id = rec.get('ID'); var title = rec.get('Z_DESIGNER') + '  :  ' + rec.get('Z_TITLE_CN'); " +
				" var Cobj = document.getElementById('CHANGE'); var Cobjid = document.getElementById('CHANGEID');" +
				" Cobj.value = title; Cobjid.value = id;" +
				" document.getElementById('ShedsqlButton').click();}}");
		egu.addTbarBtn(gb);
		String str2= " var rec = gridDiv_sm.getSelected(); \n"
	        +" var id = rec.get('ID'); 	\n"
	        +"var cobjid = document.getElementById('PARAMETERS')\n"
	        +" cobjid.value = id; \n"
	        +" document.getElementById('ShedzdmsButton').click(); \n";
		String str3= " var rec = gridDiv_sm.getSelected(); \n"
	        +" var id = rec.get('ID'); 	\n"
	        +"var cobjid = document.getElementById('PARAMETERS')\n"
	        +" cobjid.value = id; \n"
	        +" document.getElementById('ShedfzButton').click(); \n";

		egu.addTbarBtn(new GridButton("设定字段描述","function(){"+str2+"}"));
        egu.addTbarBtn(new GridButton("设定数据源分组","function(){"+str3+"}"));
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
//	页面判定方法
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

	private void UpdateSQL(IRequestCycle cycle) {
		
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一数据源设置SQL!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setLong1(Long.parseLong(getChangeid()));
		visit.setString1(getChange());
		cycle.activate("Zidyjcsjysql");
		
		
	}
    private void UpdateDescription(IRequestCycle cycle) {
		
		if(getParameters()==null || "".equals(getParameters())) {
			setMsg("请选中一数据源添加描述!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString9(getParameters());
		cycle.activate("Zidyjcsjyms");
	}
   private void UpdateGroup(IRequestCycle cycle) {
		
		if(getParameters()==null || "".equals(getParameters())) {
			setMsg("请选中一数据源设置分组!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString10(getParameters());
		cycle.activate("Zidyjcsjyfz");
	}
    
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setLong1(0);
			visit.setString1(null);
			visit.setLong9(0);
			visit.setString9(null);
			visit.setLong10(0);
			visit.setString10(null);
			getSelectData();
		}
		
	}
}

