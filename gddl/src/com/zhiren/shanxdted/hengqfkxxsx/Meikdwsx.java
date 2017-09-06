package com.zhiren.shanxdted.hengqfkxxsx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikdwsx extends BasePage implements PageValidateListener {
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
	
	private String theKey;
	private boolean Key=false;
    public String gettheKey() {
    	return theKey;
    }
    public void settheKey(String theKey) {
    	this.theKey = theKey;
    }

	private void Save() {
		StringBuffer sql = new StringBuffer();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			sql.append("update meikxxb set HENGQSXZT = '"+
					(getExtGrid().getColumn("hengqsxzt").combo)
					.getBeanId(rsl.getString("hengqsxzt"))+"' where id ="+rsl.getString("id") + ";\n");
			
		}
		int flag = 0;
		if(sql.length()!=0){
			flag = con.getUpdate("begin\n" + sql.toString() + "end;\n");
			if(flag==-1){
				setMsg("����ʧ��"); 
			}else{
				setMsg("���óɹ�");
			}
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ChazChick = false;
    public void ChazButton(IRequestCycle cycle) {
    	_ChazChick = true;
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_ChazChick) {
    		_ChazChick = false;
    		Key=true;
        }
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql = "";
		if(Key){
    		sql = "select id,mingc,quanc,decode(hengqsxzt,0,'_',1,'Ĭ��ʹ��',2,'����ʹ��') hengqsxzt from meikxxb  \n" +
		     	  " where (mingc like '%" + gettheKey() + "%' or quanc like '%" + gettheKey() + "%')" ;
    		Key=false;
    	}else{
    		sql = "select id,mingc,quanc,decode(hengqsxzt,0,'_',1,'Ĭ��ʹ��',2,'����ʹ��') hengqsxzt from meikxxb  \n" ;
    	}
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meikxxb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("mingc").setHeader("���");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").setWidth(200);
		egu.getColumn("mingc").setUpdate(false);
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setEditor(null);
		egu.getColumn("quanc").setWidth(300);
		egu.getColumn("quanc").setUpdate(false);
		
		
		egu.getColumn("hengqsxzt").setHeader("ʹ�����");
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "_"));
		l.add(new IDropDownBean(1, "Ĭ��ʹ��"));
		l.add(new IDropDownBean(2, "����ʹ��"));
		egu.getColumn("hengqsxzt").setEditor(new ComboBox());
		egu.getColumn("hengqsxzt").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("hengqsxzt").setReturnId(false);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		egu.addTbarText("-");
		egu.addTbarText("�������ƣ�");
		TextField theKey = new TextField();
		theKey.setWidth(80);
		theKey.setId("theKey_text");
		theKey.setListeners("specialkey:function(thi,e){if (e.getKey()==13){var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();document.getElementById('ChazButton').click();}}\n");
		egu.addToolbarItem(theKey.getScript());
		
		String Chaz = "function(){var objkey = document.getElementById('theKey');objkey.value=theKey_text.getValue();document.getElementById('ChazButton').click();}";
		GridButton chaz = new GridButton("ģ������", Chaz, SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(chaz);
		
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
		}
		getSelectData();
	}
}
