package com.zhiren.pub.reny;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Validate;

public abstract class Renyjbxx extends BasePage {
//	����ҳ����ʾ��Ϣ������
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value, false);;
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
//  ȡ�ó�ʼ������
    public String getInitData() {
    	Visit visit = (Visit)this.getPage().getVisit();
    	String initData = "";
    	JDBCcon con = new JDBCcon();
    	String sql = "select quanc,bum,zhiw,xingb,lianxdz,youzbm,chuanz,yiddh,guddh,email \n" +
    			"from renyxxb where id = "+visit.getRenyID();
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		������Ϣ:��ü�¼��ʧ��.
    		return initData;
    	}
    	if(rsl.next()) {
    		initData =	"inxingm = '"+rsl.getString(0)+
    					"';inbum='"+rsl.getString(1)+
    					"';inzhiw='"+rsl.getString(2)+
    					"';inxingb='"+rsl.getString(3)+
    					"';inlianxdz='"+rsl.getString(4)+
    					"';inyouzbm='"+rsl.getString(5)+
    					"';inchuanz='"+rsl.getString(6)+
    					"';inyiddh='"+rsl.getString(7)+
    					"';inguddh='"+rsl.getString(8)+
    					"';inEmail='"+rsl.getString(9)+
    					"';";
    	}
    	con.Close();
    	return initData;
    }
//	��ť�¼�����
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _ReSetPwdChick = false;
    public void ReSetPwdButton(IRequestCycle cycle) {
    	_ReSetPwdChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_ReSetPwdChick) {
    		_ReSetPwdChick = false;
    		ReSetPwd();
        }
    }
//	�������ĸĶ�
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			��ʾ��Ϣ
			setMsg("�޸�Ϊ��!");
			return ;
		}
		String change[] = getChange().split(";",10);
		Visit visit = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
		String sql = "update renyxxb set quanc='"+change[0]+"',bum='"
    	+change[1]+"',zhiw='"+change[2]+"',xingb='"+change[3]+"',lianxdz='"+change[4]
    	+"',youzbm='"+change[5]+"',chuanz='"+change[6]+"',yiddh='"+change[7]
    	+"',guddh='"+change[8]+"',email='"+change[9]+"' where id="+visit.getRenyID();
    	con.getUpdate(sql);
    	con.Close();
	}
//	��������
	private void ReSetPwd() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("�޸�Ϊ��!");
			return ;
		}
		String change[] = getChange().split(",");
		Visit visit = (Visit)this.getPage().getVisit();
    	long renyid = visit.getRenyID();
    	DataBassUtil dbu = new DataBassUtil();
    	try {
			String yuanmm =dbu.GetStrBlob("renyxxb", "mim", renyid, true);
			if(!yuanmm.equals(change[0])) {
				setMsg("ԭ�������벻��ȷ!");
				return;
			}
			dbu.UpdateBlob("renyxxb", "mim", renyid, change[1], true);
			setMsg("������³ɹ�!");
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}
}
