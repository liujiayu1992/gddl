package com.zhiren.shanxdted.yunslwh;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunslwh extends BasePage implements PageValidateListener {
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
        setTbmsg(null);
    }
//    -----------------------------------
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
//    ------------------------------------
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    
//  页面变化记录
    private String theKey;
    private boolean Key=false;
    public String gettheKey() {
    	return theKey;
    }
    public void settheKey(String theKey) {
    	this.theKey = theKey;
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

//	按钮事件处理

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    
    private boolean _ChazChick = false;
    public void ChazButton(IRequestCycle cycle) {
    	_ChazChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_ChazChick) {
    		_ChazChick = false;
    		Key=true;
        }
    }

//  取得组数据
    public void getSelectData() {
    	String sql=new String();
    	if(Key){
    		sql = "select id,mingc,yunsl,yunslx " +
		     " from  meikxxb where " +
		     		" mingc like '%" + gettheKey() + "%'" ;
    		Key=false;
    	}else{
    		sql = "select id , mingc ,yunsl, yunslx from meikxxb  ";
    	}
		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("meikxxb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("mingc").setHeader("煤矿单位信息");
		egu.getColumn("mingc").editor=null;
		egu.getColumn("mingc").setWidth(250);
		egu.getColumn("yunsl").setHeader("运损率");
		egu.getColumn("yunsl").setWidth(60);
		egu.getColumn("yunslx").setHeader("运输路线");
		egu.getColumn("yunslx").setWidth(450);
	
		egu.addTbarText("输入煤矿单位信息：");
		TextField theKey = new TextField();
		theKey.setWidth(100);
		theKey.setId("theKey_text");
		theKey.setListeners("specialkey:function(thi,e){if (e.getKey()==13){var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();document.getElementById('ChazButton').click();}}\n");
		egu.addToolbarItem(theKey.getScript());

		String Chaz = "function(){var objkey = document.getElementById('theKey');objkey.value=theKey_text.getValue();document.getElementById('ChazButton').click();}";
		GridButton chaz = new GridButton("查找", Chaz, SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(chaz);		
		setExtGrid(egu);
		egu.addTbarText("-");
		GridButton gSave =  new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		egu.addTbarBtn(gSave);
		egu.addTbarText("   （1、当煤矿单位信息为空时查找显示全部内容；2、翻页或查找操作前请保存）");

    }
//	保存分组的改动
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
		JDBCcon con = new JDBCcon();
		String id = "";
		String yunsl = "";
		String yunslx = "";
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
				id = rsl.getString("id");
				yunsl = rsl.getString("yunsl");
				yunslx = rsl.getString("yunslx");
			
				String sql = "update meikxxb set yunsl = "+yunsl+",yunslx = '"+yunslx+"' where id =" + id;
				con.getUpdate(sql);
		}
		
	}


//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			setTreeid("");
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
