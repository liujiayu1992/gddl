package com.zhiren.dc.jilgl.shujcl;

import java.util.ArrayList;
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
import com.zhiren.common.ext.form.Radio;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author Rock
 * @since 2009-06-30
 * @version 1.0
 * @description
 * �����ճ����� �����ճ�ģʽѡ����Ԥ�������ճ����ܴ��봦��
 * ����xitxxb����ȷ��Ĭ���ճ�ʱ�� Ĭ���ճ����ݲ�ѯʱ�� Ĭ���ճ�ģʽ
 * ���ã�
 * leib = '����'
 * zhuangt = 1
 * diancxxb_id = �糧id
 * 
 * mingc = 'Ĭ���ճ�ģʽ'  zhi = (1:������ʾģʽ / 0:��������ʾģʽ)
 * mingc = 'Ĭ���ճ�����'	 zhi = (sysdate-1 ��ǰϵͳ���ڵ�ǰһ��...)
 * mingc = 'Ĭ���ճ���ѯ��ʼʱ��'	zhi = ͬĬ���ճ�����
 * mingc = 'Ĭ���ճ���ѯ����ʱ��'	zhi = ͬĬ���ճ�����
 */
public class Shouc extends BasePage implements PageValidateListener {
	/* ���ܵ���ʾģʽ (��ʾһ�����ݽ����ճ�) */
	private static final int showType_sum = 1;	
	/* �������ʾģʽ (��ʾÿ���ε�����) */
	private static final int showType_group = 2;
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	// �Ƿ���ʾ ����/���� �µ����г�Ƥ
	private boolean showlist;
	public boolean isShowlist(){
		return showlist;
	}
	public void setShowlist(boolean sl){
		showlist = sl;
	}
	// ��ǰѡ�е�ҳ����ʾģʽ
	private int currentType;
	public int getCurType(){
		return currentType;
	}
	public void setCurType(int ctype){
		currentType = ctype;
	}
	
	// �����û���ʾ 
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	// �󶨿�ʼ����
	private String startRiq;
	public String getStartRiq() {
		return startRiq;
	}
	public void setStartRiq(String riq) {
		this.startRiq = riq;
	}

	// �󶨽�������
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}
	
	// ���ճ�����
	private String daohrq;
	public String getDaohrq(){
		return daohrq;
	}
	public void setDaohrq(String dhrq){
		daohrq = dhrq;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// �ճ����� :ͬһ���ε�Ҫ���в�֣������ĳ�Ƥ�ճ���û��������������
	// begin -----------------
	private void GroupShouc(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag = 0;
		String sql = "";
		String shoucrq = DateUtil.FormatOracleDate(getDaohrq());
		long diancxxb_id = visit.getDiancxxb_id();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		List fhlist = new ArrayList();
		while(rsl.next()){
			String fhid = rsl.getString("id");
			double piz = rsl.getDouble("piz");
//			���Ƥ��Ϊ0��֤���÷���û��һ�����ⲻ���ճ�
			if(piz == 0)continue;
			sql = "select * from chepb where fahb_id=" + fhid + " and piz=0";//һ��fahb��id��Ӧ���chepb��id
			if(con.getHasIt(sql)){  //������ݿ����溬�������¼��
//				����һ���µķ���
				String newfahid = Jilcz.CopyFahb(con, fhid, diancxxb_id);//����������ص���id������null��
				if (newfahid == null) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("����fahbʧ��");
					setMsg("����fahbʧ��");
					return;
				}
				Jilcz.addFahid(fhlist, newfahid);
//				����δ��ﳵƤ��fahb_id
				sql = "update chepb set fahb_id =" + newfahid + " where fahb_id=" + fhid +" and piz=0";
				flag = con.getUpdate(sql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
							+ sql);
					setMsg("����δ��ﳵƤ��fahb_idʧ��");
					return;
				}
//				�����·�����ëƤ�ص���Ϣ
				flag = Jilcz.updateFahb(con, newfahid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("����fahbʧ��");
					setMsg("����fahbʧ��");
					return;
				}
//				�ж��Ƿ񵥳���������
				boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("����", "������㷽��", String.valueOf(diancxxb_id), "����"));
				if(isDancYuns){
					sql = "select id from chepb where fahb_id=" + fhid + " and piz<>0";
					ResultSetList rscp = con.getResultSetList(sql);
					while(rscp.next()){
						flag = Jilcz.CountChepbYuns(con, rscp.getString("id"), SysConstant.HEDBZ_YJJ);
						if (flag == -1) {
							con.rollBack();
							con.Close();
							WriteLog.writeErrorLog("����chepb����ʧ��");
							setMsg("����chepb����ʧ��");
							return;
						}
					}
					rscp.close();
				}
				flag = Jilcz.updateFahb(con, fhid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("����fahbʧ��");
					setMsg("����fahbʧ��");
					return;
				}
//				������ǵ������䣬�������������
				if(!isDancYuns){
					flag = Jilcz.CountFahbYuns(con, fhid);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog("����fahb����ʧ��");
						setMsg("����fahb����ʧ��");
						return;
					}
				}
				
			
			}//if(con.getHasIt(sql))
			
