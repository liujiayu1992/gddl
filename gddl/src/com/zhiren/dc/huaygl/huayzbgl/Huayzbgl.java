package com.zhiren.dc.huaygl.huayzbgl;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huayzbgl extends BasePage implements PageValidateListener {
	public String testfile;
	//保存comboBox里的选择的数据
	public String getTestfile() {
		return testfile;
	}

	public void setTestfile(String testfile) {
		this.testfile = testfile;
	}
	public void ExtGridUtil() {
	}

	public String OratypeOfExt(String oratype) {
		if ("NUMBER".equals(oratype)) {
			return "float";
		}
		if ("DATE".equals(oratype)) {
			return "date";
		}

		return "string";
	}

//	日期
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {

		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}
	//新添加的日期字段
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString3() == null || ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString3() != null && !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString3(riq2);
		}
	}
	//结束
	protected void initialize() {
		msg = "";
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
		Save1(getChange(), visit);
	}
	
	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		//随机获取id
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
		StringBuffer sql = new StringBuffer("begin\n");
		
		if (!("0".equals(mdrsl.getString("ID")))) {
//			mdrsl.getString("kuangfzlb_id");
			while(mdrsl.next()){
				String id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
				
				sql.append("insert into kuangfzlglb (id,kuangfzlb_id,fahb_id,jiesszbmzb_id) values (")
				.append(id+",").append(this.getExtGrid().getValueSql(getExtGrid().getColumn("kuangfzlb_id"),mdrsl.getString("kuangfzlb_id"))).append(",")
				.append(this.getExtGrid().getValueSql(getExtGrid().getColumn("ID"),mdrsl.getString("ID"))).append(",")
				.append(this.getDisfhyjgValue().getId()).append(");\n");
				
				sql.append("update chepb cp set cp.kuangfzlzb_id = (select zb.id from kuangfzlzb zb where zb.bianm = (select zl.huaybm from kuangfzlb zl where zl.id = ")
					.append(this.getExtGrid().getValueSql(getExtGrid().getColumn("kuangfzlb_id"),mdrsl.getString("kuangfzlb_id"))).append(")) where cp.fahb_id = ")
					.append(this.getExtGrid().getValueSql(getExtGrid().getColumn("ID"),mdrsl.getString("ID"))).append(";\n");
				
			}
		}
		sql.append("end;");
		
		if (!sql.toString().equals("begin\nend;")) {
			con.getUpdate(sql.toString());
		}
		
		mdrsl.close();
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
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		} else if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 日期条件
		String Riq = this.getRiq();
		String Riq2 = this.getRiq2();
		//日期类型
		String riqlx = this.getRiqlxValue().getValue();
		//获得电厂id
		long changb_id = this.getChangbValue().getId();
		//获得供应商的id
		String id = this.getTreeid();
		
		long Diancxxb_id=0;
		String condition1="";
		
		if(!getTreeid().equals("0")){
			
			condition1=" and(g.id = "+getTreeid()+")";
		}
			
		
		
		if(((Visit) getPage().getVisit()).isFencb()){
			
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//根据日期选择
		String riqlx1 = "";
		if(riqlx.equals("到货日期")){
			riqlx1 = "daohrq";
		}else{
			riqlx1 = "fahrq";
		}
		String dianc_id = "";
		if(!id.equals("0")){
			dianc_id =" 	and (g.id='"+id+"' or m.id = '"+id+"')";
		}
		String sql="";

			sql =


				"select distinct f.id,\n" +
				"                g.mingc as gongysb_id,\n" + 
				"                m.mingc as meikxxb_id,\n" + 
				"                j.mingc as jihkjb_id,\n" + 
				"                y.mingc as yunsfsb_id,\n" + 
				"                c.mingc as faz_id,\n" + 
				"                f.fahrq as fahrq,\n" + 
				"                f.daohrq,\n" + 
				"                f.biaoz,\n" + 
				"                f.ches,\n" + 
				"                nvl(gl.huaybm, '未匹配') as kuangfzlb_id\n" + 
				"        from kuangfzlb k,\n" + 
				"       gongysb   g,\n" + 
				"       meikxxb   m,\n" + 
				"       fahb      f,\n" + 
				"       jihkjb    j,\n" + 
				"       yunsfsb   y,\n" + 
				"       chezxxb   c,\n" + 
				"       diancxxb  d," +
				"       jiesszbmzb jz, \n" + 
				"         (select fahb_id, gl.jiesszbmzb_id, kz.huaybm\n" + 
				"        from kuangfzlb kz, kuangfzlglb gl\n" + 
				"       where kz.id = gl.kuangfzlb_id and gl.jiesszbmzb_id= '"+ this.getDisfhyjgValue().getId()+"') gl\n" + 
				" where f.kuangfzlb_id = k.id(+)\n" + 
				"   and f.faz_id = c.id\n" + 
				"   and g.id = f.gongysb_id\n" + 
				"   and f.meikxxb_id = m.id\n" + 
				"   and f.diancxxb_id = "+changb_id+"\n" + 
//				"   and d.mingc = '" +changb+"'\n" + 
				"   and j.id = f.jihkjb_id\n" + 
				"   and y.id = f.yunsfsb_id\n" +
				"   and f.id = gl.fahb_id(+) \n" + dianc_id+
				"   and jiesb_id=0 \n" +
				" and to_date('" + Riq2+"','yyyy-mm-dd')>=f."+riqlx1+" and f."+riqlx1+">=to_date('"+Riq+
				" ','yyyy-mm-dd')and y.id = f.yunsfsb_id order by f."+riqlx1+"";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		//设置多选框
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		// /设置显示列名称
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("faz_id").setHeader("发站");
//		egu.getColumn("faz_id").returnId=true;
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("biaoz").setHeader("矿方供应量");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("kuangfzlb_id").setHeader("化验编号");
		
		//设置化验编码一列为comboBox
		ComboBox luj = new ComboBox();
		luj.setEditable(true);
		egu.getColumn("kuangfzlb_id").setEditor(luj);
		egu.getColumn("kuangfzlb_id").setWidth(300);
		String com = this.getDisfhyjgValue().getValue();

		egu.getColumn("kuangfzlb_id").setComboEditor(egu.gridId,
				new IDropDownModel(
						"select distinct nvl(k.id,0) as id,\n" +
						"k.huaybm||'(化验编号)'||'   '||'Qnet_ar:'||k.qnet_ar||'   '||'Mt:'||k.mt||'   '||'Vdaf:'||k.vdaf\n" + 
						"from kuangfzlb k,jiesszbmzb jz ,jiesszbmb j,jiesszbmglb jg\n" + 
						"where  k.jiesszbmzb_id = jz.id(+) and jz.id = jg.jiesszbmzb_id(+) and jg.jiesszbmb_id = j.id(+) and j.bianm = '"+Locale.addhyjg_jies+"' and jz.zhi ='"+this.getDisfhyjgValue().getValue()+"'"));
		
		egu.getColumn("kuangfzlb_id").returnId=true;
//		设置ExtGridUtil不可被编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addPaging(25);


		// ********************工具栏************************************************
		egu.addTbarText("日期类型:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("RiqlxSelect");
		comb1.setId("RiqlxSelect");
		comb1.setWidth(80);
//		comb1.setListeners("select:function(own,rec,index){Ext.getDom('RiqlxSelect').selectedIndex=index}");
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("RiqlxSelect.on('select',function(){document.forms[0].submit();});");
		

		
		egu.addTbarText(getRiqlxValue().getValue()+":");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至");
		//新添加的日期
		DateField df2 = new DateField();
		df2.setValue(this.getRiq2());
		df2.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df2.setWidth(80);
		egu.addToolbarItem(df2.getScript());
		
		egu.addTbarText("-");
		// 设置树
		egu.addTbarText(Locale.gongysb_id_fahb+":");
		String condition=" and "+riqlx1+">=to_date('"+Riq+"','yyyy-MM-dd') \n " +
						" and "+riqlx1+"<=to_date('"+Riq2+"','yyyy-MM-dd') and jiesb_id=0 ";
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		etu.setWidth(50);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		
//		分厂别
		egu.addTbarText("-");
		egu.addTbarText("单位:");
		ComboBox comb = new ComboBox();
		comb.setTransform("ChangbDropDown");
		comb.setId("Changb");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(80);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");
		
		
		//设置化验机构comboBox
		egu.addTbarText("-");
		egu.addTbarText("化验机构:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("DisfhyjgSelect");
		comb2.setId("DisfhyjgSelect");
		comb2.setWidth(80);
		comb2.setListeners("select:function(own,rec,index){Ext.getDom('DisfhyjgSelect').selectedIndex=index}");
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("DisfhyjgSelect.on('select',function(){document.forms[0].submit();});");
		

//		设置Toolbar按钮	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		egu.addTbarText("-");
		GridButton gb = new GridButton("匹配", GridButton.ButtonType_Save, egu.gridId, egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gb);
		egu.addTbarText("-");
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
			this.setChangbModel(null);
			this.setChangbValue(null);
			this.setDisfhyjgModel(null);
			this.setDisfhyjgValue(null);
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
		((Visit) getPage().getVisit()).setString2("");			//Treeid
		((Visit) getPage().getVisit()).setboolean1(false);		//厂别
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
		
		this.setGongysModel(null);		//IPropertySelectionModel2
		getGongysModels();
		
//		this.setMeikdwModel(null);		//IPropertySelectionModel3
//		getMeikdwModels();
		
		this.setTree(null);
		this.setTreeid("0");
	}
	//	 日期类型
	public IDropDownBean getRiqlxValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean7()==null) {
			if(getRiqlxModel().getOptionCount()>0) {
				setRiqlxValue((IDropDownBean)getRiqlxModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean7();
	}
	public void setRiqlxValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean7(value);
	}
	
	public IPropertySelectionModel getRiqlxModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel7()==null) {
			setRiqlxModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel7();
	}
	public void setRiqlxModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel7(value);
	}
	
	public void setRiqlxModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "到货日期"));
		l.add(new IDropDownBean(2, "发货日期"));
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(l));
 
	}
