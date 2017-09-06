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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;
/*
 * ���ߣ����
 * ʱ�䣺2012-03-27
 * ��������������ˢ���߼�
 * 		 ����������ú��ʱ���жϣ�����޽��ն˶˿���ֻ���±������ݣ��¼���λ�������ֹ����¡�
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-25
 * �����������������ж��ظ����������Ϊֻ�жϱ����Ƿ��ظ���
 */


public class Meikxxpf_gd extends BasePage implements PageValidateListener {

	// ϵͳ��־���е�״̬�ֶ�
	private static final String ZhangTConstant1 = "�ɹ�";

	private static final String ZhangTConstant2 = "ʧ��";

	// ϵͳ��־���е�����ֶ�
	private static final String leiBConstant = "ú������";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		setToAddMsg(null);
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

	private String DataSource; // ������ú��ҳ����ѡ��ú�����

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	// �����е���ļ�¼ҳ����ѡ��ļ�¼��id
	private String Meikdwid_value; // ������ú��ҳ����ѡ���ú��id

	public String getMeikdwid_value() {
		return Meikdwid_value;
	}

	public void setMeikdwid_value(String meikdwid_value) {
		Meikdwid_value = meikdwid_value;
	}

	private String Meikdqmc_value; // ú���������

	public String getMeikdqbm_value() {
		return Meikdqmc_value;
	}

	public void setMeikdqbm_value(String meikdqbm_value) {
		Meikdqmc_value = meikdqbm_value;
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
	private String RowNumIndex; // ��ѡ���Ҫ����������ú��id

	public String getRowNumIndex() {
		return RowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		RowNumIndex = rowNumIndex;
	}

	private String currentPage = "1"; // ����ҳ����Grid��ǰ��ʾ��ҳ��

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPages) {
		this.currentPage = currentPages;
	}

	private String LiuSBM;

	public String getLiuSBM() {
		return LiuSBM;
	}

