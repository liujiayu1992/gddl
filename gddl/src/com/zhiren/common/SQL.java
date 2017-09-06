package com.zhiren.common;

import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class SQL extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	MainSQL
	private String mainSql;
	public String getMainSql() {
		return mainSql;
	}
	public void setMainSql(String value) {
		mainSql = value;
	}
//	TempSQL
	private String tempSql;
	public String getTempSql() {
		return tempSql;
	}
	public void setTempSql(String value) {
		tempSql = value;
	}
	
	private boolean _SearchClick = false;
	
	public void SearchButton(IRequestCycle cycle) {
		_SearchClick = true;
	}
	
	private void Search() {
		if(getMainSql()== null || "".equals(getMainSql())) {
			setMsg("��ѯSQLΪ��");
			return ;
		}
		initGrid(getMainSql(),ExtGridUtil.Gridstyle_Read,"");
	}
	
	private boolean _ExecClick = false;

	public void ExecButton(IRequestCycle cycle) {
		_ExecClick = true;
	}
	
	private void Exec() {
		if(getMainSql()== null || "".equals(getMainSql())) {
			setMsg("ִ��SQLΪ��");
			return ;
		}
		JDBCcon con = new JDBCcon();
		if(con.getUpdate(getMainSql().replaceAll("\r", "\n"))==-1) {
			setMsg("sql�쳣");
		}else {
			setMsg("ִ�гɹ�");
		}
		con.Close();
	}
	
	private boolean _UpdateClick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateClick = true;
	}
	
	private void Update() {
		if(getMainSql()== null || "".equals(getMainSql())) {
			setMsg("��ѯSQLΪ��");
			return ;
		}
		String tableName = null;
		String[] a = getMainSql().split(" ");
		for(int i=0;i<a.length;i++) {
			if("from".equals(a[i].toLowerCase()) && (i+1 < a.length)) {
				tableName = a[i+1];
			}
		}
		if(tableName == null) {
			setMsg("update��SQL����");
		}
		initGrid(getMainSql(),ExtGridUtil.Gridstyle_Edit,tableName);
	}
	
	private boolean _CommitClick = false;

	public void CommitButton(IRequestCycle cycle) {
		_CommitClick = true;
	}
	
	private void Commit() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		getExtGrid().Save(getChange(), visit);
		Update();
	}

	public void submit(IRequestCycle cycle) {
		if (_SearchClick) {
			_SearchClick = false;
			Search();
		}
		if (_ExecClick) {
			_ExecClick = false;
			Exec();
		}
		if (_UpdateClick) {
			_UpdateClick = false;
			Update();
		}
		if (_CommitClick) {
			_CommitClick = false;
			Commit();
		}
		
	}
//	��ʼ��������
	private void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.setWidth("bodyWidth");
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('SearchButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		
		ToolbarButton ebtn = new ToolbarButton(null,"ִ��","function(){document.getElementById('ExecButton').click();}");
		ebtn.setIcon(SysConstant.Btn_Icon_SelSubmit);
		tb1.addItem(ebtn);
		
		ToolbarButton ubtn = new ToolbarButton(null,"For Update","function(){document.getElementById('UpdateButton').click();}");
		ubtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(ubtn);
		
		setToolbar(tb1);
	}
//	������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getToolbar()==null) {
			return "";
		}
		return getToolbar().getRenderScript();
	}
//	��ʼ��������
	private void initGrid(String sql,int type,String tableName) {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl == null) {
			setMsg("���ݿ�����ʧ�ܻ��ѯSQL����ȷ��");
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName(tableName);
		// ����GRID�Ƿ���Ա༭
		egu.setGridType(type);
		egu.setWidth(Locale.Grid_DefaultWidth);
		int paging = 14;
		
		if(ExtGridUtil.Gridstyle_Edit == type) {
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Copy, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "CommitButton");
			paging = 13;
		}
		egu.addPaging(paging);
		setExtGrid(egu);
		con.Close();
		
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			initToolbar();
			initGrid("select sysdate from dual",ExtGridUtil.Gridstyle_Read,"");
		}
	}
	
	public void pageValidate(PageEvent arg0) {
		/*String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}*/
	}
	
}
