package com.zhiren.jt.gongys;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

public class Meikxx_Xuanzpf extends BasePage implements PageValidateListener {

	//	private final int columns_Length = 13;
	

	// ϵͳ��־���е�״̬�ֶ�
	private static final String ZhangTConstant1 = "�ɹ�";

	private static final String ZhangTConstant2 = "ʧ��";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String DataSource;

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	private String Parameters;// ��¼ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	// ��ѡ��ļ�¼�ı���
	private String Meikdwmc_value;

	public String getMeikdwmc_value() {
		return Meikdwmc_value;
	}

	public void setMeikdwmc_value(String meikdwmc_value) {
		Meikdwmc_value = meikdwmc_value;
	}

	// ���ݸ�ú����Ϣ����(Meikxxpf)ҳ���ú���������
	private String Meikdqmc_value;

	public String getMeikdqmc_value() {
		return Meikdqmc_value;
	}

	public void setMeikdqmc_value(String meikdqmc_value) {
		Meikdqmc_value = meikdqmc_value;
	}

	private String Meikdwid_value;

	public String getMeikdwid_value() {
		return Meikdwid_value;
	}

	public void setMeikdwid_value(String meikdwid_value) {
		Meikdwid_value = meikdwid_value;
	}

	private boolean _FanHClick = false;

	public void FanHBt(IRequestCycle cycle) {
		_FanHClick = true;
	}

	private boolean _MoHCHClick = false;

	public void MoHCHBt(IRequestCycle cycle) {
		_MoHCHClick = true;
	}

	private boolean _TianjClick = false;

	public void TianjBt(IRequestCycle cycle) {
		_TianjClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_FanHClick) { // ���ذ�ť����
			_FanHClick = false;
			cycle.activate("Meikxxpf");
		}
		if (_MoHCHClick) { // ģ����ѯ��ť����
			_MoHCHClick = false;

		}
		if (_TianjClick) { // �����ѡú��ť����
			_TianjClick = false;
            tianj();
			Visit visit = (Visit) getPage().getVisit();
			visit.setString13(Meikdwmc_value); // ú�����
			visit.setString14(Meikdwid_value); // ú��id
//			visit.setString15(Meikdqmc_value); // ú���������
			cycle.activate("Meikxxpf");
		}
	}
	
    private String Meikxxmc_value;
	
	public String getGongysmc_value() {
		return Meikxxmc_value;
	}

	public void setMeikxxmc_value(String meikxxmc_value) {
		Meikxxmc_value = meikxxmc_value;
	}
	
	private long Meikdqbm_value;
	
	public long getMeikdqbm_value(){
		return Meikdqbm_value;
	}
	
	public void setMeikdqbm_value(long meikdqbm_value){
		Meikdqbm_value = meikdqbm_value;
	}
	
	public void tianj(){
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long meikdqbm = 0;
//		String sql1 = "select id,meikdqbm from meikxxbsqpfb"+visit.getString16()+"";
//		ResultSetList list=con.getResultSetList(sql1);
//		while(list.next()){
//			meikdqbm = list.getLong("meikdqbm");
//		}
//		meikdqbm = this.getMeikdqbm_value();
		if(GengGSJBM(con, "meikxxb", Meikdwmc_value, "����", visit.getString16())){
			String sql = "update meikxxbsqpfb set pifzt = 1,bianm = '"+Meikdwmc_value+"' where id = "+ visit.getString16();
			int flag = con.getUpdate(sql.toString());
			if(-1 == flag){
				setMsg("����ʧ�ܣ�");
			}else{
				setMsg("�����ɹ���");
			}
		}else{
			setMsg("�޷������¼���λ��");
		}
		visit.setString17(msg);         //������ʾ��Ϣ����һ��ҳ��
		
		con.Close();
	}
	
	private String logMsg = "";

	private String zhuangT = "";
	
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
			String operation, String id) {
        Visit visit = (Visit)getPage().getVisit();
		InterCom_dt dt = new InterCom_dt();
		String sql_diancxxb_id = "select g.diancxxb_id,d.mingc from meikxxbsqpfb g,diancxxb d where g.diancxxb_id=d.id and g.id="
				+ visit.getString16();
		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
		String diancxxb_id = "";
		String diancmc = "";
		if (rsl.next()) {
			diancxxb_id = rsl.getString("DIANCXXB_ID");
			diancmc = rsl.getString("mingc");
		}

		// ���¼��糧��λ����Ҫ����

		boolean t;

		String[] sqls;
		// Ϊ���ǵ糧��������������--Meikdqmc_value
		
			sqls = new String[] { "update meikxxb set SHANGJGSBM='" + bianm
					+ "' where id=" + visit.getString16()};
		
		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

		if (answer[0].equals("true")) { // �����¼��ֶγɹ�
			zhuangT = ZhangTConstant1;
			t = true;
		} else {
			zhuangT = ZhangTConstant2;
			t = false;
		}

		logMsg = operation + "��" + tableName + "�б���"
				+ bianm.replaceAll("'", "") + "�����¼�" + diancmc + zhuangT;

		return t;
	}
	
