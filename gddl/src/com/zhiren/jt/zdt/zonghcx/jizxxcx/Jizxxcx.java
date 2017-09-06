package com.zhiren.jt.zdt.zonghcx.jizxxcx;

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
	import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.ToolbarButton;
	import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
	import com.zhiren.common.ext.form.TextField;
	import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

	public class Jizxxcx extends BasePage implements PageValidateListener {
		private String msg = "";

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		// 页面变化记录
		private String Change;

		public String getChange() {
			return Change;
		}

		public void setChange(String change) {
			Change = change;
		}

		public int getDataColumnCount() {
			int count = 0;
			for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
				if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
					count++;
				}
			}
			return count;
		}

		private boolean _RefreshChick = false;

		public void RefreshButton(IRequestCycle cycle) {
			_RefreshChick = true;
		}
		
		public void submit(IRequestCycle cycle) {
			if (_RefreshChick) {
				_RefreshChick = false;
				getSelectData();
			}
		}
		public void getSelectData() {
			JDBCcon con = new JDBCcon();
			String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = " and jib=3 ";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				str = " and dc.fuid = "+ getTreeid() + "";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = " and dc.id = " + getTreeid() + "";
			}
			String sql ="select * from (select j.id,dc.mingc as diancxxb_id,j.jizbh,j.jizurl,j.shejmz,j.qnet_ar,j.aad,j.vdaf,j.st_d,j.st,\n" +
				"       j.meihl,j.rijhm,j.jihdl,j.cansmz,j.cansbl,j.toucrq,j.zhizs\n" + 
				"from jizb j,diancxxb dc where j.diancxxb_id=dc.id "+str+" order by dc.mingc,j.jizbh)";

			ResultSetList rsl = con.getResultSetList(sql);
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("meicb");
			egu.setWidth(990);
			egu.getColumn("id").setHeader("id");
			egu.getColumn("id").setEditor(null);
			egu.getColumn("id").setHidden(true);
			egu.getColumn("diancxxb_id").setHeader("单位名称");
			egu.getColumn("diancxxb_id").setEditor(null);
			egu.getColumn("jizbh").setHeader("机组号");
			egu.getColumn("jizbh").setEditor(null);
			egu.getColumn("jizurl").setHeader("容量(mw/h)");
			egu.getColumn("jizurl").setEditor(null);
			egu.getColumn("shejmz").setHeader("设计煤种");
			egu.getColumn("shejmz").setEditor(null);
			egu.getColumn("qnet_ar").setHeader("热值Qnet_ar[MJ/kg]");
			egu.getColumn("qnet_ar").setEditor(null);
			egu.getColumn("aad").setHeader("灰份Aad[%]");
			egu.getColumn("aad").setEditor(null);
			egu.getColumn("vdaf").setHeader("挥发份Vdaf[%]");
			egu.getColumn("vdaf").setEditor(null);
			egu.getColumn("st_d").setHeader("硫份St，d[%]");
			egu.getColumn("st_d").setEditor(null);
			egu.getColumn("st").setHeader("灰熔点ST[℃]");
			egu.getColumn("st").setEditor(null);
			egu.getColumn("meihl").setHeader("煤耗率（克/千瓦时）");
			egu.getColumn("meihl").setEditor(null);
			egu.getColumn("rijhm").setHeader("日均耗煤(吨)");
			egu.getColumn("rijhm").setEditor(null);
			egu.getColumn("jihdl").setHeader("计划电量(亿度)");
			egu.getColumn("jihdl").setEditor(null);
			egu.getColumn("cansmz").setHeader("掺烧煤种");
			egu.getColumn("cansmz").setEditor(null);
			egu.getColumn("cansbl").setHeader("掺配比例");
			egu.getColumn("cansbl").setEditor(null);
			egu.getColumn("toucrq").setHeader("投产日期");
			egu.getColumn("toucrq").setEditor(null);
			egu.getColumn("zhizs").setHeader("制造商");
			egu.getColumn("zhizs").setEditor(null);
//			egu.getColumn("chak").setHeader("查看");
//			egu.getColumn("chak").setWidth(100);
//			egu.getColumn("chak").setEditor(null);
//			egu.getColumn("chak").setRenderer(
//					"function(value,p,record){" +rsl.getString("shebzp")+
//					";return \"<a href=# onclick=window.open('\"http://www.baidu.con\"&flag=1','_self')>查看</a>\"}"
//			);
			
			egu.getColumn("diancxxb_id").setWidth(100);
	
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addPaging(25);

//			 工具栏
			// 电厂树
			egu.addTbarText("单位名称:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			egu.addTbarText("-");

			
			StringBuffer rsb = new StringBuffer();
			rsb.append("function (){")
			.append("document.getElementById('RefreshButton').click();}");
			GridButton gbr = new GridButton("刷新",rsb.toString());
			gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addTbarBtn(gbr);
			
		
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
			}
			getSelectData();
		}
		
		boolean treechange = false;

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