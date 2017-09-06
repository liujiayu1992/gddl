package com.zhiren.shanxdted.diqmkext;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.main.validate.Validate;
/*
 * 作者:tzf
 * 时间:2009-11-03
 * 内容:煤矿地区名称
 */
public class Diqmkext extends BasePage implements PageValidateListener {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
//	    _msg = _value;
		_msg=MainGlobal.getExtMessageBox(_value,false);
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
	
	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	

//	按钮事件处理

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
    }
    
   
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    }

//  取得组数据
    public void getSelectData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql = " select id,mingc,beiz from diqmkgroup ";

		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("diqmkgroup");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHeader("编号");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("mingc").setHeader("地区煤矿名称");
		egu.getColumn("beiz").setHeader("备注");
//
//		//是否显示选择电厂树
//		if (visit.isFencb()) {
//			egu.addTbarText("单位名称:");
//			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//					ExtTreeUtil.treeWindowType_Dianc,
//					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
//			setTree(etu);
//			egu.addTbarTreeBtn("diancTree");
//			egu.addTbarText("-");
//		}
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
//		GridButton gSave =  new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//		egu.addTbarBtn(gSave);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		String sPowerHandler = "function(){"
					+"if(gridDiv_sm.getSelected()== null){"
		        	+"	Ext.MessageBox.alert('提示信息','请选中一个进行设置!');"
		        	+"	return;"
		        	+"}"
		        	+"var grid_rcd = gridDiv_sm.getSelected();"
		        	+"if(grid_rcd.get('ID') == '0'){"
		        	+"	Ext.MessageBox.alert('提示信息','在设置分组之前请先保存!');"
		        	+"	return;"
		        	+"}"
		        	+"grid_history = grid_rcd.get('ID');"
					+"var Cobj = document.getElementById('CHANGE');"
					+"Cobj.value = grid_history;"
					+"document.getElementById('PowerButton').click();"
					+"}";
		
		egu.addTbarBtn(new GridButton("设置煤矿",sPowerHandler));
		
		setExtGrid(egu);

    }
//	保存分组的改动
	private void Save() {
		Visit visit=(Visit)this.getPage().getVisit();
		int flag=this.getExtGrid().Save(this.getChange(), visit);
		
		if(flag>=0)
			this.setMsg("数据操作成功!");
		else
			this.setMsg("数据操作失败!");
	}
//	设置分组
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个进行设置!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString12(getChange());
		cycle.activate("Diqmkgroup");
	}

//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setTreeid("");
		}
		init();
	} 
	
	private void init() {
		setExtGrid(null);
		setTree(null);
		getSelectData();
	}
//	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
}
