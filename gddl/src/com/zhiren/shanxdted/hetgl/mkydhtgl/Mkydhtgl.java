package com.zhiren.shanxdted.hetgl.mkydhtgl;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.ext.tree.Tree;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreePanel;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Mkydhtgl extends BasePage implements PageValidateListener {



//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "";
		if (visit.isFencb()) {
			sql = "select id,mingc from diancxxb where fuid=" + visit.getDiancxxb_id()+ " order by id";
		} else {
			sql = "select id,mingc from diancxxb where id=" + visit.getDiancxxb_id();
		}
		
		setDiancmcModel(new IDropDownModel(sql));
	}
	
	public String getTreeid_dc() {
		Visit visit = (Visit) this.getPage().getVisit();
		String treeid = visit.getString3();
		if (treeid == null || treeid.equals("")) {
			if (visit.isFencb()) {
				visit.setString3("" + ((IDropDownBean)getDiancmcModel().getOption(0)).getId());
			} else {
				visit.setString3(String.valueOf(visit.getDiancxxb_id()));
			}
		}
		return ((Visit) getPage().getVisit()).getString3();
	}
	
	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	
	public DefaultTree getTree_dc() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree_dc(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
//		((DateField) getToolbar().getItem("daohrq")).setValue(getRiq());
		return getToolbar().getScript();
	}
	
	public Tree getTree() {
		return ((Visit) this.getPage().getVisit()).getTree1();
	}
	public void setTree(Tree tree) {
		((Visit) this.getPage().getVisit()).setTree1(tree);
	}
	public String getNavTreeScript() {
		return getTree().getScript();
	}
	
	public void setClickEveScript(String script) {
		((Visit) this.getPage().getVisit()).setString4(script);
	}
	
	public String getClickEveScript() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}
	
//	public void setBriq(String value) {
//		Visit visit = (Visit)getPage().getVisit();
//		visit.setString5(value);
//	}
//	
//	public String getBriq() {
//		Visit visit = (Visit)getPage().getVisit();
//		if ("".equals(visit.getString5()) || visit.getString5()==null) {
//			setBriq(DateUtil.FormatDate(new Date()));
//		}
//		return visit.getString5();
//	}
//	
//	public void setEriq(String value) {
//		Visit visit = (Visit)getPage().getVisit();
//		visit.setString6(value);
//	}
//	
//	public String getEriq() {
//		Visit visit = (Visit)getPage().getVisit();
//		if ("".equals(visit.getString6()) || visit.getString6()==null) {
//			setEriq(DateUtil.FormatDate(new Date()));
//		}
//		return visit.getString6();
//	}
	
	private void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar(null);	
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null, getTreeid_dc());
		
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);			
		tb1.addText(new ToolbarText("-"));	
//		tb1.addText(new ToolbarText("合同日期:"));
//		DateField dStart = new DateField();
//		dStart.Binding("Briq","");
//		dStart.setValue(getBriq());
//		tb1.addField(dStart);
//		tb1.addText(new ToolbarText("至"));
//		DateField dEnd = new DateField();
//		dEnd.Binding("Eriq","");
//		dEnd.setValue(getEriq());
//		tb1.addField(dEnd);
//		tb1.addText(new ToolbarText("-"));
		//刷新按钮
		ToolbarButton refurbish = new ToolbarButton(null, "刷新",
		"function (){document.getElementById('RefreshButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(refurbish);
		setToolbar(tb1);
		
		TreeNode RootNode = new TreeNode("_root", "合同");
		StringBuffer sb = new StringBuffer();
		StringBuffer clickEve = new StringBuffer();
		sb.append(
				"SELECT DISTINCT TO_CHAR(QISRQ, 'yyyy') AS NIANF_ID,TO_CHAR(QISRQ, 'yyyy') AS NIANF\n" +
				"  FROM HETB HT\n" + 
				"WHERE HT.DIANCXXB_ID = " + this.getTreeid_dc() + "\n" +
				" ORDER BY TO_CHAR(QISRQ, 'yyyy')");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			TreeNode Node = new TreeNode(rsl.getString("NIANF_ID"),rsl.getString("NIANF")+ "年");
			Node.setLeaf(false);
			RootNode.appendChild(Node);
			setChildNode(con, Node, rsl.getString("nianf"), clickEve);
		}
//		sb.delete(0, sb.length());
		setClickEveScript(clickEve.toString());
		
		TreePanel tp = new TreePanel("NavTree");	
		tp.setHeight("bodyHeight");
		tp.setTreeRootNodeid(RootNode.getId());
		tp.setEnableDD(true);
		tp.setRootVisible(false);
		Tree tree = new Tree("NavTree",false,RootNode,tp);
