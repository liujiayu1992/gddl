package com.zhiren.jt.zdt.xitgl.gongyspj.pingjfs;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;


import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pingjfs extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


	private void Save() {
		
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		
		while (rsl.next()) {
			sb.append("update pingjfsb set dengjfs=");
			sb.append(rsl.getString("dengjfs")).append("  where id="+rsl.getLong("id")+";\n");
		}
		sb.append("end;");
		con.getUpdate(sb.toString());
	
	
		con.Close();
	
		setMsg("����ɹ�!");
		
		
	}
	
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _InsertButton = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertButton = true;
	}
	private boolean _DeleteButton = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
		if(_InsertButton){
			_InsertButton=false;
			Insert();
			getSelectData();
		}
		if(_DeleteButton){
			_DeleteButton = false;
			Delete();
			getSelectData();
		}
		
	}
	
	public void Delete(){
		
		JDBCcon con = new JDBCcon();
		long pingjmc_id=0;
		
		pingjmc_id=this.getPingjmcValue().getId();
		
		
		String DeleteStr="delete pingjfsb where pingjmcb_id="+pingjmc_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	public void Insert(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long pingjmc_id=0;
	
		pingjmc_id=this.getPingjmcValue().getId();
		
		
		ResultSetList Re = con.getResultSetList("select * from pingjfsb p where p.pingjmcb_id="+pingjmc_id+"");
		if(Re.next()){
			con.Close();
			this.setMsg(" <"+getPingjmcValue().getValue()+">�ĵȼ�����ϵͳ���Ѿ�����,�������ظ����! ");
			return;
		}
		StringBuffer sbInsert = new StringBuffer("begin \n");
		long pingjdjb_id=0;
		
		ResultSetList rsl = con.getResultSetList("select id from pingjdjb ");
		while (rsl.next()){
			pingjdjb_id=rsl.getLong("id");
			long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
			
			sbInsert.append("insert into pingjfsb (id,pingjmcb_id,pingjdjb_id,dengjfs)")
			.append(" values ("+id+","+pingjmc_id+","+pingjdjb_id+",null);").append("\n");
		}
		
		sbInsert.append("end;");
		if(sbInsert.length()>10){
			con.getInsert(sbInsert.toString());
		}
		con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long pingjmc=0;
		pingjmc=this.getPingjmcValue().getId();
		

		String chaxun = "select fs.id,mc.mingc as pingjmcb_id,dj.mingc as pingjdjb_id,fs.dengjfs\n"
				+ "from pingjfsb fs,pingjdjb dj,pingjmcb mc\n"
				+ "where fs.pingjmcb_id=mc.id\n"
				+ "and fs.pingjdjb_id=dj.id\n"
				+ "and mc.id="+pingjmc+"";
;



		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("pingjfsb");
   	
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);

	egu.getColumn("pingjmcb_id").setCenterHeader("��������");
	egu.getColumn("pingjmcb_id").setHidden(true);
	egu.getColumn("pingjmcb_id").setEditor(null);
	
	egu.getColumn("pingjdjb_id").setCenterHeader("�ȼ�����");
	egu.getColumn("pingjdjb_id").setEditor(null);
	egu.getColumn("dengjfs").setCenterHeader("�ȼ�����");

	
//	�趨���ɱ༭�е���ɫ
	egu.getColumn("pingjdjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	
	egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
	
	//�趨�г�ʼ���
	
	
	
	
//	�趨�е�С��λ
	//((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(100);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
	
	
	//********************������************************************************
		egu.addTbarText("��������:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("PINGJMC");
		comb1.setId("PINGJMC");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(150);
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
	
		
		egu.addTbarText("-");// ���÷ָ���
		
		
		//�趨�������������Զ�ˢ��
		egu.addOtherScript("PINGJMC.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
	
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		��Ӱ�ť
		StringBuffer ins = new StringBuffer();
		ins.append("function (){")
		.append(MainGlobal.getExtMessageBox("'�����������,���Ժ�'",true))
		.append("document.getElementById('InsertButton').click();}");
		GridButton gbIns = new GridButton("���",ins.toString());
		gbIns.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(gbIns);
//		ɾ����ť

		String ss="Ext.MessageBox.confirm('����', '��ȷ��Ҫɾ����ҳ�ĵȼ�������', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
		StringBuffer del = new StringBuffer();
		del.append("function (){")
		.append(""+ss+"}");
		GridButton gbDel = new GridButton("ɾ��",del.toString());
		gbDel.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbDel);
		
		//���水ť
		
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
		
		egu.addTbarText("->");
		

		
		
		
		
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
		if (getExtGrid() == null) {
			return "";
		}
		
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
		
			setTbmsg(null);
		
			this.setPingjmcValue(null);
			this.getIPingjmcModels();
			
		}
			
			getSelectData();
		
		
	}


	
	

	

//	 ��������
	public boolean _PingjmcChange = false;

	private IDropDownBean _PingjmcValue;

	public IDropDownBean getPingjmcValue() {
		if (_PingjmcValue == null) {
			_PingjmcValue = (IDropDownBean) getIPingjmcModels().getOption(0);
		}
		return _PingjmcValue;
	}

	public void setPingjmcValue(IDropDownBean Value) {
		long id = -2;
		if (_PingjmcValue != null) {
			id = _PingjmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_PingjmcChange = true;
			} else {
				_PingjmcChange = false;
			}
		}
		_PingjmcValue = Value;
	}

	private IPropertySelectionModel _IPingjmcModel;

	public void setIPingjmcModel(IPropertySelectionModel value) {
		_IPingjmcModel = value;
	}

	public IPropertySelectionModel getIPingjmcModel() {
		if (_IPingjmcModel == null) {
			getIPingjmcModels();
		}
		return _IPingjmcModel;
	}

	public IPropertySelectionModel getIPingjmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from pingjmcb order by riq";
			_IPingjmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IPingjmcModel;
	}
	
///////////////////////////////////////	


	


}
