package com.zhiren.jt.gongys;

/*作者:王总兵
 * 日期:2010-1-11 19:14:20
 * 描述:增加分页显示行数的参数设置,默认为每页显示25行
 */


/*作者:王总兵
 * 日期:2009年10月27日16:13:10
 * 描述:修改编码的宽度
 */

/*
 * 时间：2010-01-05
 * 作者：尹佳明
 * 描述：判断保存时是否有错误数据，如果有显示在页面上
 */
/*
 * 作者：夏峥
 * 时间：2013-03-2
 * 描述：使用xl_meikxxb_id取得煤矿对应的ID，避免使用公共方法导致ID过大的问题
 */

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

public class Meikczgl extends BasePage implements PageValidateListener {
	// 系统日志表中的状态字段
	private static final String ZhangTConstant1 = "成功";

	private static final String ZhangTConstant2 = "失败";

	// 系统日志表中的类别字段
	private static final String leiBConstant = "煤矿维护";
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	private String id;  //记录前台传递过来的id
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = MainGlobal.getExtMessageBox(saveMsg, false);;
	}

	private boolean tiShi;// 给予提示信息是否显示的
	
	private boolean happenWrong; // 判断保存时是否有错误数据，true为是，false为否

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}
	
	private String Meikmc; // 保存页面上的煤矿名称
	
	public String getMeikmc() {
		return Meikmc;
	}

	public void setMeikmc(String meikmc) {
		Meikmc = meikmc;
	}
	
	private String DataSource;

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}
	
	private String wrongDataSource; // 保存时将有错误的数据拼成可以生成Record的字符串后，保存到该变量中。
	
	public String getWrongDataSource() {
		return wrongDataSource;
	}

	public void setWrongDataSource(String wrongDataSource) {
		this.wrongDataSource = wrongDataSource;
	}

	// 有可能从返回按钮返回本页面，也可能是从添加所选按钮返回本页面，标记作用
	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}
	
//	省份IDropDownModel
	public IPropertySelectionModel getShengfModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setShengfModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setShengfModels() {
		String sql = "select sf.id, sf.quanc from shengfb sf";
		setShengfModel(new IDropDownModel(sql));
	}
	
