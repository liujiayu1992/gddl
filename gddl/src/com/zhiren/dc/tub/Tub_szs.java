package com.zhiren.dc.tub;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;

import com.zhiren.common.DateUtil;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
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
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Tub_szs extends BasePage implements PageValidateListener{
	private static final String kucm_rb_zx="ú̿�������ͼ";
	private static final String rucmrz_rb_zx="�볧ú��ֵ����ͼ";
	private static final String rucmrz_rb_zz="����¶ȹ�ú����״ͼ";
	
	
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	��ʼ��
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	��ǰҳ
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}
//	����ҳ
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages = _value;
	}
//	ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
//	������
	public String getBRiq() {
		return ((Visit)getPage().getVisit()).getString2();
	}
	public void setBRiq(String briq) {
		((Visit)getPage().getVisit()).setString2(briq);
	}
//	������
	public String getERiq() {
		return ((Visit)getPage().getVisit()).getString3();
	}
	public void setERiq(String eriq) {
		((Visit)getPage().getVisit()).setString3(eriq);
	}
	
	public boolean getRaw() {
		return true;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getToolbar();
		}
	}

	public String getPrintTable() {
		if (kucm_rb_zx.equals(getLeixValue().getValue())) {
			return getKucChart();
		}else if(rucmrz_rb_zx.equals(getLeixValue().getValue())){
			return getRucrzrbChart();
		}else if(rucmrz_rb_zz.equals(getLeixValue().getValue())){
			return getKuangfydgmlChart();
		}else{
			return "";
		}
	}
//	ú̿�������ͼ
	public String getKucChart(){
		StringBuffer sbsql = new StringBuffer();
		long lngDays = DateUtil.getQuot(getERiq(), getBRiq());
		sbsql.append(
				"select x.riq,x.fenx,shuj.zhi from (\n" +
				"select s.riq, d.mingc fenx, s.kuc zhi\n" + 
				"  from shouhcrbb s, diancxxb d\n" + 
				" where s.diancxxb_id = d.id\n" + 
				"   and to_char(s.riq, 'yyyy-mm-dd') >= '"+getBRiq()+"'\n" + 
				"   and to_char(s.riq, 'yyyy-mm-dd') < '"+getERiq()+"'\n" + 
				") shuj,\n" + 
				"(select * from\n" + 
				"(select to_date('"+getERiq()+"','yyyy-mm-dd')-rownum as riq from all_objects where rownum<="+lngDays+"),\n" + 
				"(select mingc fenx from diancxxb where id in(509,510))) x\n" + 
				"where shuj.fenx(+)=x.fenx\n" + 
				"   and shuj.riq(+)=x.riq\n" + 
				"order by x.riq,x.fenx");
		return getZheXChart(sbsql.toString(),kucm_rb_zx);
	}
//	�볧ú��ֵ����ͼ
	public String getRucrzrbChart(){
		StringBuffer sbsql = new StringBuffer();
		String diancid = "and f.diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n";
		sbsql.append(
				"select * from (\n" +
				"select f.daohrq riq,'�ط�' fenx ,decode(sum(f.sanfsl),0,0,round(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),3)) zhi\n" + 
				"  from zhilb z, fahb f, meikxxb m\n" + 
				" where f.zhilb_id = z.id and f.meikxxb_id=m.id\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') >= '"+getBRiq()+"'\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') < '"+getERiq()+"'\n" + 
				"   and m.jihkjb_id=2\n" + //�ط�
				diancid +
				"group by f.daohrq\n" + 
				"union\n" + 
				"select f.daohrq riq,'ͳ��' fenx ,round(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),3) zhi\n" + 
				"  from zhilb z, fahb f, meikxxb m\n" + 
				" where f.zhilb_id = z.id and f.meikxxb_id=m.id\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') >= '"+getBRiq()+"'\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') < '"+getERiq()+"'\n" + 
				"   and m.jihkjb_id=1\n" + //ͳ��
				diancid +
				"group by f.daohrq\n" + 
				") order by riq,fenx");
		return getZheXChart(sbsql.toString(),rucmrz_rb_zx);
	}
