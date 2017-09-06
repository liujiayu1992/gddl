package com.zhiren.gangkjy.baobgl.yueb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


public class Caigxsmzcx  extends BasePage implements PageValidateListener{

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
	
	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
		
		if (_FindChick) {
			_FindChick = false;
	    }
	}

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		StringBuffer strSQL = new StringBuffer();

		String strDianc = "";
		if(this.getDiancTreeJib()==1){//����
			strDianc = "";
		}else if(this.getDiancTreeJib()==2){
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
		String daohrq = " and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())+"\n"
						+"and f.daohrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" ;
		String fahrq = " and sl.fahrq>="+DateUtil.FormatOracleDate(getBRiq())+"\n"
						+"and sl.fahrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" ;
		String sql = "";
		String biaot="�ɹ�����ú�ʲ�ѯ��ϸ��";
		String Dformat="'yyyy-mm-dd'";
		String GP="group by rollup (du.dcmc,du.hz,du.riq)";
		if(getBBLXValue().getValue().equals("����")){
			Dformat="'yyyy-mm'";
			GP="group by rollup (du.dcmc,(du.hz,du.riq))";
			biaot="�ɹ�����ú�ʲ�ѯ���ܱ�";
		}
		
		String ywlx="";
		if(getYWLXValue().getValue().equals("ֱ��")){
			ywlx="(3)";
		}else if(getYWLXValue().getValue().equals("���ؽ���")){
			ywlx="(2)";
		}else{
			ywlx="(2,3)";
		}
		
		sql="select du.dcmc,\n" +
			"decode(grouping(du.hz)+grouping(du.riq)+grouping(du.dcmc),3,'�ܼ�',2,'�ϼ�',1,'��С��',0,du.riq) as riq,\n" + 
			"sum(fah.laiml) as laiml,\n" + 
			"decode(sum(fah.laiml),0,0,round(sum(fah.laiml*fah.farl)/sum(fah.laiml),0)) as farl,\n" + 
			"sum(xs.meil) as meil,\n" + 
			"decode(sum(xs.meil),0,0,round(sum(xs.meil*xs.farl2)/sum(xs.meil),0)) as farl2\n" + 
			"from\n" + 
			"(select dc.mingc as dcmc, to_char(f.daohrq,"+Dformat+") as daohrq,\n" + 
			"       sum(f.jingz) laiml,\n" + 
			"       decode(sum(f.jingz),0,0,round(sum(f.jingz*round(z.qnet_ar*7000/29.271,0))/sum(f.jingz),0)) farl\n" + 
			" from fahb f,zhilb z,diancxxb dc\n" + 
			" where f.zhilb_id=z.id  and dc.id=f.diancxxb_id "+daohrq+strDianc+" and f.yewlxb_id in "+ywlx+"\n" + 
			" group by (dc.mingc,daohrq)\n" + 
			" ) fah,\n" + 
			"( select dc.mingc as dcmc, to_char(sl.fahrq,"+Dformat+") as fahrq,\n" + 
			"         sum(sl.meil)as meil,\n" + 
			"         decode(sum(sl.meil),0,0,round(sum(sl.meil*round(fyzl.qnet_ar*7000/29.271,0))/sum(sl.meil),0)) farl2\n" + 
			" from fayslb sl,zhilb fyzl,diancxxb dc\n" + 
			" where sl.zhilb_id=fyzl.id  and dc.id=sl.diancxxb_id "+fahrq+strDianc+" and sl.yewlxb_id in "+ywlx+"\n" + 
			" group by (dc.mingc,fahrq)\n" + 
			" )xs,\n" + 
			" (select distinct to_char(f.daohrq,'yyyy-mm') as hz, dc.mingc as dcmc, to_char(f.daohrq,"+Dformat+") as riq from fahb f, diancxxb dc, zhilb fzl " +
					"where dc.id=f.diancxxb_id and fzl.id=f.zhilb_id "+daohrq+strDianc+" and f.yewlxb_id in "+ywlx+" \n" + 
			"  union\n" + 
			"  select distinct to_char(sl.fahrq,'yyyy-mm') as hz, dc.mingc as dcmc, to_char(sl.fahrq,"+Dformat+") as riq from fayslb sl, diancxxb dc, zhilb fyzl " +
					"where dc.id=sl.diancxxb_id and fyzl.id=sl.zhilb_id "+fahrq+strDianc+" and sl.yewlxb_id in "+ywlx+" ) du\n" + 
			"  where du.riq=fah.daohrq(+) and du.riq=xs.fahrq(+) and du.dcmc=fah.dcmc(+) and du.dcmc=xs.dcmc(+)\n" + 
			GP+"\n" + 
			" order by grouping(du.dcmc) desc,du.dcmc,grouping(du.hz) desc, du.hz, grouping(du.hz)+grouping(du.riq) desc, riq";
		
		strSQL.append(sql);
		String ArrHeader[][]=new String[2][6];
		ArrHeader[0]=new String[] {"�糧����","����","��ú","��ú","����","����"};
		ArrHeader[1]=new String[] {"�糧����","����","����","����","����","����"};

		int ArrWidth[]=new int[] {80,100,75,75,75,75};

		ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		
		Table tb=new Table(rs, 2, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle(biaot, ArrWidth);
//		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(1, 6, "��ѯ����:"+getBRiq()+"��"+getERiq(), Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(33);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		
		
		
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(4, 2, "���:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "�Ʊ�:", Table.ALIGN_LEFT);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
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
		
//	public Date getYesterday(Date dat){
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(dat);
//		cal.add(Calendar.DATE,-1);
//		return cal.getTime();
//	}
	
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

		tb1.addText(new ToolbarText("�������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
//		 ��
		tb1.addText(new ToolbarText("��λ����:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
		((Visit)getPage().getVisit()).setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("ҵ������"));
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("YWLX");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(85);
		comb1.setReadOnly(true);
		comb1.setId("YWLX");
		comb1.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(comb1);
		
		tb1.addText(new ToolbarText("��������"));
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("BBLX");
		comb2.setEditable(false);
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(70);
		comb2.setReadOnly(true);
		comb2.setId("BBLX");
		comb2.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(comb2);
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
//	�󶨱�������
	private IDropDownBean _BBlx;
	public IDropDownBean getBBLXValue() {
		if (_BBlx == null) {
			_BBlx = (IDropDownBean) getBBLXModel().getOption(0);
		}
		return _BBlx;
	}
	public void setBBLXValue(IDropDownBean Value) {
		_BBlx = Value;
	}

	private IPropertySelectionModel _BBlxModel;
	public void setBBLXModel(IPropertySelectionModel value) {
		_BBlxModel = value;
	}
	public IPropertySelectionModel getBBLXModel() {
		if (_BBlxModel == null) {
			getBBLXModels();
		}
		return _BBlxModel;
	}

	public void getBBLXModels() {
		
		List List = new ArrayList();
		List.add(new IDropDownBean(0, "����"));
		List.add(new IDropDownBean(1, "��ϸ"));
		_BBlxModel = new IDropDownModel(List);

	}
	
//	��ҵ������
	private IDropDownBean _YWlx;
	public IDropDownBean getYWLXValue() {
		if (_YWlx == null) {
			_YWlx = (IDropDownBean) getYWLXModel().getOption(0);
		}
		return _YWlx;
	}
	public void setYWLXValue(IDropDownBean Value) {
		_YWlx = Value;
	}

	private IPropertySelectionModel _YWlxModel;
	public void setYWLXModel(IPropertySelectionModel value) {
		_YWlxModel = value;
	}
	public IPropertySelectionModel getYWLXModel() {
		if (_YWlxModel == null) {
			getYWLXModels();
		}
		return _YWlxModel;
	}

	public void getYWLXModels() {
		
		List List = new ArrayList();
		List.add(new IDropDownBean(0, "ȫ��"));
		List.add(new IDropDownBean(1, "ֱ��"));
		List.add(new IDropDownBean(2, "���ؽ���"));
		_YWlxModel = new IDropDownModel(List);

	}

//	��ӵ糧��
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
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
	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}


//	******************ҳ���ʼ����********************//
		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

			Visit visit = (Visit) getPage().getVisit();
			
			if (!visit.getActivePageName().toString().equals(
					this.getPageName().toString())) {
				// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
				visit.setActivePageName(getPageName().toString());
				setBRiq(DateUtil.FormatDate(getMonthFirstday(new Date())));
				setERiq(DateUtil.FormatDate(new Date()));
				visit.setProSelectionModel1(null);
				visit.setDropDownBean1(null);
				setBBLXValue(null);
				setBBLXModel(null);
				setTreeid(null);
			}
			isBegin=true;
			getToolBars();
			this.getSelectData();
			
		}

		public String getPrintTable(){
			if(!isBegin){
				return "";
			}
			isBegin=false;
		
			return getSelectData();
		
		}

		private boolean isBegin=false;


}