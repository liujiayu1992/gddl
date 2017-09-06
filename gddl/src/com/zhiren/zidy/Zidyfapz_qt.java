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

public class Zidyfapz_qt extends BasePage implements PageValidateListener {
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
		while(rs.next()){
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','colname_cn','"+rs.getString("z_column_cn")+"');\n";
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','width','"+rs.getString("width")+"');\n";
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','operational','"+rs.getString("opera")+"');\n";
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','align','"+rs.getString("align")+"');\n";	
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','datatype','"+rs.getString("datatype")+"');\n";	
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','fontsize','"+rs.getString("font")+"');\n";	
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','scales','"+rs.getString("scales")+"');\n";	
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','orderNum','"+rs.getString("orderNum")+"');\n";
			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) values("
				+ pz_id + ",'" + rs.getString("z_column")+ "','merger','"+(rs.getString("merger").equals("是")?1:0)+"');\n";	
		}
		rs.close();
		sql += "end;";
		String delSql;
		delSql="begin\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='colname_cn';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='width';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='operational';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='align';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='datatype';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='fontsize';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='scales';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='orderNum';\n";
		delSql+="delete from zidypzms where zidyfapz_id ="+ pz_id +" and z_code ='merger';\n";
		delSql+="end;";
		con.getDelete(delSql);
		con.getUpdate(sql);
		