	public void setLiuSBM(String liuSBM) {
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
			setCurrentPage("1"); // ��"ˢ��"��ťʱ��Grid��ʾ��1ҳ��
		}
		if (_InsertButtoMK) {
			_InsertButtoMK = false;
			Visit visit = (Visit)getPage().getVisit();
			visit.setString16(RowNumIndex);
			cycle.activate("Meikxx_xzpf_gd");
		}
		if (_ShengcBm) {
			_ShengcBm = false;
			if (saveToPfb()) {
				operate_BM();
			}
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
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm, String operation, String id, String meikdqbm) {
		InterCom_dt dt = new InterCom_dt();

		String sql_diancxxb_id = "select b.diancxxb_id,d.mingc from meikxxbsqpfb b," +
				"diancxxb d where b.diancxxb_id=d.id and b.id="+ id;
		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
		String diancxxb_id = "";
		String diancmc = "";
		if (rsl.next()) {
			diancxxb_id = rsl.getString("DIANCXXB_ID");
			diancmc = rsl.getString("mingc");
		}
//		������ն˶˿���Ϣ��ֻ���¹�˾�����ݿ�
		if (Chk_endpoint(diancxxb_id,con)) {
			return true;
		}
		
		// ���¼��糧��λ����Ҫ����
		boolean t;
		String[] sqls;
		StringBuffer strSql = new StringBuffer();
//		�ڸ���ú����Ϣ���ͬʱ���¹�Ӧ�̱��е���Ϣ
//		����ú����Ϣ��ֻ����SHANGJGSBM�ֶ�
//		��Ӧ�̱��л��轫ú�������������Ӧ��ú�����ID��������Ӧ�̱��е�FUID��
		strSql.append("begin\n");
		strSql.append("update meikxxb set SHANGJGSBM='" + bianm+ "' where id=" + id+";\n");
		
		strSql.append("UPDATE GONGYSB\n" );
		strSql.append("   SET FUID =\n" ); 
		strSql.append("       (SELECT ID\n" ); 
		strSql.append("          FROM MEIKDQB\n" ); 
		strSql.append("         WHERE BIANM = '"+meikdqbm+"'\n" ); 
		strSql.append("           AND ROWNUM = 1),\n" ); 
		strSql.append("           SHANGJGSBM = '" + bianm+ "'\n" ); 
		strSql.append(" WHERE ID = (SELECT MEIKDQ_ID FROM MEIKXXB WHERE ID ="+id+");");
		strSql.append("end;");
		
		sqls = new String[] {strSql.toString() };

		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

		if (answer[0].equals("true")) { // �����¼��ֶγɹ�
			zhuangT = ZhangTConstant1;
			t = true;
		} else {
			zhuangT = ZhangTConstant2;
			t = false;
		}

		logMsg += operation + "ú����Ϣ����"
				+ bianm
				+ "�������¼�" + diancmc + zhuangT;

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
		String tableName = "meikxxb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("");

		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(
				this.getChange());
		while (delrsl.next()) {
			// ɾ��ʱҪ���Ĳ���
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(this.getChange());
		while (mdrsl.next()) {
			sql = new StringBuffer();
			String SaveMsgLocal = "";
			String MK_ID=MainGlobal.getNewID(visit.getDiancxxb_id());
			String GYS_ID=MainGlobal.getNewID(visit.getDiancxxb_id());
			
//			�ж�ʡ���Ƿ���Ч
			long SHENGFB_ID = (getExtGrid().getColumn("SHENGFB_ID").combo).getBeanId(mdrsl.getString("SHENGFB_ID"));
			if (SHENGFB_ID == -1L) {
				SaveMsgLocal += "<br>---ʡ��:"+ mdrsl.getString("SHENGFB_ID")	+ "���ؿ��в�����<br>";
			}
//			�жϼƻ��ھ��Ƿ���Ч
			long JIHKJB_ID = (getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id"));
			if (JIHKJB_ID == -1L) {
				SaveMsgLocal += "<br>---�ƻ��ھ�:"+ mdrsl.getString("JIHKJB_ID")+ "���ؿ��в�����<br>";
			}
			
//			�ж�����Ƿ���Ч
			long LEIB = (getExtGrid().getColumn("LEIB").combo).getBeanId(mdrsl.getString("LEIB"));
			if (LEIB == -1L) {
				SaveMsgLocal += "<br>---���:" + mdrsl.getString("LEIB")+ "���ؿ��в�����<br>";
			}
			
//			�ж������Ƿ���Ч
			long LEIX = (getExtGrid().getColumn("LEIX").combo).getBeanId(mdrsl.getString("LEIX"));
			if (LEIX == -1L) {
				SaveMsgLocal += "<br>---����:" + mdrsl.getString("LEIX")+ "���ؿ��в�����<br>";
			} 
			
//			�жϳ����Ƿ���Ч
			long CHENGSB_ID = (getExtGrid().getColumn("CHENGSB_ID").combo).getBeanId(mdrsl.getString("CHENGSB_ID"));
			if (CHENGSB_ID == -1L) {
				SaveMsgLocal += "<br>---����:"+ mdrsl.getString("CHENGSB_ID")	+ "���ؿ��в�����<br>";
			}
			
//			�ж�ú������Ƿ���Ч
			String MEIKDQBM = (getExtGrid().getColumn("MEIKDQBM").combo).getBeanStrId(mdrsl.getString("MEIKDQBM"));
			String str = "select id from MEIKDQB where bianm = '"+ MEIKDQBM + "'";
			ResultSetList rsl = con.getResultSetList(str);
			long mekdq_id =-1L;
			while (rsl.next()) {
				mekdq_id = rsl.getLong("ID");
			}
			rsl.close();
			if (mekdq_id == -1L) {
				SaveMsgLocal += "<br>---ú�����:"+ mdrsl.getString("MEIKDQBM")+ "���ؿ��в�����<br>";
			}
			
			String bianm = ""; // ѭ��ʱ��ÿ����¼�ı��룬���ݱ����ѯ���Ƿ��ڱ��ؿ����Ѿ�����
			bianm = mdrsl.getString("BIANM");
			
			String mingc = mdrsl.getString("MINGC");
			
			String quanc =  mdrsl.getString("QUANC");
			
			String xuh= mdrsl.getString("XUH");
			
			String piny=mdrsl.getString("PINY");
			
			
			if (!"0".equals(mdrsl.getString("ID"))) { // ���Ĳ�������Ҫ����������ͱ��ؿ��е�ú����Ϣ��
				String sql_check = "select id from " + tableName
						+ " where 1=1 and id=" + Meikdwid_value
						+ " and bianm='" + bianm + "'";
				
				// ����¼����һֱʱ��֪��������¼�ǵ���ģ��������п��ܱ��ֶ��ı��ˣ�����Ҫ�жϱ����Ƿ�һ��
				if (RowNumIndex == null) {
					RowNumIndex = "";
				}
				if (mdrsl.getString("ID").equals(this.RowNumIndex)) {

					if (this.Shujpd(con, sql_check) != 0) { // ˵������һ�£��ڱ��ؿ����Ѿ�����,
						// ֻ�����������״̬���͵��ýӿڣ������¼����ϼ���λ�����ֶμ���
						if (this.GengGSJBM(con, tableName, bianm, "����", mdrsl.getString("ID"), MEIKDQBM)) {
							// �����¼���λ���ϼ���˾�����ֶγɹ�,����������״̬

							String sql3 = " update meikxxbsqpfb set pifzt=1 where id="+ mdrsl.getString("ID");
							con.getUpdate(sql3);
//							������ն˶˿���Ϣ��ֻ���¹�˾�����ݿ�
							if (Chk_endpoint(mdrsl.getString("DCID"),con)) {
								SaveMsg += "<br>----------------������¼-----------<br>---����:"
									+ bianm + "<br>---����:" + mingc + "<br>---ȫ��:" + quanc
									+ "<br>---�ļ�¼���ر���ɹ��������ֶ������¼���λ��Ϣ��<br>";
							}
						} else {
							SaveMsg += "<br>----------------������¼-----------<br>---����:"
									+ bianm
									+ "<br>---����:"
									+ mingc
									+ "<br>---ȫ��:"
									+ quanc + "<br>--�ļ�¼�¼���λ����ʧ��,��������!<br>";
						}
						continue;
					}
				}

//				String sql4 = "select id from meikxxb where bianm='" + bianm+ "' or mingc='" + mingc + "' or quanc='" + quanc + "'";
//				�����������ж��ظ����������Ϊֻ�жϱ����Ƿ��ظ���
				String sql4 = "select id from meikxxb where bianm='" + bianm+"'";
				if (this.Shujpd(con, sql4) != 0) { // �ڱ��ؿ��У�����һ���ĵ�λ,������ʾ��Ϣ

					SaveMsgLocal += "<br>----------------������¼-----------<br>---����:"
							+ bianm + "<br>---����:" + mingc + "<br>---ȫ��:" + quanc
							+ "<br>---�ļ�¼�����ظ�,��������!<br>";
				} else if (SaveMsgLocal.equals("")) { // �ڱ��ؿ��в�����,�жϸ����Ƿ�ɹ�

					if (this.GengGSJBM(con, tableName, bianm, "����", mdrsl.getString("ID"), MEIKDQBM)) {
						// �����¼���λ���ϼ���˾�����ֶγɹ�,����������״̬
						// ������¼���λ���ն˿���ֻ���±�����Ϣ�������뷵��

						// �ȸ���������
						String sql3 = " update meikxxbsqpfb set pifzt=1 where id="+ mdrsl.getString("ID");
						int flag = con.getUpdate(sql3);
						// ��������ĳɹ�����ȥ���ı������ݿ�
						if (flag != 0) {
							sql.append("begin \n");
//							���뵽��Ӧ�̱���
							sql.append("INSERT INTO GONGYSB\n" );
							sql.append("  (ID, FUID, XUH, MINGC, QUANC, PINY, BIANM,SHENGFB_ID, beiz,LEIX, ZHUANGT)\n");
							sql.append("VALUES\n");
							sql.append("  ("+GYS_ID+","+mekdq_id+",'"+xuh+"','"+mingc+"','"+quanc+"','"+piny+"','"+bianm+"'," +
									   "   "+SHENGFB_ID+",'',0,1);\n");
//							���뵽ú����Ϣ����
							sql.append("INSERT INTO MEIKXXB\n" );
							sql.append("  (ID,XUH,BIANM,MINGC,QUANC,PINY,SHENGFB_ID,JIHKJB_ID,CHENGSB_ID,LEIX,LEIB,\n" );
							sql.append("   MEIKDQ_ID,beiz,SHIYZT)\n" ); 
							sql.append("VALUES\n" );
							sql.append("   ("+MK_ID+",'"+xuh+"','"+bianm+"','"+mingc+"','"+quanc+"','"+piny+"'" +
									   ","+SHENGFB_ID+","+JIHKJB_ID+","+CHENGSB_ID+",'"+mdrsl.getString("LEIX")+"','"+mdrsl.getString("LEIB")+"',"+GYS_ID+",'',1);\n");
							sql.append("end;" );
							
							con.getInsert(sql.toString());// ���ı������ݿ�״̬�ɹ�,�ϱ���һ����˾

//							������ն˶˿���Ϣ��ֻ���¹�˾�����ݿ�
							if (Chk_endpoint(mdrsl.getString("DCID"),con)) {
								SaveMsg += "<br>----------------������¼-----------<br>---����:"
									+ bianm + "<br>---����:" + mingc + "<br>---ȫ��:" + quanc
									+ "<br>---�ļ�¼���ر���ɹ��������ֶ������¼���λ��Ϣ��<br>";
							}
						}

					} else {
						SaveMsg += "<br>----------------������¼-----------<br>---����:"
								+ bianm + "<br>---����:" + mingc + "<br>---ȫ��:" + quanc
								+ "<br>---�ļ�¼�¼���λ����ʧ��,��������<br>";
					}
				}
				SaveMsg += SaveMsgLocal;
			}
		}

		this.WriteLog(con);
		if (SaveMsg.equals("")) {

			SaveMsg += "���³ɹ�";
			ToAddMsg = ""; // ���ĺ��ʶ���ϼ������ҳ�治����ʾ
		}
		tiShi = true;
	}
	
	private boolean Chk_endpoint(String diancxxb_id,JDBCcon con){
		String sql=
			"select d.diancxxb_id,z.endpointaddress\n" +
			"from dianczhb d,jiekzhb z\n" + 
			"where d.jiekzhb_id=z.id and d.diancxxb_id="+diancxxb_id;
		ResultSetList rs=con.getResultSetList(sql);
		if(rs.next()){
//			������ն˶˿���Ϊ����������Ϣ������ΪԶ������
			if(rs.getString("endpointaddress")==null||rs.getString("endpointaddress").equals("")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}

	}

	/**
	 * @author yinjm ��ú������ҳ���"���ɱ���"��ťʱ������ѡ��ļ�¼���浽meikxxbsqpfb���У�
	 *         ��У��ú�������ʡ�ݡ����еı��볤�ȣ��Ƿ�ֱ�Ϊ6λ��2λ��2λ��֮���Խ���ѡ��ļ�¼����
	 *         ��meikxxbsqpfb���У�����Ϊ����ú�����ʱ��Ҫ��meikxxbsqpfb���е�ú��������롢ʡ�ݱ��롢
	 *         ���б��뵽meikxxb��ģ����ѯ���ı��롣
	 */
	public boolean saveToPfb() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
				this.getChange());

		while (mdrsl.next()) {

			if (!mdrsl.getString("ID").equals("0")) {
				StringBuffer sql = new StringBuffer();
				ResultSetList rsl;
				String bianm = ""; // ��������ݿ�ȡ���ı���
				sql.append("update meikxxbsqpfb set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if (mdrsl.getColumnNames()[i].equals("MEIKDQBM")) {
						String meikdqbm = String.valueOf((getExtGrid()
								.getColumn("MEIKDQBM").combo).getBeanId(mdrsl
								.getString("MEIKDQBM")));
						if (meikdqbm.length() != 6) {
							this.setMsg("ú������ı��볤�Ȳ�Ϊ6���޷��������ɱ��룡");
							return false;
						}
						sql.append("MEIKDQBM = ").append("'").append(meikdqbm)
								.append("',");
					} else if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {
						String shengfb_id = String.valueOf((getExtGrid()
								.getColumn("SHENGFB_ID").combo).getBeanId(mdrsl
								.getString("SHENGFB_ID")));
						String shengfbm = "select s.bianm from shengfb s where s.id = "
								+ shengfb_id;
						rsl = con.getResultSetList(shengfbm);
						while (rsl.next()) {
							bianm = rsl.getString("BIANM");
						}
						if (bianm.length() != 2) {
							this.setMsg("ʡ�ݵı��볤�Ȳ�Ϊ2���޷��������ɱ��룡");
							return false;
						}
						sql.append("SHENGFB_ID = ").append(shengfb_id).append(
								",");
					} else if (mdrsl.getColumnNames()[i].equals("CHENGSB_ID")) {
						String chengb_id = String.valueOf((getExtGrid()
								.getColumn("CHENGSB_ID").combo).getBeanId(mdrsl
								.getString("CHENGSB_ID")));
						String chengsbm = "select c.bianm from chengsb c where c.id = "
								+ chengb_id;
						rsl = con.getResultSetList(chengsbm);
						while (rsl.next()) {
							bianm = rsl.getString("BIANM");
						}
						if (bianm.length() != 2) {
							this.setMsg("���еı��볤�Ȳ�Ϊ2���޷��������ɱ��룡");
							return false;
						}
						sql.append("CHENGSB_ID = ").append(chengb_id).append(
								",");
					} else if (!mdrsl.getColumnNames()[i].equals("COLOR")
							&&!mdrsl.getColumnNames()[i].equals("DCID")
							&& !mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")
							&& !mdrsl.getColumnNames()[i].equals("BIANM")) {
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id = ").append(mdrsl.getString("ID"))
						.append("\n");
				con.getUpdate(sql.toString());
			}
		}
		mdrsl.close();
		con.Close();
		return true;
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
		} else {
			return value;
		}
	}

	/**
	 * @author yinjm ��createBM()�������صı�����д���ȡ����ˮ�˺Ų��Ҽ�1�����û�а�λ��ʮλ��ô���㡣
	 */
	private void operate_BM() {

		JDBCcon con = new JDBCcon();
		String bianm_value = this.createBM();
		StringBuffer new_bianm = new StringBuffer();
		String liusbmStr = "";

		try {
			int liusbm = Integer.valueOf(bianm_value.substring(6, 9))
					.intValue() + 1;
			if (liusbm < 10) {
				liusbmStr = "00" + liusbm;
			} else if (liusbm < 100 && liusbm >= 10) {
				liusbmStr = "0" + liusbm;
			} else {
				liusbmStr = String.valueOf(liusbm);
			}

			new_bianm.append(bianm_value.substring(0, 6)).append(liusbmStr)
					.append(bianm_value.substring(9));
			AddBM = Long.valueOf(new_bianm.toString()).longValue();
			String sql = "update meikxxbsqpfb set bianm = '"
					+ new_bianm.toString() + "' where id = " + getRowNumIndex();
			con.getUpdate(sql);
		} catch (Exception e) {
			AddBM = -1;
		} finally {
			con.Close();
		}
	}

	/**
	 * @author yinjm
	 * @return ���ش�����ȡ���������� ��ú��������롢ʡ�ݱ��롢���б���ͬʱ��Ϊ��ѯ��������meikxxb����ģ����ѯbianm�ֶΣ�
	 *         ȡ�����ı��룬bianm�ֶι�13λ������ǰ6λΪú��������룬7λ��9λΪ��ˮ�˺ţ�
	 *         10��11λΪʡ�ݱ��룬12��13λΪ���б��룬���û�в�ѯ������bianm�� ��ô��ˮ�˺Ŵ�"001"��ʼ��
	 */
	private String createBM() {

		JDBCcon con = new JDBCcon();

		String strSql = "select max(meikbm.bianm) max_bianm from\n"
				+ "(select mkpf.bianm from meikxxbsqpfb mkpf union select mk.bianm from meikxxb mk) meikbm,\n"
				+ "(select to_char(t.meikdqbm||'___'||t.s_bianm||t.c_bianm) as new_bianm from\n"
				+ "   (select mkpf.meikdqbm, s.bianm s_bianm, c.bianm c_bianm from meikxxbsqpfb mkpf, shengfb s, chengsb c\n"
				+ "  where mkpf.shengfb_id = s.id(+)\n"
				+ "    and mkpf.chengsb_id = c.id(+)\n" + "    and mkpf.id = "
				+ getRowNumIndex() + ") t\n" + ") d\n"
				+ "where meikbm.bianm like d.new_bianm";

		ResultSetList rsl = con.getResultSetList(strSql);
		String bianm_value = "";
		while (rsl.next()) {
			bianm_value = rsl.getString("max_bianm");
		}

		if (bianm_value.equals("") || bianm_value == null) {
			strSql = "select mkpf.meikdqbm||'000'||s.bianm||c.bianm as max_bianm from meikxxbsqpfb mkpf, shengfb s, chengsb c\n"
					+ "where mkpf.shengfb_id = s.id(+)\n"
					+ "  and mkpf.chengsb_id = c.id(+)\n"
					+ "  and mkpf.id = "
					+ getRowNumIndex();
			rsl = con.getResultSetList(strSql);
			while (rsl.next()) {
				bianm_value = rsl.getString("max_bianm");
			}
		}
		rsl.close();
		con.Close();
		return bianm_value;
	}

	public void getSelectData() {
		Visit visit=(Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
			String pfzt = "";
		
		if(getZhuangtValue().getId() == 0){
			pfzt = "AND M.PIFZT = 0";
		}
		if(getZhuangtValue().getId() == 1){
			pfzt = "AND M.PIFZT = 1";
		}
		String sql = "";

		sql = " select distinct  m.id,m.xuh,dc.id dcid, dc.mingc as diancxxb_id,g.DQMC as meikdqbm,\n"
				+ "case\n"
				+ "  when length(m.bianm) < 13 then\n"
				+ "   ''\n"
				+ "  else\n"
				+ "   m.bianm\n"
				+ "end as bianm,\n"
				+ "m.mingc,m.quanc, "
				+ " s.quanc  as shengfb_id, "
				+ "  c.quanc  as chengsb_id, "
				+ " m.leib, m.leix, "
				+ "  j.mingc  as jihkjb_id, "
				+ " m.piny, nvl('<font color=\"green\">����</font>','') as color " 
				+ " from MEIKXXBsqpfb m,diancxxb dc,meikxxb x ,shengfb s,jihkjb j,chengsb c, vwgongysdq g \n"
				+ "		where m.shengfb_id=s.id(+) and m.chengsb_id=c.id(+) and m.jihkjb_id=j.id(+) " 
				+ pfzt+" and m.meikdqbm = g.DQ_BIANM(+) and dc.id=m.diancxxb_id  "
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and m.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid())
				+ " and (m.mingc=x.mingc or m.quanc=x.quanc) "
				+ " union \n"
				+ "       select distinct m.id,m.xuh,dc.id dcid, dc.mingc as diancxxb_id,g.DQMC as meikdqbm,\n"
				+ "case\n"
				+ "  when length(m.bianm) < 13 then\n"
				+ "   ''\n"
				+ "  else\n"
				+ "   m.bianm\n"
				+ "end as bianm,\n"
				+ "m.mingc,m.quanc,"
				+ "     s.quanc  as shengfb_id, "
				+ "     c.quanc  as chengsb_id, "
				+ "  m.leib, m.leix, "
				+ "  j.mingc  as jihkjb_id, "
				+ " m.piny, nvl('<font color=\"red\">δ��</font>','') as color \n" 
				+ " from MEIKXXBsqpfb m,shengfb s,jihkjb j,chengsb c,diancxxb dc, vwgongysdq g \n"
				+ " where m.shengfb_id=s.id(+) and m.chengsb_id=c.id(+) and m.jihkjb_id=j.id(+) \n" 
				+ pfzt+" and m.meikdqbm = g.DQ_BIANM(+) and dc.id=m.diancxxb_id  "
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and m.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid())
				+ " and m.id not in (select m.id  "
				+ "  from MEIKXXBsqpfb m, diancxxb dc, meikxxb x"
				+ "	where dc.id=m.diancxxb_id  "
				+ " and (m.mingc=x.mingc or m.quanc=x.quanc)) order by color desc,DCID,XUH";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("meikxxbsqpfb");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("dcid").setHeader("���뵥λID");
		egu.getColumn("dcid").setWidth(100);
		egu.getColumn("dcid").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("���뵥λ");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("meikdqbm").setHeader("ú�����");
		egu.getColumn("meikdqbm").setWidth(130);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(110);
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(80);
		egu.getColumn("shengfb_id").setHeader("ʡ��");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("chengsb_id").setHeader("����");
		egu.getColumn("chengsb_id").setWidth(80);
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("piny").setHeader("ƴ��");
		egu.getColumn("piny").setWidth(100);
		egu.getColumn("color").setHeader("״̬");
		egu.getColumn("color").setWidth(50);

		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("color").setEditor(null);

		egu.getColumn("shengfb_id").setEditor(new ComboBox());
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		((ComboBox)egu.getColumn("shengfb_id").editor).setEditable(true);
		egu.getColumn("chengsb_id").setEditor(new ComboBox());
		egu.getColumn("chengsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from chengsb"));
		((ComboBox)egu.getColumn("chengsb_id").editor).setEditable(true);
		
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from jihkjb "));

