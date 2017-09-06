package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Kucmjgds extends BasePage {
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
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		JDBCcon con=new JDBCcon();
		ResultSetList changList=getExtGrid().getModifyResultSet(getChange());
		int rs=0;
		String sql="";
		while(changList.next()){
			String id=changList.getString("id");
			if(id.equals("-1")){
				continue;
			}
			double JINML=changList.getDouble("JINML");
			double HAOYL=changList.getDouble("HAOYL");
			double KUC=changList.getDouble("KUC");
			double KUCRZ=changList.getDouble("KUCRZ");
			double KUCBMDJ=changList.getDouble("KUCBMDJ");
			sql+="update KUCMJGB\n" +
				"   set JINML = "+JINML+", HAOYL = "+HAOYL+", KUC = "+KUC+",KUCRZ="+KUCRZ+",KUCBMDJ="+KUCBMDJ+"\n" + 
				" where id = "+id+";\n";

		}
		if(sql.length()!=0){
			rs=con.getInsert("begin\n"+sql.toString()+"\n end;");
			if (rs==-1) {
				setMsg("����ʧ�ܣ�");
			}else{
				setMsg("����ɹ���");
			}
		}
		con.Close();
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
		DelData();
		long diancxxb_id = Long.parseLong(getTreeid());
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		JDBCcon con = new JDBCcon();

		String sql="INSERT INTO KUCMJGB\n" +
		"  (ID, DIANCXXB_ID, RIQ, MEICB_ID, JINML, HAOYL, KUC, KUCRZ, KUCBMDJ)\n" + 
		"  SELECT GETNEWID(1), "+diancxxb_id+","+CurDate+", M.ID, 0, 0, 0, 0, 0\n" + 
		"    FROM MEICB M";
		con.getInsert(sql);
		sql="SELECT K.MEICB_ID, K.JINML, K.HAOYL, K.KUC, K.KUCRZ, K.KUCBMDJ\n" +
			"  FROM KUCMJGB K\n" + 
			" WHERE k.diancxxb_id= "+diancxxb_id+" AND k.riq="+CurDate+"-1";
		ResultSetList rsl=con.getResultSetList(sql);
		while(rsl.next()){
			double KUC=rsl.getDouble("KUC");
			double KUCRZ=rsl.getDouble("KUCRZ");
			double KUCBMDJ=rsl.getDouble("KUCBMDJ");
			sql="update KUCMJGB\n" +
				"   set KUC = "+KUC+",KUCRZ="+KUCRZ+",KUCBMDJ="+KUCBMDJ+"\n" + 
				" where MEICB_ID = "+rsl.getString("MEICB_ID")+" and diancxxb_id= "+diancxxb_id+" AND riq="+CurDate;
			con.getUpdate(sql);
		}
		rsl.close();
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
		sb.append("delete from KUCMJGB where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq = ")
		.append(CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
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
		getSelectData();
	}
	
	public void getSelectData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
//		�����Ƿ�ɱ༭
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT K.ID, M.MINGC MEICB_ID, K.JINML, K.HAOYL, K.KUC, K.KUCRZ, K.KUCBMDJ\n" +
				"  FROM KUCMJGB K, MEICB M\n" + 
				" WHERE K.MEICB_ID = M.ID \n"+
				"	and k.diancxxb_id="+diancxxb_id+"\n"+
				"   and K.riq = "+CurDate+"" );
		
		JDBCcon con = new JDBCcon();

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("KUCMJG");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("MEICB_ID").setHeader("ú��");
		egu.getColumn("MEICB_ID").setWidth(100);
		egu.getColumn("MEICB_ID").setEditor(null);
		
		egu.getColumn("JINML").setHeader("��ú��(��)");
		egu.getColumn("JINML").setWidth(100);
		((NumberField)egu.getColumn("JINML").editor).setDecimalPrecision(2);
				
		egu.getColumn("HAOYL").setHeader("������(��)");
		egu.getColumn("HAOYL").setWidth(100);
		((NumberField)egu.getColumn("HAOYL").editor).setDecimalPrecision(2);
		
		egu.getColumn("KUC").setHeader("���(��)");
		egu.getColumn("KUC").setWidth(100);
		((NumberField)egu.getColumn("KUC").editor).setDecimalPrecision(2);
		
		egu.getColumn("KUCRZ").setHeader("�����ֵ(MJ/kg)");
		egu.getColumn("KUCRZ").setWidth(100);
		((NumberField)egu.getColumn("KUCRZ").editor).setDecimalPrecision(2);
		
		egu.getColumn("KUCBMDJ").setHeader("����ú����(Ԫ/��)");
		egu.getColumn("KUCBMDJ").setWidth(120);
		((NumberField)egu.getColumn("KUCBMDJ").editor).setDecimalPrecision(2);
		
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

		GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//				ɾ����ť
		GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
//				���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		egu.addTbarBtn(gbs);

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����:<br>")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
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
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),0,DateUtil.AddType_intDay)));
			setChangbModels();
			getSelectData();
			setTreeid(null);
		}
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
