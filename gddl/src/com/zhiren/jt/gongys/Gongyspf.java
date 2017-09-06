package com.zhiren.jt.gongys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

/*
 * 作者：王磊
 * 时间：2009-12-23
 * 描述：修改页面不进行改动也可进行批复
 */

/*
 * 作者：尹佳明
 * 时间：2010-03-04
 * 描述：
 * 	    1、修改SQL查询语句，解决不显示煤矿编码的问题，并按照"已有"、"未有"状态排序；
 * 		2、增加模糊查询功能；
 */
/*
 * 作者：夏峥	
 * 时间：2012-04-20
 * 描述：
 * 	    1、修改生成编码的方式，采用供应商对应的省份表ID进行生成，如果没有省份表则需用户手动指定一个省份。
 * 		2、生成编码时，保存用户在前台所作的修改操作，但是编码不保存。
 */
/*
 * 作者：夏峥	
 * 时间：2012-06-11
 * 描述：
 * 	    1、修改界面提示框错误
 * 		2、编码字段可编辑
 */
/*
 * 作者：夏峥	
 * 时间：2013-11-21
 * 描述：
 * 	    1、修改界面显示重复数据的错误
 * 		2、编码字段生成后可自动保存至数据库。
 */
public class Gongyspf extends BasePage implements PageValidateListener {

	// 系统日志表中的状态字段
	private static final String ZhangTConstant1 = "成功";

	private static final String ZhangTConstant2 = "失败";

