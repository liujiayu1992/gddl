package com.zhiren.shihs.caiygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.Tree;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreePanel;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;

public class Shihcyxg extends BasePage {
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

//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	刷新车皮衡单对应列表
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		TreeNode RootNode = new TreeNode("_root", "采样编码");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ID,bianm FROM shihcyb WHERE caiysj=")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append(" order by bianm");

		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			TreeNode Node = new TreeNode(rsl.getString("id"),rsl.getString("bianm"));
			Node.setLeaf(false);
			RootNode.appendChild(Node);
			setChildNode(con,Node);
		}
		sb.delete(0, sb.length());
		sb.append(
				"SELECT ID,\n" +
				"       (SELECT MINGC FROM SHIHGYSB WHERE ID = CP.GONGYSB_ID) || ' ' ||\n" + 
				"       (SELECT MINGC FROM SHIHPZB WHERE ID = CP.SHIHPZB_ID) || ' ' ||\n" + 
				"       TO_CHAR(CP.DAOHRQ, 'yyyy-mm-dd') || '到货 ' || CP.CHEPH || ' 票重' ||\n" + 
				"       CP.BIAOZ || '吨' AS TEXT\n" + 
				"  FROM SHIHCPB CP" +
				" WHERE SHIHCYB_ID = 0");
		rsl = con.getResultSetList(sb.toString());
		TreeNode Node = null;
		while(rsl.next()) {
			if(rsl.getRow()==0) {
				Node = new TreeNode("0","无关联"); 
				Node.setLeaf(false);
				RootNode.appendChild(Node);
			}
			TreeNode CNode = new TreeNode(rsl.getString("id"),rsl.getString("text"));
			Node.appendChild(CNode);
		}
		Toolbar tbar = new Toolbar(null); 
		tbar.addText(new ToolbarText("采样日期:"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		tbar.addField(df);
		
		ToolbarButton tbtn = new ToolbarButton(null,"刷新","function(){document.getElementById('RefurbishButton').click();}");
		tbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tbtn.setMinWidth(75);
		tbar.addItem(tbtn);
		
		ToolbarButton ibtn = new ToolbarButton(null,"添加","function(){document.getElementById('InsertButton').click();}");
		ibtn.setIcon(SysConstant.Btn_Icon_Insert);
		ibtn.setMinWidth(75);
		tbar.addItem(ibtn);
//		删除操作的js
		sb.delete(0, sb.length());
		sb.append("function(){\n").append("treenode = Tree_treePanel.getSelectionModel().getSelectedNode();\n")
		.append("if(treenode == null || treenode.isLeaf()){\n").append("Ext.MessageBox.alert('提示信息','请选择编号进行删除！');\n")
		.append("return ;}").append("document.getElementById('CHANGE').value = treenode.id;\n")
		.append("document.getElementById('DeleteButton').click();}");
		ToolbarButton dbtn = new ToolbarButton(null,"删除",sb.toString());
		dbtn.setIcon(SysConstant.Btn_Icon_Delete);
		dbtn.setMinWidth(75);
		tbar.addItem(dbtn);
		
//		保存操作的js
		sb.delete(0, sb.length());
		sb.append("function(){\n").append("if(Tree_history == \"\"){\n").append("Ext.MessageBox.alert('提示信息','没有改动无需进行保存！');\n")
		.append("return;} ").append("document.getElementById('CHANGE').value = Tree_history;\n")
		.append("document.getElementById('SaveButton').click();}");
		ToolbarButton sbtn = new ToolbarButton(null,"保存",sb.toString()) ;
		sbtn.setIcon(SysConstant.Btn_Icon_Save);
		sbtn.setMinWidth(75);
		tbar.addItem(sbtn);
		
		
		sb.delete(0, sb.length());
		sb.append("function(){\n").append("treenode = Tree_treePanel.getSelectionModel().getSelectedNode();\n")
		.append("if(treenode == null || treenode.isLeaf() || treenode.id==0){\n").append("Ext.MessageBox.alert('提示信息','请选择编号进行修改！');\n")
		.append("return ;}").append("Rpt_window.show();}");
		ToolbarButton ubtn = new ToolbarButton(null,"修改",sb.toString()) ;
		ubtn.setIcon(SysConstant.Btn_Icon_Copy);
		ubtn.setMinWidth(75);
		tbar.addItem(ubtn);
		
		
		tbar.addFill();
		tbar.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		TreePanel tp = new TreePanel("Tree");
		tp.setHeight("bodyHeight");
		tp.setTreeRootNodeid(RootNode.getId());
		tp.setEnableDD(true);
		tp.setRootVisible(false);
		tp.setTbar(tbar);
		Tree tree = new Tree("Tree",false,RootNode,tp);
		setTree(tree);
		if(RootNode.getChildNodes().size()==0) {
			setMsg("今天还没有采样编码！");
		}else {
			if("0".equals(RootNode.getLastChild().getId())) {
				setMsg("今天共有 "+(RootNode.getChildNodes().size()-1)+" 个采样编码，另有 "+RootNode.getLastChild().getChildNodes().size()+" 个发货未匹配到采样！");
			}else {
				setMsg("今天共有 "+RootNode.getChildNodes().size()+" 个采样编码");
			}
		}
	}
	public void setChildNode(JDBCcon con,TreeNode ParentNode) {
		String csb =

			"SELECT ID,\n" +
			"       (SELECT MINGC FROM SHIHGYSB WHERE ID = CP.GONGYSB_ID) || ' ' ||\n" + 
			"       (SELECT MINGC FROM SHIHPZB WHERE ID = CP.SHIHPZB_ID) || ' ' ||\n" + 
			"       TO_CHAR(CP.DAOHRQ, 'yyyy-mm-dd') || '到货 ' || CP.CHEPH || ' 票重' ||\n" + 
			"       CP.BIAOZ || '吨' AS TEXT\n" + 
			"  FROM SHIHCPB CP" +
			" WHERE SHIHCYB_ID = " + ParentNode.getId();
		ResultSetList crs = con.getResultSetList(csb.toString());
		while(crs.next()) {
			TreeNode Node = new TreeNode(crs.getString("id"),crs.getString("text"));
			ParentNode.appendChild(Node);
		}
	}

//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setTree1(null);
			this.setTreeid(null);
			getSelectData();
		}
	}
	
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	
	public Tree getTree() {
		return ((Visit) this.getPage().getVisit()).getTree1();
	}
	public void setTree(Tree tree) {
		((Visit) this.getPage().getVisit()).setTree1(tree);
	}
	public String getTreeScript() {
		if(getTbmsg()!=null) {
			getTree().getTreePanel().getTbar().deleteItem();
			getTree().getTreePanel().getTbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getTree().getScript();
	}

//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _InsertClick = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertClick = true;
	}
	private void Insert() {
		Visit visit = (Visit)this.getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		
		String sql =
			"INSERT INTO SHIHCYB\n" +
			"VALUES\n" + 
			"  (GETNEWID(" + diancxxb_id + "),\n" + 
			"   SUBSTR(GETSHIHCYBM(TO_DATE('" + getRiq() + "', 'YYYY-MM-DD')), LENGTH(GETSHIHCYBM(TO_DATE('" + getRiq() + "', 'YYYY-MM-DD'))) - 1, 2),\n" + 
			"   GETSHIHCYBM(TO_DATE('" + getRiq() + "', 'YYYY-MM-DD')),\n" + 
			"   GETSHIHCYBM(TO_DATE('" + getRiq() + "', 'YYYY-MM-DD')),\n" + 
			"   TO_DATE('" + getRiq() + "', 'YYYY-MM-DD'),\n" + 
			"   ' ',\n" + 
			"   ' ',\n" + 
			"   ' ',\n" + 
			"   SYSDATE,\n" + 
			"   0,\n" + 
			"   '')";

		int flag = con.getInsert(sql);
		if(flag ==-1) {
			setMsg("添加采样编号失败！错误代码 CYXG-001");
			return;
		}
		setMsg("编号添加成功！");
	}
	
	private boolean _DeleteClick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	private void Delete() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择编号进行删除！");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from shihcyb where id =").append(getChange());
		int flag = con.getDelete(sb.toString());
		if(flag == -1) {
			setMsg("删除采样失败！错误代码 CYXG-002");
			con.rollBack();
			con.Close();
			return;
		}
		sb.delete(0, sb.length());
		sb.append("UPDATE shihcpb SET shihcyb_id=0 WHERE shihcyb_id= " + getChange());
		con.getUpdate(sb.toString());
		if (flag == -1) {
			setMsg("删除采样失败！错误代码 CYXG-002");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setMsg("编号删除成功！");
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		Visit v  = ((Visit) getPage().getVisit());
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		if(getChange()==null || "".equals(getChange())) {
			setMsg("记录未被改动无需保存！");
		}
		StringBuffer sb = new StringBuffer();
		String[] recs = getChange().split(";");
		for(int i =0;i<recs.length;i++) {
			String[] rec = recs[i].split(",",2);
			sb.delete(0, sb.length());
			sb.append("update shihcpb set shihcyb_id=").append(rec[1]).append(" where id=").append(rec[0]);
			int flag = con.getInsert(sb.toString());
			if(flag == -1) {
				setMsg("更新发货失败！错误代码 CYXG-003");
				con.rollBack();
				con.Close();
				return;
			}		
		}
		con.commit();
		con.Close();
		setMsg("保存成功！");
	}
	
	private boolean _UpdateChick = false;
	public void UpdateButton(IRequestCycle cycle) {
		_UpdateChick = true;
	}
	private void Update(){
		if(getChange()==null || "".equals(getChange())) {
			setMsg("记录未被改动无需保存！");
		}
		String[] str = getChange().split(",");
		String sql = "update shihcyb set bianm = '" + 
		str[1] + "' where id= " +str[0];
		JDBCcon con = new JDBCcon();
		con.getUpdate(sql);
		con.Close();
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if(_InsertClick) {
			_InsertClick = false;
			Insert();
			getSelectData();
		}
		if(_DeleteClick) {
			_DeleteClick = false;
			Delete();
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_UpdateChick) {
			_UpdateChick = false;
			Update();
			getSelectData();
		}
	}
}
