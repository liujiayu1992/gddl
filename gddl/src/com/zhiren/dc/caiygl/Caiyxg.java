package com.zhiren.dc.caiygl;

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
/*
 * 作者：王磊
 * 时间：2009-10-29
 * 描述：增加参数判断化验二级审核之后不允许修改采样
 * 数据库：xitxxb	mingc = '进场批次修改判断锁定'
 * 				leib = '采样'
 * 				beiz = '使用'
 * 				zhuangt = 1
 * 				zhi	= '是'
 */
/*
 * 作者：王磊
 * 时间：2009-10-30
 * 描述：修改zhilb判断shenhzt = 1  原判断shenhzt = 7 有误
 */
/**
 * huochaoyuan
 * 2010-01-16,根据电厂需求，且因为车次为一条fahb信息的唯一属性，
 * 故在进厂批号修改页面显示的条目上加上车次信息，添加方法为java页面添加列显示
 */
public class Caiyxg extends BasePage {
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
	
	private boolean isLock(JDBCcon con,String zhilbid){
		boolean isLock = false;
		String sql = "select * from xitxxb where mingc = '进场批次修改判断锁定' and leib = '采样' and zhuangt = 1 and zhi='是' and beiz='使用'";
		if(con.getHasIt(sql)){
			sql = "select * from zhilb where id =" + zhilbid + " and shenhzt = 1";
			isLock = con.getHasIt(sql);
		}
		return isLock;
	}

//	刷新车皮衡单对应列表
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		TreeNode RootNode = new TreeNode("_root", "采样编码");
		StringBuffer sb = new StringBuffer();
		sb.append("select zhilb_id,bianm,id from caiyb where caiyrq = ")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append(" order by bianm");
				//.append(" union (select zhilb_id,'无关联',-1 from fahb minus select zhilb_id,'无关联',-1 from caiyb)");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			TreeNode Node = new TreeNode(rsl.getString("zhilb_id"),rsl.getString("bianm"));
			Node.setLeaf(false);
			RootNode.appendChild(Node);
			setChildNode(con,Node);
		}
		sb.delete(0, sb.length());
		sb.append("select f.id ,m.mingc||' '||p.mingc||' '||to_char(f.fahrq,'yyyy-mm-dd')||'发货 '||\n ")
/**
 * huochaoyuan 2009-12-28
 * 添加条目的‘车次’列，考虑到车次为一条fahb信息的唯一属性，且由于运输单位无法体现，故用车次来区别，迎合某些电厂需要看到运输单位信息的需求
 * （运输单位名车后缀号码，检斤时车次填入该号码，一次方法区别运输单位分样）
 */		
		.append("to_char(f.daohrq,'yyyy-mm-dd')||'到货 车次'||f.chec||' 共'||f.ches||'车 票重'||f.biaoz||'吨' as text \n")