	// 系统日志表中的类别字段
	private static final String leiBConstant = "供应商批复";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
        super.initialize();
        setToAddMsg("");
        msg = "";
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}

	// 从已有导入的记录页面中选择的记录的id
	private String Gongysid_value;

	public String getGongysid_value() {
		return Gongysid_value;
	}

	public void setGongysid_value(String gongysid_value) {
		Gongysid_value = gongysid_value;
	}

	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = saveMsg;
	}

	private boolean tiShi;// 给予提示信息是否显示的

	// 被选中要更改编码的记录id号
	private String RowNumIndex;

	public String getRowNumIndex() {
		return RowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		RowNumIndex = rowNumIndex;
	}

	// 用户输入的6个编码符

	private String LiuSBM;

	public String getLiuSBM() {
		return LiuSBM;
	}

	public void setLiuSBM(String liuSBM) {
//		System.out.println("set____");
		LiuSBM = liuSBM;
	}

	private String LiuSBM_Msg;

	public String getLiuSBM_Msg() {
		return LiuSBM_Msg;
	}

	public void setLiuSBM_Msg(String liuSBM_Msg) {
		LiuSBM_Msg = liuSBM_Msg;
	}

	// 取得的流水编码后的编码
	private long AddBM = -2;

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _InsertButtoMK = false;

	public void InsertButtoMK(IRequestCycle cycle) {
		_InsertButtoMK = true;

	}

	// 生成流水编码
	private boolean _ShengcBm = false;

	public void ShengcBm(IRequestCycle cycle) {
		_ShengcBm = true;

	}

	// 页面树中单位变化 的单位名称
	private String ShenQDW;

	public String getShenQDW() {
		return ShenQDW;
	}

	public void setShenQDW(String shenQDW) {
		ShenQDW = shenQDW;
	}

	// 批复按钮
	private boolean _ReplyButton = false;

	public void ReplyButton(IRequestCycle cycle) {
		_ReplyButton = true;
	}

	private boolean _SaveButton = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveButton = true;
	}

	public void submit(IRequestCycle cycle) {

		if (_RefreshChick) {
			_RefreshChick = false;
			ToAddMsg = "";
			AddBM = -2;
			// getSelectData();
		}

		if (_InsertButtoMK) {
			_InsertButtoMK = false;
			((Visit)this.getPage().getVisit()).setString15(RowNumIndex);
			cycle.activate("Gongys_Xuanzpf");

		}
		if (_ShengcBm) {
			_ShengcBm = false;
			operate_BM();
		}
		if (_ReplyButton) {
			_ReplyButton = false;
			operation_Reply();
		}
		if (_SaveButton) {
			_SaveButton = false;
			operation_Reply();
		}

	}

	// 数据判断，得到行数
	private int Shujpd(JDBCcon con, String sql) {
		return JDBCcon.getRow(con.getResultSet(sql));
	}

	// 更改下级单位的上级公司编码字段返回标示
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
			String operation, String id) {

		InterCom_dt dt = new InterCom_dt();
		String sql_diancxxb_id = "select g.diancxxb_id,d.mingc from gongyssqpfb g,diancxxb d where g.diancxxb_id=d.id and g.id="
				+ id;
		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
		String diancxxb_id = "";
		String diancmc = "";
		if (rsl.next()) {
			diancxxb_id = rsl.getString("DIANCXXB_ID");
			diancmc = rsl.getString("mingc");
		}

		// 有下级电厂单位，需要更新

		boolean t;

		String[] sqls;
		// 为连城电厂特设置审批方法
		if (diancxxb_id.equals("266")) {
			// sqls = null;
			sqls = new String[] { "update fahdwb set SHANGJGSBM=" + bianm
					+ " where id=replace(" + id+",266,'')" };
		}else{
			sqls = new String[] { "update gongysb set SHANGJGSBM=" + bianm
					+ " where id=" + id };
		}
		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

		if (answer[0].equals("true")) { // 更新下级字段成功
			zhuangT = ZhangTConstant1;
			t = true;
		} else {
			zhuangT = ZhangTConstant2;
			t = false;
		}

		logMsg += operation + "表" + tableName + "中编码"
				+ bianm.replaceAll("'", "") + "更新下级" + diancmc + zhuangT;

		return t;
	}

	// 日志记录
	private String logMsg = "";

	private String zhuangT = "";

	private void WriteLog(JDBCcon con) {

		Visit visit = (Visit) this.getPage().getVisit();

		if (!logMsg.equals("")) {// 不为空，需要写入日志记录

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str = sdf.format(date);
			String sql = " insert into xitrzb(id,diancxxb_id,yonghm,leib,shij,zhuangt,beiz) values("
					+ " getnewid("
					+ visit.getDiancxxb_id()
					+ "),"
					+ visit.getDiancxxb_id()
					+ ",'"
					+ visit.getRenymc()
					+ "','"
					+ leiBConstant
					+ "',to_date('"
					+ date_str
					+ "','YYYY-MM-DD,HH24:mi:ss'),'"
					+ this.zhuangT
					+ "','"
					+ logMsg + "')";

			con.getInsert(sql);

		}
	}

	// 进行批复的操作
	public void operation_Reply() {
		Visit visit = (Visit) this.getPage().getVisit();
		SaveMsg = "";
		logMsg = "";
		String tableName = "gongysb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("");

		Change = Change.replaceAll("<font color=\"green\">", "");
		Change = Change.replaceAll("<font color=\"red\">", "");
		Change = Change.replaceAll("</font>", "");
		this.setChange(Change);
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		while (mdrsl.next()) {
			if("已有".equals(mdrsl.getString("COLOR"))){
				SaveMsg += "已存在供应商\""+mdrsl.getString("MINGC")+"\"，请在\"已有供应商\"中查询编码";
			}else{
				StringBuffer sql2 = new StringBuffer();
				sql = new StringBuffer();
				String SaveMsgLocal = "";
				sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
				if (!"0".equals(mdrsl.getString("ID"))) { // 更改操作，则要更新批复表和本地库中的供应商表
					String sql_check = "select id from " + tableName
							+ " where 1=1 and id=" + Gongysid_value + " ";
					sql.append("insert into ").append(tableName).append("(id");
					String mingc = "";
					String quanc = "";
					String bianm = ""; // 循环时，每条记录的编码，根据编码查询，是否在本地库中已经存在
					// String id = ""; // 循环时，每条记录的id，如果和Gongysid_value值一样，
					// 才去判断编码是否一样，进而判断不需上报，就可审批，还是得上报，审批及在本地库中保存
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {

						if (!mdrsl.getColumnNames()[i].equals("COLOR")
								&& !mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")) {
							sql.append(",").append(mdrsl.getColumnNames()[i]);
						}

						if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {

							long SHENGFB_ID = (getExtGrid().getColumn(
									mdrsl.getColumnNames()[i]).combo)
									.getBeanId(mdrsl.getString("SHENGFB_ID"));
							if (mdrsl.getString("SHENGFB_ID") == null
									|| mdrsl.getString("SHENGFB_ID").equals("")) {

								sql2.append(",").append("''");
							} else if (SHENGFB_ID == -1) {
								SaveMsgLocal += "--省份:'"
										+ mdrsl.getString("SHENGFB_ID")
										+ "'在本地库中不存在，请先添加!<br>";
								// sql2.append(",").append("''");
							} else {

								sql2.append(",").append(SHENGFB_ID);
							}
							//							
						} else if (mdrsl.getColumnNames()[i].equals("FUID")) {

							long FUID = (getExtGrid().getColumn(
									mdrsl.getColumnNames()[i]).combo)
									.getBeanId(mdrsl.getString("fuid"));
							// 隶属于的判断条件
							// 如果FUID>0，说明已在分公司数据库中找到该上级单位。
							// 否则FUID=-1，说明：
							// 1、在分公司数据库中没有该上级单位。
							// 2、本身就没有上级单位。

							if (FUID > 0) {
								// 已在分公司数据库中找到该上级单位
								sql2.append(",").append(FUID);

							} else if (FUID == -1
									&& mdrsl.getString("fuid").equals("请选择")) {
								// 本身就没有上级单位。
								sql2.append(",").append(FUID);
							} else {
								// 在分公司数据库中没有该上级单位。
								SaveMsgLocal += "--隶属于:" + mdrsl.getString("FUID")
										+ "本地库中不存在<br>";
							}

						} else {
							if (mdrsl.getColumnNames()[i].equals("BIANM")) {
								bianm = getExtGrid().getValueSql(
										getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]),
										mdrsl.getString(i));
								sql_check += " and bianm=" + bianm + "";
							}
							if (mdrsl.getColumnNames()[i].equals("MINGC")) {
								mingc = getExtGrid().getValueSql(
										getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]),
										mdrsl.getString(i));
							}
							if (mdrsl.getColumnNames()[i].equals("QUANC")) {
								quanc = getExtGrid().getValueSql(
										getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]),
										mdrsl.getString(i));
							}

							if (!mdrsl.getColumnNames()[i].equals("COLOR")
									&& !mdrsl.getColumnNames()[i]
											.equals("DIANCXXB_ID")) {
								sql2.append(",").append(
										getExtGrid().getValueSql(
												getExtGrid().getColumn(
														mdrsl.getColumnNames()[i]),
												mdrsl.getString(i)));
							}
						}
					}

					// 当记录号与一直时，知道此条记录是导入的，但编码有可能被手动改变了，所以要判断编码是否一致
					if (RowNumIndex == null) {
						RowNumIndex = "";
					}
					if (mdrsl.getString("ID").equals(this.RowNumIndex)) {
						if (this.Shujpd(con, sql_check) != 0) { // 说明编码一致，在本地库中已经存在,
							if (this.GengGSJBM(con, tableName, bianm, "更改", mdrsl
									.getString("ID"))) {// 更新下级单位的上级公司编码字段成功,更改批复表状态

								String sql3 = " update gongyssqpfb set pifzt=1  where id="
										+ mdrsl.getString("ID");
								int flag = con.getUpdate(sql3);

								if (flag == 0) {// 更改本级数据库状态不成功

								}
							} else {

								SaveMsg += "----------------批复记录-----------" 
										+ "<br>--编码:"+ bianm.replaceAll("'", " ")
										+ "<br>---名称:"+ mingc.replaceAll("'", " ")
										+ "<br>---全称:"+ quanc.replaceAll("'", " ") 
										+ "<br>--的记录下级单位更新失败,不能批复!<br>";
							}
							continue;
						}
					}
						if (SaveMsgLocal.equals("")) { // 在本地库中不存在,判断更新是否成功

						if (this.GengGSJBM(con, tableName, bianm, "更改", mdrsl
								.getString("ID"))) {// 更新下级单位的上级公司编码字段成功,更改批复表状态

							// 先更改批复表
							String sql3 = " update gongyssqpfb set pifzt=1 where id="
									+ mdrsl.getString("ID");
							int flag = con.getUpdate(sql3);

							// 批复表更改成功，再去更改本地数据库
							if (flag != 0) {

								sql.append(") values(").append(sql2).append(")\n");
								con.getInsert(sql.toString());// 更改本级数据库状态成功,上报上一级公司
								// 数据库中触发器已经自动完成
							}

						} else {

							SaveMsg += "----------------批复记录-----------" +
									"<br>--编码:" + bianm.replaceAll("'", " ") + 
									"<br>--名称:" + mingc.replaceAll("'", " ") + 
									"<br>--全称:" + quanc.replaceAll("'", " ")+ 
									"<br>--的记录下级单位更新失败,不能批复<br>";
						}
					}

					SaveMsg += SaveMsgLocal;
				}
			}
		}
		this.WriteLog(con);
		if (SaveMsg.equals("")) {

			SaveMsg += "更新成功";
			ToAddMsg = ""; // 更改后标识，上级导入的页面不再显示
		}
		con.Close();
		tiShi = true;
	}

	// 对数据库中得到的流水编码进行操作，判断是否满足需要
	private void operate_BM() {

		String bianm_value = this.createBM();

		try {
			if (bianm_value.length() <= 6) {
				AddBM = 0;
			} else {
				AddBM = Long.valueOf(bianm_value).longValue();
			}
		} catch (Exception e) {
			AddBM = -1;
		}
	}

	// 从库中取得流水编码
	private String createBM() {
		JDBCcon con = new JDBCcon();
		
		Change = Change.replaceAll("<font color=\"green\">", "");
		Change = Change.replaceAll("<font color=\"red\">", "");
		Change = Change.replaceAll("</font>", "");
		this.setChange(Change);
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		long SHENGFB_ID=0;
		
		while(mdrsl.next()){
			SHENGFB_ID = (getExtGrid().getColumn("SHENGFB_ID").combo).getBeanId(mdrsl.getString("SHENGFB_ID"));
//			保存相关数据
			String U_sql="update gongyssqpfb set mingc='"+mdrsl.getString("MINGC")+"',\n" +
					" quanc='"+mdrsl.getString("QUANC")+"',\n" +
					" xuh='"+mdrsl.getString("XUH")+"',\n" +
					" shengfb_id="+SHENGFB_ID+" ,\n" +
					" piny='"+mdrsl.getString("PINY")+"',\n" +
					" danwdz='"+mdrsl.getString("DANWDZ")+"'\n" +
					" where id="+mdrsl.getString("ID");
			con.getUpdate(U_sql);
		}
//		取得编码
		String sql =
			"SELECT NVL(MAX(BIANM), '"+SHENGFB_ID+"0') BIANM\n" +
			"  FROM (SELECT BIANM\n" + 
			"          FROM GONGYSB\n" + 
			"         WHERE BIANM LIKE '"+ SHENGFB_ID + "%'\n" + 
			"        UNION (SELECT BIANM\n" + 
			"                FROM GONGYSSQPFB\n" + 
			"               WHERE BIANM LIKE '"+ SHENGFB_ID + "%'\n" + 
			"              MINUS\n" + 
			"              SELECT BIANM FROM GONGYSSQPFB WHERE ID = "+getRowNumIndex()+"))";
 
		ResultSetList rsl = con.getResultSetList(sql);
		String bianm_value = "";
		while (rsl.next()) {
			bianm_value = rsl.getString("bianm");
			int liusbm = Integer.valueOf(bianm_value.substring(7, bianm_value.length())).intValue() + 1;
			if (liusbm < 10) {
				bianm_value = SHENGFB_ID+"0000" + liusbm;
			} else if (liusbm < 100 && liusbm >= 10) {
				bianm_value = SHENGFB_ID+"00" + liusbm;
			} else if (liusbm < 1000 && liusbm >= 100) {
				bianm_value = SHENGFB_ID+"0" + liusbm;
			} else {
				bianm_value = SHENGFB_ID+String.valueOf(liusbm);
			}
		}
		
		sql = "update gongyssqpfb set bianm = '"+ bianm_value + "' where id = " + getRowNumIndex();
		con.getUpdate(sql);
		con.Close();
		return bianm_value;
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		String sql = "";
		sql = "select g.id,\n"
				+ "       g.xuh,\n"
				+ "       dc.mingc as diancxxb_id,\n"
//				+ "case\n"
//				+ "  when length(g.bianm) < 15 then\n"
//				+ "   ''\n"
//				+ "  else\n"
//				+ "   g.bianm\n"
//				+ "end as bianm,\n"
				+ "		  g.bianm as bianm,\n"
				+ "       g.mingc,\n"
				+ "       g.quanc,\n"
				+ "		decode(g.shangjdwmc,null,'请选择',\n"
				+ "             (select mingc from gongysb where bianm=g.shangjdwbm),null,\n"
				+ "             g.shangjdwmc,\n"
				+ "             (select mingc from gongysb where bianm=g.shangjdwbm)\n"
				+ "             ) as fuid,\n"
				+ "       g.danwdz,\n"
				+ "       g.piny,\n"
				+ "       s.quanc as shengfb_id,\n"
				+ "       DECODE(c.id,null,nvl('<font color=\"red\">未有</font>',''),nvl('<font color=\"green\">已有</font>','')) AS color\n"
				+ "  from gongyssqpfb g, diancxxb dc, shengfb s,\n"
				+ "       (select DISTINCT g.id\n"
				+ "        from gongyssqpfb g, diancxxb dc, shengfb s, gongysb gys\n"
				+ "        where g.shengfb_id = s.id\n"
				+ "              and g.diancxxb_id = dc.id\n"
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and g.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid())
				+ "\n"
				+ "              and gys.leix = 1 and (g.mingc = gys.mingc or g.quanc = gys.quanc)) c\n"
				+ "  where g.shengfb_id = s.id(+)\n"
				+ "       and g.diancxxb_id = dc.id\n"
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and g.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid()) + "\n"
				+ "		and g.id=c.id(+)\n " 
			    + "     and g.pifzt = "+getZhuangtValue().getId()
				+ "  order by color desc, xuh";
		// System.out.println(sql);
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("gongyssqpfb");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("diancxxb_id").setHeader("申请单位");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(130);
//		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setHeader("简称");
		egu.getColumn("mingc").setWidth(90);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(200);
		egu.getColumn("fuid").setHeader("隶属于");
		egu.getColumn("fuid").setWidth(150);
		egu.getColumn("danwdz").setHeader("单位地址");
		egu.getColumn("danwdz").setWidth(150);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(50);
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		// egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("color").setHeader("状态");
		egu.getColumn("color").setWidth(50);
		egu.getColumn("color").setEditor(null);

		egu.getColumn("diancxxb_id").setUpdate(false);
		egu.getColumn("shengfb_id").setEditor(new ComboBox());
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		egu.getColumn("fuid").setEditor(new ComboBox());
		egu.getColumn("fuid").setComboEditor(egu.gridId,new IDropDownModel("select -1 as id,'请选择' as mingc from dual\n"
				+ "union\n" + "select id,mingc from gongysb"));

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		// egu.setWidth(1000);
		egu.setWidth("bodyWidth");
		egu.addTbarText("申请单位:");
		if (((Visit)this.getPage().getVisit()).getRenyjb() == 1) {
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Jit_Fengs, ((Visit) this.getPage()
				.getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
		} else {
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
				.getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
		}
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addTbarText("批复状态:");
		ComboBox zhuangt = new ComboBox();
		zhuangt.setTransform("ZhuangtDropDown");
		zhuangt.setId("Zhuangt");
		zhuangt.setLazyRender(true);//动态绑定
		zhuangt.setWidth(70);
        egu.addToolbarItem(zhuangt.getScript());
        egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		egu.addTbarText("供应商名称:");
		TextField tf = new TextField();
		tf.setId("Gongysmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("模糊查询", "function(){ "
				+ "	var mh_value=Gongysmc.getValue(); "
				+ "	mohcx(mh_value,gridDiv_data,gridDiv_ds);" + "} "));
		egu.addTbarText("-");

		egu.addTbarBtn(new GridButton(
						"在已有供应商中查询",
						"function(){  if(gridDiv_sm.getSelected()== null){Ext.MessageBox.alert('提示信息','请选中一个供应商'); return;} var rec=gridDiv_sm.getSelected();    document.all.RowNumIndex.value=rec.get('ID');  document.all.InsertButtoMK.click();}"));

		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton(
						"生成编码",
						"function(){  if(gridDiv_sm.getSelected()== null){Ext.MessageBox.alert('提示信息','请选中一个供应商'); return;}\n" +
						" var rec=gridDiv_sm.getSelected(); \n" +
						"if(rec.get('SHENGFB_ID')==''){Ext.MessageBox.alert('提示信息','供应商所在省份不能为空'); return;}\n" +
						"var Cobj = document.getElementById('CHANGE');\n" +
						"Cobj.value = '<result>'+'<result>' + '<sign>U</sign>'" +
						"+ '<ID update=\"true\">' + rec.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"+
						"+ '<XUH update=\"true\">' + rec.get('XUH')+ '</XUH>'"+
						"+ '<MINGC update=\"true\">' + rec.get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"+
						"+ '<QUANC update=\"true\">' + rec.get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"+
						"+ '<SHENGFB_ID update=\"true\">' + rec.get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"+
						"+ '<PINY update=\"true\">' + rec.get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"+
						"+ '<DANWDZ update=\"true\">' + rec.get('DANWDZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DANWDZ>'"+
						"+ '</result>'+'</result>';\n" +
						" document.all.RowNumIndex.value=gridDiv_sm.getSelected().get('ID');\n"+
						" document.all.ShengcBm.click();}"));

		egu.addTbarText("-");

		String fn = "function(){"
				+ "var Mrcd =  gridDiv_sm.getSelections(); "
				+ "for(i = 0; i< Mrcd.length; i++){ "
				+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} "
				+ "if(Mrcd[i].get('FUID') == ''){Ext.MessageBox.alert('提示信息','字段 隶属于 不能为空');return;}"
				+ "if(Mrcd[i].get('BIANM') == ''){Ext.MessageBox.alert('提示信息','字段 编码 不能为空');return;}"
				+ "if(Mrcd[i].get('SHENGFB_ID') == ''){Ext.MessageBox.alert('提示信息','供应商所在省份不能为空'); return;}"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"
				+ "+ '<XUH update=\"true\">' + Mrcd[i].get('XUH')+ '</XUH>'"
				+ "+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'"
				+ "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"
				+ "+ '<QUANC update=\"true\">' + Mrcd[i].get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"
				+ "+ '<SHENGFB_ID update=\"true\">' + Mrcd[i].get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"
				+ "+ '<FUID update=\"true\">' + Mrcd[i].get('FUID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FUID>'"
				+ "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'"
				+ "+ '<PINY update=\"true\">' + Mrcd[i].get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"
				+ "+ '<DANWDZ update=\"true\">' + Mrcd[i].get('DANWDZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DANWDZ>'"
				+ "+ '<COLOR update=\"true\">' + Mrcd[i].get('COLOR')+ '</COLOR>'"
				+ " + '</result>' ; }"
				+ "if(gridDiv_history==''){ "
				+ "Ext.MessageBox.alert('提示信息','没有进行改动无需保存');"
				+ "}else{"
				+ "var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+'</result>';document.getElementById('savebutton').click();Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
				+ "}" + "}";
		egu.addTbarBtn(new GridButton("批复", fn));

		if (ToAddMsg == null) {
			ToAddMsg = "";
		}

		// 从添加所选按钮回来给予的提示信息，
		if (ToAddMsg.equals("toAdd")) {
			this.setMsg(visit.getString14());
		}

		// 点击批复按钮后，成功与否的提示信息
		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('提示信息','" + SaveMsg + "');\n";

		} else {
			SaveMsg = "";
		}

		// 流水编码的提示信息
		if (AddBM == -1) {
			egu.addOtherScript(" var regexp=new RegExp('^" + RowNumIndex
					+ "$','gi'); "
					+ "var rec_index=gridDiv_ds.find('ID',regexp);"
					+ "gridDiv_ds.getAt(rec_index).set('BIANM','" + LiuSBM
					+ "');");
			LiuSBM_Msg = "Ext.MessageBox.alert('提示信息','该条记录无法生成流水号');";
		} else if (AddBM == -2) {
			LiuSBM_Msg = "";
		} else {
			LiuSBM_Msg = "";
			String s = LiuSBM;
			if (AddBM < 10) { // 流水号为单数，第一位补0
				s += "0";
			}
			s += AddBM;
			egu.addOtherScript(" var regexp=new RegExp('^"
							+ RowNumIndex
							+ "$','gi');\n  var rec_index=gridDiv_ds.find('ID',regexp);gridDiv_ds.getAt(rec_index).set('BIANM','"
							+ s + "');");
		}
		AddBM = -2;
		// egu.addOtherScript(" gridDiv_sm.singleSelect=true; function
		// rowSelectIndex(sml,rowIndex,record
		// ){document.all.RowNumIndex.value=rowIndex;}
		// gridDiv_sm.addListener('rowselect',rowSelectIndex);\n");
		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");
		// egu.addOtherScript(" for(var i=0;i<gridDiv_ds.getCount();i++){var
		// reco=gridDiv_ds.getAt(i); var color_value=reco.get('COLOR');
		// if(color_value=='已有'){
		// gridDiv_grid.getView().getCell(i,11).style.backgroundColor='green';}
		// else if(color_value=='未有'){
		// gridDiv_grid.getView().getCell(i,11).style.backgroundColor='yellow';}}");
		// egu.addOtherScript("gridDiv_ds.addListener('load',function(gridDiv_ds,rds,opts){
		// for(var i=0;i<gridDiv_ds.getCount();i++){var
		// reco=gridDiv_ds.getAt(i); var color_value=reco.get('COLOR');
		// if(color_value==0){
		// gridDiv_grid.getView().getRow(i).style.backgroundColor='green';} else
		// if(color_value==1){
		// gridDiv_grid.getView().getRow(i).style.backgroundColor='yellow';}}
		// });");
		egu.addOtherScript(" grid=gridDiv_grid;");
		// egu.addOtherScript(" grid=gridDiv_grid;");
		// egu.addOtherScript("
		// diancTree_text.addListener('change',diancTree_textChange);");
		// egu.addOtherScript("
		// gridDiv_grid.addListener('sortchange',sortColor);");
		setExtGrid(egu);
		con.Close();

	}

	//状态下拉框
	public IDropDownBean getZhuangtValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getZhuangtModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setZhuangtValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setZhuangtModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getZhuangtModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getZhuangtModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "未批复"));
		list.add(new IDropDownBean(1,"已批复"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
		return;
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			initNavigation();
			ToAddMsg = cycle.getRequestContext().getRequest().getParameter(
					"MsgAdd");
			if (ToAddMsg == null) {
				ToAddMsg = "";
			}

//			this.setDataSource(visit.getString15());
//			Gongysid_value = visit.getString14();
			setZhuangtValue(null);
			setZhuangtModel(null);
		}
		
		// else{
		initNavigation();
		getSelectData();
		// }
	}

	public String getNavigetion() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setNavigetion(String nav) {
		((Visit) this.getPage().getVisit()).setString3(nav);
	}

	public void initNavigation() {
		// Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");

		// 导航栏树的查询SQL
		String sql = "";

		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			sql = "select id,decode(jib,1,'根',mingc) as mingc,jib,fuid,checked from (\n"
					+ "select decode(id,"
					+ getTreeid()
					+ ",0,id) as id,mingc as mingc,\n"
					+ "level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n"
					+ "(select id,mingc,fuid from diancxxb\n"
					+ "union\n"
					+ "select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n"
					+ "start with id ="
					+ getTreeid()
					+ "\n"
					+ "connect by fuid=prior id order SIBLINGS by mingc )";

		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			sql = "select id,decode(jib,1,'根',mingc) as mingc,jib,fuid,checked from (\n"
					+ "select decode(id,"
					+ getTreeid()
					+ ",0,id) as id,mingc as mingc,\n"
					+ "level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n"
					+ "(select id,mingc,fuid,shangjgsid from diancxxb\n"
					+ "union\n"
					+ "select id,xingm as mingc,diancxxb_id as fuid,0 as shangjgsid from lianxrb) a\n"
					+ "start with id ="
					+ getTreeid()
					+ "\n"
					+ "connect by (fuid=prior id or shangjgsid=prior id) order SIBLINGS by mingc )";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			sql = "select id,decode(jib,1,'根',mingc) as mingc,jib,fuid,checked from (\n"
					+ "select decode(id,"
					+ getTreeid()
					+ ",0,id) as id,mingc as mingc,\n"
					+ "level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n"
					+ "(select id,mingc,fuid from diancxxb\n"
					+ "union\n"
					+ "select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n"
					+ "start with id ="
					+ getTreeid()
					+ "\n"
					+ "connect by fuid=prior id order SIBLINGS by mingc )";
		}

		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql, true);
		sql = "select id, mingc, fuid, 0 dc\n" + "  from diancxxb\n"
				+ " where\n" + "  id not in (select distinct diancxxb_id\n"
				+ "  from lianxrb l, diancxxb d\n"
				+ " where l.diancxxb_id = d.id and d.id in (select id\n"
				+ "\t\t\t from(\n" + "\t\t\t select id from diancxxb\n"
				+ "\t\t\t start with id=" + getTreeid() + "\n"
				+ "\t\t\t connect by (fuid=prior id or shangjgsid=prior id)\n"
				+ "\t\t\t )\n" + "\t\t\t union\n" + "\t\t\t select id\n"
				+ "\t\t\t from diancxxb\n" + "\t\t\t where id=" + getTreeid()
				+ "))\n" + " order by fuid desc";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		TreeNode tmp;
		while (rsl.next()) {
			tmp = (TreeNode) node.getNodeById(rsl.getString("id"));
			if (tmp != null && tmp.isLeaf()) {
				tmp.remove();
			}
		}
		rsl.close();
		con.Close();
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	private String treeid;

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
		this.treeid = treeid;
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
}
