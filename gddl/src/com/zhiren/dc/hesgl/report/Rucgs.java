package com.zhiren.dc.hesgl.report;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
/*
 * ���ߣ�������
 * ʱ�䣺2009-11-01
 * �������볧�Ĺ��յ��� ��
 */
public class Rucgs extends BasePage {
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	 //	ҳ���ж�����
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private String riq;
	boolean riqchange = false;
	
	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String RiqEnd;

	public String getRiqEnd() {
		if (RiqEnd == null || RiqEnd.equals("")) {
			RiqEnd = DateUtil.FormatDate(new Date());
		}
		return RiqEnd;
	}

	public void setRiqEnd(String after) {

		if (this.RiqEnd != null && !this.RiqEnd.equals(after)) {
			this.RiqEnd = after;
			afterchange = true;
		}

	}
	
	// ������
//	public String getRiq() {
//		
//		if (((Visit) this.getPage().getVisit()).getString1() == null 
//				|| ((Visit) this.getPage().getVisit()).getString1().equals("")) {
//			
//			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
//		}
//		return ((Visit) this.getPage().getVisit()).getString1();
//	}
//
//	public void setRiq(String riq1) {
//
//		if (((Visit) this.getPage().getVisit()).getString1() != null 
//				&& !((Visit) this.getPage().getVisit()).getString1().equals(riq1)) {
//			
//			((Visit) this.getPage().getVisit()).setString1(riq1);
//			((Visit) this.getPage().getVisit()).setboolean1(true);
//		}
//	}
//    public String getRiqEnd() {
//		
//		if (((Visit) this.getPage().getVisit()).getString3() == null 
//				|| ((Visit) this.getPage().getVisit()).getString3().equals("")) {
//			
//			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
//		}
//		return ((Visit) this.getPage().getVisit()).getString3();
//	}
//
//	public void setRiqEnd(String riq2) {
//
//		if (((Visit) this.getPage().getVisit()).getString3() != null 
//				&& !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
//			
//			((Visit) this.getPage().getVisit()).setString3(riq2);
//			((Visit) this.getPage().getVisit()).setboolean2(true);
//		}
//	}
	public boolean getRaw() {
		return true;
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
	
	//�õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		
		if(! isBegin){
			return "";
		}
		
		return getCaiggsb();
//		
//		if(mstrReportName.equals(REPORT_JIESTJQUERY)){
//			return getJiestjquery();
//		}else if(mstrReportName.equals(REPORT_JIESTJZB)){
//			return getJiestjzb();
//		}else{
//			return "�޴˱���";
//		}
	}

