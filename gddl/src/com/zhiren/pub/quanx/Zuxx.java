package com.zhiren.pub.quanx;


import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zuxx extends BasePage implements PageValidateListener {
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
    public String getGridData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql = "select id,mingc,beiz from zuxxb where diancxxb_id = "
    			+visit.getDiancxxb_id()+" order by mingc";
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
			String record[] = change[i].split(",",4);
			if("D".equals(record[0])) {
				String id = record[1];
				String sql = "delete from zuxxb where id ="+id;
				con.getDelete(sql);
			}else
				if("U".equals(record[0])) {
					String id = record[1];
					String mingc = record[2];
					String beiz = record[3]==null?"":record[3];
					if(id.equals("0")) {
						String sql = "insert into zuxxb(id,mingc,beiz,diancxxb_id) values("
								+MainGlobal.getNewID(visit.getDiancxxb_id()) +",'"+mingc
								+"','"+beiz+"',"+visit.getDiancxxb_id()+")";
						con.getInsert(sql);
					}else {
						String sql = "update zuxxb set mingc='"+mingc+"',beiz='"+beiz+"' where id="+id;
						con.getUpdate(sql);
					}
				}
		}
	}
//	设置权限
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个分组设置权限!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Zuqx");
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
