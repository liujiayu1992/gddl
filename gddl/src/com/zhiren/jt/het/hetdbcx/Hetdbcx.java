package com.zhiren.jt.het.hetdbcx;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;


import org.apache.tapestry.contrib.palette.SortMode;
/*
 *  sy
 * 2009-08-26
 * �޸ĵ糧���򣬰�xuh����*/

public class Hetdbcx  extends BasePage implements PageValidateListener{
//	�ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
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
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
			+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}
	private String userName="";

	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
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
			Refurbish();
		}
	}


	private void Refurbish() {
		//Ϊ"ˢ��" ��ť��Ӵ������
		isBegin=true;
		getHetdbcx();
	}

//	******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			isBegin=true;
			visit.setList1(null);
			this.setTreeid(null);
			this.setNianfValue(null);
			this.getNianfModels();

		}

		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}
	private String RT_HET="hetdbcx";
	private String mstrReportName="hetdbcx";
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getHetdbcx();
		}else{
			return "�޴˱���";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}
	
	private boolean isBegin=false;


	private String getHetdbcx(){//ȡ���ſ� leix��2

		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="��ͬ�ԱȲ�ѯ";
		//String strGongsID = "";
		int jib=this.getDiancTreeJib();
		/*if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}*/
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		
		StringBuffer grouping_sql = new StringBuffer();
		StringBuffer where_sql = new StringBuffer();
		StringBuffer rollup_sql = new StringBuffer();
		StringBuffer having_sql = new StringBuffer();
		StringBuffer orderby_sql = new StringBuffer();

		StringBuffer strSQL = new StringBuffer();
		
		if(jib==1){
			grouping_sql.append(" select decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,g.mingc,\n");

			where_sql.append(" ");
			rollup_sql.append(" group by rollup (dc.fgsmc,dc.mingc,g.mingc) ");
			having_sql.append("");
			orderby_sql.append("  order by grouping(dc.fgsmc) desc ,dc.fgsmc,grouping(dc.mingc) desc ,max(dc.xuh),grouping(g.mingc) desc,max(g.xuh),g.mingc ");
		}else if(jib==2){
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//ȼ�Ϲ�˾
					grouping_sql.append(" select decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'�ܼ�',\n");
					grouping_sql.append("2,dc.rlgsmc,1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,g.mingc,\n");

					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.rlgsmc,dc.fgsmc,dc.mingc,g.mingc) ");
					having_sql.append("");
					orderby_sql.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc,grouping(dc.fgsmc) desc ,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,max(g.xuh),g.mingc\n ");
				}else{
					grouping_sql.append(" select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',\n");
					grouping_sql.append("1,dc.fgsmc,'&nbsp;&nbsp;'||dc.mingc) as danw,g.mingc,\n");

					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.fgsmc,dc.mingc,g.mingc) ");
					having_sql.append("");
					orderby_sql.append(" order by grouping(dc.fgsmc) desc ,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,max(g.xuh),g.mingc\n ");
				}
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
		}else if(jib==3){
			grouping_sql.append(" select decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as danw,g.mingc,\n");

			where_sql.append(" and dc.id=").append(this.getTreeid());//.append("  and riq="+riq+" \n");
			rollup_sql.append(" group by rollup (dc.mingc,g.mingc) \n");
			having_sql.append(" ");
			orderby_sql.append(" order by grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,max(g.xuh),g.mingc\n ");
		} 
		
		
		strSQL.append(grouping_sql.toString());

		strSQL.append(" nvl(round_new(sum(jn.hetl),2),0) as hetl_jn,\n");
		strSQL.append(" nvl(round_new(sum(qn.hetl),2),0) as hetl_qn,\n");
		strSQL.append(" nvl(round_new(sum(jn.hetl),2),0)-nvl(round_new(sum(qn.hetl),2),0) as hetl_cz, \n");
		
		strSQL.append("       round_new(nvl(decode(sum(jn.hetl),0,0,sum(jn.rel*jn.hetl)/sum(jn.hetl)),0),0) as rel_jn, \n");
		strSQL.append("       round_new(nvl(decode(sum(qn.hetl),0,0,sum(qn.rel*qn.hetl)/sum(qn.hetl)),0),0) as rel_qn, \n");
		strSQL.append("       round_new(nvl(decode(sum(jn.hetl),0,0,sum(jn.rel*jn.hetl)/sum(jn.hetl)),0)-nvl(decode(sum(qn.hetl),0,0,sum(qn.rel*qn.hetl)/sum(qn.hetl)),0),0) as rel_cz, \n");
		
		strSQL.append(" round_new(nvl(decode(sum(jn.hetl),0,0,sum(jn.jiag*jn.hetl)/sum(jn.hetl)),0),2) as jiag_jn,\n");
		strSQL.append(" round_new(nvl(decode(sum(qn.hetl),0,0,sum(qn.jiag*qn.hetl)/sum(qn.hetl)),0),2) as jiag_qn,\n");
		strSQL.append(" round_new(nvl(decode(sum(jn.hetl),0,0,sum(jn.jiag*jn.hetl)/sum(jn.hetl)),0),2)-round_new(nvl(decode(sum(qn.hetl),0,0,sum(qn.jiag*qn.hetl)/sum(qn.hetl)),0),2) as jiag_cz \n");
		
		strSQL.append("from \n");
		
		//��ͷ
		strSQL.append("(select distinct n.diancxxb_id,n.gongysb_id \n");
		strSQL.append("  from niandhtqkb n  \n");
		strSQL.append(" where (n.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and n.riq<=to_date('"+intyear+"-12-31','yyyy-mm-dd'))");
		strSQL.append("  or (n.riq>=to_date('"+(intyear-1)+"-01-01','yyyy-mm-dd') and n.riq<=to_date('"+(intyear-1)+"-12-31','yyyy-mm-dd'))) t, \n");
		//����
		strSQL.append("(select a.diancxxb_id,a.gongysb_id,decode(sum(hetl),0,0,sum(rel*hetl)/sum(hetl)) as rel,  \n");
		strSQL.append(" 		round(decode(sum(hetl),0,0,sum(jiag*hetl)/sum(hetl)),2) as jiag,sum(hetl) as hetl from \n");
		strSQL.append("  (select max(n.rez) rel, \n");
		strSQL.append("         max(n.chebjg) jiag, \n");
		strSQL.append("         n.diancxxb_id, \n");
		strSQL.append("         n.gongysb_id, \n");
		strSQL.append("         sum(n.hej) hetl \n");
		strSQL.append("    from niandhtqkb n \n");
		strSQL.append("   where n.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and n.riq<=to_date('"+intyear+"-12-31','yyyy-mm-dd')  \n");
