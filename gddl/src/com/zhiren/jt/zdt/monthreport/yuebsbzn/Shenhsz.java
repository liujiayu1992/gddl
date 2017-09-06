package com.zhiren.jt.zdt.monthreport.yuebsbzn;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
/* 
* ʱ�䣺2009-04-07 
* ���ߣ� ll
* �޸����ݣ�  ����xitxxb��mingc:'����Ftp�±��û�'��'����Ftp�±�����'�ֶ����ݸı䣬
* 				�����޸�ҳ���ʼ��sql.
*/
/* 
* ʱ�䣺2009-04-14
* ���ߣ� ll
* �޸����ݣ�1����xitxxb�����ӣ���������SQLServer���ݿ���û��������롣
* 			2��ҳ�����ӣ���������SQLServer���ݿ���û���������
*/  
public abstract class Shenhsz extends BasePage {
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
//  ȡ�ó�ʼ������
    public String getInitData() {
    	Visit visit = (Visit)this.getPage().getVisit();
    	String initData = "";
    	JDBCcon con = new JDBCcon();
    	String sql = 
			"select ip.zhi,yh.zhi,m.zhi,sqlyh.zhi,sqlm.zhi\n" +
			"from (select diancxxb_id as id,zhi from xitxxb where mingc ='����Ftp������ip')ip,\n" + 
				 "(select diancxxb_id as id,zhi from xitxxb where mingc ='����Ftp�±��û�')yh,\n" + 
			     "(select diancxxb_id as id,zhi from xitxxb where mingc ='����Ftp�±�����')m,\n" +
			     "(select diancxxb_id as id,zhi from xitxxb where mingc ='����SQLServer�û�')sqlyh,\n" +
			     "(select diancxxb_id as id,zhi from xitxxb where mingc ='����SQLServer����')sqlm,diancxxb dc\n" + 
			"where dc.id=ip.id and dc.id=yh.id and dc.id=m.id\n" + 
				 "and dc.id="+visit.getDiancxxb_id();
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		������Ϣ:��ü�¼��ʧ��.
    		return initData;
    	
//    		StringBuffer sql1=new StringBuffer();
//        	sql1.append("begin \n");
//        	sql1.append("insert into xitxxb (set zhi='"+change[0]+"'where mingc='����Ftp������ip';\n");
//    		sql1.append("update xitxxb set zhi='"+change[1]+"'where mingc='����Ftp�û�';\n");
//    		sql1.append("update xitxxb set zhi='"+change[2]+"'where mingc='����Ftp����';\n");
//    		sql1.append("end;");
//
//        	con.getUpdate(sql.toString());
    	}
    	if(rsl.next()) {
    		initData =	"indizip = '"+rsl.getString(0)+
    					"';inxingm='"+rsl.getString(1)+
    					"';inmim='"+rsl.getString(2)+
    					"';inxingmsql='"+rsl.getString(3)+
    					"';inmimsql='"+rsl.getString(4)+
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
    		cycle.activate("Yuebsbzn");
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
    	StringBuffer sql=new StringBuffer();
    	sql.append("begin \n");
    	sql.append("update xitxxb set zhi='"+change[0]+"'where mingc='����Ftp������ip';\n");
		sql.append("update xitxxb set zhi='"+change[1]+"'where mingc='����Ftp�±��û�';\n");
		sql.append("update xitxxb set zhi='"+change[2]+"'where mingc='����Ftp�±�����';\n");
		sql.append("update xitxxb set zhi='"+change[3]+"'where mingc='����SQLServer�û�';\n");
		sql.append("update xitxxb set zhi='"+change[4]+"'where mingc='����SQLServer����';\n");
		sql.append("end;");
		
//    	String sql = "update renyxxb set quanc='"+change[0]+"',bum='"
//    	+change[1]+"',zhiw='"+change[2]+"',xingb='"+change[3]+"',lianxdz='"+change[4]
//    	+"',youzbm='"+change[5]+"',chuanz='"+change[6]+"',yiddh='"+change[7]
//    	+"',guddh='"+change[8]+"',email='"+change[9]+"' where id="+visit.getRenyID();
    	con.getUpdate(sql.toString());
    	con.Close();
	}

}
