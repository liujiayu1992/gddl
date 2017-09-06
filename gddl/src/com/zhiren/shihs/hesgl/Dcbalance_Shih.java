package com.zhiren.shihs.hesgl;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Money;
import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Dcbalance_Shih extends BasePage {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	
	private String _msg;

	public String _liucmc;

	protected void initialize() {
		_msg = "";
		_liucmc = "";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value, false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public void setLiucmc(String _value) {
		_liucmc = _value;
	}

	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}

	public String getTitle() {

		return Locale.jiesd_title;
	}

	public String getJiesslcyText() {

		return Locale.jiesslcy_title;
	}

	private long SaveDiancjsmkb(JDBCcon con) {
		// 存储shihjsb
		String sql = "";
		long Id = 0;

		try {
				Id = Long
						.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));

			sql = "insert into shihjsb	\n"
				+ " (id, diancxxb_id, bianm, shihgysb_id, gongysmc, faz, meiz, daibch, yuanshr, xianshr, fahksrq, fahjzrq, \n"
				+ "	yansksrq, yansjzrq, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, \n"
				+ " bukk, hansje, buhsje, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzbid, jingbr, jingrq, beiz)	\n"
					+ " values ("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ",'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiesbh()
					+ "',"
					+ MainGlobal.getTableId("shihgysb", "quanc",
							((Dcbalancebean_Shih) getEditValues().get(0))
									.getFahdw())
					+ ", '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFahdw()
					+ "','"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFaz()
					+ "','"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getPinz()
					+ "','"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getDaibcc()
					+ "','"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYuanshr()
					+ "','"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getXianshr()
					+ "',"
					+ "to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getFahjzrq())
					+ "','yyyy-MM-dd')"
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYansksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYansjzrq())
					+ "','yyyy-MM-dd'),"
					+ "'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getShoukdw()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getDuifdd()
					+ "',"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiessl()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJingz()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getShulzjbz()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getBukyqjk()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiasje()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiakhj()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiakje()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiaksk()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiaksl()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getBuhsdj()
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong2()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getJiesrq())
					+ "','yyyy-MM-dd'),null,"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getHetb_id()
					+ ",0,0"
					+ ",'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getRanlbmjbrq()) + "','yyyy-MM-dd')"
					+ ",'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getBeiz()
					+ "')";

			if (con.getInsert(sql)>= 0) {

				((Dcbalancebean_Shih) getEditValues().get(0)).setId(Id);

				if (UpdateFahb_Jiesbid(Id)) {
					// 更新车皮表中jiesb_id
					
					return Id;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}

	private boolean UpdateJiesyfb_Diancjsmkbid(long meikjsb_id) {
		// 如果先结算了运费，再结算煤款是要和jiesyfb进行关联
		boolean Flag = false;
		Jiesdcz Jscz = new Jiesdcz();
		JDBCcon con = new JDBCcon();
		String sql = "update jiesyfb set diancjsmkb_id="
				+ meikjsb_id
				+ " where id in("
				+ "select distinct dj.yunfjsb_id\n"
				+ "       from fahb f,chepb c,danjcpb dj,yunfdjb yd\n"
				+ "       where f.id=c.fahb_id\n"
				+ "             and c.id=dj.chepb_id\n"
				+ "             and dj.yunfdjb_id=yd.id\n"
				+ "             and yd.feiylbb_id="
				+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
						.getLong2()) + "\n"
				+ "             and f.jiesb_id in (" + meikjsb_id + "))";

		if (con.getUpdate(sql) >= 0) {

			Flag = true;
		}
		con.Close();
		return Flag;
	}

	private boolean UpdateFahb_Jiesbid(long JiesbId) {
		// 更新发货表jiesb_id
		JDBCcon con = new JDBCcon();
		boolean Flag = false;
		String sql = "update shihcpb set shihjsb_id="+ JiesbId
				+ " where id in ("
				+ ((Visit) getPage().getVisit()).getString1() + ")";
		if (con.getUpdate(sql) >= 0) {

			Flag = true;
		}
		
		con.Close();
		return Flag;
	}

	private boolean UpdateDanjcpb_Jiesyfbid(long JiesyfbId, JDBCcon con) {
		// 单据车皮表yunfjsb_id
		// 如果在结算选择页面中选择“需要核对货票”时说明，yunfdjb和danjcpb中已经有记录
		// 如果在结算选择页面中选择“不需要核对货票”时说明，yunfdjb和danjcpb中都没有记录，要插入新纪录
		boolean Flag = false;
		boolean Hedbz = false; // true 已核对、false 未核对
		Jiesdcz Jscz = new Jiesdcz();
		String yunsdw_contion = "";

		if (((Visit) getPage().getVisit()).getLong9() > -1) {

			yunsdw_contion = " and c.yunsdwb_id="
					+ ((Visit) getPage().getVisit()).getLong9();
		}

		try {
			StringBuffer sbcpid = new StringBuffer();
			String sql = "select c.id from fahb f,chepb c		\n"
					+ " where f.id=c.fahb_id and f.lie_id in ("
					+ ((Visit) getPage().getVisit()).getString1() + ")	\n"
					+ yunsdw_contion;

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				sbcpid.append(rs.getLong("id")).append(",");
			}
			sbcpid.deleteCharAt(sbcpid.length() - 1);

			// 判断yunfdjb和danjcpb中是否存在记录，如果存在Hedbz=true直接更新，如果不存在Hedbz=false;先将数据插入二表，再更新状态
			sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
					+ " and danjcpb.chepb_id in ("
					+ sbcpid.toString()
					+ ") and yunfdjb.feiylbb_id="
					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
							.getLong2()) + "";

			rs = con.getResultSet(sql);
			if (rs.next()) {

				Hedbz = true;
			}

			if (!Hedbz) {

				String Chep[] = sbcpid.toString().split(",");
				StringBuffer sb = new StringBuffer("begin		\n");
				String Yunfdjb_id = "0";

				for (int i = 0; i < Chep.length; i++) {

					Yunfdjb_id = MainGlobal.getNewID(((Visit) getPage()
							.getVisit()).getLong1());
					sb
							.append(
									"insert into yunfdjb(id, danjbh, feiyb_id, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id, ches)	\n")
							.append(" values		\n")
							.append(
									" ("
											+ Yunfdjb_id
											+ ", '', 0, 0, 0, '"
											+ ((Visit) getPage().getVisit())
													.getRenymc()
											+ "', sysdate, ")
							.append(
									"'不需核对货票或汽运费结算', "
											+ Jscz
													.Feiylbb_transition(((Visit) getPage()
															.getVisit())
															.getLong2())
											+ ", 1);	\n");

					sb
							.append(
									"insert into danjcpb(yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl, id)		\n")
							.append(" values		\n").append(
									"("
											+ Yunfdjb_id
											+ ", "
											+ Chep[i]
											+ ", 0, 0, 0, getnewid("
											+ ((Visit) getPage().getVisit())
													.getLong1() + "));	\n");
				}
				sb.append("end;");
				con.getInsert(sb.toString());
			}
			rs.close();

			sql = "update danjcpb set yunfjsb_id="
					+ JiesyfbId
					+ " where chepb_id in ("
					+ sbcpid.toString()
					+ ") 	\n"
					+ " and danjcpb.yunfdjb_id in	\n"
					+ " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
					+ "	and danjcpb.chepb_id in ("
					+ sbcpid.toString()
					+ ") and yunfdjb.feiylbb_id="
					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
							.getLong2()) + ") ";
			if (con.getUpdate(sql) > 0) {

				Flag = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return Flag;
	}

	private long SaveDiancjsyfb(long Meikid, JDBCcon con) {
		// 存储运费表
		String sql = "";
		long Id = 0;
		try {

			if (((Dcbalancebean_Shih) getEditValues().get(0)).getYunfjsb_id() > 0) {

				Id = ((Dcbalancebean_Shih) getEditValues().get(0))
						.getYunfjsb_id();
			} else {

				Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
						.getVisit()).getLong1()));
			}

			sql = " insert into jiesyfb (id,diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, gongfsl, yanssl, jiessl, yingk, guohl, yuns, koud, jiesslcy, guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, diancjsmkb_id, RANLBMJBR, RANLBMJBRQ)"
					+ " values("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ", '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJiesbh()
					+ "', "
					+ MainGlobal.getTableId("shihgysb", "quanc",
							((Dcbalancebean_Shih) getEditValues().get(0))
									.getFahdw())
					+ ", '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFahdw()
					+ "',"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getYunsfsb_id()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYunju()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYingd()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getKuid()
					+ ",'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFaz()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getYuanshr()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getXianshr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYansksrq())
					+ "','yyyy-MM-dd'), to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYansjzrq())
					+ "','yyyy-MM-dd'), '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getShoukdw()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getDuifdd()
					+ "', "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getGongfsl()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYanssl()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getYunfjsl()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getYingksl()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getJingz()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYuns()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getKoud_js()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getJiesslcy()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getTielyf()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getTielzf()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getKuangqyf()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getKuangqzf()
					+ ","
					+ (double) Math
							.round((((Dcbalancebean_Shih) getEditValues()
									.get(0)).getTielzf() + ((Dcbalancebean_Shih) getEditValues()
									.get(0)).getKuangqzf()) * 100)
					/ 100
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getShulzjbz()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getBukyqyzf()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getYunzfhj()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getBuhsyf()
					+ ","
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYunfsk()
					+ ", "
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getYunfsl()
					+ ", 0, "
					+ ((Visit) getPage().getVisit()).getLong2()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getJiesrq())
					+ "','yyyy-MM-dd'), null,"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getHetb_id()
					+ ", 0, 0, '"
					+ ((Dcbalancebean_Shih) getEditValues().get(0)).getBeiz()
					+ "',"
					+ GetMeikjsb_id(Meikid)
					+ ",'"
					+ ((Dcbalancebean_Shih) getEditValues().get(0))
							.getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getRanlbmjbrq()) + "','yyyy-MM-dd'))";

			if (con.getInsert(sql) >= 0) {

				((Dcbalancebean_Shih) getEditValues().get(0)).setYid(Id);
				if (UpdateDanjcpb_Jiesyfbid(Id, con)) {

					return Id;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}

	private long GetMeikjsb_id(long meikjsb_id) throws SQLException {
		// 结算运费时要和结算表进行绑定，即更新jiesyfb.diancjsmkb_id为jiesb.id
		long Jiesb_id = meikjsb_id;
		if (meikjsb_id > 0) {
			// 如果是两票结算，meikjsb_id>0

		} else {

			JDBCcon con = new JDBCcon();
			String sql = "select jiesb_id from fahb where lie_id in ("
					+ ((Visit) getPage().getVisit()).getString1() + ")";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				Jiesb_id = rs.getLong("jiesb_id");
			}
			rs.close();
			con.Close();
		}

		return Jiesb_id;
	}

	private boolean UpdateJieszfb(long id, JDBCcon con) {
		// TODO 自动生成方法存根
		// 更新杂费表中jiesyfb_id字段
		// 当保存jieszf时把该次操作的session存入表中
		// 保存jieszf表时更新session相同的记录
		// JDBCcon con=new JDBCcon();
		String sql = "update jieszfb set jiesyfb_id=" + id
				+ " where serversession='"
				+ ((Visit) getPage().getVisit()).getSession() + "'";
		if (con.getUpdate(sql) >= 0) {

			return true;
		}
		// con.Close();
		return false;
	}

	private String SaveJszbsjb(long Mkid, String mingc, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// 保存结算单中指标数据
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));

			sql = " insert into shihjszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
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
					+ ");	\n";

		} catch (Exception e) {

			e.printStackTrace();
		}
		return sql;
	}

	private boolean Save() {
		// 重要说明：所有结算表未填写 合同id、流程状态id、流程跟踪id、矿方结算id
		String msg = "";
		long Mkid = 0;// 煤款id
		long Yfid = 0;// 运费id
		boolean Flag = false;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		try {

			if (Jiesdcz.CheckHetshzt_Shih(((Dcbalancebean_Shih) getEditValues().get(
					0)).getHetb_id())) {
				// 判断合同的审核状态

				if (getEditValues() != null
						&& !getEditValues().isEmpty()
						&& !((Dcbalancebean_Shih) getEditValues().get(0))
								.getJiesbh().equals("")) {
					if (Jiesdcz.checkbh_Shih(((Dcbalancebean_Shih) getEditValues()
							.get(0)).getJiesbh(), 0, 0)) {

						if (((Visit) getPage().getVisit()).getLong2() == Locale.shihsjs_feiylbb_id) {
							// 单保存石灰石
							if (((Dcbalancebean_Shih) getEditValues().get(0))
									.getId() == 0) {
								// 保存石灰石表
								Mkid = SaveDiancjsmkb(con);

								if (Mkid > 0) {

									if (this.SaveZhib(Mkid, con)) {

										Flag = true;
									}
								}
							}
						} else {
							// 单结算运费
							if (checkXitszDjyf()) {
								// 读系统信息表的设置，看公司系统能不能单结算运费
								Yfid = SaveDiancjsyfb(0, con);

								if (Yfid > 0) {

									Flag = true;
								}

							} else {

								msg = "请选择与该运费对应的煤款结算单";
							}
						}
					} else {

						msg = "结算单编号重复";
					}
				} else {

					msg = "不能保存空结算单";
				}

			} else {

				msg = "合同未审核不能保存！";
			}

			if (Flag) {

				setMsg("结算操作成功！");
				con.commit();

			} else {

				setMsg(msg + " 结算操作失败！");
				con.rollBack();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			con.Close();
		}
		return Flag;
	}

	private boolean SaveZhib(long Mkid, JDBCcon con) {
		// 保存结算单指标
		boolean Flag = false;

		StringBuffer sbsql = new StringBuffer(" begin \n");
//		结算数量
		sbsql
				.append(this.SaveJszbsjb(Mkid, Locale.jiessl_zhibb,
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getHetsl(),
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getGongfsl(),
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getYanssl(),
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getJiessl(),
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getYingksl(),
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getShulzjbz(),
						((Dcbalancebean_Shih) getEditValues().get(0))
								.getShulzjje(), 1));

		if (!((Dcbalancezbbean_Shih) getJieszbValues().get(0)).getCaO_ht()
				.equals("")) {
//			CaO
			sbsql.append(this.SaveJszbsjb(Mkid, Locale.CaO_zhibb,
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_ht(),

					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_kf(),

					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_cf(),

					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_js(),

					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_yk(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_zdj(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getCaO_zje(), 1));
		}

		if (!((Dcbalancezbbean_Shih) getJieszbValues().get(0)).getMgO_ht()
				.equals("")) {
//			MgO
			sbsql.append(this.SaveJszbsjb(Mkid, Locale.MgO_zhibb,
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_ht(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_kf(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_cf(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_js(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_yk(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_zdj(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getMgO_zje(), 1));
		}

		if (!((Dcbalancezbbean_Shih) getJieszbValues().get(0)).getXid_ht()
				.equals("")) {
//			细度
			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Xid_zhibb,
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_ht(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_kf(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_cf(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_js(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_yk(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_zdj(),
					((Dcbalancezbbean_Shih) getJieszbValues().get(0))
							.getXid_zje(), 1));
		}

		sbsql.append(" end; ");

		if (con.getInsert(sbsql.toString()) >= 0) {

			Flag = true;
		}
		return Flag;
	}

	private boolean checkXitszDjyf() {
		// TODO 自动生成方法存根
		// 检查系统设置中的"可单独结算运费"设置
		JDBCcon con = new JDBCcon();
		try {
			String zhi = "";

			String sql = "select zhi from xitxxb where mingc='可单独结算运费'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				zhi = rs.getString("zhi");
			}

			if (zhi.trim().equals("是")) {

				return true;
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return false;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {

		_QuedChick = true;
	}

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {

		_ReturnChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_QuedChick) {
			_QuedChick = false;
			Submit();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Qux(cycle);
		}
	}

	private void Qux(IRequestCycle cycle) {
		// TODO 自动生成方法存根

		cycle.activate("Jiesxz_Shih");
	}

	private void Submit() {
		// TODO 自动生成方法存根

		if (((Dcbalancebean_Shih) getEditValues().get(0)).getId() == 0
				&& ((Dcbalancebean_Shih) getEditValues().get(0)).getYid() == 0) {// 没保存先做保存

			if (Save()
					&& this.getProperId(this.getILiucmcModel(), getLiucmc()) > -1) {
				// 流程方法
				if (((Visit) getPage().getVisit()).getLong2() == 1) {// 两票结算

					Liuc.tij("jiesb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());
					Liuc.tij("jiesyfb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYid(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());

					Jiesdcz
							.SubmitGsDiancjsmkb(((Dcbalancebean_Shih) getEditValues()
									.get(0)).getId());
					Jiesdcz
							.SubmitGsDiancjsyfb(((Dcbalancebean_Shih) getEditValues()
									.get(0)).getYid());

				} else if (((Visit) getPage().getVisit()).getLong2() == 0) {// 煤款结算

					Liuc.tij("jiesb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());

					Jiesdcz
							.SubmitGsDiancjsmkb(((Dcbalancebean_Shih) getEditValues()
									.get(0)).getId());

				} else if (((Visit) getPage().getVisit()).getLong2() == 2) {// 运费结算

					Liuc.tij("jiesyfb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYid(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());

					Jiesdcz
							.SubmitGsDiancjsyfb(((Dcbalancebean_Shih) getEditValues()
									.get(0)).getYid());
				}

			} else if (getProperId(this.getILiucmcModel(), getLiucmc()) == -1) {

				setMsg("请选择流程名称！");
			}

		} else {// 已经保存

			if (this.getProperId(this.getILiucmcModel(), getLiucmc()) > -1) {
				// 流程方法
				if (((Visit) getPage().getVisit()).getLong2() == 1) {// 两票结算

					Liuc.tij("jiesb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());
					Liuc.tij("jiesyfb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYid(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());

				} else if (((Visit) getPage().getVisit()).getLong2() == 0) {// 煤款结算

					Liuc.tij("jiesb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());

				} else if (((Visit) getPage().getVisit()).getLong2() == 2) {// 运费结算

					Liuc.tij("jiesyfb", ((Dcbalancebean_Shih) getEditValues()
							.get(0)).getYid(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getLiucmcValue().getId());
				}

			} else if (getProperId(this.getILiucmcModel(), getLiucmc()) == -1) {

				setMsg("请选择流程名称！");
			}
		}
	}

	private Dcbalancebean_Shih _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public List getJieszbValues() {
		return ((Visit) getPage().getVisit()).getList2();
	}

	public void setJieszbValues(List editList) {
		((Visit) getPage().getVisit()).setList2(editList);
	}

	public Dcbalancebean_Shih getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dcbalancebean_Shih EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		Visit visit = new Visit();
		List _editvalues = new ArrayList();
		List _Jieszbvalues = new ArrayList();

		if (getEditValues() != null) {

			getEditValues().clear();
		}
		if (getJieszbValues() != null) {

			getJieszbValues().clear();
		}

		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			String mtianzdw = MainGlobal.getTableCol("diancxxb", "quanc", "id",
					String.valueOf(((Visit) getPage().getVisit()).getLong1()));
			String mjiesbh = "";
			String mfahdw = "";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq = "";
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

			String mCaO_ht = ""; // 合同CaO
			double mCaO_kf = 0; // 供方CaO
			double mCaO_cf = 0; // 厂方CaO
			double mCaO_js = 0; // 结算CaO
			double mCaO_yk = 0; // 盈亏CaO
			double mCaO_zdj = 0; // 折单价CaO
			double mCaO_zje = 0; // 这金额CaO

			String mMgO_ht = ""; // 合同MgO
			double mMgO_kf = 0; // 供方MgO
			double mMgO_cf = 0; // 厂方MgO
			double mMgO_js = 0; // 结算MgO
			double mMgO_yk = 0; // 盈亏MgO
			double mMgO_zdj = 0; // 折单价MgO
			double mMgO_zje = 0; // 这金额MgO

			String mXid_ht = ""; // 合同细度
			double mXid_kf = 0; // 供方热量
			double mXid_cf = 0; // 厂方热量
			double mXid_js = 0; // 结算热量
			double mXid_yk = 0; // 厂方盈亏
			double mXid_zdj = 0; // 折单价
			double mXid_zje = 0; // 这金额

			long mhetb_id = 0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mkoud_js = 0; // 结算时的扣吨
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf = 0;
			double mkuangqzf = 0;
			double mkuangqsk = 0;
			double mkuangqjk = 0;
			double mjiesslcy = 0;
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
			String mranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = ((Visit) getPage().getVisit()).getLong2();
			double myuns = 0;
			String myunsfs = "";
			long myunsfsb_id = 0;
			String mstrJieszb = "";
			String mErroMessage = "";
			double myingd = 0;
			double mkuid = 0;
			double myunju = 0; // 运距

			// 进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
			// 结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id = 0;
			long mYunfjsb_id = 0;
			long mJihkjb_id = 0;

			// mkuangqyf=((Visit) getPage().getVisit()).getDouble2(); //矿区运费
			// mkuangqzf=((Visit) getPage().getVisit()).getDouble3(); //矿区杂费
			// mkuangqsk=((Visit) getPage().getVisit()).getDouble4(); //矿区税款
			// mkuangqjk=((Visit) getPage().getVisit()).getDouble5(); //矿区价款

			// ((Visit) getPage().getVisit()).getLong1() //电厂信息表id
			// ((Visit) getPage().getVisit()).getString1() //列Id
			// ((Visit) getPage().getVisit()).getLong2() //结算类型
			// ((Visit) getPage().getVisit()).getString2() //是否需要结算指标调整（是、否）
			// ((Visit) getPage().getVisit()).getString4() //验收编号
			// ((Visit) getPage().getVisit()).getLong3() //发货单位表id
			// ((Visit) getPage().getVisit()).getLong8() //合同表id
			// ((Visit) getPage().getVisit()).getLong9() //运输单位id
			// ((Visit) getPage().getVisit()).getString7() //结算扣吨量

			Balances_Shih bls = new Balances_Shih();
			Balances_variable_Shih bsv = new Balances_variable_Shih(); // Balances变量
			// bsv.setKuangqyf(mkuangqyf);
			// bsv.setKuangqzf(mkuangqzf);
			// bsv.setKuangqsk(mkuangqsk);
			// bsv.setKuangqjk(mkuangqjk);
			bls.setBsv(bsv);

			bls.getBalanceData(
					((Visit) getPage().getVisit()).getString1(),
					((Visit) getPage().getVisit()).getLong1(),
					((Visit) getPage().getVisit()).getLong2(),
					Double.parseDouble(((Visit) getPage().getVisit())
									.getString7()));

			bsv = bls.getBsv();
			mErroMessage = bsv.getErroInfo();
			mjiesbh = bsv.getJiesbh();
			mfahdw = bsv.getFahdw();
			mfaz = bsv.getFaz();
			mshoukdw = bsv.getShoukdw();
			mfahksrq = bsv.getFahksrq();
			mfahjzrq = bsv.getFahjzrq();
			mfahrq = bsv.getFahrq();
			myansksrq = bsv.getYansksrq();
			myansjzrq = bsv.getYansjsrq();
			mkaihyh = bsv.getKaihyh();
			mpinz = bsv.getRanlpz();
			mzhangh = bsv.getZhangh();
			mhetsl = String.valueOf(bsv.getHetml());
			mgongfsl = bsv.getGongfsl();
			mches = bsv.getChes();
			myansbh = bsv.getYansbh();
			mshulzjbz = bsv.getHansmj();
			myanssl = bsv.getYanssl();
			myingksl = bsv.getYingksl();
			mshulzjje = bsv.getShul_zje();
			mhetb_id = bsv.getHetb_Id();
			mdaibcc = bsv.getDaibcc();
			mkoud_js = bsv.getKoud_js();
			myunsfsb_id = bsv.getYunsfsb_id();

			// 指标_Begin
			mCaO_ht = bsv.getCaO_ht();
			mCaO_kf = bsv.getCaO_kf();
			mCaO_cf = bsv.getCaO_cf();
			mCaO_js = bsv.getCaO_js();
			mCaO_yk = bsv.getCaO_yk();
			mCaO_zdj = bsv.getCaO_zdj();
			mCaO_zje = bsv.getCaO_zje();

			mMgO_ht = bsv.getMgO_ht();
			mMgO_kf = bsv.getMgO_kf();
			mMgO_cf = bsv.getMgO_cf();
			mMgO_js = bsv.getMgO_js();
			mMgO_yk = bsv.getMgO_yk();
			mMgO_zdj = bsv.getMgO_zdj();
			mMgO_zje = bsv.getMgO_zje();

			mXid_ht = bsv.getXid_ht();
			mXid_kf = bsv.getXid_kf();
			mXid_cf = bsv.getXid_cf();
			mXid_js = bsv.getXid_js();
			mXid_yk = bsv.getXid_yk();
			mXid_zdj = bsv.getXid_zdj();
			mXid_zje = bsv.getXid_zje();

			// 指标_End

			mjiessl = bsv.getJiessl();
			mbuhsdj = bsv.getBuhsmj();
			mjiakje = bsv.getJine();
			mjiakhj = bsv.getJiakhj();
			mjiaksl = bsv.getMeiksl();
			mjiaksk = bsv.getJiaksk();
			mjiasje = bsv.getJiashj();
			mtielyf = bsv.getTielyf();
			mtielzf = bsv.getTielzf();
			// 矿区运费、杂费
			mkuangqyf = bsv.getKuangqyf();
			mkuangqzf = bsv.getKuangqzf();

			mjiesslcy = bsv.getJiesslcy();
			mbuhsyf = bsv.getBuhsyf();
			myunfsk = bsv.getYunfsk();
			myunzfhj = bsv.getYunzfhj();
			mhej = bsv.getHej();
			Money mn = new Money();
			mdaxhj = mn.NumToRMBStr(mhej);
			mmeikhjdx = mn.NumToRMBStr(bsv.getJiashj());
			myunzfhjdx = mn.NumToRMBStr(bsv.getYunzfhj());
			mbeiz = bsv.getBeiz();
			mjingz = bsv.getJingz();
			mhetjg = bsv.getHetmdj();
			myuns = bsv.getYuns();
			myunsfs = bsv.getYunsfs();
			mJihkjb_id = bsv.getJihkjb_id();
			mMeikjsb_id = bsv.getMeikjsb_id();
			mYunfjsb_id = bsv.getYunfjsb_id();

//			((Visit) getPage().getVisit()).setLong4(bsv.getMeikxxb_Id()); // 煤矿信息表id
//			((Visit) getPage().getVisit()).setLong5(bsv.getFaz_Id()); // 发站id
//			((Visit) getPage().getVisit()).setLong6(bsv.getDaoz_Id()); // 到站id
//			((Visit) getPage().getVisit()).setDouble1(bsv.getGongfsl()); // 供方数量

			Jiesdcz Jsdcz = new Jiesdcz();

			if (!mCaO_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("CaO(%)", "CaO_ht",
						"CaO_kf", "CaO_cf", "CaO_js", "CaO_yk",
						"CaO_zdj", "CaO_zje", mCaO_ht, mCaO_kf, mCaO_cf, 
						mCaO_js, mCaO_yk, mCaO_zdj, mCaO_zje);
			}

			if (!mMgO_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("MgO(%)", "MgO_ht", "MgO_kf",
						"MgO_cf", "MgO_js", "MgO_yk", "MgO_zdj", "MgO_zje", 
						mMgO_ht,mMgO_kf, mMgO_cf, mMgO_js, mMgO_yk, mMgO_zdj, mMgO_zje);
			}

			if (!mXid_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("细度(%)", "Xid_ht", "Xid_kf",
						"Xid_cf", "Xid_js", "Xid_yk", "Xid_zdj", "Xid_zje",
						mXid_ht, mXid_kf, mXid_cf, mXid_js, mXid_yk, mXid_zdj,mXid_zje);
			}

			setJieszb(mstrJieszb);

			if (!mErroMessage.equals("")) {

				this.setMsg(mErroMessage);
			} else if ((((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id || ((Visit) getPage()
					.getVisit()).getLong2() == Locale.meikjs_feiylbb_id)
					&& mshulzjbz == 0) {

				this.setMsg("合同设置有误请检查");
			}

			_editvalues.add(new Dcbalancebean_Shih(mid, myid, mtianzdw,
					mjiesbh, mfahdw, mfaz, myunsfsb_id, mshoukdw, mfahksrq,
					mfahjzrq, myansksrq, myansjzrq, mkaihyh, mpinz, myuanshr,
					mzhangh, mhetsl, mgongfsl, mches, mxianshr, mfapbh,
					mdaibcc, myansbh, mduifdd, mfukfs, mshulzjbz, myanssl,
					myingksl, myingd, mkuid, mshulzjje, mjiessl, mjiesslcy,
					myunfjsl, mbuhsdj, mjiakje, mbukyqjk, mjiakhj, mjiaksl,
					mjiaksk, mjiasje, mtielyf, mtielzf, mkuangqyf, mkuangqzf,
					mkuangqsk, mkuangqjk, mbukyqyzf, mjiskc, mbuhsyf, myunfsl,
					myunfsk, myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj,
					mbeiz, mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq, mjieszxjbr,
					mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq, mhetjg, mjieslx,
					myuns, mkoud_js, myunsfs, mhetb_id, myunju, mMeikjsb_id,
					mYunfjsb_id, mJihkjb_id));

			_Jieszbvalues
					.add(new Dcbalancezbbean_Shih(
							mCaO_ht, mMgO_ht, mXid_ht,
							mCaO_kf, mMgO_kf, mXid_kf,
							mCaO_cf, mMgO_cf, mXid_cf,
							mCaO_js, mMgO_js, mXid_js,
							mCaO_yk, mMgO_yk, mXid_yk,
							mCaO_zdj, mMgO_zdj, mXid_zdj,
							mCaO_zje, mMgO_zje, mXid_zje));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Dcbalancebean_Shih());
		}
		setEditValues(_editvalues);
		this.setJieszbValues(_Jieszbvalues);
		return getEditValues();
	}

	public void setJieszb(String value) {

		((Visit) getPage().getVisit()).setString8(value);
	}

	public String getJieszb() {

		return ((Visit) getPage().getVisit()).getString8();
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		// 返回
		ToolbarButton quxbt = new ToolbarButton(null, "返回",
				"function(){ document.Form0.ReturnButton.click();}");
		quxbt.setId("fanhbt");
		tb1.addItem(quxbt);
		tb1.addText(new ToolbarText("-"));
		//		
		if (((Dcbalancebean_Shih) getEditValues().get(0)).getId() == 0
				|| ((Dcbalancebean_Shih) getEditValues().get(0)).getYid() == 0) {

			// 保存
			ToolbarButton savebt = new ToolbarButton(null, "保存",
					"function(){ document.Form0.SaveButton.click(); }");
			savebt.setId("savebt");
			tb1.addItem(savebt);

			// if(((Visit) getPage().getVisit()).getLong2()==1||((Visit)
			// getPage().getVisit()).getLong2()==3){
			//				
			// tb1.addText(new ToolbarText("-"));
			//				
			// ToolbarButton kuangqzfbt=new
			// ToolbarButton(null,"矿区杂费录入","function(){
			// document.Form0.KuangqzfButton.click(); }");
			// kuangqzfbt.setId("kuangqzfbt");
			// tb1.addItem(kuangqzfbt);
			// }

		} else if (((Dcbalancebean_Shih) getEditValues().get(0)).getId() > 0
				|| ((Dcbalancebean_Shih) getEditValues().get(0)).getYid() > 0) {

			// 提交进入流程
			ToolbarButton submitbt = new ToolbarButton(
					null,
					"提交进入流程",
					"function(){  \n"
							+ " if(!win){	\n"
							+ "	\tvar form = new Ext.form.FormPanel({	\n"
							+ " \tbaseCls: 'x-plain',	\n"
							+ " \tlabelAlign:'right',	\n"
							+ " \tdefaultType: 'textfield',	\n"
							+ " \titems: [{		\n"
							+ " \txtype:'fieldset',	\n"
							+ " \ttitle:'请选择流程名称',	\n"
							+ " \tautoHeight:false,	\n"
							+ " \theight:220,	\n"
							+ " \titems:[	\n"
							+ " \tlcmccb=new Ext.form.ComboBox({	\n"
							+ " \twidth:150,	\n"
							+ " \tselectOnFocus:true,	\n"
							+ "	\ttransform:'LIUCMCSelect',	\n"
							+ " \tlazyRender:true,	\n"
							+ " \tfieldLabel:'流程名称',		\n"
							+ " \ttriggerAction:'all',	\n"
							+ " \ttypeAhead:true,	\n"
							+ " \tforceSelection:true,	\n"
							+ " \teditable:false	\n"
							+ " \t})	\n"
							+ " \t]		\n"
							+ " \t}]	\n"
							+ " \t});	\n"
							+ " \twin = new Ext.Window({	\n"
							+ " \tel:'hello-win',	\n"
							+ " \tlayout:'fit',	\n"
							+ " \twidth:500,	\n"
							+ " \theight:300,	\n"
							+ " \tcloseAction:'hide',	\n"
							+ " \tplain: true,	\n"
							+ " \ttitle:'流程',	\n"
							+ " \titems: [form],	\n"
							+ " \tbuttons: [{	\n"
							+ " \ttext:'确定',	\n"
							+ " \thandler:function(){	\n"
							+ " \twin.hide();	\n"
							+ " \tif(lcmccb.getRawValue()=='请选择'){		\n"
							+ "	\t	alert('请选择流程名称！');		\n"
							+ " \t}else{"
							+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
							+ " \t\t document.all.item('QuedButton').click();	\n"
							+ " \t}	\n"
							+ " \t}	\n"
							+ " \t},{	\n"
							+ " \ttext: '取消',	\n"
							+ " \thandler: function(){	\n"
							+ " \twin.hide();	\n"
							+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"
							+ " \t}		\n"
							+ " \t}]	\n"
							+ " \t});}	\n"
							+ " \twin.show(this);	\n"

							+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"
							// + "
							// \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);
							// \n"
							+ " \t}	\n" + " \t}");
			submitbt.setId("submitbt");
			tb1.addItem(submitbt);
		}

		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);

//			电厂Id
//			((Visit) getPage().getVisit()).setLong1
//			车皮
//			((Visit) getPage().getVisit()).setString1
//			类型
//			((Visit) getPage().getVisit()).setLong2
//			扣吨量
//			((Visit) getPage().getVisit()).getString7
//			合同表id
//			((Visit) getPage().getVisit()).setLong8
			// visit.setLong4(0); //煤矿信息表id
			// visit.setLong5(0); //发站
			// visit.setLong6(0); //到站
			// visit.setLong7(0); //结算运费表id
			// visit.setLong8(0); //合同表id
			// visit.setLong9(0); //运输单位id
			// visit.setDouble1(0); //总票重

			((Visit) getPage().getVisit()).setDropDownBean1(null); // zhibb
			((Visit) getPage().getVisit()).setProSelectionModel1(null);

			((Visit) getPage().getVisit()).setDropDownBean3(null); // 供应商
			((Visit) getPage().getVisit()).setProSelectionModel3(null);

			((Visit) getPage().getVisit()).setDropDownBean4(null); // 车站
			((Visit) getPage().getVisit()).setProSelectionModel4(null);

			((Visit) getPage().getVisit()).setDropDownBean6(null); // 品种
			((Visit) getPage().getVisit()).setProSelectionModel6(null);

			((Visit) getPage().getVisit()).setDropDownBean7(null); // 收款单位
			((Visit) getPage().getVisit()).setProSelectionModel7(null);

			((Visit) getPage().getVisit()).setString8(""); // 结算指标页面显示值

			((Visit) getPage().getVisit()).setLong4(0); // 煤矿信息表id
			((Visit) getPage().getVisit()).setLong5(0); // 发站
			((Visit) getPage().getVisit()).setLong6(0); // 到站
			((Visit) getPage().getVisit()).setLong7(0); // 结算运费表id
			((Visit) getPage().getVisit()).setDouble1(0); // 总票重
			((Visit) getPage().getVisit()).setString11(""); // 用于Kuangqzf返回时，跳转到正确的界面(DCBalance,Jiesdxg)

			setLiucmcValue(null);// 10
			setILiucmcModel(null);// 10
			getILiucmcModels();

			getIZhibbmModels();
			getIShoukdwModels();
			((Visit) getPage().getVisit()).setboolean1(false); // 合同号
			((Visit) getPage().getVisit()).setboolean2(false); // 填制单位
			getSelectData();
		}
		getToolbars();
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
		String sql = "select id,bianm from zhibb order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, ""));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// 指标表编码Model_end

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
			String sql = " select shoukdw from (	\n"
					+ " select distinct gongfdwmc as shoukdw from hetb h	\n"
					+ " union	\n"
					+ " select distinct quanc as shoukdw from shoukdw	\n"
					+ " ) order by shoukdw";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("shoukdw")));
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

	// 工具条_begin

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	// 工具条_end
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
