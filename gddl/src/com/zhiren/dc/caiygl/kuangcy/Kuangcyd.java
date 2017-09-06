package com.zhiren.dc.caiygl.kuangcy;

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

public class Kuangcyd extends BasePage {

	/**
	 * ����:���ܱ�
	 * ʱ��:2010-4-21 16:34:55
	 * ����:���ǿ������
	 */
	
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
	private String Markbh = "true"; // ��Ǳ���������Ƿ�ѡ��
	
	public String getMarkbh() {
		return Markbh;
	}

	public void setMarkbh(String markbh) {
		Markbh = markbh;
	}
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
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

		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("BRiq");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRiq').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
	    tb1.addField(dfb);
		
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("������Ϣ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(200);
		CB_GONGYS.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false'; document.forms[0].submit();}");
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
			kais=DateUtil.FormatDate(new Date());
		}
		
		
		
		String  bianh=this.getGongysValue().getValue();
		String newBianh="";
		if(bianh.equals("-")){
			newBianh="";
		}else{
			newBianh=bianh.substring(0, bianh.indexOf(","));
		}
		
		
			
			
			
			String sqlth=


				"select k.huaybh,decode(mk.mingc,null,k.meikdwmc,mk.mingc) as mingc,to_char(k.caiysj,'yyyy-mm-dd') as caiysj from kuangcyb k,meikxxb mk\n" +
				"where k.meikxxb_id=mk.id(+)\n" + 
				"and k.caiysj=to_date('"+kais+"','yyyy-mm-dd')\n" + 
				"and k.huaybh='"+newBianh+"'";

			
			String meik="";
			String caiybm="";
			String caiyrq="";
			
			rs=con.getResultSetList(sqlth);
			
			if(rs.next()){
				meik=rs.getString("mingc");
				caiybm=rs.getString("huaybh");
				caiyrq=rs.getString("caiysj");
				
			}
			
			
			
			
			
			

			String[][] CAIY=new String[][]{
					
			   {"ѡ����뿨�������ң�","ѡ����뿨�������ң�"},
			   {"��������:",caiyrq},
			   {"����:",caiybm},
			   {"",""},	
				
				
				
			   {"ѡ����뿨�������ң�","ѡ����뿨�������ң�"},
			   {"��������:",caiyrq},
			   {"����:",caiybm},
			   {"",""},	
			   
			   {"ѡ����뿨�����أ�","ѡ����뿨�����أ�"},
			   {"��������:",caiyrq},
			   {"����:",caiybm}
			
			};
			

			
			String[][] ArrHeader = new String[11][2];
			int i=0;
			for(int j=0;j<CAIY.length;j++){
				ArrHeader[i++]=CAIY[j];
			}
			int[] ArrWidth = new int[] { 200,400};
			
			Table bt=new Table(11,2);
			rt.setBody(bt);
			
		
			rt.setTitle("��ѡ��������", ArrWidth);
		
			rt.body.merge(1, 1, 1, 2);
			rt.body.merge(5, 1, 5, 2);
			rt.body.merge(9, 1, 9, 2);
			
			
			
			rt.body.merge(4, 1, 4, 2);
			rt.body.merge(8, 1, 8, 2);
			
			
			rt.body.setWidth(ArrWidth);
			
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			
			rt.title.fontSize=14;
			rt.body.fontSize=14;
			rt.body.setRowHeight(30);
			for(int a=1;a<=rt.body.getRows();a++){
				if(a==4||a==8){
					rt.body.setRowHeight(a, 60);
				}else{
					rt.body.setRowHeight(a, 75);
					
				}
					
			}
			
			rt.body.setCellFont(1, 1,"",23, true);
			rt.body.setCellFont(2, 1,"",23, true);
			rt.body.setCellFont(2, 2, "",23,true);
			rt.body.setCellFont(3, 1,"",23, true);
			rt.body.setCellFont(3, 2,"",25, true);
			rt.body.setCellFont(5, 1, "",23,true);
			
			
			rt.body.setCellFont(6, 1, "",23,true);
			rt.body.setCellFont(6, 2, "",23,true);
			
			rt.body.setCellFont(7, 1, "",23,true);
			rt.body.setCellFont(7, 2, "",25,true);
			
			
			rt.body.setCellFont(9, 1, "",23,true);
			rt.body.setCellFont(10, 1, "",23,true);
			rt.body.setCellFont(10, 2, "",23,true);
			rt.body.setCellFont(11, 1, "",23,true);
			
			rt.body.setCellFont(11, 2, "",25,true);
			
			
			
			
			
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
		
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
		if (getMarkbh().equals("true")) { // �ж����getMarkbh()����"true"����ô���³�ʼ�����������
			getGongysModels();
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
		
		String sql_gongys = 


			"select k.id,k.huaybh||','||decode(mk.mingc,null,k.meikdwmc,mk.mingc) from kuangcyb k,meikxxb mk\n" +
			"where k.meikxxb_id=mk.id(+)\n" + 
			"and k.caiysj=to_date('"+kais+"','yyyy-mm-dd')\n" + 
			"order by k.id";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"-"));
		return;
	}
	
}
