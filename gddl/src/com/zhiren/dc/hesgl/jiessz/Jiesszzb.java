package com.zhiren.dc.hesgl.jiessz;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jiesszzb extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		msg = "";
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
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			GotoJiesszfab(cycle);
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
	}

	private void GotoJiesszfab(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		
		cycle.activate("Jiesszfab");
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String shifwbk = getShifwb();
			if(shifwbk.equals("文本框")){
				Save1(getChange(), visit);//是下拉框选择的保存方法
			}else{
				Save2(getChange(), visit);//需要用户配置自己输入的保存方法
			}
	}

	
//	需要用户配置自己输入的保存方法
	public void Save1(String strchange, Visit visit) {
		String tableName = "jiesszb";
		JDBCcon con = new JDBCcon();
	
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = getExtGrid()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		long Diancxxb_id=Long.parseLong(visit.getString11());
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				sql2.append(MainGlobal.getNewID(Diancxxb_id));
				
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					
					
					if(mdrsl.getColumnNames()[i].equals("JIESSZFAB_ID")||mdrsl.getColumnNames()[i].equals("ZHI")){
						
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
					}else if(mdrsl.getColumnNames()[i].equals("JIESSZBMB_ID")){
						
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(getBianmValue().getId());
					}
				}
				sql.append(") values(").append(sql2).append(");\n");

			} else {
					
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					if(mdrsl.getColumnNames()[i].equals("ZHI")){
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
						}else{
							continue;
						}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		   }
		}
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("保存成功！");
		}else{
			
			this.setMsg("保存失败！");
		}
		mdrsl.close();
		con.Close();
	}

	//是下拉框选择的保存方法
	public void Save2(String strchange, Visit visit) {
		String tableName = "jiesszb";
		JDBCcon con = new JDBCcon();
	
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = getExtGrid()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		long Diancxxb_id=Long.parseLong(visit.getString11());
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				sql2.append(MainGlobal.getNewID(Diancxxb_id));
				
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {					
					if(mdrsl.getColumnNames()[i].equals("JIESSZFAB_ID")){
						
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
					}else if(mdrsl.getColumnNames()[i].equals("JIESSZBMB_ID")){
						
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(getBianmValue().getId());
					}else if(mdrsl.getColumnNames()[i].equals("ZHI")){
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",'").append(MainGlobal.getProperId_String(this.getZhiModels(), mdrsl.getString(i))).append("'");
					}
				}
				sql.append(") values(").append(sql2).append(");\n");

			} else {
					
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					if(mdrsl.getColumnNames()[i].equals("ZHI")){
						sql.append(mdrsl.getColumnNames()[i]).append(" = '");
						sql.append(MainGlobal.getProperId_String(this.getZhiModels(), mdrsl.getString(i))).append("'").append(",");
						}else{
							continue;
						}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		   }
		}
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("保存成功！");
		}else{
			
			this.setMsg("保存失败！");
		}
		mdrsl.close();
		con.Close();
	}
	private String getShifwy() {
		String Shifwy="";
		JDBCcon cn =new JDBCcon();
        String sql="select j.id,j.shifwy from jiesszbmb j where j.bianm like '%"+getBianmValue().getValue()+"%'";
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				Shifwy = rs.getString("shifwy");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return Shifwy;
	}
	
	private String getShifwb() {
		String Shifwb="";
		JDBCcon cn =new JDBCcon();
        String sql="select zb.bianm, zb.zhi\n" +
                       "  from jiesszbmzb zb, jiesszbmglb gl\n" + 
                       " where gl.jiesszbmzb_id = zb.id\n" + 
                       "   and gl.jiesszbmb_id =" + getBianmValue().getId();
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				Shifwb = rs.getString("bianm");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return Shifwb;
	}
	
	public void getSelectData(String Param) {
	    Visit visit = (Visit) getPage().getVisit();
	    String str_lb="";
	    
		JDBCcon con = new JDBCcon();
		long ID=Long.parseLong(Param.substring(0,Param.lastIndexOf(',')));
		
		if(Param.length()>Param.lastIndexOf(",")){
			
			str_lb=Param.substring(Param.lastIndexOf(',')+1);
		}
			
		if(!str_lb.equals("")){
			
			for(int i=0;i<this.getBianmModel().getOptionCount();i++){
				
				if(((IDropDownBean)getBianmModel().getOption(i)).getValue().equals(str_lb)){
					
					setBianmValue(((IDropDownBean)getBianmModel().getOption(i)));
					visit.setString10(visit.getString10().substring(0,visit.getString10().lastIndexOf(",")+1));
				}
			}
		}
		
		try{
		String str = "select sz.id,sz.jiesszfab_id,bm.bianm as jiesszbmb_id, decode(zb.bianm,null,sz.zhi,zb.bianm) as zhi\n"
					+ "  from jiesszb sz, jiesszbmb bm,jiesszbmzb zb\n"
					+ " where sz.jiesszfab_id ="
					+ ID
					+ "\n"
					+ "   and sz.jiesszbmb_id = bm.id\n"
					+"    and zb.zhi(+)=sz.zhi"
					+ "   and bm.id ="
					+ getBianmValue().getId();
		
		ResultSetList rsl = con.getResultSetList(str);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("ID").setHeader("ID");
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("jiesszfab_id").setHeader("JIESSZFAB_ID");
		egu.getColumn("jiesszfab_id").setHidden(true);
		egu.getColumn("jiesszfab_id").setDefaultValue(""+ID);
		egu.getColumn("jiesszbmb_id").setHeader("编码");
		egu.getColumn("jiesszbmb_id").setEditor(null);
		egu.getColumn("jiesszbmb_id").setWidth(150);
		egu.getColumn("jiesszbmb_id").setDefaultValue(getBianmValue().getValue());
		egu.getColumn("jiesszbmb_id").setReturnId(true);
	
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("zhi").setWidth(220);
		egu.addTbarText("编码");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("BianmDropDown");
		comb1.setId("Bianm");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(180);
//		comb1.setValue(str_lb);
		comb1.setReadOnly(true);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Bianm.on('select',function(){document.forms[0].submit();});");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		String shifwbk = getShifwb();

		if (shifwbk.equals("文本框")) {
			egu.getColumn("zhi").setReturnId(true);
		} else {
			egu.getColumn("zhi").setEditor(new ComboBox());
			egu.getColumn("zhi").setComboEditor(egu.gridId,
						new IDropDownModel(this.getZhiSql()));
			egu.getColumn("zhi").setReturnId(true);
			egu.getColumn("zhi").setWidth(150);
		}
		String Shifwy=getShifwy();
		 String tianjs="";
		if(Shifwy.equals("1")){	
			tianjs="if(gridDiv_ds.getCount()>1){" +
			"Ext.MessageBox.alert('提示信息','不可以保存多条记录'); return;}";
		}
	
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert,null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",tianjs);
	    egu.addToolbarItem("{"+new GridButton("返回","function(){ document.getElementById('ReturnButton').click();" +
		"}").getScript()+"}");
		setExtGrid(egu);
		
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

//	这是一个下拉框
	public IDropDownBean getBianmValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getBianmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setBianmValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public IPropertySelectionModel getBianmModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getBianmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setBianmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getBianmModels() {
		
		String sql ="select id, bianm  from jiesszbmb order by bianm";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
	//这是一个下拉框
	
	public IDropDownBean getZhiValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getZhiModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setZhiValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}
	
	public IPropertySelectionModel getZhiModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getZhiModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setZhiModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getZhiModels() {
		
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(getZhiSql()));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	private String getZhiSql(){
		
		String sql ="select zb.zhi, zb.bianm\n" +
        "  from jiesszbmglb gl, jiesszbmzb zb\n" + 
        " where gl.jiesszbmzb_id=zb.id\n" +
        " and gl.jiesszbmb_id ="+getBianmValue().getId();
		
		return sql;
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
//			getZhiValue();
			setZhiValue(null);
			setZhiModel(null);
			getZhiModels();
//			getBianmValue();
			setBianmValue(null);
			setBianmModel(null);
			getBianmModels();
//			visit.getString10()	//上个页面传过来的参数
//			visit.getString11()	//上个页面对应的diancxxb_id
		}
		getSelectData(visit.getString10());
	}
	
}