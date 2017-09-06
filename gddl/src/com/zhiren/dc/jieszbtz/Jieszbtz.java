package com.zhiren.dc.jieszbtz;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dc.hesgl.jiesd.Balances;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jieszbtz extends BasePage implements PageValidateListener {
	private String msg = "";
	boolean shishowDak=false;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RoolbackChick = false;

	public void RoolbackButton(IRequestCycle cycle) {
		_RoolbackChick = true;
	}

	private boolean _SubmitChick = false;

	public void SubmitButton(IRequestCycle cycle) {
		_SubmitChick = true;
	}

	public void Submit() {

	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}

	private void Save(IRequestCycle cycle) {

		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit, cycle);
	}

	private String SaveXitrzb() {
		// дϵͳ��־�������Ҫ���� ָ���޸�ʱҪ�ȼ�¼
		String sql = "insert into xitrzb\n"
				+ "  (id, diancxxb_id, yonghm, leib, shij, zhuangt, beiz)\n"
				+ "values\n" + "  (getnewid("
				+ ((Visit) getPage().getVisit()).getLong1() + "), "
				+ ((Visit) getPage().getVisit()).getLong1() + ", '"
				+ ((Visit) getPage().getVisit()).getRenymc() + "','����ָ�����', "
				+ "sysdate, '����', '"
				+ getTiaozbz(((Visit) getPage().getVisit()).getString1())
				+ "');\n";

		return sql;
	}

	private String getTiaozbz(String lie_id) {
		String beiz = "";
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select '����ʱ�����˴�'||to_char(min(daohrq),'yyyy-MM-dd')||'��'||\n"
					+ "to_char(max(daohrq),'yyyy-MM-dd')||','||max(m.mingc)||'�Ľ���ָ��' as beiz \n"
					+ "from fahb f,meikxxb m\n"
					+ "where f.meikxxb_id=m.id\n"
					+ "and lie_id in (" + lie_id + ")";

			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				beiz = rs.getString("beiz");
			}
			rs.close();

		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return beiz;
	}

	public void Save1(String strchange, Visit visit, IRequestCycle cycle) {
		String tableName = "jieszbsjb";
		JDBCcon con = new JDBCcon();
		String str_id = "0"; // yansbhb_id
		boolean insertFlag = false;
		
		try {

			StringBuffer sql = new StringBuffer("begin \n");
			sql.append(SaveXitrzb());

			String sql1 = "";
			String sql2 = "";
			ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
					strchange);
			// �������Ͳ�����ô����Ҫɾ����Ӧ�����ձ��
			sql2 = "select distinct yansbhb_id from fahb where lie_id in ("
					+ ((Visit) getPage().getVisit()).getString1() + ")";
			ResultSetList rsl2 = con.getResultSetList(sql2);
			sql1 = "select id from yansbhb where id in (select distinct yansbhb_id from fahb where lie_id in ("
					+ ((Visit) getPage().getVisit()).getString1()
					+ ")) and jiesdid=0";
			if (rsl2.getRows() > 1
					|| ((Visit) getPage().getVisit()).getboolean2()) {
				ResultSetList rsl1 = con.getResultSetList(sql1);
				while (rsl1.next()) {
					sql.append("delete from yansbhb where id="
							+ rsl1.getString("ID") + ";\n");
					sql.append("update fahb set yansbhb_id=0 where yansbhb_id="
							+ rsl1.getString("ID") + ";\n");
				}
				rsl1.close();
			}
			rsl2.close();

			while (mdrsl.next()) {

				if ("0".equals(mdrsl.getString("ID"))) {

					if (!insertFlag) {

						str_id = MainGlobal.getNewID(((Visit) getPage()
								.getVisit()).getLong1());
						sql
								.append("insert into yansbhb ")
								.append("(id, bianm,lie_id)\n")
								.append("values	\n")
								.append("(")
								.append(str_id)
								.append(",")
								.append("'")
								.append(
										((Visit) getPage().getVisit())
												.getString4())
								.append(
										"','"
												+ ((Visit) getPage().getVisit())
														.getString1()
												+ "');	\n");

						insertFlag = true;
					}

					
					if(shishowDak){//����ָ���Դ���ʾ,�����ʱ����Ҫת�����׽�����
						if("Qnetar".equals(mdrsl.getString("ZHIBB_ID"))){
							sql
							.append("insert into ")
							.append(tableName)
							.append(
									"(id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n")
							.append("values \n").append("(getnewid(").append(
									((Visit) getPage().getVisit()).getLong1())
							.append("),").append("0").append(",").append(
									this.getProperId(this.getIZhibModel(),
											Jiesdcz.Sub_danw(mdrsl
													.getString("ZHIBB_ID"))))
							.append(",").append("round("+Double.valueOf(mdrsl.getString("GONGF"))+"/1000*4.1816,3)")
							.append(",").append("round("+Double.valueOf(mdrsl.getString("CHANGF"))+"/1000*4.1816,3)")
							.append(",").append("round("+Double.valueOf(mdrsl.getString("JIES"))+"/1000*4.1816,3)")
							.append(",").append("0").append(",").append("0")
							.append(",").append("0").append(",").append("1")
							.append(",").append(str_id).append("); \n");
						}else{
							sql
							.append("insert into ")
							.append(tableName)
							.append(
									"(id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n")
							.append("values \n").append("(getnewid(").append(
									((Visit) getPage().getVisit()).getLong1())
							.append("),").append("0").append(",").append(
									this.getProperId(this.getIZhibModel(),
											Jiesdcz.Sub_danw(mdrsl
													.getString("ZHIBB_ID"))))
							.append(",").append(mdrsl.getString("GONGF"))
							.append(",").append(mdrsl.getString("CHANGF"))
							.append(",").append(mdrsl.getString("JIES"))
							.append(",").append("0").append(",").append("0")
							.append(",").append("0").append(",").append("1")
							.append(",").append(str_id).append("); \n");
						}
					
					}else{
						sql
						.append("insert into ")
						.append(tableName)
						.append(
								"(id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n")
						.append("values \n").append("(getnewid(").append(
								((Visit) getPage().getVisit()).getLong1())
						.append("),").append("0").append(",").append(
								this.getProperId(this.getIZhibModel(),
										Jiesdcz.Sub_danw(mdrsl
												.getString("ZHIBB_ID"))))
						.append(",").append(mdrsl.getString("GONGF"))
						.append(",").append(mdrsl.getString("CHANGF"))
						.append(",").append(mdrsl.getString("JIES"))
						.append(",").append("0").append(",").append("0")
						.append(",").append("0").append(",").append("1")
						.append(",").append(str_id).append("); \n");
					}
					

				} else {
					
					if(shishowDak){
						if("Qnetar".equals(mdrsl.getString("ZHIBB_ID"))){
							sql.append("update ").append(tableName).append(" set ")
							.append("GONGF =").append("round("+Double.valueOf(mdrsl.getString("GONGF"))+"/1000*4.1816,3)")
							.append(",").append("CHANGF =").append(
									"round("+Double.valueOf(mdrsl.getString("CHANGF"))+"/1000*4.1816,3)").append(",")
							.append("JIES =").append("round("+Double.valueOf(mdrsl.getString("JIES"))+"/1000*4.1816,3)")
							.append(" where id =")
							.append(mdrsl.getString("ID")).append(";\n");
						}else{
							sql.append("update ").append(tableName).append(" set ")
							.append("GONGF =").append(Double.valueOf(mdrsl.getString("GONGF")))
							.append(",").append("CHANGF =").append(
									Double.valueOf(mdrsl.getString("CHANGF"))).append(",")
							.append("JIES =").append(Double.valueOf(mdrsl.getString("JIES")))
							.append(" where id =")
							.append(mdrsl.getString("ID")).append(";\n");
						}
					
					}else{
						sql.append("update ").append(tableName).append(" set ")
						.append("GONGF =").append(Double.valueOf(mdrsl.getString("GONGF")))
						.append(",").append("CHANGF =").append(
								Double.valueOf(mdrsl.getString("CHANGF"))).append(",")
						.append("JIES =").append(Double.valueOf(mdrsl.getString("JIES")))
						.append(" where id =")
						.append(mdrsl.getString("ID")).append(";\n");
					}
					
					
					
				}
			}

			if (insertFlag) {
				// �����Insert�͸���fahb��yansbhb_id
				sql.append("update fahb set yansbhb_id=").append(str_id)
						.append(" where lie_id in (").append(
								((Visit) getPage().getVisit()).getString1())
						.append(");	\n");
			}
			//			

			sql.append("end;");
			mdrsl.close();

			if (sql.length() > 13) {

				if (con.getUpdate(sql.toString()) >= 0) {

					// this.setMsg("����ɹ���");
					cycle.activate("DCBalance");
				} else {

					this.setMsg("����ʧ�ܣ�");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DelChick = false;

	public void DelButton(IRequestCycle cycle) {

		_DelChick = true;
		((Visit) getPage().getVisit()).setboolean1(_DelChick);
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save(cycle);
			// getSelectData();
		}
		if (_SubmitChick) {
			_SubmitChick = false;

		}
		if (_DelChick) {
			// _DelChick=false;

			// ˢ��ҳ��
			this.getSelectData();
		}
		if (_RoolbackChick) {
			_RoolbackChick = false;
			// ����
			Roolback(cycle);
		}
	}

	private String chongxjs() {
		
		
		// Ҫ�²�������
		Balances bal = new Balances();
		Balances_variable bsv = new Balances_variable();
		// �õ�����ԭʼ����
		String sql = "";

		JDBCcon con = new JDBCcon();
		String sq="select * from xitxxb x where x.mingc='�����ͬר��'";
		ResultSetList rrr = con.getResultSetList(sq);
		//������ʾ��ʱ��,��ͬ�糧���׽�ת��Ϊ����ʾ
		if(rrr.next()){
			//��ͬ�糧������ʾ��ʱ���ô���ʾ
			sql = " select id,rownum as xuh,zhibb_id,changf,gongf,jies 	\n"
				+ " from ("
				+ "	select js.id,rownum as xuh,z.bianm as zhibb_id,\n"
                +"            decode(z.bianm,'Qnetar',round(js.changf*1000/4.1816,0),js.changf) as changf ,\n" 
				+"            decode(z.bianm,'Qnetar',round(js.gongf*1000/4.1816,0),js.gongf) as gongf ,\n" 
				+"            decode(z.bianm,'Qnetar',round(js.changf*1000/4.1816,0),js.changf) as jies"
				+ "             from jieszbsjb js,zhibb z\n"
				+ "             where js.zhibb_id=z.id\n"
				+ "                   and yansbhb_id in\n"
				+ "                   (select distinct yansbhb_id from fahb\n"
				+ "                           where lie_id in ("
				+ ((Visit) getPage().getVisit()).getString1()
				+ ")) order by js.id) ";
		}else{
		
		// ���㷽��_begin
		bsv = bal.getJiesszl("��", ((Visit) getPage().getVisit()).getString1(),
				((Visit) getPage().getVisit()).getLong1(), ((Visit) getPage()
						.getVisit()).getLong3(), ((Visit) getPage().getVisit())
						.getLong8(), Double.parseDouble(((Visit) getPage()
						.getVisit()).getString7()), ((Visit) getPage()
						.getVisit()).getLong9(), ((Visit) getPage().getVisit())
						.getLong2(), 0, "");

		sql = " select * from \n (select 0 as id,1 as xuh,'"
				+ Locale.jiessl_zhibb + "(" + Locale.dun_danw
				+ ")' as zhibb_id," + bsv.getYanssl() + "  as changf,"
				+ bsv.getGongfsl() + " as gongf," + bsv.getJiessl()
				+ " as jies" + " from dual 	\n" + " union \n"
				+ " select 0 as id,2 as xuh,'" + Locale.Qnetar_zhibb + "("
				+ Locale.zhaojmqk_fh_danw + ")' as zhibb_id,"
				+ bsv.getQnetar_cf() + " as changf," + bsv.getQnetar_kf()
				+ " as gongf," + bsv.getQnetar_js() + " as jies"
				+ " from dual 	\n" + " union \n" + " select 0 as id,6 as xuh,'"
				+ Locale.Qgrad_zhibb + "(" + Locale.zhaojmqk_fh_danw
				+ ")' as zhibb_id," + bsv.getQgrad_cf() + " as changf,"
				+ bsv.getQgrad_kf() + " as gongf," + bsv.getQgrad_js()
				+ " as jies" + " from dual	\n" + " union \n"
				+ " select 0 as id,3 as xuh,'" + Locale.Std_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getStd_cf()
				+ " as changf," + bsv.getStd_kf() + " as gongf,"
				+ bsv.getStd_js() + "  as jies" + " from dual	\n" + " union \n"
				+ " select 0 as id,5 as xuh,'" + Locale.Stad_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getStad_cf()
				+ " as changf," + bsv.getStad_kf() + " as gongf,"
				+ bsv.getStad_js() + " as jies" + " from dual" + " union \n"
				+ " select 0 as id,4 as xuh,'" + Locale.Star_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getStar_cf()
				+ " as changf," + bsv.getStar_kf() + " as gongf,"
				+ bsv.getStar_js() + " as jies" + " from dual" + " union \n"
				+ " select 0 as id,8 as xuh,'" + Locale.Mt_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getMt_cf()
				+ " as changf," + bsv.getMt_kf() + " as gongf,"
				+ bsv.getMt_js() + " as jies" + " from dual \n " + " union \n"
				+ " select 0 as id,10 as xuh,'" + Locale.Vdaf_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getVdaf_cf()
				+ " as changf," + bsv.getVdaf_kf() + " as gongf,"
				+ bsv.getVdaf_js() + " as jies" + " from dual \n" + "union \n"
				+ " select 0 as id,12 as xuh,'" + Locale.Ad_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getAd_cf()
				+ " as changf," + bsv.getAd_kf() + " as gongf,"
				+ bsv.getAd_js() + " as jies" + " from dual \n" + "union	\n"
				+ " select 0 as id,7 as xuh,'" + Locale.Qbad_zhibb + "("
				+ Locale.zhaojmqk_fh_danw + ")' as zhibb_id,"
				+ bsv.getQbad_cf() + " as changf," + bsv.getQbad_kf()
				+ " as gongf," + bsv.getQbad_js() + " as jies"
				+ " from dual \n" + "	union	\n" + " select 0 as id,15 as xuh,'"
				+ Locale.Had_zhibb + "(" + Locale.baifb_danw
				+ ")' as zhibb_id," + bsv.getHad_cf() + " as changf,"
				+ bsv.getHad_kf() + " as gongf," + bsv.getHad_js() + " as jies"
				+ " from dual \n" + " union	\n" + " select 0 as id,13 as xuh,'"
				+ Locale.Aar_zhibb + "(" + Locale.baifb_danw
				+ ")' as zhibb_id," + bsv.getAar_cf() + " as changf,"
				+ bsv.getAar_kf() + " as gongf," + bsv.getAar_js() + " as jies"
				+ " from dual \n" + " union	\n" + " select 0 as id,11 as xuh,'"
				+ Locale.Vad_zhibb + "(" + Locale.baifb_danw
				+ ")' as zhibb_id," + bsv.getVad_cf() + " as changf,"
				+ bsv.getVad_kf() + " as gongf," + bsv.getVad_js() + " as jies"
				+ " from dual \n" + "	union	\n" + " select 0 as id,16 as xuh,'"
				+ Locale.T2_zhibb + "(" + Locale.shesd_danw + ")' as zhibb_id,"
				+ bsv.getT2_cf() + " as changf," + bsv.getT2_kf()
				+ " as gongf," + bsv.getT2_js() + " as jies" + " from dual \n"
				+ " union	\n" + " select 0 as id,9 as xuh,'" + Locale.Mad_zhibb
				+ "(" + Locale.baifb_danw + ")' as zhibb_id," + bsv.getMad_cf()
				+ " as changf," + bsv.getMad_kf() + " as gongf,"
				+ bsv.getMad_js() + " as jies" + " from dual \n" + "	union	\n"
				+ " select 0 as id,14 as xuh,'" + Locale.Aad_zhibb + "("
				+ Locale.baifb_danw + ")' as zhibb_id," + bsv.getAad_cf()
				+ " as changf," + bsv.getAad_kf() + " as gongf,"
				+ bsv.getAad_js() + " as jies" + " from dual "
				+ ") order by xuh"
		// ����_end
		;
		}
		return sql;
	}

	private void Roolback(IRequestCycle cycle) {

		cycle.activate("Jiesxz");
	}

	public String du(String colName, String TableName, String a)
			throws SQLException, IOException {
		StringBuffer strClob = new StringBuffer();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "select " + colName + " from " + TableName + " where id="
				+ a;
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(colName);
			if (clob == null) {
				return "";
			}
			BufferedReader br = new BufferedReader(clob.getCharacterStream());
			String out = br.readLine();
			while (out != null) {
				strClob.append(out);
				strClob.append("\n");
				out = br.readLine();
			}
			br.close();
		}
		rs.close();
		con.commit();
		con.Close();
		return strClob.toString();
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		ResultSetList rsl = null;
		boolean Insert_Flag = true;
		String yansbhb = "yansbhb";
		String lie_id = "lie_id";
		String[] sq = ((Visit) getPage().getVisit()).getString1().split(",");

		try {
			sql1 = "select distinct yansbhb_id from fahb where lie_id in ("
					+ ((Visit) getPage().getVisit()).getString1() + ")";
			ResultSetList rsl1 = con.getResultSetList(sql1);
			if (rsl1.getRows() == 1) {// ���ڵ���1˵�������ݣ���ô�͸���
				if (rsl1.next()) {
					sql1 = rsl1.getString("yansbhb_id");
					// if (!sql1.equals("")) {
					// Insert_Flag = false;
					// }
					if (!"".equals(sql1)) {

						String ss = this.du(lie_id, yansbhb, sql1);
						String[] as = ss.split(",");
						// ����lie_id�����ձ�ű��е�һһ��Ӧ��jiesdid��Ϊ0
						// �����ͬһ�飬�Ͳ���Ҫ����
						int count = 0;
						as[as.length - 1] = as[as.length - 1].replaceAll("\n",
								"");

						sql2 = "select jiesdid  from yansbhb where id =" + sql1;
						rsl1 = con.getResultSetList(sql2);
						if (rsl1.next()) {
							if (!"0".equals(rsl1.getString("JIESDID"))) {// 1��ʾ����֤����ô�½�
								Insert_Flag = true;
							} else {
								Insert_Flag = false;
							}
						}

						if (as.length == sq.length) {//������ȣ����ǿ���Ԫ�ز�ͬ

							for (int i = 0; i < as.length - 1; i++) {
								for (int j = 0; j < as.length - 1; j++) {

									if (as[i].equals(sq[j])) {
										count++;
									}

								}
								if (count == 0) {//Ԫ�ز�ͬ
									Insert_Flag = true;
									((Visit) getPage().getVisit())
											.setboolean2(true);
									break;
								}
							}
						} else {
							((Visit) getPage().getVisit()).setboolean2(true);
							Insert_Flag = true;
						}
					} else {
						Insert_Flag = true;
					}

				}
			} else {
				((Visit) getPage().getVisit()).setboolean2(true);
				sql1 = "";
			}
			// �жϽ���ָ���Ƿ����
			String _jieszbsftz = Jiesdcz.getJiessz_item(((Visit) getPage()
					.getVisit()).getLong1(), ((Visit) getPage().getVisit())
					.getLong3(), ((Visit) getPage().getVisit()).getLong8(),
					Locale.jieszbtz_jies, "��");
			if (Insert_Flag || ((Visit) getPage().getVisit()).getboolean1()) {

				sql = chongxjs();

			} else {
				
				if(shishowDak){
//					�����Դ�Ϊ��λ��ʾ
					sql = " select id,rownum as xuh,zhibb_id,changf,gongf,jies 	\n"
							+ " from ("
							+ "	select js.id,rownum as xuh,z.bianm as zhibb_id,\n"
							+ "         decode(z.bianm,'Qnetar',round(js.changf*1000/4.1816,0),js.changf) as changf,\n" 
							+"          decode(z.bianm,'Qnetar',round(js.gongf*1000/4.1816,0),js.gongf) as gongf ,\n" 
							+"         decode(z.bianm,'Qnetar',round(js.changf*1000/4.1816,0),js.changf) as jies\n"
							+ "             from jieszbsjb js,zhibb z\n"
							+ "             where js.zhibb_id=z.id\n"
							+ "                   and yansbhb_id in\n"
							+ "                   (select distinct yansbhb_id from fahb\n"
							+ "                           where lie_id in ("
							+ ((Visit) getPage().getVisit()).getString1()
							+ ")) order by js.id) ";

					((Visit) this.getPage().getVisit()).setString4(MainGlobal
							.getTableCol("fahb f,yansbhb y", "distinct y.bianm",
									"f.yansbhb_id=y.id and f.lie_id in ("
											+ ((Visit) getPage().getVisit())
													.getString1() + ")"));
				}else{
                     //	�������׽���ʾ
					sql = " select id,rownum as xuh,zhibb_id,changf,gongf,jies 	\n"
							+ " from ("
							+ "	select js.id,rownum as xuh,z.bianm as zhibb_id,\n"
							+ "                    js.changf,js.gongf,js.changf as jies\n"
							+ "             from jieszbsjb js,zhibb z\n"
							+ "             where js.zhibb_id=z.id\n"
							+ "                   and yansbhb_id in\n"
							+ "                   (select distinct yansbhb_id from fahb\n"
							+ "                           where lie_id in ("
							+ ((Visit) getPage().getVisit()).getString1()
							+ ")) order by js.id) ";

					((Visit) this.getPage().getVisit()).setString4(MainGlobal
							.getTableCol("fahb f,yansbhb y", "distinct y.bianm",
									"f.yansbhb_id=y.id and f.lie_id in ("
											+ ((Visit) getPage().getVisit())
													.getString1() + ")"));
				}
				
			}

			rsl = con.getResultSetList(sql);
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

			egu.setTableName("jieszbsjb");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("xuh").setHeader("���");
			egu.getColumn("zhibb_id").setHeader("����ָ��");
			egu.getColumn("changf").setHeader("����");
			egu.getColumn("gongf").setHeader("��");
			egu.getColumn("jies").setHeader("����");
			egu.getColumn("id").setEditor(null);
			egu.getColumn("xuh").setEditor(null);
			egu.getColumn("zhibb_id").setEditor(null);
//			egu.getColumn("changf").setEditor(null);
			egu.getColumn("xuh").setWidth(50);
			((NumberField) egu.getColumn("jies").editor).setDecimalPrecision(3);// ����3λС��
			// egu.getColumn("gongf").setEditor(null);

			egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
			egu.addPaging(0);// ���÷�ҳ
			egu.setWidth(Locale.Grid_DefaultWidth);// ����ҳ��Ŀ��,������������ʱ��ʾ������
			if (((Visit) this.getPage().getVisit()).getString4() == null
					|| ((Visit) this.getPage().getVisit()).getString4().equals(
							"")) {
				String Ys = MainGlobal.getYansbh();
				egu.addTbarText("���ձ��:" + Ys);

				((Visit) this.getPage().getVisit()).setString4(Ys);
			} else {

				egu.addTbarText("���ձ��:"
						+ ((Visit) this.getPage().getVisit()).getString4());
			}

			egu
					.addToolbarItem("{"
							+ new GridButton(
									"����",
									"function(){document.all.item('RoolbackButton').click()}",
									SysConstant.Btn_Icon_Return).getScript()
							+ "}");
			// egu
			// .addToolbarItem("{"
			// + new GridButton("ȷ��",
			// "function(){document.all.item('SubmitButton').click()}")
			// .getScript() + "}");
			egu.addToolbarButton("ȷ��",GridButton.ButtonType_SaveAll, "SaveButton");

//			egu.addTbarBtn(new GridButton("�Կ�Ϊ׼","function(){CountKuangf(gridDiv_grid);this.setDisabled(false);}") );
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("����ƽ��ֵ","function(){CountShc(gridDiv_grid);this.setDisabled(false);}") );
			
			if (_jieszbsftz.equals("��")) {
				// �������ָ��ȡ�󷽽���ָ�꣬��ô������󷽽���ָ��ʱ������ָ���Զ������޸�
				_jieszbsftz = "gridDiv_grid.on('afteredit',countJies);\n"
						+ "\n"
						+ "function countJies(e){\n"
						+ "\trec = e.record;\n"
						+ "\tchangeValue = parseFloat(e.value - e.originalValue);\n"
						+ "\tif(e.field=='GONGF'){\n" + "\n"
						+ "\t\trec.set('JIES',eval(rec.get('GONGF')));\n"
						+ "\t}\n" + "}";
				egu.addOtherScript(_jieszbsftz);
			}

			setExtGrid(egu);
			rsl.close();

		} catch (NumberFormatException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {

			con.Close();
		}
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

	public IPropertySelectionModel getIZhibModels() {

		String sql = "select id,bianm from zhibb ";
		((Visit) this.getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIZhibModel() {

		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {

			getIZhibModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());

			// ((Visit) getPage().getVisit()).getLong1() //�糧��Ϣ��id
			// ((Visit) getPage().getVisit()).getString1() //��Id
			// ((Visit) getPage().getVisit()).getLong2() //��������
			// ((Visit) getPage().getVisit()).getString2() //�Ƿ���Ҫ����ָ��������ǡ���
			// ((Visit) getPage().getVisit()).getString4() //���ձ��
			// ((Visit) getPage().getVisit()).getLong3() //������λ��id
			// ((Visit) getPage().getVisit()).getString7() //����۶���
			// visit.setLong4(0); //ú����Ϣ��id
			// visit.setLong5(0); //��վ
			// visit.setLong6(0); //��վ
			// visit.setLong7(0); //�����˷ѱ�id
			// visit.setLong8(0); //��ͬ��id
			// visit.setLong9(0); //���䵥λid
			// visit.setDouble1(0); //��Ʊ��
			visit.setboolean1(false);
			visit.setboolean2(false);
			 shishowDak="��".equals(MainGlobal.getXitxx_item("����", "����ָ������Ƿ��Դ���ʾ", visit.getDiancxxb_id(), "��"));
			visit.setList1(null);
			// ָ��
			visit.setProSelectionModel1(null);
			getIZhibModels();
			// visit.setString4("");
		}

		getSelectData();
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

	public static String getNewID(long diancxxb_id) {
		JDBCcon con = new JDBCcon();
		String id = "";
		ResultSetList rs = con
				.getResultSetList("select xl_xul_id.nextval id from dual");
		if (rs.next()) {
			id = rs.getString(0);
		}
		return diancxxb_id + id;
	}
}
