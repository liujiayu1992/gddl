package com.zhiren.shanxdted.meikkdxm;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
 * 作者:tzf
 * 时间:2009-11-05
 * 内容:煤矿扣吨项目
 */
public class Meikkdxm extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		
		

		JDBCcon con = new JDBCcon();
		String tableName=this.getExtGrid().tableName;
		
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(this.getChange());
		while (delrsl.next()) {
		
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(this.getChange());
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					
					if(mdrsl.getColumnNames()[i].equalsIgnoreCase("gongysb_id")){
						sql2.append(",").append(
								((IDropDownModel) getGongysModel()).getBeanId(mdrsl
										.getString("gongysb_id")));
					}else if( mdrsl.getColumnNames()[i].equalsIgnoreCase("meikxxb_id") ){
						sql2.append(",").append(
								((IDropDownModel) getMeikModel()).getBeanId(mdrsl
										.getString("meikxxb_id")));
					}else if (mdrsl.getColumnNames()[i].equalsIgnoreCase("koud")) {
						sql2.append(",").append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));		
					}else if (mdrsl.getColumnNames()[i].equalsIgnoreCase("xianzsj")) {
						sql2.append(",").append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));	
					}else {
						sql2.append(",").append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
				
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
			
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					
					if(mdrsl.getColumnNames()[i].equalsIgnoreCase("gongysb_id")){
						sql.append(
								((IDropDownModel) getGongysModel()).getBeanId(mdrsl
										.getString("gongysb_id"))).append(",");
					}else if(mdrsl.getColumnNames()[i].equalsIgnoreCase("meikxxb_id")){
						sql.append(
								((IDropDownModel) getMeikModel()).getBeanId(mdrsl
										.getString("meikxxb_id"))).append(",");
					}else if (mdrsl.getColumnNames()[i].equalsIgnoreCase("koud")) {
						sql.append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");	
					} else if(mdrsl.getColumnNames()[i].equalsIgnoreCase("xianzsj")) {
						sql.append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");	
					}else{
						sql.append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		con.Close();
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
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
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_Refreshclick){
			_Refreshclick=false;
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		ResultSetList rsl = con
				.getResultSetList("select m.id,(select mingc from gongysb where id=m.gongysb_id) gongysb_id,\n" +
						" (select mingc from meikxxb where id=m.meikxxb_id) meikxxb_id,\n" +
						" (select mingc from yunsdwb where id=m.yunsdwb_id) yunsdwb_id,\n" +
						" (select mingc from pinzb where id=m.pinzb_id) pinzb_id,\n" +
						" m.koud, m.xianzsj  from MEIKDXMB m  order by m.gongysb_id asc,m.meikxxb_id asc,m.yunsdwb_id asc ");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("MEIKDXMB");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("koud").setHeader("扣吨");
		egu.getColumn("xianzsj").setHeader("限制时间");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		
		egu.getColumn("gongysb_id").editor.allowBlank=false;
		egu.getColumn("meikxxb_id").editor.allowBlank=true;
		egu.getColumn("meikxxb_id").editor.readOnly=true;
		
		
		ComboBox yunsdw=new ComboBox();
		yunsdw.allowBlank=true;
		egu.getColumn("yunsdwb_id").setEditor(yunsdw);
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from yunsdwb "));
		
		
		ComboBox pinz=new ComboBox();
		pinz.allowBlank=true;
		egu.getColumn("pinzb_id").setEditor(pinz);
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from pinzb "));
		
		egu.getColumn("koud").setDefaultValue("0");
		
		
		egu
		.addOtherScript("var currentrow;\n gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+ "currentrow=irow;if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+ "gongysTree_window.show();}});\n");
		
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		GridButton ref=new GridButton("刷新","function(){document.all.RefreshButton.click();} ");
		ref.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(ref);
		setExtGrid(egu);
		
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				
				.append(
						"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append(
						"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
						
				.append("if(cks.getDepth() == 2){ \n")
				
//				.append("var count = gridDiv_grid.getStore().getCount();\n" +
//					"for(var i=0;i<count;i++) {\n" + 
//					"    if((gridDiv_ds.getAt(i).get('GONGYSB_ID')==cks.parentNode.text && gridDiv_ds.getAt(i).get('MEIKXXB_ID')== cks.text) && (i!=currentrow)) {\n" + 
//					"       Ext.MessageBox.alert('煤矿供应商重复!');\n" + 
//					"       gongysTree_window.hide();\n" + 
//					"       return;\n" + 
//					"    }\n" + 
//					"}")

				.append(" rec.set('GONGYSB_ID',cks.parentNode.text); rec.set('MEIKXXB_ID', cks.text); ").append("}\n")
					
				.append("if(cks.getDepth() == 1){ \n")
//				.append("var count = gridDiv_grid.getStore().getCount();\n" +
//					"for(var i=0;i<count;i++) {\n" + 
//					"    if((gridDiv_ds.getAt(i).get('GONGYSB_ID')==cks.parentNode.text && gridDiv_ds.getAt(i).get('MEIKXXB_ID')== cks.text) && (i!=currentrow)) {\n" + 
//					"       Ext.MessageBox.alert('煤矿供应商重复!');\n" + 
//					"       gongysTree_window.hide();\n" + 
//					"       return;\n" + 
//					"    }\n" + 
//					"}")
				.append(" rec.set('GONGYSB_ID',cks.text); rec.set('MEIKXXB_ID',''); ").append("}\n")
				
				.append(" gongysTree_window.hide();return; ")
				
				.append("}");
		
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		
		
		con.Close();
	}
	
//	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb ";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb ";
		setMeikModel(new IDropDownModel(sql));
	}
	
	

	public String getTreeScript1() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
			
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			
			getSelectData();
		}
	}
}