//		strSQL.append("         and hetb.diancxxb_id "++" \n");
		strSQL.append("   group by  \n");
		strSQL.append("            n.diancxxb_id, \n");
		strSQL.append("            n.gongysb_id, \n");
		strSQL.append("            n.hangid) a \n");
		strSQL.append("   group by a.diancxxb_id,a.gongysb_id) jn, \n");
		//ȥ��
		strSQL.append("(select a.diancxxb_id,a.gongysb_id,decode(sum(hetl),0,0,sum(rel*hetl)/sum(hetl)) as rel,  \n");
		strSQL.append(" 	round(decode(sum(hetl),0,0,sum(jiag*hetl)/sum(hetl)),2) as jiag,sum(hetl) as hetl from \n");
		strSQL.append("  (select max(n.rez) rel, \n");
		strSQL.append("         max(n.chebjg) jiag, \n");
		strSQL.append("         n.diancxxb_id, \n");
		strSQL.append("         n.gongysb_id, \n");
		strSQL.append("         sum(n.hej) hetl \n");
		strSQL.append("    from niandhtqkb n \n");
		strSQL.append("   where n.riq>=to_date('"+(intyear-1)+"-01-01','yyyy-mm-dd') and n.riq<=to_date('"+(intyear-1)+"-12-31','yyyy-mm-dd')  \n");
//		strSQL.append("         and hetb.diancxxb_id "++" \n");
		strSQL.append("   group by  \n");
		strSQL.append("            n.diancxxb_id, \n");
		strSQL.append("            n.gongysb_id, \n");
		strSQL.append("            n.hangid) a \n");
		strSQL.append("   group by a.diancxxb_id,a.gongysb_id) qn,vwdianc dc,gongysb g \n");
		strSQL.append("where t.diancxxb_id=dc.id and t.gongysb_id=g.id and  \n");
		strSQL.append("      t.diancxxb_id=jn.diancxxb_id(+) and t.diancxxb_id=qn.diancxxb_id(+)  and  \n");
		strSQL.append("      t.gongysb_id=jn.gongysb_id(+) and t.gongysb_id=qn.gongysb_id(+)  \n");
		strSQL.append(where_sql.toString());
		strSQL.append(rollup_sql.toString());
		strSQL.append(having_sql.toString());
		strSQL.append(orderby_sql.toString());

		
		//chh 2008-12-16 ��ǰ�գ������滻Ϊ�������ڵ���
		ArrHeader =new String[2][11];
		ArrHeader[0]=new String[] {"�糧����","������λ","��ͬú��","��ͬú��","��ͬú��","��ͬú��","��ͬú��","��ͬú��","��ͬú��","��ͬú��","��ͬú��"};
		ArrHeader[1]=new String[] {"�糧����","������λ",""+intyear,""+(intyear-1),"����",""+intyear,""+(intyear-1),"����",""+intyear,""+(intyear-1),"����"};

		ArrWidth =new int[] {120,150,60,60,60,40,40,50,40,40,50};

//		System.out.println(strSQL.toString());
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		rt.setBody(new Table(rs,2, 0, 2));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3,""+intyear+"��", Table.ALIGN_CENTER);
//		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(15);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.body.setColFormat(3, "0.00");
		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		//ҳ�� 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,1,"��λ:������",Table.ALIGN_LEFT);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

	//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}

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

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setId("nianf");
		nianf.setWidth(60);
		tb1.addField(nianf);

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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
	
	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

}