//	城市IDropDownModel
	public IPropertySelectionModel getChengsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChengsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChengsModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setChengsModels() {
		String sql = "select cs.id, cs.quanc from chengsb cs";
		setChengsModel(new IDropDownModel(sql));
	}

	private void gotochez(IRequestCycle cycle) {
		// 需要传给下个页面取值
		if (getChange() == null || "".equals(getChange())) {
			setMsg("请选中一个人员设置分组!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Chezgl");
	}

	private void UpdateDescription(IRequestCycle cycle) {

		if (getParameters() == null || "".equals(getParameters())) {
			setMsg("请选中一行添加描述!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString9(getParameters());
		cycle.activate("Meiksxglb");
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	// 判断数据是否在本地库中已经存在(不存在返回0，存在返回行数)
	private int Shujpd(JDBCcon con, String sql) {
		return JDBCcon.getRow(con.getResultSet(sql));
	}

	// 删除下级公司煤矿单位的上级编码字段
	private void DeleteXiaj(JDBCcon con, String tableName, String bianm,
			String operation) {

		InterCom_dt dt = new InterCom_dt();
		String sql = " select  d.id,d.mingc  from diancxxb d where d.fuid="
				+ ((Visit) (this.getPage().getVisit())).getDiancxxb_id();

		ResultSetList mdrsl = con.getResultSetList(sql);

		// 有下级电厂单位，需要更新
		if (mdrsl.getRows() != 0) {
			int count_success = 0;
			int count_fail = 0;
			String s = "";
			while (mdrsl.next()) {

				String diancxxb_id = mdrsl.getString("ID");
				String[] sqls = new String[] { "update meikxxb set SHANGJGSBM='' where SHANGJGSBM='"
						+ bianm + "'" };
				String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

				if (answer[0].equals("true")) { // 更新下级字段成功
					count_success++;
					s += "单位" + mdrsl.getString("MINGC") + "更新成功;";
				} else {
					count_fail++;
					s += "单位" + mdrsl.getString("MINGC") + "更新失败;";
				}

			}

			if (count_fail > 0) {
				zhuangT = ZhangTConstant2;
			} else {
				zhuangT = ZhangTConstant1;
			}
			logMsg += operation + "表" + tableName + "中编码"
					+ bianm.replaceAll("'", "") + "更新下级" + mdrsl.getRows()
					+ "个;" + s + ",总共成功:" + count_success + "个，失败:"
					+ count_fail + "个;";

		}

	}

	private void GengGSJBM(JDBCcon con, String tableName, String bianm,
			String operation, String oldBianM) {
		InterCom_dt dt = new InterCom_dt();

		String sql = " select  d.id,d.mingc  from diancxxb d where d.fuid="
				+ ((Visit) (this.getPage().getVisit())).getDiancxxb_id();

		ResultSetList mdrsl = con.getResultSetList(sql);

		// 有下级电厂单位，需要更新
		if (mdrsl.getRows() != 0) {

			int count_success = 0;
			int count_fail = 0;
			String s = "";
			while (mdrsl.next()) {

				String diancxxb_id = mdrsl.getString("ID");
				String[] sqls = new String[] { "update meikxxb set SHANGJGSBM='"
						+ bianm.replaceAll("'", "")
						+ "' where SHANGJGSBM='"
						+ oldBianM + "'" };
				String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

				if (answer[0].equals("true")) { // 更新下级字段成功
					count_success++;
					s += "单位" + mdrsl.getString("MINGC") + "更新成功;";
				} else {
					count_fail++;
					s += "单位" + mdrsl.getString("MINGC") + "更新失败;";
				}

			}

			if (count_fail > 0) {
				zhuangT = ZhangTConstant2;
			} else {
				zhuangT = ZhangTConstant1;
			}

			logMsg += operation + "表" + tableName + "中编码"
					+ bianm.replaceAll("'", "") + "更新下级" + mdrsl.getRows()
					+ "个;" + s + ",总共成功:" + count_success + "个，失败:"
					+ count_fail + "个;";
		}

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
			logMsg = "";

		}
	}

	// 新增加一条记录时，通知上级待批复
	private void DaiPF() {

	}

	public void Save1(String strchange, Visit visit) {
		
		logMsg = "";
		SaveMsg = "";
		String tableName = "meikxxb";
		JDBCcon con = new JDBCcon();
		boolean YiJGS=this.ShiFYiJGS(con);
		
//		此配制参数在马头电厂的一厂多制情况下使用并添加的
		String zhi = MainGlobal.getXitxx_item("系统信息", "一厂多制下煤矿是否关联电厂", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
		
		StringBuffer sql = new StringBuffer();
		List arraylist = new ArrayList(); // 用来保存有错误的数据

		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql = new StringBuffer("begin\n");
//			sql.append("update ").append(tableName).append(" set shiyzt = ").append(0).append(" where id = ").append(delrsl.getString(0)).append(";\n");
			if (zhi.equals("是")) {
				sql.append("delete meikdcglb where meikxxb_id = ").append(delrsl.getString("id")).append(";\n");
			}
			sql.append("end;");
			int i = con.getDelete(sql.toString());
			if (i != -1) { // 操作失败，不需要记录日志
				this.DeleteXiaj(con, tableName, delrsl.getString("BIANM"),"删除");
			} else {
				// this.DeleteXiaj(con,tableName,delrsl.getString("BIANM"),"删除");
				// 将下级单位的上级编码字段清空
			}
		}

		sql = new StringBuffer("");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql = new StringBuffer();
			String SaveMsgLocal = "";
			ResultSetList weis = con.getResultSetList("select zhi from xitxxb where mingc='煤矿编码位数' and zhuangt=1");
			if(weis.next()){
				if(mdrsl.getString("bianm").length()<weis.getInt("zhi")){
					SaveMsgLocal="煤矿编码位数小于"+weis.getInt("zhi")+",请按要求重新编码。";
					setSaveMsg(SaveMsgLocal);
					return;
				}
			}
			
			String id = "";
			ResultSetList rs = con.getResultSetList("select xl_meikxxb_id.nextval id from dual");
			if(rs.next()) {
				id = rs.getString(0);
			}
			rs.close();
//			String meikxxb_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			String meikxxb_id=visit.getDiancxxb_id()+id;
			sql2.append(meikxxb_id);
			if ("0".equals(mdrsl.getString("ID"))) {
				String sql_check = "select id from meikxxb where 1=0 ";
				sql.append("insert into ").append(tableName).append("(id");
				String mingc = "";
				String quanc = "";
				String bianm = "";
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if (!mdrsl.getColumnNames()[i].equals("MEIKDCGLB_ID")) {
						sql.append(",").append(mdrsl.getColumnNames()[i]);
					}
					if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {

						long SHENGFB_ID = ((IDropDownModel)getShengfModel()).getBeanId(mdrsl.getString("SHENGFB_ID"));

						if (mdrsl.getString("SHENGFB_ID") == null
								|| mdrsl.getString("SHENGFB_ID").equals("")) {
							sql2.append(",").append("''");
						} else if (SHENGFB_ID == -1L) {
							SaveMsgLocal += "--省份:"
									+ mdrsl.getString("SHENGFB_ID")
									+ "不存在,须先加入!<br>";
							sql2.append(",").append("''");
						} else {

							sql2.append(",").append(SHENGFB_ID);
						}

					} else if (mdrsl.getColumnNames()[i].equals("JIHKJB_ID")) {

						long JIHKJB_ID = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("jihkjb_id"));
						if (JIHKJB_ID == -1L) {
							SaveMsgLocal += "--计划口径:"
									+ mdrsl.getString("JIHKJB_ID")
									+ "不存在,须先加入!<br>";
							sql2.append(",").append("''");
						} else {
							sql2.append(",").append(JIHKJB_ID);
						}

					} else if (mdrsl.getColumnNames()[i].equals("LEIB")) {

						long LEIB = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("LEIB"));
						if (LEIB == -1L) {
							SaveMsgLocal += "--类别:" + mdrsl.getString("LEIB")
									+ "不存在,须先加入!<br>";
							sql2.append(",").append("''");
						} else {
							sql2.append(",").append(
									getValueSql(visit.getExtGrid1().getColumn(
											mdrsl.getColumnNames()[i]), mdrsl
											.getString(i)));
						}
					} else if (mdrsl.getColumnNames()[i].equals("LEIX")) {

						long LEIX = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("LEIX"));
						if (LEIX == -1L) {
							SaveMsgLocal += "--类型:" + mdrsl.getString("LEIX")
									+ "不存在,须先加入!<br>";
							sql2.append(",").append("''");
						} else {
							sql2.append(",").append(
									getValueSql(visit.getExtGrid1().getColumn(
											mdrsl.getColumnNames()[i]), mdrsl
											.getString(i)));
						}
						// //////////////////////////////////////////////////////////////城市不一样
						// 也要进行判断?
					} else if (mdrsl.getColumnNames()[i].equals("CHENGSB_ID")) {
						long CHENGSB_ID = ((IDropDownModel)getChengsModel()).getBeanId(mdrsl.getString("CHENGSB_ID"));
						if (mdrsl.getString("CHENGSB_ID") == null
								|| mdrsl.getString("CHENGSB_ID").equals("")) {
							sql2.append(",").append("''");

						} else if (CHENGSB_ID == -1L) {
							SaveMsgLocal += "--城市:"
									+ mdrsl.getString("CHENGSB_ID")
									+ "不存在,须先加入!<br>";
							sql2.append(",").append("''");
						} else {
							sql2.append(",").append(CHENGSB_ID);
						}
					} else if (mdrsl.getColumnNames()[i].equals("MEIKDQ_ID")) {
						long MEIKDQ_ID = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("MEIKDQ_ID"));
						if (mdrsl.getString("MEIKDQ_ID") == null
								|| mdrsl.getString("MEIKDQ_ID").equals("")) {
							sql2.append(",").append("default");

						} else if (MEIKDQ_ID == -1L) {
							SaveMsgLocal += "--煤矿地区:"
									+ mdrsl.getString("MEIKDQ_ID")
									+ "不存在,须先加入!<br>";
							sql2.append(",").append("default");
							
						} else {
							sql2.append(",").append(MEIKDQ_ID);
						}
					} else if (mdrsl.getColumnNames()[i].equals("MEIKDCGLB_ID")) {
						String diancxxb_id = "default";
						long MEIKDCGLB_ID = (getExtGrid().getColumn("MEIKDCGLB_ID").combo).getBeanId(mdrsl.getString("MEIKDCGLB_ID"));
						if (MEIKDCGLB_ID != -1L) {
							diancxxb_id = String.valueOf(MEIKDCGLB_ID);
						}
						String strsql = "insert into meikdcglb(id, diancxxb_id, meikxxb_id) values(getnewid("+ visit.getDiancxxb_id() +"), "+ diancxxb_id +", "+ meikxxb_id +")";
						con.getInsert(strsql);
					} else {
						if (mdrsl.getColumnNames()[i].equals("BIANM")) {
							bianm = getValueSql(visit.getExtGrid1().getColumn(
									mdrsl.getColumnNames()[i]), mdrsl
									.getString(i));
							sql_check += " or bianm=" + bianm + "";
						}
						if (mdrsl.getColumnNames()[i].equals("MINGC")) {
							mingc = getValueSql(visit.getExtGrid1().getColumn(
									mdrsl.getColumnNames()[i]), mdrsl
									.getString(i));
							sql_check += " or mingc=" + mingc + "";
						}
						if (mdrsl.getColumnNames()[i].equals("QUANC")) {
							quanc = getValueSql(visit.getExtGrid1().getColumn(
									mdrsl.getColumnNames()[i]), mdrsl
									.getString(i));
							sql_check += " or quanc=" + quanc + "";
						}
						
						if (mdrsl.getColumnNames()[i].equals("SHANGJGSBM") && mdrsl.getString("SHANGJGSBM").equals("") && !YiJGS){
							sql2.append(",").append(
									mdrsl.getString("BIANM"));
						}else if(!mdrsl.getColumnNames()[i].equals("MEIKDCGLB_ID")){
							sql2.append(",").append(
									getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}
				}

				// 当SHANGJGSBM不为空时，说明是从上级导入的煤矿单位，无需批复即可使用
				if (!mdrsl.getString("SHANGJGSBM").equals("")) {

					if (this.Shujpd(con, sql_check) != 0) { // 在本地库中已经存在,要进行的操作
						SaveMsgLocal += "----------------添加记录-----------<br>--编码:"
								+ bianm
								+ "---名称:"
								+ mingc
								+ "---全称:"
								+ quanc
								+ "--的记录有重复,不能保存!<br>";
						arraylist.add(mdrsl.getResultSetlist().get(mdrsl.getRow())); // 将有错误的数据放到一个list中
					} else if (SaveMsgLocal.equals("")) {
						sql.append(") values(").append(sql2).append(")\n");
						int flag = con.getInsert(sql.toString());
						// 插入时不需要记录日志
						if (flag == -1) { // 插入不成功

						} else { // 插入成功

						}
					} else if (!SaveMsgLocal.equals("")) {
						arraylist.add(mdrsl.getResultSetlist().get(mdrsl.getRow())); // 将有错误的数据放到一个list中
					}
				} else {
					if (this.Shujpd(con, sql_check) != 0) { // 在本地库中已经存在,要进行的操作
						SaveMsgLocal += "----------------添加记录-----------<br>--编码:"
								+ bianm
								+ "---名称:"
								+ mingc
								+ "---全称:"
								+ quanc
								+ "--的记录有重复,不能保存!<br>";
						arraylist.add(mdrsl.getResultSetlist().get(mdrsl.getRow())); // 将有错误的数据放到一个list中
					} else if (SaveMsgLocal.equals("")) {
						sql.append(") values(").append(sql2).append(")\n");
						int flag = con.getInsert(sql.toString());

						if (flag == -1) { // 插入不成功

						} else { // 插入成功
							this.DaiPF(); // 插入一条新的记录时，通知上级单位，为待批复状态

						}
					} else if (!SaveMsgLocal.equals("")) {
						arraylist.add(mdrsl.getResultSetlist().get(mdrsl.getRow())); // 将有错误的数据放到一个list中
					}
				}
				SaveMsg += SaveMsgLocal;
				// 说明所有字段在本地库中都存在，保存成功

			} else {
				// 在update过程中不需要进行判断有的字段是否存在
				sql = new StringBuffer();
				String bianm = "";
				String mingc = "";
				String quanc = "";
				StringBuffer sql_update = new StringBuffer();
				sql.append("update ").append(tableName).append(" set ");
				sql_update.append("select id from ").append(tableName).append(" where ( 1=0 ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if (!mdrsl.getColumnNames()[i].equals("MEIKDCGLB_ID")) {
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					}
					if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {
						
						sql.append(((IDropDownModel)getShengfModel()).getBeanId(mdrsl.getString("SHENGFB_ID"))).append(",");
					} else if(mdrsl.getColumnNames()[i].equals("CHENGSB_ID")) {
						
						sql.append(((IDropDownModel)getChengsModel()).getBeanId(mdrsl.getString("CHENGSB_ID"))).append(",");
					} else if(mdrsl.getColumnNames()[i].equals("JIHKJB_ID")) {

						sql
								.append(
										(getExtGrid().getColumn(
												mdrsl.getColumnNames()[i]).combo)
												.getBeanId(mdrsl
														.getString("jihkjb_id")))
								.append(",");
					} else if (mdrsl.getColumnNames()[i].equals("BIANM")) {
						bianm = getValueSql(visit.getExtGrid1().getColumn(
								mdrsl.getColumnNames()[i]), mdrsl.getString(i));
						sql_update.append(" or bianm=").append(bianm);
						sql.append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");

					} else if (mdrsl.getColumnNames()[i].equals("MINGC")) {
						mingc = getValueSql(visit.getExtGrid1().getColumn(
								mdrsl.getColumnNames()[i]), mdrsl.getString(i));
						sql_update.append(" or mingc=").append(mingc);
						sql.append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");

					}
//					else if (mdrsl.getColumnNames()[i].equals("QUANC")) {
//						quanc = getValueSql(visit.getExtGrid1().getColumn(
//								mdrsl.getColumnNames()[i]), mdrsl.getString(i));
//						sql_update.append(" or quanc=").append(quanc);
//						sql.append(
//								getValueSql(visit.getExtGrid1().getColumn(
//										mdrsl.getColumnNames()[i]), mdrsl
//										.getString(i))).append(",");
//
//					} 
					else if (mdrsl.getColumnNames()[i].equals("MEIKDQ_ID")) {
						long MEIKDQ_ID = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("MEIKDQ_ID"));
						if (MEIKDQ_ID == -1L) {
								sql.append("default").append(",");
							} else {
								sql.append(MEIKDQ_ID).append(",");
							}
					} else if (mdrsl.getColumnNames()[i].equals("MEIKDCGLB_ID")) {
						String diancxxb_id = "default";
						long MEIKDCGLB_ID = (getExtGrid().getColumn("MEIKDCGLB_ID").combo).getBeanId(mdrsl.getString("MEIKDCGLB_ID"));
						if (MEIKDCGLB_ID != -1L) {
							diancxxb_id = String.valueOf(MEIKDCGLB_ID);
						}
						String select = "select id from meikdcglb where meikxxb_id = " + mdrsl.getString("id");
						ResultSetList rsl = con.getResultSetList(select);
						String strsql;
						if (rsl.getRows() > 0) {
							strsql = "update meikdcglb set diancxxb_id = "+ diancxxb_id +" where meikxxb_id = " + mdrsl.getString("id");
						} else {
							strsql = "insert into meikdcglb(id, diancxxb_id, meikxxb_id) values(getnewid("+ visit.getDiancxxb_id() +"), "+ diancxxb_id +", "+ mdrsl.getString("id") +")";
						}
						con.getUpdate(strsql);
						rsl.close();
					}else {
						
						if (mdrsl.getColumnNames()[i].equals("SHANGJGSBM")  && !YiJGS){
						
							sql.append(mdrsl.getString("BIANM")
									);
						}else if(!mdrsl.getColumnNames()[i].equals("MEIKDCGLB_ID")){
							sql.append(
									getValueSql(visit.getExtGrid1().getColumn(
											mdrsl.getColumnNames()[i]), mdrsl
											.getString(i))).append(",");
						}
						
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append("\n");
				sql_update.append(") and id<>").append(mdrsl.getString("ID"));

				int flag = this.Shujpd(con, sql_update.toString());

				if (flag != 0) { // 更新的数据在本地库中已经存在

					SaveMsg += "----------------更改记录-----------<br>--编码:"
							+ bianm + "---名称:" + mingc + "---全称:" + quanc
							+ "--的记录有重复,不能保存<br>!";
				} else {

					String sql_change = " select bianm from " + tableName
							+ " where id=" + mdrsl.getString("ID");
					String oldValue = bianM_Change(con, sql_change); // t为true说明需要更改，调用接口
					int k = con.getUpdate(sql.toString());
					if (k != 0) { // 更新数据成功

						if (oldValue != null && bianm != null
								&& !oldValue.equals(bianm.replaceAll("'", ""))) {

							this.GengGSJBM(con, tableName, bianm, "更改",
									oldValue);
						}
						// 通过判断更改bianm字段前后的变化与否来判断是否更新下级单位的上级编码字段

					} else { // 不成功

					}
				}
			}
		}

		this.WriteLog(con);
		if (SaveMsg.equals("")) {
			SaveMsg += "更新成功";
		} else if (arraylist.size() > 0) {
//			将有错误的数据拼成可以生成Record的字符串，保存到wrongDataSource变量中。
			String str2 = "";
			StringBuffer strsb = new StringBuffer();
			for(int i = 0; i < arraylist.size(); i ++) {
				String str = "";
				for (int j = 0; j < mdrsl.getColumnCount(); j ++) {
					str += mdrsl.getColumnNames()[j]+":'"+((String[])arraylist.get(i))[j]+"',";
				}
				str2 = "{"+str.substring(0, str.lastIndexOf(","))+"}";
				strsb.append(str2).append("&");
			}
			setWrongDataSource(strsb.substring(0, strsb.lastIndexOf("&")));
			happenWrong = true; // 保存时页面有错误数据。
		}
		ToAddMsg = ""; // 更改后标识，上级导入的页面不再显示
		tiShi = true;
	}

	// 查询编码值（根据id），判断前后是否改变，进而判断是否需要更改下级单位的上级编码字段
	public String bianM_Change(JDBCcon con, String sql) {

		ResultSetList rsl = con.getResultSetList(sql);
		String oldValue = null;
		if (rsl.next()) {
			oldValue = rsl.getString("bianm");
		}
		// 需要调用接口更改下级单位的上级编码字段
		return oldValue;
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
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	private boolean _BeginChick=false;
	
	public void BeginButtonQY(IRequestCycle cycle){
		_BeginChick = true;
	}
	
	private boolean _StopChick = false;
	
	public void StopButtonTY(IRequestCycle cycle){
		_StopChick = true;
	}
	

//	private boolean _DeleteChick = false;
//
//	public void DeleteButton(IRequestCycle cycle) {
//		_DeleteChick = true;
//	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}

	private boolean _SZChick = false;

	public void SZButton(IRequestCycle cycle) {
		_SZChick = true;
	}

	private boolean _InsertButtoMK = false;

	public void InsertButtoMK(IRequestCycle cycle) {
		_InsertButtoMK = true;

	}
	
//	"关联供应商"按钮
	private boolean _GuanlgysClick = false;
	
	public void GuanlgysButton(IRequestCycle cycle) {
		_GuanlgysClick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
//	"设置元素分析项目"按钮
	private boolean _ShezfxxmButton = false;
	
	public void ShezfxxmButton(IRequestCycle cycle) {
		_ShezfxxmButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
//			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;

		}
		if(_BeginChick){
			_BeginChick = false;
			Begin();
			getSelectData();
		}
		if(_StopChick){
			_StopChick = false;
			Stop();
			getSelectData();
		}
//		if (_DeleteChick) {
//			_DeleteChick = false;
//			Save();
//		}
		if (_RbChick) {
			_RbChick = false;
			gotochez(cycle);
		}
		if (_SZChick) {
			_SZChick = false;
			UpdateDescription(cycle);
		}
		if (_GuanlgysClick) {
			_GuanlgysClick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString9(getParameters()); // 将煤矿id传到下个页面
			visit.setString10(getMeikmc()); // 将煤矿名称传到下个页面
			cycle.activate("Meikglgys");
		}
		if (_ShezfxxmButton) {
			_ShezfxxmButton = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString9(getParameters()); // 将煤矿id传到下个页面
			visit.setString10(getMeikmc()); // 将煤矿名称传到下个页面
			cycle.activate("Meikysfx");
		}
		if (_InsertButtoMK) {
			_InsertButtoMK = false;
			cycle.activate("Meikxx_Tjyy");
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			ToAddMsg = "";
		}
	}
	
	private boolean ShiFYiJGS(JDBCcon con){ //判断是否是一级公司
		String sql=" select zhi from xitxxb where mingc='入口地址'";
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			if(rsl.getString("ZHI")!=null && !rsl.getString("ZHI").equals("")){
				return true;
			}
		}
		return false;
	}

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str1 ="";
		if(getShiyztValue().getId() == 1){
			str1="and m.shiyzt = 1 \n";
		}else{
			str1="and m.shiyzt = 0 \n";
		}
		
