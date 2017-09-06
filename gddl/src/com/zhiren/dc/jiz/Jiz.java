package com.zhiren.dc.jiz;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

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

public class Jiz extends BasePage implements PageValidateListener {
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

		ResultSetList rsl = con
				.getResultSetList("SELECT j.ID, j.xuh,dc.mingc as diancxxb_id, j.jizbh, j.jizurl, j.toucrq, j.rijhm, j.meihl, j.jihdl, j.shejmz, j.qnet_ar,\n"
						+ "      j.mt, j.vdaf,j.st_d, j.st, j.aad,j.xiaohmz,j.qnet_ar_xh,j.mt_xh,j.aad_xh,j.vdaf_xh,j.st_d_xh,j.st_xh , j.zhizs, j.beiz \n"
						+ "  FROM jizb j,diancxxb dc\n"
						+ " WHERE j.diancxxb_id = dc.id\n"
						+""+str+" order by j.xuh");
						
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jizb");
		egu.setWidth("bodyWidth");
		
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("diancxxb_id").setHeader("��λ����");
		egu.getColumn("jizbh").setHeader("������");
		egu.getColumn("jizurl").setHeader("��������(MW)");
		egu.getColumn("jizurl").setDefaultValue("0");
		egu.getColumn("toucrq").setHeader("Ͷ������");
		egu.getColumn("rijhm").setHeader("�վ���ú(t)");
		egu.getColumn("rijhm").setDefaultValue("0");
		egu.getColumn("meihl").setHeader("ú����(g/kwh)");
		egu.getColumn("meihl").setDefaultValue("0");
		egu.getColumn("jihdl").setHeader("�ƻ�����(�ڶ�)");
		egu.getColumn("jihdl").setDefaultValue("0");
		egu.getColumn("shejmz").setHeader("���ú��");
		egu.getColumn("qnet_ar").setHeader("��ֵ(MJ/Kg)");
		egu.getColumn("mt").setHeader("ȫˮ��(%)");
		egu.getColumn("vdaf").setHeader("�ӷ���(%)");
		//egu.getColumn("sar").setHeader("Sar(%)");
		egu.getColumn("st_d").setHeader("���(%)");
		egu.getColumn("st").setHeader("���۵�(%)");
		egu.getColumn("aad").setHeader("�ҷ�(%)");
		egu.getColumn("xiaohmz").setHeader("У��ú��");
		egu.getColumn("qnet_ar_xh").setHeader("У����ֵ");
		egu.getColumn("mt_xh").setHeader("У��ȫˮ��");
		egu.getColumn("aad_xh").setHeader("У�˻ҷ�");
		egu.getColumn("vdaf_xh").setHeader("У�˻ӷ���");
		
		egu.getColumn("st_d_xh").setHeader("У�����");
		egu.getColumn("st_xh").setHeader("У�˻��۵�");
		
		
		egu.getColumn("zhizs").setHeader("������");
		egu.getColumn("beiz").setHeader("��ע");
		// egu.getColumn("diancxxb_id").setHidden(true);
		// egu.getColumn("diancxxb_id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		// egu.getColumn("toucrq").setEditor(new DateField());
		//��λ����������
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
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String s = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Jizreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + s + "}").getScript()
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