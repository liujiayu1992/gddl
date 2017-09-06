package com.zhiren.dc.jilgl.gongl.daoy;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author zhangl
 *
 */

public class Daoybl extends BasePage implements PageValidateListener {
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

	//绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujblQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		//插入车皮临时表
		sb.append("begin\n");
		while (rsl.next()) {
			String diancxxb_id = this.getTreeid();
			sb.append("insert into qicdyb\n");
			sb.append("(id, diancxxb_id, meikxxb_id,yunsdwb_id,yuanmc_id,xiemc_id,cheph, maoz, piz, biaoz,zhongcsj,zhongch,zhongcjjy,qingcsj,qingch,qingcjjy)\n");
			sb.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id);
			sb.append(",0,0,").append((getExtGrid().getColumn("yuanmc_id").combo).getBeanId(rsl.getString("yuanmc_id")));
			sb.append(",").append((getExtGrid().getColumn("xiemc_id").combo).getBeanId(rsl.getString("xiemc_id")));
			sb.append(",'").append(rsl.getString("cheph"));
			sb.append("',").append(rsl.getDouble("maoz"));
			sb.append(",").append(rsl.getDouble("piz"));
			sb.append(",").append(rsl.getDouble("biaoz"));
			sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",'A','").append(visit.getRenymc());
			sb.append("',").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",'A','").append(visit.getRenymc()).append("');\n");
		}
		sb.append("end;");
		int flag = con.getUpdate(sb.toString());
		if(flag == -1){
			con.rollBack();
			setMsg("保存失败");
		}else{
			con.commit();
			setMsg("保存成功");
		}
		con.Close();
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
		StringBuffer sb = new StringBuffer();
//		sb.append("select c.diancxxb_id,c.cheph,c.maoz,c.piz,c.biaoz,c.koud,c.kous,c.kouz,c.sanfsl,c.id,c.chec,'' as gongysb_id,'' as meikxxb_id,'' as faz_id,\n");
		sb.append("select c.cheph,c.maoz,c.piz,c.biaoz,c.id,'' guohsj,\n");
		sb.append(" '' yuanmc_id,'' xiemc_id from qicdyb c where zhongcsj=''");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误sb:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
//		egu.getColumn("cheph").editor
//		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 3); }");
		egu.getColumn("cheph").editor
		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 2); }");
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
//		egu.getColumn("maoz").editor
//		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("maoz").editor
		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 3); }");
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
//		egu.getColumn("piz").editor
//		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 5); }");
		egu.getColumn("piz").editor
		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		StringBuffer lins = new StringBuffer();
		lins
				.append("specialkey:function(own,e){ \n")
				.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
				.append("Ext.MessageBox.alert('提示信息','已到达数据末尾！');return; \n")
				.append("} \n")
				.append("row = row+1; \n")
				.append("last = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
				.append("cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("copylastrec(last,cur); \n")
				.append("gridDiv_grid.startEditing(row , 1); }");
		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		egu.getColumn("yuanmc_id").setHeader("原煤场");
		egu.getColumn("yuanmc_id").setWidth(80);
		egu.getColumn("xiemc_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("xiemc_id").setWidth(80);
		egu.getColumn("guohsj").setHidden(true);
		egu.getColumn("guohsj").editor = null;
		
		
		
		
		egu.addTbarText("过衡时间:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("时:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("HOUR");
		comb1.setId("HOUR");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("分:");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("MIN");
		comb2.setId("MIN");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
		
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
//		原煤场
		ComboBox cmc = new ComboBox();
		egu.getColumn("yuanmc_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = "select id, mingc from meicb";
		egu.getColumn("yuanmc_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
//		卸煤场
		ComboBox cxmc = new ComboBox();
		egu.getColumn("xiemc_id").setEditor(cxmc);
		cmc.setEditable(true);
		egu.getColumn("xiemc_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
		
		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//设置点击添加按钮时触发事件
		egu.addOtherScript("gridDiv_ds.on('add',setDateDefault);\n");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+ "row = irow;}); \n");
		egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(e.field=='MAOZ' || e.field=='PIZ'){biaoz = eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0);if(biaoz>0){e.record.set('BIAOZ',biaoz);}else{Ext.MessageBox.alert('提示信息','毛重的值必须比皮重大');return;}}});\n");
		
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
		.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("if(cks.getDepth() == 3){\n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
		.append("rec.set('JIHKJB_ID', cks.text);\n")
		.append("}else if(cks.getDepth() == 2){\n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.text);\n")
		.append("rec.set('MEIKXXB_ID', cks.text);\n")
		.append("}else if(cks.getDepth() == 1){\n")
		.append("rec.set('GONGYSB_ID', cks.text); }\n")
		.append("gongysTree_window.hide();\n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		
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
	
	public String getTreeScript1() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	//设置小时下拉框
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	//	 设置分钟下拉框
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where leix=1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}

	//电厂树
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
			setRiq(DateUtil.FormatDate(new Date()));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			visit.setDefaultTree(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			getSelectData();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
	}
}