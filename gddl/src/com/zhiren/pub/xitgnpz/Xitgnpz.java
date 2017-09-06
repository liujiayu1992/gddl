package com.zhiren.pub.xitgnpz;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-08-13 13：46
 * 描述：修改代码不规范的地方恢复删除配置功能
 */
/*
 * 作者：王磊
 * 时间：2009-08-12 14：36
 * 描述：将grid中guanjz字段修改为不可编辑，除去删除按钮。防止配置出错。
 */

public class Xitgnpz extends BasePage implements PageValidateListener {
//	进行页面提示信息的设置
	private String _msg;

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			getSelectData();
			setLeiBModel(null);
			setLeiBValue(null);
		}
	}
//	按钮事件处理

	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _XxszChick = false;

	public void XxszButton(IRequestCycle cycle) {
		_XxszChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_shuaxin){
			_shuaxin=false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_XxszChick){
			_XxszChick=false;
			gotobaobpzsz(cycle);
		}
		getSelectData();
	}
	
	private boolean falg1 = false;

	private IDropDownBean LeiBValue;

	public IDropDownBean getLeiBValue() {
		if (LeiBValue == null) {
			LeiBValue = (IDropDownBean) getLeiBModel().getOption(0);
		}
		return LeiBValue;
	}

	public void setLeiBValue(IDropDownBean Value) {
		if (!(LeiBValue == Value)) {
			LeiBValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel LeiBModel;

	public void setLeiBModel(IPropertySelectionModel value) {
		LeiBModel = value;
	}

	public IPropertySelectionModel getLeiBModel() {
		if (LeiBModel == null) {
			getLeiBModels();
		}
		return LeiBModel;
	}

	public void getLeiBModels() {
		List l = new ArrayList();
		l.add(new IDropDownBean(1,"系统功能配置"));
		setLeiBModel(new IDropDownModel(l));
	}
		
	private void gotobaobpzsz(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个数据进行详细设置!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
//		System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Xitgnpzsz");
	}
	
//	保存分组的改动
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		getExtGrid().Save(getChange(), visit);
	}
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,guanjz,biaot,miaos,beiz from xitpzb");
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("xitpzb");
		egu.getColumn("guanjz").setHeader("关键字");		
		egu.getColumn("guanjz").setWidth(100);
		egu.getColumn("guanjz").setEditor(null);
		egu.getColumn("biaot").setHeader("标题");
		egu.getColumn("biaot").setWidth(150);
		egu.getColumn("miaos").setHeader("描述");
		egu.getColumn("miaos").setWidth(300);
     	egu.getColumn("beiz").setHeader("备注");
     	egu.getColumn("beiz").setWidth(90);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(Locale.Grid_DefaultWidth);
			
		ComboBox LeiB= new ComboBox();
		LeiB.setEditable(true);
		LeiB.setTransform("LeiB");
		LeiB.setWidth(150);
		egu.addToolbarItem(LeiB.getScript());
		
		egu.addTbarText("-");
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addTbarText("-");	
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "ShuaxinButton");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
		egu.addTbarText("-");
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    egu.addTbarText("-");
	    egu.addToolbarItem("{text:'详细设置',minWidth:75,handler:function (){  var grid1_history =\'\'; if(gridDiv_sm.getSelected()==null){ 	Ext.MessageBox.alert(\'提示信息\',\'请选择一条配置信息\');  return; }  grid1_rcd = gridDiv_sm.getSelected(); if(grid1_rcd.get(\'ID\') == \'0\'){ 	Ext.MessageBox.alert(\'提示信息\',\'在设置权限之前请先保存!\'); return; } grid1_history = grid1_rcd.get(\'ID\'); var Cobj = document.getElementById(\'CHANGE\'); Cobj.value = grid1_history;  document.getElementById(\'XxszButton\').click();}}");
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
}

