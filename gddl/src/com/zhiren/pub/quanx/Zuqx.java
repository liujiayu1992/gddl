package com.zhiren.pub.quanx;


import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zuqx extends BasePage implements PageValidateListener {
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
    		Return(cycle);
        }
    }
//  取得组数据
    public String getTreeData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql = "select zy.id,zy.mingc||decode(zy.leib,1,'(集团)',2,'(分公司)','(电厂)') mingc,zy.fuid,\n" +
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = "+
    		visit.getString1()+"),0) checked\n" + 
    		"from ziyxxb zy " +
    		" where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d " +
    		"where z.diancxxb_id = d.id and z.id = "+visit.getString1()+")"+
    		"order by zy.jib,zy.xuh";
    	ExtTreeUtil etu = new ExtTreeUtil();
    	etu.setTreeDs(sql, "资源权限");
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
			String ziyid = record[1];
			String sql = "delete from zuqxb where zuxxb_id="+visit.getString1()+" and ziyxxb_id="+ziyid;
			con.getDelete(sql);
			if("+".equals(sign)) {
				sql = "insert into zuqxb(id,zuxxb_id,ziyxxb_id) " +
				"values(getNewID("+visit.getDiancxxb_id()+"),"+visit.getString1()+","+ziyid+")";
				con.getInsert(sql);
			}
		}
		con.Close();
	}
	
//	返回
	public void Return(IRequestCycle cycle) {
		cycle.activate("Zuxxext");
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
