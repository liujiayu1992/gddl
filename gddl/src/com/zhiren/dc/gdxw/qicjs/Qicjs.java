package com.zhiren.dc.gdxw.qicjs;

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

public class Qicjs extends BasePage implements PageValidateListener {
//	界面用户提示
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
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin \n");
		
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("id"))||"".equals(mdrsl.getString("id"))) {
				
			} else {
				sbsql.append("update suocztb set jiessj = sysdate,jiesry='"+visit.getRenymc()+"',")
				.append(" zt= decode('"+ mdrsl.getString("zt")+ "','已锁车',1,2)").append(" where id = ").append(mdrsl.getString("id"))
				.append("; \n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
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
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
		long SuocID=this.getMeikdqmcValue().getId();
		
		sb.append("select s.id, c.cheph,c.meikdwmc,c.yunsdw,to_char(s.suocsj,'yyyy-mm-dd hh24:mi:ss') as suocsj,s.suocry,s.suocyy,"); 
		if(SuocID==2){
			sb.append("to_char(s.jiessj,'yyyy-mm-dd hh24:mi:ss') as jiessj,s.jiesry,\n");
		}
		sb.append(	"decode(s.zt,1,'已锁车','已解锁') as zt\n");
		sb.append("from chepbtmp c,suocztb s,diancxxb d\n");
		sb.append(" where s.chepbtmp_id=c.id\n");
		sb.append(" and c.diancxxb_id=d.id\n");
		sb.append(" and s.zt="+SuocID+"\n"); 
		sb.append(" AND d.ID = ").append(visit.getDiancxxb_id());
		sb.append("  order by s.suocsj");

		
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设置是否可以编辑
		egu.setTableName("suocztb");
	
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").width=80;
		egu.getColumn("cheph").editor=null;
		egu.getColumn("meikdwmc").setHeader("煤矿名称");
		egu.getColumn("meikdwmc").width=100;
		egu.getColumn("meikdwmc").editor=null;
		egu.getColumn("yunsdw").setHeader("运输单位");
		egu.getColumn("yunsdw").width=80;
		egu.getColumn("yunsdw").editor=null;
		egu.getColumn("suocsj").setHeader("锁车时间");
		egu.getColumn("suocsj").width=120;
		egu.getColumn("suocsj").editor=null;
		egu.getColumn("suocry").setHeader("锁车人员");
		egu.getColumn("suocry").width=80;
		egu.getColumn("suocry").editor=null;
		egu.getColumn("suocyy").setHeader("锁车原因");
		egu.getColumn("suocyy").width=120;
		egu.getColumn("suocyy").editor=null;
		egu.getColumn("zt").setHeader("锁车状态");
		
		if(SuocID==2){
			egu.getColumn("jiessj").setHeader("解锁时间");
			egu.getColumn("jiessj").width=120;
			egu.getColumn("jiessj").editor=null;
			egu.getColumn("jiesry").setHeader("解锁人员");
			egu.getColumn("jiesry").width=80;
			egu.getColumn("jiesry").editor=null;
		}
		//设置Grid行数
		egu.addPaging(25);
		//锁车状态
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"已锁车"));
		list.add(new IDropDownBean(2,"已解锁"));
		egu.getColumn("zt").setEditor(new ComboBox());
		
		egu.getColumn("zt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("zt").setReturnId(true);
		
		String sRefreshHandler = 
			"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
			"if (grid_Mrcd.length>0) {" +
			"Ext.MessageBox.confirm('消息提示','刷新界面后您所做的更改将不被保存,是否继续?',function(btn){" +
			"if (btn == 'yes') {" +
			"document.getElementById('RefreshButton').click();" +
			"}" +
			"})" +
			"}else {document.getElementById('RefreshButton').click();}" +
			"}";
		GridButton gRefresh = new GridButton("刷新",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		if(SuocID==1){
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		
		egu.addTbarText("-");
		egu.addTbarText("-");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});");
		
		
		
		
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
			visit.setList1(null);
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			//getSelectData();
		}
		getSelectData();
	}
	
	

	
//	 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select 1 as id,'已锁定车辆'  as mingc from dual union select 2 as id,'已解锁车辆' as mingc from dual";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
}
