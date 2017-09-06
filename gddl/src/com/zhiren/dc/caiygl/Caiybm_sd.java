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
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author Rock
 * @version v1.1.2.4
 * @since 2009-05-20
 * 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-19
 * �������������䷽ʽΪ���˻�ȫ��ʱ���ӷ�վ��,���������շ�����֡�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-19 18:20
 * ����������ʱ��ѡ��Ĳ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-25 17:39
 * ������ȡ��ҳ���ҳ����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-16 17��02
 * ���������Ӳ���ַ���ʱ����ID �������㷨�ĵ���
 */
public class Caiybm_sd extends BasePage implements PageValidateListener {
	/* ��Դ���ò��� ģʽ �����ɣ�*/
	private static final String MOD_SC = "SC";
//	private static final String MOD_XG = "XG";
	/* ��Դ���ò��� ���䷽ʽ��ȫ����*/
	private static final String YUNSFS_ALL = "ALL";
	/* ��Դ���ò��� ���䷽ʽ����·��*/
	private static final String YUNSFS_HY = "HY";
	/* ��Դ���ò��� ���䷽ʽ����·��*/
	private static final String YUNSFS_QY = "QY";
	/* ��ť���� ���� */
	private static final String BtnType_SC = "����";
	/* ��ť���� �ϲ� */
	private static final String BtnType_HB = "�ϲ�";
	/* ��ť���� ���� */
	private static final String BtnType_TZ = "����";
	/* ��ť���� ɾ�� */
	private static final String BtnType_DL = "ɾ��";
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
//	���������
	public IDropDownBean getBianhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianhModel().getOptionCount() > 0) {
				setBianhValue((IDropDownBean) getBianhModel().getOption(0));
			}else{
				setBianhValue(new IDropDownBean());
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
	}

	public void setBianhModels() {
		String sql = 
			"select c.zhilb_id,c.bianm from fahb f, caiyb c\n" +
			"where f.zhilb_id = c.zhilb_id\n" + 
			"and f.hedbz <" + SysConstant.HEDBZ_YSH + getStrWhere("bianm") ;
		setBianhModel(new IDropDownModel(sql,null));
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
	
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _HebingClick = false;

	public void HebingButton(IRequestCycle cycle) {
		_HebingClick = true;
	}
	
	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	
	private boolean _UpdateClick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			setBianhModels();
			initGrid();
		}
		if (_CreateClick) {
			_CreateClick = false;
			Save(BtnType_SC);
			init();
		}
		if (_HebingClick) {
			_HebingClick = false;
			Save(BtnType_HB);
			init();
		}
		if (_DeleteClick) {
			_DeleteClick = false;
			Save(BtnType_DL);
			init();
		}
		if (_UpdateClick) {
			_UpdateClick = false;
			Save(BtnType_TZ);
			init();
		}
	}
	/**
	 * 
	 * @param mod	����SQL�ĵص�
	 * @return		����SQL��where��������
	 * @description	���mod=bianm�򲻼�������ID����
	 * 				���mod=meik�򲻼���ú��ID����
	 */
	private String getStrWhere(String mod){
		Visit visit = (Visit) getPage().getVisit();
		String where = "";
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		if(!"bianm".equalsIgnoreCase(mod))
			if(MOD_SC.equals(visit.getString2())){
				where += " and f.zhilb_id = 0 \n";
			}else{
				where += " and f.zhilb_id != 0 \n";
			}
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
			"select c.id,c.fahb_id,c.xuh, m.mingc meik, cz.mingc faz, c.cheph,\n " +
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') guohsj\n " +
			"from fahb f, chepb c, meikxxb m,chezxxb cz\n" + 
			"where f.id = c.fahb_id and f.faz_id = cz.id\n" + 
			"and f.meikxxb_id = m.id\n" + getStrWhere("") +
			"order by c.xuh";

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
		egu.getColumn("fahb_id").setHidden(true);
		// ����grid�б���
		egu.getColumn("xuh").setHeader(Local.xuh);
		egu.getColumn("meik").setHeader(Local.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader(Local.faz_id_fahb);
		egu.getColumn("cheph").setHeader(Local.cheph);
		egu.getColumn("guohsj").setHeader(Local.guohrq);
		
//		�����п��
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("meik").setWidth(120);
		egu.getColumn("faz").setWidth(120);
		egu.getColumn("cheph").setWidth(100);
		egu.getColumn("guohsj").setWidth(120);
//		�趨grid�пɷ�༭
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("meik").setEditor(null);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("guohsj").setEditor(null);
		
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
		egu.addTbarText(Local.riqz);
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
		ComboBox bianm = new ComboBox();
		bianm.setTransform("BianhSelect");
		bianm.setWidth(100);
		bianm
				.setListeners("select:function(own,rec,index){Ext.getDom('BianhSelect').selectedIndex=index}");
//		���ز�ͬģʽ�µİ�ť
		if(MOD_SC.equals(visit.getString2())){
//			 ���ɰ�ť
			GridButton create = new GridButton("����",GridButton.ButtonType_SubmitSel,
					"gridDiv", egu.getGridColumns(), "CreateButton");
			egu.addTbarBtn(create);
			egu.addTbarText("����:");
			egu.addToolbarItem(bianm.getScript());

			// �ϲ���ť
			GridButton save = new GridButton("�ϲ�",GridButton.ButtonType_SubmitSel, "gridDiv",
					egu.getGridColumns(), "HebingButton");
			egu.addTbarBtn(save);
		}else{
//			 ɾ����ť
			GridButton delete = new GridButton("ɾ��",GridButton.ButtonType_SubmitSel,
					"gridDiv", egu.getGridColumns(), "DeleteButton");
			egu.addTbarBtn(delete);
			egu.addTbarText("����:");
			egu.addToolbarItem(bianm.getScript());
			// ������ť
			GridButton save = new GridButton("����",GridButton.ButtonType_SubmitSel, "gridDiv",
					egu.getGridColumns(), "UpdateButton");
			egu.addTbarBtn(save);
		}
		
		setExtGrid(egu);
		con.Close();

	}
	/**
	 * 
	 * @param con	JDBCConnection
	 * @param buttonType	��ť����
	 * @param diancxxb_id	�糧ID
	 * @return	�õ���������ID
	 * @description	���ݲ�ͬ�İ�ť���͵õ�����ID 
	 * 				�����ťΪ������ֱ�Ӳ���һ����������
	 * 				�����ťΪ�ϲ��������õ�ҳ����ѡ������ID 
	 * 				�����ťΪɾ����õ�������IDΪ0
	 */
	private long getZhilid(JDBCcon con, String buttonType, long diancxxb_id){
		long zhilb_id = 0;
		if(BtnType_SC.equals(buttonType)){
			zhilb_id = Jilcz.getZhilbid(con, null, new Date(), diancxxb_id);
			int flag = Caiycl.CreatBianh(con,zhilb_id,diancxxb_id);
			if(flag ==-1) {
				return flag;
			}
		}else if(BtnType_HB.equals(buttonType) || BtnType_TZ.equals(buttonType)){
			if(getBianhValue()!=null&& getBianhValue().getId() != -1){
				zhilb_id = getBianhValue().getId();
			}else{
				return -1;
			}
		}else if(BtnType_DL.equals(buttonType)){
			
		}
		return zhilb_id;
	}
	
	private boolean Save(String buttonType){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û��δ�ύ�Ķ���");
			return false;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String chepbids = ""; //��¼���Ĺ��ĳ�ƤID
		String sql = ""; //sql���
		List fahlist = new ArrayList(); //��¼����id
		//�õ�ѡ�е����г�ƤID
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		if(rs.getRows()>0){
			while(rs.next()){
				chepbids += "," + rs.getString("id");
			}
			rs.close();
			chepbids = chepbids.substring(1);
		}
		sql = 
			"select c.fahb_id,max(f.ches) zcs,count(c.id) xcs from fahb f, chepb c\n" +
			"where f.id = c.fahb_id\n" + 
			"and c.id in ("+chepbids+")\n" + 
			"group by c.fahb_id";
		rs = con.getResultSetList(sql);
//		�õ�����
		long zhilb_id = getZhilid(con,buttonType,visit.getDiancxxb_id());
//		ѭ������
		while(rs.next()){
			long upfahb_id = rs.getLong("fahb_id");
			Jilcz.addFahid(fahlist, String.valueOf(upfahb_id));
//			�Ƿ��ַ���
			if(rs.getInt("zcs") != rs.getInt("xcs")){
//				���Ʒ���
				String newfahid = Jilcz.CopyFahb(con, String.valueOf(upfahb_id), visit.getDiancxxb_id());
//				��¼�Ѹ��ķ���ID
				Jilcz.addFahid(fahlist, newfahid);
//				����ҳ�����õ�ģʽ��������δѡ��������ѡ������������
				String sqltmp = "";
				if(MOD_SC.equals(visit.getString2())){
					sqltmp = "not";
				}
				sql = " update chepb set fahb_id =" + newfahid + " where id "+sqltmp+" in (" + chepbids + ") and fahb_id =" +upfahb_id;
				con.getUpdate(sql);
			}
//			���·�������ID
			if(zhilb_id != -1){
				sql = "update fahb set zhilb_id = " + zhilb_id + " where id = " + upfahb_id;
				con.getUpdate(sql);
			}else{
				setMsg("����ʧ�ܣ�");
				return false;
			}
		}
		rs.close();
//		�ж��Ƿ񵥳���������
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("����", "������㷽��", String.valueOf(visit.getDiancxxb_id()), "����"));
//		ѭ��
		for (int i = 0; i < fahlist.size(); i++) {
			String fahbid = (String) fahlist.get(i);
			if(isDancYuns){
				sql = "select id,hedbz from chepb where fahb_id =" + fahbid;
				rs = con.getResultSetList(sql);
				while(rs.next()){
					Jilcz.CountChepbYuns(con, rs.getString("id"), rs.getInt("hedbz"));
				}
				rs.close();
			}
			int flag = Jilcz.updateFahb(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundxg007);
				setMsg(ErrorMessage.Yundxg007);
				return false;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundxg008);
				setMsg(ErrorMessage.Yundxg008);
				return false; 
			}
		}
		con.commit();
		con.Close();
		return true;
	}

	private void init() {
		setChangbModels();
		setBianhModels();
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
		String MOD = cycle.getRequestContext().getParameter("mod");
		if (MOD != null) {
			visit.setString2(MOD);
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqe(DateUtil.FormatDate(new Date()));
			setRiqsetModels();
			riqchange = true;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			if (yunsfs == null) {
				visit.setString1(YUNSFS_ALL);
			}
			if (MOD == null) {
				visit.setString2(MOD_SC);
			}
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