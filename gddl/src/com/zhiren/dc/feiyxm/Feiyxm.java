package com.zhiren.dc.feiyxm;

import java.util.ArrayList;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
 * �޸����ݣ�1���޸�Save1(String strchange, Visit visit)�����Ĳ������ݣ���ά���糧������3�ĵ糧����ʱ����Ӧ�õ糧�������糧������ҲҪͬʱ���Ÿ��ġ�
 *    2���޸ķ�������������ͳ����������SQL��ѯ��䡣
 * �޸�ʱ�䣺2009-09-16
 * �޸��ˣ�������
 */

public class Feiyxm extends BasePage implements PageValidateListener {

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

	private String Parameters;// ��¼������ĿID���������

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private String Feiylb;

	public String getFeiylb() {
		return Feiylb;
	}

	public void setFeiylb(String chan) {
		Feiylb = chan;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	public void Save1(String strchange, Visit visit) {
		
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = getExtGrid();
		long mlngDiancxxb_id = 0;
		String mstrMeikxxb_id = "0";
		long mlngFeiylbb_id = 0;
		String mstrFeiyxmb_id = "";
		String mstrFeiyglb_id = "";
		try {

			if (visit.getPagePreferences().equals("Huopd") || visit.getPagePreferences().equals("Kuangqzf")) {
							
				if (visit.isFencb()) {
					// Huopd���÷ֳ���
					mlngDiancxxb_id = visit.getLong1();
				} else {
					mlngDiancxxb_id = visit.getDiancxxb_id();
				}

				StringBuffer sql = new StringBuffer("begin \n");
				StringBuffer sql3 = new StringBuffer();
				StringBuffer sql5 = new StringBuffer();

				String tableName = "feiyxmb";
				String tableName2 = "feiyglb";
				String tableName3 = "feiyxmmkglb";

				ResultSetList delrsl = egu.getDeleteResultSet(strchange);
				while (delrsl.next()) {
					sql.append(" delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";	\n");
					sql.append(" delete from ").append(tableName2).append(" where id in ( select feiyglb_id as id from feiyxmmkglb where feiyxmb_id=")
					.append(delrsl.getString(0)).append(");	\n");
					sql.append(" delete from ").append(tableName3).append(" where feiyxmb_id =").append(delrsl.getString(0)).append(";	\n");
				}

				ResultSetList mdrsl = egu.getModifyResultSet(strchange);
				while (mdrsl.next()) {

					StringBuffer sql2 = new StringBuffer();
					StringBuffer sql4 = new StringBuffer();
					StringBuffer sql6 = new StringBuffer();

					if ("0".equals(mdrsl.getString("ID"))) {

						mstrFeiyxmb_id = MainGlobal.getNewID(mlngDiancxxb_id);
						mstrFeiyglb_id = MainGlobal.getNewID(mlngDiancxxb_id);

						sql2.append(mstrFeiyxmb_id).append("," + mlngDiancxxb_id + "");
						sql4.append(mstrFeiyglb_id).append("," + mlngDiancxxb_id + "");
						sql6.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

						sql.append("insert into ").append(tableName).append("(id,diancxxb_id");
						sql3.append("insert into ").append(tableName2).append("(id,diancxxb_id");
						sql5.append("insert into ").append(tableName3).append("(id,feiyglb_id,feiyxmb_id,shifsy");

						for (int i = 1; i < mdrsl.getColumnCount(); i++) {

							if (mdrsl.getColumnNames()[i].equals("FEIYMCB_ID")|| mdrsl.getColumnNames()[i].equals("GONGS")
									|| mdrsl.getColumnNames()[i].equals("SHUIB")
									|| mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")
									|| mdrsl.getColumnNames()[i].equals("JUFLX")
									|| mdrsl.getColumnNames()[i].equals("SHUIL")) {

								sql.append(",").append(mdrsl.getColumnNames()[i]);

								if (mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")) {
									sql2.append(",").append(
											MainGlobal.getProperId(this
													.getIFeiylxModel(), mdrsl
													.getString(i)));
								} else {

									sql2.append(",").append(
											egu.getValueSql(egu.getColumn(mdrsl
													.getColumnNames()[i]),
													mdrsl.getString(i)));
								}
							}

							if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("FEIYLBB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("SHOUKDWB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("MEIKYFXTGYS")) {

								sql3.append(",").append(
										mdrsl.getColumnNames()[i]);

								if (mdrsl.getColumnNames()[i]
										.equals("MEIKXXB_ID")) {

									mstrMeikxxb_id = String.valueOf(MainGlobal.getProperId(
											this.getIMeikdwmcModel(), mdrsl
													.getString(i)));
									sql4.append(",").append(mstrMeikxxb_id);
								} else if (mdrsl.getColumnNames()[i]
										.equals("FEIYLBB_ID")) {

									mlngFeiylbb_id = MainGlobal.getProperId(
											this.getIFeiylxModel(), mdrsl
													.getString(i));
									sql4.append(",").append(mlngFeiylbb_id);
								} else {

									sql4.append(",").append(
											egu.getValueSql(egu.getColumn(mdrsl
													.getColumnNames()[i]),
													mdrsl.getString(i)));
								}
							}

							if (mdrsl.getColumnNames()[i].equals("SHIFSY")) {

								sql6
										.append(",")
										.append(mstrFeiyglb_id)
										.append(",")
										.append(mstrFeiyxmb_id)
										.append(",")
										.append(
												egu
														.getValueSql(
																egu
																		.getColumn(mdrsl
																				.getColumnNames()[i]),
																mdrsl
																		.getString(i)));
							}
						}

						sql.append(") values(").append(sql2).append(");\n");
						sql3.append(") values(").append(sql4).append(");\n");
						sql5.append(") values(").append(sql6).append(");\n");

					} else {

						sql.append("  update ").append(tableName).append(
								" set ");
						sql3.append(" update ").append(tableName2).append(
								" set ");
						sql5.append(" update ").append(tableName3).append(
								" set ");

						for (int i = 1; i < mdrsl.getColumnCount(); i++) {

							if (mdrsl.getColumnNames()[i].equals("FEIYMCB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("GONGS")
									|| mdrsl.getColumnNames()[i]
											.equals("SHUIB")
									|| mdrsl.getColumnNames()[i]
											.equals("FEIYLBB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("JUFLX")
									|| mdrsl.getColumnNames()[i]
											.equals("SHUIL")) {

								sql.append(mdrsl.getColumnNames()[i]).append(
										" = ");

								if (mdrsl.getColumnNames()[i]
										.equals("FEIYLBB_ID")) {

									mlngFeiylbb_id = MainGlobal.getProperId(
											this.getIFeiylxModel(), mdrsl
													.getString(i));
									sql.append(mlngFeiylbb_id).append(",");
								} else {

									sql.append(
											egu.getValueSql(egu.getColumn(mdrsl
													.getColumnNames()[i]),
													mdrsl.getString(i)))
											.append(",");
								}
							}

							if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("FEIYLBB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("SHOUKDWB_ID")
									|| mdrsl.getColumnNames()[i]
											.equals("MEIKYFXTGYS")) {

								sql3.append(mdrsl.getColumnNames()[i]).append(
										" = ");

								if (mdrsl.getColumnNames()[i]
										.equals("MEIKXXB_ID")) {

									mstrMeikxxb_id = String.valueOf(MainGlobal.getProperId(
											this.getIMeikdwmcModel(), mdrsl
													.getString(i)));
									;
									sql3.append(mstrMeikxxb_id).append(",");

								} else if (mdrsl.getColumnNames()[i]
										.equals("FEIYLBB_ID")) {

									mlngFeiylbb_id = MainGlobal.getProperId(
											this.getIFeiylxModel(), mdrsl
													.getString(i));
									;
									sql3.append(mlngFeiylbb_id).append(",");

								} else {

									sql3.append(
											egu.getValueSql(egu.getColumn(mdrsl
													.getColumnNames()[i]),
													mdrsl.getString(i)))
											.append(",");
								}
							}
							if (mdrsl.getColumnNames()[i].equals("SHIFSY")) {

								sql5.append(mdrsl.getColumnNames()[i]).append(
										" = ");
								sql5.append(
										egu.getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i))).append(",");
							}
						}
						sql.deleteCharAt(sql.length() - 1);
						sql3.deleteCharAt(sql3.length() - 1);
						sql5.deleteCharAt(sql5.length() - 1);

						sql.append(" where id =").append(mdrsl.getString("ID"))
								.append(";\n");
						sql3.append(" where id =")
								.append(mdrsl.getString("ID")).append(";\n");
						sql5.append(" where id =")
								.append(mdrsl.getString("ID")).append(";\n");
					}
				}
				sql.append(sql3).append(sql5);
				sql.append("end;");
				con.getUpdate(sql.toString());
				getSelectData(mlngDiancxxb_id, mstrMeikxxb_id, mlngFeiylbb_id);
			} else {
//				��ά���糧������3�ĵ糧����ʱ����Ӧ�õ糧�������糧������ҲҪͬʱ���Ÿ��ġ�
				StringBuffer sql = new StringBuffer("begin \n");
				String tableName = "feiyxmb";

				ResultSetList delrsl = egu.getDeleteResultSet(strchange);
				
				while (delrsl.next()) {
					// ɾ������
					sql.append(" delete from ").append(tableName).append(" where id =").append(delrsl.getString("id")).append(";\n");
				}
				delrsl.close();
				
				ResultSetList mdrsl = egu.getModifyResultSet(strchange);
				while (mdrsl.next()) {

					if ("0".equals(mdrsl.getString("ID"))) {
						// �������
							StringBuffer strSb = new StringBuffer();
							strSb.append("getnewid(").append(getChangbValue().getId()).append("), ").append(getChangbValue().getId());
							sql.append("insert into ").append(tableName).append("(id,diancxxb_id");

							for (int i = 1; i < mdrsl.getColumnCount(); i++) {
								if (mdrsl.getColumnNames()[i].equals("FEIYMCB_ID")
									|| mdrsl.getColumnNames()[i].equals("GONGS")
									|| mdrsl.getColumnNames()[i].equals("SHUIB")
									|| mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")
									|| mdrsl.getColumnNames()[i].equals("JUFLX")
									|| mdrsl.getColumnNames()[i].equals("SHUIL")) {
									
									sql.append(",").append(mdrsl.getColumnNames()[i]);
									if (mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")) {
										strSb.append(",").append(MainGlobal.getProperId(this.getIFeiylxModel(), mdrsl.getString(i)));
									} else {
										strSb.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
									}
								}
							}
							sql.append(") values(").append(strSb).append(");\n");
					} else {
						// ���²���
						
						sql.append(" update ").append(tableName).append(" set ");
						for (int i = 1; i < mdrsl.getColumnCount(); i++) {
							if (mdrsl.getColumnNames()[i].equals("FEIYMCB_ID")
								|| mdrsl.getColumnNames()[i].equals("GONGS")
								|| mdrsl.getColumnNames()[i].equals("SHUIB")
								|| mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")
								|| mdrsl.getColumnNames()[i].equals("JUFLX")
								|| mdrsl.getColumnNames()[i].equals("SHUIL")) {
								
								sql.append(mdrsl.getColumnNames()[i]).append(" = ");
								if (mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")) {
									mlngFeiylbb_id = MainGlobal.getProperId(this.getIFeiylxModel(), mdrsl.getString(i));
									sql.append(mlngFeiylbb_id).append(",");
								} else {
									sql.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i))).append(",");
								}
							}
						}
						sql.deleteCharAt(sql.length() - 1);
						sql.append(" where id =").append(mdrsl.getString("id")).append(";\n");
					}
				}
				sql.append("end;");
//				System.out.println("Feiyxm Save1(String strchange, Visit visit) sql: \n" + sql);
				mdrsl.close();
				if (con.getUpdate(sql.toString()) >= 0) {
					con.commit();
					this.setMsg("����ɹ���");
				} else {
					con.rollBack();
					this.setMsg("����ʧ�ܣ�");
				}
				getSelectData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private boolean _FeiyglChick = false;

	public void FeiyglButton(IRequestCycle cycle) {
		_FeiyglChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			// cycle.activate("");

		}
		if (_ReturnChick) {
			_ReturnChick = false;
			if (((Visit) getPage().getVisit()).getPagePreferences().equals("Huopd")) {
				((Visit) getPage().getVisit()).setPagePreferences("");
				cycle.activate("Huopd");
			} else if (((Visit) getPage().getVisit()).getPagePreferences().equals("Kuangqzf")) {
				((Visit) getPage().getVisit()).setPagePreferences("");
				cycle.activate("Kuangqzf");
			}
		}
		if (_FeiyglChick) {
			_FeiyglChick = false;
			gotoFeiygl(cycle);
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			if (((Visit) getPage().getVisit()).getPagePreferences().equals("Huopd")) {
				getSelectData(((Visit) getPage().getVisit()).getLong1(),
					((Visit) getPage().getVisit()).getString12(),
					((Visit) getPage().getVisit()).getLong2());
			} else if (((Visit) getPage().getVisit()).getPagePreferences().equals("Kuangqzf")) {
				getSelectData(((Visit) getPage().getVisit()).getLong1(),
					((Visit) getPage().getVisit()).getString12(),
					Locale.kuangqyf_feiylbb_id);
			} else {
				getSelectData();
			}
		}
	}

	private void gotoFeiygl(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setString10(this.getParameters());
		cycle.activate("Feiyglb");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		long diancxxb_id = 0;

		if (visit.isFencb()) {

			diancxxb_id = this.getChangbValue().getId();
		} else {

			diancxxb_id = visit.getDiancxxb_id();
		}

		((Visit) getPage().getVisit()).setLong1(diancxxb_id);

		long feiylxid = this.getFeiylxValue().getId();
		visit.setLong10(feiylxid);
		String sql = "select fx.id,fm.mingc as feiymcb_id,fx.gongs,decode(fx.shuib,1,'�ɵ�˰','���ɵ�˰') as shuib,fx.shuil,\n"
				+ "fl.mingc as feiylbb_id,decode(fx.juflx,1,'�ܸ�','���ܸ�') as juflx from FEIYXMB fx,FEIYMCB fm,diancxxb dc,FEIYLBB fl \n"
				+ "where fx.feiymcb_id=fm.id and fx.diancxxb_id=dc.id and fx.FEIYLBB_ID=fl.id and fl.id="
				+ feiylxid + " and (dc.id=" + diancxxb_id +" or dc.id in (select id from diancxxb where jib=3 and id in (select fuid from diancxxb where id="+diancxxb_id+")))";
//		System.out.println("Feiyxm getSelectData() sql: \n" + sql);
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("FEIYXMB");
		egu.getColumn("id").setHidden(true);
		// egu.getColumn("diancxxb_id").setHeader("��λ����");
		egu.getColumn("feiymcb_id").setHeader("��������");
		egu.getColumn("gongs").setHeader("��ʽ");
		egu.getColumn("shuib").setHeader("˰��");
		egu.getColumn("shuil").setHeader("˰��");
		egu.getColumn("feiylbb_id").setHeader("��������");
		egu.getColumn("juflx").setHeader("�ܸ�����");

		// ��������
		egu.getColumn("feiymcb_id").setEditor(new ComboBox());
		egu.getColumn("feiymcb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id,mingc from FEIYMCB order by mingc"));

		// ˰��
		List shuiblist = new ArrayList();
		shuiblist.add(new IDropDownBean(0, "���ɵ�˰"));
		shuiblist.add(new IDropDownBean(1, "�ɵ�˰"));
		egu.getColumn("shuib").setEditor(new ComboBox());
		egu.getColumn("shuib").setComboEditor(egu.gridId,
				new IDropDownModel(shuiblist));
		egu.getColumn("shuib").setReturnId(true);
		egu.getColumn("shuib").setDefaultValue("�ɵ�˰");
		egu.getColumn("shuil").setDefaultValue("0.07");

		// ��������

		egu.getColumn("feiylbb_id").setDefaultValue(
				this.getFeiylxValue().getValue());
		egu.getColumn("feiylbb_id").setEditor(null);

		// �ܸ�����
		List juflxlist = new ArrayList();
		juflxlist.add(new IDropDownBean(0, "���ܸ�"));
		juflxlist.add(new IDropDownBean(1, "�ܸ�"));
		egu.getColumn("juflx").setEditor(new ComboBox());
		egu.getColumn("juflx").setComboEditor(egu.gridId,
				new IDropDownModel(juflxlist));
		egu.getColumn("juflx").setReturnId(true);
		egu.getColumn("juflx").setDefaultValue("���ܸ�");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(18);

		// ********************������************************************************
		egu.addTbarText("��������:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("FeiylxDropDown");
		comb3.setId("feiylxs");
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

//		if (visit.isFencb()) {

			egu.addTbarText("����:");
			ComboBox CBchangb = new ComboBox();
			CBchangb.setTransform("ChangbDropDown");
			CBchangb.setId("Changb");
			CBchangb.setLazyRender(true);// ��̬��
			CBchangb.setWidth(100);
			egu.addToolbarItem(CBchangb.getScript());
			egu
					.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");
//		}

		// �趨�������������Զ�ˢ��
		egu
				.addOtherScript("feiylxs.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/Feiyxmreport&lx='+ feiylxs.getValue();"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + str + "}").getScript()
				+ "}");
		// egu.addToolbarButton("�˶Է���",GridButton.ButtonType_SubmitSel,"SbButton");

		String str2 = " if(gridDiv_sm.getSelections().length <= 0 "
				+ "|| gridDiv_sm.getSelections().length > 1){ \n"
				+ " 	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��������Ŀ!'); \n"
				+ " 	return;"
				+ "}"
				+ " var rec = gridDiv_sm.getSelected(); \n"
				+ " if(rec.get('ID') == 0){ \n"
				+ " 	Ext.MessageBox.alert('��ʾ��Ϣ','�ڷ��ù���֮ǰ���ȱ���!'); \n"
				+ "  	return;"
				+ "}"
				+ " gridDiv_history = rec.get('ID')+','+feiylxs.getRawValue()+','+Changb.getRawValue()+','+Changb.getValue()+','+rec.get('FEIYMCB_ID'); \n"
				+ " document.getElementById('PARAMETERS').value=gridDiv_history; \n"
				+ " document.getElementById('FeiyglButton').click(); \n";

		egu.addToolbarItem("{"
				+ new GridButton("���ù���", "function(){" + str2 + "}")
						.getScript() + "}");
		setExtGrid(egu);
		con.Close();
	}

