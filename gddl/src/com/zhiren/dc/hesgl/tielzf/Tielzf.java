package com.zhiren.dc.hesgl.tielzf;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import bsh.Interpreter;

import com.zhiren.common.DateUtil;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-12-04
 * 描述：增加翻车机数设置
 */
public class Tielzf extends BasePage implements PageValidateListener {
	private static final String CustomSetKey = "Tielzf"; 
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
	public static void updatezongje(JDBCcon con,String strid){
		String sql = "update tielzf set zongje=(select sum(zongje) from tielzfmx where tielzf_id = "+strid+") where id=" + strid;
		con.getUpdate(sql);
	}
	private void countmx(JDBCcon con,String diancxxb_id, String faz_id, String daoz_id, String strid, long ches, long zibcs,long fancjs){
		String sql = "select * from tielzfgl where faz_id = " + faz_id +
		" and daoz_id =" + daoz_id + " and diancxxb_id = " + diancxxb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		while(rsl.next()){
			double dbv = 0.0;
			if(rsl.getString("gongs")!=null || !"".equals(rsl.getString("gongs"))){
				try{
					Interpreter bsh = new Interpreter();
					bsh.set("车数", ches);
					bsh.set("自备车数", zibcs);
					bsh.set("翻车机数", fancjs);
					bsh.eval("double Round(double value, int _bit) {\n" +
							"    double value1;// 拟舍弃数字的第一位等于5，且5前面的数字\n" + 
							"    value1 = Math.floor(value * Math.pow(10, _bit + 1))\n" + 
							"            - Math.floor(value * Math.pow(10, _bit)) * 10.0;\n" + 
							"    if(value1/10 < 0.5){\n" + 
							"                return Math.floor(value * Math.pow(10, _bit)) / Math.pow(10, _bit);\n" + 
							"    } else {\n" + 
							"        return (Math.floor(value * Math.pow(10, _bit)) + 1)\n" + 
							"                       / Math.pow(10, _bit);\n" + 
							"}}");
					bsh.eval("va=" + rsl.getString("gongs"));
					dbv = Double.parseDouble(String.valueOf(bsh.get("va")));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			sql = "insert into tielzfmx(id,tielzf_id,feiymc_item_id,shuik,zongje,beiz) values(" +
			"getnewid(" + diancxxb_id + ")," + strid + "," + rsl.getString("feiymc_item_id") +
			",0," + dbv + ",'')";
			con.getInsert(sql);
		}
		rsl.close();
	}
	private void Save() {
		JDBCcon con = new JDBCcon();
		String sql = "";
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		while(rsl.next()){
			String id = rsl.getString("id");
			sql = "delete from tielzfmx where tielzf_id = " + id;
			con.getDelete(sql);
			sql = "delete from tielzf where id =" + id;
			con.getDelete(sql);
		}
		rsl.close();
		rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			long id = rsl.getLong("id");
			String diancxxb_id = rsl.getString("diancxxb_id");
			String riq = DateUtil.FormatOracleDate(rsl.getString("riq"));
			String faz_id = getExtGrid().getColumn("faz_id").combo.getBeanStrId(rsl.getString("faz_id"));
			String daoz_id = getExtGrid().getColumn("daoz_id").combo.getBeanStrId(rsl.getString("daoz_id"));
			long ches = rsl.getLong("ches");
			long zibcs = rsl.getLong("zibcs");
			long fancjs = rsl.getLong("fancjs");
			double zongje = rsl.getDouble("zongje");
			String beiz = rsl.getString("beiz");
			
			if(id == 0){
				String strid = MainGlobal.getNewID(con, Long.parseLong(diancxxb_id));
				sql = "insert into tielzf(id,riq,diancxxb_id,faz_id,daoz_id,ches,zibcs,fancjs,zongje,beiz) " +
						"values(" + strid + "," + riq +"," + diancxxb_id + "," +
						faz_id + "," + daoz_id + "," + ches + "," + zibcs + "," + fancjs + "," + zongje + 
						",'" + beiz + "')";
				con.getUpdate(sql);
				countmx(con,diancxxb_id,faz_id,daoz_id,strid,ches,zibcs,fancjs);
				updatezongje(con,strid);
			}else{
				sql = "update tielzf set riq = " + riq +
				",faz_id=" + faz_id + ",daoz_id =" + daoz_id +
				",ches =" + ches + ",zibcs = " +zibcs + 
				",fancjs = " + fancjs + ",zongje = " + zongje +
				",beiz = '" + beiz + "' where id=" + id;
				con.getUpdate(sql);
			}
		}
		rsl.close();
		con.Close();
		setMsg("保存成功");
	}
	private void AddCost(IRequestCycle cycle){
		cycle.getRequestContext().getSession().setAttribute("bianm", "DZZF");
		cycle.activate("Zafxmwh");
	}
	private void Relation(IRequestCycle cycle){
		cycle.activate("Tielzfgl");
	}
	private void Modify(IRequestCycle cycle){
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		if(rsl.next()){
			if(rsl.getLong("id") ==0){
				setMsg("请选定一条记录");
				return;
			}
			((Visit) this.getPage().getVisit()).setString1(rsl.getString("id"));
			cycle.activate("Tielzfmx");
		}
		rsl.close();
		
	}
	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _AddCostclick = false;
	public void AddCostButton(IRequestCycle cycle) {
		_AddCostclick = true;
	}
	private boolean _Modifyclick = false;
	public void ModifyButton(IRequestCycle cycle) {
		_Modifyclick = true;
	}
	private boolean _Relationclick = false;
	public void RelationButton(IRequestCycle cycle) {
		_Relationclick = true;
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
		if (_AddCostclick) {
			_AddCostclick = false;
			AddCost(cycle);
		}
		if (_Modifyclick) {
			_Modifyclick = false;
			Modify(cycle);
		}
		if (_Relationclick) {
			_Relationclick = false;
			Relation(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		if(getBRiq()==null || "".equals(getBRiq())){
			setBRiq(DateUtil.FormatDate(new Date()));
		}
		if(getERiq()==null || "".equals(getERiq())){
			setERiq(DateUtil.FormatDate(new Date()));
		}
		String sql = 
			"select t.id,riq,diancxxb_id,f.mingc as faz_id,\n" +
			"d.mingc as daoz_id,ches,zibcs,fancjs,zongje, t.beiz\n" + 
			"from tielzf t, chezxxb f, chezxxb d\n" + 
			"where t.faz_id = f.id and t.daoz_id = d.id\n" + 
			"and t.riq >=" + DateUtil.FormatOracleDate(getBRiq()) + "\n" +
			"and t.riq <=" + DateUtil.FormatOracleDate(getERiq()) + "\n" +
			"and t.diancxxb_id =" + getTreeid() + " order by riq,faz_id,daoz_id";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey );
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(50);
		// egu.setTitle("车站信息");
		egu.setTableName("tielzf");
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("zibcs").setHeader("自备车数");
		egu.getColumn("fancjs").setHeader("翻车机数");
		egu.getColumn("zongje").setHeader("总金额");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("faz_id").setWidth(80);
		egu.getColumn("daoz_id").setWidth(80);
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("zibcs").setWidth(80);
		egu.getColumn("fancjs").setWidth(80);
		egu.getColumn("zongje").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		
		
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
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("riq").setDefaultValue(getERiq());
		sql = "select c.mingc " +
				"from diancdzb d, chezxxb c " +
				"where d.chezxxb_id = c.id and d.leib = '车站' " +
				"and diancxxb_id = " + getTreeid();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu.getColumn("faz_id").setDefaultValue(rsl.getString("mingc"));
		}
		egu.getColumn("zongje").setEditor(null);
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
		String rfb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rfb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarBtn(new GridButton("修改明细", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "ModifyButton"));
		egu.addTbarBtn(new GridButton("添加费用", "function(){document.getElementById('AddCostButton').click();}"));
		egu.addTbarBtn(new GridButton("费用关联", "function(){document.getElementById('RelationButton').click();}"));
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
			if(visit.getActivePageName().toString().equals("Zafxmwh") || 
				visit.getActivePageName().toString().equals("Tielzfgl") ||
				visit.getActivePageName().toString().equals("Tielzfmx") ){
				
			}else{
				visit.setList1(null);
				setTreeid(visit.getDiancxxb_id() + "");
				setBRiq(DateUtil.FormatDate(new Date()));
				setERiq(DateUtil.FormatDate(new Date()));
			}
			visit.setActivePageName(getPageName().toString());
			getSelectData();
		}
	}
}
