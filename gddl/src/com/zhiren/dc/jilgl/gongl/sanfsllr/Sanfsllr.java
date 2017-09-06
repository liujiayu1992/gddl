package com.zhiren.dc.jilgl.gongl.sanfsllr;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Sanfsllr extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
		Visit visit = (Visit)this.getPage().getVisit();
		this.getExtGrid().Save(this.getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		String diancxxb_id  = visit.getDiancxxb_id() + "";
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqi();
		String meikmcWhere = "";
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			meikmcWhere = "AND MEIKXXB_ID = " + getTreeid();
		}
		String sql = 
			"SELECT ID,\n" +
			"       CHEPH,\n" + 
			"       MAOZ,\n" + 
			"       PIZ,\n" + 
			"       BIAOZ,\n" + 
			"       SANFSL,\n" + 
			"       (BIAOZ-SANFSL) AS CHAE,\n" +
			"       (SELECT NVL(MINGC, '') FROM MEIKXXB WHERE ID = (SELECT MEIKXXB_ID FROM FAHB WHERE ID = FAHB_ID)) AS MEIKMC,\n" + 
			"       (SELECT NVL(MINGC, '') FROM PINZB WHERE ID = (SELECT PINZB_ID FROM FAHB WHERE ID = FAHB_ID)) AS PINZ,\n" + 
			"       (SELECT NVL(MINGC, '') FROM YUNSDWB WHERE ID = YUNSDWB_ID) AS YUNSDW,\n" + 
			"       TO_CHAR(ZHONGCSJ,'yyyy-mm-dd hh24:mi:ss') AS ZHONGCSJ,\n" + 
			"       TO_CHAR(QINGCSJ,'yyyy-mm-dd hh24:mi:ss') AS QINGCSJ\n" + 
			"  FROM CHEPB\n" + 
			" WHERE FAHB_ID IN (SELECT ID\n" + 
			"                     FROM FAHB\n" + 
			"                    WHERE DAOHRQ = TO_DATE('" + riqTiaoj + "', 'yyyy-mm-dd')\n" + 
			"                     AND DIANCXXB_ID = " + diancxxb_id + "\n" +
			"                      " + meikmcWhere + ")\n" + 
			" ORDER BY CHEPH";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		 egu.setTableName("chepb");
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu.addPaging(0);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setUpdate(false);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setUpdate(false);
		egu.getColumn("biaoz").setHeader("净重");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setUpdate(false);
		egu.getColumn("sanfsl").setHeader("三方量");
		egu.getColumn("sanfsl").setWidth(60);
//		egu.getColumn("sanfsl").setEditor(null);
		egu.getColumn("chae").setHeader("差额");
		egu.getColumn("chae").setEditor(null);
		egu.getColumn("chae").setWidth(60);
		egu.getColumn("chae").setUpdate(false);
		egu.getColumn("meikmc").setHeader("煤矿单位");
		egu.getColumn("meikmc").setEditor(null);
		egu.getColumn("meikmc").setUpdate(false);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setUpdate(false);
		egu.getColumn("yunsdw").setHeader("运输单位");
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("yunsdw").setUpdate(false);
		egu.getColumn("zhongcsj").setHeader("重车时间");
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(150);
		egu.getColumn("zhongcsj").setUpdate(false);
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").setWidth(150);
		egu.getColumn("qingcsj").setUpdate(false);
		
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addTbarText("供货单位:");	
		String condition=" and fh.daohrq = to_date('"+ riqTiaoj +"','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		egu.addTbarText("-");	
		//刷新按钮
		GridButton rbtn = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(rbtn);
		//保存按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");	
		setExtGrid(egu);
		con.Close();
		// 打印按钮
		String openScript =
			"var openUrl = 'http://'+document.location.host+document.location.pathname; " +
			"var end = openUrl.indexOf(';');" +
			"openUrl = openUrl.substring(0,end);" +
			"openUrl = openUrl + '?service=page/SanfsllrReport';" +
			"window.open(openUrl ,'newWin','resizable=1');";
		GridButton gbp = new GridButton("打印", "function (){" + openScript + "}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
	}
	
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2()==null
				||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
		}
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
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
		return getExtGrid().getGridScript();
	}
	
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {		
		return getTree().getWindowTreeScript();
	}
	
	public String getRiqi() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiqi(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setExtGrid1(null);
			visit.setString2(null);
			visit.setString5(null);
		}
		getSelectData();
	}
}
