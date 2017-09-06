package com.zhiren.dc.huaygl.rulhy;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.grid.CheckColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qywzsz extends BasePage implements PageValidateListener {
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
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while(drsl.next()){
			
			sql.append("delete from ").append("xitxxb").append(" where id =").append(drsl.getString(0)).append(";\n");
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());		
		while (rsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(rsl.getString("ID"))) {
				sql.append("insert into ").append("xitxxb").append("(id");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					if(rsl.getString(i).equals("true")){
						rsl.setString(i, "1");
					}else if(rsl.getString(i).equals("false")||rsl.getString(i).equals("")){
						rsl.setString(i, "0");
					}
					sql.append(",").append(rsl.getColumnNames()[i]);
					sql2.append(",").append(
							visit.getExtGrid1().getValueSql(
									visit.getExtGrid1().getColumn(
											rsl.getColumnNames()[i]),
									rsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append("xitxxb").append(" set ");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					if(rsl.getString(i).equals("true")){
						rsl.setString(i, "1");
					}else if(rsl.getString(i).equals("false")){
						rsl.setString(i, "0");
					}
					sql.append(rsl.getColumnNames()[i]).append(" = ");
					sql.append(
							visit.getExtGrid1().getValueSql(
									visit.getExtGrid1().getColumn(
											rsl.getColumnNames()[i]),
									rsl.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(rsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		// visit.getExtGrid1().Save(getChange(), visit);
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
		// long i=visit.getDiancxxb_id();
		// String st=
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id,xuh,diancxxb_id,mingc,zhi,leib,decode(zhuangt,0,'',1) as zhuangt from xitxxb where leib='��¯úȡúλ' order by xuh");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("xitxxb");
		// /������ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("mingc").setHeader("ȡúλ");
		egu.getColumn("zhi").setHidden(true);
		egu.getColumn("zhi").setDefaultValue("0");
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("leib").setEditor(null);
		// /����������Ĭ��ֵ
		egu.getColumn("leib").setDefaultValue("��¯úȡúλ");
		egu.getColumn("leib").setHidden(true);

		CheckColumn cc = new CheckColumn();
		cc.setId("zt");
		cc.setHeader("״̬");
		cc.setWidth(40);
		cc.setDataIndex("zhuangt");
		egu.getColumn("zhuangt").setId("zt");
		egu.getColumn("zhuangt").setDefaultValue("");
		egu.getColumn("zhuangt").setUpdate(true);
		egu.getColumn("zhuangt").setColtype(GridColumn.ColType_default);
		egu.getColumn("zhuangt").setDataindex("zhuangt");
		egu.getColumn("zhuangt").setDataType(GridColumn.DataType_Float);
		egu.setCheckPlugins(cc);

		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		// /���ð�ť
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
}
