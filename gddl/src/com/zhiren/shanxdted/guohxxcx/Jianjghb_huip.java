package com.zhiren.shanxdted.guohxxcx;

import java.sql.PreparedStatement;
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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*����:���ܱ�
 *����:��ͬ�糧3������������������ˢ���������,����ú����жú��ʱ��,ˢ���Ŀ�Ƭ��ʧ,�����ϵͳ��ز���Ƥ,���Կ�������ֹ���Ƥ�Ľ���
 * 
 * 
 */
public class Jianjghb_huip extends BasePage implements PageValidateListener {
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
			riqi = DateUtil.FormatDate(new Date());
			//riqi=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
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
			riq2 = DateUtil.FormatDate(new Date());
			//riq2=DateUtil.FormatDate(DateUtil.getLastDayOfMonth(new Date()));
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
		Visit visit = (Visit) getPage().getVisit();
		String strchange = getChange();
		JDBCcon con = new JDBCcon();
		
		
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (!"".equals(mdrsl.getString("ID"))) {
				String zhiybh="123";//�������Ĭ��ֵ
				String huaybh="123";//������Ĭ��ֵ
				String bianhSql="select zhiybh,huaybh from jianjghb jj\n" +
								"              where jj.zhongcsj>date'"+mdrsl.getString("daohrq")+"' and jj.zhongcsj<date'"+mdrsl.getString("daohrq")+"'+1\n" + 
								"              and jj.diancxxb_id=303 and meikxxb_id="+mdrsl.getString("meikxxb_id2")+" and yunsdwb_id="+mdrsl.getString("yunsdwb_id2")+"\n" + 
								"          and jj.qingcsj is not null    order by jj.zhongcsj";
				ResultSetList bh_rl = con.getResultSetList(bianhSql.toString());
				if(bh_rl.next()){//�����ѯ�������ǵõ��޸���һ��ú��Ӧ��������źͻ�����
					zhiybh=bh_rl.getString("zhiybh");
					huaybh=bh_rl.getString("huaybh");
				}
							
				sql.append("update jianjghb set piz=").append(mdrsl.getDouble("piz"));
				sql.append(",qingcsj=to_date('"+mdrsl.getString("zhongcsj")+"','yyyy-mm-dd hh24:mi:ss'),qingcjjy='").append(visit.getRenymc()).append("'");
				sql.append(",zhuangt=4,jingz=").append(mdrsl.getDouble("maoz")).append("-").append(mdrsl.getDouble("piz")).append("-").append(mdrsl.getDouble("kous"));
				sql.append(",kous=").append(mdrsl.getDouble("kous"));
				sql.append(",zhiybh='"+zhiybh+"',huaybh='"+huaybh+"',qingchh='3',beiz=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||'"+visit.getRenymc()+"�ֹ�����Ƥ��,��Ƥ�ó�,�������:"+zhiybh+",������:"+huaybh+"'");
				sql.append("where id=").append(mdrsl.getString("id")+";\n");
			} 
		}
		mdrsl.close();
		if(sql.length()>0){
			if(con.getUpdate("begin\n" + sql.toString() + "end;") >= 0){				
				setMsg("�ֹ���Ƥ�ɹ����ó��Ӹ�ҳ����ʧ");
			}else {
				setMsg("����ʧ�ܣ�");
			}
		}else {
			setMsg("����ʧ�ܣ�");
		}
		
		con.Close();
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
			gyssql = " and jj.meikxxb_id="+getGongysValue().getId();
		}
		
		String dcsql = "";
		if(getTreeid()=="300"){
			dcsql = " and jj.diancxxb_id in(301,302,303) ";
		}else{
			dcsql = " and jj.diancxxb_id in("+this.getTreeid()+") ";
		}
		
		String sql=
			"select jj.id,mk.mingc as meikxxb_id,ys.mingc as yunsdwb_id,jj.cheh,\n" +
			"maoz,to_char(jj.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,jj.zhongcjjy,jj.biaoz,nvl(jj.piz,0) as piz,jj.kous,\n" +
			"jj.meikxxb_id as meikxxb_id2,jj.yunsdwb_id as yunsdwb_id2, to_char(jj.zhongcsj,'yyyy-mm-dd') as daohrq\n" + 
			"from jianjghb jj,meikxxb mk,yunsdwb ys\n" + 
			" where  jj.zhongcsj>date'"+this.getRiqi()+"' and jj.zhongcsj<date'"+this.getRiq2()+"'+1\n" + 
			" "+dcsql+"\n"+
			" "+gyssql+"\n"+
			" and jj.meikxxb_id=mk.id\n" + 
			" and jj.yunsdwb_id=ys.id\n" + 
			"and jj.qingcsj is null";


		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jianjghb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("ú������");
		egu.getColumn("meikxxb_id").setWidth(170);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu.getColumn("yunsdwb_id").setWidth(90);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("cheh").setHeader("����");
		egu.getColumn("cheh").setWidth(90);
		egu.getColumn("cheh").setEditor(null);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(90);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("zhongcsj").setHeader("�س�ʱ��");
		egu.getColumn("zhongcsj").setWidth(120);
		egu.getColumn("zhongcsj").setEditor(null);
		
		egu.getColumn("zhongcjjy").setHeader("�س�����Ա");	
		egu.getColumn("zhongcjjy").setWidth(90);
		egu.getColumn("zhongcjjy").setEditor(null);
		
		egu.getColumn("biaoz").setCenterHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("piz").setCenterHeader("Ƥ��");
		egu.getColumn("piz").setWidth(80);
		((NumberField)egu.getColumn("piz").editor).setDecimalPrecision(2);
		
		egu.getColumn("kous").setCenterHeader("�۶�");
		egu.getColumn("kous").setWidth(80);
		
		egu.getColumn("meikxxb_id2").setHeader("ú��ID");
		egu.getColumn("meikxxb_id2").setWidth(70);
		egu.getColumn("meikxxb_id2").setEditor(null);
		egu.getColumn("meikxxb_id2").setHidden(true);
		
		egu.getColumn("yunsdwb_id2").setHeader("����ID");
		egu.getColumn("yunsdwb_id2").setWidth(70);
		egu.getColumn("yunsdwb_id2").setEditor(null);
		egu.getColumn("yunsdwb_id2").setHidden(true);
		
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(90);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setHidden(true);
		

		
	
		
	
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(1000);		
		egu.setWidth("bodyWidth");
	
		
