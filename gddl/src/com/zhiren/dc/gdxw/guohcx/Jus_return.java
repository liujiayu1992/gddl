package com.zhiren.dc.gdxw.guohcx;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class Jus_return extends BasePage implements PageValidateListener {
	
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
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	
	private String eriq; // 保存日期
	
	public String getEriq() {
		return eriq;
	}

	public void setEriq(String eriq) {
		this.eriq = eriq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    
	
//	煤矿单位下拉框_开始
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = "select mk.id, mk.mingc from meikxxb mk order by mk.mingc";
		setMeikdwModel(new IDropDownModel(sql, "请选择"));
	}
//	煤矿单位下拉框_结束
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			
		}
		this.getSelectData();
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		String riq =this.getRiq();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				//不允许添加
			} else {
				sbsql.append("update chepbtmp c set c.isjus=0,c.jussj=null,c.jusry=null where id="+mdrsl.getString("id")+";\n");
					

			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
		this.setMsg("拒收回退成功!");
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		String meikmc=this.getMeikdwValue().getValue();
		//System.out.println(meikmc);
		if(meikmc.equals("请选择")){
			tiaoj="";
		}else{
			tiaoj= "and c.meikdwmc='"+meikmc+"'";
		}
		String kais=this.getRiq();
		String jies=this.getEriq();
		
		
		JDBCcon con = new JDBCcon();
		String sql = 
			"select c.id, c.cheph,c.meikdwmc,c.jusry,to_char(c.jussj,'yyyy-mm-dd hh24:mi:ss') as jussj,\n" +
			"c.lury,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') as lursj\n" + 
			"from chepbtmp c where c.jussj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
			"and  c.jussj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
			"and c.isjus=1\n" + 
			""+tiaoj+"\n"+
			"order by c.jussj";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置每页显示行数
		egu.addPaging(0);
		
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
	    egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setWidth(80);
		
		egu.getColumn("meikdwmc").setHeader("煤矿");
		egu.getColumn("meikdwmc").setWidth(150);
		
		egu.getColumn("jusry").setHeader("拒收人员");
		egu.getColumn("jusry").setWidth(90);
		
		egu.getColumn("jussj").setHeader("拒收时间");
		egu.getColumn("jussj").setWidth(150);
		
		egu.getColumn("lury").setHeader("采样人");
		egu.getColumn("lury").setWidth(80);
		
		egu.getColumn("lursj").setHeader("采样时间");
		egu.getColumn("lursj").setWidth(150);
		
	
		

		
		
		egu.addTbarText("拒收日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		
		egu.addTbarText("至：");
		DateField df1 = new DateField();
		df1.setValue(getEriq());
		df1.setId("eriq");
		df1.Binding("Eriq", "");
		egu.addToolbarItem(df1.getScript());
		egu.addOtherScript("eriq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("煤矿单位：");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setListWidth(150);
		comb.setTransform("Meikdw");
		comb.setId("Meikdw");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meikdw.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("-");
		egu.addToolbarButton("拒收回退",GridButton.ButtonType_Sel, "SaveButton", null, SysConstant.Btn_Icon_SelSubmit);
		//egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		

	
	
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
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
			setRiq(DateUtil.FormatDate(new Date()));
			setEriq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // 煤矿单位下拉框
			visit.setDropDownBean3(null);
			
			
		}
		getSelectData();
	}
}