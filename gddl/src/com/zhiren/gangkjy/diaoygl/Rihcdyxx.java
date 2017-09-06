package com.zhiren.gangkjy.diaoygl;
/*
 * ����:����
 * ʱ��:2009-3-23
 * ����:�ջ𳵵�����Ϣ��ά��
 */
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Rihcdyxx extends BasePage implements PageValidateListener {
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// ҳ��仯��¼
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
//-------------------------------------------------------------  save()  -----------------------------

	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		int flag=visit.getExtGrid1().Save(getChange(), visit);
 		if(flag!=-1){
 			setMsg(ErrorMessage.SaveSuccessMessage);
 		}else{
 			setMsg("����ʧ�ܣ�");
 		}
 	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	//ɾ���ķ���
	private void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiqi());
		long diancxxb_id = visit.getDiancxxb_id();
		if(visit.isFencb() && getChangbValue()!=null) {
			diancxxb_id = getChangbValue().getId();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from rihcdyxx where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq=")
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
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
	}

	public void getSelectData() {
		
//		if(!this.isZuorkc()){
//			this.setMsg("��������û����д,������д��������!");
			returnMsg=true;
//		}else{
//			this.setMsg("");
//		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String riqTiaoj=this.getRiqi();
		String chaxun="";
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
//		int jib=this.getDiancTreeJib();
//		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
//			strdiancTreeID="";
//		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			strdiancTreeID = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
//		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
//			strdiancTreeID=this.getTreeid();
//		} 
		
	   chaxun= 

		   "select r.id,\n" +
		   "       r.diancxxb_id as diancxxb_id,\n" + 
		   "       f.mingc as gongysb_id,\n" + 
		   "       p.mingc as pinzb_id,\n" + 
		   "       c.mingc as faz_id,\n" + 
		   "       r.riq as riq,\n" + 
		   "       r.qingcls as qingcls,\n" + 
		   "      r.qingccs as qingccs,\n" + 
		   "      r.qingcds as qingcds,\n" + 
		   "      r.picls as picls,\n" + 
		   "      r.piccs as piccs,\n" + 
		   "      r.picds as picds,\n" + 
		   "      r.zhuangcls as zhuangcls,\n" + 
		   "      r.zhuangccs as zhuangccs,\n" + 
		   "      r.zhuangcds as zhuangcds,\n" + 
		   "      r.zaitls as zaitls,\n" + 
		   "      r.zaitcs as zaitcs,\n" + 
		   "      r.zaitds as zaitds,\n" + 
		   "      r.daogls as daogls,\n" + 
		   "      r.daogcs as daogcs,\n" + 
		   "      r.daogds as daogds,\n" + 
		   "      r.zhancls as zhancls,\n" + 
		   "      r.zhanccs as zhanccs,\n" + 
		   "      r.zhancds as zhancds,\n" + 
		   "       r.beiz as beiz\n" + 
		   "  from rihcdyxx r, vwfahr f, vwpinz p, vwchez c\n" + 
		  
		   "       where  r.gongysb_id=f.id\n" + 
		   "      and  r.pinzb_id=p.id\n" + 
		   "      and  r.faz_id=c.id\n" + 
//		    ""+strdiancTreeID+ 
		   "      and  r.diancxxb_id="+ visit.getDiancxxb_id()+"\n" + 
		   "      and  r.riq= to_date('"+this.getRiqi()+"','yyyy-mm-dd')";

//	   System.out.println(chaxun);
	   rsl = con.getResultSetList(chaxun);
	   
			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyWidth-10");
		
		egu.setTableName("rihcdyxx");
		egu.getColumn("id").setHeader("id");	
		egu.getColumn("diancxxb_id").setHeader(Local.diancmc);
		egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancxxb_id()+"");//Ĭ����ʾΪ�糧��id��
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader(Local.fahr);
		egu.getColumn("pinzb_id").setHeader(Local.pinz);
		egu.getColumn("faz_id").setHeader(Local.faz);
		egu.getColumn("riq").setHeader(Local.riq);
		egu.getColumn("qingcls").setHeader(Local.lies);
		egu.getColumn("qingccs").setHeader(Local.ches);
		egu.getColumn("qingcds").setHeader(Local.duns);
		egu.getColumn("picls").setHeader(Local.lies);
		egu.getColumn("piccs").setHeader(Local.ches);
		egu.getColumn("picds").setHeader(Local.duns);
		egu.getColumn("zhuangcls").setHeader(Local.lies);
		egu.getColumn("zhuangccs").setHeader(Local.ches);
		egu.getColumn("zhuangcds").setHeader(Local.duns);
		egu.getColumn("zaitls").setHeader(Local.lies);
		egu.getColumn("zaitcs").setHeader(Local.ches);
		egu.getColumn("zaitds").setHeader(Local.duns);
		egu.getColumn("daogls").setHeader(Local.lies);
		egu.getColumn("daogcs").setHeader(Local.ches);
		egu.getColumn("daogds").setHeader(Local.duns);
		egu.getColumn("zhancls").setHeader(Local.lies);
		egu.getColumn("zhanccs").setHeader(Local.ches);
		egu.getColumn("zhancds").setHeader(Local.duns);
		egu.getColumn("beiz").setHeader(Local.beiz);
		
		egu.getColumn("qingcls").setDefaultValue("0");
		egu.getColumn("qingccs").setDefaultValue("0");
		egu.getColumn("qingcds").setDefaultValue("0");
		egu.getColumn("picls").setDefaultValue("0");
		egu.getColumn("piccs").setDefaultValue("0");
		egu.getColumn("picds").setDefaultValue("0");
		egu.getColumn("zhuangcls").setDefaultValue("0");
		egu.getColumn("zhuangccs").setDefaultValue("0");
		egu.getColumn("zhuangcds").setDefaultValue("0");
		egu.getColumn("zaitls").setDefaultValue("0");
		egu.getColumn("zaitcs").setDefaultValue("0");
		egu.getColumn("zaitds").setDefaultValue("0");
		egu.getColumn("daogls").setDefaultValue("0");
		egu.getColumn("daogcs").setDefaultValue("0");
		egu.getColumn("daogds").setDefaultValue("0");
		egu.getColumn("zhancls").setDefaultValue("0");
		egu.getColumn("zhanccs").setDefaultValue("0");
		egu.getColumn("zhancds").setDefaultValue("0");
		

		//�趨�еĳ�ʼ���
		egu.getColumn("id").setWidth(40);
		egu.getColumn("diancxxb_id").setWidth(75);
		egu.getColumn("gongysb_id").setWidth(85);
		egu.getColumn("pinzb_id").setWidth(65);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("riq").setWidth(65);
		egu.getColumn("qingcls").setWidth(45);
		egu.getColumn("qingccs").setWidth(45);
		egu.getColumn("qingcds").setWidth(45);
		egu.getColumn("picls").setWidth(45);
		egu.getColumn("piccs").setWidth(45);
		egu.getColumn("picds").setWidth(45);
		egu.getColumn("zhuangcls").setWidth(45);
		egu.getColumn("zhuangccs").setWidth(45);
		egu.getColumn("zhuangcds").setWidth(45);
		egu.getColumn("zaitls").setWidth(45);
		egu.getColumn("zaitcs").setWidth(45);
		egu.getColumn("zaitds").setWidth(45);
		egu.getColumn("daogls").setWidth(45);
		egu.getColumn("daogcs").setWidth(45);
		egu.getColumn("daogds").setWidth(45);
		egu.getColumn("zhancls").setWidth(45);
		egu.getColumn("zhanccs").setWidth(45);
		egu.getColumn("zhancds").setWidth(45);
		egu.getColumn("beiz").setWidth(45);
		
		
		
		//�趨���ɱ༭�е���ɫ
		egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("shangbzt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.addPaging(18);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		
		
		
		//*************************������*****************************************88
		//�糧������
//		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
//	    egu.getColumn("diancxxb_id").setReturnId(true);
//		
      
		//��������Ϣ��������
		ComboBox  com2=new ComboBox();
		com2.setEditable(true);
		egu.getColumn("gongysb_id").setEditor(com2);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from vwfahr order by mingc"));
		egu.getColumn("gongysb_id").setReturnId(true);
		
//		Ʒ����Ϣ��������
		ComboBox  com1=new ComboBox();
		com1.setEditable(true);
		
		egu.getColumn("pinzb_id").setEditor(com1);
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from vwpinz where leib='ú' order by mingc"));
		egu.getColumn("pinzb_id").setReturnId(true);
		
//		��վ��Ϣ��������
		ComboBox  com6=new ComboBox();
		com6.setEditable(true);
		egu.getColumn("faz_id").setEditor(com6);
		
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from vwchez order by mingc"));
		egu.getColumn("faz_id").setReturnId(true);
		
		
		// ״̬����
//		List list = new ArrayList();
//		list.add(new IDropDownBean(1,"����"));
//		list.add(new IDropDownBean(0,"δ����"));
//		egu.getColumn("zhuangt").setEditor(new ComboBox());
//		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
//		egu.getColumn("zhuangt").setReturnId(true);//���ˣ�ҳ����ʾ���֣�������ʾ���֡�
		
		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		// �糧��
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
//						.getVisit()).getDiancxxb_id(),getTreeid());
//		setTree(etu);
//		egu.addTbarText("-");// ���÷ָ���
//		egu.addTbarText("��λ:");
//		egu.addTbarTreeBtn("diancTree");

		
//		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
//		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�糧�в�����༭
		sb.append("});");
		
		
//		
//		
//       //�趨�ϼ��в�����
//		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return 'continue';}");
//		 
		egu.addOtherScript(sb.toString());
//		//---------------ҳ��js�������--------------------------
		
		egu.addTbarText("-");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButton");
//		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		 ��ӡ��ť
		GridButton gbp = new GridButton("��ѯ", "function (){"
				+ MainGlobal.getOpenWinScript("Rihcdyxxreport&lx=rihcdyxx")
				+ "}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
//		setExtGrid(egu);
//		con.Close();
		
//      ˫��ͷ---
		sb.delete(0, sb.length());
		String Headers = 

			"		 [\n" +
			"         {header:'<table><tr><td width=5 align=center></td></tr></table>', align:'center',rowspan:2},\n" + 
			"         {header:'ID', align:'center',rowspan:2},\n" +	
			"         {header:'�糧����', align:'center',rowspan:2},\n" + 
			"         {header:'������', align:'center',rowspan:2},\n" + 
			"         {header:'Ʒ��', align:'center',rowspan:2},\n" + 
			"         {header:'��վ', align:'center',rowspan:2},\n" + 
			"         {header:'����', align:'center',rowspan:2},\n" + 
			
			"         {header:'�복', colspan:3},\n" + 
			"         {header:'����', colspan:3},\n" + 
			"         {header:'װ��', colspan:3},\n" + 
			"         {header:'��;', colspan:3},\n" + 
			"         {header:'����', colspan:3},\n" + 
			"         {header:'վ��', colspan:3},\n" + 
			
			"         {header:'��ע', align:'center',rowspan:2}\n" + 
			
			"        ],\n" + 
			"        [\n" + 
			"	      {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"	      {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"	      {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"	      {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"	      {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'},\n" + 
			"	      {header:'<table><tr><td  align=center style=border:0>����</td></tr></table>'}\n" + 
			
			"        ]";

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
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
			this.setRiqi(null);
			
			
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			
		
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		Visit visit = (Visit) getPage().getVisit();
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
			+ getTreeid();
			
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
//	 �õ��Ƿ��������ϵͳ���ò���
//	private String getBaohys() {
//		String baohys = "";
//		JDBCcon cn = new JDBCcon();
//		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
//		String sql= "select zhi from xitxxb where mingc='�Ƿ��������' and diancxxb_id="
//				+ diancid;
//		ResultSet rs = cn.getResultSet(sql);
//		try {
//			while (rs.next()) {
//				baohys = rs.getString("zhi");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//
//		return baohys;
//
//	}
	
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
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
		System.out.println("AAAAAAAAAAAA");
		
		System.out.println(getTree().getWindowTreeScript());
		return getTree().getWindowTreeScript();
	}

	//���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-0,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
//	public int getDiancTreeJib() {
//		JDBCcon con = new JDBCcon();
//		int jib = -1;
//		String DiancTreeJib = this.getTreeid();
//		//System.out.println("jib:" + DiancTreeJib);
//		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
//			DiancTreeJib = "0";
//		}
//		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
//		ResultSet rs = con.getResultSet(sqlJib.toString());
//		
//		try {
//			while (rs.next()) {
//				jib = rs.getInt("jib");
//			}
//			rs.close();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}finally{
//			
//			con.Close();
//		}
//
//		return jib;
//	}
	
	
}
