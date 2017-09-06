package com.zhiren.dc.gdxw.qiangzzy;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-12-29
 * 修改内容:强制制样时，同样维护制样信息zhiyryb
 */
public class Qiangzzy extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
	
	
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	
	
	private void Show() {
		
		Visit visit=(Visit)this.getPage().getVisit();
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择数据进行强制制样！");
			return;
		}
		int flag=0;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin\n");
		while(rsl.next()) {
			sbsql.append("update gdxw_cy set zhuangt=1,beiz='强制制样'  where id ="+rsl.getString("id")+";\n");
		}	
		
		sbsql.append("end;");
		
		
		flag=con.getUpdate(sbsql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sbsql.toString());
			setMsg("保存过程出现错误！保存未成功！");
			con.rollBack();
			con.Close();
			return;
		}
		
		con.Close();
		this.setMsg("强制制样成功!");
		
	}
	
	
	
	
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show();
			getSelectData();
		}
	}
	


	private String filterDcid(Visit v){
		
		String sqltmp = " ("+ v.getDiancxxb_id()+")";
		if(v.isFencb()){
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList("select id from diancxxb where fuid="+v.getDiancxxb_id());
			sqltmp = "";
			while(rsl.next()) {
				sqltmp += ","+rsl.getString("id");
			}
			sqltmp ="("+ sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	
	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=this.getRiq();
		StringBuffer sb = new StringBuffer();
		
				sb.append("select cy.id,rownum  as xuh,y.mingc,cy.jingz\n");
				sb.append("from gdxw_cy cy,caiyb c ,cunywzb y\n");
				sb.append(" where  cy.zhuangt=0\n"); 
				sb.append(" and cy.zhilb_id=c.zhilb_id\n");
				sb.append(" and c.cunywzb_id=y.id\n"); 
				sb.append(" and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"'\n"); 
				sb.append(" order by y.xuh");



		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置每页显示行数
		egu.addPaging(0);
		
		
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
	
		
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		
		egu.getColumn("mingc").setHeader("桶号");
		egu.getColumn("mingc").setWidth(120);
	
		egu.getColumn("jingz").setHeader("煤量");
		egu.getColumn("jingz").setWidth(80);
		
	
		
	
		
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ");
		egu.addTbarText("来煤日期：");
		egu.addToolbarItem(df.getScript());
		/*	
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("EndRq");
		egu.addTbarText("至");
		egu.addToolbarItem(dfe.getScript());*/
		
		
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("强制制样",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_SelSubmit);
		
//		egu.addOtherScript(" gridDiv_grid.addListener('cellclick',function(grid, rowIndex, columnIndex, e){});");
		
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
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
			setRiq( DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			getSelectData();
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		getSelectData();
	}
}