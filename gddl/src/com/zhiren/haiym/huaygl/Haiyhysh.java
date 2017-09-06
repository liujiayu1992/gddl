package com.zhiren.haiym.huaygl;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-06-02
 * 描述：修改 化验二级审核成本计算 调用方法Chengbjs
 */
/*
 * 作者：王磊
 * 时间：2009-06-15
 * 描述：增加质量临时表是否计入zhilib平均值的标记
 */
public class Haiyhysh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg("");
		setZhilbids("");
	}
	
	private String zhilbids = "";
	public String getZhilbids(){
		return zhilbids;
	}
	public void setZhilbids(String ids){
		zhilbids= ids;
	}
	// 绑定日期
	boolean riqichange = false;

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
			riqichange = true;
		}

	}

	boolean riq2change = false;

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
			riq2change = true;
		}

	}
	
//	public IDropDownBean getJincpcValue() {
//		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
//			if(getJincpcModel().getOptionCount()>0) {
//				setJincpcValue((IDropDownBean)getJincpcModel().getOption(0));
//			}
//		}
//		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
//	}
//	public void setJincpcValue(IDropDownBean value) {
//		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
//	}
//	
//	public IPropertySelectionModel getJincpcModel() {
//		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
//			setJincpcModels();
//		}
//		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
//	}
//	public void setJincpcModel(IPropertySelectionModel value) {
//		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
//	}
//	
//	public void setJincpcModels() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("select distinct c.zhilb_id,c.bianm from caiyb c,fahb f,zhillsb z\n")
//		.append(" where f.zhilb_id = c.zhilb_id and c.zhilb_id = z.zhilb_id and (z.shenhzt = 5 or z.shenhzt = 6) \n")
//		.append("and f.daohrq>=").append(DateUtil.FormatOracleDate(getBeginRiq()))
//		.append(" and f.daohrq <=").append(DateUtil.FormatOracleDate(getEndRiq()));
//		List list = new ArrayList();
//		list.add(new IDropDownBean(-1,"请选择"));
//		setJincpcModel(new IDropDownModel(list,sb));
//	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Shifjqpj;

	public String getShifjqpj() {
		return Shifjqpj;
	}

	public void setShifjqpj(String shifjqpj) {
		Shifjqpj = shifjqpj;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		List fhlist = new ArrayList();
		String zhilbid = "";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String strsql = "begin \n";
		if (mdrsl.next()) {
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
			
		}
		strsql += " end;";
		
		con.getUpdate(strsql);
		
		
		
		String Sql = "select id from fahb where zhilb_id = " + zhilbid;
		mdrsl = con.getResultSetList(Sql);
		while (mdrsl.next()) {
			String id = mdrsl.getString("id");
			Jilcz.addFahid(fhlist, id);
		}
		mdrsl.close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(),fhlist,false);
		getYundxxModels();