//	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
//			String operation, String id,long meikdqbm) {
//
//		InterCom_dt dt = new InterCom_dt();
//		Visit visit = (Visit)getPage().getVisit();
//
//		String sql_diancxxb_id = "select b.diancxxb_id,d.mingc from meikxxbsqpfb b,diancxxb d where b.diancxxb_id=d.id and b.id="
//				+ visit.getString16();
//		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
//		String diancxxb_id = "";
//		String diancmc = "";
//		if (rsl.next()) {
//			diancxxb_id = rsl.getString("DIANCXXB_ID");
//			diancmc = rsl.getString("mingc");
//		}
//
//		// ���¼��糧��λ����Ҫ����
//		boolean t;
//
//		String[] sqls;
//		// Ϊ���ǵ糧��������������
////		if (diancxxb_id.equals("266")) {
////			sqls = new String[] { "update meikxxb set SHANGJGSBM="
////					+ bianm
////					+ "where id=replace("
////					+ id
////					+ ",266,'') "
////					+ " and MEIKDQB_ID in (select id from meikdqb where shangjgsbm='"
////					+ meikdqbm + "') " };
////		} else {
//			sqls = new String[] { "update meikxxb set SHANGJGSBM=" + bianm
//					+ ", MEIKDQ_ID = (select id from gongysb where bianm='"
//					+ meikdqbm + "' and leix=0 and rownum=1) where id=" + visit.getString16() };
////		}
//		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);
//
//		if (answer[0].equals("true")) { // �����¼��ֶγɹ�
//			zhuangT = ZhangTConstant1;
//			t = true;
//		} else {
//			zhuangT = ZhangTConstant2;
//			t = false;
//		}
//
//		logMsg += operation + "��" + tableName + "�б���"
//				+ bianm.replaceAll("'", "") + "�����¼�" + diancmc + zhuangT;
//
//		return t;
//	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		//		sql = "  select m.id,m.xuh,m.bianm,m.mingc,m.quanc,s.quanc as shengfb_id,m.leib,m.leix,j.mingc as jihkjb_id,m.danwdz " +
		//				"from meikxxb m,shengfb s,jihkjb j where m.shengfb_id=s.id(+) and m.jihkjb_id=j.id(+) ";

		String sql = "select m.id,\n" + "       m.xuh,\n"
				+ "       g.mingc gongysb_id,\n" + "       m.bianm,\n"
				+ "       m.mingc,\n" + "       m.quanc,\n"
				+ "       s.quanc as shengfb_id,\n" + "       m.leib,\n"
				+ "       m.leix,\n" + "       j.mingc as jihkjb_id,\n"
				+ "       m.danwdz\n"
				+ "  from meikxxb m, shengfb s, jihkjb j, gongysb g\n"
				+ " where m.shengfb_id = s.id(+)\n"
				+ "   and m.jihkjb_id = j.id(+)\n"
				+ "   and m.meikdq_id = g.id";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikxxb");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("gongysb_id").setHeader("ú�����");
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(150);
		egu.getColumn("shengfb_id").setHeader("ʡ��");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("danwdz").setHeader("��λ��ַ");
		egu.getColumn("danwdz").setWidth(80);

		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
		//		egu.setWidth(1000);
		egu.addTbarBtn(new GridButton("����",
				"function(){document.all.FanHBt.click();}"));
		egu.addTbarText("-");
		egu.addTbarText("ú��λ����:");
		TextField tf = new TextField();
		tf.setId("Meikdwmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("ģ����ѯ", "function(){ "
				+ "	var mh_value=Meikdwmc.getValue(); "
				+ "	mohcx(mh_value,gridDiv_data,gridDiv_ds);" + "} "));
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("����ѡú���������", "function(){"
				+ "	if(gridDiv_sm.getSelected()== null){"
				+ "		Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��ú��λ');" + "	}else{ "
				+ "		var rec=gridDiv_sm.getSelected(); "
				+ "		var str=rec.get('BIANM');"
				+ "		document.all.Meikdwmc_value.value=str;"
				+ "		document.all.Meikdwid_value.value=rec.get('ID'); "
				+ "		document.all.Meikdqmc_value.value=rec.get('GONGYSB_ID'); "
				+ "		document.all.MsgAdd.value='toAdd';" +
						"Ext.Msg.confirm('��ʾ', '��ȷ��Ҫ�øù�Ӧ��������?',function(button,text){" +
				"if(button=='yes'){" +
				"document.all.TianjBt.click();" +
				"}else{" +
				"return;" +
				"}});}}"));
		setExtGrid(egu);

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
			visit.setString17(null);
			// visit.setString9(null);
			Meikdwmc_value = "";
			getSelectData();
		}
		getSelectData();
	}

}
