package com.zhiren.dc.jilgl.gongl.shouc;

/*
 * 2009-03-17
 * Yanghj
 * �޸����ݣ����ճ���ʱ�����ͬһ���εĳ�Ƥ�м���δ���ģ�����һ���ν��в�֣�
 *          �����ĳ�Ƥ�����ճ���û���ĳ�Ƥ����һ��������
 */
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * �޸���:tzf
 * ʱ��:2010-03-05
 * �޸�����:�ճ�ʱ�����������ķ�����lie_idҲҪ���и��£�������ԭ���ķ�����һ����
 */
/*
 * �޸���:tzf
 * ʱ��:2009-06-17
 * �޸�����:�ճ�ʱ����fahb��Ӧ�ĳ�Ƥ������ʱ�����ƣ���ҳ����ѡ��ʱ����ڵ�fahb���ݽ����ճ�
 */
/*
 * 2009-05-14
 * ����
 * �޸ļ���ɱ��ķ���listδȫ�����ص�����
 */
public class Shouc extends BasePage implements PageValidateListener {
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
		// setTbmsg(null);
	}

	// private String tbmsg;
	// public String getTbmsg() {
	// return tbmsg;
	// }
	// public void setTbmsg(String tbmsg) {
	// this.tbmsg = tbmsg;
	// }
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

	public void setSRiq(String sRiq) {
		((Visit) getPage().getVisit()).setString1(sRiq);
	}

	public void setERiq(String eRiq) {
		((Visit) getPage().getVisit()).setString2(eRiq);
	}

	public String getSRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public String getERiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public String getQRiq(String ss) {
		if (ss == null || ss.equals("")) {
			String riq = getSRiq();
			return riq.substring(0, 4) + "��" + riq.substring(5, 7) + "��"
					+ riq.substring(8, 10) + "��";
		} else {
			return ss.substring(0, 4) + "��" + ss.substring(5, 7) + "��"
					+ ss.substring(8, 10) + "��";
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

	// �ճ����� :ͬһ���ε�Ҫ���в�֣������ĳ�Ƥ�ճ���û��������������
	// begin -----------------
	private void shouc() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);//ȡ���Զ����¡�
		ResultSetList rsl;
		String sShoucRiq = getSRiq();
		String eShoucRiq = getERiq();
		String shoucrq = DateUtil.FormatOracleDate(sShoucRiq);
		String sql = "select * from xitxxb where mingc = '�ճ���������' and zhuangt=1 and leib = '����' and zhi = '��������'";
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			shoucrq = DateUtil.FormatOracleDate(eShoucRiq);
		}
		rsl.close();// ��rslѭ��֮�������ں���д�رա�

		int flag;
		List fhlist = new ArrayList();
		long diancxxb_id = visit.getDiancxxb_id();
		String id = "";
//		û�ճ���id�У���piz<>0����������˼��һ���ζ�û���Ͳ��������ӷ�����
//		sql = "select id from fahb where daohrq = to_date('2050-12-31','yyyy-mm-dd') and piz <> 0 and yunsfsb_id =" + SysConstant.YUNSFS_QIY;
		
		//û�ж�fahb������������  Ӧ�ú� ҳ���ϵ�ʱ��һ��
		
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
				//��������ɵķ�����lei_id
				flag=this.UpdateLieId(con, newfahid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("lie_id����ʧ��");
					setMsg("lie_id����ʧ��");
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
			setMsg(getQRiq(sShoucRiq) + "�ճ����̳ɹ�!");
		}else {
			setMsg(getQRiq(sShoucRiq) + "û���ճ�����!");
		}
		rsl.close();
		

//		setMsg(ErrorMessage.SaveSuccessMessage);
		
//		����ɱ�
		Chengbjs.CountChengb(diancxxb_id, fhlist);
		
	}//shouc()



	// �ճ����� ---------------------------------------------------------------- 
	//  end------------------------

	private int UpdateLieId(JDBCcon con,String fahb_id){
		
		String sql=" select * from xitxxb where mingc='�ճ��·���lie_id����' and zhi='��' and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(!rsl.next()){//û������ ����Ҫ����
			return 1;
		}
		
		sql=" update fahb set lie_id=getnewid(diancxxb_id) where id="+fahb_id;
		int flag=con.getUpdate(sql);
		return flag;
	}
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
			setSRiq(getStartRiq());
			setERiq(getEndRiq());
			getSelectData();
		} else if (_ShoucChick) {
			_ShoucChick = false;
			shouc();
			getSelectData();
		}
	}

	// ҳ��������ʾ�ķ�����
	public void getSelectData() {
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
//		System.out.println(sb);
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

		egu.addTbarText("��ʼʱ��:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ", "");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("����ʱ��:");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ", "");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);

		StringBuffer sShoucHandler = new StringBuffer();
		sShoucHandler.append(
				"function(){Ext.MessageBox.confirm('��ʾ��Ϣ','�Ƿ����"
						+ getQRiq(getSRiq()) + "�ճ�������',").append(
				"function(btn){if (btn == 'yes') {").append(
				"document.getElementById('ShoucButton').click();").append("}}")
				.append(")}");

		GridButton gShouc = new GridButton("�ճ�", sShoucHandler.toString());
		egu.addTbarBtn(gShouc);

		// egu.setGridType(ExtGridUtil.Gridstyle_Edit);
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
		// if(getTbmsg()!=null) {
		// getExtGrid().addToolbarItem("'->'");
		// getExtGrid().addToolbarItem("'<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>'");
		// }
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
			init();
		}

	}

	private void init() {
		setStartRiq(DateUtil.FormatDate(new Date()));
		setEndRiq(DateUtil.FormatDate(new Date()));
		setSRiq(getStartRiq());
		setERiq(getEndRiq());
		getSelectData();
	}
}
