package com.zhiren.dc.caiygl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author 
 * ��ɽ��������ѯ
 */
public class Caiyd_hs  extends BasePage implements PageValidateListener{

	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
//	private static final String BAOBPZB_GUANJZ = "CAIYLBCX";// baobpzb�ж�Ӧ�Ĺؼ���
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	private boolean rqchange = false;//�ж������Ƿ�ı�
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (this.briq!=null){
			if(!this.briq.equals(briq)){
				rqchange = true;
			}
		}
		
		this.briq = briq;
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
			this.getSelectData();
		}else if(rqchange){
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
		}
		rqchange = false;
		isBegin = true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;
	
	private String getCaiySql(){
		String riq=this.getBRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		String	sSql =  
			"select m.mingc as kuangb,\n" +
			"       h.hetbh,\n" + 
			"       to_char(min(c.zhongcsj),'yyyy-MM-dd HH24:mi:ss') as kaissj,\n" + 
			"       to_char(max(c.qingcsj),'yyyy-MM-dd HH24:mi:ss') as jiessj,\n" + 
			"       count(c.id) as ches,\n" + 
			"       g.quanc as gongmdw\n" + 
			"from fahb f,meikxxb m,hetb h,chepb c,gongysb g,caiyb cy,zhillsb z,zhuanmb zm\n" + 
			"where f.meikxxb_id = m.id\n" + 
			"      and f.hetb_id = h.id(+)\n" + 
			"      and c.fahb_id = f.id\n" + 
			"      and f.gongysb_id = g.id\n" + 
			"      and f.zhilb_id = z.zhilb_id\n" + 
			"      and zm.zhillsb_id = z.id\n" + 
			"      and zm.zhuanmlb_id = 100661\n" + 
			"      and zm.bianm = '" + getBianhValue().getValue() + "'\n" + 
			"      and cy.zhilb_id = f.zhilb_id\n" + 
			"      and cy.caiyrq = "+ DateUtil.FormatOracleDate(riq) +"\n" + 
			"group by (m.mingc,h.hetbh,g.quanc)\n";

		return sSql;
	}
	
	private String getCaizrSql(){
		String riq=this.getBRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		String	sSql =  
			"select distinct r.quanc as caizr,y.beiz as beiz\n" +
			"from fahb f,meikxxb m,hetb h,chepb c,gongysb g,caiyb cy,zhillsb z,zhuanmb zm,yangpdhb y,caiyryglb cgl,renyxxb r\n" + 
			"where f.meikxxb_id = m.id\n" + 
			"      and f.hetb_id = h.id(+)\n" + 
			"      and c.fahb_id = f.id\n" + 
			"      and f.gongysb_id = g.id\n" + 
			"      and f.zhilb_id = z.zhilb_id\n" + 
			"      and zm.zhillsb_id = z.id\n" + 
			"      and zm.zhuanmlb_id = 100661\n" + 
			"      and zm.bianm = '" + getBianhValue().getValue() + "'\n" + 
			"      and cy.zhilb_id = f.zhilb_id\n" + 
			"      and cy.caiyrq = "+ DateUtil.FormatOracleDate(riq) +"\n" + 
			"      and y.caiyb_id = cy.id\n" + 
			"      and cgl.yangpdhb_id = y.id\n" + 
			"      and cgl.renyxxb_id = r.id\n";

		return sSql;
	}
	
	private String getChephSql(){
		String riq=this.getBRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		String	sSql =  
			"select c.cheph\n" +
			"from fahb f,meikxxb m,hetb h,chepb c,gongysb g,caiyb cy,zhillsb z,zhuanmb zm\n" + 
			"where f.meikxxb_id = m.id\n" + 
			"      and f.hetb_id = h.id(+)\n" + 
			"      and c.fahb_id = f.id\n" + 
			"      and f.gongysb_id = g.id\n" + 
			"      and f.zhilb_id = z.zhilb_id\n" + 
			"      and zm.zhillsb_id = z.id\n" + 
			"      and zm.zhuanmlb_id = 100661\n" + 
			"      and zm.bianm = '" + getBianhValue().getValue() + "'\n" + 
			"      and cy.zhilb_id = f.zhilb_id\n" + 
			"      and cy.caiyrq = "+ DateUtil.FormatOracleDate(riq) +"\n";
		return sSql;
	}

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		String sql = this.getCaiySql();
		ResultSetList rs = con.getResultSetList(sql);
		String kuangb = "";
		String hetbh = "";
		String kaissj = "";
		String jiessj = "";
		String ches = "";
		String gongys = "";

		while(rs.next()){
			kuangb = rs.getString("KUANGB");
			hetbh = rs.getString("HETBH");
			kaissj = rs.getString("KAISSJ");
			jiessj = rs.getString("JIESSJ");
			ches = rs.getString("CHES");
			gongys = rs.getString("GONGMDW");
		}
		
		String sql_czr = this.getCaizrSql();
		rs = con.getResultSetList(sql_czr);
		String caizr = "";
		String beiz="";
		while(rs.next()){
			caizr += "��";
			caizr += rs.getString("CAIZR");
			beiz=rs.getString("beiz");
		}
		if(!(caizr.equals(null)||caizr.equals(""))){
			caizr = caizr.substring(1);
		}else{
			caizr = " ";
		}
		
		String sql_cph = this.getChephSql();
		rs = con.getResultSetList(sql_cph);
		String cheph = "";
		int a=0;
		while(rs.next()){
			a++;
			if(a % 15 == 0){
				cheph += rs.getString("CHEPH")+",<br>";
			}else{
				cheph += rs.getString("CHEPH")+",";
			}
		}
		if(!(cheph.equals(null)||cheph.equals(""))){
			cheph = cheph.substring(0,cheph.length()-1);
		}

		String[][] CAIY=new String[][]{
			{"ú��λ","��ͬ���","���⿪ʼʱ��","�������ʱ��","����"},
			{kuangb,hetbh,kaissj,jiessj,ches},
			{"��ú��λ",gongys,gongys,gongys,gongys},
			{cheph,cheph,cheph,cheph,cheph},
			{beiz,beiz,"������:",caizr,caizr}};
		rs.close();
		
		String[][] ArrHeader = new String[5][5];
		int i=0;
		for(int j=0;j<CAIY.length;j++){
			ArrHeader[i++]=CAIY[j];
		}
		int[] ArrWidth = new int[] { 220,100,120,120,65 };
		
		Table bt=new Table(5,5);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][5];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		
		rt.setTitle("��úȡ����ǩ��¼", ArrWidth);
		
		rt.setDefaultTitle(1,3,"ú�����:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"��������:"+getBRiq(),Table.ALIGN_LEFT);
		
		rt.setDefaultTitle(4,2,"��������:"+ getBianhValue().getValue(),Table.ALIGN_RIGHT);

		//�ϲ�
		rt.body.merge(3, 2, 3, 5);
		rt.body.merge(4, 1, 4, 5);
		rt.body.merge(5, 1, 5, 2);
		rt.body.merge(5, 4, 5, 5);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setCellAlign(3, 2, Table.ALIGN_LEFT);
		rt.body.setCellAlign(4, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(5, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(5, 3, Table.ALIGN_CENTER);
		
		rt.createFooter(1, ArrWidth);
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
//	******************************************************************************
	
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
		private int _CurrentPage = -1;

		public int getCurrentPage() {
			return _CurrentPage;
		}

		public void setCurrentPage(int _value) {
			_CurrentPage = _value;
		}

		private int _AllPages = -1;

		public int getAllPages() {
			return _AllPages;
		}

		public void setAllPages(int _value) {
			_AllPages = _value;
		}
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "caiF");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		
		tb1.addText(new ToolbarText("-")); 
		
		tb1.addText(new ToolbarText("��������:"));
		ComboBox bh = new ComboBox();
		bh.setTransform("BianhDropDown");
		bh.setEditable(true);
		bh.setWidth(100);
		bh.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(bh);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){if(document.all.BianhDropDown.value==-1){Ext.Msg.alert('��ʾ��Ϣ','��ѡ��һ����������!'); return;} document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

//	���

	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getBianhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setBianhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setBianhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getBianhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getBianhModels() {

		String sql =
			"select zm.id,zm.bianm\n" +
			"from zhuanmb zm,fahb f,zhillsb z,caiyb cy\n" + 
			"where zm.zhuanmlb_id = 100661\n" + 
			"      and f.zhilb_id = z.zhilb_id\n" + 
			"      and zm.zhillsb_id = z.id\n" + 
			"      and cy.zhilb_id = f.zhilb_id\n" + 
			"      and f.yunsfsb_id=2\n"+
			"      and cy.caiyrq = "+ DateUtil.FormatOracleDate(getBRiq()) + "\n";


		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

}