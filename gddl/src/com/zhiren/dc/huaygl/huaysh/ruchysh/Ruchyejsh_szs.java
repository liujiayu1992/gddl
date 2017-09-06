package com.zhiren.dc.huaygl.huaysh.ruchysh;

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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.huaygl.Shenhcl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruchyejsh_szs extends BasePage implements PageValidateListener {
	private String CustomSetKey = "Ruchyejsh_szs";
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String zhilbids = "";
	public String getZhilbids(){
		return zhilbids;
	}
	public void setZhilbids(String ids){
		zhilbids= ids;
	}

	// 保存页面上比率的总和
	private String BilTotal;

	public String getBilTotal() {
		return BilTotal;
	}

	public void setBilTotal(String BilTotal) {
		this.BilTotal = BilTotal;
	}

	// 绑定日期
//	boolean riqichange = false;
	private String riqi;

	public String getBeginRiq() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setBeginRiq(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
//			riqichange = true;
		}

	}

//	boolean riq2change = false;

	private String riq2;

	public String getEndRiq() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setEndRiq(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
//			riq2change = true;
		}

	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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

	private boolean _AvgClick = false;
	public void AvgButton(IRequestCycle cycle) {
		_AvgClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelect();
			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelect();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelect();
			getSelectData();
		}
		if (_AvgClick) {
			_AvgClick = false;
			getAvg();
		}
	}
	
	public void Save(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		List fhlist = new ArrayList();
		String zhilbid = "";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String strsql = "begin \n";
		while (mdrsl.next()) {
			strsql += "update zhillsb set shenhzt=7,shenhryej='"+visit.getRenymc()+"' where zhilb_id =" + mdrsl.getString("id") + ";\n";
			
			strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
					",had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
			+"values("+mdrsl.getString("id")+",'"+mdrsl.getString("huaybh")+"',"+mdrsl.getString("caiyb_id")+","
			+ DateUtil.FormatOracleDate(mdrsl.getString("huaysj"))+","+mdrsl.getString("qnet_ar") + ","
			+ mdrsl.getString("aar") + "," + mdrsl.getString("ad") + "," + mdrsl.getString("vdaf") + ","
			 + mdrsl.getString("mt") + "," + mdrsl.getString("stad") + "," + mdrsl.getString("aad") + ","
			 + mdrsl.getString("mad") + "," + mdrsl.getString("qbad") + "," + mdrsl.getString("had") + ","
			 + mdrsl.getString("vad") + "," + mdrsl.getString("fcad") + "," + mdrsl.getString("std") + ","
			 + mdrsl.getString("qgrad") + "," + mdrsl.getString("hdaf") + "," + mdrsl.getString("qgrad_daf") + ","
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
			 	+ mdrsl.getString("lury") + "','" + mdrsl.getString("beiz") + "',1,1);\n";
			zhilbid = mdrsl.getString("ID");
//			更改时增加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					"二级审核",getExtGrid().mokmc,
					"zhilb",mdrsl.getString("ID"));
			
			String Sql = "select id from fahb where zhilb_id = " + zhilbid;
			ResultSetList rs = con.getResultSetList(Sql);
			while (rs.next()) {
				String id = rs.getString("id");
				Jilcz.addFahid(fhlist, id);
			}
			rs.close();
		}
		strsql += " end;";
		con.getUpdate(strsql);
		mdrsl.close();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(),fhlist,false);
	}

	public void Huit(){
		Visit visit = (Visit) this.getPage().getVisit();
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set shenhzt=4,shenhryej='"+visit.getRenymc()+"' where zhilb_id =").append(mdrsl.getString("id"))
			.append(";\n");
//			更改时增加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					"二级审核回退",getExtGrid().mokmc,
					"zhilb",mdrsl.getString("ID"));
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}
	
	private boolean Display = false;
	private boolean xiansztl = false;
	private boolean xiansztq = false;
	private boolean yangplb = false;
	private boolean IsShow=false;
	
	public void getSelect(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		//在系统信息表中查出，二级审核样品类别显示，如果为'是'则在页面显示,如果不为'是'页面则不显示
		sql = "select zhi from xitxxb where mingc ='二级审核样品类别显示' and zhuangt = 1 and diancxxb_id = "+
			 	visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			if(rsl.getString("zhi").equals("是")){
				yangplb = true;
			}else{
				yangplb = false;
			}
		}
		sql = "select zhi from xitxxb where mingc = '是否显示入厂化验硫' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
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
		sql = "select zhi from xitxxb where mingc = '二级审核显示矿'";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			if(rsl.getString("zhi").equals("显示")){
				Display = true;
			}
		}
	//	化验二级审核是否只显示化验的原始指标(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),默认为全部显示
		sql = "select zhi from xitxxb where mingc = '化验二级审核是否只显示化验的主要指标'  and zhuangt = 1  ";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				IsShow = true;
			} else {
				IsShow = false;
			}
		}
		rsl.close();
	//	电厂Tree刷新条件
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
		sql = "select distinct l.id,\n"
			+ "                l.zhilb_id as zhilid,\n"
			+ "                l.bil\n"
			+ "  from zhilb z,\n"
			+ "       zhillsb l,\n"
			+ "       caiyb c,\n"
			+ "       (select *\n"
			+ "          from zhuanmb\n"
			+ "         where zhuanmlb_id =\n"
			+ "       (select id\n"
			+ "          from zhuanmlb\n"
			+ "         where jib = (select nvl(max(jib), 0) from zhuanmlb))) zm,\n"
			+ "       (select sum(laimsl) as jingz,sum(f.ches) as ches, meikxxb_id, zhilb_id, pinzb_id,faz_id\n"
			+ "          from fahb f, diancxxb dc\n"
			+ "				where f.diancxxb_id = dc.id \n"
			+ 				  str + "\n"
			+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
			+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
			+ " where f.zhilb_id = z.id(+)\n"
			+ "   and c.zhilb_id = f.zhilb_id\n"
			+ "   and f.meikxxb_id = m.id\n"
			+ "   and f.zhilb_id = l.zhilb_id\n"
			+ "   and f.pinzb_id = p.id\n"
			+ "   and f.faz_id=cz.id\n"
			+ "   and zm.zhillsb_id = l.id\n"
			+ "   and (l.shenhzt = 5 or l.shenhzt = 6)\n"
			+ "   and l.zhilb_id in (select distinct c.zhilb_id from caiyb c,fahb f,zhillsb z\n" 
			+ "                       where f.zhilb_id = c.zhilb_id and c.zhilb_id = z.zhilb_id and (z.shenhzt = 5 or z.shenhzt = 6) \n"
			+ "                         and f.daohrq>=" + DateUtil.FormatOracleDate(getBeginRiq())
			+ "                         and f.daohrq<=" + DateUtil.FormatOracleDate(getEndRiq())
			+		")\n"
			+ " order by l.zhilb_id";
		rsl = con.getResultSetList(sql);
		String zhilb_ids = "";
		String sqlsb = new String();//"begin"
		String mark = MainGlobal.getXitxx_item("化验", "是否输入比率计算化验值", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
		if (mark.equals("是")) {
			while(rsl.next()){
				zhilb_ids += "," + rsl.getString("id");
				sqlsb = "\n update zhillsb set shifsy = 1, bil = " + rsl.getString("bil") + " where id = " + rsl.getString("id");
				con.getUpdate(sqlsb);
			}
		} else {
			while(rsl.next()){
				zhilb_ids += "," + rsl.getString("id");
				sqlsb = "\n update zhillsb set shifsy = 1" + " where id = " + rsl.getString("id");
				con.getUpdate(sqlsb);
			}
		}
		if(!"".equals(zhilb_ids)){
			zhilb_ids = zhilb_ids.substring(1);
		}else{
			zhilb_ids = "-1";
		}
		setZhilbids(zhilb_ids);
		//更新质量临时表中是否使用的状态
		sql = "update zhillsb set shifsy = 0 where zhilb_id in(" + getZhilbids() + ") ";
		con.getUpdate(sql);
		rsl.close();
		con.Close();
	}
	
	public void getSelectData(){
		JDBCcon con = new JDBCcon();
		String sql = "";
		String mark = MainGlobal.getXitxx_item("化验", "是否输入比率计算化验值", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
		String erjshxs = MainGlobal.getXitxx_item("化验", "二级审核显示化验编号", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "是");
		String zzhy = MainGlobal.getXitxx_item("化验", "最终化验编号设置", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "进厂批号");
		if ("是".equals(mark)) {
			sql = Shenhcl.getErjshpj(con, getZhilbids(), getBilTotal());
		} else {
			if("是".equals(erjshxs) && "进厂批号".equals(zzhy)){
				sql = "select a.id,a.meikdw,a.faz,a.caiyb_id,a.pinz,a.ches,a.shul,a.huaybh," +
						"zm.bianm huaybianh,a.huaysj,a.qnet_ar,a.aar,a.ad,a.vdaf,a.mt,a.stad," +
						"a.aad,a.mad,a.qbad,a.had,a.vad,a.fcad,a.std,a.qgrad,a.hdaf,a.qgrad_daf," +
						"a.sdaf,a.t1,a.t2,a.t3,a.t4,a.huayy,a.lury,a.beiz  " +
						"from (" + Shenhcl.Judgment(con, getZhilbids()) + ") a," +
								"zhillsb zls,zhuanmb zm,zhuanmlb zmlb " +
						"where a.id=zls.zhilb_id and zls.id=zm.zhillsb_id and zm.zhuanmlb_id=zmlb.id and zmlb.jib=3";
			}else{
				sql = Shenhcl.Judgment(con, getZhilbids());
			}
		}
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		// 设置表名称用于保存
		egu.setTableName("zhilb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// 设置显示列名称
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("shul").setHeader("数量(吨)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("huaybh").setHeader("进厂批号");
		egu.getColumn("huaybh").setEditor(null);
		
		if("是".equals(erjshxs) && "进厂批号".equals(zzhy)){
			egu.getColumn("huaybianh").setHeader("化验编号");
			egu.getColumn("huaybianh").setEditor(null);
			egu.getColumn("huaybianh").setWidth(90);
		}
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("收到基地位热量<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
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
		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
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
		egu.getColumn("shul").setWidth(50);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
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
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		
		egu.getColumn("beiz").setWidth(80);
		
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
		if(!Display){
			egu.getColumn("meikdw").setHidden(true);
			egu.getColumn("faz").setHidden(true);
			egu.getColumn("pinz").setHidden(true);
			egu.getColumn("shul").setHidden(true);
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
		if(!yangplb){
			egu.getColumn("huaylb").setHidden(true);
		}		
		egu.addPaging(25);
		
		// 工具栏
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		egu.addTbarText("起始日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBeginRiq());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("截止日期:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getEndRiq());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton("审核", GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
		egu.addTbarText("-");
		egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,"HuitButton");
		egu.addTbarText("-");
		GridButton Create = new GridButton("查看平均值", "ShowAvg", SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(Create);
		setExtGrid(egu);
		con.Close();
	}
	
	//查看平均值
	public void getAvg() {
		JDBCcon con = new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		String zhilb_ids = "";
		while (mdrsl.next()) {
			zhilb_ids += "," + mdrsl.getString("id");
		}
		mdrsl.close();
		zhilb_ids = zhilb_ids.substring(1);
		String sql = "select avg(z.qnet_ar) qnet_ar,\n"
				+ "       avg(z.aar) aar,\n"
				+ "       avg(z.ad) ad,\n"
				+ "       avg(z.vdaf) vdaf,\n"
				+ "       avg(z.mt) mt,\n"
				+ "       avg(z.stad) stad,\n"
				+ "       avg(z.aad) aad,\n"
				+ "       avg(z.mad) mad,\n"
				+ "       avg(z.qbad) qbad,\n"
				+ "       avg(z.had) had,\n"
				+ "       avg(z.vad) vad,\n"
				+ "       avg(z.fcad) fcad,\n"
				+ "       avg(z.std) std,\n"
				+ "      avg( z.qgrad) qgrad,\n"
				+ "       avg(z.hdaf) hdaf,\n"
				+ "       avg(z.qgrad_daf) qgrad_daf,\n"
				+ "       avg(z.sdaf) sdaf\n"
				+ "      from (select max(l.zhilb_id) as id,\n"
				+ "               max(l.huaysj) as huaysj,\n"
				+ "               avg(l.qnet_ar) qnet_ar,\n"
				+ "               avg(l.aar) aar,\n"
				+ "               avg(l.ad) ad,\n"
				+ "               avg(l.vdaf) vdaf,\n"
				+ "               avg(l.mt) mt,\n"
				+ "               avg(l.stad) stad,\n"
				+ "               avg(l.aad) aad,\n"
				+ "               avg(l.mad) mad,\n"
				+ "               avg(l.qbad) qbad,\n"
				+ "               avg(l.had) had,\n"
				+ "               avg(l.vad) vad,\n"
				+ "               avg(l.fcad) fcad,\n"
				+ "               avg(l.std) std,\n"
				+ "               avg(l.qgrad) qgrad,\n"
				+ "               avg(l.hdaf) hdaf,\n"
				+ "               avg(l.qgrad_daf) qgrad_daf,\n"
				+ "               avg(l.sdaf) sdaf \n"
				+ "          from zhillsb l\n"
				+ "         where id in ("
				+ zhilb_ids
				+ ")\n"
				+ "         group by l.zhilb_id, l.huaysj) z,\n"
				+ "       caiyb c,\n"
				+ "       (select sum(laimsl) as jingz, meikxxb_id, zhilb_id, pinzb_id\n"
				+ "          from fahb\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p\n"
				+ " where f.zhilb_id = c.zhilb_id\n"
				+ "   and f.zhilb_id = z.id\n" + "   and c.zhilb_id = z.id\n"
				+ "   and f.meikxxb_id = m.id\n" + "   and f.pinzb_id = p.id";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zhilb");
		egu.setWidth(1000);
		egu.getColumn("qnet_ar").setHeader("收到基地位热量<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
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
		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
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
		egu.addPaging(18);
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("返回",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(refurbish);

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
			riqi = null;
//			riqichange = false;
			riq2 = null;
//			riq2change = false;
			this.setTreeid("" + visit.getDiancxxb_id());
			getSelect();
			getSelectData();
		}
//		if (riqichange || riq2change) {
//			riqichange = false;
//			riq2change = false;
//		}
		
	}

}
