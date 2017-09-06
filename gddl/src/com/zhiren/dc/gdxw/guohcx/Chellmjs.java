package com.zhiren.dc.gdxw.guohcx;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class Chellmjs extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	
	private String eriq; // 保存日期
	
	public String getEriq() {
		return eriq;
	}

	public void setEriq(String eriq) {
		this.eriq = eriq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    
	
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			
		}
		this.getSelectData();
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
	
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				//不允许添加
			} else {
				String jusyy=mdrsl.getString("jusyy");
				String cheph=mdrsl.getString("cheph");
				sbsql.append("update chepbtmp c set c.isjus=1,c.jussj=sysdate,c.jusry='"+visit.getRenymc()+"',c.jusyy='"+jusyy+"' where id="+mdrsl.getString("id")+";\n");
			    //当拒收原因是"原因不明"或者"采到钢绳"时,在车辆信息表中停用此车辆,并更新车辆日志表.
				if(jusyy.equals("原因不明")||jusyy.equals("采到钢绳")){
					sbsql.append("update chelxxb cc set cc.islocked=1,cc.suocr='"+visit.getRenymc()+"',cc.suocsj=sysdate where cc.cheph='"+cheph+"';\n");
					SaveLog(con,sbsql,"1",cheph);
				}

			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
		this.setMsg("拒收成功!<br>查询已经拒收的车辆,请进<<拒收查询>>页面!");
	}
	
	

	public void SaveLog(JDBCcon con,StringBuffer sbsql,String zhuangt,String cheph){
		Visit visit = (Visit) this.getPage().getVisit();
		
		String sql="select c.id,c.jiecr from chelxxlogb c where c.cheph='"+cheph+"' ";
		ResultSetList rs=con.getResultSetList(sql);
		if(rs.next()){
			String jiesry=rs.getString("jiecr");
			if(jiesry.equals("")||jiesry==null){
				sbsql.append("update chelxxlogb c set c.jiecr='"+visit.getRenymc()+"',jiecsj=sysdate where id="+rs.getLong("id")+";\n");
			}else{
				sbsql.append("update chelxxlogb c set c.suocr='"+visit.getRenymc()+"',suocsj=sysdate,jiecr=null,jiecsj=null where id="+rs.getLong("id")+";\n");
			}
			
		}else if(zhuangt.equals("1")){
			sbsql.append("insert into chelxxlogb (id,cheph,suocr,suocsj,jiecr,jiecsj) values (" +
					"xl_xul_id.nextval,'"+cheph+"','"+visit.getRenymc()+"',sysdate,null,null);\n");
		}
		
		
		
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		
		
		
		JDBCcon con = new JDBCcon();
		String sql = 

			"select c.id,c.cheph,c.meikdwmc,c.jusyy,c.zhongcjjy,\n" +
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcjs,\n" + 
			"c.zhongchh,c.piaojh,c.lury,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') as lursj\n" + 
			"from chepbtmp c\n" + 
			"where c.piz=0 and c.qingcsj is null\n" + 
			"and c.isjus=0\n" + 
			"order by c.lursj";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);

		//设置每页显示行数
		egu.addPaging(0);
		
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
	    egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("cheph").setEditor(null);
		
		egu.getColumn("meikdwmc").setHeader("煤矿");
		egu.getColumn("meikdwmc").setWidth(130);
		egu.getColumn("meikdwmc").setEditor(null);
		
		egu.getColumn("jusyy").setHeader("拒收原因");
		egu.getColumn("jusyy").setWidth(100);
		egu.getColumn("jusyy").editor.setAllowBlank(true);
		
		egu.getColumn("zhongcjjy").setHeader("重衡员");
		egu.getColumn("zhongcjjy").setWidth(70);
		egu.getColumn("zhongcjjy").setEditor(null);
		
		
		egu.getColumn("zhongcjs").setHeader("重车时间");
		egu.getColumn("zhongcjs").setWidth(130);
		egu.getColumn("zhongcjs").setEditor(null);
		
		egu.getColumn("zhongchh").setHeader("衡号");
		egu.getColumn("zhongchh").setWidth(60);
		egu.getColumn("zhongchh").setEditor(null);
		
		
		egu.getColumn("piaojh").setHeader("采样号");
		egu.getColumn("piaojh").setWidth(60);
		egu.getColumn("piaojh").setEditor(null);
		
		egu.getColumn("lury").setHeader("采样员");
		egu.getColumn("lury").setWidth(60);
		egu.getColumn("lury").setEditor(null);
		
		egu.getColumn("lursj").setHeader("采样时间");
		egu.getColumn("lursj").setWidth(130);
		egu.getColumn("lursj").setEditor(null);
		

		
		
		ComboBox cmk= new ComboBox();
		egu.getColumn("jusyy").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select it.id,it.mingc from itemsort i ,item it\n" +
			"where i.bianm='CHELJSYY'\n" + 
			"and it.itemsortid=i.id order by it.xuh";

		egu.getColumn("jusyy").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("-");
		egu.addToolbarButton("拒收",GridButton.ButtonType_Save, "SaveButton", null, SysConstant.Btn_Icon_SelSubmit);
		

	
	
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			
			
			
		}
		getSelectData();
	}
}