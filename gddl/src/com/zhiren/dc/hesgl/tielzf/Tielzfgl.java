package com.zhiren.dc.hesgl.tielzf;

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

public class Tielzfgl extends BasePage implements PageValidateListener {
	private static final String CustomSetKey = "Tielzfgl"; 
	private boolean showReturnButton = false;
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
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	设置电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
//	设置电厂树_结束

	private void Save() {
		getExtGrid().Save(getChange(), ((Visit) this.getPage().getVisit()));
		setMsg("保存成功");
	}
	private void Return(IRequestCycle cycle){
		cycle.activate("Tielzf");
	}
	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _Returnclick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_Returnclick = true;
	}
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		if (_Returnclick) {
			_Returnclick = false;
			Return(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = 
			"select t.id,t.diancxxb_id,f.mingc as faz_id,\n" +
			"d.mingc as daoz_id, i.mingc as feiymc_item_id, t.gongs\n" + 
			"from tielzfgl t, chezxxb f, chezxxb d, item i\n" + 
			"where t.faz_id = f.id and t.daoz_id = d.id\n" + 
			"and t.feiymc_item_id = i.id and t.diancxxb_id =" + getTreeid();

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey );
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(50);
		egu.setTableName("tielzfgl");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("feiymc_item_id").setHeader("费用项目");
		egu.getColumn("gongs").setHeader("公式");
		egu.getColumn("faz_id").setWidth(80);
		egu.getColumn("daoz_id").setWidth(80);
		egu.getColumn("feiymc_item_id").setWidth(150);
		egu.getColumn("gongs").setWidth(260);
		
		ComboBox fzmc = new ComboBox();
		fzmc.setEditable(true);
		egu.getColumn("faz_id").setEditor(fzmc);
		String fzmcsql = "select id, mingc from chezxxb order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fzmcsql));
		egu.getColumn("faz_id").setReturnId(true);
		egu.getColumn("faz_id").editor.setAllowBlank(true);
		
		ComboBox dzmc = new ComboBox();
		dzmc.setEditable(true);
		egu.getColumn("daoz_id").setEditor(dzmc);
		egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(fzmcsql));
		egu.getColumn("daoz_id").setReturnId(true);
		egu.getColumn("daoz_id").editor.setAllowBlank(true);
		
		ComboBox fymc = new ComboBox();
		fymc.setEditable(true);
		egu.getColumn("feiymc_item_id").setEditor(fymc);
		fzmcsql = "select id, mingc from item where itemsortid = (select itemsortid " +
		"from itemsort where bianm = 'DZZF') order by mingc";
		egu.getColumn("feiymc_item_id").setComboEditor(egu.gridId, new IDropDownModel(fzmcsql));
		egu.getColumn("feiymc_item_id").setReturnId(true);
		egu.getColumn("feiymc_item_id").editor.setAllowBlank(true);
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());

		sql = "select c.mingc " +
				"from diancdzb d, chezxxb c " +
				"where d.chezxxb_id = c.id and d.leib = '车站' " +
				"and diancxxb_id = " + getTreeid();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu.getColumn("faz_id").setDefaultValue(rsl.getString("mingc"));
		}
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		String rfb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rfb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		if(showReturnButton){
			egu.addTbarBtn(new GridButton("返回", "function(){document.getElementById('ReturnButton').click();}",SysConstant.Btn_Icon_Return));
		}
		setExtGrid(egu);
		rsl.close();
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
			if(visit.getActivePageName().toString().equals("Tielzf")){
				showReturnButton = true;
			}
			visit.setList1(null);
			setTreeid(visit.getDiancxxb_id() + "");
			visit.setActivePageName(getPageName().toString());
			getSelectData();
		}
	}
}
