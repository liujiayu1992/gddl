package com.zhiren.dc.gdxw.yanmzcx;

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

public class Maozcx extends BasePage {

	/**
	 * ����:���ܱ�
	 * ʱ��:2011-11-16 14:29:24
	 * ����:����ë�����Ʋ�ѯ
	 */
	public boolean IsDay=false;
	private String msg="";
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	
	
	

	private String Markmk = "true"; // ���ú��λ�������Ƿ�ѡ��
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
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

    

	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ú����:"));
		
		
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); " +
		" }");
		tb1.addField(bdf);
		
		
		tb1.addText(new ToolbarText(" �� "));
		DateField edf = new DateField();
		edf.setValue(getERiq());
		//edf.Binding("ERIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		edf.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		"}");
		tb1.addField(edf);
		tb1.addText(new ToolbarText("-"));
		
	
	
		
		
//		�غ���Ա������
		tb1.addText(new ToolbarText("�غ�Ա:"));
		ComboBox cheh = new ComboBox();
		cheh.setTransform("CHEH");
		cheh.setWidth(100);
		cheh.setListeners("select:function(){document.getElementById('Mark_mk').value = 'false';document.forms[0].submit();}");
		cheh.setEditable(true);
		tb1.addField(cheh);
		tb1.addText(new ToolbarText("-"));
		
		
	/*	//��Ӧ��������
		tb1.addText(new ToolbarText("ú��λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(120);
		CB_GONGYS.setListeners("select:function(){document.getElementById('Mark_mk').value = 'false';document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));*/

	
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
			return getJuscx();
			
		
		
	}
	
	
	
	
	public String getJuscx(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //������
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
	
		
	
		
		String chehtj="";
		String cheh=this.getChehValue().getValue();
		if(cheh.equals("ȫ��")){
			chehtj="";
		}else{
			chehtj=" and c.zhongcjjy='"+this.getChehValue().getValue()+"'\n";
		}
		
		
		
		
					sbsql.append("select c.zhongcjjy,to_char(c.daohrq,'yyyy-mm-dd') as daohrq,c.meikdwmc,c.cheph,c.maoz,c.piz,c.koud,(c.maoz-c.piz-c.koud) as jingz,\n");
					sbsql.append("c.zhongchh,to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,c.qingcjjy,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,c.meigy\n");
					sbsql.append("from chepbtmp c\n");
					sbsql.append(" where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n");
					sbsql.append("and  c.daohrq<to_date('"+jies+"','yyyy-mm-dd')+1\n");
					sbsql.append("and c.maoz>0\n");
					sbsql.append(chehtj);
					sbsql.append("order by c.zhongcjjy, c.daohrq,c.zhongchh,c.zhongcsj");
				
		
			
				
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][13];
//				1120
				ArrHeader[0]=new String[] {"�غ�Ա","��ú����","ú��","����","ë��","Ƥ��","�۶�","����","�س����","�س�ʱ��","�պ�Ա","��Ƥʱ��","��ú��"};
				int ArrWidth[]=new int[] {80,90,100,75,50,50,50,50,40,100,70,100,60};
				  strFormat = new String[] { "","", "","","" , "","","","","","","",""};
				// ����
				rt.setTitle(visit.getDiancmc()+"��������ë�����Ʋ�ѯ",ArrWidth);
				rt.setDefaultTitle(1, 3, "����:"+kais+" �� "+jies, Table.ALIGN_LEFT);
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500000);
			
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				
				
				
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				
				
				
				
				for (int i=2;i<rt.body.getRows();i++){
					//�õ���һ�е�ֵ
					String shang_str_maoz=rt.body.getCellValue(i, 5);
					double Shang_maoz=Double.parseDouble(shang_str_maoz);
					String shang_daohrq=rt.body.getCellValue(i, 1);//��������
					String shang_hengh=rt.body.getCellValue(i, 9);//���
					
					
					//�õ���һ�е�ֵ
					String xia_str_maoz=rt.body.getCellValue(i+1, 5);
					double xia_maoz=Double.parseDouble(xia_str_maoz);
					String xia_daohrq=rt.body.getCellValue(i+1, 1);
					String xia_hengh=rt.body.getCellValue(i+1, 9);//���
					
					
					if(shang_daohrq.equals(xia_daohrq)&&shang_hengh.equals(xia_hengh)){//���������ںͺ����ͬʱ�����ж�
//						��һ����ʾ���
						if(Shang_maoz<=xia_maoz&&Shang_maoz+0.2>=xia_maoz){
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="red";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
						//��һ����ʾ���
						if(Shang_maoz<=xia_maoz&&Shang_maoz+0.2>=xia_maoz){
							
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i+1, j).backColor="red";
								rt.body.getCell(i+1, j).fontBold=true;
							}
						}
					}
						
					
					
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setFontSize(10);
				rt.body.setRowHeight(20);
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
	

	

	

	
//	 �غ���Ա������
	public IDropDownBean getChehValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getChehModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setChehValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setChehModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getChehModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getChehModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getChehModels() {
		String sql_gongys =
			"select rownum as xuh,zhongcjjy from (\n" +
			"select distinct c.zhongcjjy from chepbtmp c)\n";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	
	
	
	
}
