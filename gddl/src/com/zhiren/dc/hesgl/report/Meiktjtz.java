package com.zhiren.dc.hesgl.report;

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

import com.zhiren.common.ext.ExtTreeUtil;
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
 * @author ����
 * 2009-05-20
 */
public class Meiktjtz  extends BasePage implements PageValidateListener{

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
	
	private static final String BAOBPZB_GUANJZ = "MEIKTJTZ";// baobpzb�ж�Ӧ�Ĺؼ���
	
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
	
//	������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		if (this.eriq!=null){
			if(!this.eriq.equals(eriq)){
				rqchange = true;
			}
		}
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
			visit.setList1(null);
			
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel11(null);
			visit.setDropDownBean11(null);
			
			getJieslxModels();
			setJieslxValue(null);
			getJieslxModel();
			
			getYunsfsModels();
			setYunsfsValue(null);
			getYunsfsModel();
			
			getGongysDropDownModels();
			setGongysDropDownModel(null);
			getGongysDropDownModel();
			
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			this.getSelectData();
		}
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

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		
//		�õ��ꡢ�¡���
		
		String[] a = getBRiq().split("-");
		String[] b = getERiq().split("-");
		String title_bt = b[0]+"��"+b[1]+"�·�("+a[1]+"��"+a[2]+"-"+b[1]+"��"+b[2]+"��)";
		
		StringBuffer strSQL = new StringBuffer();
		String lx = "";
		String fs = "";
		String gongys = "";
		//������������������
		if(getJieslxValue().getValue().equals("ȫ��")){
			lx = " ";
		}else{
			lx = "and j.jieslx = '" + getJieslxValue().getId() + "'\n";
		}
		//���䷽ʽ����������
		if(getYunsfsValue().getValue().equals("ȫ��")){
			fs = " ";
		}else{
			fs = "and j.yunsfsb_id = '" + getYunsfsValue().getId() + "'\n";
		}
		//��Ӧ��ú��Tree����
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				gongys = " and 1=2\n";
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " and g.id ="+getTreeid() + "\n";
				}else {
					gongys = " and m.id ="+getTreeid() + "\n";
				}
			}
		}
		String diancxxb_id="";
		if(v.isFencb()){
			diancxxb_id = this.getTreeid_dc();
		}else{
			diancxxb_id = v.getDiancxxb_id()+"";
		}
		
		String sql_mx = "select g.mingc as gongys,\n" + 
		"       m.mingc as kuangm,\n" + 
		"       getjiesdzb('jiesb' ,j.id ,'��������' ,'hetbz') as hetml,\n" + 
		"       getjiesdzb('jiesb' ,j.id ,'Qnetar' ,'hetbz') as hetrz,\n" + 
		"       j.hetj as hetmj,\n" + 
		"       j.jiessl as jiesml,\n" + 
		"       j.ches,\n" + 
		"       j.jiesrl as shijrz,\n" + 
		"       j.jieslf as shijlf,\n" + 
		"       getjiesdzb('jiesb' ,j.id ,'Std','zhejbz') as liufkk,\n" + 
		"       getjiesdzb('jiesb' ,j.id ,'Qnetar','zhejbz') as rezjf,\n" + 
		"       j.hansdj as jiesmj,\n" + 
		"       j.buhsmk as shijmj,\n" + 
		"       j.shuik as shuij,\n" + 
		"       j.hansmk as hej\n" + 
		"from jiesb j,gongysb g,meikxxb m\n" + 
		"where j.gongysb_id = g.id\n" + 
		"      and j.meikxxb_id = m.id\n" + 
		" 	   and j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq()) +
		" 	   and j.jiesrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
		"	   and j.diancxxb_id = "+diancxxb_id+"\n"+
		gongys + 
		lx + 
		fs;
		
		
		String sql = 

			"select * from\n" +
			"((select '�ϼ�' as gongys,\n" + 
			"       null as kuangm,\n" + 
			"       '' as hetml,\n" + 
			"       '' as hetrz,\n" + 
			"       null as hetmj,\n" + 
			"       sum(j.jiesml) as jiesml,\n" + 
			"       sum(j.ches) as ches,\n" + 
			"       decode(sum(j.jiesml),null,0,round_new(sum(j.shijrz*j.jiesml)/sum(j.jiesml),0)) as shijrz,\n" + 
			"       decode(sum(j.jiesml),null,0,round_new(sum(j.shijlf*j.jiesml)/sum(j.jiesml),0)) as shijlf,\n" + 
			"       to_char(sum(liufkk)) as liufkk,\n" + 
			"       decode(sum(j.jiesml),null,'0',to_char(round_new(sum(j.rezjf*j.jiesml)/sum(j.jiesml),0))) as rezjf,\n" + 
			"       decode(sum(j.jiesml),null,0,to_char(round_new(sum(j.jiesmj*j.jiesml)/sum(j.jiesml),0))) as jiesmj,\n" + 
			"       sum(j.shijmj) as shijmj,\n" + 
			"       sum(j.shuij) as shuij,\n" + 
			"       sum(j.hej) as hej\n" +
			"from\n" + 
			"(select g.mingc as gongys,\n" + 
			"       m.mingc as kuangm,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'��������' ,'hetbz') as hetml,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'Qnetar' ,'hetbz') as hetrz,\n" + 
			"       j.hetj as hetmj,\n" + 
			"       j.jiessl as jiesml,\n" + 
			"       j.ches,\n" + 
			"       j.jiesrl as shijrz,\n" + 
			"       j.jieslf as shijlf,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'Std','zhejbz') as liufkk,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'Qnetar','zhejbz') as rezjf,\n" + 
			"       j.hansdj as jiesmj,\n" + 
			"       j.buhsmk as shijmj,\n" + 
			"       j.shuik as shuij,\n" + 
			"       j.hansmk as hej\n" + 
			"from jiesb j,gongysb g,meikxxb m\n" + 
			"where j.gongysb_id = g.id\n" + 
			"      and j.meikxxb_id = m.id\n" + 
			" 	   and j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   and j.jiesrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"	   and j.diancxxb_id = "+diancxxb_id+"\n"+
			gongys + 
			lx + 
			fs + 
			") j)\n" + 
			"union\n" + 
			"(select g.mingc as gongys,\n" + 
			"       m.mingc as kuangm,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'��������' ,'hetbz') as hetml,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'Qnetar' ,'hetbz') as hetrz,\n" + 
			"       j.hetj as hetmj,\n" + 
			"       j.jiessl as jiesml,\n" + 
			"       j.ches,\n" + 
			"       j.jiesrl as shijrz,\n" + 
			"       j.jieslf as shijlf,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'Std','zhejbz') as liufkk,\n" + 
			"       getjiesdzb('jiesb' ,j.id ,'Qnetar','zhejbz') as rezjf,\n" + 
			"       j.hansdj as jiesmj,\n" + 
			"       j.buhsmk as shijmj,\n" + 
			"       j.shuik as shuij,\n" + 
			"       j.hansmk as hej\n" + 
			"from jiesb j,gongysb g,meikxxb m\n" + 
			"where j.gongysb_id = g.id\n" + 
			"      and j.meikxxb_id = m.id\n" + 
			" 	   and j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   and j.jiesrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"	   and j.diancxxb_id = "+diancxxb_id+"\n"+
			gongys + 
			lx + 
			fs + 
			")) x\n" + 
			"order by decode(x.gongys,'�ϼ�',0,1) desc,x.gongys,x.kuangm";

		ResultSetList rsl1 = con.getResultSetList(sql_mx);
		if(rsl1.next()){
			strSQL.append(sql);
		}else{
			strSQL.append(sql_mx);
		}

		ResultSetList rstmp = con.getResultSetList(strSQL.toString());
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			rt.setTitle(Htitle, ArrWidth);
			rt.setDefaultTitle(1, 15, title_bt, Table.ALIGN_CENTER);
			rsl.close();
		} else {
			rs = rstmp;
			
			ArrHeader =new String[1][15];
			ArrHeader[0]=new String[] {"��Ӧ��","����","��ͬú��<br> (��)","��ͬ��ֵ<br> (��)","��ͬú��<br>(Ԫ/��)","����ú��<br> (��)","����<br> (��)","ʵ����ֵ<br> (MJ/MG)","ʵ�����<br> (%)","��ݿۿ�<br> (Ԫ)","��ֵ����<br> (Ԫ)","����ú��<br> (Ԫ/��)","ʵ��ú��<br> (Ԫ)","˰��<br> (Ԫ)","�ϼ�<br> (Ԫ)"};
			
			ArrWidth = new int[] {150,150,90,90,75,75,60,60,60,75,75,75,75,75,90};

			rt.setTitle("����СҤúú�ѽ����", ArrWidth);
			rt.setDefaultTitle(1, 15, title_bt, Table.ALIGN_CENTER);
		}