//		此配制参数在马头电厂的一厂多制情况下使用并添加的
		String str = MainGlobal.getXitxx_item("系统信息", "一厂多制下煤矿是否关联电厂", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
		
		String sql = "";
		if (str.equals("是")) {
			sql = 
				"select distinct m.id,\n" +
				"                m.xuh,\n" + 
				"                g.mingc meikdq_id,\n" + 
				"                m.bianm,\n" + 
				"                m.mingc,\n" + 
				"                m.quanc,\n" + 
				"                m.piny,\n" + 
				"                dc.mingc as meikdcglb_id,\n" + 
				"                s.quanc as shengfb_id,\n" + 
				"                c.quanc as chengsb_id,\n" + 
				"                m.leib,\n" + 
				"                m.leix,\n" + 
				"                j.mingc as jihkjb_id,\n" + 
				"                m.danwdz,\n" + 
				"                m.beiz,\n" + 
				"                m.shangjgsbm\n" + 
				"  from meikxxb m, shengfb s, jihkjb j, chengsb c, gongysb g, diancxxb dc, meikdcglb dcgl\n" + 
				" where m.shengfb_id = s.id(+)\n" + 
				"   and m.chengsb_id = c.id(+)\n" + 
				"   and m.jihkjb_id = j.id(+)\n" + 
				"   and m.meikdq_id = g.id(+)\n" + 
				"   and m.id = dcgl.meikxxb_id(+)\n" + 
				"   and dcgl.diancxxb_id = dc.id(+)\n" + str1+
				" order by m.xuh";
		} else {
			sql = 
				"select distinct m.id,\n" +
				"                m.xuh,\n" +
				"                g.mingc meikdq_id,\n" + 
				"                m.bianm,\n" + 
				"                m.mingc,\n" + 
				"                m.quanc,\n" + 
				"                m.piny,\n" + 
				"                s.quanc as shengfb_id,\n" + 
				"                c.quanc as chengsb_id,\n" + 
				"                m.leib,\n" + 
				"                m.leix,\n" + 
				"                j.mingc as jihkjb_id,\n" + 
				"                m.danwdz,\n" + 
				"                m.beiz,\n" + 
				"                m.shangjgsbm\n" +
				"  from meikxxb m, shengfb s, jihkjb j, chengsb c, gongysb g\n" + 
				" where m.shengfb_id = s.id(+)\n" + 
				"   and m.chengsb_id = c.id(+)\n" + 
				"   and m.jihkjb_id = j.id(+)\n" + 
				"   and m.meikdq_id = g.id(+)\n" + str1 +
				" order by m.xuh";
		}
		
//		sql = "select distinct m.id,m.xuh,m.bianm,m.mingc,m.quanc,m.piny,s.quanc  as shengfb_id,c.quanc as chengsb_id,m.leib, \n"
//				+ "m.leix, j.mingc as jihkjb_id,m.danwdz,m.beiz,m.shangjgsbm from meikxxb m,shengfb s,jihkjb j,chengsb c where m.shengfb_id=s.id(+) and m.chengsb_id=c.id(+) and m.jihkjb_id=j.id(+) \n"
//				+ "order by m.xuh \n";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikxxb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(95);
		egu.getColumn("meikdq_id").setHeader("煤矿地区");
		egu.getColumn("meikdq_id").setWidth(130);
		egu.getColumn("bianm").editor.allowBlank = false;
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(150);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(80);
		if(str.equals("是")){
			egu.getColumn("meikdcglb_id").setHeader("所属电厂");
		}
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("danwdz").setHeader("单位地址");
		egu.getColumn("danwdz").setWidth(80);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		egu.getColumn("shangjgsbm").setHeader("上级公司编码");
		egu.getColumn("shangjgsbm").setWidth(80);
		egu.getColumn("shangjgsbm").setEditor(null);
		egu.getColumn("chengsb_id").setHeader("城市");
		egu.getColumn("chengsb_id").setWidth(80);
		egu.getColumn("chengsb_id").setEditor(null);

//		egu.getColumn("chengsb_id").setEditor(new ComboBox());
//		egu.getColumn("chengsb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id,quanc from chengsb"));
//		egu.getColumn("chengsb_id").editor.allowBlank = true;
//		
//		egu.getColumn("shengfb_id").setEditor(new ComboBox());
//		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id,quanc from shengfb"));
//		egu.getColumn("shengfb_id").editor.allowBlank = true;
		
		egu.getColumn("meikdq_id").setEditor(new ComboBox());
		egu.getColumn("meikdq_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id, mingc from gongysb where leix = 0 order by mingc"));
		egu.getColumn("meikdq_id").editor.allowBlank = true;
		
		if (str.equals("是")) {
			egu.getColumn("meikdcglb_id").setEditor(new ComboBox());
			egu.getColumn("meikdcglb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select dc.id, dc.mingc from diancxxb dc where dc.fuid = "+ visit.getDiancxxb_id() +" order by dc.id"));
			egu.getColumn("meikdcglb_id").editor.allowBlank = true;
		}
		
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from jihkjb "));
		
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
		String meikhangs="select zhi from xitxxb where mingc='煤矿维护页面单页显示的行数' and zhuangt=1";
		rsl = con.getResultSetList(meikhangs);
		if(rsl.next()){
			String zhi=rsl.getString("zhi");
			egu.addPaging(Integer.parseInt(zhi));
		}else{//默认每页显示25行
			egu.addPaging(25);
		}
		egu.addTbarText("使用状态:");
		ComboBox cb = new ComboBox();
		cb.setTransform("SHIYZT");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(cb.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新",
		"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		if(this.ShiFYiJGS(con)){
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("添加已有煤矿","function(){document.all.InsertButtoMK.click();}"));
		}
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		if(getShiyztValue().getId() == 0){
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("启用","function(){var gridRow = gridDiv_sm.getSelected();" +
				"document.getElementById('id').value = gridRow.get('ID');document.all.BeginButtonQY.click();}"));
		}else{
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("停用","function(){var gridRow = gridDiv_sm.getSelected();" +
				"document.getElementById('id').value = gridRow.get('ID');document.all.StopButtonTY.click();}"));
		}
