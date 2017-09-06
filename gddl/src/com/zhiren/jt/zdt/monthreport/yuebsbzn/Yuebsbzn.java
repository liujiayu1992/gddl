package com.zhiren.jt.zdt.monthreport.yuebsbzn;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.*;
import com.zhiren.common.tools.FtpCreatTxt;
import com.zhiren.common.tools.FtpUpload;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
 * 时间：2009-04-07 
 * 作者： ll
 * 修改内容：1、修改cpi01上传sql，把gongysb改为中能供货单位表（zhongnghdwb），把shengfb改为中能煤矿地区表(zhongnmkdqb)
 *			2、编码位数不够，以0补充位数。
 *			3、在xitxxb中增加，连接中能数据库的ODBC名称。
 */
/* 
 * 时间：2009-04-14
 * 作者： ll
 * 修改内容：1、在xitxxb中增加，连接中能SQLServer数据库的用户名和密码。
 *			2、修改调燃01表sql,yueshcyb按电厂排序。
 *			3、增加电厂编码为0或null的提示框，增加中电投供货单位与中能供货单位没匹配上的提示框。
 */
/* 
 * 时间：2009-04-15
 * 作者： ll
 * 修改内容：1、增加判断调燃01和调燃02上报中能电厂数据是否完整，不完成提示那些厂什么填报没有审核数据。
 */
/* 
 * 时间：2009-05-25
 * 作者： ll
 * 修改内容：1、替换表名,把yuezbb_zdt改为yuezbb。
 * 		   
 */
/* 
 * 时间：2009-06-1
 * 作者： ll
 * 修改内容：1、修改调燃02表中中能供应商关联的sql。
 * 		   
 */

/* 
 * 时间：2009-06-23
 * 作者： chh
 * 修改内容：1、修改cpi01表上报中能矿别编码的取值由8位改为6位。
 * 		   
 */
/* 
 * 时间：2009-07-30
 * 作者： ll
 * 修改内容：1、修改cpi燃料管理01表上报中能，不上报闵行、外高桥、杨树浦电厂数据。
 * 		   
 */
/* 
 * 时间：2009-8-3
 * 作者： sy
 * 修改内容：判断cpi01表电厂编码，若为阜新电厂编码为212118
 * 		   
 */
/* 
 * 时间：2009-8-3
 * 作者： sy
 * 修改内容：判断上报中能电厂编码是否为null或0增加判断条件leix=1（上报）
 * 		   
 */


