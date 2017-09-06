package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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
/*����:��衻�
 * ʱ��:2010-10-9 
 * ������shouhcfkb��ά��ҳ�档
 * 
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-07-12
 * ���������ӵ�λ���ĳ�ʼ������
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-08-01
 * ������������Ӱ�ť������ׯ�ӵ糧ʹ��
 * 		 ������ϵͳ��Ϣ���е����ݶԽ����й�Ӧ�̣�ú��λ��Ʒ����Ϣ�Ƿ�ɱ༭�����޶���
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-09-19
 * ���÷�Χ����������������е�λ
 * �����������ƻ��ھ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-10-12
 * ���÷�Χ���������������λ����ׯ�ӵ糧��
 * ����������ʱ�����¹�Ӧ�̣�ú��Ʒ�ֺͼƻ��ھ���Ϣ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-10-28
 * ���÷�Χ���������������λ����ׯ�ӵ糧��
 * ��������������ˢ��SQL
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-1
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * �������Է�������ú��Ϣ������Լ�ұ���2λС��
 * 		��������ú������ϵͳ��
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-4
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * ���������������߼������û��������ʱֻ�����������ݣ������ɵ���Ϣ������ú�����⣩�����ı�
 * 		  �����Ҫ�������ɣ�����Ҫ������ɾ���������ٴ����ɡ�
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-11
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * �����������չ�����ú�Զ�����������Ϣ���жϣ�Ĭ��ϵͳ�Զ�����������Ϣ��
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-16
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * �����������չ��۽��������ж���Ϣ�������2��������������治��ʾ�༭��ذ�ť��
 * 		 �����չ��۽��������ж���Ϣ��������г��˼��⣬��Ϣ��ȫ���ܽ��б��档
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-18
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * ��������ϵͳ��Ϣ������ȡϵͳ�趨���ձ����ɱ༭���ڣ�Ĭ��Ϊ2��
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-23
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * �������û�������ɰ�ťʱͬʱ�����պĴ��ձ��ͷֿ��չ�����Ϣ
 * 		  ��������ʾ�ϼ��У����Һϼ���ֻ���ڱ����ż���ú�ۣ��˼ۣ���ֵ����������Ϣ
 * 		  ¼������е���ֵ��ϢΪ0ʱ����ǰ�е���Ϣ�����ɱ��༭
 * 		 ��ú������ֵ��Ϣ�����ɱ��༭�Ҿ���ϵͳ�Զ����ɣ������糧��ú�����ɱ༭����
 * 			������ϵͳ�������պĴ��ձ���ú�ɱ༭�������պĴ��ձ���ֵ�ɱ༭��������н���������
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-25
 * ���÷�Χ�������������������λ����ׯ�ӵ糧��
 * ����������ǰ̨�����������߼��쳣
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-02-29
 * ʹ�÷�Χ�������������������λ
 * ����������ѡ��λ�з��ӵ�λ��ϵͳ���á��ֳ����ܳ���ʾ���ɰ�ť����ֵΪ��ʱ��
 * 		 ϵͳ���������ɣ�ɾ�������水ť��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-03
 * ���÷�Χ���������
 * ����������"��ú�Ƿ����ֳ���"���ж�����	
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-11
 * ���÷�Χ���������(��ׯ�ӵ糧)
 * ������������˰��ú������	
 */
/*
 * ����:���
 * ����:2013-09-26
 * �޸�����:ú�ۺ��˼۽����ӹ���ȡ��Ĭ��Ϊ0��
 * 			����ʱ����ú��˰���˼�˰�����˼ۣ����˼�˰�������˼ۣ������˼�˰
 */	
/*
 * ����:���
 * ����:2013-10-16
 * �޸�����:�������䷽ʽ
 */	
/*
 * ����:���
 * ����:2014-03-26
 * �޸�����:�ձ����˷�Ӫ����
 * 		 	���Ӳ�������·�˷�˰�Ƿ�����ֵ˰��Ĭ��ֵΪ���ǡ�
 * 		 	���Ӳ�������·�˷���ֵ˰�ʡ�Ĭ��ֵΪ��0.11��
 */	
