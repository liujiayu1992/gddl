package com.zhiren.dc.hesgl.daozzf;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 2009-07-10
 * 类名：Daozzf(到站杂费)
 */

public class Daozzf extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	绑定起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Tianjfymc = false;
	
	public void AddName(IRequestCycle cycle) {
		_Tianjfymc = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick){
			_Refreshclick = false;
		}
		if (_SaveChick){
			_SaveChick = false;
			save();
		}
		if (_Tianjfymc){
			_Tianjfymc = false;
			goAddName(cycle);
		}
	}
	
	private void goAddName(IRequestCycle cycle) {
		cycle.getRequestContext().getSession().setAttribute("bianm", "DZZF");
		cycle.activate("Zafxmwh");
	}
	
	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from daozzfb where id = ").append(delrsl.getString("id")).append("; \n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			long mkid = (getExtGrid().getColumn("mkmc").combo).getBeanId(mdrsl.getString("mkmc"));
			if (mkid == -1) {
				mkid = 0;
			}
			long fzid = (getExtGrid().getColumn("fzmc").combo).getBeanId(mdrsl.getString("fzmc"));
			if (fzid == -1) {
				fzid = 0;
			}
			String ches = mdrsl.getString("ches");
			if (ches.equals("")) {
				ches = "default";
			}
			String zibcs = mdrsl.getString("zibcs");
			if (zibcs.equals("")) {
				zibcs = "default";
			}
			String shuik = mdrsl.getString("shuik");
			if (shuik.equals("")) {
				shuik = "default";
			}
			String zongje = mdrsl.getString("zongje");
			if (zongje.equals("")) {
				zongje = "default";
			}
			if ("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into daozzfb(id, riq, diancxxb_id, meikxxb_id, " +
				"faz_id, ches, zibcs, feiymc_item_id, shuik, zongje, beiz) values(")
				.append("getnewid("+ getTreeid() +"), to_date('"+ mdrsl.getString("riq") +"','yyyy-MM-dd'), ").append(getTreeid())
				.append(", ").append(mkid).append(", ").append(fzid).append(", ").append(ches).append(", ").append(zibcs)
				.append(", ").append((getExtGrid().getColumn("fymc").combo).getBeanId(mdrsl.getString("fymc"))).append(", ")
				.append(shuik).append(", ").append(zongje).append(", '").append(mdrsl.getString("beiz")).append("'); \n");
			} else {
				sbsql.append("update daozzfb set riq = ").append("to_date('").append(mdrsl.getString("riq")).append("', 'yyyy-MM-dd')")
				.append(", meikxxb_id = ").append(mkid).append(", faz_id = ").append(fzid).append(", ches = ").append(ches).append(", zibcs = ")
				.append(zibcs).append(", feiymc_item_id = ").append((getExtGrid().getColumn("fymc").combo).getBeanId(mdrsl.getString("fymc")))
				.append(", shuik = ").append(shuik).append(", zongje = ").append(zongje)
				.append(", beiz = '").append(mdrsl.getString("beiz")).append("' where id = ").append(mdrsl.getString("id")).append("; \n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	private void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select dzzf.id, dzzf.riq, mkxx.mingc mkmc, czxx.mingc fzmc, " +
			"dzzf.ches, dzzf.zibcs, itm.mingc fymc, dzzf.shuik, dzzf.zongje, dzzf.beiz " +
			"from daozzfb dzzf, meikxxb mkxx, chezxxb czxx, item itm " +
			"where dzzf.meikxxb_id = mkxx.id(+) " +
			"and dzzf.faz_id = czxx.id(+) and dzzf.feiymc_item_id = itm.id " +
			"and dzzf.riq >= to_date('"+ getBRiq() +"', 'yyyy-MM-dd') " +
			"and dzzf.riq <= to_date('"+ getERiq() +"', 'yyyy-MM-dd') and dzzf.diancxxb_id = "+ getTreeid() +
			" order by riq, mkmc");
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(DateUtil.Formatdate("yyyy-MM-dd", new Date()));
		
		egu.getColumn("mkmc").setHeader("煤矿名称");
		egu.getColumn("mkmc").setWidth(130);
		ComboBox mkmc = new ComboBox();
		mkmc.setEditable(true);
		egu.getColumn("mkmc").setEditor(mkmc);
		String mkmcsql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("mkmc").setComboEditor(egu.gridId, new IDropDownModel(mkmcsql));
		egu.getColumn("mkmc").setReturnId(true);
		egu.getColumn("mkmc").editor.setAllowBlank(true);
		
		egu.getColumn("fzmc").setHeader("发站名称");
		egu.getColumn("fzmc").setWidth(130);
		ComboBox fzmc = new ComboBox();
		fzmc.setEditable(true);
		egu.getColumn("fzmc").setEditor(fzmc);
		String fzmcsql = "select id, mingc from chezxxb order by mingc";
		egu.getColumn("fzmc").setComboEditor(egu.gridId, new IDropDownModel(fzmcsql));
		egu.getColumn("fzmc").setReturnId(true);
		egu.getColumn("fzmc").editor.setAllowBlank(true);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ches").setDefaultValue("0");
		
		egu.getColumn("zibcs").setHeader("自备车数");
		egu.getColumn("zibcs").setWidth(60);
		egu.getColumn("zibcs").setDefaultValue("0");
		
		egu.getColumn("fymc").setHeader("费用名称");
		egu.getColumn("fymc").setWidth(130);
		ComboBox fymc = new ComboBox();
		fymc.setEditable(true);
		egu.getColumn("fymc").setEditor(fymc);
		String fymcsql = "select id, mingc from item where itemsortid = (select itemsortid " +
			"from itemsort where bianm = 'DZZF') order by mingc";
		egu.getColumn("fymc").setComboEditor(egu.gridId, new IDropDownModel(fymcsql));
		egu.getColumn("fymc").setReturnId(true);
		
		egu.getColumn("shuik").setHeader("税款");
		egu.getColumn("shuik").setDefaultValue("0");
		egu.getColumn("zongje").setHeader("总金额");
		egu.getColumn("zongje").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(200);
		
		egu.addTbarText("选择日期：");
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.Binding("BRiq", "");
		egu.addToolbarItem(bdf.getScript());
		egu.addTbarText("至");
		DateField edf = new DateField();
		edf.setValue(getERiq());
		edf.Binding("ERiq", "");
		egu.addToolbarItem(edf.getScript());
		egu.addTbarText("-"); 
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
//		除了费用名称、总金额和备注之外，其它列都加多行替换功能
		Checkbox cbdhth = new Checkbox();
		cbdhth.setChecked(false);
		cbdhth.setId("duohth");
		egu.addToolbarItem(cbdhth.getScript());
		egu.addTbarText("多行替换");
		egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){			\n" +
			"	if(!duohth.checked){											\n" +
			"		return;														\n" +
			"	}																\n" +
			"	for(i=e.row; i<gridDiv_ds.getCount(); i++){						\n" +
			"		if(e.field=='FYMC'||e.field=='SHUIK'||e.field=='ZONGJE'||e.field=='BEIZ'){	\n" +
			"			return;													\n" +
			"		};															\n" +
			"		gridDiv_ds.getAt(i).set(e.field,e.value);					\n" +
			"	}																\n" +
			"});																\n");
		egu.addTbarText("-");
		
		String rfb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rfb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarBtn(new GridButton("添加费用名称", "function(){document.getElementById('AddName').click();}", 
			SysConstant.Btn_Icon_SelSubmit));
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
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
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}
