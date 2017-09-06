package com.zhiren.dc.jilgl.gongl.jianj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：王磊
 * 时间：2009-07-06 19：13
 * 描述：数据保存时对核对标志的更新错误
 */

/*
 * 作者：ww
 * 时间：2010-01-20
 * 描述：添加日期查询方便查询数据，默认不显示日期查询条件
 * 		参数配置
 * 		INSERT INTO xitxxb VALUES (
 getnewid(diancID),       -- diancID  电厂ID
 1,
 diancID,                 -- diancID  电厂ID
 '检斤修改显示日期',
 '是',                    --是 显示日期条件，否 不显示日期条件
 '',
 '数量',
 1,
 '使用'
 )
 */
public class Jianjxg extends BasePage implements PageValidateListener {
	// 界面用户提示
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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// 绑定日期
	private String riq;

	public String getRiq() {
		if (riq == null || "".equals(riq)) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		long diancxxb_id;
		int yunsfsb_id = 2;
		List fhlist = new ArrayList();
		ResultSetList rsld = getExtGrid().getDeleteResultSet(getChange());
		if (rsld == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Jianjxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 删除车皮
		while (rsld.next()) {
			String fahbid = rsld.getString("fahbid");
			Jilcz.addFahid(fhlist, fahbid);
			String id = rsld.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_DEL,
					SysConstant.RizOpMokm_Jianjxg, "chepb", id);
			String sql = "delete from chepb where id =" + id;
			int flag = con.getDelete(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.Jianjxg001);
				return;
			}
		}
		// 修改车皮
		rsld = getExtGrid().getModifyResultSet(getChange());
		if (rsld == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Jianjxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsld.next()) {
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsld.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			String chepid = rsld.getString("id");
			StringBuffer sb = new StringBuffer();
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Jianjxg, "chepb", chepid);
			sb.append("update chepb set biaoz=")
					.append(rsld.getDouble("biaoz")).append(",koud=").append(
							rsld.getDouble("koud")).append(",kous=").append(
							rsld.getDouble("kous")).append(",kouz=").append(
							rsld.getDouble("kouz")).append(",zongkd=").append(
							this.isZongkdBfb() ? (rsld.getDouble("koud")
									+ rsld.getDouble("kous") + rsld
									.getDouble("kouz")) : (rsld
									.getDouble("koud")
									+ rsld.getDouble("kous") + rsld
									.getDouble("kouz")));// 原系统zongkd就是koud项
															// 调整他们之和
			sb.append(",sanfsl=").append(rsld.getDouble("sanfsl")).append(",");
			sb.append("meicb_id=").append(
					(getExtGrid().getColumn("meicb_id").combo).getBeanId(rsld
							.getString("meicb_id"))).append(",");

			// 增加卸车方式后，得更新chepb。
			sb.append("xiecfsb_id=").append(
					(getExtGrid().getColumn("xiecfsb_id").combo).getBeanId(rsld
							.getString("xiecfsb_id"))).append(",");

			sb.append("yunsdwb_id=").append(
					(getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsld
							.getString("yunsdwb_id"))).append(",");

