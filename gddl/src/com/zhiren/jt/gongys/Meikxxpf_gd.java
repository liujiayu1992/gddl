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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;
/*
 * 作者：夏峥
 * 时间：2012-03-27
 * 描述：修正界面刷新逻辑
 * 		 增加在批复煤矿时的判断，如果无接收端端口则只更新本地数据，下级单位数据由手工更新。
 */
/*
 * 作者：夏峥
 * 时间：2012-10-25
 * 描述：批复界面中判断重复的条件变更为只判断编码是否重复。
 */


public class Meikxxpf_gd extends BasePage implements PageValidateListener {

	// 系统日志表中的状态字段
	private static final String ZhangTConstant1 = "成功";

	private static final String ZhangTConstant2 = "失败";

	// 系统日志表中的类别字段
	private static final String leiBConstant = "煤矿批复";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		setToAddMsg(null);
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

	private String DataSource; // 从已有煤矿页面中选择煤矿编码

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	// 从已有导入的记录页面中选择的记录的id
	private String Meikdwid_value; // 从已有煤矿页面中选择的煤矿id

	public String getMeikdwid_value() {
		return Meikdwid_value;
	}

	public void setMeikdwid_value(String meikdwid_value) {
		Meikdwid_value = meikdwid_value;
	}

	private String Meikdqmc_value; // 煤矿地区名称

	public String getMeikdqbm_value() {
		return Meikdqmc_value;
	}

	public void setMeikdqbm_value(String meikdqbm_value) {
		Meikdqmc_value = meikdqbm_value;
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
	private String RowNumIndex; // 已选择的要申请批复的煤矿id

	public String getRowNumIndex() {
		return RowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		RowNumIndex = rowNumIndex;
	}

	private String currentPage = "1"; // 保存页面上Grid当前显示的页号

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPages) {
		this.currentPage = currentPages;
	}

	private String LiuSBM;

	public String getLiuSBM() {
		return LiuSBM;
	}

