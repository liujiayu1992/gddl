package com.zhiren.dc.zhuangh.huaybl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.grid.ColumnModel;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;

import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：刘雨
 * 时间：2010-04-02
 * 描述：新增功能：1.用方向键控制光标移动
 * 				  2.去掉氢字段，变成提交时自动计算氢值
 * 		适用范围：大唐阳城
 */
/*
 * 作者：王磊
 * 时间：2009-09-04 14：29
 * 描述：由于增加了ExtGrid的配置造成了列数的不可预知性故去掉了回车替代TAB键的方法
 */
/*
 * 作者:夏峥	
 * 时间:2009-5-11
 * 修改内容:验证是否隐藏 化验类别
 */
/*
 * 作者：王磊
 * 时间：2009-06-26
 * 描述：增加xitxxb设置，休约低位发热量的小数位 默认为3位设置如下
 * 		mingc	= '低位发热量小数位'
 * 		zhi		= 小数位
 * 		leib 	= '化验'
 * 		zhuangt = 1
 * 		diancxxb_id = 电厂ID
 */
/*
 * 作者：王磊
 * 时间：2009-07-27
 * 描述：增加主要指标按回车键跳至下一个输入项的功能
 */
/*
 * 作者：王磊
 * 时间：2009-08-11 16：53
 * 描述：增加ExtGrid的配置可显示隐藏列
 */

/*
 * 修改时间：2009-09-18
 * 修改人：  ww
 * 修改内容：
 * 			1 增加厂别判断
 * 			在获取元素分析时没有加入厂别的判断，无法获取元素分析项目的值
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '分厂同矿氢值不同',
 '是',
 '',
 '化验',
 1,
 '使用'
 )
 
 *          2 添加采样时间,默认不显示
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '化验录入显示采样日期',
 '是',
 '',
 '化验',
 1,
 '使用'
 )
 */

/*作者:王总兵
 * 时间:2009-10-26 10:33:14
 * 修改内容:刷新加入制样日期的判断,只有宣威电厂用这种方法,
 *         暂时对其它电厂不适用
 * 
 * 
 * 
 */

/* 修改时间:2010-01-25 
 * 人员：liht
 * 修改内容:化验时间录入时由原来年月日基础上精确到时、分
 * 
 * 
 * 
 */
public class Ruchylrxg extends BasePage implements PageValidateListener {

	private String CustomSetKey = "Ruchylr";

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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getKuangm() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setKuangm(String kuangm) {
		((Visit) this.getPage().getVisit()).setString1(kuangm);
	}

	public String getBianh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setBianh(String bianh) {
		((Visit) this.getPage().getVisit()).setString2(bianh);
	}

	public String getShul() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setShul(String shul) {
		((Visit) this.getPage().getVisit()).setString3(shul);
	}

	public String getHuaysj() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setHuaysj(String huaysj) {
		((Visit) this.getPage().getVisit()).setString4(huaysj);
	}

