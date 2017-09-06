package com.zhiren.dc.hesgl.changnzf;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Changnzf extends BasePage {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private String riq;

	public void setRiq(String value) {
		riq = value;
	}

	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			ResultSetList rs = new JDBCcon()
//					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') time from dual");
					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd') time from dual");
			rs.next();
			riq = rs.getString("time");
		}
		return riq;
	}

	private String riq1;

	public void setRiq1(String value) {
		riq1 = value;
	}

	public String getRiq1() {
		if (riq1 == null) {
			setRiq1(DateUtil.FormatDate(new Date()));

		}
		return riq1;
	}

	private String riq2;

	public void setRiq2(String value) {
		riq2 = value;
	}

	public String getRiq2() {
		if (riq2 == null) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}

	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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

	// 按钮
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ZafxmwhChick = false;
	public void ZafxmwhButton(IRequestCycle cycle) {
		_ZafxmwhChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
		if (_ZafxmwhChick){
			_ZafxmwhChick = false;
			Update(cycle);
		}
	}

	private void save() {

		Visit visit = (Visit) this.getPage().getVisit();		
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		ResultSetList rsl1 = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl1.next()){//删除 
			sql.append("delete from changnzfb where\n");
			sql.append("id=" + rsl1.getString("id"));
			sql.append(";\n");
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "ShujblH.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			if("0".equals(rsl.getString("id"))){//增加
				sql.append("insert into changnzfb(\n");
				sql.append("id, riq, diancxxb_id, gongysb_id, feiyxm_item_id, ");
				sql.append("feiylb_item_id, shoukdw_id, qiszyfw, jiezzyfw, ");
				sql.append("shuil, danj, shul, feiyhj, beiz, zafjsb_id");
				sql.append(")\n");
				
				sql.append("values (");
				sql.append("getnewid("+getTreeid()+")");//id
				sql.append(",").append("to_date('"+rsl.getString("riq")+"','yyyy-mm-dd')" );//riq
				sql.append(",").append(getTreeid());//diancxxb_id
				
				if((getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl
						.getString("gongysb_id")) == -1){
					sql.append(",").append("null");
				}else{
					sql.append(",").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl
							.getString("gongysb_id")));//gongysb_id
				}				
				
				sql.append(",").append((getExtGrid().getColumn("feiyxm_item_id").combo).getBeanId(rsl
										.getString("feiyxm_item_id")));//feiyxm_item_id
				sql.append(",").append((getExtGrid().getColumn("feiylb_item_id").combo).getBeanId(rsl
										.getString("feiylb_item_id")));//feiylb_item_id
				sql.append(",").append((getExtGrid().getColumn("shoukdw_id").combo).getBeanId(rsl
										.getString("shoukdw_id")));//shoukdw_id
				sql.append(",").append(DateUtil.FormatOracleDate(rsl.getString("qiszyfw")));//qixayfw
				sql.append(",").append(DateUtil.FormatOracleDate(rsl.getString("jiezzyfw")));//jiezzyfw
				sql.append(",").append("round(").append(rsl.getDouble("shuil")).append(",4)");//shuil
				sql.append(",").append("round(").append(rsl.getDouble("danj")).append(",4)");//danj
				sql.append(",").append("round(").append(rsl.getDouble("shul")).append(",4)");//shul
			
				sql.append(",").append("round(").append(rsl.getDouble("feiyhj")).append(",4)");//feiyhj
				sql.append(",").append("'").append(rsl.getString("beiz")).append("'");//beiz
				
				sql.append(",").append("0");//zafjsb_id
				sql.append(");\n");
			}else{	//修改
				System.out.println();
				sql.append("update ").append("changnzfb").append(" set ");
				sql.append("riq=").append(DateUtil.FormatOracleDate(rsl.getString("riq")));
				sql.append(",").append("gongysb_id=").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl
											.getString("gongysb_id")));
				sql.append(",").append("feiyxm_item_id=").append((getExtGrid().getColumn("feiyxm_item_id").combo).getBeanId(rsl
											.getString("feiyxm_item_id")));
				sql.append(",").append("feiylb_item_id=").append((getExtGrid().getColumn("feiylb_item_id").combo).getBeanId(rsl
											.getString("feiylb_item_id")));
				sql.append(",").append("shoukdw_id=").append((getExtGrid().getColumn("shoukdw_id").combo).getBeanId(rsl
											.getString("shoukdw_id")));
				sql.append(",").append("qiszyfw=").append(DateUtil.FormatOracleDate(rsl.getString("qiszyfw")));
				sql.append(",").append("jiezzyfw=").append(DateUtil.FormatOracleDate(rsl.getString("jiezzyfw")));  
				sql.append(",").append("shuil=").append("round(").append(rsl.getDouble("shuil")).append(",4)");
				sql.append(",").append("danj=").append("round(").append(rsl.getDouble("danj")).append(",4)");
				sql.append(",").append("shul=").append("round(").append(rsl.getDouble("shul")).append(",4)");
				sql.append(",").append("feiyhj=").append("round(").append(rsl.getDouble("feiyhj")).append(",4)");
				sql.append(",").append("beiz=").append("'").append(rsl.getString("beiz")).append("'");
				sql.append(" where id="+rsl.getString("id"));
				sql.append(";\n");
			}
		}
		sql.append("end;");
		JDBCcon con=new JDBCcon();
		con.getResultSet(sql.toString());
	}

	private void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();

		sql.append("select c.id,c.riq,g.mingc gongysb_id,i.mingc feiyxm_item_id,f.mingc feiylb_item_id,s.mingc shoukdw_id,c.qiszyfw,c.jiezzyfw, ");
		sql.append("c.shuil,c.danj,c.shul,c.feiyhj,c.beiz,decode(c.zafjsb_id,0,'未结算','已结算') zafjsb_id  ");
		sql.append("from  gongysb g, changnzfb c, item i,item f,shoukdw s,diancxxb d ");
		sql.append("where g.id(+)=c.gongysb_id and c.feiyxm_item_id=i.id and d.id =c.diancxxb_id  ");
		sql.append(" and c.feiylb_item_id=f.id and c.shoukdw_id=s.id ");
		sql.append(" and c.riq >= to_date('"+ getRiq1() + "','yyyy-mm-dd')");
		sql.append(" and c.riq <= to_date('"+ getRiq2() + "','yyyy-mm-dd')");
		sql.append(" and d.id = "+ getTreeid());
		if(getGongysSelectValue().getId() != 0){
			sql.append(" and g.id = "+getGongysSelectValue().getId());
		}
		sql.append(" order by c.id asc");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("changnzfb");
		
		egu.getColumn("riq").setHeader("录入日期");
		egu.getColumn("riq").setDefaultValue(getRiq());
		egu.getColumn("riq").setWidth(80);
		
		
		//四个下拉框
		//gongysb_id
		egu.getColumn("gongysb_id").setHeader("供应商");
		String gongysSql = "select id,mingc from gongysb order by mingc asc";
		ComboBox c1 = new ComboBox();
		c1.allowBlank=true;
