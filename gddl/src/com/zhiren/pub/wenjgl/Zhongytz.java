package com.zhiren.pub.wenjgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhongytz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
//			getSelectData();
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select fabwjb.id,renyxxb.quanc,to_char(fabwjb.shij,'yyyy-mm-dd HH24:MI:SS')shij,wenjb.biaot,diancxxb.mingc,decode(fabwjb.zhid,1,'是','否')zhid from fabwjb,renyxxb,wenjb,diancxxb where fabwjb.renyxxb_id=renyxxb.id and fabwjb.wenjb_id=wenjb.id and fabwjb.diancxxb_id=diancxxb.id"
						+" and to_char(fabwjb.shij,'YYYY-MM-DD')>='"+getRiq()+"' and  to_char(fabwjb.shij,'YYYY-MM-DD')<='"+getriq1()+"'");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fabwjb");
		egu.getColumn("quanc").setHeader("人员");
		egu.getColumn("shij").setHeader("时间");
		egu.getColumn("shij").setWidth(200);
		egu.getColumn("biaot").setHeader("文件");
		egu.getColumn("mingc").setHeader("接收人");
		egu.getColumn("zhid").setHeader("重要通知");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "是"));
		l.add(new IDropDownBean(0, "否"));
		egu.getColumn("zhid").setEditor(new ComboBox());
		egu.getColumn("zhid").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("zhid").setDefaultValue("否");
		egu.getColumn("quanc").setEditor(null);
		egu.getColumn("quanc").update=false;
		egu.getColumn("shij").setEditor(null);
		egu.getColumn("shij").update=false;
		egu.getColumn("biaot").setEditor(null);
		egu.getColumn("biaot").update=false;
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").update=false;
		
		egu.addTbarText("发布日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("riq","Form0");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		egu.addToolbarItem(df.getScript());
		
		DateField df1 = new DateField();
		df1.setValue(this.getriq1());
		df1.Binding("riq1","Form0");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		egu.addToolbarItem(df1.getScript());
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(e.column!=6){return;};var biaot=e.record.get('BIAOT');var records=gridDiv_ds.getRange(e.row);for (var i=0;i<records.length;i++){if(records[i].get('BIAOT')==biaot){records[i].set('ZHID',e.value);}}});");
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
		}
		getSelectData();
	}
	private String riq1;
	public String getriq1() {
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setriq1(String riq1) {
		this.riq1 = riq1;
	}
	private String riq;
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -15, DateUtil.AddType_intDay));
		}
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
}
