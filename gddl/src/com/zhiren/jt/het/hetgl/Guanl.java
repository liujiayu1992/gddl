package com.zhiren.jt.het.hetgl;

import java.sql.ResultSet;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Guanl extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	// ҳ��仯��¼
	public String getChange() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setChange(String change) {
		((Visit) this.getPage().getVisit()).setString1(change);
	}
	
	protected void initialize() {
		msg = "";
	}
	
//	 ������
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick=false;
			UpdateChep();
			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			GotoJiesszfab(cycle);
		}
		
	}
	private void UpdateChep() {
		// TODO �Զ����ɷ������
		Visit visit = (Visit) this.getPage().getVisit();
		this.Save(getChange(), visit);
	}

	private void GotoJiesszfab(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
		((Visit) this.getPage().getVisit()).setString2("GUANL");
		cycle.activate("Hetgl");
	}
	private void Save(String strchange,Visit visit) {
		JDBCcon con = new JDBCcon();
		long guanlid=Long.parseLong(visit.getString10().substring(0,visit.getString10().length()));
		String guanllx = visit.getString11();
		ResultSetList rsl = null;
		if(strchange.equals("")){
			if(guanllx.equals("1")){
				String sql="delete from meikyfhtglb where hetb_id="+guanlid;
				con.getUpdate(sql);
			}
			if(guanllx.equals("0")){
				String sql="delete from meikyfhtglb where hetys_id="+guanlid;
				con.getUpdate(sql);
			}
		}else{
			rsl = getExtGrid().getModifyResultSet(strchange);
//			rsl.beforefirst();
			long mlngDiancxxb_id=visit.getDiancxxb_id();
			
			
			StringBuffer Str_sql = null;
			if(guanllx.equals("1")){
				String sql="delete from meikyfhtglb where hetb_id="+guanlid;
				con.getUpdate(sql);
				 
				Str_sql = new StringBuffer();
				Str_sql.append("begin\n");
				while(rsl.next()){
					String id =MainGlobal.getNewID(mlngDiancxxb_id);
					String hetysid= rsl.getString("ID");
					Str_sql.append("insert into meikyfhtglb(id,diancxxb_id,hetb_id,hetys_id) values(");
					Str_sql.append(id).append(",").append(mlngDiancxxb_id).append(",").append(guanlid).append(",").append(hetysid).append(");");
				}
				Str_sql.append("End;");
			}
			if(guanllx.equals("0")){
				String sql="delete from meikyfhtglb where hetys_id="+guanlid;
				con.getUpdate(sql);

				Str_sql = new StringBuffer();
				Str_sql.append("begin\n");
				while(rsl.next()){
					String id =MainGlobal.getNewID(mlngDiancxxb_id);
					String hetbid= rsl.getString("ID");
					Str_sql.append("insert into meikyfhtglb(id,diancxxb_id,hetb_id,hetys_id) values(");
					Str_sql.append(id).append(",").append(mlngDiancxxb_id).append(",").append(hetbid).append(",").append(guanlid).append(");");
				}
				Str_sql.append("End;");
			}
			
			if(con.getUpdate(Str_sql.toString())>=0){
				
				con.commit();
				this.setMsg("����ɹ���");
			}else{
				
				con.rollBack();
				this.setMsg("����ʧ�ܣ�");
			}
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		//���ڿؼ�
//		String riqTiaoj=this.getRiqi();
//		if(riqTiaoj==null||riqTiaoj.equals("")){
//			riqTiaoj=DateUtil.FormatDate(new Date());	
//		}
		
		//������������
		String guanllxValue = ((Visit) getPage().getVisit()).getString11();
		String het_id= ((Visit) getPage().getVisit()).getString10();
//		String dat = DateUtil.FormatDate(new Date());
		ResultSetList rsl = null;
		ExtGridUtil egu = null;
		if(guanllxValue.equals("0")){
			rsl = con
			.getResultSetList("(select m.id as glb_id,h.id,h.hetbh,g.mingc as gongysmc,h.qisrq,h.guoqrq from hetb h,gongysb g,meikyfhtglb m "
					+ " where h.gongysb_id = g.id "
					+ " and h.id = m.hetb_id"
					+ " and m.hetys_id="+ het_id
					+ " and h.qisrq >= to_date('"+this.getRiq1()+"','yyyy-mm-dd')"
					+ " and h.qisrq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+this.getRiq2()+"','yyyy-mm-dd'))"
					+ " union "
					+ " (select 0 as glb_id,h.id,h.hetbh,g.mingc as gongysmc,h.qisrq,h.guoqrq from hetb h,gongysb g "
					+ " where h.gongysb_id = g.id "
//					+ " and h.id = m.hetb_id(+)"
//					+ " and (m.hetys_id <> " +het_id+ " or m.id is null)"
					+ " and h.qisrq >= to_date('"+this.getRiq1()+"','yyyy-mm-dd')"
					+ " and h.qisrq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.id not in " 
					+ " (select h.id from hetb h,gongysb g,meikyfhtglb m " 
					+ " where h.gongysb_id = g.id "
					+ " and h.id = m.hetb_id"
					+ " and m.hetys_id="+ het_id
					+ " and h.qisrq >= to_date('"+this.getRiq1()+"','yyyy-mm-dd')"
					+ " and h.qisrq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+this.getRiq2()+"','yyyy-mm-dd'))"
					+ " )");
			egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("hetgl");
			
			//������ʾ������
			egu.getColumn("id").setHidden(true);
//			egu.getColumn("t_id").setHeader("t_id");
//			egu.getColumn("t_id").setHidden(true);
			egu.getColumn("glb_id").setHeader("glb_id");
			egu.getColumn("glb_id").setHidden(true);
			egu.getColumn("hetbh").setHeader("��ͬ���");
			egu.getColumn("gongysmc").setHeader("��Ӧ������");
			egu.getColumn("qisrq").setHeader("��ʼ����");
			egu.getColumn("guoqrq").setHeader("��ֹ����");		
			 //�����п��
			 egu.getColumn("gongysmc").setWidth(200);

		}else{
			rsl = con
			.getResultSetList("(select m.id as glb_id,h.id,h.hetbh,y.mingc as chengydwmc,h.qisrq,h.guoqrq from hetys h,yunsdwb y,meikyfhtglb m " 
					+ " where h.yunsdwb_id = y.id"
					+ " and h.id = m.hetys_id"
					+ " and m.hetb_id="+ het_id
					+ " and h.qisrq >= to_date('"+this.getRiq1()+"','yyyy-mm-dd')"
					+ " and h.qisrq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+this.getRiq2()+"','yyyy-mm-dd'))"
					+ " union "
					+ " (select 0 as glb_id,h.id,h.hetbh,y.mingc as chengydwmc,h.qisrq,h.guoqrq from hetys h,yunsdwb y "
					+ " where h.yunsdwb_id = y.id"
//					+ " and h.id  = m.hetys_id(+)"
//					+ " and m.id is null"
					+ " and h.qisrq >= to_date('"+this.getRiq1()+"','yyyy-mm-dd')"
					+ " and h.qisrq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.id not in "
					+ "	(select h.id from hetys h, yunsdwb y, meikyfhtglb m "
					+ " where h.yunsdwb_id = y.id"
					+ " and h.id = m.hetys_id"
					+ " and m.hetb_id="+ het_id
					+ " and h.qisrq >= to_date('"+this.getRiq1()+"','yyyy-mm-dd')"
					+ " and h.qisrq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+this.getRiq2()+"','yyyy-mm-dd'))"
					+ " )");
			egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("hetgl");
			
			//������ʾ������
			egu.getColumn("id").setHidden(true);
//			egu.getColumn("t_id").setHeader("t_id");
//			egu.getColumn("t_id").setHidden(true);
			egu.getColumn("glb_id").setHeader("glb_id");
			egu.getColumn("glb_id").setHidden(true);
			egu.getColumn("hetbh").setHeader("��ͬ���");
			egu.getColumn("chengydwmc").setHeader("���˵�λ����");
			egu.getColumn("qisrq").setHeader("��ʼ����");
			egu.getColumn("guoqrq").setHeader("��ֹ����");	
			 //�����п��
			 egu.getColumn("chengydwmc").setWidth(200);
		}
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		
//		//���õ�ǰgrid�Ƿ�ɱ༭
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		
		String tmp="var rec=gridDiv_grid.getSelectionModel().getSelections();	\n"
				+ " if(rec.length==0){	\n"
				+ " document.getElementById('CHANGE').value='';\n"
				+ " document.getElementById('SaveButton').click();	\n"
				+ " return;}";
	
	    egu.addToolbarButton("ȷ��",GridButton.ButtonType_SubmitSel_condition, "SaveButton",tmp);
//		egu.addToolbarItem("{"+new GridButton("ȷ��","function(){ document.getElementById('SaveButton').click();" +
//		"}").getScript()+"}");
	    egu.addToolbarItem("{"+new GridButton("����","function(){ document.getElementById('ReturnButton').click();" +
		"}").getScript()+"}");
	    
	    egu.addTbarText("-");// ���÷ָ���
        
        egu.addTbarText("��ʼ����");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
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
			visit.setExtGrid1(null);//δ�˶�
			visit.setString1("");	//change
		}
		getSelectData();
	}

	
	
}