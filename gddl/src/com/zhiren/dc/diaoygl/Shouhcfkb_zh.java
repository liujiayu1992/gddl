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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2011-09-26
 * ���÷�Χ���������ׯ�ӵ糧
 * ���������ڹ������ͨ�ð汾(1.1.1.16)Ϊׯ�ӵ糧��������������������Ľ�Ŀ
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-1
 * ���÷�Χ���������ׯ�ӵ糧
 * �������Է�������ú��Ϣ������Լ�ұ���2λС��
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-17
 * ���÷�Χ��ׯ�ӵ糧
 * �����������չ��۽��������ж���Ϣ�������2��������������治��ʾ�༭��ذ�ť��
 * 		 �����չ��۽��������ж���Ϣ��������г��˼��⣬��Ϣ��ȫ���ܽ��б��档
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-18
 * ���÷�Χ��ׯ�ӵ糧
 * ��������ϵͳ��Ϣ������ȡϵͳ�趨���ձ����ɱ༭���ڣ�Ĭ��Ϊ2��
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-23
 * ���÷�Χ��ׯ�ӵ糧
 * �������û�������ɰ�ťʱͬʱ�����պĴ��ձ��ͷֿ��չ�����Ϣ
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-12-20
 * ���÷�Χ��ׯ�ӵ糧
 * ����������������ʾ
 */
/*
 * ����:���
 * ����:2013-09-26
 * �޸�����:ú�ۺ��˼۽����ӹ���ȡ��Ĭ��Ϊ0��
 * 			����ʱ����ú��˰���˼�˰
 */
/*
 * ����:���
 * ����:2013-10-16
 * �޸�����:�������䷽ʽ
 */
