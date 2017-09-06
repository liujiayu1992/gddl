package com.zhiren.dc.zhuangh.ruchysh;

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
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*


/*作者:王总兵
 *日期:2010-3-24
 *描述:化验一级审核增加干基高位热值(Qgr,d)
 * 
 * 
 * 
 */



 /*wzb
 * 2009-7-14
 * 一级审核页面增加参数"化验一级审核是否只显示化验的主要指标"
 * 
 * 
 * 
 * 
 */

/*
 * 修改人：ww
 * 修改时间：2009-09-18
 * 
 * 修改内容：
 * 	   1.化验一审热值以卡的形式显示
		INSERT INTO xitxxb VALUES(
				(SELECT MAX(ID)+1 AS ID FROM xitxxb),
				1,
				301,
				'一级审核热值以卡显示',
				'是',
				'',
				'化验',
				1,
				'使用'
				)
	   2.化验一审回退增加日志记录
		INSERT INTO xitxxb VALUES(
			(SELECT MAX(ID)+1 AS ID FROM xitxxb),
			1,
			301,
			'化验一审回退日志',
			'是',
			'',
			'化验',
			1,
			'使用'
			)
	   3.化验一级审核是否显示运输单位,默认不审核
	   4.加载页面时初始化setTreeid()方法，避免一厂多制不同厂别同时登录造成数据混乱
 */


//2009-10-19  王总兵  注释掉此句话,否则不显示运输单位的厂会报错
/*if (!ShowHuaylb) {
	egu.getColumn("yunsdw").setHidden(true);
}*/

/*
 * 修改人：ww
 * 修改时间：2009-11-20
 * 修改内容：
 * 		   修改“一级审核显示矿”参数配置时完全不显示矿名，以前是可以在列中点击复选框显示的
 * 		  一级审核中添加可配置grid关键字，“Ruchyyjsh”，配置要显示的列
 * 	      回退时添加了日志记录
 */
/*
 * 作者：夏峥
 * 时间：2014-03-10
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		调整程序存在的BUG，清除无用注释。
 */
/*
 * 作者：夏峥
 * 时间：2014-04-1
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		即2日入炉煤量为1日早9点至2日早9点的入炉煤量
 */
public class Ruchyejht_zh extends BasePage implements PageValidateListener {
	
//	private String CustomSetKey = "Ruchyyjsh";
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	private boolean xiansztl = false;

	private boolean xiansztq = false;

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		sql = "select zhi from xitxxb where mingc = '是否显示入厂化验硫' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztl = true;
			} else {
				xiansztl = false;
			}
		}

		sql = "select zhi from xitxxb where mingc = '是否显示入厂化验氢' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		boolean Display = false;
		sql = "select zhi from xitxxb where mingc = '一级审核显示矿'";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("显示")) {
				Display = true;
			}
		}
		
		//化验一级审核是否显示化验类别,默认为显示
		boolean ShowHuaylb=true;
		sql = "select zhi from xitxxb where mingc = '化验一级审核是否显示化验类别'  and zhuangt = 1  ";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				ShowHuaylb = true;
			} else {
				ShowHuaylb = false;
			}
		}
		
//		化验一级审核是否只显示化验的原始指标(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),默认为全部显示
		boolean IsShow=false;
		sql = "select zhi from xitxxb where mingc = '化验一级审核是否只显示化验的主要指标'  and zhuangt = 1  ";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				IsShow = true;
			} else {
				IsShow = false;
			}
		}
		
		//化验一级审核是否显示运输单位
		boolean ShowYunsdw = false;
		sql = "select zhi from xitxxb where mingc = '一级审核显示运输单位' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("显示")) {
				ShowYunsdw = true;
			} else {
				ShowYunsdw = false;
			}
		}

		boolean ShowCal = false;
		sql = "select zhi from xitxxb where mingc = '一级审核热值以卡显示' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				ShowCal = true;
			} else {
				ShowCal = false;
			}
		}
		
		rsl.close();
		
