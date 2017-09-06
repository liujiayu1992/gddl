package com.zhiren.dc.caiygl;
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-10 11��29
 * ���������Ϊһ������ѡ���ܳ���ʱ���г����зֳ��Ĳ�����Ϣ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-31 16��58
 * ���������Ӹ������������ѯ�Ĺ��ܣ������Ӳ�������
 * 		insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * 		values(getnewid(diancxxb_id),1,diancxxb_id,'�����������������ѯ','��','','���ƻ�',1,'ʹ��')
 */
/**
 * @author ����
 * @version 2009-05-15
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-26 11:31
 * �������޸���ˢ�����ݵ�ʱ�򲻹���������ȡ���ظ������������ж�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-26 13:57
 * �������޸�ˢ������ʱ�����ظ������⣬�������ظ���ʱ���Ѷ�������ݻ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-06-06
 * ����������Xinzcy����ԭ�л����Ͻ��е���������ʾ�������˵Ļ�������
 */
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xinzcy_hd extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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

	// ��¼ҳ��ѡ���е�����
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

	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_ShedsqlChick) {
			_ShedsqlChick = false;
			Update(cycle);
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) this.getPage().getVisit();
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql="";
		String dcsql = " and d.id = " + getTreeid();;
		if(v.isFencb()){
			if(v.getDiancxxb_id() == Long.parseLong(getTreeid())){
				dcsql = " and d.fuid = " + getTreeid();
			}
		}

		sql ="select c.id,\n" +
			"       c.zhilb_id as zhilb_id,\n" +
			"		f.diancxxb_id diancxxb_id,\n" +
			"       c.bianm as jincph,\n" + 
			"       getFahxx4zl(c.zhilb_id) as fahxx,\n" + 
			"       getCaiybh4zl(c.zhilb_id) as caiybh,\n" + 
			"       getHuaybh4zl(c.zhilb_id) as huaybh\n" + 
			"from caiyb c,(select distinct f.diancxxb_id,f.zhilb_id from fahb f,diancxxb d\n" + 
			"where  f.diancxxb_id = d.id and f.daohrq>="+caiyrqb +"\n" + 
			"      and f.daohrq<="+caiyrqe +"\n" +
			"	   " +dcsql+ 
			"	   minus select distinct f.diancxxb_id,f.zhilb_id from fahb f,diancxxb d, zhilb z" +
			"	   where f.zhilb_id=z.id and f.diancxxb_id = d.id and f.daohrq>="+caiyrqb +"\n" + 
			"      and f.daohrq<="+caiyrqe +"\n" +
			"	   " +dcsql+ 
					") f\n" +
			"where f.zhilb_id = c.zhilb_id order by c.caiyrq,c.bianm\n";
	
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caiyb");
		// /������ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("zhilb_id").setHeader("ZHILB_ID");
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("jincph").setHeader("��������");
		egu.getColumn("fahxx").setHeader("������Ϣ");
		egu.getColumn("fahxx").setWidth(450);
		egu.getColumn("caiybh").setHeader("�������");
		egu.getColumn("caiybh").setWidth(100);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setWidth(100);
		// ����
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setValue(this.getRiqi2());
		df1.setReadOnly(true);
		df1.Binding("RIQI2", "forms[0]");
		df1.setId("riqi2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// ֻ��ѡ�е���
		
//		 ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		
		// �������䷽ʽ������
		egu.addTbarText("���������:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(80);
		comb1.setTransform("Leib");
		comb1.setListeners("select:function(own,rec,index){ Ext.getDom('Leib').selectedIndex=index;}");
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		GridButton gb = new GridButton(
				"����",
				"function(){"
						+ "if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){"
						+ "Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��������¼');"
						+ "return;}"
						+ "grid1_rcd = gridDiv_sm.getSelections()[0];"
						+ "grid1_history = grid1_rcd.get('ID')+'&'+grid1_rcd.get('DIANCXXB_ID')+'&'+grid1_rcd.get('ZHILB_ID');"
						+ "var Cobj = document.getElementById('CHANGE');"
						+ "Cobj.value = grid1_history;"
						+ "document.getElementById('ShedsqlButton').click();"
						+ "}");
		gb.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gb);
		setExtGrid(egu);
		con.Close();
	}

	//���������
	public IDropDownBean getLeibValue() {
		IDropDownBean vi = ((Visit) this.getPage().getVisit()).getDropDownBean4();
		if (vi == null) {
			if (getLeibModel().getOptionCount() > 0) {
				setLeibValue((IDropDownBean) getLeibModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setLeibValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setLeibModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setLeibModels() {
		String sb;
		sb = " select l.id,l.mingc from leibb l where beiz = '����'\n";
		setLeibModel(new IDropDownModel(sb));
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
		JDBCcon con = new JDBCcon();
		String caiyb_id = "";
		long diancxxb_id = 0;
		long zhilb_id = 0;
		String bumb_id = "";
		String zhuanmsz = "";
		String leibb_id = getLeibValue().getId()+"";
		String leib = getLeibValue().getValue();
		String xinx = getChange();
		String[] a = xinx.split("&");
		caiyb_id = a[0];
		diancxxb_id =  Long.parseLong(a[1]);
		zhilb_id =  Long.parseLong(a[2]);
		String cysj = "";
		String beiz = "";
		String sqlnew  = "select to_char(y.caiysj,'yyyy-mm-dd') caiysj,y.beiz from yangpdhb y ,zhillsb z where  y.zhilblsb_id = z.id and z.zhilb_id=" + zhilb_id;
		ResultSetList rsl_yp = con.getResultSetList(sqlnew);
		while(rsl_yp.next()){
			cysj = rsl_yp.getString("CAIYSJ");
			beiz = rsl_yp.getString("BEIZ");
		}
		String sql = "select * from leibb where id = " + leibb_id + "\n";
		ResultSetList rsl_zm = con.getResultSetList(sql);
		while(rsl_zm.next()){
			zhuanmsz = rsl_zm.getString("ZHUANMSZ");
		}
		rsl_zm.close();
		//�õ�����
		String sSql = "select * from bumb order by zhuhybm desc,mingc";
		ResultSetList rsl = con.getResultSetList(sSql);
		if(rsl.next()){
			bumb_id = rsl.getString("ID");
		}
		rsl.close();
//		��ӱ���
		int flag = Caiycl.AddBianhC(con, zhilb_id, diancxxb_id,
				caiyb_id, leibb_id, bumb_id, leib,zhuanmsz,cysj,beiz);
	    if(flag>=0){
	    	
	    	setMsg("���������ɹ���");
	    }else{
	    	setMsg("���ɹ��̳��ִ�������δ�ɹ���");
	    }
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setLong1(0);
			setLeibValue(null);
			setLeibModel(null);
		}
		getSelectData();
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

}