public abstract class Shouhcfkb_zh extends BasePage {
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
		String diancxxb_id = getTreeid();
		int rs=0;
		String sql="";
		while(changList.next()){
			String id=changList.getString("id");
			double rez=changList.getDouble("rez");
			double meij=changList.getDouble("meij");
			double yunj=changList.getDouble("yunj");
			double meijs=changList.getDouble("meijs");
			double yunjs=changList.getDouble("yunjs");
			double laimsl=changList.getDouble("laimsl");
			double fahb_id=0.0;
			
			if ("0".equals(changList.getString("id"))) {
				sql=
					"insert into shouhcfkb(id,diancxxb_id,fahb_id,rez,meij,meijs,yunj,yunjs,laimsl,riq,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id) values(\n" +
					"getnewid("+diancxxb_id+"),"+diancxxb_id+","+fahb_id+"," +
					rez+","+meij+","+meijs+","+yunj+","+yunjs+","+laimsl+"," +
					DateUtil.FormatOracleDate(getRiq())+"," +
					(getExtGrid().getColumn("meikmc").combo).getBeanId(changList.getString("meikmc"))+"," +
					(getExtGrid().getColumn("pinz").combo).getBeanId(changList.getString("pinz"))+"," +
					(getExtGrid().getColumn("gongysmc").combo).getBeanId(changList.getString("gongysmc")) +"," +
					(getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(changList.getString("JIHKJB_ID"))+"," +
					(getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(changList.getString("yunsfsb_id"))+
					");\n";
			}else{
				sql=
					"update shouhcfkb\n" +
					"   set rez = "+rez+", meij = "+meij+", yunj = "+yunj+", meijs = "+meijs+", yunjs = "+yunjs+",laimsl="+laimsl+",\n" + 
							"meikxxb_id = " +(getExtGrid().getColumn("meikmc").combo).getBeanId(changList.getString("meikmc"))+"," +
							"pinzb_id = " +(getExtGrid().getColumn("pinz").combo).getBeanId(changList.getString("pinz"))+"," +
							"gongysb_id = " +(getExtGrid().getColumn("gongysmc").combo).getBeanId(changList.getString("gongysmc"))+"," +
							"JIHKJB_ID = " +(getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(changList.getString("JIHKJB_ID"))+"," +
							"yunsfsb_id = " +(getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(changList.getString("yunsfsb_id"))+"," +
							"FAHB_ID="+fahb_id+
					" where id = "+id+";\n";
			}
		}
		if(sql.length()!=0){
			rs=con.getInsert("begin\n"+sql.toString()+"\nend;");
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

	private String Fahxx;
	public void setFahxx(){
		String rslSQL=getFahxxSQL();
		JDBCcon con = new JDBCcon();
		ResultSetList list=con.getResultSetList(rslSQL.toString());
		String rsl="var fahxx=[ ";
		while(list.next()){
			String fahb_id=list.getString("FAHXX");
			String gongys=list.getString("GONGYS");
			String MEIK=list.getString("MEIK");
			String JIHKJ=list.getString("JIHKJ");
			String PINZ=list.getString("PINZ");
			String yunsfs=list.getString("yunsfsb_id");
			String LAIMSL=list.getString("LAIMSL");
			String REZ=list.getString("REZ");
			String MEIJ=list.getString("MEIJ");
			String YUNJ=list.getString("YUNJ");
			String MEIJS=list.getString("MEIJS");
			String YUNJS=list.getString("YUNJS");
			rsl+="[\""+fahb_id+"\",\""+gongys+"\",\""+MEIK+"\",\""+JIHKJ+"\",\""+PINZ+"\",\""+yunsfs+"\",\""+LAIMSL+"\",\""+REZ+"\",\""+MEIJ+"\",\""+MEIJS+"\",\""+YUNJ+"\",\""+YUNJS+"\"],";
		}
		rsl=rsl.substring(0, rsl.length()-1);
		rsl+="];";
		this.Fahxx=rsl;
	}
	
	public String getFahxx(){
		return this.Fahxx;
	}
	
	private String getFahxxSQL(){
		return 
		"SELECT G.MINGC || TO_CHAR(A.DAOHRQ, 'yyyy-mm-dd') || '��' || A.LAIMSL || '��' FAHXX,\n" +
		"       G.MINGC GONGYS,\n" + 
		"       M.MINGC MEIK,\n" + 
		"       J.MINGC JIHKJ,\n" + 
		"       P.MINGC PINZ,\n" + 
		"       y.MINGC yunsfsb_id,\n" + 
		"       A.DAOHRQ,\n" + 
		"       A.LAIMSL,\n" + 
		"       ROUND_NEW(NVL(DECODE(NVL(A.REZ,0), 0, B.REZ, A.REZ), 0),2) REZ,\n" + 
//		"       ROUND_NEW(NVL(DECODE(A.MEIJ, 0, B.MEIJ, A.MEIJ), 0),2) MEIJ,\n" + 
//		"       ROUND_NEW(NVL(DECODE(A.YUNJ, 0, B.YUNJ, A.YUNJ), 0),2) YUNJ\n" + 
		"       ROUND_NEW(NVL(B.MEIJ, 0),2) MEIJ,\n" + 
		"       ROUND_NEW(NVL(B.MEIJS, 0),2) MEIJS,\n" + 
		"       ROUND_NEW(NVL(B.YUNJ, 0),2) YUNJ,\n" + 
		"       ROUND_NEW(NVL(B.YUNJS, 0),2) YUNJS\n" + 
		"  FROM (SELECT F.GONGYSB_ID,\n" + 
		"               F.MEIKXXB_ID,\n" + 
		"               F.DAOHRQ,\n" + 
		"               F.PINZB_ID,\n" + 
		"               F.JIHKJB_ID,\n" + 
		"               F.yunsfsb_id,\n" + 
		"               SUM(F.LAIMSL) LAIMSL,\n" + 
		"               DECODE(SUM(F.LAIMSL),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      SUM(F.LAIMSL * Z.QNET_AR) / SUM(F.LAIMSL)) REZ,\n" + 
		"               DECODE(SUM(F.LAIMSL),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      SUM(F.LAIMSL * (NVL(G.MEIJ, 0) + NVL(G.MEIS, 0))) /\n" + 
		"                      SUM(F.LAIMSL)) MEIJ,\n" + 
		"               DECODE(SUM(F.LAIMSL),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      SUM(F.LAIMSL * (NVL(G.YUNF, 0) + NVL(G.YUNFS, 0))) /\n" + 
		"                      SUM(F.LAIMSL)) YUNJ\n" + 
		"          FROM ( --ȡ�ù�����Ϣ\n" + 
		"                SELECT ID, FAHB_ID, MEIJ, MEIS, YUNF, YUNFS\n" + 
		"                  FROM GUSLSB\n" + 
		"                -- �õ�ͬһ���͵�������ID\n" + 
		"                 WHERE ID IN (SELECT MAX(G.ID)\n" + 
		"                                FROM GUSLSB G,\n" + 
		"                                     --ȡ��ͬһ����������͵Ĺ�����Ϣ\n" + 
		"                                     (SELECT FAHB_ID, MAX(LEIX) LEIX\n" + 
		"                                        FROM GUSLSB\n" + 
		"                                       GROUP BY FAHB_ID) G2\n" + 
		"                               WHERE G.FAHB_ID = G2.FAHB_ID\n" + 
		"                                 AND G.LEIX = G2.LEIX\n" + 
		"                               GROUP BY G.FAHB_ID)) G,\n" + 
		"               FAHB F,\n" + 
		"               ZHILB Z\n" + 
		"         WHERE F.ZHILB_ID = Z.ID(+)\n" + 
		"           AND F.ID = G.FAHB_ID(+)\n" + 
		"           AND F.DAOHRQ BETWEEN "+DateUtil.FormatOracleDate(getRiq())+"- 10 AND\n" + 
		"               "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
		"         GROUP BY F.GONGYSB_ID,\n" + 
		"                  F.MEIKXXB_ID,\n" + 
		"                  F.DAOHRQ,\n" + 
		"                  F.PINZB_ID,\n" + 
		"                  F.JIHKJB_ID,f.yunsfsb_id) A,\n" + 
		"       --ȡ���ձ���ÿ����Ӧ�̡�ú������Ӧ�����һ��¼�������\n" + 
		"       (SELECT MEIKXXB_ID, GONGYSB_ID, PINZB_ID, JIHKJB_ID,yunsfsb_id,REZ, MEIJ, YUNJ,MEIJS,YUNJS\n" + 
		"          FROM SHOUHCFKB\n" + 
		"         WHERE ID IN\n" + 
		"               (SELECT MAX(ID)\n" + 
		"                  FROM SHOUHCFKB\n" + 
		"                 WHERE RIQ BETWEEN "+DateUtil.FormatOracleDate(getRiq())+"- 10 AND\n" + 
		"                       "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
		"                 GROUP BY MEIKXXB_ID, GONGYSB_ID, PINZB_ID, JIHKJB_ID)) B,\n" + 
		"       GONGYSB G,\n" + 
		"       MEIKXXB M,\n" + 
		"       JIHKJB J,yunsfsb y,\n" + 
		"       PINZB P\n" + 
		" WHERE A.GONGYSB_ID = B.GONGYSB_ID(+)\n" + 
		"   AND A.MEIKXXB_ID = B.MEIKXXB_ID(+)\n" + 
		"   AND A.PINZB_ID = B.PINZB_ID(+)\n" + 
		"   AND A.JIHKJB_ID = B.JIHKJB_ID(+)\n" + 
		"   AND A.yunsfsb_id = B.yunsfsb_id(+)\n" + 
		"   AND A.yunsfsb_id = y.id\n" + 
		"   AND A.GONGYSB_ID = G.ID\n" + 
		"   AND A.MEIKXXB_ID = M.ID\n" + 
		"   AND A.JIHKJB_ID = J.ID\n" + 
		"   AND A.PINZB_ID = P.ID\n" + 
		" ORDER BY A.DAOHRQ";
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
				"       g.mingc||to_char(f.daohrq,'yyyy-mm-dd')||'��'||f.laimsl||'��' fahb_id,\n" + 
				"       g.mingc  gongysmc,\n" + 
				"       m.mingc  meikmc,\n" + 
				"       p.mingc  pinz,\n" + 
				"       j.mingc  JIHKJB_ID,\n" + 
				"       y.mingc  yunsfsb_id,\n" + 
				"       f.biaoz,\n" + 
				"       f.ches,\n" + 
				"       s.rez,\n" + 
				"       s.meij,\n" + 
				"       s.meijs,\n" + 
				"       s.yunj,\n" + 
				"       s.yunjs,\n" + 
				"       s.laimsl\n" + 
				"  from shouhcfkb s,\n" + 
				"       meikxxb m,\n" + 
				"       gongysb g,pinzb p,JIHKJB j,yunsfsb y,\n" + 
				"       (select diancxxb_id,\n" + 
				"               daohrq,\n" + 
				"               gongysb_id,pinzb_id,yunsfsb_id,\n" + 
				"               meikxxb_id,\n" + 
				"               nvl(sum(round_new(LAIMSL,0)), 0) LAIMSL,\n" +
				"				nvl(sum(round_new(biaoz,0)), 0) biaoz,\n" + 
				"               nvl(sum(ches), 0) ches\n" + 
				"          from fahb\n" + 
				"			where daohrq="+CurDate+"\n" +
				"         group by diancxxb_id, daohrq, gongysb_id, meikxxb_id,pinzb_id,yunsfsb_id) f\n" + 
				" where s.riq = f.daohrq(+) and s.JIHKJB_ID=j.ID(+)\n" + 
				"   and s.diancxxb_id = f.diancxxb_id(+)\n" + 
				"   and s.meikxxb_id = f.meikxxb_id(+)\n" + 
				"   and s.yunsfsb_id = f.yunsfsb_id(+)\n" + 
				"   and s.yunsfsb_id = y.id(+)\n" + 
				"   and s.pinzb_id = p.id(+)\n" + 
				"   and s.gongysb_id = f.gongysb_id(+)\n" + 
				"   and s.meikxxb_id = m.id(+)\n" + 
				"   and s.gongysb_id = g.id(+)\n" + 
				"   and s.riq = "+CurDate+"\n" + 
				"   and s.diancxxb_id in (select id from diancxxb where id="+getTreeid()+" or fuid="+getTreeid()+")");
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
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcfkb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("fahb_id").setHeader("������Ϣ");
		egu.getColumn("fahb_id").setWidth(200);
		ComboBox cb_fahxx = new ComboBox();
		egu.getColumn("fahb_id").setEditor(cb_fahxx);
		egu.getColumn("fahb_id").setDefaultValue("");
		cb_fahxx.setEditable(true);
		String cb_fahxxSql="SELECT rownum id,\n" +
							"       fahxx\n" + 
							"  FROM (\n" + 
							getFahxxSQL()+
							") F \n" + 
							"UNION\n" + 
							"SELECT 0 ID, DECODE(0, 0, '�޷�����Ϣ') MINGC FROM DUAL ORDER BY ID";
		egu.getColumn("fahb_id").setReturnId(true);
		egu.getColumn("fahb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_fahxxSql));
		egu.getColumn("fahb_id").setDefaultValue("�޷�����Ϣ");
		egu.getColumn("fahb_id").setUpdate(false);
		egu.getColumn("fahb_id").editor.setAllowBlank(false);
		
		
		egu.getColumn("gongysmc").setHeader("������λ");
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysmc").setEditor(cb_gongysb);
		egu.getColumn("gongysmc").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql="select id,mingc from gongysb  where leix =1 order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
		
		egu.getColumn("meikmc").setHeader("ú��λ");
		ComboBox c3 = new ComboBox();
		egu.getColumn("meikmc").setEditor(c3);
		c3.setEditable(true);
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikmc").setComboEditor(egu.gridId,new IDropDownModel(mksb));
				
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(80);
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinz").setEditor(c2);
		c2.setEditable(true);
		String pzsb = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinz").setComboEditor(egu.gridId,new IDropDownModel(pzsb));
		
		egu.getColumn("JIHKJB_ID").setHeader("ͳ�ƿھ�");
		egu.getColumn("JIHKJB_ID").setWidth(80);
		ComboBox ct = new ComboBox();
		egu.getColumn("JIHKJB_ID").setEditor(ct);
		ct.setEditable(true);
		String kjsb = "select id,mingc from JIHKJB order by mingc";
		egu.getColumn("JIHKJB_ID").setComboEditor(egu.gridId,new IDropDownModel(kjsb));
		
		egu.getColumn("YUNSFSB_ID").setHeader("���䷽ʽ");
		egu.getColumn("YUNSFSB_ID").setWidth(80);
		ComboBox cy = new ComboBox();
		egu.getColumn("YUNSFSB_ID").setEditor(cy);
		cy.setEditable(true);
		String cysb = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("YUNSFSB_ID").setComboEditor(egu.gridId,new IDropDownModel(cysb));
		
		egu.getColumn("biaoz").setHeader("����");
		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(40);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("rez").setHeader("��ֵ(MJ/kg)");
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		egu.getColumn("rez").setWidth(90);
		
		
		egu.getColumn("meij").setHeader("ú��<br>(Ԫ/��)");
		((NumberField)egu.getColumn("meij").editor).setDecimalPrecision(2);
		egu.getColumn("meij").setWidth(80);
		
		egu.getColumn("meijS").setHeader("ú��˰<br>(Ԫ/��)");
		egu.getColumn("meijS").setWidth(60);
		egu.getColumn("meijS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meijS").setEditor(null);
		
		egu.getColumn("yunj").setHeader("�˼�(Ԫ/��)");
		((NumberField)egu.getColumn("yunj").editor).setDecimalPrecision(2);
		egu.getColumn("yunj").setWidth(80);
		
		egu.getColumn("yunjs").setHeader("�˼�˰<br>(Ԫ/��)");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("laimsl").setHeader("��ú����");
		egu.getColumn("laimsl").setWidth(100);
		
		if("��".equals(MainGlobal.getXitxx_item("�ձ�", "�չ���ά�������Ƿ�����", getTreeid(), "��"))){
			egu.getColumn("biaoz").setHidden(true);
		}
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.Binding("RIQ","");
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
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish); 
		egu.addTbarBtn(gbr);
		
		if(isEditable){
	//		��Ӱ�ť
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
	//		���ɰ�ť
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�պĴ��ձ�", "�ֳ����ܳ���ʾ���ɰ�ť", diancxxb_id, "��").equals("��")){
				
			}else{
				GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
			}
	//		ɾ����ť
			GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
	//		���水ť
			String Condition="\n var mrec=gridDiv_ds.getModifiedRecords();\n"+
							"for(i = 0; i< mrec.length; i++){\n"+
							"	if(mrec[i].get('FAHB_ID') == '�޷�����Ϣ'){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������Ϣ ӦΪ��Ч������Ϣ');return;}\n"+
							"	}\n"+
							"var rec = gridDiv_ds.getRange();\n" +
							"for(i = 0; i< rec.length; i++){if(rec[i].get('REZ') <=0 || rec[i].get('MEIJ') <=0)\n" +
							"{Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,����δ��ȫ');return;}}";
			GridButton gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton",Condition);
			egu.addTbarBtn(gbs);
			
			String yunjslx = MainGlobal.getXitxx_item("����", "�˷�˰�Ƿ�����ֵ˰", diancxxb_id, "��");
			String meijs = MainGlobal.getXitxx_item("����", "ú��˰��", diancxxb_id, "0.17");
			String yunjsl = MainGlobal.getXitxx_item("����", "�˷�˰��", diancxxb_id, "0.07");		
			String yunjzzsl = MainGlobal.getXitxx_item("����", "�˷���ֵ˰��", diancxxb_id, "0.11");		
			
	//		grid ���㷽��
			StringBuffer script=new StringBuffer();
			script.append(
				"gridDiv_grid.on('afteredit',function(e)\n"+
					"{\n"+
				"rec = e.record;\n"+
				"var fah=rec.get('FAHB_ID');\n"+
				"if(e.field=='FAHB_ID'& fah!='�޷�����Ϣ')\n"+
				"{\n"+
				"	for (var i=0;i<fahxx.length;i++ ){\n"+
				"		if(fahxx[i][0]==fah){\n"+
				"			rec.set('GONGYSMC',fahxx[i][1]);\n"+
				"			rec.set('MEIKMC',fahxx[i][2]);\n"+
				"			rec.set('JIHKJB_ID',fahxx[i][3]);\n"+
				"			rec.set('PINZ',fahxx[i][4]);\n"+
				"			rec.set('YUNSFSB_ID',fahxx[i][5]);\n"+
				"			rec.set('REZ',fahxx[i][7]);\n"+
				"			rec.set('MEIJ',fahxx[i][8]);\n"+
				"			rec.set('MEIJS',fahxx[i][9]);\n"+
				"			rec.set('YUNJ',fahxx[i][10]);\n"+
				"			rec.set('YUNJS',fahxx[i][11]);\n"+
				"			}\n"+
				"	}	\n"+
				"}\n"+
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n"+
				"  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n");
				if(yunjslx.equals("��")){
					script.append("if(gridDiv_ds.getAt(i).get('YUNSFSB_ID')=='��·'){  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);}\n" ); 
					script.append(" else{  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + yunjzzsl + ")),2);}\n");
				}else{
					script.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n" );
				}
				script.append("    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n");
				
				script.append("  if(e.field=='YUNSFSB_ID'){\n" + 
						"    var yunjs=0,i=0;\n" + 
						"    i=e.row;\n"); 
				if(yunjslx.equals("��")){
					script.append("if(gridDiv_ds.getAt(i).get('YUNSFSB_ID')=='��·'){  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);}\n" ); 
					script.append(" else{  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + yunjzzsl + ")),2);}\n");
				}else{
					script.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n" );
				}
				script.append("    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n");
				
				script.append("});\n");
			egu.addOtherScript(script.toString());
		}
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
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
		setFahxx();
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