//		setJincpcValue(null);
//		setJincpcModel(null);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set shenhzt=2,shenhryej='"+visit.getRenymc()+"' where id =").append(mdrsl.getString("id"))
			.append(";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
//		setJincpcValue(null);
//		setJincpcModel(null);
	}
	
	private void Select(){
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		String zhilb_ids = "";
		JDBCcon con = new JDBCcon();
		while(mdrsl.next()){
			zhilb_ids += "," + mdrsl.getString("id");
			String sql = "update zhillsb set shifsy= 1 where id =" + mdrsl.getString("id") ;
			con.getUpdate(sql);
		}
		mdrsl.close();
		con.Close();
		zhilb_ids = zhilb_ids.substring(1);
		setZhilbids(zhilb_ids);
		getSelectData();
	}

	private void Fanh(){
		String zhilb_ids = "";
		setZhilbids(zhilb_ids);
		getSelectData();
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

	private boolean _SelectChick = false;

	public void SelectButton(IRequestCycle cycle) {
		_SelectChick = true;
	}
	
	private boolean _FanhChick = false;

	public void FanhButton(IRequestCycle cycle) {
		_FanhChick = true;
	}
	
	private boolean _AvgClick = false;
	public void AvgButton(IRequestCycle cycle){
		_AvgClick = true;
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
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SelectChick) {
			_SelectChick = false;
			Select();
		}
		if (_FanhChick) {
			_FanhChick = false;
			Fanh();
		}
//		if (_AvgClick){
//			_AvgClick = false;
//			getAvg();
//		}

	}
	private boolean Display = false;
	
	private boolean xiansztl = false;

	private boolean xiansztq = false;
	
	private boolean jiaqpj = false;
	
	private boolean yangplb = false;
	
	private boolean IsShow=false;
	
	private void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String SQL = "select zhi from xitxxb where mingc = '海运煤化验审核平均值计算方法' and zhuangt = 1 and diancxxb_id = "
			+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		Shifjqpj="算术平均";
	    while (rs.next()) {
			Shifjqpj=rs.getString("zhi");
		}
		
		if("".equals(getZhilbids())){
			getSelect();
		}else{
			getQuer();
		}
	}
	
	private void getSelect(){
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
//		sql = "select zhi from xitxxb where mingc = '二级审核显示矿'";
//		rsl = con.getResultSetList(sql);
//		while(rsl.next()){
//			if(rsl.getString("zhi").equals("显示")){
//				Display = true;
//			}
//		}
		
		
//		化验二级审核是否只显示化验的原始指标(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),默认为全部显示
		
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
		
			sql = "select distinct l.id,\n"
				+ "                l.zhilb_id as zhilid,\n"
				+ "                m.mingc meikdw,\n"
				+ "                cz.mingc faz,\n"
				+ "                p.mingc as pinz,\n"
				+ "                l.huaysj,\n" 
				+"				   l.huaylb,\n"
				+ "                zm.bianm as huaybh,\n"
				+ "                l.caiyml as shul,\n"
				+ "                l.qnet_ar,\n"
				+ "                l.aar,\n"
				+ "                l.ad,\n"
				+ "                l.vdaf,\n"
				+ "                l.mt,\n"
				+ "                l.stad,\n"
				+ "                l.aad,\n"
				+ "                l.mad,\n"
				+ "                l.qbad,\n"
				+ "                l.had,\n"
				+ "                l.vad,\n"
				+ "                l.fcad,\n"
				+ "                l.std,\n"
				+ "                l.qgrad,\n"
				+ "                l.hdaf,\n"
				+ "                l.qgrad_daf,\n"
				+ "                l.sdaf,\n"
				+ "                l.t1,\n"
				+ "                l.t2,\n"
				+ "                l.t3,\n"
				+ "                l.t4,\n"
				+ "                l.huayy,\n"
				+ "                l.lury,\n"
				+ "                l.beiz\n"
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
				/**
				 * huochaoyuan 2009-02-17
				 * 修改上边fahb中取得的数量信息，以前是sum(jingz-koud),修改为根据新的统计量取值(laimsl)
				 */		
				+ "          from fahb f, diancxxb dc\n"
				+ "				where f.diancxxb_id = dc.id \n"
				+ str + "\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
				+ " where f.zhilb_id = z.id(+)\n"
				+ "   and c.zhilb_id = f.zhilb_id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and f.zhilb_id = l.zhilb_id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id=cz.id\n"
				+ "   and zm.zhillsb_id = l.id\n"
				+ "   and (l.shenhzt = 3 or l.shenhzt = 4)\n"
//				+ "   and l.huaysj between "
//				+ DateUtil.FormatOracleDate(getBeginRiq()) + " and\n" + "       "
//				+ DateUtil.FormatOracleDate(getEndRiq()) + " and\n"
/**369
 * huochaoyuan 2009-03-04,由于上边SQL导致条件重复，数据不能完全查出，故注掉；
 */				
				+ "and l.zhilb_id =" + getYundxxValue().getId()
				+ " order by l.zhilb_id";
		rsl = con.getResultSetList(sql);
		//更新质量临时表中是否使用的状态
		sql = "update zhillsb set shifsy = 0 where zhilb_id =" + getYundxxValue().getId();
		con.getUpdate(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("zhilb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth(Locale.Grid_DefaultWidth);
		// /设置显示列名称
		if(!Display){
			egu.getColumn("meikdw").setHidden(true);
		}
		egu.getColumn("zhilid").setHidden(true);
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("faz").setHeader("发运港");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("shul").setHeader("数量(吨)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("shul").setWidth(70);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaylb").setHeader("化验类别");
		egu.getColumn("huaylb").setEditor(null);
		egu.getColumn("huaylb").setWidth(80);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("收到基低位热量<p>Qnet,ar(Mj/kg)</p>");
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
		
//		是否只显示化验的主要指标(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),默认为全部显示
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
		//判断是否显示化验类别
		if(!yangplb){
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.addPaging(25);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		// Toolbar tb1 = new Toolbar("tbdiv");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("WeizSelectx");
		comb1.setId("WeizSelectx");
		comb1.setWidth(100);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("WeizSelectx.on('select',function(){document.forms[0].submit();});");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBeginRiq());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getEndRiq());
		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("运单信息:");
		ComboBox comb = new ComboBox();
		comb.setTransform("YundxxDropDown");
		comb.setId("Yundxx");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(200);
		comb.setListWidth(250);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Yundxx.on('select',function(){document.forms[0].submit();});");
		
//		egu.addTbarText("日期:");
//		DateField df = new DateField();
//		df.setReadOnly(true);
//		df.setValue(this.getBeginRiq());
//		df.Binding("RIQI", "Form0");// 与html页中的id绑定,并自动刷新
//		df.setId("riqi");
//		egu.addToolbarItem(df.getScript());
//
//		egu.addTbarText("截止日期:");
//		DateField df1 = new DateField();
//		df1.setReadOnly(true);
//		df1.setValue(this.getEndRiq());
//		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
//		df1.setId("riq2");
//		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");
//		
//		egu.addTbarText("进厂批次号:");
//		ComboBox shij = new ComboBox();
//		shij.setTransform("JincpcSelect");
//		shij.setWidth(150);
//		shij.setListeners("select:function(own,rec,index){Ext.getDom('JincpcSelect').selectedIndex=index}");
//		egu.addToolbarItem(shij.getScript());
//		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("确认", GridButton.ButtonType_SubmitSel, "SelectButton");
		egu.addTbarText("-");
		egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,
		"HuitButton");
		GridButton Create = new GridButton("查看平均值", "ShowAvg", SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(Create);
		
		setExtGrid(egu);
		con.Close();
	}

	private void getQuer() {
		JDBCcon con = new JDBCcon();
		String sql = Judgment(con, getZhilbids());
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		 设置表名称用于保存
		egu.setTableName("zhilb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth(1000);
		// /设置显示列名称
		// egu.getColumn("linsid").setHidden(true);
		// egu.getColumn("linsid").setEditor(null);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("faz").setHeader("发运港");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("shul").setHeader("数量(吨)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
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
		// Toolbar tb1 = new Toolbar("tbdiv");

		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("审核", GridButton.ButtonType_Sel, "SaveButton",
				null, SysConstant.Btn_Icon_Show);
		 egu.addToolbarItem("{"+new GridButton("返回","function(){ document.getElementById('FanhButton').click();" +
			"}").getScript()+"}");

		setExtGrid(egu);
		con.Close();
	}
	
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			JDBCcon con=new JDBCcon();
			ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+" and zhuangt=1 and mingc='海运煤日期查询默认值'");
			String zhi="";
			while(rsl.next()){
				zhi=rsl.getString("zhi");
			}
			if(zhi.equals("离泊日期")){
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(2));
			}else if(zhi.equals("到泊日期")){
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(1));
			}else{
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean3(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		list.add(new IDropDownBean(1, "靠泊日期"));
		list.add(new IDropDownBean(2, "到泊日期"));
		list.add(new IDropDownBean(3, "离泊日期"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}
	

	public boolean _Yundxxchange = false;

	public IDropDownBean getYundxxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYundxxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYundxxValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setYundxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYundxxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYundxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getYundxxModels() {
		
		String sql="";
		
		switch ((int) this.getWeizSelectValue().getId()) {
		case 1:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.kaobrq>=to_date('"+this.getBeginRiq()+"','yyyy-MM-dd')\n"+
    		" and not exists(select * from zhilb where id=f.zhilb_id) and f.kaobrq<=to_date('"+this.getEndRiq()+"','yyyy-MM-dd') \n";
			break;// 靠泊日期
		case 2:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daobrq>=to_date('"+this.getBeginRiq()+"','yyyy-MM-dd')\n"+
    		" and not exists(select * from zhilb where id=f.zhilb_id) and f.daobrq<=to_date('"+this.getEndRiq()+"','yyyy-MM-dd') \n";
			break;// 到泊日期
		case 3:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daohrq>=to_date('"+this.getBeginRiq()+"','yyyy-MM-dd')\n"+
    		" and not exists(select * from zhilb where id=f.zhilb_id) and f.daohrq<=to_date('"+this.getEndRiq()+"','yyyy-MM-dd') \n";
			break;// 离泊日期
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
	
//	public void getAvg(){
//		Visit visit = (Visit) getPage().getVisit();
//		JDBCcon con = new JDBCcon();
//		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
//		String zhilb_ids = "";
//		while(mdrsl.next()){
//			zhilb_ids += "," + mdrsl.getString("id");
//		}
//		mdrsl.close();
//		zhilb_ids = zhilb_ids.substring(1);
//		String SQL = "select zhi from xitxxb where mingc = '海运煤化验审核平均值计算方法' and zhuangt = 1 and diancxxb_id = "
//			+ visit.getDiancxxb_id();
//		ResultSetList rs = con.getResultSetList(SQL);
//		String sql="";
//	    while (rs.next()) {
//		if (rs.getString("zhi").equals("加权平均")) {
//			jiaqpj = true;
//		 } else {
//			jiaqpj = false;
//		   }
//		}
//	    if(jiaqpj){
//	    	 sql = "select avg(z.qnet_ar) qnet_ar,\n" +
//	    	  "       avg(z.aar) aar,\n" + 
//	    	  "       avg(z.ad) ad,\n" + 
//	    	  "       avg(z.vdaf) vdaf,\n" + 
//	    	  "       avg(z.mt) mt,\n" + 
//	    	  "       avg(z.stad) stad,\n" + 
//	    	  "       avg(z.aad) aad,\n" + 
//	    	  "       avg(z.mad) mad,\n" + 
//	    	  "       avg(z.qbad) qbad,\n" + 
//	    	  "       avg(z.had) had,\n" + 
//	    	  "       avg(z.vad) vad,\n" + 
//	    	  "       avg(z.fcad) fcad,\n" + 
//	    	  "       avg(z.std) std,\n" + 
//	    	  "      avg( z.qgrad) qgrad,\n" + 
//	    	  "       avg(z.hdaf) hdaf,\n" + 
//	    	  "       avg(z.qgrad_daf) qgrad_daf,\n" + 
//	    	  "       avg(z.sdaf) sdaf\n" + 
//	    	  "      from (select max(l.zhilb_id) as id,\n" + 
//	    	  "               max(l.huaysj) as huaysj,\n" + 
//	    	  "               avg(l.qnet_ar) qnet_ar,\n" + 
//	    	  "               avg(l.aar) aar,\n" + 
//	    	  "               avg(l.ad) ad,\n" + 
//	    	  "               avg(l.vdaf) vdaf,\n" + 
//	    	  "               avg(l.mt) mt,\n" + 
//	    	  "               avg(l.stad) stad,\n" + 
//	    	  "               avg(l.aad) aad,\n" + 
//	    	  "               avg(l.mad) mad,\n" + 
//	    	  "               avg(l.qbad) qbad,\n" + 
//	    	  "               avg(l.had) had,\n" + 
//	    	  "               avg(l.vad) vad,\n" + 
//	    	  "               avg(l.fcad) fcad,\n" + 
//	    	  "               avg(l.std) std,\n" + 
//	    	  "               avg(l.qgrad) qgrad,\n" + 
//	    	  "               avg(l.hdaf) hdaf,\n" + 
//	    	  "               avg(l.qgrad_daf) qgrad_daf,\n" + 
//	    	  "               avg(l.sdaf) sdaf \n" + 
//	    	  "          from zhillsb l\n" + 
//	    	  "         where id in ("+ zhilb_ids +")\n" + 
//	    	  "         group by l.zhilb_id, l.huaysj) z,\n" + 
//	    	  "       caiyb c,\n" + 
//	    	  "       (select sum(laimsl) as jingz, meikxxb_id, zhilb_id, pinzb_id\n" + 
//	    	  "          from fahb\n" + 
//	    	  "         group by pinzb_id, zhilb_id, meikxxb_id) f,\n" + 
//	    	  "       meikxxb m,\n" + 
//	    	  "       pinzb p\n" + 
//	    	  " where f.zhilb_id = c.zhilb_id\n" + 
//	    	  "   and f.zhilb_id = z.id\n" + 
//	    	  "   and c.zhilb_id = z.id\n" + 
//	    	  "   and f.meikxxb_id = m.id\n" + 
//	    	  "   and f.pinzb_id = p.id";
//      }else {
//    	  
//    	  sql = "select avg(z.qnet_ar) qnet_ar,\n" +
//    	  "       avg(z.aar) aar,\n" + 
//    	  "       avg(z.ad) ad,\n" + 
//    	  "       avg(z.vdaf) vdaf,\n" + 
//    	  "       avg(z.mt) mt,\n" + 
//    	  "       avg(z.stad) stad,\n" + 
//    	  "       avg(z.aad) aad,\n" + 
//    	  "       avg(z.mad) mad,\n" + 
//    	  "       avg(z.qbad) qbad,\n" + 
//    	  "       avg(z.had) had,\n" + 
//    	  "       avg(z.vad) vad,\n" + 
//    	  "       avg(z.fcad) fcad,\n" + 
//    	  "       avg(z.std) std,\n" + 
//    	  "      avg( z.qgrad) qgrad,\n" + 
//    	  "       avg(z.hdaf) hdaf,\n" + 
//    	  "       avg(z.qgrad_daf) qgrad_daf,\n" + 
//    	  "       avg(z.sdaf) sdaf\n" + 
//    	  "      from (select max(l.zhilb_id) as id,\n" + 
//    	  "               max(l.huaysj) as huaysj,\n" + 
//    	  "               avg(l.qnet_ar) qnet_ar,\n" + 
//    	  "               avg(l.aar) aar,\n" + 
//    	  "               avg(l.ad) ad,\n" + 
//    	  "               avg(l.vdaf) vdaf,\n" + 
//    	  "               avg(l.mt) mt,\n" + 
//    	  "               avg(l.stad) stad,\n" + 
//    	  "               avg(l.aad) aad,\n" + 
//    	  "               avg(l.mad) mad,\n" + 
//    	  "               avg(l.qbad) qbad,\n" + 
//    	  "               avg(l.had) had,\n" + 
//    	  "               avg(l.vad) vad,\n" + 
//    	  "               avg(l.fcad) fcad,\n" + 
//    	  "               avg(l.std) std,\n" + 
//    	  "               avg(l.qgrad) qgrad,\n" + 
//    	  "               avg(l.hdaf) hdaf,\n" + 
//    	  "               avg(l.qgrad_daf) qgrad_daf,\n" + 
//    	  "               avg(l.sdaf) sdaf \n" + 
//    	  "          from zhillsb l\n" + 
//    	  "         where id in ("+ zhilb_ids +")\n" + 
//    	  "         group by l.zhilb_id, l.huaysj) z,\n" + 
//    	  "       caiyb c,\n" + 
//    	  "       (select sum(laimsl) as jingz, meikxxb_id, zhilb_id, pinzb_id\n" + 
//    	  "          from fahb\n" + 
//    	  "         group by pinzb_id, zhilb_id, meikxxb_id) f,\n" + 
//    	  "       meikxxb m,\n" + 
//    	  "       pinzb p\n" + 
//    	  " where f.zhilb_id = c.zhilb_id\n" + 
//    	  "   and f.zhilb_id = z.id\n" + 
//    	  "   and c.zhilb_id = z.id\n" + 
//    	  "   and f.meikxxb_id = m.id\n" + 
//    	  "   and f.pinzb_id = p.id";
//      }
//		ResultSetList  rsl = con.getResultSetList(sql);
//
//		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		egu.setTableName("zhilb");
//		egu.setWidth(1000);
//		egu.getColumn("qnet_ar").setHeader("收到基地位热量<p>Qnet,ar(Mj/kg)</p>");
//		egu.getColumn("qnet_ar").setEditor(null);
//		egu.getColumn("aar").setHeader("收到基灰分<p>Aar(%)</p>");
//		egu.getColumn("aar").setEditor(null);
//		egu.getColumn("ad").setHeader("干燥基灰分<p>Ad(%)</p>");
//		egu.getColumn("ad").setEditor(null);
//		egu.getColumn("vdaf").setHeader("干燥无灰基挥发分<p>Vdaf(%)</p>");
//		egu.getColumn("vdaf").setEditor(null);
//		egu.getColumn("mt").setHeader("全水分<p>Mt(%)</p>");
//		egu.getColumn("mt").setEditor(null);
//		egu.getColumn("stad").setHeader("空气干燥基全硫<p>St,ad(%)</p>");
//		egu.getColumn("stad").setEditor(null);
//		egu.getColumn("aad").setHeader("空气干燥基灰分<p>Aad(%)</p>");
//		egu.getColumn("aad").setEditor(null);
//		egu.getColumn("mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
//		egu.getColumn("mad").setEditor(null);
//		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
//		egu.getColumn("qbad").setEditor(null);
//		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
//		egu.getColumn("had").setEditor(null);
//		egu.getColumn("vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
//		egu.getColumn("vad").setEditor(null);
//		egu.getColumn("fcad").setHeader("固定碳<p>FCad(%)</p>");
//		egu.getColumn("fcad").setEditor(null);
//		egu.getColumn("std").setHeader("干燥基全硫<p>St,d(%)</p>");
//		egu.getColumn("std").setEditor(null);
//		egu.getColumn("qgrad").setHeader("空气干燥基高位热值<p>Qgr,ad(Mj/kg)</p>");
//		egu.getColumn("qgrad").setEditor(null);
//		egu.getColumn("hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
//		egu.getColumn("hdaf").setEditor(null);
//		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热值<p>Qgr,daf(Mj/kg)</p>");
//		egu.getColumn("qgrad_daf").setEditor(null);
//		egu.getColumn("sdaf").setHeader("干燥无灰基全硫<p>Sdaf(%)</p>");
//		egu.getColumn("sdaf").setEditor(null);
//		egu.addPaging(18);
//		egu.addTbarText("-");
//
//		GridButton refurbish = new GridButton("返回",
//				"function (){document.getElementById('RefurbishButton').click();}");
//		refurbish.setIcon(SysConstant.Btn_Icon_Return);
//		egu.addTbarBtn(refurbish);
//
//		setExtGrid(egu);
//		con.Close();
//		
//	}
	
	
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
	
	public  String Judgment(JDBCcon con, String Bianh) {
		Visit visit = (Visit) getPage().getVisit();
		String sb="select zhi from xitxxb where mingc='最终化验编号设置' and leib = '化验' and zhuangt =1";
		String zhi="自动";
		ResultSetList rsl=con.getResultSetList(sb);
		if(rsl.next()){
			zhi = rsl.getString("zhi");
		}
		String SQL = "select zhi from xitxxb where mingc = '海运煤化验审核平均值计算方法' and zhuangt = 1 and diancxxb_id = "
			+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		String sql="";
	    while (rs.next()) {
		if (rs.getString("zhi").equals("加权平均")) {
			jiaqpj = true;
		 } else {
			jiaqpj = false;
		   }
		}
		if(jiaqpj){
			sql = "select z.id,\n"
				+ "       m.mingc meikdw,\n"
				+ "       cz.mingc as faz,\n"
				+ "       c.id as caiyb_id,\n"
				+ "       p.mingc as pinz,\n"
				+ "       z.caiyml as shul,\n";
			
			if(zhi.equals("进厂批号")){
				sql+="c.bianm as huaybh,\n";
			}else{
				sql+= "       (select max(zl.bianh) as bianh\n"
					+ "          from (select substr(to_char(Sysdate, 'yyyy') ||\n"
					+ "                              decode(to_char(Sysdate, 'yy'),\n"
					+ "                                     substr(max(huaybh), 0, 2),\n"
					+ "                                     substr(max(huaybh), 3),\n"
					+ "                                     '00000') + 1,\n"
					+ "                              3) as bianh\n"
					+ "                  from zhilb) zl) as huaybh,\n";
			}
			sql+= "   z.huaysj,\n"
				+ "       z.qnet_ar,\n"
				+ "       z.aar,\n"
				+ "       z.ad,\n"
				+ "       z.vdaf,\n"
				+ "       z.mt,\n"
				+ "       z.stad,\n"
				+ "       z.aad,\n"
				+ "       z.mad,\n"
				+ "       z.qbad,\n"
				+ "       z.had,\n"
				+ "       z.vad,\n"
				+ "       z.fcad,\n"
				+ "       z.std,\n"
				+ "       z.qgrad,\n"
				+ "       z.hdaf,\n"
				+ "       z.qgrad_daf,\n"
				+ "       z.sdaf,\n"
				+ "       z.t1,\n"
				+ "       z.t2,\n"
				+ "       z.t3,\n"
				+ "       z.t4,\n"
				+ "       GetHuayy(z.id) as huayy,\n"
				+ "       GetLury(z.id) as lury,\n"
				+ "       GetBeiz(z.id) as beiz\n"
				+ "  from (select max(l.zhilb_id) as id,\n"
				+ "               max(l.huaysj) as huaysj,\n"
				+"                sum(l.caiyml) as caiyml,\n"
				+ "               round_new(sum(l.qnet_ar*l.caiyml)/sum(l.caiyml),3) qnet_ar,\n"
				+ "               round_new(sum(l.aar*l.caiyml)/sum(l.caiyml),2) aar,\n"
				+ "               round_new(sum(l.ad*l.caiyml)/sum(l.caiyml),2) ad,\n"
				+ "               round_new(sum(l.vdaf*l.caiyml)/sum(l.caiyml),2) vdaf,\n"
				+ "               round_new(sum(l.mt*l.caiyml)/sum(l.caiyml),2) mt,\n"
				+ "               round_new(sum(l.stad*l.caiyml)/sum(l.caiyml),2) stad,\n"
				+ "               round_new(sum(l.aad*l.caiyml)/sum(l.caiyml),2) aad,\n"
				+ "               round_new(sum(l.mad*l.caiyml)/sum(l.caiyml),2) mad,\n"
				+ "               round_new(sum(l.qbad*l.caiyml)/sum(l.caiyml),3) qbad,\n"
				+ "               round_new(sum(l.had*l.caiyml)/sum(l.caiyml),2) had,\n"
				+ "               round_new(sum(l.vad*l.caiyml)/sum(l.caiyml),2) vad,\n"
				+ "               round_new(sum(l.fcad*l.caiyml)/sum(l.caiyml),2) fcad,\n"
				+ "               round_new(sum(l.std*l.caiyml)/sum(l.caiyml),2) std,\n"
				+ "               round_new(sum(l.qgrad*l.caiyml)/sum(l.caiyml),3) qgrad,\n"
				+ "               round_new(sum(l.hdaf*l.caiyml)/sum(l.caiyml),2) hdaf,\n"
				+ "               round_new(sum(l.qgrad_daf*l.caiyml)/sum(l.caiyml),3) qgrad_daf,\n"
				+ "               round_new(sum(l.sdaf*l.caiyml)/sum(l.caiyml),2) sdaf,\n"
				+ "               round_new(sum(l.t1*l.caiyml)/sum(l.caiyml),0) t1,\n"
				+ "               round_new(sum(l.t2*l.caiyml)/sum(l.caiyml),0) t2,\n"
				+ "               round_new(sum(l.t3*l.caiyml)/sum(l.caiyml),0) t3,\n"
				+ "               round_new(sum(l.t4*l.caiyml)/sum(l.caiyml),0) t4\n"
				+ "          from zhillsb l\n"
				+ "         where id in ("
				+ Bianh
				+ ")\n"
				+ "         group by l.zhilb_id, l.huaysj) z,\n"
				+ "       caiyb c,\n"
				+ "       (select sum(laimsl) as jingz,sum(ches) as ches, meikxxb_id, zhilb_id, pinzb_id,faz_id\n"
				/**
				 * huochaoyuan 2009-02-17
				 * 修改上边fahb中取得的数量信息，以前是sum(jingz-koud),修改为根据新的统计量取值(laimsl)
				 */					
				+ "          from fahb\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
				+ " where f.zhilb_id = c.zhilb_id\n"
				+ "   and f.zhilb_id = z.id\n" + "   and c.zhilb_id = z.id\n"
				+ "   and f.meikxxb_id = m.id\n" + "   and f.pinzb_id = p.id   and f.faz_id=cz.id";
			
		}else {		
			sql = "select z.id,\n"
				+ "       m.mingc meikdw,\n"
				+ "       cz.mingc as faz,\n"
				+ "       c.id as caiyb_id,\n"
				+ "       p.mingc as pinz,\n"
				+ "       z.caiyml as shul,\n";
			
			if(zhi.equals("进厂批号")){
				sql+="c.bianm as huaybh,\n";
			}else{
				sql+= "       (select max(zl.bianh) as bianh\n"
					+ "          from (select substr(to_char(Sysdate, 'yyyy') ||\n"
					+ "                              decode(to_char(Sysdate, 'yy'),\n"
					+ "                                     substr(max(huaybh), 0, 2),\n"
					+ "                                     substr(max(huaybh), 3),\n"
					+ "                                     '00000') + 1,\n"
					+ "                              3) as bianh\n"
					+ "                  from zhilb) zl) as huaybh,\n";
			}
			sql+= "   z.huaysj,\n"
				+ "       z.qnet_ar,\n"
				+ "       z.aar,\n"
				+ "       z.ad,\n"
				+ "       z.vdaf,\n"
				+ "       z.mt,\n"
				+ "       z.stad,\n"
				+ "       z.aad,\n"
				+ "       z.mad,\n"
				+ "       z.qbad,\n"
				+ "       z.had,\n"
				+ "       z.vad,\n"
				+ "       z.fcad,\n"
				+ "       z.std,\n"
				+ "       z.qgrad,\n"
				+ "       z.hdaf,\n"
				+ "       z.qgrad_daf,\n"
				+ "       z.sdaf,\n"
				+ "       z.t1,\n"
				+ "       z.t2,\n"
				+ "       z.t3,\n"
				+ "       z.t4,\n"
				+ "       GetHuayy(z.id) as huayy,\n"
				+ "       GetLury(z.id) as lury,\n"
				+ "       GetBeiz(z.id) as beiz\n"
				+ "  from (select max(l.zhilb_id) as id,\n"
				+ "               max(l.huaysj) as huaysj,\n"
				+"                sum(l.caiyml) as caiyml,\n"
				+ "               round_new(avg(l.qnet_ar),3) qnet_ar,\n"
				+ "               round_new(avg(l.aar),2) aar,\n"
				+ "               round_new(avg(l.ad),2) ad,\n"
				+ "               round_new(avg(l.vdaf),2) vdaf,\n"
				+ "               round_new(avg(l.mt),2) mt,\n"
				+ "               round_new(avg(l.stad),2) stad,\n"
				+ "               round_new(avg(l.aad),2) aad,\n"
				+ "               round_new(avg(l.mad),2) mad,\n"
				+ "               round_new(avg(l.qbad),3) qbad,\n"
				+ "               round_new(avg(l.had),2) had,\n"
				+ "               round_new(avg(l.vad),2) vad,\n"
				+ "               round_new(avg(l.fcad),2) fcad,\n"
				+ "               round_new(avg(l.std),2) std,\n"
				+ "               round_new(avg(l.qgrad),3) qgrad,\n"
				+ "               round_new(avg(l.hdaf),2) hdaf,\n"
				+ "               round_new(avg(l.qgrad_daf),3) qgrad_daf,\n"
				+ "               round_new(avg(l.sdaf),2) sdaf,\n"
				+ "               round_new(avg(l.t1),0) t1,\n"
				+ "               round_new(avg(l.t2),0) t2,\n"
				+ "               round_new(avg(l.t3),0) t3,\n"
				+ "               round_new(avg(l.t4),0) t4\n"
				+ "          from zhillsb l\n"
				+ "         where id in ("
				+ Bianh
				+ ")\n"
				+ "         group by l.zhilb_id, l.huaysj) z,\n"
				+ "       caiyb c,\n"
				+ "       (select sum(laimsl) as jingz,sum(ches) as ches, meikxxb_id, zhilb_id, pinzb_id,faz_id\n"
				/**
				 * huochaoyuan 2009-02-17
				 * 修改上边fahb中取得的数量信息，以前是sum(jingz-koud),修改为根据新的统计量取值(laimsl)
				 */					
				+ "          from fahb\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
				+ " where f.zhilb_id = c.zhilb_id\n"
				+ "   and f.zhilb_id = z.id\n" + "   and c.zhilb_id = z.id\n"
				+ "   and f.meikxxb_id = m.id\n" + "   and f.pinzb_id = p.id   and f.faz_id=cz.id";
		}
		
		return sql;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			riqi = null;
			riqichange = false;
			riq2 = null;
			riq2change = false;
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			setYundxxModel(null);	
			getYundxxModels();		
//			setJincpcValue(null);
//			setJincpcModel(null);
			getSelectData();
		}
		if(riqichange || riq2change){
			riqichange = false;
			riq2change = false;
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			setYundxxModel(null);	
			getYundxxModels();		
//			setJincpcValue(null);
//			setJincpcModel(null);
			getSelectData();
		}
		getSelectData();
	}

}