package com.zhiren.dc.rulgl.jizfzb;

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

public class Jizmc extends BasePage implements PageValidateListener {
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
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Jizfzbext");
        }
    }
//  取得组数据
    public String getGridData() {
    	Visit visit = (Visit) getPage().getVisit();
    	
    	String sql =
    		"select j.id,\n" +
    		"       (select decode(id, null, '', '1') id\n" + 
    		"           from jizfzglb\n" + 
    		"           where jizb_id = j.id\n" + 
    		"            and diancxxb_id = " + visit.getString10() + " and jizfzb_id = "+visit.getString1()+") checked,\n" + 
    		"       j.jizbh, j.beiz\n" + 
    		"  from jizb j, diancxxb d\n" + 
    		" where j.diancxxb_id = d.id\n" + 
    		"   and d.id = " + visit.getString10()+"";
    	
    	ExtGridUtil egu = new ExtGridUtil();
    	egu.setGridDs(sql);
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
		for(int i = 0 ;i < change.length ; i++) {
			if(change[i] == null || "".equals(change[i])) {
				continue;
			}
			String record[] = change[i].split(",",2);
			String op = record[0];
			String id = record[1];
			String sql = "delete from jizfzglb where jizfzb_id="+visit.getString1()+" and diancxxb_id="+visit.getString10()+" and  jizb_id="+id;
			con.getDelete(sql);
			if("I".equals(op)) {
				sql = "insert into jizfzglb(id,diancxxb_id,jizfzb_id,jizb_id) values(getnewid("+visit.getString10()+"),"+visit.getString10()+","+visit.getString1()+","+id+")";
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
