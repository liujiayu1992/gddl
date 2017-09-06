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
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuecghtb extends BasePage implements PageValidateListener {
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
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

	public void DelData() {
		String diancxxb_id = this.getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String strSql = "delete from yuecght where riq=" + CurrODate
				+ " and diancxxb_id=" + diancxxb_id;
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + strSql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}
	
	public double getByJinml(JDBCcon con, String CurrSDate, long gongysb_id, long diancxxb_id) {
		double bymeil = 0;
		String sql = 
			"SELECT SUM(NVL(JINGZ, 0)) AS JINGZ\n" +
			"  FROM YUESLB SL, YUETJKJB HJ\n" + 
			" WHERE SL.YUETJKJB_ID = HJ.ID\n" + 
			"   AND TO_CHAR(RIQ, 'yyyy-mm') = '" + CurrSDate + "'\n" + 
			"   AND FENX = '����'\n" + 
			"   AND GONGYSB_ID = " + gongysb_id + "\n" +
			"   AND DIANCXXB_ID = " + diancxxb_id + "\n" + 
			" GROUP BY GONGYSB_ID";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			bymeil = rsl.getDouble("jingz");
		} 
		rsl.close();
		return bymeil;
	}

	public void CreateData() {		
		long diancxxb_id = Long.parseLong(this.getTreeid());
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		String CurrSDate = getNianf() + "-" + getYuef();
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		int intYuef = Integer.parseInt(getYuef());
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		String strSql = "";
		int flag = 0;
		long lngId = 0;
		
		
		con.setAutoCommit(false);
		
		// ɾ�������ɵ�����
		strSql = "delete from yuecght where to_char(riq,'yyyy-mm')='"
				+ CurrSDate + "' and diancxxb_id=" + diancxxb_id;
		flag = con.getDelete(strSql);

		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "��������SQL:" + strSql);
			con.Close();
			return;
		}

		strSql = 
			"SELECT DISTINCT JH.DIANCXXB_ID,\n" +
			"                JH.GONGYSB_ID,\n" + 
			"                JH.JIHKJB_ID,\n" + 
			"                JH.YUNSFSB_ID,\n" + 
			"                0 AS MEIJ,\n" + 
			"                0 AS YUNF,\n" + 
			"                0 AS ZAF,\n" + 
			"                0 AS REZ,\n" + 
			"                0 AS SHOUDJQS,\n" + 
			"                0 AS KONGGJHF,\n" + 
			"                0 AS BIAOMDJ\n" + 
			"  FROM YUETJKJB JH\n" + 
			" WHERE TO_CHAR(RIQ, 'yyyy-mm') = '" + CurrSDate + "'\n" + 
			"   AND DIANCXXB_ID = " + diancxxb_id;

		ResultSetList rs = con.getResultSetList(strSql);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
			con.rollBack();
			con.Close();
			return;
		}

		while (rs.next()) {
			// ���ɱ�������
			double benyml = getByJinml(con, CurrSDate, rs.getLong("gongysb_id"), diancxxb_id);
			lngId = Long.parseLong(MainGlobal.getNewID(diancxxb_id));		
			strSql =
				"INSERT INTO YUECGHT\n" +
				"  (ID,\n" + 
				"   RIQ,\n" + 
				"   FENX,\n" + 
				"   DIANCXXB_ID,\n" + 
				"   GONGYSB_ID,\n" + 
				"   JIHKJB_ID,\n" + 
				"   YUNSFSB_ID,\n" + 
				"   HETL,\n" + 
				"   MEIJ,\n" + 
				"   YUNF,\n" + 
				"   ZAF,\n" + 
				"   REZ,\n" + 
				"   SHOUDJQS,\n" + 
				"   KONGGJHF,\n" + 
				"   BIAOMDJ)\n" + 
				"VALUES\n" + 
				"  (" + lngId + ",\n" + 
				"   " + CurrODate + ",\n" + 
				"   '����',\n" + 
				"   " + diancxxb_id + ",\n" + 
				"   " + rs.getLong("gongysb_id") + ",\n" + 
				"   " + rs.getLong("jihkjb_id") + ",\n" + 
				"   " + rs.getLong("yunsfsb_id") + ",\n" + 
				"   " + benyml + ",\n" + 
				"   " + rs.getDouble("meij") + ",\n" + 
				"   " + rs.getDouble("yunf") + ",\n" + 
				"   " + rs.getDouble("zaf") + ",\n" + 
				"   " + rs.getDouble("rez") + ",\n" + 
				"   " + rs.getDouble("shoudjqs") + ",\n" + 
				"   " + rs.getDouble("konggjhf") +",\n" + 
				"   " + rs.getDouble("biaomdj") + ")";
			flag = con.getInsert(strSql);
			if (flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
				setMsg("���ɹ��̳��ִ����²ɹ���ͬ���²���ʧ�ܣ�");
				con.rollBack();
				con.Close();
				return;
			}

			// �����ۼ�����
			strSql =
				"SELECT *\n" +
				"  FROM YUECGHT\n" + 
				" WHERE FENX = '�ۼ�'\n" + 
				"   AND GONGYSB_ID =" + + rs.getLong("gongysb_id") + "\n" + 
				"   AND JIHKJB_ID =" + rs.getLong("jihkjb_id") + "\n" + 
				"   AND YUNSFSB_ID =" + rs.getLong("yunsfsb_id") + "\n" + 
				"   AND RIQ =" + LastODate + "\n" + 
				"   AND DIANCXXB_ID =" + diancxxb_id;


			ResultSetList rsL = con.getResultSetList(strSql);
			if (rsL == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
				con.rollBack();
				con.Close();
				return;
			}
			double dblHetl = 0;
			double dblMeij = 0;
			double dblYunf = 0;
			double dblZaf = 0;
			double dblRez = 0;
			double dblShoudjqs = 0;
			double dblKonggjhf = 0;
			double dblBiaomdj = 0;
			
//			double dblBYHetl = rs.getDouble("hetl");benyml
			double dblBYHetl = benyml;
			double dblBYMeij = rs.getDouble("meij");
			double dblBYYunf = rs.getDouble("yunf");
			double dblBYZaf = rs.getDouble("zaf");
			double dblBYRez = rs.getDouble("rez");
			double dblBYShoudjqs = rs.getDouble("shoudjqs");
			double dblBYKonggjhf = rs.getDouble("konggjhf");
			double dblBYBiaomdj = rs.getDouble("biaomdj");

			// ���¹�Ӧ�����ۼ�
			if (rsL.next() && intYuef != 1) {				
				double dblLJHetl = rsL.getDouble("hetl");
				double dblLJMeij = rsL.getDouble("meij");
				double dblLJYunf = rsL.getDouble("yunf");
				double dblLJZaf = rsL.getDouble("zaf");
				double dblLJRez = rsL.getDouble("rez");
				double dblLJShoudjqs = rsL.getDouble("shoudjqs");
				double dblLJKonggjhf = rsL.getDouble("konggjhf");
				double dblLJBiaomdj = rsL.getDouble("biaomdj");
				
				if (dblLJHetl > 0) {
					dblHetl = dblBYHetl + dblLJHetl;
					dblMeij = CustomMaths.Round_new((dblBYMeij*dblBYHetl + dblLJMeij*dblLJHetl) / dblHetl, 3);
					dblYunf = CustomMaths.Round_new((dblBYYunf*dblBYHetl + dblLJYunf*dblLJHetl) / dblHetl, 2);
					dblZaf = CustomMaths.Round_new((dblBYZaf*dblBYHetl + dblLJZaf*dblLJHetl) / dblHetl, 2);
					dblRez = CustomMaths.Round_new((dblBYRez*dblBYHetl + dblLJRez*dblLJHetl) / dblHetl, 2);
					dblShoudjqs = CustomMaths.Round_new((dblBYShoudjqs*dblBYHetl + dblLJShoudjqs*dblLJHetl) / dblHetl, 2);
					dblKonggjhf = CustomMaths.Round_new((dblBYKonggjhf*dblBYHetl + dblLJKonggjhf*dblLJHetl) / dblHetl, 2);
					dblBiaomdj = CustomMaths.Round_new((dblMeij + dblYunf + dblZaf) * 29271 / dblRez, 2);
				} 

				lngId = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
				strSql = "insert into yuecght (id,riq,fenx,diancxxb_id,gongysb_id,\n"
						+ "jihkjb_id,yunsfsb_id,hetl,meij,yunf,zaf,rez,shoudjqs,konggjhf,biaomdj)\n"
						+ "values(\n"
						+ lngId
						+ ",\n"
						+ CurrODate
						+ ",\n"
						+ "'�ۼ�',\n"
						+ rsL.getLong("diancxxb_id")
						+ ",\n"
						+ rsL.getLong("gongysb_id")
						+ ",\n"
						+ rsL.getLong("jihkjb_id")
						+ ",\n"
						+ rsL.getLong("yunsfsb_id")
						+ ",\n"
						+ dblHetl
						+ ", \n"
						+ dblMeij
						+ ",\n"
						+ dblYunf
						+ ",\n"
						+ dblZaf
						+ ",\n"
						+ dblRez
						+ ",\n"
						+ dblShoudjqs
						+ ",\n"
						+ dblKonggjhf
						+ ",\n" 
						+ dblBiaomdj + "\n" + ")";

				flag = con.getInsert(strSql);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+ "SQL:" + strSql);
					setMsg("���ɹ��̳��ִ����²ɹ���ͬ�ۼƲ���ʧ�ܣ�");
					con.rollBack();
					con.Close();
					return;
				}

			} else { // ���¹�Ӧ��û���ۼ����ݻ����·�Ϊ1��
				lngId = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
				strSql = "insert into yuecght (id,riq,fenx,diancxxb_id,gongysb_id,\n"
						+ "jihkjb_id,yunsfsb_id,hetl,meij,yunf,zaf,rez,shoudjqs,konggjhf,biaomdj)\n"
						+ "values(\n"
						+ lngId
						+ ",\n"
						+ CurrODate
						+ ",\n"
						+ "'�ۼ�',\n"
						+ rs.getLong("diancxxb_id")
						+ ",\n"
						+ rs.getLong("gongysb_id")
						+ ",\n"
						+ rs.getLong("jihkjb_id")
						+ ",\n"
						+ rs.getLong("yunsfsb_id")
						+ ",\n"
						+ dblBYHetl
						+ ", \n"
						+ dblBYMeij
						+ ",\n"
						+ dblBYYunf
						+ ",\n"
						+ dblBYZaf
						+ ",\n"
						+ dblBYRez
						+ ",\n"
						+ dblBYShoudjqs
						+ ",\n"
						+ dblBYKonggjhf
						+ ",\n"
						+ dblBYBiaomdj + "\n" + ")";
				flag = con.getInsert(strSql);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+ "SQL:" + strSql);
					setMsg("���ɹ��̳��ִ����²ɹ���ͬ�ۼƲ���ʧ�ܣ�");
					con.rollBack();
					con.Close();
					return;
				}
			}
		}

		rs.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate + "�����ݳɹ����ɣ�");
	}

	public void getSelectData() {
		Visit visit = (Visit)getPage().getVisit();
		long diancxxb_id = Long.parseLong(this.getTreeid());
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		JDBCcon con = new JDBCcon();
		String strSql = 
				"select * from yueslb s, yuetjkjb k where s.yuetjkjb_id = k.id and k.riq = "
				+ CurrODate + " and k.diancxxb_id=" + diancxxb_id;
		boolean isLocked = !con.getHasIt(strSql);
		boolean isDisable = false;
		if (visit.isFencb() && diancxxb_id == visit.getDiancxxb_id()) {
			isDisable = true;
			isLocked = true;
		}
		if (isLocked && !isDisable) {
			setMsg("����ʹ�ñ�ģ��֮ǰ������������������ݵļ��㣡");
		}

		strSql = 
			"SELECT H.ID,\n" + 
			"       G.MINGC AS GONGYSB_ID,\n" + 
			"       J.MINGC AS JIHKJB_ID,\n" + 
			"       Y.MINGC AS YUNSFSB_ID,\n" + 
			"       H.FENX,\n" + 
			"       H.HETL,\n" + 
			"       H.MEIJ,\n" + 
			"       H.YUNF,\n" + 
			"       H.ZAF,\n" + 
			"       H.REZ AS REZ,\n" + 
			"       H.SHOUDJQS,\n" + 
			"       H.KONGGJHF,\n" + 
			"       H.BIAOMDJ\n" + 
			"  FROM YUECGHT H, GONGYSB G, JIHKJB J, YUNSFSB Y\n" + 
			" WHERE H.GONGYSB_ID = G.ID\n" + 
			"   AND H.JIHKJB_ID = J.ID\n" + 
			"   AND H.YUNSFSB_ID = Y.ID\n" + 
			"   AND RIQ = " + CurrODate + "\n" + 
			"   AND H.DIANCXXB_ID = " + diancxxb_id + "\n" +
			" ORDER BY GONGYSB_ID, FENX";
		
		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("yuecght");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(60);
		// egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		// egu.getColumn("pinzb_id").setWidth(40);
		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("fenx").setWidth(40);
		egu.getColumn("hetl").setCenterHeader("��ͬ��(��)");
		egu.getColumn("hetl").setWidth(80);
		egu.getColumn("meij").setCenterHeader("����۸�<br>(����˰)");
		egu.getColumn("meij").setWidth(80);
		egu.getColumn("yunf").setCenterHeader("�˷�<br>(����˰)");
		egu.getColumn("yunf").setWidth(80);
		egu.getColumn("zaf").setCenterHeader("�ӷ�");
		egu.getColumn("zaf").setWidth(80);
		egu.getColumn("rez").setCenterHeader("��ֵ<br>(ǧ��/ǧ��)");
		egu.getColumn("rez").setWidth(80);
		egu.getColumn("shoudjqs").setCenterHeader("�յ���ȫˮ<br>(%)");
		egu.getColumn("shoudjqs").setWidth(90);
		egu.getColumn("konggjhf").setCenterHeader("�ոɻ��ҷ�<br>(%)");
		egu.getColumn("konggjhf").setWidth(90);
		egu.getColumn("biaomdj").setCenterHeader("��ú����<br>(����˰)");
		egu.getColumn("biaomdj").setWidth(90);

		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setEditor(null);
		// egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("biaomdj").setEditor(null);
		((NumberField) egu.getColumn("meij").editor).setDecimalPrecision(3);
		((NumberField) egu.getColumn("yunf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("zaf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("shoudjqs").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("konggjhf").editor).setDecimalPrecision(2);
		
		egu.setDefaultsortable(false);

		egu.getColumn("gongysb_id").update = false;
		egu.getColumn("jihkjb_id").update = false;
		// egu.getColumn("pinzb_id").update=false;
		egu.getColumn("yunsfsb_id").update = false;
		egu.getColumn("fenx").update = false;

		// /�Ƿ񷵻��������ֵ��ID
		// egu.getColumn("").setReturnId(true);
		// egu.getColumn("").setReturnId(true);
		// /���ð�ť
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
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
		
		// �ж������Ƿ�����
		// boolean isLocked = isLocked(con);
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
						MainGlobal.getExtMessageBox(
										"'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�²ɹ���ͬ������,���Ժ�'",
										true)).append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// ���ɰ�ť
		GridButton gbc = new GridButton("����",
				getBtnHandlerScript("CreateButton"));
		gbc.setDisabled(isLocked);
		// if(isLocked) {
		// gbc.setHandler("function
		// (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
		// }
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		// ɾ����ť
		GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
		gbd.setDisabled(isLocked);
		// if(isLocked) {
		// gbd.setHandler("function
		// (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
		// }
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// ���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		gbs.setDisabled(isLocked);
		// if(isLocked) {
		// gbs.setHandler("function
		// (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
		// }
		egu.addTbarBtn(gbs);
		// ��ӡ��ť
		String openScript =
			"var openUrl = 'http://'+document.location.host+document.location.pathname; " +
			"var end = openUrl.indexOf(';');" +
			"openUrl = openUrl.substring(0,end);" +
			"openUrl = openUrl + '?service=page/GdMonthReport&lx=yuecght';" +
			"window.open(openUrl ,'newWin','resizable=1');";
		GridButton gbp = new GridButton("��ӡ", "function (){" + openScript + "}");
		
//		GridButton gbp = new GridButton("��ӡ", "function (){"
//				+ MainGlobal.getOpenWinScript("GdMonthReport&lx=yuecght","screen.width","screen.height") + "}");
//		gbp.setDisabled(isLocked);
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
			"SELECT G.MINGC AS GONGYSMC, J.MINGC AS JIHKJMC, Y.MINGC AS YUNSFSMC, HT.*\n" +
			"  FROM YUECGHT HT, GONGYSB G, JIHKJB J, YUNSFSB Y\n" + 
			" WHERE HT.GONGYSB_ID = G.ID\n" + 
			"   AND HT.JIHKJB_ID = J.ID\n" + 
			"   AND HT.YUNSFSB_ID = Y.ID\n" + 
			"   AND HT.FENX = '�ۼ�'\n" + 
			"   AND HT.RIQ = " + LastODate + "\n" + 
			"   AND HT.DIANCXXB_ID = " + diancxxb_id;

		ResultSetList rs = con.getResultSetList(strSql);
		sb.append("varRecord = new Array();\n");
		int i = 0;
		while (rs.next()) {		
			sb.append("  varOneRecord = new Array();\n");
			sb.append("  varOneRecord[v_Gongys]='" + rs.getString("gongysmc")+ "';\n");
			sb.append("  varOneRecord[v_Jihkj]='" + rs.getString("jihkjmc")+ "';\n");
			sb.append("  varOneRecord[v_Yunsfs]='" + rs.getString("yunsfsmc")+ "';\n");
			sb.append("  varOneRecord[v_Meij]=" + rs.getDouble("meij")+ ";\n");
			sb.append("  varOneRecord[v_Yunf]=" + rs.getDouble("yunf")+ ";\n");
			sb.append("  varOneRecord[v_Zaf]=" + rs.getDouble("zaf")+ ";\n");
			sb.append("  varOneRecord[v_Rez]=" + rs.getDouble("rez")+ ";\n");
			sb.append("  varOneRecord[v_Shoudjqs]=" + rs.getDouble("shoudjqs")+ ";\n");
			sb.append("  varOneRecord[v_Konggjhf]=" + rs.getDouble("konggjhf")+ ";\n");
			sb.append("  varRecord[" + i + "] = varOneRecord;\n");
			i++;
		}
//		sb.append(
//				"function copyData() {\n" +
//				"  varRecord = new Array();\n" + 
//				"  for (i=0; i<=gridDiv_ds.getCount()-1; i++) {\n" + 
//				"    if (gridDiv_ds.getAt(i).get('FENX')=='�ۼ�') {\n" + 
//				"      varOneRecord = new Array();\n" + 
//				"      varOneRecord[v_Hetl] = gridDiv_ds.getAt(i).get('HETL');\n" + 
//				"      varOneRecord[v_Meij] = gridDiv_ds.getAt(i).get('MEIJ');\n" + 
//				"      varOneRecord[v_Yunf] = gridDiv_ds.getAt(i).get('YUNF');\n" + 
//				"      varOneRecord[v_Zaf] = gridDiv_ds.getAt(i).get('ZAF');\n" + 
//				"      varOneRecord[v_Rez] = gridDiv_ds.getAt(i).get('REZ');\n" + 
//				"      varOneRecord[v_Shoudjqs] = gridDiv_ds.getAt(i).get('SHOUDJQS');\n" + 
//				"      varOneRecord[v_Konggjhf] = gridDiv_ds.getAt(i).get('KONGGJHF');\n" + 
//				"      varRecord[i] = varOneRecord;\n" + 
//				"    }\n" + 
//				"  }\n" + 
//				"}\n" +
//				"copyData();\n");

		// sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if(e.record.get('GONGYSB_ID')=='�ܼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		// sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");//�糧�в�����༭
		// sb.append("});");

		// �趨�ϼ��в�����
		// sb.append("function
		// gridDiv_save(record){if(record.get('gongysb_id')=='�ܼ�') return
		// 'continue';}");
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("beforeedit(gridDiv_ds,e,SelectLike);");// ��"�ۼ�"ʱ,��һ�в�����༭
		sb.append("});");
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
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
	
//	�ֳ���
	public boolean isFencb() {
		return ((Visit) this.getPage().getVisit()).isFencb();
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
