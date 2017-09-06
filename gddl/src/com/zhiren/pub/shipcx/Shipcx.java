package com.zhiren.pub.shipcx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shipcx extends BasePage implements PageValidateListener {

	// 视频文件路径
	private String Filepath="ftp://10.67.204.11/Video/";
	private String msg = "";
	private String table_id="";
	
	protected void initialize() {
		msg = "";
	}
	public boolean getRaw() {
		return true;
	}

	public String getFilepath() {
		return Filepath;
	}

	public void setFilepath(String filepath) {
		Filepath = filepath;
	}
	
	public String getMsg() {
		return msg;
	} 
	
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
    public String getPrintTable(){
    	return LeftTable();
    }
    public String LeftTable(){
    //设置采样点的颜色
       JDBCcon cn=new JDBCcon();
       String sql="select caiyd from jianjghb where id="+table_id+"\n";
       ResultSetList rs=cn.getResultSetList(sql);
       String str="";
       while(rs.next()){
    	   str=rs.getString("caiyd");
       }
   
       String []StrArr =str.split(",");
       String A="";
       String B="";
       String C="";
       String D="";
       String E="";
       String F="";
       String G="";
       String H="";
       String I="";
       String J="";
       String K="";
       String L="";
       String M="";
       String N="";
       String O="";
       String P="";
       String Q="";
       String R="";
       for(int i=0;i<StrArr.length;i++){
    	   	if(StrArr[i].equals("A")){
    	   		A="<td bgcolor=\"fuchsia\" align=\"center\">A</td>\n";
    	   	}else{
    	   		if(A.equals("<td bgcolor=\"fuchsia\" align=\"center\">A</td>\n")){
    	   			A="<td bgcolor=\"fuchsia\" align=\"center\">A</td>\n";
    	   		}else{
    	   		A="<td align=\"center\">A</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("B")){
    	   		B="<td bgcolor=\"fuchsia\" align=\"center\">B</td>\n";
    	   	}else{
    	   		if(B.equals("<td bgcolor=\"fuchsia\" align=\"center\">B</td>\n")){
    	   			B="<td bgcolor=\"fuchsia\" align=\"center\">B</td>\n";
    	   		}else{
    	   		B="<td  align=\"center\">B</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("C")){
    	   		C="<td bgcolor=\"fuchsia\" align=\"center\">C</td>\n";
    	   	}else{
    	   		if(C.equals("<td bgcolor=\"fuchsia\" align=\"center\">C</td>\n")){
    	   			C="<td bgcolor=\"fuchsia\" align=\"center\">C</td>\n";
    	   		}else{
    	   		C="<td  align=\"center\">C</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("D")){
    	   		D="<td bgcolor=\"fuchsia\" align=\"center\">D</td>\n";
    	   	}else{
    	   		if(D.equals("<td bgcolor=\"fuchsia\" align=\"center\">D</td>\n")){
    	   			D="<td bgcolor=\"fuchsia\" align=\"center\">D</td>\n";
    	   		}else{
    	   		D="<td align=\"center\">D</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("E")){
    	   		E="<td bgcolor=\"fuchsia\" align=\"center\">E</td>\n";
    	   	}else{
    	   		if(E.equals("<td bgcolor=\"fuchsia\" align=\"center\">E</td>\n")){
    	   			E="<td bgcolor=\"fuchsia\" align=\"center\">E</td>\n";
    	   		}else{
    	   		E="<td  align=\"center\">E</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("F")){
    	   		F="<td bgcolor=\"fuchsia\" align=\"center\">F</td>\n";
    	   	}else{
    	   		if(F.equals("<td bgcolor=\"fuchsia\" align=\"center\">F</td>\n")){
    	   			F="<td bgcolor=\"fuchsia\" align=\"center\">F</td>\n";
    	   		}else{
    	   		F="<td align=\"center\">F</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("G")){
    	   		G="<td bgcolor=\"fuchsia\" align=\"center\">G</td>\n";
    	   	}else{
    	   		if(G.equals("<td bgcolor=\"fuchsia\" align=\"center\">G</td>\n")){
    	   			G="<td bgcolor=\"fuchsia\" align=\"center\">G</td>\n";
    	   		}else{
    	   		G="<td align=\"center\">G</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("H")){
    	   		H="<td bgcolor=\"fuchsia\" align=\"center\">H</td>\n";
    	   	}else{
    	   		if(H.equals("<td bgcolor=\"fuchsia\" align=\"center\">H</td>\n")){
    	   			H="<td bgcolor=\"fuchsia\" align=\"center\">H</td>\n";
    	   		}else{
    	   		H="<td align=\"center\">H</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("I")){
    	   		I="<td bgcolor=\"fuchsia\" align=\"center\">I</td>\n";
    	   	}else{
    	   		if(I.equals("<td bgcolor=\"fuchsia\" align=\"center\">I</td>\n")){
    	   			I="<td bgcolor=\"fuchsia\" align=\"center\">I</td>\n";
    	   		}else{
    	   		I="<td align=\"center\">I</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("J")){
    	   		J="<td bgcolor=\"fuchsia\" align=\"center\">J</td>\n";
    	   	}else{
    	   		if(J.equals("<td bgcolor=\"fuchsia\" align=\"center\">J</td>\n")){
    	   			J="<td bgcolor=\"fuchsia\" align=\"center\">J</td>\n";
    	   		}else{
    	   		J="<td align=\"center\">J</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("K")){
    	   		K="<td bgcolor=\"fuchsia\" align=\"center\">K</td>\n";
    	   	}else{
    	   		if(K.equals("<td bgcolor=\"fuchsia\" align=\"center\">K</td>\n")){
    	   			K="<td bgcolor=\"fuchsia\" align=\"center\">K</td>\n";
    	   		}else{
    	   		K="<td align=\"center\">K</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("L")){
    	   		L="<td bgcolor=\"fuchsia\" align=\"center\">L</td>\n";
    	   	}else{
    	   		if(L.equals("<td bgcolor=\"fuchsia\" align=\"center\">L</td>\n")){
    	   			L="<td bgcolor=\"fuchsia\" align=\"center\">L</td>\n";
    	   		}else{
    	   		L="<td align=\"center\">L</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("M")){
    	   		M="<td bgcolor=\"fuchsia\" align=\"center\">M</td>\n";
    	   	}else{
    	   		if(M.equals("<td bgcolor=\"fuchsia\" align=\"center\">M</td>\n")){
    	   			M="<td bgcolor=\"fuchsia\" align=\"center\">M</td>\n";
    	   		}else{
    	   		M="<td align=\"center\">M</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("N")){
    	   		N="<td bgcolor=\"fuchsia\" align=\"center\">N</td>\n";
    	   	}else{
    	   		if(N.equals("<td bgcolor=\"fuchsia\" align=\"center\">N</td>\n")){
    	   			N="<td bgcolor=\"fuchsia\" align=\"center\">N</td>\n";
    	   		}else{
    	   		N="<td align=\"center\">N</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("O")){
    	   		O="<td bgcolor=\"fuchsia\" align=\"center\">O</td>\n";
    	   	}else{
    	   		if(O.equals("<td bgcolor=\"fuchsia\" align=\"center\">O</td>\n")){
    	   			O="<td bgcolor=\"fuchsia\" align=\"center\">O</td>\n";
    	   		}else{
    	   		O="<td align=\"center\">O</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("P")){
    	   		P="<td bgcolor=\"fuchsia\" align=\"center\">P</td>\n";
    	   	}else{
    	   		if(P.equals("<td bgcolor=\"fuchsia\" align=\"center\">P</td>\n")){
    	   			P="<td bgcolor=\"fuchsia\" align=\"center\">P</td>\n";
    	   		}else{
    	   		P="<td align=\"center\">P</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("Q")){
    	   		Q="<td bgcolor=\"fuchsia\" align=\"center\">Q</td>\n";
    	   	}else{
    	   		if(Q.equals("<td bgcolor=\"fuchsia\" align=\"center\">Q</td>\n")){
    	   			Q="<td bgcolor=\"fuchsia\" align=\"center\">Q</td>\n";
    	   		}else{
    	   		Q="<td align=\"center\">Q</td>\n";
    	   		}
    	   	}
    		if(StrArr[i].equals("R")){
    	   		R="<td bgcolor=\"fuchsia\" align=\"center\">R</td>\n";
    	   	}else{
    	   		if(R.equals("<td bgcolor=\"fuchsia\" align=\"center\">R</td>\n")){
    	   			R="<td bgcolor=\"fuchsia\" align=\"center\">R</td>\n";
    	   		}else{
    	   		R="<td align=\"center\">R</td>\n";
    	   		}
    	   	}
    	    }
       String ad=
    	   "<tr height=\"35\">\n" +
    	   R + 
    	   O + 
    	   L + 
    	   I + 
    	   F + 
    	   C + 
    	   "</tr>\n" + 
    	   "<tr height=\"35\">\n" + 
    	   Q + 
    	   N + 
    	   K + 
    	   H + 
    	   E + 
    	   B + 
    	   "</tr>\n" + 
    	   "<tr height=\"35\">\n" + 
    	   P + 
    	   M + 
    	   J + 
    	   G + 
    	   D + 
    	   A + 
    	   "</tr>";


   	   return ad;
    }
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		setFilepath(MainGlobal.getXitxx_item("采样", Locale.shipwjwz, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), this.getFilepath()));
		boolean Flag = false;
		if(null!=this.table_id&&!"".equals(table_id)){
			
			String sql="select filename from JIANJGHB where id = "+table_id;
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.getRows()>0){
				
				if(rsl.next()){

					if(null!=rsl.getString("filename")&&!"".equals(rsl.getString("filename"))){
						
						String filename=rsl.getString("filename");
						String [] name=filename.split(".avi");
						if(name.length==1){
						setFilepath(this.getFilepath()+name[0]+".avi");
						}else
						{
						setFilepath(this.getFilepath()+name[0]+".avi");
						setFilepath(this.getFilepath()+name[1]+".avi");
						}
						Flag = true;
					}
				}
			}
			
			if(!Flag){
				
				this.setMsg("没有得到视频文件名!");
			}
			
			rsl.close();
			con.Close();
			
		}else{
			
			this.setMsg("没有得到视频文件名!");
		}
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
//		if (!visit.getActivePageName().toString().equals(
//				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
//			visit.setActivePageName(getPageName().toString());
//			visit.setList1(null);
		if(cycle.getRequestContext().getParameters("lx")!=null) {
			
			table_id = cycle.getRequestContext().getParameters("lx")[0];
		}
		getSelectData();
//		}
	}
}