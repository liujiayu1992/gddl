package com.zhiren.dc.jilgl.gongl.qith;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-15 16��44
 * �������޸�GRID��ͷdanwdz����λ��ַ�� δ��ʾ���ֵ�BUG 
 */
public class Qitgysb extends BasePage implements PageValidateListener {
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id, xuh, mingc, quanc,piny,bianm," +
						"danwdz,faddbr,weitdlr,kaihyh,zhangh,dianh,shuih," +
						"youzbm,chuanz ,beiz  from qitgysb  ");
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qitgysb");
		egu.setWidth("bodyWidth");
		//egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("mingc").setHeader("���");
	
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("piny").setHeader("��Ӧ��ƴ��");
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("danwdz").setHeader("��λ��ַ");
		egu.getColumn("faddbr").setHeader("����������");
		egu.getColumn("weitdlr").setHeader("ί�д�����");
		egu.getColumn("kaihyh").setHeader("��������");
		egu.getColumn("zhangh").setHeader("�˺�");
		egu.getColumn("dianh").setHeader("��ϵ�绰");
		egu.getColumn("shuih").setHeader("˰��");
		egu.getColumn("youzbm").setHeader("��������");
		egu.getColumn("chuanz").setHeader("�����");
		egu.getColumn("beiz").setHeader("��ע");
//		egu.getColumn("mingc").setHidden(true);

		
		
		egu.addPaging(25);
	
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);   //ɾ����ť
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//��ӡ
		/*String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Chezreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + str + "}").getScript()
				+ "}");*/  
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
