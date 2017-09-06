package com.zhiren.jt.dtsx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
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
/**
 * @author huochaoyuan
 * �����ֹ�˾�ձ�������
 *�½�ϵͳ����ġ��糧���ơ�(Zhillr_sb)
 *����diancxxb��beiz�ֶΣ�¼������ʱ���ַ�������һ�����ơ����������
 *�ڶ������ơ��պĴ�ά���������������ơ�����������ֵ糧���ƣ�
 *�����趨����֮ǰ��ҳ�潫����ʾ�����桱��ť����ʵ�ַֹ�˾��Ա���Ƶ糧�޸�����Ȩ�ޣ�
 */
public class Dianc_kz extends BasePage implements PageValidateListener {
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
		Save1(getChange(), visit);
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = getExtGrid();
		try {

			StringBuffer sql = new StringBuffer("begin \n");
			String tableName = "diancxxb";
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while (mdrsl.next()) {
					sql.append("update ").append(tableName).append(" set ");
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {
						if	(mdrsl.getColumnNames()[i].equals("RIQ1")){	
						sql.append(" beiz ='");
								sql.append(mdrsl.getString("RIQ1"));
								sql.append(";");
								sql.append(mdrsl.getString("RIQ2"));
								sql.append(";");
								sql.append(mdrsl.getString("RIQ3")+"'\n");
								sql.append("where id="+mdrsl.getString("ID")+";\n");	
						}
					}	
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
			con.Close();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		getSelectData();
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
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
		String riq = DateUtil.FormatDate(new Date());
		String sqll="select dc.id as id,dc.mingc as mingc,\n" +
		"to_date(decode(substr(dc.beiz,1,10),null,'"+riq+"',substr(dc.beiz,1,10)),'yyyy-mm-dd') as riq1, \n" +
		"to_date(decode(substr(dc.beiz,12,10),null,'"+riq+"',substr(dc.beiz,12,10)),'yyyy-mm-dd') as riq2,\n "+
		"to_date(decode(substr(dc.beiz,23,10),null,'"+riq+"',substr(dc.beiz,23,10)),'yyyy-mm-dd') as riq3\n "+
				"from diancxxb dc\n" +
		"where  dc.id is not null "+str;
		ResultSetList rsl = con.getResultSetList(sqll);
						
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("diancxxb");
		egu.setWidth(990);
		egu.getColumn("id").setHeader("���");
		egu.getColumn("id").setWidth(50);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("��λ����");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").setUpdate(false);
		egu.getColumn("riq1").setHeader("����ʱ��");
		egu.getColumn("riq2").setHeader("�������ʱ��");
		egu.getColumn("riq3").setHeader("����ʱ��");
		// egu.getColumn("diancxxb_id").setHidden(true);
		// egu.getColumn("diancxxb_id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		// egu.getColumn("toucrq").setEditor(new DateField());
		//��λ����������
		//�糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			egu.getColumn("id").setEditor(new ComboBox());
			egu.getColumn("id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("id").setReturnId(true);
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("id").setEditor(new ComboBox());
			egu.getColumn("id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
			egu.getColumn("id").setReturnId(true);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			egu.getColumn("id").setEditor(new ComboBox());
			egu.getColumn("id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("id").setHidden(true);
			egu.getColumn("id").setDefaultValue(mingc);
			
		}		

		// �糧��
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
//		egu.getColumn("diancxxb_id").setDefaultValue(
//				"" +  visit.getDiancxxb_id());
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		String s = " var url = 'http://'+document.location.host+document.location.pathname;"
//				+ "var end = url.indexOf(';');"
//				+ "url = url.substring(0,end);"
//				+ "url = url + '?service=page/' + 'Jizreport&lx=rezc';"
//				+ " window.open(url,'newWin');";
//		egu.addToolbarItem("{"
//				+ new GridButton("��ӡ", "function (){" + s + "}").getScript()
//				+ "}");
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