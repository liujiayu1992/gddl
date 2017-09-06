package com.zhiren.dc.huaygl.huaybb.huaybhcx;


import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

import com.zhiren.dc.huaygl.Caiycl;

public class Huaybhcx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}
	
	private String huaybm = "";
	public String getHuaybm(){
		return huaybm;
	}
	public void setHuaybm(String zybm){
		huaybm = zybm;
	}

	// ��������list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// ��¼ҳ��ѡ���е�����
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


	private String Parameters;// ��¼ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {

	
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) this.getPage().getVisit();
		
		String sql;
		
	
			sql ="select z.bianm,ls.huaysj,ls.qnet_ar,ls.mt,ls.mad,ls.aad,ls.vad,ls.stad,\n" +
			"ls.had,ls.huayy,decode(ls.shenhzt,0,'δ¼�뻯��ֵ',2,'δ¼�뻯��ֵ',3,'һ�����δ���',\n" + 
			" 4,'һ�����δ���',5,'�������δ���',7,'����������','״̬����') as shenhzt\n" + 
			"from zhuanmb z,zhillsb ls\n" + 
			" where z.zhillsb_id=ls.id\n" + 
			" and z.zhuanmlb_id=(select id from zhuanmlb lb where lb.jib=3)\n" + 
			" and z.bianm='"+this.getHuaybm()+"'";
			
		ResultSetList rsl = con.getResultSetList(sql);
		if(!getHuaybm().equals("")){
			if(rsl.getRows()==0){
				this.setMsg("ϵͳ���޴˻�����,���ʵ��");
			}
		}
			
		

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("huayb");
		// /������ʾ������
		
		egu.getColumn("bianm").setHeader("�������");
	
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
		egu.getColumn("mt").setHeader("Mt");
		egu.getColumn("mt").setWidth(50);
		egu.getColumn("mad").setHeader("Mad");
		egu.getColumn("mad").setWidth(50);
		egu.getColumn("aad").setHeader("Aad");
		egu.getColumn("aad").setWidth(50);
		egu.getColumn("vad").setHeader("Vad");
		egu.getColumn("vad").setWidth(50);
		egu.getColumn("stad").setHeader("Stad");
		egu.getColumn("stad").setWidth(50);
		egu.getColumn("had").setHeader("Had");
		egu.getColumn("had").setWidth(50);
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("shenhzt").setHeader("���״̬");
		egu.getColumn("shenhzt").setWidth(140);
		

		egu.addTbarText("-");// ���÷ָ���

		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// ֻ��ѡ�е���
		
//		 ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
		
		
			egu.addTbarText("�������:");
			TextField tf=new TextField();
			tf.setWidth(80);
			tf.setValue(getHuaybm());
			
			tf.setListeners("change:function(own,n,o){document.getElementById('Huaybm').value = n}");
			egu.addToolbarItem(tf.getScript());
			egu.addTbarText("-");
		
		
//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		
		
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


	// ҳ���ж�����
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
			visit.setLong1(0);

		
		}
		getSelectData();
	}
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

}
