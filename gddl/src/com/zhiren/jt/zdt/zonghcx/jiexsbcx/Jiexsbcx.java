package com.zhiren.jt.zdt.zonghcx.jiexsbcx;

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
/*
 * 作者:tzf
 * 时间：2009-5-4
 * 修改内容:增加图片查看功能
 */
	public class Jiexsbcx extends BasePage implements PageValidateListener {
		private String msg = "";

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = MainGlobal.getExtMessageBox(msg, false);
			;
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

		public String getTup(){
			Visit visit= (Visit) getPage().getVisit();
			JDBCcon con = new JDBCcon();
			String str = "";
			String url="";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = " and jib=3 ";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				str = " and dc.fuid = "+ getTreeid() + "";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = " and dc.id = " + getTreeid() + "";
			}
			String sql= "select sb.id,sb.diancxxb_id,sb.shebzp " +
			"		 from jiexsbb sb,diancxxb dc where sb.diancxxb_id=dc.id "+str;
			ResultSetList rslt=con.getResultSetList(sql);
			try{
			while(rslt.next()){
				url=rslt.getString("shebzp");
			}
			}catch(Exception e){
				e.getStackTrace();
			}finally{
				con.Close();
			}
			return url;
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
//			String sql = "select * from (select sb.id,sb.diancxxb_id,dc.mingc as diancmc,sb.mingc,sb.bianh,sb.xiangm,sb.shebcs,sb.shuz,'查看' as chak, " +
//			"case when (sb.shebzp like '%.%') then sb.shebzp else 'null' end as shebzp \n" +
//					"		 from jiexsbb sb,diancxxb dc where sb.diancxxb_id=dc.id "+str+" order by dc.mingc,sb.mingc,sb.bianh)";
			
			String sql = "select * from (select sb.id,sb.diancxxb_id,dc.mingc as diancmc,sb.mingc,sb.bianh,sb.xiangm,sb.shebcs,sb.shuz,'<a href=\""+MainGlobal.getHomeContext(this)+"/app?service=page/ImageReport&&id='||sb.id||'&&mk=shebxx\" target=\"_blank\">查看</a>' as chak, " +
			"case when (sb.shebzp like '%.%') then sb.shebzp else 'null' end as shebzp \n" +
					"		 from jiexsbb sb,diancxxb dc where sb.diancxxb_id=dc.id "+str+" order by dc.mingc,sb.mingc,sb.bianh)";
			
			ResultSetList rsl = con.getResultSetList(sql);

			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("jiexsbb");
			egu.getColumn("id").setHeader("id");
			egu.getColumn("id").setEditor(null);
			egu.getColumn("id").setHidden(true);
			egu.getColumn("diancxxb_id").setHeader("diancxxb_id");
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setEditor(null);
			egu.getColumn("diancmc").setHeader("单位名称");
			egu.getColumn("diancmc").setEditor(null);
			
			egu.getColumn("mingc").setHeader("设备名称");
			egu.getColumn("mingc").setEditor(null);

			egu.getColumn("bianh").setHeader("设备编号");
			egu.getColumn("bianh").setEditor(null);
			egu.getColumn("xiangm").setHeader("项目");
			egu.getColumn("xiangm").setEditor(null);
			
			egu.getColumn("shebcs").setHeader("设备参数");
			egu.getColumn("shebcs").setEditor(null);
			egu.getColumn("shuz").setHeader("数值");
			egu.getColumn("shuz").setEditor(null);
			
			egu.getColumn("chak").setHeader("查看");
			egu.getColumn("chak").setWidth(100);
			egu.getColumn("chak").setEditor(null);
			
			egu.getColumn("shebzp").setHeader("图片名称");
			egu.getColumn("shebzp").setEditor(null);
			egu.getColumn("shebzp").setHidden(true);

//			String str1=
//	       		" var url1 = 'http://'+document.location.host+document.location.pathname;"+
//	            " var end1 = url1.indexOf(';');"+
//	            " var imgName = record.data['SHEBZP'];"+
//				" url1 = url1.substring(0,end1);"+
//	       	    " url1 = url1 + '?service=page/' + 'Tupzs&jiexsbb_id='+record.data['ID'];";
//			egu.getColumn("chak").setRenderer(
//					"function(value,p,record){" +str1+
////					"return \"<a href=# onclick=window.open('\"+url1+\"','_blank')>查看</a>\"}"
//					"return \"<a href=# onclick=if(\"+imgName+\"!=null){window.open('\"+url1+\"','_blank');}else{alert('暂无图片！');}>查看</a>\"}"
//			);

			egu.getColumn("diancmc").setWidth(200);
			
//			egu.getColumn("id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//			egu.getColumn("shebxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//			egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//			egu.getColumn("bianh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//			egu.getColumn("shebmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			
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
			
//			GridButton gbphoto = new GridButton("图片处理","function (){var rec = gridDiv_sm.getSelected();if(!rec){"+MainGlobal.getExtMessageBox("请选择设备查看对应的图片", false)+"\n return;}"+MainGlobal.getOpenWinScript("ImageUpLoad&mk=shebxx&id='+rec.get(\"ID\")+'")+ "}");
//			gbphoto.setIcon(SysConstant.Btn_Icon_Show);
//			egu.addTbarBtn(gbphoto);
			
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
				//this.setTreeid(null);
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