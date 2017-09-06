package com.zhiren.dc.gdxw.fenycx;

/**
 * @author ���ܱ�
 * @version 2009-09-23
 */


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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

import com.zhiren.dc.huaygl.Caiycl;

public class Fenycx extends BasePage implements PageValidateListener {
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
	
	private String caiybm = "";
	public String getCaiybm(){
		return caiybm;
	}
	public void setCaiybm(String cybm){
		caiybm = cybm;
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

	boolean riqichange1 = false;

	boolean riqichange2 = false;

	private String riqi1; // ҳ����ʼ��������ѡ��

	private String riqi2; // ҳ���������ѡ��

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riqi1;
	}

	public void setRiqi1(String riqi1) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi1)) {
			this.riqi1 = riqi1;
			riqichange1 = true;
		}
	}

	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi1 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
			riqichange2 = true;
		}
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
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ�����ݽ��д�ӡ��");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String caiyid="";
		while(rsl.next()) {
			caiyid+=""+rsl.getString("id")+",";
			
			//this.setDiancxxb_id(rsl.getString("diancxxb_id"));
		}
		String aa=caiyid.substring(0,caiyid.length()-1);
		((Visit)this.getPage().getVisit()).setString1(aa);
		
		cycle.activate("Fenycx_dy");
	}
	
	public void submit(IRequestCycle cycle) {

		
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) this.getPage().getVisit();
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql;
		
		
	
		if("".equals(getCaiybm())){
			sql=
			"select cp.id,cp.piaojh,decode(wz.mingc,null,decode(cp.isjus,1,'����','����жú'),wz.mingc) as mingc\n" +
			"from chepbtmp cp,caiyb cy,cunywzb wz\n" + 
			"where cp.zhilb_id=cy.zhilb_id(+)\n" + 
			"and cy.cunywzb_id=wz.id(+)\n" + 
			"and cp.lursj>="+caiyrqb+"\n"+
			"and cp.lursj<"+caiyrqe+" +1\n"+
			"order by wz.mingc,cp.piaojh";
		}else{
			sql="select cp.id,cp.piaojh,decode(wz.mingc,null,decode(cp.isjus,1,'����','����жú'),wz.mingc) as mingc\n" +
			"from chepbtmp cp,caiyb cy,cunywzb wz\n" + 
			"where cp.zhilb_id=cy.zhilb_id(+)\n" + 
			"and cy.cunywzb_id=wz.id(+)\n" + 
			"and cp.lursj>="+caiyrqb+"\n"+
			"and cp.lursj<"+caiyrqe+" +1\n"+
			" and cp.piaojh='"+this.getCaiybm()+"'\n"+
			"order by wz.mingc,cp.piaojh";
		}
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caiyb");
		// /������ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("piaojh").setHeader("������");
		egu.getColumn("mingc").setHeader("Ͱ��");
		
		// ����
		egu.addTbarText("��ú����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setValue(this.getRiqi2());
		df1.setReadOnly(true);
		df1.Binding("RIQI2", "forms[0]");
		df1.setId("riqi2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(0);
		//egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// ֻ��ѡ�е���
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		

	
			egu.addTbarText("��������:");
			TextField tf=new TextField();
			tf.setWidth(80);
			tf.setValue(getCaiybm());
			//tf.setId("Zhiybm");
			tf.setListeners("change:function(own,n,o){document.getElementById('Caiybm').value = n}");
			egu.addToolbarItem(tf.getScript());
			egu.addTbarText("-");
		
		
//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
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
		if (!visit.getActivePageName().toString().equals("Fenycx_dy")) {
			visit.setList1(null);
			riqi1 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			riqi2 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setLong1(0);
		}
		visit.setActivePageName(getPageName().toString());

		}
		
		getSelectData();
	}
	

}
