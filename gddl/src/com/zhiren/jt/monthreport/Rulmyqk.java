package com.zhiren.jt.monthreport;

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
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rulmyqk extends BasePage implements PageValidateListener {
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if (flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}
	
	private void GotoRucmtqk(IRequestCycle cycle){
		cycle.activate("Rucmtqk");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnButton = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		
		if (_ReturnButton) {
			_ReturnButton = false;
			GotoRucmtqk(cycle);
		}
	}
	

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		// -----------------------------------
		
		String sql = 

			"select r.id,\n" +
			"       r.diancxxb_id,\n" + 
			"       r.riq,\n" + 
			"       r.fenx,\n" + 
			"       r.rez,\n" + 
			"       r.laiy,\n" + 
			"       r.haoy,\n" + 
			"       r.youkc\n" + 
			"from rucmtqkzb r\n" + 
			"where r.diancxxb_id = "+visit.getString3()+"\n" + 
			"      and r.riq = to_date('"+visit.getString4()+"','yyyy-MM-dd')\n" + 
			"order by decode(r.fenx,'����',1,'����',2,'ͬ��',3,'��ֵ',4,5)";

		rsl = con.getResultSetList(sql);	
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rucmtqkzb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("rez").setHeader("��¯��ֵ");
		egu.getColumn("laiy").setHeader("����");
		egu.getColumn("haoy").setHeader("����");
		egu.getColumn("youkc").setHeader("�Ϳ��");

		// �趨�г�ʼ���
//		egu.getColumn("riq").setWidth(80);
//		egu.getColumn("rez").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(30);// ���÷�ҳ

		// *****************************************����Ĭ��ֵ****************************
		
//		// �������ڵ�Ĭ��ֵ,
//		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// ********************������************************************************
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);
				
		StringBuffer ret = new StringBuffer();
		ret.append("function(){").append("document.getElementById('ReturnButton').click();}");
		GridButton fh = new GridButton("����", ret.toString());
		fh.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(fh);
		
//		 ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		
		
		
		String sb_str=
			"gridDiv_grid.on('beforeedit',function(e){" +
			" 	if(e.row==3){ e.cancel=true;}" +//��4�в�ֵ�в�����༭
			"});" +
		
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"   if(e.row==1||e.row==2){\n" +
			"		var rec1=gridDiv_ds.getAt(1);\n" +
			"		var rec2=gridDiv_ds.getAt(2);\n" +
			"		var rec3=gridDiv_ds.getAt(3);\n" +
			"		rec3.set(e.field,rec1.get(e.field)-rec2.get(e.field));\n" +
			"	}\n" +
			"});\n";

		StringBuffer sb = new StringBuffer(sb_str);
		
		egu.addOtherScript(sb.toString());
		// ---------------ҳ��js�������--------------------------
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}
		getSelectData(null);

	}


//	// �õ���½�û����ڵ糧���߷ֹ�˾������
//	public String getDiancmc() {
//		String diancmc = "";
//		JDBCcon cn = new JDBCcon();
//		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
//		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
//				+ diancid;
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				diancmc = rs.getString("quanc");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		} finally {
//			cn.Close();
//		}
//
//		return diancmc;
//
//	}
//
//	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
//	public String getIDropDownDiancmc(String diancmcId) {
//		if (diancmcId == null || diancmcId.equals("")) {
//			diancmcId = "1";
//		}
//		String IDropDownDiancmc = "";
//		JDBCcon cn = new JDBCcon();
//
//		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
//				+ diancmcId;
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				IDropDownDiancmc = rs.getString("mingc");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		} finally {
//			cn.Close();
//		}
//
//		return IDropDownDiancmc;
//
//	}
//
//	private String treeid;
//
//	/*
//	 * public String getTreeid() { return treeid; } public void setTreeid(String
//	 * treeid) { this.treeid = treeid; }
//	 */
//	public String getTreeid() {
//		String treeid = ((Visit) getPage().getVisit()).getString2();
//		if (treeid == null || treeid.equals("")) {
//			((Visit) getPage().getVisit()).setString2(String
//					.valueOf(((Visit) this.getPage().getVisit())
//							.getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString2();
//	}
//	public boolean dctree = false;
//	public void setTreeid(String treeid) {
//		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
//			dctree = true;
//		}
//		((Visit) getPage().getVisit()).setString2(treeid);
//	}
//
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//
//	public String getTreeScript() {
//		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
//	}
//	
//	public String getTreedcScript() {
//		return getTree().getWindowTreeScript();
//	}
//
//	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
//	public int getDiancTreeJib() {
//		JDBCcon con = new JDBCcon();
//		int jib = -1;
//		String DiancTreeJib = this.getTreeid();
//		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
//			DiancTreeJib = "0";
//		}
//		String sqlJib = "select d.jib from diancxxb d where d.id="
//				+ DiancTreeJib;
//		ResultSet rs = con.getResultSet(sqlJib.toString());
//
//		try {
//			while (rs.next()) {
//				jib = rs.getInt("jib");
//			}
//			rs.close();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		} finally {
//			con.Close();
//		}
//
//		return jib;
//	}
//	
}
