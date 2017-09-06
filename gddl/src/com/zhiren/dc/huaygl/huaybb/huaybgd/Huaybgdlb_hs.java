package com.zhiren.dc.huaygl.huaybb.huaybgd;

import java.util.Date;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huaybgdlb_hs extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
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
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择数据进行打印！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String caiydh="";
		String zhiyh="";
		while(rsl.next()) {
			caiydh+=""+rsl.getString("id")+",";
			zhiyh+="'"+rsl.getString("bianm")+"',";
			
		}
		String aa=caiydh.substring(0,caiydh.length()-1);
		String bb=zhiyh.substring(0,zhiyh.length()-1);
		((Visit)this.getPage().getVisit()).setString11(bb);
		setChepid(aa);
		//System.out.println(aa);
		cycle.activate("Huaybgdlb_hs_mx");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}
	


	private String filterDcid(Visit v){
		
		String sqltmp = " ("+ v.getDiancxxb_id()+")";
		if(v.isFencb()){
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList("select id from diancxxb where fuid="+v.getDiancxxb_id());
			sqltmp = "";
			while(rsl.next()) {
				sqltmp += ","+rsl.getString("id");
			}
			sqltmp ="("+ sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	
	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		String gys="";
		if(getHetdwValue().getValue().equals("-")){
			gys = " ";
		}else{
			gys = " 	and g.id = " + getHetdwValue().getId() + "\n";
		}
		String faz="";
		if(getFazValue().getValue().equals("-")){
			faz = " ";
		}else{
			faz = "	  and cz.id = " + getFazValue().getId() + "\n";
		}
		
		String cheph="";
		
		if(this.getChephValue().getValue().equals("-")){
			cheph = " ";
		}else{
			cheph = "  and f.id in (select c.fahb_id from chepb c where c.cheph='"+getChephValue().getValue()+"')\n";
		}	
				sb.append("select min(zl.id) as id,max(zm.bianm) as bianm,\n");
				sb.append("       max(g.mingc) as gonghdw,\n");
				sb.append("       max(cz.mingc) as faz,\n");
				sb.append("       max(h.hetbh) as heth,\n");
				sb.append("       min(f.fahrq) as fahrq,\n");
				sb.append("       min(f.daohrq) as daohrq,\n");
				sb.append("       min(f.ches) as ches,\n");
				sb.append("       GETHUAYBBCHEPS(max(f.zhilb_id)) as cheh\n");
				sb.append(" from fahb f ,chepb c,gongysb g,meikxxb m,chezxxb cz,hetb h,zhillsb zl,caiyb cy,zhuanmb zm\n");
				sb.append(" where f.id=c.fahb_id\n"); sb.append(" and f.gongysb_id=g.id\n");
				sb.append(" and f.meikxxb_id=m.id\n"); sb.append(" and f.faz_id=cz.id\n");
				sb.append(" and f.hetb_id=h.id(+)\n"); sb.append(" and f.zhilb_id=zl.zhilb_id\n");
				sb.append(" and f.zhilb_id=cy.zhilb_id\n");
				sb.append(" and zm.zhillsb_id=zl.id\n");
				sb.append("  and zl.shifsy=1\n");
				sb.append(" and zm.zhuanmlb_id=(select id from zhuanmlb where jib=2)\n");
				sb.append("  and f.daohrq BETWEEN\n");
				sb.append(DateUtil.FormatOracleDate(getBeginRiq()));
				sb.append("  AND\n");
				sb.append(DateUtil.FormatOracleDate(getEndRiq()));
				sb.append("+1 \n ");
				sb.append(gys);
				sb.append(faz);
				sb.append(cheph);
				sb.append("   group by (f.zhilb_id)\n"); 
				sb.append("   order by max(f.daohrq)\n");
				



		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置每页显示行数
		egu.addPaging(100);
		
		egu.getColumn("id").setHeader("质量临时表id");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("bianm").setHeader("制样单号");
		egu.getColumn("bianm").setWidth(70);
		
		egu.getColumn("gonghdw").setHeader("供货单位");
		egu.getColumn("gonghdw").setWidth(90);
		
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setWidth(70);
	
		egu.getColumn("heth").setHeader("合同号");
		egu.getColumn("heth").setWidth(80);
		
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(80);
		
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(80);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(50);
		
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("cheh").setWidth(500);
		
		
	
		
		
		
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// 与html页中的id绑定,并自动刷新
		df.setId("BeginRq");
		egu.addTbarText("到货日期：");
		egu.addToolbarItem(df.getScript());
		
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("EndRq");
		egu.addTbarText("至");
		egu.addToolbarItem(dfe.getScript());
		
		
		egu.addTbarText("-");
		egu.addTbarText("合同单位:");
		ComboBox comb5 = new ComboBox();
		comb5.setTransform("HetdwDropDown");
		comb5.setId("Heth");
		comb5.setEditable(true);
		comb5.setLazyRender(true);// 动态绑定
		comb5.setWidth(120);
		comb5.setReadOnly(false);
		egu.addToolbarItem(comb5.getScript());
		egu.addTbarText("-");
		
		
		egu.addTbarText("发站:");
		ComboBox comb6 = new ComboBox();
		comb6.setTransform("FazDropDown");
		comb6.setId("faz");
		comb6.setEditable(true);
		comb6.setLazyRender(true);// 动态绑定
		comb6.setWidth(70);
		comb6.setReadOnly(false);
		egu.addToolbarItem(comb6.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("车号:");
		ComboBox comb7 = new ComboBox();
		comb7.setTransform("ChephDropDown");
		comb7.setId("Cheph");
		comb7.setEditable(true);
		comb7.setLazyRender(true);// 动态绑定
		comb7.setWidth(75);
		comb7.setReadOnly(false);
		egu.addToolbarItem(comb7.getScript());
		
		egu.addTbarText("-");
		
		
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("打印",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		
//		egu.addOtherScript(" gridDiv_grid.addListener('cellclick',function(grid, rowIndex, columnIndex, e){});");
		
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
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
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
	
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(!visit.getActivePageName().toString().equals("Qicjjd")){
				setBeginRiq(DateUtil.FormatDate(new Date()));
				setEndRiq(DateUtil.FormatDate(new Date()));
				init();
				String dianclb= cycle.getRequestContext().getParameter("lx");
				if(dianclb!=null){
					visit.setString15(dianclb);
				}else{
					visit.setString15("PRINT_MOR");
				}
			}
			setHetdwValue(null);	//2
			setHetdwModel(null);
			getHetdwModels();		//2
			this.setFazValue(null);
			this.setFazModel(null);
			this.getFazModels();
			this.setChephValue(null);
			this.setChephModel(null);
			this.getFazModels();
		
			getSelectData();
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		getSelectData();
	}
	

//	 合同单位
	public IDropDownBean getHetdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getHetdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setHetdwValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setHetdwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getHetdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getHetdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getHetdwModels() {
		
		String sql=" select  g.id,g.mingc from gongysb g  order by g.mingc\n";

			((Visit) getPage().getVisit())
			.setProSelectionModel2(new IDropDownModel(sql, "-"));
	return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	

//	 发站
	public IDropDownBean getFazValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getFazModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setFazValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setFazModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getFazModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getFazModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getFazModels() {
		
		String sql="select  c.id,c.mingc from chezxxb c  order by c.mingc";
			

			((Visit) getPage().getVisit())
			.setProSelectionModel3(new IDropDownModel(sql,"-"));
	return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	


//	 车皮号
	public IDropDownBean getChephValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getChephModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setChephValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setChephModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getChephModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getChephModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getChephModels() {
		
		String sql="select rownum as id,cheph from chepb cp  order by cp.cheph";
			

			((Visit) getPage().getVisit())
			.setProSelectionModel4(new IDropDownModel(sql,"-"));
	return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
	
}