//end		
		.append("from fahb f, meikxxb m, pinzb p  where f.zhilb_id in (select zhilb_id from fahb minus select zhilb_id from caiyb)")
		.append(" and f.pinzb_id = p.id ").append(" and f.meikxxb_id = m.id");
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
//		df.setId("guohrq");
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
			setTbmsg("今天还没有采样编码！");
		}else {
			if("0".equals(RootNode.getLastChild().getId())) {
				setTbmsg("今天共有 "+(RootNode.getChildNodes().size()-1)+" 个采样编码，另有 "+RootNode.getLastChild().getChildNodes().size()+" 个发货未匹配到采样！");
			}else {
				setTbmsg("今天共有 "+RootNode.getChildNodes().size()+" 个采样编码");
			}
		}
	}
	public void setChildNode(JDBCcon con,TreeNode ParentNode) {
		StringBuffer csb = new StringBuffer();
		csb.append("select f.id ,m.mingc||' '||p.mingc||' '||to_char(f.fahrq,'yyyy-mm-dd')||'发货 '||\n ")
/**
 * huochaoyuan 2009-12-28
 * 添加条目的‘车次’列，考虑到车次为一条fahb信息的唯一属性，且由于运输单位无法体现，故用车次来区别，迎合某些电厂需要看到运输单位信息的需求
 * （运输单位名车后缀号码，检斤时车次填入该号码，一次方法区别运输单位分样）
 */		
		.append("to_char(f.daohrq,'yyyy-mm-dd')||'到货 车次'||f.chec||' 共'||f.ches||'车 票重'||f.biaoz||'吨' as text \n")
//end		
		.append("from fahb f, meikxxb m, pinzb p  where f.zhilb_id = ").append(ParentNode.getId())
		.append(" and f.pinzb_id = p.id ").append(" and f.meikxxb_id = m.id");
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
		JDBCcon con = new JDBCcon();
		long zhilb_id = Jilcz.insertCaiyb(con, DateUtil.getDate(getRiq()), ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if(zhilb_id ==-1) {
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
		if(isLock(con,getChange())){
			setMsg("失败！该数据已被锁定，不能删除！");
			con.Close();
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from caiyb where zhilb_id =").append(getChange());
		int flag = con.getDelete(sb.toString());
		if(flag == -1) {
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
		String msg = "";
		if(getChange()==null || "".equals(getChange())) {
			setMsg("记录未被改动无需保存！");
		}
		StringBuffer sb = new StringBuffer();
		String[] recs = getChange().split(";");
		for(int i =0;i<recs.length;i++) {
			String[] rec = recs[i].split(",",2);
			if(isLock(con,rec[1])){
				msg = "失败！数据已被锁定，不能更新！";
				continue;
			}
			if(isLock(con,rec[0])){
				msg = "失败！数据已被锁定，不能更新！";
				continue;
			}
			sb.delete(0, sb.length());
			sb.append("update fahb set zhilb_id=").append(rec[1]).append(" where id=").append(rec[0]);
			int flag = con.getInsert(sb.toString());
			if(flag == -1) {
				setMsg("更新发货失败！错误代码 CYXG-003");
				con.rollBack();
				con.Close();
				return;
			}
			sb.delete(0, sb.length());
			sb.append("select * from fahb where id =" + rec[0]);
			ResultSetList rs = con.getResultSetList(sb.toString());
			if(rs.next()){
				long jihkjid = SysConstant.JIHKJ_NONE;
				if(rs.getString("jihkjb_id") != null) {
					jihkjid = rs.getLong("jihkjb_id");
				}
				long fazid = SysConstant.Chez_q;
				if(rs.getString("faz_id") != null) {
					fazid = rs.getLong("faz_id");
				}
				long yunsfs = rs.getInt("yunsfsb_id");
				flag = Caiycl.CreatBianh(con,Long.valueOf(rec[1]).longValue(),v.getDiancxxb_id(),rs.getLong("meikxxb_id"),jihkjid,yunsfs,fazid);
				if(flag == -1) {
					setMsg("更新发货失败！错误代码 CYXG-004");
					con.rollBack();
					con.Close();
					return;
				}
			}
			
		}
		con.commit();
		con.Close();
		if("".equals(msg)){
			setMsg("保存成功！");
		}else{
			setMsg(msg);
		}
	}
	
	private boolean _UpdateChick = false;
	public void UpdateButton(IRequestCycle cycle) {
		_UpdateChick = true;
	}
	private void Update(){
		if(getChange()==null || "".equals(getChange())) {
			setMsg("记录未被改动无需保存！");
		}
		JDBCcon con = new JDBCcon();
		if(isLock(con,getChange())){
			setMsg("失败！该数据已被锁定，不能更新！");
			con.Close();
			return;
		}
		String[] str = getChange().split(",");
		String sql = "update caiyb set bianm = '" + 
		str[1] + "' where zhilb_id= " +str[0];
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