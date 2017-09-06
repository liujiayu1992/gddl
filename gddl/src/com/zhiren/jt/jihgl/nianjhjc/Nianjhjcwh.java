package com.zhiren.jt.jihgl.nianjhjc;

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
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Nianjhjcwh extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sSQL = "";
		StringBuffer sbuffer = new StringBuffer();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		ResultSetList rsl;
		
		rsl = getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()) {
			sbuffer.delete(0, sbuffer.length());
			sbuffer.append("delete from nianjhjcb where id = " + rsl.getString("id")+"\n");
			flag = con.getDelete(sbuffer.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + sbuffer);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		String strYear = "";
		String sNianjhjchzb_id = "";
		//保存更新或新增
		while (rsl.next()){
			sbuffer.delete(0, sbuffer.length());
			sSQL = "";
			strYear=getNianfValue().getValue() +"-01-01";
			ResultSetList rs = con.getResultSetList(
					"SELECT id FROM nianjhjchzb WHERE to_char(riq,'yyyy')='" 
						+ getNianfValue().getValue() + "' AND diancxxb_id=" 
						+ getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"))
					);
			
			if (rs.next()) {
				sNianjhjchzb_id = rs.getString("id");
			} else {
				sNianjhjchzb_id = MainGlobal.getNewID(visit.getDiancxxb_id());
				sSQL = "INSERT INTO NIANJHJCHZB (ID, RIQ, DIANCXXB_ID) VALUES ("
					+ sNianjhjchzb_id + ",to_date('" + strYear + "','yyyy-mm-dd')"
					+ "," + getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id")) + ")";
				flag = con.getInsert(sSQL);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sSQL);
					setMsg(ErrorMessage.InsertDatabaseFail);
					return;
				}
			}
			if ("0".equals(rsl.getString("id"))) {
				sbuffer.append(
						"INSERT INTO NIANJHJCB\n" +
						"  (ID,\n" + 
						"   NIANJHJCHZB_ID,\n" + 
						"   GONGYSB_ID,\n" + 
						"   FAZ_ID,\n" + 
						"   NIANJH_DUN,\n" + 
						"   NIANJH_CHE,\n" + 
						"   NIANJH_BUHSBMDJ,\n" + 
						"   ZHEHMY_DUN,\n" + 
						"   ZHEHMY_CHE,\n" + 
						"   ZHEHMY_BUHSBMDJ)\n" + 
						"VALUES\n" + 
						"  (\n" + 
						"getnewid(" + visit.getDiancxxb_id() + "),\n" +
						sNianjhjchzb_id + ",\n" +
						getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsl.getString("gongysb_id")) + ",\n" +
						getExtGrid().getColumn("faz_id").combo.getBeanId(rsl.getString("faz_id")) + ",\n" +
						rsl.getString("nianjh_dun") + ",\n" +
						rsl.getString("nianjh_che") + ",\n" +
						rsl.getString("nianjh_buhsbmdj") + ",\n" +
						rsl.getString("zhehmy_dun") + ",\n" +
						rsl.getString("zhehmy_che") + ",\n" +
						rsl.getString("zhehmy_buhsbmdj") + "\n" +
						"\n" + 
						"   )"
					);
				flag = con.getInsert(sbuffer.toString());
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sbuffer);
					setMsg(ErrorMessage.InsertDatabaseFail);
					return;
				}
			} else {
				sbuffer.append(
						"UPDATE nianjhjcb SET nianjhjchzb_id=" + sNianjhjchzb_id + "," +
						"gongysb_id=" + getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsl.getString("gongysb_id")) + ",\n" +
						"faz_id=" + getExtGrid().getColumn("faz_id").combo.getBeanId(rsl.getString("faz_id")) + ",\n" +
						"nianjh_dun= " + rsl.getString("nianjh_dun") + ",nianjh_che=" + rsl.getString("nianjh_che") + ",nianjh_buhsbmdj=" + rsl.getString("nianjh_buhsbmdj") + ",\n" + 
						"zhehmy_dun=" + rsl.getString("zhehmy_dun") + ",zhehmy_che=" + rsl.getString("zhehmy_che") + ",zhehmy_buhsbmdj=" + rsl.getString("zhehmy_buhsbmdj") + "\n" + 
						"WHERE ID="+rsl.getString("id")
					);
				flag = con.getUpdate(sbuffer.toString());
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sbuffer);
					setMsg(ErrorMessage.InsertDatabaseFail);
					return;
				}
			}
			
		}
		con.commit();
		con.Close();
		this.setMsg("保存成功!");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}
	}
	
	public boolean isFuDianc(JDBCcon con) {
		return con.getHasIt("select * from diancxxb where fuid=" + this.getTreeid());
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		String strdiancTreeID = "";
		boolean isShowDianc = false;
		int jib = this.getDiancTreeJib();
		if(jib == 1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib == 2){//选分公司的时候刷新出分公司下所有的电厂
			if (isFuDianc(con)) {
				strdiancTreeID = " and dc.fuid= " +this.getTreeid();
				isShowDianc = true;
			} else {
				strdiancTreeID = " and dc.id= " +this.getTreeid();
			}
			
		}else if (jib == 3){//选电厂只刷新出该电厂
			if (visit.isFencb()) {
				if (isFuDianc(con)) {
					strdiancTreeID = " and dc.fuid= " +this.getTreeid();
					isShowDianc = true;
				} else {
					strdiancTreeID = " and dc.id= " +this.getTreeid();
				}
			} else {
				strdiancTreeID = " and dc.id= " + this.getTreeid();
			}			
		}
		String sSQL = 
			"SELECT jh.id,(select mingc from diancxxb where id=jhz.diancxxb_id) as diancxxb_id,\n" +
			"(SELECT mingc FROM gongysb g WHERE g.id=jh.gongysb_id) AS gongysb_id,\n" +
			"(SELECT f.mingc FROM chezxxb f WHERE f.id=jh.faz_id) AS faz_id,\n" + 
			"  jh.nianjh_dun,jh.nianjh_che,jh.nianjh_buhsbmdj,\n" + 
			"  jh.zhehmy_dun,jh.zhehmy_che,jh.zhehmy_buhsbmdj\n" + 
			"FROM nianjhjcb jh,nianjhjchzb jhz,diancxxb dc\n" + 
			"WHERE jh.nianjhjchzb_id=jhz.id\n" + 
			"  AND to_char(jhz.riq,'yyyy')='" + intyear + "'\n" + 
			"  AND jhz.diancxxb_id=dc.id\n" +
			strdiancTreeID;

		ResultSetList rsl = con.getResultSetList(sSQL);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		egu.setTableName("nianjhjcb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("gongysb_id").setHeader("供应商名称");
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("faz_id").setWidth(80);
		egu.getColumn("nianjh_dun").setHeader("年计划吨数");
		egu.getColumn("nianjh_che").setHeader("年计划车数");
		egu.getColumn("nianjh_buhsbmdj").setHeader("年计划不含税标煤单价（元/吨）");
		egu.getColumn("zhehmy_dun").setHeader("折合每月吨数");
		egu.getColumn("zhehmy_che").setHeader("折合每月车数");
		egu.getColumn("zhehmy_buhsbmdj").setHeader("折合每月不含税标煤单价（元/吨）");
		egu.getColumn("zhehmy_buhsbmdj").setEditor(null);
		egu.getColumn("zhehmy_che").setEditor(null);
		egu.getColumn("zhehmy_dun").setEditor(null);
		
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where 1=1 " + strdiancTreeID));
		if (!isShowDianc) {
			try {
				egu.getColumn("diancxxb_id").setDefaultValue(MainGlobal.getTableCol("diancxxb", "mingc", "id=" + this.getTreeid()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from gongysb order by mingc"));
		
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from chezxxb order by mingc"));

		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		设定不可编辑列的颜色
//		egu.getColumn("zhehmy_dun").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("zhehmy_che").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("zhehmy_buhsbmdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		StringBuffer sb = new StringBuffer();
		String Headers = 

			"		 [\n" +
			"         {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n" + 
			"         {header:'ID', align:'center',rowspan:2},\n" + 
			"         {header:'电厂', align:'center',rowspan:2},\n" + 
			"         {header:'供应商名称', align:'center',rowspan:2},\n" + 
			"         {header:'发站', align:'center',rowspan:2},\n" +
			"         {header:'年计划', colspan:3},\n" + 
			"         {header:'折合本月', colspan:3}\n" + 
			"        ],\n" + 
			"        [\n" + 
			"	      {header:'<table><tr><td width=55 align=center style=border:0>吨数</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>车数</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=85 align=center style=border:0>不含税标煤单价（元/吨）</td></tr></table>', align:'center'},\n" + 
			"	      {header:'<table><tr><td width=55 align=center style=border:0>吨数</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>车数</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=85 align=center style=border:0>不含税标煤单价（元/吨）</td></tr></table>', align:'center'}\n" + 
			"        ]";

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
		
		sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){\n");
		sb.append("   if (e.field=='NIANJH_DUN') {\n");
		sb.append("      var nianjh_dun = Math.round(eval(e.record.get('NIANJH_DUN')||0)/12*100)/100;\n");
		sb.append("      e.record.set('ZHEHMY_DUN', nianjh_dun);\n");
		sb.append("   }\n");
		sb.append("   if (e.field=='NIANJH_CHE') {\n");
		sb.append("      var nianjh_che = Math.round(eval(e.record.get('NIANJH_CHE')||0)/12*100)/100;\n");
		sb.append("      e.record.set('ZHEHMY_CHE', nianjh_che);\n");
		sb.append("   }\n");
		sb.append("   if (e.field=='NIANJH_BUHSBMDJ') {\n");
		sb.append("      var nianjh_buhsbmdj = eval(e.record.get('NIANJH_BUHSBMDJ')||0);\n");
		sb.append("      e.record.set('ZHEHMY_BUHSBMDJ', nianjh_buhsbmdj);\n");
		sb.append("   }\n");
		sb.append("});\n");
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		rsl.close();
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
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
		}
			getSelectData();
	}
//	 年份
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

	
//	 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from gongysb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
//	 得到登陆用户所在电厂或者分公司的名称
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
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		cn.Close();
		return diancmc;

	}
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
}