	public void getSelectData(long Diancxxb_id, String Meikxxb_id, long Feiylbb_id) {
		JDBCcon con = new JDBCcon();
		try {
			String mstrFeiylb = MainGlobal.getTableCol("feiylbb", "mingc",
					"id", String.valueOf(Feiylbb_id));
			String mstrMeikdwmc = MainGlobal.getTableCol("meikxxb", "mingc",
					"id", String.valueOf(Meikxxb_id));

			String sql = "select fx.id,m.mingc as meikxxb_id,fymc.mingc as feiymcb_id,fx.gongs,decode(fx.shuib,1,'�ɵ�˰','���ɵ�˰') as shuib,fx.shuil,	\n"
					+ " decode(1,1,'"
					+ mstrFeiylb
					+ "') as feiylbb_id,decode(fx.juflx,1,'�ܸ�','���ܸ�') as juflx,decode(glb.shifsy,1,'ʹ��','��ʹ��') as shifsy		\n"
					+ " from feiyxmb fx,feiyxmmkglb glb,meikxxb m,feiyglb fyglb,feiymcb fymc	\n"
					+ " where fx.id=glb.feiyxmb_id and glb.feiyglb_id=fyglb.id and fyglb.meikxxb_id=m.id and fx.diancxxb_id=fyglb.diancxxb_id	\n"
					+ " and fyglb.feiylbb_id=fx.feiylbb_id and fx.feiymcb_id=fymc.id	\n"
					+ " and (fx.diancxxb_id="+Diancxxb_id+" or fx.diancxxb_id in (select id from diancxxb where jib=3 and id in (select fuid from diancxxb where id="+Diancxxb_id+")))"
					+ " and m.id in ("
					+ Meikxxb_id + ") and fx.feiylbb_id=" + Feiylbb_id + "";

			ResultSetList rsl = con.getResultSetList(sql);

			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("FEIYXMB");
			egu.getColumn("id").setHidden(true);

			egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
			egu.getColumn("feiymcb_id").setHeader("��������");
			egu.getColumn("gongs").setHeader("��ʽ");
			egu.getColumn("shuil").setHeader("˰��");
			egu.getColumn("shuib").setHeader("˰��");
			egu.getColumn("feiylbb_id").setHeader("�������");
			egu.getColumn("juflx").setHeader("�ܸ�����");
			egu.getColumn("shifsy").setHeader("�Ƿ�ʹ��");

			egu.getColumn("shuil").setDefaultValue("0.07");

			// �����п�
			egu.getColumn("meikxxb_id").setWidth(100);
			egu.getColumn("feiymcb_id").setWidth(100);
			egu.getColumn("gongs").setWidth(200);
			egu.getColumn("shuil").setWidth(70);
			egu.getColumn("shuib").setWidth(70);
			egu.getColumn("feiylbb_id").setWidth(80);
			egu.getColumn("juflx").setWidth(70);
			egu.getColumn("shifsy").setWidth(70);
			// egu.getColumn("shoukdwb_id").setWidth(150);

			// ��������
			egu.getColumn("feiymcb_id").setEditor(new ComboBox());
			egu.getColumn("feiymcb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from feiymcb order by mingc"));
			egu.getColumn("feiymcb_id").setReturnId(true);

			// ˰��
			List shuiblist = new ArrayList();
			shuiblist.add(new IDropDownBean(0, "���ɵ�˰"));
			shuiblist.add(new IDropDownBean(1, "�ɵ�˰"));
			egu.getColumn("shuib").setEditor(new ComboBox());
			egu.getColumn("shuib").setComboEditor(egu.gridId,
					new IDropDownModel(shuiblist));
			egu.getColumn("shuib").setReturnId(true);
			egu.getColumn("shuib").setDefaultValue("�ɵ�˰");

			// ���õ�Ԫ���Ƿ�ɱ༭��Ĭ��ֵ
			egu.getColumn("meikxxb_id").setDefaultValue(mstrMeikdwmc);
			egu.getColumn("feiylbb_id").setDefaultValue(mstrFeiylb);
			egu.getColumn("feiylbb_id").setEditor(null);
			egu.getColumn("meikxxb_id").setEditor(null);

			// �ܸ�����
			List juflxlist = new ArrayList();
			juflxlist.add(new IDropDownBean(1, "�ܸ�"));
			juflxlist.add(new IDropDownBean(0, "���ܸ�"));
			egu.getColumn("juflx").setEditor(new ComboBox());
			egu.getColumn("juflx").setComboEditor(egu.gridId,
					new IDropDownModel(juflxlist));
			egu.getColumn("juflx").setReturnId(true);
			egu.getColumn("juflx").setDefaultValue("���ܸ�");

			// ú�����˷���ͬ��Ӧ��
			// List meikyfxtgys = new ArrayList();
			// meikyfxtgys.add(new IDropDownBean(1, "��"));
			// meikyfxtgys.add(new IDropDownBean(0, "��"));
			// egu.getColumn("meikyfxtgys").setEditor(new ComboBox());
			// egu.getColumn("meikyfxtgys").setComboEditor(egu.gridId, new
			// IDropDownModel(meikyfxtgys));
			// egu.getColumn("meikyfxtgys").setReturnId(true);
			// egu.getColumn("meikyfxtgys").setDefaultValue("��");

			// �Ƿ�ʹ��
			List shifsylist = new ArrayList();
			shifsylist.add(new IDropDownBean(1, "ʹ��"));
			shifsylist.add(new IDropDownBean(0, "��ʹ��"));
			egu.getColumn("shifsy").setEditor(new ComboBox());
			egu.getColumn("shifsy").setComboEditor(egu.gridId,
					new IDropDownModel(shifsylist));
			egu.getColumn("shifsy").setReturnId(true);
			egu.getColumn("shifsy").setDefaultValue("ʹ��");

			// �տλ
			// egu.getColumn("shoukdwb_id").setEditor(new ComboBox());
			// egu.getColumn("shoukdwb_id").setComboEditor(egu.gridId,
			// new IDropDownModel("select id,mingc from shoukdw order by
			// mingc"));
			// egu.getColumn("shoukdwb_id").setReturnId(true);

			egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
			// ********************������************************************************

			// �趨��������ť
			egu.addToolbarButton(GridButton.ButtonType_Refresh,
					"RefurbishButton");
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			egu
					.addToolbarItem("{"
							+ new GridButton("����",
									"function (){ document.getElementById('ReturnButton').click();}")
									.getScript() + "}");

			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
					+ "var end = url.indexOf(';');"
					+ "url = url.substring(0,end);"
					+ "url = url + '?service=page/Feiyxmreport&lx='+ feiylxs.getValue();"
					+ " window.open(url,'newWin');";
			egu.addToolbarItem("{"
					+ new GridButton("��ӡ", "function (){" + str + "}")
							.getScript() + "}");
			// egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			setExtGrid(egu);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
	}