//		c1.setEditable(true);
		c1.setListWidth(130);
		egu.getColumn("gongysb_id").setEditor(c1);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gongysSql));
		if( 0 != (getGongysSelectValue().getId())){
			egu.getColumn("gongysb_id").setDefaultValue(getGongysSelectValue().getValue());
			egu.getColumn("gongysb_id").setEditor(null);
		}
		egu.getColumn("gongysb_id").setReturnId(true);
		
		//feiyxm_item_id
		egu.getColumn("feiyxm_item_id").setHeader("费用名称");
		String feiyxmSql = "select id,mingc from item where zhuangt=1 and itemsortid=(select itemsortid from itemsort where bianm='ZAF') order by mingc asc";
		ComboBox c2 = new ComboBox();
		c2.setEditable(true);
		egu.getColumn("feiyxm_item_id").setEditor(c2);
		egu.getColumn("feiyxm_item_id").setComboEditor(egu.gridId,
				new IDropDownModel(feiyxmSql));
		egu.getColumn("feiyxm_item_id").setReturnId(true);
		
		//feiylb_item_id
		egu.getColumn("feiylb_item_id").setHeader("费用类别");
		String feiylbSql = "select id,mingc from item where zhuangt=1 and itemsortid=(select itemsortid from itemsort where bianm='ZAFLB') order by mingc asc";
		ComboBox c3 = new ComboBox();
		c3.setEditable(true);
		egu.getColumn("feiylb_item_id").setEditor(c3);
		egu.getColumn("feiylb_item_id").setComboEditor(egu.gridId,
				new IDropDownModel(feiylbSql));
		egu.getColumn("feiylb_item_id").setReturnId(true);
		egu.getColumn("feiylb_item_id").setWidth(80);
		
		//shoukdw_id
		egu.getColumn("shoukdw_id").setHeader("收款单位");
		String showkdwSql = "select id,mingc from shoukdw order by mingc asc";
		ComboBox c4 = new ComboBox();
		c4.setEditable(true);
		c4.setListWidth(130);
		egu.getColumn("shoukdw_id").setEditor(c4);
		egu.getColumn("shoukdw_id").setComboEditor(egu.gridId,
				new IDropDownModel(showkdwSql));
		egu.getColumn("shoukdw_id").setReturnId(true);
		
		egu.getColumn("qiszyfw").setHeader("起始作用范围");
		egu.getColumn("qiszyfw").setDefaultValue(getRiq());
		egu.getColumn("qiszyfw").setWidth(80);
		
		egu.getColumn("jiezzyfw").setHeader("截止作用范围");
		egu.getColumn("jiezzyfw").setDefaultValue(getRiq());
		egu.getColumn("jiezzyfw").setWidth(80);
		
		egu.getColumn("shuil").setHeader("税率");
		((NumberField)egu.getColumn("shuil").editor).setDecimalPrecision(5);
		egu.getColumn("shuil").setWidth(60);
		
		egu.getColumn("danj").setHeader("单价");
		((NumberField)egu.getColumn("danj").editor).setDecimalPrecision(5);
		egu.getColumn("danj").setWidth(60);
		
		egu.getColumn("shul").setHeader("数量");
		((NumberField)egu.getColumn("shul").editor).setDecimalPrecision(5);
		egu.getColumn("shul").setWidth(60);
		
		egu.getColumn("feiyhj").setHeader("费用合计");
		egu.getColumn("feiyhj").setWidth(70);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(150);

