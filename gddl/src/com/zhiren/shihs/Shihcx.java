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

public class Shihcx extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(strchange);
		StringBuffer Str_sql = new StringBuffer();
		long mlngDiancxxb_id=visit.getDiancxxb_id();
		Str_sql.append("begin\n");
		String beiz = "";
		while(rsl.next()){
			beiz = rsl.getString("beiz");
			if(beiz == null){
				beiz = " ";
			}
			
			long shihzlbid= 0;
			shihzlbid = rsl.getLong("shihzlb_id");
			if((shihzlbid)>0){
				Str_sql.append("update shihzlb set ")
					.append("bianm = ").append(rsl.getString("huaybm")).append(",")
					.append("huayy = ").append("'").append(rsl.getString("huayy")).append("'").append(",")
					.append("huaysj = ").append("to_date('").append(rsl.getString("huaysj")).append("','yyyy-mm-dd'),")
					.append("caco3 = ").append(rsl.getInt("caco3")).append(",")
					.append("caoh2 = ").append(rsl.getInt("caoh2")).append(",")
					.append("xid = ").append(rsl.getInt("xid")).append(",")
					.append("mgco3 = ").append(rsl.getInt("mgco3")).append(",")
					.append("sio2 = ").append(rsl.getInt("sio2")).append(",")
					.append("beiz = ").append("'").append(beiz).append("'")
					.append(" where id = ").append(rsl.getString("shihzlb_id")).append(";");
			}
			if(shihzlbid==0){
				String id =MainGlobal.getNewID(mlngDiancxxb_id);
				Str_sql.append("insert into shihzlb(id,bianm,huayy,huaysj,caco3,caoh2,xid,mgco3,sio2,lurry,beiz) values(")
					.append(id).append(",")
					.append(rsl.getString("huaybm")).append(",")
					.append("'").append(rsl.getString("huayy")).append("'").append(",")
					.append("to_date('").append(rsl.getString("huaysj")).append("','yyyy-mm-dd'),")
					.append(rsl.getInt("caco3")).append(",")
					.append(rsl.getInt("caoh2")).append(",")
					.append(rsl.getInt("xid")).append(",")
					.append(rsl.getInt("mgco3")).append(",")
					.append(rsl.getInt("sio2")).append(",")
					.append("'").append(v.getRenymc()).append("',")
					.append("'").append(rsl.getString("beiz")).append("')").append(";");
				
				Str_sql.append("update shihcyb set ")
					.append("shihzlb_id = ").append(id)
					.append(" where id = ").append(rsl.getString("shihcyb_id")).append(";");
			}
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
		ResultSetList rsl = con
				.getResultSetList("select cy.id as shihcyb_id,cy.bianm as caiybm,nvl(zl.id,0) as shihzlb_id,zl.bianm as huaybm,  " 
						+ " zl.huayy,zl.huaysj,zl.caco3,zl.caoh2,zl.xid,zl.mgco3,zl.sio2,zl.beiz "
						+ " from shihcyb cy,shihzlb zl "
						+ " where cy.shihzlb_id = zl.id(+) "
						+ " and cy.caiysj>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and cy.caiysj<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
						+ " order by cy.xuh");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("shihcyb");
		egu.getColumn("shihcyb_id").setHeader("SHIHCYB_ID");
		egu.getColumn("shihcyb_id").setHidden(true);
		egu.getColumn("shihcyb_id").editor = null;
//		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));
//		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("caiybm").setHeader("采样编码");
		egu.getColumn("caiybm").setWidth(70);
		egu.getColumn("caiybm").editor = null;
		egu.getColumn("shihzlb_id").setHeader("SHIHZLB_ID");
		egu.getColumn("shihzlb_id").setHidden(true);
		egu.getColumn("shihzlb_id").editor = null;
		egu.getColumn("huaybm").setHeader("化验编码");
		egu.getColumn("huaybm").setWidth(70);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setWidth(70);
		egu.getColumn("huayy").editor.allowBlank = false;//化验员不能为空
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setWidth(70);
		egu.getColumn("caco3").setHeader("碳酸钙");
		egu.getColumn("caco3").setWidth(70);
		egu.getColumn("caoh2").setHeader("氢氧化钙");
		egu.getColumn("caoh2").setWidth(70);
		egu.getColumn("xid").setHeader("细度");
		egu.getColumn("xid").setWidth(70);
		egu.getColumn("mgco3").setHeader("碳酸镁");
		egu.getColumn("mgco3").setWidth(70);
		egu.getColumn("sio2").setHeader("二氧化硅");
		egu.getColumn("sio2").setWidth(60);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(60);
		
		
//		到货日期查询
		egu.addTbarText("采样日期:");
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
