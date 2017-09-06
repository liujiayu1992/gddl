package com.zhiren.dc.zhuangh.caiygl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

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

/*����:���ܱ�
 *����:2010-7-3 12:01:40
 *����:���ӵ糧��
 * 
 * 
 */


public class Duowgl_zh extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		String sql = "begin \n";
		while(rs.next()){
			sql += "delete duowglb  where id = "+ rs.getString("id")+";\n";
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getDelete(sql);
		}
		rs.close();
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		Visit visit=(Visit)getPage().getVisit();
		while(rs.next()){
			if(rs.getInt("id") == 0){
				
				sql += "insert into duowglb(id,diancxxb_id,mingc_old,mingc_new,beiz) values(getnewId("
					+ v.getDiancxxb_id() + "),"+getTreeid() + ",'" 
					+ rs.getString("mingc_old") +"','"+
					rs.getString("mingc_new") +"',"
					+ "'" + rs.getString("beiz") +"'" +");\n";
			}else{
				sql += "update duowglb set mingc_old='" 
					+ rs.getString("mingc_old")
				+ "' , mingc_new='" + rs.getString("mingc_new")
				+ "' , beiz='"+ rs.getString("beiz")
				+ "' where id = "+ rs.getString("id")+";\n";
			}
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getInsert(sql);
		}
		rs.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 

			"select g.id,g.mingc_old,g.mingc_new,g.beiz from duowglb g where " +
			"g.diancxxb_id="+getTreeid();


		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		egu.setTableName("duowglb");
		egu.getColumn("beiz").setHeader("��ע");
//		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
//		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
//		egu.getColumn("tongjkj").setHeader("ͳ�ƿھ�");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
//		egu.getColumn("diancxxb_id").setDefaultValue(v.getDiancxxb_id()+"");
//		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
//		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
//						new IDropDownModel(SysConstant.SQL_Kouj));
//		egu.getColumn("jihkjb_id").editor.allowBlank = true;
		egu.getColumn("mingc_old").setHeader("��һ��ú�ѱ��");
		egu.getColumn("mingc_new").setHeader("��һ��ú�ѱ��");
		egu.getColumn("mingc_old").setEditor(new ComboBox());
		egu.getColumn("mingc_old").setComboEditor(egu.gridId,
				new IDropDownModel(" select id,mingc from duow "));
//		egu.getColumn("yunsfsb_id").editor.allowBlank = true;
		egu.getColumn("mingc_new").setEditor(new ComboBox());
		egu.getColumn("mingc_new").setComboEditor(egu.gridId,
						new IDropDownModel(
								" select id,mingc from duow "
										));
		/*
		 * huochaoyuan 2008-12-21
		 * �޸��ϱ�ȡ��sql��mk.quanc��Ϊmk.mingc,ȡȫ��������ɻ�������ҳ��ȡ��ʱҲѡ��mingc,�������Ϊ���ֶ�
		 */
//		List kj = new ArrayList();
//		kj.add(new IDropDownBean(1,"����"));
//		kj.add(new IDropDownBean(2,"Ʊ��"));
//		kj.add(new IDropDownBean(3,"ʵ��"));
//		sql = "select i.id,i.mingc from item i, itemsort s\n" +
//			"where i.itemsortid = s.id\n" + 
//			"and s.mingc = '��ú������Ŀ'";
//		egu.getColumn("tongjkj").setEditor(new ComboBox());
//		egu.getColumn("tongjkj").setComboEditor(egu.gridId, new IDropDownModel(sql));
//		egu.getColumn("tongjkj").setReturnId(false);
//		egu.getColumn("lurry").setHeader("��Ա");
//		egu.getColumn("lurry").setEditor(null);
//		egu.getColumn("lurry").setDefaultValue(v.getRenymc());
		
//		�Ƿ���ʾѡ��糧��
		if (v.isFencb()) {
			egu.addTbarText("��λ����:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			egu.addTbarText("-");
		}
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
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

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid("");
			
		}
		init();
	}
	
	private void init() {
		setExtGrid(null);
		setTree(null);
		getSelectData();
	}
}
