package com.zhiren.dc.huaygl.huaysh.ruchysh;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2014-03-18
 * 描述：基于com/zhiren/dc/huaygl/huaysh/ruchysh/Ruchyyjsh.java 1.1.3
 * 		为英化热电特殊制作
 */

public class Huayys_xb extends BasePage implements PageValidateListener {
	
	private String CustomSetKey = "Huayys_xb";
	
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


		sql = 
			"SELECT L.ID,\n" +
			"       TO_CHAR(L.HUAYSJ, 'yyyy-mm-dd') AS HUAYSJ,\n" + 
			"       TO_CHAR(Z.BIANM) AS HUAYBH,\n" + 
			"       ROUND_NEW(L.QNET_AR * 1000 / 4.1816, 0) QNET_AR_CAL,\n" + 
			"       L.QNET_AR,\n" + 
			"       L.AAR,\n" + 
			"       L.AD,\n" + 
			"       L.VDAF,\n" + 
			"       ROUND_NEW(L.MT, 2) AS MT,\n" + 
			"       L.STAD,\n" + 
			"       L.AAD,\n" + 
			"       NVL(L.AAD_YS, L.AAD) AAD_YS,\n" + 
			"       L.MAD,\n" + 
			"       ROUND_NEW(100 * (L.MT - L.MAD) / (100 - L.MAD), 2) AS MF,\n" + 
			"       L.QBAD,\n" + 
			"       NVL(L.QBAD_YS, L.QBAD) QBAD_YS,\n" + 
			"       L.HAD,\n" + 
			"       NVL(L.HAD_YS, L.HAD) HAD_YS,\n" + 
			"       L.VAD,L.FCAD,L.STD,L.QGRAD,L.HDAF,L.QGRAD_DAF,L.SDAF,L.QGRD,\n" + 
			"       L.T1,L.T2,L.T3,L.T4,HUAYY,L.LURY,L.BEIZ,L.HUAYLB \n"
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
			+ "     AND (shenhzt = 3 OR shenhzt = 4)\n"
			+ "ORDER BY z.ID, z.bianm, l.huaylb";
		
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
//		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("zhillsb");
		egu.setMokmc("化验一级审核");
		// 设置页面宽度以便增加滚动条
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_formgrid);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		egu.setDefaultsortable(false);

		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
//		egu.getColumn("huaybh").setUpdate(false);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
//		egu.getColumn("huaysj").setUpdate(false);
		egu.getColumn("huaylb").setHeader("化验类别");
		egu.getColumn("huaylb").setEditor(null);
