package com.zhiren.dc.dianckj;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* 
* 时间：2010-01-27
* 作者： sy
* 修改内容：1、修改刷新数据时没有对dianckjb_id做判断，刷新出所有口径的记录
*/
public class Guanldc extends BasePage implements PageValidateListener {
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
			
//			visit.getString1();	 口径id
//			visit.getString2();	 前页面tree_id
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
//  取得电厂信息
    public String getTreeData() {
    	
    	Visit visit = (Visit) getPage().getVisit();
//    	String sql = "select a.id,a.mingc,a.fuid,a.childs,a.checked from (select d.id id, d.quanc mingc, d.fuid fuid,\n" +
//    		"(select count(c.id) from diancxxb c where c.fuid = d.id) childs,\n" + 
//    		"(select count(m.id) from dianckjmx m where m.diancxxb_id = d.id and m.dianckjb_id = \n" +
//    		visit.getString1()+") checked\n" + 
//    		"from diancxxb d where d.fuid = " + visit.getString2() + ") a where a.childs=0 union \n" +
//    		"select d.id id, d.quanc mingc, d.fuid fuid,(select count(c.id) from diancxxb c where c.fuid = d.id) childs,\n" +
//    		"(select count(m.id) from dianckjmx m where m.diancxxb_id = d.id and m.dianckjb_id = \n" +
//    		visit.getString1()+") checked\n" +
//    		"from diancxxb d where d.id = " + visit.getString2();
    	
    	String sql=
    		"select d.id,\n" +
    		"       d.mingc,\n" + 
    		"       decode(d.id,"+visit.getString2()+",0,d.fuid) fuid,\n" + 
    		"       (select count(id) from diancxxb c where c.fuid=d.id) childs,\n" + 
    		"       decode(mx.id,null,0,1) as checked\n" + 
    		"  from diancxxb d,(select * from dianckjmx mx where mx.dianckjb_id="+visit.getString1()+")mx\n" + 
    		"  where d.id=mx.diancxxb_id(+)\n" +
   //		"        and (mx.dianckjb_id="+visit.getString1()+" or mx.dianckjb_id is null)\n" + 
    		" start with d.id = "+visit.getString2()+"\n" + 
    		"connect by fuid = prior d.id\n" + 
    		" order SIBLINGS by d.xuh";
	
    	
    	ExtTreeUtil etu = new ExtTreeUtil();
    	etu.setTreeDs(sql, "关联电厂");
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
			String sql = "delete from dianckjmx where dianckjb_id="+visit.getString1()+" and diancxxb_id="+ziyid;
			con.getDelete(sql);
			if("+".equals(sign)) {
				sql = "insert into dianckjmx(id,dianckjb_id,diancxxb_id,xuh) " +
				"values(getNewID("+visit.getDiancxxb_id()+"),"+visit.getString1()+","+ziyid+",0)";
				con.getInsert(sql);
			}
		}
		con.Close();
	}
	
//	返回
	public void Return(IRequestCycle cycle) {
		cycle.activate("Dianckjcx");
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
