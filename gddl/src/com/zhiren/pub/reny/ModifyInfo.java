package com.zhiren.pub.reny;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class ModifyInfo extends BasePage implements
		PageValidateListener {
//	可修改的字段
	private String yuanmm;
	private String xinmm;
	private String quanc;
	private String bum;
	private String zhiw;
	private String xingb;
	private String lianxdz;
	private String youzbm;
	private String chuanz;
	private String yiddh;
	private String guddh;
	private String Email;
	private String qianm;
	
	public String getBum() {
		return bum;
	}
	public void setBum(String bum) {
		this.bum = bum;
	}
	public String getChuanz() {
		return chuanz;
	}
	public void setChuanz(String chuanz) {
		this.chuanz = chuanz;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getGuddh() {
		return guddh;
	}
	public void setGuddh(String guddh) {
		this.guddh = guddh;
	}
	public String getLianxdz() {
		return lianxdz;
	}
	public void setLianxdz(String lianxdz) {
		this.lianxdz = lianxdz;
	}
	public String getQianm() {
		return qianm;
	}
	public void setQianm(String qianm) {
		this.qianm = qianm;
	}
	public String getQuanc() {
		return quanc;
	}
	public void setQuanc(String quanc) {
		this.quanc = quanc;
	}
	public String getXingb() {
		return xingb;
	}
	public void setXingb(String xingb) {
		this.xingb = xingb;
	}
	public String getXinmm() {
		return xinmm;
	}
	public void setXinmm(String xinmm) {
		this.xinmm = xinmm;
	}
	public String getYiddh() {
		return yiddh;
	}
	public void setYiddh(String yiddh) {
		this.yiddh = yiddh;
	}
	public String getYouzbm() {
		return youzbm;
	}
	public void setYouzbm(String youzbm) {
		this.youzbm = youzbm;
	}
	public String getYuanmm() {
		return yuanmm;
	}
	public void setYuanmm(String yuanmm) {
		this.yuanmm = yuanmm;
	}
	public String getZhiw() {
		return zhiw;
	}
	public void setZhiw(String zhiw) {
		this.zhiw = zhiw;
	}
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
				this.getPage().getPageName())) {
			visit.setActivePageName(this.getPage().getPageName());
			RefurbishChick();
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
//  刷新用户
    private void RefurbishChick() {
    	Visit visit = (Visit)this.getPage().getVisit();
    	long renyid = visit.getRenyID();
    	JDBCcon con = new JDBCcon();
    	String sql = "select quanc,bum,zhiw,xingb,lianxdz,youzbm,chuanz,yiddh,guddh,email \n" +
    			"from renyxxb where id = "+renyid;
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		错误信息:获得记录集失败.
    		return;
    	}
    	if(rsl.next()) {
    		setQuanc(rsl.getString(0));
    		setBum(rsl.getString(1));
    		setZhiw(rsl.getString(2));
    		setXingb(rsl.getString(3));
    		setLianxdz(rsl.getString(4));
    		setYouzbm(rsl.getString(5));
    		setChuanz(rsl.getString(6));
    		setYiddh(rsl.getString(7));
    		setGuddh(rsl.getString(8));
    		setEmail(rsl.getString(9));
    	}
    	con.Close();
    }
//	按钮事件处理
    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _UpdateChick = false;
    public void UpdateButton(IRequestCycle cycle) {
    	_UpdateChick = true;
    }
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
    		RefurbishChick();
        }
    	if (_UpdateChick) {
    		_UpdateChick = false;
    		Update();
        }
    }
//  保存
	private void Save() {
		Visit visit = (Visit)this.getPage().getVisit();
    	long renyid = visit.getRenyID();
    	JDBCcon con = new JDBCcon();
    	String quanc = getQuanc();
    	String bum = getBum();
    	String zhiw = getZhiw();
    	String xingb = getXingb();
    	String lianxdz = getLianxdz();
    	String youzbm = getYouzbm();
    	String chuanz = getChuanz();
    	String yiddh = getYiddh();
    	String guddh = getGuddh();
    	String email = getEmail();
//    	此处增加 对以上各个字段的验证
//    	.....
//    	验证结束
    	String sql = "update renyxxb set quanc='"+quanc+"',bum='"
    	+bum+"',zhiw='"+zhiw+"',xingb='"+xingb+"',lianxdz='"+lianxdz
    	+"',youzbm='"+youzbm+"',chuanz='"+chuanz+"',yiddh='"+yiddh
    	+"',guddh='"+guddh+"',email='"+email+"' where id="+renyid;
    	con.getUpdate(sql);
    	con.Close();
	}
//  更新密码
	private void Update() {
		Visit visit = (Visit)this.getPage().getVisit();
    	long renyid = visit.getRenyID();
//    	JDBCcon con = new JDBCcon();
    	DataBassUtil dbu = new DataBassUtil();
    	try {
			String yuanmm =dbu.GetStrBlob("renyxxb", "mim", renyid, true);
			if(!yuanmm.equals(getYuanmm())) {
				setMsg("原密码输入不正确!");
				return;
			}
			dbu.UpdateBlob("renyxxb", "mim", renyid, getXinmm(), true);
			setMsg("密码更新成功!");
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
	}
}
