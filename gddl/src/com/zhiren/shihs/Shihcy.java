package com.zhiren.shihs;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihcy extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	//绑定开始日期
	private String startRiq;
	public String getStartRiq() {
		return startRiq;
	}
	public void setStartRiq(String riq) {
		this.startRiq = riq;
	}
	//绑定结束日期
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}
	
	// 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private void Save(){
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,修改记录为空！");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		String delMsg = "";
		String sql = "";
		while(rsl.next()){
			if("已化验".equalsIgnoreCase(rsl.getString("shihzlb_id"))){
				delMsg += "采样编号："+ rsl.getString("bianm") + " 已化验，请通知化验部门取消化验重新删除。";
			}else{
				sql = "delete from shihcyb where id =" + rsl.getString("id");
				con.getDelete(sql);
			}
		}
		rsl.close();
		rsl = getExtGrid().getModifyResultSet(getChange());
		sql = "begin\n";
		while(rsl.next()){
			int id = rsl.getInt("id");
			String caiyrq = DateUtil.FormatOracleDate(rsl.getString("caiysj"));
			String bianm = rsl.getString("bianm");
			String caiyfs = rsl.getString("caiyfs");
			String caiyy = rsl.getString("caiyy");
			String lurry = rsl.getString("lurry");
			String beiz = rsl.getString("beiz");
			if(id == 0){
				if("AUTO".equalsIgnoreCase(bianm)){
					bianm = "getShihcybm("+caiyrq+",nvl(max(xuh),0)+1)";
				}else{
					bianm = "'" + bianm + "'";
				}
				sql += "insert into shihcyb(id,xuh,autobianm,bianm,caiysj,caiyfs,caiyy,lurry,lursj,beiz) (select "
					+"getnewid("+v.getDiancxxb_id() + "),nvl(max(xuh),0)+1,getShihcybm("+caiyrq+",nvl(max(xuh),0)+1),"
					+bianm+"," + caiyrq + ",'"+caiyfs+ "','" + caiyy + "','"+lurry+"',sysdate,'"+beiz+"' from shihcyb "
					+" where caiysj = "+caiyrq + "); \n";
			}else{
				sql += "update shihcyb set caiysj = "+caiyrq +",bianm='"
				+bianm + "',caiyfs = '" + caiyfs + "',caiyy = '"+caiyy
				+"',beiz = '"+beiz+"' where id=" + id + ";\n";
			}
		}
		String savMsg = "";
		if(rsl.getRows()>0){
			sql += "end;\n";
			con.getUpdate(sql);
			savMsg = "保存操作成功!";
		}
		setMsg(delMsg + savMsg);
		rsl.close();
		con.Close();
	}

	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
	private void CreateEGU(){
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select count(id) cs from shihcyb ") 
		.append("where caiysj >= ").append(DateUtil.FormatOracleDate(getStartRiq()))
		.append("\n and caiysj < ").append(DateUtil.FormatOracleDate(getEndRiq()))
		.append("+1 \n");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		String rownum = "";
		if(rsl.next())
			if(rsl.getInt("cs") > 150){
				rownum = " where rownum <= 150";
				setMsg("您查询的记录数过多，会造成程序运行速度缓慢，<br>系统已经自动为您截取了前150条记录。");
			}
		rsl.close();
		sb.delete(0, sb.length());
		sb.append("select * from (")
		.append("select decode(shihzlb_id,0,'未化验','已化验') shihzlb_id,id,bianm,caiysj,caiyfs,caiyy,lurry,beiz \n")
		.append("       from shihcyb \n")
		.append("where caiysj >= ").append(DateUtil.FormatOracleDate(getStartRiq()))
		.append("\n and caiysj < ").append(DateUtil.FormatOracleDate(getEndRiq()))
		.append("+1 order by xuh\n").append(") ").append(rownum);
		rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
//		egu.setHeight("bodyHeight");
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("bianm").setDefaultValue("AUTO");
		egu.getColumn("caiysj").setHeader("采样日期");
		egu.getColumn("caiysj").setWidth(80);
		egu.getColumn("caiysj").setDefaultValue(getEndRiq());
		egu.getColumn("caiyfs").setHeader("采样方式");
		egu.getColumn("caiyfs").setWidth(80);
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "人工"));
		l.add(new IDropDownBean(1, "机械"));
		ComboBox cc = new ComboBox();
		egu.getColumn("caiyfs").setEditor(cc);
		cc.setEditable(true);
		egu.getColumn("caiyfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("caiyfs").returnId=false;
		egu.getColumn("caiyfs").setDefaultValue("机械");
		
		egu.getColumn("caiyy").setHeader("采样员");
		egu.getColumn("caiyy").setWidth(150);
		egu.getColumn("lurry").setHeader("录入人员");
		egu.getColumn("lurry").setWidth(80);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setDefaultValue(v.getRenymc());
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(150);
		egu.getColumn("shihzlb_id").setHeader("状态");
		egu.getColumn("shihzlb_id").setEditor(null);
		egu.getColumn("shihzlb_id").setDefaultValue("未化验");
		
		egu.addTbarText("日期:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ","");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("至");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ","");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
	}

	public void getSelectData() {
		CreateEGU();
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
			init();
		}
	}
	private void init() {
		setStartRiq(DateUtil.FormatDate(new Date()));
		setEndRiq(DateUtil.FormatDate(new Date()));
		getSelectData();
	}
}