//		电厂Tree刷新条件
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
				ResultSet rsss=con.getResultSet("select id from diancxxb where fuid="+getTreeid());
				if(rsss.next()){
					str = "and dc.fuid="+ getTreeid() ;
				}else{
					str = "and dc.id = " + getTreeid() ;
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		if (ShowYunsdw) {
			if (Display) {
				sql = "SELECT   l.ID, m.mingc meikdw, yd.mingc yunsdw,f.jingz AS shul, p.mingc AS pinz,\n";
			} else {
				sql = "SELECT   l.ID,l.zhilb_id,\n";
			}
			sql+= "         TO_CHAR (l.huaysj, 'yyyy-mm-dd') AS huaysj,\n"
				+ "         TO_CHAR (z.bianm) AS huaybh, c.id caiyb_id," + (ShowCal?"round_new(l.qnet_ar*1000/4.1816,0) qnet_ar_cal,":"") + "l.qnet_ar, l.aar, l.ad, l.vdaf, l.mt,\n"
				+ "         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n"
				+ "         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, l.t1, l.t2, l.t3, l.t4, huayy, l.lury,\n"
				+ "         l.beiz, l.huaylb\n"
				+ "    FROM zhuanmb z,\n"
				+ "         zhillsb l,\n"
				+ "         caiyb c,\n"
				+ "         (SELECT DISTINCT fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id,\n"
				+ "                          SUM (cp.maoz-cp.piz-cp.zongkd) jingz,cp.yunsdwb_id\n"
				+ "                     FROM fahb fh,diancxxb dc,chepb cp\n"
				+ "						WHERE fh.diancxxb_id = dc.id AND cp.fahb_id=fh.id\n"
				+ 							str + "\n"
				+ "                 GROUP BY fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id,cp.yunsdwb_id) f,\n"
				+ "         meikxxb m,\n"
				+ "         pinzb p,\n"
				+ "         yunsdwb yd\n"
				+ "   WHERE z.zhillsb_id = l.ID\n"
				+ "     AND f.zhilb_id = c.zhilb_id\n"
				+ "     AND c.zhilb_id = l.zhilb_id\n"
				+ "     AND f.meikxxb_id = m.ID\n"
				+ "     AND f.pinzb_id = p.ID\n"
				+ "     AND f.yunsdwb_id = yd.id(+)\n"
				+ "     AND z.zhuanmlb_id = (SELECT ID\n"
				+ "                            FROM zhuanmlb\n"
				+ "                           WHERE jib = (SELECT NVL (MAX (jib), 0)\n"
				+ "                                          FROM zhuanmlb))\n"
				+ "     AND (shenhzt = 5 )\n"//OR shenhzt = 4
				+ "ORDER BY z.ID, z.bianm, l.huaylb";

		}
		else {
			if (Display) {
				sql = "SELECT   l.ID, m.mingc meikdw, f.jingz AS shul, p.mingc AS pinz,\n";
			} else {
				sql = "SELECT   l.ID,l.zhilb_id,\n";
			}

			sql +=	"         TO_CHAR (l.huaysj, 'yyyy-mm-dd') AS huaysj,\n"
				+ "         TO_CHAR (z.bianm) AS huaybh,1 as caiyb_id," + (ShowCal?"round_new(l.qnet_ar*1000/4.1816,0) qnet_ar_cal,":"") + " l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n"
				+ "         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n"
				+ "         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, l.t1, l.t2, l.t3, l.t4, huayy, l.lury,\n"
				+ "         l.beiz, l.huaylb\n"
				+ "    FROM zhuanmb z,\n"
				+ "         zhillsb l,\n"
				+ "         caiyb c,\n"
				+ "         (SELECT DISTINCT fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id,\n"
				+ "                          SUM (fh.jingz) jingz\n"
				+ "                     FROM fahb fh,diancxxb dc\n"
				+ "						WHERE fh.diancxxb_id = dc.id\n"
				+ 							str + "\n"
				+ "                 GROUP BY fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id) f,\n"
				+ "         meikxxb m,\n"
				+ "         pinzb p\n"
				+ "   WHERE z.zhillsb_id = l.ID\n"
				+ "     AND f.zhilb_id = c.zhilb_id\n"
				+ "     AND c.zhilb_id = l.zhilb_id\n"
				+ "     AND f.meikxxb_id = m.ID\n"
				+ "     AND f.pinzb_id = p.ID\n"
				+ "     AND z.zhuanmlb_id = (SELECT ID\n"
				+ "                            FROM zhuanmlb\n"
				+ "                           WHERE jib = (SELECT NVL (MAX (jib), 0)\n"
				+ "                                          FROM zhuanmlb))\n"
				+ "     AND (shenhzt = 5 OR shenhzt = 4)\n"//此处也应该是0
				+ "--ORDER BY z.ID, z.bianm, l.huaylb\n";
		}

		String sq=
			"\n" + 
			"(\n" + 
			"\n" + 
			"SELECT  distinct  l.zhuanmbzllsb_id as ID,l.zhuanmbzllsb_id  as zhilb_id,--z.id,\n" + 
			"         TO_CHAR (l.fenxrq, 'yyyy-mm-dd') AS huaysj,\n" + 
			"         TO_CHAR (z.bianm) AS huaybh,l.caiyb_id caiyb_id,\n" +
			" l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n" + 
			"         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n" + 
			"         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, 0 as t1,  0 as t2,  0 as t3,  0 as t4, huayy, l.lury,\n" + 
			"         l.beiz,'入炉' as  huaylb\n" + 
			"    FROM zhuanmb z,\n" + 
			"         rulmzlzmxb l\n" + 
			"       \n" + 
			"\n" + 
			"   WHERE z.zhillsb_id = l.zhuanmbzllsb_id\n" + 

			"AND z.zhuanmlb_id in (SELECT ID\n" +
			"                     FROM zhuanmlb\n" + 
			"                    WHERE jib = (SELECT NVL (MAX (jib), 0)\n" + 
			"                                   FROM zhuanmlb))"+
			"	\n	"+
			"   and shenhzt=7 and  TO_CHAR (l.fenxrq, 'yyyy-mm-dd')>='"+getRiq1()+"' and  TO_CHAR (l.fenxrq, 'yyyy-mm-dd')<='"+getRiq2()+"'\n" + 
			")\n"+
			"union (\n" +
			"\n" + 
			"SELECT   l.ID,l.zhilb_id as zhilb_id,\n" + 
			"         TO_CHAR (l.huaysj, 'yyyy-mm-dd') AS huaysj,\n" + 
			"         TO_CHAR (z.bianm) AS huaybh, 3 as caiyb_id,l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n" + 
			"         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n" + 
			"         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, 0 as t1,  0 as t2,  0 as t3,  0 as t4, huayy, l.lury,\n" + 
			"         l.beiz,huaylb\n" + 
			"    FROM zhuanmb z,\n" + 
			"         zhillsb l\n" + 
			"\n" + 
			"   WHERE z.zhillsb_id = l.ID\n" + 
			"    AND z.zhuanmlb_id = (SELECT ID\n" + 
			"                            FROM zhuanmlb\n" + 
			"                           WHERE jib = (SELECT NVL (MAX (jib), 0)\n" + 
			"                                          FROM zhuanmlb))\n" + 
			"   and shenhzt=7 and l.zhilb_id <> 0 and TO_CHAR (l.huaysj, 'yyyy-mm-dd')>='"+getRiq1()+"' and  TO_CHAR (l.huaysj, 'yyyy-mm-dd')<='"+getRiq2()+"'\n" + 
			"\n" + 
			")";

		rsl = con.getResultSetList(sq);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("zhillsb");
		egu.setWidth("bodyWidth");
		egu.setMokmc("化验二级回退");
		// 设置页面宽度以便增加滚动条
		egu.setWidth(1000);
		
		egu.setDefaultsortable(false);

		if (Display) {
			egu.getColumn("meikdw").setHeader("煤矿单位");
			egu.getColumn("meikdw").setEditor(null);
			egu.getColumn("meikdw").setWidth(60);
		}
		if (ShowYunsdw) {
			egu.getColumn("yunsdw").setHeader("运输单位");
			egu.getColumn("yunsdw").setEditor(null);
			egu.getColumn("yunsdw").setWidth(60);	
		}
		if (Display) {
			egu.getColumn("pinz").setHeader("品种");
			egu.getColumn("pinz").setEditor(null);
			egu.getColumn("shul").setWidth(60);
			egu.getColumn("shul").setHeader("数量(吨)");
			egu.getColumn("shul").setRenderer("shifcc");
			egu.getColumn("shul").setEditor(null);
			egu.getColumn("pinz").setWidth(60);
		}
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("caiyb_id").setHeader("采样");
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("zhilb_id").setHeader("质量");
		egu.getColumn("zhilb_id").setHidden(true);
		
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("huaylb").setHeader("化验类别");
		egu.getColumn("huaylb").setEditor(null);
		egu.getColumn("huaylb").setHidden(true);
		if(!ShowHuaylb){
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.getColumn("qnet_ar").setHeader("收到基低位热量<p>Qnet,ar(MJ/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setHidden(true);
		
		if (ShowCal) {
			egu.getColumn("qnet_ar_cal").setHeader("收到基低位热量<p>Qnet,ar(大卡)</p>");
			egu.getColumn("qnet_ar_cal").setEditor(null);
			egu.getColumn("qnet_ar_cal").setHidden(true);
		}
		egu.getColumn("aar").setHeader("收到基灰分<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("干燥基灰分<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("干燥无灰基挥发分<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("全水分<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		
		egu.getColumn("stad").setHeader("空气干燥基全硫<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("空气干燥基灰分<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(MJ/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("固定碳<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("干燥基全硫<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("空气干燥基高位热值<p>Qgr,ad(MJ/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热值<p>Qgr,daf(MJ/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("干燥无灰基全硫<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("qgrd").setHeader("干燥基高位热值<p>Qgr,d(MJ/kg)</p>");
		egu.getColumn("qgrd").setEditor(null);
		
		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t1").setEditor(null);
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t2").setEditor(null);
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t3").setEditor(null);
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("t4").setEditor(null);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		// egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("qgrd").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		
		//是否只显示化验的主要指标(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar)
		if(IsShow){
			egu.getColumn("aar").setHidden(true);
			egu.getColumn("ad").setHidden(true);
			egu.getColumn("vad").setHidden(true);
			egu.getColumn("fcad").setHidden(true);
			egu.getColumn("std").setHidden(true);
			egu.getColumn("qgrad_daf").setHidden(true);
			egu.getColumn("sdaf").setHidden(true);
			egu.getColumn("t1").setHidden(true);
			egu.getColumn("t2").setHidden(true);
			egu.getColumn("t3").setHidden(true);;
			egu.getColumn("t4").setHidden(true);
		}
		
		if(xiansztq){
			egu.getColumn("had").setHidden(true);
			egu.getColumn("hdaf").setHidden(true);
		}
		if(xiansztl){
			egu.getColumn("stad").setHidden(true);
			egu.getColumn("std").setHidden(true);
			egu.getColumn("sdaf").setHidden(true);
		}
		egu.addPaging(25);
		egu.setGridSelModel(3);
		
		egu.addTbarText("-");
		egu.addTbarText("化验日期");
		DateField df = new DateField();
		df.setReadOnly(true); 
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");//Form0
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,
				"HuitButton");

		setExtGrid(egu);
		con.Close();
	}
	
	

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}

	private void Hed() {
		// Visit visit = (Visit) this.getPage().getVisit();
		// JDBCcon con = new JDBCcon();
		// String sql = "";
		// sql = "";
	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "zhillsb";
		
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		List fhlist = new ArrayList();
		String zhilbid = "";
		String strsql = "begin \n";
		String huaylb="";
		boolean flag1=false;
		while(mdrsl.next()){
			huaylb=mdrsl.getString("huaylb");
			flag1=true;
			if (flag1&&"其他样".equals(huaylb)) {
				strsql += "update zhillsb set shenhzt=7,shenhryej='"+visit.getRenymc()+"' where id =" + mdrsl.getString("id") + ";\n";
			}
			if (flag1&&!"入炉".equals(huaylb)&&!"其他样".equals(huaylb)) {
				strsql += "update zhillsb set shenhzt=7,shenhryej='"+visit.getRenymc()+"' where zhilb_id =" + mdrsl.getString("zhilb_id") + ";\n";
				
				strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
				",had,vad,fcad,std,qgrad,qgrd,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
				+"values("+mdrsl.getString("zhilb_id")+",'"+mdrsl.getString("huaybh")+"',"+mdrsl.getString("caiyb_id")+","
				+ DateUtil.FormatOracleDate(mdrsl.getString("huaysj"))+","+mdrsl.getString("qnet_ar") + ","
				+ mdrsl.getString("aar") + "," + mdrsl.getString("ad") + "," + mdrsl.getString("vdaf") + ","
				+ mdrsl.getString("mt") + "," + mdrsl.getString("stad") + "," + mdrsl.getString("aad") + ","
				+ mdrsl.getString("mad") + "," + mdrsl.getString("qbad") + "," + mdrsl.getString("had") + ","
				+ mdrsl.getString("vad") + "," + mdrsl.getString("fcad") + "," + mdrsl.getString("std") + ","
				+ mdrsl.getString("qgrad") + ","+mdrsl.getString("qgrd")+"," + mdrsl.getString("hdaf") + "," + mdrsl.getString("qgrad_daf") + ","
				+ mdrsl.getString("sdaf") + ",";
				if (mdrsl.getString("t1")==null ||mdrsl.getString("t1").equals("")){
					strsql=strsql+"0,";
				}else{
					strsql=strsql+mdrsl.getString("t1")+",";
				}
				
				if (mdrsl.getString("t2")==null ||mdrsl.getString("t2").equals("")){
					strsql=strsql+"0,";
				}else{
					strsql=strsql+mdrsl.getString("t2")+",";
				}
				if (mdrsl.getString("t3")==null ||mdrsl.getString("t3").equals("")){
					strsql=strsql+"0,";
				}else{
					strsql=strsql+mdrsl.getString("t3")+",";
				}
				if (mdrsl.getString("t4")==null ||mdrsl.getString("t4").equals("")){
					strsql=strsql+"0,";
				}else{
					strsql=strsql+mdrsl.getString("t4")+",";
				}
				
				strsql=strsql+"'" + mdrsl.getString("huayy") + "','"
				+ mdrsl.getString("LURY") + "','" + mdrsl.getString("beiz") + "',1,1);\n";
				zhilbid = mdrsl.getString("ID");
				
				
				String Sql = "select id from fahb where zhilb_id = " + zhilbid;
				mdrsl = con.getResultSetList(Sql);
				while (mdrsl.next()) {
					String id = mdrsl.getString("id");
					Jilcz.addFahid(fhlist, id);
				}
				mdrsl.close();
				Chengbjs.CountChengb(visit.getDiancxxb_id(),fhlist,false);
				
			}
			if(flag1&&"入炉".equals(huaylb)){
				
//				String caiyid="";
				String zhillsb_id=mdrsl.getString("ID");
				String ru_sql=" select * from rulmzlzmxb where zhuanmbzllsb_id="+zhillsb_id;
				ResultSetList ru_rsl=con.getResultSetList(ru_sql);//查询当前的rulmzlzmxb 的id是我们选择的
				int count=0;
				while (ru_rsl.next()&&count<1){
					count++;
					String zhiyry=
						"select rb.id,rb.quanc from rulmzlzmxb rzb ,yangpdhb yb,zhiyryglb zb,renyxxb rb\n" +
						"where yb.zhilblsb_id=rzb.id and yb.id=zb.yangpdhb_id and zb.renyxxb_id=rb.id\n" + 
						"and rzb.id="+zhillsb_id;
					ResultSetList zy_rsl=con.getResultSetList(zhiyry);
					String zhiyy="";
					while(zy_rsl.next()){
						zhiyy=zy_rsl.getString("quanc");
					}
					
					
					String jiz="0";
//					String rulbzb_id="0";
					if("".equals(ru_rsl.getString("jizfzb_id"))||ru_rsl.getString("jizfzb_id")==null){
						ru_rsl.getString("jizfzb_id");
					}

//					long bin2=Integer.parseInt(ru_rsl.getDateTimeString("rulrq").substring(0, 10).replaceAll("-", ""));
//					Date d=new Date(bin2);
					int da=0; 
//					String caiysj=String.valueOf(d.getTime());//getChangerq();
					String shijd=MainGlobal.getXitxx_item("数量", "入厂入炉节点时间", "0", "0");
					if(Integer.parseInt(ru_rsl.getDateTimeString("rulrq").substring(11, 13))>=Integer.parseInt(shijd)){
//						caiysj=String.valueOf(d.getTime()+1);
						da=1;
					}
					//查询所有caiyb_id 相同的rulmzlzmxb，取得煤量并累加
					String caiy="select * from rulmzlzmxb where caiyb_id="+ru_rsl.getString("caiyb_id");
					ResultSetList rsl_caiy=con.getResultSetList(caiy);
					double totalmeil=0;
					boolean flag_meil=false;
					while(rsl_caiy.next()){
						//有煤量就累加
						totalmeil+=	rsl_caiy.getDouble("meil");
						flag_meil=true;
					}
					if(!flag_meil){
						//假如没有煤量
						totalmeil=	ru_rsl.getDouble("meil");
					}
					
					String ruf = ru_rsl.getString("id") + ",to_date('"+ru_rsl.getDateTimeString("rulrq").substring(0, 10)+"', 'YYYY-MM-DD')+"+da+"," 
					+"to_date('"+ru_rsl.getDateTimeString("fenxrq")+"', 'YYYY-MM-DD hh24:mi:ss'),"+visit.getDiancxxb_id()+","//1
					+ 0 + ","+jiz+","+ru_rsl.getString("qnet_ar")+","+ru_rsl.getString("aar")+","//2
					+ ru_rsl.getString("ad") + ","+ru_rsl.getString("vdaf")+","+ru_rsl.getString("mt")+","+ru_rsl.getString("stad")+","//3
					+ ru_rsl.getString("aad")+","+ru_rsl.getString("mad")+","+ru_rsl.getString("qbad")+","+ru_rsl.getString("had")+","//4
					+ru_rsl.getString("vad")+","+ru_rsl.getString("fcad")+","+ru_rsl.getString("std")+","+ru_rsl.getString("qgrad")+","//5
					+ru_rsl.getString("hdaf")+","+ru_rsl.getString("sdaf")+","+ru_rsl.getInt("var")+",'"+ru_rsl.getString("huayy")+"','"//6
					+ru_rsl.getString("beiz")+"','"+ru_rsl.getString("lury")+"',to_date('"+ru_rsl.getDateTimeString("shangmkssj")+"', 'YYYY-MM-DD hh24:mi:ss'),"+"7,'"//7
					+ru_rsl.getString("bianm")+"',"+ru_rsl.getString("har")+","+ru_rsl.getString("qgrd")+","+ru_rsl.getString("qgrad_daf")+","//8
					+totalmeil+",'"+zhiyy+"','"+visit.getRenymc()+"',"+"to_date(to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')"+",'"//9
					+visit.getRenymc()+"',"+"to_date(to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')"+",1"//10
					;//ru_rsl.getString("meil")
					//构造记录集ResultSetList zy_rsl=con.getResultSetList(zhiyry);
					//假如今天没有数据，那么就要insert，如果有那么就构造，所以要先判断有没有数据
					//增加判断13:00的语句
			
					String sql_gz=" select * from rulmzlb where rulrq= to_date('"+
					ru_rsl.getDateTimeString("rulrq").substring(0, 10)+" ','yyyy-mm-dd')+"+da+" ";//这个日期是
					ResultSetList rsl_gz=con.getResultSetList(sql_gz);
					String gouz="";
					String aa="";
					double meil=0;
					boolean flag =false;
					while(rsl_gz.next()){//今天存在而且应该是一条，对应不同的caiyb_id
						gouz= 
							"select\n" +
							"    decode(sum(meil),0,0,round_new(sum(meil*qnet_ar)/sum(meil),2)) as qnet_ar,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*aar)/sum(meil),2)) as aar,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*ad)/sum(meil),2)) as ad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*vdaf)/sum(meil),2)) as vdaf,\n" + 
							"    decode(sum(meil),0,0,round_new(sum(meil*mt)/sum(meil),2)) as mt,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*stad)/sum(meil),2)) as stad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*aad)/sum(meil),2)) as aad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*mad)/sum(meil),2)) as mad,\n" + 
							"    decode(sum(meil),0,0,round_new(sum(meil*qbad)/sum(meil),2)) as qbad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*had)/sum(meil),2)) as had,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*vad)/sum(meil),2)) as vad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*fcad)/sum(meil),2)) as fcad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*std)/sum(meil),2)) as std,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*var)/sum(meil),2)) as var,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*har)/sum(meil),2)) as har,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*qgrd)/sum(meil),2)) as qgrd,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*qgrad_daf)/sum(meil),2)) as qgrad_daf\n" + 
							" from\n" + 
							"(--煤量的\n" + 
							"      select 1 as id, "+rsl_gz.getDouble("meil")+" as meil from dual\n" + //今天原有的
							"        union\n" + 
							"      select 2 as id ,"+totalmeil+" as meil from dual\n" + //要添加的
							"\n" +  //
							"       ) fdl,\n" + 
							"(---各个指标的\n" + //今天原有的
							"select 1 as id, "+rsl_gz.getDouble("qnet_ar")+" as qnet_ar ,"+rsl_gz.getDouble("aar")+" as aar,"
							+rsl_gz.getDouble("ad")+" as ad,"+rsl_gz.getDouble("vdaf")+" as vdaf,"+rsl_gz.getDouble("mt")+
							" as mt,"+rsl_gz.getDouble("stad")+" as stad,"+rsl_gz.getDouble("aad")+" as aad,"+rsl_gz.getDouble("mad")+
							" as mad,"+rsl_gz.getDouble("qbad")+" as qbad" +
							","+rsl_gz.getDouble("had")+" as had,"+rsl_gz.getDouble("vad")+" as vad, "+rsl_gz.getDouble("fcad")+
							" as fcad,"+rsl_gz.getDouble("std")+" as std,"+rsl_gz.getDouble("qgrad")+" as qgrad,"
							+rsl_gz.getDouble("hdaf")+" as hdaf,"+rsl_gz.getDouble("sdaf")+" as sdaf ,"+rsl_gz.getDouble("var")+
							" as var,"+rsl_gz.getDouble("har")+" as har,"+rsl_gz.getDouble("qgrd")+" as qgrd,"+rsl_gz.getDouble("qgrad_daf")+
							" as qgrad_daf from dual\n" + 
							"        union\n" +  //要添加的
							"   select  2 as id,  "+ru_rsl.getDouble("qnet_ar")+" as qnet_ar ,"+ru_rsl.getDouble("aar")+" as aar,"
							+ru_rsl.getDouble("ad")+" as ad,"+ru_rsl.getDouble("vdaf")+" as vdaf,"+ru_rsl.getDouble("mt")+
							" as mt,"+ru_rsl.getDouble("stad")+" as stad,"+ru_rsl.getDouble("aad")+" as aad,"+ru_rsl.getDouble("mad")+
							" as mad,"+ru_rsl.getDouble("qbad")+" as qbad" +
							","+ru_rsl.getDouble("had")+" as had,"+ru_rsl.getDouble("vad")+" as vad, "+ru_rsl.getDouble("fcad")+
							" as fcad,"+ru_rsl.getDouble("std")+" as std,"+ru_rsl.getDouble("qgrad")+" as qgrad,"
							+ru_rsl.getDouble("hdaf")+" as hdaf,"+ru_rsl.getDouble("sdaf")+" as sdaf ,"+ru_rsl.getDouble("var")+
							" as var,"+ru_rsl.getDouble("har")+" as har,"+ru_rsl.getDouble("qgrd")+" as qgrd,"+ru_rsl.getDouble("qgrad_daf")+
							" as qgrad_daf from dual\n" + 
							"\n" + 
							"   ) mh\n" + 
							"\n" + 
							"where\n" + 
							" fdl.id=mh.id";

						flag=true;
						aa=rsl_gz.getString("id");
						meil+=rsl_gz.getDouble("meil");//已经存在的
					}
					
					if(flag){//有数据，更新
						ResultSetList rsl_g=con.getResultSetList(gouz);
						while(rsl_g.next()){
							sql.append(" update rulmzlb set qnet_ar="+rsl_g.getDouble("qnet_ar")+",aar="+rsl_g.getDouble("aar")+"," +
									" ad="+rsl_g.getDouble("ad")+", vdaf="+rsl_g.getDouble("vdaf")+", mt="+
									rsl_g.getDouble("mt")+", stad="+rsl_g.getDouble("stad")+", aad="+rsl_g.getDouble("aad")+
									", mad="+rsl_g.getDouble("mad")+", qbad="+rsl_g.getDouble("qbad")+"," +
									"had="+rsl_g.getDouble("had")+", vad="+rsl_g.getDouble("vad")+", fcad="+rsl_g.getDouble("fcad")+
									", std="+rsl_g.getDouble("std")+", qgrad="+rsl_g.getDouble("qgrad")+", hdaf="
									+rsl_g.getDouble("hdaf")+", sdaf="+rsl_g.getDouble("sdaf")+", var="+rsl_g.getDouble("var")+
									",har="+rsl_g.getDouble("har")+",qgrd="+rsl_g.getDouble("qgrd")+",qgrad_daf="+
									rsl_g.getDouble("qgrad_daf")+",meil="+(totalmeil+meil)+//meil已经存在的，totalmeil是新增加的
									" where id="+aa+";\n");
						}
						
					}else{//没有数据，那么就要insert
						
						
						sql.append("insert into rulmzlb(id, rulrq, fenxrq, diancxxb_id, rulbzb_id, jizfzb_id,"+//6
								"qnet_ar, aar, ad, vdaf, mt, stad, aad, mad, qbad,"+//9
								"had, vad, fcad, std, qgrad, hdaf, sdaf, var,huayy,beiz,lury,lursj,"+//12
								"shenhzt,bianm,har,qgrd,qgrad_daf,meil,zhiyr,shenhryyj,shenhsjyj,shenhryej ,shenhsjej,"+//11star,shenhryej ,shenhsjej
								"erjshzt"+//2
						") values (").append(ruf).append(");\n");
					}

					tableName="rulmzlzmxb";
					sql.append("update ").append(tableName).append(" set ");
					sql.append("shenhzt").append(" = ");
					sql.append("7").append(",");
					
					//增加 shenhry的记录
					sql.append(" shenhry='").append(visit.getRenymc()).append("',");
					sql.deleteCharAt(sql.length() - 1);
					sql.append(" where zhuanmbzllsb_id =").append(mdrsl.getString("zhilb_id")).append(";\n");
				}
				sql.append(" end;");
				
				int flag = 0;
				if(sql.length()>13){
					flag = con.getInsert(sql.toString());
				}
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
							+ sql);
					return;
				}
			}
		}
		
		strsql += " end;";
		
		int flag = 0;
		if(strsql.length()>13){
			flag = con.getUpdate(strsql);
		}
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ strsql);
			return;
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
		}
	}

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
		}
	}

	public void Save2(String strchange, Visit visit) {
		
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String huaylb="";
		boolean flag1=false;
		while(mdrsl.next()){
			huaylb=mdrsl.getString("huaylb");
			flag1=true;
		}
		mdrsl.beforefirst();

		if(flag1&&!"入炉".equals(huaylb)){
		while (mdrsl.next()) {
			sql.append(" delete from zhilb where id= "+mdrsl.getString("zhilb_id")).append(";");
			sql.append("update ").append(tableName).append(" set shenhzt=5,shenhryej='"+visit.getRenymc()+"' where id =").append(mdrsl.getString("id"))
			.append(";\n");
		}
		sql.append("end;");
		
		int flag = 0;
		if(sql.length()>13){
			flag = con.getUpdate(sql.toString());
		}
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sql);
			return;
		}
		}
		
		if(flag1&&"入炉".equals(huaylb)){
			 tableName = "rulmzlzmxb";
			
			 sql.setLength(0);
			 sql.append("begin \n");
			while (mdrsl.next()) {
				StringBuffer sql2 = new StringBuffer("begin\n");
				sql2.append("update ").append(tableName).append(" set ");
				sql2.append("shenhzt").append(" = ");
				sql2.append("5").append(",");
				
				sql2.append(" shenhry='").append(visit.getRenymc()).append("',");
				
				sql2.deleteCharAt(sql2.length() - 1);
				sql2.append(" where zhuanmbzllsb_id =").append(mdrsl.getString("zhilb_id")).append(
						";\n");
				sql2.append("end;\n");
				int flag3 = 0;
				if(sql2.length()>13){
					flag3 = con.getUpdate(sql2.toString());
				}
				if (flag3 == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
							+ sql2);
					return;
				}
				//更新rulmzlzmxb表的相同采样的状态，还要重新计算rulmzlb 的数据，通过加权平均
				//主要情况：1，假如数据库就一条数据那么更新rulmzlzmxb状态并且更新rulmzlb的各项指标
				//以及煤量，其他不变；2，假如更新的数据是第一条以外的，那么更新rulmzlzmxb状态，并且取到
				//所有的满足本天的rulmzlzmxb的数据并加权求平均，最后更新到数据库rulmzlb，这个加权平均是
				//按照同一caiyb_id取得煤量相加，然后再与不同指标加权平均，
				
				//第一种情况:
				String sql_mx=" select distinct rulrq from rulmzlzmxb where zhuanmbzllsb_id ="+mdrsl.getString("zhilb_id");
				ResultSetList ru_mx=con.getResultSetList(sql_mx);
				String rulrq="";
				while(ru_mx.next()){
					//取得入炉日期
					rulrq=ru_mx.getDateTimeString("rulrq");
				}

				String shijd=MainGlobal.getXitxx_item("数量", "入厂入炉节点时间", "0", "0");

//				String zhillsb_id=mdrsl.getString("zhilb_id");
//				long bin2=Integer.parseInt(rulrq.substring(0, 10).replaceAll("-", ""));
//				Date d=new Date(bin2);
				int da=0;
//				String caiysj=String.valueOf(d.getTime());//getChangerq();
				if(Integer.parseInt(rulrq.substring(11, 13))>=Integer.parseInt(shijd)){
//					caiysj=String.valueOf(d.getTime()+1);
					da=1;
				}
				//当天小于13点，那么就要昨天13点到今天13:00，当天大于13点，那么从今天13点到明天13点，
				//取到所有的区间范围值，相同的caiyb_id那么就累加煤量，然后加权平均
				String ru_sql=
					"select nvl(sum(meil),0) meil, nvl(decode(sum(nvl(meil,0)),0,0,round_new(sum(meil*b.qnet_ar)/sum(meil),2)),0) as qnet_ar,\n" +
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.aar)/sum(meil),2)),0) as aar,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.ad)/sum(meil),2)),0) as ad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.vdaf)/sum(meil),2)),0) as vdaf,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.mt)/sum(meil),2)),0) as mt,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.stad)/sum(meil),2)),0) as stad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.aad)/sum(meil),2)),0) as aad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.mad)/sum(meil),2)),0) as mad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.qbad)/sum(meil),2)),0) as qbad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.had)/sum(meil),2)),0) as had,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.vad)/sum(meil),2)),0) as vad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.fcad)/sum(meil),2)),0) as fcad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.std)/sum(meil),2)),0) as std,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.hdaf)/sum(meil),2)),0) as hdaf,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.sdaf)/sum(meil),2)),0) as sdaf,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.har)/sum(meil),2)),0) as har,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.qgrd)/sum(meil),2)),0) as qgrd,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.qgrad)/sum(meil),2)),0) as qgrad,\n" + 
					"nvl(decode(sum(meil),0,0,round_new(sum(meil*b.qgrad_daf)/sum(meil),2)),0) as qgrad_daf\n" + 
					"\n" + 
					" from\n" + 
					"(select  caiyb_id,sum(nvl(meil,0)) meil\n" + 
					"         from rulmzlzmxb\n" + 
					"         where shenhzt =7\n" + 
					"         and rulrq>=to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00','yyyy-mm-dd hh24:mi:ss')+(-1)+("+da+")\n" + 
					" 		  and rulrq<to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00','yyyy-mm-dd hh24:mi:ss')+("+da+")\n" + 
					"         group by caiyb_id) a ,\n" + 
					"(select r.caiyb_id, max(nvl(r.qnet_ar,0)) qnet_ar, max(nvl(r.aar,0)) aar,\n" + 
					"        max(nvl(r.ad,0)) ad, max(nvl(r.sdaf,0)) sdaf, max(nvl(r.har,0)) har, max(nvl(r.qgrd,0)) qgrd,\n" + 
					"        max(nvl(r.vdaf,0)) vdaf,max(nvl(r.mt,0)) mt,max(nvl(r.stad,0)) stad,max(nvl(r.hdaf,0)) hdaf,\n" + 
					"        max(nvl(r.aad,0)) aad,max(nvl(r.mad,0)) mad,max(nvl(r.qbad,0)) qbad,max(nvl(r.had,0)) had,\n" + 
					"        max(nvl(r.vad,0)) vad,max(nvl(r.fcad,0)) fcad,max(nvl(r.std,0)) std,max(nvl(r.qgrad,0)) qgrad,\n" + 
					"        max(nvl(r.qgrad_daf,0)) qgrad_daf\n" + 
					"\n" + 
					" from rulmzlzmxb r where r.shenhzt =7  \n" +
					" and r.rulrq>=to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00','yyyy-mm-dd hh24:mi:ss')+(-1)+("+da+")\n" + 
					" and r.rulrq<to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00','yyyy-mm-dd hh24:mi:ss')+("+da+")\n" + 
					"group by r.caiyb_id\n" + 
					") b where a.caiyb_id=b.caiyb_id";
					
				ResultSetList ru_rsl=con.getResultSetList(ru_sql);
				while(ru_rsl.next()){
					
					String sql_gz=" select * from rulmzlb where rulrq= to_date('"+
					rulrq.substring(0, 10)+" ','yyyy-mm-dd')+"+da+" ";//这个日期是
					String aa="0";
					ResultSetList rsl_gz=con.getResultSetList(sql_gz);
					while(rsl_gz.next()){
						aa=rsl_gz.getString("id");
					}
					
					sql.append(" update rulmzlb set qnet_ar="+ru_rsl.getDouble("qnet_ar")+",aar="+ru_rsl.getDouble("aar")+"," +
							" ad="+ru_rsl.getDouble("ad")+", vdaf="+ru_rsl.getDouble("vdaf")+", mt="+
							ru_rsl.getDouble("mt")+", stad="+ru_rsl.getDouble("stad")+", aad="+ru_rsl.getDouble("aad")+
							", mad="+ru_rsl.getDouble("mad")+", qbad="+ru_rsl.getDouble("qbad")+"," +
							"had="+ru_rsl.getDouble("had")+", vad="+ru_rsl.getDouble("vad")+", fcad="+ru_rsl.getDouble("fcad")+
							", std="+ru_rsl.getDouble("std")+", qgrad="+ru_rsl.getDouble("qgrad")+", hdaf="
							+ru_rsl.getDouble("hdaf")+", sdaf="+ru_rsl.getDouble("sdaf")+", var="+ru_rsl.getDouble("var")+
							",har="+ru_rsl.getDouble("har")+",qgrd="+ru_rsl.getDouble("qgrd")+",qgrad_daf="+
							ru_rsl.getDouble("qgrad_daf")+",meil="+ru_rsl.getDouble("meil")+//meil已经存在的，totalmeil是新增加的
							" where id="+aa+";\n");
				}
				
				
				
				//更改时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						"二审回退",getExtGrid().mokmc,
						tableName,mdrsl.getString("id"));
			}
			sql.append("end;");
			int flag = 0;
			if(sql.length()>13){
				flag = con.getUpdate(sql.toString());
			}
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ sql);
				return;
			}
		}
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

	private boolean blnHed = false;

	public void HedButton(IRequestCycle cycle) {
		blnHed = true;

	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();

			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();

		}
		if (_RefurbishChick) {
			_RefurbishChick = false;

		}
		if (blnHed) {
			blnHed = false;
			Hed();
		}

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
			riq1 = null;
			riq2 = null;
		}
		getSelectData();
	}
}