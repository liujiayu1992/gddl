package com.zhiren.pub.qichjcsjpp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-05-15
 * 王磊
 * 去掉注释中非GB2312字符集字符
 */
public class Qichjcsjpp extends BasePage implements PageValidateListener {
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

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
    	getSelectData();
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			if(Savepd()){
				Save();
			}else{
				setMsg("保存时发现重复的记录，保存失败！");
			}
			getSelectData();
		}
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
	}
	
//	保存条件判断:是否有一致的数据
	public boolean Savepd() {
		JDBCcon con = new JDBCcon();
        StringBuffer sb = new StringBuffer();
        boolean sv = true;
        String sql = "";
        int i = 0;
        String[][] rec = null;
        try {
        	sql = 
    			"select q.id,q.diancxxb_id,q.gongysmc,q.meikdwmc,q.pinz,\n" +
    			"       g.mingc as gongysb_id,\n" + 
    			"       m.mingc as meikxxb_id,\n" + 
    			"       p.mingc as pinzb_id,\n" + 
    			"       j.mingc as jihkjb_id\n" + 
    			"from qichjcsjppb q,gongysb g,pinzb p,meikxxb m,jihkjb j\n" + 
    			"where q.gongysb_id = g.id\n" + 
    			"      and q.meikxxb_id = m.id\n" + 
    			"      and q.pinzb_id = p.id\n" + 
    			"      and q.jihkjb_id = j.id\n" + 
    			"order by q.id";
        	sb.append(sql);
            ResultSet rs=con.getResultSet(sb.toString());
            ResultSetList rsl=this.getExtGrid().getModifyResultSet(this.getChange());
            while(rsl.next()){
            	while(rs.next()){
            		if(rsl.getString("DIANCXXB_ID").equals(rs.getString("DIANCXXB_ID"))&&rsl.getString("GONGYSMC").equals(rs.getString("GONGYSMC"))&&rsl.getString("MEIKDWMC").equals(rs.getString("MEIKDWMC"))&&rsl.getString("PINZ").equals(rs.getString("PINZ"))&&rsl.getString("GONGYSB_ID").equals(rs.getString("GONGYSB_ID"))&&rsl.getString("MEIKXXB_ID").equals(rs.getString("MEIKXXB_ID"))&&rsl.getString("PINZB_ID").equals(rs.getString("PINZB_ID"))&&rsl.getString("JIHKJB_ID").equals(rs.getString("JIHKJB_ID"))){
            			sv = false;
            		}
            	}
            }
            rs.close();
        }catch (Exception e) {
        	e.printStackTrace();
        }         
		con.Close();
        return sv;
	}
	
//	长别下拉框取值

	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancxxbid = "";
		if(((Visit) getPage().getVisit()).isFencb()){
			diancxxbid = ""+MainGlobal.getProperId(getFencbModel(),this.getChangb());
		}else{
			diancxxbid = ""+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		String sql = 
			"select q.id,q.diancxxb_id,q.gongysmc,q.meikdwmc,q.pinz,\n" +
			"       g.mingc as gongysb_id,\n" + 
			"       m.mingc as meikxxb_id,\n" + 
			"       p.mingc as pinzb_id,\n" + 
			"       j.mingc as jihkjb_id,\n" + 
			"       decode(q.zhuangt,1,'启用','不启用') as zhuangt\n" + 
			"from qichjcsjppb q,gongysb g,pinzb p,meikxxb m,jihkjb j\n" + 
			"where q.gongysb_id = g.id\n" + 
			"      and q.meikxxb_id = m.id\n" + 
			"      and q.pinzb_id = p.id\n" + 
			"      and q.jihkjb_id = j.id\n" + 
			"	   and q.diancxxb_id="+diancxxbid+"\n" + 
			"order by q.id";
			
		
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight - 150");
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("qichjcsjppb");		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("id").setWidth(70);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("diancxxb_id").setDefaultValue(diancxxbid);
		egu.getColumn("gongysmc").setHeader("供应商");
		egu.getColumn("gongysmc").setWidth(95);
		egu.getColumn("gongysmc").setFixed(true);
		egu.getColumn("meikdwmc").setHeader("煤矿单位");
		egu.getColumn("meikdwmc").setWidth(95);
		egu.getColumn("meikdwmc").setFixed(true);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(95);
		egu.getColumn("pinz").setFixed(true);
//		egu.getColumn("yunsdw").setHeader("运输单位");
//		egu.getColumn("yunsdw").setWidth(70);		
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setFixed(true);
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setFixed(true);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(75);
		egu.getColumn("pinzb_id").setFixed(true);
//		egu.getColumn("yunsdwb_id").setHeader("运输单位");
//		egu.getColumn("yunsdwb_id").setWidth(70);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(75);
		egu.getColumn("jihkjb_id").setFixed(true);
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setWidth(75);
		egu.getColumn("zhuangt").setFixed(true);

//		 设置供应商下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String gysSql = "select id,mingc from gongysb where leix=1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gysSql));
//		 设置煤矿单位下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c2);
		c2.setEditable(true);
		String meikSql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(meikSql));		
