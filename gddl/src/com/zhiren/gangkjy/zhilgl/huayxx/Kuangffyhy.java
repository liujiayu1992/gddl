package com.zhiren.gangkjy.zhilgl.huayxx;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuangffyhy extends BasePage implements PageValidateListener {
	// 界面用户提示
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

	// 页面刷新日期（卸煤日期）
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	 页面刷新日期（卸煤日期）
	private String riqe;

	public String getRiqe() {
		return riqe;
	}

	public void setRiqe(String riqe) {
		this.riqe = riqe;
	}
//	与电厂树有关
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
		return getExtGrid().getDataScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private boolean _UpdateClick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateClick = true;
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if(_UpdateClick){
			_UpdateClick = false;
			((Visit) getPage().getVisit()).setString7(getRiq());
			((Visit) getPage().getVisit()).setString8(getRiqe());
			cycle.activate("Ruccyxg");
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if(_DelClick){
			_DelClick=false;
			Del();
			initGrid();
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 电厂ID
//		long dcid = visit.getDiancxxb_id();
		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = " and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";
		} 
		String sql ="select z.id,fy.id as fid, g.mingc gongys,\n"
			+"fy.chec, p.mingc pinz,fy.meil,fy.chukjssj,\n"
			+"z.mt, z.mad, z.aar, \n"
			+"z.aad, z.ad, z.var, z.vad, z.vdaf, z.fcad, z.qgrad,\n" 
			+"z.qnet_ar, z.stad, z.std, z.qbad, z.had,\n" 
			+"z.hdaf\n"
			+"from fayslb fy,kuangfzlb z,gongysb g,vwpinz p,diancxxb dc\n" 
			+"where fy.kuangfzlb_id=z.id(+) and fy.diancxxb_id=dc.id \n"
		    +"and fy.gongysb_id=g.id and fy.XIAOSJSB_ID=0 \n" 
			+"and fy.pinzb_id=p.id(+)\n"
			+"and fy.yewlxb_id<>(select id from yewlxb where mingc='直达') \n " 
			+ str 
			+"   and fy.chukjssj >= " + DateUtil.FormatOracleDate(getRiq()) + " and fy.chukjssj < \n"
			 + DateUtil.FormatOracleDate(getRiqe()) + "+1\n";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置grid可编辑
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid列标题
//		egu.getColumn("fahr").setHeader(Local.fahr);
//		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("chec").setHeader(Local.chec);
//		egu.getColumn("ches").setHeader(Local.ches);
//		egu.getColumn("jiexrq").setHeader(Local.jiexrq);
//		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("mt").setHeader(Local.Mt);
		egu.getColumn("mad").setHeader(Local.Mad);
		egu.getColumn("aad").setHeader(Local.Aad);
		egu.getColumn("vdaf").setHeader(Local.Vdaf);
		egu.getColumn("had").setHeader(Local.Had);
		egu.getColumn("stad").setHeader(Local.Stad);
		// 设置grid列宽度
//		egu.getColumn("fahr").setWidth(100);
//		egu.getColumn("meikdw").setWidth(100);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("chec").setWidth(60);
//		egu.getColumn("ches").setWidth(60);
//		egu.getColumn("biaoz").setWidth(60);
//		egu.getColumn("jiexrq").setWidth(110);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("had").setWidth(60);
		egu.getColumn("stad").setWidth(60);
//		设置grid隐藏列
		egu.getColumn("FID").setHidden(true);
		//egu.getColumn("mt").setHidden(true);
		//egu.getColumn("mad").setHidden(true);
		egu.getColumn("aar").setHidden(true);
		//egu.getColumn("aad").setHidden(true);
		egu.getColumn("ad").setHidden(true);
		egu.getColumn("var").setHidden(true);
		egu.getColumn("vad").setHidden(true);
		//egu.getColumn("vdaf").setHidden(true);
		egu.getColumn("fcad").setHidden(true);
		egu.getColumn("qgrad").setHidden(true);
		egu.getColumn("qnet_ar").setHidden(true);
		//egu.getColumn("stad").setHidden(true);
		egu.getColumn("std").setHidden(true);
		egu.getColumn("qbad").setHidden(true);
		//egu.getColumn("had").setHidden(true);
		egu.getColumn("hdaf").setHidden(true);
		
		// 接卸日期选择
		egu.addTbarText(Local.jiexrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		 接卸日期选择
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// 与html页中的id绑定,并自动刷新
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
//		设置电厂树
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		
	
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		// 详细过衡按钮
		GridButton Create = new GridButton("编辑", "EditValue", SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(Create);
		// 详细过衡按钮
		GridButton Update = new GridButton(GridButton.ButtonType_Save, 
				"gridDiv", egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(Update);

		setExtGrid(egu);
		con.Close();

	}

	private void Del() {
		JDBCcon con = new JDBCcon();
//		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		String change=getChange();
		String sql="begin \n" ;
//		while(rsl.next()){
			sql+="update fayslb set kuangfzlb_id=0 where kuangfzlb_id="+change+"; \n"+
				"delete kuangfzlb where id="+change+"; \n";
//		}
		sql+="end;";
		int flag = con.getUpdate(sql);
		if(flag == -1){
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.UpdateDatabaseFail);
			con.rollBack();
			return;
		}
		con.Close();
		setMsg("成功删除！");
	}
	
	private void save() {
		Visit visit = (Visit)getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有未提交改动！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql;
		int flag;
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		sql = "begin\n";
		while(rsl.next()){
			int id=rsl.getInt("ID"); 
			long fid=rsl.getLong("FID");
			double mt=rsl.getDouble("MT");
			double mad=rsl.getDouble("MAD");
			double aar=rsl.getDouble("AAR");
			double aad=rsl.getDouble("AAD");
			double ad=rsl.getDouble("AD");
			double var=rsl.getDouble("VAR");
			double vad=rsl.getDouble("VAD");
			double vdaf=rsl.getDouble("VDAF");
			double fcad=rsl.getDouble("FCAD");
			double qgrad=rsl.getDouble("QGRAD");
			double qnet=rsl.getDouble("QNET_AR");
			double stad=rsl.getDouble("STAD");
			double std=rsl.getDouble("STD");
			double qbad=rsl.getDouble("QBAD");
			double had=rsl.getDouble("HAD");
			double hdaf=rsl.getDouble("HDAF");
			String newID=MainGlobal.getNewID(visit.getDiancxxb_id());
			if(id==0){
				sql+="insert into kuangfzlb (id,meikxxb_id,gongysb_id,mt, mad, aar,aad, " +
						"ad, var, vad, vdaf, fcad, qgrad, qnet_ar, stad, std, qbad, had,hdaf,LURY)values("+
						newID+",0,"+
						"(select id from gongysb where mingc='"+rsl.getString("GONGYS")+"'),"+
						mt+","+mad+","+aar+","+aad+","+ad+","+var+","+vad+","+vdaf+","+fcad+","+
						qgrad+","+qnet+","+stad+","+std+","+qbad+","+had+","+hdaf+",'"+visit.getRenymc()+"');\n";
				sql+="update fayslb set kuangfzlb_id="+newID+" where id="+fid+";\n";
				
			}else{
				sql += "update kuangfzlb set mt = " + rsl.getDouble("mt") + 
				", mad = " + mad + 		", aar = " + aar +
				", aad = " + aad +		", ad = " + ad +
				", var = " + var +		", vad = " + vad + 
				", vdaf = " + vdaf + 	", fcad = " + fcad +
				", qgrad = " + qgrad +	", qnet_ar = " + qnet +
				", stad = " + stad +	", std = " + std +
				", qbad = " + qbad +	", had = " + had +
				", hdaf = " + hdaf +
				", lury = '" + visit.getRenymc() +"'  where id = " + rsl.getString("id") + ";\n";
			}
		}
		if(rsl.getRows()>0){
			sql += "end;\n";
			flag = con.getUpdate(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}
	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if (getRiqe() == null) {
				setRiqe(DateUtil.FormatDate(new Date()));
			}
			visit.setActivePageName(getPageName().toString());
			this.setTreeid("");
		}
		init();
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
}