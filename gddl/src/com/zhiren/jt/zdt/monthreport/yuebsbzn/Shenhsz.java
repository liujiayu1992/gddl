package com.zhiren.jt.zdt.monthreport.yuebsbzn;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
/* 
* 时间：2009-04-07 
* 作者： ll
* 修改内容：  由于xitxxb中mingc:'中能Ftp月报用户'和'中能Ftp月报密码'字段内容改变，
* 				所以修改页面初始化sql.
*/
/* 
* 时间：2009-04-14
* 作者： ll
* 修改内容：1、在xitxxb中增加，连接中能SQLServer数据库的用户名和密码。
* 			2、页面增加，连接中能SQLServer数据库的用户名和密码
*/  
public abstract class Shenhsz extends BasePage {
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
//  取得初始化数据
    public String getInitData() {
    	Visit visit = (Visit)this.getPage().getVisit();
    	String initData = "";
    	JDBCcon con = new JDBCcon();
    	String sql = 
			"select ip.zhi,yh.zhi,m.zhi,sqlyh.zhi,sqlm.zhi\n" +
			"from (select diancxxb_id as id,zhi from xitxxb where mingc ='中能Ftp服务器ip')ip,\n" + 
				 "(select diancxxb_id as id,zhi from xitxxb where mingc ='中能Ftp月报用户')yh,\n" + 
			     "(select diancxxb_id as id,zhi from xitxxb where mingc ='中能Ftp月报密码')m,\n" +
			     "(select diancxxb_id as id,zhi from xitxxb where mingc ='中能SQLServer用户')sqlyh,\n" +
			     "(select diancxxb_id as id,zhi from xitxxb where mingc ='中能SQLServer密码')sqlm,diancxxb dc\n" + 
			"where dc.id=ip.id and dc.id=yh.id and dc.id=m.id\n" + 
				 "and dc.id="+visit.getDiancxxb_id();
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		错误信息:获得记录集失败.
    		return initData;
    	
//    		StringBuffer sql1=new StringBuffer();
//        	sql1.append("begin \n");
//        	sql1.append("insert into xitxxb (set zhi='"+change[0]+"'where mingc='中能Ftp服务器ip';\n");
//    		sql1.append("update xitxxb set zhi='"+change[1]+"'where mingc='中能Ftp用户';\n");
//    		sql1.append("update xitxxb set zhi='"+change[2]+"'where mingc='中能Ftp密码';\n");
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
    		cycle.activate("Yuebsbzn");
        }
    }
//	保存分组的改动
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
		String change[] = getChange().split(";",10);
		Visit visit = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
    	StringBuffer sql=new StringBuffer();
    	sql.append("begin \n");
    	sql.append("update xitxxb set zhi='"+change[0]+"'where mingc='中能Ftp服务器ip';\n");
		sql.append("update xitxxb set zhi='"+change[1]+"'where mingc='中能Ftp月报用户';\n");
		sql.append("update xitxxb set zhi='"+change[2]+"'where mingc='中能Ftp月报密码';\n");
		sql.append("update xitxxb set zhi='"+change[3]+"'where mingc='中能SQLServer用户';\n");
		sql.append("update xitxxb set zhi='"+change[4]+"'where mingc='中能SQLServer密码';\n");
		sql.append("end;");
		
//    	String sql = "update renyxxb set quanc='"+change[0]+"',bum='"
//    	+change[1]+"',zhiw='"+change[2]+"',xingb='"+change[3]+"',lianxdz='"+change[4]
//    	+"',youzbm='"+change[5]+"',chuanz='"+change[6]+"',yiddh='"+change[7]
//    	+"',guddh='"+change[8]+"',email='"+change[9]+"' where id="+visit.getRenyID();
    	con.getUpdate(sql.toString());
    	con.Close();
	}

}