//		 设置品种下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c3);
		c3.setEditable(true);
		String pzSql = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pzSql));
////		 设置运输单位下拉框
//		ComboBox c4 = new ComboBox();
//		egu.getColumn("yunsdwb_id").setEditor(c4);
//		c4.setEditable(true);
//		String ysdwSql = "select id,mingc from yunsdwb order by mingc";
//		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
//				new IDropDownModel(ysdwSql));
//		 设置计划口径下拉框
		ComboBox c5 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c5);
		c5.setEditable(true);
		String jihkjSql = "select id,mingc from jihkjb order by mingc";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
//		 设置状态下拉框
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "启用"));
		ls.add(new IDropDownBean(0, "不启用"));
		ComboBox c6 = new ComboBox();
		egu.getColumn("zhuangt").setEditor(c6);
		c6.setEditable(true);
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(ls));
		egu.getColumn("zhuangt").setDefaultValue("不启用");
		egu.getColumn("zhuangt").returnId = true;
		
		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		if(((Visit) getPage().getVisit()).isFencb()){
			
			egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(FencbDropDown.getRawValue()=='请选择'){Ext.MessageBox.alert('提示信息','请选择电厂名称'); return;}");
		}else{
			
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		}
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		//逻辑：判断页面中是否有重复的记录
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton",
				"var flag=true;\n"
				+ " var rec=gridDiv_ds.getRange();\n"
				+ "for(var i=0;i<rec.length;i++){\n"
				+ "    for(var j=i+1;j<rec.length;j++){\n"
				+ "        if(rec[i].get('DIANCXXB_ID')==rec[j].get('DIANCXXB_ID')&&rec[i].get('GONGYSMC')==rec[j].get('GONGYSMC')&&rec[i].get('MEIKDWMC')==rec[j].get('MEIKDWMC')&&rec[i].get('PINZ')==rec[j].get('PINZ')&&rec[i].get('GONGYSB_ID')==rec[j].get('GONGYSB_ID')&&rec[i].get('MEIKXXB_ID')==rec[j].get('MEIKXXB_ID')&&rec[i].get('PINZB_ID')==rec[j].get('PINZB_ID')&&rec[i].get('JIHKJB_ID')==rec[j].get('JIHKJB_ID')){\n"
				+ "            Ext.MessageBox.alert('提示信息','无需重复保存！');\n"
				+ "            flag=false;\n"
				+ "            break;\n"
				+ "        }\n"
				+ "    }\n"
				+ "}\n"
				+ "if(!flag){	\n"
				+ "		return;	\n"
				+ "}	\n");
				
		
		if(((Visit) getPage().getVisit()).isFencb()){
			egu.addTbarText("-");
			egu.addTbarText("厂别:");
			ComboBox comb5 = new ComboBox();
			comb5.setTransform("FencbDropDown");
			comb5.setId("FencbDropDown");
			comb5.setEditable(false);
			comb5.setLazyRender(true);// 动态绑定
			comb5.setWidth(135);
			comb5.setReadOnly(true);
			egu.addToolbarItem(comb5.getScript());
		}
		
		
		StringBuffer sb = new StringBuffer();
		String Headers = 

			"		 [\n" +
			"         {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n" + 
			"         {header:'ID', align:'center',rowspan:2},\n" + 
			"         {header:'DIANCXXB_ID', align:'center',rowspan:2},\n" + 
			"         {header:'汽车衡', colspan:3},\n" + 
			"         {header:'系统信息', colspan:5}\n" + 
			"        ],\n" + 
			"        [\n" + 
			"	      {header:'<table><tr><td width=80 align=center style=border:0>供应商</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>煤矿单位</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>品种</td></tr></table>', align:'center'},\n" + 
			"	      {header:'<table><tr><td width=70 align=center style=border:0>供应商</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=70 align=center style=border:0>煤矿单位</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>品种</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>计划口径</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>状态</td></tr></table>'}\n" + 
			"        ]";

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
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
			visit.setString3("");	//厂
			visit.setList1(null);
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			getSelectData();
		}
	}
	
//	厂别
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql,"请选择"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
}