//			�������fahb��id��daohrq����Ϊshoucrq
			sql = "update fahb set daohrq = " + shoucrq + " where id=" + fhid;
			con.getUpdate(sql);
		}
		rsl.close();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
//		����ɱ�
		Chengbjs.CountChengb(diancxxb_id, fhlist);
	}
	private void SumShouc(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);//ȡ���Զ����¡�
		ResultSetList rsl;
		String shoucrq = DateUtil.FormatOracleDate(getDaohrq());
		String sql = "";
		int flag;
		List fhlist = new ArrayList();
		long diancxxb_id = visit.getDiancxxb_id();
		String id = "";
//		û�ճ���id�У���piz<>0����������˼��һ���ζ�û���Ͳ��������ӷ�����
//		û�ж�fahb������������  Ӧ�ú� ҳ���ϵ�ʱ��һ��
		sql = "select f.id from fahb f where f.daohrq = to_date('2050-12-31','yyyy-mm-dd') and f.piz <> 0 and f.yunsfsb_id =" + SysConstant.YUNSFS_QIY
			+" and exists(select c.fahb_id from chepb c where c.fahb_id=f.id   and c.qingcsj >="+DateUtil.FormatOracleDate(this.getStartRiq())+" and  c.qingcsj<"+DateUtil.FormatOracleDate(this.getEndRiq())+"+1 )";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			id = rsl.getString("id");
			Jilcz.addFahid(fhlist, id);
			sql = "select * from chepb where fahb_id=" + id + " and piz=0";//һ��fahb��id��Ӧ���chepb��id
			if(con.getHasIt(sql)){  //������ݿ����溬�������¼��
//				����һ���µķ���
				String newfahid = Jilcz.CopyFahb(con, id, diancxxb_id);//����������ص���id������null��
				if (newfahid == null) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("����fahbʧ��");
					setMsg("����fahbʧ��");
					return;
				}
				Jilcz.addFahid(fhlist, newfahid);
//				����δ��ﳵƤ��fahb_id
				sql = "update chepb set fahb_id =" + newfahid + " where fahb_id=" + id +" and piz=0";
				flag = con.getUpdate(sql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
							+ sql);
					setMsg("����δ��ﳵƤ��fahb_idʧ��");
					return;
				}
//				�����·�����ëƤ�ص���Ϣ
				flag = Jilcz.updateFahb(con, newfahid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("����fahbʧ��");
					setMsg("����fahbʧ��");
					return;
				}
//				�ж��Ƿ񵥳���������
				boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("����", "������㷽��", String.valueOf(diancxxb_id), "����"));
				if(isDancYuns){
					sql = "select id from chepb where fahb_id=" + id + " and piz<>0";
					ResultSetList rscp = con.getResultSetList(sql);
					while(rscp.next()){
						flag = Jilcz.CountChepbYuns(con, rscp.getString("id"), SysConstant.HEDBZ_YJJ);
						if (flag == -1) {
							con.rollBack();
							con.Close();
							WriteLog.writeErrorLog("����chepb����ʧ��");
							setMsg("����chepb����ʧ��");
							return;
						}
					}
					rscp.close();
				}
				flag = Jilcz.updateFahb(con, id);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("����fahbʧ��");
					setMsg("����fahbʧ��");
					return;
				}
