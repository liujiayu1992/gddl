package com.zhiren.shanxdted.kuangfhy;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*作者:王总兵
 *日期:2013-5-8
 *描述:增加数据修改的限制,一般人员只能修改7天只内的数据,7天之外的数据不允许修改,但是特殊人员除外
 * 
 */
public class Kuangfhy extends BasePage implements PageValidateListener {

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

	public void Save() {
		String strchange = getChange();
		JDBCcon con = new JDBCcon();
		String strId="";
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
//				新增id
				strId=MainGlobal.getNewID(con, getChangbValue().getId());
				sql.append("insert into kuangfzlb(id, gongysb_id, meikxxb_id, yunsdwb_id, qnet_ar,qnet_ar_ck,qnet_ar_ht, lury) values(\n" +
							strId + ",\n" +
							"(select max(id) from gongysb where mingc ='" + mdrsl.getString("gongysb_id") + "')" + ",\n" +
							"(select max(id) from meikxxb where mingc ='" + mdrsl.getString("meikxxb_id") + "')" + ",\n" +
							"(select max(id) from yunsdwb where mingc ='" + mdrsl.getString("yunsdwb_id") + "' and diancxxb_id="+ getChangbValue().getId() + "),\n" +
							"round_new(" + mdrsl.getString("qnet_ar") + "*4.1816/1000,4),\n" +
							"round_new(" + mdrsl.getString("qnet_ar_ck") + "*4.1816/1000,4),\n" +
							"round_new(" + mdrsl.getString("qnet_ar_ht") + "*4.1816/1000,4),\n" +
							"'" +mdrsl.getString("lury") + "');\n");
				sql.append("insert into kuangfzlzb(id, qnet_ar,qnet_ar_ck,qnet_ar_ht, leib, lury) values(\n" +
						strId + ",\n" +
						"round_new(" + mdrsl.getString("qnet_ar") + "*4.1816/1000,4),\n" +
						"round_new(" + mdrsl.getString("qnet_ar_ck") + "*4.1816/1000,4),\n" +
						"round_new(" + mdrsl.getString("qnet_ar_ht") + "*4.1816/1000,4),\n" +
						0 + "," +
						"'" +mdrsl.getString("lury") + "');\n");
				sql.append("update fahb set kuangfzlb_id=" + strId + " 	where id=" + mdrsl.getString("fid") + ";\n");
				sql.append("update chepb set kuangfzlzb_id=").append(strId).append(" where fahb_id=").append(mdrsl.getString("fid")).append(";\n");
			} else {
				sql.append("update kuangfzlb set " +
						   "	qnet_ar=round_new(" + mdrsl.getString("qnet_ar") + "*4.1816/1000,4)," +
						   "	qnet_ar_ck=round_new(" + mdrsl.getString("qnet_ar_ck") + "*4.1816/1000,4)," +
						   "	qnet_ar_ht=round_new(" + mdrsl.getString("qnet_ar_ht") + "*4.1816/1000,4)," +
						   "	lury='" +visit.getRenymc() + "'" + 
						   " where id=" + mdrsl.getString("id") + ";\n");
				sql.append("update kuangfzlzb set " +
						   "	qnet_ar=round_new(" + mdrsl.getString("qnet_ar") + "*4.1816/1000,4)," +
						   "	qnet_ar_ck=round_new(" + mdrsl.getString("qnet_ar_ck") + "*4.1816/1000,4)," +
						   "	qnet_ar_ht=round_new(" + mdrsl.getString("qnet_ar_ht") + "*4.1816/1000,4)," +
						   "	lury='" + visit.getRenymc() + "'" + 
						   " where id=" + mdrsl.getString("id") + ";\n");
			}
		}
		mdrsl.close();
		if(sql.length()>0){
			if(con.getUpdate("begin\n" + sql.toString() + "end;") >= 0){
				setMsg("保存成功！");
			}else {
				setMsg("保存失败！");
			}
		}else {
			setMsg("保存失败！");
		}
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
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		
		String where ="";
		if("0" != getTreeid()){
			where = "	and f.gongysb_id=" + getTreeid() + "\n";
		}
		
