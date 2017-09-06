package com.zhiren.dc.meic;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meiccedwd extends BasePage implements PageValidateListener {
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
//		页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			getData(getInsertSql(((Visit) this.getPage().getVisit()).getString1(),"","").toString());
			getSelectData();
		}
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
			gotoMeicwd(cycle);
			
		}
		
	}
	private void gotoMeicwd(IRequestCycle cycle) {

//			System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Meicwd");
	}
    
//		保存分组的改动
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="meiccedwdb";
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		mdrsl.beforefirst();
		
		if(mdrsl.getRows()==0){
		
		}else{
			while(mdrsl.next()){
				sql.append("update ").append(tableName).append(" set wend='")
				.append(mdrsl.getString("WEND"))
				.append("',beiz='")
				.append(mdrsl.getString("BEIZ"))
				.append("'").append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append("end; \n");
		con.getUpdate(sql.toString());
	
		
	}
	
	private StringBuffer getInsertSql(String id,String wend,String beiz){
		StringBuffer insertSql=new StringBuffer();
		insertSql.append("begin \n");
		JDBCcon con=new JDBCcon();
		PreparedStatement ps=null;
		ResultSet rst=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from meiccedwdb where meicwdb_id=");
		sql.append(id);
		sql.append(" and zhuangt=1");

		try {
			ps=con.getPresultSet(sql.toString());
			rst=ps.executeQuery();
			int i=0;
			while(rst.next()){
				i++;
			}
			if(i==0){
				JDBCcon con1=new JDBCcon();
				PreparedStatement ps1=null;
				ResultSet rst1=null;
				ps1=con1.getPresultSet("select * from meicb");
				rst1=ps1.executeQuery();
				int j=0;
				while(rst1.next()){
					j++;
					if(j>0){
						JDBCcon con2=new JDBCcon();
						PreparedStatement ps2=null;
						ResultSet rst2=null;
						StringBuffer sqlitem=new StringBuffer();
						sqlitem.append("select * from item where ITEMSORTID=")
						.append(rst1.getInt("ID")).append(" and beiz='煤场测点'");
						ps2=con2.getPresultSet(sqlitem.toString());
						rst2=ps2.executeQuery();
						int k=0;
						while(rst2.next()){
							k++;
							if(k>0){
								insertSql.append("insert into meiccedwdb values(getnewid(")
								.append(((Visit) this.getPage().getVisit()).getDiancxxb_id())		
								.append("),")
								.append(id)
								.append(",")
								.append(rst1.getInt("ID"))
								.append(",")
								.append(rst2.getInt("ID"))
								.append(",'")
								.append(wend)
								.append("',1,'")
								.append(beiz)
								.append("');");
							}
						}
						
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        con.Close();
        insertSql.append("end; \n");
		return insertSql;
	}
	private void getData(String sql){
		JDBCcon con=new JDBCcon();
		con.getInsert(sql);
	}
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer BUFsql=new StringBuffer();
		BUFsql.append("select meiccedwdb.id,meicb.mingc as meic,item.mingc as ced,wend,meiccedwdb.beiz from meiccedwdb,item,meicb where meiccedwdb.meicb_id=meicb.id and meiccedwdb.item_id=item.id and meiccedwdb.meicwdb_id=");
		BUFsql.append(((Visit) this.getPage().getVisit()).getString1());
		ResultSetList rsl = con.getResultSetList(BUFsql.toString());

		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meiccedwdb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meic").setHeader("煤场");
		egu.getColumn("meic").setEditor(null);
		egu.getColumn("ced").setHeader("测点");
		egu.getColumn("ced").setEditor(null);
		egu.getColumn("wend").setHeader("温度");
		egu.getColumn("beiz").setHeader("备注");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(1000);
		GridButton gbtj = new GridButton("提交",GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		gbtj.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(gbtj);
//	    egu.addToolbarButton(GridButton.ButtonType_SubmitSel, "SaveButton","");
	    egu.addTbarText("-");
		egu.addToolbarItem("{text:' 刷新',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
		egu.addTbarText("-");
		egu.addToolbarItem("{"
				+ new GridButton("返回",
						"function(){ document.getElementById('ReturnButton').click();"
								+ "}").getScript() + "}");
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

