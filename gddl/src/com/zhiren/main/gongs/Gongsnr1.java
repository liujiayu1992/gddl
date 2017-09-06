package com.zhiren.main.gongs;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Gongsnr1 extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBiaot() {
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setBiaot(String title) {
		((Visit)this.getPage().getVisit()).setString1(title);
	}
	public String getGongs() {
		return ((Visit)this.getPage().getVisit()).getString2();
	}
	public void setGongs(String gongs) {
		((Visit)this.getPage().getVisit()).setString2(gongs);
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
		if(getGongs()==null || getGongs().equals("")
			|| ((Visit)this.getPage().getVisit()).getLong1()<=0) {
			return;
		}
		DataBassUtil dbu = new DataBassUtil();
    	try {
    		//String gongs = //getGongs();
    		
    		//new String(getGongs().getBytes("GB2312"),"ISO8859-1");
    		dbu.UpdateClob("gongsb", "gongs", ((Visit)this.getPage().getVisit()).getLong1(), getGongs());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
    }
	public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
    		RefurbishChick();
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;
    		Return(cycle);
        }
    }
	
	private void Return(IRequestCycle cycle) {
		setBiaot(null);
		setGongs(null);
		cycle.activate("Gongs1");
	}
	
	 private void RefurbishChick() {
	    	DataBassUtil dbu = new DataBassUtil();
	    	try {
	    		String gongs = dbu.getClob("gongsb", "gongs", ((Visit)this.getPage().getVisit()).getLong1());
	    		
//	    		gongs = new 
//	    		String(gongs.getBytes("ISO8859-1"),"GB2312");
	    		setGongs(gongs);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
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
		if(getGongs() == null || "".equals(getGongs())) {
			RefurbishChick();
		}
	}
}
