package com.zhiren.dc.jilgl.baob;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jinmtz extends BasePage {
	
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
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
//		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		
		dfb.Binding("BRiq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERiq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	public String getPrintTable(){
		return getMeikmx();
	}

	public String getMeikmx(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
//		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}

		Report rt = new Report(); //������
		sbsql.setLength(0);
		sbsql.append(
				"SELECT DECODE(GROUPING(TO_CHAR(F.DAOHRQ, 'yyyy-mm-dd')) + GROUPING(G.MINGC),\n" +
				"       2,'�ܼ�',1,'��С��',TO_CHAR(F.DAOHRQ, 'yyyy-mm-dd')) DAOHRQ,\n" + 
				"       G.MINGC GONGYSB_ID,\n" + 
				"       J.MINGC JIHKJB_ID,\n" + 
				"       ROUND(SUM(CHES),0) CHES,\n" + 
				"       SUM(LAIMSL) LAIMSL,\n" + 
				"       SUM(BIAOZ) BIAOZ,\n" + 
				"       SUM(JINGZ) JINGZ,\n" + 
				"       SUM(MAOZ) MAOZ,\n" + 
				"       SUM(PIZ) PIZ,\n" + 
				"       SUM(YUNS) YUNS,\n" + 
				"       SUM(YINGK) YINGK,\n" + 
				"       SUM(ZONGKD) ZONGKD\n" + 
				"  FROM FAHB F, GONGYSB G, JIHKJB J\n" + 
				" WHERE F.GONGYSB_ID = G.ID\n" + 
				"   AND F.JIHKJB_ID = J.ID\n" + 
				"   AND F.DAOHRQ BETWEEN DATE '"+kais+"' AND DATE '"+jies+"'\n" + 
				" GROUP BY ROLLUP(TO_CHAR(F.DAOHRQ, 'yyyy-mm-dd'), (G.MINGC, J.MINGC))\n" + 
				" ORDER BY GROUPING(TO_CHAR(F.DAOHRQ, 'yyyy-mm-dd')) DESC,\n" + 
				"       TO_CHAR(F.DAOHRQ, 'yyyy-mm-dd'),GROUPING(G.MINGC) DESC,G.MINGC"
		);
		rs=con.getResultSetList(sbsql.toString());
		
		String ArrHeader[][]=new String[1][12];
//			1120
		ArrHeader[0]=new String[] {"��������","��ú��λ","�ƻ��ھ�","����","ʵ����","Ʊ��","����","ë��","Ƥ��","����","ӯ��","�ܿ���"};
		int ArrWidth[]=new int[] {80,150,60,60,60,60,60,60,60,60,60,60};
		strFormat = new String[] { "","","", "0", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"};
		// ����
		rt.setTitle("�ս�ų́�ʻ���",ArrWidth);
		rt.setDefaultTitle(1, 5, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, "��λ:��", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setColFormat(strFormat);
//					�趨С���еı���ɫ������
		for (int i=2;i<=rt.body.getRows();i++){
			String xiaoj=rt.body.getCellValue(i, 2);
			if((xiaoj.equals(""))){
				for (int j=0;j<rt.body.getCols()+1;j++){
					rt.body.getCell(i, j).backColor="silver";
					rt.body.getCell(i, j).fontBold=true;
				}
			}
		}
		rt.body.mergeFixedCol(1);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(30);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.body.ShowZero = true;
		rs.close();
		con.Close();
//			���ö��䷽ʽ
		for (int i=1;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();
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
}