//		 �糧��(���õ糧idΪ303,���ڵ糧)
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,303, getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
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
		
		
		
		StringBuffer sb=new StringBuffer();
		egu.addTbarText("-");
		egu.addToolbarItem("{text:' ˢ��',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
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
		
		String  sql =
			"select id,mingc from meikxxb mk where mk.id in (\n" +
			"select jj.meikxxb_id from jianjghb jj where\n" + 
			" jj.zhongcsj>date'"+this.getRiqi()+"'\n" + 
			" and jj.zhongcsj<date'"+this.getRiq2()+"'+1\n" + 
			" and jj.qingcsj is null \n"+
			" and jj.diancxxb_id in ("+this.getTreeid()+"))";
		  
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
			this.setRiqi(null);
			this.setRiq2(null);
			getGongysValue();
			setGongysValue(null);
			getGongysModels();
		
			this.setTreeid("303");//��Ϊ�������ֹ���Ƥ,һ�ںͶ��ڲ��ֹ���Ƥ,��������������Ĭ��ֵ
			
			
		}
		if(riqichange||riq2change){
			this.getGongysModels();
			riqichange=false;
			riq2change=false;
		}
		getSelectData();
	}

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

//			treeid = String.valueOf(((Visit) getPage().getVisit())
//					.getDiancxxb_id());
			treeid = String.valueOf(303);
			
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
	
	private String getDcMingc(JDBCcon con, String id){
		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id=" + id);
		if(rsl.next()){
			return rsl.getString("mingc");
		}else{
			return "";
		}
	}
}