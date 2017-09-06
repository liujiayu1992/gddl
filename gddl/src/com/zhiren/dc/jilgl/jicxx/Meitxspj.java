package com.zhiren.dc.jilgl.jicxx;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * ú̿����Ʊ��
 */

public class Meitxspj extends BasePage implements PageValidateListener {
	
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
	
	private String riq; // ��������
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    // ��Ӧ��������
	public IDropDownBean getGongmdwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getGongmdwModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getGongmdwModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setGongmdwValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setGongmdwModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGongmdwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIGongmdwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIGongmdwModels() {		
		String sql = "select id, mingc from gongysb where leix=1 order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql, "��ѡ��"));
	}
	
//	ú��λ������_��ʼ
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = "select mk.id, mk.mingc from meikxxb mk order by mk.mingc";
		setMeikdwModel(new IDropDownModel(sql, "��ѡ��"));
	}
//	ú��λ������_����
	
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
			sbsql.append("delete from meitxspjb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				long gysid = ((IDropDownModel)getGongmdwModel()).getBeanId(mdrsl.getString("gongysb_id"));
				long mkid = ((IDropDownModel)getMeikdwModel()).getBeanId(mdrsl.getString("meikxxb_id"));
				sbsql.append("insert into meitxspjb(id, gongysb_id, meikxxb_id, riq, bianh, piaoh, cheh, shul) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append(gysid)
				.append(", ")
				.append(mkid)
				.append(", to_date('").append(mdrsl.getString("riq")).append("', 'yyyy-mm-dd'), '")
				.append(mdrsl.getString("bianh")).append("', '").append(mdrsl.getString("piaoh")).append("', '")
				.append(mdrsl.getString("cheh")).append("', ").append(getSqlValue(mdrsl.getString("shul"))).append(");\n");
			} else {
				long gysid = ((IDropDownModel)getGongmdwModel()).getBeanId(mdrsl.getString("gongysb_id"));
				long mkid = ((IDropDownModel)getMeikdwModel()).getBeanId(mdrsl.getString("meikxxb_id"));
				sbsql.append("update meitxspjb set ")
				.append("riq= to_date('").append(mdrsl.getString("riq")).append("', 'yyyy-mm-dd'), ")
				.append("gongysb_id = ").append(gysid)
				.append(", ")
				.append("meikxxb_id = ").append(mkid)
				.append(", bianh = '").append(mdrsl.getString("bianh")).append("', piaoh = '").append(mdrsl.getString("piaoh"))
				.append("', cheh = '").append(mdrsl.getString("cheh"))
				.append("', shul = ").append(getSqlValue(mdrsl.getString("shul")))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select pj.id,pj.riq, gys.mingc as gongysb_id, mk.mingc meikxxb_id, pj.bianh, pj.piaoh, pj.cheh, pj.shul\n" +
			"  from meitxspjb pj, meikxxb mk, gongysb gys\n" + 
			" where pj.meikxxb_id = mk.id\n" + 
			"   and pj.gongysb_id = gys.id\n" +
			"   and pj.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and pj.meikxxb_id = "+ getMeikdwValue().getStrId() +"\n" + 
			"   and pj.gongysb_id = "+ getGongmdwValue().getStrId() +"\n" + 
			"   order by pj.bianh, pj.id";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setDefaultValue(this.getRiq());
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		//egu.getColumn("gongysb_id").setDefaultValue(getGongmdwValue().getValue());
//		ComboBox cbox = new ComboBox();
//		egu.getColumn("gongysb_id").setEditor(cbox);
//		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from gongysb  where leix=1 order by mingc"));
		//egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		//egu.getColumn("meikxxb_id").setDefaultValue(getMeikdwValue().getValue());
//		ComboBox com = new ComboBox();
//		egu.getColumn("meikxxb_id").setEditor(com);
//		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from meikxxb order by mingc"));
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("bianh").setHeader("���");
		egu.getColumn("bianh").editor.setListeners("specialkey:function(own,e){if(e.getKey() == e.ENTER){gridDiv_grid.startEditing(row, 6);}}");
		egu.getColumn("piaoh").setHeader("Ʊ��");
		egu.getColumn("piaoh").editor.setListeners("specialkey:function(own,e){if(e.getKey() == e.ENTER){gridDiv_grid.startEditing(row, 7);}}");
		egu.getColumn("cheh").setHeader("����");
		egu.getColumn("cheh").editor.setListeners("specialkey:function(own,e){if(e.getKey() == e.ENTER){gridDiv_grid.startEditing(row, 8);}}");
		egu.getColumn("shul").setHeader("����");
		String listeners = 
			"specialkey:function(own,e){\n" +
			"    if(e.getKey() == e.ENTER){\n" + 
			"        if(row+1 == gridDiv_grid.getStore().getCount()){\n" + 
			"            Ext.MessageBox.alert('��ʾ��Ϣ','�ѵ�������ĩβ��');\n" + 
			"            return;\n" + 
			"        }\n" + 
			"        row = row+1;\n" + 
			"        last = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"        gridDiv_grid.getSelectionModel().selectRow(row);\n" + 
			"        cur = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"        copylastrec(last,cur);\n" + 
			"        gridDiv_grid.startEditing(row, 6);\n" + 
			"    }\n" + 
			"}";
		egu.getColumn("shul").editor.setListeners(listeners);
		
		egu.addTbarText("���ڣ�");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("��Ӧ�̣�");
		ComboBox cb = new ComboBox();
		cb.setWidth(120);
		cb.setListWidth(150);
		cb.setTransform("GONGMDW");
		cb.setId("GONGMDW");
		cb.setLazyRender(true);
		cb.setEditable(false);
		egu.addToolbarItem(cb.getScript());
		egu.addOtherScript("GONGMDW.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("ú��λ��");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setListWidth(150);
		comb.setTransform("Meikdw");
		comb.setId("Meikdw");
		comb.setLazyRender(true);
		comb.setEditable(false);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meikdw.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		String handler = 
			"if (GONGMDW.getValue() == -1) {\n" +
			"    Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ��һ����Ӧ��');\n" + 
			"    return;\n" + 
			"}" + 
			"if (Meikdw.getValue() == -1) {\n" +
			"    Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ��һ��ú��λ');\n" + 
			"    return;\n" + 
			"}";
		egu.addToolbarButton(GridButton.ButtonType_Inserts_condition, null, handler);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;});");
		
		
		
		
		
		egu
		.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+ "gongysTree_window.show();}});");
		
		
//		��Ӧ����
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler1 = new StringBuffer();
		handler1.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("if(cks.getDepth() < 2){ \n")
		.append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ӧ��ú��λ��');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.text); \n")
		.append("rec.set('MEIKXXB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler1.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		
//		�����滻
		egu.addTbarText("-");
		egu.addTbarText("-");
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("�����滻");
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){\n" +
				
				" if(SelectLike.checked) { \n" +
				
				"for(var i=e.row;i<gridDiv_ds.getCount();i++){\n" +
				"var rec=gridDiv_ds.getAt(i);\n" +
				" rec.set(e.field+'',e.value);\n" +
				"}\n" +
				
				"}\n" +
				
				"" +
				"  }); ");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * �����ҳ����ȡ����ֵΪNull���ǿմ�����ô�����ݿⱣ���ֶε�Ĭ��ֵ
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
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
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // ú��λ������
			visit.setDropDownBean3(null);
			
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
		}
		getSelectData();
	}
}