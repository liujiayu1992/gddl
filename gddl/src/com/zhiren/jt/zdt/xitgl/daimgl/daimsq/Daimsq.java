package com.zhiren.jt.zdt.xitgl.daimgl.daimsq;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* 
* 时间：2009-06-26
* 作者： sy
* 修改内容：修改“煤矿单位名称”名字为“煤矿地区全称”，
* 修改“煤矿简称”名字为“煤矿单位简称”，
* 修改“煤矿全称”名字为“煤矿单位全称”，
* 统一名称，不然会引起歧义
*			
*/ 
public class Daimsq extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
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
		visit.getExtGrid1().Save(getChange(), visit);
		setMsg("保存成功！");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		//-----------------------------------
		String str = "";
		long zhuangtID=this.getZhuangtValue().getId();
	
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		
		}

		StringBuffer sb=new StringBuffer();
		sb.append( "select sq.id,dc.mingc as diancxxb_id,sq.shenqrq,sq.gonghdwqc,\n");
		if(zhuangtID==1){
			sb.append( "sq.gonghdwbm,\n");
		}
		sb.append(" sq.meikjc, sq.meikqc,\n");
		if(zhuangtID==1){
			sb.append( "sq.meikbm,\n");
		}
		sb.append("      cz.mingc as faz_id,s.quanc as shengfb_id,cs.mingc as chengsb_id,\n");
		sb.append("       sq.shenqr,decode(sq.kuangb,1,'统配矿',2,'地方矿','') as kuangb,sq.beiz,sq.zhuangt\n");
		sb.append(" from gongysmksqb sq,diancxxb dc ,chezxxb cz,shengfb s,chengsb cs\n");
		sb.append("where sq.diancxxb_id=dc.id\n");
		sb.append("and   sq.faz_id=cz.id(+)\n");
		sb.append("and   sq.shengfb_id=s.id(+)\n" );
		sb.append("and   sq.chengsb_id=cs.id(+) \n ");
		sb.append("and  sq.zhuangt="+zhuangtID+"  "+str+" order by sq.shenqrq desc");
		
 ;

		
		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(sb.toString());
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("gongysmksqb");
   	egu.setWidth("bodyWidth");
	egu.getColumn("id").setHeader("id");
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	egu.getColumn("diancxxb_id").setCenterHeader("电厂名称");
	egu.getColumn("gonghdwqc").setCenterHeader("煤矿地区全称");
	egu.getColumn("meikjc").editor.setAllowBlank(false);
	egu.getColumn("meikjc").setCenterHeader("煤矿单位简称");
	egu.getColumn("meikqc").setCenterHeader("煤矿单位全称");
	egu.getColumn("faz_id").setCenterHeader("发站");
	egu.getColumn("shengfb_id").setCenterHeader("省份");
	egu.getColumn("chengsb_id").setCenterHeader("城市");
	egu.getColumn("kuangb").setCenterHeader("矿别");
	egu.getColumn("shenqr").setCenterHeader("申请人");
	egu.getColumn("shenqr").setHidden(true);
	egu.getColumn("shenqrq").setCenterHeader("申请日期");
	egu.getColumn("shenqrq").setHidden(true);
	egu.getColumn("beiz").setCenterHeader("备注");
	egu.getColumn("zhuangt").setCenterHeader("批复状态");
	egu.getColumn("zhuangt").setEditor(null);
	egu.getColumn("zhuangt").setHidden(true);
	
	
	if(zhuangtID==1){//当状态是已批复,数据不允许修改
		egu.getColumn("gonghdwbm").setCenterHeader("供货单位编码");
		egu.getColumn("gonghdwbm").setEditor(null);
		egu.getColumn("meikbm").setCenterHeader("煤矿编码");
		egu.getColumn("meikbm").setEditor(null);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gonghdwqc").setEditor(null);
		egu.getColumn("meikjc").setEditor(null);
		egu.getColumn("meikqc").setEditor(null);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("chengsb_id").setEditor(null);
		egu.getColumn("kuangb").setEditor(null);
		egu.getColumn("shenqrq").setHidden(false);
		//egu.getColumn("beiz").setEditor(null);
	}
	
	//设定列初始宽度
	
	egu.getColumn("diancxxb_id").setWidth(100);
	egu.getColumn("gonghdwqc").setWidth(120);
	egu.getColumn("meikjc").setWidth(100);
	egu.getColumn("meikqc").setWidth(150);
	egu.getColumn("faz_id").setWidth(80);
	egu.getColumn("shengfb_id").setWidth(80);
	egu.getColumn("chengsb_id").setWidth(80);
	egu.getColumn("kuangb").setWidth(80);
	egu.getColumn("beiz").setWidth(200);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(30);//设置分页
	
	
	
	//*****************************************设置默认值****************************
	//	电厂下拉框
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// 选集团时刷新出所有的电厂
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 3) {// 选电厂只刷新出该电厂
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
	}		
	
	
	//设置日期的默认值,
	
	
	egu.getColumn("shenqr").setDefaultValue(visit.getRenymc());
	egu.getColumn("shenqrq").setDefaultValue(String.valueOf(DateUtil.FormatDate(new Date())));
	egu.getColumn("zhuangt").setDefaultValue("0");
	
	//*************************下拉框*****************************************88
	//设置省份的下拉框
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	//egu.getColumn("shengfb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String ShengfbSql="select id,quanc from shengfb order by quanc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(ShengfbSql));
	//设置城市下拉框
	ComboBox cb_chengs=new ComboBox();
	egu.getColumn("chengsb_id").setEditor(cb_chengs);
	cb_chengs.setEditable(true);
	//egu.getColumn("chengsb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String ChengsbSql="select j.id,j.mingc from chengsb j order by id  ";
	egu.getColumn("chengsb_id").setComboEditor(egu.gridId, new IDropDownModel(ChengsbSql));
	//设置发站下拉框
	ComboBox cb_faz=new ComboBox();
	egu.getColumn("faz_id").setEditor(cb_faz);
	cb_faz.setEditable(true);
	egu.getColumn("faz_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String fazSql="select j.id,j.mingc from chezxxb j order by id  ";
	egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
	
//	设置矿别下拉框
	ComboBox cb_kuangb=new ComboBox();
	egu.getColumn("kuangb").setEditor(cb_kuangb);
	cb_kuangb.setEditable(true);
	//egu.getColumn("kuangb").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String kuangbSql="select 1 as id,'统配矿' as mingc from dual union select 2 as id,'地方矿' as mingc from dual ";
	egu.getColumn("kuangb").setComboEditor(egu.gridId, new IDropDownModel(kuangbSql));
	

	
	//********************工具栏************************************************
		
		//设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");// 设置分隔符
		//批复状态
		egu.addTbarText("批复状态:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("ZHUANGT");
		comb1.setId("ZHUANGT");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setListeners("select:function(){document.Form0.submit();}");
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		//设定工具栏下拉框自动刷新
	
		egu.addTbarText("-");// 设置分隔符
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		if(zhuangtID==0){//当状态是未批复时,显示添,删,保存按钮
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
	
		
		

		
		
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
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
			
			this.setTreeid(null);
			this.setZhuangtValue(null);
			this.getZhuangtModels();
			setTbmsg(null);
		}
		
			getSelectData();
		
		
	}

	

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	
//	矿报表类型
	public boolean _Zhuangtchange = false;
	private IDropDownBean _ZhuangtValue;

	public IDropDownBean getZhuangtValue() {
		if(_ZhuangtValue==null){
			_ZhuangtValue=(IDropDownBean)getZhuangtModels().getOption(0);
		}
		return _ZhuangtValue;
	}

	public void setZhuangtValue(IDropDownBean Value) {
		long id = -2;
		if (_ZhuangtValue != null) {
			id = _ZhuangtValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Zhuangtchange = true;
			} else {
				_Zhuangtchange = false;
			}
		}
		_ZhuangtValue = Value;
	}

	private IPropertySelectionModel _ZhuangtModel;

	public void setZhuangtModel(IPropertySelectionModel value) {
		_ZhuangtModel = value;
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (_ZhuangtModel == null) {
			getZhuangtModels();
		}
		return _ZhuangtModel;
	}

	public IPropertySelectionModel getZhuangtModels() {
		JDBCcon con = new JDBCcon();
		try{
		List ztList = new ArrayList();
		ztList.add(new IDropDownBean(0,"未批复"));
		ztList.add(new IDropDownBean(1,"已批复"));
	
		_ZhuangtModel = new IDropDownModel(ztList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _ZhuangtModel;
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
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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
		return getTree().getWindowTreeScript();
	}
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
}
