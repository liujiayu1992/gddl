package com.zhiren.shihs;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihpp extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

//	 绑定日期
	private String riqi;

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
			this.riqi = riqi;
	}

	private String riq2;

	public String getRiq2() {
		return riq2;
	}

	public void setRiq2(String riq2) {
			this.riq2 = riq2;
	}

//	 页面变化记录
	public String getChange() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setChange(String change) {
		((Visit) this.getPage().getVisit()).setString1(change);
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
			UpdateChep();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}
	
	private void UpdateChep() {
		// TODO 自动生成方法存根
		Visit visit = (Visit) this.getPage().getVisit();
		this.Save(getChange(), visit);
	}
	
	private void Save(String strchange,Visit visit) {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(strchange);
		StringBuffer Str_sql = new StringBuffer();
		Str_sql.append("begin\n");
		while(rsl.next()){
			Str_sql.append("update shihcptmp set ")
			.append("gongysb_id = ").append(getExtGrid().getColumn("gongys_mc").combo.getBeanId(rsl.getString("gongys_mc"))).append(",")
			.append("gongys = '").append(rsl.getString("gongys_mc")).append("',")
			.append("shihpzb_id = ").append(getExtGrid().getColumn("shihpz_mc").combo.getBeanId(rsl.getString("shihpz_mc"))).append(",")
			.append("shihpz = '").append(rsl.getString("shihpz_mc")).append("',")
			.append("yunsdwb_id = ").append(getExtGrid().getColumn("yunsdw_mc").combo.getBeanId(rsl.getString("yunsdw_mc"))).append(",")
			.append("yunsdw = '").append(rsl.getString("yunsdw_mc")).append("' ")
			.append("where gongys = '").append(rsl.getString("gongys")).append("' ")
			.append("and shihpz = '").append(rsl.getString("shihpz")).append("' ")
			.append("and yunsdw = '").append(rsl.getString("yunsdw")).append("' ")
			.append("and to_date('").append(rsl.getString("daohrq")).append("','yyyy-mm-dd')").append(">=to_date('").append(getRiqi())
			.append("','yyyy-mm-dd') and to_date('").append(rsl.getString("daohrq")).append("','yyyy-mm-dd')").append("<to_date('").append(getRiq2())
			.append("','yyyy-mm-dd')+1;");
		}
		Str_sql.append("End;");
		if(rsl==null){
			this.setMsg("数据没有进行改动，无需保存！");
		}else{
			if(con.getUpdate(Str_sql.toString())>=0){
				
				con.commit();
				this.setMsg("保存成功！");
			}else{
				con.rollBack();
				this.setMsg("保存失败！");
			}
		}
	}


	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer Str_sql = new StringBuffer();
		String sql = "select gongys,shihpz,yunsdw from shihcptmp "
				+ " where gongysb_id = 0 and shihpzb_id = 0 and yunsdwb_id = 0 "
				+ " and daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
				+ " Group by gongys,shihpz,yunsdw";
		ResultSetList rsl1 = con
				.getResultSetList(sql);
		Str_sql.append("begin\n");
		while(rsl1.next()){
			int gysid = 0;
			int pzid = 0;
			int ysdwid = 0;
			ResultSetList rsl2 = null;
			rsl2 = con
				.getResultSetList("select gongysb_id from vwshihgysdy "
					+ " where gongys = '" + rsl1.getString("gongys")
					+ "' order by id desc");
			if(rsl2.next()){
				gysid = rsl2.getInt("gongysb_id");
			}
			rsl2.close();
			
			rsl2 = con
			.getResultSetList("select shihpzb_id from vwshihpzdy "
				+ " where shihpz = '" + rsl1.getString("shihpz")
				+ "' order by id desc");
		    if(rsl2.next()){
		    	pzid = rsl2.getInt("shihpzb_id");
		    }
			rsl2.close();
			
			rsl2 = con
			.getResultSetList("select yunsdwb_id from vwshihysdwdy "
				+ " where yunsdw = '" + rsl1.getString("yunsdw")
				+ "' order by id desc");
		    if(rsl2.next()){
		    	ysdwid = rsl2.getInt("yunsdwb_id");
		    }
		    rsl2.close();
			
			Str_sql.append("update shihcptmp set gongysb_id = ").append(gysid).append(",")
			.append("shihpzb_id = ").append(pzid).append(",")
			.append("yunsdwb_id = ").append(ysdwid)
			.append(" where daohrq>=to_date('").append(getRiqi()).append("','yyyy-mm-dd') and daohrq<to_date('").append(getRiq2()).append("','yyyy-mm-dd')+1")
			.append(" and gongys = '").append(rsl1.getString("gongys")).append("'")
			.append(" and shihpz = '").append(rsl1.getString("shihpz")).append("'")
			.append(" and yunsdw = '").append(rsl1.getString("yunsdw")).append("';");
		}
		Str_sql.append("End;");
		if(rsl1.getRows()>0){
			con.getUpdate(Str_sql.toString());
			con.commit();
		}
		String sql_xs = "select nvl(g.mingc,0) as gongys_mc,nvl(p.mingc,0) as shihpz_mc,nvl(y.mingc,0) as yunsdw_mc," 
				+ " s.gongys as gongys,s.shihpz as shihpz,s.yunsdw as yunsdw,daohrq,count(s.id) ches,sum(s.maoz) zongmz,sum(s.piz) zongpz"
				+ " from shihgysb g,shihpzb p,yunsdwb y,shihcptmp s"
				+ " where g.id(+) = s.gongysb_id "
				+ " and p.id(+) = s.shihpzb_id "
				+ " and y.id(+) = s.yunsdwb_id"
				+ " and daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
				+ " Group by gongys,shihpz,yunsdw,daohrq,g.mingc,p.mingc,y.mingc";
		ResultSetList rsl = con
			.getResultSetList(sql_xs);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("shihcyb");
		egu.getColumn("gongys_mc").setHeader("供应商x");
		egu.getColumn("gongys_mc").setWidth(70);
		egu.getColumn("shihpz_mc").setHeader("品种x");
		egu.getColumn("shihpz_mc").setWidth(70);
		egu.getColumn("yunsdw_mc").setHeader("运输单位x");
		egu.getColumn("yunsdw_mc").setWidth(70);
		egu.getColumn("gongys").setHeader("供应商");
		egu.getColumn("gongys").setWidth(70);
		egu.getColumn("gongys").editor = null;
		egu.getColumn("shihpz").setHeader("品种");
		egu.getColumn("shihpz").setWidth(70);
		egu.getColumn("shihpz").editor = null;
		egu.getColumn("yunsdw").setHeader("运输单位");
		egu.getColumn("yunsdw").setWidth(70);
		egu.getColumn("yunsdw").editor = null;
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").editor = null;
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(70);
		egu.getColumn("ches").editor = null;
		egu.getColumn("zongmz").setHeader("总毛重");
		egu.getColumn("zongmz").setWidth(70);
		egu.getColumn("zongmz").editor = null;
		egu.getColumn("zongpz").setHeader("总皮重");
		egu.getColumn("zongpz").setWidth(70);
		egu.getColumn("zongpz").editor = null;
		
//		到货日期查询
		egu.addTbarText("到货日期:");
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
		
		egu.addTbarText("-");
		
//		 设置供应商下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("gongys_mc").setEditor(c1);
		c1.setEditable(true);
		String gysSql = "select id,mingc from shihgysb order by mingc";
		egu.getColumn("gongys_mc").setDefaultValue("");
		egu.getColumn("gongys_mc").setComboEditor(egu.gridId,
				new IDropDownModel(gysSql));
//		 设置品种下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("shihpz_mc").setEditor(c2);
		c2.setEditable(true);
		String pzSql = "select id,mingc from shihpzb order by mingc";
		egu.getColumn("shihpz_mc").setDefaultValue("");
		egu.getColumn("shihpz_mc").setComboEditor(egu.gridId,
				new IDropDownModel(pzSql));
//		 设置运输单位下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("yunsdw_mc").setEditor(c4);
		c4.setEditable(true);
		String ysdwSql = "select id,mingc from yunsdwb order by mingc";
		egu.getColumn("yunsdw_mc").setDefaultValue("");
		egu.getColumn("yunsdw_mc").setComboEditor(egu.gridId,
				new IDropDownModel(ysdwSql));
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			visit.setString1("");	//change
			getSelectData();
		}
	}
}