	// 从数字合理范围表里取出 Mt, Mad，Aad,Vad,Std,Qbad的范围
	public ResultSetList getmaxmin(JDBCcon con, long diancxxb_id) {
		ResultSetList shuzhirsl;
		String sql = "select shangx,xiax,mingc from shuzhlfwb "
				+ "where leib = '质量' and beiz='化验录入'";
		shuzhirsl = con.getResultSetList(sql);
		return shuzhirsl;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		// 氢是否可编辑
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// 氢元素符号
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// 硫元素符号
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}
		// 数值合理范围
		ResultSetList szhlfw = getmaxmin(con, visit.getDiancxxb_id());
		// 转码类别中对应化验编码的ID号
		String zhuanmlbid = "";
		sql = "select id from zhuanmlb where mingc = '化验编码'";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			zhuanmlbid = rsl.getString("id");
		}
		rsl.close();
		if ("".equals(zhuanmlbid)) {
			WriteLog.writeErrorLog(ErrorMessage.ZhuanmlbNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.ZhuanmlbNotFound);
			return;
		}

		// 电厂Tree刷新条件
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
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			try {
				ResultSet rsss = con
						.getResultSet("select id from diancxxb where fuid="
								+ getTreeid());
				if (rsss.next()) {
					str = "and dc.fuid=" + getTreeid();
				} else {
					str = "and dc.id = " + getTreeid();
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

		}

		// 是否显示采样时间
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || "".equals(riqTiaoj)) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		boolean isShowRq = false;
		String strTemp = "";
		isShowRq = "是".equals(MainGlobal.getXitxx_item("化验", "化验录入显示采样日期",
				visit.getDiancxxb_id() + "", "否"));
		if (isShowRq) {
			strTemp = " AND (SELECT caiyrq FROM caiyb WHERE zhilb_id=l.zhilb_id)=to_date('"
					+ riqTiaoj + "','yyyy-mm-dd')\n";
		}

		// 是否为阳城发电
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电", visit
				.getDiancxxb_id()
				+ "", "否"));

		String sql_h = "";
		if (!isYc) {
			sql_h = " l." + sign_H + ",\n";
		}
		// 查询数据SQL
		// 说明:宣威发电的化验编号列表需要能够根据制样日期单独刷新出来,所以在这里sql单独写.其它电厂不适用
		if (visit.getDiancmc().equals("宣威发电")) {
			sql = "select l.id,\n" + "       to_char(z.bianm) as huaybh,\n"
					+ "       nvl(l.huaysj,sysdate) huaysj,\n"
					+ "       l.mt,\n" + "       l.mad,\n" + "       l.aad,\n"
					+ "       l.vad,\n" + "       l." + sign_S + ","
					+ "       l." + sign_H + ",\n" + "       l.qbad,\n"
					+ "       l.t1,\n" + "       l.t2,\n" + "       l.t3,\n"
					+ "       l.t4,\n" + "       huayy,\n" + "       l.lury,\n"
					+ "       l.beiz,\n" + "       l.huaylb\n"
					+ "from zhuanmb z, zhillsb l,\n"
					+ "       (select distinct f.zhilb_id\n"
					+ "        from fahb f,diancxxb dc\n"
					+ "        where f.diancxxb_id = dc.id\n" + str
					+ ") fh ,zhiyryb zy\n"
					+ "where z.zhillsb_id = l.id and zhuanmlb_id = "
					+ zhuanmlbid + "\n"
					+ "      and l.zhilb_id = fh.zhilb_id\n"
					+ "      and (l.shenhzt = 0 or l.shenhzt = 2)\n"
					+ "      and l.zhilb_id=zy.zhilb_id\n"
					+ "      and zy.zhiyrq=to_date('" + this.getZhiyrq()
					+ "','yyyy-mm-dd')\n" + strTemp + "order by l.id, l.huaylb";
		} else {
			sql = "select l.id,\n"
					+ "       to_char(z.bianm) as huaybh,\n"
					+ "       nvl(l.huaysj,sysdate) huaysj,\n"
					+ "       l.mt,\n"
					+ "       l.mad,\n"
					+ "       l.aad,\n"
					+ "       l.vad,\n"
					+ "       l."
					+ sign_S
					+ ","
					+ sql_h
					+ "       l.qbad,\n"
					// + " l." + sign_H + ",\n" + " l.qbad,\n"
					+ "       l.t1,\n" + "       l.t2,\n" + "       l.t3,\n"
					+ "       l.t4,\n" + "       huayy,\n" + "       l.lury,\n"
					+ "       l.beiz,\n" + "       l.huaylb,l.leix\n"
					+ "from zhuanmb z, zhillsb l,\n"
					+ "       (select distinct f.zhilb_id\n"
					+ "        from fahb f,diancxxb dc\n"
					+ "        where f.diancxxb_id = dc.id\n" + str + ") fh\n"
					+ "where z.zhillsb_id = l.id and zhuanmlb_id = "
					+ zhuanmlbid + "\n"
					+ "      and l.zhilb_id = fh.zhilb_id\n"
					+ "      and (l.shenhzt = 0 or l.shenhzt = 2)\n" + strTemp
					+ "order by l.id, l.huaylb";
		}
		String ssql = "select b.id,b.huaybh,to_char(b.huaysj,'YYYY-MM-DD hh24:mi:ss') huaysj,round_new(b.qnet_ar/0.0041816,0) as qnet_ar" +
				//",b.biaoz,b.sanfsl,b.daohrq" +
				",b.mt,b.mad,b.aad,b.vad,b.stad,b.had,b.qbad,b.t1,b.t2,b.t3,b.t4,b.huayy,b.lury,b.beiz ,b.huaylb from\n"
				+ "(\n"
				+ "select  z.id,zb.bianm as huaybh ,   nvl(z.huaysj,sysdate) huaysj,\n"
				+ "        z.mt,z.mad,z.aad,z.vad,z.stad,z.had,z.qbad,z.t1,z.t2,z.t3,z.t4,z.huayy,z.lury,z.beiz,z.huaylb \n"
				+ " from yangpdhb yb ,zhillsb z,zhuanmb zb,zhuanmlb zlb\n"
				+ "  where yb.zhilblsb_id=z.id\n"
				+ "  and yb.zhilblsb_id=zb.zhillsb_id\n"
				+ "  and zb.zhuanmlb_id=zlb.id\n"
				+ "  and zlb.jib=2\n"
				//+ "  and f.zhilb_id=z.zhilb_id"
				+ "  and zlb.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "  and z.shenhzt=6\n"
				+ "  ) a,\n"
				+ "  (\n"
				+ "    select z.id,zb.bianm as huaybh ,   nvl(z.huaysj,sysdate) huaysj,\n"
				+ "       z.qnet_ar, z.mt,z.mad,z.aad,z.vad,z.stad,z.had,z.qbad,z.t1,z.t2,z.t3,z.t4,z.huayy,z.lury,z.beiz,z.huaylb" +
						" \n"
				+ " from yangpdhb yb ,zhillsb z,zhuanmb zb,zhuanmlb zlb\n"
				+ "  where yb.zhilblsb_id=z.id\n"
				+ "  and yb.zhilblsb_id=zb.zhillsb_id\n"
				+ "  and zb.zhuanmlb_id=zlb.id\n"
				+ "  and zlb.jib=3\n"
				//+ "  and f.zhilb_id=z.zhilb_id"
				+ "  and zlb.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "  and z.shenhzt=6\n"
				+ "  ) b\n"
				+ "  where b.id=a.id(+)\n"
				+ "   \n"
				+ "\n"
