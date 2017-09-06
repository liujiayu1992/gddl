package com.zhiren.jt.jihgl.yunsjhjc;

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
 * 运输计划监测报表
 */

public class YunsjhjcReport extends BasePage implements PageValidateListener {
	
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
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private String daohrq; // 保存查询日期

	public String getDaohrq() {
		return daohrq;
	}

	public void setDaohrq(String daohrq) {
		this.daohrq = daohrq;
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
		String sql = 
			"select decode(grouping(jhjc.id), 1, '总计', gys.mingc) gongysb_id,\n" +
			"       decode(grouping(jhjc.id)+grouping(fz.mingc), 2, '总计', 1, '小计', fz.mingc) faz_id,\n" + 
			"       sum(jhjc.dangr_dun) dangr_dun,\n" + 
			"       sum(jhjc.dangr_che) dangr_che,\n" + 
			"       sum(jhjc.quany_dun) quany_dun,\n" + 
			"       sum(jhjc.quany_che) quany_che,\n" + 
			"       sum(jhjc.bucjh_dun) bucjh_dun,\n" + 
			"       sum(jhjc.bucjh_che) bucjh_che,\n" + 
			"       sum(temp.dr_laimsl) dr_laimsl,\n" + 
			"       sum(temp.dr_ches) dr_ches,\n" + 
			"       sum(temp.lj_laimsl) lj_laimsl,\n" + 
			"       sum(temp.lj_ches) lj_ches,\n" + 
			"       sum(jhjc.dangr_dun - temp.dr_laimsl) as weiwc_dr_dun,\n" + 
			"       sum(jhjc.quany_dun - temp.lj_laimsl) as weiwc_lj_dun,\n" + 
			"       sum(jhjc.quany_dun + jhjc.bucjh_dun - temp.lj_laimsl) as weiwc_lj_dun_bc\n" + 
			"  from yunsjhjcb jhjc, gongysb gys, chezxxb fz,\n" + 
			"       (select lj.id,\n" + 
			"               lj.gongysb_id,\n" + 
			"               lj.faz_id,\n" + 
			"               dr.laimsl dr_laimsl,\n" + 
			"               dr.ches dr_ches,\n" + 
			"               lj.laimsl lj_laimsl,\n" + 
			"               lj.ches lj_ches\n" + 
			"          from (select jh.id,\n" + 
			"                       gys.id || fz.id as newid,\n" + 
			"                       gys.id gongysb_id,\n" + 
			"                       fz.id faz_id,\n" + 
			"                       sum(fh.laimsl) laimsl,\n" + 
			"                       sum(fh.ches) ches\n" + 
			"                  from yunsjhjcb jh, gongysb gys, chezxxb fz, fahb fh\n" + 
			"                 where jh.gongysb_id = gys.id\n" + 
			"                   and jh.faz_id = fz.id\n" + 
			"                   and jh.riq = to_date('"+ getDaohrq() +"', 'yyyy-mm-dd')\n" + 
			"                   and jh.gongysb_id = fh.gongysb_id(+)\n" + 
			"                   and jh.faz_id = fh.faz_id(+)\n" + 
			"                   and fh.daohrq(+) = to_date('"+ getDaohrq() +"', 'yyyy-mm-dd')\n" + 
			"                   and fh.diancxxb_id(+) = "+ getTreeid() +"\n" + 
			"                 group by (jh.id, gys.id, fz.id)\n" + 
			"                union\n" + 
			"                select jh.id,\n" + 
			"                       gys.id || fz.id as newid,\n" + 
			"                       gys.id gongysb_id,\n" + 
			"                       fz.id faz_id,\n" + 
			"                       sum(fh.laimsl) laimsl,\n" + 
			"                       sum(fh.ches) ches\n" + 
			"                  from yunsjhjcb jh, gongysb gys, chezxxb fz, fahb fh\n" + 
			"                 where fh.gongysb_id = gys.id\n" + 
			"                   and fh.faz_id = fz.id\n" + 
			"                   and jh.riq(+) = to_date('"+ getDaohrq() +"', 'yyyy-mm-dd')\n" + 
			"                   and jh.gongysb_id(+) = fh.gongysb_id\n" + 
			"                   and jh.faz_id(+) = fh.faz_id\n" + 
			"                   and fh.daohrq = to_date('"+ getDaohrq() +"', 'yyyy-mm-dd')\n" + 
			"                   and fh.diancxxb_id = "+ getTreeid() +"\n" + 
			"                 group by (jh.id, gys.id, fz.id)) dr,\n" + 
			"               (select jh.id,\n" + 
			"                       gys.id || fz.id as newid,\n" + 
			"                       gys.id gongysb_id,\n" + 
			"                       fz.id faz_id,\n" + 
			"                       sum(fh.laimsl) laimsl,\n" + 
			"                       sum(fh.ches) ches\n" + 
			"                  from yunsjhjcb jh, gongysb gys, chezxxb fz, fahb fh\n" + 
			"                 where jh.gongysb_id = gys.id\n" + 
			"                   and jh.faz_id = fz.id\n" + 
			"                   and jh.riq = to_date('"+ getDaohrq() +"', 'yyyy-mm-dd')\n" + 
			"                   and jh.gongysb_id = fh.gongysb_id(+)\n" + 
			"                   and jh.faz_id = fh.faz_id(+)\n" + 
			"                   and to_char(fh.daohrq(+), 'yyyy-mm') = '"+ getDaohrq().split("-")[0] +"-"+ getDaohrq().split("-")[1] +"'\n" + 
			"                   and fh.diancxxb_id(+) = "+ getTreeid() +"\n" + 
			"                 group by (jh.id, gys.id, fz.id)\n" + 
			"                union\n" + 
			"                select jh.id,\n" + 
			"                       gys.id || fz.id as newid,\n" + 
			"                       gys.id gongysb_id,\n" + 
			"                       fz.id faz_id,\n" + 
			"                       sum(fh.laimsl) laimsl,\n" + 
			"                       sum(fh.ches) ches\n" + 
			"                  from yunsjhjcb jh, gongysb gys, chezxxb fz, fahb fh\n" + 
			"                 where fh.gongysb_id = gys.id\n" + 
			"                   and fh.faz_id = fz.id\n" + 
			"                   and jh.riq(+) = to_date('"+ getDaohrq() +"', 'yyyy-mm-dd')\n" + 
			"                   and jh.gongysb_id(+) = fh.gongysb_id\n" + 
			"                   and jh.faz_id(+) = fh.faz_id\n" + 
			"                   and to_char(fh.daohrq, 'yyyy-mm') = '"+ getDaohrq().split("-")[0] +"-"+ getDaohrq().split("-")[1] +"'\n" + 
			"                   and fh.diancxxb_id = "+ getTreeid() +"\n" + 
			"                 group by (jh.id, gys.id, fz.id)) lj\n" + 
			"         where dr.newid(+) = lj.newid) temp\n" + 
			" where jhjc.id(+) = temp.id\n" + 
			"   and temp.gongysb_id = gys.id\n" + 
			"   and temp.faz_id = fz.id\n" + 
			"group by rollup (jhjc.id, gys.mingc, fz.mingc)\n" + 
			"having not (grouping(jhjc.id) + grouping(gys.mingc) = 1)";

		String[][] ArrHeader = new String[3][15];
		ArrHeader[0] = new String[]{"单位","发站","运输计划","运输计划","运输计划","运输计划","补充计划","补充计划","实际完成","实际完成","实际完成","实际完成","未完成","未完成","未完成含</br>补充计划"};
		ArrHeader[1] = new String[]{"单位","发站","当日","当日","全月","全月","补充计划","补充计划","当日","当日","累计","累计","当日","累计","未完成含</br>补充计划"};
		ArrHeader[2] = new String[]{"单位","发站","吨","车","吨","车","吨","车","吨","车","吨","车","吨","吨","未完成含</br>补充计划"};
		
		int[] ArrWidth = new int[] {150, 90, 60, 60, 70, 70, 70, 70, 60, 60, 70, 70, 70, 70, 90};
		
		ResultSetList rslData =  con.getResultSetList(sql);
		rt.setTitle("月运输计划到货率查询", ArrWidth);
		rt.setBody(new Table(rslData, 3, 0, 1));
		if (rslData.getRows() > 0) {
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 2);
		}
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.setDefaultTitle(1, 5, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 4, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 4, "制表：", Table.ALIGN_LEFT);
		
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
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel())
			.getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("日期："));
		DateField df = new DateField();
		df.setValue(getDaohrq());
		df.setId("daohrq");
		df.Binding("Daohrq", "forms[0]");
//		df.setListeners("change:function(){document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
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
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			setDaohrq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}