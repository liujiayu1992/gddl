package com.zhiren.dc.caiygl.meicy;

/**
 * @author ���ܱ�
 * @version 2010-5-11 
 * ����:ú�������
 * 
 */


import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meicyshej extends BasePage implements PageValidateListener {
	private String CustomSetKey = "Meicysh";
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
	
//	private boolean xiansztq = true;

//	private boolean xiansztl = true;

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
	
	
	//���
	public void Save() {
		String tableName = "meicyb";
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
//		ú�������,�����״̬��3�ĵ�4
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = 5,shenhryej='"+visit.getRenymc()+"' where id =");
			sql.append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		this.setMsg("��˳ɹ�!");
		con.Close();
	}
	
	//����
	public void Huit() {
		String tableName = "meicyb";
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = 0 ,shenhryej='', shenhry='' ");
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		this.setMsg("���˳ɹ�!");
		con.Close();
	}
	
	
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
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
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelectData();
		}
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();
//		Visit visit = (Visit) this.getPage().getVisit();
//		ResultSetList rs = new ResultSetList();
		
		String sql;
		sql="select k.id, k.huaybh,k.huaysj,formatxiaosws(k.mt,1) as mt,\n" +
			"formatxiaosws(k.mad,2) as mad,formatxiaosws(k.aad,2) as aad,\n" + 
			"formatxiaosws(k.ad,2)  as ad, formatxiaosws(k.vad,2) as vad,\n" + 
			"formatxiaosws(k.vdaf,2) as vdaf,formatxiaosws(k.fcad,2) as fcad,\n" + 
			"formatxiaosws(k.stad,2) as stad,formatxiaosws(k.std,2) as std,\n" + 
			"formatxiaosws(k.had,2) as had,formatxiaosws(round_new(k.qbad,2),2)  as qbad,\n" + 
			"formatxiaosws(round_new(k.qgrd,2),2) as qgrd,formatxiaosws(k.qnet_ar,2) as qnet_ar,\n" + 
			"round_new(k.qnet_ar*1000/4.1816,0) as qnet_ar_daka,\n" + 
			"k.huayy,k.zhiyry\n" + 
			"from meicyb k\n" + 
			"where k.shenhzt=4\n" + 
			"order by k.huaybh";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		egu.setTableName("meicyb");
//		 ����ҳ�����Ա����ӹ�����
//		egu.setWidth(1000);
		egu.setWidth("bodyWidth");
		// /������ʾ������

		 egu.getColumn("id").setHidden(true);
		 egu.getColumn("huaybh").setHeader("���");
		 egu.getColumn("huaysj").setHeader("��������");
		 egu.getColumn("mt").setHeader("Mt(%)");
		 egu.getColumn("mad").setHeader("Mad(%)");
		 egu.getColumn("aad").setHeader("Aad(%)");
		 egu.getColumn("ad").setHeader("Ad(%)");
		 egu.getColumn("vad").setHeader("Vad(%)");
		 egu.getColumn("vdaf").setHeader("Vdaf(%)");
		 egu.getColumn("fcad").setHeader("FCad(%)");
		 egu.getColumn("stad").setHeader("St,ad(%)");
		 egu.getColumn("std").setHeader("St,d(%)");
		 egu.getColumn("had").setHeader("Had(%)");
		 egu.getColumn("qbad").setHeader("Qb,ad(Mj/kg)");
		 egu.getColumn("qgrd").setHeader("Qgrd(%)");
		 egu.getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
		 egu.getColumn("qnet_ar_daka").setHeader("Qnet,ar(K/g)");
		 egu.getColumn("huayy").setHeader("����Ա");
		 egu.getColumn("zhiyry").setHeader("����Ա");
		
		 
		//���
		 egu.getColumn("huaybh").setWidth(80);
		 egu.getColumn("huaysj").setWidth(80);
		 egu.getColumn("mt").setWidth(60);
		 egu.getColumn("mad").setWidth(60);
		 egu.getColumn("aad").setWidth(60);
	     egu.getColumn("ad").setWidth(60);
		 egu.getColumn("vad").setWidth(60);
		 egu.getColumn("vdaf").setWidth(60);
		 egu.getColumn("fcad").setWidth(60);
		 egu.getColumn("stad").setWidth(60);
		 egu.getColumn("std").setWidth(60);
		 egu.getColumn("had").setWidth(60);
		 egu.getColumn("qbad").setWidth(60);
		 egu.getColumn("qgrd").setWidth(60);
		 egu.getColumn("qnet_ar").setWidth(80);
		 egu.getColumn("qnet_ar_daka").setWidth(80);
		 egu.getColumn("huayy").setWidth(60);
		 egu.getColumn("zhiyry").setWidth(60);

		egu.addTbarText("-");// ���÷ָ���

		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(0);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton("���",GridButton.ButtonType_Sel, "SaveButton", null, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarText("-");
		egu.addToolbarButton("����",GridButton.ButtonType_Sel, "HuitButton", null, SysConstant.Btn_Icon_Return);
		egu.addTbarText("-");
//		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
//		egu.addTbarText("-");

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
		
			visit.setLong1(0);
		}
		visit.setActivePageName(getPageName().toString());

		}
		
		getSelectData();
	}
	

}