	// ������������
	public IDropDownBean getFeiylxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIFeiylxModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setFeiylxValue(IDropDownBean Value) {

		if (((Visit) getPage().getVisit()).getDropDownBean1() != Value) {

			// ((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setIFeiylxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIFeiylxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIFeiylxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIFeiylxModels() {

		String sql = "select id, mingc from feiylbb where leib >= 1 order by leib";
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// ˰��
	public void setIShuibModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIShuibModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIShuibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIShuibModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ɵ�˰"));
		list.add(new IDropDownBean(0, "���ɵ�˰"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	// �ܸ�����
	public void setIJuflxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIJuflxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIJuflxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIJuflxModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ܸ�"));
		list.add(new IDropDownBean(0, "���ܸ�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));

		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// ú��λ

	public void setIMeikdwmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIMeikdwmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIMeikdwmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIMeikdwmcModels() {

		String sql = "select id,mingc from meikxxb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	// ú��λ_End

	// ����

	public IDropDownBean getChangbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getIChangbModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setChangbValue(IDropDownBean Value) {

		if (((Visit) getPage().getVisit()).getDropDownBean4() != Value) {

			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setIChangbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIChangbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIChangbModels() {
		
		String sql = "";
		if (((Visit) getPage().getVisit()).isFencb()) {
			sql = "select id, mingc from diancxxb d where d.fuid = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() 
				+ "union select id, mingc from diancxxb where id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " order by id";
		} else {
			sql = "select id, mingc from diancxxb d where d.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	// ����end

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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			
			if(!visit.getActivePageName().equals("Huopd")){
//				������Ǵ�"Huopd"ҳ����ת�����ģ������ҳ�����
				visit.setPagePreferences("");
			}
			
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			// visit.setboolean1(false); //Feiylb_Change

			setFeiylxValue(null); // DropDownBean1
			setIFeiylxModel(null); // ProSelectionModel1
			setIShuibModel(null); // ProSelectionModel2
			setIJuflxModel(null); // ProSelectionModel3
			setChangbValue(null); // DropDownBean4
			setIChangbModel(null); // ProSelectionModel4
			setIMeikdwmcModel(null);
			visit.setString10(null);
			visit.setString11(null); // ProSelectionModel5
		}

		if (visit.getPagePreferences().equals("")) {

			// if(visit.getboolean1()){
			// visit.setboolean1(false);
			getSelectData();
			// }
		} else if (visit.getPagePreferences().equals("Huopd")) {

			getSelectData(visit.getLong1(), visit.getString12(), visit.getLong2());
		} else if (visit.getPagePreferences().equals("Kuangqzf")) {

			// long Diancxxb_id,long Meikxxb_id,long Feiylbb_id
			getSelectData(visit.getLong1(), visit.getString12(),Locale.kuangqyf_feiylbb_id);
		}
	}

}