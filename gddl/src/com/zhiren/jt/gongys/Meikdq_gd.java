package com.zhiren.jt.gongys;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2012-06-20
 * ����������ҳ���߼����ڵ�BUG
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-16
 * ����������ú�����ʱ,	����ʱͬʱ��������Ӧ�̱���
 * 		 ����������֤�����Ƿ�С��6λ�����С�����ܱ���
 */


public class Meikdq_gd extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	private String id;  //��¼ǰ̨���ݹ�����id
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = MainGlobal.getExtMessageBox(saveMsg, false);;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setSaveMsg("");
	}
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	ʡ��IDropDownModel
	public IPropertySelectionModel getShengfModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setShengfModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setShengfModels() {
		String sql = "select sf.id, sf.quanc from shengfb sf";
		setShengfModel(new IDropDownModel(sql));
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		SaveMsg = "";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next())	{

//			��ʼ������ֵ����ʱ�������
			String meikdq_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			String xuh=mdrsl.getString("XUH");
			String BIANM=mdrsl.getString("BIANM");
			String MINGC=mdrsl.getString("MINGC");
			String QUANC=mdrsl.getString("QUANC");
			long SHENGFB_ID =((IDropDownModel)getShengfModel()).getBeanId(mdrsl.getString("SHENGFB_ID"));
			if(BIANM.length()<6){
				SaveMsg += "----------��¼---------<br>--����:"
					+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
					+ "<br>�ļ�¼����С��6λ,���ܱ���!";
				continue;
			}
			String sql_check = "select id from meikdqb where (1=0 or bianm='"+BIANM+"' or mingc='"+MINGC+"' OR QUANC='"+QUANC+"')";

			if ("0".equals(mdrsl.getString("ID"))) {
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------��¼---------<br>--����:"
						+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
						+ "<br>�ļ�¼���ظ�,���ܱ���!";
					continue;
				}
				sql.append("begin \n");
//				���뵽��Ӧ�̱���
				sql.append("INSERT INTO MEIKDQB\n" );
				sql.append("  (ID, XUH, MINGC, QUANC, BIANM,SHENGFB_ID,ZHUANGT)\n");
				sql.append("VALUES\n");
				sql.append("  ("+meikdq_id+","+xuh+",'"+MINGC+"','"+QUANC+"','"+BIANM+"',"+SHENGFB_ID+",1);\n");
				sql.append("INSERT INTO GONGYSB\n" );
				sql.append("  (ID, FUID, MINGC, QUANC, BIANM, SHENGFB_ID, BEIZ, LEIX, ZHUANGT)\n" );
				sql.append("VALUES\n" );
				sql.append(" ("+meikdq_id+", "+meikdq_id+", '"+MINGC+"', '"+QUANC+"', '"+BIANM+"', "+SHENGFB_ID+", 'ԭ����', 0, 0);\n" );
				sql.append("end;" );
			}else{
				sql_check+="and id<>"+mdrsl.getString("ID");
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------��¼---------<br>--����:"
						+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
						+ "<br>�ļ�¼���ظ�,���ܱ���!";
					continue;
				}
				sql.append("begin \n");
//				����ú����Ϣ��
				sql.append("UPDATE MEIKDQB\n" );
				sql.append("   SET XUH        = '"+xuh+"',\n" );
				sql.append("       BIANM      = '"+BIANM+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+"\n" );
				sql.append("    WHERE ID="+mdrsl.getString("ID")+";\n");
				
				sql.append("UPDATE GONGYSB\n" );
				sql.append("   SET BIANM      = '"+BIANM+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+"\n" );
				sql.append("    WHERE ID="+mdrsl.getString("ID")+";\n");
				sql.append("end;" );
			}
			int flag=con.getUpdate(sql.toString());
			if(flag==-1){
				SaveMsg += "----------��¼---------<br>--����:"
					+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
					+ "<br>����ʧ��!";
			}
		}
		
		if(SaveMsg.length()>10){
			setSaveMsg(SaveMsg);
		}else{
			setSaveMsg("����ɹ�");
		}
	}

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
				return value == null || "".equals(value) ? "null" : value;
			}
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	private boolean _BeginChick=false;
	
	public void BeginButtonQY(IRequestCycle cycle){
		_BeginChick = true;
	}
	
	private boolean _StopChick = false;
	
	public void StopButtonTY(IRequestCycle cycle){
		_StopChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
//	�������ú�����
	private boolean _GuanlChick = false;
	
    public void GuanlButton(IRequestCycle cycle) {
    	_GuanlChick = true;
    }
    
//  �п��ܴӷ��ذ�ť���ر�ҳ�棬Ҳ�����Ǵ������ѡ��ť���ر�ҳ�棬�������
	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}
	
