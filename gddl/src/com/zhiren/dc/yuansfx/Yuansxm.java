package com.zhiren.dc.yuansfx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tools.ant.taskdefs.condition.IsReference;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuansxm extends BasePage implements PageValidateListener {
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
	
//	 �ж��Ƿ��"Meikysfx"��Դ��������"Meikysfx"��Դ��ú��Ĺ���Ԫ�ط�����Ŀҳ�档
	private boolean isFromMeik; 

	public boolean isFromMeik() {
		return isFromMeik;
	}

	public void setFromMeik(boolean isFromMeik) {
		this.isFromMeik = isFromMeik;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
//	"����"��ť
	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			cycle.activate("Meikysfx");
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id,xuh,mingc,decode(zhuangt,1,'����','δ����')as zhuangt from yuansxmb order by xuh,mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("yuansxmb");
		// /������ʾ������	
		egu.getColumn("xuh").setHeader("���");	
		egu.getColumn("mingc").setHeader("����");		
		egu.getColumn("zhuangt").setHeader("����״̬");

		// //�����п��
//		egu.getColumn("leib").setWidth(70);
		// //���õ�ǰ���Ƿ�༭
//		egu.getColumn("piny").setEditor(null);
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
//		// /��̬������
//		egu.getColumn("lujxxb_id").setEditor(new ComboBox());
//		egu.getColumn("lujxxb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from lujxxb"));
//		// /��̬������
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "����"));
		l.add(new IDropDownBean(0, "δ����"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(l));
		// /�Ƿ񷵻��������ֵ��ID
		egu.getColumn("zhuangt").setReturnId(true);
		// /����������Ĭ��ֵ
		egu.getColumn("zhuangt").setDefaultValue("����");
		// /���ð�ť
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Yuansxmreport&lx=rezc';" +
   	    " window.open(url,'newWin');";
		egu.addToolbarItem("{"+new GridButton("��ӡ","function (){"+str+"}").getScript()+"}");
		
		if (isFromMeik()) {
			egu.addTbarBtn(new GridButton("����","function(){document.all.ReturnButton.click();}",SysConstant.Btn_Icon_Return));
		}
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
//			�ж�����Ƿ��"Meikysfx"��Դ�������ģ���ô��ʾ"����"��ť��������ʾ��"Meikysfx"��Դ��ú��Ĺ���Ԫ�ط�����Ŀҳ�档
			if (visit.getActivePageName().toString().equals("Meikysfx")) {
				setFromMeik(true);
			} else {
				setFromMeik(false);
			}
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}
}