//				������ǵ������䣬�������������
				if(!isDancYuns){
					flag = Jilcz.CountFahbYuns(con, id);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog("����fahb����ʧ��");
						setMsg("����fahb����ʧ��");
						return;
					}
				}
			}//if(con.getHasIt(sql))
//			�������fahb��id��daohrq����Ϊshoucrq
			sql = "update fahb set daohrq = " + shoucrq + " where id=" + id;
			con.getUpdate(sql);
		}//while(rsl.next())
		
		con.commit();
		con.Close();
		if (rsl.getRows() != 0) {
			setMsg(getDaohrq() + "�ճ����̳ɹ�!");
		}else {
			setMsg(getDaohrq() + "û���ճ�����!");
		}
		rsl.close();
//		����ɱ�
		Chengbjs.CountChengb(diancxxb_id, fhlist);
	}
	private void shouc() {
		if(getCurType() == showType_group){
			if(isShowlist()){
//				������չ�����ճ�����
			}else{
				GroupShouc();
			}
		}else{
			if(isShowlist()){
//				������չ�����ճ�����
			}else{
				SumShouc();
			}
		}

	}//shouc()



	// �ճ����� ---------------------------------------------------------------- 
	//  end------------------------

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ShoucChick = false;

	public void ShoucButton(IRequestCycle cycle) {
		_ShoucChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			initGrid();
		} else if (_ShoucChick) {
			_ShoucChick = false;
			shouc();
			initGrid();
		}
	}

	private void initGrid(){
		if(getCurType() == showType_group){
			if(isShowlist()){
//				������չ�����ճ�����
			}else{
				initGroupGrid();
			}
		}else{
			if(isShowlist()){
//				������չ�����ճ�����
			}else{
				initSumGrid();
			}
		}
	}
	// ����ģʽ��grid��ʼ��
	public void initGroupGrid() {
		JDBCcon con = new JDBCcon();
		// Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select f.id,g.mingc as gongysb_id, m.mingc as meikxxb_id, p.mingc as pinzb_id,\n")
						.append("f.chec,f.fahrq,")
				.append("sum(f.maoz) maoz,sum(f.piz) piz,sum(f.biaoz) biaoz,sum(jingz) jingz,sum(f.koud) koud,\n")
						.append(
						" sum(f.kous) kous,sum(f.kouz) kouz,sum(f.ches) ches,sum(c.yjcs) yjcs\n")
				.append(" from fahb f,gongysb g,meikxxb m,pinzb p,\n")
				.append(
						" (select fahb_id,count(id) yjcs from chepb where maoz<>0 and piz <>0\n")
				.append(" and qingcsj >=").append(
						DateUtil.FormatOracleDate(getStartRiq())).append("\n")
				.append(" and qingcsj <").append(
						DateUtil.FormatOracleDate(getEndRiq())).append("+1")
				.append("\n").append(" group by fahb_id) c\n").append(
						" where f.id = c.fahb_id and f.gongysb_id = g.id and f.meikxxb_id = m.id\n")
						.append("and f.pinzb_id = p.id ").append(
						" and f.daohrq = to_date('2050-12-31','yyyy-mm-dd')\n")
				.append(" group by f.id,g.mingc,m.mingc,p.mingc,f.chec,f.fahrq\n");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
//		egu.setHeight("bodyHeight");
//		�����б���
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("piz").setHeader(Locale.piz_fahb);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("koud").setHeader(Locale.koud_fahb);
		egu.getColumn("kous").setHeader(Locale.kous_fahb);
		egu.getColumn("kouz").setHeader(Locale.kouz_fahb);
		egu.getColumn("ches").setHeader(Locale.ches_fahb);
		egu.getColumn("yjcs").setHeader("�Ѽ쳵��");
//		�����п�
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("chec").setWidth(70);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("maoz").setWidth(40);
		egu.getColumn("piz").setWidth(40);
		egu.getColumn("biaoz").setWidth(40);
		egu.getColumn("jingz").setWidth(40);
		egu.getColumn("koud").setWidth(40);
		egu.getColumn("kous").setWidth(40);
		egu.getColumn("kouz").setWidth(40);
		egu.getColumn("ches").setWidth(40);
		egu.getColumn("yjcs").setWidth(70);

		egu.addTbarText("��������:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ", "");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("��:");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ", "");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		GridButton gRefresh = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addTbarText("-");
		egu.addTbarText("�ճ�����");
		DateField scrq = new DateField();
		scrq.setId("scrq");
		scrq.Binding("SHOUCRQ", "");
		scrq.setValue(getDaohrq());
		egu.addToolbarItem(scrq.getScript());
		egu.addToolbarButton("�ճ�",GridButton.ButtonType_SubmitSel, "ShoucButton");
		egu.addTbarText("->");
		Radio rs = new Radio("Model");
		rs.setBoxLabel("����ģʽ");
		rs.setChecked(getCurType() == showType_sum);
		rs.setListeners("check:function(own,ck){if(loadfinish){if(ck){document.getElementById('CurType').value=1;}else{document.getElementById('CurType').value=2}document.getElementById('RefreshButton').click()}}");
		egu.addToolbarItem(rs.getScript());
		Radio rg = new Radio("Model");
		rg.setBoxLabel("����ģʽ");
		rg.setChecked(getCurType() == showType_group);
		rg.setListeners("");
		egu.addToolbarItem(rg.getScript());
		setExtGrid(egu);
		
	}
	// ����ģʽ��grid��ʼ��
	public void initSumGrid() {
		JDBCcon con = new JDBCcon();
		// Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select sum(f.maoz) maoz,sum(f.piz) piz,sum(f.biaoz) biaoz,sum(jingz) jingz,sum(f.koud) koud,\n")
				.append(
						" sum(f.kous) kous,sum(f.kouz) kouz,sum(f.ches) ches,sum(c.yjcs) yjcs\n")
				.append(" from fahb f,\n")
				.append(
						" (select fahb_id,count(id) yjcs from chepb where maoz<>0 and piz <>0\n")
				.append(" and qingcsj >=").append(
						DateUtil.FormatOracleDate(getStartRiq())).append("\n")
				.append(" and qingcsj <").append(
						DateUtil.FormatOracleDate(getEndRiq())).append("+1")
				.append("\n").append(" group by fahb_id) c\n").append(
						" where f.id = c.fahb_id\n").append(
						" and f.daohrq = to_date('2050-12-31','yyyy-mm-dd')\n")
				.append(" group by daohrq\n");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("piz").setHeader(Locale.piz_fahb);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("koud").setHeader(Locale.koud_fahb);
		egu.getColumn("kous").setHeader(Locale.kous_fahb);
		egu.getColumn("kouz").setHeader(Locale.kouz_fahb);
		egu.getColumn("ches").setHeader(Locale.ches_fahb);
		egu.getColumn("yjcs").setHeader("�Ѽ쳵��");

		egu.addTbarText("��������:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ", "");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("��:");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ", "");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		GridButton gRefresh = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addTbarText("-");
		egu.addTbarText("�ճ�����");
		DateField scrq = new DateField();
		scrq.setId("scrq");
		scrq.Binding("SHOUCRQ", "");
		scrq.setValue(getDaohrq());
		egu.addToolbarItem(scrq.getScript());
		StringBuffer sShoucHandler = new StringBuffer();
		sShoucHandler.append(
				"function(){Ext.MessageBox.confirm('��ʾ��Ϣ','�Ƿ����' + scrq.getValue().dateFormat('Y-m-d') + '�ճ�������',").append(
				"function(btn){if (btn == 'yes') {").append(
				"document.getElementById('ShoucButton').click();").append("}}")
				.append(")}");

		GridButton gShouc = new GridButton("�ճ�", sShoucHandler.toString());
		egu.addTbarBtn(gShouc);
		egu.addTbarText("->");
		Radio rs = new Radio("Model");
		rs.setBoxLabel("����ģʽ");
		rs.setChecked(getCurType() == showType_sum);
		rs.setListeners("check:function(own,ck){if(loadfinish){if(ck){document.getElementById('CurType').value=1;}else{document.getElementById('CurType').value=2}document.getElementById('RefreshButton').click()}}");
		egu.addToolbarItem(rs.getScript());
		Radio rg = new Radio("Model");
		rg.setBoxLabel("����ģʽ");
		rg.setChecked(getCurType() == showType_group);
		rg.setListeners("");
		egu.addToolbarItem(rg.getScript());
		setExtGrid(egu);
		
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
			init(visit);
		}
	}

	private void init(Visit v) {
		JDBCcon con = new JDBCcon();
		setDefaultStartRiq(con,v.getDiancxxb_id());
		setDefaultEndRiq(con,v.getDiancxxb_id());
		setDefaultShoucRiq(con,v.getDiancxxb_id());
		setDefaultShowType(con,v.getDiancxxb_id());
		showlist = false;
		con.Close();
		initGrid();
	}
	
	private void setDefaultStartRiq(JDBCcon con, long dcid){
//		���ò�ѯĬ�Ͽ�ʼʱ��
//		zhi = sysdate - to_number(to_char(sysdate-1,'dd')) ��ǰ�µ�һ��
		Date morkssj = new Date();
		String sql = "select zhi from xitxxb where mingc ='Ĭ���ճ���ѯ��ʼʱ��'"
				+ " and zhuangt =1 and leib = '����' and diancxxb_id = " + dcid;
		ResultSetList rsl = con.getResultSetList(sql);
		String morsj = "sysdate - 1";
		if(rsl.next()){
			morsj = rsl.getString("zhi");
		}
		rsl.close();
		sql = "select " + morsj + " kssj from dual";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			morkssj = DateUtil.getDate(rsl.getDateString("kssj"));
		}
		rsl.close();
		setStartRiq(DateUtil.FormatDate(morkssj));
	}
	private void setDefaultEndRiq(JDBCcon con, long dcid){
//		���ò�ѯĬ�Ͽ�ʼʱ��
//		zhi = sysdate - to_number(to_char(sysdate-1,'dd')) ��ǰ�µ�һ��
		Date morjssj = new Date();
		String sql = "select zhi from xitxxb where mingc ='Ĭ���ճ���ѯ����ʱ��'"
				+ " and zhuangt =1 and leib = '����' and diancxxb_id = " + dcid;
		ResultSetList rsl = con.getResultSetList(sql);
		String morsj = "sysdate - 1";
		if(rsl.next()){
			morsj = rsl.getString("zhi");
		}
		rsl.close();
		sql = "select " + morsj + " kssj from dual";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			morjssj = DateUtil.getDate(rsl.getDateString("kssj"));
		}
		rsl.close();
		setEndRiq(DateUtil.FormatDate(morjssj));
	}
	private void setDefaultShoucRiq(JDBCcon con, long dcid){
//		���ò�ѯĬ�Ͽ�ʼʱ��
//		zhi = sysdate - to_number(to_char(sysdate-1,'dd')) ��ǰ�µ�һ��
		Date mor = new Date();
		String sql = "select zhi from xitxxb where mingc ='Ĭ���ճ�����'"
				+ " and zhuangt =1 and leib = '����' and diancxxb_id = " + dcid;
		ResultSetList rsl = con.getResultSetList(sql);
		String morsj = "sysdate - 1";
		if(rsl.next()){
			morsj = rsl.getString("zhi");
		}
		rsl.close();
		sql = "select " + morsj + " kssj from dual";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			mor = DateUtil.getDate(rsl.getDateString("kssj"));
		}
		rsl.close();
		setDaohrq(DateUtil.FormatDate(mor));
	}
	private void setDefaultShowType(JDBCcon con, long dcid){
		String sql = "select zhi from xitxxb where mingc ='Ĭ���ճ�ģʽ'"
			+ " and zhuangt =1 and leib = '����' and diancxxb_id = " + dcid;
		ResultSetList rsl = con.getResultSetList(sql);
		int mor = showType_sum;
		if(rsl.next()){
			mor = rsl.getInt("zhi");
		}
		rsl.close();
		currentType = mor;
	}
}
