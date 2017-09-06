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
 * �޸���: ww
 * ʱ�䣺      2010-05-24
 * ���� �� ����һҳ��Page���ƴ����Visit.String11()��
 * 		  ������ʱȡ��page����
 */

public class Caiyangry extends BasePage implements PageValidateListener {
//	����ҳ����ʾ��Ϣ������
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
//  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
		}
	}
//	��ť�¼�����

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
//  ȡ��������
    public String getGridData() {
    	Visit visit = (Visit) getPage().getVisit();

    	String sql1="select id,'' checked,quanc mingc from renyxxb where bum='����'";
    	ExtGridUtil egu = new ExtGridUtil();
    	
    	egu.setGridDs(sql1);
    	return egu.getDataset();
    }
//	�������ĸĶ�
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			��ʾ��Ϣ
			setMsg("�޸�Ϊ��!");
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
//	ҳ���ж�����
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
