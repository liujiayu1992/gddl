package com.zhiren.dc.huaygl.huaydbsz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class Huaydbszxx extends BasePage implements PageValidateListener {
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
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

		if (_shuaxin){
			_shuaxin=false;
			getSelectData();
		}
		
		if (_ReturnChick){
			_ReturnChick=false;
			gotoHuaydbsz(cycle);
			
		}
		
	}
	private void gotoHuaydbsz(IRequestCycle cycle) {

//		System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Huaydbsz");
	}
    
//	保存分组的改动
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="huaydbszxxb";
		boolean isinsert=false;//判断是否有数据插入
		boolean isdelete=false;//判断是否有数据删除
		JDBCcon con = new JDBCcon();
		
		StringBuffer insertsql = new StringBuffer("begin \n");
		StringBuffer deletesql = new StringBuffer("begin \n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		mdrsl.beforefirst();
		
		if(mdrsl.getRows()==0||getselect(((Visit) this.getPage().getVisit()).getString1()).isEmpty()){
			mdrsl.beforefirst();
			isinsert=true;
			while(mdrsl.next()){
				StringBuffer sql2 = new StringBuffer();
				insertsql.append("insert into ").append(tableName).append("(id");	
				insertsql.append(",").append("HUAYDBSZB_ID").append(",").append("LEIBB_ID").append(",").append("ZHUANGT");		
	            sql2.append("getnewid("+visit.getDiancxxb_id()+")");
				sql2.append(",").append(((Visit) this.getPage().getVisit()).getString1()).append(",").append(mdrsl.getString("ID")).append(",").append("1");
				insertsql.append(") values(").append(sql2).append(");\n");
			}
		}else{
//添加		
			while(mdrsl.next()) {
				for(int i=0;i<getselect(((Visit) this.getPage().getVisit()).getString1()).size();i++){
					if(getselect(((Visit) this.getPage().getVisit()).getString1()).get(i).equals(mdrsl.getString("ID"))){
							break;
					}else{
						isinsert=true;
						if(i==(getselect(((Visit) this.getPage().getVisit()).getString1()).size()-1)){
							StringBuffer sql2 = new StringBuffer();
							insertsql.append("insert into ").append(tableName).append("(id");	
							insertsql.append(",").append("HUAYDBSZB_ID").append(",").append("LEIBB_ID").append(",").append("ZHUANGT");		
				            sql2.append("getnewid("+visit.getDiancxxb_id()+")");
							sql2.append(",").append(((Visit) this.getPage().getVisit()).getString1()).append(",").append(mdrsl.getString("ID")).append(",").append("1");
							insertsql.append(") values(").append(sql2).append(");\n");	
						}		
					}
				}
			}
//删除
			mdrsl.beforefirst();
			for(int i=0;i<getselect(((Visit) this.getPage().getVisit()).getString1()).size();i++){
				mdrsl.beforefirst();
				while(mdrsl.next()) {
					if(getselect(((Visit) this.getPage().getVisit()).getString1()).get(i).equals(mdrsl.getString("ID"))){
						break;
					}else{
						isdelete=true;
						if(mdrsl.getRow()==(mdrsl.getRows()-1)){
							deletesql.append("delete from ").append(tableName).append(" where huaydbszb_id =").append(((Visit) this.getPage().getVisit()).getString1()).append(" and leibb_id=").append(getselect(((Visit) this.getPage().getVisit()).getString1()).get(i)).append(";\n");
						}
					}	
				}
			}
		}
		insertsql.append("end; \n");
		deletesql.append("end; \n");
		if(isinsert){
			con.getInsert(insertsql.toString());
			System.out.println(insertsql.toString());
			isinsert=false;
		}		
		if(isdelete){
			con.getDelete(deletesql.toString());
			System.out.println(deletesql.toString());
			isdelete=false;
		}	
		
	}
	
	private ArrayList getselect(String id){
		ArrayList list=new ArrayList();
		JDBCcon con=new JDBCcon();
		PreparedStatement ps=null;
		ResultSet rst=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select leibb_id from huaydbszxxb where huaydbszb_id=");
		sql.append(id);
		sql.append("and zhuangt=1");

		try {
			ps=con.getPresultSet(sql.toString());
			rst=ps.executeQuery();
			while(rst.next()){
				list.add(rst.getString("leibb_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        con.Close();
		return list;
	}
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,mingc from leibb");

		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("huaydbszxxb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(1000);
		
	    egu.addToolbarButton(GridButton.ButtonType_SubmitSel, "SaveButton","");
	    egu.addTbarText("-");
		egu.addToolbarItem("{text:' 刷新',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
		egu.addTbarText("-");
		egu.addToolbarItem("{"
				+ new GridButton("返回",
						"function(){ document.getElementById('ReturnButton').click();"
								+ "}").getScript() + "}");
		setExtGrid(egu);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addOtherScript("var i;var nums=[];");
		for(int i=0;i<getselect(((Visit) this.getPage().getVisit()).getString1()).size();i++){
				egu.addOtherScript(
						    "for(i=0;i<gridDiv_data.length;i++){"+
				            	
				            		"if(gridDiv_data[i][0]=='"+getselect(((Visit) this.getPage().getVisit()).getString1()).get(i)+"'){" +
				            			"nums[i]=i"+
				            		"}"+						  
			    "}");				
		}
		egu.addOtherScript("gridDiv_sm.selectRows(nums);");
			

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

