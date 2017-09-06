package com.zhiren.dc.caiygl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.jilgl.tiel.duih.Guohxx;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-11-29
 * 修改内容:增加电厂树，发货信息增加 首尾车号
 */
/* 修改人：ww
 * 修改时间：2009-09-18
 * 修改内容：添加按厂别过滤数据SQL语句条件避免登录后查看不同厂别的数据
 */
/*
 * 作者：王磊
 * 时间：2009-10-16
 * 描述：解决上一个修改后一厂多制下，总厂不能看见所有厂的采制化编码的问题。
 */

public class Caizhbmxg extends BasePage implements PageValidateListener {
	
	private static String PAR_CAIY = "caiy"; //采样人样资源
	private static String PAR_ZHIY = "zhiy"; //制样人员资源
	private static String PAR_HUAY = "huay"; //制样人员资源
	
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

//	绑定日期
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}
	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	
	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
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
		JDBCcon con = new JDBCcon();
		try {
			if (getChange() == null || "".equals(getChange())) {
				setMsg("请选择一行数据进行查看！");
				return;
			}
			Visit visit = (Visit) this.getPage().getVisit();

			ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
			if (rsl == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult
						+ "Caizhbmxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
				setMsg(ErrorMessage.NullResult);
				return;
			}
			String sql = "select * from zhuanmlb order by jib";
			String id = "";
			String dataIndex = "";
			String colvalue = "";
			ResultSetList rs = con.getResultSetList(sql);
			sql = "begin\n";
			while (rsl.next()) {
				String zhillsb_id = rsl.getString("id");
				MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit.getRenymc() == null ? "Admin" : visit.getRenymc(),
						SysConstant.RizOpType_UP, SysConstant.RizOpMokm_Caizhbm, "zhillsb", zhillsb_id);
				String checksql = "select id from zhuanmb where zhillsb_id=" + zhillsb_id;
				ResultSet crs = con.getResultSet(checksql);
				if (crs.next()) {
					if (con.getHasIt("select * from zhillsb where id =" + rsl.getString("id") + " and shenhzt=0")) {
						rs.beforefirst();
						while (rs.next()) {
							id = rs.getString("id");
							dataIndex = "BM" + id;
							colvalue = rsl.getString(dataIndex);
							if (colvalue != null) {
								sql += "update zhuanmb set bianm ='" + colvalue + "' where zhillsb_id =" + rsl.getString("id") + " and zhuanmlb_id = " + id + "; \n";
							}
						}
					}
				}else {
					if (con.getHasIt("select * from zhillsb where id =" + rsl.getString("id") + " and shenhzt=0")) {
						rs.beforefirst();
						while (rs.next()) {
							id = rs.getString("id");
							dataIndex = "BM" + id;
							colvalue = rsl.getString(dataIndex);
							if (colvalue != null) {
								sql += "insert into zhuanmb ( id,bianm ,zhillsb_id,zhuanmlb_id)values(getnewid("+ visit.getDiancxxb_id()+"),'" + colvalue + "', " + rsl.getString("id") + " , " + id + "); \n";
							}
						}
					}
				}


			}
			sql += "end;";
			if (rsl.getRows() > 0) {
				con.getUpdate(sql);
				getSelectData();
			}
			rs.close();
			rsl.close();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			con.Close();
		}


	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
//			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		getSelectData();
	}
	
	private boolean hasFenc(JDBCcon con){//有分厂返回  true
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			return true;
		}
		
		return false;
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		//yuss 2012-3-28  判断发货信息是否显示煤矿名称
		String getfahxx = "";
		String add=" order by caiyrq";
		ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='发货信息显示煤矿名称' and zhuangt=1");
		 if(rs.next()){//是酒泉
				if(rs.getString("zhi").equals("是")){
					getfahxx="getfahxx_jq";
					add=" order by jincbm";
				}
		 }else{
			 getfahxx = "getfahxx4zl";
		 }
		
		
		if("是".equals(MainGlobal.getXitxx_item("采样", "采样信息修改发货信息加入运输单位", getTreeid(), "否"))){
			getfahxx = "getfahxx_MeikANDYunsdw";
		}
		
		String sql = "select * from zhuanmlb order by jib";
		ResultSetList rsl = con.getResultSetList(sql);
