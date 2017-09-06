package com.zhiren.jt.het.hetmkgl;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikfz extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		msg = "";
	}
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private boolean _GuanlmkChick = false;

	public void GuanlmkButton(IRequestCycle cycle) {
		_GuanlmkChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Gotohetmkgl(cycle);
		}
		if (_GuanlmkChick) {
			_GuanlmkChick = false;
         	Gotoguanlmk(cycle);
		}
	}

	private void Gotoguanlmk(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ����������Ȩ��!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString16(getChange());
		cycle.activate("Meikfzmx");
	}
	
	private void Gotohetmkgl(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
		cycle.activate("Hetmkfzgl");
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList drsl = getExtGrid().getDeleteResultSet(getChange());
		String TableName="meikfzb";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql_Insert = new StringBuffer("");	//insert
		StringBuffer sql_Update = new StringBuffer("");	//Update
		while (drsl.next()) {
			//ɾ��
			sql.append("delete from " ).append(TableName).append(" where id=")
				.append(drsl.getString("ID")).append(";\n");
			sql.append("delete from meikfzmxb" ).append(" where meikfzb_id=")
			.append(drsl.getString("ID")).append(";\n");
		}
		drsl.close();
//		��ӡ��޸�
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("ID"))) {
				
				sql_Insert.append("insert into ").append(TableName)
					.append("(id, mingc,beiz")
				    .append(") values(").append("getnewid(").append(visit.getDiancxxb_id()).append(")")
					.append(",'").append(mdrsl.getString("MINGC")).append("','")
					.append(mdrsl.getString("BEIZ")).append("');\n");
				
//				��������������
//				sql_Insert.append(Zicjlcz("I",getTreeid(),TableName,sql_Insert.toString()));
				
			} else {
				sql_Update.append(" update meikfzb ").append("set mingc='").append(mdrsl.getString("MINGC"))
						.append("', beiz = '").append(mdrsl.getString("BEIZ"))
						.append("' where id=").append(mdrsl.getString("ID")).append(";\n");
				
			}
		}
		mdrsl.close();
		sql.append(sql_Insert);
		sql.append(sql_Update);
		sql.append("end;");
		if(sql.length()>13){
			
			if(con.getUpdate(sql.toString())>=0){
				
				setMsg("����ɹ���");
			}else{
				
				setMsg("����ʧ�ܣ�");
			}
		}
		con.Close();
	
	}	
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		try{
		String str = " select id,mingc,beiz from meikfzb order by mingc";
		
		ResultSetList rsl = con.getResultSetList(str);
	
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//ֻ�ܵ�ѡ����
		egu.setTableName("meikfzb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("��������");
		egu.getColumn("beiz").setHeader("��ע");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		GridButton btnreturn = new GridButton("����",
		"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
			+"  if(rec!=null){\n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('��ʾ��Ϣ','�ڹ���ú��֮ǰ���ȱ���!'); \n"
		    + "  	return;"
	        +"   }"
	        +"  }else{\n"
	        +"  Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ŀ!'); \n"
	        +"  return;"
	        +"  }"
	        +" var Cobj = document.getElementById(\"CHANGE\"); \n" 
			+" Cobj.value = rec.get('ID'); \n" 
	        +" document.getElementById('GuanlmkButton').click(); \n";
	    egu.addToolbarItem("{"+new GridButton("����ú��","function(){"+str2+"}").getScript()+"}");
	   
		setExtGrid(egu);
		
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) { 
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}
		getSelectData();
	}
	
}