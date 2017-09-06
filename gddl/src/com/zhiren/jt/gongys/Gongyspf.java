package com.zhiren.jt.gongys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

/*
 * ���ߣ�����
 * ʱ�䣺2009-12-23
 * �������޸�ҳ�治���иĶ�Ҳ�ɽ�������
 */

/*
 * ���ߣ�������
 * ʱ�䣺2010-03-04
 * ������
 * 	    1���޸�SQL��ѯ��䣬�������ʾú���������⣬������"����"��"δ��"״̬����
 * 		2������ģ����ѯ���ܣ�
 */
/*
 * ���ߣ����	
 * ʱ�䣺2012-04-20
 * ������
 * 	    1���޸����ɱ���ķ�ʽ�����ù�Ӧ�̶�Ӧ��ʡ�ݱ�ID�������ɣ����û��ʡ�ݱ������û��ֶ�ָ��һ��ʡ�ݡ�
 * 		2�����ɱ���ʱ�������û���ǰ̨�������޸Ĳ��������Ǳ��벻���档
 */
/*
 * ���ߣ����	
 * ʱ�䣺2012-06-11
 * ������
 * 	    1���޸Ľ�����ʾ�����
 * 		2�������ֶοɱ༭
 */
/*
 * ���ߣ����	
 * ʱ�䣺2013-11-21
 * ������
 * 	    1���޸Ľ�����ʾ�ظ����ݵĴ���
 * 		2�������ֶ����ɺ���Զ����������ݿ⡣
 */
public class Gongyspf extends BasePage implements PageValidateListener {

	// ϵͳ��־���е�״̬�ֶ�
	private static final String ZhangTConstant1 = "�ɹ�";

	private static final String ZhangTConstant2 = "ʧ��";

