package com.zhiren.dc.monthReport;

import java.util.Date;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author Rock
 * @since 2009-12-25
 * @version V0.1
 * @discription �±������ú���۵���д �����й����Ƴ���
 */

/*
 * ����:licj
 * ʱ��:2011-04-18
 * �޸�����: �����ӷ�˰(zafs)�ֶΣ��޸Ĳ���˰��ú���۹�ʽ��(�����ۺϼۣ��˼�˰��ú��˰���ӷ�˰)*29.271/��ֵ
 */
/*
 * ���ߣ����پ�
 * ʱ�䣺2010-08-02
 * ��������������ʱ���߼������������ﴦ���߼����жϡ�
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-06-02
 * ��������������ʱ���ϸ����н��㣬�����û�н���ʱ��Ҫ���ۼ�ֵ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-01-16
 * ����������ˢ��ʱ��ʱ��ĸ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-02-03
 * �������޸ı���ʱδ�ж����������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-02-04
 * �������޸ĵ����ۺϼ۹�ʽ     �����ۺϼ� = ú��(��˰) + �����˷� + ��·�˷�(��˰) + ��վ�ӷ� + ��·�ӷ� + ���� 
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ������ȡ��ȫ�������Զ�ˢ�¹��ܣ��û����ֶ����ˢ�°�ť�ſ�ˢ������
 */
/*
 * ���ߣ����
 * ���ڣ�2012-09-07
 * ��������ԭ�н��㵥�۽�����Ϊ�볧��ú�������ɽ���
 * 		 ��д���ɷ���������ԭ��Ϊ������ú���ѽ�����Ϣ�Լ�������úδ������Ϣʹ����ú��������ú������Ȩ�ó���Ӧ��ú�ۣ��˼ۣ������Ӧ�ı�ú������Ϣ��
 * 		 ��д���淽������������г��ۼ���Ϣ���ȫ����Ϣ��Ȼ��ʹ�ñ���ı�����Ϣ�����ۼ�ֵ��
 */

