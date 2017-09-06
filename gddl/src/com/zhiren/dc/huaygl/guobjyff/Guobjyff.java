package com.zhiren.dc.huaygl.guobjyff;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;

public class Guobjyff extends BasePage implements PageValidateListener {
	public List gridColumns;
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
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		ExtGridUtil egu=getExtGrid();
		JDBCcon con = new JDBCcon();
		try {
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while (mdrsl.next()) {
				StringBuffer sb = new StringBuffer("begin \n");
				if (mdrsl.getString("id").equals("0")) {
						sb.append("insert into guobb(id,xuh,mingc,xiangmmc) values");
						sb.append("(getnewid(" + visit.getDiancxxb_id() + "),"
								+ mdrsl.getString("xuh") + ",'"
								+ mdrsl.getString("mingc") + "','"
								+ mdrsl.getString("xiangmmc") + "');")
								.append("\n");
				} else {
					sb.append("update guobb set xuh = "
							+ mdrsl.getString("xuh") + ",mingc='"
							+ mdrsl.getString("mingc") + "',xiangmmc='"
							+ mdrsl.getString("xiangmmc") + "'");
					sb.append(" where id = " + mdrsl.getString("id") + ";\n");
				}
				sb.append("end;");
				con.getUpdate(sb.toString());
				con.Close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
	
		JDBCcon con = new JDBCcon();
		sql="select id,xuh,mingc,xiangmmc from guobb order by xuh";
			
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("guobb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("mingc").setHeader("�����׼");
		egu.getColumn("xiangmmc").setHeader("������Ŀ����");
		
//		List shifsy1 = new ArrayList();
//		shifsy1.add(new IDropDownBean(0, "ȫˮ��"));
//		shifsy1.add(new IDropDownBean(1, "���������ˮ��"));
//		shifsy1.add(new IDropDownBean(2, "����������ҷ�"));
//		shifsy1.add(new IDropDownBean(3, "�յ����ҷ�"));
//		shifsy1.add(new IDropDownBean(4, "������ҷ�"));
//		shifsy1.add(new IDropDownBean(5, "����������ӷ���"));
//		shifsy1.add(new IDropDownBean(6, "�����޻һ��ӷ���"));
//		shifsy1.add(new IDropDownBean(7, "���������ȫ��"));
//		shifsy1.add(new IDropDownBean(8, "�����ȫ��"));
//		shifsy1.add(new IDropDownBean(9, "�յ���ȫ��"));
//		shifsy1.add(new IDropDownBean(10, "�����������"));
//		shifsy1.add(new IDropDownBean(11, "�յ�����"));
//		shifsy1.add(new IDropDownBean(12, "�����������Ͳ��ֵ"));
//		shifsy1.add(new IDropDownBean(13, "�������λ��ֵ"));
//		shifsy1.add(new IDropDownBean(14, "�����������λ��ֵ"));
//		shifsy1.add(new IDropDownBean(15, "�����޻һ���λ��ֵ"));
//		shifsy1.add(new IDropDownBean(16, "�յ�����λ��ֵ"));
//		shifsy1.add(new IDropDownBean(17, "��������"));
//		shifsy1.add(new IDropDownBean(18, "��������"));
		
		egu.getColumn("xiangmmc").setEditor(new ComboBox());
		egu.getColumn("xiangmmc").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from item where itemsortid = (select id from itemsort where bianm = 'GUOBBZ') order by xuh"));
		egu.getColumn("xiangmmc").setDefaultValue("��ѡ��");
		egu.getColumn("xiangmmc").setReturnId(true);
		egu.getColumn("xiangmmc").setWidth(200);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert,null);
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}

}