//		ArrWidth = new int[] { 100,100,400 };
//		rt.setTitle("��������ѯ", ArrWidth);
		

		rt.setBody(new Table(rs, 1, 0, 1));
//		rt.setBody(tb);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRowCol();//�Զ��ϲ�

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(2, 2, "���ܣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "�Ʊ�", Table.ALIGN_LEFT);
		
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��������:"));	
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "fid");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("riq1");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "fid");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("riq2");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		//��Ӧ��
		String gongys_sql = 
			"select id, mingc, level jib\n" +
			"  from (select 0 id, 'ȫ��' mingc, -1 fuid\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select gys.id, gys.mingc, 0 fuid\n" + 
			"          from gongysb gys, gongysdcglb gysgl,jiesb j\n" + 
			"         where gys.id = gysgl.gongysb_id\n" + 
			"           and j.gongysb_id = gys.id\n" + 
			" 	   		and j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   		and j.jiesrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"           and gysgl.diancxxb_id = dcid\n" + 
			"        union\n" + 
			"        select m.id, m.mingc, glb.gongysb_id as fuid\n" + 
			"          from meikxxb m, gongysmkglb glb, gongysb gys, gongysdcglb glbdc,jiesb j\n" + 
			"         where m.id = glb.meikxxb_id\n" + 
			"           and glb.gongysb_id = gys.id\n" + 
			"           and j.meikxxb_id = m.id\n" + 
			" 	   		and j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   		and j.jiesrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"           and gys.id = glbdc.gongysb_id\n" + 
			"           and glbdc.diancxxb_id = dcid\n" + 
			"           )\n" + 
			" where fuid = -1\n" + 
			"    or (fuid = 0 and level = 2)\n" + 
			"    or (fuid > 0 and level = 3)\n" + 
			"connect by NOCYCLE fuid = PRIOR id\n";
		
		DefaultTree dt = new DefaultTree();
		dt.setTree_window(gongys_sql,"gongysTree",""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		if(visit.isFencb()){
//			�糧Tree
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
		}
		
		tb1.addText(new ToolbarText("��������:"));
		ComboBox lx = new ComboBox();
		lx.setTransform("JieslxDropDown");
		lx.setEditable(true);
		lx.setWidth(100);
		lx.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(lx);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox fs = new ComboBox();
		fs.setTransform("YunsfsDropDown");
		fs.setEditable(true);
		fs.setWidth(100);
		fs.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(fs);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){document.Form0.submit();}");
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

