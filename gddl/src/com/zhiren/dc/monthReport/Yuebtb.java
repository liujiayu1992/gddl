package com.zhiren.dc.monthReport;

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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2012-02-15
 * ���÷�Χ���������
 * ���������ƿھ������·�Ϊ1��ʱ��ֻ���������п��ֵ��ͳ�ƿھ�
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-02-10
 * ���÷�Χ���������
 * �������޸Ľ����ú���۽���ǰ̨���㹫ʽ�����в��Զ�����˰
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-02-02
 * ���÷�Χ���������
 * �������޸�getYuejsdjSql_by��ȡ����ʽ�����ȡ��������
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-03
 * ���÷�Χ���������
 * �������޸�initYuejsbmj���������ʽ
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-06-22
 * ���÷�Χ���������
 * �������޸�initYuezl�������ӿ󷽿ոɻ��ҷ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-05-04
 * ���÷�Χ���������
 * �������޸�initYuejsbmj����ʹ ��ú������ǰ̨�κ�ʱ�򶼿����Զ�����
 * 		�޸�initYuejsbmj�����ж�ϵͳ��Ϣ����ԭ���±�ҳ����㡯���Ϊ'�±��ۼ��Զ�����'�жϣ������ֵ�жϽ��Ϊ�棬���ۼ��в��ɱ༭��
 * 		�޸�initYuejsbmj��Ӧ�ı��淽�������'�±��ۼ��Զ�����'�жϣ������ֵ�жϽ��Ϊ�棬������ۼ��Զ�����ı��淽����
 */
/*
 * ����:licj
 * ʱ��:2011-04-18
 * �޸�����: �޸��½����ú�����ҳ�棬�����ӷ�˰(zafs)�ֶΣ��޸Ĳ���˰��ú���۹�ʽ��(�����ۺϼۣ��˼�˰��ú��˰���ӷ�˰)*29.271/��ֵ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-17
 * �������޸����������ˮ���ҡ��ӷ��֡���ֵ���򡢻��۵����Ϣ\���ӿոɻ��ҡ��ɻ���¼��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-01
 * �������޸��±���±�ú���۵Ĺ�������ȷ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-18
 * �������޸���ͳ�ƿھ����Ӧ��������ʹ������������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-21
 * �����������������»������ݵ������
 */
/**
 * @author ����
 * @since 2009-08-12
 * @describe �±������
 * @version 1.0
 */
public class Yuebtb extends BasePage implements PageValidateListener {
	/* �±���Զ������ùؼ��� */
	public static final String CustomKey_tjkj = "Yuebtb_tjkj";

	public static final String CustomKey_sl = "Yuebtb_sl";

	public static final String CustomKey_zl = "Yuebtb_zl";

	public static final String CustomKey_hc = "Yuebtb_hc";

	public static final String CustomKey_rcbmj = "Yuebtb_rcbmj";

	public static final String CustomKey_jsbmj = "Yuebtb_jsbmj";

	/* �±������ */
	public static final String MonthReportType_tjkj = "tjkj";

	public static final String MonthReportType_sl = "yuesl";

	public static final String MonthReportType_zl = "yuezl";

	public static final String MonthReportType_hc = "yuehc";

	public static final String MonthReportType_rcbmj = "yuercbmj";

	public static final String MonthReportType_jsbmj = "yuejsbmj";

	/* ���� */
	public static final String TableName_sl = "yueslb";

	public static final String TableName_zl = "yuezlb";

	public static final String TableName_hc = "yuehcb";

	public static final String TableName_rcbmj = "yuercbmdj";

	public static final String TableName_jsbmj = "yuejsbmdj";

	/* �õ��������� */
	public String getMonthReportType() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	/*
	 * ������ҳ����û���ʾ�� �ں�̨������ʾ��Ϣ,�൱���ں�̨��ǰ̨������һ��java script �ű�
	 */
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	/*
	 * ҳ���ʼ���ص�ʱ��ִ�еķ��� һЩÿ��ˢ��ҳ����ˢ�µı��� �ĳ�ʼ����������д������
	 * 
	 * @see org.apache.tapestry.AbstractPage#initialize()
	 */
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	/*
	 * ��������ҳ��Ķ���һ��textfield��� ���������Ϊһ��xml�ĵ� ����ResultSetList �е�getModifyResultSet
	 * �� getDeleteResultSet ���н���
	 */
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	/*
	 * ҳ�水ť�ļ���Դ��Tapestry����� ���ڱ����أ�ͨ��Ext����İ�ť��click�¼�����
	 */
	/* ��Ӱ�ť */
	private boolean _Insertclick = false;

	public void InsertButton(IRequestCycle cycle) {
		_Insertclick = true;
	}

