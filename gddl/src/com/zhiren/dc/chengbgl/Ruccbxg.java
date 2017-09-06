package com.zhiren.dc.chengbgl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author rock
 * @since 2009-12-03
 * @version v1.0
 */
public class Ruccbxg extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	private String riq1;
	public String getRiq1() {
		return riq1;
	}
	public void setRiq1(String riq) {
		this.riq1 = riq;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		int flag = getExtGrid().Save(getChange(), visit);
		if(flag ==-1){
			setMsg("保存失败");
		}else{
			setMsg("保存成功");
		}
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String strdhrq = DateUtil.FormatOracleDate(getRiq());
		String strdhrq1 = DateUtil.FormatOracleDate(getRiq1());
		String sql = 
			"select r.id,fh.mk,fh.pz,round_new(fh.qnet_ar/0.0041816,0) qnet_ar_k,fh.qnet_ar,r.meij,r.meijs,r.yunj,r.yunjs,\n" +
			"r.jiaohqzf,r.zaf,r.daozzf, r.qitfy from\n" + 
			"(select f.ruccbb_id,max(m.mingc ) mk, max(p.mingc) pz, sum(f.laimsl) sl,\n" + 
			"decode(sum(f.laimsl),0,0,round_new(sum(f.laimsl * z.qnet_ar)/sum(laimsl),2)) qnet_ar\n" + 
			"from fahb f, meikxxb m, pinzb p, zhilb z\n" + 
			"where f.meikxxb_id = m.id and f.pinzb_id = p.id\n" + 
			"and f.zhilb_id = z.id and f.daohrq >="+strdhrq +" and f.daohrq <= "+strdhrq1+"\n" + 
			"group by f.ruccbb_id) fh, ruccb r\n" + 
			"where fh.ruccbb_id = r.id order by r.id";

		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.setTableName("ruccb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		//egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置每页显示行数
		egu.addPaging(25);

		egu.getColumn("mk").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("mk").setWidth(80);
		egu.getColumn("mk").setEditor(null);
		egu.getColumn("mk").update = false;
		egu.getColumn("pz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pz").setWidth(50);
		egu.getColumn("pz").setEditor(null);
		egu.getColumn("pz").update = false;
		egu.getColumn("qnet_ar_k").setHeader("热值Kcal/kg");
		egu.getColumn("qnet_ar_k").setWidth(70);
		egu.getColumn("qnet_ar_k").update = false;
		egu.getColumn("qnet_ar").setHeader("热值MJ/kg");
		egu.getColumn("qnet_ar").setWidth(70);
		((NumberField)egu.getColumn("qnet_ar").editor).setDecimalPrecision(2);
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("meijs").setEditor(null);
		egu.getColumn("yunj").setHeader("运价");
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunjs").setHeader("运价税");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setEditor(null);
		egu.getColumn("jiaohqzf").setHeader("交货前杂费");
		egu.getColumn("jiaohqzf").setWidth(60);
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("qitfy").setHeader("其他费用");
		egu.getColumn("qitfy").setWidth(60);

		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		DateField df1 = new DateField();
		df1.Binding("RIQ1","");
		df1.setValue(getRiq1());
		egu.addToolbarItem(df1.getScript());
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("保存", GridButton.ButtonType_Save, "SaveButton");
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
		if(getExtGrid()==null){
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
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
			setRiq(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date())));
			setRiq1(DateUtil.FormatDate(new Date()));
			init();
		}
	} 
	
	private void init() {
		setExtGrid(null);
		getSelectData();
	}
}
