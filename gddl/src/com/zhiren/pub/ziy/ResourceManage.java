package com.zhiren.pub.ziy;

import java.sql.ResultSet;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.JsTreeUtil;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class ResourceManage extends BasePage implements PageValidateListener  {
	private long ID;
	private long SerialNumber;
	private String ResourceName;
	private String ResourcePlace;
	private String OperateNotes;
	private boolean OperateType = false;
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getOperateNotes() {
		return OperateNotes;
	}
	public void setOperateNotes(String operateNotes) {
		OperateNotes = operateNotes;
	}
	public String getResourceName() {
		return ResourceName;
	}
	public void setResourceName(String resourceName) {
		ResourceName = resourceName;
	}
	public String getResourcePlace() {
		return ResourcePlace;
	}
	public void setResourcePlace(String resourcePlace) {
		ResourcePlace = resourcePlace;
	}
	public long getSerialNumber() {
		return SerialNumber;
	}
	public void setSerialNumber(long serialNumber) {
		SerialNumber = serialNumber;
	}
	public boolean isOperateType() {
		return OperateType;
	}
	public void setOperateType(boolean operateType) {
		OperateType = operateType;
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPage().getPageName())) {
			visit.setActivePageName(this.getPage().getPageName());
			visit.setList1(null);
		}
	}
//	按钮事件处理
	private boolean _InsertChick = false;

    public void InsertButton(IRequestCycle cycle) {
        _InsertChick = true;
    }
    
    private boolean _UpdateChick = false;

    public void UpdateButton(IRequestCycle cycle) {
        _UpdateChick = true;
    }
    
    private boolean _DeleteChick = false;

    public void DeleteButton(IRequestCycle cycle) {
        _DeleteChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
		if (_InsertChick) {
            _InsertChick = false;
            Insert();
        }
		if (_UpdateChick) {
			_UpdateChick = false;
			Update();
        }
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
        }
	}
//    插入一个资源
    private void Insert(){
    	JDBCcon con = new JDBCcon();
    	String sql;
		if(getID()==0){
    		sql = "insert into ziyxxb(id,xuh,mingc,wenjwz,jib,fuid,leix,leib)  values(nvl((select max(id)+1 from ziyxxb where jib=1),1),"+
    		getSerialNumber()+",'"+getResourceName()+"','"+getResourcePlace()+"',1,0,-1,"+MainGlobal.getSystemLeib()+")";
    	}else{
	    	sql = "insert into ziyxxb (select nvl((select max(id)+1 from ziyxxb where fuid = "+getID()+"),"+getID()+"*100+1),"+
	    	getSerialNumber()+",'"+getResourceName()+"','"+getResourcePlace()+
	    	"',jib+1,id,leix,leib from ziyxxb where id="+getID()+")";
    	}
    	con.getInsert(sql);
    	con.Close();
    	setOperateType(false);
    }
//	更新资源
    private void Update(){
    	JDBCcon con = new JDBCcon();
    	String sql = "update ziyxxb set xuh="+getSerialNumber()+",mingc='"+getResourceName()
    	+"',wenjwz='"+getResourcePlace()+"' where id="+getID();
    	con.getUpdate(sql);
    	con.Close();
    	setOperateType(false);
    }
