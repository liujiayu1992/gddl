package com.zhiren.main.gongs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

/*
 * 作者：夏峥
 * 时间：2011-05-05
 * 描述：使用visit中的String3作为页面跳转传递的参数
 */

public abstract class Gongsnrext extends BasePage {
//	进行页面提示信息的设置
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		if(getGongs() == null || "".equals(getGongs())) {
			RefurbishChick();
		}
	}
	//刷新
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
	 
//	返回
	private void Return(IRequestCycle cycle) {
		setBiaot(null);
		setGongs(null);
		cycle.activate(((Visit) getPage().getVisit()).getString3());
	}
	
//  取得初始化数据
    public String getInitData() {
    	String initData = "";
    	RefurbishChick();
    	initData = "intitle ='"+getBiaot()+"';";

    	return initData;
    }
//	按钮事件处理
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
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;
    		Return(cycle);
        }
    }
	
//	确认
	private void Save() {
		if(getGongs()==null || ((Visit)this.getPage().getVisit()).getLong1()<=0) {
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

	//工具条
//	public String getTbarScript() {
//		
//		Toolbar tbar = new Toolbar(null); 
//		tbar.id="formtb";
//
//		ToolbarButton tbtn = new ToolbarButton(null,"确认","function(){document.getElementById('InsertButton').click();}");
//		tbtn.setMinWidth(50);
//		tbar.addItem(tbtn);
//		
//		ToolbarButton ibtn = new ToolbarButton(null,"返回","function(){document.getElementById('InsertButton').click();}");
//		ibtn.setMinWidth(50);
//		tbar.addItem(ibtn);
//		
//		tbar.getRenderScript();
//		
//		String tb=tbar.getRenderScript();
//		return tb;
//	}
	
	
}
