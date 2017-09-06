package com.zhiren.pub.reny;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Renyz extends BasePage implements PageValidateListener {
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
			getSelectData();
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
    		cycle.activate("Renyxxfj");
        }
    }
//  取得组数据
    public String getGridData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql = "select z.id,(select id from renyzqxb where zuxxb_id = z.id and renyxxb_id = "
    			+ visit.getString1() +") checked,z.mingc,z.beiz "+
    			//" from zuxxb z where z.diancxxb_id in (select distinct diancxxb_id from renyxxb where id = "+visit.getString1()+")  order by mingc";
    			" from zuxxb z where z.diancxxb_id = "+visit.getDiancxxb_id()+" order by mingc";
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
			String sql = "delete from renyzqxb where renyxxb_id = "+ visit.getString1()+ " and zuxxb_id="+id;
			con.getDelete(sql);
			if("I".equals(op)) {
				sql = "insert into renyzqxb(id,renyxxb_id,zuxxb_id) values(getnewid("+visit.getDiancxxb_id()+"),"+visit.getString1()+","+id+")";
				con.getInsert(sql);
			}
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select mingc,beiz from zuxxb z,renyzqxb q \n")
			.append("where q.zuxxb_id = z.id and q.renyxxb_id =")
			.append(visit.getString1()).append(" \n")
			.append("and z.diancxxb_id !=")
			.append(visit.getDiancxxb_id());
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.getColumn("mingc").setHeader("组名");
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		con.Close();
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
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
