package com.zhiren.dc.huocfcjse;

/*
 * ����:zl
 * ʱ��:2009-8-10
 * �޸�����:���ӷ�����������ѯ����
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-20 17��57
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class HuocfcjReport extends BasePage {

	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}
	protected void initialize() {
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
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// ������
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

	// ҳ��仯��¼
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

//	ú��������
	public IDropDownBean getKuangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getKuangbModel().getOptionCount()>0) {
				setKuangbValue((IDropDownBean)getKuangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setKuangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getKuangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setKuangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setKuangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setKuangbModels() {
		String sql=" select rownum id,mingc from (select distinct to_char(riq,'YYYY-MM-DD HH24:mi:ss') mingc from fancjghb \n" +
		"where to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')<=to_date('"+this.getERiq()+"','yyyy-MM-dd') and " +
				"to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')>=to_date('"+this.getBRiq()+"','yyyy-MM-dd')) order by mingc desc";
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"ȫ��"));
		setKuangbModel(new IDropDownModel(list,sql));
	}
	
//	��ʾ��ʽ������
	public IDropDownBean getXiansfsValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getXiansfsModel().getOptionCount()>0) {
				setXiansfsValue((IDropDownBean)getXiansfsModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setXiansfsValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getXiansfsModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setXiansfsModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setXiansfsModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setXiansfsModels() {
		String diancid = "" ;
		StringBuffer sb = new StringBuffer();
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"����"));
		list.add(new IDropDownBean(2,"��ϸ"));
		setXiansfsModel(new IDropDownModel(list));
	}
	
	// ����������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	private String REPORT_NAME_ZIBCCX = "HuocfcjReport";// zhillsb�е�����

	/**2008-10-18 huochaoyuan
	*����һ������ú���ɶ��������ŵ����������ԭ�ȱ�����ʾ�����⣬�������һ����ʽ�ı���
	*������getMeizjyrb_zhilb_1()
**/
	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	// private String leix = "";

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHuocfcjReport();
		
	}

	// �����ձ�(�볧)zhillsb�е�����
	/*
	 * ���������� maoz-piz�޸�Ϊ laimsl ��������Լ�趨������Լ����ȫˮ����������Լ��Ϊ����
	 * �޸ķ�Χ getMeizjyrb(),getMeizjyrb_zhilb(),String getMeizjyrb_zhilb_1()
	 * �޸����ڣ�2008-12-04
	 * �޸��ˣ����� 
	 */
	
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
	private String getHuocfcjReport() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String b="";
		String sql="";
		if(getKuangbValue().getId()==-1){
			b = "";
		}else{
			b = " and to_char(riq,'yyyy-mm-dd hh24:mi:ss')='"+getKuangbValue().getValue()+"' \n";
		}
		// DateUtil custom = new DateUtil();
		if(getXiansfsValue().getId()==1){
			sql = "select to_char(riq, 'yyyy-mm-dd hh24:mi:ss') as riq, \n"
				+ "m.mingc as meik,p.mingc as pinz,count(*) as ches, \n"
				+ "sum(maoz) as maoz,sum(piz) as piz,sum(maoz - piz) as jingz \n"
				+ "from fancjghb f, meikxxb m, pinzb p \n"
				+ "where riq>="+DateUtil.FormatOracleDate(getBRiq())+" and riq<="+DateUtil.FormatOracleDate(getERiq())+"+1 \n"
				+ b
				+ "and f.meikxxb_id = m.id(+) \n"
				+ "and f.pinzb_id = p.id(+) \n"
				+ "group by riq,m.mingc,p.mingc \n"
				+ "union  \n"
				+ "select '����' as riq,'' as meik,'' as pinz,count(*) as ches, \n"
				+ "sum(maoz) as maoz,sum(piz) as piz,sum(maoz - piz) as jingz \n"
				+ "from fancjghb f \n"
				+ "where riq>="+DateUtil.FormatOracleDate(getBRiq())+" and riq<="+DateUtil.FormatOracleDate(getERiq())+"+1 \n"
				+ b
				+ "order by riq";

		}else {
		 sql = "select decode(grouping(riq) + grouping(cheph) + grouping(f.id), \n"
			 + "3,'�ۼ�',to_char(riq, 'yyyy-mm-dd hh24:mi:ss')) as riq,m.mingc,p.mingc, \n"
			 + "decode(grouping(riq) + grouping(cheph) + grouping(f.id), \n"
			 + "3,'',2,'С��',cheph) as cheph, \n"
			 + "sum(maoz) maoz,sum(piz) piz,sum(maoz - piz) jingz \n"
			 + "from fancjghb f,meikxxb m,pinzb p \n"
			 + "where riq>="+DateUtil.FormatOracleDate(getBRiq())+" and riq<="+DateUtil.FormatOracleDate(getERiq())+"+1 \n"
			 + b
			 + "and f.meikxxb_id=m.id(+) and f.pinzb_id=p.id(+) \n"
			 + "group by rollup(riq, f.id, cheph,m.mingc,p.mingc) \n"
			 + "having not(grouping(f.id) =0 and grouping(p.mingc)<>0)";
		 
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		
		
		ResultSetList rstmp = con.getResultSetList(buffer.toString());
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		int aw=0;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='Zibcxx' order by xuh");
        ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='Meizjyrb'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
    		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
//        	-----------------------------  �¼ӵķ��� -------------------------------------------------------

    	    ArrHeader = new String[1][7];

    		ArrHeader[0] = new String[] { "����", "ú��","Ʒ��","����", "ë��","Ƥ��", "����"};
    	    ArrWidth = new int[7];

    		ArrWidth = new int[] {120,100,100,100,100,100,100};

    		aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
    		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
    		rt.setTitle("�� �� �� �� �� ͳ ��"
    				+ ((getChangbValue().getId() > 0 && getChangbModel()
    						.getOptionCount() > 2) ? "("
    						+ getChangbValue().getValue() + ")" : ""), ArrWidth);
    		rt.title.setRowHeight(2, 40);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

//    		rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
    		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

    	    strFormat = new String[] { "", "", "", "", ""};
     

        }

		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 7; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		// rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

		rt.createDefautlFooter(ArrWidth);