//  删除资源
    private void Delete(){
    	JDBCcon con = new JDBCcon();
    	DeleteChild(con,getID());
    	con.Close();
    	setOperateType(true);
    }
    private void DeleteChild(JDBCcon con,long id){
    	String sql = "select id from ziyxxb where fuid="+id;
    	try{
    		if(!con.getHasIt(sql)){
    			sql = "delete ziyxxb where id="+id;
    			con.getDelete(sql);
    			sql = "delete zuqxb where ziyxxb_id="+id;
    			con.getDelete(sql);
    			sql = "delete renyqxb where ziyxxb_id="+id;
    			con.getDelete(sql);
    			return;
    		}
    		sql = "select id from ziyxxb where id="+id;
    		ResultSet rs = con.getResultSet(sql);
    		while(rs.next()){
    			DeleteChild(con,rs.getLong("id"));
    			sql = "delete ziyxxb where id="+id;
    			con.getDelete(sql);
    			sql = "delete zuqxb where ziyxxb_id="+id;
    			con.getDelete(sql);
    			sql = "delete renyqxb where ziyxxb_id="+id;
    			con.getDelete(sql);
    		}
    		rs.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
//  得到树的array
    public String getArrayScript() {
		JDBCcon con = new JDBCcon();
		JsTreeUtil jtree = new JsTreeUtil(JsTreeUtil.TreeType_ImgTree);
    	jtree.CreateRoot("燃料管理资源信息");
    	for(int i = 1; i<5 ;i++) {
        	ResultSetList rs = con.getResultSetList(
        			"select fuid,id,mingc,xuh,wenjwz from ziyxxb where jib = "
        			+i+" order by xuh");
        	jtree.addNode(rs);
    	}
    	con.Close();
        return jtree.getTree();
//        StringBuffer array = new StringBuffer();
//        StringBuffer sbSql;
//        long fuid = 0;
//        long id = 0;
//        String mingc = "";
//        long xuh = 0;
//        String wenjwz = "";
//        try {
//            for(int i = 1; i<5 ;i++){
//            	sbSql = new StringBuffer();
//            	sbSql.append("select fuid,id,mingc,xuh,wenjwz from ziyxxb where jib = "+i+" order by xuh");
//            	ResultSetList rs = con.getResultSetList(sbSql.toString());       	
//            	array.append("TreeNode"+i+" = new Array();\n");
//            	int j=0;
//            	while(rs.next()){
//            		fuid = Long.parseLong(rs.getString("fuid"));
//            		id = Long.parseLong(rs.getString("id"));
//            		mingc = rs.getString("mingc");
//            		if(mingc == null) {
//            			mingc = "";
//            		}
//            		xuh = Long.parseLong(rs.getString("xuh"));
//            		wenjwz = rs.getString("wenjwz");
//            		if(wenjwz == null) {
//            			wenjwz = "";
//            		}
//            		array.append("TreeNode"+i+"["+j+++"] = new Array(\""+fuid+"\",");
//            		array.append(id+",\"");
//            		array.append(mingc.replaceAll("\n", "").replaceAll("\r", "")+"\",");
//            		array.append(xuh+",\"");
//            		array.append(wenjwz.replaceAll("\n", "").replaceAll("\r", "")+"\");\n");
//            	}
//            	array.append("Tree["+i+"]= TreeNode"+i+";\n");
//            }
//            array.append("");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        con.Close();
//        return array.toString();
	}
//  得到操作列表
    public String getOprateScript(){
    	String script =	JsTreeUtil.
    		getOprateScript(getOperateNotes(),isOperateType(),getID());
    	setOperateNotes(null);
    	return script;
//    	if(getOperateNotes() == null || "".equals(getOperateNotes())){
//			return "";
//		}
//		String arrNote[] = getOperateNotes().split(";");
//		StringBuffer array = new StringBuffer();
//		for(int i=0; i < arrNote.length ; i++){
//			if("".equals(arrNote[i])){
//				continue;
//			}
//			if(isOperateType()){
//				if(arrNote[i].indexOf(""+getID())!=-1){
//					continue;
//				}
//			}
//			String str[] = arrNote[i].split(",");
//			if(str[0].equals("O")){
//				array.append("OpenOrClose(document.getElementById(\"");
//				array.append(str[1]);
//				array.append("\"));");
//			}else{
//				array.append("ClickNode(document.getElementById(\"");
//				array.append(str[1]);
//				array.append("\").nextSibling);");
//			}
//		}
//		setOperateNotes(null);
//		return array.toString();
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
