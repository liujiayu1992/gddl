package com.zhiren.dc.hesgl.qitjs;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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

public class Jiestjcx extends BasePage implements PageValidateListener{

	boolean riqchange = false;
	private String riq;
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
	private String after;
	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}
	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

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
	
	public boolean getRaw() {
		return true;
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getRucmzjyyb();
	}
	
	private boolean _QueryClick = false;
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	private String getRucmzjyyb() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
//		String s="";
//		if(!this.hasDianc(this.getTreeid_dc())){
//			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//���� ����������;
//		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select a.gmingc" +
//				",a.hhetbh" +
				"	   ,getHtmlAlert('"+ MainGlobal.getHomeContext(this)+ "','Shenhrz','hetb_id',a.hid,a.hhetbh)\n" +
				"	   ,decode(a.ymingc,null,'�����˷�',a.ymingc) ymingc\n" +
//				"      ,decode(a.yhetbh,null,'notransport',a.yhetbh) yhetbh" +
				"      ,decode(a.yhetbh,null,'notransport',getHtmlAlert('"+ MainGlobal.getHomeContext(this)+ "','Yunshtshrz','hetys_id',a.yhid,a.yhetbh)) yhetbh" +
				"	   ,a.meikdwmc\n" + 
				"      ,a.lmingc,a.chec,a.czmingc,to_char(a.kaobrq,'yyyy-mm-dd') kaobrq,to_char(a.daohrq,'yyyy-mm-dd') daohrq,yundl,a.xieml,a.yingkd\n" + 
				"      ,a.yuns,a.shisl,a.jiessl\n" + 
				
//				"      ,b.bianm" +
				"	   ,getHtmlAlert('"+ MainGlobal.getHomeContext(this)+ "','Jiesdcz','lx','dianc'||',' ||b.bianm,b.bianm)\n" +
				"	   ,b.jiesrq,b.ruzrq,b.hansdj,b.hetj,b.jiesrl,b.gongfrl,b.zhejbzrl,b.rlc,b.zhejjerl\n" + 
				"      ,b.jieslf,b.gongfl,b.zhejbzl,b.lc,b.zhejjel,b.buhsmk,b.shuik,b.hansmk,b.bukmk\n" + 
