package com.zhiren.jt.zdt.rulgl.rulmzllr;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.Field;
import com.zhiren.dc.rulgl.meihyb.Meihybext;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * sy
 * 2009-05-23
 * 增加一列,关联入炉煤数量的"发电供热耗用"
 */
public class Rulmzllr extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		//Meihybext.UpdateRulzlID(getRiqi(),visit.getDiancxxb_id());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
			
		}
		
		String chaxun = "select r.id,r.diancxxb_id,r.rulrq,r.fenxrq,r.rulbzb_id,rb.mingc as rulbzbmc,r.jizfzb_id,j.mingc as jizfzbmc,r.meil,\n"
				+ "       r.qnet_ar,r.vdaf,r.mt,r.std,r.ad,r.mad,r.aar,r.stad,r.aad,r.qbad,r.had,r.vad,r.fcad,\n"
				+ "       r.qgrad,r.hdaf,r.sdaf,r.var,r.huayy,r.beiz,r.lury,r.lursj,r.shenhzt\n"
				+ "  from rulmzlb r, diancxxb d, rulbzb rb, jizfzb j\n"
				+ " where r.diancxxb_id = d.id(+)\n"
				+ "   and r.rulbzb_id = rb.id(+)\n"
				+ "   and r.jizfzb_id = j.id(+)\n"
				+ "   and r.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd')\n" 
				+ "   and d.id="+visit.getDiancxxb_id()+"";

		;
	//System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("rulmzlb");
   	egu.setWidth("bodyWidth");
   	egu.getColumn("diancxxb_id").setHeader("单位");
	egu.getColumn("rulrq").setHeader("入炉日期");
	egu.getColumn("rulrq").setEditor(null);
	egu.getColumn("fenxrq").setHeader("分析日期");
	egu.getColumn("fenxrq").setEditor(null);
	egu.getColumn("diancxxb_id").setHidden(true);
	egu.getColumn("diancxxb_id").setEditor(null);
	egu.getColumn("rulbzb_id").setHeader("入炉班组ID");
	egu.getColumn("rulbzb_id").setHidden(true);
	egu.getColumn("rulbzb_id").setEditor(null);
	egu.getColumn("rulbzbmc").setHeader("入炉班组");
	egu.getColumn("rulbzbmc").setEditor(null);
	egu.getColumn("rulbzbmc").setUpdate(false);
	egu.getColumn("jizfzb_id").setHeader("入炉机组ID");
	egu.getColumn("jizfzb_id").setHidden(true);
	egu.getColumn("jizfzb_id").setEditor(null);
	egu.getColumn("jizfzbmc").setHeader("入炉机组");
	egu.getColumn("jizfzbmc").setEditor(null);
	egu.getColumn("jizfzbmc").setUpdate(false);
	egu.getColumn("meil").setHeader("发电供热<br>耗用(t)");
	egu.getColumn("meil").setEditor(null);
	egu.getColumn("meil").setUpdate(false);
	
	
	egu.getColumn("qnet_ar").setHeader("低位热<br>Qnet,ar(Mj/kg)");
	egu.getColumn("qnet_ar").setDefaultValue("0");
	egu.getColumn("qnet_ar").editor.setMinValue("5");
	egu.getColumn("qnet_ar").editor.setMaxValue("29.271");
	egu.getColumn("vdaf").setHeader("挥发分<br>Vdaf(%)");
	egu.getColumn("vdaf").setDefaultValue("0");
	egu.getColumn("mt").setHeader("全水<br>Mt(%)");
	egu.getColumn("mt").setDefaultValue("0");
	egu.getColumn("std").setHeader("干基硫<br>St,d(%)");
	egu.getColumn("std").setDefaultValue("0");
	egu.getColumn("ad").setHeader("干基灰<br>Ad(%)");
	egu.getColumn("ad").setDefaultValue("0");
	egu.getColumn("mad").setHeader("空干基水<br>Mad(%)");
	egu.getColumn("mad").setDefaultValue("0");
	
	egu.getColumn("aar").setHeader("收到基<br>灰分Aar(%)");
	egu.getColumn("aar").setHidden(true);
	egu.getColumn("stad").setHeader("空干基硫<br>St,ad(%)");
	egu.getColumn("stad").setHidden(true);
	egu.getColumn("aad").setHeader("空干基灰<br>Aad(%)");
	egu.getColumn("aad").setHidden(true);
	egu.getColumn("qbad").setHeader("弹筒热<br>Qb,ad(Mj/kg)");
	egu.getColumn("qbad").setHidden(true);
	egu.getColumn("had").setHeader("空干基氢<br>Had(%)");
	egu.getColumn("had").setHidden(true);
	egu.getColumn("vad").setHeader("空干基<br>挥发分Vad(%)");
	egu.getColumn("vad").setHidden(true);
	egu.getColumn("fcad").setHeader("固定碳<br>Fcad(%)");
	egu.getColumn("fcad").setHidden(true);
	egu.getColumn("qgrad").setHeader("空干基<br>高位热Qgr,ad(Mj/kg)");
	egu.getColumn("qgrad").setHidden(true);
	egu.getColumn("hdaf").setHeader("干燥无灰<br>基氢Hdaf(%)");
	egu.getColumn("hdaf").setHidden(true);
	egu.getColumn("sdaf").setHeader("干燥无灰<br>基硫Sdaf(%)");
	egu.getColumn("sdaf").setHidden(true);
	egu.getColumn("var").setHeader("收到基<br>挥发份Var(%)");
	egu.getColumn("var").setHidden(true);
	egu.getColumn("huayy").setHeader("化验员");
	egu.getColumn("beiz").setHeader("备注");
	egu.getColumn("lury").setHeader("录入员");
	egu.getColumn("lury").setHidden(true);
	egu.getColumn("lury").setEditor(null);
	egu.getColumn("lursj").setHeader("录入时间");
	egu.getColumn("lursj").setHidden(true);
	egu.getColumn("lursj").setEditor(null);
	egu.getColumn("shenhzt").setHeader("状态");
	egu.getColumn("shenhzt").setHidden(true);
	egu.getColumn("shenhzt").setEditor(null);
	
	
	egu.getColumn("aar").setDefaultValue("0");
	egu.getColumn("stad").setDefaultValue("0");
	egu.getColumn("aad").setDefaultValue("0");
	egu.getColumn("qbad").setDefaultValue("0");
	egu.getColumn("had").setDefaultValue("0");
	egu.getColumn("vad").setDefaultValue("0");
	egu.getColumn("fcad").setDefaultValue("0");
	egu.getColumn("qgrad").setDefaultValue("0");
	egu.getColumn("hdaf").setDefaultValue("0");
	egu.getColumn("sdaf").setDefaultValue("0");
	egu.getColumn("var").setDefaultValue("0");
	
	
	egu.getColumn("rulrq").setWidth(85);
	egu.getColumn("fenxrq").setWidth(85);
	egu.getColumn("diancxxb_id").setWidth(85);
	egu.getColumn("rulbzb_id").setWidth(85);
	egu.getColumn("rulbzbmc").setWidth(85);
	egu.getColumn("jizfzb_id").setWidth(85);
	egu.getColumn("jizfzbmc").setWidth(85);
	egu.getColumn("meil").setWidth(85);
	
	
	egu.getColumn("qnet_ar").setWidth(80);
	egu.getColumn("aar").setWidth(60);
	egu.getColumn("ad").setWidth(60);
	egu.getColumn("vdaf").setWidth(60);
	egu.getColumn("mt").setWidth(60);
	egu.getColumn("stad").setWidth(60);
	egu.getColumn("aad").setWidth(60);
	egu.getColumn("mad").setWidth(60);
	egu.getColumn("qbad").setWidth(60);
	egu.getColumn("had").setWidth(60);
	egu.getColumn("vad").setWidth(60);
	egu.getColumn("fcad").setWidth(60);
	egu.getColumn("std").setWidth(60);
	egu.getColumn("qgrad").setWidth(60);
	egu.getColumn("hdaf").setWidth(60);
	egu.getColumn("sdaf").setWidth(60);
	egu.getColumn("var").setWidth(60);
	egu.getColumn("huayy").setWidth(60);
	
	
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(25);//设置分页
	//*****************************************设置默认值****************************
	egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
	egu.getColumn("rulrq").setDefaultValue(riqTiaoj);
	egu.getColumn("fenxrq").setDefaultValue(DateUtil.FormatDate(new Date()));
	egu.getColumn("lury").setDefaultValue(visit.getRenymc());
	egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
	egu.getColumn("shenhzt").setDefaultValue("0");
	
	
	//设置下拉框入炉班组
