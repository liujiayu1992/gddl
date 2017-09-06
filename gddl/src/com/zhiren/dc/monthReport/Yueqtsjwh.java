package com.zhiren.dc.monthReport;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*
 * ���ߣ�hc
 * ʱ�䣺2014-3-12
 * ������������
 */

public  class Yueqtsjwh extends BasePage implements PageValidateListener {
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
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Shenc() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String cxsql = "select d.id,nvl(y.RLBMDJBNJH,0)RLBMDJBNJH,nvl(y.RLBMLBNJH,0)RLBMLBNJH \n"
				+ "from (select * from yuebqtsjb where riq=add_months(to_date('"
				+ strDate
				+ "','yyyy-mm-dd'),-1)) y,diancxxb d \n"
				+ "where y.diancxxb_id(+)=d.id and d.jib>2 and d.id not in (300)";
		ResultSetList cxrsl = con.getResultSetList(cxsql);
		StringBuffer sql = new StringBuffer("begin\n");
		while (cxrsl.next()) {
			sql
					.append(
							"insert into yuebqtsjb (id,riq,diancxxb_id,RLBMDJBNJH,RLBMLBNJH)")
					.append(
							"values(getnewid(" + visit.getDiancxxb_id()
									+ "),to_date('" + strDate
									+ "','yyyy-mm-dd'),").append(
							cxrsl.getLong("id")).append(",").append(
							cxrsl.getDouble("RLBMDJBNJH")).append(",").append(
							cxrsl.getDouble("RLBMLBNJH")).append(");\n");
		}

		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			setMsg("����ʧ�ܣ�");
		} else {
			setMsg("����ɹ���");
		}
		con.Close();
	}

	private void Shanc() {
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String scsql = "delete from yuebqtsjb where riq=date'" + strDate + "'";
		int flag = con.getDelete(scsql);
		if (flag == -1) {
			setMsg("ɾ��ʧ�ܣ�");
		} else {
			setMsg("ɾ���ɹ���");
		}
		con.Close();
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin\n");

		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update yuebqtsjb set \n").append("rlbmdjxyjh = ")
					.append(mdrsl.getDouble("rlbmdjxyjh")).append(
							",rlbmdjbyjh = ").append(
							mdrsl.getDouble("rlbmdjbyjh")).append(
							",rlbmdjjcsbxyjh = ").append(
							mdrsl.getDouble("rlbmdjjcsbxyjh")).append(
							",rlbmlbyjh=").append(mdrsl.getDouble("rlbmlbyjh"))
					.append(",rlbmlxyjh=").append(mdrsl.getDouble("rlbmlxyjh"))
					.append(",byzgl=").append(mdrsl.getDouble("byzgl")).append(
							",byzgj=").append(mdrsl.getDouble("byzgj")).append(
							",rlbmdjbnjh=").append(
							mdrsl.getDouble("rlbmdjbnjh"))
					.append(",rlbmlbnjh=").append(mdrsl.getDouble("rlbmlbnjh"))
					.append(" where id = ").append(mdrsl.getString("id"))
					.append(";\n");
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			setMsg("����ʧ�ܣ�");
		} else {
			setMsg("����ɹ���");
		}
		mdrsl.close();
		con.Close();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ShencChick = false;

	public void ShencButton(IRequestCycle cycle) {
		_ShencChick = true;
	}

	private boolean _ShancChick = false;

	public void ShancButton(IRequestCycle cycle) {
		_ShancChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_ShencChick) {
			_ShencChick = false;
			Shenc();
		}
		if (_ShancChick) {
			_ShancChick = false;
			Shanc();
		}
		getSelectData();
	}
	
	public void getSelectData() {
		String strDate = getNianfValue().getValue() + "-"
		+ getYuefValue().getValue() + "-01";
		String sDate = getNianfValue().getValue() + "-"
		+ getYuefValue().getValue();
		JDBCcon con = new JDBCcon();
		String sql = "select y.id,to_char(y.riq,'yyyy-mm-dd')riq,d.mingc diancxxb_id,\n"
				+ "round(y.rlbmdjxyjh,2)rlbmdjxyjh,round(y.rlbmdjbyjh,2)rlbmdjbyjh,\n"
				+ "round(y.rlbmdjjcsbxyjh,2)rlbmdjjcsbxyjh,round(y.rlbmlbyjh,0)rlbmlbyjh,\n"
				+ "round(y.rlbmlxyjh,0)rlbmlxyjh,\n"
				+ "round(y.byzgl,0)byzgl,round(y.byzgj,2)byzgj,round(y.rlbmdjbnjh,2)rlbmdjbnjh,\n"
				+ "round(y.rlbmlbnjh,0)rlbmlbnjh\n"
				+ "from yuebqtsjb y,diancxxb d\n"
				+ "where y.diancxxb_id=d.id and y.riq=date'"
				+ strDate
				+ "'\n"
				+ "order by d.xuh,d.mingc";
		ResultSetList rsl = con.getResultSetList(sql);
		boolean a = false;
		if (rsl.next()) {
			a = true;
		}
		rsl.beforefirst();

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth("bodyWidth");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(23);// ���÷�ҳ
		egu.setTableName("yuebqtsjb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("riq").setCenterHeader("����");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rlbmdjxyjh").setCenterHeader("��¯��ú<br>�������¼ƻ�");
		egu.getColumn("rlbmdjxyjh").setWidth(90);
		egu.getColumn("rlbmdjxyjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmdjxyjh").editor)
				.setDecimalPrecision(2);
		egu.getColumn("rlbmdjbyjh").setCenterHeader("ȼ�ϱ�ú<br>���۱��¼ƻ�");
		egu.getColumn("rlbmdjbyjh").setWidth(90);
		egu.getColumn("rlbmdjbyjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmdjbyjh").editor)
				.setDecimalPrecision(2);
		egu.getColumn("rlbmdjjcsbxyjh").setCenterHeader("ȼ�ϱ�ú����<br>�����ϱ��ƻ�");
		egu.getColumn("rlbmdjjcsbxyjh").setWidth(90);
		egu.getColumn("rlbmdjjcsbxyjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmdjjcsbxyjh").editor)
				.setDecimalPrecision(2);
		egu.getColumn("rlbmlbyjh").setCenterHeader("��¯��<br>ú�����¼ƻ�");
		egu.getColumn("rlbmlbyjh").setWidth(90);
		egu.getColumn("rlbmlbyjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmlbyjh").editor)
				.setDecimalPrecision(0);
		egu.getColumn("rlbmlxyjh").setCenterHeader("��¯��<br>ú�����¼ƻ�");
		egu.getColumn("rlbmlxyjh").setWidth(90);
		egu.getColumn("rlbmlxyjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmlxyjh").editor)
				.setDecimalPrecision(0);
