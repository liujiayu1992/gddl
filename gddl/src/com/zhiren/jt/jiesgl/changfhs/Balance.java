package com.zhiren.jt.jiesgl.changfhs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Balance extends BasePage {
	// List1
	// dropdwon1,2

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private String getDiancjsbs(long diancxxb_id) {

		JDBCcon con = new JDBCcon();
		String sql = "", diancjsbs = "";

		try {

			sql = "select JIESBDCBS from diancxxb where id=" + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				diancjsbs = rs.getString("JIESBDCBS") + "-";
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return diancjsbs;
	}

	private long SaveDiancjsmkb() {
		// 存储煤款表
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;

		try {
			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			sql = "insert into diancjsmkb(id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje,shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, diancjsb_id, beiz,RANLBMJBR,RANLBMJBRQ) "
					+ " values ("
					+ Id
					+ ", "
					+ getDiancmcValue().getId()
					+ ",'"
					+ ((Balancebean) getEditValues().get(0)).getDiancjsbs()
					+ ((Balancebean) getEditValues().get(0)).getJiesbh()
					+ "',"
					+ MainGlobal.getTableId("gongysb", "quanc",
							((Balancebean) getEditValues().get(0)).getFahdw())
					+ ", '"
					+ ((Balancebean) getEditValues().get(0)).getFahdw()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getFaz()
					+ "', to_date('"
					+ FormatDate(((Balancebean) getEditValues().get(0))
							.getFahrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Balancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getXianshr()
					+ "', to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Balancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getDuifdd()
					+ "',"
					+ ((Balancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJiessl()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJingz()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getShulzjbz()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getBukyqjk()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJiasje()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJiakhj()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJiakje()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getJiaksk()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getJiaksl()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getBuhsdj()
					+ ", "
					+ getJieslxValue().getId()
					+ ", to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'),null,"
					+ getHetbhValue().getId()
					+ ",0,0,0,'"
					+ ((Balancebean) getEditValues().get(0)).getBeiz()
					+ "','"
					+ ((Balancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd'))";

			if (con.getInsert(sql) == 1) {
				
				((Balancebean) getEditValues().get(0)).setId(Id);
				return Id;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}

	private long SaveDiancjsyfb(long Meikid) {
		// 存储运费表
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		

		try {
			
			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			sql = " insert into diancjsyfb (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, guotyf, dityf, jiskc,hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, beiz, diancjsb_id, diancjsmkb_id,RANLBMJBR,RANLBMJBRQ)"
					+ " values("
					+ Id
					+ ", "
					+ getDiancmcValue().getId()
					+ ", '"
					+ ((Balancebean) getEditValues().get(0)).getDiancjsbs()
					+ ((Balancebean) getEditValues().get(0)).getJiesbh()
					+ "', "
					+ MainGlobal.getTableId("gongysb", "quanc",
							((Balancebean) getEditValues().get(0)).getFahdw())
					+ ", '"
					+ ((Balancebean) getEditValues().get(0)).getFahdw()
					+ "','"
					+ ((Balancebean) getEditValues().get(0)).getFaz()
					+ "',to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Balancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getXianshr()
					+ "',to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'), to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'), '"
					+ ((Balancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Balancebean) getEditValues().get(0)).getDuifdd()
					+ "', "
					+ ((Balancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJiessl()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getJingz()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getTielyf()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getQitzf()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getJiskc()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getShulzjbz()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getBukyqyzf()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getYunzfhj()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getBuhsyf()
					+ ","
					+ ((Balancebean) getEditValues().get(0)).getYunfsk()
					+ ", "
					+ ((Balancebean) getEditValues().get(0)).getYunfsl()
					+ ", 0, "
					+ getJieslxValue().getId()
					+ ", to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'), null,"
					+ getHetbhValue().getId()
					+ ", 0, 0, '"
					+ ((Balancebean) getEditValues().get(0)).getBeiz()
					+ "',0,"+Meikid+",'"
					+ ((Balancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Balancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd'))";

			if (con.getInsert(sql) == 1) {
				
				((Balancebean) getEditValues().get(0)).setYid(Id);
				return Id;
			}
				

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}

	private long SaveJszbsjb(long Mkid, String mingc, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// 保存结算单中指标数据
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));

			sql = " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
					+ " values ("
					+ Id
					+ ", "
					+ Mkid
					+ ", "
					+ getProperId(getIZhibbmModel(), mingc)
					+ ", '"
					+ hetbz
					+ "', "
					+ gongf
					+ ", "
					+ changf
					+ ", "
					+ jies
					+ ", "
					+ yingk
					+ ", "
					+ zhejbz
					+ ", "
					+ zhejje
					+ ","
					+ zhuangt
					+ ")";

			if (con.getInsert(sql) == 1) {

				return Id;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}

	private long SaveDiancjsb(long diancjsbId) {

		// 保存结算总表数据
		// insert时参数=0，update参数=diancjsbid
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			if (diancjsbId == 0) {

				Id = Long
						.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));

				sql = "insert into diancjsb(id, diancxxb_id, gongyb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, ches, jiessl, guohl, bukmk, hansmk, buhsmk, meisk, guotyf, dityf, jiskc, yunfsk, yunzfhj, hanshj, jieslx, ruzrq, hetb_id, beiz) "
						+ " values("
						+ Id
						+ ", "
						+ getDiancmcValue().getId()
						+ ", "
						+ MainGlobal.getTableId("gongysb", "quanc",
								((Balancebean) getEditValues().get(0))
										.getFahdw())
						+ ", '"
						+ ((Balancebean) getEditValues().get(0)).getFahdw()
						+ "', '"
						+ ((Balancebean) getEditValues().get(0)).getFaz()
						+ "', to_date('"
						+ this
								.FormatDate(((Balancebean) getEditValues().get(
										0)).getFahksrq())
						+ "','yyyy-MM-dd'), to_date('"
						+ this
								.FormatDate(((Balancebean) getEditValues().get(
										0)).getFahjzrq())
						+ "','yyyy-MM-dd'), '"
						+ ((Balancebean) getEditValues().get(0)).getPinz()
						+ "', '"
						+ ((Balancebean) getEditValues().get(0)).getDaibcc()
						+ "', '"
						+ ((Balancebean) getEditValues().get(0)).getYuanshr()
						+ "', '"
						+ ((Balancebean) getEditValues().get(0)).getXianshr()
						+ "', to_date('"
						+ this
								.FormatDate(((Balancebean) getEditValues().get(
										0)).getYansksrq())
						+ "','yyyy-MM-dd'),to_date('"
						+ this
								.FormatDate(((Balancebean) getEditValues().get(
										0)).getYansjzrq())
						+ "','yyyy-MM-dd'), '"
						+ ((Balancebean) getEditValues().get(0)).getYansbh()
						+ "', "
						+ ((Balancebean) getEditValues().get(0)).getChes()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getJiessl()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getJingz()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getBukyqjk()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getJiasje()
						+ ","
						+ ((Balancebean) getEditValues().get(0)).getJiakhj()
						+ ","
						+ ((Balancebean) getEditValues().get(0)).getJiaksk()
						+ ","
						+ ((Balancebean) getEditValues().get(0)).getTielyf()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getQitzf()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getJiskc()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getYunfsk()
						+ ", "
						+ ((Balancebean) getEditValues().get(0)).getYunzfhj()
						+ ","
						+ ((Balancebean) getEditValues().get(0)).getHej()
						+ ", "
						+ getJieslxValue().getId()
						+ ", null, "+getHetbhValue().getId()+", '"
						+ ((Balancebean) getEditValues().get(0)).getBeiz()
						+ "')";

				if (con.getInsert(sql) == 1) {

					return Id;
				}

			} else {

				sql = " update diancjsb set hansmk = hansmk+"
						+ ((Balancebean) getEditValues().get(0)).getJiasje()
						+ ", buhsmk = buhsmk+"
						+ ((Balancebean) getEditValues().get(0)).getJiakhj()
						+ "," + " meisk = meisk+"
						+ ((Balancebean) getEditValues().get(0)).getJiaksk()
						+ ",guotyf = guotyf+"
						+ ((Balancebean) getEditValues().get(0)).getTielyf()
						+ "," + " dityf = dityf+"
						+ ((Balancebean) getEditValues().get(0)).getQitzf()
						+ ",jiskc = jiskc+"
						+ ((Balancebean) getEditValues().get(0)).getJiskc()
						+ "," + " yunfsk = yunfsk+"
						+ ((Balancebean) getEditValues().get(0)).getYunfsk()
						+ ",yunzfhj = yunzfhj+"
						+ ((Balancebean) getEditValues().get(0)).getYunzfhj()
						+ "," + " hanshj = hanshj+"
						+ ((Balancebean) getEditValues().get(0)).getHej()
						+ ",beiz =beiz||' ''"
						+ ((Balancebean) getEditValues().get(0)).getBeiz()
						+ "'," + " where id = " + diancjsbId + "";

				con.getUpdate(sql);
				return diancjsbId;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}

	private boolean checkbh(String jiesbh) {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			sql = " select jiesbh from ((select bianm as jiesbh from diancjsmkb) union (select bianm as jiesbh from diancjsyfb)) where jiesbh='" + jiesbh + "'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				return false;
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return true;
	}

	private boolean Save() {
		// 重要说明：所有结算表未填写 合同id、流程状态id、流程跟踪id、矿方结算id
		JDBCcon con = new JDBCcon();
		String msg="";
		long Mkid = 0;// 煤款id
		long Yfid = 0;// 运费id
		long Zbbid = 0;// 指标表id
		boolean Flag = false;
		try {
			if (getEditValues() != null
					&& !getEditValues().isEmpty()
					&& !((Balancebean) getEditValues().get(0)).getJiesbh()
							.equals("")) {
				if (checkbh(((Balancebean) getEditValues().get(0))
						.getDiancjsbs()
						+ ((Balancebean) getEditValues().get(0)).getJiesbh())) {

					if (getJieslxValue().getId() == 1) {// 两票结算

						if (((Balancebean) getEditValues().get(0)).getId() == 0) {
							// 电厂煤矿表
							Mkid = SaveDiancjsmkb();

							if (Mkid > 0) {
								// 操作电厂结算运费表
								Yfid = SaveDiancjsyfb(Mkid);

								if (Yfid > 0) {
									// 要和基础信息指标模块定一下zhibb内容

									Zbbid = this.SaveJszbsjb(Mkid, "数量",
											((Balancebean) getEditValues().get(
													0)).getHetsl(),
											((Balancebean) getEditValues().get(
													0)).getGongfsl(),
											((Balancebean) getEditValues().get(
													0)).getYanssl(),
											((Balancebean) getEditValues().get(
													0)).getJiessl(),
											((Balancebean) getEditValues().get(
													0)).getYingksl(),
											((Balancebean) getEditValues().get(
													0)).getShulzjbz(),
											((Balancebean) getEditValues().get(
													0)).getShulzjje(), 1);

									if (Zbbid > 0) {

										Zbbid = this.SaveJszbsjb(Mkid,
												"收到基低位热值",
												((Balancebean) getEditValues()
														.get(0)).getHetrl(),
												((Balancebean) getEditValues()
														.get(0)).getGongfrl(),
												((Balancebean) getEditValues()
														.get(0)).getYansrl(),
												((Balancebean) getEditValues()
														.get(0)).getJiesrl(),
												((Balancebean) getEditValues()
														.get(0)).getYingkrl(),
												((Balancebean) getEditValues()
														.get(0)).getRelzjbz(),
												((Balancebean) getEditValues()
														.get(0)).getRelzjje(),
												1);

										if (Zbbid > 0) {

											Zbbid = this
													.SaveJszbsjb(
															Mkid,
															"干燥基全硫",
															((Balancebean) getEditValues()
																	.get(0))
																	.getHetlf(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getLiubz(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getYanslf(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getJieslf(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getLiuyk(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getLiuzjbz(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getLiuzjje(),
															1);

											if (Zbbid > 0) {

												Zbbid = this
														.SaveJszbsjb(
																Mkid,
																"干燥无灰基挥发分",
																((Balancebean) getEditValues()
																		.get(0))
																		.getHethff(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getGongfhff(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getYanshff(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getJieshff(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getYingkhff(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getHuiffzjbz(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getHuiffzjje(),
																1);

												if (Zbbid > 0) {

													Zbbid = this
															.SaveJszbsjb(
																	Mkid,
																	"干燥基灰分",
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getHethf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getGongfhf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getYanshf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getJieshf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getYingkhf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getHuifzjbz(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getHuifzjje(),
																	1);

													if (Zbbid > 0) {

														Zbbid = this
																.SaveJszbsjb(
																		Mkid,
																		"全水分",
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getHetsf(),
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getGongfsf(),
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getYanssf(),
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getJiessf(),
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getYingksf(),
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getShuifzjbz(),
																		((Balancebean) getEditValues()
																				.get(
																						0))
																				.getShuifzjje(),
																		1);
														
														if(Zbbid > 0){
															
															Flag = true;
														}

//														if (Zbbid > 0) {
//															// 总表操作
//															if (getDiancjsmkbhValue()
//																	.getId() == -1) {
//																// 没有有相关结算表
//																Zbid = SaveDiancjsb(0);
//
//																if (Zbid > 0) {
//
//																	// 更新两个表的结算总表id
//																	sql = "update diancjsmkb set diancjsb_id="
//																			+ Zbid
//																			+ " where id="
//																			+ Mkid;
//																	con
//																			.getUpdate(sql);
//
//																	sql = "update diancjsyfb set diancjsb_id="
//																			+ Zbid
//																			+ " where id="
//																			+ Yfid;
//																	con
//																			.getUpdate(sql);
//																	
//																	Flag = true;
//																}
//															} else if (getDiancjsbId(getDiancjsmkbhValue()
//																	.getId()) > 0) {
//																// 有相关结算表
//																Zbid = SaveDiancjsb(getDiancjsbId(getDiancjsmkbhValue()
//																		.getId()));
//
//																if (Zbid > 0) {
//																	// 更新两个表的结算总表id
//																	sql = "update diancjsmkb set diancjsb_id="
//																			+ Zbid
//																			+ " where id="
//																			+ Mkid;
//																	con
//																			.getUpdate(sql);
//
//																	sql = "update diancjsyfb set diancjsb_id="
//																			+ Zbid
//																			+ " where id="
//																			+ Yfid;
//																	con
//																			.getUpdate(sql);
//
//																	Flag = true;
//																}
//															}
//														}
													}
												}
											}
										}
									}
								}
							}
						}

					} else if (getJieslxValue().getId() == 0) {
						// 单结算煤款
						if (((Balancebean) getEditValues().get(0)).getId() == 0) {
							// 保存煤款表
							Mkid = SaveDiancjsmkb();

							if (Mkid > 0) {
								// 保存指标表

								Zbbid = this.SaveJszbsjb(Mkid, "数量",
										((Balancebean) getEditValues().get(0))
												.getHetsl(),
										((Balancebean) getEditValues().get(0))
												.getGongfsl(),
										((Balancebean) getEditValues().get(0))
												.getYanssl(),
										((Balancebean) getEditValues().get(0))
												.getJiessl(),
										((Balancebean) getEditValues().get(0))
												.getYingksl(),
										((Balancebean) getEditValues().get(0))
												.getShulzjbz(),
										((Balancebean) getEditValues().get(0))
												.getShulzjje(), 1);

								if (Zbbid > 0) {

									Zbbid = this.SaveJszbsjb(Mkid, "收到基低位热值",
											((Balancebean) getEditValues().get(
													0)).getHetrl(),
											((Balancebean) getEditValues().get(
													0)).getGongfrl(),
											((Balancebean) getEditValues().get(
													0)).getYansrl(),
											((Balancebean) getEditValues().get(
													0)).getJiesrl(),
											((Balancebean) getEditValues().get(
													0)).getYingkrl(),
											((Balancebean) getEditValues().get(
													0)).getRelzjbz(),
											((Balancebean) getEditValues().get(
													0)).getRelzjje(), 1);

									if (Zbbid > 0) {

										Zbbid = this.SaveJszbsjb(Mkid, "干燥基全硫",
												((Balancebean) getEditValues()
														.get(0)).getHetlf(),
												((Balancebean) getEditValues()
														.get(0)).getGongflf(),
												((Balancebean) getEditValues()
														.get(0)).getYanslf(),
												((Balancebean) getEditValues()
														.get(0)).getJieslf(),
												((Balancebean) getEditValues()
														.get(0)).getLiuyk(),
												((Balancebean) getEditValues()
														.get(0)).getLiuzjbz(),
												((Balancebean) getEditValues()
														.get(0)).getLiuzjje(),
												1);

										if (Zbbid > 0) {

											Zbbid = this
													.SaveJszbsjb(
															Mkid,
															"干燥无灰基挥发分",
															((Balancebean) getEditValues()
																	.get(0))
																	.getHethff(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getGongfhff(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getYanshff(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getJieshff(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getYingkhff(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getHuiffzjbz(),
															((Balancebean) getEditValues()
																	.get(0))
																	.getHuiffzjje(),
															1);

											if (Zbbid > 0) {

												Zbbid = this
														.SaveJszbsjb(
																Mkid,
																"干燥基灰分",
																((Balancebean) getEditValues()
																		.get(0))
																		.getHethf(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getGongfhf(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getYanshf(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getJieshf(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getYingkhf(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getHuifzjbz(),
																((Balancebean) getEditValues()
																		.get(0))
																		.getHuifzjje(),
																1);

												if (Zbbid > 0) {

													Zbbid = this
															.SaveJszbsjb(
																	Mkid,
																	"全水分",
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getHetsf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getGongfsf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getYanssf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getJiessf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getYingksf(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getShuifzjbz(),
																	((Balancebean) getEditValues()
																			.get(
																					0))
																			.getShuifzjje(),
																	1);
													
													if(Zbbid>0){
														
														Flag = true;
													}

//													if (Zbbid > 0) {
//
//														if (getDiancjsmkbhValue()
//																.getId() == -1) {
//															// 没有关联的煤款表
//															Zbid = SaveDiancjsb(0);
//
//															if (Zbid > 0) {
//
//																sql = "update diancjsmkb set diancjsb_id="
//																		+ Zbid
//																		+ " where id="
//																		+ Mkid;
//																con
//																		.getUpdate(sql);
//																Flag = true;
//
//															}
//
//														} else if (getDiancjsbId(getDiancjsmkbhValue()
//																.getId()) > 0) {
//															// 有关联的煤款表id
//
//															Zbid = SaveDiancjsb(getDiancjsbId(getDiancjsmkbhValue()
//																	.getId()));
//
//															if (Zbid > 0) {
//
//																sql = "update diancjsmkb set diancjsb_id="
//																		+ Zbid
//																		+ " where id="
//																		+ Mkid;
//																con
//																		.getUpdate(sql);
//																Flag = true;
//															}
//														}
//													}
												}
											}
										}
									}
								}
							}
						}
					} else {
						// 单结算运费
						if (getDiancjsmkbhValue().getId()> -1) {
							// 有关联的
							if (((Balancebean) getEditValues().get(0)).getId() == 0) {
								Yfid = SaveDiancjsyfb(getDiancjsmkbhValue().getId());

								if (Yfid > 0) {

//									Zbid = SaveDiancjsb(getDiancjsbId(getDiancjsmkbhValue()
//											.getId()));
//									sql = "update diancjsyfb set diancjsb_id="
//											+ Zbid + " where id=" + Yfid;
//									con.getUpdate(sql);
									Flag = true;
								}
							}
							
						}else if(checkXitszDjyf()){
//							读系统信息表的设置，看公司系统能不能单结算运费
							Yfid = SaveDiancjsyfb(0);

							if (Yfid > 0) {

								Flag = true;
							}
							
						}else {

							msg="请选择与该运费对应的煤款结算单";
						}
					}
				} else {

					msg="结算单编号重复";
				}
			} else {

				msg="不能保存空结算单";
			}

			if (Flag) {

				((Balancebean) getEditValues().get(0)).setId(Mkid+Yfid);
				setMsg("结算操作成功！");
			} else {

				setMsg(msg+" 结算操作失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		getIHetbhModels();
		return Flag;
	}

	private boolean checkXitszDjyf() {
		// TODO 自动生成方法存根
//		检查系统设置中的"可单独结算运费"设置
		JDBCcon con=new JDBCcon();
		try{
			String zhi="";
			
			String sql="select zhi from xitxxb where mingc='可单独结算运费'";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				zhi=rs.getString("zhi");
			}
			
			if(zhi.trim().equals("是")){
				
				return true;
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return false;
	}

	private long getDiancjsbId(long id) {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		try {

			String sql = "select diancjsb_id from diancjsmkb where id=" + id
					+ "";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				return rs.getLong("diancjsb_id");
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}

	private String Diqbm(String Meikdqmc) {

		JDBCcon con = new JDBCcon();
		String mdiqbm = "";
		try {
			String sql = "select meikbm from ((select meikbm,meikdwmc as meikdqqc from meikxxb)union(select meikdqbm as meikbm,meikdqmc from meikdqb)) where meikdqqc='"
					+ Meikdqmc + "'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				mdiqbm = rs.getString("meikbm");
			}

			if (mdiqbm.length() > 6) {

				mdiqbm = mdiqbm.substring(0, 6);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return mdiqbm;
	}

	private long Diancjsmkb_id(String meikjsdbh) {

		JDBCcon con = new JDBCcon();
		long id = 0;
		try {
			String sql = "select id from diancjsmkb where bianh='" + meikjsdbh
					+ "'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				id = rs.getLong("id");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return id;
	}

	private boolean CheckMeikjsdbh(String meikjsdbh) {
		if (!meikjsdbh.equals("")) {
			JDBCcon con = new JDBCcon();
			try {
				String sql = "select id from diancjsmkb where bianh='"
						+ meikjsdbh + "'";
				ResultSet rs = con.getResultSet(sql);
				if (rs.next()) {
					return true;
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return false;

		} else {

			return true;
		}
	}

	private void Retruns() {

		if (getHetbhValue().getId() > -1) {

			getSelectData();
			getIDiancjsmkbhModels();
			getIFahdwModels();
			getIFazModels();
			getIRanlpzModels();
			getIShoukdwModels();
			Fuz();
		} else {

			setMsg("请选择合同编号！");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {

		_QuedChick = true;
	}
	
	private boolean _QuxChick = false;

	public void QuxButton(IRequestCycle cycle) {

		_QuxChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns();
		}
		if (_QuedChick) {
			_QuedChick = false;
			Submit();
		}
		if (_QuxChick) {
			_QuxChick = false;
			Qux();
		}
	}

	private void Qux() {
		// TODO 自动生成方法存根
		
		getSelectData();
		getIHetbhModels();
	}

	private void Submit() {
		// TODO 自动生成方法存根

		if (((Balancebean) getEditValues().get(0)).getId() == 0
				||((Balancebean) getEditValues().get(0)).getYid() == 0) {//没保存先做保存

			if (Save() && getLiucmcValue().getId() > -1) {
//				 流程方法
				if(getJieslxValue().getId()==1){//两票结算
					
					Liuc.tij("diancjsmkb", ((Balancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
					Liuc.tij("diancjsyfb", ((Balancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				
				}else if(getJieslxValue().getId()==0){//煤款结算
					
					Liuc.tij("diancjsmkb", ((Balancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				
				}else if(getJieslxValue().getId()==2){//运费结算
					
					Liuc.tij("diancjsyfb", ((Balancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				}

			} else if (getLiucmcValue().getId() == -1) {

				setMsg("请选择流程名称！");
			}
			
		}else{//已经保存
			
			if (getLiucmcValue().getId() > -1) {
//				 流程方法
				if(getJieslxValue().getId()==1){//两票结算
					
					Liuc.tij("diancjsmkb", ((Balancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
					Liuc.tij("diancjsyfb", ((Balancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				
				}else if(getJieslxValue().getId()==0){//煤款结算
					
					Liuc.tij("diancjsmkb", ((Balancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				
				}else if(getJieslxValue().getId()==2){//运费结算
					
					Liuc.tij("diancjsyfb", ((Balancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				}

			} else if (getLiucmcValue().getId() == -1) {

				setMsg("请选择流程名称！");
			}
		}
	}

	private Balancebean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Balancebean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Balancebean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		Visit visit = new Visit();
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		JDBCcon JDBCcon = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			String mtianzdw = MainGlobal.getTableCol("diancxxb", "mingc", "id",
					String.valueOf(visit.getDiancxxb_id()));
			String mjiesbh = "";
			String mfahdw = "";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = mtianzdw;
			String mzhangh = "";
			String mhetsl = "";// 合同数量
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = myuanshr;
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			String mhetrl = "";// 合同热量
			double mgongfrl = 0;// 供方热量
			double myansrl = 0;
			double myingkrl = 0;
			double mrelzjbz = 0;
			double mjiesrl = 0;// 结算热量
			double mrelzjje = 0;
			String mhethff = "";// 合同挥发分
			double mgongfhff = 0;
			double myanshff = 0;
			double myingkhff = 0;
			double mhuiffzjbz = 0;
			double mhuiffzjje = 0;
			double mjieshff = 0;// 结算挥发分
			String mhethf = "";
			double mgongfhf = 0;
			double myanshf = 0;
			double mjieshf = 0;
			double myingkhf = 0;
			double mhuifzjbz = 0;
			double mhuifzjje = 0;
			String mhetsf = "";// 合同水分
			double mgongfsf = 0;
			double myanssf = 0;
			double myingksf = 0;
			double mshuifzjbz = 0;
			double mshuifzjje = 0;
			double mjiessf = 0;// 结算水分
			String mhetlf = "";// 合同硫分
			double mgongflf = 0;
			double myanslf = 0;
			double mliuyk = 0;
			double mliubz = 0;
			double mliuzjbz = 0;
			double mliuzjje = 0;
			double mjieslf = 0;// 结算硫分
			double mjiessl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mzaf = 0;
			double mbukyqyzf = 0;
			double mjiskc = 0;
			double mbuhsyf = 0;
			double myunfsl = 0.07;
			double myunfsk = 0;
			double myunzfhj = 0;
			double mhej = 0;
			String mdaxhj = "";
			String mmeikhjdx = "";
			String myunzfhjdx = "";
			String mbeiz = "";
			String mranlbmjbr = visit.getRenymc();
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			Date mfahrq = new Date();
			double mhuirdzjbz = 0;
			double mhuirdzjje = 0;
			double mhuirdbz = 0;
			double mgongfhrd = 0;
			double myanshrd = 0;
			double mqitzf = 0;
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = 0;
			double mrelsx = 0;
			double mrelxx = 0;
			double mliusx = 0;
			double mliuxx = 0;
			double myuns = 0;
			String myunsfs = "";
			String mdiancjsbs = "";
			mdiancjsbs = getDiancjsbs(visit.getDiancxxb_id());
			_editvalues.add(new Balancebean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mfaz, mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, mfapbh, mdaibcc, myansbh,
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, mshulzjje,
					mhetrl, mgongfrl, myansrl, mjiesrl, myingkrl, mrelzjbz,
					mrelzjje, mhethff, mgongfhff, myanshff, mjieshff,
					myingkhff, mhuiffzjbz, mhuiffzjje, mhethf, mgongfhf,
					myanshf, mjieshf, myingkhf, mhuifzjbz, mhuifzjje, mhetsf,
					mgongfsf, myanssf, mjiessf, myingksf, mshuifzjbz,
					mshuifzjje, mhetlf, mgongflf, myanslf, mjieslf, mliuyk,
					mliubz, mliuzjbz, mliuzjje, mjiessl, mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,
					mzaf, mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mhuirdzjbz, mhuirdzjje, mhuirdbz, mgongfhrd,
					myanshrd, mqitzf, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx, mrelsx, mrelxx, mliusx, mliuxx, myuns,
					myunsfs, mdiancjsbs));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Balancebean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		//结算类型
		tb1.addText(new ToolbarText("结算类型:"));
		ComboBox cbjslx = new ComboBox();
		cbjslx.setTransform("JIESLXDropDown");
		cbjslx.setId("JSLXD");
		cbjslx.setWidth(80);
		tb1.addField(cbjslx);
		tb1.addText(new ToolbarText("-"));
		
//		//关联结算单编号
		tb1.addText(new ToolbarText("关联结算单编号:"));
		ComboBox cbglbh = new ComboBox();
		cbglbh.setTransform("DiancjsmkbhDropDown");
		cbglbh.setId("DCJSBH");
		cbglbh.setWidth(120);
		tb1.addField(cbglbh);
		tb1.addText(new ToolbarText("-"));
		
//		//结算合同号
		tb1.addText(new ToolbarText("结算合同号:"));
		ComboBox cbhtbh = new ComboBox();
		cbhtbh.setTransform("HetbhDropDown");
		cbhtbh.setId("HTBHD");
		cbhtbh.setWidth(120);
		cbhtbh.setListeners(
		"select:function(own,newValue,oldValue){"+
        " document.forms[0].submit();}" );
		tb1.addField(cbhtbh);
		tb1.addText(new ToolbarText("-"));
		
//		添加
		ToolbarButton addbt=new ToolbarButton(null,"添加","function(){ "
													+ " if(!checkHetbh()){" +
															"" +
															" return false;" +
															"}else{" +
															"" +
															"	return true;" +
															"}"
													+ "}");
		addbt.setId("addbt");
		tb1.addItem(addbt);
//		tb1.addText(new ToolbarText("-"));
		
		
//		取消
		ToolbarButton quxbt=new ToolbarButton(null,"取消","function(){ document.Form0.QuxButton.click();}");
		quxbt.setId("quxbt");
		tb1.addItem(quxbt);
//		tb1.addText(new ToolbarText("-"));
		
//		保存
		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ document.Form0.SaveButton.click() }");
		savebt.setId("savebt");
		tb1.addItem(savebt);
//		tb1.addText(new ToolbarText("-"));
		
//		提交进入流程
		ToolbarButton submitbt=new ToolbarButton(null,"提交进入流程","function(){ Lcsubmit();}");
		submitbt.setId("submitbt");
		tb1.addItem(submitbt);
		
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			
			setDiancmcValue(null);// 9
			setIDiancmcModel(null);// 9
			getIDiancmcModels();

			setLiucmcValue(null);// 10
			setILiucmcModel(null);// 10
			getILiucmcModels();

			getIZhibbmModels();
			getIDiancjsmkbhModels();
			getIFahdwModels();
			getIFazModels();
			getIJieslxModels();
			getIRanlpzModels();
			getIShoukdwModels();
			getIHetbhModels();
			visit.setboolean1(false);// 合同号
			visit.setboolean2(false);// 填制单位
			getSelectData();
			getJiesbsArrayScript();
		}
		getToolbars();
		if (visit.getboolean1()) {

			getSelectData();
			Fuz();
			visit.setboolean1(false);
		}

	}
	
	public String getJiesbsArrayScript() {
		JDBCcon con = new JDBCcon();
        StringBuffer JiesbsArrayScript = new StringBuffer();
        String sql = "";
        String tmp="";
        int i=0;
        try {
           sql=" select dc.quanc,dc.jiesbdcbs||'-' as jiesbdcbs from diancxxb dc ";
           ResultSet rstmp=con.getResultSet(sql);
           while(rstmp.next()){
               
               i++;
           }
           rstmp.close();
           
           for(int j=0;j<i;j++){
              
               if(j==0){
                   tmp="new Array()";                   
               }else{
                   tmp+=",new Array()";
               }
               
           }
           i=0;
           JiesbsArrayScript.append("var Jiesbs=new Array("+tmp+");");
           
           ResultSet rs=con.getResultSet(sql);
           while(rs.next()){
               
               String mmingc=rs.getString("quanc");
               String mjiesdbs=rs.getString("jiesbdcbs");
               
               JiesbsArrayScript.append("Jiesbs["+i+"][0] ='"+mmingc+"';");
               JiesbsArrayScript.append("Jiesbs["+i+"][1] ='"+mjiesdbs+"';");
               i++;
           }
           rs.close();           
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.Close();
        return JiesbsArrayScript.toString();
	}

	private void Fuz() {
		JDBCcon con = new JDBCcon();

		try {
			String sql = "select GONGFDWMC,GONGFKHYH,GONGFZH,gongys,pinz,faz,daoz "
					+ " from hetb,"
					+ " (select quanc as gongys from gongysb,hetb "
					+ " where hetb.gongysb_id=gongysb.id and hetbh='"
					+ getHetbhValue().getValue()
					+ "'),"
					+ " (select distinct pinzb.mingc as pinz from hetslb,hetb,pinzb "
					+ " where hetslb.hetb_id=hetb.id and hetslb.pinzb_id=pinzb.id and hetbh='"
					+ getHetbhValue().getValue()
					+ "'),"
					+ " (select distinct mingc as faz from hetslb,hetb,chezxxb "
					+ " where hetslb.faz_id=chezxxb.id and hetslb.hetb_id=hetb.id and hetbh='"
					+ getHetbhValue().getValue()
					+ "'),"
					+ " (select distinct mingc as daoz from hetslb,hetb,chezxxb "
					+ " where hetslb.daoz_id=chezxxb.id and hetslb.hetb_id=hetb.id and hetbh='"
					+ getHetbhValue().getValue()
					+ "')"
					+ " where hetbh='"
					+ getHetbhValue().getValue() + "'";

			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				((Balancebean) getEditValues().get(0)).setFahdw(rs
						.getString("gongys"));
				((Balancebean) getEditValues().get(0)).setShoukdw(rs
						.getString("GONGFDWMC"));
				((Balancebean) getEditValues().get(0)).setKaihyh(rs
						.getString("GONGFKHYH"));
				((Balancebean) getEditValues().get(0)).setZhangh(rs
						.getString("GONGFZH"));
				((Balancebean) getEditValues().get(0)).setPinz(rs
						.getString("pinz"));
				((Balancebean) getEditValues().get(0)).setFaz(rs
						.getString("faz"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	// 指标表编码Model

	public IDropDownBean getZhibbmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIZhibbmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setZhibbmValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}

	public void setIZhibbmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIZhibbmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIZhibbmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIZhibbmModels() {
		String sql = "select id,mingc from zhibb where leib=1 order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// 指标表编码Model_end

	// 有关系的DIANCJSMKB结算编号（Balance模块调用）
	public IDropDownBean getDiancjsmkbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIDiancjsmkbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setDiancjsmkbhValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean2(value);
	}

	public void setIDiancjsmkbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIDiancjsmkbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIDiancjsmkbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIDiancjsmkbhModels() {

		String sql = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			sql = "select id,bianm from DIANCJSMKB where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by bianm";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			sql = "select id,bianm from DIANCJSMKB where diancxxb_id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") order by bianm";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			sql = "select id,bianm from DIANCJSMKB  order by bianm";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	// DIANCJSMKB结算编号end

	// 发货单位
	public IDropDownBean getFahdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIFahdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setFahdwValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean3(value);
	}

	public void setIFahdwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIFahdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIFahdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIFahdwModels() {
		String sql = "select id,quanc from gongysb order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// 发货单位end

	// 发站
	public IDropDownBean getFazValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getIFazModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFazValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean4(value);
	}

	public void setIFazModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIFazModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIFazModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIFazModels() {
		String sql = "select id,mingc from chezxxb order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	// 发站_end

	// 结算类型
	public IDropDownBean getJieslxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getIJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJieslxValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean5(value);
	}

	public void setIJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIJieslxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIJieslxModels() {
		List arryJieslx = new ArrayList();
		arryJieslx.add(new IDropDownBean(0, "煤款结算"));
		arryJieslx.add(new IDropDownBean(1, "两票结算"));
		arryJieslx.add(new IDropDownBean(2, "运费结算"));
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(arryJieslx));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	// 结算类型_end

	// 品种
	public IDropDownBean getRanlpzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getIRanlpzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setRanlpzValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean6(value);
	}

	public void setIRanlpzModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getIRanlpzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getIRanlpzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public IPropertySelectionModel getIRanlpzModels() {
		String sql = "select id,mingc from pinzb order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	// 品种_end

	// 收款单位
	public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getIShoukdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setShoukdwValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setIShoukdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIShoukdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getIShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public IPropertySelectionModel getIShoukdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			String sql = "select distinct gongfdwmc from hetb h order by gongfdwmc";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("gongfdwmc")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	// 收款单位_end

	// 合同编号
	public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getIHetbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHetbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean8() != value) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setIHetbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getIHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {

			getIHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getIHetbhModels() {

		String sql = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			sql = "select id,hetbh from hetb where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " and leib=0 order by hetbh";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			sql = "select id,hetbh from hetb where diancxxb_id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") and leib=0 order by hetbh";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			sql = "select id,hetbh from hetb where leib=0 order by hetbh";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	// 收款单位_end

	// 电厂名称
	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getIDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setDiancmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean9() != value) {
			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean9(value);
			
//			if(getEditValues()!=null){
//				
//				((Balancebean) getEditValues().get(0))
//					.setDiancjsbs(getDiancjsbs(getDiancmcValue().getId()));
//			}
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {

			getIDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getIDiancmcModels() {

		String sql = "select id,quanc from diancxxb";
		String where = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			where = " where id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by quanc";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			where = " where id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") order by quanc";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			where = " where jib=3 order by quanc ";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql + where, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	// 电厂名称_end

	// 流程名称
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getILiucmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setLiucmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getILiucmcModels() {

		String sql = "select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	// 电厂名称_end

	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
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
	
	//工具条_begin
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	//工具条_end
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
}