//	����¶ȹ�ú����״ͼ
	public String getKuangfydgmlChart(){
		StringBuffer sbsql = new StringBuffer();
		String diancid = "and f.diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n";
		
		sbsql.append("select quanc||' '||mingc as mingc,fenx,zhi shuj from   \n");
		sbsql.append(" (select dc.mingc as quanc,m.mingc, '��Ӧ' as fenx,sum(f.sanfsl) as zhi \n");
		sbsql.append(" from fahb f, meikxxb m,diancxxb dc \n");
		sbsql.append("where f.meikxxb_id = m.id \n");
		sbsql.append("and f.daohrq >= to_date('" + getBRiq() + "', 'yyyy-mm-dd') \n");
		sbsql.append("and f.daohrq <= to_date('" + getERiq() + "', 'yyyy-mm-dd') \n");
		sbsql.append("and dc.id=f.diancxxb_id \n");
		sbsql.append(diancid);
		sbsql.append("group by dc.mingc,m.mingc) s order by s.quanc");
		return getZhuZChart(sbsql.toString(),rucmrz_rb_zz);
	}
//	����ͼ
	public String getZheXChart(String sql,String title){
		JDBCcon cn = new JDBCcon();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		Report rt=new Report();
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
		
		ResultSet rs=cn.getResultSet(sql.toString());
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","riq", "zhi");//rs��¼����������ͼƬ��Ҫ������
		/*--------------����ͼƬ������ʼ-------------------*/
		ct.intDigits=0;			//	��ʾС��λ��
		ct.xfontSize=9;		//	x�������С
		ct.dateApeakShowbln = false;//��ֱ��ʾx�������
//		ct.lineDateFormatOverride = "dd";//x�������ֻ��ʾ��
		/*--------------����ͼƬ��������-------------------*/		
		ArrHeader[0]=new String[] {""+ct.ChartTimeGraph(getPage(), data2, title + "("+getBRiq()+"~"+getERiq()+")", 900, 445)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		cn.Close();

		return rt.getHtml();
	}
//	��״ͼ
	public String getZhuZChart(String sql,String title){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
			
		ResultSet rs=cn.getResultSet(sql);
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", "shuj");//rs��¼����������ͼƬ��Ҫ������
		/*--------------����ͼƬ������ʼ-------------------*/
		ct.intDigits=0;			//	��ʾС��λ��
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//��б��ʾX�������
		/*--------------����ͼƬ��������-------------------*/		
		//������״ͼ����ʾ��ҳ��
		ArrHeader[0]=new String[] {""+ct.ChartBar3D(getPage(), dataset,title+"("+getBRiq()+"~"+getERiq()+")", 900, 445)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		cn.Close();
		return rt.getHtml();
	}
	
	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		---------------------------
		tb1.addText(new ToolbarText("����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
//		----------------------------
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
//		----------------------------
		tb1.addText(new ToolbarText("����:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("LeixSelect");
		leix.setWidth(150);
		leix.setListeners("select:function(own,rec,index){Ext.getDom('LeixSelect').selectedIndex=index}");
		tb1.addField(leix);
//		----------------------------
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		setToolbar(tb1);
	}
	
//	-------------------------------------
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
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	public void setDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
	}
//	-------------------------------------------
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	--------------------------------------------
//	����������
	public IDropDownBean getLeixValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getLeixModel().getOptionCount()>0) {
				setLeixValue((IDropDownBean)getLeixModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setLeixValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getLeixModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setLeixModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setLeixModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	public void setLeixModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0,kucm_rb_zx));
		list.add(new IDropDownBean(1,rucmrz_rb_zx));
		list.add(new IDropDownBean(2,rucmrz_rb_zz));
		list.add(new IDropDownBean(3,"X"));
		list.add(new IDropDownBean(4,"X"));
		setLeixModel(new IDropDownModel(list));
	}
//	--------------------------------------------
	
//	 ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -15, DateUtil.AddType_intDay)));
			setERiq(DateUtil.FormatDate(new Date()));
			setDiancmcModels();

			visit.setString4(null);
			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString4(pagewith);
			}
			getToolBars();
		}
		
		getToolBars();
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
	
}