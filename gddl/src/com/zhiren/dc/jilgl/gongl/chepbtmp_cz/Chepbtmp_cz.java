package com.zhiren.dc.jilgl.gongl.chepbtmp_cz;

import java.util.ArrayList;
import java.util.Date;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.DateUtil;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王总兵
 * 时间：2009-12-15 16:50:13
 * 描述：太原二电处理上传数据重复
 */
public class Chepbtmp_cz extends BasePage implements PageValidateListener{	
//	界面用户提示
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
//	刷新数据日期绑定
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	
	
	private boolean _DeleteChick=false;
	public void DeleteButton(IRequestCycle cycle){
		_DeleteChick=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_DeleteChick){
				_DeleteChick=false;
				delete();
				getSelectData();
			}
	}

	private void delete(){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改哦！");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		long hengh=this.getHengqValue().getId();
		String henghTiaoj="";
		if(hengh==1){
			henghTiaoj=" and qingchh='C'";
		}else if(hengh==2){
			henghTiaoj=" and qingchh='A'";
		}else {
			henghTiaoj="";
		}
		
		String riqTiaoj = this.getRiq();
		
		String sql=" delete from chepbtmp where daohrq= to_date('"+ riqTiaoj
		+ "','yyyy-mm-dd') "+ Jilcz.filterDcid(visit, "")+" and fahb_id=0 "+henghTiaoj+"    and meikdwmc='"+getChange()+"' and yunsfs='公路'";
		
		//System.out.println(sql);
		int flag=con.getDelete(sql);
		
		con.Close();
		
		if(flag>=0){
			this.setMsg("数据删除成功!");
		}else{
			this.setMsg("数据删除失败!");
		}
	}
	public void Save() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long lngId = 0;
		int flag = 0;
		if(getChange( )==null || "".equals(getChange())) {
			setTbmsg("没有做出任何更改！");
			return;
		}
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			if (rsl.getString("id").equals("0")) {
				lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				String sInsert = "insert into fahhtb(id,qisrq,jiesrq,gongysb_id,diancxxb_id,meikxxb_id,faz_id,hetb_id,pinzb_id) values(" 
					+ lngId + ","
					+ DateUtil.FormatOracleDate(rsl.getString("qisrq")) + ","
					+ DateUtil.FormatOracleDate(rsl.getString("jiesrq")) + ","
					+ getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsl.getString("gongysb_id")) + ","
					+ visit.getDiancxxb_id() + ","
					+ getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsl.getString("meikxxb_id")) + ","
					+ getExtGrid().getColumn("faz_id").combo.getBeanId(rsl.getString("faz_id")) + ","
					+ getExtGrid().getColumn("hetb_id").combo.getBeanId(rsl.getString("hetb_id")) + ","
					+ getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsl.getString("pinzb_id"))
					+	")";
				flag = con.getInsert(sInsert);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sInsert);
					setMsg("保存过程出现错误！保存未成功！");
					con.rollBack();
					con.Close();
					return;
				}
			}else {
				String sUpdate = "update fahhtb set qisrq=" + DateUtil.FormatOracleDate(rsl.getString("qisrq"))
						+ ",jiesrq=" + DateUtil.FormatOracleDate(rsl.getString("jiesrq"))
						+ ",gongysb_id=" + getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsl.getString("gongysb_id"))
						+ ",meikxxb_id=" + getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsl.getString("meikxxb_id"))
						+ ",faz_id=" + getExtGrid().getColumn("faz_id").combo.getBeanId(rsl.getString("faz_id"))
						+ ",hetb_id=" + getExtGrid().getColumn("hetb_id").combo.getBeanId(rsl.getString("hetb_id"))
						+ ",pinzb_id=" + getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsl.getString("pinzb_id"))
						+ " where id=" + rsl.getString("id");
				flag = con.getUpdate(sUpdate);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sUpdate);
					setMsg("保存过程出现错误！保存未成功！");
					con.rollBack();
					con.Close();
					return;
				}
			}
		}
		rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsl.next()) {
			String sDelete = "delete fahhtb where id=" + rsl.getString("id");
			flag = con.getDelete(sDelete);
			if (flag == -1){
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + sDelete);
				setMsg("保存过程出现错误！保存未成功！");
				con.rollBack();
				con.Close();
				return;
			}
		}
		con.commit();
		con.Close();
		setTbmsg("保存成功");
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long hengh=this.getHengqValue().getId();
		String henghTiaoj="";
		if(hengh==1){
			henghTiaoj=" and c.qingchh='C'";
		}else if(hengh==2){
			henghTiaoj=" and c.qingchh='A'";
		}else {
			henghTiaoj="";
		}
		
		String sSql = "select decode(c.meikdwmc,null,'合计',c.meikdwmc) as meikdwmc,decode(max(c.qingchh),'C','新衡','旧衡') as hengh,\n" +
		"count(c.id) as ches,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
		"sum(c.koud) as koud,to_char(max(c.daohrq),'yyyy-mm-dd') as jiezrq\n" + 
		"from chepbtmp c\n" + 
		"where c.daohrq>="+ DateUtil.FormatOracleDate(getRiq()) +"\n" + 
		"and c.daohrq<="+ DateUtil.FormatOracleDate(getRiq()) +"\n" + 
		" "+henghTiaoj+"\n"+
		"and diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
		" and c.yunsfs='公路'\n"+
		"  and c.fahb_id = 0\n"+
		"group by rollup (c.meikdwmc)\n" + 
		"order by c.meikdwmc";

		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		//egu.setTableName("fahhtb");
		egu.getColumn("meikdwmc").setHeader("煤矿单位");
		egu.getColumn("meikdwmc").setWidth(150);
		egu.getColumn("hengh").setHeader("衡号");
		egu.getColumn("hengh").setWidth(60);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(80);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(80);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(80);
		egu.getColumn("koud").setHeader("扣吨");
		egu.getColumn("koud").setWidth(80);
		egu.getColumn("jiezrq").setHeader("到货日期");
		egu.getColumn("jiezrq").setWidth(100);
		
	
		
		egu.addTbarText("日期:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("衡器:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("HENGQ");
		comb3.setId("HENGQ");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(80);
		egu.addToolbarItem(comb3.getScript());
		egu.addTbarText("-");
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
		egu.addTbarText("-");
		egu.addTbarText("-");
		String sDeleteHandler = 
			"function(){"
			+"if(gridDiv_sm.getSelected() == null){"
			+"	 Ext.MessageBox.alert('提示信息','请先单击要删除的煤矿所在行');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDiv_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('提示信息','您确定要删除&nbsp;'+grid_rcd.get('HENGH')+'&nbsp;&nbsp;,'+ grid_rcd.get('MEIKDWMC')+',&nbsp;&nbsp;&nbsp;'+grid_rcd.get('JIEZRQ')+'日来煤吗?',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('MEIKDWMC');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('DeleteButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		GridButton delte = new GridButton("删除",sDeleteHandler);
		delte.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(delte);
		setExtGrid(egu);
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

	
//	 新衡旧衡
	public IDropDownBean getHengqValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getHengqModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHengqValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setHengqModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getHengqModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getHengqModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getHengqModels() {
		List list = new ArrayList();
		
		list.add(new IDropDownBean(1, "新衡"));
		list.add(new IDropDownBean(2, "旧衡"));
		
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(list));
		return;
	}
	
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			init();
		}
	}
	
	private void init() {
		setExtGrid(null);
		setRiq(DateUtil.FormatDate( new Date()));
		
		getSelectData();
	}
}
