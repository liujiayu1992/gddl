package com.zhiren.gangkjy.zhilgl.huayxx;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dtrlgs.faygl.faygs.FaycbBean;
import com.zhiren.dtrlgs.faygl.faygs.FayzgInfo;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumcbBean;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumzgInfo;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhidsjcyhy extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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
		// 卸煤日期的ora字符串格式
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		// 电厂ID
//		long dcid = visit.getDiancxxb_id();
		String dcid=this.getTreeid();
		String sql = 
			"select z.id, /*'cybh' bianm,*/ g.mingc fahr,d.mingc YUANSHDWB_ID, p.mingc pinz,\n" +
			"f.chec,f.daohrq jiexrq, f.ches,  f.biaoz, f.jingz, z.mt, z.mad, z.aar,\n" + 
			"z.aad, z.ad, z.var, z.vad, z.vdaf, z.fcad, z.qgrad,\n" + 
			"z.qgrd, z.qnet_ar, z.stad, z.std, z.qbad, z.har, z.had,\n" + 
			"z.hdaf, z.huaysj, z.huaybh, z.huayy, z.beiz,z.shenhzt,f.id fahbtmp_id\n" + 
			"from fahbtmp f, zhillsb z," +
//			" caiyb c, caiylbb l," +
			" vwfahr g,\n" + 
			" diancxxb d,\n"+
			"vwpinz p\n" + 
			"where f.zhilb_id = z.zhilb_id(+)   \n" +
			   " and f.leix_id=3 and f.shujly='手工录入'"+
//			"and z.caiyb_id = c.id\n" + 
//			"and c.caiylbb_id = l.id and l.mingc = '进厂化验'\n" + 
			"and f.gongysb_id = g.id and f.pinzb_id = p.id and d.id=f.yuanshdwb_id\n" ;
		       
//			"   and f.diancxxb_id = " + dcid +
//			if(!this.getTreeid().equals("199")){
		        sql+="and f.diancxxb_id="+this.getTreeid()+"\n";
//	             } 
			sql+="   and f.daohrq >= " + strxmrqOra + " and f.daohrq < "
			+ strxmrqeOra + "+1 order by jiexrq desc";

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
//		egu.getColumn("bianm").setHeader(Local.bianm);
		egu.getColumn("fahr").setHeader(Local.fahr);
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("chec").setHeader(Local.chec);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("jiexrq").setHeader(Local.jiexrq);
		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("jingz").setHeader(Local.jingz);
		egu.getColumn("mt").setHeader(Local.Mt);
		egu.getColumn("mad").setHeader(Local.Mad);
		egu.getColumn("aad").setHeader(Local.Aad);
		egu.getColumn("vdaf").setHeader(Local.Vdaf);
		egu.getColumn("had").setHeader(Local.Had);
		egu.getColumn("stad").setHeader(Local.Stad);
		// 设置grid列宽度
//		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("fahr").setWidth(100);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jiexrq").setWidth(110);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("had").setWidth(60);
		egu.getColumn("stad").setWidth(60);
