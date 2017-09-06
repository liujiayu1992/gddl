package com.zhiren.pub.wenjgl;


import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Wenjfbll extends BasePage  {
	private boolean xiansnr = false;
    public void xiansnr(IRequestCycle cycle) {
    	xiansnr = true;
    }
    private boolean shuax = false;
    public void shuax(IRequestCycle cycle) {
    	shuax = true;
    }
	public void submit(IRequestCycle cycle) {
		if (shuax) {
			shuax = false;
			loadDate();
        }
		if (xiansnr) {
			xiansnr = false;
			xians();
        }
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//显示
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			loadDate();
			this.setRiq(DateUtil.FormatDate(visit.getMorkssj()));
			this.setriq1(DateUtil.FormatDate(visit.getMorjssj())); 
		}
	}
	private void xians(){
		JDBCcon con=new JDBCcon();
//		String tem="var wenjnr='";
//		String sql=
//		"select neir\n" +
//		"from wenjb\n" + 
//		"where id="+getChange();
		String biaot="";
		String neir="";
		String yuanmc="";
		String url="";
		String fujbItem=" fujb='";
		String sql="select wenjb.id,Replace(wenjb.neir,chr(13)||chr(10),'')neir,biaot,fujb.url,yuanmc\n" +
		"from wenjb,fujb\n" + 
		"where wenjb.id=fujb.wenjb_id and wenjb.id='"+getChange()+"'";
		ResultSet rs=con.getResultSet(sql);
		try{
			while(rs.next()){
				biaot=" wenjbt='"+rs.getString("biaot")+"';\n";
				neir=" wenjnr='"+rs.getString("neir")+"';\n";
				yuanmc=rs.getString("yuanmc");
				url=rs.getString("url");
//				fujbItem+= "<a href="+url+" target=_blank>"+yuanmc+"</a><br>";
				fujbItem+= "<a  onclick=\"window.open(\\'"+url+"\\')\" href=\"#\" >"+yuanmc+"</a><br>";
				
			}
			fujbItem+="';\n";
			setWenjnr(biaot+neir+fujbItem);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	
	private void loadDate(){
		// 取数据
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		StringBuffer date=new StringBuffer();
		int i=0;
		try{
			String sql="";
			sql=
				"select wenjb.id,renyxxb.quanc,to_char(fabwjb.shij,'YYYY-MM-DD HH24:MI:SS')shij,wenjb.biaot,wenjb.leix\n" +
				"from  fabwjb,wenjb,renyxxb\n" + 
				"where fabwjb.wenjb_id=wenjb.id and fabwjb.renyxxb_id=renyxxb.id and fabwjb.diancxxb_id="+visit.getDiancxxb_id()+" and  to_char(fabwjb.youxq,'YYYY-MM-DD')>=to_char(sysdate,'YYYY-MM-DD')\n" + 
				"and to_char(fabwjb.shij,'YYYY-MM-DD')>='"+getRiq()+"' and  to_char(fabwjb.shij,'YYYY-MM-DD')<='"+getriq1()+"'";
			ResultSet rs=con.getResultSet(sql);
			date.append(" var TongZ = [\n");
			while(rs.next()){
				if(i==0){
					date.append("['"+rs.getString("id")+"','"+rs.getString("quanc")+"','"+rs.getString("shij")+"','"+rs.getString("biaot")+"','"+rs.getString("leix")+"']\n");
				}else{
					date.append(",['"+rs.getString("id")+"','"+rs.getString("quanc")+"','"+rs.getString("shij")+"','"+rs.getString("biaot")+"','"+rs.getString("leix")+"']\n");
				}
				i++;
			}
			date.append("];\n");
			setGridData(date.toString());
//			//
//			//树数据
//			TreeNodes tns=new TreeNodes("diancxxb",visit.getDiancxxb_id(),"mingc",2,true);//类型0:树1：上级2：树和上级
//			setTreeData(tns.getScript());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}

	
	private String GridData;
	public String getGridData(){
		return GridData;
	}
	public void setGridData(String value){
		this.GridData=value;
	}
	private String TreeData;
	public String getTreeData(){
		return TreeData;
	}
	public void setTreeData(String value){
		this.TreeData=value;
	}
	private String riq1;
	public String getriq1() {
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setriq1(String riq1) {
		this.riq1 = riq1;
	}
	private String riq;
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -15, DateUtil.AddType_intDay));
		}
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    private String wenjnr;
	public String getWenjnr() {
		if(wenjnr==null||wenjnr.equals("")){
			wenjnr="var wenjnr='';";
		}
		return wenjnr;
	}
	public void setWenjnr(String wenjnr) {
		this.wenjnr = wenjnr;
	}
    
}