package com.zhiren.jt.jiesgl.report.pub;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.jt.jiesgl.report.changfhs.Changfjsd;
import com.zhiren.jt.jiesgl.report.dianchs.Diancjsd;
import com.zhiren.jt.jiesgl.report.kuangfhs.Kuangfjsd;
import com.zhiren.main.Visit;

/*
 * 修改人：ww
 * 修改时间：2010-06-29
 * 修改内容：将Visit对象传入Changfjsd类
 */

/*
 * 修改人：王耀霆
 * 修改时间：2014-1-9
 * 修改内容：添加一个单批次明细单按钮
 */
/*
 * 修改人：王耀霆
 * 修改时间：2014-1-22
 * 修改内容：在页面初始化的if语句那加上对Danpcmxd的判断
 */
public class Jiesdcz extends BasePage {

	public boolean getRaw() {
		
		return true;
	}

	private int chaxunzt1 = 0;// 查询状态
	private int zhuangt =1;
	private int _CurrentPage = -1;
	private String RT_HET = "jies";
	private String mstrReportName = "jies";
	
	public String getPrintTable() {
		if (mstrReportName.equals(RT_HET)) {
			
			return getSelectData();
			
		} else {
			
			return "无此报表";
		}
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
//	明晰查询
	private boolean _YansmxChick = false;

	public void YansmxButton(IRequestCycle cycle) {
		
		_YansmxChick = true;
	}
	
	private boolean _DanpcmxdChick = false;
	
	public void DanpcmxdButton(IRequestCycle cycle){
		_DanpcmxdChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		
		this.setWindowScript("");
		if(_DanpcmxdChick){
			_DanpcmxdChick = false;
			Danpcmxd();
		}
		
		if(_YansmxChick){
			_YansmxChick = false;
			Yansmx();
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())&&
				!visit.getActivePageName().toString().equals("Yansmxreport")&&
				!visit.getActivePageName().toString().equals("Danpcmxd")
		){
			
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2);//是第一次显示
			visit.setString1("");
			visit.setString2("");
			zhuangt=visit.getInt1();
			chaxunzt1 = 0;
			
//			用来记录结算单
			((Visit) this.getPage().getVisit()).setString15("");
			
			((Visit) getPage().getVisit()).setString3("");	//WindowScript(页面显示)
			
			if(cycle.getRequestContext().getParameters("lx") !=null) {
				
				((Visit) this.getPage().getVisit()).setString15(cycle.getRequestContext().getParameters("lx")[0]);
			}
		}
        
		if(cycle.getRequestContext().getParameters("lx") !=null && !cycle.getRequestContext().getParameters("lx")[0].equals("")) {
			
			((Visit) getPage().getVisit()).setString3("");
			((Visit) this.getPage().getVisit()).setString15(cycle.getRequestContext().getParameters("lx")[0]);
        }

		if(cycle.getRequestContext().getParameters("changf_bianm") !=null) {
			
			if(cycle.getRequestContext().getParameters("changf_bianm")[0].lastIndexOf("(")>-1){
				
				String strtmp=cycle.getRequestContext().getParameters("changf_bianm")[0];
				
				visit.setString1(strtmp.substring(0,strtmp.lastIndexOf("(")));
				chaxunzt1 = 2;
				visit.setString2("changf");
			}else{
				
				visit.setString1(cycle.getRequestContext().getParameters("changf_bianm")[0]);
				chaxunzt1 = 2;
				visit.setString2("changf");
			}
			
        }else if(cycle.getRequestContext().getParameters("kuangf_bianm") !=null){
        	
        	if(cycle.getRequestContext().getParameters("kuangf_bianm")[0].lastIndexOf("(")>-1){
        		
        		String strtmp=cycle.getRequestContext().getParameters("kuangf_bianm")[0];
        		visit.setString1(cycle.getRequestContext().getParameters("kuangf_bianm")[0]);
        		chaxunzt1 = 2;
    			visit.setString2("kuangf");
        	}else{
        		
        		visit.setString1(cycle.getRequestContext().getParameters("kuangf_bianm")[0]);
    			chaxunzt1 = 2;
    			visit.setString2("kuangf");
        	}
        }else if(cycle.getRequestContext().getParameters("dianc_bianm") !=null){
        	
        	if(cycle.getRequestContext().getParameters("dianc_bianm")[0].lastIndexOf("(")>-1){
        		
        		String strtmp=cycle.getRequestContext().getParameters("dianc_bianm")[0];
        		visit.setString1(cycle.getRequestContext().getParameters("dianc_bianm")[0]);
        		chaxunzt1 = 2;
    			visit.setString2("dianc");
        	}else{
        		
        		visit.setString1(cycle.getRequestContext().getParameters("dianc_bianm")[0]);
    			chaxunzt1 = 2;
    			visit.setString2("dianc");
        	}
        }
		
