package com.zhiren.jt.zdt.monthreport.yuebsbzn;


import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
* ʱ�䣺2009-04-09
* ���ߣ� ll
* �޸����ݣ�  ҳ����ʾ�޸��ֶο�ȡ�
*/
public class Zhongnghdw extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
    }
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Yuebsbzn");
        }
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList(
				"select dw.id as id,dw.xuh as xuh, dw.bianm as bianm, dw.mingc as mingc, dw.quanc as quanc,dq.mingc as zhongnmkdqb_id\n" +
				"  from zhongnghdwb dw,zhongnmkdqb dq\n" + 
				"  where dw.zhongnmkdqb_id=dq.id"
				);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zhongnghdwb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(70);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("mingc").setHeader("������λ����");
		egu.getColumn("mingc").setWidth(260);
		egu.getColumn("quanc").setHeader("������λȫ��");
		egu.getColumn("quanc").setWidth(350);
		egu.getColumn("zhongnmkdqb_id").setHeader("��������");
		egu.getColumn("zhongnmkdqb_id").setWidth(200);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		
		egu.getColumn("zhongnmkdqb_id").setEditor(new ComboBox());
		egu.getColumn("zhongnmkdqb_id")
				.setComboEditor(egu.gridId,
						new IDropDownModel(	" select id,mingc from zhongnmkdqb order by xuh "));
		
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("����","function(){"+
				
				"document.getElementById('ReturnButton').click();" +
				"}").getScript()+"}");
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
			getSelectData();
		}
	}
}