//	��������
	public IDropDownBean getJieslxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setJieslxValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getJieslxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getJieslxModels() {

		String sql = 
			"select id,mingc from feiylbb\n" +
			"where diancxxb_id = " + ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n" + 
			"order by id\n";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
//	���䷽ʽ
	public IDropDownBean getYunsfsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean11((IDropDownBean) getYunsfsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setYunsfsValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean11() != value) {

			((Visit) getPage().getVisit()).setDropDownBean11(value);
		}
	}

	public void setYunsfsModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {

			getYunsfsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getYunsfsModels() {

		String sql = 
			"select id,mingc from yunsfsb order by id\n";

		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}
	
	
//	��Ӧ��ú��Tree
//	��ȡ��Ӧ��
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
    public void getGongysDropDownModels() {
    	String sql=
    		"(select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id() + ")\n" +
    		"union\n" +
    		"(select 0 as id,'ȫ��' as mingc from dual)\n";
//    		"select distinct v.id, v.mingc\n" +
//    		"from vwgongysmk v,jiesb j,gongysb g,meikxxb m\n" + 
//    		"where v.diancxxb_id = " + ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n" + 
//    		"      and j.gongysb_id = g.id\n" + 
//    		"      and j.meikxxb_id = m.id\n" + 
//    		"      and (g.mingc = v.mingc or m.mingc = v.mingc)\n" + 
//    		" 	   and j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq()) +
//			" 	   and j.jiesrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n";

        setGongysDropDownModel(new IDropDownModel(sql,"ȫ��")) ;
        return ;
    }
	
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
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		
		if(getTree_dc()==null){
			return "";
		}
		return getTree_dc().getScript();
	}
	
	
}