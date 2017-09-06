package com.zhiren.jt.dtsx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom;

/**
 * @author huo1
 *2010-03-02��Դ��������ֹ�˾���˲�����ĵ����ձ������İ�ú���������ú��λ¼���¼ƻ���ҳ��
 *���ݱ�����ԭ�����ݿ��yuecgjhb,�������������Yuecgjhb.java
 */
public class Yuecgjhb_sx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO �Զ����ɷ������
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
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if (flag != -1) {
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	private boolean _ShengcChick = false;

	public void ShengcButton(IRequestCycle cycle) {
		_ShengcChick = true;
	}

	private boolean _XiadChick = false;

	public void XiadButton(IRequestCycle cycle) {
		_XiadChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData(null);
		}
		if (_ShengcChick) {
			_ShengcChick = false;
			Shengc();
		}
		if (_XiadChick) {
			_XiadChick = false;
			Xiad();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}
	
	public void Xiad() {
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// �������
		String strriq = intyear + "-" + StrMonth1 + "-01";
		
		StringBuffer strmsg = new StringBuffer();
		
		String sql_dianc="select id,mingc from diancxxb where (id="+this.getTreeid()
		+" or fuid="+this.getTreeid()+" or shangjgsid="+this.getTreeid()+") and jib=3 ";
		
		ResultSetList rs = con.getResultSetList(sql_dianc);
		while (rs.next()){
			if (Xiad_dc(strriq,rs.getString("id"))){
				strmsg.append(rs.getString("mingc")).append(":").append(
				"�´����ݳɹ�!").append("<br>");
			}else{
				strmsg.append(rs.getString("mingc")).append(":").append(
						"�´�����ʧ��").append("<br>");
			}
		}
		
		if (strmsg.toString().equals("")) {
			setMsg("û���´����ݣ�");
		} else {
			strmsg.delete(strmsg.length() - 4, strmsg.length());
			setMsg(strmsg.toString());
		}
	}
	
	public boolean Xiad_dc(String riq,String diancxxbid) {
		JDBCcon con = new JDBCcon();
		
		String sql ="select y.id, riq, diancxxb_id,g.bianm as gongysbm,j.bianm as jihkjbm,f.mingc as faz, yuejhcgl,\n" +
				"chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,biaomdj\n" + 
				"from yuecgjhb y,diancxxb d,gongysb g,chezxxb f,jihkjb j\n" + 
				"where y.diancxxb_id=d.id(+) and y.gongysb_id=g.id(+) and y.faz_id=f.id(+) " +
				"and y.jihkjb_id=j.id(+) and riq=to_date('"+riq+"','yyyy-mm-dd') " +
				"and diancxxb_id="+diancxxbid+"";
		
		ResultSetList rs = con.getResultSetList(sql);
		
		String sb[] = new String[rs.getRows()+1];
		
		sb[0]="delete from yukb where riq=to_date('"+ riq+ "','yyyy-mm-dd') and diancxxb_id in (select cb.id from diancxxb dc,changbb cb where cb.diancxxb_id=dc.id and cb.id_jit = "+ diancxxbid+")";
		int i=0;
		//System.out.println(sb[0]);
		while (rs.next()){
			i=i+1;
			sb[i]= "insert into yukb\n"
				+ "  (id, riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
				+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
				+ "  biaomdj, yunsfsb_id, jizzt, zhuangt, meikxxb_id) values (\n"
				+ "  XL_YUKB_ID.nextval,to_date('"
				+ riq
				+ "','yyyy-mm-dd'),nvl((select cb.id from diancxxb dc,changbb cb where cb.diancxxb_id=dc.id and cb.id_jit ="
				+ rs.getLong("diancxxb_id")
				+ "),0),"
				+ "  nvl((select max(id) as meikid from vwfahdwb where meikbm='"
				+ rs.getString("gongysbm")
				+ "'),0),nvl((select id from jihkjb where koujbm='"
				+ rs.getString("jihkjbm")
				+ "'),0),"
				+ "  nvl((select id from chezxxb where jianc='"
				+ rs.getString("faz")
				+ "'),0),nvl((select dc.chezxxb_id from diancxxb dc,changbb cb where cb.diancxxb_id=dc.id and cb.id_jit ="
				+ rs.getLong("diancxxb_id") + "),0)," + rs.getDouble("yuejhcgl") + "," + rs.getDouble("chebjg") + ","
				+ rs.getDouble("yunf") + "," + rs.getDouble("zaf") + "," + rs.getDouble("rez") + ",'0','0'," + rs.getDouble("huiff")
				+ "," + rs.getDouble("liuf") + "," + rs.getDouble("daocj") + "," + rs.getDouble("biaomdj") + ","
				+ " 0,0,0,0)";
			//System.out.println("��"+i+"�� "+rs.getLong("diancxxb_id")+"sql=;\n"+sb[i]+"\n");
		}
		
		InterCom inter = new InterCom();
		boolean flag=true;
		
		String[] insert = inter.sqlExe(diancxxbid, sb, true);
		if (insert[0].equals("true")) {// �ж����´�ɹ�
			String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
					+ riq
					+ "','yyyy-mm-dd') and diancxxb_id="+diancxxbid;

			con.getUpdate(update_sql);
			flag =true;
		} else {
			flag =false;
		}
		con.Close();
		rs.close();
		return flag;
	}

	public void Xiad1() {// �´�

		// Visit visit = (Visit) this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// �������
		String strriq = intyear + "-" + StrMonth1 + "-01";
		//
		StringBuffer strmsg = new StringBuffer();// ��Ϣ��

		String shangygdiancid = "";
		int j = 0;// �������ݵ����
		StringBuffer yuecgid = new StringBuffer();
		int rows = Shuj.size();
		String sql[] = new String[rows];

		InterCom inter = new InterCom();

		Iterator it = Shuj.iterator();

		for (int i = 0; it.hasNext(); i++) {
			String v[] = (String[]) it.next();// �õ�ÿһ�е�����

			String yuecgjhid = v[0];// id
			String riq = v[1];// ����
			String diancxxbmc = v[2];// �糧����
			String diancbm = getIDropDownDiancid(diancxxbmc);// getIDropDownDiancbm(diancxxbmc);//�糧����
			String gonghdwmc = v[3];// ������λ����
			String gonghdwbm = getIDropDownGonghdwbm(gonghdwmc);// ������λ����
			String jihkjmc = v[4];// �ƻ��ھ�����
			String fazmc = v[5];// ��վ����
			// String daozmc=v[6];//��վ����
			String yunsfsmc = v[7];// ���䷽ʽ
			String yuejhcgl = v[8];// �¼ƻ��ɹ���
			String rez = v[9];// ��ֵ
			String huiff = v[10];// �ӷ���
			String liuf = v[11];// ���
			String chebjg = v[12];// ����۸�
			String yunf = v[13];// �˷�
			String zaf = v[14];// �ӷ�
			String daocj = v[15];// ������
			String biaomdj = v[16];// ��ú����

			if (i == 0) {
				yuecgid.append(yuecgjhid).append(",");// �ռ��²ɹ��ƻ��´����ݵ�ID

				sql[j++] = "delete from yukb where \n"
						+ "  riq=to_date('"
						+ riq
						+ "','yyyy-mm-dd') and diancxxb_id= "
						+ diancbm
						+ " and "
						+ "  gongysb_id= nvl((select max(id) as meikid from vwfahdwb where meikbm='"
						+ gonghdwbm
						+ "'),0) and "
						+ "  jihkjb_id= nvl((select id from jihkjb where mingc='"
						+ jihkjmc
						+ "'),0) and "
						+ "  faz_id= nvl((select id from chezxxb where jianc='"
						+ fazmc
						+ "'),0) and "
						+ "  daoz_id= nvl((select chezxxb_id from diancxxb where diancbm='"
						+ diancbm
						+ "'),0) and "
						+ "  yunsfsb_id= nvl((select id from yunsfsb where mingc ='"
						+ yunsfsmc + "'),0) ";

				sql[j++] = "insert into yukb\n"
						+ "  (id, riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
						+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
						+ "  biaomdj, yunsfsb_id, jizzt, zhuangt, meikxxb_id) values (\n"
						+ "  XL_YUKB_ID.nextval,to_date('"
						+ riq
						+ "','yyyy-mm-dd'),nvl((select cb.id from diancxxb dc,changbb cb where cb.diancxxb_id=dc.id and dc.id ="
						+ diancbm
						+ "),0),"
						+ "  nvl((select max(id) as meikid from vwfahdwb where meikbm='"
						+ gonghdwbm
						+ "'),0),nvl((select id from jihkjb where mingc='"
						+ jihkjmc
						+ "'),0),"
						+ "  nvl((select id from chezxxb where jianc='"
						+ fazmc
						+ "'),0),nvl((select chezxxb_id from diancxxb where diancbm='"
						+ diancbm + "'),0)," + yuejhcgl + "," + chebjg + ","
						+ yunf + "," + zaf + "," + rez + ",'0','0'," + huiff
						+ "," + liuf + "," + daocj + "," + biaomdj + ","
						+ "  nvl((select id from yunsfsb where mingc ='"
						+ yunsfsmc + "'),0),0,0,0)";
				// j++;
				shangygdiancid = diancxxbmc;

				if (i == rows - 1) {// ���һ����
					String sql1[] = new String[j];
					for (int ii = 0; ii < sql1.length; ii++) {
						sql1[ii] = sql[ii];
					}
					String diancxxbid = getIDropDownDiancid(shangygdiancid);// �õ��ϸ��糧ID
					String[] insert = inter.sqlExe(diancxxbid, sql1, true);
					yuecgid.deleteCharAt(yuecgid.length() - 1);// ȥ�����һ��
					if (insert[0].equals("true")) {// �ж����´�ɹ�
						String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
								+ strriq
								+ "','yyyy-mm-dd') and id in ("
								+ yuecgid.toString() + ")";

						con.getUpdate(update_sql);

						strmsg.append(shangygdiancid).append(":").append(
								"�´����ݳɹ�!").append("<br>");
					} else {
						strmsg.append(shangygdiancid).append(":").append(
								insert[0]).append("<br>");
					}
				}
			} else {
				if (!diancxxbmc.equals(shangygdiancid)) {// �ж��Ƿ���ͬһ���糧���������
					String diancxxbid = "";
					diancxxbid = getIDropDownDiancid(shangygdiancid);// �õ��ϸ��糧ID
					String sql1[] = new String[j];
					for (int ii = 0; ii < sql1.length; ii++) {
						sql1[ii] = sql[ii];
					}
					String[] insert = inter.sqlExe(diancxxbid, sql1, true);
					yuecgid.deleteCharAt(yuecgid.length() - 1);// ȥ�����һ��
					if (insert[0].equals("true")) {// �ж����´�ɹ�
						String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
								+ strriq
								+ "','yyyy-mm-dd') and id in ("
								+ yuecgid.toString() + ")";

						con.getUpdate(update_sql);

						strmsg.append(shangygdiancid).append(":").append(
								"�´����ݳɹ�!").append("<br>");

					} else {

						strmsg.append(shangygdiancid).append(":").append(
								insert[0]).append("<br>");
					}

					yuecgid.setLength(0);// ���
					sql = new String[rows];
					j = 0;

					sql[j++] = "delete from yukb where \n"
							+ "  riq=to_date('"
							+ riq
							+ "','yyyy-mm-dd') and diancxxb_id= "
							+ diancbm
							+ " and "
							+ "  gongysb_id= nvl((select max(id) as meikid from vwfahdwb where meikbm='"
							+ gonghdwbm
							+ "'),0) and "
							+ "  jihkjb_id= nvl((select id from jihkjb where mingc='"
							+ jihkjmc
							+ "'),0) and "
							+ "  faz_id= nvl((select id from chezxxb where jianc='"
							+ fazmc
							+ "'),0) and "
							+ "  daoz_id= nvl((select chezxxb_id from diancxxb where diancbm='"
							+ diancbm
							+ "'),0) and "
							+ "  yunsfsb_id= nvl((select id from yunsfsb where mingc ='"
							+ yunsfsmc + "'),0) ";

					sql[j++] = "insert into yukb\n"
							+ "  (id, riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
							+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
							+ "  biaomdj, yunsfsb_id, jizzt, zhuangt, meikxxb_id) values (\n"
							+ "  XL_YUKB_ID.nextval,to_date('"
							+ riq
							+ "','yyyy-mm-dd'),nvl((select cb.id from diancxxb dc,changbb cb where cb.diancxxb_id=dc.id and dc.id ="
							+ diancbm
							+ "),0),"
							+ "  nvl((select max(id) as meikid from vwfahdwb where meikbm='"
							+ gonghdwbm
							+ "'),0),nvl((select id from jihkjb where mingc='"
							+ jihkjmc
							+ "'),0),"
							+ "  nvl((select id from chezxxb where jianc='"
							+ fazmc
							+ "'),0),nvl((select chezxxb_id from diancxxb where diancbm='"
							+ diancbm + "'),0)," + yuejhcgl + "," + chebjg
							+ "," + yunf + "," + zaf + "," + rez + ",'0','0',"
							+ huiff + "," + liuf + "," + daocj + "," + biaomdj
							+ ","
							+ "  nvl((select id from yunsfsb where mingc ='"
							+ yunsfsmc + "'),0),0,0,0)";

					// j++;
					shangygdiancid = diancxxbmc;
					yuecgid.append(yuecgjhid).append(",");// �ռ��²ɹ��ƻ��´����ݵ�ID

					if (i == rows - 1) {// ���һ����
						diancxxbid = getIDropDownDiancid(shangygdiancid);// �õ��ϸ��糧ID
						String sql2[] = new String[j];
						for (int ii = 0; ii < sql2.length; ii++) {
							sql2[ii] = sql[ii];
						}
						String[] insert1 = inter.sqlExe(diancxxbid, sql2, true);
						yuecgid.deleteCharAt(yuecgid.length() - 1);// ȥ�����һ��
						if (insert1[0].equals("true")) {// �ж����´�ɹ�
							String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
									+ strriq
									+ "','yyyy-mm-dd') and id in ("
									+ yuecgid.toString() + ")";

							con.getUpdate(update_sql);

							strmsg.append(shangygdiancid).append(":").append(
									"�´����ݳɹ�!").append("<br>");
						} else {
							strmsg.append(shangygdiancid).append(":").append(
									insert1[0]).append("<br>");
						}
					}

				} else {
					sql[j++] = "delete from yukb where \n"
							+ "  riq=to_date('"
							+ riq
							+ "','yyyy-mm-dd') and diancxxb_id= "
							+ diancbm
							+ " and "
							+ "  gongysb_id= nvl((select max(id) as meikid from vwfahdwb where meikbm='"
							+ gonghdwbm
							+ "'),0) and "
							+ "  jihkjb_id= nvl((select id from jihkjb where mingc='"
							+ jihkjmc
							+ "'),0) and "
							+ "  faz_id= nvl((select id from chezxxb where jianc='"
							+ fazmc
							+ "'),0) and "
							+ "  daoz_id= nvl((select chezxxb_id from diancxxb where diancbm='"
							+ diancbm
							+ "'),0) and "
							+ "  yunsfsb_id= nvl((select id from yunsfsb where mingc ='"
							+ yunsfsmc + "'),0) ";

					sql[j++] = "insert into yukb\n"
							+ "  (id, riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
							+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
							+ "  biaomdj, yunsfsb_id, jizzt, zhuangt, meikxxb_id) values (\n"
							+ "  XL_YUKB_ID.nextval,to_date('"
							+ riq
							+ "','yyyy-mm-dd'),nvl((select cb.id from diancxxb dc,changbb cb where cb.diancxxb_id=dc.id and dc.id ="
							+ diancbm
							+ "),0),"
							+ "  nvl((select max(id) as meikid from vwfahdwb where meikbm='"
							+ gonghdwbm
							+ "'),0),nvl((select id from jihkjb where mingc='"
							+ jihkjmc
							+ "'),0),"
							+ "  nvl((select id from chezxxb where jianc='"
							+ fazmc
							+ "'),0),nvl((select chezxxb_id from diancxxb where diancbm='"
							+ diancbm + "'),0)," + yuejhcgl + "," + chebjg
							+ "," + yunf + "," + zaf + "," + rez + ",'0','0',"
							+ huiff + "," + liuf + "," + daocj + "," + biaomdj
							+ ","
							+ "  nvl((select id from yunsfsb where mingc ='"
							+ yunsfsmc + "'),0),0,0,0)";
					// j++;
					shangygdiancid = diancxxbmc;
					yuecgid.append(yuecgjhid).append(",");// �ռ��²ɹ��ƻ��´����ݵ�ID

					if (i == rows - 1) {// ���һ����
						String diancxxbid = getIDropDownDiancid(shangygdiancid);// �õ��ϸ��糧ID
						String sql2[] = new String[j];
						for (int ii = 0; ii < sql2.length; ii++) {
							sql2[ii] = sql[ii];
						}
						String[] insert = inter.sqlExe(diancxxbid, sql2, true);
						yuecgid.deleteCharAt(yuecgid.length() - 1);// ȥ�����һ��
						if (insert[0].equals("true")) {// �ж����´�ɹ�
							String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
									+ strriq
									+ "','yyyy-mm-dd') and id in ("
									+ yuecgid.toString() + ")";

							con.getUpdate(update_sql);

							strmsg.append(shangygdiancid).append(":").append(
									"�´����ݳɹ�!").append("<br>");
						} else {
							strmsg.append(shangygdiancid).append(":").append(
									insert[0]).append("<br>");
						}
					}
				}

			}

		}
		if (strmsg.toString().equals("")) {
			setMsg("û���´����ݣ�");
		} else {
			strmsg.delete(strmsg.length() - 4, strmsg.length());
			setMsg(strmsg.toString());
		}
		con.Close();
	}

	public void Shengc() {// ����
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// �������
		String strriq = intyear + "-" + StrMonth1 + "-01";

		// �õ��糧����
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		/*
		 * int treejib = this.getDiancTreeJib(); if (treejib == 1) {//
		 * ѡ����ʱˢ�³����еĵ糧 str = ""; } else if (treejib == 2) {//
		 * ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧 str = "and (dc.id = " + getTreeid() + " or
		 * dc.fuid = " + getTreeid() + ")"; } else if (treejib == 3) {//
		 * ѡ�糧ֻˢ�³��õ糧 str = "and dc.id = " + getTreeid() + ""; }
		 */

		String sql = "select * from yuecgjhb y,vwdianc dc where y.diancxxb_id=dc.id and riq=to_date('"
				+ strriq + "','yyyy-mm-dd') " + str + " ";

		if (con.getHasIt(sql)) {// ������

		} else {// û������
			String inser_sql = "insert into yuecgjhb\n"
					+ "  (id, riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
					+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
					+ "  biaomdj, yunsfsb_id, jizzt, zhuangt, meikxxb_id)\n"
					+ "(select getnewid(diancxxb_id) as id,riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
					+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
					+ "  biaomdj, yunsfsb_id, jizzt, 1, meikxxb_id\n"
					+ "  from yuesbjhb y,vwdianc dc where y.diancxxb_id=dc.id and riq=to_date('"
					+ strriq + "','yyyy-mm-dd') " + str + " )";

			con.getInsert(inser_sql);// ��������
		}

		con.Close();
	}

	public void CoypLastYueData() {// ������������
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// -----------------------------------
		String strriq = intyear + "-" + StrMonth1 + "-01";

		// ���·���1ʱ�ϸ�����ʾ12
		if (intMonth - 1 == 0) {
			intMonth = 12;
			intyear = intyear - 1;
		} else {
			intMonth = intMonth - 1;
		}
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}

		String str_jizzt = "";

		if (getLeixDropDownValue() != null) {
			str_jizzt = " and y.jizzt=" + getLeixDropDownValue().getId();
		}

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and (dc.fuid=  " + this.getTreeid()+ " or dc.shangjgsid=" + this.getTreeid() + " or dc.id="+this.getTreeid()+")";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";

		}

		String chaxun = "select y.id as id,\n"
				+ "       y.riq as riq,\n"
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       j.mingc as jihkjb_id,\n"
				+ "       ch.mingc as faz_id,\n"
				+ "       che.mingc as daoz_id,\n"
				+ "       ys.mingc as yunsfsb_id,\n"
				+ "       y.yuejhcgl as yuejhcgl,\n"
				+ "       y.rez as rez,\n"
				+ "       y.huiff as huiff,\n"
				+ "       y.liuf as liuf,\n"
				+ "       y.chebjg as chebjg,\n"
				+ "       y.yunf as yunf,\n"
				+ "       y.zaf as zaf,\n"
				+ "       y.daocj as daocj,\n"
				+ "       y.biaomdj as biaomdj,\n"
				+ "       y.jiakk as jiakk,\n"
				+ "       y.jihddsjysl as jihddsjysl\n ,y.jizzt \n"
				+ "  from yuecgjhb y, (select id,mingc from (\n" +