	public void setLiuSBM(String liuSBM) {
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
			setCurrentPage("1"); // 点"刷新"按钮时让Grid显示第1页。
		}
		if (_InsertButtoMK) {
			_InsertButtoMK = false;
			Visit visit = (Visit)getPage().getVisit();
			visit.setString16(RowNumIndex);
			cycle.activate("Meikxx_xzpf_gd");
		}
		if (_ShengcBm) {
			_ShengcBm = false;
			if (saveToPfb()) {
				operate_BM();
			}
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
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm, String operation, String id, String meikdqbm) {
		InterCom_dt dt = new InterCom_dt();

		String sql_diancxxb_id = "select b.diancxxb_id,d.mingc from meikxxbsqpfb b," +
				"diancxxb d where b.diancxxb_id=d.id and b.id="+ id;
		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
		String diancxxb_id = "";
		String diancmc = "";
		if (rsl.next()) {
			diancxxb_id = rsl.getString("DIANCXXB_ID");
			diancmc = rsl.getString("mingc");
		}
//		如果无终端端口信息则只更新公司级数据库
		if (Chk_endpoint(diancxxb_id,con)) {
			return true;
		}
		
		// 有下级电厂单位，需要更新
		boolean t;
		String[] sqls;
		StringBuffer strSql = new StringBuffer();
//		在更新煤款信息表的同时更新供应商表中的信息
//		其中煤矿信息表只更新SHANGJGSBM字段
//		供应商表中还需将煤矿地区编码所对应的煤矿地区ID更新至供应商表中的FUID中
		strSql.append("begin\n");
		strSql.append("update meikxxb set SHANGJGSBM='" + bianm+ "' where id=" + id+";\n");
		
		strSql.append("UPDATE GONGYSB\n" );
		strSql.append("   SET FUID =\n" ); 
		strSql.append("       (SELECT ID\n" ); 
		strSql.append("          FROM MEIKDQB\n" ); 
		strSql.append("         WHERE BIANM = '"+meikdqbm+"'\n" ); 
		strSql.append("           AND ROWNUM = 1),\n" ); 
		strSql.append("           SHANGJGSBM = '" + bianm+ "'\n" ); 
		strSql.append(" WHERE ID = (SELECT MEIKDQ_ID FROM MEIKXXB WHERE ID ="+id+");");
		strSql.append("end;");
		
		sqls = new String[] {strSql.toString() };

		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

		if (answer[0].equals("true")) { // 更新下级字段成功
			zhuangT = ZhangTConstant1;
			t = true;
		} else {
			zhuangT = ZhangTConstant2;
			t = false;
		}

		logMsg += operation + "煤矿信息编码"
				+ bianm
				+ "，更新下级" + diancmc + zhuangT;

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
		String tableName = "meikxxb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("");

		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(
				this.getChange());
		while (delrsl.next()) {
			// 删除时要做的操作
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(this.getChange());
		while (mdrsl.next()) {
			sql = new StringBuffer();
			String SaveMsgLocal = "";
			String MK_ID=MainGlobal.getNewID(visit.getDiancxxb_id());
			String GYS_ID=MainGlobal.getNewID(visit.getDiancxxb_id());
			
//			判断省份是否有效
			long SHENGFB_ID = (getExtGrid().getColumn("SHENGFB_ID").combo).getBeanId(mdrsl.getString("SHENGFB_ID"));
			if (SHENGFB_ID == -1L) {
				SaveMsgLocal += "<br>---省份:"+ mdrsl.getString("SHENGFB_ID")	+ "本地库中不存在<br>";
			}
//			判断计划口径是否有效
			long JIHKJB_ID = (getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id"));
			if (JIHKJB_ID == -1L) {
				SaveMsgLocal += "<br>---计划口径:"+ mdrsl.getString("JIHKJB_ID")+ "本地库中不存在<br>";
			}
			
//			判断类别是否有效
			long LEIB = (getExtGrid().getColumn("LEIB").combo).getBeanId(mdrsl.getString("LEIB"));
			if (LEIB == -1L) {
				SaveMsgLocal += "<br>---类别:" + mdrsl.getString("LEIB")+ "本地库中不存在<br>";
			}
			
//			判断类型是否有效
			long LEIX = (getExtGrid().getColumn("LEIX").combo).getBeanId(mdrsl.getString("LEIX"));
			if (LEIX == -1L) {
				SaveMsgLocal += "<br>---类型:" + mdrsl.getString("LEIX")+ "本地库中不存在<br>";
			} 
			
//			判断城市是否有效
			long CHENGSB_ID = (getExtGrid().getColumn("CHENGSB_ID").combo).getBeanId(mdrsl.getString("CHENGSB_ID"));
			if (CHENGSB_ID == -1L) {
				SaveMsgLocal += "<br>---城市:"+ mdrsl.getString("CHENGSB_ID")	+ "本地库中不存在<br>";
			}
			
//			判断煤矿地区是否有效
			String MEIKDQBM = (getExtGrid().getColumn("MEIKDQBM").combo).getBeanStrId(mdrsl.getString("MEIKDQBM"));
			String str = "select id from MEIKDQB where bianm = '"+ MEIKDQBM + "'";
			ResultSetList rsl = con.getResultSetList(str);
			long mekdq_id =-1L;
			while (rsl.next()) {
				mekdq_id = rsl.getLong("ID");
			}
			rsl.close();
			if (mekdq_id == -1L) {
				SaveMsgLocal += "<br>---煤矿地区:"+ mdrsl.getString("MEIKDQBM")+ "本地库中不存在<br>";
			}
			
			String bianm = ""; // 循环时，每条记录的编码，根据编码查询，是否在本地库中已经存在
			bianm = mdrsl.getString("BIANM");
			
			String mingc = mdrsl.getString("MINGC");
			
			String quanc =  mdrsl.getString("QUANC");
			
			String xuh= mdrsl.getString("XUH");
			
			String piny=mdrsl.getString("PINY");
			
			
			if (!"0".equals(mdrsl.getString("ID"))) { // 更改操作，则要更新批复表和本地库中的煤矿信息表
				String sql_check = "select id from " + tableName
						+ " where 1=1 and id=" + Meikdwid_value
						+ " and bianm='" + bianm + "'";
				
				// 当记录号与一直时，知道此条记录是导入的，但编码有可能被手动改变了，所以要判断编码是否一致
				if (RowNumIndex == null) {
					RowNumIndex = "";
				}
				if (mdrsl.getString("ID").equals(this.RowNumIndex)) {

					if (this.Shujpd(con, sql_check) != 0) { // 说明编码一致，在本地库中已经存在,
						// 只需更改批复表状态，和调用接口，更新下级的上级单位编码字段即可
						if (this.GengGSJBM(con, tableName, bianm, "更改", mdrsl.getString("ID"), MEIKDQBM)) {
							// 更新下级单位的上级公司编码字段成功,更改批复表状态

							String sql3 = " update meikxxbsqpfb set pifzt=1 where id="+ mdrsl.getString("ID");
							con.getUpdate(sql3);
//							如果无终端端口信息则只更新公司级数据库
							if (Chk_endpoint(mdrsl.getString("DCID"),con)) {
								SaveMsg += "<br>----------------批复记录-----------<br>---编码:"
									+ bianm + "<br>---名称:" + mingc + "<br>---全称:" + quanc
									+ "<br>---的记录本地保存成功，但需手动更新下级单位信息。<br>";
							}
						} else {
							SaveMsg += "<br>----------------批复记录-----------<br>---编码:"
									+ bianm
									+ "<br>---名称:"
									+ mingc
									+ "<br>---全称:"
									+ quanc + "<br>--的记录下级单位更新失败,不能批复!<br>";
						}
						continue;
					}
				}

//				String sql4 = "select id from meikxxb where bianm='" + bianm+ "' or mingc='" + mingc + "' or quanc='" + quanc + "'";
//				批复界面中判断重复的条件变更为只判断编码是否重复。
				String sql4 = "select id from meikxxb where bianm='" + bianm+"'";
				if (this.Shujpd(con, sql4) != 0) { // 在本地库中，存在一样的单位,给予提示信息

					SaveMsgLocal += "<br>----------------批复记录-----------<br>---编码:"
							+ bianm + "<br>---名称:" + mingc + "<br>---全称:" + quanc
							+ "<br>---的记录编码重复,不能批复!<br>";
				} else if (SaveMsgLocal.equals("")) { // 在本地库中不存在,判断更新是否成功

					if (this.GengGSJBM(con, tableName, bianm, "更改", mdrsl.getString("ID"), MEIKDQBM)) {
						// 更新下级单位的上级公司编码字段成功,更改批复表状态
						// 如果无下级单位接收端口则只更新本地信息并将编码返回

						// 先更改批复表
						String sql3 = " update meikxxbsqpfb set pifzt=1 where id="+ mdrsl.getString("ID");
						int flag = con.getUpdate(sql3);
						// 批复表更改成功，再去更改本地数据库
						if (flag != 0) {
							sql.append("begin \n");
//							插入到供应商表中
							sql.append("INSERT INTO GONGYSB\n" );
							sql.append("  (ID, FUID, XUH, MINGC, QUANC, PINY, BIANM,SHENGFB_ID, beiz,LEIX, ZHUANGT)\n");
							sql.append("VALUES\n");
							sql.append("  ("+GYS_ID+","+mekdq_id+",'"+xuh+"','"+mingc+"','"+quanc+"','"+piny+"','"+bianm+"'," +
									   "   "+SHENGFB_ID+",'',0,1);\n");
//							插入到煤款信息表中
							sql.append("INSERT INTO MEIKXXB\n" );
							sql.append("  (ID,XUH,BIANM,MINGC,QUANC,PINY,SHENGFB_ID,JIHKJB_ID,CHENGSB_ID,LEIX,LEIB,\n" );
							sql.append("   MEIKDQ_ID,beiz,SHIYZT)\n" ); 
							sql.append("VALUES\n" );
							sql.append("   ("+MK_ID+",'"+xuh+"','"+bianm+"','"+mingc+"','"+quanc+"','"+piny+"'" +
									   ","+SHENGFB_ID+","+JIHKJB_ID+","+CHENGSB_ID+",'"+mdrsl.getString("LEIX")+"','"+mdrsl.getString("LEIB")+"',"+GYS_ID+",'',1);\n");
							sql.append("end;" );
							
							con.getInsert(sql.toString());// 更改本级数据库状态成功,上报上一级公司

//							如果无终端端口信息则只更新公司级数据库
							if (Chk_endpoint(mdrsl.getString("DCID"),con)) {
								SaveMsg += "<br>----------------批复记录-----------<br>---编码:"
									+ bianm + "<br>---名称:" + mingc + "<br>---全称:" + quanc
									+ "<br>---的记录本地保存成功，但需手动更新下级单位信息。<br>";
							}
						}

					} else {
						SaveMsg += "<br>----------------批复记录-----------<br>---编码:"
								+ bianm + "<br>---名称:" + mingc + "<br>---全称:" + quanc
								+ "<br>---的记录下级单位更新失败,不能批复<br>";
					}
				}
				SaveMsg += SaveMsgLocal;
			}
		}

		this.WriteLog(con);
		if (SaveMsg.equals("")) {

			SaveMsg += "更新成功";
			ToAddMsg = ""; // 更改后标识，上级导入的页面不再显示
		}
		tiShi = true;
	}
	
	private boolean Chk_endpoint(String diancxxb_id,JDBCcon con){
		String sql=
			"select d.diancxxb_id,z.endpointaddress\n" +
			"from dianczhb d,jiekzhb z\n" + 
			"where d.jiekzhb_id=z.id and d.diancxxb_id="+diancxxb_id;
		ResultSetList rs=con.getResultSetList(sql);
		if(rs.next()){
//			如果无终端端口则为本地批复信息，否则为远程批复
			if(rs.getString("endpointaddress")==null||rs.getString("endpointaddress").equals("")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}

	}

	/**
	 * @author yinjm 在煤矿批复页面点"生成编码"按钮时，将已选择的记录保存到meikxxbsqpfb表中，
	 *         并校验煤矿地区、省份、城市的编码长度，是否分别为6位、2位、2位，之所以将已选择的记录保存
	 *         到meikxxbsqpfb表中，是因为生成煤矿编码时需要用meikxxbsqpfb表中的煤矿地区编码、省份编码、
	 *         城市编码到meikxxb中模糊查询最大的编码。
	 */
	public boolean saveToPfb() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
				this.getChange());

		while (mdrsl.next()) {

			if (!mdrsl.getString("ID").equals("0")) {
				StringBuffer sql = new StringBuffer();
				ResultSetList rsl;
				String bianm = ""; // 保存从数据库取到的编码
				sql.append("update meikxxbsqpfb set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if (mdrsl.getColumnNames()[i].equals("MEIKDQBM")) {
						String meikdqbm = String.valueOf((getExtGrid()
								.getColumn("MEIKDQBM").combo).getBeanId(mdrsl
								.getString("MEIKDQBM")));
						if (meikdqbm.length() != 6) {
							this.setMsg("煤矿地区的编码长度不为6，无法正常生成编码！");
							return false;
						}
						sql.append("MEIKDQBM = ").append("'").append(meikdqbm)
								.append("',");
					} else if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {
						String shengfb_id = String.valueOf((getExtGrid()
								.getColumn("SHENGFB_ID").combo).getBeanId(mdrsl
								.getString("SHENGFB_ID")));
						String shengfbm = "select s.bianm from shengfb s where s.id = "
								+ shengfb_id;
						rsl = con.getResultSetList(shengfbm);
						while (rsl.next()) {
							bianm = rsl.getString("BIANM");
						}
						if (bianm.length() != 2) {
							this.setMsg("省份的编码长度不为2，无法正常生成编码！");
							return false;
						}
						sql.append("SHENGFB_ID = ").append(shengfb_id).append(
								",");
					} else if (mdrsl.getColumnNames()[i].equals("CHENGSB_ID")) {
						String chengb_id = String.valueOf((getExtGrid()
								.getColumn("CHENGSB_ID").combo).getBeanId(mdrsl
								.getString("CHENGSB_ID")));
						String chengsbm = "select c.bianm from chengsb c where c.id = "
								+ chengb_id;
						rsl = con.getResultSetList(chengsbm);
						while (rsl.next()) {
							bianm = rsl.getString("BIANM");
						}
						if (bianm.length() != 2) {
							this.setMsg("城市的编码长度不为2，无法正常生成编码！");
							return false;
						}
						sql.append("CHENGSB_ID = ").append(chengb_id).append(
								",");
					} else if (!mdrsl.getColumnNames()[i].equals("COLOR")
							&&!mdrsl.getColumnNames()[i].equals("DCID")
							&& !mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")
							&& !mdrsl.getColumnNames()[i].equals("BIANM")) {
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id = ").append(mdrsl.getString("ID"))
						.append("\n");
				con.getUpdate(sql.toString());
			}
		}
		mdrsl.close();
		con.Close();
		return true;
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
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
		} else {
			return value;
		}
	}

	/**
	 * @author yinjm 对createBM()方法返回的编码进行处理，取出流水账号并且加1，如果没有百位、十位那么补零。
	 */
	private void operate_BM() {

		JDBCcon con = new JDBCcon();
		String bianm_value = this.createBM();
		StringBuffer new_bianm = new StringBuffer();
		String liusbmStr = "";

		try {
			int liusbm = Integer.valueOf(bianm_value.substring(6, 9))
					.intValue() + 1;
			if (liusbm < 10) {
				liusbmStr = "00" + liusbm;
			} else if (liusbm < 100 && liusbm >= 10) {
				liusbmStr = "0" + liusbm;
			} else {
				liusbmStr = String.valueOf(liusbm);
			}

			new_bianm.append(bianm_value.substring(0, 6)).append(liusbmStr)
					.append(bianm_value.substring(9));
			AddBM = Long.valueOf(new_bianm.toString()).longValue();
			String sql = "update meikxxbsqpfb set bianm = '"
					+ new_bianm.toString() + "' where id = " + getRowNumIndex();
			con.getUpdate(sql);
		} catch (Exception e) {
			AddBM = -1;
		} finally {
			con.Close();
		}
	}

	/**
	 * @author yinjm
	 * @return 返回从数据取到的最大编码 将煤矿地区编码、省份编码、城市编码同时作为查询条件，到meikxxb表中模糊查询bianm字段，
	 *         取出最大的编码，bianm字段共13位，其中前6位为煤矿地区编码，7位至9位为流水账号，
	 *         10至11位为省份编码，12至13位为城市编码，如果没有查询到最大的bianm， 那么流水账号从"001"开始。
	 */
	private String createBM() {

		JDBCcon con = new JDBCcon();

		String strSql = "select max(meikbm.bianm) max_bianm from\n"
				+ "(select mkpf.bianm from meikxxbsqpfb mkpf union select mk.bianm from meikxxb mk) meikbm,\n"
				+ "(select to_char(t.meikdqbm||'___'||t.s_bianm||t.c_bianm) as new_bianm from\n"
				+ "   (select mkpf.meikdqbm, s.bianm s_bianm, c.bianm c_bianm from meikxxbsqpfb mkpf, shengfb s, chengsb c\n"
				+ "  where mkpf.shengfb_id = s.id(+)\n"
				+ "    and mkpf.chengsb_id = c.id(+)\n" + "    and mkpf.id = "
				+ getRowNumIndex() + ") t\n" + ") d\n"
				+ "where meikbm.bianm like d.new_bianm";

		ResultSetList rsl = con.getResultSetList(strSql);
		String bianm_value = "";
		while (rsl.next()) {
			bianm_value = rsl.getString("max_bianm");
		}

		if (bianm_value.equals("") || bianm_value == null) {
			strSql = "select mkpf.meikdqbm||'000'||s.bianm||c.bianm as max_bianm from meikxxbsqpfb mkpf, shengfb s, chengsb c\n"
					+ "where mkpf.shengfb_id = s.id(+)\n"
					+ "  and mkpf.chengsb_id = c.id(+)\n"
					+ "  and mkpf.id = "
					+ getRowNumIndex();
			rsl = con.getResultSetList(strSql);
			while (rsl.next()) {
				bianm_value = rsl.getString("max_bianm");
			}
		}
		rsl.close();
		con.Close();
		return bianm_value;
	}

	public void getSelectData() {
		Visit visit=(Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
			String pfzt = "";
		
		if(getZhuangtValue().getId() == 0){
			pfzt = "AND M.PIFZT = 0";
		}
		if(getZhuangtValue().getId() == 1){
			pfzt = "AND M.PIFZT = 1";
		}
		String sql = "";

		sql = " select distinct  m.id,m.xuh,dc.id dcid, dc.mingc as diancxxb_id,g.DQMC as meikdqbm,\n"
				+ "case\n"
				+ "  when length(m.bianm) < 13 then\n"
				+ "   ''\n"
				+ "  else\n"
				+ "   m.bianm\n"
				+ "end as bianm,\n"
				+ "m.mingc,m.quanc, "
				+ " s.quanc  as shengfb_id, "
				+ "  c.quanc  as chengsb_id, "
				+ " m.leib, m.leix, "
				+ "  j.mingc  as jihkjb_id, "
				+ " m.piny, nvl('<font color=\"green\">已有</font>','') as color " 
				+ " from MEIKXXBsqpfb m,diancxxb dc,meikxxb x ,shengfb s,jihkjb j,chengsb c, vwgongysdq g \n"
				+ "		where m.shengfb_id=s.id(+) and m.chengsb_id=c.id(+) and m.jihkjb_id=j.id(+) " 
				+ pfzt+" and m.meikdqbm = g.DQ_BIANM(+) and dc.id=m.diancxxb_id  "
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and m.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid())
				+ " and (m.mingc=x.mingc or m.quanc=x.quanc) "
				+ " union \n"
				+ "       select distinct m.id,m.xuh,dc.id dcid, dc.mingc as diancxxb_id,g.DQMC as meikdqbm,\n"
				+ "case\n"
				+ "  when length(m.bianm) < 13 then\n"
				+ "   ''\n"
				+ "  else\n"
				+ "   m.bianm\n"
				+ "end as bianm,\n"
				+ "m.mingc,m.quanc,"
				+ "     s.quanc  as shengfb_id, "
				+ "     c.quanc  as chengsb_id, "
				+ "  m.leib, m.leix, "
				+ "  j.mingc  as jihkjb_id, "
				+ " m.piny, nvl('<font color=\"red\">未有</font>','') as color \n" 
				+ " from MEIKXXBsqpfb m,shengfb s,jihkjb j,chengsb c,diancxxb dc, vwgongysdq g \n"
				+ " where m.shengfb_id=s.id(+) and m.chengsb_id=c.id(+) and m.jihkjb_id=j.id(+) \n" 
				+ pfzt+" and m.meikdqbm = g.DQ_BIANM(+) and dc.id=m.diancxxb_id  "
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : " and m.diancxxb_id=")
				+ (this.treeid == null
						|| this.treeid.equals("")
						|| this.treeid.equals(((Visit) (this.getPage()
								.getVisit())).getDiancxxb_id()
								+ "") ? "" : this.getTreeid())
				+ " and m.id not in (select m.id  "
				+ "  from MEIKXXBsqpfb m, diancxxb dc, meikxxb x"
				+ "	where dc.id=m.diancxxb_id  "
				+ " and (m.mingc=x.mingc or m.quanc=x.quanc)) order by color desc,DCID,XUH";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("meikxxbsqpfb");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("dcid").setHeader("申请单位ID");
		egu.getColumn("dcid").setWidth(100);
		egu.getColumn("dcid").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("申请单位");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("meikdqbm").setHeader("煤矿地区");
		egu.getColumn("meikdqbm").setWidth(130);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(110);
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(80);
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("chengsb_id").setHeader("城市");
		egu.getColumn("chengsb_id").setWidth(80);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(100);
		egu.getColumn("color").setHeader("状态");
		egu.getColumn("color").setWidth(50);

		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("color").setEditor(null);

		egu.getColumn("shengfb_id").setEditor(new ComboBox());
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		((ComboBox)egu.getColumn("shengfb_id").editor).setEditable(true);
		egu.getColumn("chengsb_id").setEditor(new ComboBox());
		egu.getColumn("chengsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,quanc from chengsb"));
		((ComboBox)egu.getColumn("chengsb_id").editor).setEditable(true);
		
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from jihkjb "));