//				+ "   union\n"
//				+ "   select distinct c.zhilb_id as id,c.huaybh,to_char(min(c.huaysj),'YYYY-MM-DD hh24:mi:ss') huaysj,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb \n"
//				+ "   from\n"
//				+ "  ( select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n"
//				+ "          rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'入炉' as huaylb\n"
//				+ "   from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n"
//				+ "   where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n"
//				+ "   and zb.zhillsb_id=rb.zhuanmbzllsb_id\n"
//				+ "   and zb.zhuanmlb_id=zlb.id\n"
//				+ "   and zlb.jib=3\n"
//				+ "   and zlb.diancxxb_id="
//				+ visit.getDiancxxb_id()
//				+ "\n"
//				+ "   --and rb.shenhzt=0\n"
//				+ "   ) c,\n"
//				+ "   (\n"
//				+ "        select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n"
//				+ "          rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'入炉' as huaylb \n"
//				+ "   from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n"
//				+ "   where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n"
//				+ "   and zb.zhillsb_id=rb.zhuanmbzllsb_id\n"
//				+ "   and zb.zhuanmlb_id=zlb.id\n"
//				+ "   and zlb.jib=2\n"
//				+ "   and zlb.diancxxb_id="
//				+ visit.getDiancxxb_id()
//				+ "\n"
//				+ " --  and rb.shenhzt=0\n"
//				+ "   ) d\n"
//				+ "   where c.zhilb_id=d.zhilb_id(+) group by (c.zhilb_id ,c.huaybh,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb)"


				;

		
		rsl = con.getResultSetList(ssql);
		// ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomSetKey);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //

		// //设置表名称用于保存
		egu.setTableName("zhillsb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth("bodyWidth");
		// /设置显示列名称
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("收到基低位热值(Kcal/kg)");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("mt").setHeader("全水分Mt(%)");
		egu.getColumn("mad").setHeader("水分Mad(%)");
		egu.getColumn("aad").setHeader("灰分Aad(%)");
		egu.getColumn("vad").setHeader("挥发分Vad(%)");
		egu.getColumn("qbad").setHeader("弹筒热量Qbad(MJ/kg)");
		egu.getColumn("stad").setHeader("硫分" + "stad" + "(%)");
		// egu.getColumn(sign_S).setHeader("硫分" + sign_S + "(%)");
		if (!isYc) {
			egu.getColumn(sign_H).setHeader("氢" + sign_H + "(%)");
		}
//		egu.getColumn("biaoz").setHeader("运单量");
//		egu.getColumn("sanfsl").setHeader("水尺量");
//		egu.getColumn("daohrq").setHeader("到货日期");
//		egu.getColumn("biaoz").setEditor(null);
//		egu.getColumn("sanfsl").setEditor(null);
//		egu.getColumn("daohrq").setEditor(null);
		
		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t3").setHidden(true);
		egu.getColumn("t2").setHidden(true);
		egu.getColumn("t1").setHidden(true);
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("t4").setHidden(true);
		egu.getColumn("huayy").setCenterHeader("化验员");
		egu.getColumn("huayy").setWidth(150);
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("huaylb").setHeader("化验类别");
		egu.getColumn("huaylb").setEditor(null);

		// egu.getColumn("leix").setCenterHeader("采样类型");//采样类型
		// egu.getColumn("leix").setWidth(70);
		// egu.getColumn("leix").setHidden(true);

		// egu.getColumn("huaysj").setColtype(GridColumn.ColType_default);
		egu.getColumn("huaysj").setCenterHeader("化验时间");
		egu.getColumn("huaysj").setWidth(120);
		egu.getColumn("huaysj").editor.setAllowBlank(false);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("mt").editor.setMaxValue("100");
		egu.getColumn("mt").editor.setMinValue("0");
		egu.getColumn("mt").setColtype(GridColumn.ColType_default);
		egu.getColumn("mt").editor.setAllowBlank(false);

		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mad").editor.setMaxValue("100");
		egu.getColumn("mad").editor.setMinValue("0");
		egu.getColumn("mad").setColtype(GridColumn.ColType_default);
		egu.getColumn("mad").editor.setAllowBlank(false);

		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").editor.setMaxValue("100");
		egu.getColumn("aad").editor.setMinValue("0");
		egu.getColumn("aad").setColtype(GridColumn.ColType_default);
		egu.getColumn("aad").editor.setAllowBlank(false);

		egu.getColumn("vad").setWidth(80);
		egu.getColumn("vad").editor.setMaxValue("100");
		egu.getColumn("vad").editor.setMinValue("0");
		egu.getColumn("vad").setColtype(GridColumn.ColType_default);
		egu.getColumn("vad").editor.setAllowBlank(false);

		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("qbad").editor.setMaxValue("100");
		egu.getColumn("qbad").editor.setMinValue("0");
		egu.getColumn("qbad").setColtype(GridColumn.ColType_default);
		egu.getColumn("qbad").editor.setAllowBlank(false);

		egu.getColumn("stad").setWidth(80);
		egu.getColumn("stad").editor.setMaxValue("100");
		egu.getColumn("stad").editor.setMinValue("0");
		egu.getColumn("stad").setColtype(GridColumn.ColType_default);
		// egu.getColumn("stad").setHidden(true);

		// egu.getColumn(sign_S).editor.setMaxValue("100");
		// egu.getColumn(sign_S).editor.setMinValue("0");
		// egu.getColumn(sign_S).setColtype(GridColumn.ColType_default);
		// egu.getColumn(sign_S).setHidden(!editable_S);

		// ComboBox c9 = new ComboBox();
		// egu.getColumn("leix").setEditor(c9);
		// egu.getColumn("leix").setDefaultValue("入厂");
		// c9.setEditable(true);
		// String MeikbSql=
		// "select 0 as id,'入厂' as mingc from dual\n" +
		// "union\n" +
		// "select 1 as id,'入炉' as mingc from dual\n" +
		// "union\n" +
		// "select 2 as id,'其他' as mingc from dual\n" +
		// "order by id";
		//
		// egu.getColumn("leix").setComboEditor(egu.gridId, new
		// IDropDownModel(MeikbSql));
		// egu.getColumn("leix").setReturnId(true);
		if (editable_S) {
			egu.getColumn("stad").editor.setAllowBlank(false);
			if (isYc) {
				egu.getColumn("stad").editor.setListeners(getStr(9));
			}
		}

		if (!isYc) {
			egu.getColumn(sign_H).setWidth(80);
			egu.getColumn(sign_H).editor.setMaxValue("100");
			egu.getColumn(sign_H).editor.setMinValue("0");
			egu.getColumn(sign_H).setColtype(GridColumn.ColType_default);
			egu.getColumn(sign_H).setHidden(!editable_H);
			if (editable_H) {
				egu.getColumn(sign_H).editor.setAllowBlank(false);
			}
		}

		// 用用户设置的范围覆盖程序默认
		while (szhlfw.next()) {
			GridColumn gc = egu.getColumn(szhlfw.getString("mingc"));
			if (gc != null) {
				gc.editor.setMaxValue(szhlfw.getString("shangx"));
				gc.editor.setMinValue(szhlfw.getString("xiax"));
			}
		}
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t1").setDefaultValue("20");
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t2").setDefaultValue("20");
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t3").setDefaultValue("20");
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("t4").setDefaultValue("20");
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setWidth(80);



		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(22);

		// 判断化验时间录入是否精确到时分
		String strXitszzt = MainGlobal.getXitxx_item("化验",
				Locale.huaysjlrsfjqdsf_xitxx, String.valueOf(getTreeid()), "否");
		if (strXitszzt.equals("是")) {
			DatetimeField huaysj = new DatetimeField();
			huaysj.setFormat("Y-m-d H:i");
			huaysj.setMenu("new DatetimeMenu()");
			egu.getColumn("huaysj").setRenderer(GridColumn.Renderer_DateTime);
			egu.getColumn("huaysj").setEditor(huaysj);
		}

		DatetimeField datetime = new DatetimeField();
		egu.getColumn("huaysj").setEditor(datetime);
		egu.getColumn("huaysj").setDefaultValue(
				DateUtil.FormatDateTime(new Date()));

		// Toolbar tb1 = new Toolbar("tbdiv");
		// egu.setGridSelModel(3);
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// 是否显示采样日期
		if (isShowRq) {
			egu.addTbarText("采样日期:");
			DateField df = new DateField();
			df.setValue(this.getRiq());
			df.Binding("Caiyrq", "");// 与html页中的id绑定,并自动刷新
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("-");
		}
		if (visit.getDiancmc().equals("宣威发电")) {
			egu.addTbarText("制样日期:");
			DateField zhiyrq = new DateField();
			zhiyrq.Binding("Zhiyrq", "");
			zhiyrq.setValue(this.getZhiyrq());
			egu.addToolbarItem(zhiyrq.getScript());
			egu.addTbarText("-");
		}
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '化验人员',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'确定',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('HUAYY',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '化验人员',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("保存", GridButton.ButtonType_SubmitSel,
				"SaveButton", null, null);
		
		egu.addToolbarButton("提交", GridButton.ButtonType_SubmitSel,
				"SavebButton", null, null);
		egu.addToolbarButton("发货信息查询", GridButton.ButtonType_SubmitSel,
				"SavecButton", null, null);
		egu.addToolbarButton("导出化验值", GridButton.ButtonType_SubmitSel,
				"SavedButton", null, null);
		// 保存判断
//		String sqll = " select id from xitxxb x where x.mingc='阳城入厂化验保存'";
//		ResultSetList rsll = con.getResultSetList(sqll);
//		if (rsll.getRows() >= 1) {
//
//		}
//		GridButton baocun = new GridButton("保存", getfunction("SavebButton",
//				sign_S));
//		baocun.setIcon(SysConstant.Btn_Icon_SelSubmit);
//		egu.addTbarBtn(baocun);
		// egu.addToolbarButton(GridButton.ButtonType_Save,
		// getfunction("SavebButton"));
//		GridButton Huit = new GridButton("清空",
//				getfunction("HuitButton", sign_S));
//		Huit.setIcon(SysConstant.Btn_Icon_SelSubmit);
//		egu.addTbarBtn(Huit);
//		GridButton zuofei = new GridButton("作废", getfunction("ZuofButton",
//				sign_S));
//		zuofei.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		
	egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
			+ "row = irow; \n"
			+ "if('HUAYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
			+"if(!win){"+treepanel+Strtmpfunction+"}"
			+"win.show(this);}});\n");
//		egu.addTbarBtn(zuofei);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		// 验证是否隐藏 化验类别
		String sqltmp = "select * from xitxxb where mingc='化验值录入显示样品类别' and zhi='隐藏'";
		ResultSetList rsl2 = con.getResultSetList(sqltmp);
		if (rsl2.next()) {
			egu.getColumn("huaylb").setHidden(true);
		}

		egu.addTbarText("-");
		egu.addTbarText("当前登录人:"+visit.getRenymc());
//		egu
//				.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;});");


		setExtGrid(egu);
		rsl2.close();
		con.Close();
	}

	public String getStr(int col) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Str = "";
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		Str = "specialkey:function(own,e){\n" + "			if(row>0){\n"
				+ "				if(e.getKey()==e.UP){\n"
				+ "					gridDiv_grid.startEditing(row-1, " + col + ");\n"
				+ "					row = row-1;\n" + "				}\n" + "			}\n"
				+ "			if(row+1 < gridDiv_grid.getStore().getCount()){\n"
				+ "				if(e.getKey()==e.DOWN){\n"
				+ "					gridDiv_grid.startEditing(row+1, " + col + ");\n"
				+ "					row = row+1;\n" + "				}\n" + "			}\n";
		if (editable_S) {
			Str += "		if(e.getKey()==e.LEFT){\n" + "			if(" + col + ">5&&"
					+ col + "<=10){\n" + "				gridDiv_grid.startEditing(row, "
					+ col + "-1);\n" + "			} else if (" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ "			} else if (" + col + "==17){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n" + "			}\n"
					+ "		}\n" + "		if(e.getKey()==e.RIGHT){\n" + "			if(" + col
					+ ">=5&&" + col + "<10){\n"
					+ "				gridDiv_grid.startEditing(row, " + col + "+1);\n"
					+ "			} else if (" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n"
					+ "			} else if (" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 17);\n" + "			}\n"
					+ "		}\n" + "	  }\n";
		} else {
			Str += "		if(e.getKey()==e.LEFT){\n" + "			if(" + col + ">5&&"
					+ col + "<=8){\n" + "				gridDiv_grid.startEditing(row, "
					+ col + "-1);\n" + "			} else if(" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 8);\n"
					+ " 			} else if(" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ " 			} else if(" + col + "==17){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n" + "			}\n"
					+ "		}\n" + "		if(e.getKey()==e.RIGHT){\n" + "			if(" + col
					+ ">=5&&" + col + "<8){\n"
					+ "				gridDiv_grid.startEditing(row, " + col + "+1);\n"
					+ "			} else if(" + col + "==8){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ "	 		} else if(" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n"
					+ "	 		} else if(" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 17);\n" + "			}\n"
					+ "		}\n" + "	  }\n";
		}
		return Str;
	}

	public String getfunction(String binder, String s) {
		String handler = "function(){\n"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+ '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' + Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ '<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"
				+ s.toUpperCase()
				+ " update=\"true\">' + Mrcd[i].get('"
				+ s.toUpperCase()
				+ "')+ '</"
				+ s.toUpperCase()
				+ ">'+ '<QBAD update=\"true\">' + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<T1 update=\"true\">' + Mrcd[i].get('T1')+ '</T1>'+ '<T2 update=\"true\">' + Mrcd[i].get('T2')+ '</T2>'+ '<T3 update=\"true\">' + Mrcd[i].get('T3')+ '</T3>'+ '<T4 update=\"true\">' + Mrcd[i].get('T4')+ '</T4>'+  '<LURY update=\"true\">' + Mrcd[i].get('LURY').replace('<','&lt;').replace('>','&gt;')+ '</LURY>'+ '<BEIZ update=\"true\">' +'<HUAYY update=\"true)\">' + Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+ Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>'+ '<HUAYLB update=\"true\">' + Mrcd[i].get('HUAYLB').replace('<','&lt;').replace('>','&gt;')+ '</HUAYLB>' + '</result>' ; \n"
				+ "}\n"//
				+ "if(gridDiv_history==''){ \n"
				+ "Ext.MessageBox.alert('提示信息','没有选择数据信息');\n"
				+ "}else{\n"
				+ "    var Cobj = document.getElementById('CHANGE');\n"
				+ "    Cobj.value = '<result>'+gridDiv_history+'</result>';\n"
				+ "    document.getElementById('"
				+ binder
				+ "').click();\n"
				+ "    Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"
				+ "}\n" + "}";
		return handler;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}

	private void Zuof() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
	}

	private int Judgment(String value) {
		int v = 0;
		if (value.equals(null) || value.equals("")) {
			v = 0;
		} else {
			v = Integer.parseInt(value);
		}
		return v;
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString7();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString7(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		导航栏树的查询SQL
		String sql=
			"select 0 id,'根' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='化验' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		// 是否为阳城发电
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电", visit
				.getDiancxxb_id()
				+ "", "否"));
		// 氢是否可编辑
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// 氢元素符号
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// 硫元素符号
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}

		// 判断登陆人员是否就是化验员,如果是,则自动把huayy字段存入登陆人员姓名
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '入厂化验录入默认登录人员就是化验员' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
			zhillsbid = Long.parseLong(mdrsl.getString("ID"));
			double H = 0;
			if (!isYc) {
				H = mdrsl.getDouble(sign_H);
			}
			double S = mdrsl.getDouble(sign_S);
			double Mt = mdrsl.getDouble("mt");
			double Mad = mdrsl.getDouble("mad");
			double Aad = mdrsl.getDouble("aad");
			double Vad = mdrsl.getDouble("vad");
			double Qbad = mdrsl.getDouble("qbad");
			double Vdaf = 0;
			boolean failed = false;

			/*
			 * 作者：ww 时间：2009-09-18 描述：分厂别，同矿的供应商氢值不同
			 */
			boolean isNotValue = "是".equals(MainGlobal.getXitxx_item("化验",
					"分厂同矿氢值不同", visit.getDiancxxb_id() + "", "否"));
			boolean blnFencb = false;
			blnFencb = ((Visit) this.getPage().getVisit()).isFencb();
			if (isNotValue && blnFencb) {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"), blnFencb, getTreeid());
						if (H == -1) {
							msg += "编码:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"),
							blnFencb, getTreeid());
					if (S == -1) {
						msg += "编码:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
						failed = true;
					}
				}

			} else {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"));
						if (H == -1) {
							msg += "编码:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"));
					if (S == -1) {
						msg += "编码:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
						failed = true;
					}
				}
			}
			// if(mdrsl.getString("leix").equals("入厂")){
			//				
			// }
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			if ("入炉".equals(mdrsl.getString("huaylb"))) {
				//查询所有和本条数据相同的caib_id 然后更新所有rulmzmxb
				sql.append("update rulmzlzmxb set ");
				sql.append("fenxrq").append("=").append(
						"to_date('" + mdrsl.getString("HUAYSJ")
								+ "','YYYY-MM-DD hh24:mi:ss'),").append("");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
						sign_H, H, sign_S, S, sacle));

			}