"select id,'***'||mingc||'***' as mingc from gongysb where leix=0\n" + 
"union\n" + 
"select id,mingc as mmingc from meikxxb\n" + 
"))  g, chezxxb ch, chezxxb che, diancxxb dc,jihkjb j,yunsfsb ys\n"
				+ " where y.gongysb_id = g.id(+)\n"
				+ "   and y.faz_id = ch.id(+)\n"
				+ "   and y.daoz_id = che.id(+)\n"
				+ "   and y.diancxxb_id = dc.id(+)\n"
				+ "   and y.jihkjb_id = j.id(+)\n"
				+ "   and y.yunsfsb_id = ys.id(+)\n"
				+ "   and to_char(y.riq,'yyyy-mm') ='" + intyear + "-"
				+ StrMonth + "'  \n" + "    " + str + "  " + str_jizzt
				+ "   order by y.id";

		// System.out.println("����ͬ�ڵ�����:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(chaxun);
		while (rslcopy.next()) {

			double yuejhcgl = rslcopy.getDouble("yuejhcgl");
			double huiff = rslcopy.getDouble("huiff");
			double liuf = rslcopy.getDouble("liuf");
			double daocj = rslcopy.getDouble("daocj");
			double biaomdj = rslcopy.getDouble("biaomdj");
			double chebjg = rslcopy.getDouble("chebjg");
			double yunf = rslcopy.getDouble("yunf");
			double zaf = rslcopy.getDouble("zaf");
			String rez = rslcopy.getString("rez");
			String jiakk = rslcopy.getString("jiakk");
			String jihddsjysl = rslcopy.getString("jihddsjysl");
			String jizzt = rslcopy.getString("jizzt");
			// Date riq=rslcopy.getDate("riq");
			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			String sql = "insert into yuecgjhb(id,riq,diancxxb_id,gongysb_id,jihkjb_id,faz_id,daoz_id,yuejhcgl,chebjg,yunf,zaf,rez,jiakk,jihddsjysl,huiff,liuf,daocj,biaomdj,yunsfsb_id,jizzt) values("
					+
					// _id+","+"to_date('"+strriq+"','yyyy-mm-dd')"
					_id
					+ ","
					+ "to_date('"
					+ strriq
					+ "','yyyy-mm-dd')"
					+ ",(select id from diancxxb where mingc='"
					+ rslcopy.getString("diancxxb_id")
					+ "'),(select id from (select id,mingc from (\n" +
"select id,'***'||mingc||'***' as mingc from gongysb where leix=0\n" + 
"union\n" + 
"select id,mingc as mmingc from meikxxb\n" + 
")) where mingc='"
					+ rslcopy.getString("gongysb_id")
					+ "'),(select id from jihkjb where mingc='"
					+ rslcopy.getString("jihkjb_id")
					+ "'),(select id from chezxxb where mingc='"
					+ rslcopy.getString("faz_id")
					+ "'),(select id from chezxxb where mingc='"
					+ rslcopy.getString("daoz_id")
					+ "'),"
					+ yuejhcgl
					+ ","
					+ chebjg
					+ ","
					+ yunf
					+ ","
					+ zaf
					+ ",'"
					+ rez
					+ "','"
					+ jiakk
					+ "','"
					+ jihddsjysl
					+ "',"
					+ huiff
					+ ","
					+ liuf
					+ ","
					+ daocj
					+ ","
					+ biaomdj
					+ ",getYunsfsbId('"
					+ rslcopy.getString("yunsfsb_id") + "')," + jizzt + ")";
			con.getInsert(sql);
			con.commit();
		}
		getSelectData(null);
		con.Close();
	}

	// private String FormatDate(Date _date) {
	// if (_date == null) {
	// return "";
	// }
	// return DateUtil.Formatdate("yyyy-MM-dd", _date);
	// }

	// private ResultSetList Shuj=null;
	private List Shuj = new ArrayList();

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}

		visit.setString3("" + intyear + "-" + StrMonth);// ���
		// -----------------------------------

		// String str_jizzt ="";
		//		
		// if(getLeixDropDownValue()!=null){
		// str_jizzt=" and y.jizzt="+getLeixDropDownValue().getId();
		// }

		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}

		int jib = this.getDiancTreeJib();

		StringBuffer strSQL = new StringBuffer();

		StringBuffer strgrouping = new StringBuffer();
		StringBuffer strwhere = new StringBuffer();
		StringBuffer strgroupby = new StringBuffer();
		StringBuffer strhaving = new StringBuffer();
		StringBuffer strorderby = new StringBuffer();

		if (rsl == null) {

			if (jib == 1) {// ѡ����ʱˢ�³����еĵ糧
				strgrouping
						.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,dc.mingc) as diancxxb_id,\n");
				strgrouping
						.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
				strwhere.append("");
				strgroupby
						.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc,j.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt,y.zhuangt)\n");
				strhaving
						.append(" having not (grouping(g.mingc) || grouping(y.zhuangt)) =1 \n");
				strorderby
						.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
			} else if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				String ranlgs = "select id from diancxxb where shangjgsid= "
						+ this.getTreeid();

				try {
					ResultSet rl = con.getResultSet(ranlgs);
					if (rl.next()) {// ȼ�Ϲ�˾
						strgrouping
								.append("decode(grouping(dc.rlgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.rlgsmc,dc.mingc) as diancxxb_id,\n");
						strgrouping
								.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
						strwhere
								.append(" and (dc.id=" + this.getTreeid()
										+ " or dc.fuid= " + this.getTreeid()
										+ ") \n");
						strgroupby
								.append("group by rollup(dc.rlgsmc,dc.mingc,g.mingc,j.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt,y.zhuangt)\n");
						strhaving
								.append("having not (grouping(g.mingc) || grouping(y.zhuangt)) =1 and not grouping(dc.rlgsmc)=1\n");
						strorderby
								.append("order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
					} else {// �ֹ�˾
						strgrouping
								.append("decode(grouping(dc.mingc),1,'�ܼ�',1,dc.mingc,dc.mingc) as diancxxb_id,\n");
						strgrouping
								.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
						strwhere
								.append(" and (dc.fuid=" + this.getTreeid()
										+ " or dc.id= " + this.getTreeid()
										+ ") \n");
						strgroupby
								.append("group by rollup(dc.mingc,g.mingc,j.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt,y.zhuangt)\n");
						strhaving
								.append("having not grouping(zhuangt)=1 \n");
						strorderby
								.append("order by grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
					}
					rl.close();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					con.Close();
				}
			} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
				strgrouping
						.append("decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as diancxxb_id,\n");
				strgrouping
						.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby
						.append("group by rollup(dc.mingc,g.mingc,j.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt,y.zhuangt)\n");
				strhaving
						.append("having not (grouping(g.mingc) || grouping(y.zhuangt)) =1 and not grouping(dc.mingc)=1\n");
				strorderby
						.append("order by grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
			}

			strSQL.append("select \n");
			strSQL.append("max(y.id) as id,max(y.riq) as riq,\n");
			strSQL.append(strgrouping);
			strSQL.append("nvl(j.mingc,'-') as jihkjb_id,nvl(ch.mingc,'-') as faz_id,nvl(che.mingc,'-') as daoz_id,nvl(ys.mingc,'') as yunsfsb_id,\n");
			strSQL.append("sum(y.yuejhcgl) as yuejhcgl,");
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl),2)) as rez,\n");
			strSQL.append("decode(sum(y.yuejhcgl),0,0,round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl)*1000/4.1816,2)) as rez_dk,\n");// ��ֵ_��
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n");// �ӷ���
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n");// ���
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as chebjg,\n");
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.yunf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as yunf,\n");
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.zaf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as zaf,\n");
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl),2)) as daocj,\n");
			//strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.biaomdj*y.yuejhcgl)/sum(y.yuejhcgl),2)) as biaomdj,\n");
			
			strSQL.append("Round(decode(sum(y.rez*y.yuejhcgl),0,0,Round((sum((y.chebjg/(1+0.17)+y.yunf*(1-0.07)+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl)),2)*29.271/Round((sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl)),2)),2) as biaomdj,\n");
			
			strSQL.append("max(y.jiakk) as jiakk,max(y.jihddsjysl) as jihddsjysl,y.jizzt,y.zhuangt\n");
			strSQL.append(" from yuecgjhb y, (select id,mingc from (\n" +
"select id,'***'||mingc||'***' as mingc from gongysb where leix=0\n" + 
"union\n" + 
"select id,mingc as mmingc from meikxxb\n" + 
")) g, chezxxb ch, chezxxb che, diancxxb dc,jihkjb j,yunsfsb ys\n");
			strSQL.append("  where y.gongysb_id = g.id(+) \n");
			strSQL.append(" and y.faz_id = ch.id(+) and y.daoz_id = che.id(+) and y.diancxxb_id = dc.id(+) \n");
			strSQL.append(" and y.jihkjb_id = j.id(+) and y.yunsfsb_id = ys.id(+)");
			strSQL.append(" and y.riq=to_date('" + intyear + "-" + StrMonth
					+ "-01','yyyy-mm-dd') \n");
			strSQL.append(strwhere);
			strSQL.append(strgroupby);
			strSQL.append(strhaving);
			strSQL.append(strorderby);

			/*
			 * String chaxun = "select y.id as id,\n" + "
			 * getLinkMingxTaiz('1',dc.id,'���������ƻ�') as mingx,\n" + " y.riq as
			 * riq,\n" + " dc.mingc as diancxxb_id,\n" + " g.mingc as
			 * gongysb_id,\n" + " j.mingc as jihkjb_id,\n" + " ch.mingc as
			 * faz_id,\n" + " che.mingc as daoz_id,\n" + " ys.mingc as
			 * yunsfsb_id,\n" + " y.yuejhcgl as yuejhcgl,\n" + " y.rez as
			 * rez,\n" + " y.huiff as huiff,\n" + " y.liuf as liuf,\n" + "
			 * y.chebjg as chebjg,\n" + " y.yunf as yunf,\n" + " y.zaf as
			 * zaf,\n" + " y.daocj as daocj,\n" + " y.biaomdj as biaomdj,\n" + "
			 * y.jiakk as jiakk,\n" + " y.jihddsjysl as jihddsjysl\n
			 * ,y.jizzt,y.zhuangt \n" + " from yuecgjhb y, gongysb g, chezxxb
			 * ch, chezxxb che, diancxxb dc,jihkjb j,yunsfsb ys\n" + " where
			 * y.gongysb_id = g.id(+)\n" + " and y.faz_id = ch.id(+)\n" + " and
			 * y.daoz_id = che.id(+)\n" + " and y.diancxxb_id = dc.id(+)\n" + "
			 * and y.jihkjb_id = j.id(+)\n" + " and y.yunsfsb_id = ys.id(+)\n" + // "
			 * and y.zhuangt="+getZhuangtDropDownValue().getId()+"\n"+ " and
			 * to_char(y.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth + "' \n" + " " +
			 * str + // " "+ str_jizzt + " order by y.id";
			 */
			// System.out.println(chaxun);
			rsl = con.getResultSetList(strSQL.toString());
			// Shuj = rsl;//������ݵ���Ϊ�´������á�
			Shuj = rsl.getResultSetlist();

		}
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yuecgjhb");
		egu.setWidth("bodyWidth");
		egu.getColumn("riq").setCenterHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		/*
		 * egu.getColumn("mingx").setCenterHeader("��ϸ"); if (flage == 0) {//
		 * �Ƿ��е�Ͷʹ�� egu.getColumn("mingx").setHidden(true); }
		 */
		egu.setDefaultsortable(false);//����ÿ�б�ͷ����󲻿�������
		egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
		egu.getColumn("gongysb_id").setCenterHeader("������λ");
		egu.getColumn("jihkjb_id").setCenterHeader("�ƻ��ھ�");
		egu.getColumn("faz_id").setCenterHeader("��վ");
		egu.getColumn("daoz_id").setCenterHeader("��վ");
		egu.getColumn("yunsfsb_id").setCenterHeader("����<br>��ʽ");
		egu.getColumn("yuejhcgl").setCenterHeader("�ƻ���<br>(��)");
		egu.getColumn("rez").setCenterHeader("��ֵ<br>(MJ/Kg)");
		egu.getColumn("rez_dk").setHeader("��ֵ<br>(kcal/Kg)");
		egu.getColumn("rez_dk").setEditor(null);
		egu.getColumn("rez_dk").setUpdate(false);
		egu.getColumn("huiff").setCenterHeader("�ӷ���<br>%");
		egu.getColumn("liuf").setCenterHeader("���<br>%");
		egu.getColumn("chebjg").setCenterHeader("�����<br>(Ԫ/��)");
		egu.getColumn("yunf").setCenterHeader("�˷�<br>(Ԫ/��)");
		egu.getColumn("zaf").setCenterHeader("�ӷ�<br>(Ԫ/��)");
		egu.getColumn("daocj").setCenterHeader("������<br>(Ԫ/��)");
		egu.getColumn("biaomdj").setCenterHeader("����˰��ú<br>����(Ԫ/��)");
		egu.getColumn("jiakk").setCenterHeader("�ӿۿ�<br>(Ԫ/��)");
		egu.getColumn("jiakk").setHidden(true);
		egu.getColumn("jihddsjysl").setCenterHeader("�ƻ�����ʱ<br>��������");
		egu.getColumn("jihddsjysl").setHidden(true);

		egu.getColumn("jizzt").setHeader("����״̬");
		egu.getColumn("jizzt").setHidden(true);

		egu.getColumn("zhuangt").setHeader("����״̬");
		egu.getColumn("zhuangt").setHidden(true);

		((NumberField) egu.getColumn("rez").editor).setDecimalPrecision(2);
