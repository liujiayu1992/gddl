package com.zhiren.dc.huaygl.huaybl;
/*
 * ���ߣ�����ΰ
 * ʱ�䣺2017-06-22
 * ���������������ά��ҳ��
 */

import com.zhiren.common.*;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.MonthField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import java.text.SimpleDateFormat;
import java.util.Date;


public class choujylr extends BasePage implements PageValidateListener{
//	�����û���ʾ
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
//	ˢ���������ڰ�
	private String riq= (new SimpleDateFormat("yyyy-MM")).format(new Date());
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
//	ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}else if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
	public void Save() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long lngId = 0;
		int flag = 0;
		if(getChange( )==null || "".equals(getChange())) {
			setTbmsg("û�������κθ��ģ�");
			return;
		}
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			if (rsl.getString("id")==null||rsl.getString("id").equals("0")) {
				lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				String sInsert ="insert into zhilb_cj(huaysj,id,HUAYBH,mad,aad,ad,vad,vdaf,stad,std,qbad,qgrad,beiz) values" +
						"(date'" + rsl.getString("huaysj") + "'," +
						"getnewid(" + visit.getDiancxxb_id() + ")" + ",'" +
						rsl.getString("HUAYBh") + "'," +
						rsl.getDouble("mad") + "," +
						rsl.getDouble("aad") + "," +
						rsl.getDouble("ad") + "," +
						rsl.getDouble("vad") + "," +
						rsl.getDouble("vdaf") + "," +
						rsl.getDouble("stad") + "," +
						rsl.getDouble("std") + "," +
						rsl.getDouble("qbad") + "," +
						rsl.getDouble("qgrad") + ",'" +
						rsl.getString("beiz") + "'" +
						")";
				flag = con.getInsert(sInsert);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sInsert);
					setMsg("������̳��ִ��󣡱���δ�ɹ���");
					con.rollBack();
					con.Close();
					return;
				}
			}else {
				String sUpdate = "update zhilb_cj set huaysj= date'"+rsl.getString("huaysj")+"'," + "" +
						"huaybh='"+rsl.getString("huaybh")+"'," +
						"mad="+rsl.getDouble("mad")+","+
						"aad="+rsl.getDouble("aad")+","+
						"ad="+rsl.getDouble("ad")+","+
						"vad="+rsl.getDouble("vad")+","+
						"vdaf="+rsl.getDouble("vdaf")+","+
						"stad="+rsl.getDouble("stad")+","+
						"std="+rsl.getDouble("std")+","+
						"qbad="+rsl.getDouble("qbad")+","+
						"qgrad="+rsl.getDouble("qgrad")+","+
						"beiz='"+rsl.getString("beiz")+"'"+
						" where id= "+rsl.getString("id");
				flag = con.getUpdate(sUpdate);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sUpdate);
					setMsg("������̳��ִ��󣡱���δ�ɹ���");
					con.rollBack();
					con.Close();
					return;
				}
			}
		}
		rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsl.next()) {
			String sDelete = "delete zhilb_cj where id=" + rsl.getString("id");
			flag = con.getDelete(sDelete);
			if (flag == -1){
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + sDelete);
				setMsg("������̳��ִ��󣡱���δ�ɹ���");
				con.rollBack();
				con.Close();
				return;
			}
		}
		con.commit();
		con.Close();
		setTbmsg("����ɹ�");
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sSql = 
			"select id,huaysj,HUAYBH,mad,aad,ad,vad,vdaf,stad,std,qbad,qgrad,beiz from zhilb_cj \n" +
					"where to_char(huaysj,'yyyy-mm')='"+this.getRiq()+"'";

		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		//egu.setTableName("fahhtb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaybh").setHeader("����");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("aad").setHeader("Aad%��");
		egu.getColumn("ad").setHeader("Ad(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("vdaf").setHeader("Vdaf(%)");
		egu.getColumn("stad").setHeader("St,ad(%)");
		egu.getColumn("std").setHeader("St,d(%)");
		egu.getColumn("qbad").setHeader("Qb,ad(MJ/kg)");
		egu.getColumn("qgrad").setHeader("Qgr,v,d(MJ/kg)");
		egu.getColumn("beiz").setHeader("��ע");

//		egu.addTbarText("���");
//		ComboBox comb1 = new ComboBox();
//		comb1.setWidth(60);
//		comb1.setTransform("NianfDropDown");
//		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
//		comb1.setLazyRender(true);// ��̬��
//		comb1.setEditable(true);
//		egu.addToolbarItem(comb1.getScript());
//		// �����·�������
//		egu.addTbarText("�·�");
//		ComboBox comb2 = new ComboBox();
//		comb2.setWidth(50);
//		comb2.setTransform("YuefDropDown");
//		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
//		comb2.setLazyRender(true);// ��̬��
//		comb2.setEditable(true);
//		egu.addToolbarItem(comb2.getScript());

		

		
		egu.addTbarText("����:");
		MonthField dfRIQ = new MonthField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		String sRefreshHandler = 
				"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
				"if (grid_Mrcd.length>0) {" +
				"Ext.MessageBox.confirm('��Ϣ��ʾ','ˢ�½�����������ĸ��Ľ���������,�Ƿ����?',function(btn){" +
				"if (btn == 'yes') {" +
				"document.getElementById('RefreshButton').click();" +
				"}" +
				"})" +
				"}else {document.getElementById('RefreshButton').click();}" +
				"}";

		GridButton gRefresh = new GridButton("ˢ��",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
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
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init() {
		setExtGrid(null);
		setRiq((new SimpleDateFormat("yyyy-MM")).format(new Date()));
		getSelectData();
	}
}
