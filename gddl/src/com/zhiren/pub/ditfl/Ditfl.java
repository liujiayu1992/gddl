package com.zhiren.pub.ditfl;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ditfl extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// 页面变化记录
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
	
	public void Save(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(getExtGrid().tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append(" insert into ").append(getExtGrid().tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
						
						sql2.append(",").append(MainGlobal.getProperId(getGongysModel(), getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
								mdrsl.getString(i)).replace('\'',' ').trim()));
					}else{
						
						sql2.append(",").append(
								getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append(" update ").append(getExtGrid().tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
						
						sql.append(
									MainGlobal.getProperId(getGongysModel(), getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
								mdrsl.getString(i)).replace('\'',' ').trim())).append(",");
						
					}else{
						
						sql.append(
								getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("保存成功！");
		}
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb=new StringBuffer();
		
		long Diancxxb_id=visit.getDiancxxb_id();
		
		if(visit.isFencb()){
			
			Diancxxb_id=getChangbValue().getId();
		}
		
		sb.append("select nvl(ditflb.id,0) as id,gongysb.mingc as gongysb_id,decode(ditflb.diancxxb_id,null,"+Diancxxb_id+",ditflb.diancxxb_id) as diancxxb_id,		\n");
		sb.append("		fz.mingc as faz_id,dz.mingc as daoz_id,nvl(lic,0) as lic,nvl(feil,0) as feil,nvl(shuil,0) as shuil,ditflb.beiz								\n");
		sb.append("		from ditflb,gongysb,chezxxb fz,chezxxb dz																									\n");
		sb.append("		where ditflb.gongysb_id(+)=gongysb.id and ditflb.faz_id=fz.id(+) 																			\n");
		sb.append("				and ditflb.daoz_id=dz.id(+)	and (ditflb.diancxxb_id="+Diancxxb_id+"	or ditflb.diancxxb_id is null)									\n");
		sb.append("		order by gongysb_id	");
		
		ResultSetList rsl = con
				.getResultSetList(sb.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("ditflb");
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("lic").setHeader("里程");
		egu.getColumn("feil").setHeader("费率");
		egu.getColumn("shuil").setHeader("税率");
		egu.getColumn("beiz").setHeader("备注");
		
//		设定是否可编辑
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("daoz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from chezxxb order by mingc"));
		egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from chezxxb order by mingc"));
		egu.getColumn("faz_id").setReturnId(true);
		egu.getColumn("daoz_id").setReturnId(true);
		
//		设定显示不显示
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		
//		设定小数位
		((NumberField)egu.getColumn("lic").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("feil").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("shuil").editor).setDecimalPrecision(2);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		if(visit.isFencb()){
			
			egu.addTbarText(Locale.diancxxb_id_fahb);
			ComboBox DiancSelect = new ComboBox();
			DiancSelect.setId("Dianc");
			DiancSelect.setWidth(80);
			DiancSelect.setLazyRender(true);
			DiancSelect.setTransform("ChangbSelect");
			egu.addToolbarItem(DiancSelect.getScript());
			egu.addOtherScript("Dianc.on('select',function(){ document.forms[0].submit();});	\n ");
		}
		
		egu.getColumn("daoz_id").setDefaultValue(MainGlobal.getDiancDefaultDaoz(Diancxxb_id));
		egu.getColumn("shuil").setDefaultValue("0.07");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
//		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
//        "var end = url.indexOf(';');"+
//                 "url = url.substring(0,end);"+
//   	    "url = url + '?service=page/' + 'Meizreport&lx=rezc';" +
//   	    " window.open(url,'newWin');";
//		egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");
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
	
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id()+" order by mingc");
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select id,mingc as gongys from gongysb order by mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setGongysModel(null);	//ProSelectionModel1
			setChangbValue(null);	//DropDownBean2()
			setChangbModel(null);	//ProSelectionModel2
			getGongysModels();
			getChangbModels();
			getSelectData();
		}
	}
}
