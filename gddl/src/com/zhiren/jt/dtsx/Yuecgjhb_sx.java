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
 *2010-03-02针对大唐陕西分公司调运部提出的调运日报制作的按煤矿地区或者煤矿单位录入月计划的页面
 *数据保存至原有数据库表yuecgjhb,代码基本复制于Yuecgjhb.java
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
		// TODO 自动生成方法存根
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

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// 获得日期
		String strriq = intyear + "-" + StrMonth1 + "-01";
		
		StringBuffer strmsg = new StringBuffer();
		
		String sql_dianc="select id,mingc from diancxxb where (id="+this.getTreeid()
		+" or fuid="+this.getTreeid()+" or shangjgsid="+this.getTreeid()+") and jib=3 ";
		
		ResultSetList rs = con.getResultSetList(sql_dianc);
		while (rs.next()){
			if (Xiad_dc(strriq,rs.getString("id"))){
				strmsg.append(rs.getString("mingc")).append(":").append(
				"下达数据成功!").append("<br>");
			}else{
				strmsg.append(rs.getString("mingc")).append(":").append(
						"下达数据失败").append("<br>");
			}
		}
		
		if (strmsg.toString().equals("")) {
			setMsg("没有下达数据！");
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
			//System.out.println("第"+i+"行 "+rs.getLong("diancxxb_id")+"sql=;\n"+sb[i]+"\n");
		}
		
		InterCom inter = new InterCom();
		boolean flag=true;
		
		String[] insert = inter.sqlExe(diancxxbid, sb, true);
		if (insert[0].equals("true")) {// 判断是下达成功
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

	public void Xiad1() {// 下达

		// Visit visit = (Visit) this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
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

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// 获得日期
		String strriq = intyear + "-" + StrMonth1 + "-01";
		//
		StringBuffer strmsg = new StringBuffer();// 消息框

		String shangygdiancid = "";
		int j = 0;// 插入数据的序号
		StringBuffer yuecgid = new StringBuffer();
		int rows = Shuj.size();
		String sql[] = new String[rows];

		InterCom inter = new InterCom();

		Iterator it = Shuj.iterator();

		for (int i = 0; it.hasNext(); i++) {
			String v[] = (String[]) it.next();// 得到每一行的数据

			String yuecgjhid = v[0];// id
			String riq = v[1];// 日期
			String diancxxbmc = v[2];// 电厂名称
			String diancbm = getIDropDownDiancid(diancxxbmc);// getIDropDownDiancbm(diancxxbmc);//电厂编码
			String gonghdwmc = v[3];// 供货单位名称
			String gonghdwbm = getIDropDownGonghdwbm(gonghdwmc);// 供货单位编码
			String jihkjmc = v[4];// 计划口径名称
			String fazmc = v[5];// 发站名称
			// String daozmc=v[6];//到站名称
			String yunsfsmc = v[7];// 运输方式
			String yuejhcgl = v[8];// 月计划采购量
			String rez = v[9];// 热值
			String huiff = v[10];// 挥发分
			String liuf = v[11];// 硫分
			String chebjg = v[12];// 车板价格
			String yunf = v[13];// 运费
			String zaf = v[14];// 杂费
			String daocj = v[15];// 到厂价
			String biaomdj = v[16];// 标煤单价

			if (i == 0) {
				yuecgid.append(yuecgjhid).append(",");// 收集月采购计划下达数据的ID

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

				if (i == rows - 1) {// 最后一行数
					String sql1[] = new String[j];
					for (int ii = 0; ii < sql1.length; ii++) {
						sql1[ii] = sql[ii];
					}
					String diancxxbid = getIDropDownDiancid(shangygdiancid);// 得到上个电厂ID
					String[] insert = inter.sqlExe(diancxxbid, sql1, true);
					yuecgid.deleteCharAt(yuecgid.length() - 1);// 去掉最后一个
					if (insert[0].equals("true")) {// 判断是下达成功
						String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
								+ strriq
								+ "','yyyy-mm-dd') and id in ("
								+ yuecgid.toString() + ")";

						con.getUpdate(update_sql);

						strmsg.append(shangygdiancid).append(":").append(
								"下达数据成功!").append("<br>");
					} else {
						strmsg.append(shangygdiancid).append(":").append(
								insert[0]).append("<br>");
					}
				}
			} else {
				if (!diancxxbmc.equals(shangygdiancid)) {// 判断是否是同一个电厂，如果不是
					String diancxxbid = "";
					diancxxbid = getIDropDownDiancid(shangygdiancid);// 得到上个电厂ID
					String sql1[] = new String[j];
					for (int ii = 0; ii < sql1.length; ii++) {
						sql1[ii] = sql[ii];
					}
					String[] insert = inter.sqlExe(diancxxbid, sql1, true);
					yuecgid.deleteCharAt(yuecgid.length() - 1);// 去掉最后一个
					if (insert[0].equals("true")) {// 判断是下达成功
						String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
								+ strriq
								+ "','yyyy-mm-dd') and id in ("
								+ yuecgid.toString() + ")";

						con.getUpdate(update_sql);

						strmsg.append(shangygdiancid).append(":").append(
								"下达数据成功!").append("<br>");

					} else {

						strmsg.append(shangygdiancid).append(":").append(
								insert[0]).append("<br>");
					}

					yuecgid.setLength(0);// 清空
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
					yuecgid.append(yuecgjhid).append(",");// 收集月采购计划下达数据的ID

					if (i == rows - 1) {// 最后一行数
						diancxxbid = getIDropDownDiancid(shangygdiancid);// 得到上个电厂ID
						String sql2[] = new String[j];
						for (int ii = 0; ii < sql2.length; ii++) {
							sql2[ii] = sql[ii];
						}
						String[] insert1 = inter.sqlExe(diancxxbid, sql2, true);
						yuecgid.deleteCharAt(yuecgid.length() - 1);// 去掉最后一个
						if (insert1[0].equals("true")) {// 判断是下达成功
							String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
									+ strriq
									+ "','yyyy-mm-dd') and id in ("
									+ yuecgid.toString() + ")";

							con.getUpdate(update_sql);

							strmsg.append(shangygdiancid).append(":").append(
									"下达数据成功!").append("<br>");
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
					yuecgid.append(yuecgjhid).append(",");// 收集月采购计划下达数据的ID

					if (i == rows - 1) {// 最后一行数
						String diancxxbid = getIDropDownDiancid(shangygdiancid);// 得到上个电厂ID
						String sql2[] = new String[j];
						for (int ii = 0; ii < sql2.length; ii++) {
							sql2[ii] = sql[ii];
						}
						String[] insert = inter.sqlExe(diancxxbid, sql2, true);
						yuecgid.deleteCharAt(yuecgid.length() - 1);// 去掉最后一个
						if (insert[0].equals("true")) {// 判断是下达成功
							String update_sql = " update yuecgjhb set zhuangt=1 where riq=to_date('"
									+ strriq
									+ "','yyyy-mm-dd') and id in ("
									+ yuecgid.toString() + ")";

							con.getUpdate(update_sql);

							strmsg.append(shangygdiancid).append(":").append(
									"下达数据成功!").append("<br>");
						} else {
							strmsg.append(shangygdiancid).append(":").append(
									insert[0]).append("<br>");
						}
					}
				}

			}

		}
		if (strmsg.toString().equals("")) {
			setMsg("没有下达数据！");
		} else {
			strmsg.delete(strmsg.length() - 4, strmsg.length());
			setMsg(strmsg.toString());
		}
		con.Close();
	}

	public void Shengc() {// 生成
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
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

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// 获得日期
		String strriq = intyear + "-" + StrMonth1 + "-01";

		// 得到电厂名称
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
		 * 选集团时刷新出所有的电厂 str = ""; } else if (treejib == 2) {//
		 * 选分公司的时候刷新出分公司下所有的电厂 str = "and (dc.id = " + getTreeid() + " or
		 * dc.fuid = " + getTreeid() + ")"; } else if (treejib == 3) {//
		 * 选电厂只刷新出该电厂 str = "and dc.id = " + getTreeid() + ""; }
		 */

		String sql = "select * from yuecgjhb y,vwdianc dc where y.diancxxb_id=dc.id and riq=to_date('"
				+ strriq + "','yyyy-mm-dd') " + str + " ";

		if (con.getHasIt(sql)) {// 有数据

		} else {// 没有数据
			String inser_sql = "insert into yuecgjhb\n"
					+ "  (id, riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
					+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
					+ "  biaomdj, yunsfsb_id, jizzt, zhuangt, meikxxb_id)\n"
					+ "(select getnewid(diancxxb_id) as id,riq, diancxxb_id, gongysb_id, jihkjb_id, faz_id, daoz_id, yuejhcgl,\n"
					+ "  chebjg, yunf, zaf, rez, jiakk, jihddsjysl, huiff, liuf, daocj,\n"
					+ "  biaomdj, yunsfsb_id, jizzt, 1, meikxxb_id\n"
					+ "  from yuesbjhb y,vwdianc dc where y.diancxxb_id=dc.id and riq=to_date('"
					+ strriq + "','yyyy-mm-dd') " + str + " )";

			con.getInsert(inser_sql);// 插入数据
		}

		con.Close();
	}

	public void CoypLastYueData() {// 复制上月数据
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
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

		String StrMonth1 = "";
		if (intMonth < 10) {

			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// -----------------------------------
		String strriq = intyear + "-" + StrMonth1 + "-01";

		// 当月份是1时上个月显示12
		if (intMonth - 1 == 0) {
			intMonth = 12;
			intyear = intyear - 1;
		} else {
			intMonth = intMonth - 1;
		}
		// 当月份是1的时候显示01,
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
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = " and (dc.fuid=  " + this.getTreeid()+ " or dc.shangjgsid=" + this.getTreeid() + " or dc.id="+this.getTreeid()+")";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
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

		// System.out.println("复制同期的数据:"+copyData);
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
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}

		visit.setString3("" + intyear + "-" + StrMonth);// 年份
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

			if (jib == 1) {// 选集团时刷新出所有的电厂
				strgrouping
						.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,dc.mingc) as diancxxb_id,\n");
				strgrouping
						.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'小计'),g.mingc) as gongysb_id,\n");
				strwhere.append("");
				strgroupby
						.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc,j.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt,y.zhuangt)\n");
				strhaving
						.append(" having not (grouping(g.mingc) || grouping(y.zhuangt)) =1 \n");
				strorderby
						.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
			} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = "select id from diancxxb where shangjgsid= "
						+ this.getTreeid();

				try {
					ResultSet rl = con.getResultSet(ranlgs);
					if (rl.next()) {// 燃料公司
						strgrouping
								.append("decode(grouping(dc.rlgsmc)+grouping(dc.mingc),2,'总计',1,dc.rlgsmc,dc.mingc) as diancxxb_id,\n");
						strgrouping
								.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'小计'),g.mingc) as gongysb_id,\n");
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
					} else {// 分公司
						strgrouping
								.append("decode(grouping(dc.mingc),1,'总计',1,dc.mingc,dc.mingc) as diancxxb_id,\n");
						strgrouping
								.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'小计'),g.mingc) as gongysb_id,\n");
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
			} else if (jib == 3) {// 选电厂只刷新出该电厂
				strgrouping
						.append("decode(grouping(dc.mingc),1,'总计',dc.mingc) as diancxxb_id,\n");
				strgrouping
						.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'小计'),g.mingc) as gongysb_id,\n");
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
			strSQL.append("decode(sum(y.yuejhcgl),0,0,round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl)*1000/4.1816,2)) as rez_dk,\n");// 热值_大卡
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n");// 挥发分
			strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n");// 硫分
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
			 * getLinkMingxTaiz('1',dc.id,'月需用量计划') as mingx,\n" + " y.riq as
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
			// Shuj = rsl;//获得数据的做为下达数据用。
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
		egu.getColumn("riq").setCenterHeader("日期");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		/*
		 * egu.getColumn("mingx").setCenterHeader("明细"); if (flage == 0) {//
		 * 是否中电投使用 egu.getColumn("mingx").setHidden(true); }
		 */
		egu.setDefaultsortable(false);//设置每列表头点击后不可以排序。
		egu.getColumn("diancxxb_id").setCenterHeader("电厂名称");
		egu.getColumn("gongysb_id").setCenterHeader("供货单位");
		egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
		egu.getColumn("faz_id").setCenterHeader("发站");
		egu.getColumn("daoz_id").setCenterHeader("到站");
		egu.getColumn("yunsfsb_id").setCenterHeader("运输<br>方式");
		egu.getColumn("yuejhcgl").setCenterHeader("计划量<br>(吨)");
		egu.getColumn("rez").setCenterHeader("热值<br>(MJ/Kg)");
		egu.getColumn("rez_dk").setHeader("热值<br>(kcal/Kg)");
		egu.getColumn("rez_dk").setEditor(null);
		egu.getColumn("rez_dk").setUpdate(false);
		egu.getColumn("huiff").setCenterHeader("挥发分<br>%");
		egu.getColumn("liuf").setCenterHeader("硫分<br>%");
		egu.getColumn("chebjg").setCenterHeader("车板价<br>(元/吨)");
		egu.getColumn("yunf").setCenterHeader("运费<br>(元/吨)");
		egu.getColumn("zaf").setCenterHeader("杂费<br>(元/吨)");
		egu.getColumn("daocj").setCenterHeader("到厂价<br>(元/吨)");
		egu.getColumn("biaomdj").setCenterHeader("不含税标煤<br>单价(元/吨)");
		egu.getColumn("jiakk").setCenterHeader("加扣款<br>(元/吨)");
		egu.getColumn("jiakk").setHidden(true);
		egu.getColumn("jihddsjysl").setCenterHeader("计划到达时<br>间与数量");
		egu.getColumn("jihddsjysl").setHidden(true);

		egu.getColumn("jizzt").setHeader("机组状态");
		egu.getColumn("jizzt").setHidden(true);

		egu.getColumn("zhuangt").setHeader("数据状态");
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
		
		
		// 设置不可编辑的颜色
		// egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext';
		// return value;}");

		// 设定列初始宽度
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

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(100);// 设置分页
		egu.setWidth("bodyWidth");
		
		
		
		// *****************************************设置默认值****************************
		// 电厂下拉框
		egu.getColumn("zhuangt").setDefaultValue("0");

		if (jib == 1) {// 选集团时刷新出所有的电厂
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
		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
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
		} else if (jib == 3) {// 选电厂只刷新出该电厂
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

		// 设置电厂默认到站
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		// 设置日期的默认值,
		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// *************************下拉框*****************************************88
//		 设置供应商的下拉框
		// egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		
		//与电厂相关联的供应商 //增加查所有供应商，现供应商和电厂关联，否则查不出供应商
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
		
		// 设置计划口径的下拉框
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		String JihkjSql = "select id,mingc from jihkjb order by mingc ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(JihkjSql));
//		 设置发站下拉框
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						" where f.diancxxb_id=dc.id  and f.faz_id=c.id and c.leib='车站' "+strwhere+
						" union\n" +
						" select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
						" where n.diancxxb_id=dc.id and n.faz_id=c.id " +
						" and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
		                 "union\n" + 
		                 "select distinct c.id,c.mingc from chezxxb c  ";  
//		System.out.println(fazSql);
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		// 设置到站下拉框
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						 " where f.diancxxb_id=dc.id  and f.daoz_id=c.id and c.leib='车站' "+strwhere+
						 " union\n" +
						 " select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
						 " where n.diancxxb_id=dc.id and n.daoz_id=c.id " +
						 " and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
		                 "union\n" + 
		                 "select distinct c.id,c.mingc from chezxxb c  ";
//		System.out.println(daozSql);
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));

		// 设置运输方式下拉框
		ComboBox cb_yunsfs = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_yunsfs);
		cb_daoz.setEditable(true);

		String yunsfsSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsSql));
		egu.getColumn("yunsfsb_id").editor.allowBlank=true;
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		// 设定工具栏下拉框自动刷新
		// egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符

		/*
		 * egu.addTbarText("机组状态:"); ComboBox comb3=new ComboBox();
		 * comb3.setTransform("LeixDropDown");
		 * comb3.setId("LeixDropDown");//和自动刷新绑定
		 * comb3.setLazyRender(true);//动态绑定 comb3.setWidth(80);
		 * egu.addToolbarItem(comb3.getScript());
		 * 
		 * egu.addOtherScript("LeixDropDown.on('select',function(){document.forms[0].submit();});");
		 * egu.addTbarText("-");// 设置分隔符
		 */

		/*
		 * if (flage==1){//如果是中电投用户可下达数据 egu.addTbarText("数据状态:"); ComboBox
		 * comb4=new ComboBox(); comb4.setTransform("ZhuangtDropDown");
		 * comb4.setId("ZhuangtDropDown");//和自动刷新绑定
		 * comb4.setLazyRender(true);//动态绑定 comb4.setWidth(80);
		 * egu.addToolbarItem(comb4.getScript());
		 * 
		 * egu.addOtherScript("ZhuangtDropDown.on('select',function(){document.forms[0].submit();});");
		 * egu.addTbarText("-");// 设置分隔符 }
		 */

		// 刷新按钮
		StringBuffer rsb2 = new StringBuffer();
		rsb2
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'正在刷新'+Ext.getDom('NIANF').value+'年'+Ext.getDom('YUEF').value+'月的数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr2 = new GridButton("刷新", rsb2.toString());
		gbr2.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr2);

		// 生成按钮
