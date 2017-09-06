package com.zhiren.jt.zdt.duib;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 尹佳明
 * 2009-07-16
 * 类名：Duibzbwh(对比指标维护)
 */
/* 
* 时间：2009-07-23
* 作者： ll
* 修改内容：填报数据时增加电厂下拉框，选取电厂名称。
* 		   修改保存数据时，不应保存电厂树id的问题。
*                    
*			
*/ 
public class Duibzbwh extends BasePage {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	电厂树_begin
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
//	电厂树_end
	
//	获得电厂树级别，1是集团、2是分公司、3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String diancTreeJib = this.getTreeid();
		String sqlJib = "select d.jib from diancxxb d where d.id = "+ diancTreeJib;
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
	
//	年份下拉框_begin
	private IDropDownBean _NianfValue;
	
	private IPropertySelectionModel _NianfModel;
	
	public boolean nianfchanged;
	
	public IDropDownBean getNianfValue() {
		if (_NianfValue == null || _NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
					return _NianfValue = (IDropDownBean) obj;
					
				}
			}
		}
		return _NianfValue;
	}
	
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}
	
	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}
//	年份下拉框_end
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick){
			_Refreshclick = false;
		}
		if (_SaveChick){
			_SaveChick = false;
			save();
		}
	}
	
	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin \n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from duibzb where id = ")
			.append(delrsl.getString("id")).append("; \n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			String rezc = mdrsl.getString("rezc");
			if (rezc.equals("")) {
				rezc = "512";
			}
			String jianjl = mdrsl.getString("jianjl");
			if (jianjl.equals("")) {
				jianjl = "0";
			}
			String jiancl = mdrsl.getString("jiancl");
			if (jiancl.equals("")) {
				jiancl = "0";
			}
			if ("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into duibzb (id, riq, diancxxb_id, biaomdj, rezc, jianjl, jiancl, beiz) values(")
				.append("getnewid("+ getTreeid() +"), to_date('").append(getNianfValue().getValue()).append("-01-01', 'yyyy-MM-dd'), ")
				.append("(select max(id) from diancxxb where mingc ='"+mdrsl.getString("diancxxb_id")+"')").append(", ").append(mdrsl.getString("biaomdj")).append(", ")
				.append(rezc).append(", ").append(jianjl).append(", ")
				.append(jiancl).append(", '").append(mdrsl.getString("beiz")).append("'); \n");
			} else {
				sbsql.append("update duibzb set biaomdj = ").append(mdrsl.getString("biaomdj")).append(", rezc = ").append(rezc)
				.append(", jianjl = ").append(jianjl).append(", jiancl = ").append(jiancl)
				.append(", beiz = '").append(mdrsl.getString("beiz")).append("' where id = ").append(mdrsl.getString("id"))
				.append("; \n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
		
	}

	private void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sbsql = new StringBuffer();
		
		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) { // 选集团时刷新出所有的电厂
			str = "select d.id from diancxxb d where d.fuid in (select d.id from diancxxb d " +
				"where d.fuid in (select d.id from diancxxb d where d.fuid = "+ getTreeid() +")) " +
				"or d.fuid in (select d.id from diancxxb d where d.fuid = "+ getTreeid() +") " +
				"or d.fuid = "+ getTreeid() +" or d.id = "+ getTreeid() +"";
		} else if (treejib == 2) { // 选分公司的时候刷新出分公司下所有的电厂
			str = "select d.id from diancxxb d where d.fuid in (select d.id from diancxxb d " +
				"where d.fuid = "+ getTreeid() +") or d.fuid = "+ getTreeid() +" " +
				"or d.id = "+ getTreeid() +"";
		} else if (treejib == 3) { // 选电厂只刷新出该电厂
			str = "select d.id from diancxxb d where d.fuid = "+ getTreeid() +" or d.id = "+ getTreeid() +"";
		}
		
		sbsql.append("select dbzb.id, dcxx.mingc as diancxxb_id, dbzb.biaomdj, dbzb.rezc, dbzb.jianjl, dbzb.jiancl, dbzb.beiz from duibzb dbzb, diancxxb dcxx " +
			"where dbzb.diancxxb_id = dcxx.id and dbzb.diancxxb_id in ("+ str +") " +
			"and riq = to_date('"+ getNianfValue().getValue() +"-01-01', 'yyyy-MM-dd') " + "order by dcxx.mingc, dbzb.biaomdj");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("rezc").setHeader("热值差");
		egu.getColumn("rezc").setDefaultValue("512");
		egu.getColumn("jianjl").setHeader("检斤率");
		egu.getColumn("jianjl").setDefaultValue("0");
		egu.getColumn("jiancl").setHeader("检质率");
		egu.getColumn("jiancl").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("备注");
		
		
		String dc_sql="";
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			dc_sql=	"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc";
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,	new IDropDownModel(dc_sql));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			dc_sql=	"select id,mingc from diancxxb where fuid="+ getTreeid() + " order by mingc";
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,	new IDropDownModel(dc_sql));
			egu.getColumn("diancxxb_id").setReturnId(true);
			
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			dc_sql="select id,mingc from diancxxb where id="+getTreeid()+" order by mingc";
			ResultSetList r = con.getResultSetList("select id,mingc as dcmc from diancxxb where id="+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("dcmc");
			}
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,	new IDropDownModel(dc_sql));
			egu.getColumn("diancxxb_id").setReturnId(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		}
		
		
		egu.addTbarText("年份：");
		ComboBox comb = new ComboBox();
		comb.setWidth(60);
		comb.setTransform("NIANF");
		comb.setId("NIANF_EXT");
		comb.setLazyRender(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("NIANF_EXT.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		String rfb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rfb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		String condition="var Mrcd_bo = gridDiv_ds.getModifiedRecords();" +
			"for(i = 0; i< Mrcd_bo.length; i++){" +
			"	if(Mrcd_bo[i].get('BIAOMDJ')!='0' && Mrcd_bo[i].get('BIAOMDJ') == ''){Ext.MessageBox.alert('提示信息','字段 标煤单价 不能为空');return;}" +
			"}\n";
		GridButton gdb = new GridButton(GridButton.ButtonType_Save_condition,egu.gridId,egu.getGridColumns(), "SaveButton", condition);
		egu.addTbarBtn(gdb);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		setExtGrid(egu);
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
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			this.setNianfValue(null);
			this.setNianfModel(null);
			this.getNianfModels();
		}
		getSelectData();
	}
	
}
