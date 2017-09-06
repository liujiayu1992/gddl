package com.zhiren.jt.zdt.yansgl.zhilb;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2011-07-22
 * ������������Ϻ�����ŵڶ������������ι�˾�ڽ����ϼ����Զ�����AAD,AD,VDAF�ķ�����
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-12-27
 * ���������չ������Ҫ��ȫˮ(Mt)���ոɻ�ˮ(Mad)���ոɻ���(St,ad)���ոɻ���(Aad)����Ͳ��ֵ(Qb,ad)���ոɻ���(Had)���ոɻ��ӷ���(Vad)����Ϊ������
 * 		�����޻һ��ӷ���(Vdaf)���ɻ���(St,d)���ɻ���(Ad)���յ�����(Aar)���յ�����(St,ar)����λ��(Qgr,ad)�������޻һ���(Hdaf)��
 * 		�յ����ӷ���(Var)�͵�λ��(Qnet,ar)����Ϊ��������ͨ�����������ó�
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-12-28
 * �������޸��յ����ӷ���(Var)�ļ��㹫ʽ
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-02
 * �����������̶�̼(FCAD)�ļ��㹫ʽ
 */
public class Zhillr extends BasePage implements PageValidateListener {
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


	private boolean _EndChange=false;
	public Date getRiq() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setRiq(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_EndChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndChange=true;
		}
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
		// visit.getExtGrid1().Save(getChange(), visit);
		Save1(getChange(), visit);
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		ExtGridUtil egu = getExtGrid();
		String zhilbid = "";
		String fahbid = "";

