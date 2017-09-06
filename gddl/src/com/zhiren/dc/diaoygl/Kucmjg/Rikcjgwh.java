package com.zhiren.dc.diaoygl.Kucmjg;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Rikcjgwh extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	������
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	ҳ��仯��¼
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

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private void CreateData() {
		String diancxxb_id = getTreeid();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		JDBCcon con = new JDBCcon();
//		���������������Ϣ
		DelData();
//		д�뵱����Ϣ
		String sql=
			"INSERT INTO KUCMRBB\n" +
			"  (ID, RIQ, DIANCXXB_ID, DCMZB_ID)\n" + 
			"  (SELECT GETNEWID(DIANCXXB_ID) ID,\n" + 
			"          "+CurDate+",\n" + 
			"          DIANCXXB_ID,\n" + 
			"          ID DCMZB_ID\n" + 
			"     FROM DCMZB\n" + 
			"    WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"      AND ZHUANGT = 1)";
		con.getInsert(sql);
//		�����յ�������úԤ����Ϣ����������
		sql="UPDATE (SELECT DCMZB_ID, XZLMYC\n" +
			"          FROM KUCMRBB\n" + 
			"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND RIQ = "+CurDate+") K\n" + 
			"   SET K.XZLMYC =\n" + 
			"       (SELECT NVL(XZLMYC,0)\n" + 
			"          FROM KUCMRBB\n" + 
			"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND RIQ = ("+CurDate+" - 1)\n" + 
			"           AND K.DCMZB_ID = DCMZB_ID)";
		con.getUpdate(sql);
		setMsg("�����������");
		con.Close();
	}

	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("delete from KUCMRBB where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq = ")
		.append(CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		setMsg("����ɾ�����");
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
	}
	
	public void getSelectData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
//		�����Ƿ�ɱ༭
		boolean isEditable=true;
		boolean isCreat=true;
		String sql="";
		sql="SELECT K.ID,\n" +
			"       K.RIQ,\n" + 
			"       K.DIANCXXB_ID,\n" + 
			"       M.MEIZMC DCMZB_ID,\n" + 
			"       K.LAIMSL,\n" + 
			"       K.LAIMRZ,\n" + 
			"       K.LAIMRLF,\n" + 
			"       K.HAOML,\n" + 
			"       K.HAOMRZ,\n" + 
			"       K.HAOMRLF,\n" + 
			"       K.KUCSL,\n" + 
			"       K.KUCRL,\n" + 
			"       K.KUCYMDJ,\n" + 
			"       K.XZLMYC\n" + 
			"  FROM KUCMRBB K, DIANCXXB DC, DCMZB M\n" + 
			" WHERE K.DCMZB_ID = M.ID\n" + 
			"   AND K.DIANCXXB_ID = DC.ID\n" + 
			"   AND K.RIQ = "+CurDate+"\n" + 
			"   AND DC.ID = "+diancxxb_id;

		JDBCcon con = new JDBCcon();

		Date curdate=new Date();
		long t=(curdate.getTime()-DateUtil.getDate(getRiq()).getTime())/(3600*24*1000);
//			���ʱ�����2�죬��ô������ʾ�༭��ذ�ť��Ϊ�ر�״̬
//			��ϵͳ��Ϣ����ȡ���ձ����ɱ༭������Ϣ��Ĭ��ֵΪ2�졣
		if(t<0 || t>Long.parseLong(MainGlobal.getXitxx_item("����ձ�", "�ձ����ɱ༭����", "0", "1"))){
			isEditable=false;
		}
//		�ж���ѡ�������Ƿ�������
		if(con.getHasIt(sql)){
			isCreat=false;
		}

		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" +sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("KUCMRBB");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("RIQ").setHidden(true);
		egu.getColumn("RIQ").setEditor(null);
		
		egu.getColumn("DIANCXXB_ID").setHidden(true);
		egu.getColumn("DIANCXXB_ID").setEditor(null);
		egu.getColumn("DIANCXXB_ID").setUpdate(false);
		
		egu.getColumn("DCMZB_ID").setHeader("ú��");
		egu.getColumn("DCMZB_ID").setWidth(100);
		egu.getColumn("DCMZB_ID").setEditor(null);
		egu.getColumn("DCMZB_ID").setUpdate(false);
		
		egu.getColumn("LAIMSL").setHeader("��ú����<br>(��)");
		egu.getColumn("LAIMSL").setWidth(80);
		((NumberField)egu.getColumn("LAIMSL").editor).setDecimalPrecision(2);
		
		egu.getColumn("LAIMRZ").setHeader("��ú��ֵ<br>(Kcal/kg)");
		egu.getColumn("LAIMRZ").setWidth(60);
		((NumberField)egu.getColumn("LAIMRZ").editor).setDecimalPrecision(0);
		
		egu.getColumn("LAIMRLF").setHeader("��úȼ�Ϸ�<br>����˰���˷�(Ԫ)");
		egu.getColumn("LAIMRLF").setWidth(120);
		((NumberField)egu.getColumn("LAIMRLF").editor).setDecimalPrecision(2);
		
		egu.getColumn("HAOML").setHeader("��ú����<br>(��)");
		egu.getColumn("HAOML").setWidth(80);
		((NumberField)egu.getColumn("HAOML").editor).setDecimalPrecision(2);
		
		egu.getColumn("HAOMRZ").setHeader("��ú��ֵ<br>(Kcal/kg)");
		egu.getColumn("HAOMRZ").setWidth(60);
		((NumberField)egu.getColumn("HAOMRZ").editor).setDecimalPrecision(0);
		
		egu.getColumn("HAOMRLF").setHeader("��úȼ�Ϸ�<br>(Ԫ)");
		egu.getColumn("HAOMRLF").setWidth(120);
		((NumberField)egu.getColumn("HAOMRLF").editor).setDecimalPrecision(2);
		
		egu.getColumn("KUCSL").setHeader("�������<br>(��)");
		egu.getColumn("KUCSL").setWidth(80);
		((NumberField)egu.getColumn("KUCSL").editor).setDecimalPrecision(2);
		
		egu.getColumn("KUCRL").setHeader("�����ֵ<br>(Kcal/kg)");
		egu.getColumn("KUCRL").setWidth(60);
		((NumberField)egu.getColumn("KUCRL").editor).setDecimalPrecision(0);
		
		egu.getColumn("KUCYMDJ").setHeader("��治��˰<br>ԭú����<br>(Ԫ/��)");
		egu.getColumn("KUCYMDJ").setWidth(80);
		((NumberField)egu.getColumn("KUCYMDJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("XZLMYC").setHeader("������ú<br>Ԥ��(��)");
		egu.getColumn("XZLMYC").setWidth(80);
		((NumberField)egu.getColumn("XZLMYC").editor).setDecimalPrecision(2);
		
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish); 
		egu.addTbarBtn(gbr);
		
		if(isEditable){
			if(isCreat){
				GridButton gbc = new GridButton("�",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
			}else{
//				ɾ����ť
				GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
//				���水ť
				GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
				egu.addTbarBtn(gbs);
			}
		}

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("�Ƿ��").append(cnDate).append("�����ݣ�");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
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
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setTreeid(null);
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
