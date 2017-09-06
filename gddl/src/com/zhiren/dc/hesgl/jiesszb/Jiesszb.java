package com.zhiren.dc.hesgl.jiesszb;

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
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jiesszb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		if(getShifwb().equals("文本框")) {//不是文本框			
			Save1(getChange(), visit);
		}else{
			Save2(getChange(), visit);;
	}
	}

	public void Save2(String strchange,Visit visit) {
		String tableName="jiesszb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl =  visit.getExtGrid1().getModifyResultSet(strchange);
		while(mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();
			StringBuffer sql4 = new StringBuffer();
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				sql3.append(",diancxxb_id");//可变
				sql4.append(",").append(visit.getDiancxxb_id());
				sql5.append(",xuh");
				sql6.append(",").append(getBianmValue().getId());
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					if(mdrsl.getColumnNames()[i].equals("SHIFKDX")||mdrsl.getColumnNames()[i].equals("ZB_ID")){
						continue;
					}
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
						sql2.append(",").append(getGongysValue().getId());
						//continue;
					}
//                    if(mdrsl.getColumnNames()[i].equals("JIESSZZB_ID")){
//                    	sql2.append(",").append(getZhi());
//					}
                    else{
						sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
					}

				}
				sql.append(sql3).append(sql5).append(") values(").append(sql2).append(sql4).append(sql6).append(");\n");

				}else {
					
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					if(mdrsl.getColumnNames()[i].equals("SHIFKDX")||mdrsl.getColumnNames()[i].equals("ZB_ID")){
						continue;

					}
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
						
						sql.append(getGongysValue().getId()).append(",");
					}else{
					sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}


	
	public void Save1(String strchange,Visit visit) {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=getExtGrid();

		String zib_id="";

		try{
			
			StringBuffer sql = new StringBuffer("begin \n");
			StringBuffer sql_zi = new StringBuffer();
			StringBuffer sql_zu = new StringBuffer();
			
			String tableName_zi="jiesszzb";
			String tableName_zu="jiesszb";
			
			ResultSetList delrsl = egu.getDeleteResultSet(strchange);
			while(delrsl.next()) {
				sql_zu.append("delete from ").append(tableName_zu).append(" where id =").append(delrsl.getString(0)).append(";\n");
				sql_zi.append("delete from ").append(tableName_zi).append(" where id =").append(delrsl.getString("ZB_ID")).append(";\n");
			}
			
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while(mdrsl.next()) {
				StringBuffer sql_zinr = new StringBuffer();
				StringBuffer sql_zunr = new StringBuffer();
				
				if("0".equals(mdrsl.getString("ID"))) {
					
					zib_id=MainGlobal.getNewID(visit.getDiancxxb_id());//子表ID
					
					sql_zunr.append("getnewid(").append(visit.getDiancxxb_id()).append("),")
							.append(getBianmValue().getId()).append(",").append(visit.getDiancxxb_id());
					
					sql_zinr.append(zib_id);
					
					sql_zi.append("insert into ").append(tableName_zi).append("(id");//bianm,miaos,zhi,shifkdx
					sql_zu.append("insert into ").append(tableName_zu).append("(id,xuh,diancxxb_id");
					
					for(int i=1;i<mdrsl.getColumnCount();i++) {
						//得到主表的表头及内容
						if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
							sql_zu.append(",").append(mdrsl.getColumnNames()[i]);
							sql_zunr.append(",").append(getGongysValue().getId());
						}else if(mdrsl.getColumnNames()[i].equals("MIAOS")){
							sql_zu.append(",").append(mdrsl.getColumnNames()[i]);
							sql_zunr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
						}else if(mdrsl.getColumnNames()[i].equals("BIANM")){
							sql_zu.append(",").append(mdrsl.getColumnNames()[i]);
							sql_zunr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));

							sql_zi.append(",").append(mdrsl.getColumnNames()[i]);
							sql_zinr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));

						}else if (mdrsl.getColumnNames()[i].equals("JIESSZZB_ID")){
							sql_zu.append(",").append(mdrsl.getColumnNames()[i]);
							sql_zunr.append(",").append(zib_id);

							sql_zi.append(",zhi");
							sql_zinr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));

						}else if(mdrsl.getColumnNames()[i].equals("SHIFKDX")){
							sql_zi.append(",").append(mdrsl.getColumnNames()[i]);
							sql_zinr.append(",").append(getShifkdx());

						}
					}
					
					sql_zi.append(",miaos) values(").append(sql_zinr).append(",'文本框');\n");
					sql_zu.append(") values(").append(sql_zunr).append(");\n");
					
				}else {
					
					sql_zi.append("update ").append(tableName_zi).append(" set ");
					sql_zu.append("update ").append(tableName_zu).append(" set ");
					
					for(int i=1;i<mdrsl.getColumnCount();i++) {
						
//						得到主表的表头及内容
						if(mdrsl.getColumnNames()[i].equals("MIAOS")){
								//||mdrsl.getColumnNames()[i].equals("JIESSZZB_ID")){
							sql_zu.append(mdrsl.getColumnNames()[i]);
							sql_zu.append(" = ").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append("'");
						}else if (mdrsl.getColumnNames()[i].equals("JIESSZZB_ID")){
							sql_zi.append("zhi");
							sql_zi.append(" = ").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append("'");

						}
					}

					sql_zi.deleteCharAt(sql_zi.length()-1);
					sql_zu.deleteCharAt(sql_zu.length()-1);
					
					sql_zi.append(" where id =").append(mdrsl.getString("ZB_ID")).append(";\n");
					sql_zu.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
				}
			}
			sql.append(sql_zi).append(sql_zu);
			sql.append("end;");
			con.getUpdate(sql.toString());
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
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
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}

	}

		
	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
	

			ResultSetList rsl = con.getResultSetList(
			        "select j.id,jz.id as zb_id,g.mingc as gongysb_id,j.bianm,j.miaos,jz.zhi as jiesszzb_id,jz.shifkdx from jiesszb j,gongysb g,jiesszzb jz\n" +
			        "where j.gongysb_id=g.id and j.jiesszzb_id=jz.id and j.gongysb_id="+getGongysValue().getId()+"and j.diancxxb_id="+visit.getDiancxxb_id()+
			        "and j.bianm='"+getBianmValue().getValue()+"'");
	
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("jiesszb");
		egu.getColumn("id").setHidden(true);

		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);		
		egu.getColumn("bianm").setHeader("编号");
		egu.getColumn("miaos").setHeader("描述");
		egu.getColumn("jiesszzb_id").setHeader("值");	
		egu.getColumn("shifkdx").setHidden(true);
		egu.getColumn("zb_id").setHidden(true);
		
		egu.getColumn("bianm").setWidth(130);
		egu.getColumn("miaos").setWidth(200);
		egu.getColumn("jiesszzb_id").setWidth(220);
		
		

		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setDefaultValue(getGongysValue().getValue());
		egu.getColumn("gongysb_id").setReturnId(true); 
		
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("bianm").setDefaultValue(getBianmValue().getValue());
 
		
//		String bianm="";
//		
//		if(getBianmValue().getValue().equals("结算数量")||getBianmValue().getValue().equals("结算加权数量")){
//			bianm="结算数量";
//		}else if(getBianmValue().getValue().equals("结算显示指标")){
//			bianm="结算显示指标";
//		}else if(getBianmValue().getValue().equals("结算数量取整方式")){
//			bianm="结算数量取整方式";
//		}else if(getBianmValue().getValue().equals("结算数量保留小数位")||getBianmValue().getValue().equals("Mt保留小数位")||
//				getBianmValue().getValue().equals("Mad保留小数位")||getBianmValue().getValue().equals("Aar保留小数位")||
//				getBianmValue().getValue().equals("Aad保留小数位")||getBianmValue().getValue().equals("Adb保留小数位")||
//				getBianmValue().getValue().equals("Vad保留小数位")||getBianmValue().getValue().equals("Vdaf保留小数位")||
//				getBianmValue().getValue().equals("Stad保留小数位")||getBianmValue().getValue().equals("Std保留小数位")||
//				getBianmValue().getValue().equals("Had保留小数位")||getBianmValue().getValue().equals("Qnetar保留小数位")||
//				getBianmValue().getValue().equals("Qbad保留小数位")||getBianmValue().getValue().equals("Qgrad保留小数位")){
//			bianm="结算数量保留小数位";
//		}
		if(getShifwb().equals("文本框")){
			
			egu.getColumn("miaos").setDefaultValue(getShifwb());
			egu.getColumn("miaos").setEditor(null);
			egu.getColumn("jiesszzb_id").setReturnId(true); 

		}else{
		egu.getColumn("jiesszzb_id").setEditor(new ComboBox());
		egu.getColumn("jiesszzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,miaos from jiesszzb where bianm like '%"+getBianmValue().getValue()+"%'"));
		}
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(1000);
		

		
		// 供货单位
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");

		

		egu.addTbarText("-");
		
		egu.addTbarText("编号");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("BianmDropDown");
		comb1.setId("Bianm");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(150);
		comb1.setReadOnly(true);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Bianm.on('select',function(){document.forms[0].submit();});");
		
		egu.addTbarText("-");
        String Shifkdx=getShifkdx();
	    String tianjs="";
		if(Shifkdx.equals("0")){	
			tianjs="if(gridDiv_ds.getCount()>1){" +
					"alert('不可以保存多条记录'); return;}";
			}
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='请选择'){alert('请选择供应商'); return;}");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition,"savebutton",tianjs);
		setExtGrid(egu);
		
		con.Close();
	}
	
	