	// ϵͳ��־���е�����ֶ�
	private static final String leiBConstant = "��Ӧ������";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
        super.initialize();
        setToAddMsg("");
        msg = "";
    }
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}

	// �����е���ļ�¼ҳ����ѡ��ļ�¼��id
	private String Gongysid_value;

	public String getGongysid_value() {
		return Gongysid_value;
	}

	public void setGongysid_value(String gongysid_value) {
		Gongysid_value = gongysid_value;
	}

	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = saveMsg;
	}

	private boolean tiShi;// ������ʾ��Ϣ�Ƿ���ʾ��

	// ��ѡ��Ҫ���ı���ļ�¼id��
	private String RowNumIndex;

	public String getRowNumIndex() {
		return RowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		RowNumIndex = rowNumIndex;
	}

	// �û������6�������

	private String LiuSBM;

	public String getLiuSBM() {
		return LiuSBM;
	}

	public void setLiuSBM(String liuSBM) {
//		System.out.println("set____");
		LiuSBM = liuSBM;
	}

	private String LiuSBM_Msg;

	public String getLiuSBM_Msg() {
		return LiuSBM_Msg;
	}

	public void setLiuSBM_Msg(String liuSBM_Msg) {
		LiuSBM_Msg = liuSBM_Msg;
	}

	// ȡ�õ���ˮ�����ı���
	private long AddBM = -2;

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _InsertButtoMK = false;

	public void InsertButtoMK(IRequestCycle cycle) {
		_InsertButtoMK = true;

	}

	// ������ˮ����
	private boolean _ShengcBm = false;

	public void ShengcBm(IRequestCycle cycle) {
		_ShengcBm = true;

	}

	// ҳ�����е�λ�仯 �ĵ�λ����
	private String ShenQDW;

	public String getShenQDW() {
		return ShenQDW;
	}

	public void setShenQDW(String shenQDW) {
		ShenQDW = shenQDW;
	}

	// ������ť
	private boolean _ReplyButton = false;

	public void ReplyButton(IRequestCycle cycle) {
		_ReplyButton = true;
	}

	private boolean _SaveButton = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveButton = true;
	}

	public void submit(IRequestCycle cycle) {

		if (_RefreshChick) {
			_RefreshChick = false;
			ToAddMsg = "";
			AddBM = -2;
			// getSelectData();
		}

		if (_InsertButtoMK) {
			_InsertButtoMK = false;
			((Visit)this.getPage().getVisit()).setString15(RowNumIndex);
			cycle.activate("Gongys_Xuanzpf");

		}
		if (_ShengcBm) {
			_ShengcBm = false;
			operate_BM();
		}
		if (_ReplyButton) {
			_ReplyButton = false;
			operation_Reply();
		}
		if (_SaveButton) {
			_SaveButton = false;
			operation_Reply();
		}

	}

	// �����жϣ��õ�����
	private int Shujpd(JDBCcon con, String sql) {
		return JDBCcon.getRow(con.getResultSet(sql));
	}

	// �����¼���λ���ϼ���˾�����ֶη��ر�ʾ
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
			String operation, String id) {

		InterCom_dt dt = new InterCom_dt();
		String sql_diancxxb_id = "select g.diancxxb_id,d.mingc from gongyssqpfb g,diancxxb d where g.diancxxb_id=d.id and g.id="
				+ id;
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
		// Ϊ���ǵ糧��������������
		if (diancxxb_id.equals("266")) {
			// sqls = null;
			sqls = new String[] { "update fahdwb set SHANGJGSBM=" + bianm
					+ " where id=replace(" + id+",266,'')" };
		}else{
			sqls = new String[] { "update gongysb set SHANGJGSBM=" + bianm
					+ " where id=" + id };
		}
		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

		if (answer[0].equals("true")) { // �����¼��ֶγɹ�
			zhuangT = ZhangTConstant1;
			t = true;
		} else {
			zhuangT = ZhangTConstant2;
			t = false;
		}

		logMsg += operation + "��" + tableName + "�б���"
				+ bianm.replaceAll("'", "") + "�����¼�" + diancmc + zhuangT;

		return t;
	}

	// ��־��¼
	private String logMsg = "";

	private String zhuangT = "";

	private void WriteLog(JDBCcon con) {

		Visit visit = (Visit) this.getPage().getVisit();

		if (!logMsg.equals("")) {// ��Ϊ�գ���Ҫд����־��¼

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str = sdf.format(date);
			String sql = " insert into xitrzb(id,diancxxb_id,yonghm,leib,shij,zhuangt,beiz) values("
					+ " getnewid("
					+ visit.getDiancxxb_id()
					+ "),"
					+ visit.getDiancxxb_id()
					+ ",'"
					+ visit.getRenymc()
					+ "','"
					+ leiBConstant
					+ "',to_date('"
					+ date_str
					+ "','YYYY-MM-DD,HH24:mi:ss'),'"
					+ this.zhuangT
					+ "','"
					+ logMsg + "')";

			con.getInsert(sql);

		}
	}

	// ���������Ĳ���
	public void operation_Reply() {
		Visit visit = (Visit) this.getPage().getVisit();
		SaveMsg = "";
		logMsg = "";
		String tableName = "gongysb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("");

		Change = Change.replaceAll("<font color=\"green\">", "");
		Change = Change.replaceAll("<font color=\"red\">", "");
		Change = Change.replaceAll("</font>", "");
		this.setChange(Change);
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		while (mdrsl.next()) {
			if("����".equals(mdrsl.getString("COLOR"))){
				SaveMsg += "�Ѵ��ڹ�Ӧ��\""+mdrsl.getString("MINGC")+"\"������\"���й�Ӧ��\"�в�ѯ����";
			}else{
				StringBuffer sql2 = new StringBuffer();
				sql = new StringBuffer();
				String SaveMsgLocal = "";
				sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
				if (!"0".equals(mdrsl.getString("ID"))) { // ���Ĳ�������Ҫ����������ͱ��ؿ��еĹ�Ӧ�̱�
					String sql_check = "select id from " + tableName
							+ " where 1=1 and id=" + Gongysid_value + " ";
					sql.append("insert into ").append(tableName).append("(id");
					String mingc = "";
					String quanc = "";
					String bianm = ""; // ѭ��ʱ��ÿ����¼�ı��룬���ݱ����ѯ���Ƿ��ڱ��ؿ����Ѿ�����
					// String id = ""; // ѭ��ʱ��ÿ����¼��id�������Gongysid_valueֵһ����
					// ��ȥ�жϱ����Ƿ�һ���������жϲ����ϱ����Ϳ����������ǵ��ϱ����������ڱ��ؿ��б���
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {

						if (!mdrsl.getColumnNames()[i].equals("COLOR")
								&& !mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")) {
							sql.append(",").append(mdrsl.getColumnNames()[i]);
						}

						if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {

							long SHENGFB_ID = (getExtGrid().getColumn(
									mdrsl.getColumnNames()[i]).combo)
									.getBeanId(mdrsl.getString("SHENGFB_ID"));
							if (mdrsl.getString("SHENGFB_ID") == null
									|| mdrsl.getString("SHENGFB_ID").equals("")) {

								sql2.append(",").append("''");
							} else if (SHENGFB_ID == -1) {
								SaveMsgLocal += "--ʡ��:'"
										+ mdrsl.getString("SHENGFB_ID")
										+ "'�ڱ��ؿ��в����ڣ��������!<br>";
								// sql2.append(",").append("''");
							} else {

								sql2.append(",").append(SHENGFB_ID);
							}
							//							
						} else if (mdrsl.getColumnNames()[i].equals("FUID")) {

							long FUID = (getExtGrid().getColumn(
									mdrsl.getColumnNames()[i]).combo)
									.getBeanId(mdrsl.getString("fuid"));
							// �����ڵ��ж�����
							// ���FUID>0��˵�����ڷֹ�˾���ݿ����ҵ����ϼ���λ��
							// ����FUID=-1��˵����
							// 1���ڷֹ�˾���ݿ���û�и��ϼ���λ��
							// 2�������û���ϼ���λ��

							if (FUID > 0) {
								// ���ڷֹ�˾���ݿ����ҵ����ϼ���λ
								sql2.append(",").append(FUID);

							} else if (FUID == -1
									&& mdrsl.getString("fuid").equals("��ѡ��")) {
								// �����û���ϼ���λ��
								sql2.append(",").append(FUID);
							} else {
								// �ڷֹ�˾���ݿ���û�и��ϼ���λ��
								SaveMsgLocal += "--������:" + mdrsl.getString("FUID")
										+ "���ؿ��в�����<br>";
							}

						} else {
							if (mdrsl.getColumnNames()[i].equals("BIANM")) {
								bianm = getExtGrid().getValueSql(
										getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]),
										mdrsl.getString(i));
								sql_check += " and bianm=" + bianm + "";
							}
							if (mdrsl.getColumnNames()[i].equals("MINGC")) {
								mingc = getExtGrid().getValueSql(
										getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]),
										mdrsl.getString(i));
							}
							if (mdrsl.getColumnNames()[i].equals("QUANC")) {
								quanc = getExtGrid().getValueSql(
										getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]),
										mdrsl.getString(i));
							}

							if (!mdrsl.getColumnNames()[i].equals("COLOR")
									&& !mdrsl.getColumnNames()[i]
											.equals("DIANCXXB_ID")) {
								sql2.append(",").append(
										getExtGrid().getValueSql(
												getExtGrid().getColumn(
														mdrsl.getColumnNames()[i]),
												mdrsl.getString(i)));
							}
						}
					}

					// ����¼����һֱʱ��֪��������¼�ǵ���ģ��������п��ܱ��ֶ��ı��ˣ�����Ҫ�жϱ����Ƿ�һ��
					if (RowNumIndex == null) {
						RowNumIndex = "";
					}
					if (mdrsl.getString("ID").equals(this.RowNumIndex)) {
						if (this.Shujpd(con, sql_check) != 0) { // ˵������һ�£��ڱ��ؿ����Ѿ�����,
							if (this.GengGSJBM(con, tableName, bianm, "����", mdrsl
									.getString("ID"))) {// �����¼���λ���ϼ���˾�����ֶγɹ�,����������״̬

								String sql3 = " update gongyssqpfb set pifzt=1  where id="
										+ mdrsl.getString("ID");
								int flag = con.getUpdate(sql3);

								if (flag == 0) {// ���ı������ݿ�״̬���ɹ�

								}
							} else {

								SaveMsg += "----------------������¼-----------" 
										+ "<br>--����:"+ bianm.replaceAll("'", " ")
										+ "<br>---����:"+ mingc.replaceAll("'", " ")
										+ "<br>---ȫ��:"+ quanc.replaceAll("'", " ") 
										+ "<br>--�ļ�¼�¼���λ����ʧ��,��������!<br>";
							}
							continue;
						}
					}
						if (SaveMsgLocal.equals("")) { // �ڱ��ؿ��в�����,�жϸ����Ƿ�ɹ�

						if (this.GengGSJBM(con, tableName, bianm, "����", mdrsl
								.getString("ID"))) {// �����¼���λ���ϼ���˾�����ֶγɹ�,����������״̬

							// �ȸ���������
							String sql3 = " update gongyssqpfb set pifzt=1 where id="
									+ mdrsl.getString("ID");
							int flag = con.getUpdate(sql3);

							// ��������ĳɹ�����ȥ���ı������ݿ�
							if (flag != 0) {

								sql.append(") values(").append(sql2).append(")\n");
								con.getInsert(sql.toString());// ���ı������ݿ�״̬�ɹ�,�ϱ���һ����˾
								// ���ݿ��д������Ѿ��Զ����
							}

						} else {

							SaveMsg += "----------------������¼-----------" +
									"<br>--����:" + bianm.replaceAll("'", " ") + 
									"<br>--����:" + mingc.replaceAll("'", " ") + 
									"<br>--ȫ��:" + quanc.replaceAll("'", " ")+ 
									"<br>--�ļ�¼�¼���λ����ʧ��,��������<br>";
						}
					}

					SaveMsg += SaveMsgLocal;
				}
			}
		}
		this.WriteLog(con);
		if (SaveMsg.equals("")) {

			SaveMsg += "���³ɹ�";
			ToAddMsg = ""; // ���ĺ��ʶ���ϼ������ҳ�治����ʾ
		}
		con.Close();
		tiShi = true;
	}

	// �����ݿ��еõ�����ˮ������в������ж��Ƿ�������Ҫ
	private void operate_BM() {

		String bianm_value = this.createBM();

		try {
			if (bianm_value.length() <= 6) {
				AddBM = 0;
			} else {
				AddBM = Long.valueOf(bianm_value).longValue();
			}
		} catch (Exception e) {
			AddBM = -1;
		}
	}

	// �ӿ���ȡ����ˮ����
	private String createBM() {
		JDBCcon con = new JDBCcon();
		
		Change = Change.replaceAll("<font color=\"green\">", "");
		Change = Change.replaceAll("<font color=\"red\">", "");
		Change = Change.replaceAll("</font>", "");
		this.setChange(Change);
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		long SHENGFB_ID=0;
		
		while(mdrsl.next()){
			SHENGFB_ID = (getExtGrid().getColumn("SHENGFB_ID").combo).getBeanId(mdrsl.getString("SHENGFB_ID"));
//			�����������
			String U_sql="update gongyssqpfb set mingc='"+mdrsl.getString("MINGC")+"',\n" +
					" quanc='"+mdrsl.getString("QUANC")+"',\n" +
					" xuh='"+mdrsl.getString("XUH")+"',\n" +
					" shengfb_id="+SHENGFB_ID+" ,\n" +
					" piny='"+mdrsl.getString("PINY")+"',\n" +
					" danwdz='"+mdrsl.getString("DANWDZ")+"'\n" +
					" where id="+mdrsl.getString("ID");
			con.getUpdate(U_sql);
		}
//		ȡ�ñ���
		String sql =
			"SELECT NVL(MAX(BIANM), '"+SHENGFB_ID+"0') BIANM\n" +
			"  FROM (SELECT BIANM\n" + 
			"          FROM GONGYSB\n" + 
			"         WHERE BIANM LIKE '"+ SHENGFB_ID + "%'\n" + 
			"        UNION (SELECT BIANM\n" + 
			"                FROM GONGYSSQPFB\n" + 
			"               WHERE BIANM LIKE '"+ SHENGFB_ID + "%'\n" + 
			"              MINUS\n" + 
			"              SELECT BIANM FROM GONGYSSQPFB WHERE ID = "+getRowNumIndex()+"))";
 
		ResultSetList rsl = con.getResultSetList(sql);
		String bianm_value = "";
		while (rsl.next()) {
			bianm_value = rsl.getString("bianm");
			int liusbm = Integer.valueOf(bianm_value.substring(7, bianm_value.length())).intValue() + 1;
			if (liusbm < 10) {
				bianm_value = SHENGFB_ID+"0000" + liusbm;
			} else if (liusbm < 100 && liusbm >= 10) {
				bianm_value = SHENGFB_ID+"00" + liusbm;
			} else if (liusbm < 1000 && liusbm >= 100) {
				bianm_value = SHENGFB_ID+"0" + liusbm;
			} else {
				bianm_value = SHENGFB_ID+String.valueOf(liusbm);
			}
		}
		
		sql = "update gongyssqpfb set bianm = '"+ bianm_value + "' where id = " + getRowNumIndex();
		con.getUpdate(sql);
		con.Close();
		return bianm_value;
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		String sql = "";
		sql = "select g.id,\n"
				+ "       g.xuh,\n"
				+ "       dc.mingc as diancxxb_id,\n"
//				+ "case\n"
//				+ "  when length(g.bianm) < 15 then\n"
//				+ "   ''\n"
//				+ "  else\n"
//				+ "   g.bianm\n"
//				+ "end as bianm,\n"
				+ "		  g.bianm as bianm,\n"
				+ "       g.mingc,\n"
				+ "       g.quanc,\n"
				+ "		decode(g.shangjdwmc,null,'��ѡ��',\n"
				+ "             (select mingc from gongysb where bianm=g.shangjdwbm),null,\n"
				+ "             g.shangjdwmc,\n"
				+ "             (select mingc from gongysb where bianm=g.shangjdwbm)\n"
				+ "             ) as fuid,\n"
				+ "       g.danwdz,\n"
				+ "       g.piny,\n"
				+ "       s.quanc as shengfb_id,\n"
				+ "       DECODE(c.id,null,nvl('<font color=\"red\">δ��</font>',''),nvl('<font color=\"green\">����</font>','')) AS color\n"
				+ "  from gongyssqpfb g, diancxxb dc, shengfb s,\n"
				+ "       (select DISTINCT g.id\n"
				+ "        from gongyssqpfb g, diancxxb dc, shengfb s, gongysb gys\n"
				+ "        where g.shengfb_id = s.id\n"
				+ "              and g.diancxxb_id = dc.id\n"
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and g.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid())
				+ "\n"
				+ "              and gys.leix = 1 and (g.mingc = gys.mingc or g.quanc = gys.quanc)) c\n"
				+ "  where g.shengfb_id = s.id(+)\n"
				+ "       and g.diancxxb_id = dc.id\n"
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and g.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid()) + "\n"
				+ "		and g.id=c.id(+)\n " 
			    + "     and g.pifzt = "+getZhuangtValue().getId()
				+ "  order by color desc, xuh";
		// System.out.println(sql);
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("gongyssqpfb");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("diancxxb_id").setHeader("���뵥λ");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(130);
//		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setHeader("���");
		egu.getColumn("mingc").setWidth(90);
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(200);
		egu.getColumn("fuid").setHeader("������");
		egu.getColumn("fuid").setWidth(150);
		egu.getColumn("danwdz").setHeader("��λ��ַ");
		egu.getColumn("danwdz").setWidth(150);
		egu.getColumn("piny").setHeader("ƴ��");
		egu.getColumn("piny").setWidth(50);
		egu.getColumn("shengfb_id").setHeader("ʡ��");
		egu.getColumn("shengfb_id").setWidth(80);
		// egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("color").setHeader("״̬");
		egu.getColumn("color").setWidth(50);
		egu.getColumn("color").setEditor(null);

		egu.getColumn("diancxxb_id").setUpdate(false);
		egu.getColumn("shengfb_id").setEditor(new ComboBox());
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		egu.getColumn("fuid").setEditor(new ComboBox());
		egu.getColumn("fuid").setComboEditor(egu.gridId,new IDropDownModel("select -1 as id,'��ѡ��' as mingc from dual\n"
				+ "union\n" + "select id,mingc from gongysb"));

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		// egu.setWidth(1000);
		egu.setWidth("bodyWidth");
		egu.addTbarText("���뵥λ:");
		if (((Visit)this.getPage().getVisit()).getRenyjb() == 1) {
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Jit_Fengs, ((Visit) this.getPage()
				.getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
		} else {
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
				.getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
		}
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addTbarText("����״̬:");
		ComboBox zhuangt = new ComboBox();
		zhuangt.setTransform("ZhuangtDropDown");
		zhuangt.setId("Zhuangt");
		zhuangt.setLazyRender(true);//��̬��
		zhuangt.setWidth(70);
        egu.addToolbarItem(zhuangt.getScript());
        egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		egu.addTbarText("��Ӧ������:");
		TextField tf = new TextField();
		tf.setId("Gongysmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("ģ����ѯ", "function(){ "
				+ "	var mh_value=Gongysmc.getValue(); "
				+ "	mohcx(mh_value,gridDiv_data,gridDiv_ds);" + "} "));
		egu.addTbarText("-");

		egu.addTbarBtn(new GridButton(
						"�����й�Ӧ���в�ѯ",
						"function(){  if(gridDiv_sm.getSelected()== null){Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ӧ��'); return;} var rec=gridDiv_sm.getSelected();    document.all.RowNumIndex.value=rec.get('ID');  document.all.InsertButtoMK.click();}"));

		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton(
						"���ɱ���",
						"function(){  if(gridDiv_sm.getSelected()== null){Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ӧ��'); return;}\n" +
						" var rec=gridDiv_sm.getSelected(); \n" +
						"if(rec.get('SHENGFB_ID')==''){Ext.MessageBox.alert('��ʾ��Ϣ','��Ӧ������ʡ�ݲ���Ϊ��'); return;}\n" +
						"var Cobj = document.getElementById('CHANGE');\n" +
						"Cobj.value = '<result>'+'<result>' + '<sign>U</sign>'" +
						"+ '<ID update=\"true\">' + rec.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"+
						"+ '<XUH update=\"true\">' + rec.get('XUH')+ '</XUH>'"+
						"+ '<MINGC update=\"true\">' + rec.get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"+
						"+ '<QUANC update=\"true\">' + rec.get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"+
						"+ '<SHENGFB_ID update=\"true\">' + rec.get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"+
						"+ '<PINY update=\"true\">' + rec.get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"+
						"+ '<DANWDZ update=\"true\">' + rec.get('DANWDZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DANWDZ>'"+
						"+ '</result>'+'</result>';\n" +
						" document.all.RowNumIndex.value=gridDiv_sm.getSelected().get('ID');\n"+
						" document.all.ShengcBm.click();}"));

		egu.addTbarText("-");

		String fn = "function(){"
				+ "var Mrcd =  gridDiv_sm.getSelections(); "
				+ "for(i = 0; i< Mrcd.length; i++){ "
				+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} "
				+ "if(Mrcd[i].get('FUID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;}"
				+ "if(Mrcd[i].get('BIANM') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;}"
				+ "if(Mrcd[i].get('SHENGFB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','��Ӧ������ʡ�ݲ���Ϊ��'); return;}"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"
				+ "+ '<XUH update=\"true\">' + Mrcd[i].get('XUH')+ '</XUH>'"
				+ "+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'"
				+ "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"
				+ "+ '<QUANC update=\"true\">' + Mrcd[i].get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"
				+ "+ '<SHENGFB_ID update=\"true\">' + Mrcd[i].get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"
				+ "+ '<FUID update=\"true\">' + Mrcd[i].get('FUID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FUID>'"
				+ "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'"
				+ "+ '<PINY update=\"true\">' + Mrcd[i].get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"
				+ "+ '<DANWDZ update=\"true\">' + Mrcd[i].get('DANWDZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DANWDZ>'"
				+ "+ '<COLOR update=\"true\">' + Mrcd[i].get('COLOR')+ '</COLOR>'"
				+ " + '</result>' ; }"
				+ "if(gridDiv_history==''){ "
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');"
				+ "}else{"
				+ "var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+'</result>';document.getElementById('savebutton').click();Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
				+ "}" + "}";
		egu.addTbarBtn(new GridButton("����", fn));

		if (ToAddMsg == null) {
			ToAddMsg = "";
		}

		// �������ѡ��ť�����������ʾ��Ϣ��
		if (ToAddMsg.equals("toAdd")) {
			this.setMsg(visit.getString14());
		}

		// ���������ť�󣬳ɹ�������ʾ��Ϣ
		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('��ʾ��Ϣ','" + SaveMsg + "');\n";

		} else {
			SaveMsg = "";
		}

		// ��ˮ�������ʾ��Ϣ
		if (AddBM == -1) {
			egu.addOtherScript(" var regexp=new RegExp('^" + RowNumIndex
					+ "$','gi'); "
					+ "var rec_index=gridDiv_ds.find('ID',regexp);"
					+ "gridDiv_ds.getAt(rec_index).set('BIANM','" + LiuSBM
					+ "');");
			LiuSBM_Msg = "Ext.MessageBox.alert('��ʾ��Ϣ','������¼�޷�������ˮ��');";
		} else if (AddBM == -2) {
			LiuSBM_Msg = "";
		} else {
			LiuSBM_Msg = "";
			String s = LiuSBM;
			if (AddBM < 10) { // ��ˮ��Ϊ��������һλ��0
				s += "0";
			}
			s += AddBM;
			egu.addOtherScript(" var regexp=new RegExp('^"
							+ RowNumIndex
							+ "$','gi');\n  var rec_index=gridDiv_ds.find('ID',regexp);gridDiv_ds.getAt(rec_index).set('BIANM','"
							+ s + "');");
		}
		AddBM = -2;
		// egu.addOtherScript(" gridDiv_sm.singleSelect=true; function
		// rowSelectIndex(sml,rowIndex,record
		// ){document.all.RowNumIndex.value=rowIndex;}
		// gridDiv_sm.addListener('rowselect',rowSelectIndex);\n");
		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");
		// egu.addOtherScript(" for(var i=0;i<gridDiv_ds.getCount();i++){var
		// reco=gridDiv_ds.getAt(i); var color_value=reco.get('COLOR');
		// if(color_value=='����'){
		// gridDiv_grid.getView().getCell(i,11).style.backgroundColor='green';}
		// else if(color_value=='δ��'){
		// gridDiv_grid.getView().getCell(i,11).style.backgroundColor='yellow';}}");
		// egu.addOtherScript("gridDiv_ds.addListener('load',function(gridDiv_ds,rds,opts){
		// for(var i=0;i<gridDiv_ds.getCount();i++){var
		// reco=gridDiv_ds.getAt(i); var color_value=reco.get('COLOR');
		// if(color_value==0){
		// gridDiv_grid.getView().getRow(i).style.backgroundColor='green';} else
		// if(color_value==1){
		// gridDiv_grid.getView().getRow(i).style.backgroundColor='yellow';}}
		// });");
		egu.addOtherScript(" grid=gridDiv_grid;");
		// egu.addOtherScript(" grid=gridDiv_grid;");
		// egu.addOtherScript("
		// diancTree_text.addListener('change',diancTree_textChange);");
		// egu.addOtherScript("
		// gridDiv_grid.addListener('sortchange',sortColor);");
		setExtGrid(egu);
		con.Close();

	}

	//״̬������
	public IDropDownBean getZhuangtValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getZhuangtModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setZhuangtValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setZhuangtModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getZhuangtModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getZhuangtModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "δ����"));
		list.add(new IDropDownBean(1,"������"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
		return;
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
			this.setTreeid(null);
			initNavigation();
			ToAddMsg = cycle.getRequestContext().getRequest().getParameter(
					"MsgAdd");
			if (ToAddMsg == null) {
				ToAddMsg = "";
			}

//			this.setDataSource(visit.getString15());
//			Gongysid_value = visit.getString14();
			setZhuangtValue(null);
			setZhuangtModel(null);
		}
		
		// else{
		initNavigation();
		getSelectData();
		// }
	}

	public String getNavigetion() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setNavigetion(String nav) {
		((Visit) this.getPage().getVisit()).setString3(nav);
	}

	public void initNavigation() {
		// Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");

		// ���������Ĳ�ѯSQL
		String sql = "";

		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			sql = "select id,decode(jib,1,'��',mingc) as mingc,jib,fuid,checked from (\n"
					+ "select decode(id,"
					+ getTreeid()
					+ ",0,id) as id,mingc as mingc,\n"
					+ "level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n"
					+ "(select id,mingc,fuid from diancxxb\n"
					+ "union\n"
					+ "select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n"
					+ "start with id ="
					+ getTreeid()
					+ "\n"
					+ "connect by fuid=prior id order SIBLINGS by mingc )";

		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			sql = "select id,decode(jib,1,'��',mingc) as mingc,jib,fuid,checked from (\n"
					+ "select decode(id,"
					+ getTreeid()
					+ ",0,id) as id,mingc as mingc,\n"
					+ "level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n"
					+ "(select id,mingc,fuid,shangjgsid from diancxxb\n"
					+ "union\n"
					+ "select id,xingm as mingc,diancxxb_id as fuid,0 as shangjgsid from lianxrb) a\n"
					+ "start with id ="
					+ getTreeid()
					+ "\n"
					+ "connect by (fuid=prior id or shangjgsid=prior id) order SIBLINGS by mingc )";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			sql = "select id,decode(jib,1,'��',mingc) as mingc,jib,fuid,checked from (\n"
					+ "select decode(id,"
					+ getTreeid()
					+ ",0,id) as id,mingc as mingc,\n"
					+ "level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n"
					+ "(select id,mingc,fuid from diancxxb\n"
					+ "union\n"
					+ "select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n"
					+ "start with id ="
					+ getTreeid()
					+ "\n"
					+ "connect by fuid=prior id order SIBLINGS by mingc )";
		}

		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql, true);
		sql = "select id, mingc, fuid, 0 dc\n" + "  from diancxxb\n"
				+ " where\n" + "  id not in (select distinct diancxxb_id\n"
				+ "  from lianxrb l, diancxxb d\n"
				+ " where l.diancxxb_id = d.id and d.id in (select id\n"
				+ "\t\t\t from(\n" + "\t\t\t select id from diancxxb\n"
				+ "\t\t\t start with id=" + getTreeid() + "\n"
				+ "\t\t\t connect by (fuid=prior id or shangjgsid=prior id)\n"
				+ "\t\t\t )\n" + "\t\t\t union\n" + "\t\t\t select id\n"
				+ "\t\t\t from diancxxb\n" + "\t\t\t where id=" + getTreeid()
				+ "))\n" + " order by fuid desc";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		TreeNode tmp;
		while (rsl.next()) {
			tmp = (TreeNode) node.getNodeById(rsl.getString("id"));
			if (tmp != null && tmp.isLeaf()) {
				tmp.remove();
			}
		}
		rsl.close();
		con.Close();
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
		((Visit) getPage().getVisit()).setString2(treeid);
	}

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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
}
