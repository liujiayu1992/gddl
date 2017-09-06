package com.zhiren.dc.meic;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meicwd extends BasePage implements PageValidateListener {
//		进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//	  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//		按钮事件处理

	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	private boolean _XxszChick = false;

	public void XxszButton(IRequestCycle cycle) {
		_XxszChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
		if (_shuaxin){
			_shuaxin=false;
			getSelectData();
		}
		
		if (_XxszChick){
			_XxszChick=false;
			gotoMeiccedwd(cycle);
			
		}
		
	}
	
//	 绑定日期
	public String getRiq() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiq(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getAfter() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setAfter(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}
	
	private void gotoMeiccedwd(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个数据进行详细设置!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
//			System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Meiccedwd");
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
			return "to_date('"+value+"','yyyy-mm-dd hh24:mi:ss')";
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
//				return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
    
//		保存分组的改动
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="meicwdb";
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
			sql.append("delete from ").append("meiccedwdb").append(" where meicwdb_id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		while(mdrsl.next()) {
		
			StringBuffer sql2 = new StringBuffer();
	
			if("0".equals(mdrsl.getString("ID"))) {
            	sql.append("insert into ").append(tableName).append("(id");
            	sql.append(",").append("RIQ");
            	sql2.append("getnewid("+visit.getDiancxxb_id()+")");
            	sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[1]),mdrsl.getString(1)+" "+mdrsl.getString(2)));
            	sql.append(",").append("diancxxb_id");					
				sql2.append(",").append(visit.getDiancxxb_id());            	
            	sql.append(",").append("HUANJWD");					
				sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[3]),mdrsl.getString(3)));
            	sql.append(",").append("CAOZY_ID");					
				sql2.append(",").append(visit.getRenyID());
            	sql.append(",").append("CAOZSJ");					
				sql2.append(",").append("to_date('").append(mdrsl.getString("CAOZSJ")).append("', 'yyyy-mm-dd hh24:mi:ss')");
            	sql.append(",").append("BEIZ");					
				sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[6]),mdrsl.getString(6)));

				sql.append(") values(").append(sql2).append(");\n");
				
			}else {
				
				sql.append("update ").append(tableName).append(" set ");
				sql.append("RIQ").append(" = ");					
				sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[1]),mdrsl.getString(1)+" "+mdrsl.getString(2))).append(",");
				sql.append("HUANJWD").append(" = ");					
				sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[3]),mdrsl.getString(3))).append(",");
				sql.append("BEIZ").append(" = ");					
				sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[6]),mdrsl.getString(6))).append(",");				

				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
//			System.out.println(sql.toString());
		con.getUpdate(sql.toString());
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss"); 
		SimpleDateFormat   formatterDate   =   new   SimpleDateFormat   ("yyyy-MM-dd");
		StringBuffer sql=new StringBuffer();
		JDBCcon con = new JDBCcon();
		sql.append("select id,riq,to_char(riq,'hh24:mi:ss') as shij,huanjwd,getcaozy(meicwdb.caozy_id) as caozy,to_char(caozsj,'yyyy-mm-dd hh24:mi:ss') as caozsj,beiz from meicwdb where riq>=");
		sql.append(DateUtil.FormatOracleDate(getRiq())).append(" and riq <");
		sql.append(DateUtil.FormatOracleDate(getAfter())).append(" + 1");
		
		ResultSetList rsl = con.getResultSetList(sql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(formatterDate.format(new Date()));
		egu.getColumn("shij").setHeader("时间");
		egu.getColumn("shij").setDefaultValue("00:00:00");
		egu.getColumn("huanjwd").setHeader("环境温度");
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caozy").setDefaultValue(visit.getRenymc());
		egu.getColumn("caozy").setEditor(null);
		egu.getColumn("caozsj").setHeader("操作时间");
		egu.getColumn("caozsj").setDefaultValue(formatter.format(new Date()));
		egu.getColumn("caozsj").setEditor(null);
		egu.getColumn("beiz").setHeader("备注");				

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		
//		StringBuffer sb=new StringBuffer();
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		egu.addToolbarItem(df1.getScript());		
		egu.addTbarText("-");
		GridButton gbtj = new GridButton("添加",GridButton.ButtonType_Insert,"gridDiv",egu.getGridColumns(),"InsertButton");
		gbtj.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(gbtj);
		egu.addTbarText("-");  
		
		
//		sb.append("{text:' 添加',icon:'imgs/btnicon/insert.gif',cls:'x-btn-text-icon',minWidth:75,handler:function() {	\n")
//			
//			.append("var plant = new gridDiv_plant({ID: '0',RIQ: '',SHIJ: '',HUANJWD: '',CAOZY_ID: '")
//			.append(visit.getRenymc())
//			.append("',CAOZSJ: '")
//			.append(formatter.format(new Date()))
//			.append("',BEIZ: ''});	\n")
//			.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);	\n}}");
		
		
		egu.addToolbarItem("{text:' 刷新',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");

		egu.addTbarText("-");	
//		egu.addToolbarItem(sb.toString());
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
		egu.addTbarText("-");
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","");
	    egu.addTbarText("-");
//			egu.addToolbarItem("{"
//					+ new GridButton("详细设置","function (){ " +
//							" var Baobpz_id =\"\";" +
//							" if(gridDiv_sm.getSelected()==null){ " +
//							"	Ext.MessageBox.alert(\"提示信息\",\"请选中一个数据进行详细设置\");  return; } " +
//							" Rstlist = gridDiv_sm.getSelected();" +
//							" if(Rstlist.get(\"ID\") == \"0\"){ " +
//							"	Ext.MessageBox.alert(\"提示信息\",\"在详细设置之前请先保存!\"); return; }" +
//							" Baobpz_id = Rstlist.get(\"ID\");" +
//							" var cge = document.getElementById(\"CHANGE\");" +
//							" cge.value = Baobpz_id; " +
//							"function(){ document.getElementById('XxszButton').click();"
//									+ "}").getScript() + "}");
	    egu.addToolbarItem("{text:'编辑煤场温度',minWidth:75,handler:function (){  var grid1_history =\'\'; if(gridDiv_sm.getSelected()==null){ 	Ext.MessageBox.alert(\'提示信息\',\'请选中一个分组设置权限\');  return; }  grid1_rcd = gridDiv_sm.getSelected(); if(grid1_rcd.get(\'ID\') == \'0\'){ 	Ext.MessageBox.alert(\'提示信息\',\'在设置权限之前请先保存!\'); return; } grid1_history = grid1_rcd.get(\'ID\'); var Cobj = document.getElementById(\'CHANGE\'); Cobj.value = grid1_history;  document.getElementById(\'XxszButton\').click();}}");
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
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setAfter(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
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
}


