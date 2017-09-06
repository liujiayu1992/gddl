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

public class Rijhcywh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	public String getRiq() {
		if(((Visit)this.getPage().getVisit()).getString2()== null){
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), 0,
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
	
	private void Save() {
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
		String sql = "begin\n";
		while(rs.next()){
			long cunywzid = getExtGrid().getColumn("cywz").combo.getBeanId(rs.getString("cywz"));
			sql += "update caiyb set cunywzb_id = "+
			cunywzid 
			+" where id = "+rs.getString("cid")+";\n";
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
	
	private boolean _CisChick = false;

	public void CisButton(IRequestCycle cycle) {
		_CisChick = true;
	}
	
	private void Cis(IRequestCycle cycle){
		if(getChange()==null || "".equals(getChange())){
			setMsg("请选择一条记录!");
			return;
		}
		Visit v = (Visit)this.getPage().getVisit();
		v.setString10(getChange());
		cycle.activate("Rijhcyds");
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CisChick) {
			_CisChick = false;
			System.out.println(getChange());
			Cis(cycle);
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
		String sql = 
			"select q.id qid, c.id cid, q.mingc, c.bianm\n" +
			", getQicrjh_Caiybh(c.zhilb_id) cybm\n" + 
			", w.mingc cywz, nvl(sum(p.yunmcs),0) ches\n" + 
			"from qicrjhb q, caiyb c, qicrjhcpb p,cunywzb w\n" + 
			"where q.zhilb_id = c.zhilb_id and q.id = p.qicrjhb_id(+)\n" + 
			"and c.cunywzb_id = w.id(+) and q.riq = "+rq+"\n" + 
			"and q.diancxxb_id = "+v.getDiancxxb_id()+"\n" + 
			"group by q.id,c.id,q.mingc,c.bianm,c.zhilb_id,w.mingc";
			
		ResultSetList rs = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
//		sql =  "select * from qicrjhb where riq = "+rq;
		egu.getColumn("qid").setHidden(true);
		egu.getColumn("qid").setEditor(null);
		egu.getColumn("cid").setHidden(true);
		egu.getColumn("cid").setEditor(null);
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").setWidth(200);
		egu.getColumn("mingc").setHeader(Locale.qicrjh_mingc);
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("bianm").setHeader(Locale.caiybh_zhuanmb);
		egu.getColumn("cybm").setEditor(null);
		egu.getColumn("cybm").setWidth(100);
		egu.getColumn("cybm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("cywz").setEditor(null);
		egu.getColumn("cywz").setWidth(100);
		egu.getColumn("cywz").setHeader(Locale.mingc_cunywzb);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(100);
		egu.getColumn("ches").setHeader(Locale.qicrjh_jhcs);
//		到货日期
		egu.addTbarText(Locale.daohrq_id_fahb+":");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
//		汽车日计划
		ComboBox cywz = new ComboBox();
		egu.getColumn("cywz").setEditor(cywz);
		cywz.setEditable(true);
		String jh = "select id,mingc from cunywzb where zhuangt = 0 and diancxxb_id ="+v.getDiancxxb_id();
		egu.getColumn("cywz").setComboEditor(egu.gridId,
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
		GridButton cx = new GridButton("采样次数", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('QID');"
				+ " var Cobjid = document.getElementById('CHANGE');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('CisButton').click();}else{"
				+ MainGlobal.getExtMessageBox("请选择一条记录",false)+"}}");
		egu.addTbarBtn(cx);
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(!visit.getActivePageName().toString().equalsIgnoreCase("Rijhcyds")){
				setRiq(null);
			}
			visit.setActivePageName(this.getPageName().toString());
			getSelectData();
		}
	}
}