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

	// 保存数据list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// 记录页面选中行的内容
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


	private String Parameters;// 记录ID

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
			"ls.had,ls.huayy,decode(ls.shenhzt,0,'未录入化验值',2,'未录入化验值',3,'一级审核未审核',\n" + 
			" 4,'一级审核未审核',5,'二级审核未审核',7,'二级审核完毕','状态错误') as shenhzt\n" + 
			"from zhuanmb z,zhillsb ls\n" + 
			" where z.zhillsb_id=ls.id\n" + 
			" and z.zhuanmlb_id=(select id from zhuanmlb lb where lb.jib=3)\n" + 
			" and z.bianm='"+this.getHuaybm()+"'";
			
		ResultSetList rsl = con.getResultSetList(sql);
		if(!getHuaybm().equals("")){
			if(rsl.getRows()==0){
				this.setMsg("系统中无此化验编号,请核实！");
			}
		}
			
		

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("huayb");
		// /设置显示列名称
		
		egu.getColumn("bianm").setHeader("化验编码");
	
		egu.getColumn("huaysj").setHeader("化验时间");
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
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("shenhzt").setHeader("审核状态");
		egu.getColumn("shenhzt").setWidth(140);
		

		egu.addTbarText("-");// 设置分隔符

		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// 只能选中单行
		
//		 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		
		
			egu.addTbarText("化验编码:");
			TextField tf=new TextField();
			tf.setWidth(80);
			tf.setValue(getHuaybm());
			
			tf.setListeners("change:function(own,n,o){document.getElementById('Huaybm').value = n}");
			egu.addToolbarItem(tf.getScript());
			egu.addTbarText("-");
		
		
//		 刷新按钮
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


	// 页面判定方法
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
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
