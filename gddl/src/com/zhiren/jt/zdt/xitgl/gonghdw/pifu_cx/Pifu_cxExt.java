package com.zhiren.jt.zdt.xitgl.gonghdw.pifu_cx;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pifu_cxExt extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
		Visit visit1 = (Visit) this.getPage().getVisit();
		visit1.getExtGrid1().Save(getChange(), visit1);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit1 = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String tiaoj="";
	    tiaoj=visit1.getString1();
		
		String Sql="select g.id, g.xuh,g.bianm,g.quanc,decode(1,1,'�鿴') as chak from gongysb g where g.quanc like  '%"+tiaoj+"%'";
		ResultSetList rsl = con.getResultSetList(Sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("gongysb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setCenterHeader("���");
		egu.getColumn("bianm").setCenterHeader("����");
		egu.getColumn("quanc").setCenterHeader("ȫ��");
		egu.getColumn("chak").setCenterHeader("�굥");
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(100);
		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("quanc").setWidth(230);
		egu.getColumn("chak").setWidth(80);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
		String str=
   		" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
		"url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'gonghdw_db&id='+record.data['ID'];";
		
		egu.getColumn("chak").setRenderer(
				"function(value,p,record){" +str+
				"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}"
		);
		String strs="var bianmzhi=gridDiv_sm.getSelected().get('BIANM');"+
					"var myObject = new Object();"+
					"myObject.bianh =bianmzhi;"+
					"window.returnValue=myObject;"+
					"window.close();";
		egu.addTbarText("-");
		egu.addToolbarItem("{"+new GridButton("������ѡ���еı���","function(){"+strs+"}").getScript()+"}");
		egu.addTbarText("-");

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		
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
		Visit visit1 = (Visit) getPage().getVisit();
		visit1.setString1("");
		if (cycle.getRequestContext().getParameter("tiaoj") != null) {
			visit1.setString1(String.valueOf(cycle.getRequestContext().getParameter("tiaoj")));
			
		}
		if (!visit1.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit1.setActivePageName(getPageName().toString());
			visit1.setList1(null);
		
		}
		
		getSelectData();
	}
}
