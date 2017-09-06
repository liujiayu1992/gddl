package com.zhiren.dc.rulgl.feihlzjc;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：刘雨
 * 时间：2010-04-14
 * 描述：新增功能：1.用方向键控制光标移动
 * 		适用范围：大唐阳城
 */

public class Feihlzjc extends BasePage implements PageValidateListener {
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
	}

	//记录Jizb表ID
	public long getJizb_id() {
		return getJizValue().getId();
	}
	
	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
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

		if (_RefurbishChick) {
			_RefurbishChick = false;;
		}
		getSelectData();
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
//		是否为阳城发电
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电",
				visit.getDiancxxb_id() + "", "否"));
		
		String fenxsj = this.getRiqi();
		if (fenxsj == null || fenxsj.equals("")) {
			fenxsj = DateUtil.FormatDate(new Date());
		}
		String sql = "select fh.id, fh.diancxxb_id, fh.jizxxb_id, hd.mingc as huidxxb_id, " +
				"fenxsj, quysj, kerw, fh.beiz,fh.lury,fh.lursj from feihlzb fh, jizxxb jz, huidxxb hd where " +
				"fh.jizxxb_id = jz.id and fh.huidxxb_id = hd.id and fh.diancxxb_id = " + getTreeid() + 
				" and fenxsj = to_date('" + fenxsj + "','yyyy-mm-dd') and jizxxb_id = " + getJizb_id();

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("feihlzb");
		egu.setWidth("bodyWidth");

		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("jizxxb_id").setHidden(true);
		egu.getColumn("jizxxb_id").setDefaultValue(getJizb_id() + "");
		egu.getColumn("huidxxb_id").setHeader("灰斗");
		// 灰斗下拉框
		ComboBox cbx = new ComboBox();
		egu.getColumn("huidxxb_id").setEditor(cbx);
		cbx.setEditable(false);
		sql = "select id, mingc from huidxxb order by xuh";
		egu.getColumn("huidxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("fenxsj").setHidden(true);
		egu.getColumn("fenxsj").setDefaultValue(fenxsj);
		egu.getColumn("quysj").setHeader("取样时间");
		egu.getColumn("quysj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("kerw").setHeader("可燃物");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setDefaultValue(fenxsj);
		
		if(isYc){
			egu.getColumn("kerw").editor.setListeners(getStr(0));
			egu.getColumn("beiz").editor.setListeners(getStr(1));
		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

        // 日期
		egu.addTbarText("分析时间:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		// 机组
		egu.addTbarText("机组:");
		ComboBox jiz = new ComboBox();
		jiz.setTransform("JIZB_ID");
		jiz.setId("JIZB_ID");
		jiz.setListeners("select:function(){document.Form0.submit();}");
		jiz.setLazyRender(true);
		jiz.setEditable(false);
		jiz.setWidth(110);
		egu.addToolbarItem(jiz.getScript());
		egu.addTbarText("-");
		
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		//多行替换
		egu.addTbarText("-");
		egu.addTbarText("-");
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("多行替换");
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){\n" +
				
				" if(SelectLike.checked) { \n" +
				
				"for(var i=e.row;i<gridDiv_ds.getCount();i++){\n" +
				"var rec=gridDiv_ds.getAt(i);\n" +
				" rec.set(e.field+'',e.value);\n" +
				"}\n" +
				
				"}\n" +
				
				"" +
				"  }); ");
		
		
		if(isYc){
			egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;col=icol;});");//得到行、列
		}
		
		setExtGrid(egu);
		con.Close();
	}
	
//	方向键控制光标监听JS
	public String getStr(int col){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Str = "";
////		 硫是否可编辑
//		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
//				visit.getDiancxxb_id(), false);
		Str = "specialkey:function(own,e){\n" +
				"			if(row>0){\n" +
				"				if(e.getKey()==e.UP){\n" +
				"					gridDiv_grid.startEditing(row-1, col);\n" +
				"					row = row-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(row+1 < gridDiv_grid.getStore().getCount()){\n" +
				"				if(e.getKey()==e.DOWN){\n" +
				"					gridDiv_grid.startEditing(row+1, col);\n" +
				"					row = row+1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.LEFT){\n" +
				"				if("+col+"!=0){\n" +
				"					gridDiv_grid.startEditing(row, col-1);\n" +
				"					col = col-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.RIGHT){\n" +
				"				if("+col+"!=1){\n" +
				"					gridDiv_grid.startEditing(row, col+1);\n" +
				"					col = col+1;\n" +
				"				}\n" +
				"			}\n" +
				"	 	 }\n";	
		return Str;
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
			
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
		}
		getSelectData();
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
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
	
    //机组下拉框
	public IDropDownBean getJizValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getJizModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getJizModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setJizValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setJizModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getJizModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIJizModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIJizModels() {		
		String sql = "select id, mingc from jizxxb order by id";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
}
