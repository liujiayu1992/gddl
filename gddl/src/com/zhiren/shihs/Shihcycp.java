package com.zhiren.shihs;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihcycp extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	//�󶨿�ʼ����
	private boolean datechange;
	private String startRiq;
	public String getStartRiq() {
		return startRiq;
	}
	public void setStartRiq(String riq) {
		if(riq != null && startRiq != null 
				&& !riq.equalsIgnoreCase(startRiq)){
			datechange = true;
		}
		this.startRiq = riq;
	}
	//�󶨽�������
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		if(riq != null && endRiq != null 
				&& !riq.equalsIgnoreCase(endRiq)){
			datechange = true;
		}
		this.endRiq = riq;
	}
	
	// ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
//	�������������
	public IDropDownBean getCaiybmValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean1() == null) {
            if(getCaiybmModel().getOptionCount() > 0) {
	        	Object obj = getCaiybmModel().getOption(0);
	            setCaiybmValue((IDropDownBean) obj);
            }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	
	public void setCaiybmValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(Value);
	}
	
	public IPropertySelectionModel getCaiybmModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel1() == null) {
	        getCaiybmModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	
	public void setCaiybmModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(_value);
    }
	
	public IDropDownBean getCaiybmValue1() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean2() == null) {
            if(getCaiybmModel().getOptionCount() > 0) {
	        	Object obj = getCaiybmModel().getOption(0);
	            setCaiybmValue1((IDropDownBean) obj);
            }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	
	public void setCaiybmValue1(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(Value);
	}
	
//	public IPropertySelectionModel getCaiybmModel1() {
//	    if (((Visit)this.getPage().getVisit()).getProSelectionModel2() == null) {
//	        getCaiybmModels();
//	    }
//	    return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
//	}
//	
//	public void setCaiybmModel1(IPropertySelectionModel _value) {
//		((Visit)this.getPage().getVisit()).setProSelectionModel2(_value);
//    }

    public void getCaiybmModels() {
    	List l = new ArrayList();
    	l.add(new IDropDownBean(0,"δ����"));
        setCaiybmModel(new IDropDownModel(l,"select id,bianm from shihcyb where caiysj >= "
    		+DateUtil.FormatOracleDate(getStartRiq())+" and caiysj < " 
    		+DateUtil.FormatOracleDate(getEndRiq())+"+1"));
    }
	
	private void Save(){
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,�޸ļ�¼Ϊ�գ�");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		String sql = "begin\n";
		while(rsl.next()){
			int id = rsl.getInt("id");
			sql += "update shihcpb set shihcyb_id = " + getCaiybmValue1().getId()
			+" where id = " + id + " and diancxxb_id="+v.getDiancxxb_id() + ";\n ";
		}
		if(rsl.getRows()>0){
			sql += "end;\n";
			con.getUpdate(sql);
			setMsg("ȷ�ϲ����ɹ�!");
		}
		rsl.close();
		con.Close();
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
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(datechange){
			setCaiybmValue(null);
			setCaiybmModel(null);
			getCaiybmModels();
			datechange = false;
			getSelectData();
		}
	}
	
	private void CreateEGU(){
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select  c.id, g.mingc gongysb_id, p.mingc shihpzb_id, c.cheph, \n")
		.append("c.daohrq, c.maoz, c.piz \n")
		.append("       from shihcpb c,shihgysb g,shihpzb p \n")
		.append("where shihcyb_id = ").append(getCaiybmValue().getId())
		.append("\n and c.gongysb_id = g.id and c.shihpzb_id = p.id ");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
//		egu.setHeight("bodyHeight");
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("shihpzb_id").setHeader("Ʒ��");
		egu.getColumn("shihpzb_id").setWidth(100);
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(80);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(80);
		
		egu.addTbarText("����:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ","forms[0]");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("��");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ","forms[0]");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("��������:");
		ComboBox cbm = new ComboBox();
		cbm.setWidth(100);
		cbm.setTransform("Caiybm");
		cbm.setId("Caiybm");//���Զ�ˢ�°�
		cbm.setLazyRender(true);//��̬��
		cbm.setEditable(false);
		egu.addToolbarItem(cbm.getScript());
		
		GridButton gRefresh = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton("ȷ����:",GridButton.ButtonType_SubmitSel, "SaveButton");
		
		ComboBox cbm1 = new ComboBox();
		cbm1.setWidth(100);
		cbm1.setTransform("Caiybm1");
		cbm1.setId("Caiybm1");//���Զ�ˢ�°�
		cbm1.setLazyRender(true);//��̬��
		cbm1.setEditable(false);
		egu.addToolbarItem(cbm1.getScript());
		
		
		setExtGrid(egu);
	}

	public void getSelectData() {
		CreateEGU();
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
		if (getExtGrid() == null) {
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
			init();
		}
	}
	private void init() {
		setStartRiq(DateUtil.FormatDate(new Date()));
		setEndRiq(DateUtil.FormatDate(new Date()));
		setCaiybmValue(null);
		setCaiybmModel(null);
		getSelectData();
	}
}