//供货单位

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select gys.id,gys.mingc from gongysdcglb glb,diancxxb dc,gongysb gys\n" +
			"where glb.diancxxb_id=dc.id and glb.gongysb_id=gys.id and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by gys.mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	//编码
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

//	public void getBianmModels() {
//
//		List list = new ArrayList();
//		list.add(new IDropDownBean(1, "结算数量"));
//		list.add(new IDropDownBean(2, "结算加权数量"));
//		list.add(new IDropDownBean(3, "结算显示指标"));
//		list.add(new IDropDownBean(4, "结算数量保留小数位"));
//		list.add(new IDropDownBean(5, "结算数量取整方式"));
//		list.add(new IDropDownBean(6, "Mt保留小数位"));
//		list.add(new IDropDownBean(7, "Mad保留小数位"));
//		list.add(new IDropDownBean(8, "Aar保留小数位"));
//		list.add(new IDropDownBean(9, "Aad保留小数位"));
//		list.add(new IDropDownBean(10, "Adb保留小数位"));
//		list.add(new IDropDownBean(11, "Vad保留小数位"));
//		list.add(new IDropDownBean(12, "Vdaf保留小数位"));
//		list.add(new IDropDownBean(13, "Stad保留小数位"));
//		list.add(new IDropDownBean(14, "Std保留小数位"));
//		list.add(new IDropDownBean(15, "Had保留小数位"));
//		list.add(new IDropDownBean(16, "Qnetar保留小数位"));
//		list.add(new IDropDownBean(17, "Qbad保留小数位"));
//		list.add(new IDropDownBean(18, "Qgrad保留小数位"));
//		((Visit) getPage().getVisit())
//				.setProSelectionModel3(new IDropDownModel(list));
//	}
	public IPropertySelectionModel getBianmModels() {
		
		String sql ="select distinct xuh,bianm  from jiesszb order by xuh";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
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

	
	private String getShifkdx() {
		String Shifkdx="";
		JDBCcon cn =new JDBCcon();
        String sql="select j.id,j.shifkdx from jiesszzb j where j.bianm like '%"+getBianmValue().getValue()+"%'";
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				Shifkdx = rs.getString("shifkdx");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return Shifkdx;
	}
	private String getShifwb() {
		String Shifwb="";
		JDBCcon cn =new JDBCcon();
        String sql="select id, miaos from jiesszb where bianm like '%"+getBianmValue().getValue()+"%'";
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				Shifwb = rs.getString("miaos");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return Shifwb;
	}
	private String getZhi() {
		String Zhi="";
		JDBCcon cn =new JDBCcon();
        String sql="select id  from jiesszzb where bianm like '%"+getBianmValue().getValue()+"%'";
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				Zhi = rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return Zhi;
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

			getGongysValue();
			setGongysValue(null);
			getGongysModels();
			getBianmValue();
			setBianmValue(null);
			getBianmModels();

			getSelectData();
		}
		getSelectData();
	}

}