//	ComboBox cb_banz=new ComboBox();
//	egu.getColumn("rulbzb_id").setEditor(cb_banz);
//	cb_banz.setEditable(true);
//	String rulbzb_idSql="select r.id,r.mingc from rulbzb r where r.diancxxb_id="+visit.getDiancxxb_id()+" order by r.xuh";
//	egu.getColumn("rulbzb_id").setComboEditor(egu.gridId, new IDropDownModel(rulbzb_idSql));
////	设置下拉框入炉机组
//	ComboBox cb_jiz=new ComboBox();
//	egu.getColumn("jizfzb_id").setEditor(cb_jiz);
//	cb_jiz.setEditable(true);
//	String cb_jizSql="select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="+visit.getDiancxxb_id()+"";
//	egu.getColumn("jizfzb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_jizSql));
	
	
//	 工具栏
	egu.addTbarText("日期:");
	DateField df = new DateField();
	df.setValue(this.getRiqi());
	df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
	egu.addToolbarItem(df.getScript());

	//************************************************************
//	刷新按钮
	StringBuffer rsb = new StringBuffer();
	rsb.append("function (){")
	 .append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQI').value+'日期的数据,请稍候！'",true))
			.append("document.getElementById('RefreshButton').click();}");
	GridButton gbr = new GridButton("刷新", rsb.toString());
	gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
	egu.addTbarBtn(gbr);

	//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
	//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
			setRiqi(DateUtil.FormatDate(new Date()));
			
		}
		getSelectData();
	}

//	日期控件
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(new Date());
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		
		
	}
}