//			else if("其他样".equals(mdrsl.getString("huaylb"))){
//				
//				sql.append("update QITYMXB set ");
//				sql.append("fenxrq").append("=").append(
//						"to_date('" + mdrsl.getString("HUAYSJ")
//								+ "','YYYY-MM-DD hh24:mi:ss'),").append(" shenhzt=4,");
//				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
//						sign_H, H, sign_S, S, sacle));
//			} 
			else {// 入厂及其它样

				sql.append("update zhillsb set ");
				sql.append("huaysj").append("=").append(
						"to_date('" + mdrsl.getString("Huaysj")
								+ "','YYYY-MM-DD hh24:mi:ss'),").append("shenhzt=6, ");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
						sign_H, H, sign_S, S, sacle));
				sql.append("t1").append(
						"=" + Judgment(mdrsl.getString("t1")) + ",");
				sql.append("t2").append(
						"=" + Judgment(mdrsl.getString("t2")) + ",");
				sql.append("t3").append(
						"=" + Judgment(mdrsl.getString("t3")) + ",");
				sql.append("t4").append(
						"=" + Judgment(mdrsl.getString("t4")) + ",");
			}
			// sql.append("update zhillsb set ");
			// sql.append("huaysj").append("=").append(
			// "to_date('" + mdrsl.getString("Huaysj")
			// + "','yyyy-mm-dd'),");
			// sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, sign_H,
			// H,
			// sign_S, S, sacle));
			if (!failed) {
//				sql.append("shenhzt").append("=0,");
			}
			// 王总兵,判断化验值录入时,登陆人员是否就是化验员
			if (xiansztq) {
				sql.append("huayy").append("='" + visit.getRenymc() + "',");
			} else {
				sql.append("huayy").append(
						"='" + mdrsl.getString("huayy") + "',");
			}
