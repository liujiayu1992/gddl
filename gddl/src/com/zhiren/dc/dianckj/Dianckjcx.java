package com.zhiren.dc.dianckj;

/*
 * ���ڣ�2011-1-5
 * ���ߣ�licj
 * ���÷�Χ: �������
 * �������糧�ھ�ά��ҳ��
         
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import org.jdom.Element;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Dianckjcx extends BasePage implements PageValidateListener {
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
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
		
		if (_RefurbishChick) {
		//	_RefurbishChick = false;
		//	Save();
			getSelectData();
		}
		
		if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
	}
	
	private void Power(IRequestCycle cycle) {
/*		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ���ھ����ƹ����糧!");
			return;
		}
		int treejib = this.getDiancTreeJib();
		if(treejib == 3) {
			setMsg("û�п��Թ����ĵ糧��");
			return;
		}*/
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		visit.setString2(getTreeid());
		cycle.activate("Guanldc");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		String str = "";
		if (visit.isJTUser()) {
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
		if (treejib == 1) {
			str = "and dc.id = " + getTreeid() + "";
		} else if (treejib == 2) {
			str = "and dc.id = " + getTreeid() + "";
		} else if (treejib == 3) {
			str = "and dc.id = " + getTreeid() + "";
		}

		ResultSetList rsl = con
				.getResultSetList("SELECT d.ID, d.xuh, dc.mingc as diancxxb_id, d.mingc as mingc, d.beiz \n"
						+ "  FROM dianckjb d,diancxxb dc\n"
						+ " WHERE d.diancxxb_id = dc.id \n"
						+""+str+" order by d.xuh");
						
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("dianckjb");
		egu.setWidth("bodyWidth");
		
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("diancxxb_id").setHeader("��λ����");
		egu.getColumn("diancxxb_id").setHidden(true);
//		egu.getColumn("diancxxb_id").setDefaultValue();
//		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("mingc").setHeader("�ھ�����");
		//egu.getColumn("fenl_id").setHeader("����");
		//����������
		/*ComboBox cb = new ComboBox();
		egu.getColumn("fenl_id").setEditor(cb);
		cb.setEditable(true);
		
		String Sql = "select i.id,i.mingc from item i,itemsort s where i.itemsortid=s.itemsortid and s.bianm='DCKJFL' order by i.xuh";
		egu.getColumn("fenl_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));*/
		//egu.getColumn("fenl_id").setDefaultValue(visit.getDaoz());
		
		egu.getColumn("beiz").setHeader("��ע");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		//�糧������

		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc order by dc.mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");
			egu.getColumn("diancxxb_id").setReturnId(true);
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
//		    egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			
		}	
		// �糧��
		egu.addTbarText("�糧��:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
//		egu.getColumn("diancxxb_id").setDefaultValue(
//				"" +  visit.getDiancxxb_id());
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", "var Mr = gridDiv_grid.getStore().getRange();  \n" +
        "for(i = 0; i< Mr.length; i++){  \n"+
        "	for(j=i+1; j< Mr.length; j++){  \n"+
        "		if(Mr[i].get('MINGC')==Mr[j].get('MINGC')){  \n"+
        "       	Ext.MessageBox.alert('��ʾ��Ϣ','�ھ�����'+Mr[i].get('MINGC')+'�ظ�!');  \n"+
        "         	return false;"+			
          "       }  \n"+        
        "   }  \n"+  
       "} \n");
		
		egu.addToolbarItem("{"
				+ new GridButton("�����糧", "function (){ " +
						" var grid1_history =\"\";" +
						" if(gridDiv_sm.getSelected()==null){ " +
						"	Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"��ѡ��һ���ھ����ƹ����糧!\");  return; } " +
						" grid1_rcd = gridDiv_sm.getSelected();" +
						" if(grid1_rcd.get(\"ID\") == \"0\"){ " +
						"	Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"�ڹ����糧֮ǰ���ȱ���!\"); return; }" +
						" grid1_history = grid1_rcd.get(\"ID\");" +
						" var Cobj = document.getElementById(\"CHANGE\");" +
						" Cobj.value = grid1_history; " +
						" document.getElementById(\"PowerButton\").click();}").getScript()
				+ "}");
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
			setTreeid(null);
		}
		getSelectData();
	}

	

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid==null||treeid.equals("")) {

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