		egu.getColumn("meikdqbm").setEditor(new ComboBox());
		egu.getColumn("meikdqbm").setComboEditor(egu.gridId,new IDropDownModel("SELECT BIANM, MINGC FROM MEIKDQB ORDER BY MINGC"));
		egu.getColumn("meikdqbm").editor.allowBlank = true;

		List l = new ArrayList();
		l.add(new IDropDownBean(1, "统配"));
		l.add(new IDropDownBean(2, "地方"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("leib").setReturnId(false);
		egu.getColumn("leib").setDefaultValue("统配");

		List k = new ArrayList();
		k.add(new IDropDownBean(1, "煤"));
		k.add(new IDropDownBean(2, "油"));
		k.add(new IDropDownBean(3, "油"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(k));
		egu.getColumn("leix").setReturnId(false);
		egu.getColumn("leix").setDefaultValue("煤");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setCurrentPage(Integer.parseInt(getCurrentPage()));

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
		
		egu.addTbarBtn(new GridButton("在已有煤矿单位中查询", "function(){  "
				+ "	if(gridDiv_sm.getSelected()== null){"
				+ "		Ext.MessageBox.alert('提示信息','请选中一个煤矿'); " + "		return;"
				+ "	} " + "	var rec=gridDiv_sm.getSelected(); "
				+ "	document.all.RowNumIndex.value=rec.get('ID');  "
				+ "	document.all.InsertButtoMK.click();" + "}"));

		egu.addTbarText("-");
		egu
				.addTbarBtn(new GridButton(
						"生成编码",
						"function(){\n"
								+ "    if(gridDiv_sm.getSelected()== null){\n"
								+ "        Ext.MessageBox.alert('提示信息','请选中一个煤矿');\n"
								+ "        return;\n"
								+ "    }\n"
								+ "    if (gridDiv_sm.getSelected().get('MEIKDQBM') == '') {\n"
								+ "       Ext.MessageBox.alert('提示信息','字段 煤矿地区 不能为空');return;\n"
								+ "    }\n"
								+ "    if (gridDiv_sm.getSelected().get('SHENGFB_ID') == '') {\n"
								+ "       Ext.MessageBox.alert('提示信息','字段 省份 不能为空');return;\n"
								+ "    }\n"
								+ "    if (gridDiv_sm.getSelected().get('CHENGSB_ID') == '') {\n"
								+ "       Ext.MessageBox.alert('提示信息','字段 城市 不能为空');return;\n"
								+ "    }\n"
								+ "    document.all.RowNumIndex.value = gridDiv_sm.getSelected().get('ID');\n"
								+ "    document.all.LiuSBM.value = gridDiv_sm.getSelected().get('BIANM');\n"
								+ "	 var Mrcd = gridDiv_sm.getSelections();"
								+ "for(i = 0; i< Mrcd.length; i++){ "
								+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} "
								+ "if(Mrcd[i].get('LEIB') == ''){Ext.MessageBox.alert('提示信息','字段 类别 不能为空');return;"
								+ "}if(Mrcd[i].get('LEIX') == ''){Ext.MessageBox.alert('提示信息','字段 类型 不能为空');return;"
								+ "}if(Mrcd[i].get('JIHKJB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 计划口径 不能为空');return;"
								+ "}gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"
								+ "+ '<XUH update=\"true\">' + Mrcd[i].get('XUH')+ '</XUH>'"
								+ "+ '<DCID update=\"true\">' + Mrcd[i].get('DCID')+ '</DCID>'"
								+ "+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'"
								+ "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"
								+ "+ '<QUANC update=\"true\">' + Mrcd[i].get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"
								+ "+ '<SHENGFB_ID update=\"true\">' + Mrcd[i].get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"
								+ "+ '<CHENGSB_ID update=\"true\">' + Mrcd[i].get('CHENGSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHENGSB_ID>'"
								+ "+ '<LEIB update=\"true\">' + Mrcd[i].get('LEIB').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIB>'"
								+ "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'"
								+ "+ '<LEIX update=\"true\">' + Mrcd[i].get('LEIX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIX>'"
								+ "+ '<JIHKJB_ID update=\"true\">' + Mrcd[i].get('JIHKJB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIHKJB_ID>'"
								+ "+ '<PINY update=\"true\">' + Mrcd[i].get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"
								+ "+ '<MEIKDQBM update=\"true\">' + Mrcd[i].get('MEIKDQBM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKDQBM>'"
								+ "+ '<COLOR update=\"true\">' + Mrcd[i].get('COLOR')+ '</COLOR>'"
								+ " + '</result>' ; }"
								+ "if(gridDiv_history==''){ "
								+ "Ext.MessageBox.alert('提示信息','没有进行改动无需保存');"
								+ "}else{"
								+ "var Cobj = document.getElementById('CHANGE');"
								+ "Cobj.value = '<result>'+gridDiv_history+'</result>';"
								+ "document.all.ShengcBm.click();\n"
								+ "Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
								+ "}" + "}"));
		egu.addTbarText("-");

		String fn = "function(){"
				+ "var Mrcd = gridDiv_sm.getSelections(); "
				+ "for(i = 0; i< Mrcd.length; i++){ "
				+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} "
				+ "if(Mrcd[i].get('BIANM') == ''){Ext.MessageBox.alert('提示信息','字段 编码 不能为空');return;"
				+ "}if(Mrcd[i].get('LEIB') == ''){Ext.MessageBox.alert('提示信息','字段 类别 不能为空');return;"
				+ "}if(Mrcd[i].get('LEIX') == ''){Ext.MessageBox.alert('提示信息','字段 类型 不能为空');return;"
				+ "}if(Mrcd[i].get('JIHKJB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 计划口径 不能为空');return;"
				+ "}gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'"
				+ "+ '<XUH update=\"true\">' + Mrcd[i].get('XUH')+ '</XUH>'"
				+ "+ '<DCID update=\"true\">' + Mrcd[i].get('DCID')+ '</DCID>'"
				+ "+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'"
				+ "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'"
				+ "+ '<QUANC update=\"true\">' + Mrcd[i].get('QUANC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUANC>'"
				+ "+ '<SHENGFB_ID update=\"true\">' + Mrcd[i].get('SHENGFB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENGFB_ID>'"
				+ "+ '<CHENGSB_ID update=\"true\">' + Mrcd[i].get('CHENGSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHENGSB_ID>'"
				+ "+ '<LEIB update=\"true\">' + Mrcd[i].get('LEIB').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIB>'"
				+ "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'"
				+ "+ '<LEIX update=\"true\">' + Mrcd[i].get('LEIX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIX>'"
				+ "+ '<JIHKJB_ID update=\"true\">' + Mrcd[i].get('JIHKJB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIHKJB_ID>'"
				+ "+ '<PINY update=\"true\">' + Mrcd[i].get('PINY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINY>'"
				+ "+ '<MEIKDQBM update=\"true\">' + Mrcd[i].get('MEIKDQBM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKDQBM>'"
				+ "+ '<COLOR update=\"true\">' + Mrcd[i].get('COLOR')+ '</COLOR>'"
				+ " + '</result>' ; }"
				+ "if(gridDiv_history==''){ "
				+ "Ext.MessageBox.alert('提示信息','没有进行改动无需保存');"
				+ "}else{"
				+ "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = '<result>'+gridDiv_history+'</result>';"
				+ "document.getElementById('savebutton').click();"
				+ "Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
				+ "}" + "}";
		egu.addTbarBtn(new GridButton("批复", fn));

		if (ToAddMsg == null) {
			ToAddMsg = "";
		}

		// 从添加所选按钮回来给予的提示信息，
		if (ToAddMsg.equals("toAdd")) {
			this.setMsg(visit.getString17());
		}
		// 点击批复按钮后，成功与否的提示信息
		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('提示信息',\"" + SaveMsg + "\");";

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
			egu.addOtherScript(" var regexp=new RegExp('^" + RowNumIndex
					+ "$','gi');  "
					+ "var rec_index=gridDiv_ds.find('ID',regexp);"
					+ "gridDiv_ds.getAt(rec_index).set('BIANM','"
					+ String.valueOf(AddBM) + "');");
		}
		AddBM = -2;

		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");
		egu.addOtherScript(" grid=gridDiv_grid;");
		egu
				.addOtherScript("gridDiv_ds.on('load',function(){\n"
						+ "    document.all.currentPage.value = gridDiv_grid.getBottomToolbar().getPageData().activePage;\n"
						+ "});");

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此判断如果不是从煤矿选择批复页面跳转过来的，那么Grid显示第1页。
			if (!visit.getActivePageName().toString().equals("Meikxx_Xuanzpf")) {
				setCurrentPage("1");
			}
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString16(null);
			this.setTreeid(null);
			initNavigation();
			setZhuangtValue(null);
			setZhuangtModel(null);

			ToAddMsg = cycle.getRequestContext().getRequest().getParameter(
					"MsgAdd");
			if (ToAddMsg == null) {
				ToAddMsg = "";
			}
			
			DataSource = visit.getString13(); // 煤矿编码
			Meikdwid_value = visit.getString14(); // 已有煤矿页面中的煤矿id
			Meikdqmc_value = visit.getString15(); // 煤矿地区名称
		}
		
		initNavigation();
		getSelectData();
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

		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	private String treeid;

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
	
//批复状态下拉框
	
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

}