package com.zhiren.jt.gongys;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2012-06-20
 * 描述：修正页面逻辑存在的BUG
 */
/*
 * 作者：夏峥
 * 时间：2012-07-16
 * 描述：新增煤矿地区时,	保存时同时保存至供应商表中
 * 		 点击保存后验证编码是否小于6位，如果小于则不能保存
 */


public class Meikdq_gd extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	private String id;  //记录前台传递过来的id
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = MainGlobal.getExtMessageBox(saveMsg, false);;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setSaveMsg("");
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	省份IDropDownModel
	public IPropertySelectionModel getShengfModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setShengfModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setShengfModels() {
		String sql = "select sf.id, sf.quanc from shengfb sf";
		setShengfModel(new IDropDownModel(sql));
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		SaveMsg = "";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next())	{

//			初始化并赋值保存时所需变量
			String meikdq_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			String xuh=mdrsl.getString("XUH");
			String BIANM=mdrsl.getString("BIANM");
			String MINGC=mdrsl.getString("MINGC");
			String QUANC=mdrsl.getString("QUANC");
			long SHENGFB_ID =((IDropDownModel)getShengfModel()).getBeanId(mdrsl.getString("SHENGFB_ID"));
			if(BIANM.length()<6){
				SaveMsg += "----------记录---------<br>--编码:"
					+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
					+ "<br>的记录编码小于6位,不能保存!";
				continue;
			}
			String sql_check = "select id from meikdqb where (1=0 or bianm='"+BIANM+"' or mingc='"+MINGC+"' OR QUANC='"+QUANC+"')";

			if ("0".equals(mdrsl.getString("ID"))) {
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------记录---------<br>--编码:"
						+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
						+ "<br>的记录有重复,不能保存!";
					continue;
				}
				sql.append("begin \n");
//				插入到供应商表中
				sql.append("INSERT INTO MEIKDQB\n" );
				sql.append("  (ID, XUH, MINGC, QUANC, BIANM,SHENGFB_ID,ZHUANGT)\n");
				sql.append("VALUES\n");
				sql.append("  ("+meikdq_id+","+xuh+",'"+MINGC+"','"+QUANC+"','"+BIANM+"',"+SHENGFB_ID+",1);\n");
				sql.append("INSERT INTO GONGYSB\n" );
				sql.append("  (ID, FUID, MINGC, QUANC, BIANM, SHENGFB_ID, BEIZ, LEIX, ZHUANGT)\n" );
				sql.append("VALUES\n" );
				sql.append(" ("+meikdq_id+", "+meikdq_id+", '"+MINGC+"', '"+QUANC+"', '"+BIANM+"', "+SHENGFB_ID+", '原地区', 0, 0);\n" );
				sql.append("end;" );
			}else{
				sql_check+="and id<>"+mdrsl.getString("ID");
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------记录---------<br>--编码:"
						+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
						+ "<br>的记录有重复,不能保存!";
					continue;
				}
				sql.append("begin \n");
//				更新煤款信息表
				sql.append("UPDATE MEIKDQB\n" );
				sql.append("   SET XUH        = '"+xuh+"',\n" );
				sql.append("       BIANM      = '"+BIANM+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+"\n" );
				sql.append("    WHERE ID="+mdrsl.getString("ID")+";\n");
				
				sql.append("UPDATE GONGYSB\n" );
				sql.append("   SET BIANM      = '"+BIANM+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+"\n" );
				sql.append("    WHERE ID="+mdrsl.getString("ID")+";\n");
				sql.append("end;" );
			}
			int flag=con.getUpdate(sql.toString());
			if(flag==-1){
				SaveMsg += "----------记录---------<br>--编码:"
					+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
					+ "<br>保存失败!";
			}
		}
		
		if(SaveMsg.length()>10){
			setSaveMsg(SaveMsg);
		}else{
			setSaveMsg("保存成功");
		}
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	private boolean _BeginChick=false;
	
	public void BeginButtonQY(IRequestCycle cycle){
		_BeginChick = true;
	}
	
	private boolean _StopChick = false;
	
	public void StopButtonTY(IRequestCycle cycle){
		_StopChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
//	添加已有煤矿地区
	private boolean _GuanlChick = false;
	
    public void GuanlButton(IRequestCycle cycle) {
    	_GuanlChick = true;
    }
    
//  有可能从返回按钮返回本页面，也可能是从添加所选按钮返回本页面，标记作用
	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}
	