		if(zhuangt==1){//不要动
			visit.setInt1(1);
		}
		if(zhuangt==2){//不要动
			visit.setInt1(2);
		}
		zhuangt=1;
	}

	public String getSelectData() {
		
		Visit visit = (Visit)getPage().getVisit();
//		String15为结算单查询的参数
		
		String Temp=((Visit) this.getPage().getVisit()).getString15();
		String Type="";
		String bianm="";
		
		Type=Temp.substring(0,Temp.indexOf(","));
		bianm=Temp.substring(Temp.indexOf(",")+1);
				
		if(Type.equals("changf")){
			
			Changfjsd jsd = new Changfjsd();
			StringBuffer sb = new StringBuffer();
			setAllPages(1);
			jsd.setAllPages(1);
//			sb.append(jsd.getChangfjsd("",0,"diancjsmkb,diancjsyfb"));
			sb.append(jsd.getChangfjsd(bianm,0,"diancjsmkb,diancjsyfb"));
			setCurrentPage(1);
			return sb.toString();
		}else if(Type.equals("kuangf")){
			Changfjsd jsd = new Changfjsd();
//			Kuangfjsd jsd = new Kuangfjsd();
			StringBuffer sb = new StringBuffer();
			setAllPages(1);
			jsd.setAllPages(1);
			sb.append(jsd.getChangfjsd(bianm,0,"kuangfjsmkb,kuangfjsyfb"));
//			sb.append(jsd.getKuangfjsd(bianm,0));
			setCurrentPage(1);
			return sb.toString();
		}else if(Type.equals("dianc")){
			
			Changfjsd jsd = new Changfjsd();
			StringBuffer sb = new StringBuffer();
			setAllPages(1);
			jsd.setAllPages(1);
			jsd.setVisit(visit); //传入Changfjsd类Visit对象
			sb.append(jsd.getChangfjsd(bianm,0,"jiesb,jiesyfb"));
			setCurrentPage(1);
			return sb.toString();
		}else{
			return "";
		}				
	}
	
	
	public void Danpcmxd() {

		String Temp=((Visit) this.getPage().getVisit()).getString15();
		String Type="";
		String bianm="";
		String talbename="jiesb,jiesyfb";
		
		Type=Temp.substring(0,Temp.indexOf(","));
		bianm=Temp.substring(Temp.indexOf(",")+1);
		
		if(Type.equals("changf")){
			talbename="diancjsmkb,diancjsyfb";
		}else if(Type.equals("kuangf")){
			talbename="kuangfjsmkb,kuangfjsyfb";
		}else if (Type.equals("dianc")){
			talbename="jiesb,jiesyfb";
		}
			
		String str =
				" var url = \"http://\"+document.location.host+document.location.pathname; \n" +
				" var end = url.indexOf(\";\");\n" +
				" url = url.substring(0,end);\n" +
				" url=url+'?service=page/Danpcmxd&lx='+'"+talbename+";"+bianm+"';\n" +
				" window.open(url,'Danpcmxd');";
		
			zhuangt = 2;
			((Visit) this.getPage().getVisit()).setInt1(2);
			this.setWindowScript(str);
	}
	
	public void Yansmx() {

		String Temp=((Visit) this.getPage().getVisit()).getString15();
		String Type="";
		String bianm="";
		String talbename="jiesb,jiesyfb";
		
		Type=Temp.substring(0,Temp.indexOf(","));
		bianm=Temp.substring(Temp.indexOf(",")+1);
		
		if(Type.equals("changf")){
			talbename="diancjsmkb,diancjsyfb";
		}else if(Type.equals("kuangf")){
			talbename="kuangfjsmkb,kuangfjsyfb";
		}else if (Type.equals("dianc")){
			talbename="jiesb,jiesyfb";
		}
			
		String str =
				" var url = \"http://\"+document.location.host+document.location.pathname; \n" +
				" var end = url.indexOf(\";\");\n" +
				" url = url.substring(0,end);\n" +
				" url=url+'?service=page/Yansmxreport&lx='+'"+talbename+";"+bianm+","+Type+"';\n" +
				" window.open(url,'Yansmx');";
		
			zhuangt = 2;
			((Visit) this.getPage().getVisit()).setInt1(2);
			this.setWindowScript(str);
	}
	
//	窗口弹出js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString3();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString3(value);
	}
//	窗口弹出js_end
}
