package com.zhiren.gangkjy.zhilgl.huayxx;

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
import com.zhiren.dtrlgs.faygl.faygs.FaycbBean;
import com.zhiren.dtrlgs.faygl.faygs.FayzgInfo;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumcbBean;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumzgInfo;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Zhilqr extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg("");
	}
	
	// ������

	private String riqi;

	public String getBeginRiq() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setBeginRiq(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
		}

	}

	private String riq2;

	public String getEndRiq() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setEndRiq(String riq2) {
		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		List fhlist = new ArrayList();
		String zhilbid = "";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String strsql = "begin \n";
		String msg="";
		while (mdrsl.next()) {
			
			strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
					",had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
			+"values("+mdrsl.getString("id")+",'"+mdrsl.getString("huaybh")+"',"+mdrsl.getString("caiyb_id")+","
			+ DateUtil.FormatOracleDate(mdrsl.getString("huaysj"))+","+mdrsl.getString("qnet_ar") + ","
			+ mdrsl.getString("aar") + "," + mdrsl.getString("ad") + "," + mdrsl.getString("vdaf") + ","
			 + mdrsl.getString("mt") + "," + mdrsl.getString("stad") + "," + mdrsl.getString("aad") + ","
			 + mdrsl.getString("mad") + "," + mdrsl.getString("qbad") + "," + mdrsl.getString("had") + ","
			 + mdrsl.getString("vad") + "," + mdrsl.getString("fcad") + "," + mdrsl.getString("std") + ","
			 + mdrsl.getString("qgrad") + "," + mdrsl.getString("hdaf") + "," + mdrsl.getString("qgrad_daf") + ","
			 + mdrsl.getString("sdaf") + "," + mdrsl.getString("t1") + "," + mdrsl.getString("t2") + ","
			 + mdrsl.getString("t3") + "," + mdrsl.getString("t4") + ",'" + mdrsl.getString("huayy") + "','"
			 + mdrsl.getString("lury") + "','" + mdrsl.getString("beiz") + "',1,1);\n";
			
			
			if(zhilbid.equals("")){
				zhilbid = mdrsl.getString("ID");
			}else{
				zhilbid = zhilbid + "," + mdrsl.getString("ID");
			}
			
			strsql += "update zhillsb set shenhzt=7,shenhry='"+visit.getRenymc()+"' where zhilb_id =" + mdrsl.getString("id") + ";\n";
			
			msg+=mdrsl.getString("huaybh")+",";
		}
		strsql += " end;";
		
		if(con.getUpdate(strsql)>=0){
			setMsg("������Ϊ"+msg+" �ļ�¼��������");
		}else{
			setMsg("���ʧ�ܣ�");
		}
		if(getShujlyValue().getStrId().equals("3")){ //3=ֱ��
			String Sql = "select id,diancxxb_id from fayslb where zhilb_id in (" + zhilbid +")";
			mdrsl = con.getResultSetList(Sql);
			while (mdrsl.next()) {
				fhlist.add(new FaycbBean(mdrsl.getLong("diancxxb_id"),mdrsl.getLong("id")));
			}
			FayzgInfo.CountChengb(fhlist,false);
			
			Sql = "select id,diancxxb_id from fahb where zhilb_id in (" + zhilbid+")";
			mdrsl = con.getResultSetList(Sql);
			while (mdrsl.next()) {
				fhlist.add(new ShoumcbBean(mdrsl.getLong("diancxxb_id"),mdrsl.getLong("id")));
			}
			mdrsl.close();
			ShoumzgInfo.CountChengb(fhlist,false);
		}
		if(getShujlyValue().getStrId().equals("2")){ //2=���ؽ���
			String Sql = "select id,diancxxb_id from fayslb where zhilb_id in (" + zhilbid +")";
			mdrsl = con.getResultSetList(Sql);
			while (mdrsl.next()) {
				fhlist.add(new FaycbBean(mdrsl.getLong("diancxxb_id"),mdrsl.getLong("id")));
			}
			FayzgInfo.CountChengb(fhlist,false);
		}
		con.Close();
	}
	
	public static void addFahid(List list,String fahid) {
		if(list == null) {
			list = new ArrayList();
		}
		int i=0;
		for( ;i<list.size() ;i++) {
			if(((String)list.get(i)).equals(fahid)) {
				break;
			}
		}
		if(i == list.size()) {
			list.add(fahid);
		}
	}
	
	private void Select(){
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		String zhilb_ids = "";
		String sql="begin \n";
		JDBCcon con = new JDBCcon();
		while(mdrsl.next()){
			zhilb_ids +=","+ mdrsl.getString("huaybh");
			sql +="delete zhilb where id="+ mdrsl.getString("id") + ";\n";
			sql +="update zhillsb set shenhzt=0 ,shenhry='"+visit.getRenymc()+"' where zhilb_id =" + mdrsl.getString("id") + ";\n";
		}
		sql+="end;";
		int flg=con.getUpdate(sql);
		if(flg==-1){
			setMsg("ȡ�����ʧ�ܣ�");
		}else{
			zhilb_ids = zhilb_ids.substring(1);
			setMsg("������"+zhilb_ids+"��ȡ����ˣ�");
		}
		mdrsl.close();
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

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Select();
		}

	}
	
	private void getSelectData() {

		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		String sql = Shenhcl.Judgment(con, getZhilbids());

		String where ="";
		if(!getDiancValue().getStrId().equals("-1")){
			where+=" and dc.id="+getDiancValue().getStrId()+" \n";
		}
		
			where+=" and f.leix_id="+getShujlyValue().getStrId()+" \n";
			
		if(getSHhztValue().getValue().equals("δ���")){
			where+=" and z.shenhzt=0 \n";
		}else{
			where+=" and z.shenhzt=7 and f.jiesb_id=0 \n";
		}
		String sql =
			"select z.zhilb_id as id, z.caiyb_id,vg.mingc as gongys, m.mingc as meikdw, dc.mingc as shouhr, c.mingc as faz, p.mingc as pinz,\n" +
			"f.chec, f.biaoz as biaoz, f.jingz as jingz, z.huaybh, z.huaysj,\n" + 
			"nvl(z.qnet_ar,0) qnet_ar, nvl(z.aar,0) aar, nvl(z.ad,0) ad, nvl(z.vdaf,0) vdaf, nvl(z.mt,0) mt,\n" + 
			"nvl(z.stad,0) stad, nvl(z.aad,0) aad, nvl(z.mad,0) mad, nvl(z.qbad,0) qbad,nvl(z.had,0) had, nvl(z.vad,0) vad, nvl(z.fcad,0) fcad, nvl(z.std,0) std, nvl(z.qgrad,0) qgrad, nvl(z.hdaf,0) hdaf, nvl(z.qgrad_daf,0) qgrad_daf,\n" + 
			"nvl(z.sdaf,0) sdaf, nvl(z.t1,0) t1,nvl(z.t2,0) t2,nvl(z.t3,0) t3,nvl(z.t4,0) t4,z.huayy, z.lury,z.beiz\n" + 
			" from zhillsb z, fahbtmp f, vwgongys vg, meikxxb m, chezxxb c, pinzb p, diancxxb dc\n" + 
			" where z.zhilb_id=f.zhilb_id\n" + 
			" and f.gongysb_id=vg.id\n" + 
			" and m.id=f.meikxxb_id\n" + 
			" and c.id=f.faz_id\n" + 
			" and p.id=f.pinzb_id\n" + 
			" and f.diancxxb_id=dc.id\n" + 
			" and f.shujly='�Զ��ϴ�'\n" + 
			" and f.fayslb_id<>0\n" + 
			where + 
			" and f.fahrq between "
			+ DateUtil.FormatOracleDate(getBeginRiq()) + " and  "+ DateUtil.FormatOracleDate(getEndRiq()) ;


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		 ���ñ��������ڱ���
		egu.setTableName("zhilb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// /������ʾ������
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("gongys").setHeader("��Ӧ��<br>&nbsp");
		egu.getColumn("gongys").setWidth(100);
		egu.getColumn("meikdw").setHeader("ú��λ<br>&nbsp");
		egu.getColumn("meikdw").setWidth(70);
		egu.getColumn("shouhr").setHeader("�ջ���<br>&nbsp");
		egu.getColumn("shouhr").setWidth(100);
		egu.getColumn("faz").setHeader("��վ<br>&nbsp");
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("Ʒ��<br>&nbsp");
		egu.getColumn("pinz").setWidth(50);
		egu.getColumn("chec").setHeader("����/����<br>&nbsp");
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("biaoz").setHeader("Ʊ��(��)<br>&nbsp");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("jingz").setHeader("����(��)<br>&nbsp");
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("huaybh").setHeader("������<br>&nbsp");
		egu.getColumn("huaysj").setHeader("����ʱ��<br>&nbsp");
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("aar").setHeader("�յ����ҷ�<p>Aar(%)</p>");
		egu.getColumn("ad").setHeader("������ҷ�<p>Ad(%)</p>");
		egu.getColumn("vdaf").setHeader("�����޻һ��ӷ���<p>Vdaf(%)</p>");
		egu.getColumn("mt").setHeader("ȫˮ��<p>Mt(%)</p>");
		egu.getColumn("stad").setHeader("���������ȫ��<p>St,ad(%)</p>");
		egu.getColumn("aad").setHeader("����������ҷ�<p>Aad(%)</p>");
		egu.getColumn("mad").setHeader("���������ˮ��<p>Mad(%)</p>");
		egu.getColumn("qbad").setHeader("�����������Ͳ��ֵ<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("had").setHeader("�����������<p>Had(%)</p>");
		egu.getColumn("vad").setHeader("����������ӷ���<p>Vad(%)</p>");
		egu.getColumn("fcad").setHeader("�̶�̼<p>FCad(%)</p>");
		egu.getColumn("std").setHeader("�����ȫ��<p>St,d(%)</p>");
		egu.getColumn("qgrad").setHeader("�����������λ��ֵ<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("t1").setHeader("T1(��)<br>&nbsp");
		egu.getColumn("t2").setHeader("T2(��)<br>&nbsp");
		egu.getColumn("t3").setHeader("T3(��)<br>&nbsp");
		egu.getColumn("t4").setHeader("T4(��)<br>&nbsp");
		egu.getColumn("huayy").setHeader("����Ա<br>&nbsp");
		egu.getColumn("lury").setHeader("����¼��Ա<br>&nbsp");
		egu.getColumn("beiz").setHeader("���鱸ע<br>&nbsp");
		egu.getColumn("huaybh").setWidth(100);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		
		
		egu.addPaging(25);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);//����Grid������Ϊֻ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		// Toolbar tb1 = new Toolbar("tbdiv");
		egu.addTbarText("��λ");
		ComboBox DianCombo = new ComboBox();
		DianCombo.setTransform("DiancDropDown");
		DianCombo.setId("DiancDropDown");
		DianCombo.setEditable(false);
		DianCombo.setLazyRender(true);// ��̬��
		DianCombo.setWidth(120);
		DianCombo.setReadOnly(true);
		DianCombo.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(DianCombo.getScript());

		
		egu.addTbarText("��������");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBeginRiq());
		df.Binding("RIQI", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getEndRiq());
		df1.Binding("RIQ2", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");

		egu.addTbarText("״̬");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("SHhztDropDown");
		comb1.setId("SHhzt");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(80);
		comb1.setReadOnly(true);
		comb1.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(comb1.getScript());

//		ҵ������
		egu.addTbarText("����");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("ShujlyDropDown");
		comb2.setId("ShujlyDropDown");
		comb2.setEditable(false);
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(80);
		comb2.setReadOnly(true);
		comb2.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(comb2.getScript());
		
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		if(getSHhztValue().getValue().equals("δ���")){
			egu.addToolbarButton("���", GridButton.ButtonType_SubmitSel, "SaveButton");
			egu.addTbarText("-");
		}else{
			egu.addToolbarButton("ȡ�����", GridButton.ButtonType_SubmitSel, "HuitButton");
			egu.addTbarText("-");
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

//	���״̬�����˵�_begin
	public IDropDownBean getSHhztValue(){
		if(((Visit)this.getPage().getVisit()).getDropDownBean8()==null){
			IPropertySelectionModel ipsm=getSHhztModel();
		
			setSHhztValue((IDropDownBean)ipsm.getOption(0));
		}
	return	((Visit)this.getPage().getVisit()).getDropDownBean8();
	}
	public void setSHhztValue(IDropDownBean SHhztValue){
		((Visit)this.getPage().getVisit()).setDropDownBean8(SHhztValue);
	}
	public IPropertySelectionModel getSHhztModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel8()==null){
			getSHhztOpitions();
		}
	return	((Visit)this.getPage().getVisit()).getProSelectionModel8();
	}
	public void setSHhztModel(IPropertySelectionModel SHhztModel){
		((Visit)this.getPage().getVisit()).setProSelectionModel8(SHhztModel);
	}
	public void getSHhztOpitions(){
		List list=new ArrayList();
			list.add(new IDropDownBean("1","δ���"));
			list.add(new IDropDownBean("2","�����"));
		setSHhztModel(new IDropDownModel(list));
	}
	
//	ҵ������
	private IDropDownBean ShujlyValue;

	public IDropDownBean getShujlyValue() {
		if (ShujlyValue == null) {
			ShujlyValue = (IDropDownBean) getShujlyModel().getOption(0);
		}
		return ShujlyValue;
	}

	public void setShujlyValue(IDropDownBean Value) {
		if (!(ShujlyValue == Value)) {
			ShujlyValue = Value;
		}
	}

	private IPropertySelectionModel ShujlyModel;

	public void setShujlyModel(IPropertySelectionModel value) {
		ShujlyModel = value;
	}

	public IPropertySelectionModel getShujlyModel() {
		if (ShujlyModel == null) {
			getShujlyModels();
		}
		return ShujlyModel;
	}

	public IPropertySelectionModel getShujlyModels() {
		String sql="select id, mingc from yewlxb union select -1 as id, '��ѡ��' as mingc from dual order by id";
		ShujlyModel = new IDropDownModel(sql);
		return ShujlyModel;
	}
	
//	�糧������
	private IDropDownBean DiancValue;

	public IDropDownBean getDiancValue() {
		if (DiancValue == null) {
			DiancValue = (IDropDownBean) getDiancModel().getOption(0);
		}
		return DiancValue;
	}

	public void setDiancValue(IDropDownBean Value) {
		if (!(DiancValue == Value)) {
			DiancValue = Value;
		}
	}

	private IPropertySelectionModel DiancModel;

	public void setDiancModel(IPropertySelectionModel value) {
		DiancModel = value;
	}

	public IPropertySelectionModel getDiancModel() {
		if (DiancModel == null) {
			getDiancModels();
		}
		return DiancModel;
	}

	public IPropertySelectionModel getDiancModels() {
		String sql="select id,mingc from diancxxb where jib=3 and neibxs=0 and cangkb_id=1";
		DiancModel = new IDropDownModel(sql,"��ѡ��");
		return DiancModel;
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			setShujlyValue(null);
			setShujlyModel(null);
			setDiancValue(null);
			setDiancModel(null);
			riqi = null;
			riq2 = null;
		}
		getSelectData();
	}

}