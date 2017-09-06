package com.zhiren.shanxdted.baob.kuidkkb;

import java.util.ArrayList;
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2014-01-23
 * ���������ӽ�����ֵ����=������ֵ+������ֵ��/2
 * 		����ú�۹�ʽ����ͬú��/��ͬ��ֵ*������ֵ
 */

public class Kuidkkb_mksz extends BasePage implements PageValidateListener {
	private String msg = "";
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg =  MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			//riqi = DateUtil.FormatDate(new Date());
			riqi=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			//riq2 = DateUtil.FormatDate(new Date());
			riq2=DateUtil.FormatDate(DateUtil.getLastDayOfMonth(new Date()));
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	

	private void Save() {
		/*Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);*/
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
		if (_shuaxin){
			_shuaxin=false;
		}
	}

	public String getGongysxx() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setGongysxx(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String gyssql = "";
		if(getGongysValue().getId()!=-1){
			gyssql = " and sz.meikxxb_id="+getGongysValue().getId();
		}
		
//		String dcsql = "";
//		if(getTreeid()=="300"){
//			dcsql = " and d.id in(301,302,303) ";
//		}
		
		String sql=
		    	  "select sz.id,mk.mingc as meikxxb_id,sz.kaisrq,sz.jiezrq,sz.hetmj,sz.hetrz,\n" +
		    	  "decode(sz.jiesrz,0,'����',1,'��',2,'˫����Ȩ',3,'��ͬ',4,'��ͬ��ֵ-100',5,'��ͬ��ֵ-70'," +
		    	  "6,'��ͬ��ֵ-200',7,'(����ֵ+������ֵ)/2','��ѡ��') as jiesrz,\n" +
		    	  "decode(sz.jiesl,0,'����',1,'��','��ѡ��') as jiesl,fs.mingc as meijfsb_id,\n"+
		    	  "fskk.mingc as meijfsb_kouk_id\n"+
		    	  "from kuidkkb_mksz sz ,meikxxb mk,meijfsb fs,meijfsb_kouk fskk\n" + 
		    	  "where sz.meikxxb_id=mk.id\n" + 
		    	  "and sz.kaisrq>=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
		    	  "and sz.jiezrq<=to_date('"+getRiq2()+"','yyyy-mm-dd') \n"+gyssql+"" +
		    	  " and sz.meijfsb_id=fs.id(+)"+
		    	  " and sz.meijfsb_kouk_id=fskk.id(+)"+
		    	  " order by mk.mingc,sz.jiezrq";

		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("kuidkkb_mksz");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("ú������");
		egu.getColumn("meikxxb_id").setWidth(170);
		egu.getColumn("kaisrq").setHeader("��ʼ����");
		egu.getColumn("kaisrq").setWidth(90);
		egu.getColumn("jiezrq").setHeader("��ֹ����");
		egu.getColumn("jiezrq").setWidth(90);
		egu.getColumn("hetmj").setHeader("��ͬú��");
		egu.getColumn("hetmj").setWidth(60);
		egu.getColumn("hetmj").setDefaultValue("0");
		egu.getColumn("hetrz").setHeader("��ͬ��ֵ");	
		egu.getColumn("hetrz").setWidth(60);
		egu.getColumn("hetrz").setDefaultValue("0");
		egu.getColumn("jiesrz").setHeader("������ֵ");
		egu.getColumn("jiesrz").setWidth(120);
		egu.getColumn("meijfsb_id").setCenterHeader("ú��(��)");
		egu.getColumn("meijfsb_id").setWidth(250);
		egu.getColumn("jiesl").setHeader("������������");
		egu.getColumn("jiesl").setWidth(80);
		egu.getColumn("meijfsb_kouk_id").setCenterHeader("ú��(��)");
		egu.getColumn("meijfsb_kouk_id").setWidth(250);
		
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		//ȡ���60����ú��ú��λ����
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id ,mingc from meikxxb mk where mk.id in ("+
                                  "select distinct f.meikxxb_id from fahb f where f.daohrq<=date'"+this.getRiq2()+"'+30  and f.daohrq>=date'"+this.getRiqi()+"' -30)"+
"									order by mingc"));
		egu.getColumn("meikxxb_id").setDefaultValue(getGongysValue().getValue());
		egu.getColumn("meikxxb_id").setReturnId(true); 
		
		List lb = new ArrayList();
		lb.add(new IDropDownBean(0, "����"));
		lb.add(new IDropDownBean(1, "��"));
		lb.add(new IDropDownBean(2, "˫����Ȩ"));
		lb.add(new IDropDownBean(3, "��ͬ"));
		lb.add(new IDropDownBean(4, "��ͬ��ֵ-100"));
		lb.add(new IDropDownBean(5, "��ͬ��ֵ-70"));
		lb.add(new IDropDownBean(6, "��ͬ��ֵ-200"));
		lb.add(new IDropDownBean(7, "(����ֵ+������ֵ)/2"));
		
		egu.getColumn("jiesrz").setEditor(new ComboBox());
		egu.getColumn("jiesrz").setComboEditor(egu.gridId,
				new IDropDownModel(lb));
		egu.getColumn("jiesrz").setDefaultValue("˫����Ȩ");
		egu.getColumn("jiesrz").setReturnId(true);
		
		
		List jsl = new ArrayList();
		jsl.add(new IDropDownBean(0, "����"));
		jsl.add(new IDropDownBean(1, "��"));
		egu.getColumn("jiesl").setEditor(new ComboBox());
		egu.getColumn("jiesl").setComboEditor(egu.gridId,
				new IDropDownModel(jsl));
		egu.getColumn("jiesl").setDefaultValue("����");
		egu.getColumn("jiesl").setReturnId(true);
		
		
//		ú��
		egu.getColumn("meijfsb_id").setEditor(new ComboBox());
		egu.getColumn("meijfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id ,mingc from meijfsb order by id "));
		egu.getColumn("meijfsb_id").setDefaultValue("��ͬú��");
		egu.getColumn("meijfsb_id").setReturnId(true); 
		
		

//		ú��(�ۿ�)
		egu.getColumn("meijfsb_kouk_id").setEditor(new ComboBox());
		egu.getColumn("meijfsb_kouk_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id ,mingc from meijfsb_kouk order by id "));
		egu.getColumn("meijfsb_kouk_id").setDefaultValue("��ͬú��");
		egu.getColumn("meijfsb_kouk_id").setReturnId(true); 
	
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(1000);		
		egu.setWidth("bodyWidth");
	
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
	
		// ������λ
		egu.addTbarText("ú��");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(true);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		// �糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
//		StringBuffer sb=new StringBuffer();
		
		egu.addTbarText("-");
		egu.addToolbarItem("{text:' ˢ��',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert,"");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    
		setExtGrid(egu);
		
		con.Close();
	}
	
//������λ
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select id ,mingc from meikxxb mk where mk.id in ("+
						"select distinct f.meikxxb_id from fahb f where  f.daohrq<=date'"+this.getRiq2()+"'+30  and f.daohrq>=date'"+this.getRiqi()+"' -30)"+
        "			order by mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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

			((Visit) getPage().getVisit()).setString1("");
			getGongysValue();
			setGongysValue(null);
			getGongysModels();
			this.setRiqi(null);
			this.setRiq2(null);
			
			
		}
		getSelectData();
	}

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

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
	
//	private String getDcMingc(JDBCcon con, String id){
//		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id=" + id);
//		if(rsl.next()){
//			return rsl.getString("mingc");
//		}else{
//			return "";
//		}
//	}
}