//		((NumberField) egu.getColumn("rez_dk").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("huiff").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("liuf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("chebjg").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("yunf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("zaf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("daocj").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("biaomdj").editor).setDecimalPrecision(2);
		
		
		// ���ò��ɱ༭����ɫ
		// egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext';
		// return value;}");

		// �趨�г�ʼ���
		egu.getColumn("riq").setWidth(80);
		// egu.getColumn("mingx").setWidth(80);
		// egu.getColumn("mingx").setEditor(null);
		// egu.getColumn("mingx").setUpdate(false);
		egu.getColumn("gongysb_id").setWidth(200);
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setDefaultValue("");
		egu.getColumn("yuejhcgl").setWidth(70);
		egu.getColumn("yuejhcgl").setDefaultValue("0");
		egu.getColumn("rez").setWidth(45);
		egu.getColumn("rez").setDefaultValue("0.00");
		egu.getColumn("rez_dk").setWidth(60);
		egu.getColumn("rez_dk").setDefaultValue("0.00");
		egu.getColumn("huiff").setWidth(50);
		egu.getColumn("huiff").setDefaultValue("0.00");
		egu.getColumn("liuf").setWidth(45);
		egu.getColumn("liuf").setDefaultValue("0.00");
		egu.getColumn("chebjg").setWidth(50);
		egu.getColumn("chebjg").setDefaultValue("0.00");
		egu.getColumn("yunf").setWidth(50);
		egu.getColumn("yunf").setDefaultValue("0.00");
		egu.getColumn("zaf").setWidth(50);
		egu.getColumn("zaf").setDefaultValue("0.00");
		egu.getColumn("daocj").setWidth(50);
		egu.getColumn("daocj").setDefaultValue("0.00");
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("biaomdj").setEditor(null);
		egu.getColumn("biaomdj").setDefaultValue("0.00");
		egu.getColumn("jiakk").setWidth(50);
		egu.getColumn("jiakk").setDefaultValue("0");
		egu.getColumn("jihddsjysl").setWidth(50);
		egu.getColumn("jihddsjysl").setDefaultValue("0");

		egu.getColumn("jizzt").setWidth(50);
		egu.getColumn("jizzt").setDefaultValue(
				"" + getLeixDropDownValue().getId());

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ
		egu.setWidth("bodyWidth");
		
		
		
		// *****************************************����Ĭ��ֵ****************************
		// �糧������
		egu.getColumn("zhuangt").setDefaultValue("0");

		if (jib == 1) {// ѡ����ʱˢ�³����еĵ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where (fuid="
									+ getTreeid() + " or shangjgsid ="
									+ getTreeid() + " or id="+getTreeid()+") order by id"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where id="
									+ getTreeid() + " order by mingc"));
			ResultSetList r = con
					.getResultSetList("select id,mingc from diancxxb where id="
							+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// ���õ糧Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		// �������ڵ�Ĭ��ֵ,
		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// *************************������*****************************************88
//		 ���ù�Ӧ�̵�������
		// egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		
		//��糧������Ĺ�Ӧ�� //���Ӳ����й�Ӧ�̣��ֹ�Ӧ�̺͵糧����������鲻����Ӧ��
		String GongysSql = 
			"select id,mingc from (\n" +
			"select m.id as id,m.mingc as mingc,m.meikdq_id as id2,1 as xuh1,g.xuh as xuh2\n" + 
			"from\n" + 
			"meikxxb m,gongysb g\n" + 
			"where m.meikdq_id=g.id\n" + 
			"union\n" + 
			"select id as id,'***'||mingc||'***' as mingc,id as id2,0 as xuh1,xuh as xuh2\n" + 
			"from gongysb\n" + 
			"where leix=0)\n" + 
			"order by xuh2,id2 ,xuh1";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		
		// ���üƻ��ھ���������
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		String JihkjSql = "select id,mingc from jihkjb order by mingc ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(JihkjSql));
//		 ���÷�վ������
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						" where f.diancxxb_id=dc.id  and f.faz_id=c.id and c.leib='��վ' "+strwhere+
						" union\n" +
						" select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
						" where n.diancxxb_id=dc.id and n.faz_id=c.id " +
						" and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
		                 "union\n" + 
		                 "select distinct c.id,c.mingc from chezxxb c  ";  
//		System.out.println(fazSql);
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		// ���õ�վ������
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						 " where f.diancxxb_id=dc.id  and f.daoz_id=c.id and c.leib='��վ' "+strwhere+
						 " union\n" +
						 " select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
						 " where n.diancxxb_id=dc.id and n.daoz_id=c.id " +
						 " and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
		                 "union\n" + 
		                 "select distinct c.id,c.mingc from chezxxb c  ";
//		System.out.println(daozSql);
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));

		// �������䷽ʽ������
		ComboBox cb_yunsfs = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_yunsfs);
		cb_daoz.setEditable(true);

		String yunsfsSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsSql));
		egu.getColumn("yunsfsb_id").editor.allowBlank=true;
		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		// �趨�������������Զ�ˢ��
		// egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���

		/*
		 * egu.addTbarText("����״̬:"); ComboBox comb3=new ComboBox();
		 * comb3.setTransform("LeixDropDown");
		 * comb3.setId("LeixDropDown");//���Զ�ˢ�°�
		 * comb3.setLazyRender(true);//��̬�� comb3.setWidth(80);
		 * egu.addToolbarItem(comb3.getScript());
		 * 
		 * egu.addOtherScript("LeixDropDown.on('select',function(){document.forms[0].submit();});");
		 * egu.addTbarText("-");// ���÷ָ���
		 */

		/*
		 * if (flage==1){//������е�Ͷ�û����´����� egu.addTbarText("����״̬:"); ComboBox
		 * comb4=new ComboBox(); comb4.setTransform("ZhuangtDropDown");
		 * comb4.setId("ZhuangtDropDown");//���Զ�ˢ�°�
		 * comb4.setLazyRender(true);//��̬�� comb4.setWidth(80);
		 * egu.addToolbarItem(comb4.getScript());
		 * 
		 * egu.addOtherScript("ZhuangtDropDown.on('select',function(){document.forms[0].submit();});");
		 * egu.addTbarText("-");// ���÷ָ��� }
		 */

		// ˢ�°�ť
		StringBuffer rsb2 = new StringBuffer();
		rsb2
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'����ˢ��'+Ext.getDom('NIANF').value+'��'+Ext.getDom('YUEF').value+'�µ�����,���Ժ�'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr2 = new GridButton("ˢ��", rsb2.toString());
		gbr2.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr2);

		// ���ɰ�ť
