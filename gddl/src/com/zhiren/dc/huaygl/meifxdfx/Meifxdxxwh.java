package com.zhiren.dc.huaygl.meifxdfx;

import java.util.Date;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*作者:王总兵
 *日期:2010-4-25 17:31:48
 *描述:增加小键盘上下左右单元格导航
 */
/**
 * @author yinjm
 * 类名：煤粉细度详细维护
 */

public class Meifxdxxwh extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
   private String Fenxrq; // 保存分析日期
	
	public String getFenxrq() {
		return Fenxrq;
	}

	public void setFenxrq(String fenxrq) {
		Fenxrq = fenxrq;
	}
	
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String rowNumIndex; // 保存从上个页面传过来的id

	public String getRowNumIndex() {
		return rowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		this.rowNumIndex = rowNumIndex;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	

	
//	炉号下拉框_开始
	public IDropDownBean getLuhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getLuhModel().getOptionCount() > 0) {
				setLuhValue((IDropDownBean) getLuhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setLuhValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LuhValue);
	}

	public IPropertySelectionModel getLuhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getLuhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setLuhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getLuhModels() {
		String sql = "select id, jizbh from jizb";
		setLuhModel(new IDropDownModel(sql));
	}
	//方向键控制光标监听JS
	public String getStr(int col){
	
		String Str = "";

		Str = "specialkey:function(own,e){\n" +
				"			if(row>0){\n" +
				"				if(e.getKey()==e.UP){\n" +
				"					gridDiv_grid.startEditing(row-1, col);\n" +
				"					row = row-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(row+1 < gridDiv_grid.getStore().getCount()){\n" +
				"				if(e.getKey()==e.DOWN){\n" +
				"					gridDiv_grid.startEditing(row+1, col);\n" +
				"					row = row+1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.LEFT){\n" +
				"				if("+col+"!=1){\n" +
				"					gridDiv_grid.startEditing(row, col-1);\n" +
				"					col = col-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.RIGHT){\n" +
				"				if("+col+"!=2){\n" +
				"					gridDiv_grid.startEditing(row, col+1);\n" +
				"					col = col+1;\n" +
				"				}\n" +
				"			}\n" +
				"	 	 }\n";	
		return Str;
	}
