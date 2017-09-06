package com.zhiren.pub.riz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rizcx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	//行数
	private String row;
	public String getRow(){
		return row;
	}
	public void setRow(String Row){
		row = Row;
	}
	//日志内容
	private String File;
	public String getFile(){
		return File;
	}
	public void setFile(String File){
		File = File;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	 绑定日期
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //为  "查询"  按钮添加处理程序
    	getSelectData();
    }
    
    private boolean _RiznrChick = false;
    public void RiznrButton(IRequestCycle cycle) {
    	_RiznrChick = true;
    }

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		if(_RiznrChick){
			_RiznrChick = false;
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

//		电厂Tree刷新条件
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		} 
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			try {
				ResultSet rsss=con.getResultSet("select id from diancxxb where fuid="+getTreeid());
				if(rsss.next()){
					str = "and (dc.fuid = "+ getTreeid() + "or dc.id = "+ getTreeid() + ")" ;
				}else{
					str = "and dc.id = " + getTreeid() ;
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			
		}
		
		String sql = 
			"select r.id,\n" +
			"       diancxxb_id,\n" + 
			"       to_char(caozsj,'yyyy-MM-dd') as caozsj,\n" + 
			"       caozy,\n" + 
			"       caoz,\n" + 
			"       ip,\n" + 
			"       mokmc,\n" + 
			"       biaom,\n" + 
			"       biaoid,\n" + 
			"       leix,\n" + 
			"       r.beiz,\n" + 
			"		GetNeir(r.id) as neir\n" +
			"from rizb r,diancxxb dc \n" + 
			"where caozsj >= to_date('"+getRiq1()+"', 'yyyy-mm-dd')	\n" + 
            "	and caozsj < to_date('"+getRiq2()+"', 'yyyy-mm-dd') + 1 \n" +
			str + "\n" +
			"   and r.diancxxb_id = dc.id \n" +
			"order by caozsj";
		
		

		ResultSetList rsl = con.getResultSetList(sql);
		row=rsl.getRows()+"";
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rizb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("caozsj").setHeader("操作时间");
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caoz").setHeader("操作");
		egu.getColumn("ip").setHeader("IP");
		egu.getColumn("mokmc").setHeader("模块名称");
		egu.getColumn("biaom").setHeader("表名");
		egu.getColumn("biaoid").setHeader("表ID");
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("neir").setHidden(true);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
	
//**********************************************功能模块*****************************************************************
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		egu.addToolbarItem("{"+new GridButton("查询","function(){\n" +
													 "	var i = document.getElementById('ROW').value;\n" +
													 "	if(i>1000){\n" +
													 "		Ext.Msg.alert('提示信息','记录数过多，请重新设定查询条件！');\n" +
													 "		gridDiv_grid.store.removeAll();\n" +
													 "	}else{\n" +
													 "		document.getElementById('RefurbishButton').click();\n" +
													 "	}\n" +
													 "}" ).getScript()+"}");
		egu.addTbarText("-");
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  if(rec==null){\n"
	        +"  	Ext.MessageBox.alert('提示信息','请选中一条记录!'); \n"
	        +"  	return;\n"
	        +"  }else{\n"
	        +"  	sqms.setValue(rec.get('NEIR'));\n"
	        +"		Rpt_window.show();\n"
	        +"  }\n";
	        //+"  document.getElementById('RiznrButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("日志内容","function(own,row,col,e){"+str2+"}").getScript()+"}");
        egu.addTbarText("-");
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
			visit.setString5("");	//riq1
			visit.setString6("");	//riq2
			visit.setboolean1(true);	//日期change
			
		}
		getSelectData();
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
}
