package com.zhiren.dc.jilgl.shenh;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*����:���ܱ�
 *����:2010-4-26 15:07:46
 *����:�����س�����ʱ����ʾ,ֻ��ʾʱ����
 * 
 */


/*����:���ܱ�
 *ʱ��:2010-4-12 18:25:40
 * �޸�����:�������ʱ,�޸�ë��ʱ,����3λС��
 * 
 */

/*
 * ����:tzf
 * ʱ��:2009-01-05
 * �޸�����:������ͷ�糧Ҫ��  ���Ӷ����滻����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-16
 * ���������ӹ���������Ҳ��ɱ༭
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-16
 * ����������ú��ѡ���ҿ���Ϊ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-01 11��20
 * �������޸�ˢ��ʱδ��������Ӧ��ú�������Ҳ������ʾ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-26 10��19
 * �������޸ĳ���combo��ȡֵ��ʽΪSQLȡֵ
 */
/*
 * ���ߣ�����
 * ʱ��:2009-08-19 11:12
 * ����:���Ӽ�������Ĵ���ť
 */
/*
 * �޸���:tzf
 * ʱ��:2009-07-29
 * ����:���ҹ��ĳ��ż�¼���ú�ɫ��ǳ��������������복�Ų���ʱ���Լ�¼�����ı�ĳ������ȱ���.
 */
/*
 * �޸���:tzf
 * ʱ��:2009-06-19
 * ����:���ӻ�����
 */
/*
 * �޸���:tzf
 * ʱ��:2009-06-16
 * ����:�����˵�¼����Ա��ʾ
 */
/*
 * �޸���:tzf
 * ʱ��:2009-06-11
 * ����:����������ģ����ѯ����,���Ӿ����ֶΣ�������������ֶ�ֵ�ĸı����̬�ı�ֵ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-15 15��18
 * �������޸ķ�����ԭ�ջ���λID����Ϊ�����ӱ��ⷢ����Ѱ������Ƥ���ݵ�����
 */
/*
 * ����:tzf
 * ʱ��:2010-01-25
 * �޸�����:���ݺ���Ҫ��  ������� ����  ģ������ʱ  ��ɫ��ʾ �� ��Ч��һ��,��ѡ��¼ ȫ��������ɫ��ǡ�
 */
/*
 * ����:tzf
 * ʱ��:2010-01-25
 * �޸�����:���ݺ���Ҫ��  ������� ���� ��Ӻ�ɾ�����ܡ�
 */
/*
 * ����:���
 * ʱ��:2012-11-02
 * ������ȡ����ע����ʾ�ͱ��湦�ܡ�
 */
public class Shujshcp extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	public String getFahids() {
		return ((Visit) this.getPage().getVisit()).getString1();
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
			return;
		}
		
		int hedbz = SysConstant.HEDBZ_YJJ;
		if (Shujsh.ALL_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
		} else if (Shujsh.HY_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
		} else if (Shujsh.QY_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
		} else if (Shujsh.PDC_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
		}
		
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		List fhlist = new ArrayList();
		StringBuffer sb = new StringBuffer();
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal
				.getXitxx_item("����", "������㷽��", String.valueOf(visit
						.getDiancxxb_id()), "����"));
		int flag = 0;
		ResultSetList rsdel=this.getExtGrid().getDeleteResultSet(this.getChange());
		
		while(rsdel.next()){//��chep��ɾ��
			String chepbid=rsdel.getString("id");
			
			
			if (chepbid.equals("-1") || chepbid.equals("0")) {
				continue;
			}
			String delSql=" delete from chepb where id="+rsdel.getString("id");
			
			int delflag=con.getDelete(delSql);
			
			if(delflag>=0){
				
				String yuanfhbid=rsdel.getString("fahbid");
				Jilcz.addFahid(fhlist, yuanfhbid);
				
			}else{

				this.setMsg("ɾ����Ƥ��Ϣ����!");
				return;
			}
		
		}
		
		
		// �޸ĳ�Ƥ
		ResultSetList rsld = getExtGrid().getModifyResultSet(getChange());
		while (rsld.next()) {
			String chepid = rsld.getString("id");
			long meicb_id = getExtGrid().getColumn("meicb_id").combo.getBeanId(rsld.getString("meicb_id"));
			meicb_id = meicb_id ==-1 ?0:meicb_id;
			// �ж��Ƿ��� ������ ��ȥ
			if (chepid.equals("-1")) {
				continue;
			}
			
			if(rsld.getString("cheph")==null || rsld.getString("cheph").equals("")){
				this.setMsg("����û����д������������!");
				return;
			}
			
			boolean addChepid=false;
			if(chepid.equals("0")){//����ӵĳ�Ʊ��
				
				chepid=MainGlobal.getNewID(visit.getDiancxxb_id());
				String cheSql=" insert into chepb(id,xuh,cheph,piaojh,yuanmz,maoz,piz,biaoz,yingd,yingk,yuns,koud,kous,kouz," +
						"koum,zongkd,sanfsl,ches,jianjfs,guohb_id,fahb_id,chebb_id,YUANMKDW,YUNSDWB_ID,MEICB_ID,XIECB_ID,DAOZCH," +
						" HEDBZ,LURSJ,LURY" +
						") values("+chepid+",nvl((select count(id) from chepb),1)," +
						"'"+rsld.getString("cheph")+"','',0,"+rsld.getString("maoz")+","+rsld.getString("piz")+"," +rsld.getString("biaoz")+","+
						"0,0,0,0,0,0,0," +rsld.getString("zongkd")+",0,0,'����',(select guohb_id from chepb where fahb_id="+rsld.getString("fahbid")+" and rownum=1 ),"+rsld.getString("fahbid")+",(select CHEBB_ID from chepb where fahb_id="+rsld.getString("fahbid")+" and rownum=1 ),'"+rsld.getString("YUANMKDW")+"',"+
						"(select YUNSDWB_ID from chepb where fahb_id="+rsld.getString("fahbid")+" and rownum=1 ),"+meicb_id+",0,''," +hedbz+",sysdate,'"+rsld.getString("lury")+"')";
				
				int cheflag=con.getInsert(cheSql);
				
				
				if(cheflag>=0){
					addChepid=true;
				}else{

					this.setMsg("��ӳ�Ƥ��Ϣ����!");
					return;
				}
				
			}
			sb.delete(0, sb.length());
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Shulsh, "chepb", chepid);

			if (this.getJingzFlag()) {// ��Ҫ�����ر���С��λ�ֶΣ�kouz�ֶ���Ҫ����
				sb.append("update chepb set koud=" + rsld.getDouble("koud")+", kouz=" + rsld.getDouble("kouz")+",zongkd="+rsld.getDouble("zongkd")
						+ ", biaoz=");
			} else if(this.isZongkdBfb()){//�ٷֱ�¼��,���ӿ�koud��kous��kouz
				sb.append(" update chepb set kouz="+rsld.getDouble("kouz")+",koud="+rsld.getDouble("koud")+",kous="+rsld.getString("kous")
						+",zongkd="+rsld.getDouble("zongkd")+",biaoz=");
				
			}else {
				sb.append("update chepb set biaoz=");
			}

			sb.append(rsld.getDouble("biaoz")).append(",maoz=").append(
					rsld.getDouble("maoz")).append(",piz=").append(
					rsld.getDouble("piz")).append(",cheph='").append(
					rsld.getString("cheph")).append("',daozch='").append(
					rsld.getString("daozch")).append("',sanfsl=").append(rsld.getDouble("sanfsl"));
