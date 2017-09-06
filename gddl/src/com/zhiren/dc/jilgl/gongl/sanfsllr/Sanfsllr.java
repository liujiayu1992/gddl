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
	// �����û���ʾ
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

	// ҳ��仯��¼
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
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		 egu.setTableName("chepb");
		// ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// ����ÿҳ��ʾ����
		egu.addPaging(0);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setUpdate(false);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setUpdate(false);
		egu.getColumn("biaoz").setHeader("����");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setUpdate(false);
		egu.getColumn("sanfsl").setHeader("������");
		egu.getColumn("sanfsl").setWidth(60);
//		egu.getColumn("sanfsl").setEditor(null);
		egu.getColumn("chae").setHeader("���");
		egu.getColumn("chae").setEditor(null);
		egu.getColumn("chae").setWidth(60);
		egu.getColumn("chae").setUpdate(false);
		egu.getColumn("meikmc").setHeader("ú��λ");
		egu.getColumn("meikmc").setEditor(null);
		egu.getColumn("meikmc").setUpdate(false);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setUpdate(false);
		egu.getColumn("yunsdw").setHeader("���䵥λ");
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("yunsdw").setUpdate(false);
		egu.getColumn("zhongcsj").setHeader("�س�ʱ��");
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(150);
		egu.getColumn("zhongcsj").setUpdate(false);
		egu.getColumn("qingcsj").setHeader("�ᳵʱ��");
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").setWidth(150);
		egu.getColumn("qingcsj").setUpdate(false);
		
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addTbarText("������λ:");	
		String condition=" and fh.daohrq = to_date('"+ riqTiaoj +"','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		egu.addTbarText("-");	
		//ˢ�°�ť
		GridButton rbtn = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(rbtn);
		//���水ť
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");	
		setExtGrid(egu);
		con.Close();
		// ��ӡ��ť
		String openScript =
			"var openUrl = 'http://'+document.location.host+document.location.pathname; " +
			"var end = openUrl.indexOf(';');" +
			"openUrl = openUrl.substring(0,end);" +
			"openUrl = openUrl + '?service=page/SanfsllrReport';" +
			"window.open(openUrl ,'newWin','resizable=1');";
		GridButton gbp = new GridButton("��ӡ", "function (){" + openScript + "}");
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setExtGrid1(null);
			visit.setString2(null);
			visit.setString5(null);
		}
		getSelectData();
	}
}
