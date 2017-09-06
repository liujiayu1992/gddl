package com.zhiren.dc.caiygl.choucy;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�songy
 * ʱ�䣺2011-03-23 
 * �������޸������˵�������Ҫ�������ƽ�������
 */
public class Choucyhybb extends BasePage {
	

//	�����û���ʾ
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
//	������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
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

    

    



	

	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); }");
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.forms[0].submit();}");
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("ú��λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(150);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		
		
//		����Ա������
		tb1.addText(new ToolbarText("����Ա:"));
		ComboBox cy = new ComboBox();
		cy.setTransform("CAIYY");
		cy.setWidth(100);
		cy.setListeners("select:function(){document.forms[0].submit();}");
		cy.setEditable(true);
		tb1.addField(cy);
		tb1.addText(new ToolbarText("-"));
		
//		�糧Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		
		

		
	
		
		

		tb1.setWidth("bodyWidth");
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
			return getYansd();
		
	}
	
	
	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//�������
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		long meik=this.getGongysValue().getId();
		String meiktiaoj="";
		if(meik==-1){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikxxb_id= "+meik;
		}
		
		String caiyy=this.getCaiyyValue().getValue();
		String caiyytiaoj="";
		if(caiyy.equals("ȫ��")){
			caiyytiaoj="";
		}else{
			caiyytiaoj="  and c.caiyy='"+caiyy+"' \n";
		}
		
		sbsql.append(


				"select to_char(c.caiysj,'yyyy-mm-dd') as caiysj,min(m.mingc),c.huaybh,\n" +
				"round_new(sum(c.mt*1)/sum(1),1) as mt,\n" + 
				"round_new(sum(c.mad*1)/sum(1),2) as mad,\n" + 
				"round_new(sum(c.aad*1)/sum(1),2) as aad,\n" + 
				"round_new(sum(c.ad*1)/sum(1),2) as ad,\n" + 
				"round_new(sum(c.vad*1)/sum(1),2) as vad,\n" + 
				"round_new(sum(c.vdaf*1)/sum(1),2) as vdaf,\n" + 
				//�����糧ȥ���̶�̼
			//	"round_new(sum(c.fcad*1)/sum(1),2) as fcad,\n" + 
				"round_new(sum(c.stad*1)/sum(1),2) as stad,\n" + 
				"round_new(sum(c.std*1)/sum(1),2) as std,\n" + 
				"round_new(sum(c.had*1)/sum(1),2) as had,\n" + 
				"round_new(sum(c.qbad*1)/sum(1),2) as qbad,\n" + 
				"round_new(sum(c.qgrd*1)/sum(1),2) as qgrd,\n" + 
				"round_new(sum(c.qnet_ar*1)/sum(1),2) as qnet_ar,\n" + 
				"round_new(sum(c.qnet_ar*1)/sum(1)*1000/4.1816,0) as dak,\n" + 
				"min(c.caiyy) as huayy,\n" + 
				"min(c.pizr) as huayy,\n" + 
				"min(c.huayy) as huayy\n" + 
				"from choucyb c ,meikxxb m\n" + 
				"where c.meikxxb_id=m.id\n" + 
				"and c.caiysj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
				"and c.caiysj<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
				""+meiktiaoj+""+
				""+caiyytiaoj+""+
				"and c.shenhzt=5\n" + 
				"group by rollup (c.caiysj,c.huaybh)\n" + 
				"having not grouping(c.caiysj)+grouping(c.huaybh)=1 \n"+
				"order by  c.caiysj,c.huaybh"


		);
		
			rs=con.getResultSetList(sbsql.toString());
			
			
			
			Report rt = new Report(); //������
			String ArrHeader[][]=new String[1][19];
//			1120
			ArrHeader[0]=new String[] {"��������","ú������","���","ȫˮ<br>Mt(%)","��ˮ<br>Mad(%)","�ոɻ�<br>Aad(%)",
					"�ɻ�<br>Ad(%)","�ոɻӷ���<br>Vad(%)","�����޻һ��ӷ���<br>Vdaf(%)",
					"���������ȫ��<br>St,ad(%)","�����ȫ��<br>St,d(%)","�����������<br>Had(%)","��Ͳ��<br>Qb.ad(%)",
					"��λ��<br>Qgr,d(%)","��λ��<br>Qnet,ar(%)","��λ������<br>(��)","������","����<br>��׼��","����Ա"};
			int ArrWidth[]=new int[] {80,100,80,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45};
			  strFormat = new String[] { "", "","",
	    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00",  "0.00", 
	    				"0.00", "0.00","0.00", "0.00","","","" ,"",""};
			// ����
			rt.setTitle("ú�ʻ������ĳ��������",ArrWidth);
			rt.setDefaultTitle(1,3,"��λ:"+visit.getDiancqc(),Table.ALIGN_LEFT);
			
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
			rt.body.setPageRows(23);
			
			
			int iLastRow=rt.body.getRows();
			int yangges=iLastRow-2;
			if(iLastRow>1){
				rt.body.setCellValue(iLastRow, 2, "�ϼ�:"+yangges+"����");
				rt.body.setCellValue(iLastRow, 1, "");
				rt.body.setCellValue(iLastRow, 17, "");
				rt.body.setCellValue(iLastRow, 18, "");
				rt.body.setCellValue(iLastRow, 19, "");
				
			}
//			�趨С���еı���ɫ������
			for (int i=2;i<=rt.body.getRows();i++){
				String xiaoj=rt.body.getCellValue(i, 1);
				if((xiaoj.equals(""))){
					for (int j=0;j<rt.body.getCols()+1;j++){
						rt.body.getCell(i, j).backColor="silver";
						rt.body.getCell(i, j).fontBold=true;
					}
				}
			}
			
			
			rt.body.setWidth(ArrWidth);

			rt.body.setHeaderData(ArrHeader);// ��ͷ����

			rt.body.ShowZero = true;
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			

			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 4, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			con.Close();
	     	return rt.getAllPagesHtml();// ph;
	}
	
	
	
	

	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			setChangbValue(null);
			setChangbModel(null);
//			visit.setDefaultTree(null);
//			setDiancmcModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		
		if (_RefurbishChick) {
			_RefurbishChick = false;
		
			
		}
		getSelectData();
	}
//	ҳ���½��֤
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
	
	
	
	
	
	
	
	
//	 ú��������
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		
		String sql_gongys = "select g.id,g.piny||'-'||g.mingc from meikxxb g order by g.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	
	

	
	
//	 ����Ա������
	public IDropDownBean getCaiyyValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getCaiyyModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setCaiyyValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setCaiyyModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getCaiyyModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getCaiyyModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getCaiyyModels() {
		
		String sql_gongys = "select r.id,r.quanc from renyxxb r where r.bum='��úԱ' order by r.quanc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
}