//		rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(16, 4, "�Ʊ�:", Table.ALIGN_LEFT);
//		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
//		RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"ú  ��  ��  ��  ��  ��","Meizjyrb");

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(24);

		return rt.getAllPagesHtml();

	}
	
	
	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
//	��ť�ļ����¼�
	private boolean _SearchChick = false;
	public void SearchButton(IRequestCycle cycle) {
		_SearchChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			setKuangbValue(null);
			setKuangbModel(null);
			setKuangbModels();
			getSelectData();
		}
		if (_SearchChick) {
			_SearchChick = false;
			getSelectData();
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
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	
	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		if (visit.isFencb()) {
//			tb1.addText(new ToolbarText("����:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb
//					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
		
		
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
		
//		tb1.addText(new ToolbarText("�糧:"));
//		tb1.addField(tf1);
//		tb1.addItem(tb3);
//		
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ʼ����:"));
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
		ToolbarButton rbtn = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����ʱ��:"));
		ComboBox kuangb = new ComboBox();
		kuangb.setTransform("KuangbSelect");
		kuangb.setWidth(150);
		kuangb.setListeners("select:function(own,rec,index){Ext.getDom('KuangbSelect').selectedIndex=index}");
		tb1.addField(kuangb);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��ѯ��ʽ:"));
		ComboBox xiansfs = new ComboBox();
		xiansfs.setTransform("XiansfsSelect");
		xiansfs.setWidth(150);
		xiansfs.setListeners("select:function(own,rec,index){Ext.getDom('XiansfsSelect').selectedIndex=index}");
		tb1.addField(xiansfs);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton sbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('SearchButton').click();}");
		sbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(sbtn);
		tb1.addFill();
//		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
		
//		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ","function(){document.getElementById('RefurbishButton').click();}");
//		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
//		tb1.addItem(rbtn);
//		tb1.addFill();
//		// tb1.addText(new ToolbarText("<marquee width=300
//		// scrollamount=2></marquee>"));
//		setToolbar(tb1);
	}

	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		return getToolbar().getRenderScript();
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setString1("");
			setChangbValue(null);
			setChangbModel(null);
			setKuangbValue(null);
			setKuangbModel(null);
			setXiansfsValue(null);
			setXiansfsModel(null);
			
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
				
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
		// mstrReportName="diaor04bb";
		// if (mstrReportName.equals("Meizjyrb")) {
		// leix = "1";
		// } else if (mstrReportName.equals("Meizjyrb_zhilb")) {
		// leix = "2";
		// }
		blnIsBegin = true;
	}

}
