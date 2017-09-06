package com.zhiren.dc.hesgl.zafjs;

import java.util.ArrayList;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：发货杂费结算修改
 */

public class Fahzfjsxg extends BasePage implements PageValidateListener {

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
	
//	发货起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	发货截止日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
//	杂费名称下拉框_开始
	public IDropDownBean getZafmcValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean28() == null) {
			if (getZafmcModel().getOptionCount() > 0) {
				setZafmcValue((IDropDownBean) getZafmcModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean28();
	}

	public void setZafmcValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean28(LeibValue);
	}

	public IPropertySelectionModel getZafmcModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel28() == null) {
			getZafmcModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel28();
	}

	public void setZafmcModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel28(value);
	}

	public void getZafmcModels() {
		String sql = "select it.id, it.mingc from item it where it.itemsortid = (select id from itemsort ist where ist.bianm = 'DZZF') order by it.mingc";
		setZafmcModel(new IDropDownModel(sql));
	}
//	杂费名称下拉框_结束
	
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
		
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			sbsql.append("update zafjsxxb js set js.shuil =").append(getSqlValue(mdrsl.getString("shuil")))
				 .append(", js.shuik = ").append(getSqlValue(mdrsl.getString("shuik")))
				 .append(", js.zongje = ").append(getSqlValue(mdrsl.getString("zafje")))
				 .append(", js.shifds = ").append(getComboxValue(String.valueOf((getExtGrid().getColumn("shifds").combo).getBeanId(mdrsl.getString("shifds")))))
				 .append(", js.beiz = '").append(mdrsl.getString("beiz")).append("' where js.id = ").append(mdrsl.getString("zafjsxxb_id"))
				 .append(";\n");
		}
		sbsql.append("end;");
		if (con.getUpdate(sbsql.toString()) == 1) {
			setMsg("更新成功！");
		} else {
			setMsg("更新失败！");
		}
		
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String where = "";
    	String gongysb_id = this.getGongysmlttree_id();
    	
    	if(!(gongysb_id==null||gongysb_id.equals(""))){
			where = "   and (mk.id in ("+ gongysb_id +") or gys.id in ("+ gongysb_id +"))\n";
		}
		
		String sql = 
			"select to_char(js.id) zafjsxxb_id,\n" +
			"       gys.mingc fahdw,\n" +
			"       gys.id gongysb_id,\n" +
			"       fh.id fahb_id,\n" +
			"       mk.mingc meikdw,\n" + 
			"       mk.id meikxxb_id,\n" +
			"       fh.fahrq,\n" + 
			"       fh.daohrq,\n" + 
			"       cz.mingc faz,\n" + 
			"       fh.ches,\n" + 
			"       fh.biaoz,\n" + 
			"       fh.yingk,\n" + 
			"       fh.yuns,\n" + 
			"       fh.jingz,\n" + 
			"       js.shuil,\n" + 
			"       js.shuik,\n" + 
			"       decode(js.shifds, 1, '是', '否') shifds,\n" + 
			"       js.zongje as zafje,\n" +
			"       js.beiz\n" +
			"  from fahb fh, gongysb gys, meikxxb mk, hetb ht, chezxxb cz, zhilb zl, hetzfb zf, zafjsxxb js\n" + 
			" where fh.gongysb_id = gys.id\n" + 
			"   and fh.meikxxb_id = mk.id\n" + 
			"   and fh.hetb_id = ht.id\n" + 
			"   and fh.faz_id = cz.id\n" + 
			"   and fh.zhilb_id = zl.id\n" + 
			"   and zl.liucztb_id = 1\n" + 
			"   and fh.fahrq >= to_date('"+ getBRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and fh.fahrq <= to_date('"+ getERiq() +"', 'yyyy-mm-dd')\n" + 
			"   and zf.hetb_id = ht.id\n" + where +
			"   and zf.item_id = "+ getZafmcValue().getStrId() +"\n" + 
			"   and js.fahb_id = fh.id\n" +
			"   and js.zafmc_item_id = zf.item_id\n" + 
			"union\n" + 
			"select '' as zafjsxxb_id,\n" + 
			"       '合计' as fahdw,\n" + 
			"       to_number('') fahb_id,\n" +
			"       to_number('') gongysb_id,\n" +
			"       '' as meikdw,\n" + 
			"       to_number('') meikxxb_id,\n" +
			"       null as fahrq,\n" + 
			"       null as daohrq,\n" + 
			"       '' as faz,\n" + 
			"       to_number('') as ches,\n" + 
			"       to_number('') as biaoz,\n" + 
			"       to_number('') as yingk,\n" + 
			"       to_number('') as yuns,\n" + 
			"       to_number('') as jingz,\n" + 
			"       to_number('') as shuil,\n" + 
			"       to_number('') as shuik,\n" + 
			"       '' as shifds,\n" + 
			"       to_number('') as zafje,\n" + 
			"       '' as beiz\n" +
			"  from dual\n" + 
			" where exists\n" + 
			"(select fh.id\n" + 
			"  from fahb fh, gongysb gys, meikxxb mk, hetb ht, chezxxb cz, zhilb zl, hetzfb zf, zafjsxxb js\n" + 
			" where fh.gongysb_id = gys.id\n" + 
			"   and fh.meikxxb_id = mk.id\n" + 
			"   and fh.hetb_id = ht.id\n" + 
			"   and fh.faz_id = cz.id\n" + 
			"   and fh.zhilb_id = zl.id\n" + 
			"   and zl.liucztb_id = 1\n" + 
			"   and fh.fahrq >= to_date('"+ getBRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and fh.fahrq <= to_date('"+ getERiq() +"', 'yyyy-mm-dd')\n" + 
			"   and zf.hetb_id = ht.id\n" + where +
			"   and zf.item_id = "+ getZafmcValue().getStrId() +
			"   and js.fahb_id = fh.id\n" +
			"   and js.zafmc_item_id = zf.item_id\n" + 
			")";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("zafjsxxb_id").setHeader("ZAFJSXXB_ID");
		egu.getColumn("zafjsxxb_id").setEditor(null);
		egu.getColumn("zafjsxxb_id").setHidden(true);
		egu.getColumn("fahb_id").setHeader("FAHB_ID");
		egu.getColumn("fahb_id").setEditor(null);
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahdw").setHeader("发货单位");
		egu.getColumn("fahdw").setEditor(null);
		egu.getColumn("fahdw").setWidth(110);
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(110);
		egu.getColumn("meikxxb_id").setHidden(true);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setHeader("盈亏");
		egu.getColumn("yingk").setEditor(null);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("yuns").setHeader("运损");
		egu.getColumn("yuns").setEditor(null);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("shuil").setHeader("税率");
		egu.getColumn("shuil").setWidth(60);
		egu.getColumn("shuik").setHeader("税款");
		egu.getColumn("shuik").setWidth(70);
		egu.getColumn("shifds").setHeader("是否抵税");
		egu.getColumn("shifds").setWidth(70);
		egu.getColumn("zafje").setHeader(getZafmcValue().getValue() + "金额");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(130);
		
