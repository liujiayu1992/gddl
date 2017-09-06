package com.zhiren.dc.jihk;

/*
 * 作者:ww
 * 时间:2009-11-29
 * 内容:人工采样维护
 */
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

public class Rengcywh extends BasePage implements PageValidateListener {
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
					
					if( mdrsl.getColumnNames()[i].equalsIgnoreCase("MEIKMC") ){
						sql2.append(",'").append(mdrsl.getString(i)).append("'");
					}else if (mdrsl.getColumnNames()[i].equalsIgnoreCase("PINZ")) {
						sql2.append(",'").append(mdrsl.getString(i)).append("'");		
					}else {
						sql2.append(",").append(
										((IDropDownModel) getCaiyfsModel()).getBeanId(mdrsl
												.getString("caiyfs")));
					}
				
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
			
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					if(mdrsl.getColumnNames()[i].equalsIgnoreCase("MEIKMC")){
						sql.append("'").append(mdrsl.getString(i)).append("',");
					}else if (mdrsl.getColumnNames()[i].equalsIgnoreCase("PINZ")) {
						sql.append("'").append(mdrsl.getString(i)).append("',");		
					}else{
						sql.append(
								((IDropDownModel) getCaiyfsModel()).getBeanId(mdrsl
										.getString("caiyfs"))).append(",");
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
		String SQL = "select id,meikmc,pinz,decode(caiyfs,0,'机械','人工') as caiyfs from caiyfssz ";
		ResultSetList rsl = con.getResultSetList(SQL);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caiyfssz");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikmc").setHeader("煤矿");
		egu.getColumn("meikmc").setWidth(150);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("caiyfs").setHeader("采样方式");
		egu.getColumn("caiyfs").setDefaultValue("人工");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		
		egu.getColumn("meikmc").editor.allowBlank=false;
		egu.getColumn("pinz").editor.allowBlank=false;
		egu.getColumn("caiyfs").editor.readOnly=false;
		
		
		ComboBox meikdw=new ComboBox();
		egu.getColumn("meikmc").setEditor(meikdw);
		egu.getColumn("meikmc").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from meikxxb "));		
		
		ComboBox pinz=new ComboBox();
		egu.getColumn("pinz").setEditor(pinz);
		egu.getColumn("pinz").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from pinzb "));
		
		ComboBox caiyfs=new ComboBox();
		List list = new ArrayList();			
		list.add(new IDropDownBean(0,"机械"));
		list.add(new IDropDownBean(1,"人工"));
		egu.getColumn("caiyfs").setEditor(caiyfs);
		egu.getColumn("caiyfs").setComboEditor(egu.gridId, new IDropDownModel(list));
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		GridButton ref=new GridButton("刷新","function(){document.all.RefreshButton.click();} ");
		ref.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(ref);
		setExtGrid(egu);
		
		con.Close();
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
	
	public IPropertySelectionModel getCaiyfsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			setCaiyfsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setCaiyfsModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(_value);
	}

	public void setCaiyfsModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0,"机械"));
		list.add(new IDropDownBean(1,"人工"));
		setCaiyfsModel(new IDropDownModel(list));
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
			setMsg(null);
			setMeikModel(null);
			setMeikModels();
			setCaiyfsModel(null);
			setCaiyfsModels();
		}	
		getSelectData();
	}
	
}