public abstract class Shouhcfkb extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	������
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		JDBCcon con=new JDBCcon();
		ResultSetList changList=getExtGrid().getModifyResultSet(getChange());
		int rs=0;
		String sql="";
		while(changList.next()){
			String id=changList.getString("id");
			if(id.equals("-1")){
				continue;
			}
			double rez=changList.getDouble("rez");
			double meij=changList.getDouble("meij");
			double yunj=changList.getDouble("yunj");
			double laimsl=changList.getDouble("laimsl");
			
			double meijs=changList.getDouble("meijs");
			double yunjs=changList.getDouble("yunjs");
			double huocyj=changList.getDouble("huocyj");
			double huocyjs=changList.getDouble("huocyjs");
			double qicyj=changList.getDouble("qicyj");
			double qicyjs=changList.getDouble("qicyjs");
			
			sql+="update shouhcfkb\n" +
				"   set rez = "+rez+", meij = "+meij+", meijs = "+meijs+",\n" +
						" huocyj = "+huocyj+", huocyjs = "+huocyjs+", \n" +
						" qicyj = "+qicyj+", qicyjs = "+qicyjs+",\n" +
						" yunjs = "+yunjs+", yunj = "+yunj+",laimsl="+laimsl+"\n" + 
				" where id = "+id+";\n";

		}
		if(sql.length()!=0){
			rs=con.getInsert("begin\n"+sql.toString()+"end;");
			if (rs==-1) {
				setMsg("����ʧ�ܣ�");
			}else{
				setMsg("����ɹ���");
			}
		}
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}
	private void CreateData() {
		long diancxxb_id = Long.parseLong(getTreeid());
		JDBCcon con = new JDBCcon();
//		����ʱ�Զ��������պĴ�ͷֿ�����
		AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
		String rbb=RP.CreateRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String fcb=RP.CreateFCB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String Smsg="";
		if(rbb.length()>0){
			Smsg+=rbb+"<br>";
		}
		if(fcb.length()>0){
			Smsg+=fcb+"<br>";
		}
		if(Smsg.length()>0){
			setMsg(Smsg);
		}
		con.Close();
	}

	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("delete from shouhcfkb where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq = ")
		.append(CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
//		�����Ƿ�ɱ༭
		boolean isEditable=true;
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select s.id,\n" +
				"       g.mingc  gongysmc,\n" + 
				"       m.mingc  meikmc,\n" + 
				"       p.mingc  pinz,\n" + 
				"       j.mingc  JIHKJB_ID,\n" +
				"		DECODE(Y.MINGC, NULL, '����', Y.MINGC) YUNSFSB_ID," + 
				"       f.biaoz,\n" + 
				"       f.ches,\n" + 
				"       s.laimsl, s.rez,\n" + 
				"       s.meij,\n" + 
				"       s.meijs,\n" + 
				"       s.huocyj,\n" + 
				"       s.huocyjs,\n" + 
				"       s.qicyj,\n" + 
				"       s.qicyjs,\n" + 
				"       s.yunj,\n" + 
				"       s.yunjs,\n" + 
				"       ROUND(DECODE(S.REZ, 0, 0, (S.MEIJ + S.YUNJ) * 29.271 / S.REZ), 2) BMDJ,\n" + 
				"       ROUND(DECODE(S.REZ, 0, 0, (S.MEIJ + S.YUNJ-S.MEIJS-S.YUNJS) * 29.271 / S.REZ), 2) BHSBMDJ,0 LEIB\n" + 
				"  from shouhcfkb s,\n" + 
				"       meikxxb m,\n" + 
				"       gongysb g,pinzb p,JIHKJB j,yunsfsb y,\n" + 
				"       (select diancxxb_id,\n" + 
				"               daohrq,\n" + 
				"               gongysb_id,pinzb_id,JIHKJB_ID,\n" + 
				"               meikxxb_id,\n" + 
				"               nvl(sum(round_new(biaoz,0)), 0) biaoz,\n" + 
				"               nvl(sum(ches), 0) ches\n" + 
				"          from fahb\n" + 
				"         group by diancxxb_id, daohrq, gongysb_id, meikxxb_id,pinzb_id,JIHKJB_ID) f\n" + 
				" where s.riq = f.daohrq(+)\n" + 
				"   and s.gongysb_id = f.gongysb_id(+)\n" +
				"   and s.meikxxb_id = f.meikxxb_id(+)\n" + 
				"   AND s.PINZB_ID=f.PINZB_ID(+)\n" +
				"   AND s.jihkjb_id=f.JIHKJB_ID(+)\n" +
				"   and s.JIHKJB_ID=j.ID(+)\n" +
				"	AND s.YUNSFSB_ID=y.id(+)" +
				"   and s.pinzb_id = p.id(+) \n"+
				"   and s.meikxxb_id = m.id(+)\n" + 
				"   and s.gongysb_id = g.id(+)\n" + 
				"   and s.riq = "+CurDate+"\n" );
		
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "��ú�Ƿ����ֳ���", "0", "��").equals("��")){
			sb.append("   and s.diancxxb_id = f.diancxxb_id(+)\n");
			sb.append("   and s.diancxxb_id in (select id from diancxxb where id="+diancxxb_id+" or fuid="+diancxxb_id+")");
		}
		
		JDBCcon con = new JDBCcon();