//			String sql_caiy=" select * from rulmzlzmxb where id="+mdrsl.getString("ID");
//			ResultSetList rsl_caiy = con.getResultSetList(sql_caiy);
			String caiyb_id=mdrsl.getString("id");
//			 boolean flag=false;
//			while(rsl_caiy.next()){
//				caiyb_id=rsl_caiy.getString("zhilb_id");
//				flag=true;
//			}
//			sql.append("lury").append("='" + visit.getRenymc() + "',");
			sql.append("beiz").append("='" + mdrsl.getString("beiz") + "' ");
			if("入炉".equals(mdrsl.getString("huaylb"))){
				
				sql.append("where zhuanmbzllsb_id =").append(caiyb_id).append("\n");//id
			}else{
				
				sql.append("where id =").append(mdrsl.getString("ID")).append("\n");//id
				
			}
			con.getUpdate(sql.toString());
			//查出zhillsb的数据
			
			String sqlz=
				"select * from zhillsb z where z.id=" +mdrsl.getString("ID")
				;
			ResultSetList rsllz=con.getResultSetList(sqlz);
			
			while(rsllz.next()){
				
				String sqll=
					"select f.gongysb_id from fahb f\n" +
					"where f.zhilb_id="+rsllz.getString("zhilb_id")+" and\n" + 
					"f.gongysb_id in\n" + 
					"( select zhi from xitxxb\n" + 
					" where  mingc ='入厂三级审核供应商'\n" + 
					" and leib='化验审核'\n" + 
					" and zhuangt=1 and beiz='使用'\n" + 
					"  )";
//				ResultSetList rsll=con.getResultSetList(sqll);
//				while(rsll.next()){
//					String strsql="begin \n";
//					strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
//					",had,vad,fcad,std,qgrad,qgrd,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
//					+"values("+rsllz.getString("zhilb_id")+",'"+mdrsl.getString("huaybh")+"',"+"3,"
//					+ DateUtil.FormatOracleDate(rsllz.getDateString("huaysj"))+","+rsllz.getString("qnet_ar") + ","//DateUtil.FormatOracleDate()
//					+ rsllz.getString("aar") + "," + rsllz.getString("ad") + "," + rsllz.getString("vdaf") + ","
//					+ rsllz.getString("mt") + "," + rsllz.getString("stad") + "," + rsllz.getString("aad") + ","
//					+ rsllz.getString("mad") + "," + rsllz.getString("qbad") + "," + rsllz.getString("had") + ","
//					+ rsllz.getString("vad") + "," + rsllz.getString("fcad") + "," + rsllz.getString("std") + ","
//					+ rsllz.getString("qgrad") + ","+rsllz.getString("qgrd")+"," + rsllz.getString("hdaf") + "," + rsllz.getString("qgrad_daf") + ","
//					+ rsllz.getString("sdaf") + ",";
//					if (rsllz.getString("t1")==null ||rsllz.getString("t1").equals("")){
//						strsql=strsql+"0,";
//					}else{
//						strsql=strsql+rsllz.getString("t1")+",";
//					}
//					
//					if (rsllz.getString("t2")==null ||rsllz.getString("t2").equals("")){
//						strsql=strsql+"0,";
//					}else{
//						strsql=strsql+rsllz.getString("t2")+",";
//					}
//					if (rsllz.getString("t3")==null ||rsllz.getString("t3").equals("")){
//						strsql=strsql+"0,";
//					}else{
//						strsql=strsql+rsllz.getString("t3")+",";
//					}
//					if (rsllz.getString("t4")==null ||rsllz.getString("t4").equals("")){
//						strsql=strsql+"0,";
//					}else{
//						strsql=strsql+rsllz.getString("t4")+",";
//					}
//					
//					strsql=strsql+"'" + rsllz.getString("huayy") + "','"
//					+ rsllz.getString("LURY") + "','" + rsllz.getString("beiz") + "',1,1);\n";
//					strsql += " end;";
//					con.getInsert(strsql);
//			}
			}