//		StringBuffer rsb = new StringBuffer();
//		rsb.append("function (){")
//		// .append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NIANF').value+'��'+Ext.getDom('YUEF').value+'�µ�����,���Ժ�'",true))
//				.append("document.getElementById('ShengcButton').click();}");
//		GridButton gbr = new GridButton("����", rsb.toString());
//		gbr.setIcon(SysConstant.Btn_Icon_Create);
//		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		// �´ﰴť
		if (flage == 1) {// ������е�Ͷ�û����´�����
			// if (getZhuangtDropDownValue().getValue().equals("δ�´�")){
			StringBuffer rsb1 = new StringBuffer();
			rsb1.append("function (){").append(
					MainGlobal
							.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200));
			// MainGlobal.getExtMessageBox("'�����´�����......,���Ժ�'",true));
			// rsb1.append(" var grid1_history =\"\";" +
			// " if(gridDiv_sm.getSelected()==null){ " +
			// " Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"��ѡ��һ�������·���\"); return; } " +
			// " grid1_rcd = gridDiv_sm.getSelected();" +
			// " if(grid1_rcd.get(\"ID\") == \"0\"){ " +
			// " Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"���´�����֮ǰ���ȱ���!\"); return; }" +
			// " grid1_history = grid1_rcd.get(\"DIANCXXB_ID\");" +
			// " var Cobj = document.getElementById(\"CHANGE\");" +
			// " Cobj.value = grid1_history; " +
			rsb1.append(" document.getElementById(\"XiadButton\").click();}");

			GridButton gbr1 = new GridButton("�´�", rsb1.toString());
			gbr1.setIcon(SysConstant.Btn_Icon_Cancel);
			egu.addTbarBtn(gbr1);
			// }
		}

		if (showBtn) {
			// egu.addToolbarItem("{"+new
			// GridButton("����ͬ�ڼƻ�","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("����ǰ������", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		// egu.addTbarText("->");
		// egu.addTbarText("<font color=\"#EE0000\">��λ:�֡�Ԫ/�֡�ǧ��/ǧ��</font>");
		
//		2009-09-24 ȡ���ܼƼ�С���е���ɫ
		String BgColor="";
		String sql_color="select zhi from xitxxb where mingc ='�ܼ�С������ɫ' and zhuangt=1 ";
		
		rsl = con.getResultSetList(sql_color);
		
		if (rsl.next()){
			BgColor=rsl.getString("zhi");
		}
		rsl.close();

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
//		StringBuffer sb = new StringBuffer();
//		
//		sb.append("var rows=gridDiv_ds.getTotalCount();\n" +
//				"for (var i=rows-1;i>=0;i--){\n" + 
//				"\t var rec1=gridDiv_ds.getAt(i);\n" + 
//				"\t var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
//				"\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//ȡС����\n" + 
//				"\t if (gonghdw1==\"-\" ||  xiaoj==\"С��\"){//С����\n" + 
//				"\t\tgridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n" + 
//				"\t}\n" + 
//				"}\n");
//		
//		sb.append("gridDiv_grid.on('beforeedit',function(e){\n"
//						+ "if(e.record.get('GONGYSB_ID')=='-'){e.cancel=true;}//�ж�Ϊ����ʱ�в��ɱ༭\n"
//						+ "var mingc=e.record.get('GONGYSB_ID');\n"
//						+ "var le=mingc.length;\n"
//						+ "var xiaoj=mingc.substring(le-6,le-4);//ȡС����\n"
//						+ "if(xiaoj=='С��'){e.cancel=true;}//�ж�С��ʱ�в��ɱ༭\n"
//						+ "});\n"
//						+ "\n"
//						+ "gridDiv_grid.on('afteredit',function(e){\n"
//						+ "e.record.set('DAOCJ',Round(eval(e.record.get('CHEBJG')||0)+eval(e.record.get('YUNF')||0)+eval(e.record.get('ZAF')||0),2));\n"
//						+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(eval(Round((eval(e.record.get('CHEBJG')||0)/1.17+eval(e.record.get('YUNF')||0)*(1-0.07)+eval(e.record.get('ZAF')||0))*29.271/eval(e.record.get('REZ')||0),2)||0),2));}\n"
//						+ "if(e.field == 'REZ'){e.record.set('REZ_DK',Round(e.value/0.0041816,2));}\n"
//						+ "\n"
//						//+ "//if(e.record.get('ZHUANGT')=='1'){document.all.item('SaveButton').disabled=true;}\n"
//						+ "\tvar rows=gridDiv_ds.getTotalCount();\n"
//						+ "\tvar colDianc=2;\n"
//						+ "\tvar colGongys=3;\n"
//						+ "\tvar colJihl=8;//�ɹ��ƻ���\n"
//						+ "\tvar colRez=9;//��ֵ\n"
//						+ "\tvar colRez_dk=10;//��ֵ_dk\n"
//						+ "\tvar colHuiff=11;//�ӷ���\n"
//						+ "\tvar colLiuf=12;//���\n"
//						+ "\tvar colChebjg=13;//����۸�\n"
//						+ "\tvar colYunf=14;//�˷�\n"
//						+ "\tvar colZaf=15;//�ӷ�\n"
//						+ "\tvar colDaocj=16;//������\n"
//						+ "\tvar colBiaomdj=17;//��ú����\n"
//						+ "\n"
//						+ "\tvar rowXJ=0;\n"
//						+ "\tvar rowZj=1;\n"
//						+ "\tvar ArrSumZJ=new Array(20);\n"
//						+ "\tvar ArrSumXJ=new Array(20);\n"
//						+ "\tvar cgl_xj;//�ɹ���С��\n"
//						+ "\n"
//						+ "\tfor (var i=0 ;i<20;i++){\n"
//						+ "\t\tArrSumXJ[i]=0.0;\n"
//						+ "\t\tArrSumZJ[i]=0.0;\n"
//						+ "\t}\n"
//						+ "\tfor (var i=rows-1;i>=0;i--){\n"
//						+ "\t\t var rec1=gridDiv_ds.getAt(i);\n"
//						+ "\t\t var gonghdw1=rec1.get('GONGYSB_ID');\n"
//						+ "\t\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//ȡС����\n"
//						+ "\t\t if (xiaoj==\"С��\"){//С����\n"
//						+ "\t\t \trec1.set('YUEJHCGL',ArrSumXJ[colJihl]);\n"
//						+ "\t\t \tif (ArrSumXJ[colJihl]==0.0){\n"
//						+ "\t\t \t\trec1.set('REZ',0);\n"
//						+ "\t\t \t\trec1.set('REZ_DK',0);\n"
//						+ "\t\t \t\trec1.set('HUIFF',0);\n"
//						+ "\t\t \t\trec1.set('LIUF',0);\n"
//						+ "\t\t \t\trec1.set('CHEBJG',0);\n"
//						+ "\t\t \t\trec1.set('YUNF',0);\n"
//						+ "\t\t \t\trec1.set('ZAF',0);\n"
//						+ "\t\t \t\trec1.set('DAOCJ',0);\n"
//						+ "\t\t \t\trec1.set('BIAOMDJ',0);\n"
//						+ "\t\t \t}else{\n"
//						+ "\t\t \t\trec1.set('REZ',Round(ArrSumXJ[colRez]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('REZ_DK',Round(ArrSumXJ[colRez_dk]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('HUIFF',Round(ArrSumXJ[colHuiff]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('LIUF',Round(ArrSumXJ[colLiuf]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('CHEBJG',Round(ArrSumXJ[colChebjg]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('YUNF',Round(ArrSumXJ[colYunf]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('ZAF',Round(ArrSumXJ[colZaf]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('DAOCJ',Round(ArrSumXJ[colDaocj]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('BIAOMDJ',Round(ArrSumXJ[colBiaomdj]/ArrSumXJ[colJihl],2));\n"
//						+ "\t\t \t}\n"
//						+ "\t\t \t//�ۼ��ܼ�\n"
//						+ "\t\t \tArrSumZJ[colJihl]=ArrSumZJ[colJihl]+ArrSumXJ[colJihl];\n"
//						+ "\t\t \tArrSumZJ[colRez]=ArrSumZJ[colRez]+ArrSumXJ[colRez];\n"
//						+ "\t\t \tArrSumZJ[colRez_dk]=ArrSumZJ[colRez_dk]+ArrSumXJ[colRez_dk];\n"
//						+ "\t\t\tArrSumZJ[colHuiff]=ArrSumZJ[colHuiff]+ArrSumXJ[colHuiff];\n"
//						+ "\t\t\tArrSumZJ[colLiuf]=ArrSumZJ[colLiuf]+ArrSumXJ[colLiuf];\n"
//						+ "\t\t\tArrSumZJ[colChebjg]=ArrSumZJ[colChebjg]+ArrSumXJ[colChebjg];\n"
//						+ "\t\t\tArrSumZJ[colYunf]=ArrSumZJ[colYunf]+ArrSumXJ[colYunf];\n"
//						+ "\t\t\tArrSumZJ[colZaf]=ArrSumZJ[colZaf]+ArrSumXJ[colZaf];\n"
//						+ "\t\t\tArrSumZJ[colDaocj]=ArrSumZJ[colDaocj]+ArrSumXJ[colDaocj];\n"
//						+ "\t\t\tArrSumZJ[colBiaomdj]=ArrSumZJ[colBiaomdj]+ArrSumXJ[colBiaomdj];\n"
//						+ "\t\t\t//���С��\n"
//						+ "\t\t \tArrSumXJ[colJihl]=0;\n"
//						+ "\t\t \tArrSumXJ[colRez]=0;\n"
//						+ "\t\t \tArrSumXJ[colRez_dk]=0;\n"
//						+ "\t\t \tArrSumXJ[colHuiff]=0;\n"
//						+ "\t\t\tArrSumXJ[colLiuf]=0;\n"
//						+ "\t\t\tArrSumXJ[colChebjg]=0;\n"
//						+ "\t\t\tArrSumXJ[colYunf]=0;\n"
//						+ "\t\t\tArrSumXJ[colZaf]=0;\n"
//						+ "\t\t\tArrSumXJ[colDaocj]=0;\n"
//						+ "\t\t\tArrSumXJ[colBiaomdj]=0;\n"
//						+ "\t\t\t gridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n"						
//						+ "\t\t }else if ( i==0) {//�ܼ���\n"
//						+ "\t\t \trec1.set('YUEJHCGL',ArrSumZJ[colJihl]);\n"
//						+ "\t\t \tif (ArrSumZJ[colJihl]==0.0){\n"
//						+ "\t\t \t\trec1.set('REZ',0);\n"
//						+ "\t\t \t\trec1.set('REZ_DK',0);\n"
//						+ "\t\t \t\trec1.set('HUIFF',0);\n"
//						+ "\t\t \t\trec1.set('LIUF',0);\n"
//						+ "\t\t \t\trec1.set('CHEBJG',0);\n"
//						+ "\t\t \t\trec1.set('YUNF',0);\n"
//						+ "\t\t \t\trec1.set('ZAF',0);\n"
//						+ "\t\t \t\trec1.set('DAOCJ',0);\n"
//						+ "\t\t \t\trec1.set('BIAOMDJ',0);\n"
//						+ "\t\t \t}else{\n"
//						+ "\t\t \t\trec1.set('REZ',Round(ArrSumZJ[colRez]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('REZ_DK',Round(ArrSumZJ[colRez_dk]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('HUIFF',Round(ArrSumZJ[colHuiff]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('LIUF',Round(ArrSumZJ[colLiuf]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('CHEBJG',Round(ArrSumZJ[colChebjg]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('YUNF',Round(ArrSumZJ[colYunf]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('ZAF',Round(ArrSumZJ[colZaf]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('DAOCJ',Round(ArrSumZJ[colDaocj]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t\trec1.set('BIAOMDJ',Round(ArrSumZJ[colBiaomdj]/ArrSumZJ[colJihl],2));\n"
//						+ "\t\t \t}\n"
//						+ "\t\t\t gridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n"						
//						+ "\t\t }else{\n"
//						+ "\t\t \t//�ۼӵ糧��\n"
//						+ "\t\t \tArrSumXJ[colJihl]=ArrSumXJ[colJihl]+eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t \tArrSumXJ[colRez]=ArrSumXJ[colRez]+eval(rec1.get('REZ')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t \tArrSumXJ[colRez_dk]=ArrSumXJ[colRez_dk]+eval(rec1.get('REZ_DK')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t \tArrSumXJ[colHuiff]=ArrSumXJ[colHuiff]+eval(rec1.get('HUIFF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t\tArrSumXJ[colLiuf]=ArrSumXJ[colLiuf]+eval(rec1.get('LIUF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t\tArrSumXJ[colChebjg]=ArrSumXJ[colChebjg]+eval(rec1.get('CHEBJG')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t\tArrSumXJ[colYunf]=ArrSumXJ[colYunf]+eval(rec1.get('YUNF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t\tArrSumXJ[colZaf]=ArrSumXJ[colZaf]+eval(rec1.get('ZAF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t\tArrSumXJ[colDaocj]=ArrSumXJ[colDaocj]+eval(rec1.get('DAOCJ')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\t\t\tArrSumXJ[colBiaomdj]=ArrSumXJ[colBiaomdj]+eval(rec1.get('BIAOMDJ')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
//						+ "\n" + "\t\t }\n" + "\t}\n" + "});");
//
//		// �趨�ϼ��в�����
//		sb.append("function gridDiv_save(record){ \n" +
//				" var gonghdw1=record.get('GONGYSB_ID');\n"+
//				" var mm; \n"+
//				" if (gonghdw1=='-') return 'continue';\n " +
//				" if (gonghdw1.length>6){mm=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);\n}" +
//				" if(mm==''|| mm=='С��') return 'continue';}\n");
//		
//		egu.addOtherScript(sb.toString());
		
//		�����
//		StringBuffer sb2 = new StringBuffer();
//		sb2.append("gridDiv_grid.on('afteredit', function(e){");
//		sb2.append("if(e.field == 'REZ'){e.record.set('REZ_DK',parseFloat(e.value)/0.0041486);}");
//		sb2.append("});");
//		egu.addOtherScript(sb2.toString());
		// ---------------ҳ��js�������--------------------------
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

	private long flage = 0;

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
			this.setLeixDropDownValue(null);
			this.getLeixDropDownModels();
			this.setZhuangtDropDownValue(null);
			this.getZhuangtDropDownModels();
			visit.setString1(null);
			visit.setString3(null);

			JDBCcon con = new JDBCcon();
			String sql = "select zhi from xitxxb where mingc = '�´�����' and diancxxb_id="
					+ visit.getDiancxxb_id();
			ResultSet rl = con.getResultSet(sql);
			try {
				if (rl.next()) {// ȼ�Ϲ�˾
					this.flage = rl.getLong("zhi");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
		}

		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc = '�´�����' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSet rl = con.getResultSet(sql);
		try {
			if (rl.next()) {// ȼ�Ϲ�˾
				this.flage = rl.getLong("zhi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		getSelectData(null);

	}

	// ���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	// �·�
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// �õ��糧ID
	public String getIDropDownDiancid(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancid = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.id from diancxxb d where d.mingc='"
				+ diancmcId + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancid = rs.getString("id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// �õ��糧����
	public String getIDropDownDiancbm(String diancmc) {
		if (diancmc == null || diancmc.equals("")) {
			diancmc = "1";
		}
		String IDropDownDiancid = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select bianm from diancxxb d where d.mingc='"
				+ diancmc + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancid = rs.getString("bianm");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// ������λ����
	public String getIDropDownGonghdwbm(String gonghdwmc) {
		if (gonghdwmc == null || gonghdwmc.equals("")) {
			gonghdwmc = "1";
		}
		String IDropDownGonghdwbm = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "  select bianm from gongysb  where mingc ='"+ gonghdwmc + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownGonghdwbm = rs.getString("bianm");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownGonghdwbm;

	}

	// �õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
		 
		 StringBuffer sql = new StringBuffer();
		sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

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

	// ����
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getLeixDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "���ۻ���"));
		list.add(new IDropDownBean(1, "��������"));
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(list));
		return;
	}

	// ����״̬--��ʱ����
	public IDropDownBean getZhuangtDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getZhuangtDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setZhuangtDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setZhuangtDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getZhuangtDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getZhuangtDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getZhuangtDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "δ�´�"));
		list.add(new IDropDownBean(1, "���´�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(list));
		return;
	}
}