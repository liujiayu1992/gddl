package com.zhiren.pub.reny;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.main.validate.Validate;

public class Renyxx extends BasePage implements PageValidateListener {
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
    
    private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
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
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    }
//  取得组数据
    public String getGridData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql = "select id,mingc,quanc,xingb,bum,zhiw,decode(zhuangt,1,'是','否') zhuangt,yiddh,guddh,chuanz,youzbm,email,lianxdz from  renyxxb where diancxxb_id = "
    			+visit.getDiancxxb_id()+" order by mingc";
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
			String record[] = change[i].split(",",14);
			if("D".equals(record[0])) {
				String id = record[1]; 
				String sql = "delete from renyxxb where id ="+id;
				con.getDelete(sql);
			}else
				if("U".equals(record[0])) {
					String id = record[1];
					String mingc = record[2];
					String quanc = record[3];
					String xingb = record[4];
					String bum   = record[5];
					String zhiw  = record[6];
					String zhuangt = "是".equals(record[7])?"1":"0";
					String yiddh = record[8];
					String guddh = record[9];
					String chuanz = record[10];
					String youzbm = record[11];
					String Email = record[12];
					String lianxdz = record[13];
					if(id.equals("0")) {
						if(Validate.UserExists(mingc,"0")) {
							setMsg("用户 "+mingc+" 已存在!");
							continue;
						}
						id = MainGlobal.getNewID(visit.getDiancxxb_id());
						String sql = "insert into renyxxb(id, mingc, quanc, mim, xingb,bum,zhiw,yiddh,guddh,chuanz,lianxdz,youzbm,email, diancxxb_id, zhuangt) values("+id+",'"
						+ mingc + "','"+quanc+"',empty_blob(),'"+xingb+"','"+bum+"','"+zhiw+"','"+yiddh+"','"+guddh+"','"+chuanz+"','"+lianxdz+"','"+youzbm+"','"+Email+"',"+visit.getDiancxxb_id()+","+zhuangt+")";
						con.getInsert(sql);
						DataBassUtil dbu = new DataBassUtil();
						try {
							dbu.UpdateBlob("renyxxb", "mim", Long.parseLong(id), mingc,true);
						}catch(Exception e) {
							e.printStackTrace();
							continue;
						}
					}else {
						if(Validate.UserExists(mingc,id)) {
							setMsg("用户 "+mingc+" 已存在!");
							continue;
						}
						String sql = "update renyxxb set mingc='"+mingc+"',quanc='"+quanc
						+"',xingb='"+xingb+"',bum='"+bum+"',zhiw='"+zhiw+"',zhuangt='"+zhuangt
						+"',yiddh='"+yiddh+"',guddh='"+guddh+"',chuanz='"+chuanz+"',lianxdz='"+lianxdz
						+"',youzbm='"+youzbm+"',email='"+Email+"' where id =" + id;
						con.getUpdate(sql);
					}
				}
		}
	}
//	设置分组
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个人员设置分组!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Renyz");
	}
//	重置密码
	private void ReSetPwd() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个人员重置密码!");
			return;
		}
		String c[]  = getChange().split(",");
		long id = Long.parseLong(c[0]);
		String mingc = c[1];
		if(id == 0) {
			return ;
		}else {
			DataBassUtil dbu = new DataBassUtil();
			try {
				dbu.UpdateBlob("renyxxb", "mim", id, mingc,true);
//				setMsg(dbu.GetStrBlob("renyxxb", "mim", id,true));
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
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