//		egu.addToolbarButton(GridButton.ButtonType_Delete, "deletebutton");
		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		String sPowerHandler = "function(){"
				+ "if(gridDiv_sm.getSelected()== null){"
				+ "	Ext.MessageBox.alert('提示信息','请选中一个煤矿再设置车站');" + "	return;"
				+ "}" + "var grid_rcd = gridDiv_sm.getSelected();"
				+ "if(grid_rcd.get('ID') == '0'){"
				+ "	Ext.MessageBox.alert('提示信息','在设置车站之前请先保存!');" + "	return;"
				+ "}" + "grid_history = grid_rcd.get('ID');"
				+ "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = grid_history;"
				+ "document.getElementById('RbButton').click();" + "}";
		egu.addTbarBtn(new GridButton("设置车站", sPowerHandler));
		egu.addTbarText("-");
		String str2 = "if(gridDiv_sm.getSelected()== null){ \n"
				+ "	Ext.MessageBox.alert('提示信息','请选中一个煤矿再设置属性'); \n"
				+ "	return; \n" + "} \n"
				+ "var rec = gridDiv_sm.getSelected(); \n"
				+ " var id = rec.get('ID'); 	\n" + "if(id == '0'){ \n"
				+ "	Ext.MessageBox.alert('提示信息','在设置属性之前请先保存!'); \n"
				+ "	return; \n" + "} \n"
				+ " var cobjid = document.getElementById('PARAMETERS')\n"
				+ " cobjid.value = id; \n"
				+ " document.getElementById('SZButton').click(); \n";

		egu.addTbarBtn(new GridButton("设置属性", "function(){" + str2 + "}"));
		
		egu.addTbarText("-");
		String shezfxxm_click = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelected()== null){\n" + 
			"        Ext.MessageBox.alert('提示信息','请选中一个煤矿再设置项目');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var rec = gridDiv_sm.getSelected();\n" + 
			"    if(rec.get('ID') == '0'){\n" + 
			"        Ext.MessageBox.alert('提示信息','在设置项目之前请先保存!');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var cobjid = document.getElementById('PARAMETERS')\n" + 
			"    cobjid.value = rec.get('ID');\n" +
			"    var meikmc = document.getElementById('Meikmc')\n" + 
			"    meikmc.value = rec.get('MINGC');\n" + 
			"    document.getElementById('ShezfxxmButton').click();\n" +
			"}";
		egu.addTbarBtn(new GridButton("设置元素分析项目", shezfxxm_click));
		
		egu.addTbarText("-");
		String guanlgys_click = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelected()== null){\n" + 
			"        Ext.MessageBox.alert('提示信息','请选中一个煤矿再关联供应商');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var rec = gridDiv_sm.getSelected();\n" + 
			"    if(rec.get('ID') == '0'){\n" + 
			"        Ext.MessageBox.alert('提示信息','在关联供应商之前请先保存!');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var cobjid = document.getElementById('PARAMETERS')\n" + 
			"    cobjid.value = rec.get('ID');\n" +
			"    var meikmc = document.getElementById('Meikmc')\n" + 
			"    meikmc.value = rec.get('MINGC');\n" + 
			"    document.getElementById('GuanlgysButton').click();\n" + 
			"}";
		egu.addTbarBtn(new GridButton("关联供应商", guanlgys_click));
		
		// 从添加所选按钮回来给予的提示信息，
		if (ToAddMsg.equals("toAdd")) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
		}
		