//			sb.append(",beiz ='").append(rsld.getString("beiz"));
			sb.append(",meicb_id = " + meicb_id);
			sb.append(" where id=").append(chepid);
			flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb);
				setMsg(ErrorMessage.Chepsh001);
				return;
			} else {
				
				// �ж�Ʊ���Ƿ���۶�
				Jilcz.piaozPz(con, chepid, "chepb");
			}
			String yuanfhid = rsld.getString("fahbid");
			sb.delete(0, sb.length());
			sb.append("select f.id fahbid\n");
			sb
					.append("from fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,vwyuanshdw d,\n");
			sb.append("(select id from chezxxb where mingc='").append(
					rsld.getString("faz_id")).append("') fz,\n");
			sb.append("(select id from chezxxb where mingc='").append(
					rsld.getString("daoz_id")).append("') dz,\n");
			;
			sb.append("(select id from chezxxb where mingc='").append(
					rsld.getString("yuandz_id")).append("') yz\n");
			;
			sb.append("where f.fahrq = to_date('").append(
					rsld.getString("fahrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.daohrq = to_date('").append(
					rsld.getString("daohrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.chec ='").append(rsld.getString("chec")).append(
					"' and g.mingc='").append(rsld.getString("gongysb_id"))
					.append("'\n");
			sb.append("and m.mingc ='").append(rsld.getString("meikxxb_id"))
					.append("' and p.mingc = '").append(
							rsld.getString("pinzb_id")).append("'\n");
			sb.append("and j.mingc = '").append(rsld.getString("jihkjb_id"))
					.append("' and d.mingc = '").append(
							rsld.getString("yuanshdwb_id")).append("'\n");
			sb.append(" and f.chex=").append(rsld.getLong("chex")).append("\n");
			sb
					.append("and f.yuandz_id = yz.id and f.faz_id = fz.id and f.daoz_id = dz.id\n");
			sb
					.append("and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id\n");
			sb
					.append(
							"and f.jihkjb_id=j.id and f.yuanshdwb_id=d.id and f.yunsfsb_id = ")
					.append(rsld.getLong("yunsfsb_id"));
			ResultSetList r = con.getResultSetList(sb.toString());
			if (r == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
						+ sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			// /////////
			if (r.next()) {
				if (!yuanfhid.equals(r.getString("fahbid"))) {
					String sql = "update chepb set fahb_id ="
							+ r.getString("fahbid") + " where id=" + chepid;
					flag = con.getUpdate(sql);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail
								+ "SQL:" + sql);
						setMsg(ErrorMessage.Chepsh002);
						return;
					}
					Jilcz.addFahid(fhlist, r.getString("fahbid"));
				}else{
					
					
					if(addChepid){//��ʱ����ӵĳ�Ƥʱ   ���·���
						addChepid=false;
						Jilcz.addFahid(fhlist, yuanfhid);
					}
				}
			} else {
				// ��������
				String newFhid = MainGlobal.getNewID(visit.getDiancxxb_id());

				long meikxxb_id = getExtGrid().getColumn("meikxxb_id").combo
						.getBeanId(rsld.getString("meikxxb_id"));
				long pinzb_id = getExtGrid().getColumn("pinzb_id").combo
						.getBeanId(rsld.getString("pinzb_id"));
				long diancxxb_id = rsld.getInt("diancxxb_id");
				double yunsl = Jilcz.getYunsl(diancxxb_id, pinzb_id, rsld
						.getInt("yunsfsb_id"), meikxxb_id);
				sb.delete(0, sb.length());
				sb.append("insert into fahb (id,");
				sb.append("yuanid, diancxxb_id, gongysb_id, meikxxb_id,");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq,");
				sb.append("daohrq, zhilb_id, yunsfsb_id,");
				sb.append("chec,yuandz_id, yuanshdwb_id, yunsl) values(");
				sb.append(newFhid).append(",").append(newFhid);
				sb.append(",").append(diancxxb_id).append(",");
				sb.append(
						getExtGrid().getColumn("gongysb_id").combo
								.getBeanId(rsld.getString("gongysb_id")))
						.append(",");
				sb.append(meikxxb_id).append(",");
				sb.append(pinzb_id).append(",");
				sb.append(
						getExtGrid().getColumn("faz_id").combo.getBeanId(rsld
								.getString("faz_id"))).append(",");
				sb.append(
						getExtGrid().getColumn("daoz_id").combo.getBeanId(rsld
								.getString("daoz_id"))).append(",");
				sb.append(
						getExtGrid().getColumn("jihkjb_id").combo
								.getBeanId(rsld.getString("jihkjb_id")))
						.append(",");
				sb.append("to_date('").append(rsld.getString("fahrq")).append(
						"','yyyy-mm-dd'),");
				sb.append("to_date('").append(rsld.getString("daohrq")).append(
						"','yyyy-mm-dd'),");
				sb.append(rsld.getInt("zhilb_id")).append(",");
				sb.append(rsld.getLong("yunsfsb_id")).append(",");
				// sb.append(getExtGrid().getColumn("yunsfsb_id").combo.getBeanId(rsld.getString("yunsfsb_id"))).append(",");
				sb.append("'").append(rsld.getString("chec")).append("',");
				sb.append(
						getExtGrid().getColumn("daoz_id").combo.getBeanId(rsld
								.getString("yuandz_id"))).append(",");
				sb.append(
						getExtGrid().getColumn("yuanshdwb_id").combo
								.getBeanId(rsld.getString("yuanshdwb_id")))
						.append(",");
				sb.append(yunsl).append(")");

				flag = con.getInsert(sb.toString());
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+ "SQL:" + sb);
					setMsg(ErrorMessage.Chepsh003);
					return;
				}
				String sql = "update chepb set fahb_id =" + newFhid
						+ " where id=" + chepid;
				flag = con.getUpdate(sql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+ "SQL:" + sql);
					setMsg(ErrorMessage.Chepsh004);
					return;
				}
				Jilcz.addFahid(fhlist, newFhid);

			}
			if (isDancYuns) {
				flag = Jilcz.CountChepbYuns(con, chepid, SysConstant.HEDBZ_YJJ);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chepsh005);
					setMsg(ErrorMessage.Chepsh005);
					return;
				}
			}
			Jilcz.addFahid(fhlist, yuanfhid);
		}

		for (int i = 0; i < fhlist.size(); i++) {

			flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Chepsh006);
				setMsg(ErrorMessage.Chepsh006);
				return;
			}

			flag = Jilcz.updateLieid(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Chepsh007);
				setMsg(ErrorMessage.Chepsh007);
				return;
			}
			if (!isDancYuns) {
				flag = Jilcz.CountFahbYuns(con, (String) fhlist.get(i));
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail);
					setMsg(ErrorMessage.ShujshH002);
					return;
				}
			}
		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		setMsg("����ɹ�");
	}
	
	/**
	 * @author Rock
	 * @since 2009-08-19
	 * @discription �Է��ص����г�Ƥ��������
	 */
	private void Jisys() {
		/*  �鿴��¼�Ƿ��иĶ�  */
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
//		ϵͳVISIT
		Visit v = (Visit) this.getPage().getVisit();
//		���ݿ�����
		JDBCcon con = new JDBCcon();
		/*  �������ݿ�����Ϊ���Զ��ύ  */
		con.setAutoCommit(false);
//		��¼fahID��list
		List fhlist = new ArrayList();
//		�жϼ�¼�Ƿ���ȷ�ı�־
		int flag = -1;
		/*  �ж�����ļ��㷽��  (���������)*/
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal
				.getXitxx_item("����", "������㷽��", String.valueOf(v
						.getDiancxxb_id()), "����"));
		/*  �õ�ҳ�淵�صļ�¼  */
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
//		ѭ����ȡ��Ƥ�ļ�¼
		while(rs.next()){
//			�õ���ǰ��ƤID
			String chepid = rs.getString("id");
//			�ж��Ƿ��ǻ�����������򲻽����κβ���
			if (chepid.equals("-1")) {
				continue;
			}
//			ȡ�øó�Ƥ��Ӧ�ķ���ID
			String yuanfhid = rs.getString("fahbid");
//			�ж��Ƿ��ǵ�����������
			if (isDancYuns) {
//				���������㵥������
				flag = Jilcz.CountChepbYuns(con, chepid, SysConstant.HEDBZ_YJJ);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chepsh005);
					setMsg(ErrorMessage.Chepsh005);
					return;
				}
			}
			Jilcz.addFahid(fhlist, yuanfhid);
		}
		rs.close();
