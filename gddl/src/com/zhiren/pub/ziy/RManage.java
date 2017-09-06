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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class RManage extends BasePage implements PageValidateListener  {
	private String OperateNotes;

	public String getOperateNotes() {
		return OperateNotes;
	}
	public void setOperateNotes(String operateNotes) {
		OperateNotes = operateNotes;
	}
	
	private String Jib = "1";
	public String getJib() {
		return Jib;
	}
	public void setJib(String jib) {
		Jib = jib;
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPage().getPageName())) {
			visit.setActivePageName(this.getPage().getPageName());
			visit.setList1(null);
			setJib(String.valueOf(visit.getRenyjb()));
		}
	}
//	按钮事件处理
	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    public void submit(IRequestCycle cycle) {
    	if (_RefurbishClick) {
			_RefurbishClick = false;
			getTreeData();
		}
    	if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
	}
    
    private void Save() {
    	JDBCcon con = new JDBCcon();
    	String nots[] = getOperateNotes().split(";");
		for(int i = 0 ;i <nots.length ; i++) {
			Op(con,nots[i]);
		}
		con.Close();
    }
    
    private void Op(JDBCcon con, String op) {
    	if(op == null || "".equals(op)) {
    		return;
    	}else {
    		String[] opnode = op.split(",");
    		if("I".equals(opnode[0])) {
    			Insert(con,opnode);
    		}else 
    			if("M".equals(opnode[0])) {
    				Move(con,opnode);
    			}else
    				if("U".equals(opnode[0])) {
    					Update(con,opnode);
    				}else
    					if("D".equals(opnode[0])) {
    						Delete(con,opnode);
    					}else
    						if("R".equals(opnode[0])) {
    							ModifyR(con,opnode);
    						}
    						
    	}
    }
//	添加资源
    private void Insert(JDBCcon con,String[] params) {
    	String fuid = params[2];
    	String mingc = params[3];
    	String wenjwz = params[4];
    	String sql = "";
    	//
    	if("0".equals(fuid)) {
    		//nvl((select max(id)+1 from ziyxxb where jib=1),1)
    		sql = "insert into ziyxxb(id,xuh,mingc,wenjwz,jib,fuid,leix,leib)  values(getnewid(100),"+
    		"(select nvl(max(xuh),0)+1 from ziyxxb where fuid = "+fuid+"),'"+mingc+"','"+wenjwz+"',1,0,-1,"+getJib()+")";
    	}else {
    		//(select "+fuid+"*100+nvl(max(xuh),0)+1 from ziyxxb where fuid = "+fuid+")
    		sql = "insert into ziyxxb(id,xuh,mingc,wenjwz,jib,fuid,leix,leib)  (select getnewid(100),"+
	    	"(select nvl(max(xuh),0)+1 from ziyxxb where fuid = "+fuid+"),'"+mingc+"','"+wenjwz+
	    	"',jib+1,id,leix,leib from ziyxxb where id="+fuid+")";
    	}
    	con.getInsert(sql);
    }
//  更新资源
    private void Update(JDBCcon con,String[] params) {
    	String id = params[1];
    	String mingc = params[2];
    	String sql = "update ziyxxb set mingc='"+mingc+"' where id="+id;
    	con.getUpdate(sql);
    }
//  移动资源
    private void Move(JDBCcon con,String[] params) {
    	String id = params[1];
    	String newpid = params[2];
    	String oldpid = params[3];
    	String index = params[4];
    	String sql = "";
    	if(newpid.equals(oldpid)) {
    		try {
				String xuh = MainGlobal.getTableCol("ziyxxb", "xuh", "id", id);
				int intin = Integer.parseInt(index) + 1;
				int intxuh = Integer.parseInt(xuh);
				if(intin > intxuh) {
					sql = "update ziyxxb set xuh = xuh-1 where xuh>"+intxuh +" and xuh <="+intin +" and fuid="+newpid;
					con.getUpdate(sql);
				}else 
				if(intin < intxuh){
					sql = "update ziyxxb set xuh = xuh+1 where xuh>="+intin + " and xuh <"+intxuh +" and fuid="+newpid;
					con.getUpdate(sql);
				}
				sql = "update ziyxxb set xuh = "+intin+" where id="+id;
				con.getUpdate(sql);
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
    	}else {
    		String xuh;
			try {
				xuh = MainGlobal.getTableCol("ziyxxb", "xuh", "id", id);
				sql = "update ziyxxb set xuh = xuh-1 where xuh >"+xuh +" and fuid="+oldpid;
	    		con.getUpdate(sql);
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
    		sql = "update ziyxxb set xuh = xuh+1 where xuh > "+index+" and fuid = "+newpid;
    		con.getUpdate(sql);
    		sql = "update ziyxxb set xuh = 1+"+index+",fuid = "+newpid+",jib=(select jib+1 from ziyxxb where id= "+newpid+") where id ="+id;
    		con.getUpdate(sql);
    	}
    }
//  更新资源路径
    private void ModifyR(JDBCcon con,String[] params) {
    	String id = params[1];
    	String wenjwz = params[2];
    	con.getUpdate("update ziyxxb set wenjwz='"+wenjwz+"' where id ="+id);
    }
//  删除资源
    private void Delete(JDBCcon con,String[] params){
    	DeleteChild(con,params[1]);
    }
    private void DeleteChild(JDBCcon con,String id){
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
    		//sql = "select id from ziyxxb where id="+id;
    		ResultSetList rsl = con.getResultSetList(sql);
    		while(rsl.next()){
    			DeleteChild(con,rsl.getString("id"));
    			sql = "delete ziyxxb where id="+id;
    			con.getDelete(sql);
    			sql = "delete zuqxb where ziyxxb_id="+id;
    			con.getDelete(sql);
    			sql = "delete renyqxb where ziyxxb_id="+id;
    			con.getDelete(sql);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
//    
    public String getTreeData() {
    	ExtTreeUtil etu = new ExtTreeUtil();
    	etu.setTreeDs("select zy.id,zy.mingc,zy.fuid,(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs from ziyxxb zy where zy.leib = "
    			+(getJib()==null?"1":getJib())+" order by jib,xuh", "资源");
    	return etu.getDataset();
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
