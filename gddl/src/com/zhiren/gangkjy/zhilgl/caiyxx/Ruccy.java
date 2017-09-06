package com.zhiren.gangkjy.zhilgl.caiyxx;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruccy extends BasePage implements PageValidateListener {
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

	// ҳ��ˢ�����ڣ�жú���ڣ�
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	 ҳ��ˢ�����ڣ�жú���ڣ�
	private String riqe;

	public String getRiqe() {
		return riqe;
	}

	public void setRiqe(String riqe) {
		this.riqe = riqe;
	}

//	��糧���й�
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String yangpbh;
	public String getYangpbh(){
		return yangpbh;
	}
	public void setYangpbh(String _yangpbh){
		yangpbh = _yangpbh;
	}
	private double yangpzl;
	public double getYangpzl(){
		return yangpzl;
	}
	public void setYangpzl(double _yangpzl){
		yangpzl = _yangpzl;
	}
	private String yangpfs;
	public String getYangpfs(){
		return yangpfs;
	}
	public void setYangpfs(String _yangpfs){
		yangpfs = _yangpfs;
	}
	private String jieyr;
	public String getJieyr(){
		return jieyr;
	}
	public void setJieyr(String _jieyr){
		jieyr = _jieyr;
	}
	private String beiz;
	public String getBeiz(){
		return beiz;
	}
	public void setBeiz(String _beiz){
		beiz = _beiz;
	}
	private String jydw;
	public String getJianydw(){
		return jydw;
	}
	public void setJianydw(String _jydw){
		jydw = _jydw;
	}
	
//	������λ����Դ 
	IDropDownBean songjdw;
	public IDropDownBean getSongjdw(){
		if(songjdw == null){
			if(getSongjdwModel() != null){
				setSongjdw((IDropDownBean)getSongjdwModel().getOption(0));
			}else{
				songjdw = new IDropDownBean();
			}
		}
		return songjdw;
	}
	public void setSongjdw(IDropDownBean _songjdw){
		songjdw = _songjdw;
	}
	IPropertySelectionModel songjdwmodel;
	public IPropertySelectionModel getSongjdwModel(){
		if(songjdwmodel == null){
			setSongjdwModelData();
		} 
		return songjdwmodel;
	}
	public void setSongjdwModel(IPropertySelectionModel _songjdwmodel){
		songjdwmodel = _songjdwmodel;
	}
	public void setSongjdwModelData(){
		String sql = 
			"select t.id,t.mingc from item t, itemsort s\n" +
			"where t.itemsortid = s.id and s.mingc = '�������鵥λ'";
		setSongjdwModel(new IDropDownModel(sql));
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
	
	private boolean _UpdateClick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateClick = true;
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if(_UpdateClick){
			_UpdateClick = false;
			((Visit) getPage().getVisit()).setString7(getRiq());
			((Visit) getPage().getVisit()).setString8(getRiqe());
			cycle.activate("Ruccyxg");
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		// жú���ڵ�ora�ַ�����ʽ
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		// �糧ID
//		long dcid = visit.getDiancxxb_id();
		String dcid=this.getTreeid();
		String sql = "select f.id,\n   g.mingc fahr,\n"
				+ "       p.mingc pinz,\n   c.mingc faz,\n"
				+ "       f.chec,\n    f.ches,\n   f.biaoz,\n"
				+ "       f.maoz,\n    f.piz,\n f.jingz,\n f.yingk,\n    f.fahrq,\n"
				+ "       to_char(f.daohrq,'yyyy-mm-dd hh24:mi:ss') jiexrq,\n"
				+ "       to_char(f.guohsj,'yyyy-mm-dd hh24:mi:ss') guohrq\n"
				+ "  from fahb f, vwfahr g, vwpinz p, vwchez c\n"
				+ " where f.gongysb_id = g.id\n" + "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id = c.id\n" + " and f.zhilb_id=0	";
//				if(!this.getTreeid().equals("199")){
			        sql+="and f.diancxxb_id="+this.getTreeid()+"\n";
//		             }  
//				+"and f.diancxxb_id = " + dcid
				sql+= "   and f.daohrq >= " + strxmrqOra + " and f.daohrq < "
				+ strxmrqeOra + "+1";
	

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// �½�grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// ����grid�ɱ༭
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		//���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// �������ݲ���ҳ
		egu.addPaging(0);
		// ����grid�б���
		egu.getColumn("fahr").setHeader(Local.fahr);
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("faz").setHeader(Local.faz);
		egu.getColumn("chec").setHeader(Local.chec);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("maoz").setHeader(Local.maoz);
		egu.getColumn("piz").setHeader(Local.piz);
		egu.getColumn("jingz").setHeader(Local.jingz);
		egu.getColumn("yingk").setHeader(Local.yingk);
		egu.getColumn("fahrq").setHeader(Local.fahrq);
		egu.getColumn("jiexrq").setHeader(Local.jiexrq);
		egu.getColumn("guohrq").setHeader(Local.guohrq);
		// ����grid�п��
		egu.getColumn("fahr").setWidth(100);
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("jiexrq").setWidth(110);
		egu.getColumn("guohrq").setWidth(110);

		egu.getColumn("maoz").setHidden(true);
		egu.getColumn("piz").setHidden(true);
		// ��ж����ѡ��
		egu.addTbarText(Local.jiexrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		 ��ж����ѡ��
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		//���õ糧��
		egu.addTbarText("-");
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		
	
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		// ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		// ��ϸ���ⰴť
		GridButton Create = new GridButton("����", "Create");
		Create.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(Create);
		// ��ϸ���ⰴť
		GridButton Update = new GridButton("�޸�", "Update");
		Update.setIcon(SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(Update);

		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û��δ�ύ�Ķ���");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql;
		int flag;
		String zhilbid = MainGlobal.getNewID(visit.getDiancxxb_id());
		String caiybid = MainGlobal.getNewID(visit.getDiancxxb_id());
		String caiylbid = getCaiylbid();
		String songjdwid = getJianydw();
		sql = "update fahb set zhilb_id=" + zhilbid + " where id in(" +
		getChange() + ")";
		flag = con.getUpdate(sql);
		if(flag == -1){
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.UpdateDatabaseFail);
			con.rollBack();
			return;
		}
		sql = "insert into caiyb(id,zhilb_id,caiylbb_id,xuh,bianm,caiyrq,yangplb," +
		"yangpzl,songjdwb_id,jieyr,lury,beiz) values(" + caiybid + "," + zhilbid +
		"," + caiylbid + ",0,'" + getYangpbh() + "',sysdate,'" + getYangpfs() + 
		"'," + getYangpzl() + "," + songjdwid + ",'" + getJieyr() + "','" + 
		visit.getRenymc() + "','" + getBeiz() + "')";
		flag = con.getInsert(sql);
		if(flag == -1){
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.InsertDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.InsertDatabaseFail);
			con.rollBack();
			return;
		}
		sql = "insert into zhillsb(id,caiyb_id, zhilb_id) values(getnewid(" +
		visit.getDiancxxb_id() + ")," + caiybid + "," + zhilbid + ")";
		flag = con.getInsert(sql);
		if(flag == -1){
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.InsertDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.InsertDatabaseFail);
			con.rollBack();
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}
	
	private String getCaiylbid(){
		String caiylbid = "0";
		JDBCcon con = new JDBCcon();
		String sql = "select id from caiylbb where mingc = '��������'";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			caiylbid = rs.getString("id");
		}
		rs.close();
		return caiylbid;
	}
	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(visit.getActivePageName().toString().equals(
			"Ruccyxg")){
				setRiq(visit.getString7());
				setRiqe(visit.getString8());
			}
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if (getRiqe() == null) {
				setRiqe(DateUtil.FormatDate(new Date()));
			}
			visit.setActivePageName(getPageName().toString());
			this.setTreeid("");
		}
		init();
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