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

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Gongs extends BasePage implements PageValidateListener {
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
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			RefurbishChick();
		}
	}
//	保存数据list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
    
//  保存页面Bean
    private GongsBean editValue;
    public GongsBean getEditValue() {
    	return editValue;
    }
    public void setEditValue(GongsBean editValue) {
    	this.editValue = editValue;
    }
//	按钮事件处理
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }

    private boolean _InsertChick = false;
    public void InsertButton(IRequestCycle cycle) {
        _InsertChick = true;
    }

    private boolean _DeleteChick = false;
    public void DeleteButton(IRequestCycle cycle) {
        _DeleteChick = true;
    }

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _UpdateChick = false;
    public void UpdateButton(IRequestCycle cycle) {
        _UpdateChick = true;
    }
    public void submit(IRequestCycle cycle) {
    	if (_RefurbishChick) {
    		_RefurbishChick = false;
    		RefurbishChick();
        }
    	if (_InsertChick) {
    		_InsertChick = false;
    		Insert();
        }
    	if (_DeleteChick) {
    		_DeleteChick = false;
    		Delete();
        }
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
    		RefurbishChick();
        }
    	if (_UpdateChick) {
    		_UpdateChick = false;
    		Update(cycle);
        }
    }
//  刷新
    private void RefurbishChick() {
    	List editList = new ArrayList();
    	JDBCcon con = new JDBCcon();
    	String sql = "select id,mingc,leix,zhuangt,beiz from gongsb where zhuangt=1 order by zhuangt,leix,mingc";
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
    		setMsg("获得公式信息失败");
//    		错误信息:获得记录集失败.
    		return;
    	}
    	while(rsl.next()) {
    		long id = rsl.getLong(0);
    		String mingc = rsl.getString(1);
    		String leix = rsl.getString(2);
    		int zhuangt = rsl.getInt(3);
    		String beiz = rsl.getString(4);
    		editList.add(new GongsBean(false,id,mingc,leix,zhuangt,beiz));
    	}
    	setEditValues(editList);
    	con.Close();
    }
//  新建
	private void Insert() {
		List listtmp = getEditValues();
		if(listtmp == null) {
			listtmp = new ArrayList();
		}
		for(int i =0 ; i< listtmp.size(); i++) {
			long id = ((GongsBean)listtmp.get(i)).getId();
			if(id ==0) {
				setMsg("请先保存未保存的公式");
				return;
			}
		}
		listtmp.add(new GongsBean(true,0,"","",1,""));
		setEditValues(listtmp);
	}
//	删除
	private void Delete() {
		List listtmp = getEditValues();
		if(listtmp == null) {
			return;
		}
		JDBCcon con = new JDBCcon();
		for(int i =0 ; i< listtmp.size(); i++) {
			if(((GongsBean)listtmp.get(i)).getSelected()) {
				long id = ((GongsBean)listtmp.get(i)).getId();
				if(id != 0) {
					con.getDelete("update gongsb set zhuangt=0 where id="+id);
				}
				listtmp.remove(i--);
				break;
			}
		}
		con.Close();
	}
//	保存
	private void Save() {
		List listtmp = getEditValues();
		if(listtmp == null) {
//			错误信息:无需保存记录
			return;
		}
		JDBCcon con = new JDBCcon();
		for(int i =0 ; i< listtmp.size(); i++) {
			if(((GongsBean)listtmp.get(i)).getSelected()) {
				long id = ((GongsBean)listtmp.get(i)).getId();
				String mingc = ((GongsBean)listtmp.get(i)).getMingc();
				String leix = ((GongsBean)listtmp.get(i)).getLeix();
				String beiz = ((GongsBean)listtmp.get(i)).getBeiz();
				if(id == 0) {
					String sql = "insert into gongsb(id,mingc,gongs,leix,zhuangt,beiz) \n" +
							"values(getNewID(999),'"+mingc+"',empty_clob(),'"+leix+"',1,'"+beiz+"')";
					con.getInsert(sql);
				}else {
					String sql = "update gongsb set mingc='"+
					mingc+"',leix='"+leix+"',beiz='"+beiz+"' where id="+id;
					con.getUpdate(sql);
				}
				break;
			}
		}
		
	}
	private void Update(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		List listtmp = getEditValues();
		if(listtmp == null) {
//			错误信息:无需保存记录
			return;
		}
		long id = -1;
		String title = "";
		for(int i =0 ; i< listtmp.size(); i++) {
			if(((GongsBean)listtmp.get(i)).getSelected()) {
				id = ((GongsBean)listtmp.get(i)).getId();
				title = ((GongsBean)listtmp.get(i)).getLeix() 
					+ "  :   "
					+ ((GongsBean)listtmp.get(i)).getMingc();
				break;
			}
		}
		if(id == -1) {
			setMsg("请选择一条记录!");
			return;
		}else {
			visit.setLong1(id);
			visit.setString1(title);
		}
		cycle.activate("Gongsnr");
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