//		egu.getColumn("rzcbyjh").setCenterHeader("��ֵ��<br>���¼ƻ�");
//		egu.getColumn("rzcbyjh").setWidth(80);
//		egu.getColumn("rzcbyjh").editor.setMaxValue("999");
//		egu.getColumn("rzcbyjh").editor.setMinValue("-999");
//		((NumberField) egu.getColumn("rzcbyjh").editor).setDecimalPrecision(2);
		
		egu.getColumn("byzgl").setCenterHeader("�����ݹ���");
		egu.getColumn("byzgl").setWidth(80);
		egu.getColumn("byzgl").editor.setMinValue("0");
		((NumberField) egu.getColumn("byzgl").editor).setDecimalPrecision(0);
		egu.getColumn("byzgj").setCenterHeader("�����ݹ���<br>(���˷Ѳ���˰)");
		egu.getColumn("byzgj").setWidth(90);
		egu.getColumn("byzgj").editor.setMinValue("0");
		((NumberField) egu.getColumn("byzgj").editor).setDecimalPrecision(2);
		egu.getColumn("rlbmdjbnjh").setCenterHeader("��¯��ú<br>���۱���ƻ�");
		egu.getColumn("rlbmdjbnjh").setWidth(90);
		egu.getColumn("rlbmdjbnjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmdjbnjh").editor)
				.setDecimalPrecision(2);
		egu.getColumn("rlbmlbnjh").setCenterHeader("��¯��<br>ú������ƻ�");
		egu.getColumn("rlbmlbnjh").setWidth(90);
		egu.getColumn("rlbmlbnjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmlbnjh").editor)
				.setDecimalPrecision(0);

		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���

//		 �趨�������������Զ�ˢ��
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		if(!a){
			String handler = "function (){Ext.MessageBox.confirm('��ʾ��Ϣ','�Ƿ�����"+sDate+"���������ݣ�',function(btn){if(btn == 'yes'){" +
			"document.getElementById('ShencButton').click();"+
			"Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...'," +
			"progressText:'������...',width:300,wait:true,waitConfig: " +
			"{interval:200},icon:Ext.MessageBox.INFO});} // end if \n});}";
			GridButton cpy = new GridButton("����", handler);
			cpy.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(cpy);
		}
		if (a) {
			String shanc = "function (){Ext.MessageBox.confirm('��ʾ��Ϣ','�Ƿ�ɾ��"+sDate+"���������ݣ�',function(btn){if(btn == 'yes'){" +
					"document.getElementById('ShancButton').click();"+
					"Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...'," +
					"progressText:'������...',width:300,wait:true,waitConfig: " +
					"{interval:200},icon:Ext.MessageBox.INFO});} // end if \n});}";
			GridButton shc = new GridButton("ɾ��", shanc);
			shc.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(shc);
		}
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
			getExtGrid().addToolbarItem("'<marquee width=200 scrollamount=2>" + getTbmsg()+ "</marquee>'");
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
			setTbmsg(null);
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
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 12) {
				_nianf = _nianf + 1;
			}
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
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
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
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
				int _yuef = DateUtil.getMonth(new Date());
				if (_yuef == 12) {
					_yuef = 1;
				} else {
					_yuef = _yuef + 1;
				}
				for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
					Object obj = _YuefModel.getOption(i);
					if (_yuef == ((IDropDownBean) obj).getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}

		public void setYuefValue(IDropDownBean Value) {
			long id = -2;
			if (_YuefValue != null) {
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
			for (int i = 1; i < 13; i++) {
				listYuef.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
		}
}