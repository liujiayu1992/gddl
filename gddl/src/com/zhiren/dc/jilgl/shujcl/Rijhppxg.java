package com.zhiren.dc.jilgl.shujcl;

import java.util.Date;

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
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;

public class Rijhppxg extends BasePage implements PageValidateListener {
	
	public static final String YUNSFS_QY = "QY";//汽运
	public static final String YUNSFS_HY = "HY";//火运
	public static final String YUNSFS_All = "ALL";//全部
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString1(yunsfs);
	}
	
	public String getRiqi() {
		if(((Visit)this.getPage().getVisit()).getString2()== null){
			setRiqi(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay)));
		}
		return ((Visit)this.getPage().getVisit()).getString2();
	}

	public void setRiqi(String riqi) {
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
		Visit v = (Visit)this.getPage().getVisit();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
		String sql = "begin\n";
		while(rs.next()){
			long qicrjhid = getExtGrid().getColumn("qicrjh").combo.getBeanId(rs.getString("qicrjh"));
			sql += "update chepbtmp set qicrjhb_id = "+
			qicrjhid 
			+" where qicrjhb_id = "+rs.getString("id")+";\n";
			StringBuffer callSql = new StringBuffer();
			callSql.append("{call saveqicjhpp(").append(qicrjhid)
			.append(",'").append(rs.getString("meikdwmc")).append("','").append(rs.getString("yunsdw"))
			.append("','").append(rs.getString("pinz")).append("',").append(v.getDiancxxb_id()).append(")}");
			con.UpdateCall(callSql.toString());
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
		String rq = DateUtil.FormatOracleDate(new Date());
		if(getRiqi() != null){
			rq = DateUtil.FormatOracleDate(getRiqi());
		}
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
		JDBCcon con = new JDBCcon();
		String sql = 
			"select j.mingc qicrjh,c.meikdwmc,c.pinz,c.yunsdw,c.jihkj,c.chec\n" +
			",count(c.id) ches,sum(c.maoz) maoz,sum(c.piz) piz,j.id\n" + 
			"       from chepbtmp c,qicrjhb j \n" + 
			"where c.daohrq = " + rq + sql_yunsfs + "\n" +
			"      and j.diancxxb_id= c.diancxxb_id and c.diancxxb_id =" +v.getDiancxxb_id() + " \n" +
			"      and c.fahb_id = 0 " +
			"      and c.qicrjhb_id = j.id\n" + 
			"group by j.mingc,c.meikdwmc,c.pinz,c.yunsdw,c.jihkj,c.chec,j.id";
		
		ResultSetList rs = con.getResultSetList(sql);
		rs = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
//		sql =  "select * from qicrjhb where riq = "+rq;
		egu.getColumn("qicrjh").setHeader("日计划名称");
		egu.getColumn("qicrjh").setWidth(200);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdwmc").setWidth(120);
		egu.getColumn("meikdwmc").setEditor(null);
		egu.getColumn("pinz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdw").setWidth(100);
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("jihkj").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkj").setWidth(80);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("ches").setHeader(Locale.ches_fahb);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setEditor(null);
		
//		到货日期
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
//		汽车日计划
		ComboBox cjh = new ComboBox();
		egu.getColumn("qicrjh").setEditor(cjh);
		cjh.setEditable(true);
		String jh = "select id,mingc from qicrjhb where riq = "+rq;
		egu.getColumn("qicrjh").setComboEditor(egu.gridId,
				new IDropDownModel(jh));
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//只能单选中行

		egu.addPaging(25);
		
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
			setRiqi(null);
			setYunsfsModel(null);
			getSelectData();
		}
		
	}
}