//		设置grid隐藏列
		egu.getColumn("jingz").setHidden(true);
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
		egu.getColumn("qgrd").setHidden(true);
		egu.getColumn("qnet_ar").setHidden(true);
		//egu.getColumn("stad").setHidden(true);
		egu.getColumn("std").setHidden(true);
		egu.getColumn("qbad").setHidden(true);
		egu.getColumn("har").setHidden(true);
		//egu.getColumn("had").setHidden(true);
		egu.getColumn("hdaf").setHidden(true);
		egu.getColumn("huaysj").setHidden(true);
		egu.getColumn("huaybh").setHidden(true);
		egu.getColumn("huayy").setHidden(true);
		egu.getColumn("beiz").setHidden(true);
		
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
			sql+=//"delete caiyb where zhilb_id=(select zhilb_id from zhillsb where id="+change+"); \n"+
				"update fahbtmp set zhilb_id=0 where zhilb_id=(select zhilb_id from zhillsb where id="+change+"); \n"+
				"update fahb set zhilb_id=0 where zhilb_id=(select zhilb_id from zhillsb where id="+change+"); \n"+
				"update fayslb set zhilb_id=0 where zhilb_id=(select zhilb_id from zhillsb where id="+change+");"+
				"delete zhillsb where id="+change+"; \n";
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
		String strZlid = "";
		List fhlist = new ArrayList();
		List fylist = new ArrayList();
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		sql = "begin\n";
		while(rsl.next()){
			String id=rsl.getString("id");
			if(rsl.getLong("id")<=0){
				String zhilbid=MainGlobal.getNewID(visit.getDiancxxb_id());
				id=zhilbid;
//				质量表id
				String new_zhilb_id=MainGlobal.getNewID(visit.getDiancxxb_id());
				
				if(strZlid.equals("")){
					strZlid = new_zhilb_id;
				}else{
					strZlid = strZlid+","+new_zhilb_id;
				}
				
				sql+="insert into zhillsb (id,huaybh,huaysj,huayy,beiz,mt,mad,aar,aad,ad,var,vad,vdaf,fcad,qgrad,qgrd,qnet_ar,stad,std,qbad,had,hdaf,lury,zhilb_id,shenhzt,shenhry)values" +
						"("+zhilbid+",'" + rsl.getString("huaybh") +"'," + DateUtil.FormatOracleDate(rsl.getString("huaysj")) +",'" + rsl.getString("huayy") + "','"
						+ rsl.getString("beiz") +"'," + rsl.getDouble("mt") +"," + rsl.getDouble("mad") + "," + rsl.getDouble("aar") 
						+"," + rsl.getDouble("aad") +"," + rsl.getDouble("ad") +"," + rsl.getDouble("var") 
						+"," + rsl.getDouble("vad") +"," + rsl.getDouble("vdaf") +"," + rsl.getDouble("fcad") 
						+"," + rsl.getDouble("qgrad") +"," + rsl.getDouble("qgrd")  
					    +"," + rsl.getDouble("qnet_ar") +"," + rsl.getDouble("stad") 
						+"," + rsl.getDouble("std") +"," + rsl.getDouble("qbad") +"," + rsl.getDouble("had") 
						+"," + rsl.getDouble("hdaf") +",'" + visit.getRenymc() +"',"+new_zhilb_id+",7,'"+visit.getRenymc()+"');";
				
				sql+="\n update fahb set zhilb_id="+new_zhilb_id+" where id=(select fahb_id from fahbtmp where id="+rsl.getString("fahbtmp_id")+");";
				sql+="\n update fayslb set zhilb_id="+new_zhilb_id+" where id=(select fayslb_id from fahbtmp where id="+rsl.getString("fahbtmp_id")+");";
				sql+="\n update fahbtmp set zhilb_id="+new_zhilb_id+" where id="+rsl.getString("fahbtmp_id")+";\n";
				
				sql+="insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)values("
					+new_zhilb_id+",'" + rsl.getString("huaybh") +"',0," + DateUtil.FormatOracleDate(rsl.getString("huaysj")) 
					
					+","+rsl.getString("qnet_ar") + ","
					+ rsl.getString("aar") + ", " + rsl.getString("ad") + "," + rsl.getString("vdaf") + ","
					 + rsl.getString("mt") + "," + rsl.getString("stad") + "," + rsl.getString("aad") + ","
					 + rsl.getString("mad") + "," + rsl.getString("qbad") + "," + rsl.getString("had") + ","
					 + rsl.getString("vad") + "," + rsl.getString("fcad") + "," + rsl.getString("std") + ","
					 + rsl.getString("qgrad") + "," + rsl.getString("hdaf") + "," + rsl.getString("qgrad_daf") + ","
					 + rsl.getString("sdaf") + "," + rsl.getString("t1") + "," + rsl.getString("t2") + ","
					 + rsl.getString("t3") + "," + rsl.getString("t4") + ",'" + rsl.getString("huayy") + "','"
					 + rsl.getString("lury") + "','" + rsl.getString("beiz") + "',1,1);\n";
					
			}
			else{
				
				sql += "update zhillsb set huaybh ='" + rsl.getString("huaybh") +
					"', huaysj = " + DateUtil.FormatOracleDate(rsl.getString("huaysj")) +
					", huayy ='" + rsl.getString("huayy") + 
					"', beiz ='" + rsl.getString("beiz") +
					"', mt = " + rsl.getDouble("mt") + 
					", mad = " + rsl.getDouble("mad") + 
					", aar = " + rsl.getDouble("aar") +
					", aad = " + rsl.getDouble("aad") +
					", ad = " + rsl.getDouble("ad") +
					", var = " + rsl.getDouble("var") +
					", vad = " + rsl.getDouble("vad") + 
					", vdaf = " + rsl.getDouble("vdaf") + 
					", fcad = " + rsl.getDouble("fcad") +
					", qgrad = " + rsl.getDouble("qgrad") +
					", qgrd = " + rsl.getDouble("qgrd") +
					", qnet_ar = " + rsl.getDouble("qnet_ar") +
					", stad = " + rsl.getDouble("stad") +
					", std = " + rsl.getDouble("std") +
					", qbad = " + rsl.getDouble("qbad") +
					", had = " + rsl.getDouble("had") +
					", hdaf = " + rsl.getDouble("hdaf") +
					", lury = '" + visit.getRenymc() +
					"'  where id = " + rsl.getString("id") + ";\n";
				
				sql += "update zhilb set huaybh ='" + rsl.getString("huaybh") +
					"', huaysj = " + DateUtil.FormatOracleDate(rsl.getString("huaysj")) +
					", huayy ='" + rsl.getString("huayy") + 
					"', beiz ='" + rsl.getString("beiz") +
					"', mt = " + rsl.getDouble("mt") + 
					", mad = " + rsl.getDouble("mad") + 
					", aar = " + rsl.getDouble("aar") +
					", aad = " + rsl.getDouble("aad") +
					", ad = " + rsl.getDouble("ad") +
					", var = " + rsl.getDouble("var") +
					", vad = " + rsl.getDouble("vad") + 
					", vdaf = " + rsl.getDouble("vdaf") + 
					", fcad = " + rsl.getDouble("fcad") +
					", qgrad = " + rsl.getDouble("qgrad") +
					", qgrd = " + rsl.getDouble("qgrd") +
					", qnet_ar = " + rsl.getDouble("qnet_ar") +
					", stad = " + rsl.getDouble("stad") +
					", std = " + rsl.getDouble("std") +
					", qbad = " + rsl.getDouble("qbad") +
					", had = " + rsl.getDouble("had") +
					", hdaf = " + rsl.getDouble("hdaf") +
					", lury = '" + visit.getRenymc() +
					"'  where id = " + rsl.getString("zhilb_id") + ";\n";
				
				if(strZlid.equals("")){
					strZlid = rsl.getString("zhilb_id");
				}else{
					strZlid = strZlid+","+rsl.getString("zhilb_id");
				}
				
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
			}else{
				con.commit();
				
				String fhSql = "select id,diancxxb_id from fahb where zhilb_id in (" + strZlid +")";
				rsl = con.getResultSetList(fhSql);
				while (rsl.next()) {
					fhlist.add(new ShoumcbBean(rsl.getLong("diancxxb_id"),rsl.getLong("id")));
				}
				rsl.close();
				
				String fySql = "select id,diancxxb_id from fayslb where zhilb_id in (" + strZlid +")";
				rsl = con.getResultSetList(fySql);
				while (rsl.next()) {
					fylist.add(new FaycbBean(rsl.getLong("diancxxb_id"),rsl.getLong("id")));
				}
				rsl.close();
				ShoumzgInfo.CountChengb(fhlist,false);
				FayzgInfo.CountChengb(fylist,false);
			}
		}
		rsl.close();
//		con.commit();
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