//			zhilbid = mdrsl.getString("ID");
			sql.delete(0, sql.length());
//			strsql="";
		}
		setMsg(msg);
		con.Close();
	}

	public void Save2(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		String tableName = "zhillsb";
		String huaysj = "huaysj";
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (mdrsl.getString("huaylb").equals("入炉")) {
				tableName = "rulmzlzmxb";
				huaysj = "rulrq";
			}
//			if (mdrsl.getString("huaylb").equals("其他样")) {
//				tableName = "QITYMXB";
//				huaysj = "rulrq";
//			}
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append(huaysj).append("= null,");
			sql.append("qnet_ar").append("=0,");
			sql.append("aar").append("=0,");
			sql.append("ad").append("=0,");
			sql.append("vdaf").append("=0,");
			sql.append("mt").append("=0,");
			sql.append("stad").append("=0,");
			sql.append("aad").append("=0,");
			sql.append("mad").append("=0,");
			sql.append("qbad").append("=0,");
			sql.append("had").append("=0,");
			sql.append("vad").append("=0,");
			sql.append("fcad").append("=0,");
			sql.append("std").append("=0,");
			sql.append("qgrad").append("=0,");
			sql.append("hdaf").append("=0,");
			sql.append("qgrad_daf").append("=0,");
			sql.append("sdaf").append("=0,");
			if (mdrsl.getString("huaylb").equals("入炉")) {

				// 没有t1-t4
			} else {// /入厂以及其它样
				sql.append("t1").append("=0,");
				sql.append("t2").append("=0,");
				sql.append("t3").append("=0,");
				sql.append("t4").append("=0,");
			}
			sql.append("shenhzt").append("=0,");
			sql.append("huayy").append("=null,");
			sql.append("lury").append("=null,");
			sql.append("beiz").append("=null ");
			if(mdrsl.getString("huaylb").equals("入炉")){
				sql.append("where zhuanmbzllsb_id =").append(mdrsl.getString("id"))
				.append(";\n");

				
			}else{
				
				sql.append("where id =").append(mdrsl.getString("ID"))
				.append(";\n");
			}
		}
		sql.append(" end;");
		
		con.getUpdate(sql.toString());
	}

	public void Save3(String strchange, Visit visit) {
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (mdrsl.getString("huaylb").equals("入炉")) {
				tableName = "rulmzlzmxb";
			}
//			if (mdrsl.getString("huaylb").equals("其他样")) {
//				tableName = "QITYMXB";
//			}
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append("=-1 ");
			if(mdrsl.getString("huaylb").equals("入炉")){
				sql.append("where zhuanmbzllsb_id =").append(mdrsl.getString("id"))
				.append(";\n");

				
			}else{
				
				sql.append("where id =").append(mdrsl.getString("ID"))
				.append(";\n");
			}
//			sql.append("where id =").append(mdrsl.getString("ID"))
//					.append(";\n");
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ZuofChick = false;

	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}

	private boolean _SavebChick = false;

	public void SavebButton(IRequestCycle cycle) {
		_SavebChick = true;
	}

	private boolean _SavecChick = false;

	public void SavecButton(IRequestCycle cycle) {
		_SavecChick = true;
	}
	private boolean _SavedChick = false;

	public void SavedButton(IRequestCycle cycle) {
		_SavedChick = true;
	}
	
	public void Saveb() {

		Visit visit = (Visit) this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
		// 是否为阳城发电
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电", visit
				.getDiancxxb_id()
				+ "", "否"));
		// 氢是否可编辑
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// 氢元素符号
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// 硫元素符号
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}

		// 判断登陆人员是否就是化验员,如果是,则自动把huayy字段存入登陆人员姓名
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '入厂化验录入默认登录人员就是化验员' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
			zhillsbid = Long.parseLong(mdrsl.getString("ID"));
			double H = 0;
			if (!isYc) {
				H = mdrsl.getDouble(sign_H);
			}
			double S = mdrsl.getDouble(sign_S);
			double Mt = mdrsl.getDouble("mt");
			double Mad = mdrsl.getDouble("mad");
			double Aad = mdrsl.getDouble("aad");
			double Vad = mdrsl.getDouble("vad");
			double Qbad = mdrsl.getDouble("qbad");
			double Vdaf = 0;
			boolean failed = false;

			/*
			 * 作者：ww 时间：2009-09-18 描述：分厂别，同矿的供应商氢值不同
			 */
			boolean isNotValue = "是".equals(MainGlobal.getXitxx_item("化验",
					"分厂同矿氢值不同", visit.getDiancxxb_id() + "", "否"));
			boolean blnFencb = false;
			blnFencb = ((Visit) this.getPage().getVisit()).isFencb();
			if (isNotValue && blnFencb) {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"), blnFencb, getTreeid());
						if (H == -1) {
							msg += "编码:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"),
							blnFencb, getTreeid());
					if (S == -1) {
						msg += "编码:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
						failed = true;
					}
				}

			} else {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"));
						if (H == -1) {
							msg += "编码:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"));
					if (S == -1) {
						msg += "编码:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
						failed = true;
					}
				}
			}
			// if(mdrsl.getString("leix").equals("入厂")){
			//				
			// }
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			if ("入炉".equals(mdrsl.getString("huaylb"))) {
				//查询所有和本条数据相同的caib_id 然后更新所有rulmzmxb
				sql.append("update rulmzlzmxb set ");
				sql.append("fenxrq").append("=").append(
						"to_date('" + mdrsl.getString("HUAYSJ")
								+ "','YYYY-MM-DD hh24:mi:ss'),").append("");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
						sign_H, H, sign_S, S, sacle));

			}