//				"      ,c.bianm" +
				"	   ,getHtmlAlert('"+ MainGlobal.getHomeContext(this)+ "','Jiesdcz','lx','dianc'||',' ||c.bianm,c.bianm)\n" +
				"	   ,c.jiesrq,c.ruzrq,c.yunj,c.yunf,c.zaf,c.buhsyf,c.hansyf,hej\n" + 
				"from\n" + 
				"(\n" + 
				"select j.id jid, y.id yid,y.diancjsmkb_id, max(f.id) fid,h.id hid,y.hid yhid,g.mingc gmingc,h.hetbh hhetbh,j.meikdwmc,l.mingc lmingc\n" + 
				"      ,ff.chec,cz.mingc czmingc,ff.kaobrq,ff.daohrq,sum(f.biaoz) yundl\n" + 
				"      ,j.yingd-j.kuid yingkd,j.yuns,sum(f.maoz) shisl,j.jiessl\n" + 
				"      ,x.xieml,y.ymingc,y.hetbh yhetbh\n" + 
				" from jiesb j,fahb f,fahb ff,gongysb g,hetb h,luncxxb l,chezxxb cz,diancxxb d\n" + 
				"     ,(select f.id,decode(sum(l.xieml),null,0,sum(l.xieml)) xieml\n" + 
				"         from fahb f,luncxmb l\n" + 
				"        where f.id=l.fahb_id(+)\n" + 
				"        group by f.id) x\n" + 
				"     ,(select j.id,j.diancjsmkb_id, h.id hid,y.mingc ymingc ,h.hetbh\n" + 
				"         from jiesyfb j,hetys h,yunsdwb y\n" + 
				"        where j.hetb_id=h.id\n" + 
				"          and j.jieslx=3\n" + 
				"          and h.yunsdwb_id=y.id) y\n" + 
				"where f.jiesb_id=j.id\n" + 
				"  and ff.gongysb_id=g.id(+)\n" + 
				"  and ff.hetb_id=h.id(+)\n" + 
				"  and ff.luncxxb_id=l.id(+)\n" + 
				"  and ff.faz_id=cz.id(+)\n" + 
				"  and ff.id=x.id\n" + 
				"  and j.id=y.diancjsmkb_id(+)\n" + 
				"  and ff.gongysb_id = " + getTongjfsValue().getId() + 
				"  and ff.diancxxb_id = d.id\n" +
				"  and (ff.diancxxb_id=" + getTreeid_dc() + "\n"+
				"   or d.fuid = " + getTreeid_dc() + ")\n"+ 
				"  and ff.daohrq >= to_date('" + getRiq() + "', 'yyyy-mm-dd')\n" + 
				"  and ff.daohrq <= to_date('" + getAfter() + "', 'yyyy-mm-dd')\n" +
				"group by j.id,ff.id,j.meikdwmc,g.mingc,h.hetbh,l.mingc\n" + 
				"        ,ff.chec,cz.mingc,ff.kaobrq,ff.daohrq,j.yingd,j.kuid,j.yuns\n" + 
				"        ,j.jiessl,x.xieml,y.id,y.ymingc,y.hetbh,h.id,y.hid,y.diancjsmkb_id\n" + 
				"having max(f.id)=ff.id\n" + 
				") a,\n" + 
				"(\n" + 
				"select j.id,\n" + 
				"        j.bianm,\n" + 
				"        to_char(j.jiesrq,'yyyy-mm-dd') jiesrq,\n" + 
				"        to_char(j.ruzrq,'yyyy-mm-dd') ruzrq,\n" + 
				"        j.hansdj,\n" + 
				"        j.hetj,\n" + 
				"        j.jiesrl,\n" + 
				"        getjiesdzb('jiesb',j.id,'Qnetar','gongf') gongfrl,\n" + 
				"        getjiesdzb('jiesb',j.id,'Qnetar','zhejbz') zhejbzrl,\n" + 
				"        getjiesdzb('jiesb',j.id,'Qnetar','changf')-getjiesdzb('jiesb',j.id,'Qnetar','gongf') rlc,\n" + 
				"        getjiesdzb('jiesb',j.id,'Qnetar','zhejje') zhejjerl,\n" + 
				"        j.jieslf,\n" + 
				"        getjiesdzb('jiesb',j.id,'std','gongf') gongfl,\n" + 
				"        getjiesdzb('jiesb',j.id,'std','zhejbz') zhejbzl,\n" + 
				"        getjiesdzb('jiesb',j.id,'std','changf')-getjiesdzb('jiesb',j.id,'std','gongf') lc,\n" + 
				"        getjiesdzb('jiesb',j.id,'std','zhejje') zhejjel,\n" + 
				"        j.buhsmk,\n" + 
				"        j.shuik,\n" + 
				"        j.hansmk,\n" + 
				"        j.bukmk\n" + 
				"from jiesb j\n" + 
				") b,\n" + 
				"(\n" + 
				"select j.id,\n" + 
				"        j.bianm ,\n" + 
				"        to_char(j.jiesrq,'yyyy-mm-dd') jiesrq,\n" + 
				"        to_char(j.ruzrq,'yyyy-mm-dd') ruzrq,\n" +  
				"        j.yunj ,\n" + 
				"        j.guotyf + j.kuangqyf yunf,\n" + 
				"        j.guotzf + j.kuangqzf zaf,\n" + 
				"        j.buhsyf ,\n" + 
				"        j.hansyf ,\n" + 
				"        j.guotyf + j.kuangqyf + j.guotzf + j.kuangqzf hej\n" + 
				"from jiesyfb j\n" + 
				") c\n" + 
				"where a.jid=b.id(+)\n" + 
				"  and a.yid=c.id(+)\n" +
				"order by a.gmingc");
		
		ResultSetList rstmp = con.getResultSetList(sql.toString());
		int[] ArrWidth=null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		ArrHeader = new String[2][44];
		ArrHeader[0] = new String[] { "������", "�ɹ���ͬ���", "���䵥λ", "�����ͬ���","���","��������","����"	,"װ��","��������"
				,"�벴����","�˵���<br>(��)","ʵж��<br>(��)","ӯ��<br>(��)","����<br>(��)","ʵ����<br>(��)","��������<br>(��)"
				,"ú�����","ú�����","ú�����","ú�����","ú�����","ú�����"
				,"ú�����","ú�����","ú�����","ú�����"
				,"ú�����","ú�����","ú�����","ú�����","ú�����"
				,"ú�����","ú�����","ú�����","ú�����"
				,"�˷ѽ���","�˷ѽ���","�˷ѽ���","�˷ѽ���","�˷ѽ���","�˷ѽ���","�˷ѽ���","�˷ѽ���","�˷ѽ���"
				};
		ArrHeader[1] = new String[] { "������", "�ɹ���ͬ���", "���䵥λ", "�����ͬ���","���","��������","����"	,"װ��","��������"
				,"�벴����","�˵���<br>(��)","ʵж��<br>(��)","ӯ��<br>(��)","����<br>(��)","ʵ����<br>(��)","��������<br>(��)"
				,"���","��������","��������","�����<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��������<br>(Kcal/kg)"
				,"��������(Kcal/kg)","�����ۼ۱�׼<br>(Kcal/kg)","������<br>(Kcal/kg)","�����۽��(Ԫ)"
				,"������(Ԫ)","������(%)","���ۼ۱�׼(%)","���(%)","���۽��(Ԫ)"
				,"����˰ú��(Ԫ)","ú��˰(Ԫ)","ú��ϼ�(Ԫ)","��ú��(Ԫ)"
				,"������","��������","��������","�˼�(Ԫ/��)","�˷�(Ԫ)","���ӷ�(Ԫ)","����˰�˷�(Ԫ)","�˷�˰(Ԫ)","���ӷѺϼ�(Ԫ)"
				};
		ArrWidth = new int[44];
		ArrWidth = new int[] { 100, 130, 100, 130, 80, 70, 30, 60, 70, 
								70, 50, 50, 40, 40, 50, 40,
								
								130, 70, 70, 60, 60, 50,
								50, 50, 50, 60, 
								50, 50, 50, 50, 60,
								60, 60, 60, 60, 
								
								130, 70, 70, 60, 60, 60, 60, 60, 60};
		strFormat = new String[44];
		strFormat = new String[] {
								"", "", "","", "", "","", "", "","",
								"0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
								
								"","","","0.00","0.00",
								"0","0","0.00","0","0",
								"0.00","0.00","0.00","0.00",
								"0","0","0","0","0" ,
								"","","","0","0","0","0","0","0" };
		rt.setTitle(v.getDiancqc() +"<br>ȼ�Ͻ���ͳ�Ʋ�ѯ", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE,18);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.setDefaultTitle(1, 5, "�벴����:" + getRiq() + "��" + getAfter(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rstmp, 2, 0, 10));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.createDefautlFooter(ArrWidth);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		rt.setDefautlFooter(40, 4, "��ӡ����:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(30, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(1, 2, "�Ʊ�:", Table.ALIGN_LEFT);

		// ����ҳ��
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		con.Close();
		rt.body.setRowHeight(35);
//		RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"ú�ʼ����±�","Rucmzjyyb");
		return rt.getAllPagesHtml();
	}

	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();
		//�糧
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
		tb1.addText(new ToolbarText("�벴����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("������λ:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("TONGJFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
		"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);

		setToolbar(tb1);

	}
	
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
//    private boolean hasDianc(String id){ 
//		JDBCcon con=new JDBCcon();
//		boolean mingc= false;
//		String sql="select mingc from diancxxb where fuid = " + id;
//		ResultSetList rsl=con.getResultSetList(sql);
//		if(rsl.next()){
//			mingc=true;
//		}
//		rsl.close();
//		return mingc;
//	}
	
//	 ͳ�Ʒ�ʽ������
	private IDropDownBean TongjfsValue;
	public IDropDownBean getTongjfsValue() {
		if (TongjfsValue == null) {
			TongjfsValue = (IDropDownBean) TongjfsModel.getOption(0);
		}
		return TongjfsValue;
	}
	public void setTongjfsValue(IDropDownBean Value) {
		if (!(TongjfsValue == Value)) {
			TongjfsValue = Value;
		}
	}
	private IPropertySelectionModel TongjfsModel;
	public void setTongjfsModel(IPropertySelectionModel value) {
		TongjfsModel = value;
	}
	public IPropertySelectionModel getTongjfsModel() {
		if (TongjfsModel == null) {
			getTongjfsModels();
		}
		return TongjfsModel;
	}
	public IPropertySelectionModel getTongjfsModels() {
		String sql = "select 0 id,'ȫ��' mingc from dual union select distinct g.id,g.mingc from jiesb j,gongysb g where j.gongysb_id=g.id";
		TongjfsModel = new IDropDownModel(sql);
		return TongjfsModel;
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

	public String getcontext() {
		return "";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}
	// Page����
	protected void initialize() {
		_pageLink = "";
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
				setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
				setTongjfsValue(null);
				setTongjfsModel(null);
		}
		blnIsBegin = true;
		getSelectData();
	}
	
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