	public String getCaiggsb(){
		//�ɹ����ձ�		
		_CurrentPage=1;
		_AllPages=1;
		
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String strC_Diancxxb_id = ""; //�糧����
		String strC_Gongys_id = ""; //��Ӧ������
		String having = "";
		int treejib = this.getDiancTreeJib();
		

		if(!this.getTreeid().equals("0")){
			
			strC_Diancxxb_id=" and f.diancxxb_id="+this.getTreeid()+"\n";
		}
		int i=0;
		String sql="";
		String ArrHeader[][]=null;
		int ArrWidth[]=null;
		sql="select decode(gongys,null,'�ϼ�',gongys) as gongys,\n"
			+ " decode(leix,null,decode(gongys,null,'�ϼ�','С��'),leix) as leix,\n"
			+ " decode(meikdw,null,'С��',meikdw) as meikdw,decode(daohrq,null,'С��',to_char(daohrq,'yyyy-mm-dd'))daohrq,\n"
			+ " decode(chec,null,'С��',chec)chec,sum(gusl) as gusl,sum(jingz) as jingz,sum(biaoz) as biaoz,sum(yuns) as yuns,sum(yingk) as yingk,\n"
			+ " decode(sum(gusl),0,0,round(sum(gusl*qnetar)/sum(gusl),0)) as qnetar,\n"
			+ " decode(sum(gusl),0,0,round(sum(gusl*meij)/sum(gusl),2)) as meij,\n"
			+ " decode(sum(gusl),0,0,round(sum(gusl*meijs)/sum(gusl),2)) as meijs,\n"
			+ " decode(sum(gusl),0,0,round(sum(gusl*yunf)/sum(gusl),2)) as yunf,\n"
			+ " decode(sum(gusl),0,0,round(sum(gusl*yunfs)/sum(gusl),2)) as yunfs\n"
			+ " from(\n"
			+ " select '����' as leix,g.mingc as gongys,m.mingc as meikdw,f.daohrq,f.chec,sum(round_new(maoz-piz,0)) as gusl,sum(maoz-piz) as jingz,sum(biaoz) as biaoz,sum(round(f.yuns,2)) as yuns,sum(round(f.yingk,2)) as yingk,\n"
			+ " nvl(round(decode(sum(maoz-piz),0,0,round_new(sum((maoz-piz)*qnet_ar)/sum(maoz-piz),2))/0.0041816,0),0) as qnetar ,\n"
			+ " nvl(to_number(getGusxx(f.id,'meij')),0) as meij,nvl(to_number(getGusxx(f.id,'meis')),0) as meijs,\n"
			+ " nvl(to_number(getGusxx(f.id,'yunf')),0) as yunf,nvl(to_number(getGusxx(f.id,'yunfs')),0) as yunfs\n"
			+ " from fahb f,zhilb z,meikxxb m,gongysb g\n"
			+ " where f.zhilb_id=z.id(+) \n"
			+ " and f.jiesb_id=0 \n"
			+ " and f.meikxxb_id=m.id and m.meikdq_id=g.id and daohrq>=to_date('"+getRiq()+"','yyyy-mm-dd')\n"
			+ " and daohrq<=to_date('"+getRiqEnd()+"','yyyy-mm-dd')\n"
			+ " group by f.id,g.mingc,m.mingc,f.daohrq,f.chec\n"
			+ " union\n"
			+ "  select '����' as leix,g.mingc as gongys,m.mingc as meikdw,f.daohrq,f.chec,sum(round_new(maoz-piz,0)) as gusl,sum(maoz-piz) as jingz,sum(biaoz) as biaoz,sum(round(f.yuns,2)) as yuns,sum(round(f.yingk,2)) as yingk,\n"
			+ "  nvl(round(decode(sum(maoz-piz),0,0,round_new(sum((maoz-piz)*qnet_ar)/sum(maoz-piz),2))/0.0041816,0),0) as qnetar ,\n"
			+ "  nvl(to_number(getGusxx(f.id,'meij')),0) as meij,nvl(to_number(getGusxx(f.id,'meis')),0) as meijs,\n"
			+ "  nvl(to_number(getGusxx(f.id,'yunf')),0) as yunf,nvl(to_number(getGusxx(f.id,'yunfs')),0) as yunfs\n"
			+ "  from fahb f,zhilb z,meikxxb m,gongysb g,jiesb j\n"
			+ "  where f.zhilb_id=z.id(+) and f.jiesb_id=j.id and (j.ruzrq>to_date('"+getRiqEnd()+"','yyyy-mm-dd') or j.ruzrq is null)\n"
			+ "  and f.meikxxb_id=m.id and m.meikdq_id=g.id and daohrq>=to_date('"+getRiq()+"','yyyy-mm-dd')\n"
			+ "  and daohrq<=to_date('"+getRiqEnd()+"','yyyy-mm-dd')\n"
			+ "  group by f.id,g.mingc,m.mingc,f.daohrq,f.chec\n"
			+ " union\n"
			+ " select '����' as leix,g.mingc as gongys,m.mingc as meikdw,f.daohrq,f.chec,sum(round_new(maoz-piz,0)) as gusl,sum(maoz-piz) as jingz,sum(biaoz) as biaoz,sum(round(f.yuns,2)) as yuns,sum(round(f.yingk,2)) as yingk,\n"
			+ " nvl(round(decode(sum(maoz-piz),0,0,round_new(sum((maoz-piz)*qnet_ar)/sum(maoz-piz),2))/0.0041816,0),0) as qnetar ,\n"
			+ " nvl(decode(sum(jiessl),0,0,round(sum(jiessl*j.buhsdj)/sum(jiessl),2)),0) as meij,nvl(nvl(decode(sum(jiessl),0,0,round(sum(jiessl*(j.hansdj-j.buhsdj))/sum(jiessl),2)),0),0) as meijs,\n"
			+ " nvl(nvl(decode(sum(jiessl),0,0,round(sum(jiessl*j.yunfhsdj)/sum(jiessl),2)),0),0) as yunf,nvl(nvl(decode(sum(jiessl),0,0,round(sum(jiessl*j.yunfhsdj*0.07)/sum(jiessl),2)),0),0) as yunfs\n"
			+ " from fahb f,zhilb z,meikxxb m,gongysb g,jiesb j\n"
			+ " where f.zhilb_id=z.id(+) and f.jiesb_id=j.id(+)\n"
			+ " and j.ruzrq is not null and j.ruzrq<=to_date('"+getRiqEnd()+"','yyyy-mm-dd') and daohrq>=to_date('"+getRiq()+"','yyyy-mm-dd')\n"
			+ " and daohrq<=to_date('"+getRiqEnd()+"','yyyy-mm-dd')\n"
			+ " and f.meikxxb_id=m.id and m.meikdq_id=g.id\n"
			+ " group by f.id,g.mingc,m.mingc,f.daohrq,f.chec\n"
			+ " )";
		if(this.getLeixValue().getId()==0){
			sql=sql+" where leix='����'";
			
		}else if(this.getLeixValue().getId()==1){
			sql=sql+" where leix='����'";
		}else if(this.getLeixValue().getId()==-1){
			 
		}
		sql=sql+" group by rollup(gongys,leix,meikdw,daohrq,chec)\n"
			+ " having not(grouping(chec)=1 and grouping(meikdw)<>1)";

		ArrHeader=new String[1][15];
		ArrHeader[0]=new String[] {"ú�����","����","ú��λ","��������","����","������<br>(��)","����<br>(��)","Ʊ��<br>(��)","����<br>(��)","ӯ��<br>(��)","�볧������(Kcal/Kg)","ú��<br>(Ԫ/��)","ú��˰<br>(Ԫ/��)","�˼�<br>(Ԫ/��)","�˼�˰<br>(Ԫ/��)"};
		ArrWidth=new int[] {120,60,80,80,50,80,80,80,75,75,75,75,75,75,75};
		i=3;
		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("�볧����ͳ�Ʊ�",ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 8, "����:"+this.getRiq()+"��"+this.getRiqEnd(), Table.ALIGN_CENTER);
		
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.body.setPageRows(24);
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);

//		ҳ�� 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,4,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			((Visit)getPage().getVisit()).setDropDownBean1(null);	//��Ӧ��
			((Visit)getPage().getVisit()).setProSelectionModel1(null);
			
