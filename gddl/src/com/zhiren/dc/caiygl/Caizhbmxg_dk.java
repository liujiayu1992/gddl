package com.zhiren.dc.caiygl;

import java.util.Date;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2012-09-07
 * 描述：在制样编码界面显示采样编码的状态是否锁定
 * 		 在化验编码界面显示制样编码的状态是否锁定
 * 		回退时应按照化验->制样->采样的顺序回退
 * 		锁定时，如果在化验界面点击锁定时，锁定制样和化验的信息。
 */
/*
 * 作者：夏峥
 * 时间：2012-09-010
 * 描述：使用参数控制是否显示发货信息。
 * 		MainGlobal.getXitxx_item("采样", "采样时是否显示发货信息", "0", "否")
 */

public class Caizhbmxg_dk extends BasePage implements PageValidateListener {
	
	private static String PAR_CAIY = "caiy"; // 采样人样资源

	private static String PAR_ZHIY = "zhiy"; // 制样人员资源

	private static String PAR_HUAY = "huay"; // 制样人员资源
	
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
	
	// 绑定日期
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

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("请先选择一行数据！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
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
			String zhuanmbid = rsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Caizhbm, "zhuanmb", zhuanmbid);
			if (con.getHasIt("select * from zhillsb where id = " + rsl.getString("id") + " and shenhzt = 0")) {
				rs.beforefirst();
				while (rs.next()) {
					id = rs.getString("id");
					dataIndex = "BM" + id;
					colvalue = rsl.getString(dataIndex);
					if (colvalue != null) {
						sql += "update zhuanmb set bianm = '" + colvalue + "'\n" +
							   " where zhillsb_id = " + rsl.getString("id") + "\n" +
							   "   and zhuanmlb_id = " + id + ";\n";
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
		con.Close();
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _SuodChick = false;

	public void SuodButton(IRequestCycle cycle) {
		_SuodChick = true;
	}

	private boolean _JiesChick = false;

	public void JiesButton(IRequestCycle cycle) {
		_JiesChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_SuodChick) {
			_SuodChick = false;
			Suod();
		}
		if (_JiesChick) {
			_JiesChick = false;
			Jies();
		}
	}
	
	private void Suod() {		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "begin\n";
		String jib = "0";
		if (con.getHasIt("select * from zhillsb where id = " + getChange() + " and shenhzt = 0")) {
			if (PAR_ZHIY.equals(visit.getString10())) {
				jib = "2";
			} else if (PAR_HUAY.equals(visit.getString10())) {
				jib = "2,3";
			}
			sql += "update zhuanmb set zhuangt = 1\n" +
				   " where id in (select zhuanmb.id\n" +
				   "			   from zhuanmb, zhuanmlb\n" +
				   "			  where zhuanmb.zhuanmlb_id = zhuanmlb.id\n" +
				   "				and zhuanmb.zhillsb_id = " + getChange() + "\n" +
				   "   				and zhuanmlb.jib in( " + jib + "));\n";
			}
		sql += "end;";
		
		con.getUpdate(sql);
		getSelectData();
		
		con.Close();
	}

	private void Jies() {		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "begin\n";
		int jib = 0;
		if (con.getHasIt("select * from zhillsb where id = " + getChange() + " and shenhzt = 0")) {
			if (PAR_ZHIY.equals(visit.getString10())) {
				jib = 2;
			} else if (PAR_HUAY.equals(visit.getString10())) {
				jib = 3;
			}
			sql += "update zhuanmb set zhuangt = 0\n" +
				   " where id = (select zhuanmb.id\n" +
				   "			   from zhuanmb, zhuanmlb\n" +
				   "			  where zhuanmb.zhuanmlb_id = zhuanmlb.id\n" +
				   "				and zhuanmb.zhillsb_id = " + getChange() + "\n" +
				   "   				and zhuanmlb.jib = " + jib + ");\n";
			}
		sql += "end;";
		
		con.getUpdate(sql);
		getSelectData();
		
		con.Close();
	}
	
	private boolean hasFenc(JDBCcon con) {// 有分厂返回 true
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		ResultSetList rsl = con.getResultSetList(sql);

		if (rsl.next()) {
			return true;
		}

		return false;
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		// yuss 2012-3-28 判断发货信息是否显示煤矿名称
		String getfahxx = "";
		String add = " order by caiyrq";
		ResultSetList rs = con.getResultSetList("select zhi from xitxxb where mingc = '发货信息显示煤矿名称' and zhuangt = 1");
		if (rs.next()) {// 是酒泉
			if (rs.getString("zhi").equals("是")) {
				getfahxx = "getfahxx_jq";
				add = " order by jincbm";
			}
		} else {
			getfahxx = "getfahxx4zl";
		}
		
		if ("是".equals(MainGlobal.getXitxx_item("采样", "采样信息修改发货信息加入运输单位", getTreeid(), "否"))) {
			getfahxx = "getfahxx_MeikANDYunsdw";
		}
		
		String sql = "select * from zhuanmlb order by jib";
		ResultSetList rsl = con.getResultSetList(sql);
		// 查询不同的编号所需的sqltmp
		String sqltmp = "";
		
		// 编码
		String[] columnId = new String[rsl.getRows()];
		while(rsl.next()){
			columnId[rsl.getRow()] = //"BM" + 
				rsl.getString("id");
			sqltmp += ", (select bianm from zhuanmb where zhillsb_id = d.zhilblsb_id and zhuanmlb_id = " + rsl.getString("id") + ") " + rsl.getString("mingc") + "\n";
		}
		
		rsl.beforefirst();
		// 状态
		String[] columnZhuangt = new String[rsl.getRows()];
		while(rsl.next()){
			columnZhuangt[rsl.getRow()] = //"BM" + 
				rsl.getString("id") + rsl.getString("jib");
			sqltmp += ", (select decode(zhuangt, 0, '未锁定', '已锁定') from zhuanmb where zhillsb_id = d.zhilblsb_id and zhuanmlb_id = " + rsl.getString("id") + ") zhuangt" + rsl.getString("jib") + "\n";
		}
		
		rsl.close();
		sql = "select d.zhilblsb_id id,"+getfahxx+"(c.zhilb_id) fahxx,to_char(caiyrq,'yyyy-mm-dd') caiyrq,bianm jincbm,b.mingc bum,d.leib\n";
		sql += sqltmp;
		sql += ",decode(z.shenhzt,0,'未化验','已化验') shenhzt from caiyb c,yangpdhb d,bumb b,zhillsb z\n" ;
		sql += "where c.caiyrq>= " + DateUtil.FormatOracleDate(getBeginRiq()) + "\n";
		sql += "and c.caiyrq <" + DateUtil.FormatOracleDate(getEndRiq()) + "+1\n";
		sql += "and c.id = d.caiyb_id and d.bumb_id = b.id and d.zhilblsb_id = z.id ";
		
		if (this.hasFenc(con)) {
			sqltmp = " and (d.fuid = " + this.getTreeid() + " or d.id = "
					+ this.getTreeid() + ")";
		} else {
			sqltmp = " and d.id = " + this.getTreeid();
		}
		//ww 2009-09-18  添加按厂别过滤数据SQL语句条件避免登录后查看不同厂别的数据
		sql += " AND z.zhilb_id IN (SELECT zhilb_id FROM fahb f, diancxxb d WHERE f.diancxxb_id = d.id " + sqltmp + "\n" +
			   " AND daohrq BETWEEN to_date('" + getBeginRiq() + "','yyyy-mm-dd') AND to_date('" + getEndRiq() + "','yyyy-mm-dd'))" ;
		
		sql += add;
		rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		// 设置grid为可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置为grid数据不分页
		egu.addPaging(0);
		// 设置grid宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// 设置grid列信息
		egu.getColumn("shenhzt").setEditor(null);
		// egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setHeader("化验状态");
		egu.getColumn("shenhzt").setWidth(60);
		egu.getColumn(2).setHeader("发货信息");
		egu.getColumn(2).setEditor(null);
		egu.getColumn(2).setWidth(340);
		if(MainGlobal.getXitxx_item("采样", "采样时是否显示发货信息", "0", "否").equals("否")){
			egu.getColumn(2).setHidden(true);
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
		egu.getColumn(10).setHeader("采样状态");
		egu.getColumn(10).setEditor(null);
		egu.getColumn(11).setHeader("制样状态");
		egu.getColumn(11).setEditor(null);
		egu.getColumn(12).setHeader("化验状态");
		egu.getColumn(12).setEditor(null);
		
		// 编码
		for (int i = 0; i < columnId.length; i++) {
			egu.getColumn(i + 7).setDataindex("BM" + columnId[i]);
		}
		
		if (PAR_CAIY.equals(visit.getString10())) {
			egu.getColumn(8).setHidden(true);
			egu.getColumn(9).setHidden(true);
			egu.getColumn(10).setHidden(true);
//			egu.getColumn(11).setHidden(true);
			egu.getColumn(12).setHidden(true);
		} else if (PAR_ZHIY.equals(visit.getString10())) {
			egu.getColumn(2).setHidden(true);
			egu.getColumn(3).setHidden(true);
			egu.getColumn(4).setHidden(true);
			egu.getColumn(5).setHidden(true);
			egu.getColumn(6).setHidden(true);
			egu.getColumn(7).setEditor(null);
			egu.getColumn(9).setHidden(true);
			egu.getColumn(10).setHidden(true);
//			egu.getColumn(11).setHidden(true);
//			egu.getColumn(12).setHidden(true);
		} else if (PAR_HUAY.equals(visit.getString10())) {
			egu.getColumn(2).setHidden(true);
			egu.getColumn(3).setHidden(true);
			egu.getColumn(4).setHidden(true);
			egu.getColumn(5).setHidden(true);
			egu.getColumn(6).setHidden(true);
			egu.getColumn(7).setHidden(true);
			egu.getColumn(8).setEditor(null);
			egu.getColumn(10).setHidden(true);
			egu.getColumn(11).setHidden(true);
//			egu.getColumn(12).setHidden(true);
		}
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		// 增加grid中Toolbar显示日期参数
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
		
		String zhuangt = "";
		if (PAR_CAIY.equals(visit.getString10())) {
			zhuangt = "||e.record.get('ZHUANGT2')=='已锁定'";
		} else if (PAR_ZHIY.equals(visit.getString10())) {
			zhuangt = "||e.record.get('ZHUANGT3')=='已锁定'";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('SHENHZT')=='已化验'" + zhuangt + "){e.cancel=true;}");// 当"已化验"时,这一行不允许编辑
		sb.append("});");
		egu.addOtherScript(sb.toString());
		
		// 设置grid按钮
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		String condition="";
		if (PAR_ZHIY.equals(visit.getString10())){
			condition=" if(grid1_rcd.get('ZHUANGT3')=='已锁定'){ " +
					  "	Ext.MessageBox.alert(\"提示信息\",\"化验数据已锁定，请先回退化验数据\");  return; } ";
		}
	
		
		if (PAR_ZHIY.equals(visit.getString10()) || PAR_HUAY.equals(visit.getString10())) {
			GridButton suod = new GridButton("锁定", "function (){ " +
					" var grid1_history =\"\";" +
					" if(gridDiv_sm.getSelected()==null){ " +
					"	Ext.MessageBox.alert(\"提示信息\",\"请先选中一条数据\");  return; } " +
					" grid1_rcd = gridDiv_sm.getSelected();" +
					" grid1_history = grid1_rcd.get(\"ID\");" +
					" var Cobj = document.getElementById(\"CHANGE\");" +
					" Cobj.value = grid1_history; " +
					" document.getElementById(\"SuodButton\").click();}");
			suod.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(suod);
			
			GridButton jies = new GridButton("解锁", "function (){ " +
					" var grid1_history =\"\";" +
					" if(gridDiv_sm.getSelected()==null){ " +
					"	Ext.MessageBox.alert(\"提示信息\",\"请先选中一条数据\");  return; } " +
					" grid1_rcd = gridDiv_sm.getSelected();" +
					" grid1_history = grid1_rcd.get(\"ID\");" +
					condition+
					" var Cobj = document.getElementById(\"CHANGE\");" +
					" Cobj.value = grid1_history; " +
					" document.getElementById(\"JiesButton\").click();}");
			jies.setIcon(SysConstant.Btn_Icon_Cancel);
			egu.addTbarBtn(jies);
		}
		
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
		if (getTree() == null) {
			return "";
		} else {
			return getTree().getWindowTreeHtml(this);
		}
	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		} else {
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
			
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
		}
		
		getSelectData();
	} 
}