package com.zhiren.dc.feiylb;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Feiylb extends BasePage implements PageValidateListener {
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
		Save(getChange(), visit);
	}
	public void Save(String strchange,Visit visit) {
		String tableName="feiylbb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1(). getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl =visit.getExtGrid1(). getModifyResultSet(strchange);
		while(mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();
			StringBuffer sql4 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				sql3.append(",diancxxb_id");//�ɱ�
				if(visit.isFencb()==true){
				sql4.append(",").append(getFencbValue().getId());
				}else{
					sql4.append(",").append(visit.getDiancxxb_id());
				}
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
				}
				sql.append(sql3).append(") values(").append(sql2).append(sql4).append(");\n");
			}else {
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
				}
//				for(int i=1;i<mdrsl.getColumnCount();i++) {
//					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
//					if(mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")){
//						if(visit.isFencb()==true){
//							sql.append(getFencbValue().getId()).append(",");
//						}else{
//							sql.append(visit.getDiancxxb_id()).append(",");
//						}
//						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
//					}
//				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
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
		 long changbid=0;
		 if(visit.isFencb()==true){
			 changbid=getFencbValue().getId();
		 }else{
			 changbid=visit.getDiancxxb_id();
		 }
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select f.id,f.mingc,decode(f.leib,1,'��������',2,'��Ʊ�˶�',3,'�ӷ�') as leib from feiylbb f where f.diancxxb_id="+
			changbid+"order by f.mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("feiylbb");
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("leib").setHeader("���");
		//egu.getColumn("mingc").setHeader("����");
		//egu.getColumn("piny").setHeader("ƴ��");
		//egu.getColumn("zhuangt").setHeader("״̬");
		//egu.getColumn("pinzms").setHeader("Ʒ������");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(23);
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "��������"));
		l.add(new IDropDownBean(2, "��Ʊ�˶�"));
		l.add(new IDropDownBean(3, "�ӷ�"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("leib").setDefaultValue("��������");
		egu.getColumn("leib").setReturnId(true);
		
	   if(visit.isFencb()==true){
			egu.addTbarText("����:");
			ComboBox comb4 = new ComboBox();
			comb4.setTransform("FencbDropDown");
			comb4.setId("changb");
			comb4.setEditable(false);
			comb4.setLazyRender(true);// ��̬��
			comb4.setWidth(100);
			comb4.setReadOnly(true);
			egu.addToolbarItem(comb4.getScript());
			egu.addOtherScript("changb.on('select',function(){document.forms[0].submit();});");
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

//	����
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {

		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "��ѡ��"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
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