//		 日期条件
		String Riq = this.getRiq();
		String Riq2 = this.getRiq2();
		String riqTiaoj="";
		if(this.getweizSelectValue().getValue().equals("发货日期")){
			riqTiaoj="	and f.fahrq >= to_date('"+ Riq + "','yyyy-mm-dd')\n" + 
			"   and f.fahrq <= to_date('"+ Riq2 + "','yyyy-mm-dd')\n" ;
		}else{
			riqTiaoj="	and f.daohrq >= to_date('"+ Riq + "','yyyy-mm-dd')\n" + 
			"   and f.daohrq <= to_date('"+ Riq2 + "','yyyy-mm-dd')\n" ;
		}
			
		
		
		
		String sql =
			"select distinct nvl(k.id,0) as id,nvl(f.id,0) as fid,g.mingc as gongysb_id,m.mingc as meikxxb_id,y.mingc as yunsdwb_id,\n" +
			"       f.fahrq,f.daohrq as daohrq, nvl(round_new(k.qnet_ar/4.1816*1000,0),0) as qnet_ar,\n" +
			"nvl(round_new(k.qnet_ar_ck/4.1816*1000,0),0) as qnet_ar_ck,\n" +
			"decode(k.id,null,kuidkk(f.meikxxb_id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),round_new(k.qnet_ar_ht/4.1816*1000,0)) as qnet_ar_ht,\n" +
			" nvl(k.lury,'"+ visit.getRenymc() +"') as lury\n" + 
			"  from kuangfzlb k,gongysb g,meikxxb m,fahb f, yunsdwb y, chepb c\n" + 
			" where f.kuangfzlb_id=k.id(+) and g.id = f.gongysb_id  and  f.meikxxb_id=m.id\n" + 
			" and f.diancxxb_id="+getChangbValue().getId()+"\n" + 
			//"   and f.jiesb_id=0 \n" +
			where + 
			"	"+riqTiaoj+"  " + 
		
			"   and f.id=c.fahb_id\n" + 
			"   and c.yunsdwb_id=y.id(+)\n" + 
			" order by g.mingc,m.mingc,f.daohrq";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("kuangfzlb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		
		if(IsBenYue(con)){
			egu.setGridType(ExtGridUtil.Gridstyle_Read);//设定grid不可以编辑
			this.setMsg("非本月来煤数据只能查看,不允许修改!");
		}
		
		if(isNotEdit(con)){
			//数据超过设定的日期条件,不允许进行编辑,但是特殊用户可以编辑
			if(IsTeShuYongHu(con,visit.getRenymc())){
				//如果是特殊用户,超过系统设置的条件后也能进行编辑
				egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			}else{
				egu.setGridType(ExtGridUtil.Gridstyle_Read);//设定grid不可以编辑
				this.setMsg("页面数据已经锁定,不允许修改,如有修改,请通知系统管理员!");
			}
			
		}else{
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
		}
		
	
		
		
		//设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /设置显示列名称
		egu.getColumn("fid").setHeader("fid");
		egu.getColumn("fid").setWidth(60);
		egu.getColumn("fid").setHidden(true);
		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("gongysb_id").setHidden(true);
		
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(250);
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(200);
		
		egu.getColumn("fahrq").setHeader("发货日期");
		
		egu.getColumn("daohrq").setHeader("到货日期");
		
		egu.getColumn("qnet_ar").setHeader("矿发热值(kcal/kg)");
		egu.getColumn("qnet_ar").setWidth(100);
		((NumberField)egu.getColumn("qnet_ar").editor).setDecimalPrecision(0);
		
		egu.getColumn("qnet_ar_ck").setHeader("出矿热值(kcal/kg)");
		egu.getColumn("qnet_ar_ck").setWidth(100);
		((NumberField)egu.getColumn("qnet_ar_ck").editor).setDecimalPrecision(0);
		
		egu.getColumn("qnet_ar_ht").setHeader("合同热值(kcal/kg)");
		egu.getColumn("qnet_ar_ht").setWidth(100);
		((NumberField)egu.getColumn("qnet_ar_ht").editor).setDecimalPrecision(0);
		
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setWidth(60);
		
		egu.addPaging(0);
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("fid").setEditor(null);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("lury").setEditor(null);

		// ********************工具栏************************************************
		
		egu.addTbarText("-");
		ComboBox comb2=new ComboBox();
		comb2.setId("weiz");
		comb2.setWidth(100);
		comb2.setTransform("weizSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");
		

		egu.addTbarText(":");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至");

		DateField df2 = new DateField();
		df2.setValue(this.getRiq2());
		df2.Binding("RIQ2", "");
		egu.addToolbarItem(df2.getScript());
		
		egu.addTbarText("-");
		// 设置树
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and daohrq>=to_date('"+Riq+"','yyyy-MM-dd') \n " +
						" and daohrq<=to_date('"+Riq2+"','yyyy-MM-dd') and jiesb_id=0 ";
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
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
		comb.setWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
//		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");

//		设置Toolbar按钮	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		setExtGrid(egu);
		con.Close();
	}
	
	
	public boolean isNotEdit(JDBCcon cn){
		//厂里要增加限制,当然日期前7天的数据可以修改,超过7天就不允许再进行修改
		boolean booIsNotEdit=false;
		long tians=0;
		String sql="select to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date('"+this.getRiq()+"','yyyy-mm-dd') as riqicha from dual";
		ResultSetList rsl =cn.getResultSetList(sql);
		if(rsl.next()){
			tians=rsl.getLong("riqicha");
		}
		String xitszrq=MainGlobal.getXitxx_item("调运", "矿方化验维护页面可修改前几天的数据", "0", "7");
		if(tians>Long.parseLong(xitszrq)){
			//当然日期天数超过系统限制的可编辑天数
			booIsNotEdit= true;
		}
		
		return booIsNotEdit;
		
	}
	
	
	public boolean IsBenYue(JDBCcon cn){
		
		String sql="select to_char(sysdate,'yyyy-mm-dd') as riq from dual";
		ResultSetList rsl =cn.getResultSetList(sql);
		String Sysdate="";
		if(rsl.next()){
			Sysdate=rsl.getString("riq");
			Sysdate=Sysdate.substring(0, 7);
		}
		
		if(Sysdate.equals(this.getRiq().substring(0, 7))){
			return false;
			
		}
		String rezcxg=MainGlobal.getXitxx_item("调运", "矿方化验维护页面是否可修改上月数据", "0", "否");
		if(rezcxg.equals("否")){
			return true;
		}else{
			return false;
		}
		
		
	}
	
	

	public boolean IsTeShuYongHu(JDBCcon cn,String username){
		//厂里要增加限制,当然日期前7天的数据可以修改,超过7天就不允许再进行修改
		boolean booIsTeShuYongHu=false;
		
		String sql="select quanc from renyxxb where beiz='矿方化验管理员'";
		ResultSetList rsl =cn.getResultSetList(sql);
		while(rsl.next()){
			if(rsl.getString("quanc").equals(username)){
				booIsTeShuYongHu=true;
				break;
			}
		}
		return booIsTeShuYongHu;
		
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
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
		((Visit) getPage().getVisit()).setString2("");			//Treeid
		((Visit) getPage().getVisit()).setboolean1(false);		//厂别
		((Visit) getPage().getVisit()).setDropDownBean2(null);
		((Visit) getPage().getVisit()).setProSelectionModel2(null);
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
		
		this.setTree(null);
		this.setTreeid("0");
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
		String sql ="select id,mingc from diancxxb d where d.fuid=300 order by id";
		((Visit) getPage().getVisit())
		.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	厂别_End
	
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
	
	
	

    //位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"到货日期"));
        list.add(new IDropDownBean(2,"发货日期"));
      
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
}