//		查询不同的编号所需的sqltmp
		String sqltmp = "";	
		String[] columnId = new String [rsl.getRows()];
		while(rsl.next()){
			columnId[rsl.getRow()] = //"BM" + 
				rsl.getString("id");
			sqltmp += ",(select bianm from zhuanmb where zhillsb_id = d.zhilblsb_id and zhuanmlb_id = "+rsl.getString("id")+") " + rsl.getString("mingc") + "\n";
		}
		rsl.close();
		sql = "select d.zhilblsb_id id,"+getfahxx+"(c.zhilb_id) fahxx,to_char(caiyrq,'yyyy-mm-dd') caiyrq,bianm jincbm,b.mingc bum,d.leib\n";
		sql += sqltmp;
		sql += ",decode(z.shenhzt,0,'未化验','已化验') shenhzt from caiyb c,yangpdhb d,bumb b,zhillsb z\n" ;
		sql += "where c.caiyrq>= " + DateUtil.FormatOracleDate(getBeginRiq()) + "\n";
		sql += "and c.caiyrq <" + DateUtil.FormatOracleDate(getEndRiq()) + "+1\n";
		sql += "and c.id = d.caiyb_id and d.bumb_id = b.id and d.zhilblsb_id = z.id ";
		
		
		if(this.hasFenc(con)){
			sqltmp = " and (d.fuid = " + this.getTreeid()
			 + " or d.id = " + this.getTreeid() + ")";
		}else{
			sqltmp = " and d.id = " + this.getTreeid();
		}
		//ww 2009-09-18  添加按厂别过滤数据SQL语句条件避免登录后查看不同厂别的数据
		sql += " AND z.zhilb_id IN (SELECT zhilb_id FROM fahb f, diancxxb d WHERE f.diancxxb_id = d.id " + sqltmp + "\n" +
			   " AND daohrq BETWEEN to_date('" + getBeginRiq() + "','yyyy-mm-dd') AND to_date('" + getEndRiq() + "','yyyy-mm-dd'))" ;
		
		sql += add;
		rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		设置grid为可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置为grid数据不分页
		egu.addPaging(0);
//		设置grid宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置grid列信息
		egu.getColumn("shenhzt").setEditor(null);
//		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setHeader("化验状态");
		egu.getColumn("shenhzt").setWidth(60);
		egu.getColumn(2).setHeader("发货信息");
		egu.getColumn(2).setEditor(null);
		egu.getColumn(2).setWidth(340);
		
		if(this.getTreeid().equals("302")){	
			//大同电厂
			//当厂别选择大同二期的时候,并且登陆人员不是管理员和王小林,发货信息进行隐藏
			if(!(visit.getRenymc().equals("王小林")||visit.getRenymc().equals("admin"))){
				egu.getColumn(2).setHidden(true);
			}
		}
		
		egu.getColumn(3).setHeader("采样日期");
		egu.getColumn(3).setEditor(null);
		egu.getColumn(3).setWidth(70);
		egu.getColumn(4).setHeader("进厂批号");
		egu.getColumn(4).setEditor(null);
		egu.getColumn(4).setWidth(70);
		egu.getColumn(5).setHeader("化验部门");
		egu.getColumn(5).setEditor(null);
		egu.getColumn(5).setWidth(60);
		egu.getColumn(6).setHeader("化验类别");
		egu.getColumn(6).setEditor(null);
		egu.getColumn(6).setWidth(60);
		
		
		for(int i = 0; i< columnId.length ; i++){
			egu.getColumn(i+7).setDataindex("BM" + columnId[i]);
		}
		
		if(PAR_CAIY.equals(visit.getString10())){
			egu.getColumn(8).setHidden(true);
			egu.getColumn(9).setHidden(true);
		}else if(PAR_ZHIY.equals(visit.getString10())){
			egu.getColumn(2).setHidden(true);
			egu.getColumn(3).setHidden(true);
			egu.getColumn(4).setHidden(true);
			egu.getColumn(5).setHidden(true);
			egu.getColumn(6).setHidden(true);
			egu.getColumn(9).setHidden(true);
			egu.getColumn(7).setEditor(null);
		}else if(PAR_HUAY.equals(visit.getString10())){
			egu.getColumn(2).setHidden(true);
			egu.getColumn(3).setHidden(true);
			egu.getColumn(4).setHidden(true);
			egu.getColumn(5).setHidden(true);
			egu.getColumn(6).setHidden(true);
			egu.getColumn(7).setHidden(true);
			egu.getColumn(8).setEditor(null);
		}
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		
//		增加grid中Toolbar显示日期参数
		egu.addTbarText("采样时间:");
		DateField dStart = new DateField();
		dStart.Binding("BeginRq","");
		dStart.setValue(getBeginRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText(" 至 ");
		DateField dEnd = new DateField();
		dEnd.Binding("EndRq","");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('SHENHZT')=='已化验'){e.cancel=true;}");//当"已化验"时,这一行不允许编辑
		sb.append("});");
		egu.addOtherScript(sb.toString());
//		设置grid按钮
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		con.Close();
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			this.treeid = treeid;
		}
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
		
		if (cycle.getRequestContext().getParameters("lx") != null) {
			String czhParameter = cycle.getRequestContext().getParameters("lx")[0];
			visit.setString10(czhParameter);
		}
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			
			setExtGrid(null);
			setTreeid("");
			setTree(null);
			
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(!visit.getActivePageName().toString().equals("ChepshQ")){
				setBeginRiq(DateUtil.FormatDate(new Date()));
				setEndRiq(DateUtil.FormatDate(new Date()));
				init();
			}
			
		}
		getSelectData();
	} 
	
	private void init() {
		setExtGrid(null);
		getSelectData();
	}
}