//		StringBuffer rsb = new StringBuffer();
//		rsb.append("function (){")
//		// .append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NIANF').value+'年'+Ext.getDom('YUEF').value+'月的数据,请稍候！'",true))
//				.append("document.getElementById('ShengcButton').click();}");
//		GridButton gbr = new GridButton("生成", rsb.toString());
//		gbr.setIcon(SysConstant.Btn_Icon_Create);
//		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		// 下达按钮
		if (flage == 1) {// 如果是中电投用户可下达数据
			// if (getZhuangtDropDownValue().getValue().equals("未下达")){
			StringBuffer rsb1 = new StringBuffer();
			rsb1.append("function (){").append(
					MainGlobal
							.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200));
			// MainGlobal.getExtMessageBox("'正在下达数据......,请稍候！'",true));
			// rsb1.append(" var grid1_history =\"\";" +
			// " if(gridDiv_sm.getSelected()==null){ " +
			// " Ext.MessageBox.alert(\"提示信息\",\"请选中一行数据下发！\"); return; } " +
			// " grid1_rcd = gridDiv_sm.getSelected();" +
			// " if(grid1_rcd.get(\"ID\") == \"0\"){ " +
			// " Ext.MessageBox.alert(\"提示信息\",\"在下达数据之前请先保存!\"); return; }" +
			// " grid1_history = grid1_rcd.get(\"DIANCXXB_ID\");" +
			// " var Cobj = document.getElementById(\"CHANGE\");" +
			// " Cobj.value = grid1_history; " +
			rsb1.append(" document.getElementById(\"XiadButton\").click();}");

			GridButton gbr1 = new GridButton("下达", rsb1.toString());
			gbr1.setIcon(SysConstant.Btn_Icon_Cancel);
			egu.addTbarBtn(gbr1);
			// }
		}

		if (showBtn) {
			// egu.addToolbarItem("{"+new
			// GridButton("复制同期计划","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("复制前月数据", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		// egu.addTbarText("->");
		// egu.addTbarText("<font color=\"#EE0000\">单位:吨、元/吨、千卡/千克</font>");
		
//		2009-09-24 取得总计及小计行的颜色
		String BgColor="";
		String sql_color="select zhi from xitxxb where mingc ='总计小计行颜色' and zhuangt=1 ";
		
		rsl = con.getResultSetList(sql_color);
		
		if (rsl.next()){
			BgColor=rsl.getString("zhi");
		}
		rsl.close();

		// ---------------页面js的计算开始------------------------------------------
//		StringBuffer sb = new StringBuffer();
//		
//		sb.append("var rows=gridDiv_ds.getTotalCount();\n" +
//				"for (var i=rows-1;i>=0;i--){\n" + 
//				"\t var rec1=gridDiv_ds.getAt(i);\n" + 
//				"\t var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
//				"\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//取小计行\n" + 
//				"\t if (gonghdw1==\"-\" ||  xiaoj==\"小计\"){//小计行\n" + 
//				"\t\tgridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n" + 
//				"\t}\n" + 
//				"}\n");
//		
//		sb.append("gridDiv_grid.on('beforeedit',function(e){\n"
//						+ "if(e.record.get('GONGYSB_ID')=='-'){e.cancel=true;}//判断为“”时行不可编辑\n"
//						+ "var mingc=e.record.get('GONGYSB_ID');\n"
//						+ "var le=mingc.length;\n"
//						+ "var xiaoj=mingc.substring(le-6,le-4);//取小计行\n"
//						+ "if(xiaoj=='小计'){e.cancel=true;}//判断小计时行不可编辑\n"
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
//						+ "\tvar colJihl=8;//采购计划量\n"
//						+ "\tvar colRez=9;//热值\n"
//						+ "\tvar colRez_dk=10;//热值_dk\n"
//						+ "\tvar colHuiff=11;//挥发分\n"
//						+ "\tvar colLiuf=12;//硫分\n"
//						+ "\tvar colChebjg=13;//车板价格\n"
//						+ "\tvar colYunf=14;//运费\n"
//						+ "\tvar colZaf=15;//杂费\n"
//						+ "\tvar colDaocj=16;//到厂价\n"
//						+ "\tvar colBiaomdj=17;//标煤单价\n"
//						+ "\n"
//						+ "\tvar rowXJ=0;\n"
//						+ "\tvar rowZj=1;\n"
//						+ "\tvar ArrSumZJ=new Array(20);\n"
//						+ "\tvar ArrSumXJ=new Array(20);\n"
//						+ "\tvar cgl_xj;//采购量小计\n"
//						+ "\n"
//						+ "\tfor (var i=0 ;i<20;i++){\n"
//						+ "\t\tArrSumXJ[i]=0.0;\n"
//						+ "\t\tArrSumZJ[i]=0.0;\n"
//						+ "\t}\n"
//						+ "\tfor (var i=rows-1;i>=0;i--){\n"
//						+ "\t\t var rec1=gridDiv_ds.getAt(i);\n"
//						+ "\t\t var gonghdw1=rec1.get('GONGYSB_ID');\n"
//						+ "\t\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//取小计行\n"
//						+ "\t\t if (xiaoj==\"小计\"){//小计行\n"
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
//						+ "\t\t \t//累加总计\n"
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
//						+ "\t\t\t//清除小计\n"
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
//						+ "\t\t }else if ( i==0) {//总计行\n"
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
//						+ "\t\t \t//累加电厂内\n"
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
//		// 设定合计列不保存
//		sb.append("function gridDiv_save(record){ \n" +
//				" var gonghdw1=record.get('GONGYSB_ID');\n"+
//				" var mm; \n"+
//				" if (gonghdw1=='-') return 'continue';\n " +
//				" if (gonghdw1.length>6){mm=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);\n}" +
//				" if(mm==''|| mm=='小计') return 'continue';}\n");
//		
//		egu.addOtherScript(sb.toString());
		
//		计算大卡
//		StringBuffer sb2 = new StringBuffer();
//		sb2.append("gridDiv_grid.on('afteredit', function(e){");
//		sb2.append("if(e.field == 'REZ'){e.record.set('REZ_DK',parseFloat(e.value)/0.0041486);}");
//		sb2.append("});");
//		egu.addOtherScript(sb2.toString());
		// ---------------页面js计算结束--------------------------
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
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
			String sql = "select zhi from xitxxb where mingc = '下达数据' and diancxxb_id="
					+ visit.getDiancxxb_id();
			ResultSet rl = con.getResultSet(sql);
			try {
				if (rl.next()) {// 燃料公司
					this.flage = rl.getLong("zhi");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
		}

		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc = '下达数据' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSet rl = con.getResultSet(sql);
		try {
			if (rl.next()) {// 燃料公司
				this.flage = rl.getLong("zhi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		getSelectData(null);

	}

	// 年份
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

	// 月份
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
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// 得到登陆用户所在电厂或者分公司的名称
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// 得到电厂ID
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// 得到电厂编码
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// 供货单位编码
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownGonghdwbm;

	}

	// 得到电厂的默认到站
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

	// 类型
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
		list.add(new IDropDownBean(0, "现役机组"));
		list.add(new IDropDownBean(1, "新增机组"));
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(list));
		return;
	}

	// 数据状态--暂时不用
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
		list.add(new IDropDownBean(0, "未下达"));
		list.add(new IDropDownBean(1, "已下达"));
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(list));
		return;
	}
}