//		egu.getColumn("huaylb").setUpdate(false);
		if(!ShowHuaylb){
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.getColumn("qnet_ar").setHeader("收到基低位热量<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		
		egu.getColumn("qnet_ar_cal").setHeader("收到基低位热量<p>Qnet,ar(大卡)</p>");
		egu.getColumn("qnet_ar_cal").setEditor(null);
//		egu.getColumn("qnet_ar_cal").setUpdate(false);

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
//		egu.getColumn("aad").setEditor(null);
		egu.getColumn("aad_ys").setHeader("空气干燥基灰分<p>原始值Aad(%)</p>");
		egu.getColumn("aad_ys").setEditor(null);
		egu.getColumn("mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		// 增加化验外水指标
		egu.getColumn("mf").setHeader("外水<p>Mf(%)</p>");
		egu.getColumn("mf").setEditor(null);
		egu.getColumn("mf").setHidden(true);
		egu.getColumn("mf").setUpdate(false);
		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
//		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("qbad_ys").setHeader("空气干燥基弹筒热值<p>原始值Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad_ys").setEditor(null);
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
//		egu.getColumn("had").setEditor(null);
		egu.getColumn("had_ys").setHeader("空气干燥基氢<p>原始值Had(%)</p>");
		egu.getColumn("had_ys").setEditor(null);
		egu.getColumn("vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("固定碳<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("干燥基全硫<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("空气干燥基高位热值<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热值<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("干燥无灰基全硫<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("qgrd").setHeader("干燥基高位热值<p>Qgr,d(Mj/kg)</p>");
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
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		// egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad_ys").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mf").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("qbad_ys").setWidth(80);
		egu.getColumn("had_ys").setWidth(80);
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
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		egu.addToolbarButton("保存", GridButton.ButtonType_Save,"HedButton");
		
		egu.addToolbarButton("审核", GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);

		egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,"HuitButton");

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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
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

		StringBuffer sql=new StringBuffer();
		sql.append("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			double Mt=mdrsl.getDouble("mt");
			double Mad=mdrsl.getDouble("mad");
			double Aad=mdrsl.getDouble("aad");
			double Vad=mdrsl.getDouble("vad");
			double Qbad=mdrsl.getDouble("qbad");
			double H=mdrsl.getDouble(sign_H);
			double S=mdrsl.getDouble(sign_S);
//			更新质量临时表
			sql.append("update zhillsb set ");
			sql.append("AAD").append("=" + mdrsl.getDouble("AAD") + ",\n");
			sql.append("QBAD").append("=" + mdrsl.getDouble("QBAD") + ",\n");
			sql.append("HAD").append("=" + mdrsl.getDouble("HAD") + ",\n");
			sql.append("AAD_YS").append("=" + mdrsl.getDouble("AAD_YS") + ",\n");
			sql.append("QBAD_YS").append("=" + mdrsl.getDouble("QBAD_YS") + ",\n");
			sql.append("HAD_YS").append("=" + mdrsl.getDouble("HAD_YS") + "\n");
			sql.append("where id =").append(mdrsl.getString("ID")).append(";\n");
			
//			重新计算相关热量信息并更新质量临时表
			sql.append("update zhillsb set ");
			sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, sign_H, H, sign_S, S, sacle));
			sql.append("AAD_YS").append("=" + mdrsl.getDouble("AAD_YS") + ",\n");
			sql.append("QBAD_YS").append("=" + mdrsl.getDouble("QBAD_YS") + ",\n");
			sql.append("HAD_YS").append("=" + mdrsl.getDouble("HAD_YS") + "\n");
			sql.append("where id =").append(mdrsl.getString("ID")).append(";\n");
		}
		sql.append("end; \n");
		if(sql.length()>10){
			con.getUpdate(sql.toString());
		}
//		向记录表写入记录
		SaveLog(con, getChange(), visit );
		
		mdrsl.close();
		con.Close();
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
		
//		5为不需要进行确认操作，9为需要确认操作
		String shenhzt=MainGlobal.getXitxx_item("化验", "化验一审和二审间是否需要确认操作", getTreeid(), "5");
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			
			/*作者:王总兵,时间:2010-8-8 17:03:41 电厂:宣威电厂
			 * 当化验值来源于化验接口原始报告单时,如果一个矿有两个样,化验员当天只提交了一个样,化验2审人员
			 * 因为不确定是两个样的情况下就单独把这一个样给审核了.呢么当化验员从原始报告单中重新提交
			 * 另外一个样的化验结果时,这个样到化验2审就审核不过去.原因就是zhilb已经插入有值了.
			 * 所以在化验一级审核提交时,先判断这个样所对应的zhilb中是否有值,如果有值,把zhilb中的这一条数据
			 * 删除掉.然后更新这条zhilb数据所对应的zhillsb的状态和是否使用字段.
			 */
			
			String zhillsb_id=mdrsl.getString("ID");
			String IsZhilbHaveZhi=
				"select zl.id from zhilb zl where zl.id in (\n" +
				"select ls.zhilb_id from zhillsb ls where ls.id="+zhillsb_id+"\n" + 
				")";
			ResultSetList rs =con.getResultSetList(IsZhilbHaveZhi);
			if(rs.next()){
				sql.append("delete zhilb where id=").append(rs.getLong("id")).append(";\n");
				sql.append("update zhillsb ls set ls.shenhzt="+shenhzt+",ls.shifsy=0 where ls.id in (");
				sql.append("select ls.id from zhillsb ls where ls.zhilb_id="+rs.getLong("id")+" and ls.shifsy=1)").append(";\n");
			}
			
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt ="+shenhzt).append(",");
			
			//增加 shenhry的记录
			sql.append(" shenhry='").append(visit.getRenymc()).append("',");
			
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	public void Save2(String strchange, Visit visit) {
		String tableName = "zhillsb";
		
		JDBCcon con = new JDBCcon();

		boolean saveLog = true;
		ResultSetList rs = con.getResultSetList("select zhi from xitxxb where mingc = '化验一审回退日志' and diancxxb_id = "
				+ visit.getDiancxxb_id());
		while (rs.next()) {
			if (rs.getString("zhi").equals("是")) {
				saveLog = true;
			} else {
				saveLog = false;
			}
		}
		
		StringBuffer sql = new StringBuffer("begin \n");	
		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = ");
			sql.append("2").append(",");
			
			sql.append(" shenhry='").append(visit.getRenymc()).append("',");
			
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
			
			//更改时增加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					"一审回退",getExtGrid().mokmc,
					tableName,mdrsl.getString("id"));
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		
		if (saveLog) {
			SaveLog(con, strchange, visit );
		}		
	}
	
	private String SaveLog(JDBCcon con,String strchange,Visit visit){
		String[] ipAddress = new String[2];
		getIpAddr(ipAddress);
		String tableName = "zhillsb_log";
		StringBuffer sb = new StringBuffer("begin \n");
				
		ResultSetList delrsl = visit.getExtGrid1()
		.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sb.append( "insert into ").append(tableName);
			sb.append(
					"(ID,ZHILB_ID,HUAYSJ,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QGRAD,HDAF,QGRAD_DAF,SDAF,\n" +
					" T1,T2,T3,T4,HUAYY,LURY,BEIZ,SHENHZT,BANZ,HUAYLBB_ID,HUAYLB,LIUCZTB,BUMB_ID,HAR,QGRD,VAR,QBRAD,SHIFSY,\n" + 
					" SHENHRY,SHENHRYEJ,zhillsb_id,IPADDRESS,HOSTNAME,DONGZMC,DONGZR,DONGZSJ\n" + 
					" )\n");
				sb.append(" values(getNewId(" + visit.getDiancxxb_id() + "),"); 
				sb.append(delrsl.getLong("zhilb_id")).append(",").append("to_date('" + delrsl.getString("Huaysj")+ "','yyyy-mm-dd'),");
				sb.append(delrsl.getDouble("qnet_ar")).append(",").append(delrsl.getDouble("aar")).append(",");
				sb.append(delrsl.getDouble("ad")).append(",").append(delrsl.getDouble("vdaf")).append(",");
				sb.append(delrsl.getDouble("mt")).append(",").append(delrsl.getDouble("stad")).append(",");
				sb.append(delrsl.getDouble("aad")).append(",").append(delrsl.getDouble("mad")).append(",");
				sb.append(delrsl.getDouble("qbad")).append(",").append(delrsl.getDouble("had")).append(",");
				sb.append(delrsl.getDouble("vad")).append(",").append(delrsl.getDouble("fcad")).append(",");
				sb.append(delrsl.getDouble("std")).append(",").append(delrsl.getDouble("qgrad")).append(",");
				sb.append(delrsl.getDouble("hdaf")).append(",").append(delrsl.getDouble("qgrad_daf")).append(",");
				sb.append(delrsl.getDouble("sdaf")).append(",").append(delrsl.getDouble("t1")).append(",");
				sb.append(delrsl.getDouble("t2")).append(",").append(delrsl.getDouble("t3")).append(",");
				sb.append(delrsl.getDouble("t4")).append(",'").append(delrsl.getString("huayy")).append("','");
				sb.append(delrsl.getString("lury")).append("','").append(delrsl.getString("beiz")).append("',");
				sb.append(delrsl.getInt("shenhzt")).append(",").append(delrsl.getString("banz")).append(",");
				sb.append(delrsl.getLong("huaylbb_id")).append(",'").append(delrsl.getString("huaylb")).append("',");
				sb.append(delrsl.getLong("liucztb")).append(",").append(delrsl.getLong("bumb_id")).append(",");
				sb.append(delrsl.getDouble("har")).append(",").append(delrsl.getDouble("qgar")).append(",");
				sb.append(delrsl.getDouble("var")).append(",").append(delrsl.getDouble("qbrad")).append(",");
				sb.append(delrsl.getInt("shifsy")).append(",").append(delrsl.getString("shenhry")).append(",");
				sb.append(delrsl.getString("shenhryej")).append(",").append(delrsl.getLong("id")).append(",'");
			sb.append(ipAddress[0]).append("','").append(ipAddress[1]).append("',").append("'一审删除'").append(",'");
			sb.append(visit.getRenymc()).append("',").append("sysdate); \n");
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while(mdrsl.next()){
			sb.append( "insert into ").append(tableName);
			sb.append(
				"(ID,ZHILB_ID,HUAYSJ,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QGRAD,HDAF,QGRAD_DAF,SDAF,\n" +
				" T1,T2,T3,T4,HUAYY,LURY,BEIZ,SHENHZT,BANZ,HUAYLBB_ID,HUAYLB,LIUCZTB,BUMB_ID,HAR,QGRD,VAR,QBRAD,SHIFSY,\n" + 
				" SHENHRY,SHENHRYEJ,zhillsb_id,IPADDRESS,HOSTNAME,DONGZMC,DONGZR,DONGZSJ\n" + 
				" )\n");
			sb.append(" values(getNewId(" + visit.getDiancxxb_id() + "),"); 
			sb.append(mdrsl.getLong("zhilb_id")).append(",").append("to_date('" + mdrsl.getString("Huaysj")+ "','yyyy-mm-dd'),");
			sb.append(mdrsl.getDouble("qnet_ar")).append(",").append(mdrsl.getDouble("aar")).append(",");
			sb.append(mdrsl.getDouble("ad")).append(",").append(mdrsl.getDouble("vdaf")).append(",");
			sb.append(mdrsl.getDouble("mt")).append(",").append(mdrsl.getDouble("stad")).append(",");
			sb.append(mdrsl.getDouble("aad")).append(",").append(mdrsl.getDouble("mad")).append(",");
			sb.append(mdrsl.getDouble("qbad")).append(",").append(mdrsl.getDouble("had")).append(",");
			sb.append(mdrsl.getDouble("vad")).append(",").append(mdrsl.getDouble("fcad")).append(",");
			sb.append(mdrsl.getDouble("std")).append(",").append(mdrsl.getDouble("qgrad")).append(",");
			sb.append(mdrsl.getDouble("hdaf")).append(",").append(mdrsl.getDouble("qgrad_daf")).append(",");
			sb.append(mdrsl.getDouble("sdaf")).append(",").append(mdrsl.getDouble("t1")).append(",");
			sb.append(mdrsl.getDouble("t2")).append(",").append(mdrsl.getDouble("t3")).append(",");
			sb.append(mdrsl.getDouble("t4")).append(",'").append(mdrsl.getString("huayy")).append("','");
			sb.append(mdrsl.getString("lury")).append("','").append(mdrsl.getString("beiz")).append("',");
			sb.append(mdrsl.getInt("shenhzt")).append(",").append(mdrsl.getString("banz")).append(",");
			sb.append(mdrsl.getLong("huaylbb_id")).append(",'").append(mdrsl.getString("huaylb")).append("',");
			sb.append(mdrsl.getLong("liucztb")).append(",").append(mdrsl.getLong("bumb_id")).append(",");
			sb.append(mdrsl.getDouble("har")).append(",").append(mdrsl.getDouble("qgar")).append(",");
			sb.append(mdrsl.getDouble("var")).append(",").append(mdrsl.getDouble("qbrad")).append(",");
			sb.append(mdrsl.getInt("shifsy")).append(",").append(mdrsl.getString("shenhry")).append(",");
			sb.append(mdrsl.getString("shenhryej")).append(",").append(mdrsl.getLong("id")).append(",'");
			sb.append(ipAddress[0]).append("','").append(ipAddress[1]).append("',").append("'一审修改'").append(",'");
			sb.append(visit.getRenymc()).append("',").append("sysdate); \n");			
		}		
		sb.append("end;");	
		con.getInsert(sb.toString());
		return sb.toString();	
	}
	
	private  void getIpAddr(String[] ipAddress) {
		ipAddress[0] = this.getPage().getRequestCycle().getRequestContext().getRequest().getRemoteAddr();
		ipAddress[1] = this.getPage().getRequestCycle().getRequestContext().getRequest().getRemoteHost();	
	};
	
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
			this.setTreeid("" + visit.getDiancxxb_id());
		}
		getSelectData();
	}

}