package com.zhiren.dc.huaygl.huayysjlcl;

import java.util.Date;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Judge;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*����:���ܱ�
 *����:2010-8-3 16:44:08
 *�޸�����:�����ſɱ༭,�������Զ�ˢ��
 * 
 */



/*
 * ����:tzf
 * ʱ��:2010-03-10
 * �޸�����:�ҷ� ȫˮ��ָ�����  �п���zidbm�ֶδ��ڻس����з� ���ڽ����
 */
public class Huayysjlsh extends BasePage implements PageValidateListener {
	public static final String Qbad = "Qbad";
	public static final String Stad = "Stad";
	public static final String Mt = "Mt";
	public static final String Mad = "Mad";
	public static final String Aad = "Aad";
	public static final String Vad = "Vad";
	
	public static final String RL = "RL";
	public static final String DC = "DC";
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg(null);
	}
	
	public String getLeix() {
		return ((Visit)this.getPage().getVisit()).getString10();
	}
	public void setLeix(String leix) {
		((Visit)this.getPage().getVisit()).setString10(leix);
	}
	
	public String getHuaylx() {
		return ((Visit)this.getPage().getVisit()).getString12();
	}
	public void setHuaylx(String leix) {
		((Visit)this.getPage().getVisit()).setString12(leix);
	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _BlankOutChick = false;

	public void BlankOutButton(IRequestCycle cycle) {
		_BlankOutChick = true;
	}
	
	private boolean _ResetChick = false;

	public void ResetButton(IRequestCycle cycle) {
		_ResetChick = true;
	}

	private boolean _FixChick = false;

	public void FixButton(IRequestCycle cycle) {
		_FixChick = true;
	}

	private boolean _AutoFixChick = false;

	public void AutoFixButton(IRequestCycle cycle) {
		_AutoFixChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_BlankOutChick) {
			_BlankOutChick = false;
			BlankOut();
		}
		if (_ResetChick) {
			_ResetChick = false;
			Reset();
		}
		if (_FixChick) {
			_FixChick = false;
			Fix();
		}
		if (_AutoFixChick) {
			_AutoFixChick = false;
			AutoFix();
		}
	}
	private int getTtype(){
		int tType=0;
		if(Qbad.equals(getLeix())){
			tType = Judge.T_TYPE_Qbad;
		}else if(Stad.equals(getLeix())){
			tType = Judge.T_TYPE_Stad;
		}else if(Mt.equals(getLeix())){
			tType = Judge.T_TYPE_Mt;
		}else if(Mad.equals(getLeix())){
			tType = Judge.T_TYPE_Mad;
		}else if(Aad.equals(getLeix())){
			tType = Judge.T_TYPE_Aad;
		}else if(Vad.equals(getLeix())){
			tType = Judge.T_TYPE_Vad;
		}
		return tType;
	}
	private String getResultName(){
		String resultName = "";
		if(Qbad.equals(getLeix())){
			resultName = "farl";
		}else if(Stad.equals(getLeix())){
			resultName = "liuf";
		}else {
			resultName = "huayz";
		}
		return resultName;
	}
	private String getTableName(){
		String tableName = "";
		if(Qbad.equals(getLeix())){
			tableName = "huayrlb";
		}else if(Stad.equals(getLeix())){
			tableName = "huaylfb";
		}else {
			tableName = "huaygyfxb";
		}
		return tableName;
	}
	private String getFenxxmCond(){
		String cond = " and i.bianm='" + getLeix() + "'" ;
		return cond;
	}
	private String getDataSql(){
		String sql = "";
		if(Qbad.equals(getLeix())){
			sql = "select h.id,h.bianm,h.yangdbh,replace( replace(h.zidbm,chr(13),'') ,chr(10),'') zidbm,h.farl,h.rerl,h.wens,h.dianhr,\n"
				+ "decode(h.shenhzt,-1,'����',0,'δ���',1,'�ѽ���','�����') shenhzt\n"
				+ "from huayrlb h, item i, itemsort s\n"
				+ "where h.fenxxmb_id = i.id and i.itemsortid = s.id\n" 
				+ "and s.mingc = '����ԭʼ��Ŀ' " + getFenxxmCond() + "\n"
				+ "and h.bianm = '"
				+ getHuaybhValue().getValue()
				+ "'"
				+ " and h.diancxxb_id = "
				+ getTreeid() + " \n" 
				+"  and h.riq>=to_date('"+this.getRiqi()+"','yyyy-mm-dd')-7  \n"
				+"  and h.riq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd')+7";
				//���ܱ���2010-9-4   �����������ƣ���ֹ����ظ���-7��+7��Ϊ�˴�������
				//		" order by cis";
		}else if(Stad.equals(getLeix())){
			sql = "select h.id, h.bianm, replace( replace(h.zidbm,chr(13),'') ,chr(10),'') zidbm, h.liuf, h.meiyzl, h.shebbh, h.shebxh,\n" +
				"decode(h.shenhzt,-1,'����',0,'δ���',1,'�ѽ���','�����') shenhzt\n" 
				+ "from huaylfb h, item i, itemsort s\n"
				+ "where h.fenxxmb_id = i.id and i.itemsortid = s.id\n" 
				+ "and s.mingc = '����ԭʼ��Ŀ' " + getFenxxmCond() + "\n"
				+ "and h.bianm = '"
				+ getHuaybhValue().getValue()
				+ "'"
				+ " and h.diancxxb_id = "
				+ getTreeid() + " \n" 
				+"  and h.riq>=to_date('"+this.getRiqi()+"','yyyy-mm-dd')-7  \n"
				+"  and h.riq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd')+7";
				//���ܱ���2010-9-4   �����������ƣ���ֹ����ظ���-7��+7��Ϊ�˴�������
				//+	"order by cis";
		}else {
			sql = "select h.id, h.bianm, replace( replace(h.zidbm,chr(13),'') ,chr(10),'') zidbm, h.qimbh, h.meiyzl, h.huayz,\n" +
			"decode(h.shenhzt,-1,'����',0,'δ���',1,'�ѽ���','�����') shenhzt\n" 
			+ "from huaygyfxb h, item i, itemsort s\n"
			+ "where h.fenxxmb_id = i.id and i.itemsortid = s.id\n" 
			+ "and s.mingc = '����ԭʼ��Ŀ' " + getFenxxmCond() + "\n"
			+ "and h.bianm = '"
			+ getHuaybhValue().getValue()
			+ "'"
			+ " and h.diancxxb_id = "
			+ getTreeid() + " \n" 
			+"  and h.riq>=to_date('"+this.getRiqi()+"','yyyy-mm-dd')-7  \n"
			+"  and h.riq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd')+7";
			//���ܱ���2010-9-4   �����������ƣ���ֹ����ظ���-7��+7��Ϊ�˴�������
			//+" order by cis";
		}
		return sql;
	}
	
	private void setColumnHeader(ExtGridUtil egu){
		if(Qbad.equals(getLeix())){
			egu.getColumn("bianm").setHeader("����");
			egu.getColumn("yangdbh").setHeader("�������");
			egu.getColumn("zidbm").setHeader("�Զ�����");
			egu.getColumn("farl").setHeader("������");
			egu.getColumn("rerl").setHeader("������");
			egu.getColumn("wens").setHeader("����");
			egu.getColumn("dianhr").setHeader("�����");
			egu.getColumn("shenhzt").setHeader("״̬");
		}else if(Stad.equals(getLeix())){
			egu.getColumn("bianm").setHeader("����");
			egu.getColumn("zidbm").setHeader("�Զ�����");
			egu.getColumn("liuf").setHeader("���");
			egu.getColumn("meiyzl").setHeader("ú������");
			egu.getColumn("shebbh").setHeader("�豸���");
			egu.getColumn("shebxh").setHeader("�豸�ͺ�");
			egu.getColumn("shenhzt").setHeader("״̬");
		}else{
			if(Mt.equals(getLeix())){
				egu.getColumn("huayz").setHeader("ȫˮ(Mt)");
			}else if(Mad.equals(getLeix())){
				egu.getColumn("huayz").setHeader("�ոɻ�ˮ(Mad)");
			}else if(Aad.equals(getLeix())){
				egu.getColumn("huayz").setHeader("�ոɻ���(Aad)");
			}else if(Vad.equals(getLeix())){
				egu.getColumn("huayz").setHeader("�ոɻ��ӷ���(Vad)");
			}
			egu.getColumn("bianm").setHeader("����");
			egu.getColumn("zidbm").setHeader("�Զ�����");
			egu.getColumn("qimbh").setHeader("������");
			egu.getColumn("meiyzl").setHeader("ú������");
			egu.getColumn("shenhzt").setHeader("״̬");
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = getDataSql();

		ResultSetList rsl = con.getResultSetList(sql);
		boolean isEnd = false;
		while(rsl.next()){
			if("�ѽ���".equals(rsl.getString("shenhzt"))){
				isEnd = true;
			}
		}
		if(isEnd){
			rsl.beforefirst();
			while(rsl.next()){
				rsl.setString(rsl.findCol("shenhzt"), "�ѽ���");
			}
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		setColumnHeader(egu);
		
		egu.addTbarText("��������:");
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
		df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("������");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("HuaybhDropDown");
		comb4.setId("Huaybh");
		comb4.setEditable(true);
		comb4.setListeners("select:function(){document.forms[0].submit();}");
		
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(100);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());

		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);

		egu.addTbarText("-");
		egu.addToolbarButton("���", GridButton.ButtonType_Sel, "FixButton",
				null, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarText("-");
		egu.addToolbarButton("�Զ����", GridButton.ButtonType_SaveAll,
				"AutoFixButton", null, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarText("-");
		egu.addToolbarButton("����", GridButton.ButtonType_Sel,
				"BlankOutButton", null, SysConstant.Btn_Icon_Delete);
		egu.addTbarText("-");
		egu.addToolbarButton("��ԭ", GridButton.ButtonType_Sel,
				"ResetButton", null, SysConstant.Btn_Icon_Return);
		setExtGrid(egu);

		con.Close();
	}
	
	private int UpdateZt(JDBCcon con, String tableName, String id, int zt){
		String sql = "update "+tableName+" set shenhzt = "+zt+" where id =" + id;
		return con.getUpdate(sql);
	}
	private int SetSelZhuangt(int zt){
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		while(rs.next()){
			if("�ѽ���".equals(rs.getString("shenhzt"))){
				setMsg("�ѽ������ݲ��ܽ�����ˣ�");
				return -1;
			}
			UpdateZt(con,getTableName(),rs.getString("id"),zt);
		}
		rs.close();
		con.Close();
		return 0;
	}
	
	private void BlankOut(){
		SetSelZhuangt(-1);
	}
	private void Reset(){
		SetSelZhuangt(0);
	}
	private void Fix(){
		SetSelZhuangt(2);
	}
	private void AutoFix(){
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		double[] array = new double[rs.getRows()];
		while(rs.next()){
			if("�ѽ���".equals(rs.getString("shenhzt"))){
				setMsg("�ѽ������ݲ��ܽ�����ˣ�");
				return;
			}
			array[rs.getRow()] = rs.getDouble(getResultName());
		}
		Judge ju = new Judge();
		double[] dbarrjudge = ju.getJudgeData(getTtype(), array);
		for(int i = 0; i < dbarrjudge.length ; i++){
			rs.beforefirst();
			while(rs.next()){
				if(dbarrjudge[i] == rs.getDouble(getResultName())){
					UpdateZt(con,getTableName(),rs.getString("id"),2);
					rs.Remove(rs.getRow());
					break;
				}
			}
		}
		rs.beforefirst();
		while(rs.next()){
			UpdateZt(con,getTableName(),rs.getString("id"),-1);
		}
		rs.close();
		con.commit();
		con.Close();
	}

	// �������

	public IDropDownBean getHuaybhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			setHuaybhValue((IDropDownBean) getHuaybhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setHuaybhValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}

	public void setHuaybhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getHuaybhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getHuaybhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getHuaybhModels() {
		String bianm = "";
		if(DC.equals(getHuaylx().toUpperCase())){
			bianm = "and substr(h.bianm,0,2) != 'RL' \n";
		} else if(RL.equals(getHuaylx().toUpperCase())){
			bianm = "and substr(h.bianm,0,2) = 'RL' \n";
		}
		String sql = "select rownum, bianm from (select distinct h.bianm from " +
				getTableName()+ " h, item i, itemsort s\n" + 
				"where h.fenxxmb_id = i.id and i.itemsortid = s.id\n" +  
				"and s.mingc = '����ԭʼ��Ŀ' " + getFenxxmCond() + "\n" +
				"and h.riq >= " +DateUtil.FormatOracleDate(getRiqi()) + "\n" + 
				"and h.riq < " + DateUtil.FormatOracleDate(getRiq2()) + "+1\n" +
				bianm +
				"and h.diancxxb_id = " + getTreeid() + ") order by bianm";
		setHuaybhModel(new IDropDownModel(sql, "��ѡ��"));
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

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
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
		String hylx = cycle.getRequestContext().getParameter("hylx");

		
		if(lx != null) {
			setLeix(lx);
//			init();
		}
		if(hylx != null) {
			setHuaylx(hylx);
//			init();
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString9(lx);
			visit.setString15(hylx);
			if(lx == null){
				setLeix(Qbad);
			}
			if(hylx == null){
				setHuaylx(DC);
			}
			init();
		}
		if(lx != null && !lx.equals(null)){
				if(!visit.getString9().equals(lx)){
					setRiqi(DateUtil.FormatDate(new Date()));
					setRiq2(DateUtil.FormatDate(new Date()));
					((Visit)getPage().getVisit()).setProSelectionModel10(null);
					visit.setString9(lx);
				}
		}
		if(hylx != null && !hylx.equals(null)){
			
					if(!visit.getString15().equals(hylx)){
						setRiqi(DateUtil.FormatDate(new Date()));
						setRiq2(DateUtil.FormatDate(new Date()));
						((Visit)getPage().getVisit()).setProSelectionModel10(null);
						visit.setString15(hylx);
					}
		}
		if (riqichange) {
			riqichange = false;
			getHuaybhModels();
		}
		if (riq2change) {
			riq2change = false;
			getHuaybhModels();
		}
		
		getSelectData();
	}
	
	private void init() {
		Visit visit = (Visit) getPage().getVisit();
		getHuaybhValue();
		setHuaybhValue(null);
		getHuaybhModels();
		
		setRiqi(DateUtil.FormatDate(new Date()));
		setRiq2(DateUtil.FormatDate(new Date()));
		setTreeid(String.valueOf(visit.getDiancxxb_id()));
	}

}