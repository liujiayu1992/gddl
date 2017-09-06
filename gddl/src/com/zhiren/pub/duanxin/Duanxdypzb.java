package com.zhiren.pub.duanxin;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duanxdypzb extends BasePage implements PageValidateListener {
//	进行页面提示信息的设置
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
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			getSelectData();
		}
	}
//	按钮事件处理

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
		}
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
    
//	保存分组的改动
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	/*public void Save1(String strchange,Visit visit) {
		String tableName="duanxdypzb";
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		while(mdrsl.next()) {
		
			StringBuffer sql2 = new StringBuffer();
	
			if("0".equals(mdrsl.getString("ID"))) {
            	sql.append("insert into ").append(tableName).append("(id");
            	sql2.append("getnewid("+visit.getDiancxxb_id()+")");
				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
			
					sql.append(",").append(mdrsl.getColumnNames()[i]);					
					sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));}

				sql.append(") values(").append(sql2).append(");\n");
				
			}else {
				
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");					
					sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");

				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
		System.out.println(sql.toString());
		con.getUpdate(sql.toString());
	}*/
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,fashs,miaos,beiz from duanxdypzb");

		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("duanxdypzb");
		//2009-08-27 chh 设置宽度为屏幕宽度
//		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fashs").setHeader("内容函数");		
		egu.getColumn("miaos").setHeader("描述");		
		egu.getColumn("beiz").setHeader("备注");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
//		egu.setWidth(1000);
		
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("{text:' 添加',icon:'imgs/btnicon/insert.gif',cls:'x-btn-text-icon',minWidth:75,handler:function() {	\n")
			
			.append("var plant = new gridDiv_plant({ID: '0',FASHS: '',MIAOS: '',BEIZ: ''});	\n")
			.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);	\n}}");
		
		
		egu.addToolbarItem("{text:' 刷新',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");

		egu.addTbarText("-");	
		egu.addToolbarItem(sb.toString());
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
		egu.addTbarText("-");
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","");
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
}

