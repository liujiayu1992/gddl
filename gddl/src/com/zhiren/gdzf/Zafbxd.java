package com.zhiren.gdzf;

import java.sql.ResultSet;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.WriteLog;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zafbxd extends BasePage {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public StringBuffer getCaozrSql() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
			sb.append("select gz.liucdzbmc,\n" +
							"       gz.caozy,\n" + 
							"       to_char(gz.shij, 'yyyy-mm-dd hh24:mi:ss') shij,\n" + 
							"       nvl(r.zhiw, '') zhiw,\n" + 
							"       nvl(gz.miaos, '') miaos\n" + 
							"  from liucgzb gz, zaffybxd z, renyxxb r\n" + 
							" where gz.liucgzid = z.id\n" + 
							"   and z.bianh = '"+visit.getString5()+"'\n" + 
							"   and gz.caozy = r.quanc(+) order by shij");
		return sb;
	}
	public String getPrintTableU(){
		Visit visit = (Visit) getPage().getVisit();
		Report rt=new Report();
		JDBCcon con = new JDBCcon();
		if(visit.getString5()==""){
			con.Close();
			return "";
		}
		try{		
			ResultSet rsc = con.getResultSet(getCaozrSql(),
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (rsc == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult);
				setMsg(ErrorMessage.NullResult);
				return "";
			}

			rsc.last();
			int footerlength = 5;
			if(rsc.getRow()!=0){
				footerlength = rsc.getRow()*5;
			}
			 String ArrBody[][]=new String[footerlength][2];
			 		 rsc.beforeFirst();
			int j=0;
			while(rsc.next()){
				if(rsc.getString("liucdzbmc").equals("发起")){
					 ArrBody[j]=new String[] {"",""};
					 ArrBody[j+1]=new String[] {"提交人：",rsc.getString("caozy")};
					 ArrBody[j+2]=new String[] {"职位：",rsc.getString("zhiw")};
					 ArrBody[j+3]=new String[] {"提交时间：",rsc.getString("shij")};
					 ArrBody[j+4]=new String[] {"",""};	
				}else{
					ArrBody[j]=new String[] {"审批人：",rsc.getString("caozy")};
					 ArrBody[j+1]=new String[] {"职位：",rsc.getString("zhiw")};
					 ArrBody[j+2]=new String[] {"审批时间：",rsc.getString("shij")};
					 ArrBody[j+3]=new String[] {"审批意见：",rsc.getString("miaos")};
					 ArrBody[j+4]=new String[] {"",""};
				}
				j+=5;
			}
			rsc.close();
			int ArrWidth[]=new int[] {80,1000};
			rt.setBody(new Table(ArrBody,0,0,0));
			rt.body.setWidth(ArrWidth);
			rt.body.ShowZero = true;
			 rt.body.setBorder(0,0,0,0);
			 rt.body.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
			 for(int l=1;l<=footerlength+1;l++){
				 rt.body.setRowCells(l,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(l,Table.PER_BORDER_RIGHT,0);
			 }
		}catch(Exception e) {
			// TODO 自动生成方法存根
					e.printStackTrace();
		}
		
		con.Close();
		return rt.getAllPagesHtml();
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setString5("");
			String reportType = cycle.getRequestContext().getParameter("bh");
			if(reportType != null) {
				visit.setString5(reportType);
			}
			visit.setActivePageName(getPageName().toString());
			getPrintTableU();
		}
	}
//	页面登陆验证
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
}