//		ѭ���Ѽ�¼�ķ���ID 			
		for (int i = 0; i < fhlist.size(); i++) {
//			�ж��Ƿ��ǵ�����������
			if (isDancYuns) {
//				����ǵ���������������·���
				flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chepsh006);
					setMsg(ErrorMessage.Chepsh006);
					return;
				}
			}else{
//				����Ƿ����μ������� ����м��㴦��
				flag = Jilcz.CountFahbYuns(con, (String) fhlist.get(i));
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail);
					setMsg(ErrorMessage.ShujshH002);
					return;
				}
			}
		}
		con.commit();
		con.Close();
		setMsg("�������ɹ���");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _JisysChick = false;

	public void JisysButton(IRequestCycle cycle) {
		_JisysChick = true;
	}

	private boolean _FanhClick = false;

	public void FanhButton(IRequestCycle cycle) {
		_FanhClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_JisysChick) {
			_JisysChick = false;
			Jisys();
			getSelectData();
		}
		if (_FanhClick) {
			_FanhClick = false;
			Fanh(cycle);
		}
	}

	// ���ط���
	private void Fanh(IRequestCycle cycle) {
		cycle.activate("Shujsh");
	}

	public String getLeix() {
		return ((Visit) this.getPage().getVisit()).getString10();
	}

	private void setJingzFlag(boolean t) {
		((Visit) this.getPage().getVisit()).setboolean5(t);
	}

	private boolean getJingzFlag() {
		return ((Visit) this.getPage().getVisit()).getboolean5();
	}

	private boolean zongkdBfb=false;
	
	public boolean isZongkdBfb() {
		return zongkdBfb;
	}
	public void setZongkdBfb(boolean zongkdBfb) {
		this.zongkdBfb = zongkdBfb;
	}
	
	public void getSelectData() {
		boolean isshenh = true;
		int hedbz = SysConstant.HEDBZ_YJJ;
		if (Shujsh.ALL_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
			isshenh = false;
		} else if (Shujsh.HY_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
			isshenh = false;
		} else if (Shujsh.QY_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
			isshenh = false;
		} else if (Shujsh.PDC_QXSH.equals(getLeix())) {
			hedbz = SysConstant.HEDBZ_YSH;
			isshenh = false;
		}
		/*
		 * else if(Shujsh.HY_SH.equals(getLeix())) { }else
		 * if(Shujsh.QY_SH.equals(getLeix())) { }else
		 * if(Shujsh.ALL_SH.equals(getLeix())) { }
		 */
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();

		StringBuffer hj = new StringBuffer();
		String huiz = " select * from xitxxb where mingc='���������ʾ����' and zhi='��' and zhuangt=1 and leib='����' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSetList rshj = con.getResultSetList(huiz);

		boolean flag = false;
		String ches = "";
		if (rshj.next()) {
			flag = true;
			ches = " 1 ches,";
		}

		// �Ծ����ֶ�ֵ����ʾ

		String jingz_cons = " (c.maoz-c.piz-c.zongkd) ";// �����ֶ� �õ��� ���㹫ʽ
		String kouz = "";
		String kouzsum = "";
		boolean jingz_flag = false;
		String baol_cu = "-1";
		if (Shujsh.QY_SH.equals(getLeix())) {

			String jingz_sql = " select zhi  from xitxxb where mingc='���������ֶν�ȡ����λ��' and leib='����' and zhuangt=1 and diancxxb_id="
					+ visit.getDiancxxb_id();
			ResultSetList rsl_jz = con.getResultSetList(jingz_sql);

			if (rsl_jz.next()) {// �ж�ϵͳ���� �ֶ� ģʽ
				baol_cu = rsl_jz.getString("zhi");
				jingz_flag = true;
//				jingz_cons = " trunc((c.maoz-c.piz-c.kouz-c.koud)," + baol_cu
//						+ ") ";
				kouz = "  c.kouz kouz,c.koud koud,";
				kouzsum = "sum(kouz) kouz,sum(koud) koud,";
			}
		}
		if (Shujsh.QY_QXSH.equals(getLeix())) {

			String jingz_sql = " select zhi  from xitxxb where mingc='���������ֶν�ȡ����λ��' and leib='����' and zhuangt=1 and diancxxb_id="
					+ visit.getDiancxxb_id();
			ResultSetList rsl_jz = con.getResultSetList(jingz_sql);

			if (rsl_jz.next()) {// �ж�ϵͳ���� �ֶ� ģʽ
				baol_cu = rsl_jz.getString("zhi");
				jingz_flag = true;
//				jingz_cons = " trunc((c.maoz-c.piz-c.kouz-c.koud)," + baol_cu
//						+ ") ";
				kouz = "  c.kouz kouz,c.koud koud,";
				kouzsum = "sum(kouz) kouz,sum(koud) koud,";
			}
		}

		this.setJingzFlag(jingz_flag);
		
		boolean koudShowBoo=false;
		if( (Shujsh.QY_SH.equals(getLeix()) || Shujsh.QY_QXSH.equals(getLeix())) 
				&& con.getHasIt(" select * from xitxxb where mingc='������˰ٷֱ�¼��ʱ��ˮ�����Ƿ���ʾ' and leib='����' and zhi='��' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id())){
			jingz_flag = false;
			this.setJingzFlag(false);
			
			koudShowBoo=true;
			kouz = "  c.kouz kouz,c.koud koud,c.kous kous,";
			kouzsum = "sum(kouz) kouz,sum(koud) koud,sum(kous) kous,";
//			jingz_cons = " (c.maoz-c.piz-c.koud-c.kous-c.kouz)";
			
			
		}

		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select c.id,f.id fahbid,f.diancxxb_id,f.zhilb_id, g.mingc as gongysb_id, m.mingc as meikxxb_id,y.mingc yunsdw,\n")
				.append(
						"f.yunsfsb_id,(select mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n")
				.append(
						"p.mingc as pinzb_id, mc.mingc as meicb_id, fahrq,to_char(c.zhongcsj,'hh24:mi:ss') as zhongcsj, c.cheph, c.maoz, c.piz, c.yuns, c.yingk,c.biaoz, c.sanfsl, c.zongkd,"+ kouz
								+ jingz_cons
								+ " as jingz, "
								+ ches
								+ " j.mingc as jihkjb_id, \n")
				.append("daohrq, chec,zhongchh,cb.mingc as chebb_id,\n")
				.append(
						"(select mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id,\n")
				.append(
						"(select mingc from chezxxb yc where yc.id = f.yuandz_id) yuandz_id,\n")
				// .append("d.mingc as yuanshdwb_id,yuanmkdw,daozch,c.beiz\n")
				.append(
						"d.mingc as yuanshdwb_id,yuanmkdw,daozch,c.lury lury,f.chex"
								+ "\n")
				.append(
						"from chepb c,fahb f ,meicb mc,gongysb g,meikxxb m,pinzb p,jihkjb j,chebb cb,vwyuanshdw d,yunsdwb y\n")
				.append("where f.id in (")
				.append(getFahids())
				.append(")\n")
				.append(
						"and c.fahb_id=f.id and f.gongysb_id=g.id(+) and f.meikxxb_id=m.id(+) and c.yunsdwb_id=y.id(+)\n")
				.append(
						"and c.meicb_id = mc.id(+) and f.pinzb_id=p.id and f.jihkjb_id=j.id and c.chebb_id=cb.id(+) \n")
				.append("and f.yuanshdwb_id=d.id(+)  and c.hedbz=").append(
						hedbz).append("\n");

		if (!flag) {
			sb.append(" order by c.zhongcsj");
		}

		if (flag) {// ���Ӻϼ���
			hj
					.append(
							" select -1 id,-1 fahbid,-1 diancxb_id,-1 zhilb_id,'�ϼ�' gongysb_id,null meikxxb_id,null yunsdw,")
					.append(
							" -1 yunsfsb_id,null faz_id,null pinzb_id, null meicb_id, null fahrq,null zhongcsj,'' cheph, sum(maoz) maoz, sum(piz) piz,")
					.append(
							" sum(yuns) yuns,sum(yingk) yingk,sum(biaoz) biaoz,sum(sanfsl) sanfsl,sum(zongkd) zongd,"+ kouzsum+" sum(jingz) jingz,sum(ches) ches, null jihkjb_id,")
					.append(
							" null daohrq, '' chec,'' zhongchh, null chebb_id ,null daoz_id,null yuandz_id, null yuanshdwb_id, null yuanmkdw,null daozch,null lury,null chex "
									+ " from ("
									+ sb.toString()
									+ " )");

			sb.append(" \n union \n").append(hj.toString()).append(
					" \n order by daohrq");

		}
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ����ÿҳ��ʾ����

		// �ж��Ƿ��ҳ��ʾ
		String fysql = " select zhi from xitxxb where mingc='��������Ƿ��ҳ' and zhuangt=1 and leib='����' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSetList fyrsl = con.getResultSetList(fysql);
		String fyzhi = "��";
		while (fyrsl.next()) {
			fyzhi = fyrsl.getString("zhi");
		}
		fyrsl.close();
		if (fyzhi.equals("��")) {
			egu.addPaging(25);
		} else {
			egu.addPaging(0);
		}

		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("fahbid").editor = null;
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").editor = null;
		egu.getColumn("yunsfsb_id").setHidden(true);
		egu.getColumn("yunsfsb_id").editor = null;
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(110);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(80);
        egu.getColumn("yunsdw").setHeader("���䵥λ");
        egu.getColumn("yunsdw").setWidth(80);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(55);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(55);
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(55);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(77);
		egu.getColumn("zhongcsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(77);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(77);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(45);
		
		
		// egu.getColumn("maoz").editor = null;
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(45);
		// egu.getColumn("piz").editor = null;
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(45);
		egu.getColumn("yingk").setHeader(Locale.yingk_chepb);
		egu.getColumn("yingk").setWidth(45);
		//egu.getColumn("yuns").editor = null;
		egu.getColumn("zongkd").setHeader(Locale.zongkd_chepb);
		egu.getColumn("zongkd").setWidth(45);
		egu.getColumn("zongkd").editor = null;
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(45);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setWidth(45);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(50);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(40);
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongchh").setWidth(77);
		egu.getColumn("zhongchh").setEditor(null);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuandz_id").setHidden(true);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("yuanshdwb_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHidden(true);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("yuanmkdw").setEditor(null);
		egu.getColumn("yuanmkdw").setHidden(true);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("daozch").setWidth(60);

		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setWidth(80);
		
		egu.getColumn("chex").setHeader("����");
		egu.getColumn("chex").setWidth(60);
		egu.getColumn("chex").setHidden(true);

		
//		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
//		egu.getColumn("beiz").setWidth(70);

		egu.getColumn("jingz").setHeader("����");// ������ ���ɱ༭ ��������ֶ�ֵ�ĸı� ���ı�
		NumberField nf = new NumberField();
		nf.setReadOnly(true);
		nf.setDecimalPrecision(3l);
		egu.getColumn("jingz").setEditor(nf);
		egu.getColumn("jingz").setWidth(45);

		if(koudShowBoo){
			egu.getColumn("kouz").setHeader("����");
			egu.getColumn("koud").setHeader("�۶�");
			egu.getColumn("kous").setHeader("��ˮ");
			
			egu.getColumn("kouz").setWidth(45);
			egu.getColumn("koud").setWidth(45);
			egu.getColumn("kous").setWidth(45);
		}
		
		if (flag) {

			egu.getColumn("ches").setHeader("����");
			egu.getColumn("ches").editor = null;
			egu.getColumn("ches").update = false;
		}

		if (jingz_flag) {
			egu.getColumn("kouz").setHeader("����");
			egu.getColumn("koud").setHeader("�۶�");
			egu.getColumn("kouz").setWidth(45);
			egu.getColumn("koud").setWidth(45);
			
		}

		String shifkxg = "��";
		String sql = "select zhi from xitxxb where mingc='��˲����޸�����' "
				+ "and leib='����' and diancxxb_id=" + visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			shifkxg = rs.getString("zhi");
		}
		rs.close();
		

		String  sql_check=" select * from xitxxb  where mingc='���������ʾ�����滻' and zhi='��' and leib='����' and  zhuangt=1 ";
		ResultSetList check=con.getResultSetList(sql_check);
		boolean check_bool=false;
		if(check.next()){
			check_bool=true;
			
		}
		check.close();
		
		if (isshenh && shifkxg.equals("��")) {
			// ����GRID�Ƿ���Ա༭
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);

			// ���û�ë���Ƿ�ɱ༭
			String hcmzkxg = "��";
			sql = "select zhi from xitxxb where mingc='��ë�ؿ��޸�' "
					+ "and leib='����' and zhuangt=1 and diancxxb_id="
					+ visit.getDiancxxb_id();
			rs = con.getResultSetList(sql);
			while (rs.next()) {
				hcmzkxg = rs.getString("zhi");
			}
			rs.close();
			if (Shujsh.HY_SH.equals(getLeix()) && hcmzkxg.equals("��")) {
				egu.getColumn("maoz").editor = null;
			}else{
				//�����ë�ؿ����޸�,����ë�ر���3λС��
				((NumberField)egu.getColumn("maoz").editor).setDecimalPrecision(3);
			}
			// ���û�Ƥ���Ƿ�ɱ༭
			String huocpzkxg = "��";
			sql = "select zhi from xitxxb where mingc='�𳵲����޸�Ƥ��' "
					+ "and leib='����' and diancxxb_id=" + visit.getDiancxxb_id();
			rs = con.getResultSetList(sql);
			while (rs.next()) {
				huocpzkxg = rs.getString("zhi");
			}
			rs.close();
			if (Shujsh.HY_SH.equals(getLeix()) && huocpzkxg.equals("��")) {
				egu.getColumn("piz").editor = null;
			}
			// ��������Ƥ���Ƿ�ɱ༭
			String qcmzkxg = "��";
			sql = "select zhi from xitxxb where mingc='����ë�ؿ��޸�' "
					+ "and leib='����' and zhuangt = 1 and diancxxb_id="
					+ visit.getDiancxxb_id();
			rs = con.getResultSetList(sql);
			while (rs.next()) {
				qcmzkxg = rs.getString("zhi");
			}
			rs.close();
			if (Shujsh.QY_SH.equals(getLeix()) && qcmzkxg.equals("��")) {
				egu.getColumn("maoz").editor = null;
			}
			// ��������Ƥ���Ƿ�ɱ༭
			String qicpzkxg = "��";
			sql = "select zhi from xitxxb where mingc='�������޸�Ƥ��' "
					+ "and leib='����' and diancxxb_id=" + visit.getDiancxxb_id();
			rs = con.getResultSetList(sql);
			while (rs.next()) {
				qicpzkxg = rs.getString("zhi");
			}
			rs.close();
			if (Shujsh.QY_SH.equals(getLeix()) && qicpzkxg.equals("��")) {
				egu.getColumn("piz").editor = null;
			}

			// ���ù�Ӧ��������
			ComboBox c1 = new ComboBox();
			egu.getColumn("gongysb_id").setEditor(c1);
			c1.setEditable(true);
			String gysSql = "select id,mingc from gongysb where leix=1 order by mingc";
			egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
					new IDropDownModel(gysSql));

			// ����ú��λ������
			ComboBox c2 = new ComboBox();
			egu.getColumn("meikxxb_id").setEditor(c2);
			c2.setEditable(true);
			String mkSql = "select id,mingc from meikxxb order by mingc";
			egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(mkSql));

			// ���÷�վ������
			ComboBox c3 = new ComboBox();
			egu.getColumn("faz_id").setEditor(c3);
			c3.setEditable(true);
			String FazSql = "select id,mingc from chezxxb order by mingc";
			egu.getColumn("faz_id").setComboEditor(egu.gridId,
					new IDropDownModel(FazSql));
			// ����Ʒ��������
			ComboBox c4 = new ComboBox();
			egu.getColumn("pinzb_id").setEditor(c4);
			c4.setEditable(true);
			String pinzSql = SysConstant.SQL_Pinz_mei;
			egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
					new IDropDownModel(pinzSql));
			// ���ÿھ�������
			ComboBox c5 = new ComboBox();
			egu.getColumn("jihkjb_id").setEditor(c5);
			c5.setEditable(true);
			String jihkjSql = SysConstant.SQL_Kouj;
			egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
					new IDropDownModel(jihkjSql));

			// ���ó���������
			ComboBox c6 = new ComboBox();
			egu.getColumn("chebb_id").setEditor(c6);
			c6.setEditable(true);
			egu.getColumn("chebb_id").setComboEditor(egu.gridId,
					new IDropDownModel(SysConstant.SQL_Cheb));
			egu.getColumn("chebb_id").setDefaultValue("·��");

			// ���õ�վ������
			ComboBox c7 = new ComboBox();
			egu.getColumn("daoz_id").setEditor(c7);
			c1.setEditable(true);
			String daozSql = "select id,mingc from chezxxb order by mingc";
			egu.getColumn("daoz_id").setComboEditor(egu.gridId,
					new IDropDownModel(daozSql));

			// ����ԭ�ջ���λ������
			ComboBox c8 = new ComboBox();
			egu.getColumn("yuanshdwb_id").setEditor(c8);
			c8.setEditable(true);// ���ÿ�����
			String Sql = "select id,mingc from vwyuanshdw order by mingc";
			egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
					new IDropDownModel(Sql));
			
