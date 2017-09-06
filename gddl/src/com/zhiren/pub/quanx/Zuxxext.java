package com.zhiren.pub.quanx;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zuxxext extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public int getDataColumnCount() {
		int count = 0;
		for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
			if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
				count++;
			}
		}
		return count;
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
		} else {
			return value;
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
    }
	    
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
	}

//	����Ȩ��
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ����������Ȩ��!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Zuqx");
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		String str = "";
		str = "and dc.id = " + getTreeid() + "";
		/*if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {  
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = " and dc.id =" + visit.getDiancxxb_id();
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
*/
		String sql = " select z.* from zuxxb z,diancxxb dc where z.diancxxb_id=dc.id "
			+str+" order by z.mingc";
		
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//ֻ�ܵ�ѡ����
		egu.setTableName("zuxxb");
		//egu.setWidth(990);
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHeader("���");
		egu.getColumn("id").setWidth(50);
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("beiz").setHeader("����");
		egu.getColumn("beiz").setWidth(120);
		egu.getColumn("diancxxb_id").setHeader("�糧ID");
		egu.getColumn("diancxxb_id").setWidth(50);
		
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		// egu.getColumn("toucrq").setEditor(new DateField());
		/*//��λ����������
		//�糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			
		}		*/

		// �糧��
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				visit.getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
//		egu.getColumn("diancxxb_id").setDefaultValue(
//				"" +  visit.getDiancxxb_id());
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		���ɰ�ť
//		GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
		
//		String s = " var url = 'http://'+document.location.host+document.location.pathname;"
//				+ "var end = url.indexOf(';');"
//				+ "url = url.substring(0,end);"
//				+ "url = url + '?service=page/' + 'Jizreport&lx=rezc';"
//				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("ȡ��", "function (){gridDiv_ds.rejectChanges();}").getScript()
				+ "}");
		
		egu.addToolbarItem("{"//document.getElementById(\"ShezButton\").click()
				+ new GridButton("����Ȩ��", "function (){ " +
						" var grid1_history =\"\";" +
						" if(gridDiv_sm.getSelected()==null){ " +
						"	Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"��ѡ��һ����������Ȩ��\");  return; } " +
						" grid1_rcd = gridDiv_sm.getSelected();" +
						" if(grid1_rcd.get(\"ID\") == \"0\"){ " +
						"	Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"������Ȩ��֮ǰ���ȱ���!\"); return; }" +
						" grid1_history = grid1_rcd.get(\"ID\");" +
						" var Cobj = document.getElementById(\"CHANGE\");" +
						" Cobj.value = grid1_history; " +
						" document.getElementById(\"PowerButton\").click();}").getScript()
				+ "}");
		setExtGrid(egu);
		con.Close();
	}
	
	//      
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
			setTreeid("");
		}
		getSelectData();
	}
	

	boolean treechange = false;

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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

}