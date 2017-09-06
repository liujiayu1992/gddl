package com.zhiren.gangkjy.duicgl.duicfgl;
/*
 * 作者:杨宏杰
 * 时间:2009-3-27
 * 内容:堆存费的维护
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
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Duicfwh extends BasePage implements PageValidateListener {
	
	
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
		String CurDate = DateUtil.FormatOracleDate(getRiqi());
		long diancxxb_id = visit.getDiancxxb_id();
		if(visit.isFencb() && getChangbValue()!=null) {
			diancxxb_id = getChangbValue().getId();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from duicfb where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq=")
		.append(CurDate);
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
		String riqTiaoj=this.getRiqi();
		String chaxun="";
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
//		int jib=this.getDiancTreeJib();
//		if(jib==1){//选集团时刷新出所有的电厂
//			strdiancTreeID="";
//		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
//			strdiancTreeID = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
//		}else if (jib==3){//选电厂只刷新出该电厂
//			strdiancTreeID=this.getTreeid();
//		} 
		
	   chaxun= 


		   "select d.id      as id,\n" +
		   "       d.diancxxb_id  as diancxxb_id,\n" + 
		   "       m.mingc   as meicb_id,\n" + 
		   "       d.riq     as riq,\n" + 
		   "       d.jifksrq as jifksrq,\n" + 
		   "       d.jifjsrq as jifjsrq,\n" + 
		   "       d.duicsl as duicsl,\n" + 
			"       d.jine as jine,\n" + 
		   "       d.beiz    as beiz\n" + 
		   "\n" + 
		   "  from duicf d,  meicb m\n" + 
		   "\n" + 
		 
		   "   where d.meicb_id=m.id\n" + 
		   "    and d.diancxxb_id="+ visit.getDiancxxb_id()+"\n" + 
		   "    and d.riq= to_date('"+this.getRiqi()+"','yyyy-mm-dd')";



//	   System.out.println(chaxun);
	   rsl = con.getResultSetList(chaxun);
	   
			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyWidth-10");
		
		egu.setTableName("duicf");
//		egu.getColumn("id").setHeader("id");	
		egu.getColumn("diancxxb_id").setHeader(Local.diancmc);
		egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancxxb_id()+"");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("meicb_id").setHeader(Local.duow);
		egu.getColumn("riq").setHeader(Local.fufrq);
		egu.getColumn("jifksrq").setHeader(Local.jifksrq);
		egu.getColumn("jifksrq").setDefaultValue(this.getRiqi());
		egu.getColumn("jifjsrq").setHeader(Local.jifjsrq);
		egu.getColumn("jifjsrq").setDefaultValue(this.getRiqi());
		egu.getColumn("duicsl").setHeader(Local.duicsl);
		egu.getColumn("duicsl").setDefaultValue("0");
		egu.getColumn("jine").setHeader(Local.jine);
		egu.getColumn("jine").setDefaultValue("0");
		egu.getColumn("beiz").setHeader(Local.beiz);
		
		
		

		//设定列的初始宽度
		egu.getColumn("id").setWidth(65);
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("meicb_id").setWidth(100);
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("jifksrq").setWidth(100);
		egu.getColumn("jifjsrq").setWidth(100);
		egu.getColumn("duicsl").setWidth(100);
		egu.getColumn("jine").setWidth(100);
		egu.getColumn("beiz").setWidth(120);
		
		
		
		
		
		//设定不可编辑列的颜色
		egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("shangbzt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.addPaging(18);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.setDefaultsortable(false);//设定页面不自动排序
		// *****************************************设置默认值****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		
		
		
		//*************************下拉框*****************************************88
		//电厂下拉框
//		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
//	    egu.getColumn("diancxxb_id").setReturnId(true);
//		
      
		//跺位信息表下拉框
		ComboBox cb=new ComboBox();
		cb.setEditable(true);
		egu.getColumn("meicb_id").setEditor(cb);
		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel("select v.id,v.mingc from vwduow v " +
				                                                                 "where v.diancxxb_id="+visit.getDiancxxb_id()+" and v.mingc<>'直达煤场'order by mingc"));
		egu.getColumn("meicb_id").setReturnId(true);
		

		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
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
			this.setRiqi(null);
			
			
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			
		
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		Visit visit = (Visit) getPage().getVisit();
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
			+ getTreeid();
			
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
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
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
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
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
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
		System.out.println("AAAAAAAAAAAA");
		
		System.out.println(getTree().getWindowTreeScript());
		return getTree().getWindowTreeScript();
	}

	//日期控件
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-0,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
//	public int getDiancTreeJib() {
//		JDBCcon con = new JDBCcon();
//		int jib = -1;
//		String DiancTreeJib = this.getTreeid();
//		//System.out.println("jib:" + DiancTreeJib);
//		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
//			DiancTreeJib = "0";
//		}
//		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
//		ResultSet rs = con.getResultSet(sqlJib.toString());
//		
//		try {
//			while (rs.next()) {
//				jib = rs.getInt("jib");
//			}
//			rs.close();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}finally{
//			
//			con.Close();
//		}
//
//		return jib;
//	}
	
	
}