		try {

			StringBuffer sql = new StringBuffer("begin \n");

			String tableName = "zhilb";
			String tableName2 = "fahb";

			ResultSetList delrsl = egu.getDeleteResultSet(strchange);
			while (delrsl.next()) {
				sql.append(" delete from ").append(tableName).append(
						" where id =").append(delrsl.getString(0)).append(
						";	\n");
			}

			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while (mdrsl.next()) {
				StringBuffer sql2 = new StringBuffer();
				StringBuffer sql3 = new StringBuffer();
				zhilbid = MainGlobal.getNewID(visit.getDiancxxb_id());
				sql2.append(zhilbid);
			
				if ("0".equals(mdrsl.getString("ID"))) {
					sql.append("insert into ").append(tableName).append("(id");
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {
						if (mdrsl.getColumnNames()[i].equals("GONGYSB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("MEIKXXB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("JIHKJB_ID")
								|| mdrsl.getColumnNames()[i].equals("FAHRQ")
								|| mdrsl.getColumnNames()[i].equals("DAOHRQ")
								|| mdrsl.getColumnNames()[i]
										.equals("YUNSFSB_ID")
								|| mdrsl.getColumnNames()[i].equals("CHES")) {

						} else {
							if (mdrsl.getColumnNames()[i].equals("FAHBID")) {
								fahbid = getValueSql(egu.getColumn(mdrsl
										.getColumnNames()[i]), mdrsl
										.getString(i));

							} else if (mdrsl.getColumnNames()[i]
									.equals("HUAYSJ")) {
								sql.append(",").append(
										mdrsl.getColumnNames()[i]);
								sql2.append(",").append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i)));
							} else {
								String zhi="0";
								if(mdrsl.getString(i)==null || mdrsl.getString(i).equals("")){
									zhi="0";
								}else{
									zhi=mdrsl.getString(i);
								}
								sql.append(",").append(
										mdrsl.getColumnNames()[i]);
								sql2.append(",").append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), zhi));
							}
						}
					}
					sql.append(") values(").append(sql2).append(");\n");
					sql3.append("update ").append(tableName2).append(
							" set zhilb_id=").append(zhilbid).append(
							" where id=").append(fahbid).append(";\n");
					sql.append(sql3.toString());
				} else {
					sql.append("update ").append(tableName).append(" set ");
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {
						if (mdrsl.getColumnNames()[i].equals("GONGYSB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("MEIKXXB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("JIHKJB_ID")
								|| mdrsl.getColumnNames()[i].equals("FAHRQ")
								|| mdrsl.getColumnNames()[i].equals("DAOHRQ")
								|| mdrsl.getColumnNames()[i]
										.equals("YUNSFSB_ID")
								|| mdrsl.getColumnNames()[i].equals("CHES")) {

						} else {
							if (mdrsl.getColumnNames()[i].equals("FAHBID")) {
								fahbid = getValueSql(egu.getColumn(mdrsl
										.getColumnNames()[i]), mdrsl
										.getString(i));
							} else if (mdrsl.getColumnNames()[i]
									.equals("HUAYSJ")) {
								sql.append(" HUAYSJ = ");
								sql.append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i))).append(",");
							} else {
								String zhi="0";
								if(mdrsl.getString(i)==null || mdrsl.getString(i).equals("")){
									zhi="0";
								}else{
									zhi=mdrsl.getString(i);
								}
								sql.append(mdrsl.getColumnNames()[i]).append(
										" = ");
								sql.append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), zhi)).append(",");
							}
						}
					}
					sql.deleteCharAt(sql.length() - 1);
					zhilbid = mdrsl.getString("ID");
					sql.append(" where id =").append(mdrsl.getString("ID"))
							.append(";\n");
				}
				sql3.append("update ").append(tableName2).append(
						" set zhilb_id=").append(zhilbid).append(" where id=")
						.append(fahbid).append(";\n");
				sql.append(sql3.toString());
			}

			sql.append("end;");
			flag=con.getUpdate(sql.toString());
			if (flag!=-1){
				setMsg(ErrorMessage.SaveSuccessMessage);
			}
			con.commit();
			con.Close();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		getSelectData();
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
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ShenhClick = false;

	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	private void Shenh() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Shujsh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
		}
		int flag = 0;
		int ches = 0;
		int biaoz = 0;
		while (rsl.next()) {
			String fahbid = rsl.getString("id");
			ches += rsl.getInt("ches");
			biaoz += rsl.getInt("biaoz");
			sb.append("update chepb set hedbz = ")
					.append(SysConstant.HEDBZ_YSH).append(" where fahb_id = ")
					.append(fahbid);
			flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ sb);
				setMsg(ErrorMessage.ShujshH001);
				return;
			}
			sb.delete(0, sb.length());
			sb.append("update fahb set liucztb_id =1,hedbz = ").append(
					SysConstant.HEDBZ_YSH).append(" where id = ")
					.append(fahbid);
			flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ sb);
				setMsg(ErrorMessage.ShujshH002);
				return;
			}
			sb.delete(0, sb.length());
		}
		con.commit();
		con.Close();
		setMsg("��˳ɹ���������� " + ches + " ������Ʊ�� " + biaoz + " �֡�");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
