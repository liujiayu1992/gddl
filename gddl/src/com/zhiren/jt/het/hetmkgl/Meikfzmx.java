package com.zhiren.jt.het.hetmkgl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikfzmx extends BasePage implements PageValidateListener {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String msg) {
		this._msg = MainGlobal.getExtMessageBox(msg,false);;
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
    		Return(cycle);
        }
    }
//  取得组数据
    public String getTreeData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql =
    		"select g.id    as id,\n" +
    		"       g.mingc as mingc,\n" + 
    		"       0 as fuid,\n" + 
    		"   (select count(gl.id) from gongysmkglb gl where gl.gongysb_id="+visit.getLong10()+") as childs,\n"+
    		"       nvl(m.id,0) as checked\n" + 
    		"      from gongysmkglb gl, gongysb g,meikfzmxb m\n" + 
    		"   where gl.gongysb_id=g.id\n" + 
    		"   and m.meikxxb_id(+)=g.id\n" + 
    		"   and m.meikfzb_id(+)="+visit.getString16()+
    		"   and g.id="+visit.getLong10()+
    		"   union\n" + 
    		"     select mk.id as id,\n" + 
    		"     mk.mingc as mingc,\n" + 
    		"     gl.gongysb_id as fuid,\n" + 
    		"     0 as childs,\n"+
    		"     nvl(m.id,0) as checked\n" + 
    		"     from gongysmkglb gl,meikxxb mk,meikfzmxb m\n" + 
    		"     where gl.meikxxb_id=mk.id\n" + 
    		"     and m.meikxxb_id(+)=mk.id\n" + 
    		"     and m.meikfzb_id(+)="+visit.getString16()+
    		"     and gl.gongysb_id="+visit.getLong10()+
    	    "     order by fuid,mingc";

    	ExtTreeUtil etu = new ExtTreeUtil();
    	etu.setTreeDs(sql, "供应商煤矿");
    	return etu.getDataset();
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
		String change[] = getChange().split(";");
		for(int i = 0 ;i < change.length ; i++) {
			if(change[i] == null || "".equals(change[i])) {
				continue;
			}
			String record[] = change[i].split(",",2);
			String sign = record[0];
			String meikid = record[1];
			String sql = "delete from meikfzmxb where meikfzb_id="+visit.getString16()+" and meikxxb_id="+meikid;
			con.getDelete(sql);
			setMsg("保存成功!");
			if("+".equals(sign)&& !meikid.equals(""+visit.getLong10())) {
				sql = "insert into meikfzmxb(id,meikfzb_id,meikxxb_id) " +
				"values(getNewID("+visit.getDiancxxb_id()+"),"+visit.getString16()+","+meikid+")";
				con.getInsert(sql);
				setMsg("保存成功!");
			}
		}
		con.Close();
	}
	
//	返回
	public void Return(IRequestCycle cycle) {
		cycle.activate("Meikfz");
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
