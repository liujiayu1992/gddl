package com.zhiren.zidy;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zidyfapz_sjy extends BasePage {
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
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
//	新建表格
	public void CreateGrid(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "select p.id,f.z_name zidyfa_id, y.z_title_cn zidyjcsjy_id\n"
				+ " from zidyfapz p,zidyfa f,zidyjcsjy y \n"
				+ "where p.zidyfa_id = f.id and p.zidyjcsjy_id = y.id \n"
				+ "and p.zidyfa_id = " + v.getString1();
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = null;
		if(rsl.getRows() == 0){
			sql = "select 0 id, '' zidyfa_id, '' zidyjcsjy_id from dual";
			egu = new ExtGridUtil("gridDiv",con.getResultSetList(sql));
		}else{
			egu = new ExtGridUtil("gridDiv", rsl);
		}
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		
		egu.getColumn("zidyfa_id").setHeader("方案");
		egu.getColumn("zidyfa_id").setHidden(true);
		egu.getColumn("zidyfa_id").editor = null;
		
		egu.getColumn("zidyjcsjy_id").setHeader("自定义基础数据源");
		egu.getColumn("zidyjcsjy_id").setWidth(300);
		
		ComboBox cb = new ComboBox();
		egu.getColumn("zidyjcsjy_id").setEditor(cb);
		cb.setEditable(true);
		sql = "select id, z_title_cn from zidyjcsjy order by z_title_cn";
		egu.getColumn("zidyjcsjy_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		
		GridButton gbl = new GridButton("上一步","function(){document.getElementById('LastButton').click();}");
		egu.addTbarBtn(gbl);
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		GridButton gbn = new GridButton("下一步","function(){document.getElementById('NextButton').click();}");
//		egu.addTbarBtn(gbn);
		egu.addToolbarButton("下一步",GridButton.ButtonType_SaveAll, "NextButton");
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}

//	表格使用的方法
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

//	按钮的监听事件
	private boolean _NextChick = false;

	public void NextButton(IRequestCycle cycle) {
		_NextChick = true;
	}
	
	private void Next(IRequestCycle cycle){
		Visit v = (Visit) getPage().getVisit();
		String sql = "";
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		while(rs.next()){
			if("0".equals(rs.getString("ID"))){
				sql = "insert into zidyfapz values(getnewid(100),"+v.getString1()+","+
				(getExtGrid().getColumn("zidyjcsjy_id").combo)
				.getBeanId(rs.getString("zidyjcsjy_id"))+")";
			}else{
				sql = "update zidyfapz set zidyjcsjy_id = "+
				(getExtGrid().getColumn("zidyjcsjy_id").combo)
				.getBeanId(rs.getString("zidyjcsjy_id")) + " where id="+rs.getString("ID");
			}
			con.getUpdate(sql);
		}
		rs.close();
		con.Close();
		cycle.activate("Zidyfapz_px");
	}

	private boolean _LastChick = false;

	public void LastButton(IRequestCycle cycle) {
		_LastChick = true;
	}
	
	private void Last(IRequestCycle cycle){
		cycle.activate("Zidyfapz");
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_NextChick) {
			_NextChick = false;
			Next(cycle);
		}
		if (_LastChick) {
			_LastChick = false;
			Last(cycle);
		}
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
			if("".equals(visit.getString1())){
//				方案ID未得到 的错误代码
			}
			CreateGrid();
		}
	}
}