//		 ----------�糧��--------------
		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + visit.getDiancxxb_id() + " or d.fuid = "
						+ visit.getDiancxxb_id() + ")";
			} else {
				str = "and d.id = " + visit.getDiancxxb_id() + "";
			}
		}
		
		StringBuffer sb = new StringBuffer();
		String riq = DateUtil.FormatDate(getRiq());// �鿴

		sb.append("select nvl(zl.id,0) as id,");// ,f.jingz
		sb.append(" f.id as fahbid,g.mingc as gongysb_id,mk.mingc as meikxxb_id,jh.mingc as jihkjb_id,f.fahrq,f.daohrq,y.mingc as yunsfsb_id,");
		sb.append(" f.ches,f.laimsl,f.jingz,zl.huaybh,nvl(zl.caiyb_id,0) as caiyb_id,");
		//sb.append(" decode(zl.huaysj,null,to_char(to_date('"+ riq+ "','yyyy-mm-dd'),'yyyy-mm-dd'),to_char(zl.huaysj,'yyyy-mm-dd')) as huaysj,");
		sb.append(" to_date(to_char(nvl(zl.huaysj,sysdate),'yyyy-mm-dd'),'yyyy-mm-dd') as huaysj,");
		
		// --ȫˮ(Mt)���ոɻ�ˮ(Mad)���ոɻ���(St,ad)���ոɻ���(Aad)����Ͳ��ֵ(Qb,ad)���ոɻ���(Had)���ոɻ��ӷ���(Vad)
		sb.append(" zl.qnet_ar,zl.mt,zl.mad,zl.stad,zl.aad,zl.qbad,zl.had, zl.vad,");
		// ���ɱ༭���ֶ�
		sb.append(" zl.vdaf,zl.std,zl.ad,zl.aar,zl.star,");
		sb.append(" zl.fcad,zl.qgrad,zl.hdaf,zl.qgrad_daf,zl.sdaf,");
		sb.append(" zl.var,zl.jijsf,zl.jijfrl,zl.beiz");
		sb.append(" from zhilb zl,fahb f,gongysb g,meikxxb mk,jihkjb jh,yunsfsb y,diancxxb d where f.zhilb_id=zl.id(+) and f.gongysb_id=g.id ");
		sb.append(" and f.meikxxb_id=mk.id and f.jihkjb_id=jh.id and f.yunsfsb_id=y.id and f.diancxxb_id=d.id ").append(str);
		sb.append(" and to_char(f.daohrq,'yyyy-mm-dd')='").append(riq).append("'");

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			// setMsg(ErrorMessage.NullResult);
			setMsg("��ȷ�Ϸ������Ƿ���������ݣ�");
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		// ���ö�ѡ��
		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		egu.getColumn(1).setAlign("middle");
		egu.setTableName("zhilb");
		egu.getColumn("fahbid").setCenterHeader("������ID");
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("fahbid").setWidth(80);
		egu.getColumn("fahbid").editor = null;
		// ��������Ϣ
		egu.getColumn("gongysb_id").setCenterHeader("������λ");
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("gongysb_id").editor = null;
		egu.getColumn("gongysb_id").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("meikxxb_id").setCenterHeader("ú������");
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("meikxxb_id").editor = null;
		egu.getColumn("meikxxb_id").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("meikxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jihkjb_id").setCenterHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setHidden(false);
		egu.getColumn("jihkjb_id").editor = null;
		egu.getColumn("jihkjb_id").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("jihkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fahrq").setCenterHeader("����ʱ��");
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("fahrq").editor = null;
		egu.getColumn("fahrq").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("fahrq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("daohrq").setCenterHeader("����ʱ��");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("daohrq").setHidden(true);
		egu.getColumn("daohrq").editor = null;
		egu.getColumn("daohrq").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("daohrq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("yunsfsb_id").setCenterHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setHidden(false);
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").editor = null;
		egu.getColumn("yunsfsb_id").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("yunsfsb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ches").setCenterHeader("����");
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("ches").editor = null;
		egu.getColumn("ches").setUpdate(false);
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("ches").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("laimsl").setCenterHeader("ʵ����<br>(��)");
		egu.getColumn("laimsl").setWidth(60);
		egu.getColumn("laimsl").editor = null;
		egu.getColumn("laimsl").setUpdate(false);
		egu.getColumn("laimsl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jingz").setCenterHeader("����<br>(��)");
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jingz").editor = null;
		egu.getColumn("jingz").setUpdate(false);
		egu.getColumn("jingz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("huaybh").setCenterHeader("������");
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("huaybh").setDefaultValue("0");
		
		
		egu.getColumn("caiyb_id").setCenterHeader("������ID");
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("caiyb_id").setDefaultValue("-1");
		egu.getColumn("caiyb_id").editor = null;
		egu.getColumn("caiyb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("huaysj").setCenterHeader("����ʱ��");
		egu.getColumn("huaysj").setWidth(80);

		egu.getColumn("qnet_ar").setCenterHeader("��λ��<br>(Qnet,ar)<br>(MJ/Kg)");//
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("qnet_ar").setDefaultValue("0");
		egu.getColumn("qnet_ar").setEditor(null);

		//������
		egu.getColumn("mt").setCenterHeader("*ȫˮ<br>(Mt)%");//
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mt").setDefaultValue("0");
		((NumberField)egu.getColumn("mt").editor).setDecimalPrecision(3);
		
		egu.getColumn("mad").setCenterHeader("*�ոɻ�ˮ<br>(Mad)%");//
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mad").setDefaultValue("0");
		((NumberField)egu.getColumn("mad").editor).setDecimalPrecision(3);
		
		egu.getColumn("stad").setCenterHeader("*�ոɻ���<br>(St,ad)%");
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("stad").setDefaultValue("0");
		((NumberField)egu.getColumn("stad").editor).setDecimalPrecision(3);
		

		egu.getColumn("aad").setCenterHeader("*�ոɻ���<br>(Aad)%");
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").setDefaultValue("0");
		((NumberField)egu.getColumn("aad").editor).setDecimalPrecision(3);
		
		egu.getColumn("qbad").setCenterHeader("*��Ͳ��<br>(Qb,ad)<br>(MJ/Kg)");
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("qbad").setDefaultValue("0");
		((NumberField)egu.getColumn("qbad").editor).setDecimalPrecision(3);
		
		egu.getColumn("had").setCenterHeader("*�ոɻ���<br>(Had)%");
		egu.getColumn("had").setWidth(80);
		egu.getColumn("had").setDefaultValue("0");
		((NumberField)egu.getColumn("had").editor).setDecimalPrecision(3);
		
		egu.getColumn("vad").setCenterHeader("*�ոɻ�<br>�ӷ���(Vad)%");
		egu.getColumn("vad").setWidth(100);
		egu.getColumn("vad").setDefaultValue("0");
		((NumberField)egu.getColumn("vad").editor).setDecimalPrecision(3);
		//
		egu.getColumn("vdaf").setCenterHeader("�ӷ���<br>(Vdaf)%");//
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("vdaf").setDefaultValue("0");
		egu.getColumn("vdaf").setEditor(null);


		egu.getColumn("std").setCenterHeader("�ɻ���<br>(St,d)%");//
		egu.getColumn("std").setWidth(60);
		egu.getColumn("std").setDefaultValue("0");
		egu.getColumn("std").setEditor(null);
		
		egu.getColumn("ad").setCenterHeader("�ɻ���<br>(Ad)%");//
		egu.getColumn("ad").setWidth(70);
		egu.getColumn("ad").setDefaultValue("0");
		egu.getColumn("ad").setEditor(null);
		
		egu.getColumn("star").setHeader("�յ�����<br>(St,ar)%");
		egu.getColumn("star").setWidth(70);
		egu.getColumn("star").setDefaultValue("0");
		egu.getColumn("star").setEditor(null);
		
		egu.getColumn("aar").setCenterHeader("�յ���<br>�ҷ�(Aar)%");
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("aar").setDefaultValue("0");
		egu.getColumn("aar").setEditor(null);
		
		egu.getColumn("qgrad").setCenterHeader("�ոɻ�<br>��λ��(Qgr,ad)<br>(MJ/Kg)");
		egu.getColumn("qgrad").setWidth(100);
		egu.getColumn("qgrad").setDefaultValue("0");
		egu.getColumn("qgrad").setEditor(null);
		
		egu.getColumn("hdaf").setCenterHeader("�����޻�<br>����(Hdaf)%");
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("hdaf").setDefaultValue("0");
		egu.getColumn("hdaf").setEditor(null);
		
		egu.getColumn("qgrad_daf").setCenterHeader("�����޻һ�<br>��λ��(Qgrad,daf)<br>(MJ/Kg)");
		egu.getColumn("qgrad_daf").setWidth(100);
		egu.getColumn("qgrad_daf").setDefaultValue("0");
		egu.getColumn("qgrad_daf").setHidden(true);
		
		egu.getColumn("sdaf").setCenterHeader("�����޻�<br>����(Sdaf)%");
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("sdaf").setDefaultValue("0");
		egu.getColumn("sdaf").setHidden(true);

		egu.getColumn("var").setCenterHeader("�յ���<br>�ӷ���(Var)%");
		egu.getColumn("var").setWidth(80);
		egu.getColumn("var").setDefaultValue("0");
		egu.getColumn("var").setEditor(null);

		egu.getColumn("fcad").setCenterHeader("�̶�̼<br>(Fcad)%");
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("fcad").setDefaultValue("0");
		
		egu.getColumn("jijsf").setCenterHeader("�Ƽ�ˮ��<br>(%)");
		egu.getColumn("jijsf").setHidden(true);
		egu.getColumn("jijsf").setWidth(80);
		egu.getColumn("jijsf").setDefaultValue("0");
		
		egu.getColumn("jijfrl").setCenterHeader("�Ƽ۷�����<br>(Mj/kg)");
		egu.getColumn("jijfrl").setWidth(80);
		egu.getColumn("jijfrl").setHidden(true);
		egu.getColumn("jijfrl").setDefaultValue("0");
		
		egu.getColumn("beiz").setCenterHeader("��ע");
		egu.getColumn("beiz").setWidth(50);
		
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getRiq()));
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());

		// ���û������ڵ�Ĭ��ֵ
		egu.getColumn("huaysj").setDefaultValue(DateUtil.FormatDate(this.getRiq()));
		// ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ����ÿҳ��ʾ����
		egu.addPaging(25);

//		 ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		// egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		egu.addTbarText("*Ϊ������");
		
		
		StringBuffer OtherScript=new StringBuffer();
		OtherScript.append("gridDiv_grid.addListener('afteredit',function(e){\n");
		OtherScript.append("	var _value;\n");
		OtherScript.append("	var Qgrad=e.record.get('QGRAD');\n");
		OtherScript.append("	if(Qgrad==null || Qgrad=='') Qgrad='0';\n");
		
		OtherScript.append("	var Had=e.record.get('HAD');\n");
		OtherScript.append("	if(Had==null || Had=='') Had='0';\n");
		
		OtherScript.append("	var Mt=e.record.get('MT');\n");
		OtherScript.append("	if(Mt==null || Mt=='') Mt='0';\n");
		
		OtherScript.append("	var Mad=e.record.get('MAD');\n");
		OtherScript.append("	if(Mad==null || Mad=='') Mad='0';\n");
		
		OtherScript.append("	var Qbad=e.record.get('QBAD');\n");
		OtherScript.append("	if(Qbad==null || Qbad=='') Qbad='0';\n");
		
		OtherScript.append("	var A=0;\n");
		OtherScript.append("	if (Qbad < 16.72) {\n");
		OtherScript.append("			A = 0.001;\n");
		OtherScript.append("		} else {\n");
		OtherScript.append("			if (Qbad >= 16.72 && Qbad <= 25.1) {\n");
		OtherScript.append("				A = 0.0012;\n");
		OtherScript.append("			} else {\n");
		OtherScript.append("				A = 0.0016;\n");
		OtherScript.append("			}\n");
		OtherScript.append("	}\n");

		OtherScript.append("	var St_ad=e.record.get('STAD');\n");
		OtherScript.append("	if(St_ad==null || St_ad=='') St_ad='0';\n");
		
		OtherScript.append("	var Aad=e.record.get('AAD');\n");
		OtherScript.append("	if(Aad==null || Aad=='') Aad='0';\n");
		
		OtherScript.append("	var Vad=e.record.get('VAD');\n");
		OtherScript.append("	if(Vad==null || Vad=='') Vad='0';\n");
		
		OtherScript.append("	if(eval(Qbad)!=0&&eval(St_ad)!=0){\n");
		OtherScript.append("	_value=Math.round((Qbad-(0.0941*St_ad+A*Qbad))*100)/100;\n");
		OtherScript.append("	Qgrad=_value;\n");
		OtherScript.append("	e.record.set('QGRAD',Qgrad);\n"); 
		OtherScript.append("	}\n");
//		��λ��
		OtherScript.append("	if(eval(Qgrad)!=0&&eval(Had)!=0&&eval(Mt)!=0&&eval(Mad)!=0){\n");
		OtherScript.append("	_value=Math.round(((Qgrad-0.206*Had)*(100-Mt)/(100-Mad)-0.023*Mt)*100)/100;\n");
		OtherScript.append("	e.record.set('QNET_AR',_value);\n"); 
		OtherScript.append("	}\n");
//		�յ����ҷ�
		OtherScript.append("	if(eval(Aad)!=0&&eval(Mt)!=0&&eval(Mad)!=0){\n");
		OtherScript.append("	_value=Math.round((Aad*(100-Mt)/(100-Mad))*100)/100;\n");
		OtherScript.append("	e.record.set('AAR',_value);\n"); 
		OtherScript.append("	}\n");
//		������ҷ�
		OtherScript.append("	if(eval(Aad)!=0&&eval(Mad)!=0){\n");
		OtherScript.append("	_value=Math.round((Aad*100/(100-Mad))*100)/100;\n");
		OtherScript.append("	e.record.set('AD',_value);\n"); 
		OtherScript.append("	}\n");
//		�����޻һ��ӷ���
		OtherScript.append("	if(eval(Vad)!=0&&eval(Mad)!=0&&eval(Aad)!=0){\n");
		OtherScript.append("	_value=Math.round((Vad*100/(100-Mad-Aad))*100)/100;\n");
		OtherScript.append("	e.record.set('VDAF',_value); \n");
		OtherScript.append("	}\n");
//		�յ����ӷ���
		OtherScript.append("	if(eval(Vad)!=0&&eval(Mad)!=0&&eval(Mt)!=0){\n");
		OtherScript.append("	_value=Math.round((Vad*(100-Mt)/(100-Mad))*100)/100;\n");
		OtherScript.append("	e.record.set('VAR',_value); \n");
		OtherScript.append("	}\n");
//		�̶�̼
		OtherScript.append("	if(eval(Aad)!=0&&eval(Mad)!=0&&eval(Vad)!=0){\n");
		OtherScript.append("	_value=Math.round((100-Aad-Mad-Vad)*100)/100;\n");
		OtherScript.append("	e.record.set('FCAD',_value); \n");
		OtherScript.append("	}\n");

		OtherScript.append("	if(eval(St_ad)!=0&&eval(Mad)!=0&&eval(Mt)!=0){\n");
		OtherScript.append("	_value=Math.round((St_ad*(100-Mt)/(100-Mad))*100)/100;\n");
		OtherScript.append("	e.record.set('STAR',_value); \n");
		
		OtherScript.append("	_value=Math.round((St_ad* 100 / (100 - Mad))*100)/100;\n");
		OtherScript.append("	e.record.set('STD',_value); \n");
		OtherScript.append("	}\n");
		
		OtherScript.append("	if(eval(Had )!=0&&eval(Mad)!=0&&eval(Aad)!=0){\n");
		OtherScript.append("	_value=Math.round((Had * 100 / (100 - Mad - Aad))*100)/100;\n");
		OtherScript.append("	e.record.set('HDAF',_value); \n");
		OtherScript.append("	}\n");
		OtherScript.append("	});\n");
		
		egu.addOtherScript(OtherScript.toString());

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

	// ����Сʱ������
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	// ���÷���������
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}

	// ������������
	public IDropDownBean getSecValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getSecModel().getOptionCount(); i++) {
				Object obj = getSecModel().getOption(i);
				if (DateUtil.getSeconds(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setSecValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setSecValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getSecModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getSecModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setSecModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getSecModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setSecModel(new IDropDownModel(listMin));
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
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			setSecValue(null);
			setSecModel(null);
			getSelectData();
		}
	}
}