			sb.append("zhuangcdw_item_id=").append(
					(getExtGrid().getColumn("zhuangcdw_item_id").combo)
							.getBeanId(rsld.getString("zhuangcdw_item_id")))
					.append(",");
			sb.append("cheph='").append(rsld.getString("cheph")).append("',");
			sb.append("beiz ='").append(rsld.getString("beiz"));
			sb.append("' where id=").append(chepid);
			int flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb);
				setMsg(ErrorMessage.Jianjxg002);
				return;
			}
			String yuanfhid = rsld.getString("fahbid");
			sb.delete(0, sb.length());
			sb.append("select f.id fahbid\n");
			sb
					.append("from fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,vwyuanshdw d\n");
			sb.append("where f.fahrq = to_date('").append(
					rsld.getString("fahrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.daohrq = to_date('").append(
					rsld.getString("daohrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.chec ='").append(rsld.getString("chec")).append(
					"' and g.mingc='").append(rsld.getString("gongysb_id"))
					.append("'\n");
			sb.append("and m.mingc ='").append(rsld.getString("meikxxb_id"))
					.append("' and p.mingc = '").append(
							rsld.getString("pinzb_id")).append("'\n");
			sb.append("and j.mingc = '").append(rsld.getString("jihkjb_id"))
					.append("' and d.mingc = '").append(
							rsld.getString("yuanshdwb_id")).append("'\n");
			sb
					.append(
							"and f.yuandz_id = 1 and f.faz_id = 1 and f.daoz_id = 1 and f.diancxxb_id = ")
					.append(diancxxb_id).append("\n");
			sb
					.append("and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id\n");
			sb
					.append(
							"and f.jihkjb_id=j.id and f.yuanshdwb_id=d.id and f.yunsfsb_id = ")
					.append(yunsfsb_id);
			ResultSetList r = con.getResultSetList(sb.toString());
			if (r == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:"
						+ sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			if (r.next()) {
				if (!yuanfhid.equals(r.getString("fahbid"))) {
					String sql = "update chepb set fahb_id ="
							+ r.getString("fahbid") + " where id=" + chepid;
					flag = con.getUpdate(sql);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail
								+ "SQL:" + sb);
						setMsg(ErrorMessage.Jianjxg003);
						return;
					}
					Jilcz.addFahid(fhlist, r.getString("fahbid"));
				}
			} else {
				// 新增发货
				String newFhid = MainGlobal.getNewID(diancxxb_id);

				long meikxxb_id = getExtGrid().getColumn("meikxxb_id").combo
						.getBeanId(rsld.getString("meikxxb_id"));
				long pinzb_id = getExtGrid().getColumn("pinzb_id").combo
						.getBeanId(rsld.getString("pinzb_id"));
				double yunsl = Jilcz.getYunsl(diancxxb_id, pinzb_id,
						yunsfsb_id, meikxxb_id);
				sb.delete(0, sb.length());
				sb.append("insert into fahb (id,");
				sb.append("yuanid, diancxxb_id, gongysb_id, meikxxb_id,");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq,");
				sb.append("daohrq, zhilb_id, yunsfsb_id,");
				sb.append("chec,yuandz_id, yuanshdwb_id, yunsl) values(");
				sb.append(newFhid).append(",").append(newFhid);
				sb.append(",").append(diancxxb_id).append(",");
				sb.append(
						getExtGrid().getColumn("gongysb_id").combo
								.getBeanId(rsld.getString("gongysb_id")))
						.append(",");
				sb.append(meikxxb_id).append(",");
				sb.append(pinzb_id).append(",");
				sb.append("1,1,");
				sb.append(
						getExtGrid().getColumn("jihkjb_id").combo
								.getBeanId(rsld.getString("jihkjb_id")))
						.append(",");
				sb.append("to_date('").append(rsld.getString("fahrq")).append(
						"','yyyy-mm-dd'),");
				sb.append("to_date('").append(rsld.getString("daohrq")).append(
						"','yyyy-mm-dd'),");
				sb.append(rsld.getInt("zhilb_id")).append(",");
				sb.append(yunsfsb_id).append(",");
				sb.append("'").append(rsld.getString("chec")).append("',");
				sb.append("1,");
				sb.append(
						getExtGrid().getColumn("yuanshdwb_id").combo
								.getBeanId(rsld.getString("yuanshdwb_id")))
						.append(",");
				sb.append(yunsl).append(")");

				flag = con.getInsert(sb.toString());
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+ "SQL:" + sb);
					setMsg(ErrorMessage.Jianjxg003);
					return;
				}
				String sql = "update chepb set fahb_id =" + newFhid
						+ " where id=" + chepid;
				flag = con.getUpdate(sql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Jianjxg004);
					setMsg(ErrorMessage.Jianjxg004);
					return;
				}
				Jilcz.addFahid(fhlist, newFhid);

			}
			flag = Jilcz.CountChepbYuns(con, chepid, -1);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jianjxg005);
				setMsg(ErrorMessage.Jianjxg005);
				return;
			}
			Jilcz.addFahid(fhlist, yuanfhid);
		}
		for (int i = 0; i < fhlist.size(); i++) {
			int flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jianjxg006);
				setMsg(ErrorMessage.Jianjxg006);
				return;
			}
			flag = Jilcz.updateLieid(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jianjxg007);
				setMsg(ErrorMessage.Jianjxg007);
				return;
			}

		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		setMsg("保存成功");
	}

	/*
	 * 作者:童忠付 时间:2009-4-13 修改内容:增加一场多制时电厂id的过滤
	 * 
	 */
	private String filterDcid(Visit v) {

		String sqltmp = " (" + v.getDiancxxb_id() + ")";
		if (v.isFencb()) {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select id from diancxxb where fuid="
							+ v.getDiancxxb_id());
			sqltmp = "";
			while (rsl.next()) {
				sqltmp += "," + rsl.getString("id");
			}
			sqltmp = "(" + sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
	}

	/*
	 * 作者:车必达 时间:2009-11-19 修改内容:1.增加参数，判断卸车方式煤场是否可为空，默认不可为空 设置下面的参数可为空。 insert
	 * into xitxxb (id,xuh,diancxxb_id,mingc,zhi,leib,zhuangt,beiz)
	 * values(getnewid(diancxxb_id),1,diancxxb_id,'卸车方式煤场是否可为空'
	 * ,'是','数量',1,'使用') 2.增加装车单位字段。
	 */

	private boolean isnull() {
		boolean editor = false;
		Visit visit = (Visit) getPage().getVisit();
		editor = "是".equals(MainGlobal.getXitxx_item("数量", "卸车方式煤场是否可为空", ""
				+ visit.getDiancxxb_id(), "否"));
		return editor;
	}

	/*
	 * 作者:童忠付 时间:2009-4-3 修改内容:把 f.chec, x.mingc xiecfsb_id(车次，卸运方式字段放到 其他量 字段后
	 * 显示)， 并且增加按车号模糊查询功能
	 */

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 判断是否显示日期
		boolean blnShowRiq = false;
		blnShowRiq = "是".equals(MainGlobal.getXitxx_item("数量", "检斤修改显示日期", ""
				+ ((Visit) getPage().getVisit()).getDiancxxb_id(), "否"));

		StringBuffer sb = new StringBuffer();
		// sb.append("select c.id,f.id fahbid,f.zhilb_id, f.chec, x.mingc
		// xiecfsb_id, c.cheph, d.mingc diancxxb_id, g.mingc gongysb_id, m.mingc
		// meikxxb_id, p.mingc pinzb_id,\n");
		sb
				.append("select c.id,f.id fahbid,f.zhilb_id, c.cheph, d.mingc diancxxb_id, g.mingc gongysb_id, m.mingc meikxxb_id, p.mingc pinzb_id,\n");
		// sb.append("f.fahrq, f.daohrq, c.maoz,c.piz, c.biaoz, c.koud,
		// c.sanfsl, mc.mingc meicb_id, j.mingc jihkjb_id, \n");
		sb
				.append("f.fahrq, f.daohrq, c.maoz,c.piz, c.biaoz, c.koud,c.kous,c.kouz, c.sanfsl,x.mingc xiecfsb_id,nvl(mc.mingc,(select mingc from meicb where rownum = 1)) meicb_id,f.chec,  "
						+ " j.mingc jihkjb_id, \n");
		sb
				.append("decode(c.yunsdwb_id,-1,(select mingc from  yunsdwb where rownum=1),y.mingc) yunsdwb_id,");
		sb
				.append("nvl(vs.mingc,(select mingc from vwyuanshdw where rownum=1)) yuanshdwb_id,i.mingc zhuangcdw_item_id, c.beiz\n");
		sb
				.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j, meicb mc, yunsdwb y, diancxxb d,vwyuanshdw vs,xiecfsb x,item i\n");
		sb
				.append("where f.id = c.fahb_id and f.yunsfsb_id=2 and c.hedbz <3 \n");
		sb
				.append("and f.gongysb_id = g.id and f.meikxxb_id = m.id and f.diancxxb_id = d.id \n");
		// sb.append(Jilcz.filterDcid(visit, "f"));
		sb.append("and f.diancxxb_id in " + this.filterDcid(visit) + "\n");
		sb
				.append(" and f.pinzb_id = p.id and f.jihkjb_id = j.id and f.yuanshdwb_id = vs.id(+) \n");
		// 是否显示日期(发货日期)
		if (blnShowRiq) {
			sb.append(" AND f.fahrq = to_date('" + this.getRiq()
					+ "','yyyy-mm-dd') \n");
		}
		sb
				.append("and c.meicb_id = mc.id(+) and c.yunsdwb_id = y.id(+)  and c.xiecfsb_id=x.id(+) and c.zhuangcdw_item_id=i.id(+) order by c.zhongcsj desc\n");
		// System.out.println(sb);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		egu.addPaging(23);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("fahbid").editor = null;
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").editor = null;
		// egu.getColumn("chec").setHidden(true);
		// egu.getColumn("chec").editor=null;
		egu.getColumn("yuanshdwb_id").setHidden(true);
		egu.getColumn("yuanshdwb_id").editor = null;
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		if (visit.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql = "select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		} else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setEditor(null);
		}
		// 新加的车次和卸车方式
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("xiecfsb_id").setHeader(Locale.xiecfs_chepb);
		egu.getColumn("xiecfsb_id").setWidth(70);

		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(50);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(50);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setWidth(50);
		// egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setWidth(50);
		// egu.getColumn("sanfsl").setEditor(null);
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(80);

		// egu.getColumn("meicb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("zhuangcdw_item_id").setHeader("装车单位");
		egu.getColumn("zhuangcdw_item_id").setWidth(80);
		egu.getColumn("zhuangcdw_item_id").setEditor(null);

		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(80);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(70);
		egu.getColumn("kous").setHeader("扣水");
		egu.getColumn("kouz").setHeader("扣杂");
		egu.getColumn("kous").setWidth(50);
		egu.getColumn("kouz").setWidth(50);

		if (!con
				.getHasIt(" select * from xitxxb where mingc='检斤修改扣水扣杂是否显示' and leib='数量' and zhi='是' and zhuangt=1 and diancxxb_id="
						+ visit.getDiancxxb_id())) {

			egu.getColumn("kous").setHidden(true);
			egu.getColumn("kouz").setHidden(true);
		}

		// 设置供应商下拉框
		ComboBox cgys = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cgys);
		cgys.setEditable(true);
		String gysSql = "select id,mingc from gongysb where leix=1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gysSql));

		// 卸车方式下拉框
		ComboBox xcfs = new ComboBox();
		egu.getColumn("xiecfsb_id").setEditor(xcfs);
		xcfs.setEditable(true);
		String xcfsSql = "select id,mingc from xiecfsb order by mingc";
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(xcfsSql));

		// 设置煤矿单位下拉框
		ComboBox cmk = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql = "select id,mingc from meikxxb where zhuangt=1 order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(mkSql));
		// 设置品种下拉框
		ComboBox cpz = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cpz);
		cpz.setEditable(true);
		String pinzSql = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		// 设置口径下拉框
		ComboBox ckj = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(ckj);
		ckj.setEditable(true);
		String jihkjSql = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
		// 运输单位
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="
				+ visit.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwSql));
		// 煤场
		ComboBox cmc = new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = "select id, mingc from meicb";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cmcSql));
		if (this.isnull()) {
			egu.getColumn("xiecfsb_id").editor.allowBlank = true;
			egu.getColumn("meicb_id").editor.allowBlank = true;
		}
		// 装车单位
		ComboBox zhuangcdw = new ComboBox();
		egu.getColumn("zhuangcdw_item_id").setEditor(zhuangcdw);
		zhuangcdw.setEditable(true);
		String zhuangcdwSql = "select id ,mingc from item i where i.itemsortid=(select id from itemsort where bianm='ZHUANFGCDW')";
		egu.getColumn("zhuangcdw_item_id").setComboEditor(egu.gridId,
				new IDropDownModel(zhuangcdwSql));
		egu.getColumn("zhuangcdw_item_id").editor.allowBlank = true;

		ComboBox cshdw = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(cshdw);
		cshdw.setEditable(true);// 设置可输入
		String Sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(
				"" + ((Visit) getPage().getVisit()).getDiancmc());

		// 添加发货日期查询，需配置参数
		if (blnShowRiq) {
			egu.addTbarText("发货日期:");
			DateField dfb = new DateField();
			dfb.setValue(getRiq());
			dfb.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
			dfb.setId("RIQ");
			egu.addToolbarItem(dfb.getScript());

			// append(MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...",
			// 200))
			ToolbarButton rbtn = new ToolbarButton(null, "刷新", "function() {"
					+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...",
							200)
					+ " document.getElementById('RefurbishButton').click();}");
			rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addToolbarItem(rbtn.getScript());
			egu.addTbarText("-");
		}

		egu.addTbarText("输入车号：");
		TextField theKey = new TextField();
		theKey.setId("theKey");

		theKey
				.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13) {chaxun();}}\n");
		egu.addToolbarItem(theKey.getScript());
		// 这是ext中的第二个egu，其中带有gridDiv字样的变量都比第一个多Piz字样，gridDiv----gridDivPiz.
		GridButton chazhao = new GridButton("（模糊）查找/查找下一个",
				"function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);
		egu.addTbarText("-");

		String otherscript = " var sta='';function chaxun(){\n"
				+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"
				+ "       var len=gridDiv_data.length;\n"
				+ "       var count;\n"
				+ "       if(len%"
				+ egu.getPagSize()
				+ "!=0){\n"
				+ "        count=parseInt(len/"
				+ egu.getPagSize()
				+ ")+1;\n"
				+ "        }else{\n"
				+ "          count=len/"
				+ egu.getPagSize()
				+ ";\n"
				+ "        }\n"
				+ "        for(var i=0;i<count;i++){\n"
				+ "           gridDiv_ds.load({params:{start:i*"
				+ egu.getPagSize()
				+ ", limit:"
				+ egu.getPagSize()
				+ "}});\n"
				+ "           var rec=gridDiv_ds.getRange();\n "
				+ "           for(var j=0;j<rec.length;j++){\n "
				+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
				+ "                 var nw=[rec[j]]\n"
				+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
				+ "                      gridDiv_sm.selectRecords(nw);\n"
				+ "                      sta+=rec[j].get('ID').toString()+';';\n"
				+ "                       return;\n" + "                  }\n"
				+ "                \n" + "               }\n"
				+ "           }\n" + "        }\n" + "        if(sta==''){\n"
				+ "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"
				+ "        }else{\n" + "           sta='';\n"
				+ "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"
				+ "         }\n" + "      }\n";

		egu.addOtherScript(otherscript);

		boolean kougFlag = false;
		String kouggs = "KOUD";
		rsl = con
				.getResultSetList(" select zhi from xitxxb where  mingc='扣矸是百分比录入'  and leib='数量' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			kougFlag = true;
			kouggs = rsl.getString("zhi");
		}

		boolean kousFlag = false;
		String kousgs = "KOUS";
		rsl = con
				.getResultSetList(" select zhi from xitxxb where  mingc='扣水是百分比录入'  and leib='数量' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			kousFlag = true;
			kousgs = rsl.getString("zhi");
		}

		boolean kouzFlag = false;
		String kouzgs = "KOUZ";
		rsl = con
				.getResultSetList(" select zhi from xitxxb where  mingc='扣杂是百分比录入'  and leib='数量' and zhuangt=1 and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			kouzFlag = true;
			kouzgs = rsl.getString("zhi");
		}

		if (kougFlag || kousFlag || kouzFlag) {
			egu
					.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){ "
							+

							" var rec=e.record;\n"
							+ " var MAOZ=rec.get('MAOZ');\n"
							+ " var PIZ=rec.get('PIZ');\n"
							+ " var KOUD=rec.get('KOUD');\n"
							+ " var KOUS=rec.get('KOUS');\n"
							+ " var KOUZ=rec.get('KOUZ');"
							+

							" if(KOUD==null || KOUD==''){KOUD=0;}\n"
							+ " if(KOUS==null || KOUS==''){KOUS=0;}\n"
							+ " if(KOUZ==null || KOUZ==''){KOUZ=0;}\n"
							+

							"if( e.field=='KOUD' ){\n"
							+ " rec.set('KOUD',Round_new("
							+ kouggs
							+ ",2) );"
							+ "} \n"
							+

							"if( e.field=='KOUS' ){\n"
							+ " rec.set('KOUS',Round_new("
							+ kousgs
							+ ",2));"
							+ "} \n"
							+

							"if( e.field=='KOUZ' ){\n"
							+ " rec.set('KOUZ',Round_new("
							+ kouzgs
							+ ",2));"
							+ "} \n"
							+ ""
							+

							" var bs=rec.get('BEIZ');\n"
							+ " if(bs==null ||  bs==''){\n"
							+ " bs='0,0,0';\n"
							+ " }\n"
							+ " var bssp=bs.split(',');\n"
							+ // 格式 koud ， kous ， kouz
							"if(e.field=='KOUD'){bs=KOUD+','+bssp[1]+','+bssp[2];}\n"
							+ "if(e.field=='KOUS'){bs=bssp[0]+','+KOUS+','+bssp[2];}\n"
							+ "if(e.field=='KOUZ'){bs=bssp[0]+','+bssp[1]+','+KOUZ;}\n"
							+

							"rec.set('BEIZ',bs);\n" +

							"\n});");

			this.setZongkdBfb(true);// 参数控制 总扣杂 按照 扣水+扣杂+扣吨的方式 否则按照系统原来方式进行

		}

		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		con.Close();
	}

	private boolean zongkdBfb = false;

	public boolean isZongkdBfb() {
		return zongkdBfb;
	}

	public void setZongkdBfb(boolean zongkdBfb) {
		this.zongkdBfb = zongkdBfb;
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			this.setZongkdBfb(false);
			setRiq(DateUtil.FormatDate(new Date()));
			init();

		}
	}

	private void init() {
		setExtGrid(null);
		getSelectData();
	}
}