	/* ˢ�°�ť */
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	/* ���ɰ�ť */
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	/* ɾ����ť */
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	/* ���水ť */
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
	/* ���水ť */
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}

	/* �ϱ���ť */
	private boolean _ShangbClick = false;

	public void ShangbButton(IRequestCycle cycle) {
		_ShangbClick = true;
	}

	/* �޸����밴ť */
	private boolean _ShenqxgClick = false;

	public void ShenqxgButton(IRequestCycle cycle) {
		_ShenqxgClick = true;
	}

	/*
	 * ����ҳ���form ��submit�¼� cycle �൱��jsp�е� request
	 */
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			
		}
		// if (_Insertclick) {
		// _Insertclick = false;
		// Insert();
		// }
		// if (_CreateClick) {
		// _CreateClick = false;
		// CreateData();
		// }
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_CopyClick) {
			_CopyClick = false;
			Copysykj();//��ã��������¿ھ�
		}
		if (_ShangbClick) {
			_ShangbClick = false;
			Shangb();
		}
		if (_ShenqxgClick) {
			_ShenqxgClick = false;
			Shenqxg();
		}
		setRiq();
		initGrid();
	}

	private String getInsertSql(String diancxxb_id, String gongysb_id,
			String CurrODate, String fenx) {
		String sql = "insert into yueqfmkb(id,diancxxb_id,gongysb_id,riq,fenx) values("
				+ "getnewid("
				+ diancxxb_id
				+ "),"
				+ diancxxb_id
				+ ","
				+ gongysb_id + "," + CurrODate + ",'" + fenx + "')";
		return sql;
	}

	/**
	 * @describe �����û�ѡ��Ĺ�Ӧ�̲���Ƿ��ú����Ϣ
	 * 
	 */
	private void Insert() {
		String gongysid = "";
		if (getGongysValue() == null || getGongysValue().getId() == -1) {
			setMsg("��ѡ����Ҫ���Ƿ������Ϣ�Ĺ�Ӧ��");
			return;
		}
		gongysid = getGongysValue().getStrId();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "select * from yueqfmkb where diancxxb_id =" + diancxxb_id
				+ " and gongysb_id=" + gongysid + " and riq=" + CurrODate;
		if (con.getHasIt(sql)) {
			setMsg("�˹�Ӧ�������Ѵ���,�����ٴ���ӣ�");
			return;
		}
		con.getInsert(getInsertSql(diancxxb_id, gongysid, CurrODate,
				SysConstant.Fenx_Beny));
		con.getInsert(getInsertSql(diancxxb_id, gongysid, CurrODate,
				SysConstant.Fenx_Leij));
	}

	/**
	 * @describe ������ʷδ�������ݼ������������ɹ�Ӧ���б� �������δ��ú�� 1��ɾ���������� 2�����ɹ�Ӧ���б��������ݿ�
	 *           3���������ݿ��й�Ӧ����Ϣ����δ��ú��
	 * 
	 */
	private void CreateData() {
		String diancxxb_id = getTreeid(); // �õ���ѡ�糧ID
		JDBCcon con = new JDBCcon(); // �������ݿ�JDBC����
		con.setAutoCommit(false); // �趨JDBC���Զ��ύ
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		// ���ĵ�����
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01"); // ��ǰѡ�����¶�Ӧ��Date����
		String CurrODate = DateUtil.FormatOracleDate(cd);
		// ��ǰѡ������Oracle��ʽ����
		String LastODate = DateUtil.FormatOracleDate(DateUtil.AddDate(cd, -1,
				DateUtil.AddType_intMonth));
		// ����õ��ϸ��µ�Oracle��ʽ����
		// ɾ����������
		String sql = "delete from yueqfmkb where riq = " + CurrODate
				+ " and diancxxb_id = " + diancxxb_id;
		con.getDelete(sql);
		// ���ɹ�Ӧ���б��������ݿ�
		sql = "select distinct gongysb_id from yueqfmkb where diancxxb_id ="
				+ diancxxb_id
				+ " and riq = "
				+ LastODate
				+ " union select distinct g.dqid "
				+ "from fahb f,vwgongys g where f.jiesb_id = 0 and f.gongysb_id = g.id"
				+ " and f.diancxxb_id = " + diancxxb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			while (rsl.next()) {
				con.getInsert(getInsertSql(diancxxb_id, rsl
						.getString("gongysb_id"), CurrODate,
						SysConstant.Fenx_Beny));
				con.getInsert(getInsertSql(diancxxb_id, rsl
						.getString("gongysb_id"), CurrODate,
						SysConstant.Fenx_Leij));
			}
			rsl.close();
		}
		sql = "select * from yueqfmkb where riq = " + CurrODate
				+ " and diancxxb_id = " + diancxxb_id;
		rsl = con.getResultSetList(sql);
		ResultSetList rs = null;
		while (rsl.next()) {
			String tmp = SysConstant.Fenx_Beny.equals(rsl.getString("fenx")) ? " and f.daohrq>"
					+ CurrODate
					+ " and f.daohrq < add_months("
					+ CurrODate
					+ ",1)"
					: "";
			sql = "select sum(f.laimsl) laimsl,sum(f.laimsl * r.meij / 10000) qiank from fahb f,vwgongys g,ruccb r "
					+ "where f.gongysb_id = g.id and f.ruccbb_id = r.id and diancxxb_id = "
					+ rsl.getString("diancxxb_id")
					+ " and g.dqid = "
					+ rsl.getString("gongysb_id")
					+ " and f.jiesb_id = 0 "
					+ tmp;
			rs = con.getResultSetList(sql);
			if (rs.next()) {
				sql = "update yueqfmkb set meil =" + rs.getDouble("laimsl")
						+ ",qiank =" + rs.getDouble("qiank") + " where id="
						+ rsl.getString("id");
				con.getUpdate(sql);
			}
			rs.close();
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate + "�����ݳɹ����ɣ�");
	}

	/**
	 * @describe ɾ����������
	 * 
	 */
	public void DelData() {
		String diancxxb_id = getTreeid(); // �õ���ѡ�糧ID
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "delete from yueqfmkb where riq = " + CurrODate
				+ " and diancxxb_id = " + diancxxb_id;
		int flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ sql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}
	
	/**
	 * @describe ��ã��������¿ھ�
	 * 
	 */
	public void Copysykj() {
		String diancxxb_id = getTreeid(); // �õ���ѡ�糧ID
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "";
		if(Integer.parseInt(getYuef())==1){
			sql ="insert into yuetjkjb(id,diancxxb_id,xuh,riq,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id)\n" +
			"SELECT GETNEWID(" + diancxxb_id + "),T.DIANCXXB_ID,Rownum," + CurrODate + ",\n" +
			"       T.GONGYSB_ID,T.JIHKJB_ID,T.PINZB_ID,T.YUNSFSB_ID\n" + 
			"  FROM YUETJKJB T, YUEHCB HC WHERE T.ID = HC.YUETJKJB_ID\n" + 
			"   AND HC.KUC <> 0 AND HC.FENX='����' AND t.diancxxb_id = " + diancxxb_id + " and T.riq = add_months(" + CurrODate + ",-1)";
		}else{
			sql ="insert into yuetjkjb(id,diancxxb_id,xuh,riq,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id)\n" +
			"select getnewid(" + diancxxb_id + "),t.diancxxb_id,Rownum," + CurrODate + ",t.gongysb_id,t.jihkjb_id,\n" + 
			"          t.pinzb_id,t.yunsfsb_id\n" + 
			"  from yuetjkjb t\n" + 
			" where t.diancxxb_id = " + diancxxb_id + " and riq = add_months(" + CurrODate + ",-1)";
		}

		int flag = con.getDelete(sql);
		if (flag == -1) {
			setMsg("����ʧ�ܣ�");
		} else {
			setMsg("������ھ����Ƴɹ���");
		}
		con.Close();
	}

	/**
	 * @describe �����޸ĺ��Ƿ��ú����Ϣ
	 * 
	 */

	private void Save() {
		if (MonthReportType_tjkj.equalsIgnoreCase(getMonthReportType())) {
			getExtGrid().Save(getChange(), (Visit) this.getPage().getVisit());
		} else if (MonthReportType_jsbmj.equalsIgnoreCase(getMonthReportType())) {
			getExtGrid().Save(getChange(), (Visit) this.getPage().getVisit());
			JDBCcon con = new JDBCcon();
			/*String sql_script=" select Id from xitxxb where mingc='�±�ҳ�����' and  zhi='��' and leib='�±�' and beiz='ʹ��' ";
			ResultSetList rsl_script = con.getResultSetList(sql_script);*/
			if(MainGlobal.getXitxx_item("�±�", "�±��ۼ��Զ�����", getTreeid(), "��").equals("��")){
				Save1();
			}else{
				
			}
			//rsl_script.close();
			con.Close();
		} else if (MonthReportType_sl.equalsIgnoreCase(getMonthReportType())) {
			getExtGrid().Save(getChange(), (Visit) this.getPage().getVisit());
			if(MainGlobal.getXitxx_item("�±�", "�±������Զ������ۼ�", getTreeid(), "��").equals("��")){
				Save2();
			}else{
				
			}
		} else if (MonthReportType_zl.equalsIgnoreCase(getMonthReportType())) {
			getExtGrid().Save(getChange(), (Visit) this.getPage().getVisit());
			if(MainGlobal.getXitxx_item("�±�", "�±������Զ������ۼ�", getTreeid(), "��").equals("��")){
				Save3();
			}else{
				
			}
		}
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	private void Save1() {
		DelData1();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String diancxxb_id = getTreeid();
		String sql = "";
		int intYuef = Integer.parseInt(getYuef());
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String date_c = MainGlobal.getXitxx_item("�±�", "�±�ȡ�����ڲ�", diancxxb_id,
				"0");
		sql = "select t.id,tj.id yuetjkjb_id,gongysb.id as meikdq_id,jihkjb.id as jihkjb_id,pinzb.id as pinzb_id,\n"
				+ "yunsfsb.id as yunsfsb_id,fenx,jiesl,meij,meijs,kuangqyf,yunj,yunjs,daozzf,zaf,zafs,qit,qnet_ar,biaomdj,buhsbmdj\n"
				+ "from yuetjkjb tj,yuejsbmdj t,gongysb,jihkjb,pinzb,yunsfsb\n"
				+ "where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n"
				+ "and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id \n"
				+ "and t.fenx = '����'\n"
				+ "and tj.diancxxb_id="
				+ diancxxb_id
				+ " and tj.riq="
				+ CurrODate + "\n" + "order by t.id ";
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String gongysb_id = rs.getString("meikdq_id");
			String jihkjb_id = rs.getString("jihkjb_id");
			String pinzb_id = rs.getString("pinzb_id");
			String yunsfsb_id = rs.getString("yunsfsb_id");
			/* �õ�ͳ�ƿھ�ID */
			String tjkjid = MonthReportOp.getYuetjkj(con, CurrODate,
					gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, diancxxb_id);
			if (intYuef == 1) { // �����һ�·�ȡ��������
				sql = getYuejsdjSql_by(CurrODate, date_c, gongysb_id,
						jihkjb_id, pinzb_id, yunsfsb_id, diancxxb_id);
			} else {
				sql = getYuejsdjSql_lj(CurrODate, date_c, gongysb_id,
						jihkjb_id, pinzb_id, yunsfsb_id, diancxxb_id);
			}
			/* �����ۼ����� */
			InsertYuejsdj(con, sql, tjkjid, SysConstant.Fenx_Leij, diancxxb_id);
		}
		rs.close();
		con.commit();
		con.Close();
	}

	private void Save2(){
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String diancxxb_id = getTreeid();
		String sql = "";
		int intYuef = Integer.parseInt(getYuef());
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String date_c = MainGlobal.getXitxx_item("�±�", "�±�ȡ�����ڲ�", diancxxb_id,
				"0");
		con.getDelete("delete from yueslb where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ " and fenx = '�ۼ�')");
		sql = "select t.id,tj.id yuetjkjb_id,gongysb.id as meikdq_id,jihkjb.id as jihkjb_id,pinzb.id as pinzb_id,yunsfsb.id as yunsfsb_id\n"
				+ "from yuetjkjb tj,yueslb t,gongysb,jihkjb,pinzb,yunsfsb\n"
				+ "where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n"
				+ "and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id \n"
				+ "and t.fenx = '����'\n"
				+ "and tj.diancxxb_id="
				+ diancxxb_id
				+ " and tj.riq="
				+ CurrODate + "\n" + "order by t.id ";
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String strid = MainGlobal.getNewID(con, diancxxb_id);
			String gongysb_id = rs.getString("meikdq_id");
			String jihkjb_id = rs.getString("jihkjb_id");
			String pinzb_id = rs.getString("pinzb_id");
			String yunsfsb_id = rs.getString("yunsfsb_id");
			/* �õ�ͳ�ƿھ�ID */
			String tjkjid = MonthReportOp.getYuetjkj(con, CurrODate,
					gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, diancxxb_id);
			sql ="SELECT SUM(jingz) jingz, SUM(biaoz) biaoz, SUM(yingd) yingd, SUM(kuid)kuid,\n" +
				"SUM(yuns) yuns, SUM(koud) koud, SUM(kous) kous, SUM(kouz) kouz, SUM(koum) koum,\n" + 
				"SUM(zongkd) zongkd,SUM(sanfsl) sanfsl, SUM(jianjl)jianjl, SUM(ructzl)ructzl,\n" + 
				"SUM(yingdzje)yingdzje,SUM(kuidzje)kuidzje,SUM(suopsl) suopsl,SUM(suopje)suopje\n" + 
				"FROM"+
				"(SELECT t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n" +
				"y.jingz,y.biaoz,y.yingd,y.kuid,y.yuns,y.koud,y.kous,y.kouz,y.koum,y.zongkd,\n" + 
				"y.sanfsl,y.jianjl,y.ructzl,y.yingdzje,y.kuidzje,y.suopsl,y.suopje\n" + 
				"FROM yueslb y,yuetjkjb t\n" + 
				"WHERE y.yuetjkjb_id = t.id\n"
				+ "and t.riq = "
				+ CurrODate
				+ "\n"
				+ "and t.gongysb_id = "
				+ gongysb_id
				+ "\n"
				+ "and t.jihkjb_id = "
				+ jihkjb_id
				+ "\n"
				+ "and t.pinzb_id = "
				+ pinzb_id
				+ "\n"
				+ "and t.yunsfsb_id = "
				+ yunsfsb_id
				+ "\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ "\n"
				+ "and y.fenx = '"
				+ SysConstant.Fenx_Beny 
				+ "'\n"
				+ "union \n"
				+ "SELECT t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n" +
				" y.jingz,y.biaoz,y.yingd,y.kuid,y.yuns,y.koud,y.kous,y.kouz,y.koum,y.zongkd,\n" + 
				" y.sanfsl,y.jianjl,y.ructzl,y.yingdzje,y.kuidzje,y.suopsl,y.suopje\n" + 
				" FROM yueslb y,yuetjkjb t\n" + 
				" WHERE y.yuetjkjb_id = t.id\n";
				if(intYuef == 1){
					sql = sql + "and t.riq = "+ CurrODate +"\n";
				}else{
					sql = sql + "and t.riq = add_months("+ CurrODate +",-1 ) \n";
				}
				sql = sql + "and t.gongysb_id = "
				+ gongysb_id
				+ "\n"
				+ "and t.jihkjb_id = "
				+ jihkjb_id
				+ "\n"
				+ "and t.pinzb_id = "
				+ pinzb_id
				+ "\n"
				+ "and t.yunsfsb_id = "
				+ yunsfsb_id
				+ "\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ "\n"
				+ "and y.fenx = '";
				if(intYuef ==1){
					sql = sql+ SysConstant.Fenx_Beny+"')\n"; 
				}else{
					sql = sql+ SysConstant.Fenx_Leij+"')\n"; 
				}
				sql = sql + "group by diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n";
				ResultSetList rslj = con.getResultSetList(sql);
			/* �����ۼ����� */
				if(rslj.next()){
					sql = "insert into yueslb (id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl," +
							"jianjl,ructzl,zhuangt,laimsl,yingdzje,kuidzje,suopsl,suopje) values("+
							strid +","+tjkjid+ ",'�ۼ�',"+rslj.getDouble("jingz")+","+rslj.getDouble("biaoz")+","+rslj.getDouble("yingd")
							+","+rslj.getDouble("kuid")+","+rslj.getDouble("yuns")+","+rslj.getDouble("koud")+","+rslj.getDouble("kous")
							+","+rslj.getDouble("kouz")+","+rslj.getDouble("koum")+","+rslj.getDouble("zongkd")+","+rslj.getDouble("sanfsl")
							+","+rslj.getDouble("jianjl")+","+rslj.getDouble("ructzl")+",0,"+rslj.getDouble("laimsl")+","+rslj.getDouble("yingdzje")
							+","+rslj.getDouble("kuidzje")+","+rslj.getDouble("suopsl")+","+rslj.getDouble("suopje")+")";
					con.getInsert(sql);
				}
		}
		rs.close();
		con.commit();
		con.Close();
	}
	
	private void Save3(){
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String diancxxb_id = getTreeid();
		String sql = "";
		int intYuef = Integer.parseInt(getYuef());
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String date_c = MainGlobal.getXitxx_item("�±�", "�±�ȡ�����ڲ�", diancxxb_id,
				"0");
		con.getDelete("delete from yuezlb where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ " and fenx = '�ۼ�')");
		
		sql = "select t.id,tj.id yuetjkjb_id,gongysb.id as meikdq_id,jihkjb.id as jihkjb_id,pinzb.id as pinzb_id,yunsfsb.id as yunsfsb_id\n"
				+ "from yuetjkjb tj,yuezlb t,gongysb,jihkjb,pinzb,yunsfsb\n"
				+ "where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n"
				+ "and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id \n"
				+ "and t.fenx = '����'\n"
				+ "and tj.diancxxb_id="
				+ diancxxb_id
				+ " and tj.riq="
				+ CurrODate + "\n" + "order by t.id ";
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String strid = MainGlobal.getNewID(con, diancxxb_id);
			String gongysb_id = rs.getString("meikdq_id");
			String jihkjb_id = rs.getString("jihkjb_id");
			String pinzb_id = rs.getString("pinzb_id");
			String yunsfsb_id = rs.getString("yunsfsb_id");
			/* �õ�ͳ�ƿھ�ID */
			String tjkjid = MonthReportOp.getYuetjkj(con, CurrODate,
					gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, diancxxb_id);
			sql ="SELECT\n" + 
				"sum(jingz) jingz,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*qnet_ar)/SUM(jingz)),2) AS qnet_ar,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*qbad)/SUM(jingz)),2) AS qbad,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*mt)/SUM(jingz)),2) AS mt,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*mad)/SUM(jingz)),2) AS mad,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*aar)/SUM(jingz)),2) AS aar,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*aad)/SUM(jingz)),2) AS aad,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*ad)/SUM(jingz)),2) AS ad,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*vdaf)/SUM(jingz)),2) AS vdaf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*std)/SUM(jingz)),2) AS std,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*had)/SUM(jingz)),2) AS had,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*fcad)/SUM(jingz)),2) AS fcad,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*qnet_ar_kf)/SUM(jingz)),2) AS qnet_ar_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*qbad_kf)/SUM(jingz)),2) AS qbad_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*mt_kf)/SUM(jingz)),2) AS mt_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*mad_kf)/SUM(jingz)),2) AS mad_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*aar_kf)/SUM(jingz)),2) AS aar_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*aad_kf)/SUM(jingz)),2) AS aad_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*vdaf_kf)/SUM(jingz)),2) AS vdaf_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*std_kf)/SUM(jingz)),2) AS std_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*had_kf)/SUM(jingz)),2) AS had_kf,\n" + 
				"round(decode(sum(jingz),0,0,sum(jingz*fcad_kf)/SUM(jingz)),2) AS fcad_kf,\n" + 
				"SUM(zhijbfje) zhijbfje,SUM(suopje)suopje,SUM(lsuopsl)lsuopsl,SUM(zhijbfje_m)zhijbfje_m,\n" +
				"SUM(zhijbfje_a)zhijbfje_a,SUM(zhijbfje_v)zhijbfje_v,SUM(zhijbfje_q)zhijbfje_q,\n" + 
				"SUM(zhijbfje_s)zhijbfje_s,SUM(zhijbfje_t)zhijbfje_t\n" +
				"FROM\n" + 
				"(SELECT y.id,t.id yuetjkjb_id,t.diancxxb_id,t.gongysb_id,t.jihkjb_id,t.pinzb_id,t.yunsfsb_id,y.fenx,\n" + 
				"jingz,qnet_ar,qbad, mt,mad,aar, aad,ad,vdaf,std,had, fcad, qnet_ar_kf,qbad_kf, mt_kf, mad_kf, aar_kf,aad_kf,vdaf_kf,std_kf,had_kf, fcad_kf,\n" +
				"y.zhijbfje,y.suopje,y.lsuopsl,y.lsuopje,y.zhijbfje_m,y.zhijbfje_a,y.zhijbfje_v,y.zhijbfje_q,y.zhijbfje_s,y.zhijbfje_t \n"+
				"FROM yuezlb y,yuetjkjb t,yueslb ys\n" + 
				"WHERE y.yuetjkjb_id = t.id and ys.yuetjkjb_id = t.id\n" + 
				"and ys.fenx = y.fenx\n"
				+ "and t.riq = "
				+ CurrODate
				+ "\n"
				+ "and t.gongysb_id = "
				+ gongysb_id
				+ "\n"
				+ "and t.jihkjb_id = "
				+ jihkjb_id
				+ "\n"
				+ "and t.pinzb_id = "
				+ pinzb_id
				+ "\n"
				+ "and t.yunsfsb_id = "
				+ yunsfsb_id
				+ "\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ "\n"
				+ "and y.fenx = '"
				+ SysConstant.Fenx_Beny 
				+ "'\n"
				+ "union \n"
				+ "SELECT y.id,t.id yuetjkjb_id,t.diancxxb_id,t.gongysb_id,t.jihkjb_id,t.pinzb_id,t.yunsfsb_id,y.fenx,\n" +
				"jingz,qnet_ar,qbad, mt,mad,aar, aad,ad,vdaf,std,had, fcad, qnet_ar_kf,qbad_kf, mt_kf, mad_kf, aar_kf,aad_kf,vdaf_kf,std_kf,had_kf, fcad_kf,\n" +
				"y.zhijbfje,y.suopje,y.lsuopsl,y.lsuopje,y.zhijbfje_m,y.zhijbfje_a,y.zhijbfje_v,y.zhijbfje_q,y.zhijbfje_s,y.zhijbfje_t\n"+
				"FROM yuezlb y,yuetjkjb t,yueslb ys\n" + 
				"WHERE y.yuetjkjb_id = t.id  AND ys.yuetjkjb_id = t.id\n" + 
				"and ys.fenx = y.fenx\n";
				if(intYuef == 1){
					sql = sql + "and t.riq = "+ CurrODate +"\n";
				}else{
					sql = sql + "and t.riq = add_months("+ CurrODate +",-1 ) \n";
				}
				sql = sql + "and t.gongysb_id = "
				+ gongysb_id
				+ "\n"
				+ "and t.jihkjb_id = "
				+ jihkjb_id
				+ "\n"
				+ "and t.pinzb_id = "
				+ pinzb_id
				+ "\n"
				+ "and t.yunsfsb_id = "
				+ yunsfsb_id
				+ "\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ "\n"
				+ "and y.fenx = '";
				if(intYuef ==1){
					sql = sql+ SysConstant.Fenx_Beny+"')\n"; 
				}else{
					sql = sql+ SysConstant.Fenx_Leij+"')\n"; 
				}
				sql = sql + "group by diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n";
				ResultSetList rslj = con.getResultSetList(sql);
			/* �����ۼ����� */
				if(rslj.next()){
					sql = "insert into yuezlb (id,yuetjkjb_id,fenx,qnet_ar,qbad,mt,mad,aar,aad,ad,vdaf,std,had,fcad,qnet_ar_kf,\n" +
							"qbad_kf,mt_kf,mad_kf,aar_kf,aad_kf,vdaf_kf,std_kf,had_kf,fcad_kf,zhijbfje,suopje,lsuopsl,\n" + 
							"lsuopje,zhijbfje_m,zhijbfje_a,zhijbfje_v,zhijbfje_q,zhijbfje_s,zhijbfje_t) values(\n"+
							
							strid +","+tjkjid+ ",'�ۼ�',"+rslj.getDouble("qnet_ar")+","+rslj.getDouble("qbad")+","+rslj.getDouble("mt")
							+","+rslj.getDouble("mad")+","+rslj.getDouble("aar")+","+rslj.getDouble("aad")+","+rslj.getDouble("ad")
							+","+rslj.getDouble("vdaf")+","+rslj.getDouble("std")+","+rslj.getDouble("had")+","+rslj.getDouble("fcad")
							+","+rslj.getDouble("qnet_ar_kf")+","+rslj.getDouble("qbad_kf")+","+rslj.getDouble("mt_kf")+","+rslj.getDouble("mad_kf")
							+","+rslj.getDouble("aar_kf")+","+rslj.getDouble("aad_kf")+","+rslj.getDouble("vdaf_kf")+","+rslj.getDouble("std_kf")+","+rslj.getDouble("had_kf")
							+","+rslj.getDouble("fcad_kf")+","+rslj.getDouble("zhijbfje")+","+rslj.getDouble("suopje")+","+rslj.getDouble("lsuopsl")
							+","+rslj.getDouble("lsuopje")+","+rslj.getDouble("zhijbfje_m")+","+rslj.getDouble("zhijbfje_a")+","+rslj.getDouble("zhijbfje_v")
							+","+rslj.getDouble("zhijbfje_q")+","+rslj.getDouble("zhijbfje_s")+","+rslj.getDouble("zhijbfje_t")+")";
					con.getInsert(sql);
				}
		}
		rs.close();
		con.commit();
		con.Close();
	}
	
	
	private String getYuejsdjSql_by(String CODate, String data_c,
			String gongysb_id, String jihkjb_id, String pinzb_id,
			String yunsfsb_id, String diancxxb_id) {
		String sql = "select\n"
			+ "round_new(sum(jiesl),0) as jiesl,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(meij*jiesl)/sum(jiesl)),3) meij,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(meijs*jiesl)/sum(jiesl)),3) meijs,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(yunj*jiesl)/sum(jiesl)),3) yunj,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(yunjs*jiesl)/sum(jiesl)),3) yunjs,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(daozzf*jiesl)/sum(jiesl)),3) daozzf,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(zaf*jiesl)/sum(jiesl)),3) zaf,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(zafs*jiesl)/sum(jiesl)),3) zafs,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(qit*jiesl)/sum(jiesl)),3) qit,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(qnet_ar*jiesl)/sum(jiesl)),3) qnet_ar,\n"
			+ "round_new(decode(sum(jiesl),0,0,sum(kuangqyf*jiesl)/sum(jiesl)),3) kuangqyf\n"
			+ "from\n"
			+ "(select t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n"
			+ "y.jiesl, y.meij, y.meijs, y.yunj , y.yunjs, y.daozzf, y.zaf,y.zafs, y.qit, y.qnet_ar,\n"
			+ "y.kuangqyf\n"
			+ "from yuejsbmdj y, yuetjkjb t\n"
			+ "where y.yuetjkjb_id = t.id\n"
			+ "and t.riq = "
			+ CODate
			+ "\n"
			+ "and t.gongysb_id = "
			+ gongysb_id
			+ "\n"
			+ "and t.jihkjb_id = "
			+ jihkjb_id
			+ "\n"
			+ "and t.pinzb_id = "
			+ pinzb_id
			+ "\n"
			+ "and t.yunsfsb_id = "
			+ yunsfsb_id
			+ "\n"
			+ "and t.diancxxb_id = "
			+ diancxxb_id
			+ "\n"
			+ "and y.fenx = '"
			+ SysConstant.Fenx_Beny
			+ "'\n"
//			+ "union\n"
//			+ "select t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n"
//			+ "y.jiesl, y.meij, y.meijs, y.yunj , y.yunjs, y.daozzf, y.zaf,y.zafs, y.qit, y.qnet_ar,\n"
//			+ "y.kuangqyf\n"
//			+ "from yuejsbmdj y, yuetjkjb t\n"
//			+ "where y.yuetjkjb_id = t.id\n"
//			+ "and t.riq = add_months("
//			+ CODate
//			+ ",-1)\n"
//			+ "and t.gongysb_id = "
//			+ gongysb_id
//			+ "\n"
//			+ "and t.jihkjb_id = "
//			+ jihkjb_id
//			+ "\n"
//			+ "and t.pinzb_id = "
//			+ pinzb_id
//			+ "\n"
//			+ "and t.yunsfsb_id = "
//			+ yunsfsb_id
//			+ "\n"
//			+ "and t.diancxxb_id = "
//			+ diancxxb_id
//			+ "\n"
//			+ "and y.fenx = '"
//			+ SysConstant.Fenx_Beny
			+ ")\n"
			+ "group by diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n"
			+ "";
	return sql;
	}

	private String getYuejsdjSql_lj(String CODate, String data_c,
			String gongysb_id, String jihkjb_id, String pinzb_id,
			String yunsfsb_id, String diancxxb_id) {
		String sql =

		"select\n"
				+ "round_new(sum(jiesl),0) as jiesl,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(meij*jiesl)/sum(jiesl)),3) meij,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(meijs*jiesl)/sum(jiesl)),3) meijs,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(yunj*jiesl)/sum(jiesl)),3) yunj,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(yunjs*jiesl)/sum(jiesl)),3) yunjs,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(daozzf*jiesl)/sum(jiesl)),3) daozzf,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(zaf*jiesl)/sum(jiesl)),3) zaf,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(zafs*jiesl)/sum(jiesl)),3) zafs,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(qit*jiesl)/sum(jiesl)),3) qit,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(qnet_ar*jiesl)/sum(jiesl)),3) qnet_ar,\n"
				+ "round_new(decode(sum(jiesl),0,0,sum(kuangqyf*jiesl)/sum(jiesl)),3) kuangqyf\n"
				+ "from\n"
				+ "(select t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n"
				+ "y.jiesl, y.meij, y.meijs, y.yunj , y.yunjs, y.daozzf, y.zaf,y.zafs, y.qit, y.qnet_ar,\n"
				+ "y.kuangqyf\n"
				+ "from yuejsbmdj y, yuetjkjb t\n"
				+ "where y.yuetjkjb_id = t.id\n"
				+ "and t.riq = "
				+ CODate
				+ "\n"
				+ "and t.gongysb_id = "
				+ gongysb_id
				+ "\n"
				+ "and t.jihkjb_id = "
				+ jihkjb_id
				+ "\n"
				+ "and t.pinzb_id = "
				+ pinzb_id
				+ "\n"
				+ "and t.yunsfsb_id = "
				+ yunsfsb_id
				+ "\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ "\n"
				+ "and y.fenx = '"
				+ SysConstant.Fenx_Beny
				+ "'\n"
				+ "union\n"
				+ "select t.diancxxb_id, t.gongysb_id, t.jihkjb_id, t.pinzb_id, t.yunsfsb_id,\n"
				+ "y.jiesl, y.meij, y.meijs, y.yunj , y.yunjs, y.daozzf, y.zaf,y.zafs, y.qit, y.qnet_ar,\n"
				+ "y.kuangqyf\n"
				+ "from yuejsbmdj y, yuetjkjb t\n"
				+ "where y.yuetjkjb_id = t.id\n"
				+ "and t.riq = add_months("
				+ CODate
				+ ",-1)\n"
				+ "and t.gongysb_id = "
				+ gongysb_id
				+ "\n"
				+ "and t.jihkjb_id = "
				+ jihkjb_id
				+ "\n"
				+ "and t.pinzb_id = "
				+ pinzb_id
				+ "\n"
				+ "and t.yunsfsb_id = "
				+ yunsfsb_id
				+ "\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ "\n"
				+ "and y.fenx = '"
				+ SysConstant.Fenx_Leij
				+ "')\n"
				+ "group by diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n"
				+ "";
		return sql;
	}

	private String InsertYuejsdj(JDBCcon con, String sql, String tjkjid,
			String fenx, String diancxxb_id) {
		String strid = MainGlobal.getNewID(con, diancxxb_id);
		double jiessl = 0.0;
		double meij = 0.0;
		double meijs = 0.0;
		double kuangqyf = 0.0;
		double tielyf = 0.0;
		double tielyfs = 0.0;
		double daozzf = 0.0;
		double tielzf = 0.0;
		double qit = 0.0;
		double farl = 0.0;
		double daoczhj = 0.0;
		double biaomdj = 0.0;
		double buhsbmdj = 0.0;
		double zafs=0.0;//�����ӷ�˰
		ResultSetList rsmx = con.getResultSetList(sql);
		if (rsmx.next()) {
			jiessl = rsmx.getDouble("jiesl");
			meij = rsmx.getDouble("meij");
			meijs = rsmx.getDouble("meijs");
			kuangqyf = rsmx.getDouble("kuangqyf");
			tielyf = rsmx.getDouble("yunj");
			tielyfs = rsmx.getDouble("yunjs");
			daozzf = rsmx.getDouble("daozzf");
			tielzf = rsmx.getDouble("zaf");
			qit = rsmx.getDouble("qit");
			farl = rsmx.getDouble("qnet_ar");
			zafs=rsmx.getDouble("zafs");
			// �����ۺϼ� = ú��(��˰) + �����˷� + ��·�˷�(��˰) + ��վ�ӷ� + ��·�ӷ� + ����
			daoczhj = meij + kuangqyf + tielyf + daozzf + tielzf + qit;
			if (farl > 0) {
				biaomdj = CustomMaths.Round_new(daoczhj * 29.271 / farl, 3);
				buhsbmdj = CustomMaths.Round_new((daoczhj - meijs - tielyfs-zafs)
						* 29.271 / farl, 3);
			}
		}
		rsmx.close();
		sql = "insert into yuejsbmdj (id, fenx, yuetjkjb_id, jiesl, meij, meijs, kuangqyf, "
				+ "yunj, yunjs, daozzf, zaf,zafs, qit, qnet_ar, biaomdj, buhsbmdj)\n values ("
				+ strid
				+ ",'"
				+ fenx
				+ "',"
				+ tjkjid
				+ ","
				+ jiessl
				+ ","
				+ meij
				+ ","
				+ meijs
				+ ","
				+ kuangqyf
				+ ","
				+ tielyf
				+ ","
				+ tielyfs
				+ ","
				+ daozzf
				+ ","
				+ tielzf
				+ ","
				+zafs
				+ ","
				+ qit
				+ ","
				+ farl + "," + biaomdj + "," + buhsbmdj + ")";
		if (con.getInsert(sql) == -1) {
			return null;
		}
		return strid;
	}

	private void DelData1() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from yuejsbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ " and fenx = '�ۼ�')";
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
	/**
	 * @describe �������ϱ�
	 * 
	 */
	private void Shangb() {
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01"); // ��ǰѡ�����¶�Ӧ��Date����
		String CurrODate = DateUtil.FormatOracleDate(cd);
		// ��ǰѡ������Oracle��ʽ����
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		MainGlobal.Shujshcz(con, getTreeid(), CurrODate, "0", "Yueqfmk", v
				.getRenymc(), "");
		con.Close();
		setMsg("�ϱ����ݳɹ���");
	}

	/**
	 * @describe ������ϱ����ݵ��޸�
	 * 
	 */
	private void Shenqxg() {
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01"); // ��ǰѡ�����¶�Ӧ��Date����
		String CurrODate = DateUtil.FormatOracleDate(cd);
		// ��ǰѡ������Oracle��ʽ����
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con, getTreeid(), CurrODate, "0", "Yueqfmk", v
				.getRenymc(), getChange());
	}
	
	/**
	 * @param con
	 * @param diancId
	 * @param riq
	 * @return
	 */
	private String getTjkjXuh(JDBCcon con,String diancId,String riq){
		int num=0;
		String sql="select max(xuh) xuh from yuetjkjb where diancxxb_id="+diancId+" and riq="+riq;
		ResultSetList rs=con.getResultSetList(sql);
		if (rs.next()) {
			num=rs.getInt("xuh")+1;
		}
		return String.valueOf(num);
	}
	/**
	 * 
	 * @param con
	 * @param diancxxb_id
	 * @param CurrODate
	 * @discription ��ͳ�ƿھ��������޸�
	 */
	private void initYuetjkj(JDBCcon con, String diancxxb_id, String CurrODate) {
		String sql = "select t.id,t.diancxxb_id,t.xuh,t.riq,g.mingc gongysb_id,j.mingc jihkjb_id,\n"
				+ "p.mingc pinzb_id,y.mingc yunsfsb_id\n"
				+ "from yuetjkjb t, gongysb g, jihkjb j, pinzb p, yunsfsb y\n"
				+ "where t.gongysb_id = g.id and t.jihkjb_id = j.id\n"
				+ "and t.pinzb_id = p.id and t.yunsfsb_id = y.id\n"
				+ "and t.diancxxb_id = "
				+ diancxxb_id
				+ " and riq = "
				+ CurrODate +" ORDER BY xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomKey_tjkj);
		// ���ø��±�
		egu.setTableName("yuetjkjb");
		// ����grid���
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ����Ϊ�༭����ҳ
		egu.addPaging(0);
		// ����Ϊ�ɱ༭grid
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// �����б���
		egu.getColumn("diancxxb_id").setHeader("�糧");
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		// �����п�
		egu.getColumn("diancxxb_id").setWidth(0);
		egu.getColumn("xuh").setWidth(45);
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("jihkjb_id").setWidth(100);
		egu.getColumn("pinzb_id").setWidth(100);
		egu.getColumn("yunsfsb_id").setWidth(100);
		// ���ò��ɱ༭
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setEditor(null);
		// ����������
		egu.getColumn("diancxxb_id").setHidden(true);
		// ����Ĭ��ֵ
		egu.getColumn("diancxxb_id").setDefaultValue(diancxxb_id);
		egu.getColumn("riq").setDefaultValue(
				getNianf() + "-" + getYuef() + "-01");
		egu.getColumn("xuh").setDefaultValue(getTjkjXuh(con,diancxxb_id,CurrODate));
		// ���ù�Ӧ��������
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id, mingc from gongysb where leix =0"));
		// ����Ʒ��������
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Pinz_mei));
		// ���ÿھ�������
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Kouj));
		// �������䷽ʽ������
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb"));

		// �������������
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		// �����·�������
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		// ���÷ָ���
		egu.addTbarText("-");
		// ���õ糧ѡ����
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// ���÷ָ���
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gbt = new GridButton("�������¿ھ�","function(){document.getElementById('CopyButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(gbt);

		setExtGrid(egu);
	}

	/**
	 * @param diancxxb_id
	 *            �糧ID
	 * @param yuetjkjb_id
	 *            ͳ�ƿھ�ID
	 * @param fenx
	 *            �������/�ۼƣ�
	 * @param TableName
	 *            ����
	 * @return ����SQL
	 */
	private String getInsertSqlForTableName(String diancxxb_id,
			String yuetjkjb_id, String fenx, String TableName) {
		String sql = "insert into " + TableName
				+ "(id,fenx,yuetjkjb_id) values(" + "getnewid(" + diancxxb_id
				+ "),'" + fenx + "'," + yuetjkjb_id + ")";
		return sql;
	}

	/**
	 * @param con
	 *            ����
	 * @param diancxxb_id
	 *            �糧ID
	 * @param CurrODate
	 *            ͬ������oracle��ʽ
	 * @param TableName
	 *            ��Ҫͬ���ı���
	 * @discription �˹�����Ҫͬ�����µ� �����ú���� ����ͳ�ƿھ��Ĺ�ϵ
	 */
	private void updateTable(JDBCcon con, String diancxxb_id, String CurrODate,
			String TableName) {
		String sql = "select id from yuetjkjb\n" + "where diancxxb_id = "
				+ diancxxb_id + " and riq = " + CurrODate + "\n" + "minus\n"
				+ "select distinct yuetjkjb_id from " + TableName;
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			con.getInsert(getInsertSqlForTableName(diancxxb_id, rsl
					.getString("id"), SysConstant.Fenx_Beny, TableName));
			con.getInsert(getInsertSqlForTableName(diancxxb_id, rsl
					.getString("id"), SysConstant.Fenx_Leij, TableName));
		}
		rsl.close();
	}

	/**
	 * @param con
	 * @return   true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
	 */
	private boolean getZhangt(JDBCcon con,String tblName){
		Visit visit=(Visit)getPage().getVisit();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from "+tblName+" s, yuetjkjb k\n" + 
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
	
	
	private void initYuesl(JDBCcon con, String diancxxb_id, String CurrODate) {
		// ͬ���½����ú��������ͳ�ƿھ���
		updateTable(con, diancxxb_id, CurrODate, TableName_sl);
		String sql = 
			"SELECT * FROM (\n" +
			"select -2 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
			"'' as yunsfsb_id,'���ºϼ�' fenx,sum(t.jingz) AS jingz, sum(t.biaoz) biaoz, sum(t.yingd) yingd,\n" + 
			"sum(t.kuid) kuid, sum(t.yuns) yuns, sum(t.zongkd) zongkd,sum(t.jianjl) jianjl,\n" + 
			"sum(t.ructzl) ructzl, sum(t.yingdzje) yingdzje, sum(t.kuidzje) kuidzje, sum(t.suopsl)suopsl, sum(t.suopje)suopje\n" + 
			"from yuetjkjb tj,yueslb t,gongysb,jihkjb,pinzb,yunsfsb\n" + 
			"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
			"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND t.fenx='����'\n" + 
			"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
			"UNION\n" + 
			"select -1 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
			"'' as yunsfsb_id,'�ۼƺϼ�' fenx,sum(t.jingz) AS jingz, sum(t.biaoz) biaoz, sum(t.yingd) yingd,\n" + 
			"sum(t.kuid) kuid, sum(t.yuns) yuns, sum(t.zongkd) zongkd,sum(t.jianjl) jianjl,\n" + 
			"sum(t.ructzl) ructzl, sum(t.yingdzje) yingdzje, sum(t.kuidzje) kuidzje, sum(t.suopsl)suopsl, sum(t.suopje)suopje\n" + 
			"from yuetjkjb tj,yueslb t,gongysb,jihkjb,pinzb,yunsfsb\n" + 
			"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
			"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND t.fenx='�ۼ�'\n" + 
			"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
			"UNION\n" + 
			"select t.id,tj.id yuetjkjb_id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" + 
			"yunsfsb.mingc as yunsfsb_id,fenx,t.jingz, t.biaoz, t.yingd, t.kuid, t.yuns, t.zongkd, t.jianjl, t.ructzl, t.yingdzje, t.kuidzje, t.suopsl, t.suopje\n" + 
			"from yuetjkjb tj,yueslb t,gongysb,jihkjb,pinzb,yunsfsb\n" + 
			"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
			"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id\n" + 
			"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
			")ORDER BY gongysb_id desc,jihkjb_id,pinzb_id,fenx";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomKey_sl);
		// ����grid���
		egu.setWidth("bodyWidth");
		// ����Ϊ�༭����ҳ
		egu.addPaging(0);
		// ����Ϊ�ɱ༭grid
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ������ʾ������
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ���ñ���
		egu.setTableName("yueslb");
		// �����б���
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
		egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("zongkd").setHeader(Locale.zongkd_fahb);
		egu.getColumn("jianjl").setHeader(Locale.MRtp_jianjl);
		egu.getColumn("ructzl").setHeader(Locale.MRtp_ructzl);
		egu.getColumn("yingdzje").setHeader(Locale.MRtp_yingdzje);
		egu.getColumn("kuidzje").setHeader(Locale.MRtp_kuidzje);
		egu.getColumn("suopsl").setHeader(Locale.MRtp_suopsl);
		egu.getColumn("suopje").setHeader(Locale.MRtp_suopje);

		// �����п�
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingd").setWidth(60);
		egu.getColumn("kuid").setWidth(60);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("zongkd").setWidth(60);
		egu.getColumn("jianjl").setWidth(60);
		egu.getColumn("ructzl").setWidth(90);
		egu.getColumn("yingdzje").setWidth(90);
		egu.getColumn("kuidzje").setWidth(90);
		egu.getColumn("suopsl").setWidth(80);
		egu.getColumn("suopje").setWidth(80);
		// ���ò��ɱ༭
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("gongysb_id").setEditor(null);
		egu
				.getColumn("gongysb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu
				.getColumn("jihkjb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pinzb_id").setEditor(null);
		egu
				.getColumn("pinzb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu
				.getColumn("yunsfsb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setEditor(null);
		egu
				.getColumn("fenx")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		// ���ò������ֶ�
		egu.getColumn("yuetjkjb_id").setUpdate(false);
		egu.getColumn("gongysb_id").setUpdate(false);
		egu.getColumn("jihkjb_id").setUpdate(false);
		egu.getColumn("pinzb_id").setUpdate(false);
		egu.getColumn("yunsfsb_id").setUpdate(false);
		egu.getColumn("fenx").setUpdate(false);
		// ����������
		egu.getColumn("yuetjkjb_id").setHidden(true);
		// �������������
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		// �����·�������
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		// ���÷ָ���
		egu.addTbarText("-");
		// ���õ糧ѡ����
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// ���÷ָ���
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){"
				+ "if(e.record.get('ID')=='-1' || e.record.get('ID')=='-2'){"
				+ " e.cancel=true;" + "}" + "});");
		
		//�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con,"yueslb")){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		setExtGrid(egu);
	}

	private void initYuezl(JDBCcon con, String diancxxb_id, String CurrODate) {
		// ͬ���½����ú��������ͳ�ƿھ���
		updateTable(con, diancxxb_id, CurrODate, TableName_zl);
		String sql = "SELECT * FROM\n" +
		"(\n" + 
		"select -2 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
		"'' as yunsfsb_id,'���ºϼ�' AS fenx,sum(sl.jingz) jingz,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar)/SUM(sl.jingz)),2) AS qnet_ar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad)/SUM(sl.jingz)),2) AS qbad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt)/SUM(sl.jingz)),2) AS mt,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad)/SUM(sl.jingz)),2) AS mad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar)/SUM(sl.jingz)),2) AS aar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad)/SUM(sl.jingz)),2) AS aad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*ad)/SUM(sl.jingz)),2) AS ad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf)/SUM(sl.jingz)),2) AS vdaf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std)/SUM(sl.jingz)),2) AS std,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had)/SUM(sl.jingz)),2) AS had,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad)/SUM(sl.jingz)),2) AS fcad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar_kf)/SUM(sl.jingz)),2) AS qnet_ar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad_kf)/SUM(sl.jingz)),2) AS qbad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt_kf)/SUM(sl.jingz)),2) AS mt_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad_kf)/SUM(sl.jingz)),2) AS mad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar_kf)/SUM(sl.jingz)),2) AS aar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad_kf)/SUM(sl.jingz)),2) AS aad_kf,\n"+
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf_kf)/SUM(sl.jingz)),2) AS vdaf_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std_kf)/SUM(sl.jingz)),2) AS std_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had_kf)/SUM(sl.jingz)),2) AS had_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad_kf)/SUM(sl.jingz)),2) AS fcad_kf,\n" + 
		"SUM(zhijbfml) AS zhijbfml,SUM(zhijbfje) AS zhijbfje, SUM(zhijbfje_m) AS zhijbfje_m,SUM(zhijbfje_a) AS zhijbfje_a,\n" + 
		"SUM(zhijbfje_v) AS zhijbfje_v,SUM(zhijbfje_q) AS zhijbfje_q,SUM(zhijbfje_s) AS zhijbfje_s,\n" + 
		"SUM(zhijbfje_t) AS zhijbfje_t,SUM(t.suopje) AS suopje, sum(t.lsuopsl) AS lsuopsl, sum(t.lsuopje) lsuopje\n" + 
		"from yuetjkjb tj,yuezlb t,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n" + 
		"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND tj.id = sl.yuetjkjb_id AND sl.fenx = t.fenx\n" + 
		"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+" AND t.fenx='����'\n" + 
		"UNION\n" + 
		" select -1 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
		"'' as yunsfsb_id,'�ۼƺϼ�' AS fenx,sum(sl.jingz) jingz,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar)/SUM(sl.jingz)),2) AS qnet_ar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad)/SUM(sl.jingz)),2) AS qbad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt)/SUM(sl.jingz)),2) AS mt,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad)/SUM(sl.jingz)),2) AS mad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar)/SUM(sl.jingz)),2) AS aar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad)/SUM(sl.jingz)),2) AS aad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*ad)/SUM(sl.jingz)),2) AS ad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf)/SUM(sl.jingz)),2) AS vdaf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std)/SUM(sl.jingz)),2) AS std,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had)/SUM(sl.jingz)),2) AS had,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad)/SUM(sl.jingz)),2) AS fcad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar_kf)/SUM(sl.jingz)),2) AS qnet_ar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad_kf)/SUM(sl.jingz)),2) AS qbad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt_kf)/SUM(sl.jingz)),2) AS mt_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad_kf)/SUM(sl.jingz)),2) AS mad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar_kf)/SUM(sl.jingz)),2) AS aar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad_kf)/SUM(sl.jingz)),2) AS aad_kf,\n"+
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf_kf)/SUM(sl.jingz)),2) AS vdaf_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std_kf)/SUM(sl.jingz)),2) AS std_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had_kf)/SUM(sl.jingz)),2) AS had_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad_kf)/SUM(sl.jingz)),2) AS fcad_kf,\n" + 
		"SUM(zhijbfml) AS zhijbfml,SUM(zhijbfje) AS zhijbfje, SUM(zhijbfje_m) AS zhijbfje_m,SUM(zhijbfje_a) AS zhijbfje_a,\n" + 
		"SUM(zhijbfje_v) AS zhijbfje_v,SUM(zhijbfje_q) AS zhijbfje_q,SUM(zhijbfje_s) AS zhijbfje_s,\n" + 
		"SUM(zhijbfje_t) AS zhijbfje_t,SUM(t.suopje) AS suopje, sum(t.lsuopsl) AS lsuopsl, sum(t.lsuopje) lsuopje\n" + 
		"from yuetjkjb tj,yuezlb t,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n" + 
		"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND tj.id = sl.yuetjkjb_id AND sl.fenx = t.fenx\n" + 
		"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+" AND t.fenx='�ۼ�'\n" + 
		"UNION\n" + 
		"select t.id,tj.id yuetjkjb_id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" + 
		"yunsfsb.mingc as yunsfsb_id,t.fenx,sl.jingz,qnet_ar, qbad, mt, mad, aar, aad, ad,\n" + 
		"vdaf, std, had, fcad, qnet_ar_kf, qbad_kf, mt_kf, mad_kf,\n" + 
		"aar_kf,aad_kf, vdaf_kf, std_kf, had_kf, fcad_kf, zhijbfml,zhijbfje,zhijbfje_m,\n" + 
		"zhijbfje_a,zhijbfje_v,zhijbfje_q,zhijbfje_s,zhijbfje_t,t.suopje, lsuopsl, lsuopje\n" + 
		"from yuetjkjb tj,yuezlb t,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n" + 
		"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND tj.id = sl.yuetjkjb_id AND sl.fenx = t.fenx\n" + 
		"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
		") ORDER BY gongysb_id desc,jihkjb_id,pinzb_id,fenx";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomKey_zl);
		// ����grid���
		egu.setWidth("bodyWidth");
		// ����Ϊ�༭����ҳ
		egu.addPaging(0);
		// ����Ϊ�ɱ༭grid
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ������ʾ������
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ���ñ���
		egu.setTableName("yuezlb");
		// �����б���
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("qnet_ar").setCenterHeader("�յ�����λ��(Qnet,ar(Mj/kg))", 6, 2);
		egu.getColumn("qbad").setCenterHeader("��Ͳ��(Qbad)", 3, 2);
		egu.getColumn("mt").setCenterHeader("ȫˮ(Mt)", 2, 2);
		egu.getColumn("mad").setCenterHeader("�ոɻ�ˮ(Mad)", 4, 2);
		egu.getColumn("aar").setCenterHeader("�յ����ҷ�(Aar)", 5, 2);
		egu.getColumn("aad").setCenterHeader("�ոɻ��ҷ�(Aad)", 5, 2);
		egu.getColumn("ad").setCenterHeader("������ҷ�(Ad)", 5, 2);
		egu.getColumn("vdaf").setCenterHeader("�����޻һ��ӷ���(Vdaf)", 8, 2);
		egu.getColumn("std").setCenterHeader("�ɻ���(Std)", 3, 2);
		egu.getColumn("had").setCenterHeader("�ոɻ���(Had)", 4, 2);
		egu.getColumn("fcad").setCenterHeader("�̶�̼(Fcad)", 3, 2);
		egu.getColumn("qnet_ar_KF").setCenterHeader("�󷽵�λ��(Qnet,ar(Mj/kg))", 5,2);
		egu.getColumn("qbad_KF").setCenterHeader("�󷽵�Ͳ��(Qbad)", 5, 2);
		egu.getColumn("mt_KF").setCenterHeader("��ȫˮ(Mt)", 4, 2);
		egu.getColumn("mad_KF").setCenterHeader("�󷽿ոɻ�ˮ(Mad)", 6, 2);
		egu.getColumn("aar_KF").setCenterHeader("���յ����ҷ�(Aar)", 7, 2);
		egu.getColumn("aad_KF").setCenterHeader("�󷽿ոɻ��ҷ�(Aad)", 7, 2);
		egu.getColumn("vdaf_KF").setCenterHeader("�󷽻ӷ���(Vdaf)", 5, 2);
		egu.getColumn("std_KF").setCenterHeader("�󷽸ɻ���(Std)", 5, 2);
		egu.getColumn("had_KF").setCenterHeader("�󷽿ոɻ���(Had)", 6, 2);
		egu.getColumn("fcad_KF").setCenterHeader("�󷽹̶�̼(Fcad)", 5, 2);
		egu.getColumn("zhijbfml").setCenterHeader("�ʼ۲���ú��(��)", 4, 2);
		egu.getColumn("zhijbfje").setCenterHeader("�ʼ۲������(Ԫ)", 4, 2);
		egu.getColumn("zhijbfje_m").setCenterHeader("ˮ�ֲ������(Ԫ)", 4, 2);
		egu.getColumn("zhijbfje_a").setCenterHeader("�ҷֲ������(Ԫ)", 4, 2);
		egu.getColumn("zhijbfje_v").setCenterHeader("�ӷ��ֲ������(Ԫ)", 4, 2);
		egu.getColumn("zhijbfje_q").setCenterHeader("��ֵ�������(Ԫ)", 4, 2);
		egu.getColumn("zhijbfje_s").setCenterHeader("�򲻷����(Ԫ)", 4, 2);
		egu.getColumn("zhijbfje_t").setCenterHeader("�ӷ��ֲ������(Ԫ)", 4, 2);
		egu.getColumn("suopje").setCenterHeader("������(Ԫ)", 2, 2);
		egu.getColumn("lsuopsl").setCenterHeader("����������(��)", 3, 2);
		egu.getColumn("lsuopje").setCenterHeader("��������(Ԫ)", 3, 2);

		// �����п�
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("qnet_ar").setWidth(90);
		egu.getColumn("qbad").setWidth(60);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("ad").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("std").setWidth(60);
		egu.getColumn("had").setWidth(60);
		egu.getColumn("fcad").setWidth(60);
		egu.getColumn("qnet_ar_KF").setWidth(90);
		egu.getColumn("qbad_KF").setWidth(60);
		egu.getColumn("mt_KF").setWidth(60);
		egu.getColumn("mad_KF").setWidth(60);
		egu.getColumn("aar_KF").setWidth(60);
		egu.getColumn("aad_KF").setWidth(60);
		egu.getColumn("vdaf_KF").setWidth(60);
		egu.getColumn("std_KF").setWidth(60);
		egu.getColumn("had_KF").setWidth(60);
		egu.getColumn("fcad_KF").setWidth(60);
		egu.getColumn("zhijbfml").setWidth(60);
		egu.getColumn("zhijbfje").setWidth(60);
		egu.getColumn("zhijbfje_m").setWidth(60);
		egu.getColumn("zhijbfje_a").setWidth(60);
		egu.getColumn("zhijbfje_v").setWidth(60);
		egu.getColumn("zhijbfje_q").setWidth(60);
		egu.getColumn("zhijbfje_s").setWidth(60);
		egu.getColumn("zhijbfje_t").setWidth(60);
		egu.getColumn("suopje").setWidth(60);
		egu.getColumn("lsuopsl").setWidth(60);
		egu.getColumn("lsuopje").setWidth(60);
		// ���ò��ɱ༭
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("pinzb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		// ���ò������ֶ�
		egu.getColumn("yuetjkjb_id").setUpdate(false);
		egu.getColumn("gongysb_id").setUpdate(false);
		egu.getColumn("jihkjb_id").setUpdate(false);
		egu.getColumn("pinzb_id").setUpdate(false);
		egu.getColumn("yunsfsb_id").setUpdate(false);
		egu.getColumn("fenx").setUpdate(false);
		egu.getColumn("jingz").setUpdate(false);
		// ����������
		egu.getColumn("yuetjkjb_id").setHidden(true);
		// �������������
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		// �����·�������
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		// ���÷ָ���
		egu.addTbarText("-");
		// ���õ糧ѡ����
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// ���÷ָ���
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
//		�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con,"yuezlb")){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){"
				+ "if(e.record.get('ID')=='-1' || e.record.get('ID')=='-2'){"
				+ " e.cancel=true;" + "}" + "});");
		setExtGrid(egu);
	}

	private void initYuejsbmj(JDBCcon con, String diancxxb_id, String CurrODate) {
		// ͬ���½����ú��������ͳ�ƿھ���
		updateTable(con, diancxxb_id, CurrODate, TableName_jsbmj);
		String sql = "select -2 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
					"'' as yunsfsb_id,'���ºϼ�' fenx,sum(jiesl) jiesl,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(meij*jiesl)/SUM(jiesl)),2) meij,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(meijs*jiesl)/SUM(jiesl)),2) meijs,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(kuangqyf*jiesl)/SUM(jiesl)),2) kuangqyf,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(yunj*jiesl)/SUM(jiesl)),2) yunj,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(yunjs*jiesl)/SUM(jiesl)),2) yunjs,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(daozzf*jiesl)/SUM(jiesl)),2) daozzf,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(zaf*jiesl)/SUM(jiesl)),2) zaf,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(zafs*jiesl)/SUM(jiesl)),2) zafs,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(qit*jiesl)/SUM(jiesl)),2) qit,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2) qnet_ar,\n" + 
					"ROUND(decode(ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2),0,0," +
					"(ROUND(decode(sum(jiesl),0,0,SUM(meij*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(yunj*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(kuangqyf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(daozzf*jiesl)/SUM(jiesl)),2) +" +
					"ROUND(decode(sum(jiesl),0,0,SUM(zaf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qit*jiesl)/SUM(jiesl)),2))*29.271/" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2)),3) biaomdj,\n" +
					"ROUND(decode(ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2),0,0," +
					"(ROUND(decode(sum(jiesl),0,0,SUM(meij*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(yunj*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(kuangqyf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(daozzf*jiesl)/SUM(jiesl)),2) +" +
					"ROUND(decode(sum(jiesl),0,0,SUM(zaf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qit*jiesl)/SUM(jiesl)),2)-" +
					"ROUND(decode(sum(jiesl),0,0,SUM(meijs*jiesl)/SUM(jiesl)),2)-" +
					"ROUND(decode(sum(jiesl),0,0,SUM(yunjs*jiesl)/SUM(jiesl)),2)-" +
					"ROUND(decode(sum(jiesl),0,0,SUM(zafs*jiesl)/SUM(jiesl)),2))*29.271/" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2)),3) buhsbmdj\n" +
