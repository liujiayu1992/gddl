package com.zhiren.dc.gdxw.zhiycx;

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

public class Zhiycx extends BasePage {

	/**
	 * ����:wzb
	 * ʱ��:2009-9-24 10:15:06
	 * ����:����������500�ֲ�ѯ
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb�ж�Ӧ�Ĺؼ���
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ú����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("Ͱ��:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(150);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
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
		
		
		
		

		
		//�����һ�����ƾ���ʾ�糧��,������ǾͲ���ʾ�糧
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
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
		Report rt = new Report(); //������
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		
		long meik=this.getGongysValue().getId();
		
		
		
		if(meik==-1){

			sbsql.append("select rownum as xuh,a.mingc,a.jingz,zm.bianm from\n"
							+ "zhuanmb zm,\n"
							+ "(select wz.mingc,c.jingz,c.zhilb_id,\n"
							+ "(select ls.id from zhillsb ls where ls.zhilb_id=c.zhilb_id) as zhillsb_id\n"
							+ "from gdxw_cy c,caiyb cy,cunywzb wz\n"
							+ "where c.zhilb_id=cy.zhilb_id\n"
							+ "and cy.cunywzb_id=wz.id\n"
							+ "and c.zhuangt=1\n"
							+ "and c.zhiyrq>=to_date('"+kais+"','yyyy-mm-dd')\n"
							+ "and c.zhiyrq<to_date('"+jies+"','yyyy-mm-dd')+1) a\n"
							+ "where zm.zhillsb_id=a.zhillsb_id\n"
							+ "and zm.zhuanmlb_id=100663");
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][4];
//				1120
				ArrHeader[0]=new String[] {"���","Ͱ��","ú��","�������"};
				int ArrWidth[]=new int[] {50,80,80,80};
				  strFormat = new String[] { "", "","0.00","" };
				// ����
				rt.setTitle(visit.getDiancmc()+"��������Ͱ���б�",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(50);
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(21);
				return rt.getAllPagesHtml();// ph;
			
		}else{
			
			
			
			String sqlth=
				"select  a.mingc,a.jingz,a.zhilb_id,zm.bianm,to_char(a.zhiyrq,'yyyy-mm-dd') as zhiyrq,a.renyxxb_id\n" +
				"from\n" + 
				"zhuanmb zm,\n" + 
				"(select wz.mingc,c.jingz,c.zhilb_id,ry.zhiyrq,r.quanc as renyxxb_id,ry.beiz,ry.id,\n" + 
				"(select ls.id from zhillsb ls where ls.zhilb_id=c.zhilb_id) as zhillsb_id\n" + 
				"from gdxw_cy c,caiyb cy,cunywzb wz,zhiyryb ry,renyxxb r\n" + 
				"where c.zhilb_id=cy.zhilb_id\n" + 
				"and cy.cunywzb_id=wz.id\n" + 
				"and c.zhuangt=1\n" + 
				"and c.zhiyrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
				"and c.zhiyrq<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
				"and c.zhilb_id=ry.zhilb_id(+)\n" + 
				"and cy.cunywzb_id="+meik+"\n" + 
				"and ry.renyxxb_id=r.id(+)) a\n" + 
				"where zm.zhillsb_id=a.zhillsb_id\n" + 
				"and zm.zhuanmlb_id=100663";
			
			String zhiyy="";
			String zhiyrq="";
			String zhiybh="";
			String tongh="";
			rs=con.getResultSetList(sqlth);
			
			if(rs.next()){
				zhiyy=rs.getString("renyxxb_id");
				zhiyrq=rs.getString("zhiyrq");
				zhiybh=rs.getString("bianm");
				tongh=rs.getString("mingc");
			}
			
			
			
			
			
			

			String[][] CAIY=new String[][]{
				{"����������˾","����������˾"},
				{"Ͱ��:",tongh},
				{"��������:",zhiyrq},
				{"�������:",zhiybh},
				{"����Ա:",zhiyy},
				{"������Ŀ:","ȫˮ��"},
				{"",""},
				{"����������˾","����������˾"},
				{"Ͱ��:",tongh},
				{"��������:",zhiyrq},
				{"�������:",zhiybh},
				{"����Ա:",zhiyy},
				{"������Ŀ:","ú������"},
				{"",""},
				{"����������˾","����������˾"},
				{"Ͱ��:",tongh},
				{"��������:",zhiyrq},
				{"�������:",zhiybh},
				{"����Ա:",zhiyy},
				{"������Ŀ:","����"}
			};
			

			
			String[][] ArrHeader = new String[20][2];
			int i=0;
			for(int j=0;j<CAIY.length;j++){
				ArrHeader[i++]=CAIY[j];
			}
			int[] ArrWidth = new int[] { 150,150};
			
			Table bt=new Table(20,2);
			rt.setBody(bt);
			
			/*String[][] ArrHeader1 = new String[1][2];
			ArrHeader1[0] = ArrHeader[0];
			rt.body.setHeaderData(ArrHeader1);// ��ͷ����
*/			
			rt.setTitle(tongh+"������", ArrWidth);
		
			rt.body.merge(1, 1, 1, 2);
			rt.body.merge(7, 1, 7, 2);
			rt.body.merge(8, 1, 8, 2);
			rt.body.merge(14, 1, 14, 2);
			rt.body.merge(15, 1, 15, 2);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.title.fontSize=12;
			rt.body.fontSize=12;
			rt.body.setRowHeight(30);
			rt.body.setRowHeight(14, 70);

			
			
			
			
		rt.createFooter(1, ArrWidth);


			rt.footer.fontSize=11;
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			rt.body.ShowZero = true;
			con.Close();
			return rt.getAllPagesHtml();
			
			
			
			
		}
		
		
		
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			//visit.setInt1(Integer.parseInt(reportType));
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			setERiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);

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
			getGongysModels();
			getSelectData();
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
		String kais=this.getBRiq();
		String jies=this.getERiq();
		String sql_gongys = 
			"select wz.id,wz.mingc\n" +
			"from gdxw_cy c,caiyb cy,cunywzb wz\n" + 
			"where c.zhilb_id=cy.zhilb_id\n" + 
			"and cy.cunywzb_id=wz.id\n" + 
			"and c.zhuangt=1\n" + 
			"and c.zhiyrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
			"and c.zhiyrq<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
			"order by wz.mingc";

		
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
}
