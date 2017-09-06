package com.zhiren.jt.zdt.xitgl.gongyspj.pingjdjfs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pingjdjfs extends BasePage implements PageValidateListener {
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
			
			
			sb.append("update pingjdjfsb set ");
			sb.append("fens=").append(rsl.getDouble("��")).append(" where pingjmcb_id=get_Pingjmcb_Id").append("('"+rsl.getString("pingjmcb_id")+"')");
			sb.append(" and pingjxmb_id=get_Pingjxmb_Id").append("('"+rsl.getString("pingjxmb_id")+"')");
			sb.append(" and pingjxmdjb_id=(select id from pingjxmdjb where mingc='��') ;\n");
			
			sb.append("update pingjdjfsb set ");
			sb.append("fens=").append(rsl.getDouble("��")).append(" where pingjmcb_id=get_Pingjmcb_Id").append("('"+rsl.getString("pingjmcb_id")+"')");
			sb.append(" and pingjxmb_id=get_Pingjxmb_Id").append("('"+rsl.getString("pingjxmb_id")+"')");
			sb.append(" and pingjxmdjb_id=(select id from pingjxmdjb where mingc='��') ;\n");
			
			sb.append("update pingjdjfsb set ");
			sb.append("fens=").append(rsl.getDouble("��")).append(" where pingjmcb_id=get_Pingjmcb_Id").append("('"+rsl.getString("pingjmcb_id")+"')");
			sb.append(" and pingjxmb_id=get_Pingjxmb_Id").append("('"+rsl.getString("pingjxmb_id")+"')");
			sb.append(" and pingjxmdjb_id=(select id from pingjxmdjb where mingc='��') ;\n");
			
			sb.append("update pingjdjfsb set ");
			sb.append("fens=").append(rsl.getDouble("��")).append(" where pingjmcb_id=get_Pingjmcb_Id").append("('"+rsl.getString("pingjmcb_id")+"')");
			sb.append(" and pingjxmb_id=get_Pingjxmb_Id").append("('"+rsl.getString("pingjxmb_id")+"')");
			sb.append(" and pingjxmdjb_id=(select id from pingjxmdjb where mingc='��') ;\n");
			
			
			
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
		
		String DeleteStr="delete pingjdjfsb where pingjmcb_id="+pingjmc_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	public void Insert(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long pingjmc_id=0;
		pingjmc_id=this.getPingjmcValue().getId();
		ResultSetList Re = con.getResultSetList("select * from pingjdjfsb p where p.pingjmcb_id="+pingjmc_id+"");
		if(Re.next()){
			con.Close();
			this.setMsg(" <"+getPingjmcValue().getValue()+">ϵͳ���Ѿ�����,�������ظ����! ");
			return;
		}
		
		
		StringBuffer sbInsert = new StringBuffer("begin \n");
		long pingjxmb_id=0;
		long pingjxmdjb_id=0;
		ResultSetList rsl = con.getResultSetList("select id from pingjxmb ");
		while (rsl.next()){
			pingjxmb_id=rsl.getLong("id");
			ResultSetList rs = con.getResultSetList("select id from pingjxmdjb ");
			while(rs.next()){
				pingjxmdjb_id=rs.getLong("id");
				long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sbInsert.append("insert into pingjdjfsb (id,pingjmcb_id,pingjxmb_id,pingjxmdjb_id,fens,beiz)")
				.append(" values ("+id+","+pingjmc_id+","+pingjxmb_id+","+pingjxmdjb_id+",(select  fens from pingjdjfsb p where p.pingjmcb_id=(select id from pingjmcb where mingc='Ĭ�����۷�������' ) and p.pingjxmb_id="+pingjxmb_id+" and p.pingjxmdjb_id="+pingjxmdjb_id+"),'');").append("\n");
				
			}	
			
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
		
	
		
		String chaxun = "select min(fs.id) as id,mc.mingc as pingjmcb_id,  xm.mingc as pingjxmb_id  ,\n"
				+ "      max(decode(fs.pingjxmdjb_id,1,fs.fens,'')) as ��,\n"
				+ "      max(decode(fs.pingjxmdjb_id,2,fs.fens,'')) as ��,\n"
				+ "      max(decode(fs.pingjxmdjb_id,3,fs.fens,'')) as ��,\n"
				+ "      max(decode(fs.pingjxmdjb_id,4,fs.fens,'')) as ��\n"
				+""
				+ "from pingjdjfsb fs ,pingjxmb xm,pingjxmdjb dj,pingjmcb mc\n"
				+ "where fs.pingjxmb_id=xm.id\n"
				+ "and   fs.pingjxmdjb_id=dj.id\n"
				+ "and  fs.pingjmcb_id=mc.id\n"
				+ "and  mc.id="+pingjmc+"\n"
				+ "group by (mc.mingc ,xm.mingc )\n" + "order by max(xm.xuh )";

	
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("pingjdjfsb");
   	egu.setWidth("bodyWidth");

	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);

	egu.getColumn("pingjmcb_id").setCenterHeader("��������");
	egu.getColumn("pingjmcb_id").setHidden(true);
	egu.getColumn("pingjmcb_id").setEditor(null);
	
	egu.getColumn("pingjxmb_id").setCenterHeader("��������");
	egu.getColumn("pingjxmb_id").setEditor(null);
	egu.getColumn("��").setCenterHeader("��");
	egu.getColumn("��").setCenterHeader("��");
	egu.getColumn("��").setCenterHeader("��");
	egu.getColumn("��").setCenterHeader("��");
	
	
//	�趨���ɱ༭�е���ɫ
	egu.getColumn("pingjxmb_id").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	
	egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
	
	//�趨�г�ʼ���
	
	egu.getColumn("pingjxmb_id").setWidth(200);
	egu.getColumn("��").setWidth(60);
	egu.getColumn("��").setWidth(60);
	egu.getColumn("��").setWidth(60);
	egu.getColumn("��").setWidth(60);
	
	
//	�趨�е�С��λ
	//((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(30);//���÷�ҳ
	
	
	

	
	
	//*************************������*****************************************88
	
	//********************������************************************************
		egu.addTbarText("��������:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("PINGJMC");
		comb1.setId("PINGJMC");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(150);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		
		
		
		//�趨�������������Զ�ˢ��
		egu.addOtherScript("PINGJMC.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
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

		String ss="Ext.MessageBox.confirm('����', '��ȷ��Ҫɾ����ҳ���е�����������', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
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
			this.setNianfValue(null);
			this.getNianfModels();
		
			this.setYuefValue(null);
			this.getYuefModels();
			setTbmsg(null);
			
			this.setPingjmcValue(null);
			this.getIPingjmcModel();
			this.getIPingjmcModels();
		}
			
			getSelectData();
		
		
	}
//	 ���
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	


//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

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
			sql = "select id,mingc from pingjmcb order by xuh";
			_IPingjmcModel = new IDropDownModel(sql,"��ѡ��");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IPingjmcModel;
	}
}
