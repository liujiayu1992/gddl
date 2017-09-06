package com.zhiren.dc.jilgl.gongl.daoy;

import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.jilgl.tiel.duih.Guohxx;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-23
 * ��������������������ʾ��ť�޸���תҳ��ˢ������
 */
public class Daoypp extends BasePage implements PageValidateListener {
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
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}
	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	
	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _SelectChick = false;
	public void SelectButton(IRequestCycle cycle) {
		_SelectChick = true;
	}
	private void Save() {
	
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			gotoDaoyppzb(cycle);
		}
		if (_SelectChick) {
			_SelectChick = false;
			gotoDaoyppmx(cycle);
		}
		
	}
	
	private void gotoDaoyppzb(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ�����ݽ�����ϸ����!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
//			System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Daoyppzb");
	}
	private void gotoDaoyppmx(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ�����ݲ鿴��ƥ�䳵����ϸ!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
//			System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Daoyppmx");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		ResultSetList rsl;
		sql = "select f.id,g.mingc gongysb_id,m.mingc meikxxb_id,p.mingc pinzb_id,c.mingc faz_id,j.mingc jihkjb_id,fahrq,daohrq,maoz,piz,jingz,biaoz,ches from fahb f,gongysb g,meikxxb m,pinzb p,chezxxb c,jihkjb j where f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id and f.faz_id=c.id and f.jihkjb_id=j.id " ;
		sql += "and f.daohrq>= " + DateUtil.FormatOracleDate(getBeginRiq()) + "\n";
		sql += "and f.daohrq<" + DateUtil.FormatOracleDate(getEndRiq()) + "+1\n";
		sql += "and yunsfsb_id=1"+ "\n";
		sql += "order by f.daohrq desc";
		rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		����gridΪ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
//		����Ϊgrid���ݲ���ҳ
		egu.addPaging(0);
//		����grid���
		egu.setWidth(Locale.Grid_DefaultWidth);
//		����grid����Ϣ
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(60);
		egu.getColumn("gongysb_id").setHeader("��Ӧ������");
		egu.getColumn("gongysb_id").setWidth(70);
		egu.getColumn("meikxxb_id").setHeader("ú������");
		egu.getColumn("meikxxb_id").setWidth(70);
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(70);
		egu.getColumn("faz_id").setHeader("��վ");
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(70);


		
//		����grid��Toolbar��ʾ���ڲ���
		egu.addTbarText("����ʱ��:");
		DateField dStart = new DateField();
		dStart.Binding("BeginRq","");
		dStart.setValue(getBeginRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText(" �� ");
		DateField dEnd = new DateField();
		dEnd.Binding("EndRq","");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());

				
//		����grid��ť
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		
		if (visit.getInt5()==0){
			egu.addToolbarItem("{text:'ƥ�Գ���',minWidth:75,handler:function (){  var grid1_history =\'\'; if(gridDiv_sm.getSelected()==null){ 	Ext.MessageBox.alert(\'��ʾ��Ϣ\',\'��ѡ��һ������ƥ�Գ���\');  return; }  grid1_rcd = gridDiv_sm.getSelected(); if(grid1_rcd.get(\'ID\') == \'0\'){ 	Ext.MessageBox.alert(\'��ʾ��Ϣ\',\'������Ȩ��֮ǰ���ȱ���!\'); return; } grid1_history = grid1_rcd.get(\'ID\'); var Cobj = document.getElementById(\'CHANGE\'); Cobj.value = grid1_history;  document.getElementById(\'SaveButton\').click();}}");
		}
		
		egu.addToolbarItem("{text:'�鿴��ƥ�䳵����ϸ',minWidth:75,handler:function (){  var grid1_history =\'\'; if(gridDiv_sm.getSelected()==null){ 	Ext.MessageBox.alert(\'��ʾ��Ϣ\',\'��ѡ��һ�������鿴��ƥ�Գ�����ϸ\');  return; }  grid1_rcd = gridDiv_sm.getSelected(); if(grid1_rcd.get(\'ID\') == \'0\'){ 	Ext.MessageBox.alert(\'��ʾ��Ϣ\',\'������Ȩ��֮ǰ���ȱ���!\'); return; } grid1_history = grid1_rcd.get(\'ID\'); var Cobj = document.getElementById(\'CHANGE\'); Cobj.value = grid1_history;  document.getElementById(\'SelectButton\').click();}}");
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
		String lx = cycle.getRequestContext().getParameter("lx");
		if(lx != null && lx.equals("edit")){
			visit.setInt5(0);
			init();
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if(lx == null){
				if(!visit.getActivePageName().toString().equals("Daoyppmx")
					&& !visit.getActivePageName().toString().equals("Daoyppzb")){
					visit.setInt5(1);
				}
				init();	
			}
			
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setBeginRiq(DateUtil.FormatDate(new Date()));
		setEndRiq(DateUtil.FormatDate(new Date()));
		setExtGrid(null);
		getSelectData();
	}
}