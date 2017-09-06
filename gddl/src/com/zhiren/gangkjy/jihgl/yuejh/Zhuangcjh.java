package com.zhiren.gangkjy.jihgl.yuejh;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:童忠付
 * 时间:2009-3-24
 * 内容:展示月装船计划，实现维护，查询功能
 */
public class Zhuangcjh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riqi;

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
			this.riqi = riqi;
	}
	
	private boolean _FindButton=false;
	public void FindButton(IRequestCycle cycle){
		_FindButton=true;
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


	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save(this.getChange());
			
		}
		if (_InsertChick) {
			_InsertChick = false;

		}
		if (_DeleteChick) {
			_DeleteChick = false;
		}
		if (_RefreshChick) {
			_RefreshChick = false;

		}
		if(_FindButton){
			_FindButton=false;
		}

	}
	
	
	private void Save(String strchange){
		
		
		Visit visit=(Visit)this.getPage().getVisit();
		String tableName = "zhuangcjhb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					if (mdrsl.getColumnNames()[i].equals("XUQDC_ID")) {
						long danwmc_id = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("XUQDC_ID"));
						sql2.append(",").append(danwmc_id);
					}else if(mdrsl.getColumnNames()[i].equals("GANGK_ID")){
						long danwmc_id = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("GANGK_ID"));
						sql2.append(",").append(danwmc_id);
					} else if(mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")){
						sql2.append(",").append(visit.getDiancxxb_id());
					}else{
						sql2.append(",").append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i)));
					}

				}

				sql.append(") values(").append(sql2).append(");\n");

			}else{
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {

					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					if (mdrsl.getColumnNames()[i].equals("XUQDC_ID")) {
						long danwmc_id = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("XUQDC_ID"));
						sql.append(danwmc_id).append(",");
					}else if(mdrsl.getColumnNames()[i].equals("GANGK_ID")){
						long danwmc_id = (getExtGrid().getColumn(
								mdrsl.getColumnNames()[i]).combo)
								.getBeanId(mdrsl.getString("GANGK_ID"));
						sql.append(danwmc_id).append(",");
					} else if(mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")){
						sql.append(visit.getDiancxxb_id()).append(",");
					} else {
						sql.append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		
		sql.append("end;"); 
//		System.out.println(sql.toString());
		int flag=con.getUpdate(sql.toString());
		
		if(flag>=0){
			this.msg="Ext.Msg.alert('提示信息',' 数据更新成功!')";
		}else{
			this.msg="Ext.Msg.alert('提示信息',' 数据更新失败!')";
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
			return "to_date('" + value.substring(0, value.lastIndexOf("-"))+"-01" + "','yyyy-mm-dd')";
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
	public void getSelectData() {
		
    int month=Integer.parseInt(getYueSelectValue().getValue());
		
		String yuef="";
		
		if(month<10){
			yuef="0"+month;
		}else{
			yuef=""+month;
		}
		
		Visit visit=(Visit)this.getPage().getVisit();
		
		String strDiancId = "";
		String strXuqdwId = "";
		if(visit.isJTUser()){
			strDiancId="";
			strXuqdwId = "";
		}else if(visit.isGSUser()){
			strDiancId=" and (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
			strXuqdwId=" where (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
		}else{
			strDiancId=" and dc.id="+visit.getDiancxxb_id();
			strXuqdwId=" where dc.id="+visit.getDiancxxb_id();
		}
			
		
       JDBCcon con = new JDBCcon();
		String sql =" select z.id,z.diancxxb_id, z.riq,d.mingc as xuqdc_id,v.mingc as gangk_id,z.chuanq,z.chuanm,z.meil,z.beiz  " +
				" from zhuangcjhb z ,vwgangk v,vwxuqdw d ,diancxxb dc  " +
				"where z.xuqdc_id=d.id and v.id=z.gangk_id and z.diancxxb_id=dc.id"+strDiancId;
		
		
//		if(this.riqi!=null && !this.riqi.equals("")){
			sql+=" and z.riq=to_date('" +getNianSelectValue()+"-"+ getYueSelectValue()+"-01','yyyy-MM-dd')";
//		}
		
		sql+=" order by z.riq desc";
//		System.out.println(sql);
		
		ResultSetList rsl=con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);//设定记录集对应的表单
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("zhuangcjhb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(getNianSelectValue()+"-"+ yuef+"-01");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("xuqdc_id").setHeader("单位名称");
		egu.getColumn("gangk_id").setHeader("港口");
		egu.getColumn("chuanq").setHeader("船期");
    	egu.getColumn("chuanq").setDefaultValue("0");
		egu.getColumn("chuanm").setHeader("船名");
		egu.getColumn("meil").setHeader("煤量(吨)");
		egu.getColumn("meil").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("xuqdc_id").setEditor(new ComboBox());
		egu.getColumn("xuqdc_id").setComboEditor(egu.gridId,
				new IDropDownModel("select dc.id,dc.mingc from diancxxb dc "+strXuqdwId));
		
		egu.getColumn("gangk_id").setEditor(new ComboBox());
		egu.getColumn("gangk_id").setComboEditor(egu.gridId, new IDropDownModel(" select v.id,v.mingc from vwgangk v"));
		
		egu.getColumn("chuanm").setEditor(new ComboBox());
		egu.getColumn("chuanm").setComboEditor(egu.gridId,new IDropDownModel(" select id,mingc from luncxxb order by mingc"));
		egu.getColumn("chuanm").setReturnId(false);
		
		
		egu.addTbarText("年:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NianDropDown");
		comb1.setId("Nian");
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(55);
		comb1.setListWidth(58);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Nian.on('select',function(){document.forms[0].submit();});");//动态刷新
		egu.addTbarText("-");
		egu.addTbarText("月:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YueDropDown");
		comb2.setId("Yue");
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(45);
		comb2.setListWidth(48);
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("Yue.on('select',function(){document.forms[0].submit();});");//动态刷新
		egu.addTbarText("-");// 设置分隔符
	
		
		GridButton gbt = new GridButton("刷新",
		"function(){document.getElementById('RefreshButton').click();}");
		
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "savebutton");
		egu.addTbarText("-");
	
	/*	
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setFormat("Y-m");
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		
		*/
//		egu.addTbarBtn(new GridButton("查询",
//		"function(){document.all.FindButton.click();}",SysConstant.Btn_Icon_Search));
//		
		setExtGrid(egu);
	
	}
	
//	年
	public IDropDownBean getNianSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getNianSelectModel()
							.getOption(DateUtil.getYear(new Date())-2007));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	public void setNianSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}
	public void setNianSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
	public IPropertySelectionModel getNianSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	public void getNianSelectModels() {
				StringBuffer sql=new StringBuffer();
		int i=0;
		for(i=2007;i<DateUtil.getYear(new Date())+2;i++){
			sql.append("select " + i + " id," + i + " mingc from dual union all ");
		}
		sql.append("select " + i + " id," + i + " mingc from dual ");
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}
	
//	月
	public IDropDownBean getYueSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			
			int _yuef = DateUtil.getMonth(new Date());
			for (int i = 0; i < getYueSelectModel().getOptionCount(); i++) {
				Object obj = getYueSelectModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setYueSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public void setYueSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	public IPropertySelectionModel getYueSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYueSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	public void getYueSelectModels() {
		
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
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
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setNianSelectValue(null);
			setNianSelectModel(null);
			setYueSelectValue(null);
			setYueSelectModel(null);
		}
		 
		getSelectData();
		
	}
	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根

	}
	
	protected void initialize(){
		this.msg="";
	}



}
