package com.zhiren.main.gongs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.main.Visit;

public abstract class Gongsnr extends BasePage {
	
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
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		if(getGongs() == null || "".equals(getGongs())) {
			RefurbishChick();
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
//  刷新
    private void RefurbishChick() {
    	DataBassUtil dbu = new DataBassUtil();
    	try {
    		String gongs = dbu.getClob("gongsb", "gongs", ((Visit)this.getPage().getVisit()).getLong1());
    		
//    		gongs = new 
//    		String(gongs.getBytes("ISO8859-1"),"GB2312");
    		setGongs(gongs);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
//  确认 
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
//	返回
	private void Return(IRequestCycle cycle) {
		setBiaot(null);
		setGongs(null);
		cycle.activate("Gongs");
	}
}