//		在第2列加入复选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(0, "否"));
		list.add(new IDropDownBean(1, "是"));
		egu.getColumn("shifds").setEditor(new ComboBox());
		egu.getColumn("shifds").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("shifds").editor.setAllowBlank(true);
		
		egu.addTbarText("发货日期");
		DateField bdf = new DateField();
		bdf.setReadOnly(true);
		bdf.setValue(getBRiq());
		bdf.Binding("BRiq", "");
		egu.addToolbarItem(bdf.getScript());
		
		egu.addTbarText("至");
		DateField edf = new DateField();
		edf.setReadOnly(true);
		edf.setValue(getERiq());
		edf.Binding("ERiq", "");
		egu.addToolbarItem(edf.getScript());
		egu.addTbarText("-"); 
		
		egu.addTbarText("供货单位：");
		String condition = "and fh.fahrq >= to_date('"+ getBRiq() +"','yyyy-MM-dd') and fh.fahrq <= to_date('"+ getERiq() +"','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree", ExtTreeUtil.treeWindowCheck_gongys, 
			((Visit) this.getPage().getVisit()).getDiancxxb_id(), getTreeid(), condition, true);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		egu.addTbarText("-");
		
		egu.addTbarText("杂费名称：");
		ComboBox cb = new ComboBox();
		cb.setWidth(120);
		cb.setListWidth(150);
		cb.setTransform("ZAFMCSelect");
		cb.setId("Zafmc");
		cb.setLazyRender(true);
		cb.setEditable(false);
		egu.addToolbarItem(cb.getScript());
		egu.addOtherScript("Zafmc.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		String rfb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rfb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addTbarText("-");
		egu.addToolbarButton("保存", GridButton.ButtonType_SubmitSel_condition, "SaveButton", 
				"var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
				"if (rec.length == 1 && rec[0].get('FAHB_ID') == '') {\n" +
				"    Ext.MessageBox.alert('提示信息','没有选择数据信息！');\n" + 
				"    return;\n" + 
				"}\n" + 
				"if (rec.length == 0) {\n" + 
				"    Ext.MessageBox.alert('提示信息','请选择数据信息！');\n" + 
				"    return;\n" + 
				"}\n" + 
				"for (var i=0; i < rec.length; i++) {\n" + 
				"    if (rec[i].get('FAHB_ID') == '') {\n" + 
				"        Ext.MessageBox.alert('提示信息','不需要选择合计！');\n" + 
				"        return;\n" + 
				"    }\n" + 
				"}");
		
//		页面自动计算合计
		egu.addOtherScript(
				"gridDiv_grid.on('rowclick',function(){\n" +
				"    var ches=0, biaoz=0, yingk=0, yuns=0, jingz=0, shuik=0, zafje=0;\n" + 
				"    var rec = gridDiv_grid.getSelectionModel().getSelections();\n" + 
				"    for(var i=0; i<rec.length; i++){\n" + 
				"        if(''!=rec[i].get('FAHB_ID')){\n" + 
				"            ches += eval(rec[i].get('CHES'));\n" + 
				"            biaoz += eval(rec[i].get('BIAOZ'));\n" + 
				"            yingk += eval(rec[i].get('YINGK'));\n" + 
				"            yuns += eval(rec[i].get('YUNS'));\n" + 
				"            jingz += eval(rec[i].get('JINGZ'));\n" + 
				"            shuik += eval(rec[i].get('SHUIK')||0);\n" + 
				"            zafje += eval(rec[i].get('ZAFJE')||0);\n" + 
				"        }\n" + 
				"    }\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('SHUIK',shuik);\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('ZAFJE',zafje);\n" + 
				"});");
		
//		点全选时计算合计
		egu.addOtherScript(
				"gridDiv_grid.on('click',function(){\n" +
				"    reCountToolbarNum(this);\n" + 
				"});\n" + 
				"\n" + 
				"function reCountToolbarNum(obj){\n" + 
				"    var ches=0, biaoz=0, yingk=0, yuns=0, jingz=0, shuik=0, zafje=0;\n" + 
				"    var rec = obj.getSelectionModel().getSelections();\n" + 
				"    for(var i=0; i<rec.length; i++){\n" + 
				"        if(''!=rec[i].get('FAHB_ID')){\n" + 
				"            ches += eval(rec[i].get('CHES'));\n" + 
				"            biaoz += eval(rec[i].get('BIAOZ'));\n" + 
				"            yingk += eval(rec[i].get('YINGK'));\n" + 
				"            yuns += eval(rec[i].get('YUNS'));\n" + 
				"            jingz += eval(rec[i].get('JINGZ'));\n" + 
				"            shuik += eval(rec[i].get('SHUIK')||0);\n" + 
				"            zafje += eval(rec[i].get('ZAFJE')||0);\n" + 
				"        }\n" + 
				"    }\n" + 
				"    if(gridDiv_ds.getCount()>0){\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('SHUIK',shuik);\n" + 
				"        gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('ZAFJE',zafje);\n" + 
				"    }\n" + 
				"}");
		
//		多选供应商树的赋值
		egu.addOtherScript(
				"gongysTree_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },gongysTree_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif(gongysTree_history==\"\"){\n" + 
				"\t\t\tgongysTree_history = history;\n" + 
				" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = gongysTree_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  gongysTree_history = his.join(\";\");\n" + 
				"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 gongysTree_history += history;\n" + 
				"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
//	gongysTree_id_begin
	public String getGongysmlttree_id(){
		
		String gongys_id="";
		if(((Visit) getPage().getVisit()).getString14()!=null
				&&!((Visit) getPage().getVisit()).getString14().equals("")){
			
			String change[] = ((Visit) getPage().getVisit()).getString14().split(";");
			if(change.length==1&&
					(change[0].indexOf("+")==-1&&change[0].indexOf("-")==-1)){
				
				gongys_id=change[0];
			}else{
				
				for(int i = 0 ;i < change.length ; i++) {
					if(change[i] == null || "".equals(change[i])) {
						continue;
					}
					String record[] = change[i].split(",",2);
					String sign = record[0];
					String ziyid = record[1];
					if("+".equals(sign)) {
						gongys_id+=ziyid+",";
					}
				}
				
				if(!gongys_id.equals("")){
					
					gongys_id=gongys_id.substring(0,gongys_id.lastIndexOf(","));
				}
			}
			
		}
		
		if(gongys_id.indexOf(",")==-1){
			
			if(gongys_id.equals("")){
				
				this.setTreeid("0");
			}else{
				
				this.setTreeid(MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongys_id));
			}
			
		}else{
			
			this.setTreeid(MainGlobal.getLeaf_ParentNodeId(this.getTree(), 
					gongys_id.substring(0,gongys_id.indexOf(","))));
		}
		
		return gongys_id;
	}
	
	public void setGongysmlttree_id(String value){
		((Visit) getPage().getVisit()).setString14(value);
	}
//	gongysTree_id_end
	
	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	/**
	 * 如果在页面上取到的Combox值为-1，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getComboxValue(String value) {
		return value == null || "-1".equals(value) ? "default" : value;
	}
	
//	设置树_开始
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
//	设置树_结束
	
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
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setString14("");	// gongysbmlt_id(供应商表)
			((Visit) getPage().getVisit()).setDropDownBean28(null);
			((Visit) getPage().getVisit()).setProSelectionModel28(null);
		}
		getSelectData();
	}

}
