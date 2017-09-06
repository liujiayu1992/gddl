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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�����
 * ʱ�䣺2010-05-05
 * ���������Ӷ��ظ�������Ϣ�ĸ��´���
 */
/*
 * ����:tzf
 * ʱ��:2009-11-30
 * �޸�����:ͨ���������ÿ���  �����кϼ�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-20
 * ���������ӶԳ���Ĵ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-20
 * ���������������޸ĵ�����ʽ����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-01 11��20
 * �������޸�ˢ��ʱδ��������Ӧ��ú�������Ҳ������ʾ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-30 10��07
 * ���������ӱ���ʱ����ļ��㷽ʽ��������/�������Ĵ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-26 10��19
 * �������޸ĳ���combo��ȡֵ��ʽΪSQLȡֵ
 */
/*
 * �޸���:tzf
 * ʱ��:2009-06-26
 * ����:��·����   ʱ  �������ݵ�  ���䵥λ �п��ܲ����ڣ������������
 */
/*
 * 2009-05-15
 * ����
 * �޸ĵ�������Ĭ��Ϊ��ʾ���޸�
 */
/*
 * 2009-04-30
 * ����
 * �޸ı���ʱ����һ��������ɵ�ж����ʽ�Ķ����治�ϵ�BUG
 * 
 */

/*
 * 2009-8-6 
 * ���ܱ�
 * 1.����"�����޸��Ƿ���Ա༭Ƥ��"�Ĳ�������,Ĭ��Ϊ���ɱ༭
 * 2.ȥ������ʱ�����������䵥λ������
 * 
 * 
 * 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-16
 * ������������Ϣ�޸������Ӱ�����ѡ�����ɫʶ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-03
 * ��������������ʱ�ܿ����㷨,ԭ�㷨��this.isZongkdBfb()?(rsl.getDouble("koud")+rsl.getDouble("kous")+rsl.getDouble("kouz")):rsl.getDouble("koud")
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-03-06
 * �������޸ĳ���bug
 */
