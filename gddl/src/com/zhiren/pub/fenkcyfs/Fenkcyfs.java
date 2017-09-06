package com.zhiren.pub.fenkcyfs;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-07-28 17：30
 * 描述：修改采样方式设置的时候不选择分厂厂别，需要对厂别进行处理时选择电厂树
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Fenkcyfs extends BasePage implements PageValidateListener {
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
		}
		getSelectData();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("SELECT   f.ID, m.mingc AS meikxxb_id, j.mingc AS jihkjb_id,\n"
						+ "         y.mingc AS yunsfsb_id, ch.mingc AS chezxxb_id,\n"
						+ "         ca.mingc AS caizhfsb_id, d.id AS diancxxb_id\n"
						+ "    FROM fenkcyfsb f,\n"
						+ "         meikxxb m,\n"
						+ "         yunsfsb y,\n"
						+ "         jihkjb j,\n"
						+ "         chezxxb ch,\n"
						+ "         diancxxb d,\n"
						+ "         caizhfsb ca\n"
						+ "   WHERE f.meikxxb_id = m.ID\n"
						+ "     AND f.yunsfsb_id = y.ID\n"
						+ "     AND f.jihkjb_id = j.ID\n"
						+ "     AND f.chezxxb_id = ch.ID\n"
						+ "     AND f.diancxxb_id = d.ID\n"
						+ "     AND f.caizhfsb_id = ca.ID\n"
						+ "     AND (f.diancxxb_id = "
						+ getTreeid() + " or d.fuid = " + getTreeid()
						+ ")\n" + "ORDER BY f.meikxxb_id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("fenkcyfsb");
		// /设置显示列名称
		egu.getColumn("meikxxb_id").setHeader("煤矿信息");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("chezxxb_id").setHeader("发站");
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("caizhfsb_id").setHeader("采制化方式");
		// //设置列宽度
		//egu.getColumn("jihkjb_id").setWidth(200);
		// //设置当前列是否编辑
		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		// /动态下拉框
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb  order by mingc "));
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb  order by mingc "));
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from jihkjb  order by mingc "));
		egu.getColumn("chezxxb_id").setEditor(new ComboBox());
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb  order by mingc "));
		egu.getColumn("caizhfsb_id").setEditor(new ComboBox());
		egu.getColumn("caizhfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from caizhfsb where diancxxb_id = " + 
						visit.getDiancxxb_id()+" order by mingc "));
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		/*
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		dc.setEditable(true);
		String sql = 
			"select id, mingc from (\n" +
			"select id, mingc, level,CONNECT_BY_ISLEAF leaf\n" + 
			"  from diancxxb\n" + 
			" start with id = "+getTreeid()+"\n" + 
			"connect by fuid = prior id)\n" + 
			"where leaf = 1";
		IDropDownModel dcmd = new IDropDownModel(sql);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dcmd);
		if(dcmd.getOptionCount()>1){
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else{
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			egu.getColumn("diancxxb_id").setDefaultValue(dcmd.getBeanValue(getTreeid()));
		}
		*/
		// /设置下拉框默认值
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		// /设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		GridButton gbp = new GridButton("打印", "function (){"
//				+ MainGlobal.getOpenWinScript("Fenkcyfsreport&lx=rezc") + "}");
//		gbp.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(gbp);
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
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
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
			getSelectData();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
	}
}
