package com.zhiren.dc.xiem;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiem extends BasePage implements PageValidateListener {
	private String msg = "";
	private boolean returnId = false;
	private IPropertySelectionModel saveModel=null;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg =  MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
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
//		visit.getExtGrid1().Save(getChange(), visit);
		save1();
	}
	
	
	private void save1(){
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			//������
			String zhil ="delete from chepb " +
			"where id="+rsl.getString("id")+";\n";
			sb.append(zhil.toString());
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		
		while (rsl.next()) {
			if ("0".equals(rsl.getString("id"))) {
				//������ʱ��
				String id = MainGlobal.getNewID(visit.getDiancxxb_id());
				sb.append("insert into chepb(id,MAOZ,BANZ,QINGCSJ,ZHONGCSJ,HEDBZ,lursj,lury,fahb_id) values(");
				sb.append(id).append(",").append(rsl.getDouble("maoz")).append(",");
				sb.append(rsl.getLong("banz")).append(",to_date('").append(rsl.getString("qingcsj")).append("','yyyy-mm-dd'),");
				sb.append("to_date('").append(rsl.getString("qingcsj")).append("','yyyy-mm-dd'),");
				sb.append(rsl.getLong("hedbz")).append(",sysdate,'").append(visit.getRenymc()).append("',");
				sb.append(getLeixSelectValue().getId()).append(");\n");
				
			}else{
				sb.append("update chepb set maoz=");
				sb.append(rsl.getDouble("maoz")).append(",banz=").append(rsl.getDouble("banz"));
				sb.append(",QINGCSJ=").append("to_date('").append(rsl.getString("qingcsj")).append("','yyyy-mm-dd')");
				sb.append(",ZHONGCSJ=").append("to_date('").append(rsl.getString("qingcsj")).append("','yyyy-mm-dd')");
				sb.append(",fahb_id=").append(getLeixSelectValue().getId());
				sb.append(" where id=");
				sb.append(rsl.getString("id")).append(";\n");
				
			}
			
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
		
		con.commit();
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
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
//	 ������
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getRiq2() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}

	public void getSelectData() {
//		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql=
			"select c.id,c.maoz,c.banz,c.qingcsj,c.hedbz\n" + //to_char(g.guohsj,'YYYY-MM-DD')guohsj,
			" from chepb c \n" + 
			" where c.fahb_id="+getLeixSelectValue().getId()+" ";
		
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("chepb");
//		������
		egu.getColumn("id").hidden=true;
		// /������ʾ������
		egu.getColumn("qingcsj").setHeader("жú����");
		egu.getColumn("banz").setHeader("жú��ֵ");
		egu.getColumn("maoz").setHeader("жú����(��)");
		egu.getColumn("hedbz").setHeader("״̬");
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		
		
		// /��̬������
		egu.addTbarText("�벴����:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("�˵���Ϣ:");
		
//		ComboBox cb = new ComboBox();
//		cb.setTransform("YUNDDropDown");
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		cb.setId("YUND");
//		cb.setWidth(350);
//		egu.addToolbarItem(cb.getScript());
		
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("YUNDDropDown");
		comb4.setId("YUND");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(350);
		comb4.setListWidth(350);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
//		egu.addOtherScript("YUND.on('select',function(){document.forms[0].submit();});");
		
		egu.addTbarText("-");

		// /���ð�ť
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
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
			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			
//			getLeixSelectValue();
			setLeixSelectValue(null);
			setLeixSelectModel(null);
			getLeixSelectModels();
			
		}
		getSelectData();
	}
	
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setLeixSelectValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setLeixSelectModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getLeixSelectModels() {
		
		String beginriq=getRiqi();
		String endriq=getRiq2();
		
		String sql =
			"select fahb.id,\n" +
			"       ('�ۿ�����:'||luncxxb.mingc ||\n" + 
			"\t\t       ' ��Ӧ������:'||gongysb.mingc ||\n" + 
			"\t\t       ' ú������:'||meikxxb.mingc ||\n" + 
			"\t\t       ' ���䷽ʽ:'||yunsfsb.mingc ||\n" + 
			"\t\t       ' ����:'||fahb.chec||\n" + 
			"       ' ������:'||zhilb.huaybh||\n" + 
			"\t\t       ' ë��:'||fahb.maoz||\n" + 
			"       ' Ƥ��:'||fahb.piz||\n" + 
			"       ' ����:'||fahb.biaoz) as beiz\n" + 
			"\t\t  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb\n" + 
			"\t\t where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+)\n" + 
			"\t\t   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+)\n" + 
			"\t\t   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)\n" + 
			"\t\t   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)\n" + 
			"\t\t   and yunsfsb.mingc='����' and fahb.jiesb_id=0\n" + 
			"\t\t   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+beginriq+"'\n" + 
			"\t\t   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'\n" + 
			"\t\torder by fahb.daohrq";
		
		setLeixSelectModel(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	
}