//	���������ҳ�洫��������ѡ���ú�������Ϣ
    private String DataSource;
	
	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}
	
	public void submit(IRequestCycle cycle) {
		ToAddMsg="";
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
		}
		if(_BeginChick){
			_BeginChick = false;
			Begin();
			getSelectData();
		}
		if(_StopChick){
			_StopChick = false;
			Stop();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_GuanlChick){
			_GuanlChick = false;
			cycle.activate("Meikdq_gdTj");
		}
	}

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str1 ="";
		if(getShiyztValue().getId() == 1){
			str1="and m.zhuangt = 1 \n";
		}else{
			str1="and m.zhuangt = 0 \n";
		}
		
		String sql = 
			"SELECT M.ID, M.XUH, M.MINGC, M.QUANC, M.BIANM, S.QUANC SHENGFB_ID\n" +
			"  FROM MEIKDQB M, SHENGFB S\n" + 
			" WHERE S.ID = M.SHENGFB_ID \n"+
			str1 +
			" ORDER BY M.XUH";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikdqb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(80);
		
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(150);

		
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(95);
		egu.getColumn("bianm").editor.allowBlank = false;

		egu.getColumn("SHENGFB_ID").setHeader("ʡ��");
		egu.getColumn("SHENGFB_ID").setWidth(80);
		egu.getColumn("SHENGFB_ID").editor.setAllowBlank(false);
		
//		Ĭ��ÿҳ��ʾ25��
		egu.addPaging(25);

		egu.addTbarText("ʹ��״̬:");
		ComboBox cb = new ComboBox();
		cb.setTransform("SHIYZT");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(cb.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��",
		"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addTbarText("-");
		String str = MainGlobal.getXitxx_item("ϵͳ��Ϣ", "�Ƿ���ʾú���������Ӱ�ť", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");

		String shengf_click = 
			"gridDiv_grid.on('cellclick',\n" +
			"function(own, irow, icol, e){\n" + 
			"    row = irow;\n" + 
			"    if('SHENGFB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){\n" + 
			"        shengfTree_window.show();\n" + 
			"    }\n" + 
			"});";		
		
		if (str.equals("��")) {
			egu.setGridType(ExtGridUtil.Gridstyle_Read);
		    egu.addToolbarItem("{"+new GridButton("�������ú�����","function(){document.getElementById('GuanlButton').click();}").getScript()+"}");
		} else {
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addOtherScript(shengf_click);
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		}
		
		if(getShiyztValue().getId() == 0){
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("����","function(){\n"+
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����¼');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.BeginButtonQY.click();}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ���¼');\n"+
						"}}"));
		}else{
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("ͣ��","function(){" +
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����¼');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.StopButtonTY.click();}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ���¼');\n"+
						"	}}"));
		}

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		

		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");

		DefaultTree dt = new DefaultTree(DefaultTree.tree_sf_cs, "shengfTree", ""+visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		String handler = 
			"function() {\n" +
			"    var cks = shengfTree_treePanel.getSelectionModel().getSelectedNode();\n" + 
			"    if(cks==null){\n" + 
			"        shengfTree_window.hide();\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    rec = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"    if(cks.getDepth() == 2){\n" + 
			"        rec.set('SHENGFB_ID', cks.parentNode.text);\n" + 
			"    }else if(cks.getDepth() == 1){\n" + 
			"        rec.set('SHENGFB_ID', cks.text);\n" +
			"    }\n" + 
			"    shengfTree_window.hide();\n" + 
			"    return;\n" + 
			"}";
		
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);

		
//		�������ѡ��ť�����������ʾ��Ϣ
		if(ToAddMsg.equals("toAdd")){
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("\nvar p=new gridDiv_plant("+ recs[i] +");\ngridDiv_ds.insert("+ i +",p);");
				sb.append("\n").append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
//		System.out.print(((Visit)this.getPage().getVisit()).getDefaultTree().getScript());
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			setShengfModel(null);
			setShengfModels();
			setShiyztValue(null);
			setShiyztModel(null);
			ToAddMsg=cycle.getRequestContext().getRequest().getParameter("MsgAdd");
			
			if(ToAddMsg==null){
				ToAddMsg="";
			}
			DataSource = visit.getString12();
			getSelectData();
		} else {
			getSelectData();
		}
	}
		
		//ʹ��������
		public IDropDownBean getShiyztValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getShiyztModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setShiyztValue(IDropDownBean value) {

			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}

		public void setShiyztModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getShiyztModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getShiyztModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public IPropertySelectionModel getShiyztModels() {
			List shiyzt = new ArrayList();
			shiyzt.add(new IDropDownBean(1, "ʹ����"));
			shiyzt.add(new IDropDownBean(0, "δʹ��"));
			((Visit) getPage().getVisit())
					.setProSelectionModel5(new IDropDownModel(shiyzt));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}
		
	//���õķ���	
		public void Begin(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "update MEIKDQB set zhuangt = 1 where id ="+getChange();
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("���óɹ�!");
				}
				else{
					setSaveMsg("����ʧ��!");
				}
		}
	//ͣ�õķ���	
		public void Stop(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql =  "update MEIKDQB set zhuangt = 0 where id ="+getChange();
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("ͣ�óɹ�!");
				}
				else{
					setSaveMsg("ͣ��ʧ��!");
				}
		}
		
	}