		egu.getColumn("meikdqbm").setEditor(new ComboBox());
		egu.getColumn("meikdqbm").setComboEditor(egu.gridId,new IDropDownModel("SELECT BIANM, MINGC FROM MEIKDQB ORDER BY MINGC"));
		egu.getColumn("meikdqbm").editor.allowBlank = true;

		List l = new ArrayList();
		l.add(new IDropDownBean(1, "ͳ��"));
		l.add(new IDropDownBean(2, "�ط�"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("leib").setReturnId(false);
		egu.getColumn("leib").setDefaultValue("ͳ��");

		List k = new ArrayList();
		k.add(new IDropDownBean(1, "ú"));
		k.add(new IDropDownBean(2, "��"));
		k.add(new IDropDownBean(3, "��"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(k));
		egu.getColumn("leix").setReturnId(false);
		egu.getColumn("leix").setDefaultValue("ú");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setCurrentPage(Integer.parseInt(getCurrentPage()));

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
		
		egu.addTbarBtn(new GridButton("������ú��λ�в�ѯ", "function(){  "
				+ "	if(gridDiv_sm.getSelected()== null){"
				+ "		Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��ú��'); " + "		return;"
				+ "	} " + "	var rec=gridDiv_sm.getSelected(); "
				+ "	document.all.RowNumIndex.value=rec.get('ID');  "
				+ "	document.all.InsertButtoMK.click();" + "}"));

		egu.addTbarText("-");
		egu
				.addTbarBtn(new GridButton(
						"���ɱ���",
						"function(){\n"
								+ "    if(gridDiv_sm.getSelected()== null){\n"
								+ "        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��ú��');\n"
								+ "        return;\n"
								+ "    }\n"
								+ "    if (gridDiv_sm.getSelected().get('MEIKDQBM') == '') {\n"
								+ "       Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú����� ����Ϊ��');return;\n"
								+ "    }\n"
								+ "    if (gridDiv_sm.getSelected().get('SHENGFB_ID') == '') {\n"
								+ "       Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ʡ�� ����Ϊ��');return;\n"
								+ "    }\n"
								+ "    if (gridDiv_sm.getSelected().get('CHENGSB_ID') == '') {\n"
								+ "       Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;\n"
								+ "    }\n"
								+ "    document.all.RowNumIndex.value = gridDiv_sm.getSelected().get('ID');\n"
								+ "    document.all.LiuSBM.value = gridDiv_sm.getSelected().get('BIANM');\n"
								+ "	 var Mrcd = gridDiv_sm.getSelections();"
								+ "for(i = 0; i< Mrcd.length; i++){ "
								+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} "
								+ "if(Mrcd[i].get('LEIB') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��� ����Ϊ��');return;"
								+ "}if(Mrcd[i].get('LEIX') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;"
								+ "}if(Mrcd[i].get('JIHKJB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �ƻ��ھ� ����Ϊ��');return;"
								+ "}gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"
								+ "+ '<XUH update=\"true\">' + Mrcd[i].get('XUH')+ '</XUH>'"
								+ "+ '<DCID update=\"true\">' + Mrcd[i].get('DCID')+ '</DCID>'"
								+ "+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'"
								+ "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"
								+ "+ '<QUANC update=\"true\">' + Mrcd[i].get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"
								+ "+ '<SHENGFB_ID update=\"true\">' + Mrcd[i].get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"
								+ "+ '<CHENGSB_ID update=\"true\">' + Mrcd[i].get('CHENGSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHENGSB_ID>'"
								+ "+ '<LEIB update=\"true\">' + Mrcd[i].get('LEIB').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIB>'"
								+ "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'"
								+ "+ '<LEIX update=\"true\">' + Mrcd[i].get('LEIX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIX>'"
								+ "+ '<JIHKJB_ID update=\"true\">' + Mrcd[i].get('JIHKJB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIHKJB_ID>'"
								+ "+ '<PINY update=\"true\">' + Mrcd[i].get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"
								+ "+ '<MEIKDQBM update=\"true\">' + Mrcd[i].get('MEIKDQBM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKDQBM>'"
								+ "+ '<COLOR update=\"true\">' + Mrcd[i].get('COLOR')+ '</COLOR>'"
								+ " + '</result>' ; }"
								+ "if(gridDiv_history==''){ "
								+ "Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');"
								+ "}else{"
								+ "var Cobj = document.getElementById('CHANGE');"
								+ "Cobj.value = '<result>'+gridDiv_history+'</result>';"
								+ "document.all.ShengcBm.click();\n"
								+ "Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
								+ "}" + "}"));
		egu.addTbarText("-");

		String fn = "function(){"
				+ "var Mrcd = gridDiv_sm.getSelections(); "
				+ "for(i = 0; i< Mrcd.length; i++){ "
				+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} "
				+ "if(Mrcd[i].get('BIANM') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;"
				+ "}if(Mrcd[i].get('LEIB') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��� ����Ϊ��');return;"
				+ "}if(Mrcd[i].get('LEIX') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;"
				+ "}if(Mrcd[i].get('JIHKJB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �ƻ��ھ� ����Ϊ��');return;"
				+ "}gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"
				+ "+ '<XUH update=\"true\">' + Mrcd[i].get('XUH')+ '</XUH>'"
				+ "+ '<DCID update=\"true\">' + Mrcd[i].get('DCID')+ '</DCID>'"
				+ "+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'"
				+ "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"
				+ "+ '<QUANC update=\"true\">' + Mrcd[i].get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"
				+ "+ '<SHENGFB_ID update=\"true\">' + Mrcd[i].get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"
				+ "+ '<CHENGSB_ID update=\"true\">' + Mrcd[i].get('CHENGSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHENGSB_ID>'"
				+ "+ '<LEIB update=\"true\">' + Mrcd[i].get('LEIB').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIB>'"
				+ "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'"
				+ "+ '<LEIX update=\"true\">' + Mrcd[i].get('LEIX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIX>'"
				+ "+ '<JIHKJB_ID update=\"true\">' + Mrcd[i].get('JIHKJB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIHKJB_ID>'"
				+ "+ '<PINY update=\"true\">' + Mrcd[i].get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"
				+ "+ '<MEIKDQBM update=\"true\">' + Mrcd[i].get('MEIKDQBM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKDQBM>'"
				+ "+ '<COLOR update=\"true\">' + Mrcd[i].get('COLOR')+ '</COLOR>'"
				+ " + '</result>' ; }"
				+ "if(gridDiv_history==''){ "
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');"
				+ "}else{"
				+ "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = '<result>'+gridDiv_history+'</result>';"
				+ "document.getElementById('savebutton').click();"
				+ "Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
				+ "}" + "}";
		egu.addTbarBtn(new GridButton("����", fn));

		if (ToAddMsg == null) {
			ToAddMsg = "";
		}

		// �������ѡ��ť�����������ʾ��Ϣ��
		if (ToAddMsg.equals("toAdd")) {
			this.setMsg(visit.getString17());
		}
		// ���������ť�󣬳ɹ�������ʾ��Ϣ
		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('��ʾ��Ϣ',\"" + SaveMsg + "\");";

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
			egu.addOtherScript(" var regexp=new RegExp('^" + RowNumIndex
					+ "$','gi');  "
					+ "var rec_index=gridDiv_ds.find('ID',regexp);"
					+ "gridDiv_ds.getAt(rec_index).set('BIANM','"
					+ String.valueOf(AddBM) + "');");
		}
		AddBM = -2;

		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");
		egu.addOtherScript(" grid=gridDiv_grid;");
		egu
				.addOtherScript("gridDiv_ds.on('load',function(){\n"
						+ "    document.all.currentPage.value = gridDiv_grid.getBottomToolbar().getPageData().activePage;\n"
						+ "});");

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ��ж�������Ǵ�ú��ѡ������ҳ����ת�����ģ���ôGrid��ʾ��1ҳ��
			if (!visit.getActivePageName().toString().equals("Meikxx_Xuanzpf")) {
				setCurrentPage("1");
			}
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString16(null);
			this.setTreeid(null);
			initNavigation();
			setZhuangtValue(null);
			setZhuangtModel(null);

			ToAddMsg = cycle.getRequestContext().getRequest().getParameter(
					"MsgAdd");
			if (ToAddMsg == null) {
				ToAddMsg = "";
			}
			
			DataSource = visit.getString13(); // ú�����
			Meikdwid_value = visit.getString14(); // ����ú��ҳ���е�ú��id
			Meikdqmc_value = visit.getString15(); // ú���������
		}
		
		initNavigation();
		getSelectData();
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

		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	private String treeid;

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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
	
//����״̬������
	
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

}