public class Yuebsbzn extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg1 = "";

	public String getMsg() {
		return msg1;
	}

	public void setMsg(String msg) {
		this.msg1 = MainGlobal.getExtMessageBox(msg, false);
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

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _ShangbClick = false;

	public void ShangbButton(IRequestCycle cycle) {
		_ShangbClick = true;
	}

	// 按钮事件处理

	private boolean _DiaorszChick = false;

	public void DiaorszButton(IRequestCycle cycle) {
		_DiaorszChick = true;
	}

	private boolean _GonghdwszChick = false;

	public void GonghdwszButton(IRequestCycle cycle) {
		_GonghdwszChick = true;
	}

	private boolean _ZhongnghdwszChick = false;

	public void ZhongnghdwszButton(IRequestCycle cycle) {
		_ZhongnghdwszChick = true;
	}

	private boolean _DanwmcszChick = false;

	public void DanwmcszButton(IRequestCycle cycle) {
		_DanwmcszChick = true;
	}

	public void submit(IRequestCycle cycle) {
		// Visit visit = (Visit) this.getPage().getVisit();

		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		if (_ShangbClick) {
			_ShangbClick = false;
			Shangb();
			if (diancbmc_change == false) {
				if (diancbzt_change == false) {
					getUploadFtp(Tablename);
				}
			}
			getSelectData();
		}
		if (_DiaorszChick) {
			_DiaorszChick = false;
			Diaorsz(cycle);
		}
		if (_GonghdwszChick) {
			_GonghdwszChick = false;
			Gonghdwsz(cycle);
		}
		if (_ZhongnghdwszChick) {
			_ZhongnghdwszChick = false;
			Zhongnghdwsz(cycle);
		}
		if (_DanwmcszChick) {
			_DanwmcszChick = false;
			Danwmcsz(cycle);
		}
	}

	// **************************上报中能*********************************//
	private String ZhongNbm = "000007";

	private String ZhongNyh = "";// ftp用户名

	private String ZhongNmm = "";// ftp用户名

	private String ZhongNip = "";// 127.0.0.1";//"210.77.176.26";

	private String ZhongN_ODBC = "";

	private String ZhongNyh_sql = "";// SQLServer用户名;

	private String ZhongNmm_sql = "";// SQLServer密码;
	// ---------------------------------------------------------------------------

	private String Tablename = "";// 上传文件的文件名

	StringBuffer msg = new StringBuffer();// 记录数据上传是否成功

	private void getSettings() {
		JDBCcon cn = new JDBCcon();
		try {
			ResultSet rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能编码'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongNbm = rs.getString("zhi");
				}
				;
			}
			rs.close();

			rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能Ftp月报用户'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongNyh = rs.getString("zhi");
				}
				;
			}
			rs.close();

			rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能Ftp月报密码'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongNmm = rs.getString("zhi");
				}
				;
			}
			rs.close();

			rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能Ftp服务器ip'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongNip = rs.getString("zhi");
				}
				;
			}
			rs.close();

			rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能ODBC名称'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongN_ODBC = rs.getString("zhi");
				}
				;
			}
			rs.close();

			rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能SQLServer用户'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongNyh_sql = rs.getString("zhi");
				}
				;
			}
			rs.close();

			rs = cn
					.getResultSet("select zhi from xitxxb where mingc='中能SQLServer密码'");
			while (rs.next()) {
				if (rs.getString("zhi") != null) {
					ZhongNmm_sql = rs.getString("zhi");
				}
				;
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.closeRs();
			cn.Close();
		}
	}

	// public void ShangbznUpdate(){//上报中能以后改变shouhcrbb 的状态为1,不允许电厂修改
	// JDBCcon con = new JDBCcon();
	// // String riq=DateUtil.FormatDate(_BeginriqValue);
	// String sql="update shouhcrbb s set s.zhuangt=1\n" +
	// // "where s.riq=to_date('"+riq+"','yyyy-mm-dd')\n" +
	// "and s.diancxxb_id in (select kj.diancxxb_id from dianckjpxb kj where
	// kj.kouj='收耗存日报' and kj.shujsbzt=1)";
	//		
	// int isUpdateTrue = con.getUpdate(sql);
	// if(isUpdateTrue==0){
	// System.out.println("上传中能日报更新收耗存日报表的状态没有成功!");
	// }
	// con.Close();
	// }
	// **********************************检查提示的内容*************************************************
	private boolean gonghdw_change = false;

	private String gonghdwmc = "";

	// 判断中电投供货单位是否与中能供货单位，全部匹配。
	public boolean Gonghdw_zn() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gonghdwmc_zdt = "";
		String zhongnghdwsql = "select gy.id,gy.mingc as mingc,nvl(zngh2.id,0) as znghid\n"
				+ "from (select distinct gys.id as id,gys.mingc \n"
				+ "          from gongysb gys,yuetjkjb kj where kj.gongysb_id=gys.id) gy,\n"
				+ "       (select zngh.id as id,zngl.gongysb_id from zhongnghdwglb zngl,zhongnghdwb zngh where zngh.id=zngl.zhongnghdwb_id)zngh2\n"
				+ "where  gy.id=zngh2.gongysb_id(+)\n" + "order by zngh2.id ";

		ResultSet zhongnghdw = con.getResultSet(zhongnghdwsql);
		gonghdw_change = false;
		gonghdwmc = "";
		try {
			// 判断中电投供货单位匹配的中能供货单位id是否为0，为0记下供应单位名称。
			while (zhongnghdw.next()) {
				if (zhongnghdw.getString("znghid").equals("0")) {
					gonghdw_change = true;
					gonghdwmc_zdt = zhongnghdw.getString("mingc") + ",";
				}
				gonghdwmc = gonghdwmc_zdt + gonghdwmc;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			con.Close();
		}
		return gonghdw_change;
	}

	// 判断上报中能电厂编码是否为null或0。
	private boolean diancbmc_change = false;

	private String diancmc = "";

	public boolean Diancbm_zn(String baobmc) {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancmc_zdt = "";
		int baoblx = 0;
		if (baobmc.equals("调燃01表") || baobmc.equals("调燃02表")) {
			baoblx = 1;
		} else {
			baoblx = 2;
		}
		String diancmcsql = "select dc.mingc as mingc,dc.bianm as bianm,dy.* from koujdyb dy,diancxxb dc\n"
				+ "where dy.diancxxb_id=dc.id and dy.koujmcb_id="
				+ baoblx
				+" and leix=1  "
				+ "\n" + "order by dc.bianm desc";

		ResultSet diancbm = con.getResultSet(diancmcsql);
		diancbmc_change = false;
		diancmc = "";
		try {
			// //判断中电投上报中能的电厂编码是否为0或null，为0记下电厂单位名称。
			while (diancbm.next()) {
				if (diancbm.getString("bianm").equals("0")
						|| diancbm.getString("bianm").equals(null)
						|| diancbm.getString("bianm").equals("")) {
					diancbmc_change = true;
					diancmc_zdt = diancbm.getString("mingc") + ",";
				}
				diancmc = diancmc_zdt + diancmc;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			con.Close();
		}
		return diancbmc_change;
	}

	// 判断调燃01、02表中电厂与上报中能的电厂是否一致。
	private boolean diancbzt_change = false;

	private String diancmc_y = "";

	private String diancmc_zb = "";

	private String diancmc_m = "";

	private String diancmc_s = "";

	private String diancmc_cg = "";

	public boolean Diancmczt_zn(String baobmc) {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int baoblx = 0;
		if (baobmc.equals("调燃01表") || baobmc.equals("调燃02表")) {
			baoblx = 1;
		} else {
			baoblx = 2;
		}
		String diancztsql = "";
		if (baobmc.equals("调燃01表")) {
			diancztsql = "select * from (\n"
					+ "select nvl(shcy.id,0) as ydiancid,nvl(yzb.id,0) as zbdiancid,nvl(shcm.id,0) as mdiancid,nvl(kj.id,0) as znid,kj.mingc as znmc\n"
					+ "from (select distinct nvl(yshc.diancxxb_id,0) as id  from yueshcyb yshc,diancxxb dc\n"
					+ "           where riq  = to_date('"
					+ getNianfValue().getValue()
					+ "-"
					+ getYuefValue().getValue()
					+ "-01', 'yyyy-mm-dd') and fenx='本月'\n"
					+ "                  and yshc.diancxxb_id=dc.id  and yshc.zhuangt=2\n"
					+ "                  group by (yshc.diancxxb_id) )shcy ,\n"
					+ "     (select distinct nvl(diancxxb_id,0) as id from yuezbb\n"
					+ "           where riq  = to_date('"
					+ getNianfValue().getValue()
					+ "-"
					+ getYuefValue().getValue()
					+ "-01', 'yyyy-mm-dd') and fenx='本月'  and zhuangt=2)yzb,\n"
					+ "     (select distinct nvl(diancxxb_id,0) as id from yueshchjb\n"
					+ "           where riq  = to_date('"
					+ getNianfValue().getValue()
					+ "-"
					+ getYuefValue().getValue()
					+ "-01', 'yyyy-mm-dd') and fenx='本月'  and zhuangt=2)shcm ,\n"
					+ "     (select distinct nvl(diancxxb_id,0) as id,dc.mingc from koujdyb ,diancxxb dc where diancxxb_id = dc.id and koujmcb_id="
					+ baoblx
					+ " and leix=1) kj\n"
					+ "where kj.id=shcy.id(+) and kj.id=yzb.id(+) and kj.id=shcm.id(+)\n"
					+ ")"
					+ "order by ydiancid desc,zbdiancid desc,mdiancid desc";

		} else if (baobmc.equals("调燃02表")) {
			diancztsql = "select * from (\n"
					+ "select distinct nvl(sl.diancxxb_id,0) as sldiancid,nvl(c.diancxxb_id,0) as cgdiancid,nvl(zn.id,0) as znid,zn.mingc as znmc\n"
					+ "from(select distinct nvl(cg.diancxxb_id,0) as diancxxb_id,dc.mingc as mingc\n"
					+ "            from yuecgjhb cg,diancxxb dc\n"
					+ "            where cg.riq>=to_date('"
					+ getNianfValue().getValue()
					+ "-01-01','yyyy-mm-dd') and cg.riq<=to_date('"
					+ getNianfValue().getValue()
					+ "-"
					+ getYuefValue().getValue()
					+ "-01','yyyy-mm-dd')\n"
					+ "                 and cg.zhuangt=2)c,\n"
					+ "    (select distinct nvl(ykj.diancxxb_id,0) as diancxxb_id\n"
					+ "            from yueslb s,yuetjkjb ykj\n"
					+ "            where s.yuetjkjb_id=ykj.id and s.fenx='本月'\n"
					+ "              and ykj.riq>=to_date('"
					+ getNianfValue().getValue()
					+ "-01-01','yyyy-mm-dd') and ykj.riq<=to_date('"
					+ getNianfValue().getValue()
					+ "-"
					+ getYuefValue().getValue()
					+ "-01','yyyy-mm-dd')\n"
					+ "              and s.zhuangt=2)sl,\n"
					+ "    (select distinct diancxxb_id as id,dc.mingc as mingc from koujdyb,diancxxb dc where dc.id = diancxxb_id and koujmcb_id="
					+ baoblx
					+ " and leix=1)zn\n"
					+ "where sl.diancxxb_id(+)=zn.id and c.diancxxb_id(+)=zn.id\n"
					+ ")" + "order by sldiancid desc,cgdiancid desc";
		}
		ResultSet diancmc_zt = con.getResultSet(diancztsql);
		diancbzt_change = false;
		diancmc_y = "";
		diancmc_zb = "";
		diancmc_m = "";
		diancmc_s = "";
		diancmc_cg = "";
		String diancmc_yhc = "";
		String diancmc_zbb = "";
		String diancmc_mhc = "";
		String diancmc_sl = "";
		String diancmc_cgjh = "";
		try {
			// //判断中电投上报中能的电厂编码是否为0或null，为0记下电厂单位名称。
			while (diancmc_zt.next()) {
				if (baobmc.equals("调燃01表")) {
					if (diancmc_zt.getString("ydiancid").equals("0")
							|| diancmc_zt.getString("ydiancid") == null
							|| diancmc_zt.getString("ydiancid").equals("")) {
						diancbzt_change = true;
						diancmc_yhc = diancmc_zt.getString("znmc") + ",";
						diancmc_y = diancmc_yhc + diancmc_y;// 油耗存电厂名称
					}
					if (diancmc_zt.getString("zbdiancid").equals("0")
							|| diancmc_zt.getString("zbdiancid") == null
							|| diancmc_zt.getString("zbdiancid").equals("")) {
						diancbzt_change = true;
						diancmc_zbb = diancmc_zt.getString("znmc") + ",";
						diancmc_zb = diancmc_zbb + diancmc_zb;// 财务生产电厂名称
					}
					if (diancmc_zt.getString("mdiancid").equals("0")
							|| diancmc_zt.getString("mdiancid") == null
							|| diancmc_zt.getString("mdiancid").equals("")) {
						diancbzt_change = true;
						diancmc_mhc = diancmc_zt.getString("znmc") + ",";
						diancmc_m = diancmc_mhc + diancmc_m;// 煤耗存电厂名称
					}
				} else {
					if (diancmc_zt.getString("sldiancid").equals("0")
							|| diancmc_zt.getString("sldiancid") == null
							|| diancmc_zt.getString("sldiancid").equals("")) {
						diancbzt_change = true;
						diancmc_sl = diancmc_zt.getString("znmc") + ",";
						diancmc_s = diancmc_sl + diancmc_s;// 数量电厂名称
					}
					if (diancmc_zt.getString("cgdiancid").equals("0")
							|| diancmc_zt.getString("cgdiancid") == null
							|| diancmc_zt.getString("cgdiancid").equals("")) {
						diancbzt_change = true;
						diancmc_cgjh = diancmc_zt.getString("znmc") + ",";
						diancmc_cg = diancmc_cgjh + diancmc_cg;// 采购计划电厂名称
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			con.Close();
		}
		return diancbzt_change;
	}

	// *************************************************************************************************
	public void Shangb() { // 上报按钮

		// 工具栏的年份和月份下拉框
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

		String UploadFilename;
		String TxtName = "c://Yuebsc/";// 得到生成文件名
		if (!(new File("c://Yuebsc")).isDirectory()) {
			(new File("c://Yuebsc")).mkdir();
		}

		Visit visit = (Visit) getPage().getVisit();

		JDBCcon con = new JDBCcon();
		StringBuffer tableName = new StringBuffer();
		con.setAutoCommit(false);
		String diancbm = "000007";
		int flag = 0;

		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		Gonghdw_zn();
		while (rsl.next()) {
			Diancbm_zn(rsl.getString("baobmc"));
			if (diancbmc_change == true) {
				setMsg(diancmc + "电厂编码为0。请核对！");
			} else {
				if (rsl.getString("baobmc").equals("调燃01表")) {
					Diancmczt_zn(rsl.getString("baobmc"));
					if (diancbzt_change == true) {
						String tisk = diancmc_y + "以上电厂没有审核月油耗存数据；<br>"
								+ diancmc_zb + "以上电厂没有审核月煤耗存数据；<br>"
								+ diancmc_m + "以上电厂没有审核财务生产数据。请核对！";
						setMsg(tisk);
					} else {
						// M代表当月
						// A代表累计
						TxtName = getStrName(1, intMonth, diancbm, "m");// (电厂编码,上报表,月份,类型)
						msg.append(getdiaor01(diancbm, TxtName, "本月", intyear,
								intMonth)
								+ "\n");
						tableName.append(TxtName + ",");
						TxtName = getStrName(1, intMonth, diancbm, "a");// (电厂编码,上报表,月份,类型)
						msg.append(getdiaor01(diancbm, TxtName, "累计", intyear,
								intMonth)
								+ "\n");
						tableName.append(TxtName + ",");
					}
				}

				if (rsl.getString("baobmc").equals("调燃02表")) {
					Diancmczt_zn(rsl.getString("baobmc"));
					if (diancbzt_change == true) {
						setMsg(diancmc_s + "以上电厂没有审核数量填报数据；<br>" + diancmc_cg
								+ "以上电厂没有审核月采购计划数据。请核对！");
					} else {
						// M代表当月
						// A代表累计
						TxtName = getStrName(2, intMonth, diancbm, "m");// (电厂编码,上报表,月份,类型)
						msg.append(getdiaor02(diancbm, TxtName, "本月", intyear,
								intMonth)
								+ "\n");
						tableName.append(TxtName + ",");
						TxtName = getStrName(2, intMonth, diancbm, "a");// (电厂编码,上报表,月份,类型)
						msg.append(getdiaor02(diancbm, TxtName, "累计", intyear,
								intMonth)
								+ "\n");
						tableName.append(TxtName + ",");
					}

				}
				if (rsl.getString("baobmc").equals("cpi燃料管理01表		")) {
					if (gonghdw_change == true) {
						setMsg(gonghdwmc + "没有匹配中能供货单位。请核对！");
						continue;
					} else {
						String sql =

						"select y.riq as riq,sf.bianm as kuangsf,g.bianm as kuangbm,\n"
								+ "    sum(decode(y.jihkjb_id,1,y.daohl,0)) as daohl_zd,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.chebj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as chebj_zd,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.farl,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),3) as farl_zd,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.daocj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2) as daocj_zd,\n"
								+ "    sum(decode(y.jihkjb_id,3,y.daohl,0)) as daohl_qy,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.chebj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),2) as chebj_qy,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.farl,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as farl_qy,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.daocj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3) as daocj_qy,\n"
								+ "    sum(decode(y.jihkjb_id,2,y.daohl,0)) as daohl_sc,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.chebj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as chebj_sc,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.farl,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as farl_sc,\n"
								+ "    Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0, sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.daocj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2) as daocj_sc,\n"
								+ "    decode(dc.bianm,'212105','212118',dc.bianm) as dcbm,dsf.bianm as dcsf\n"
								+ "from yuedmjgmxb y, diancxxb dc,(select diancxxb_id as id from koujdyb where koujmcb_id=2 and leix=1)kj,jihkjb j,shengfb dsf,zhongnmkdqb sf,\n"
								+ "     (select gys.id as id,zngh.xuh,zngh.bianm,zngh.zhongnmkdqb_id as znmkdqid from gongysb gys,zhongnghdwglb zngl,zhongnghdwb zngh\n"
								+ "                where zngl.gongysb_id=gys.id and zngl.zhongnghdwb_id=zngh.id )g\n"
								+ " where y.diancxxb_id = dc.id and dc.id=kj.id \n"
								+ " and dc.shengfb_id = dsf.id\n"
								+ " and  y.gongysb_id=g.id\n"
								+ " and g.znmkdqid=sf.id\n"
								+ " and y.jihkjb_id=j.id\n"
								+ " and y.riq=to_date('"
								+ getNianfValue().getValue()
								+ "-"
								+ getYuefValue().getValue()
								+ "-01','yyyy-mm-dd')\n"
								+ " group by (y.riq,sf.bianm,g.bianm,dc.bianm,dsf.bianm)\n"
								+ " order by grouping(y.riq) desc,grouping(sf.bianm) desc ,max(sf.xuh),sf.bianm,grouping(g.bianm) desc ,max(g.xuh),g.bianm,\n"
								+ "          grouping(dc.bianm) desc ,max(dc.xuh),dc.bianm";

						// -------------------------------------------------------------------------------------------------------
						// "select y.riq as riq,sf.bianm as kuangsf,g.bianm as
						// kuangbm,\n" +
						// " sum(decode(y.jihkjb_id,1,y.daohl,0)) as
						// daohl_zd,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.chebj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2)
						// as chebj_zd,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.farl,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),3)
						// as farl_zd,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,1,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,1,y.daohl,0)*decode(y.jihkjb_id,1,y.daocj,0))/sum(decode(y.jihkjb_id,1,y.daohl,0))),2)
						// as daocj_zd,\n" +
						// " sum(decode(y.jihkjb_id,3,y.daohl,0)) as
						// daohl_qy,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.chebj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),2)
						// as chebj_qy,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.farl,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3)
						// as farl_qy,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,3,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,3,y.daohl,0)*decode(y.jihkjb_id,3,y.daocj,0))/sum(decode(y.jihkjb_id,3,y.daohl,0))),3)
						// as daocj_qy,\n" +
						// " sum(decode(y.jihkjb_id,2,y.daohl,0)) as
						// daohl_sc,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.chebj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2)
						// as chebj_sc,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.farl,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2)
						// as farl_sc,\n" +
						// "
						// Round(decode(sum(decode(y.jihkjb_id,2,y.daohl,0)),0,0,
						// sum(decode(y.jihkjb_id,2,y.daohl,0)*decode(y.jihkjb_id,2,y.daocj,0))/sum(decode(y.jihkjb_id,2,y.daohl,0))),2)
						// as daocj_sc,\n" +
						// " dc.bianm as dcbm,dsf.bianm as dcsf\n" +
						// " from yuedmjgmxb y, diancxxb dc, gongysb g, jihkjb
						// j,shengfb sf,shengfb dsf\n" +
						// " where y.diancxxb_id = dc.id\n" +
						// " and dc.shengfb_id = dsf.id\n" +
						// " and y.gongysb_id=g.id\n" +
						// " and g.shengfb_id=sf.id\n" +
						// " and y.jihkjb_id=j.id\n" +
						// " and
						// y.riq=to_date('"+getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01','yyyy-mm-dd')\n"
						// +
						// " group by
						// (y.riq,sf.bianm,g.bianm,dc.bianm,dsf.bianm)\n" +
						// " order by grouping(y.riq) desc,grouping(sf.bianm)
						// desc ,max(sf.xuh),sf.bianm,grouping(g.bianm) desc
						// ,max(g.xuh),g.bianm,\n" +
						// " grouping(dc.bianm) desc ,max(dc.xuh),dc.bianm";

						try {

							ResultSet rs = con.getResultSet(sql);
							String duank = "1433";
							String biaom = "test";
							getSettings();
							// SVJDBCcon dbcn=new
							// SVJDBCcon(ZhongNip,duank,biaom);
							// String ConnStr="jdbc:microsoft:sqlserver://"+
							// ZhongNip+":"+duank+";DatabaseName="+biaom;
							String ConnStr = "jdbc:odbc:" + ZhongN_ODBC + "";

							JDBCcon sqlserver = new JDBCcon(
									JDBCcon.ConnectionType_ODBC, "", ConnStr,
									ZhongNyh_sql, ZhongNmm_sql);
							// dbcn.setAutoCommit(false);
							// dbcn.setUserName(ZhongNyh_sql);
							// dbcn.setPassword(ZhongNmm_sql);

							int result = -1;
							// 删除当月上报数据

							String scsql = "delete from xdmjg where contentdate='"
									+ getNianfValue().getValue()
									+ "-"
									+ getYuefValue().getValue() + "-01'";
							result = sqlserver.getUpdate(scsql);
							if (result == -1) {
								System.out
										.println("********************数据修改失败！******************");
							}
							// 添加当月上报数据
							StringBuffer instsql = new StringBuffer();
							instsql.append("begin\n");
							while (rs.next()) {
								instsql
										.append("insert into xdmjg(contentdate,sheng,kuang,qgdhl,qgcbj,qgrz,qgdcj,qydhl,qycbj,qyrz,qydcj,scdhl,sccbj,scrz,scdcj,plant,sheng_p,xsm,biaoshi,fff,ffff,operator)\n");
								instsql
										.append("values('"
												+ rs.getDate("riq")
												+ "','"
												+ rs.getString("kuangsf")
												+ "','"
												+ getStr_cpi(6, rs
														.getString("kuangbm"))
												+ "',"
												// +rs.getString("kuangbm").substring(0,8)
												// +"',"

												+ rs.getDouble("daohl_zd")
												+ ","
												+ rs.getDouble("chebj_zd")
												+ ","
												+ rs.getDouble("farl_zd")
												+ ","
												+ rs.getDouble("daocj_zd")
												+ ","
												+ rs.getDouble("daohl_qy")
												+ ","
												+ rs.getDouble("chebj_qy")
												+ ","
												+ rs.getDouble("farl_qy")
												+ ","
												+ rs.getDouble("daocj_qy")
												+ ","
												+ rs.getDouble("daohl_sc")
												+ ","
												+ rs.getDouble("chebj_sc")
												+ ","
												+ rs.getDouble("farl_sc")
												+ ","
												+ rs.getDouble("daocj_sc")
												+ ",'"
												+ rs.getString("dcbm")
												+ "','"
												+ rs.getString("dcsf")
												+ "','1','1',null,null,null);\n");

							}
							instsql.append("end;");

							flag = sqlserver.getUpdate(instsql.toString());

							// System.out.println(instsql);
							if (flag == -1) {
								sqlserver.rollBack();
								sqlserver.Close();
								WriteLog
										.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
								setMsg(ErrorMessage.UpdateDatabaseFail);
								return;
							}

							if (flag != -1) {
								setMsg(ErrorMessage.SaveSuccessMessage);
							}
							sqlserver.commit();
							sqlserver.Close();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							con.Close();
						}
					}
				}
			}

			Tablename = tableName.toString();
			msg.append("***************TxT文件生成结束***************" + "\n");
		}
	}

	public void getCpi01() {
		JDBCcon con = new JDBCcon();
	}

	public String getdiaor01(String diancbm, String TxtName, String leix,
			long nianf, long yuef) {
		if (diancbm == "") {
			return "diaor01表" + leix + "没有所上传数据!";
		} else {

			String diaor01bb = "";
			StringBuffer diao01 = new StringBuffer();
			StringBuffer sbsql = new StringBuffer();
			JDBCcon con = new JDBCcon();
			FtpCreatTxt ct = new FtpCreatTxt();
			ct.CreatTxt("c://Yuebsc/" + TxtName + ".txt");// 生成文件
			try {
				// 调然01

				String dr01_diancbm = "";
				double fadsbrl = 0;
				double meitsg = 0;
				double meithyhj = 0;
				double meithyfd = 0;
				double meithygr = 0;
				double meithyqt = 0;
				double meithysh = 0;
				double meitkc = 0;

				double shiysg = 0;
				double shiyhyhj = 0;
				double shiyhyfd = 0;
				double shiyhygr = 0;
				double shiyhyqt = 0;
				double shiyhysh = 0;
				double shiykc = 0;

				double fadl = 0;
				double gongrl = 0;
				double biaozmhfd = 0;
				double biaozmhgr = 0;
				double tianrmhfd = 0;
				double tianrmhgr = 0;
				double biaozmlfd = 0;
				double biaozmlgr = 0;
				double zonghrl = 0;
				double zonghm = 0;

				sbsql.append("select  dc.bianm as diancbm \n");
				sbsql
						.append("       ,decode(sum(nvl(jz.jizurl,0)),0,0,sum(nvl(jz.jizurl,0))) as jizrl \n");
				sbsql.append("       ,sum(nvl(bq.shouml,0)) as shouml_b \n");
				sbsql
						.append("       ,sum(nvl(bq.fady,0)+nvl(bq.gongry_m,0)+nvl(bq.qith_m,0)+nvl(bq.sunh_m,0)) as hej_m \n");
				sbsql.append("       ,sum(nvl(bq.fady,0)) as fady_b \n");
				sbsql.append("       ,sum(nvl(bq.gongry_m,0)) as gongrym_b \n");
				sbsql.append("       ,sum(nvl(bq.qith_m,0)) as qithm_b \n");
				sbsql.append("       ,sum(nvl(bq.sunh_m,0)) as sunhm_b \n");
				sbsql.append("       ,sum(nvl(bq.kuc_m,0)) as kucm_b \n");
				sbsql.append("       ,sum(nvl(bq.shouyl,0)) as shouyl_b \n");
				sbsql
						.append("       ,sum(nvl(bq.fadyy,0)+nvl(bq.gongry_y,0)+nvl(bq.qithy,0)+nvl(bq.sunh_y,0)) as hej_y \n");
				sbsql.append("       ,sum(nvl(bq.fadyy,0)) as fadyy_b \n");
				sbsql.append("       ,sum(nvl(bq.gongry_y,0)) as gongryy_b \n");
				sbsql.append("       ,sum(nvl(bq.qithy,0)) as qithy_b \n");
				sbsql.append("       ,sum(nvl(bq.sunh_y,0)) as sunhy_b \n");
				sbsql.append("       ,sum(nvl(bq.kuc_y,0)) as kucy_b \n");
				sbsql.append("       ,round(sum(nvl(bq.fadl,0)),2) as fadl \n");
				sbsql.append("    ,sum(nvl(bq.gongrl,0)/10) as gongrl \n");
				sbsql
						.append(",Round(decode(sum(nvl(bq.fadl,0)),0,0,sum(nvl(bq.fadhbzml,0)+nvl(bq.fadyzbzml,0)+nvl(bq.fadqzbzml,0)) / sum(nvl(bq.fadl,0)) * 100), 0) as fadbzmh--（发电煤折标准煤量＋发电油折标准煤量＋发电气折标准煤量）/发电量 \n");
				sbsql
						.append(",Round(decode(sum(nvl(bq.gongrl,0)),0,0,sum(nvl(bq.gongrhbzml,0)+nvl(bq.gongryhbzml,0)+nvl(bq.gongrqhbzml,0)) / sum(nvl(bq.gongrl,0)) * 1000), 2) as gongrbzmh  --（供热煤折标准煤量＋供热油折标准煤量＋供热气折标准煤量）/供热量 \n");
				sbsql
						.append(",Round(decode(sum(nvl(bq.fadl,0)),0,0,(sum(nvl(bq.fadhml,0)) + 2 * sum(nvl(bq.fadhy,0))+sum(nvl(bq.fadhq,0))) / sum(nvl(bq.fadl,0)) * 100), 0) as fadtrmh \n");
				sbsql
						.append(",Round(decode(sum(nvl(bq.gongrl,0)),0,0,(sum(nvl(bq.gongrhml,0)) + 2 * sum(nvl(bq.gongrhy,0))+sum(nvl(bq.gongrhq,0))) / sum(nvl(bq.gongrl,0)) * 1000),1) as gongrtrmh \n");
				sbsql
						.append(",sum(nvl(bq.fadhbzml,0)+nvl(bq.fadyzbzml,0)+nvl(bq.fadqzbzml,0)) as fadhbzml_b \n");
				sbsql
						.append(",sum(nvl(bq.gongrhbzml,0)+nvl(bq.gongryhbzml,0)+nvl(bq.gongrqhbzml,2)) as gongrhbzml_b \n");
				sbsql
						.append(",Round(decode((sum(nvl(bq.fadhy,0)) + sum(nvl(bq.gongrhy,0))),0,0, \n");
				sbsql
						.append("              ((sum(nvl(bq.fadhbzml,0)+nvl(bq.fadyzbzml,0)+nvl(bq.fadqzbzml,0)) + sum(nvl(bq.gongrhbzml,0)+nvl(bq.gongryhbzml,0)+nvl(bq.gongrqhbzml,0))) * 7000*0.0041816 \n");
				sbsql
						.append("     / ((sum(nvl(bq.fadhy,0)) + sum(nvl(bq.gongrhy,0)))*2+sum(nvl(bq.fadhml,0))+sum(nvl(bq.gongrhml,0))))),2) as zonghrl \n");
				sbsql
						.append(",Round(decode((sum(nvl(bq.fadhml,0)) + sum(nvl(bq.gongrhml,0))),0,0, \n");
				sbsql
						.append("             ((sum(nvl(bq.fadhbzml,0)) + sum(nvl(bq.gongrhbzml,0))) * 7000 - (sum(nvl(bq.fadhy,0)) + sum(nvl(bq.gongrhy,0))) * 10000) * 0.0041816 \n");
				sbsql
						.append("                      / (sum(nvl(bq.fadhml,0)) + sum(nvl(bq.gongrhml,0)))),2) as zonghm  \n");
				sbsql.append(" from(select yz.diancxxb_id \n");
				sbsql.append("            ,shcm.shouml as shouml \n");
				sbsql.append("            ,shcm.fady as fady \n");
				sbsql.append("            ,shcm.gongry as gongry_m \n");
				sbsql.append("            ,shcm.qith as qith_m \n");
				sbsql.append("            ,shcm.sunh as sunh_m \n");
				sbsql.append("            ,shcm.kuc as kuc_m \n");
				sbsql.append("            ,shcy.shouyl as shouyl \n");
				sbsql.append("            ,shcy.fadyy as fadyy \n");
				sbsql.append("            ,shcy.gongry as gongry_y \n");
				sbsql.append("            ,shcy.qithy as qithy \n");
				sbsql.append("            ,shcy.sunh as sunh_y \n");
				sbsql.append("            ,shcy.kuc as kuc_y \n");
				sbsql.append("            ,yz.fadl as fadl \n");
				sbsql.append("            ,yz.gongrl as gongrl \n");
				sbsql.append(" ,yz.FADMZBML as fadhbzml--发电煤折标煤量 \n");
				sbsql.append(" ,yz.FADYZBZML as fadyzbzml--发电油折标准煤量 \n");
				sbsql.append(" ,yz.FADQZBZML as fadqzbzml--发电气折标准煤量 \n");
				sbsql.append(" ,yz.GONGRMZBML as gongrhbzml--供热煤折标煤量 \n");
				sbsql.append(" ,yz.GONGRYZBZML as gongryhbzml--供热油折标准煤量 \n");
				sbsql.append(" ,yz.GONGRQZBZML as gongrqhbzml--供热气折标准煤量 \n");
				sbsql.append(" ,yz.FADYTRML as fadhml \n");
				sbsql.append(" ,yz.FADYTRYL as fadhy \n");
				sbsql.append(" ,yz.FADYTRQL as fadhq \n");
				sbsql.append(" ,yz.GONGRYTRML as gongrhml \n");
				sbsql.append(" ,yz.GONGRYTRYL as gongrhy \n");
				sbsql.append(" ,yz.GONGRYTRQL as gongrhq  \n");
				sbsql.append("       from yuezbb yz,yueshchjb shcm, \n");
				// sbsql.append("(select * from yuezbb where fenx='"+leix+"' and
				// riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd'))yz,");
				// sbsql.append("(select * from yueshchjb where fenx='本月'/* and
				// riq >= to_date('2009-01-01','yyyy-mm-dd') and */and riq =
				// to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd'))shcm, ");
				sbsql
						.append("(select yshc.diancxxb_id,sum(shouyl) as shouyl,sum(fadyy) as fadyy,sum(gongry) as gongry,sum(qithy) as qithy,sum(sunh) as sunh,sum(kuc) as kuc \n");
				sbsql.append("from yueshcyb yshc,diancxxb dc \n");
				sbsql.append("            where riq  = to_date('" + nianf + "-"
						+ yuef + "-01', 'yyyy-mm-dd') and fenx='" + leix
						+ "'  \n");
				sbsql
						.append("                   and yshc.diancxxb_id=dc.id  \n");
				sbsql
						.append("                   group by (yshc.diancxxb_id)  \n");
				sbsql.append("                   order by max(dc.xuh)  \n");
				sbsql.append("           )shcy \n");
				sbsql
						.append("       where yz.diancxxb_id=shcm.diancxxb_id and yz.diancxxb_id=shcy.diancxxb_id \n");
				sbsql.append("             and yz.riq = to_date('" + nianf
						+ "-" + yuef
						+ "-01','yyyy-mm-dd') and shcm.riq = to_date('" + nianf
						+ "-" + yuef + "-01','yyyy-mm-dd') \n");
				sbsql.append("             and yz.fenx = '" + leix
						+ "' and shcm.fenx='" + leix + "' \n");
				sbsql
						.append("      )bq ,(select j.diancxxb_id as id ,sum(nvl(j.jizurl,0)) as jizurl from jizb j,diancxxb dc \n");
				sbsql
						.append("                      where j.diancxxb_id=dc.id   and j.toucrq<=to_date('"
								+ nianf + "-" + yuef + "-01','yyyy-mm-dd')\n");
				sbsql.append("					  group by(j.diancxxb_id))jz       \n");
				sbsql
						.append("      ,diancxxb dc,(select diancxxb_id as id from koujdyb where koujmcb_id=1 and leix=1) kj \n");
				sbsql
						.append("where dc.id=kj.id  and bq.diancxxb_id=jz.id(+) and dc.id=bq.diancxxb_id(+) \n");
				sbsql.append("group by  (dc.bianm) \n");
				sbsql
						.append("order by grouping(dc.bianm) desc,max(dc.xuh)   \n");

				// ------------------------------------------
				if (con.getHasIt(sbsql.toString()) == false) {
					new File("c://Yuebsc/" + TxtName + ".txt").delete();// 删除文件
					diaor01bb = "生成diaor01bb数据失败,没有找到diaor01bb" + nianf + "年"
							+ yuef + "月的'" + leix + "'所要数据" + "\n";
				} else {
					ResultSet rsdata = con.getResultSet(sbsql.toString());
					for (int i = 0; rsdata.next(); i++) {
						diaor01bb = "";
						diao01.setLength(0);
						dr01_diancbm = rsdata.getString("diancbm");
						fadsbrl = rsdata.getDouble("jizrl");
						meitsg = rsdata.getDouble("shouml_b");
						meithyhj = rsdata.getDouble("hej_m");
						meithyfd = rsdata.getDouble("fady_b");
						meithygr = rsdata.getDouble("gongrym_b");
						meithyqt = rsdata.getDouble("qithm_b");
						meithysh = rsdata.getDouble("sunhm_b");
						meitkc = rsdata.getDouble("kucm_b");

						shiysg = rsdata.getDouble("shouyl_b");
						shiyhyhj = rsdata.getDouble("hej_y");
						shiyhyfd = rsdata.getDouble("fadyy_b");
						shiyhygr = rsdata.getDouble("gongryy_b");
						shiyhyqt = rsdata.getDouble("qithy_b");
						shiyhysh = rsdata.getDouble("sunhy_b");
						shiykc = rsdata.getDouble("kucy_b");

						fadl = rsdata.getDouble("fadl");
						gongrl = rsdata.getDouble("gongrl");
						biaozmhfd = rsdata.getDouble("fadbzmh");
						biaozmhgr = rsdata.getDouble("gongrbzmh");
						tianrmhfd = rsdata.getDouble("fadtrmh");
						tianrmhgr = rsdata.getDouble("gongrtrmh");
						biaozmlfd = rsdata.getDouble("fadhbzml_b");
						biaozmlgr = rsdata.getDouble("gongrhbzml_b");
						zonghrl = rsdata.getDouble("zonghrl");
						zonghm = rsdata.getDouble("zonghm");

						diao01.append(getStr(6, dr01_diancbm));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(fadsbrl))));
						diao01.append(getNum(10, 0, String.valueOf(Math
								.round(meitsg))));
						diao01.append(getNum(10, 0, String.valueOf(Math
								.round(meithyhj))));
						diao01.append(getNum(10, 0, String.valueOf(Math
								.round(meithyfd))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(meithygr))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(meithyqt))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(meithysh))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(meitkc))));

						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(shiysg))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(shiyhyhj))));
						diao01.append(getNum(6, 0, String.valueOf(Math
								.round(shiyhyfd))));
						diao01.append(getNum(6, 0, String.valueOf(Math
								.round(shiyhygr))));
						diao01.append(getNum(6, 0, String.valueOf(Math
								.round(shiyhyqt))));
						diao01.append(getNum(6, 0, String.valueOf(Math
								.round(shiyhysh))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(shiykc))));

						diao01.append(getNum(12, 2, String.valueOf(fadl)));
						diao01.append(getNum(10, 0, String.valueOf(Math
								.round(gongrl))));
						diao01.append(getNum(4, 0, String.valueOf(Math
								.round(biaozmhfd))));
						diao01.append(getNum(6, 2, String.valueOf(biaozmhgr)));
						diao01.append(getNum(4, 0, String.valueOf(Math
								.round(tianrmhfd))));
						diao01.append(getNum(6, 2, String.valueOf(tianrmhgr)));
						diao01.append(getNum(10, 0, String.valueOf(Math
								.round(biaozmlfd))));
						diao01.append(getNum(8, 0, String.valueOf(Math
								.round(biaozmlgr))));
						diao01.append(getNum(6, 2, String.valueOf(zonghrl)));
						diao01.append(getNum(6, 2, String.valueOf(zonghm)));

						// 调用生成TXT类
						diaor01bb = diao01.toString();// 得到一行数

						ct.aLine(diaor01bb);// 写入行数据
					}
					ct.finish();// 关闭输入流，将文字从缓存写入文件
					rsdata.close();
					diaor01bb = "生成diaor01bb" + nianf + "年" + yuef + "月的'"
							+ leix + "'数据成功!" + "\n";
				}
			} catch (Exception e) {
				diaor01bb = "生成diaor01bb数据有异常!" + "\n";
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return diaor01bb;
		}
	}

	public String getdiaor02(String diancbm, String TxtName, String leix,
			long nianf, long yuef) {
		if (diancbm == "") {
			return "diaor02表" + leix + "没有所上传数据!";
		} else {
			String diaor02bb = "";
			JDBCcon con = new JDBCcon();
			FtpCreatTxt ct = new FtpCreatTxt();
			ct.CreatTxt("c://Yuebsc/" + TxtName + ".txt");// 生成文件
			try {
				// 调然02
				String dr02_diancbm = "";
				String koujbm = "";
				String gongysbm = "";
				double jihlml = 0;
				double shislml = 0;

				if (leix.equals("本月")) {
					StringBuffer diao02 = new StringBuffer();
					String sql =
					// "select dc.bianm as
					// dcbm,decode(kj.id,1,'10',3,'20',2,'30') as kjbm,gy.bianm
					// as gysbm\n" +
					// " ,sum(byjh.laimsl) as jihlml_by ,sum(byss.laimsl) as
					// shislml_by\n" +
					// "from diancxxb dc,gongysb gy ,jihkjb kj,\n" +
					// " (select cg.diancxxb_id as diancxxb_id,cg.jihkjb_id as
					// jihkjb_id,gy.fuid as gongysb_id,sum(cg.yuejhcgl) as
					// laimsl\n" +
					// " from yuecgjhb cg,diancxxb dc,gongysb gy\n" +
					// " where cg.diancxxb_id=dc.id and
					// cg.riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and
					// cg.gongysb_id=gy.id\n" +
					// " group by (cg.diancxxb_id,cg.jihkjb_id,gy.fuid)\n" +
					// " )byjh,\n" +
					// " (select cg.diancxxb_id as diancxxb_id,cg.jihkjb_id as
					// jihkjb_id,gy.fuid as gongysb_id,sum(cg.yuejhcgl) as
					// laimsl\n" +
					// " from yuecgjhb cg,diancxxb dc,gongysb gy\n" +
					// " where cg.diancxxb_id=dc.id and cg.gongysb_id=gy.id\n" +
					// " and cg.riq>=to_date('"+nianf+"-01-01','yyyy-mm-dd') and
					// cg.riq<=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n"
					// +
					// " group by (cg.diancxxb_id,cg.jihkjb_id,gy.fuid)\n" +
					// " )ljjh,\n" +
					// " (select tj.diancxxb_id as diancxxb_id,tj.jihkjb_id as
					// jihkjb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as
					// laimsl\n" +
					// " from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy\n" +
					// " where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and
					// tj.gongysb_id=gy.id\n" +
					// " and sl.fenx='本月' and
					// tj.riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n" +
					// " group by (tj.diancxxb_id,tj.jihkjb_id,tj.gongysb_id)\n"
					// +
					// " )byss,\n" +
					// " (select tj.diancxxb_id as diancxxb_id,tj.jihkjb_id as
					// jihkjb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as
					// laimsl\n" +
					// " from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy\n" +
					// " where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and
					// tj.gongysb_id=gy.id\n" +
					// " and sl.fenx='本月' and
					// tj.riq>=to_date('"+nianf+"-01-01','yyyy-mm-dd') and
					// tj.riq<=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n"
					// +
					// " group by (tj.diancxxb_id,tj.jihkjb_id,tj.gongysb_id)\n"
					// +
					// " )ljss\n" +
					// " ,(select diancxxb_id as id from koujdyb where
					// koujmcb_id=1 and leix=1) zn"+
					// " where dc.id= zn.id\n" +
					// " and zn.id=byss.diancxxb_id\n" +
					// " and gy.id=byjh.gongysb_id\n" +
					// " and gy.id=byss.gongysb_id\n" +
					// " and zn.id=ljjh.diancxxb_id\n" +
					// " and zn.id=ljss.diancxxb_id\n" +
					// " and gy.id=ljss.gongysb_id\n" +
					// " and gy.id=ljjh.gongysb_id\n" +
					// " and kj.id=byjh.jihkjb_id\n" +
					// " and kj.id=byss.jihkjb_id\n" +
					// " and kj.id=ljss.jihkjb_id\n" +
					// " and kj.id=ljjh.jihkjb_id\n" +
					// "group by (dc.bianm,kj.id,gy.bianm)\n" +
					// "order by grouping(dc.bianm)desc,min(dc.xuh)";
					// -------------------------------------------------------------------------------------
					"select dc.bianm as dcbm,decode(gy.kjid,1,'10',3,'20',2,'30') as kjbm,gy.bianm as gysbm\n"
							+ "       ,sum(byjh.laimsl) as jihlml_by  ,sum(byss.laimsl) as shislml_by\n"
							+ "from diancxxb dc,\n"
							+ "      (select cg.gongysb_id as id,zngh.bianm as bianm,cg.jihkjb_id as kjid,cg.diancxxb_id as diancxxb_id\n"
							+ "              from yuecgjhb cg,gongysb g,jihkjb jh,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "              where cg.gongysb_id=g.id and cg.jihkjb_id=jh.id and g.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "			   and cg.riq=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "              and cg.diancxxb_id in (select diancxxb_id as id from koujdyb where koujmcb_id=1 and leix=1)\n"
							+ "        union\n"
							+ "        select ykj.gongysb_id as id,zngh.bianm as bianm,ykj.jihkjb_id as kjid,ykj.diancxxb_id as diancxxb_id\n"
							+ "              from yueslb s,yuetjkjb ykj,gongysb g,jihkjb jh,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "              where s.yuetjkjb_id=ykj.id and ykj.gongysb_id=g.id and s.fenx='本月'and ykj.jihkjb_id=jh.id and g.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "              and ykj.riq=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "              and ykj.diancxxb_id in (select diancxxb_id as id from koujdyb where koujmcb_id=1 and leix=1)\n"
							+ "        )gy,\n"
							+ "       (select cg.diancxxb_id as diancxxb_id,cg.jihkjb_id as jihkjb_id,gy.fuid as gongysb_id,sum(cg.yuejhcgl) as laimsl\n"
							+ "        from yuecgjhb cg,diancxxb dc,gongysb gy,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "        where cg.diancxxb_id=dc.id and cg.riq=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd') and cg.gongysb_id=gy.id and gy.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "        group by (cg.diancxxb_id,cg.jihkjb_id,gy.fuid)\n"
							+ "       )byjh,\n"
							+ "       (select tj.diancxxb_id as diancxxb_id,tj.jihkjb_id as jihkjb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as laimsl\n"
							+ "        from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "        where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and gy.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "             and sl.fenx='本月' and tj.riq=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "       group by (tj.diancxxb_id,tj.jihkjb_id,tj.gongysb_id)\n"
							+ "       )byss\n"
							+ "      where dc.id = gy.diancxxb_id\n"
							+ "         and gy.diancxxb_id=byjh.diancxxb_id(+)\n"
							+ "         and gy.diancxxb_id=byss.diancxxb_id(+)\n"
							+ "         and gy.id=byjh.gongysb_id(+)\n"
							+ "         and gy.id=byss.gongysb_id(+)\n"
							+ "         and gy.kjid=byjh.jihkjb_id(+)\n"
							+ "         and gy.kjid=byss.jihkjb_id(+)\n"
							+ "group by (dc.bianm,gy.kjid,gy.bianm)\n"
							+ "order by grouping(dc.bianm)desc,min(dc.xuh)";
					// ------------------------------------------
					if (con.getHasIt(sql.toString()) == false) {
						new File("c://Yuebsc/" + TxtName + ".txt").delete();// 删除文件
						diaor02bb = "生成diaor02bb数据失败,没有找到diaor02bb" + nianf
								+ "年" + yuef + "月的'" + leix + "'所要数据" + "\n";
					} else {
						ResultSet rsdata = con.getResultSet(sql);
						while (rsdata.next()) {
							diaor02bb = "";
							diao02.setLength(0);
							dr02_diancbm = rsdata.getString("dcbm");
							koujbm = rsdata.getString("kjbm");
							gongysbm = rsdata.getString("gysbm");
							jihlml = rsdata.getDouble("jihlml_by");
							shislml = rsdata.getDouble("shislml_by");

							diao02.append(getStr(6, dr02_diancbm));
							diao02.append(getStr(2, koujbm));
							diao02.append(getStr(6, gongysbm.substring(0, 6)));
							diao02.append(getNum(10, 0, String.valueOf(Math
									.round(jihlml))));
							diao02.append(getNum(10, 0, String.valueOf(Math
									.round(shislml))));

							// 调用生成TXT类
							diaor02bb = diao02.toString();// 得到一行数

							ct.aLine(diaor02bb);// 写入行数据

						}
						diaor02bb = "生成diaor02bb" + nianf + "年" + yuef + "月的'"
								+ leix + "'数据成功!" + "\n";
						ct.finish();// 关闭输入流，将文字从缓存写入文件
						rsdata.close();
					}

				} else {
					StringBuffer diao02 = new StringBuffer();
					String sql =
					// "select dc.bianm as
					// dcbm,decode(kj.id,1,'10',3,'20',2,'30') as kjbm,gy.bianm
					// as gysbm\n" +
					// " ,sum(ljjh.laimsl) as jihlml_lj ,sum(ljss.laimsl) as
					// shislml_lj\n" +
					// "from diancxxb dc,gongysb gy ,jihkjb kj,\n" +
					// " (select cg.diancxxb_id as diancxxb_id,cg.jihkjb_id as
					// jihkjb_id,gy.fuid as gongysb_id,sum(cg.yuejhcgl) as
					// laimsl\n" +
					// " from yuecgjhb cg,diancxxb dc,gongysb gy\n" +
					// " where cg.diancxxb_id=dc.id and
					// cg.riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and
					// cg.gongysb_id=gy.id\n" +
					// " group by (cg.diancxxb_id,cg.jihkjb_id,gy.fuid)\n" +
					// " )byjh,\n" +
					// " (select cg.diancxxb_id as diancxxb_id,cg.jihkjb_id as
					// jihkjb_id,gy.fuid as gongysb_id,sum(cg.yuejhcgl) as
					// laimsl\n" +
					// " from yuecgjhb cg,diancxxb dc,gongysb gy\n" +
					// " where cg.diancxxb_id=dc.id and cg.gongysb_id=gy.id\n" +
					// " and cg.riq>=to_date('"+nianf+"-01-01','yyyy-mm-dd') and
					// cg.riq<=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n"
					// +
					// " group by (cg.diancxxb_id,cg.jihkjb_id,gy.fuid)\n" +
					// " )ljjh,\n" +
					// " (select tj.diancxxb_id as diancxxb_id,tj.jihkjb_id as
					// jihkjb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as
					// laimsl\n" +
					// " from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy\n" +
					// " where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and
					// tj.gongysb_id=gy.id\n" +
					// " and sl.fenx='本月' and
					// tj.riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n" +
					// " group by (tj.diancxxb_id,tj.jihkjb_id,tj.gongysb_id)\n"
					// +
					// " )byss,\n" +
					// " (select tj.diancxxb_id as diancxxb_id,tj.jihkjb_id as
					// jihkjb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as
					// laimsl\n" +
					// " from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy\n" +
					// " where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and
					// tj.gongysb_id=gy.id\n" +
					// " and sl.fenx='本月' and
					// tj.riq>=to_date('"+nianf+"-01-01','yyyy-mm-dd') and
					// tj.riq<=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n"
					// +
					// " group by (tj.diancxxb_id,tj.jihkjb_id,tj.gongysb_id)\n"
					// +
					// " )ljss\n" +
					// " ,(select diancxxb_id as id from koujdyb where
					// koujmcb_id=1 and leix=1) zn"+
					// " where dc.id=zn.id\n" +
					// " and zn.id=byss.diancxxb_id\n" +
					// " and gy.id=byjh.gongysb_id\n" +
					// " and gy.id=byss.gongysb_id\n" +
					// " and zn.id=ljjh.diancxxb_id\n" +
					// " and zn.id=ljss.diancxxb_id\n" +
					// " and gy.id=ljss.gongysb_id\n" +
					// " and gy.id=ljjh.gongysb_id\n" +
					// " and kj.id=byjh.jihkjb_id\n" +
					// " and kj.id=byss.jihkjb_id\n" +
					// " and kj.id=ljss.jihkjb_id\n" +
					// " and kj.id=ljjh.jihkjb_id\n" +
					// "group by (dc.bianm,kj.id,gy.bianm)\n" +
					// "order by grouping(dc.bianm)desc,min(dc.xuh)";
					// ----------------------------------------------------------------------

					"select dc.bianm as dcbm,decode(gy.kjid,1,'10',3,'20',2,'30') as kjbm,gy.bianm as gysbm\n"
							+ "       ,sum(ljjh.laimsl) as jihlml_lj  ,sum(ljss.laimsl) as shislml_lj\n"
							+ "from diancxxb dc,\n"
							+ "      (select cg.gongysb_id as id,zngh.bianm as bianm,cg.jihkjb_id as kjid,cg.diancxxb_id as diancxxb_id\n"
							+ "              from yuecgjhb cg,gongysb g,jihkjb jh,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "              where cg.gongysb_id=g.id and cg.jihkjb_id=jh.id and g.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "              and cg.riq>=to_date('"
							+ nianf
							+ "-01-01','yyyy-mm-dd') and cg.riq<=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "              and cg.diancxxb_id in (select diancxxb_id as id from koujdyb where koujmcb_id=1 and leix=1)\n"
							+ "        union\n"
							+ "        select ykj.gongysb_id as id,zngh.bianm as bianm,ykj.jihkjb_id as kjid,ykj.diancxxb_id as diancxxb_id\n"
							+ "              from yueslb s,yuetjkjb ykj,gongysb g,jihkjb jh,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "              where s.yuetjkjb_id=ykj.id and ykj.gongysb_id=g.id and s.fenx='本月'and ykj.jihkjb_id=jh.id and g.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "              and ykj.riq>=to_date('"
							+ nianf
							+ "-01-01','yyyy-mm-dd') and ykj.riq<=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "              and ykj.diancxxb_id in (select diancxxb_id as id from koujdyb where koujmcb_id=1 and leix=1)\n"
							+ "        )gy,\n"
							+ "        (select cg.diancxxb_id as diancxxb_id,cg.jihkjb_id as jihkjb_id,gy.fuid as gongysb_id,sum(cg.yuejhcgl) as laimsl\n"
							+ "         from yuecgjhb cg,diancxxb dc,gongysb gy,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "         where cg.diancxxb_id=dc.id and cg.gongysb_id=gy.id and gy.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "              and cg.riq>=to_date('"
							+ nianf
							+ "-01-01','yyyy-mm-dd') and cg.riq<=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "        group by (cg.diancxxb_id,cg.jihkjb_id,gy.fuid)\n"
							+ "       )ljjh,\n"
							+ "       (select tj.diancxxb_id as diancxxb_id,tj.jihkjb_id as jihkjb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as laimsl\n"
							+ "        from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy,zhongnghdwglb glb,zhongnghdwb zngh\n"
							+ "        where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and gy.id=glb.gongysb_id and glb.zhongnghdwb_id=zngh.id\n"
							+ "             and sl.fenx='本月' and tj.riq>=to_date('"
							+ nianf
							+ "-01-01','yyyy-mm-dd') and tj.riq<=to_date('"
							+ nianf
							+ "-"
							+ yuef
							+ "-01','yyyy-mm-dd')\n"
							+ "       group by (tj.diancxxb_id,tj.jihkjb_id,tj.gongysb_id)\n"
							+ "       )ljss\n"
							+ "      where dc.id=gy.diancxxb_id\n"
							+ "         and gy.diancxxb_id=ljjh.diancxxb_id(+)\n"
							+ "         and gy.diancxxb_id=ljss.diancxxb_id(+)\n"
							+ "         and gy.id=ljss.gongysb_id(+)\n"
							+ "         and gy.id=ljjh.gongysb_id(+)\n"
							+ "         and gy.kjid=ljss.jihkjb_id(+)\n"
							+ "         and gy.kjid=ljjh.jihkjb_id(+)\n"
							+ "group by (dc.bianm,gy.kjid,gy.bianm)\n"
							+ "order by grouping(dc.bianm)desc,min(dc.xuh)";

					// ------------------------------------------
					if (con.getHasIt(sql.toString()) == false) {
						new File("c://Yuebsc/" + TxtName + ".txt").delete();// 删除文件
						diaor02bb = "生成diaor02bb数据失败,没有找到diaor02bb" + nianf
								+ "年" + yuef + "月的'" + leix + "'所要数据" + "\n";
					} else {
						ResultSet rsdata = con.getResultSet(sql);
						while (rsdata.next()) {
							diaor02bb = "";
							diao02.setLength(0);
							dr02_diancbm = rsdata.getString("dcbm");
							koujbm = rsdata.getString("kjbm");
							gongysbm = rsdata.getString("gysbm");
							jihlml = rsdata.getDouble("jihlml_lj");
							shislml = rsdata.getDouble("shislml_lj");

							diao02.append(getStr(6, dr02_diancbm));
							diao02.append(getStr(2, koujbm));
							diao02.append(getStr(6, gongysbm.substring(0, 6)));
							diao02.append(getNum(10, 0, String.valueOf(Math
									.round(jihlml))));
							diao02.append(getNum(10, 0, String.valueOf(Math
									.round(shislml))));

							// 调用生成TXT类
							diaor02bb = diao02.toString();// 得到一行数

							ct.aLine(diaor02bb);// 写入行数据
						}
						diaor02bb = "生成diaor02bb" + nianf + "年" + yuef + "月的'"
								+ leix + "'数据成功!" + "\n";
						ct.finish();// 关闭输入流，将文字从缓存写入文件
						rsdata.close();
					}
				}
			} catch (Exception e) {
				diaor02bb = "生成diaor02bb数据有异常!" + "\n";
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return diaor02bb;
		}
	}

	private void getUploadFtp(String tableName) {// 上传Ftp
		getSettings();
		if (tableName == "") {
			setMsg("没有可上传文件");
		} else {
			try {
				String upload = "";
				String[] shangbbm = tableName.split(",");
				FtpUpload fu = new FtpUpload();
				// ------------连接fpt服务器上传文件-----------//
				// if(getFtpServer().equals("")){
				// upload="上传失败,请设置Ftp地址!"+"\n";
				// setMsg("请设置Ftp地址及目录!");
				// }else{
				// FTP设置
				String ip = "";
				String filepath = "";
				String username = "";
				String password = "";
				ip = ZhongNip;
				username = ZhongNyh;
				password = ZhongNmm;
				// if(filepath.equals("/")){
				// filepath = "";
				// }else{
				// filepath = "/"+filepath+"/";
				// }
				// if (getNim()){
				// username="administrator";
				// password="980904";
				// }else{
				if (username.equals("") || password.equals("")) {
					setMsg("请录入Ftp用户名及密码!");
				} else {
					username = ZhongNyh;
					password = ZhongNmm;
					fu.connectServer(ip, username, password, filepath);
					for (int i = 0; i < shangbbm.length; i++) {
						String filename = "C://Yuebsc//" + shangbbm[i] + ".txt";
						upload = shangbbm[i]
								+ fu.upload(filename, shangbbm[i] + ".txt"
										+ "\n");// 上传文件
						msg.append(upload + "\n");
					}
					fu.closeConnect();
				}
				// }
				// }
			} catch (Exception e) {
				msg.append("Ftp连接失败请确认Ftp设置!" + "\n");
				e.printStackTrace();
			} finally {
				// con.close();
			}
		}
		msg.append("***************TxT文件上传结束***************" + "\n");
		// setFanhz(msg.toString());
		setMsg("上传完成!");
	}

	// *********************************生成上传数据字段串
	// ***************************************************//
	private String getStrName(long TN, long yuef, String str, String leix) {// 得到上传文件名(上报表,月份,电厂编码,类型)
		/*
		 * 接口文件名称说明:接口文件名由8位字符组成如:1019308M 第1位代表报表的类型 1调燃01表 2调燃02表 3cpi燃料管理01表
		 * 第2-3位代表月份 第4-7位代表电厂 由电厂编码的第1位和电厂编码的最后3位构成 第8位代表当月累计 M代表当月 A代表累计
		 */
		StringBuffer Str_zf = new StringBuffer();

		Str_zf.append(TN);// 得到第一位N为报表类型(1,2,3,4,6,8,z)
		// Str_zf.append(getNianfValue().getValue().substring(2));
		if (yuef == 10 || yuef == 11 || yuef == 12) {// 得到第2,3位
			Str_zf.append(yuef);
		} else {
			Str_zf.append("0" + yuef);
		}
		// 得到电厂编码
		if (str == null && str.equals("")) {
			Str_zf.append("0000");
		} else {
			Str_zf.append(str.substring(0, 1));
			Str_zf.append(str.substring(str.length() - 3, str.length()));
		}
		Str_zf.append(leix);

		return Str_zf.toString();
	}

	private String getStr(int weis, String str) {
		StringBuffer Str_zf = new StringBuffer();
		if (str == null || str.equals("")) {
			for (int i = 0; i < weis; i++) {
				Str_zf.append(" ");
			}
		} else {
			char[] Str = str.toCharArray();
			int Str_lenght = Str.length;

			for (int j = 0; j < Str_lenght; j++) {
				String Strs = "" + Str[j];
				Str_zf.append(Strs);
			}
			int cha = 0;
			if (Str_lenght != weis) {
				cha = weis - Str_lenght;
				for (int i = 0; i < cha; i++) {
					Str_zf.append(" ");
				}
			}
		}
		return Str_zf.toString();
	}

	private String getStr_cpi(int weis, String str) {
		StringBuffer Str_zf = new StringBuffer();
		if (str == null || str.equals("")) {
			for (int i = 0; i < weis; i++) {
				Str_zf.append(" ");
			}
		} else {
			char[] Str = str.toCharArray();
			int Str_lenght = Str.length;

			for (int j = 0; j < Str_lenght; j++) {
				String Strs = "" + Str[j];
				Str_zf.append(Strs);
			}
			int cha = 0;
			if (Str_lenght != weis) {
				cha = weis - Str_lenght;
				for (int i = 0; i < cha; i++) {
					Str_zf.append("0");
				}
			}
		}
		return Str_zf.toString();
	}

	private String getNum(int weis, int xiaos, String Number) {// 得到位数及数符串
		StringBuffer Str_zf = new StringBuffer();
		String str = "";
		str = Number;
		if (str.equals("")) {
			for (int j = 0; j < weis - xiaos - 2; j++) {
				String Strs = "";
				Str_zf.append(Strs);
			}
			Str_zf.append(0.);
			for (int j = 0; j < xiaos; j++) {
				Str_zf.append(0);
			}
		} else {
			int zhengsw = 0;
			if (xiaos != 0) {// 带小数位的
				String[] c = str.split("\\.");
				String strs1 = c[0];// 整数位
				char[] Str1 = strs1.toCharArray();// 整数位
				String Strs2 = c[1];// 小数位
				char[] Str2 = Strs2.toCharArray();// 小数位
				// 录入整数位
				zhengsw = weis - xiaos - 1;
				if (Str1.length != zhengsw) {
					int cha = zhengsw - Str1.length;
					for (int i = 0; i < cha; i++) {
						Str_zf.append(" ");
					}
				}
				for (int j = 0; j < Str1.length; j++) {
					String Strs = "" + Str1[j];
					Str_zf.append(Strs);
				}
				// 录入小数位
				Str_zf.append(".");
				if (Str2.length != xiaos) {
					for (int j = 0; j < Str2.length; j++) {
						String Strs = "" + Str2[j];
						Str_zf.append(Strs);
					}
					for (int j = 0; j < xiaos - Str2.length; j++) {
						Str_zf.append(0);
					}
				} else {
					for (int j = 0; j < Str2.length; j++) {
						String Strs = "" + Str2[j];
						Str_zf.append(Strs);
					}
				}
			} else {// 不带小数位
				char[] Str = str.toCharArray();
				int Str_lenght = Str.length;
				int cha = 0;
				if (Str_lenght != weis) {// 补空格
					cha = weis - Str_lenght;
					for (int i = 0; i < cha; i++) {
						Str_zf.append(" ");
					}
				}
				for (int j = 0; j < Str_lenght; j++) {// 录入数据
					String Strs = "" + Str[j];
					Str_zf.append(Strs);
				}
			}
		}

		return Str_zf.toString();
	}

	public void getSelectData() {
		ResultSetList rsl = null;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();

		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");

		// ---------------------------------------------------------------------
		String context = MainGlobal.getHomeContext(this);
		String chaolj = "";

		// chaolj="get_Zhuangt("+getTreeid()+","+CurrODate+",sjb.shujbmc) as
		// zhuangt \n";

		if (rsl == null) {
			String strSql = "select sjb.id as id,sjb.xuh as xuh,sjb.baobmc as baobmc \n"
					+ // +",\n" +
					// chaolj+
					"from yuebbdysjb sjb\n"
					+ "where sjb.zhuangt=2\n"
					+ "order by xuh";

			rsl = con.getResultSetList(strSql);
		}

		rsl.beforefirst();
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuebshsjb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		// 设置多选框
		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		egu.getColumn(1).setAlign("middle");

		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("xuh").setHidden(true);
		egu.getColumn("baobmc").setHeader("报表名称");
		egu.getColumn("baobmc").setWidth(300);
		egu.getColumn("baobmc").setEditor(null);
		// egu.getColumn("zhuangt").setHeader("状态");
		// egu.getColumn("zhuangt").setWidth(100);
		// egu.getColumn("zhuangt").setEditor(null);

		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("月份");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		// 上报按钮
		egu.addToolbarButton("上报中能", GridButton.ButtonType_SubmitSel,
				"ShangbButton", "", SysConstant.Btn_Icon_SelSubmit);

		// 调燃设置
		egu.addToolbarItem("{" + new GridButton("调燃设置", "function(){" +

		"document.getElementById('DiaorszButton').click();" + "}").getScript()
				+ "}");

		// cpi01供货单位设置
		egu.addToolbarItem("{"
				+ new GridButton("cpi01供货单位设置", "function(){" +

				"document.getElementById('GonghdwszButton').click();" + "}")
						.getScript() + "}");

		// 中能供货单位设置
		egu.addToolbarItem("{"
				+ new GridButton("中能供货单位设置", "function(){" +

				"document.getElementById('ZhongnghdwszButton').click();" + "}")
						.getScript() + "}");

		// 电厂名称设置
		egu.addToolbarItem("{" + new GridButton("电厂名称设置", "function(){" +

		"document.getElementById('DanwmcszButton').click();" + "}").getScript()
				+ "}");

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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	// 调燃设置
	private void Diaorsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString4(getChange());
		cycle.activate("Shenhsz");
	}

	// cpi01供货单位设置
	private void Gonghdwsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString5(getChange());
		cycle.activate("Gonghdwsz");
	}

	// 中能供货单位设置
	private void Zhongnghdwsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString6(getChange());
		cycle.activate("Zhongnghdw");
	}

	// cpi01电厂名称设置
	private void Danwmcsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString7(getChange());
		cycle.activate("Danwmcsz");
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
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			visit.setString4(null);
			visit.setString5(null);
			visit.setString6(null);
			visit.setString7(null);
			setYuefValue(null);
			setNianfValue(null);
			this.getYuefModels();
			this.getNianfModels();

			setRiq();

		}
		getSelectData();
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString3());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString3();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString3(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	// 年份下拉框
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
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

	// 月份下拉框
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
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

}