			((Visit)getPage().getVisit()).setDropDownBean2(null);	//�糧����
			((Visit)getPage().getVisit()).setProSelectionModel2(null);
			((Visit)getPage().getVisit()).setString1("");	//riq
			((Visit)getPage().getVisit()).setString2("");	//Treeid
			((Visit)getPage().getVisit()).setString3("");
			((Visit)getPage().getVisit()).setboolean1(false); //���ڸı�
			((Visit)getPage().getVisit()).setboolean2(false);
		}
		getToolBars();
		if(((Visit)getPage().getVisit()).getboolean1() || ((Visit)getPage().getVisit()).getboolean2() ){
			
			this.getILeixModels();
		}
		isBegin=true;
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(getRiq());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("��"));
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqEnd());
		df2.Binding("RiqEnd", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df2.setId("RiqEnd");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + ((Visit)getPage().getVisit()).getDiancxxb_id(), "forms[0]", null,
				getTreeid());
		((Visit)getPage().getVisit()).setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����:"));
		ComboBox comb = new ComboBox();
		comb.setTransform("LeixDropDown");
		comb.setId("Leix");
		comb.setEditable(false);
		comb.setLazyRender(true);// ��̬��
		comb.setWidth(80);
		comb.setReadOnly(true);
		comb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(comb);
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
//	������λ
	public IDropDownBean getLeixValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getILeixModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setLeixValue(IDropDownBean Value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean1()!=Value){
			
			((Visit)getPage().getVisit()).setboolean1(true);
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setILeixModel(IPropertySelectionModel value) {
		
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getILeixModel() {
		
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			
			getILeixModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getILeixModels() {

		String sql=	"select 0 as id,'����' as mingc from dual union select 1 as id,'����' as mingc from dual";
		
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"ȫ��"));
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
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
		String sql="";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		
		_IDiancmcModel = new IDropDownModel(sql,"ȫ��");
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


	public String getTreeid() {
		if (((Visit) getPage().getVisit()).getString2() == null 
				|| ((Visit) getPage().getVisit()).getString2().equals("")) {
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		if (((Visit) getPage().getVisit()).getString2() != null) {
			if (!treeid.equals(((Visit) getPage().getVisit()).getString2())) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getIDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
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
}
