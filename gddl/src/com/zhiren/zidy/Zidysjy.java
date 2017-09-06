package com.zhiren.zidy;

import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2011-06-14
 * ���������淽���У��õ�ID�ķ�ʽ����Int�ͱ������Ȳ��������ת��Ϊlong��
 */

public class Zidysjy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
//	��������list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
    
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	private String Parameters;//��¼ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		getExtGrid().Save(getChange(), visit);
		JDBCcon con = new JDBCcon();
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while(delrsl.next()){
			String sql = "delete from zidysjysql where zidysjy_id =" + delrsl.getString(0);
			con.getDelete(sql);
		}
		delrsl.close();
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShedsqlChick){
			_ShedsqlChick = false;
			Update(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,z_name,z_remark from zidysjy order by decode(z_remark,'��������Դ',0,1),z_name");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidysjy");
		egu.getColumn("z_name").setHeader("����Դ����");
		egu.getColumn("z_name").setWidth(300);
		egu.getColumn("z_remark").setHeader("��ע");
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//ֻ�ܵ�ѡ����

		egu.addPaging(25);

		egu.addToolbarButton(GridButton.ButtonType_Refresh, null);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gb = new GridButton("���SQL","function(){ " +
				" var rec = gridDiv_sm.getSelected(); " +
				" if(rec != null){var id = rec.get('ID'); var title = '����Դ����' + '  :  ' + rec.get('Z_NAME'); " +
				" var Cobj = document.getElementById('CHANGE'); var Cobjid = document.getElementById('CHANGEID');" +
				" Cobj.value = title; Cobjid.value = id;" +
				" document.getElementById('ShedsqlButton').click();}}");
		egu.addTbarBtn(gb);
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
//	ҳ���ж�����
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

	private void Update(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ����Դ����SQL!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
//		����Int�ͱ������Ȳ��������ת��Ϊlong��
		visit.setLong1(Long.parseLong(getChangeid()));
		visit.setString1(getChange());
		cycle.activate("Zidysjysql");
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setLong1(0);
			visit.setString1(null);
			visit.setLong9(0);
			visit.setString9(null);
			getSelectData();
		}
		
	}
}