//		�ж���ѡ�������Ƿ�������
		if(con.getHasIt(sb.toString())){
			Date curdate=new Date();
			long t=(curdate.getTime()-DateUtil.getDate(getRiq()).getTime())/(3600*24*1000);
//			���ʱ�����2�죬��ô������ʾ�༭��ذ�ť��Ϊ�ر�״̬
//			��ϵͳ��Ϣ����ȡ���ձ����ɱ༭������Ϣ��Ĭ��ֵΪ2�֡�
			if(t>Long.parseLong(MainGlobal.getXitxx_item("�պĴ��ձ�", "�ձ����ɱ༭����", "0", "2"))){
				isEditable=false;
			}
		}
//		������������Ϣ
		sb.append(
				"   UNION\n" +
				"SELECT -1 ID,'�ܼ�' GONGYSMC,MEIKMC,PINZ,JIHKJB_ID,YUNSFSB_ID,BIAOZ,CHES,LAIMSL,REZ,\n" +
				"		MEIJ,MEIJS,HUOCYJ,HUOCYJS,QICYJ,QICYJS,YUNJ,YUNJS,\n" +
				"		ROUND(DECODE(REZ, 0, 0, (MEIJ + YUNJ) * 29.271 / REZ), 2) BMDJ,\n" +
				"		ROUND(DECODE(REZ, 0, 0, (MEIJ + YUNJ-MEIJS-YUNJS) * 29.271 / REZ), 2) BHSBMDJ,1 LEIB\n" + 
				"  FROM (SELECT G.MINGC GONGYSMC,\n" + 
				"               M.MINGC MEIKMC,\n" + 
				"               P.MINGC PINZ,\n" + 
				"               J.MINGC JIHKJB_ID,\n" +
				"				DECODE(GROUPING(G.MINGC),1,'',DECODE(Y.MINGC, NULL, '����', Y.MINGC)) YUNSFSB_ID,\n" + 
				"               SUM(F.BIAOZ) BIAOZ,\n" + 
				"               SUM(F.CHES) CHES,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.REZ,0,0,S.LAIMSL)), 0, 0, SUM(S.REZ * S.LAIMSL) / SUM(DECODE(S.REZ,0,0,S.LAIMSL))),2) REZ,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(S.MEIJ * S.LAIMSL) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) MEIJ,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJS,0,0,S.LAIMSL)), 0, 0, SUM(S.MEIJS * S.LAIMSL) / SUM(DECODE(S.MEIJS,0,0,S.LAIMSL))),2) MEIJS,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.huocyj) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) huocyj,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.huocyjs) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) huocyjs,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.qicyj) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) qicyj,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.qicyjs) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) qicyjs,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.YUNJ) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) YUNJ,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.yunjs) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) YUNJS,\n" + 
				"               ROUND_NEW(SUM(S.LAIMSL),2) LAIMSL\n" + 
				"          FROM SHOUHCFKB S,\n" + 
				"               MEIKXXB M,\n" + 
				"               GONGYSB G,\n" + 
				"               PINZB P,\n" + 
				"               JIHKJB J,yunsfsb y,\n" + 
				"               (SELECT DIANCXXB_ID,\n" + 
				"                       DAOHRQ,\n" + 
				"                       GONGYSB_ID,\n" + 
				"                       PINZB_ID,\n" + 
				"                       JIHKJB_ID,\n" + 
				"                       MEIKXXB_ID,\n" + 
				"                       NVL(SUM(ROUND_NEW(BIAOZ, 0)), 0) BIAOZ,\n" + 
				"                       NVL(SUM(CHES), 0) CHES\n" + 
				"                  FROM FAHB\n" + 
				"                 GROUP BY DIANCXXB_ID,\n" + 
				"                          DAOHRQ,\n" + 
				"                          GONGYSB_ID,\n" + 
				"                          MEIKXXB_ID,\n" + 
				"                          PINZB_ID,\n" + 
				"                          JIHKJB_ID) F\n" + 
				"         WHERE S.RIQ = F.DAOHRQ(+)\n" + 
				"           AND S.GONGYSB_ID = F.GONGYSB_ID(+)\n" + 
				"           AND S.MEIKXXB_ID = F.MEIKXXB_ID(+)\n" + 
				"           AND S.PINZB_ID = F.PINZB_ID(+)\n" + 
				"           AND S.JIHKJB_ID = F.JIHKJB_ID(+)\n" + 
				"           AND S.JIHKJB_ID = J.ID(+)\n" + 
				"           AND S.PINZB_ID = P.ID(+)\n" + 
				"           AND s.yunsfsb_id=y.id(+)\n" + 
				"           AND S.MEIKXXB_ID = M.ID(+)\n" + 
				"           AND S.GONGYSB_ID = G.ID(+)\n" + 
				"           AND S.RIQ = "+CurDate+"\n");
				if(MainGlobal.getXitxx_item("�պĴ��ձ�", "��ú�Ƿ����ֳ���", "0", "��").equals("��")){
					sb.append("   and s.diancxxb_id = f.diancxxb_id(+)\n");
					sb.append("   and s.diancxxb_id in (select id from diancxxb where id="+diancxxb_id+" or fuid="+diancxxb_id+")");
				}
		sb.append("         GROUP BY ROLLUP((G.MINGC, M.MINGC, P.MINGC, J.MINGC,y.mingc))\n" + 
				  "        HAVING GROUPING(G.MINGC) = 1) ORDER BY LEIB DESC, BMDJ");

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		String yunjslx = MainGlobal.getXitxx_item("����", "�˷�˰�Ƿ�����ֵ˰", diancxxb_id, "��");
		String huoqlx = MainGlobal.getXitxx_item("�±�", "�˷��Ƿ��������", diancxxb_id, "��");
		
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcfkb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("gongysmc").setHeader("������λ");
		egu.getColumn("gongysmc").setWidth(160);
		egu.getColumn("gongysmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("meikmc").setHeader("ú��λ");
		egu.getColumn("meikmc").setWidth(160);
		egu.getColumn("meikmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
				
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("JIHKJB_ID").setHeader("ͳ�ƿھ�");
		egu.getColumn("JIHKJB_ID").setWidth(60);
		egu.getColumn("JIHKJB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("YUNSFSB_ID").setHeader("���䷽ʽ");
		egu.getColumn("YUNSFSB_ID").setWidth(60);
		egu.getColumn("YUNSFSB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("YUNSFSB_ID").setEditor(null);
		
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(40);
		egu.getColumn("ches").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("laimsl").setHeader("��ú����");
		egu.getColumn("laimsl").setWidth(70);
		
		egu.getColumn("rez").setHeader("��ֵ<br>(MJ/kg)");
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		egu.getColumn("rez").setWidth(50);
		
		egu.getColumn("meij").setHeader("��˰ú��<br>(Ԫ/��)");
		((NumberField)egu.getColumn("meij").editor).setDecimalPrecision(2);
		egu.getColumn("meij").setWidth(60);
		
		egu.getColumn("meijS").setHeader("ú��˰<br>(Ԫ/��)");
		egu.getColumn("meijS").setWidth(60);
		egu.getColumn("meijS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meijS").setEditor(null);
		
		egu.getColumn("HUOCYJ").setHeader("��<br>��˰�˼�<br>(Ԫ/��)");
		egu.getColumn("HUOCYJ").setWidth(60);
		egu.getColumn("HUOCYJ").editor.setMinValue("0");
		((NumberField)egu.getColumn("HUOCYJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("HUOCYJS").setHeader("��<br>�˼�˰<br>(Ԫ/��)");
		egu.getColumn("HUOCYJS").setWidth(60);
		egu.getColumn("HUOCYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("HUOCYJS").setEditor(null);
		
		egu.getColumn("QICYJ").setHeader("����<br>��˰�˼�<br>(Ԫ/��)");
		egu.getColumn("QICYJ").setWidth(60);
		egu.getColumn("QICYJ").editor.setMinValue("0");
		((NumberField)egu.getColumn("QICYJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("QICYJS").setHeader("����<br>�˼�˰<br>(Ԫ/��)");
		egu.getColumn("QICYJS").setWidth(60);
		egu.getColumn("QICYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("QICYJS").setEditor(null);
		
		egu.getColumn("yunj").setHeader("���˼�<br>(Ԫ/��)");
		egu.getColumn("yunj").editor.setMinValue("0");
		((NumberField)egu.getColumn("yunj").editor).setDecimalPrecision(2);
		egu.getColumn("yunj").setWidth(60);
		
//		��������ʱ���˼۽����ɱ༭��
		if(huoqlx.equals("��")){
			egu.getColumn("HUOCYJ").setHidden(true);
			egu.getColumn("HUOCYJS").setHidden(true);
			egu.getColumn("QICYJ").setHidden(true);
			egu.getColumn("QICYJS").setHidden(true);
		}else{
			egu.getColumn("yunj").setEditor(null);
			egu.getColumn("yunj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		}
		egu.getColumn("yunjs").setHeader("���˼�˰<br>(Ԫ/��)");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("BMDJ").setEditor(null);
		egu.getColumn("BMDJ").setHeader("��˰<br>��ú����<br>(Ԫ/��)");
		egu.getColumn("BMDJ").setWidth(60);
		egu.getColumn("BMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("BMDJ").setUpdate(false);
		
		egu.getColumn("BHSBMDJ").setEditor(null);
		egu.getColumn("BHSBMDJ").setHeader("����˰<br>��ú����<br>(Ԫ/��)");
		egu.getColumn("BHSBMDJ").setWidth(60);
		egu.getColumn("BHSBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("BHSBMDJ").setUpdate(false);
		
		egu.getColumn("LEIB").setEditor(null);
		egu.getColumn("LEIB").setHidden(true);
		egu.getColumn("LEIB").setUpdate(false);
		
		
		if("��".equals(MainGlobal.getXitxx_item("�ձ�", "�չ���ά�������Ƿ�����", getTreeid(), "��"))){
			egu.getColumn("biaoz").setHidden(true);
		}
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ú�ɱ༭", "0", "��").equals("��")){
			egu.getColumn("laimsl").setEditor(null);
			egu.getColumn("laimsl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		}
		
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ֵ�ɱ༭", "0", "��").equals("��")){
			egu.getColumn("rez").setEditor(null);
			egu.getColumn("rez").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		}
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.Binding("Riq","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('Riq').value+'������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish); 
		egu.addTbarBtn(gbr);
		
		if(isEditable){
//			���ɰ�ť
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�պĴ��ձ�", "�ֳ����ܳ���ʾ���ɰ�ť", diancxxb_id, "��").equals("��")){
				
			}else{
				GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
//				ɾ����ť
				GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
//				���水ť
				GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
				egu.addTbarBtn(gbs);
				egu.addTbarText("-");
				egu.addTbarText("�����ˢ���ܼ������ݣ����������쳣���ֶ�ɾ�����ٴ�����");
			}
		}
		
		String meijs = MainGlobal.getXitxx_item("����", "ú��˰��", diancxxb_id, "0.17");
		String yunjsl = MainGlobal.getXitxx_item("����", "�˷�˰��", diancxxb_id, "0.07");		
		String yunjzzsl = MainGlobal.getXitxx_item("����", "�˷���ֵ˰��", diancxxb_id, "0.11");	
		String Tlyjslx = MainGlobal.getXitxx_item("����", "��·�˷�˰�Ƿ�����ֵ˰", diancxxb_id, "��");
		String Tlyjzzsl = MainGlobal.getXitxx_item("����", "��·�˷���ֵ˰��", diancxxb_id, "0.11");	
		
//		grid ���㷽��
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){"+
				"if(e.field=='GONGYSMC'||e.field=='MEIKMC'||e.field=='PINZ'||e.field=='JIHKJB_ID'||e.record.get('ID')=='-1'){e.cancel=true;}\n" +
				"if(e.field=='MEIJ'||e.field=='YUNJ'||e.field=='HUOCYJ'||e.field=='QICYJ'){if(e.record.get('REZ')==0){e.cancel=true;}}\n" +
				"});\n");
		
		StringBuffer sbAF = new StringBuffer();
		sbAF.append("gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n"); 
		if(huoqlx.equals("��")){
			sbAF.append("  if(e.field=='HUOCYJ'){\n" + 
					"    var HCyunjs=0,i=0;\n" + 
					"    i=e.row;\n" );
			if(Tlyjslx.equals("��")){
				sbAF.append("HCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)-(eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)/(1+" + Tlyjzzsl + ")),2);\n");
			}else{
				sbAF.append("HCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)*" + yunjsl + ",2);\n" );
			}

			sbAF.append("    gridDiv_ds.getAt(i).set('HUOCYJS',HCyunjs);\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJ',eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)+eval(gridDiv_ds.getAt(i).get('QICYJ')||0));\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJS',eval(gridDiv_ds.getAt(i).get('HUOCYJS')||0)+eval(gridDiv_ds.getAt(i).get('QICYJS')||0));}\n" );
			
			sbAF.append("  if(e.field=='QICYJ'){\n" + 
					"    var QCyunjs=0,i=0;\n" + 
					"    i=e.row;\n" );
			if(yunjslx.equals("��")){
				sbAF.append("QCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('QICYJ')||0)-(eval(gridDiv_ds.getAt(i).get('QICYJ')||0)/(1+" + yunjzzsl + ")),2);\n");
			}else{
				sbAF.append("QCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('QICYJ')||0)*" + yunjsl + ",2);\n" );
			}
			sbAF.append("   gridDiv_ds.getAt(i).set('QICYJS',QCyunjs);\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJ',eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)+eval(gridDiv_ds.getAt(i).get('QICYJ')||0));\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJS',eval(gridDiv_ds.getAt(i).get('HUOCYJS')||0)+eval(gridDiv_ds.getAt(i).get('QICYJS')||0));}\n" );
		}
				
		sbAF.append("  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" );
		if(yunjslx.equals("��")){
			sbAF.append("if(gridDiv_ds.getAt(i).get('YUNSFSB_ID')=='��·'){  \n");
			if(Tlyjslx.equals("��")){
				sbAF.append(" yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + Tlyjzzsl + ")),2);\n");
			}else{
				sbAF.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n");
			}
			sbAF.append("} else{  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + yunjzzsl + ")),2);}\n");
		}else{
			sbAF.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n" );
		}
									
		sbAF.append("    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n" + 
				"  if(e.field=='HUOCYJ'||e.field=='QICYJ'||e.field=='MEIJ'||e.field=='YUNJ'||e.field=='REZ'){\n" + 
				"    var daoczhj=0,biaomdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ')||0)+eval(gridDiv_ds.getAt(i).get('YUNJ')||0);\n" + 
				" 	if(eval(gridDiv_ds.getAt(i).get('REZ'))!=0){\n" +
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('REZ')||0),2);\n" + 
				"   }\n" + 
				"    gridDiv_ds.getAt(i).set('BMDJ',biaomdj);\n" + 
				"  }\n" + 
				"});"
		);
		egu.addOtherScript(sbAF.toString());
		
		AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
		String errmsg=DR.ChkFKB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		if(errmsg.length()>0){
			egu.addOtherScript("Ext.MessageBox.alert('��ʾ��Ϣ','"+errmsg+"�����ݲ�����,�벹ȫ��');\n");
			
		}
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('Riq').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�ͬʱ����:���պĴ���չ���<br>")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
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
		if(getExtGrid() == null) {
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
			visit.setList1(null);
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setChangbModels();
			getSelectData();
			setTreeid(null);
		}
	}
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
}