//		判断是否有错误数据，如果有显示在页面上
		if (happenWrong) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getWrongDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
			happenWrong = false;
		}
		
		String shengf_click = 
			"gridDiv_grid.on('cellclick',\n" +
			"function(own, irow, icol, e){\n" + 
			"    row = irow;\n" + 
			"    if('SHENGFB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){\n" + 
			"        shengfTree_window.show();\n" + 
			"    }\n" + 
			"});";
		egu.addOtherScript(shengf_click);
		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");

		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('提示信息',\"" + SaveMsg + "\");";
		} 
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_sf_cs, "shengfTree", ""+visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		String handler = 
			"function() {\n" +
			"    var cks = shengfTree_treePanel.getSelectionModel().getSelectedNode();\n" + 
			"    if(cks==null){\n" + 
			"        shengfTree_window.hide();\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    rec = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"    if(cks.getDepth() == 2){\n" + 
			"        rec.set('SHENGFB_ID', cks.parentNode.text);\n" + 
			"        rec.set('CHENGSB_ID', cks.text);\n" + 
			"    }else if(cks.getDepth() == 1){\n" + 
			"        rec.set('SHENGFB_ID', cks.text);\n" +
			"        rec.set('CHENGSB_ID', '');\n" + 
			"    }\n" + 
			"    shengfTree_window.hide();\n" + 
			"    return;\n" + 
			"}";
		
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);

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
	
	public String getTreeScript() {
//		System.out.print(((Visit)this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			setShengfModel(null);
			setShengfModels();
			setChengsModel(null);
			setChengsModels();
			setShiyztValue(null);
			setShiyztModel(null);
//			visit.setExtGrid1(null);

			ToAddMsg = cycle.getRequestContext().getRequest().getParameter("MsgAdd");

			if (ToAddMsg == null) {
				ToAddMsg = "";
			}

			DataSource = visit.getString13();

			getSelectData();
		} else {
			getSelectData();
		}
	}
		
		//使用下拉框
		public IDropDownBean getShiyztValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getShiyztModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setShiyztValue(IDropDownBean value) {

			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}

		public void setShiyztModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getShiyztModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getShiyztModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public IPropertySelectionModel getShiyztModels() {
			List shiyzt = new ArrayList();
			shiyzt.add(new IDropDownBean(1, "使用中"));
			shiyzt.add(new IDropDownBean(0, "未使用"));
			((Visit) getPage().getVisit())
					.setProSelectionModel5(new IDropDownModel(shiyzt));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}
		
	//启用的方法	
		public void Begin(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "update meikxxb set shiyzt = 1 where id ="+getId()+"";
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("启用成功!");
				}
				else{
					setSaveMsg("启用失败!");
				}
		}
	//停用的方法	
		public void Stop(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "update meikxxb set shiyzt = 0 where id ="+getId()+"";
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("停用成功!");
				}
				else{
					setSaveMsg("停用失败!");
				}
		}
		
	}