		con.Close();
		cycle.activate("Zidyfapz_tj");
	}
	
	private boolean _LastChick = false;

	public void LastButton(IRequestCycle cycle) {
		_LastChick = true;
	}
	
	private void Last(IRequestCycle cycle){
		cycle.activate("Zidyfapz_px");
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
		String sql = "select * from zidypzms  where zidyfapz_id = (select id from zidyfapz where zidyfa_id = "+visit.getString1()+") and z_code = 'width'";
//		判断ZIDYPZMS表中是否含有z_code='width'的数据（判断此步骤是否保存过）
		ResultSetList rs = con.getResultSetList(sql);
//		如果保存过1
		if(rs.next()){
//			取zidypzms中的值
			sql = "select y.z_column, y.z_column_cn, pz.width,pz.opera,pz.align,pz.datatype as datatype,pz.font,pz.scales,pz.ordernum,pz.merger\n" +
			"from zidyjcsjyms y,\n" + 
			"(select\n" + 
			"    p.z_column,\n" + 
			"    max(decode(p.z_code,'width',z_value,'')) width,\n" + 
			"    max(decode(p.z_code,'operational',z_value,'')) opera,\n" + 
			"    max(decode(p.z_code,'weighted',z_value,'')) weight,\n" + 
			"    max(decode(p.z_code,'align',z_value,'')) align,\n" + 
			"    max(decode(p.z_code,'datatype',z_value,'')) datatype,\n" + 
			"    max(decode(p.z_code,'fontsize',z_value,'')) font,\n" + 
			"    max(decode(p.z_code,'scales',z_value,'')) scales,\n" + 
			"    max(decode(p.z_code,'orderNum',z_value,'')) ordernum,\n" + 
			"    max(decode(p.z_code,'merger',decode(z_value,'1','是','否'),'')) merger\n" + 
			"from zidypzms p\n" + 
			"     where p.zidyfapz_id = (select id from zidyfapz where zidyfa_id = "+visit.getString1()+")\n" + 
			"group by p.z_column) pz\n" + 
			"where y.zidyjcsjy_id = (select zidyjcsjy_id from zidyfapz where zidyfa_id = "+visit.getString1()+")\n" + 
			"  and y.z_column = pz.z_column order by y.id";
//		否则1
		}else{
//		    取zidyjcsjyms中的值作为默认值
			sql = "select y.z_column,y.z_column_cn,y.z_width as width,y.z_operational as opera,decode(y.z_align,-1,'居左',1,'居中',2,'居右','居左') as align ,z_datatypes as datatype" +
					",nvl(y.z_fontsize,9) as font,0 as scales,pz.z_value orderNum,nvl('否','') as merger \n" +
			"from zidyjcsjyms y,\n" + 
			"( select p.* from zidypzms p where p.zidyfapz_id = (select id from zidyfapz where zidyfa_id = "+visit.getString1()+")\n" + 
			" and  p.z_code = 'showNum' order by p.z_value) pz\n" + 
			"where y.zidyjcsjy_id = (select zidyjcsjy_id from zidyfapz where zidyfa_id = "+visit.getString1()+")\n" + 
			"and y.z_column = pz.z_column order by y.id";
		}
		rs.close();
		rs = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
		egu.setTableName("zidyjcsjyms");
		egu.getColumn("z_column").setHeader("列名");
		egu.getColumn("z_column").setWidth(100);
//		egu.getColumn("z_column").setHidden(true);
		egu.getColumn("z_column_cn").setHeader("中文名");
		egu.getColumn("z_column_cn").setWidth(200);
//		egu.getColumn("zidyjcsjy_id").setHidden(true);
//		egu.getColumn("zidyfapz_id").setHidden(true);
		egu.getColumn("width").setHeader("宽度");
		egu.getColumn("width").setWidth(50);
		egu.getColumn("opera").setHeader("统计方法");
		egu.getColumn("opera").setWidth(60);
		egu.getColumn("align").setHeader("对齐方式");
		egu.getColumn("align").setWidth(60);
		egu.getColumn("datatype").setHeader("数据类型");
		egu.getColumn("datatype").setWidth(60);
		egu.getColumn("font").setHeader("字体大小");
		egu.getColumn("font").setWidth(60);
		egu.getColumn("scales").setHeader("小数位");
		egu.getColumn("scales").setWidth(50);
		egu.getColumn("orderNum").setHeader("排序");
		egu.getColumn("orderNum").setWidth(50);
		egu.getColumn("merger").setHeader("合并重复值");
		egu.getColumn("merger").setWidth(70);
		
		ComboBox datatype = new ComboBox();
		egu.getColumn("datatype").setEditor(datatype);
		datatype.setEditable(true);
		List type = new ArrayList();
		type.add(new IDropDownBean(0, "date"));
		type.add(new IDropDownBean(1, "datetime"));
		type.add(new IDropDownBean(2, "varchar"));
		type.add(new IDropDownBean(3, "number"));
		egu.getColumn("datatype").setComboEditor(egu.gridId, new IDropDownModel(type));
		egu.getColumn("datatype").returnId=false;
		
		ComboBox zid = new ComboBox();
		egu.getColumn("merger").setEditor(zid);
		zid.setEditable(true);
		List ziduan = new ArrayList();
		ziduan.add(new IDropDownBean(0, "是"));
		ziduan.add(new IDropDownBean(1, "否"));
		egu.getColumn("merger").setComboEditor(egu.gridId, new IDropDownModel(ziduan));
		
		ComboBox operational = new ComboBox();
		egu.getColumn("opera").setEditor(operational);
		operational.setEditable(true);
		List tongj = new ArrayList();
		tongj.add(new IDropDownBean(-1, "无"));
		tongj.add(new IDropDownBean(0, "sum"));
		tongj.add(new IDropDownBean(1, "avg"));
		tongj.add(new IDropDownBean(2, "加权"));
		
		egu.getColumn("opera").setComboEditor(egu.gridId, new IDropDownModel(tongj));
		
//		ComboBox tj = new ComboBox();
//		egu.getColumn("orderNum").setEditor(tj);
//		tj.setEditable(true);
//		List tiaoj = new ArrayList();
//		tiaoj.add(new IDropDownBean(0, ">"));
//		tiaoj.add(new IDropDownBean(1, "<"));
//		tiaoj.add(new IDropDownBean(2, "="));
//		tiaoj.add(new IDropDownBean(3, ">="));
//		tiaoj.add(new IDropDownBean(4, "<="));
//		egu.getColumn("orderNum").setComboEditor(egu.gridId, new IDropDownModel(tiaoj));
		
		ComboBox align = new ComboBox();
		egu.getColumn("align").setEditor(align);
		align.setEditable(true);
		List duiq = new ArrayList();
		duiq.add(new IDropDownBean(-1, "居左"));
		duiq.add(new IDropDownBean(2, "居右"));
		duiq.add(new IDropDownBean(1, "居中"));
		egu.getColumn("align").setComboEditor(egu.gridId, new IDropDownModel(duiq));
		egu.getColumn("align").returnId=true;
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		GridButton gbl = new GridButton("上一步","function(){document.getElementById('LastButton').click();}");
		egu.addTbarBtn(gbl);
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		GridButton gbn = new GridButton("下一步","function(){document.getElementById('NextButton').click();}");
//		egu.addTbarBtn(gbn);
		egu.addToolbarButton("下一步",GridButton.ButtonType_SaveAll, "NextButton");
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