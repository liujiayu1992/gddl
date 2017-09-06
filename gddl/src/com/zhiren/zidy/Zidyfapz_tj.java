package com.zhiren.zidy;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zidyfapz_tj extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	按钮的监听事件
	private boolean _NextChick = false;

	public void NextButton(IRequestCycle cycle) {
		_NextChick = true;
	}
	
	private void Next(IRequestCycle cycle){
		Visit v = (Visit) getPage().getVisit();
		String sql = "select * from zidyfapz where zidyfa_id=" + v.getString1();
		String pz_id = "-1";
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			pz_id = rs.getString("id");
		}
		rs.close();
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		String delSql;
		delSql="begin\n";
		while(rs.next()){
//			if(!rs.getString("z_column_cn").equals("")){
//				delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='colname_cn';\n";
//				sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
//					+ pz_id + ",'" + rs.getString("z_column")+ "','colname_cn','"+rs.getString("z_column_cn")+"');\n";
//			}
			if(!rs.getString("cond").equals("")){
				delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='conditions';\n";
				sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
					+ pz_id + ",'" + rs.getString("z_column")+ "','conditions','"+(getExtGrid().getColumn("cond").combo).getBeanId(rs.getString("cond"))+"');\n";
			}
			if(!rs.getString("condop").equals("")){
				delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='conditionOP';\n";
				sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
					+ pz_id + ",'" + rs.getString("z_column")+ "','conditionOP','"+rs.getString("condop")+"');\n";
			}
			if(!rs.getString("param").equals("")){
				delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='parameters';\n";
				sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
					+ pz_id + ",'" + rs.getString("z_column")+ "','parameters','"+rs.getString("param")+"');\n";
			}
			if(!rs.getString("groupnum").equals("")){
				delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='groupNum';\n";
				sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
					+ pz_id + ",'" + rs.getString("z_column")+ "','groupNum','"+rs.getString("groupnum")+"');\n";
			}
		}
		rs.close();
		sql += "end;";
		delSql+="end;";
		con.getDelete(delSql);
		con.getUpdate(sql);
		
		con.Close();
//		完成
		cycle.activate("Zidyfapz");
	}
	
	private boolean _LastChick = false;

	public void LastButton(IRequestCycle cycle) {
		_LastChick = true;
	}
	
	private void Last(IRequestCycle cycle){
		cycle.activate("Zidyfapz_qt");
	}


	public void submit(IRequestCycle cycle) {
		if (_LastChick) {
			_LastChick = false;
			Last(cycle);
		}
		if (_NextChick) {
			_NextChick = false;
			Next(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList(
						"select y.z_column, y.z_column_cn, pz.cond,pz.condop,pz.param,pz.groupnum\n" +
						"from zidyjcsjyms y,\n" + 
						"(select\n" + 
						"    p.z_column,\n" + 
						"    max(decode(p.z_code,'conditions',(select z_paramname from zidybxcs where ''||id = p.z_value),'')) cond,\n" + 
						"    max(decode(p.z_code,'conditionOP',z_value,'')) condop,\n" + 
						"    max(decode(p.z_code,'parameters',z_value,'')) param,\n" + 
						"    max(decode(p.z_code,'groupNum',z_value,'')) groupnum\n" + 
						"from zidypzms p\n" + 
						"     where p.zidyfapz_id = (select id from zidyfapz where zidyfa_id = "+visit.getString1()+")\n" + 
						"group by p.z_column) pz\n" + 
						"where y.zidyjcsjy_id = (select zidyjcsjy_id from zidyfapz where zidyfa_id = "+visit.getString1()+")\n" + 
						"  and y.z_column = pz.z_column(+)\n" + 
						"  order by y.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("dual");
		egu.getColumn("z_column").setHeader("列名");
		egu.getColumn("z_column_cn").setHeader("中文名");
		egu.getColumn("cond").setHeader("条件");
//		egu.getColumn("mingc").setHidden(true);
		
		egu.getColumn("condop").setHeader("条件符号");
		egu.getColumn("param").setHeader("参数");
		egu.getColumn("groupnum").setHeader("分组位置");
		egu.getColumn("groupnum").setDataType("number");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		ComboBox tj = new ComboBox();
		tj.setAllowBlank(true);
		tj.setEditable(true);
		egu.getColumn("cond").setEditor(tj);
		egu.getColumn("cond").setComboEditor(egu.gridId,
				new IDropDownModel("select id,z_paramname from zidybxcs"));
		
		
		ComboBox cond = new ComboBox();
		cond.setAllowBlank(true);
		cond.setEditable(true);
		List l1 = new ArrayList();
		l1.add(new IDropDownBean(0, ">"));
		l1.add(new IDropDownBean(1, "<"));
		l1.add(new IDropDownBean(2, "="));
		l1.add(new IDropDownBean(3, ">="));
		l1.add(new IDropDownBean(4, "<="));
		egu.getColumn("condop").setEditor(cond);
		egu.getColumn("condop").setComboEditor(egu.gridId, new
		IDropDownModel(l1));
		egu.getColumn("condop").setReturnId(false);
		
		
		ComboBox param = new ComboBox();
		param.setAllowBlank(true);
		param.setEditable(true);
		List l2 = new ArrayList();
		l2.add(new IDropDownBean(0, "是"));
		l2.add(new IDropDownBean(1, "否"));
		egu.getColumn("param").setEditor(param);
		egu.getColumn("param").setComboEditor(egu.gridId, new
		IDropDownModel(l2));
		egu.getColumn("param").setReturnId(false);
		
		
		GridButton gbl = new GridButton("上一步","function(){document.getElementById('LastButton').click();}");
		egu.addTbarBtn(gbl);
//		GridButton gbn = new GridButton("完成","function(){document.getElementById('NextButton').click();}");
//		egu.addTbarBtn(gbn);
		egu.addToolbarButton("完成",GridButton.ButtonType_SaveAll, "NextButton");
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}
}