public class Shulxxxg extends BasePage implements PageValidateListener {
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

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		List fhlist = new ArrayList();
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundxg.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// �ж��Ƿ񵥳���������
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal
				.getXitxx_item("����", "������㷽��", String.valueOf(visit
						.getDiancxxb_id()), "����"));

		// ɾ����Ƥ
		while (rsl.next()) {
			String fahbid = rsl.getString("fahbid");
			Jilcz.addFahid(fhlist, fahbid);
			String id = rsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_DEL,
					SysConstant.RizOpMokm_Shulxg, "chepb", id);
			String sql = "delete from chepb where id =" + id;
			int flag = con.getDelete(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.Yundxg001);
				return;
			}

		}
		rsl.close();
		rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			String chepid = rsl.getString("id");
			StringBuffer sb = new StringBuffer();
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Shulxg, "chepb", chepid);
			sb
					.append("update chepb set biaoz=")
					.append(rsl.getDouble("biaoz"))
					.append(",koud=")
					.append(rsl.getDouble("koud"))
					.append(",kous=")
					.append(rsl.getDouble("kous"))
					.append(",kouz=")
					.append(rsl.getDouble("kouz"))
					.append(",zongkd=")
					.append(
							rsl.getDouble("koud") + rsl.getDouble("kous")
									+ rsl.getDouble("kouz"))
					.append(",piz=")
					.append(rsl.getDouble("piz"))
					.append(",yunsdwb_id = ")
					.append(
							getExtGrid().getColumn("yunsdwb_id").combo
									.getBeanId(rsl.getString("yunsdwb_id")))
					.append(",meicb_id = ")
					.append(
							getExtGrid().getColumn("meicb_id").combo
									.getBeanId(rsl.getString("meicb_id")) == -1 ? 0
									: getExtGrid().getColumn("meicb_id").combo
											.getBeanId(rsl
													.getString("meicb_id")))
					.append(",chebb_id=").append(
							getExtGrid().getColumn("chebb_id").combo
									.getBeanId(rsl.getString("chebb_id")))
					.append(",cheph='").append(rsl.getString("cheph")).append(
							"',daozch='").append(rsl.getString("daozch"))
					.append("',yuanmkdw='").append(rsl.getString("yuanmkdw"))
					.append("',sanfsl='").append(rsl.getString("sanfsl"))
					.append("',beiz ='").append(rsl.getString("beiz"));
			// Visit v=(Visit)this.getPage().getVisit();
			sb.append("',xiecfsb_id=").append(
					getExtGrid().getColumn("xiecfsb_id").combo.getBeanId(rsl
							.getString("xiecfsb_id"))).append(" where id=")
					.append(chepid);
			int flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb);
				setMsg(ErrorMessage.Yundxg002);
				return;
			} else {
				
				// �ж�Ʊ���Ƿ���۶�
				Jilcz.piaozPz(con, chepid, "chepb");
			}
			String yuanfhid = rsl.getString("fahbid");
			long diancxxb_id;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsl.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}

			sb.delete(0, sb.length());
			sb.append("select f.id fahbid\n");
			sb
					.append("from fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,vwyuanshdw d,\n");
			sb.append("(select id from chezxxb where mingc='").append(
					rsl.getString("faz_id")).append("') fz,\n");
			sb.append("(select id from chezxxb where mingc='").append(
					rsl.getString("daoz_id")).append("') dz,\n");
			;
			sb.append("(select id from chezxxb where mingc='").append(
					rsl.getString("yuandz_id")).append("') yz\n");
			;
			sb.append("where f.fahrq = to_date('").append(
					rsl.getString("fahrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.daohrq = to_date('").append(
					rsl.getString("daohrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.chec ='").append(rsl.getString("chec")).append(
					"' and g.mingc='").append(rsl.getString("gongysb_id"))
					.append("'\n");
			sb.append("and m.mingc ='").append(rsl.getString("meikxxb_id"))
					.append("' and p.mingc = '").append(
							rsl.getString("pinzb_id")).append("'\n");
			sb.append("and j.mingc = '").append(rsl.getString("jihkjb_id"))
					.append("' and d.mingc = '").append(
							rsl.getString("yuanshdwb_id")).append("'\n");
			sb
					.append(
							"and f.yuandz_id = yz.id and f.faz_id = fz.id and f.daoz_id = dz.id and f.diancxxb_id = ")
					.append(diancxxb_id).append("\n");
			sb
					.append("and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id\n");
			sb
					.append(
							"and f.jihkjb_id=j.id and f.yuanshdwb_id=d.id and f.yunsfsb_id = ")
					.append(rsl.getString("yunsfsb_id"));
			ResultSetList r = con.getResultSetList(sb.toString());
			if (r == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
						+ sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			boolean isnotnew = false;
			String updatefhid = null;
			while (r.next()) {
				isnotnew = true;
				if (yuanfhid.equals(r.getString("fahbid"))) {
					updatefhid = null;
					break;
				} else {
					updatefhid = r.getString("fahbid");
				}
			}
			r.close();
			if (isnotnew) {
				if (updatefhid != null) {
					String sql = "update chepb set fahb_id =" + updatefhid
							+ " where id=" + chepid;
					flag = con.getUpdate(sql);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail
								+ "SQL:" + sql);
						setMsg(ErrorMessage.Yundxg003);
						return;
					}
					Jilcz.addFahid(fhlist, updatefhid);
				}
			} else {
				// ��������
				String newFhid = MainGlobal.getNewID(diancxxb_id);

				long meikxxb_id = ((IDropDownModel) getMeikModel())
						.getBeanId(rsl.getString("meikxxb_id"));
				long pinzb_id = getExtGrid().getColumn("pinzb_id").combo
						.getBeanId(rsl.getString("pinzb_id"));
				double yunsl = Jilcz.getYunsl(visit.getDiancxxb_id(), pinzb_id,
						rsl.getInt("yunsfsb_id"), meikxxb_id);
				sb.delete(0, sb.length());
				sb.append("insert into fahb (id,");
				sb.append("yuanid, diancxxb_id, gongysb_id, meikxxb_id,");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq,");
				sb.append("daohrq, zhilb_id, yunsfsb_id,");
				sb.append("chec,yuandz_id, yuanshdwb_id, yunsl) values(");
				sb.append(newFhid).append(",").append(newFhid);
				sb.append(",").append(diancxxb_id).append(",");
				sb.append(
						((IDropDownModel) getGongysModel()).getBeanId(rsl
								.getString("gongysb_id"))).append(",");
				sb.append(meikxxb_id).append(",");
				sb.append(pinzb_id).append(",");
				sb.append(
						((IDropDownModel) getChezModel()).getBeanId(rsl
								.getString("faz_id"))).append(",");
				sb.append(
						getExtGrid().getColumn("daoz_id").combo.getBeanId(rsl
								.getString("daoz_id"))).append(",");
				sb.append(
						getExtGrid().getColumn("jihkjb_id").combo.getBeanId(rsl
								.getString("jihkjb_id"))).append(",");
				sb.append("to_date('").append(rsl.getString("fahrq")).append(
						"','yyyy-mm-dd'),");
				sb.append("to_date('").append(rsl.getString("daohrq")).append(
						"','yyyy-mm-dd'),");
				sb.append(rsl.getString("zhilb_id")).append(",");
				sb.append(rsl.getString("yunsfsb_id")).append(",");
				sb.append("'").append(rsl.getString("chec")).append("',");
				sb.append(
						getExtGrid().getColumn("yuandz_id").combo.getBeanId(rsl
								.getString("yuandz_id"))).append(",");
				sb.append(
						getExtGrid().getColumn("yuanshdwb_id").combo
								.getBeanId(rsl.getString("yuanshdwb_id")))
						.append(",");
				sb.append(yunsl).append(")");
				flag = con.getInsert(sb.toString());

				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+ "SQL:" + sb);
					setMsg(ErrorMessage.Yundxg004);
					return;
				}
				String sql = "update chepb set fahb_id =" + newFhid
						+ " where id=" + chepid;
				flag = con.getUpdate(sql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail
							+ "SQL:" + sql);
					setMsg(ErrorMessage.Yundxg005);
					return;
				}
				Jilcz.addFahid(fhlist, newFhid);

			}
			if (isDancYuns) {
				flag = Jilcz.CountChepbYuns(con, chepid, SysConstant.HEDBZ_YJJ);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Yundxg006);
					setMsg(ErrorMessage.Yundxg006);
					return;
				}
			}
			Jilcz.addFahid(fhlist, yuanfhid);
		}
		for (int i = 0; i < fhlist.size(); i++) {
			String fahbid = (String) fhlist.get(i);
			int flag = Jilcz.updateFahb(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundxg007);
				setMsg(ErrorMessage.Yundxg007);
				return;
			}
			// ����ǰ����μ�����������㲢���µ�������
			if (!isDancYuns) {
				flag = Jilcz.CountFahbYuns(con, (String) fhlist.get(i));
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.CountFahbYunsFailed
							+ "\n\t\t" + this.getPageName());
					setMsg(ErrorMessage.CountFahbYunsFailed);
					return;
				}
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundxg008);
				setMsg(ErrorMessage.Yundxg008);
				return;
			}
		}
		rsl.close();
		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		setMsg("����ɹ�");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private void Return(IRequestCycle cycle) {
		cycle.activate("ShulxgIndex");
	}

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(visit.getString1());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData(String tt) {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();

		boolean IsEditPiz = false;
		String isedit = "select zhi from xitxxb where mingc='�����޸��Ƿ���Ա༭Ƥ��' and leib='����' and zhuangt=1 ";
		ResultSetList rsPIz = con.getResultSetList(isedit);
		while (rsPIz.next()) {
			if (rsPIz.getString("zhi").equals("��")) {
				IsEditPiz = true;
			} else {
				IsEditPiz = false;
			}
		}
		rsPIz.close();
		String ordersql = "order by f.id,c.zhongcsj desc";
		isedit = "select zhi from xitxxb where mingc='�����޸�����ʽ' and leib='����' and zhuangt=1 and beiz = 'ʹ��' ";
		rsPIz = con.getResultSetList(isedit);
		if (rsPIz.next()) {
			ordersql = rsPIz.getString("zhi");
		}
		rsPIz.close();

		// �ж��Ƿ���Ҫ���Ӻϼ���
		String hejrows = "select * from xitxxb where mingc='������Ϣ�޸����Ӻϼ���' and leib='����' and zhi='��' and  zhuangt=1";
		rsPIz = con.getResultSetList(hejrows);
		boolean boo_hejrows = false;
		if (rsPIz.next()) {
			boo_hejrows = true;
		}
		rsPIz.close();

		StringBuffer sb = new StringBuffer();

		if (!boo_hejrows) {
			/*sb
					.append("select c.id,f.id fahbid,f.yunsfsb_id,d.mingc  diancxxb_id,f.zhilb_id, cheph, c.biaoz, c.koud,c.kous,c.kouz, c.maoz, c.piz, c.yuns, c.zhongcsj, g.mingc as gongysb_id, m.mingc as meikxxb_id,mc.mingc as meicb_id,\n");
			sb
					.append("(select mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n");
			sb.append("p.mingc as pinzb_id, j.mingc as jihkjb_id, fahrq,\n");
			sb.append("daohrq, chec,cb.mingc as chebb_id,\n");
			sb
					.append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id\n");
			sb
					.append(",(select mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id,y.mingc yunsdwb_id,\n");
			sb
					.append("(select mingc from chezxxb yc where yc.id = f.yuandz_id) yuandz_id,\n");
			sb.append("vy.mingc yuanshdwb_id,yuanmkdw,c.sanfsl,daozch");
			sb.append(",c.beiz\n");
			sb
					.append("from chepb c,fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,chebb cb,diancxxb d,meicb mc,vwyuanshdw vy,yunsdwb y \n");
			// sb.append("where lursj=to_date('").append(guohsj).append(
			// "','yyyy-mm-dd hh24:mi:ss')\n");
			// sb.append("where \n");
			sb
					.append("where c.fahb_id=f.id and f.gongysb_id=g.id(+) and f.meikxxb_id=m.id(+) and c.yunsdwb_id = y.id(+) \n");
			sb
					.append("and f.pinzb_id=p.id and f.jihkjb_id=j.id and c.chebb_id=cb.id \n");
			sb
					.append("and f.diancxxb_id=d.id and f.yuanshdwb_id = vy.id(+) and c.meicb_id=mc.id(+) \n");
			sb.append("and c.fahb_id in (").append(tt).append(") \n");
			sb.append(ordersql);*/
			//yuss 2012-03-29 ����ԭú��λ��Ĭ��ȡֵΪ��Ӧ��ú�����ƣ��������޸�ʱÿ�ζ�Ҫ�˹�ȥѡ��ԭú��λ�ұ���ʱ��ʾ��ԭú����Ϊ�ա�
			sb
			.append("select c.id,f.id fahbid,f.yunsfsb_id,d.mingc  diancxxb_id,f.zhilb_id, cheph, c.biaoz, c.koud,c.kous,c.kouz, c.maoz, c.piz, c.yuns, c.zhongcsj, g.mingc as gongysb_id, m.mingc as meikxxb_id,mc.mingc as meicb_id,\n");
	sb
			.append("(select mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n");
	sb.append("p.mingc as pinzb_id, j.mingc as jihkjb_id, fahrq,\n");
	sb.append("daohrq, chec,cb.mingc as chebb_id,\n");
	sb
			.append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id\n");
	sb
			.append(",(select mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id,y.mingc yunsdwb_id,\n");
	sb
			.append("(select mingc from chezxxb yc where yc.id = f.yuandz_id) yuandz_id,\n");
	sb.append("vy.mingc yuanshdwb_id,(select mingc from meikxxb where id=(select meikxxb_id from fahb where id="+tt+")) as yuanmkdw,c.sanfsl,daozch");
	sb.append(",c.beiz\n");
	sb
			.append("from chepb c,fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,chebb cb,diancxxb d,meicb mc,vwyuanshdw vy,yunsdwb y \n");
	sb
			.append("where c.fahb_id=f.id and f.gongysb_id=g.id(+) and f.meikxxb_id=m.id(+) and c.yunsdwb_id = y.id(+) \n");
	sb
			.append("and f.pinzb_id=p.id and f.jihkjb_id=j.id and c.chebb_id=cb.id \n");
	sb
			.append("and f.diancxxb_id=d.id and f.yuanshdwb_id = vy.id(+) and c.meicb_id=mc.id(+) \n");
	sb.append("and c.fahb_id in (").append(tt).append(") \n");
	sb.append(ordersql);
			} else {
			sb
					.append("  select id, fahbid,yunsfsb_id, diancxxb_id,zhilb_id, cheph, biaoz, koud,kous,kouz,maoz,piz, yuns, zhongcsj, gongysb_id,  meikxxb_id, meicb_id, faz_id, pinzb_id, jihkjb_id, fahrq,\n"
							+ " daohrq, chec, chebb_id, xiecfsb_id,daoz_id,yunsdwb_id,yuandz_id,yuanshdwb_id,yuanmkdw,sanfsl,daozch,beiz from ( \n");

			sb
					.append("select c.guohb_id,c.piaojh,c.id,f.id fahbid,f.yunsfsb_id,d.mingc  diancxxb_id,f.zhilb_id, cheph, c.biaoz, c.koud,c.kous,c.kouz, c.maoz, c.piz, c.yuns, c.zhongcsj, g.mingc as gongysb_id, m.mingc as meikxxb_id,mc.mingc as meicb_id,\n");
			sb
					.append("(select mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n");
			sb.append("p.mingc as pinzb_id, j.mingc as jihkjb_id, fahrq,\n");
			sb.append("daohrq, chec,cb.mingc as chebb_id,\n");
			sb
					.append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id\n");
			sb
					.append(",(select mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id,y.mingc yunsdwb_id,\n");
			sb
					.append("(select mingc from chezxxb yc where yc.id = f.yuandz_id) yuandz_id,\n");
			sb.append("vy.mingc yuanshdwb_id,yuanmkdw,c.sanfsl,daozch");
			sb.append(",c.beiz\n");
			sb
					.append("from chepb c,fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,chebb cb,diancxxb d,meicb mc,vwyuanshdw vy,yunsdwb y \n");
			sb
					.append("where c.fahb_id=f.id and f.gongysb_id=g.id(+) and f.meikxxb_id=m.id(+) and c.yunsdwb_id = y.id(+) \n");
			sb
					.append("and f.pinzb_id=p.id and f.jihkjb_id=j.id and c.chebb_id=cb.id \n");
			sb
					.append("and f.diancxxb_id=d.id and f.yuanshdwb_id = vy.id(+) and c.meicb_id=mc.id(+) \n");
			sb.append("and c.fahb_id in (").append(tt).append(") \n");

			sb.append(" union \n");

			sb
					.append("   select -99 guohb_id,'' piaojh,-99 as id,-99 fahbid,-99 yunsfsb_id,'�ϼ�'  diancxxb_id,-99  zhilb_id,'' cheph,\n");
			sb
					.append("  sum(c.biaoz) biaoz, sum(c.koud) koud,sum(c.kous) kous,sum(c.kouz) kouz, sum(c.maoz) maoz, sum(c.piz) piz, sum(c.yuns) yuns, null zhongcsj,\n");
			sb
					.append("  '' as gongysb_id, '' as meikxxb_id,'' as meicb_id,'' faz_id, '' as pinzb_id, '' as jihkjb_id,null fahrq,null daohrq, '' chec,'' as chebb_id,\n");
			sb
					.append(" '' xiecfsb_id,''  daoz_id,'' yunsdwb_id,'' yuandz_id,'' yuanshdwb_id,'' yuanmkdw,sum(c.sanfsl) sanfsl,'' daozch,'' beiz\n");
			sb
					.append(" from chepb c,fahb f where c.fahb_id=f.id and  c.fahb_id in ("
							+ tt + ") \n");

			sb.append(" )\n");

			sb.append(ordersql);
		}

		// System.out.println(sb.toString());

		ResultSetList rsl = con.getResultSetList(sb.toString());
		int coun = 0;
		if (rsl != null) {

			coun = rsl.getRows();
		}
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTableName("cheplsb");
		// egu.setTableName("fahb");
		// ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// ����ÿҳ��ʾ����
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("fahbid").editor = null;
		egu.getColumn("yunsfsb_id").setHidden(true);
		egu.getColumn("yunsfsb_id").editor = null;
		// egu.getColumn("diancxxb_id").setHidden(true);
		// egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").editor = null;
		if (v.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql = "select id,mingc from diancxxb where fuid="
					+ v.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		} else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			egu.getColumn("diancxxb_id").setDefaultValue(
					String.valueOf(v.getDiancxxb_id()));
		}
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setWidth(60);

		egu.getColumn("kous").setHeader("��ˮ");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kous").setHidden(true);

		egu.getColumn("kouz").setHeader("����");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("kouz").setHidden(true);

		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").editor = null;
		egu.getColumn("piz").setHeader(Locale.piz_fahb);
		egu.getColumn("piz").setWidth(60);
		if (IsEditPiz) {
			// ���������,�������޸�Ƥ��
			String isQic = "select f.yunsfsb_id from fahb f where f.id=" + tt;
			ResultSetList ISQIC = con.getResultSetList(isQic);
			while (ISQIC.next()) {
				if (ISQIC.getInt("yunsfsb_id") == 2) {
					egu.getColumn("piz").editor = null;
				}
			}

		} else {
			egu.getColumn("piz").editor = null;
		}

		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("yuns").editor = null;
		egu.getColumn("zhongcsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("zhongcsj").setWidth(60);
		egu.getColumn("zhongcsj").editor = null;
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		// egu.getColumn("daohrq").setEditor(null);
		// egu.getColumn("daohrq").setHidden(true);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(90);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(85);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("sanfsl").setHeader(Locale.sfsl_chepb);
		egu.getColumn("sanfsl").setWidth(90);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("xiecfsb_id").setHeader("ж����ʽ");
		egu.getColumn("xiecfsb_id").setWidth(90);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		// ����Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(v.getDaoz());
		// ���÷������ں͵������ڵ�Ĭ��ֵ
		String riq = DateUtil.FormatDate(new Date());
		egu.getColumn("fahrq").setDefaultValue(riq);
		egu.getColumn("daohrq").setDefaultValue(riq);
		// ���õ�վ������
		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c1);
		c1.setEditable(true);
		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		// ����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzSql = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		// ���ÿھ�������
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjSql = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));

		// ���ó���������
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Cheb));
		egu.getColumn("chebb_id").setDefaultValue("·��");
		// ����ԭ��վ������
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz_id").setEditor(c6);
		c6.setEditable(true);
		String YuandzSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel(YuandzSql));
		egu.getColumn("yuandz_id").setDefaultValue(v.getDaoz());
		// ����ԭ�ջ���λ������
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c7);
		c7.setEditable(true);// ���ÿ�����
		String Sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(v.getDiancmc());
		// ����ԭú��λ������
		ComboBox c10 = new ComboBox();
		egu.getColumn("yuanmkdw").setEditor(c10);
		c10.setEditable(true);
		String mkSql1 = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("yuanmkdw").setComboEditor(egu.gridId,
				new IDropDownModel(mkSql1));
		// ����ж����ʽ������
		ComboBox c12 = new ComboBox();
		// c12.setTransform("xiecfsSe");

		String XiecfsSql = "select id,mingc from xiecfsb where diancxxb_id = "
				+ v.getDiancxxb_id() + " order by id";
		c12.setEditable(true);
		egu.getColumn("xiecfsb_id").setEditor(c12);
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(XiecfsSql));
		// ����ú��������
		ComboBox c13 = new ComboBox();
		egu.getColumn("meicb_id").setEditor(c13);
		c1.setEditable(true);
		String meicSql = "select id,mingc from meicb order by mingc";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,
				new IDropDownModel(meicSql));
		egu.getColumn("meicb_id").editor.setAllowBlank(true);
		// �������䵥λ������
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(comb);
		comb.setEditable(true);
		String yunsdwsb = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwsb));
		egu.getColumn("yunsdwb_id").returnId = true;
		egu.getColumn("yunsdwb_id").editor.allowBlank = true;

		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu
				.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});");
		GridButton btnreturn = new GridButton("����",
				"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
		egu.setDefaultsortable(true);// �����Ƿ��ֶο�����

		String color_show = "";
		String s = "";
		String ts = " select * from xitxxb where mingc='��·���������ɫ��ʾ' and zhi='��' and leib='����' and zhuangt=1 and diancxxb_id="
				+ v.getDiancxxb_id();

		ResultSetList rt = con.getResultSetList(ts);

		if (rt.next()) {
			egu.addPaging(0);
			s = " gridDiv_ds.reload();";
			color_show = " colorStr='gridDiv_grid.getView().getRow('+row_num+').style.backgroundColor=\"yellow\";';eval(colorStr);";
			// color_show="
			// gridDiv_grid.getView().addRowClass(row_num,'row_color_show');";
			egu
					.addOtherScript("gridDiv_sm.addListener('rowselect',function(sml,rowIndex,record){row_num=rowIndex;});");

		} else {

		}

		egu.addTbarText("���복�ţ�");
		TextField theKey = new TextField();
		theKey.setId("theKey");
		// System.out.println(theKey);
		theKey
				.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13){chaxun();}}\n");
		egu.addToolbarItem(theKey.getScript());

		GridButton chazhao = new GridButton("��ģ��������/������һ��",
				"function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);

		String otherscript = "var sta='';function chaxun(){\n"
				+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"
				+ "       var len=gridDiv_data.length;\n"
				+ "       var count;\n"
				+ "       if(len%"
				+ egu.getPagSize()
				+ "!=0){\n"
				+ "        count=parseInt(len/"
				+ egu.getPagSize()
				+ ")+1;\n"
				+ "        }else{\n"
				+ "          count=len/"
				+ egu.getPagSize()
				+ ";\n"
				+ "        }\n"
				+ "        for(var i=0;i<count;i++){\n"
				+ "           gridDiv_ds.load({params:{start:i*"
				+ egu.getPagSize()
				+ ", limit:"
				+ egu.getPagSize()
				+ "}});\n"
				+ "           var rec=gridDiv_ds.getRange();\n "
				+ "           for(var j=0;j<rec.length;j++){\n "
				+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
				+ "                 var nw=[rec[j]];\n"
				+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
				+ "                      gridDiv_sm.selectRecords(nw);\n"
				+ "                      sta+=rec[j].get('ID').toString()+';';\n"
				+ "						 theKey.focus(true,true);\n"
				+ "                       return;\n" + "                  }\n"
				+ "                \n" + "               }\n"
				+ "           }\n" + "        }\n" + "        if(sta==''){\n"
				+ "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"
				+ "        }else{\n" + "           sta='';\n"
				+ "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"
				+ "         }\n" + "   }\n";

		if (!s.equals("")) {

			otherscript = "var sta='';function chaxun(){\n"
					+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"
					+ "           var rec=gridDiv_ds.getRange();\n "
					+ "           for(var j=0;j<rec.length;j++){\n "
					+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
					+ "                 var nw=[rec[j]];\n"
					+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
					+ "                      gridDiv_sm.selectRecords(nw);\n"
					+ color_show
					+ "                      gridDiv_grid.getView().focusRow(row_num);\n"
					+ "                      sta[sta.length]=rec[j].get('ID').toString();\n"
					+ "						 theKey.focus(true,true);"
					+ "                      sta+=rec[j].get('ID').toString()+';';\n"
					+ "                       return;\n"
					+ "                  }\n" + "                \n"
					+ "               }\n" + "           }\n" +

					"        if(sta==''){\n"
					+ "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"
					+ "        }else{\n" + "           sta='';\n"
					+ "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"
					+ "         }\n" + "   }\n";
		}
		egu.addOtherScript(otherscript);
		/*
		 * String chaz="function SearCH(field,e){\n"+
		 *  " if(e!=null && e.getCharCode()!=e.ENTER){return;}\n"+ "
		 * if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"+ "
		 * var len=gridDiv_data.length;\n"+ " var count=1;\n"+ // "
		 * if(len%"+egu.getPagSize()+"!=0){\n"+ // "
		 * count=parseInt(len/"+egu.getPagSize()+")+1;\n"+ // " }else{\n"+ // "
		 * count=len/"+egu.getPagSize()+";\n"+ // " }\n"+ " for(var i=0;i<count;i++){\n"+ // "
		 * gridDiv_ds.load({params:{start:i*"+egu.getPagSize()+",
		 * limit:"+egu.getPagSize()+"}});\n"+ " var
		 * rec=gridDiv_ds.getRange();\n"+ " for(var j=0;j<rec.length;j++){\n"+ "
		 * if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+ "
		 * var nw=[rec[j]]\n"+ "
		 * if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+ "
		 * gridDiv_sm.selectRecords(nw);\n"+ "
		 * sta[sta.length]=rec[j].get('ID').toString();\n"+ "
		 * theKey.focus(true,true);"+ " return;\n"+ " }\n"+ " \n"+ " }\n"+ "
		 * }\n"+ " }\n"+ " if(sta.length==0){\n"+ "
		 * Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"+ " }else{\n"+ " sta=new
		 * Array;\n"+ " Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"+ " }\n"+ "
		 * }\n";
		 * 
		 * egu.addOtherScript(chaz);
		 */
		// egu.addTbarText("-");
		// egu.addTbarText("���복�ţ�");
		// TextField theKey=new TextField();
		// theKey.setId("theKey");
		// theKey.setListeners("change:function(thi,newva,oldva){ sta=new
		// Array;}\n");
		// theKey.setListeners("specialkey:function(field,e){SearCH(field,e);}");
		// egu.addToolbarItem(theKey.getScript());
		// GridButton chazhao=new
		// GridButton("��ģ��������/������һ��","function(){SearCH(null,null);}");
		// chazhao.setIcon(SysConstant.Btn_Icon_Search);
		// egu.addTbarBtn(chazhao);
		// egu.addTbarText("-");
		Checkbox cbselectlike = new Checkbox();
		boolean isDefaultChecked = "��".equals(MainGlobal.getXitxx_item("����",
				"�����޸�Ĭ�϶����滻", String.valueOf(v.getDiancxxb_id()), "��"));
		cbselectlike.setChecked(isDefaultChecked);
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("�����滻");

		if (con
				.getHasIt("  select * from xitxxb where mingc='������Ϣ�޸Ŀ�ˮ�����Ƿ���ʾ' and leib='����' and zhi='��' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id())) {
			egu.getColumn("kous").setHidden(false);
			egu.getColumn("kouz").setHidden(false);
		}
		boolean kougFlag = false;
		String kouggs = "KOUD";
		rsl = con
				.getResultSetList(" select zhi from xitxxb where  mingc='����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			kougFlag = true;
			kouggs = rsl.getString("zhi");
		}

		boolean kousFlag = false;
		String kousgs = "KOUS";
		rsl = con
				.getResultSetList(" select zhi from xitxxb where  mingc='��ˮ�ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			kousFlag = true;
			kousgs = rsl.getString("zhi");
		}

		boolean kouzFlag = false;
		String kouzgs = "KOUZ";
		rsl = con
				.getResultSetList(" select zhi from xitxxb where  mingc='�����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			kouzFlag = true;
			kouzgs = rsl.getString("zhi");
		}

		// ����һ�����ƣ�������
		StringBuffer sbjs = new StringBuffer();
		// sbjs.append("gridDiv_grid.on('afteredit',function(e){");
		sbjs
				.append("if("
						+ " e.field=='DIANCXXB_ID')"
						+ "{ "
						+ "var ches =gridDiv_ds.getAt(e.row).get('DIANCXXB_ID')"
						+ ";"
						+
						// "if(e.row==1||e.row==2){gridDiv_ds.getAt(e.row).set('VALUE',Round_new(ches,2));}else{}"+
						"for (var i=0;i<("
						+ coun
						+ "-e.row);i++){ gridDiv_ds.getAt(e.row+i).set('DIANCXXB_ID',ches);}"
						+ "}");
		// sbjs.append("});");
		// egu.addOtherScript(sbjs.toString());

		if (kougFlag || kousFlag || kouzFlag) {
			egu
					.addOtherScript("gridDiv_grid.on('afteredit',function(e){"
							+ ""
							+ " var rec=e.record;\n"
							+

							" if(rec.get('YUNSFSB_ID')==2){\n"
							+ // ֻ֧������

							" var MAOZ=rec.get('MAOZ');\n"
							+ " var PIZ=rec.get('PIZ');\n"
							+ " var KOUD=rec.get('KOUD');\n"
							+ " var KOUS=rec.get('KOUS');\n"
							+ " var KOUZ=rec.get('KOUZ');"
							+

							" if(KOUD==null || KOUD==''){KOUD=0;}\n"
							+ " if(KOUS==null || KOUS==''){KOUS=0;}\n"
							+ " if(KOUZ==null || KOUZ==''){KOUZ=0;}\n"
							+

							"if( e.field=='KOUD' ){\n"
							+ " rec.set('KOUD',Round_new("
							+ kouggs
							+ ",2) );"
							+ "} \n"
							+

							"if( e.field=='KOUS' ){\n"
							+ " rec.set('KOUS',Round_new("
							+ kousgs
							+ ",2));"
							+ "} \n"
							+

							"if( e.field=='KOUZ' ){\n"
							+ " rec.set('KOUZ',Round_new("
							+ kouzgs
							+ ",2));"
							+ "} \n"
							+ ""
							+

							" var bs=rec.get('BEIZ');\n"
							+ " if(bs==null ||  bs==''){\n"
							+ " bs='0,0,0';\n"
							+ " }\n"
							+ " var bssp=bs.split(',');\n"
							+ // ��ʽ koud �� kous �� kouz
							"if(e.field=='KOUD'){bs=KOUD+','+bssp[1]+','+bssp[2];}\n"
							+ "if(e.field=='KOUS'){bs=bssp[0]+','+KOUS+','+bssp[2];}\n"
							+ "if(e.field=='KOUZ'){bs=bssp[0]+','+bssp[1]+','+KOUZ;}\n"
							+

							"rec.set('BEIZ',bs);\n"
							+

							"}\n"
							+

							" if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set(e.field,e.record.get(e.field));}"
							+ sbjs.toString() + "});\n");
			this.setZongkdBfb(true);// �������� �ܿ��� ���� ��ˮ+����+�۶ֵķ�ʽ ������ϵͳԭ����ʽ����

		} else {
			egu
					.addOtherScript("gridDiv_grid.on('afteredit',function(e){ if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set(e.field,e.value);}"
							+ sbjs.toString() + "});\n");
		}

		egu
				.addOtherScript("gridDiv_sm.singleSelect=true; gridDiv_sm.on('rowselect',function(sm,rowIndex,record){row_select=rowIndex;} );");
		if (boo_hejrows) {
			egu
					.addOtherScript(" gridDiv_grid.on('beforeedit',function(e){ if(e.record.get('ID')=='-99') e.cancel=true;  } );");
		}

		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
				"gongysTree", "" + v.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append(
						"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append(
						"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append(
						" if(rec.get('ID')=='-99'){ gongysTree_window.hide();return;} \n")
				.append("if(cks.getDepth() == 4){ \n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.parentNode.text);\n")
				.append(
						"rec.set('MEIKXXB_ID', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('FAZ_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text);\n")
				.append(
						"if(SelectLike.checked){for(i=row_select;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set('GONGYSB_ID',rec.get('GONGYSB_ID'));gridDiv_ds.getAt(i).set('MEIKXXB_ID',rec.get('MEIKXXB_ID'));gridDiv_ds.getAt(i).set('YUANMKDW',rec.get('YUANMKDW'));gridDiv_ds.getAt(i).set('FAZ_ID',rec.get('FAZ_ID'));gridDiv_ds.getAt(i).set('JIHKJB_ID',rec.get('JIHKJB_ID'));}} \n")
				.append("}else if(cks.getDepth() == 3){\n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
				.append("rec.set('YUANMKDW', cks.parentNode.text);\n")
				.append("rec.set('FAZ_ID', cks.text);\n")
				.append(
						"if(SelectLike.checked){for(i=row_select;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set('GONGYSB_ID',rec.get('GONGYSB_ID'));gridDiv_ds.getAt(i).set('MEIKXXB_ID',rec.get('MEIKXXB_ID'));gridDiv_ds.getAt(i).set('YUANMKDW',rec.get('YUANMKDW'));gridDiv_ds.getAt(i).set('FAZ_ID',rec.get('FAZ_ID'));}} \n")
				.append("}else if(cks.getDepth() == 2){\n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.text);\n")
				.append("rec.set('YUANMKDW', cks.text);\n")
				.append(
						"if(SelectLike.checked){for(i=row_select;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set('GONGYSB_ID',rec.get('GONGYSB_ID'));gridDiv_ds.getAt(i).set('MEIKXXB_ID',rec.get('MEIKXXB_ID'));gridDiv_ds.getAt(i).set('YUANMKDW',rec.get('YUANMKDW'));}} \n")
				.append(
						"}else if(cks.getDepth() == 1){  rec.set('GONGYSB_ID', cks.text);\n")
				.append(
						"if(SelectLike.checked){for(i=row_select;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set('GONGYSB_ID',rec.get('GONGYSB_ID'));}}\n")
				.append(" }\n").append("gongysTree_window.hide();\n").append(
						"return;").append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		v.setDefaultTree(dt);
		con.Close();
	}

	private boolean zongkdBfb = false;

	public boolean isZongkdBfb() {
		return zongkdBfb;
	}

	public void setZongkdBfb(boolean zongkdBfb) {
		this.zongkdBfb = zongkdBfb;
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

	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where leix=1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			this.setZongkdBfb(false);
			visit.setList1(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
		}
		getSelectData(visit.getString1());
	}
}