//	从添加已有页面传过来的已选择的煤矿地区信息
    private String DataSource;
	
	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}
	
	public void submit(IRequestCycle cycle) {
		ToAddMsg="";
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
		}
		if(_BeginChick){
			_BeginChick = false;
			Begin();
			getSelectData();
		}
		if(_StopChick){
			_StopChick = false;
			Stop();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_GuanlChick){
			_GuanlChick = false;
			cycle.activate("Meikdq_gdTj");
		}
	}

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str1 ="";
		if(getShiyztValue().getId() == 1){
			str1="and m.zhuangt = 1 \n";
		}else{
			str1="and m.zhuangt = 0 \n";
		}
		
		String sql = 
			"SELECT M.ID, M.XUH, M.MINGC, M.QUANC, M.BIANM, S.QUANC SHENGFB_ID\n" +
			"  FROM MEIKDQB M, SHENGFB S\n" + 
			" WHERE S.ID = M.SHENGFB_ID \n"+
			str1 +
			" ORDER BY M.XUH";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikdqb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(80);
		
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(150);

		
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(95);
		egu.getColumn("bianm").editor.allowBlank = false;

		egu.getColumn("SHENGFB_ID").setHeader("省份");
		egu.getColumn("SHENGFB_ID").setWidth(80);
		egu.getColumn("SHENGFB_ID").editor.setAllowBlank(false);
		
//		默认每页显示25行
		egu.addPaging(25);

		egu.addTbarText("使用状态:");
		ComboBox cb = new ComboBox();
		cb.setTransform("SHIYZT");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(cb.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新",
		"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addTbarText("-");
		String str = MainGlobal.getXitxx_item("系统信息", "是否显示煤矿地区的添加按钮", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");

		String shengf_click = 
			"gridDiv_grid.on('cellclick',\n" +
			"function(own, irow, icol, e){\n" + 
			"    row = irow;\n" + 
			"    if('SHENGFB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){\n" + 
			"        shengfTree_window.show();\n" + 
			"    }\n" + 
			"});";		
		
		if (str.equals("否")) {
			egu.setGridType(ExtGridUtil.Gridstyle_Read);
		    egu.addToolbarItem("{"+new GridButton("添加已有煤矿地区","function(){document.getElementById('GuanlButton').click();}").getScript()+"}");
		} else {
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addOtherScript(shengf_click);
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		}
		
		if(getShiyztValue().getId() == 0){
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("启用","function(){\n"+
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('提示信息','请先保存记录');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.BeginButtonQY.click();}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('提示信息','请先选择记录');\n"+
						"}}"));
		}else{
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("停用","function(){" +
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('提示信息','请先保存记录');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.StopButtonTY.click();}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('提示信息','请先选择记录');\n"+
						"	}}"));
		}

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		

		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");

		DefaultTree dt = new DefaultTree(DefaultTree.tree_sf_cs, "shengfTree", ""+visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		String handler = 
			"function() {\n" +
			"    var cks = shengfTree_treePanel.getSelectionModel().getSelectedNode();\n" + 
			"    if(cks==null){\n" + 
			"        shengfTree_window.hide();\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    rec = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"    if(cks.getDepth() == 2){\n" + 
			"        rec.set('SHENGFB_ID', cks.parentNode.text);\n" + 
			"    }else if(cks.getDepth() == 1){\n" + 
			"        rec.set('SHENGFB_ID', cks.text);\n" +
			"    }\n" + 
			"    shengfTree_window.hide();\n" + 
			"    return;\n" + 
			"}";
		
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);

		
//		从添加所选按钮回来给予的提示信息
		if(ToAddMsg.equals("toAdd")){
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("\nvar p=new gridDiv_plant("+ recs[i] +");\ngridDiv_ds.insert("+ i +",p);");
				sb.append("\n").append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
		}
		
		
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
	
	public String getTreeScript() {
//		System.out.print(((Visit)this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			setShengfModel(null);
			setShengfModels();
			setShiyztValue(null);
			setShiyztModel(null);
			ToAddMsg=cycle.getRequestContext().getRequest().getParameter("MsgAdd");
			
			if(ToAddMsg==null){
				ToAddMsg="";
			}
			DataSource = visit.getString12();
			getSelectData();
		} else {
			getSelectData();
		}
	}
		
		//使用下拉框
		public IDropDownBean getShiyztValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getShiyztModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setShiyztValue(IDropDownBean value) {

			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}

		public void setShiyztModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getShiyztModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getShiyztModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public IPropertySelectionModel getShiyztModels() {
			List shiyzt = new ArrayList();
			shiyzt.add(new IDropDownBean(1, "使用中"));
			shiyzt.add(new IDropDownBean(0, "未使用"));
			((Visit) getPage().getVisit())
					.setProSelectionModel5(new IDropDownModel(shiyzt));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}
		
	//启用的方法	
		public void Begin(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "update MEIKDQB set zhuangt = 1 where id ="+getChange();
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("启用成功!");
				}
				else{
					setSaveMsg("启用失败!");
				}
		}
	//停用的方法	
		public void Stop(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql =  "update MEIKDQB set zhuangt = 0 where id ="+getChange();
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("停用成功!");
				}
				else{
					setSaveMsg("停用失败!");
				}
		}
		
	}

