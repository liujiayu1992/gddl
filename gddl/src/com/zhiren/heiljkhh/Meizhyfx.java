package com.zhiren.heiljkhh;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

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

/**
 * @author ly
 *
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */

public class Meizhyfx  extends BasePage implements PageValidateListener{

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
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
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
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setList1(null);
			setJihkjValue(null);
			setJihkjModel(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
			this.getSelectData();
		}
		isBegin=true;
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

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String sql_dc = "";
		String qc = "";
		StringBuffer strSQL = new StringBuffer();
		
		String diancxxbid = " ";
		String jihkj = "";
		if(getDiancTreeJib()==2){
			diancxxbid = "and (d.id = "+getTreeid_dc()+" or d.fuid = "+getTreeid_dc()+")\n";
		}else if(getDiancTreeJib()==3){
			diancxxbid = "and d.id = "+getTreeid_dc()+"\n";
		}
		
		if(getJihkjValue().getValue().equals("ȫ��")){
			jihkj = " ";
		}else{
			jihkj = "		and j.id = " + getJihkjValue().getId() + "\n";
		}
		String sql = 
			"select decode(x.daohrq,null,'�ϼ�',rownum),\n" +
			"       x.daohrq,x.kuangm,x.jihkj,x.shul,x.qnet_ar,x.mt,x.mad,x.ad,x.vdaf,x.stad\n" + 
			"from\n" + 
			"(select to_char(f.daohrq,'yyyy-MM-dd') as daohrq,\n" + 
			"        m.quanc as kuangm,\n" +
			"		 j.mingc as jihkj,\n" +
			"        sum(round_new(f.laimsl,0)) as shul,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as qnet_ar,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.mt)/sum(round_new(f.laimsl,0)),1)) as mt,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.mad)/sum(round_new(f.laimsl,0)),2)) as mad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.ad)/sum(round_new(f.laimsl,0)),2)) as ad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.vdaf)/sum(round_new(f.laimsl,0)),2)) as vdaf,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.stad)/sum(round_new(f.laimsl,0)),2)) as stad\n" + 
			"from fahb f,zhilb z,meikxxb m,diancxxb d,jihkjb j\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.meikxxb_id = m.id\n" + 
			"	   and f.jihkjb_id = j.id\n" +
			jihkj +
			" 	   and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   and f.daohrq<="+DateUtil.FormatOracleDate(getERiq()) + "\n" +
			"	   and f.diancxxb_id = d.id\n" +
			diancxxbid +
			"group by rollup(f.daohrq,m.quanc,j.mingc)\n" + 
			"having not (grouping(f.daohrq)||grouping(j.mingc))=1\n" + 
			"order by f.daohrq,m.quanc\n" + 
			") x";

		strSQL.append(sql);
		
		String ArrHeader[][]=new String[1][11];
		ArrHeader[0]=new String[] {"���","��úʱ��","����","�ƻ��ھ�","����","��ֵ","ȫˮ��","���������ˮ��","������ҷ�","�����޻һ��ӷ���","���������ȫ��"};

		int ArrWidth[]=new int[] {30,100,180,80,60,60,60,60,60,60,60};

		ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		sql_dc = "select quanc from diancxxb where id = " + getTreeid_dc();
		ResultSetList rsl = cn.getResultSetList(sql_dc);
		if(rsl.next()){
			qc = rsl.getString("QUANC");
		}
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle(qc+"ú�ʻ������", ArrWidth);
		rt.setDefaultTitle(1, 2, "���λ��", Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 6, getBRiq() + " �� " + getERiq(), Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 3, "��λ��", Table.ALIGN_LEFT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		
//		ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9,3,"ͳ��:",Table.ALIGN_LEFT);

			
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��úʱ��:"));
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
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�ƻ��ھ�:"));
		ComboBox fh = new ComboBox();
		fh.setTransform("JihkjSelect");
		fh.setWidth(130);
		fh.setListeners("select:function(own,rec,index){Ext.getDom('JihkjSelect').selectedIndex=index}");
		tb1.addField(fh);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
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
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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
	
//	-------------------------�糧Tree END----------
	
//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid_dc();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}
	
//	�ƻ��ھ�������
	public IDropDownBean getJihkjValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getJihkjModel().getOptionCount()>0) {
				setJihkjValue((IDropDownBean)getJihkjModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setJihkjValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getJihkjModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setJihkjModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setJihkjModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public IPropertySelectionModel setJihkjModels() {
		String sql = "select id,mingc from jihkjb";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
}