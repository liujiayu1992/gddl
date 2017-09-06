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
 * ���ߣ�����
 * ʱ�䣺2009-10-29
 * ���������Ӳ����жϻ���������֮�������޸Ĳ���
 * ���ݿ⣺xitxxb	mingc = '���������޸��ж�����'
 * 				leib = '����'
 * 				beiz = 'ʹ��'
 * 				zhuangt = 1
 * 				zhi	= '��'
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-30
 * �������޸�zhilb�ж�shenhzt = 1  ԭ�ж�shenhzt = 7 ����
 */
/**
 * huochaoyuan
 * 2010-01-16,���ݵ糧��������Ϊ����Ϊһ��fahb��Ϣ��Ψһ���ԣ�
 * ���ڽ��������޸�ҳ����ʾ����Ŀ�ϼ��ϳ�����Ϣ����ӷ���Ϊjavaҳ���������ʾ
 */
public class Caiyxg extends BasePage {
//	�����û���ʾ
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

//	������
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean isLock(JDBCcon con,String zhilbid){
		boolean isLock = false;
		String sql = "select * from xitxxb where mingc = '���������޸��ж�����' and leib = '����' and zhuangt = 1 and zhi='��' and beiz='ʹ��'";
		if(con.getHasIt(sql)){
			sql = "select * from zhilb where id =" + zhilbid + " and shenhzt = 1";
			isLock = con.getHasIt(sql);
		}
		return isLock;
	}

//	ˢ�³�Ƥ�ⵥ��Ӧ�б�
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		TreeNode RootNode = new TreeNode("_root", "��������");
		StringBuffer sb = new StringBuffer();
		sb.append("select zhilb_id,bianm,id from caiyb where caiyrq = ")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append(" order by bianm");
				//.append(" union (select zhilb_id,'�޹���',-1 from fahb minus select zhilb_id,'�޹���',-1 from caiyb)");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			TreeNode Node = new TreeNode(rsl.getString("zhilb_id"),rsl.getString("bianm"));
			Node.setLeaf(false);
			RootNode.appendChild(Node);
			setChildNode(con,Node);
		}
		sb.delete(0, sb.length());
		sb.append("select f.id ,m.mingc||' '||p.mingc||' '||to_char(f.fahrq,'yyyy-mm-dd')||'���� '||\n ")
/**
 * huochaoyuan 2009-12-28
 * �����Ŀ�ġ����Ρ��У����ǵ�����Ϊһ��fahb��Ϣ��Ψһ���ԣ����������䵥λ�޷����֣����ó���������ӭ��ĳЩ�糧��Ҫ�������䵥λ��Ϣ������
 * �����䵥λ������׺���룬���ʱ��������ú��룬һ�η����������䵥λ������
 */		
		.append("to_char(f.daohrq,'yyyy-mm-dd')||'���� ����'||f.chec||' ��'||f.ches||'�� Ʊ��'||f.biaoz||'��' as text \n")
//end		
		.append("from fahb f, meikxxb m, pinzb p  where f.zhilb_id in (select zhilb_id from fahb minus select zhilb_id from caiyb)")
		.append(" and f.pinzb_id = p.id ").append(" and f.meikxxb_id = m.id");
		rsl = con.getResultSetList(sb.toString());
		TreeNode Node = null;
		while(rsl.next()) {
			if(rsl.getRow()==0) {
				Node = new TreeNode("0","�޹���"); 
				Node.setLeaf(false);
				RootNode.appendChild(Node);
			}
			TreeNode CNode = new TreeNode(rsl.getString("id"),rsl.getString("text"));
			Node.appendChild(CNode);
		}
		Toolbar tbar = new Toolbar(null); 
		tbar.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setId("guohrq");
		tbar.addField(df);
		
		ToolbarButton tbtn = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
		tbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tbtn.setMinWidth(75);
		tbar.addItem(tbtn);
		
		ToolbarButton ibtn = new ToolbarButton(null,"���","function(){document.getElementById('InsertButton').click();}");
		ibtn.setIcon(SysConstant.Btn_Icon_Insert);
		ibtn.setMinWidth(75);
		tbar.addItem(ibtn);
//		ɾ��������js
		sb.delete(0, sb.length());
		sb.append("function(){\n").append("treenode = Tree_treePanel.getSelectionModel().getSelectedNode();\n")
		.append("if(treenode == null || treenode.isLeaf()){\n").append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ž���ɾ����');\n")
		.append("return ;}").append("document.getElementById('CHANGE').value = treenode.id;\n")
		.append("document.getElementById('DeleteButton').click();}");
		ToolbarButton dbtn = new ToolbarButton(null,"ɾ��",sb.toString());
		dbtn.setIcon(SysConstant.Btn_Icon_Delete);
		dbtn.setMinWidth(75);
		tbar.addItem(dbtn);
		
