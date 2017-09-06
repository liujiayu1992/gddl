package com.zhiren.dc.diaoygl.Kucmjg;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
 
public  class Dckcmbwh extends BasePage implements PageValidateListener {
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
	
	private void copy(){
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String sqlchk="SELECT GETNEWID(1),DIANCXXB_ID,DATE'"+strDate+"',ZHI FROM DCYKKCMB WHERE RIQ=ADD_MONTHS(DATE'"+strDate+"',-1)";
//		��������������򲻸���
		if(!con.getHasIt(sqlchk)){
			setMsg("����������");
			con.Close();
			return;
		}
		
		String sql = "";
//		//ɾ����������
		sql = "delete DCYKKCMB where riq=DATE'"+strDate+"'";
		int flag = con.getDelete(sql);
		if(flag==-1){
			setMsg("��������ɾ��ʧ��");
			con.Close();
			return;
		}
		//������������
		sql = 
			"INSERT INTO DCYKKCMB\n" +
			"  (ID, DIANCXXB_ID, RIQ, ZHI)\n" + 
			"  (SELECT GETNEWID(1), DIANCXXB_ID, DATE '"+strDate+"', ZHI\n" + 
			"     FROM DCYKKCMB\n" + 
			"    WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1))";
		
		flag = con.getUpdate(sql);
		if(flag==-1)
			setMsg("������������ʧ��");
		else
			setMsg("�����������������");
		
		con.Close();
	}

	private void Save()	{
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();

//		������»����� 
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");
		while (rsl.next()) {
			String ID_1=rsl.getString("ID");
			double ZHI=rsl.getDouble("ZHI");
			sql.append("update DCYKKCMB set ZHI="+ ZHI+" where id=" + ID_1+";\n");
		}
		sql.append("end;");
		int flag=0;
		if(sql.length()>11){
			flag=con.getUpdate(sql.toString());
		}
		
		if (flag>-1){
			setMsg("����ɹ���");
		}else{
			setMsg("����ʧ�ܣ�");
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _CopyChick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefurbishChick){
			_RefurbishChick=false;
			getSelectData();
		}
		if(_CopyChick){
			_CopyChick=false;
			copy();
			getSelectData();
		}
	}
	
	public void createdate(String date){
		JDBCcon con = new JDBCcon();
		String sql=
			"INSERT INTO DCYKKCMB\n" +
			"  (ID, DIANCXXB_ID, RIQ, ZHI)\n" + 
			"  (SELECT GETNEWID(1), ID, DATE '"+date+"', 0 FROM DIANCXXB WHERE JIB = 3)";
		con.getInsert(sql);
		con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		// �����������������
		long intyear;
	
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
//		 ���������·�������
		long intmonth;
		if (getYuefValue()==null){
			intmonth =DateUtil.getMonth(new Date());
		}else{
			intmonth= getYuefValue().getId();
		}
		
		String  chaxun = 
	    	"SELECT K.ID, DC.MINGC DIANCXXB_ID, K.RIQ, K.ZHI\n" +
	    	"  FROM DCYKKCMB K, DIANCXXB DC\n" + 
	    	" WHERE K.DIANCXXB_ID = DC.ID\n" + 
	    	"   AND RIQ = DATE '"+intyear+"-"+intmonth+"-01' ORDER BY DC.XUH";
//		�����ѡ�·�û�����ݣ���������ݽ��г�ʼ��������
		if(!con.getHasIt(chaxun)){
			createdate(intyear+"-"+intmonth+"-01");
		}
		
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("DCYKKCMB");
		egu.setWidth("bodyWidth");
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("ID").setEditor(null);
		
		egu.getColumn("DIANCXXB_ID").setHeader("��λ����");
		egu.getColumn("DIANCXXB_ID").setWidth(120);
		egu.getColumn("DIANCXXB_ID").setEditor(null);
		
		egu.getColumn("RIQ").setHeader("����");
		egu.getColumn("RIQ").setWidth(100);
		egu.getColumn("RIQ").setEditor(null);
		
		egu.getColumn("ZHI").setHeader("Ԥ��Ŀ��ֵ<br>(��)");
		egu.getColumn("ZHI").editor.setAllowBlank(false);
		egu.getColumn("ZHI").setWidth(120);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(23);// ���÷�ҳ
	
		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(60);
		egu.addToolbarItem(comb2.getScript());
		
		egu.addToolbarButton("ˢ��", GridButton.ButtonType_Refresh, "RefurbishButton");
//					
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		
//		String copyscript = "{"+ //���ư�ť
//	    new GridButton("����","function(){document.getElementById('CopyButton').click();\n"
//	    		,SysConstant.Btn_Icon_Copy).getScript()+  "}}";
//		egu.addToolbarItem(copyscript);

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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
//				�ж������ǰ��1�·ݣ����Ϊȥ��
				int year = DateUtil.getYear(new Date());
				if(DateUtil.getMonth(new Date())==1){
					year -= 1;
				}
//				-------
				if (year == ((IDropDownBean) obj).getId()) {
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
		for (i = 2013; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	/**
	 * �·�
	 */
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
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	 
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
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