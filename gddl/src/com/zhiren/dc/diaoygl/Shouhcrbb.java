package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
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
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-24
 * ���������糧�������Ϊ�糧�� ���Ҽ�������ֳ���ѡ���ܳ�ʱ�Ƿ���ʾ���ɰ�ť
 * insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz) 
 * values(getnewid(201),1,201,'�ֳ����ܳ���ʾ���ɰ�ť','��','','�պĴ��ձ�',1,'')
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-23
 * �����������ڳ�ʼ��ʱ���¼���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-04
 * �������޸�create �պĴ��ձ����� �����Ƿ����ò�������������
 */
public abstract class Shouhcrbb extends BasePage {
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
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}
	private void CreateData() {
		DelData();
		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = Long.parseLong(getTreeid());
		JDBCcon con = new JDBCcon();
		if(visit.isFencb()&&isParentDc(con)){
			AutoCreateShouhcrb.Create(con, diancxxb_id, DateUtil.getDate(getRiq()), true, true);
		}else{
			AutoCreateShouhcrb.Create(con, diancxxb_id, DateUtil.getDate(getRiq()), true);
		}
		con.Close();
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("delete from shouhcrbb where diancxxb_id=")
		.append(diancxxb_id).append(" and riq=")
		.append(CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("select s.id,s.fadl, s.jingz,s.jingz as dangrgm, s.biaoz, s.yuns, s.yingd, s.kuid, s.fady, \n")
		.append("s.gongry, s.qity,s.fady+s.gongry+s.qity+s.cuns+s.feiscy as haoyqkdr,s.feiscy, s.cuns, s.tiaozl, s.shuifctz, s.panyk, s.kuc \n")
		.append("from shouhcrbb s where diancxxb_id =")
		.append(diancxxb_id).append(" and riq=").append(CurDate);
//		sb.append("select kuc from shouhcrbb where diancxxb_id=")
//		.append(diancxxb_id)
//		.append(" and riq = ")
//		.append(LastDate);
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcrbb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("dangrgm").setHidden(true);
		egu.getColumn("haoyqkdr").setHidden(true);
		egu.getColumn("fadl").setHeader(Locale.fadl);
		egu.getColumn("fadl").setWidth(60);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);

		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(60);
	
		egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
		egu.getColumn("yingd").setWidth(60);
	
		egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
		egu.getColumn("kuid").setWidth(60);
		
		egu.getColumn("fady").setHeader(Locale.fady);
		egu.getColumn("fady").setWidth(60);
		egu.getColumn("gongry").setHeader(Locale.gongry);
		egu.getColumn("gongry").setWidth(60);
		egu.getColumn("qity").setHeader(Locale.qity);
		egu.getColumn("qity").setWidth(60);
		egu.getColumn("feiscy").setHeader(Locale.feiscy);
		egu.getColumn("feiscy").setWidth(60);
		egu.getColumn("cuns").setHeader(Locale.cuns);
		egu.getColumn("cuns").setWidth(60);
		egu.getColumn("tiaozl").setHeader(Locale.tiaozl);
		egu.getColumn("tiaozl").setWidth(60);
		egu.getColumn("shuifctz").setHeader(Locale.shuifctz);
		egu.getColumn("shuifctz").setWidth(80);
		egu.getColumn("panyk").setHeader(Locale.panyk);
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("kuc").setHeader(Locale.kuc);
		egu.getColumn("kuc").setWidth(60);
		if(!MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ú�ɱ༭", "0", "��").equals("��")){
			egu.getColumn("biaoz").setEditor(null);
			egu.getColumn("jingz").setEditor(null);
			egu.getColumn("yuns").setEditor(null);
			egu.getColumn("yingd").setEditor(null);
			egu.getColumn("kuid").setEditor(null);
			egu.getColumn("kuc").setEditor(null);
		}
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		���ɰ�ť
		if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�պĴ��ձ�", "�ֳ����ܳ���ʾ���ɰ�ť", diancxxb_id, "��").equals("��")){
			
		}else{
			GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
		}
//		ɾ����ť
		GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
//		���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		egu.addTbarBtn(gbs);
		
//		grid ���㷽��
		egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);");
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
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
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setChangbModels();
			getSelectData();
		}
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
