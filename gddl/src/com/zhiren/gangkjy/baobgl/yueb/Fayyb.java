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
 * 
 * @author ����
 *
 */
public class Fayyb  extends BasePage implements PageValidateListener{

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
	
	
	// ���������
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�������
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	//���ڿؼ�����
	
	
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
			visit.setList1(null);
			setTreeid(null);
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
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		StringBuffer sb = new StringBuffer();
//		 ����������ݺ��·�������
		long intyear;
		String StrMonth = "";
		long intMonth;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
			
		} else {
			StrMonth = "" + intMonth;
			
		}
		String date1 = intyear + "-" +StrMonth + "-01";
		String date2 = intyear + "-01-01";
		//����ʱ��
//		DateUtil.FormatDate(new Date());

		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";
		}

		// ������ϸ��SQL
		String sqltable =
			"select decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as fahdw,\n" +
			"        decode(grouping(g.mingc)+grouping(dc.mingc),1,'�ϼ�',g.mingc) as gongys,\n" + 
			"       decode(grouping(dc2.mingc)+grouping(g.mingc),1,'С��',dc2.mingc) as shouhr,\n" + 
			"       tb2.fenx as leix,\n" + 
			"       ybb.pinz,\n" + 
			"       decode(grouping(ybb.pinz),1,'',max(ybb.chec)) chec,\n" + 
			"       decode(grouping(ybb.pinz),1,'',max(ybb.chuanm)) chuanm,\n" + 
			"       decode(grouping(ybb.pinz),1,'',max(ybb.faz)) faz,\n" + 
			"       decode(grouping(ybb.pinz),1,'',max(ybb.daoz)) daoz,\n" + 
			"       sum(ybb.meil) as meil,\n" + 
			"       decode(sum(ybb.meil),0,0,sum(ybb.rel)/sum(ybb.meil)) as rel\n" + 
			"from\n" + 
			"(select f.diancxxb_id as fahd, f.gongysb_id as gys,f.shr_diancxxb_id as shr,pz.mingc pinz,decode(1,1,'����','����') as leix,f.chec as chec, lc.mingc as chuanm,fz.mingc as faz,\n" + 
			"dz.mingc as daoz,f.fahrq as fahrq, f.daohrq as daohrq,sum(f.meil) as meil,sum(f.meil*zl.qnet_ar) as rel\n" + 
			"from fayslb f,diancxxb dc, vwpinz pz, luncxxb lc, vwchez fz, vwchez dz, zhilb zl\n" + 
			"where f.diancxxb_id=dc.id and f.pinzb_id=pz.id(+) and lc.id(+)=f.luncxxb_id and fz.id(+)=f.faz_id and dz.id(+)=f.daoz_id and zl.id(+)=f.zhilb_id\n" + 
			"and f.fahrq>=to_date('"+date1+"','yyyy-MM-dd') and f.fahrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1 "+str+" \n"+
			"group by(f.diancxxb_id, f.gongysb_id,f.shr_diancxxb_id ,pz.mingc,decode(1,1,'����','����') ,f.chec, lc.mingc,fz.mingc,dz.mingc,f.fahrq, f.daohrq)\n" + 
			"union\n" + 
			"select f.diancxxb_id as fahd, f.gongysb_id as gys,f.shr_diancxxb_id as shr,pz.mingc pinz,decode(1,1,'�ۼ�','�ۼ�') as leix,f.chec as chec, lc.mingc as chuanm,fz.mingc as faz,\n" + 
			"dz.mingc as daoz,f.fahrq as fahrq, f.daohrq as daohrq,sum(f.meil) as meil,sum(f.meil*zl.qnet_ar) as rel\n" + 
			"from fayslb f,diancxxb dc, vwpinz pz, luncxxb lc, vwchez fz, vwchez dz, zhilb zl\n" + 
			"where f.diancxxb_id=dc.id and f.pinzb_id=pz.id(+) and lc.id(+)=f.luncxxb_id and fz.id(+)=f.faz_id and dz.id(+)=f.daoz_id and zl.id(+)=f.zhilb_id\n" + 
			"and f.fahrq>=to_date('"+date2+"','yyyy-MM-dd') and f.fahrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1 "+str+" \n"+
			"group by(f.diancxxb_id, f.gongysb_id,f.shr_diancxxb_id ,pz.mingc,decode(1,1,'����','����') ,f.chec, lc.mingc,fz.mingc,dz.mingc,f.fahrq, f.daohrq)\n" + 
			")ybb,\n" + 
			"(select * from (select distinct z.diancxxb_id as fahd, z.gongysb_id as gys, z.shr_diancxxb_id as shr\n" + 
			"  from fayslb z, diancxxb dc, zhilb zl\n" + 
			"   where dc.id=z.diancxxb_id and zl.id(+)=z.zhilb_id\n" + 
			"	and z.fahrq>=to_date('"+date2+"','yyyy-MM-dd') and z.fahrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1 "+str+" \n"+
			"    group by(z.diancxxb_id,z.gongysb_id,z.shr_diancxxb_id)) sj,\n" + 
			"(select decode(1,1,'����','����') fenx from dual union select decode(2,2,'�ۼ�','�ۼ�')from dual) fx\n" + 
			" ) tb2, vwgongys g,diancxxb dc2, diancxxb dc\n" + 
			"where ybb.fahd(+)=tb2.fahd\n" + 
			"and tb2.gys=ybb.gys(+)\n" + 
			"and tb2.shr=ybb.shr(+)\n" + 
			"and ybb.leix(+)=tb2.fenx\n" + 
			"and tb2.fahd=dc.id\n" + 
			"and g.id=tb2.gys\n" + 
			"and dc2.id=tb2.shr\n" + 
			"group by rollup(tb2.fenx,dc.mingc,g.mingc,(dc2.mingc,ybb.pinz))\n" + 
			"having not grouping(tb2.fenx)=1\n" + 
			"order by grouping(dc.mingc) desc,fahdw,grouping(g.mingc) desc, gongys,grouping(dc2.mingc) desc, shouhr,grouping(tb2.fenx) desc,leix";

		sb.append(sqltable);
		

			ResultSet rs = con.getResultSet(sb,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			
			String[][] ArrHeader = new String[1][13];
			ArrHeader[0] = new String[] {"�����ص�","��Ӧ��","�ջ���","����/�ۼ�","Ʒ��","����/����","����","��վ","��վ","ú��","������(Qnet_ar)"};

			int[] ArrWidth = new int[13];
			ArrWidth = new int[] { 100, 100, 100, 60, 60, 70,90,60, 60, 60, 60 };
			


			// ����
			
			Table tb=new Table(rs, 1, 0, 9);
			rt.setBody(tb);
			String []strFormat=new String[]{"","","","","","","","","","0.00","0.00"};
//			�糧��Ϣ��̶� �ػʵ���Ϊ501
			String biaot="";
			if(getTreeid().equals("501")){
				biaot="װ �� �� ��";
			}else{
				biaot="�� �� �� ��";
			}
			rt.setTitle(biaot, ArrWidth);
			rt.title.setRowHeight(2, 40);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:"
					+ visit.getDiancqc(),
					Table.ALIGN_LEFT);
			rt.setDefaultTitle(4,3, "�±�����:"
					+ date1,
					Table.ALIGN_CENTER);
			rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.setColFormat(strFormat);
			rt.body.setColAlign(10, Table.ALIGN_RIGHT);
			rt.body.setColAlign(11, Table.ALIGN_RIGHT);
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
			rt.createDefautlFooter(ArrWidth);

			rt.setDefautlFooter(1, 3, "�Ʊ�����:"
					+ DateUtil.FormatDate(new Date()),
					Table.ALIGN_LEFT);
			rt.setDefautlFooter(4, 2, "����:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 2, "���:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(8, 3, "�Ʊ�:", Table.ALIGN_LEFT);
			rt.footer.setRowCells(1, Table.PER_FONTSIZE, 10);
//			rt.createFooter(1, ArrWidth);
//			rt.setDefautlFooter(1, 2, "�ܼ�:", Table.ALIGN_LEFT);

				_CurrentPage=1;
				_AllPages=rt.body.getPages();
				if (_AllPages==0){
					_CurrentPage=0;
				}
			

			con.Close();
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
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��λ����:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
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
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
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

	
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


}