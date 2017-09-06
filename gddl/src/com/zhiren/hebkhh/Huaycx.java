package com.zhiren.hebkhh;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：Huaycx(化验查询)
 */

public class Huaycx extends BasePage implements PageValidateListener {

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
	}
	
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}
	
	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	public boolean getRaw() {
		return true;
	}
	
//	起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {

		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String strSql = "select fh.mkmc, getHuaybh4zl(z.zhilb_id) bianm, z.qnet_ar,\n" +
//			"z.mt, z.mad, z.aad, z.vdaf, z.std, z.had, z.qbad, '化验单' huayd, '对比查询' duibcx from\n" + 
//			"z.mt, z.mad, z.aad, z.vdaf, z.std, z.had, z.qbad, getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Huayd', 'parme', z.zhilb_id, '化验单') as huayd, '对比查询' duibcx from\n" +
			"z.mt, z.mad, z.aad, z.vdaf, z.std, z.had, z.qbad, " +
			"'<a target=_blank title=化验单 href="+ MainGlobal.getHomeContext(this) +"/app?service=page/Huayd&zhilb_id='||z.zhilb_id||'&dianc_id="+ getTreeid() +">化验单</a>' as huayd, " + 
			"'<a target=_blank title=对比查询 href="+ MainGlobal.getHomeContext(this) +"/app?service=page/Duibcx&bianm='||getHuaybh4zl(z.zhilb_id)||'>对比查询</a>' as duibcx from \n " +
//			"'<a target=_blank title=对比查询>比对查询</a>' as duibcx from \n " +
			"(select zhilb_id,max(m.mingc) mkmc from fahb f, meikxxb m\n" + 
			"where f.meikxxb_id = m.id and f.daohrq >= to_date('"+ getBRiq() +"', 'yyyy-MM-dd') " +
			"and f.daohrq < to_date('"+ getERiq() +"', 'yyyy-MM-dd') + 1 and f.diancxxb_id = "+ getTreeid() +
			" group by zhilb_id) fh,\n" + 
			"(select zl.zhilb_id, round_new(avg(zl.qnet_ar),2) qnet_ar,\n" + 
			"round_new(avg(zl.mt),1) mt, round_new(avg(zl.mad),2) mad,\n" + 
			"round_new(avg(zl.aad),2) aad, round_new(avg(zl.vdaf),2) vdaf,\n" + 
			"round_new(avg(zl.std),2) std, round_new(avg(zl.had),2) had,\n" + 
			"round_new(avg(zl.qbad),2) qbad\n" + 
			"from zhillsb zl where zl.shenhzt != -1\n" + 
			"group by zl.zhilb_id) z\n" + 
			"where fh.zhilb_id = z.zhilb_id order by fh.mkmc";
		ResultSetList rslData = con.getResultSetList(strSql);
		
		int ArrWidth[] = new int[12];
		ArrWidth[0] = 130;
		ArrWidth[1] = 130;
		ArrWidth[2] = 70;
		ArrWidth[3] = 70;
		ArrWidth[4] = 70;
		ArrWidth[5] = 70;
		ArrWidth[6] = 70;
		ArrWidth[7] = 70;
		ArrWidth[8] = 70;
		ArrWidth[9] = 70;
		ArrWidth[10] = 60;
		ArrWidth[11] = 60;
		
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0][0] = "煤矿名称";
		ArrHeader[0][1] = "化验编码";
		ArrHeader[0][2] = "低热值(Mj/Kg)<br>Qnet,ar";
		ArrHeader[0][3] = "全水(%)<br>Mt";
		ArrHeader[0][4] = "空干基水(%)<br>Mad";
		ArrHeader[0][5] = "灰分(%)<br>Aad";
		ArrHeader[0][6] = "挥发分(%)<br>Vdaf";
		ArrHeader[0][7] = "硫(%)<br>St,d";
		ArrHeader[0][8] = "氢(%)<br>Had";
		ArrHeader[0][9] = "弹筒热(Mj/Kg)<br>Qb,ad";
		ArrHeader[0][10] = "化验单";
		ArrHeader[0][11] = "对比查询";
		
		rt.setTitle("化 验 查 询", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		rt.body.setPageRows(25);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setColCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setColCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 4, "查询日期："+getBRiq()+" 至 "+getERiq(), Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("到货日期："));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");
		tbr.addField(dfb);
		tbr.addText(new ToolbarText(" "));
		tbr.addText(new ToolbarText(" 至 "));
		tbr.addText(new ToolbarText(" "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");
		tbr.addField(dfe);
		tbr.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		tbr.addFill();
		setToolbar(tbr);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
//	电厂树_开始
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂树_结束
	
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			this.setTreeid(null);
			getDiancmcModels();
		}
		getSelectData();
	}
}
