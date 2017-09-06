package com.zhiren.dc.huaygl.qityhy;
/*
 * ����:����
 * ����:2009-08-20
 * */

/*
 * ���ߣ���ΰ
 * �޸����ڣ�2010-05-23
 * �޸����ݣ���������Ϊ��ʱĬ��Ϊ��ǰ����
 */
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qitycy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}

	// ��������list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	boolean riqichange1 = false;

	boolean riqichange2 = false;

	private String riqi1; // ҳ����ʼ��������ѡ��

	private String riqi2; // ҳ���������ѡ��

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi1) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi1)) {
			this.riqi1 = riqi1;
			riqichange1 = true;
		}
	}

	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(new Date());
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi1 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
			riqichange2 = true;

		}

	}

	private String Parameters;// ��¼ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Fahhtwh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		JDBCcon con = new JDBCcon();
		int flag = 0;
		int flag1 = 0;
		con.setAutoCommit(true);
		StringBuffer sb1 = new StringBuffer();
		// ������Ʒ���š�������
		while (rsl.next()) {
			sb1.delete(0, sb1.length());
			if("".equals(rsl.getString("id")) || rsl.getString("id")==null){//����
				
				sb1.append("insert into qitycyb(id,zhillsb_id,diancxxb_id,meikmc,had,caiyrq,caiyry,zhiyry) values(\n");
				sb1.append("getnewid(" + getTreeid() + "),\n");
				sb1.append(rsl.getString("zid")+",\n");
				sb1.append(getTreeid()+",\n");
				sb1.append("'" + rsl.getString("meikmc") + "',\n");
				sb1.append("" + rsl.getString("had") + ",\n");
				sb1.append("to_date('" + rsl.getString("caiyrq") +"','yyyy-mm-dd'),\n");
				sb1.append("'" + rsl.getString("caiyry")+"',\n");
				sb1.append("'" + rsl.getString("zhiyry") + "'\n");
				sb1.append(")");
				flag = con.getInsert(sb1.toString());
			}else{	//�޸�
				
				sb1.append("update qitycyb set");
				sb1.append(" meikmc = '" +rsl.getString("meikmc") + "'");
				sb1.append(", had =" +rsl.getString("had") + "");
				sb1.append(",caiyrq = to_date('" +rsl.getString("caiyrq") + "','yyyy-mm-dd')");
				sb1.append(",caiyry = '" +rsl.getString("caiyry") + "'");
				sb1.append(",zhiyry = '" +rsl.getString("zhiyry") + "'");
				sb1.append(" where id=" +rsl.getString("id"));
				sb1.append("");
				flag1 = con.getUpdate(sb1.toString());
			}

			if (flag == -1 || flag1 == -1) {
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb1.toString());
				setMsg("������̳��ִ��󣡱���δ�ɹ���");
				con.rollBack();
				con.Close();
				return;
			}
		}
		if(flag == 1 || flag1 == 1){
			setMsg("����ɹ���");
		}
		rsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String huayrqb = DateUtil.FormatOracleDate(getRiqi1());
		String huayrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql;
		sql = 
			"select q.id,z.id zid,z.beiz,q.meikmc,q.had,q.caiyrq,q.caiyry,q.zhiyry\n" +
			"from  zhillsb z,qitycyb q\n" + 
			"where z.huaylb='��ʱ��'\n" + 
			"and z.id=q.zhillsb_id(+)\n" + 
			"and q.diancxxb_id(+) = "+ getTreeid()+" \n" + 
			"and z.huaysj>=" + huayrqb +"\n" + 
			"and z.huaysj<" + huayrqe + "+1" ;
		

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		egu.setTableName("yangpdhb");
		// /������ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("zid").setHidden(true);
		egu.getColumn("zid").setEditor(null);
		egu.getColumn("beiz").setHeader("���");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("meikmc").setHeader("ú������");
		egu.getColumn("meikmc").setWidth(100);
		egu.getColumn("had").setHeader("Had(%)");
		egu.getColumn("had").setWidth(80);
		egu.getColumn("caiyrq").setHeader("��������");
		egu.getColumn("caiyrq").setWidth(80);
		egu.getColumn("caiyry").setHeader("������Ա");
		egu.getColumn("caiyry").setWidth(200);
		egu.getColumn("zhiyry").setHeader("������Ա");
		egu.getColumn("zhiyry").setWidth(200);

		// ����
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");

		DateField df1 = new DateField();
		df1.setValue(this.getRiqi2());
		df1.setReadOnly(true);
		df1.Binding("RIQI2", "");//forms[0]
		df1.setId("riqi2");
		egu.addToolbarItem(df1.getScript());
		// /����������Ĭ��ֵ
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// ֻ��ѡ�е���
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
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

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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

	// ҳ���ж�����
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
			visit.setLong1(0);
			setExtGrid(null);
			visit.setString1(null);
		}
		getSelectData();
	}
}
