package com.zhiren.dc.shoukdw;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shoukdw extends BasePage implements PageValidateListener {
	
	private static boolean disf=false;//�ж��Ƿ���ʾ���õ�����
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	 private boolean _ShezChick = false;
	    public void ShezButton(IRequestCycle cycle) {
	    	_ShezChick = true;
	    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_ShezChick){
			_ShezChick = false;
			GotoShezfa();
			getSelectData();
		}	
//		cycle.activate("12321");
	}
	
	private void GotoShezfa() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("begin	\n");
		if(getChange()==null||getChange().equals("")){
			
			sb.append("update shoukdw set shifdsf = 0;\n");
		}else{
			
			ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
			sb.append("update shoukdw set shifdsf = 0;\n");
			while(rsl.next()){
				sb.append("update shoukdw set shifdsf = 1 where id = "+rsl.getString("id")+";\n");
			}
		}
		sb.append("end;\n");
		int flag = con.getUpdate(sb.toString());
		if(flag==-1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		setMsg("ȷ�ϵ������տλ�ɹ���");
		con.commit();
		con.Close();
	}
	
	
//	ͨ��
	public boolean getShifdsf(){//xitxxb������ Ĭ��Ϊ7��
		Visit visit=(Visit)this.getPage().getVisit();
		boolean dsf = false;
		String zhi = "";
		zhi = MainGlobal.getXitxx_item("����", "�Ƿ��������Ӧ��", String.valueOf(visit.getDiancxxb_id()), "0");
		if(zhi.equals("1")){
			dsf = true;
		}
		return dsf;
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id,mingc,quanc,bianm,kaihyh,zhangh,shuih,youzbm,diz,lianxr,lianxdh,shifdsf from shoukdw order by mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("shoukdw");
		egu.setWidth("bodyWidth");
		egu.getColumn("mingc").setHeader("���");
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("kaihyh").setHeader("��������");
		egu.getColumn("zhangh").setHeader("�ʺ�");
		egu.getColumn("shuih").setHeader("˰��");
		egu.getColumn("youzbm").setHeader("��������");
		egu.getColumn("diz").setHeader("��ַ");
		egu.getColumn("lianxr").setHeader("��ϵ��");
		egu.getColumn("lianxdh").setHeader("��ϵ�绰");
//		egu.getColumn("shifdsf").setHeader("SHIFDSF");
		egu.getColumn("shifdsf").setHidden(true);
		egu.getColumn("shifdsf").setEditor(null);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		if(disf==true){
//			��ѡ
			egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//			���ö�ѡ��
			egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		}
		egu.addPaging(25);
		

//		List l = new ArrayList();
//		l.add(new IDropDownBean(1, "ʹ��"));
//		l.add(new IDropDownBean(0, "ͣ��"));
//		egu.getColumn("zhuangt").setEditor(new ComboBox());
//		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
//				new IDropDownModel(l));
//		egu.getColumn("zhuangt").setDefaultValue("ʹ��");
//		egu.getColumn("zhuangt").setReturnId(true);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		if(disf==true){
			
			egu.addToolbarButton("���õ�����",GridButton.ButtonType_SubmitSel_condition, "ShezButton"
					, 
						"var rec=gridDiv_grid.getSelectionModel().getSelections();\n" +
						"if(rec==''){\n" + 
						"\n" + 
						"\t   document.getElementById('ShezButton').click();\n" + 
						"\t   return;\n" + 
						"}"
						, SysConstant.Btn_Icon_SelSubmit);
		}	     
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
			setExtGrid(null);
			disf = getShifdsf();
			getSelectData();
		}
	}
}
