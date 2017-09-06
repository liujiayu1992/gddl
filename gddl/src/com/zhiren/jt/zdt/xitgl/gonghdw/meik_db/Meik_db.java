package com.zhiren.jt.zdt.xitgl.gonghdw.meik_db;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meik_db extends BasePage implements PageValidateListener {
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


	



	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		
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
		
		
		
		String chaxun = "select xx.id,\n"
				+ "       xx.xuh,\n"
				+ "       xx.bianm,\n"
				+ "       xx.mingc,\n"
				+ "       xx.quanc,\n"
				+ "       xx.piny,\n"
				+ "       sf.quanc as shengfb_id,\n"
				+ "       jh.mingc as jihkjb_id,\n"
				+ "       (select cz.quanc\n"
				+ "          from kuangzglb kgl, chezxxb cz\n"
				+ "         where kgl.meikxxb_id = xx.id\n"
				+ "           and cz.id = kgl.chezxxb_id\n"
				+ "           and cz.leib = '车站') as faz,\n"
				+ "       xx.leib,\n"
				+ "       xx.leix,\n"
				+ "       xx.danwdz\n"
				+ "  from gongysmkglb gl, meikxxb xx, shengfb sf, jihkjb jh\n"
				+ " where gl.meikxxb_id = xx.id\n"
				+ "   and xx.jihkjb_id = jh.id\n"
				+ "   and sf.id= xx.shengfb_id\n"
				+ "   and gl.gongysb_id = (select id from gongysb g where g.bianm='"+visit.getString10()+"')\n"
				+ " order by xx.bianm, xx.xuh, xx.mingc";



		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("meikxxb");
   	
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	
	egu.getColumn("xuh").setCenterHeader("排序号");
	egu.getColumn("bianm").setCenterHeader("编码");
	egu.getColumn("mingc").setCenterHeader("简称");
	egu.getColumn("quanc").setCenterHeader("全称");
	egu.getColumn("piny").setCenterHeader("拼音");
	egu.getColumn("shengfb_id").setCenterHeader("省份");
	egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
	egu.getColumn("faz").setCenterHeader("发站");
	egu.getColumn("jihkjb_id").setHidden(true);
	egu.getColumn("leib").setCenterHeader("类别");
	egu.getColumn("leix").setCenterHeader("类型");
	egu.getColumn("danwdz").setCenterHeader("单位地址");

	
	//设定列初始宽度
	egu.getColumn("xuh").setWidth(45);
	egu.getColumn("bianm").setWidth(100);
	egu.getColumn("mingc").setWidth(120);
	egu.getColumn("quanc").setWidth(170);
	egu.getColumn("piny").setWidth(50);
	egu.getColumn("faz").setWidth(80);
	egu.getColumn("shengfb_id").setWidth(80);
	egu.getColumn("leib").setWidth(80);
	egu.getColumn("jihkjb_id").setWidth(80);
	egu.getColumn("leix").setWidth(60);
	egu.getColumn("danwdz").setWidth(100);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Read);//
	egu.addPaging(50);//设置分页
	egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
	

	
	//*************************下拉框*****************************************88
	//设置省份的下拉框
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	String ShengfSql="select id,quanc from shengfb order by mingc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(ShengfSql));
	
	
	//计划口径下拉框
	ComboBox cb_jhkj=new ComboBox();
	egu.getColumn("jihkjb_id").setEditor(cb_jhkj);
	cb_jhkj.setEditable(true);
	egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String cb_pinzStr="select j.id,j.mingc from jihkjb j order by id  ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_pinzStr));
	egu.getColumn("jihkjb_id").setDefaultValue("重点订货");
	
//	类别下拉框
	List leib = new ArrayList();
	leib.add(new IDropDownBean(0, "统配矿"));
	leib.add(new IDropDownBean(1, "地方矿"));
	egu.getColumn("leib").setEditor(new ComboBox());
	egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(leib));
	egu.getColumn("leib").setReturnId(false);
	egu.getColumn("leib").setDefaultValue("统配矿");
	
//	类型下拉框
	List leix = new ArrayList();
	leix.add(new IDropDownBean(0, "煤"));
	leix.add(new IDropDownBean(1, "油"));
	leix.add(new IDropDownBean(2,"气"));
	egu.getColumn("leix").setEditor(new ComboBox());
	egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(leix));
	egu.getColumn("leix").setReturnId(false);
	egu.getColumn("leix").setDefaultValue("煤");
	
	//********************工具栏************************************************
		
	
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		

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
			setTbmsg(null);
		}
		if (cycle.getRequestContext().getParameter("bianm") != null) {
			visit.setString10(String.valueOf(cycle.getRequestContext().getParameter("bianm")));
			
		}
		getSelectData();
		
		
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
	
	
	
	
	
	

}