//			else if("其他样".equals(mdrsl.getString("huaylb"))){
//				
//				sql.append("update QITYMXB set ");
//				sql.append("fenxrq").append("=").append(
//						"to_date('" + mdrsl.getString("HUAYSJ")
//								+ "','YYYY-MM-DD hh24:mi:ss'),").append(" shenhzt=4,");
//				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
//						sign_H, H, sign_S, S, sacle));
//			} 
			else {// 入厂及其它样

				sql.append("update zhillsb set ");
				sql.append("huaysj").append("=").append(
						"to_date('" + mdrsl.getString("Huaysj")
								+ "','YYYY-MM-DD hh24:mi:ss'),").append("shenhzt=7, ");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
						sign_H, H, sign_S, S, sacle));
				sql.append("t1").append(
						"=" + Judgment(mdrsl.getString("t1")) + ",");
				sql.append("t2").append(
						"=" + Judgment(mdrsl.getString("t2")) + ",");
				sql.append("t3").append(
						"=" + Judgment(mdrsl.getString("t3")) + ",");
				sql.append("t4").append(
						"=" + Judgment(mdrsl.getString("t4")) + ",");
			}
			// sql.append("update zhillsb set ");
			// sql.append("huaysj").append("=").append(
			// "to_date('" + mdrsl.getString("Huaysj")
			// + "','yyyy-mm-dd'),");
			// sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, sign_H,
			// H,
			// sign_S, S, sacle));
			if (!failed) {
//				sql.append("shenhzt").append("=0,");
			}
			// 王总兵,判断化验值录入时,登陆人员是否就是化验员
			if (xiansztq) {
				sql.append("huayy").append("='" + visit.getRenymc() + "',");
			} else {
				sql.append("huayy").append(
						"='" + mdrsl.getString("huayy") + "',");
			}
//			String sql_caiy=" select * from rulmzlzmxb where id="+mdrsl.getString("ID");
//			ResultSetList rsl_caiy = con.getResultSetList(sql_caiy);
			String caiyb_id=mdrsl.getString("id");
