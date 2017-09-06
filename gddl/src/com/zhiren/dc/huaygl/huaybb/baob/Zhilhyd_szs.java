package com.zhiren.dc.huaygl.huaybb.baob;

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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhilhyd_szs extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
//	Ʒ��������
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	public void setPinzModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"ȫ��"));
		setPinzModel(new IDropDownModel(list,SysConstant.SQL_Pinz_mei));
	}
//	�ƻ��ھ�������
	public IDropDownBean getJihkjValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getJihkjModel().getOptionCount()>0) {
				setJihkjValue((IDropDownBean)getJihkjModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setJihkjValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getJihkjModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setJihkjModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setJihkjModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	public void setJihkjModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"ȫ��"));
		String sql =
			"select id, mingc from jihkjb where mingc<>'��'";

		setJihkjModel(new IDropDownModel(list,sql));
	}
	
//  ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getDcMingc(String id){ 
    	if(id == null || "".equals(id)){
    		return "";
    	}
		JDBCcon con=new JDBCcon();
		String mingc="";
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
//  ���ѡ������ڵ�Ķ�Ӧ�Ĺ�Ӧ������   
    private String[] getGys(String id){ 
    	String[] gys={"ȫ��","-1"};
    	if(id==null || "".equals(id)){
    		return gys;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			gys[0]=rsl.getString("mingc");
			gys[1]=rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
//	ȡ�����ڲ���SQL
    private String getDateParam(){

//		��������
		String rqsql = "";
		if(getBRiq() == null || "".equals(getBRiq())){
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(new Date())+"\n";
		}else{
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq())+"\n";
		}
		if(getERiq() == null || "".equals(getERiq())){
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(new Date())+"+1\n";
		}else{
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(getERiq())+"+1\n";
		}
		return rqsql;
    }
//  ȡ�ù�Ӧ�̲���SQL
    private String getGysParam(){
//		��Ӧ��ú������
		String gyssql = "";
		if("1".equals(getGys(getTreeid())[1])){
			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
		}else if("0".equals(getGys(getTreeid())[1])){
			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
		}
		return gyssql;
    }
//  ȡ��Ʒ�ֲ���SQL
    private String getPinzParam(){
//		Ʒ��sql
		String pzsql = "";
		if(getPinzValue() != null && getPinzValue().getId() != -1){
			pzsql = "and f.pinzb_id = " + getPinzValue().getId() + "\n";
		}
		return pzsql;
    }

//	������
    public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��������:"));
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
//		�糧��
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(80);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
//		��Ӧ����
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		//�ƻ��ھ�
		tb1.addText(new ToolbarText("�ƻ��ھ�:"));
		ComboBox jihkj = new ComboBox();
		jihkj.setTransform("JihkjSelect");
		jihkj.setWidth(70);
		jihkj.setListeners("select:function(own,rec,index){Ext.getDom('JihkjSelect').selectedIndex=index}");
		tb1.addField(jihkj);
		tb1.addText(new ToolbarText("-"));
	
		tb1.addText(new ToolbarText("������λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//		Ʒ��ѡ��
//		tb1.addText(new ToolbarText("Ʒ��:"));
//		ComboBox pinz = new ComboBox();
//		pinz.setTransform("PinzSelect");
//		pinz.setWidth(50);
//		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
//		tb1.addField(pinz);
//		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	private String getDc_Gys_Mk_Pzhz(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
				"select\n" +
				"  decode(grouping(diancxxb_id)+grouping(meikxxb_id),2,'�ܼ�',1,diancxxb_id || 'С��',diancxxb_id) diancxxb_id\n" + 
				"  ,decode(grouping(id)+grouping(diancxxb_id)+grouping(meikxxb_id),1,meikxxb_id || '_С��',meikxxb_id) meikxxb_id\n" + 
				"  ,decode(daohrq,null,meikxxb_id || '_С��',to_char(daohrq,'yyyy-mm-dd'))\n" +
//				"  ,sum(biaoz) biaoz\n" + 
				"  ,sum(sanfsl) sanfsl\n" + 
				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*qnet_ar_mj)/sum(sanfsl)),3) qnet_ar_mj\n" + 
				
//				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*qnet_ar)/sum(sanfsl)),0) qnet_ar\n" + 
				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*mt)/sum(sanfsl)),2) mt\n" + 
				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*mad)/sum(sanfsl)),2) mad\n" + 
				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*vad)/sum(sanfsl)),2) vad\n" + 
				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*std)/sum(sanfsl)),2) std\n" + 
				"  ,round(decode(sum(sanfsl),0,0,sum(sanfsl*aar)/sum(sanfsl)),2) aar\n" + 
//				"  ,mxhedbz,ches\n" +
				"from (\n" + 
				"select distinct f.id,  d.mingc diancxxb_id, m.mingc meikxxb_id,mxhedbz,ches,\n" + 
				"f.daohrq, f.sanfsl, f.biaoz,z.qnet_ar qnet_ar_mj,round(z.qnet_ar/4.1816*1000,0) qnet_ar,z.mt,z.mad,z.vad,z.std,z.aar\n" + 
				"from (select fh.*,(select nvl(max(hedbz),0) hedbz from chepb c where c.fahb_id = fh.id) mxhedbz from fahb fh ) f,\n" + 
				"     gongysb g, meikxxb m, pinzb p, jihkjb j,yunsfsb y, diancxxb d,zhillsb z\n" + 
				"where f.gongysb_id = g.id(+) and f.meikxxb_id = m.id(+) and f.yunsfsb_id=y.id\n" + 
				"  and f.pinzb_id = p.id and f.jihkjb_id = j.id and f.diancxxb_id = d.id\n" +
				getDateParam() + getGysParam() + getPinzParam();
			if(v.isFencb()){
				sql +="and diancxxb_id in(select id from diancxxb where id=" + getTreeid_dc() + " or fuid=" + getTreeid_dc() + ")\n";
			}
			sql +="  and f.zhilb_id=z.zhilb_id\n" + 
				"  )\n" + 
				"  group by rollup(diancxxb_id,meikxxb_id,(daohrq,id,mxhedbz,ches))";
		
		String Htitle = "�볧úú��ͳ����ϸ��";
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
		
		int aw=0;
    	ArrHeader = new String[][] {{"�糧","ú��","��������","����","��ֵ(kg/mj)","ȫˮ(mt)","ˮ��(mad)","�ӷ���(vad)","���(std)","�ҷ�(aar)"} };

		ArrWidth = new int[] {80, 130, 80, 60, 60, 60, 60, 60, 60, 60};

	    aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle(Htitle, ArrWidth);
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

		rt.setBody(new Table(rstmp, 1, 0, 3));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(28);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for(int i = 0;i<rt.body.getRows();i++){
			rt.body.merge(i, 2, i, 3);
		}
		
		rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 7);
		
//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();
	}
	
	public String getPrintTable(){
		return getDc_Gys_Mk_Pzhz();
	}
//	������ʹ�õķ���
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	-------------------------�糧Tree-----------------------------------------------------------------
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
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			setPinzValue(null);
			setPinzModel(null);
			setJihkjValue(null);
			setJihkjModel(null);
			setTreeid_dc(visit.getDiancxxb_id() + "");
			initToolbar();
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
			
		}
		initToolbar();
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