//					"ROUND(decode(sum(jiesl),0,0,SUM(biaomdj*jiesl)/SUM(jiesl)),2) biaomdj,\n" + 
//					"ROUND(decode(sum(jiesl),0,0,SUM(buhsbmdj*jiesl)/SUM(jiesl)),2) buhsbmdj\n" + 
					"from yuetjkjb tj,yuejsbmdj t,gongysb,jihkjb,pinzb,yunsfsb\n" + 
					"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
					"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND t.fenx = '����'\n" + 
					"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
					"\n" + 
					"union\n" + 
					"select -1 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
					"'' as yunsfsb_id,'�ۼƺϼ�' fenx,sum(jiesl) jiesl,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(meij*jiesl)/SUM(jiesl)),2) meij,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(meijs*jiesl)/SUM(jiesl)),2) meijs,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(kuangqyf*jiesl)/SUM(jiesl)),2) kuangqyf,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(yunj*jiesl)/SUM(jiesl)),2) yunj,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(yunjs*jiesl)/SUM(jiesl)),2) yunjs,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(daozzf*jiesl)/SUM(jiesl)),2) daozzf,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(zaf*jiesl)/SUM(jiesl)),2) zaf,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(zafs*jiesl)/SUM(jiesl)),2) zafs,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(qit*jiesl)/SUM(jiesl)),2) qit,\n" + 
					"ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2) qnet_ar,\n" + 
					"ROUND(decode(ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2),0,0," +
					"(ROUND(decode(sum(jiesl),0,0,SUM(meij*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(yunj*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(kuangqyf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(daozzf*jiesl)/SUM(jiesl)),2) +" +
					"ROUND(decode(sum(jiesl),0,0,SUM(zaf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qit*jiesl)/SUM(jiesl)),2))*29.271/" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2)),3) biaomdj,\n" +
					"ROUND(decode(ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2),0,0," +
					"(ROUND(decode(sum(jiesl),0,0,SUM(meij*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(yunj*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(kuangqyf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(daozzf*jiesl)/SUM(jiesl)),2) +" +
					"ROUND(decode(sum(jiesl),0,0,SUM(zaf*jiesl)/SUM(jiesl)),2)+" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qit*jiesl)/SUM(jiesl)),2)-" +
					"ROUND(decode(sum(jiesl),0,0,SUM(meijs*jiesl)/SUM(jiesl)),2)-" +
					"ROUND(decode(sum(jiesl),0,0,SUM(yunjs*jiesl)/SUM(jiesl)),2)-" +
					"ROUND(decode(sum(jiesl),0,0,SUM(zafs*jiesl)/SUM(jiesl)),2))*29.271/" +
					"ROUND(decode(sum(jiesl),0,0,SUM(qnet_ar*jiesl)/SUM(jiesl)),2)),3) buhsbmdj\n" +