//		���������js
		sb.delete(0, sb.length());
		sb.append("function(){\n").append("if(Tree_history == \"\"){\n").append("Ext.MessageBox.alert('��ʾ��Ϣ','û�иĶ�������б��棡');\n")
		.append("return;} ").append("document.getElementById('CHANGE').value = Tree_history;\n")
		.append("document.getElementById('SaveButton').click();}");
		ToolbarButton sbtn = new ToolbarButton(null,"����",sb.toString()) ;
		sbtn.setIcon(SysConstant.Btn_Icon_Save);
		sbtn.setMinWidth(75);
		tbar.addItem(sbtn);
		
		
		sb.delete(0, sb.length());
		sb.append("function(){\n").append("treenode = Tree_treePanel.getSelectionModel().getSelectedNode();\n")
		.append("if(treenode == null || treenode.isLeaf() || treenode.id==0){\n").append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ž����޸ģ�');\n")
		.append("return ;}").append("Rpt_window.show();}");
		ToolbarButton ubtn = new ToolbarButton(null,"�޸�",sb.toString()) ;
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
			setTbmsg("���컹û�в������룡");
		}else {
			if("0".equals(RootNode.getLastChild().getId())) {
				setTbmsg("���칲�� "+(RootNode.getChildNodes().size()-1)+" ���������룬���� "+RootNode.getLastChild().getChildNodes().size()+" ������δƥ�䵽������");
			}else {
				setTbmsg("���칲�� "+RootNode.getChildNodes().size()+" ����������");
			}
		}
	}
	public void setChildNode(JDBCcon con,TreeNode ParentNode) {
		StringBuffer csb = new StringBuffer();
		csb.append("select f.id ,m.mingc||' '||p.mingc||' '||to_char(f.fahrq,'yyyy-mm-dd')||'���� '||\n ")
/**
 * huochaoyuan 2009-12-28
 * �����Ŀ�ġ����Ρ��У����ǵ�����Ϊһ��fahb��Ϣ��Ψһ���ԣ����������䵥λ�޷����֣����ó���������ӭ��ĳЩ�糧��Ҫ�������䵥λ��Ϣ������
 * �����䵥λ������׺���룬���ʱ��������ú��룬һ�η����������䵥λ������
 */		
		.append("to_char(f.daohrq,'yyyy-mm-dd')||'���� ����'||f.chec||' ��'||f.ches||'�� Ʊ��'||f.biaoz||'��' as text \n")
//end		
		.append("from fahb f, meikxxb m, pinzb p  where f.zhilb_id = ").append(ParentNode.getId())
		.append(" and f.pinzb_id = p.id ").append(" and f.meikxxb_id = m.id");
		ResultSetList crs = con.getResultSetList(csb.toString());
		while(crs.next()) {
			TreeNode Node = new TreeNode(crs.getString("id"),crs.getString("text"));
			ParentNode.appendChild(Node);
		}
	}

//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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

//	��ť�ļ����¼�
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
			setMsg("��Ӳ������ʧ�ܣ�������� CYXG-001");
			return;
		}
		setMsg("�����ӳɹ���");
	}
	
	private boolean _DeleteClick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	private void Delete() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ���Ž���ɾ����");
		}
		if(isLock(con,getChange())){
			setMsg("ʧ�ܣ��������ѱ�����������ɾ����");
			con.Close();
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from caiyb where zhilb_id =").append(getChange());
		int flag = con.getDelete(sb.toString());
		if(flag == -1) {
			setMsg("ɾ������ʧ�ܣ�������� CYXG-002");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setMsg("���ɾ���ɹ���");
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
			setMsg("��¼δ���Ķ����豣�棡");
		}
		StringBuffer sb = new StringBuffer();
		String[] recs = getChange().split(";");
		for(int i =0;i<recs.length;i++) {
			String[] rec = recs[i].split(",",2);
			if(isLock(con,rec[1])){
				msg = "ʧ�ܣ������ѱ����������ܸ��£�";
				continue;
			}
			if(isLock(con,rec[0])){
				msg = "ʧ�ܣ������ѱ����������ܸ��£�";
				continue;
			}
			sb.delete(0, sb.length());
			sb.append("update fahb set zhilb_id=").append(rec[1]).append(" where id=").append(rec[0]);
			int flag = con.getInsert(sb.toString());
			if(flag == -1) {
				setMsg("���·���ʧ�ܣ�������� CYXG-003");
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
					setMsg("���·���ʧ�ܣ�������� CYXG-004");
					con.rollBack();
					con.Close();
					return;
				}
			}
			
		}
		con.commit();
		con.Close();
		if("".equals(msg)){
			setMsg("����ɹ���");
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
			setMsg("��¼δ���Ķ����豣�棡");
		}
		JDBCcon con = new JDBCcon();
		if(isLock(con,getChange())){
			setMsg("ʧ�ܣ��������ѱ����������ܸ��£�");
			con.Close();
			return;
		}
		String[] str = getChange().split(",");
		String sql = "update caiyb set bianm = '" + 
		str[1] + "' where zhilb_id= " +str[0];
		con.getUpdate(sql);
		con.Close();
	}

//	����ť�ύ����
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