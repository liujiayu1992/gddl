package com.zhiren.dc.jilgl.jicxx;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiecfs extends BasePage implements PageValidateListener {
	private String msg="";
   public String getMsg(){
	   return msg;
   }
   public void setMsg(String msg){
	   this.msg=msg;
   }
   //ҳ��仯��¼
   private String change;
   public String getChange(){
	   return change;
   }
   public void setChange(String change){
	   this.change=change;
   }
   public void save(){
	   
	   Visit visit=(Visit)this.getPage().getVisit();
//����ǰ��֤�Ƿ��ظ�
	   ResultSetList rsl=visit.getExtGrid1().getModifyResultSet(getChange());
	   //��֤�Ķ������Ƿ����ظ�,����б������һ��
	   int col=rsl.findCol("mingc".toUpperCase());
	   int rowcount=rsl.getResultSetlist().size();
	  
		   for(int j=0;j<rowcount;j++){
			   int k=0;
			   for(int i=0;i<rowcount;i++){
				  if(rsl.getString(j,col).equals(rsl.getString(i,col))){
					   k++;
				  }
			    }
			   if(k>1){
				 //  rsl.Remove(j);
				   setMsg(MainGlobal.getExtMessageBox("���������ظ����������������Ҫ�������",false));return;
			   }
		    }
	 //��֤�Ķ����������ݿ����Ƿ����ظ�
	   while(rsl.next()){
		   JDBCcon con=new JDBCcon();
		   String sql="";
		   if(rsl.getInt("id")==0){
			   sql="select count(*) cou from xiecfsb where mingc='"+rsl.getString("mingc")+"'";
		   }else{
			   sql="select count(*) cou from xiecfsb where id<>"+rsl.getString("id")+" and mingc='"+rsl.getString("mingc")+"'";
		   }
		   ResultSet rs= con.getResultSet(sql);
			  try {
				if(rs.next()&&rs.getInt("cou")>0 ){
				       setMsg(MainGlobal.getExtMessageBox("���䷽ʽ("+rsl.getString("mingc")+")�Ѿ����ڣ�������ӵ���Ŀδ����",false));
				       rsl.Remove(rsl.getRow());
				       con.Close();   
				       return;
				  }
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			con.Close();
	   }
//��֤ͨ������ʼд�����ݿ�

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin\n");
        String tableName="xiecfsb";
        rsl.beforefirst();
		while (rsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(rsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					sql.append(",").append(rsl.getColumnNames()[i]);
				}
				sql2.append(",").append(rsl.getInt("DIANCXXB_ID")).append(",'")
				 .append(rsl.getString("mingc")).append("','").append(rsl.getString("beiz")).append("'");
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				sql.append("diancxxb_id=").append(rsl.getInt("DIANCXXB_ID")).append(",mingc='")
				    .append(rsl.getString("mingc")).append("',beiz='").append(rsl.getString("beiz")).append("'");
				sql.append(" where id =").append(rsl.getInt("ID")).append(
						";\n");
			}
		}
		rsl.close();
		sql.append("end;\n");
		if(con.getUpdate(sql.toString())!=-1){ setMsg(MainGlobal.getExtMessageBox("�¸��ĵ����䷽ʽ����ɹ�",false));}else{
			 setMsg(MainGlobal.getExtMessageBox("������ִ������뿪����Ա��ϵ",false));
		};
		con.Close();
		
	
   }
   
   private boolean _saveClick=false;
   public void SaveButton(IRequestCycle cycle){
	   this._saveClick=true;
   }
   
   public void submit(IRequestCycle cycle){
	   if(_saveClick){
		   _saveClick=false;
		   save();
		   getSelectData();
	   }
   }
	public void getSelectData(){
		JDBCcon con=new JDBCcon();
		Visit v=(Visit)this.getPage().getVisit();
		
 		ResultSetList rsl=con.getResultSetList("select id,diancxxb_id,mingc,beiz from xiecfsb where "+Jilcz.filterDcid(v,null).substring(4)+" order by id");
	    ExtGridUtil egu=new ExtGridUtil("gridDiv",rsl);	
	    egu.setTableName("xiecfsb");
	    egu.getColumn("mingc").setHeader("ж����ʽ����(�����ظ�)");
	    egu.getColumn("mingc").setWidth(160);
	    egu.getColumn("mingc").setUnique(true);
	    egu.getColumn("diancxxb_id").setHidden(true);
	    egu.getColumn("diancxxb_id").setDefaultValue(""+v.getDiancxxb_id());
	    egu.getColumn("beiz").setHeader("��ע");
		  egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		  egu.addPaging(20);
		  egu.addToolbarButton(GridButton.ButtonType_Insert,null);
		  egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		  setExtGrid(egu);
		  con.Close();
	}
	public void setExtGrid(ExtGridUtil egu) {
		// TODO �Զ����ɷ������
		((Visit)this.getPage().getVisit()).setExtGrid1(egu);
	}
	public ExtGridUtil getExtGrid(){
		return ((Visit)this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
	  Visit visit=(Visit)getPage().getVisit();
	  if(!visit.getActivePageName().toString().equals(this.getPageName().toString())){
		  visit.setActivePageName(getPageName().toString());
		  visit.setList1(null);
		  getSelectData();
		  this.setMsg("");
	  }
    }

	public void pageValidate(PageEvent arg0) {
		// TODO �Զ����ɷ������
		String PageName=arg0.getPage().getPageName();
		String ValPageName=Login.ValidateLogin(arg0.getPage());
		if(!PageName.equals(ValPageName)){
			IPage ipage=arg0.getRequestCycle().getPage(ValPageName);
			throw new PageRedirectException(ipage);
		}
	}
}
