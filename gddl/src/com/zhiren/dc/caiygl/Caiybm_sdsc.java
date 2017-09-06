package com.zhiren.dc.caiygl;

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

/*
 * ���ߣ�����
 * ʱ�䣺2009-08-03 10��45
 * �������޸ĳ��ŵ��п� ���ӳ��ſ�ѡ������ʾ����ĳ���
 */
/**
 * 
 * @author Rock
 * @version v1.1.2.1
 * @since 2009-07-31
 * @description �ֶ����������ɾ��
 */

public class Caiybm_sdsc extends BasePage implements PageValidateListener {
	/* ��Դ���ò��� ���䷽ʽ��ȫ����*/
	private static final String YUNSFS_ALL = "ALL";
	/* ��Դ���ò��� ���䷽ʽ����·��*/
	private static final String YUNSFS_HY = "HY";
	/* ��Դ���ò��� ���䷽ʽ����·��*/
	private static final String YUNSFS_QY = "QY";
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean riqchange;
//	 ҳ��ˢ�����ڣ�жú���ڣ�
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq !=null){
			if(!this.riq.equals(riq)){
				riqchange = true;
			}
		}
		this.riq = riq;
	}
//	 ҳ��ˢ�����ڣ�жú���ڣ�
	private String riqe;

	public String getRiqe() {
		return riqe;
	}
	public void setRiqe(String riqe) {
		if(this.riqe !=null){
			if(!this.riqe.equals(riqe)){
				riqchange = true;
			}
		}
		this.riqe = riqe;
	}
