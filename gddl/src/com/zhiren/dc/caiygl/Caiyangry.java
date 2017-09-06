package com.zhiren.dc.caiygl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 修改人: ww
 * 时间：      2010-05-24
 * 内容 ： 将上一页面Page名称存放在Visit.String11()中
 * 		  当返回时取出page名称
 */

public class Caiyangry extends BasePage implements PageValidateListener {
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
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
		}
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
//    		cycle.activate("Caiywh");
    		cycle.activate( ((Visit) getPage().getVisit()).getString11());
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;
//    		cycle.activate("Caiywh");
    		cycle.activate( ((Visit) getPage().getVisit()).getString11());
        }
    }
//  取得组数据
    public String getGridData() {
    	Visit visit = (Visit) getPage().getVisit();

    	String sql1="select id,'' checked,quanc mingc from renyxxb where bum='采样'";
    	ExtGridUtil egu = new ExtGridUtil();
    	
    	egu.setGridDs(sql1);
    	return egu.getDataset();
    }
//	保存分组的改动
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		visit.getDiancxxb_id();
		String change[] = getChange().split(";");

		String sqlte = "delete from caiyryglb where yangpdhb_id="+visit.getString1();
		
		con.getDelete(sqlte);
		for(int i = 0 ;i < change.length ; i++) {
			if(change[i] == null || "".equals(change[i])) {
				continue;
			}
			String record[] = change[i].split(",",2);
			String op = record[0];
			String id = record[1];
			
			
			
			if("I".equals(op)) {
				String sql="insert into caiyryglb(yangpdhb_id,renyxxb_id) values("+visit.getString1()+","+id+")";
				
			
				con.getInsert(sql);
			}
		}
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
