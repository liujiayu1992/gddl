package com.zhiren.dc.caiygl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:����
 * ʱ�䣺2009-07-28
 * */

/*
 * �޸��ˣ�ww
 * �޸�ʱ�䣺 2009-10-25
 * �޸����ݣ���ѡ��������Աʱ����ʾrenyxxb����Ϊ������������Ա�������ò�����
 *  insert into xitxxb values(
	getnewid(diancxxb_id),
	1,diancxxb_id,'������ʾ������','��','','����','1','ʹ��'
    )
 */
public class Zhiyry extends BasePage {
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
    		cycle.activate("Zhiywh");
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Zhiywh");
        }
    }
//  ȡ��������
    public String getGridData() {
    	String sql1;
    	long  diancxxbID = ((Visit) getPage().getVisit()).getDiancxxb_id();
    	boolean zhiyry = "��".equals(MainGlobal.getXitxx_item("����", "������ʾ������", "" + diancxxbID, "��"));
    	if (zhiyry) {
    		sql1="select id,'' checked,quanc mingc from renyxxb where bum='����'";
    	} else {
    		sql1="select id,'' checked,quanc mingc from renyxxb where bum='����'";
    	}
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
		String tiaoj=visit.getString1().substring(0, visit.getString1().length()-1);
		JDBCcon con = new JDBCcon();
		visit.getDiancxxb_id();
		String change[] = getChange().split(";");
		String sqlte;
		sqlte = "delete from zhiyryglb where yangpdhb_id in ("+tiaoj+")";
		con.getDelete(sqlte);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ;i < change.length ; i++) {
			if(change[i] == null || "".equals(change[i])) {
				continue;
			}
			String record[] = change[i].split(",",2);
			String op = record[0];
			String id = record[1];
			if("I".equals(op)) {
				sb.delete(0, sb.length());
				sb.append("begin\n");
				String yangp_id[]=tiaoj.split(",");
				for(int w=0;w<yangp_id.length;w++){
					String abc=yangp_id[w];
					sb.append("insert into zhiyryglb(yangpdhb_id,renyxxb_id) values("+abc+","+id+");\n");
				}
				sb.append("end;");
				con.getInsert(sb.toString());
				
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
