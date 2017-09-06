package com.zhiren.shanxdted.baob.kuidkkb;

import java.sql.PreparedStatement;
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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuidkkb_yfsz extends BasePage implements PageValidateListener {
	private String msg = "";
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg =  MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			//riqi = DateUtil.FormatDate(new Date());
			riqi=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			//riq2 = DateUtil.FormatDate(new Date());
			riq2=DateUtil.FormatDate(DateUtil.getLastDayOfMonth(new Date()));
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

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
		/*Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);*/
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		
	}
	
	private String getValueSql(GridColumn gc, String value) {
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
	
	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
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
		if (_shuaxin){
			_shuaxin=false;
		}
	}

	public String getGongysxx() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setGongysxx(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String gyssql = "";
		if(getGongysValue().getId()!=-1){
			gyssql = " and sz.meikxxb_id="+getGongysValue().getId();
		}
		
		String dcsql = "";
		if(getTreeid()=="300"){
			dcsql = " and d.id in(301,302,303) ";
		}
		
		String sql=
		    	  "select sz.id,mk.mingc as meikxxb_id,sz.kaisrq,sz.jiezrq,sz.yunsl\n" +
		    	  "from kuidkkb_yfsz sz ,meikxxb mk\n" + 
		    	  "where sz.meikxxb_id=mk.id\n" + 
		    	  "and sz.kaisrq>=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
		    	  "and sz.jiezrq<=to_date('"+getRiq2()+"','yyyy-mm-dd') \n"+gyssql+"" +
		    	 " order by mk.mingc,sz.jiezrq";

		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("kuidkkb_yfsz");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("kaisrq").setHeader("开始日期");
		egu.getColumn("kaisrq").setWidth(120);
		egu.getColumn("jiezrq").setHeader("截止日期");
		egu.getColumn("jiezrq").setWidth(120);
		egu.getColumn("yunsl").setHeader("运损率");
		egu.getColumn("yunsl").setWidth(80);
		egu.getColumn("yunsl").setDefaultValue("0.012");

		
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		//取最近60天来煤的煤矿单位名称
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id ,mingc from meikxxb mk where mk.id in ("+
                                  "select distinct f.meikxxb_id from fahb f where f.daohrq<=sysdate  and f.daohrq>=sysdate -60)"+
"									order by mingc"));
		egu.getColumn("meikxxb_id").setDefaultValue(getGongysValue().getValue());
		egu.getColumn("meikxxb_id").setReturnId(true); 
		
	

	
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(1000);		
		egu.setWidth("bodyWidth");
	
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
	
		// 供货单位
		egu.addTbarText("煤矿");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(true);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		StringBuffer sb=new StringBuffer();
		
		
		
		egu.addTbarText("-");
		egu.addToolbarItem("{text:' 刷新',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert,"");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    
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
		
		String  sql ="select id ,mingc from meikxxb mk where mk.id in ("+
						"select distinct f.meikxxb_id from fahb f where f.daohrq<=sysdate  and f.daohrq>=sysdate -60)"+
        "			order by mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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

			((Visit) getPage().getVisit()).setString1("");
			getGongysValue();
			setGongysValue(null);
			getGongysModels();
			this.setRiqi(null);
			this.setRiq2(null);
			
			
		}
		getSelectData();
	}

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
	
	private String getDcMingc(JDBCcon con, String id){
		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id=" + id);
		if(rsl.next()){
			return rsl.getString("mingc");
		}else{
			return "";
		}
	}
}