//	厂别
	public IDropDownBean getChangbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setChangbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getChangbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setChangbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getChangbModels() {
		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
					((Visit) getPage().getVisit()).getDiancxxb_id()+"\n"
					+ " union select id,mingc from diancxxb where id="
					+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";

			((Visit) getPage().getVisit())
			.setProSelectionModel1(new IDropDownModel(sql));
			return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	厂别_End
	
	//供应商Model
	
	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select g.id,g.mingc from gongysb g,gongysdcglb l where 	\n"
				+ " l.gongysb_id=g.id and l.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by g.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	//供应商Model_end


	public String getTreeid() {

		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
			((Visit) getPage().getVisit()).setboolean2(true);
		}
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
	// 第三方化验机构
	public IDropDownBean getDisfhyjgValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getDisfhyjgModel().getOptionCount()>0) {
				setDisfhyjgValue((IDropDownBean)getDisfhyjgModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setDisfhyjgValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getDisfhyjgModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setDisfhyjgModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setDisfhyjgModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setDisfhyjgModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
			
		sql="select jz.id,jz.zhi from jiesszbmzb jz ,jiesszbmb j,jiesszbmglb jg where jz.id = jg.jiesszbmzb_id and j.id = jg.jiesszbmb_id and j.bianm = '"+Locale.addhyjg_jies+"'";
 
		setDisfhyjgModel(new IDropDownModel(sql));
	}
}