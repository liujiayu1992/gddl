package com.zhiren.dc.jilgl.gongl.rijh;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.DateUtil;

public class Rijhpp extends BasePage implements PageValidateListener {
	
	public static final String YUNSFS_QY = "QY";//汽运
	public static final String YUNSFS_HY = "HY";//火运
	public static final String YUNSFS_All = "ALL";//全部
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString1(yunsfs);
	}
	
	public String getRiq() {
		if(((Visit)this.getPage().getVisit()).getString2()== null){
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay)));
		}
		return ((Visit)this.getPage().getVisit()).getString2();
	}

	public void setRiq(String riqi) {
		((Visit)this.getPage().getVisit()).setString2(riqi);
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private IDropDownModel getYunsfsModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null){
			setYunsfsModel(new IDropDownModel(SysConstant.SQL_yunsfs));
		}
		return (IDropDownModel)((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	private void setYunsfsModel(IDropDownModel value){
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	private void Save() {
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
		String sql = "begin\n";
		while(rs.next()){
			long qicrjhid = getExtGrid().getColumn("qicrjhb_id").combo.getBeanId(rs.getString("qicrjhb_id"));
			sql += "update chepbtmp set qicrjhb_id = "+
			qicrjhid 
			+" where id = "+rs.getString("id")+";\n";
		}
		sql += "end;\n";
		con.getUpdate(sql);
		con.Close();
	}
	
	private boolean _RefurbishChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit v = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rq = DateUtil.FormatOracleDate(new Date());
		if(getRiq() != null){
			rq = DateUtil.FormatOracleDate(getRiq());
		}
//		进入页面即自动匹配计划ID
		StringBuffer callSql = new StringBuffer();
		callSql.append("{call updateChepbtmpjhid(").append(rq)
		.append(",").append(v.getDiancxxb_id()).append(")}");
		con.UpdateCall(callSql.toString());
//		刷新数据
		String sql_yunsfs = "";
		String ysfs = "全部";
		if(YUNSFS_HY.equals(getYunsfs())) {
			ysfs = getYunsfsModel().getBeanValue(SysConstant.YUNSFS_HUOY);
			sql_yunsfs = " and yunsfs = '"+ysfs+"' ";
		}else
			if(YUNSFS_QY.equals(getYunsfs())) {
				ysfs = getYunsfsModel().getBeanValue(SysConstant.YUNSFS_QIY);
				sql_yunsfs = " and yunsfs = '"+ysfs+"' ";
			}
		
		String sql = 
			"select c.id,q.mingc qicrjhb_id,c.meikdwmc\n" +
			",c.yunsdw,c.piaojh,c.cheph,c.maoz,c.piz,c.zhongcsj,c.qingcsj,c.lury\n" + 
			"from chepbtmp c,qicrjhb q\n" + 
			"where c.qicrjhb_id = q.id(+)\n" + 
			"and daohrq = "+rq+" and c.diancxxb_id = "+v.getDiancxxb_id() + sql_yunsfs
			+" order by c.qingcsj";
		ResultSetList rs = con.getResultSetList(sql);
		rs = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
//		sql =  "select * from qicrjhb where riq = "+rq;
		egu.getColumn("qicrjhb_id").setHeader(Locale.qicrjh_mingc);
		egu.getColumn("qicrjhb_id").setWidth(200);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdwmc").setWidth(120);
		egu.getColumn("meikdwmc").setEditor(null);
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdw").setWidth(100);
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu.getColumn("piaojh").setWidth(80);
		egu.getColumn("piaojh").setEditor(null);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("zhongcsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("zhongcsj").setWidth(110);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("qingcsj").setHeader(Locale.qingcsj_chepb);
		egu.getColumn("qingcsj").setWidth(110);
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("lury").setHeader(Locale.lury_chepb);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("lury").setEditor(null);
//		到货日期
		egu.addTbarText(Locale.daohrq_id_fahb+":");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
//		汽车日计划
		ComboBox cjh = new ComboBox();
		egu.getColumn("qicrjhb_id").setEditor(cjh);
		cjh.setEditable(true);
		String jh = "select id,mingc from qicrjhb where zhuangt = 0 and riq = "+rq + " and diancxxb_id ="+v.getDiancxxb_id();
		egu.getColumn("qicrjhb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jh));
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//只能单选中行
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(21);
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setYunsfs(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			if(reportType == null) {
				setYunsfs(YUNSFS_All);
			}
			setRiq(null);
			setYunsfsModel(null);
			getSelectData();
		}
		
	}
}