package com.zhiren.jt.het.renyjs;


import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Renyjs extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
//	保存数据list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
    
//  保存页面Bean
    private Renyjsbean editValue;
    public Renyjsbean getEditValue() {
    	return editValue;
    }
    public void setEditValue(Renyjsbean editValue) {
    	this.editValue = editValue;
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Shouhry;
	
	public String getShouhry() {
		return Shouhry;
	}

	public void setShouhry(String shouhry) {
		Shouhry = shouhry;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	

	private void Save() {

		this.SaveData(this.getChange());
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
		}else if(_RefreshChick){
			_RefreshChick = false;
			getSelectData();
		}
		
	}
	
	public void SaveData(String strchange) {
		
		Visit visit = (Visit) this.getPage().getVisit();
	
		
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ExtGridUtil util=visit.getExtGrid1();
		ResultSetList delrsl = util.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(util.tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = util.getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(visit.getExtGrid1().tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
				
					if(mdrsl.getColumnNames()[i].toLowerCase().equals("shouqr_id")){
						
						sql2.append(",").append(visit.getRenyID());
						
					}else{
						
						sql2.append(",").append(
								util.getValueSql(util.getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
					
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append(util.tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					
					if(mdrsl.getColumnNames()[i].toLowerCase().equals("shouqr_id")){
						sql.append(visit.getRenyID()).append(",");
					}else{
						
						sql.append(
								util.getValueSql(util.getColumn(mdrsl.getColumnNames()[i]),
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
		
		
		con.getUpdate(sql.toString());
		con.Close();
	}


	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String renymc=visit.getRenymc();
		this.Shouhry=renymc;
		String sql =
			" select  renyjsb.id as id, renyxxb.quanc as renyxxb_id,liucjsb.mingc as liucjsb_id,qiysj,guoqsj,a.quanc as shouqr_id "+
            " from renyjsb,renyxxb,liucjsb,(select id, quanc from renyxxb) a "+
            " where  renyjsb.renyxxb_id=renyxxb.id and  renyjsb.liucjsb_id=liucjsb.id    and a.id=renyjsb.shouqr_id ";

		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("renyjsb");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").hidden = true;

		
		egu.getColumn("renyxxb_id").setHeader("人员");
		egu.getColumn("liucjsb_id").setHeader("角色");
		egu.getColumn("qiysj").setHeader("起始时间");
		egu.getColumn("guoqsj").setHeader("过期时间");
		egu.getColumn("shouqr_id").setHeader("授权人");
	
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//只能单选中行
		//复选框

		egu.addPaging(25);

		
		egu.getColumn("shouqr_id").setEditor(null);
		
		
		IDropDownModel renymd=new IDropDownModel(" select id, quanc from renyxxb ");
		egu.getColumn("renyxxb_id").setEditor(new ComboBox());
		egu.getColumn("renyxxb_id").setComboEditor(egu.gridId, renymd);
		
		IDropDownModel juesmd=new IDropDownModel(" select id,mingc from liucjsb ");
		egu.getColumn("liucjsb_id").setEditor(new ComboBox());
		egu.getColumn("liucjsb_id").setComboEditor(egu.gridId, juesmd);
		
		egu.getColumn("qiysj").setEditor(new DateField());
		egu.getColumn("guoqsj").setEditor(new DateField());
		
	
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
	
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("\n"+"var fn= function(e){  if(true){var ShouhryValue=document.all.Shouhry.value; var sto=e.grid.getStore(); var reco=sto.getAt(e.row); reco.set('SHOUQR_ID',ShouhryValue);} }; gridDiv_grid.addListener('afteredit',fn,null,null);");
		
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
		
		return  getExtGrid().getHtml();
	}
//	页面判定方法
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
			getSelectData();
		}
	}

}