//		zafjsb_id
		egu.getColumn("zafjsb_id").setHeader("结算状态");
		egu.getColumn("zafjsb_id").setEditor(null);
		egu.getColumn("zafjsb_id").setWidth(70);
		
		//工具栏
		egu.addTbarText("录入时间:");
		DateField df = new DateField();
		df.setValue(this.getRiq1());
		df.Binding("Riq1", "Form0");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("至");
		DateField df1 = new DateField();
		df1.setValue(this.getRiq2());
		df1.Binding("Riq2", "Form0");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
//		 工具栏设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addPaging(20);//设置一页多少行
		
//		工具栏下拉框
		egu.addTbarText("供应商:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("GongysDropDown");
		comb1.setId("Gongys");
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(100);
		comb1.setListWidth(130);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		String sPowerHandler = "function(){"
//			+"if(gridDiv_sm.getSelected()== null){"
//        	+"	Ext.MessageBox.alert('提示信息','请选中一条记录');"
//        	+"	return;"
//        	+"}"
//        	+"var grid_rcd = gridDiv_sm.getSelected();"
//        	+"if(grid_rcd.get('ID') == '0'){"
//        	+"	Ext.MessageBox.alert('提示信息','在设置杂费项目之前请先保存!');"
//        	+"	return;"
//        	+"}"
//        	+"grid_history = grid_rcd.get('FEIYXM_ITEM_ID');"
//			+"var Cobj = document.getElementById('CHANGE');"
//			+"Cobj.value = grid_history;"
			+"document.getElementById('ZafxmwhButton').click();"
			+"}";
		egu.addTbarBtn(new GridButton("设置杂费项目",sPowerHandler,SysConstant.Btn_Icon_SelSubmit));
		
		StringBuffer script = new StringBuffer();
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("var record = gridDiv_ds.getAt(e.row);\n")
				.append("var danj = eval(record.get('DANJ')||0);\n")
				.append("var shul = eval(record.get('SHUL')||0);\n")
				.append("if(e.field=='DANJ' || e.field=='SHUL'){")
				.append("var feiyhj = danj * shul;\n")
				.append("record.set('FEIYHJ',feiyhj);}\n")
				.append("});");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);//解决宽度问题
		egu.addOtherScript(script.toString());
		setExtGrid(egu);
		
		con.Close();
	}

	//树
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
	
	//供应商
	public IDropDownBean getGongysSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	public void setGongysSelectValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}
	public void setGongysSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
	public IPropertySelectionModel getGongysSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	public void getGongysSelectModels() {
		StringBuffer sql=new StringBuffer();
		sql.append("select 0 id,'全部' mingc from dual union all ");
		sql.append("select * from (select id,mingc from gongysb where leix=1 order by mingc asc)");
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}
	
	//跳转页↓
	private String Changeid;
	public String getChangeid() {
		return Changeid;
	}
	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	
	
	private void Update(IRequestCycle cycle) {	
		cycle.getRequestContext().getSession().setAttribute("bianm","ZAF");//杂费
		cycle.activate("Zafxmwh");//跳转页的资源名
	}
	//跳转页↑
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq1(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			visit.setDefaultTree(null);
			setGongysSelectModel(null);
			getGongysSelectModels();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		init();
	}

	private void init() {
		setOriRiq(getRiq());
		getSelectData();
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
}