//	炉号下拉框_结束
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from meifxdb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into meifxdb(ID,FENXRQ,QUYRQ,JIZB_ID,ZHIFXTMC,R200,R90,R75,LURY,LURSJ,BEIZ")
				.append(") values(getnewid(").append(visit.getDiancxxb_id()).append("), ").append("to_date('"+mdrsl.getString("fenxrq")+"','yyyy-mm-dd')").append(", ")
				.append("to_date('"+mdrsl.getString("quyrq")+"','yyyy-mm-dd'),")
				.append(getLuhValue().getStrId()).append(", '")
				.append(mdrsl.getString("ZHIFXTMC")).append("', ")
				.append(getSqlValue(mdrsl.getString("r200"))).append(", ")
				.append(getSqlValue(mdrsl.getString("r90"))).append(", ")
				.append(getSqlValue(mdrsl.getString("r75"))).append(", '")
				.append(mdrsl.getString("lury")).append("',")
				.append("sysdate").append(",'")
				.append(mdrsl.getString("beiz"))
				.append("');\n");
			} else {
				sbsql.append("update meifxdb set ")
				.append(" FENXRQ = ")
				.append("to_date('"+mdrsl.getString("fenxrq")+"','yyyy-mm-dd')")
				.append(", quyrq = ")
				.append("to_date('"+mdrsl.getString("quyrq")+"','yyyy-mm-dd')")
				.append(", ZHIFXTMC = '").append(mdrsl.getString("ZHIFXTMC"))
				.append("', R200 = ").append(getSqlValue(mdrsl.getString("r200")))
				.append(", R90 = ").append(getSqlValue(mdrsl.getString("r90")))
				.append(", R75 = ").append(getSqlValue(mdrsl.getString("r75")))
				.append(", beiz = '").append(mdrsl.getString("beiz"))
				.append("' where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 

			"select m.id,m.fenxrq,m.quyrq,m.zhifxtmc,m.r200,m.r90,m.r75,m.beiz,m.lury\n" +
			" from meifxdb m,jizb j\n" + 
			"where m.fenxrq=to_date('"+this.getFenxrq()+"','yyyy-mm-dd')\n" + 
			"and m.jizb_id=j.id\n" + 
			"and j.id="+this.getLuhValue().getId()+" order by m.id ";


		ResultSetList rsl =  con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("fenxrq").setHeader("分析日期");
		egu.getColumn("fenxrq").setEditor(null);
		egu.getColumn("fenxrq").setHidden(true);
		egu.getColumn("quyrq").setHeader("取样日期");
		egu.getColumn("zhifxtmc").setHeader("制粉系统名称");
		egu.getColumn("r200").setHeader("R<sub>200</sub>(%)");
		egu.getColumn("r90").setHeader("R<sub>90</sub>(%)");
		egu.getColumn("r75").setHeader("R<sub>75</sub>(%)");
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setHidden(true);
		
		//监听小键盘
		egu.getColumn("zhifxtmc").editor.setListeners(getStr(1));
		egu.getColumn("r200").editor.setListeners(getStr(0));
		egu.getColumn("r90").editor.setListeners(getStr(0));
		egu.getColumn("r75").editor.setListeners(getStr(0));
		egu.getColumn("beiz").editor.setListeners(getStr(2));
		
		//监听回车键
//		egu.getColumn("zhifxtmc").editor.setListeners("specialkey:function(own,e){if(e.getKey() == e.ENTER){gridDiv_grid.startEditing(row, 5);}}");
//		egu.getColumn("r200").editor.setListeners("specialkey:function(own,e){if(e.getKey() == e.ENTER){gridDiv_grid.startEditing(row, 6);}}");
//		egu.getColumn("r90").editor.setListeners("specialkey:function(own,e){if(e.getKey() == e.ENTER){gridDiv_grid.startEditing(row, 7);}}");
//		
//		String listeners = 
//			"specialkey:function(own,e){\n" +
//			"    if(e.getKey() == e.ENTER){\n" + 
//			"        if(row+1 == gridDiv_grid.getStore().getCount()){\n" + 
//			"            Ext.MessageBox.alert('提示信息','已到达数据末尾！');\n" + 
//			"            return;\n" + 
//			"        }\n" + 
//			"        row = row+1;\n" + 
//			"        last = gridDiv_grid.getSelectionModel().getSelected();\n" + 
//			"        gridDiv_grid.getSelectionModel().selectRow(row);\n" + 
//			"        cur = gridDiv_grid.getSelectionModel().getSelected();\n" + 
//			"        copylastrec(last,cur);\n" + 
//			"        gridDiv_grid.startEditing(row, 4);\n" + 
//			"    }\n" + 
//			"}";
//		egu.getColumn("r75").editor.setListeners(listeners);
		
		
		egu.getColumn("fenxrq").setDefaultValue(this.getFenxrq());
		egu.getColumn("quyrq").setDefaultValue(this.Fenxrq);
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		
		
		
		
		egu.addTbarText("分析日期：");
		DateField df = new DateField();
		df.setValue(getFenxrq());
		df.setId("riq");
		df.Binding("Fenxrq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("炉号：");
		ComboBox comb = new ComboBox();
		comb.setWidth(100);
		comb.setTransform("Luh");
		comb.setId("Luh");
		comb.setLazyRender(true);
		comb.setEditable(false);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Luh.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		
//		多行替换
		egu.addTbarText("-");
		egu.addTbarText("-");
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("多行替换");
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){\n" +
				
				" if(SelectLike.checked) { \n" +
				
				"for(var i=e.row;i<gridDiv_ds.getCount();i++){\n" +
				"var rec=gridDiv_ds.getAt(i);\n" +
				" rec.set(e.field+'',e.value);\n" +
				"}\n" +
				
				"}\n" +
				
				"" +
				"  }); ");

		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;col=icol;});");
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}


	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel3(null); // 炉号下拉框
			visit.setDropDownBean3(null);
			setFenxrq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}