//			 boolean flag=false;
//			while(rsl_caiy.next()){
//				caiyb_id=rsl_caiy.getString("zhilb_id");
//				flag=true;
//			}
//			sql.append("lury").append("='" + visit.getRenymc() + "',");
			sql.append("beiz").append("='" + mdrsl.getString("beiz") + "' ");
			if("入炉".equals(mdrsl.getString("huaylb"))){
				
				sql.append("where zhuanmbzllsb_id =").append(caiyb_id).append("\n");//id
			}else{
				
				sql.append("where id =").append(mdrsl.getString("ID")).append("\n");//id
				
			}
			con.getUpdate(sql.toString());
			//查出zhillsb的数据
			
			String sqlz=
				"select * from zhillsb z where z.id=" +mdrsl.getString("ID")
				;
			ResultSetList rsllz=con.getResultSetList(sqlz);
			
			while(rsllz.next()){
				
//				String sqll=
//					"select f.gongysb_id from fahb f\n" +
//					"where f.zhilb_id="+rsllz.getString("zhilb_id")+" and\n" + 
//					"f.gongysb_id in\n" + 
//					"( select zhi from xitxxb\n" + 
//					" where  mingc ='入厂三级审核供应商'\n" + 
//					" and leib='化验审核'\n" + 
//					" and zhuangt=1 and beiz='使用'\n" + 
//					"  )";
//				ResultSetList rsll=con.getResultSetList(sqll);
//				while(rsll.next()){
					String strsql="begin \n";
					strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
					",had,vad,fcad,std,qgrad,qgrd,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
					+"values("+rsllz.getString("zhilb_id")+",'"+mdrsl.getString("huaybh")+"',"+"3,"
					+ DateUtil.FormatOracleDate(rsllz.getDateString("huaysj"))+","+rsllz.getString("qnet_ar") + ","//DateUtil.FormatOracleDate()
					+ rsllz.getString("aar") + "," + rsllz.getString("ad") + "," + rsllz.getString("vdaf") + ","
					+ rsllz.getString("mt") + "," + rsllz.getString("stad") + "," + rsllz.getString("aad") + ","
					+ rsllz.getString("mad") + "," + rsllz.getString("qbad") + "," + rsllz.getString("had") + ","
					+ rsllz.getString("vad") + "," + rsllz.getString("fcad") + "," + rsllz.getString("std") + ","
					+ rsllz.getString("qgrad") + ","+rsllz.getString("qgrd")+"," + rsllz.getString("hdaf") + "," + rsllz.getString("qgrad_daf") + ","
					+ rsllz.getString("sdaf") + ",";
					if (rsllz.getString("t1")==null ||rsllz.getString("t1").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t1")+",";
					}
					
					if (rsllz.getString("t2")==null ||rsllz.getString("t2").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t2")+",";
					}
					if (rsllz.getString("t3")==null ||rsllz.getString("t3").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t3")+",";
					}
					if (rsllz.getString("t4")==null ||rsllz.getString("t4").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t4")+",";
					}
					
					strsql=strsql+"'" + rsllz.getString("huayy") + "','"
					+ rsllz.getString("LURY") + "','" + rsllz.getString("beiz") + "',1,1);\n";
					strsql += " end;";
					con.getInsert(strsql);
			}
			}
//			zhilbid = mdrsl.getString("ID");
			sql.delete(0, sql.length());
//			strsql="";
//		}
		setMsg(msg);
		con.Close();
	
	}

	public void Savec(IRequestCycle cycle){



		Visit visit = (Visit) this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
		// 是否为阳城发电
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电", visit
				.getDiancxxb_id()
				+ "", "否"));
		// 氢是否可编辑
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// 氢元素符号
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// 硫元素符号
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}

		// 判断登陆人员是否就是化验员,如果是,则自动把huayy字段存入登陆人员姓名
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '入厂化验录入默认登录人员就是化验员' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
			visit.setString9(mdrsl.getString("ID"));
		}
//			zhilbid = mdrsl.getString("ID");
//			sql.delete(0, sql.length());
//			strsql="";
//		}
//		setMsg(msg);
		con.Close();
		cycle.activate("Fahxxcx_zh");
	
	
	
		
	}
	
	public void Saved(IRequestCycle cycle){


		Visit visit = (Visit) this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
		// 是否为阳城发电
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电", visit
				.getDiancxxb_id()
				+ "", "否"));
		// 氢是否可编辑
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// 氢元素符号
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// 硫元素符号
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}

		// 判断登陆人员是否就是化验员,如果是,则自动把huayy字段存入登陆人员姓名
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '入厂化验录入默认登录人员就是化验员' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
			visit.setString8(mdrsl.getString("ID"));
		}
//			zhilbid = mdrsl.getString("ID");
//			sql.delete(0, sql.length());
//			strsql="";
//		}
//		setMsg(msg);
		con.Close();
		cycle.activate("Ruchycx_zh");
	
	
	}
	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (_SavebChick) {
			_SavebChick = false;
			Saveb();

			getSelectData();
		}
		//查看发货信息
		if (_SavecChick) {
			_SavecChick = false;

			Savec(cycle);
			
		}
		//导出入厂化验报告单
		if (_SavedChick) {
			_SavedChick = false;
			
			Saved(cycle);
			
		}

		if (_SaveChick) {
			_SaveChick = false;
			Save();

			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;

		}
		if (_ZuofChick) {
			_ZuofChick = false;
			Zuof();

		}

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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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

	private String treeid = "";

	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String tbars;

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
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
			setExtGrid(null);
			visit.setString7(null);
			visit.setString6(DateUtil.FormatDate(new Date()));
			riq1 = null;
//			riqchange1 = false;
			riq2 = null;

		}
		initNavigation();
		getSelectData();
	}

	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null
				|| ((Visit) this.getPage().getVisit()).getString5().equals("")) {

			((Visit) this.getPage().getVisit()).setString5(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null
				&& !((Visit) this.getPage().getVisit()).getString5().equals(
						riq1)) {

			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}
	
	// 绑定日期
//	boolean riqchange1 = false;

	private String riq1;

	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}

	public void setRiq1(String riq1) {

		if (this.riq1 != null && !this.riq1.equals(riq1)) {
			this.riq1 = riq1;
//			riqchange1 = true;
		}

	}
	
//	private boolean riqchange2 = false;//结束日期是否变化
	
	private String riq2;
	public String getRiq2(){
		if(riq2 == null || riq2.equals("")){
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq2){
		if(this.riq2 != null && !this.riq2.equals(riq2)){
			this.riq2 = riq2;
//			riqchange2 = true;
		}
	}

	// 制样日期绑定,宣威电厂用

	public void setZhiyrq(String zhiyrq) {
		if (((Visit) this.getPage().getVisit()).getString6() != null
				&& !((Visit) this.getPage().getVisit()).getString6().equals(
						zhiyrq)) {

			((Visit) this.getPage().getVisit()).setString6(zhiyrq);
		}
	}

	public String getZhiyrq() {
		if (((Visit) this.getPage().getVisit()).getString6() == null
				|| ((Visit) this.getPage().getVisit()).getString6().equals("")) {

			((Visit) this.getPage().getVisit()).setString6(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}
}