package com.zhiren.dc.monthReport.gd;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuebmdjrlzb extends BasePage implements PageValidateListener {
	
	private static final int LAST_HAOYL = 0;
	
	private static final int LAST_QICL = 1;
	
	private static final int LAST_QICDJ = 2;
	
	private static final int LAST_RULL = 3;
	
	private static final int LAST_RUCDJ = 4;
	
	private static final int LAST_RUCHDJ = 5;
	
	private static final int LAST_PINGJJ = 6;
	
	private static final int LAST_Farl = 7;
	
	private static final int LAST_MT = 8;
	
	private static final int LAST_AAD = 9;
	
	private static final int LAST_YOUHYL = 10;
	
	private static final int LAST_YOUPJDJ = 11;
	
	private static final int LAST_YOUPJRL = 12;
	
	private static final int LAST_BIAOMDJ = 13;
	
	private static final String BY = "����";
	
	private static final String LJ = "�ۼ�";
	
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
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
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
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
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
	
	public void DelData() {
		String diancxxb_id = this.getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrZnDate=getNianf()+"��"+getYuef()+"��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql=
			"delete from yuebmdjrlzb where riq="
			+CurrODate+" and diancxxb_id=" + diancxxb_id;
		
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("ɾ�������з�������");
		}else {
			setMsg(CurrZnDate+"�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}
	
	private void getLastData(double[] lastData, String lastDate, 
			JDBCcon con, String diancxxb_id, String fenx, long yuebjzb_id) {
		
		String sql= "select * from yuebmdjrlzb where riq=" + lastDate 
			+ " and fenx='" + fenx + "'"
			+ " and yuebjzb_id=" + yuebjzb_id
			+ " and diancxxb_id=" + diancxxb_id;
		
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {//�ж��Ƿ�����ʧ��
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return ;
		}
		
		if (rsl.next()) {
			lastData[LAST_HAOYL] = rsl.getDouble("haoyl");
			lastData[LAST_QICL] = rsl.getDouble("qicl");
			lastData[LAST_QICDJ] = rsl.getDouble("qicdj");
			lastData[LAST_RULL] = rsl.getDouble("rulsl");
			lastData[LAST_RUCDJ] = rsl.getDouble("rucdj");
			lastData[LAST_RUCHDJ] = rsl.getDouble("ruchdj");
			lastData[LAST_PINGJJ] = rsl.getDouble("pingjj");
			lastData[LAST_Farl] = rsl.getDouble("rez");
			lastData[LAST_MT] = rsl.getDouble("shoudjqs");
			lastData[LAST_AAD] = rsl.getDouble("konggjhf");
			lastData[LAST_YOUHYL] = rsl.getDouble("youhyl");
			lastData[LAST_YOUPJDJ] = rsl.getDouble("youpjdj");
			lastData[LAST_YOUPJRL] = rsl.getDouble("youpjrl");
			lastData[LAST_BIAOMDJ] = rsl.getDouble("biaomdj");
		} else {
			for (int i = 0; i< lastData.length; i++)
				lastData[i] = 0;
		}
				
	}
	
	private double getTianrmdj(JDBCcon con, String diancxxb_id, 
			String CurrODate, String strFenx) {
		
		double tianrmdj = 0;
		
		String sql = 
			"select fenx,decode(sum(nvl(shul,0)),0,0,round_new(sum(nvl(shul,0)*(nvl(meij,0)+nvl(yunf,0)+nvl(yunzf,0)))/sum(nvl(shul,0)),3)) as meij\n" +
			"from yuebmdjrczb\n" +
			"where riq=" + CurrODate + "\n" + 
			"and diancxxb_id=" + diancxxb_id + "\n" + 
			"and fenx='" + strFenx + "'" +
			"group by fenx";
		
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {//�ж��Ƿ�����ʧ��
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return tianrmdj;
		}
		while (rsl.next()) {
				tianrmdj = rsl.getDouble("meij");

		}
		return tianrmdj;
	}
	
	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate=getNianf()+"��"+getYuef()+"��";
		String CurrSDate=getNianf()+"-"+getYuef();
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		int intYuef = Integer.parseInt(getYuef());
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		String strSql="";
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		int flag;
		long lngId=0;
		double[] lastByData = new double[14];
		double[] lastLjData = new double[14];
		
	//ɾ�������ɵ�����	
		strSql = "delete from yuebmdjrlzb where to_char(riq,'yyyy-mm')='" 
			+ CurrSDate + "' and diancxxb_id=" + diancxxb_id;
		flag = con.getDelete(strSql);
		
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "��������SQL:" + strSql);
			con.Close();
			return;
		}
	
//		strSql = 
//			"SELECT JIZFZB_ID,\n" +
//			"       SUM(HAOY) AS HAOY,\n" + 
//			"       SUM(RULML) AS RULML,\n" + 
//			"       ROUND_NEW(DECODE(SUM(DECODE(QNET_AR, 0, 0, HAOY)),0,0,SUM(HAOY * QNET_AR)/SUM(DECODE(QNET_AR, 0, 0, HAOY))),0) AS QNET_AR,\n" + 
//			"       ROUND_NEW(DECODE(SUM(DECODE(MT, 0, 0, HAOY)),0,0,SUM(HAOY * MT) / SUM(DECODE(MT, 0, 0, HAOY))),1) AS MT,\n" + 
//			"       ROUND_NEW(DECODE(SUM(DECODE(AAD, 0, 0, HAOY)),0,0,SUM(HAOY * AAD) / SUM(DECODE(AAD, 0, 0, HAOY))),2) AS AAD\n" + 
//			"\n" + 
//			"  FROM (SELECT (NVL(HY.FADHY, 0) + NVL(HY.GONGRHY, 0) + NVL(HY.QITY, 0) + NVL(HY.FEISCY, 0)) AS HAOY,\n" + 
//			"               (NVL(HY.FADHY, 0) + NVL(HY.GONGRHY, 0) + NVL(HY.QITY, 0) + NVL(HY.FEISCY, 0)) AS RULML,\n" + 
//			"               JZ.ID AS JIZFZB_ID,\n" + 
//			"               --hy.jizfzb_id,\n" + 
//			"               (NVL(ZL.QNET_AR, 0) * 1000) AS QNET_AR,\n" + 
//			"               NVL(ZL.MT, 0) AS MT,\n" + 
//			"               NVL(ZL.AAD, 0) AS AAD\n" + 
//			"          FROM MEIHYB HY, RULMZLB ZL, JIZFZB JZ\n" + 
//			"         WHERE HY.RULMZLB_ID = ZL.ID\n" + 
//			"           AND JZ.ID = HY.JIZFZB_ID(+)\n" + 
//			"           AND TO_CHAR(HY.RULRQ, 'yyyy-mm') = '" + CurrSDate + "'\n" + 
//			"           AND HY.DIANCXXB_ID = "+ diancxxb_id + "\n" + 
//			"           AND JZ.DIANCXXB_ID = "+ diancxxb_id + "\n" + 
//			"\n" + 
//			"        )\n" + 
//			" GROUP BY JIZFZB_ID";
		
		strSql =
			"SELECT JZ.ID AS YUEBJZB_ID,\n" +
			"       0        AS HAOY,\n" + 
			"       0        AS RULML,\n" + 
			"       0        AS QNET_AR,\n" + 
			"       0        AS MT,\n" + 
			"       0        AS AAD\n" + 
			"  FROM YUEBJZB JZ\n" + 
			" WHERE JZ.DIANCXXB_ID = " + diancxxb_id;
	
		ResultSetList rsl = con.getResultSetList(strSql);
		
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		while (rsl.next()) {
			
//		ȡ��������
			getLastData(lastByData, LastODate, con, diancxxb_id, 
					BY, rsl.getLong("YUEBJZB_ID"));
			
			if (lastByData == null) {
				return;
			}
			
			getLastData(lastLjData, LastODate, con, diancxxb_id, 
					LJ, rsl.getLong("YUEBJZB_ID"));
			
			if (lastLjData == null) {
				return;
			}
			
//			�����ڳ�����������¯���������ڳ�����
			double byQicl = lastByData[LAST_RULL];;
			double byRull = rsl.getDouble("rulml");
			double byDj = lastByData[LAST_PINGJJ];
			
//			�ۼ��ڳ������ۼ���¯�����ۼ��ڳ�����
			double byLjQicl = lastLjData[LAST_RULL];
			double byLjrull = lastLjData[LAST_RULL] + rsl.getDouble("rulml");
			double byLjDj = lastLjData[LAST_PINGJJ];
			
//			ȡ�����볧���ۡ��ۼ��볧����
			double byrcdj = getTianrmdj(con, diancxxb_id, CurrODate, BY);	
			double byLjrcdj = getTianrmdj(con, diancxxb_id, CurrODate, LJ);			
//			if (byLjrull>0) {
//				byLjrcdj = CustomMaths.Round_new((byrcdj*byRull + lastLjData[LAST_RUCDJ] * lastLjData[LAST_RULL])/byLjrull,3);
//			} else if (byRull == byLjrull){
//				byLjrcdj = byrcdj;
//			}
			
//			�볧�����ƽ������
//			�����볧�󵥼��п��ܴ���һ�ű���ȡ������ʱûȷ��
			double byRuchdj = 0;
			double ljRuchdj = 0;
			if (byLjrull>0) {
				ljRuchdj = CustomMaths.Round_new((byRuchdj*byRull + lastLjData[LAST_RUCHDJ] * lastLjData[LAST_RULL])/byLjrull,2);
			} else if (byRull == byLjrull){
				ljRuchdj = byRuchdj;
			}
//			ƽ������
			double byPingjj =0;
			double ljPingjj = 0;
			
			if ((byQicl + byRull) > 0) {
				byPingjj = CustomMaths.Round_new((byQicl * byDj + byRull * (byrcdj + byRuchdj)) / (byQicl + byRull), 3);
			}
			
			if ((byLjQicl + byLjrull) > 0) {
				ljPingjj = CustomMaths.Round_new((byLjQicl * byLjDj + byLjrull * (byLjrcdj + ljRuchdj)) / (byLjQicl + byLjrull), 3);
			}
			
//			�ͺ���
			double byYouhyl = 0;
			double byYoupjdj = 0;
			double byYoupjrl = 0;
			double ljYouhyl = lastLjData[LAST_YOUHYL];
			double ljYoupjdj = lastLjData[LAST_YOUPJDJ];
			double ljYoupjrl = lastLjData[LAST_YOUPJRL];
			
//			��ú����
			double byBiaomdj = 0;
			double ljBiaomdj = 0;
			
			double byQnet_ar = rsl.getDouble("Qnet_ar");
			double ljQnet_ar = 0;
			double ljMt = 0;
			double ljAad = 0;
			
			if (byLjrull > 0) {
				ljQnet_ar = CustomMaths.Round_new((byQnet_ar * byRull + lastLjData[LAST_Farl]*lastLjData[LAST_RULL])/ byLjrull, 0);
				ljMt = CustomMaths.Round_new((rsl.getDouble("Mt")*byRull + lastLjData[LAST_MT]*lastLjData[LAST_RULL])/byLjrull, 1);				                                					                                                 					
				ljAad = CustomMaths.Round_new((rsl.getDouble("Aad")*byRull + lastLjData[LAST_AAD]*lastLjData[LAST_RULL])/byLjrull, 2);			                                					                 
			}
			if (byQnet_ar > 0) {
				byBiaomdj = CustomMaths.Round_new(byPingjj * 29271 / byQnet_ar, 2);
			}
			
			if (ljQnet_ar > 0) {
				ljBiaomdj = CustomMaths.Round_new(ljPingjj * 29271 / ljQnet_ar, 2);
			}
				
			
//			��������
			lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			strSql = 
				"insert into yuebmdjrlzb values(\n" +
				lngId + ",\n" + 
				CurrODate + ",\n" + 
			    "'����',\n" + 
			    diancxxb_id + ",\n" + 
				rsl.getLong("YUEBJZB_ID") + ",\n" + 
				rsl.getDouble("haoy") + ",\n" + 
				byQicl + ",\n" +
				byDj + ",\n" + 
				byRull + ",\n" + 
				byrcdj + ",\n" + 
				byRuchdj + ",\n" + 
				byPingjj + ",\n" + 
				byQnet_ar + ",\n" + 
				rsl.getDouble("Mt") + ",\n" + 
				rsl.getDouble("Aad") + ",\n" + 
				byYouhyl + ",\n" +
				byYoupjdj + ",\n" +
				byYoupjrl + ",\n" +
				byBiaomdj + "\n" + 
				")";
			
			flag = con.getInsert(strSql);
			if(flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
				setMsg("���ɹ��̳��ִ�������¯��ú�����ۼƲ���ʧ�ܣ�");
				con.rollBack();
				con.Close();
				return;
		  }
			
//		�ۻ�����
			if (intYuef == 1) {
				lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				strSql = 
					"insert into yuebmdjrlzb values(\n" +
					lngId + ",\n" + 
					CurrODate + ",\n" + 
					"'�ۼ�',\n" + 
				    diancxxb_id + ",\n" + 
					rsl.getLong("YUEBJZB_ID") + ",\n" + 
					rsl.getDouble("haoy") + ",\n" + 
					byQicl + ",\n" +
					byDj + ",\n" + 
					byRull + ",\n" + 
					byrcdj + ",\n" + 
					byRuchdj + ",\n" + 
					byPingjj + ",\n" + 
					byQnet_ar + ",\n" + 
					rsl.getDouble("Mt") + ",\n" + 
					rsl.getDouble("Aad") + ",\n" + 
					byYouhyl + ",\n" +
					byYoupjdj + ",\n" +
					byYoupjrl + ",\n" +
					byBiaomdj + "\n" + 
					")";
				
			} else {								
				lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				strSql = 
					"insert into yuebmdjrlzb values(\n" +
					lngId + ",\n" + 
					CurrODate + ",\n" + 
					"'�ۼ�',\n" + 
					diancxxb_id + ",\n" + 
					rsl.getLong("YUEBJZB_ID") + ",\n" + 
					(lastLjData[LAST_HAOYL] +rsl.getDouble("haoy")) + ",\n" + 
					byLjQicl + ",\n" +
					byLjDj + ",\n" + 
					byLjrull + ",\n" + 
					byLjrcdj + ",\n" + 
					ljRuchdj + ",\n" + 
					ljPingjj + ",\n" + 
					ljQnet_ar + ",\n" + 
					ljMt + ",\n" + 
					ljAad + ",\n" + 
					ljYouhyl + ",\n" +
					ljYoupjdj + ",\n" +
					ljYoupjrl + ",\n" +
					ljBiaomdj + "\n" + 
					")";
				
			}			
			flag = con.getInsert(strSql);
			if(flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
				setMsg("���ɹ��̳��ִ�������¯��ú�����ۼƲ���ʧ�ܣ�");
				con.rollBack();
				con.Close();
				return;
		  }
		}
		
		rsl.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"�����ݳɹ����ɣ�");
	}

	public void getSelectData() {
		Visit visit = (Visit)getPage().getVisit();
		long diancxxb_id = Long.parseLong(this.getTreeid());
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		JDBCcon con = new JDBCcon();
		
		boolean isDisable = false;
		if (visit.isFencb() && diancxxb_id == visit.getDiancxxb_id()) {
			isDisable = true;
//			isLocked = true;
		}
		String strSql =
			"SELECT RL.ID,\n" +
			"       JZ.MINGC AS YUEBJZB_ID,\n" + 
			"       FENX,\n" + 
			"       QICL,\n" + 
			"       QICDJ,\n" + 
			"       HAOYL,\n" + 
			"       RULSL,\n" + 
			"       RUCDJ,\n" + 
			"       RUCHDJ,\n" + 
			"       PINGJJ,\n" + 
			"       REZ,\n" + 
			"       SHOUDJQS,\n" + 
			"       KONGGJHF,\n" + 
			"       YOUHYL,\n" + 
			"       YOUPJDJ,\n" + 
			"       YOUPJRL,\n" + 
			"       BIAOMDJ\n" + 
			"  FROM YUEBMDJRLZB RL,YUEBJZB JZ\n" + 
			" WHERE RL.DIANCXXB_ID = " + diancxxb_id + "\n" + 
			"   AND RL.YUEBJZB_ID = JZ.ID\n" +
			"   AND RIQ = " + CurrODate + "\n" + 
			" ORDER BY JZ.MINGC, FENX";
				
		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("yuebmdjrlzb");
		// /������ʾ������
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.getColumn("YUEBJZB_ID").setHeader("����");
		egu.getColumn("YUEBJZB_ID").setWidth(60);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("fenx").setWidth(40);
		egu.getColumn("haoyl").setHeader("������");
		egu.getColumn("haoyl").setWidth(60);
		egu.getColumn("qicl").setHeader("�ڳ���");
		egu.getColumn("qicl").setWidth(60);
		egu.getColumn("qicdj").setHeader("�ڳ�����");
		egu.getColumn("qicdj").setWidth(60);
		egu.getColumn("rulsl").setHeader("��¯����");
		egu.getColumn("rulsl").setWidth(60);
		egu.getColumn("rucdj").setHeader("�볧����");
		egu.getColumn("rucdj").setWidth(60);
		egu.getColumn("ruchdj").setHeader("�볧�󵥼�");
		egu.getColumn("ruchdj").setWidth(80);
		egu.getColumn("pingjj").setHeader("ƽ����");
		egu.getColumn("pingjj").setWidth(60);
		egu.getColumn("rez").setHeader("������(KJ/Kg)");
		egu.getColumn("rez").setWidth(80);
		egu.getColumn("shoudjqs").setHeader("�յ���ȫˮ");
		egu.getColumn("shoudjqs").setWidth(80);
		egu.getColumn("konggjhf").setHeader("�ոɻ��ҷ�");
		egu.getColumn("konggjhf").setWidth(80);
		egu.getColumn("youhyl").setHeader("�ͺ�����");
		egu.getColumn("youhyl").setWidth(60);
		egu.getColumn("youpjdj").setHeader("��ƽ������");
		egu.getColumn("youpjdj").setWidth(80);
		egu.getColumn("youpjrl").setHeader("��ƽ������");
		egu.getColumn("youpjrl").setWidth(80);
		egu.getColumn("biaomdj").setHeader("��ú����");
		egu.getColumn("biaomdj").setWidth(60);
		
		egu.getColumn("YUEBJZB_ID").setEditor(null);
		egu.getColumn("fenx").setEditor(null);
//		egu.getColumn("qicl").setEditor(null);
//		egu.getColumn("qicdj").setEditor(null);
		egu.getColumn("pingjj").setEditor(null);
		egu.getColumn("biaomdj").setEditor(null);
		
		((NumberField) egu.getColumn("ruchdj").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("youhyl").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("youpjdj").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("youpjrl").editor).setDecimalPrecision(2);
		egu.setDefaultsortable(false);             
		
		egu.getColumn("YUEBJZB_ID").update=false;
		egu.getColumn("fenx").update=false;
		
		egu.addTbarText("���");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("�·�");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
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
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�±�ú������¯����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		���ɰ�ť
		GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		gbc.setDisabled(isDisable);
		egu.addTbarBtn(gbc);
		
//		ɾ����ť
		GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		gbd.setDisabled(isDisable);
		egu.addTbarBtn(gbd);
		
//		���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		gbs.setDisabled(isDisable);
		egu.addTbarBtn(gbs);
//		��ӡ��ť
		String openScript =
			"var openUrl = 'http://'+document.location.host+document.location.pathname; " +
			"var end = openUrl.indexOf(';');" +
			"openUrl = openUrl.substring(0,end);" +
			"openUrl = openUrl + '?service=page/GdMonthReport&lx=yuebmdjrlzb';" +
			"window.open(openUrl ,'newWin','resizable=1');";
		GridButton gbp = new GridButton("��ӡ", "function (){" + openScript + "}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setChecked(false);
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("�ۼ����ݿɱ༭");
		
		StringBuffer sb = new StringBuffer();		
		sb.append("gridDiv_grid.on('afteredit', function(e){afteredit(gridDiv_ds, e);});\n");
		strSql =
			"SELECT JZ.MINGC AS JZMC, RL.*\n" +
			"  FROM YUEBMDJRLZB RL, YUEBJZB JZ\n" + 
			" WHERE RL.YUEBJZB_ID = JZ.ID\n" + 
			"   AND RIQ = " + LastODate + "\n" + 
			"   AND FENX = '�ۼ�'\n" + 
			"   AND RL.DIANCXXB_ID = " + diancxxb_id;
		
		ResultSetList rs = con.getResultSetList(strSql);
		sb.append("varRecord = new Array();\n");
		int i = 0;
		while (rs.next()) {		
			sb.append("  varOneRecord = new Array();\n");
			sb.append("  varOneRecord[v_Jiz]='" + rs.getString("JZMC")+ "';\n");
			sb.append("  varOneRecord[v_Rucdj]=" + rs.getDouble("rucdj")+ ";\n");
			sb.append("  varOneRecord[v_Ruchdj]=" + rs.getDouble("ruchdj")+ ";\n");
			sb.append("  varOneRecord[v_Rez]=" + rs.getDouble("rez")+ ";\n");
			sb.append("  varOneRecord[v_Shoudjqs]=" + rs.getDouble("shoudjqs")+ ";\n");
			sb.append("  varOneRecord[v_Konggjhf]=" + rs.getDouble("konggjhf")+ ";\n");
			sb.append("  varOneRecord[v_Youpjdj]=" + rs.getDouble("youpjdj")+ ";\n");
			sb.append("  varOneRecord[v_Youpjrl]=" + rs.getDouble("youpjrl")+ ";\n");
			sb.append("  varRecord[" + i + "] = varOneRecord;\n");
			i++;
		}
		sb.append("gridDiv_grid.on('beforeedit',function(e){beforeedit(gridDiv_ds, e, SelectLike);});");
		egu.addOtherScript(sb.toString());
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"��"+getYuef()+"��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����")
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
			setRiq();
			getSelectData();
		}
	}
	
	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		return con.getHasIt("select * from yueshchjb where riq="+CurrODate);
	}
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString2());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
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
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
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
    	if  (_YuefValue!=Value){
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
}