//					"ROUND(decode(sum(jiesl),0,0,SUM(biaomdj*jiesl)/SUM(jiesl)),2) biaomdj,\n" + 
//					"ROUND(decode(sum(jiesl),0,0,SUM(buhsbmdj*jiesl)/SUM(jiesl)),2) buhsbmdj\n" + 
					"from yuetjkjb tj,yuejsbmdj t,gongysb,jihkjb,pinzb,yunsfsb\n" + 
					"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
					"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND t.fenx = '�ۼ�'\n" + 
					"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
					"union\n" + 
					"select t.id,tj.id yuetjkjb_id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" + 
					"yunsfsb.mingc as yunsfsb_id,fenx,jiesl,meij,meijs,kuangqyf,yunj,yunjs,daozzf,zaf,zafs,qit,qnet_ar,biaomdj,buhsbmdj\n" + 
					"from yuetjkjb tj,yuejsbmdj t,gongysb,jihkjb,pinzb,yunsfsb\n" + 
					"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
					"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id\n" + 
					"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
					"order by gongysb_id desc,jihkjb_id,pinzb_id,yuetjkjb_id,fenx";

//			"select t.id,tj.id yuetjkjb_id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n"
//				+ "yunsfsb.mingc as yunsfsb_id,fenx,jiesl,meij,meijs,kuangqyf,yunj,yunjs,daozzf,zaf,zafs,qit,qnet_ar,biaomdj,buhsbmdj\n"
//				+ "from yuetjkjb tj,yuejsbmdj t,gongysb,jihkjb,pinzb,yunsfsb\n"
//				+ "where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n"
//				+ "and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id \n"
//				+ "and tj.diancxxb_id="
//				+ diancxxb_id
//				+ " and tj.riq="
//				+ CurrODate + "\n" + "order by t.yuetjkjb_id,t.fenx ";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);//,CustomKey_jsbmj
		// ����grid���
		egu.setWidth("bodyWidth");
		// ����Ϊ�༭����ҳ
		egu.addPaging(0);
		// ����Ϊ�ɱ༭grid
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ������ʾ������
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ���ñ���
		egu.setTableName("yuejsbmdj");
		
		// �����б���
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("jiesl").setHeader("������<br>(��)");
		egu.getColumn("meij").setHeader("��˰ú��<br>(Ԫ/��)");
		((NumberField) egu.getColumn("meij").editor).setDecimalPrecision(3);
		egu.getColumn("meijs").setHeader("ú��˰<br>(Ԫ/��)");
		((NumberField) egu.getColumn("meijs").editor).setDecimalPrecision(3);
		egu.getColumn("kuangqyf").setHeader("����ǰ�ӷ�<br>(Ԫ/��)");
		((NumberField) egu.getColumn("kuangqyf").editor).setDecimalPrecision(3);
		egu.getColumn("yunj").setHeader("��˰�˼�<br>(Ԫ/��)");
		((NumberField) egu.getColumn("yunj").editor).setDecimalPrecision(3);
		egu.getColumn("yunjs").setHeader("�˼�˰<br>(Ԫ/��)");
		((NumberField) egu.getColumn("yunjs").editor).setDecimalPrecision(3);
		egu.getColumn("daozzf").setHeader("��վ�ӷ�<br>(Ԫ/��)");
		((NumberField) egu.getColumn("daozzf").editor).setDecimalPrecision(3);
		egu.getColumn("zaf").setHeader("�ӷ�<br>(Ԫ/��)");
		((NumberField) egu.getColumn("zaf").editor).setDecimalPrecision(3);
		egu.getColumn("zafs").setHeader("�ӷ�˰<br>(Ԫ/��)");
		((NumberField) egu.getColumn("zafs").editor).setDecimalPrecision(3);
		egu.getColumn("qit").setHeader("����<br>(Ԫ/��)");
		((NumberField) egu.getColumn("qit").editor).setDecimalPrecision(3);
		egu.getColumn("qnet_ar").setHeader("��������<br>(MJ/kg)");
		((NumberField) egu.getColumn("qnet_ar").editor).setDecimalPrecision(3);
		egu.getColumn("biaomdj").setHeader("��ú����<br>(Ԫ/��)");
		((NumberField) egu.getColumn("biaomdj").editor).setDecimalPrecision(3);
		egu.getColumn("buhsbmdj").setHeader("����˰��ú����<br>(Ԫ/��)");
		((NumberField) egu.getColumn("buhsbmdj").editor).setDecimalPrecision(3);
		// �����п�
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("jiesl").setWidth(60);
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("kuangqyf").setWidth(70);
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("daozzf").setWidth(60);
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("zafs").setWidth(60);
		egu.getColumn("qit").setWidth(60);
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("buhsbmdj").setWidth(100);
		// ���ò��ɱ༭
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("gongysb_id").setEditor(null);
		egu
				.getColumn("gongysb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu
				.getColumn("jihkjb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pinzb_id").setEditor(null);
		egu
				.getColumn("pinzb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu
				.getColumn("yunsfsb_id")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setEditor(null);
		egu
				.getColumn("fenx")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");
		// ���ò������ֶ�
		egu.getColumn("yuetjkjb_id").setUpdate(false);
		egu.getColumn("gongysb_id").setUpdate(false);
		egu.getColumn("jihkjb_id").setUpdate(false);
		egu.getColumn("pinzb_id").setUpdate(false);
		egu.getColumn("yunsfsb_id").setUpdate(false);
		egu.getColumn("fenx").setUpdate(false);
		// ����������
		egu.getColumn("yuetjkjb_id").setHidden(true);
		// �������������
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		// �����·�������
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		// ���÷ָ���
		egu.addTbarText("-");
		// ���õ糧ѡ����
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// ���÷ָ���
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		
//		�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con,"yuejsbmdj")){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		//�޸�buhsbmdj��daoczhj-meijs-yunjs-zafs
		String aftereditScript = "gridDiv_grid.on('afteredit',function(e){\n"
				+ "    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0,meijs=0,yunjs=0,zafs=0;\n"
				+ "    i=e.row;\n"
				+ "	if(eval(gridDiv_ds.getAt(i).get('QNET_AR')) != 0){\n"
				+ "    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ'))+eval(gridDiv_ds.getAt(i).get('KUANGQYF'))+eval(gridDiv_ds.getAt(i).get('YUNJ'))+eval(gridDiv_ds.getAt(i).get('DAOZZF'))+eval(gridDiv_ds.getAt(i).get('ZAF'))+eval(gridDiv_ds.getAt(i).get('QIT'));\n"
//				+ "    meijs=eval(gridDiv_ds.getAt(i).get('MEIJ'))-Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ'))/1.17,2);\n"
//				+ "    yunjs=eval(gridDiv_ds.getAt(i).get('YUNJ'))-Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ'))*0.93,2);\n"
				+ "    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),3);\n"
				+ "    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS'))-eval(gridDiv_ds.getAt(i).get('YUNJS'))-eval(gridDiv_ds.getAt(i).get('ZAFS')))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),3);\n"
				+ "\n" + "    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n"
//				+ "    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n"
//				+ "    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n"
				+ "    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n"
				+ "    }\n" + "});";
		//String sql_script=" select Id from xitxxb where mingc='�±�ҳ�����' and  zhi='��' and leib='�±�' and beiz='ʹ��' ";
		//ResultSetList rsl_script = con.getResultSetList(sql_script);
		
		if(MainGlobal.getXitxx_item("�±�", "�±��ۼ��Զ�����", diancxxb_id, "��").equals("��")){
			String beforeeditScript="gridDiv_grid.on('beforeedit',function(e){if(e.record.get('FENX')=='�ۼ�'){e.cancel=true;}});\n";
			egu.addOtherScript(beforeeditScript);
		}
		if(MainGlobal.getXitxx_item("�±�", "�±�ҳ�����", diancxxb_id, "��").equals("��")){
			egu.addOtherScript(aftereditScript);
		}else{
//			egu.addOtherScript(aftereditScript);
		}
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){"
				+ "if(e.record.get('ID')=='-1' || e.record.get('ID')=='-2'){"
				+ " e.cancel=true;" + "}" + "});");
		setExtGrid(egu);
	}

	/**
	 * @describe ��ʼ��ҳ��
	 * 
	 */
	public void initGrid() {
		// �õ���ǰ�糧����ѡ��ĵ糧
		String diancxxb_id = getTreeid();
		// �õ���ǰѡ�����¶�Ӧ��Oracle��ʽ������
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		JDBCcon con = new JDBCcon();
		if (MonthReportType_sl.equalsIgnoreCase(getMonthReportType())) {
			initYuesl(con, diancxxb_id, CurrODate);
		} else if (MonthReportType_zl.equalsIgnoreCase(getMonthReportType())) {
			initYuezl(con, diancxxb_id, CurrODate);
		} else if (MonthReportType_hc.equalsIgnoreCase(getMonthReportType())) {

		} else if (MonthReportType_rcbmj.equalsIgnoreCase(getMonthReportType())) {

		} else if (MonthReportType_jsbmj.equalsIgnoreCase(getMonthReportType())) {
			initYuejsbmj(con, diancxxb_id, CurrODate);
		} else if (MonthReportType_tjkj.equalsIgnoreCase(getMonthReportType())) {
			initYuetjkj(con, diancxxb_id, CurrODate);
		}
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "��" + getYuef() + "��";
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
		boolean initGrid = false;
		String lx = cycle.getRequestContext().getParameter("lx");
		if (lx != null) {
			visit.setString4(cycle.getRequestContext().getParameter("lx"));
			initGrid = true;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			if (lx == null) {
				visit.setString4(MonthReportType_sl);
			}
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			setExtGrid(null);
			initGrid = true;
		}
		if (initGrid) {
			initGrid();
		}
	}

	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		return con.getHasIt("select zhuangt from shujshb where diancxxb_id = "
				+ getTreeid() + "\n"
				+ "and mokmc = 'Yueqfmk' and zhuangt = 1 and riq = "
				+ CurrODate);
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	// ���������
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�������
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// ��Ӧ��
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getGongysModels() {
		String sql = "select g.dqid,g.dqmc from vwgongys g order by g.dqxh";
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "��ѡ��"));
		return;
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

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}