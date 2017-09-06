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
 * ���ߣ�����
 * ʱ�䣺2010-04-14
 * �������������ܣ�1.�÷�������ƹ���ƶ�
 * 		���÷�Χ����������
 */

public class Feihlzjc extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
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

	//��¼Jizb��ID
	public long getJizb_id() {
		return getJizValue().getId();
	}
	
	// ���ڿؼ�
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
		
//		�Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���",
				visit.getDiancxxb_id() + "", "��"));
		
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
		egu.getColumn("huidxxb_id").setHeader("�Ҷ�");
		// �Ҷ�������
		ComboBox cbx = new ComboBox();
		egu.getColumn("huidxxb_id").setEditor(cbx);
		cbx.setEditable(false);
		sql = "select id, mingc from huidxxb order by xuh";
		egu.getColumn("huidxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("fenxsj").setHidden(true);
		egu.getColumn("fenxsj").setDefaultValue(fenxsj);
		egu.getColumn("quysj").setHeader("ȡ��ʱ��");
		egu.getColumn("quysj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("kerw").setHeader("��ȼ��");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setDefaultValue(fenxsj);
		
		if(isYc){
			egu.getColumn("kerw").editor.setListeners(getStr(0));
			egu.getColumn("beiz").editor.setListeners(getStr(1));
		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

        // ����
		egu.addTbarText("����ʱ��:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		// ����
		egu.addTbarText("����:");
		ComboBox jiz = new ComboBox();
		jiz.setTransform("JIZB_ID");
		jiz.setId("JIZB_ID");
		jiz.setListeners("select:function(){document.Form0.submit();}");
		jiz.setLazyRender(true);
		jiz.setEditable(false);
		jiz.setWidth(110);
		egu.addToolbarItem(jiz.getScript());
		egu.addTbarText("-");
		
		// �糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		//�����滻
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
		
		
		if(isYc){
			egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;col=icol;});");//�õ��С���
		}
		
		setExtGrid(egu);
		con.Close();
	}
	
//	��������ƹ�����JS
	public String getStr(int col){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Str = "";
////		 ���Ƿ�ɱ༭
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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
	
    //����������
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
