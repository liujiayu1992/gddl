package com.zhiren.pub.reny;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.pub.quanx.GroupBean;

public abstract class PersonPower extends BasePage {
	//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setList2(null);
		RefurbishChick(visit.getString1());
	}
//	保存数据list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList2();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList2(editList);
    }
    
//  保存页面Bean
    private GroupBean editValue;
    public GroupBean getEditValue() {
    	return editValue;
    }
    public void setEditValue(GroupBean editValue) {
    	this.editValue = editValue;
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
    		//RefurbishChick();
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("PersonManage");
        }
    }
    
//  刷新分组
    private void RefurbishChick(String Renyid) {
    	List editList = new ArrayList();
    	JDBCcon con = new JDBCcon();
    	String sql = 
    		"select\n" +
    		"nvl((select id from renyzqxb\n" + 
    		"where zuxxb_id = z.id and renyxxb_id = "+ Renyid +"),0) checked\n" + 
    		", z.id, z.mingc, z.beiz\n" + 
    		"from zuxxb z";
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		错误信息:获得记录集失败.
    		return;
    	}
    	while(rsl.next()) {
    		boolean checked = rsl.getInt(0)!=0;
    		long id = rsl.getLong(1);
    		String mingc = rsl.getString(2);
    		String beiz = rsl.getString(3);
    		editList.add(new GroupBean(checked,id,mingc,beiz));
    	}
    	setEditValues(editList);
    	con.Close();
    }
//	保存人员相应分组的改动
	private void Save() {
		List listtmp = getEditValues();
		if(listtmp == null) {
//			错误信息:无需保存记录
			return;
		}
		JDBCcon con = new JDBCcon();
		String renyxxb_id = ((Visit) getPage().getVisit()).getString1();
		con.setAutoCommit(false);
		con.getDelete("delete from renyzqxb where renyxxb_id="+renyxxb_id);
		for(int i =0 ; i< listtmp.size(); i++) {
			if(((GroupBean)listtmp.get(i)).isSelected()) {
				long id = ((GroupBean)listtmp.get(i)).getID();
				con.getInsert("insert into renyzqxb(id,renyxxb_id,zuxxb_id) values(getnewid((select diancxxb_id from renyxxb where id = "+renyxxb_id+")),"+renyxxb_id+","+id+")");
			}
		}
		con.commit();
		con.Close();
	}
	
}