//		if (!"".equals(visit.getString10()) || visit.getString10()!=null) {
//			tree.setSelectedNodeid(visit.getString10());
//		}
		setTree(tree);
		con.Close();
	}
	
	public void setChildNode(JDBCcon con,TreeNode ParentNode, String nianf, StringBuffer ebuffer) {
		StringBuffer csb = new StringBuffer();
		
		csb.append(
				"SELECT DISTINCT TO_CHAR(QISRQ, 'mm') AS YUEF_ID, TO_CHAR(QISRQ, 'mm') AS YUEF\n" +
				"  FROM HETB HT\n" + 
				" WHERE TO_CHAR(QISRQ, 'yyyy') = '" + nianf + 
				"' AND HT.DIANCXXB_ID = " + this.getTreeid_dc() + " \n" + 
				" ORDER BY TO_CHAR(QISRQ, 'mm')");
		ResultSetList crs = con.getResultSetList(csb.toString());
		while(crs.next()) {
			TreeNode Node = new TreeNode(nianf + crs.getString("YUEF_ID"),crs.getString("YUEF")+"月");
			Node.setLeaf(false);
			ParentNode.appendChild(Node);
			setLastChildNode(con, Node, nianf, crs.getString("YUEF"), ebuffer);
		}
		
	}
	
	public void setLastChildNode(JDBCcon con,TreeNode ParentNode, String nianf, String yuef, StringBuffer ebuff) {
		StringBuffer csb = new StringBuffer();
		
		csb.append(
				"SELECT DISTINCT TO_CHAR(QISRQ, 'yyyymmdd')||TO_CHAR(GUOQRQ, 'yyyymmdd') AS ID,\n" +
				"       TO_CHAR(QISRQ, 'yyyy-mm-dd') AS QISRQ,\n" + 
				"       TO_CHAR(GUOQRQ, 'yyyy-mm-dd') AS GUOQRQ,\n" + 
				"       TO_CHAR(QISRQ, 'yyyy-mm-dd') || '至' ||\n" + 
				"       TO_CHAR(GUOQRQ, 'yyyy-mm-dd') AS TEXT\n" + 
				"  FROM HETB HT\n" + 
				" WHERE TO_CHAR(QISRQ, 'yyyy')= '" + nianf + 
				"' AND TO_CHAR(QISRQ, 'mm') = '" + yuef + 
				"' AND HT.DIANCXXB_ID = " + this.getTreeid_dc()+ "\n" + 
				" ORDER BY TEXT");
		ResultSetList crs = con.getResultSetList(csb.toString());
		while(crs.next()) {
			TreeNode Node = new TreeNode(crs.getString("ID"),crs.getString("TEXT"));
			ParentNode.appendChild(Node);
			Node.setLeaf(false);
			setLeafChildNode(con, Node, nianf, yuef, crs.getString("qisrq"), 
					crs.getString("guoqrq"), crs.getString("id"), ebuff);
		}
	}
	
	public void setLeafChildNode(JDBCcon con,TreeNode ParentNode, String nianf, String yuef,
			String qisrq, String guoqrq, String rqid, StringBuffer ebuff) {
		
		StringBuffer csb = new StringBuffer();		
		csb.append(
				"SELECT DISTINCT HT.ID,\n" +
				"       HT.HETBH || '【' ||MEIKMCS || '】'AS TEXT\n" +  
				"  FROM HETB HT\n" + 
				" WHERE TO_CHAR(QISRQ, 'yyyy-mm-dd')= '" + qisrq + 
				"' AND TO_CHAR(GUOQRQ, 'yyyy-mm-dd') = '" + guoqrq + 
				"' AND HT.DIANCXXB_ID = " + this.getTreeid_dc()+ "\n" + 
				" ORDER BY TEXT");
		ResultSetList crs = con.getResultSetList(csb.toString());
		while(crs.next()) {
			TreeNode Node = new TreeNode(crs.getString("ID"),crs.getString("TEXT"));
			ParentNode.appendChild(Node);
			
			//添加双击事件
			ebuff.append("\n");
			ebuff.append("NavTree_root"+nianf+nianf+yuef+rqid+crs.getString("ID")+".on('click',function(node,e){\n");
//			ebuff.append("document.all.myhFrame.src =\"http://\"+document.location.host+document.location.pathname + \"?service=page/Mkydht&lx=" + crs.getString("ID") 
//					+ "&riq=" + crs.getString("TEXT") + "\";");
			ebuff.append("document.all.myhFrame.src =\"http://\"+document.location.host+document.location.pathname + \"?service=page/Mkydht&lx=" + crs.getString("ID") + "\";");
			ebuff.append("});\n");
		}
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;	
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(null);
	}
	
	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit)getPage().getVisit();
		visit.setString10(null);
		if (_RefreshChick) {
			_RefreshChick = false;		
		}
		getSelectData();
	}
	
	//	页面登陆验证
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
			visit.setString1(null);
			visit.setString3(null);
			visit.setString4(null);
//			visit.setString5(null);
//			visit.setString6(null);
//			visit.setString10(null);
			visit.setProSelectionModel2(null);
			visit.setDefaultTree(null);
			visit.setTree1(null);
			visit.setToolbar(null);
			getDiancmcModels();
		}			
		getSelectData();
	}
}
