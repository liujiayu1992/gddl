package com.zhiren.dc.gdxw.xwhuaybb;

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

public class Xwhuaybb3 extends BasePage {
	

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

    




	
//	��ȡ������
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"����ԭʼ��¼";
		
		return sb;
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
		tb1.addText(new ToolbarText("��������:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(150);
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		

		


		
		

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
		
		String meik=this.getGongysValue().getValue();
		
	
	    if(meik.equals("�볧����")){
	    	sbsql.append(
					

					"select zm.bianm,ls.mt,ls.mad,ls.aad,ls.vad,ls.stad,\n" +
					"ls.had,ls.qbad,ls.qnet_ar,to_char(ls.huaysj,'yyyy-mm-dd') as huaysj,\n" + 
					"ls.huayy\n" + 
					"from zhillsb ls ,zhuanmb zm,zhuanmlb lb\n" + 
					"where (ls.shenhzt=7 or ls.shenhzt=3)\n" + 
					"and zm.zhillsb_id=ls.id\n" + 
					"and zm.zhuanmlb_id=lb.id\n" + 
					"and lb.mingc='�������'\n" + 
					"and ls.huaysj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and ls.huaysj<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and ls.qnet_ar<>0\n" + 
					"order by ls.huaysj"


			);
	    }else if(meik.equals("�����")){
	    	sbsql.append(
					

	    			"select c.huaybh,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.qnet_ar,\n" +
	    			"to_char(c.huaysj,'yyyy-mm-dd') as huaysj,c.huayy\n" + 
	    			"from choucyb c where (c.shenhzt = 3 or c.shenhzt = 5)\n" + 
	    			"and c.huaysj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
	    			"and c.huaysj<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
	    			"and c.qnet_ar<>0\n" + 
	    			"order by c.huaysj,c.huaybh"


			);
	    }else{//�����
	    	sbsql.append(
					


	    			"select c.huaybh,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.qnet_ar,\n" +
	    			"to_char(c.huaysj,'yyyy-mm-dd') as huaysj,c.huayy\n" + 
	    			"from kuangcyb c where (c.shenhzt = 3 or c.shenhzt = 5)\n" + 
	    			"and c.huaysj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
	    			"and c.huaysj<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
	    			"and c.qnet_ar<>0\n" + 
	    			"order by c.huaysj,c.huaybh"


			);
	    }
			
		
		
			rs=con.getResultSetList(sbsql.toString());
			
			
			
			Report rt = new Report(); //������
			String ArrHeader[][]=new String[1][11];
//			1120
			ArrHeader[0]=new String[] {"���","ȫˮ","��ˮ","�ҷ�","�ӷ���","���","��","��Ͱ��","��λ��","��������","����Ա"};
			int ArrWidth[]=new int[] {80,60,60,60,60,60,60,60,60,100,70};
			  strFormat = new String[] { "",
	    				"0.0", "0.00", "0.00", "0.00", "0.00","0.00", "0.00",  "0.00", 
	    				"", ""};
			// ����
			rt.setTitle(this.getGongysValue().getValue()+"ԭʼ��¼",ArrWidth);
			rt.setDefaultTitle(1,3,"��λ:"+visit.getDiancqc(),Table.ALIGN_LEFT);
			
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
			rt.body.setPageRows(300);
			
			
		
			
			
			
			rt.body.setWidth(ArrWidth);

			rt.body.setHeaderData(ArrHeader);// ��ͷ����

			rt.body.ShowZero = true;
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			//rt.body.setColAlign(2, Table.ALIGN_LEFT);

			rt.createFooter(1, ArrWidth);
			//rt.setDefautlFooter(1, 4, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			con.Close();
	     	return rt.getAllPagesHtml();// ph;
	}
	
	
	
	

	

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

		
			
			
		}
		
		
		getSelectData();
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
	
	
	
	
	
	
	
//	 ��Ӧ��������
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
		
		List list=new ArrayList();
		list.add(new IDropDownBean("1","�볧����"));
		list.add(new IDropDownBean("2","�����"));
		list.add(new IDropDownBean("3","�����"));
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}
	
}
