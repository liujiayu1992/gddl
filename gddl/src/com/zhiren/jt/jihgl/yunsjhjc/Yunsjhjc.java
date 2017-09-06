package com.zhiren.jt.jihgl.yunsjhjc;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 运输计划监测
 */

public class Yunsjhjc extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
//	月份下拉框_开始
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	月份下拉框_结束
	
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from yunsjhjcb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into yunsjhjcb(id, diancxxb_id, gongysb_id, faz_id, riq, dangr_dun, dangr_che, quany_dun, quany_che, ")
				.append("bucjh_dun, bucjh_che) values(getnewid(").append(getTreeid()).append("), ").append(getTreeid()).append(", ")
				.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id"))).append(", ")
				.append((getExtGrid().getColumn("faz_id").combo).getBeanId(mdrsl.getString("faz_id"))).append(", ")
				.append("to_date('").append(getNianfValue().getValue()).append("-").append(getYuefValue().getValue()).append("-1', 'yyyy-mm-dd'),")
				.append(getSqlValue(mdrsl.getString("dangr_dun"))).append(", ").append(getSqlValue(mdrsl.getString("dangr_che"))).append(", ")
				.append(getSqlValue(mdrsl.getString("quany_dun"))).append(", ").append(getSqlValue(mdrsl.getString("quany_che"))).append(", ")
				.append(getSqlValue(mdrsl.getString("bucjh_dun"))).append(", ").append(getSqlValue(mdrsl.getString("bucjh_che"))).append(");\n");
			} else {
				sbsql.append("update yunsjhjcb set ")
				.append(" gongysb_id = ").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")))
				.append(", faz_id = ").append((getExtGrid().getColumn("faz_id").combo).getBeanId(mdrsl.getString("faz_id")))
				.append(", dangr_dun = ").append(getSqlValue(mdrsl.getString("dangr_dun")))
				.append(", dangr_che = ").append(getSqlValue(mdrsl.getString("dangr_che")))
				.append(", quany_dun = ").append(getSqlValue(mdrsl.getString("quany_dun")))
				.append(", quany_che = ").append(getSqlValue(mdrsl.getString("quany_che")))
				.append(", bucjh_dun = ").append(getSqlValue(mdrsl.getString("bucjh_dun")))
				.append(", bucjh_che = ").append(getSqlValue(mdrsl.getString("bucjh_che")))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql = 
			"select jh.id,\n" +
			"       gys.mingc gongysb_id,\n" + 
			"       fz.mingc faz_id,\n" + 
			"       jh.dangr_dun,\n" + 
			"       jh.dangr_che,\n" + 
			"       jh.quany_dun,\n" + 
			"       jh.quany_che,\n" + 
			"       jh.bucjh_dun,\n" + 
			"       jh.bucjh_che\n" + 
			"  from yunsjhjcb jh, gongysb gys, chezxxb fz\n" + 
			" where jh.gongysb_id = gys.id\n" + 
			"   and jh.faz_id = fz.id" +
			"	and jh.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-1', 'yyyy-mm-dd')" +
			"	and jh.diancxxb_id = " + getTreeid();
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("dangr_dun").setHeader("当日吨数");
		egu.getColumn("dangr_dun").setEditor(null);
		egu.getColumn("dangr_che").setHeader("当日车数");
		egu.getColumn("dangr_che").setEditor(null);
		egu.getColumn("quany_dun").setHeader("全月吨数");
		egu.getColumn("quany_che").setHeader("全月车数");
		egu.getColumn("quany_che").setEditor(null);
		egu.getColumn("bucjh_dun").setHeader("补充计划吨数");
		egu.getColumn("bucjh_che").setHeader("补充计划车数");
		
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from gongysb where leix = 1 order by mingc"));
		
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from chezxxb order by mingc"));
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("年份：");
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(80);
		nf_comb.setTransform("Nianf");
		nf_comb.setId("Nianf");
		nf_comb.setLazyRender(true);
		nf_comb.setEditable(true);
		egu.addToolbarItem(nf_comb.getScript());
//		egu.addOtherScript("Nianf.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("月份：");
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(80);
		yf_comb.setTransform("Yuef");
		yf_comb.setId("Yuef");
		yf_comb.setLazyRender(true);
		yf_comb.setEditable(true);
		egu.addToolbarItem(yf_comb.getScript());
//		egu.addOtherScript("Yuef.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		String script = 
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"   if (e.field=='QUANY_DUN') {\n" + 
			"      var quany_che = Math.round(eval(e.record.get('QUANY_DUN'))/60*100)/100;\n" + 
			"      e.record.set('QUANY_CHE', quany_che);\n" + 
			"      var dangr_dun = Math.round(eval(e.record.get('QUANY_DUN'))/30*100)/100;\n" + 
			"      e.record.set('DANGR_DUN', dangr_dun);\n" + 
			"      var dangr_che = Math.round(quany_che/30*100)/100\n" + 
			"      e.record.set('DANGR_CHE', dangr_che);\n" + 
			"   }\n" + 
			"});";
		egu.addOtherScript(script);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
//	电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
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
//	电厂树_结束
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
}