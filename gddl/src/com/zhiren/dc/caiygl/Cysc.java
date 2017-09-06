package com.zhiren.dc.caiygl;
/*
 * ���ߣ�����ƽ
 * ʱ�䣺2009-11-27
 * �������ڲ�����Ϣά��ҳ���������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-28
 * ���������Ӳ�����Ϣά��ʱ����ʱ���Ĭ��ֵ,����ɾ��ʱ����ʾ
 */
/**
 * @author ����
 * @version 2009318.1
 */
/*
 * 2009-05-08
 * ����
 * �޸�ҳ�����ʱ���msg �ͻ���ʾ��Ϣ
 * ���а汾:1.1.2.8
 */

/*
 * 2009-05-15
 * ���ش�
 * ����ɾ���������빦�ܡ�
 * ���а汾:1.1.2.10
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-26 14:59
 * �������޸Ķ��м�¼ʱ�����������⡣
 * �汾��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-11 
 * ����������ɾ���������빦�ܵ����ã�������Щ�糧��ɾ���������롣
 * �汾��
 */

/*
 * �޸���: ��ΰ
 * ʱ�䣺  2010-05-24
 * ���� �� ��Page���ƴ����Visit.String11()��
 * 		  ��Caiyangry.javaȡ��Ҫ���ظ�ҳ���Page����
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Cysc extends BasePage implements PageValidateListener {
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
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		// ������Ʒ���š�������
		while (rsl.next()) {
			String id = rsl.getString("id");
			sb.delete(0, sb.length());
			sb.append("update yangpdhb\n");
			sb.append("set caiysj=to_date('").append(rsl.getString("caiysj"))
					.append("','yyyy-mm-dd')");
			sb.append(",bianh='").append(rsl.getString("bianh")).append("'");
			sb.append(",kaissj='").append(rsl.getString("kaissj")).append("'");
			sb.append(",jiessj='").append(rsl.getString("jiessj")).append("'");
			sb.append(",beiz='").append(rsl.getString("beiz")).append("'");
			sb.append(" where id=").append(rsl.getString("id")).append("\n");
			flag = con.getUpdate(sb.toString());
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Caiyxxwh, "yangpdhb", id);
			sb1.delete(0, sb.length());
			sb1.append("update caiyb \n");
			sb1.append("set caiyrq=to_date('").append(rsl.getString("caiysj"))
					.append("','yyyy-mm-dd')");
			sb1.append(" where id in (select caiyb_id from yangpdhb where id=")
					.append(rsl.getString("id")).append(")");
			flag1 = con.getUpdate(sb1.toString());
			// MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
			// SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Caiyxxwh,
			// "caiyb",id);
			if (flag == -1 || flag1 == -1) {
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb.toString());
				setMsg("������̳��ִ��󣡱���δ�ɹ���");
				con.rollBack();
				con.Close();
				return;
			}

		}
		rsl.close();
		con.Close();
	}

	private String getZhillsb_id() {
		Visit visit = (Visit) this.getPage().getVisit();
		String zhillsb_id = "";
		String bianm = "";
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
				getChange());
		JDBCcon cn = new JDBCcon();
		while (mdrsl.next()) {
			bianm = mdrsl.getString("BIANH");
		}
		String sql = "select zhillsb_id from zhuanmb where bianm in '" + bianm
				+ "'";
		ResultSet rs = cn.getResultSet(sql);

		try {
			while (rs.next()) {
				zhillsb_id = rs.getString("zhillsb_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return zhillsb_id;
	}

	// ɾ����������ķ���
	private void shanc() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
	}

	private void Save3(String strchang, Visit visit) {

		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("begin \n");
		StringBuffer sb1 = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchang);
		// ResultSetList rs = visit.getExtGrid1().getModifyResultSet(strchang);

		while (mdrsl.next()) {

			sb.append("delete  from zhuanmb\n");
			sb.append("where zhillsb_id = ").append(getZhillsb_id()).append(
					";\n");
			sb1.append("update zhillsb  \n");
			sb1.append("set shenhzt=-1");
			sb1.append(" where id=").append(getZhillsb_id()).append(";\n");
			sb.append(" end;");
			sb1.append(" end;");

			con.getUpdate(sb.toString());
			con.getUpdate(sb1.toString());
		}
		con.Close();

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	private boolean _Delete = false;

	public void DeleteButton(IRequestCycle cycle) {

		_Delete = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShedsqlChick) {
			_ShedsqlChick = false;
			Update(cycle);
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}

		if (_Delete) {
			_Delete = false;

			shanc();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		String sqlysfs = "";
		if (getYunsfsValue().getId() != -1) {
			sqlysfs = " and f.yunsfsb_id =" + getYunsfsValue().getId();
		}
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql;
		sql = "select j.id,j.bianm bianh,j.caiysj,decode(j.kaissj,null,to_char(sysdate,'hh24:mi:ss'),j.kaissj) kaissj,"
				+ "decode(j.jiessj,null,to_char(sysdate,'hh24:mi:ss'),j.jiessj) jiessj,j.caiyry lurry,j.beiz beiz from\n"
				+ "(select c.id from fahb f, caiyb c, diancxxb d\n"
				+ "where f.zhilb_id = c.zhilb_id and f.diancxxb_id = d.id\n"
				+ "and (f.diancxxb_id = "
				+ getTreeid()
				+ " or d.fuid = "
				+ getTreeid()
				+ ") \n"
				+ "and c.caiyrq >= "
				+ caiyrqb
				+ "\n"
				+ "and c.caiyrq < "
				+ caiyrqe
				+ "+ 1 "
				+ sqlysfs
				+ "\n"
				+ "group by c.id) k,\n"
				+ "(select y.caiyb_id,y.id,z.bianm,y.caiysj,y.kaissj,y.jiessj,GetCaiyry(y.id) caiyry,y.beiz\n"
				+ "from yangpdhb y, zhuanmb z, zhuanmlb zb\n"
				+ "where y.zhilblsb_id = z.zhillsb_id and zb.id = z.zhuanmlb_id\n"
				+ "and zb.jib = 1) j\n" + "where k.id = j.caiyb_id order by caiysj,bianh";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yangpdhb");
		// /������ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("bianh").setHeader("���");
		egu.getColumn("bianh").setEditor(null);
		egu.getColumn("caiysj").setHeader("��������");
		egu.getColumn("caiysj").setWidth(80);
		egu.getColumn("kaissj").setHeader("��ʼʱ��");
		egu.getColumn("jiessj").setHeader("����ʱ��");
		egu.getColumn("lurry").setHeader("������Ա");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(200);
		egu.getColumn("lurry").setWidth(140);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setReturnId(false);

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
		df1.Binding("RIQI2", "forms[0]");
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
		// // //�����п��
		// egu.getColumn("mingc").setWidth(70);
		// // //���õ�ǰ���Ƿ�༭
		// egu.getColumn("piny").setEditor(null);
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// ֻ��ѡ�е���
		// // /�Ƿ񷵻��������ֵ��ID
		// egu.getColumn("leib").setReturnId(false);
		// �������䷽ʽ������
		egu.addTbarText("���䷽ʽѡ��:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(80);
		comb1.setTransform("Yunsfs");
		comb1
				.setListeners("select:function(own,rec,index){ Ext.getDom('Yunsfs').selectedIndex=index;}");
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		// ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		GridButton gb = new GridButton(
				"ѡ�������Ա",
				"function(){"
						+ "if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){"
						+ "Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��������¼');"
						+ "return;}"
						+ "grid1_rcd = gridDiv_sm.getSelections()[0];"
						+ "var fun=gridDiv_grid.on('afteredit',function(e){"
						+ "Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�������Ա֮ǰ���ȱ���');"
						+ "return;});" + "if(grid1_rcd.get('ID') == '0'){"
						+ "Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�������Ա֮ǰ���ȱ���!');"
						+ "return;}" + "grid1_history = grid1_rcd.get('ID');"
						+ "var Cobj = document.getElementById('CHANGE');"
						+ "Cobj.value = grid1_history;"
						+ "document.getElementById('ShedsqlButton').click();"
						+ "}");

		gb.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(gb);
		// ɾ����ť
		GridButton dbtn = new GridButton("ɾ��", getfunction("DeleteButton"));
		dbtn.setIcon(SysConstant.Btn_Icon_Delete);
		if (MainGlobal.getXitxx_item("����", "�Ƿ��ɾ����������",
				"" + ((Visit) getPage().getVisit()).getDiancxxb_id(), "��")
				.equals("��")) {
			egu.addTbarBtn(dbtn);
		}
		// ��ѡ��
		// egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		// egu.getGridColumns().add(2, new
		// GridColumn(GridColumn.ColType_Check));
		setExtGrid(egu);
		con.Close();
	}

	public String getfunction(String binder) {
		String handler = "function(){"
				+ "if(gridDiv_sm.getSelections().length <= 0 ){"
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��������¼');"
				+ "return;}"
				+ "Ext.MessageBox.confirm('��ʾ��Ϣ','ɾ�����������ָܻ���ȷ��ɾ����',function(btn){if(btn == 'yes'){"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i<Mrcd.length; i++){\n"
				+ "grid1_history = '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<BIANH update=\"true\">' + Mrcd[i].get('BIANH').replace('<','&lt;').replace('>','&gt;')+ '</BIANH>'+ '<CAIYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('CAIYSJ'))?Mrcd[i].get('CAIYSJ'):Mrcd[i].get('CAIYSJ').dateFormat('Y-m-d'))+ '</CAIYSJ>'+ '<KAISSJ update=\"true\">' + Mrcd[i].get('KAISSJ')+ '</KAISSJ>'+ '<JIESSJ update=\"true\">' + Mrcd[i].get('JIESSJ')+ '</JIESSJ>'+'<LURRY update=\"true\">' + Mrcd[i].get('LURRY')+ '</LURRY>'+'</result>' ; \n"
				+ "}\n" + "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = '<result>'+grid1_history+'</result>';"
				+ "document.getElementById('" + binder + "').click();}});"
				+ "}";
		return handler;
	}

	/* ���䷽ʽ */
	public IDropDownBean getYunsfsValue() {
		IDropDownBean vi = ((Visit) this.getPage().getVisit())
				.getDropDownBean4();
		if (vi == null) {
			if (getYunsfsModel().getOptionCount() > 0) {
				setYunsfsValue((IDropDownBean) getYunsfsModel().getOption(0));
			}

		}

		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setYunsfsValue(IDropDownBean value) {
		// ((Visit) this.getPage().getVisit()).setLong1(2);

		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setYunsfsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setYunsfsModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setYunsfsModels() {
		String sb;
		// ֻȡ�������д��ڵ����䷽ʽ
		// sb= "select distinct yun.id id,yun.mingc mingc from\n" +
		// " ( select f.yunsfsb_id\n" +
		// " from caiyb c, fahb f, yangpdhb y\n" +
		// " where c.zhilb_id = f.zhilb_id\n" +
		// " and y.caiyb_id = c.id) k ,yunsfsb yun where yun.id=k.yunsfsb_id";
		// ȡȫ�������䷽ʽ
		sb = "select -1 id, 'ȫ��' mingc from yunsfsb\n" + "union\n"
				+ "select id,mingc\n" + "  from yunsfsb";

		setYunsfsModel(new IDropDownModel(sb));
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

	private void Update(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Caiyangry");
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
			setYunsfsValue(null);
			setYunsfsModel(null);
			visit.setString1(null);
			
			//ww ��¼page  ��ѡ������˺���ת��
			visit.setString11(getPageName().toString());
		}
		getSelectData();
	}

}