//			 ����ú��������
			ComboBox c13 = new ComboBox();
			egu.getColumn("meicb_id").setEditor(c13);
			c1.setEditable(true);
			String meicSql = "select id,mingc from meicb order by mingc";
			egu.getColumn("meicb_id").setComboEditor(egu.gridId,
					new IDropDownModel(meicSql));
			egu.getColumn("meicb_id").editor.setAllowBlank(true);

			
			
			String insql="";
			//����  ����  ��Ӻ�ɾ�� ��ť
			
			if(Shujsh.HY_SH.equals(this.getLeix())){
				
				
				egu.getColumn("zongkd").editor = new NumberField();
				
				insql=" select * from xitxxb where mingc='�������������Ӻ�ɾ����ʾ' and leib='����' and zhuangt=1 and zhi='��' and diancxxb_id="+visit.getDiancxxb_id();
				
				
			}
			//����  ����  ��Ӻ�ɾ�� ��ť
			if(Shujsh.QY_SH.equals(this.getLeix())){
				
				
				egu.getColumn("zongkd").editor = new NumberField();
				
				insql=" select * from xitxxb where mingc='�������������Ӻ�ɾ����ʾ' and leib='����' and zhuangt=1 and zhi='��' and diancxxb_id="+visit.getDiancxxb_id();
				
				
			}
			
			if(insql!=null && !insql.equals("")){
				
				
				ResultSetList in=con.getResultSetList(insql);
				if(in.next()){//���  ɾ����ť��ʾ
					
					
					GridButton insertBt=new GridButton("���","function(){\n" +
							" if(gridDiv_ds.getTotalCount()<=0){Ext.Msg.alert('��ʾ��Ϣ','ҳ���޼�¼���޷����!'); return;} \n"+
							" var res=[];\n" +
							" var recc=gridDiv_ds.getAt(0);\n" +
							" res[0]=recc;\n"+
							" gridDiv_ds.insert(0,res);\n" +
							" var rec=gridDiv_ds.getAt(0);\n" +
							" rec.set('ID','0');rec.set('CHEPH','');rec.set('YUNS','0');rec.set('MAOZ','0');rec.set('PIZ','0');rec.set('JINGZ','0');rec.set('BIAOZ','0');rec.set('ZONGKD','0');rec.set('LURY','"+visit.getRenymc()+"');\n"+
							
							"}");
					insertBt.setIcon(SysConstant.Btn_Icon_Insert);
					egu.addTbarBtn(insertBt);
					
					
					egu.addToolbarButton(GridButton.ButtonType_Delete, "");
					
					String check_condi=" ";
					if(check_bool){
						check_condi=" if(e.field!='MAOZ' && e.field!='PIZ' && e.field!='YUNS' && e.field!='BIAOZ' && e.field!='ZONGKD' && e.field!='JINGZ' && e.field!='CHES' && e.field!='CHEPH' ){ \n " +
								" if(!SelectLike.checked){return;} " +
								" for(i=e.row;i<gridDiv_ds.getCount()-1;i++){gridDiv_ds.getAt(i).set(e.field,e.value);} \n " +
								"} \n";
					}
					egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){\n" +
							
							" if(e.field=='MAOZ' || e.field=='PIZ' || e.field=='ZONGKD'){\n" +
							
							" var rec=e.record;\n"+
							"var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ'))-parseFloat(rec.get('ZONGKD')) )+'';\n"+
							
							" rec.set('JINGZ',jin_va);\n"+
							"}\n " +
							
							check_condi+
							
							"\n});");
					
					
					
				}
			
			}
			
			// ���ð�ť
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			sql = " select * from xitxxb where mingc='���������ʾ��������ť' and zhi='��' and beiz ='ʹ��' and zhuangt=1 and leib='����' and diancxxb_id="
				+ visit.getDiancxxb_id();
			if(con.getHasIt(sql)){
				egu.addToolbarButton("��������", GridButton.ButtonType_SaveAll, 
						"JisysButton", null, SysConstant.Btn_Icon_Count);
			}
			// egu.addOtherScript("
			// gridDiv_grid.addListener('afteredit',function(e){
			// if(e.field=='MAOZ' || e.field=='PIZ' || e.field =='ZONGKD'){var
			// rcd=e.record; var
			// va_jz=parseFloat(rcd.get('MAOZ'))-parseFloat(rcd.get('PIZ'))-parseFloat(rcd.get('ZONGKD'));
			// rcd.set('JINGZ',va_jz);} } );");
			
			if( (Shujsh.QY_SH.equals(getLeix()) || Shujsh.QY_QXSH.equals(getLeix()))  ){
				
				boolean kougFlag=false;
				String kouggs="KOUD";
				rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
				if(rsl.next()){
					kougFlag=true;
					kouggs=rsl.getString("zhi");
				}
				
				boolean kousFlag=false;
				String kousgs="KOUS";
				rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='��ˮ�ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
				if(rsl.next()){
					kousFlag=true;
					kousgs=rsl.getString("zhi");
				}
				
				boolean kouzFlag=false;
				String kouzgs="KOUZ";
				rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='�����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
				if(rsl.next()){
					kouzFlag=true;
					kouzgs=rsl.getString("zhi");
				}
				
				
				
				if( koudShowBoo && (kougFlag || kousFlag ||  kouzFlag) ){
					egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){ " +
							
							" var rec=e.record;\n" +
							" var MAOZ=rec.get('MAOZ');\n" +
							" var PIZ=rec.get('PIZ');\n"+
							" var KOUD=rec.get('KOUD');\n" +
							" var KOUS=rec.get('KOUS');\n" +
							" var KOUZ=rec.get('KOUZ');"+
							" if(KOUD==null || KOUD==''){KOUD=0;}\n"+
							" if(KOUS==null || KOUS==''){KOUS=0;}\n"+
							" if(KOUZ==null || KOUZ==''){KOUZ=0;}\n"+
							
							"if( e.field=='KOUD' ){\n" +
								" rec.set('KOUD',Round_new("+kouggs+",2) );"+
							"} \n" +
							
							"if( e.field=='KOUS' ){\n" +
							" rec.set('KOUS',Round_new("+kousgs+",2));"+
							"} \n" +
							
							"if( e.field=='KOUZ' ){\n" +
							" rec.set('KOUZ',Round_new("+kouzgs+",2));"+
							"} \n" +
							"" +
							
//							" var bs=rec.get('BEIZ');\n" +
							" if(bs==null ||  bs==''){\n" +
							" bs='0,0,0';\n"+
							" }\n"+
							" var bssp=bs.split(',');\n"+//  ��ʽ  koud �� kous �� kouz
							"if(e.field=='KOUD'){bs=KOUD+','+bssp[1]+','+bssp[2];}\n" +
							"if(e.field=='KOUS'){bs=bssp[0]+','+KOUS+','+bssp[2];}\n" +
							"if(e.field=='KOUZ'){bs=bssp[0]+','+bssp[1]+','+KOUZ;}\n" +
						
//							"rec.set('BEIZ',bs);\n"+
							
							" rec.set('ZONGKD',parseFloat(rec.get('KOUD'))+parseFloat(rec.get('KOUS'))+parseFloat(rec.get('KOUZ')) );\n" +
							"rec.set('JINGZ',rec.get('MAOZ')-rec.get('PIZ')-rec.get('KOUD')-rec.get('KOUS')-rec.get('KOUZ'));\n" +
							"" +
							"\n});" );
					
					jingz_flag=false;//ֻ����һ��afteredit  ϵͳ������������
					this.setZongkdBfb(true);
				}
				
				
			}

			if (jingz_flag) {
				
				String check_condi=" ";
				if(check_bool){
					check_condi=" if(e.field!='MAOZ' && e.field!='PIZ' && e.field!='YUNS' && e.field!='BIAOZ' && e.field!='ZONGKD' && e.field!='JINGZ' && e.field!='CHES' && e.field!='CHEPH' ){ \n " +
							" if(!SelectLike.checked){return;} " +
							" for(i=e.row;i<gridDiv_ds.getCount()-1;i++){gridDiv_ds.getAt(i).set(e.field,e.value);} \n " +
							"} \n";
				}
				
				
				egu
						.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){ if(e.field=='MAOZ' || e.field=='PIZ'){ var rec=e.record;"
								+ " if("
								+ baol_cu
								+ "<0){ if(true){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ'))-parseFloat(rec.get('ZONGKD')) )+'';"
								+ " rec.set('JINGZ',jin_va);} \n"
								+ " }else{ \n"
								+ " if(true){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ'))-parseFloat(rec.get('KOUD')) )+'';"
								+ " var  koud_va=\"0\";"
								+ "  var s=\"0.\";\n"
								+ " var km=\"\";\n"
								+ " jin_va=jin_va.substring(0,jin_va.lastIndexOf(\".\"))+jin_va.substr(jin_va.lastIndexOf(\".\"),6);\n"
								+ " if(jin_va.lastIndexOf(\".\")>=0){ \n"
								// +"
								// alert(jin_va.substring(jin_va.lastIndexOf(\".\")+1));"

								+ " koud_va=\"0.\";"
								+ " if( parseFloat(rec.get('MAOZ')) <  parseFloat(rec.get('PIZ'))){ \n"
								+ " koud_va=\"-0.\"; \n"
								+ " s=\"-0.\";\n"
								+ " km=\"-\";\n"
								+ " } \n"

								+ " if(jin_va.substring(jin_va.lastIndexOf(\".\")+1).length>"
								+ baol_cu
								+ "){"

								+ " for(var i=0;i<"
								+ baol_cu
								+ ";i++){ \n"
								+ " koud_va+=\"0\"; \n"
								+ " if(i!="
								+ baol_cu
								+ "-1) \n"
								+ " s+=\"0\";\n"
								+ " } \n"

								+ " var num_tem=Math.ceil(parseFloat(\"0.\"+jin_va.substr(jin_va.lastIndexOf(\".\")+1+"
								+ baol_cu
								+ "+1,2))); \n"
								+ " var k_t=parseFloat(jin_va.substr(jin_va.lastIndexOf(\".\")+1+"
								+ baol_cu
								+ ",1))+num_tem; \n"

								// +" alert(num_tem); alert(k_t);"
								+ " if(k_t>9){ s+=\"1\"; koud_va=\"0\"; rec.set('KOUZ',\"0\");} \n"
								+ " else{koud_va+=k_t;rec.set('KOUZ',koud_va); } \n"
								// +" koud_va+=k_t;"

								// +"
								// koud_va+=jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt,4);
								// \n"
								+ " rec.set('KOUZ',parseFloat(koud_va)); \n"
								+ " } \n"
								+ " else{ s=\"0\"; } \n"

								// +"
								// alert(parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")))+'---'+km+'---'+parseFloat(km+\"0\"+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1))+'---'+parseFloat(s));"

								// +"
								// jin_va=jin_va.substring(0,jin_va.lastIndexOf(\".\"))+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1);
								// \n"
								+ "  jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")))+parseFloat(km+\"0\"+jin_va.substr(jin_va.lastIndexOf(\".\"),"
								+ baol_cu
								+ "+1))+parseFloat(s)+\"\"; \n"

								// +" alert(jin_va);"

								+ "  if( jin_va.lastIndexOf(\".\")!=-1 && jin_va.substring(jin_va.lastIndexOf(\".\")).length>"
								+ baol_cu
								+ "+1){ var kl=\"0.\";for(var i=0;i<"
								+ baol_cu
								+ ";i++){kl+=\"0\";} jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")+"
								+ baol_cu
								+ "+1))+Math.ceil(parseFloat(kl+jin_va.substr(jin_va.lastIndexOf(\".\")+"
								+ baol_cu
								+ "+1,2))); } \n"
								+ "  } \n"
								+ " else{rec.set('KOUZ',\"0\"); } \n"
								+ " rec.set('JINGZ',jin_va);  if(koud_va=='0.' || koud_va=='-0.'){koud_va=\"0\";} rec.set('KOUZ',koud_va);  } \n"
								+ " } \n" + "} \n" +
								check_condi +
										"});");

			}

		}
		/*
		 * else { egu.getColumn("gongysb_id").editor = null;
		 * egu.getColumn("meikxxb_id").editor = null;
		 * egu.getColumn("faz_id").editor = null;
		 * egu.getColumn("pinzb_id").editor = null;
		 * egu.getColumn("fahrq").editor = null;
		 * egu.getColumn("zhilb_id").editor = null;
		 * egu.getColumn("zhilb_id").editor = null;
		 * egu.getColumn("zhilb_id").editor = null; }
		 */
		GridButton btnreturn = new GridButton("����",
				"function (){document.getElementById('FanhButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);

		String color_show = "";
		String s = "";
		if (this.getLeix().equals(Shujsh.HY_SH)) {
			String ts = " select * from xitxxb where mingc='��·���������ɫ��ʾ' and zhi='��' and leib='����' and zhuangt=1 and diancxxb_id="
					+ visit.getDiancxxb_id();

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
		} if (this.getLeix().equals(Shujsh.HY_QXSH)) {
				String ts = " select * from xitxxb where mingc='��·���������ɫ��ʾ' and zhi='��' and leib='����' and zhuangt=1 and diancxxb_id="
					+ visit.getDiancxxb_id();
	
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
		}else if (this.getLeix().equals(Shujsh.QY_SH)){
			

			String ts = " select * from xitxxb where mingc='��·���������ɫ��ʾ' and zhi='��' and leib='����' and zhuangt=1 and diancxxb_id="
					+ visit.getDiancxxb_id();

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
				+
				// " sta+=rec[j].get('ID').toString()+';';\n"+
				"                      sta[sta.length]=rec[j].get('ID').toString();\n"
				+ "						 theKey.focus(true,true);"
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
					+

					"           var rec=gridDiv_ds.getRange();\n "
					+ "           for(var j=0;j<rec.length;j++){\n "
					+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
					+ "                 var nw=[rec[j]];\n"
					+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
					+ "                      gridDiv_sm.selectRecords(nw);\n"
					+

					color_show
					
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

		if (flag) {
			egu
					.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){"
							+ "if(e.record.get('ID')=='-1'){"
							+ " e.cancel=true;" + "}" + "});");
		}

		
		
		
		
		
		if(check_bool){

			Checkbox cbselectlike=new Checkbox();
			cbselectlike.setId("SelectLike");
			egu.addToolbarItem(cbselectlike.getScript());
			egu.addTbarText("�����滻");
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
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
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
			this.setZongkdBfb(false);
			visit.setboolean5(false);
			setTbmsg(null);
			getSelectData();
		}
	}
}