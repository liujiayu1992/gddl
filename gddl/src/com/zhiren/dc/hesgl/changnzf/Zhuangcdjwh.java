package com.zhiren.dc.hesgl.changnzf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 刘雨
 * 2010-01-28
 * 类名：单价设置跳转页
 */
/**
 * @author 尹佳明
 * 2009-10-30
 * 类名：Zhuangcdjwh(装车单价维护)
 */

public class Zhuangcdjwh extends BasePage implements PageValidateListener {
	
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
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick){
			_Refreshclick = false;
			getSelectData();
		}
		if (_SaveChick){
			_SaveChick = false;
			save();
		}
		if (_ReturnChick){
			_ReturnChick = false;
			cycle.activate("Zhuangcjs");
		}
	}
	
	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		ResultSetList rsl=null;
		String sql_mc="";
		sbsql.append("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from zhuangcdwjgb where id = ").append(delrsl.getString("id")).append("; \n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {

			if ("0".equals(mdrsl.getString("id"))) {
				//判断是否有重复编码、名称
				sql_mc="select * from zhuangcdwjgb where zhuangcdw_item_id in (select id from item where mingc='"+mdrsl.getString("zhuangcdw").toString().trim()+"')\n";
				rsl = con.getResultSetList(sql_mc);
				 if(rsl.next()){
					 setMsg("装车单位"+mdrsl.getString("zhuangcdw").toString().trim()+"有重复,不能保存！");
					 return;
				 }
				 
				sbsql.append("insert into zhuangcdwjgb(id, diancxxb_id, zhuangcdw_item_id, danj, " +
				"shuil, zhuangt, qiyrq, guojrq, beiz) values(").append("getnewid("+getTreeid()+"), ")
				.append(getTreeid()).append(", ")
				.append((getExtGrid().getColumn("zhuangcdw").combo).getBeanId(mdrsl.getString("zhuangcdw")))
				.append(", ").append(getValueSql(getExtGrid().getColumn("danj"), mdrsl.getString("danj")))
				.append(", ").append(getValueSql(getExtGrid().getColumn("shuil"), mdrsl.getString("shuil")))
				.append(", ").append((getExtGrid().getColumn("zhuangt").combo).getBeanId(mdrsl.getString("zhuangt")))
				.append(", ").append(DateUtil.FormatOracleDate(mdrsl.getString("qiyrq")))
				.append(", ").append(DateUtil.FormatOracleDate(mdrsl.getString("guojrq")))
				.append(", '").append(mdrsl.getString("beiz")).append("'); \n");
			} else {
				//判断是否有重复编码、名称
				sql_mc="select z.id,i.mingc from item i,zhuangcdwjgb z where z.zhuangcdw_item_id = i.id and i.mingc='"+mdrsl.getString("zhuangcdw").toString().trim()+"' and z.id<>"+mdrsl.getString("id")+"\n";
				rsl = con.getResultSetList(sql_mc);
				 if(rsl.next()){
					 setMsg("装车单位"+mdrsl.getString("zhuangcdw").toString().trim()+"有重复,不能保存！");
					 return;
				 }
				sbsql.append("update zhuangcdwjgb set zhuangcdw_item_id = ")
				.append((getExtGrid().getColumn("zhuangcdw").combo).getBeanId(mdrsl.getString("zhuangcdw")))
				.append(", danj = ").append(getValueSql(getExtGrid().getColumn("danj"), mdrsl.getString("danj")))
				.append(", shuil = ").append(getValueSql(getExtGrid().getColumn("shuil"), mdrsl.getString("shuil")))
				.append(", zhuangt = ").append((getExtGrid().getColumn("zhuangt").combo).getBeanId(mdrsl.getString("zhuangt")))
				.append(", qiyrq = ").append(DateUtil.FormatOracleDate(mdrsl.getString("qiyrq")))
				.append(", guojrq = ").append(DateUtil.FormatOracleDate(mdrsl.getString("guojrq")))
				.append(", beiz = '").append(mdrsl.getString("beiz"))
				.append("' where id = ").append(mdrsl.getString("id")).append("; \n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String str = 
			"select z.id,\n" +
			"       i.mingc zhuangcdw,\n" + 
			"       z.danj,\n" + 
			"       z.shuil,\n" + 
			"       decode(z.zhuangt, 1, '是', 0, '否') zhuangt,\n" + 
			"       z.qiyrq,\n" + 
			"       z.guojrq,\n" + 
			"       z.beiz\n" + 
			"  from zhuangcdwjgb z, item i\n" + 
			" where z.zhuangcdw_item_id = i.id\n" +
			"	    and z.diancxxb_id = "+ getTreeid() + "\n" +
			" order by z.qiyrq, z.guojrq";

		ResultSetList rsl = con.getResultSetList(str);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("zhuangcdw").setHeader("装车单位");
		egu.getColumn("zhuangcdw").setWidth(130);
		egu.getColumn("danj").setHeader("单价");
		egu.getColumn("danj").setWidth(70);
		egu.getColumn("shuil").setHeader("税率");
		egu.getColumn("shuil").setWidth(70);
		egu.getColumn("zhuangt").setHeader("是否启用");
		egu.getColumn("qiyrq").setHeader("启用日期");
		egu.getColumn("qiyrq").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("guojrq").setHeader("过期日期");
		egu.getColumn("guojrq").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(200);
		
//		装车单位下拉框
		ComboBox zhuangcdw_comb = new ComboBox();
		zhuangcdw_comb.setListWidth(150);
		String tmpStr = "select id, mingc from item where zhuangt = 1 and itemsortid = (select itemsortid " +
			"from itemsort where bianm = 'ZHUANGCDW') order by mingc";
		egu.getColumn("zhuangcdw").setEditor(zhuangcdw_comb);
		egu.getColumn("zhuangcdw").setComboEditor(egu.gridId, new IDropDownModel(tmpStr));
//		egu.getColumn("zhuangcdw").setReturnId(true);
		
//		状态下拉框
		ComboBox zhuangt_comb = new ComboBox();
		List zhuangt_list = new ArrayList();
		zhuangt_list.add(new IDropDownBean(0, "否"));
		zhuangt_list.add(new IDropDownBean(1, "是"));
		egu.getColumn("zhuangt").setEditor(zhuangt_comb);
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(zhuangt_list));
		egu.getColumn("zhuangt").setDefaultValue("是");
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新",
		"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("返回","function(){ document.getElementById('ReturnButton').click();" +
		"}").getScript()+"}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
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
	
//	设置电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)this.getPage().getVisit()).getDiancxxb_id()));
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
//	设置电厂树_结束
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "default" : value;
			}
		} else {
			return value;
		}
	}

	public void pageValidate(PageEvent event) {
		String PageName = event.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(event.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(event.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = event.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
		}
		getSelectData();
	}
}
