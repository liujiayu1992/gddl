package com.zhiren.dc.gdxw.qicjs;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qicjs extends BasePage implements PageValidateListener {
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
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin \n");
		
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("id"))||"".equals(mdrsl.getString("id"))) {
				
			} else {
				sbsql.append("update suocztb set jiessj = sysdate,jiesry='"+visit.getRenymc()+"',")
				.append(" zt= decode('"+ mdrsl.getString("zt")+ "','������',1,2)").append(" where id = ").append(mdrsl.getString("id"))
				.append("; \n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
		long SuocID=this.getMeikdqmcValue().getId();
		
		sb.append("select s.id, c.cheph,c.meikdwmc,c.yunsdw,to_char(s.suocsj,'yyyy-mm-dd hh24:mi:ss') as suocsj,s.suocry,s.suocyy,"); 
		if(SuocID==2){
			sb.append("to_char(s.jiessj,'yyyy-mm-dd hh24:mi:ss') as jiessj,s.jiesry,\n");
		}
		sb.append(	"decode(s.zt,1,'������','�ѽ���') as zt\n");
		sb.append("from chepbtmp c,suocztb s,diancxxb d\n");
		sb.append(" where s.chepbtmp_id=c.id\n");
		sb.append(" and c.diancxxb_id=d.id\n");
		sb.append(" and s.zt="+SuocID+"\n"); 
		sb.append(" AND d.ID = ").append(visit.getDiancxxb_id());
		sb.append("  order by s.suocsj");

		
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�����Ƿ���Ա༭
		egu.setTableName("suocztb");
	
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").width=80;
		egu.getColumn("cheph").editor=null;
		egu.getColumn("meikdwmc").setHeader("ú������");
		egu.getColumn("meikdwmc").width=100;
		egu.getColumn("meikdwmc").editor=null;
		egu.getColumn("yunsdw").setHeader("���䵥λ");
		egu.getColumn("yunsdw").width=80;
		egu.getColumn("yunsdw").editor=null;
		egu.getColumn("suocsj").setHeader("����ʱ��");
		egu.getColumn("suocsj").width=120;
		egu.getColumn("suocsj").editor=null;
		egu.getColumn("suocry").setHeader("������Ա");
		egu.getColumn("suocry").width=80;
		egu.getColumn("suocry").editor=null;
		egu.getColumn("suocyy").setHeader("����ԭ��");
		egu.getColumn("suocyy").width=120;
		egu.getColumn("suocyy").editor=null;
		egu.getColumn("zt").setHeader("����״̬");
		
		if(SuocID==2){
			egu.getColumn("jiessj").setHeader("����ʱ��");
			egu.getColumn("jiessj").width=120;
			egu.getColumn("jiessj").editor=null;
			egu.getColumn("jiesry").setHeader("������Ա");
			egu.getColumn("jiesry").width=80;
			egu.getColumn("jiesry").editor=null;
		}
		//����Grid����
		egu.addPaging(25);
		//����״̬
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"������"));
		list.add(new IDropDownBean(2,"�ѽ���"));
		egu.getColumn("zt").setEditor(new ComboBox());
		
		egu.getColumn("zt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("zt").setReturnId(true);
		
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
		if(SuocID==1){
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		
		egu.addTbarText("-");
		egu.addTbarText("-");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// �趨�������������Զ�ˢ��
		egu.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});");
		
		
		
		
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
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
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			//getSelectData();
		}
		getSelectData();
	}
	
	

	
//	 �������
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select 1 as id,'����������'  as mingc from dual union select 2 as id,'�ѽ�������' as mingc from dual";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
}