public class Yuejsdj extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
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

	private void Save() {
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = strDate[0] + "��" + strDate[1] + "��";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		String CurrYDate = DateUtil.FormatOracleDate(strDate[0]+"-01-01");
		if(getChange() == null || "".equals(getChange())){
			setMsg("û�н��иĶ����豣�棡");
			return ;
		}
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sql=new StringBuffer();
		sql.append("begin\n");
		while(rs.next()){
			if(!rs.getString("ID").equals("-1")){
				double jiesl = rs.getDouble("jiesl");
				double meij = rs.getDouble("meij");
				double meijs = rs.getDouble("meijs");
				double yunj = rs.getDouble("yunj");
				double yunjs = rs.getDouble("yunjs");
				double daozzf = rs.getDouble("daozzf");
				double zaf = rs.getDouble("zaf");
				double zafs = rs.getDouble("zafs");
				double qit = rs.getDouble("qit");
				double kuangqyf = rs.getDouble("kuangqyf");
				double qnet_ar = rs.getDouble("qnet_ar");
				double biaomdj = rs.getDouble("biaomdj");
				double buhsbmdj = rs.getDouble("buhsbmdj");
				
				sql.append("update yuejsbmdj set " +
						"jiesl = " + jiesl +
						",meij = " + meij + 
						",meijs = " + meijs +
						",yunj = " + yunj +
						",yunjs = " + yunjs +
						",daozzf = " + daozzf +
						",zaf = " + zaf +
						",zafs = " + zafs +
						",qit = " + qit +
						",qnet_ar = " + qnet_ar +
						",biaomdj = " + biaomdj + 
						",buhsbmdj = " + buhsbmdj +
						",kuangqyf = " + kuangqyf +
						" where id =" + rs.getString("id") + ";");
			}
		}
		sql.append("end;\n");
		int flag=con.getUpdate(sql.toString());
		if(flag>-1){
			if(MainGlobal.getXitxx_item("�±�", "�±��볧�����Զ������ۼ�", getTreeid(), "��").equals("��")){
				flag=countLj(CurrYDate,CurrODate);
				rs.close();
				con.Close();
			}
			if(flag>-1){
				setMsg(CurrZnDate+"�����ݱ���ɹ���");
			}else{
				setMsg(CurrZnDate+"�����ݱ���ɹ������ۼƼ��㱣��ʧ�ܣ�");
			}
		}else{
			rs.close();
			con.Close();
			setMsg(CurrZnDate+"�����ݱ���ʧ�ܣ�");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
	}
	
	private void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String strDate [] = this.getRiq2().split("-");
		String CurrZnDate = strDate[0] + "��" + strDate[1] + "��";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-"+ strDate[1] + "-01");
		
		String strSql = "delete from yuejsbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ ")";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}
	
	public void CreateData() {
		/* ����ɾ���������� */
		DelData();
		String diancxxb_id = getTreeid();	//�糧ID
		int Flag = -1;
		JDBCcon con = new JDBCcon();
		
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = strDate[0]+"��"+strDate[1]+"��";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		String CurrYDate = DateUtil.FormatOracleDate(strDate[0]+"-01-01");
		String Riq1=DateUtil.FormatOracleDate(this.getRiq1());
		String Riq2=DateUtil.FormatOracleDate(this.getRiq2());

		int intYuef=0;
		intYuef=Integer.parseInt(strDate[1]);
	
		String meijs = MainGlobal.getXitxx_item("����", "ú��˰��", diancxxb_id, "0.17");
		String yunjs = MainGlobal.getXitxx_item("����", "�˷�˰��", diancxxb_id, "0.07");
		
		String yunj = "decode(nvl(jy.jiessl,0),0,0,round(nvl(jy.guotyf,0)/nvl(j.jiessl,0),2)) as yunj,\n";
		if (MainGlobal.getXitxx_item("����", "�Ƿ����¹���ú����㵥��Ӧ���˷���Ϣ", 
			String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
//			���ڹ��������糧��ú����˷��Ƿֿ�����ģ����ҿ���һ��ú����㵥��Ӧ������䵥λ����ô�����ж���˷ѽ��㵥��
//			��������Ƚ����⣬������Ҫ�ڴ����¹������˷���Ϣ
			yunj = "decode(getYunfxx4Jiesbid(j.id, 'guotyf'), 0, 0, round(getYunfxx4Jiesbid(j.id, 'guotyf') / nvl(j.jiessl, 0), 2)) as yunj,\n";
		}
		
		String InsertSql=
		"INSERT INTO YUEJSBMDJ (ID,YUETJKJB_ID,FENX,JIESL,MEIJ,MEIJS,YUNJ,YUNJS,ZAF,QNET_AR,BIAOMDJ,BUHSBMDJ)\n" + 
		"SELECT getnewid(" + diancxxb_id + "),ID,FENX,JIESL,MEIJ,MEIJS,YUNJ,YUNJS,ZAF,QNET_AR,\n" + 
		"      ROUND(DECODE(QNET_AR, 0, 0, (MEIJ + YUNJ + ZAF) * 29.271 / QNET_AR),2) BIAOMDJ,\n" + 
		"      ROUND(DECODE(QNET_AR,0,0,(MEIJ - MEIJS + YUNJ - YUNJS + ZAF) * 29.271 / QNET_AR),2) BUHSBMDJ\n" + 
		"FROM (SELECT T.ID,\n" + 
		"       NVL(SR.JIESL,0)JIESL,\n" + 
		"       NVL(SR.MEIJ,0)MEIJ,\n" + 
		"       NVL(ROUND(SR.MEIJ - SR.MEIJ / (1+"+meijs+"), 2),0) MEIJS,\n" + 
		"       NVL(SR.YUNJ,0)YUNJ,\n" + 
		"       NVL(ROUND(SR.YUNJ * "+yunjs+", 2),0) YUNJS,\n" + 
		"       NVL(SR.ZAF,0)ZAF,\n" + 
		"       NVL(SR.QNET_AR,0)QNET_AR\n" + 
		"  FROM (SELECT GONGYSB_ID,\n" + 
		"               JIHKJB_ID,\n" + 
		"               PINZB_ID,\n" + 
		"               YUNSFSB_ID,\n" + 
		"               ROUND(SUM(JIESL), 0) JIESL,\n" + 
		"               ROUND(DECODE(SUM(JIESL), 0, 0, SUM(JIESL * MEIJ) / SUM(JIESL)),2) MEIJ,\n" + 
		"               ROUND(DECODE(SUM(JIESL), 0, 0, SUM(JIESL * YUNJ) / SUM(JIESL)),2) YUNJ,\n" + 
		"               ROUND(DECODE(SUM(JIESL), 0, 0, SUM(JIESL * ZAF) / SUM(JIESL)),2) ZAF,\n" + 
		"               ROUND(DECODE(SUM(JIESL),0,0,SUM(JIESL * QNET_AR) / SUM(JIESL)),2) QNET_AR\n" + 
		"          FROM (SELECT GONGYSB_ID,\n" + 
		"                       PINZB_ID,\n" + 
		"                       JIHKJB_ID,\n" + 
		"                       YUNSFSB_ID,\n" + 
		"                       JIESL,\n" + 
		"                       MEIJ,\n" + 
		"                       YUNJ,\n" + 
		"                       ZAF,\n" + 
		"                       ZLQNET_AR QNET_AR\n" + 
		"                  FROM (SELECT GYS.ID GONGYSB_ID,\n" + 
		"                               F.MEIKXXB_ID,\n" + 
		"                               F.PINZB_ID,\n" + 
		"                               F.JIHKJB_ID,\n" + 
		"                               F.YUNSFSB_ID,\n" + 
		"                               F.JINGZ AS JIESL,\n" + 
		"                              DECODE(J.JIESSL,0,0,ROUND(J.HANSMK / J.JIESSL, 2)) AS MEIJ,\n" + 
									   yunj+
		"                              DECODE(NVL(JY.JIESSL, 0),0,0,ROUND((NVL(JY.GUOTZF, 0) +NVL(JY.KUANGQZF, 0)) /NVL(J.JIESSL, 0),2)) AS ZAF,\n" + 
		"                              DECODE(J.JIESSL,0,0,ROUND(J.JIESSL * J.JIESRL * 0.0041816 /J.JIESSL,3)) AS QNET_AR,\n" + 
		"                               Z.QNET_AR ZLQNET_AR\n" + 
		"                          FROM JIESB   J,\n" + 
		"                               JIESYFB JY,\n" + 
		"                               FAHB    F,\n" + 
		"                               ZHILB   Z,\n" + 
		"                               MEIKXXB MK,\n" + 
		"                               GONGYSB GYS\n" + 
		"                         WHERE J.ID = F.JIESB_ID\n" + 
		"                           AND F.MEIKXXB_ID = MK.ID\n" + 
		"                           AND MK.MEIKDQ_ID = GYS.ID\n" + 
		"                           AND F.ZHILB_ID = Z.ID\n" + 
		"                           AND J.ID = JY.DIANCJSMKB_ID(+)\n" + 
		"                           AND F.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                           AND J.RUZRQ BETWEEN "+Riq1+" AND "+Riq2+"\n" + 
		"                           AND F.DAOHRQ BETWEEN "+Riq1+" AND "+Riq2+")\n" + 
		"                UNION\n" + 
		"                SELECT GYS.ID       GONGYSB_ID,\n" + 
		"                       F.PINZB_ID,\n" + 
		"                       F.JIHKJB_ID,\n" + 
		"                       F.YUNSFSB_ID,\n" + 
		"                       F.JINGZ      AS JIESL,\n" + 
		"                       HC.MEIJ,\n" + 
		"                       HC.YUNJ,\n" + 
		"                       0            ZAF,\n" + 
		"                       Z.QNET_AR\n" + 
		"                  FROM FAHB F, ZHILB Z, SHOUHCFKB HC, MEIKXXB MK, GONGYSB GYS\n" + 
		"                 WHERE F.JIESB_ID = 0\n" + 
		"                   AND F.MEIKXXB_ID = MK.ID\n" + 
		"                   AND MK.MEIKDQ_ID = GYS.ID\n" + 
		"                   AND F.ZHILB_ID = Z.ID\n" + 
		"                   AND F.GONGYSB_ID = HC.GONGYSB_ID\n" + 
		"                   AND F.MEIKXXB_ID = HC.MEIKXXB_ID\n" + 
		"                   AND F.PINZB_ID = HC.PINZB_ID\n" + 
		"                   AND F.JIHKJB_ID = HC.JIHKJB_ID\n" + 
		"                   AND F.DIANCXXB_ID = HC.DIANCXXB_ID\n" + 
		"                   AND F.DAOHRQ = HC.RIQ\n" + 
		"                   AND F.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                   AND F.DAOHRQ BETWEEN "+Riq1+" AND "+Riq2+")\n" + 
		"         GROUP BY (GONGYSB_ID, JIHKJB_ID, PINZB_ID, YUNSFSB_ID)) SR,\n" + 
		"       YUETJKJB T\n" + 
		" WHERE T.GONGYSB_ID = SR.GONGYSB_ID(+)\n" + 
		"   AND T.JIHKJB_ID = SR.JIHKJB_ID(+)\n" + 
		"   AND T.PINZB_ID = SR.PINZB_ID(+)\n" + 
		"   AND T.YUNSFSB_ID = SR.YUNSFSB_ID(+)\n" + 
		"   AND T.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"   AND T.RIQ = "+CurrODate+")SR,(SELECT NVL('����', '') FENX\n" + 
		"  FROM DUAL\n" + 
		"UNION\n" + 
		"SELECT NVL('�ۼ�', '') FENX FROM DUAL)\n";

		Flag=con.getInsert(InsertSql);
		con.Close();
		
//		�������һ�·����ݣ���ôϵͳ�Զ������ۼ�ֵ��
		if(intYuef != 1){	
			Flag=countLj(CurrYDate,CurrODate);
		}
		
		if(Flag>-1){
			setMsg(CurrZnDate+"�����ݳɹ����ɣ�");
		}else{
			setMsg(CurrZnDate+"����������ʧ�ܣ�");
		}
		con.Close();
	}
	
	private int countLj(String CurrYDate,String CurrODate){
		JDBCcon con=new JDBCcon();
		StringBuffer sql = new StringBuffer();
		
		String sqlsr = 
			"SELECT YT2.ID,\n" +
			"ROUND_NEW(SUM(JIESL), 0) AS JIESL,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(MEIJ * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(MEIJS * JIESL) / SUM(JIESL)),2) MEIJS,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(YUNJ * JIESL) / SUM(JIESL)),2) YUNJ,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(YUNJS * JIESL) / SUM(JIESL)),2) YUNJS,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(DAOZZF * JIESL) / SUM(JIESL)),2) DAOZZF,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(ZAF * JIESL) / SUM(JIESL)), 2) ZAF,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(ZAFS * JIESL) / SUM(JIESL)),2) ZAFS,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(QIT * JIESL) / SUM(JIESL)), 2) QIT,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(QNET_AR * JIESL) / SUM(JIESL)),2) QNET_AR,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(BIAOMDJ * JIESL) / SUM(JIESL)),2) BIAOMDJ,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL),0,0,SUM(BUHSBMDJ * JIESL) / SUM(JIESL)),2) BUHSBMDJ,\n" + 
			"ROUND_NEW(DECODE(SUM(JIESL),0,0,SUM(KUANGQYF * JIESL) / SUM(JIESL)),2) KUANGQYF\n" + 
			"  FROM YUEJSBMDJ Y,\n" + 
			"       YUETJKJB YT,\n" + 
			"       (SELECT ID, GONGYSB_ID, JIHKJB_ID, PINZB_ID, YUNSFSB_ID\n" + 
			"          FROM YUETJKJB\n" + 
			"         WHERE DIANCXXB_ID = "+getTreeid()+"\n" + 
			"           AND RIQ = "+CurrODate+") YT2\n" + 
			" WHERE Y.YUETJKJB_ID = YT.ID\n" + 
			"   AND YT.GONGYSB_ID = YT2.GONGYSB_ID\n" + 
			"   AND YT.JIHKJB_ID = YT2.JIHKJB_ID\n" + 
			"   AND YT.PINZB_ID = YT2.PINZB_ID\n" + 
			"   AND YT.YUNSFSB_ID = YT2.YUNSFSB_ID\n" + 
			"   AND YT.RIQ >= "+CurrYDate+"\n" + 
			"   AND YT.RIQ <= "+CurrODate+"\n" + 
			"   AND Y.FENX = '" + SysConstant.Fenx_Beny + "'\n" + 
			"   AND YT.DIANCXXB_ID = "+getTreeid()+"\n" + 
			" GROUP BY YT2.ID";

		ResultSetList rsl = con.getResultSetList(sqlsr);
		while(rsl.next()){
				double daoczhj = rsl.getDouble("meij") + rsl.getDouble("yunj") + rsl.getDouble("daozzf") + rsl.getDouble("zaf") + rsl.getDouble("qit") + rsl.getDouble("kuangqyf");
				double biaomdj = 0;
				double buhsbmdj = 0;
				
				if(rsl.getDouble("qnet_ar")!=0){
					biaomdj = daoczhj*29.271/rsl.getDouble("qnet_ar");
					buhsbmdj = (daoczhj-rsl.getDouble("meijs")-rsl.getDouble("yunjs")-rsl.getDouble("zafs"))*29.271/rsl.getDouble("qnet_ar");
				}
				
				sql.append("update yuejsbmdj set " +
						"jiesl = " + rsl.getDouble("jiesl") +
						",meij = " + rsl.getDouble("meij") + 
						",meijs = " + rsl.getDouble("meijs") +
						",yunj = " + rsl.getDouble("yunj") +
						",yunjs = " + rsl.getDouble("yunjs") +
						",daozzf = " + rsl.getDouble("daozzf") +
						",zaf = " + rsl.getDouble("zaf") +
						",zafs = " + rsl.getDouble("zafs") +
						",qit = " + rsl.getDouble("qit") +
						",qnet_ar = " + rsl.getDouble("qnet_ar") +
						",biaomdj = round_new("+ biaomdj + ",2)" +
						",buhsbmdj = round_new("+ buhsbmdj + ",2)" +
						",kuangqyf = " + rsl.getDouble("kuangqyf") +
						" where yuetjkjb_id =" + rsl.getString("id") + " and fenx = '" + SysConstant.Fenx_Leij + "';");
		}
		int flag = con.getUpdate("begin\n"+sql.toString() + "\n end;");
		rsl.close();
		con.Close();
		return flag;
	}

	/**
	 * @param con
	 * @return   true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
	 */
	private boolean getZhangt(JDBCcon con){
		String CurrODate = "";
		String sj[] = this.getRiq2().split("-");
			CurrODate=DateUtil.FormatOracleDate(sj[0] + "-" + sj[1]+ "-01");

		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from yuejsbmdj s, yuetjkjb k\n" + 
			" where s.yuetjkjb_id = k.id\n" + 
			"   and k.diancxxb_id = "+getTreeid()+"\n" + 
			"   and k.riq = "+CurrODate;
		ResultSetList rs=con.getResultSetList(sql);
		boolean zt=true;
		if(con.getHasIt(sql)){
			while(rs.next()){
				if(rs.getInt("zhuangt")==0||rs.getInt("zhuangt")==2){
					zt=false;
				}
			}
		}else{
			zt=false;
		}
		return zt;
	}
	
	public void getSelectData() {
		String strDate1[]=this.getRiq2().split("-");
		String strDate=strDate1[0]+"-"+strDate1[1]+"-01";
		String CurrZnDate = strDate1[0]+"��"+strDate1[1]+"��";
		String strSql="";	
		String diancxxb_id=this.getTreeid();
		strSql=
			"SELECT ID,YUETJKJB_ID,GONGYSB_ID,JIHKJB_ID,PINZB_ID,YUNSFSB_ID,\n" +
			"FENX,JIESL,QNET_AR,MEIJ,MEIJS,YUNJ,YUNJS,KUANGQYF,DAOZZF,ZAF,ZAFS,QIT,\n" + 
			"ROUND(DECODE(QNET_AR,0,0,(MEIJ + KUANGQYF + YUNJ + DAOZZF + ZAF + QIT) * 29.271 /QNET_AR),2) BIAOMDJ,\n" + 
			"ROUND(DECODE(QNET_AR,0,0,(MEIJ + KUANGQYF + YUNJ + DAOZZF + ZAF + QIT - ZAFS -MEIJS - YUNJS) * 29.271 / QNET_AR),2) BUHSBMDJ\n" + 
			"  FROM (SELECT -1 ID,\n" + 
			"               -1 YUETJKJB_ID,\n" + 
			"               DECODE(0, 0, '�ܼ�') AS GONGYSB_ID,\n" + 
			"               '-' AS JIHKJB_ID,\n" + 
			"               '-' AS PINZB_ID,\n" + 
			"               '-' AS YUNSFSB_ID,\n" + 
			"               FENX,\n" + 
			"               SUM(JIESL) JIESL,\n" + 
			"              ROUND(DECODE(SUM(JIESL), 0, 0, SUM(MEIJ * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"              ROUND(DECODE(SUM(JIESL),0,0,SUM(MEIJS * JIESL) / SUM(JIESL)),2) MEIJS,\n" + 
			"              ROUND(DECODE(SUM(JIESL),0,0,SUM(KUANGQYF * JIESL) / SUM(JIESL)),2) KUANGQYF,\n" + 
			"              ROUND(DECODE(SUM(JIESL), 0, 0, SUM(YUNJ * JIESL) / SUM(JIESL)),2) YUNJ,\n" + 
			"              ROUND(DECODE(SUM(JIESL),0,0,SUM(YUNJS * JIESL) / SUM(JIESL)),2) YUNJS,\n" + 
			"              ROUND(DECODE(SUM(JIESL),0,0,SUM(DAOZZF * JIESL) / SUM(JIESL)),2) DAOZZF,\n" + 
			"              ROUND(DECODE(SUM(JIESL), 0, 0, SUM(ZAF * JIESL) / SUM(JIESL)),2) ZAF,\n" + 
			"              ROUND(DECODE(SUM(JIESL), 0, 0, SUM(ZAFS * JIESL) / SUM(JIESL)),2) ZAFS,\n" + 
			"              ROUND(DECODE(SUM(JIESL), 0, 0, SUM(QIT * JIESL) / SUM(JIESL)),2) QIT,\n" + 
			"              ROUND(DECODE(SUM(JIESL),0,0,SUM(QNET_AR * JIESL) / SUM(JIESL)),2) QNET_AR\n" + 
			"          FROM YUETJKJB TJ, YUEJSBMDJ DJ, GONGYSB, JIHKJB, PINZB, YUNSFSB\n" + 
			"         WHERE TJ.ID = DJ.YUETJKJB_ID\n" + 
			"           AND TJ.GONGYSB_ID = GONGYSB.ID\n" + 
			"           AND TJ.JIHKJB_ID = JIHKJB.ID\n" + 
			"           AND TJ.PINZB_ID = PINZB.ID\n" + 
			"           AND TJ.YUNSFSB_ID = YUNSFSB.ID\n" + 
			"           AND DIANCXXB_ID = " + diancxxb_id +"\n" + 
			"           AND RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n" + 
			"         GROUP BY ROLLUP(FENX)\n" + 
			"        HAVING GROUPING(FENX) = 0)\n" + 
			"UNION\n" + 
			"SELECT DJ.ID,\n" + 
			"       DJ.YUETJKJB_ID,\n" + 
			"       GONGYSB.MINGC  AS GONGYSB_ID,\n" + 
			"       JIHKJB.MINGC   AS JIHKJB_ID,\n" + 
			"       PINZB.MINGC    AS PINZB_ID,\n" + 
			"       YUNSFSB.MINGC  AS YUNSFSB_ID,\n" + 
			"       FENX,\n" + 
			"       JIESL,\n" + 
			"       QNET_AR,\n" + 
			"       MEIJ,\n" + 
			"       MEIJS,\n" + 
			"       YUNJ,\n" + 
			"       YUNJS,\n" + 
			"       KUANGQYF,\n" + 
			"       DAOZZF,\n" + 
			"       ZAF,\n" + 
			"       ZAFS,\n" + 
			"       QIT,\n" + 
			"       BIAOMDJ,\n" + 
			"       BUHSBMDJ\n" + 
			"  FROM YUETJKJB TJ, YUEJSBMDJ DJ, GONGYSB, JIHKJB, PINZB, YUNSFSB\n" + 
			" WHERE TJ.ID = DJ.YUETJKJB_ID\n" + 
			"   AND TJ.GONGYSB_ID = GONGYSB.ID\n" + 
			"   AND TJ.JIHKJB_ID = JIHKJB.ID\n" + 
			"   AND TJ.PINZB_ID = PINZB.ID\n" + 
			"   AND TJ.YUNSFSB_ID = YUNSFSB.ID\n" + 
			"   AND DIANCXXB_ID = " + diancxxb_id +"\n" + 
			"   AND RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n" + 
			" ORDER BY JIHKJB_ID,PINZB_ID,YUNSFSB_ID,GONGYSB_ID,YUETJKJB_ID, FENX";
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���ñ��������ڱ���
		egu.setTableName("yuejsbmdj");
		// /������ʾ������
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight-30");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("yuetjkjb_id").setHeader("yuetjkjb_id");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("yuetjkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(80);	
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("pinzb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jiesl").setHeader("�볧��<br>(��)");
		egu.getColumn("jiesl").setWidth(60);
		((NumberField)egu.getColumn("jiesl").editor).setDecimalPrecision(3);
		
		egu.getColumn("qnet_ar").setHeader("�볧����<br>(MJ/kg)");
		egu.getColumn("qnet_ar").setWidth(70);
		((NumberField)egu.getColumn("qnet_ar").editor).setDecimalPrecision(2);
		
		egu.getColumn("meij").setHeader("��˰ú��<br>(Ԫ/��)");
		egu.getColumn("meij").setWidth(60);	
		((NumberField)egu.getColumn("meij").editor).setDecimalPrecision(2);
		
		egu.getColumn("meijs").setHeader("ú��˰<br>(Ԫ/��)");
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("meijs").setEditor(null);
		egu.getColumn("meijs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("kuangqyf").setHeader("����ǰ�ӷ�<br>(Ԫ/��)");
		egu.getColumn("kuangqyf").setWidth(70);
		((NumberField)egu.getColumn("kuangqyf").editor).setDecimalPrecision(2);
		
		egu.getColumn("yunj").setHeader("��˰�˼�<br>(Ԫ/��)");
		egu.getColumn("yunj").setWidth(70);
		((NumberField)egu.getColumn("yunj").editor).setDecimalPrecision(2);
		
		egu.getColumn("yunjs").setHeader("�˼�˰<br>(Ԫ/��)");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setEditor(null);
		egu.getColumn("yunjs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");		
		
		egu.getColumn("daozzf").setHeader("��վ�ӷ�<br>(Ԫ/��)");
		egu.getColumn("daozzf").setWidth(60);
		((NumberField)egu.getColumn("daozzf").editor).setDecimalPrecision(2);
		
		egu.getColumn("zaf").setHeader("�ӷ�<br>(Ԫ/��)");
		egu.getColumn("zaf").setWidth(60);
		((NumberField)egu.getColumn("zaf").editor).setDecimalPrecision(2);
		
		egu.getColumn("zafs").setHeader("�ӷ�˰<br>(Ԫ/��)");
		egu.getColumn("zafs").setWidth(60);
		((NumberField)egu.getColumn("zafs").editor).setDecimalPrecision(2);
		
		egu.getColumn("qit").setHeader("����<br>(Ԫ/��)");
		egu.getColumn("qit").setWidth(60);
		((NumberField)egu.getColumn("qit").editor).setDecimalPrecision(3);
		
		egu.getColumn("biaomdj").setHeader("��ú����<br>(Ԫ/��)");
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("biaomdj").setEditor(null);
		egu.getColumn("biaomdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("buhsbmdj").setHeader("����˰��ú����<br>(Ԫ/��)");
		egu.getColumn("buhsbmdj").setWidth(100);
		egu.getColumn("buhsbmdj").setEditor(null);
		egu.getColumn("buhsbmdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.setDefaultsortable(false); 
		egu.addPaging(0);

		// /���ð�ť
		egu.addTbarText("ʱ��:");
		DateField df = new DateField();
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
	
		egu.addTbarText("-");
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��"+CurrZnDate+"������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		//�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con)){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
		
	//		 ���ɰ�ť
			GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
	//		ɾ����ť
			GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
	//		 ���水ť
			GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
					egu.getGridColumns(), "SaveButton");
			egu.addTbarBtn(gbs);
		
		}
	
		
		String meijs = MainGlobal.getXitxx_item("����", "ú��˰��", diancxxb_id, "0.17");
		String yunjs = MainGlobal.getXitxx_item("����", "�˷�˰��", diancxxb_id, "0.07");
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('beforeedit',function(e){"+
				"if(e.record.get('ID')=='-1'){e.cancel=true;}"+
				"});"
		);
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n" + 
				"  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjs + ",2);\n" + 
				"    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n" + 
				"\n" + 
				"  if(e.field=='MEIJ'||e.field=='MEIJS'||e.field=='KUANGQYF'||e.field=='YUNJ'||e.field=='YUNJS'||e.field=='DAOZZF'||e.field=='ZAF'||e.field=='ZAFS'||e.field=='QIT'||e.field=='QNET_AR'){\n" + 
				"\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ')||0)+eval(gridDiv_ds.getAt(i).get('KUANGQYF')||0)+eval(gridDiv_ds.getAt(i).get('YUNJ')||0)+eval(gridDiv_ds.getAt(i).get('DAOZZF')||0)+eval(gridDiv_ds.getAt(i).get('ZAF')||0)+eval(gridDiv_ds.getAt(i).get('QIT')||0);\n" + 
				" 	if(eval(gridDiv_ds.getAt(i).get('QNET_AR'))!=0){\n" +
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS')||0)-eval(gridDiv_ds.getAt(i).get('YUNJS')||0)-eval(gridDiv_ds.getAt(i).get('ZAFS')||0))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"}\n" + 
				"    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				
				"});"
		);
		egu.addOtherScript(sb.toString());

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String strDate [] = this.getRiq2().split("-");
		String cnDate = strDate[0] + "��" + strDate[1] + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		String strDate = cycle.getRequestContext().getParameter("lx");
		if(strDate!=null){
			visit.setString12(strDate);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString11("");
			visit.setString13("");
			setTreeid(null);
		}
		getSelectData();
	}
    
    public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
	public String getRiq1(){
	       if(((Visit) this.getPage().getVisit()).getString11()==null||((Visit) this.getPage().getVisit()).getString11().equals("")){
				
				((Visit) this.getPage().getVisit()).setString11(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString11();
		}
		public void setRiq1(String riq1){
	         if(((Visit) this.getPage().getVisit()).getString11()!=null &&!((Visit) this.getPage().getVisit()).getString11().equals(riq1)){
				
				((Visit) this.getPage().getVisit()).setString11(riq1);
			}
		}
		
		public String getRiq2(){
	         if(((Visit) this.getPage().getVisit()).getString13()==null||((Visit) this.getPage().getVisit()).getString13().equals("")){
				
				((Visit) this.getPage().getVisit()).setString13(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString13();
		}
		public void setRiq2(String riq2){
	        if(((Visit) this.getPage().getVisit()).getString13()!=null &&!((Visit) this.getPage().getVisit()).getString13().equals(riq2)){
				
				((Visit) this.getPage().getVisit()).setString13(riq2);
			}
		}
}