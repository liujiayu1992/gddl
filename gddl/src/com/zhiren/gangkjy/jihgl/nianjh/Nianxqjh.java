package com.zhiren.gangkjy.jihgl.nianjh;
/*
 * 作者:杨宏杰
 * 时间:2009-3-31
 * 内容:年需求计划的维护
 */
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Nianxqjh extends BasePage implements PageValidateListener {
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
//-------------------------------------------------------------  save()  -----------------------------

	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		int flag=visit.getExtGrid1().Save(getChange(), visit);
 		if(flag!=-1){
 			setMsg(ErrorMessage.SaveSuccessMessage);
 		}else{
 			setMsg("保存失败！");
 		}
 	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	//删除的方法
	private void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01" + "-01");
		long diancxxb_id = visit.getDiancxxb_id();
		if(visit.isFencb() && getChangbValue()!=null) {
			diancxxb_id = getChangbValue().getId();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from nianxqjhb where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq=")
		.append(CurrODate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
	}

	public void getSelectData() {
		
//		if(!this.isZuorkc()){
//			this.setMsg("昨日数据没有填写,请先填写昨日数据!");
			returnMsg=true;
//		}else{
//			this.setMsg("");
//		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
//		String riqTiaoj=this.getRiqi();
		String chaxun="";
//		if(riqTiaoj==null||riqTiaoj.equals("")){
//			riqTiaoj=DateUtil.FormatOracleDate(getNianf() + "-"
//					+ "-01" + "-01");
//		}
		
		String CurrODate = DateUtil.FormatOracleDate(getNianfValue()+ "-01" + "-01");
		String riq = getNianfValue()+ "-01" + "-01";
//		int jib=this.getDiancTreeJib();
//		if(jib==1){//选集团时刷新出所有的电厂
//			strdiancTreeID="";
//		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
//			strdiancTreeID = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
//		}else if (jib==3){//选电厂只刷新出该电厂
//			strdiancTreeID=this.getTreeid();
//		} 
		
	   chaxun= 
		   "select n.id as id,\n" +
		   "       n.diancxxb_id as diancxxb_id,\n" + 
		   "       n.riq as riq,\n" + 
		   "       d.mingc as xuqdc_id,\n" + 
		   "       n.qickc as qickc,\n" + 
		   "       n.jihdl as jihdl,\n" + 
		   "       n.jihhyl as jihhyl,\n" + 
		   "       n.qimkc as qimkc,\n" + 
		   "       n.xuql as xuql,\n" + 
		   "       n.beiz as beiz\n" + 
		   "  from nianxqjhb n, vwxuqdw d\n" + 
		  
		   "   where  n.xuqdc_id=d.id\n" + 
		   "    and n.diancxxb_id="+ visit.getDiancxxb_id()+"\n" + 
		   "    and n.riq= " + CurrODate + "\n "+ 
		   "    order by d.mingc,n.riq";


//	   System.out.println(chaxun);
//	   System.out.println(CurrODate);
	   rsl = con.getResultSetList(chaxun);
	   
			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyWidth-10");
		
		egu.setTableName("nianxqjhb");
//		egu.getColumn("id").setHeader("id");	
		egu.getColumn("diancxxb_id").setHeader(Local.diancmc);
		egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancxxb_id()+"");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("riq").setHeader(Local.riq);
		egu.getColumn("riq").setDefaultValue(riq);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setHidden(true);
		
		egu.getColumn("xuqdc_id").setHeader(Local.xuqdw);
		egu.getColumn("qickc").setHeader(Local.qickc);
		egu.getColumn("qickc").setDefaultValue("0");
		egu.getColumn("jihdl").setHeader(Local.jihdl);
		egu.getColumn("jihdl").setDefaultValue("0");
		egu.getColumn("jihhyl").setHeader(Local.jihhyl);
		egu.getColumn("jihhyl").setDefaultValue("0");
		egu.getColumn("qimkc").setHeader(Local.qimkc);
		egu.getColumn("qimkc").setDefaultValue("0");
		egu.getColumn("xuql").setHeader(Local.xuql);
		egu.getColumn("xuql").setDefaultValue("0");
		egu.getColumn("beiz").setHeader(Local.beiz);
		
		
		

		//设定列的初始宽度
		egu.getColumn("id").setWidth(65);
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("xuqdc_id").setWidth(100);
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("qickc").setWidth(100);
		egu.getColumn("jihdl").setWidth(120);
		egu.getColumn("jihhyl").setWidth(120);
		egu.getColumn("qimkc").setWidth(120);
		egu.getColumn("xuql").setWidth(100);
		egu.getColumn("beiz").setWidth(120);
//		((NumberField)egu.getColumn("qickc").editor).setDecimalPrecision(3);
		
		
		
		
		//设定不可编辑列的颜色
		egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("shangbzt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.addPaging(18);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.setDefaultsortable(false);//设定页面不自动排序
		// *****************************************设置默认值****************************
		String Date = DateUtil.FormatOracleDate(getNianfValue()+ "-01" + "-01");
//		egu.getColumn("riq").setDefaultValue(Date);
		
		
		
		//*************************下拉框*****************************************88
		//电厂下拉框
//		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
//	    egu.getColumn("diancxxb_id").setReturnId(true);
	
      
		
		
//		电厂下拉框
		ComboBox xuqdc_id=new ComboBox();
		xuqdc_id.setEditable(true);
		egu.getColumn("xuqdc_id").setEditor(xuqdc_id);
		egu.getColumn("xuqdc_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb where cangkb_id=1 and jib=3  order by mingc"));
	    egu.getColumn("xuqdc_id").setReturnId(true);
		
//		年份信息表下拉框
//		List list = new ArrayList();
//		list.add(new IDropDownBean(1,"启用"));
//		list.add(new IDropDownBean(0,"未启用"));
//		egu.getColumn("zhuangt").setEditor(new ComboBox());
//		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
//		egu.getColumn("zhuangt").setReturnId(true);//绑定了，页面显示汉字，库里显示数字。
		
//		List listNianf = new ArrayList();
//		int i;
//		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
//			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
//		}
//		
//		egu.getColumn("riq").setEditor(new ComboBox());
//		egu.getColumn("riq").setComboEditor(egu.gridId, new IDropDownModel(listNianf));
//		egu.getColumn("riq").setReturnId(true);
//		

		// 工具栏
//		 /设置按钮
		egu.addTbarText("请选择年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		// 电厂树
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
//						.getVisit()).getDiancxxb_id(),getTreeid());
//		setTree(etu);
//		egu.addTbarText("-");// 设置分隔符
//		egu.addTbarText("单位:");
//		egu.addTbarTreeBtn("diancTree");

		
//		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
//		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if(e.record.get('DIANCXXB_ID')=='合计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//电厂列不允许编辑
		sb.append("});");
		
		
//		
//		
//       //设定合计列不保存
//		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='合计') return 'continue';}");
//		 
		egu.addOtherScript(sb.toString());
//		//---------------页面js计算结束--------------------------
		
		egu.addTbarText("-");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButton");
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		 打印按钮
		GridButton gbp = new GridButton("查询", "function (){"
				+ MainGlobal.getOpenWinScript("Nianxqjhreport&lx=nianxqjhreport")
				+ "}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
//      双表头---
		
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
			visit.setList1(null);
//			this.setRiqi(null);
			
			setRiq();
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			this.setNianfValue(null);
			this.getNianfModels();
		
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 得到登陆用户所在电厂或者分公司的名称
//	public String getDiancmc() {
//		Visit visit = (Visit) getPage().getVisit();
//		String diancmc = "";
//		JDBCcon cn = new JDBCcon();
//		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
//			+ getTreeid();
//			
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				diancmc = rs.getString("mingc");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//
//		return diancmc;
//
//	}
//	 得到是否包含运损系统设置参数
//	private String getBaohys() {
//		String baohys = "";
//		JDBCcon cn = new JDBCcon();
//		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
//		String sql= "select zhi from xitxxb where mingc='是否包含运损' and diancxxb_id="
//				+ diancid;
//		ResultSet rs = cn.getResultSet(sql);
//		try {
//			while (rs.next()) {
//				baohys = rs.getString("zhi");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//
//		return baohys;
//
//	}
	
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
//	public String getIDropDownDiancmc(String diancmcId) {
//		if(diancmcId==null||diancmcId.equals("")){
//			diancmcId="1";
//		}
//		String IDropDownDiancmc = "";
//		JDBCcon cn = new JDBCcon();
//		
//		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				IDropDownDiancmc = rs.getString("mingc");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//		return IDropDownDiancmc;
//	}
//	
//	//得到电厂的默认到站
//	public String getDiancDaoz(){
//		String daoz = "";
//		String treeid=this.getTreeid();
//		if(treeid==null||treeid.equals("")){
//			treeid="1";
//		}
//		JDBCcon con = new JDBCcon();
//		try {
//			StringBuffer sql = new StringBuffer();
//			sql.append("select dc.mingc, cz.mingc  as daoz\n");
//			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
//			sql.append(" where dd.diancxxb_id=dc.id\n");
//			sql.append(" and  dd.chezxxb_id=cz.id\n");
//			sql.append("   and dc.id = "+treeid+"");
//
//			ResultSet rs = con.getResultSet(sql.toString());
//			
//			while (rs.next()) {
//				 daoz = rs.getString("daoz");
//			}
//			rs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			con.Close();
//		}
//		
//		return daoz;
//	}
	
	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
//	public String getTreeid() {
//		String treeid=((Visit) getPage().getVisit()).getString2();
//		if(treeid==null||treeid.equals("")){
//			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString2();
//	}
//	public void setTreeid(String treeid) {
//		((Visit) getPage().getVisit()).setString2(treeid);
//	}
//	
//	
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//	public String getTreeScript() {
//				return getTree().getWindowTreeScript();
//	}

//	 -------------------  年份 --------------------------------------------------------------------
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

//	public String getYuef() {
//		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
//				.getString2());
//		if (intYuef < 10) {
//			return "0" + intYuef;
//		} else {
//			return ((Visit) getPage().getVisit()).getString2();
//		}
//	}
//
//	public void setYuef(String value) {
//		((Visit) getPage().getVisit()).setString2(value);
//	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
//		setYuef(getYuefValue().getValue());
	}

	// 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
//			int _yuef = DateUtil.getMonth(new Date());
//			if (_yuef == 1) {
//				_nianf = _nianf - 1;
//			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}
//年份下拉框
	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
}
