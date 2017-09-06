package com.zhiren.main;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.MainGlobal;
import com.zhiren.main.validate.Login;

public abstract class LoginMain extends BasePage {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//	用户名
	public String getUserName() {
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setUserName(String title) {
		((Visit)this.getPage().getVisit()).setString1(title);
	}
//	密码
	public String getPassWord() {
		return ((Visit)this.getPage().getVisit()).getString2();
	}
	public void setPassWord(String gongs) {
		((Visit)this.getPage().getVisit()).setString2(gongs);
	}
    
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			setUserName(null);
			setPassWord(null);
		}
	}
    public void submit(IRequestCycle cycle) {
		Login(cycle);
    }
    private void Login(IRequestCycle cycle) {
    	String userName = getUserName();
    	String pwd = getPassWord();
    	String LoginPageName = Login.MainLogin(this.getPage(), userName, pwd);
    	if(!LoginPageName.equals(this.getPageName())) {
    		int intErrCode = ((Visit)getPage().getVisit()).getErrcode();
    		setMsg(MainGlobal.getErrMsg(intErrCode));
    		return;
//    		throw new PageRedirectException(LoginPageName);
    	}
    	cycle.activate("Main");
    }
    
    public String getImgPath() {
		String imgpath = "imgs/main/"+MainGlobal.getStyleColor();
		return imgpath;
	}
    
	public String getImgSrc(String imgName) {
		return getImgPath()+"/"+imgName;
	}
	
	public String getLoginLogo() {
		return "imgs/login/" + MainGlobal.getLogoPath() + "/login_new_logo.jpg";
	}

	public String getSpacer() {
		return getImgSrc("spacer.gif");
	}

	public String getBai() {
		String returnString="";
		if (MainGlobal.getStyleColor().equals("blue")){
			returnString="<table width=\"100\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n";
			returnString=returnString + "<tr>\n";
			returnString=returnString + "<td><img src=\"" + getImgSrc("spacer.gif") +"\" width=\"1\" height=\"327\"/></td>\n";
			returnString=returnString + "</tr>\n</table>";
			return returnString;
		}else{
			returnString="<table width=\"1004\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n";
			returnString=returnString + "<tr>\n";
			returnString=returnString + "<td><img src=\"" + getImgSrc("login_bai.gif") +"\" width=\"807\" height=\"242\"/></td>\n";
			returnString=returnString + "</tr>\n";
			returnString=returnString + "<tr>\n";
			returnString=returnString + "<td><img src=\"" + getImgSrc("spacer.gif") +"\" width=\"1\" height=\"85\"/></td>\n";
			returnString=returnString + "</tr>\n</table>";
			return returnString;
		}
	}
	
	public String getGBlight() {
		return getImgSrc("glassBanner_light.gif");
	}
	
	public String getGBuser() {
		return getImgSrc("glassBanner_user.gif");
	}
	
	public String getGBpw() {
		return getImgSrc("glassBanner_pw.gif");
	}
	
	public String getGBbg() {
		return getImgSrc("glassBanner_bg.gif");
	}
	
	public String getLoginbt() {
		return getImgSrc("login_bt.gif");
	}
	
	public String getPowerby() {
		return getImgSrc("PowerBy_style01.gif");
	}

	public String getLoginbg() {
//		if (MainGlobal.getStyleColor().equals("blue")){
			return "background-image: url("  + getImgSrc("login_bg.gif")+ ");";
//		}else{
//			return "";
//		}
			
	}
	public String getColor() {
		if (MainGlobal.getStyleColor().equals("blue")){
			return "#4179B8";
		}else{
			return "#323232";
		}
	}
	
}