//	��������������(����������)
	public IDropDownBean getRiqsetValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getRiqsetModel().getOptionCount() > 0) {
				setRiqsetValue((IDropDownBean) getRiqsetModel().getOption(0));
			}else{
				setRiqsetValue(new IDropDownBean());
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setRiqsetValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getRiqsetModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setRiqsetModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setRiqsetModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setRiqsetModels() {
		List riqlist = new ArrayList();
		riqlist.add(new IDropDownBean(1,"����"));
		riqlist.add(new IDropDownBean(2,"����"));
		setRiqsetModel(new IDropDownModel(riqlist));
	}
//	 ú��������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}else{
				setChangbValue(new IDropDownBean());
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		String sql = "select distinct m.id,m.mingc from meikxxb m,fahb f\n" +
		"where f.meikxxb_id = m.id "+getStrWhere("meik");
		setChangbModel(new IDropDownModel(sql,"ȫ��"));
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

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_DeleteClick) {
			_DeleteClick = false;
			Save();
			init();
		}
	}
	/**
	 * 
	 * @param mod	����SQL�ĵص�
	 * @return		����SQL��where��������
	 * @description	���mod=meik�򲻼���ú��ID����
	 */
	private String getStrWhere(String mod){
		Visit visit = (Visit) getPage().getVisit();
		String where = " and f.zhilb_id != 0 \n";
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		if(YUNSFS_HY.equals(visit.getString1())){
			where += " and f.yunsfsb_id = " + SysConstant.YUNSFS_HUOY + "\n";
		}else if(YUNSFS_QY.equals(visit.getString1())){
			where += " and f.yunsfsb_id = " + SysConstant.YUNSFS_QIY + "\n";
		}
		if(!"meik".equalsIgnoreCase(mod)){
			if(getChangbValue()!=null && getChangbValue().getId() !=-1){
				where += " and f.meikxxb_id = " + getChangbValue().getId();
			}
		}
		String rq = "daohrq";
		if(getRiqsetValue()!=null){
			if(getRiqsetValue().getId() == 1){
				rq = "daohrq";
			}else{
				rq = "fahrq";
			}
		}
		where += " and f."+rq+" >= " + strxmrqOra 
		+"\n and f."+rq+" < " + strxmrqeOra + " +1 \n";
		where += " and f.diancxxb_id =" + visit.getDiancxxb_id() + "\n";
		return where;
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
//		 жú���ڵ�ora�ַ�����ʽ
		JDBCcon con = new JDBCcon();
		String sql = 
			"select f.zhilb_id id, zm.bianm, max(m.mingc) meik, max(z.mingc ) faz,\n" +
			"max(h.hetbh) heth, max(to_char(cp.zhongcsj,'yyyy-mm-dd hh24:mi:ss')) guohsj,\n " +
			"replace(getcaiycheph(f.zhilb_id),'<br>','') cheh\n" + 
			"       from caiyb y, fahb f, meikxxb m, chezxxb z, hetb h,\n" + 
			"       zhuanmb zm, zhuanmlb l, zhillsb zl, chepb cp\n" + 
			"where y.zhilb_id = f.zhilb_id and f.meikxxb_id = m.id and f.id = cp.fahb_id\n" + 
			"and f.faz_id = z.id and f.hetb_id = h.id(+) and zm.zhillsb_id = zl.id\n" + 
			"and f.zhilb_id = zl.zhilb_id and l.jib = 1 and zm.zhuanmlb_id = l.id\n" + 
			getStrWhere("") + "group by f.zhilb_id, zm.bianm";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// �½�grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		����grid����ҳ
		egu.addPaging(0);
//		����grid������
		// ����grid�б���
		egu.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("meik").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader(Locale.faz_id_fahb);
		egu.getColumn("heth").setHeader(Locale.hetb_id_fahb);
		egu.getColumn("guohsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("cheh").setHeader(Locale.cheph_chepb);
//		�����п��
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("meik").setWidth(120);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("heth").setWidth(100);
		egu.getColumn("guohsj").setWidth(120);
		egu.getColumn("cheh").setWidth(400);
//		�趨grid�пɷ�༭
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("meik").setEditor(null);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("heth").setEditor(null);
		egu.getColumn("guohsj").setEditor(null);
//		egu.getColumn("cheh").setEditor(null);
		
		egu.getColumn("faz").setHidden(YUNSFS_QY.equals(visit.getString1()));
		// ����������������
		
		ComboBox riqset = new ComboBox();
		riqset.setTransform("RiqsetSelect");
		riqset.setWidth(60);
		riqset
				.setListeners("select:function(own,rec,index){Ext.getDom('RiqsetSelect').selectedIndex=index}");
		egu.addToolbarItem(riqset.getScript());
//		 ��ж����ѡ��
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		 ��ж����ѡ��
		egu.addTbarText("��");
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		
		
		egu.addTbarText("ú��:");
		ComboBox changbcb = new ComboBox();
		changbcb.setTransform("ChangbSelect");
		changbcb.setWidth(130);
		changbcb
				.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		egu.addToolbarItem(changbcb.getScript());

//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		
		egu.addToolbarButton("ɾ��",GridButton.ButtonType_SubmitSel, "DeleteButton");
		setExtGrid(egu);
		con.Close();

	}
	
	private void Save(){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û��δ�ύ�Ķ���");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			String sql = "update fahb set zhilb_id = 0 where zhilb_id =" + rsl.getString("id");
			con.getUpdate(sql);
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	private void init() {
		setChangbModels();
		setExtGrid(null);
		initGrid();
	} 

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String yunsfs = cycle.getRequestContext().getParameter("lx");
		if (yunsfs != null) {
			visit.setString1(yunsfs);
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqe(DateUtil.FormatDate(new Date()));
			setRiqsetModels();
			riqchange = true;
		}
//		String MOD = cycle.getRequestContext().getParameter("mod");
//		if (MOD != null) {
//			visit.setString2(MOD);
//			setRiq(DateUtil.FormatDate(new Date()));
//			setRiqe(DateUtil.FormatDate(new Date()));
//			setRiqsetModels();
//			riqchange = true;
//		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			if (yunsfs == null) {
				visit.setString1(YUNSFS_ALL);
			}
//			if (MOD == null) {
//				visit.setString2(MOD_SC);
//			}
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqe(DateUtil.FormatDate(new Date()));
			setRiqsetModels();
			riqchange = true;
		}
		if(riqchange){
			riqchange = false;
			init();
		}
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
}