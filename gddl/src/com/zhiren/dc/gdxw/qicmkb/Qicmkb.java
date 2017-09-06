package com.zhiren.dc.gdxw.qicmkb;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Qicmkb extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}

	
	private String cheph = "";
	public String getCheph(){
		return cheph;
	}
	public void setCheph(String cheph){
		this.cheph = cheph;
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
	
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	
	private void Save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
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
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_CopyButton){
			_CopyButton=false;
			getSelectData1();
		}
	}
	
	
	//查询按钮
	public void getSelectData1() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		
		
		//供应商条件
		long gongysID=this.getMeikdqmcValue().getId();
		String chaxun="";
		
			 chaxun = "select q.id,d.mingc as diancxxb_id,m.mingc as meikxxb_id,\n"
					+ "q.cheph,q.piz,decode(q.zhuangt,1,'使用','停用') as zhuangt,q.lury,q.lursj,q.beiz\n"
					+ " from qicmkb q,meikxxb m ,diancxxb d\n"
					+ "where q.diancxxb_id=d.id\n"
					+ "and q.meikxxb_id=m.id(+)\n"
					+" and q.cheph like '%"+getCheph()+"%'\n"
					+ "order by q.cheph";
		
		
		

		
		//System.out.println(chaxun);
		//System.out.println("----------------------------------------");
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qicmkb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("beiz").setHeader("备注");
		
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
	
		
		
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("piz").setWidth(80);
		egu.getColumn("zhuangt").setWidth(80);
		egu.getColumn("beiz").setWidth(150);
		
		
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(1000);// 设置分页

		// *****************************************设置默认值****************************
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("zhuangt").setDefaultValue("使用");
		egu.getColumn("diancxxb_id").setDefaultValue(
				this.getIDropDownDiancmc(this.getTreeid()));
		// 设置供应商的默认值
		egu.getColumn("meikxxb_id").setDefaultValue(
				this.getMeikdqmcValue().getValue());
		// *************************下拉框*****************************************88
		// 电厂下拉框
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").editor.readOnly=true;
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from diancxxb where jib=3 order by mingc"));
		// 设置供应商的下拉框
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		String GongysSql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));
		//状态
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"使用"));
		list.add(new IDropDownBean(2,"停用"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setDefaultValue("使用");
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("zhuangt").setReturnId(true);

		// ********************工具栏************************************************
	
		// 设置树
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addTbarText("煤矿:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		String sRefreshHandler = 
			"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
			"if (grid_Mrcd.length>0) {" +
			"Ext.MessageBox.confirm('消息提示','刷新界面后您所做的更改将不被保存,是否继续?',function(btn){" +
			"if (btn == 'yes') {" +
			"document.getElementById('RefreshButton').click();" +
			"}" +
			"})" +
			"}else {document.getElementById('RefreshButton').click();}" +
			"}";
		GridButton gRefresh = new GridButton("刷新",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//egu.addToolbarItem("{"+new GridButton("复制去年计划","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
		egu.addTbarText("->");
		egu.addTbarText("车号查询:");
		TextField tf=new TextField();
		tf.setWidth(80);
		tf.setValue(getCheph());
		//tf.setId("Zhiybm");
		tf.setListeners("change:function(own,n,o){document.getElementById('Cheph').value = n}");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		/*GridButton gRefresh2 = new GridButton("查询",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh2);*/
		egu.addToolbarItem("{"+new GridButton("查询","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
		

		setExtGrid(egu);
		con.Close();

		
		
	}


	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		
		
		//供应商条件
		long gongysID=this.getMeikdqmcValue().getId();
		String chaxun="";
		
			 chaxun = "select q.id,d.mingc as diancxxb_id,m.mingc as meikxxb_id,\n"
					+ "q.cheph,q.piz,decode(q.zhuangt,1,'使用','停用') as zhuangt,q.lury,q.lursj,q.beiz\n"
					+ " from qicmkb q,meikxxb m ,diancxxb d\n"
					+ "where q.diancxxb_id=d.id\n"
					+ "and q.meikxxb_id=m.id(+)\n"
					+" and q.meikxxb_id="+gongysID+"\n"
					+ "order by q.cheph";
	
		
		

		
		
		//System.out.println(chaxun);
		//System.out.println("----------------------------------------");
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qicmkb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("beiz").setHeader("备注");
		
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
	
		
		
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("piz").setWidth(80);
		egu.getColumn("zhuangt").setWidth(80);
		egu.getColumn("beiz").setWidth(150);
		
		
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(1000);// 设置分页

		// *****************************************设置默认值****************************
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("zhuangt").setDefaultValue("使用");
		egu.getColumn("diancxxb_id").setDefaultValue(
				this.getIDropDownDiancmc(this.getTreeid()));
		// 设置供应商的默认值
		egu.getColumn("meikxxb_id").setDefaultValue(
				this.getMeikdqmcValue().getValue());
		// *************************下拉框*****************************************88
		// 电厂下拉框
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").editor.readOnly=true;
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from diancxxb where jib=3 order by mingc"));
		// 设置供应商的下拉框
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		String GongysSql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));
		//状态
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"使用"));
		list.add(new IDropDownBean(2,"停用"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setDefaultValue("使用");
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("zhuangt").setReturnId(true);

		// ********************工具栏************************************************
	
		// 设置树
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addTbarText("煤矿:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		String sRefreshHandler = 
			"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
			"if (grid_Mrcd.length>0) {" +
			"Ext.MessageBox.confirm('消息提示','刷新界面后您所做的更改将不被保存,是否继续?',function(btn){" +
			"if (btn == 'yes') {" +
			"document.getElementById('RefreshButton').click();" +
			"}" +
			"})" +
			"}else {document.getElementById('RefreshButton').click();}" +
			"}";
		GridButton gRefresh = new GridButton("刷新",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//egu.addToolbarItem("{"+new GridButton("复制去年计划","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
		egu.addTbarText("->");
		egu.addTbarText("车号查询:");
		TextField tf=new TextField();
		tf.setWidth(80);
		tf.setValue(getCheph());
		//tf.setId("Zhiybm");
		tf.setListeners("change:function(own,n,o){document.getElementById('Cheph').value = n}");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		/*GridButton gRefresh2 = new GridButton("查询",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh2);*/
		egu.addToolbarItem("{"+new GridButton("查询","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
		

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			this.setTreeid(null);
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			this.setCheph("");
			getSelectData();
		}
		if(_meikdqmcchange){
			getSelectData();
		}
			
		
		
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
			sql = "select id,mingc from meikxxb order by mingc";
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
