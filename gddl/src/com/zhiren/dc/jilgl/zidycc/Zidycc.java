package com.zhiren.dc.jilgl.zidycc;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zidycc extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		int flag = 0;
		sb.append("begin \n");
		ResultSetList rsdel=this.getExtGrid().getDeleteResultSet(this.getChange());
		while(rsdel.next()) {
			sb.append(" delete from zidyccb where id="+rsdel.getString("id")).append("; \n");
		}
		
		rsdel = this.getExtGrid().getModifyResultSet(this.getChange());	
		while(rsdel.next()) {
			if ("0".equals(rsdel.getString("id"))) {
				sb.append(" insert into zidyccb (id, chec, qissj, jiezsj) values (")
					.append("getnewid(").append(visit.getDiancxxb_id()).append("),\n")
					.append(rsdel.getString("chec")).append(",\n")
					.append("to_date('1900-01-01 " + ((IDropDownModel)getExtGrid().getColumn("qissj_hour").combo).getBeanId(rsdel.getString("qissj_hour"))+":"+((IDropDownModel)getExtGrid().getColumn("qissj_min").combo).getBeanId(rsdel.getString("qissj_min"))+"','yyyy-mm-dd hh24:mi')").append(",\n")
					.append("to_date('1900-01-01 " + ((IDropDownModel)getExtGrid().getColumn("jiezsj_hour").combo).getBeanId(rsdel.getString("jiezsj_hour"))+":"+((IDropDownModel)getExtGrid().getColumn("jiezsj_min").combo).getBeanId(rsdel.getString("jiezsj_min"))+"','yyyy-mm-dd hh24:mi')").append("\n")
					.append("); \n");
			} else {
				sb.append("update zidyccb set chec='").append(rsdel.getString("chec")).append("', \n")
					.append("qissj=to_date('1900-01-01 ").append(((IDropDownModel)getExtGrid().getColumn("qissj_hour").combo).getBeanId(rsdel.getString("qissj_hour"))+":"+((IDropDownModel)getExtGrid().getColumn("qissj_min").combo).getBeanId(rsdel.getString("qissj_min"))).append("','yyyy-mm-dd hh24:mi'), \n")
					.append("jiezsj=to_date('1900-01-01 ").append(((IDropDownModel)getExtGrid().getColumn("jiezsj_hour").combo).getBeanId(rsdel.getString("jiezsj_hour"))+":"+((IDropDownModel)getExtGrid().getColumn("jiezsj_min").combo).getBeanId(rsdel.getString("jiezsj_min"))+ "','yyyy-mm-dd hh24:mi') \n")
					.append("where id=").append(rsdel.getString("id")).append("; \n");
			}
		}
		sb.append("end; \n");
		if (sb.length()>13) {
			flag = con.getUpdate(sb.toString());
			if (flag==-1) {
				this.setMsg("保存失败!");
				 
			} else {
				this.setMsg(" 保存成功!");
			}
		}
	}
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick= true;
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
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		StringBuffer sSQL = new StringBuffer();
		sSQL.append("select z.id,z.chec, \n")
			.append("to_char(z.qissj,'hh24') || '时' as qissj_hour, \n")
			.append("to_char(z.qissj,'mi') || '分' as qissj_min, \n")
			.append("nvl('至','') as fromto,")
			.append("to_char(z.jiezsj,'hh24') || '时' as jiezsj_hour, \n")
			.append("to_char(z.jiezsj,'mi') || '分'  as jiezsj_min \n")
			.append("from zidyccb z");
		
		ResultSetList rsl = con.getResultSetList(sSQL.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSQL);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");		
		egu.getColumn("chec").setHeader("车次");	
		egu.getColumn("chec").setWidth(120);
		egu.getColumn("qissj_hour").setHeader("起始时间(时)");
		egu.getColumn("qissj_hour").setWidth(100);
		egu.getColumn("qissj_min").setHeader("起始时间(分)");
		egu.getColumn("qissj_min").setWidth(100);
		egu.getColumn("fromto").setHeader("至");
		egu.getColumn("fromto").setWidth(50);
		egu.getColumn("fromto").setDefaultValue("至");
		egu.getColumn("fromto").setEditor(null);
		egu.getColumn("jiezsj_hour").setHeader("截止时间(时)");
		egu.getColumn("jiezsj_hour").setWidth(100);
		egu.getColumn("jiezsj_min").setHeader("截止时间(分)");
		egu.getColumn("jiezsj_min").setWidth(100);	
		
		egu.getColumn("qissj_hour").setEditor(new ComboBox());
		egu.getColumn("qissj_hour").setComboEditor(egu.gridId, (IDropDownModel)getQissjHourModel());
		egu.getColumn("qissj_hour").setReturnId(true);
		
		egu.getColumn("qissj_min").setEditor(new ComboBox());
		egu.getColumn("qissj_min").setComboEditor(egu.gridId, (IDropDownModel)getQissjMinModel());
		egu.getColumn("qissj_min").setReturnId(true);
		
		egu.getColumn("jiezsj_hour").setEditor(new ComboBox());
		egu.getColumn("jiezsj_hour").setComboEditor(egu.gridId, (IDropDownModel)getJiezsjHourModel());
		egu.getColumn("jiezsj_hour").setReturnId(true);
		
		egu.getColumn("jiezsj_min").setEditor(new ComboBox());
		egu.getColumn("jiezsj_min").setComboEditor(egu.gridId, (IDropDownModel)getJiezsjMinModel());
		egu.getColumn("jiezsj_min").setReturnId(true);
		
		GridButton gRefresh = new GridButton("刷新",
			"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton("保存",GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
	}
	
	//起始时间（时）
	private IDropDownBean _QissjHourValue;
	
	public void setQissjHourValue(IDropDownBean Value) {
		if (_QissjHourValue != Value) {
			_QissjHourValue = Value;
		}
	}

	public IDropDownBean getQissjHourValue() {
		if (_QissjHourValue == null) {
			_QissjHourValue =(IDropDownBean) getQissjHourModel().getOption(0);
		}
		return _QissjHourValue;
	}
	
	private IPropertySelectionModel _QissjHourModel;
	
	public void setQissjHourModel(IPropertySelectionModel value) {
		_QissjHourModel = value;
	}
	
	public IPropertySelectionModel getQissjHourModel() {
		if (_QissjHourModel == null) {
			getQissjHourModels();
		}
		return _QissjHourModel;
	}
	
	private void getQissjHourModels() {
		List list = new ArrayList();
		for (int i=0; i<24;i++) {
			String sTemp = "";
			if (i<10) {
				sTemp = "0";
			}
			sTemp = sTemp + i + "时";
			
			list.add(new IDropDownBean(i, sTemp));
		}
		_QissjHourModel = new IDropDownModel(list);
	}
	
	//起始时间（分）
	private IDropDownBean _QissjMinValue;
	
	public void setQissjMinValue(IDropDownBean Value) {
		if (_QissjMinValue != Value) {
			_QissjMinValue = Value;
		}
	}

	public IDropDownBean getQissjMinValue() {
		if (_QissjMinValue == null) {
			_QissjMinValue =(IDropDownBean) getQissjMinModel().getOption(0);
		}
		return _QissjMinValue;
	}
	
	private IPropertySelectionModel _QissjMinModel;
	
	public void setQissjMinModel(IPropertySelectionModel value) {
		_QissjMinModel = value;
	}
	
	public IPropertySelectionModel getQissjMinModel() {
		if (_QissjMinModel == null) {
			getQissjMinModels();
		}
		return _QissjMinModel;
	}
	
	private void getQissjMinModels() {
		List list = new ArrayList();
		for (int i=0; i<60;i++) {
			String sTemp = "";
			if (i<10) {
				sTemp = "0";
			}
			sTemp = sTemp + i + "分";
			
			list.add(new IDropDownBean(i, sTemp));
		}
		_QissjMinModel = new IDropDownModel(list);
	}
	
	//截止时间（时）
	private IDropDownBean _JiezsjHourValue;
	
	public void setJiezsjHourValue(IDropDownBean Value) {
		if (_JiezsjHourValue != Value) {
			_JiezsjHourValue = Value;
		}
	}

	public IDropDownBean getJiezsjHourValue() {
		if (_JiezsjHourValue == null) {
			_JiezsjHourValue =(IDropDownBean) getJiezsjHourModel().getOption(0);
		}
		return _JiezsjHourValue;
	}
	
	private IPropertySelectionModel _JiezsjHourModel;
	
	public void setJiezsjHourModel(IPropertySelectionModel value) {
		_JiezsjHourModel = value;
	}
	
	public IPropertySelectionModel getJiezsjHourModel() {
		if (_JiezsjHourModel == null) {
			getJiezsjHourModels();
		}
		return _JiezsjHourModel;
	}
	
	private void getJiezsjHourModels() {
		List list = new ArrayList();
		for (int i=0; i<24;i++) {
			String sTemp = "";
			if (i<10) {
				sTemp = "0";
			}
			sTemp = sTemp + i + "时";
			
			list.add(new IDropDownBean(i, sTemp));
		}
		_JiezsjHourModel = new IDropDownModel(list);
	}
	
	//截止时间（分）
	private IDropDownBean _JiezsjMinValue;
	
	public void setJiezsjMinValue(IDropDownBean Value) {
		if (_JiezsjMinValue != Value) {
			_JiezsjMinValue = Value;
		}
	}

	public IDropDownBean getJiezsjMinValue() {
		if (_JiezsjMinValue == null) {
			_JiezsjMinValue =(IDropDownBean) getJiezsjMinModel().getOption(0);
		}
		return _JiezsjMinValue;
	}
	
	private IPropertySelectionModel _JiezsjMinModel;
	
	public void setJiezsjMinModel(IPropertySelectionModel value) {
		_JiezsjMinModel = value;
	}
	
	public IPropertySelectionModel getJiezsjMinModel() {
		if (_JiezsjMinModel == null) {
			getJiezsjMinModels();
		}
		return _JiezsjMinModel;
	}
	
	private void getJiezsjMinModels() {
		List list = new ArrayList();
		for (int i=0; i<60;i++) {
			String sTemp = "";
			if (i<10) {
				sTemp = "0";
			}
			sTemp = sTemp + i + "分";
			
			list.add(new IDropDownBean(i, sTemp));
		}
		_JiezsjMinModel = new IDropDownModel(list);
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
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
			getSelectData();
		}
	}
}
