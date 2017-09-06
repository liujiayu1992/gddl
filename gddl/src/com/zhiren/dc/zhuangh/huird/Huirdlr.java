package com.zhiren.dc.zhuangh.huird;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*����:����
 * ����:2010-7-22 
 * ����:���ӻ��۵�¼�빦��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-09-01
 * �������������۵�¼������еı���ˢ�¹��򣬸�Ϊ��������ʱ����ֱ����ȡ
 */

/*����:��ʤ��
 * ����:2012-09-03 
 * ����:������۵�¼��ʱ��������ںź�С�ں�
 */
public class Huirdlr extends BasePage implements PageValidateListener {
//	�ͻ��˵���Ϣ��
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//	ҳ���ʼ��(ÿ��ˢ�¶�ִ��)
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
	

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getBeginRiq() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setBeginRiq(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getEndRiq() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setEndRiq(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	private void Save() {
		// Visit visit = (Visit) this.getPage().getVisit();
		// visit.getExtGrid1().Save(getChange(), visit);
		// UpdateRulzlID(getRiqi(),visit.getDiancxxb_id());

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		ResultSetList delrs = getExtGrid().getDeleteResultSet(getChange());
		while (delrs.next()){
			con.getDelete("delete from huirdjlb where id = "+delrs.getString("id"));
			con.commit();
			delrs.close();
		}
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		if (rs == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Huirdlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rs.next()){
			String sql ="select * from zhuanmb where zhuanmlb_id=100663 and bianm ='"+rs.getString("bianm")+"'";
			ResultSetList rss = con.getResultSetList(sql);
			if(rss.next()){
				sb.append("begin\n");
				if ("0".equals(rs.getString("id"))) {
					sb.append("insert into huirdjlb(id,songyrq,huayrq,bianm,bianxwd,ruanhwd,banqwd,liudwd,shenhzt,huayy) values (getnewid(");
					sb.append(visit.getDiancxxb_id()).append("),to_date('").append(rs.getString("songyrq")).append("','yyyy-mm-dd'),to_date('");
					sb.append(rs.getString("huayrq")).append("','yyyy-mm-dd'),'").append(rs.getString("bianm")).append("','").append(rs.getString("bianxwd"));
					sb.append("','").append(rs.getString("ruanhwd")).append("','").append(rs.getString("banqwd")).append("','").append(rs.getString("liudwd"));
					sb.append("',1,'").append(rs.getString("huayy")).append("');\n");
				}else{
					sb.append("update huirdjlb set songyrq = to_date('").append(rs.getString("songyrq")).append("','yyyy-mm-dd'),huayrq = to_date('");
					sb.append(rs.getString("huayrq")).append("','yyyy-mm-dd'),bianm ='").append(rs.getString("bianm")).append("',bianxwd ='");
					sb.append(rs.getString("bianxwd")).append("',ruanhwd = '").append(rs.getString("ruanhwd")).append("',banqwd = '");
					sb.append(rs.getString("banqwd")).append("',liudwd ='").append(rs.getString("liudwd")).append("',shenhzt=1\n");
					sb.append("where id = ").append(rs.getString("id")).append(";\n");
				}
				sb.append("end;");
				
				int flag = con.getInsert(sb.toString());
				if(flag==-1){
					setMsg("����ʧ��");
				}else{
					setMsg("����ɹ�");
				}
			}else{
				setMsg("û�д˻�����룬��˶�");
			}
		}
		con.commit();
		con.Close();
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
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		getSelectData();
	}
	
	
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String chaxun = "select id,songyrq,huayrq,bianm,bianxwd,ruanhwd,banqwd,liudwd,huayy from huirdjlb\n"
				+ "   where huayrq >= "+ DateUtil.FormatOracleDate(getBeginRiq()) + "\n"
				+ "   and huayrq <= "+ DateUtil.FormatOracleDate(getEndRiq()) + "\n"
				+ "   and shenhzt = 0"
		        + "   order by huayrq";
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("huirdjlb");
		egu.setWidth("bodyWidth");
		egu.getColumn("songyrq").setHeader("��������");
		egu.getColumn("huayrq").setHeader("��������");
		egu.getColumn("bianm").setHeader("������");
		egu.getColumn("bianxwd").setHeader("�����¶�");
		egu.getColumn("ruanhwd").setHeader("���¶�");
		egu.getColumn("banqwd").setHeader("�����¶�");
		egu.getColumn("liudwd").setHeader("�����¶�");
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("songyrq").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("huayrq").setDefaultValue(DateUtil.FormatDate(new Date()));
//		egu.getColumn("huayy").setDefaultValue(visit.getRenymc());
		egu.getColumn("huayy").setEditor(null);
		//������Ӧ���л��۵㣬����û��д�ĵĻ������г�����

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("bianm").setEditor(c1);
		c1.setEditable(true);
		String MeicbSql1= "SELECT ZHUANMB.ID, BIANM AS MINGC\n" +
			"  FROM ZHUANMB, ZHUANMLB, ZHILLSB\n" + 
			" WHERE ZHUANMB.ZHUANMLB_ID = ZHUANMLB.ID\n" + 
			"   AND ZHUANMLB.JIB = 3\n" + 
			"   AND ZHILLSB_ID = ZHILLSB.ID\n" + 
			"   AND ZHILLSB.HUIRDSF = '��'\n" + 
			"   AND BIANM NOT IN (SELECT BIANM FROM HUIRDJLB)";

		egu.getColumn("bianm").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql1));
		egu.getColumn("bianm").setReturnId(true);
		
		
		// ������
		egu.addTbarText("���鿪ʼ����:");
		DateField df = new DateField();
		df.setValue(this.getBeginRiq());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("�����������:");
		DateField df1 = new DateField();
		df1.setValue(this.getEndRiq());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq");
		egu.addToolbarItem(df1.getScript());
		

		egu.addTbarText("-");

		// ************************************************************
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '������Ա',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('HUAYY',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '������Ա',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		

		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
				+ "row = irow; \n"
				+ "if('HUAYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"if(!win){"+treepanel+Strtmpfunction+"}"
				+"win.show(this);}});\n");

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

//		����ǰ̨��������ʾ������
		ResultSetList dif = con.getResultSetList(MeicbSql1);
		StringBuffer sb=new StringBuffer();
		
		while(dif.next()){
			sb.append(dif.getString("MINGC"));
			sb.append(",");

		}
		
		if(sb.length()>1){
			sb.setLength(sb.length()-1);
			egu.addTbarText("Ӧ��Ļ�����룺"+sb.toString());
		}
		setExtGrid(egu);
		con.Close();
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		���������Ĳ�ѯSQL
		String sql=
			"select 0 id,'��' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='����' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
//		System.out.println(sql);
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setEndRiq(null);
			setBeginRiq(null);
		}
		initNavigation();
		getSelectData();
	}

}